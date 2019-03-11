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

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoTitolo;
import it.finsiel.sbn.polo.factoring.util.Formattazione;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCerca;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloUniformeCerca;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A015;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe CercaTitoloFactoring
 * <p>
 * Classe per la gestoine del servizio di ricerca dei titoli.
 * Contiene le funzionalità di ricerca per documenti e titoli.
 * Restituisce la lista sintetica o analitica.
 * la preparazione dell'output avviene principalmente sulla tabella tb_titolo
 * L'output può essere composto da documenti, titoli di accesso o da elementiAut.
 * La distinzione avviene secondo la natura del titolo trovato: A è elemento di
 * authority, B,P,T,D è titolo di accesso, C,M,W,S,N è documento.
 *
 * I tioli si specializzano in: documenti, titoli di accsso, titoli uniformi, titoli uniformi musica.
 *
 * I documenti si specializzano per tipo materiale: moderno, musica, grafico, cartografico.
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 16-mar-02
 */
public class CercaTitoloUniforme extends CercaElementoAutFactoring {

    //file di log
    static Category log = Category.getInstance("iccu.sbnmarcserver.CercaTitoloFactoring");

    private boolean ricercaUnivoca = false;

    // Attributi da XML-input
    private CercaElementoAutType cercaTitolo = null;
    private CercaDatiAutType cercaDatiTit = null;
    private StringaCercaType stringaCerca = null;
    private A015 isadn = null;

    //Dati di ricerca
    //Identificativo: T001
    private String t001 = null;
    //entità collegata: ArrivoLegame
    private ArrivoLegame arrivoLegame = null;


    public CercaTitoloUniforme(SBNMarc input_root_object) {
        super(input_root_object);
        cercaTitolo = factoring_object.getCercaElementoAut();
        cercaDatiTit = cercaTitolo.getCercaDatiAut();
        arrivoLegame = cercaTitolo.getArrivoLegame();
        if (cercaDatiTit != null && cercaDatiTit.getCanaliCercaDatiAut() != null) {
            t001 = cercaDatiTit.getCanaliCercaDatiAut().getT001();
            stringaCerca = cercaDatiTit.getCanaliCercaDatiAut().getStringaCerca();
            isadn = cercaDatiTit.getCanaliCercaDatiAut().getT015();
        }
    }

    /**
    * Esamina i parametri di ricerca ricevuti via xml e attiva il metodo di ricerca
    * opportuno.
    * gestisce il tipo di risposta richiesto (lista o esame) e produce il
    * file xml di output richiesto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
    */
    protected void executeCerca() throws IllegalArgumentException, InvocationTargetException, Exception {
        boolean ricercaPerLastra = false;
        boolean ricercaPerEditor = false;
        boolean localizzazione = false;
        int counter = 0;

        if (t001 != null)
            counter++;
        if (stringaCerca != null) {
            if (arrivoLegame == null)
                counter++;
        }
        if (isadn != null)
            counter++;
        if (arrivoLegame != null)
            counter++;

        if (counter != 1) {
            //segnala errore
            throw new EccezioneSbnDiagnostico(3039);
        }
        if (t001 != null)
            cercaPerId();
        else if (isadn != null)
            cercaPerIsadn();
        else if (arrivoLegame != null) {
            if (arrivoLegame.getLegameDoc() != null)
                cercaPerLegameDoc(arrivoLegame.getLegameDoc());
            else if (arrivoLegame.getLegameElementoAut() != null) {
                cercaPerLegameAuthority();
            }
        } else if (stringaCerca != null)
            cercaPerStringaTitolo();
    }

