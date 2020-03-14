package fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobin.io.weatherapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class showDetailFragment extends Fragment {


    public showDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_detail, container, false);
    }

}
