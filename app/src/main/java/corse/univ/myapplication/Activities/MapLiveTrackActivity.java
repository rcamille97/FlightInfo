package corse.univ.myapplication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.flightstats.Utils;

import androidx.appcompat.app.AppCompatActivity;
import corse.univ.myapplication.R;
import corse.univ.myapplication.ui.mapLiveTrack.MapLiveTrackFragment;
import corse.univ.myapplication.ui.noConnexion.NoConnexionFragment;

public class MapLiveTrackActivity extends AppCompatActivity {

    private static final String ICAO       = "icao";
    private static final String TAG        = "MapLiveTrackActivity";

    public static void startActivity(Activity activity, String icao)
    {

        Intent i = new Intent(activity, MapLiveTrackActivity.class);
        i.putExtra(ICAO, icao);
        activity.startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_live_tracking);
        Intent intent = getIntent();
        String icao = intent.getStringExtra(ICAO);
        if (savedInstanceState == null  && Utils.Companion.isNetworkAvailable(this))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, MapLiveTrackFragment.newInstance(icao), TAG).commitNow();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.container, NoConnexionFragment.newInstance(TAG)).commitNow();
        }
    }
}
