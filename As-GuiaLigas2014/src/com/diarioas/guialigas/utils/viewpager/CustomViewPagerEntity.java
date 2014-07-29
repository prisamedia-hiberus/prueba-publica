/**
 * 
 */
package com.diarioas.guialigas.utils.viewpager;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author robertosanchez
 * 
 */
public class CustomViewPagerEntity extends CustomViewPagerMain {

	protected String teamName;

	public CustomViewPagerEntity(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param teamName
	 *            the teamName to set
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
}
