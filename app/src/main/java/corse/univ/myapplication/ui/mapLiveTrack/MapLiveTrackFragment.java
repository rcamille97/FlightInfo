package corse.univ.myapplication.ui.mapLiveTrack;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.flightstats.Airport;
import com.example.flightstats.corse.univ.myapplication.data.Aircraft;
import com.example.flightstats.corse.univ.myapplication.data.AircraftData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import corse.univ.myapplication.R;

public class MapLiveTrackFragment extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;

    private static final String ICAO         = "icao";

    private MapLiveTrackViewModel mViewModel;

    public static MapLiveTrackFragment newInstance(String icao) {
        MapLiveTrackFragment mMap = new MapLiveTrackFragment();
        Bundle args = new Bundle();
        args.putString(ICAO, icao);
        mMap.setArguments(args);
        return mMap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_live_tracking, container, false);
        Bundle arguments = getArguments();

        mViewModel = ViewModelProviders.of(this).get(MapLiveTrackViewModel.class);
        if(arguments!=null)
            mViewModel.loadData(arguments.getString(ICAO));
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                Bundle arguments = getArguments();
                googleMap = mMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                if(arguments!=null)
                    mViewModel.loadData(arguments.getString(ICAO));

                mViewModel.mapLiveTrackLiveData.observe(getViewLifecycleOwner(), new Observer<Aircraft>()
                {
                    @Override
                    public void onChanged(Aircraft aircraft) {
                        AircraftData aircraftData = aircraft.getStates();
                        if(aircraftData!=null){
                            LatLng departure = new LatLng(aircraftData.getLatitude(),aircraftData.getLongitude());
                            googleMap.addMarker(new MarkerOptions().position(departure));
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(departure).zoom(5).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }else{
                            Toast.makeText(getActivity(),"No data available",Toast.LENGTH_LONG);
                            Log.i("a","no data");
                        }
                    }
                });
            }
        });



        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}