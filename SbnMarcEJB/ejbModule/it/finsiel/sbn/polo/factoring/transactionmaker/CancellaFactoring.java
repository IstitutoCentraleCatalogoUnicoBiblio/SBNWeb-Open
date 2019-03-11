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
import it.iccu.sbn.ejb.model.unimarcmodel.CancellaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;

import org.apache.log4j.Category;


/**
 * Classe CancellaFactoring
 * <p>
 * Classe "Dispatcher" - attiva i Factoring di tipo "Cancella"
 * Ogni Factoring richiama(esegue) a sua volta le azioni specifiche
 * </p>
 */
public abstract class CancellaFactoring extends Factoring {


    //attributi
    protected CancellaType factoring_object = null ;

    static Category log = Category.getInstance("iccu.serversbnmarc.CancellaFactoring");

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
    public CancellaFactoring(SBNMarc input_root_object)
    {
        // Assegno radice e classi XML principali
        super (input_root_object) ;

        // Assegno classi specifiche per questo factoring
        factoring_object = request_object.getCancella() ;
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
        // Creo il secondo livello di factoring (valutando input)
        if (factoring_object.getTipoOggetto().getTipoMateriale()!= null)
            current_factoring = new CancellaDocumento(sbnmarcObj);
        else if (factoring_object.getTipoOggetto().getTipoAuthority() != null){
            current_factoring = CancellaElementoAutFactoring.getFactoring(sbnmarcObj);
        }else
            throw new EccezioneFactoring(100);
        //log.info("HO CREATO IL CURRENT FACTORING:"+current_factoring+":");


        // Mi faccio tornare l'ennesimo livello, quello che esegue l'azione
        return current_factoring;

    }

}
