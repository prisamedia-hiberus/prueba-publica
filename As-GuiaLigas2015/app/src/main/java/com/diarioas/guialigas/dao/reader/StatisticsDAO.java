/**
 * 
 */
package com.diarioas.guialigas.dao.reader;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.FileUtils;
import com.omniture.AppMeasurement;

import java.util.HashMap;

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

//		Log.d("OMMNITURE", "sendStaticsOnState--> " + section + " :: "
//				+ subsection + " :: " + subsubsection + " :: " + tema + " :: "
//				+ tipo + " :: " + detailPage);
		AppMeasurement s = getAppMeasurement(app, section, subsection,
				subsubsection, tema, tipo, detailPage);

		s.events = "event2";
		s.currencyCode = "EUR";

		Log.d("OMMNITURE", "CHANNEL--> " + s.channel + " PROP1--> " + s.prop1
				+ " PROP2--> " + s.prop2 + " PROP3--> " + s.prop3
				+ " PROP4--> " + s.prop4 + " PAGENAME--> " + s.pageName);
		s.track();
	}

	public void sendStatisticsShare(Application app, String contentTitle, String section, String subsection, String subsubsection) {

		AppMeasurement s = getAppMeasurement(app, section, subsection,
				subsubsection, null, null, contentTitle);

		s.linkTrackEvents = s.events;
		String evento = "event69";
		s.events = evento;

		s.eVar1 = s.prop1;
		s.eVar3 = s.pageName;
		s.eVar13 = s.prop32;
		s.eVar17 = s.prop17;
		s.eVar20 = s.prop20;
		s.eVar30 = s.prop30;
		s.eVar39 = contentTitle;
		s.eVar69 = ""; //We don't have access to the selected social network in Android

		s.linkTrackVars = "events,channel,eVar3,eVar4,eVar13,eVar17,eVar20,eVar30,eVar39,eVar69";

		if (evento != null && !evento.equalsIgnoreCase("")) {
			//Log.d("OMMNITURE", "CHANNEL--> " + s.channel + " PROP1--> "
			//		+ s.prop1 + " PROP2--> " + s.prop2 + " PROP3--> " + s.prop3
			//		+ " PROP4--> " + s.prop4 + " PAGENAME--> " + s.pageName);

			s.trackLink(null, "o", evento);
		}
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
			s.eVar3 = s.pageName;
			s.eVar13 = s.prop32;
			s.eVar17 = s.prop17;
			s.eVar20 = s.prop20;
			s.eVar30 = s.prop30;
			s.linkTrackVars = "events,channel,eVar3,eVar8,eVar13,eVar17,eVar20,eVar30";

		}
		if (evento != null && !evento.equalsIgnoreCase("")) {
			Log.d("OMMNITURE", "CHANNEL--> " + s.channel
					+ " PAGENAME--> " + s.pageName
					+ " eVar1--> "+ s.eVar1 + " eVar3--> " + s.eVar3 + " eVar13--> " + s.eVar13
					+ " eVar17--> "+ s.eVar17 + " eVar20--> " + s.eVar20 + " eVar30--> " + s.eVar30
					);

			s.trackLink(null, "o", evento);
		}
	}

	private AppMeasurement getAppMeasurement(Application app, String section,
			String subsection, String subsubsection, String tema, String tipo,
			String detailPage) {

		AppMeasurement s;

		s = new AppMeasurement(app); // Activity.getApplication

		/* Specify the Report Suite ID(s) to track here */
		s.account = FileUtils.readOmnitureProperties(mContext, "TRACKING_RSID");
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
		s.trackingServer = FileUtils.readOmnitureProperties(mContext, "TRACKING_SERVER");

		s.channel = section;

		s.pageName = ""; // Descripcion de la p�gina que se visualiza
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

		s.prop14 = FileUtils.readOmnitureProperties(mContext, "PAIS"); // Pais del Medio
		s.prop15 = FileUtils.readOmnitureProperties(mContext, "PAIS"); // Zona
		s.prop16 = ""; // KeyWord Interna, (para b�squedas)
		s.prop17 = FileUtils.readOmnitureProperties(mContext, "CANAL"); // Canal
		s.prop18 = FileUtils.readOmnitureProperties(mContext, "ORGANIZACION");; // Organizacion
		s.prop19 = FileUtils.readOmnitureProperties(mContext, "PRODUCTO"); // Producto
		s.prop20 = FileUtils.readOmnitureProperties(mContext, "DOMINIO"); // Dominio
		s.prop23 = version; // Version app (coger de la app)

		s.prop30 = FileUtils.readOmnitureProperties(mContext, "UNIDAD_NEGOCIO"); // Unidad de Negocio
		s.prop31 = FileUtils.readOmnitureProperties(mContext, "TEMATICA"); // Tematica
		s.prop32 = FileUtils.readOmnitureProperties(mContext, "NOMBRE_APP"); // Nombre app

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
			s.eVar39 = "D=c39"; // Titulo p�gina
		if (s.prop40 != null)// * Autor del articulo */
			s.eVar40 = "D=c40";
		if (s.prop44 != null) /* Fecha del articulo */
			s.eVar44 = "D=c44";
		if (s.prop56 != null) /* Xref: Identificador unico de articulo */
			s.eVar56 = "D=c56";

		if (s.prop60 != null) /* D�as desde ultima visita */
			s.eVar60 = "D=c60";

		/* Send page view event */

		return s;
	}
	
}
