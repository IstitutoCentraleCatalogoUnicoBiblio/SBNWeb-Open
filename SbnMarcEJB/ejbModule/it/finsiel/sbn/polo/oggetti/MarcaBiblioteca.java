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
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_mar_bibResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.orm.Tb_marca;
import it.finsiel.sbn.polo.orm.Tr_mar_bib;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;


public class MarcaBiblioteca extends Tr_mar_bib {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2586131515678511443L;
	private static Category log = Category.getInstance("iccu.serversbnmarc.MarcaBiblioteca");



	public MarcaBiblioteca(){
		super();
	}

	/**
	 * Method inserisciTr_mar_bib.
	 * @param user
	 * @param id
	 * @param codice_polo
	 * @param codice_biblioteca
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public boolean inserisciTr_mar_bib(
		String user,
		String id,
		String codice_polo,
		String codice_biblioteca) throws IllegalArgumentException, InvocationTargetException, Exception {

		Tr_mar_bib tr_mar_bib = new Tr_mar_bib();
		tr_mar_bib.setMID(id);
		tr_mar_bib.setCD_POLO(codice_polo);
		tr_mar_bib.setCD_BIBLIOTECA(codice_biblioteca);
		tr_mar_bib.setFL_CANC(" ");
		tr_mar_bib.setFL_ALLINEA(" ");
		tr_mar_bib.setFL_ALLINEA_SBNMARC(" ");
		tr_mar_bib.setUTE_VAR(user);
		tr_mar_bib.setUTE_INS(user);
		Tr_mar_bibResult tr_mar_bibResult = new Tr_mar_bibResult(tr_mar_bib);


        tr_mar_bibResult.executeCustom("verificaEsistenza");
        if (tr_mar_bibResult.getElencoRisultati().size()>0)
            tr_mar_bibResult.update(tr_mar_bib);
        else
    		tr_mar_bibResult.insert(tr_mar_bib);
		return true;

	}


	/**
	 * Method verificaEsistenzaTr_mar_bib.
	 * @param user
	 * @param id
	 * @param codice_polo
	 * @param codice_biblioteca
     * verifica esistenza record tr_mar_bib
	 * @throws InfrastructureException
	 */
	public List verificaEsistenzaTr_mar_bib(String id, String cd_polo, String cd_biblioteca) throws EccezioneDB, InfrastructureException {
		Tr_mar_bib tr_mar_bib = new Tr_mar_bib();
		tr_mar_bib.setMID(id);
		tr_mar_bib.setCD_POLO(cd_polo);
		tr_mar_bib.setCD_BIBLIOTECA(cd_biblioteca);
		Tr_mar_bibResult tr_mar_bibResult = new Tr_mar_bibResult(tr_mar_bib);


		tr_mar_bibResult.selectPerKey(tr_mar_bib.leggiAllParametro());
        List resultTableDao = tr_mar_bibResult.getElencoRisultati();

		log.info("vettore esistenza"+ resultTableDao.size());
		if (resultTableDao.size() != 0) {
              return resultTableDao;
		} else {
              return null;
        }
	}

