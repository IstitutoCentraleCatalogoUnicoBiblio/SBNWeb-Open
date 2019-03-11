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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_luogoResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_luo_luoResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_luogo_luoResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.LegamiLuogo;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.orm.Tb_luogo;
import it.finsiel.sbn.polo.orm.Tr_luo_luo;
import it.finsiel.sbn.polo.orm.viste.Vl_luogo_luo;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author
 */


/**
 * Classe per la gestione delle operazioni complesse sulla tavola Tr_luo_luo.
 */
public class LuogoLuogo extends Tr_luo_luo {


	private static final long serialVersionUID = -3764481890406730542L;


	public LuogoLuogo() {


   }

//	public LuogoLuogo() {
//   }


   /**
    * metodo che inserisce un record in tr_luo_luo.
    *
    * imposta ts_ins e ts_var con il timestamp del sistema.
    * imposta ute_ins e ute_var con l'utente che ha attivato la richiesta di
    * creazione.
    *
    * se tipoLegame = 5XX: tp_legame = '4', ID_luogo_base = T001, ID_luogo_coll =
    * idArrivo
    * se tipoLegame = 4XX: tp_legame = '8', se tipoForma = 'A' ID_luogo_base = T001,
    * ID_luogo_coll = idArrivo; se  tipoForma = 'R' ID_luogo_coll = T001,
    * ID_luogo_base = idArrivo
    * modifica Tb_luogo.ts_var = Tr_luo_luo.ts_var  in Tb_luogo dei due ID_luogo_coll
    * e ID_luogo_base (utilizza un metodo di LuogoDB) NB: ricordarsi di aggiornare
    * T005 della classe utilizzata per la response (oppure occorre rileggere TB_luogo
    * nella preparazione della response).
    *
    * Mentre inserisce il legame nella tabella tr_luogo_luo deve essere aggiornato
    * anche il timeHash in quanto dopo potrebbe esserci una fase di cancellazione
 * @throws InfrastructureException
    */
	public void inserisceInTr_luo_luo(
	String utente,
	String t001,
	SbnFormaNome tipoForma,
	String tipoLegame,
	String idArrivo) throws  EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
		Tr_luo_luo 	tr_luo_luo = 	new Tr_luo_luo();
		Tb_luogo	tb_luogo1 =		new Tb_luogo();
		Tb_luogo	tb_luogo2 =		new Tb_luogo();
		String tp_legame = null;

		if (tipoLegame.equals("5XX")){
			tr_luo_luo.setLID_BASE(t001);
			tr_luo_luo.setLID_COLL(idArrivo);
			tr_luo_luo.setTP_LEGAME("4");
			tp_legame = "4";
		}else if (tipoLegame.equals("4XX")){
			tr_luo_luo.setTP_LEGAME("8");
			if (tipoForma.toString().equals("A")){
				tr_luo_luo.setLID_BASE(t001);
				tr_luo_luo.setLID_COLL(idArrivo);
			}
			if (tipoForma.toString().equals("R")){
				tr_luo_luo.setLID_COLL(t001);
				tr_luo_luo.setLID_BASE(idArrivo);
			}
			tp_legame = "8";
		}
		tr_luo_luo.setFL_CANC(" ");
		tr_luo_luo.setUTE_INS(utente);
		tr_luo_luo.setUTE_VAR(utente);

		Tr_luo_luoResult	tr_luogoResult = 	new Tr_luo_luoResult(tr_luo_luo);

		//inserisco una riga in Tr_luo_luo dopo aver controllato che se sto inserendo:
		//legame di tipo 8 --> base=A + coll=R
		//legame di tipo 4 --> base=A + coll=A
		Luogo cercaLuogo = new Luogo();
		tb_luogo1 = cercaLuogo.cercaLuogoPerID(t001);
		String forma1=tb_luogo1.getTP_FORMA();
		tb_luogo2 = cercaLuogo.cercaLuogoPerID(idArrivo);
		String forma2=tb_luogo2.getTP_FORMA();

		if (
		(tp_legame.equals("8"))&((forma1.equals("R")&forma2.equals("A"))|(forma2.equals("R")&forma1.equals("A")))|
		(tp_legame.equals("4"))&((forma1.equals("A")&forma2.equals("A")))
		){


			tr_luogoResult.insert(tr_luo_luo);

			//aggiornaLegami(t001,idArrivo);
			LegamiLuogo legamiLuogo = new LegamiLuogo(tr_luo_luo.getUTE_INS(),tr_luo_luo.getUTE_VAR(),"4");
		}

//

		//modifico Tb_luogo.ts_var in entrambe le tabelle: quella riferita a idArrivo e quella riferita a T001
		//CercaLuogo 		cercaLuogo = new CercaLuogo();
		//tb_luogo1 = cercaLuogo.cercaLuogoPerID(t001);
		tb_luogo1.setTS_VAR(tr_luo_luo.getTS_VAR());
		Tb_luogoResult	tb_luogoResult1	= new Tb_luogoResult(tb_luogo1);


