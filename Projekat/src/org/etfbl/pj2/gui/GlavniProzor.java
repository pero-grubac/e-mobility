package org.etfbl.pj2.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.etfbl.pj2.izvjestaj.DnevniIzvjestaj;
import org.etfbl.pj2.izvjestaj.MenadzerIzvjestaja;
import org.etfbl.pj2.izvjestaj.SpecijalniIzvjestaj;
import org.etfbl.pj2.izvjestaj.SumarniIzvjestaj;
import org.etfbl.pj2.parser.DnevniIzvjestajParser;
import org.etfbl.pj2.parser.SumarniIzvjestajParser;
import org.etfbl.pj2.racun.Racun;
import org.etfbl.pj2.util.Util;
import org.etfbl.pj2.vozilo.Auto;
import org.etfbl.pj2.vozilo.Biciklo;
import org.etfbl.pj2.vozilo.Trotinet;
import org.etfbl.pj2.vozilo.Vozilo;

public class GlavniProzor extends JFrame {
	private List<Racun> racuni;

	private static Color bojaAuta = Color.RED;
	private static Color bojaBicikla = Color.GREEN;
	private static Color bojaTrotineta = Color.YELLOW;
	private Color unutrasnjaBoja = Color.BLUE;
	private Color vanjskaBoja = Color.WHITE;
	private Color bojaPozadine = Color.ORANGE;

	private JPanel datumPanel;
	private JLabel datumTekst = new JLabel("Datum i vrijeme");

	private JPanel dugmadPanel;
	private JButton dnevniIzvjestajBtn;
	private JButton sumarniIzvjestajBtn;
	private JButton specijalniIzvjestajBtn;
	private JButton autaBtn;
	private JButton biciklaBtn;
	private JButton trotinetiBtn;
	private JButton kvarBtn;
	private JButton poslovanjeBtn;

	private JPanel vrhAplikacije;

	private JPanel[][] tabela;
	private JLabel[][] vozilaLbls;
	private List<Vozilo>[][] vozila;
	private JPanel centar;
	private int redovi = 20, kolone = 20;

	private List<Vozilo> auta = new ArrayList<>();
	private List<Vozilo> bicikla = new ArrayList<>();
	private List<Vozilo> trotineti = new ArrayList<>();

	public GlavniProzor(List<Racun> racuni, List<Vozilo> vozila) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		try {

			setTitle("Projekat");
		} catch (Exception e) {
			System.err.println("Error " + e.getMessage());
		}
		setSize(new Dimension(400, 400));
		setLocationRelativeTo(null);

		this.racuni = racuni;
		razvrstajVozila(vozila);
		setLayout(new BorderLayout());
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		vrhAplikacije = new JPanel(new BorderLayout());
		add(vrhAplikacije, BorderLayout.NORTH);

