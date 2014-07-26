package com.diarioas.guiamundial.utils;

public class Defines {

	public static interface ReturnRequestCodes {

		public static int HOME = 1;
		public static int SELECTION = 1;
		public static int RESULT = 2;
		public static int LOADING = 3;
		public static int INTERSTITIAL = 4;
		public static int SHARE_CHOOSER = 5;
		public static int COMPARATORPLAYER_RETURN_OK = 800;
		public static int COMPARATORPLAYER_LEFT = 550;
		public static int COMPARATORPLAYER_RIGHT = 600;
		public static int COMPARATOR_NO_PLAYER = 650;
		public static int GALLERY_BACK = 7;

		public static int PUBLI_BACK = 700;

	}

	public static interface Prefix {
		public static final String PREFIX_IMAGE = "imageURLPrefix";
		public static final String PREFIX_DATA = "dataURLPrefix";
		public static final String PREFIX_DATA_RSS = "dataServiceURLPrefix";
		public static final String PREFIX_VIDEO = "videoURLPrefix";
		public static final String PREFIX_RESULT = "resultsURLPrefix";

	}

	public static interface ReturnDataDatabases {

		public static String DB_SETTINGS_FILE_NAME = "conf-guia1.1.plist";
		public static String DATABASE_NAME = "2014_mundial.db";

	}

	public static interface URL_DATA {
		public static final String URL_SITE = ".as.com";
	}

	public static interface ReturnSharedPreferences {
		public static final String SP_NAME = "GUIAMUNDIALPreferences";
		public static final String SP_COOKIE_NAME_AVISOPC = "avisopc";
		public static final String SP_NEWSDATE = "newsDate";
	}

	public static final int NUM_MAX_TVS = 4;

	public static String URL_REMOTE_CONFIG_FILE = "http://as.com/apps/guia_del_mundial/config/v1.x/v1.1/conf-guia.xml";

	// public static String URL_REMOTE_CONFIG_FILE =
	// "https://googledrive.com/host/0B57LzJ1pIeo2eFhrZmRLemtqZGc/conf-guia-mundial.plist";

	public static interface MatchStats {
		public static final String POS_GOLES_ENCAJADOS = "GolesEnc";
		public static final String POS_GOLES = "Goles";
		public static final String POS_PARTIDOS_JUGADOS = "jugados";
		public static final String POS_TARJETASA = "tarjetasA";
		public static final String POS_TARJETASR = "tarjetasR";
		public static final String POS_PARTIDOS_GANADOS = "ganados";
		public static final String POS_PARTIDOS_EMPATADOS = "empatados";
		public static final String POS_PARTIDOS_PERDIDOS = "perdidos";
		public static final String POS_PARADAS = "paradas";
		public static final String POS_ASISTENCIAS = "asistencias";
		public static final String POS_MINUTOS = "minutos";
	}

	public static interface ShieldName {

		public static String GRID = "grid";
		public static String CALENDAR = "calendar";
		public static String DETAIL = "detail";
		public static String PREFIX_FLAG = "flag_";
		public static String PREFIX_SHIELD = "escudo_";
	}

	public static interface StaffCharge {

		public static String PRESIDENT = "President";
		public static String MANAGER = "Manager";
		public static String MISTER = "Mister";
		public static String STAR = "Star";
	}

	public static interface RequestTimes {
		// public static final long TIMER_REMOTEFILE_UPDATE = 6 * 60 * 60 *
		// 1000;// 6horas
		public static final long TIMER_REMOTEFILE_UPDATE = 1 * 30 * 1000;// 30
																			// segundos
		public static final long TIMER_CALENDAR_UPDATE = 5 * 60 * 1000;// 5mins
		// public static final long TIMER_CALENDAR_UPDATE = 1 * 30 * 1000; //
		// 30seg
		// public static final long TIMER_CARRUSEL_UPDATE_NO_ACTIVE = 15 * 1000;
		// public static final long TIMER_CARRUSEL_UPDATE_ACTIVE = 10 * 1000;
		public static final long TIMER_CARRUSEL_UPDATE_NO_ACTIVE = 5 * 60 * 1000; // 5mins
		public static final long TIMER_CARRUSEL_UPDATE_ACTIVE = 30 * 1000; //
		// 30sg

	}

	public static interface MatchStates {

		public static final String FORPLAY = "prev";
		public static final String PLAYING = "during";
		public static final String PLAYED = "post";

	}

