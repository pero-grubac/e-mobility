package org.etfbl.pj2.iznajmljivanje;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.etfbl.pj2.korisnik.Korisnik;
import org.etfbl.pj2.polje.Polje;
import org.etfbl.pj2.util.Util;

public class Iznajmljivanje {
	private Korisnik korisnik;
	private String idVozila;
	private Polje pocetak;
	private Polje kraj;
	private transient List<Polje> najkracaPutanja;
	private LocalDateTime pocetnoVrijeme;
	private LocalDateTime zavrsnoVrijeme;
	private Long trajanjeUSekundama;
	private Boolean promocija;
	private Boolean popust;
	private static transient Integer pravoNaPopust = 0;
	private Boolean kvar;

	public Iznajmljivanje(String ime, String idVozila, Polje pocetak, Polje kraj, LocalDateTime pocetnoVrijeme,
			 Long trajanjeUSekundama, Boolean promocija, Boolean kvar) {
		super();
		this.korisnik = new Korisnik(ime);
		this.idVozila = idVozila;
		this.pocetak = pocetak;
		this.kraj = kraj;
		this.pocetnoVrijeme = pocetnoVrijeme;
		this.trajanjeUSekundama = trajanjeUSekundama;
		this.promocija = promocija;
		this.kvar = kvar;
		pravoNaPopust++;
		popust = pravoNaPopust % 10 == 0;
		najkracaPutanja = Util.najkracaPutanja(pocetak, kraj);
	}

	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}

	public String getIdVozila() {
		return idVozila;
	}

	public void setIdVozila(String idVozila) {
		this.idVozila = idVozila;
	}

	public Polje getPocetak() {
		return pocetak;
	}

	public void setPocetak(Polje pocetak) {
		this.pocetak = pocetak;
	}

	public Polje getKraj() {
		return kraj;
	}

	public void setKraj(Polje kraj) {
		this.kraj = kraj;
	}

	public List<Polje> getNajkracaPutanja() {
		return najkracaPutanja;
	}

	public void setNajkracaPutanja(List<Polje> najkracaPutanja) {
		this.najkracaPutanja = najkracaPutanja;
	}

	public LocalDateTime getPocetnoVrijeme() {
		return pocetnoVrijeme;
	}

	public void setPocetnoVrijeme(LocalDateTime pocetnoVrijeme) {
		this.pocetnoVrijeme = pocetnoVrijeme;
	}

	public LocalDateTime getZavrsnoVrijeme() {
		return zavrsnoVrijeme;
	}

	public void setZavrsnoVrijeme(LocalDateTime krajnjeVrijeme) {
		this.zavrsnoVrijeme = krajnjeVrijeme;
	}

	public Long getTrajanjeUSekundama() {
		return trajanjeUSekundama;
	}

	public void setTrajanjeUSekundama(Long trajanjeUSekundama) {
		this.trajanjeUSekundama = trajanjeUSekundama;
	}

	public Boolean getPromocija() {
		return promocija;
	}

	public void setPromocija(Boolean promocija) {
		this.promocija = promocija;
	}

	public Boolean getPopust() {
		return popust;
	}

	public void setPopust(Boolean popust) {
		this.popust = popust;
	}

	public static Integer getPravoNaPopust() {
		return pravoNaPopust;
	}

	public static void setPravoNaPopust(Integer pravoNaPopust) {
		Iznajmljivanje.pravoNaPopust = pravoNaPopust;
	}

	public Boolean getKvar() {
		return kvar;
	}

	public void setKvar(Boolean kvar) {
		this.kvar = kvar;
	}

	@Override
	public String toString() {
		return "Iznajmljivanje [" + korisnik + ", idVozila=" + idVozila + ", pocetak=" + pocetak + ", kraj="
				+ kraj + ", pocetnoVrijeme=" + pocetnoVrijeme.format(Util.DATE_TIME_FORMATTER) + ", krajnjeVrijeme="
				+ (zavrsnoVrijeme !=null ? zavrsnoVrijeme.format(Util.DATE_TIME_FORMATTER): "N/A") + ", trajanjeUSekundama=" + trajanjeUSekundama
				+ ", promocija=" + promocija + ", popust=" + popust + ", kvar=" + kvar + "]";
	}

}
