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
import it.iccu.sbn.ejb.model.unimarcmodel.EsportaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;

import org.apache.log4j.Category;


/**
 * Classe EsportaFactoring
 * <p>
 * Classe "Dispatcher" - attiva i Factoring di tipo "Esporta"
 * Ogni Factoring richiama(esegue) a sua volta le azioni specifiche
 * </p>
 *
 * Riceve in input la richiesta di esportazione.
 * Valida la richiesta secondo l'utente, tramite lo scheduler:
 * . abilitazione all'attività (da verificare la necessità di parcellizzare le
 * attività sul tipo elemento di authority, e solo sui documenti localizzati)
 *
 * Al termine dell'esecuzione dell'elaborazione batch deve essere inviato un mail
 * con il numero di record
 * estratti e il nome del file prodotto ( o i nomi se si è prodotto a blocchi)
 *
 * L'elaborazione batch attiva EsportaDocumenti o EsportaElementiDiAuthority
 * secondo la richiesta ricevuta.
 */
public class EsportaFactoring extends Factoring
{

    //attributi
    protected EsportaType factoring_object = null ;

    static Category log = Category.getInstance("iccu.serversbnmarc.EsportaFactoring");

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
    public EsportaFactoring(SBNMarc input_root_object)
    {
        // Assegno radice e classi XML principali
        super (input_root_object) ;


        // Assegno classi specifiche per questo factoring
        factoring_object = request_object.getEsporta() ;
    }

    public EsportaFactoring(){
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
		EsportaType factoring_object = sbnmarcObj.getSbnMessage().getSbnRequest().getEsporta();
        log.debug("STO PER VERIFICARE COSA LANCIARE");
        // Creo il secondo livello di factoring (valutando input)
        if (factoring_object.getEsportaTypeChoice().getNomeFileBis() != null) {
             //current_factoring = new EsportaDaBisFactoring(sbnmarcObj);
        	throw new EccezioneFactoring(501);
        } else if (factoring_object.getEsportaTypeChoice().getEstraeDocumento() != null){
			 //current_factoring = new EsportaDocumentiFactoring(sbnmarcObj);
        	throw new EccezioneFactoring(501);
		 } else if (factoring_object.getEsportaTypeChoice().getEstraeElementoAut()!=null){
			 //current_factoring = new EsportaElementiAutFactoring(sbnmarcObj);
			 throw new EccezioneFactoring(501);
		 }else
            throw new EccezioneFactoring(100);

        //log.info("HO CREATO IL CURRENT FACTORING:"+current_factoring+":");
        //return current_factoring;
    }

    public void proseguiTransazione() {
    }

}
