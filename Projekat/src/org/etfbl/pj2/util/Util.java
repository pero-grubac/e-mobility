package org.etfbl.pj2.util;

import java.awt.Color;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

import org.etfbl.pj2.polje.Polje;
import org.etfbl.pj2.vozilo.Vozilo;

public class Util {
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
	public static transient final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy.");
	public static final DateTimeFormatter DATE_FOR_FILE_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy HH.mm");

	private static Color unutrasnjaBoja = Color.BLUE;
	private static Color vanjskaBoja = Color.WHITE;

	/**
	 * Generiše nasumičan string dužine {@code length} koristeći UTF-8 karaktere.
	 *
	 * @param length dužina nasumičnog stringa
	 * @return nasumično generisan string
	 */
	public static String randomString(int length) {
		byte[] niz = new byte[length];
		new Random().nextBytes(niz);
		String randomString = new String(niz, Charset.forName("UTF-8"));
		return randomString;
	}

	/**
	 * Pronalazi najkraću putanju između početnog i krajnjeg polja koristeći
	 * algoritam Breadth-First Search (BFS).
	 *
	 * @param pocetak početno polje
	 * @param kraj    krajnje polje
	 * @return lista polja koja čine najkraću putanju od početnog do krajnjeg polja
	 */
	public static List<Polje> najkracaPutanja(Polje pocetak, Polje kraj) {
		int n = 20;
		int m = 20;

		Queue<Polje> red = new java.util.LinkedList<Polje>();
		red.offer(pocetak);

		boolean[][] posecen = new boolean[n][m];
		Polje[][] predhodni = new Polje[n][m];

		while (!red.isEmpty()) {
			Polje trenutno = red.poll();
			int x = trenutno.getX();
			int y = trenutno.getY();

			// kraj
			if (x == kraj.getX() && y == kraj.getY())
				break;

			if (posecen[x][y])
				continue;

			posecen[x][y] = true;

			int[] dx = { -1, 1, 0, 0 };
			int[] dy = { 0, 0, -1, 1 };

			for (int i = 0; i < 4; i++) {
				int newX = x + dx[i];
				int newY = y + dy[i];

				// Provjeravamo da li je novi korak validan i ne posjećen
				if (validnost(newX, newY, n, m) && !posecen[newX][newY]) {
					red.offer(new Polje(newX, newY));
					predhodni[newX][newY] = trenutno;
				}
			}
		}

		List<Polje> najkracaPutanja = new ArrayList<Polje>();
		Polje temp = kraj;
		while (temp != null) {
			najkracaPutanja.add(temp);
			temp = predhodni[temp.getX()][temp.getY()];
		}
		Collections.reverse(najkracaPutanja);

		return najkracaPutanja;
	}

	/**
	 * Provjerava da li su koordinate (x, y) unutar granica definisanih
	 * vrijednostima {@code n} i {@code m}.
	 *
	 * @param x koordinata po x osi
	 * @param y koordinata po y osi
	 * @param n maksimalna vrijednost za x
	 * @param m maksimalna vrijednost za y
	 * @return {@code true} ako su koordinate validne, u suprotnom {@code false}
	 */
	private static boolean validnost(int x, int y, int n, int m) {
		return (x >= 0 && x < n && y >= 0 && y < m);
	}

	/**
	 * Kreira listu ID-ova vozila iz liste objekata klase {@code Vozilo}.
	 *
	 * @param vozila lista vozila
	 * @return lista ID-ova vozila
	 */
	public static List<String> listaIdVozila(List<Vozilo> vozila) {
		return vozila.stream().map(Vozilo::getId).collect(Collectors.toList());
	}

	/**
	 * Određuje boju polja na osnovu njegovih koordinata. Ako su koordinate unutar
	 * centralnog dijela (5-14), vraća unutrašnju boju, u suprotnom vraća spoljašnju
	 * boju.
	 *
	 * @param x koordinata po x osi
	 * @param y koordinata po y osi
	 * @return boja polja
	 */
	public static Color bojaPolja(int x, int y) {
		if (x < 5 || x > 14 || y < 5 || y > 14)
			return vanjskaBoja;
		return unutrasnjaBoja;
	}
}
