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

import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_autoreResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_aut_autResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.exception.EccezioneSbnMessage;
import it.finsiel.sbn.polo.factoring.base.FormatoAutore;
import it.finsiel.sbn.polo.factoring.base.TipiAutore;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.AllineamentoAutore;
import it.finsiel.sbn.polo.oggetti.AllineamentoMarca;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.AutoreAutore;
import it.finsiel.sbn.polo.oggetti.AutoreMarca;
import it.finsiel.sbn.polo.oggetti.Marca;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.oggetti.RepertorioAutore;
import it.finsiel.sbn.polo.oggetti.estensione.AutoreAllineamento;
import it.finsiel.sbn.polo.oggetti.estensione.AutoreValida;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_marca;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tr_aut_aut;
import it.finsiel.sbn.polo.orm.Tr_aut_mar;
import it.finsiel.sbn.polo.orm.Tr_rep_aut;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A015;
import it.iccu.sbn.ejb.model.unimarcmodel.AutorePersonaleType;
import it.iccu.sbn.ejb.model.unimarcmodel.C899;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.EnteType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.LocalizzaInfoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LocalizzaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAzioneLocalizza;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe CreaAutore<BR>
 * <p>
 * Factoring:<BR>
 * Crea un autore come richiesto dal messaggio xml, dopo aver controllato la
 * validità. Crea gli eventuali legami autore-autore
 * <BR>
 * <BR>
 * Tabelle coinvolte:<BR>
 * - Tb_autore; Tr_aut_aut<BR>
 * Vista: Vl_autore_aut<BR>
 * <BR>
 * OggettiCoinvolti:<BR>
 * - Autore<BR>
 * - AutoreAutore <BR>
 * <BR>
 * Passi da eseguire:<BR>
 * Valida l'autore.
 * se non ok ritorna il msg response di diagnostica
 * se ok e se esistono informazioni di ArrivoLegame:valida legami autore-autore; se
 * non ok  ritorna il msg response di diagnostica
 * Se ok inserisce l'autore nel database; inserisce i legami nel database; prepara
 * il msg di response di output
 * <BR>
 * <BR>
 * Nota sull'ID:<br>
 * se T001 contiene la stringa '0000000000' (concordata con Finsiel come
 * default) si richiede l'assegnazione di un vid di sistema. In questo caso la
 * Creazione autore: non deve effettuare il controllo di esistenza per id;
 * prima di inserire il record in tb_autore occorre leggere ts_progressivo_id
 * con pk = 'V', sommare 1 a progr_ultimo e fare l'update del record, chiudere
 * la transazione per liberare il lock sul record ts_progressivi_id; costruire
 * il VID con sigla_indice 'V' progr_ultimo e fare l'insert. Il vid assegnato
 * deve essere ritornato al client nel T001 di output.
 * sigla_indice è un codice di default del sistema, per adesso usa SBN. Penso
 * che sia meglio metterlo in una property del sistema, e gestirlo nell'albero
 * jndi (??).
 * </p>
 * @author
 * @author
 * @version 07/11/2002
 */
public class CreaAutore extends CreaElementoAutFactoring {

    //file di log
    static Category log = Category.getInstance("iccu.sbnmarcserver.CreaAutore");

    // Attributi da XML-input
    private ElementAutType elementoAut;
    private DatiElementoType datiElementoAut;

    private SbnLivello livelloAut;

    //entità collegata: ArrivoLegame
    private LegamiType[] arrivoLegame = null;

    private SbnSimile tipoControllo = null;
    //Utente che esegue la richiesta
    private SbnUserType sbnUser = null;

    //Autore creato dal factoring
    Tb_autore autoreCreato;

    private final static int maxRighe =
        Integer.parseInt(ResourceLoader.getPropertyString("RIGHE_PER_BLOCCO_SBNMARC"));

    private LocalizzaType localizzaType;
    private String  _condiviso;

