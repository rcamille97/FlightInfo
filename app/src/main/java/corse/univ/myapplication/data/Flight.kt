package com.example.flightstats.corse.univ.myapplication.data

//Information about a Flight, used in FlightList fragment
class Flight {
    var icao24: String? = null
    var firstSeen: Long = 0
    var estDepartureAirport: String? = null
    var lastSeen: Long = 0
    var estArrivalAirport: String? = null
    var callsign: String? = null
    var estDepartureAirportHorizDistance: Long = 0
    var estDepartureAirportVertDistance: Long = 0
    var estArrivalAirportHorizDistance: Long = 0
    var estArrivalAirportVertDistance: Long = 0
    var departureAirportCandidatesCount: Int = 0
    var arrivalAirportCandidatesCount: Int = 0
}