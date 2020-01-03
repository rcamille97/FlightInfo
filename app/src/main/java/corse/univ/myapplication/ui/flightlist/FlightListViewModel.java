package corse.univ.myapplication.ui.flightlist;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.example.flightstats.corse.univ.myapplication.data.Flight;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class FlightListViewModel extends AndroidViewModel
{
    private static final String TAG = FlightListViewModel.class.getSimpleName();

    MutableLiveData<List<Flight>> flightListLiveData = new MutableLiveData<>();

    public FlightListViewModel(@NonNull Application application)
    {
        super(application);
    }



    public void loadData(long begin, long end, boolean isArrival, String icao)
    {
        StringBuilder urlBuilder = new StringBuilder("https://opensky-network.org/api/flights/");
        urlBuilder.append(isArrival ? "arrival" : "departure").append("?").append("airport=").append(icao).append("&begin=").append(begin).append("&end=")
                .append(end);

        Log.i(TAG, "URL is " + urlBuilder.toString());
        String url = urlBuilder.toString();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response)
                    {
                        Log.i(TAG, "String response is " + response);

                        List<Flight> flightList = new ArrayList<>();
                        Gson gson = new Gson();

                        JsonArray flightsJsonArray = getFlightsRequestJson(response);
                        for (JsonElement flightObject : flightsJsonArray)
                        {
                            flightList.add(gson.fromJson(flightObject.getAsJsonObject(), Flight.class));
                        }
                        Log.i(TAG, "flight list has size" + flightList.size());
                        flightListLiveData.setValue(flightList);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplication().getBaseContext());
        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }


    private JsonArray getFlightsRequestJson(String jsonString)

    {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(jsonString);
        return jsonElement.getAsJsonArray();
    }


}
