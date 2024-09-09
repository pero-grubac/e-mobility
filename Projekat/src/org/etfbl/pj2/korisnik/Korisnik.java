package org.etfbl.pj2.korisnik;

import org.etfbl.pj2.util.Util;

/**
 * Klasa koja predstavlja korisnika u sistemu. Ova klasa sadrži osnovne
 * informacije o korisniku, kao što su ime, identifikacioni dokument i vozačka
 * dozvola.
 */
public class Korisnik {
	private String ime;
	private String identifikacioniDokument;
	private String vozackaDozvola;

	/**
	 * Podrazumevani konstruktor koji inicijalizuje objekat klase Korisnik.
	 */
	public Korisnik() {
		super();
	}

	/**
	 * Konstruktor koji inicijalizuje objekat klase Korisnik sa zadatim imenom,
	 * identifikacionim dokumentom i vozačkom dozvolom.
	 *
	 * @param ime                     Ime korisnika.
	 * @param identifikacioniDokument Identifikacioni dokument korisnika.
	 * @param vozackaDozvola          Vozačka dozvola korisnika.
	 */
	public Korisnik(String ime, String identifikacioniDokument, String vozackaDozvola) {
		super();
		this.ime = ime;
		this.identifikacioniDokument = identifikacioniDokument;
		this.vozackaDozvola = vozackaDozvola;
	}

	/**
	 * Konstruktor koji inicijalizuje objekat klase Korisnik sa zadatim imenom.
	 *
	 * @param ime Ime korisnika.
	 */
	public Korisnik(String ime) {
		super();
		this.ime = ime;

	}

	/**
	 * Generiše slučajne vrednosti za identifikacioni dokument i vozačku dozvolu.
	 * Ove vrednosti su generisane pomoću {@link Util#randomString(int)} metode.
	 */
	public void generisiDokumentaciju() {
		this.identifikacioniDokument = Util.randomString(5);
		this.vozackaDozvola = Util.randomString(5);
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getIdentifikacioniDokument() {
		return identifikacioniDokument;
	}

	public void setIdentifikacioniDokument(String identifikacioniDokument) {
		this.identifikacioniDokument = identifikacioniDokument;
	}

	public String getVozackaDozvola() {
		return vozackaDozvola;
	}

	public void setVozackaDozvola(String vozackaDozvola) {
		this.vozackaDozvola = vozackaDozvola;
	}

	@Override
	public String toString() {
		return "Korisnik ime=" + ime + ", identifikacioniDokument="
				+ (identifikacioniDokument != null ? identifikacioniDokument : "nema") + ", vozackaDozvola="
				+ (vozackaDozvola != null ? vozackaDozvola : "nema");
	}

}
