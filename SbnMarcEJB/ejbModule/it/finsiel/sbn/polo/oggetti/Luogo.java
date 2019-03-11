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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_luogoResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_luogo_luoResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_luogo_titResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.NormalizzaNomi;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.orm.Tb_luogo;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tr_rep_luo;
import it.finsiel.sbn.polo.orm.viste.Vl_luogo_luo;
import it.finsiel.sbn.polo.orm.viste.Vl_luogo_tit;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;
/**
 * @author
 */
/**
 * Classe per la gestione delle operazioni complesse sulla tavola Tb_luogo.
 */
public class Luogo extends Tb_luogo {
    /**
	 * 
	 */
	private static final long serialVersionUID = 275305105192401291L;
	private static Category log = Category.getInstance("iccu.serversbnmarc.Luogo");
    private boolean filtriValorizzati;
    public Luogo() {

    }
    /**
     * Metodo per cercare il luogo con n. identificativo:
     * ricerca su Tb_luogo con LID
     * @throws InfrastructureException
     */
    public Tb_luogo cercaLuogoPerID(String id) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        Tb_luogo luogo = new Tb_luogo();
        luogo.setLID(id);
        Tb_luogoResult tavola = new Tb_luogoResult(luogo);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(luogo.leggiAllParametro()));
        List vettoreRisultati = tavola.getElencoRisultati();

        if (vettoreRisultati.size() > 0) {
            Tb_luogo elementoTrovato = (Tb_luogo) vettoreRisultati.get(0);
            return elementoTrovato;
        } else
            return null;
    }
    public Tb_luogo cercaLuogoPerEsistenza(String id) throws IllegalArgumentException, InvocationTargetException, Exception {
      Tb_luogo luogo = new Tb_luogo();
      luogo.setLID(id);
      Tb_luogoResult tavola = new Tb_luogoResult(luogo);


      tavola.executeCustom("selectPerEsistenza");
      List vettoreRisultati = tavola.getElencoRisultati();

      if (vettoreRisultati.size() > 0) {
        Tb_luogo elementoTrovato = (Tb_luogo) vettoreRisultati.get(0);
        return elementoTrovato;
      } else
        return null;
    }
    public TableDao cercaLuogoPerIDInTavola(String id) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        Tb_luogo luogo = new Tb_luogo();
        luogo.setLID(id);
        Tb_luogoResult tavola = new Tb_luogoResult(luogo);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(luogo.leggiAllParametro()));
        return tavola;
    }
    /**
     * Metodo per cercare il luogo con stringa nome in forma ESATTA.
     * ricerca su Tb_luogo
     * Input: nome, statusAuthority, formaNome
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaLuogoPerNome(CercaDatiAutType cerca, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_luogoResult tavola = new Tb_luogoResult(this);


        tavola.executeCustom("selectPerNome", ordinamento);
        return tavola;
    }
    /**
     * Metodo per cercare il luogo con stringa nome in forma LIKE.
     * ricerca su Tb_luogo
     * Input: nome, statusAuthority, formaNome
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaLuogoPerNomeLike(CercaDatiAutType cerca, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_luogoResult tavola = new Tb_luogoResult(this);


        //valorizzaFiltri(cerca);
        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return tavola;
    }
    public int contaLuogoPerNome(CercaDatiAutType cerca) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_luogoResult tavola = new Tb_luogoResult(this);


        valorizzaFiltri(cerca);
        tavola.executeCustom("countPerNome");
        int n = conta(tavola);

        return n;
    }
    public int contaLuogoPerNomeLike(CercaDatiAutType cerca) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_luogoResult tavola = new Tb_luogoResult(this);


        valorizzaFiltri(cerca);
        tavola.executeCustom("countPerNomeLike");
        int n = conta(tavola);

        return n;
    }

    public int contaLuogoPerTitolo(String idArrivo, CercaDatiAutType cerca) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_luogo_tit tit_luo = new Vl_luogo_tit();
        tit_luo.setBID(idArrivo);
        valorizzaFiltri(tit_luo, cerca);
        Vl_luogo_titResult tavola = new Vl_luogo_titResult(tit_luo);


        tavola.executeCustom("countLuogoPerTitolo");
        int n = conta(tavola);

        return n;
    }
    /** Esegue una ricerca per i luoghi legati ad un documento
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaLuogoPerTitolo(String id_titolo, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_luogo_tit tit_tit = new Vl_luogo_tit();
        tit_tit.setBID(id_titolo);
        Vl_luogo_titResult tavola = new Vl_luogo_titResult(tit_tit);


        tavola.executeCustom("selectLuogoPerTitolo", ordinamento);
        return tavola;
    }
    /**
     * Metodo per leggere i rinvii di un luogo
     * Input: LID
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List cercaRinviiLuogo(String lid, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_luogo_luo luogo = new Vl_luogo_luo();
        luogo.setLID_1(lid);
        Vl_luogo_luoResult tavola = new Vl_luogo_luoResult(luogo);


        tavola.executeCustom("selectLuogoPerRinvii", ordinamento);
        List v = tavola.getElencoRisultati();

        return v;
    }
    /**
     * metodo che inserisce un record in tb_luogo.
     *
     * imposta ts_ins e ts_var con il timestamp del sistema.
     * imposta ute_ins e ute_var con l'utente che ha attivato la richiesta di
     * creazione.
     * se ID_luogo non è valorizzato recupera il valore dalla gestione dei progressivi
     * sul DB.
     *
     *
     * @return Boolean
     * @throws InfrastructureException
     */
    // evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi
    // nota informativa , nota catalogatore e legame a repertor
    public void inserisceLuogo(String id_luogo, String polo, String user, SbnFormaNome tipoForma, String cd_paese,
    		String cd_livello, String ds_luogo, String nota, String notaCat)
        throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
    	Tb_luogo tb_luogo = new Tb_luogo();
    	tb_luogo.setCD_LIVELLO(cd_livello);
        if (ds_luogo != null) {
        	tb_luogo.setDS_LUOGO(ds_luogo);
        	tb_luogo.setKY_NORM_LUOGO(NormalizzaNomi.normalizzazioneGenerica(ds_luogo));
        }
        tb_luogo.setLID(id_luogo);
        tb_luogo.setCD_PAESE(cd_paese);
        tb_luogo.setKY_LUOGO(" ");
        tb_luogo.setNOTA_LUOGO(nota);
        tb_luogo.setNOTA_CATALOGATORE(notaCat);
        if (tipoForma == null)
        	tb_luogo.setTP_FORMA(" ");
        else
        	tb_luogo.setTP_FORMA(tipoForma.toString());
        tb_luogo.setUTE_INS(user);
        tb_luogo.setUTE_VAR(user);
        tb_luogo.setFL_CANC(" ");
        Tb_luogoResult result_luogo = new Tb_luogoResult(tb_luogo);
        result_luogo.insert(tb_luogo);

    }
    /**
     * metodo che fa update in tb_luogo.
     *
     * imposta ts_var con il timestamp del sistema.
     * imposta ute_var con l'utente che ha attivato la richiesta di modifica
     *
     * @return Boolean
     * @throws InfrastructureException
     */
    public boolean modificaLuogo(String id_luogo, String polo, String user, SbnFormaNome tipoForma, String cd_livello, String ds_luogo) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        boolean esito = true;
        setCD_LIVELLO(cd_livello);
        if (ds_luogo != null) {
            setDS_LUOGO(ds_luogo);
            setKY_NORM_LUOGO(NormalizzaNomi.normalizzazioneGenerica(ds_luogo));
        }
        setLID(id_luogo);
        setKY_LUOGO(" ");
        if (tipoForma == null) {
            setTP_FORMA(" ");
        } else {
            setTP_FORMA(tipoForma.toString());
        }
        setUTE_VAR(user);
        setFL_CANC(" ");
        SbnDatavar data_operazione = new SbnDatavar(System.currentTimeMillis());
        setTS_VAR(ConverterDate.SbnDataVarToDate(data_operazione));
        Tb_luogoResult result_luogo = new Tb_luogoResult(this);

        return result_luogo.update(this);

    }
    public boolean updateLuogo(Tb_luogo tb_luogo) throws EccezioneDB, InfrastructureException {
        Tb_luogoResult tb_luogoResult = new Tb_luogoResult(tb_luogo);
        return tb_luogoResult.update(tb_luogo);

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
    /** Valorizza i filtri in base al contenuto del CercaLuogoType */
    public void valorizzaFiltri(CercaDatiAutType cerca) {
        valorizzaFiltri(this, cerca);
    }
    /** Valorizza i filtri di luogo in base al contenuto del CercaLuogoType */
    public Tb_luogo valorizzaFiltri(Tb_luogo luogo, CercaDatiAutType cerca) {
        filtriValorizzati = true;
        if (cerca == null)
            return luogo;
        if (cerca.getLivelloAut_Da() != null)
            luogo.settaParametro(TableDao.XXXlivello_aut_da,
                Decodificatore.livelloSogliaDa(cerca.getLivelloAut_Da().toString()));
        if (cerca.getLivelloAut_A() != null)
            luogo.settaParametro(TableDao.XXXlivello_aut_a, cerca.getLivelloAut_A().toString());
        if (cerca.getFormaNome() != null)
            luogo.setTP_FORMA(cerca.getFormaNome().toString());
        if (cerca.getT005_Range() != null) {
            int filtro = 1;
            if (cerca.getT005_Range().hasTipoFiltroDate())
                cerca.getT005_Range().getTipoFiltroDate();
            if (filtro == 1) {
                luogo.settaParametro(TableDao.XXXdata_var_Da, cerca.getT005_Range().getDataDa().toString());
                luogo.settaParametro(TableDao.XXXdata_var_A, cerca.getT005_Range().getDataA().toString());
            } else {
                luogo.settaParametro(TableDao.XXXdata_ins_Da, cerca.getT005_Range().getDataDa().toString());
                luogo.settaParametro(TableDao.XXXdata_ins_A, cerca.getT005_Range().getDataA().toString());
            }
        }
        return luogo;
    }
    /**
     * Method cancellaLuogo.
     * @param idArrivo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void cancellaLuogo(String lid, String user, TimestampHash timeHash) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_luogo luogoDaCancellare = new Tb_luogo();
        luogoDaCancellare = cercaLuogoPerID(lid);
        luogoDaCancellare.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tb_luogo", luogoDaCancellare.getLID())));
        cancellaLuogo(luogoDaCancellare, user, timeHash);
    }
    public List cercaAltriRinviiLuogo(String lid, String lidDiverso, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_luogo_luo luogo = new Vl_luogo_luo();
        luogo.setLID_1(lid);
        luogo.setLID(lidDiverso);
        Vl_luogo_luoResult tavola = new Vl_luogo_luoResult(luogo);


        tavola.executeCustom("selectLuogoPerAltriRinvii", ordinamento);
        List v = tavola.getElencoRisultati();

        return v;
    }
    public List cercaLuoghiSimili(String nome, String t001, String ds_luogo) throws IllegalArgumentException, InvocationTargetException, Exception {
        setDS_LUOGO(ds_luogo);
    	setKY_NORM_LUOGO(nome);
        settaParametro(TableDao.XXXidDaModificare, t001);
        Tb_luogoResult tavola = new Tb_luogoResult(this);


        tavola.executeCustom("selectLuoghiSimili");
        List v = tavola.getElencoRisultati();

        return v;
    }
    /**
     * Method cancellaLuogo.
     * @param luogoDaCancellare
     * @param string
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void cancellaLuogo(Tb_luogo luogoDaCancellare, String user, TimestampHash timeHash) throws IllegalArgumentException, InvocationTargetException, Exception {
        //prima cancello tutti i legami luogo-luogo
        LuogoLuogo luogoLuogo = new LuogoLuogo();
        luogoLuogo.cancellaLegame(user, luogoDaCancellare.getLID(), timeHash);
        luogoDaCancellare.setUTE_VAR(user);
        luogoDaCancellare.setFL_CANC("S");
        luogoDaCancellare.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tb_luogo", luogoDaCancellare.getLID())));
        Tb_luogoResult tavola = new Tb_luogoResult(luogoDaCancellare);
        tavola.executeCustom("cancellaLuogo");
    }
    /**
     * Method cercaLuogoSimile.
     * @param nome
     * @return TableDao
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public List cercaLuogoSimile(String nome, String ds_luogo) throws IllegalArgumentException, InvocationTargetException, Exception {
        setDS_LUOGO(ds_luogo);
    	setKY_NORM_LUOGO(nome);
        Tb_luogoResult tavola = new Tb_luogoResult(this);


        tavola.executeCustom("selectSimili");
        List v = tavola.getElencoRisultati();

        return v;
    }
    public void updateVersione(String id, String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        setLID(id);
        setUTE_VAR(ute_var);
        Tb_luogoResult tb_luogoResult = new Tb_luogoResult(this);
        tb_luogoResult.executeCustom("updateVersione");
    }

    // evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi
    // nota informativa , nota catalogatore e legame a repertori
  //almaviva4 04/05/2015 evolutiva creazione legame a repertorio
    public void creaLegame(LegameElementoAutType	legameElementoAutType, String idPartenza, String user) throws Exception{
   	if (legameElementoAutType.getTipoAuthority().toString().equals("RE")){
		RepertorioLuogo repLuo = new RepertorioLuogo();
//14/05
        Tb_repertorio tb_repertorio;
        Repertorio authority = new Repertorio();
        tb_repertorio = authority.cercaRepertorioPerCdSig(legameElementoAutType.getIdArrivo());

		if (!repLuo.esisteLegame(idPartenza, (int) tb_repertorio.getID_REPERTORIO())){

//14/05
//		if (!repLuo.esisteLegame(idPartenza,legameElementoAutType.getIdArrivo())){
//			if (!repLuo.cercaPerCitazioneRepertorio(legameElementoAutType.getCitazione(),legameElementoAutType.getIdArrivo())){
				Tr_rep_luo tr_rep_luo = new Tr_rep_luo();
				tr_rep_luo.setFL_CANC(" ");
				tr_rep_luo.setLID(idPartenza);
				tr_rep_luo.setNOTA_REP_LUO(legameElementoAutType.getNoteLegame());
				Repertorio repertorio = new Repertorio();
//				Tb_repertorio tb_repertorio;
				tb_repertorio = repertorio.cercaRepertorioPerCdSigTipoRepertorioLU(legameElementoAutType.getIdArrivo());
				if (tb_repertorio == null)
					throw new EccezioneSbnDiagnostico(3023); //repertorio inesistente
				tr_rep_luo.setID_REPERTORIO(tb_repertorio.getID_REPERTORIO());
				tr_rep_luo.setUTE_INS(user);
				tr_rep_luo.setUTE_VAR(user);
				repLuo.inserisciRepertorioLuogo(tr_rep_luo);
//			}else throw new EccezioneSbnDiagnostico(3030,"Legame con repertorio esistente");
		}else throw new EccezioneSbnDiagnostico(3030); //Legame con repertorio esistente
   		}
	}

//    public void cancellaLegame(
//			LegameElementoAutType	legameElementoAutType,
//			String 					idPartenza,
//			String 					user,
//			TimestampHash			timeHash) throws EccezioneDB{
//		       	if (legameElementoAutType.getTipoAuthority().toString().equals("RE")){
//					RepertorioLuogo repLuo = new RepertorioLuogo();
//					Tr_rep_luo tr_rep_luo = new Tr_rep_luo();
//					tr_rep_luo.setFL_CANC("S");
//					tr_rep_luo.setLID(idPartenza);
//					Tb_repertorio tb_repertorio;
//		        	Repertorio authority =new Repertorio();
//		        	tb_repertorio = authority.cercaRepertorioPerCdSig(legameElementoAutType.getIdArrivo());
//					tr_rep_luo.setID_REPERTORIO(tb_repertorio.getID_REPERTORIO());
//		            tr_rep_luo.setTS_VAR(
//		                            timeHash.getTimestamp("Tr_rep_luo",
//		                            tr_rep_luo.getID_REPERTORIO() + tr_rep_luo.getLID()));
//					repLuo.cancellaRepertorioLuogo(tr_rep_luo,user);
//				}
//			}

//	public void modificaLegame(LegameElementoAutType	legameElementoAutType, String idPartenza, String user) throws EccezioneDB, EccezioneSbnDiagnostico{
//       	if (legameElementoAutType.getTipoAuthority().toString().equals("RE")){
//			RepertorioLuogo repLuo = new RepertorioLuogo();
//				Tr_rep_luo tr_rep_luo = new Tr_rep_luo();
//				tr_rep_luo.setFL_CANC(" ");
//				tr_rep_luo.setLID(idPartenza);
//				tr_rep_luo.setID_REPERTORIO(legameElementoAutType.getIdArrivo());
//				tr_rep_luo.setUTE_VAR(user);
//				if (legameElementoAutType.getNoteLegame() != null)
//					tr_rep_luo.setNOTA_REP_LUO(legameElementoAutType.getNoteLegame());
//				Tr_rep_luoResult tr_rep_luoResult = new Tr_rep_luoResult(tr_rep_luo);
//				tr_rep_luoResult.setConnessione();
//				tr_rep_luoResult.openStatement();
//				tr_rep_luoResult.update();
//				tr_rep_luoResult.closeStatement();
//				log.info("inserito il legame con il repertorio");
//			}else throw new EccezioneSbnDiagnostico(3030,"Legame con repertorio esistente");
//		}
}
