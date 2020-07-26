package dialogBoxes;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;
import mobin.io.weatherapp.R;

public class InternetDialog extends AppCompatDialogFragment  {
    private Activity myactivity ;
    private AlertDialog alertDialog  ;
    public InternetDialog(Activity activity) {
        myactivity = activity ;
    }


    public void startDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(myactivity);

        LayoutInflater layoutInflater = myactivity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.internet_dialog ,null));
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();
    }
    public void  dissmisDialog (){

        alertDialog.dismiss();
    }

}


