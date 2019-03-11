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

import it.finsiel.sbn.polo.exception.EccezioneAbilitazioni;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.AskDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.MServDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.RicTit4Abbonamento;
import it.iccu.sbn.ejb.model.unimarcmodel.RicTit4Inventario;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;

import org.apache.log4j.Logger;


/**
 * Classe FondeFactoring
 * <p>
 * Classe "Dispatcher" - attiva i Factoring di tipo "Fonde"
 * Ogni Factoring richiama(esegue) a sua volta le azioni specifiche
 * </p>
 * Attiva il factoring opportuno secondo la richiesta ricevuta: se c'è
 * tipoMateriale attiva FondeDocumento, altrimenti FondeElementoAutFactoring
 */
public class MServAskDocFactoring extends MServDocFactoring {

	private static Logger log = Logger.getLogger("iccu.serversbnmarc.MservAskDoc");

	RicTit4Inventario ricTit4Inventario;

	RicTit4Abbonamento ricTit4Abbonamento;

	private static String _xdoc;

	private RicTit4Inventario _xinv;

	private RicTit4Abbonamento _xabb;

	private static String _Kbibl;

	private static String _Kinv;

	private static String _Kordi;

	private static String _Kanno;
   /**
	 * Metodo costruttore classe di factoring
	 * <p>
	 * Riceve il root object da cui estrapolare le informazioni XML ricevute in
	 * input riempie gli oggetti (CASTOR) mappando i dati provenienti dall XML
	 * </p>
	 *
	 * @param SBNMarc
	 *            oggetto radice XML in input
	 * @return istanza oggetto (default)
	 */
    //costruttore
    public MServAskDocFactoring(SBNMarc input_root_object)
    {
        // Assegno radice e classi XML principali
        super (input_root_object) ;

        // Assegno classi specifiche per questo factoring
        factoring_object = servDoc;
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
    public static Factoring getFactoring(SBNMarc sbnmarcObj)
        throws EccezioneFactoring
    {
        Factoring current_factoring = null;
        log.debug("STO PER VERIFICARE COSA LANCIARE");
        // Creo il secondo livello di factoring (valutando input)
        AskDoc _askDoc;
        _askDoc = sbnmarcObj.getSbnMessage().getMServDoc().getAskdoc();
        if (_askDoc.getXdoc() != null)
            _xdoc = _askDoc.getXdoc();

        if (_askDoc.getXinv() != null){
        	if(_askDoc.getXinv().getKinv() != null)
        		_Kinv = _askDoc.getXinv().getKinv();
        	if(_askDoc.getXinv().getKbibl() != null)
        		_Kbibl = _askDoc.getXinv().getKbibl();
        }
        if(_askDoc.getXabb() != null){
        	if(_askDoc.getXabb().getKbibl() != null)
        		_Kbibl = _askDoc.getXabb().getKbibl();
        	if(_askDoc.getXabb().getKordi() != null)
        		_Kordi = _askDoc.getXabb().getKordi();
        	if(_askDoc.getXabb().getKanno() != null)
        		_Kanno = _askDoc.getXabb().getKanno();
        }



		MServDoc factoring_object =  sbnmarcObj.getSbnMessage().getMServDoc();
        if (factoring_object.getAskdoc() != null){
            current_factoring = MservAskDoc.getFactoring(sbnmarcObj);
        }else
            if (factoring_object.getAskinv() != null){
                current_factoring = MservAskInv.getFactoring(sbnmarcObj);
        }else
            throw new EccezioneFactoring(100);
        //log.info("HO CREATO IL CURRENT FACTORING:"+current_factoring+":");

        return current_factoring;
    }

    /**
     * Verifica le abilitazioni specifiche per la fusione. L'utente deve essere abilitato
     * ad una delle funzioni 1024, 1200, 1201, 1202.
     */
    public void verificaAbilitazioni() throws EccezioneAbilitazioni, EccezioneSbnDiagnostico {
        if (!validator.verificaAttivitaID(getCdUtente(),CodiciAttivita.getIstance().FONDE_DOCUMENTI_1024) && !!validator.verificaAttivitaID(getCdUtente(),CodiciAttivita.getIstance().FONDE_DOCUMENTI_DI_RAGGRUPPAMENTO_1200) && !validator.verificaAttivitaID(getCdUtente(),CodiciAttivita.getIstance().FONDE_DOCUMENTI_DI_RAGGRUPPAMENTO_CON_LINK_1201) && !!validator.verificaAttivitaID(getCdUtente(),CodiciAttivita.getIstance().FONDE_ELEMENTI_DI_AUTHORITY_CON_LINK_1202))
            throw new EccezioneAbilitazioni(4000, "Utente non autorizzato");
    }


}
