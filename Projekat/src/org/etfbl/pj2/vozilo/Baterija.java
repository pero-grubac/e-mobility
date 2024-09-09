package org.etfbl.pj2.vozilo;

/**
 * Interfejs Baterija definiše osnovne operacije koje se mogu izvršavati nad
 * baterijom, kao što su punjenje i pražnjenje baterije.
 * 
 * @author VašeIme
 * @version 1.0
 * @since 2024-09-09
 */
public interface Baterija {
	/**
	 * Puni bateriju do punog kapaciteta.
	 */
	public void napuniBateriju();

	/**
	 * Prazni bateriju do minimalnog nivoa.
	 */
	public void isprazniBateriju();
}
