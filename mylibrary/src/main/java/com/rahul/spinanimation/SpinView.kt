package com.rahul.spinanimation

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
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

    @StyleRes
    private var textStyle: Int = 0

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

    private val onScrollListener: OnScrollListener by lazy {
        object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
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
