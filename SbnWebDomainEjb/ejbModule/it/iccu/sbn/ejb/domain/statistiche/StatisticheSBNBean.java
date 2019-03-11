/**
 *
 */
package it.iccu.sbn.ejb.domain.statistiche;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.statistiche.AreaVO;
import it.iccu.sbn.ejb.vo.statistiche.DettVarStatisticaVO;
import it.iccu.sbn.ejb.vo.statistiche.StatisticaVO;
import it.iccu.sbn.persistence.dao.amministrazione.Tbf_biblioteca_in_poloDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.statistiche.StatisticheDao;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.iccu.sbn.polo.orm.statistiche.Tb_stat_parameter;
import it.iccu.sbn.polo.orm.statistiche.Tbf_config_statistiche;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ticket.TicketChecker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

/**
 *
 * <!-- begin-user-doc -->
 * A generated session bean
 * <!-- end-user-doc -->
 * *
 * <!-- begin-xdoclet-definition -->
 * @ejb.bean name="AmministrazioneBiblioteca"
 *           description="A session bean named AmministrazioneBiblioteca"
 *           display-name="AmministrazioneBiblioteca"
 *           jndi-name="sbnWeb/AmministrazioneBiblioteca"
 *           type="Stateless"
 *           transaction-type="Container"
 *           view-type = "remote"
 *
 * @ejb.util
 *   generate="no"
 *
 * <!-- end-xdoclet-definition -->
 * @generated
 */

public abstract class StatisticheSBNBean extends TicketChecker implements SessionBean {

	private static final long serialVersionUID = 5983426042968528695L;
	private static Logger log = Logger.getLogger(StatisticheSBNBean.class);

