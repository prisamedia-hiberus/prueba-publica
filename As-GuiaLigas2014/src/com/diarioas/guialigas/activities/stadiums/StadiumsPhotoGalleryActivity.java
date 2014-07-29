package com.diarioas.guialigas.activities.stadiums;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.GeneralFragmentActivity;
import com.diarioas.guialigas.activities.stadiums.fragments.PhotoGalleryFSFragment;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.Defines;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.FragmentAdapter;
import com.diarioas.guialigas.utils.bitmapfun.ImageCache.ImageCacheParams;
import com.diarioas.guialigas.utils.bitmapfun.ImageFetcher;

public class StadiumsPhotoGalleryActivity extends GeneralFragmentActivity
		implements OnPageChangeListener {

	private static final int CIRCLE_ID = 8000;
	private ImageFetcher mImageFetcher;
	private ViewPager photoGalleryViewPager;
	private ArrayList<String> urls;
	private LinearLayout circleIndicator;
	private int fragmentPos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_stadiums_photogallery);

		int idStadium = getIntent().getExtras().getInt("idStadium");
		fragmentPos = getIntent().getExtras().getInt("fragmentPos");
		if (fragmentPos == 0)
			urls = DatabaseDAO.getInstance(getApplicationContext())
					.getPhotoStadium(idStadium,
							Defines.STADIUM_IMAGE_TYPE.TYPE_STADIUM);
		else
			urls = DatabaseDAO.getInstance(getApplicationContext())
					.getPhotoStadium(idStadium,
							Defines.STADIUM_IMAGE_TYPE.TYPE_CITY);

		if (urls != null && urls.size() > 0) {
			configureImageFetcher();
			configureView();
		} else {
			onBackPressed();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		Intent newIntent = new Intent();
		newIntent.putExtra("pos", photoGalleryViewPager.getCurrentItem());
		newIntent.putExtra("fragmentPos", fragmentPos);
		setResult(Activity.RESULT_OK, newIntent);

		super.onBackPressed();
		overridePendingTransition(R.anim.null_anim, R.anim.slide_out_left);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
		String name = getIntent().getExtras().getString("name");
		if (name != null && name.length() > 0)
			StatisticsDAO.getInstance(this).sendStatisticsState(
					getApplication(), Omniture.SECTION_SEDES, name,
					Omniture.SUBSUBSECTION_GALLERY, null,
					Omniture.TYPE_ARTICLE,
					Omniture.DETAILPAGE_FOTOGALERIA + " " + name + " ", null);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mImageFetcher.setExitTasksEarly(false);
		mImageFetcher.flushCache();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		mImageFetcher.clearCache();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mImageFetcher.closeCache();
		if (photoGalleryViewPager != null) {
			photoGalleryViewPager.removeAllViews();
			photoGalleryViewPager = null;
		}
		if (urls != null) {
			urls.clear();
			urls = null;
		}
	}

	private void configureImageFetcher() {
		ImageCacheParams cacheParams = new ImageCacheParams(this,
				Defines.NAME_CACHE_THUMBS + "2");
		cacheParams.setMemCacheSizePercent(0.25f);

		mImageFetcher = new ImageFetcher(this, getResources()
				.getDimensionPixelSize(R.dimen.image_image_height));
		// mImageFetcher
		// .setLoadingImage(R.drawable.galeria_imagenrecurso_fullscreen);

		mImageFetcher.addImageCache(this.getSupportFragmentManager(),
				cacheParams);

	}

	private void configureView() {

		circleIndicator = (LinearLayout) findViewById(R.id.circleIndicator);

		photoGalleryViewPager = (ViewPager) findViewById(R.id.photoGalleryViewPager);
		photoGalleryViewPager.setOnPageChangeListener(this);
		photoGalleryViewPager.setAdapter(new FragmentAdapter(
				getSupportFragmentManager(), getFragments()));

		setInitialPosition();
	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		Fragment fragment;
		Bundle args;
		String type = getIntent().getExtras().getString("type");
		for (int i = 0; i < urls.size(); i++) {

			fragment = new PhotoGalleryFSFragment();
			args = new Bundle();
			args.putInt("fragmentPos", fragmentPos);
			args.putInt("pos", i);
			args.putString("url", urls.get(i));
			args.putString("type", type);
			fragment.setArguments(args);
			fList.add(fragment);

			circleIndicator.addView(getCircle(i));
		}

		return fList;
	}

	private View getCircle(int id) {
		ImageView circle = new ImageView(getApplicationContext());
		circle.setId(CIRCLE_ID + id);
		circle.setBackgroundResource(R.drawable.paginador_off);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = DimenUtils.getRegularPixelFromDp(
				getApplicationContext(), 5);
		params.rightMargin = params.leftMargin;
		circle.setLayoutParams(params);
		return circle;
	}

	private void setInitialPosition() {
		int pos = getIntent().getExtras().getInt("pos");
		photoGalleryViewPager.setCurrentItem(pos, true);
		circleIndicator.findViewById(CIRCLE_ID + pos).setBackgroundResource(
				R.drawable.paginador_on);

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
		if (circleIndicator != null) {
			for (int i = 0; i < circleIndicator.getChildCount(); i++) {
				if (i == arg0)
					circleIndicator.findViewById(CIRCLE_ID + i)
							.setBackgroundResource(R.drawable.paginador_on);
				else
					circleIndicator.findViewById(CIRCLE_ID + i)
							.setBackgroundResource(R.drawable.paginador_off);
			}
		}

	}

	/**************** ViewPager Methods *****************************/

	public ImageFetcher getmImageFetcher() {
		// TODO Auto-generated method stub
		return mImageFetcher;
	}
}
