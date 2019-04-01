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

import it.finsiel.gateway.intf.KeySoggetto;
import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.common.tavole.Tb_soggettoCommonDao;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_descrittore_desResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.ChiaviSoggetto;
import it.finsiel.sbn.polo.factoring.base.FormatoSoggetto;
import it.finsiel.sbn.polo.factoring.util.CountDesSog;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ElencoParole;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Descrittore;
import it.finsiel.sbn.polo.oggetti.DescrittoreDescrittore;
import it.finsiel.sbn.polo.oggetti.Soggetto;
import it.finsiel.sbn.polo.oggetti.SoggettoTitolo;
import it.finsiel.sbn.polo.oggetti.estensione.SoggettoValida;
import it.finsiel.sbn.polo.orm.Tb_descrittore;
import it.finsiel.sbn.polo.orm.Tb_soggetto;
import it.finsiel.sbn.polo.orm.Tr_des_des;
import it.finsiel.sbn.polo.orm.viste.Vl_descrittore_des;
import it.finsiel.sbn.polo.orm.viste.Vl_soggetto_des;
import it.finsiel.sbn.polo.orm.viste.Vl_soggetto_tit;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaSoggettoDescrittoreClassiReperType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CountDesSogType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRangeDate;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SogType;
import it.iccu.sbn.ejb.model.unimarcmodel.SoggettoType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.PosizionePosType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



/**
 * Contiene le funzionalità di ricerca per l'entità Soggetto
 * restituisce la lista sintetica o analitica
 *
 * Possibili parametri di RICERCA:
 * Identificativo: T001
 * parole del soggetto: paroleAut
 * soggetto: esatto: stringa esatta
 * soggetto parte iniziale: stringaLike
 *
 * FILTRI di ricerca:
 * livello di autorità: tipoAuthority
 * intervallo di data di aggiornamento: T005_Range
 * codice soggettario
 * entità collegate: titolo o descrittore (ArrivoLegame)
 *
 * Parametrizzazioni di OUTPUT:
 * tipoOrd: ordinamento richiesto, può essere su identificativo , sulla
 * descrizione, sul timestamp + identificativo
 * tipoOut: analitico o sintetico
 *
 * Tabelle coinvolte:
 * - Tb_soggetto,tr_sog_des,Tb_descrittore, Tr_des_des
 * si usano le viste: descrittori di soggetto, soggetto di descrittore
 *
 * OggettiCoinvolti:
 * - Soggetto, SoggettoDescrittore, Descrittore, DescrittoreDescrittore
 * Passi da eseguire:
 * esegue la ricerca secondo i parametri ricevuti;
 * prepara l'output secondo le indicazioni ricevute
 * (in analitica sono compresi i descrittori legati al soggetto e i rinvii tra
 * descrittori)
 * se non ok ritorna il msg response di diagnostica
 *
 * @author
 * @version 13/01/2003
 */
public class CercaSoggetto extends CercaElementoAutFactoring {

    private CercaElementoAutType _elementoAut;
    private CercaDatiAutType _datiElementoAut;
    private CercaSoggettoDescrittoreClassiReperType _cercaSoggetto;
    private String stringaLike;
    private String stringaEsatta;
    private String _T001;
    private SbnRangeDate _T005_Range;
    private String _livelloAutA;
    private String _livelloAutDA;
    private String _c2_250 = null;
    private String[] paroleAut = null;
	private String[] paroleSoggettoPerDescrittori = null;
	private String[] didSoggettoPerDescrittori = null;

	//almaviva5_20091028 Se provengo da una ricerca per arrivoLegameDoc
	//sto cercando per soggettazione. Attivo la query per flag gestione
	private String idArrivoSoggettazione = null;
	private boolean filtroGestione = false;
	private boolean filtroUtilizzati = false;

    private Tb_soggetto _tb_soggetto = new Tb_soggetto();
    private boolean ricercaPuntuale = false;
    private boolean ricercaSoggettiPerDescrittori = false;
    private boolean ricercaDidMultipli= false;
    private boolean ricercaSoggettoPerDescrittorePerPosizione= false;

    private List _TableDao_response = new ArrayList();
    private boolean ricercaUnivoca;
    private ArrivoLegame arrivoLegame = null;
    private SbnTipoOrd sbnOrd;
    private PosizionePosType posizione;
    private boolean legatoTitolo;

    private SbnEdizioneSoggettario _sog_edizione;


