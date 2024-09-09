package org.etfbl.pj2.racun;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.etfbl.pj2.iznajmljivanje.Iznajmljivanje;
import org.etfbl.pj2.kvar.Kvar;
import org.etfbl.pj2.polje.Polje;
import org.etfbl.pj2.resources.AppConfig;
import org.etfbl.pj2.util.Util;
import org.etfbl.pj2.vozilo.Auto;
import org.etfbl.pj2.vozilo.Biciklo;
import org.etfbl.pj2.vozilo.Trotinet;
import org.etfbl.pj2.vozilo.Vozilo;

/**
 * Klasa koja predstavlja račun za iznajmljivanje vozila.
 */
public class Racun {

	private static AppConfig CONF = new AppConfig();

	private Double ukupno;
	private Double osnovnaCijena = 0.0;
	private Double udaljenost = 0.0;
	private Double vanjskaUdaljenost = 0.0;
	private Double unutrasnjaUdaljenost = 0.0;
	private Double iznos;
	private Double popust = 0.0;
	private Double promocija = 0.0;
	private Iznajmljivanje iznajmljivanje;
	private Vozilo vozilo;
	private Double pocetniNivoBaterije;
	private Double zavrsniNivoBaterije;
	private transient Kvar kvar;
	private transient Integer vanjskaDuzina = 0;
	private transient Integer unutranjaDuzina = 0;

	/**
	 * Konstruktor koji inicijalizuje račun sa iznajmljivanjem i vozilom.
	 *
	 * @param iznajmljivanje Objekat iznajmljivanja.
	 * @param vozilo         Objekat vozila.
	 */
	public Racun(Iznajmljivanje iznajmljivanje, Vozilo vozilo) {
		this.iznajmljivanje = iznajmljivanje;
		this.vozilo = vozilo;
		izracunajOsnovnuCijenu();
		izracunajDistancu();
		izracunajIznos();
		izracunajPopust();
		izracinajPromociju();
		izracunajUkupno();
	}

	/**
	 * Izračunava osnovnu cenu na osnovu tipa vozila.
	 */
	private void izracunajOsnovnuCijenu() {
		try {
			if (!iznajmljivanje.getKvar()) {
				if (vozilo instanceof Auto)
					osnovnaCijena = CONF.getCijenaAuta();
				else if (vozilo instanceof Biciklo)
					osnovnaCijena = CONF.getCijenaBicikla();
				else if (vozilo instanceof Trotinet)
					osnovnaCijena = CONF.getCijenaTrotineta();
				else
					throw new IllegalArgumentException("nepoznat tip vozila");
			}
		} catch (Exception e) {
			System.out.println("Greska: " + e.getMessage());
		}
	}

	/**
	 * Izračunava udaljenost na osnovu putanje iznajmljivanja.
	 */
	private void izracunajDistancu() {
		unutrasnjaUdaljenost = CONF.getUdaljenostUnutrasnja();
		vanjskaUdaljenost = CONF.getUdaljenostVanjska();
		boolean unutrasnja = true;

		for (Polje polje : iznajmljivanje.getNajkracaPutanja()) {
			if (polje.getX() < 5 || polje.getX() > 14 || polje.getY() < 5 || polje.getY() > 14) {
				unutrasnja = false;
				++vanjskaDuzina;
			} else {
				++unutranjaDuzina;
			}
		}
		if (unutrasnja)
			udaljenost = iznajmljivanje.getNajkracaPutanja().size() * unutrasnjaUdaljenost
					* iznajmljivanje.getTrajanjeUSekundama();
		else {
			udaljenost = iznajmljivanje.getNajkracaPutanja().size() * vanjskaUdaljenost
					* iznajmljivanje.getTrajanjeUSekundama();
		}
	}

	/**
	 * Izračunava ukupni iznos na osnovu udaljenosti i osnovne cene.
	 */
	private void izracunajIznos() {
		iznos = udaljenost * osnovnaCijena;
	}

	/**
	 * Izračunava popust na osnovu konfiguracije i iznajmljivanja.
	 */
	private void izracunajPopust() {
		if (iznajmljivanje.getPopust())
			popust = osnovnaCijena * CONF.getPopust();
	}

