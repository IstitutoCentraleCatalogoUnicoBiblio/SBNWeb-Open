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
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_rep_luoResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_rep_marResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tr_rep_luo;
import it.finsiel.sbn.polo.orm.Tr_rep_mar;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

public class RepertorioLuogo extends Tr_rep_luo {

	private static final long serialVersionUID = 7117111157980800272L;

	private static Category log = Category.getInstance(RepertorioLuogo.class);
	private boolean filtriValorizzati;

	public RepertorioLuogo(){
		super();
   }

    public void valorizzaFiltri(CercaDatiAutType cerca) {
        valorizzaFiltri(cerca);
	}

    /** Valorizza i filtri */
    public Tr_rep_luo valorizzaFiltri(Tr_rep_luo tr_rep_luo, CercaDatiAutType cerca) {
        filtriValorizzati = true;
        if (cerca == null)
            return tr_rep_luo;
        if (cerca.getT005_Range() != null) {
        	int filtro = cerca.getT005_Range().getTipoFiltroDate();
        	if (filtro < 2){
	            tr_rep_luo.settaParametro(
	                "data_var_Da",
	                cerca.getT005_Range().getDataDa().toString());
	            tr_rep_luo.settaParametro(
	                "data_var_A",
	                cerca.getT005_Range().getDataA().toString());
        	} else  if (filtro == 2){
	            tr_rep_luo.settaParametro(
	                "data_ins_Da",
	                cerca.getT005_Range().getDataDa().toString());
	            tr_rep_luo.settaParametro(
	                "data_ins_A",
	                cerca.getT005_Range().getDataA().toString());
        	}
        }
        return tr_rep_luo;
    }


//
	public boolean esisteLegame(String lid, int idRepertorio) throws Exception{
        setLID(lid);
        setID_REPERTORIO(idRepertorio);
   		Tr_rep_luoResult tr_rep_luoResult = new Tr_rep_luoResult(this);

   		// LARA: PROVA PER VERIFICARE SE FUNZIONA
        //tr_rep_luoResult.executeCustom("selectEsistenza");
   		tr_rep_luoResult.executeCustom("selectPerKey");
        List v = tr_rep_luoResult.getElencoRisultati();
		return v.size()>0;

	}

	public List cercaCitazioniInRepertori(String lid, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        setLID(lid);
        Tr_rep_luoResult tavola = new Tr_rep_luoResult(this);
        tavola.executeCustom("selectPerLuogo", ordinamento);
        List v = tavola.getElencoRisultati();
        return v;
    }


//	public boolean cercaPerCitazioneRepertorio(int citazione, String sig_repertorio) throws EccezioneDB{
//		Vl_repertorio_luo vl = new Vl_repertorio_luo();
//		vl.setCd_sig_repertorio(sig_repertorio);
//		vl.setProgr_repertorio(citazione);
//		Vl_repertorio_luoResult vl_repertorio_luoResult = new Vl_repertorio_luoResult(vl);
//		vl_repertorio_luoResult.setConnessione(_db_conn);
//		vl_repertorio_luoResult.openStatement();
//		vl_repertorio_luoResult.executeCustom("selectPerRepertorioCitazione");
//		List v = vl_repertorio_luoResult.getElencoRisultati();
//		vl_repertorio_luoResult.closeStatement();
//		return v.size()>0;
//	}

//	public List cercaMarcaPerCitazioneRepertorio(int citazione, String sig_repertorio) throws EccezioneDB{
//		Vl_repertorio_mar vl_rep_mar = new Vl_repertorio_mar();
//		vl_rep_mar.setCd_sig_repertorio(sig_repertorio);
//		vl_rep_mar.setProgr_repertorio(citazione);
//		Vl_repertorio_marResult vl_repertorio_marResult = new Vl_repertorio_marResult(vl_rep_mar);
//        vl_repertorio_marResult.setConnessione(_db_conn);
//        vl_repertorio_marResult.openStatement();
//        vl_repertorio_marResult.executeCustom("selectPerRepertorioCitazione");
//        List vectorResult = vl_repertorio_marResult.getElencoRisultati();
//        vl_repertorio_marResult.closeStatement();
//        return vectorResult;
//	}
//
//
//	public DBTavola cercaMarcaPerRepertorio(String idRep) throws EccezioneDB{
//		setId_repertorio(idRep);
//        Tr_rep_marResult tr_marResult = new Tr_rep_marResult(this);
//        tr_marResult.setConnessione(_db_conn);
//        tr_marResult.openStatement();
//        tr_marResult.executeCustom("selectMarcaPerRepertorio");
//        return tr_marResult;
//	}
//
//
//   /**
//    * Metodo che ricerca le citazioni nei repertori di una marca:
//    * utilizza una vista in join tra tb_repertorio,tr_rep_mar,tb_marca
//    * Input: citazione e repertorio
//    * Output= elenco delle citazioni in repertori della marca
//    */
//    public int contaLegamiMarcaPerCitRep(int citazione, String idRep, String mid) throws EccezioneDB {
//		Vl_repertorio_mar vl_rep_mar = new Vl_repertorio_mar();
//		vl_rep_mar.setCd_sig_repertorio(idRep);
//        vl_rep_mar.setMid(mid);
//		vl_rep_mar.setProgr_repertorio(citazione);
//		Vl_repertorio_marResult vl_repertorio_marResult = new Vl_repertorio_marResult(vl_rep_mar);
//        vl_repertorio_marResult.setConnessione(_db_conn);
//        vl_repertorio_marResult.openStatement();
//        vl_repertorio_marResult.executeCustom("countRepertorioPerCitazione");
//        int n = conta(vl_repertorio_marResult);
//        vl_repertorio_marResult.closeStatement();
//        return n;
//    }
//
//	public int countRepertorioPerCitazione(String mid) throws EccezioneDB {
//		Vl_repertorio_mar vl_rep_mar = new Vl_repertorio_mar();
//		vl_rep_mar.setMid(mid);
//		Vl_repertorio_marResult vl_repertorio_marResult = new Vl_repertorio_marResult(vl_rep_mar);
//		vl_repertorio_marResult.setConnessione(_db_conn);
//		vl_repertorio_marResult.openStatement();
//		vl_repertorio_marResult.executeCustom("countRepertorioPerCitazione");
//		int n = conta(vl_repertorio_marResult);
//		vl_repertorio_marResult.closeStatement();
//
//		return n;
//	}
//

