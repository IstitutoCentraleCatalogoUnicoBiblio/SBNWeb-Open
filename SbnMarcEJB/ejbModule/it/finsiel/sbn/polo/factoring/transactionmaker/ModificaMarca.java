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
import it.finsiel.sbn.polo.factoring.base.NormalizzaNomi;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.AllineamentoAutore;
import it.finsiel.sbn.polo.oggetti.AllineamentoMarca;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Marca;
import it.finsiel.sbn.polo.oggetti.Parola;
import it.finsiel.sbn.polo.oggetti.estensione.AutoreAllineamento;
import it.finsiel.sbn.polo.oggetti.estensione.MarcaValida;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_marca;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A921;
import it.iccu.sbn.ejb.model.unimarcmodel.C856;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.MarcaType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * Classe ModificaMarca
 *
 * Factoring:
 * Modifica una marca come richiesto dal messaggio xml, dopo aver controllato la
 * validità. Aggiorna gli eventuali legami luogo-luogo
 *
 * Tabelle coinvolte:
 * - Tb_marca;
 *
 * OggettiCoinvolti:
 * - Marca
 *
 * Passi da eseguire:
 * Valida la marca: controllo di esistenza per identificativo, controllo su
 * timestamp, controllo su livello di autorità dell'utente, controllo sul livello
 * di autorità della marca.
 * Se viene modificato il nome e tipoControllo <>'Conferma': controllo esistenza
 * marca con nuovo nome (ricerca per nome): se esiste si comunica diagnostico:
 * marca esistente.
 * se non ok ritorna il msg response di diagnostica (analogo a creazione)
 * se ok e se esistono informazioni di ArrivoLegame:
 * valida legami secondo tipoOperazione, che può essere crea,
 * cancella,scambio forma; se crea valgono gli stessi controlli della creazione
 * marca. La cancellazione imposta flag_canc = 'S' su tr_luo_luo.
 * Se ok modifica la marca nel database; modifica i legami nel database; prepara
 * il msg di response di output
 *
 * @param sbnmarcObj
 * @author
 */

public class ModificaMarca extends ModificaElementoAutFactoring {

    private SbnTipoOutput _tipoOut;
    private SbnUserType _sbnUser;
    private BigDecimal _schemaVersion = null;
    private ElementAutType _elementoAut;
    private DatiElementoType _datiElementoAut;
    private String _T001 = null;

    private List _TableDao_response = new ArrayList();
    private ModificaType _modificaType;
    private LegamiType[] _arrivoLegame;

    private String _livelloAut;
    private SbnFormaNome _formaNome;
    private C856 _t856[];
    //private String u_856;
    //	private A921 				_t921;

    private String _t005;
    private int _contaInput;
    private String[] _b921;
    private A921 _a921;

    private StatoRecord _statoRecord;

    private TimestampHash _timeHash = new TimestampHash();
    private String  _condiviso;

    public ModificaMarca(SBNMarc sbnmarcObj) {
        super(sbnmarcObj);
        _elementoAut = sbnmarcObj.getSbnMessage().getSbnRequest().getModifica().getElementoAut();
        _modificaType = sbnmarcObj.getSbnMessage().getSbnRequest().getModifica();
        _datiElementoAut = _elementoAut.getDatiElementoAut();
        _sbnUser = sbnmarcObj.getSbnUser();
        _schemaVersion = sbnmarcObj.getSchemaVersion();
        _T001 = _datiElementoAut.getT001();
        _livelloAut = _datiElementoAut.getLivelloAut().toString();
        _arrivoLegame = this._modificaType.getElementoAut().getLegamiElementoAut();
        _t005 = _datiElementoAut.getT005();
        _contaInput = 0;

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

        if (_modificaType.getElementoAut().getDatiElementoAut() instanceof MarcaType) {
            MarcaType datiElemento = null;
            datiElemento = (MarcaType) _modificaType.getElementoAut().getDatiElementoAut();
            if (datiElemento.getT856Count() > 0)
                _t856 = datiElemento.getT856();
            if (_t856 != null)
                _contaInput++;
//            if (_t856 != null) {
//                u_856 = _t856.getU_856();
//            }
            _a921 = ((MarcaType) _elementoAut.getDatiElementoAut()).getT921();
            if (_a921 != null) {
                _b921 = ((MarcaType) _elementoAut.getDatiElementoAut()).getT921().getB_921();
                _contaInput++;
            } else
                try {
                    throw new EccezioneSbnDiagnostico(3019, "Almeno una parola obbligatoria");
                } catch (EccezioneSbnDiagnostico e) {
                }

            _statoRecord = ((MarcaType) _elementoAut.getDatiElementoAut()).getStatoRecord();
        }
    }

