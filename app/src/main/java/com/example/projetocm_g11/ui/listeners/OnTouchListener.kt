package com.example.projetocm_g11.ui.listeners

interface OnTouchListener : OnClickListener {

    fun onSwipeLeftEvent(data: Any?)

    fun onSwipeRightEvent(data: Any?)
}