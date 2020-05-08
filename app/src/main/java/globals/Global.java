package globals;

import android.app.Activity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import fragments.showDetailFragment;
import mobin.io.weatherapp.R;



enum FragmentsAvailable {

    DETAIL
}

public class Global extends FragmentActivity {

    public void changeFragment(Fragment newFragment, FragmentsAvailable newFragmentType, boolean withoutAnimation) {


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        try {
            getSupportFragmentManager().popBackStackImmediate(newFragmentType.toString(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (java.lang.IllegalStateException e) {

        }

        transaction.addToBackStack(newFragmentType.toString());
        transaction.replace(R.id.fl_main, newFragment);
        transaction.commitAllowingStateLoss();

    }
}
