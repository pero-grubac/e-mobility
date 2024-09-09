package org.etfbl.pj2.vozilo;

import java.time.LocalDate;
import org.etfbl.pj2.util.Util;

/**
 * Klasa Auto predstavlja vozilo sa dodatnim informacijama o datumu kupovine,
 * opisu i mogućnosti da li može prevoziti više putnika. Nasleđuje klasu Vozilo.
 * 
 * @author VašeIme
 * @version 1.0
 * @since 2024-09-09
 */
public class Auto extends Vozilo {
	private LocalDate datumKupovine;
	private String opis;
	private boolean visePutnika = true;

	/**
	 * Podrazumevani konstruktor klase Auto.
	 */
	public Auto() {
	}

	/**
	 * Konstruktor koji inicijalizuje sve atribute klase Auto.
	 * 
	 * @param id            Jedinstveni identifikator automobila.
	 * @param proizvodjac   Naziv proizvođača automobila.
	 * @param model         Model automobila.
	 * @param cijena        Cijena automobila.
	 * @param nivoBaterije  Trenutni nivo baterije automobila.
	 * @param datumKupovine Datum kada je automobil kupljen.
	 * @param opis          Opis automobila.
	 */
	public Auto(String id, String proizvodjac, String model, Double cijena, Double nivoBaterije,
			LocalDate datumKupovine, String opis) {
		super(id, proizvodjac, model, cijena, nivoBaterije, 5.0);
		this.datumKupovine = datumKupovine;
		this.opis = opis;
	}

	/**
	 * Konstruktor koji inicijalizuje atribute klase Auto bez nivoa baterije.
	 * 
	 * @param id            Jedinstveni identifikator automobila.
	 * @param proizvodjac   Naziv proizvođača automobila.
	 * @param model         Model automobila.
	 * @param cijena        Cijena automobila.
	 * @param datumKupovine Datum kada je automobil kupljen.
	 * @param opis          Opis automobila.
	 */
	public Auto(String id, String proizvodjac, String model, Double cijena, LocalDate datumKupovine, String opis) {
		super(id, proizvodjac, model, cijena, 5.0);
		this.datumKupovine = datumKupovine;
		this.opis = opis;
	}

	/**
	 * Metoda koja puni bateriju automobila na pun kapacitet.
	 */

	@Override
	public void napuniBateriju() {
		setNivoBaterije(getPunaBaterija());

	}

	/**
	 * Metoda koja prazni bateriju automobila. Ako nivo baterije postane manji od
	 * minimalnog, baterija se automatski puni.
	 */
	@Override
	public void isprazniBateriju() {
		if (getNivoBaterije() == null || (getNivoBaterije() - getPotrosnjaBaterije() < getPraznaBataerija()))
			napuniBateriju();
		setNivoBaterije(getNivoBaterije() - getPotrosnjaBaterije());
	}

	public LocalDate getDatumKupovine() {
		return datumKupovine;
	}

	public void setDatumKupovine(LocalDate datumKupovine) {
		this.datumKupovine = datumKupovine;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public boolean isVisePutnika() {
		return visePutnika;
	}

	public void setVisePutnika(boolean visePutnika) {
		this.visePutnika = visePutnika;
	}
	/**
	 * Vraća tekstualni opis automobila.
	 * 
	 * @return Opis automobila sa osnovnim karakteristikama.
	 */
	@Override
	public String toString() {
		return "Auto " + super.toString() + ", datumKupovine="
				+ (datumKupovine != null ? datumKupovine.format(Util.DATE_FORMATTER) : "N/A") + ", opis=" + opis
				+ ", visePutnika=" + visePutnika;
	}

}