    //vettore utilizzato per i risultati delle ricerche
    /**
     * Metodo costruttore classe di factoring
     * <p>
     * Riceve il root object da cui estrapolare le informazioni XML ricevute in input
     * riempie gli oggetti (CASTOR) mappando i dati provenienti dall XML
     * </p>
     * @param  SBNMarc oggetto radice XML in input
     * @return istanza oggetto (default)
     */
    public CreaAutore(SBNMarc input_root_object) {
        super(input_root_object);
        elementoAut = factoring_object.getCreaTypeChoice().getElementoAut();
        datiElementoAut = elementoAut.getDatiElementoAut();
        arrivoLegame = elementoAut.getLegamiElementoAut();
        tipoControllo = factoring_object.getTipoControllo();
        sbnUser = input_root_object.getSbnUser();
        localizzaType = input_root_object.getSbnMessage().getSbnRequest().getCrea().getLocalizza();

        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
        if (datiElementoAut.getCondiviso() == null ){
        	_condiviso = "s";
        }
        else if ((datiElementoAut.getCondiviso().getType() == DatiElementoTypeCondivisoType.S_TYPE)){
        	_condiviso = "s";
        }
        else if ((datiElementoAut.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE)){
        	_condiviso = "n";
        }

//            if (datiElementoAut.getCondiviso() == null && (datiElementoAut.getCondiviso().equals(DatiElementoTypeCondivisoType.S)) ) {
//            	_condiviso = datiElementoAut.getCondiviso().toString();
//            }else{
//            	_condiviso = "n";
//            }

        //}

   }