    private int conta(TableDao tavola) throws EccezioneDB {
    	return tavola.getCount();
    }

    public void validaPerCreaLegame(String idLuogo, ArrivoLegame legame, LegamiType[] legami)
    throws IllegalArgumentException, InvocationTargetException, Exception {
    LegameElementoAutType legEl = legame.getLegameElementoAut();

    if (!(legEl.getTipoLegame().equals(SbnLegameAut.valueOf("810"))
        || legEl.getTipoLegame().equals(SbnLegameAut.valueOf("815"))))
        throw new EccezioneSbnDiagnostico(3031); //"Legame con repertorio non valido"
//    Tb_luogo tb_luogo = new Tb_luogo();
    Repertorio repertorio = new Repertorio();

    if (repertorio.cercaRepertorioPerCdSig(legEl.getIdArrivo()) == null)
        throw new EccezioneSbnDiagnostico(3023); //"Repertorio da collegare non esistente"
    if (legami != null) {
        boolean cancellato = false;
        for (int i = 0; !cancellato && i < legami.length; i++) {
            LegamiType leg = legami[i];
            if (leg.getTipoOperazione().equals(SbnTipoOperazione.CANCELLA))
                for (int j = 0; !cancellato && j < leg.getArrivoLegameCount(); j++)
                    if (leg.getArrivoLegame(j).getLegameElementoAut() != null)
                        if (leg
                            .getArrivoLegame(j)
                            .getLegameElementoAut()
                            .getTipoAuthority()
                            .equals(SbnAuthority.RE)
                            && leg.getArrivoLegame(j).getLegameElementoAut().getIdArrivo().equals(
                                legEl.getIdArrivo()))
                            cancellato = true;
        }
        if (!cancellato)
            if (cercaLegame(idLuogo, legEl.getIdArrivo()).size() > 0)
                throw new EccezioneSbnDiagnostico(3030, "Legame con repertorio già esistente");
    }
}

    private List cercaLegame(String idLuogo, String idRep) throws IllegalArgumentException, InvocationTargetException, Exception {
        Repertorio repDB = new Repertorio();
        Tb_repertorio rep = repDB.cercaRepertorioPerCdSig(idRep);
//
        Tr_rep_luo vl = new Tr_rep_luo();
        vl.setID_REPERTORIO(rep.getID_REPERTORIO());
        vl.setLID(idLuogo);
        //
//        setLid(idLuogo);
//        setId_repertorio(rep.getId_repertorio());
        Tr_rep_luoResult tr_luoResult = new Tr_rep_luoResult(vl);
        tr_luoResult.valorizzaElencoRisultati(tr_luoResult.selectPerKey(this.leggiAllParametro()));
        List v = tr_luoResult.getElencoRisultati();
        return v;

    }

	/**
	 * Metodo contaCitazioni.
	 * restituisce il numero di citazioni in repertorio  di una marca
	 * INPUT: mid, cd_repertorio
	 * @param String
	 * @return int
	 */
//	private int contaCitazioni(String mid, String cd_sig_repertorio) throws EccezioneDB {
//		Vl_repertorio_mar vl_rep_mar = new Vl_repertorio_mar();
//		vl_rep_mar.setMid(mid);
//		vl_rep_mar.setCd_sig_repertorio(cd_sig_repertorio);
//		Vl_repertorio_marResult tavola = new Vl_repertorio_marResult(vl_rep_mar);
//        tavola.setConnessione(_db_conn);
//        tavola.openStatement();
//        tavola.executeCustom("contaCitazioniInRepertorio");

/*        setMid(mid);
        setId_repertorio(cd_sig_repertorio);
        Tr_rep_marResult tavola = new Tr_rep_marResult(this);
        tavola.setConnessione(this._db_conn);
        tavola.openStatement();
        tavola.executeCustom("contaCitazioniInRepertorio");
*/
//        int n = conta(tavola);
//        tavola.closeStatement();
//        return n;
//	}



