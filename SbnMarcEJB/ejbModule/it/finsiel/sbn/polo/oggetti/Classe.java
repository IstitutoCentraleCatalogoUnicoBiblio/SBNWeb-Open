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

import static it.finsiel.sbn.polo.factoring.util.ValidazioneDati.coalesce;
import static it.finsiel.sbn.polo.factoring.util.ValidazioneDati.isFilled;
import static it.finsiel.sbn.polo.factoring.util.ValidazioneDati.isT001Dewey;
import static it.finsiel.sbn.polo.factoring.util.ValidazioneDati.listToMap;
import static it.finsiel.sbn.polo.factoring.util.ValidazioneDati.trunc;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_classeResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_sistemi_classi_bibliotecheResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_claResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_classe_thesResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_classe_titResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.NormalizzaNomi;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.ConverterDate;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ElencoParole;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.SimboloDewey;
import it.finsiel.sbn.polo.factoring.util.TimbroCondivisione;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.orm.Tb_classe;
import it.finsiel.sbn.polo.orm.Tb_codici;
import it.finsiel.sbn.polo.orm.Tr_sistemi_classi_biblioteche;
import it.finsiel.sbn.polo.orm.Tr_tit_cla;
import it.finsiel.sbn.polo.orm.viste.Vl_classe_the;
import it.finsiel.sbn.polo.orm.viste.Vl_classe_tit;
import it.iccu.sbn.ejb.model.unimarcmodel.A676;
import it.iccu.sbn.ejb.model.unimarcmodel.A686;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaSoggettoDescrittoreClassiReperType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

/**
 * Classe Classe.java
 *
 * @author
 * @author
 * @author
 *
 * @version 17-mar-03
 */
public class Classe extends Tb_classe {

	private static final long serialVersionUID = 8716734328940789868L;

	private static int DS_CLASSE_MAX_LENGTH = ResourceLoader.getPropertyInteger("DS_CLASSE_MAX_LENGTH");

	private static Category log = Category.getInstance("iccu.serversbnmarc.Classe");
	private boolean filtriValorizzati;

	public Classe() {
	}

	/**
	 * Metodo per cercare la classe con numero di identificativo: ricerca su
	 * Tb_classe con ID
	 *
	 * @throws InfrastructureException
	 */
	public Tb_classe cercaClassePerID(String id) throws EccezioneDB,
			EccezioneSbnDiagnostico, InfrastructureException {
		Tb_classe classe = new Tb_classe();
		//almaviva5_20141114 test edizione ridotta
		SimboloDewey sd = SimboloDewey.parse(id);

		if (sd.isDewey()) {
			String edizione = Decodificatore.convertUnimarcToSbn("ECLA", sd.getEdizione());
			if (!isFilled(edizione))
            	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente

			classe.setCD_SISTEMA(sd.getSistema() + edizione );
			classe.setCD_EDIZIONE(edizione);
		} else {
			classe.setCD_SISTEMA(sd.getSistema() );
			classe.setCD_EDIZIONE(sd.getEdizione());
		}
		classe.setCLASSE(sd.getSimbolo());

		Tb_classeResult tavola = new Tb_classeResult(classe);

		tavola.valorizzaElencoRisultati(tavola.selectPerKey(classe.leggiAllParametro()));
		List response = new ArrayList(tavola.getElencoRisultati());

		if (response.size() > 0)
			return (Tb_classe) response.get(0);
		else
			return null;
	}

	public TableDao cercaClassePerIDInTavola(String id) throws EccezioneDB,
			EccezioneSbnDiagnostico, InfrastructureException {
		Tb_classe classe = new Tb_classe();
		//almaviva5_20141114 edizioni ridotte
        SimboloDewey sd = SimboloDewey.parse(id);
         //almaviva5_20090414
        if (sd.isDewey()) {
        	String edizione = Decodificatore.convertUnimarcToSbn("ECLA", sd.getEdizione());
        	if (!isFilled(edizione))
            	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente

        	classe.setCD_SISTEMA(sd.getSistema() + edizione);
        	classe.setCD_EDIZIONE(edizione);
        } else {
        	classe.setCD_SISTEMA(sd.getSistema());
            classe.setCD_EDIZIONE("  ");
        }
    	classe.setCLASSE(sd.getSimbolo());

		Tb_classeResult tavola = new Tb_classeResult(classe);

		tavola.valorizzaElencoRisultati(tavola.selectPerKey(classe.leggiAllParametro()));
		return tavola;
	}

