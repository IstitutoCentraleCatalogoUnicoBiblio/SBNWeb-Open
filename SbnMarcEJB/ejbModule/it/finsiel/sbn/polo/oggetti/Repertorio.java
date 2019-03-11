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
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_repertorioResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaSoggettoDescrittoreClassiReperType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Classe Repertorio.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 * @author
 *
 * @version 06-mar-03
 */
public class Repertorio extends Tb_repertorio {

	private static final long serialVersionUID = -4801231081714210951L;

	protected boolean filtriValorizzati;
    private static Logger log = Logger.getLogger("iccu.serversbnmarc.Repertorio");

    public Repertorio() {
    }


    public void valorizzaFiltri(CercaDatiAutType cerca) {
        valorizzaFiltri(this, cerca);
	}

    /** Valorizza i filtri */
    public Tb_repertorio valorizzaFiltri(Tb_repertorio repertorio, CercaDatiAutType cerca) {
        filtriValorizzati = true;
        if (cerca == null)
            return repertorio;
        if (cerca.getT005_Range() != null) {
        	int filtro = cerca.getT005_Range().getTipoFiltroDate();
        	if (filtro < 2){
	            repertorio.settaParametro(TableDao.XXXdata_var_Da,cerca.getT005_Range().getDataDa().toString());
	            repertorio.settaParametro(TableDao.XXXdata_var_A,cerca.getT005_Range().getDataA().toString());
        	} else  if (filtro == 2){
	            repertorio.settaParametro(TableDao.XXXdata_ins_Da,cerca.getT005_Range().getDataDa().toString());
	            repertorio.settaParametro(TableDao.XXXdata_ins_A,cerca.getT005_Range().getDataA().toString());
        	}
        }
   		if (cerca instanceof CercaSoggettoDescrittoreClassiReperType)
			cerca = cerca;
		if (cerca != null)
			if (cerca.getFormaNome() != null)
				setTP_REPERTORIO(cerca.getFormaNome().toString());
        return repertorio;
    }

    /**
     * Method cercaAutorePerParoleNome.
     * @param string
     * @return TableDao
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaRepertorioPerParoleNome(
        String[] paroleNome,
        String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        for (int i = 1; i <= paroleNome.length; i++)
            settaParametro("XXXparola" + i, paroleNome[i - 1]);
        Tb_repertorioResult tavola = new Tb_repertorioResult(this);


        tavola.executeCustom("selectPerParoleNome", ordinamento);
        return tavola;
    }
    public int contaRepertorioPerParoleNome(
        String[] paroleNome)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        for (int i = 1; i <= paroleNome.length; i++)
            settaParametro("XXXparola" + i, paroleNome[i - 1]);
        Tb_repertorioResult tavola = new Tb_repertorioResult(this);


        tavola.executeCustom("countPerParoleNome");
        int n = conta(tavola);

        return n;
    }


    public int contaRepertorioPerNomeLike(String nome)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_repertorioResult tavola = new Tb_repertorioResult(this);


        settaParametro(TableDao.XXXstringaLike, nome);
        tavola.executeCustom("countPerNomeLike");
        int n = conta(tavola);

        return n;
    }


    public TableDao cercaRepertorioPerNomeLike(
        String nome,
        String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_repertorioResult tavola = new Tb_repertorioResult(this);


        settaParametro(TableDao.XXXstringaLike, nome);
        tavola.executeCustom("selectPerNomeLike", ordinamento);
        return tavola;
    }


    public int contaRepertorioPerNomeEsatto(String nome)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_repertorioResult tavola = new Tb_repertorioResult(this);


        settaParametro(TableDao.XXXstringaEsatta, nome);
        tavola.executeCustom("countPerNomeEsatto");
        int n = conta(tavola);

        return n;
    }


    public TableDao cercaRepertorioPerNomeEsatto(
        String nome,
        String ordinamento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_repertorioResult tavola = new Tb_repertorioResult(this);


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
     * Metodo per cercare la marca con codice di identificativo:
     * ricerca su Tb_repertorio con cd_sig_repertorio
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public Tb_repertorio cercaRepertorioPerCdSig(String id) throws IllegalArgumentException, InvocationTargetException, Exception{
        setCD_SIG_REPERTORIO(id);
        Tb_repertorioResult tavola = new Tb_repertorioResult(this);


        tavola.executeCustom("selectPerCd_sig_repertorio");
        List v = tavola.getElencoRisultati();

        if (v.size()>0)
            return (Tb_repertorio) v.get(0);
        return null;
    }



 // Aprile 2017 EVOLUTIVA su link esterni; viene completato intervento affiche si crei automaticamente
 // la string link esterno da passare al protocollo che provvederà a validarla e solo se corretta ad inserirla
 // sulla base dati
 // INZIO inserimento metodi da Indice
    /**
	 * Metodo per cercare la marca con codice di identificativo e tipo_repertorio:
	 * ricerca su Tb_repertorio con cd_sig_repertorio
     * @throws InfrastructureException
	 */
	public Tb_repertorio cercaRepertorioPerCdSigTipoRepertorio(String id) throws IllegalArgumentException, InvocationTargetException, Exception{
		setCD_SIG_REPERTORIO(id);
		setTP_REPERTORIO("M");
		Tb_repertorioResult tavola = new Tb_repertorioResult(this);
		tavola.executeCustom("selectPerCd_sig_repertorio_tp_repertorio");
		List v = tavola.getElencoRisultati();

		if (v.size()>0)
			return (Tb_repertorio) v.get(0);
		return null;
	}
	/**
	 * Metodo per cercare il link della basedati relativa all codice LINK:
	 * ricerca su Tb_repertorio con cd_sig_repertorio
	 */
	public Tb_repertorio cercaRepertorioPerCdSigTipoRepertorioLink(String id) throws IllegalArgumentException, InvocationTargetException, Exception{
		setCD_SIG_REPERTORIO(id);
		setTP_REPERTORIO("B");
		Tb_repertorioResult tavola = new Tb_repertorioResult(this);

		tavola.executeCustom("selectPerCd_sig_repertorio_tp_repertorio");
		List v = tavola.getElencoRisultati();
		if (v.size()>0)
			return (Tb_repertorio) v.get(0);
		return null;
	}
 // FINE inserimento metodi da Indice

