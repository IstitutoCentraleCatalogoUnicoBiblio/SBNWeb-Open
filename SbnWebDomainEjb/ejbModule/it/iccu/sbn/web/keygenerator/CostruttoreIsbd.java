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
package it.iccu.sbn.web.keygenerator;

import it.iccu.sbn.web.integration.sbnmarc.SbnMarcGateway;

import org.jboss.logging.Logger;

/**
 * Implementa la classe astratta IsbdMusicale di Akros, ed in particolare il
 * metodo getDescrizioneCodice per l'accesso ai dati.
 *
 * @author Maurizio Alvino
 *
 */
public class CostruttoreIsbd extends SbnMarcGateway {

	private static Logger log = Logger.getLogger(CostruttoreIsbd.class);

	private String isbd;

	public void definisciISBDtitUniMusicale(String a_929, String b_929,
			String c_929, String e_929, String f_929, String g_929,
			String h_929, String i_929, String[] a_928, String b_928,
			String c_928) {
		try {
			isbd = getGateway().definisciIsbdTitUniMusicale(a_929, b_929,
					c_929, e_929, f_929, g_929, h_929, i_929, a_928, b_928,	c_928);
		} catch (Exception e) {
			isbd = null;
			gateway = null;
			log.error("", e);
		}

	}

	public String getIsbd() {
		return isbd;
	}

}
