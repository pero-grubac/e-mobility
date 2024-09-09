package org.etfbl.pj2.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.etfbl.pj2.izvjestaj.SpecijalniIzvjestaj;
import org.etfbl.pj2.util.Util;
import org.etfbl.pj2.vozilo.Auto;
import org.etfbl.pj2.vozilo.Biciklo;
import org.etfbl.pj2.vozilo.Trotinet;

public class SpecijalniIzvjestajProzor extends JFrame {
	private JTable tabela;
	private int sirinaKolone = 0;

	public SpecijalniIzvjestajProzor(SpecijalniIzvjestaj izvjestaj, String ime) {
		try {
			setSize(new Dimension(100, 100));
			setLocationRelativeTo(null);
			setResizable(false);
			setTitle(ime);
			napraviTabelu(izvjestaj);
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

	private void napraviTabelu(SpecijalniIzvjestaj izvjestaj) {
		try {
			String[] imenaKolona = { "Tip", "Id", "Proizvodjac", "Model", "Cijena", "Nivo baterije",
					"Potrosnja baterije", "Zarada", "Maksimalna brzina", "Autonomija", "Datum kupovine", "Opis" };

			DefaultTableModel model = new DefaultTableModel(imenaKolona, 0) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			for (int i = 0; i < imenaKolona.length; i++)
				if (sirinaKolone < imenaKolona[i].length())
					sirinaKolone = imenaKolona[i].length();

			izvjestaj.getNajboljaVozila().forEach((vozilo, zarada) -> {
				String tip = "";
				String maksimalnaBrzina = "";
				String autonomija = "";
				String datum = "";
				String opis = "";
				if (vozilo instanceof Auto) {
					tip = "Auto";
					datum = ((Auto) vozilo).getDatumKupovine().format(Util.DATE_FORMATTER);
					opis = ((Auto) vozilo).getOpis();
				} else if (vozilo instanceof Biciklo) {
					tip = "Biciklo";
					autonomija = ((Biciklo) vozilo).getAutonomija().toString();
				} else if (vozilo instanceof Trotinet) {
					tip = "Trotinet";
					maksimalnaBrzina = ((Trotinet) vozilo).getMaksimalnaBrzina().toString();
				} else
					tip = "Error";

				Object[] rowData = new Object[] { tip, vozilo.getId(), vozilo.getProizvodjac(), vozilo.getModel(),
						vozilo.getCijena(), vozilo.getNivoBaterije() + "%", vozilo.getPotrosnjaBaterije() + "%", zarada,
						maksimalnaBrzina, autonomija, datum, opis };
				for (int i = 0; i < rowData.length; i++)
					if (sirinaKolone < rowData[i].toString().length())
						sirinaKolone = rowData[i].toString().length();
				model.addRow(rowData);

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
