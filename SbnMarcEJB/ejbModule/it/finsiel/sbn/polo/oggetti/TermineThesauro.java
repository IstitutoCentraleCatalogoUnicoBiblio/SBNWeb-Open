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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_termine_thesauroResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_termini_terminiResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_thesauro_titResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.NormalizzaNomi;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.orm.Tb_termine_thesauro;
import it.finsiel.sbn.polo.orm.viste.Vl_termini_termini;
import it.finsiel.sbn.polo.orm.viste.Vl_thesauro_tit;
import it.iccu.sbn.ejb.model.unimarcmodel.A935;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaSoggettoDescrittoreClassiReperType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
public class TermineThesauro extends Tb_termine_thesauro{
   
	private static final long serialVersionUID = 8272311591215020087L;
	private boolean 		filtriValorizzati;
	private static 		Category log = Category.getInstance("iccu.serversbnmarc.Descrittore");


    public TermineThesauro() {
    }


    /** Esegue una ricerca dei soggetti legati ad un documento
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public List cercaThesauroPerTitolo(String id_titolo, String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_thesauro_tit the_tit = new Vl_thesauro_tit();
        the_tit.setBID(id_titolo);
        HashMap param = this.leggiAllParametro();
        Iterator iter = param.keySet().iterator();
        while(iter.hasNext()) {
        	String key = (String)iter.next();
        	the_tit.settaParametro(key,param.get(key));
        }

        Vl_thesauro_titResult tavola = new Vl_thesauro_titResult(the_tit);

        tavola.executeCustom("selectThesauroPerTitolo", ordinamento);
        List v = tavola.getElencoRisultati();

        return v;

    }

    /** Esegue una ricerca dei soggetti legati ad un documento
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public int contaThesauroPerTitolo(String id_titolo)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_thesauro_tit the_tit = new Vl_thesauro_tit();
        the_tit.setBID(id_titolo);
        Vl_thesauro_titResult tavola = new Vl_thesauro_titResult(the_tit);
        tavola.executeCustom("countPerTitolo");
        int n = conta(tavola);
        return n;

    }





    public Tb_termine_thesauro cercaTerminePerId(String id) throws EccezioneDB, InfrastructureException {
        setDID(id);
        Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        Tb_termine_thesauro descr = null;
        if (v.size() > 0)
            descr = (Tb_termine_thesauro) v.get(0);
        return descr;

    }

    public Tb_termine_thesauro verificaEsistenza(String id) throws IllegalArgumentException, InvocationTargetException, Exception {
        setDID(id);
        Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(this);


        tavola.executeCustom("selectPerEsistenza");
        List v = tavola.getElencoRisultati();

        Tb_termine_thesauro descr = null;
        if (v.size() > 0)
            descr = (Tb_termine_thesauro) v.get(0);
        return descr;

    }

    public TableDao cercaTerminePerk_norm_termine(
    String ky_termine_thesauro,
    String cd_the) throws IllegalArgumentException, InvocationTargetException, Exception {
        setKY_TERMINE_THESAURO(ky_termine_thesauro);
        setCD_THE(cd_the);
        Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(this);


        tavola.executeCustom("selectPerKyNormTermine");
        return tavola;
    }


    public TableDao cercaTerminePerIdInTavola(String id) throws EccezioneDB, InfrastructureException {
        setDID(id);
        Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        return tavola;

    }


    public void valorizzaFiltri(CercaDatiAutType cerca) {
        valorizzaFiltri(this, cerca);
	}

    /** Valorizza i filtri */
    public Tb_termine_thesauro valorizzaFiltri(Tb_termine_thesauro soggetto, CercaDatiAutType cerca) {
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
		if (cerca instanceof CercaSoggettoDescrittoreClassiReperType)
			cercaSoggetto = (CercaSoggettoDescrittoreClassiReperType) cerca;
		if (cercaSoggetto != null)
			if (cercaSoggetto.getC2_250() != null)
				setCD_THE(Decodificatore.getCd_tabella("Tb_termine_thesauro","cd_the", cercaSoggetto.getC2_250().toUpperCase().trim()));
        return soggetto;
    }

//    public int contaSoggettoPerDescrittori(
//        String[] paroleNome)
//        throws IllegalArgumentException, InvocationTargetException, Exception {
//        for (int i = 1; i <= paroleNome.length; i++)
//            settaParametro("XXXparola" + i, paroleNome[i - 1]);
//        Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(this);
//
//
//        tavola.executeCustom("countPerParoleNome");
//        int n = conta(tavola);
//
//        return n;
//    }