   /**
    * crea un record nella tavola tr_rep_luo
    * prima si controlla l'esistenza del record dul db, qualora fosse presente con fl_canc = 'S'
    * verrebbe ripristinato
 * @throws Exception
    */
	public boolean inserisciRepertorioLuogo(Tr_rep_luo tr_rep_luo) throws Exception{
   		Tr_rep_luoResult tr_rep_luoResult = new Tr_rep_luoResult(tr_rep_luo);

   		tr_rep_luo.setFL_CANC(" ");
		if (!controllaEsistenzaLegame(tr_rep_luo)){
			tr_rep_luo.setFL_CANC(" ");
	   		return tr_rep_luoResult.insert(tr_rep_luo);
		} else {
			tr_rep_luo.setFL_CANC(" ");
	   		return tr_rep_luoResult.update(tr_rep_luo);
		}

	}


	private boolean controllaEsistenzaLegame(Tr_rep_luo legame) throws Exception {
        Tr_rep_luoResult tavola = new Tr_rep_luoResult(legame);

        tavola.executeCustom("selectPerKeyCancellato");
        List vec = tavola.getElencoRisultati();

        return vec.size() > 0;
	}

	/**
	 * Method cancellaRepertorioMarca.
	 * @param tr_rep_mar
	 * @throws InfrastructureException
	 */
	public boolean cancellaRepertorioLuogo(Tr_rep_luo tr_rep_luo, String user) throws EccezioneDB, InfrastructureException {
		tr_rep_luo.setFL_CANC("S");
		tr_rep_luo.setUTE_VAR(user);
   		Tr_rep_luoResult tr_rep_luoResult = new Tr_rep_luoResult(tr_rep_luo);
   		return tr_rep_luoResult.update(tr_rep_luo);
	}

    public void cancellaLegamiRepertorio(String idRepertorio, String user) throws Exception {
    	setID_REPERTORIO(Long.parseLong(idRepertorio));
        setUTE_VAR(user);
        Tr_rep_luoResult tavola = new Tr_rep_luoResult(this);
        tavola.executeCustom("cancellaLegamiRepertorio");
    }

	/**
	 * Method spostaLegami.
	 * @param idPartenza
	 * @param idArrivo
	 * @param user
	 * @throws Exception
	 */
	public void spostaLegami(
		String idPartenza,
		String idArrivo,
		String user) throws Exception {
        Tr_rep_mar legame = new Tr_rep_mar();
        legame.settaParametro("idPartenza", idPartenza);
        legame.settaParametro("idArrivo", idArrivo);
        legame.setUTE_VAR(user);
        Tr_rep_marResult tabella = new Tr_rep_marResult(legame);
        tabella.executeCustom("spostaLegami");
	}

	public void validaPerModificaLegame(String lid, ArrivoLegame legame,
			SbnTipoOperazione operazione, LegamiType[] legami,
			TimestampHash timeHash) throws IllegalArgumentException,
			InvocationTargetException, Exception {
		if (operazione == null)
			throw new EccezioneSbnDiagnostico(3102); //Tipo operazione sul legame non specificato
		if (operazione.equals(SbnTipoOperazione.CREA))
			validaPerCreaLegame(lid, legame, legami);
		else if (operazione.equals(SbnTipoOperazione.CANCELLA)
				|| operazione.equals(SbnTipoOperazione.MODIFICA))
			validaPerModificaLegame(lid, legame, timeHash);
	}

	public void validaPerModificaLegame(String lid, ArrivoLegame legame,
			TimestampHash timeHash) throws IllegalArgumentException,
			InvocationTargetException, Exception {

		Repertorio repertorio = new Repertorio();
		LegameElementoAutType legamedoc = legame.getLegameElementoAut();
		Tb_repertorio tbm = repertorio.cercaRepertorioPerCdSig(legamedoc.getIdArrivo());
		if (tbm == null)
			throw new EccezioneSbnDiagnostico(3023); // "repertorio non esistente"

		List v = cercaLegame(lid, legamedoc.getIdArrivo());
		if (v.size() == 0)
			// Non esiste il legame
			throw new EccezioneSbnDiagnostico(3029); // "Legame non esistente"
	}

    public boolean cancellaLuogoRepertorio(Tr_rep_luo tr_rep_luo) throws EccezioneDB, InfrastructureException {
        tr_rep_luo.setFL_CANC("S");
        Tr_rep_luoResult t_repluo = new Tr_rep_luoResult(tr_rep_luo);
        return t_repluo.update(tr_rep_luo);
    }

    public boolean modificaLuogoRepertorio(Tr_rep_luo tr_rep_luo) throws EccezioneDB, InfrastructureException {
        tr_rep_luo.setFL_CANC(" ");
        Tr_rep_luoResult t_repluo = new Tr_rep_luoResult(tr_rep_luo);
        return t_repluo.update(tr_rep_luo);
    }

}
