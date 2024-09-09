package org.etfbl.pj2.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.etfbl.pj2.iznajmljivanje.Iznajmljivanje;
import org.etfbl.pj2.polje.Polje;
import org.etfbl.pj2.resources.AppConfig;
import org.etfbl.pj2.util.Util;
import org.etfbl.pj2.vozilo.Vozilo;
import org.etfbl.pj2.vozilo.Auto;
import org.etfbl.pj2.korisnik.Korisnik;

/**
 * Klasa koja parsira podatke o iznajmljivanju iz CSV fajla.
 */
public class IznajmljivanjeParser {
	private AppConfig CONF = new AppConfig();
	public List<String> idVozila;
	List<Vozilo> vozila;

	/**
	 * Konstruktor klase koji inicijalizuje parser sa listom vozila.
	 *
	 * @param vozila Lista vozila koja se koristi za validaciju ID-eva vozila.
	 */
	public IznajmljivanjeParser(List<Vozilo> vozila) {
		this.idVozila = Util.listaIdVozila(vozila);
		this.vozila = vozila;
	}

	/**
	 * Parsira iznajmljivanja iz CSV fajla i vrati listu objekata Iznajmljivanje.
	 *
	 * @return Lista objekata Iznajmljivanje.
	 */
	public List<Iznajmljivanje> parsirajIznajmljivanja() {
		List<Iznajmljivanje> iznajmljivanja = new ArrayList<>();
		try {
			String putanja = CONF.getTestFolder() + File.separator + CONF.getCsvIznajmljivanje();
			try (BufferedReader br = new BufferedReader(new FileReader(putanja))) {
				br.readLine(); // Preskočiti zaglavlje
				String linija;
				while ((linija = br.readLine()) != null) {
					try {
						String[] vrijednosti = linija.split(",");
						validacijaDuzineLinije(vrijednosti, 10);

						LocalDateTime pocetnoVrijeme = validacijaDatuma(vrijednosti[0].replace("\"", ""),
								"pocetno vrijeme");
						String ime = vrijednosti[1];
						String idVozila = vrijednosti[2];
						Polje pocetak = parsiranjePolja(vrijednosti[3].replace("\"", ""),
								vrijednosti[4].replace("\"", ""));
						Polje kraj = parsiranjePolja(vrijednosti[5].replace("\"", ""),
								vrijednosti[6].replace("\"", ""));
						validacijaPolja(pocetak, kraj);
						Long trajanje = validacijaTrajanja(vrijednosti[7]);
						boolean kvar = validacijaBoolean(vrijednosti[8]);
						boolean promocija = validacijaBoolean(vrijednosti[9].replace("\"", ""));

						iznajmljivanja.add(new Iznajmljivanje(ime, idVozila, pocetak, kraj, pocetnoVrijeme, trajanje,
								promocija, kvar));
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

		return validacijaIznajmljivanja(iznajmljivanja.stream()
				.sorted(Comparator.comparing(Iznajmljivanje::getPocetnoVrijeme)).collect(Collectors.toList()));
	}

	/**
	 * Validira listu iznajmljivanja kako bi se osigurali pravilni podaci.
	 *
	 * @param iznajmljivanja Lista iznajmljivanja koja se validira.
	 * @return Lista validnih iznajmljivanja.
	 */
	public List<Iznajmljivanje> validacijaIznajmljivanja(List<Iznajmljivanje> iznajmljivanja) {
		List<Iznajmljivanje> novaIznajmljivanja = new ArrayList<>();
		Map<String, LocalDateTime> kranjaVremenaIznajmljivanja = new HashMap<>();
		Map<String, LocalDateTime> kranjaVremenaKorisnika = new HashMap<>();
		Set<String> korisnikVoziloParovi = new HashSet<>();
		for (Iznajmljivanje izn : iznajmljivanja) {
			try {
				LocalDateTime posljenjeVrijemeZaVozlo = kranjaVremenaIznajmljivanja.get(izn.getIdVozila());
				LocalDateTime posljenjeVrijemeZaKorisnika = kranjaVremenaKorisnika.get(izn.getKorisnik().getIme());

				// Provjera da li je vozilo već iznajmljeno u isto vrijeme
				if (posljenjeVrijemeZaVozlo != null && posljenjeVrijemeZaVozlo.isAfter(izn.getPocetnoVrijeme()))
					throw new IllegalArgumentException("Vozilo " + izn.getIdVozila() + " je vec iznajmljeno");

				// Provjera da li korisnik već iznajmljuje drugo vozilo u isto vrijeme
				if (posljenjeVrijemeZaKorisnika != null && posljenjeVrijemeZaKorisnika.isAfter(izn.getPocetnoVrijeme()))
					throw new IllegalArgumentException("Korisnik " + izn.getKorisnik().getIme() + " je vec iznajmio");

				// Provjera da li korisnik već iznajmljuje isto vozilo u isto vrijeme
				String korisnikVozilo = izn.getKorisnik().getIme() + "-" + izn.getIdVozila();
				if (korisnikVoziloParovi.contains(korisnikVozilo))
					throw new IllegalArgumentException(
							"Korisnik " + izn.getKorisnik().getIme() + " je vec iznajmio vozilo " + izn.getIdVozila());

				// Provjera da li vozilo postoji
				if (!idVozila.contains(izn.getIdVozila()))
					throw new IllegalArgumentException("Id vozila ne postoji: " + izn.getIdVozila());

				novaIznajmljivanja.add(izn);
				
				kranjaVremenaIznajmljivanja.put(izn.getIdVozila(),
						izn.getPocetnoVrijeme().plusSeconds(izn.getTrajanjeUSekundama()));
				
				kranjaVremenaKorisnika.put(izn.getKorisnik().getIme(),
						izn.getPocetnoVrijeme().plusSeconds(izn.getTrajanjeUSekundama()));
				
				korisnikVoziloParovi.add(korisnikVozilo);
			} catch (Exception e) {
				System.out.println("Greska: " + e.getMessage());
			}
		}
		Map<String, Vozilo> mapaVozila = vozila.stream().filter(v -> v instanceof Auto)
				.collect(Collectors.toMap(Vozilo::getId, Function.identity()));
		novaIznajmljivanja.stream().filter(izn -> mapaVozila.containsKey(izn.getIdVozila()))
				.map(Iznajmljivanje::getKorisnik).filter(Objects::nonNull).forEach(Korisnik::generisiDokumentaciju);
		return novaIznajmljivanja;
	}

	/**
	 * Validira dužinu linije podataka.
	 *
	 * @param podaci Niz podataka koji se validira.
	 * @param duzina Očekivana dužina linije.
	 * @throws IllegalArgumentException Ako dužina linije nije validna.
	 */
	private void validacijaDuzineLinije(String[] podaci, int duzina) throws IllegalArgumentException {
		if (podaci.length != duzina)
			throw new IllegalArgumentException("Ne validan format linije");
	}

	/**
	 * Validira ID vozila.
	 *
	 * @param id       ID vozila koji se validira.
	 * @param idVozila Skup svih validnih ID-eva vozila.
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
	 * Validira string vrednost.
	 *
	 * @param vrijednost String vrednost koja se validira.
	 * @param naziv      Naziv vrednosti.
	 * @return Validna string vrednost.
	 * @throws IllegalArgumentException Ako string vrednost nije validna.
	 */
	private String validacijaStringa(String vrijednost, String imePolja) throws IllegalArgumentException {
		if (vrijednost.isEmpty() || vrijednost.isBlank())
			throw new IllegalArgumentException(imePolja + " ne moze biti  prazno");
		return vrijednost;
	}

	/**
	 * Validira i konvertuje string vrednost u Double. Proverava da li je vrednost
	 * negativna i da li je u ispravnom formatu.
	 *
	 * @param vrijednost String koji se konvertuje u Double.
	 * @param imePolja   Naziv polja za koji se vrši validacija.
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
	 * Validira i konvertuje string vrednost u Integer. Proverava da li je vrednost
	 * negativna i da li je u ispravnom formatu.
	 *
	 * @param vrijednost String koji se konvertuje u Integer.
	 * @param imePolja   Naziv polja za koji se vrši validacija.
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
	 * Validira datum.
	 *
	 * @param datum String datum.
	 * @param naziv Naziv datuma.
	 * @return Validan LocalDateTime objekat.
	 * @throws IllegalArgumentException Ako datum nije validan.
	 */
	private LocalDateTime validacijaDatuma(String vrijednost, String imePolja) throws IllegalArgumentException {
		try {
			return LocalDateTime.parse(vrijednost, Util.DATE_TIME_FORMATTER);
		} catch (Exception e) {
			throw new IllegalArgumentException(imePolja + " mora biti validan datum");
		}
	}

	/**
	 * Parsira koordinate polja iz string vrednosti. Proverava da li su koordinate
	 * validne i u ispravnom formatu.
	 *
	 * @param xVr String koji predstavlja X koordinatu.
	 * @param yVr String koji predstavlja Y koordinatu.
	 * @return Polje objekat sa validnim koordinatama.
	 * @throws IllegalArgumentException Ako koordinate nisu validne.
	 */
	private Polje parsiranjePolja(String xVr, String yVr) throws IllegalArgumentException {
		try {
			Integer x = Integer.parseInt(xVr);
			Integer y = Integer.parseInt(yVr);
			return new Polje(x, y);
		} catch (Exception e) {
			throw new IllegalArgumentException("Koordinate polja moraju biti validne");
		}
	}

	/**
	 * Validira trajanje u sekundama.
	 *
	 * @param trajanje Trajanje u sekundama.
	 * @return Trajanje u sekundama.
	 * @throws IllegalArgumentException Ako trajanje nije validno.
	 */
	private Long validacijaTrajanja(String vrijednost) throws IllegalArgumentException {
		try {
			Long loongVrijednost = Long.parseLong(vrijednost);
			if (loongVrijednost < 0)
				throw new IllegalArgumentException("Trajanje ne moze biti  negativno");
			return loongVrijednost;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Trajanje mora biti validan broj");
		}
	}

	/**
	 * Validira boolean vrednost.
	 *
	 * @param vrijednost Boolean vrednost koja se validira.
	 * @return Validan boolean.
	 * @throws IllegalArgumentException Ako boolean vrednost nije validna.
	 */
	private boolean validacijaBoolean(String vrijednost) throws IllegalArgumentException {
		if ("da".equals(vrijednost))
			return true;
		else if ("ne".equals(vrijednost))
			return false;
		else
			throw new IllegalArgumentException("Ne validan boolean");

	}

	/**
	 * Parsira polje iz string vrednosti.
	 *
	 * @param x Koordinata X.
	 * @param y Koordinata Y.
	 * @return Polje objekat.
	 */
	private void validacijaPolja(Polje pocetak, Polje kraj) throws IllegalArgumentException {
		if (validacijaGranicaPolja(pocetak) || validacijaGranicaPolja(kraj))
			throw new IllegalArgumentException(pocetak + " " + kraj + " ne validana granica");

	}

	private boolean validacijaGranicaPolja(Polje polje) {
		return polje.getX() < 0 || polje.getX() > 19 || polje.getY() < 0 || polje.getY() > 19;
	}
}
