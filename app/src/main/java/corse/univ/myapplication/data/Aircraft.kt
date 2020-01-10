package com.example.flightstats.corse.univ.myapplication.data

//Object given when we ask for plane info about its current state
//Used in live tracking fragment
data class Aircraft(
        var time: Long = 0,
        var states: AircraftData? = null)