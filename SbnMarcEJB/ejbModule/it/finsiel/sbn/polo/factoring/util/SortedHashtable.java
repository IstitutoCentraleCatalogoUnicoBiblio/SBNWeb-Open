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
package it.finsiel.sbn.polo.factoring.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Category;

/**
 * CLASSE PER LA GESTIONE SORTED DI UNA HASHTABLE
 * <p/>
 * Provo ad usare un oggetto TreeMap con Interfaccia Ordinata di tipo SortedMap
 * <p/>
 *
 * @author
 * @author
 */
public class SortedHashtable  extends TreeMap implements SortedMap
{

	private static final long serialVersionUID = -768855554465116649L;

	static  Category log = Category.getInstance("iccu.box.utility.modelli.SortedInfo");

	DescrizioneComparator desc_comparator = new DescrizioneComparator() ;

	/**
	 * Assegno il mio comparator
	 * <br>
	 * Ho così personalizzato i criteri di uguaglianza
	 * Vedi DescrizioneComparator sempre nello stesso package
	 * <br>
	 * @param nessuno
	 * @return il comparator
	 */
	public Comparator comparator()
	{
		return desc_comparator;
	}

	/**
	 * Test oggetto
	 * <br>
	 * Verifico se il sort funziona correttamente
	 * <br>
	 * @param  argomenti in input
	 * @return void
	 */
	public static void main(String[] args)
	{
		SortedHashtable prova = new SortedHashtable();
		prova.put("Pippo", "cane");
		prova.put("Pluto", "tossico");
		prova.put("Minnie", "casalinga");
		prova.put("Topolino", "detective");

		// Prova "standard"
		// Queste stesse righe vanno nelle Jsp
		// La Set (keySet()) è una Collection con valori univoci (chiavi)
		Iterator it_keys = prova.keySet().iterator() ;
		Iterator it_values = prova.values().iterator() ;

		while ( it_keys.hasNext())
		{
			String nome =(String)it_keys.next();
			log.debug("NOME:"+nome+":") ;
			String valore = (String)it_values.next() ;
			log.debug("VALORE:"+valore+":") ;
		}
	}
}
