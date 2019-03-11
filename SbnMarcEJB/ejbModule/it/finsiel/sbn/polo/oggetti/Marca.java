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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_marcaResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_aut_marResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_rep_marResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Ts_link_multimResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_marca_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_marca_titResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_repertorio_marResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.NormalizzaNomi;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Progressivi;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.TimbroCondivisione;
import it.finsiel.sbn.polo.oggetti.estensione.AutoreAllineamento;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_marca;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tr_aut_mar;
import it.finsiel.sbn.polo.orm.Tr_rep_mar;
import it.finsiel.sbn.polo.orm.Ts_link_multim;
import it.finsiel.sbn.polo.orm.viste.Vl_marca_aut;
import it.finsiel.sbn.polo.orm.viste.Vl_marca_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_repertorio_mar;
import it.iccu.sbn.ejb.model.unimarcmodel.A921;
import it.iccu.sbn.ejb.model.unimarcmodel.C856;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaMarcaType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.MarcaType;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

/**
 * @author
 * l'oggeto Marca implementa i metodi di ricerca sul db
 * gestisce operazioni complesse sull'oggetto tb_marca
 */
public class Marca extends Tb_marca{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7595720100391236757L;
	private static Category log = Category.getInstance("it.finsiel.sbn.polo.oggetti");
    boolean filtriValorizzati = false;

	public Marca() {
	}

