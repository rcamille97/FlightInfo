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


import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
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
        //String url = "http://www.mocky.io/v2/5e0ea3913400008e002d7c6b";

        new AsyncTask<Void,Void,List<Flight>>() {
            @Override
            protected List<Flight> doInBackground(Void... voids) {

                URL urlObject = null;
                List<Flight> flights = new ArrayList<>();
                try {
                    //urlObject = new URL(url);
                    urlObject = new URL("http://www.mocky.io/v2/5e0ea3913400008e002d7c6b");

                    HttpURLConnection conn = (HttpURLConnection) urlObject.openConnection();
                    conn.setReadTimeout(7000);
                    conn.setConnectTimeout(7000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(is, writer, Charset.forName("UTF-8"));
                    String jsonAsString = writer.toString();

                    Gson gson = new Gson();

                    JsonArray flightsJsonArray = getFlightsRequestJson(jsonAsString);
                    for (JsonElement flightObject : flightsJsonArray)
                    {
                        flights.add(gson.fromJson(flightObject.getAsJsonObject(), Flight.class));
                    }
                    Log.i(TAG, "flight list has size" + flights.size());
                    Log.i(TAG, jsonAsString);

                    //return flights;
                    return null;



                }catch (Exception e){
                    Log.v("AAAAA","c cassé");
                }

                return null;
            }
            @Override
            protected void onPostExecute(List<Flight> data) {
                flightListLiveData.setValue(data);
            }
        }.execute();
        /*// Request a string response from the provided URL.
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
        requestQueue.add(stringRequest);*/
    }

    public static boolean isStringValid(String string){
        return string != null && !string.isEmpty();
    }

    private JsonArray getFlightsRequestJson(String jsonString)

    {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(jsonString);
        return jsonElement.getAsJsonArray();
    }


}
