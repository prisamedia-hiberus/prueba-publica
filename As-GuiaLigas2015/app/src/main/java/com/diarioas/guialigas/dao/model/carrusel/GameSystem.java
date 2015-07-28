/**
 * 
 */
package com.diarioas.guialigas.dao.model.carrusel;

import java.util.ArrayList;
import java.util.HashMap;

import com.diarioas.guialigas.utils.Defines.CarruselEventos;

/**
 * @author robertosanchez
 * 
 */
public class GameSystem {

	private int localTeamId;
	private String localTeamName;
	private int localTeamGSId;
	private String localTeamMisterName;
	private ArrayList<PlayerOnField> localTitulares;
	private ArrayList<PlayerOnField> localSuplentes;

	private int awayTeamId;
	private String awayTeamName;
	private int awayTeamGSId;
	private String awayTeamMisterName;
	private ArrayList<PlayerOnField> awayTitulares;
	private ArrayList<PlayerOnField> awaySuplentes;

	private HashMap<Integer, ArrayList<Event>> eventos;

	// private final HashMap<Integer, ArrayList<Event>> cambios;
	// private final HashMap<Integer, ArrayList<Event>> goles;
	// private final HashMap<Integer, ArrayList<Event>> tarjetasAma;
	// private final HashMap<Integer, ArrayList<Event>> tarjetasRoj;

	public GameSystem() {
		localTitulares = new ArrayList<PlayerOnField>();
		localSuplentes = new ArrayList<PlayerOnField>();
		awayTitulares = new ArrayList<PlayerOnField>();
		awaySuplentes = new ArrayList<PlayerOnField>();

		eventos = new HashMap<Integer, ArrayList<Event>>();
		// cambios = new HashMap<Integer, ArrayList<Event>>();
		// goles = new HashMap<Integer, ArrayList<Event>>();
		// tarjetasAma = new HashMap<Integer, ArrayList<Event>>();
		// tarjetasRoj = new HashMap<Integer, ArrayList<Event>>();
	}

	/**
	 * @return the localTeamId
	 */
	public int getLocalTeamId() {
		return localTeamId;
	}

	/**
	 * @param localTeamId
	 *            the localTeamId to set
	 */
	public void setLocalTeamId(int localTeamId) {
		this.localTeamId = localTeamId;
	}

	/**
	 * @return the localTeamName
	 */
	public String getLocalTeamName() {
		return localTeamName;
	}

	/**
	 * @param localTeamName
	 *            the localTeamName to set
	 */
	public void setLocalTeamName(String localTeamName) {
		this.localTeamName = localTeamName;
	}

	/**
	 * @return the localTeamGSId
	 */
	public int getLocalTeamGSId() {
		return localTeamGSId;
	}

	/**
	 * @param localTeamGSId
	 *            the localTeamGSId to set
	 */
	public void setLocalTeamGSId(int localTeamGSId) {
		this.localTeamGSId = localTeamGSId;
	}

	/**
	 * @return the localTeamMisterName
	 */
	public String getLocalTeamMisterName() {
		return localTeamMisterName;
	}

	/**
	 * @param localTeamMisterName
	 *            the localTeamMisterName to set
	 */
	public void setLocalTeamMisterName(String localTeamMisterName) {
		this.localTeamMisterName = localTeamMisterName;
	}

	/**
	 * @return the localTitulares
	 */
	public ArrayList<PlayerOnField> getLocalTitulares() {
		return localTitulares;
	}

	/**
	 * @param localTitulares
	 *            the localTitulares to set
	 */
	public void setLocalTitulares(ArrayList<PlayerOnField> localTitulares) {
		this.localTitulares = localTitulares;
	}

	/**
	 * @return the localSuplentes
	 */
	public ArrayList<PlayerOnField> getLocalSuplentes() {
		return localSuplentes;
	}

	/**
	 * @param localSuplentes
	 *            the localSuplentes to set
	 */
	public void setLocalSuplentes(ArrayList<PlayerOnField> localSuplentes) {
		this.localSuplentes = localSuplentes;
	}