    /**
     * Metodo per cercare la marca con numero di identificativo:
     * ricerca su Tb_marca con ID
     * @throws InfrastructureException
     */
    public TableDao cercaMarcaPerID(String id) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        Tb_marca marca = new Tb_marca();
        marca.setMID(id);
        Tb_marcaResult tavola = new Tb_marcaResult(marca);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(marca.leggiAllParametro()));
	    return tavola;
    }
    public Tb_marca estraiMarcaPerID(String id) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        Tb_marca marca = null;
        TableDao tavola = cercaMarcaPerID(id);
        List v = tavola.getElencoRisultati();
        if (v.size()>0) marca= (Tb_marca)v.get(0);

        return marca;
    }

    public Tb_marca verificaEsistenza(String id) throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        Tb_marca marca = new Tb_marca();
        marca.setMID(id);
        Tb_marcaResult tavola = new Tb_marcaResult(marca);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(marca.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        if (v.size()>0) return (Tb_marca)v.get(0);
        return null;
    }

    /**
     * Metodo per cercare la marca con le parole chiavi:
     * Metodo per cercare la marca in base alle parole inserite
     * Input: vettore di parole
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaMarcaPerParole(String parole[],CercaMarcaType cercaMarca,String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        for (int i = 1; i <= parole.length; i++){
        	String parolaNormalizzata = NormalizzaNomi.normalizzazioneGenerica(parole[i - 1].trim());
            settaParametro("XXXparola" + i, parolaNormalizzata);
        }

		Tb_marcaResult tavola = new Tb_marcaResult(this);


        if ((!filtriValorizzati) && (cercaMarca!=null))
            valorizzaFiltri(cercaMarca);
        tavola.executeCustom("selectPerParoleNome",ordinamento);
        return tavola;
    }

    public int contaMarcaPerParole(String parole[],CercaMarcaType cercaMarca) throws IllegalArgumentException, InvocationTargetException, Exception {
        for (int i = 1; i <= parole.length; i++)
            settaParametro("XXXparola" + i, parole[i - 1]);
		Tb_marcaResult tavola = new Tb_marcaResult(this);


        valorizzaFiltri(cercaMarca);
        tavola.executeCustom("countPerParoleNome");
        int n = conta(tavola);

        return n;
    }


    public TableDao cercaMarcaPerMotto(String motto,CercaMarcaType cercaMarca,String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        settaParametro(TableDao.XXXmotto , motto);
		Tb_marcaResult tavola = new Tb_marcaResult(this);


        if ((!filtriValorizzati) && (cercaMarca!=null))
            valorizzaFiltri(cercaMarca);
        tavola.executeCustom("selectPerMotto",ordinamento);
        return tavola;
    }

    public int contaMarcaPerMotto(String motto,CercaMarcaType cercaMarca) throws IllegalArgumentException, InvocationTargetException, Exception {
        settaParametro(TableDao.XXXmotto , motto);
		Tb_marcaResult tavola = new Tb_marcaResult(this);


        if ((!filtriValorizzati) && (cercaMarca!=null))
            valorizzaFiltri(cercaMarca);
        tavola.executeCustom("countPerMotto");
        int n = conta(tavola);

        return n;
    }


    /** Legge da una tavola il valore del COUNT(*) */
    private int conta(TableDao tavola) throws EccezioneDB {
    	return tavola.getCount();
//       try {
//            ResultSet rs = tavola.getResultSet();
//            rs.next();
//            return rs.getInt(1);
//        } catch (SQLException ecc) {
//            log.error("Errore nella lettura del COUNT (*) dal resultset");
//            throw new EccezioneDB(1203,"Errore nella lettura dal DB");
//        }
    }

	/**
	 * metodo per inserire una marca nel db
	 * @throws InfrastructureException
	 * @throws EccezioneSbnDiagnostico
	 */
	public boolean inserisciMarca(
			MarcaType marcaType,
			String user,
			String cd_livello,
			String t001,
			String _condiviso) throws EccezioneDB, InfrastructureException, EccezioneSbnDiagnostico{
		//verifica se va bene qui il metodo!!!!
		Tb_marca tb_marca = new Tb_marca();
	  	SbnDatavar data_operazione = new SbnDatavar(System.currentTimeMillis()) ;
		tb_marca.setDS_MARCA(marcaType.getT921().getA_921());
		tb_marca.setDS_MOTTO(marcaType.getT921().getE_921());
		tb_marca.setNOTA_MARCA(marcaType.getT921().getD_921());
		tb_marca.setFL_CANC(" ");
		tb_marca.setMID(t001);
		tb_marca.setTS_INS(ConverterDate.SbnDataVarToDate(data_operazione));
		tb_marca.setTS_VAR(ConverterDate.SbnDataVarToDate(data_operazione));
		tb_marca.setUTE_INS(user);
		tb_marca.setUTE_VAR(user);
		tb_marca.setFL_SPECIALE("N");
        // Timbro Condivisione
		tb_marca.setFL_CONDIVISO(_condiviso);
		tb_marca.setTS_CONDIVISO(TableDao.now());
		tb_marca.setUTE_CONDIVISO(user);

		//prendere il cd_livello da livelloAut
		tb_marca.setCD_LIVELLO(cd_livello);

		//qui setto tutti i valori della marca che vado ad inserire
        Tb_marcaResult tb_marcaResult = new Tb_marcaResult(tb_marca);
        tb_marcaResult.insert(tb_marca);

        return true;

	}

	/**
	 * metodo per cercare il link all'immagine
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public TableDao cercaLinkMarcaImmagine(String mid) throws IllegalArgumentException, InvocationTargetException, Exception{
		Ts_link_multim ts_link_multim = new Ts_link_multim();
		ts_link_multim.setKY_LINK_MULTIM(mid);
		Ts_link_multimResult ts_link_multimResult = new Ts_link_multimResult(ts_link_multim);


		ts_link_multimResult.executeCustom("selectPerKy");
        //return (Ts_link_multim) ts_link_multimResult.getElencoRisultati().get(0);
        return ts_link_multimResult;
	}

	/**
	 * metodo per cercare il link all'immagine
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void eliminaLinkMarcaImmagine(String mid,String user) throws IllegalArgumentException, InvocationTargetException, Exception{
		Ts_link_multim ts_link_multim = new Ts_link_multim();
		ts_link_multim.setKY_LINK_MULTIM(mid);
		Ts_link_multimResult ts_link_multimResult = new Ts_link_multimResult(ts_link_multim);
		//TEST PROVO CON ELIMINAZIONE LOGICA
		ts_link_multim.setUTE_INS(user);
		ts_link_multim.setUTE_VAR(user);

		ts_link_multimResult.executeCustom("deletePerKy");
		//ts_link_multimResult.executeCustom("cancellaPerKy");
	}


	/**
	 * metodo per creare il link all'immagine
	 * @throws InfrastructureException
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	public void creaLinkMarcaImmagine(String mid,String uri,byte[] im, String user) throws EccezioneDB, NumberFormatException, InfrastructureException, SQLException{
		Ts_link_multim ts_link_multim = new Ts_link_multim();
		Progressivi prog = new Progressivi();
		ts_link_multim.setID_LINK_MULTIM(Long.parseLong(prog.getNextIdLinkMultim()));
		ts_link_multim.setKY_LINK_MULTIM(mid);
		ts_link_multim.setURI_MULTIM(uri);
		ts_link_multim.setFL_CANC(" ");
		ts_link_multim.setUTE_INS(user);
		ts_link_multim.setUTE_VAR(user);
		Link_multim link_multim = new Link_multim();
        link_multim.insert(ts_link_multim, im);
	}

	/**
	 * metodo per cercare gli autori collegati ad una marca
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public TableDao cercaMarcaAutore(String mid) throws IllegalArgumentException, InvocationTargetException, Exception{
		Vl_marca_aut vl_marca_aut = new Vl_marca_aut();
		vl_marca_aut.setMID(mid);
		Vl_marca_autResult vl_marca_autResult = new Vl_marca_autResult(vl_marca_aut);


		vl_marca_autResult.executeCustom("selectPerMid");
		return vl_marca_autResult;
	}


    /**
     * Metodo per cercare le marche con intervallo di date di variazione
     * Input: T005_Range, statusAuthority, formaNome
     * ricerca su Tb_autore per intervallo su ts_var
     * Output possibili:
     * . marca inesistente
     * . marche trovati: tutti gli attributi
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaMarcaPerDatavar(
        SbnDatavar datada,
        SbnDatavar dataa,
        CercaMarcaType cerca,
        String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        settaParametro(TableDao.XXXinizio_intervallo,datada.getJdbcDate());
        settaParametro(TableDao.XXXfine_intervallo,dataa.getJdbcDate());
        if(!filtriValorizzati) valorizzaFiltri(cerca);
        Tb_marcaResult tavola = new Tb_marcaResult(this);


        tavola.executeCustom("selectPerDatavar", ordinamento);
        return tavola;
    }

    public int contaMarcaPerDatavar(SbnDatavar datada, SbnDatavar dataa, CercaMarcaType cerca) throws IllegalArgumentException, InvocationTargetException, Exception {
        settaParametro(TableDao.XXXinizio_intervallo,datada.getJdbcDate());
        settaParametro(TableDao.XXXfine_intervallo,dataa.getJdbcDate());
        valorizzaFiltri(cerca);
        Tb_marcaResult tavola = new Tb_marcaResult(this);


        tavola.executeCustom("countPerDatavar");
        int n = conta(tavola);

        return n;
    }

    /** Valorizza i filtri in base al contenuto del CercaAutoreType */
    public void valorizzaFiltri(CercaMarcaType cerca) {
        valorizzaFiltri(this, cerca);
    }

    /** Valorizza i filtri di autore in base al contenuto del CercaAutoreType */
    public Tb_marca valorizzaFiltri(Tb_marca marca, CercaMarcaType cerca) {
        filtriValorizzati = true;
        if (cerca == null)
            return marca;
        if (cerca.getLivelloAut_Da() != null)
            marca.settaParametro(TableDao.XXXlivello_aut_da,
                Decodificatore.livelloSogliaDa(cerca.getLivelloAut_Da().toString()));
        if (cerca.getLivelloAut_A() != null)
            marca.settaParametro(TableDao.XXXlivello_aut_a, cerca.getLivelloAut_A().toString());
/*        if (cerca.getT005_Range() != null) {
            marca.settaParametro(TableDao.XXXdata_var_Da",cerca.getT005_Range().getDataDa().toString());
            marca.settaParametro(TableDao.XXXdata_var_A",cerca.getT005_Range().getDataA().toString());
        }
*/
        if (cerca.getT005_Range() != null) {
        	int filtro = cerca.getT005_Range().getTipoFiltroDate();
        	if (filtro < 2){
	            marca.settaParametro(TableDao.XXXdata_var_Da,cerca.getT005_Range().getDataDa().toString());
	            marca.settaParametro(TableDao.XXXdata_var_A,cerca.getT005_Range().getDataA().toString());
        	} else  if (filtro == 2){
	            marca.settaParametro(TableDao.XXXdata_ins_Da,cerca.getT005_Range().getDataDa().toString());
	            marca.settaParametro(TableDao.XXXdata_ins_A,cerca.getT005_Range().getDataA().toString());
        	}
        }


        return marca;
    }

    /** Esegue una ricerca di marche legate ad un documento
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaMarcaPerTitolo (String id_titolo, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_marca_tit tit_tit = new Vl_marca_tit();
        tit_tit.setBID(id_titolo);
        Vl_marca_titResult tavola = new Vl_marca_titResult(tit_tit);


        tavola.executeCustom("selectMarcaPerTitolo",ordinamento);
        return tavola;
    }

    /** Esegue una ricerca di marche legate ad un autore
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaMarcaPerAutore (String id_autore, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_marca_aut mar_aut = new Vl_marca_aut();
        mar_aut.setVID(id_autore);
        Vl_marca_autResult tavola = new Vl_marca_autResult(mar_aut);


        tavola.executeCustom("selectPerVid",ordinamento);
        return tavola;
    }

	/**
	 * Method contaMarcaPerAutore.
	 * @param id_autore
	 * @return int
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public int contaMarcaPerAutore(String id_autore) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_marca_aut mar_aut = new Vl_marca_aut();
        mar_aut.setVID(id_autore);
        Vl_marca_autResult tavola = new Vl_marca_autResult(mar_aut);


        tavola.executeCustom("countAutorePerLegameVid");
        int n = conta(tavola);

        return n;
	}



	/**
	 * Method cercaMarcaRepertorio.
	 * @param mid
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public TableDao cercaMarcaPerRepertorio(String mid, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_repertorio_mar rep_mar = new Vl_repertorio_mar();
        rep_mar.setMID(mid);
        Vl_repertorio_marResult tavola = new Vl_repertorio_marResult(rep_mar);


        tavola.executeCustom("selectPerMid",ordinamento);
		return tavola;
	}






	/**
	 * Method crealegame.
	 * @param legameElementoAutType
	 * @param string
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */

	public void creaLegame(LegameElementoAutType	legameElementoAutType, String idPartenza, String user) throws IllegalArgumentException, InvocationTargetException, Exception{
       	if (legameElementoAutType.getTipoAuthority().toString().equals("RE")){
			RepertorioMarca repMar = new RepertorioMarca();
			if (!repMar.esisteLegame(idPartenza,legameElementoAutType.getCitazione(),legameElementoAutType.getIdArrivo())){
				if (legameElementoAutType.getCitazione() == 0)
					throw new EccezioneSbnDiagnostico(3016,"la citazione è obbligatoria");
				Tr_rep_mar tr_rep_mar = new Tr_rep_mar();
				tr_rep_mar.setFL_CANC(" ");
				tr_rep_mar.setMID(idPartenza);
				tr_rep_mar.setPROGR_REPERTORIO(legameElementoAutType.getCitazione());
				Repertorio repertorio = new Repertorio();
				Tb_repertorio tb_repertorio;
				tb_repertorio = repertorio.cercaRepertorioPerCdSig(legameElementoAutType.getIdArrivo());
				if (tb_repertorio == null)
					throw new EccezioneSbnDiagnostico(3023,"repertorio inesistente");
				tr_rep_mar.setID_REPERTORIO(tb_repertorio.getID_REPERTORIO());
				tr_rep_mar.setUTE_INS(user);
				tr_rep_mar.setUTE_VAR(user);
				repMar.inserisciRepertorioMarca(tr_rep_mar);
//				log.info("inserito il legame con il repertorio");
			}else throw new EccezioneSbnDiagnostico(3030,"Legame con repertorio esistente");
		}else if (legameElementoAutType.getTipoAuthority().toString().equals("AU")){
			Autore autore = new Autore();
			Tb_autore tb_autore = null;
			TableDao tavola;
            List temp;
			tavola = autore.cercaAutorePerID(legameElementoAutType.getIdArrivo());
			temp = tavola.getElencoRisultati();
			if (temp.size()>0)
				tb_autore = (Tb_autore) temp.get(0);
			if (tb_autore == null)
					throw new EccezioneSbnDiagnostico(3022,"autore inesistente");
			if (!tb_autore.getTP_NOME_AUT().equals("E"))
					throw new EccezioneSbnDiagnostico(3045,"tipo nome non valido");
			AutoreMarca autMar = new AutoreMarca();
			if (autMar.estraiLegame(legameElementoAutType.getIdArrivo(),idPartenza) == null){
				Tr_aut_mar tr_aut_mar = new Tr_aut_mar();
				tr_aut_mar.setFL_CANC(" ");
				tr_aut_mar.setMID(idPartenza);
				tr_aut_mar.setVID(legameElementoAutType.getIdArrivo());
				tr_aut_mar.setUTE_INS(user);
				tr_aut_mar.setUTE_VAR(user);
                tr_aut_mar.setNOTA_AUT_MAR(legameElementoAutType.getNoteLegame());
				autMar.inserisciAutoreMarca(tr_aut_mar);
				log.info("inserito il legame con autore");
                Tb_autore aut = new Tb_autore();
                aut.setVID(legameElementoAutType.getIdArrivo());
                aut.setFL_CANC("N");
                AllineamentoAutore flagAllineamentoAutore = new AllineamentoAutore(aut);
                flagAllineamentoAutore.setTr_aut_mar(true);
                new AutoreAllineamento().aggiornaFlagAllineamento(
                    flagAllineamentoAutore,
                    user);
                new Autore().updateVariazione(legameElementoAutType.getIdArrivo(), user);
			} else {
                throw new EccezioneSbnDiagnostico(3030,"legame già esistente");
            }
       	}
	}


	public void modificaLegame(LegameElementoAutType	legameElementoAutType, String idPartenza, String user) throws NumberFormatException, IllegalArgumentException, InvocationTargetException, Exception{
       	if (legameElementoAutType.getTipoAuthority().toString().equals("RE")){
			RepertorioMarca repMar = new RepertorioMarca();
			if (!repMar.esisteLegame(idPartenza,legameElementoAutType.getCitazione(),legameElementoAutType.getIdArrivo())){
				if (legameElementoAutType.getCitazione() == 0)
					throw new EccezioneSbnDiagnostico(3016,"la citazione è obbligatoria");
				Tr_rep_mar tr_rep_mar = new Tr_rep_mar();
				tr_rep_mar.setFL_CANC(" ");
				tr_rep_mar.setMID(idPartenza);
				tr_rep_mar.setPROGR_REPERTORIO(legameElementoAutType.getCitazione());
				tr_rep_mar.setID_REPERTORIO(Long.parseLong(legameElementoAutType.getIdArrivo()));
				tr_rep_mar.setUTE_VAR(user);
				if (legameElementoAutType.getNoteLegame() != null)
					tr_rep_mar.setNOTA_REP_MAR(legameElementoAutType.getNoteLegame());
				Tr_rep_marResult tr_rep_marResult = new Tr_rep_marResult(tr_rep_mar);


				tr_rep_marResult.update(tr_rep_mar);

				log.info("inserito il legame con il repertorio");
			}else throw new EccezioneSbnDiagnostico(3030,"Legame con repertorio esistente");
		}else if (legameElementoAutType.getTipoAuthority().toString().equals("AU")){
			AutoreMarca autMar = new AutoreMarca();
			if (autMar.estraiLegame(legameElementoAutType.getIdArrivo(),idPartenza) != null){
				Tr_aut_mar tr_aut_mar = new Tr_aut_mar();
				tr_aut_mar.setFL_CANC(" ");
				tr_aut_mar.setMID(idPartenza);
				tr_aut_mar.setVID(legameElementoAutType.getIdArrivo());
				tr_aut_mar.setUTE_VAR(user);
                tr_aut_mar.setNOTA_AUT_MAR(legameElementoAutType.getNoteLegame());
				Tr_aut_marResult tr_aut_marResult = new Tr_aut_marResult(tr_aut_mar);


				tr_aut_marResult.update(tr_aut_mar);

				log.info("inserito il legame con autore");
            } else {
                throw new EccezioneSbnDiagnostico(3029,"legame non esistente");
            }
       	}
	}



	/**
	 * Method cancellaLegame
	 * questo metodo è da invocare dopo aver validato i legami e quindi si
	 * ha la certezza della loro esistenza
	 *
	 * @param legameElementoAutType
	 * @param string
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void cancellaLegame(
	LegameElementoAutType	legameElementoAutType,
	String 					idPartenza,
	String 					user,
	TimestampHash			timeHash) throws IllegalArgumentException, InvocationTargetException, Exception{
       	if (legameElementoAutType.getTipoAuthority().toString().equals("RE")){
			RepertorioMarca repMar = new RepertorioMarca();
			Tr_rep_mar tr_rep_mar = new Tr_rep_mar();
			tr_rep_mar.setFL_CANC("S");
			tr_rep_mar.setMID(idPartenza);
			tr_rep_mar.setPROGR_REPERTORIO(legameElementoAutType.getCitazione());
			Tb_repertorio tb_repertorio;
        	Repertorio authority =new Repertorio();
        	tb_repertorio = authority.cercaRepertorioPerCdSig(legameElementoAutType.getIdArrivo());
			tr_rep_mar.setID_REPERTORIO(tb_repertorio.getID_REPERTORIO());
            tr_rep_mar.setTS_VAR(
            		ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_rep_mar",
                            tr_rep_mar.getID_REPERTORIO() + tr_rep_mar.getMID()+ tr_rep_mar.getPROGR_REPERTORIO())));
			repMar.cancellaRepertorioMarca(tr_rep_mar,user);
		}else if (legameElementoAutType.getTipoAuthority().toString().equals("AU")){
			AutoreMarca autMar = new AutoreMarca();
			Tr_aut_mar tr_aut_mar = new Tr_aut_mar();
			tr_aut_mar.setFL_CANC("S");
			tr_aut_mar.setMID(idPartenza);
			tr_aut_mar.setVID(legameElementoAutType.getIdArrivo());
			tr_aut_mar.setUTE_VAR(user);
            tr_aut_mar.setTS_VAR(
            		ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tr_aut_mar", tr_aut_mar.getVID() + tr_aut_mar.getMID())));
			autMar.cancellaAutoreMarca(tr_aut_mar,user);
       	}
	}





	/**
	 * Method modificaMarca.
	 * @param tb_marca
	 * @param _modificaType
	 * @param _userID
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void modificaMarca(
			Tb_marca tb_marca,
			String t005,
			C856 c856[],
			A921 a921,
			String userID,
			String _condiviso) throws IllegalArgumentException, InvocationTargetException, Exception {
		//tb_marca.setTS_VAR(t005);
        TimbroCondivisione timbroCondivisione = new TimbroCondivisione();
        timbroCondivisione.modificaTimbroCondivisione(tb_marca,userID,_condiviso);

		tb_marca.setFL_CANC(" ");
		if (a921 != null){
			if (a921.getA_921() != null)
				tb_marca.setDS_MARCA(a921.getA_921());
			if (a921.getE_921() != null)
				tb_marca.setDS_MOTTO(a921.getE_921());
			else
				tb_marca.setDS_MOTTO(null);
			if (a921.getD_921() != null)
				tb_marca.setNOTA_MARCA(a921.getD_921());
			else
				tb_marca.setNOTA_MARCA(null);
		}
		tb_marca.setUTE_VAR(userID);
		tb_marca.setFL_SPECIALE("N");
		Tb_marcaResult tb_marcaResult = new Tb_marcaResult(tb_marca);


		tb_marcaResult.update(tb_marca);

//      INIZIO COPIATO DA PROTOCOLLO INDICE All'IF originario si aggiunge l'AND sul Maggiore/uguale a 0
		if (c856 != null && c856.length >= 0 ) {

			eliminaLinkMarcaImmagine(tb_marca.getMID(),userID);
			for(int i=0;i<c856.length; i++) {
				if (c856[i].getC9_856_1() != null) {
					creaLinkMarcaImmagine(tb_marca.getMID(),c856[i].getU_856(),c856[i].getC9_856_1(),userID);
				}
			}
		}

	}

	/**
	 * Questo metodo si preoccupa di cancellare in maniera ordinata:
	 * i legami della marca con i repertori
	 * i legami della marca con gli autori
	 * le parole della marca
	 * i link alle immagini
	 *
	 * Method cancellaMarca.
	 * @param marcaDaCancellare
	 * @param string
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void cancellaMarca(Tb_marca marcaDaCancellare, String user, TimestampHash timeHash) throws IllegalArgumentException, InvocationTargetException, Exception {
		String mid = marcaDaCancellare.getMID();
        List tempTableDao = new ArrayList();
		//cancello i legami con repertori
		RepertorioMarca repMar = new RepertorioMarca();
		Tr_rep_mar tr_rep_mar = new Tr_rep_mar();
		tr_rep_mar.setMID(mid);
		Tr_rep_marResult tavola1 = new Tr_rep_marResult(tr_rep_mar);


		tavola1.executeCustom("selectRepertorioPerMarca");
		tempTableDao = tavola1.getElencoRisultati();
		if (tempTableDao.size() >0 )
			for (int i=0;i<tempTableDao.size();i++)
				repMar.cancellaRepertorioMarca((Tr_rep_mar)tempTableDao.get(i), user);
//XXXXXXXXXXX
		//cancello i legami con gli autori
		AutoreMarca autMar = new AutoreMarca();
		Tr_aut_mar tr_aut_mar = new Tr_aut_mar();
		tr_aut_mar.setFL_CANC("S");
		tr_aut_mar.setMID(mid);
		Tr_aut_marResult tavola2 = new Tr_aut_marResult(tr_aut_mar);


		// MON FUNZIONA SQL NON CORRETTA ? chiede la lista deglia autori con vid? molto strano
		//tavola2.executeCustom("selectAutoreMarca");
		tavola2.executeCustom("selectAutoriMarca");
		tempTableDao = tavola2.getElencoRisultati();
		if (tempTableDao.size() >0 )
			for (int i=0;i<tempTableDao.size();i++)
				autMar.cancellaAutoreMarca((Tr_aut_mar)tempTableDao.get(i), user);
//XXXXXXXXXXX

		//cancello le parole della marca
		Parola parola = new Parola();
		parola.eliminaParole(mid,user);
		//cancello i link alle immagini
		TableDao tavola;
		tavola = cercaLinkMarcaImmagine(mid);
        List vettoreLinkImmagine = tavola.getElencoRisultati();

		cancellaLinkImmagine(vettoreLinkImmagine, user);
		marcaDaCancellare.setFL_CANC("S");
		marcaDaCancellare.setUTE_VAR(user);
		marcaDaCancellare.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tb_marca",marcaDaCancellare.getMID())));
		Tb_marcaResult tb_marcaResult = new Tb_marcaResult(marcaDaCancellare);
		tb_marcaResult.executeCustom("cancellaMarca");
	}

	/**
	 * Method cancellaLinkImmagine.
	 * @param vettoreLinkImmagine
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void cancellaLinkImmagine(List vettoreLinkImmagine, String user) throws IllegalArgumentException, InvocationTargetException, Exception {
		Ts_link_multim link;
		for (int i=0;i<vettoreLinkImmagine.size();i++){
			link = new Ts_link_multim();
			link = (Ts_link_multim)vettoreLinkImmagine.get(i);
			link.setFL_CANC("S");
			link.setUTE_VAR(user);
			Ts_link_multimResult tavola = new Ts_link_multimResult(link);
			tavola.executeCustom("cancellaLink");
		}
	}

    public void updateVersione(String id, String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_marca marca = new Tb_marca();
        marca.setMID(id);
        marca.setUTE_VAR(ute_var);
        Tb_marcaResult tb_marcaResult = new Tb_marcaResult(marca);
        tb_marcaResult.executeCustom("updateVersione");
    }



}
