package com.rahul.spinanimation

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpinLayoutManager(
    context: Context,
    private val spinScroller: SpinScroller
) : LinearLayoutManager(
    context,
    RecyclerView.VERTICAL, false
) {
    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State?,
        position: Int
    ) {
        spinScroller.targetPosition = position
        startSmoothScroll(spinScroller)
    }
}
