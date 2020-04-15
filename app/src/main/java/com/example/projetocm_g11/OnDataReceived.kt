package com.example.projetocm_g11

import com.example.projetocm_g11.domain.data.ParkingLot

interface OnDataReceived {

    fun onDataReceived(list: ArrayList<ParkingLot>)
}