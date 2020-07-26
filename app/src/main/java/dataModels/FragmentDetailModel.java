package dataModels;

public class FragmentDetailModel {
    String cityName ;
    String wind ;
    String temp ;
    String max ;
    String hummidty ;
    String description ;

    public String getHummidty() {
        return hummidty;
    }

    public void setHummidty(String hummidty) {
        this.hummidty = hummidty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
}
