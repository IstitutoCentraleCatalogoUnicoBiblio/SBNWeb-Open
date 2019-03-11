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

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_sog_desResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_descrittore_sogResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.orm.Tr_sog_des;
import it.finsiel.sbn.polo.orm.viste.Vl_descrittore_sog;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


public class SoggettoDescrittore extends Tr_sog_des {



	private static final long serialVersionUID = -5218388388104636828L;

	public SoggettoDescrittore() {
		return;
	}

	public void inserisciLegame(String cid, String did, String primaVoce,
			int posizione, String user) throws IllegalArgumentException, InvocationTargetException, Exception{
        Tr_sog_des tr_sog_des = new Tr_sog_des();
        tr_sog_des.setCID(cid);
        tr_sog_des.setDID(did);
        tr_sog_des.setFL_PRIMAVOCE(primaVoce);
        tr_sog_des.setFL_CANC(" ");
        tr_sog_des.setUTE_VAR(user);

        //almaviva5_20120516 evolutive CFI
        tr_sog_des.setFL_POSIZIONE(posizione);

		Tr_sog_desResult dao = new Tr_sog_desResult(tr_sog_des);
        dao.executeCustom("verificaEsistenzaId");
        List v = dao.getElencoRisultati();
		if (v.size() > 0) {
            dao.update(tr_sog_des);
        } else {
	        tr_sog_des.setUTE_INS(user);
    		dao.insert(tr_sog_des);
        }

	}

	/**
	 * Method controllaEsistenzaLegame.
	 * Controlla l'esistenza del legame fra descrittore e soggetto
	 * Ritorna TRUE qualora avesse trovato il legame soggetto/descrittore
	 *
	 * @param idSoggetto
	 * @param idDescrittore
	 * @throws InfrastructureException
	 */
	public Tr_sog_des controllaEsistenzaLegame(String idSoggetto, String idDescrittore) throws EccezioneDB, InfrastructureException {
		Tr_sog_des tr_sog_des = null;
        List risultati;
		setCID(idSoggetto);
		setDID(idDescrittore);
		Tr_sog_desResult tavola = new Tr_sog_desResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
		risultati = tavola.getElencoRisultati();
		if (risultati.size() > 0)
			tr_sog_des = (Tr_sog_des) tavola.getElencoRisultati().get(0);

		return tr_sog_des;
	}


	/**
	 * Method updateVoceLegame.
	 * dato un legame setta l'attributo primavoce a "N"
	 * @param posizione
	 * @param user
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public boolean updateLegame(int posizione, String user) throws IllegalArgumentException, InvocationTargetException, Exception {
		setFL_PRIMAVOCE("N");
		setUTE_VAR(user);

		//almaviva5_20120516 evolutive CFI
        setFL_POSIZIONE(posizione);

		Tr_sog_desResult dao = new Tr_sog_desResult(this);
		return dao.executeCustom("updatePrimaVoce");
		//return tavola;
	}



	/**
	 * Method leggiLegami.
	 * questo metodo cancella i legami non più esistenti fra un soggetto e descrittori
	 * Viene passato in input "stringheDiLegamiInModifica" contenente la descrizione
	 * dei descrittori inseriti nell'xml con la richiesta di modifica
	 * Vengono confrontati i descrittori contenuti in stringheDiLegamiInModifica con
	 * quelli trovati dalla vista e viene eliminato il legame di quelli non presenti
	 * in stringheDiLegamiInModifica
	 *
	 * @param idSoggetto
	 * @param stringheDiLegamiInModifica
	 * @param user
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void leggiLegami(String idSoggetto, SbnEdizioneSoggettario edizioneSog, String user) throws IllegalArgumentException, InvocationTargetException, Exception {
		Vl_descrittore_sog vl_descrittore_sog = new Vl_descrittore_sog();
        vl_descrittore_sog.setCID(idSoggetto);
		Vl_descrittore_sogResult tavola = new Vl_descrittore_sogResult(vl_descrittore_sog);

		tavola.executeCustom("selectDescrittorePerSoggetto");
		List descrittoriLegati = tavola.getElencoRisultati();

		for (int i = 0; i < descrittoriLegati.size(); i++) {
            boolean isManuale = false;
            Vl_descrittore_sog tb_descrittore = (Vl_descrittore_sog)descrittoriLegati.get(i);
			String fl_PrimaVoce = tb_descrittore.getFL_PRIMAVOCE();
			//almaviva QUI METTO IL CHECK PER VERIFICARE CHE IL FLAG FL_PRIMAVOCE NON SIA SETTATTO A "M" CIOE'
            // MANUALE IN QUESTO CASO NON ELIMINO IL LEGAME
			isManuale = ValidazioneDati.equals(fl_PrimaVoce, "M");

			if (isManuale)	{
				Descrittore d = new Descrittore();
				d.controllaEdizioneDescrittore(edizioneSog, tb_descrittore, user);
			} else
				//descrittore automatico
				cancellaLegame(idSoggetto, tb_descrittore.getDID(), user);

		}

	}

	/**
	 * Method cancellaLegame.
	 *
	 * @param idSoggetto
	 * @param idDescrittore
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void cancellaLegame(
	String idSoggetto,
	String idDescrittore,
	String user) throws IllegalArgumentException, InvocationTargetException, Exception {
		setCID(idSoggetto);
		setDID(idDescrittore);
		setUTE_VAR(user);
		setFL_CANC("S");

		 //almaviva5_20120516 evolutive CFI
        setFL_POSIZIONE(0);

		Tr_sog_desResult dao = new Tr_sog_desResult(this);
		dao.executeCustomUpdate("updateFl_canc");
	}

	/**
	 * questo metodo restituisce true qualora fosse stato trovato un legame
	 * con almeno un soggetto
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public boolean cercaSoggettoPerDescrittore(
	String idDescrittore) throws IllegalArgumentException, InvocationTargetException, Exception {
		setDID(idDescrittore);
		Tr_sog_desResult tavola = new Tr_sog_desResult(this);


		tavola.executeCustom("selectLegameDescrittore");
		int numeroElementi = tavola.getElencoRisultati().size();

		return numeroElementi>0;
	}




}
