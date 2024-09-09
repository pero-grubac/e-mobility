package org.etfbl.pj2.vozilo;

import java.time.LocalDate;

/**
 * Klasa Biciklo predstavlja vozilo sa dodatnim informacijama o autonomiji
 * (dometu). Nasleđuje klasu Vozilo.
 * 
 * @author VašeIme
 * @version 1.0
 * @since 2024-09-09
 */
public class Biciklo extends Vozilo {
	private Integer autonomija;

	/**
	 * Podrazumevani konstruktor klase Biciklo.
	 */
	public Biciklo() {
		super();
	}

	/**
	 * Konstruktor koji inicijalizuje sve atribute klase Biciklo.
	 * 
	 * @param id           Jedinstveni identifikator bicikla.
	 * @param proizvodjac  Naziv proizvođača bicikla.
	 * @param model        Model bicikla.
	 * @param cijena       Cijena bicikla.
	 * @param nivoBaterije Trenutni nivo baterije bicikla.
	 * @param autonomija   Autonomija (domet) bicikla.
	 */
	public Biciklo(String id, String proizvodjac, String model, Double cijena, Double nivoBaterije,
			Integer autonomija) {
		super(id, proizvodjac, model, cijena, nivoBaterije, 3.0);
		this.autonomija = autonomija;
	}

	/**
	 * Konstruktor koji inicijalizuje atribute klase Biciklo bez nivoa baterije.
	 * 
	 * @param id          Jedinstveni identifikator bicikla.
	 * @param proizvodjac Naziv proizvođača bicikla.
	 * @param model       Model bicikla.
	 * @param cijena      Cijena bicikla.
	 * @param autonomija  Autonomija (domet) bicikla.
	 */
	public Biciklo(String id, String proizvodjac, String model, Double cijena, Integer autonomija) {
		super(id, proizvodjac, model, cijena, 3.0);
		this.autonomija = autonomija;
	}

	/**
	 * Metoda koja puni bateriju bicikla na pun kapacitet.
	 */
	@Override
	public void napuniBateriju() {
		setNivoBaterije(getPunaBaterija());
	}

	/**
	 * Metoda koja prazni bateriju bicikla. Ako nivo baterije postane manji od
	 * minimalnog, baterija se automatski puni.
	 */
	@Override
	public void isprazniBateriju() {
		if (getNivoBaterije() == null || (getNivoBaterije() - getPotrosnjaBaterije() < getPraznaBataerija()))
			napuniBateriju();
		setNivoBaterije(getNivoBaterije() - getPotrosnjaBaterije());
	}

	public Integer getAutonomija() {
		return autonomija;
	}

	public void setAutonomija(Integer autonomija) {
		this.autonomija = autonomija;
	}

	/**
	 * Vraća tekstualni opis bicikla.
	 * 
	 * @return Opis bicikla sa osnovnim karakteristikama.
	 */
	@Override
	public String toString() {
		return "Biciklo " + super.toString() + ", autonomija=" + autonomija;
	}

}
