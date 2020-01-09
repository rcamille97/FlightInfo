package corse.univ.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.flightstats.Utils;

import corse.univ.myapplication.R;
import corse.univ.myapplication.ui.flightlist.FlightListFragment;
import corse.univ.myapplication.ui.noConnexion.NoConnexionFragment;

public class GlobalActivity extends AppCompatActivity {

    private static final String TAG        = "GlobalActivity";
    private static final String BEGIN      = "begin";
    private static final String END        = "end";
    private static final String IS_ARRIVAL = "isArrival";
    private static final String ICAO       = "icao";

    public static void startActivity(Activity activity, long begin, long end, boolean isArrival, String icao)
    {

        Intent i = new Intent(activity, GlobalActivity.class);
        i.putExtra(BEGIN, begin);
        i.putExtra(END, end);
        i.putExtra(IS_ARRIVAL, isArrival);
        i.putExtra(ICAO, icao);
        activity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_activity);

        Intent intent = getIntent();

        long begin = intent.getLongExtra(BEGIN, -1);
        long end = intent.getLongExtra(END, -1);
        boolean isArrival = intent.getBooleanExtra(IS_ARRIVAL, false);
        String icao = intent.getStringExtra(ICAO);

        if (savedInstanceState == null && Utils.Companion.isNetworkAvailable(this))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, FlightListFragment.newInstance(begin, end, isArrival, icao),TAG).commitNow();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.container, NoConnexionFragment.newInstance(TAG)).commitNow();
        }
    }

}
