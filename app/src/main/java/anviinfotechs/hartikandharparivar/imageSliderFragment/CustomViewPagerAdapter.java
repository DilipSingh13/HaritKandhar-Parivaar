package anviinfotechs.hartikandharparivar.imageSliderFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CustomViewPagerAdapter extends FragmentStatePagerAdapter {

    public CustomViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        if (position == 0) {
            fragment = new FragmentSliderOne();
        } else if (position == 1) {
            fragment = new FragmentSliderTwo();
        }else if (position == 2) {
            fragment = new FragmentSliderThree();
        }
        else if (position == 3) {
            fragment = new FragmentSliderFour();
        }
        else {
            fragment = new FragmentSliderFifth();
        }

        return fragment;
    }

    @Override
    public int getCount() {

        return 5;
    }
}
