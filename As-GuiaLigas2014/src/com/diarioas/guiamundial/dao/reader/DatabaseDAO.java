package com.diarioas.guiamundial.dao.reader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.diarioas.guiamundial.dao.model.carrusel.PlayerOnField;
import com.diarioas.guiamundial.dao.model.competition.Competition;
import com.diarioas.guiamundial.dao.model.competition.Group;
import com.diarioas.guiamundial.dao.model.general.ClasificacionSection;
import com.diarioas.guiamundial.dao.model.general.Section;
import com.diarioas.guiamundial.dao.model.general.TeamSection;
import com.diarioas.guiamundial.dao.model.news.MediaItem;
import com.diarioas.guiamundial.dao.model.news.NewsItem;
import com.diarioas.guiamundial.dao.model.news.PhotoMediaItem;
import com.diarioas.guiamundial.dao.model.news.VideoMediaItem;
import com.diarioas.guiamundial.dao.model.player.ItemStats;
import com.diarioas.guiamundial.dao.model.player.ItemTrayectoria;
import com.diarioas.guiamundial.dao.model.player.Player;
import com.diarioas.guiamundial.dao.model.player.PlayerStats;
import com.diarioas.guiamundial.dao.model.player.TituloPlayer;
import com.diarioas.guiamundial.dao.model.player.Trayectoria;
import com.diarioas.guiamundial.dao.model.stadium.Stadium;
import com.diarioas.guiamundial.dao.model.team.Article;
import com.diarioas.guiamundial.dao.model.team.PalmaresLabel;
import com.diarioas.guiamundial.dao.model.team.Staff;
import com.diarioas.guiamundial.dao.model.team.Star;
import com.diarioas.guiamundial.dao.model.team.Team;
import com.diarioas.guiamundial.dao.model.team.TeamStats;
import com.diarioas.guiamundial.dao.model.team.TituloTeam;
import com.diarioas.guiamundial.utils.Defines;
import com.diarioas.guiamundial.utils.Defines.DATABASE;
import com.diarioas.guiamundial.utils.Defines.DateFormat;
import com.diarioas.guiamundial.utils.Defines.MEDIA_TYPE;
import com.diarioas.guiamundial.utils.Defines.SECTIONS;
import com.diarioas.guiamundial.utils.Defines.STADIUM_IMAGE_TYPE;
import com.diarioas.guiamundial.utils.Defines.SplashTypes;

public class DatabaseDAO extends SQLiteOpenHelper {

	private static final String TAG = "DATABASEDAO";

	private static DatabaseDAO sInstance = null;

	private static File DATABASE_FILE;

	private static final String TABLE_SPLASH = "splash";
	// SPLASH Table Column name
	private static final String KEY_SPLASH_AVAILABLE = "available";
	private static final String KEY_SPLASH_URL = "image";
	private static final String KEY_SPLASH_SECONDS = "seconds";
	private static final String KEY_SPLASH_TYPE = "type";
	

	private static final String TABLE_CLASIFICATION_LABELS = "clasificationLabels";
	private static final String KEY_CLASIFICATION_LABELS_CODE = "code";
	private static final String KEY_CLASIFICATION_LABELS_COLOR = "color";

	private static final String TABLE_PREFIX = "prefix";
	// PREFIX Table Column name
	private static final String KEY_PREFIX_NAME = "name";
	private static final String KEY_PREFIX_URL = "url";

	private static final String TABLE_STATUS = "status";
	// STATUS Table Column name
	private static final String KEY_STATUS_CODE = "code";
	private static final String KEY_STATUS_STATE = "status";

	private static final String TABLE_GAMEPLAY = "gameplay";
	// GAMEPLAY Table Column name
	private static final String KEY_GAMEPLAY_CODE = "code";
	private static final String KEY_GAMEPLAY_DESCRIPTION = "description";

	private static final String TABLE_SPADES = "spades";
	// SPADES Table Column name
	private static final String KEY_SPADES_CODE = "code";
	private static final String KEY_SPADES_VALUE = "value";

	private static final String TABLE_COMPETITION = "Competition";
	// COMPETITION Table Column names
	private static final String KEY_COMPETITION_ID = "id";
	private static final String KEY_COMPETITION_NAME = "name";
	private static final String KEY_COMPETITION_IMAGE = "image";
	private static final String KEY_COMPETITION_ORDER = "orden";
	private static final String KEY_COMPETITION_URL = "url";
	private static final String KEY_COMPETITION_FECMOD = "fecModificacion";

	private static final String TABLE_COMPETITION_SECTION = "CompetitionSections";
	// COMPETITIONSECTIONS Table Column names
	private static final String KEY_COMPETITION_SECTION_COMPETITION_ID = "competitionId";
	private static final String KEY_COMPETITION_SECTION_TYPE = "sectionType";
	private static final String KEY_COMPETITION_SECTION_VIEWTYPE = "viewType";

	private static final String KEY_COMPETITION_SECTION_URL = "url";
	private static final String KEY_COMPETITION_SECTION_NAME = "name";
	private static final String KEY_COMPETITION_SECTION_ACTIVE = "active";
	private static final String KEY_COMPETITION_SECTION_DEFAULT = "start";
	private static final String KEY_COMPETITION_SECTION_ORDER = "orderField";
	private static final String KEY_COMPETITION_SECTION_TYPEORDER = "orderType";

	private static final String TABLE_TEAM = "Team";
	// TEAM Table Column names
	private static final String KEY_TEAM_ID = "id";
	private static final String KEY_TEAM_NAME = "name";
	private static final String KEY_TEAM_SHORTNAME = "shortName";
	private static final String KEY_TEAM_URL = "url";
	private static final String KEY_TEAM_URL_INFO = "urlInfo";
	private static final String KEY_TEAM_URLFICHA = "urlFicha";
	private static final String KEY_TEAM_URLTAG = "urlTag";
	private static final String KEY_TEAM_WEB = "web";
	private static final String KEY_TEAM_FUNDATION = "fundation";
	private static final String KEY_TEAM_LYPOSITION = "lastYearPosition";
	private static final String KEY_TEAM_SHIELD1 = "shield1";
	private static final String KEY_TEAM_SHIELD2 = "shield2";
	private static final String KEY_TEAM_SHIELD3 = "shield3";
	private static final String KEY_TEAM_HISTORY = "history";
	private static final String KEY_TEAM_FEDERATION_FOUNDATION = "federationFoundation";
	private static final String KEY_TEAM_FEDERATION_AFFILIATION = "federationAffiliation";
	private static final String KEY_TEAM_NUMPLAYERS = "NumPlayers";
	private static final String KEY_TEAM_NUMCLUBS = "NumClubs";
	private static final String KEY_TEAM_NUMREFEREES = "NumReferees";

	private static final String KEY_TEAM_SHIRT1 = "shirt1";
	private static final String KEY_TEAM_SHIRT2 = "shirt2";
	private static final String KEY_TEAM_SHIRT3 = "shirt3";
	private static final String KEY_TEAM_FECMOD = "fecModificacion";
	private static final String KEY_TEAM_GAMESYSTEM = "gameSystem";
	private static final String KEY_TEAM_STATIC_INFO = "static";
	// Article Table Column names
	private static final String KEY_TEAM_ARTICLE_AUTHOR = "author";
	private static final String KEY_TEAM_ARTICLE_CHARGE = "charge";
	private static final String KEY_TEAM_ARTICLE_TITLE = "title";
	private static final String KEY_TEAM_ARTICLE_SUBTITLE = "subtitle";
	private static final String KEY_TEAM_ARTICLE_BODY = "body";
	private static final String KEY_TEAM_ARTICLE_VIDEO = "video";
	private static final String KEY_TEAM_ARTICLE_VIDEOIMAGE = "videoImage";

	private static final String TABLE_COMPETITION_TEAM = "Competition_Team";
	// COMPETITION_TEAM Table Column names
	private static final String KEY_COMPETITION_TEAM_COMPETITION_ID = "competition_id";
	private static final String KEY_COMPETITION_TEAM_TEAM_ID = "team_id";
	private static final String KEY_COMPETITION_TEAM_GROUP_NAME = "groupName";
	private static final String KEY_COMPETITION_TEAM_GROUP_ORDER = "groupOrder";

	private static final String TABLE_STAFF = "Staff";
	// STAFF Table Columns names
	private static final String KEY_STAFF_ID = "team_id";
	private static final String KEY_STAFF_CHARGE = "charge";
	private static final String KEY_STAFF_NAME = "name";
	private static final String KEY_STAFF_BORN = "born";
	private static final String KEY_STAFF_CONTRACT = "contract";
	private static final String KEY_STAFF_HISTORY = "history";
	private static final String KEY_STAFF_PHOTO = "photo";
	private static final String KEY_STAFF_POSITION = "position";
	private static final String KEY_STAFF_NUMINTERNATIONAL = "international";
	private static final String KEY_STAFF_AGE = "age";
	private static final String KEY_STAFF_STATURE = "stature";
	private static final String KEY_STAFF_WEIGHT = "weight";
	private static final String KEY_STAFF_CLUBNAME = "clubName";
	private static final String KEY_STAFF_CLUBSHIELD = "clubShield";
	private static final String KEY_STAFF_URL = "url";
	private static final String KEY_STAFF_PLAYERID = "playerId";

	private static final String TABLE_PLAYER = "Player";
	// PLANTILLA Table Column name
	private static final String KEY_PLAYER_TEAM_ID = "team_id";
	private static final String KEY_PLAYER_TEAM_NAME = "team_name";
	private static final String KEY_PLAYER_ID = "id";
	private static final String KEY_PLAYER_NAME = "name";
	private static final String KEY_PLAYER_SHORTNAME = "shortName";
	private static final String KEY_PLAYER_URL = "URL";
	private static final String KEY_PLAYER_URL_FOTO = "FOTO";
	private static final String KEY_PLAYER_URL_FICHA = "urlFicha";
	private static final String KEY_PLAYER_URL_TAG = "urlTag";
	private static final String KEY_PLAYER_DORSAL = "dorsal";
	private static final String KEY_PLAYER_DEMARCACION = "demarcacion";
	private static final String KEY_PLAYER_POSITION = "position";
	private static final String KEY_PLAYER_WEIGHT = "weight";
	private static final String KEY_PLAYER_HEIGHT = "height";
	private static final String KEY_PLAYER_NACIONALITY = "nacionality";
	private static final String KEY_PLAYER_AGE = "age";
	private static final String KEY_PLAYER_DATEBORN = "dateborn";
	private static final String KEY_PLAYER_FECMOD = "fecModificacion";
	private static final String KEY_PLAYER_COMPETICIONES = "competiciones";

	private static final String TABLE_PLAYER_TRAYECTORIA = "Trayectoria";
	// TRAYECTORIA Table Collumn name
	private static final String KEY_PLAYER_TRAYECTORIA_ID = "playerId";
	private static final String KEY_PLAYER_TRAYECTORIA_YEAR = "year";
	private static final String KEY_PLAYER_TRAYECTORIA_DESCRIPCION = "descripcion";
	private static final String KEY_PLAYER_TRAYECTORIA_TEAM = "team";
	private static final String KEY_PLAYER_TRAYECTORIA_PARTIDOS = "numPartidos";
	private static final String KEY_PLAYER_TRAYECTORIA_GOLES = "numGoles";

	private static final String TABLE_PLAYER_PALMARES = "PalmaresPlayer";
	// PALMARES Table Column name
	private static final String KEY_PLAYER_PALMARES_ID = "playerId";
	private static final String KEY_PLAYER_PALMARES_NAME = "name";
	private static final String KEY_PLAYER_PALMARES_TEAM = "team";
	private static final String KEY_PLAYER_PALMARES_YEAR = "year";

	private static final String TABLE_PLAYER_ESTADISTICAS = "EstaditicasPlayer";
	// PLAYER_ESTADISTICAS Table Collumn name
	private static final String KEY_PLAYER_ESTADISTICAS_ID = "playerId";
	private static final String KEY_PLAYER_ESTADISTICAS_YEAR = "year";
	private static final String KEY_PLAYER_ESTADISTICAS_COMP = "competicion";
	private static final String KEY_PLAYER_ESTADISTICAS_TEAM = "team";
	private static final String KEY_PLAYER_ESTADISTICAS_GOLES_ENC = "golEn";
	private static final String KEY_PLAYER_ESTADISTICAS_GOLES = "gol";
	private static final String KEY_PLAYER_ESTADISTICAS_PARTIDOS = "partidos";
	private static final String KEY_PLAYER_ESTADISTICAS_MINUTOS = "min";
	private static final String KEY_PLAYER_ESTADISTICAS_TARJETASAMARILLAS = "tarjetasAm";
	private static final String KEY_PLAYER_ESTADISTICAS_TARJETASROJAS = "tarjetasRo";
	private static final String KEY_PLAYER_ESTADISTICAS_PARADAS = "paradas";
	private static final String KEY_PLAYER_ESTADISTICAS_ASISTENCIAS = "asistencias";

	private static final String TABLE_TEAM_PALMARES = "PalmaresTeam";
	// PALMARES Table Columns name
	private static final String KEY_TEAM_PALMARES_ID = "team_id";
	private static final String KEY_TEAM_PALMARES_NAME = "name";
	private static final String KEY_TEAM_PALMARES_YEAR = "year";

	private static final String TABLE_TEAM_ESTADISTICAS = "EstaditicasTeam";
	// ESTADISTICAS DE EQUIPO Table Columns name
	private static final String KEY_TEAM_ESTADISTICAS_ID = "team_id";
	private static final String KEY_TEAM_ESTADISTICAS_YEAR = "year";
	private static final String KEY_TEAM_ESTADISTICAS_COMPETITION = "competition";
	private static final String KEY_TEAM_ESTADISTICAS_GOLES = "goles";
	private static final String KEY_TEAM_ESTADISTICAS_PARTIDOSJUGADOS = "p_jugados";
	private static final String KEY_TEAM_ESTADISTICAS_PARTIDOSGANADOS = "p_ganados";
	private static final String KEY_TEAM_ESTADISTICAS_PARTIDOSEMPATADOS = "p_empatados";
	private static final String KEY_TEAM_ESTADISTICAS_PARTIDOSPERDIDOS = "p_perdidos";
	private static final String KEY_TEAM_ESTADISTICAS_TARJETASAMARILLAS = "t_amarillas";
	private static final String KEY_TEAM_ESTADISTICAS_TARJETASROJAS = "t_rojas";

	private static final String TABLE_PALMARES_LABELS_Y = "palmaresCompetitionY";
	// PALMARES LABELS Y Position
	private static final String KEY_PALMARES_LABELS_Y_CODE = "code";
	private static final String KEY_PALMARES_LABELS_Y_LABEL = "label";
	private static final String KEY_PALMARES_LABELS_Y_COMPETITION = "competitionId";

	private static final String TABLE_PALMARES_LABELS_X = "palmaresCompetitionX";
	// PALMARES LABELS Y Position
	private static final String KEY_PALMARES_LABELS_X_CODE = "code";
	private static final String KEY_PALMARES_LABELS_X_YEAR = "year";
	private static final String KEY_PALMARES_LABELS_X_COUNTRY = "country";
	private static final String KEY_PALMARES_LABELS_X_COMPETITION = "competitionId";
	private static final String KEY_PALMARES_LABELS_X_LEGEND = "legend";

	private static final String TABLE_PALMARES_LABELS_L = "palmaresCompetitionLegend";
	// PALMARES LABELS Y Position
	private static final String KEY_PALMARES_LABELS_L_CODE = "code";
	private static final String KEY_PALMARES_LABELS_L_LABEL = "label";
	private static final String KEY_PALMARES_LABELS_L_COMPETITION = "competitionId";

	private static final String TABLE_HISTORCAL_PALMARES_TEAM = "Competition_His_Palmares";
	// PALMARES LABELS Y Position
	private static final String KEY_HISTORCAL_PALMARES_TEAM_ID = "teamId";
	private static final String KEY_HISTORCAL_PALMARES_TEAM_COMPETITION = "competitionId";
	private static final String KEY_HISTORCAL_PALMARES_TEAM_ORDER = "orderField";
	private static final String KEY_HISTORCAL_PALMARES_TEAM_POSITION = "position";

	private static final String TABLE_STADIUM = "Stadium";
	// STADIUMS
	private static final String KEY_STADIUM_ID = "id";
	private static final String KEY_STADIUM_STADIUM_NAME = "stadium_name";
	private static final String KEY_STADIUM_STADIUM_MAP = "stadium_map";
	private static final String KEY_STADIUM_STADIUM_HISTORY = "stadium_history";
	private static final String KEY_STADIUM_STADIUM_CAPACITY = "stadium_capacity";
	private static final String KEY_STADIUM_STADIUM_YEAR = "stadium_year";
	private static final String KEY_STADIUM_URL_PHOTO = "urlPhoto";
	private static final String KEY_STADIUM_URL = "url";
	private static final String KEY_STADIUM_CITY_NAME = "city_name";
	private static final String KEY_STADIUM_CITY_STATE = "city_state";
	private static final String KEY_STADIUM_CITY_POPULATION = "city_population";
	private static final String KEY_STADIUM_CITY_ALTITUDE = "city_altitude";
	private static final String KEY_STADIUM_CITY_HISTORY = "city_history";
	private static final String KEY_STADIUM_CITY_TRANSPORT = "city_transport";
	private static final String KEY_STADIUM_CITY_ECONOMY = "city_economy";
	private static final String KEY_STADIUM_CITY_TOURISM = "city_tourism";

	private static final String TABLE_COMPETITION_STADIUM = "Competition_Stadium";
	// COMPETITION_STADIUM Table Column names
	private static final String KEY_COMPETITION_STADIUM_COMPETITION_ID = "competition_id";
	private static final String KEY_COMPETITION_STADIUM_STADIUM_ID = "stadium_id";

