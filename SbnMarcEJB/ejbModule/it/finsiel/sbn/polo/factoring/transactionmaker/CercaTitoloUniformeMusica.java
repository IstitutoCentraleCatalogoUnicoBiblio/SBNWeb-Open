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
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloUniformeMusicaCerca;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A015;
import it.iccu.sbn.ejb.model.unimarcmodel.A928;
import it.iccu.sbn.ejb.model.unimarcmodel.A929;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.CanaliCercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaTitoloType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaTitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaTypeChoice;
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
public class CercaTitoloUniformeMusica extends CercaElementoAutFactoring {

    //file di log
    static Category log = Category.getInstance("iccu.sbnmarcserver.CercaTitoloFactoring");

    private boolean ricercaUnivoca = false;
    private boolean composizione = false;

    // Attributi da XML-input
    private CercaElementoAutType cercaTitolo = null;
    private CercaDatiAutType cercaDatiTit = null;
    private CercaTitoloUniformeMusicaType cercaDatiMus = null;
    private StringaCercaType stringaCerca = null;
    private A015 isadn = null;

    //Dati di ricerca
    //Identificativo: T001
    private String t001 = null;
    //entità collegata: ArrivoLegame
    private ArrivoLegame arrivoLegame = null;

    public CercaTitoloUniformeMusica(SBNMarc input_root_object) {
        super(input_root_object);
        setTp_materiale("U");
        cercaTitolo = factoring_object.getCercaElementoAut();
        if (cercaTitolo == null) {
            //CASO ANOMALO: ho un cerca documento da convertire in cercaTitUniMusicale.
            cercaTitolo = converti();
        }
        cercaDatiTit = cercaTitolo.getCercaDatiAut();
        if (cercaDatiTit != null) {
            if (cercaDatiTit instanceof CercaTitoloUniformeMusicaType)
                cercaDatiMus = (CercaTitoloUniformeMusicaType) cercaDatiTit;
        }
        arrivoLegame = cercaTitolo.getArrivoLegame();
        if (cercaDatiTit != null && cercaDatiTit.getCanaliCercaDatiAut() != null) {
            t001 = cercaDatiTit.getCanaliCercaDatiAut().getT001();
            stringaCerca = cercaDatiTit.getCanaliCercaDatiAut().getStringaCerca();
            isadn = cercaDatiTit.getCanaliCercaDatiAut().getT015();
        }
    }

    /**
     * Converte da un cercaDocumentoType ad un cercaTitUniMusType. e setta i vari campi
     * @return
     */
    private CercaElementoAutType converti() {
        CercaElementoAutType cerca = new CercaElementoAutType();
        CercaTitoloType cercaTitolo = factoring_object.getCercaTitolo();
        cerca.setArrivoLegame(cercaTitolo.getArrivoLegame());
        CercaDatiTitType cercaDatit = cercaTitolo.getCercaDatiTit();
        if (cercaDatit != null) {
            CercaTitoloUniformeMusicaType cercad= new CercaTitoloUniformeMusicaType();
            cerca.setCercaDatiAut(cercad);
            CercaDatiTitTypeChoice choice = cercaDatit.getCercaDatiTitTypeChoice();
            if (choice != null) {
                CanaliCercaDatiAutType canali = new CanaliCercaDatiAutType();
                cercad.setCanaliCercaDatiAut(canali);
                canali.setT001(choice.getT001());
                //canali.setT015();
                if (choice.getTitoloCerca()!=null) {
                    canali.setStringaCerca(choice.getTitoloCerca().getStringaCerca());
                }
            }
            if (cercaDatit instanceof CercaDocMusicaType) {
                CercaDocMusicaType cercaMus = (CercaDocMusicaType)cercaDatit;
                cercad.setT928(cercaMus.getT928());
                cercad.setT929(cercaMus.getT929());
                cercad.setDataFine_A(cercaMus.getDataFine_A());
                cercad.setDataFine_Da(cercaMus.getDataFine_Da());
                cercad.setDataInizio_A(cercaMus.getDataInizio_A());
                cercad.setDataInizio_Da(cercaMus.getDataInizio_Da());
            }
        }

        return cerca;
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
        boolean ricercaPerComposizione = false;
        int counter = 0;
        A929 a929 = null;
        A928 a928 = null;
        if (t001 != null)
            counter++;
        if (stringaCerca != null) {
            if (arrivoLegame == null)
                counter++;
        }
        if (cercaDatiMus != null) {
            a929 = cercaDatiMus.getT929();
            a928 = cercaDatiMus.getT928();
        }
        if (a929 != null) {
            composizione = true;
            if (a929.getG_929() != null || a929.getH_929() != null || a929.getI_929() != null) {
                ricercaPerComposizione = true;
                counter++;
            }
        }
        if (a928 != null)
            composizione = true;
        // MODIFICA PER CONTROLLO DATE CHE SONO SPECIFICHE PER LA COMPOSIZIONE
        if ( (cercaDatiMus.getDataInizio_Da() != null ) ||
        		(cercaDatiMus.getDataInizio_A() != null ) ||
        		(cercaDatiMus.getDataFine_Da() != null ) ||
        		(cercaDatiMus.getDataFine_A() != null )
        ){
        		composizione = true;
        }



        if (arrivoLegame != null)
            counter++;

        if (counter != 1) {
            //segnala errore
            throw new EccezioneSbnDiagnostico(3039);
        }
        if (t001 != null)
            cercaPerId();
        else if (ricercaPerComposizione)
            cercaPerComposizione(a929);
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
            TitoloUniformeMusicaCerca titCerca = new TitoloUniformeMusicaCerca();
            tavola_response = titCerca.cercaTitoloPerAutore(composizione, idArrivo, tipoOrd, cercaTitolo, this);
        } else if (authority.getType() == SbnAuthority.UM_TYPE) {
            TitoloUniformeMusicaCerca titCerca = new TitoloUniformeMusicaCerca();
            tavola_response = titCerca.cercaTitoloPerDocumento(composizione, idArrivo, tipoOrd, cercaTitolo, this);
        } else if (authority.getType() == SbnAuthority.TU_TYPE) {
            TitoloUniformeMusicaCerca titCerca = new TitoloUniformeMusicaCerca();
            tavola_response = titCerca.cercaTitoloPerDocumento(composizione, idArrivo, tipoOrd, cercaTitolo, this);
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
        TitoloUniformeMusicaCerca titCerca = new TitoloUniformeMusicaCerca();
        tavola_response = titCerca.cercaTitoloPerDocumento(composizione, idArrivo, tipoOrd, cercaTitolo, this);
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
    private void cercaPerComposizione(A929 t929) throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloUniformeMusicaCerca titCerca = new TitoloUniformeMusicaCerca();
        tavola_response =
            titCerca.cercaPerComposizione(
            	dopoAsterisco(t929.getG_929()),
                t929.getH_929(),
                t929.getI_929(),
                tipoOrd,
                cercaTitolo,
                this);
    }

