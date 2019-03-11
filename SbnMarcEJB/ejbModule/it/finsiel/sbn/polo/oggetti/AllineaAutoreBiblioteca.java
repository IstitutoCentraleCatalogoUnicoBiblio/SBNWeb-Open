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
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.orm.viste.Vl_allinea_aut_bib;
import it.iccu.sbn.ejb.model.unimarcmodel.FiltraAllineaTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;

import java.util.List;

import org.apache.log4j.Category;


public class AllineaAutoreBiblioteca extends Vl_allinea_aut_bib
{

	private static final long serialVersionUID = -5954099505635521366L;
	static Category log = Category.getInstance("iccu.serversbnmarc.AllineaAutore");
	public AllineaAutoreBiblioteca(){
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
	public TableDao verificaEsistenzaVl_allinea_aut_bib(
	    String flagAllineamento,
		String cd_polo,
		List cd_biblioteca,
		SbnTipoLocalizza tipoInfo,
		SbnDatavar dataInizio,
		SbnDatavar dataFine,
		FiltraAllineaTitType filtraAllinea,
		boolean differita)
	throws EccezioneDB, EccezioneSbnDiagnostico {
		/*
		SbnLivello livelloDa = null;
		SbnLivello livelloA = null;

		if (filtraAllinea != null) {
			livelloDa = filtraAllinea.getLivelloAut_Da();
			livelloA = filtraAllinea.getLivelloAut_A();
			if (livelloA == null) livelloA = livelloDa;
			if (livelloDa == null) livelloDa = livelloA;
		}

		Vl_allinea_aut_bib vl_aut_bib = new Vl_allinea_aut_bib();
		vl_aut_bib.setCD_POLO(cd_polo);
		if (cd_biblioteca != null) {
			for (int i=0;i<cd_biblioteca.size();i++){
				vl_aut_bib.settaParametro("XXXcdBiblio"+i,(String)cd_biblioteca.get(i));
				vl_aut_bib.setCD_BIBLIOTECA("multiplo");
				log.info("biblioteca>"+i+"<>"+(String)cd_biblioteca.get(i)+"<<");
			}
		}

		if (livelloDa != null) {
		 	vl_aut_bib.settaParametro("XXXlivelloA"+"0",livelloDa.toString());
			vl_aut_bib.settaParametro("XXXlivelloA"+"1",livelloA.toString());
			vl_aut_bib.setCD_LIVELLO("between");
		}

        Vl_allinea_aut_bibResult vl_aut_bibResult = new Vl_allinea_aut_bibResult(vl_aut_bib);

        int nRighe = 0;

        if (dataInizio != null) {
            vl_aut_bibResult.executeCustom("settaFormatoTsVar");
            vl_aut_bib.settaParametro(TableDao.XXXts_var_da,dataInizio.getXmlDate() + " 0000000");
            vl_aut_bib.settaParametro(TableDao.XXXts_var_a,dataFine.getXmlDate() + " 2359599");
        }


		if (!differita) {
			if (flagAllineamento.equals("1")) vl_aut_bibResult.executeCustom("countPerFlag");
			if (flagAllineamento.equals("2")) vl_aut_bibResult.executeCustom("countPerData");
			nRighe = conta(vl_aut_bibResult);
			int maxRighe = Integer.parseInt(ResourceLoader.getPropertyString("RIGHE_PER_BLOCCO_SBNMARC"));
			if ((nRighe - maxRighe) > 0) {
				throw new EccezioneSbnDiagnostico(3089, "Avviso: La richiesta viene schedulata in differita; verrà inviata una e-mail a fine elaborazione");
			}
		}
		if (flagAllineamento.equals("1")) vl_aut_bibResult.executeCustom("selectPerFlag");
		if (flagAllineamento.equals("2")) vl_aut_bibResult.executeCustom("selectPerData");
        return vl_aut_bibResult;
        */
        throw new EccezioneSbnDiagnostico(501);
	}

	public int conta(TableDao tavola) throws EccezioneDB {
    	return tavola.getCount();
	}


}
