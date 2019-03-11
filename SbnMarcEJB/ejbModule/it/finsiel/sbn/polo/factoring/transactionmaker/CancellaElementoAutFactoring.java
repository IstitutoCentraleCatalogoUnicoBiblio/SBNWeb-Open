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
package it.finsiel.sbn.polo.factoring.transactionmaker;

import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.CancellaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOggetto;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;

import org.apache.log4j.Category;


/**
 * Classe CancellaElementoAutFactoring
 * <p>
 * Classe che esegue l'operazione di CancellaElementoAut, richiesta in input via
 * xml.
 * </p>
 */
public abstract class CancellaElementoAutFactoring extends CancellaFactoring{
    //file di log
    static Category log = Category.getInstance("iccu.sbnmarcserver.CancellaElementoAutFactoring");


	private SbnOggetto tipoOggetto;
	private String idCancella;


    // Attributi da XML-input
//    private CancellaType			elementoAut ;
//    private CercaDatiAutType    	datiElementoAut ;

   /**
    * Metodo costruttore classe di factoring
    * <p>
    * Riceve il root object da cui estrapolare le informazioni XML ricevute in input
    * riempie gli oggetti (CASTOR) mappando i dati provenienti dall XML
    * </p>
    * @param  SBNMarc oggetto radice XML in input
    * @return istanza oggetto (default)
    */
    //costruttore
	public CancellaElementoAutFactoring(SBNMarc input_root_object) {
        super(input_root_object) ;
//		getFactoring(input_root_object);
		tipoOggetto = factoring_object.getTipoOggetto();
		idCancella = factoring_object.getIdCancella();
        //elementoAut = factoring_object.getCercaElementoAut() ;
//        datiElementoAut  = elementoAut.getCercaDatiAut() ;
    }

   /**
    * getFactoring - ritorna il Factoring opportuno
    * <p>
    * Metodo che lancia il Factoring verificando le informazioni ricevute in input
    * La Request verificata è di tipo SBnRequest (XML)
    * </p>
    * @param  nessuno
    * @return void
    */
    static public Factoring getFactoring(SBNMarc sbnmarcObj)
        throws EccezioneFactoring
    {
        Factoring current_factoring = null;
		CancellaType factoring_object = sbnmarcObj.getSbnMessage().getSbnRequest().getCancella();
        log.debug("STO PER VERIFICARE COSA LANCIARE");
        SbnAuthority authority = factoring_object.getTipoOggetto().getTipoAuthority();
		if (authority.getType() == SbnAuthority.AU_TYPE){
            current_factoring = new CancellaAutore(sbnmarcObj);
        } else if (authority.getType() == SbnAuthority.TU_TYPE){
                current_factoring = new CancellaTitoloUniforme(sbnmarcObj);
        } else if (authority.getType() == SbnAuthority.UM_TYPE){
                current_factoring = new CancellaTitoloUniformeMusica(sbnmarcObj);
        }else if (authority.getType() == SbnAuthority.MA_TYPE){
            current_factoring = new CancellaMarca(sbnmarcObj);
        }else if (authority.getType() == SbnAuthority.SO_TYPE){
            current_factoring = new CancellaSoggetto(sbnmarcObj);
        }else if (authority.getType() == SbnAuthority.LU_TYPE){
            current_factoring = new CancellaLuogo(sbnmarcObj);
        }else if (authority.getType() == SbnAuthority.CL_TYPE){
            current_factoring = new CancellaClasse(sbnmarcObj);
        }else if (authority.getType() == SbnAuthority.AB_TYPE){
            current_factoring = new CancellaAbstract(sbnmarcObj);
        }else if (authority.getType() == SbnAuthority.DE_TYPE){
            current_factoring = new CancellaDescrittore(sbnmarcObj);
        }else if (authority.getType() == SbnAuthority.TH_TYPE){
            current_factoring = new CancellaTermineThesauro(sbnmarcObj);
        }else throw new EccezioneFactoring(100);
        //log.info("HO CREATO IL CURRENT FACTORING:"+current_factoring+":");
        return current_factoring;
    }


	public String getIdAttivita() {
		return CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028;
	}


}
