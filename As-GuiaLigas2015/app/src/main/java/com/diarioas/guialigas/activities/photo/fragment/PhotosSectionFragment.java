package com.diarioas.guialigas.activities.photo.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.fragment.SectionFragment;
import com.diarioas.guialigas.activities.home.HomeActivity;
import com.diarioas.guialigas.activities.photo.PhotoGalleryActivity;
import com.diarioas.guialigas.dao.model.news.GalleryMediaItem;
import com.diarioas.guialigas.dao.model.news.PhotoMediaItem;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.dao.reader.RemoteGalleryDAO;
import com.diarioas.guialigas.dao.reader.RemoteGalleryDAO.RemotePhotosDAOListener;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.AlertManager;
import com.diarioas.guialigas.utils.Defines.DateFormat;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.FileUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PhotosSectionFragment extends SectionFragment implements
		RemotePhotosDAOListener {

	private ScrollView contentGallery;

	private ArrayList<GalleryMediaItem> galleries;
	private SimpleDateFormat dateFormatPull;

	private SwipeRefreshLayout swipeRefreshLayout = null;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.adSection = NativeAds.AD_PHOTOS + "/" + NativeAds.AD_PORTADA;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_photo_section,
				container, false);
		return generalView;
	}

	@Override
	public void onStop() {
		super.onStop();
		RemoteGalleryDAO.getInstance(mContext).removeListener(this);
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/
	@Override
	protected void configureView() {
		dateFormatPull = new SimpleDateFormat(DateFormat.PULL_FORMAT,
				Locale.getDefault());

		galleries = new ArrayList<GalleryMediaItem>();
		callToOmniture();
	}

	@Override
	public void loadInformation() {
		reUpdateGallery();
		configureSwipeLoader();
		configureListView();

		if (RemoteGalleryDAO.getInstance(mContext).isGalleryPreLoaded()) {
			galleries = RemoteGalleryDAO.getInstance(mContext)
					.getGalleryPreloaded();
			loadData();
		}

	}

	private void configureSwipeLoader() {
		this.swipeRefreshLayout = (SwipeRefreshLayout) generalView
				.findViewById(R.id.swipe_contentGallery);
		this.swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				reUpdateGallery();
			}
		});
		this.swipeRefreshLayout.setColorSchemeResources(R.color.black,
				R.color.red, R.color.white);
	}

	private void configureListView() {
		contentGallery = (ScrollView) generalView
				.findViewById(R.id.contentGallery);
		contentGallery.setClickable(false);
		contentGallery.setHorizontalScrollBarEnabled(false);
	}

	private LinearLayout getContainer() {

		Point size = DimenUtils.getSize(getActivity().getWindowManager());
		int width = (size.x - 2 * DimenUtils.getRegularPixelFromDp(mContext,
				R.dimen.padding_photo)) / 2;

		int height;
		int heightLeft = 0;
		int heightRight = 0;
		LinearLayout scrollLinearContainer = new LinearLayout(this.mContext);
		LinearLayout.LayoutParams scrollLinearContainerParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		scrollLinearContainer.setLayoutParams(scrollLinearContainerParams);
		scrollLinearContainer.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout leftColumn = getColumn();
		LinearLayout rightColumn = getColumn();

		RelativeLayout imageRelativeLayout;
		PhotoMediaItem photoFront;
		Animation anim;
		for (int i = 0; i < galleries.size(); i++) {
			if (galleries.get(i).getCoverPhoto() != null) {
				photoFront = galleries.get(i).getCoverPhoto();
				height = getHeightFromWidth(width, photoFront.getWidth(),
						photoFront.getHeight());
				imageRelativeLayout = getPhotoElement(galleries.get(i), width,
						height);
				anim = getAnimation(i, imageRelativeLayout);

				imageRelativeLayout.startAnimation(anim);
				imageRelativeLayout.setTag(i);
				imageRelativeLayout
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								openPictureDetail((Integer) v.getTag());
							}
						});
				if (heightLeft <= heightRight) {
					leftColumn.addView(imageRelativeLayout);
					heightLeft += height;
				} else {
					rightColumn.addView(imageRelativeLayout);
					heightRight += height;
				}

				// Log.d("PHOTO", "heightLeft: " + heightLeft + "heightRight: "
				// + heightRight);
			}
		}

		scrollLinearContainer.addView(leftColumn);
		scrollLinearContainer.addView(rightColumn);
		return scrollLinearContainer;
	}

	private Animation getAnimation(int i, final View view) {
		int durationMillis = 200;
		if (i <= 10) {
			durationMillis += 100 * i;
		} else {
			durationMillis += 1000;
		}
		// TranslateAnimation traslacion = new TranslateAnimation(0, 0, -800,
		// 0);
		Animation traslacion = AnimationUtils.loadAnimation(mContext,
				R.anim.slide_in_down_photo);

		traslacion.setDuration(durationMillis);
		traslacion.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				view.setVisibility(View.VISIBLE);

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}

		});

		return traslacion;
	}

	protected void openPictureDetail(Integer position) {

		Intent intent = new Intent(getActivity(), PhotoGalleryActivity.class);
		intent.putExtra("gallery", position);
		intent.putExtra("galleryTitle", galleries.get(position).getTitle());

		getActivity().startActivityForResult(intent,
				ReturnRequestCodes.PUBLI_BACK);
		getActivity().overridePendingTransition(R.anim.slide_in_left,
				R.anim.null_anim);

	}

	private LinearLayout getColumn() {
		LinearLayout leftColumn = new LinearLayout(this.mContext);
		LinearLayout.LayoutParams leftColumnParams = new LinearLayout.LayoutParams(
				0, LayoutParams.WRAP_CONTENT, 1);
		leftColumn.setLayoutParams(leftColumnParams);
		leftColumn.setOrientation(LinearLayout.VERTICAL);
		return leftColumn;
	}

	private RelativeLayout getPhotoElement(GalleryMediaItem galleryMediaItem,
			int width, int height) {

		RelativeLayout imageRelativeLayout = (RelativeLayout) inflater.inflate(
				R.layout.item_list_photo_small, null);
		imageRelativeLayout.setVisibility(View.INVISIBLE);

		android.widget.LinearLayout.LayoutParams relativeLayoutParams = new LinearLayout.LayoutParams(
				width, height);
		imageRelativeLayout.setLayoutParams(relativeLayoutParams);

		ImageDAO.getInstance(mContext).loadRegularImage(
				galleryMediaItem.getCoverPhoto().getUrl(),
				(ImageView) imageRelativeLayout.findViewById(R.id.image));

		TextView title = (TextView) imageRelativeLayout
				.findViewById(R.id.title);
		FontUtils.setCustomfont(mContext, title, FontTypes.ROBOTO_REGULAR);
		title.setText(galleryMediaItem.getTitle());

		// TextView numPhotos = (TextView) imageRelativeLayout
		// .findViewById(R.id.numPhotos);
		// FontUtils.setCustomfont(mContext, numPhotos,
		// FontTypes.HELVETICANEUE);
		// numPhotos.setText(String.valueOf(galleryMediaItem.getNumElements()));
		return imageRelativeLayout;
	}

	private int getHeightFromWidth(int width, int originalWidth,
			int originalHeight) {
		int height = (width * originalHeight) / originalWidth;
		// Log.d("PHOTO", "OriginalPhoto Width: " + originalWidth
		// + " OriginalPhoto Height: " + originalHeight + " Photo Width: "
		// + width + " Photo Height: " + height);
		return height;
	}

	private void loadData() {
		contentGallery.removeAllViews();
		contentGallery.addView(getContainer());
	}

	protected void reUpdateGallery() {
		RemoteGalleryDAO.getInstance(mContext).addListener(this);
		RemoteGalleryDAO.getInstance(mContext).getGalleries(section.getUrl());
	}

	private void stopAnimation() {
		((HomeActivity) getActivity()).stopAnimation();
		if (swipeRefreshLayout != null) {
			swipeRefreshLayout.setRefreshing(false);
		}

	}

	/***************************************************************************/
	/** Libraries methods **/
	/***************************************************************************/
	@Override
	protected void callToOmniture() {
		StatisticsDAO.getInstance(mContext).sendStatisticsState(
				getActivity().getApplication(),
                FileUtils.readOmnitureProperties(mContext, "SECTION_PHOTOS"),
				null,
				null,
				null,
                FileUtils.readOmnitureProperties(mContext, "TYPE_ARTICLE"),
                FileUtils.readOmnitureProperties(mContext, "DETAILPAGE_PORTADA") + " " + FileUtils.readOmnitureProperties(mContext, "SECTION_PHOTOS"),
				null);
	}

	/***************************************************************************/
	/** RemotePhotosDAOListener methods **/
	/***************************************************************************/
	@Override
	public void onSuccessRemoteconfig(ArrayList<GalleryMediaItem> galleries) {
		RemoteGalleryDAO.getInstance(mContext).removeListener(this);
		this.galleries = galleries;
		loadData();
		stopAnimation();
	}

	@Override
	public void onFailureRemoteconfig() {
		RemoteGalleryDAO.getInstance(mContext).removeListener(this);
		AlertManager.showAlertOkDialog(getActivity(),
				getResources().getString(R.string.section_not_conection),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						contentGallery.removeAllViews();
						contentGallery.addView(getErrorContainer());
						// palmaresArray = DatabaseDAO.getInstance(
						// getApplicationContext()).getStadiums();
						// if (palmaresArray != null && palmaresArray.size() >
						// 0) {
						// loadData();
						// } else {
						// getActivity().onBackPressed();
						// }
					}

				});

		stopAnimation();
	}

	@Override
	public void onFailureNotConnection() {
		RemoteGalleryDAO.getInstance(mContext).removeListener(this);
		AlertManager.showAlertOkDialog(getActivity(),
				getResources().getString(R.string.section_not_conection),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						contentGallery.removeAllViews();
						contentGallery.addView(getErrorContainer());
						// palmaresArray = DatabaseDAO.getInstance(
						// getApplicationContext()).getStadiums();
						// if (palmaresArray != null && palmaresArray.size() >
						// 0) {
						// loadData();
						// } else {
						// getActivity().onBackPressed();
						// }
					}

				});

		stopAnimation();

	}

}
