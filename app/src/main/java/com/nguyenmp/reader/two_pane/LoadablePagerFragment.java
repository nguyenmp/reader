package com.nguyenmp.reader.two_pane;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.nguyenmp.reader.R;

public abstract class LoadablePagerFragment<CollectionType> extends PagerFragment<CollectionType> {
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ViewPager pager = (ViewPager) view.findViewById(R.id.view_pager);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private static final int THRESHOLD = 5;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int count = pager.getAdapter().getCount();
                if ((count - position) <= THRESHOLD) ((CollectionManager) getActivity()).loadMore();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