	private static final Pattern REGEX_TITOLO_COLONNA = Pattern.compile("\\{.+?\\}", Pattern.CASE_INSENSITIVE);


	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @ejb.create-method view-type="remote"
	 * <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 */
	public void ejbCreate() {
		log.info("creato ejb");
		return;
	}


	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		return;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 * @throws DaoManagerException
	 * @ejb.interface-method view-type="remote"
	 * <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean method stub
	 */
    // Il metodo restituisce l'elenco delle Aree per l'utente nel carico sia riempita la mappa attivita con i permessi utente,
	// altrimenti restituisce tutte le aree.
    public List<AreaVO> getAreeUtente(Map attivita) throws RemoteException {
		List<AreaVO> output = new ArrayList<AreaVO>();
    	try {
    		StatisticheDao statDAO = new StatisticheDao();
    		List<Tbf_config_statistiche> listaAree = statDAO.cercaAreeUtente(attivita);
    		for (int i = 0; i < listaAree.size(); i++) {
    			Tbf_config_statistiche areaDB = listaAree.get(i);
    			AreaVO area = new AreaVO();
    			String idAreaSezione = areaDB.getId_area_sezione().trim();
				area.setIdAreaSezione(idAreaSezione);
    			area.setSequenzaOrdinamento(areaDB.getSeq_ordinamento());
	    		ResourceBundle bundle = ResourceBundle.getBundle("it.iccu.sbn.web.resources.AmministrazioneSistemaLabels");
	    		//almaviva5_20120926
	    		try {
					area.setDescrizione(bundle.getString(idAreaSezione));
				} catch (MissingResourceException e) {
					area.setDescrizione(idAreaSezione);
				}
    			output.add(area);
    		}
    		output = ordinaAreaVO(output);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
		return output;
    }

    public List<StatisticaVO> getListaStatistiche(String area, String ticket) throws RemoteException {
    	List<StatisticaVO> output = new ArrayList<StatisticaVO>();
    	try {
    		StatisticheDao statDAO = new StatisticheDao();
    		List<Tbf_config_statistiche> listaStatistiche = statDAO.getListaStatistiche(area);
    		int countStat = 0;
    		for (int i = 0; i < listaStatistiche.size(); i++) {
    			Tbf_config_statistiche areaDB = listaStatistiche.get(i);
    			StatisticaVO stat = new StatisticaVO();
    			stat.setPrg(++countStat);
    			stat.setIdConfig(String.valueOf(areaDB.getId_stat()));
    			stat.setIdAreaSezione(areaDB.getId_area_sezione().trim());
    			stat.setSeqOrdinamento(areaDB.getSeq_ordinamento());
    			//nome statistica senza spazi
				String[] nome_composto = areaDB.getNome_statistica().split("_");
				String nomeConSpazi="";
				for (int x=0; x< nome_composto.length; x++) {
					nomeConSpazi=nomeConSpazi+nome_composto[x]+" ";
				}
    			stat.setNomeStatistica(nomeConSpazi);
    			stat.setTipoQuery(areaDB.getTipo_query().trim());
    			stat.setFlPoloBiblio(areaDB.getFl_polo_biblio().trim());
    			stat.setFlTxt(areaDB.getFl_txt().trim());
    			output.add(stat);
    		}
    		output = ordinaStatisticheVO(output);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	return output;
    }
/**
	 * Metodo di ordinamento di un List di AreaVO in base alla sequenza ordinamento.
	 * Una volta effettuato l'ordinamento, viene impostato anche l'indice per la visualizzazione nella JSP.
	 * L'ordinamento viene effettuato con algoritmo di selection sort.
	 * @param a L'List di AreaVO che verrà ordinato.
	 * @return L'List di AreaVO ordinato.
	 */
	private static List<AreaVO> ordinaAreaVO(List<AreaVO> a) {
		int n = a.size();
		for (int i=0; i<n-1; i++) {
			// trova il piu’ piccolo elemento da i a n-1
			int jmin = i;
			for (int j=i+1; j<n; j++) {
				if (new Integer(a.get(j).getSequenzaOrdinamento()) < new Integer(a.get(jmin).getSequenzaOrdinamento()))
				jmin = j;
			}
			// scambia gli elementi i e jmin
			AreaVO aux = a.get(jmin);
			a.add(jmin, a.get(i));
			a.remove(jmin + 1);
			a.add(i, aux);
			a.remove(i + 1);
		}
		return a;
    }

	private static List<StatisticaVO> ordinaStatisticheVO(List<StatisticaVO> a) {
		int n = a.size();
		for (int i=0; i<n-1; i++) {
			// trova il piu’ piccolo elemento da i a n-1
			int jmin = i;
			for (int j=i+1; j<n; j++) {
				if (new Integer(a.get(j).getSeqOrdinamento()) < new Integer(a.get(jmin).getSeqOrdinamento()))
				jmin = j;
			}
			// scambia gli elementi i e jmin
			StatisticaVO aux = a.get(jmin);
			a.add(jmin, a.get(i));
			a.remove(jmin + 1);
			a.add(i, aux);
			a.remove(i + 1);
		}
		return a;
    }

    public List<DettVarStatisticaVO> getDettVarStatistica(int idStatistica, String ticket) throws RemoteException {
		List<DettVarStatisticaVO> output = new ArrayList<DettVarStatisticaVO>();
    	try {
    		StatisticheDao statDAO = null;
    		statDAO = new StatisticheDao();
    		Tbf_config_statistiche stat = statDAO.getStatistica(idStatistica);
    		if(stat != null){
    			List<Tb_stat_parameter> listaVariabili = statDAO.getDettVariabili(stat);
    			for (int i = 0; i < listaVariabili.size(); i++) {
    				Tb_stat_parameter var = listaVariabili.get(i);
    				DettVarStatisticaVO stati = new DettVarStatisticaVO();
    				stati.setIdStatistica(String.valueOf(var.getId_stat().getId_stat()));
    				stati.setNomeVar(var.getNome().trim());
    				stati.setTipoVar(var.getTipo().trim());
    				stati.setValore(var.getValore().trim());
    				stati.setEtichettaNomeVar(var.getEtichetta_nome().trim());
       				stati.setObbligatorio(var.getObbligatorio().trim());
   				output.add(stati);
    			}
    		}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
		return output;
    }

    public boolean elaboraStatistica(StatisticaVO statistica, String ticket, BatchLogWriter batchLog)
    throws ResourceNotFoundException, ApplicationException, ValidationException, DataException	{
    	boolean output = false;
    	Logger _log = batchLog.getLogger();
    	try {
    		StatisticheDao statDAO = null;
    		statDAO = new StatisticheDao();
    		List<DettVarStatisticaVO> listaVar = new ArrayList<DettVarStatisticaVO>();
    		Tbf_config_statistiche stat = statDAO.getStatistica(Integer.parseInt(statistica.getIdConfig()));
    		if(stat != null){
    			//scorro le variabili impostate in input e le ricerca su tb_stat_parameter,
    			//trovate costruisce un'array che servirà da input alla costrizione della query
    			statDAO = new StatisticheDao();
    			List lista = null;
    			if (stat.getTipo_query() != null){
    				if (stat.getTipo_query().equals("0")){
    					_log.debug("Tipo query senza parametri");
    				}
    			}
    			_log.debug(stat.getQuery());
    			if (statistica.getElencoVariabili() == null){
    				lista = statDAO.creaQuery(stat, null, statistica);
    			}else{
    				for (int i = 0; i < statistica.getElencoVariabili().size(); i++) {
    					DettVarStatisticaVO v = null;
    					DettVarStatisticaVO var = statistica.getElencoVariabili().get(i);
    					Tb_stat_parameter variabile = statDAO.getVariabile(Integer.valueOf(var.getIdStatistica()), var.getNomeVar());
    					if ((variabile.getId_stat().getId_stat())== Integer.valueOf(var.getIdStatistica())){
    						v = new DettVarStatisticaVO();
    						v.setTipoVar(var.getTipoVar());
    						if (var.getTipoVar() != null && var.getTipoVar().equals("combo")){
    							//con il codice
    							v.setEtichettaNomeVar(var.getEtichettaNomeVar());
    							v.setNomeVar(var.getNomeVar());
    							v.setValore(var.getValore());
    						}else{
    							v.setEtichettaNomeVar(var.getEtichettaNomeVar());
    							v.setNomeVar(var.getNomeVar());
    							v.setValore(var.getValore());
    						}
    					}
    					String valore = v.getValore();
    					//						if (valore != null && valore.trim().equals("")){
    					//							valore = "*";
    					//						}
    					statistica.getParametriRichiestaInput().add((v.getEtichettaNomeVar() + "|" + v.getNomeVar() + "|" + valore));
    					_log.debug(v);
    					listaVar.add(v);
    				}
    				_log.debug(stat.getQuery());
    				lista = statDAO.creaQuery(stat, listaVar, statistica);
    			}
    			if (lista != null){
    				boolean flTxt = (ValidazioneDati.equals(stat.getFl_txt(), "S"));
    				if (flTxt){
    					// Creazione file di output
     					BufferedWriter writer = new BufferedWriter(new FileWriter(statistica.getDownloadPath() + File.separator + statistica.getFileName()));
     					try {
     						if (lista != null){
     							for (int i=0; i< lista.size(); i++)	{
     								String campo = ((String)lista.get(i));
     								writer.write(campo);
     								writer.newLine();
     							}
     						}
     					} catch (FileNotFoundException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					} catch (IOException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					} finally  {
    						writer.close();
    					}
    	   				flTxt = false;
    					output = true;
   				}else{
    					if (stat.getColonne_output() != null){
    						String [] colonne= stat.getColonne_output().split("\\|");
    						int totColonne=0;
    						totColonne=colonne.length;
    						// esame delle intestazioni composte
    						for (int y=0; y< totColonne; y++) {
    							String[] intestCol_composto=colonne[y].split("_");
    							String intestCol="";
    							for (int x=0; x< intestCol_composto.length; x++) {
    								intestCol=intestCol+intestCol_composto[x]+" ";
    							}
    							if ( intestCol!=null && intestCol.trim().length()>0) {
    								colonne[y]=completaTitoloColonna(intestCol, listaVar).trim();
    							}

    						}
    						Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
    						int totRighe=lista.size();
    						int numRiga=0;
    						if (numRiga==0)	{
    							String[] nome_composto = statistica.getParExc().getQuery().trim().split(" ");
    							String nomeConUnderscore="";
    							for (int x=0; x< nome_composto.length; x++) {
    								nomeConUnderscore=nomeConUnderscore+nome_composto[x]+"_";
    							}
    							statistica.setNomeStatisticaPerLog(nomeConUnderscore);
    						}
    						statistica.getParExc().getTitoli().add("Area: "+ statistica.getParExc().getArea());
    						statistica.getParExc().getTitoli().add("Query eseguita: " +  statistica.getSeqOrdinamento().toString() + " - '" + statistica.getParExc().getQuery()+ "'" );
    						if (statistica.getParametriRichiestaInput() != null && statistica.getParametriRichiestaInput().size() > 0 ){
    							for (int x=0; x< statistica.getParametriRichiestaInput().size(); x++) {
    								String parRich [] = statistica.getParametriRichiestaInput().get(x).split("\\|");
    								if (parRich.length > 2){
    									if (parRich[2].equals("null")){
    										parRich[2] = new String("");
    									}
    									statistica.getParExc().getTitoli().add(new String(parRich[0] + ": " +parRich[2] + " "));
    								}else{
    									statistica.getParExc().getTitoli().add(new String(parRich[0] + ":  "));
    								}
    							}
    						}
    						statistica.getParExc().getTitoli().add("Data elaborazione: '" + DateUtil.formattaDataOra(ts)+ "'" ); // rs.getStatement().toString()
    						statistica.getParExc().getTitoli().add("ID batch: '" + statistica.getIdBatch()+ "'" ); // rs.getStatement().toString()

    						int numColumn=colonne.length;
    						for (int y=0; y< numColumn && numRiga==0; y++) {
    							statistica.getIntestazioni().add(new StrutturaCombo(colonne[y], "String"));
    						}
    						if (numColumn==1) {
    							for (int i=0; i< totRighe; i++)	{
    								statistica.getRisultati().add((Serializable) lista.get(i));
    							}
    						} else {
    							Object[] riga =new Object[numColumn];
    							for (int i=0; i< totRighe; i++)	{
    								riga=(Object[]) lista.get(i);
    								statistica.getRisultati().add(riga);
    							}
    						}
    					}
    					output = true;
    				}
    			}else{
    				throw new DataException ();
    			}
    		}
    	}catch (DaoManagerException e) {
    		_log.error("", e);
    		throw new DataException (e);
    	} catch (Exception e) {
    		_log.error("", e);
    		throw new DataException (e);
    	}
    	return output;
    }

	public String completaTitoloColonna(String colonna,
			List<DettVarStatisticaVO> listaVar) {

		String tmp = new String(colonna);

		while (true) {
			boolean found = false;
			Matcher m = REGEX_TITOLO_COLONNA.matcher(tmp);
			if (!m.find())
				break;

			String nomeVar = m.group().substring(1, m.group().length() - 1);
			for (DettVarStatisticaVO v : listaVar) {
				if (ValidazioneDati.equals(nomeVar, v.getNomeVar())) {
					tmp = m.replaceFirst(v.getValore());
					found = true;
					break;
				}
			}
			if (!found)
				//almaviva5_20111215 sostituzione per parametro non trovato
				tmp = m.replaceFirst("???");

		}

		return tmp;
	}

	    public List<CodiceVO> getListaCodDescrDaTabGenerica(String codPolo, String valori0/*codBib*/, String valori1/*tabella*/,
	    		String  valori2/*codice*/, String valori3/*descrizione*/, String valori4/*valore codBib*/,String ticket)  throws RemoteException {
	    	List<CodiceVO> output = new ArrayList<CodiceVO>();
	    	try {
	    		Tbf_biblioteca_in_poloDao bibDao = null;
	    		bibDao = new Tbf_biblioteca_in_poloDao();
	    		Tbf_biblioteca_in_polo bib = bibDao.select(codPolo, valori4);
	    		StatisticheDao statDAO = new StatisticheDao();
	    		if(bib != null){
	    			if (valori3.length() == 4){
	    				List<String> listaCod = statDAO.getListaCodDescrDaTabGenerica(valori0, valori1/*tabella*/,
	    						valori2/*codice*/, valori4);
	    				if (listaCod != null && listaCod.size() > 0){
	    					for (int i = 0; i < listaCod.size(); i++) {
	    						String rec = listaCod.get(i);
	    						CodiceVO recDB = new CodiceVO();
	    						recDB.setCodice(rec);
	    						CodiciType tab = CodiciType.fromString(valori3);
	    						recDB.setDescrizione(CodiciProvider.cercaDescrizioneCodice(String.valueOf(rec.trim()),
	    								tab, CodiciRicercaType.RICERCA_CODICE_SBN));
	    						output.add(recDB);
	    					}
	    				}
	    			}else if (valori3.length() > 4 ){
	    				List<CodiceVO> listaCodDescr = statDAO.getListaCodDescrDaTabGenerica(bib, valori1/*tabella*/,
	    						valori2/*codice*/, valori3/*descrizione*/, valori0);
	    				for (int i = 0; i < listaCodDescr.size(); i++) {
	    					CodiceVO rec = listaCodDescr.get(i);
	    					CodiceVO recDB = new CodiceVO();
	    					recDB.setCodice(rec.getCodice());
	    					recDB.setDescrizione(rec.getDescrizione());
	    					output.add(recDB);
	    				}
	    			}
	    		}
	    	}
	    	catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    	return output;
	    }


}