    /** Anche le ricerche su musica, grafica e cartografia
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaPerLegameAuthority() throws IllegalArgumentException, InvocationTargetException, Exception {
        SbnAuthority authority = arrivoLegame.getLegameElementoAut().getTipoAuthority();
        String idArrivo = arrivoLegame.getLegameElementoAut().getIdArrivo();
        if (authority.getType() == SbnAuthority.AU_TYPE) {
            TitoloUniformeCerca titCerca = new TitoloUniformeCerca();
            tavola_response = titCerca.cercaTitoloPerAutore(idArrivo, tipoOrd, cercaTitolo, this);
        } else {
            throw new EccezioneSbnDiagnostico(1111, "Solo la ricerca per autore è consentita");
        }
    }

    /** Cerca per legame documento
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaPerLegameDoc(LegameDocType legame) throws IllegalArgumentException, InvocationTargetException, Exception {
        String idArrivo = idArrivo = legame.getIdArrivo();
        TitoloUniformeCerca titCerca = new TitoloUniformeCerca();
        tavola_response = titCerca.cercaTitoloPerDocumento(idArrivo, tipoOrd, cercaTitolo, this);
    }

    /** Ricerca per chiave identificatrice
     * @throws InfrastructureException */
    private void cercaPerId() throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        TitoloCerca titolo = new TitoloCerca();
        String id = Formattazione.formatta(t001);
        ricercaUnivoca = true;
        tavola_response = titolo.cercaTitoloPerID(id);
    }

    /** Ricerca in base ad un numero Standard.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaPerIsadn() throws IllegalArgumentException, InvocationTargetException, Exception {
        String num = isadn.getA_015();

        TitoloUniformeCerca titCerca = new TitoloUniformeCerca();
        tavola_response = titCerca.cercaTitoloPerIsadn(num, tipoOrd, cercaTitolo, this);
    }

    /** Esegue una ricerca utilizzando il campo titoloCerca;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaPerStringaTitolo() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null)
            cercaPerStringaEsatta();
        else if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null)
            cercaPerStringaLike();
        else
            throw new EccezioneSbnDiagnostico(3040);

    }

    /** Esegue una ricerca utilizzando il campo stringa esatta, solo senza filtri sui legami;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaPerStringaEsatta() throws IllegalArgumentException, InvocationTargetException, Exception {
        //Formattazione.formatta(stringaEsatta);
        TitoloUniformeCerca titCerca = new TitoloUniformeCerca();
        tavola_response = titCerca.cercaTitoloPerStringaEsatta(tipoOrd, cercaTitolo, this);
    }

    /** Esegue una ricerca utilizzando il campo stringalike;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaPerStringaLike() throws IllegalArgumentException, InvocationTargetException, Exception {
        //Formattazione.formatta(stringaLike);
        TitoloUniformeCerca titCerca = new TitoloUniformeCerca();
        tavola_response = titCerca.cercaTitoloPerStringaLike(tipoOrd, cercaTitolo, this);
    }

    /**
     * Metodo principale invocato dall'esterno per dare avvio all'esecuzione
     * /
    public String eseguiTransazione() throws EccezioneFactoring {
        try {
            verificaAbilitazioni();
            if (factoring_object.getIdLista() != null) {
                return eseguiRecupero();
            } else {
                executeCerca();
                if (numeroRecord > maxRighe) {
                    Progressivi progr = new Progressivi();
                    idLista = progr.getIdLista();
                }
                object_response = marshal (preparaOutput());
                if (numeroRecord > maxRighe) {
                    String nomeBlocco = idLista + "_" + 1;
                    log.info("Scrivo in LDAP con nome file:" + nomeBlocco);
                    IndiceLDAPObjectLocator.bind(nomeBlocco, object_response);
                }
            }
        } catch (EccezioneIccu ecc) {
            log.debug("Errore, eccezione:" + ecc);
            if (tavola_response != null) {
                try {

                } catch (EccezioneDB ex) {
                    log.error("Errore chiusura statement",ex);
                }
                tavola_response = null;
            }
            object_response = new FormatoErrore().preparaMessaggioErrore(ecc, user_object, schemaVersion);
            throw new EccezioneFactoring(56, "Errore in fase di esecuzione", ecc);
        }
        return getResult();
    }

    public String getResult() {
        return object_response;
    }

    /** Prepara la stringa di output in formato xml */
    protected SBNMarc preparaOutput() throws EccezioneDB, EccezioneSbnDiagnostico {
        if (tavola_response == null) {
            log.error("Errore nel factoring ricorsivo: oggetto tavola_response nullo");
            throw new EccezioneSbnDiagnostico(201);
        }
        // Deve utilizzare il numero di richieste che servono
        //TableDao response = tavola_response.getElencoRisultati(maxRighe);
        List response = eliminaBidDuplicati();
        SBNMarc risultato = formattaOutput(response);
        rowCounter += response.size();
        return risultato;
    }

    public String getIdAttivita() {
        return CodiciAttivita.getIstance().CERCA_DOCUMENTO_1002;
    }

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().CERCA_TITOLI_UNIFORMI_1249;
    }

    /**
     * Prepara l'xml di risposta utilizzando il vettore TableDao_response
     * @return Stringa contenente l'xml
     */
    private SBNMarc formattaOutput(List TableDao_response)
        throws EccezioneDB,  EccezioneSbnDiagnostico {
        if (ricercaUnivoca && TableDao_response.size() > 0)
            numeroRecord = 1;
        return FormatoTitolo.formattaLista(
            TableDao_response,
            user_object,
            factoring_object.getTipoOutput(),
            factoring_object.getTipoOrd(),
            idLista,
            rowCounter,
            maxRighe,
            numeroRecord,
            schemaVersion,
            null,
            null,
            false);
    }

}
