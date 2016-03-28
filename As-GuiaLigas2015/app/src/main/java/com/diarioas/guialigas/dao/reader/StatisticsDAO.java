/**
 * 
 */
package com.diarioas.guialigas.dao.reader;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;


import com.adobe.adms.measurement.ADMS_Measurement;
import com.diarioas.guialigas.BuildConfig;
import com.diarioas.guialigas.utils.Defines;
import com.diarioas.guialigas.utils.FileUtils;


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
        ADMS_Measurement s = getAppMeasurement(app, section, subsection,
				subsubsection, tema, tipo, detailPage);

		s.setEvents("event2");
		s.setCurrencyCode("EUR");

		Log.d("OMMNITURE", "CHANNEL--> " + s.getChannel() + " PROP1--> " + s.getProp(1)
				+ " PROP2--> " + s.getProp(2) + " PROP3--> " + s.getProp(3)
				+ " PROP4--> " + s.getProp(4) + " PAGENAME--> " + s.getAppState());
		s.track();
	}

	public void sendStatisticsShare(Application app, String contentTitle, String section, String subsection, String subsubsection) {

        ADMS_Measurement s = getAppMeasurement(app, section, subsection,
				subsubsection, null, null, contentTitle);

		s.setLinkTrackEvents(s.getEvents());
		String evento = "event69";
		s.setEvents(evento);

		s.setEvar(1, s.getProp(1));
		s.setEvar(3, s.getAppState());
		s.setEvar(13, s.getProp(32));
		s.setEvar(17, s.getProp(17));
		s.setEvar(20, s.getProp(20));
		s.setEvar(30, s.getProp(30));
		s.setEvar(39, contentTitle);
		s.setEvar(69, ""); //We don't have access to the selected social network in Android

		s.setLinkTrackVars("events,channel,eVar3,eVar4,eVar13,eVar17,eVar20,eVar30,eVar39,eVar69");

		if (evento != null && !evento.equalsIgnoreCase("")) {
			//Log.d("OMMNITURE", "CHANNEL--> " + s.channel + " PROP1--> "
			//		+ s.prop1 + " PROP2--> " + s.prop2 + " PROP3--> " + s.prop3
			//		+ " PROP4--> " + s.prop4 + " PAGENAME--> " + s.pageName);

			s.setLinkTrackEvents(evento);
		}
	}


	public void sendStatisticsAction(Application app, String section,
			String subsection, String subsubsection, String tema, String tipo,
			String detailPage, HashMap<String, String> info) {

        ADMS_Measurement s = getAppMeasurement(app, section, subsection,
				subsubsection, tema, tipo, detailPage);

		s.setLinkTrackEvents(s.getEvents());

		String evento = "";

		if (info != null && info.size() > 0) {
			if (info.containsKey("event11")) {// event11 -- video starts
				evento = "event11";
				s.setEvar(8, info.get("event11"));
			}

			if (info.containsKey("event12")) {// event12 -- video ends
				evento = "event12";
				s.setEvar(8, info.get("event12"));

			}
			s.setEvents(evento);
			s.setEvar(1, s.getProp(1));
			s.setEvar(3, s.getAppState());
			s.setEvar(13, s.getProp(32));
			s.setEvar(17, s.getProp(17));
			s.setEvar(20, s.getProp(20));
			s.setEvar(30, s.getProp(30));
			s.setLinkTrackVars("events,channel,eVar3,eVar8,eVar13,eVar17,eVar20,eVar30");

		}
		if (evento != null && !evento.equalsIgnoreCase("")) {
			Log.d("OMMNITURE", "CHANNEL--> " + s.getChannel()
					+ " PAGENAME--> " + s.getAppState()
					+ " eVar1--> "+ s.getEvar(1) + " eVar3--> " + s.getEvar(3) + " eVar13--> " + s.getEvar(13)
					+ " eVar17--> "+ s.getEvar(17) + " eVar20--> " + s.getEvar(20) + " eVar30--> " + s.getEvar(30)
					);

            s.setLinkTrackEvents(evento);
		}
	}

	private ADMS_Measurement getAppMeasurement(Application app, String section,
                                               String subsection, String subsubsection, String tema, String tipo,
                                               String detailPage) {

        ADMS_Measurement s;

        s = ADMS_Measurement.sharedInstance(mContext);

        if (BuildConfig.DEBUG) {
            s.setReportSuiteIDs(Defines.OMNITURE_CONSTANTS.TRACKING_RSID_DEV);
        } else {
            s.setReportSuiteIDs(FileUtils.readOmnitureProperties(mContext, "TRACKING_RSID"));
        }

		s.setDebugLogging(true);

		// Clear Vars
		s.clearVars();

		// Set Unique ID
		s.setVisitorID("");
		s.setCurrencyCode("EUR");

		// WARNING: Changing any of the below variables will cause drastic
		// changes
		// to how your visitor data is collected. Changes should only be made
		// when instructed to do so by your account manager.
		s.setTrackingServer(FileUtils.readOmnitureProperties(mContext, "TRACKING_SERVER"));

		s.setChannel(section);


        if (section != null) {
            s.setAppState(section);
            if (subsection != null) {
                s.setProp(1, String.format("%s>%s", section, subsection));
                s.setAppState(String.format("%s:%s", section, subsection));
                if (subsubsection != null) {
                    s.setProp(2, String.format("%s>%s>%s", section, subsection, subsubsection));
                    s.setAppState(String.format("%s:%s:%s", section, subsection, subsubsection));
                }
            }
            if (detailPage != null) {
                s.setAppState(String.format("%s:%s", s.getAppState(), detailPage));
            }


        }

		if (tipo != null)
			s.setProp(3, tipo);

		if (tema != null) {
			s.setProp(4, section + ">" + tema);
			if (subsection != null && !subsection.equalsIgnoreCase("")) {
				s.setProp(4, section + ">" + subsection + ">" + tema);
				if (subsubsection != null
						&& !subsubsection.equalsIgnoreCase("")) {
					s.setProp(4, section + ">" + subsection + ">" + subsubsection
                            + ">" + tema);
				}
			}
		}

		s.setProp(14, FileUtils.readOmnitureProperties(mContext, "PAIS")); // Pais del Medio
		s.setProp(15, FileUtils.readOmnitureProperties(mContext, "PAIS")); // Zona
		s.setProp(16, ""); // KeyWord Interna, (para b�squedas)
		s.setProp(17, FileUtils.readOmnitureProperties(mContext, "CANAL")); // Canal
		s.setProp(18, FileUtils.readOmnitureProperties(mContext, "ORGANIZACION")); // Organizacion
		s.setProp(19, FileUtils.readOmnitureProperties(mContext, "PRODUCTO")); // Producto
		s.setProp(20, FileUtils.readOmnitureProperties(mContext, "DOMINIO")); // Dominio
		s.setProp(23, version); // Version app (coger de la app)

		s.setProp(30, FileUtils.readOmnitureProperties(mContext, "UNIDAD_NEGOCIO")); // Unidad de Negocio
		s.setProp(31, FileUtils.readOmnitureProperties(mContext, "TEMATICA")); // Tematica
		s.setProp(32, FileUtils.readOmnitureProperties(mContext, "NOMBRE_APP")); // Nombre app

		if (section != null && !section.equalsIgnoreCase("")) {
			if (subsection != null && !subsection.equalsIgnoreCase("")) {
				if (subsubsection != null
						&& !subsubsection.equalsIgnoreCase("")) {
					if (tema != null && !tema.equalsIgnoreCase("")) {
						s.setHier(1, s.getProp(18) + ">" + s.getProp(17) + ">" + s.getProp(19)
                                + ">" + s.getProp(32) + ">" + s.getProp(23) + ">"
                                + section + ">" + subsection + ">"
                                + subsubsection + ">" + tema);
					} else {
						s.setHier(1, s.getProp(18) + ">" + s.getProp(17) + ">" + s.getProp(19)
                                + ">" + s.getProp(32) + ">" + s.getProp(23) + ">"
                                + section + ">" + subsection + ">"
                                + subsubsection);
					}
				} else {
					s.setHier(1, s.getProp(18) + ">" + s.getProp(17) + ">" + s.getProp(19) + ">"
                            + s.getProp(32) + ">" + s.getProp(23) + ">" + section + ">"
                            + subsection);
				}
			} else {
				s.setHier(1, s.getProp(18) + ">" + s.getProp(17) + ">" + s.getProp(19) + ">"
                        + s.getProp(32) + ">" + s.getProp(32) + ">" + section);
			}
			if (detailPage != null && !detailPage.equalsIgnoreCase("")) {
				s.setHier(1, s.getHier(1) + ">" + detailPage);
			}
		} else {
			s.setHier(1, s.getProp(18) + ">" + s.getProp(17) + ">" + s.getProp(19) + ">"
                    + s.getProp(32) + ">" + s.getProp(23));
		}

		s.setEvar(3, s.getAppState()); // Copy pageName to eVar
		s.setEvar(4, "D=ch"); // Copy channel to eVar

		if (s.getProp(1) != null)/* prop1=ch+seccion to eVar1 */
			s.setEvar(1, "D=c1");
		if (s.getProp(2) != null) /* prop2=prop1+subseccion to eVar6 */
			s.setEvar(6, "D=c2");
		if (s.getProp(3) != null)
			s.setEvar(7, "D=c3"); // Tipo de Contenido
		if (s.getProp(4) != null)/* Tema */
			s.setEvar(10, "D=c4");

		if (s.getProp(8) != null) /* Set day Lunes, Martes... */
			s.setEvar(48, "D=c8");

		if (s.getProp(9) != null) /* Tipo de Dia */
			s.setEvar(9, "D=c9");

		if (s.getProp(11) != null)/* Subtema */
			s.setEvar(11, "D=c11");

		if (s.getProp(12) != null)/* Subtema1 */
			s.setEvar(12, "D=c11");

		if (s.getProp(14) != null)
			s.setEvar(14, "D=c14"); // Pais del Medio
		if (s.getProp(15) != null)/* Zona */
			s.setEvar(15, "D=c15");
		if (s.getProp(16) != null)/* searched word */
			s.setEvar(16, "D=c16");
		if (s.getProp(17) != null)
			s.setEvar(17, "D=c17"); // Canal
		if (s.getProp(18) != null)
			s.setEvar(18, "D=c18"); // Organization
		if (s.getProp(19) != null)
			s.setEvar(19, "D=c19"); // Producto
		if (s.getProp(20) != null)
			s.setEvar(20,"D=c20"); // Dominio
		if (s.getProp(22) != null)// Formato (variable solo para radio) - valores
								// posibles: convencional o musical
			s.setEvar(24, "D=c22");
		if (s.getProp(23) != null)
			s.setEvar(23, "D=c23"); // Version app
		if (s.getProp(18) != null)/* Registrado / No Registrado */
			s.setEvar(22, "D=c28");
		if (s.getProp(30) != null)
			s.setEvar(30, "D=c30"); // Unidad de negocio
		if (s.getProp(31) != null)
			s.setEvar(62, "D=c31"); // Tematica
		if (s.getProp(32) != null)
			s.setEvar(13, "D=c32"); // Nombre app
		if (s.getProp(39) != null)
			s.setEvar(39, "D=c39"); // Titulo p�gina
		if (s.getProp(40) != null)// * Autor del articulo */
			s.setEvar(40, "D=c40");
		if (s.getProp(44) != null) /* Fecha del articulo */
			s.setEvar(44, "D=c44");
		if (s.getProp(56) != null) /* Xref: Identificador unico de articulo */
			s.setEvar(56, "D=c56");

		if (s.getProp(60)!= null) /* D�as desde ultima visita */
			s.setEvar(60, "D=c60");

		/* Send page view event */

		return s;
	}
	
}
