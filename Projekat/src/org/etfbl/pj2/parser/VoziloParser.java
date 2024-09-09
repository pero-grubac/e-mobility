package org.etfbl.pj2.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.etfbl.pj2.korisnik.Korisnik;
import org.etfbl.pj2.polje.Polje;
import org.etfbl.pj2.resources.AppConfig;
import org.etfbl.pj2.util.Util;
import org.etfbl.pj2.vozilo.Auto;
import org.etfbl.pj2.vozilo.Biciklo;
import org.etfbl.pj2.vozilo.Trotinet;
import org.etfbl.pj2.vozilo.Vozilo;

/**
 * Klasa za parsiranje vozila iz CSV datoteke. Ova klasa učitava podatke o
 * vozilima iz datoteke i kreira odgovarajuće instance klase Vozilo.
 */
public class VoziloParser {
	private AppConfig CONF = new AppConfig();

	/**
	 * Parsira vozila iz CSV datoteke i vraća listu instanci klase Vozilo.
	 *
	 * @return Lista objekata tipa Vozilo.
	 */
	public List<Vozilo> parsiranjeVozila() {
		Set<Vozilo> vozila = new LinkedHashSet<Vozilo>();
		Set<String> idVozila = new HashSet<>();
		try {
			String putanja = CONF.getTestFolder() + File.separator + CONF.getCsvVozila();

			try (BufferedReader br = new BufferedReader(new FileReader(putanja))) {
				String linija;
				br.readLine();// Preskoci prvu liniju

				while ((linija = br.readLine()) != null) {
					try {
						String[] vrijednosti = linija.split(",");
						validacijaDuzineLinije(vrijednosti, 9);

						String id = validacijaIdVozila(vrijednosti[0], idVozila);
						String proizvodjac = validacijaStringa(vrijednosti[1], "proizvodjac");
						String model = validacijaStringa(vrijednosti[2], "model");
						LocalDate danumKupovine = validacijaDatuma(vrijednosti[3], "datum");
						Double cijena = validacijaDouble(vrijednosti[4], "cijena");
						Integer autonomija = vrijednosti[5].isEmpty() ? 0
								: validacijaInteger(vrijednosti[5], "autonomija");
						Integer maksimalnaBrzina = vrijednosti[6].isEmpty() ? 0
								: validacijaInteger(vrijednosti[6], "maksimalna brzina");
						String opis = vrijednosti[7];
						String tip = validacijaStringa(vrijednosti[8], "tip");

						Vozilo vozilo = kreiranjeVozila(id, proizvodjac, model, cijena, danumKupovine, autonomija,
								maksimalnaBrzina, opis, tip);
						vozila.add(vozilo);
					} catch (Exception e) {
						System.out.println("Greska : " + e.getMessage());
					}
				}
			} catch (Exception e) {
				System.out.println("Greska:" + e.getMessage());
			}

		} catch (Exception e) {
			System.out.println("Greska: " + e.getMessage());
		}
		return new ArrayList<>(vozila);
	}

	/**
	 * Validira da li linija CSV datoteke ima ispravnu dužinu.
	 *
	 * @param podaci Niz vrednosti iz linije.
	 * @param duzina Očekivana duzina linije.
	 * @throws IllegalArgumentException Ako duzina linije nije ispravna.
	 */
	private void validacijaDuzineLinije(String[] podaci, int duzina) throws IllegalArgumentException {
		if (podaci.length != duzina)
			throw new IllegalArgumentException("Ne validan format linije");
	}

	/**
	 * Validira da li ID vozila nije duplikat i dodaje ga u set ako je validan.
	 *
	 * @param id       ID vozila.
	 * @param idVozila Set već postojećih ID-ova vozila.
	 * @return Validan ID vozila.
	 * @throws IllegalArgumentException Ako je ID vozila duplikat.
	 */
	private String validacijaIdVozila(String id, Set<String> idVozila) throws IllegalArgumentException {
		if (idVozila.contains(id))
			throw new IllegalArgumentException("Duplikat id vozila pronadjen: " + id);
		idVozila.add(id);
		return id;
	}

	/**
	 * Validira da li je string vrednost prazna ili sadrži samo prazne karaktere.
	 *
	 * @param vrijednost Vrednost koja se validira.
	 * @param imePolja   Ime polja za prikaz u grešci.
	 * @return Validan string.
	 * @throws IllegalArgumentException Ako je string prazan.
	 */
	private String validacijaStringa(String vrijednost, String imePolja) throws IllegalArgumentException {
		if (vrijednost.isEmpty() || vrijednost.isBlank())
			throw new IllegalArgumentException(imePolja + " ne moze biti  prazno");
		return vrijednost;
	}

	/**
	 * Validira da li je string vrednost validan Double broj i da li je
	 * ne-negativan.
	 *
	 * @param vrijednost Vrednost koja se validira.
	 * @param imePolja   Ime polja za prikaz u grešci.
	 * @return Validan Double broj.
	 * @throws IllegalArgumentException Ako vrednost nije validan broj ili je
	 *                                  negativna.
	 */
	private Double validacijaDouble(String vrijednost, String imePolja) throws IllegalArgumentException {
		try {
			Double doubleVrijednost = Double.parseDouble(vrijednost);
			if (doubleVrijednost < 0)
				throw new IllegalArgumentException(imePolja + " ne moze biti  negativno");
			return doubleVrijednost;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(imePolja + " mora biti validan broj");
		}
	}

