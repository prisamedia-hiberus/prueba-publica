/**
 * 
 */
package com.diarioas.guialigas.activities.carrusel;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.GeneralFragmentActivity;
import com.diarioas.guialigas.activities.carrusel.fragment.CarruselDirectoFragment;
import com.diarioas.guialigas.activities.carrusel.fragment.CarruselFragment;
import com.diarioas.guialigas.activities.carrusel.fragment.CarruselGameSystemFragment;
import com.diarioas.guialigas.activities.carrusel.fragment.CarruselResumenFragment;
import com.diarioas.guialigas.activities.carrusel.fragment.CarruselSpadesFragment;
import com.diarioas.guialigas.activities.carrusel.fragment.CarruselStatsFragment;
import com.diarioas.guialigas.dao.model.calendar.Match;
import com.diarioas.guialigas.dao.reader.CarruselDAO;
import com.diarioas.guialigas.dao.reader.CarruselDAO.CarruselDAODetailListener;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.Defines.CarruselDetail;
import com.diarioas.guialigas.utils.Defines.DateFormat;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.Defines.RequestTimes;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;
import com.diarioas.guialigas.utils.FragmentAdapter;
import com.diarioas.guialigas.utils.comparator.CarruselComparator;
import com.diarioas.guialigas.utils.scroll.CustomHoizontalScroll;
import com.diarioas.guialigas.utils.scroll.CustomHoizontalScroll.ScrollEndListener;
import com.diarioas.guialigas.utils.viewpager.CustomViewPagerLeague;

/**
 * @author robertosanchez
 * 
 */
