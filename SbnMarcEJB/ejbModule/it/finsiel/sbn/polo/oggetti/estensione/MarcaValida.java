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
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_aut_marResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_rep_marResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.AutoreMarca;
import it.finsiel.sbn.polo.oggetti.Marca;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.oggetti.RepertorioMarca;
import it.finsiel.sbn.polo.oggetti.TitoloMarca;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_marca;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tr_aut_mar;
import it.finsiel.sbn.polo.orm.Tr_rep_mar;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaMarcaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.MarcaType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;



/**
 * @author
 *
 */
public class MarcaValida extends Marca {

	private static final long serialVersionUID = 4198603167363196921L;
	protected static ValidatorProfiler validator = ValidatorProfiler.getInstance();
	static String SIGLA_INDICE = validator.getCodicePolo();

    //static String SIGLA_INDICE = ResourceLoader.getPropertyString("SIGLA_INDICE");

    private DatiElementoType _datiElementoAut;
    private ElementAutType _elementAutType;
    private SbnSimile _tipoControllo;
    private boolean _trovatoRepertorio = false;
    private boolean trovatoRepertorio = true;

    public MarcaValida() {
        super();
    }

    public void setTrovatoRepertorio(boolean valore) {
        trovatoRepertorio = valore;
    }

    /** metodo che verifica l'esistenza di una marca per id
     * se la marca con quell'id esite allora ritorna true, diversamente false
     * @throws InfrastructureException
     */
    public boolean verificaEsistenzaID(String id) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        if (verificaEsistenza(id) != null)
            return true;
        return false;
    }

    public List verificaEsistenzaPerParole(String[] parole, CercaMarcaType cercaMarca, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        TableDao tavolaResponse;
        tavolaResponse = cercaMarcaPerParole(parole, cercaMarca, ordinamento);
        List v = tavolaResponse.getElencoRisultati();

        return v;
    }
    //correggere quì !!!!

    public void validaAutoreMarca() {
    }

    /**
     *	metodo di validazione per operazione di creazione marca
     *	Le parole vanno normalizzare. Almeno una parola è obbligatoria,
     *  al massimo ci possono essere 5 parole.
     *	La descrizione è obbligatoria
     *	- verificaEsistenzaID: se trovato ritorna diagnostico 'Marca esistente'
     *	se tipoControllo <> 'Conferma'
     *	- verifica EsistenzaPerParole: se trovato/i ritorna la lista  sintetica delle
     *    marche trovate al client; se non esistono marche simili ritorna ok al chiamante
     *	Controlli sui legami con repertori:
     *	- Almeno un legame a repertorio è obbligatorio
     *	- Attiva validaPerLegame di RepertorioMarca
     *	Controlli sul legame autore:
     *	- verifica che esista l'autore: cerca per ID, e che tp_nome_aut sia 'E'
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     *
     */
    public List validaPerCrea(boolean ignoraId, CreaType creaType, String user,boolean _cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        String id, tipoControllo;
        List resultTableDao = new ArrayList();
        //considero come "parola" b_921
        String paroleArray[] = null;
        tipoControllo = null;
        _datiElementoAut = creaType.getCreaTypeChoice().getElementoAut().getDatiElementoAut();
        if (!ignoraId && !verificaMid(_datiElementoAut.getT001(),user))
            throw new EccezioneSbnDiagnostico(3902, "Mid non valido");
        _elementAutType = creaType.getCreaTypeChoice().getElementoAut();
        if (((MarcaType) _elementAutType.getDatiElementoAut()).getT921() == null)
            throw new EccezioneSbnDiagnostico(3019, "Almeno una parola obbligatoria");
        if (((MarcaType) _elementAutType.getDatiElementoAut()).getT921().getB_921() != null) {
            int size = ((MarcaType) _elementAutType.getDatiElementoAut()).getT921().getB_921().length;
            if (size == 0)
                throw new EccezioneSbnDiagnostico(3019, "Almeno una parola obbligatoria");
            if (size > 5)
                throw new EccezioneSbnDiagnostico(3021, "Massimo 5 parole");
            paroleArray = ((MarcaType) _elementAutType.getDatiElementoAut()).getT921().getB_921();
            for (int i=0;i<paroleArray.length;i++) {
                if (paroleArray[i].length()>10) {
                    throw new EccezioneSbnDiagnostico(3266, "Max 10 caratteri per parola");
                }
            }
        }
        if ((paroleArray.length != 0) && (!ignoraId)) {
            id = creaType.getCreaTypeChoice().getElementoAut().getDatiElementoAut().getT001();
            if (verificaEsistenzaID(id))
            	if(!_cattura)
            		throw new EccezioneSbnDiagnostico(3012, "Esiste una marca con stesso id");
        }
        //deve esistere almeno un legame
        if (creaType.getCreaTypeChoice().getElementoAut().getLegamiElementoAut().length == 0) {
            throw new EccezioneSbnDiagnostico(3020, "Almeno un legame a repertorio obbligatorio");
        } else {
            if (controllaLegami(creaType.getCreaTypeChoice().getElementoAut().getLegamiElementoAut(),
                new TimestampHash()))
                validaLegami(creaType.getCreaTypeChoice().getElementoAut(), null);
            else
                throw new EccezioneSbnDiagnostico(3034, "legami non validati");
        }
        return resultTableDao;
    }

    private void validaLegami(ElementAutType elementAutType, String mid_modifica) throws IllegalArgumentException, InvocationTargetException, Exception {
        int size = elementAutType.getLegamiElementoAutCount();
        int i = 0;
        int j;
        ArrivoLegame[] arrivoLegame;
        int sizeArrivoLegame;
        LegamiType[] legamiType;
        legamiType = elementAutType.getLegamiElementoAut();
        for (i = 0; i < size; i++) {
            arrivoLegame = legamiType[i].getArrivoLegame();
            sizeArrivoLegame = legamiType[i].getArrivoLegameCount();
            for (j = 0; j < sizeArrivoLegame; j++) {
                if (arrivoLegame[j].getLegameElementoAut().getTipoAuthority().toString().equals("RE")) {
                    RepertorioMarca repMar = new RepertorioMarca();
                    if (!repMar.validaPerCreaLegame(legamiType[i], arrivoLegame[j].getLegameElementoAut(),mid_modifica)) {
                        throw new EccezioneSbnDiagnostico(3029, "legame repertorio errato");
                    }
                } else if (arrivoLegame[j].getLegameElementoAut().getTipoAuthority().toString().equals("AU")) {
                    AutoreMarca autMar = new AutoreMarca();
                    if (legamiType[i].getTipoOperazione().getType() == SbnTipoOperazione.CREA_TYPE) {
                        if (autMar.esisteLegame(legamiType[i], arrivoLegame[j].getLegameElementoAut())) {
                            throw new EccezioneSbnDiagnostico(3030, "legame autore errato");
                        }
                    } else {
                        if (!autMar.esisteLegame(legamiType[i], arrivoLegame[j].getLegameElementoAut())) {
                            throw new EccezioneSbnDiagnostico(3029, "legame autore errato");
                        }
                    }
                } else
                    throw new EccezioneSbnDiagnostico(3029, "Legame inesistente");
            }
        }

    }

    /**
     * metodo di validazione per operazione di modifica marca:
     * . verificaEsistenzaID: se non trovato ritorna diagnostico 'Marca inesistente'
     * . verificaVersioneMarca: se il risultato è negativo ritorna il diagnostico '
     * Versione non aggiornata'
     * . verificaLivelloModifica: se il risultato è falso ritorna il diagnostico al
     * client.
     * se tipoControllo <> 'Conferma'
     * - verifica EsistenzaPerNome: se trovato/i ritorna la lista  sintetica delle
     * marche trovate al client
     * se non esistono marche simili ritorna ok al chiamante
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List validaPerModifica(
        ModificaType modificaType,
        String tipoOrd,
        TimestampHash timeHash,
        String user,
        String livelloAut,
		boolean modificato,
        boolean _cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        List marcaTableDao = new ArrayList();
        Tb_marca tb_marca = null;
        String id, tipoControllo;
        tipoControllo = null;
        _datiElementoAut = modificaType.getElementoAut().getDatiElementoAut();
        id = modificaType.getElementoAut().getDatiElementoAut().getT001();
        TableDao tavola = cercaMarcaPerID(id);
        marcaTableDao = tavola.getElencoRisultati();

        if (modificaType.getTipoControllo() != null)
            tipoControllo = modificaType.getTipoControllo().toString();
        if (tipoControllo == null)
            tipoControllo = SbnSimile.SIMILE.toString();
        if (marcaTableDao.size() != 0)
            tb_marca = (Tb_marca) marcaTableDao.get(0);
        if (tb_marca != null) {
            if (_datiElementoAut.getT005() != null) {
                if (!verificaVersioneMarca(tb_marca)) {
                    throw new EccezioneSbnDiagnostico(3009, "versione non aggiornata");
                } else {
                    if (!verificaLivello(livelloAut, tb_marca,_cattura)) {
                        throw new EccezioneSbnDiagnostico(
                            3006,
                            "utente non ha sufficiente livello di autorità");
                    } else {
                        if (modificaType.getElementoAut().getLegamiElementoAut().length > 0) {
                            if (controllaLegami(modificaType.getElementoAut().getLegamiElementoAut(),
                                new TimestampHash()))
                                validaLegami(modificaType.getElementoAut(), tb_marca.getMID());
                            else
                                throw new EccezioneSbnDiagnostico(3034, "legami non validati");
                        }
                    }
                }
            } else {
                throw new EccezioneSbnDiagnostico(3041, "T005 mancante!");
            }
        } else {
            throw new EccezioneSbnDiagnostico(3013, "marca inesistente");
        }
        // 19.7.2005  deve controllare le parolo solo se sono in modifica dati
        if (modificato) {
	        if (((MarcaType) _datiElementoAut).getT921() == null)
	            throw new EccezioneSbnDiagnostico(3019, "Almeno una parola obbligatoria");
	        if (((MarcaType) _datiElementoAut).getT921().getB_921() != null) {
	            int size = ((MarcaType) _datiElementoAut).getT921().getB_921().length;
	            if (size == 0)
	                throw new EccezioneSbnDiagnostico(3019, "Almeno una parola obbligatoria");
	            if (size > 5)
	                throw new EccezioneSbnDiagnostico(3021, "Massimo 5 parole");
	            String [] paroleArray = ((MarcaType) _datiElementoAut).getT921().getB_921();
	            for (int i=0;i<paroleArray.length;i++) {
	                if (paroleArray[i].length()>10) {
	                    throw new EccezioneSbnDiagnostico(3266, "Max 10 caratteri per parola");
	                }
	            }
	        }
        }
        controllaParametriUtente(user, tb_marca.getCD_LIVELLO(),_cattura);
        return marcaTableDao;
    }

    /**
     * metodo che controlla il vincolo di versione in modifica:
     * T005 deve essere uguale a ts_var
     * NB: questo metodo è comune, può essere spostato in Factoring
     */
    public boolean verificaVersioneMarca(Tb_marca tb_marca) {
        boolean verificato = false;
        if (tb_marca.getTS_VAR().toString() != _datiElementoAut.getT005())
            verificato = true;
        return verificato;
    }

    /**
     * metodo che verifica se l'utente ha sufficente livello di autorità per
     * effettuare l'operazione di modifica richiesta.
     * decodifica livelloAut in tb_codici
     * se livelloAut > cd_livello letto sul db predispone in diagnostico: 'Livello di
     * autorità utente non consente l'operazione'.
     * NB: questo metodo è comune, può essere spostato in Factoring
     */
    public boolean verificaLivello(String livelloAut, Tb_marca tb_marca,boolean _cattura) throws EccezioneSbnDiagnostico {
        boolean verificato = false;
        //		livelloAut=_datiElementoAut.getLivelloAut().toString();
        if(_cattura)
            return true;
        if (Integer.parseInt(livelloAut) < Integer.parseInt(tb_marca.getCD_LIVELLO())) {
        	throw new EccezioneSbnDiagnostico(3010,"Livello di autorità in base dati superiore a quello comunicato");
        } else
            verificato = true;
        return verificato;
    }

    /**
     * Method controllaLegami
     * controlla che fra i legami ce ne sia almeno uno con tipoAuthority = "RE".
     * controlla che  sia i legami con tipoAuthority = "RE" e "AU" abbiano un idArrivo valido
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private boolean controllaLegami(LegamiType[] legamiType, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        SbnFormaNome tipoForma = null;
        boolean validato = false;
        String tp_forma;
        ArrivoLegame[] arrivoLegame;
        int sizeArrivoLegame;
        int size = legamiType.length;
        int i = 0;
        int j;
        boolean esito = true;
        while ((i < size) && (esito == true)) {
            arrivoLegame = legamiType[i].getArrivoLegame();
            j = 0;
            while ((j < legamiType[i].getArrivoLegameCount()) && (esito == true)) {
                if (arrivoLegame[j].getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.RE_TYPE) {
                    Repertorio authority = new Repertorio();
                    Tb_repertorio tb_repertorio;
                    tb_repertorio =
                        authority.cercaRepertorioPerCdSig(
                            arrivoLegame[j].getLegameElementoAut().getIdArrivo());
                    if (tb_repertorio == null) {
                        esito = false;
                    } else {
                    timeHash.putTimestamp(
                        "Tb_repertorio",
                        Long.toString(tb_repertorio.getID_REPERTORIO()) ,
                        new SbnDatavar(( tb_repertorio.getTS_VAR())));
                        _trovatoRepertorio = true;
                    }
                } else if (arrivoLegame[j].getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
                    Autore authority = new Autore();
                    Tb_autore tb_autore = null;
                    TableDao tavola =
                        authority.cercaAutorePerID(arrivoLegame[j].getLegameElementoAut().getIdArrivo());
                    if (tavola.getElencoRisultati().size() > 0) {
                        tb_autore = (Tb_autore) tavola.getElencoRisultati().get(0);
                        timeHash.putTimestamp("Tb_autore", tb_autore.getVID(), new SbnDatavar( tb_autore.getTS_VAR()));
                    }

                    if (tb_autore == null)
                        esito = false;
                }
                j++;
            }
            i++;
        }
        if ((trovatoRepertorio == false) && (size > 0))
            throw new EccezioneSbnDiagnostico(3031, "non e' stato trovato nessun legame con il repertorio");
        return esito;
    }

    private TableDao verificaEsistenzaPerParole() {
        return null;
    }

    /**
     * Method validaPerCancella.
     * controllo di esistenza per identificativo,
     * controllo che non esistano legami con titoli (tr_tit_mar), altrimenti segnalo
     * diagnostico 'Esistono legami a titoli'
     * se non ok ritorna il msg response di diagnostica (analogo a creazione)
     * @param _idCancellazione
     * @param string
     * @return Tb_marca
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public Tb_marca validaPerCancella(String _idCancellazione, String utente, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_marca marcaTrovata = null;
        String polo;
        polo = utente.substring(0, 3);
        TableDao tavola = cercaMarcaPerID(_idCancellazione);
        if (tavola.getElencoRisultati().size() > 0)
            marcaTrovata = (Tb_marca) tavola.getElencoRisultati().get(0);

        if (marcaTrovata == null)
            throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
        timeHash.putTimestamp("Tb_marca", marcaTrovata.getMID(), new SbnDatavar( marcaTrovata.getTS_VAR()));
        controllaParametriUtente(utente, marcaTrovata.getCD_LIVELLO(),false);
        TitoloMarca titoloMarca = new TitoloMarca();
        tavola = titoloMarca.cercaTitoloMarca(_idCancellazione);
        List v=  tavola.getElencoRisultati();

        if (v.size() > 0)
            throw new EccezioneSbnDiagnostico(3091, "Esistono legami con i titoli");
        AutoreMarca autMar = new AutoreMarca();
        v = autMar.cercaLegamiMarca(_idCancellazione);
        if (v.size() > 0)
            throw new EccezioneSbnDiagnostico(3131, "Esistono legami con autori");
        // almaviva LA LOCALIZZAZIONE PER MARCA E' STATA ELIMINATA IN QUANTO UNA MARCA IN POLO E'
		// AUTOMATICAMENTE LOCALIZZATA QUINDI SIA IN FASE DI INSERIMENTO CHE MODIFICA NON EFFETTUO NESSUN
		// TIPO DI OPERAZIONE SULLA TR_MAR_BIB CHE NON HA PIU' MOTIVO DI ESISTERE
		// VERIFICARE ALTRE EVENTUALI CHIAMATE A QUESTA TABELLA
		// INDICAZIONI DI ROSSANA 03/04/2007
//        MarcaBiblioteca marcaBiblioteca = new MarcaBiblioteca();
//        if (ValidatorAdminNoCache.getInstance().isPolo(polo)
//            && marcaBiblioteca.verificaLocalizzazioniTr_mar_bib(_idCancellazione, polo))
//            throw new EccezioneSbnDiagnostico(
//                3092,
//                "Marca localizzata in altri poli, cancellazione impossibile");

        return marcaTrovata;
    }

    /**
     * Method validaPerFonde.
     * @param idPartenza
     * @param idArrivo
     * @throws InfrastructureException
     */
    public void validaPerFonde(String utente, String idPartenza, String idArrivo, TimestampHash timeHash)
        throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        Tb_marca marcaTrovata = null;
        TableDao tavola = cercaMarcaPerID(idPartenza);
        List vettoreAppoggio;
        vettoreAppoggio = tavola.getElencoRisultati();
        if (vettoreAppoggio.size() > 0)
            marcaTrovata = (Tb_marca) vettoreAppoggio.get(0);

        if (marcaTrovata == null)
            throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
        timeHash.putTimestamp("Tb_marca", marcaTrovata.getMID(), new SbnDatavar( marcaTrovata.getTS_VAR()));
        controllaParametriUtente(utente, marcaTrovata.getCD_LIVELLO(), false);
        marcaTrovata = new Tb_marca();
        tavola = cercaMarcaPerID(idArrivo);
        vettoreAppoggio = tavola.getElencoRisultati();

        marcaTrovata = (Tb_marca) vettoreAppoggio.get(0);
        if (marcaTrovata != null)
            timeHash.putTimestamp("Tb_marca", marcaTrovata.getMID(), new SbnDatavar( marcaTrovata.getTS_VAR()));

        if (vettoreAppoggio.size() > 0)
            marcaTrovata = (Tb_marca) vettoreAppoggio.get(0);
        if (marcaTrovata == null)
            throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
        timeHash.putTimestamp("Tb_marca", marcaTrovata.getMID(), new SbnDatavar( marcaTrovata.getTS_VAR()));
    }

    private void controllaParametriUtente(String utente, String livelloAut,boolean _cattura) throws EccezioneSbnDiagnostico {
        Tbf_par_auth par = ValidatorProfiler.getInstance().getParametriUtentePerAuthority(utente, "MA");
        if(!_cattura){
            if (par == null)
                throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");
            String livelloUtente = par.getCd_livello();
            if (par.getTp_abil_auth()!='S')
                throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");
            if (Integer.parseInt(livelloAut) > Integer.parseInt(livelloUtente))
                throw new EccezioneSbnDiagnostico(3007, "Livello di autorità utente non consente l'operazione");
        }
    }

    /**
     * Method validaPerCancellaLegame.
     * questo metodo si preoccupa solamente di controllare che il legame sia esistente (o non cancellato)
     *
     * @param legameElementoAutType
     * @param idPartenza
     * @param timeHash
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void validaPerCancellaLegame(
        LegameElementoAutType legameElementoAutType,
        String idPartenza,
        TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        List vettoreLegami;
        if (legameElementoAutType.getTipoAuthority().toString() .equals("RE")) {
            Tr_rep_mar tr_rep_mar = new Tr_rep_mar();
            tr_rep_mar.setMID(idPartenza);
            tr_rep_mar.setPROGR_REPERTORIO(legameElementoAutType.getCitazione());

            Tb_repertorio tb_repertorio;
            Repertorio authority = new Repertorio();
            tb_repertorio = authority.cercaRepertorioPerCdSig(legameElementoAutType.getIdArrivo());
            tr_rep_mar.setID_REPERTORIO(tb_repertorio.getID_REPERTORIO());
            Tr_rep_marResult tavola = new Tr_rep_marResult(tr_rep_mar);


            tavola.valorizzaElencoRisultati(tavola.selectPerKey(tr_rep_mar.leggiAllParametro()));
            Tr_rep_mar elemento = null;
            vettoreLegami = tavola.getElencoRisultati();

            if (vettoreLegami.size() > 0) {
                elemento = (Tr_rep_mar) vettoreLegami.get(0);
                timeHash.putTimestamp(
                    "Tr_rep_mar",
                    elemento.getID_REPERTORIO() + elemento.getMID() + elemento.getPROGR_REPERTORIO(),
                    new SbnDatavar( elemento.getTS_VAR()));
            } else
                throw new EccezioneSbnDiagnostico(3029, "il legame da cancellare inesistente");
        } else if (legameElementoAutType.getTipoAuthority().toString() .equals("AU")) {
            Tr_aut_mar tr_aut_mar = new Tr_aut_mar();
            tr_aut_mar.setMID(idPartenza);
            tr_aut_mar.setVID(legameElementoAutType.getIdArrivo());
            Tr_aut_marResult tavola = new Tr_aut_marResult(tr_aut_mar);


            tavola.valorizzaElencoRisultati(tavola.selectPerKey(tr_aut_mar.leggiAllParametro()));
            vettoreLegami = tavola.getElencoRisultati();

            if (vettoreLegami.size() > 0) {
                Tr_aut_mar elemento = (Tr_aut_mar) vettoreLegami.get(0);
                timeHash.putTimestamp(
                    "Tr_aut_mar",
                    elemento.getVID() + elemento.getMID(),
                    new SbnDatavar(( elemento.getTS_VAR())));
            } else
                throw new EccezioneSbnDiagnostico(3029, "il legame da cancellare inesistente");
        }
    }

    public boolean verificaMid(String mid, String user) {
        boolean b = false;
        if (mid.length() > 10) {
            return false;
        }
        if (!mid.startsWith(SIGLA_INDICE)) {
            // Ometto il controllo sul POLO che invia la richiesta el il polo contenuto
            // nel ID dell'oggetto
            // Si ipotizza vedi (almaviva2 ) che questo controllo non serva in polo in quanto
            // in fase di cattura devo importare anche id diversi dal polo stesso
            // VERIFICARE SE QUESTO CONTROLLO EFFETTIVAMENTE NON SERVA

//            if (user != null && !user.substring(0,3).equals(mid.substring(0, 3)))
//                return false;

//        	 LA CHAIMATA AL CONTROLLO E' STATO TOLTO SU INDICAZIONE DI almaviva2 12/12/2007
//            if (Decodificatore.getCd_tabella("POLO", mid.substring(0, 3)) == null)
//                return false;
        }
        char[] c_vid = mid.toCharArray();
        if (c_vid[3]=='M') {
            boolean c = true;
            for (int i = 4; i < c_vid.length && b; i++) {
                c = Character.isDigit(c_vid[i]);
            }
            b = c;
        }
        return b;
    }

}