    /** Esegue una ricerca utilizzando il campo titoloCerca;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaPerStringaTitolo() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null) {
            StringaCercaTypeChoice st = new StringaCercaTypeChoice();
            st.setStringaEsatta(dopoAsterisco(stringaCerca.getStringaCercaTypeChoice().getStringaEsatta()));
            stringaCerca.setStringaCercaTypeChoice(st);
            //stringaCerca.setStringaEsatta(dopoAsterisco(stringaCerca.getStringaCercaTypeChoice().getStringaEsatta()));
            cercaPerStringaEsatta();
        } else if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null) {
            StringaCercaTypeChoice st = new StringaCercaTypeChoice();
            st.setStringaLike(dopoAsterisco(stringaCerca.getStringaCercaTypeChoice().getStringaLike()));
            stringaCerca.setStringaCercaTypeChoice(st);
            //stringaCerca.setStringaLike(dopoAsterisco(stringaCerca.getStringaCercaTypeChoice().getStringaLike()));
            cercaPerStringaLike();
        }
        else
            throw new EccezioneSbnDiagnostico(3040);

    }

    /** Esegue una ricerca utilizzando il campo stringa esatta, solo senza filtri sui legami;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaPerStringaEsatta() throws IllegalArgumentException, InvocationTargetException, Exception {
        //Formattazione.formatta(stringaEsatta);
        TitoloUniformeMusicaCerca titCerca = new TitoloUniformeMusicaCerca();
        tavola_response = titCerca.cercaTitoloPerStringaEsatta(composizione, tipoOrd, cercaTitolo, this);
    }

    /** Esegue una ricerca utilizzando il campo stringalike;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaPerStringaLike() throws IllegalArgumentException, InvocationTargetException, Exception {
        //Formattazione.formatta(stringaLike);
        TitoloUniformeMusicaCerca titCerca = new TitoloUniformeMusicaCerca();
        tavola_response = titCerca.cercaTitoloPerStringaLike(composizione, tipoOrd, cercaTitolo, this);
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
        return CodiciAttivita.getIstance().CERCA_TITOLI_UNIFORMI_MUSICA_1250;
    }

    /**
     * Prepara l'xml di risposta utilizzando il vettore TableDao_response
     * @return Stringa contenente l'xml
     */
    private SBNMarc formattaOutput(List TableDao_response)
        throws EccezioneDB, EccezioneSbnDiagnostico {
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
            null,false);
    }

}
