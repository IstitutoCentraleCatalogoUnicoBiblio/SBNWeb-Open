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
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.ChiaviAutore;
import it.finsiel.sbn.polo.factoring.base.FormatoAutore;
import it.finsiel.sbn.polo.factoring.util.Formattazione;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A015;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.CanaliCercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaAutoreType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.ChiaviAutoreCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRangeDate;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Classe CercaAutore<BR>
 * <p>
 * Contiene le funzionalità di ricerca per l'entità Autore.
 * restituisce la lista sintetica o analitica.
 * Possibili parametri di ricerca:
 * Identificativo: T001
 * nome autore per parte iniziale: nome
 * intervallo di data di aggiornamento: T005_Range
 * n. isadn : T015
 * parole del nome autore: paroleAut
 * entità collegata: ArrivoLegame
 * Filtri di ricerca:
 * forma del nome: formaNome
 * livello di autorità: tipoAuthority
 * Parametrizzazioni di output:
 * tipoOrd: ordinamento richiesto, può essere su identificativo (vid) o sul nome
 * (ky_cles1_a ky_cles2_a)
 * tipoOut: tipo di output richiesto: Esame, Lista
 * <BR>
 * <BR>
 * Tabelle coinvolte:<BR>
 * - Tb_autore; Tr_aut_aut;Tr_tit_aut,Tr_au_mar,Tr_rep_aut
 * <BR>
 * OggettiCoinvolti:<BR>
 * - Autore<BR>
 * - AutoreAutore<BR>
 * - TitoloAutore<BR>
 * . AutoreMarca<BR>
 * . RepertorioMarca
 * <BR>
 * Passi da eseguire:<BR>
 * esegue la ricerca secondo i parametri ricevuti;
 * prepara l'output secondo le indicazioni ricevute
 * se non ok ritorna il msg response di diagnostica
 * <BR>
 * </p>
 * @author
 * @version 7/11/2002
 */
public class CercaAutore extends CercaElementoAutFactoring {

    //file di log
    static Logger log = Logger.getLogger("iccu.sbnmarcserver.CercaAutore");

    //Se la ricerca può resituire un solo risultato.
    private boolean ricercaUnivoca = false;

    // Attributi da XML-input
    private CercaElementoAutType elementoAut;
    private CercaAutoreType cercaAutore;
    private CercaDatiAutType cercaElementoAut;
    //Dati di ricerca
    //Identificativo: T001
    private String T001 = null;
    //nome autore per parte iniziale
    private StringaCercaType stringaCerca;
    //chiavi di ricerca
    private ChiaviAutoreCercaType chiaviDiCerca;
    //intervallo di data di aggiornamento
    private SbnRangeDate T005_Range = null;
    //n. isadn
    private A015 T015 = null;
    //parole del nome autore: paroleAut
    private String[] paroleAut = null;
    //entità collegata: ArrivoLegame
    private ArrivoLegame arrivoLegame = null;
    //Filtri di ricerca:
    //forma del nome: formaNome
    private SbnFormaNome formaNome = null;
    //livello di autorità: statusAuthority
    private SbnAuthority statusAuthority = null;