	/**
	 * Method verificaEsistenzaTr_mar_bib con 2 parametri
	 * @param id
	 * @param codice_polo
     * verifica esistenza record tr_mar_bib
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public List verificaEsistenzaTr_mar_bib(String id, String cd_polo) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tr_mar_bib tr_mar_bib = new Tr_mar_bib();
		tr_mar_bib.setMID(id);
		tr_mar_bib.setCD_POLO(cd_polo);
		Tr_mar_bibResult tr_mar_bibResult = new Tr_mar_bibResult(tr_mar_bib);


		tr_mar_bibResult.executeCustom("selectPerPolo");
        List resultTableDao = tr_mar_bibResult.getElencoRisultati();

		log.info("vettore esistenza"+ resultTableDao.size());
		if (resultTableDao.size() != 0) {
              return resultTableDao;
		} else {
              return null;
        }
	}

	/**
	 * Method aggiornaTr_mar_bib.
	 * @param user
	 * @param id
	 * @param codice_polo
	 * @param codice_biblioteca
	 * aggiorna il record in tr_mar_bib con fl_canc = 'S'
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public boolean aggiornaTr_mar_bib(
		String user,
		String id,
		String cd_polo,
		String cd_biblioteca
		) throws IllegalArgumentException, InvocationTargetException, Exception {

		Tr_mar_bib tr_mar_bib = new Tr_mar_bib();
		tr_mar_bib.setMID(id);
		tr_mar_bib.setFL_CANC("S");
//???
		tr_mar_bib.setFL_ALLINEA(" ");
		tr_mar_bib.setCD_POLO(cd_polo);
		tr_mar_bib.setCD_BIBLIOTECA(cd_biblioteca);
	  	SbnDatavar data_operazione = new SbnDatavar(System.currentTimeMillis()) ;
		tr_mar_bib.setTS_VAR(ConverterDate.SbnDataVarToDate(data_operazione));
		tr_mar_bib.setUTE_VAR(user);
		Tr_mar_bibResult tr_mar_bibResult = new Tr_mar_bibResult(tr_mar_bib);

		tr_mar_bibResult.executeCustom("updatePerModifica");
        int nroRec = tr_mar_bibResult.getRowsUpdate();
   		log.info("aggiornamenti eseguiti "+ nroRec);

		if (nroRec != 0){
			return true;
		}else {
			return false;
		}

	}

	public boolean allineaTr_mar_bib(
		String user,
		Tb_marca marca,
		String cd_polo,
		String cd_biblioteca) throws IllegalArgumentException, InvocationTargetException, Exception{

		Tr_mar_bib tr_mar_bib = new Tr_mar_bib();
		tr_mar_bib.setMID(marca.getMID());
		tr_mar_bib.setFL_ALLINEA(" ");
		tr_mar_bib.setFL_ALLINEA_SBNMARC(" ");
		tr_mar_bib.setCD_POLO(cd_polo);
		tr_mar_bib.setCD_BIBLIOTECA(cd_biblioteca);
        if (marca.getFL_CANC().equals("S")) {
            tr_mar_bib.setFL_CANC("S");
        }else tr_mar_bib.setFL_CANC(" ");
		tr_mar_bib.setUTE_VAR(user);
		Tr_mar_bibResult tr_mar_bibResult = new Tr_mar_bibResult(tr_mar_bib);
		tr_mar_bibResult.executeCustom("updatePerModifica");
        int nroRec = tr_mar_bibResult.getRowsUpdate();
   		log.info("aggiornamenti eseguiti "+ nroRec);

		if (nroRec != 0){
			return true;
		}else {
			return false;
		}
	}


	/**
	 * Method verificaLocalizzazioniTr_mar_bib.
	 * @param idMarca
	 * @param polo
	 * @return boolean
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public boolean verificaLocalizzazioniTr_mar_bib(
		String idMarca,
		String polo) throws IllegalArgumentException, InvocationTargetException, Exception {

		setMID(idMarca);
		setCD_POLO(polo);
		Tr_mar_bibResult tr_mar_bibResult = new Tr_mar_bibResult(this);


		tr_mar_bibResult.executeCustom("selectPerPoloDiverso");
        List resultTableDao = tr_mar_bibResult.getElencoRisultati();

		if (resultTableDao.size() != 0){
			return true;
		}else {
			return false;
		}
	}

	public void aggiornaPerAllinea(
		String user,
		String mid,
		String cd_polo,
		String cd_biblioteca) throws IllegalArgumentException, InvocationTargetException, Exception{
		setMID(mid);
		setFL_ALLINEA("S");
		setCD_POLO(cd_polo);
		setCD_BIBLIOTECA(cd_biblioteca);
		setUTE_VAR(user);
		Tr_mar_bibResult tr_mar_bibResult = new Tr_mar_bibResult(this);
		tr_mar_bibResult.executeCustom("updatePerModifica");
	}

    public List cercaPerAllineamento(String mid, String polo) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_mar_bib tr_tit_bib = new Tr_mar_bib();
        tr_tit_bib.setMID(mid);
        tr_tit_bib.setCD_POLO(polo);
        Tr_mar_bibResult tr_tit_bibResult = new Tr_mar_bibResult(tr_tit_bib);


        tr_tit_bibResult.executeCustom("selectPerAllineamento");
        List resultTableDao = tr_tit_bibResult.getElencoRisultati();

        return resultTableDao;
    }

    public void aggiornaTuttiFlAllinea(Tr_mar_bib tb) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_mar_bibResult tr_tit_bibResult = new Tr_mar_bibResult(tb);

        tr_tit_bibResult.executeCustomUpdate("updateTuttiFlag");

    }


}
