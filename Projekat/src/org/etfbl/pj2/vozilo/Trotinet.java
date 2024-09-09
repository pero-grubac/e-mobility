package org.etfbl.pj2.vozilo;

/**
 * Klasa Trotinet predstavlja vozilo sa dodatnom informacijom o maksimalnoj
 * brzini. Nasleđuje klasu Vozilo.
 * 
 * @author VašeIme
 * @version 1.0
 * @since 2024-09-09
 */
public class Trotinet extends Vozilo {
	private Integer maksimalnaBrzina;

	/**
	 * Podrazumevani konstruktor klase Trotinet.
	 */
	public Trotinet() {
		super();
	}

	/**
	 * Konstruktor koji inicijalizuje sve atribute klase Trotinet.
	 * 
	 * @param id               Jedinstveni identifikator trotineta.
	 * @param proizvodjac      Naziv proizvođača trotineta.
	 * @param model            Model trotineta.
	 * @param cijena           Cijena trotineta.
	 * @param nivoBaterije     Trenutni nivo baterije trotineta.
	 * @param maksimalnaBrzina Maksimalna brzina trotineta.
	 */
	public Trotinet(String id, String proizvodjac, String model, Double cijena, Double nivoBaterije,
			Integer maksimalnaBrzina) {
		super(id, proizvodjac, model, cijena, nivoBaterije, 2.0);
		this.maksimalnaBrzina = maksimalnaBrzina;
	}

	/**
	 * Konstruktor koji inicijalizuje atribute klase Trotinet bez nivoa baterije.
	 * 
	 * @param id               Jedinstveni identifikator trotineta.
	 * @param proizvodjac      Naziv proizvođača trotineta.
	 * @param model            Model trotineta.
	 * @param cijena           Cijena trotineta.
	 * @param maksimalnaBrzina Maksimalna brzina trotineta.
	 */
	public Trotinet(String id, String proizvodjac, String model, Double cijena, Integer maksimalnaBrzina) {
		super(id, proizvodjac, model, cijena, 2.0);
		this.maksimalnaBrzina = maksimalnaBrzina;
	}

	/**
	 * Metoda koja puni bateriju trotineta na pun kapacitet.
	 */
	@Override
	public void napuniBateriju() {
		setNivoBaterije(getPunaBaterija());
	}

	/**
	 * Metoda koja prazni bateriju trotineta. Ako nivo baterije postane manji od
	 * minimalnog, baterija se automatski puni.
	 */
	@Override
	public void isprazniBateriju() {
		if (getNivoBaterije() == null || (getNivoBaterije() - getPotrosnjaBaterije() < getPraznaBataerija()))
			napuniBateriju();
		setNivoBaterije(getNivoBaterije() - getPotrosnjaBaterije());
	}

	public Integer getMaksimalnaBrzina() {
		return maksimalnaBrzina;
	}

	public void setMaksimalnaBrzina(Integer maksimalnaBrzina) {
		this.maksimalnaBrzina = maksimalnaBrzina;
	}

	/**
	 * Vraća tekstualni opis trotineta.
	 * 
	 * @return Opis trotineta sa osnovnim karakteristikama.
	 */
	@Override
	public String toString() {
		return "Trotinet " + super.toString() + ", maksimalnaBrzina=" + maksimalnaBrzina;
	}

}
