package org.etfbl.pj2.kvar;

import java.time.LocalDateTime;
import org.etfbl.pj2.util.Util;
/**
 * Klasa koja predstavlja kvar na vozilu.
 * Ova klasa sadrži informacije o vremenu kada je kvar nastao i opis kvara.
 */
public class Kvar {
	private LocalDateTime vrijemeKvara;
	private String opis;

    /**
     * Konstruktor koji inicijalizuje objekat klase Kvar sa zadatim vremenom kvara.
     * Opis kvara se automatski generiše.
     *
     * @param vrijemeKvara Vreme kada je kvar nastao.
     */
	public Kvar(LocalDateTime vrijemeKvara) {
		super();
		this.vrijemeKvara = vrijemeKvara;
		this.opis = Util.randomString(10);
	}

	public LocalDateTime getVrijemeKvara() {
		return vrijemeKvara;
	}

	public void setVrijemeKvara(LocalDateTime vrijemeKvara) {
		this.vrijemeKvara = vrijemeKvara;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	@Override
	public String toString() {
		return "Kvar [vrijemeKvara=" + vrijemeKvara.format(Util.DATE_TIME_FORMATTER) + ", opis=" + opis + "]";
	}

}