	/**
	 * Validira da li je string vrednost validan Integer broj i da li je
	 * ne-negativan.
	 *
	 * @param vrijednost Vrednost koja se validira.
	 * @param imePolja   Ime polja za prikaz u grešci.
	 * @return Validan Integer broj.
	 * @throws IllegalArgumentException Ako vrednost nije validan broj ili je
	 *                                  negativna.
	 */
	private Integer validacijaInteger(String vrijednost, String imePolja) throws IllegalArgumentException {
		try {
			Integer integerVrijednost = Integer.parseInt(vrijednost);
			if (integerVrijednost < 0)
				throw new IllegalArgumentException(imePolja + " ne moze biti  negativno");
			return integerVrijednost;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(imePolja + " mora biti validan broj");
		}
	}

	/**
	 * Validira da li je string vrednost validan datum i vraća LocalDate objekat.
	 *
	 * @param vrijednost Vrednost koja se validira.
	 * @param imePolja   Ime polja za prikaz u grešci.
	 * @return Validan LocalDate objekat.
	 * @throws IllegalArgumentException Ako vrednost nije validan datum.
	 */
	private LocalDate validacijaDatuma(String vrijednost, String imePolja) throws IllegalArgumentException {
		try {
			return vrijednost.isEmpty() ? null : LocalDate.parse(vrijednost, Util.DATE_FORMATTER);
		} catch (Exception e) {
			throw new IllegalArgumentException(imePolja + " mora biti validan datum");
		}
	}

	/**
	 * Kreira instancu vozila na osnovu prosleđenih parametara i tipa vozila.
	 *
	 * @param id               ID vozila.
	 * @param proizvodjac      Proizvođač vozila.
	 * @param model            Model vozila.
	 * @param cijena           Cena vozila.
	 * @param datumKupovine    Datum kupovine vozila.
	 * @param autonomija       Autonomija vozila (za bicikl).
	 * @param maksimalnaBrzina Maksimalna brzina vozila (za trotinet).
	 * @param opis             Opis vozila.
	 * @param tip              Tip vozila (automobil, bicikl, trotinet).
	 * @return Kreirana instanca vozila.
	 * @throws IllegalArgumentException Ako je tip vozila nepoznat.
	 */
	private Vozilo kreiranjeVozila(String id, String proizvodjac, String model, Double cijena, LocalDate datumKupovine,
			Integer autonomija, Integer maksimalnaBrzina, String opis, String tip) throws IllegalArgumentException {
		switch (tip.toLowerCase()) {
		case "automobil": {
			return new Auto(id, proizvodjac, model, cijena, datumKupovine, opis);
		}
		case "bicikl": {
			return new Biciklo(id, proizvodjac, model, cijena, autonomija);
		}
		case "trotinet": {
			return new Trotinet(id, proizvodjac, model, cijena, maksimalnaBrzina);
		}
		default:
			throw new IllegalArgumentException("Nepoznati tip: " + tip.toLowerCase());
		}
	}

	/**
	 * Parsira nivo baterije iz stringa u Double vrednost.
	 *
	 * @param vrijednost Vrednost nivoa baterije u string formatu.
	 * @return Nivo baterije kao Double.
	 * @throws IllegalArgumentException Ako nivo baterije nije validan broj.
	 */
	private Double parisanjeBaterije(String vrijednost) throws IllegalArgumentException {
		try {
			return Double.parseDouble(vrijednost.replace("%", "").replace(",00", ""));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Nivo baterije mora biti validan broj");
		}
	}

	/**
	 * Parsira string datum u LocalDate objekat.
	 *
	 * @param vrijednost Vrednost datuma u string formatu.
	 * @param imePolja   Ime polja za prikaz u grešci.
	 * @return Validan LocalDate objekat.
	 * @throws IllegalArgumentException Ako datum nije u ispravnom formatu.
	 */
	private LocalDate parsiranjeDatuma(String vrijednost, String imePolja) throws IllegalArgumentException {
		if (vrijednost.isEmpty())
			throw new IllegalArgumentException(imePolja + " ne moze biti prazno");
		try {
			return LocalDate.parse(vrijednost, Util.DATE_FORMATTER);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(imePolja + " mora biti formata d.M.yyyy");
		}
	}

	/**
	 * Parsira string vrednost u Integer.
	 *
	 * @param vrijednost Vrednost koja se parsira.
	 * @param imePolja   Ime polja za prikaz u grešci.
	 * @return Validan Integer.
	 * @throws IllegalArgumentException Ako vrednost nije validan broj.
	 */
	private Integer parsiranjeInteger(String vrijednost, String imePolja) throws IllegalArgumentException {
		try {
			Integer intVrijednost = Integer.parseInt(vrijednost);
			if (intVrijednost < 0)
				throw new IllegalArgumentException(imePolja + " ne moze biti negativno");
			return intVrijednost;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(imePolja + " mora biti validan broj");
		}
	}

	/**
	 * Dobija vrednost iz stringa koji je u formatu "ključ=vrednost".
	 *
	 * @param dio String u formatu "ključ=vrednost".
	 * @return Vrednost iz stringa.
	 */
	private String getVrijednost(String dio) {
		return dio.split("=")[1].trim();
	}
}
