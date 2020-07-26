package mobin.io.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import Services.InternetConnection;
import Services.TimeService;
import adapters.RecycleViewAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import connectionToServer.RetrofitBuilder;
import connectionToServer.WeatherApi;
import dataModels.RecyclerViewModel;
import dataModels.WeatherModel;
import dialogBoxes.InternetDialog;
import fragments.showDetailFragment;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import otherClass.RecyclerViewOnItemClick;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    private static final int COARSE_LOCATION = 10;
    private static final String TAG = "tag";
    //    @BindView(R.id.iv_main_logo)
//    ImageView iv_logo;
    @BindView(R.id.act_main_place)
    AutoCompleteTextView act_places;
    @BindView(R.id.iv_main_search)
    ImageView iv_search;
    @BindView(R.id.pb_main)
    ProgressBar pb_main;
    RecyclerView.LayoutManager layoutManager;
    @BindView(R.id.rv_main)
    RecyclerView rv_showTemp;
    @BindView(R.id.tv_time)
    TextView tv_time;
    RecycleViewAdapter recycleViewAdapter;
    Animation animation;
    ItemTouchHelper itemTouchHelper;
    Realm realm;
    RealmResults<WeatherModel> city;
    ArrayList<String> name = new ArrayList<>();
    int counter = 0;
    List<RecyclerViewModel> recyclerViewModels = new ArrayList<RecyclerViewModel>();
    String tag = "x";
    RecyclerViewOnItemClick recyclerViewOnItemClick = null;
    TimeService timeService = new TimeService();
    boolean isBound = true;
    String cityName = "";
    InternetConnection internetConnection = null ;
    Vector<WeatherModel>globalWeathers = new Vector<>();
    InternetDialog internetDialog = new InternetDialog(this);
