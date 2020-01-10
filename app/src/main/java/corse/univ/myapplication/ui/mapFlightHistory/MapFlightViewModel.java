package corse.univ.myapplication.ui.mapFlightHistory;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.flightstats.corse.univ.myapplication.data.Flight;
import com.example.flightstats.corse.univ.myapplication.data.FlightPath;
import com.example.flightstats.corse.univ.myapplication.data.FlightTrack;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MapFlightViewModel extends AndroidViewModel {

    private static final String TAG = MapFlightViewModel.class.getSimpleName();

    MutableLiveData<FlightTrack> mapFlightLiveData = new MutableLiveData<>();

    public MapFlightViewModel(@NonNull Application application) {
        super(application);
    }

    //We download data from API
    public void loadData(String icao, long date)
    {
        StringBuilder urlBuilder = new StringBuilder("https://opensky-network.org/api/tracks/");
        urlBuilder.append("all").append("?").append("icao24=").append(icao).append("&time=").append(date);
        Log.i(TAG, "URL is " + urlBuilder.toString());
        String url = urlBuilder.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response)
                    {
                        Log.i(TAG, "String response is " + response);
                        FlightTrack flightTrack;
                        List<FlightPath> flightPathList = new ArrayList<>();
                        JsonObject flightsJsonArray = getPathRequestJson(response);

                        //Creating new objects from results
                        for (JsonElement flightPath : flightsJsonArray.getAsJsonArray("path"))
                        {
                            flightPathList.add(new FlightPath(flightPath.getAsJsonArray().get(0).getAsLong(),
                                            flightPath.getAsJsonArray().get(1).getAsFloat(),
                                            flightPath.getAsJsonArray().get(2).getAsFloat(),
                                            flightPath.getAsJsonArray().get(3).getAsFloat(),
                                            flightPath.getAsJsonArray().get(4).getAsLong(),
                                            flightPath.getAsJsonArray().get(5).getAsBoolean()));
                        }

                        flightTrack = new FlightTrack(flightsJsonArray.get("icao24").getAsString(),
                                                    flightsJsonArray.get("callsign").getAsString(),
                                                    flightsJsonArray.get("startTime").getAsLong(),
                                                    flightsJsonArray.get("endTime").getAsLong(),
                                                    flightPathList);
                        mapFlightLiveData.setValue(flightTrack);

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplication().getBaseContext());
        requestQueue.add(stringRequest);
    }

    private JsonObject getPathRequestJson(String jsonString)
    {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(jsonString);
        return jsonElement.getAsJsonObject();
    }

}
