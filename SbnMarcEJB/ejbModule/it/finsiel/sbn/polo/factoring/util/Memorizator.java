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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Category;



/**
  * Classe Memorizator
 * <p>
 *
 * La classe Memorizator permette ai vari factoring di memorizzare l'andamento
 * delle operazioni effettuate.
 * Opera sulla Tavola TBTIM
 * </p>
 * @author
 */
public class Memorizator
{
	static Category log = Category.getInstance("iccu.amministrazione.monitoraggio.Memorizator");

	public Memorizator()
	{

	}

    static SimpleDateFormat Format = new SimpleDateFormat("yyyyMMddHHmmss.SSS") ;

	/**
	 * Ritorna data/ora attuale
	 * <br>
	 * Rispetta il formato TIMESTAMP (stringa nel formato YYYYMMDDhhmmss.S)
	 * <br>
	 * @param  nessuno
	 * @return String data & ora
	 */
	public String getTimestamp()
	{
		Date now = new Date(System.currentTimeMillis()) ;
        String original_date = Format.format(now) ;

        if (original_date.length() > 16)
        	original_date = original_date.substring(0,16) ;

		return original_date ;
	}






}
