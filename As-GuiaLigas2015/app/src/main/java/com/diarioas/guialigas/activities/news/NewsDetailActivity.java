package com.diarioas.guialigas.activities.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.GeneralFragmentActivity;
import com.diarioas.guialigas.activities.news.fragment.NewsDetailFragment;
import com.diarioas.guialigas.dao.model.news.NewsItem;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsDetailActivity extends GeneralFragmentActivity implements
		OnPageChangeListener {

	private ViewPager newsViewPager;
	private Integer pos = -1;
	private Integer originalPos = -1;
	

	private ArrayList<NewsItem> newsList = new ArrayList<NewsItem>();

	/***************************************************************************/
	/** Activity lifecycle methods **/
	/***************************************************************************/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_news_detail);
		
		pos = (Integer) getIntent().getExtras().get("position");
		originalPos = (Integer) getIntent().getExtras().get("position");
		
		ArrayList<NewsItem> news = (ArrayList<NewsItem>) getIntent().getExtras().get("news");

		configActionBar();
		fillNewsArray(news);
		configureView();

	}
	
	@Override
	public void onStop() {
		super.onStop();
		this.newsViewPager = null;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		callToAds();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_menu, menu);
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
	
	

	private void fillNewsArray(ArrayList<NewsItem> currentItems) {
		int numItems = currentItems.size();
		for (int i = 0; i < numItems; i++) {
			if (currentItems.get(i) != null) {
				this.newsList.add(currentItems.get(i));
			} else if (i <= originalPos) {
				pos = pos - 1;
			}
		}
	}

	
	private void share() {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");

		i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
		
		if ((newsList!=null)&&(newsList.size()>pos)&&(pos>=0)) {
			NewsItem nItem = (NewsItem)newsList.get(pos);
			if (nItem!=null) {
				String body = nItem.getTitle()+" "+nItem.getUrlDetail();

				i.putExtra(Intent.EXTRA_TEXT, body);
				startActivity(Intent.createChooser(i,
						getString(R.string.share_mens_title)
								+ getString(R.string.app_name)));

				StatisticsDAO.getInstance(getApplicationContext()).sendStatisticsShare(getApplication(),
						nItem.getTitle(),
						Omniture.SECTION_NEWS,
						Omniture.SUBSECTION_NEWSDETAIL,
						null);
			}

		}
	}
	
	private void configureView() {
		newsViewPager = (ViewPager) findViewById(R.id.newsViewPager);

		List<Fragment> fragments = getFragments();

		newsViewPager.setAdapter(new FragmentAdapter(
				getSupportFragmentManager(), fragments));
		int currentIndex = getIntent().getExtras().getInt("position");
		newsViewPager.setCurrentItem(currentIndex, true);
		NewsItem item = newsList.get(currentIndex);
		callToOmniture(item.getTitle());
		newsViewPager.setOnPageChangeListener(this);

	}
	
	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		NewsDetailFragment newsDetailFragment;
		Bundle args;
		// for (NewsItem newsItem : newsArray) {
		for (int i = 0; i < newsList.size(); i++) {
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
		NewsItem item = newsList.get(pos);
		callToOmniture(item.getTitle());
		
	}

	private void callToAds() {
		String section = NativeAds.AD_NEWS+NativeAds.AD_DETAIL;
		callToAds(section, false);
	}
	/***************************************************************************/
	/** Libraries methods **/
	/***************************************************************************/

	protected void callToOmniture(String title) {
		StatisticsDAO.getInstance(getApplicationContext()).sendStatisticsState(
				getApplication(),
				Omniture.SECTION_NEWS,
				Omniture.SUBSECTION_NEWSDETAIL,
				null,
				null,
				Omniture.TYPE_PORTADA,
				title,
				null);
	}




}
