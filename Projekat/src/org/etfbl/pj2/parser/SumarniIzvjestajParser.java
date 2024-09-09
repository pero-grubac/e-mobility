package org.etfbl.pj2.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.etfbl.pj2.izvjestaj.DnevniIzvjestaj;
import org.etfbl.pj2.izvjestaj.MenadzerIzvjestaja;
import org.etfbl.pj2.izvjestaj.SumarniIzvjestaj;

/**
 * Klasa za parsiranje sumarnih izveštaja iz tekstualnih podataka. Ova klasa
 * koristi metode za konverziju teksta u objekte tipa SumarniIzvjestaj.
 */
public class SumarniIzvjestajParser {
	/**
	 * Parsira sumarni izveštaj i vraća mapu sa nazivima izveštaja i odgovarajućim
	 * SumarniIzvjestaj objektima.
	 *
	 * @return Mapa u kojoj su ključevi nazivi izveštaja, a vrednosti odgovarajući
	 *         SumarniIzvjestaj objekti.
	 */
	public Map<String, SumarniIzvjestaj> parsiraj() {
		Map<String, String> izvjestaji = MenadzerIzvjestaja.readSumarniIzvjestaj();
		Map<String, SumarniIzvjestaj> sumarniIzvjestaji = new HashMap<>();
		izvjestaji.forEach((naziv, tekst) -> {
			SumarniIzvjestaj izvjestaj = parsirajTekst(tekst);
			sumarniIzvjestaji.put(naziv, izvjestaj);
		});
		return sumarniIzvjestaji;
	}

	/**
	 * Parsira tekstualni sadržaj u SumarniIzvjestaj objekat. Ova metoda analizira
	 * linije teksta i postavlja odgovarajuće vrednosti u SumarniIzvjestaj objekat.
	 *
	 * @param tekst Tekstualni sadržaj izveštaja koji treba parsirati.
	 * @return SumarniIzvjestaj objekat sa postavljenim vrednostima.
	 */
	private SumarniIzvjestaj parsirajTekst(String tekst) {
		SumarniIzvjestaj izvjestaj = new SumarniIzvjestaj();
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
				else if (linija.startsWith("Ukupni porez:"))
					izvjestaj.setUkupniPorez(parseDouble(linija));
				else if (linija.startsWith("Ukupni trosak:"))
					izvjestaj.setUkupniTrosak(parseDouble(linija));
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
	 * Parsira string vrednost u Double. Ako vrednost nije validan broj, metoda
	 * ispisuje grešku i vraća null.
	 *
	 * @param tekst Tekstualna vrednost koja se parsira.
	 * @return Vrednost tipa Double, ili null ako dođe do greške prilikom
	 *         parsiranja.
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
