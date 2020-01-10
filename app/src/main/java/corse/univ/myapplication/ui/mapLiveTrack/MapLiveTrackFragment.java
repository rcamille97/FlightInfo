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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import corse.univ.myapplication.R;

public class MapLiveTrackFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    MapView mMapView;
    private GoogleMap googleMap;

    private static final String ICAO         = "icao";

    private String mIcao;
    private Float mVitesse;
    private Float altitude;
    private Float vertical_rate;

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
        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        mMapView.onResume(); // needed to get the map to display immediately

        return rootView;
    }


    @Override
    public void onMapReady(GoogleMap mMap) {

        MapsInitializer.initialize(getContext());

        googleMap = mMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mViewModel = ViewModelProviders.of(this).get(MapLiveTrackViewModel.class);

        Bundle arguments = getArguments();

        if(arguments!=null){
            mViewModel.loadData(arguments.getString(ICAO));
            mViewModel.loadAircraftHistory(arguments.getString(ICAO));
        }



        mViewModel.mapLiveTrackLiveData.observe(getViewLifecycleOwner(), new Observer<Aircraft>()
        {
            @Override
            public void onChanged(Aircraft aircraft) {
                if (aircraft != null) {
                    AircraftData aircraftData = aircraft.getStates();
                    if(aircraftData!=null){
                        Toast.makeText(getActivity(),"Click on marker to see data",Toast.LENGTH_LONG).show();
                        LatLng position = new LatLng(aircraftData.getLatitude(),aircraftData.getLongitude());
                        mIcao = aircraftData.getIcao();
                        mVitesse = aircraftData.getVelocity();
                        altitude = aircraftData.getBaro_altitude();
                        vertical_rate = aircraftData.getVertical_rate();

                        googleMap.addMarker(new MarkerOptions()
                                .position(position)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.position))
                                .title(aircraftData.getCallsign()));
                        Log.i("a","added" + aircraftData.getLatitude() + ";" + aircraftData.getLongitude());
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(7).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                }else{
                    Toast.makeText(getActivity(),"No data available for this aircraft",Toast.LENGTH_LONG).show();
                    Log.i("a","no data");
                }
            }
        });
        googleMap.setOnMarkerClickListener(this);

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


    @Override
    public boolean onMarkerClick(Marker marker) {
        String isMontee;
        if(vertical_rate<0){
            isMontee = "Descente";
        }else{
            isMontee = "Montée";
        }
        Toast.makeText(getContext(),"Numero du Vol: " + mIcao
                + "\nVitesse actuelle: " + mVitesse
                + "\nAltitude: " + altitude
                + "\nMontée ou descente: " +isMontee,Toast.LENGTH_LONG).show();
        return true;
    }
}