    public SBNMarc formattaOutput() throws EccezioneFactoring, EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        SbnOutputType output = new SbnOutputType();
        result.setEsito("0000");
        result.setTestoEsito("OK");
        sbnmarc.setSbnMessage(message);
        sbnmarc.setSbnUser(_sbnUser);
        sbnmarc.setSchemaVersion(_schemaVersion);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        Tb_marca tb_marca = new Tb_marca();
        Marca marca = new Marca();
        TableDao tav = marca.cercaMarcaPerID(_T001);
        _TableDao_response = tav.getElencoRisultati();

        int numElementi = _TableDao_response.size();
        if (numElementi > 0)
            tb_marca = (Tb_marca) _TableDao_response.get(0);
        DatiElementoType datiResp = new DatiElementoType();
        datiResp.setT001(tb_marca.getMID());
        SbnDatavar data = new SbnDatavar(tb_marca.getTS_VAR());
        datiResp.setT005(data.getT005Date());
        datiResp.setLivelloAut(factoring_object.getElementoAut().getDatiElementoAut().getLivelloAut());
        datiResp.setTipoAuthority(SbnAuthority.valueOf("MA"));
        ElementAutType elementoAutResp = new ElementAutType();
        elementoAutResp.setDatiElementoAut(datiResp);
        output.addElementoAut(elementoAutResp);

