package com.example.flightstats.corse.univ.myapplication.data

//Flight info retrieved to show on MapFlight Fragment
data class FlightTrack (var icao24: String? = null,
    var callsign: String? = null,
    var startTime: Long = 0,
    var endTime: Long = 0,
    var path: List<FlightPath>? = null)
