package org.etfbl.pj2.izvjestaj;

import java.util.List;

import org.etfbl.pj2.polje.Polje;
import org.etfbl.pj2.racun.Racun;
import org.etfbl.pj2.resources.AppConfig;
import org.etfbl.pj2.vozilo.Auto;
import org.etfbl.pj2.vozilo.Biciklo;
import org.etfbl.pj2.vozilo.Trotinet;
import org.etfbl.pj2.vozilo.Vozilo;

/**
 * Klasa koja predstavlja statistiku na osnovu liste računa.
 * Ova klasa omogućava proračun različitih finansijskih parametara
 * kao što su ukupni prihod, popust, promocije, zarada u vanjskom i unutrašnjem delu, troškovi održavanja i popravke.
 */
public class Statistika {
	private List<Racun> racuni;
	private AppConfig conf = new AppConfig();

	public Statistika(List<Racun> racuni) {
		this.racuni = racuni;
	}

	public Double ukupniPrihod() {
		return racuni.stream().mapToDouble(Racun::getUkupno).sum();
	}

	public Double ukupniPopust() {
		return racuni.stream().mapToDouble(Racun::getPopust).sum();
	}

	public Double ukupnePromocije() {
		return racuni.stream().mapToDouble(Racun::getPromocija).sum();
	}

	public Double ukupnaZaradaUVanjskomDijelu() {
		Double temp = 0.0;
		for (Racun r : racuni) {
			for (Polje polje : r.getIznajmljivanje().getNajkracaPutanja()) {
				if (polje.getX() < 5 || polje.getX() > 14 || polje.getY() < 5 || polje.getY() > 14)
					temp += r.getOsnovnaCijena() * conf.getUdaljenostVanjska();
			}
		}
		return temp;
	}

	public Double ukupnaZaradaUUnutrasnjemDijelu() {
		Double temp = 0.0;
		for (Racun r : racuni) {
			for (Polje polje : r.getIznajmljivanje().getNajkracaPutanja()) {
				if (polje.getX() >= 5 || polje.getX() <= 14 || polje.getY() >= 5 || polje.getY() <= 14)
					temp += r.getOsnovnaCijena() * conf.getUdaljenostUnutrasnja();
			}
		}
		return temp;
	}

	public Double ukupniTroskoviOdrzavanja() {
		return ukupniPrihod() * conf.getOdrzavanje();
	}

	public Double ukupniTrosak() {
		return ukupniPrihod() * conf.getUkupnaCijenaProcenat();
	}

	public Double ukupniPorez() {
		return ((ukupniPrihod() - ukupniTroskoviOdrzavanja() - ukupniTroskoviPopravke() - ukupniTrosak())
				* conf.getPorez());
	}

	public Double ukupniTroskoviPopravke() {
		Double temp = 0.0;
		Vozilo vozilo;
		try {
			for (Racun r : racuni) {
				vozilo = r.getVozilo();
				if (vozilo instanceof Auto)
					temp += vozilo.getCijena() * conf.getCijenaPopravkeAuta();
				else if (vozilo instanceof Biciklo)
					temp += vozilo.getCijena() * conf.getCijenaPopravkeBicikla();
				else if (vozilo instanceof Trotinet)
					temp += vozilo.getCijena() * conf.getCijenaPopravkeTrotineta();
				else {
					throw new IllegalArgumentException("ne postojece vozilo");
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return temp;
	}
	
}
