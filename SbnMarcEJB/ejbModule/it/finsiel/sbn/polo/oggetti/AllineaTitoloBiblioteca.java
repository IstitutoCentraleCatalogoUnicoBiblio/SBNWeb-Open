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
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.orm.viste.Vl_allinea_tit_bib;
import it.iccu.sbn.ejb.model.unimarcmodel.FiltraAllineaTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;

import java.util.List;

import org.apache.log4j.Category;


public class AllineaTitoloBiblioteca extends Vl_allinea_tit_bib
{

	private static final long serialVersionUID = 1627643560277278943L;
	static Category log = Category.getInstance("iccu.serversbnmarc.AllineaTitolo");
    private int nRighe;
    static int MAX_RIGHE = Integer.parseInt(ResourceLoader.getPropertyString("NRO_MAX_CHIEDI_ALLINEA"));

	public AllineaTitoloBiblioteca(){
	}
	/**
	 * Method verificaEsistenza
	 * Alcuni dei parametri vengono passati come vettori in quanto l'azione di ricerca potrebbe coinvolgere
	 * più oggetti dello stesso tipo; pre es: in fase di ricerca della biblioteca possiamo decidere di ricercare
	 * o nessuna od una o più biblioteche. Per costruire la clausola di where legata a questa condizione
	 * dobbiamo settare la variabile relativa al codice biblioteca a "multiplo" (..setCD_BIBLIOTECA("multiplo"))
	 * e memorizzare le ricorrenze di biblioteca attraverso il comando ....settaParametro(TableDao.XXXcdBiblio"+i,
	 * (String)nomeVettore.get(i)).
	 * Nel file xml che contiene la select  occorre inserire
	 *   se utilizzeremo una condizione di OR
	 * 		<opzionale dipende="XXXcd_biblioteca"> AND (</opzionale>
     *		<opzionale dipende="XXXcdBiblio" ciclico="S" separatore=" OR ">cd_biblioteca = XXXcdBiblio</opzionale>
	 *		<opzionale dipende="XXXcd_biblioteca">)</opzionale>
	 *   mentre se utilizzeremo la IN
	 * 		<opzionale dipende="XXXcd_biblioteca"> AND cd_biblioteca IN (</opzionale>
	 *		<opzionale dipende="XXXcdBiblio" ciclico="S" separatore=",">XXXcdBiblio</opzionale>
	 *		<opzionale dipende="XXXcd_biblioteca">)</opzionale>
	 * N.B. cdBiblio è stato scelto solo in funzione di questo esempio.
	 * @param user
	 * @param id
	 * @param cd_polo
	 * @param cd_biblioteca
	 */
	public TableDao verificaEsistenza(
	    String flagAllineamento,
		String cd_polo,
		List cd_biblioteca,
		SbnTipoLocalizza tipoInfo,
		SbnDatavar dataInizio,
		SbnDatavar dataFine,
		FiltraAllineaTitType filtraAllinea,
		boolean differita)
	throws EccezioneDB, EccezioneSbnDiagnostico {

		throw new EccezioneSbnDiagnostico(501);
		/*
		String[] naturaSbn = null;
		GuidaDoc[] guida = null;

		List naturaSbnTableDao = new ArrayList();
		List guidaTableDao = new ArrayList();
		SbnLivello livelloDa = null;
		SbnLivello livelloA = null;

		if (filtraAllinea != null) {
			naturaSbn = filtraAllinea.getNaturaSbn();
			if (naturaSbn.length != 0) {
				for (int j=0; j<naturaSbn.length; j++){
					naturaSbnTableDao.add(naturaSbn[j]);
					log.info("natura --"+j+"-- " + naturaSbn[j]);
				}
			}
			guida = filtraAllinea.getGuida();
			if (guida.length != 0) {
				for (int j=0; j<guida.length; j++){
					guidaTableDao.add((guida[j].getTipoRecord()).toString());
					log.info("guida --"+j+"-- " + (guida[j].getTipoRecord()).toString());
				}
			}
			livelloDa = filtraAllinea.getLivelloAut_Da();
			livelloA = filtraAllinea.getLivelloAut_A();
			if (livelloA == null) livelloA = livelloDa;
			if (livelloDa == null) livelloDa = livelloA;
		}

		Vl_allinea_tit_bib vl_tit_bib = new Vl_allinea_tit_bib();
		vl_tit_bib.setCD_POLO(cd_polo);
		if (cd_biblioteca != null) {
			for (int i=0;i<cd_biblioteca.size();i++){
				vl_tit_bib.settaParametro("XXXcdBiblio"+i,(String)cd_biblioteca.get(i));
				vl_tit_bib.setCD_BIBLIOTECA("multiplo");
				log.info("biblioteca>"+i+"<>"+(String)cd_biblioteca.get(i)+"<<");
			}
		}
		for (int i=0;i<naturaSbnTableDao.size();i++){
			vl_tit_bib.settaParametro("XXXnaturaS"+i,(String)naturaSbnTableDao.get(i));
			vl_tit_bib.setCD_NATURA("multiplo");
			log.info("natura>"+i+"<>"+(String)naturaSbnTableDao.get(i)+"<<");
		}
		for (int i=0;i<guidaTableDao.size();i++){
			vl_tit_bib.settaParametro("XXXtpRecUni"+i,(String)guidaTableDao.get(i));
			vl_tit_bib.setTP_RECORD_UNI("multiplo");
			log.info("tipo Record>"+i+"<>"+(String)guidaTableDao.get(i)+"<<");
		}
		if (livelloDa != null) {
		 	vl_tit_bib.settaParametro("XXXlivelloA"+"0",livelloDa.toString());
			vl_tit_bib.settaParametro("XXXlivelloA"+"1",livelloA.toString());
			vl_tit_bib.setCD_LIVELLO("between");
		}
        Vl_allinea_tit_bibResult vl_tit_bibResult = new Vl_allinea_tit_bibResult(vl_tit_bib);

        nRighe = 0;

		if (dataInizio != null) {
            vl_tit_bibResult.executeCustom("settaFormatoTsVar");
		 	vl_tit_bib.settaParametro(TableDao.XXXts_var_da,dataInizio.getXmlDate() + " 0000000");
			vl_tit_bib.settaParametro(TableDao.XXXts_var_a,dataFine.getXmlDate() + " 2359599");
		}

		if (tipoInfo == SbnTipoLocalizza.POSSESSO) {
		 	vl_tit_bib.settaParametro(TableDao.XXXpossesso,"S");
		}
		if (tipoInfo == SbnTipoLocalizza.GESTIONE) {
		 	vl_tit_bib.settaParametro(TableDao.XXXgestione,"S");
		}
		if (flagAllineamento.equals("1")) {
             vl_tit_bibResult.executeCustom("countPerFlag");
             nRighe = conta(vl_tit_bibResult);
        } else if (flagAllineamento.equals("2")) {
            vl_tit_bibResult.executeCustom("countPerData");
		    nRighe = conta(vl_tit_bibResult);
        }
// da modificare
       if (!differita) {
			if (nRighe > MAX_RIGHE) {

				throw new EccezioneSbnDiagnostico(3089, "Avviso: La richiesta viene schedulata in differita; verrà inviata una e-mail a fine elaborazione");
			}
 		}
		if (flagAllineamento.equals("1")) vl_tit_bibResult.executeCustom("selectPerFlag");
		if (flagAllineamento.equals("2")) vl_tit_bibResult.executeCustom("selectPerData");
        return vl_tit_bibResult;
		//resultTableDao = vl_tit_bibResult.getElencoRisultati();

		//log.info("vettore esistenza "+ resultTableDao.size() + " righe >> "+ nRighe);
		//if (resultTableDao.size() != 0){
		//	return resultTableDao;
		//}else {
		//   log.info("passa da null ");
		//   	return null;
		//}
        */
	}

	public int conta(TableDao tavola) throws EccezioneDB {
		return tavola.getCount();
//        int nrighe = 0;
//        try {
//			ResultSet rs = tavola.getResultSet();
//			while (rs.next()) {
//				nrighe +=rs.getInt(1);
//			}
//			return nrighe;
//		} catch (SQLException ecc) {
//			log.error("Errore nella lettura del COUNT (*) dal resultset");
//			throw new EccezioneDB(1203);
//		}
	}

    public int contaRighe(TableDao tavola) throws EccezioneDB {
		return tavola.getCount();
//        int nrighe = 0;
//        try {
//            ResultSet rs = tavola.getResultSet();
//            while (rs.next()) {
//                nrighe ++;
//            }
//            return nrighe;
//        } catch (SQLException ecc) {
//            log.error("Errore nella lettura del COUNT (*) dal resultset");
//            throw new EccezioneDB(1203);
//        }
    }
    /**
     * @return
     */
    public int getNRighe() {
        return nRighe;
    }

}
