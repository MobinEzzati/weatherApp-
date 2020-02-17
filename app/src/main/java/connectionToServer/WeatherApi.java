package connectionToServer;

import java.util.List;

import dataModels.JsonModel;
import dataModels.WeatherModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("data/2.5/weather?q,&APPID=a67ef6c84476df6f70da89017c86b32c")
    Call<WeatherModel>listWeather(@Query("q")String q);

//    @GET("posts")
//    Call<List<JsonModel>>LIST_CALL();
}
