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
import com.example.flightstats.corse.univ.myapplication.data.AircraftHistory;
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
    MutableLiveData<AircraftHistory> mapAircraftHistoryLiveData = new MutableLiveData<>();

    public MapLiveTrackViewModel(@NonNull Application application) {
        super(application);
    }


    //We download data from API
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

                        JsonObject flightsJsonArray = getPathRequestJson(response);
                        JsonArray jsonAircraftData;
                        if(!flightsJsonArray.get("states").isJsonNull()){
                            Log.i("a",flightsJsonArray.get("states").toString());
                            jsonAircraftData = flightsJsonArray.get("states").getAsJsonArray();
                            Log.i("a", jsonAircraftData.toString());
                            if (jsonAircraftData != null) {
                                JsonArray mData = jsonAircraftData.get(0).getAsJsonArray();
                                Boolean isNull = false;
                                for(Object data : mData){
                                    if(data == null)
                                        isNull= true;
                                }
                                if(!isNull){
                                    aircraftData = new AircraftData(
                                            mData.get(0).getAsString(),
                                            mData.get(1).getAsString(),
                                            mData.get(2).getAsString(),
                                            mData.get(3).getAsLong(),
                                            mData.get(4).getAsLong(),
                                            mData.get(5).getAsFloat(),
                                            mData.get(6).getAsFloat(),
                                            mData.get(7).getAsFloat(),
                                            mData.get(8).getAsBoolean(),
                                            mData.get(9).getAsFloat(),
                                            mData.get(10).getAsFloat(),
                                            mData.get(11).getAsFloat(),
                                            mData.get(13).getAsFloat()
                                    );
                                    aircraft = new Aircraft(flightsJsonArray.get("time").getAsLong(), aircraftData);
                                    mapLiveTrackLiveData.setValue(aircraft);
                                }else
                                    mapLiveTrackLiveData.setValue(null);

                            }
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


    //Tried to start 4bis part but too late, sorry
    public void loadAircraftHistory(String icao){

        //On obtient la date du jour pour la requête
        long end = System.currentTimeMillis()/1000;
        //On convertit 3 jours en secondes pour soustraire à la date d'aujourd'hui
        int NUMBER_OF_DAYS = 3;
        long days = NUMBER_OF_DAYS*24*60*60;
        //On calcule la date de début pour notre requête
        long begin = end - days;


        StringBuilder urlBuilder = new StringBuilder("https://opensky-network.org/api/flights/");
        urlBuilder.append("aircraft").append("?").append("icao24=").append(icao).append("&begin=").append(begin).append("&end=")
                .append(end);
        Log.i(TAG, "URL is " + urlBuilder.toString());
        String url = urlBuilder.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response)
                    {
                        Log.i(TAG, "String response of History is " + response);
                        Aircraft aircraft;
                        AircraftHistory aircraftHistory;

                        /*JsonObject flightsJsonArray = getPathRequestJson(response);
                        JsonArray jsonAircraftHistory;
                        */

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
