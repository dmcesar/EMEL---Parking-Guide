package com.example.projetocm_g11.ui.listeners

import com.example.projetocm_g11.data.local.entities.ParkingLot

interface OnDataReceivedWithOrigin {

    fun onDataReceivedWithOrigin(data: ArrayList<ParkingLot>, updated: Boolean)
}