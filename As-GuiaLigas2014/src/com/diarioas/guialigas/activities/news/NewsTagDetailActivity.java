package com.diarioas.guialigas.activities.news;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MenuItem;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.GeneralFragmentActivity;
import com.diarioas.guialigas.activities.news.fragment.NewsTagDetailFragment;
import com.diarioas.guialigas.dao.model.news.NewsItemTag;
import com.diarioas.guialigas.dao.reader.RemoteNewsDAO;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.Defines;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;

public class NewsTagDetailActivity extends GeneralFragmentActivity implements
		OnPageChangeListener {

	private ArrayList<NewsItemTag> newsList = new ArrayList<NewsItemTag>();

	private ViewPager newsViewPager;
	private Integer pos = -1;
	private Integer originalPos = -1;

	/***************************************************************************/
	/** Activity lifecycle methods **/
	/***************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tag_detail_activity);

		pos = (Integer) getIntent().getExtras().get("position");
		originalPos = (Integer) getIntent().getExtras().get("position");
		ArrayList<NewsItemTag> news = (ArrayList<NewsItemTag>) getIntent()
				.getExtras().get("news");

		configActionBar();
		fillNewsArray(news);
		configureView();
	}

	@Override
	public void onResume() {
		super.onResume();
		callToBannerAction(NativeAds.AD_NEWS + NativeAds.AD_DETAIL);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.actionbar_share_square, menu);
//		return super.onCreateOptionsMenu(menu);
//	}

	private void fillNewsArray(ArrayList<NewsItemTag> currentItems) {
		int numItems = currentItems.size();
		for (int i = 0; i < numItems; i++) {
			if (currentItems.get(i) != null
					&& currentItems.get(i).getJumpToWeb() != Defines.JUMP_TO_WEB_OK
					&& currentItems.get(i).getLink() != null
					&& !RemoteNewsDAO.getInstance(getApplicationContext())
							.isURLNeedOut(currentItems.get(i).getLink())) {
				this.newsList.add(currentItems.get(i));
			} else if (i <= originalPos) {
				pos = pos - 1;
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		stopCurrentWebAction();
	}

	@Override
	public void onStop() {
		super.onStop();
		this.newsViewPager = null;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return false;
		case R.id.share:
			share();
			break;
		}
		return true;
	}

	private void share() {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");

		i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
		String body = "Mensaje";

		i.putExtra(Intent.EXTRA_TEXT, body);
		startActivity(Intent.createChooser(i,
				getString(R.string.share_mens_title)
						+ getString(R.string.app_name)));
	}

	/***************************************************************************/
	/** Activity lifecycle methods **/
	/***************************************************************************/

	private void configureView() {
		newsViewPager = (ViewPager) findViewById(R.id.newsViewPager);
		newsViewPager.setAdapter(new PageAdapterWebViews(
				getSupportFragmentManager(), newsList));
		newsViewPager.setCurrentItem(pos, true);
		newsViewPager.setOffscreenPageLimit(0);

//		if (newsList.size() > pos) {
//			NewsItem item = newsList.get(pos);
//			callToOmniture(item.getTitle());
//		}
		newsViewPager.setOnPageChangeListener(this);

	}

	public static class PageAdapterWebViews extends FragmentStatePagerAdapter {

		ArrayList<NewsItemTag> currenNews = null;
		boolean isFirstItemLoaded = true;

		public PageAdapterWebViews(FragmentManager fragmentManager,
				ArrayList<NewsItemTag> news) {
			super(fragmentManager);
			currenNews = news;
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return currenNews.size();
		}

		// Returns the fragment to display for that page
		@Override
		public Fragment getItem(int position) {

			Log.v("Pedimos la pagina", "Pedimos la pagina: " + position);
			Fragment fragment = new NewsTagDetailFragment();
			Bundle args = new Bundle();

			args.putInt("position", position);
			args.putSerializable("currentNews", currenNews.get(position));
			if (isFirstItemLoaded) {
				isFirstItemLoaded = false;
				args.putBoolean("needAutoLoad", true);
			}

			fragment.setArguments(args);

			return fragment;

		}
	}

	private void stopCurrentWebAction() {

		if (this.newsList != null) {
			int numFragments = this.newsList.size();
			for (int i = 0; i < numFragments; i++) {
				Fragment fragment = (Fragment) getSupportFragmentManager()
						.findFragmentByTag(
								"android:switcher:" + R.id.newsViewPager + ":"
										+ i);
				if (fragment != null
						&& (fragment instanceof NewsTagDetailFragment)) {
					NewsTagDetailFragment currentFragment = (NewsTagDetailFragment) fragment;
					currentFragment.pauseDetailWebView();
				}
			}
		}

	}

	/***************************************************************************/
	/** OnPageChangeListener methods **/
	/***************************************************************************/
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int newPos) {
		stopCurrentWebAction();
		this.pos = newPos;
//		NewsItem item = newsList.get(pos);
//		callToOmniture(item.getTitle());
	}

	/************** Not allowing call Ads or Statistics **************/

	protected void callToOmniture(String title) {
		StatisticsDAO.getInstance(getApplicationContext()).sendStatisticsState(
				getApplication(), Omniture.SECTION_NEWS,
				Omniture.SUBSECTION_NEWSDETAIL, null, null,
				Omniture.TYPE_PORTADA, title, null);
	}

}
