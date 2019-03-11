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
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaTitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;

import org.apache.log4j.Logger;



/**
 * Classe CercaElementoAutFactoring
 * <p>
 * Classe che esegue l'operazione di CercaElementoAut, richiesta in input via xml.
 * Attiva il factoring di ricerca opportuno secondo tipoAuthority
 * </p>
 */
public abstract class CercaElementoAutFactoring extends CercaFactoring {

    //file di log
    protected static Logger log = Logger.getLogger("iccu.sbnmarcserver.CercaElementoAutFactoring");


    // Attributi da XML-input
    protected CercaElementoAutType      elementoAut ;
    protected CercaDatiAutType    datiElementoAut ;



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
    public CercaElementoAutFactoring(SBNMarc input_root_object)
    {
        super(input_root_object) ;

        elementoAut = factoring_object.getCercaElementoAut() ;
        if (elementoAut != null)
            datiElementoAut  = elementoAut.getCercaDatiAut() ;
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
		CercaType factoring_object = sbnmarcObj.getSbnMessage().getSbnRequest().getCerca();

        log.debug("STO PER VERIFICARE COSA LANCIARE");
        // Creo il factoring (valutando input)
        if (factoring_object.getCercaElementoAut().getCercaDatiAut().getTipoAuthority().getType() == SbnAuthority.AU_TYPE)
        {
            current_factoring = new CercaAutore(sbnmarcObj);

        } else if(factoring_object.getCercaElementoAut().getCercaDatiAut().getTipoAuthority().getType() == SbnAuthority.LU_TYPE){
        	current_factoring = new CercaLuogo(sbnmarcObj);
        } else if(factoring_object.getCercaElementoAut().getCercaDatiAut().getTipoAuthority().getType() == SbnAuthority.MA_TYPE){
        	current_factoring = new CercaMarca(sbnmarcObj);
        } else if(factoring_object.getCercaElementoAut().getCercaDatiAut().getTipoAuthority().getType() == SbnAuthority.CL_TYPE){
        	current_factoring = new CercaClasse(sbnmarcObj);
        }else if(factoring_object.getCercaElementoAut().getCercaDatiAut().getTipoAuthority().getType() == SbnAuthority.RE_TYPE){
        	current_factoring = new CercaRepertorio(sbnmarcObj);
        }else if(factoring_object.getCercaElementoAut().getCercaDatiAut().getTipoAuthority().getType() == SbnAuthority.SO_TYPE){
        	current_factoring = new CercaSoggetto(sbnmarcObj);
        }else if(factoring_object.getCercaElementoAut().getCercaDatiAut().getTipoAuthority().getType() == SbnAuthority.DE_TYPE){
        	current_factoring = new CercaDescrittore(sbnmarcObj);
        }else if(factoring_object.getCercaElementoAut().getCercaDatiAut().getTipoAuthority().getType() == SbnAuthority.AB_TYPE){
        	current_factoring = new CercaAbstract(sbnmarcObj);
        }else if(factoring_object.getCercaElementoAut().getCercaDatiAut().getTipoAuthority().getType() == SbnAuthority.TH_TYPE){
        	current_factoring = new CercaTermineThesauro(sbnmarcObj);
        }else if(factoring_object.getCercaElementoAut().getCercaDatiAut().getTipoAuthority().getType() == SbnAuthority.TU_TYPE){
            current_factoring = new CercaTitoloUniforme(sbnmarcObj);
        }else if(factoring_object.getCercaElementoAut().getCercaDatiAut() instanceof CercaTitoloUniformeMusicaType
             || factoring_object.getCercaElementoAut().getCercaDatiAut().getTipoAuthority().getType() == SbnAuthority.UM_TYPE){
            current_factoring = new CercaTitoloUniformeMusica(sbnmarcObj);
        }else
            throw new EccezioneFactoring(100);
        //log.info("HO CREATO IL CURRENT FACTORING:"+current_factoring+":");

        // Mi faccio tornare l'ennesimo livello, quello che esegue l'azione
        //if (current_factoring != null)
          //  current_factoring = current_factoring.getFactoring(sbnmarcObj) ;

        return current_factoring;
    }

	public String getIdAttivita() {
		return CodiciAttivita.getIstance().CERCA_ELEMENTO_AUTHORITY_1003;
	}
    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().CERCA_ELEMENTO_AUTHORITY_1003;
    }


 }

