package pt.ulusofona.ecati.deisi.licenciatura.cm1920.grupo11.ui.listeners

interface OnTouchListener :
    OnClickListener {

    fun onSwipeLeftEvent(data: Any?)

    fun onSwipeRightEvent(data: Any?)
}