package adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dataModels.RecyclerViewModel;
import mobin.io.weatherapp.R;
import otherClass.RecyclerViewOnItemClick;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    List<RecyclerViewModel> recyclerViewModels;
    Context context ;
    RecyclerViewOnItemClick recyclerViewOnItemClick ;

    public RecycleViewAdapter(List<RecyclerViewModel> recyclerViewModels, Context context , RecyclerViewOnItemClick recyclerViewOnItemClick) {
        this.recyclerViewModels = recyclerViewModels;
        this.context = context;
        this.recyclerViewOnItemClick = recyclerViewOnItemClick ;
    }

    public static class  ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_icon;
        TextView tv_temp ;
        TextView tv_city ;
        TextView tv_country ;
        private RecyclerViewOnItemClick recyclerViewOnItemClick ;


        public ViewHolder(@NonNull View itemView  , RecyclerViewOnItemClick recyclerViewOnItemClick) {
            super(itemView);

            tv_city = itemView.findViewById(R.id.tv_model_city);
            tv_temp = itemView.findViewById(R.id.tv_model_temp);
            tv_country = itemView.findViewById(R.id.tv_model_country);
            this.recyclerViewOnItemClick = recyclerViewOnItemClick ;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerViewOnItemClick.onItemClik(view , getAdapterPosition());
        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutInflater = LayoutInflater.from(context).inflate(R.layout.model_recycler_main ,parent , false);
        return new ViewHolder(layoutInflater , recyclerViewOnItemClick);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerViewModel recyclerViewModel = recyclerViewModels.get(position);
        String temp  = String.valueOf(Math.ceil(Double.parseDouble(recyclerViewModel.getCityTemp())));
        holder.tv_temp.setText(temp+"c");
        holder.tv_city.setText(recyclerViewModel.getCityName());
        holder.tv_country.setText(recyclerViewModel.getCountry());
    }
    @Override
    public int getItemCount() {
        return recyclerViewModels.size();
    }
}
