package com.diarioas.guialigas.activities.stadiums.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.stadiums.StadiumsPhotoGalleryActivity;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;

public class PhotoGalleryFragment extends Fragment {

	private View generalView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		generalView = inflater.inflate(R.layout.fragment_sede_photo, container,
				false);

		configureView();

		return generalView;
	}

	private void configureView() {
		String url = getArguments().getString("url");
		// Log.d("PhotoGalleryFragment", "Cargando imagen: " + url);
		ImageDAO.getInstance(getActivity()).loadStadiumDetailImage(url,(ImageView) generalView.findViewById(R.id.image));
		generalView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goToPhotoViewPager();
			}
		});

	}

	protected void goToPhotoViewPager() {
		Intent intent = new Intent(getActivity(),
				StadiumsPhotoGalleryActivity.class);
		intent.putExtra("idStadium", getArguments().getInt("idStadium"));
		intent.putExtra("name", getArguments().getString("name"));
		intent.putExtra("pos", getArguments().getInt("pos"));
		intent.putExtra("type", getArguments().getString("type"));
		intent.putExtra("fragmentPos", getArguments().getInt("fragmentPos"));
		getActivity().startActivityForResult(intent,
				ReturnRequestCodes.GALLERY_BACK);
		getActivity().overridePendingTransition(R.anim.slide_in_left,
				R.anim.null_anim);

	}
}
