package mobin.io.weatherapp;

import adapters.ViewPagerAdapter;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import connectionToServer.WeatherApi;
import dataModels.JsonModel;
import dataModels.Main;
import dataModels.Sys;
import dataModels.TestModel;
import dataModels.Weather;
import dataModels.WeatherModel;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.iv_main_logo)
    ImageView iv_logo;
    @BindView(R.id.et_main_place)
    EditText et_places;
    @BindView(R.id.iv_main_search)
    ImageView iv_search ;
    Animation animation ;

    List<TestModel>testModels = new ArrayList<>();
    List<WeatherModel> weatherModels ;
    Realm realm;

    WeatherModel weatherModel ;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        iv_search.setOnClickListener(this);
        rotatingAnimation();
        Main main = new Main();
        main.setTemp(12.6);



    }

    private void rotatingAnimation() {
        animation = AnimationUtils.loadAnimation(this , R.anim.rotating_animation);
        animation.setDuration(6000);
        iv_logo.startAnimation(animation);
    }

    @OnClick(R.id.iv_main_search)
    public void OnClick (){

        Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();

    }

    private  void connectionToServer(String cityOrCountryName){
        Retrofit builder = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherApi weatherApi = builder.create(WeatherApi.class);
            Call<WeatherModel>listCall = weatherApi.listWeather(cityOrCountryName);
            listCall.enqueue(new Callback<WeatherModel>() {
                @Override
                public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                    if (response.body() != null) {
                        Toast.makeText(MainActivity.this, String.valueOf(response.body().getMain().getTemp()), Toast.LENGTH_SHORT).show();
                         addToDb(response.body());

                    }else {
                        Toast.makeText(MainActivity.this, "we dont have this country", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onFailure(Call<WeatherModel> call, Throwable t) {
                    Log.i("error" , Objects.requireNonNull(t.getMessage()));
                }
            });
    }
    @Override
    public void onClick(View view) {
        connectionToServer(et_places.getText().toString());
    }



    public void  addToDb(WeatherModel weatherModel){
        realm.executeTransactionAsync(bgRealm -> {
            Number number =  bgRealm.where(WeatherModel.class).max("id");
            int key = (number == null) ? 1 : number.intValue() + 1 ;
            WeatherModel dbModel = bgRealm.createObject(WeatherModel.class , key);
            dbModel.setMain(weatherModel.getMain());

        }, () -> {
            Toast.makeText(this, et_places.getText().toString()+"temp were added to your database", Toast.LENGTH_SHORT).show();
        }, error -> {
        });


    }


}