    public int contaTerminePerParoleNome(
            String[] paroleNome)
            throws IllegalArgumentException, InvocationTargetException, Exception {
//            for (int i = 1; i <= paroleNome.length; i++){
//            	if (paroleNome[i - 1] != "")
//            		settaParametro("XXXparola" + i, paroleNome[i - 1]);
//            }
	        for (int i = 1; i <= paroleNome.length; i++)
	        		settaParametro("XXXparola" + i, paroleNome[i - 1]);
            Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(this);


            tavola.executeCustom("countPerParoleNome");
            int n = conta(tavola);

            return n;
        }

    public int contaTerminePerParolaEsatta(
            String paroleNome)
            throws IllegalArgumentException, InvocationTargetException, Exception {
	        settaParametro("XXXparola" + 1, paroleNome);
            Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(this);


            tavola.executeCustom("countPerParoleNome");
            int n = conta(tavola);

            return n;
        }

    public TableDao cercaTerminePerParoleNome(
        String[] paroleNome,
        String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        for (int i = 1; i <= paroleNome.length; i++)
            settaParametro("XXXparola" + i, paroleNome[i - 1]);
        Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(this);


        tavola.executeCustom("selectPerParoleNome", ordinamento);
        return tavola;
    }

    public int contaTerminePerNomeLike(String nome)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(this);


        settaParametro(TableDao.XXXstringaLike, nome);
        tavola.executeCustom("countPerNomeLike");
        int n = conta(tavola);

