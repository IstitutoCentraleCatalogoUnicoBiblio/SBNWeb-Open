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
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoAutore;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.base.TipiAutore;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.TimbroCondivisione;
import it.finsiel.sbn.polo.oggetti.AllineamentoAutore;
import it.finsiel.sbn.polo.oggetti.AllineamentoMarca;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.AutoreAutore;
import it.finsiel.sbn.polo.oggetti.AutoreBiblioteca;
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
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.AutorePersonaleType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.EnteType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

/**
 * Classe ModificaAutore
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 07-feb-2003
 */
public class ModificaAutore extends ModificaElementoAutFactoring {

    private TimestampHash timeHash = new TimestampHash();
    private BigDecimal _schemaVersion = null;
    private ElementAutType _elementoAut;
    private DatiElementoType _datiElementoAut;
    private String _T001 = null;
    private AutorePersonaleType autorePersonale = null;
    private EnteType autoreEnte = null;

    private ModificaType _modificaType;
    private LegamiType[] legami;

    private SbnLivello _livelloAut;
    private SbnFormaNome _formaNome;
    private SbnSimile tipoControllo;
    private String _userID;
    private String id_lista = null;
    private Tb_autore autoreModificato = null;
    private boolean updateAutoreEseguita = false;
    private StatoRecord _statoRecord;

    private String  _condiviso;

    int maxRighe = Integer.parseInt(ResourceLoader.getPropertyString("RIGHE_PER_BLOCCO_SBNMARC"));

    /**
     * Constructor ModificaAutore
     * @param sbnmarcObj
     */
    public ModificaAutore(SBNMarc sbnmarcObj) {
        super(sbnmarcObj);
        _elementoAut = sbnmarcObj.getSbnMessage().getSbnRequest().getModifica().getElementoAut();
        _modificaType = sbnmarcObj.getSbnMessage().getSbnRequest().getModifica();
        _datiElementoAut = _elementoAut.getDatiElementoAut();
        _userID = getCdUtente();
        _schemaVersion = sbnmarcObj.getSchemaVersion();
        _T001 = _datiElementoAut.getT001();
        _livelloAut = _datiElementoAut.getLivelloAut();
        _formaNome = _datiElementoAut.getFormaNome();
        //controllare i legami!!!!
        legami = _modificaType.getElementoAut().getLegamiElementoAut();
        tipoControllo = _modificaType.getTipoControllo();
        if (_datiElementoAut instanceof AutorePersonaleType) {
            autorePersonale = (AutorePersonaleType) _datiElementoAut;
        } else if (_datiElementoAut instanceof EnteType) {
            autoreEnte = (EnteType) _datiElementoAut;
        }
        _statoRecord = _datiElementoAut.getStatoRecord();

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
        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
//        if (datiElementoAut.getCondiviso() != null && (datiElementoAut.getCondiviso().equals(DatiElementoTypeCondivisoType.S)) ) {
//        	_condiviso = datiElementoAut.getCondiviso().toString();
//        }else{
//        	_condiviso = "n";
//        }

    }

