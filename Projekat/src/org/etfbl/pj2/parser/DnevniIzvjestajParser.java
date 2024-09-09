package org.etfbl.pj2.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.etfbl.pj2.izvjestaj.DnevniIzvjestaj;
import org.etfbl.pj2.izvjestaj.MenadzerIzvjestaja;

/**
 * Klasa koja se koristi za parsiranje dnevnih izveštaja iz tekstualnih
 * podataka. Omogućava konverziju tekstualnih izveštaja u objekte tipa
 * {@link DnevniIzvjestaj}.
 */
public class DnevniIzvjestajParser {

	/**
	 * Parsira sve dnevne izveštaje koristeći menadžera izveštaja.
	 * 
	 * @return Mapa u kojoj su ključevi nazivi izveštaja, a vrednosti objekti
	 *         {@link DnevniIzvjestaj}.
	 */
	public Map<String, DnevniIzvjestaj> parsiraj() {
		Map<String, String> izvjestaji = MenadzerIzvjestaja.readDnevniIzvjestaj();
		Map<String, DnevniIzvjestaj> dnevniIzvjestaji = new HashMap<>();
		izvjestaji.forEach((naziv, tekst) -> {
			DnevniIzvjestaj izvjestaj = parsirajTekst(tekst);
			dnevniIzvjestaji.put(naziv, izvjestaj);
		});
		return dnevniIzvjestaji;
	}

	/**
	 * Parsira tekstualni izveštaj i kreira objekat {@link DnevniIzvjestaj}.
	 * 
	 * @param tekst Tekstualni izveštaj koji treba da se parsira.
	 * @return Kreirani objekat {@link DnevniIzvjestaj}.
	 */
	private DnevniIzvjestaj parsirajTekst(String tekst) {
		DnevniIzvjestaj izvjestaj = new DnevniIzvjestaj();
		List<Double> prihodi = new ArrayList<>();
		VoziloParser voziloParser = new VoziloParser();
		String[] linije = tekst.split("\n");
		for (String linija : linije) {
			try {
				if (linija.startsWith("Ukupni prihod:"))
					izvjestaj.setUkupniPrihod(parseDouble(linija));
				else if (linija.startsWith("Ukupni popust:"))
					izvjestaj.setUkupniPopust(parseDouble(linija));
				else if (linija.startsWith("Ukupna promocija"))
					izvjestaj.setUkupnePromocije(parseDouble(linija));
				else if (linija.startsWith("Zarada u vanjskom dijelu:"))
					izvjestaj.setUkupnaZaradaUVanjskomDijelu(parseDouble(linija));
				else if (linija.startsWith("Zarada u unutrasnjem dijelu:"))
					izvjestaj.setUkupnaZaradaUUnutrasnjemDijelu(parseDouble(linija));
				else if (linija.startsWith("Troskovi odrzavanja:"))
					izvjestaj.setUkupniTroskoviOdrzavanja(parseDouble(linija));
				else if (linija.startsWith("Troskovi popravke:"))
					izvjestaj.setUkupniTroskoviPopravke(parseDouble(linija));
				else if (linija.startsWith("  "))
					prihodi.add(Double.parseDouble(linija.trim()));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		izvjestaj.setPrihodi(prihodi);
		return izvjestaj;
	}

	/**
	 * Parsira string koji predstavlja broj u double vrednost.
	 * 
	 * @param tekst String koji treba da se parsira.
	 * @return Double vrednost ili null ako dođe do greške prilikom parsiranja.
	 */
	private Double parseDouble(String tekst) {
		String vrijednost = tekst.split(":")[1].trim();
		try {
			return Double.parseDouble(vrijednost);
		} catch (NumberFormatException e) {
			System.out.println("Greška prilikom parsiranja broja: " + tekst);
			return null;
		}
	}
}
