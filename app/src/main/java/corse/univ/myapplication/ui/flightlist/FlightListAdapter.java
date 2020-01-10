package corse.univ.myapplication.ui.flightlist;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.flightstats.Utils;
import com.example.flightstats.corse.univ.myapplication.data.Flight;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import corse.univ.myapplication.Activities.MapFlightActivity;
import corse.univ.myapplication.Activities.NoConnexionActivity;
import corse.univ.myapplication.R;

//Adapter for RecyclerView

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
        TextView arrivalDay;
        TextView departureDay;

        public FlightViewHolder(@NonNull final View itemView)
        {

            super(itemView);

            //We get all view from item list
            callSignView = itemView.findViewById(R.id.callSign);
            departure = itemView.findViewById(R.id.aeroportDepart);
            departureDate = itemView.findViewById(R.id.dateDepart);
            timeOfFlight = itemView.findViewById(R.id.tempsVol);
            arrival = itemView.findViewById(R.id.aeroportArrivee);
            arrivalDate = itemView.findViewById(R.id.dateArrivee);
            arrivalDay = itemView.findViewById(R.id.dateArriveeDay);
            departureDay =  itemView.findViewById(R.id.dateDepartDay);

        }

        public void onBind(int position) {

            final Flight mFlight = mFlightsList.get(position);
            //Temps de vol d'un avion en timestamp
            int flightLasting = (int) (mFlight.getLastSeen() - mFlight.getFirstSeen());
            //On convertit le temps de vol en format normal
            int mFlightLastingMin = (flightLasting%3600)/60 ;
            //On crée la chaine de caractère associée au temps de vol, on l'utilisera pour l'insérer dans la vue associée au temps de vol
            String mFlightLasting;
            if (mFlightLastingMin<10){
                mFlightLasting = flightLasting/3600 + "h0" + mFlightLastingMin;
            }else{
                mFlightLasting = flightLasting/3600 + "h" + mFlightLastingMin;
            }

            //On convertit de même les dates de départ et d'arrivée
            String mDepartureDateMin;
            String mArrivalDateMin;
            Date mdepartureDate = new Date(mFlight.getFirstSeen() *1000);
            Date marrivalDate = new Date(mFlight.getLastSeen() * 1000);

            //On convertit les dates à un format exploitables
            Calendar departureCalendar = Calendar.getInstance();
            departureCalendar.setTime(mdepartureDate);

            Calendar arrivalCalendar = Calendar.getInstance();
            arrivalCalendar.setTime(marrivalDate);

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

            Log.i("a", "Arrival date format:" + marrivalDate);
            //On place le texte dans le list item
            callSignView.setText("Vol n°" + mFlight.getCallsign());
            departure.setText(mFlight.getEstDepartureAirport());
            departureDate.setText(mdepartureDate.getHours() + "H" + mDepartureDateMin);
            arrival.setText(mFlight.getEstArrivalAirport());
            arrivalDate.setText(marrivalDate.getHours() + "H" + mArrivalDateMin);
            timeOfFlight.setText( mFlightLasting);

            //Removing a day and adding int to month to have the right date
            arrivalDay.setText((arrivalCalendar.get(Calendar.DAY_OF_MONTH)-1) + "/" + (arrivalCalendar.get(Calendar.MONTH)+1) + "/" + arrivalCalendar.get(Calendar.YEAR));
            departureDay.setText((departureCalendar.get(Calendar.DAY_OF_MONTH)-1) + "/" + (departureCalendar.get(Calendar.MONTH)+1) + "/" + departureCalendar.get(Calendar.YEAR));

            //On met un listener sur l'item de la liste afin que part un click dessus, l'utilisateur accède à la carte avec les informations du vol
            //Si il n'y a pas de connexion internet, on démarre son activité associée
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    if(Utils.Companion.isNetworkAvailable(activity)){
                        MapFlightActivity.startActivity(activity,mFlight.getIcao24(),mFlight.getFirstSeen(),mFlight.getEstDepartureAirport(), mFlight.getEstArrivalAirport());

                    }else{
                        NoConnexionActivity.startActivity(activity);
                    }

                }
            });
        }
    }
}