		konfiguracijaMenija();
		konfiguracijaDatumPanel();
		konfiguracijaTabele();
		inicijalizacijaVozila();
		setBackground(bojaPozadine);
	}

	private void razvrstajVozila(List<Vozilo> vozila) {
		for (Vozilo vozilo : vozila) {
			if (vozilo instanceof Auto) {
				auta.add(vozilo);
			} else if (vozilo instanceof Biciklo) {
				bicikla.add(vozilo);
			} else if (vozilo instanceof Trotinet) {
				trotineti.add(vozilo);
			}
		}

	}

	private void konfiguracijaMenija() {
		dugmadPanel = new JPanel(new GridLayout(1, 6));

		// Kreiramo dugme 1
		dnevniIzvjestajBtn = new JButton("Dnevni izvjestaj");
		dnevniIzvjestajBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DnevniIzvjestaj izvjestaj = new DnevniIzvjestaj(racuni);
				DnevniIzvjestajParser parser = new DnevniIzvjestajParser();
				Map<String, DnevniIzvjestaj> izvjestaji = parser.parsiraj();
				izvjestaji.forEach((datum, izv) -> {
					IzvjestajProzor prozor = new IzvjestajProzor(izv, datum);
					prozor.setVisible(true);
				});
			}
		});

		// Kreiramo dugme 2
		sumarniIzvjestajBtn = new JButton("Sumarni izvjestaj");
		sumarniIzvjestajBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SumarniIzvjestaj izvjestaj = new SumarniIzvjestaj(racuni);
				MenadzerIzvjestaja.saveIzvjestaj(izvjestaj, LocalDate.now());
				SumarniIzvjestajParser parser = new SumarniIzvjestajParser();
				Map<String, SumarniIzvjestaj> izvjestaji = parser.parsiraj();
				izvjestaji.forEach((datum, izv) -> {
					IzvjestajProzor prozor = new IzvjestajProzor(izv, datum);
					prozor.setVisible(true);
				});
			}
		});

		// Kreiramo dugme 3
		specijalniIzvjestajBtn = new JButton("Specijalni izvjestaj");
		specijalniIzvjestajBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SpecijalniIzvjestaj izvjestaj = new SpecijalniIzvjestaj(racuni);
				MenadzerIzvjestaja.saveSpecijalniIzvjestaj(izvjestaj);
				SpecijalniIzvjestaj procitani = MenadzerIzvjestaja.readSpecijalniIzvjestaj();
				SpecijalniIzvjestajProzor prozor = new SpecijalniIzvjestajProzor(izvjestaj, "Specijalni izvjestaj");
				prozor.setVisible(true);
			}
		});
		// Dugme za auta
		autaBtn = new JButton("Auta");
		autaBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				VoziloProzor prozor = new VoziloProzor(auta, "Auta");
				prozor.setVisible(true);
			}
		});
		// Dugme za bicikla
		biciklaBtn = new JButton("Bicikla");
		biciklaBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				VoziloProzor prozor = new VoziloProzor(bicikla, "Bicikla");
				prozor.setVisible(true);
			}
		});
		// Dugme za trotinete
		trotinetiBtn = new JButton("Trotineti");
		trotinetiBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				VoziloProzor prozor = new VoziloProzor(trotineti, "Trotineti");
				prozor.setVisible(true);
			}
		});
		// Dugme za kvarove
		kvarBtn = new JButton("Kvar");
		kvarBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				KvarProzor prozor = new KvarProzor(racuni);
				prozor.setVisible(true);
			}
		});
		// Dugme za poslovanje
		poslovanjeBtn = new JButton("Poslovanje");
		poslovanjeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PoslovanjeProzor prozor = new PoslovanjeProzor(racuni);
				prozor.setVisible(true);
			}
		});
		// Dodajemo dugmad na panel
		dugmadPanel.add(dnevniIzvjestajBtn);
		dugmadPanel.add(sumarniIzvjestajBtn);
		dugmadPanel.add(specijalniIzvjestajBtn);
		dugmadPanel.add(autaBtn);
		dugmadPanel.add(biciklaBtn);
		dugmadPanel.add(trotinetiBtn);
		dugmadPanel.add(kvarBtn);
		dugmadPanel.add(poslovanjeBtn);
		// Dodajemo panel sa dugmadima na vrh prozora
		vrhAplikacije.add(dugmadPanel, BorderLayout.NORTH);
	}

	private void konfiguracijaDatumPanel() {
		datumPanel = new JPanel();
		datumPanel.add(datumTekst);
		datumPanel.setBackground(bojaPozadine);

		vrhAplikacije.add(datumPanel, BorderLayout.SOUTH);
	}

	private void konfiguracijaTabele() {
		tabela = new JPanel[redovi][kolone];
		vozilaLbls = new JLabel[redovi][kolone];
		centar = new JPanel(new GridLayout(redovi, kolone, 2, 2));

		inicijalizacijaTabele();
		add(centar, BorderLayout.CENTER);
		bojenje();
	}

	private void inicijalizacijaTabele() {
		for (int i = 0; i < redovi; i++) {
			for (int j = 0; j < kolone; j++) {
				tabela[i][j] = new JPanel();
				vozilaLbls[i][j] = new JLabel();

				konfiguracijaCelije(tabela[i][j], vozilaLbls[i][j]);
				centar.add(tabela[i][j]);
			}
		}
	}

	private void konfiguracijaCelije(JPanel panel, JLabel label) {
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setBorder(BorderFactory.createEmptyBorder());
		panel.add(label);
		panel.setBackground(Color.LIGHT_GRAY);
	}

	private void bojenje() {
		for (int i = 0; i < redovi; i++) {
			for (int j = 0; j < kolone; j++) {
				tabela[i][j].setBackground(Util.bojaPolja(j, i));
			}
		}
	}

	public static Color bojaVozila(Vozilo vozilo) {
		if (vozilo instanceof Auto)
			return bojaAuta;
		else if (vozilo instanceof Biciklo)
			return bojaBicikla;
		else {
			return bojaTrotineta;
		}
	}

	private void inicijalizacijaVozila() {
		vozila = new ArrayList[redovi][kolone];
		for (int i = 0; i < redovi; i++) {
			for (int j = 0; j < kolone; j++) {
				vozila[i][j] = new ArrayList<>();
			}
		}
	}

	public List<Racun> getRacuni() {
		return racuni;
	}

	public void setRacuni(List<Racun> racuni) {
		this.racuni = racuni;
	}

	public Color getBojaAuta() {
		return bojaAuta;
	}

	public void setBojaAuta(Color bojaAuta) {
		this.bojaAuta = bojaAuta;
	}

	public Color getBojaBicikla() {
		return bojaBicikla;
	}

	public void setBojaBicikla(Color bojaBicikla) {
		this.bojaBicikla = bojaBicikla;
	}

	public Color getBojaskutera() {
		return bojaTrotineta;
	}

	public void setBojaskutera(Color bojaskutera) {
		this.bojaTrotineta = bojaskutera;
	}

	public Color getUnutrasnjaBoja() {
		return unutrasnjaBoja;
	}

	public void setUnutrasnjaBoja(Color unutrasnjaBoja) {
		this.unutrasnjaBoja = unutrasnjaBoja;
	}

	public Color getVanjskaBoja() {
		return vanjskaBoja;
	}

	public void setVanjskaBoja(Color vanjskaBoja) {
		this.vanjskaBoja = vanjskaBoja;
	}

	public Color getBojaPozadine() {
		return bojaPozadine;
	}

	public void setBojaPozadine(Color bojaPozadine) {
		this.bojaPozadine = bojaPozadine;
	}

	public JPanel getDatumPanel() {
		return datumPanel;
	}

	public void setDatumPanel(JPanel datumPanel) {
		this.datumPanel = datumPanel;
	}

	public JLabel getDatumTekst() {
		return datumTekst;
	}

	public void setDatumTekst(JLabel datumTekst) {
		this.datumTekst = datumTekst;
	}

	public JPanel getDugmadPanel() {
		return dugmadPanel;
	}

	public void setDugmadPanel(JPanel dugmadPanel) {
		this.dugmadPanel = dugmadPanel;
	}

	public JButton getDnevniIzvjestajBtn() {
		return dnevniIzvjestajBtn;
	}

	public void setDnevniIzvjestajBtn(JButton dnevniIzvjestajBtn) {
		this.dnevniIzvjestajBtn = dnevniIzvjestajBtn;
	}

	public JButton getSumarniIzvjestajBtn() {
		return sumarniIzvjestajBtn;
	}

	public void setSumarniIzvjestajBtn(JButton sumarniIzvjestajBtn) {
		this.sumarniIzvjestajBtn = sumarniIzvjestajBtn;
	}

	public JButton getSpecijalniIzvjestajBtn() {
		return specijalniIzvjestajBtn;
	}

	public void setSpecijalniIzvjestajBtn(JButton specijalniIzvjestajBtn) {
		this.specijalniIzvjestajBtn = specijalniIzvjestajBtn;
	}

	public JPanel getVrhAplikacije() {
		return vrhAplikacije;
	}

	public void setVrhAplikacije(JPanel vrhAplikacije) {
		this.vrhAplikacije = vrhAplikacije;
	}

	public JPanel[][] getTabela() {
		return tabela;
	}

	public void setTabela(JPanel[][] tabela) {
		this.tabela = tabela;
	}

	public JLabel[][] getVozilaLbls() {
		return vozilaLbls;
	}

	public void setVozilaLbls(JLabel[][] vozilaLbls) {
		this.vozilaLbls = vozilaLbls;
	}

	public List<Vozilo>[][] getVozila() {
		return vozila;
	}

	public void setVozila(List<Vozilo>[][] vozila) {
		this.vozila = vozila;
	}

	public JPanel getCentar() {
		return centar;
	}

	public void setCentar(JPanel centar) {
		this.centar = centar;
	}

	public int getRedovi() {
		return redovi;
	}

	public void setRedovi(int redovi) {
		this.redovi = redovi;
	}

	public int getKolone() {
		return kolone;
	}

	public void setKolone(int kolone) {
		this.kolone = kolone;
	}

}
