package corse.univ.myapplication.ui.flightlist;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.flightstats.corse.univ.myapplication.data.Flight;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import corse.univ.myapplication.Activities.MapFlightActivity;
import corse.univ.myapplication.R;

public class FlightListAdapter extends RecyclerView.Adapter<FlightListAdapter.FlightViewHolder>
{
    private static final String TAG = FlightListAdapter.class.getSimpleName();

    List<Flight> mFlightsList = new ArrayList<>();

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        return new FlightViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.flight_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position)
    {
        Log.i(TAG, mFlightsList.get(position).getCallsign());
        holder.onBind(position);

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

        public FlightViewHolder(@NonNull final View itemView)
        {
            super(itemView);
            callSignView = itemView.findViewById(R.id.callSign);


        }

        public void onBind(int position) {

            final Flight mFlight = mFlightsList.get(position);

            callSignView.setText(mFlight.getCallsign());
            callSignView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    MapFlightActivity.startActivity(activity,mFlight.getIcao24(),mFlight.getFirstSeen(),mFlight.getEstDepartureAirport(), mFlight.getEstArrivalAirport());

                }
            });
        }
    }
}