    /**
     * Esamina i parametri di ricerca ricevuti via xml e attiva il metodo di ricerca
     * opportuno.
     * gestisce il tipo di risposta richiesto (lista o esame) e produce il
     * file xml di output richiesto
     * @throws Exception
     * @throws SQLException
     * @throws InvocationTargetException
     * @throws IndexOutOfBoundsException
     * @throws IllegalArgumentException
     */
    protected void executeCrea()
        throws IllegalArgumentException, IndexOutOfBoundsException, InvocationTargetException, SQLException, Exception {

        // almaviva qui controllo se la forma di rinvio che si vuole inserire in cattura già non sia presete
        // nel db in caso positivo tolto i flag di cancellazione e invio un messaggio di avvenuto ripristino
        if(formaRinvioEsistente()){
            throw new EccezioneSbnMessage(3336);
        	//return;
        }

        //tipoForma=null;
        //controllo la validità del luogo
        AutoreValida validaAutore = new AutoreValida(factoring_object);
        boolean ignoraID = datiElementoAut.getT001().equals(SBNMARC_DEFAULT_ID);
		TipiAutore ca = new TipiAutore();

		Tb_autore tb_autore = ca.costruisciAutore(datiElementoAut,null);
        String user = getCdUtente();
        boolean simileImport = false;
        if (SbnSimile.SIMILEIMPORT.getType() == tipoControllo.getType()) {
            //20.7.2005 non fa più in controllo sull'esistenza id
            //	ignoraID = true;
             simileImport = true;
        }

        if (validaAutore.validaPerCrea(user,ignoraID,tb_autore,isScheduled(),simileImport,_cattura)) {
            if (SbnSimile.SIMILEIMPORT.getType() == tipoControllo.getType())
                throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
            Autore autoreDB = new Autore();
            //controllo se esistono informazioni ArrivoLegami

            // Marzo 2016: gestione ISNI (International standard number identifier)
            if (tb_autore.getISADN() != null) {
            	TableDao tavola = validaAutore.estraiAutorePerISNI(tb_autore.getISADN());
				if (tavola.getElencoRisultati() != null && tavola.getElencoRisultati().size() > 0 )
					throw new EccezioneSbnDiagnostico(3392); //"Numero ISNI esistente");
	        }


            if (ignoraID) {
                //Devo settare il vid automaticamente
                Progressivi progress = new Progressivi();
                tb_autore.setVID(progress.getNextIdAutore());
            } else
                tb_autore.setVID(datiElementoAut.getT001());

            if (SbnSimile.CONFERMA.getType() == tipoControllo.getType()  && !scheduled) {
                tb_autore.setUTE_FORZA_INS(user);
                tb_autore.setUTE_FORZA_VAR(user);
            }
            tb_autore.setUTE_INS(user);
            tb_autore.setUTE_VAR(user);
            tb_autore.setVID_LINK("");
            // Timbro Condivisione
            tb_autore.setFL_CONDIVISO(_condiviso);
            tb_autore.setTS_CONDIVISO(TableDao.now());
            tb_autore.setUTE_CONDIVISO(user);

            //bug #3796 esercizio
            //disallineamento polo/indice
            Tb_autore tb_autore2 = validaAutore.verificaEsistenzaID();
            if (tb_autore2 != null && tb_autore2.getFL_CANC().equals("S") && tb_autore2.getFL_CONDIVISO().equals("s")) {
            	//autore cancellato logicamente in polo e condiviso con indice
            	tb_autore2.setFL_CANC(" ");
            	tb_autore2.setUTE_VAR(user);
            	autoreDB.eseguiUpdate(tb_autore2);
            } else {
            	//autore fisicamente non esistente in polo
            	autoreDB.inserisciAutore(tb_autore);
            }

            int contalegami4xx = 0;
            if (elementoAut.getLegamiElementoAutCount() > 0) {
                for (int i = 0; i < elementoAut.getLegamiElementoAut().length; i++) {
                    LegamiType legame = elementoAut.getLegamiElementoAut()[i];
                    for (int j = 0; j < legame.getArrivoLegameCount(); j++) {
                        LegameElementoAutType legEl = legame.getArrivoLegame(j).getLegameElementoAut();
                        //Non so se utilizzare AutoreDB o AutoreAutoreDB
                        AutoreAutore autoreAutore = new AutoreAutore();
                        if (legEl.getTipoAuthority().getType() == SbnAuthority.AU_TYPE)
                            if (autoreAutore.validaPerCreaLegame(user, tb_autore, legame,_cattura)) {
                                //preparo per inserire il legame nel db
                                Tr_aut_aut autaut = new Tr_aut_aut();

                                autaut.setFL_CANC(" ");
                                autaut.setNOTA_AUT_AUT(legEl.getNoteLegame());
                                autaut.setUTE_INS(user);
                                autaut.setUTE_VAR(user);
                                autaut.setVID_COLL(tb_autore.getVID());
                                autaut.setVID_BASE(legEl.getIdArrivo());
                                if (legEl.getTipoLegame().getType() == SbnLegameAut.valueOf("5XX").getType())
                                    autaut.setTP_LEGAME("4");
                                else if (legEl.getTipoLegame().getType() == SbnLegameAut.valueOf("4XX").getType()) {
                                    if (!tb_autore.getTP_FORMA_AUT().equals("R"))
                                        throw new EccezioneSbnDiagnostico(3031);
                                    autaut.setTP_LEGAME("8");
                                    contalegami4xx++;
                                    //Aggiorna anche l'autore R.(ts_var e ute_var) e flag
                                    autoreDB.updateVariazione(legEl.getIdArrivo(),user);
                                    AllineamentoAutore all = new AllineamentoAutore(autoreDB.estraiAutorePerID(legEl.getIdArrivo()));
                                    all.setTr_aut_aut(true);
                                    new AutoreAllineamento().aggiornaFlagAllineamento(all,user);
                                } else {
                                    log.error("Tipo legame sconosciuto");
                                    throw new EccezioneSbnDiagnostico(3031);
                                }
                                autoreAutore.inserisciAutoreAutore(autaut);

                            } else { //Problema nella validazione legami
                                throw new EccezioneSbnDiagnostico(3038);
                            }
                        else if (legEl.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
                            AutoreMarca autoreMarca = new AutoreMarca();
                            autoreMarca.validaPerCreaLegame(tb_autore,legame.getArrivoLegame(j));
                            Tr_aut_mar autmar = new Tr_aut_mar();
                            autmar.setFL_CANC("N");
                            autmar.setVID(tb_autore.getVID());
                            autmar.setMID(legEl.getIdArrivo());
                            autmar.setNOTA_AUT_MAR(legEl.getNoteLegame());
                            autmar.setUTE_INS(user);
                            autmar.setUTE_VAR(user);
                            autoreMarca.inserisciAutoreMarca(autmar);
                            Marca marcaDB = new Marca();
                            marcaDB.updateVersione(legEl.getIdArrivo(),user);
                            Tb_marca mar = new Tb_marca();
                            mar.setMID(legEl.getIdArrivo());
                            AllineamentoMarca flagAllineamentoMarca = new AllineamentoMarca(mar);
                            flagAllineamentoMarca.setLegami(true);
                            // almaviva LA LOCALIZZAZIONE PER MARCA E' STATA ELIMINATA IN QUANTO UNA MARCA IN POLO E'
                    		// AUTOMATICAMENTE LOCALIZZATA QUINDI SIA IN FASE DI INSERIMENTO CHE MODIFICA NON EFFETTUO NESSUN
                    		// TIPO DI OPERAZIONE SULLA TR_MAR_BIB CHE NON HA PIU' MOTIVO DI ESISTERE
                    		// VERIFICARE ALTRE EVENTUALI CHIAMATE A QUESTA TABELLA
                    		// INDICAZIONI DI ROSSANA 03/04/2007
                            //new MarcaAllineamento().aggiornaFlagAllineamento(
                            //    flagAllineamentoMarca,
                            //    getCdUtente());
                        } else if (legEl.getTipoAuthority().getType() == SbnAuthority.RE_TYPE) {
                            RepertorioAutore repAut = new RepertorioAutore();
                            repAut.validaPerCreaLegame(tb_autore.getVID(),legame.getArrivoLegame(j),null);
                            Repertorio rep = new Repertorio();
                            Tb_repertorio tb_repertorio =
                                rep.cercaRepertorioPerCdSig(legEl.getIdArrivo());

                            Tr_rep_aut tr_rep_aut = new Tr_rep_aut();
                            tr_rep_aut.setFL_CANC(" ");
                            tr_rep_aut.setUTE_INS(user);
                            tr_rep_aut.setUTE_VAR(user);
                            tr_rep_aut.setVID(tb_autore.getVID());
                            tr_rep_aut.setID_REPERTORIO(tb_repertorio.getID_REPERTORIO());
                            //810 -s 815 -> n
                            if (legEl.getTipoLegame().getType() == SbnLegameAut.valueOf("810").getType())
                                tr_rep_aut.setFL_TROVATO("S");
                            else if (legEl.getTipoLegame().getType() == SbnLegameAut.valueOf("815").getType())
                                tr_rep_aut.setFL_TROVATO("N");
                            tr_rep_aut.setNOTE_REP_AUT(legEl.getNoteLegame());

                            repAut.inserisciRepertorioAutore(tr_rep_aut);
                        }
                    }
                }
            }
            if (tb_autore.getTP_FORMA_AUT().equals("R") && contalegami4xx == 0)
                throw new EccezioneSbnDiagnostico(3125,"Manca la forma accettata");
            TableDao tavola = autoreDB.cercaAutorePerID(tb_autore.getVID());
            autoreCreato = (Tb_autore) tavola.getElencoRisultati().get(0);

            object_response = formattaOutput();

            localizzaAutore();

        } else {
            //ritorna il messaggio di diagnostica
            elencoDiagnostico = validaAutore.getElencoDiagnostico();
            settaIdLista();
            if (elencoDiagnostico != null && elencoDiagnostico.size() > 0) {
                object_response = formattaLista(1);
            } else {
                //Qui non ci dovrebbe mai arrivare: utilizza le eccezioni.
                object_response = validaAutore.getDiagnostico();
            }

            //throw new EccezioneFactoring("Errore XYZ");
        }

    }



