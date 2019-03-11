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
package it.finsiel.sbn.polo.oggetti;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_tit_bResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;
/**
 * @author
 */
public class TitoloTitB extends Vl_titolo_tit_b {

	private static final long serialVersionUID = -2108860213549897273L;
	private static Category log = Category.getInstance("it.finsiel.sbn.polo.oggetti.TagMarc");


    public TitoloTitB() {
	}
	public List cercaMonografiaSuperiore(String bid, String cles1, String cles2, String clet1, String clet2) throws IllegalArgumentException, InvocationTargetException, Exception {
        List lista = null;
		Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(this);
		try {

			this.setBID_BASE(bid);

			this.settaParametro(TableDao.XXXky_cles1_t_uguale, cles1);
			this.settaParametro(TableDao.XXXky_cles2_t_uguale, cles2);
			this.settaParametro(TableDao.XXXky_clet1_t_uguale, clet1);
			this.settaParametro(TableDao.XXXky_clet2_t_uguale, clet2);
			if (tavola.executeCustom("selectMonografiaSuperiore")) {
				lista = tavola.getElencoRisultati();
			}

		} catch (EccezioneDB ex) {
			log.error("cercaMonografiaSuperiore --> ", ex);
			lista = null;
			if (tavola != null)

			throw new EccezioneDB(ex.getErrorID(), "Messaggio:" + ex.getMessaggio() + "\nMessage:" + ex.getMessage() + "\nNota:" + ex.getNota(), ex);
		}
		return lista;
	}

    // Inizio Modifica almaviva2 BUG MANTIS 4501 esercizio : inserita nuova select VL_TITOLO_TIT_B_selectMonografiaSuperioreBis
	// per consentire doppio legame M01S
	public List cercaMonografiaSuperioreBis(String bid, String cles1, String cles2, String clet1, String clet2) throws IllegalArgumentException, InvocationTargetException, Exception {
        List lista = null;
		Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(this);
		try {

			this.setBID_BASE(bid);

			this.settaParametro(TableDao.XXXky_cles1_t_uguale, cles1);
			this.settaParametro(TableDao.XXXky_cles2_t_uguale, cles2);
			this.settaParametro(TableDao.XXXky_clet1_t_uguale, clet1);
			this.settaParametro(TableDao.XXXky_clet2_t_uguale, clet2);
			if (tavola.executeCustom("selectMonografiaSuperioreBis")) {
				lista = tavola.getElencoRisultati();
			}

		} catch (EccezioneDB ex) {
			log.error("cercaMonografiaSuperioreBis --> ", ex);
			lista = null;
			if (tavola != null)

			throw new EccezioneDB(ex.getErrorID(), "Messaggio:" + ex.getMessaggio() + "\nMessage:" + ex.getMessage() + "\nNota:" + ex.getNota(), ex);
		}
		return lista;
	}


	public List cercaLegameSuperiore(String bid_base) throws IllegalArgumentException, InvocationTargetException, Exception {
        List lista;
		Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(this);
		try {

			this.setBID_BASE(bid_base);


            tavola.executeCustom("selectLegameSuperiore");
			lista = tavola.getElencoRisultati();

		} catch (EccezioneDB ex) {
			log.error("cercaLegameSuperiore --> ", ex);
			lista = null;
			if (tavola != null)

			throw new EccezioneDB(ex.getErrorID(), "Messaggio:" + ex.getMessaggio() + "\nMessage:" + ex.getMessage() + "\nNota:" + ex.getNota(), ex);
		}
		return lista;
	}

	public List cercaTuttiLegami(String bid_base) throws IllegalArgumentException, InvocationTargetException, Exception {
        List lista;
	  Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(this);
	  try {

	    this.setBID_BASE(bid_base);

	    tavola.executeCustom("selectTitoloPerLegame");
	    lista = tavola.getElencoRisultati();

	  } catch (EccezioneDB ex) {
	    log.error("cercaTuttiLegami --> ", ex);
	    lista = null;
	    if (tavola != null)

	    throw new EccezioneDB(ex.getErrorID(), "Messaggio:" + ex.getMessaggio() + "\nMessage:" + ex.getMessage() + "\nNota:" + ex.getNota(), ex);
	  }
	  return lista;
	}


	public List cercaCollane(String bid, String cles1, String cles2, String clet1, String clet2) throws IllegalArgumentException, InvocationTargetException, Exception {
        List lista = null;
		Vl_titolo_tit_bResult tavola = new Vl_titolo_tit_bResult(this);
		try {

			this.setBID_BASE(bid);

			this.settaParametro(TableDao.XXXky_cles1_t_uguale, cles1);
			this.settaParametro(TableDao.XXXky_cles2_t_uguale, cles2);
			this.settaParametro(TableDao.XXXky_clet1_t_uguale, clet1);
			this.settaParametro(TableDao.XXXky_clet2_t_uguale, clet2);
			if (tavola.executeCustom("selectCollane")) {
				lista = tavola.getElencoRisultati();
			}

		} catch (EccezioneDB ex) {
			log.error("cercaCollane --> ", ex);
			lista = null;
			if (tavola != null)

			throw new EccezioneDB(ex.getErrorID(), "Messaggio:" + ex.getMessaggio() + "\nMessage:" + ex.getMessage() + "\nNota:" + ex.getNota(), ex);
		}
		return lista;
	}
}