    /**
     * Metodo per cercare la marca con numero di identificativo:
     * ricerca su Tb_repertorio con id_repertorio
     * @throws InfrastructureException
     */
    public Tb_repertorio cercaRepertorioId(int id) throws EccezioneDB, InfrastructureException{
        setID_REPERTORIO(id);
        Tb_repertorioResult tavola = new Tb_repertorioResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        if (v.size()>0)
            return (Tb_repertorio) v.get(0);
        return null;
    }

    /**
     * Metodo per cercare la marca con numero di identificativo:
     * ricerca su Tb_repertorio con id_repertorio
     * @throws InfrastructureException
     */
    public Tb_repertorio cercaRepertorioId(String id) throws EccezioneDB, InfrastructureException{
        setID_REPERTORIO(Integer.parseInt(id));
        Tb_repertorioResult tavola = new Tb_repertorioResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
        List v = tavola.getElencoRisultati();

        if (v.size()>0)
            return (Tb_repertorio) v.get(0);
        return null;
    }

    public void updateVersione(int id, String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_repertorio repertorio = new Tb_repertorio();
        repertorio.setID_REPERTORIO(id);
        repertorio.setUTE_VAR(ute_var);
        Tb_repertorioResult tb_repertorioResult = new Tb_repertorioResult(repertorio);
        tb_repertorioResult.executeCustom("updateVersione");
    }
//    public void update(long id_repertorio,String _descrizione,TipoRepertorio _tipo,String _sigla,String ute_var) throws IllegalArgumentException, InvocationTargetException, Exception {
//        Tb_repertorio repertorio = new Tb_repertorio();
//        repertorio.setID_REPERTORIO(id_repertorio);
//        repertorio.setUTE_INS(repertorio);
//        Tb_repertorioResult tb_repertorioResult = new Tb_repertorioResult(repertorio);
//        tb_repertorioResult.insert(repertorio);
//    }

    public void eseguiUpdate(Tb_repertorio repertorio) throws EccezioneDB, InfrastructureException {
    	Tb_repertorioResult rep = new Tb_repertorioResult(repertorio);
        rep.update(repertorio);
    }
    public void eseguiInsert(Tb_repertorio repertorio) throws EccezioneDB, InfrastructureException {
    	Tb_repertorioResult rep = new Tb_repertorioResult(repertorio);
        rep.insert(repertorio);
    }
	/**
	 * Method cercaRepertorioPerID.
	 * @param _idOggetto
	 * @return TableDao
	 * @throws InfrastructureException
	 */
	public TableDao cercaRepertorioPerID(String id) throws EccezioneDB, InfrastructureException {
        setID_REPERTORIO(Integer.parseInt(id));
        Tb_repertorioResult tavola = new Tb_repertorioResult(this);


        tavola.valorizzaElencoRisultati(tavola.selectPerKey(this.leggiAllParametro()));
		return tavola;
	}

	//almaviva5_20090722 #3086
	public Tb_repertorio cercaRepertorioPerCdSigAncheCancellato(String id)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		setCD_SIG_REPERTORIO(id);
		Tb_repertorioResult tavola = new Tb_repertorioResult(this);

		tavola.executeCustom("selectPerCd_sig_repertorioAncheCancellato");
		List v = tavola.getElencoRisultati();

		if (v.size() > 0)
			return (Tb_repertorio) v.get(0);
		return null;
	}

	// evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità di gestire i campi
	// nota informativa , nota catalogatore e legame a repertori
	public Tb_repertorio cercaRepertorioPerCdSigTipoRepertorioLU(String id) throws Exception{
		setCD_SIG_REPERTORIO(id);
		setTP_REPERTORIO("L");
		Tb_repertorioResult tavola = new Tb_repertorioResult(this);
		tavola.executeCustom("selectPerCd_sig_repertorio_tp_repertorio");
		List v = tavola.getElencoRisultati();

		if (v.size()>0)
			return (Tb_repertorio) v.get(0);
		return null;
	}
}
