package dataModels;

public class RecyclerViewModel {
    private  String  cityIcon ;
    private  String cityTemp ;
    private  String cityName ;

    public RecyclerViewModel(String cityIcon, String cityTemp, String cityName) {
        this.cityIcon = cityIcon;
        this.cityTemp = cityTemp;
        this.cityName = cityName;
    }

    public String getCityIcon() {
        return cityIcon;
    }

    public String getCityTemp() {
        return cityTemp;
    }

    public String getCityName() {
        return cityName;
    }

}
