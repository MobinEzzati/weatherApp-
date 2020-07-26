package fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import Services.TimeService;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import dataModels.FragmentDetailModel;
import dataModels.WeatherModel;
import mobin.io.weatherapp.R;
import mobin.io.weatherapp.databinding.FragmentShowDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class showDetailFragment extends Fragment {
    private TimeService timeService = new TimeService();
    private String cityName , temp , wind , max  , hummidity , description;
    FragmentDetailModel fragmentDetailModel = new FragmentDetailModel();
    FragmentShowDetailBinding fragmentShowDetailBinding ;

    public showDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_detail, container, false);
         initView();
        fragmentShowDetailBinding = DataBindingUtil.inflate(inflater ,R.layout.fragment_show_detail , container , false );

                view = fragmentShowDetailBinding.getRoot();

        fragmentShowDetailBinding.setFragmentDetailModel(fragmentDetailModel);

//        Glide
//                .with(this)
//                .load("https://m.economictimes.com/thumb/msid-68228307,width-1200,height-900,resizemode-4,imgsize-482493/uri-indi.jpg")
//                .into(iv_state);

        return view ;
    }
    private void initView (){
        cityName = getArguments().get("cityName").toString();
        temp = getArguments().get("temp").toString();
        wind = getArguments().get("wind").toString();
        max = getArguments().get("max").toString() ;
        hummidity = getArguments().get("humidity").toString();
        description = getArguments().getString("description");
        fragmentDetailModel.setCityName(cityName);
        fragmentDetailModel.setMax(max);
        fragmentDetailModel.setWind(wind);
        fragmentDetailModel.setTemp(temp);
        fragmentDetailModel.setHummidty(hummidity);
        fragmentDetailModel.setDescription(description);


    }


}
