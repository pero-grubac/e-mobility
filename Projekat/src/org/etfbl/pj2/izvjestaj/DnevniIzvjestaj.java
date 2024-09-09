package org.etfbl.pj2.izvjestaj;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.etfbl.pj2.racun.Racun;

/**
 * Klasa koja predstavlja dnevni izveštaj o prihodima, popustima, promocijama i
 * troškovima. Ova klasa obrađuje podatke o računima i generiše izveštaje na
 * osnovu tih podataka.
 */
public class DnevniIzvjestaj {
	protected transient Statistika statistika;
	protected Double ukupniPrihod;
	protected Double ukupniPopust;
	protected Double ukupnePromocije;
	protected Double ukupnaZaradaUVanjskomDijelu;
	protected Double ukupnaZaradaUUnutrasnjemDijelu;
	protected Double ukupniTroskoviOdrzavanja;
	protected Double ukupniTroskoviPopravke;
	protected List<Double> prihodi = new ArrayList<Double>();
	protected LocalDate date;

	/**
	 * Podrazumevani konstruktor koji inicijalizuje objekat klase DnevniIzvjestaj.
	 */
	public DnevniIzvjestaj() {
	}

	/**
	 * Konstruktor koji inicijalizuje objekat klase DnevniIzvjestaj sa listom
	 * računa. Izračunava statistiku i postavlja vrednosti za prihod, popust,
	 * promocije, zaradu i troškove.
	 *
	 * @param racuni Lista računa koji se koriste za generisanje statistike.
	 */
	public DnevniIzvjestaj(List<Racun> racuni) {
		statistika = new Statistika(racuni);
		racuni.stream().forEach(r -> prihodi.add(r.getUkupno()));
		ukupniPrihod = statistika.ukupniPrihod();
		ukupniPopust = statistika.ukupniPopust();
		ukupnePromocije = statistika.ukupnePromocije();
		ukupnaZaradaUVanjskomDijelu = statistika.ukupnaZaradaUVanjskomDijelu();
		ukupnaZaradaUUnutrasnjemDijelu = statistika.ukupnaZaradaUUnutrasnjemDijelu();
		ukupniTroskoviOdrzavanja = statistika.ukupniTroskoviOdrzavanja();
		ukupniTroskoviPopravke = statistika.ukupniTroskoviPopravke();
	}

	/**
	 * Generiše tekstualni izveštaj o ukupnom prihodu, popustu, promocijama, zaradi
	 * i troškovima.
	 *
	 * @return Tekstualni izveštaj kao {@link String}.
	 */
	protected String tekstIzvjestaja() {
		StringBuilder tekst = new StringBuilder();
		tekst.append("Ukupni prihod: ").append(String.format(Locale.US, "%.2f", ukupniPrihod)).append("\n");
		tekst.append("Ukupni popust: ").append(String.format(Locale.US, "%.2f", ukupniPopust)).append("\n");
		tekst.append("Ukupna promocija: ").append(String.format(Locale.US, "%.2f", ukupnePromocije)).append("\n");
		tekst.append("Zarada u vanjskom dijelu: ").append(String.format(Locale.US, "%.2f", ukupnaZaradaUVanjskomDijelu))
				.append("\n");
		tekst.append("Zarada u unutrasnjem dijelu: ")
				.append(String.format(Locale.US, "%.2f", ukupnaZaradaUUnutrasnjemDijelu)).append("\n");
		tekst.append("Troskovi odrzavanja: ").append(String.format(Locale.US, "%.2f", ukupniTroskoviOdrzavanja))
				.append("\n");
		tekst.append("Troskovi popravke: ").append(String.format(Locale.US, "%.2f", ukupniTroskoviPopravke))
				.append("\n");

		return tekst.toString();
	}

	/**
	 * Generiše izveštaj koji uključuje tekstualni izveštaj i listu prihoda.
	 *
	 * @return Izveštaj sa svim prihodi u formatu {@link String}.
	 */
	protected String izvjestajSaVozilima() {
		StringBuilder tekst = new StringBuilder();
		tekst.append(tekstIzvjestaja());
		tekst.append("Lista prihoda :").append("\n");
		for (Double prihod : prihodi)
			tekst.append("  ").append(String.format(Locale.US, "%.2f", prihod)).append("\n");

		return tekst.toString();
	}

	public Statistika getStatistika() {
		return statistika;
	}

	public void setStatistika(Statistika statistika) {
		this.statistika = statistika;
	}

	public Double getUkupniPrihod() {
		return ukupniPrihod;
	}

	public void setUkupniPrihod(Double ukupniPrihod) {
		this.ukupniPrihod = ukupniPrihod;
	}

	public Double getUkupniPopust() {
		return ukupniPopust;
	}

	public void setUkupniPopust(Double ukupniPopust) {
		this.ukupniPopust = ukupniPopust;
	}

	public Double getUkupnePromocije() {
		return ukupnePromocije;
	}

	public void setUkupnePromocije(Double ukupnePromocije) {
		this.ukupnePromocije = ukupnePromocije;
	}

	public Double getUkupnaZaradaUVanjskomDijelu() {
		return ukupnaZaradaUVanjskomDijelu;
	}

	public void setUkupnaZaradaUVanjskomDijelu(Double ukupnaZaradaUVanjskomDijelu) {
		this.ukupnaZaradaUVanjskomDijelu = ukupnaZaradaUVanjskomDijelu;
	}

	public Double getUkupnaZaradaUUnutrasnjemDijelu() {
		return ukupnaZaradaUUnutrasnjemDijelu;
	}

	public void setUkupnaZaradaUUnutrasnjemDijelu(Double ukupnaZaradaUUnutrasnjemDijelu) {
		this.ukupnaZaradaUUnutrasnjemDijelu = ukupnaZaradaUUnutrasnjemDijelu;
	}

	public Double getUkupniTroskoviOdrzavanja() {
		return ukupniTroskoviOdrzavanja;
	}

	public void setUkupniTroskoviOdrzavanja(Double ukupniTroskoviOdrzavanja) {
		this.ukupniTroskoviOdrzavanja = ukupniTroskoviOdrzavanja;
	}

	public Double getUkupniTroskoviPopravke() {
		return ukupniTroskoviPopravke;
	}

	public void setUkupniTroskoviPopravke(Double ukupniTroskoviPopravke) {
		this.ukupniTroskoviPopravke = ukupniTroskoviPopravke;
	}

	public List<Double> getPrihodi() {
		return prihodi;
	}

	public void setPrihodi(List<Double> prihodi) {
		this.prihodi = prihodi;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return izvjestajSaVozilima();
	}
}
