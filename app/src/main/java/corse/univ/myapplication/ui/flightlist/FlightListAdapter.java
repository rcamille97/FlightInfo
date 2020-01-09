package corse.univ.myapplication.ui.flightlist;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.flightstats.corse.univ.myapplication.data.Flight;

import java.util.ArrayList;
import java.util.Date;
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
        TextView departure;
        TextView departureDate;
        TextView timeOfFlight;
        TextView arrival;
        TextView arrivalDate;
        View mItemView;
        TextView arrivalDay;
        TextView departureDay;

        public FlightViewHolder(@NonNull final View itemView)
        {
            super(itemView);
            callSignView = itemView.findViewById(R.id.callSign);
            departure = itemView.findViewById(R.id.aeroportDepart);
            departureDate = itemView.findViewById(R.id.dateDepart);
            timeOfFlight = itemView.findViewById(R.id.tempsVol);
            arrival = itemView.findViewById(R.id.aeroportArrivee);
            arrivalDate = itemView.findViewById(R.id.dateArrivee);
            mItemView = itemView;
            arrivalDay = itemView.findViewById(R.id.dateArriveeDay);
            departureDay =  itemView.findViewById(R.id.dateDepartDay);


        }

        public void onBind(int position) {

            final Flight mFlight = mFlightsList.get(position);

            int flightLasting = (int) (mFlight.getLastSeen() - mFlight.getFirstSeen());
            int mFlightLastingMin = (flightLasting%3600)/60 ;
            String mFlightLasting;
            if (mFlightLastingMin<10){
                mFlightLasting = flightLasting/3600 + "h0" + mFlightLastingMin;
            }else{
                mFlightLasting = flightLasting/3600 + "h" + mFlightLastingMin;
            }

            String mDepartureDateMin;
            String mArrivalDateMin;
            Date mdepartureDate = new Date(mFlight.getFirstSeen() *1000);
            Date marrivalDate = new Date(mFlight.getLastSeen() * 1000);

            if(mdepartureDate.getMinutes()<10){
                mDepartureDateMin = "0" +mdepartureDate.getMinutes();
            }else{
                mDepartureDateMin = "" + mdepartureDate.getMinutes();
            }

            if(marrivalDate.getMinutes()<10){
                mArrivalDateMin = "0" +marrivalDate.getMinutes();
            }else{
                mArrivalDateMin = "" + marrivalDate.getMinutes();
            }


            callSignView.setText("Vol n°" + mFlight.getCallsign());
            departure.setText(mFlight.getEstDepartureAirport());
            departureDate.setText(mdepartureDate.getHours() + "H" + mDepartureDateMin);
            arrival.setText(mFlight.getEstArrivalAirport());
            arrivalDate.setText(marrivalDate.getHours() + "H" + mArrivalDateMin);
            timeOfFlight.setText( mFlightLasting);
            arrivalDay.setText(marrivalDate.getDay() + "/" + marrivalDate.getMonth() + "/" + marrivalDate.getYear());
            departureDay.setText(mdepartureDate.getDay() + "/" + mdepartureDate.getMonth() + "/" + mdepartureDate.getYear());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    MapFlightActivity.startActivity(activity,mFlight.getIcao24(),mFlight.getFirstSeen(),mFlight.getEstDepartureAirport(), mFlight.getEstArrivalAirport());

                }
            });
        }
    }
}