	private static final String TABLE_STADIUM_IMAGES = "Stadium_images";
	// STADIUMS_IMAGES
	private static final String KEY_STADIUM_IMAGES_ID = "stadium_id";
	private static final String KEY_STADIUM_IMAGES_URL = "url";
	private static final String KEY_STADIUM_IMAGES_TYPE = "type";

	private static final String TABLE_TEAM_IDEALPLAYERS = "team_idealPlayers";
	// TEAM_IDEALPLAYERS Table Column names
	private static final String KEY_TEAM_IDEALPLAYERS_TEAM_ID = "teamId";
	private static final String KEY_TEAM_IDEALPLAYERS_PLAYER_ID = "playerId";
	private static final String KEY_TEAM_IDEALPLAYERS_PLAYER_POSITION = "position";
	private static final String KEY_TEAM_IDEALPLAYERS_PLAYER_DORSAL = "dorsal";
	private static final String KEY_TEAM_IDEALPLAYERS_PLAYER_NAME = "name";
	private static final String KEY_TEAM_IDEALPLAYERS_PLAYER_URLPHOTO = "urlPhoto";
	private static final String KEY_TEAM_IDEALPLAYERS_PLAYER_URLFICHA = "ficha_player";

	private static final String TABLE_NEWS = "News";
	// NEWS Table Column names
	private static final String KEY_NEWS_IDCOMPETITION = "competitionId";
	private static final String KEY_NEWS_TITLE = "title";
	private static final String KEY_NEWS_TITLENEWS = "titlenews";
	private static final String KEY_NEWS_PRETITLE = "pretitle";
	private static final String KEY_NEWS_SUBTITLE = "subtitle";;
	private static final String KEY_NEWS_SECTION = "section";
	private static final String KEY_NEWS_TITLESECTION = "titlesection";
	private static final String KEY_NEWS_PRETITLESECTION = "pretitlesection";
	private static final String KEY_NEWS_TITLEFRONT = "titlefront";
	private static final String KEY_NEWS_PRETITLEFRONT = "pretitlefront";
	private static final String KEY_NEWS_URLDETAIL = "urldetail";
	private static final String KEY_NEWS_BODY = "body";
	private static final String KEY_NEWS_PORTAL = "portal";
	private static final String KEY_NEWS_ABSTRACT = "abstract";
	private static final String KEY_NEWS_TAGS = "tags";
	private static final String KEY_NEWS_TIPOLOGY = "tipology";
	private static final String KEY_NEWS_AUTHOR = "author";
	private static final String KEY_NEWS_URLCOMMENT = "urlcomment";
	private static final String KEY_NEWS_DATE = "date";

	private static final String TABLE_MEDIA_NEWS = "MediaNews";
	// MEDIANEWS Table Column names
	private static final String KEY_NEWS_MEDIA_IDCOMPETITION = "competitionId";
	private static final String KEY_NEWS_MEDIA_TITLE = "title";
	private static final String KEY_NEWS_MEDIA_DATE = "date";
	private static final String KEY_NEWS_MEDIA_TYPE = "type";
	private static final String KEY_NEWS_MEDIA_TYPE_PHOTO = "typePhoto";
	private static final String KEY_NEWS_MEDIA_URL_PHOTO = "urlPhoto";
	private static final String KEY_NEWS_MEDIA_URL_VIDEO = "urlVideo";
	private static final String KEY_NEWS_MEDIA_WIDTH = "width";
	private static final String KEY_NEWS_MEDIA_HEIGHT = "height";
	/******************************************************************/

	private final SimpleDateFormat sdf = new SimpleDateFormat(
			DateFormat.SQL_FORMAT, Locale.getDefault());
	private final Context mContext;
	private boolean mInvalidDatabaseFile = false;
	private boolean mIsUpgraded = false;
	private int mOpenConnections = 0;

