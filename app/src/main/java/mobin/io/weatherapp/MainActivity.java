package mobin.io.weatherapp;

import adapters.RecycleViewAdapter;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import connectionToServer.WeatherApi;
import dataModels.RecyclerViewModel;
import dataModels.WeatherModel;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    @BindView(R.id.iv_main_logo)
    ImageView iv_logo;
    @BindView(R.id.et_main_place)
    EditText et_places;
    @BindView(R.id.iv_main_search)
    ImageView iv_search ;
    RecyclerView.LayoutManager layoutManager ;
    @BindView(R.id.rv_main)
    RecyclerView rv_showTemp ;
    RecycleViewAdapter recycleViewAdapter ;
    Animation animation ;
    Realm realm;
    RealmResults<WeatherModel>city ;
    ArrayList<RecyclerViewModel>recyclerViewModels  = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm -> city = realm.where(WeatherModel.class).findAll());
        if (city.size()!= 0){
            city.forEach(weatherModel -> recyclerViewModels.add(new RecyclerViewModel(weatherModel.getName() , String.valueOf(weatherModel.getMain().getTemp()) ,weatherModel.getName())));
        }else {

            Toast.makeText(this, "your db is empty", Toast.LENGTH_SHORT).show();
        }
        recycleViewAdapter = new RecycleViewAdapter(recyclerViewModels , getApplicationContext());
        layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_showTemp.setHasFixedSize(true);
        rv_showTemp.setLayoutManager(layoutManager);
        rv_showTemp.setAdapter(recycleViewAdapter);
        rotatingAnimation();
        et_places.setOnKeyListener(this);


    }
    private void rotatingAnimation() {
        animation = AnimationUtils.loadAnimation(this , R.anim.rotating_animation);
        animation.setDuration(6000);
        iv_logo.startAnimation(animation);
    }
    @OnClick(R.id.iv_main_search)
    public void OnClick (){
     connectionToServer(et_places.getText().toString());
    }
    private  void connectionToServer(String cityOrCountryName){
        Retrofit builder = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherApi weatherApi = builder.create(WeatherApi.class);
            Call<WeatherModel>listCall = weatherApi.listWeather(cityOrCountryName);
            listCall.enqueue(new Callback<WeatherModel>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                    if (response.body() != null) {
                        addToDb(response.body());
                        RecyclerViewModel recyclerViewModel = new RecyclerViewModel(response.body().getName() , String.valueOf(response.body().getMain().getTemp()),response.body().getName());
                        addItemToRecyclerView(recyclerViewModel);
                    }else {
                        Toast.makeText(MainActivity.this, "we dont have this city or country", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<WeatherModel> call, Throwable t) {
                    Log.d("server" , t.getMessage());

                }
            });
    }
    @Override
    public void onClick(View view) {
        connectionToServer(et_places.getText().toString());
    }
    private void  addToDb(WeatherModel weatherModel){

       realm.executeTransactionAsync(realm -> {
//           WeatherModel we = realm.createObject(WeatherModel.class  , weatherModel.getId());
//           we.setMain(weatherModel.getMain());

            realm.copyToRealm(weatherModel);

           }, () -> Log.d("add" , et_places.getText().toString()+" added to database"), new Realm.Transaction.OnError() {
           @Override
           public void onError(Throwable error) {

               Log.e("dbError" , error.getMessage());
           }
       });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
    private void addItemToRecyclerView( RecyclerViewModel recyclerView){

        recyclerViewModels.add(recyclerView);
        recycleViewAdapter = new RecycleViewAdapter(recyclerViewModels , getApplicationContext());
        layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_showTemp.setHasFixedSize(true);
        rv_showTemp.setLayoutManager(layoutManager);
        rv_showTemp.setAdapter(recycleViewAdapter);

    }
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i == EditorInfo.IME_ACTION_SEARCH || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            connectionToServer(et_places.getText().toString());
            return  true ;
        }
        return false;
    }
}
