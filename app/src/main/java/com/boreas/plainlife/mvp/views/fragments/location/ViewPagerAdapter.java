package com.boreas.plainlife.mvp.views.fragments.location;

import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.boreas.plainlife.base.BaseFragment;

import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<BaseFragment> mList;


    public ViewPagerAdapter(FragmentManager fm, List<BaseFragment> mList) {
        super(fm);
        this.mList = mList;
        notifyDataSetChanged();
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public BaseFragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        return POSITION_NONE;
//    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getName();
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
    }
}
