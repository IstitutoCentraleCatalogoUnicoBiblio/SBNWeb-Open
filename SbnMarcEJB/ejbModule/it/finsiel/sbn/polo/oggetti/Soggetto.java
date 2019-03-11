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
package it.finsiel.sbn.polo.oggetti;

import it.finsiel.gateway.intf.KeySoggetto;
import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.common.tavole.Tb_soggettoCommonDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_descrittoreResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_soggettoResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_sog_desResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_soggetto_desResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_soggetto_titResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.ChiaviSoggetto;
import it.finsiel.sbn.polo.factoring.base.FormatoSoggetto;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.TimbroCondivisione;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.orm.Tb_descrittore;
import it.finsiel.sbn.polo.orm.Tb_soggetto;
import it.finsiel.sbn.polo.orm.Tr_des_des;
import it.finsiel.sbn.polo.orm.Tr_sog_des;
import it.finsiel.sbn.polo.orm.viste.Vl_soggetto_des;
import it.finsiel.sbn.polo.orm.viste.Vl_soggetto_tit;
import it.iccu.sbn.ejb.model.unimarcmodel.A250;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaSoggettoDescrittoreClassiReperType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Classe Soggetto.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 10-gen-03
 */
public class Soggetto extends Tb_soggetto {

	private static final long serialVersionUID = 5991432208902256271L;

	private static Logger log = Logger.getLogger("iccu.serversbnmarc.Soggetto");

	private static int SOG_DES_MAX_LENGTH = ResourceLoader.getPropertyInteger("SOG_DES_MAX_LENGTH");

	protected boolean filtriValorizzati;


    public Soggetto() {
        super();
    }

    /** Esegue una ricerca dei soggetti legati ad un documento
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public List cercaSoggettoPerTitolo(String id_titolo, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_soggetto_tit tit_tit = new Vl_soggetto_tit();
        tit_tit.setBID(id_titolo);
        HashMap param = this.leggiAllParametro();
        Iterator iter = param.keySet().iterator();
        while(iter.hasNext()) {
        	String key = (String)iter.next();
        	tit_tit.settaParametro(key,param.get(key));
        }

        Vl_soggetto_titResult tavola = new Vl_soggetto_titResult(tit_tit);

        tavola.executeCustom("selectSoggettoPerTitolo", ordinamento);
        List v = tavola.getElencoRisultati();

        return v;

    }
	/**
	 * Method contaSoggettoPerTitolo.
	 * @param idArrivo
	 * @param object
	 * @return int
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public int contaSoggettoPerTitolo(String idArrivo)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_soggetto_tit tit_tit = new Vl_soggetto_tit();
        tit_tit.setBID(idArrivo);
        HashMap param = this.leggiAllParametro();
        Iterator iter = param.keySet().iterator();
        while(iter.hasNext()) {
        	String key = (String)iter.next();
        	tit_tit.settaParametro(key,param.get(key));
        }

        Vl_soggetto_titResult tavola = new Vl_soggetto_titResult(tit_tit);


        tavola.executeCustom("countPerTitolo");
        int n = conta(tavola);

        return n;
	}



    /** Esegue una ricerca dei soggetti legati ad un documento
     * @throws InfrastructureException */
    public Tb_soggetto cercaSoggettoPerId(String id) throws EccezioneDB, InfrastructureException {
        setCID(id);
        Tb_soggettoResult tavola = new Tb_soggettoResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        Tb_soggetto sogg = null;
        if (v.size() > 0)
            sogg = (Tb_soggetto) v.get(0);
        return sogg;

    }

    public TableDao cercaSoggettoPerIdInTavola(String id) throws EccezioneDB, InfrastructureException {
        setCID(id);
        Tb_soggettoResult tavola = new Tb_soggettoResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        return tavola;
    }

	/**
	 * metodo per la verifica in fase di validazione per la creazione di un soggetto
	 * Controlla l'esistenza di un soggetto per id senza usare filtri sul flag "fl_canc"
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
     */
    public Tb_soggetto verificaEsistenzaID(String id) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_soggetto soggetto = new Tb_soggetto();
        soggetto.setCID(id);
        Tb_soggettoResult tavola = new Tb_soggettoResult(soggetto);


		tavola.executeCustom("selectEsistenzaId");
        List response = tavola.getElencoRisultati();