	/**
     * Metodo che invoca la localizzazione dell'autore implementata in
     * LocalizzaFactoring
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void localizzaAutore() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (localizzaType != null) {
            LocalizzaInfoType[] TableDaoLocalizza = null;
            int localizzaCount = 0;
            localizzaCount = localizzaType.getLocalizzaInfoCount();
            TableDaoLocalizza = localizzaType.getLocalizzaInfo();
            if (localizzaType != null)
                for (int i = 0; i < localizzaCount; i++) {
                    String sbnIdLoc = null;
                    SbnTipoLocalizza tipoLocalizza = null;
                    SbnAuthority tipoAuthority = null;
                    SbnMateriale tipomateriale = null;
                    SbnAzioneLocalizza tipoOperazione = null;
                    C899[] t899 = null;
                    sbnIdLoc = TableDaoLocalizza[i].getSbnIDLoc();
                    t899 = TableDaoLocalizza[i].getT899();
                    tipoOperazione = TableDaoLocalizza[i].getTipoOperazione();
                    tipoLocalizza = TableDaoLocalizza[i].getTipoInfo();
                    tipomateriale = TableDaoLocalizza[i].getTipoOggetto().getTipoMateriale();
                    tipoAuthority = TableDaoLocalizza[i].getTipoOggetto().getTipoAuthority();
                    List t899_TableDao = new ArrayList();
                    if (t899 != null)
                        for (int j = 0; j < t899.length; j++) {
                            t899_TableDao.add(t899[j]);
                        }
                    if (tipoAuthority != null) {
                        if (tipoOperazione.getType() == SbnAzioneLocalizza.LOCALIZZA_TYPE) {
                                LocalizzaFactoring localizzaFactoring =
                                    new LocalizzaFactoring(
                                        localizzaType,
                                        sbnUser,
                                        sbnUser.getBiblioteca());
                                localizzaFactoring.localizza(
                                    tipoAuthority,
                                    tipomateriale,
                                    sbnIdLoc,
                                    t899_TableDao,
                                    tipoLocalizza,
                                    sbnUser.getBiblioteca());
                        } else
                            throw new EccezioneSbnDiagnostico(3035, "operazione non valida");
                    }
                }
        }
    }

    /** Prepara la lista per il diagnostico, qualora esistano altri autori simili
     * E' copiata pari pari da CreaAutore: sarebbe il caso di farne una sola.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected SBNMarc formattaLista(int numeroBlocco)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
        result.setEsito("3004");
        result.setTestoEsito("Trovati autori con nomi simili");
        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(sbnUser);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        FormatoAutore forAut = new FormatoAutore(SbnTipoOutput.valueOf("003"), null, sbnUser);
        Tb_autore tb_autore;
        int numeroPrimo = (numeroBlocco-1) * maxRighe;
        for (int i = numeroPrimo;(i < numeroPrimo + maxRighe) && (i < elencoDiagnostico.size()); i++) {
            tb_autore = (Tb_autore) elencoDiagnostico.get(i);
            output.addElementoAut(forAut.formattaAutore(tb_autore));
        }
        output.setMaxRighe(maxRighe);
        output.setNumPrimo(numeroPrimo + 1);
        output.setIdLista(idLista);
        //output.setTipoOrd(factoring_object.getTipoOrd());
        output.setTotRighe(elencoDiagnostico.size());
        output.setTipoOutput(SbnTipoOutput.valueOf("003"));

        return sbnmarc;
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
        sbnmarc.setSbnUser(sbnUser);
        sbnmarc.setSchemaVersion(schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        DatiElementoType datiResp;
		if (new TipiAutore().isPersonale(autoreCreato)) {
			datiResp = new AutorePersonaleType();
			if (autoreCreato.getISADN() != null) {
				A015 a015 = new A015();
				a015.setA_015(autoreCreato.getISADN());
				((AutorePersonaleType)datiResp).setT015(a015);
			}
		} else {
			datiResp = new EnteType();
			if (autoreCreato.getISADN() != null) {
				A015 a015 = new A015();
				a015.setA_015(autoreCreato.getISADN());
				((EnteType)datiResp).setT015(a015);
			}
		}
        datiResp.setT001(autoreCreato.getVID());
        SbnDatavar data = new SbnDatavar(autoreCreato.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        //datiResp.setT005(autoreCreato.getTS_VAR().getDate());
        datiResp.setLivelloAut(elementoAut.getDatiElementoAut().getLivelloAut());

        datiResp.setTipoAuthority(SbnAuthority.AU);
        ElementAutType elementoAutResp = new ElementAutType();
        elementoAutResp.setDatiElementoAut(datiResp);
        output.addElementoAut(elementoAutResp);
        output.setMaxRighe(1);
        output.setNumPrimo(1);
        output.setTipoOrd(SbnTipoOrd.VALUE_0);
        output.setTotRighe(1);
        output.setTipoOutput(SbnTipoOutput.VALUE_0);

        return sbnmarc;
    }

    public String getIdAttivita(){
    	if(_cattura){ // SE SONO IN CATTURA DEVO SOLO CONTROLLARE CHE SIA ABILITATO A CREARE UN DOCUMENTO
        	// Inizio Intervento google3: Per cattura non viene chiamata la verifica delle abilitazioni
        	// serve solo il Cerca + Localizza per Gestione
    		return CodiciAttivita.getIstance().LOCALIZZA_PER_GESTIONE_1009;
    		// return CodiciAttivita.getIstance().CREA_DOCUMENTO_1016;
    		// Fine Intervento google3
    	}
    	else{
    		return CodiciAttivita.getIstance().CREA_ELEMENTO_DI_AUTHORITY_1017;
    	}
    }
    public String getIdAttivitaSt(){
    	if(_cattura){ // SE SONO IN CATTURA DEVO SOLO CONTROLLARE CHE SIA ABILITATO A CREARE UN DOCUMENTO
    		return CodiciAttivita.getIstance().CREA_DOCUMENTO_1016;
    	}
    	else{
    		return CodiciAttivita.getIstance().CREA_AUTORE_1256;
    	}
    }

     public boolean formaRinvioEsistente() throws IllegalArgumentException, InvocationTargetException, Exception{
        boolean trovato= false;
        if( (_cattura) && (datiElementoAut.getFormaNome().toString().equals("R")) ){
            if (elementoAut.getLegamiElementoAutCount() > 0) {
                for (int i = 0; i < elementoAut.getLegamiElementoAut().length; i++) {
                    LegamiType legame = elementoAut.getLegamiElementoAut()[i];
                    for (int j = 0; j < legame.getArrivoLegameCount(); j++) {
                        LegameElementoAutType legEl = legame.getArrivoLegame(j).getLegameElementoAut();
                        if(legEl.getTipoLegame().toString().equals("4XX")){
                            if (legEl.getTipoAuthority().getType() == SbnAuthority.AU_TYPE){
                                AutoreValida validaAutore = new AutoreValida(factoring_object);
                                TipiAutore ca = new TipiAutore();
                                Tb_autore tb_autore = ca.costruisciAutore(datiElementoAut,null);
                                tb_autore = validaAutore.verificaEsistenzaID();
                                if ((tb_autore != null) && (tb_autore.getFL_CANC().equals("S"))) {
                                    tb_autore.setFL_CANC(" ");
                                    Tb_autoreResult tb_autoreResult = new Tb_autoreResult(tb_autore);
                                    tb_autoreResult.update(tb_autore);
                                    //tb_autoreResult.updateFlag(tb_autore);
                                    //ritorno un diagnostico
                                    log.debug("Autore in forma rinvio già esiste è necessario abilitarlo");
                                    trovato = true;
                                }
                                AutoreAutore autoreAutore = new AutoreAutore();
                                Tr_aut_aut autAut = autoreAutore.verificaEsistenza(legEl.getIdArrivo().toString(),datiElementoAut.getT001());
                                if ((autAut != null) && (autAut.getFL_CANC().equals("S"))) {
                                    autAut.setFL_CANC(" ");
                                    Tr_aut_autResult tr_aut_autResult = new Tr_aut_autResult(autAut);
                                    tr_aut_autResult.UpdateAbilitaFormaRinvioEsistente(autAut);
                                    log.debug("legame autore_autore già esiste è necessario abilitarlo");
                                    trovato = true;
                                }
                                //trovato = true;
                                    //ritorno un diagnostico
                            }
                        }
                    }
                }
            }
        }
        // NON è una forma di rinvio
        return trovato;
    }




}
