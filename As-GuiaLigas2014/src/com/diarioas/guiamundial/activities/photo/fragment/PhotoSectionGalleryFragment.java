package com.diarioas.guiamundial.activities.photo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.activities.photo.PhotoGalleryActivity;
import com.diarioas.guiamundial.dao.model.news.PhotoMediaItem;
import com.diarioas.guiamundial.utils.bitmapfun.ImageFetcher;
import com.diarioas.guiamundial.utils.imageutils.TouchImageView;

public class PhotoSectionGalleryFragment extends Fragment implements OnTouchListener {

	private View generalView;
	private ImageFetcher mImageFetcher;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		generalView = inflater.inflate(R.layout.fragment_photo_fs, container,
				false);

		mImageFetcher = ((PhotoGalleryActivity) getActivity())
				.getmImageFetcher();

		configureView();

		return generalView;
	}

	private void configureView() {
		PhotoMediaItem photoItem = (PhotoMediaItem) getArguments()
				.getSerializable("photoItem");

		TouchImageView touchImageView = (TouchImageView) generalView.findViewById(R.id.image);
		touchImageView.setCustomTouchListener(this);
		mImageFetcher.loadImage(photoItem.getUrl(),touchImageView);

//		generalView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				((PhotoGalleryActivity)getActivity()).toogleBarsVisibility();
//			}
//		});
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		((PhotoGalleryActivity)getActivity()).toogleBarsVisibility();
		return false;
	}

}
