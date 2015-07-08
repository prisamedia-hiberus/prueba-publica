package com.diarioas.guialigas.activities.team.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.dao.model.team.Staff;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.DrawableUtils;
import com.diarioas.guialigas.utils.FontUtils;

public class TeamInfoFragment extends TeamFragment {

	private LayoutInflater inflater;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;

		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_team_info, container,
				false);
		return generalView;
	}

	
	@Override
	protected void startFirstShow() {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void configureView() {

		/************************** Info General ******************************************/
		String shield = (String) getArguments().get("shield");
		if (shield != null && !shield.equalsIgnoreCase("")) {
			((ImageView) generalView.findViewById(R.id.photoTeam))
					.setBackgroundResource(DrawableUtils.getDrawableId(
							mContext, shield, 4));
		} else {
			((ImageView) generalView.findViewById(R.id.photoTeam))
					.setBackgroundResource(R.drawable.escudo_generico_size01);
		}

		TextView name = (TextView) generalView.findViewById(R.id.nameTeam);

		TextView web = (TextView) generalView.findViewById(R.id.webTeam);
		TextView estadio = (TextView) generalView
				.findViewById(R.id.estadioTeam);
		TextView estadioAddress = (TextView) generalView
				.findViewById(R.id.estadioAddressTeam);

		TextView mapTeam = (TextView) generalView.findViewById(R.id.mapTeam);
		FontUtils.setCustomfont(mContext, mapTeam,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		FontUtils.setCustomfont(mContext, name,
				FontUtils.FontTypes.ROBOTO_LIGHT);
		name.setText((String) getArguments().get("name"));

		TextView fundation = (TextView) generalView
				.findViewById(R.id.fundacionTeam);
		String fund = (String) getArguments().get("fundation");
		if (fund != null && !fund.equalsIgnoreCase("")) {
			FontUtils.setCustomfont(mContext, fundation,
					FontUtils.FontTypes.ROBOTO_LIGHT);
			fundation.setText(Html.fromHtml(getDate(
					getString(R.string.team_fundacion), fund)));
		} else {
			fundation.setVisibility(View.GONE);
		}

		String webtext = (String) getArguments().get("web");
		if (webtext != null && !webtext.equalsIgnoreCase("")) {
			FontUtils.setCustomfont(mContext, web,
					FontUtils.FontTypes.ROBOTO_LIGHT);
			web.setText(Html.fromHtml(getDate(getString(R.string.team_web),
					webtext)));
		} else {
			web.setVisibility(View.GONE);
		}

		String nameEst = (String) getArguments().get("nameEstadio");
		if (nameEst != null && !nameEst.equalsIgnoreCase("")) {
			FontUtils.setCustomfont(mContext, estadio,
					FontUtils.FontTypes.ROBOTO_LIGHT);
			estadio.setText(Html.fromHtml(getDate(
					getString(R.string.team_estadio), nameEst)));
			String addressEstadio = (String) getArguments().get(
					"addressEstadio");
			if (!addressEstadio.equalsIgnoreCase("")) {
				FontUtils.setCustomfont(mContext, estadioAddress,
						FontUtils.FontTypes.ROBOTO_LIGHT);

				estadioAddress.setText(addressEstadio);
				generalView.findViewById(R.id.estadioContent).setVisibility(
						View.VISIBLE);
			} else {
				generalView.findViewById(R.id.estadioContent).setVisibility(
						View.GONE);
			}
		} else {
			generalView.findViewById(R.id.estadioContent).setVisibility(
					View.GONE);
			estadio.setVisibility(View.GONE);
		}

		/*************************** Info de la Camisetas *****************************************/
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
					.findViewById(R.id.shieldContent1);

			Point size = DimenUtils.getSize(getActivity().getWindowManager());
			int width = size.x;

			LayoutParams parameters = new LinearLayout.LayoutParams(width / 3,
					LayoutParams.WRAP_CONTENT);

			if (idShirt1 != 0) {
				llShield.addView(getShirtItem(idShirt1), parameters);
			}
			if (idShirt2 != 0) {
				llShield.addView(getShirtItem(idShirt2), parameters);
			}
			if (idShirt3 != 0) {
				llShield.addView(getShirtItem(idShirt3), parameters);
			}
		} else {
			generalView.findViewById(R.id.gapShield).setVisibility(View.GONE);
			generalView.findViewById(R.id.shieldText).setVisibility(View.GONE);
			generalView.findViewById(R.id.shieldContent1).setVisibility(
					View.GONE);
		}
		/*************************** Info de la Historia *****************************************/

		TextView historyLabelText = (TextView) generalView
				.findViewById(R.id.historyLabelText);
		TextView historyText = (TextView) generalView
				.findViewById(R.id.historyText);

		String history = (String) getArguments().get("history");
		if (history != null && !history.equalsIgnoreCase("")) {
			FontUtils.setCustomfont(mContext, historyLabelText,
					FontUtils.FontTypes.ROBOTO_LIGHT);
			FontUtils.setCustomfont(mContext, historyText,
					FontUtils.FontTypes.ROBOTO_LIGHT);

			historyText.setText(history);
		} else {
			generalView.findViewById(R.id.gapHistory).setVisibility(View.GONE);
			historyLabelText.setVisibility(View.GONE);
			historyText.setVisibility(View.GONE);
		}

		/**************************** Info del Staff ****************************************/

		boolean presidentActive = configurePresident();
		boolean misterActive = configureMister();
		boolean managerActive = configureManager();

		if (presidentActive && (misterActive || managerActive)) {
			generalView.findViewById(R.id.gapPresident).setVisibility(
					View.VISIBLE);
		} else {
			generalView.findViewById(R.id.gapPresident)
					.setVisibility(View.GONE);
		}

		if (misterActive && managerActive) {
			generalView.findViewById(R.id.gapMister)
					.setVisibility(View.VISIBLE);
		} else {
			generalView.findViewById(R.id.gapMister).setVisibility(View.GONE);
		}
		if (presidentActive || misterActive || managerActive) {
			generalView.findViewById(R.id.gapStaff).setVisibility(View.VISIBLE);
		} else {
			generalView.findViewById(R.id.gapStaff).setVisibility(View.GONE);
		}
		/**************************** Info del articulo ****************************************/

		configureArticle();

	}

	private void configureArticle() {
		TextView article = (TextView) generalView
				.findViewById(R.id.articleTeam);
		TextView title = (TextView) generalView
				.findViewById(R.id.articleTitleTeam);
		TextView subtitle = (TextView) generalView
				.findViewById(R.id.articleSubTitleTeam);

		String articletext = getArguments().getString("article");
		if (articletext != null && !articletext.equalsIgnoreCase("")) {

			String author = getArguments().getString("author");
			String charge = getArguments().getString("charge");

			TextView authorText = (TextView) generalView
					.findViewById(R.id.articleAuthorNameTeam);
			FontUtils.setCustomfont(mContext, authorText,
					FontUtils.FontTypes.ROBOTO_LIGHT);

			TextView chargeText = (TextView) generalView
					.findViewById(R.id.articleChareAuthorTeam);

			if (author != null && !author.equalsIgnoreCase("")) {
				authorText.setText(author);

				chargeText.setVisibility(View.VISIBLE);
				FontUtils.setCustomfont(mContext, chargeText,
						FontUtils.FontTypes.ROBOTO_LIGHT);
				chargeText.setText(charge);
			} else {
				authorText.setText(getActivity().getString(
						R.string.team_autor_not_available));
				chargeText.setText(getActivity().getString(
						R.string.team_charge_not_available));
			}

			FontUtils.setCustomfont(mContext, article,
					FontUtils.FontTypes.ROBOTO_LIGHT);
			article.setText(articletext);

			String titletext = getArguments().getString("title");
			if (titletext != null && !titletext.equalsIgnoreCase("")) {
				FontUtils.setCustomfont(mContext, title,
						FontUtils.FontTypes.ROBOTO_LIGHT);
				title.setText(titletext);
			} else {
				title.setVisibility(View.GONE);
			}

			String subtitletext = getArguments().getString("subtitle");
			if (subtitletext != null && !subtitletext.equalsIgnoreCase("")) {
				FontUtils.setCustomfont(mContext, subtitle,
						FontUtils.FontTypes.ROBOTO_LIGHT);
				subtitle.setText(subtitletext);
			} else {
				subtitle.setVisibility(View.GONE);
			}
		} else {
			article.setVisibility(View.GONE);
			title.setVisibility(View.GONE);
			subtitle.setVisibility(View.GONE);
			generalView.findViewById(R.id.articleContent).setVisibility(
					View.GONE);
			generalView.findViewById(R.id.gapArticle).setVisibility(View.GONE);
		}
	}

	private RelativeLayout getShirtItem(int idShirt1) {
		RelativeLayout contentShirt1 = (RelativeLayout) inflater.inflate(
				R.layout.item_shirt, null);
		TextView shield1Text = (TextView) contentShirt1
				.findViewById(R.id.shirtText);
		shield1Text.setText(getString(R.string.team_equipacion_local));
		FontUtils.setCustomfont(mContext, shield1Text,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		((ImageView) contentShirt1.findViewById(R.id.shirtImage))
				.setImageDrawable(getResources().getDrawable(idShirt1));
		return contentShirt1;
	}

	private boolean configurePresident() {
		// Info del presidente
		Staff president = (Staff) getArguments().get("president");
		TextView presidentLabelTeam = (TextView) generalView
				.findViewById(R.id.presidentLabelTeam);
		TextView presidentName = (TextView) generalView
				.findViewById(R.id.presidentNameTeam);
		TextView presidentBorn = (TextView) generalView
				.findViewById(R.id.presidentBornLabelTeam);
		TextView presidentContract = (TextView) generalView
				.findViewById(R.id.presidentContractTeam);

		return configureStaff(
				president,
				presidentLabelTeam,
				presidentName,
				presidentBorn,
				presidentContract,
				((ImageView) generalView.findViewById(R.id.presidentTeamImage)),
				generalView.findViewById(R.id.presidentContent));
	}

	private boolean configureMister() {
		Staff mister = (Staff) getArguments().get("mister");
		TextView misterLabelTeam = (TextView) generalView
				.findViewById(R.id.misterLabelTeam);
		TextView misterName = (TextView) generalView
				.findViewById(R.id.misterNameTeam);
		TextView misterBorn = (TextView) generalView
				.findViewById(R.id.misterBornLabelTeam);
		TextView misterContract = (TextView) generalView
				.findViewById(R.id.misterContractTeam);

		return configureStaff(mister, misterLabelTeam, misterName, misterBorn,
				misterContract,
				((ImageView) generalView.findViewById(R.id.misterTeamImage)),
				generalView.findViewById(R.id.misterContent));
	}

	private boolean configureManager() {
		Staff manager = (Staff) getArguments().get("manager");
		TextView managerLabelTeam = (TextView) generalView
				.findViewById(R.id.managerLabelTeam);
		TextView managerName = (TextView) generalView
				.findViewById(R.id.managerNameTeam);
		TextView managerBorn = (TextView) generalView
				.findViewById(R.id.managerBornLabelTeam);
		TextView managerContract = (TextView) generalView
				.findViewById(R.id.managerContractTeam);

		return configureStaff(manager, managerLabelTeam, managerName,
				managerBorn, managerContract,
				((ImageView) generalView.findViewById(R.id.managerTeamImage)),
				generalView.findViewById(R.id.managerContent));
	}

	private boolean configureStaff(Staff staff, TextView staffLabelTeam,
			TextView staffName, TextView staffBorn, TextView staffContract,
			ImageView staffImage, View staffContent) {
		boolean managerActive = false;
		if (staff != null && staff.getName() != null
				&& !staff.getName().equalsIgnoreCase("")
				&& !staff.getName().equalsIgnoreCase(" ")) {
			managerActive = true;
			// Info del manager
			if (staff != null) {
				if (staff.getPhoto() != null
						&& !staff.getPhoto().equalsIgnoreCase("")) {
					int idManager = DrawableUtils.getDrawableId(mContext,
							staff.getPhoto(), 4);
					if (idManager != 0) {
						staffImage.setImageDrawable(getResources().getDrawable(
								idManager));
					}
				}

				FontUtils.setCustomfont(mContext, staffLabelTeam,
						FontUtils.FontTypes.ROBOTO_LIGHT);
				FontUtils.setCustomfont(mContext, staffName,
						FontUtils.FontTypes.ROBOTO_LIGHT);
				FontUtils.setCustomfont(mContext, staffBorn,
						FontUtils.FontTypes.ROBOTO_LIGHT);
				FontUtils.setCustomfont(mContext, staffContract,
						FontUtils.FontTypes.ROBOTO_LIGHT);
				staffName.setText(staff.getName());

				staffBorn.setText(Html.fromHtml(getDate(
						getString(R.string.team_fecha_de_nacimiento),
						staff.getBorn())));

				staffContract.setText(Html.fromHtml(getDate(
						getString(R.string.team_fecha_del_cargo),
						staff.getContract())));
			}

		} else {
			staffContent.setVisibility(View.GONE);
		}
		return managerActive;
	}

}
