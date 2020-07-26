package adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import dataModels.RecyclerViewModel;
import mobin.io.weatherapp.R;

public class CityAdapter extends ArrayAdapter<RecyclerViewModel> {
    private Context context;
    private int resource;
    private List<RecyclerViewModel> models;

    public CityAdapter(@NonNull Context context, int resource, @NonNull List<RecyclerViewModel> objects) {
        super(context, resource, objects);


    }


//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View view = convertView ;
//
//        try {
//            if (convertView == null){
//
//                LayoutInflater layoutInflater  = ((Activity) context).getLayoutInflater();
//                view = layoutInflater.inflate(R.layout.auto_complete_row ,parent , false);
//            }
//            RecyclerViewModel recyclerViewModel =
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