		tb_luogoResult1.update(tb_luogo1);


		//tb_luogo2 = cercaLuogo.cercaLuogoPerID(idArrivo);
		tb_luogo2.setTS_VAR(tr_luo_luo.getTS_VAR());
		Tb_luogoResult	tb_luogoResult2	= new Tb_luogoResult(tb_luogo2);


		tb_luogoResult2.update(tb_luogo2);

	}
  public void inserisciTrLuoLuo(Tr_luo_luo ll) throws EccezioneDB, InfrastructureException {
    Tr_luo_luoResult  tr_luogoResult =  new Tr_luo_luoResult(ll);


    tr_luogoResult.insert(ll);

  }

  /** cancella il legame in tr_luo_luo dove:
	 *  lid_base = lid1 | lid2
	 *  lid_coll = lid1 | lid2
 * @throws Exception
 * @throws InvocationTargetException
 * @throws IllegalArgumentException
	 */
	public void cancellaLegame(
	String user,
	String lid1,
	String lid2,
	TimestampHash timeHash) throws IllegalArgumentException, InvocationTargetException, Exception{
		Tr_luo_luo tr_luo_luo = new Tr_luo_luo();
		tr_luo_luo.setLID_BASE(lid1);
		tr_luo_luo.setLID_COLL(lid2);
		String time;
		time = timeHash.getTimestamp("Tr_luo_luo",lid1+lid2);
		if (time == null)
			time = timeHash.getTimestamp("Tr_luo_luo",lid2+lid1);
        //tr_luo_luo.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("tr_luo_luo", lid1 + lid2)));

		tr_luo_luo.setTS_VAR(ConverterDate.SbnDataVarToDate(time));
		tr_luo_luo.setUTE_VAR(user);
		Tr_luo_luoResult tr_luo_luoResult = new Tr_luo_luoResult(tr_luo_luo);
        tr_luo_luoResult.executeCustom("cancellaLegameLuogoLuogo");
	}


	/**
	 * questo metodo riceve in input un lid e cancella tutti i legami presenti in
	 * tr_luo_luo, qualora avesse legami di tipo '8' vengono cancellati anche questi
	 * e di conseguenza anche il luogo rinviato
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 *
	 */
	public void cancellaLegame(String user,String lid1, TimestampHash timeHash) throws IllegalArgumentException, InvocationTargetException, Exception{
		Vl_luogo_luo vl_luogoLuo = new Vl_luogo_luo();
		vl_luogoLuo.setLID_1(lid1);
		Vl_luogo_luoResult tavola = new Vl_luogo_luoResult(vl_luogoLuo);


		tavola.executeCustom("selectLuogoPerRinvii");
        List vec = tavola.getElencoRisultati();

		for (int i=0;i<vec.size();i++){
			//lid2 è il luogo legato
			String lid2 = ((Vl_luogo_luo) vec.get(i)).getLID();
			cancellaLegame(user,lid1,lid2,timeHash);
			//se ho un legame con un rinvio
			if (((Vl_luogo_luo) vec.get(i)).getTP_LEGAME().equals("8")){
				Luogo luogo = new Luogo ();
				//cancello anche il rinvio
				Tb_luogo tb_luogo = luogo.cercaLuogoPerID(lid2);
				timeHash.putTimestamp("Tb_luogo",tb_luogo.getLID(),new SbnDatavar( tb_luogo.getTS_VAR()));
				if (tb_luogo.getTP_FORMA().equals("R"))
					luogo.cancellaLuogo(tb_luogo,user,timeHash);
			}
		}
	}


	public void spostaLegami(String idPartenza, String idArrivo, String user) throws IllegalArgumentException, InvocationTargetException, Exception {
        List vettoreDiLegami = null;
        Tr_luo_luo legame = new Tr_luo_luo();
		legame.setLID_BASE(idPartenza);
        Tr_luo_luoResult tabella = new Tr_luo_luoResult(legame);


		tabella.executeCustom("selectLegami");
        vettoreDiLegami = tabella.getElencoRisultati();

		for (int i=0; i<vettoreDiLegami.size(); i++){
			legame = (Tr_luo_luo) vettoreDiLegami.get(i);
            Tr_luo_luo leg_esistente = estraiLegame(idArrivo,legame.getLID_COLL());
            if (leg_esistente != null) {
                tabella = new Tr_luo_luoResult(leg_esistente);
                leg_esistente.setUTE_VAR(user);
                tabella.executeCustom("updateLegame");
            } else {
                legame.setLID_BASE(idArrivo);
                legame.setUTE_VAR(user);
                tabella = new Tr_luo_luoResult(legame);


                tabella.insert(legame);
            }
            legame.setLID_BASE(idPartenza);
            legame.setUTE_VAR(user);
            tabella = new Tr_luo_luoResult(legame);
            tabella.executeCustom("cancellaLegameLuogoLuogo");

		}
		legame = new Tr_luo_luo();
		legame.setLID_COLL(idPartenza);
		tabella = new Tr_luo_luoResult(legame);


		tabella.executeCustom("selectLegami");
        vettoreDiLegami = tabella.getElencoRisultati();

		for (int i=0; i<vettoreDiLegami.size(); i++){
            legame = (Tr_luo_luo) vettoreDiLegami.get(i);
            Tr_luo_luo leg_esistente = estraiLegame(legame.getLID_BASE(),idArrivo);
            if (leg_esistente != null) {
                tabella = new Tr_luo_luoResult(leg_esistente);
                leg_esistente.setUTE_VAR(user);
                tabella.executeCustom("updateLegame");
            } else {
                legame.setLID_COLL(idArrivo);
                legame.setUTE_VAR(user);
                tabella = new Tr_luo_luoResult(legame);
                tabella.insert(legame);
            }
            legame.setLID_COLL(idPartenza);
            legame.setUTE_VAR(user);
            tabella = new Tr_luo_luoResult(legame);
            tabella.executeCustom("cancellaLegameLuogoLuogo");

		}
	}

    /**
     * Estrae un legame dal db: anche se ha fl_canc = 'S'
     * @param lid
     * @return
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public Tr_luo_luo estraiLegame(String lidBase,String lid_coll) throws IllegalArgumentException, InvocationTargetException, Exception {
        setLID_BASE(lidBase);
        setLID_COLL(lid_coll);
        Tr_luo_luoResult tr_luo_luoResult = new Tr_luo_luoResult(this);
        tr_luo_luoResult.executeCustom("verifica");
        List TableDaoResult = tr_luo_luoResult.getElencoRisultati();

        if (TableDaoResult.size()>0)
            return (Tr_luo_luo)TableDaoResult.get(0);
        return null;
    }

	/**
	 * Questo metodo retituisce un vettore di legami in cui il lid è in
	 * lid_base OPPURE in lid_coll
	 *
	 * Method cercaLegame.
	 * @param 	lid
	 * @return TableDao
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public List cercaLegami(String lid) throws IllegalArgumentException, InvocationTargetException, Exception {
        List TableDaoResult = new ArrayList();
		setLID_BASE(lid);
		setLID_COLL(lid);
		Tr_luo_luoResult tr_luo_luoResult = new Tr_luo_luoResult(this);


		tr_luo_luoResult.executeCustom("cercaLegamiPerLid");
		TableDaoResult = tr_luo_luoResult.getElencoRisultati();

		return TableDaoResult;
	}
	public Tr_luo_luo verificaEsistenza(String lid_base,String lid_coll) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_luo_luo tr = new Tr_luo_luo();
        Tr_luo_luoResult tabella = new Tr_luo_luoResult(tr);
        tr.setLID_BASE(lid_base);
        tr.setLID_COLL(lid_coll);
        tabella.executeCustom("verifica");
        List v = tabella.getElencoRisultati();
        if (v.size() > 0) {
            return (Tr_luo_luo) v.get(0);
        }
        return null;
	}

	public List cercaLegami(String lid, String tpLeg) throws IllegalArgumentException, InvocationTargetException, Exception {

	  List TableDaoResult = new ArrayList();
	  setLID_BASE(lid);
	  setLID_COLL(lid);
	  setTP_LEGAME(tpLeg);
	  Tr_luo_luoResult tr_luo_luoResult = new Tr_luo_luoResult(this);


	  tr_luo_luoResult.executeCustom("cercaLegamiPerLid");
	  TableDaoResult = tr_luo_luoResult.getElencoRisultati();

	  return TableDaoResult;
	}


  public List scambioFormaLuoghi(String lidAccettato, String lidRinviato, String userID) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
    Luogo luogo = new Luogo();
    Tb_luogo luogoAccettato, luogoRinviato;
    List vettoreLuoghi = new ArrayList();
    luogoAccettato = luogo.cercaLuogoPerID(lidAccettato);
    luogoRinviato = luogo.cercaLuogoPerID(lidRinviato);
    luogoAccettato.setLID(lidRinviato);
    luogoRinviato.setLID(lidAccettato);
    luogoAccettato.setTP_FORMA("R");
    luogoRinviato.setTP_FORMA("A");
    luogoAccettato.setUTE_VAR(userID);
    luogoRinviato.setUTE_VAR(userID);
    if (luogo.updateLuogo(luogoAccettato))
      luogo.updateLuogo(luogoRinviato);
    vettoreLuoghi.add(luogoAccettato);
    vettoreLuoghi.add(luogoRinviato);
    return vettoreLuoghi;
  }
}