public class CarruselDetailActivity extends GeneralFragmentActivity implements
		ViewPager.OnPageChangeListener, ScrollEndListener,
		CarruselDAODetailListener {

	private static final int SCROLL_WITH = 5;

	private static final int PREV_BUTTON = 201;
	private static final int POST_BUTTON = 202;

	private ViewPager viewPager;

	private CustomHoizontalScroll detailSroll;
	private ImageView buttonPrev;
	private ImageView buttonNext;

	private int width;
	private int widthButton;
	private ArrayList<String> details;

	private Match match;

	private int idShieldLocal;
	private int idShieldAway;

	private long timeToUpdate;
	private long timeToUpdateOld;
	private Timer t;

	private TextView leftTeamText;
	private TextView rightTeamText;
	private TextView stateTeamText;
	private RelativeLayout resultContainer;
	private TextView stateTeamMinText;
	private TextView resultText;

	private int stateCode;
	private int stateCodeOld = -1;

	private List<Fragment> fragments;

	private ImageView localShield;

	private ImageView awayShield;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_carrusel_detail);
		spinner = (RelativeLayout) findViewById(R.id.spinner);

		Point size = DimenUtils.getSize(getWindowManager());
		width = size.x;
		widthButton = size.x / SCROLL_WITH;

		details = new ArrayList<String>();

		timeToUpdate = RequestTimes.TIMER_CARRUSEL_UPDATE_NO_ACTIVE;
		timeToUpdateOld = timeToUpdate;


		configActionBar();
		configureView();
		startAnimation();
		// getData();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		startTimer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (t != null) {
			t.cancel();
			t = null;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ImageDAO.getInstance(this).exitPlayerTaskEarly();
		callToAds(NativeAds.AD_CARROUSEL + "/" + NativeAds.AD_DETAIL, false);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ImageDAO.getInstance(this).exitPlayerTaskEarly();
		ImageDAO.getInstance(this).flushPlayerCache();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		CarruselDAO.getInstance(getApplicationContext()).clean();

		if (viewPager != null) {
			viewPager.removeAllViews();
			viewPager = null;
		}
		if (detailSroll != null) {
			detailSroll.removeAllViews();
			detailSroll = null;
		}
		if (details != null) {
			details.clear();
			details = null;
		}
		match = null;
		if (fragments != null) {
			fragments.clear();
			fragments = null;
		}
		if (t!=null){
			t.cancel();
			t=null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onLowMemory()
	 */
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		ImageDAO.getInstance(this).clearCache();
	}

	/**
	 * 
	 */
	private void configureView() {

		// Imagen Local
		localShield = (ImageView) findViewById(R.id.localShield);
		idShieldLocal = getIntent().getIntExtra("idLocal", 0);

		// Imagen visitante
		awayShield = (ImageView) findViewById(R.id.awayShield);
		idShieldAway = getIntent().getIntExtra("idAway", 0);

		// Nombre Local
		leftTeamText = (TextView) findViewById(R.id.leftTeamText);
		FontUtils.setCustomfont(getApplicationContext(), leftTeamText,
				FontTypes.HELVETICANEUE);

		// Nombre Visitante
		rightTeamText = (TextView) findViewById(R.id.rightTeamText);
		FontUtils.setCustomfont(getApplicationContext(), rightTeamText,
				FontTypes.HELVETICANEUE);

		resultContainer = (RelativeLayout) findViewById(R.id.resultContainer);

		resultText = (TextView) findViewById(R.id.resultText);
		FontUtils.setCustomfont(getApplicationContext(), resultText,
				FontTypes.HELVETICANEUE);

		stateTeamText = (TextView) findViewById(R.id.stateTeamText);
		FontUtils.setCustomfont(getApplicationContext(), stateTeamText,
				FontTypes.HELVETICANEUE);

		stateTeamMinText = (TextView) findViewById(R.id.stateTeamMinText);
		FontUtils.setCustomfont(getApplicationContext(), stateTeamMinText,
				FontTypes.HELVETICANEUEBOLD);

	}


	/**
	 * 
	 */

	private void startTimer() {
		if (t == null || timeToUpdate != timeToUpdateOld) {

			if (t != null)
				t.cancel();

			t = new Timer();
			t.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					updateCarrusel();
				}
			}, 0, timeToUpdate);
			timeToUpdateOld = timeToUpdate;
		}
	}

	public void updateCarrusel() {
		String dataLink = getIntent().getExtras().getString("dataLink");
		dataLink = "http://as.com/apps/datos/directo/a9208f91ca1720d6c27572b7d825053a/regular/a/38/p_2772/marcador.js2";
		Log.d("CARRUSELUPDATE", "Actualizando CarruselActivityetail: "
				+ dataLink);
		if (dataLink != null && !dataLink.equalsIgnoreCase("")) {
			CarruselDAO.getInstance(getApplicationContext()).addDetailListener(
					this);
			CarruselDAO.getInstance(getApplicationContext())
					.getDetail(dataLink);
		} else {
			stopAnimation();
			showError(getString(R.string.carrusel_detail_error_no_info));

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.actionbar_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		setResult(RESULT_OK);
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return false;
		case R.id.share:
			sharePlayer();
			break;
		}
		return true;
	}

	private void sharePlayer() {

		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		String link = getIntent().getExtras().getString("link");

		String body = getString(R.string.mens_share_part3_1) + " <a href="
				+ link + ">" + match.getLocalTeamName() + " - "
				+ match.getAwayTeamName() + " </a>"
				+ getString(R.string.mens_share_part3_2)
		// + getString(R.string.share_mens_url_long)
		;

		intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
		startActivity(Intent.createChooser(intent,
				getString(R.string.share_mens_title)
						+ getString(R.string.app_name)));

	}

	public void goToTeam(String tag) {
		// Intent intent = new Intent(getApplicationContext(),
		// TeamActivity.class);
		// intent.putExtra("teamId", tag);
		// startActivityForResult((intent, ReturnRequestCodes.PUBLI_BACK);
		// overridePendingTransition(R.anim.grow_from_middle,
		// R.anim.shrink_to_middle);
	}

	private void loadData() {
		findViewById(R.id.gapBar).setVisibility(View.VISIBLE);
		findViewById(R.id.gapBar2).setVisibility(View.VISIBLE);
		View localContainer = findViewById(R.id.localContainer);
		localContainer.setTag(match.getLocalId());
		localContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goToTeam(String.valueOf(v.getTag()));

			}
		});
		View awayContainer = findViewById(R.id.awayContainer);
		awayContainer.setTag(match.getAwayId());
		awayContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goToTeam(String.valueOf(v.getTag()));

			}
		});
		localShield.setBackgroundResource(idShieldLocal);
		awayShield.setBackgroundResource(idShieldAway);

		leftTeamText.setText(match.getLocalTeamName());
		rightTeamText.setText(match.getAwayTeamName());

		stateCode = match.getStateCode();
		stateTeamText.setText(DatabaseDAO.getInstance(getApplicationContext())
				.getStatus(stateCode));

		String resultString;
		if (stateCode == 0) {
			SimpleDateFormat formatterHour = new SimpleDateFormat(
					DateFormat.HOUR_FORMAT, Locale.getDefault());
			SimpleDateFormat formatterDate = new SimpleDateFormat(
					DateFormat.DAY_FORMAT, Locale.getDefault());

			Time date = new Time(Long.valueOf(match.getDate()) * 1000);
			String dateString = formatterHour.format(date);
			// if (!dateString.equalsIgnoreCase("00:00"))
			dateString += " h";
			// else {
			// dateString = "  -  ";
			// }
			resultString = dateString;

			stateTeamText.setText(formatterDate.format(new Time(Long
					.valueOf(match.getDate()) * 1000)));

			resultContainer.setBackgroundColor(getResources().getColor(
					R.color.calendar_yellow));
		} else {
			int markerLocalTeam = match.getMarkerLocalTeam();
			if (markerLocalTeam < 0) {
				markerLocalTeam = 0;
			}
			int markerAwayTeam = match.getMarkerAwayTeam();
			if (markerAwayTeam < 0) {
				markerAwayTeam = 0;
			}
			resultString = String.valueOf(markerLocalTeam) + " - "
					+ String.valueOf(markerAwayTeam);
			stateTeamText.setText(DatabaseDAO.getInstance(
					getApplicationContext()).getStatus(stateCode));
			if (stateCode < 7) {
				resultContainer.setBackgroundColor(getResources().getColor(
						R.color.calendar_red));
				if (stateCode == 1 || stateCode > 2) {
					stateTeamMinText.setText(" " + match.getMinute() + "'");
				}
			} else {
				resultContainer.setBackgroundColor(getResources().getColor(
						R.color.calendar_gray));
			}
		}

		resultText.setText(resultString);
		// resultText.setText(getIntent().getStringExtra("resultString"));

		if (stateCode != stateCodeOld || stateCodeOld < 0) {
			details.clear();
			details.add(CarruselDetail.CARRUSEL_RESUMEN);
			details.addAll(match.getReferencias().keySet());
			Collections.sort(details,
					new CarruselComparator(match.getStateCode()));

			configureHeader();
			final int initPosition = configViewPager();
			if (initPosition != 0) {
				detailSroll.getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener() {

							@SuppressLint("NewApi")
							@Override
							public void onGlobalLayout() {
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
									detailSroll.getViewTreeObserver()
											.removeOnGlobalLayoutListener(this);
								} else {
									detailSroll.getViewTreeObserver()
											.removeGlobalOnLayoutListener(this);
								}
								detailSroll.setHeaderPosition(initPosition);
							}
						});
			}
		} else {
			updateViewPager();
		}

		if (match.getStateCode() > 0 && match.getStateCode() < 7) {
			timeToUpdate = RequestTimes.TIMER_CARRUSEL_UPDATE_ACTIVE;
		} else {
			timeToUpdate = RequestTimes.TIMER_CARRUSEL_UPDATE_NO_ACTIVE;
		}
		startTimer();
	}

	private void configureHeader() {

		// ArrayList<String> strings = new ArrayList<String>();
		// for (int i = 0; i < details.size(); i++) {
		// strings.add(details.get(i));
		// }
		detailSroll = (CustomHoizontalScroll) findViewById(R.id.detailSroll);
		detailSroll.setScreenWidth(width);
		detailSroll.setFont(FontTypes.ROBOTO_LIGHT);
		detailSroll.setMainColor(getResources()
				.getColor(R.color.red_comparator));
		detailSroll
				.setSecondColor(getResources().getColor(R.color.medium_gray));
		detailSroll.addScrollEndListener(this);
		detailSroll.addViews(details);

		buttonPrev = (ImageView) findViewById(R.id.buttonPrev);
		buttonNext = (ImageView) findViewById(R.id.buttonNext);

		final RelativeLayout rel1 = (RelativeLayout) findViewById(R.id.contentCompetition);
		rel1.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							rel1.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						} else {
							rel1.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						}

						RelativeLayout.LayoutParams paramsLeft = new RelativeLayout.LayoutParams(
								widthButton, rel1.getHeight());
						paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
						findViewById(R.id.buttonLeft).setLayoutParams(
								paramsLeft);

						RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(
								widthButton, rel1.getHeight());
						paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						findViewById(R.id.buttonRight).setLayoutParams(
								paramsRight);
					}
				});

	}

	private void setButtonsVisibility() {
		if (viewPager.getCurrentItem() > 0) {
			buttonPrev.setVisibility(View.VISIBLE);
		} else {
			buttonPrev.setVisibility(View.INVISIBLE);
		}

		if (viewPager.getCurrentItem() < details.size() - 1) {
			buttonNext.setVisibility(View.VISIBLE);
		} else {
			buttonNext.setVisibility(View.INVISIBLE);
		}

	}

	private int configViewPager() {
		stateCodeOld = stateCode;
		viewPager = (CustomViewPagerLeague) findViewById(R.id.carruselViewPager);

		fragments = getFragments();

		viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),
				fragments));
		int initPosition = 0;
		// if (match.getStateCode() > 0 && match.getStateCode() < 8) {
		if (match.getStateCode() > 0 && match.getStateCode() < 7) {
			for (int i = 0; i < fragments.size(); i++) {
				if (fragments.get(i) instanceof CarruselDirectoFragment) {
					initPosition = i;
					break;
				}
			}
			this.detailSroll.setInitPosition(initPosition);
			viewPager.setCurrentItem(initPosition, true);
		} else {
			viewPager.setCurrentItem(initPosition, true);
		}

		callToOmniture(initPosition);
		viewPager.setOnPageChangeListener(this);

		setButtonsVisibility();
		return initPosition;
	}

	private void callToOmniture(int pos) {
		StatisticsDAO.getInstance(this).sendStatisticsState(
				getApplication(),
				Omniture.SECTION_CARROUSEL,
				getIntent().getExtras().getString("dayName"),
				match.getLocalTeamName() + match.getAwayTeamName(),
				details.get(pos),
				Omniture.TYPE_ARTICLE,
				Omniture.SECTION_CARROUSEL + " "
						+ getIntent().getExtras().getString("dayName") + " "
						+ match.getLocalTeamName() + match.getAwayTeamName()
						+ " " + details.get(pos), null);
	}

	private void updateViewPager() {
		for (Fragment fragment : fragments) {
			if (fragment instanceof CarruselResumenFragment) {
				Bundle args = fragment.getArguments();
				args.putParcelable("match", match);
				args.putString("jornadaName",
						getIntent().getStringExtra("dayName"));
			}
			try {
				((CarruselFragment) fragment).updateData();
			} catch (Exception e) {
				Log.e("UPDATE", "Fallo al actualizar: "+e.getMessage());
//				e.printStackTrace();
			}
		}
	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();
		Fragment fragment = null;
		for (int i = 0; i < details.size(); i++) {
			Bundle args = new Bundle();
			if (details.get(i)
					.equalsIgnoreCase(CarruselDetail.CARRUSEL_RESUMEN)) {
				args.putParcelable("match", match);
				args.putString("jornadaName",getIntent().getStringExtra("dayName"));
				fragment = new CarruselResumenFragment();
			} else if (details.get(i).equalsIgnoreCase(
					CarruselDetail.CARRUSEL_PICAS)) {
				fragment = new CarruselSpadesFragment();
				args.putString(
						"url",
						match.getReferencias().get(
								CarruselDetail.CARRUSEL_PICAS));
			} else if (details.get(i).equalsIgnoreCase(
					CarruselDetail.CARRUSEL_DIRECTO)) {
				fragment = new CarruselDirectoFragment();
				args.putString(
						"url",
						match.getReferencias().get(
								CarruselDetail.CARRUSEL_DIRECTO));

			} else if (details.get(i).equalsIgnoreCase(
					CarruselDetail.CARRUSEL_ESTADISTICAS)) {
				fragment = new CarruselStatsFragment();
				args.putString(
						"url",
						match.getReferencias().get(
								CarruselDetail.CARRUSEL_ESTADISTICAS));
			} else if (details.get(i).equalsIgnoreCase(
					CarruselDetail.CARRUSEL_PLANTILLA)) {
				fragment = new CarruselGameSystemFragment();
				args.putString(
						"url",
						match.getReferencias().get(
								CarruselDetail.CARRUSEL_PLANTILLA));
				// } else {
				// fragment = new CarruselDetailFragment();
			}

			args.putInt("idShieldLocal", idShieldLocal);
			args.putInt("idShieldAway", idShieldAway);
			
			if (fragment != null) {
				fragment.setArguments(args);
				fList.add(fragment);
			}
		}
		return fList;
	}

	public void buttonClick(View view) {
		int tag = Integer.valueOf((String) view.getTag());
		switch (tag) {
		case PREV_BUTTON:
			if (viewPager.getCurrentItem() > 0)
				viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
			break;
		case POST_BUTTON:
			if (viewPager.getCurrentItem() < details.size())
				viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
			break;

		default:
			break;
		}
	}

	/**************************************************************************************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.ViewPager.OnPageChangeListener#
	 * onPageScrollStateChanged(int)
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled
	 * (int, float, int)
	 */
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected
	 * (int)
	 */
	@Override
	public void onPageSelected(int pos) {
		detailSroll.setHeaderPosition(pos);
		setButtonsVisibility();
		// hideFooter(pos);

		callToOmniture(pos);
	}

	/****************************** CUSTOMHOIZONTALSCROLL LISTENER ********************************************************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diarioas.guiamundial.utils.scroll.CustomHoizontalScroll.ScrollEndListener
	 * #onScrollEnd(int, int, int, int, int)
	 */
	@Override
	public void onScrollEnd(int x, int y, int oldx, int oldy, int pos) {
		this.viewPager.setCurrentItem(pos, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diarioas.guiamundial.utils.scroll.CustomHoizontalScroll.ScrollEndListener
	 * #onItemClicked(int)
	 */
	@Override
	public void onItemClicked(int position) {
		this.viewPager.setCurrentItem(position, true);

	}

	/****************************** CARRUSELDAODETAIL LISTENER ********************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CarruselDAO.CarruselDAODetailListener#
	 * onSuccessDetailCarrusel(es.prisacom.as.dao.model.calendar.Fase)
	 */
	@Override
	public void onSuccessDetailCarrusel(Match match) {
		CarruselDAO.getInstance(getApplicationContext()).removeDetailListener(
				this);

		this.match = match;
		stopAnimation();
		loadData();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CarruselDAO.CarruselDAODetailListener#
	 * onFailureDetailCarrusel()
	 */
	@Override
	public void onFailureDetailCarrusel() {
		CarruselDAO.getInstance(getApplicationContext()).removeDetailListener(
				this);
		stopAnimation();
		showError(getString(R.string.carrusel_detail_error));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CarruselDAO.CarruselDAODetailListener#
	 * onFailureDetailNotConnection()
	 */
	@Override
	public void onFailureDetailNotConnection() {
		CarruselDAO.getInstance(getApplicationContext()).removeDetailListener(
				this);
		stopAnimation();
		showError(getString(R.string.carrusel_detail_error));
	}

	/**
	 * @param errorMessage
	 * 
	 */
	private void showError(String errorMessage) {
		((TextView) findViewById(R.id.errorMessage)).setText(errorMessage);
		findViewById(R.id.errorContent).setVisibility(View.VISIBLE);

	}
	/**************************************************************************************/

}
