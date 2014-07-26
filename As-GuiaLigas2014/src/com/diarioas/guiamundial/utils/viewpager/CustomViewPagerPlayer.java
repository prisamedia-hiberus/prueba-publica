/**
 * 
 */
package com.diarioas.guiamundial.utils.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.diarioas.guiamundial.R;

/**
 * @author robertosanchez
 * 
 */
public class CustomViewPagerPlayer extends CustomViewPagerEntity {

	private static final int POS_FRAGMENT_PALMARES = 0;
	private static final int POS_FRAGMENT_INFO = 1;
	private static final int POS_FRAGMENT_TRAYECTORIA = 2;

	private static final int TARGET_FRAGMENT = 1;

	private String playerName;

	public CustomViewPagerPlayer(Context context, AttributeSet attrs) {
		super(context, attrs);
		childIds.add(R.id.yearsPlayerSroll);
		childIds.add(R.id.competitionPlayerSroll);
		// childIds.add(R.id.teamPlayerSroll);
	}

	/**
	 * @param playerName
	 *            the playerName to set
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {

		if (activeFragment == TARGET_FRAGMENT) {

			if (findChild(event.getRawX(), event.getRawY()))
				return false;
		}
		return super.onInterceptTouchEvent(event);
	}

	@Override
	public void setActiveFragment(int active) {
		this.activeFragment = active;
		// if (teamName != null)
		// switch (activeFragment) {
		// case POS_FRAGMENT_PALMARES:
		// StatisticsDAO.getInstance(mContext).sendStatisticsState(,
		// teamName.toLowerCase(),
		// Defines.Ommniture.SUBSECTION_PLAYER_PLANTILLA,
		// Defines.Ommniture.SUBSUBSECTION_PLAYER_FICHA,
		// Defines.Ommniture.TEMA_PLAYER_PALMARES,
		// Defines.Ommniture.HOMEPAGE_CONTENT_TYPE,
		// playerName.toLowerCase(), null);
		// break;
		// case POS_FRAGMENT_INFO:
		// StatisticsDAO.getInstance(mContext).sendStatisticsState(,
		// teamName.toLowerCase(),
		// Defines.Ommniture.SUBSECTION_PLAYER_PLANTILLA,
		// Defines.Ommniture.SUBSUBSECTION_PLAYER_FICHA,
		// Defines.Ommniture.TEMA_PLAYER_INFO,
		// Defines.Ommniture.HOMEPAGE_CONTENT_TYPE,
		// playerName.toLowerCase(), null);
		// break;
		// case POS_FRAGMENT_TRAYECTORIA:
		// StatisticsDAO.getInstance(mContext).sendStatisticsState(,
		// teamName.toLowerCase(),
		// Defines.Ommniture.SUBSECTION_PLAYER_PLANTILLA,
		// Defines.Ommniture.SUBSUBSECTION_PLAYER_FICHA,
		// Defines.Ommniture.TEMA_PLAYER_TRAYECTORIA,
		// Defines.Ommniture.HOMEPAGE_CONTENT_TYPE,
		// playerName.toLowerCase(), null);
		// break;
		//
		// default:
		// break;
		// }
	}
}
