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
import it.finsiel.sbn.polo.oggetti.AllineamentoAutore;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.AutoreAutore;
import it.finsiel.sbn.polo.oggetti.AutoreBiblioteca;
import it.finsiel.sbn.polo.oggetti.estensione.AutoreAllineamento;
import it.finsiel.sbn.polo.oggetti.estensione.AutoreValida;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;

import java.lang.reflect.InvocationTargetException;

/**
 * Classe CancellaAutore
 * Factoring:
 * Cancella un autore come richiesto dal messaggio xml, dopo aver controllato la
 * validità. Cancella gli eventuali legami autore-autore, autore-marca e
 * autore-repertorio
 * Tabelle coinvolte:
 * - Tb_autore; Tr_aut_aut,tr_aut_mar,tr_rep_aut, tr_tit_aut,tr_aut_bib
 * OggettiCoinvolti:
 *
 * Passi da eseguire:
 * Valida l'autore (chiama metodo validaPerCancella in AutoreValida'): controllo
 * di esistenza per identificativo,
 * se la forma è 'A' controllo che non esistano legami con titoli (tr_tit_aut),
 * altrimenti segnalo diagnostico 'Esistono legami a titoli'
 * Controllo che non esistano localizzazioni diverse dal polo dell'utente che sta
 * cancellando (tr_aut_bib), altrimenti segnalo: autore localizzato in altri poli,
 * cancellazione impossibile
 * se non ok ritorna il msg response di diagnostica (analogo a creazione)
 * se ok chiama il metodo cancellaAutore della classe Autore: imposta
 * fl_canc='S',ts_var e ute_var su tb_autore; vengono cancellati i legami con
 * marche e repertori, vengono cancellati anche gli eventuali legami
 * autore-autore, se l'autore è in forma accettata vengono cancellati anche gli
 * autori di forma 'R' collegati
 * Aggiorna fl_allinea = 'S' per tutte le occorrenze di tr_aut_bib (chiama il
 * metodo 'aggiornaFlAllinea' di AutoreBiblioteca.
 */
public class CancellaAutore extends CancellaFactoring {

    private String _idCancellazione;
    //	private TableDao 		tavola_response;
    private Tb_autore autoreCancellato;

    public CancellaAutore(SBNMarc input_root_object) {
        super(input_root_object);
        _idCancellazione = factoring_object.getIdCancella();
    }

    private void executeCancella() throws IllegalArgumentException, InvocationTargetException, Exception {
        AutoreValida autoreValida = new AutoreValida();
        String user = getCdUtente();
        autoreCancellato = autoreValida.validaPerCancella(user, _idCancellazione);
        if (autoreCancellato == null) {
            log.error("Errore nel factoring ricorsivo: oggetto tavola_response nullo");
            throw new EccezioneFactoring(201);
        }
        Autore autore = new Autore();
        autore.cancellaAutore(autoreCancellato, user);

        // MARCO RANIERI MODIFICA:
        // Prima di passare l'autore Cancellato alla funzione di allineamento
        // setto il flag cancellazione a S (cancellato)
        autoreCancellato.setFL_CANC("S") ;

        new AutoreAutore().cancellaLegami(_idCancellazione, user, autoreCancellato.getTP_FORMA_AUT());
        AllineamentoAutore flagAll = new AllineamentoAutore(autoreCancellato);
        flagAll.setTb_autore(true);
        AutoreAllineamento allineamento =new AutoreAllineamento();
        allineamento.aggiornaFlagAllineamento(flagAll, user);

        new AutoreBiblioteca().aggiornaFlagCancCancellazione(_idCancellazione,getCdUtente());

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
            //throw new EccezioneFactoring(56, "Errore in fase di esecuzione",ecc);
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
        return CodiciAttivita.getIstance().CANCELLA_AUTORE_1277;
    }

}