	/**
	 * metodo per la verifica in fase di validazione per la creazione di una
	 * classe Controlla l'esistenza di una classe per id senza usare filtri sul
	 * flag "fl_canc"
	 *
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public Tb_classe verificaEsistenzaID(String id)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		Tb_classe classe = new Tb_classe();
		//almaviva5_20141114 test edizione ridotta
		SimboloDewey sd = SimboloDewey.parse(id);

		if (sd.isDewey()) {
			String edizione = Decodificatore.convertUnimarcToSbn("ECLA", sd.getEdizione());
			if (!isFilled(edizione))
            	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente

			classe.setCD_SISTEMA(sd.getSistema() + edizione );
			classe.setCD_EDIZIONE(edizione);
		} else {
			classe.setCD_SISTEMA(sd.getSistema() );
			classe.setCD_EDIZIONE(sd.getEdizione());
		}
		classe.setCLASSE(sd.getSimbolo());

		Tb_classeResult tavola = new Tb_classeResult(classe);

		tavola.executeCustom("selectEsistenzaId");
		List response = tavola.getElencoRisultati();

		if (response.size() > 0)
			return (Tb_classe) response.get(0);
		else
			return null;
	}

	/**
	 * Esegue una ricerca delle classi legate ad un documento
	 *
	 * @return vettore di viste Vl_classe_tit
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public TableDao cercaClassePerTitolo(String id_titolo, String ordinamento)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {

		Vl_classe_tit tit_tit = new Vl_classe_tit();
		tit_tit.setBID(id_titolo);
		Vl_classe_titResult tavola = new Vl_classe_titResult(tit_tit);

		tavola.executeCustom("selectClassePerTitolo", ordinamento);

		reorder(tavola, ordinamento);
		return tavola;
	}

	public int contaClassePerTitolo(String id_titolo)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		Vl_classe_tit tit_tit = new Vl_classe_tit();
		tit_tit.setBID(id_titolo);
		Vl_classe_titResult tavola = new Vl_classe_titResult(tit_tit);

		tavola.executeCustom("countClassePerTitolo");
		int n = conta(tavola);

		return n;
	}

	public int contaClassePerThesauro(String idArrivo)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		Vl_classe_the cla_the = new Vl_classe_the();
		cla_the.setDID(idArrivo);
		Vl_classe_thesResult tavola = new Vl_classe_thesResult(cla_the);
		tavola.mergeParametro(this.leggiAllParametro());

		tavola.executeCustom("contaClassePerThesauro");
		int n = conta(tavola);

		return n;
	}


	public TableDao cercaClassePerThesauro(String idArrivo, String tipoOrd)
		throws IllegalArgumentException, InvocationTargetException,	Exception {
		Vl_classe_the cla_the = new Vl_classe_the();
		cla_the.setDID(idArrivo);
		Vl_classe_thesResult tavola = new Vl_classe_thesResult(cla_the);
		tavola.mergeParametro(this.leggiAllParametro());

		tavola.executeCustom("selectClassePerThesauro", tipoOrd);
		return tavola;
}

	public TableDao cercaClassePerTitoloSistemaEdizione(String id_titolo,
			String sistema, String edizione, String ordinamento)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {

		Vl_classe_tit tit_tit = new Vl_classe_tit();
		tit_tit.setBID(id_titolo);
		tit_tit.setCD_SISTEMA(sistema);
		tit_tit.setCD_EDIZIONE(edizione);

		Vl_classe_titResult tavola = new Vl_classe_titResult(tit_tit);

		tavola.executeCustom("selectClassePerTitoloSistemaEdizione", ordinamento);
		return tavola;
	}

	public int contaClassePerTitoloSistemaEdizione(String id_titolo,
			String sistema, String edizione) throws IllegalArgumentException,
			InvocationTargetException, Exception {
		Vl_classe_tit tit_tit = new Vl_classe_tit();
		tit_tit.setBID(id_titolo);
		tit_tit.setCD_SISTEMA(sistema);
		tit_tit.setCD_EDIZIONE(edizione);

		Vl_classe_titResult tavola = new Vl_classe_titResult(tit_tit);

		tavola.executeCustom("countClassePerTitoloSistemaEdizione");
		int n = conta(tavola);

		return n;
	}

	public boolean creaClasse(String id, String cd_livello, String cd_sistema,
			String cd_edizione, String classe, String ds_classe, String user,
			String _condiviso, SbnIndicatore c9_676, String ultTermine)
			throws EccezioneDB, InfrastructureException,
			EccezioneSbnDiagnostico {

		//almaviva5_20151002
		if (ValidazioneDati.length(ds_classe) > DS_CLASSE_MAX_LENGTH)
			throw new EccezioneSbnDiagnostico(3219);	//lunghezza eccessiva

		if (isT001Dewey(id))
			return creaClasseClassificazioneDewey(cd_livello, cd_edizione,
					classe, ds_classe, user, _condiviso, c9_676.toString(),
					ultTermine);
		else
			return creaClasseClassificazioneNonDewey(cd_livello, cd_sistema,
					cd_edizione, classe, ds_classe, user, _condiviso,
					ultTermine);
	}

	private boolean creaClasseClassificazioneDewey(String cd_livello,
			String cd_edizione, String classe, String ds_classe, String user,
			String _condiviso, String c9_676, String ultTermine)
			throws EccezioneDB, InfrastructureException,
			EccezioneSbnDiagnostico {

		//almaviva5_20141114 edizione ridotta
		cd_edizione = Decodificatore.convertUnimarcToSbn("ECLA", cd_edizione);

		Tb_classe tb_classe = new Tb_classe();
		tb_classe.setCD_SISTEMA("D" + cd_edizione);
		if (cd_edizione == null)
			tb_classe.setCD_EDIZIONE("  ");
		else
			tb_classe.setCD_EDIZIONE(cd_edizione);

		tb_classe.setCLASSE(classe);
		tb_classe.setCD_LIVELLO(cd_livello);
		tb_classe.setDS_CLASSE(ds_classe);
		tb_classe.setFL_CANC(" ");
		if (c9_676 == null) {
			tb_classe.setFL_COSTRUITO("N");
		} else {
			tb_classe.setFL_COSTRUITO(c9_676);
		}
		tb_classe.setFL_SPECIALE("N");
		tb_classe.setUTE_INS(user);
		tb_classe.setUTE_VAR(user);
		// Timbro Condivisione
		tb_classe.setFL_CONDIVISO(_condiviso);
		tb_classe.setTS_CONDIVISO(TableDao.now());
		tb_classe.setUTE_CONDIVISO(user);

		// almaviva5_20090401 #2780
		tb_classe.setULT_TERM(ultTermine);

		tb_classe.setKY_CLASSE_ORD(creaKeyOrdinamentoClasse(ds_classe));

		Tb_classeResult tavola = new Tb_classeResult(tb_classe);

		tavola.insert(tb_classe);

		return true;
	}

	static final int KEY_MAX_LENGTH = 34;

	private String creaKeyOrdinamentoClasse(String ds_classe) throws EccezioneSbnDiagnostico {
		if (!isFilled(ds_classe))
			return null;

		String key = NormalizzaNomi.normalizzazioneGenerica(ds_classe);
		//eliminazione delle stoplist
		StringBuilder buf = new StringBuilder(240);
		for (String parola : key.split("\\s")) {
			if (!ElencoParole.getInstance().contiene(parola) ) {
				buf.append(parola).append('\u0020');
			};
		}

		key = buf.toString().trim();
		if (!isFilled(key))
			return null;

		return trunc(key, KEY_MAX_LENGTH);
	}

	private boolean creaClasseClassificazioneNonDewey(String cd_livello,
			String cd_sistema, String cd_edizione, String classe,
			String ds_classe, String user, String _condiviso, String ultTermine)
			throws EccezioneDB, InfrastructureException,
			EccezioneSbnDiagnostico {
		Tb_classe tb_classe = new Tb_classe();
		tb_classe.setCD_SISTEMA(cd_sistema);
		if (cd_edizione == null)
			tb_classe.setCD_EDIZIONE("  ");
		else
			tb_classe.setCD_EDIZIONE(cd_edizione);

		tb_classe.setCLASSE(classe);
		tb_classe.setCD_LIVELLO(cd_livello);
		tb_classe.setDS_CLASSE(ds_classe);
		tb_classe.setFL_CANC(" ");
		tb_classe.setFL_COSTRUITO("N");
		tb_classe.setFL_SPECIALE("N");
		tb_classe.setUTE_INS(user);
		tb_classe.setUTE_VAR(user);
		// Timbro Condivisione
		tb_classe.setFL_CONDIVISO(_condiviso);
		tb_classe.setTS_CONDIVISO(TableDao.now());
		tb_classe.setUTE_CONDIVISO(user);

		// almaviva5_20090401 #2780
		tb_classe.setULT_TERM(ultTermine);

		tb_classe.setKY_CLASSE_ORD(creaKeyOrdinamentoClasse(ds_classe));

		Tb_classeResult tavola = new Tb_classeResult(tb_classe);

		tavola.insert(tb_classe);

		return true;
	}

	/** Legge da una tavola il valore del COUNT(*) */
	public int conta(TableDao tavola) throws EccezioneDB {
		return tavola.getCount();
	}