	/**
	 * Izračunava promociju na osnovu konfiguracije i iznajmljivanja.
	 */
	private void izracinajPromociju() {
		if (iznajmljivanje.getPromocija())
			promocija = osnovnaCijena * CONF.getPromocija();
	}

	/**
	 * Izračunava ukupni iznos računajući popust i promociju
	 */
	private void izracunajUkupno() {
		ukupno = iznos - (popust - promocija) * iznos;
	}
	/**
	 * Generiše račun i čuva ga u tekstualnom fajlu.
	 */
	public void generisanjeRacuna() {
		String fajl = iznajmljivanje.getKorisnik().getIme() + " - "
				+ iznajmljivanje.getZavrsnoVrijeme().format(Util.DATE_FOR_FILE_FORMATTER) + ".txt";
		String putanja = CONF.getRacunFolder() + File.separator + fajl;
		File racunFolder = new File(CONF.getRacunFolder());
		if (!racunFolder.exists())
			racunFolder.mkdir();

		try (FileWriter writer = new FileWriter(putanja)) {
			writer.write(tekstRacuna());
			System.out.println("Racun sacuvan: " + putanja);
		} catch (Exception e) {
			System.out.println("Greska: " + e.getMessage());
		}
	}
	/**
	 * Priprema tekstualni sadržaj računa.
	 *
	 * @return Tekst računa u string formatu.
	 */
	private String tekstRacuna() {
		StringBuilder tekstRacuna = new StringBuilder();
		tekstRacuna.append(iznajmljivanje.getKorisnik()).append("\n");
		tekstRacuna.append("Vozilo ID: ").append(vozilo.getId()).append("\n");
		tekstRacuna.append("Pocetna vrijednost baterije: ").append(String.format("%.2f", pocetniNivoBaterije))
				.append("%").append("\n");
		tekstRacuna.append("Zavrsna vrijednost baterije: ").append(String.format("%.2f", zavrsniNivoBaterije))
				.append("%").append("\n");
		tekstRacuna.append("Pocetno vrijeme: ")
				.append((iznajmljivanje.getPocetnoVrijeme() != null
						? iznajmljivanje.getPocetnoVrijeme().format(Util.DATE_TIME_FORMATTER)
						: "N/A"))
				.append("\n");
		tekstRacuna.append("Zavrsno vrijeme: ")
				.append((iznajmljivanje.getZavrsnoVrijeme() != null
						? iznajmljivanje.getZavrsnoVrijeme().format(Util.DATE_TIME_FORMATTER)
						: "N/A"))
				.append("\n");

		tekstRacuna.append("Pocetna lokacija: ").append(iznajmljivanje.getPocetak()).append("\n");
		tekstRacuna.append("Zavrsna lokacija: ").append(iznajmljivanje.getKraj()).append("\n");
		tekstRacuna.append("Duzina: ").append(iznajmljivanje.getNajkracaPutanja().size()).append("\n");
		tekstRacuna.append("Udaljenost u uzem dijelu: ").append(String.format("%.2f", unutrasnjaUdaljenost))
				.append(" (x").append(unutranjaDuzina).append(")").append("\n");
		tekstRacuna.append("Udaljenost u sirem dijelu: ").append(String.format("%.2f", vanjskaUdaljenost)).append(" (x")
				.append(vanjskaDuzina).append(")").append("\n");

		tekstRacuna.append("Trajanje: ").append(iznajmljivanje.getTrajanjeUSekundama()).append("\n");

		tekstRacuna.append("Udaljenost: ").append(String.format("%.2f", udaljenost)).append("\n");
		tekstRacuna.append("Osnovna cijena: ").append(String.format("%.2f", osnovnaCijena)).append("\n");
		tekstRacuna.append("Iznos: ").append(iznos).append("\n");
		tekstRacuna.append("Popust: ").append(String.format("%.2f", popust)).append("\n");
		tekstRacuna.append("Promocija: ").append(String.format("%.2f", promocija)).append("\n");
		tekstRacuna.append("Ukupno: ").append(ukupno).append("\n");
		/*
		 * tekstRacuna.append("Pocetno vrijeme: ")
		 * .append((iznajmljivanje.getPocetnoVrijeme() != null ?
		 * iznajmljivanje.getPocetnoVrijeme().format(Util.DATE_TIME_FORMATTER) : "N/A"))
		 * .append("\n"); tekstRacuna.append("Zavrsno vrijeme: ")
		 * .append((iznajmljivanje.getZavrsnoVrijeme() != null ?
		 * iznajmljivanje.getZavrsnoVrijeme().format(Util.DATE_TIME_FORMATTER) : "N/A"))
		 * .append("\n");
		 */
		if (iznajmljivanje.getKvar()) {
			kvar = new Kvar(iznajmljivanje.getPocetnoVrijeme());
			tekstRacuna.append(kvar).append("\n");

		} else {
			tekstRacuna.append("Kvar: Ne").append("\n");
		}
		return tekstRacuna.toString();
	}

