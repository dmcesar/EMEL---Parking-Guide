package pt.ulusofona.ecati.deisi.ui.listeners

interface OnTouchListener :
    OnClickListener {

    fun onSwipeLeftEvent(data: Any?)

    fun onSwipeRightEvent(data: Any?)
}