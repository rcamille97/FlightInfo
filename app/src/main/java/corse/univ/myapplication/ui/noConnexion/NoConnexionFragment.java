package corse.univ.myapplication.ui.noConnexion;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import corse.univ.myapplication.R;

public class NoConnexionFragment extends Fragment {


    private static final String FRAGMENT         = "fragment";
    Button refreshButton ;

    public static NoConnexionFragment newInstance(String tag) {
        NoConnexionFragment mFragment = new NoConnexionFragment();
        Bundle arguments = new Bundle();
        arguments.putString(FRAGMENT, tag);
        mFragment.setArguments(arguments);
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.no_internet, container, false);
        refreshButton = rootView.findViewById(R.id.refreshButton);


        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arguments = getArguments();

                //Fragment f = getFragmentManager().findFragmentByTag(getActivity().getClass().getSimpleName());
                //put some verification
                Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(arguments.getString("TAG"));
                Log.i("AAAAAAAAAAA", f.getActivity().getClass().getSimpleName());
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.attach(f);
                ft.commitNow();
            }
        });
        return rootView;
    }



}
