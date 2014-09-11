package com.diarioas.guialigas.activities.stadiums.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.utils.Defines.STADIUM_IMAGE_TYPE;
import com.diarioas.guialigas.utils.imageutils.TouchImageView;
import com.diarioas.guialigas.utils.imageutils.imageloader.MemoryReleaseUtils;

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
	
	@Override
	public void onDestroy() {
		super.onDestroy();

		if (generalView != null) {
			
		    TouchImageView image = (TouchImageView)generalView.findViewById(R.id.image);
		    if((image!=null)&&(image.getBackground()!=null))
		    {
		    	image.setImageDrawable(null);
		    	image.getBackground().setCallback(null);
		    }			
			MemoryReleaseUtils.unbindDrawables(generalView);
		}
	}

	private void configureView() {
		String url = getArguments().getString("url");
		Log.d("PhotoGalleryFragment", "Cargando imagen: " + url);
		ImageDAO.getInstance(getActivity()).loadStadiumImage(url,(ImageView) generalView.findViewById(R.id.image));

		generalView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});
	}

}
