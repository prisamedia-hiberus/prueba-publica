package com.diarioas.guiamundial.activities.team.fragment;

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

import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.dao.model.team.Staff;
import com.diarioas.guiamundial.utils.DimenUtils;
import com.diarioas.guiamundial.utils.DrawableUtils;
import com.diarioas.guiamundial.utils.FontUtils;

public class TeamInfoFragment extends TeamFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_team_info, container,
				false);
		return generalView;
	}

	@Override
	protected void configureView() {

		/************************** Info General ******************************************/
		String shield = (String) getArguments().get("shield");
		if (shield != null && !shield.equalsIgnoreCase("")) {
			((ImageView) generalView.findViewById(R.id.photoTeam))
					.setBackgroundResource(DrawableUtils.getDrawableId(mContext, shield, 4));
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
			idShirt1 = mContext.getResources().getIdentifier(
					shirt1.substring(0, shirt1.length() - 4), "drawable",
					mContext.getPackageName());
		}
		String shirt2 = (String) getArguments().get("shirt2");
		int idShirt2 = 0;
		if (shirt2 != null && !shirt2.equalsIgnoreCase("")) {
			idShirt2 = mContext.getResources().getIdentifier(
					shirt2.substring(0, shirt1.length() - 4), "drawable",
					mContext.getPackageName());
		}
		String shirt3 = (String) getArguments().get("shirt3");
		int idShirt3 = 0;
		if (shirt3 != null && !shirt3.equalsIgnoreCase("")) {
			idShirt3 = mContext.getResources().getIdentifier(
					shirt3.substring(0, shirt1.length() - 4), "drawable",
					mContext.getPackageName());
		}

		if (idShirt1 != 0 || idShirt2 != 0 || idShirt3 != 0) {
			LinearLayout llShield = (LinearLayout) generalView
					.findViewById(R.id.shieldContent1);
			LayoutInflater inf = LayoutInflater.from(mContext);

			Point size = DimenUtils.getSize(getActivity().getWindowManager());
			int width = size.x;

			LayoutParams parameters = new LinearLayout.LayoutParams(width / 3,
					LayoutParams.WRAP_CONTENT);

			if (idShirt1 != 0) {
				RelativeLayout contentShirt1 = (RelativeLayout) inf.inflate(
						R.layout.item_shirt, null);
				TextView shield1Text = (TextView) contentShirt1
						.findViewById(R.id.shirtText);
				shield1Text.setText(getString(R.string.team_equipacion_local));
				FontUtils.setCustomfont(mContext, shield1Text,
						FontUtils.FontTypes.ROBOTO_LIGHT);

				((ImageView) contentShirt1.findViewById(R.id.shirtImage))
						.setBackgroundResource(idShirt1);
				llShield.addView(contentShirt1, parameters);
			}
			if (idShirt2 != 0) {
				RelativeLayout contentShirt2 = (RelativeLayout) inf.inflate(
						R.layout.item_shirt, null);
				TextView shield2Text = (TextView) contentShirt2
						.findViewById(R.id.shirtText);
				shield2Text
						.setText(getString(R.string.team_equipacion_visitante));
				FontUtils.setCustomfont(mContext, shield2Text,
						FontUtils.FontTypes.ROBOTO_LIGHT);

				((ImageView) contentShirt2.findViewById(R.id.shirtImage))
						.setBackgroundResource(idShirt2);
				llShield.addView(contentShirt2, parameters);
			}
			if (idShirt3 != 0) {
				RelativeLayout contentShirt3 = (RelativeLayout) inf.inflate(
						R.layout.item_shirt, null);
				TextView shield3Text = (TextView) contentShirt3
						.findViewById(R.id.shirtText);
				shield3Text
						.setText(getString(R.string.team_equipacion_alternativa));
				FontUtils.setCustomfont(mContext, shield3Text,
						FontUtils.FontTypes.ROBOTO_LIGHT);
				((ImageView) contentShirt3.findViewById(R.id.shirtImage))
						.setBackgroundResource(idShirt3);
				llShield.addView(contentShirt3, parameters);
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
		boolean presidentActive = false;
		if (president != null && president.getName() != null
				&& !president.getName().equalsIgnoreCase("")
				&& !president.getName().equalsIgnoreCase(" ")) {
			presidentActive = true;
			if (president.getPhoto() != null
					&& !president.getPhoto().equalsIgnoreCase("")) {
				int idPresi = mContext.getResources().getIdentifier(
						president.getPhoto().substring(0,
								president.getPhoto().length() - 4), "drawable",
						mContext.getPackageName());
				if (idPresi != 0) {
					((ImageView) generalView
							.findViewById(R.id.presidentTeamImage))
							.setBackgroundResource(idPresi);
				}

			}
			FontUtils.setCustomfont(mContext, presidentLabelTeam,
					FontUtils.FontTypes.ROBOTO_LIGHT);
			FontUtils.setCustomfont(mContext, presidentName,
					FontUtils.FontTypes.ROBOTO_LIGHT);
			FontUtils.setCustomfont(mContext, presidentBorn,
					FontUtils.FontTypes.ROBOTO_LIGHT);
			FontUtils.setCustomfont(mContext, presidentContract,
					FontUtils.FontTypes.ROBOTO_LIGHT);
			presidentName.setText(president.getName());

			presidentBorn.setText(Html.fromHtml(getDate(
					getString(R.string.team_fecha_de_nacimiento),
					president.getBorn())));

			presidentContract.setText(Html.fromHtml(getDate(
					getString(R.string.team_fecha_del_cargo),
					president.getContract())));
		} else {
			generalView.findViewById(R.id.presidentContent).setVisibility(
					View.GONE);

		}

		Staff mister = (Staff) getArguments().get("mister");
		TextView misterLabelTeam = (TextView) generalView
				.findViewById(R.id.misterLabelTeam);
		TextView misterName = (TextView) generalView
				.findViewById(R.id.misterNameTeam);
		TextView misterBorn = (TextView) generalView
				.findViewById(R.id.misterBornLabelTeam);
		TextView misterContract = (TextView) generalView
				.findViewById(R.id.misterContractTeam);

		boolean misterActive = false;
		if (mister != null && mister.getName() != null
				&& !mister.getName().equalsIgnoreCase("")
				&& !mister.getName().equalsIgnoreCase(" ")) {
			misterActive = true;
			// Info del mister
			if (mister != null) {
				if (mister.getPhoto() != null
						&& !mister.getPhoto().equalsIgnoreCase("")) {
					int idMister = mContext.getResources().getIdentifier(
							mister.getPhoto().substring(0,
									mister.getPhoto().length() - 4),
							"drawable", mContext.getPackageName());
					if (idMister != 0)
						((ImageView) generalView
								.findViewById(R.id.misterTeamImage))
								.setBackgroundResource(idMister);
				}
				FontUtils.setCustomfont(mContext, misterLabelTeam,
						FontUtils.FontTypes.ROBOTO_LIGHT);
				FontUtils.setCustomfont(mContext, misterName,
						FontUtils.FontTypes.ROBOTO_LIGHT);
				FontUtils.setCustomfont(mContext, misterBorn,
						FontUtils.FontTypes.ROBOTO_LIGHT);
				FontUtils.setCustomfont(mContext, misterContract,
						FontUtils.FontTypes.ROBOTO_LIGHT);
				misterName.setText(mister.getName());

				misterBorn.setText(Html.fromHtml(getDate(
						getString(R.string.team_fecha_de_nacimiento),
						mister.getBorn())));

				misterContract.setText(Html.fromHtml(getDate(
						getString(R.string.team_fecha_del_cargo),
						mister.getContract())));
			}
		} else {
			generalView.findViewById(R.id.misterContent).setVisibility(
					View.GONE);

		}

		Staff manager = (Staff) getArguments().get("manager");
		TextView managerLabelTeam = (TextView) generalView
				.findViewById(R.id.managerLabelTeam);
		TextView managerName = (TextView) generalView
				.findViewById(R.id.managerNameTeam);
		TextView managerBorn = (TextView) generalView
				.findViewById(R.id.managerBornLabelTeam);
		TextView managerContract = (TextView) generalView
				.findViewById(R.id.managerContractTeam);

		boolean managerActive = false;
		if (manager != null && manager.getName() != null
				&& !manager.getName().equalsIgnoreCase("")
				&& !manager.getName().equalsIgnoreCase(" ")) {
			managerActive = true;
			// Info del manager
			if (manager != null) {
				if (manager.getPhoto() != null
						&& !manager.getPhoto().equalsIgnoreCase("")) {
					int idManager = mContext.getResources().getIdentifier(
							manager.getPhoto().substring(0,
									manager.getPhoto().length() - 4),
							"drawable", mContext.getPackageName());
					if (idManager != 0)
						((ImageView) generalView
								.findViewById(R.id.managerTeamImage))
								.setBackgroundResource(idManager);
				}

				FontUtils.setCustomfont(mContext, managerLabelTeam,
						FontUtils.FontTypes.ROBOTO_LIGHT);
				FontUtils.setCustomfont(mContext, managerName,
						FontUtils.FontTypes.ROBOTO_LIGHT);
				FontUtils.setCustomfont(mContext, managerBorn,
						FontUtils.FontTypes.ROBOTO_LIGHT);
				FontUtils.setCustomfont(mContext, managerContract,
						FontUtils.FontTypes.ROBOTO_LIGHT);
				managerName.setText(manager.getName());

				managerBorn.setText(Html.fromHtml(getDate(
						getString(R.string.team_fecha_de_nacimiento),
						manager.getBorn())));

				managerContract.setText(Html.fromHtml(getDate(
						getString(R.string.team_fecha_del_cargo),
						manager.getContract())));
			}

		} else {
			generalView.findViewById(R.id.managerContent).setVisibility(
					View.GONE);
		}

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

}
