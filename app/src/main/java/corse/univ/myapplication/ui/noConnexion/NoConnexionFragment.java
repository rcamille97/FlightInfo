package corse.univ.myapplication.ui.noConnexion;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.flightstats.Utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import corse.univ.myapplication.R;

public class NoConnexionFragment extends Fragment {


    private static final String FRAGMENT         = "fragment";
    Button refreshButton ;

    public static NoConnexionFragment newInstance() {
        NoConnexionFragment mFragment = new NoConnexionFragment();
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.no_internet, container, false);
        refreshButton = rootView.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.Companion.isNetworkAvailable(v.getContext()))
                    getActivity().onBackPressed();

            }
        });
        return rootView;
    }



}
