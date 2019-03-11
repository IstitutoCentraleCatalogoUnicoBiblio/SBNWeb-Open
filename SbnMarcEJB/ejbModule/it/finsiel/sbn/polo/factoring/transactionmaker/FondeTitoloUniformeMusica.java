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
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.estensione.TitoloFonde;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;

import java.lang.reflect.InvocationTargetException;

/**
 * Classe FondeTitoloUniformeMusica
 *
 * Gestisce la fusione o lo spostamento legame tra due titoli uniformi musicale
 *
 * Riceve in input la richiesta di fusione
 *
 * Valida la richiesta:
 * . i due titoli idPartenza e idArrivo devono esistere in tb_titolo,  devono
 * avere natura A e tipo materiale='U', altrimenti si segnala diagnostico ' i bid
 * indicati non si riferiescono a titoli uniformi musicali'.
 *
 * Se si tratta di spostamento legami (presenza di spostaId):
 * . verifica che il numero di elementi non sia maggiore di un valore massimo
 * gestito in un property di sistema, altrimenti restituisce un diagnostico:
 * 'troppi identificativi comunicati: spezzare la richiesta in parti'
 * . per ogni spostaId: verifica esistenza tr_tit_tit con bid_base=spostaId e
 * bid_coll=idPartenza, se non esiste interrompe il servizio e segnala diagnostico
 * 'legame inesistente' comunicando spostaId nel testo diagnostico;
 * verifica esistenza tr_tit_tit con bid_base=spostaId e bid_arrivo=idArrivo, se
 * esiste interrompe il servizio e segnala diagnostico 'legame già esistente'
 * comunicando spostaId nel testo diagnostico.
 * Se il controllo è ok: modifica tr_tit_tit con bid_coll= idArrivo, modifica
 * tb_titolo.ts_var e ute_var con bid=spostaId, aggiorna fl_allinea = 'S' per
 * tutte le occorrenze di tr_tit_bib con bid=spostaID (chiama il metodo
 * 'aggiornaFlAllinea' di TitoloBiblioteca).
 *
 * Se spostaId non c'è si tratta di fusione: si leggono i legami tr_tit_tit con
 * bid_coll=idPartenza, si  verifica che il numero di legami non sia maggiore di
 * un valore massimo gestito in un property di sistema, altrimenti restituisce un
 * diagnostico: 'troppi legami: spezzare la richiesta in parti'
 * pe ogni legame tr_tit_tit: verifica esistenza tr_tit_tit con bid_base=bid e
 * bid_coll=idArrivo, se esiste interrompe il servizio e segnala diagnostico
 * 'legame già esistente' comunicando bid nel testo diagnostico.
 * Se il controllo è ok: si eseguono le stesse operazioni descritte sopra.
 *
 * Quando idPartenza non ha più legami tr_tit_tit  si spostano i legami tr_tit_aut
 * e tit_tit con bid_base=idPartenza da idPartenza a idArrivo (update sui legami),
 * si spostano tr_rep_tit.
 * Per tutti i legame si controlla che non esista già la stessa relazione con
 * idArrivo, nel qual caso non si esegue nessuna operazione.
 * Al termine dell'elaborazione sui legami si cancella il titolo idPartenza e i
 * suoi legami ad eccezione dei legami tr_tit_bib, in cui si aggiorna fl_allinea
 * (chiama il metodo 'aggiornaFlAllinea' di TitoloBiblioteca con bid=idPartenza).
 *
 * @author
 * @author
 *
 * @version 06-mag-03
 */
public class FondeTitoloUniformeMusica extends FondeElementoAutFoctoring {
    public FondeTitoloUniformeMusica(SBNMarc input_root_object) {
        super(input_root_object);
        setTp_materiale("U");
    }
    TimestampHash timeHash = new TimestampHash();

    Tb_titolo titoloModificato = null;

    public String getIdAttivita() {
        return CodiciAttivita.getIstance().FONDE_ELEMENTI_DI_AUTHORITY_1027;
    }

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().FONDE_TITOLO_UNIFORME_MUSICA_1269;
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
            executeFonde();
            object_response = formattaOutput();
        } catch (EccezioneIccu ecc) {
            log.debug("Errore, eccezione:" + ecc);
            object_response = FormatoErrore.buildMessaggioErrore(ecc, root_object.getSbnUser());
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
        DatiDocType datiResp = new DatiDocType();
        datiResp.setT001(titoloModificato.getBID());
        SbnDatavar data = new SbnDatavar(titoloModificato.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAutDoc(SbnLivello.valueOf(Decodificatore.livelloSoglia(titoloModificato.getCD_LIVELLO())));
        DocumentoType elementoAutResp = new DocumentoType();
        DocumentoTypeChoice docChoice = new DocumentoTypeChoice();
        docChoice.setDatiDocumento(datiResp);
        elementoAutResp.setDocumentoTypeChoice(docChoice);
        output.addDocumento(elementoAutResp);
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTotRighe(1);

        return sbnmarc;
    }

    private void executeFonde() throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloFonde titFonde = new TitoloFonde(factoring_object);
        titoloModificato = titFonde.eseguiFusione(getCdUtente(), timeHash);

    }
}
