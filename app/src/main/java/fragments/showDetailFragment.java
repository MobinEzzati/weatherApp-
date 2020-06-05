package fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobin.io.weatherapp.MainActivity;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        new Handler().postDelayed(() -> {
//
//            Intent intent = new Intent(view.getContext() , MainActivity.class);
//            startActivity(intent);
//        }, 5000);
    }
}
