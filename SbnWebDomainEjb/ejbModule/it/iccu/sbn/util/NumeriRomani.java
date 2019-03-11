/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.iccu.sbn.util;

public final class NumeriRomani {

	private static class Cifra {
		int num;
		String str;

		public Cifra(int num, String str) {
			super();
			this.num = num;
			this.str = str;
		}
	}

	private final static Cifra[] cifre = {
		new Cifra(1000, "M"),
		new Cifra(900, "CM"),
		new Cifra(500, "D"),
		new Cifra(400, "CD"),
		new Cifra(100, "C"),
		new Cifra(90, "XC"),
		new Cifra(50, "L"),
		new Cifra(40, "XL"),
		new Cifra(10, "X"),
		new Cifra(9, "IX"),
		new Cifra(5, "V"),
		new Cifra(4, "IV"),
		new Cifra(1, "I")
	};

	public static final String converti(int x) {
		if (x == 0)
			return "0";

		String str = "";
		int i = 0;
		while (x > 0) {
			if ((x / cifre[i].num) == 0)
				++i;
			else {
				str += cifre[i].str;
				x -= cifre[i].num;
			}
		}
		return str;
	}


	  public static void main(String[] args) {
		  for (int i = 1; i <= 1000; i++)
			  System.out.println(i + "= " + converti(i));
	  }


}
