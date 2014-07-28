/**
 * 
 */
package com.diarioas.guiamundial.dao.reader;

import java.util.HashMap;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.diarioas.guiamundial.utils.Defines.Omniture;
import com.omniture.AppMeasurement;

/**
 * @author robertosanchez
 * 
 */
public class StatisticsDAO {

	private static StatisticsDAO sInstance;
	private static String version;

	public static StatisticsDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new StatisticsDAO(ctx);
		}

		return sInstance;
	}

	private final Context mContext;

	public StatisticsDAO(Context ctx) {
		this.mContext = ctx;
		PackageInfo pInfo = null;
		try {
			pInfo = ctx.getPackageManager().getPackageInfo(
					ctx.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		version = pInfo.versionName;
	}

	public void sendStatisticsState(Application app, String section,
			String subsection, String subsubsection, String tema, String tipo,
			String detailPage, HashMap<String, String> info) {

		Log.d("OMMNITURE", "sendStaticsOnState--> " + section + " :: "
				+ subsection + " :: " + subsubsection + " :: " + tema + " :: "
				+ tipo + " :: " + detailPage);
		AppMeasurement s = getAppMeasurement(app, section, subsection,
				subsubsection, tema, tipo, detailPage);

		s.events = "event2";
		s.currencyCode = "EUR";

		// Log.d("OMMNITURE", "CHANNEL--> " + s.channel + " PROP1--> " + s.prop1
		// + " PROP2--> " + s.prop2 + " PROP3--> " + s.prop3
		// + " PROP4--> " + s.prop4 + " PAGENAME--> " + s.pageName);
		// s.track();
	}

	public void sendStatisticsAction(Application app, String section,
			String subsection, String subsubsection, String tema, String tipo,
			String detailPage, HashMap<String, String> info) {

		AppMeasurement s = getAppMeasurement(app, section, subsection,
				subsubsection, tema, tipo, detailPage);

		s.linkTrackEvents = s.events;

		String evento = "";

		if (info != null && info.size() > 0) {
			if (info.containsKey("event11")) {// event11 -- video starts
				evento = "event11";
				s.eVar8 = info.get("event11");
			}

			if (info.containsKey("event12")) {// event12 -- video ends
				evento = "event12";
				s.eVar8 = info.get("event12");

			}
			s.events = evento;
			s.eVar1 = s.prop1;
			s.eVar17 = s.prop17;
			s.eVar13 = s.prop32;
			s.linkTrackVars = "events,eVar8,channel,pageName,prop1,prop2,prop17,prop32,eVar1,eVar17,eVar13";

		}
		// if (evento != null && !evento.equalsIgnoreCase("")) {
		// Log.d("OMMNITURE", "CHANNEL--> " + s.channel + " PROP1--> "
		// + s.prop1 + " PROP2--> " + s.prop2 + " PROP3--> " + s.prop3
		// + " PROP4--> " + s.prop4 + " PAGENAME--> " + s.pageName);
		//
		// s.trackLink(null, "o", evento);
		// }
	}

	private AppMeasurement getAppMeasurement(Application app, String section,
			String subsection, String subsubsection, String tema, String tipo,
			String detailPage) {

		AppMeasurement s;

		s = new AppMeasurement(app); // Activity.getApplication

		/* Specify the Report Suite ID(s) to track here */
		s.account = Omniture.TRACKING_RSID;

		s.debugTracking = true;

		// Clear Vars
		s.clearVars();

		// You may add or alter any code config here
		s.usePlugins = false;

		// Set Unique ID
		s.visitorID = "";
		s.currencyCode = "EUR";

		// Turn on and configure debugging here
		s.debugTracking = true;

		// WARNING: Changing any of the below variables will cause drastic
		// changes
		// to how your visitor data is collected. Changes should only be made
		// when instructed to do so by your account manager.
		s.trackingServer = Omniture.TRACKING_SERVER;

		s.channel = section;

		s.pageName = ""; // Descripcion de la p‡gina que se visualiza
		s.pageURL = "";

		if (section != null && !section.equalsIgnoreCase("")) {
			s.pageName = section;
			if (subsection != null && !subsection.equalsIgnoreCase("")) {
				s.prop1 = section + ">" + subsection;
				s.pageName = section + ":" + subsection;
				if (subsubsection != null
						&& !subsubsection.equalsIgnoreCase("")) {
					s.prop2 = section + ">" + subsection + ">" + subsubsection;
					s.pageName = section + ":" + subsection + ":"
							+ subsubsection;
					if (tema != null) {
						s.pageName = section + ":" + subsection + ":"
								+ subsubsection + ":" + tema;
					}
				}
			}
			if (detailPage != null && !detailPage.equalsIgnoreCase("")) {
				s.pageName = s.pageName + ":" + detailPage;
			}
		}

		if (tipo != null)
			s.prop3 = tipo;

		if (tema != null) {
			s.prop4 = section + ">" + tema;
			if (subsection != null && !subsection.equalsIgnoreCase("")) {
				s.prop4 = section + ">" + subsection + ">" + tema;
				if (subsubsection != null
						&& !subsubsection.equalsIgnoreCase("")) {
					s.prop4 = section + ">" + subsection + ">" + subsubsection
							+ ">" + tema;
				}
			}
		}

		s.prop14 = Omniture.PAIS; // Pais del Medio
		s.prop15 = Omniture.PAIS; // Zona
		s.prop16 = ""; // KeyWord Interna, (para bœsquedas)
		s.prop17 = Omniture.CANAL; // Canal
		s.prop18 = Omniture.ORGANIZACION; // Organizacion
		s.prop19 = Omniture.PRODUCTO; // Producto
		s.prop20 = Omniture.DOMINIO; // Dominio
		s.prop23 = version; // Version app (coger de la app)

		s.prop30 = Omniture.UNIDAD_NEGOCIO; // Unidad de Negocio
		s.prop31 = Omniture.TEMATICA; // Tematica
		s.prop32 = Omniture.NOMBRE_APP; // Nombre app

		if (section != null && !section.equalsIgnoreCase("")) {
			if (subsection != null && !subsection.equalsIgnoreCase("")) {
				if (subsubsection != null
						&& !subsubsection.equalsIgnoreCase("")) {
					if (tema != null && !tema.equalsIgnoreCase("")) {
						s.hier1 = s.prop18 + ">" + s.prop17 + ">" + s.prop19
								+ ">" + s.prop32 + ">" + s.prop23 + ">"
								+ section + ">" + subsection + ">"
								+ subsubsection + ">" + tema;
					} else {
						s.hier1 = s.prop18 + ">" + s.prop17 + ">" + s.prop19
								+ ">" + s.prop32 + ">" + s.prop23 + ">"
								+ section + ">" + subsection + ">"
								+ subsubsection;
					}
				} else {
					s.hier1 = s.prop18 + ">" + s.prop17 + ">" + s.prop19 + ">"
							+ s.prop32 + ">" + s.prop23 + ">" + section + ">"
							+ subsection;
				}
			} else {
				s.hier1 = s.prop18 + ">" + s.prop17 + ">" + s.prop19 + ">"
						+ s.prop32 + ">" + s.prop23 + ">" + section;
			}
			if (detailPage != null && !detailPage.equalsIgnoreCase("")) {
				s.hier1 = s.hier1 + ">" + detailPage;
			}
		} else {
			s.hier1 = s.prop18 + ">" + s.prop17 + ">" + s.prop19 + ">"
					+ s.prop32 + ">" + s.prop23;
		}

		s.eVar3 = s.pageName; // Copy pageName to eVar
		s.eVar4 = "D=ch"; // Copy channel to eVar

		if (s.prop1 != null)/* prop1=ch+seccion to eVar1 */
			s.eVar1 = "D=c1";
		if (s.prop2 != null) /* prop2=prop1+subseccion to eVar6 */
			s.eVar6 = "D=c2";
		if (s.prop3 != null)
			s.eVar7 = "D=c3"; // Tipo de Contenido
		if (s.prop4 != null)/* Tema */
			s.eVar10 = "D=c4";

		if (s.prop8 != null) /* Set day Lunes, Martes... */
			s.eVar48 = "D=c8";

		if (s.prop9 != null) /* Tipo de Dia */
			s.eVar9 = "D=c9";

		if (s.prop11 != null)/* Subtema */
			s.eVar11 = "D=c11";

		if (s.prop12 != null)/* Subtema1 */
			s.eVar12 = "D=c11";

		if (s.prop14 != null)
			s.eVar14 = "D=c14"; // Pais del Medio
		if (s.prop15 != null)/* Zona */
			s.eVar15 = "D=c15";
		if (s.prop16 != null)/* searched word */
			s.eVar16 = "D=c16";
		if (s.prop17 != null)
			s.eVar17 = "D=c17"; // Canal
		if (s.prop18 != null)
			s.eVar18 = "D=c18"; // Organization
		if (s.prop19 != null)
			s.eVar19 = "D=c19"; // Producto
		if (s.prop20 != null)
			s.eVar20 = "D=c20"; // Dominio
		if (s.prop22 != null)// Formato (variable solo para radio) - valores
								// posibles: convencional o musical
			s.eVar24 = "D=c22";
		if (s.prop23 != null)
			s.eVar23 = "D=c23"; // Version app
		if (s.prop28 != null)/* Registrado / No Registrado */
			s.eVar22 = "D=c28";
		if (s.prop30 != null)
			s.eVar30 = "D=c30"; // Unidad de negocio
		if (s.prop31 != null)
			s.eVar62 = "D=c31"; // Tematica
		if (s.prop32 != null)
			s.eVar13 = "D=c32"; // Nombre app
		if (s.prop39 != null)
			s.eVar39 = "D=c39"; // Titulo p‡gina
		if (s.prop40 != null)// * Autor del articulo */
			s.eVar40 = "D=c40";
		if (s.prop44 != null) /* Fecha del articulo */
			s.eVar44 = "D=c44";
		if (s.prop56 != null) /* Xref: Identificador unico de articulo */
			s.eVar56 = "D=c56";

		if (s.prop60 != null) /* Días desde ultima visita */
			s.eVar60 = "D=c60";

		/* Send page view event */

		return s;
	}
	// public void sendStaticsOnState(String section, String subsection,
	// String subsubsection, String tema, String tipo, String detailPage,
	// HashMap<String, String> info) {
	//
	// Log.d("OMMNITURE", "sendStaticsOnState--> " + section + " :: "
	// + subsection + " :: " + subsubsection + " :: " + tema + " :: "
	// + tipo + " :: " + detailPage);
	//
	// Config.setContext(mContext);
	// Config.setDebugLogging(true);
	// Map<String, Object> map = getFieldsOnMap(section, subsection,
	// subsubsection, tema, tipo, detailPage, info);
	//
	// Analytics.trackState(detailPage, map);
	//
	// }
	//
	// public void sendStaticsOnAction(String section, String subsection,
	// String subsubsection, String tema, String tipo, String detailPage,
	// HashMap<String, String> info) {
	//
	// Log.d("OMMNITURE", "sendStaticsOnAction--> " + section + " :: "
	// + subsection + " :: " + subsubsection + " :: " + tema + " :: "
	// + tipo + " :: " + detailPage);
	//
	// Config.setContext(mContext);
	// Config.setDebugLogging(true);
	// Map<String, Object> map = getFieldsOnMap(section, subsection,
	// subsubsection, tema, tipo, detailPage, info);
	//
	// Analytics.trackAction(detailPage, map);
	//
	// }
	//
	// private Map<String, Object> getFieldsOnMap(String section,
	// String subsection, String subsubsection, String tema, String tipo,
	// String detailPage, HashMap<String, String> info) {
	// Map<String, Object> map = new HashMap<String, Object>();
	//
	// String appState = "";
	//
	// if (section != null && !section.equalsIgnoreCase("")) {
	// appState += section;
	// if (subsection != null && !subsection.equalsIgnoreCase("")) {
	// appState += ":" + subsection;
	// if (subsubsection != null
	// && !subsubsection.equalsIgnoreCase("")) {
	// appState += ":" + subsubsection;
	// if (tema != null && !tema.equalsIgnoreCase("")) {
	// appState += ":" + tema;
	// }
	// }
	// }
	// }
	// if (detailPage != null) {
	// appState += ":" + detailPage;
	// }
	//
	// map.put("appState", appState);
	// map.put("channel", section);
	//
	// if (subsection != null && !subsection.equalsIgnoreCase("")) {
	// map.put("prop1", section + ">" + subsection);
	// if (subsubsection != null && !subsubsection.equalsIgnoreCase("")) {
	// map.put("prop2", section + ">" + subsection + ">"
	// + subsubsection);
	// }
	// }
	//
	// map.put("prop3", tipo);
	//
	// if (tema != null)
	// map.put("prop4", tema);
	//
	// map.put("prop14", Ommniture.PAIS);
	// map.put("prop17", Ommniture.CANAL);
	// map.put("prop18", Ommniture.ORGANIZACION);
	// map.put("prop19", Ommniture.PRODUCTO);
	// map.put("prop20", Ommniture.DOMINIO);
	// map.put("prop23", version);
	// map.put("prop30", Ommniture.UNIDAD_NEGOCIO);
	// map.put("prop31", Ommniture.TEMATICA);
	// map.put("prop32", Ommniture.NOMBRE_APP);
	//
	// if (info != null && info.size() > 0) {
	// map.put("prop39", info.get("titulo"));
	// // measurement.setProp(39, info.get("titulo"));
	// if (info.get("titulo") != null) {
	// appState += info.get("titulo");
	// map.put("appState", appState);
	// }
	// map.put("prop40", info.get("autor"));
	// map.put("prop56", info.get("idArticulo"));
	// }
	//
	// map.put("hier1", "");
	// String appStatePageDetail = null;
	// String[] appStateSplits = appState.split(":");
	// //
	// if (appStateSplits != null) {
	// appStatePageDetail = appStateSplits[appStateSplits.length - 1]
	// .toLowerCase();
	// } else {
	// appStatePageDetail = appState.toLowerCase();
	// }
	//
	// if (section != null && !section.equalsIgnoreCase("")) {
	// if (subsection != null && !subsection.equalsIgnoreCase("")) {
	// if (subsubsection != null
	// && !subsubsection.equalsIgnoreCase("")) {
	// if (tema != null && !tema.equalsIgnoreCase("")) {
	// map.put("hier1",
	// map.get("prop18") + ">" + map.get("prop17")
	// + ">" + map.get("prop19") + ">"
	// + map.get("prop32") + ">"
	// + map.get("prop23") + ">" + section
	// + ">" + subsection + ">"
	// + subsubsection + ">" + tema + ">"
	// + appStatePageDetail);
	// } else {
	// map.put("hier1",
	// map.get("prop18") + ">" + map.get("prop17")
	// + ">" + map.get("prop19") + ">"
	// + map.get("prop32") + ">"
	// + map.get("prop23") + ">" + section
	// + ">" + subsection + ">"
	// + subsubsection + ">"
	// + appStatePageDetail);
	// }
	// } else {
	// map.put("hier1",
	// map.get("prop18") + ">" + map.get("prop17") + ">"
	// + map.get("prop19") + ">"
	// + map.get("prop32") + ">"
	// + map.get("prop23") + ">" + section + ">"
	// + subsection + ">" + appStatePageDetail);
	// }
	// } else {
	// map.put("hier1", map.get("prop18") + ">" + map.get("prop17")
	// + ">" + map.get("prop19") + ">" + map.get("prop32")
	// + ">" + map.get("prop23") + ">" + section + ">"
	// + appStatePageDetail);
	// }
	// }
	//
	// map.put("evar3", map.get("appState"));
	// map.put("evar4", map.get("channel"));
	//
	// if (map.get("prop1") != null)/* prop1=ch+seccion to eVar1 */
	// map.put("evar1", "D=c1");
	// if (map.get("prop2") != null) /* prop2=prop1+subseccion to eVar6 */
	// map.put("evar6", "D=c2");
	//
	// if (map.get("prop3") != null)/* Tipo de Contenido */
	// map.put("evar7", "D=c3");
	//
	// if (map.get("prop4") != null)/* Tema */
	// map.put("evar10", "D=c4");
	//
	// if (map.get("prop8") != null) /* Set day Lunes, Martes... */
	// map.put("evar48", "D=c8");
	//
	// if (map.get("prop9") != null) /* Tipo de Dia */
	// map.put("evar66", "D=c9");
	//
	// if (map.get("prop11") != null)/* Subtema */
	// map.put("evar11", "D=c11");
	//
	// if (map.get("prop12") != null)/* Subtema1 */
	// map.put("evar12", "D=c11");
	//
	// if (map.get("prop14") != null)/* Pais del Medio */
	// map.put("evar14", "D=c14");
	//
	// if (map.get("prop15") != null)/* Zona */
	// map.put("evar15", "D=c15");
	// if (map.get("prop16") != null)/* searched word */
	// map.put("evar16", "D=c16");
	//
	// if (map.get("prop17") != null)/* Canal */
	// map.put("evar17", "D=c17");
	//
	// if (map.get("prop18") != null)/* Organization */
	// map.put("evar18", "D=c18");
	//
	// if (map.get("prop19") != null) /* Producto */
	// map.put("evar19", "D=c19");
	//
	// if (map.get("prop20") != null)/* Dominio */
	// map.put("evar20", "D=c20");
	//
	// if (map.get("prop22") != null)/*
	// * Formato (variable solo para radio) -
	// * valores posibles: convencional o musical
	// */
	// map.put("eva24", "D=c22");
	//
	// if (map.get("prop23") != null) /* Version app */
	// map.put("eva23", "D=c23");
	//
	// if (map.get("prop24") != null) /* ID Usuario Registrado */
	// map.put("eva43", "D=c34");
	//
	// if (map.get("prop28") != null)/* Registrado / No Registrado */
	// map.put("eva22", "D=c28");
	//
	// if (map.get("prop30") != null)/* Bussines Unit */
	// map.put("evar30", "D=c30");
	//
	// if (map.get("prop31") != null) {/* Tematica */
	// map.put("evar62", "D=c31");
	// map.put("evar31", map.get("prop31"));
	// }
	// if (map.get("prop32") != null)/* Nombre APP */
	// map.put("evar13", "D=c32");
	//
	// if (map.get("prop39") != null) /* Titulo página */
	// map.put("evar39", "D=c39");
	//
	// if (map.get("prop40") != null)// * Autor del articulo */
	// map.put("evar40", "D=c40");
	//
	// if (map.get("prop44") != null) /* Fecha del articulo */
	// map.put("evar44", "D=c44");
	//
	// if (map.get("prop56") != null) /* Xref: Identificador unico de articulo
	// */
	// map.put("evar56", "D=c56");
	//
	// if (map.get("prop60") != null) /* Días desde ultima visita */
	// map.put("evar60", "D=c60");
	//
	// map.remove("appState");
	// map.put("events", "event2");
	// map.put("currency", "EUR");
	// return map;
	// }

}