	/** Valorizza i filtri in base al contenuto del CercaAutoreType */
	public void valorizzaFiltri(CercaDatiAutType cerca) {
		valorizzaFiltri(this, cerca);
	}

	/**
	 * Method cercaAutorePerParoleNome.
	 *
	 * @param string
	 * @return TableDao
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public TableDao cercaClassePerParoleNome(String[] paroleNome,
			String ordinamento) throws IllegalArgumentException,
			InvocationTargetException, Exception {
		for (int i = 1; i <= paroleNome.length; i++)
			settaParametro("XXXparola" + i, paroleNome[i - 1]);
		Tb_classeResult tavola = new Tb_classeResult(this);

		tavola.executeCustom("selectPerParoleNome", ordinamento);
		//almaviva5_20141126 edizioni ridotte
		reorder(tavola, ordinamento);

		return tavola;
	}

	public int contaClassePerParoleNome(String[] paroleNome)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		for (int i = 1; i <= paroleNome.length; i++)
			settaParametro("XXXparola" + i, paroleNome[i - 1]);
		Tb_classeResult tavola = new Tb_classeResult(this);

		tavola.executeCustom("countPerParoleNome");
		int n = conta(tavola);

		return n;
	}

	/** Valorizza i filtri */
	public Tb_classe valorizzaFiltri(Tb_classe classe, CercaDatiAutType cerca) {
		CercaSoggettoDescrittoreClassiReperType cercaClasse = null;
		filtriValorizzati = true;
		if (cerca == null)
			return classe;
		if (cerca.getLivelloAut_Da() != null)
			classe.settaParametro(TableDao.XXXlivello_aut_da, Decodificatore
					.livelloSogliaDa(cerca.getLivelloAut_Da().toString()));
		if (cerca.getLivelloAut_A() != null)
			classe.settaParametro(TableDao.XXXlivello_aut_a, cerca
					.getLivelloAut_A().toString());
		if (cerca.getT005_Range() != null) {
			int filtro = cerca.getT005_Range().getTipoFiltroDate();
			if (filtro < 2) {
				classe.settaParametro(TableDao.XXXdata_var_Da, cerca
						.getT005_Range().getDataDa().toString());
				classe.settaParametro(TableDao.XXXdata_var_A, cerca
						.getT005_Range().getDataA().toString());
			} else if (filtro == 2) {
				classe.settaParametro(TableDao.XXXdata_ins_Da, cerca
						.getT005_Range().getDataDa().toString());
				classe.settaParametro(TableDao.XXXdata_ins_A, cerca
						.getT005_Range().getDataA().toString());
			}
		}
		if (cerca instanceof CercaSoggettoDescrittoreClassiReperType)
			cercaClasse = (CercaSoggettoDescrittoreClassiReperType) cerca;

		if (cercaClasse != null) {
			if (cercaClasse.getSistemaClassificazione() != null) {
				classe.setCD_SISTEMA(cercaClasse.getSistemaClassificazione().toString());
				classe.settaParametro(TableDao.XXXcd_sistema, cercaClasse.getSistemaClassificazione().toString());
			}

			if (cercaClasse.getV_676() != null) {
				classe.setCD_EDIZIONE(cercaClasse.getV_676().toString());
				classe.settaParametro(TableDao.XXXcd_edizione, cercaClasse.getV_676().toString());
			}
		}
		return classe;
	}

