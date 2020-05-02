package dialogBoxes;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import mobin.io.weatherapp.R;

public class LoadingDialog  {

    private Activity myactivity ;
    private AlertDialog alertDialog  ;
    public LoadingDialog( Activity activity) {

       myactivity = activity ;
    }
    public void startDialog(){

       AlertDialog.Builder builder = new AlertDialog.Builder(myactivity);

        LayoutInflater layoutInflater = myactivity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.dialog_loading ,null));
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void  dissmisDialog (){

        alertDialog.dismiss();
    }
}
