package com.diarioas.guialigas.activities.photo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.photo.PhotoGalleryActivity;
import com.diarioas.guialigas.dao.model.news.PhotoMediaItem;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.utils.bitmapfun.ImageFetcher;
import com.diarioas.guialigas.utils.imageutils.TouchImageView;

public class PhotoSectionGalleryFragment extends Fragment implements
		OnTouchListener {

	private View generalView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		generalView = inflater.inflate(R.layout.fragment_photo_fs, container,
				false);

		configureView();

		return generalView;
	}

	private void configureView() {
		PhotoMediaItem photoItem = (PhotoMediaItem) getArguments()
				.getSerializable("photoItem");

		TouchImageView touchImageView = (TouchImageView) generalView
				.findViewById(R.id.image);
		touchImageView.setCustomTouchListener(this);

		ImageDAO.getInstance(getActivity()).loadFullScreenImage(
				photoItem.getUrl(), touchImageView);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		((PhotoGalleryActivity) getActivity()).toogleBarsVisibility();
		return false;
	}

}
