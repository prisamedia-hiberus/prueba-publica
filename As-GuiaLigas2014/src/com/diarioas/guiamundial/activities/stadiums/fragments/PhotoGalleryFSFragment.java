package com.diarioas.guiamundial.activities.stadiums.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.activities.stadiums.StadiumsPhotoGalleryActivity;
import com.diarioas.guiamundial.utils.Defines.STADIUM_IMAGE_TYPE;
import com.diarioas.guiamundial.utils.bitmapfun.ImageFetcher;

public class PhotoGalleryFSFragment extends Fragment {

	private View generalView;
	private ImageFetcher mImageFetcher;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (STADIUM_IMAGE_TYPE.TYPE_STADIUM
				.equalsIgnoreCase((String) getArguments().get("type")))
			generalView = inflater.inflate(R.layout.fragment_sede_photo_fs,
					container, false);
		else
			generalView = inflater.inflate(R.layout.fragment_city_photo_fs,
					container, false);
		mImageFetcher = ((StadiumsPhotoGalleryActivity) getActivity())
				.getmImageFetcher();

		configureView();

		return generalView;
	}

	private void configureView() {
		String url = getArguments().getString("url");
		Log.d("PhotoGalleryFragment", "Cargando imagen: " + url);
		mImageFetcher.loadImage(url,
				(ImageView) generalView.findViewById(R.id.image));

		generalView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});
	}

}
