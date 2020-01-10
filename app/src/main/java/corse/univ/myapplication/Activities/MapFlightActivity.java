package corse.univ.myapplication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import corse.univ.myapplication.R;
import corse.univ.myapplication.ui.mapFlightHistory.MapFlightFragment;

//Activity to start MapFlightFragment called in FlightListAdapter class
public class MapFlightActivity extends AppCompatActivity {

    private static final String TAG        = "MapFlightActivity";
    private static final String BEGIN     = "begin";
    private static final String ICAO       = "icao";
    private static final String DEPARTURE       = "departure";
    private static final String ARRIVAL       = "arrival";


    public static void startActivity(Activity activity, String icao, Long date, String departure, String arrival)
    {

        Intent i = new Intent(activity, MapFlightActivity.class);
        i.putExtra(ICAO, icao);
        i.putExtra(BEGIN,date);
        i.putExtra(DEPARTURE,departure);
        i.putExtra(ARRIVAL,arrival);
        activity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_flight_history_fragment);

        Intent intent = getIntent();

        long begin = intent.getLongExtra(BEGIN, -1);
        String icao = intent.getStringExtra(ICAO);
        String departure = intent.getStringExtra(DEPARTURE);
        String arrival = intent.getStringExtra(ARRIVAL);;


        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, MapFlightFragment.newInstance(icao,begin,departure,arrival),TAG).commitNow();
        }
    }

}
