package corse.univ.myapplication.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.flightstats.Airport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import corse.univ.myapplication.Activities.GlobalActivity;
import corse.univ.myapplication.R;
import managers.AirportManager;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private EditText fromDate;
    private EditText toDate;
    private Switch switchToFrom;
    private Button okButton;
    private Spinner airportSpinner;
    private AirportManager airportManager;
    

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        airportManager = AirportManager.getInstance();
        switchToFrom = root.findViewById(R.id.switchToFrom);
        fromDate = root.findViewById(R.id.fromDate);
        toDate = root.findViewById(R.id.toDate);
        okButton = root.findViewById(R.id.okButton);


        Calendar currentCalendar = Calendar.getInstance();
        updateDateLabel(fromDate, currentCalendar);
        updateDateLabel(toDate, currentCalendar);

        homeViewModel.getFromCalendarLive().observe(this, new Observer<Calendar>()
        {
            @Override
            public void onChanged(Calendar calendar)
            {
                updateDateLabel(fromDate, calendar);
            }
        });
        homeViewModel.getToCalendarLive().observe(this, new Observer<Calendar>()
        {
            @Override
            public void onChanged(Calendar calendar)
            {
                updateDateLabel(toDate, calendar);
            }
        });

        fromDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar currentCalendar = Calendar.getInstance();
                Calendar fromCalendar = homeViewModel.getFromCalendarLive().getValue();
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), fromDateListener, fromCalendar.get(Calendar.YEAR),
                        fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.DAY_OF_MONTH));
                DatePicker datePicker = datePickerDialog.getDatePicker();
                currentCalendar.add(Calendar.DAY_OF_MONTH, -1);
                datePicker.setMaxDate(currentCalendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar currentCalendar = Calendar.getInstance();
                Calendar toCalendar = homeViewModel.getFromCalendarLive().getValue();
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), toDateListener, toCalendar.get(Calendar.YEAR),
                        toCalendar.get(Calendar.MONTH), toCalendar.get(Calendar.DAY_OF_MONTH));
                DatePicker datePicker = datePickerDialog.getDatePicker();
                currentCalendar.add(Calendar.DAY_OF_MONTH, -1);
                datePicker.setMaxDate(currentCalendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });


        //List<Airport> airportList = Utils.Companion.generateAirportList();
        List<String> airportListString = new ArrayList<>();
        for (Airport airport: airportManager.getAirportList() ){
            airportListString.add(airport.getFormattedName());
        }

        airportSpinner = root.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, airportListString );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        airportSpinner.setAdapter(adapter);

        okButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                doSearch();
            }

        });

        return root;
    }


    private DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            homeViewModel.getFromCalendarLive().setValue(calendar);
        }
    };

    private DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            homeViewModel.getToCalendarLive().setValue(calendar);
        }
    };

    private void updateDateLabel(EditText dateEditText, Calendar calendar)
    {
        dateEditText.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR));
    }

    private void doSearch(){

        // Récupérer le code aéroport
        int selectedIndex = airportSpinner.getSelectedItemPosition();
        Airport selectedAirport = AirportManager.getInstance().getAirportByIndex(selectedIndex);

        // Récupérer départ / arrivée
        boolean isArrival = switchToFrom.isChecked();

        Calendar fromCalendar = homeViewModel.getFromCalendarLive().getValue();
        Calendar toCalendar = homeViewModel.getToCalendarLive().getValue();
        // Récupérer begin et end timestamp
        if (toCalendar.getTimeInMillis() - fromCalendar.getTimeInMillis() < 24 * 3600 * 1000)
        {
            Toast.makeText(getActivity(), "Put a end date higher than the start date", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (toCalendar.getTimeInMillis() - fromCalendar.getTimeInMillis() > 7 * 24 * 3600 * 1000) //7 days (* 24h * 1000 ms)
        {
            Toast.makeText(getActivity(), "Interval should not be higher than 7 days", Toast.LENGTH_SHORT).show();
            return;
        }
        long begin = fromCalendar.getTimeInMillis() / 1000;
        long end = toCalendar.getTimeInMillis() / 1000;

        GlobalActivity.startActivity(getActivity(), begin, end, isArrival, selectedAirport.getIcao());
    }
}