package corse.univ.myapplication.ui.mapLiveTrack;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.flightstats.corse.univ.myapplication.data.Aircraft;
import com.example.flightstats.corse.univ.myapplication.data.AircraftData;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MapLiveTrackViewModel extends AndroidViewModel {


    private static final String TAG = MapLiveTrackViewModel.class.getSimpleName();

    MutableLiveData<Aircraft> mapLiveTrackLiveData = new MutableLiveData<>();


    public MapLiveTrackViewModel(@NonNull Application application) {
        super(application);
    }


    public void loadData(String icao)
    {
        StringBuilder urlBuilder = new StringBuilder("https://opensky-network.org/api/states/");
        urlBuilder.append("all").append("?").append("icao24=").append(icao);
        Log.i(TAG, "URL is " + urlBuilder.toString());
        String url = urlBuilder.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response)
                    {
                        Log.i(TAG, "String response is " + response);
                        Aircraft aircraft;
                        AircraftData aircraftData;
                        Gson gson = new Gson();

                        JsonObject flightsJsonArray = getPathRequestJson(response);
                        JsonArray jsonAircraftData = flightsJsonArray.get("states").getAsJsonArray();
                        Log.i("AAA", jsonAircraftData.getAsString());
                        if (jsonAircraftData != null){
                            aircraftData = new AircraftData(
                                    jsonAircraftData.get(0).getAsString(),
                                    jsonAircraftData.get(1).getAsString(),
                                    jsonAircraftData.get(2).getAsString(),
                                    jsonAircraftData.get(3).getAsLong(),
                                    jsonAircraftData.get(4).getAsLong(),
                                    jsonAircraftData.get(6).getAsFloat(),
                                    jsonAircraftData.get(7).getAsFloat(),
                                    jsonAircraftData.get(8).getAsFloat(),
                                    jsonAircraftData.get(9).getAsBoolean(),
                                    jsonAircraftData.get(10).getAsFloat(),
                                    jsonAircraftData.get(11).getAsFloat(),
                                    jsonAircraftData.get(12).getAsFloat(),
                                    jsonAircraftData.get(14).getAsFloat(),
                                    jsonAircraftData.get(15).getAsString(),
                                    jsonAircraftData.get(16).getAsBoolean(),
                                    jsonAircraftData.get(17).getAsLong()
                            );
                            aircraft = new Aircraft(flightsJsonArray.get("time").getAsLong(), aircraftData);
                            mapLiveTrackLiveData.setValue(aircraft);
                        }else{
                            mapLiveTrackLiveData.setValue(null);
                        }
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
    //Url for live tracking: https://opensky-network.org/api/states/all?icao24=3c4b26
    //For specific plane aircraft?icao24=3c675a&begin=1517184000&end=1517270400"

}
