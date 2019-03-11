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

import it.finsiel.gateway.intf.KeySoggetto;
import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_soggettoResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_sog_bibResult;
import it.finsiel.sbn.polo.dao.vo.MaxLivelloLegameSog;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.ChiaviSoggetto;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Descrittore;
import it.finsiel.sbn.polo.oggetti.Soggetto;
import it.finsiel.sbn.polo.oggetti.SoggettoDescrittore;
import it.finsiel.sbn.polo.oggetti.SoggettoTitolo;
import it.finsiel.sbn.polo.orm.Tb_descrittore;
import it.finsiel.sbn.polo.orm.Tb_soggetto;
import it.finsiel.sbn.polo.orm.Tr_sog_des;
import it.finsiel.sbn.polo.orm.Tr_soggettari_biblioteche;
import it.finsiel.sbn.polo.orm.Tr_tit_sog_bib;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.iccu.sbn.ejb.model.unimarcmodel.A250;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Valida le informazioni relative a un Soggetto
 * Input= DatiElementoAut con tipoAuthority = 'SO'
 *
 * Operazioni di validazione:
 * controllo esistenza con identificativo: T001 = CID
 * controllo versione: T005 = ts_var
 * controllo abilitazione su livello utente/cd_livello
 * Estrae i descrittori dal soggetto (divide le stringhe separate da ' - ')
 * verifica l'esistenza dei descrittori per stringa esatta.
 * Se esistono devono avere forma=A
 *
 * @author
 *
 */

public class SoggettoValida extends Soggetto {

	private static final long serialVersionUID = 169220589823776082L;

	protected static ValidatorProfiler validator = ValidatorProfiler.getInstance();


	private String 			_livelloAut;
	private static Logger log = Logger.getLogger("iccu.sbnmarcserver.SoggettoValida");

	public SoggettoValida( String livello){
        super();
		_livelloAut = livello;
	}

	public SoggettoValida(){
		super();
		_livelloAut = null;
	}


