/**
 * 
 */
package com.diarioas.guialigas.utils;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author robertosanchez
 * 
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {

	protected ArrayList<Fragment> fragments;

	/**
	 * @param fm
	 * @param fragments
	 */
	public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = (ArrayList<Fragment>) fragments;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see FragmentStatePagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int pos) {
		return fragments.get(pos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}

}
