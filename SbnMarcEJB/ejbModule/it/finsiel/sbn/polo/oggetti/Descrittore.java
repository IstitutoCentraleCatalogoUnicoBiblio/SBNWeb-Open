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

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_descrittoreResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_descrittore_desResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_descrittore_sogResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.ChiaviSoggetto;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.orm.Tb_descrittore;
import it.finsiel.sbn.polo.orm.viste.Vl_descrittore_des;
import it.finsiel.sbn.polo.orm.viste.Vl_descrittore_sog;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaSoggettoDescrittoreClassiReperType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

/**
 * @author
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Descrittore extends Tb_descrittore {

	private static final long serialVersionUID = 1873629061342064753L;

	private static Category log = Category.getInstance("iccu.serversbnmarc.Descrittore");

	private static int SOG_DES_MAX_LENGTH = ResourceLoader.getPropertyInteger("SOG_DES_MAX_LENGTH");

	protected boolean filtriValorizzati;



    public Descrittore() {
    	super();
    }

    public List cercaDescrittorePerSoggetto(String id_soggetto)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_descrittore_sog des_sog = new Vl_descrittore_sog();
        des_sog.setCID(id_soggetto);
        Vl_descrittore_sogResult dao = new Vl_descrittore_sogResult(des_sog);


        dao.executeCustom("selectDescrittorePerSoggetto");
        List v = dao.getElencoRisultati();

        return v;

    }

    public Tb_descrittore cercaDescrittorePerId(String id) throws EccezioneDB, InfrastructureException {
        setDID(id);
        Tb_descrittoreResult tavola = new Tb_descrittoreResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        Tb_descrittore descr = null;
        if (v.size() > 0)
            descr = (Tb_descrittore) v.get(0);
        return descr;

    }

    public Tb_descrittore verificaEsistenza(String id) throws IllegalArgumentException, InvocationTargetException, Exception {
        setDID(id);
        Tb_descrittoreResult tavola = new Tb_descrittoreResult(this);


        tavola.executeCustom("selectPerEsistenza");
        List v = tavola.getElencoRisultati();

        Tb_descrittore descr = null;
        if (v.size() > 0)
            descr = (Tb_descrittore) v.get(0);
        return descr;

    }

    public TableDao cercaDescrittorePerk_norm_descrittore(
    String ky_norm,
    String cd_soggettario) throws IllegalArgumentException, InvocationTargetException, Exception {
        setKY_NORM_DESCRITT(ky_norm);
        setCD_SOGGETTARIO(cd_soggettario);
        Tb_descrittoreResult tavola = new Tb_descrittoreResult(this);


        tavola.executeCustom("selectPerKyNormDescrittore");
        return tavola;
    }


    public TableDao cercaDescrittorePerIdInTavola(String id) throws EccezioneDB, InfrastructureException {
        setDID(id);
        Tb_descrittoreResult tavola = new Tb_descrittoreResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        return tavola;

    }


    public void valorizzaFiltri(CercaDatiAutType cerca) {
        valorizzaFiltri(this, cerca);
	}

    /** Valorizza i filtri */
    public Tb_descrittore valorizzaFiltri(Tb_descrittore soggetto, CercaDatiAutType cerca) {
        CercaSoggettoDescrittoreClassiReperType cercaSoggetto = null;
		filtriValorizzati = true;
        if (cerca == null)
            return soggetto;
        if (cerca.getLivelloAut_Da() != null)
            soggetto.settaParametro(TableDao.XXXlivello_aut_da,
                Decodificatore.livelloSogliaDa(cerca.getLivelloAut_Da().toString()));
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
		if (cerca instanceof CercaSoggettoDescrittoreClassiReperType) {
			cercaSoggetto = (CercaSoggettoDescrittoreClassiReperType) cerca;

			if (cercaSoggetto.getC2_250() != null)
				setCD_SOGGETTARIO(Decodificatore.getCd_tabella("Tb_soggetto","cd_soggettario", cercaSoggetto.getC2_250().toUpperCase().trim()));
			// almaviva5_20111130 evolutive CFI
			if (cercaSoggetto.getEdizione() != null)
				setCD_EDIZIONE(cercaSoggetto.getEdizione().toString());
		}
        return soggetto;
    }

    public int contaSoggettoPerDescrittori(
        String[] paroleNome)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        for (int i = 1; i <= paroleNome.length; i++)
            settaParametro("XXXparola" + i, paroleNome[i - 1]);
        Tb_descrittoreResult tavola = new Tb_descrittoreResult(this);


        tavola.executeCustom("countPerParoleNome");
        int n = conta(tavola);

        return n;
    }

    public int contaDescrittorePerParoleNome(
            String[] paroleNome)
            throws IllegalArgumentException, InvocationTargetException, Exception {
//            for (int i = 1; i <= paroleNome.length; i++){
//            	if (paroleNome[i - 1] != "")
//            		settaParametro("XXXparola" + i, paroleNome[i - 1]);
//            }
	        for (int i = 1; i <= paroleNome.length; i++)
	        		settaParametro("XXXparola" + i, paroleNome[i - 1]);
            Tb_descrittoreResult tavola = new Tb_descrittoreResult(this);


            tavola.executeCustom("countPerParoleNome");
            int n = conta(tavola);

            return n;
        }

    public int contaDescrittorePerParolaEsatta(
            String paroleNome)
            throws IllegalArgumentException, InvocationTargetException, Exception {
	        settaParametro("XXXparola" + 1, paroleNome);
            Tb_descrittoreResult tavola = new Tb_descrittoreResult(this);


            tavola.executeCustom("countPerParoleNome");
            int n = conta(tavola);

            return n;
        }

    public TableDao cercaDescrittorePerParoleNome(
        String[] paroleNome,
        String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        for (int i = 1; i <= paroleNome.length; i++)
            settaParametro("XXXparola" + i, paroleNome[i - 1]);
        Tb_descrittoreResult tavola = new Tb_descrittoreResult(this);


        tavola.executeCustom("selectPerParoleNome", ordinamento);
        return tavola;
    }

    public int contaDescrittorePerNomeLike(String nome)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_descrittoreResult tavola = new Tb_descrittoreResult(this);


        settaParametro(TableDao.XXXstringaLike, nome);
        tavola.executeCustom("countPerNomeLike");
        int n = conta(tavola);

        return n;
    }


    public TableDao cercaDescrittorePerNomeLike(
        String nome,
        String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_descrittoreResult tavola = new Tb_descrittoreResult(this);


        settaParametro(TableDao.XXXstringaLike, nome);
        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return tavola;
    }


    public int contaDescrittorePerNomeEsatto(String nome,String cd_soggettario)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_descrittoreResult tavola = new Tb_descrittoreResult(this);


        settaParametro(TableDao.XXXstringaEsatta, nome);
        if (cd_soggettario != null)
        	settaParametro(TableDao.XXXcd_soggettario, cd_soggettario);
        tavola.executeCustom("countPerNomeEsatto");
        int n = conta(tavola);

        return n;
    }


    public TableDao cercaDescrittorePerNomeEsatto(
        String nome,
        String ordinamento,String cd_soggettario)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_descrittoreResult tavola = new Tb_descrittoreResult(this);


        settaParametro(TableDao.XXXstringaEsatta, nome);
        if (cd_soggettario != null)
        	settaParametro(TableDao.XXXcd_soggettario, cd_soggettario);
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
	 * Method creaDescrittore.
	 * @param id
	 * @param codiceSoggettario
	 * @param descrizioneDes
	 * @param livello
	 * @param edizione
	 * @throws InfrastructureException
	 */
	public boolean creaDescrittore(String id, String codiceSoggettario,
			String a931, String livello, String user,
			String _condiviso, String edizione, String cat_termine) throws EccezioneDB,EccezioneSbnDiagnostico, InfrastructureException {

        Tb_descrittore tb_descrittore = new Tb_descrittore();
        tb_descrittore.setCD_LIVELLO(livello);
		if (codiceSoggettario != null) {
			String codiceSogg = Decodificatore.getCd_tabella("Tb_soggetto","cd_soggettario", codiceSoggettario.toUpperCase().trim());
            tb_descrittore.setCD_SOGGETTARIO(codiceSogg);
		}
		//almaviva5_20120322 evolutive CFI
		tb_descrittore.setCD_EDIZIONE(edizione);
		tb_descrittore.setCAT_TERMINE(cat_termine);

		tb_descrittore = gestisceDescrizione(a931, tb_descrittore);

        tb_descrittore.setDID(id);
        tb_descrittore.setFL_CANC(" ");

        tb_descrittore.setNOTA_DESCRITTORE("");
        tb_descrittore.setUTE_INS(user);
        tb_descrittore.setUTE_VAR(user);
        tb_descrittore.setTP_FORMA_DES("A");
        // Timbro Condivisione
        tb_descrittore.setFL_CONDIVISO(_condiviso);
        tb_descrittore.setTS_CONDIVISO(TableDao.now());
        tb_descrittore.setUTE_CONDIVISO(user);

        Tb_descrittoreResult tavola = new Tb_descrittoreResult(tb_descrittore);

        tavola.insert(tb_descrittore);

		return true;
	}


	/**
	 * Method cercaRinviiDescrittore.
	 * @param string
	 * @param object
	 * @return TableDao
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
    public List cercaRinviiDescrittore(String did, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_descrittore_des descrittore = new Vl_descrittore_des();
        descrittore.setDID_1(did);
        Vl_descrittore_desResult tavola = new Vl_descrittore_desResult(descrittore);


        tavola.executeCustom("selectDescrittorePerRinvii", ordinamento);
        List v = tavola.getElencoRisultati();

        return v;
    }


	/**
	 * Method cancellaDescrittore.
	 * @param descrittoreDaCancellare
	 * @param string
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void cancellaDescrittore(
		Tb_descrittore descrittoreDaCancellare,
		String user) throws IllegalArgumentException, InvocationTargetException, Exception {
		List vettoreDiDescrittoriDaCancellare= new ArrayList();
//se il descrittore è in forma accettata vengono cancellati anche i descrittori in forma R
		if (descrittoreDaCancellare.getTP_FORMA_DES().equals("A")){
			//controlla se ci sono legami RT prendi il desc legato e cancella
			vettoreDiDescrittoriDaCancellare = controllaLegami(user,descrittoreDaCancellare);
		}

		DescrittoreDescrittore descrittoreDescrittore = new DescrittoreDescrittore();
		descrittoreDescrittore.cancellaLegame(user,descrittoreDaCancellare.getDID());
		cancellaDescrittori(user, vettoreDiDescrittoriDaCancellare);
		descrittoreDaCancellare.setUTE_VAR(user);
		descrittoreDaCancellare.setFL_CANC("S");
		Tb_descrittoreResult tavola = new Tb_descrittoreResult(descrittoreDaCancellare);


		tavola.executeCustom("cancellaDescrittore");


	}

	/**
	 * Method cancellaDescrittori.
	 * @param user
	 * @param vettoreDiDescrittoriDaCancellare
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void cancellaDescrittori(
		String user,
		List vettoreDiDescrittoriDaCancellare) throws IllegalArgumentException, InvocationTargetException, Exception {
			Tb_descrittore tb_descrittore;
		for (int i=0;i<vettoreDiDescrittoriDaCancellare.size();i++){
			tb_descrittore = (Tb_descrittore) vettoreDiDescrittoriDaCancellare.get(i);
			tb_descrittore.setUTE_VAR(user);
			tb_descrittore.setFL_CANC("S");
			Tb_descrittoreResult tavola = new Tb_descrittoreResult(tb_descrittore);


			tavola.executeCustom("cancellaDescrittore");

		}
	}


	/**
	 * Method controllaLegami.
	 * @param user
	 * @param descrittoreDaCancellare
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private List controllaLegami(
		String user,
		Tb_descrittore descrittoreDaCancellare) throws IllegalArgumentException, InvocationTargetException, Exception {
		Vl_descrittore_des vl_descrittore_des = new Vl_descrittore_des();
		vl_descrittore_des.setDID_1(descrittoreDaCancellare.getDID());
		vl_descrittore_des.setTP_LEGAME("RT");
		Vl_descrittore_desResult tavola = new Vl_descrittore_desResult(vl_descrittore_des);


		tavola.executeCustom("selectLegamiPerTipo");
        List TableDaoResult = tavola.getElencoRisultati();

		//restituisco il vettore dei descrittori che dovranno
		//essere cancellati dopo la cancellazione dei legami
		return TableDaoResult;
	}


	/**
	 * Method inserisceDescrittore.
	 * @param t001
	 * @param a931
	 * @param b931
	 * @param c2_931
	 * @param _edizione
	 * @param codiceUtente
	 * @param _condiviso2
	 * @return boolean
	 * @throws InfrastructureException
	 */
	public boolean inserisceDescrittore(String t001, String a931, String b931,
			String c2_931, SbnEdizioneSoggettario _edizione, String cat_termine,
			String codiceUtente, String livello, String formaNome,
			String _condiviso) throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        Tb_descrittore tb_descrittore = gestisceDescrizione(a931, null);

		tb_descrittore.setDID(t001);
		tb_descrittore.setNOTA_DESCRITTORE(b931);
		String cds = Decodificatore.getCd_tabella("Tb_descrittore",	"cd_soggettario", c2_931.toUpperCase());
		if (cds == null)
			throw new EccezioneSbnDiagnostico(3088, "Codice errato");

        tb_descrittore.setCD_SOGGETTARIO(cds);
        // almaviva5_20111130 evolutive CFI
        tb_descrittore.setCD_EDIZIONE(_edizione != null ? _edizione.toString() : null);
        tb_descrittore.setCAT_TERMINE(cat_termine);
        tb_descrittore.setCD_LIVELLO(livello);
        tb_descrittore.setUTE_INS(codiceUtente);
        tb_descrittore.setUTE_VAR(codiceUtente);
        tb_descrittore.setFL_CANC(" ");
        tb_descrittore.setTP_FORMA_DES(formaNome);
        tb_descrittore.setFL_CONDIVISO(_condiviso);
		Tb_descrittoreResult tavola = new Tb_descrittoreResult(tb_descrittore);

		tavola.insert(tb_descrittore);

		return true;
	}

	public Tb_descrittore gestisceDescrizione(String a931, Tb_descrittore d) throws EccezioneSbnDiagnostico {
		Tb_descrittore tb_descrittore = d != null ? d : new Tb_descrittore();

		String descrizioneDes = ValidazioneDati.trimOrEmpty(a931.replaceAll("\\[.*?\\]", "") );
		//descrizioneDes = NormalizzaNomi.eliminaAsterischi(descrizioneDes);
		if (!ValidazioneDati.isFilled(descrizioneDes))
			throw new EccezioneSbnDiagnostico(3049); //dati incompleti
		//almaviva5_20150915
		if (ValidazioneDati.length(descrizioneDes) > SOG_DES_MAX_LENGTH)
			throw new EccezioneSbnDiagnostico(3219);	//lunghezza eccessiva
		tb_descrittore.setDS_DESCRITTORE(descrizioneDes);

		String keyDescr = ChiaviSoggetto.normalizzaDescrittore(a931);
		if (!ValidazioneDati.isFilled(a931))
			throw new EccezioneSbnDiagnostico(3049); //dati incompleti
		tb_descrittore.setKY_NORM_DESCRITT(keyDescr);

		return tb_descrittore;
	}

	/**
	 * Method aggiornaDescrittore.
	 * @param tb_descrittore
	 * @param codiceUtente
	 * @throws InfrastructureException
	 */
	public void aggiornaDescrittore(
		Tb_descrittore 	tb_descrittore,
		String 			codiceUtente) throws EccezioneDB, InfrastructureException {
		tb_descrittore.setUTE_VAR(codiceUtente);
		Tb_descrittoreResult tavola = new Tb_descrittoreResult(tb_descrittore);


		tavola.update(tb_descrittore);

	}

	private Tb_descrittore controllaEdizioneDescrittoreRinvio(SbnEdizioneSoggettario edizioneIdPartenza,
			Tb_descrittore tb_descrittore,
			String cd_utente) throws Exception {

		SbnFormaNome forma = SbnFormaNome.valueOf(tb_descrittore.getTP_FORMA_DES());
		if (forma.getType() != SbnFormaNome.R_TYPE)	//forma accettata?
			return tb_descrittore;

		if (edizioneIdPartenza == null)
			//edizione vuota: controllo classico
			throw new EccezioneSbnDiagnostico(3343, true, new String[]{tb_descrittore.getDID()});

		Timestamp ts = TableDao.now();
		String edizione = tb_descrittore.getCD_EDIZIONE();
		/*if (!ValidazioneDati.isFilled(edizione)) {
			tb_descrittore.setCD_EDIZIONE(edizioneIdPartenza.toString());
			tb_descrittore.setUTE_VAR(cd_utente);
			tb_descrittore.setTS_VAR(ts);
			aggiornaDescrittore(tb_descrittore, cd_utente);
			return tb_descrittore;
		}*/

		DescrittoreDescrittore dd = new DescrittoreDescrittore();

		//edizione valorizzata: controllo congruenza edizione/legami del descrittore
		SbnEdizioneSoggettario edizioneDes = ValidazioneDati.isFilled(edizione) ?
			SbnEdizioneSoggettario.valueOf(edizione) :
			null;

		switch (edizioneIdPartenza.getType()) {
		case SbnEdizioneSoggettario.I_TYPE:
			if (ValidazioneDati.in(edizioneDes,
					SbnEdizioneSoggettario.I,
					SbnEdizioneSoggettario.E) )
				throw new EccezioneSbnDiagnostico(3343, true, new String[]{tb_descrittore.getDID()});

			//se il descrittore di rinvio è della nuova edizione
			//provo a riportarlo alla vecchia edizione modificando i suoi legami alla forma accettata
			tb_descrittore.setTP_FORMA_DES(SbnFormaNome.A.toString());
			tb_descrittore.setCD_EDIZIONE(SbnEdizioneSoggettario.I.toString());
			tb_descrittore.setUTE_VAR(cd_utente);
			tb_descrittore.setTS_VAR(ts);
			aggiornaDescrittore(tb_descrittore, cd_utente);

			dd.legameRinvio2legameStorico(tb_descrittore.getDID(), SbnEdizioneSoggettario.I, cd_utente);
			break;

		case SbnEdizioneSoggettario.N_TYPE:
			if (ValidazioneDati.in(edizioneDes,
					SbnEdizioneSoggettario.N,
					SbnEdizioneSoggettario.E) )
				throw new EccezioneSbnDiagnostico(3343, true, new String[]{tb_descrittore.getDID()});

			//se il descrittore di rinvio è della vecchia edizione
			//provo a portarlo alla nuova edizione modificando i suoi legami alla forma accettata
			tb_descrittore.setTP_FORMA_DES(SbnFormaNome.A.toString());
			tb_descrittore.setCD_EDIZIONE(SbnEdizioneSoggettario.N.toString());
			tb_descrittore.setUTE_VAR(cd_utente);
			tb_descrittore.setTS_VAR(ts);
			aggiornaDescrittore(tb_descrittore, cd_utente);

			dd.legameRinvio2legameStorico(tb_descrittore.getDID(), SbnEdizioneSoggettario.N, cd_utente);
			break;

		case SbnEdizioneSoggettario.E_TYPE:
			throw new EccezioneSbnDiagnostico(3343, true, new String[]{tb_descrittore.getDID()});
		}

		return tb_descrittore;

	}


	private Tb_descrittore controllaEdizioneDescrittoreAccettato(SbnEdizioneSoggettario edizioneIdPartenza,
			Tb_descrittore tb_descrittore, String cd_utente) throws Exception {

		if (edizioneIdPartenza == null)
			//edizione vuota: controllo classico
			return tb_descrittore;

		Timestamp ts = TableDao.now();
		String edizione = tb_descrittore.getCD_EDIZIONE();
		if (!ValidazioneDati.isFilled(edizione)) {
			tb_descrittore.setCD_EDIZIONE(edizioneIdPartenza.toString());
			tb_descrittore.setUTE_VAR(cd_utente);
			tb_descrittore.setTS_VAR(ts);
			aggiornaDescrittore(tb_descrittore, cd_utente);
			return tb_descrittore;
		}

		DescrittoreDescrittore dd = new DescrittoreDescrittore();

		//edizione valorizzata: controllo congruenza edizione/legami del descrittore
		SbnEdizioneSoggettario edizioneDes = SbnEdizioneSoggettario.valueOf(edizione);
		switch (edizioneIdPartenza.getType()) {
		case SbnEdizioneSoggettario.I_TYPE:
			if (ValidazioneDati.in(edizioneDes,
					SbnEdizioneSoggettario.I,
					SbnEdizioneSoggettario.E) )
				return tb_descrittore;

			//se il descrittore è della nuova edizione devo verificare che non abbia legami storici
			//provo a portarlo a tutte le edizioni
			if (dd.countLegamiStorici(tb_descrittore.getDID()) > 0 )
				throw new EccezioneSbnDiagnostico(3345, true, new String[]{tb_descrittore.getDID()});

			tb_descrittore.setCD_EDIZIONE(SbnEdizioneSoggettario.E.toString());
			tb_descrittore.setUTE_VAR(cd_utente);
			tb_descrittore.setTS_VAR(ts);
			aggiornaDescrittore(tb_descrittore, cd_utente);
			break;

		case SbnEdizioneSoggettario.N_TYPE:
			if (ValidazioneDati.in(edizioneDes,
					SbnEdizioneSoggettario.N,
					SbnEdizioneSoggettario.E) )
				return tb_descrittore;

			//se il descrittore è della vecchia edizione devo verificare che non abbia legami storici
			//provo a portarlo a tutte le edizioni
			if (dd.countLegamiStorici(tb_descrittore.getDID()) > 0 )
				throw new EccezioneSbnDiagnostico(3345, true, new String[]{tb_descrittore.getDID()});

			tb_descrittore.setCD_EDIZIONE(SbnEdizioneSoggettario.E.toString());
			tb_descrittore.setUTE_VAR(cd_utente);
			tb_descrittore.setTS_VAR(ts);
			aggiornaDescrittore(tb_descrittore, cd_utente);
			break;

		case SbnEdizioneSoggettario.E_TYPE:
			if (ValidazioneDati.in(edizioneDes,
					SbnEdizioneSoggettario.E) )
				return tb_descrittore;

			//se il descrittore è della vecchia/nuova edizione devo verificare che non abbia legami storici
			//provo a portarlo a tutte le edizioni
			if (dd.countLegamiStorici(tb_descrittore.getDID()) > 0 )
				throw new EccezioneSbnDiagnostico(3345, true, new String[]{tb_descrittore.getDID()});

			tb_descrittore.setCD_EDIZIONE(SbnEdizioneSoggettario.E.toString());
			tb_descrittore.setUTE_VAR(cd_utente);
			tb_descrittore.setTS_VAR(ts);
			aggiornaDescrittore(tb_descrittore, cd_utente);
			break;
		}

		return tb_descrittore;

	}

	public Tb_descrittore controllaEdizioneDescrittore(SbnEdizioneSoggettario edizioneIdPartenza,
			Tb_descrittore tb_descrittore,	String cd_utente) throws Exception {

		tb_descrittore = this.controllaEdizioneDescrittoreRinvio(edizioneIdPartenza, tb_descrittore, cd_utente);
		tb_descrittore = this.controllaEdizioneDescrittoreAccettato(edizioneIdPartenza, tb_descrittore, cd_utente);

		return tb_descrittore;

	}

	public List<Tb_descrittore> cercaDescrittoreAutomaticoPerSoggetto(String cid)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_descrittore_sog des_sog = new Vl_descrittore_sog();
        des_sog.setCID(cid);
        Vl_descrittore_sogResult dao = new Vl_descrittore_sogResult(des_sog);

        dao.executeCustom("selectDescrittoreAutomaticoPerSoggetto");
        List v = dao.getElencoRisultati();

        return v;
	}

}
