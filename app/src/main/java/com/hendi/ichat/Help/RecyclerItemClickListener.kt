package com.hendi.penetasantelur.Model

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

 class RecyclerItemClickListener (mContext :Context, private val mListener : OnItemClickListener?) : RecyclerView.OnItemTouchListener {

     internal var mGestureDetector: GestureDetector

     override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
         val childView = rv.findChildViewUnder(e.x,e.y)
         if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)){
             mListener.onItemClick(childView,rv.getChildAdapterPosition(childView))
         }
         return false
     }

     override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
     }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    init {
        mGestureDetector = GestureDetector(mContext, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }
        })
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }

}