	public static interface MatchEvents {
		public static final String MATCHEVENT_GOL = "Gol";
		public static final String MATCHEVENT_CAMBIO = "Cambio";
		public static final String MATCHEVENT_REMATE = "Remate";
		public static final String MATCHEVENT_TARJETA_AMARILLA = "Tarjeta Amarilla";
		public static final String MATCHEVENT_TARJETA_ROJA = "Tarjeta Roja";

	}

	public static interface CarruselDetail {
		public static final String CARRUSEL_RESUMEN = "Resumen";
		public static final String CARRUSEL_DIRECTO = "En Directo";
		public static final String CARRUSEL_PLANTILLA = "Plantilla";
		public static final String CARRUSEL_ESTADISTICAS = "Estadísticas";
		// public static final String CARRUSEL_REMATES = "Remates";
		public static final String CARRUSEL_PICAS = "Picas";
	}

	public static interface CarruselEventos {
		public static final String CARRUSEL_EVENTO_CAMBIO = "cambio";
		public static final String CARRUSEL_EVENTO_GOL = "gol";
		public static final String CARRUSEL_EVENTO_TARJETA_AMARILLA = "amarilla";
		public static final String CARRUSEL_EVENTO_TARJETA_ROJA = "roja";
	}

	public static final String NAME_CACHE_THUMBS = "thumbs";
	public static final String GAME_SYSTEM_DEFAULT = "4-4-2";

	public static interface Demarcation {

		public static final String DEMARCATION = "demarcacion";
		public static final String GOALKEEPER = "portero";
		public static final String DEFENDER = "defensa";
		public static final String MIDFIELD = "centrocampista";
		public static final String SCORER = "delantero";

	}

	public static interface DateFormat {

		public static final String PULL_FORMAT = "dd/MMM/yy HH:mm";
		public static final String SQL_FORMAT = "yyyy-MM-dd HH:mm:ss";
		public static final String VIDEOPARSER_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";
		public static final String CARRUSEL_FORMAT = "EEEE dd/MM/yyyy - HH:mm";

		public static final String DAY_FORMAT = "dd/MM/yyyy";
		public static final String HOUR_FORMAT = "HH:mm";
	}

	public static interface SECTIONS {
		public static final String CALENDAR = "calendar";
		public static final String CARROUSEL = "carrousel";
		public static final String CLASIFICATION = "clasification";
		public static final String STADIUMS = "stadiums";
		public static final String PALMARES = "palmares";
		public static final String TEAMS = "grid";
		public static final String COMPARATOR = "comparatorPlayers";
		public static final String SEARCHER = "searcher";
		public static final String NEWS = "news";
		public static final String PHOTOS = "photos";
		public static final String VIDEOS = "videos";
		public static final String LINK = "triviaAS";

		public static final String TEAMS_ORDER_GROUP = "group";
		public static final String TEAMS_ORDER_ALPHABETIC = "alpha";
		public static final String LINK_VIEW_INSIDE = "web_view";
		public static final String LINK_VIEW_OUTSIDE = "external_view";
	}

	public static interface LEGEND_TYPE {
		public static final int LEGEND_TYPE_1 = 1;
		public static final int LEGEND_TYPE_2 = 2;
		public static final int LEGEND_TYPE_3 = 3;
	}

	public static interface STADIUM_IMAGE_TYPE {

		public static final String TYPE_STADIUM = "STADIUM";
		public static final String TYPE_CITY = "CITY";

	}

	public static interface MEDIA_TYPE {

		public static final String TYPE_PHOTO = "PHOTO";
		public static final String TYPE_PHOTO_BIG = "BIG";
		public static final String TYPE_PHOTO_NORMAL = "NORMAL";
		public static final String TYPE_PHOTO_THUMB = "THUMB";
		public static final String TYPE_VIDEO = "VIDEO";

	}

	public static interface ReturnComparator {

		public static final String POSESION = "POSESIÓN";
		public static final String REMATES_TOTALES = "REMATES TOTALES";
		public static final String REMATES_FUERA = "FUERA";
		public static final String REMATES_POSTE = "AL POSTE";
		public static final String REMATES_PORTERIA = "A PORTERÍA";
		public static final String REMATES_OTROS = "OTROS";
		public static final String ASISTENCIAS = "ASISTENCIAS";
		public static final String PARADAS = "PARADAS";
		public static final String GOLES = "GOLES";
		public static final String TARJ_ROJAS = "TARJETAS ROJAS";
		public static final String TARJ_AMAR = "TARJETAS AMARILLAS";
		public static final String FALTAS_RECIBIDAS = "FALTAS RECIBIDAS";
		public static final String FALTAS_COMETIDAS = "FALTAS COMETIDAS";
		public static final String BALONES_PERDIDOS = "BALONES PERDIDOS";
		public static final String BALONES_RECUPERADOS = "BALONES RECUPERADOS";
		public static final String FUERA_JUEGO = "FUERA DE JUEGO";
		public static final String PENALTIES = "PENALTIES";
		public static final String INTERV_PORTERO = "INTERVENCIONES DEL PORTERO";
	}

