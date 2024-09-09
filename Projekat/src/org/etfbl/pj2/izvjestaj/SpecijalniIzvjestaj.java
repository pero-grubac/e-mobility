package org.etfbl.pj2.izvjestaj;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.etfbl.pj2.racun.Racun;
import org.etfbl.pj2.vozilo.Auto;
import org.etfbl.pj2.vozilo.Biciklo;
import org.etfbl.pj2.vozilo.Trotinet;
import org.etfbl.pj2.vozilo.Vozilo;

/**
 * Klasa koja predstavlja specijalni izvještaj o vozilima sa najviše prihoda.
 * Ova klasa sadrži informacije o vozilima koja su generisala najveće prihode i
 * omogućava pristup tim informacijama.
 */
public class SpecijalniIzvjestaj implements Serializable {
	private Map<Vozilo, Double> najboljaVozila = new HashMap<>();

	/**
	 * Konstruktor koji inicijalizuje specijalni izvještaj na osnovu liste računa.
	 * Analizira prihode od vozila i beleži vozila sa najvećim prihodima za svaku
	 * vrstu vozila.
	 * 
	 * @param racuni Lista računa koja sadrži informacije o vozilima i njihovim
	 *               prihodima.
	 */
	public SpecijalniIzvjestaj(List<Racun> racuni) {
		Vozilo auto = null;
		Double prihodAuta = 0.0;

		Vozilo biciklo = null;
		Double prihodBicikla = 0.0;

		Vozilo trotinet = null;
		Double prihodTrotineta = 0.0;

		for (Racun r : racuni) {
			if (r.getVozilo() instanceof Auto && r.getUkupno().compareTo(prihodAuta) > 0) {
				auto = r.getVozilo();
				prihodAuta = r.getUkupno();
			} else if (r.getVozilo() instanceof Biciklo && r.getUkupno().compareTo(prihodBicikla) > 0) {
				biciklo = r.getVozilo();
				prihodBicikla = r.getUkupno();
			}
			if (r.getVozilo() instanceof Trotinet && r.getUkupno().compareTo(prihodTrotineta) > 0) {
				trotinet = r.getVozilo();
				prihodTrotineta = r.getUkupno();
			}
		}
		najboljaVozila.put(auto, prihodAuta);
		najboljaVozila.put(trotinet, prihodTrotineta);
		najboljaVozila.put(biciklo, prihodBicikla);
	}

	/**
	 * Pomaže u generisanju teksta izvještaja koji sadrži informacije o vozilima sa
	 * najviše prihoda.
	 * 
	 * @return Tekstualni prikaz izvještaja.
	 */
	private String tekstIzvjestaja() {
		StringBuilder tekst = new StringBuilder();
		tekst.append("Vozila sa najvise prihoda").append("\n");
		najboljaVozila.forEach((k, v) -> {
			tekst.append("Vozilo: ").append(k).append("\n");
			tekst.append(" Prihod: ").append(v).append("\n");
		});
		return tekst.toString();
	}

	public Map<Vozilo, Double> getNajboljaVozila() {
		return najboljaVozila;
	}

	public void setNajboljaVozila(Map<Vozilo, Double> najboljaVozila) {
		this.najboljaVozila = najboljaVozila;
	}

	@Override
	public String toString() {
		return tekstIzvjestaja();
	}

}
