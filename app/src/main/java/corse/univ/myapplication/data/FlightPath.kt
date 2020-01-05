package com.example.flightstats.corse.univ.myapplication.data

data class FlightPath(
    var startTime: Long = 0,
    var lat: Float?,
    var lng: Float?,
    var baro_altitude: Float?,
    var true_track: Long = 0,
    var on_ground: Boolean = false)