	/**
	 * @return the awayTeamId
	 */
	public int getAwayTeamId() {
		return awayTeamId;
	}

	/**
	 * @param awayTeamId
	 *            the awayTeamId to set
	 */
	public void setAwayTeamId(int awayTeamId) {
		this.awayTeamId = awayTeamId;
	}

	/**
	 * @return the awayTeamName
	 */
	public String getAwayTeamName() {
		return awayTeamName;
	}

	/**
	 * @param awayTeamName
	 *            the awayTeamName to set
	 */
	public void setAwayTeamName(String awayTeamName) {
		this.awayTeamName = awayTeamName;
	}

	/**
	 * @return the awayTeamGSId
	 */
	public int getAwayTeamGSId() {
		return awayTeamGSId;
	}

	/**
	 * @param awayTeamGSId
	 *            the awayTeamGSId to set
	 */
	public void setAwayTeamGSId(int awayTeamGSId) {
		this.awayTeamGSId = awayTeamGSId;
	}

	/**
	 * @return the awayTeamMisterName
	 */
	public String getAwayTeamMisterName() {
		return awayTeamMisterName;
	}

	/**
	 * @param awayTeamMisterName
	 *            the awayTeamMisterName to set
	 */
	public void setAwayTeamMisterName(String awayTeamMisterName) {
		this.awayTeamMisterName = awayTeamMisterName;
	}

	/**
	 * @return the awayTitulares
	 */
	public ArrayList<PlayerOnField> getAwayTitulares() {
		return awayTitulares;
	}

	/**
	 * @param awayTitulares
	 *            the awayTitulares to set
	 */
	public void setAwayTitulares(ArrayList<PlayerOnField> awayTitulares) {
		this.awayTitulares = awayTitulares;
	}

	/**
	 * @return the awaySuplentes
	 */
	public ArrayList<PlayerOnField> getAwaySuplentes() {
		return awaySuplentes;
	}

	/**
	 * @param awaySuplentes
	 *            the awaySuplentes to set
	 */
	public void setAwaySuplentes(ArrayList<PlayerOnField> awaySuplentes) {
		this.awaySuplentes = awaySuplentes;
	}

	/**
	 * @return the eventos
	 */
	public HashMap<Integer, ArrayList<Event>> getEventos() {
		return eventos;
	}

	public ArrayList<Event> getEventos(int id) {
		return eventos.get(id);
	}

	/**
	 * @param eventos
	 *            the eventos to set
	 */
	public void setEventos(HashMap<Integer, ArrayList<Event>> eventos) {
		this.eventos = eventos;
	}

	// /**
	// * @return the cambios
	// */
	// public HashMap<Integer, ArrayList<Event>> getCambios() {
	// return cambios;
	// }
	//
	// /**
	// * @return the goles
	// */
	// public HashMap<Integer, ArrayList<Event>> getGoles() {
	// return goles;
	// }
	//
	// /**
	// * @return the tarjetasAma
	// */
	// public HashMap<Integer, ArrayList<Event>> getTarjetasAma() {
	// return tarjetasAma;
	// }
	//
	// /**
	// * @return the tarjetasRoj
	// */
	// public HashMap<Integer, ArrayList<Event>> getTarjetasRoj() {
	// return tarjetasRoj;
	// }

	/**
	 * @param string
	 * @param string
	 */
	public void addCambio(Integer key, String time) {
		Event evento = new Event();
		evento.setTipo(CarruselEventos.CARRUSEL_EVENTO_CAMBIO);
		evento.setTime(Integer.valueOf(time));

		// if (!cambios.containsKey(key)) {
		// cambios.put(key, new ArrayList<Event>());
		// }
		// cambios.get(key).add(evento);

		addEvento(key, evento);

	}

