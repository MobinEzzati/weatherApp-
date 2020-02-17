package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiagosantos.enchantedviewpager.EnchantedViewPager;
import com.tiagosantos.enchantedviewpager.EnchantedViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import dataModels.TestModel;
import dataModels.WeatherModel;
import mobin.io.weatherapp.R;

public class ViewPagerAdapter extends EnchantedViewPagerAdapter {
    Context context ;
    private List<TestModel> testModels;
    TextView tv_temperature;

    public ViewPagerAdapter(List<TestModel> testModels , Context context) {
        super(testModels);
        this.context = context ;
        this.testModels = testModels;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewGroup rootView = (ViewGroup) layoutInflater.inflate(R.layout.vp_main_model , container , false);
        tv_temperature = rootView.findViewById(R.id.tv_vp_temp);
        tv_temperature.setText(testModels.get(position).getName());
        rootView.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);
        container.addView(rootView);
     return rootView ;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return  view == object ;
    }
}