        return n;
    }

    public int contaTerminePerNomeLike(String nome, String cd_the)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(this);

		settaParametro(TableDao.XXXstringaLike, nome);
		settaParametro(TableDao.XXXcd_the, cd_the);
		tavola.executeCustom("countPerNomeLike");
		int n = conta(tavola);

		return n;
	}

    public TableDao cercaTerminePerNomeLike(
        String nome,
        String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(this);


        settaParametro(TableDao.XXXstringaLike, nome);
        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return tavola;
    }
    public TableDao cercaTerminePerNomeLike(
            String nome,
            String ordinamento,
            String cd_the)
            throws IllegalArgumentException, InvocationTargetException, Exception {
            Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(this);


            settaParametro(TableDao.XXXstringaLike, nome);
    		settaParametro(TableDao.XXXcd_the, cd_the);
            tavola.executeCustom("selectPerNomeLike", ordinamento);
            return tavola;
        }

    public int contaTerminePerNomeEsatto(String nome,String cd_the)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(this);


        settaParametro(TableDao.XXXstringaEsatta, nome);
        settaParametro(TableDao.XXXcd_the, cd_the);
        tavola.executeCustom("countPerNomeEsatto");
        int n = conta(tavola);

        return n;
    }
    public int contaTerminePerNomeEsatto(String nome)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(this);

		settaParametro(TableDao.XXXstringaEsatta, nome);
		//settaParametro(TableDao.XXXcd_soggettario, cd_soggettario);
		tavola.executeCustom("countPerNomeEsatto");
		int n = conta(tavola);

		return n;
	}


    public TableDao cercaTerminePerNomeEsatto(
        String nome,
        String ordinamento,String cd_the)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(this);


        settaParametro(TableDao.XXXstringaEsatta, nome);
        settaParametro(TableDao.XXXcd_the, cd_the);
        tavola.executeCustom("selectPerNomeEsatto", ordinamento);
        return tavola;
    }
    public TableDao cercaTerminePerNomeEsatto(
            String nome,
            String ordinamento)
            throws IllegalArgumentException, InvocationTargetException, Exception {
            Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(this);


            settaParametro(TableDao.XXXstringaEsatta, nome);
            //settaParametro(TableDao.XXXcd_soggettario, cd_soggettario);
            tavola.executeCustom("selectPerNomeEsatto", ordinamento);
            return tavola;
        }


    /** Legge da una tavola il valore del COUNT(*) */
    public int conta(TableDao tavola) throws EccezioneDB {
    	return tavola.getCount();
    }

	/**
	 * Method creaDescrittore.
	 * @param id
	 * @param codiceSoggettario
	 * @param descrizioneSoggetto
	 * @param livello
	 * @throws InfrastructureException
	 */
	public boolean creaTermineThesauro(
		String id,
		String codiceThesauro,
		String descrizioneTermine,
		String livello,
		String user,
		String _condiviso) throws EccezioneDB,EccezioneSbnDiagnostico, InfrastructureException {
        Tb_termine_thesauro tb_termine_thesauro = new Tb_termine_thesauro();
        //tb_termine.setCD_LIVELLO(livello);
		if (codiceThesauro != null){
			String codiceThe = Decodificatore.getCd_tabella("Tb_termine_thesauro","cd_the", codiceThesauro.toUpperCase().trim());
			tb_termine_thesauro.setCD_THE(codiceThe);
		}
		if (descrizioneTermine != null)
			tb_termine_thesauro.setDS_TERMINE_THESAURO(descrizioneTermine);
		tb_termine_thesauro.setDID(id);
		tb_termine_thesauro.setFL_CANC(" ");

		tb_termine_thesauro.setKY_TERMINE_THESAURO(NormalizzaNomi.normalizzazioneGenerica(descrizioneTermine));

		tb_termine_thesauro.setNOTA_TERMINE_THESAURO("");
		tb_termine_thesauro.setUTE_INS(user);
		tb_termine_thesauro.setUTE_VAR(user);
        //tb_termine.setTP_FORMA_DES("A");
        // Timbro Condivisione
		tb_termine_thesauro.setFL_CONDIVISO(_condiviso);
		tb_termine_thesauro.setTS_CONDIVISO(TableDao.now());
		tb_termine_thesauro.setUTE_CONDIVISO(user);

        Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(tb_termine_thesauro);

        tavola.insert(tb_termine_thesauro);

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
    public List cercaRinviiTermini(String did, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_termini_termini termini_termini = new Vl_termini_termini();
        termini_termini.setDID_1(did);
        Vl_termini_terminiResult tavola = new Vl_termini_terminiResult(termini_termini);


        tavola.executeCustom("selectTerminiPerRinvii", ordinamento);
        List v = tavola.getElencoRisultati();

        return v;
    }


	/**
	 * Method cancellaDescrittore.
	 * @param descrittoreDaCancellare
	 * @param string
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException PUIPPO
	 */
	public void cancellaTermine_thesauro(
		Tb_termine_thesauro TermineDaCancellare,
		String user) throws IllegalArgumentException, InvocationTargetException, Exception {
		List vettoreDiTerminiDaCancellare= new ArrayList();
		//se il descrittore è in forma accettata vengono cancellati anche i descrittori in forma R
		if (TermineDaCancellare.getTP_FORMA_THE().equals("A")){
			//controlla se ci sono legami RT prendi il desc legato e cancella
			vettoreDiTerminiDaCancellare = controllaLegami(user,TermineDaCancellare);
		}
		TerminiTermini terminitermini = new TerminiTermini();
		//DescrittoreDescrittore descrittoreDescrittore = new DescrittoreDescrittore();
		terminitermini.cancellaLegame(user,TermineDaCancellare.getDID());
		cancellaTermini_Thesauro(user, vettoreDiTerminiDaCancellare);
		TermineDaCancellare.setUTE_VAR(user);
		TermineDaCancellare.setFL_CANC("S");
		Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(TermineDaCancellare);
		tavola.executeCustom("cancellaTermineThesauro");
	}


	/**
	 * Method cancellaDescrittori.
	 * @param user
	 * @param vettoreDiDescrittoriDaCancellare
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void cancellaTermini_Thesauro(
		String user,
		List vettoreDiTerminiDaCancellare) throws IllegalArgumentException, InvocationTargetException, Exception {
			Tb_termine_thesauro tb_termine;
		for (int i=0;i<vettoreDiTerminiDaCancellare.size();i++){
			tb_termine = (Tb_termine_thesauro) vettoreDiTerminiDaCancellare.get(i);
			tb_termine.setUTE_VAR(user);
			tb_termine.setFL_CANC("S");
			Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(tb_termine);


			tavola.executeCustom("cancellaTermineThesauro");

		}
	}

//
//
//		tavola.executeCustom("cancellaDescrittore");
//		public void cancellaDescrittore(
//				Tb_descrittore descrittoreDaCancellare,
//				String user) throws IllegalArgumentException, InvocationTargetException, Exception {
//				List vettoreDiDescrittoriDaCancellare= new ArrayList();
////		se il descrittore è in forma accettata vengono cancellati anche i descrittori in forma R
//				if (descrittoreDaCancellare.getTP_FORMA_DES().equals("A")){
//					//controlla se ci sono legami RT prendi il desc legato e cancella
//					vettoreDiDescrittoriDaCancellare = controllaLegami(user,descrittoreDaCancellare);
//				}
//
//				DescrittoreDescrittore descrittoreDescrittore = new DescrittoreDescrittore();
//				descrittoreDescrittore.cancellaLegame(user,descrittoreDaCancellare.getDID());
//				cancellaDescrittori(user, vettoreDiDescrittoriDaCancellare);
//				descrittoreDaCancellare.setUTE_VAR(user);
//				descrittoreDaCancellare.setFL_CANC("S");
//				Tb_descrittoreResult tavola = new Tb_descrittoreResult(descrittoreDaCancellare);
//
//
//				tavola.executeCustom("cancellaDescrittore");
//
//
//			}
//
//	}



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
		Tb_termine_thesauro termineDaCancellare) throws IllegalArgumentException, InvocationTargetException, Exception {
		Vl_termini_termini vl_termini_termini = new Vl_termini_termini();
		vl_termini_termini.setDID_1(termineDaCancellare.getDID());
		vl_termini_termini.setTIPO_COLL("RT");
		Vl_termini_terminiResult tavola = new Vl_termini_terminiResult(vl_termini_termini);


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
	 * @param codiceUtente
	 * @return boolean
	 * @throws InfrastructureException
	 */
	public boolean inserisceTermineThesauro(
		String t001,
		String a935,
		String b935,
		String c935,
		String codiceUtente,
		String livello,
		String formaNome,
		String _condiviso) throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        Tb_termine_thesauro tb_termine = new Tb_termine_thesauro();

        tb_termine.setDID(t001);
        tb_termine.setDS_TERMINE_THESAURO(a935);
        tb_termine.setKY_TERMINE_THESAURO(NormalizzaNomi.normalizzazioneGenerica(a935));
        tb_termine.setNOTA_TERMINE_THESAURO(b935);
        String cds = Decodificatore.getCd_tabella(
          "Tb_termine_thesauro",
          "cd_the",
          c935.toUpperCase());
        if (cds == null) {
            throw new EccezioneSbnDiagnostico(3088,"Codice errato");
        }
        tb_termine.setCD_THE(cds);
        tb_termine.setCD_LIVELLO(livello);
        tb_termine.setUTE_INS(codiceUtente);
        tb_termine.setUTE_VAR(codiceUtente);
        tb_termine.setFL_CANC(" ");
        tb_termine.setTP_FORMA_THE(formaNome);
        tb_termine.setFL_CONDIVISO(_condiviso);
		Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(tb_termine);


		tavola.insert(tb_termine);

		return true;
	}

	/**
	 * Method aggiornaDescrittore.
	 * @param tb_termine
	 * @param codiceUtente
	 * @throws InfrastructureException
	 */
	public void aggiornaTermineThesauro(
		Tb_termine_thesauro 	tb_termine,
		String 			codiceUtente) throws EccezioneDB, InfrastructureException {
		tb_termine.setUTE_VAR(codiceUtente);
		Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(tb_termine);


		tavola.update(tb_termine);

	}
	/**
	 * Method cercaSoggettiSimili.
	 * @param _t250
	 * @return TableDao
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public List cercaTerminiThesauroSimili(String id, A935 t250) throws IllegalArgumentException, InvocationTargetException, Exception {
        List vettoreSimili;
		String stringanormalizzata = "";
		if (t250 != null){
			if (t250.getC2_935() != null){
				String codiceThe = Decodificatore.getCd_tabella("Tb_termine_thesauro","cd_the", t250.getC2_935().toUpperCase().trim());
				if (codiceThe != null)
					setCD_THE(codiceThe);
				else throw new EccezioneSbnDiagnostico(3088,"codice Thesauro errato");
			}
			if (t250.getA_935() != null){
				// TERMINE UNICO non  va conposto
				// String ds_soggetto = componiSoggetto(t250);
				//normalizzare il ds_soggetto
				String ds_the = t250.getA_935();
				stringanormalizzata = NormalizzaNomi.normalizzazioneGenerica(ds_the);
				setDS_TERMINE_THESAURO(ds_the);
			}
		}
        String kyTermineThesauro = null;
        // VIENE MODIFICATA L'ASSEGNAZIONE DELLA kyCles1_s e kyCles2_s
        // tutto viene sopostato su kyCles1_s

//        if (stringanormalizzata.length() <= 50)
//            kyCles1_s = stringanormalizzata;
//        else {
//			kyCles1_s = stringanormalizzata.substring(0, 50);
//            if (stringanormalizzata.length() > 80)
//                kyCles2_s = stringanormalizzata.substring(50, 80);
//            else
//                kyCles2_s = stringanormalizzata.substring(50);
//        }
//		setKY_CLES1_S(kyCles1_s);
//		setKY_CLES2_S(kyCles2_s);
        kyTermineThesauro = stringanormalizzata;
        setKY_TERMINE_THESAURO(kyTermineThesauro);
        setDID(id);
        Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(this);
        tavola.executeCustom("selectSimili");
        vettoreSimili = tavola.getElencoRisultati();

		return vettoreSimili;
	}

	public Tb_termine_thesauro gestisceDescrizione( A935 t250, Tb_termine_thesauro tb_termine_thesauro, boolean simili) throws EccezioneSbnDiagnostico{
		String stringanormalizzata = null;
		if (t250 != null){
			if (t250.getC2_935() != null){
				String codiceThe = Decodificatore.getCd_tabella("Tb_termine_thesauro","cd_the", t250.getC2_935().toUpperCase().trim());
				if (codiceThe != null)
					setCD_THE(codiceThe);
				else throw new EccezioneSbnDiagnostico(3088,"codice Thesauro errato");
			}
			if (t250.getA_935() != null){
				// TERMINE UNICO non  va conposto
				// String ds_soggetto = componiSoggetto(t250);
				//normalizzare il ds_soggetto
				String ds_the = t250.getA_935();
				stringanormalizzata = NormalizzaNomi.normalizzazioneGenerica(ds_the);
				if (simili)
                    tb_termine_thesauro.setDS_TERMINE_THESAURO(ds_the.trim().toUpperCase());
				else
                    tb_termine_thesauro.setDS_TERMINE_THESAURO(ds_the);
			}
		}
		tb_termine_thesauro.setFL_CANC(" ");
		String kyCles1_s = null;
        String kyCles2_s = null;
        String kyTermineThesauro = null;
        kyTermineThesauro = stringanormalizzata;
        tb_termine_thesauro.setKY_TERMINE_THESAURO(kyTermineThesauro);

        return tb_termine_thesauro;
	}

    /**
     * Method eseguiUpdate per lo scambio forma.
     * @param autore
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void eseguiUpdateScambioForma(Tb_termine_thesauro termine1, Tb_termine_thesauro termine2) throws IllegalArgumentException, InvocationTargetException, Exception {

        Tb_termine_thesauroResult tavola = new Tb_termine_thesauroResult(termine2);
        tavola.executeCustom("updateScambioFormaSwitch");


        tavola = new Tb_termine_thesauroResult(termine1);
        tavola.executeCustomUpdate("updateScambioForma");

        tavola = new Tb_termine_thesauroResult(termine2);
        tavola.executeCustomUpdate("updateScambioForma");

    }
}
