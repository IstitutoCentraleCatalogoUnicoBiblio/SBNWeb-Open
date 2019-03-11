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
import it.finsiel.sbn.polo.oggetti.Luogo;
import it.finsiel.sbn.polo.oggetti.LuogoLuogo;
import it.finsiel.sbn.polo.oggetti.estensione.LuogoValida;
import it.finsiel.sbn.polo.orm.Tb_luogo;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;

import java.lang.reflect.InvocationTargetException;

/**
 * Classe CancellaLuogo
 * Factoring:
 * Cancella un luogo come richiesto dal messaggio xml, dopo aver controllato la
 * validità. Cancella gli eventuali legami luogo-luogo
 * Tabelle coinvolte:
 * - Tb_luogo; Tr_luo_luo; tr_tit_luo
 * OggettiCoinvolti:
 * - Luogo
 * - LuogoLuogo
 * - TitoloLuogo
 * Passi da eseguire:
 * Valida il luogo (chiama metodo validaPerCancella in LuogoValida'): controllo di
 * esistenza per identificativo,
 * se la forma è 'A' controllo che non esistano legami con titoli (tr_tit_luo),
 * altrimenti segnalo diagnostico 'Esistono legami a titoli'
 * se non ok ritorna il msg response di diagnostica (analogo a creazione)
 * se ok chiama il metodo cancellaLuogo della classe Luogo: imposta
 * fl_canc='S',ts_var e ute_var sul luogo; vengono cancellati anche tutti gli
 * eventuali legami luogo-luogo; se il luogo è in forma accettata vengono
 * cancellati anche i luoghi collegati con forma 'R'
 */
public class CancellaLuogo extends CancellaFactoring {

	private String 		_idCancellazione;
	private Tb_luogo		luogoDaCancellare;
	private TimestampHash	_timeHash = new TimestampHash();

	public CancellaLuogo(SBNMarc input_root_object) {
		super(input_root_object);
        _idCancellazione = factoring_object.getIdCancella();
	}

    private void executeCancella() throws IllegalArgumentException, InvocationTargetException, Exception {
		LuogoValida luogoValida = new LuogoValida();
		luogoDaCancellare = luogoValida.validaPerCancella(	_idCancellazione,
															getCdUtente(),
															_timeHash);
        if (luogoDaCancellare == null) {
            log.error("Errore nel factoring ricorsivo: oggetto tavola_response nullo");
            throw new EccezioneFactoring(201);
        }
		LuogoLuogo luogoLuo = new LuogoLuogo();
		luogoLuo.cancellaLegame(getCdUtente(),_idCancellazione,_timeHash);

        Luogo luogo = new Luogo();
		luogo.cancellaLuogo(luogoDaCancellare,getCdUtente(),_timeHash);
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

	public String getIdAttivita() {
		return CodiciAttivita.getIstance().CANCELLA_ELEMENTO_AUTHORITY_1028;
	}

    public String getIdAttivitaSt() {
        return CodiciAttivita.getIstance().CANCELLA_LUOGO_1281;
    }

}