//    CheckNetworkReciver reciver = new CheckNetworkReciver();


    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb_main = new ProgressBar(getApplicationContext());
        ButterKnife.bind(this);
        Realm.init(this);
        check();
        checkInternet();
        act_places.setOnKeyListener(this);
    }

    @OnClick(R.id.iv_main_search)
    public void OnClick() {
//        checkInternet();
//        connectionToServer(act_places.getText().toString());
//        internetDialog.startDialog();
        checkInternet();
    }

    private void connectionToServer(String cityOrCountryName) {
            pb_main.setVisibility(View.VISIBLE);
            WeatherApi weatherApi = RetrofitBuilder.getRetrofit().create(WeatherApi.class);
            Observable<WeatherModel> weatherModelObservable = weatherApi.WEATHER_MODEL_OBSERVABLE(cityOrCountryName);
            weatherModelObservable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<WeatherModel>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onNext(WeatherModel weatherModel) {
                            pb_main.setVisibility(View.INVISIBLE);
                            cityName = weatherModel.getName();
                            if (name.contains(weatherModel.getName())){
                                Toast.makeText(getApplicationContext(), "you have this item", Toast.LENGTH_SHORT).show();
                            }else {
                                name.add(weatherModel.getName());
                                RecyclerViewModel recyclerViewModel = new RecyclerViewModel(weatherModel.getName(), String.valueOf(Math.ceil(weatherModel.getMain().getTemp() - 273.15)), weatherModel.getSys().getCountry());
                                addItemToRecyclerView(recyclerViewModel);
                                addToDb(weatherModel);
                            }
//                            RecyclerViewModel recyclerViewModel = new RecyclerViewModel(weatherModel.getName(), String.valueOf(Math.ceil(weatherModel.getMain().getTemp() - 273.15)), weatherModel.getSys().getCountry());
//                            addItemToRecyclerView(recyclerViewModel);
//                            addToDb(weatherModel);
                            globalWeathers.add(weatherModel);
                        }
                        @Override
                        public void onError(Throwable e) {
                            pb_main.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            pb_main.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "onComplete: " + cityName + " added to your list");

                        }
                    });

        }





    @Override
    public void onClick(View view) {
        connectionToServer(act_places.getText().toString());
    }


    private void addToDb(WeatherModel weatherModel) {
        realm.executeTransactionAsync(realm -> {
                    realm.copyToRealm(weatherModel);
                },
                () -> Log.d("add", act_places.getText().toString() + " added to database"),
                error -> Log.d("dbError", Objects.requireNonNull(error.getMessage())));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
//        unregisterReceiver(reciver) ;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addItemToRecyclerView(RecyclerViewModel recyclerViewModel) {
        Collections.reverse(recyclerViewModels);
            recyclerViewModels.add(recyclerViewModel);
        Collections.reverse(recyclerViewModels);
        Collections.reverse(name);
            recycleViewAdapter = new RecycleViewAdapter(recyclerViewModels, getApplicationContext(), recyclerViewOnItemClick);
            recycleViewAdapter.notifyDataSetChanged();
            rv_showTemp.setHasFixedSize(true);
            rv_showTemp.setAdapter(recycleViewAdapter);
            rv_showTemp.setLayoutManager(layoutManager);
            RecyclerView.ItemDecoration  decoration =  new DividerItemDecoration(getApplicationContext() ,DividerItemDecoration.VERTICAL);
            rv_showTemp.addItemDecoration(decoration);
            itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0  ,ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position  = viewHolder.getAdapterPosition();
                    name.remove(position);
                    recyclerViewModels.remove(position);
                    recycleViewAdapter.notifyDataSetChanged();

                }
            });
        itemTouchHelper.attachToRecyclerView(rv_showTemp);
        counter = 0;
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            connectionToServer(act_places.getText().toString());
            return true;
        }
        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void check() {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm -> city = realm.where(WeatherModel.class).findAll());
        if (city.size() != 0) {
            city.forEach(weatherModel -> recyclerViewModels.add(new RecyclerViewModel(weatherModel.getName(), String.valueOf(Math.ceil(weatherModel.getMain().getTemp() - 273.15)), weatherModel.getSys().getCountry())));
            city.forEach(weatherModel -> name.add(weatherModel.getName()));
        } else {
            Toast.makeText(this, "your db is empty", Toast.LENGTH_SHORT).show();
        }
        Collections.reverse(recyclerViewModels);
        Collections.reverse(name);
        recyclerViewOnItemClick = (view, Position) -> {
                pb_main.setVisibility(View.VISIBLE);
                WeatherApi weatherApi = RetrofitBuilder.getRetrofit().create(WeatherApi.class);
                Observable<WeatherModel> weatherModelObservable = weatherApi.WEATHER_MODEL_OBSERVABLE(name.get(Position));
                weatherModelObservable.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<WeatherModel>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onNext(WeatherModel weatherModel) {
                                pb_main.setVisibility(View.INVISIBLE);
                                showDetailFragment showDetailFragment = new showDetailFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("cityName" , weatherModel.getName());
                                bundle.putString("temp" , String.valueOf(Math.ceil(weatherModel.getMain().getTemp() - 273.15)));
                                bundle.putString("wind" , String.valueOf(weatherModel.getWind().getSpeed()));
                                bundle.putString("humidity" , String.valueOf(weatherModel.getMain().getHumidity()));
                                bundle.putString("max" , String.valueOf(Math.ceil(weatherModel.getMain().getTempMax() - 273.15)));
                                bundle.putString("description" , String.valueOf(weatherModel.getWeather().get(0).getDescription()));
                                showDetailFragment.setArguments(bundle);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fl_main ,showDetailFragment)
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .addToBackStack("back")
                                        .commit();
                            }

                            @Override
                            public void onError(Throwable e) {
                                pb_main.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {
                                pb_main.setVisibility(View.INVISIBLE);
                            }

                        });
        };
        recycleViewAdapter = new RecycleViewAdapter(recyclerViewModels, getApplicationContext(), recyclerViewOnItemClick);
        layoutManager = new LinearLayoutManager(getApplicationContext()) ;
        rv_showTemp.setHasFixedSize(true);
        rv_showTemp.setLayoutManager(layoutManager);
        rv_showTemp.setAdapter(recycleViewAdapter);
//        Collections.reverse(recyclerViewModels);
//        Collections.reverse(name);
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0  ,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position  = viewHolder.getAdapterPosition();
                name.remove(position);
                recyclerViewModels.remove(position);
                recycleViewAdapter.notifyDataSetChanged();
                realm.executeTransaction(realm -> {
                    RealmResults< WeatherModel> realmResults  =  realm.where(WeatherModel.class ).findAll() ;
                  realmResults.deleteFromRealm(position);
                });

            }
        });
        itemTouchHelper.attachToRecyclerView(rv_showTemp);

    }

    private void checkInternet (){
//            registerReceiver(internetConnection , new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if ( connectivityManager.getActiveNetworkInfo() != null && connectivityManager.
                getActiveNetworkInfo().isConnected() )
        {
            connectionToServer(act_places.getText().toString());
        }
        else
        {
            internetDialog.startDialog();

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(internetConnection);
    }

}







