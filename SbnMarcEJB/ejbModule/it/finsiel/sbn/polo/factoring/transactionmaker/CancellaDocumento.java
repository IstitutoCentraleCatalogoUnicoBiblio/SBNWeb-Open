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
import it.finsiel.sbn.polo.factoring.base.FormatoErrore;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.oggetti.AllineamentoTitolo;
import it.finsiel.sbn.polo.oggetti.TitoloBiblioteca;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloCancella;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloGestisceAllineamento;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloValidaCancella;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;

import java.lang.reflect.InvocationTargetException;

/**
 * Classe CancellaDocumento
 * Factoring:
 * Cancella un titolo come richiesto dal messaggio xml, dopo aver controllato la
 * validità. Cancella gli eventuali legami del titolo.
 * Tabelle coinvolte:
 *
 * OggettiCoinvolti:
 *
 * Passi da eseguire:
 * Valida il titolo (chiama metodo validaPerCancella in TitoloValida'): controllo
 * di esistenza per identificativo, cd_natura deve essere <> 'A'
 * controllo che non esistano legami con titoli verso il basso (tr_tit_tit con
 * bid=bid_coll), altrimenti segnalo diagnostico 'Esistono legami a titoli' (nb:
 * altri controlli da definire)
 * Controllo che non esistano localizzazioni diverse dal polo dell'utente che sta
 * cancellando (tr_tit_bib), altrimenti segnalo: titolo localizzato in altri poli,
 * cancellazione impossibile
 * se non ok ritorna il msg response di diagnostica (analogo a creazione)
 *
 * se ok imposta fl_canc='S',ts_var e ute_var su tb_titolo e sulle tabelle
 * collegate: tb_musica, tb_grafica, tb_cartografia, tb_impronta, tb_numero_std,
 * tb_composizione,
 * tutte le tr_tit_ ad esclusione di  tr_tit_tit per cui si cancellano solo i
 * legami verso l'alto: tr_tit_tit con bid=bid_base)
 *
 * Aggiorna fl_allinea = 'S' per tutte le occorrenze di tr_tit_bib (chiama il
 * metodo 'aggiornaFlAllinea' di TitoloBiblioteca).
 * @author
 * @author
 *
 * @version 5-mag-03
 */
public class CancellaDocumento extends CancellaFactoring {
    private Tb_titolo titoloCancellato;
    private String idCancellazione;
    private TimestampHash timeHash = new TimestampHash();

    public String getIdAttivita() {
        return CodiciAttivita.getIstance().CANCELLA_DOCUMENTO_1025 ;
    }

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().CANCELLA_DOCUMENTO_1025 ;
    }

    public CancellaDocumento(SBNMarc input_root_object) {
        super(input_root_object);
        idCancellazione = factoring_object.getIdCancella();
    }

    private void executeCancella() throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloValidaCancella titoloValida = new TitoloValidaCancella();
        titoloCancellato = titoloValida.validaPerCancella(idCancellazione, getCdUtente(), timeHash);
        if (titoloCancellato == null) {
            log.error("Errore nel factoring ricorsivo: oggetto tavola_response nullo");
            throw new EccezioneFactoring(201);
        }
        TitoloCancella titolo = new TitoloCancella();
        titoloCancellato.setUTE_VAR(getCdUtente());
        titolo.cancellaElementiLegati(titoloCancellato);
        //Lo tolgo perchè viene chiamato in titolo.cancellaElementiLegati
        //titolo.cancellaLegamiConTitoli(titoloCancellato);

        //titolo.cancellaTitolo(titoloCancellato);
        titoloCancellato.setBID_LINK(idCancellazione);
        titoloCancellato.setTP_LINK("F");
        titolo.cancellaPerFusione(titoloCancellato);
        //Aggiorno la versione, non c'è bisogno, è già cancellato
        //titolo.updateVersione(idCancellazione, getCdUtente());


        AllineamentoTitolo allineamentoTitolo = new AllineamentoTitolo(titoloCancellato);
        allineamentoTitolo.setTb_titolo(true);
        TitoloGestisceAllineamento titoloG = new TitoloGestisceAllineamento();
        titoloG.aggiornaFlagAllineamento(allineamentoTitolo, getCdUtente());

        //Cancello le localizzazioni del bid di partenza
        new TitoloBiblioteca().cancellaPerBid(idCancellazione, getCdUtente());
        //Potrei rifare la select per selezionare l'output, ma non avrebbe un gran senso.
        object_response = formattaOutput();
    }

    /**
     * metodo invocato quando si ricerca un messaggio xml di modifica
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
    private SBNMarc formattaOutput() throws EccezioneDB, EccezioneFactoring {
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

}
