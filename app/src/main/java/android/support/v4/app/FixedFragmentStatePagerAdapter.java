package android.support.v4.app;

import android.os.Bundle;
import android.view.ViewGroup;

/**
 * This is a fix for a known Android issue with
 * Fragment Saved State Adapters.
 * @author Tsuyoshi Murakami
 *
 * http://code.google.com/u/103081586335718071220/
 *
 * http://code.google.com/p/android/issues/detail?id=37484#c1
 */
public abstract class FixedFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    public FixedFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);

        Bundle savedFragmentState = fragment.mSavedFragmentState;
        if (savedFragmentState != null) {
            savedFragmentState.setClassLoader(fragment.getClass().getClassLoader());
        }

        return fragment;
    }

}