    /**
     * Metodo costruttore classe di factoring
     * <p>
     * Riceve il root object da cui estrapolare le informazioni XML ricevute in input
     * riempie gli oggetti (CASTOR) mappando i dati provenienti dall XML
     * </p>
     * @param  SBNMarc oggetto radice XML in input
     * @return istanza oggetto (default)
     */
    public CercaAutore(SBNMarc input_root_object) {
        super(input_root_object);
        elementoAut = factoring_object.getCercaElementoAut();
        cercaElementoAut = elementoAut.getCercaDatiAut();
        if (cercaElementoAut instanceof CercaAutoreType)
            cercaAutore = (CercaAutoreType) cercaElementoAut;
        arrivoLegame = elementoAut.getArrivoLegame();
        CanaliCercaDatiAutType canaliRicerca = cercaElementoAut.getCanaliCercaDatiAut();
        if (canaliRicerca != null) {
            T001 = canaliRicerca.getT001();
            T015 = canaliRicerca.getT015();
            stringaCerca = canaliRicerca.getStringaCerca();
        }
        if (cercaAutore != null) {
            chiaviDiCerca = cercaAutore.getChiaviAutoreCerca();
            paroleAut = cercaAutore.getParoleAut();
            if (factoring_object.getTipoOrd().getType() != SbnTipoOrd.valueOf("1").getType()) {
                boolean personale = false;
                if (cercaAutore.getTipoNomeCount()>0) {
                    String tpAutore = cercaAutore.getTipoNome(0).toString();
                    if ((tpAutore.equals("E") || tpAutore.equals("R") || tpAutore.equals("G")) == false) {
                        personale = true;
                    }
                }
                if (personale) {
                    setOrder(""+factoring_object.getTipoOrd()+"P");
                }
            }
        }
        T005_Range = cercaElementoAut.getT005_Range();
        formaNome = cercaElementoAut.getFormaNome();
        //statusAuthority = datiElementoAut.getTipoAuthority();
        arrivoLegame = elementoAut.getArrivoLegame();
        //tipoSchedaDatiAut = Validator.get...
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


    	// Febbraio 2016: gestione ISNI (International standard number identifier)
    	// Attenzione vengono asteriscate le ricerche per T015 ISADN e non sostituite con quelle T010 per ISNI
    	// Quando saranno prese le decisioni in Indice su coome effettuare la ricerca si effettuerà la modifica definitiva
        int counter = 0;
        if (T001 != null)
            counter++;
//        if (T015 != null)
//            counter++;
        if (stringaCerca != null)
            counter++;
        if (paroleAut != null && paroleAut.length > 0)
            counter++;
        if (chiaviDiCerca != null)
            counter++;
        if (arrivoLegame != null)
            counter++;
        if (counter != 1) {
            //segnala errore
            throw new EccezioneSbnDiagnostico(3039);
        }
        if (T001 != null)
            cercaAutorePerID();
//        else if (T015 != null)
//            cercaAutorePerISADN();
        else if (stringaCerca != null)
            cercaAutorePerNome();
        else if (chiaviDiCerca != null)
            cercaAutorePerChiavi();
        else if (paroleAut != null && paroleAut.length > 0)
            cercaAutorePerParole();
        else if (arrivoLegame != null)
            cercaAutorePerLegame();
    }

    /** Invoca l'opportuno metodo di ricerca per legame valutando i campi settati dall'xml
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaAutorePerLegame() throws IllegalArgumentException, InvocationTargetException, Exception {
        ArrivoLegame legame = elementoAut.getArrivoLegame();
        if ((legame == null))
            return;
        if (legame.getLegameDoc() != null)
            cercaAutorePerTitolo();
// Inizio modifica per gestire i legami titoliAccesso Autori almaviva2 15.luglio 2009
        else if (legame.getLegameTitAccesso() != null)
                cercaAutorePerTitolo();
        else if (
            legame.getLegameElementoAut().getTipoAuthority().getType()
                == SbnAuthority.TU_TYPE
                || legame.getLegameElementoAut().getTipoAuthority().getType()
                    == SbnAuthority.UM_TYPE)
            cercaAutorePerTitolo();
        //if (legame[0].getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.UM_TYPE)
        //  cercaAutorePerTitoloMusicale();
        else if (
            legame.getLegameElementoAut().getTipoAuthority().getType()
                == SbnAuthority.MA_TYPE)
            cercaAutorePerMarca();
    }

    /**
     * Metodo per cercare l'autore con n. identificativo:
     * ricerca su Tb_autore con VID
     * Output possibili:
     * . autore inesistente
     * . autore trovato: tutti gli attributi
     * @throws InfrastructureException
     */
    private void cercaAutorePerID() throws EccezioneDB, InfrastructureException {
        Autore autoreDB = new Autore();
        tavola_response = autoreDB.cercaAutorePerID(T001);
        ricercaUnivoca = true;

    }

