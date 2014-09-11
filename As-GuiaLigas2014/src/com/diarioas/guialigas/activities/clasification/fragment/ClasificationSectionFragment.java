package com.diarioas.guialigas.activities.clasification.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.fragment.SectionFragment;
import com.diarioas.guialigas.activities.home.HomeActivity;
import com.diarioas.guialigas.activities.team.TeamActivity;
import com.diarioas.guialigas.dao.model.calendar.Fase;
import com.diarioas.guialigas.dao.model.calendar.Grupo;
import com.diarioas.guialigas.dao.model.clasificacion.ClasificacionInfo;
import com.diarioas.guialigas.dao.model.clasificacion.LeyendaInfo;
import com.diarioas.guialigas.dao.model.competition.Competition;
import com.diarioas.guialigas.dao.model.general.ClasificacionSection;
import com.diarioas.guialigas.dao.model.team.Team;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.RemoteClasificacionDAO;
import com.diarioas.guialigas.dao.reader.RemoteClasificacionDAO.RemoteClasificacionDAOListener;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.AlertManager;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.DrawableUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;
import com.diarioas.guialigas.utils.comparator.GroupComparator;
import com.emilsjolander.components.StickyScrollViewItems.StickyScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshStickyScrollView;

public class ClasificationSectionFragment extends SectionFragment implements
		RemoteClasificacionDAOListener {

	private static final int ID_IMAGE = 500;

	private Fase fase;

	private PullToRefreshStickyScrollView pullToRefresh;

	private RelativeLayout container;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;

		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_clasification_section,
				container, false);
		return generalView;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		RemoteClasificacionDAO.getInstance(mContext).removeListener(this);
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/

	@Override
	protected void buildView() {
		reloadData();

	}

	private void reloadData() {
		HashMap<String, String> urls = ((ClasificacionSection) section)
				.getUrls();
		Set<String> keys = urls.keySet();
		RemoteClasificacionDAO.getInstance(mContext).addListener(this);
		RemoteClasificacionDAO.getInstance(mContext)
				.refreshClasificationWithNewResults(
						urls.get((keys.toArray())[0]));
	}

	@Override
	protected void configureView() {

		pullToRefresh = (PullToRefreshStickyScrollView) generalView
				.findViewById(R.id.teamPullContent);
		pullToRefresh.setClickable(false);
		pullToRefresh.setPullLabel(getString(R.string.ptr_pull_to_refresh));
		pullToRefresh.setRefreshingLabel(getString(R.string.ptr_refreshing));
		pullToRefresh
				.setReleaseLabel(getString(R.string.ptr_release_to_refresh));
		pullToRefresh
				.setOnRefreshListener(new OnRefreshListener<StickyScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<StickyScrollView> refreshView) {
						reloadData();
					}
				});

		container = (RelativeLayout) generalView.findViewById(R.id.container);
		callToOmniture();
	}

	private LinearLayout getTeamContent() {
		LinearLayout teamContent = new LinearLayout(mContext);
		teamContent.setOrientation(LinearLayout.VERTICAL);
		teamContent.setPadding(0, 0,
				DimenUtils.getRegularPixelFromDp(mContext, 5), 0);
		teamContent.setClickable(false);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		teamContent.setLayoutParams(params);
		return teamContent;
	}

	private void loadData() {

		pullToRefresh.setPullToRefreshEnabled(false);
		pullToRefresh.setPullToRefreshOverScrollEnabled(false);

		LinearLayout teamHeaderContent = (LinearLayout) generalView
				.findViewById(R.id.teamHeaderContent);
		teamHeaderContent.removeAllViews();
		container.removeAllViews();
		LinearLayout teamContent = getTeamContent();
		container.addView(teamContent);
		HashMap<String, Team> clasificacion;
		int color;
		ArrayList<Grupo> grupos = fase.getGrupos();
		Collections.sort(grupos, new GroupComparator());
		for (Grupo grupo : grupos) {
			if (grupos.size() > 1) {
				teamContent
						.addView(getHeader("GRUPO " + grupo.getName(), true));
			} else {
				teamHeaderContent.addView(getHeader("GRUPO " + grupo.getName(),
						false));
			}
			clasificacion = grupo.getClasificacion().getClasificacion();
			for (int i = 0; i < clasificacion.size(); i++) {
				color = getColorByZones(i + 1, fase.getLeyenda());
				teamContent
						.addView(getItem(
								clasificacion.get(String.valueOf(i + 1)),
								i + 1, color));
			}
			if (grupos.size() == 1)
				teamContent.addView(getFooter(fase.getLeyenda()));

		}
	}

	private int getColorByZones(int position,
			HashMap<Integer, LeyendaInfo> leyenda) {

		if (leyenda.containsKey(position)) {
			LeyendaInfo leyendaInfo = leyenda.get(position);
			String colorRGB = DatabaseDAO.getInstance(mContext)
					.getClasificationLabel(
							leyendaInfo.getTipo() + "-"
									+ leyendaInfo.getPosicion());
			return Color.parseColor(colorRGB);

		} else {
			return Color.TRANSPARENT;
		}
	}

	private View getHeader(String nameGroup, boolean showTitle) {
		View header = inflater.inflate(R.layout.item_list_clasificacion_header,
				null);

		if (showTitle) {
			header.setTag(StickyScrollView.STICKY_TAG);
			header.findViewById(R.id.groupNameContent).setVisibility(
					View.VISIBLE);
			TextView groupName = (TextView) header
					.findViewById(R.id.groupNameText);
			FontUtils.setCustomfont(mContext, groupName,
					FontTypes.HELVETICANEUE);
			groupName.setText(nameGroup);
		}
		FontUtils.setCustomfont(mContext, header.findViewById(R.id.teamText),
				FontTypes.HELVETICANEUE);
		FontUtils.setCustomfont(mContext, header.findViewById(R.id.puntosText),
				FontTypes.HELVETICANEUEBOLD);
		FontUtils.setCustomfont(mContext,
				header.findViewById(R.id.partJugText), FontTypes.HELVETICANEUE);
		FontUtils.setCustomfont(mContext,
				header.findViewById(R.id.partGanText), FontTypes.HELVETICANEUE);
		FontUtils.setCustomfont(mContext,
				header.findViewById(R.id.partEmpText), FontTypes.HELVETICANEUE);
		FontUtils.setCustomfont(mContext,
				header.findViewById(R.id.partPerText), FontTypes.HELVETICANEUE);
		FontUtils
				.setCustomfont(mContext,
						header.findViewById(R.id.golesFavText),
						FontTypes.HELVETICANEUE);
		FontUtils.setCustomfont(mContext,
				header.findViewById(R.id.golesContText),
				FontTypes.HELVETICANEUE);

		return header;
	}

	/**
	 * @param teamContent
	 * @param team
	 * @param position
	 * @return
	 */
	private View getItem(Team team, int position, int color) {
		ClasificacionInfo teamClasificacion = team.getClasificacion();

		View item = inflater.inflate(R.layout.item_list_clasificacion, null);

		TextView teamText = (TextView) item.findViewById(R.id.teamText);
		FontUtils.setCustomfont(mContext, teamText, FontTypes.HELVETICANEUE);
		teamText.setText(team.getShortName().toUpperCase());

		TextView teamPositionText = (TextView) item
				.findViewById(R.id.teamPositionText);
		FontUtils.setCustomfont(mContext, teamPositionText,
				FontTypes.HELVETICANEUE);
		teamPositionText.setText(String.valueOf(position));

		TextView puntosText = (TextView) item.findViewById(R.id.puntosText);
		FontUtils.setCustomfont(mContext, puntosText,
				FontTypes.HELVETICANEUEBOLD);
		puntosText.setText(String.valueOf(teamClasificacion.getPts()));

		TextView partJugText = (TextView) item.findViewById(R.id.partJugText);
		FontUtils.setCustomfont(mContext, partJugText, FontTypes.HELVETICANEUE);
		partJugText.setText(String.valueOf(teamClasificacion.getPj()));

		TextView partGanText = (TextView) item.findViewById(R.id.partGanText);
		FontUtils.setCustomfont(mContext, partGanText, FontTypes.HELVETICANEUE);
		partGanText.setText(String.valueOf(teamClasificacion.getPg()));

		TextView partEmpText = (TextView) item.findViewById(R.id.partEmpText);
		FontUtils.setCustomfont(mContext, partEmpText, FontTypes.HELVETICANEUE);
		partEmpText.setText(String.valueOf(teamClasificacion.getPe()));

		TextView partPerText = (TextView) item.findViewById(R.id.partPerText);
		FontUtils.setCustomfont(mContext, partPerText, FontTypes.HELVETICANEUE);
		partPerText.setText(String.valueOf(teamClasificacion.getPp()));

		TextView golesFavText = (TextView) item.findViewById(R.id.golesFavText);
		FontUtils
				.setCustomfont(mContext, golesFavText, FontTypes.HELVETICANEUE);
		golesFavText.setText(String.valueOf(teamClasificacion.getGf()));

		TextView golesContText = (TextView) item
				.findViewById(R.id.golesContText);
		FontUtils.setCustomfont(mContext, golesContText,
				FontTypes.HELVETICANEUE);
		golesContText.setText(String.valueOf(teamClasificacion.getGc()));

		ImageView shieldImage = (ImageView) item.findViewById(R.id.shieldImage);
		ImageView positionImage = (ImageView) item
				.findViewById(R.id.positionImage);
		positionImage.setBackgroundColor(color);

		int idLocal = 0;
		team = DatabaseDAO.getInstance(mContext).getTeam(team.getId());

		if (team.getCalendarShield() != null) {
			idLocal = DrawableUtils.getDrawableId(getActivity()
					.getApplicationContext(), team.getCalendarShield(), 4);
			item.setTag(team.getId());
			item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					 selectedTeam((String)v.getTag());
				}
			});

		}else{
			idLocal = R.drawable.escudo_generico_size02;
		}

		shieldImage.setBackgroundResource(idLocal);


		
		return item;
	}

	protected void selectedTeam(String teamId) {
		Team team = DatabaseDAO.getInstance(mContext).getTeam(teamId);
		if (team != null && team.getUrlInfo() != null&& !team.getUrlInfo().equalsIgnoreCase("")) {
			Intent intent = new Intent(mContext, TeamActivity.class);
			intent.putExtra("teamId", teamId);
			intent.putExtra("competitionId", String.valueOf(competitionId));
			Competition comp = DatabaseDAO.getInstance(mContext)
					.getCompetition(Integer.valueOf(competitionId));
			if (comp != null)
				intent.putExtra("competitionName", comp.getName());

			getActivity().startActivityForResult(intent,
					ReturnRequestCodes.PUBLI_BACK);
			getActivity().overridePendingTransition(R.anim.grow_from_middle,
					R.anim.shrink_to_middle);
		}
	}

	/**
	 * @param teamContent
	 * @param leyenda
	 */
	private View getFooter(HashMap<Integer, LeyendaInfo> leyenda) {
		// LinearLayout footer = (LinearLayout)
		// inflater.inflate(R.layout.item_list_clasificacion_footer, null);
		LinearLayout footer = new LinearLayout(mContext);
		android.view.ViewGroup.LayoutParams paramsFooter = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		int dim2 = DimenUtils.getRegularPixelFromDp(mContext, 2);
		int dim5 = DimenUtils.getRegularPixelFromDp(mContext, 5);
		int dim10 = dim5 * 2;
		int dim15 = dim5 * 3;

		footer.setPadding(dim5, dim2 * 10, dim5, dim10);
		footer.setLayoutParams(paramsFooter);
		footer.setOrientation(LinearLayout.VERTICAL);

		RelativeLayout rel;

		ImageView image;
		TextView texto;

		LinearLayout.LayoutParams params;
		RelativeLayout.LayoutParams paramsImage;
		RelativeLayout.LayoutParams paramsText;

		LeyendaInfo info;
		ArrayList<String> tipos = new ArrayList<String>();
		Object[] keys = leyenda.keySet().toArray();

		Arrays.sort(keys);

		for (int i = 0; i < keys.length; i++) {
			info = leyenda.get(keys[i]);
			if (!tipos.contains(info.getTipo() + "-" + info.getPosicion())) {
				tipos.add(info.getTipo() + "-" + info.getPosicion());
				rel = new RelativeLayout(mContext);
				params = new LinearLayout.LayoutParams(
						android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
						0);
				params.weight = 1;
				rel.setPadding(0, dim2, 0, dim2);
				rel.setLayoutParams(params);

				// ImageView
				image = new ImageView(mContext);
				image.setId(ID_IMAGE + i);

				image.setBackgroundColor(getColorByZones((Integer) keys[i],
						leyenda));
				paramsImage = new RelativeLayout.LayoutParams(dim15, dim15);
				paramsImage.addRule(RelativeLayout.CENTER_VERTICAL);
				paramsImage.setMargins(2, 0, 2, 0);
				image.setLayoutParams(paramsImage);
				rel.addView(image);

				// TextView
				texto = new TextView(mContext);
				FontUtils.setCustomfont(mContext, texto,
						FontTypes.HELVETICANEUE);
				texto.setGravity(Gravity.LEFT);
				texto.setMaxLines(3);
				texto.setEllipsize(TruncateAt.END);
				texto.setHorizontallyScrolling(false);
				texto.setTextSize(TypedValue.COMPLEX_UNIT_PT, getResources()
						.getDimension(R.dimen.size_item_list_clasificacion_8));
				texto.setText(info.getTitulo().toUpperCase());
				texto.setTextColor(getResources().getColor(R.color.medium_gray));

				paramsText = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				paramsText.addRule(RelativeLayout.CENTER_VERTICAL);
				paramsText.addRule(RelativeLayout.RIGHT_OF, image.getId());
				paramsText.leftMargin = dim5;
				texto.setLayoutParams(paramsText);
				rel.addView(texto);

				footer.addView(rel);
			}
		}

		return footer;
	}

	private void stopAnimation() {
		((HomeActivity) getActivity()).stopAnimation();
		if (pullToRefresh != null) {
			pullToRefresh.onRefreshComplete();
			// pullToRefresh
			// .setLastUpdatedLabel(getString(R.string.ptr_last_updated)
			// + dateFormatPull.format(new Date()));
		}
	}

	/***************************************************************************/
	/** Libraries methods **/
	/***************************************************************************/
	@Override
	protected void callToOmniture() {
		
		Competition comp = DatabaseDAO.getInstance(mContext).getCompetition(Integer.valueOf(competitionId));
		StatisticsDAO.getInstance(mContext).sendStatisticsState(
				getActivity().getApplication(),
				comp.getName().toLowerCase(),
				Omniture.SECTION_CLASIFICATION,
				null,
				null,
				Omniture.TYPE_PORTADA,
				Omniture.DETAILPAGE_DETALLE + " "
						+ Omniture.SECTION_CLASIFICATION, null);
	}

	@Override
	public void callToAds() {
		callToAds(NativeAds.AD_CLASIFICATION+ "/" + NativeAds.AD_PORTADA);
	}

	/***************************************************************************/
	/** RemoteClasificacionDAOListener methods **/
	/***************************************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.RemoteClasificacionDAO.
	 * RemoteClasificacionDAOListener#onSuccessRemoteconfig(java.util.ArrayList)
	 */
	@Override
	public void onSuccessRemoteconfig(Fase fase) {
		RemoteClasificacionDAO.getInstance(mContext).removeListener(this);
		this.fase = fase;
		// this.teams = teams;
		loadData();
		stopAnimation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.RemoteClasificacionDAO.
	 * RemoteClasificacionDAOListener#onFailureRemoteconfig()
	 */
	@Override
	public void onFailureRemoteconfig() {
		RemoteClasificacionDAO.getInstance(mContext).removeListener(this);
		AlertManager.showAlertOkDialog(getActivity(),
				getResources().getString(R.string.clasificacion_error),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						container.removeAllViews();
						container.addView(getErrorContainer());
						pullToRefresh.setPullToRefreshEnabled(true);
						pullToRefresh.setPullToRefreshOverScrollEnabled(true);

					}

				});

		stopAnimation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.RemoteClasificacionDAO.
	 * RemoteClasificacionDAOListener#onFailureNotConnection()
	 */
	@Override
	public void onFailureNotConnection() {
		RemoteClasificacionDAO.getInstance(mContext).removeListener(this);
		AlertManager.showAlertOkDialog(getActivity(),
				getResources().getString(R.string.section_not_conection),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						container.removeAllViews();
						container.addView(getErrorContainer());
						pullToRefresh.setPullToRefreshEnabled(true);
						pullToRefresh.setPullToRefreshOverScrollEnabled(true);
					}

				});

		stopAnimation();
	}
}
