package dataModels;

public class RecyclerViewModel {
    private  String country;
    private  String cityTemp ;
    private  String cityName ;



    public RecyclerViewModel(String country, String cityTemp, String cityName) {
        this.country = country;
        this.cityTemp = cityTemp;
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public String getCityTemp() {
        return cityTemp;
    }

    public String getCityName() {
        return cityName;
    }

}
