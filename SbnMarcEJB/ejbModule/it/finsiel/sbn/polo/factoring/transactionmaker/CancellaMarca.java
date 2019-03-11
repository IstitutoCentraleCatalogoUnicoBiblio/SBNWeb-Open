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
import it.finsiel.sbn.polo.oggetti.AllineamentoMarca;
import it.finsiel.sbn.polo.oggetti.Marca;
import it.finsiel.sbn.polo.oggetti.estensione.MarcaValida;
import it.finsiel.sbn.polo.orm.Tb_marca;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;

import java.lang.reflect.InvocationTargetException;


/**
 * Classe CancellaMarca
 * Factoring:
 * Cancella una marca come richiesto dal messaggio xml, dopo aver controllato la
 * validità. Cancella gli eventuali legami autore-marca e marca-repertorio
 * Tabelle coinvolte:
 * - Tb_marca; tr_aut_mar,tr_tit_mar,tr_rep_aut, tr_tit_mar, tr_mar_bib
 * OggettiCoinvolti:
 *
 * Passi da eseguire:
 * Valida la marca (chiama metodo validaPerCancella in MarcaValida'): controllo di
 * esistenza per identificativo,
 * controllo che non esistano legami con titoli (tr_tit_mar), altrimenti segnalo
 * diagnostico 'Esistono legami a titoli'
 * se non ok ritorna il msg response di diagnostica (analogo a creazione)
 * Controllo che non esistano localizzazioni diverse dal polo dell'utente che sta
 * cancellando (tr_mar_bib), altrimenti segnalo: marca localizzata in altri poli,
 * cancellazione impossibile
 * se ok chiama il metodo cancellaMarca della classe Marca: imposta
 * fl_canc='S',ts_var e ute_var su tb_marca; vengono cancellati i legami con
 * autori e repertori, vengono cancellate le parole della marca e i link a
 * immagini (ts_link_multim)
 * Aggiorna fl_allinea = 'S' per tutte le occorrenze di tr_mar_bib (chiama il
 * metodo 'aggiornaFlAllinea' di MarcaBiblioteca.
 */
public class CancellaMarca extends CancellaFactoring {

	private String 		_idCancellazione;
	private Tb_marca 		marcaDaCancellare;
	private String 		_codiceUtente;
	private TimestampHash	_timeHash = new TimestampHash();


	public CancellaMarca(SBNMarc input_root_object){
		super(input_root_object);
        _idCancellazione = factoring_object.getIdCancella();
   }

    private void executeCancella() throws IllegalArgumentException, InvocationTargetException, Exception {
		MarcaValida marcaValida = new MarcaValida();
		String user = getCdUtente();
		marcaDaCancellare = marcaValida.validaPerCancella(_idCancellazione,user,_timeHash);
        Marca marca = new Marca();
		marca.cancellaMarca(marcaDaCancellare,_codiceUtente,_timeHash);
		//MarcaBiblioteca marcaBiblioteca = new MarcaBiblioteca();
		//marcaBiblioteca.aggiornaPerAllinea( _codiceUtente,
		//									_idCancellazione,
		//									_sbnUser.getBiblioteca().substring(0,3),
		//									_sbnUser.getBiblioteca());
        AllineamentoMarca flagAllineamento = new AllineamentoMarca(marcaDaCancellare);
        flagAllineamento.setTb_marca(true);
        // almaviva LA LOCALIZZAZIONE PER MARCA E' STATA ELIMINATA IN QUANTO UNA MARCA IN POLO E'
		// AUTOMATICAMENTE LOCALIZZATA QUINDI SIA IN FASE DI INSERIMENTO CHE MODIFICA NON EFFETTUO NESSUN
		// TIPO DI OPERAZIONE SULLA TR_MAR_BIB CHE NON HA PIU' MOTIVO DI ESISTERE
		// VERIFICARE ALTRE EVENTUALI CHIAMATE A QUESTA TABELLA
		// INDICAZIONI DI ROSSANA 03/04/2007
        //new MarcaAllineamento().aggiornaFlagAllineamento(flagAllineamento,getCdUtente());

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
        return CodiciAttivita.getIstance().CANCELLA_MARCA_1278;
    }

}
