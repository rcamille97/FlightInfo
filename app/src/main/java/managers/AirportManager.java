package managers;

import android.content.Context;
import android.util.Log;

import com.example.flightstats.Airport;
import com.example.flightstats.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AirportManager {

    private List<Airport> mAirportList;

    private static AirportManager airportManager;

    private AirportManager() {
        mAirportList = Utils.Companion.generateAirportList();
    }

    public static AirportManager getInstance() {

        if(airportManager == null)
            airportManager = new AirportManager();
        return airportManager;
    }



    public List<Airport> getAirportList() {
        return mAirportList;
    }

    public Airport getAirportByIndex(int index){
        return mAirportList.get(index);
    }

}
