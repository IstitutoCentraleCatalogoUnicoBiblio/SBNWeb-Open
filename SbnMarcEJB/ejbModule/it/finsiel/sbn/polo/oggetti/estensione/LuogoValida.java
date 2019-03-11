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
package it.finsiel.sbn.polo.oggetti.estensione;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_luoResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.NormalizzaNomi;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Luogo;
import it.finsiel.sbn.polo.oggetti.LuogoLuogo;
import it.finsiel.sbn.polo.orm.Tb_luogo;
import it.finsiel.sbn.polo.orm.Tr_luo_luo;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_luo;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LuogoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author
 */

/**
 * Valida le informazioni relative a un Luogo.
 * Input= DatiElementoAut con tipoAuthority = 'Luogo'
 * Operazioni di validazione:
 * controllo esistenza con identificativo: T001 = ID_luogo
 * controllo versione: T005 = ts_var
 * controllo esistenza con chiave normalizzata: ky_norm_luogo
 * controllo abilitazione su livello utente/cd_livello
 */
public class LuogoValida extends Luogo {

	private static final long serialVersionUID = 6065949065594811491L;
	protected static ValidatorProfiler validator = ValidatorProfiler.getInstance();
	static String SIGLA_INDICE = validator.getCodicePolo();

    //static String SIGLA_INDICE = ResourceLoader.getPropertyString("SIGLA_INDICE");

    private DatiElementoType _datiElementoAut;

    private SbnSimile _tipoControllo;
    private CreaType _datiType;
    private ModificaType _datiModificaType;
    //	public 	boolean 			_esistenzaID = false;

    public LuogoValida() {
        super();
    }

    /** costruttore utilizzato per la ricerca di un luogo */
    public LuogoValida(CreaType datiType) throws EccezioneDB {
        super();
        _datiType = datiType;
        _tipoControllo = datiType.getTipoControllo();
        _datiElementoAut = datiType.getCreaTypeChoice().getElementoAut().getDatiElementoAut();
    }

    /** costruttore utilizzato per la modifica di un luogo */
    public LuogoValida(ModificaType datiType) throws EccezioneDB {
        super();
        _datiModificaType = datiType;
        _tipoControllo = datiType.getTipoControllo();
        if (this._tipoControllo == null) {
            this._tipoControllo = SbnSimile.SIMILE;
        }
        _datiElementoAut = datiType.getElementoAut().getDatiElementoAut();
    }

