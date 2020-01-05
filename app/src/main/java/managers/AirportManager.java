package managers;

import com.example.flightstats.Airport;
import com.example.flightstats.Utils;
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

    public Airport getAirportWithCode(String value){
        for (Airport a: mAirportList
             ) {
            if(a.getIcao().equals(value)){

                return a;
            }
        }
        return null;
    }

    public Airport getAirportByIndex(int index){
        return mAirportList.get(index);
    }

}
