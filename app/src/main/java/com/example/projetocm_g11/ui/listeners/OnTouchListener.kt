package com.example.projetocm_g11.ui.listeners

interface OnTouchListener : OnClickListener {

    fun onSwipeEvent(data: Any?, direction: Int)
}