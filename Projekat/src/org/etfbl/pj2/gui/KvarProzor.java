package org.etfbl.pj2.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.etfbl.pj2.iznajmljivanje.Iznajmljivanje;
import org.etfbl.pj2.racun.Racun;
import org.etfbl.pj2.vozilo.Auto;
import org.etfbl.pj2.vozilo.Biciklo;
import org.etfbl.pj2.vozilo.Trotinet;
import org.etfbl.pj2.vozilo.Vozilo;

public class KvarProzor extends JFrame {
	private JTable tabela;
	private int sirinaKolone = 0;

	public KvarProzor(List<Racun> racuni) {
		try {
			setSize(new Dimension(100, 100));
			setLocationRelativeTo(null);
			setResizable(false);
			setTitle("Iznajmljivanja");
			napraviTabelu(racuni);
			azurirajVelicinuTabele();
			pack();
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void azurirajVelicinuTabele() {
		int sirina = tabela.getColumnModel().getColumn(0).getPreferredWidth() * tabela.getColumnCount();
		int visina = tabela.getRowHeight() * tabela.getRowCount();
		tabela.setPreferredScrollableViewportSize(new Dimension(sirina + 44, visina));

	}

	private void napraviTabelu(List<Racun> racuni) {
		try {
			String[] imenaKolona = { "Tip", "Id", "Vrijeme", "Opis" };

			DefaultTableModel model = new DefaultTableModel(imenaKolona, 0) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			for (int i = 0; i < imenaKolona.length; i++)
				if (sirinaKolone < imenaKolona[i].length())
					sirinaKolone = imenaKolona[i].length();

			racuni.stream().filter(racun -> racun.getKvar() != null).forEach(racun -> {
				String tip = "";

				if (racun.getVozilo() instanceof Auto) {
					tip = "Auto";
				} else if (racun.getVozilo() instanceof Biciklo) {
					tip = "Biciklo";
				} else if (racun.getVozilo() instanceof Trotinet) {
					tip = "Trotinet";
				}
				Object[] podaci = new Object[] { tip, racun.getVozilo().getId(), racun.getKvar().getVrijemeKvara(),
						racun.getKvar().getOpis() };
				for (int i = 0; i < podaci.length; i++)
					if (sirinaKolone < podaci[i].toString().length())
						sirinaKolone = podaci[i].toString().length();
				model.addRow(podaci);

			});
			tabela = new JTable(model);
			tabela.setAutoCreateRowSorter(true);

			azurirajSirinuCelija();
			tabela.getTableHeader().setReorderingAllowed(false);

			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			for (int i = 0; i < tabela.getColumnCount(); i++) {
				tabela.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			}
			JScrollPane scrollPane = new JScrollPane(tabela);
			add(scrollPane, BorderLayout.CENTER);
		} catch (Exception e) {
			System.err.println("Error " + e.getMessage());
		}

	}

	private void azurirajSirinuCelija() {
		final TableColumnModel columnModel = tabela.getColumnModel();
		int maxColumnWidth = 15;
		for (int column = 0; column < tabela.getColumnCount(); column++) {
			TableCellRenderer headerRenderer = tabela.getTableHeader().getDefaultRenderer();
			Component headerComp = headerRenderer.getTableCellRendererComponent(tabela,
					tabela.getColumnModel().getColumn(column).getHeaderValue(), false, false, 0, column);
			maxColumnWidth = Math.max(maxColumnWidth, headerComp.getPreferredSize().width + 1);

			for (int row = 0; row < tabela.getRowCount(); row++) {
				TableCellRenderer renderer = tabela.getCellRenderer(row, column);
				Component comp = tabela.prepareRenderer(renderer, row, column);
				maxColumnWidth = Math.max(maxColumnWidth, comp.getPreferredSize().width + 1);
			}

		}
		for (int column = 0; column < tabela.getColumnCount(); column++) {
			columnModel.getColumn(column).setPreferredWidth(maxColumnWidth);
		}
	}
}
