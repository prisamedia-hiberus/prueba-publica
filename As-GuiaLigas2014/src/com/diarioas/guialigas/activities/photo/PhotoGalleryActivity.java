package com.diarioas.guialigas.activities.photo;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.GeneralFragmentActivity;
import com.diarioas.guialigas.activities.photo.fragment.PhotoSectionGalleryFragment;
import com.diarioas.guialigas.dao.model.news.GalleryMediaItem;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.dao.reader.RemoteGalleryDAO;
import com.diarioas.guialigas.dao.reader.RemoteGalleryDAO.RemotePhotosDetailDAOListener;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;
import com.diarioas.guialigas.utils.FragmentAdapter;

public class PhotoGalleryActivity extends GeneralFragmentActivity implements
		OnPageChangeListener, RemotePhotosDetailDAOListener {

	private ViewPager photoGalleryViewPager;
	private GalleryMediaItem gallery;
	private View upperBar;
	private View bottomBar;
	private TextView title;

	/***************************************************************************/
	/** Activity lifecycle methods **/
	/***************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_photogallery);

		int position = getIntent().getExtras().getInt("gallery");

		configureView();

		if (RemoteGalleryDAO.getInstance(getApplicationContext())
				.isGalleryDetailPreLoaded(position)) {
			gallery = RemoteGalleryDAO.getInstance(getApplicationContext())
					.getGalleryDetailPreloaded(position);

			loadData();
		} else {
			RemoteGalleryDAO.getInstance(getApplicationContext())
					.addDetailListener(this);
			RemoteGalleryDAO.getInstance(getApplicationContext())
					.getDetailGallery(position);
		}

		if (savedInstanceState != null) {

			int pagerPosition = savedInstanceState.getInt("position");
			if (photoGalleryViewPager != null)
				photoGalleryViewPager.setCurrentItem(pagerPosition);
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putInt("position", photoGalleryViewPager.getCurrentItem());
		super.onSaveInstanceState(outState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {

		setResult(Activity.RESULT_OK);

		super.onBackPressed();
		overridePendingTransition(R.anim.null_anim, R.anim.slide_out_left);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ImageDAO.getInstance(this).exitGalleryTaskEarly();
		callToAds(NativeAds.AD_PHOTOS + "/" + NativeAds.AD_DETAIL, false);

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ImageDAO.getInstance(this).exitGalleryTaskEarly();
		ImageDAO.getInstance(this).flushGalleryCache();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		ImageDAO.getInstance(this).clearCache();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImageDAO.getInstance(this).closeGalleryCache();
		ImageDAO.getInstance(this).eraseGalleryCache();
		if (photoGalleryViewPager != null) {
			photoGalleryViewPager.removeAllViews();
			photoGalleryViewPager = null;
		}
		gallery = null;

	}

	/***************************************************************************/
	/** Activity lifecycle methods **/
	/***************************************************************************/

	private void configureView() {

		configureTitle();

		findViewById(R.id.iconBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();

			}
		});

		findViewById(R.id.iconShare).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				share();

			}
		});

		upperBar = findViewById(R.id.upperBar);
		bottomBar = findViewById(R.id.bottomBar);
	}

	private void configureTitle() {
		title = (TextView) findViewById(R.id.title);
		FontUtils.setCustomfont(getApplicationContext(), title,
				FontTypes.HELVETICANEUE);
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

	private void loadData() {
		configureViewPager();
		callToOmniture((String) title.getText());

	}

	private void loadError() {
		findViewById(R.id.photoGalleryViewPager).setVisibility(View.GONE);
		findViewById(R.id.errorContent).setVisibility(View.VISIBLE);
		configureTitle();
		setTitleGallery(getIntent().getExtras().getString("galleryTitle"));
		toogleBarsVisibility();
		callToOmniture((String) title.getText());
	}

	@SuppressLint("NewApi")
	private void configureViewPager() {
		photoGalleryViewPager = (ViewPager) findViewById(R.id.photoGalleryViewPager);
		photoGalleryViewPager.setOnPageChangeListener(this);
		photoGalleryViewPager.setAdapter(new FragmentAdapter(
				getSupportFragmentManager(), getFragments()));
		// Set First element
		photoGalleryViewPager.setCurrentItem(0, true);
		setTitleGallery(gallery.getPhotos().get(0).getCaption());
		photoGalleryViewPager.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							photoGalleryViewPager.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						} else {
							photoGalleryViewPager.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						}
						toogleBarsVisibility();

					}
				});
	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		Fragment fragment;
		Bundle args;
		for (int i = 0; i < gallery.getPhotos().size(); i++) {
			fragment = new PhotoSectionGalleryFragment();
			args = new Bundle();
			args.putInt("pos", i);
			args.putSerializable("photoItem", gallery.getPhotos().get(i));
			fragment.setArguments(args);
			fList.add(fragment);

		}

		return fList;
	}


	public void toogleBarsVisibility() {
		if (upperBar.getVisibility() == View.VISIBLE) {
			animOutUp(upperBar, R.anim.actionbar_item_slide_out_up, View.GONE);
			animOutUp(bottomBar, R.anim.actionbar_item_slide_out_down,
					View.GONE);
		} else {
			animOutUp(upperBar, R.anim.actionbar_item_slide_in_up, View.VISIBLE);
			animOutUp(bottomBar, R.anim.actionbar_item_slide_in_down,
					View.VISIBLE);
		}

	}

	private void animOutUp(final View view, int animId, final int visivility) {
		Animation animOut = AnimationUtils.loadAnimation(
				getApplicationContext(), animId);
		AnimationListener animationOutListener = new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				view.setVisibility(visivility);

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		};
		animOut.setAnimationListener(animationOutListener);
		view.startAnimation(animOut);
	}

	private void setTitleGallery(String caption) {
		if (caption != null && !caption.equalsIgnoreCase(""))
			title.setText(caption);
		else
			title.setText(gallery.getTitle());
	}

	/***************************************************************************/
	/** Libraries methods **/
	/***************************************************************************/

	protected void callToOmniture(String title) {
		StatisticsDAO.getInstance(getApplicationContext()).sendStatisticsState(
				getApplication(), Omniture.SECTION_PHOTOS,
				Omniture.SUBSECTION_GALLERY, null, null, Omniture.TYPE_GALLERY,
				title, null);
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
		setTitleGallery(gallery.getPhotos().get(arg0).getCaption());
		callToOmniture((String) title.getText());
	}

	/**************** ViewPager Methods *****************************/
	@Override
	public void onSuccessDetailRemoteconfig(GalleryMediaItem gallery) {
		RemoteGalleryDAO.getInstance(getApplicationContext())
				.removeDetailListener(this);
		this.gallery = gallery;
		if (gallery != null && gallery.getPhotos().size() > 0) {
			loadData();
		} else {
			loadError();

		}

	}

	@Override
	public void onFailureDetailRemoteconfig() {
		RemoteGalleryDAO.getInstance(getApplicationContext())
				.removeDetailListener(this);
		loadError();
	}

	@Override
	public void onFailureDetailNotConnection() {
		RemoteGalleryDAO.getInstance(getApplicationContext())
				.removeDetailListener(this);
		loadError();
	}

}