	public int contaClassePerNomeLike(String nome)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		Tb_classeResult tavola = new Tb_classeResult(this);
		settaParametro(TableDao.XXXstringaLike, nome);
		tavola.executeCustom("countPerNomeLike");
		int n = conta(tavola);

		return n;
	}

	public TableDao cercaClassePerNomeLike(String nome, String ordinamento)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		Tb_classeResult tavola = new Tb_classeResult(this);
		settaParametro(TableDao.XXXstringaLike, nome);
		tavola.executeCustom("selectPerNomeLike", ordinamento);
		//almaviva5_20141126 edizioni ridotte
		reorder(tavola, ordinamento);

		return tavola;
	}

	/**
	 * Method updateClasse.
	 *
	 * @param ultTermine
	 * @param _livelloAut
	 * @param _t676
	 * @param _t686
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void updateClasse(Tb_classe tb_classe, SbnLivello livelloAut,
			String user, A676 t676, A686 t686, TimestampHash timeHash,
			String _condiviso, String _fl_costruito, String ultTermine)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		tb_classe.setUTE_VAR(user);
		if (livelloAut != null)
			tb_classe.setCD_LIVELLO(livelloAut.toString());
		if (t676 != null) {
			if (t676.getC_676() != null)
				tb_classe.setDS_CLASSE(t676.getC_676());
			if (t676.getC9_676() != null)
				tb_classe.setFL_COSTRUITO(t676.getC9_676().toString());
		}
		if (t686 != null)
			if (t686.getC_686() != null)
				tb_classe.setDS_CLASSE(t686.getC_686());

		// almaviva5_20090401 #2780
		if (ultTermine != null)
			tb_classe.setULT_TERM(ultTermine);

		tb_classe.setKY_CLASSE_ORD(creaKeyOrdinamentoClasse(tb_classe.getDS_CLASSE()));

		//almaviva5_20151002
		if (ValidazioneDati.length(tb_classe.getDS_CLASSE()) > DS_CLASSE_MAX_LENGTH)
			throw new EccezioneSbnDiagnostico(3219);	//lunghezza eccessiva

		String chiavePerTimeHash = null;
		chiavePerTimeHash = tb_classe.getCD_SISTEMA()
				+ tb_classe.getCD_EDIZIONE() + tb_classe.getCLASSE().trim();
		tb_classe.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash
				.getTimestamp("Tb_classe", chiavePerTimeHash)));
		TimbroCondivisione timbroCondivisione = new TimbroCondivisione();
		timbroCondivisione.modificaTimbroCondivisione(tb_classe, user,
				_condiviso);

		Tb_classeResult tavola = new Tb_classeResult(tb_classe);
		tavola.executeCustom("updatePerModifica");

	}

	public void cercaLegameClasseTitolo(String id)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		Tr_tit_cla classe = new Tr_tit_cla();
		//almaviva5_20141114 edizioni ridotte
        SimboloDewey sd = SimboloDewey.parse(id);
        //almaviva5_20090414
        if (sd.isDewey()) {
        	String edizione = Decodificatore.convertUnimarcToSbn("ECLA", sd.getEdizione());
        	if (!ValidazioneDati.isFilled(edizione))
            	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente

        	classe.setCD_SISTEMA(sd.getSistema() + edizione);
        	classe.setCD_EDIZIONE(edizione);
        } else {
        	classe.setCD_SISTEMA(sd.getSistema());
            classe.setCD_EDIZIONE("  ");
        }
    	classe.setCLASSE(sd.getSimbolo());

		Tr_tit_claResult tavola = new Tr_tit_claResult(classe);
		// Richiamo la selectTitoloPerClasse al posto della
		// selectTitoloPerSoggetto
		// in quando la stessa non esiste nel'xml corrispondente l'unica
		// soluzione possibile rimane la classe
		// ORIGINALEtavola.executeCustom("selectTitoloPerSoggetto");
		tavola.executeCustom("selectTitoloPerClasse");
		List vec = tavola.getElencoRisultati();

		if (vec != null)
			throw new EccezioneSbnDiagnostico(3091,
					"ci sono dei legami con i titoli");
	}

	/**
	 * Method cancellaClasse.
	 *
	 * @param classeDaCancellare
	 * @param string
	 * @throws Exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public void cancellaClasse(Tb_classe classeDaCancellare, String user,
			TimestampHash timeHash) throws IllegalArgumentException,
			InvocationTargetException, Exception {
		classeDaCancellare.setFL_CANC("S");
		classeDaCancellare.setUTE_VAR(user);

		//almaviva5_20090414
		String id = classeDaCancellare.getCD_SISTEMA() +
			classeDaCancellare.getCD_EDIZIONE() + classeDaCancellare.getCLASSE();

		classeDaCancellare.setTS_VAR(ConverterDate.SbnDataVarToDate(timeHash.getTimestamp("Tb_classe", id)));
		Tb_classeResult tavola = new Tb_classeResult(classeDaCancellare);

		tavola.executeCustomUpdate("cancellaClasse");

	}

	public void updateVersione(String classe, String ediz, String sist,
			String ute_var, SbnDatavar time) throws IllegalArgumentException,
			InvocationTargetException, Exception {
		setCLASSE(classe);
		setCD_EDIZIONE(ediz);
		setCD_SISTEMA(sist);
		setUTE_VAR(ute_var);
		setTS_VAR(ConverterDate.SbnDataVarToDate(time));
		Tb_classeResult tb_classeResult = new Tb_classeResult(this);

		tb_classeResult.executeCustomUpdate("updateVersione");

	}

	//almaviva5_20120702 #5032
	public boolean verificaEdizioneDeweyBiblioteca(SbnUserType utente, String sistema, String edizione)
			throws Exception	{
		Tr_sistemi_classi_biblioteche scb = new Tr_sistemi_classi_biblioteche();
		String bib = utente.getBiblioteca();
		scb.setCD_POLO(bib.substring(0, 3));
		scb.setCD_BIBLIOTECA(bib.substring(3));
		scb.setCD_SISTEMA(sistema);
		scb.setCD_EDIZIONE(edizione);

		Tr_sistemi_classi_bibliotecheResult dao = new Tr_sistemi_classi_bibliotecheResult(scb);

		dao.executeCustom("verificaEdizioneDeweyBiblioteca");
		int n = conta(dao);

		return (n > 0);

	}

	//almaviva5_20141126 edizioni ridotte
	private void reorder(TableDao tavola, String ord) throws EccezioneDB {
		if (tavola.getCount() < 2 )
			return;

		Comparator<Tb_classe> c = null;
		List<Tb_classe> classi = tavola.getElencoRisultati();
		final StringBuilder key = new StringBuilder(32);
		final Map<String, Tb_codici> edizioni = listToMap(Decodificatore.getCodici("ECLA"), String.class, "cd_tabellaTrim");
		edizioni.put("  ", new Tb_codici());
		SbnTipoOrd tipoOrd = SbnTipoOrd.valueOf(ord == null ? "1" : ord.substring(ord.length() - 1));

		switch (tipoOrd.getType()) {
		case SbnTipoOrd.VALUE_0_TYPE:	//ord per simbolo
			 c = new Comparator<Tb_classe>() {
				public int compare(Tb_classe c1, Tb_classe c2) {
					key.setLength(0);
					key.append(c1.getCD_SISTEMA().charAt(0))
						.append(coalesce(edizioni.get(c1.getCD_EDIZIONE()).getCD_UNIMARC(), c1.getCD_SISTEMA().substring(1)))
						.append(c1.getCLASSE());
					String k1 = key.toString();
					key.setLength(0);
					key.append(c2.getCD_SISTEMA().charAt(0))
						.append(coalesce(edizioni.get(c2.getCD_EDIZIONE()).getCD_UNIMARC(), c2.getCD_SISTEMA().substring(1)))
						.append(c2.getCLASSE());
					String k2 = key.toString();

					return k1.compareTo(k2);
				}
			};
			break;

		case SbnTipoOrd.VALUE_2_TYPE:	//Data var./ins. + Identificativo
			 c = new Comparator<Tb_classe>() {
				public int compare(Tb_classe c1, Tb_classe c2) {
					int cmp = c1.getTS_VAR().compareTo(c2.getTS_VAR());
					cmp = (cmp != 0) ? cmp : c1.getTS_INS().compareTo(c2.getTS_INS());
					if (cmp == 0) {
						key.setLength(0);
						key.append(c1.getCD_SISTEMA().charAt(0))
							.append(coalesce(edizioni.get(c1.getCD_EDIZIONE()).getCD_UNIMARC(), c1.getCD_SISTEMA().substring(1)))
							.append(c1.getCLASSE());
						String k1 = key.toString();
						key.setLength(0);
						key.append(c2.getCD_SISTEMA().charAt(0))
							.append(coalesce(edizioni.get(c2.getCD_EDIZIONE()).getCD_UNIMARC(), c2.getCD_SISTEMA().substring(1)))
							.append(c2.getCLASSE());
						String k2 = key.toString();
						cmp = k1.compareTo(k2);
					}
					return cmp;
				}
			};
			break;

		default:
			return;
		}

		Collections.sort(classi, c);
	}

}
