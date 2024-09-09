package org.etfbl.pj2.simulacija;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.etfbl.pj2.gui.GlavniProzor;
import org.etfbl.pj2.iznajmljivanje.Iznajmljivanje;
import org.etfbl.pj2.izvjestaj.DnevniIzvjestaj;
import org.etfbl.pj2.izvjestaj.MenadzerIzvjestaja;
import org.etfbl.pj2.parser.IznajmljivanjeParser;
import org.etfbl.pj2.parser.VoziloParser;
import org.etfbl.pj2.polje.Polje;
import org.etfbl.pj2.racun.Racun;
import org.etfbl.pj2.util.Util;
import org.etfbl.pj2.vozilo.Vozilo;

/**
 * Klasa koja simulira proces iznajmljivanja vozila i generisanja računa u
 * okviru definisanog okruženja. Simulacija prikazuje kretanje vozila kroz
 * zadati prostor i ažurira GUI aplikacije u realnom vremenu.
 */
public class Simulacija {
	public static void main(String[] args) {

		VoziloParser voziloParser = new VoziloParser();
		List<Vozilo> vozila = voziloParser.parsiranjeVozila();
		// vozila.stream().forEach(System.out::println);

		IznajmljivanjeParser iznajmljivanjeParser = new IznajmljivanjeParser(vozila);
		List<Iznajmljivanje> iznajmljivanja = iznajmljivanjeParser.parsirajIznajmljivanja();
		// iznajmljivanja.stream().forEach(System.out::println);

		Map<String, Vozilo> map = vozila.stream()
				.collect(Collectors.toMap(Vozilo::getId, vozilo -> vozilo, (v2, v1) -> v1));
		List<Racun> racuni = iznajmljivanja.stream().map(izn -> new Racun(izn, map.get(izn.getIdVozila())))
				.collect(Collectors.toList());
		// racuni.stream().forEach(System.out::println);
		GlavniProzor prozor = new GlavniProzor(racuni,vozila);
		prozor.setVisible(true);

		startSimulation(racuni, prozor);
	}

