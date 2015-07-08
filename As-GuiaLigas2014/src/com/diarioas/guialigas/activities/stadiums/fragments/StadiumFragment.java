package com.diarioas.guialigas.activities.stadiums.fragments;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.utils.Defines.STADIUM_IMAGE_TYPE;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;

public class StadiumFragment extends SedeFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		generalView = inflater.inflate(R.layout.fragment_sede_stadium,
				container, false);

		configureView();
		configureGallery(R.id.stadiumGalleryViewPager);

		return generalView;
	}

	@Override
	protected void configureView() {

		Bundle arguments = getArguments();
		if (arguments.containsKey("photoMap")) {
			ImageView imageLocation = (ImageView) generalView
					.findViewById(R.id.imageLocation);
			imageLocation.setBackgroundResource(arguments.getInt("photoMap"));
		}

		if (arguments.containsKey("history")) {
			TextView history = (TextView) generalView
					.findViewById(R.id.history);
			FontUtils.setCustomfont(getActivity().getApplicationContext(),
					history, FontTypes.ROBOTO_REGULAR);
			history.setText(arguments.getString("history"));
		}

		if (arguments.containsKey("capacity")) {
			TextView aforo = (TextView) generalView.findViewById(R.id.aforo);
			FontUtils.setCustomfont(getActivity().getApplicationContext(),
					aforo, FontTypes.ROBOTO_REGULAR);

			String numPeople = NumberFormat.getNumberInstance(
					Locale.getDefault()).format(arguments.getInt("capacity"))
					+ " espectadores";
			aforo.setText(numPeople);
		}

		FontUtils.setCustomfont(getActivity().getApplicationContext(),
				generalView.findViewById(R.id.aforoLabel),
				FontTypes.ROBOTO_REGULAR);

		if (arguments.containsKey("year")) {
			TextView year = (TextView) generalView.findViewById(R.id.year);
			FontUtils.setCustomfont(getActivity().getApplicationContext(),
					year, FontTypes.ROBOTO_REGULAR);
			year.setText(String.valueOf(arguments.getInt("year")));
		}
		FontUtils.setCustomfont(getActivity().getApplicationContext(),
				generalView.findViewById(R.id.yearLabel),
				FontTypes.ROBOTO_REGULAR);

	}

	@Override
	protected List<Fragment> getFragments(ArrayList<String> photos) {
		List<Fragment> fList = new ArrayList<Fragment>();

		Fragment fragment;
		Bundle args;
		for (int i = 0; i < photos.size(); i++) {
			fragment = new PhotoGalleryFragment();
			args = new Bundle();
			args.putString("name", getArguments().getString("name"));
			args.putInt("fragmentPos", 0);
			args.putInt("pos", i);
			args.putString("type", STADIUM_IMAGE_TYPE.TYPE_STADIUM);
			args.putInt("idStadium", getArguments().getInt("idStadium"));
			args.putString("url", photos.get(i));

			fragment.setArguments(args);
			fList.add(fragment);

			circleIndicator.addView(getCircle(i));
		}
		return fList;
	}

}