        return sbnmarc;
    }

    /**
     * Method executeModifica.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void executeModifica()
        throws IllegalArgumentException, InvocationTargetException, Exception {
        String idArrivo, tipoLegame, idPartenza;
        // almaviva LA LOCALIZZAZIONE PER MARCA E' STATA ELIMINATA IN QUANTO UNA MARCA IN POLO E'
		// AUTOMATICAMENTE LOCALIZZATA QUINDI SIA IN FASE DI INSERIMENTO CHE MODIFICA NON EFFETTUO NESSUN
		// TIPO DI OPERAZIONE SULLA TR_MAR_BIB CHE NON HA PIU' MOTIVO DI ESISTERE
		// VERIFICARE ALTRE EVENTUALI CHIAMATE A QUESTA TABELLA
		// INDICAZIONI DI ROSSANA 03/04/2007
// START LOCALIZZAZIONE MARCA
//        if (validator.isPolo(getCdUtente())) {
//            MarcaBiblioteca marcaBiblioteca = new MarcaBiblioteca();
//            if (marcaBiblioteca.verificaEsistenzaTr_mar_bib(_T001, _sbnUser.getBiblioteca().substring(0, 3))
//                == null)
//                throw new EccezioneSbnDiagnostico(3210, "Marca non localizzata nel polo");
//        }
// END FINE LOCALIZZAZIONE
        //		String testo = "ok";
        MarcaValida marcaValida = new MarcaValida();
        marcaValida.setTrovatoRepertorio(true);
        //19.7.2005 se statoRecord non è c non dove controllare le parole
        boolean modificato = false;
        if (_statoRecord != null && _statoRecord.getType() == StatoRecord.C_TYPE)
        	modificato = true;
        _TableDao_response =
            marcaValida.validaPerModifica(_modificaType, null, _timeHash, getCdUtente(), _livelloAut , modificato,_cattura);
        Tb_marca marcaMod = (Tb_marca) _TableDao_response.get(0);
        AllineamentoMarca flagAllineamento = new AllineamentoMarca(marcaMod);
        Marca marca = new Marca();
        //se sono arrivata quì significa che anche i legami sono stati validati
        //se oltre a t001 e t005 sono stati inseriti altri dati allora significa che siamo in
        //fase di modifica della marca
        if (_statoRecord != null) {
            if (_statoRecord.getType() == StatoRecord.C_TYPE) {
                marca.modificaMarca(marcaMod, _t005, _t856, _a921, getCdUtente(),_condiviso);
                flagAllineamento.setTb_marca(true);
                if (_b921 != null) {
                    aggiornaParoleMarca();
                }
            }
        } else if (_arrivoLegame.length == 0)
            throw new EccezioneSbnDiagnostico(3090, "nessuna modifica richiesta");
        for (int i = 0; i < _arrivoLegame.length; i++) {
            idPartenza = _elementoAut.getLegamiElementoAut(i).getIdPartenza();
            int numeroArrivoLegami = _arrivoLegame[i].getArrivoLegame().length;
            for (int j = 0; j < numeroArrivoLegami; j++) {
                LegameElementoAutType legameElementoAutType = new LegameElementoAutType();
                legameElementoAutType = _arrivoLegame[i].getArrivoLegame(j).getLegameElementoAut();
                idArrivo = legameElementoAutType.getIdArrivo();
                SbnTipoOperazione tipoOperazione = _arrivoLegame[i].getTipoOperazione();
                flagAllineamento.setLegami(true);
                if (tipoOperazione == SbnTipoOperazione.CREA) {
                    //controllo che il legame non esista già
                    marca.creaLegame(legameElementoAutType, idPartenza, getCdUtente());
                } else if (tipoOperazione == SbnTipoOperazione.CANCELLA) {
                    //controllo che i legami da cancellare esistano e mi prendo il timehash
                    marcaValida.validaPerCancellaLegame(legameElementoAutType, idPartenza, _timeHash);
                    marca.cancellaLegame(legameElementoAutType, idPartenza, getCdUtente(), _timeHash);
                } else if (tipoOperazione == SbnTipoOperazione.MODIFICA) {
                    //controllo che i legami da cancellare esistano e mi prendo il timehash
                    marca.modificaLegame(legameElementoAutType, idPartenza, getCdUtente());
                } else {
                    throw new EccezioneFactoring(3035, "tipo operazione sbagliato");
                }

                if (legameElementoAutType.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
                    validaAutorizzazioneAutore(_cattura);
                    Tb_autore aut = new Tb_autore();
                    aut.setVID(legameElementoAutType.getIdArrivo());
                    aut.setFL_CANC("N");
                    AllineamentoAutore flagAllineamentoAutore = new AllineamentoAutore(aut);
                    flagAllineamentoAutore.setTr_aut_mar(true);
                    new AutoreAllineamento().aggiornaFlagAllineamento(
                        flagAllineamentoAutore,
                        getCdUtente());
                    new Autore().updateVariazione(legameElementoAutType.getIdArrivo(), getCdUtente());
                }
            }
        }
        //MarcaBiblioteca marcaBiblioteca = new MarcaBiblioteca();
        //marcaBiblioteca.aggiornaPerAllinea( _codiceUtente,
        //									_T001,
        //									_sbnUser.getBiblioteca().substring(0,3),
        //									_sbnUser.getBiblioteca());
        // almaviva LA LOCALIZZAZIONE PER MARCA E' STATA ELIMINATA IN QUANTO UNA MARCA IN POLO E'
		// AUTOMATICAMENTE LOCALIZZATA QUINDI SIA IN FASE DI INSERIMENTO CHE MODIFICA NON EFFETTUO NESSUN
		// TIPO DI OPERAZIONE SULLA TR_MAR_BIB CHE NON HA PIU' MOTIVO DI ESISTERE
		// VERIFICARE ALTRE EVENTUALI CHIAMATE A QUESTA TABELLA
		// INDICAZIONI DI ROSSANA 03/04/2007
        //new MarcaAllineamento().aggiornaFlagAllineamento(flagAllineamento, getCdUtente());

        object_response = formattaOutput();
    }

    private void validaAutorizzazioneAutore(boolean cattura) throws EccezioneSbnDiagnostico {
    	if(!cattura){
	        Tbf_par_auth par = ValidatorProfiler.getInstance().getParametriUtentePerAuthority(getCdUtente(), "AU");
	        if (par == null)
	           throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");
	        String livelloUtente = par.getCd_livello();
	        if (par.getTp_abil_auth()!='S')
	           throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");
    	}
    }

    /**
     * Cancello tutte le parole legate alla marca in modifica e inserisco quelle nuove
     *
     * questo metodo controlla che le parole da aggiornare non siano già nel db
     * qualora fossero presenti viene restituito un vettore di simili
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void aggiornaParoleMarca() throws IllegalArgumentException, InvocationTargetException, Exception {
        String paroleArray[] = null;
        int size = _b921.length;
        paroleArray = new String[size];
        paroleArray = _b921;
        TableDao vettoreMarche;
        Parola parola = new Parola();
        parola.eliminaParole(_T001, getCdUtente());
        for (int i = 0; i < _b921.length; i++)
            if (!_b921[i].equals("")) {
                String parolaNormalizzata = NormalizzaNomi.normalizzazioneGenerica(_b921[i].trim());
                parola.inserisciParola(_T001, parolaNormalizzata, getCdUtente());
            }
    }

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().MODIFICA_MARCA_1264;
    }
}