	public Double getUkupno() {
		return ukupno;
	}

	public void setUkupno(Double ukupno) {
		this.ukupno = ukupno;
	}

	public Double getOsnovnaCijena() {
		return osnovnaCijena;
	}

	public void setOsnovnaCijena(Double osnovnaCijena) {
		this.osnovnaCijena = osnovnaCijena;
	}

	public Double getUdaljenost() {
		return udaljenost;
	}

	public void setUdaljenost(Double udaljenost) {
		this.udaljenost = udaljenost;
	}

	public Double getVanjskaUdaljenost() {
		return vanjskaUdaljenost;
	}

	public void setVanjskaUdaljenost(Double vanjskaUdaljenost) {
		this.vanjskaUdaljenost = vanjskaUdaljenost;
	}

	public Double getUnutrasnjaUdaljenost() {
		return unutrasnjaUdaljenost;
	}

	public void setUnutrasnjaUdaljenost(Double unutrasnjaUdaljenost) {
		this.unutrasnjaUdaljenost = unutrasnjaUdaljenost;
	}

	public Double getIznos() {
		return iznos;
	}

	public void setIznos(Double iznos) {
		this.iznos = iznos;
	}

	public Double getPopust() {
		return popust;
	}

	public void setPopust(Double popust) {
		this.popust = popust;
	}

	public Double getPromocija() {
		return promocija;
	}

	public void setPromocija(Double promocija) {
		this.promocija = promocija;
	}

	public Iznajmljivanje getIznajmljivanje() {
		return iznajmljivanje;
	}

	public void setIznajmljivanje(Iznajmljivanje iznajmljivanje) {
		this.iznajmljivanje = iznajmljivanje;
	}

	public Vozilo getVozilo() {
		return vozilo;
	}

	public void setVozilo(Vozilo vozilo) {
		this.vozilo = vozilo;
	}

	public Double getPocetniNivoBaterije() {
		return pocetniNivoBaterije;
	}

	public void setPocetniNivoBaterije(Double pocetniNivoBaterije) {
		this.pocetniNivoBaterije = pocetniNivoBaterije;
	}

	public Double getZavrsniNivoBaterije() {
		return zavrsniNivoBaterije;
	}

	public void setZavrsniNivoBaterije(Double zavrsniNivoBaterije) {
		this.zavrsniNivoBaterije = zavrsniNivoBaterije;
	}

	public Kvar getKvar() {
		return kvar;
	}

	public void setKvar(Kvar kvar) {
		this.kvar = kvar;
	}

	public Integer getVanjskaDuzina() {
		return vanjskaDuzina;
	}

	public void setVanjskaDuzina(Integer vanjskaDuzina) {
		this.vanjskaDuzina = vanjskaDuzina;
	}

	public Integer getUnutranjaDuzina() {
		return unutranjaDuzina;
	}

	public void setUnutranjaDuzina(Integer unutranjaDuzina) {
		this.unutranjaDuzina = unutranjaDuzina;
	}

	@Override
	public String toString() {
		return tekstRacuna();
	}

}
