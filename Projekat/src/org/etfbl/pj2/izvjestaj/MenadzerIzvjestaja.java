package org.etfbl.pj2.izvjestaj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.etfbl.pj2.resources.AppConfig;
import org.etfbl.pj2.util.Util;

/**
 * Klasa koja upravlja čuvanjem i čitanjem izvještaja. Ova klasa pruža metode za
 * serijalizaciju i deserijalizaciju objekata izvještaja, kao i za čuvanje i
 * čitanje izvještaja u i iz tekstualnih fajlova.
 */
public class MenadzerIzvjestaja {
	private static AppConfig conf = new AppConfig();

	/**
	 * Čuva specijalni izvještaj u fajl.
	 * 
	 * @param specijalniIzvjestaj Specijalni izvještaj koji treba da se sačuva.
	 */
	public static void saveSpecijalniIzvjestaj(SpecijalniIzvjestaj specijalniIzvjestaj) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
			String trenutniDatum = sdf.format(new Date());

			String fajl = conf.getSpecijalniIzvjestaj() + ".txt";
			String putanja = conf.getIzvjestajFolder() + File.separator + fajl;
			File folder = new File(conf.getIzvjestajFolder());
			if (!folder.exists())
				folder.mkdir();
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(putanja))) {
				oos.writeObject(specijalniIzvjestaj);
				System.out.println("Izvjestaj sacuvan na " + putanja);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Čita specijalni izvještaj iz fajla.
	 * 
	 * @return Specijalni izvještaj pročitan iz fajla, ili null ako dođe do greške.
	 */
	public static SpecijalniIzvjestaj readSpecijalniIzvjestaj() {
		try {
			String fajl = conf.getSpecijalniIzvjestaj() + ".txt";
			String putanja = conf.getIzvjestajFolder() + File.separator + fajl;
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(putanja))) {
				return (SpecijalniIzvjestaj) ois.readObject();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return null;

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;

		}
	}

	/**
	 * Čuva dnevni ili sumarni izvještaj u fajl sa datim datumom.
	 * 
	 * @param izvjestaj Izvještaj koji treba da se sačuva.
	 * @param datum     Datum izvještaja.
	 */
	public static void saveIzvjestaj(DnevniIzvjestaj izvjestaj, LocalDate datum) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String datumIzvjestaja = formatter.format(datum);

			String fajl = ((izvjestaj instanceof SumarniIzvjestaj) ? conf.getSumarniIzvjestaj()
					: conf.getDnevniIzvjestaj()) + " " + datumIzvjestaja + ".txt";
			String putanja = conf.getIzvjestajFolder() + File.separator + fajl;
			File folder = new File(conf.getIzvjestajFolder());

			if (!folder.exists())
				folder.mkdir();

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(putanja))) {
				writer.write(izvjestaj.toString());
				System.out.println("Izvjestaj sacuvan na " + putanja);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Čita dnevni izvještaj iz fajla.
	 * 
	 * @return Mapa sa imenima fajlova i njihovim sadržajem.
	 */
	public static Map<String, String> readDnevniIzvjestaj() {
		return readIzvjestaj(conf.getDnevniIzvjestaj());
	}

	/**
	 * Čita sumarni izvještaj iz fajla.
	 * 
	 * @return Mapa sa imenima fajlova i njihovim sadržajem.
	 */
	public static Map<String, String> readSumarniIzvjestaj() {
		return readIzvjestaj(conf.getSumarniIzvjestaj());
	}

	/**
	 * Pomaže u čitanju izvještaja iz fajlova na osnovu imena izvještaja.
	 * 
	 * @param izvjestaj Ime izvještaja koji treba da se pročita.
	 * @return Mapa sa imenima fajlova i njihovim sadržajem.
	 */
	private static Map<String, String> readIzvjestaj(String izvjestaj) {
		Map<String, String> tekst = new HashMap<>();
		File folder = new File(conf.getIzvjestajFolder());
		File[] fajlovi = folder.listFiles((dir, file) -> file.contains(izvjestaj));

		if (fajlovi != null) {
			for (File f : fajlovi) {
				try (BufferedReader br = new BufferedReader(new FileReader(f))) {
					StringBuilder sadrzaj = new StringBuilder();
					String linija;
					while ((linija = br.readLine()) != null) {
						sadrzaj.append(linija).append("\n");
					}
					tekst.put(f.getName().substring(0, f.getName().lastIndexOf('.')), sadrzaj.toString());
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return tekst;
	}
}
