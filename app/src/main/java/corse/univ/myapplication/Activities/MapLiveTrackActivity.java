package corse.univ.myapplication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import corse.univ.myapplication.R;
import corse.univ.myapplication.ui.mapLiveTrack.MapLiveTrackFragment;


//Activity to start MapLiveTrackFragment called in MapFlightFragment class
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
        setContentView(R.layout.map_fragment);
        Intent intent = getIntent();
        String icao = intent.getStringExtra(ICAO);
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, MapLiveTrackFragment.newInstance(icao), TAG).commitNow();
        }
    }
}
