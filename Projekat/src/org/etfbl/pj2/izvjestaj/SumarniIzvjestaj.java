package org.etfbl.pj2.izvjestaj;

import java.util.List;
import java.util.Locale;

import org.etfbl.pj2.racun.Racun;

public class SumarniIzvjestaj extends DnevniIzvjestaj {

	private Double ukupniTrosak;
	private Double ukupniPorez;

	public SumarniIzvjestaj() {
	}

	public SumarniIzvjestaj(List<Racun> racuni) {
		super(racuni);
		ukupniTrosak = statistika.ukupniTrosak();
		ukupniPorez = statistika.ukupniPorez();
	}

	@Override
	protected String tekstIzvjestaja() {
		StringBuilder tekst = new StringBuilder(super.tekstIzvjestaja());
		try {
			tekst.append("Ukupni porez: ").append(String.format(Locale.US, "%.2f", ukupniPorez)).append("\n");
			tekst.append("Ukupni trosak: ").append(String.format(Locale.US, "%.2f", ukupniTrosak)).append("\n");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return tekst.toString();

	}

	public Double getUkupniTrosak() {
		return ukupniTrosak;
	}

	public void setUkupniTrosak(Double ukupniTrosak) {
		this.ukupniTrosak = ukupniTrosak;
	}

	public Double getUkupniPorez() {
		return ukupniPorez;
	}

	public void setUkupniPorez(Double ukupniPorez) {
		this.ukupniPorez = ukupniPorez;
	}

	@Override
	public String toString() {
		return izvjestajSaVozilima();
	}

}