    /**
     * metodo di validazione per operazione di creazione luogo:
     * - verificaEsistenzaID: se trovato ritorna diagnostico 'Luogo esistente'
     * se tipoControllo <> 'Conferma'
     * - verifica EsistenzaPerNome: se trovato/i ritorna la lista  sintetica dei
     * luoghi/luogo trovato al client
     * se non esistono luoghi simili ritorna ok al chiamante
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List validaPerCrea(String utente, boolean ignoraId,boolean _cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        List listaLuoghi = new ArrayList();
        Tb_luogo tb_luogo = null;
        if (!ignoraId) {
            tb_luogo = verificaEsistenzaID();
            if (tb_luogo != null) {
            	if(!_cattura)
            		throw new EccezioneSbnDiagnostico(3012,"Luogo gia presente");
            }
            if (!verificaLid(_datiElementoAut.getT001(),utente))
                throw new EccezioneSbnDiagnostico(3902, "Lid non valido");
        }
        String livelloAutInput = _datiElementoAut.getLivelloAut().toString();
        controllaParametriUtente(utente, livelloAutInput,_cattura);
        verificaCorrettezza();
        if (_tipoControllo.getType() == SbnSimile.CONFERMA_TYPE) {
            //creo senza fare alcun controllo
        } else if (
            _tipoControllo.getType() == SbnSimile.SIMILE_TYPE || (_tipoControllo.getType() == (SbnSimile.SIMILEIMPORT_TYPE))) {
            listaLuoghi = verificaEsistenzaPerNome();
            //se listaLuoghi è vuota significa che non esistono luoghi simili
        }
        return listaLuoghi;
    }

    private void verificaCorrettezza() throws EccezioneSbnDiagnostico {
        if (_datiElementoAut instanceof LuogoType) {
            LuogoType luogo = (LuogoType) _datiElementoAut;
            if (luogo.getT260() != null) {
            	String cd_paese = null;
            	if (luogo.getT260().getA_260() != null) {
            		cd_paese = Decodificatore.getCd_tabella("Tb_luogo","cd_paese",luogo.getT260().getA_260().toUpperCase());
            	}
            	if (cd_paese == null){
            		cd_paese = "UN";
            	}
                if (Decodificatore.getCd_tabella("Tb_luogo", "cd_paese", cd_paese) == null)
                	throw new EccezioneSbnDiagnostico(3205, "Codice paese errato");
            }
        }

    }

    /**
     * metodo di validazione per operazione di modifica luogo:
     * . verificaEsistenzaID: se non trovato ritorna diagnostico 'Luogo inesistente'
     * . verificaVersioneLuogo: se il risultato è negativo ritorna il diagnostico '
     * Versione non aggiornata'
     * . verificaLivelloModifica: se il risultato è falso ritorna il diagnostico al
     * client.
     * se tipoControllo <> 'Conferma'
     * - verifica EsistenzaPerNome: se trovato/i ritorna la lista  sintetica dei
     * luoghi/luogo trovato al client
     * se non esistono luoghi simili ritorna ok al chiamante
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List validaPerModifica(String utente, String livelloAut, TimestampHash timeHash, boolean _cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        List esistenzaPerNome = null;
        Tb_luogo tb_luogo = null;
        tb_luogo = cercaPerID();
        if (tb_luogo != null) {
            timeHash.putTimestamp("Tb_luogo", tb_luogo.getLID(), new SbnDatavar( tb_luogo.getTS_VAR()));
            if (_datiElementoAut.getT005() != null) {
                if (!verificaVersioneLuogo(tb_luogo)) {
                    throw new EccezioneSbnDiagnostico(3014);
                } else {
                    String livelloAutInput = _datiElementoAut.getLivelloAut().toString();
                    if (!verificaLivello(livelloAutInput, tb_luogo)) {
                        throw new EccezioneSbnDiagnostico(3008);
                    } else {
                        verificaCorrettezza();
                        if ((_tipoControllo.getType() != SbnSimile.CONFERMA_TYPE)
                            && (((LuogoType) _datiElementoAut).getT260() != null)) {
                            esistenzaPerNome = verificaEsistenzaPerNomeInModifica();
                        }
                    }
                }
            } else {
                throw new EccezioneSbnDiagnostico(3017);
            }
        } else {
            throw new EccezioneSbnDiagnostico(3013);
        }
        controllaParametriUtente(utente, livelloAut,_cattura);
        return esistenzaPerNome;
    }

    public void controllaParametriUtente(String utente, String livelloAut,boolean _cattura) throws EccezioneSbnDiagnostico {

        if(!_cattura){
            Tbf_par_auth par = ValidatorProfiler.getInstance().getParametriUtentePerAuthority(utente, "LU");
	        if (par == null)
	            throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");
	        if (par.getTp_abil_auth()!='S')
	            throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");
	        String livelloUtente = par.getCd_livello();
	        if (Integer.parseInt(livelloAut) > Integer.parseInt(livelloUtente))
	            throw new EccezioneSbnDiagnostico(3007, "Livello di autorità utente non consente l'operazione");
        }

    }

    /**
     * questo metodo è utilizzato per
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List verificaEsistenzaPerNomeInModifica() throws IllegalArgumentException, InvocationTargetException, Exception {
        List listaLuoghi = new ArrayList();
        LuogoType luogo = new LuogoType();
        luogo = (LuogoType) _datiElementoAut;
        String formaNome = null;
        String nome = null;

        if (luogo.getT260().getD_260() != null) {
            Luogo luogoDB = new Luogo();
            nome = NormalizzaNomi.normalizzazioneGenerica(luogo.getT260().getD_260());

            listaLuoghi = luogoDB.cercaLuoghiSimili(nome, luogo.getT001(),luogo.getT260().getD_260());
        }
        return listaLuoghi;
    }

    /**
     * Metodo che verifica se esistono luoghi con lo stesso nome già in base dati:
     * . se d_260 è valorizzato invoca   cercaLuogo.cercaLuogoPerNome(); se esiste uno
     * o più luoghi ritorna la lista sintetica dei luoghi
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List verificaEsistenzaPerNome() throws IllegalArgumentException, InvocationTargetException, Exception {
        List listaLuoghi = new ArrayList();
        LuogoType luogo = new LuogoType();
        luogo = (LuogoType) _datiElementoAut;

        if (luogo.getT260().getD_260() != null)
            listaLuoghi = cercaLuogoSimile(luogo.getT260().getD_260());
        return listaLuoghi;
    }

    public List cercaLuogoSimile(String nome) throws IllegalArgumentException, InvocationTargetException, Exception {
        List listaLuoghiResult = null;
        Tb_luogo tab_luogo = new Tb_luogo();
        String cd_livello = null;
        Luogo luogo = new Luogo();
        String ds_luogo = nome;
        //routine di normalizzazione al nome: va sostituito con un metodo più generico!!!
        if (NormalizzaNomi.normalizzazioneGenerica(nome) != null) {
            nome = NormalizzaNomi.normalizzazioneGenerica(nome);
        }

        if (nome != null)
        	listaLuoghiResult = luogo.cercaLuogoSimile(nome, ds_luogo);
        return listaLuoghiResult;
    }

    /**
     * Metodo che verifica se il luogo esiste già in base dati:
     * . se T001 è valorizzato invoca   cercaLuogo.cercaLuogoPerID(); se esiste
     * ritorna Tb_luogo
     * input: true se sono in fase di ricerca, false altrimenti
     * @throws InfrastructureException
     */
    public Tb_luogo cercaPerID() throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        Tb_luogo tb_luogo = null;
        if (_datiElementoAut.getT001() != null) {
            Luogo luogo = new Luogo();
            tb_luogo = luogo.cercaLuogoPerID(_datiElementoAut.getT001());
        }
        return tb_luogo;
    }

    public Tb_luogo verificaEsistenzaID() throws IllegalArgumentException, InvocationTargetException, Exception {
      Tb_luogo tb_luogo = null;
      if (_datiElementoAut.getT001() != null) {
        Luogo luogo = new Luogo();
        tb_luogo = luogo.cercaLuogoPerEsistenza(_datiElementoAut.getT001());
      }
      return tb_luogo;
    }

    /**
     * metodo che controlla il vincolo di versione in modifica:
     * T005 deve essere uguale a ts_var
     * NB: questo metodo è comune, può essere spostato in Factoring
     */
    public boolean verificaVersioneLuogo(Tb_luogo tb_luogo) {
        SbnDatavar data = new SbnDatavar(tb_luogo.getTS_VAR());
        return data.getT005Date().equals(_datiElementoAut.getT005());
    }

    /**
     * metodo che verifica se l'utente ha sufficente livello di autorità per
     * effettuare l'operazione di modifica richiesta.
     * decodifica livelloAut in tb_codici
     * se livelloAut > cd_livello letto sul db predispone in diagnostico: 'Livello di
     * autorità utente non consente l'operazione'.
     * NB: questo metodo è comune, può essere spostato in Factoring
     */
    public boolean verificaLivello(String livelloAut, Tb_luogo tb_luogo) throws EccezioneSbnDiagnostico {
        boolean verificato = false;
        if (Integer.parseInt(livelloAut) < Integer.parseInt(tb_luogo.getCD_LIVELLO())) {
        	throw new EccezioneSbnDiagnostico(3010,"Livello di autorità in base dati superiore a quello comunicato");
        } else
            verificato = true;
        return verificato;
    }

    private void aggiornaTimeHash(Tb_luogo luogo, TimestampHash timeHash) throws IllegalArgumentException, InvocationTargetException, Exception {
        timeHash.putTimestamp("Tb_luogo", luogo.getLID(), new SbnDatavar( luogo.getTS_VAR()));
        //inserisco nella timehash anche i legami
        LuogoLuogo luogoLuogo = new LuogoLuogo();
        List vettoreDiLegami = new ArrayList();
        Tr_luo_luo legameDummy = new Tr_luo_luo();

        vettoreDiLegami = luogoLuogo.cercaLegami(luogo.getLID());
        for (int i = 0; i < vettoreDiLegami.size(); i++) {
            legameDummy = (Tr_luo_luo) vettoreDiLegami.get(i);
            timeHash.putTimestamp(
                "Tr_luo_luo",
                legameDummy.getLID_BASE() + legameDummy.getLID_COLL(),
                new SbnDatavar( legameDummy.getTS_VAR()));
        }

    }

    /**
     * Method validaPerCancella.
     *
     * @param _idCancellazione
     * @param user
     * @return Tb_luogo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public Tb_luogo validaPerCancella(String lid, String user, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_luogo tb_luogo = null;
        Luogo luogo = new Luogo();
        tb_luogo = luogo.cercaLuogoPerID(lid);
        if (tb_luogo == null)
            throw new EccezioneSbnDiagnostico(3001, "luogo non esistente");
        if (tb_luogo.getTP_FORMA().equals("A")) {
            //controlla i legami con i titoli
            Vl_titolo_luo tit = new Vl_titolo_luo();
            tit.setLID(lid);
            Vl_titolo_luoResult tavola = new Vl_titolo_luoResult(tit);


            tavola.executeCustom("selectPerLuogo");
            List vec = tavola.getElencoRisultati();

            if (vec.size() > 0)
                throw new EccezioneSbnDiagnostico(3091, "Esistono legami a titoli");
        }
        controllaParametriUtente(user, tb_luogo.getCD_LIVELLO(),false);
        aggiornaTimeHash(tb_luogo, timeHash);
        return tb_luogo;
    }

    /**
     * Method validaPerFonde.
     * @param idPartenza
     * @param idArrivo
     * @throws InfrastructureException
     */
    public void validaPerFonde(String utente, String idPartenza, String idArrivo)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        Tb_luogo tb_luogo;
        Luogo luogo = new Luogo();
        tb_luogo = luogo.cercaLuogoPerID(idPartenza);
        if (tb_luogo == null) {
            EccezioneSbnDiagnostico ecc = new EccezioneSbnDiagnostico(3013, "non è stato trovato alcun elemento ");
            ecc.appendMessaggio(": luogo di partenza");
            throw ecc;
        }
        if (!"A".equals(tb_luogo.getTP_FORMA()))
            throw new EccezioneSbnDiagnostico(3075, "tipo forma sbagliato");
        controllaParametriUtente(utente, tb_luogo.getCD_LIVELLO(),false);
        tb_luogo = luogo.cercaLuogoPerID(idArrivo);
        if (tb_luogo == null) {
            EccezioneSbnDiagnostico ecc = new EccezioneSbnDiagnostico(3013, "non è stato trovato alcun elemento ");
            ecc.appendMessaggio(": luogo di arrivo");
            throw ecc;
        }
        if (!tb_luogo.getTP_FORMA().equals("A"))
            throw new EccezioneSbnDiagnostico(3075, "tipo forma sbagliato");
    }

    public boolean verificaLid(String lid, String user) {
        boolean b = false;
        if (lid.length() > 10) {
            return false;
        }
        if (!lid.startsWith(SIGLA_INDICE)) {
            // Ometto il controllo sul POLO che invia la richiesta el il polo contenuto
            // nel ID dell'oggetto
            // Si ipotizza vedi (almaviva2 ) che questo controllo non serva in polo in quanto
            // in fase di cattura devo importare anche id diversi dal polo stesso
            // VERIFICARE SE QUESTO CONTROLLO EFFETTIVAMENTE NON SERVA

//            if (user != null && !user.substring(0,3).equals(lid.substring(0, 3)))
//                return false;

//        	 LA CHAIMATA AL CONTROLLO E' STATO TOLTO SU INDICAZIONE DI almaviva2 12/12/2007
//            if (Decodificatore.getCd_tabella("POLO", lid.substring(0, 3)) == null)
//                return false;
        }
        char[] c_vid = lid.toCharArray();
        if (c_vid[3]=='L') {
            boolean c = true;
            for (int i = 4; i < c_vid.length && b; i++) {
                c = Character.isDigit(c_vid[i]);
            }
            b = c;
        }
        return b;
    }

}
