package org.etfbl.pj2.polje;

/**
 * Klasa koja predstavlja koordinatnu tačku na dvodimenzionalnom polju. Svaka
 * tačka se definiše sa x i y koordinatama.
 */
public class Polje {
	private Integer x;
	private Integer y;

	/**
	 * Konstruktor koji inicijalizuje tačku sa datim x i y koordinatama.
	 * 
	 * @param x X koordinata tačke.
	 * @param y Y koordinata tačke.
	 */
	public Polje(Integer x, Integer y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

}
