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

import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneIccu;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoErrore;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.oggetti.Classe;
import it.finsiel.sbn.polo.oggetti.estensione.ClasseValida;
import it.finsiel.sbn.polo.orm.Tb_classe;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;

import java.lang.reflect.InvocationTargetException;


/**
 * Classe CancellaClasse
 * Factoring:
 * Cancella una classe come richiesto dal messaggio xml, dopo aver controllato la
 * validità.
 * Tabelle coinvolte:
 * - Tb_classe;  tr_tit_cla
 * OggettiCoinvolti:
 * - Classe
 * - TitoloClasse
 * Passi da eseguire:
 * Valida la classe (chiama metodo validaPerCancella in ClasseValida'): controllo
 * di esistenza per identificativo,
 * controllo che non esistano legami con titoli (tr_tit_cla), altrimenti segnalo
 * diagnostico 'Esistono legami a titoli'
 * se non ok ritorna il msg response di diagnostica (analogo a creazione)
 * se ok chiama il metodo cancellaClasse della classe Classe: imposta
 * fl_canc='S',ts_var e ute_var su tb_classe
 */
public class CancellaClasse extends CancellaFactoring{

	private String 		_idCancellazione;
	private Tb_classe		classeDaCancellare=null;
	private String 		_codiceUtente;
	private TimestampHash	_timeHash = new TimestampHash();

	public CancellaClasse(SBNMarc input_root_object){
		super(input_root_object);
        _idCancellazione = factoring_object.getIdCancella();
   }

	private void executeCancella() throws IllegalArgumentException, InvocationTargetException, Exception {
		ClasseValida classeValida = new ClasseValida();
		classeDaCancellare = classeValida.validaPerCancella(_idCancellazione,_timeHash);

        if (classeDaCancellare == null) {
            log.error("Errore nel factoring ricorsivo: oggetto tavola_response nullo");
            throw new EccezioneSbnDiagnostico(3001,"nessun elemento trovato");
        }

        //almaviva5_20100722 controllo sistemi abilitati in biblioteca
    	classeValida.controllaVettoreParametriSemantici(_idCancellazione, this.user_object);
        Classe classe = new Classe();
		classe.cancellaClasse(classeDaCancellare,_codiceUtente,_timeHash);
		object_response = formattaOutput();
    }


    /**
     * Metodo principale invocato dall'esterno per dare avvio all'esecuzione
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void eseguiTransazione() throws IllegalArgumentException, InvocationTargetException, Exception {
        try {
            verificaAbilitazioni();
			executeCancella();
        } catch (EccezioneIccu ecc) {
            log.debug("Errore, eccezione:" + ecc);
            object_response = FormatoErrore.buildMessaggioErrore(ecc, user_object);
            throw ecc;
        }
    }

    /**
     * Prepara l'xml di risposta
     * @return Stringa contenente l'xml
     */
    private SBNMarc formattaOutput() throws EccezioneDB, EccezioneFactoring, EccezioneSbnDiagnostico {
        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
        result.setEsito("0000"); //Esito positivo
        result.setTestoEsito("OK");
        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(user_object);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);

        return sbnmarc;
    }

	public String getIdAttivita(){
		return CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028;
	}

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().CANCELLA_CLASSE_1280;
    }

}
