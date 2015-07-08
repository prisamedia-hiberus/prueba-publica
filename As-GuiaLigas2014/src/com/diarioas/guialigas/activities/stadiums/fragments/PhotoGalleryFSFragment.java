package com.diarioas.guialigas.activities.stadiums.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.utils.Defines.STADIUM_IMAGE_TYPE;

public class PhotoGalleryFSFragment extends Fragment {

	private View generalView;

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

		configureView();

		return generalView;
	}

	private void configureView() {
		String url = getArguments().getString("url");
		ImageDAO.getInstance(getActivity()).loadRegularImage(url,
				(ImageView) generalView.findViewById(R.id.image));

		generalView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});
	}
}