    /**
     * si effettua la select tb_soggetto per
     * uguaglianza su ky_cles1_s e ky_cles2_s e ds_soggetto
     * se esiste si ritorna il diagnostico 3004
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
	public List<Tb_soggetto> verificaSimiliConferma(String id, A250 t250)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		Tb_soggetto tb_soggetto = new Tb_soggetto();
		tb_soggetto = gestisceDescrizione(t250, tb_soggetto, true);
		tb_soggetto.setCID(id);
		if (t250.getC2_250() != null) {
			String codiceSogg = Decodificatore.getCd_tabella("Tb_soggetto", "cd_soggettario", t250.getC2_250().toUpperCase().trim());
			if (codiceSogg != null)
				setCD_SOGGETTARIO(codiceSogg);
			else
				throw new EccezioneSbnDiagnostico(3088,	"codice soggettario errato");
		}

		Tb_soggettoResult dao = new Tb_soggettoResult(tb_soggetto);
		if (tb_soggetto.getKY_CLES2_S() == null)
			tb_soggetto.settaParametro(TableDao.XXXky_cles2_sNULL, "null");

		dao.executeCustom("selectSimiliConferma");

		return dao.getElencoRisultati();
	}


	public boolean validaPerCrea(String id, String user,boolean _cattura) throws IllegalArgumentException, InvocationTargetException, Exception {
		Tb_soggetto tb_soggetto;
        if (!id.equals("0000000000")) {

    		tb_soggetto = verificaEsistenzaID(id);
   	    	if (tb_soggetto != null && !ValidazioneDati.in(tb_soggetto.getFL_CANC(), "S", "s") )
   	    		if(!_cattura)
   	    			throw new EccezioneSbnDiagnostico(3012,"elemento già esistente");
            if (!verificaCid(id,user))
                throw new EccezioneSbnDiagnostico(3902, "Cid non valido");
        }
  		return true;
	}


	public boolean validaCatt_sogg_Nota(String catt_sogg, String Nota,boolean _cattura) throws IllegalArgumentException, InvocationTargetException, Exception {
		if (catt_sogg != null){
			String categoriaSogg = Decodificatore.getCd_tabella("Tb_soggetto","cat_sogg", catt_sogg.toUpperCase().trim());
			if (categoriaSogg != null)
				setCAT_SOGG(categoriaSogg);
			else throw new EccezioneSbnDiagnostico(3392,"Categoria soggettario errato");
		}
		else{
			throw new EccezioneSbnDiagnostico(3391,"Categoria soggettario mancante");
		}
		if (Nota != null){
			//String categoriaSogg = Decodificatore.getCd_tabella("Tb_soggetto","cat_sogg", catt_sogg.toUpperCase().trim());
			setNOTA_SOGGETTO(Nota);
		}
  		return true;
	}


    /**
    * metodo di validazione per operazione di modifica Soggetto:
    * . verificaEsistenzaID: se non trovato ritorna un diagnostico
    * . verificaVersioneSoggetto: se il risultato è negativo ritorna un diagnostico
    * . verificaLivelloModifica: se il risultato è falso ritorna un diagnostico al client.
     * @throws InfrastructureException
    */
	public Tb_soggetto validaPerModifica(String utente, String id, String t005,boolean _cattura) throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
		Tb_soggetto tb_soggetto;
		tb_soggetto = cercaSoggettoPerId(id);
   		if (tb_soggetto == null)
  			throw new EccezioneSbnDiagnostico(3014,"elemento non trovato");
		if (verificaLivelloModifica(tb_soggetto,_cattura))
			verificaVersioneSoggetto(tb_soggetto,t005);
		controllaParametriUtente(utente,tb_soggetto.getCD_LIVELLO(),_cattura);
		//almaviva5_20180416 #6573
		//controllaModificaLivello(utente,tb_soggetto.getCD_LIVELLO(),tb_soggetto.getUTE_VAR());
  		return tb_soggetto;
	}

    private boolean verificaLivelloModifica(Tb_soggetto tb_soggetto, boolean _cattura) throws EccezioneSbnDiagnostico {
        if (_cattura)
            return true;
        if (Integer.parseInt(_livelloAut.toString()) < Integer.parseInt(tb_soggetto.getCD_LIVELLO())) {
        	throw new EccezioneSbnDiagnostico(3010,"Livello di autorità in base dati superiore a quello comunicato");
        }
        return true;
    }

    boolean controllaModificaLivello(String utente, String livelloSoggetto, String uteVar) throws EccezioneSbnDiagnostico {
        if (Integer.parseInt(_livelloAut.toString()) == Integer.parseInt(livelloSoggetto)) {
			if (!ValidazioneDati.equals(utente, uteVar))
            	throw new EccezioneSbnDiagnostico(3353);
        }
        return true;
    }

    /**
     * metodo che controlla il vincolo di versione in modifica:
     * T005 deve essere uguale a ts_var
     * NB: questo metodo è comune, può essere spostato in Factoring
     */
    private boolean verificaVersioneSoggetto(Tb_soggetto tb_soggetto, String t005) throws EccezioneSbnDiagnostico {
        if (t005 != null){
            SbnDatavar data = new SbnDatavar(tb_soggetto.getTS_VAR());
            return data.getT005Date().equals(t005);
        }
        throw new EccezioneSbnDiagnostico(3017, "Manca l'informazione sul ts_var");
    }

	/**
	 * Valida il soggetto (chiama metodo validaPerCancella in SoggettoValida'):
	 * controllo di esistenza per identificativo,
	 * controllo che non esistano legami con titoli (tr_tit_sog), altrimenti segnalo
	 * diagnostico 'Esistono legami a titoli'
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public Tb_soggetto validaPerCancella(String utente, String id) throws IllegalArgumentException, InvocationTargetException, Exception {

		Tb_soggetto tb_soggetto = cercaSoggettoPerId(id);
   		if (tb_soggetto == null)
			throw new EccezioneSbnDiagnostico(3001, "elemento non trovato");

   		//almaviva5_20091117 controllo abilitazione soggettario
		String cd = Decodificatore.getCd_unimarc("Tb_soggetto", "cd_soggettario", tb_soggetto.getCD_SOGGETTARIO().trim());
		controllaVettoreParametriSemantici(cd, utente);

		verificaTitoliPerSoggetto(id);
		controllaParametriUtente(utente, tb_soggetto.getCD_LIVELLO(), false);
		return tb_soggetto;
	}



	private void verificaTitoliPerSoggetto(String id) throws IllegalArgumentException, InvocationTargetException, Exception{
		Tr_tit_sog_bib tr_tit_sog = new Tr_tit_sog_bib();
		tr_tit_sog.setCID(id);
		Tr_tit_sog_bibResult tavola = new Tr_tit_sog_bibResult(tr_tit_sog);


		tavola.executeCustom("selectTitoloPerSoggetto");
		if (tavola.getElencoRisultati().size() > 0)
			throw new EccezioneSbnDiagnostico(3091,"Esistono legami con i titoli");

	}


	/**
	 * Method validaPerFonde.
	 * @param idPartenza
	 * @param idArrivo
	 * @param isFusione
	 * @throws InfrastructureException
	 */
	public void validaPerFonde(String utente, String idPartenza,
			String idArrivo, boolean isFusione) throws EccezioneDB,
			EccezioneSbnDiagnostico, InfrastructureException {

		if (ValidazioneDati.equals(idPartenza, idArrivo))
			throw new EccezioneSbnDiagnostico(3341); // id uguali

        Tb_soggetto tb_sogg_partenza = new Tb_soggetto();
		Soggetto soggetto = new Soggetto();
		tb_sogg_partenza = soggetto.cercaSoggettoPerId(idPartenza);
		if (tb_sogg_partenza == null)
			throw new EccezioneSbnDiagnostico(3001, "idPartenza non esistente");

		//controllo abilitazione soggettario partenza
		String cd = Decodificatore.getCd_unimarc("Tb_soggetto", "cd_soggettario", tb_sogg_partenza.getCD_SOGGETTARIO().trim());
		controllaVettoreParametriSemantici(cd, utente);

		//solo nel caso di fusione devo controllare se ho
		//l'autorità sufficiente per cancellare il soggetto
		if (isFusione)
			controllaParametriUtente(utente, tb_sogg_partenza.getCD_LIVELLO(), false);

		Tb_soggetto tb_sogg_arrivo = soggetto.cercaSoggettoPerId(idArrivo);
		if (tb_sogg_arrivo == null)
			throw new EccezioneSbnDiagnostico(3001, "idArrivo non esistente");

		//controllo abilitazione soggettario arrivo
		cd = Decodificatore.getCd_unimarc("Tb_soggetto", "cd_soggettario", tb_sogg_arrivo.getCD_SOGGETTARIO().trim());
		controllaVettoreParametriSemantici(cd, utente);
	}


	private void controllaParametriUtente(String utente, String livelloAut,boolean _cattura) throws EccezioneSbnDiagnostico{

      Tbf_par_auth par = ValidatorProfiler.getInstance().getParametriUtentePerAuthority(utente, "SO");
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

    public boolean verificaCid(String cid, String user) {
        boolean b = false;
        if (cid.length() > 10) {
            return false;
        }

        char[] c_vid = cid.toCharArray();
		if (c_vid[3] == 'C') {
            boolean c = true;
            for (int i = 4; i < c_vid.length && b; i++) {
                c = Character.isDigit(c_vid[i]);
            }
            b = c;
        }
        return b;
    }

    public boolean controllaVettoreParametriSemantici(String c2_250, String codiceUtente) throws EccezioneSbnDiagnostico{

		ValidatorProfiler validator = ValidatorProfiler.getInstance();

		Tbf_biblioteca_in_polo biblio = validator.getBiblioteca(codiceUtente.substring(0, 3), codiceUtente.substring(3,6));
        Iterator iter = biblio.getTr_soggettari_biblioteche().iterator();

		boolean trovato = false;
		c2_250 = Decodificatore.getCd_tabella("Tb_descrittore", "cd_soggettario", c2_250.toUpperCase());
		if(c2_250 == null)
			throw new EccezioneSbnDiagnostico(3088); // sogg. non valido

		while(iter.hasNext()) {
        	Tr_soggettari_biblioteche soggettari = (Tr_soggettari_biblioteche) iter.next();
        	if (c2_250.equals(soggettari.getCD_SOGG().trim())) {
        		//almaviva5_20091030
        		trovato = soggettari.getFL_ATT().equals("1"); //solo se attivo alla gestione
    			break;
        	}
		}

		if (!trovato)
			throw new EccezioneSbnDiagnostico(3211,"soggettario non gestito dalla biblioteca");
		return trovato;
	}

    public void controllaMaxLivelloSoggettazione(String cdSogg, String bid,	SbnUserType sbnUser) throws EccezioneSbnDiagnostico, Exception {

		Tbf_par_auth par =
			ValidatorProfiler.getInstance().getParametriUtentePerAuthority(sbnUser.getBiblioteca() + sbnUser.getUserId(), "SO");
		if (par == null)
            throw new EccezioneSbnDiagnostico(3009, "Utente non abilitato a modificare l'authority");

		String livelloUtente = par.getCd_livello();

		SoggettoTitolo sogTit = new SoggettoTitolo();
		Tb_soggetto soggetto = new Tb_soggetto();
		soggetto.setCD_SOGGETTARIO(cdSogg);

		//row[0]=bid
		//row[1]=sogg.
		//row[2]=ute_var
		//row[3]=livAut
		MaxLivelloLegameSog row = sogTit.cercaUteVarMaxLivelloAutLegameSoggettario(soggetto, bid);
		if (row == null)
			return;

		String uteVar = row.getUte_var();
		int livAutLegame = Integer.parseInt(row.getMaxLivAut());
		int livAutUtente = Integer.parseInt(livelloUtente);
		if (livAutLegame > livAutUtente)
			throw new EccezioneSbnDiagnostico(3339);

		//almaviva5_20100111 #3448
		// test per livello MAX(90)
		String codBibUte = uteVar.substring(0, 6);
		if (livAutLegame == 90)
			if (livAutUtente < 95 && !sbnUser.getBiblioteca().equals(codBibUte))
				throw new EccezioneSbnDiagnostico(3340);
	}

    // Inizio modifica almaviva2 22.06.2012 nuova funzione per controllare i livelli Autorità legami Tit-Sogg
	// fra due titoli in fusione
	public int controllaLivelloSoggettazioneBid(String cdSogg, String bid) throws EccezioneSbnDiagnostico, Exception {

		SoggettoTitolo sogTit = new SoggettoTitolo();
		Tb_soggetto soggetto = new Tb_soggetto();
		soggetto.setCD_SOGGETTARIO(cdSogg);

		//row[0]=bid
		//row[1]=sogg.
		//row[2]=ute_var
		//row[3]=livAut
		MaxLivelloLegameSog row = sogTit.cercaUteVarMaxLivelloAutLegameSoggettario(soggetto, bid);
		if (row == null)
			return -1;

		return Integer.parseInt(row.getMaxLivAut());
	}

	// Fine modifica almaviva2 22.06.2012 i soggetti vengono spostati dal bid di partenza a quello di arrivo

	public static final String[] preparaDescrittori(A250 _t250) {
/*
		int len = _t250.getX_250().length;
		String[] result = new String[len + 1];
		result[0] = ValidazioneDati.trimOrEmpty(_t250.getA_250().getContent());
		for (int i = 0; i < len; i++) {
			String d = _t250.getX_250(i).getContent();
			result[i + 1] = ValidazioneDati.trimOrEmpty(d);
		}

		//almaviva5_20120416 evolutive CFI
		//nuova suddivisione descrittori (da Indice)
		List<String> dd = new ArrayList<String>();
		for (String d : result) {
			String[] altri = d.split(NormalizzaNomi.REGEX_DESCRITTORI_SEP_ULTERIORI);
			for (String altro : altri)
				if (ValidazioneDati.isFilled(altro))
					dd.add(altro.trim());
		}

		result = (String[]) dd.toArray(new String[0]);

		return result;
*/
		try {
			KeySoggetto key = ChiaviSoggetto.build(null, null, _t250);
			return key.getDescrittori().toArray(new String[0]);

		} catch (EccezioneSbnDiagnostico e) {
			return null;
		}
	}

	public static final String[] getEdizioniRicerca(SbnEdizioneSoggettario cd_edizione) {
		String[] values = null;
    	switch (cd_edizione.getType()) {
    	case SbnEdizioneSoggettario.E_TYPE:
    		values = new String[] {"E"};
    		break;
    	case SbnEdizioneSoggettario.I_TYPE:
    		values = new String[] {"E", "I" };
    		break;
    	case SbnEdizioneSoggettario.N_TYPE:
    		values = new String[] {"E", "N" };
    		break;
    	}
    	return values;
	}

	/**
	 * questo metodo riceve in input un vettore di descrittori
	 * lo scorre
	 * . se non esiste il descrittore
	 * . . ne viene creato uno nuovo attribuendo anche un did
	 * . se esiste già
	 * . . controlla che esista il legame con quel descrittore
	 * . . se non esiste il legame viene creato
	 * @param cdUtente
	 * @param _condiviso
	 * @param _t250
	 * @param _condiviso2
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 *
	 */
	public boolean controllaDescrittori(String _t001, A250 _t250, String _t601, String _condiviso, String[] vettoreDescrittori, String cdUtente) throws IllegalArgumentException, InvocationTargetException, Exception{
		Descrittore descrittore = new Descrittore();
		String did = null;
		int posizione = 0;

		for (int i = 0; i < vettoreDescrittori.length; i++) {
			if (!ValidazioneDati.isFilled(vettoreDescrittori[i]) )
				continue;

			boolean isFirst = (++posizione == 1);
			String cat_termine = isFirst ? _t601 : null;

			TableDao dao = descrittore.cercaDescrittorePerNomeEsatto(
							ChiaviSoggetto.normalizzaDescrittore(vettoreDescrittori[i]),
							null, Decodificatore.getCd_tabella("Tb_soggetto", "cd_soggettario", _t250.getC2_250().toUpperCase().trim()));
			List<Tb_descrittore> risultati = dao.getElencoRisultati();
			if (!ValidazioneDati.isFilled(risultati) ) {
				Progressivi prog = new Progressivi();
				did = prog.getNextIdDescrittore();
				// I DESCRITTORI AUTOMATICI EREDITANO IL CONDIVISO DAL SOGGETTO
				//almaviva5_20120322 evolutive CFI
				String edizione = _t250.getEdizione() != null ? _t250.getEdizione().toString() : null;
				//se è il primo descrittore impongo la cat. del soggetto
				descrittore.creaDescrittore(
								did,
								_t250.getC2_250(),
								vettoreDescrittori[i],
								_livelloAut.toString(),
								cdUtente,
								_condiviso,
								edizione,
								cat_termine);
			} else {
				//almaviva5_20091120 #3281 controllo forma rinvio
				Tb_descrittore tb_descrittore = ValidazioneDati.first(risultati);
				tb_descrittore = descrittore.controllaEdizioneDescrittore(_t250.getEdizione(), tb_descrittore, cdUtente);
				//almaviva5_20120515 cat termine se non valorizzato
				if (isFirst && !ValidazioneDati.isFilled(tb_descrittore.getCAT_TERMINE()) ) {
					tb_descrittore.setCAT_TERMINE(cat_termine);
					descrittore.aggiornaDescrittore(tb_descrittore, cdUtente);
				}

				did = tb_descrittore.getDID();
			}

			//creo i legami con il soggetto
			SoggettoDescrittore sd = new SoggettoDescrittore();
			Tr_sog_des tr_sog_des = sd.controllaEsistenzaLegame(_t001, did);
			if (tr_sog_des == null)
				sd.inserisciLegame(_t001, did, (isFirst ? "S" : "N"), posizione, cdUtente);
			else
				if (ValidazioneDati.equals(tr_sog_des.getFL_PRIMAVOCE(), "S") )
					sd.updateLegame(posizione, cdUtente);

			did = null;
		}

		return true;
	}

}