        if (response.size()>0)
            return (Tb_soggetto) response.get(0);
        else return null;

    }

    public void valorizzaFiltri(CercaDatiAutType cerca) {
        valorizzaFiltri(this, cerca);
	}

    /** Valorizza i filtri */
    public Tb_soggetto valorizzaFiltri(Tb_soggetto soggetto, CercaDatiAutType cerca) {
        CercaSoggettoDescrittoreClassiReperType cercaSoggetto = null;
		filtriValorizzati = true;
        if (cerca == null)
            return soggetto;
        if (cerca.getLivelloAut_Da() != null)
            soggetto.settaParametro(TableDao.XXXlivello_aut_da,Decodificatore.livelloSogliaDa(cerca.getLivelloAut_Da().toString()));
        if (cerca.getLivelloAut_A() != null)
            soggetto.settaParametro(TableDao.XXXlivello_aut_a, cerca.getLivelloAut_A().toString());
        if (cerca.getT005_Range() != null) {
        	int filtro = cerca.getT005_Range().getTipoFiltroDate();
        	if (filtro < 2){
	            soggetto.settaParametro(TableDao.XXXdata_var_Da,cerca.getT005_Range().getDataDa().toString());
	            soggetto.settaParametro(TableDao.XXXdata_var_A,cerca.getT005_Range().getDataA().toString());
        	} else  if (filtro == 2){
	            soggetto.settaParametro(TableDao.XXXdata_ins_Da,cerca.getT005_Range().getDataDa().toString());
	            soggetto.settaParametro(TableDao.XXXdata_ins_A,cerca.getT005_Range().getDataA().toString());
        	}
        }
		if (cerca instanceof CercaSoggettoDescrittoreClassiReperType)
			cercaSoggetto = (CercaSoggettoDescrittoreClassiReperType) cerca;
		if (cercaSoggetto != null) {
			if (cercaSoggetto.getC2_250() != null) {
//				soggetto.setCD_SOGGETTARIO(cercaSoggetto.getC2_250());
				/**
				 * Converto il codice unimarc in cd_tabella
				 */
				soggetto.setCD_SOGGETTARIO(Decodificatore.convertUnimarcToSbn("SOGG", cercaSoggetto.getC2_250()));
				/*
				//almaviva5_20111128 evolutive CFI
				SbnEdizioneSoggettario edizione = cercaSoggetto.getEdizione();
				if (edizione != null)
					soggetto.setCD_EDIZIONE(edizione.toString());
				*/
            }
        }
        return soggetto;
    }


    public int contaSoggettoPerParoleNome(
        String[] paroleNome)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        for (int i = 1; i <= paroleNome.length; i++)
            settaParametro("XXXparola" + i, paroleNome[i - 1]);
        Tb_soggettoResult tavola = new Tb_soggettoResult(this);


        tavola.executeCustom("countPerParoleNome");
        int n = conta(tavola);

        return n;
    }

    public int contaSoggettoPerParoleDescrittore(
            String paroleNome)
            throws IllegalArgumentException, InvocationTargetException, Exception {
            settaParametro("XXXparola" + 1, paroleNome);
            Tb_soggettoResult tavola = new Tb_soggettoResult(this);


            tavola.executeCustom("countPerParoleNome");
            int n = conta(tavola);

            return n;
        }

    public TableDao cercaSoggettoPerParoleNome(
        String[] paroleNome,
        String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        for (int i = 1; i <= paroleNome.length; i++)
            settaParametro("XXXparola" + i, paroleNome[i - 1]);
        Tb_soggettoResult tavola = new Tb_soggettoResult(this);


        tavola.executeCustom("selectPerParoleNome", ordinamento);
        return tavola;
    }

    public int contaSoggettoPerNomeLike(String nome)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_soggettoResult tavola = new Tb_soggettoResult(this);


        settaParametro(TableDao.XXXstringaLike, nome);
        tavola.executeCustom("countPerNomeLike");
        int n = conta(tavola);

        return n;
    }


    public TableDao cercaSoggettoPerNomeLike(
        String nome,
        String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_soggettoResult tavola = new Tb_soggettoResult(this);


        settaParametro(TableDao.XXXstringaLike, nome);
        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return tavola;
    }


    public int contaSoggettoPerNomeEsatto(String nome)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_soggettoResult tavola = new Tb_soggettoResult(this);


        settaParametro(TableDao.XXXstringaEsatta, nome);
        tavola.executeCustom("countPerNomeEsatto");
        int n = conta(tavola);

        return n;
    }


    public TableDao cercaSoggettoPerNomeEsatto(
        String nome,
        String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_soggettoResult tavola = new Tb_soggettoResult(this);


        settaParametro(TableDao.XXXstringaEsatta, nome);
        tavola.executeCustom("selectPerNomeEsatto", ordinamento);
        return tavola;
    }


    /** Legge da una tavola il valore del COUNT(*) */
    public int conta(TableDao tavola) throws EccezioneDB {
    	return tavola.getCount();
//        try {
//            ResultSet rs = tavola.getResultSet();
//            rs.next();
//            return rs.getInt(1);
//        } catch (SQLException ecc) {
//            log.error("Errore nella lettura del COUNT (*) dal resultset");
//            throw new EccezioneDB(1203);
//        }
    }

	/**
	 * Method creaSoggetto.
	 * @throws InfrastructureException
	 */
	public boolean creaSoggetto(
	String t001,
	A250 t250,
	String cat_sogg,
	String nota,
	String user,
	String cd_livello,
	String _condiviso) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
		Tb_soggetto tb_soggetto = new Tb_soggetto();
		tb_soggetto = gestisceDescrizione(t250, tb_soggetto, false);
		tb_soggetto.setCID(t001);
		tb_soggetto.setCD_LIVELLO(cd_livello);
        tb_soggetto.setUTE_INS(user);
        tb_soggetto.setUTE_VAR(user);
        // almaviva FORZO I CAMPI   ky_primo_descr cat_sogg dopo mi diranno come gestirli???
        //tb_soggetto.setKY_PRIMO_DESCR("PRIMO DESCRITTORE");
        tb_soggetto.setKY_PRIMO_DESCR(" ");
        tb_soggetto.setCAT_SOGG(cat_sogg);
        tb_soggetto.setNOTA_SOGGETTO(nota);

        // Timbro Condivisione
        tb_soggetto.setFL_CONDIVISO(_condiviso);
        tb_soggetto.setTS_CONDIVISO(TableDao.now());
        tb_soggetto.setUTE_CONDIVISO(user);

        Tb_soggettoResult tavola = new Tb_soggettoResult(tb_soggetto);
        tavola.insert(tb_soggetto);

		return true;
	}


	public Tb_soggetto gestisceDescrizione(A250 t250, Tb_soggetto tb_soggetto,
			boolean simili) throws EccezioneSbnDiagnostico {
		String kyCles1_s = null;
		if (t250 != null) {
			if (t250.getC2_250() != null) {
				String codiceSogg = Decodificatore.getCd_tabella("Tb_soggetto", "cd_soggettario", t250.getC2_250().toUpperCase().trim());
				if (codiceSogg != null)
					tb_soggetto.setCD_SOGGETTARIO(codiceSogg);
				else
					throw new EccezioneSbnDiagnostico(3088,	"codice soggettario errato");
			}

			if (t250.getA_250() != null) {
				String ds_soggetto = FormatoSoggetto.componiSoggetto(t250);
				//almaviva5_20150915
				if (ValidazioneDati.length(ds_soggetto) > SOG_DES_MAX_LENGTH)
					throw new EccezioneSbnDiagnostico(3219);	//lunghezza eccessiva

				KeySoggetto key = ChiaviSoggetto.build(tb_soggetto.getCD_SOGGETTARIO(), t250.getEdizione(), t250);
				kyCles1_s = key.getKy_cles1_a();//NormalizzaNomi.normalizzaSoggetto(t250);

				if (!ValidazioneDati.isFilled(kyCles1_s))
					throw new EccezioneSbnDiagnostico(3049); //dati incompleti

				if (simili)
					tb_soggetto.setDS_SOGGETTO(ds_soggetto.trim().toUpperCase());
				else
					tb_soggetto.setDS_SOGGETTO(ds_soggetto);
			}

			tb_soggetto.setCD_EDIZIONE(t250.getEdizione() != null ? t250.getEdizione().toString() : null);
		}
		tb_soggetto.setFL_CANC("N");
		tb_soggetto.setFL_SPECIALE("N");
		tb_soggetto.setKY_CLES1_S(kyCles1_s);

		return tb_soggetto;
	}


	public void updateSoggetto(Tb_soggetto tb_soggetto, A250 t250,
			SbnLivello livelloAut, String user, String cat_sogg, String nota,
			String _condiviso) throws EccezioneDB, EccezioneSbnDiagnostico,
			InfrastructureException {
		tb_soggetto.setCAT_SOGG(cat_sogg);
		tb_soggetto.setNOTA_SOGGETTO(nota);
		tb_soggetto.setUTE_VAR(user);
		TimbroCondivisione timbroCondivisione = new TimbroCondivisione();
		timbroCondivisione.modificaTimbroCondivisione(tb_soggetto, user, _condiviso);
		tb_soggetto = gestisceDescrizione(t250, tb_soggetto, false);
		if (livelloAut != null)
			tb_soggetto.setCD_LIVELLO(livelloAut.toString());

		Tb_soggettoResult tavola = new Tb_soggettoResult(tb_soggetto);
		tavola.update(tb_soggetto);
	}



	/**
	 * Questo metodo cancella il soggetto che gli viene passato
	 * Method cancellaSoggetto.
	 * @param soggettoDaCancellare
	 * @param string
	 * @throws Exception
	 * @throws InfrastructureException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void cancellaSoggetto(
		Tb_soggetto soggettoDaCancellare,
		String user) throws IllegalArgumentException, InvocationTargetException, InfrastructureException, Exception {
		String cid = soggettoDaCancellare.getCID();

		if (cancellaLegamiDescrittori(cid, user)){
			soggettoDaCancellare.setFL_CANC("S");
			soggettoDaCancellare.setUTE_VAR(user);
			soggettoDaCancellare.setCID(cid);
			Tb_soggettoResult tavola = new Tb_soggettoResult(soggettoDaCancellare);
			tavola.executeCustom("cancellaSoggetto");
		}

	}
// almaviva LA CANCELLAZIONE DEI LEGAMI A DESCRITTORI E CORRETTA MA BISOGNA CONTROLLARE CHE SE UN DESCRITTORE E
// COLLEGATO AD UN ALTRO DESCRITTORE QUESTO NON DEVE ESSERE CANCELLATO

	private boolean cancellaLegamiDescrittori(String cid, String user) throws IllegalArgumentException, InvocationTargetException, Exception{
		Tr_sog_des tr_sog_des = new Tr_sog_des();
		tr_sog_des.setCID(cid);
		Tr_sog_desResult tavola = new Tr_sog_desResult(tr_sog_des);


		tavola.executeCustom("selectDescrittorePerSoggetto");
        List vettoreDiLegami = tavola.getElencoRisultati();

		Tr_sog_des legameDaCancellare;
        SoggettoDescrittore sogdes = new SoggettoDescrittore();
		for (int i=0; i<vettoreDiLegami.size();i++){
			legameDaCancellare = new Tr_sog_des();
			legameDaCancellare = (Tr_sog_des)vettoreDiLegami.get(i);
			legameDaCancellare.setFL_CANC("S");
			legameDaCancellare.setUTE_VAR(user);
			Tr_sog_desResult tav = new Tr_sog_desResult(legameDaCancellare);
			tav.executeCustom("updateFl_canc");

// QUI BIDOGNA INSERIRE IL CONTROLLO TRA DESCRITTORI E DESCRITTORI
            DescrittoreDescrittore descrittoreDes = new DescrittoreDescrittore();
            List<Tr_des_des> vettoreDiLegamiDescrittore = descrittoreDes.cercaLegame(legameDaCancellare.getDID());
            boolean legDesDes = true;
            if (vettoreDiLegamiDescrittore.size() > 0){
                log.debug("il descrittore non puo essere cancellato in quanto e lagato a un altro descrittore");
                legDesDes=false;
            }

            //VEcchia versione se il descrittore non ha altri soggetti legati lo devo cancellare.
            //nuova versione se il descrittore non ha altri soggetti legati e lo stesso non è lagato ad altri descrittori
            // devo cancellare.
            if ( (sogdes.cercaSoggettoPerDescrittore(legameDaCancellare.getDID()) == false) && (legDesDes) ){
                Tb_descrittore desc = new Tb_descrittore();
                desc.setFL_CANC("S");
                desc.setUTE_VAR(user);
                desc.setDID(legameDaCancellare.getDID());
                Tb_descrittoreResult dr = new Tb_descrittoreResult(desc);
                dr.executeCustom("cancellaDescrittore");
            }



		}
		return true;
	}

    public void updateVersione(String id,String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        setCID(id);
        setUTE_VAR(ute_var);
        Tb_soggettoResult tb_soggettoResult = new Tb_soggettoResult(this);
        tb_soggettoResult.executeCustom("updateVersione");
    }


	/**
	 * Method cercaSoggettiSimili.
	 * @param vettoreDescrittori
	 * @param _t250
	 * @return TableDao
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public List<Tb_soggetto> cercaSoggettiSimili(String id, A250 t250, String[] vettoreDescrittori)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		List<Tb_soggetto> vettoreSimili;
		String kyCles1_s = "";
		if (t250 != null) {
			if (t250.getC2_250() != null) {
				String codiceSogg = Decodificatore.getCd_tabella("Tb_soggetto", "cd_soggettario", t250.getC2_250().toUpperCase().trim());
				if (codiceSogg != null)
					setCD_SOGGETTARIO(codiceSogg);
				else
					throw new EccezioneSbnDiagnostico(3088,	"codice soggettario errato");
			}

			if (t250.getA_250() != null) {
				String ds_soggetto = FormatoSoggetto.componiSoggetto(t250);
				// normalizzare il ds_soggetto
				KeySoggetto key = ChiaviSoggetto.build(getCD_SOGGETTARIO(), t250.getEdizione(), t250);
				kyCles1_s = key.getKy_cles1_a();//NormalizzaNomi.normalizzaSoggetto(t250);
				if (!ValidazioneDati.isFilled(kyCles1_s))
					throw new EccezioneSbnDiagnostico(3049); // dati incompleti

				setDS_SOGGETTO(ds_soggetto);
			}
			SbnEdizioneSoggettario edizione = t250.getEdizione();
			if (edizione != null)
				setCD_EDIZIONE(edizione.toString());
		}

		setKY_CLES1_S(kyCles1_s);
		setCID(id);
		Tb_soggettoResult dao = new Tb_soggettoResult(this);
		dao.executeCustom("selectSimili");
		vettoreSimili = dao.getElencoRisultati();

		//almaviva5_20121015 controllo esteso su simili (check descrittori)
		//se il soggetto simile genera descrittori diversi non è un simile
		if (ValidazioneDati.isFilled(vettoreSimili))
			vettoreSimili = verificaDescrittoriAutomaticiPerSimili(vettoreSimili, vettoreDescrittori);

		return vettoreSimili;
	}

	private List<Tb_soggetto> verificaDescrittoriAutomaticiPerSimili(
			List<Tb_soggetto> vettoreSimili, String[] vettoreDescrittori)
			throws EccezioneSbnDiagnostico, InvocationTargetException,
			Exception {
		Descrittore descrittore = new Descrittore();
		//insieme di tutti i descrittori automatici per il soggetto in gestione
		Set<String> dd = new HashSet<String>();
		for (String ds_des : vettoreDescrittori)
			dd.add(ChiaviSoggetto.normalizzaDescrittore(ds_des));

		Iterator<Tb_soggetto> i = vettoreSimili.iterator();
		while (i.hasNext()) {
			List<Tb_descrittore> ddAutomatici = descrittore.cercaDescrittoreAutomaticoPerSoggetto(i.next().getCID());
			if (vettoreDescrittori.length != ddAutomatici.size() ) {
				//numero diverso di descrittori --> non simile
				i.remove();
				continue;
			}

			boolean found = true;
			for (Tb_descrittore d : ddAutomatici) {
				String ky_des = ValidazioneDati.trimOrEmpty(d.getKY_NORM_DESCRITT());
				if (!dd.contains(ky_des)) {
					//descrittore automatico non trovato --> non simile
					found = false;
					break;
				}
			}

			if (!found)
				i.remove();

		}
		return vettoreSimili;
	}

	/**
	 * Method contaSoggettoPerDescrittore.
	 * @param idArrivo
	 * @param _sog_edizione
	 * @return int
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public int contaSoggettoPerDescrittore(String idArrivo, SbnEdizioneSoggettario edizione)
        throws IllegalArgumentException, InvocationTargetException, Exception {
		Vl_soggetto_des vl_soggetto_des = new Vl_soggetto_des();
		vl_soggetto_des.setDID(idArrivo);
		Vl_soggetto_desResult tavola = new Vl_soggetto_desResult(vl_soggetto_des);

		//almaviva5_20120906 evolutive CFI
		if (edizione != null)
			vl_soggetto_des.settaParametro(TableDao.XXXcd_edizione_IN, edizione);

        tavola.executeCustom("countSoggettoPerDescrittore");
        int n = conta(tavola);

        return n;
	}

	public int contaSoggettoPerDescrittorePerPosizione(String[] didLegami, String posizione, SbnEdizioneSoggettario edizione)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		Vl_soggetto_des vl_soggetto_des = new Vl_soggetto_des();

		if (ValidazioneDati.size(didLegami) == 1)
			vl_soggetto_des.setDID(didLegami[0]);
		else
			vl_soggetto_des.settaParametro(Tb_soggettoCommonDao.XXXdid_IN, didLegami);

		if (ValidazioneDati.isFilled(posizione) )
			vl_soggetto_des.setFL_PRIMAVOCE(posizione);

		//almaviva5_20120906 evolutive CFI
		if (edizione != null)
			vl_soggetto_des.settaParametro(TableDao.XXXcd_edizione_IN, edizione);

		Vl_soggetto_desResult tavola = new Vl_soggetto_desResult(vl_soggetto_des);

		tavola.executeCustom("countSoggettoPerDescrittore");
		int n = conta(tavola);

		return n;
	}

	/**
	 * Method cercaSoggettoPerDescrittore.
	 * @param idArrivo
	 * @param edizione
	 * @param string
	 * @return TableDao
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public TableDao cercaSoggettoPerDescrittore(
		String idArrivo,
        String ordinamento, SbnEdizioneSoggettario edizione)
        throws IllegalArgumentException, InvocationTargetException, Exception {
		Vl_soggetto_des vl_soggetto_des = new Vl_soggetto_des();
		vl_soggetto_des.setDID(idArrivo);

		//almaviva5_20120906 evolutive CFI
		if (edizione != null)
			vl_soggetto_des.settaParametro(TableDao.XXXcd_edizione_IN, edizione);

		Vl_soggetto_desResult tavola = new Vl_soggetto_desResult(vl_soggetto_des);


        tavola.executeCustom("selectLegameDescrittore", ordinamento);
        return tavola;
	}

	public TableDao cercaSoggettoPerDescrittorePerPosizione(String[] didLegami,
			String posizione, String ordinamento,
			SbnEdizioneSoggettario edizione) throws IllegalArgumentException,
			InvocationTargetException, Exception {
		Vl_soggetto_des vl_soggetto_des = new Vl_soggetto_des();

		if (ValidazioneDati.size(didLegami) == 1)
			vl_soggetto_des.setDID(didLegami[0]);
		else
			vl_soggetto_des.settaParametro(Tb_soggettoCommonDao.XXXdid_IN, didLegami);

		if (ValidazioneDati.isFilled(posizione) )
			vl_soggetto_des.setFL_PRIMAVOCE(posizione);

		//almaviva5_20120906 evolutive CFI
		if (edizione != null)
			vl_soggetto_des.settaParametro(TableDao.XXXcd_edizione_IN, edizione);

		Vl_soggetto_desResult tavola = new Vl_soggetto_desResult(vl_soggetto_des);

		tavola.executeCustom("selectLegameDescrittore", ordinamento);
		return tavola;
	}

	public TableDao cercaDidMultpli(
			String did_1,
			String did_2,
			String did_3,
			String did_4,
	        int campi,
	        String tipoOrd)
	        throws IllegalArgumentException, InvocationTargetException, Exception {
			Vl_soggetto_des vl_soggetto_des = new Vl_soggetto_des();
			Vl_soggetto_desResult tavola = new Vl_soggetto_desResult(vl_soggetto_des);
			tavola.valorizzaElencoRisultati( tavola.cercaDidMultpli(did_1,did_2,did_3,did_4,campi,tipoOrd));
	        return tavola;
		}

	public TableDao cercaDidMultpliTr(
			String did_1,
			String did_2,
			String did_3,
			String did_4,
	        int campi,
	        String tipoOrd)
	        throws IllegalArgumentException, InvocationTargetException, Exception {
			Tr_sog_des tr_sog_des = new Tr_sog_des();
			Tr_sog_desResult tavola = new Tr_sog_desResult(tr_sog_des);


			tavola.valorizzaElencoRisultati( tavola.cercaDidMultpli(did_1,did_2,did_3,did_4,campi,tipoOrd));

	        return tavola;
		}

	public int contaDidMultpli(
			String did_1,
			String did_2,
			String did_3,
			String did_4,
	        int campi)
	        throws IllegalArgumentException, InvocationTargetException, Exception {
			Tr_sog_des tr_sog_des = new Tr_sog_des();
			Tr_sog_desResult tavola = new Tr_sog_desResult(tr_sog_des);
			List ris = tavola.cercaDidMultpli(did_1,did_2,did_3,did_4,campi,null);
			int n = ris.size();
	        return n;
		}

}
