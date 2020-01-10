package corse.univ.myapplication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.flightstats.Utils;

import androidx.appcompat.app.AppCompatActivity;
import corse.univ.myapplication.R;
import corse.univ.myapplication.ui.noConnexion.NoConnexionFragment;

//Activity started when no connexion is available
public class NoConnexionActivity extends AppCompatActivity {

    public static void startActivity(Activity activity)
    {
        Intent i = new Intent(activity, NoConnexionActivity.class);
        activity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_internet);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, NoConnexionFragment.newInstance()).commitNow();
        }
    }

    //We disable the possibility to go to previous page, the user is unable to use the previous page when no connexion is available.
    //But the data of the previous fragment are still alive when the connexion will be back
    @Override
    public void onBackPressed(){
        if(Utils.Companion.isNetworkAvailable(this)){
            super.onBackPressed();}
    }
}
