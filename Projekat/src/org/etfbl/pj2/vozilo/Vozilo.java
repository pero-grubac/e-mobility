package org.etfbl.pj2.vozilo;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.etfbl.pj2.resources.AppConfig;

/**
 * Apstraktna klasa Vozilo predstavlja osnovne atribute i metode koje vozila
 * treba da poseduju. Implementira interfejs Serializable za omogućavanje
 * serijalizacije i interfejs Baterija za operacije vezane za bateriju.
 * 
 * @author VašeIme
 * @version 1.0
 * @since 2024-09-09
 */
public abstract class Vozilo implements Serializable, Baterija {
	private String id;
	private String proizvodjac;
	private String model;
	private Double cijena;
	private Double nivoBaterije;
	private Double potrosnjaBaterije;
	private static transient AppConfig CONF = new AppConfig();
	private static transient Double punaBaterija = 100.0;
	private static transient Double praznaBataerija = 0.0;

	/**
	 * Podrazumevani konstruktor klase Vozilo.
	 */
	public Vozilo() {
		super();
	}

	/**
	 * Konstruktor koji inicijalizuje sve atribute klase Vozilo.
	 * 
	 * @param id                Jedinstveni identifikator vozila.
	 * @param proizvodjac       Naziv proizvođača vozila.
	 * @param model             Model vozila.
	 * @param cijena            Cijena vozila.
	 * @param nivoBaterije      Trenutni nivo baterije vozila.
	 * @param potrosnjaBaterije Potrošnja baterije vozila po jedinici kretanja.
	 */
	public Vozilo(String id, String proizvodjac, String model, Double cijena, Double nivoBaterije,
			Double potrosnjaBaterije) {
		super();
		this.id = id;
		this.proizvodjac = proizvodjac;
		this.model = model;
		this.cijena = cijena;
		this.nivoBaterije = nivoBaterije;
		this.potrosnjaBaterije = potrosnjaBaterije;
	}

	/**
	 * Konstruktor koji inicijalizuje atribute klase Vozilo bez početnog nivoa
	 * baterije.
	 * 
	 * @param id                Jedinstveni identifikator vozila.
	 * @param proizvodjac       Naziv proizvođača vozila.
	 * @param model             Model vozila.
	 * @param cijena            Cijena vozila.
	 * @param potrosnjaBaterije Potrošnja baterije vozila po jedinici kretanja.
	 */
	public Vozilo(String id, String proizvodjac, String model, Double cijena, Double potrosnjaBaterije) {
		super();
		this.id = id;
		this.proizvodjac = proizvodjac;
		this.model = model;
		this.cijena = cijena;
		this.potrosnjaBaterije = potrosnjaBaterije;

		napuniBateriju();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProizvodjac() {
		return proizvodjac;
	}

	public void setProizvodjac(String proizvodjac) {
		this.proizvodjac = proizvodjac;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Double getCijena() {
		return cijena;
	}

	public void setCijena(Double cijena) {
		this.cijena = cijena;
	}

	public Double getNivoBaterije() {
		return nivoBaterije;
	}

	public void setNivoBaterije(Double nivoBaterije) {
		this.nivoBaterije = nivoBaterije;
	}

	public static AppConfig getCONF() {
		return CONF;
	}

	public static void setCONF(AppConfig cONF) {
		CONF = cONF;
	}

	public Double getPotrosnjaBaterije() {
		return potrosnjaBaterije;
	}

	public void setPotrosnjaBaterije(Double potrosnjaBaterije) {
		this.potrosnjaBaterije = potrosnjaBaterije;
	}

	public static Double getPunaBaterija() {
		return punaBaterija;
	}

	public static void setPunaBaterija(Double punaBaterija) {
		Vozilo.punaBaterija = punaBaterija;
	}

	public static Double getPraznaBataerija() {
		return praznaBataerija;
	}

	public static void setPraznaBataerija(Double praznaBataerija) {
		Vozilo.praznaBataerija = praznaBataerija;
	}

	/**
	 * Metoda za generisanje tekstualnog opisa vozila.
	 * 
	 * @return String koji opisuje vozilo sa osnovnim karakteristikama.
	 */
	@Override
	public String toString() {
		String nivoBaterijeString = (nivoBaterije != null) ? String.format("%.2f%%", nivoBaterije) : "0";

		return "id=" + id + ", proizvodjac=" + proizvodjac + ", model=" + model + ", cijena=" + cijena
				+ ", nivoBaterije=" + nivoBaterije + "%, potrosnjaBaterije=" + potrosnjaBaterije + "%";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Vozilo v = (Vozilo) o;
		return Objects.equals(id, v.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
