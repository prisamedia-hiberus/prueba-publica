package com.diarioas.guiamundial.activities.player.fragment;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.dao.model.player.ItemTrayectoria;
import com.diarioas.guiamundial.dao.model.player.Trayectoria;
import com.diarioas.guiamundial.utils.DimenUtils;
import com.diarioas.guiamundial.utils.FontUtils;
import com.diarioas.guiamundial.utils.comparator.TrayectoriaComparator;

public class PlayerTrayectoriaFragment extends PlayerFragment {

	private ArrayList<Trayectoria> trayectoria;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;

		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_player_trayectoria,
				container, false);

		return generalView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// if (trayectoria != null) {
		// trayectoria.clear();
		// trayectoria = null;
		// }
	}

	@Override
	protected void configureData() {

		trayectoria = getArguments().getParcelableArrayList("trayectoria");

		Collections.sort(trayectoria, new TrayectoriaComparator());

		LinearLayout ll = ((LinearLayout) generalView
				.findViewById(R.id.linearTrayectoria));
		if (trayectoria.size() != 0) {

			View header = inflater.inflate(
					R.layout.item_headerlist_trayectoria, null);
			ll.addView(header);
			// Obtengo la lista de items
			LinearLayout convertView;
			for (Trayectoria tray : trayectoria) {
				for (ItemTrayectoria item : tray.getEquipos()) {
					convertView = (LinearLayout) inflater.inflate(
							R.layout.item_list_trayectoria, null);

					((TextView) convertView.findViewById(R.id.yearTrayectoria))
							.setText("" + tray.getYear());

					((TextView) convertView.findViewById(R.id.teamTrayectoria))
							.setText(item.getNombre());

					((TextView) convertView
							.findViewById(R.id.partidosTrayectoria)).setText(""
							+ item.getPartidos());
					((TextView) convertView.findViewById(R.id.golesTrayectoria))
							.setText("" + item.getGoles());
					ll.addView(convertView);
				}
			}

			String competiciones = getArguments().getString("competiciones");

			// competiciones
			if (competiciones != null && !("".equalsIgnoreCase(competiciones))
					&& !("null".equalsIgnoreCase(competiciones))) {

				competiciones = competiciones.replace("[", "");
				competiciones = competiciones.replace("]", "");
				competiciones = competiciones.replace("\"", "");// Se quiten las
																// comillas

				TextView trayText = (TextView) generalView
						.findViewById(R.id.trayectoriaText);
				trayText.setText(trayText.getText() + " *");

				TextView datatText = new TextView(mContext);
				datatText.setTextColor(getResources().getColor(R.color.black));
				datatText.setTextSize(TypedValue.COMPLEX_UNIT_PT, getActivity()
						.getResources().getDimension(R.dimen.size_8_px));
				// datatText.setTextSize(getResources().getDimension(
				// R.dimen.size_8));

				datatText
						.setText(getString(R.string.player_trayectoria_competiciones_mostradas)
								+ " " + competiciones);

				datatText.setMaxLines(6);
				FontUtils.setCustomfont(mContext, datatText,
						FontUtils.FontTypes.ROBOTO_LIGHT);
				datatText.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

				int pad = DimenUtils.getRegularPixelFromDp(mContext, 10);
				datatText.setPadding(pad, 2 * pad, pad, pad);

				ll.addView(datatText);
			}
		} else {
			View noData = inflater.inflate(R.layout.item_player_no_trayectoria,
					null);
			ll.addView(noData);
		}
	}

}
