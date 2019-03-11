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
import it.finsiel.sbn.polo.oggetti.Soggetto;
import it.finsiel.sbn.polo.oggetti.estensione.SoggettoValida;
import it.finsiel.sbn.polo.orm.Tb_soggetto;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;

import java.lang.reflect.InvocationTargetException;


/**
 * Classe CancellaSoggetto
 * Factoring:
 * Cancella un soggetto come richiesto dal messaggio xml, dopo aver controllato la
 * validità. Cancella i legami soggetto-descrittore
 * Tabelle coinvolte:
 * - Tb_soggetto; tr_sog_des,tr_tit_sog
 * OggettiCoinvolti:
 *
 * Passi da eseguire:
 * Valida il soggetto (chiama metodo validaPerCancella in SoggettoValida'):
 * controllo di esistenza per identificativo,
 * controllo che non esistano legami con titoli (tr_tit_sog), altrimenti segnalo
 * diagnostico 'Esistono legami a titoli'
 * se non ok ritorna il msg response di diagnostica (analogo a creazione)
 * se ok chiama il metodo cancellaSoggetto della classe Soggetto: imposta
 * fl_canc='S',ts_var e ute_var su tb_soggetto; vengono cancellati i legami con
 * descrittori: tr_sog_des
 */
public class CancellaSoggetto extends CancellaFactoring {

	private String 		_idCancellazione;
	private Tb_soggetto	soggettoDaCancellare=null;

	public CancellaSoggetto(SBNMarc input_root_object){
		super(input_root_object);
        _idCancellazione = factoring_object.getIdCancella();
	}

	private void executeCancella() throws IllegalArgumentException,
			InvocationTargetException, Exception {
		SoggettoValida soggettoValida = new SoggettoValida();
		soggettoDaCancellare = soggettoValida.validaPerCancella(getCdUtente(), _idCancellazione);
		Soggetto soggetto = new Soggetto();
		soggetto.cancellaSoggetto(soggettoDaCancellare, getCdUtente());
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

    public String getIdAttivitaSt() {
        return CodiciAttivita.getIstance().CANCELLA_SOGGETTO_1279;
    }

}
