package com.example.flightstats.corse.univ.myapplication.data

data class AircraftData(
        var icao : String? = null,
        var callsign: String? = null,
        var origin_country: String? = null,
        var time_position: Long = 0,
        var last_contact: Long = 0,
        var longitude: Float?,
        var latitude: Float?,
        var baro_altitude: Float?,
        var on_ground: Boolean = true,
        var velocity: Float?,
        var true_track: Float?,
        var vertical_rate: Float?,
        var geo_altitude: Float?)

