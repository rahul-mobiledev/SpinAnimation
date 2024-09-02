package com.rahul.spinanimation

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearSmoothScroller

class SpinScroller(
    context: Context,
    private val scrollSpeed: Float,
    private val decelerationDelay: Float
) : LinearSmoothScroller(context) {
    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
        return 500f.div(scrollSpeed).div(displayMetrics.densityDpi)
    }


    override fun calculateTimeForDeceleration(velocity: Int): Int {
        val decelerationTime = super.calculateTimeForDeceleration(velocity)
        return (decelerationTime.times(decelerationDelay)).toInt() // Example: make it 20% faster deceleration
    }
}
