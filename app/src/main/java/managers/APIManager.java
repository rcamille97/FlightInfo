package managers;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import com.example.flightstats.corse.univ.myapplication.data.Flight;

public class APIManager extends AsyncTask<Void, Void, ArrayList<Flight>> {

    private String arrivalOrDeparture = "";
    private String icao = "";
    private String begin = "";
    private String end = "";

    private ArrayList<Flight> flights = new ArrayList<>();


    public APIManager(){

    }

    public String getTextFromStream(InputStream is)
    {
        try
        {
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, Charset.forName("UTF-8"));
            return writer.toString();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public boolean isStringValid(String string){
        return string != null && !string.isEmpty();
    }



    @Override
    protected ArrayList<Flight> doInBackground(Void... voids) {
        String url = "https://opensky-network.org/api/flights/" + arrivalOrDeparture + "?airport=" + icao + "&begin="
                + begin + "&end=" + end;
        URL urlObject = null;
        try {
            urlObject = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObject.openConnection();
            conn.setReadTimeout(7000);
            conn.setConnectTimeout(7000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            String jsonAsString = getTextFromStream(is);
            if (isStringValid(jsonAsString)) {
                JSONObject responseJSON = new JSONObject(jsonAsString);
                JSONArray itemsArray = responseJSON.optJSONArray("items");
                for (int i = 0; i < itemsArray.length(); i++) {
                    //flights.add(new Flight(itemsArray.optJSONObject(i)));
                    flights.add(new Flight());
                }

                return flights;
            }


        }catch (Exception e){
            Log.v("AAAAA","c cassé");
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Flight> flights) {
        super.onPostExecute(flights);

    }

}