	/**
	 * @param string
	 * @param string
	 */
	public void addGol(Integer key, String time) {
		Event evento = new Event();
		evento.setTipo(CarruselEventos.CARRUSEL_EVENTO_GOL);
		evento.setTime(Integer.valueOf(time));
		// if (!goles.containsKey(key)) {
		// goles.put(key, new ArrayList<Event>());
		// }
		// goles.get(key).add(evento);
		addEvento(key, evento);
	}

	/**
	 * @param valueOf
	 * @param string
	 */
	public void addTarjetaAmarilla(Integer key, String time) {
		Event evento = new Event();
		evento.setTipo(CarruselEventos.CARRUSEL_EVENTO_TARJETA_AMARILLA);
		evento.setTime(Integer.valueOf(time));
		// if (!tarjetasAma.containsKey(key)) {
		// tarjetasAma.put(key, new ArrayList<Event>());
		// }
		// tarjetasAma.get(key).add(evento);
		addEvento(key, evento);

	}

	/**
	 * @param valueOf
	 * @param string
	 */
	public void addTarjetaRoja(Integer key, String time) {
		Event evento = new Event();
		evento.setTipo(CarruselEventos.CARRUSEL_EVENTO_TARJETA_ROJA);
		evento.setTime(Integer.valueOf(time));
		// if (!tarjetasRoj.containsKey(key)) {
		// tarjetasRoj.put(key, new ArrayList<Event>());
		// }
		// tarjetasRoj.get(key).add(evento);
		addEvento(key, evento);

	}

	private void addEvento(Integer key, Event evento) {
		if (!eventos.containsKey(key)) {
			eventos.put(key, new ArrayList<Event>());
		}
		eventos.get(key).add(evento);
	}

	/**
	 * @param id
	 * @return
	 */
	public boolean isScorer(int id) {
		ArrayList<Event> listEventos = eventos.get(id);
		if (listEventos == null || listEventos.size() == 0) {
			return false;
		} else {

			for (Event event : listEventos) {
				if (event.getTipo().equalsIgnoreCase(
						CarruselEventos.CARRUSEL_EVENTO_GOL))
					return true;
			}
		}
		return false;
	}

	/**
	 * @param id
	 * @return
	 */
	public boolean isYellowCard(int id) {
		ArrayList<Event> listEventos = eventos.get(id);
		if (listEventos == null || listEventos.size() == 0) {
			return false;
		} else {

			for (Event event : listEventos) {
				if (event.getTipo().equalsIgnoreCase(
						CarruselEventos.CARRUSEL_EVENTO_TARJETA_AMARILLA))
					return true;
			}
		}
		return false;
	}

	/**
	 * @param id
	 * @return
	 */
	public boolean isRedCard(int id) {
		ArrayList<Event> listEventos = eventos.get(id);
		if (listEventos == null || listEventos.size() == 0) {
			return false;
		} else {

			for (Event event : listEventos) {
				if (event.getTipo().equalsIgnoreCase(
						CarruselEventos.CARRUSEL_EVENTO_TARJETA_ROJA))
					return true;
			}
		}
		return false;
	}

	/**
	 * @param id
	 * @return
	 */
	public boolean isChanged(int id) {
		ArrayList<Event> listEventos = eventos.get(id);
		if (listEventos == null || listEventos.size() == 0) {
			return false;
		} else {

			for (Event event : listEventos) {
				if (event.getTipo().equalsIgnoreCase(
						CarruselEventos.CARRUSEL_EVENTO_CAMBIO))
					return true;
			}
		}
		return false;
	}

	public class Event {

		private String tipo;
		private int time;

		/**
		 * @return the tipo
		 */
		public String getTipo() {
			return tipo;
		}

		/**
		 * @param tipo
		 *            the tipo to set
		 */
		public void setTipo(String tipo) {
			this.tipo = tipo;
		}

		/**
		 * @return the time
		 */
		public int getTime() {
			return time;
		}

		/**
		 * @param time
		 *            the time to set
		 */
		public void setTime(int time) {
			this.time = time;
		}

	}

}