    /** Prepara la stringa risultato dell'operazione */
    public SBNMarc formattaOutput() throws EccezioneFactoring, EccezioneDB, EccezioneSbnDiagnostico {

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
        DatiElementoType datiResp = new DatiElementoType();
        datiResp.setT001(autoreModificato.getVID());
        SbnDatavar data = new SbnDatavar(autoreModificato.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAut(factoring_object.getElementoAut().getDatiElementoAut().getLivelloAut());
        datiResp.setTipoAuthority(SbnAuthority.valueOf("AU"));
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

    private Tb_autore creaAutoreModificato(Tb_autore autore, ModificaType modifica)
        throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        TipiAutore tipiAutore = new TipiAutore();
        Tb_autore autoreTemp;
        if (_statoRecord != null && _statoRecord.getType() == StatoRecord.C_TYPE) {
            autoreTemp = tipiAutore.costruisciAutore(modifica.getElementoAut().getDatiElementoAut(), autore);
        } else {
            autoreTemp = autore;
        }
        autoreTemp.setUTE_VAR(_userID);
        autoreTemp.setVID(autore.getVID());
        if (tipoControllo != null && tipoControllo.getType() == SbnSimile.valueOf("Conferma").getType())
            autoreTemp.setUTE_FORZA_VAR(_userID);
        return autoreTemp;
    }

    /**
     * Esegue la modifica di un autore
     * Due possibilità: una è quella di creare un autore con tutti i campi null tranne quelli
     * modificati,
     * l'altra è quella di modificare i campi dell'autore estratto dalla select e quindi
     * fare l'update di tutti i campi.
     * Per ora uso una soluzione ibrida.
     *
     * Dipende anche da come deve essere gestita la modifica: tutti i campi che è possibile
     * modificare devono essere sostituiti ?
     * @throws InfrastructureException
     */
    private void eseguiModificaAutore(Tb_autore autore) throws EccezioneDB, InfrastructureException {
        Autore autoreDB = new Autore();
        autoreDB.eseguiUpdate(autore);
    }

    /**
     * Esegue la modifica
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void executeModifica() throws IllegalArgumentException, InvocationTargetException, Exception {

        AutoreValida autoreValida = new AutoreValida(_modificaType);
        Autore autoreDB = new Autore();
        boolean modificato = false;
        Tb_autore autore;
        Tb_autore autoreMod = new Tb_autore();
        autore = autoreValida.estraiAutorePerID(_datiElementoAut.getT001());

        // Marzo 2016: gestione ISNI (International standard number identifier)
        String autoreSaveIsadn = null;
        autoreSaveIsadn = autore.getISADN();

        if (autore == null) {
            throw new EccezioneSbnDiagnostico(3013, "Autore non esistente");
        }
        autoreValida.verificaAllineamentoModificaAutore(_datiElementoAut.getT001());
        Tb_autore autoreOld = new Tb_autore();
        autoreOld.setDS_NOME_AUT(autore.getDS_NOME_AUT());
        autoreOld.setVID(autore.getVID());
        autoreOld.setCD_LIVELLO(autore.getCD_LIVELLO());
        autoreOld.setTP_NOME_AUT(autore.getTP_NOME_AUT());
        autoreOld.setTP_FORMA_AUT(autore.getTP_FORMA_AUT());
        autoreOld.setTS_VAR(autore.getTS_VAR());
        if (_statoRecord != null && _statoRecord.getType() == StatoRecord.C_TYPE) {
            modificato = true;
            autoreMod = creaAutoreModificato(autore, _modificaType);
        } else {
            //Se non è modificato devono esistere dei legami.
            if (_modificaType.getElementoAut().getLegamiElementoAutCount() == 0)
                throw new EccezioneSbnDiagnostico(3090, "Nessuna modifica da apportare");
        }
        autoreValida.validaPerModifica(getCdUtente(), autoreOld, autoreMod,_cattura);
        timeHash.putTimestamp("tb_autore", autore.getVID(), new SbnDatavar( autore.getTS_VAR()));
        elencoDiagnostico = autoreValida.getElencoDiagnostico();
        if (elencoDiagnostico != null && elencoDiagnostico.size() > 0) {
            settaIdLista();
            object_response = formattaLista(1);
            return;
        }
        if (modificato) {
            if (!controllaScambioForma()
            	//18.7.2005 modificato autoreMod con autoreOld
                && !autoreOld.getTP_FORMA_AUT().equals(autore.getTP_FORMA_AUT())) {
                throw new EccezioneSbnDiagnostico(3261, "Forma aut non modificabile");
            }

            // Inizio modifica almaviva2 15.07.2010 riportata dal software di Indice identificata con commento "almaviva4 09/07/2010"
            // Modifica almaviva2 09.12.2010 BUG MANTIS 3967 - per non controllare livAut in cattura/allineamento (ModificaAutore-executeModifica);
            if (!_cattura) {
                if (Integer.parseInt(autoreOld.getCD_LIVELLO()) > Integer.parseInt(autoreMod.getCD_LIVELLO())) {
                	throw new EccezioneSbnDiagnostico(3010, "Livello di autorità sulla base dati è superiore a quello comunicato");
                }
            }
            // Fine modifica almaviva2 15.07.2010 "almaviva4 fine 09/07/2010"

            // Marzo 2016: gestione ISNI (International standard number identifier)
            // OVVIAMENTE IN CATTURA LA MODIFICA E' AUTORIZZATA
            // Aprile 2016 almaviva2 su mail carla scognamiglio - è stata inserita la trim del campo autoreSaveIsadn
            // altrimenti si suppone che il campo ISNI sia stato variato (vista la presenza degli spazi) e viene inviato un
            // diagnostico anche quando il campo è rimasto uguale
            if (autore.getISADN() != null) {
            	 if (!_cattura) {
            		 // Aprile 2016 almaviva2 - La variazione dell'Isni deve essere possibile anche da parte dei poli
//            		 if (autoreSaveIsadn != null && !autore.getISADN().equals(autoreSaveIsadn.trim()))
//     					throw new EccezioneSbnDiagnostico(2900, "Numero ISNI non modificabile");
     				TableDao tavola = autoreValida.estraiAutorePerISNIMod(autore.getISADN());
     				if (tavola.getElencoRisultati() != null && tavola.getElencoRisultati().size() > 0 )
     						throw new EccezioneSbnDiagnostico(3392); //"Numero ISNI esistente");
            	 } else {
     				TableDao tavola = autoreValida.estraiAutorePerISNIMod(autore.getISADN());
     				if (tavola.getElencoRisultati() != null && tavola.getElencoRisultati().size() > 0 )
     						throw new EccezioneSbnDiagnostico(3392); //"Numero ISNI esistente");
            	 }
	        }


            TimbroCondivisione timbroCondivisione = new TimbroCondivisione();
            timbroCondivisione.modificaTimbroCondivisione(autore,user_object.getBiblioteca() + user_object.getUserId(),_condiviso);
            eseguiModificaAutore(autore);
            updateAutoreEseguita = true;
        }
        AllineamentoAutore flagAllineamento = new AllineamentoAutore(autore);
        validaLegami(autore);
        AutoreAutore autAut = new AutoreAutore();
        AutoreMarca autMar = new AutoreMarca();
        //almaviva5_20090423 #2789
        //if (legami.length >0 && modificato) {
        if (legami.length > 0) {
          //Aggiorno autore e ts_var se ho anche modificato
          autore = autoreDB.estraiAutorePerID(autore.getVID());
          timeHash.putTimestamp("tb_autore", autore.getVID(), new SbnDatavar( autore.getTS_VAR()));
        }
        for (int i = 0; i < legami.length; i++) {
            for (int j = 0; j < this.legami[i].getArrivoLegame().length; j++) {
                LegameElementoAutType legameElementoAutType =
                    legami[i].getArrivoLegame(j).getLegameElementoAut();
                if (legameElementoAutType.getTipoAuthority() == null
                    || legameElementoAutType.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
                    eseguiModificaAutAut(i, j, autore, autAut, flagAllineamento);
                } else if (legameElementoAutType.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
                    AutoreMarca autoreMarca = new AutoreMarca();
                    Tr_aut_mar autmar = new Tr_aut_mar();
                    autmar.setVID(autore.getVID());
                    autmar.setMID(legameElementoAutType.getIdArrivo());
                    autmar.setUTE_VAR(_userID);
                    autmar.setNOTA_AUT_MAR(legameElementoAutType.getNoteLegame());
                    flagAllineamento.setTr_aut_mar(true);
                    if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.CANCELLA_TYPE) {
                        autmar.setTS_VAR(
                            ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_aut_mar", autmar.getVID() + autMar.getMID())));
                        autoreMarca.cancellaAutoreMarca(autmar, _userID);
                    } else if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.CREA_TYPE) {
                        autmar.setUTE_INS(_userID);
                        autoreMarca.inserisciAutoreMarca(autmar);
                    } else if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.MODIFICA_TYPE) {
//                        autmar.setTS_VAR(
//                        		ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_aut_mar", autmar.getVID() + autMar.getMID())));


                    	// Manutenzione almaviva2 BUG MANTIS 3359 - Inserito if di controllo sulla esistenza
                    	// prima di chiamare la funzione di modificaAutoreMarca
                    	if (autoreMarca.estraiLegame(autmar.getVID(), autmar.getMID()) == null){
                    		throw new EccezioneSbnDiagnostico(3029,"legame non esistente");
                    	}
                        autoreMarca.modificaAutoreMarca(autmar, _userID);
                    }
                    Marca marcaDB = new Marca();
                    marcaDB.updateVersione(legameElementoAutType.getIdArrivo(), _userID);
                    Tb_marca mar = new Tb_marca();
                    mar.setMID(legameElementoAutType.getIdArrivo());
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

                } else if (legameElementoAutType.getTipoAuthority().getType() == SbnAuthority.RE_TYPE) {
                    flagAllineamento.setTr_rep_aut(true);
                    RepertorioAutore repAut = new RepertorioAutore();
                    Tr_rep_aut tr_rep_aut = new Tr_rep_aut();
                    tr_rep_aut.setUTE_VAR(_userID);
                    tr_rep_aut.setVID(autore.getVID());
                    Repertorio rep = new Repertorio();
                    Tb_repertorio tb_repertorio =
                        rep.cercaRepertorioPerCdSig(legameElementoAutType.getIdArrivo());
                    tr_rep_aut.setID_REPERTORIO(tb_repertorio.getID_REPERTORIO());
                    //cancellazione, inserimento o modifica
                    if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.CANCELLA_TYPE) {
                        // mdf eliminato perchè non gestito in modalità cancellazione(concorrenza)
//                        tr_rep_aut.setTS_VAR(
//                        		ConverterDate.SbnDataVarToDate(timeHash.getTimestamp(
//                                "Tr_rep_aut",
//                                tr_rep_aut.getVID() + tr_rep_aut.getID_REPERTORIO())));
                        repAut.cancellaAutoreRepertorio(tr_rep_aut);
                    } else {

                        if (legameElementoAutType.getTipoLegame().getType() == SbnLegameAut.valueOf("810").getType())
                            tr_rep_aut.setFL_TROVATO("S");
                        else if (legameElementoAutType.getTipoLegame().getType() == SbnLegameAut.valueOf("815").getType())
                            tr_rep_aut.setFL_TROVATO("N");
                        tr_rep_aut.setNOTE_REP_AUT(legameElementoAutType.getNoteLegame());

                        if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.CREA_TYPE) {
                            tr_rep_aut.setUTE_INS(_userID);
                            repAut.inserisciRepertorioAutore(tr_rep_aut);
                        } else if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.MODIFICA_TYPE)
                            repAut.modificaAutoreRepertorio(tr_rep_aut);

                    }
                }
            }
        }
        if (!updateAutoreEseguita)
            autoreDB.updateVariazione(autore.getVID(), _userID);
        else
            flagAllineamento.setTb_autore(true);
        new AutoreAllineamento().aggiornaFlagAllineamento(flagAllineamento, getCdUtente());
        autoreModificato = autoreDB.estraiAutorePerID(autore.getVID());
        object_response = formattaOutput();
    }

    private void validaLegami(Tb_autore autore) throws IllegalArgumentException, IndexOutOfBoundsException, InvocationTargetException, Exception {
        //Se non va genera un'eccezione
        AutoreAutore autAut = new AutoreAutore();
        RepertorioAutore repAut = new RepertorioAutore();
        AutoreMarca autMar = new AutoreMarca();
        for (int i = 0; i < legami.length; i++) {
            for (int j = 0; j < this.legami[i].getArrivoLegameCount(); j++) {
                LegameElementoAutType legameElementoAutType =
                    legami[i].getArrivoLegame(j).getLegameElementoAut();
                if (legameElementoAutType.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
                    if (!autAut
                        .validaPerModificaLegame(
                            autore,
                            getCdUtente(),
                            legami[i].getArrivoLegame(j),
                            legami[i].getTipoOperazione(),
                            timeHash,
                            _cattura))
                        throw new EccezioneSbnDiagnostico(3034);
                } else if (legameElementoAutType.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
                    validaAutorizzazioneMarca(_cattura);
                    autMar.validaPerModificaLegame(
                        autore,
                        legami[i].getArrivoLegame(j),
                        legami[i].getTipoOperazione(),
                        timeHash,
                        _cattura);
                } else if (legameElementoAutType.getTipoAuthority().getType() == SbnAuthority.RE_TYPE) {
                    repAut.validaPerModificaLegame(
                        autore.getVID(),
                        legami[i].getArrivoLegame(j),
                        legami[i].getTipoOperazione(),
                        legami,
                        timeHash,
                        _cattura);
                }
            }
        }

    }
    private void validaAutorizzazioneMarca(boolean cattura) throws EccezioneSbnDiagnostico {
    	if(!cattura){
	        Tbf_par_auth par =
	        	ValidatorProfiler.getInstance().getParametriUtentePerAuthority(getCdUtente(), "MA");
	        if (par == null)
	            throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");
	        String livelloUtente = par.getCd_livello();
	        if (par.getTp_abil_auth()!='S')
	            throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");
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
        sbnmarc.setSchemaVersion(schemaVersion);
        sbnmarc.setSbnUser(user_object);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        FormatoAutore forAut = new FormatoAutore(SbnTipoOutput.valueOf("003"), null, user_object);
        Tb_autore tb_autore;
        int numeroPrimo = (numeroBlocco-1) * maxRighe;
        for (int i = numeroPrimo;(i < numeroPrimo + maxRighe) && (i < elencoDiagnostico.size()); i++) {
            tb_autore = (Tb_autore) elencoDiagnostico.get(i);
            output.addElementoAut(forAut.formattaAutore(tb_autore));
        }
        output.setMaxRighe(maxRighe);
        output.setNumPrimo(numeroPrimo + 1);
        output.setIdLista(id_lista);
        //output.setTipoOrd(factoring_object.getTipoOrd());
        output.setTotRighe(elencoDiagnostico.size());
        output.setTipoOutput(SbnTipoOutput.valueOf("003"));

        return sbnmarc;
    }

    /**
     * Modifica un legame tra l'autore e un altro autore
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void eseguiModificaAutAut(
        int i,
        int j,
        Tb_autore autore,
        AutoreAutore autAut,
        AllineamentoAutore flagAll)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        LegameElementoAutType legameElementoAutType = legami[i].getArrivoLegame(j).getLegameElementoAut();
        String idArrivo = legami[i].getArrivoLegame(j).getLegameElementoAut().getIdArrivo();
        String idPartenza = _elementoAut.getLegamiElementoAut(i).getIdPartenza();
        //oppure autore.getVID()
        // almaviva INSERITO CONTROLLO 03/02/2009
        if(idArrivo.equalsIgnoreCase(idPartenza)){
			throw new EccezioneSbnDiagnostico(3337, "Operazione non consentita: I Vid di partenza e di arrivo sono uguali");
        }
        String tipoLegame = legameElementoAutType.getTipoLegame().toString();
        flagAll.setTr_aut_aut(true);
        if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.SCAMBIOFORMA_TYPE) {
            autAut.scambioForma(autore, idArrivo, _userID, timeHash);
            updateAutoreEseguita = true;
            flagAll.setTb_autore_scambio(true);
        } else {
            if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.CREA_TYPE) {
                /* valgono gli stessi controlli della creazione autore */
                Tr_aut_aut aut_aut = new Tr_aut_aut();
                aut_aut.setFL_CANC("N");
                aut_aut.setNOTA_AUT_AUT(legameElementoAutType.getNoteLegame());
                aut_aut.setUTE_INS(_userID);
                aut_aut.setUTE_VAR(_userID);
                if (legameElementoAutType.getTipoLegame().getType() == SbnLegameAut.valueOf("5XX").getType()) {
                    aut_aut.setVID_BASE(autore.getVID());
                    aut_aut.setVID_COLL(idArrivo);
                    aut_aut.setTP_LEGAME("4");
                } else if (legameElementoAutType.getTipoLegame().getType() == SbnLegameAut.valueOf("4XX").getType()) {
                    aut_aut.setTP_LEGAME("8");
                    if (autore.getTP_FORMA_AUT().equals("A")) {
                        aut_aut.setVID_BASE(autore.getVID());
                        aut_aut.setVID_COLL(idArrivo);
                    } else {
                        aut_aut.setVID_COLL(autore.getVID());
                        aut_aut.setVID_BASE(idArrivo);
                        //[TODO VERIFICARE: deve aggiornare anche l'autore R.(ts_var e ute_var) e flag
                        Autore autoreDB = new Autore();
                        autoreDB.updateVariazione(idArrivo, _userID);
                        AllineamentoAutore all = new AllineamentoAutore(autoreDB.estraiAutorePerID(idArrivo));
                        all.setTr_aut_aut(true);
                        new AutoreAllineamento().aggiornaFlagAllineamento(all, _userID);
                    }
                }
                autAut.inserisciAutoreAutore(aut_aut);
            } else if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.CANCELLA_TYPE) {
                /* La cancellazione imposta flag_canc = 'S' su tr_aut_aut e se si tratta di
                 * legame 'vedi' anche sul tb_autore con forma 'R'.
                 */
                autAut.rimuoviLegame(_userID, autore.getVID(), idArrivo, tipoLegame, timeHash);
                Autore autoreDB = new Autore();
                if (legameElementoAutType.getTipoLegame().getType() == SbnLegameAut.valueOf("4XX").getType()) {
                    if (autore.getTP_FORMA_AUT().equals("R")) {
                        // MODIFICA(per gestire gli autori R legati per errore a più forme A):
                        // se l'autore ha un altro legame ad un A non lo cancello.
                        if (autAut.cercaLegamiAutore(autore.getVID(), "8").size() == 0) {
                            autoreDB.rimuoviAutoreRinviato(autore.getVID(), _userID);
                            autore.setFL_CANC("S");
                            AllineamentoAutore all = new AllineamentoAutore(autore);
                            all.setTr_aut_aut(true);
                            all.setTb_autore(true);
                            new AutoreAllineamento().aggiornaFlagAllineamento(all, getCdUtente());
                            new AutoreBiblioteca().aggiornaFlagCancCancellazione(autore.getVID(),getCdUtente());
                        }
                        autoreDB.updateVariazione(idArrivo, _userID);
                        AllineamentoAutore all = new AllineamentoAutore(autoreDB.estraiAutorePerID(idArrivo));
                        all.setTr_aut_aut(true);
                        new AutoreAllineamento().aggiornaFlagAllineamento(all, _userID);
                    } else {
                        // MODIFICA(per gestire gli autori R legati per errore a più forme A):
                        // se l'autore ha un altro legame ad un A non lo cancello.
                        if (autAut.cercaLegamiAutore(idArrivo, "8").size() == 0) {
                            Tb_autore autoreR = autoreDB.estraiAutorePerID(idArrivo);
                            autoreDB.rimuoviAutoreRinviato(idArrivo, _userID);
                            autoreR.setFL_CANC("S");
                            AllineamentoAutore all = new AllineamentoAutore(autoreR);
                            all.setTr_aut_aut(true);
                            all.setTb_autore(true);
                            new AutoreAllineamento().aggiornaFlagAllineamento(all, getCdUtente());
                            new AutoreBiblioteca().aggiornaFlagCancCancellazione(idArrivo,getCdUtente());
                        }
                    }
                }

            } else if (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.MODIFICA_TYPE) {
                //posso modificare solo la nota di un legame.
                String nota = legameElementoAutType.getNoteLegame();

                autAut.updateModifica(
                    idPartenza,
                    idArrivo,
                    timeHash.getTimestamp("Tr_aut_aut", idPartenza + idArrivo),
                    _userID,
                    nota);

            } else {
                throw new EccezioneSbnDiagnostico(3035);
            }
            //Aggiorno ts_var e ute_var nel secondo autore.(il primo dovrebbe
            //essere modificato una volta solo)
            Autore autoreDB = new Autore();
            autoreDB.updateVariazione(idArrivo, autore.getUTE_VAR());
        }
    }

    public String getIdAttivita() {
        if (controllaScambioForma())
            return CodiciAttivita.getIstance().SCAMBIO_FORMA_1029;
        else{
            return CodiciAttivita.getIstance().MODIFICA_ELEMENTO_DI_AUTHORITY_1026;
        	//return CodiciAttivita.getIstance().MODIFICA_AUTORE_1263;
        }
    }


    public String getIdAttivitaSt() {
        return CodiciAttivita.getIstance().MODIFICA_AUTORE_1263;
    }

    private boolean controllaScambioForma() {
        boolean trovato = false;
        int i = 0;
        while ((i < legami.length) && (!trovato)) {
            for (int j = 0; j < this.legami[i].getArrivoLegame().length; j++) {
                LegameElementoAutType legameElementoAutType =
                    legami[i].getArrivoLegame(j).getLegameElementoAut();
                if ((legameElementoAutType.getTipoAuthority().getType() == SbnAuthority.AU_TYPE)
                    && (legami[i].getTipoOperazione().getType() == SbnTipoOperazione.SCAMBIOFORMA_TYPE))
                    trovato = true;
            }
            i++;
        }
        return trovato;
    }


}
