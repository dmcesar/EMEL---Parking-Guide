package com.example.projetocm_g11.ui.listeners

interface OnTouchEvent : OnClickEvent {

    fun onSwipeEvent(data: Any?, direction: Int)
}