	public static DatabaseDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new DatabaseDAO(ctx);
		}

		return sInstance;
	}

	public DatabaseDAO(Context context) {
		super(context, DATABASE.DB_NAME, null, DATABASE.VERSION);

		this.mContext = context;
		SQLiteDatabase db = null;
		try {
			db = getReadableDatabase();
			if (db != null) {
				db.close();
			}

			DATABASE_FILE = context.getDatabasePath(DATABASE.DB_NAME);

			if (mInvalidDatabaseFile) {
				copyDatabase();
			}
			if (mIsUpgraded) {
				doUpgrade();
			}

		} catch (SQLiteException e) {
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
	}

	private void doUpgrade() {
		// implement the database upgrade here.
	}

	private void copyDatabase() {
		AssetManager assetManager = mContext.getResources().getAssets();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(DATABASE.DB_NAME);
			out = new FileOutputStream(DATABASE_FILE);
			byte[] buffer = new byte[1024];
			int read = 0;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
		} catch (IOException e) {
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		setDatabaseVersion();
		mInvalidDatabaseFile = false;
	}

	private void setDatabaseVersion() {
		SQLiteDatabase db = null;
		try {
			db = SQLiteDatabase.openDatabase(DATABASE_FILE.getAbsolutePath(),
					null, SQLiteDatabase.OPEN_READWRITE);
			db.execSQL("PRAGMA user_version = " + DATABASE.VERSION);
		} catch (SQLiteException e) {
		} finally {
			if (db != null && db.isOpen()) {
				db.close();
			}
		}
	}

	/*********************** CUSTOM REQUEST METHODS *****************************************/

	public void executeSQL(String sql) {
		try {
			if (sql == null || sql.length() == 0)
				return;

			String[] separatedSQL = sql.split(";");
			SQLiteDatabase db = null;
			db = this.getWritableDatabase();

			for (int i = 0; i < separatedSQL.length; i++) {
				if ((separatedSQL[i] != null) && (separatedSQL[i].length() > 0)) {
					db.execSQL(separatedSQL[i]);
				}
			}

		} catch (SQLiteException e) {
			e.printStackTrace();
		}

	}

	/*********** DATABASE OVERRIDE METHODS ************/

	@Override
	public synchronized void close() {
		mOpenConnections--;
		if (mOpenConnections == 0) {
			super.close();
		}
	}

	@Override
	public synchronized void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		// increment the number of users of the database connection.
		mOpenConnections++;
		if (!db.isReadOnly()) {
			// Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		mInvalidDatabaseFile = true;
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		mInvalidDatabaseFile = true;
		mIsUpgraded = true;
	}

	/****************************************************************************/
	/****************************************************************************/

	/****************************************** SPLASH *****************************************************/
	public void updateStaticSplash(HashMap<?, ?> splash) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		try {
			db.beginTransaction();
			insertSplash(db, (Boolean) splash.get("available"),
					(String) splash.get("imageURL"),
					(Integer) splash.get("seconds"));
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	/**
	 * @param db
	 * @param available
	 * @param imageUrl
	 * @param seconds
	 * @throws Exception
	 */
	private void insertSplash(SQLiteDatabase db, boolean available,
			String imageUrl, int seconds) throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_SPLASH_AVAILABLE, available);
		values.put(KEY_SPLASH_URL, imageUrl);
		values.put(KEY_SPLASH_SECONDS, seconds);
		values.put(KEY_SPLASH_TYPE, SplashTypes.TYPE_SPLASH);
		try {
			String whereClause = KEY_SPLASH_TYPE + " like ? ";
			db.delete(TABLE_SPLASH, whereClause, new String[] { SplashTypes.TYPE_SPLASH });
			db.insert(TABLE_SPLASH, null, values);
		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db splash : " + e.getMessage());
		}
	}

	public ArrayList<Object> getSplashInfo() {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_SPLASH + " where "
				+ KEY_SPLASH_AVAILABLE + " = 1 and "+KEY_SPLASH_TYPE +" like '"+SplashTypes.TYPE_SPLASH+"'";

		Cursor cursor = null;
		ArrayList<Object> splash = new ArrayList<Object>();

		try {
			cursor = db.rawQuery(selectQuery, null);

			// Article article;
			if (cursor.moveToFirst()) {
				splash.add(cursor.getString(cursor
						.getColumnIndex(KEY_SPLASH_URL)));
				splash.add(cursor.getInt(cursor
						.getColumnIndex(KEY_SPLASH_SECONDS)));
			}

		} catch (Exception e) {
			Log.e(TAG,
					"Se ha producido un error al obtener el Splash: "
							+ e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return splash;
	}

	/****************************************** SPLASH *****************************************************/

	/****************************************** HEADER *****************************************************/
	public void updateStaticHeader(HashMap<?, ?> header) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		try {
			db.beginTransaction();
			insertHeader(db, (Boolean) header.get("available"),
					(String) header.get("imageURL"));
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	/**
	 * @param db
	 * @param available
	 * @param imageUrl
	 * @param seconds
	 * @throws Exception
	 */
	private void insertHeader(SQLiteDatabase db, boolean available,
			String imageUrl) throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_SPLASH_AVAILABLE, available);
		values.put(KEY_SPLASH_URL, imageUrl);
		values.put(KEY_SPLASH_TYPE, SplashTypes.TYPE_HEADER);
		try {
			String whereClause = KEY_SPLASH_TYPE + " like ? ";
			db.delete(TABLE_SPLASH, whereClause, new String[] { SplashTypes.TYPE_HEADER });
			db.insert(TABLE_SPLASH, null, values);
		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db Splash header: " + e.getMessage());
		}
	}

	public String getHeaderInfo() {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_SPLASH + " where "
				+ KEY_SPLASH_AVAILABLE + " = 1 and "+KEY_SPLASH_TYPE +" like '"+SplashTypes.TYPE_HEADER+"'";

		Cursor cursor = null;
		String header = null;

		try {
			cursor = db.rawQuery(selectQuery, null);

			// Article article;
			if (cursor.moveToFirst()) {
				header = cursor.getString(cursor.getColumnIndex(KEY_SPLASH_URL));
			}

		} catch (Exception e) {
			Log.e(TAG,
					"Se ha producido un error al obtener el Splash: "
							+ e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return header;
	}

	/****************************************** HEADER *****************************************************/	

	/****************************************** PREFIX *****************************************************/
	public void updateStaticPrefix(HashMap<String, String> prefixes) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		try {
			db.beginTransaction();
			String key;
			for (Iterator<String> iterator = prefixes.keySet().iterator(); iterator
					.hasNext();) {
				key = iterator.next();
				insertPrefixes(db, key, prefixes.get(key));
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * @param db
	 * @param string
	 * @param object
	 * @throws Exception
	 */
	private void insertPrefixes(SQLiteDatabase db, String name, String url)
			throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_PREFIX_URL, url);
		values.put(KEY_PREFIX_NAME, name);

		try {
			String whereClause = KEY_PREFIX_NAME + " like ? ";
			db.delete(TABLE_PREFIX, whereClause, new String[] { name });
			db.insert(TABLE_PREFIX, null, values);

		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} catch (Exception e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		}

	}

	/**
	 * @param prefix
	 * @return
	 */
	public String getPrefix(String prefix) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_PREFIX + " where "
				+ KEY_PREFIX_NAME + " like \"" + prefix + "\"";

		String url = "";

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);

			// Article article;
			if (cursor.moveToFirst()) {
				url = cursor.getString(cursor.getColumnIndex(KEY_PREFIX_URL));
			}
		} catch (Exception e) {
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return url;
	}

	/****************************************** PREFIX *****************************************************/
	/****************************************** STATUS *****************************************************/
	public void updateStaticStatus(HashMap<String, String> state) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		try {
			db.beginTransaction();
			String key;
			for (Iterator<String> iterator = state.keySet().iterator(); iterator
					.hasNext();) {
				key = iterator.next();
				insertStatus(db, key, state.get(key));
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * @param db
	 * @param key
	 * @param string
	 */
	private void insertStatus(SQLiteDatabase db, String code, String name)
			throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_STATUS_CODE, code);
		values.put(KEY_STATUS_STATE, name);

		try {
			String whereClause = KEY_STATUS_CODE + " like ? ";
			db.delete(TABLE_STATUS, whereClause, new String[] { code });
			db.insert(TABLE_STATUS, null, values);

		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} catch (Exception e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		}

	}

	/**
	 * @param Status
	 *            Code
	 * @return
	 */
	public String getStatus(int code) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_STATUS + " where "
				+ KEY_STATUS_CODE + " = " + code;

		String status = "";

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);

			// Article article;
			if (cursor.moveToFirst()) {
				status = cursor.getString(cursor
						.getColumnIndex(KEY_STATUS_STATE));
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return status;
	}

	/****************************************** STATUS *****************************************************/
	/****************************************** GAMEPLAY *****************************************************/
	public void updateGamePlay(HashMap<String, String> gameplay) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		try {
			db.beginTransaction();
			String key;
			for (Iterator<String> iterator = gameplay.keySet().iterator(); iterator
					.hasNext();) {
				key = iterator.next();
				insertGamePlay(db, key, gameplay.get(key));
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * @param db
	 * @param key
	 * @param string
	 */
	private void insertGamePlay(SQLiteDatabase db, String code,
			String description) throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_GAMEPLAY_CODE, code);
		values.put(KEY_GAMEPLAY_DESCRIPTION, description);

		try {
			String whereClause = KEY_GAMEPLAY_CODE + " like ? ";
			db.delete(TABLE_GAMEPLAY, whereClause, new String[] { code });
			db.insert(TABLE_GAMEPLAY, null, values);

		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} catch (Exception e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		}

	}

	/**
	 * @param Gameplay
	 *            code
	 * @return
	 */
	public String getGameplay(String gameSystem) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_GAMEPLAY + " where "
				+ KEY_GAMEPLAY_CODE + " = " + gameSystem;

		String description = "";

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);

			// Article article;
			if (cursor.moveToFirst()) {
				description = cursor.getString(cursor
						.getColumnIndex(KEY_GAMEPLAY_DESCRIPTION));
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return description;
	}

	/****************************************** GAMEPLAY *****************************************************/
	/****************************************** SPADES *****************************************************/
	public void updateSpades(HashMap<String, String> spades) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		try {
			db.beginTransaction();
			String key;
			for (Iterator<String> iterator = spades.keySet().iterator(); iterator
					.hasNext();) {
				key = iterator.next();
				insertSpades(db, key, spades.get(key));
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * @param db
	 * @param key
	 * @param string
	 */
	private void insertSpades(SQLiteDatabase db, String code, String value)
			throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_SPADES_CODE, code);
		values.put(KEY_SPADES_VALUE, value);

		try {
			String whereClause = KEY_SPADES_CODE + " like ? ";
			db.delete(TABLE_SPADES, whereClause, new String[] { code });
			db.insert(TABLE_SPADES, null, values);

		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} catch (Exception e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		}

	}

	/**
	 * @param Spades
	 *            code
	 * @return
	 */
	public String getSpades(int code) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_SPADES + " where "
				+ KEY_SPADES_CODE + " = " + code;

		String value = "";

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);

			// Article article;
			if (cursor.moveToFirst()) {
				value = cursor.getString(cursor
						.getColumnIndex(KEY_SPADES_VALUE));
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return value;
	}

	/****************************************** SPADES *****************************************************/
	/****************************************** CLASIFICATIONLABELS *****************************************************/
	public void updateClasificationLabels(
			HashMap<String, String> clasificationLabels) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		try {
			db.beginTransaction();
			String key;
			for (Iterator<String> iterator = clasificationLabels.keySet()
					.iterator(); iterator.hasNext();) {
				key = iterator.next();
				insertClasificationLabels(db, key, clasificationLabels.get(key));
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * @param db
	 * @param key
	 * @param string
	 */
	private void insertClasificationLabels(SQLiteDatabase db, String code,
			String value) throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_CLASIFICATION_LABELS_CODE, code);
		values.put(KEY_CLASIFICATION_LABELS_COLOR, value);

		try {
			String whereClause = KEY_CLASIFICATION_LABELS_CODE + " like ? ";
			db.delete(TABLE_CLASIFICATION_LABELS, whereClause,
					new String[] { code });
			db.insert(TABLE_CLASIFICATION_LABELS, null, values);

		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} catch (Exception e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		}

	}

	/**
	 * @param string
	 * @return
	 */
	public String getClasificationLabel(String code) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_CLASIFICATION_LABELS
				+ " where " + KEY_CLASIFICATION_LABELS_CODE + " like \"" + code
				+ "\"";

		String value = "";

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);

			// Article article;
			if (cursor.moveToFirst()) {
				value = cursor.getString(cursor
						.getColumnIndex(KEY_CLASIFICATION_LABELS_COLOR));
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
		return value;
	}

	/****************************************** CLASIFICATIONLABELS *****************************************************/
	/******************************************
	 * PALMARESLABELS
	 * 
	 * @param i
	 *****************************************************/
	public void updateMundialPalmaresPosition(
			HashMap<String, String> palmaresPosition, int competitionId) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		try {
			db.beginTransaction();
			String key;
			String whereClause = KEY_PALMARES_LABELS_Y_COMPETITION + " like ? ";
			db.delete(TABLE_PALMARES_LABELS_Y, whereClause,
					new String[] { String.valueOf(competitionId) });
			for (Iterator<String> iterator = palmaresPosition.keySet()
					.iterator(); iterator.hasNext();) {
				key = iterator.next();
				insertPalmaresLabels(db, key, palmaresPosition.get(key),
						competitionId);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	private void insertPalmaresLabels(SQLiteDatabase db, String code,
			String value, int competitionId) throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_PALMARES_LABELS_Y_CODE, code);
		values.put(KEY_PALMARES_LABELS_Y_LABEL, value);
		values.put(KEY_PALMARES_LABELS_Y_COMPETITION, competitionId);

		try {

			db.insert(TABLE_PALMARES_LABELS_Y, null, values);

		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} catch (Exception e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		}

	}

	public ArrayList<String> getPalmaresHistoricoLabels(Integer competitionId) {
		// TODO: Tendia que estar asociado a la competicion
		SQLiteDatabase db = sInstance.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_PALMARES_LABELS_Y
				+ " where " + KEY_PALMARES_LABELS_Y_COMPETITION + " = "
				+ competitionId + " order by " + KEY_PALMARES_LABELS_Y_CODE;

		Cursor cursor = null;
		ArrayList<String> palmaresLabels = new ArrayList<String>();

		try {
			cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					palmaresLabels.add(cursor.getString(cursor
							.getColumnIndex(KEY_PALMARES_LABELS_Y_LABEL)));

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Se ha producido un error al obtener el Splash: "
							+ e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return palmaresLabels;
	}

	public ArrayList<PalmaresLabel> getPalmaresHistoricoYears(int competitionId) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_PALMARES_LABELS_X
				+ " where " + KEY_PALMARES_LABELS_X_COMPETITION + " = "
				+ competitionId + " order by " + KEY_PALMARES_LABELS_X_YEAR
				+ " desc";

		Cursor cursor = null;
		try {

			ArrayList<PalmaresLabel> labels = new ArrayList<PalmaresLabel>();
			cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					PalmaresLabel plabel = new PalmaresLabel();
					plabel.setCode(cursor.getString(cursor
							.getColumnIndex((KEY_PALMARES_LABELS_X_CODE))));
					plabel.setYear(cursor.getString(cursor
							.getColumnIndex((KEY_PALMARES_LABELS_X_YEAR))));
					plabel.setCountry(cursor.getString(cursor
							.getColumnIndex((KEY_PALMARES_LABELS_X_COUNTRY))));
					plabel.setLegend(cursor.getString(cursor
							.getColumnIndex((KEY_PALMARES_LABELS_X_LEGEND))));

					labels.add(plabel);
				} while (cursor.moveToNext());
			}
			return labels;
		} catch (Exception e) {
			Log.e(TAG,
					"Se ha producido un error al obtener el Count: "
							+ e.getMessage());
			return null;
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
	}

	public void updateMundialPalmaresYear(
			HashMap<String, HashMap<String, String>> palmaresYear,
			int competitionId) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		try {
			db.beginTransaction();
			String key;

			String whereClause = KEY_PALMARES_LABELS_X_COMPETITION + " like ? ";
			db.delete(TABLE_PALMARES_LABELS_X, whereClause,
					new String[] { String.valueOf(competitionId) });

			for (Iterator<String> iterator = palmaresYear.keySet().iterator(); iterator
					.hasNext();) {
				key = iterator.next();
				if (!palmaresYear.get(key).containsKey("legend"))
					insertPalmaresYear(db, key,
							palmaresYear.get(key).get("year"), palmaresYear
									.get(key).get("country"), null,
							competitionId);
				else
					insertPalmaresYear(db, key,
							palmaresYear.get(key).get("year"), palmaresYear
									.get(key).get("country"),
							palmaresYear.get(key).get("legend"), competitionId);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	private void insertPalmaresYear(SQLiteDatabase db, String code,
			String year, String country, String legend, int competitionId)
			throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_PALMARES_LABELS_X_CODE, code);
		values.put(KEY_PALMARES_LABELS_X_YEAR, year);
		values.put(KEY_PALMARES_LABELS_X_COUNTRY, country);
		values.put(KEY_PALMARES_LABELS_X_COMPETITION, competitionId);
		if (legend != null)
			values.put(KEY_PALMARES_LABELS_X_LEGEND, legend);

		try {

			db.insert(TABLE_PALMARES_LABELS_X, null, values);

		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} catch (Exception e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		}

	}

	public void updateMundialPalmaresLegend(
			HashMap<String, String> palmaresLegend, int competitionId) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		try {
			db.beginTransaction();
			String key;

			String whereClause = KEY_PALMARES_LABELS_L_COMPETITION + " like ? ";
			db.delete(TABLE_PALMARES_LABELS_L, whereClause,
					new String[] { String.valueOf(competitionId) });
			for (Iterator<String> iterator = palmaresLegend.keySet().iterator(); iterator
					.hasNext();) {
				key = iterator.next();
				insertPalmaresLegend(db, key, palmaresLegend.get(key),
						competitionId);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	private void insertPalmaresLegend(SQLiteDatabase db, String code,
			String value, int competitionId) throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_PALMARES_LABELS_L_CODE, code);
		values.put(KEY_PALMARES_LABELS_L_LABEL, value);
		values.put(KEY_PALMARES_LABELS_L_COMPETITION, competitionId);

		try {
			db.insert(TABLE_PALMARES_LABELS_L, null, values);

		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} catch (Exception e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		}

	}

	public ArrayList<String> getPalmaresHistoricoLegend(int competitionId) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_PALMARES_LABELS_L
				+ " where " + KEY_PALMARES_LABELS_L_COMPETITION + " = "
				+ competitionId + " order by " + KEY_PALMARES_LABELS_L_CODE;

		Cursor cursor = null;
		ArrayList<String> palmaresLabels = new ArrayList<String>();

		try {
			cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					palmaresLabels.add(cursor.getString(cursor
							.getColumnIndex(KEY_PALMARES_LABELS_L_LABEL)));

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.e(TAG,
					"Se ha producido un error al obtener el TABLE_PALMARES_LABELS_L: "
							+ e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return palmaresLabels;

	}

	/****************************************** PALMARESLABELS *****************************************************/
	/****************************************** COMPETITION *****************************************************/
	/**
	 * @param competitionId
	 * @return
	 */
	public Competition getCompetition(int competitionId) {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_COMPETITION + " where "
				+ KEY_COMPETITION_ID + " = " + competitionId;

		Competition competition = null;

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);

			// Article article;
			if (cursor.moveToFirst()) {
				do {
					competition = new Competition();
					competition.setId(cursor.getInt(cursor
							.getColumnIndex((KEY_COMPETITION_ID))));
					competition.setName(cursor.getString(cursor
							.getColumnIndex((KEY_COMPETITION_NAME))));
					competition.setImage(cursor.getString(cursor
							.getColumnIndex((KEY_COMPETITION_IMAGE))));
					competition.setOrder(cursor.getInt(cursor
							.getColumnIndex((KEY_COMPETITION_ORDER))));
					competition.setFecModificacion(cursor.getFloat(cursor
							.getColumnIndex((KEY_COMPETITION_FECMOD))));

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
		return competition;
	}

	/**
	 * @param competition
	 */
	public void updateStaticCompetition(Competition competition) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		try {
			db.beginTransaction();

			String typesActivos = "";

			insertOrUpdateStaticCompetition(db, competition);
			for (Section section : competition.getSections()) {
				// if (section.getType().equalsIgnoreCase(SECTIONS.CALENDAR)
				// || section.getType().equalsIgnoreCase(
				// SECTIONS.CARROUSEL)
				// || section.getType()
				// .equalsIgnoreCase(SECTIONS.STADIUMS)
				// || section.getType()
				// .equalsIgnoreCase(SECTIONS.PALMARES)
				// || section.getType()
				// .equalsIgnoreCase(SECTIONS.SEARCHER)) {
				insertStaticSection(db, competition.getId(), section);
				typesActivos += section.getType() + ",";
				// }
			}

			deleteOldSections(db, typesActivos, competition);

			String idsActivos = "";
			Team team;

			for (Group group : ((TeamSection) competition
					.getSection(SECTIONS.TEAMS)).getGroups()) {
				for (int i = 0; i < group.getTeams().size(); i++) {
					team = group.getTeams().get(i);
					idsActivos += team.getId() + ",";
					insertOrUpdateStaticTeam(db, team, group.getName(),
							competition.getId(), i);
					insertOrUpdateStaticStaff(db, team.getId(),
							team.getTeamStaff());
				}
			}

			deleteOldTeams(db, idsActivos, competition);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e(TAG, "Error en updateStaticCompetition: " + e.getMessage());
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	/**
	 * @param db
	 * @param competition
	 * @throws Exception
	 */
	private void insertOrUpdateStaticCompetition(SQLiteDatabase db,
			Competition competition) throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_COMPETITION_ID, competition.getId());
		values.put(KEY_COMPETITION_NAME, competition.getName());
		values.put(KEY_COMPETITION_IMAGE, competition.getImage());
		values.put(KEY_COMPETITION_ORDER, competition.getOrder());
		values.put(KEY_COMPETITION_URL, competition.getUrl());
		values.put(KEY_COMPETITION_FECMOD, competition.getFecModificacion());

		Cursor cursor = null;
		try {
			String selectQuery = "Select 1 from " + TABLE_COMPETITION
					+ " where " + KEY_COMPETITION_ID + " = "
					+ competition.getId();

			cursor = db.rawQuery(selectQuery, null);
			if (cursor.getCount() > 0) {
				String whereClause = KEY_COMPETITION_ID + " = ?";
				db.update(TABLE_COMPETITION, values, whereClause,
						new String[] { String.valueOf(competition.getId()) });
			} else {
				db.insert(TABLE_COMPETITION, null, values);
			}

		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} catch (Exception e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

	}

	/**
	 * @return
	 */
	public ArrayList<Competition> getCompetitions() {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_COMPETITION
				+ " order by " + KEY_COMPETITION_ORDER;

		ArrayList<Competition> competitions = new ArrayList<Competition>();

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);

			Competition competition;
			// Article article;
			if (cursor.moveToFirst()) {
				do {
					competition = new Competition();
					competition.setId(cursor.getInt(cursor
							.getColumnIndex((KEY_COMPETITION_ID))));
					competition.setName(cursor.getString(cursor
							.getColumnIndex((KEY_COMPETITION_NAME))));
					competition.setImage(cursor.getString(cursor
							.getColumnIndex((KEY_COMPETITION_IMAGE))));
					competition.setOrder(cursor.getInt(cursor
							.getColumnIndex((KEY_COMPETITION_ORDER))));
					competition.setFecModificacion(cursor.getFloat(cursor
							.getColumnIndex((KEY_COMPETITION_FECMOD))));

					competitions.add(competition);

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
		return competitions;
	}

	/****************************************** COMPETITION *****************************************************/
	/****************************************** SECTION *****************************************************/
	/**
	 * 
	 * @param db
	 * @param competitionId
	 * @param section
	 * @throws Exception
	 */
	private void insertStaticSection(SQLiteDatabase db, int competitionId,
			Section section) throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_COMPETITION_SECTION_COMPETITION_ID, competitionId);
		values.put(KEY_COMPETITION_SECTION_NAME, section.getName());
		values.put(KEY_COMPETITION_SECTION_TYPE, section.getType());
		values.put(KEY_COMPETITION_SECTION_VIEWTYPE, section.getViewType());

		values.put(KEY_COMPETITION_SECTION_ORDER, section.getOrder());
		if (section.isActive())
			values.put(KEY_COMPETITION_SECTION_ACTIVE, 1);
		else
			values.put(KEY_COMPETITION_SECTION_ACTIVE, 0);
		values.put(KEY_COMPETITION_SECTION_DEFAULT, section.isStart());
		if (section instanceof TeamSection) {
			values.put(KEY_COMPETITION_SECTION_TYPEORDER,
					((TeamSection) section).getTypeOrder());

		}
		// if (section instanceof LinkSection) {
		// values.put(KEY_COMPETITION_SECTION_TYPEORDER,
		// ((LinkSection) section).getViewType());
		//
		// }
		values.put(KEY_COMPETITION_SECTION_URL, section.getUrl());
		if (section instanceof ClasificacionSection) {
			Object[] keys = ((ClasificacionSection) section).getUrls().keySet()
					.toArray();
			String key = (String) keys[0];
			values.put(KEY_COMPETITION_SECTION_URL,
					((ClasificacionSection) section).getUrls().get(key));
			values.put(KEY_COMPETITION_SECTION_NAME, key);

		}

		Cursor cursor = null;
		try {
			String selectQuery = "Select 1 from " + TABLE_COMPETITION_SECTION
					+ " where " + KEY_COMPETITION_SECTION_COMPETITION_ID
					+ " = " + competitionId + " and "
					+ KEY_COMPETITION_SECTION_TYPE + " like '"
					+ section.getType()
					// + "' and "+ KEY_COMPETITION_SECTION_NAME + " like '"+
					// section.getName()
					+ "'";

			cursor = db.rawQuery(selectQuery, null);
			if (cursor.getCount() > 0) {
				String whereClause = KEY_COMPETITION_SECTION_COMPETITION_ID
						+ " = ? and " + KEY_COMPETITION_SECTION_TYPE
						+ " like ?"
				// +" and "+ KEY_COMPETITION_SECTION_NAME + " like ?"
				;
				Log.d("SECTIONS", "Actualizando Section: " + section.getType());
				db.update(
						TABLE_COMPETITION_SECTION,
						values,
						whereClause,
						new String[] { String.valueOf(competitionId),
								section.getType()
						// ,section.getName()
						});
			} else {
				Log.d("SECTIONS", "Insertando Section: " + section.getType());
				db.insert(TABLE_COMPETITION_SECTION, null, values);
			}

		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} catch (Exception e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

	}

	private void deleteOldSections(SQLiteDatabase db, String typesActivos,
			Competition competition) {
		String[] ids = typesActivos.split(",");
		// String sql = "delete from " + TABLE_COMPETITION_SECTION + " where "
		// + KEY_COMPETITION_SECTION_COMPETITION_ID + " = \""
		// + competition.getId() + "\"";
		String whereClause = KEY_COMPETITION_SECTION_COMPETITION_ID
				+ " = ? and ";
		String[] whereArgs = new String[ids.length + 1];
		whereArgs[0] = String.valueOf(competition.getId());

		if (ids.length > 0) {
			// sql += " and ";
			for (int i = 0; i < ids.length; i++) {
				whereClause += KEY_COMPETITION_SECTION_TYPE
						+ " not like ? and ";
				whereArgs[i + 1] = ids[i];
				// sql += KEY_COMPETITION_SECTION_TYPE + " not like \"" + id
				// + "\" and ";
			}
			whereClause = whereClause.substring(0, whereClause.length() - 4);
			// sql = sql.substring(0, sql.length() - 4);

			int delete = db.delete(TABLE_COMPETITION_SECTION, whereClause,
					whereArgs);
			Log.d("SECTIONS", "Sections Deleted: " + delete);
			// db.rawQuery(sql, null);
		}

	}

	public ArrayList<Section> getSectionsCompetition(int competitionId) {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_COMPETITION_SECTION
				+ " where " + KEY_COMPETITION_SECTION_COMPETITION_ID + " = "
				+ competitionId + " order by " + KEY_COMPETITION_SECTION_ORDER
				+ ", " + KEY_COMPETITION_SECTION_TYPE;

		ArrayList<Section> sections = new ArrayList<Section>();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);
			String type;
			Section section = null;
			// Article article;
			if (cursor.moveToFirst()) {
				do {
					type = cursor.getString(cursor
							.getColumnIndex(KEY_COMPETITION_SECTION_TYPE));
					Log.d("SECTIONS",
							"Obteniendo Section: "
									+ type
									+ " Orden: "
									+ cursor.getInt(cursor
											.getColumnIndex((KEY_COMPETITION_SECTION_ORDER))));
					if (type.equalsIgnoreCase(SECTIONS.TEAMS)) {
						section = new TeamSection();
						section.setType(SECTIONS.TEAMS);
						((TeamSection) section)
								.setTypeOrder(cursor.getString(cursor
										.getColumnIndex((KEY_COMPETITION_SECTION_TYPEORDER))));
					} else if (type.equalsIgnoreCase(SECTIONS.CALENDAR)) {
						section = new Section();
						section.setType(SECTIONS.CALENDAR);
					} else if (type.equalsIgnoreCase(SECTIONS.CARROUSEL)) {
						section = new Section();
						section.setType(SECTIONS.CARROUSEL);
					} else if (type.equalsIgnoreCase(SECTIONS.STADIUMS)) {
						section = new Section();
						section.setType(SECTIONS.STADIUMS);
					} else if (type.equalsIgnoreCase(SECTIONS.PALMARES)) {
						section = new Section();
						section.setType(SECTIONS.PALMARES);
					} else if (type.equalsIgnoreCase(SECTIONS.SEARCHER)) {
						section = new Section();
						section.setType(SECTIONS.SEARCHER);
					} else if (type.equalsIgnoreCase(SECTIONS.NEWS)) {
						section = new Section();
						section.setType(SECTIONS.NEWS);
					} else if (type.equalsIgnoreCase(SECTIONS.VIDEOS)) {
						section = new Section();
						section.setType(SECTIONS.VIDEOS);
					} else if (type.equalsIgnoreCase(SECTIONS.PHOTOS)) {
						section = new Section();
						section.setType(SECTIONS.PHOTOS);
					} else if (type.equalsIgnoreCase(SECTIONS.COMPARATOR)) {
						section = new Section();
						section.setType(SECTIONS.COMPARATOR);
					} else if (type
							.equalsIgnoreCase(SECTIONS.LINK_VIEW_OUTSIDE)) {
						section = new Section();
						section.setType(SECTIONS.LINK_VIEW_OUTSIDE);
					} else if (type.equalsIgnoreCase(SECTIONS.LINK_VIEW_INSIDE)) {
						section = new Section();
						section.setType(SECTIONS.LINK_VIEW_INSIDE);
					} else if (type.equalsIgnoreCase(SECTIONS.LINK)) {
						section = new Section();
						section.setType(SECTIONS.LINK);
					} else if (type.equalsIgnoreCase(SECTIONS.CLASIFICATION)) {
						section = new ClasificacionSection();
						section.setType(SECTIONS.CLASIFICATION);
						((ClasificacionSection) section)
								.addUrl(cursor
										.getString(cursor
												.getColumnIndex(KEY_COMPETITION_SECTION_NAME)),
										cursor.getString(cursor
												.getColumnIndex(KEY_COMPETITION_SECTION_URL)));

						// } else if (type.equalsIgnoreCase(SECTIONS.LINK)) {
						// section = new LinkSection();
						// section.setType(SECTIONS.LINK);
						// ((LinkSection) section)
						// .setViewType(cursor.getString(cursor
						// .getColumnIndex((KEY_COMPETITION_SECTION_TYPEORDER))));
					}

					if (section != null) {
						section.setName(cursor.getString(cursor
								.getColumnIndex((KEY_COMPETITION_SECTION_NAME))));
						section.setUrl(cursor.getString(cursor
								.getColumnIndex((KEY_COMPETITION_SECTION_URL))));
						section.setViewType(cursor.getString(cursor
								.getColumnIndex((KEY_COMPETITION_SECTION_VIEWTYPE))));
						section.setOrder(cursor.getInt(cursor
								.getColumnIndex((KEY_COMPETITION_SECTION_ORDER))));
						section.setActive(1 == cursor.getInt(cursor
								.getColumnIndex((KEY_COMPETITION_SECTION_ACTIVE))));
						section.setStart(1 == cursor.getInt(cursor
								.getColumnIndex((KEY_COMPETITION_SECTION_DEFAULT))));
						sections.add(section);
					}

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
		return sections;
	}

	/****************************************** SECTION *****************************************************/
	/****************************************** TEAM *****************************************************/
	/**
	 * @param team
	 * @param competitionId
	 * @param groupName
	 * @param groupOrder
	 * @param competition
	 * @throws Exception
	 */
	private void insertOrUpdateStaticTeam(SQLiteDatabase db, Team team,
			String groupName, int competitionId, int groupOrder)
			throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_TEAM_ID, team.getId());
		values.put(KEY_TEAM_SHORTNAME, team.getShortName());
		values.put(KEY_TEAM_STATIC_INFO, team.isStaticInfo());
		values.put(KEY_TEAM_URL, team.getUrl());
		values.put(KEY_TEAM_URL_INFO, team.getUrlInfo());
		values.put(KEY_TEAM_SHIELD1, team.getGridShield());
		values.put(KEY_TEAM_SHIELD2, team.getCalendarShield());
		values.put(KEY_TEAM_SHIELD3, team.getDetailShield());

		if (team.getCountShirts() > 0)
			values.put(KEY_TEAM_SHIRT1, team.getShirts().get(0));
		if (team.getCountShirts() > 1)
			values.put(KEY_TEAM_SHIRT2, team.getShirts().get(1));
		if (team.getCountShirts() > 2)
			values.put(KEY_TEAM_SHIRT3, team.getShirts().get(2));

		values.put(KEY_TEAM_GAMESYSTEM, team.getGameSystem());
		values.put(KEY_TEAM_HISTORY, team.getHistory());
		values.put(KEY_TEAM_FEDERATION_FOUNDATION,
				team.getFederationFoundation());
		values.put(KEY_TEAM_FEDERATION_AFFILIATION,
				team.getFederationAffiliation());
		values.put(KEY_TEAM_NUMPLAYERS, team.getNumPlayers());
		values.put(KEY_TEAM_NUMCLUBS, team.getNumClubs());
		values.put(KEY_TEAM_NUMREFEREES, team.getNumReferees());

		if (team.getArticle() != null) {
			values.put(KEY_TEAM_ARTICLE_AUTHOR, team.getArticle().getAuthor());
			// values.put(KEY_TEAM_ARTICLE_CHARGE,
			// team.getArticle().getCharge());
			// values.put(KEY_TEAM_ARTICLE_TITLE, team.getArticle().getTitle());
			// values.put(KEY_TEAM_ARTICLE_SUBTITLE,
			// team.getArticle().getSubTitle());
			values.put(KEY_TEAM_ARTICLE_BODY, team.getArticle().getBody());
			values.put(KEY_TEAM_ARTICLE_VIDEO, team.getArticle().getUrlVideo());
			values.put(KEY_TEAM_ARTICLE_VIDEOIMAGE, team.getArticle()
					.getUrlVideoImage());
		}

		ContentValues values2 = new ContentValues();
		values2.put(KEY_COMPETITION_TEAM_COMPETITION_ID, competitionId);
		values2.put(KEY_COMPETITION_TEAM_GROUP_NAME, groupName);
		values2.put(KEY_COMPETITION_TEAM_GROUP_ORDER, groupOrder);
		values2.put(KEY_COMPETITION_TEAM_TEAM_ID, team.getId());

		Cursor cursor = null;
		Cursor cursor2 = null;
		try {
			String selectQuery = "Select 1 from " + TABLE_TEAM + " where "
					+ KEY_TEAM_ID + " like \"" + team.getId() + "\"";
			cursor = db.rawQuery(selectQuery, null);
			if (cursor.getCount() > 0) {
				String whereClause = KEY_TEAM_ID + " like ?";
				db.update(TABLE_TEAM, values, whereClause,
						new String[] { String.valueOf(team.getId()) });
			} else {
				db.insert(TABLE_TEAM, null, values);
			}

			String selectQuery2 = "Select 1 from " + TABLE_COMPETITION_TEAM
					+ " where " + KEY_COMPETITION_TEAM_TEAM_ID + " like \""
					+ team.getId() + "\" and "
					+ KEY_COMPETITION_TEAM_COMPETITION_ID + " like \""
					+ competitionId + "\"";
			cursor2 = db.rawQuery(selectQuery2, null);
			if (cursor2.getCount() == 0) {
				db.insert(TABLE_COMPETITION_TEAM, null, values2);
			}
		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}

			if (cursor2 != null && !cursor2.isClosed()) {
				cursor2.close();
				cursor2 = null;
			}
		}
	}

	private void insertOrUpdateStaticTeam(SQLiteDatabase db, Team team)
			throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_TEAM_ID, team.getId());
		values.put(KEY_TEAM_SHORTNAME, team.getShortName());
		values.put(KEY_TEAM_STATIC_INFO, team.isStaticInfo());
		values.put(KEY_TEAM_URL, team.getUrl());
		values.put(KEY_TEAM_URL_INFO, team.getUrlInfo());
		values.put(KEY_TEAM_SHIELD1, team.getGridShield());
		values.put(KEY_TEAM_SHIELD2, team.getCalendarShield());
		values.put(KEY_TEAM_SHIELD3, team.getDetailShield());

		if (team.getCountShirts() > 0)
			values.put(KEY_TEAM_SHIRT1, team.getShirts().get(0));
		if (team.getCountShirts() > 1)
			values.put(KEY_TEAM_SHIRT2, team.getShirts().get(1));
		if (team.getCountShirts() > 2)
			values.put(KEY_TEAM_SHIRT3, team.getShirts().get(2));

		values.put(KEY_TEAM_GAMESYSTEM, team.getGameSystem());
		values.put(KEY_TEAM_HISTORY, team.getHistory());
		values.put(KEY_TEAM_FEDERATION_FOUNDATION,
				team.getFederationFoundation());
		values.put(KEY_TEAM_FEDERATION_AFFILIATION,
				team.getFederationAffiliation());
		values.put(KEY_TEAM_NUMPLAYERS, team.getNumPlayers());
		values.put(KEY_TEAM_NUMCLUBS, team.getNumClubs());
		values.put(KEY_TEAM_NUMREFEREES, team.getNumReferees());

		if (team.getArticle() != null) {
			values.put(KEY_TEAM_ARTICLE_AUTHOR, team.getArticle().getAuthor());
			// values.put(KEY_TEAM_ARTICLE_CHARGE,
			// team.getArticle().getCharge());
			// values.put(KEY_TEAM_ARTICLE_TITLE, team.getArticle().getTitle());
			// values.put(KEY_TEAM_ARTICLE_SUBTITLE,
			// team.getArticle().getSubTitle());
			values.put(KEY_TEAM_ARTICLE_BODY, team.getArticle().getBody());
			values.put(KEY_TEAM_ARTICLE_VIDEO, team.getArticle().getUrlVideo());
			values.put(KEY_TEAM_ARTICLE_VIDEOIMAGE, team.getArticle()
					.getUrlVideoImage());
		}
		Cursor cursor = null;
		try {
			String selectQuery = "Select 1 from " + TABLE_TEAM + " where "
					+ KEY_TEAM_ID + " like \"" + team.getId() + "\"";
			cursor = db.rawQuery(selectQuery, null);
			if (cursor.getCount() > 0) {
				String whereClause = KEY_TEAM_ID + " like ?";
				db.update(TABLE_TEAM, values, whereClause,
						new String[] { String.valueOf(team.getId()) });
			} else {
				db.insert(TABLE_TEAM, null, values);
			}

		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
	}

	public void updateStaticTeam(Team team, String competitionId) {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		try {
			db.beginTransaction();

			insertOrUpdateStaticTeam(db, team);
			// Staff
			insertOrUpdateStaticStaff(db, team.getId(), team.getTeamStaff());
			// Ideal Team
			insertOrUpdateStaticIdealTeam(db, team.getId(),
					team.getIdealPlayers());
			// HistoricalPalmares
			insertOrUpdateHistoricalPalmaresTeam(db, team.getId(),
					competitionId, team.getHistoricalPalmares());

			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	/**
	 * @param teams
	 */
	public void updateTeam(Team team) {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		try {
			db.beginTransaction();

			updateTeam(db, team);
			// No se almacena la plantilla en el primer nivel, solo cuando se
			// acceda al detalle de jugador
			String ids = insertOrUpdatePlantilla(db, team.getId(),
					team.getPlantilla());
			if (!ids.equalsIgnoreCase(""))
				deleteOldPlayers(db, ids, team.getId());
			insertOrUpdateStaticStaff(db, team.getId(), team.getTeamStaff());
			insertOrUpdatePalmares(db, team.getId(), team.getPalmares());
			insertOrUpdateTeamEstadisticas(db, team.getId(), team.getStats());

			db.setTransactionSuccessful();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	/**
	 * @param team
	 * @throws Exception
	 */
	private void updateTeam(SQLiteDatabase db, Team team) throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_TEAM_ID, team.getId());
		values.put(KEY_TEAM_FECMOD, team.getFecModificacion());
		values.put(KEY_TEAM_URLFICHA, team.getUrlFicha());
		values.put(KEY_TEAM_URLTAG, team.getUrlTag());
		values.put(KEY_TEAM_NAME, team.getName());
		values.put(KEY_TEAM_SHORTNAME, team.getShortName());
		values.put(KEY_TEAM_URL, team.getUrl());
		values.put(KEY_TEAM_URL_INFO, team.getUrlInfo());
		values.put(KEY_TEAM_WEB, team.getWeb());
		values.put(KEY_TEAM_FUNDATION, team.getfundation());
		if (team.getShields() != null) {
			values.put(KEY_TEAM_SHIELD1, team.getShields().get("grid"));
			values.put(KEY_TEAM_SHIELD2, team.getShields().get("calendar"));
			values.put(KEY_TEAM_SHIELD3, team.getShields().get("detail"));
		}
		if (team.getShirts().size() > 0)
			values.put(KEY_TEAM_SHIRT1, team.getShirts().get(0));
		if (team.getShirts().size() > 1)
			values.put(KEY_TEAM_SHIRT2, team.getShirts().get(1));
		if (team.getShirts().size() > 2)
			values.put(KEY_TEAM_SHIRT3, team.getShirts().get(2));

		values.put(KEY_TEAM_GAMESYSTEM, team.getGameSystem());
		values.put(KEY_TEAM_HISTORY, team.getHistory());
		values.put(KEY_TEAM_FEDERATION_FOUNDATION,
				team.getFederationFoundation());
		values.put(KEY_TEAM_FEDERATION_AFFILIATION,
				team.getFederationAffiliation());
		values.put(KEY_TEAM_NUMPLAYERS, team.getNumPlayers());
		values.put(KEY_TEAM_NUMCLUBS, team.getNumClubs());
		values.put(KEY_TEAM_NUMREFEREES, team.getNumReferees());

		if (team.getArticle() != null) {
			values.put(KEY_TEAM_ARTICLE_AUTHOR, team.getArticle().getAuthor());
			// values.put(KEY_TEAM_ARTICLE_CHARGE,
			// team.getArticle().getCharge());
			// values.put(KEY_TEAM_ARTICLE_TITLE, team.getArticle().getTitle());
			// values.put(KEY_TEAM_ARTICLE_SUBTITLE,
			// team.getArticle().getSubTitle());
			values.put(KEY_TEAM_ARTICLE_BODY, team.getArticle().getBody());
			values.put(KEY_TEAM_ARTICLE_VIDEO, team.getArticle().getUrlVideo());
			values.put(KEY_TEAM_ARTICLE_VIDEOIMAGE, team.getArticle()
					.getUrlVideoImage());
		}

		try {
			db.update(TABLE_TEAM, values, KEY_TEAM_ID + " like ?",
					new String[] { String.valueOf(team.getId()) });
		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		}
	}

	/**
	 * @param db
	 * @param id
	 * @param plantilla
	 * @return
	 */
	private String insertOrUpdatePlantilla(SQLiteDatabase db, String teamId,
			ArrayList<Player> plantilla) throws Exception {
		String idsActivos = "";
		ContentValues values;
		Cursor cursor = null;
		for (Player player : plantilla) {
			values = new ContentValues();
			values.put(KEY_PLAYER_ID, player.getId());
			idsActivos += player.getId() + ",";
			values.put(KEY_PLAYER_TEAM_ID, teamId);
			values.put(KEY_PLAYER_SHORTNAME, player.getShortName());
			values.put(KEY_PLAYER_URL, player.getUrl());
			values.put(KEY_PLAYER_URL_FOTO, player.getUrlFoto());
			values.put(KEY_PLAYER_DORSAL, player.getDorsal());
			values.put(KEY_PLAYER_DEMARCACION, player.getDemarcation());
			values.put(KEY_PLAYER_POSITION, player.getPosition());
			try {

				String selectQuery = "Select 1 from " + TABLE_PLAYER
						+ " where " + KEY_PLAYER_ID + " = " + player.getId();
				cursor = db.rawQuery(selectQuery, null);
				if (cursor.getCount() > 0) {
					String whereClause = KEY_PLAYER_ID + " = ?";
					db.update(TABLE_PLAYER, values, whereClause,
							new String[] { String.valueOf(player.getId()) });
				} else {
					db.insert(TABLE_PLAYER, null, values);
				}

			} catch (SQLiteException e) {
				throw new Exception("Error al actualizar db: " + e.getMessage());
			} finally {
				if (cursor != null && !cursor.isClosed()) {
					cursor.close();
					cursor = null;
				}
			}
		}

		return idsActivos;

	}

	private String insertOrUpdateStaticIdealTeam(SQLiteDatabase db,
			String teamId, ArrayList<PlayerOnField> idealPlayers)
			throws Exception {
		String idsActivos = "";
		db.delete(TABLE_TEAM_IDEALPLAYERS, KEY_TEAM_IDEALPLAYERS_TEAM_ID
				+ " = ? ", new String[] { String.valueOf(teamId) });
		for (PlayerOnField player : idealPlayers) {
			// insertNoExistPlayer(db, teamId, player);

			insertorUpdateIdealPlayer(db, teamId, player);
			idsActivos += player.getId() + ",";
		}
		return idsActivos;

	}

	private void insertorUpdateIdealPlayer(SQLiteDatabase db, String teamId,
			PlayerOnField player) throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_TEAM_IDEALPLAYERS_TEAM_ID, teamId);
		values.put(KEY_TEAM_IDEALPLAYERS_PLAYER_ID, player.getId());
		values.put(KEY_TEAM_IDEALPLAYERS_PLAYER_POSITION, player.getPosition());
		values.put(KEY_TEAM_IDEALPLAYERS_PLAYER_NAME, player.getName());
		values.put(KEY_TEAM_IDEALPLAYERS_PLAYER_URLPHOTO, player.getUrlPhoto());
		values.put(KEY_TEAM_IDEALPLAYERS_PLAYER_URLFICHA, player.getUrl());

		Cursor cursor = null;
		try {

			// String selectQuery = "Select 1 from " + TABLE_TEAM_IDEALPLAYERS +
			// " where "+ KEY_TEAM_IDEALPLAYERS_TEAM_ID + " = " +
			// teamId+" and "+ KEY_TEAM_IDEALPLAYERS_PLAYER_ID + " = " +
			// player.getId() ;
			// cursor = db.rawQuery(selectQuery, null);
			// if (cursor.getCount() > 0) {
			// String whereClause = KEY_PLAYER_ID + " = ?";
			// db.update(TABLE_PLAYER, values, whereClause,
			// new String[] { String.valueOf(player.getId()) });
			// } else {
			db.insert(TABLE_TEAM_IDEALPLAYERS, null, values);
			// }

		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

	}

	public ArrayList<PlayerOnField> getIdealPlayers(String currentTeamId) {

		SQLiteDatabase db = sInstance.getWritableDatabase();

		ArrayList<PlayerOnField> players = new ArrayList<PlayerOnField>();

		String selectQuery;
		// selectQuery = "SELECT * FROM " + TABLE_PLAYER + ","+
		// TABLE_TEAM_IDEALPLAYERS + " where " + TABLE_PLAYER + "."+
		// KEY_PLAYER_ID + " = " + TABLE_TEAM_IDEALPLAYERS + "."+
		// KEY_TEAM_IDEALPLAYERS_PLAYER_ID + " and "+ TABLE_TEAM_IDEALPLAYERS +
		// "." + KEY_TEAM_IDEALPLAYERS_TEAM_ID+ " = " + currentTeamId +
		// " order by "+ KEY_TEAM_IDEALPLAYERS_PLAYER_POSITION + ", "+
		// KEY_PLAYER_DORSAL;
		selectQuery = "SELECT * FROM " + TABLE_TEAM_IDEALPLAYERS + " where "
				+ TABLE_TEAM_IDEALPLAYERS + "." + KEY_TEAM_IDEALPLAYERS_TEAM_ID
				+ " = " + currentTeamId + " order by "
				+ KEY_TEAM_IDEALPLAYERS_PLAYER_POSITION;

		Cursor cursor = null;
		try {

			cursor = db.rawQuery(selectQuery, null);
			Log.d("IDEALPLAYERS", "Numero: " + cursor.getCount());
			if (cursor.moveToFirst()) {
				do {
					PlayerOnField player = new PlayerOnField();
					player.setId(cursor.getInt(cursor
							.getColumnIndex(KEY_TEAM_IDEALPLAYERS_TEAM_ID)));
					player.setName(cursor.getString(cursor
							.getColumnIndex(KEY_TEAM_IDEALPLAYERS_PLAYER_NAME)));
					player.setPosition(cursor.getInt(cursor
							.getColumnIndex(KEY_TEAM_IDEALPLAYERS_PLAYER_POSITION)));
					player.setUrlPhoto(cursor.getString(cursor
							.getColumnIndex(KEY_TEAM_IDEALPLAYERS_PLAYER_URLPHOTO)));
					player.setUrl(cursor.getString(cursor
							.getColumnIndex(KEY_TEAM_IDEALPLAYERS_PLAYER_URLFICHA)));

					player.setDorsal(cursor.getInt(cursor
							.getColumnIndex(KEY_TEAM_IDEALPLAYERS_PLAYER_DORSAL)));

					Log.d("IDEALPLAYERS",
							"Database Cargando URL: "
									+ cursor.getString(cursor
											.getColumnIndex(KEY_TEAM_IDEALPLAYERS_PLAYER_URLPHOTO)));
					players.add(player);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return players;
	}

	private void insertNoExistPlayer(SQLiteDatabase db, String teamId,
			PlayerOnField player) throws Exception {
		ContentValues values;
		values = new ContentValues();
		values.put(KEY_PLAYER_ID, player.getId());
		values.put(KEY_PLAYER_TEAM_ID, teamId);
		values.put(KEY_PLAYER_SHORTNAME, player.getName());
		values.put(KEY_PLAYER_URL, player.getUrl());
		values.put(KEY_PLAYER_URL_FOTO, player.getUrlPhoto());
		// values.put(KEY_PLAYER_DORSAL, player.getDorsal());
		values.put(KEY_PLAYER_POSITION, player.getPosition());

		Cursor cursor = null;
		try {

			String selectQuery = "Select 1 from " + TABLE_PLAYER + " where "
					+ KEY_PLAYER_ID + " = " + player.getId();
			cursor = db.rawQuery(selectQuery, null);
			if (cursor.getCount() == 0) {
				db.insert(TABLE_PLAYER, null, values);
			}

		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

	}

	public void deleteOldPlayers(SQLiteDatabase db, String idsActivos,
			String teamId) throws Exception {

		String[] ids = idsActivos.split(",");
		String sql = "delete from " + TABLE_PLAYER + " where "
				+ KEY_PLAYER_TEAM_ID + " like \"" + teamId + "\"";
		if (ids.length > 0) {
			sql += " and ";
			for (String id : ids) {
				sql += KEY_PLAYER_ID + " != " + id + " and ";
			}
			sql = sql.substring(0, sql.length() - 4);
			db.rawQuery(sql, null);
		}

	}

	private ArrayList<Player> getTeamPlantilla(String currentTeamId,
			SQLiteDatabase db) {

		ArrayList<Player> plantilla = new ArrayList<Player>();

		String selectQuery;
		selectQuery = "SELECT * FROM " + TABLE_PLAYER + " where "
				+ KEY_PLAYER_TEAM_ID + " like \"" + currentTeamId
				+ "\" order by " + KEY_PLAYER_DEMARCACION + ", "
				+ KEY_PLAYER_POSITION + ", " + KEY_PLAYER_DORSAL;

		Cursor cursor2 = null;
		try {
			cursor2 = db.rawQuery(selectQuery, null);
			if (cursor2.moveToFirst()) {
				String ids = "";
				do {
					Player player = new Player();
					player.setId(cursor2.getInt(cursor2
							.getColumnIndex(KEY_PLAYER_ID)));
					ids += player.getId() + ", ";
					player.setName(cursor2.getString(cursor2
							.getColumnIndex(KEY_PLAYER_NAME)));
					player.setShortName(cursor2.getString(cursor2
							.getColumnIndex(KEY_PLAYER_SHORTNAME)));
					player.setDorsal(cursor2.getInt(cursor2
							.getColumnIndex(KEY_PLAYER_DORSAL)));
					player.setDemarcation(cursor2.getString(cursor2
							.getColumnIndex(KEY_PLAYER_DEMARCACION)));
					player.setPosition(cursor2.getInt(cursor2
							.getColumnIndex(KEY_PLAYER_POSITION)));
					player.setUrl(cursor2.getString(cursor2
							.getColumnIndex(KEY_PLAYER_URL)));
					player.setUrlFoto(cursor2.getString(cursor2
							.getColumnIndex(KEY_PLAYER_URL_FOTO)));

					plantilla.add(player);
				} while (cursor2.moveToNext());
				Log.d(TAG, "idsRecuperados; " + ids);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor2 != null && !cursor2.isClosed()) {
				cursor2.close();
				cursor2 = null;
			}
		}

		return plantilla;
	}

	public ArrayList<Group> getInfoGroups(int competitionId) {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_TEAM + ", "
				+ TABLE_COMPETITION_TEAM + " where " + TABLE_TEAM + "."
				+ KEY_TEAM_ID + " like " + TABLE_COMPETITION_TEAM + "."
				+ KEY_COMPETITION_TEAM_TEAM_ID + " and "
				+ TABLE_COMPETITION_TEAM + "."
				+ KEY_COMPETITION_TEAM_COMPETITION_ID + " like \""
				+ competitionId + "\" order by " + TABLE_COMPETITION_TEAM + "."
				+ KEY_COMPETITION_TEAM_GROUP_NAME + ","
				+ TABLE_COMPETITION_TEAM + "."
				+ KEY_COMPETITION_TEAM_GROUP_ORDER;

		Cursor cursor = null;

		ArrayList<Group> groups = new ArrayList<Group>();

		Group group = null;
		Team team = null;
		String groupName, groupNameOld = null;
		try {
			cursor = db.rawQuery(selectQuery, null);

			Log.d(TAG, "Teams in " + competitionId + ": " + cursor.getCount());

			if (cursor.moveToFirst()) {
				do {
					groupName = cursor.getString(cursor
							.getColumnIndex((KEY_COMPETITION_TEAM_GROUP_NAME)));
					if (groupNameOld == null
							|| !groupNameOld.equalsIgnoreCase(groupName)) {
						group = new Group();
						group.setName(groupName);
						groupNameOld = groupName;
						groups.add(group);
					}
					team = new Team();
					team.setId(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_ID))));
					team.setShortName(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_SHORTNAME))));
					team.setUrl(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_URL))));
					team.setUrlInfo(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_URL_INFO))));
					team.addShieldGrid(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_SHIELD1))));
					group.addTeam(team);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return groups;
	}

	/**
	 * @param competitionName
	 * @return
	 */
	public ArrayList<Team> getShortInfoTeams(int competitionId) {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_TEAM + ", "
				+ TABLE_COMPETITION_TEAM + " where " + TABLE_TEAM + "."
				+ KEY_TEAM_ID + " like " + TABLE_COMPETITION_TEAM + "."
				+ KEY_COMPETITION_TEAM_TEAM_ID + " and "
				+ TABLE_COMPETITION_TEAM + "."
				+ KEY_COMPETITION_TEAM_COMPETITION_ID + " like \""
				+ competitionId + "\" order by " + TABLE_TEAM + "."
				+ KEY_TEAM_SHORTNAME;

		ArrayList<Team> teams = new ArrayList<Team>();
		Team team = null;

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);

			Log.d(TAG, "Teams in " + competitionId + ": " + cursor.getCount());

			if (cursor.moveToFirst()) {
				do {
					team = new Team();
					team.setId(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_ID))));
					team.setShortName(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_SHORTNAME))));
					team.setUrl(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_URL))));
					team.setUrlInfo(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_URL_INFO))));
					team.addShieldGrid(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_SHIELD1))));
					teams.add(team);

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
		return teams;
	}

	/**
	 * @param db
	 * @param idsActivos
	 * @param competition
	 * @throws Exception
	 */
	private void deleteOldTeams(SQLiteDatabase db, String idsActivos,
			Competition competition) throws Exception {
		String[] ids = idsActivos.split(",");
		String sql = "delete from " + TABLE_COMPETITION_TEAM + " where "
				+ KEY_COMPETITION_TEAM_COMPETITION_ID + " = \""
				+ competition.getId() + "\"";
		if (ids.length > 0) {
			sql += " and ";
			for (String id : ids) {
				sql += KEY_COMPETITION_TEAM_TEAM_ID + " not like \"" + id
						+ "\" and ";
			}
			sql = sql.substring(0, sql.length() - 4);
			db.rawQuery(sql, null);
		}

	}

	/**
	 * @param currentTeamId
	 * @param competitionId
	 * @return
	 */
	public Team getTeam(String currentTeamId) {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_TEAM + " where "
				+ KEY_TEAM_ID + " like \"" + currentTeamId + "\"";

		Team team = new Team();

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);

			Article article;

			if (cursor.moveToFirst()) {
				do {

					team.setId(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_ID))));
					team.setFecModificacion(cursor.getInt(cursor
							.getColumnIndex((KEY_TEAM_FECMOD))));
					team.setName(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_NAME))));
					team.setShortName(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_SHORTNAME))));
					team.setStaticInfo(cursor.getInt(cursor
							.getColumnIndex((KEY_TEAM_STATIC_INFO))) == 1);
					team.setUrl(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_URL))));
					team.setUrlInfo(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_URL_INFO))));
					team.setUrlFicha(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_URLFICHA))));
					team.setUrlTag(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_URLTAG))));
					team.setGameSystem(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_GAMESYSTEM))));
					team.setHistory(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_HISTORY))));
					team.setFederationFoundation(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_FEDERATION_FOUNDATION))));
					team.setFederationAffiliation(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_FEDERATION_AFFILIATION))));
					team.setNumPlayers(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_NUMPLAYERS))));
					team.setNumClubs(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_NUMCLUBS))));
					team.setNumReferees(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_NUMREFEREES))));

					team.setWeb(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_WEB))));
					team.setFundation(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_FUNDATION))));

					team.addShieldGrid(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_SHIELD1))));
					team.addShieldCalendar(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_SHIELD2))));
					team.addShieldDetail(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_SHIELD3))));

					team.addShirt(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_SHIRT1))));
					team.addShirt(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_SHIRT2))));
					team.addShirt(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_SHIRT3))));

					if (cursor.getColumnIndex(KEY_TEAM_ARTICLE_AUTHOR) > 0) {
						article = new Article();
						article.setAuthor(cursor.getString(cursor
								.getColumnIndex(KEY_TEAM_ARTICLE_AUTHOR)));
						// article.setCharge(cursor.getString(cursor
						// .getColumnIndex(KEY_TEAM_ARTICLE_CHARGE)));
						// article.setTitle(cursor.getString(cursor
						// .getColumnIndex(KEY_TEAM_ARTICLE_TITLE)));
						// article.setSubTitle(cursor.getString(cursor
						// .getColumnIndex(KEY_TEAM_ARTICLE_SUBTITLE)));
						article.setBody(cursor.getString(cursor
								.getColumnIndex(KEY_TEAM_ARTICLE_BODY)));
						article.setUrlVideo(cursor.getString(cursor
								.getColumnIndex(KEY_TEAM_ARTICLE_VIDEO)));
						article.setUrlVideoImage(cursor.getString(cursor
								.getColumnIndex(KEY_TEAM_ARTICLE_VIDEOIMAGE)));

						team.setArticle(article);
					}

					// Se recupera el staff
					team.setTeamStaff(getStaff(currentTeamId, db));

					// Se recupera el palmares
					team.setPalmares(getTeamPalmares(currentTeamId, db));

					// Se recupera LA PLANTILLA
					team.setPlantilla(getTeamPlantilla(currentTeamId, db));

					// Se recuperan las estadisticas
					team.setStats(getStats(currentTeamId, db));

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
		return team;
	}

	public HashMap<String, Team> getCalendarInfoTeams(int competitionId) {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_TEAM + ", "
				+ TABLE_COMPETITION_TEAM + " where " + TABLE_TEAM + "."
				+ KEY_TEAM_ID + " like " + TABLE_COMPETITION_TEAM + "."
				+ KEY_COMPETITION_TEAM_TEAM_ID + " and "
				+ TABLE_COMPETITION_TEAM + "."
				+ KEY_COMPETITION_TEAM_COMPETITION_ID + " like \""
				+ competitionId + "\" order by " + TABLE_TEAM + "."
				+ KEY_TEAM_SHORTNAME;

		Team team = null;
		HashMap<String, Team> teams = new HashMap<String, Team>();

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);

			Log.d(TAG, "Teams in " + competitionId + ": " + cursor.getCount());

			if (cursor.moveToFirst()) {
				do {
					team = new Team();
					team.setId(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_ID))));
					team.setShortName(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_SHORTNAME))));
					team.addShieldCalendar(cursor.getString(cursor
							.getColumnIndex((KEY_TEAM_SHIELD2))));
					teams.put(team.getId(), team);

				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
		return teams;
	}

	/**
	 * @param teamId
	 * @return
	 */
	public String getTeamGridShield(String teamId) {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		String selectQuery = "SELECT " + KEY_TEAM_SHIELD2 + " FROM "
				+ TABLE_TEAM + " where " + KEY_TEAM_ID + " = " + teamId;

		String shield = null;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {

				shield = cursor.getString(cursor
						.getColumnIndex((KEY_TEAM_SHIELD2)));

			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
		return shield;
	}

	/**
	 * @param teamId
	 * @return
	 */
	public String getTeamShield(String teamId) {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		String selectQuery = "SELECT " + KEY_TEAM_SHIELD1 + " FROM "
				+ TABLE_TEAM + " where " + KEY_TEAM_ID + " = " + teamId;

		String shield = null;

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {

				shield = cursor.getString(cursor
						.getColumnIndex((KEY_TEAM_SHIELD1)));

			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
		return shield;
	}

	public String getTeamNameFromPlayer(Integer playerId) {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		String selectQuery = "SELECT " + TABLE_TEAM + "." + KEY_TEAM_SHORTNAME
				+ " FROM " + TABLE_TEAM + ", " + TABLE_PLAYER + " where "
				+ TABLE_TEAM + "." + KEY_TEAM_ID + " like " + TABLE_PLAYER
				+ "." + KEY_PLAYER_TEAM_ID + " and " + TABLE_PLAYER + "."
				+ KEY_PLAYER_ID + " = " + playerId;

		String name = null;

		Cursor cursor = null;
		try {
			cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {

				name = cursor.getString(cursor
						.getColumnIndex((KEY_TEAM_SHORTNAME)));

			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
		return name;

	}

	/****************************************** TEAM *****************************************************/
	/****************************************** TEAMHISTORICALPALMARES *****************************************************/
	/**
	 * 
	 * @param db
	 * @param teamId
	 * @param historicalPalmares
	 * @throws Exception
	 */
	private void insertOrUpdateHistoricalPalmaresTeam(SQLiteDatabase db,
			String teamId, String competition,
			ArrayList<String> historicalPalmares) throws Exception {
		db.delete(TABLE_HISTORCAL_PALMARES_TEAM, KEY_HISTORCAL_PALMARES_TEAM_ID
				+ " = ? ", new String[] { String.valueOf(teamId) });
		ContentValues values;

		for (int i = 0; i < historicalPalmares.size(); i++) {
			values = new ContentValues();
			values.put(KEY_HISTORCAL_PALMARES_TEAM_ID, teamId);
			values.put(KEY_HISTORCAL_PALMARES_TEAM_COMPETITION, competition);
			values.put(KEY_HISTORCAL_PALMARES_TEAM_ORDER, i);
			values.put(KEY_HISTORCAL_PALMARES_TEAM_POSITION,
					historicalPalmares.get(i));
			try {
				db.insert(TABLE_HISTORCAL_PALMARES_TEAM, null, values);
			} catch (SQLiteException e) {
				throw new Exception("Error al actualizar db: " + e.getMessage());
			} catch (Exception e) {
				throw new Exception("Error al actualizar db: " + e.getMessage());
			}
		}

	}

	public ArrayList<String> getPalmaresTeamHistoricoYears(
			String competitionId, String currentTeamId) {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_HISTORCAL_PALMARES_TEAM
				+ " where " + KEY_HISTORCAL_PALMARES_TEAM_COMPETITION
				+ " like " + competitionId + " and "
				+ KEY_HISTORCAL_PALMARES_TEAM_ID + " like " + currentTeamId
				+ " order by " + KEY_HISTORCAL_PALMARES_TEAM_ORDER + " desc";

		Cursor cursor = null;
		try {

			ArrayList<String> labels = new ArrayList<String>();
			cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					labels.add(cursor.getString(cursor
							.getColumnIndex(KEY_HISTORCAL_PALMARES_TEAM_POSITION)));
				} while (cursor.moveToNext());
			}
			return labels;
		} catch (Exception e) {
			Log.e(TAG,
					"Se ha producido un error al obtener el Count: "
							+ e.getMessage());
			return null;
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
	}

	/****************************************** TEAMHISTORICALPALMARES *****************************************************/
	/****************************************** TEAMPALMARES *****************************************************/
	/**
	 * @param db
	 * @param id
	 * @param palmares
	 */
	private void insertOrUpdatePalmares(SQLiteDatabase db, String teamId,
			ArrayList<TituloTeam> palmares) throws Exception {

		db.delete(TABLE_TEAM_PALMARES, KEY_TEAM_PALMARES_ID + " = ? ",
				new String[] { String.valueOf(teamId) });
		ContentValues values;
		for (TituloTeam titulo : palmares) {
			values = new ContentValues();
			values.put(KEY_TEAM_PALMARES_ID, teamId);
			values.put(KEY_TEAM_PALMARES_NAME, titulo.getName());
			for (String year : titulo.getYear()) {
				values.put(KEY_TEAM_PALMARES_YEAR, year);
				try {
					db.insert(TABLE_TEAM_PALMARES, null, values);
				} catch (SQLiteException e) {
					throw new Exception("Error al actualizar db: "
							+ e.getMessage());
				}
			}

		}
	}

	private ArrayList<TituloTeam> getTeamPalmares(String currentTeamId,
			SQLiteDatabase db) {
		ArrayList<TituloTeam> palmares = new ArrayList<TituloTeam>();
		String selectQuery;

		selectQuery = "SELECT * FROM " + TABLE_TEAM_PALMARES + " where "
				+ KEY_TEAM_PALMARES_ID + " = " + currentTeamId;

		Cursor cursor2 = null;
		try {
			cursor2 = db.rawQuery(selectQuery, null);
			if (cursor2.moveToFirst()) {
				String title = cursor2.getString(cursor2
						.getColumnIndex(KEY_TEAM_PALMARES_NAME));
				TituloTeam titulo = new TituloTeam(title);
				do {
					if (!(cursor2.getString(cursor2
							.getColumnIndex(KEY_TEAM_PALMARES_NAME))
							.equalsIgnoreCase(title))) {
						palmares.add(titulo);
						title = cursor2.getString(cursor2
								.getColumnIndex(KEY_TEAM_PALMARES_NAME));
						titulo = new TituloTeam(title);
					}
					titulo.addYear(cursor2.getString(cursor2
							.getColumnIndex(KEY_TEAM_PALMARES_YEAR)));
					// team.addTitle(cursor2.getString(cursor2.getColumnIndex(KEY_PALMARES_NAME)),
					// cursor2.getString(cursor2.getColumnIndex(KEY_PALMARES_YEAR)));
				} while (cursor2.moveToNext());
				palmares.add(titulo);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor2 != null && !cursor2.isClosed()) {
				cursor2.close();
				cursor2 = null;
			}
		}

		return palmares;
	}

	/****************************************** TEAMPALMARES *****************************************************/
	/****************************************** STAFF *****************************************************/
	private void insertOrUpdateStaticStaff(SQLiteDatabase db, String teamId,
			HashMap<String, Staff> teamStaff) throws Exception {
		Staff staff;
		Set<String> keys = teamStaff.keySet();
		ContentValues values;
		db.delete(TABLE_STAFF, KEY_STAFF_ID + " = ?",
				new String[] { String.valueOf(teamId) });
		Cursor cursor = null;
		for (String key : keys) {
			staff = teamStaff.get(key);
			values = new ContentValues();
			values.put(KEY_STAFF_ID, teamId);
			values.put(KEY_STAFF_NAME, staff.getName());
			values.put(KEY_STAFF_CHARGE, staff.getCharge());
			values.put(KEY_STAFF_HISTORY, staff.getHistory());
			// values.put(KEY_STAFF_BORN, staff.getBorn());
			// values.put(KEY_STAFF_CONTRACT, staff.getContract());
			values.put(KEY_STAFF_PHOTO, staff.getPhoto());
			if (staff instanceof Star) {
				values.put(KEY_STAFF_POSITION, ((Star) staff).getPosition());
				values.put(KEY_STAFF_NUMINTERNATIONAL,
						((Star) staff).getNumInternational());
				values.put(KEY_STAFF_AGE, ((Star) staff).getAge());
				values.put(KEY_STAFF_STATURE, ((Star) staff).getStature());
				values.put(KEY_STAFF_WEIGHT, ((Star) staff).getWeight());
				values.put(KEY_STAFF_CLUBNAME, ((Star) staff).getClubName());
				values.put(KEY_STAFF_CLUBSHIELD, ((Star) staff).getClubShield());
				values.put(KEY_STAFF_URL, ((Star) staff).getUrl());
				values.put(KEY_STAFF_PLAYERID, ((Star) staff).getPlayerId());
			}
			try {
				String selectQuery = "Select 1 from " + TABLE_STAFF + " where "
						+ KEY_STAFF_ID + " = " + teamId + " and "
						+ KEY_STAFF_NAME + " like \"" + staff.getName() + "\"";
				cursor = db.rawQuery(selectQuery, null);
				if (cursor.getCount() > 0) {
					String whereClause = KEY_STAFF_ID + " = ? and "
							+ KEY_STAFF_NAME + " like ?";
					db.update(TABLE_STAFF, values, whereClause, new String[] {
							String.valueOf(teamId), staff.getName() });
				} else {
					db.insert(TABLE_STAFF, null, values);
				}

			} catch (SQLiteException e) {
				throw new Exception("Error al actualizar db: " + e.getMessage());
			} finally {
				if (cursor != null && !cursor.isClosed()) {
					cursor.close();
					cursor = null;
				}
			}
		}

	}

	private HashMap<String, Staff> getStaff(String currentTeamId,
			SQLiteDatabase db) {

		HashMap<String, Staff> personalStaff = new HashMap<String, Staff>();
		String selectQuery;
		selectQuery = "SELECT * FROM " + TABLE_STAFF + " where " + KEY_STAFF_ID
				+ " = " + currentTeamId;

		Cursor cursor2 = null;
		Staff staff;
		try {
			cursor2 = db.rawQuery(selectQuery, null);
			if (cursor2.moveToFirst()) {
				do {
					String charge = cursor2.getString(cursor2
							.getColumnIndex(KEY_STAFF_CHARGE));

					if (charge.equalsIgnoreCase(Defines.StaffCharge.STAR)) {
						staff = new Star();
						((Star) staff).setPosition(cursor2.getString(cursor2
								.getColumnIndex(KEY_STAFF_POSITION)));
						((Star) staff)
								.setNumInternational(cursor2.getString(cursor2
										.getColumnIndex(KEY_STAFF_NUMINTERNATIONAL)));
						((Star) staff).setAge(cursor2.getString(cursor2
								.getColumnIndex(KEY_STAFF_AGE)));
						((Star) staff).setStature(cursor2.getString(cursor2
								.getColumnIndex(KEY_STAFF_STATURE)));
						((Star) staff).setWeight(cursor2.getString(cursor2
								.getColumnIndex(KEY_STAFF_WEIGHT)));
						((Star) staff).setClubName(cursor2.getString(cursor2
								.getColumnIndex(KEY_STAFF_CLUBNAME)));
						((Star) staff).setClubShield(cursor2.getString(cursor2
								.getColumnIndex(KEY_STAFF_CLUBSHIELD)));
						((Star) staff).setUrl(cursor2.getString(cursor2
								.getColumnIndex(KEY_STAFF_URL)));
						((Star) staff).setPlayerId(cursor2.getString(cursor2
								.getColumnIndex(KEY_STAFF_PLAYERID)));
						if (((Star) staff).getUrl() == null) {
							String id = ((Star) staff).getPlayerId();
							Player play = getPlayer(Integer.valueOf(id));
							((Star) staff).setUrl(play.getUrl());
						}

					} else {
						staff = new Staff();
					}
					staff.setName(cursor2.getString(cursor2
							.getColumnIndex(KEY_STAFF_NAME)));
					staff.setCharge(charge);
					staff.setHistory(cursor2.getString(cursor2
							.getColumnIndex(KEY_STAFF_HISTORY)));
					staff.setPhoto(cursor2.getString(cursor2
							.getColumnIndex(KEY_STAFF_PHOTO)));
					// staff.setBorn(cursor2.getString(cursor2.getColumnIndex(KEY_STAFF_BORN)));
					// staff.setContract(cursor2.getString(cursor2.getColumnIndex(KEY_STAFF_CONTRACT)));
					personalStaff.put(staff.getCharge(), staff);
				} while (cursor2.moveToNext());
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor2 != null && !cursor2.isClosed()) {
				cursor2.close();
				cursor2 = null;
			}
		}

		return personalStaff;
	}

	/****************************************** STAFF *****************************************************/
	/****************************************** PLAYER *****************************************************/

	/**
	 * @param currentTeamId
	 * @param currentPlayerId
	 * @return
	 */
	public Player getPlayer(int currentPlayerId) {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_PLAYER + " where "
				+ KEY_PLAYER_ID + " = " + currentPlayerId;

		Cursor cursor = null;
		Cursor cursor2 = null;
		Player player = new Player();
		try {
			cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					player.setId(cursor.getInt(cursor
							.getColumnIndex(KEY_PLAYER_ID)));
					player.setIdTeam(cursor.getString(cursor
							.getColumnIndex(KEY_PLAYER_TEAM_ID)));
					player.setNameTeam(cursor.getString(cursor
							.getColumnIndex(KEY_PLAYER_TEAM_NAME)));
					player.setName(cursor.getString(cursor
							.getColumnIndex(KEY_PLAYER_NAME)));
					player.setShortName(cursor.getString(cursor
							.getColumnIndex(KEY_PLAYER_SHORTNAME)));
					player.setUrl(cursor.getString(cursor
							.getColumnIndex((KEY_PLAYER_URL))));
					player.setUrlFoto(cursor.getString(cursor
							.getColumnIndex((KEY_PLAYER_URL_FOTO))));
					player.setUrlFicha(cursor.getString(cursor
							.getColumnIndex((KEY_PLAYER_URL_FICHA))));
					player.setUrlTag(cursor.getString(cursor
							.getColumnIndex((KEY_PLAYER_URL_TAG))));
					player.setDorsal(cursor.getInt(cursor
							.getColumnIndex(KEY_PLAYER_DORSAL)));
					player.setDemarcation(cursor.getString(cursor
							.getColumnIndex(KEY_PLAYER_DEMARCACION)));
					player.setPosition(cursor.getInt(cursor
							.getColumnIndex(KEY_PLAYER_POSITION)));
					player.setAge(cursor.getInt(cursor
							.getColumnIndex(KEY_PLAYER_AGE)));
					player.setDateBorn(cursor.getString(cursor
							.getColumnIndex(KEY_PLAYER_DATEBORN)));
					player.setFecModificacion(cursor.getInt(cursor
							.getColumnIndex(KEY_PLAYER_FECMOD)));
					player.setHeight(cursor.getInt(cursor
							.getColumnIndex(KEY_PLAYER_HEIGHT)));
					player.setWeight(cursor.getInt(cursor
							.getColumnIndex(KEY_PLAYER_WEIGHT)));
					player.setNacionality(cursor.getString(cursor
							.getColumnIndex(KEY_PLAYER_NACIONALITY)));
					player.setCompeticiones(cursor.getString(cursor
							.getColumnIndex(KEY_PLAYER_COMPETICIONES)));
					// player.setTeamId(cursor.getInt(cursor.getColumnIndex(KEY_PLAYER_TEAM_ID)));

				} while (cursor.moveToNext());

			}
			if (player.getId() != 0) {
				// Se recupera el palmares
				selectQuery = "SELECT * FROM " + TABLE_PLAYER_PALMARES
						+ " where " + KEY_PLAYER_PALMARES_ID + " = "
						+ currentPlayerId;
				cursor2 = db.rawQuery(selectQuery, null);
				if (cursor2.moveToFirst()) {
					String title = cursor2.getString(cursor2
							.getColumnIndex(KEY_PLAYER_PALMARES_NAME));
					String team;
					TituloPlayer titulo = new TituloPlayer(title);
					do {
						team = cursor2.getString(cursor2
								.getColumnIndex(KEY_PLAYER_PALMARES_TEAM));
						if (!(cursor2.getString(cursor2
								.getColumnIndex(KEY_PLAYER_PALMARES_NAME))
								.equalsIgnoreCase(title))) {
							titulo.calculateNumTitles();
							player.addTitle(titulo);
							title = cursor2.getString(cursor2
									.getColumnIndex(KEY_PLAYER_PALMARES_NAME));

							titulo = new TituloPlayer(title);
						}
						titulo.addYear(team, cursor2.getString(cursor2
								.getColumnIndex(KEY_PLAYER_PALMARES_YEAR)));

					} while (cursor2.moveToNext());
					titulo.calculateNumTitles();
					player.addTitle(titulo);
				}

				// Se recupera la trayectoria
				selectQuery = "SELECT * FROM " + TABLE_PLAYER_TRAYECTORIA
						+ " where " + KEY_PLAYER_TRAYECTORIA_ID + " = "
						+ currentPlayerId + " order by "
						+ KEY_PLAYER_TRAYECTORIA_YEAR + " desc";
				cursor2 = db.rawQuery(selectQuery, null);
				if (cursor2.moveToFirst()) {
					int year = cursor2.getInt(cursor2
							.getColumnIndex(KEY_PLAYER_TRAYECTORIA_YEAR));
					String descripcion = cursor2
							.getString(cursor2
									.getColumnIndex(KEY_PLAYER_TRAYECTORIA_DESCRIPCION));
					Trayectoria trayectoria = new Trayectoria(year, descripcion);
					do {
						if (year != cursor2.getInt(cursor2
								.getColumnIndex(KEY_PLAYER_TRAYECTORIA_YEAR))) {
							player.addTeamToTrayectoria(trayectoria);
							year = cursor2
									.getInt(cursor2
											.getColumnIndex(KEY_PLAYER_TRAYECTORIA_YEAR));
							descripcion = cursor2
									.getString(cursor2
											.getColumnIndex(KEY_PLAYER_TRAYECTORIA_DESCRIPCION));
							trayectoria = new Trayectoria(year, descripcion);
						}
						trayectoria
								.addEquipo(
										cursor2.getString(cursor2
												.getColumnIndex(KEY_PLAYER_TRAYECTORIA_TEAM)),
										cursor2.getInt(cursor2
												.getColumnIndex(KEY_PLAYER_TRAYECTORIA_PARTIDOS)),
										cursor2.getInt(cursor2
												.getColumnIndex(KEY_PLAYER_TRAYECTORIA_GOLES)));

					} while (cursor2.moveToNext());
					player.addTeamToTrayectoria(trayectoria);
				}

				// Se recuperan las estadisticas
				selectQuery = "SELECT * FROM " + TABLE_PLAYER_ESTADISTICAS
						+ " where " + KEY_PLAYER_ESTADISTICAS_ID + " = "
						+ currentPlayerId + " order by "
						+ KEY_PLAYER_ESTADISTICAS_YEAR;
				cursor2 = db.rawQuery(selectQuery, null);
				if (cursor2.moveToFirst()) {
					String year = cursor2.getString(cursor2
							.getColumnIndex(KEY_PLAYER_ESTADISTICAS_YEAR));

					PlayerStats stats = new PlayerStats(year);
					do {
						if (!year.equalsIgnoreCase(cursor2.getString(cursor2
								.getColumnIndex(KEY_PLAYER_ESTADISTICAS_YEAR)))) {
							player.addStats(year, stats);
							year = cursor2
									.getString(cursor2
											.getColumnIndex(KEY_PLAYER_TRAYECTORIA_YEAR));

							stats = new PlayerStats(year);
						}
						stats.addStat(
								cursor2.getString(cursor2
										.getColumnIndex(KEY_PLAYER_ESTADISTICAS_COMP)),
								cursor2.getString(cursor2
										.getColumnIndex(KEY_PLAYER_ESTADISTICAS_TEAM)),
								cursor2.getInt(cursor2
										.getColumnIndex(KEY_PLAYER_ESTADISTICAS_GOLES)),
								cursor2.getInt(cursor2
										.getColumnIndex(KEY_PLAYER_ESTADISTICAS_GOLES_ENC)),
								cursor2.getInt(cursor2
										.getColumnIndex(KEY_PLAYER_ESTADISTICAS_PARTIDOS)),
								cursor2.getInt(cursor2
										.getColumnIndex(KEY_PLAYER_ESTADISTICAS_MINUTOS)),
								cursor2.getInt(cursor2
										.getColumnIndex(KEY_PLAYER_ESTADISTICAS_TARJETASAMARILLAS)),
								cursor2.getInt(cursor2
										.getColumnIndex(KEY_PLAYER_ESTADISTICAS_TARJETASROJAS)),
								cursor2.getInt(cursor2
										.getColumnIndex(KEY_PLAYER_ESTADISTICAS_PARADAS)),
								cursor2.getInt(cursor2
										.getColumnIndex(KEY_PLAYER_ESTADISTICAS_ASISTENCIAS)));

					} while (cursor2.moveToNext());
					player.addStats(year, stats);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
			if (cursor2 != null && !cursor2.isClosed()) {
				cursor2.close();
				cursor2 = null;
			}
		}

		return player;
	}

	/**
	 * @param playerFinal
	 */
	public void updatePlayer(Player player) {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		try {
			db.beginTransaction();

			insertOrUpdatePlayer(db, player);
			insertOrUpdatePlayerPalmares(db, player.getId(),
					player.getPalmares());
			insertOrUpdatePlayerTrayectoria(db, player.getId(),
					player.getTrayectoria());
			insertOrUpdatePlayerEstadisticas(db, player.getId(),
					player.getStats());

			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	/**
	 * @param db
	 * @param player
	 * @throws Exception
	 */
	private void insertOrUpdatePlayer(SQLiteDatabase db, Player player)
			throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_PLAYER_ID, player.getId());
		values.put(KEY_PLAYER_URL, player.getUrl());
		values.put(KEY_PLAYER_URL_FOTO, player.getUrlFoto());
		values.put(KEY_PLAYER_URL_FICHA, player.getUrlFicha());
		values.put(KEY_PLAYER_URL_TAG, player.getUrlTag());
		values.put(KEY_PLAYER_TEAM_ID, player.getIdTeam());
		values.put(KEY_PLAYER_TEAM_NAME, player.getNameTeam());
		values.put(KEY_PLAYER_NAME, player.getName());
		values.put(KEY_PLAYER_SHORTNAME, player.getShortName());
		values.put(KEY_PLAYER_DORSAL, player.getDorsal());
		values.put(KEY_PLAYER_DEMARCACION, player.getDemarcation());
		// values.put(KEY_PLAYER_POSITION, player.getPosition());
		values.put(KEY_PLAYER_WEIGHT, player.getWeight());
		values.put(KEY_PLAYER_HEIGHT, player.getHeight());
		values.put(KEY_PLAYER_NACIONALITY, player.getNacionality());
		values.put(KEY_PLAYER_AGE, player.getAge());
		values.put(KEY_PLAYER_DATEBORN, player.getDateBorn());
		values.put(KEY_PLAYER_FECMOD, player.getFecModificacion());
		values.put(KEY_PLAYER_COMPETICIONES, player.getCompeticiones());
		Cursor cursor = null;
		try {

			String selectQuery = "Select 1 from " + TABLE_PLAYER + " where "
					+ KEY_PLAYER_ID + " = " + player.getId();

			cursor = db.rawQuery(selectQuery, null);
			if (cursor.getCount() > 0) {
				db.update(TABLE_PLAYER, values, KEY_PLAYER_ID + " = ?",
						new String[] { String.valueOf(player.getId()) });
			} else {
				db.insert(TABLE_PLAYER, null, values);
			}

		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
	}

	/**
	 * @param db
	 * @param player
	 * @throws Exception
	 */
	private void insertOrUpdatePlayerPalmares(SQLiteDatabase db, int playerId,
			ArrayList<TituloPlayer> palmares) throws Exception {
		int num = db.delete(TABLE_PLAYER_PALMARES, KEY_PLAYER_PALMARES_ID
				+ " = ? ", new String[] { String.valueOf(playerId) });
		Log.d("Palmares", "Borrando: " + num);
		ContentValues values;
		String team;
		for (TituloPlayer titulo : palmares) {
			values = new ContentValues();
			values.put(KEY_PLAYER_PALMARES_ID, playerId);
			values.put(KEY_PLAYER_PALMARES_NAME, titulo.getName());

			for (Iterator<String> iterator = titulo.getYears().keySet()
					.iterator(); iterator.hasNext();) {
				team = iterator.next();
				values.put(KEY_PLAYER_PALMARES_TEAM, team);
				for (String year : titulo.getYears(team)) {
					values.put(KEY_PLAYER_PALMARES_YEAR, year);
					try {
						db.insert(TABLE_PLAYER_PALMARES, null, values);
					} catch (SQLiteException e) {
						throw new Exception("Error al actualizar db: "
								+ e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * @param db
	 * @param id
	 * @param trayectoria
	 * @throws Exception
	 */
	private void insertOrUpdatePlayerTrayectoria(SQLiteDatabase db,
			int playerId, ArrayList<Trayectoria> trayectoria) throws Exception {
		db.delete(TABLE_PLAYER_TRAYECTORIA,
				KEY_PLAYER_TRAYECTORIA_ID + " = ? ",
				new String[] { String.valueOf(playerId) });
		ContentValues values;
		ItemTrayectoria team;
		for (Trayectoria tray : trayectoria) {
			values = new ContentValues();
			values.put(KEY_PLAYER_TRAYECTORIA_ID, playerId);
			values.put(KEY_PLAYER_TRAYECTORIA_YEAR, tray.getYear());
			values.put(KEY_PLAYER_TRAYECTORIA_DESCRIPCION,
					tray.getDescripcion());

			for (Iterator iterator = tray.getEquipos().iterator(); iterator
					.hasNext();) {
				team = (ItemTrayectoria) iterator.next();
				values.put(KEY_PLAYER_TRAYECTORIA_TEAM, team.getNombre());
				values.put(KEY_PLAYER_TRAYECTORIA_PARTIDOS, team.getPartidos());
				values.put(KEY_PLAYER_TRAYECTORIA_GOLES, team.getGoles());
				try {
					db.insert(TABLE_PLAYER_TRAYECTORIA, null, values);
				} catch (SQLiteException e) {
					throw new Exception("Error al actualizar db: "
							+ e.getMessage());
				}
			}
		}

	}

	/**
	 * @param db
	 * @param id
	 * @param stats
	 * @throws Exception
	 */
	private void insertOrUpdatePlayerEstadisticas(SQLiteDatabase db,
			int playerId, HashMap<String, PlayerStats> stats) throws Exception {
		db.delete(TABLE_PLAYER_ESTADISTICAS, KEY_PLAYER_ESTADISTICAS_ID
				+ " = ? ", new String[] { String.valueOf(playerId) });
		ContentValues values;
		String year, comp, team;
		ItemStats sta;
		for (Iterator<String> iterator = stats.keySet().iterator(); iterator
				.hasNext();) {
			year = iterator.next();
			values = new ContentValues();
			values.put(KEY_PLAYER_ESTADISTICAS_ID, playerId);
			values.put(KEY_PLAYER_ESTADISTICAS_YEAR, year);

			for (Iterator<String> iter = stats.get(year).getStats().keySet()
					.iterator(); iter.hasNext();) {
				comp = iter.next();
				values.put(KEY_PLAYER_ESTADISTICAS_COMP, comp);
				sta = stats.get(year).getStats(comp);
				for (Iterator<String> it = stats.get(year).getStats(comp)
						.getData().keySet().iterator(); it.hasNext();) {
					team = it.next();

					values.put(KEY_PLAYER_ESTADISTICAS_TEAM, team);
					values.put(KEY_PLAYER_ESTADISTICAS_PARTIDOS,
							sta.getPartidosJugados(team));
					values.put(KEY_PLAYER_ESTADISTICAS_MINUTOS,
							sta.getMinutos(team));
					values.put(KEY_PLAYER_ESTADISTICAS_GOLES,
							sta.getGolesMarcados(team));
					values.put(KEY_PLAYER_ESTADISTICAS_GOLES_ENC,
							sta.getGolesEncajados(team));
					values.put(KEY_PLAYER_ESTADISTICAS_TARJETASAMARILLAS,
							sta.getTarjetasAmarillas(team));
					values.put(KEY_PLAYER_ESTADISTICAS_TARJETASROJAS,
							sta.getTarjetasRojas(team));
					values.put(KEY_PLAYER_ESTADISTICAS_PARADAS,
							sta.getParadas(team));
					values.put(KEY_PLAYER_ESTADISTICAS_ASISTENCIAS,
							sta.getAsistencias(team));
					try {
						db.insert(TABLE_PLAYER_ESTADISTICAS, null, values);
					} catch (SQLiteException e) {
						throw new Exception("Error al actualizar db: "
								+ e.getMessage());
					}
				}

			}

		}

	}

	/****************************************** PLAYER *****************************************************/
	/****************************************** TEAMSTATS *****************************************************/
	/**
	 * @param db
	 * @param id
	 * @param stats
	 * @throws Exception
	 */
	private void insertOrUpdateTeamEstadisticas(SQLiteDatabase db,
			String teamId, HashMap<String, TeamStats> stats) throws Exception {

		db.delete(TABLE_TEAM_ESTADISTICAS, KEY_TEAM_ESTADISTICAS_ID + " = ? ",
				new String[] { String.valueOf(teamId) });
		ContentValues values;
		Set<String> years = stats.keySet();
		Set<String> competitions;
		TeamStats statsYear;

		for (String year : years) {
			statsYear = stats.get(year);
			competitions = statsYear.getStats().keySet();
			for (String comp : competitions) {
				values = new ContentValues();
				values.put(KEY_TEAM_ESTADISTICAS_ID, teamId);
				values.put(KEY_TEAM_ESTADISTICAS_YEAR, year);
				values.put(KEY_TEAM_ESTADISTICAS_COMPETITION, comp);
				values.put(KEY_TEAM_ESTADISTICAS_GOLES,
						statsYear.getGolesAFavor(comp));
				values.put(KEY_TEAM_ESTADISTICAS_PARTIDOSJUGADOS,
						statsYear.getPartidosJugados(comp));
				values.put(KEY_TEAM_ESTADISTICAS_PARTIDOSGANADOS,
						statsYear.getPartidosGanados(comp));
				values.put(KEY_TEAM_ESTADISTICAS_PARTIDOSEMPATADOS,
						statsYear.getPartidosEmpatados(comp));
				values.put(KEY_TEAM_ESTADISTICAS_PARTIDOSPERDIDOS,
						statsYear.getPartidosPerdidos(comp));
				values.put(KEY_TEAM_ESTADISTICAS_TARJETASAMARILLAS,
						statsYear.getTarjetasAmarillas(comp));
				values.put(KEY_TEAM_ESTADISTICAS_TARJETASROJAS,
						statsYear.getTarjetasRojas(comp));
				try {
					db.insert(TABLE_TEAM_ESTADISTICAS, null, values);
				} catch (SQLiteException e) {
					throw new Exception("Error al actualizar db: "
							+ e.getMessage());
				}
			}
		}

	}

	private HashMap<String, TeamStats> getStats(String currentTeamId,
			SQLiteDatabase db) {

		HashMap<String, TeamStats> stats = new HashMap<String, TeamStats>();
		String selectQuery;

		selectQuery = "SELECT * FROM " + TABLE_TEAM_ESTADISTICAS + " where "
				+ KEY_TEAM_ESTADISTICAS_ID + " = " + currentTeamId
				+ " order by " + KEY_TEAM_ESTADISTICAS_YEAR;

		Cursor cursor2 = null;
		try {
			cursor2 = db.rawQuery(selectQuery, null);
			if (cursor2.moveToFirst()) {
				String year = null, yearold = null, comp;
				TeamStats stat = null;
				boolean first = true;
				do {

					year = cursor2.getString(cursor2
							.getColumnIndex(KEY_TEAM_ESTADISTICAS_YEAR));
					// Se crea una nueva stat cuando cambia el a–o
					if (first) {
						stat = new TeamStats();
						yearold = year;
						first = false;
					}

					if (!year.equalsIgnoreCase(yearold)) {
						stats.put(yearold, stat);
						stat = new TeamStats();
						yearold = year;
					}

					comp = cursor2.getString(cursor2
							.getColumnIndex(KEY_TEAM_ESTADISTICAS_COMPETITION));

					stat.setGolesAFavor(comp, cursor2.getInt(cursor2
							.getColumnIndex(KEY_TEAM_ESTADISTICAS_GOLES)));
					stat.setPartidosJugados(
							comp,
							cursor2.getInt(cursor2
									.getColumnIndex(KEY_TEAM_ESTADISTICAS_PARTIDOSJUGADOS)));
					stat.setPartidosGanados(
							comp,
							cursor2.getInt(cursor2
									.getColumnIndex(KEY_TEAM_ESTADISTICAS_PARTIDOSGANADOS)));
					stat.setPartidosEmpatados(
							comp,
							cursor2.getInt(cursor2
									.getColumnIndex(KEY_TEAM_ESTADISTICAS_PARTIDOSEMPATADOS)));
					stat.setPartidosPerdidos(
							comp,
							cursor2.getInt(cursor2
									.getColumnIndex(KEY_TEAM_ESTADISTICAS_PARTIDOSPERDIDOS)));
					stat.setTarjetasAmarillas(
							comp,
							cursor2.getInt(cursor2
									.getColumnIndex(KEY_TEAM_ESTADISTICAS_TARJETASAMARILLAS)));
					stat.setTarjetasRojas(
							comp,
							cursor2.getInt(cursor2
									.getColumnIndex(KEY_TEAM_ESTADISTICAS_TARJETASROJAS)));

				} while (cursor2.moveToNext());
				stats.put(year, stat);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "Se ha producido un error: " + e.getMessage());
		} finally {
			if (cursor2 != null && !cursor2.isClosed()) {
				cursor2.close();
				cursor2 = null;
			}
		}
		return stats;
	}

	/****************************************** TEAMSTATS *****************************************************/
	/****************************************** STADIUMS *****************************************************/
	public void updateStaticStadiums(String competitionId,
			ArrayList<Stadium> stadiums) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		try {
			db.beginTransaction();

			String idsActivos = "";
			for (Stadium stadium : stadiums) {
				idsActivos += stadium.getId() + ",";
				insertStadiumGenericInfo(db, competitionId, stadium);
			}

			deleteOldStadiums(db, idsActivos, competitionId);

			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	/**
	 * @param db
	 * @param idsActivos
	 * @param competition
	 * @throws Exception
	 */
	private void deleteOldStadiums(SQLiteDatabase db, String idsActivos,
			String competition) throws Exception {
		String[] ids = idsActivos.split(",");
		String sql = "delete from " + TABLE_COMPETITION_STADIUM + " where "
				+ KEY_COMPETITION_STADIUM_COMPETITION_ID + " = \""
				+ competition + "\"";
		if (ids.length > 0) {
			sql += " and ";
			for (String id : ids) {
				sql += KEY_COMPETITION_STADIUM_STADIUM_ID + " not like \"" + id
						+ "\" and ";
			}
			sql = sql.substring(0, sql.length() - 4);
			db.rawQuery(sql, null);
		}

	}

	private void insertStadiumGenericInfo(SQLiteDatabase db,
			String competitionId, Stadium stadium) throws Exception {

		ContentValues values = new ContentValues();
		values.put(KEY_STADIUM_ID, stadium.getId());
		values.put(KEY_STADIUM_STADIUM_NAME, stadium.getStadiumName());
		values.put(KEY_STADIUM_URL_PHOTO, stadium.getPhoto());
		values.put(KEY_STADIUM_URL, stadium.getUrlInfo());
		values.put(KEY_STADIUM_CITY_NAME, stadium.getCityName());

		Cursor cursor = null;
		try {
			String selectQuery = "SELECT * FROM " + TABLE_STADIUM + " where "
					+ KEY_STADIUM_ID + " = " + stadium.getId();
			cursor = db.rawQuery(selectQuery, null);
			if (cursor.getCount() > 0) {
				String whereClause = KEY_STADIUM_ID + " = ?";
				db.update(TABLE_STADIUM, values, whereClause,
						new String[] { String.valueOf(stadium.getId()) });
			} else {
				ContentValues values2 = new ContentValues();
				values2.put(KEY_COMPETITION_STADIUM_COMPETITION_ID,
						competitionId);
				values2.put(KEY_COMPETITION_STADIUM_STADIUM_ID, stadium.getId());
				db.insert(TABLE_COMPETITION_STADIUM, null, values2);

				db.insert(TABLE_STADIUM, null, values);
			}

		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} catch (Exception e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

	}

	public ArrayList<Stadium> getStadiums(String competitionId) {
		ArrayList<Stadium> stadiums = null;

		if (competitionId != null && competitionId.length() > 0) {
			SQLiteDatabase db = sInstance.getWritableDatabase();

			String selectQuery = "SELECT * FROM " + TABLE_STADIUM + ", "
					+ TABLE_COMPETITION_STADIUM + " where " + TABLE_STADIUM
					+ "." + KEY_STADIUM_ID + " = " + TABLE_COMPETITION_STADIUM
					+ "." + KEY_COMPETITION_STADIUM_STADIUM_ID + " and "
					+ TABLE_COMPETITION_STADIUM + "."
					+ KEY_COMPETITION_STADIUM_COMPETITION_ID + " like "
					+ competitionId + " order by " + KEY_STADIUM_ID;

			Cursor cursor = null;

			Stadium stadium;
			try {
				cursor = db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst()) {
					stadiums = new ArrayList<Stadium>();
					do {
						stadium = new Stadium();
						stadium.setId(cursor.getInt(cursor
								.getColumnIndex(KEY_STADIUM_ID)));
						stadium.setUrlInfo(cursor.getString(cursor
								.getColumnIndex(KEY_STADIUM_URL)));
						stadium.setPhoto(cursor.getString(cursor
								.getColumnIndex(KEY_STADIUM_URL_PHOTO)));
						stadium.setStadiumName(cursor.getString(cursor
								.getColumnIndex(KEY_STADIUM_STADIUM_NAME)));
						stadium.setCityName(cursor.getString(cursor
								.getColumnIndex(KEY_STADIUM_CITY_NAME)));
						stadiums.add(stadium);
					} while (cursor.moveToNext());
				}

			} catch (Exception e) {
				Log.e(TAG, "Se ha producido un error al obtener el Splash: "
						+ e.getMessage());
			} finally {
				if (cursor != null && !cursor.isClosed()) {
					cursor.close();
					cursor = null;
				}
			}
		}
		return stadiums;

	}

	public void updateStatiStadium(Stadium stadium) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		try {
			db.beginTransaction();
			updateStadium(db, stadium);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	private void updateStadium(SQLiteDatabase db, Stadium stadium)
			throws Exception {

		ContentValues values = new ContentValues();
		values.put(KEY_STADIUM_ID, stadium.getId());
		values.put(KEY_STADIUM_URL_PHOTO, stadium.getPhoto());
		values.put(KEY_STADIUM_URL, stadium.getUrlInfo());
		values.put(KEY_STADIUM_STADIUM_NAME, stadium.getStadiumName());
		values.put(KEY_STADIUM_STADIUM_HISTORY, stadium.getStadiumHistory());
		values.put(KEY_STADIUM_STADIUM_MAP, stadium.getStadiumMap());
		values.put(KEY_STADIUM_STADIUM_CAPACITY, stadium.getStadiumCapacity());
		values.put(KEY_STADIUM_STADIUM_YEAR, stadium.getStadiumYear());
		values.put(KEY_STADIUM_CITY_NAME, stadium.getCityName());
		values.put(KEY_STADIUM_CITY_HISTORY, stadium.getCityHistory());
		values.put(KEY_STADIUM_CITY_ALTITUDE, stadium.getCityAltitude());
		values.put(KEY_STADIUM_CITY_POPULATION, stadium.getCityPopulation());
		values.put(KEY_STADIUM_CITY_STATE, stadium.getCityState());
		values.put(KEY_STADIUM_CITY_TOURISM, stadium.getCityTourism());
		values.put(KEY_STADIUM_CITY_TRANSPORT, stadium.getCityTransport());
		values.put(KEY_STADIUM_CITY_ECONOMY, stadium.getCityEconomy());

		ContentValues values2;

		try {
			String whereClause = KEY_STADIUM_ID + " like ? ";
			String whereClause2 = KEY_STADIUM_IMAGES_ID + " like ? ";
			db.update(TABLE_STADIUM, values, whereClause,
					new String[] { String.valueOf(stadium.getId()) });

			db.delete(TABLE_STADIUM_IMAGES, whereClause2,
					new String[] { String.valueOf(stadium.getId()) });

			for (String photo : stadium.getStadiumPhotos()) {
				values2 = new ContentValues();
				values2.put(KEY_STADIUM_IMAGES_ID, stadium.getId());
				values2.put(KEY_STADIUM_IMAGES_URL, photo);
				values2.put(KEY_STADIUM_IMAGES_TYPE,
						STADIUM_IMAGE_TYPE.TYPE_STADIUM);
				db.insertWithOnConflict(TABLE_STADIUM_IMAGES, BaseColumns._ID,
						values2, SQLiteDatabase.CONFLICT_REPLACE);
			}

			for (String photo : stadium.getCityPhotos()) {
				values2 = new ContentValues();
				values2.put(KEY_STADIUM_IMAGES_ID, stadium.getId());
				values2.put(KEY_STADIUM_IMAGES_URL, photo);
				values2.put(KEY_STADIUM_IMAGES_TYPE,
						STADIUM_IMAGE_TYPE.TYPE_CITY);
				db.insertWithOnConflict(TABLE_STADIUM_IMAGES, BaseColumns._ID,
						values2, SQLiteDatabase.CONFLICT_REPLACE);
			}
		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} catch (Exception e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		}

	}

	public Stadium getStadium(int id) {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_STADIUM + " where "
				+ KEY_STADIUM_ID + " = " + id;

		// String selectQueryImages = "SELECT * FROM " + TABLE_STADIUM_IMAGES
		// + " where " + KEY_STADIUM_IMAGES_ID + " = " + id;

		Cursor cursor = null;
		Stadium stadium = null;
		String type;
		try {
			cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {

				// do {
				stadium = new Stadium();
				stadium.setId(cursor.getInt(cursor
						.getColumnIndex(KEY_STADIUM_ID)));
				stadium.setUrlInfo(cursor.getString(cursor
						.getColumnIndex(KEY_STADIUM_URL)));
				stadium.setPhoto(cursor.getString(cursor
						.getColumnIndex(KEY_STADIUM_URL_PHOTO)));
				stadium.setStadiumName(cursor.getString(cursor
						.getColumnIndex(KEY_STADIUM_STADIUM_NAME)));
				stadium.setStadiumHistory(cursor.getString(cursor
						.getColumnIndex(KEY_STADIUM_STADIUM_HISTORY)));
				stadium.setStadiumMap(cursor.getString(cursor
						.getColumnIndex(KEY_STADIUM_STADIUM_MAP)));
				stadium.setStadiumCapacity(cursor.getInt(cursor
						.getColumnIndex(KEY_STADIUM_STADIUM_CAPACITY)));
				stadium.setStadiumYear(cursor.getInt(cursor
						.getColumnIndex(KEY_STADIUM_STADIUM_YEAR)));
				stadium.setCityName(cursor.getString(cursor
						.getColumnIndex(KEY_STADIUM_CITY_NAME)));
				stadium.setCityHistory(cursor.getString(cursor
						.getColumnIndex(KEY_STADIUM_CITY_HISTORY)));
				stadium.setCityAltitude(cursor.getString(cursor
						.getColumnIndex(KEY_STADIUM_CITY_ALTITUDE)));
				stadium.setCityPopulation(cursor.getString(cursor
						.getColumnIndex(KEY_STADIUM_CITY_POPULATION)));
				stadium.setCityState(cursor.getString(cursor
						.getColumnIndex(KEY_STADIUM_CITY_STATE)));
				stadium.setCityTourism(cursor.getString(cursor
						.getColumnIndex(KEY_STADIUM_CITY_TOURISM)));
				stadium.setCityTransport(cursor.getString(cursor
						.getColumnIndex(KEY_STADIUM_CITY_TRANSPORT)));
				stadium.setCityEconomy(cursor.getString(cursor
						.getColumnIndex(KEY_STADIUM_CITY_ECONOMY)));
				// } while (cursor.moveToNext());
			}

			// cursor = db.rawQuery(selectQueryImages, null);
			// if (cursor.moveToFirst()) {
			// do {
			// type = cursor.getString(cursor
			// .getColumnIndex(KEY_STADIUM_IMAGES_TYPE));
			// if (type.equalsIgnoreCase(STADIUM_IMAGE_TYPE.TYPE_STADIUM)) {
			// stadium.addStadiumPhoto(cursor.getString(cursor
			// .getColumnIndex(KEY_STADIUM_IMAGES_URL)));
			// } else {
			// stadium.addCityPhoto(cursor.getString(cursor
			// .getColumnIndex(KEY_STADIUM_IMAGES_URL)));
			// }
			//
			// } while (cursor.moveToNext());
			// }

		} catch (Exception e) {
			Log.e(TAG,
					"Se ha producido un error al obtener el Splash: "
							+ e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return stadium;
	}

	public ArrayList<String> getPhotoStadium(int id, String type) {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		String selectQueryImages = "SELECT * FROM " + TABLE_STADIUM_IMAGES
				+ " where " + KEY_STADIUM_IMAGES_ID + " = " + id + " and "
				+ KEY_STADIUM_IMAGES_TYPE + " like \"" + type + "\"";

		Cursor cursor = null;

		ArrayList<String> images = new ArrayList<String>();

		try {
			cursor = db.rawQuery(selectQueryImages, null);
			if (cursor.moveToFirst()) {
				do {
					images.add(cursor.getString(cursor
							.getColumnIndex(KEY_STADIUM_IMAGES_URL)));

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.e(TAG,
					"Se ha producido un error al obtener las imagenes asociadas: "
							+ e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return images;
	}

	/****************************************** STADIUMS *****************************************************/
	/******************************************** NEWS *******************************************************/
	public void updateNews(ArrayList<NewsItem> news, int idCompetition) {
		SQLiteDatabase db = sInstance.getWritableDatabase();
		long time = 0;
		try {

			db.beginTransaction();
			deleteNews(db, idCompetition);
			for (NewsItem newsItem : news) {
				insertNews(db, newsItem, idCompetition);
				if (newsItem.getDate() != null)
					time = newsItem.getDate().getTime();
				if (newsItem.getPhotoBig() != null) {
					insertNewsMedia(db, newsItem.getPhotoBig(),
							newsItem.getTitle(), time, MEDIA_TYPE.TYPE_PHOTO,
							MEDIA_TYPE.TYPE_PHOTO_BIG, idCompetition);
				}
				if (newsItem.getPhotoNormal() != null) {
					insertNewsMedia(db, newsItem.getPhotoNormal(),
							newsItem.getTitle(), time, MEDIA_TYPE.TYPE_PHOTO,
							MEDIA_TYPE.TYPE_PHOTO_NORMAL, idCompetition);
				}
				if (newsItem.getPhotoThumbnail() != null) {
					insertNewsMedia(db, newsItem.getPhotoThumbnail(),
							newsItem.getTitle(), time, MEDIA_TYPE.TYPE_PHOTO,
							MEDIA_TYPE.TYPE_PHOTO_THUMB, idCompetition);
				}

				if (newsItem.getVideo() != null) {
					insertNewsMedia(db, newsItem.getVideo(),
							newsItem.getTitle(), time, MEDIA_TYPE.TYPE_VIDEO,
							"", idCompetition);
				}
			}

			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	private void insertNewsMedia(SQLiteDatabase db, MediaItem media,
			String title, Long date, String type, String typePhoto,
			int idCompetition) throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_NEWS_MEDIA_IDCOMPETITION, idCompetition);
		values.put(KEY_NEWS_MEDIA_TITLE, title);
		values.put(KEY_NEWS_MEDIA_DATE, date);
		values.put(KEY_NEWS_MEDIA_TYPE, type);
		if (type.equalsIgnoreCase(MEDIA_TYPE.TYPE_PHOTO)) {
			values.put(KEY_NEWS_MEDIA_URL_PHOTO, media.getUrl());
			values.put(KEY_NEWS_MEDIA_WIDTH, media.getWidth());
			values.put(KEY_NEWS_MEDIA_HEIGHT, media.getHeight());
			values.put(KEY_NEWS_MEDIA_TYPE_PHOTO, typePhoto);
		} else {
			values.put(KEY_NEWS_MEDIA_URL_VIDEO, media.getUrl());
			PhotoMediaItem photo = ((VideoMediaItem) media).getPhoto();
			if (photo != null) {
				values.put(KEY_NEWS_MEDIA_URL_PHOTO, photo.getUrl());
				values.put(KEY_NEWS_MEDIA_WIDTH, photo.getWidth());
				values.put(KEY_NEWS_MEDIA_HEIGHT, photo.getHeight());
			}
		}

		try {
			db.insert(TABLE_MEDIA_NEWS, null, values);
		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		}
	}

	private void insertNews(SQLiteDatabase db, NewsItem news, int idCompetition)
			throws Exception {
		ContentValues values = new ContentValues();
		values.put(KEY_NEWS_IDCOMPETITION, idCompetition);
		values.put(KEY_NEWS_TITLE, news.getTitle());
		values.put(KEY_NEWS_TITLENEWS, news.getTitleNews());
		values.put(KEY_NEWS_PRETITLE, news.getPreTitle());
		values.put(KEY_NEWS_SUBTITLE, news.getSubTitle());

		values.put(KEY_NEWS_SECTION, news.getSection());
		values.put(KEY_NEWS_TITLESECTION, news.getTitleSection());
		values.put(KEY_NEWS_PRETITLESECTION, news.getPreTitleSection());

		values.put(KEY_NEWS_TITLEFRONT, news.getTitleFront());
		values.put(KEY_NEWS_PRETITLEFRONT, news.getPreTitleFront());

		values.put(KEY_NEWS_URLDETAIL, news.getUrlDetail());
		values.put(KEY_NEWS_BODY, news.getBody());
		values.put(KEY_NEWS_PORTAL, news.getPortal());
		values.put(KEY_NEWS_ABSTRACT, news.getAbstract());
		values.put(KEY_NEWS_TAGS, news.getTags());
		values.put(KEY_NEWS_TIPOLOGY, news.getTypology());
		values.put(KEY_NEWS_AUTHOR, news.getAuthor());
		values.put(KEY_NEWS_URLCOMMENT, news.getUrlComments());
		if (news.getDate() != null)
			values.put(KEY_NEWS_DATE, news.getDate().getTime());

		try {
			db.insert(TABLE_NEWS, null, values);
		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		}
	}

	private void deleteNews(SQLiteDatabase db, int idCompetition)
			throws Exception {

		try {
			int delete = db.delete(TABLE_NEWS,
					KEY_NEWS_IDCOMPETITION + " = ? ",
					new String[] { String.valueOf(idCompetition) });

			delete = db.delete(TABLE_MEDIA_NEWS, KEY_NEWS_MEDIA_IDCOMPETITION
					+ " = ? ", new String[] { String.valueOf(idCompetition) });
		} catch (SQLiteException e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		} catch (Exception e) {
			throw new Exception("Error al actualizar db: " + e.getMessage());
		}

	}

	public ArrayList<NewsItem> getNews(int idCompetition) {
		SQLiteDatabase db = sInstance.getWritableDatabase();

		String selectQueryImages = "SELECT * FROM " + TABLE_NEWS + " where "
				+ KEY_NEWS_IDCOMPETITION + " = " + idCompetition;

		Cursor cursor = null;
		Cursor cursor2 = null;

		ArrayList<NewsItem> news = new ArrayList<NewsItem>();
		NewsItem newItem;
		MediaItem media;
		PhotoMediaItem photo;
		long time = 0;
		String type;
		try {
			cursor = db.rawQuery(selectQueryImages, null);
			if (cursor.moveToFirst()) {
				do {
					newItem = new NewsItem();
					newItem.setIdCompetition(cursor.getInt(cursor
							.getColumnIndex(KEY_NEWS_IDCOMPETITION)));
					newItem.setTitle(cursor.getString(cursor
							.getColumnIndex(KEY_NEWS_TITLE)));
					newItem.setTitleNews(cursor.getString(cursor
							.getColumnIndex(KEY_NEWS_TITLENEWS)));
					newItem.setPreTitle(cursor.getString(cursor
							.getColumnIndex(KEY_NEWS_PRETITLE)));
					newItem.setSubTitle(cursor.getString(cursor
							.getColumnIndex(KEY_NEWS_SUBTITLE)));

					newItem.setSection(cursor.getString(cursor
							.getColumnIndex(KEY_NEWS_SECTION)));
					newItem.setTitleSection(cursor.getString(cursor
							.getColumnIndex(KEY_NEWS_TITLESECTION)));
					newItem.setPreTitleSection(cursor.getString(cursor
							.getColumnIndex(KEY_NEWS_PRETITLESECTION)));

					newItem.setTitleFront(cursor.getString(cursor
							.getColumnIndex(KEY_NEWS_TITLEFRONT)));
					newItem.setPreTitleFront(cursor.getString(cursor
							.getColumnIndex(KEY_NEWS_PRETITLEFRONT)));

					newItem.setUrlDetail(cursor.getString(cursor
							.getColumnIndex(KEY_NEWS_URLDETAIL)));
					newItem.setBody(cursor.getString(cursor
							.getColumnIndex(KEY_NEWS_BODY)));
					newItem.setPortal(cursor.getString(cursor
							.getColumnIndex(KEY_NEWS_PORTAL)));
					newItem.setAbstract(cursor.getString(cursor
							.getColumnIndex(KEY_NEWS_ABSTRACT)));
					newItem.setTags(cursor.getString(cursor
							.getColumnIndex(KEY_NEWS_TAGS)));
					newItem.setTypology(cursor.getString(cursor
							.getColumnIndex(KEY_NEWS_TIPOLOGY)));
					newItem.setAuthor(cursor.getString(cursor
							.getColumnIndex(KEY_NEWS_AUTHOR)));
					newItem.setUrlComments(cursor.getString(cursor
							.getColumnIndex(KEY_NEWS_URLCOMMENT)));
					newItem.setDate(new Date(cursor.getLong(cursor
							.getColumnIndex(KEY_NEWS_DATE))));
					if (newItem.getDate() != null)
						time = newItem.getDate().getTime();
					selectQueryImages = "SELECT * FROM " + TABLE_MEDIA_NEWS
							+ " where " + KEY_NEWS_MEDIA_IDCOMPETITION + " = "
							+ idCompetition + " and " + KEY_NEWS_MEDIA_TITLE
							+ " like '" + newItem.getTitle() + "' and "
							+ KEY_NEWS_MEDIA_DATE + " = " + time;

					try {
						cursor2 = db.rawQuery(selectQueryImages, null);
						if (cursor2.moveToFirst()) {
							do {
								type = cursor2.getString(cursor2
										.getColumnIndex(KEY_NEWS_MEDIA_TYPE));
								if (type.equalsIgnoreCase(MEDIA_TYPE.TYPE_PHOTO)) {
									media = new PhotoMediaItem();
									media.setUrl(cursor2.getString(cursor2
											.getColumnIndex(KEY_NEWS_MEDIA_URL_PHOTO)));
									media.setWidth(cursor2.getInt(cursor2
											.getColumnIndex(KEY_NEWS_MEDIA_WIDTH)));
									media.setHeight(cursor2.getInt(cursor2
											.getColumnIndex(KEY_NEWS_MEDIA_HEIGHT)));

									type = cursor2
											.getString(cursor2
													.getColumnIndex(KEY_NEWS_MEDIA_TYPE_PHOTO));
									if (type.equalsIgnoreCase(MEDIA_TYPE.TYPE_PHOTO_BIG)) {
										newItem.setPhotoBig((PhotoMediaItem) media);
									} else if (type
											.equalsIgnoreCase(MEDIA_TYPE.TYPE_PHOTO_NORMAL)) {
										newItem.setPhotoNormal((PhotoMediaItem) media);
									} else if (type
											.equalsIgnoreCase(MEDIA_TYPE.TYPE_PHOTO_THUMB)) {
										newItem.setPhotoThumbnail((PhotoMediaItem) media);
									}
								} else {
									media = new VideoMediaItem();
									if (cursor2
											.getColumnIndex(KEY_NEWS_MEDIA_URL_VIDEO) >= 0)
										media.setUrl(cursor2.getString(cursor2
												.getColumnIndex(KEY_NEWS_MEDIA_URL_VIDEO)));
									photo = new PhotoMediaItem();
									photo.setUrl(cursor2.getString(cursor2
											.getColumnIndex(KEY_NEWS_MEDIA_URL_PHOTO)));
									photo.setWidth(cursor2.getInt(cursor2
											.getColumnIndex(KEY_NEWS_MEDIA_WIDTH)));
									photo.setHeight(cursor2.getInt(cursor2
											.getColumnIndex(KEY_NEWS_MEDIA_HEIGHT)));
									((VideoMediaItem) media).setPhoto(photo);
									newItem.setVideo((VideoMediaItem) media);
								}

							} while (cursor2.moveToNext());
						}
					} catch (Exception e) {
						Log.e(TAG,
								"Se ha producido un error al obtener los media asociadas  a una news de la competition: "
										+ idCompetition + ": " + e.getMessage());
					}
					news.add(newItem);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			Log.e(TAG,
					"Se ha producido un error al obtener las news asociadas  a la competicion "
							+ idCompetition + ": " + e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
			if (cursor2 != null && !cursor2.isClosed()) {
				cursor2.close();
				cursor2 = null;
			}
		}

		return news;
	}
	/******************************************** NEWS *******************************************************/

}