	/******************************* Libraries ***************************************************/

	public static interface ComscoreCode {
		public static final String CUSTOMERID = "8671776";
		public static final String PUBLISEHSECRETCODE = "070a8dd76668b3fdcc2a7026bd3c9e12";
	}

	public static interface Omniture {
		public static final String TRACKING_RSID = "prisacommovilidadas,prisacommovilidadprisacom";
		public static final String TRACKING_SERVER = "prisacom.112.2o7.net";

		public static final String PAIS = "españa";
		public static final String CANAL = "aplicacion_android";
		public static final String ORGANIZACION = "prisa";
		public static final String PRODUCTO = "as";
		public static final String DOMINIO = "as.com";
		public static final String UNIDAD_NEGOCIO = "noticias";
		public static final String TEMATICA = "deportes";
		public static final String NOMBRE_APP = "guia del mundial android";

		public static final String TYPE_PORTADA = "portada";
		public static final String TYPE_ARTICLE = "articulo";
		public static final String TYPE_GALLERY = "fotogaleria";

		public static final String SECTION_PORTADA = "portada";
		public static final String SECTION_COMPARATOR = "comparador";
		public static final String SECTION_SEARCHER = "buscador";
		public static final String SECTION_CALENDAR = "calendario";
		public static final String SECTION_CARROUSEL = "carrusel";
		public static final String SECTION_PALMARES = "palmares";
		public static final String SECTION_SEDES = "sedes";
		public static final String SECTION_CLASIFICATION = "clasificacion";
		public static final String SECTION_VIDEOS = "videos";
		public static final String SECTION_NEWS = "noticias";

		public static final String SUBSECTION_PORTADA = "portada";
		public static final String SUBSECTION_SELECTION = "seleccion";
		public static final String SUBSECTION_PLANTILLA = "plantilla";
		public static final String SUBSECTION_IDEALTEAM = "once ideal";
		public static final String SUBSECTION_MUNDIALES = "mundiales";
		public static final String SUBSECTION_COUNTRIES = "paises";
		public static final String SUBSECTION_RESULT = "resultado";
		public static final String SUBSECTION_DAY = "jornada";
		public static final String SUBSECTION_GALLERY = "galeria";
		public static final String SUBSECTION_NEWSDETAIL = "detalle noticia";

		public static final String SUBSUBSECTION_FICHA = "ficha";
		public static final String SUBSUBSECTION_PLAYERS = "jugadores";
		public static final String SUBSUBSECTION_GALLERY = "fotogaleria";

		public static final String TEMA_INFORMATION = "informacion";
		public static final String TEMA_PALMARES = "palmares";
		public static final String TEMA_TRAYECTORIA = "trayectoria";
		public static final String TEMA_PREVIA = "previa";
		public static final String TEMA_PLANTILLA = "plantilla";
		public static final String TEMA_DIRECTO = "directo";
		public static final String TEMA_FINAL = "final";
		public static final String TEMA_STATS = "estadisticas";
		public static final String TEMA_SPADES = "picas";

		public static final String DETAILPAGE_PORTADA = "portada";
		public static final String DETAILPAGE_INFORMACION = "informacion";
		public static final String DETAILPAGE_FOTOGALERIA = "fotogaleria";
		public static final String DETAILPAGE_DETALLE = "detalle";
		public static final String SECTION_PHOTOS = "fotos";
		public static final String SECTION_LINK = "triviaAS";

	}

	public static interface NativeAds {

		public static final String AD_KEY = "7811748/as_mundial_app/android/";
		public static final String AD_PORTADA = "portada";
		public static final String AD_CALENDAR = "calendario";
		public static final String AD_COUNTRY = "pais/";
		public static final String AD_PLAYER = "jugador";
		public static final String AD_CLASIFICATION = "clasificacion";
		public static final String AD_CARROUSEL = "carrusel";
		public static final String AD_PALMARES = "palmares";
		public static final String AD_STADIUMS = "sedes";
		public static final String AD_NEWS = "noticias";
		public static final String AD_VIDEOS = "videos";
		public static final String AD_PHOTOS = "fotos";
		public static final String AD_DETAIL = "detalle";
		public static final String AD_SEARCHER = "buscador";
		public static final String AD_COMPARATOR = "comparador";
		public static final String AD_LINK = "triviaAS";
	}
}