	/**
	 * Pokreće simulaciju i ažurira GUI sa novim podacima o vozilima i računima.
	 * 
	 * @param racuni Lista računa koji se procesiraju tokom simulacije.
	 * @param prozor Glavni prozor GUI aplikacije.
	 */
	private static void startSimulation(List<Racun> racuni, GlavniProzor prozor) {
		Map<LocalDate, List<Racun>> racuniPoDatumu = racuni.stream()
				.collect(Collectors.groupingBy(racun -> racun.getIznajmljivanje().getPocetnoVrijeme().toLocalDate(),
						TreeMap::new, Collectors.toList()));
		try {
			long pauza = 5000;
			racuniPoDatumu.forEach((datum, grupa) -> {
				// String datumTekst = datum.format(Util.DATE_FORMATTER);
				// prozor.getDatumTekst().setText(datumTekst);

				Map<LocalDateTime, List<Racun>> racuniPoVremenu = grupa.stream().collect(Collectors.groupingBy(
						racun -> racun.getIznajmljivanje().getPocetnoVrijeme(), TreeMap::new, Collectors.toList()));

				racuniPoVremenu.forEach((vrijeme, list) -> {
					String vrijemeTekst = vrijeme.format(Util.DATE_TIME_FORMATTER);
					prozor.getDatumTekst().setText(vrijemeTekst);

					List<Racun> sortiraniRacuni = list.stream()
							.sorted(Comparator.comparing(r -> r.getIznajmljivanje().getPocetnoVrijeme()))
							.collect(Collectors.toList());

					Semaphore semafor = new Semaphore(sortiraniRacuni.size());
					List<Thread> tredovi = new ArrayList<>();

					for (Racun racun : sortiraniRacuni) {
						Thread tred = new Thread(() -> {
							try {
								semafor.acquire();
								obradaRacuna(racun, prozor);
							} catch (Exception e) {
								System.out.println(e.getMessage());
							} finally {
								semafor.release();
							}
						});
						tredovi.add(tred);
						tred.start();
					}

					for (Thread t : tredovi) {
						try {
							t.join();
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}
					try {
						System.out.println("Pauza");
						Thread.sleep(pauza);

					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				});
				DnevniIzvjestaj dnevniIzvjesta = new DnevniIzvjestaj(grupa);
				MenadzerIzvjestaja.saveIzvjestaj(dnevniIzvjesta, datum);
			});
			kraj();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Procesira podatke o pojedinačnom računu, simulira kretanje vozila kroz polja
	 * i ažurira GUI aplikacije.
	 * 
	 * @param racun  Račun koji se obrađuje.
	 * @param prozor Glavni prozor GUI aplikacije.
	 */
	private static void obradaRacuna(Racun racun, GlavniProzor prozor) {
		Iznajmljivanje iznajmljivanje = racun.getIznajmljivanje();
		double voznjaPoPolju = 1.0 * iznajmljivanje.getTrajanjeUSekundama()
				/ iznajmljivanje.getNajkracaPutanja().size();
		// long pauza = (long) (voznjaPoPolju * 1000);
		// brze
		long pauza = (long) (voznjaPoPolju * 1000 > 1000 ? 1000 : voznjaPoPolju * 1000);
		racun.setPocetniNivoBaterije(racun.getVozilo().getNivoBaterije());
		iznajmljivanje.getNajkracaPutanja().forEach(polje -> {
			racun.getVozilo().isprazniBateriju();

			prikaziVozilo(prozor, polje, racun.getVozilo());
			try {
				Thread.sleep(pauza);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			ukloniVozilo(prozor, polje, racun.getVozilo());
			
			racun.setZavrsniNivoBaterije(racun.getVozilo().getNivoBaterije());
			iznajmljivanje.setZavrsnoVrijeme(
					iznajmljivanje.getPocetnoVrijeme().plusSeconds(iznajmljivanje.getTrajanjeUSekundama()));
		});
		racun.generisanjeRacuna();

	}

	/**
	 * Uklanja vozilo iz GUI-a nakon što napusti određeno polje.
	 * 
	 * @param prozor Prozor aplikacije.
	 * @param polje  Polje sa kog se uklanja vozilo.
	 * @param vozilo Vozilo koje se uklanja.
	 */
	private static void ukloniVozilo(GlavniProzor prozor, Polje polje, Vozilo vozilo) {
		SwingUtilities.invokeLater(() -> {
			List<Vozilo> vozila = prozor.getVozila()[polje.getY()][polje.getX()];
			vozila.remove(vozilo);

			azuriranjePolja(prozor, polje, vozila);
		});

	}

	/**
	 * Ažurira izgled polja u GUI-u nakon što se vozila promene.
	 * 
	 * @param prozor Prozor aplikacije.
	 * @param polje  Polje koje se ažurira.
	 * @param vozila Lista vozila koja su trenutno na tom polju.
	 */
	private static void azuriranjePolja(GlavniProzor prozor, Polje polje, List<Vozilo> vozila) {
		JPanel panel = prozor.getTabela()[polje.getY()][polje.getX()];
		JLabel label = prozor.getVozilaLbls()[polje.getY()][polje.getX()];
		String tekst = vozila.stream().map(v -> v.getId() + "(" + String.format("%.0f", v.getNivoBaterije()) + "%)")
				.collect(Collectors.joining(" "));
		label.setText(tekst);
		if (vozila.isEmpty())
			panel.setBackground(Util.bojaPolja(polje.getX(), polje.getY()));
		else {
			panel.setBackground(prozor.bojaVozila(vozila.get(0)));
		}
	}

	/**
	 * Prikazuje vozilo u GUI-u na zadatom polju.
	 * 
	 * @param prozor Prozor aplikacije.
	 * @param polje  Polje na koje se vozilo prikazuje.
	 * @param vozilo Vozilo koje se prikazuje.
	 */
	private static void prikaziVozilo(GlavniProzor prozor, Polje polje, Vozilo vozilo) {
		SwingUtilities.invokeLater(() -> {
			List<Vozilo> vozila = prozor.getVozila()[polje.getY()][polje.getX()];
			vozila.add(vozilo);

			azuriranjePolja(prozor, polje, vozila);
		});

	}

	/**
	 * Završava simulaciju i obaveštava korisnika da je simulacija završena.
	 */
	private static void kraj() {
		JOptionPane.showMessageDialog(null, "KRAJ");
	}
}
