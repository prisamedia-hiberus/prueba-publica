package com.diarioas.guialigas.activities.stadiums;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.GeneralFragmentActivity;
import com.diarioas.guialigas.activities.stadiums.fragments.CityFragment;
import com.diarioas.guialigas.activities.stadiums.fragments.SedeFragment;
import com.diarioas.guialigas.activities.stadiums.fragments.StadiumFragment;
import com.diarioas.guialigas.dao.model.stadium.Stadium;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.RemoteStadiumsDAO;
import com.diarioas.guialigas.dao.reader.RemoteStadiumsDAO.RemoteStadiumDetailDAOListener;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.AlertManager;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.DrawableUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;
import com.diarioas.guialigas.utils.FragmentAdapter;
import com.diarioas.guialigas.utils.viewpager.CustomViewPagerStadium;

public class StadiumsDetailActivity extends GeneralFragmentActivity implements
		RemoteStadiumDetailDAOListener, OnPageChangeListener {

	private int idStadium;
	private Stadium stad;
	private CustomViewPagerStadium stadiumViewPager;
	private TextView nameStadium;
	private TextView nameCity;

	private List<Fragment> fragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stadiums_detail);
		configureView();
		startAnimation();
		idStadium = getIntent().getExtras().getInt("id");
		RemoteStadiumsDAO.getInstance(this).addDetailListener(this);
		RemoteStadiumsDAO.getInstance(this).getStadiumData(idStadium);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return false;
		}
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (stadiumViewPager != null) {
			callToOmniture(stadiumViewPager.getCurrentItem());
			callToAds(NativeAds.AD_STADIUMS+NativeAds.AD_DETAIL, false);
		}
	}

	private void callToOmniture(int pos) {
		if (pos == 0) {
			StatisticsDAO.getInstance(this).sendStatisticsState(
					getApplication(),
					Omniture.SECTION_SEDES,
					stad.getStadiumName(),
					null,
					null,
					Omniture.TYPE_ARTICLE,
					Omniture.DETAILPAGE_INFORMACION + " "
							+ stad.getStadiumName(), null);
		} else {
			StatisticsDAO.getInstance(this).sendStatisticsState(
					getApplication(), Omniture.SECTION_SEDES,
					stad.getCityName(), null, null, Omniture.TYPE_ARTICLE,
					Omniture.DETAILPAGE_INFORMACION + " " + stad.getCityName(),
					null);
		}
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		super.onBackPressed();
		overridePendingTransition(R.anim.null_anim, R.anim.slide_out_left);
	}

	private void configureView() {

		spinner = (RelativeLayout) findViewById(R.id.spinner);
		configActionBar();

		nameStadium = (TextView) findViewById(R.id.nameStadium);
		FontUtils.setCustomfont(getApplicationContext(), nameStadium,
				FontTypes.ROBOTO_REGULAR);
		nameStadium.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stadiumViewPager.setCurrentItem(0, true);

			}
		});

		nameCity = (TextView) findViewById(R.id.nameCity);
		FontUtils.setCustomfont(getApplicationContext(), nameStadium,
				FontTypes.ROBOTO_REGULAR);
		nameCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stadiumViewPager.setCurrentItem(1, true);

			}
		});

	}

	private void configViewPager() {
		stadiumViewPager = (CustomViewPagerStadium) findViewById(R.id.stadiumViewPager);

		fragments = getFragments();

		stadiumViewPager.setAdapter(new FragmentAdapter(
				getSupportFragmentManager(), fragments));

		stadiumViewPager.setCurrentItem(0, true);
		callToOmniture(0);
		stadiumViewPager.setOnPageChangeListener(this);

	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		Fragment fragment;
		Bundle args;

		fragment = new StadiumFragment();
		args = new Bundle();
		args.putInt("idStadium", idStadium);
		if (stad.getStadiumPhotos() != null
				&& stad.getStadiumPhotos().size() > 0)
			args.putStringArrayList("photos", stad.getStadiumPhotos());
		if (stad.getStadiumMap() != null && stad.getStadiumMap().length() > 0)
			args.putInt(
					"photoMap",
					DrawableUtils.getDrawableId(getApplicationContext(),
							stad.getStadiumMap(), 4));
		if (stad.getStadiumCapacity() > 0)
			args.putInt("capacity", stad.getStadiumCapacity());
		if (stad.getStadiumYear() > 0)
			args.putInt("year", stad.getStadiumYear());
		if (stad.getStadiumHistory() != null
				&& stad.getStadiumHistory().length() > 0)
			args.putString("history", stad.getStadiumHistory());

		args.putString("name", stad.getStadiumName());
		fragment.setArguments(args);
		fList.add(fragment);
		fragment = new CityFragment();
		args = new Bundle();
		args.putInt("idStadium", idStadium);
		if (stad.getCityState() != null && stad.getCityState().length() > 0)
			args.putString("state", stad.getCityState());
		if (stad.getCityPopulation() != null
				&& stad.getCityPopulation().length() > 0)
			args.putString("population", stad.getCityPopulation());
		if (stad.getCityAltitude() != null
				&& stad.getCityAltitude().length() > 0)
			args.putString("altitude", stad.getCityAltitude());
		if (stad.getCityHistory() != null && stad.getCityHistory().length() > 0)
			args.putString("history", stad.getCityHistory());
		if (stad.getCityTourism() != null && stad.getCityTourism().length() > 0)
			args.putString("tourism", stad.getCityTourism());
		if (stad.getCityTransport() != null
				&& stad.getCityTransport().length() > 0)
			args.putString("transport", stad.getCityTransport());
		if (stad.getCityEconomy() != null && stad.getCityEconomy().length() > 0)
			args.putString("economy", stad.getCityEconomy());
		if (stad.getCityPhotos() != null && stad.getCityPhotos().size() > 0)
			args.putStringArrayList("photos", stad.getCityPhotos());

		args.putString("name", stad.getCityName());
		fragment.setArguments(args);
		fList.add(fragment);
		return fList;
	}

	private void loadData() {
		nameStadium.setText(stad.getStadiumName());
		nameCity.setText(stad.getCityName());
		findViewById(R.id.headerLinear).setVisibility(View.VISIBLE);
		configViewPager();
		callToAds(NativeAds.AD_STADIUMS+NativeAds.AD_DETAIL, false);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ReturnRequestCodes.GALLERY_BACK) {
			if (resultCode == RESULT_OK) {
				Log.d("StadiumsDetailActivity", "BACK");
				callToAds(NativeAds.AD_STADIUMS+NativeAds.AD_DETAIL, false);
				try {
					if (data != null && data.getExtras() != null
							&& data.getExtras().containsKey("fragmentPos")) {
						int fragmentPos = (Integer) data.getExtras().get(
								"fragmentPos");
						int pos = 0;
						if (data.getExtras().containsKey("pos"))
							pos = (Integer) data.getExtras().get("pos");
						((SedeFragment) fragments.get(fragmentPos))
								.setViewPagerPosition(pos);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}
	}

	/**************** Metodos de RemoteStadiumDetailDAOListener *****************************/

	@Override
	public void onSuccessRemoteconfig(Stadium stadium) {
		RemoteStadiumsDAO.getInstance(this).removeDetailListener(this);

		stad = stadium;
		loadData();
		stopAnimation();
	}

	@Override
	public void onFailureRemoteconfig(Stadium stadium) {
		RemoteStadiumsDAO.getInstance(this).removeDetailListener(this);
		AlertManager.showAlertOkDialog(this,
				getResources().getString(R.string.stadium_error),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// onBackPressed();

					}

				});
		stad = DatabaseDAO.getInstance(getApplicationContext()).getStadium(
				idStadium);

		loadData();
		stopAnimation();
	}

	@Override
	public void onFailureNotConnection(Stadium stadium) {
		RemoteStadiumsDAO.getInstance(this).removeDetailListener(this);
		AlertManager.showAlertOkDialog(this,
				getResources().getString(R.string.stadium_not_conection),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// onBackPressed();
					}

				});
		stad = DatabaseDAO.getInstance(getApplicationContext()).getStadium(
				idStadium);
		loadData();
		stopAnimation();

	}

	/**************** Metodos de RemoteStadiumDetailDAOListener *****************************/

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
	public void onPageSelected(int pos) {
		if (pos == 0) {
			nameStadium
					.setTextColor(getResources().getColor(R.color.red_sedes));
			nameCity.setTextColor(getResources().getColor(R.color.gray_sedes));
		} else {
			nameStadium.setTextColor(getResources()
					.getColor(R.color.gray_sedes));
			nameCity.setTextColor(getResources().getColor(R.color.red_sedes));
		}

		callToOmniture(pos);

	}

	/**************** ViewPager Methods *****************************/

}
