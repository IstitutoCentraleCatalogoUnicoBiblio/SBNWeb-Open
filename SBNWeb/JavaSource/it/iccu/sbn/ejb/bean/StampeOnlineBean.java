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
package it.iccu.sbn.ejb.bean;

import it.iccu.sbn.ejb.AbstractStatelessSessionBean;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBStampeFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBiblioteca;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.domain.documentofisico.DocumentoFisicoCommon;
import it.iccu.sbn.ejb.domain.semantica.Semantica;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeBiblioteche;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeBollettario;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeBuoniOrdine;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeComunicazione;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeEtichette;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeFattura;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeFornitori;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeRichiesta;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeSchede;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeSuggerimentiBibliotecario;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeSuggerimentiLettore;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeTerminiThesauro;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeUtenti;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.StampeOnline;
import it.iccu.sbn.ejb.services.acquisizioni.ServiziAcquisizioni;
import it.iccu.sbn.ejb.services.bibliografica.ServiziBibliografici;
import it.iccu.sbn.ejb.services.servizi.ServiziErogazioneServizi;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.OutputStampaVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.StampaBollettarioDiffVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.StampaFornitoriDiffVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.StampaOrdiniDiffVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.StampaUtentiDiffVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaBibliotecheVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaFornitoriVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaSoggettarioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaUtentiVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampaType;
import it.iccu.sbn.ejb.vo.gestionestampe.common.StampeOnlineVO;
import it.iccu.sbn.ejb.vo.stampe.StampaOnLineVO;
import it.iccu.sbn.ejb.vo.stampe.StampaVo;
import it.iccu.sbn.util.jms.JMSUtil;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class StampeOnlineBean extends AbstractStatelessSessionBean implements StampeOnline {

	private static final long serialVersionUID = -5186131628926878996L;
	
	private static Logger logger = Logger.getLogger(StampeOnline.class);
	private AmministrazionePolo amministrazionePolo;

	private AmministrazionePolo getAmministrazionePolo() throws Exception {

		if (amministrazionePolo != null)
			return amministrazionePolo;

		amministrazionePolo = DomainEJBFactory.getInstance().getPolo();

		return amministrazionePolo;

	}

    public String stampaFornitori(StampaFornitoriVO stampaFornitoriVO)
            throws EJBException {
        String stampaFornitori = "";
        return stampaFornitori;
    }

    public Collection<StampaFornitoriVO> stampaFornitori_(
            StampaFornitoriVO stampaFornitoriVO) throws EJBException {
        Collection<StampaFornitoriVO> lstFornitori = new ArrayList<StampaFornitoriVO>();
        return lstFornitori;
    }

    public String stampaFornitori(StampaVo stampa)
    {
		JMSUtil manager;
		String idMessInoltro = "0";
//		boolean statoInoltro = true;
		try {

	    	InitialContext iniCtx = new InitialContext();
			Queue queA = (Queue) iniCtx.lookup("queue/A");
			manager = new JMSUtil(iniCtx);
			Map param = new HashMap();
			param.put("STATO", "HELD");
			param.put("COD_ATTIVITA", "STAMPA_FORNITORE");
			param.put("BIBLIOTECA", stampa.getCodBib());
			param.put("BIBLIOTECARIO", stampa.getUser());
			param.put("DATA_ORA_RICHIESTA", DateUtil.getDate()+DateUtil.getTime());
			param.put("DATA_ORA_ESECUZIONE_PROGRAMMATA", "cronExpr");
//			boolean statoInoltro = manager.sendQueue(queA, stampa,param);
			idMessInoltro = manager.sendQueue(queA, stampa,param);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		}
		return idMessInoltro;
    }

    public String stampaRegistroIngresso(StampaVo stampa) {
		JMSUtil manager;
		String idMessInoltro = "0";
//		boolean statoInoltro = true;
		try {
			return getAmministrazionePolo().prenotaElaborazioneDifferita(stampa, null);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idMessInoltro;
    }

    public String stampaStatisticheRegistroIngresso(StampaVo stampa)    {
		JMSUtil manager;
		String idMessInoltro = "0";
//		boolean statoInoltro = true;
		try {
			return getAmministrazionePolo().prenotaElaborazioneDifferita(stampa, null);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idMessInoltro;
    }

    public String stampaBollettino(StampaVo stampa)    {
		JMSUtil manager;
		String idMessInoltro = "0";
//		boolean statoInoltro = true;
		try {
			return getAmministrazionePolo().prenotaElaborazioneDifferita(stampa, null);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idMessInoltro;
    }

    public String stampaBuoniCarico(StampaVo stampa)    {
		JMSUtil manager;
		String idMessInoltro = "0";
//		boolean statoInoltro = true;
		try {
			return getAmministrazionePolo().prenotaElaborazioneDifferita(stampa, null);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idMessInoltro;
    }

    public String stampaRegistroTopografico(StampaVo stampa)
    {
		JMSUtil manager;
		String idMessInoltro = "0";
//		boolean statoInoltro = true;
		try {
			return getAmministrazionePolo().prenotaElaborazioneDifferita(stampa, null);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idMessInoltro;
    }

    public String elaboraStatistica(StampaVo statistica)
    {
		JMSUtil manager;
		String idMessInoltro = "0";
//		boolean statoInoltro = true;
		try {
			return getAmministrazionePolo().prenotaElaborazioneDifferita(statistica, null);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idMessInoltro;
    }

    public String stampaRegistroConservazione(StampaVo stampa)
    {
		JMSUtil manager;
		String idMessInoltro = "0";
//		boolean statoInoltro = true;
		try {
			return getAmministrazionePolo().prenotaElaborazioneDifferita(stampa, null);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			return idMessInoltro;
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idMessInoltro;
    }

    public String stampaStrumentiPatrimonio(StampaVo stampa)
    {

		String idMessInoltro = "0";
//		boolean statoInoltro = true;
		try {
			return getAmministrazionePolo().prenotaElaborazioneDifferita(stampa, null);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			return idMessInoltro;
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idMessInoltro;
    }

    public String stampaEtichette(StampaVo stampa)    {
		JMSUtil manager;
		String idMessInoltro = "0";
//		boolean statoInoltro = true;
		try {
			return getAmministrazionePolo().prenotaElaborazioneDifferita(stampa, null);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idMessInoltro;
    }

    public String stampaTerminiThesauro(StampaVo stampa)
    {
		JMSUtil manager;
		String idMessInoltro = "0";
//		boolean statoInoltro = true;
		try {

	    	InitialContext iniCtx = new InitialContext();
			Queue queA = (Queue) iniCtx.lookup("queue/A");
			manager = new JMSUtil(iniCtx);
			Map param = new HashMap();
			param.put("STATO", "HELD");
			param.put("COD_ATTIVITA", "STAMPA_TERMINI_THESAURO");
			param.put("BIBLIOTECA", stampa.getCodBib());
			param.put("BIBLIOTECARIO", stampa.getUser());
			param.put("DATA_ORA_RICHIESTA", DateUtil.getDate()+DateUtil.getTime());
			param.put("DATA_ORA_ESECUZIONE_PROGRAMMATA", "cronExpr");
//			boolean statoInoltro = manager.sendQueue(queA, stampa,param);
			idMessInoltro = manager.sendQueue(queA, stampa,param);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		}
		return idMessInoltro;
    }

    public String stampaBuoniOrdine(StampaVo stampa)
    {
		JMSUtil manager;
//    	boolean statoInoltro = true;
		String idMessInoltro = "0";
		try {
	    	InitialContext iniCtx = new InitialContext();
			Queue queA = (Queue) iniCtx.lookup("queue/A");
			manager = new JMSUtil(iniCtx);
			Map param = new HashMap();
			param.put("STATO", "HELD");
			param.put("COD_ATTIVITA", "STAMPA_BUONI_ORDINE");
			param.put("BIBLIOTECA", stampa.getCodBib());
			param.put("BIBLIOTECARIO", stampa.getUser());
			param.put("DATA_ORA_RICHIESTA", DateUtil.getDate()+DateUtil.getTime());
			param.put("DATA_ORA_ESECUZIONE_PROGRAMMATA", "cronExpr");
//			boolean statoInoltro = manager.sendQueue(queA, stampa,param);
			idMessInoltro = manager.sendQueue(queA, stampa,param);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		}
		return idMessInoltro;
//		return statoInoltro;
    }

    public String stampaSoggettario(StampaSoggettarioVO stampaSoggettarioVO)
            throws EJBException {
        String stampaSoggettario = "";
        return stampaSoggettario;
    }
    public String stampaFascicoli(StampaVo statistica)
    {
		JMSUtil manager;
		String idMessInoltro = "0";
//		boolean statoInoltro = true;
		try {
			return getAmministrazionePolo().prenotaElaborazioneDifferita(statistica, null);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idMessInoltro;
    }

/*
    public Collection<StampaSoggettarioVO> stampaSoggettario_(
            StampaSoggettarioVO stampaSoggettarioVO) throws EJBException {
        Collection<StampaSoggettarioVO> lstSoggettario = new ArrayList<StampaSoggettarioVO>();
        try {
            StampeDAO dao = DAOFactory.getDAO(StampeDAO.class);
            lstSoggettario = dao.stampaSoggettario_(stampaSoggettarioVO);
        } catch (ResourceNotFoundException e) {
            throw new EJBException(e);
        } catch (ApplicationException e) {
            throw new EJBException(e);
        }
        return lstSoggettario;
    }
*/
    public String stampaUtenti(StampaUtentiVO stampaUtentiVO)
            throws EJBException {
        String stampaUtenti = "";
        return stampaUtenti;
    }

    public String stampaUtente(StampaVo stampa)
    {
		JMSUtil manager;
//    	boolean statoInoltro = true;
		String idMessInoltro = "0";
		try {
	    	InitialContext iniCtx = new InitialContext();
			Queue queA = (Queue) iniCtx.lookup("queue/A");
			manager = new JMSUtil(iniCtx);
			Map param = new HashMap();
			param.put("STATO", "HELD");
			param.put("COD_ATTIVITA", "STAMPA_UTENTE");
			param.put("BIBLIOTECA", stampa.getCodBib());
			param.put("BIBLIOTECARIO", stampa.getUser());
			param.put("DATA_ORA_RICHIESTA", DateUtil.getDate()+DateUtil.getTime());
			param.put("DATA_ORA_ESECUZIONE_PROGRAMMATA", "cronExpr");
//			boolean statoInoltro = manager.sendQueue(queA, stampa,param);
			idMessInoltro = manager.sendQueue(queA, stampa,param);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		}
		return idMessInoltro;
//		return statoInoltro;
    }

    public String stampaBollettario(StampaVo stampa)
    {
		JMSUtil manager;
		String idMessInoltro = "0";
		try {
	    	InitialContext iniCtx = new InitialContext();
			Queue queA = (Queue) iniCtx.lookup("queue/A");
			manager = new JMSUtil(iniCtx);
			Map param = new HashMap();
			param.put("STATO", "HELD");
			param.put("COD_ATTIVITA", "STAMPA_BOLLETTARIO");
			param.put("BIBLIOTECA", stampa.getCodBib());
			param.put("BIBLIOTECARIO", stampa.getUser());
			param.put("DATA_ORA_RICHIESTA", DateUtil.getDate()+DateUtil.getTime());
			param.put("DATA_ORA_ESECUZIONE_PROGRAMMATA", "cronExpr");
			idMessInoltro = manager.sendQueue(queA, stampa,param);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		}
		return idMessInoltro;
    }

    public String stampaSpese(StampaVo stampa)
    {
		JMSUtil manager;
		String idMessInoltro = "0";
		try {
	    	InitialContext iniCtx = new InitialContext();
			Queue queA = (Queue) iniCtx.lookup("queue/A");
			manager = new JMSUtil(iniCtx);
			Map param = new HashMap();
			param.put("STATO", "HELD");
			param.put("COD_ATTIVITA", "STAMPA_SPESE");
			param.put("BIBLIOTECA", stampa.getCodBib());
			param.put("BIBLIOTECARIO", stampa.getUser());
			param.put("DATA_ORA_RICHIESTA", DateUtil.getDate()+DateUtil.getTime());
			param.put("DATA_ORA_ESECUZIONE_PROGRAMMATA", "cronExpr");
			idMessInoltro = manager.sendQueue(queA, stampa,param);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		}
		return idMessInoltro;
    }

    public String stampaFattura(StampaVo stampa)
    {
		JMSUtil manager;
		String idMessInoltro = "0";
		try {
	    	InitialContext iniCtx = new InitialContext();
			Queue queA = (Queue) iniCtx.lookup("queue/A");
			manager = new JMSUtil(iniCtx);
			Map param = new HashMap();
			param.put("STATO", "HELD");
			param.put("COD_ATTIVITA", "STAMPA_FATTURA");
			param.put("BIBLIOTECA", stampa.getCodBib());
			param.put("BIBLIOTECARIO", stampa.getUser());
			param.put("DATA_ORA_RICHIESTA", DateUtil.getDate()+DateUtil.getTime());
			param.put("DATA_ORA_ESECUZIONE_PROGRAMMATA", "cronExpr");
			idMessInoltro = manager.sendQueue(queA, stampa,param);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		}
		return idMessInoltro;
    }

    public String stampaComunicazione(StampaVo stampa)
    {
		JMSUtil manager;
		String idMessInoltro = "0";
		try {
	    	InitialContext iniCtx = new InitialContext();
			Queue queA = (Queue) iniCtx.lookup("queue/A");
			manager = new JMSUtil(iniCtx);
			Map param = new HashMap();
			param.put("STATO", "HELD");
			param.put("COD_ATTIVITA", "STAMPA_COMUNICAZIONE");
			param.put("BIBLIOTECA", stampa.getCodBib());
			param.put("BIBLIOTECARIO", stampa.getUser());
			param.put("DATA_ORA_RICHIESTA", DateUtil.getDate()+DateUtil.getTime());
			param.put("DATA_ORA_ESECUZIONE_PROGRAMMATA", "cronExpr");
			idMessInoltro = manager.sendQueue(queA, stampa,param);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		}
		return idMessInoltro;
    }

    public String stampaSuggerimentiBibliotecario(StampaVo stampa)
    {
		JMSUtil manager;
		String idMessInoltro = "0";
		try {
	    	InitialContext iniCtx = new InitialContext();
			Queue queA = (Queue) iniCtx.lookup("queue/A");
			manager = new JMSUtil(iniCtx);
			Map param = new HashMap();
			param.put("STATO", "HELD");
			param.put("COD_ATTIVITA", "STAMPA_SUGGERIMENTI_BIBLIOTECARIO");
			param.put("BIBLIOTECA", stampa.getCodBib());
			param.put("BIBLIOTECARIO", stampa.getUser());
			param.put("DATA_ORA_RICHIESTA", DateUtil.getDate()+DateUtil.getTime());
			param.put("DATA_ORA_ESECUZIONE_PROGRAMMATA", "cronExpr");
			idMessInoltro = manager.sendQueue(queA, stampa,param);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		}
		return idMessInoltro;
    }

    public String stampaSuggerimentiLettore(StampaVo stampa)
    {
		JMSUtil manager;
		String idMessInoltro = "0";
		try {
	    	InitialContext iniCtx = new InitialContext();
			Queue queA = (Queue) iniCtx.lookup("queue/A");
			manager = new JMSUtil(iniCtx);
			Map param = new HashMap();
			param.put("STATO", "HELD");
			param.put("COD_ATTIVITA", "STAMPA_SUGGERIMENTI_LETTORE");
			param.put("BIBLIOTECA", stampa.getCodBib());
			param.put("BIBLIOTECARIO", stampa.getUser());
			param.put("DATA_ORA_RICHIESTA", DateUtil.getDate()+DateUtil.getTime());
			param.put("DATA_ORA_ESECUZIONE_PROGRAMMATA", "cronExpr");
			idMessInoltro = manager.sendQueue(queA, stampa,param);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		}
		return idMessInoltro;
    }

    public OutputStampaVo stampaOnlineUtente(StampaOnLineVO stampaOnLineVO){
		OutputStampaVo output = null;
		try {

			SBNStampeUtenti stampe = DomainEJBStampeFactory.getInstance().getSBNStampeUtenti();
			// rox 10.12.09 output= (OutputStampaVo)stampe.elabora(stampaOnLineVO, null);
			StampaUtentiDiffVO passa=new StampaUtentiDiffVO();
			passa.setStampavo(stampaOnLineVO);
			output= (OutputStampaVo)stampe.elabora(passa, null);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
    }

    public OutputStampaVo stampaOnlineRichiesta(StampaOnLineVO stampaOnLineVO){
		OutputStampaVo output = null;
		try {
			SBNStampeRichiesta stampe = DomainEJBStampeFactory.getInstance().getSBNStampeRichiesta();
			output= (OutputStampaVo)stampe.elabora(stampaOnLineVO, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
    }

    public OutputStampaVo stampaOnlineEtichette(StampaOnLineVO stampaOnLineVO){
    	//stampaOnLineVO deve contenere l'array dei dati delle etichette da stampare, (tipo EtichettaDettaglioVO)
		OutputStampaVo output = null;
		try {
			SBNStampeEtichette stampe = DomainEJBStampeFactory.getInstance().getSBNStampeEtichette();
			output= (OutputStampaVo)stampe.elabora(stampaOnLineVO, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
    }

    public OutputStampaVo stampaOnlineSchede(StampaOnLineVO stampaOnLineVO){
    	//stampaOnLineVO deve contenere l'array dei dati delle etichette da stampare, (tipo EtichettaDettaglioVO)
		OutputStampaVo output = null;
		try {
			SBNStampeSchede stampe = DomainEJBStampeFactory.getInstance().getSBNStampeSchede();
			output= (OutputStampaVo)stampe.elabora(stampaOnLineVO, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
    }

    public OutputStampaVo stampaOnlineBiblioteche(StampaOnLineVO stampaOnLineVO){
		OutputStampaVo output = null;
		try {
			SBNStampeBiblioteche stampe = DomainEJBStampeFactory.getInstance().getSBNStampeBiblioteche();
			output= (OutputStampaVo)stampe.elabora(stampaOnLineVO, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
    }

    public OutputStampaVo stampaOnlineTerminiThesauro(StampaOnLineVO stampaOnLineVO){
		OutputStampaVo output = null;
		try {
			SBNStampeTerminiThesauro stampe = DomainEJBStampeFactory.getInstance().getSBNStampeTerminiThesauro();
			output= (OutputStampaVo)stampe.elabora(stampaOnLineVO, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
    }

    public OutputStampaVo stampaOnlineFornitori(StampaOnLineVO stampaOnLineVO){
		OutputStampaVo output = null;
		try {
			SBNStampeFornitori stampe = DomainEJBStampeFactory.getInstance().getSBNStampeFornitori();
			StampaFornitoriDiffVO passa=new StampaFornitoriDiffVO();
			passa.setStampavo(stampaOnLineVO);
			output= (OutputStampaVo)stampe.elabora(passa, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
    }

    public OutputStampaVo stampaOnlineBuoniOrdine(StampaOnLineVO stampaOnLineVO){
		OutputStampaVo output = null;
		try {
			SBNStampeBuoniOrdine stampe = DomainEJBStampeFactory.getInstance().getSBNStampeBuoniOrdine();
			StampaOrdiniDiffVO passa=new StampaOrdiniDiffVO();
			passa.setStampavo(stampaOnLineVO);
			output= (OutputStampaVo)stampe.elabora(passa, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
    }

    public OutputStampaVo stampaOnlineBollettario(StampaOnLineVO stampaOnLineVO){
		OutputStampaVo output = null;
		try {
			SBNStampeBollettario stampe = DomainEJBStampeFactory.getInstance().getSBNStampeBollettario();
			StampaBollettarioDiffVO passa=new StampaBollettarioDiffVO();
			passa.setStampavo(stampaOnLineVO);
			output= (OutputStampaVo)stampe.elabora(passa, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
    }

    public OutputStampaVo stampaOnlineFattura(StampaOnLineVO stampaOnLineVO){
		OutputStampaVo output = null;
		try {
			SBNStampeFattura stampe = DomainEJBStampeFactory.getInstance().getSBNStampeFattura();
			output= (OutputStampaVo)stampe.elabora(stampaOnLineVO, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
    }

    public OutputStampaVo stampaOnlineComunicazione(StampaOnLineVO stampaOnLineVO){
		OutputStampaVo output = null;
		try {
			SBNStampeComunicazione stampe = DomainEJBStampeFactory.getInstance().getSBNStampeComunicazione();
			output= (OutputStampaVo)stampe.elabora(stampaOnLineVO, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
    }

    public OutputStampaVo stampaOnlineSuggerimentiBibliotecario(StampaOnLineVO stampaOnLineVO){
		OutputStampaVo output = null;
		try {
			SBNStampeSuggerimentiBibliotecario stampe = DomainEJBStampeFactory.getInstance().getSBNStampeSuggerimentiBibliotecario();
			output= (OutputStampaVo)stampe.elabora(stampaOnLineVO, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
    }

    public OutputStampaVo stampaOnlineSuggerimentiLettore(StampaOnLineVO stampaOnLineVO){
		OutputStampaVo output = null;
		try {
			SBNStampeSuggerimentiLettore stampe = DomainEJBStampeFactory.getInstance().getSBNStampeSuggerimentiLettore();
			output= (OutputStampaVo)stampe.elabora(stampaOnLineVO, null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return output;
    }


/*
    public Collection<StampaUtentiVO> stampaUtenti_(
            StampaUtentiVO stampaUtentiVO) throws EJBException {
        Collection<StampaUtentiVO> lstUtenti = new ArrayList<StampaUtentiVO>();
        try {
            StampeDAO dao = DAOFactory.getDAO(StampeDAO.class);
            lstUtenti = dao.stampaUtenti_(stampaUtentiVO);
        } catch (ResourceNotFoundException e) {
            throw new EJBException(e);
        } catch (ApplicationException e) {
            throw new EJBException(e);
        }
        return lstUtenti;
    }
*/
    public String stampaBiblioteche(StampaBibliotecheVO stampaBibliotecheVO)
            throws EJBException {
        String stampaBiblioteche = "";
        return stampaBiblioteche;
    }

    public Collection<StampaBibliotecheVO> stampaBiblioteche_(
            StampaBibliotecheVO stampaBibliotecheVO) throws EJBException {
        Collection<StampaBibliotecheVO> lstBiblioteche = new ArrayList<StampaBibliotecheVO>();
        return lstBiblioteche;
    }

    public String stampaBiblioteche(StampaVo stampa)
    {
//    	boolean statoInoltro = true;
		JMSUtil manager;
		String idMessInoltro = "0";
		try {
	    	InitialContext iniCtx = new InitialContext();
			Queue queA = (Queue) iniCtx.lookup("queue/A");
			manager = new JMSUtil(iniCtx);
			Map param = new HashMap();
			param.put("STATO", "HELD");
			param.put("COD_ATTIVITA", "STAMPA_BIBLIOTECHE");
			param.put("BIBLIOTECA", stampa.getCodBib());
			param.put("BIBLIOTECARIO", stampa.getUser());
			param.put("DATA_ORA_RICHIESTA", DateUtil.getDate()+DateUtil.getTime());
			param.put("DATA_ORA_ESECUZIONE_PROGRAMMATA", "cronExpr");
//			boolean statoInoltro = manager.sendQueue(queA, stampa,param);
			idMessInoltro = manager.sendQueue(queA, stampa,param);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		}
		return idMessInoltro;
//		return statoInoltro;
    }

    public String stampaSchede(StampaVo stampa)
            throws EJBException {
		JMSUtil manager;
		String idMessInoltro = "0";
		try {
	    	InitialContext iniCtx = new InitialContext();
			Queue queA = (Queue) iniCtx.lookup("queue/A");
			manager = new JMSUtil(iniCtx);
			Map param = new HashMap();
			param.put("STATO", "HELD");
			param.put("COD_ATTIVITA", "STAMPA_SCHEDE");
			param.put("BIBLIOTECA", stampa.getCodBib());
			param.put("BIBLIOTECARIO", stampa.getUser());
			param.put("DATA_ORA_RICHIESTA", DateUtil.getDate()+DateUtil.getTime());
			param.put("DATA_ORA_ESECUZIONE_PROGRAMMATA", "cronExpr");
			idMessInoltro = manager.sendQueue(queA, stampa,param);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		}
		return idMessInoltro;
    }

    public StampeOnlineVO stampeOnline(String ticket, StampaType tipoStampa,
			List<?> parametri) throws ApplicationException, DataException,
			ValidationException {

		try {
			logger.info("tipo stampa: " + tipoStampa.getArea() + "::" + tipoStampa);
			InitialContext ctx = new InitialContext();
			DomainEJBFactory factory = DomainEJBFactory.getInstance();
			switch (tipoStampa.getArea()) {

			case GESTIONE_DOCUMENTO_FISICO:
				DocumentoFisicoCommon docFisico = factory.getDocumentoFisicoCommon();
				return docFisico.stampeOnline(ticket, tipoStampa, parametri);

			case GESTIONE_SERVIZI:
				ServiziErogazioneServizi servizi = factory.getSrvErogazione();
				return servizi.stampeOnline(ticket, tipoStampa, parametri);

			case GESTIONE_ACQUISIZIONI:
				ServiziAcquisizioni acquisizioni = factory.getSrvAcquisizioni();
				return acquisizioni.stampeOnline(ticket, tipoStampa, parametri);

			case GESTIONE_SISTEMA:
				AmministrazioneBiblioteca biblio = factory.getBiblioteca();
				return biblio.stampeOnline(ticket, tipoStampa, parametri);

			case GESTIONE_SEMANTICA:
				Semantica sem = factory.getSemanticaBMT();
				return sem.stampeOnline(ticket, tipoStampa, parametri);

			case GESTIONE_BIBLIOGRAFICA:
				ServiziBibliografici gb = factory.getSrvBibliografica();
				return gb.stampeOnline(ticket, tipoStampa, parametri);

			default:
				throw new ValidationException("Errore stampa");
			}
		} catch (ValidationException ve) {
			throw ve;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
	}

    public String stampaServiziCorrenti(StampaVo stampa)    {
		JMSUtil manager;
		String idMessInoltro = "0";
//		boolean statoInoltro = true;
		try {
			return getAmministrazionePolo().prenotaElaborazioneDifferita(stampa, null);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idMessInoltro;
    }

    public String stampaServiziStorico(StampaVo stampa)    {
		JMSUtil manager;
		String idMessInoltro = "0";
//		boolean statoInoltro = true;
		try {
			return getAmministrazionePolo().prenotaElaborazioneDifferita(stampa, null);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idMessInoltro;
    }

    public String stampaTitoliEditore(StampaVo stampa){
		String idMessInoltro = "0";
		try {
			return getAmministrazionePolo().prenotaElaborazioneDifferita(stampa, null);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return idMessInoltro;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			return idMessInoltro;
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idMessInoltro;
    }

	public OutputStampaVo stampaModuloPrelievo(StampaOnLineVO stampaVO) {
		try {
			return DomainEJBStampeFactory.getInstance().getSBNStampeStrumentiPatrimonio().stampaModuloPrelievo(stampaVO);
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
}