    public CercaSoggetto(SBNMarc input_root_object) {
        super(input_root_object);
        CercaType _cerca;
        StringaCercaType stringaCerca;
        _cerca = input_root_object.getSbnMessage().getSbnRequest().getCerca();
        _elementoAut = _cerca.getCercaElementoAut();
        _datiElementoAut = _elementoAut.getCercaDatiAut();

        sbnOrd = _cerca.getTipoOrd();
        if (_datiElementoAut instanceof CercaSoggettoDescrittoreClassiReperType)
            _cercaSoggetto = (CercaSoggettoDescrittoreClassiReperType) _datiElementoAut;

        //almaviva5_20130403 #5294
        filtroGestione = true;
        if (_datiElementoAut.getGestito() != null)
        	filtroGestione = _datiElementoAut.getGestito().getType() == SbnIndicatore.S_TYPE;

		filtroUtilizzati = _datiElementoAut.getUtilizzato() != null
			&& _datiElementoAut.getUtilizzato().getType() == SbnIndicatore.S_TYPE;

        if (_datiElementoAut.getCanaliCercaDatiAut() != null) {
            _T001 = _datiElementoAut.getCanaliCercaDatiAut().getT001();
            stringaCerca = _datiElementoAut.getCanaliCercaDatiAut().getStringaCerca();
            if (stringaCerca != null) {
                if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null)
                    stringaLike = stringaCerca.getStringaCercaTypeChoice().getStringaLike();
                if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null)
                    stringaEsatta = stringaCerca.getStringaCercaTypeChoice().getStringaEsatta();
            }
        }
        _T005_Range = _datiElementoAut.getT005_Range();
        if (_datiElementoAut.getLivelloAut_A() != null)
            _livelloAutA = _datiElementoAut.getLivelloAut_A().toString();
        if (_datiElementoAut.getLivelloAut_Da() != null)
            _livelloAutDA = _datiElementoAut.getLivelloAut_Da().toString();
        if (_cercaSoggetto != null) {
			if(_cercaSoggetto.getParoleAut() != null){
	            paroleAut = _cercaSoggetto.getParoleAut();
	            _c2_250 = _cercaSoggetto.getC2_250();
			}
			if(_cercaSoggetto.getParoleSoggettoPerDescrittori() != null){
				paroleSoggettoPerDescrittori = _cercaSoggetto.getParoleSoggettoPerDescrittori();
			}
			if(_cercaSoggetto.getDidSoggettoPerDescrittori() != null){
				didSoggettoPerDescrittori = _cercaSoggetto.getDidSoggettoPerDescrittori();
			}

			//almaviva5_20111122 evolutive CFI
			_sog_edizione =_cercaSoggetto.getEdizione();

        }
        arrivoLegame = _elementoAut.getArrivoLegame();
    }

    public void executeCerca() throws IllegalArgumentException, InvocationTargetException, Exception {

        if ((sbnOrd.getType() == SbnTipoOrd.VALUE_3.getType()) || (sbnOrd.getType() == SbnTipoOrd.VALUE_4.getType()))
            throw new EccezioneSbnDiagnostico(1231, "errore nell'ordinamento");

        verificaAbilitazioni();

        //almaviva5_20091030 nessun vincolo in ricerca
        /*
		if (_c2_250 != null)	{
			SoggettoValida soggettoValida = new SoggettoValida();
			soggettoValida.controllaVettoreParametriSemantici(_c2_250, this.cd_utente);
		}
		*/

        int counter = 0;
        if (_T001 != null)
            counter++;
        if ((stringaLike != null) || (stringaEsatta != null))
            counter++;
        if (paroleAut != null && paroleAut.length > 0)
            counter++;
        if (paroleSoggettoPerDescrittori != null && paroleSoggettoPerDescrittori.length > 0)
            counter++;
        if (didSoggettoPerDescrittori != null && didSoggettoPerDescrittori.length > 0)
            counter++;

        if (arrivoLegame != null)
            counter++;

        if (counter != 1)
            throw new EccezioneSbnDiagnostico(3039, "comunicare uno e un solo canale di ricerca");
        if (_T001 != null)
            cercaSoggettoPerId();
        else if (stringaLike != null || stringaEsatta != null)
            cercaSoggettoPerStringaCerca();
        else if (paroleAut != null && paroleAut.length > 0)
            cercaSoggettoPerParole();
        else if (paroleSoggettoPerDescrittori != null && paroleSoggettoPerDescrittori.length > 0)
            cercaSoggettoPerParoleDescrittori();
        else if (didSoggettoPerDescrittori != null && didSoggettoPerDescrittori.length > 0)
            cercaDidMultpli();

        else if (arrivoLegame != null)
            cercaSoggettoPerLegame();
    }

    private void cercaSoggettoPerParoleDescrittori() throws IllegalArgumentException, InvocationTargetException, Exception {
        Soggetto soggetto = new Soggetto();
        soggetto.valorizzaFiltri(_datiElementoAut);
        //almaviva5_20120323 evolutive CFI
        soggetto.settaParametro(TableDao.XXXcd_edizione_IN, _sog_edizione);
        List<String> v = new ArrayList<String>();
        if(paroleSoggettoPerDescrittori.length == 1){
        	//cercaDidMultpli();
        	throw new EccezioneSbnDiagnostico(6666, "Il numero dei descrittori deve essere compreso tra 2 e 4 ");
        }
        for (int i = 0; i < paroleSoggettoPerDescrittori.length; i++) {
            //_ e # devono diventare spazio
            if (paroleSoggettoPerDescrittori[i].length() > 0 && !ElencoParole.getInstance().contiene(paroleSoggettoPerDescrittori[i]))
                v.add(paroleSoggettoPerDescrittori[i]);
        }
		String[][] s = new String[v.size() + 1][6];
        // DOVE pos 1 (0)= Descrizione Forma Accettata
        // DOVE pos 2 (1)= Descrizione Forma Rinvio
        // DOVE pos 3 (2)= Totale Soggetti Legati
        // DOVE pos 4 (3)= Totale descrittori in des des
        // DOVE pos 5 (4)= Did Accettato
        // DOVE pos 6 (5)= Did Rinvio
		s = controllaDescrittoriPerFormaRinvio(paroleSoggettoPerDescrittori, s);
        paroleSoggettoPerDescrittori = new String[v.size()];
        int i = 0;
        String msg = "";
		int conta = 0;

        for (i = 0; i < v.size(); i++){
        	paroleSoggettoPerDescrittori[i] = v.get(i);
			if (s[i][1] != null) {
        		if(ValidazioneDati.equals(s[i][1], SBNMARC_DEFAULT_ID)){
        			// non setto nulla in quanto non esiste il descrittore
        		}else{
	        		conta = soggetto.contaSoggettoPerDescrittore(s[i][4], _sog_edizione);
	                s[i][2] =  String.valueOf(conta);
        		}
        	}
			else {
				if (ValidazioneDati.equals(s[i][1], SBNMARC_DEFAULT_ID)) {
        			// non setto nulla in quanto non esiste il descrittore
        		}else{
            		conta = soggetto.contaSoggettoPerDescrittore(s[i][4], _sog_edizione);
                    //s[i][0] = paroleSoggettoPerDescrittori[i].toString();
                    s[i][2] =  String.valueOf(conta);
        		}
        	}

       		msg += " " + paroleSoggettoPerDescrittori[i].toString();

        	log.debug("Trovati per descrittore=\"" + paroleSoggettoPerDescrittori[i].toString()+ "\"  sogg n°=" + conta);
        }
        String[] did_m = new String[4];
		did_m[0] = "";
		did_m[1] = "";
		did_m[2] = "";
		did_m[3] = "";
        for (int k = 0; k < paroleSoggettoPerDescrittori.length; k++) {
        	paroleSoggettoPerDescrittori[k] = s[k][0];
        	did_m[k]=s[k][4];
        }

        int contatot = soggetto.contaDidMultpli(did_m[0],did_m[1],did_m[2],did_m[3],paroleSoggettoPerDescrittori.length);

        //int contatot = contaSoggettoPerParole();
        log.debug("Trovati per descrittore=\"" + msg + "\"  sogg n°=" + contatot);
        s[v.size()][0] = msg;
        s[v.size()][2] =  String.valueOf(contatot);
        s[v.size()][4] =  "";
        s[v.size()][5] =  "";
        s[v.size()][3] =  "";


        CountDesSog al = null;
        //List al = new ArrayList(v.size());
		for (int j = 0; j <= v.size(); j++) {
        	al = new CountDesSog();
        	//al.setDes_accettato(s[j][0] + "provacountdesdes=" + s[j][3]);
        	al.setDes_accettato(s[j][0]);
            al.setDes_rinvio(s[j][1]);
            al.setSog_count(Integer.parseInt(s[j][2]));
            if(s[j][3] != "" )
            	al.setDes_Des_count(Integer.parseInt(s[j][3]));
            else
            	al.setDes_Des_count(0);
            al.setDid_accettato(s[j][4]);
            al.setDid_rinvio(s[j][5]);
            _TableDao_response.add(j,al);
        }
        //string[] strArray = arrList.ToArray(typeof(string)) as string[];
        //TableDao tableDao =(TableDao) s;

        ricercaSoggettiPerDescrittori = true;

    }

	private void cercaSoggettoPerLegame() throws IllegalArgumentException,
			InvocationTargetException, Exception {
		ArrivoLegame legame = _elementoAut.getArrivoLegame();
		if ((legame == null))
			return;

		if (legame.getLegameDoc() != null)
			// almaviva5_20091028
			idArrivoSoggettazione = cercaSoggettoPerTitolo();
		else
			if (legame.getLegameElementoAut().getTipoAuthority().getType() == SbnAuthority.DE_TYPE)
				cercaSoggettoPerDescrittore();
	}

    /**
     * Method cercaSoggettoPerDescrittore.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
	private void cercaSoggettoPerDescrittore() throws IllegalArgumentException,
			InvocationTargetException, Exception {
		if (arrivoLegame == null) {
			log.error("Nessun descrittore specificato nella ricerca");
			throw new EccezioneSbnDiagnostico(3041);
		}

		String idArrivo = arrivoLegame.getLegameElementoAut().getIdArrivo();
		// QUI DETERMINO SE LA RICERCA E PER POSIZIONE E LAGATO A TITOLO
		if (arrivoLegame.getLegameElementoAut().getPosizione() != null) {
			posizione = arrivoLegame.getLegameElementoAut().getPosizione().getPos();

			legatoTitolo = (arrivoLegame.getLegameElementoAut().getPosizione().getLegatoTitolo());

			String pos = "";
			switch (posizione.getType() ) {
			case PosizionePosType.VALUE_0_TYPE:
				pos = "";
				break;

			case PosizionePosType.VALUE_1_TYPE:
				pos = "S";
				break;

			case PosizionePosType.VALUE_2_TYPE:
				pos = "N";
				break;
			}

			List<Vl_soggetto_des> risultati = ricercaAggregataPerLegamiStorici(idArrivo, pos);

			int size = ValidazioneDati.size(risultati);
			if (size < 1)
				throw new EccezioneSbnDiagnostico(3001); // nessun elemento trovato

			Soggetto soggetto = new Soggetto();
			FormatoSoggetto formatoSoggetto = new FormatoSoggetto();
			int num_tit_coll = 0;
			for (Vl_soggetto_des sd : risultati) {
				// se cerco i titolo collegati verifico la loro presenza
				String cid = sd.getCID();
				if (legatoTitolo)
					num_tit_coll = formatoSoggetto.cercaNum_tit_coll_unique(cid);
				// se non cerco i titoli collegati setto il contatore di
				// controllo "num_tit_coll" a 1 ed effettuo cmq la ricerca
				else
					num_tit_coll = 1;

				if (num_tit_coll > 0)
					_TableDao_response.add(soggetto.cercaSoggettoPerId(cid));

			}
			tavola_response = null;
			ricercaSoggettoPerDescrittorePerPosizione = true;
		} else {
			// ESEGUO LA RICERCA CLASSICA
			Soggetto soggetto = new Soggetto();
			controllaNumeroRecord(soggetto.contaSoggettoPerDescrittore(idArrivo, _sog_edizione));
			tavola_response = soggetto.cercaSoggettoPerDescrittore(idArrivo, tipoOrd, _sog_edizione);
			List<Vl_soggetto_des> risultati = tavola_response.getElencoRisultati();

			if (ValidazioneDati.size(risultati) < 1)
				throw new EccezioneSbnDiagnostico(3001);	//nessun elemento trovato

			for (Vl_soggetto_des sd : risultati)
				_TableDao_response.add(soggetto.cercaSoggettoPerId(sd.getCID()));

		}

	}

	private List ricercaAggregataPerLegamiStorici(String idArrivo,
			String pos) throws EccezioneSbnDiagnostico,
			IllegalArgumentException, InvocationTargetException, Exception {

		Set<String> did_cache = new HashSet<String>();
		did_cache.add(idArrivo);

		Soggetto soggetto = new Soggetto();
		DescrittoreDescrittore dd = new DescrittoreDescrittore();

		//si prendono tutti i did con legami storici
		List<Tr_des_des> legamiStorici = dd.getLegamiStorici(idArrivo);
		for (Tr_des_des ls : legamiStorici) {
			did_cache.add(ls.getDID_BASE());
			did_cache.add(ls.getDID_COLL());
		}

		String[] didLegami = did_cache.toArray(new String[0]);

		controllaNumeroRecord(soggetto.contaSoggettoPerDescrittorePerPosizione(didLegami, pos, _sog_edizione));
		tavola_response = soggetto.cercaSoggettoPerDescrittorePerPosizione(didLegami, pos, tipoOrd, _sog_edizione);

		return tavola_response.getElencoRisultati();
	}

    /**
     * Method cercaSoggettoPerTitolo.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private String cercaSoggettoPerTitolo() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (arrivoLegame == null) {
            log.error("Nessun titolo specificato nella ricerca");
            throw new EccezioneSbnDiagnostico(3041);
        }
        String idArrivo = null;
        if (arrivoLegame.getLegameDoc() != null)
            idArrivo = arrivoLegame.getLegameDoc().getIdArrivo();
        Soggetto soggetto = new Soggetto();
        //almaviva5_20091030 filtro per soggettario gestito dall'utente (non solo visibile)
        if (filtroGestione)
        	soggetto.settaParametro(TableDao.XXXcercaPerFiltroUteSoggGestione, user_object);
        if (filtroUtilizzati)
        	soggetto.settaParametro(TableDao.XXXcercaPerFiltroSoggUtilizzatiInBib, user_object);
        //ANTONIO
        soggetto.valorizzaFiltri(_elementoAut.getCercaDatiAut());
        controllaNumeroRecord(soggetto.contaSoggettoPerTitolo(idArrivo));
        List tempTableDao = null;
        tempTableDao = soggetto.cercaSoggettoPerTitolo(idArrivo, tipoOrd);
        int size = tempTableDao.size();
		if (size == 0)
            throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
        for (int i = 0; i < size; i++)
            _TableDao_response.add(soggetto.cercaSoggettoPerId(((Vl_soggetto_tit) tempTableDao.get(i)).getCID()));

        return idArrivo;
    }

    private void cercaSoggettoPerId() throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        Soggetto soggetto = new Soggetto();
        //almaviva5_20120323 evolutive CFI
        soggetto.settaParametro(TableDao.XXXcd_edizione_IN, _sog_edizione);
        //almaviva5_20091117 solo soggettari visibili dalla biblioteca
        if (filtroGestione)
        	soggetto.settaParametro(TableDao.XXXcercaPerFiltroSoggInBib, user_object);
        if (filtroUtilizzati)
        	soggetto.settaParametro(TableDao.XXXcercaPerFiltroSoggUtilizzatiInBib, user_object);
        _tb_soggetto = soggetto.cercaSoggettoPerId(_T001);
        if (_tb_soggetto == null)
            throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
        ricercaPuntuale = true;
    }

    private void cercaSoggettoPerParole() throws IllegalArgumentException, InvocationTargetException, Exception {
        Soggetto soggetto = new Soggetto();
        //almaviva5_20120323 evolutive CFI
        soggetto.settaParametro(TableDao.XXXcd_edizione_IN, _sog_edizione);
        //almaviva5_20091117 solo soggettari visibili dalla biblioteca
        if (filtroGestione)
        	soggetto.settaParametro(TableDao.XXXcercaPerFiltroSoggInBib, user_object);
        if (filtroUtilizzati)
        	soggetto.settaParametro(TableDao.XXXcercaPerFiltroSoggUtilizzatiInBib, user_object);

        soggetto.valorizzaFiltri(_datiElementoAut);

        paroleAut = normalizzaParolePerRicerca(paroleAut, ElencoParole.getInstance());

        controllaNumeroRecord(soggetto.contaSoggettoPerParoleNome(paroleAut));
        tavola_response = soggetto.cercaSoggettoPerParoleNome(paroleAut, tipoOrd);

    }

    private int contaSoggettoPerParole() throws IllegalArgumentException, InvocationTargetException, Exception {
    	Soggetto soggetto = new Soggetto();
        //almaviva5_20120323 evolutive CFI
        soggetto.settaParametro(TableDao.XXXcd_edizione_IN, _sog_edizione);
        soggetto.valorizzaFiltri(_datiElementoAut);
        List v = new ArrayList();
        for (int i = 0; i < paroleSoggettoPerDescrittori.length; i++) {
            //_ e # devono diventare spazio
            if (paroleSoggettoPerDescrittori[i].length() > 0 && !ElencoParole.getInstance().contiene(paroleSoggettoPerDescrittori[i]))
                v.add(paroleSoggettoPerDescrittori[i]);
        }
        paroleSoggettoPerDescrittori = new String[v.size()];
        for (int i = 0; i < v.size(); i++)
        	paroleSoggettoPerDescrittori[i] = (String) v.get(i);
        	//paroleSoggettoPerDescrittori[i] = NormalizzaNomi.normalizzazioneGenerica((String) v.get(i));
        int ritorno =soggetto.contaSoggettoPerParoleNome(paroleSoggettoPerDescrittori);
        return ritorno;
    }

    private void cercaSoggettoPerStringaCerca() throws IllegalArgumentException, InvocationTargetException, Exception {
        Soggetto soggetto = new Soggetto();
        //almaviva5_20120323 evolutive CFI
        soggetto.settaParametro(TableDao.XXXcd_edizione_IN, _sog_edizione);
        //almaviva5_20091117 solo soggettari visibili dalla biblioteca
        if (filtroGestione)
        	soggetto.settaParametro(TableDao.XXXcercaPerFiltroSoggInBib, user_object);
        if (filtroUtilizzati)
        	soggetto.settaParametro(TableDao.XXXcercaPerFiltroSoggUtilizzatiInBib, user_object);

        soggetto.valorizzaFiltri(_datiElementoAut);
        if (stringaLike != null) {
        	//almaviva5_20111122 evolutive CFI
        	KeySoggetto key = ChiaviSoggetto.build(null, null, stringaLike);
			stringaLike = key.getKy_cles1_a();//NormalizzaNomi.normalizzaSoggetto(stringaLike);
			if (!ValidazioneDati.isFilled(stringaLike))
				throw new EccezioneSbnDiagnostico(3049); //dati incompleti

            controllaNumeroRecord(soggetto.contaSoggettoPerNomeLike(stringaLike));
            tavola_response = soggetto.cercaSoggettoPerNomeLike(stringaLike, tipoOrd);
        } else if (stringaEsatta != null) {
        	//almaviva5_20111122 evolutive CFI
        	KeySoggetto key = ChiaviSoggetto.build(null, null, stringaEsatta);
            stringaEsatta = key.getKy_cles1_a();//NormalizzaNomi.normalizzaSoggetto(stringaEsatta);
            if (!ValidazioneDati.isFilled(stringaEsatta))
				throw new EccezioneSbnDiagnostico(3049); //dati incompleti

            controllaNumeroRecord(soggetto.contaSoggettoPerNomeEsatto(stringaEsatta));
            tavola_response = soggetto.cercaSoggettoPerNomeEsatto(stringaEsatta, tipoOrd);
        }

    }


    /** Prepara la stringa di output in formato xml
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
	protected SBNMarc preparaOutput() throws IllegalArgumentException,
			InvocationTargetException, Exception {
		if ((tavola_response == null) && (_TableDao_response == null)) {
			log.error("Errore nel factoring ricorsivo: oggetto tavola_response nullo");
			throw new EccezioneSbnDiagnostico(201);
		}
		// almaviva5_20120827 #5065
		if (tavola_response == null) {
			tavola_response = new Tb_soggettoCommonDao();
			tavola_response.valorizzaElencoRisultati(_TableDao_response);
			numeroRecord = _TableDao_response.size();
		}
		// Deve utilizzare il numero di richieste che servono
		List response = tavola_response.getElencoRisultati(maxRighe);

		SBNMarc risultato = formattaOutput(response);
		rowCounter += response.size();
		return risultato;
	}

    private SBNMarc formattaOutput(List risultati)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		SBNMarc sbnmarc = new SBNMarc();
		SbnMessageType message = new SbnMessageType();
		SbnResponseType response = new SbnResponseType();
		SbnResultType result = new SbnResultType();
		SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
		SbnOutputType output = new SbnOutputType();
		sbnmarc.setSbnMessage(message);
		sbnmarc.setSbnUser(user_object);
		sbnmarc.setSchemaVersion(schemaVersion);
		message.setSbnResponse(response);
		response.setSbnResult(result);
		response.setSbnResponseTypeChoice(responseChoice);
		responseChoice.setSbnOutput(output);

		int totRighe = 0;
		int size = ValidazioneDati.size(risultati);
		if (size > 0)
			totRighe = size;

		FormatoSoggetto formatoSoggetto = new FormatoSoggetto();

		if ((totRighe > 0) && ricercaSoggettiPerDescrittori) {
			output.setTotRighe(size);
			CountDesSogType countDesSogType = new CountDesSogType();
			CountDesSog countDesSog = new CountDesSog();
			for (int i = 0; i < size; i++) {
				countDesSog = (CountDesSog) risultati.get(i);
				SogType sogType = new SogType();
				sogType.setDes_accettato(countDesSog.getDes_accettato());
				if (countDesSog.getDes_rinvio() != null) {
					if (countDesSog.getDes_rinvio() != "")
						sogType.setDes_rinvio(countDesSog.getDes_rinvio());
				}
				sogType.setSog_count(countDesSog.getSog_count());
				sogType.setDes_des_count(countDesSog.getDes_Des_count());
				// desType.setDid_accettato(countDesSog.getDid_accettato());
				sogType.setDid_accettato(countDesSog.getDid_accettato());

				if (countDesSog.getDid_rinvio() != null) {
					if (countDesSog.getDid_rinvio() != "")
						sogType.setDid_rinvio(countDesSog.getDid_rinvio());
				}
				countDesSogType.addSog(sogType);
			}
			// almaviva5_20111124 fix ricerca per did multipli
			output.setCountDesSog(countDesSogType);
			totRighe = 0;
			ricercaPuntuale = false;

			result.setEsito("0000"); // Esito positivo
			result.setTestoEsito("OK");
		}

		else if ((totRighe > 0) || ricercaPuntuale) {
			output.setMaxRighe(maxRighe);
			output.setTotRighe(numeroRecord);
			if (ricercaPuntuale)
				output.setTotRighe(1);
			// almaviva5_20120827 #5065
//			if (ricercaSoggettoPerDescrittorePerPosizione)
//				output.setTotRighe(totRighe);
			if (ricercaDidMultipli)
				output.setTotRighe(totRighe);

			output.setNumPrimo(rowCounter + 1);
			output.setTipoOrd(sbnOrd);
			output.setTipoOutput(tipoOut);

			SoggettoTitolo st = new SoggettoTitolo();

			if (!ricercaPuntuale) {
				for (int i = 0; i < totRighe; i++) {
					ElementAutType elemento = new ElementAutType();
					Tb_soggetto tb_soggetto = (Tb_soggetto) risultati.get(i);
					int num_tit_coll_bib = formatoSoggetto.cercaNum_tit_coll_bib(tb_soggetto.getCID(), user_object);
					int num_tit_coll = formatoSoggetto.cercaNum_tit_coll_unique(tb_soggetto.getCID());
			    	Descrittore d = new Descrittore();
			    	List dd = d.cercaDescrittorePerSoggetto(tb_soggetto.getCID());
					SoggettoType datiElemento = formatoSoggetto.formattaSoggetto(tb_soggetto, tipoOut, num_tit_coll_bib, num_tit_coll, dd);
					// almaviva5_20091028
					datiElemento.setMaxLivelloAutLegame(st.cercaMaxLivelloAutLegameSoggettario(tb_soggetto, idArrivoSoggettazione));
					// almaviva5_20120507 evolutive CFI
					datiElemento.setRank(st.getPosizioneLegame(tb_soggetto, idArrivoSoggettazione));
					elemento.setDatiElementoAut(datiElemento);
					if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) {
						LegamiType[] legamiType = formatoSoggetto.formattaSoggettoConLegamiDescrittore(tb_soggetto);
						if (legamiType != null)
							elemento.setLegamiElementoAut(legamiType);
					}

					output.addElementoAut(elemento);
				}
			} else {
				ElementAutType elemento = new ElementAutType();
				int num_tit_coll_bib = formatoSoggetto.cercaNum_tit_coll_bib(_tb_soggetto.getCID(), user_object);
				int num_tit_coll = formatoSoggetto.cercaNum_tit_coll_unique(_tb_soggetto.getCID());
		    	Descrittore d = new Descrittore();
		    	List dd = d.cercaDescrittorePerSoggetto(_tb_soggetto.getCID());
				SoggettoType datiElemento = formatoSoggetto.formattaSoggetto(_tb_soggetto, tipoOut, num_tit_coll_bib, num_tit_coll, dd);
				// almaviva5_20091028
				datiElemento.setMaxLivelloAutLegame(st.cercaMaxLivelloAutLegameSoggettario(_tb_soggetto, idArrivoSoggettazione));
				// almaviva5_20120507 evolutive CFI
				datiElemento.setRank(st.getPosizioneLegame(_tb_soggetto, idArrivoSoggettazione));
				elemento.setDatiElementoAut(datiElemento);

				if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType() ) {
					LegamiType[] legamiType = formatoSoggetto.formattaSoggettoConLegamiDescrittore(_tb_soggetto);
					if (legamiType != null)
						elemento.setLegamiElementoAut(legamiType);
				}
				output.addElementoAut(elemento);

			}
			result.setEsito("0000"); // Esito positivo
			result.setTestoEsito("OK");

		} else {
			result.setEsito("3001");
			// Esito non positivo: si potrebbe usare un'ecc.
			result.setTestoEsito("Nessun elemento trovato");
		}

		if (idLista != null)
			output.setIdLista(idLista);

		return sbnmarc;
	}

    private String[][] controllaDescrittoriPerFormaRinvio(String[] vettoreDescrittori, String[][] result)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		Descrittore descrittore = new Descrittore();
		for (int i = 0; i < vettoreDescrittori.length; i++) {
			if (ValidazioneDati.isFilled(vettoreDescrittori[i]) ) {
				// ANTONIO aggiunta gestione del codice soggettario
				TableDao dao = descrittore.cercaDescrittorePerNomeEsatto(ChiaviSoggetto.normalizzaDescrittore(vettoreDescrittori[i]),
								null, Decodificatore.getCd_tabella("Tb_soggetto", "cd_soggettario",	_c2_250.toUpperCase().trim()));
				List<Tb_descrittore> risultati = dao.getElencoRisultati();
				if (!ValidazioneDati.isFilled(risultati) ) {
					// SE NON TROVO NIENTE AZZERARE LA STRUTTURA
					result[i][0] = vettoreDescrittori[i];
					result[i][1] = "";
					result[i][3] = "0";
					result[i][4] = SBNMARC_DEFAULT_ID;
					result[i][5] = "";

				} else {
					Tb_descrittore tb_descrittore = ValidazioneDati.first(risultati);
					//almaviva5_20120511 evolutive CFI
					if (_sog_edizione != null) {
						String[] edizioni = SoggettoValida.getEdizioniRicerca(_sog_edizione);
						if (!ValidazioneDati.in(tb_descrittore.getCD_EDIZIONE(), edizioni))
							throw new EccezioneSbnDiagnostico(3001);
					}

					String forma = tb_descrittore.getTP_FORMA_DES();
					String did = tb_descrittore.getDID();
					result[i][0] = tb_descrittore.getDS_DESCRITTORE();
					if (SbnFormaNome.valueOf(forma).getType() == SbnFormaNome.R_TYPE ) {
						Vl_descrittore_des vl_descrittore_des = new Vl_descrittore_des();
						vl_descrittore_des.setDID_1(did);
						Vl_descrittore_desResult tavola = new Vl_descrittore_desResult(vl_descrittore_des);
						tavola.executeCustom("selectDescrittorePerRinvii");
						List v = tavola.getElencoRisultati();

						if (tavola.getElencoRisultati().size() != 0) {
							result[i][0] = ((Tb_descrittore) ((tavola
									.getElencoRisultati()).get(0)))
									.getDS_DESCRITTORE();
							result[i][1] = vettoreDescrittori[i];
							result[i][3] = String.valueOf(tavola
									.getElencoRisultati().size());
							result[i][4] = ((Vl_descrittore_des) ((tavola
									.getElencoRisultati()).get(0))).getDID();
							result[i][5] = did;
						}

					} else {
						Vl_descrittore_des vl_descrittore_des = new Vl_descrittore_des();
						vl_descrittore_des.setDID_1(did);
						Vl_descrittore_desResult tavola = new Vl_descrittore_desResult(vl_descrittore_des);
						tavola.executeCustom("selectDescrittorePerRinvii");
						List v = tavola.getElencoRisultati();
						tavola.getElencoRisultati().size();
						result[i][4] = did;
						result[i][5] = "";
						result[i][3] = String.valueOf(tavola.getElencoRisultati().size());

					}
				}
			}
		}
		return result;
	}

    private void cercaDidMultpli() throws IllegalArgumentException, InvocationTargetException, Exception {
        Soggetto soggetto = new Soggetto();
        //almaviva5_20120323 evolutive CFI
        soggetto.settaParametro(TableDao.XXXcd_edizione_IN, _sog_edizione);
        //almaviva5_20091117 solo soggettari visibili dalla biblioteca
        if (filtroGestione)
        	soggetto.settaParametro(TableDao.XXXcercaPerFiltroSoggInBib, user_object);
        if (filtroUtilizzati)
        	soggetto.settaParametro(TableDao.XXXcercaPerFiltroSoggUtilizzatiInBib, user_object);

        soggetto.valorizzaFiltri(_datiElementoAut);
        List v = new ArrayList();

        if(didSoggettoPerDescrittori.length == 1){
        	throw new EccezioneSbnDiagnostico(6667, "Il numero dei descrittori deve essere compreso tra 2 e 4 ");
        }
        for (int i = 0; i < didSoggettoPerDescrittori.length; i++) {
            //_ e # devono diventare spazio
            if ((didSoggettoPerDescrittori[i].length() > 0 && didSoggettoPerDescrittori[i].length() <= 10)){
            	didSoggettoPerDescrittori[i] = didSoggettoPerDescrittori[i].trim();
            }else{
            	throw new EccezioneSbnDiagnostico(6668, "Did comunicato errato " + didSoggettoPerDescrittori[i]);
            }
        }
        String[] did_m = new String[4];
        did_m[0]="";
        did_m[1]="";
        did_m[2]="";
        did_m[3]="";

        // riempo comunque la struttura
        for (int k = 0; k < didSoggettoPerDescrittori.length; k++) {
        	did_m[k] = didSoggettoPerDescrittori[k];
        }

        tavola_response = soggetto.cercaDidMultpli(did_m[0],
        		did_m[1],
        		did_m[2],
        		did_m[3],
        		didSoggettoPerDescrittori.length,
			   tipoOrd);
        List tempTableDao = null;
        tempTableDao = tavola_response.getElencoRisultati();

        if (tempTableDao.size() == 0)
            throw new EccezioneSbnDiagnostico(3001, "nessun elemento trovato");
        for (int i = 0; i < tempTableDao.size(); i++){
        	String appo = ((Vl_soggetto_des) tempTableDao.get(i)).getCID();
            _TableDao_response.add(soggetto.cercaSoggettoPerId(appo));
        }
        tavola_response = null;



        ricercaDidMultipli = true;

    }

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().CERCA_SOGGETTI_1253;
    }

}
