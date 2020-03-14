package adapters;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dataModels.RecyclerViewModel;
import mobin.io.weatherapp.R;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    List<RecyclerViewModel> recyclerViewModels;
    Context context ;

    public RecycleViewAdapter(List<RecyclerViewModel> recyclerViewModels, Context context) {
        this.recyclerViewModels = recyclerViewModels;
        this.context = context;
    }

    public static class  ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_icon;
        TextView tv_temp ;
        TextView tv_city ;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_icon = itemView.findViewById(R.id.iv_model_icon);
            tv_city = itemView.findViewById(R.id.tv_model_city);
            tv_temp = itemView.findViewById(R.id.tv_model_temp);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutInflater = LayoutInflater.from(context).inflate(R.layout.model_recycler_main ,parent , false);

        return new ViewHolder(layoutInflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerViewModel recyclerViewModel = recyclerViewModels.get(position);
        holder.tv_temp.setText(recyclerViewModel.getCityTemp()+"F");
        holder.tv_city.setText(recyclerViewModel.getCityName());
        if (recyclerViewModel.getCityName() == "tehran"){
             holder.iv_icon.setBackgroundResource(R.drawable.temp);
        }
    }

    @Override
    public int getItemCount() {
        return recyclerViewModels.size();
    }
}
