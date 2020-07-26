package globals;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;



  public class Global {

      public  enum FragName {

          LoginFrag,
          SHOWDETAIL_FRAG

      }

      public static  class global extends FragmentActivity {


          public static void toast(Context context , String text){

              Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
          }
      }

      public static void toast(Context context , String text){
          Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
      }
}