    /** Cerca autore per isadn
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
//    private void () throws IllegalArgumentException, InvocationTargetException, Exception {
//        Autore autoreDB = new Autore();
//        tavola_responcercaAutorePerISADNse = autoreDB.cercaAutorePerISADN(T015.getA_015());
//        //numeroRecord = 1;//Perchè questo ?
//        ricercaUnivoca = true;
//    }

    /**
     * Metodo per cercare l'autore con stringa nome.
     * ricerca su Tb_autore con ky_cles1_a ky_cles2_a
     * Input: nome, statusAuthority, formaNome
     * applica la routine di normalizzazione al nome
     * esegue la select * su Tb_autore
     * se i filtri sono valorizzati li applica alla select
     * Applica il tipo ordinamento richiesto in tipoOrd.
     * Output possibili:
     * . autore inesistente
     * . autori trovati: tutti gli attributi
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void cercaAutorePerNome() throws IllegalArgumentException, InvocationTargetException, Exception {
        Autore autoreDB = new Autore();
        //Da discriminare il tipo di ricerca LIKE o ESATTA
        ChiaviAutore chiaviAutore = new ChiaviAutore();
        String temp = null;
        if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null) {
            temp = stringaCerca.getStringaCercaTypeChoice().getStringaEsatta();
        } else if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null) {
            temp = stringaCerca.getStringaCercaTypeChoice().getStringaLike();
        }
        if (temp != null) {
            temp = ChiaviAutore.rimuoviApostrofi(temp);


            // Manutenzione Evolutiva 29.05.2018 almaviva2 - Eliminazione degli articoli nella scrittura della Cles
            // usando la stopList ElencoArticoli così da consentire interrogazione degli Enti con la presenza/assenza degli articoli
            // INIZIO
            // Luglio 2018 ulteriore modifica a Manutenzione Evolutiva 29.05.2018 almaviva2
            // La ricerca modificata deve essere applicata solo quando la ricerca autore è filtrata per Autore di tipo ente
            // quindi viene aggiunto controllo
            if (cercaAutore.getTipoNomeCount() > 0) {
            	 for (int i = 0; i < cercaAutore.getTipoNomeCount(); i++) {
            		 if (cercaAutore.getTipoNome(i).toString().equals("E") || cercaAutore.getTipoNome(i).toString().equals("R")) {
            			 temp = dopoAsterisco(temp);
            		 }
            	 }
            }
            // temp = dopoAsterisco(temp);
            // FINE


            // almaviva2 adeguamenti a protocollo di Indice fatta il 12.05.2015
            // almaviva MODIFICA PER SOLUZIONE BUG 0005858 22/04/2015
            temp = temp.replace('*', ' ');
            // END

            String nome1;
            String nome2 = null;
            String auteur = chiaviAutore.calcolaAuteur(Formattazione.formatta(temp));
            if (temp.indexOf('#')>=0) {
                nome1 = ChiaviAutore.eliminaDoppiSpazi(temp.replace('#', ' '));
                nome2 = ChiaviAutore.eliminaDoppiSpazi(ChiaviAutore.rimuoviCarattere(temp, "#"));
            }
            else if (temp.indexOf('-')>=0) {
                int n = temp.indexOf(' ');
                if (n >= 0)
                    nome1 = ChiaviAutore.rimuoviCarattere(temp.substring(0, n), "-") + temp.substring(n).replace('-', ' ');
                else
                    nome1 = ChiaviAutore.rimuoviCarattere(temp, "-");
                nome2 = ChiaviAutore.eliminaDoppiSpazi(temp.replace('-',' '));
            } else
                nome1 = temp;

            //[TODO : qui dovrebbe fare la stessa cosa di quando calcola la cles ]
            nome1 = Formattazione.formatta(nome1);
            nome2 = Formattazione.formatta(nome2);
            if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null) {
                controllaNumeroRecord(autoreDB.contaAutorePerNome(nome1,nome2, auteur, cercaAutore, cercaElementoAut));
                tavola_response = autoreDB.cercaAutorePerNome(nome1,nome2, auteur, cercaAutore, cercaElementoAut, tipoOrd);
            } else {
                autoreDB.setEsporta(esporta);
                controllaNumeroRecord(autoreDB.contaAutorePerNomeLike(nome1, nome2, auteur, cercaAutore, cercaElementoAut));
                tavola_response = autoreDB.cercaAutorePerNomeLike(nome1, nome2, auteur, cercaAutore, cercaElementoAut, tipoOrd);
            }
        }
    }

    private void cercaAutorePerChiavi() throws IllegalArgumentException, InvocationTargetException, Exception {
        Autore autoreDB = new Autore();
        //Da discriminare il tipo di ricerca AUTEUR o CAUTUN
        if (chiaviDiCerca.getAutoreAUTEUR() != null) {
            String nome = Formattazione.formatta(chiaviDiCerca.getAutoreAUTEUR());
            controllaNumeroRecord(autoreDB.contaAutorePerAuteur(nome, cercaAutore, cercaElementoAut));
            tavola_response = autoreDB.cercaAutorePerAuteur(nome, cercaAutore, cercaElementoAut, tipoOrd);
        } else if (chiaviDiCerca.getAutoreCAUT() != null) {
            String nome = Formattazione.formatta(chiaviDiCerca.getAutoreCAUT());
            controllaNumeroRecord(autoreDB.contaAutorePerCautun(nome, cercaAutore, cercaElementoAut));
            tavola_response = autoreDB.cercaAutorePerCautun(nome, cercaAutore, cercaElementoAut, tipoOrd);
        } else
            throw new EccezioneSbnDiagnostico(3074, "Ricerca non consentita");
    }

    /**
     * Metodo per cercare l'autore con parole della descrizione
     * NB: questa opzione dipende dall'uso o meno di text index sul db.
     * Lasciare in sospeso al momento.
     * ricerca su Tb_autore con ky_cles1_a ky_cles2_a
     * Input: paroleAut, statusAuthority, formaNome
     * esegue la select * su Tb_autore
     * se i filtri sono valorizzati li applica alla select
     * Applica il tipo ordinamento richiesto in tipoOrd.
     * Output possibili:
     * . autore inesistente
     * . autori trovati: tutti gli attributi
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void cercaAutorePerParole() throws IllegalArgumentException, InvocationTargetException, Exception {
        //String nome = formattazione(datiElementoAut.getCercaDatiAutTypeChoice().getNome());

        /*
        List v = new ArrayList();
        for (int i = 0; i < paroleAut.length; i++) {
            //_ e # devono diventare spazio
            paroleAut[i] = paroleAut[i].replace('_', ' ');
            paroleAut[i] = paroleAut[i].replace('#', ' ');
            paroleAut[i] = NormalizzaNomi.normalizzazioneGenerica(paroleAut[i]).trim();
            //if (paroleAut[i].length() > 0 && !ElencoParole.contiene(paroleAut[i]))
            if (paroleAut[i].length() > 0)
                v.add(paroleAut[i]);
        }
        paroleAut = new String[v.size()];
        for (int i = 0; i < v.size(); i++)
            paroleAut[i] = (String) v.get(i);
        */
    	//almaviva5_20100615 #3817
        String[] parolePerRicerca = normalizzaParolePerRicerca(paroleAut);
        Autore autore = new Autore();
        controllaNumeroRecord(autore.contaAutorePerParoleNome(parolePerRicerca, cercaAutore, cercaElementoAut));
        tavola_response = autore.cercaAutorePerParoleNome(parolePerRicerca, cercaAutore, cercaElementoAut, tipoOrd);
    }

    /**
     * Metodo per cercare gli autori collegati a un titolo
     * Input: idArrivo statusAuthority, formaNome
     * ricerca su Tb_autore dove TR_tit_aut.VID = Tb_autore.VID e
     * TR_tit_aut.BID = idArrivo
     * se i filtri sono valorizzati li applica alla select
     * Applica il tipo ordinamento richiesto in tipoOrd.
     * Output possibili:
     * . autore inesistente
     * . autori trovati: tutti gli attributi
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void cercaAutorePerTitolo() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (arrivoLegame == null) {
            log.error("Nessun titolo specificato nella ricerca");
            throw new EccezioneSbnDiagnostico(3041);
        }
        //SbnLivello livelloAuthority =
        // arrivoLegame.getLegameElementoAut().getElementoAutLegato().getDatiElementoAut().getLivelloAut();
        String idArrivo = null;
        if (arrivoLegame.getLegameElementoAut() != null)
            idArrivo = arrivoLegame.getLegameElementoAut().getIdArrivo();
        else if (arrivoLegame.getLegameDoc() != null)
            idArrivo = arrivoLegame.getLegameDoc().getIdArrivo();
//      Inizio modifica per gestire i legami titoliAccesso Autori almaviva2 15.luglio 2009
        else if (arrivoLegame.getLegameTitAccesso() != null)
            idArrivo = arrivoLegame.getLegameTitAccesso().getIdArrivo();
        //SbnFormaNome formaNome = cercaAutore.getFormaNome();
        Autore autoreDB = new Autore();
        controllaNumeroRecord(autoreDB.contaAutorePerTitolo(idArrivo, cercaAutore, cercaElementoAut));
        tavola_response = autoreDB.cercaAutorePerTitolo(idArrivo, cercaAutore, cercaElementoAut, tipoOrd);
    }

    /**
     * Metodo per cercare gli autori collegati a una marca
     * Input: idArrivo statusAuthority, formaNome
     * ricerca su Tb_autore dove TR_aut_mar.VID = Tb_autore.VID e
     * TR_aut_mar.MID = idArrivo
     * se i filtri sono valorizzati li applica alla select
     * Applica il tipo ordinamento richiesto in tipoOrd.
     * Output possibili:
     * . autore inesistente
     * . autori trovati: tutti gli attributi
     * Esegue la preparazione dell'output richiesto in tipoOutput: esame o lista.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void cercaAutorePerMarca() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (arrivoLegame == null) {
            log.error("Nessun titolo specificato nella ricerca");
            throw new EccezioneSbnDiagnostico(3041);
        }
        String idArrivo = arrivoLegame.getLegameElementoAut().getIdArrivo();
        Autore autoreDB = new Autore();
        //TableDao_response = autoreDB.cercaAutorePerMarca(tipoAuthority, idArrivo, formaNome, tipoOrd);
        controllaNumeroRecord(autoreDB.contaAutorePerMarca(idArrivo, cercaAutore, cercaElementoAut));
        tavola_response = autoreDB.cercaAutorePerMarca(idArrivo, cercaAutore, cercaElementoAut, tipoOrd);
    }

    /**
     * Metodo per leggere i rinvii di un autore
     * Input: VID
     * 1) ricerca su Tb_autore dove TR_aut_aut.vid_base =  VID in input e
     * Tb_autore.VID = TR_aut_aut.vid_coll
     * 2) ricerca su Tb_autore dove TR_aut_aut.vid_coll =  vid in input e
     * Tb_autore.vid = TR_aut_aut.vid_base
     * Output possibili:
     * . autore inesistente
     * . autori trovati: tutti gli attributi
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private List cercaRinviiAutore(String vid) throws IllegalArgumentException, InvocationTargetException, Exception {
        Autore autoreDB = new Autore();
        return autoreDB.cercaRinviiAutore(vid);
    }

    /**
     * Metodo per cercare gli autori con intervallo di date di variazione
     * Input: T005_Range, statusAuthority, formaNome
     * ricerca su Tb_autore per intervallo su ts_var
     * se i filtri sono valorizzati li applica alla select
     * Applica il tipo ordinamento richiesto in tipoOrd.
     * Output possibili:
     * . autore inesistente
     * . autori trovati: tutti gli attributi
     * Esegue la preparazione dell'output richiesto in tipoOutput: esame o lista.
     * /
    Rimosso perchè non esiste
    private void cercaAutorePerDatavar() throws EccezioneDB, EccezioneSbnDiagnostico {
    	SbnDatavar datada = new SbnDatavar(T005_Range.getDataDa().toDate().getTime());
    	SbnDatavar dataa = new SbnDatavar(T005_Range.getDataA().toDate().getTime());
    	Autore autore = new Autore();
    	controllaNumeroRecord(autore.contaAutorePerDatavar(datada,dataa,cercaAutore));
    	tavola_response = autore.cercaAutorePerDatavar(datada, dataa, cercaAutore, tipoOrd);
    }*/

    /**
     * Prepara l'xml di risposta utilizzando il vettore TableDao_response
     * @return Stringa contenente l'xml
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private SBNMarc formattaOutput(List TableDao_response)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        FormatoAutore forAut = new FormatoAutore(tipoOut, tipoOrd, user_object);
        if (ricercaUnivoca) {
            if (TableDao_response.size() > 0)
                numeroRecord = 1;
            else
                numeroRecord = 0;
        }
        return forAut.formattaListaE(
            TableDao_response,
            user_object,
            factoring_object.getTipoOutput(),
            factoring_object.getTipoOrd(),
            idLista,
            rowCounter,
            maxRighe,
            numeroRecord,
            schemaVersion);
    }

    /** Prepara la stringa di output in formato xml
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected SBNMarc preparaOutput() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (tavola_response == null) {
            log.error("Errore nel factoring ricorsivo: oggetto tavola_response nullo");
            throw new EccezioneFactoring(201);
        }
        // Deve utilizzare il numero di richieste che servono
        List response;
        response = tavola_response.getElencoRisultati(maxRighe);
        SBNMarc risultato = formattaOutput(response);
        rowCounter += response.size();
        return risultato;
    }

    public String getIdAttivita() {
        return CodiciAttivita.getIstance().CERCA_ELEMENTO_AUTHORITY_1003;
    }

    public String getIdAttivitaSt() {
        return CodiciAttivita.getIstance().CERCA_AUTORI_1251;
    }

}
