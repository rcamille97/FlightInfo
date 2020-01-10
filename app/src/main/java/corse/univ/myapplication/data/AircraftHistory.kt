package com.example.flightstats.corse.univ.myapplication.data

data class AircraftHistory(
        var departure : String? = null,
        var arrival: String? = null,
        var departureDate: Long = 0,
        var arrivalDate: Long = 0,
        var callsign: String? = null
)