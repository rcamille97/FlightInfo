package corse.univ.myapplication.ui.flightlist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import corse.univ.myapplication.R;
import com.example.flightstats.corse.univ.myapplication.data.Flight;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FlightListFragment extends Fragment
{
    private static final String TAG = FlightListFragment.class.getSimpleName();

    private static final String BEGIN      = "begin";
    private static final String END        = "end";
    private static final String IS_ARRIVAL = "isArrival";
    private static final String ICAO       = "icao";

    private FlightListViewModel mViewModel;
    private FlightListAdapter   mAdapter;

    public static FlightListFragment newInstance()
    {
        return new FlightListFragment();
    }

    public static FlightListFragment newInstance(long begin, long end, boolean isArrival, String icao)
    {
        FlightListFragment fragment = newInstance();
        Bundle b = new Bundle();
        b.putLong(BEGIN, begin);
        b.putLong(END, end);
        b.putBoolean(IS_ARRIVAL, isArrival);
        b.putString(ICAO, icao);
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.flight_list_fragment, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview);
        mAdapter = new FlightListAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FlightListViewModel.class);
        Bundle arguments = getArguments();
        if(arguments!=null)
        mViewModel.loadData(arguments.getLong(BEGIN),arguments.getLong(END),arguments.getBoolean(IS_ARRIVAL),arguments.getString(ICAO));

        mViewModel.flightListLiveData.observe(getViewLifecycleOwner(), new Observer<List<Flight>>()
        {
            @Override
            public void onChanged(List<Flight> flights)
            {
                Log.i(TAG, "updating list with size = " + flights.size());
                mAdapter.setFlights(flights);
            }
        });
    }

}
