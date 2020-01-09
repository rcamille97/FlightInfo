package corse.univ.myapplication.ui.mapFlightHistory;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.flightstats.Airport;
import com.example.flightstats.corse.univ.myapplication.data.FlightPath;
import com.example.flightstats.corse.univ.myapplication.data.FlightTrack;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import corse.univ.myapplication.Activities.MapLiveTrackActivity;
import corse.univ.myapplication.R;
import managers.AirportManager;


public class MapFlightFragment extends Fragment{

    MapView mMapView;
    private GoogleMap googleMap;

    private static final String ICAO         = "icao";
    private static final String BEGIN        = "begin";
    private static final String DEPARTURE       = "departure";
    private static final String ARRIVAL       = "arrival";

    Button displayDetails;

    private MapFlightViewModel mViewModel;

    public static MapFlightFragment newInstance(String icao, Long date, String departure, String arrival) {
        MapFlightFragment mMap = new MapFlightFragment();
        Bundle args = new Bundle();
        args.putString(ICAO, icao);
        args.putLong(BEGIN, date);
        args.putString(DEPARTURE, departure);
        args.putString(ARRIVAL, arrival);

        mMap.setArguments(args);
        return mMap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_flight_history_fragment, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        displayDetails = rootView.findViewById(R.id.displayDetails);
        displayDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle arguments = getArguments();
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        MapLiveTrackActivity.startActivity(activity, arguments.getString(ICAO));
                        Log.i("AAAA", "clicked");
                    }
        });

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                Bundle arguments = getArguments();

                mViewModel = ViewModelProviders.of(getActivity()).get(MapFlightViewModel.class);
                if(arguments!=null)
                    mViewModel.loadData(arguments.getString(ICAO), arguments.getLong(BEGIN));
                //MapLiveTrackActivity.startActivity(getActivity(), arguments.getString(ICAO));
                googleMap = mMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);



                // For dropping a marker at a point on the Map
                if(getCoordinates(arguments.getString(DEPARTURE))!=null){
                    LatLng departure = new LatLng(getCoordinates(arguments.getString(DEPARTURE))[0], getCoordinates(arguments.getString(DEPARTURE))[1]);
                    googleMap.addMarker(new MarkerOptions().position(departure));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(departure).zoom(4).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }else{
                    Toast.makeText(getContext(), "Can't display departure airport", Toast.LENGTH_LONG);
                }
                if(getCoordinates(arguments.getString(ARRIVAL))!=null){
                    LatLng arrival = new LatLng(getCoordinates(arguments.getString(ARRIVAL))[0], getCoordinates(arguments.getString(ARRIVAL))[1]);
                    googleMap.addMarker(new MarkerOptions().position(arrival));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(arrival).zoom(4).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }else{
                    Toast.makeText(getContext(), "Can't display arrival airport", Toast.LENGTH_LONG);
                }

                mViewModel.mapFlightLiveData.observe(getViewLifecycleOwner(), new Observer<FlightTrack>()
                {
                    @Override
                    public void onChanged(FlightTrack flightTrack) {
                        List<FlightPath> mFlightPath = flightTrack.getPath();
                        List<LatLng> mPathList = new ArrayList<>();
                        for (FlightPath p : mFlightPath) {
                            mPathList.add(new LatLng(p.getLat(),p.getLng()));
                        }
                        Polyline mPath = googleMap.addPolyline(new PolylineOptions()
                                .clickable(true)
                                .addAll(mPathList));
                        mPath.setEndCap(new RoundCap());
                        mPath.setWidth(12);
                        mPath.setColor(0xffF9A825);
                        mPath.setJointType(JointType.ROUND);
                    }
                });
            }
        });



        return rootView;
    }


    public Float[] getCoordinates(String airport){
        AirportManager airportManager = AirportManager.getInstance();
        Airport mAirport = airportManager.getAirportWithCode(airport);
        if (mAirport != null){
            Float coordinates[] = {Float.valueOf(mAirport.getLat()), Float.valueOf(mAirport.getLon())};
            return coordinates;
        }else{
            return null;
        }
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
