package org.etfbl.pj2.resources;

import java.io.*;
import java.util.Properties;

public class AppConfig {
	private Properties properties;

	public AppConfig() {
		properties = new Properties();
		loadProperties();
	}

	private void loadProperties() {

		try (InputStream input = new FileInputStream("src/org/etfbl/pj2/resources/app.properties")) {

			if (input == null) {
				System.out.println("Sorry, unable to find app.properties");
				return;
			}

			properties.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public double getDoubleProperty(String key) {
		return Double.parseDouble(properties.getProperty(key));
	}

	public Integer getIntegerProperty(String key) {
		return Integer.parseInt(properties.getProperty(key));
	}

	public String getTestFolder() {
		return getProperty("CSV_FOLDER");
	}

	public String getCsvIznajmljivanje() {
		return getProperty("CSV_IZNAJMLJIVANJE");
	}

	public String getCsvVozila() {
		return getProperty("CSV_VOZIlA");
	}

	public String getRacunFolder() {
		return getProperty("RACUN_FOLDER");
	}

	public String getIzvjestajFolder() {
		return getProperty("IZVJESTAJ_FOLDER");
	}

	public String getDnevniIzvjestaj() {
		return getProperty("DNEVNI_IZV");
	}

	public String getSumarniIzvjestaj() {
		return getProperty("SUMARNI_IZV");
	}

	public String getSpecijalniIzvjestaj() {
		return getProperty("SPECIJALNI_IZV");
	}

	public Double getCijenaAuta() {
		return getDoubleProperty("CIJENA_AUTA");
	}

	public Double getCijenaPopravkeAuta() {
		return getDoubleProperty("CIJENA_POPRAVKE_AUTA");
	}

	public Double getCijenaPopravkeBicikla() {
		return getDoubleProperty("CIJENA_POPRAVKE_BICIKLA");
	}

	public Double getCijenaBicikla() {
		return getDoubleProperty("CIJENA_BICIKLA");
	}

	public Double getCijenaTrotineta() {
		return getDoubleProperty("CIJENA_TROTINETA");
	}

	public Double getCijenaPopravkeTrotineta() {
		return getDoubleProperty("CIJENA_POPRAVKE_TROTINETA");
	}

	public Double getPopust() {
		return getDoubleProperty("POPUST");
	}

	public Double getPromocija() {
		return getDoubleProperty("POPUST_PROMOCIJA");
	}

	public Double getOdrzavanje() {
		return getDoubleProperty("CIJENA_ODRZAVANJA");
	}

	public Double getUkupnaCijenaProcenat() {
		return getDoubleProperty("UKUPNA_CIJENA_PROC");
	}

	public Double getPorez() {
		return getDoubleProperty("POREZ");
	}

	public Integer getPauza() {
		return getIntegerProperty("PAUZA_IZMEDJU_VREMENA");
	}

	public Integer getMaxVrijemeVoznje() {
		return getIntegerProperty("MAX_VRIJEME_VOZNJE_MS");
	}

	public Double getUdaljenostUnutrasnja() {
		return getDoubleProperty("UDALJENOST_UNUTRASNJA");
	}

	public Double getUdaljenostVanjska() {
		return getDoubleProperty("UDALJENJOST_VANJSKA");
	}
}
