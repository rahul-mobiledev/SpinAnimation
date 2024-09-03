package com.rahul.spinanimation

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RawRes
import androidx.annotation.StyleRes
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.SnapHelper

class SpinView @JvmOverloads constructor(
    context: Context,
    private val attributeSet: AttributeSet? = null,
    defStyledArray: Int = 0
) : FrameLayout(context, attributeSet, defStyledArray) {

    private var scrollSpeed: Float = 1f
    private var decelerationDelay: Float = 1f
    private var itemScale: Float = 0f
    private var itemHeight: Float = 100f
    private var vibrationDuration: Long = 0L

    @StyleRes
    private var textStyle: Int = 0

    @RawRes
    private var scrollSound: Int = 0

    private val snapHelper: SnapHelper by lazy {
        LinearSnapHelper()
    }

    private val spinAdapter: SpinViewAdapter by lazy {
        SpinViewAdapter(textStyle, itemHeight)
    }

    private val spinScroller: SpinScroller by lazy {
        SpinScroller(
            context = context,
            scrollSpeed = scrollSpeed,
            decelerationDelay = decelerationDelay
        )
    }

    private val spinLayoutManager: SpinLayoutManager by lazy {
        SpinLayoutManager(context, spinScroller)
    }

    private val items: MutableList<String> by lazy {
        mutableListOf()
    }

    private val audioAttributes: AudioAttributes by lazy {
        AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
    }

    private val soundPool: SoundPool by lazy {
        SoundPool.Builder()
            .setMaxStreams(Int.MAX_VALUE) // Only one stream to prevent overlapping sounds
            .setAudioAttributes(audioAttributes)
            .build()
    }

    private val soundId: Int? by lazy {
        if (scrollSound == 0) {
            null
        } else {
            soundPool.load(context, scrollSound, 1)
        }
    }

    private val onChildItemScrolled: (Int) -> Unit by lazy {
        { _ ->
            soundId?.let {
                soundPool.play(it, 1f, 1f, 1, 0, 1f)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    (vibrator as? VibratorManager)?.apply {
                        vibrate(
                            CombinedVibration.createParallel(
                                VibrationEffect.createOneShot(vibrationDuration, 250)
                            )
                        )
                    }
                } else {
                    (vibrator as? Vibrator)?.apply {
                        vibrate(vibrationDuration)
                    }
                }
            }
        }
    }

    private val vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    private val onScrollListener: OnScrollListener by lazy {
        object : OnScrollListener() {
            private var lastPlayedPosition: Int = -1

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentFirstPosition = spinLayoutManager.findFirstVisibleItemPosition()
                if (lastPlayedPosition != currentFirstPosition) {
                    lastPlayedPosition = currentFirstPosition
                    onChildItemScrolled(lastPlayedPosition)
                }
                val centerY = recyclerView.height / 2
                for (i in 0 until recyclerView.childCount) {
                    val child = recyclerView.getChildAt(i)
                    child?.let { scaleItem(it, centerY, recyclerView) }
                }
            }
        }
    }

    private val recyclerView: RecyclerView by lazy {
        RecyclerView(context).apply {
            adapter = spinAdapter
            snapHelper.attachToRecyclerView(this)
            layoutManager = spinLayoutManager
            addOnScrollListener(onScrollListener)
        }
    }

    init {
        obtainAttributes()
        addView(recyclerView)
    }

    private fun scaleItem(view: View, centerY: Int, recyclerView: RecyclerView) {
        val itemCenterY = (view.top + view.bottom) / 2
        val distanceFromCenter = Math.abs(centerY - itemCenterY)
        val scaleFactor = 1 - (distanceFromCenter.toFloat() * 2 / recyclerView.height)
        val scaleValue = itemScale + (scaleFactor * (1 - itemScale))
        view.scaleX = scaleValue
        view.scaleY = scaleValue
    }

    private fun obtainAttributes() {
        context.obtainStyledAttributes(attributeSet, R.styleable.SpinView).apply {
            scrollSpeed = getFloat(R.styleable.SpinView_scrollSpeedMultiplier, scrollSpeed)
            decelerationDelay =
                getFloat(R.styleable.SpinView_decelerationDelay, decelerationDelay)
            itemScale = getFloat(R.styleable.SpinView_edgeItemScale, itemScale)
            textStyle = getResourceId(R.styleable.SpinView_textStyle, textStyle)
            itemHeight = getDimension(R.styleable.SpinView_itemHeight, itemHeight)
            scrollSound = getResourceId(R.styleable.SpinView_scrollSound, scrollSound)
            vibrationDuration = getFloat(
                R.styleable.SpinView_vibrationDuration,
                vibrationDuration.toFloat()
            ).toLong()
            recycle()
        }
    }

    fun setDataItems(data: List<String>) {
        spinAdapter.setItems(data)
        items.apply {
            clear()
            addAll(data)
        }
    }

    fun startInfiniteScroll() {
        with(recyclerView) {
            scrollToPosition(0)
            smoothScrollToPosition(Int.MAX_VALUE)
        }
    }

    fun setResult(result: String) {
        val position = spinLayoutManager.findFirstVisibleItemPosition()
        val actualPositionIndex = position % items.size
        val index = items.indexOfFirst { it == result } + 1
        val resultPosition = position + (3 * items.size) - actualPositionIndex + index
        with(recyclerView) {
            stopScroll()
            smoothScrollToPosition(resultPosition)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }
}
