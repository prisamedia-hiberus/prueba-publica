package com.diarioas.guialigas.activities.stadiums.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.utils.Defines.STADIUM_IMAGE_TYPE;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;

public class CityFragment extends SedeFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		generalView = inflater.inflate(R.layout.fragment_sede_city, container,
				false);

		configureView();
		configureGallery(R.id.cityGalleryViewPager);

		return generalView;
	}

	@Override
	protected void configureView() {

		Bundle arguments = getArguments();

		if (arguments.containsKey("history")) {
			TextView history = (TextView) generalView
					.findViewById(R.id.history);
			FontUtils.setCustomfont(getActivity().getApplicationContext(),
					history, FontTypes.ROBOTO_REGULAR);
			history.setText(arguments.getString("history"));
		}

		if (arguments.containsKey("transport")) {
			TextView transporte = (TextView) generalView
					.findViewById(R.id.transporte);
			FontUtils.setCustomfont(getActivity().getApplicationContext(),
					transporte, FontTypes.ROBOTO_REGULAR);
			transporte.setText(arguments.getString("transport"));
			FontUtils.setCustomfont(getActivity().getApplicationContext(),
					generalView.findViewById(R.id.transporteLabel),
					FontTypes.ROBOTO_BOLD);
		} else {
			generalView.findViewById(R.id.transporteLabel).setVisibility(
					View.GONE);
		}

		if (arguments.containsKey("economy")) {
			TextView economia = (TextView) generalView
					.findViewById(R.id.economia);
			FontUtils.setCustomfont(getActivity().getApplicationContext(),
					economia, FontTypes.ROBOTO_REGULAR);
			economia.setText(arguments.getString("economy"));
			FontUtils.setCustomfont(getActivity().getApplicationContext(),
					generalView.findViewById(R.id.economiaLabel),
					FontTypes.ROBOTO_BOLD);
		} else {
			generalView.findViewById(R.id.economiaLabel).setVisibility(
					View.GONE);
		}

		if (arguments.containsKey("tourism")) {
			TextView tourism = (TextView) generalView
					.findViewById(R.id.turismo);
			FontUtils.setCustomfont(getActivity().getApplicationContext(),
					tourism, FontTypes.ROBOTO_REGULAR);
			tourism.setText(arguments.getString("tourism"));
			FontUtils.setCustomfont(getActivity().getApplicationContext(),
					generalView.findViewById(R.id.turismoLabel),
					FontTypes.ROBOTO_BOLD);
		} else {
			generalView.findViewById(R.id.turismoLabel)
					.setVisibility(View.GONE);
		}

		if (arguments.containsKey("state")
				|| arguments.containsKey("population")
				|| arguments.containsKey("altitude")) {
			if (arguments.containsKey("state")) {
				TextView state = (TextView) generalView
						.findViewById(R.id.state);
				FontUtils.setCustomfont(getActivity().getApplicationContext(),
						state, FontTypes.ROBOTO_REGULAR);
				state.setText(arguments.getString("state"));
				FontUtils.setCustomfont(getActivity().getApplicationContext(),
						generalView.findViewById(R.id.stateLabel),
						FontTypes.ROBOTO_REGULAR);
			} else {
				generalView.findViewById(R.id.stateContent).setVisibility(
						View.GONE);
			}

			if (arguments.containsKey("population")) {
				TextView population = (TextView) generalView
						.findViewById(R.id.population);
				FontUtils.setCustomfont(getActivity().getApplicationContext(),
						population, FontTypes.ROBOTO_REGULAR);
				population.setText(arguments.getString("population"));
				FontUtils.setCustomfont(getActivity().getApplicationContext(),
						generalView.findViewById(R.id.populationLabel),
						FontTypes.ROBOTO_REGULAR);
			} else {
				generalView.findViewById(R.id.populationContent).setVisibility(
						View.GONE);
			}

			if (arguments.containsKey("altitude")) {
				TextView altitude = (TextView) generalView
						.findViewById(R.id.altitude);
				FontUtils.setCustomfont(getActivity().getApplicationContext(),
						altitude, FontTypes.ROBOTO_REGULAR);
				altitude.setText(arguments.getString("altitude"));
				FontUtils.setCustomfont(getActivity().getApplicationContext(),
						generalView.findViewById(R.id.altitudeLabel),
						FontTypes.ROBOTO_REGULAR);
			} else {
				generalView.findViewById(R.id.altitudeContent).setVisibility(
						View.GONE);
			}
		} else {
			generalView.findViewById(R.id.dataContent).setVisibility(View.GONE);
		}

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
			args.putInt("fragmentPos", 1);
			args.putInt("pos", i);
			args.putString("type", STADIUM_IMAGE_TYPE.TYPE_CITY);
			args.putInt("idStadium", getArguments().getInt("idStadium"));
			args.putString("url", photos.get(i));

			fragment.setArguments(args);
			fList.add(fragment);

			circleIndicator.addView(getCircle(i));
		}
		return fList;
	}

}
