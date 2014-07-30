package com.diarioas.guialigas.activities.news;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RelativeLayout;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.GeneralFragmentActivity;
import com.diarioas.guialigas.activities.news.fragment.NewsDetailFragment;
import com.diarioas.guialigas.dao.model.news.NewsItem;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.dao.reader.RemoteDataDAO;
import com.diarioas.guialigas.dao.reader.RemoteNewsDAO;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.Defines;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.FragmentAdapter;

public class NewsDetailActivity extends GeneralFragmentActivity implements
		OnPageChangeListener {

	private ViewPager newsViewPager;
//	private ImageFetcher mImageFetcher;
	private int currentCompetitionId;

	/***************************************************************************/
	/** Activity lifecycle methods **/
	/***************************************************************************/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_news_detail);
		configActionBar();
		currentCompetitionId = RemoteDataDAO
				.getInstance(getApplicationContext()).getGeneralSettings()
				.getCurrentCompetition().getId();
		spinner = (RelativeLayout) findViewById(R.id.spinner);
		configView();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ImageDAO.getInstance(this).exitNewsTaskEarly();
		callToAds(NativeAds.AD_STADIUMS + "/" + NativeAds.AD_DETAIL, false);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ImageDAO.getInstance(this).exitNewsTaskEarly();
		ImageDAO.getInstance(this).flushNewsCache();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		ImageDAO.getInstance(this).clearNewsCache();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		super.onBackPressed();
		overridePendingTransition(R.anim.grow_from_middle,
				R.anim.shrink_to_middle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.actionbar_share, menu);
		return super.onCreateOptionsMenu(menu);
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

	/***************************************************************************/
	/** Activity lifecycle methods **/
	/***************************************************************************/

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

	private void configView() {
		newsViewPager = (ViewPager) findViewById(R.id.newsViewPager);

		List<Fragment> fragments = getFragments();

		newsViewPager.setAdapter(new FragmentAdapter(
				getSupportFragmentManager(), fragments));
		int currentIndex = getIntent().getExtras().getInt("position");
		newsViewPager.setCurrentItem(currentIndex, true);
		NewsItem item = RemoteNewsDAO.getInstance(getApplicationContext())
				.getNewsPreloaded(currentCompetitionId, currentIndex);
		callToOmniture(item.getTitle());
		newsViewPager.setOnPageChangeListener(this);

	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();
		int competitionId = RemoteDataDAO.getInstance(getApplicationContext())
				.getGeneralSettings().getCurrentCompetition().getId();
		ArrayList<NewsItem> newsArray = RemoteNewsDAO.getInstance(this)
				.getNewsPreloaded(competitionId);

		NewsDetailFragment newsDetailFragment;
		Bundle args;
		// for (NewsItem newsItem : newsArray) {
		for (int i = 0; i < newsArray.size(); i++) {
			newsDetailFragment = new NewsDetailFragment();
			args = new Bundle();
			args.putInt("itemNumber", i);
			newsDetailFragment.setArguments(args);
			fList.add(newsDetailFragment);
		}

		return fList;
	}


	/**************** ViewPager Methods *****************************/
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {

		NewsItem item = RemoteNewsDAO.getInstance(getApplicationContext())
				.getNewsPreloaded(currentCompetitionId, arg0);
		callToOmniture(item.getTitle());

	}

	/**************** ViewPager Methods *****************************/
	/***************************************************************************/
	/** Libraries methods **/
	/***************************************************************************/

	protected void callToOmniture(String title) {
		StatisticsDAO.getInstance(getApplicationContext()).sendStatisticsState(
				getApplication(), Omniture.SECTION_NEWS,
				Omniture.SUBSECTION_NEWSDETAIL, null, null,
				Omniture.TYPE_PORTADA, title, null);
	}
}
