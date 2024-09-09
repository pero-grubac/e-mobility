package org.etfbl.pj2.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.etfbl.pj2.izvjestaj.DnevniIzvjestaj;
import org.etfbl.pj2.izvjestaj.SumarniIzvjestaj;

public class IzvjestajProzor extends JFrame {
	private JTable tabela;

	public IzvjestajProzor(DnevniIzvjestaj izvjestaj, String ime) {
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

	private void napraviTabelu(DnevniIzvjestaj izvjestaj) {
		List<String> imenaKolonaLista = new ArrayList<>(Arrays.asList("Ukupni prihod", "Popust", "Promocija",
				"Troskovi odrzavanja", "Troskovi popravke", "Unutrsanji dio", "Vanjski dio"));

		List<Object> podaciLista = new ArrayList<Object>(Arrays.asList(izvjestaj.getUkupniPrihod().toString(),
				izvjestaj.getUkupniPopust().toString(), izvjestaj.getUkupnePromocije().toString(),
				izvjestaj.getUkupniTroskoviOdrzavanja(), izvjestaj.getUkupniTroskoviPopravke().toString(),
				izvjestaj.getUkupnaZaradaUVanjskomDijelu().toString(),
				izvjestaj.getUkupnaZaradaUUnutrasnjemDijelu().toString()));
		if (izvjestaj instanceof SumarniIzvjestaj) {
			imenaKolonaLista.add("Porez");
			imenaKolonaLista.add("Trosak");
			podaciLista.add(((SumarniIzvjestaj) izvjestaj).getUkupniPorez());
			podaciLista.add(((SumarniIzvjestaj) izvjestaj).getUkupniTrosak());
		}

		String[] imenaKolona = imenaKolonaLista.toArray(new String[0]);
		Object[] podaci = podaciLista.toArray(new Object[0]);
		DefaultTableModel model = new DefaultTableModel(imenaKolona, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tabela = new JTable(model);
		tabela.setAutoCreateRowSorter(true);

		model.addRow(podaci);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tabela.getColumnCount(); i++) {
			tabela.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		azurirajSirinuCelija();
		tabela.setShowGrid(false);
		tabela.setIntercellSpacing(new Dimension(0, 0));

		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane, BorderLayout.CENTER);

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
