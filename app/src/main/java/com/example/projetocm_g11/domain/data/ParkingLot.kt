package com.example.projetocm_g11.domain.data

import java.util.*

class ParkingLot(
    val type: Type,
    val name: String,
    val address: String
) {

    var lastUpdatedAt: Date
        private set

    /* Number of occupied parking spots (%) */
    var capacity: Int = 100
        set(value) {

            field = if(value >= 0 || value <= 100) { value } else capacity
            lastUpdatedAt = Date()
        }

    /* Returns state in String format according to capacity */
    fun getState(): String {

        if(capacity == 100) { return "Lotado"}

        return if(capacity >= 90) {
            "Potencialmente lotado"

        } else "Livre"
    }

    init {
        lastUpdatedAt = Date()
    }
}

enum class Type {

    SURFACE, UNDERGROUND
}