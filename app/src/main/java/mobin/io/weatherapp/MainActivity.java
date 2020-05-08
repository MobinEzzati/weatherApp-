package mobin.io.weatherapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import Services.TempService;
import Services.TimeService;
import Services.TimeService.MaBinder;
import Services.TempService.Mybinder;
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
import dialogBoxes.LoadingDialog;
import fragments.showDetailFragment;
import globals.Global;
import io.realm.Realm;
import io.realm.RealmResults;
import otherClass.CheckNetworkReciver;
import otherClass.RecyclerViewOnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    private static final int COARSE_LOCATION = 10;
    private static final String TAG = "tag";
    @BindView(R.id.iv_main_logo)
    ImageView iv_logo;
    @BindView(R.id.et_main_place)
    EditText et_places;
    @BindView(R.id.iv_main_search)
    ImageView iv_search;
    RecyclerView.LayoutManager layoutManager;
    @BindView(R.id.rv_main)
    RecyclerView rv_showTemp;
    @BindView(R.id.tv_time)
    TextView tv_time ;
    RecycleViewAdapter recycleViewAdapter;
    Animation animation;
    Realm realm;
    RealmResults<WeatherModel> city;
    ArrayList<String> name = new ArrayList<>();
    int counter = 0;
    List<RecyclerViewModel> recyclerViewModels = new ArrayList<RecyclerViewModel>();
    String tag = "x";
    RecyclerViewOnItemClick recyclerViewOnItemClick ;
    TimeService timeService = new TimeService();
    TempService tempService = new TempService();
    boolean isBound =  true  ;
    final LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);


    CheckNetworkReciver reciver = new CheckNetworkReciver() ;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Toast.makeText(MainActivity.this, "you are  connect",  Toast.LENGTH_SHORT).show();
            MaBinder maBinder = (MaBinder) iBinder;
//            Mybinder mybinder = (Mybinder) iBinder;

            timeService = maBinder.getTimeService();
//            tempService = mybinder.boundService();

            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
            Toast.makeText(MainActivity.this, "you are not connect",  Toast.LENGTH_SHORT).show();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Realm.init(this);
        rotatingAnimation();
        Intent intent = new Intent(this , TimeService.class);
        bindService(intent ,serviceConnection , Context.BIND_AUTO_CREATE);
        check();
        showTime();

        et_places.setOnKeyListener(this);
    }

    private void rotatingAnimation() {
        animation = AnimationUtils.loadAnimation(this, R.anim.rotating_animation);
        animation.setDuration(10000);
        iv_logo.startAnimation(animation);

    }

    @OnClick(R.id.iv_main_search)
    public void OnClick() {
        loadingDialog.startDialog();
        connectionToServer(et_places.getText().toString());
    }
    private void connectionToServer(String cityOrCountryName) {
        Retrofit builder = new Retrofit.Builder().baseUrl("https://api.openweathermap.org/").addConverterFactory(GsonConverterFactory.create()).build();
        WeatherApi weatherApi = builder.create(WeatherApi.class);
        Call<WeatherModel> listCall = weatherApi.listWeather(cityOrCountryName);
        listCall.enqueue(new Callback<WeatherModel>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                loadingDialog.dissmisDialog();
                if (response.body() != null) {
                    addToDb(response.body());
                    RecyclerViewModel recyclerViewModel = new RecyclerViewModel(response.body().getName(), convertFtoC(response.body().getMain().getTemp()), response.body().getName());
                    addItemToRecyclerView(recyclerViewModel);
                } else {
                    Toast.makeText(MainActivity.this, "we dont have this city or country", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                Log.d("server", t.getMessage());
            }
        });
    }


    @Override
    public void onClick(View view) {
        connectionToServer(et_places.getText().toString());
    }


    private void addToDb(WeatherModel weatherModel) {
        realm.executeTransactionAsync(realm -> {
                    realm.copyToRealm(weatherModel);
                },
                () -> Log.d("add", et_places.getText().toString() + " added to database"),
                error -> Log.d("dbError", Objects.requireNonNull(error.getMessage())));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        unregisterReceiver(reciver);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addItemToRecyclerView(RecyclerViewModel recyclerViewModel) {
        name.add(recyclerViewModel.getCityName());
        for (int i = 0; i < name.size(); i++) {
            if (name.get(i).equals(recyclerViewModel.getCityName())) {
                counter = counter + 1;
            }
        }
        if (counter > 1) {
            Toast.makeText(this, "you added this item", Toast.LENGTH_SHORT).show();
        } else {
            Collections.reverse(recyclerViewModels);
            recyclerViewModels.add(recyclerViewModel);
            Collections.reverse(recyclerViewModels);

            recycleViewAdapter = new RecycleViewAdapter(recyclerViewModels, getApplicationContext() , recyclerViewOnItemClick);
            recycleViewAdapter.notifyDataSetChanged();
            rv_showTemp.setHasFixedSize(true);
            rv_showTemp.setAdapter(recycleViewAdapter);
            rv_showTemp.setLayoutManager(layoutManager);
        }
        counter = 0;
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            connectionToServer(et_places.getText().toString());
            return true;
        }
        return false;
    }

    public String convertFtoC(Double fahrenheit) {
        return String.valueOf(Math.ceil((fahrenheit - 273.15)));
    }

    private void showTime (){
        Handler handler = new Handler();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> tv_time.setText(timeService.getTime()));

            }
        } , 0 , 1000);
    }

    private boolean networkIsconnect(){
        tempService.updatedTemp(name);
        return false ;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void check(){
        recyclerViewOnItemClick = (view, Position) ->     getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_main, new showDetailFragment()).setCustomAnimations(R.anim.enter , R.anim.exit).commit();

        realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm -> city = realm.where(WeatherModel.class).findAll());
        if (city.size() != 0) {
            city.forEach(weatherModel -> recyclerViewModels.add(new RecyclerViewModel(weatherModel.getName(), String.valueOf(weatherModel.getMain().getTemp()), weatherModel.getName())));
            city.forEach(weatherModel -> name.add(weatherModel.getName()));

        } else {
            Toast.makeText(this, "your db is empty", Toast.LENGTH_SHORT).show();
        }
        Collections.reverse(recyclerViewModels);
        recycleViewAdapter = new RecycleViewAdapter(recyclerViewModels, getApplicationContext() ,recyclerViewOnItemClick);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_showTemp.setHasFixedSize(true);
        rv_showTemp.setLayoutManager(layoutManager);
        rv_showTemp.setAdapter(recycleViewAdapter);

    }
}

