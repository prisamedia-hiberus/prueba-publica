/**
 * 
 */
package com.diarioas.guiamundial.activities.carrusel.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.activities.carrusel.CarruselDetailActivity;
import com.diarioas.guiamundial.dao.model.calendar.Match;
import com.diarioas.guiamundial.dao.model.carrusel.ItemDirecto;
import com.diarioas.guiamundial.dao.reader.CarruselDAO;
import com.diarioas.guiamundial.dao.reader.CarruselDAO.CarruselDAODirectoListener;
import com.diarioas.guiamundial.utils.Defines.DateFormat;
import com.diarioas.guiamundial.utils.Defines.MatchEvents;
import com.diarioas.guiamundial.utils.FontUtils;
import com.diarioas.guiamundial.utils.FontUtils.FontTypes;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * @author robertosanchez
 * 
 */
public class CarruselDirectoFragment extends CarruselFragment implements
		CarruselDAODirectoListener {

	private Match match;
	private ArrayList<ItemDirecto> directos;
	private PullToRefreshListView directosListView;
	private DirectosAdapter directosAdapter;
	private SimpleDateFormat dateFormatPull;
	private int idShieldLocal;
	private int idShieldAway;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflating layout
		this.inflater = inflater;
		dateFormatPull = new SimpleDateFormat(DateFormat.PULL_FORMAT,
				Locale.getDefault());
		generalView = inflater.inflate(R.layout.fragment_carrusel_directo,
				container, false);
		idShieldLocal = getArguments().getInt("idShieldLocal");
		idShieldAway = getArguments().getInt("idShieldAway");
		configureView();
		updateInfo();
		return generalView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		CarruselDAO.getInstance(mContext).removeDirectoListener(this);
	}

	/**
	 * 
	 */
	@Override
	protected void configureView() {

		super.configureView();

		directosListView = (PullToRefreshListView) generalView
				.findViewById(R.id.directosListView);
		directosListView.setDisableScrollingWhileRefreshing(true);
		ListView inner = directosListView.getRefreshableView();
		inner.setDivider(null);
		inner.setDividerHeight(0);
		inner.setCacheColorHint(0);
		directosListView.setClickable(false);
		directosListView.setPullLabel(mContext
				.getString(R.string.ptr_pull_to_refresh));
		directosListView.setRefreshingLabel(mContext
				.getString(R.string.ptr_refreshing));
		directosListView.setReleaseLabel(mContext
				.getString(R.string.ptr_release_to_refresh));

		directosListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						((CarruselDetailActivity) getActivity())
								.updateCarrusel();
						// updateData();

					}

				});

		directosAdapter = new DirectosAdapter();
		directosListView.setAdapter(directosAdapter);

	}

	public void updateInfo() {
		if (CarruselDAO.getInstance(mContext).isDetailUpdating(url)) {
			directos = (ArrayList<ItemDirecto>) CarruselDAO.getInstance(
					mContext).getDetailCached(url);
			closePullToRefresh();
			loadData();
		} else {
			updateData();
			startAnimation();
		}
	}

	@Override
	public void updateData() {
		super.updateData();
		if (url != null) {
			CarruselDAO.getInstance(mContext).addDirectoListener(this);
			CarruselDAO.getInstance(mContext).getDirecto(url);
		} else {
			closePullToRefresh();
		}
	}

	/**
	 * 
	 */
	private void loadData() {

		directosAdapter.addItems(directos);

	}

	private void closePullToRefresh() {
		if (directosListView != null) {
			directosListView.onRefreshComplete();
			directosListView.setLastUpdatedLabel(mContext
					.getString(R.string.ptr_last_updated)
					+ dateFormatPull.format(new Date()));
		}
	}

	/**************** Metodos de CarruselDAODirectoListener *****************************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CarruselDAO.CarruselDAODirectoListener#
	 * onSuccessDirecto()
	 */
	@Override
	public void onSuccessDirecto(ArrayList<ItemDirecto> directos) {
		CarruselDAO.getInstance(mContext).removeDirectoListener(this);
		closePullToRefresh();
		this.directos = directos;
		stopAnimation();
		loadData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CarruselDAO.CarruselDAODirectoListener#
	 * onFailureDirecto()
	 */
	@Override
	public void onFailureDirecto() {
		CarruselDAO.getInstance(mContext).removeDirectoListener(this);
		closePullToRefresh();
		stopAnimation();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CarruselDAO.CarruselDAODirectoListener#
	 * onFailureDirectoNotConnection()
	 */
	@Override
	public void onFailureDirectoNotConnection() {
		CarruselDAO.getInstance(mContext).removeDirectoListener(this);
		closePullToRefresh();
		stopAnimation();
	}

	/********************************************************************************/
	/****************** ADAPTER *****************************************************/
	/********************************************************************************/

	class DirectosAdapter extends BaseAdapter {

		private static final int ITEM_NORMAL_TYPE = 0;
		private static final int ITEM_NO_MINUTE_TYPE = ITEM_NORMAL_TYPE + 1;
		private static final int ITEM_GOL_TYPE = ITEM_NO_MINUTE_TYPE + 1;
		private static final int TOTAL_TYPE = ITEM_GOL_TYPE + 1;
		private ArrayList<ItemDirecto> directos;

		public DirectosAdapter() {
			this.directos = new ArrayList<ItemDirecto>();

		}

		public void addItems(ArrayList<ItemDirecto> directos) {
			this.directos = directos;
			notifyDataSetChanged();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.BaseAdapter#getItemViewType(int)
		 */
		@Override
		public int getItemViewType(int position) {
			ItemDirecto itemDirecto = directos.get(position);
			if (itemDirecto.getMin() != -1) {
				if (itemDirecto.getTipo().equalsIgnoreCase(
						MatchEvents.MATCHEVENT_GOL))
					return ITEM_GOL_TYPE;
				else
					return ITEM_NORMAL_TYPE;
			} else {
				return ITEM_NO_MINUTE_TYPE;
			}
			// return super.getItemViewType(position);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.BaseAdapter#getViewTypeCount()
		 */
		@Override
		public int getViewTypeCount() {
			return TOTAL_TYPE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return directos.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public ItemDirecto getItem(int arg0) {
			// TODO Auto-generated method stub
			return directos.get(arg0);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ItemDirecto directo = directos.get(position);
			int type = getItemViewType(position);
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				switch (type) {
				case ITEM_NORMAL_TYPE:
					convertView = inflater.inflate(
							R.layout.item_list_carrusel_directos_normal, null);
					holder.minText = (TextView) convertView
							.findViewById(R.id.minText);
					FontUtils.setCustomfont(mContext, holder.minText,
							FontTypes.HELVETICANEUE);
					holder.icon = (ImageView) convertView
							.findViewById(R.id.icon);

					break;
				case ITEM_NO_MINUTE_TYPE:
					convertView = inflater.inflate(
							R.layout.item_list_carrusel_directos_nomin, null);
					break;
				case ITEM_GOL_TYPE:
					convertView = inflater.inflate(
							R.layout.item_list_carrusel_directos_gol, null);
					holder.minText = (TextView) convertView
							.findViewById(R.id.minText);
					FontUtils.setCustomfont(mContext, holder.minText,
							FontTypes.HELVETICANEUE);
					holder.icon = (ImageView) convertView
							.findViewById(R.id.icon);
					holder.resultTextLeft = (TextView) convertView
							.findViewById(R.id.resultTextLeft);
					FontUtils.setCustomfont(mContext, holder.resultTextLeft,
							FontTypes.HELVETICANEUEBOLD);
					holder.resultText = (TextView) convertView
							.findViewById(R.id.resultText);
					FontUtils.setCustomfont(mContext, holder.resultText,
							FontTypes.HELVETICANEUE);
					holder.resultTextRight = (TextView) convertView
							.findViewById(R.id.resultTextRight);
					FontUtils.setCustomfont(mContext, holder.resultTextRight,
							FontTypes.HELVETICANEUEBOLD);

					holder.localShield = (ImageView) convertView
							.findViewById(R.id.localShield);
					holder.awayShield = (ImageView) convertView
							.findViewById(R.id.awayShield);

					holder.localShield.setBackgroundResource(idShieldLocal);
					holder.awayShield.setBackgroundResource(idShieldAway);

					break;
				default:
					break;
				}

				holder.text = (TextView) convertView
						.findViewById(R.id.textText);
				FontUtils.setCustomfont(mContext, holder.text,
						FontTypes.HELVETICANEUE);
				holder.text.setMovementMethod(LinkMovementMethod.getInstance());

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position % 2 == 0) {
				convertView.setBackgroundColor(mContext.getResources()
						.getColor(R.color.white));
			} else {
				convertView.setBackgroundColor(mContext.getResources()
						.getColor(R.color.calendar_clear_gray));
			}

			String texto = directo.getTexto();
			if (directo.getTipo() != null
					&& !directo.getTipo().equalsIgnoreCase("")) {
				String tipo = directo.getTipo() + ". ";
				// if (directo.getTipo().equalsIgnoreCase(
				// Defines.ReturnMatchEvents.MATCHEVENT_CAMBIO)) {
				tipo = "<b><font color=\""
						+ mContext.getResources().getColor(
								R.color.yellow_directo) + "\" >" + tipo
						+ "</font></b>";
				// } else {
				// tipo = "<b >" + tipo + "</b>";
				// }

				texto = tipo + texto;
			}
			holder.text.setText(Html.fromHtml(texto));

			switch (type) {
			case ITEM_NORMAL_TYPE:
				holder.minText.setText(directo.getMin() + "'");
				if (directo.getIcon() != null
						&& !directo.getIcon().equalsIgnoreCase("")) {
					holder.icon
							.setBackgroundResource(getIcon(directo.getIcon()));
					holder.icon.setVisibility(View.VISIBLE);
				} else {
					holder.icon.setVisibility(View.GONE);
				}
				break;
			case ITEM_NO_MINUTE_TYPE:

				break;
			case ITEM_GOL_TYPE:
				holder.minText.setText(directo.getMin() + "'");
				if (directo.getIcon() != null
						&& !directo.getIcon().equalsIgnoreCase("")) {
					holder.icon
							.setBackgroundResource(getIcon(directo.getIcon()));
					holder.icon.setVisibility(View.VISIBLE);
				} else {
					holder.icon.setVisibility(View.INVISIBLE);
				}

				holder.resultTextLeft.setText(String.valueOf(directo
						.getGolLocal()));
				holder.resultTextRight.setText(String.valueOf(directo
						.getGolAway()));
				if (directo.getMarca().equalsIgnoreCase("local")) {
					holder.resultTextLeft.setTextColor(mContext.getResources()
							.getColor(R.color.red_directo));
					holder.resultTextRight.setTextColor(mContext.getResources()
							.getColor(R.color.gray_carrusel_resumen));
				} else {
					holder.resultTextLeft.setTextColor(mContext.getResources()
							.getColor(R.color.gray_carrusel_resumen));
					holder.resultTextRight.setTextColor(mContext.getResources()
							.getColor(R.color.red_directo));
				}
				break;

			default:
				break;
			}

			return convertView;
		}

	}

	static class ViewHolder {

		public ImageView awayShield;
		public ImageView localShield;
		public TextView resultTextRight;
		public TextView resultText;
		public TextView resultTextLeft;
		public ImageView icon;
		public TextView minText;
		public TextView text;

	}

	/**
	 * @param icon
	 * @return
	 */
	public int getIcon(String icon) {
		if (icon.equalsIgnoreCase("i-goal"))
			return R.drawable.icn_goles;
		else if (icon.equalsIgnoreCase("i-change"))
			return R.drawable.icn_cambio_directo;
		else if (icon.equalsIgnoreCase("i-y-card"))
			return R.drawable.icn_amarillas;
		else if (icon.equalsIgnoreCase("i-r-card"))
			return R.drawable.icn_rojas;
		else
			return 0;
	}
}
