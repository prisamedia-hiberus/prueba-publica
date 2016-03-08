package com.diarioas.guialigas.activities.team.fragment;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.player.PlayerActivity;
import com.diarioas.guialigas.activities.videos.VideoActivity;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.utils.Defines;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.DrawableUtils;
import com.diarioas.guialigas.utils.FontUtils;


public class DetailTeamFragment extends TeamFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;

		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_team_detail_single_competition,
				container, false);

		return generalView;
	}

	@Override
	protected void configureView() {

		configureData();
		configureShirts();

		FontUtils.setCustomfont(mContext,
				generalView.findViewById(R.id.selectionLabel),
				FontUtils.FontTypes.ROBOTO_BOLD);
		TextView selectionText = (TextView) generalView
				.findViewById(R.id.selectionText);
		FontUtils.setCustomfont(mContext, selectionText,
				FontUtils.FontTypes.ROBOTO_REGULAR);
		selectionText.setText((String) getArguments().get("body"));

		configureVideo();
		configureMister();
		if (getArguments().containsKey("star") && getArguments().getBoolean("star")) {
			configureStar();
			generalView.findViewById(R.id.starTablet).setVisibility(View.VISIBLE);
		}else {
			generalView.findViewById(R.id.starTablet).setVisibility(View.GONE);
		}

	}

	@Override
	protected void startFirstShow() {

	}

	private void configureData() {

		int resid = 0;
		if (getArguments().containsKey("urlShield")
				&& getArguments().getString("urlShield") != null) {
            resid = DrawableUtils.getDrawableShieldId(mContext, getArguments()
                    .getString("urlShield"), 4);
            Log.d("urlShield: ", "" + getArguments().getString("urlShield"));

        }

        // TODO cambiar el tag de escudo por bandera
		if (resid != 0)
			generalView.findViewById(R.id.shieldImage).setBackgroundResource(
					resid);

		String numJug = (String) getArguments().get("numJug");
		String numClub = (String) getArguments().get("numClub");
		String numArb = (String) getArguments().get("numArb");

		if ((numJug != null && numJug.length() > 0)
				|| (numClub != null && numClub.length() > 0)
				|| (numArb != null && numArb.length() > 0)) {
			TextView numJugText = (TextView) generalView
					.findViewById(R.id.numJugText);

			FontUtils.setCustomfont(mContext, numJugText,
					FontUtils.FontTypes.ROBOTO_REGULAR);
			FontUtils.setCustomfont(mContext,
					generalView.findViewById(R.id.numJugLabel),
					FontUtils.FontTypes.ROBOTO_REGULAR);
			numJugText.setText(numJug);

			TextView numClubText = (TextView) generalView
					.findViewById(R.id.numClubText);

			FontUtils.setCustomfont(mContext, numClubText,
					FontUtils.FontTypes.ROBOTO_REGULAR);
			FontUtils.setCustomfont(mContext,
					generalView.findViewById(R.id.numClubLabel),
					FontUtils.FontTypes.ROBOTO_REGULAR);
			numClubText.setText(numClub);

			TextView numArbText = (TextView) generalView
					.findViewById(R.id.numArbText);

			FontUtils.setCustomfont(mContext, numArbText,
					FontUtils.FontTypes.ROBOTO_REGULAR);
			FontUtils.setCustomfont(mContext,
					generalView.findViewById(R.id.numArbLabel),
					FontUtils.FontTypes.ROBOTO_REGULAR);
			numArbText.setText(numArb);
		} else {
			generalView.findViewById(R.id.dataNumContainer).setVisibility(
					View.GONE);
		}

		TextView teamNameText = (TextView) generalView
				.findViewById(R.id.teamNameText);
		String teamName = (String) getArguments().get("teamName");
		FontUtils.setCustomfont(mContext, teamNameText,
				FontUtils.FontTypes.ROBOTO_BOLD);
		teamNameText.setText(teamName);

		TextView teamFederationNameText = (TextView) generalView
				.findViewById(R.id.teamFederationNameText);
		String teamFederationName = (String) getArguments().get(
				"teamFederationName");
		FontUtils.setCustomfont(mContext, teamFederationNameText,
				FontUtils.FontTypes.ROBOTO_REGULAR);
		FontUtils.setCustomfont(mContext,
				generalView.findViewById(R.id.teamFederationNameLabel),
				FontUtils.FontTypes.ROBOTO_BOLD);
		teamFederationNameText.setText(teamFederationName);

		TextView teamFederationFoundationText = (TextView) generalView
				.findViewById(R.id.teamFederationFoundationText);
		String teamFederationFoundation = (String) getArguments().get(
				"teamFederationFoundation");
		FontUtils.setCustomfont(mContext, teamFederationFoundationText,
				FontUtils.FontTypes.ROBOTO_REGULAR);
		FontUtils.setCustomfont(mContext,
				generalView.findViewById(R.id.teamFederationFoundationLabel),
				FontUtils.FontTypes.ROBOTO_BOLD);
		teamFederationFoundationText.setText(teamFederationFoundation);

		TextView teamFederationAffiliationText = (TextView) generalView
				.findViewById(R.id.teamFederationAffiliationText);
		String teamFederationAffiliation = (String) getArguments().get(
				"teamFederationAffiliation");
		FontUtils.setCustomfont(mContext, teamFederationFoundationText,
				FontUtils.FontTypes.ROBOTO_REGULAR);
		FontUtils.setCustomfont(mContext,
				generalView.findViewById(R.id.teamFederationAffiliationLabel),
				FontUtils.FontTypes.ROBOTO_BOLD);
		teamFederationAffiliationText.setText(teamFederationAffiliation);

		TextView teamFederationWebText = (TextView) generalView
				.findViewById(R.id.teamFederationWebText);
		String teamFederationWeb = (String) getArguments().get(
				"teamFederationWeb");
		FontUtils.setCustomfont(mContext, teamFederationWebText,
				FontUtils.FontTypes.ROBOTO_REGULAR);
		FontUtils.setCustomfont(mContext,
				generalView.findViewById(R.id.teamFederationWebLabel),
				FontUtils.FontTypes.ROBOTO_BOLD);
		teamFederationWebText.setText(teamFederationWeb);

		((View) teamFederationWebText.getParent()).setTag(teamFederationWeb);
		((View) teamFederationWebText.getParent())
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String url = (String) v.getTag();
						goToWeb(url);

					}

				});

	}

	private void goToWeb(String url) {
		if (url != null && !url.equalsIgnoreCase("")) {
			if (url.contains("http") == false) {
				url = "http://" + url;
			}
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

			intent.addCategory(Intent.CATEGORY_BROWSABLE);
			getActivity().startActivityForResult(intent,
					Defines.ReturnRequestCodes.PUBLI_BACK);
		}
	}

	private void configureStar() {

		ImageDAO.getInstance(mContext).loadRegularImage(
				(String) getArguments().get("starImage"),
				(ImageView) generalView.findViewById(R.id.playerImage),
				R.drawable.foto_generica, R.drawable.mask_foto);

		TextView starHistory = (TextView) generalView
				.findViewById(R.id.starHistory);

		String starHistoryText = (String) getArguments().get("starHistory");

		if (starHistoryText != null && !starHistoryText.equalsIgnoreCase("")) {
			FontUtils.setCustomfont(mContext, starHistory,
					FontUtils.FontTypes.ROBOTO_REGULAR);
			starHistory.setText(starHistoryText);
		} else {
			starHistory.setVisibility(View.GONE);
		}

		TextView starName = (TextView) generalView.findViewById(R.id.starName);
		String starNameText = (String) getArguments().get("starName");
		if (starNameText != null && !starNameText.equalsIgnoreCase("")) {
			FontUtils.setCustomfont(mContext, starName,
					FontUtils.FontTypes.ROBOTO_BOLD);
			starName.setText(starNameText);
		} else {
			starName.setVisibility(View.GONE);
		}

		FontUtils.setCustomfont(mContext,
				generalView.findViewById(R.id.starPositionLabel),
				FontUtils.FontTypes.ROBOTO_BOLD);
		TextView starPosition = (TextView) generalView
				.findViewById(R.id.starPosition);
		String starPositionText = (String) getArguments().get("starPosition");
		if (starPositionText != null && !starPositionText.equalsIgnoreCase("")) {
			FontUtils.setCustomfont(mContext, starPosition,
					FontUtils.FontTypes.ROBOTO_REGULAR);
			starPosition.setText(starPositionText);
			// } else {
			// generalView.findViewById(R.id.starPositionContent).setVisibility(
			// View.GONE);
		}

		FontUtils.setCustomfont(mContext,
				generalView.findViewById(R.id.starNumInterLabel),
				FontUtils.FontTypes.ROBOTO_BOLD);
		TextView starNumInter = (TextView) generalView
				.findViewById(R.id.starNumInter);
		String starNumInternationalText = (String) getArguments().get(
				"starNumInternational");
		if (starNumInternationalText != null
				&& !starNumInternationalText.equalsIgnoreCase("")) {
			FontUtils.setCustomfont(mContext, starNumInter,
					FontUtils.FontTypes.ROBOTO_REGULAR);
			starNumInter.setText(starNumInternationalText);
			// } else {
			// generalView.findViewById(R.id.starNumInterContent).setVisibility(
			// View.GONE);
		}

		FontUtils.setCustomfont(mContext,
				generalView.findViewById(R.id.starAgeLabel),
				FontUtils.FontTypes.ROBOTO_BOLD);
		TextView starAge = (TextView) generalView.findViewById(R.id.starAge);
		String starAgeText = (String) getArguments().get("starAge");
		if (starAgeText != null && !starAgeText.equalsIgnoreCase("")) {
			FontUtils.setCustomfont(mContext, starAge,
					FontUtils.FontTypes.ROBOTO_REGULAR);
			starAge.setText(starAgeText);
			// } else {
			// generalView.findViewById(R.id.starAgeContent).setVisibility(
			// View.GONE);
		}

		FontUtils.setCustomfont(mContext,
				generalView.findViewById(R.id.starStatureLabel),
				FontUtils.FontTypes.ROBOTO_BOLD);
		TextView starStature = (TextView) generalView
				.findViewById(R.id.starStature);
		String starStatureText = (String) getArguments().get("starStature");
		if (starStatureText != null && !starStatureText.equalsIgnoreCase("")) {
			FontUtils.setCustomfont(mContext, starStature,
					FontUtils.FontTypes.ROBOTO_REGULAR);
			starStature.setText(starStatureText);
			// } else {
			// generalView.findViewById(R.id.starStatureContent).setVisibility(
			// View.GONE);
		}

		FontUtils.setCustomfont(mContext,
				generalView.findViewById(R.id.starWeightLabel),
				FontUtils.FontTypes.ROBOTO_BOLD);
		TextView starWeight = (TextView) generalView
				.findViewById(R.id.starWeight);
		String starWeightText = (String) getArguments().get("starWeight");
		if (starWeightText != null && !starWeightText.equalsIgnoreCase("")) {
			FontUtils.setCustomfont(mContext, starWeight,
					FontUtils.FontTypes.ROBOTO_REGULAR);
			starWeight.setText(starWeightText);
			// } else {
			// generalView.findViewById(R.id.starWeightContent).setVisibility(
			// View.GONE);
		}

		FontUtils.setCustomfont(mContext,
				generalView.findViewById(R.id.starClubLabel),
				FontUtils.FontTypes.ROBOTO_BOLD);
		TextView starClub = (TextView) generalView.findViewById(R.id.starClub);
		String starClubName = (String) getArguments().get("starClubName");
		if (starClubName != null && !starClubName.equalsIgnoreCase("")) {

			FontUtils.setCustomfont(mContext, starClub,
					FontUtils.FontTypes.ROBOTO_REGULAR);
			starClub.setText(starClubName);
			// } else {
			// generalView.findViewById(R.id.starClubContent).setVisibility(
			// View.GONE);
		}

		generalView.findViewById(R.id.starContent).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						String starUrl = getArguments().getString("starUrl");
						if (starUrl != null && starUrl.length() > 0) {
							Intent intent = new Intent(getActivity(),
									PlayerActivity.class);
							intent.putExtra("playerUrl", starUrl);
							intent.putExtra("teamName", getArguments()
									.getString("teamName"));
							String starId = getArguments().getString("starId");
							if (starId != null && starId.length() > 0) {
								intent.putExtra("playerId",
										Integer.valueOf(starId));
							}

							// intent.putExtra("teamName",
							// currentTeam.getShortName());
							getActivity().startActivityForResult(intent,
									Defines.ReturnRequestCodes.PUBLI_BACK);
							getActivity().overridePendingTransition(
									R.anim.grow_from_middle,
									R.anim.shrink_to_middle);
						}
					}
				});
	}

	private void configureVideo() {

		ImageView videoImage = (ImageView) generalView
				.findViewById(R.id.videoImage);
		final String videoUrl;
		String videoImageUrl = null;
		if (getArguments().containsKey("videoImageUrl"))
			videoUrl = getArguments().getString("videoUrl");
		else
			videoUrl = null;

		if (getArguments().containsKey("videoImageUrl"))
			videoImageUrl = getArguments().getString("videoImageUrl");

		if (videoImage != null && videoImageUrl != null
				&& !videoImageUrl.equalsIgnoreCase("")) {
			ImageDAO.getInstance(mContext).loadRegularImage(videoImageUrl,
					videoImage);

		} else if (!(videoUrl != null && !videoUrl.equalsIgnoreCase(""))) {
			generalView.findViewById(R.id.videoContainer).setVisibility(
					View.GONE);
		}

		if (videoUrl != null && !videoUrl.equalsIgnoreCase("")) {
			videoImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, VideoActivity.class);
					intent.putExtra("url", videoUrl);
					intent.putExtra("section",
							getArguments().getString("teamName"));
					intent.putExtra("subsection",
							getArguments().getString("teamName"));
					getActivity().startActivityForResult(intent,
							Defines.ReturnRequestCodes.PUBLI_BACK);
					getActivity().overridePendingTransition(
							R.anim.grow_from_middle, R.anim.shrink_to_middle);
				}
			});
		}
	}

	private void configureMister() {

		TextView misterHistoryText = (TextView) generalView
				.findViewById(R.id.misterHistoryText);
		TextView misterNameText = (TextView) generalView
				.findViewById(R.id.misterNameText);
		ImageView misterImage = (ImageView) generalView
				.findViewById(R.id.misterImage);

		String misterImageText = (String) getArguments().get("misterImage");
		String misterName = (String) getArguments().get("misterName");
		String misterHistory = (String) getArguments().get("misterHistory");

		if (misterName != null && !misterName.equalsIgnoreCase("")) {
			if (misterHistory != null && !misterHistory.equalsIgnoreCase("")) {
				FontUtils.setCustomfont(mContext, misterHistoryText,
						FontUtils.FontTypes.ROBOTO_REGULAR);
				misterHistoryText.setText(misterHistory);
			} else {
				misterHistoryText.setVisibility(View.GONE);
			}
			if (misterImageText != null
					&& !misterImageText.equalsIgnoreCase("")) {

				ImageDAO.getInstance(mContext).loadRegularImage(
						misterImageText, misterImage,
						R.drawable.galeria_imagenrecurso, -1);
			}

			FontUtils.setCustomfont(mContext,
					generalView.findViewById(R.id.misterNameLabel),
					FontUtils.FontTypes.ROBOTO_REGULAR);
			FontUtils.setCustomfont(mContext, misterNameText,
					FontUtils.FontTypes.ROBOTO_BOLD);
			misterNameText.setText(misterName);
		}
	}

	private void configureShirts() {
		String shirt1 = (String) getArguments().get("shirt1");
		int idShirt1 = 0;
		if (shirt1 != null && !shirt1.equalsIgnoreCase("")) {
			idShirt1 = DrawableUtils.getDrawableId(mContext, shirt1, 4);
		}
		String shirt2 = (String) getArguments().get("shirt2");
		int idShirt2 = 0;
		if (shirt2 != null && !shirt2.equalsIgnoreCase("")) {
			idShirt2 = DrawableUtils.getDrawableId(mContext, shirt2, 4);
		}
		String shirt3 = (String) getArguments().get("shirt3");
		int idShirt3 = 0;
		if (shirt3 != null && !shirt3.equalsIgnoreCase("")) {
			idShirt3 = DrawableUtils.getDrawableId(mContext, shirt3, 4);
		}

		if (idShirt1 != 0 || idShirt2 != 0 || idShirt3 != 0) {
			LinearLayout llShield = (LinearLayout) generalView
					.findViewById(R.id.shirtContent);

			FontUtils.setCustomfont(mContext,
					generalView.findViewById(R.id.shirtLabel),
					FontUtils.FontTypes.ROBOTO_REGULAR);

			Point size = DimenUtils.getSize(getActivity().getWindowManager());
			int width = size.x;

			LayoutParams parameters = new LinearLayout.LayoutParams(width / 3,
					LayoutParams.WRAP_CONTENT);

			if (idShirt1 != 0) {
				RelativeLayout contentShirt1 = (RelativeLayout) inflater
						.inflate(R.layout.item_shirt, null);
				TextView shield1Text = (TextView) contentShirt1
						.findViewById(R.id.shirtText);
				shield1Text.setText(getString(R.string.team_equipacion_local));
				FontUtils.setCustomfont(mContext, shield1Text,
						FontUtils.FontTypes.ROBOTO_REGULAR);

				((ImageView) contentShirt1.findViewById(R.id.shirtImage))
						.setBackgroundResource(idShirt1);
				llShield.addView(contentShirt1, parameters);
			}
			if (idShirt2 != 0) {
				RelativeLayout contentShirt2 = (RelativeLayout) inflater
						.inflate(R.layout.item_shirt, null);
				TextView shield2Text = (TextView) contentShirt2
						.findViewById(R.id.shirtText);
				shield2Text
						.setText(getString(R.string.team_equipacion_visitante));
				FontUtils.setCustomfont(mContext, shield2Text,
						FontUtils.FontTypes.ROBOTO_REGULAR);

				((ImageView) contentShirt2.findViewById(R.id.shirtImage))
						.setBackgroundResource(idShirt2);
				llShield.addView(contentShirt2, parameters);
			}
			if (idShirt3 != 0) {
				RelativeLayout contentShirt3 = (RelativeLayout) inflater
						.inflate(R.layout.item_shirt, null);
				TextView shield3Text = (TextView) contentShirt3
						.findViewById(R.id.shirtText);
				shield3Text
						.setText(getString(R.string.team_equipacion_alternativa));
				FontUtils.setCustomfont(mContext, shield3Text,
						FontUtils.FontTypes.ROBOTO_REGULAR);
				((ImageView) contentShirt3.findViewById(R.id.shirtImage))
						.setBackgroundResource(idShirt3);
				llShield.addView(contentShirt3, parameters);
			}
		} else {
			generalView.findViewById(R.id.gapShirt).setVisibility(View.GONE);
			generalView.findViewById(R.id.shirtLabel).setVisibility(View.GONE);
			generalView.findViewById(R.id.shirtContent)
					.setVisibility(View.GONE);
		}
	}

}
