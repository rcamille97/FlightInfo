package corse.univ.myapplication.ui.flightlist;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.flightstats.corse.univ.myapplication.data.Flight;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FlightListAdapter extends RecyclerView.Adapter<FlightListAdapter.FlightViewHolder>
{
    private static final String TAG = FlightListAdapter.class.getSimpleName();

    List<Flight> mFlightsList = new ArrayList<>();

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        TextView callSignTextView = new TextView(parent.getContext());
        return new FlightViewHolder(callSignTextView);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position)
    {
        Log.i(TAG, mFlightsList.get(position).getCallsign());
        holder.callSignView.setText(mFlightsList.get(position).getCallsign());
    }

    @Override
    public int getItemCount()
    {
        Log.i(TAG, "taille de la liste: " + mFlightsList.size());
        return mFlightsList.size();
    }

    public void setFlights(List<Flight> flightsList)
    {
        mFlightsList.clear();
        mFlightsList.addAll(flightsList);
        notifyDataSetChanged();
    }

    class FlightViewHolder extends RecyclerView.ViewHolder
    {

        TextView callSignView;

        public FlightViewHolder(@NonNull View itemView)
        {
            super(itemView);
            callSignView = (TextView) itemView;
        }
    }
}
