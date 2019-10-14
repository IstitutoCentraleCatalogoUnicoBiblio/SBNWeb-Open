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
package it.iccu.sbn.ejb;

import it.iccu.sbn.ejb.domain.acquisizioni.Acquisizioni;
import it.iccu.sbn.ejb.domain.acquisizioni.AcquisizioniBMT;
import it.iccu.sbn.ejb.domain.acquisizioni.AcquisizioniBMTHome;
import it.iccu.sbn.ejb.domain.acquisizioni.AcquisizioniHome;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBiblioteca;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBibliotecaHome;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBibliotecario;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBibliotecarioHome;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneGestioneCodici;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneGestioneCodiciBMTHome;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneGestioneCodiciHome;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneGestioneDefault;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneGestioneDefaultHome;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneMail;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneMailHome;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePoloHome;
import it.iccu.sbn.ejb.domain.bibliografica.Interrogazione;
import it.iccu.sbn.ejb.domain.bibliografica.InterrogazioneHome;
import it.iccu.sbn.ejb.domain.bibliografica.Profiler;
import it.iccu.sbn.ejb.domain.bibliografica.ProfilerHome;
import it.iccu.sbn.ejb.domain.bibliografica.Repertorio;
import it.iccu.sbn.ejb.domain.bibliografica.RepertorioHome;
import it.iccu.sbn.ejb.domain.documentofisico.Collocazione;
import it.iccu.sbn.ejb.domain.documentofisico.CollocazioneBMT;
import it.iccu.sbn.ejb.domain.documentofisico.CollocazioneBMTHome;
import it.iccu.sbn.ejb.domain.documentofisico.CollocazioneHome;
import it.iccu.sbn.ejb.domain.documentofisico.DocumentoFisicoBMT;
import it.iccu.sbn.ejb.domain.documentofisico.DocumentoFisicoBMTHome;
import it.iccu.sbn.ejb.domain.documentofisico.DocumentoFisicoCommon;
import it.iccu.sbn.ejb.domain.documentofisico.DocumentoFisicoCommonHome;
import it.iccu.sbn.ejb.domain.documentofisico.Inventario;
import it.iccu.sbn.ejb.domain.documentofisico.InventarioBMT;
import it.iccu.sbn.ejb.domain.documentofisico.InventarioBMTHome;
import it.iccu.sbn.ejb.domain.documentofisico.InventarioHome;
import it.iccu.sbn.ejb.domain.documentofisico.Possessori;
import it.iccu.sbn.ejb.domain.documentofisico.PossessoriHome;
import it.iccu.sbn.ejb.domain.periodici.PeriodiciBMTHome;
import it.iccu.sbn.ejb.domain.periodici.PeriodiciSBN;
import it.iccu.sbn.ejb.domain.periodici.PeriodiciSBNHome;
import it.iccu.sbn.ejb.domain.semantica.Semantica;
import it.iccu.sbn.ejb.domain.semantica.SemanticaHome;
import it.iccu.sbn.ejb.domain.semantica.classi.Classi;
import it.iccu.sbn.ejb.domain.semantica.classi.ClassiHome;
import it.iccu.sbn.ejb.domain.semantica.soggetti.Soggetti;
import it.iccu.sbn.ejb.domain.semantica.soggetti.SoggettiHome;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.domain.servizi.ServiziCommon;
import it.iccu.sbn.ejb.domain.servizi.ServiziCommonHome;
import it.iccu.sbn.ejb.domain.servizi.ServiziHome;
import it.iccu.sbn.ejb.domain.servizi.batch.ServiziBMT;
import it.iccu.sbn.ejb.domain.servizi.batch.ServiziBMTHome;
import it.iccu.sbn.ejb.domain.servizi.calendario.Calendario;
import it.iccu.sbn.ejb.domain.servizi.calendario.CalendarioHome;
import it.iccu.sbn.ejb.domain.servizi.sale.Sale;
import it.iccu.sbn.ejb.domain.servizi.sale.SaleHome;
import it.iccu.sbn.ejb.domain.servizi.serviziweb.ServiziWeb;
import it.iccu.sbn.ejb.domain.servizi.serviziweb.ServiziWebHome;
import it.iccu.sbn.ejb.domain.servizi.utenti.UtenteLettore;
import it.iccu.sbn.ejb.domain.servizi.utenti.UtenteLettoreHome;
import it.iccu.sbn.ejb.domain.statistiche.SBNStatistiche;
import it.iccu.sbn.ejb.domain.statistiche.SBNStatisticheHome;
import it.iccu.sbn.ejb.domain.statistiche.StatisticheSBN;
import it.iccu.sbn.ejb.domain.statistiche.StatisticheSBNHome;
import it.iccu.sbn.ejb.services.Codici;
import it.iccu.sbn.ejb.services.CodiciHome;
import it.iccu.sbn.ejb.services.acquisizioni.ServiziAcquisizioni;
import it.iccu.sbn.ejb.services.acquisizioni.ServiziAcquisizioniHome;
import it.iccu.sbn.ejb.services.bibliografica.ServiziBibliografici;
import it.iccu.sbn.ejb.services.bibliografica.ServiziBibliograficiHome;
import it.iccu.sbn.ejb.services.semantica.SemanticaBMTHome;
import it.iccu.sbn.ejb.services.servizi.ServiziErogazioneServizi;
import it.iccu.sbn.ejb.services.servizi.ServiziErogazioneServiziHome;
import it.iccu.sbn.util.jndi.JNDIUtil;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

public class DomainEJBFactory {

	public static abstract class Reference<T> {
		protected T ref;
		protected abstract T init() throws Exception;
		public T get() { try {
			return ref != null ? ref : (ref = init());
		} catch (Exception e) { return null; } }
		public void invalidate() { this.ref = null; }
	}

	private static DomainEJBFactory instance;

	private final Context ctx;

	private AmministrazioneBibliotecario bibliotecario;
	private AmministrazioneBiblioteca biblioteca;
	private AmministrazionePolo polo;

	private AmministrazioneGestioneCodici amministrazioneGestioneCodici;
	private Acquisizioni acquisizioni;
	private AmministrazioneMail amministrazioneMail;
	private Inventario inventario;
	private Interrogazione interrogazione;
	private Profiler profiler;

	private AmministrazioneGestioneDefault AmministrazioneGestioneDefault;
	private Collocazione collocazione;
	private DocumentoFisicoCommon documentoFisicoCommon;
	private Possessori possessori;
	private Repertorio repertorio;
	private Semantica semantica;
	private Servizi servizi;
	private ServiziWeb serviziWeb;
	private UtenteLettore utenteLettore;

	private ServiziErogazioneServizi srvErogazione;
	private ServiziAcquisizioni srvAcquisizioni;
	private Semantica semanticaBMT;
	private ServiziBibliografici srvBibliografica;

	private StatisticheSBN statistiche;
	private ServiziBMT serviziBMT;
	private ServiziCommon serviziCommon;
	private PeriodiciSBN periodici;

	private Soggetti soggetti;
	private Classi classi;

	private Codici codici;
	private AmministrazioneGestioneCodici codiciBMT;
	private SBNStatistiche sbnStatistiche;
	private DocumentoFisicoBMT documentoFisicoBMT;
	private AcquisizioniBMT acquisizioniBMT;
	private PeriodiciSBN periodiciBMT;
	private InventarioBMT InventarioBMT;
	private CollocazioneBMT CollocazioneBMT;

	private Sale sale;
	private Calendario calendario;

	static {
		try {
			instance = new DomainEJBFactory();
		} catch (NamingException e) {}
	}

	private DomainEJBFactory() throws NamingException {
		ctx = JNDIUtil.getContext();
	}

	public static final DomainEJBFactory getInstance()
			throws NamingException, RemoteException, CreateException {
		return instance;
	}

	public synchronized AmministrazioneBibliotecario getBibliotecario()
			throws NamingException, RemoteException, CreateException {
		if (bibliotecario != null)
			return bibliotecario;

		AmministrazioneBibliotecarioHome amministrazioneBibliotecarioHome = (AmministrazioneBibliotecarioHome) ctx
				.lookup(AmministrazioneBibliotecarioHome.JNDI_NAME);
		this.bibliotecario = amministrazioneBibliotecarioHome.create();
		return bibliotecario;
	}

	public synchronized AmministrazioneBiblioteca getBiblioteca() throws NamingException,
			RemoteException, CreateException {
		if (biblioteca != null)
			return biblioteca;

		AmministrazioneBibliotecaHome amministrazioneBibliotecaHome = (AmministrazioneBibliotecaHome) ctx
				.lookup(AmministrazioneBibliotecaHome.JNDI_NAME);
		this.biblioteca = amministrazioneBibliotecaHome.create();
		return biblioteca;
	}

	public synchronized AmministrazionePolo getPolo() throws NamingException,
			RemoteException, CreateException {
		if (polo != null)
			return polo;

		AmministrazionePoloHome amministrazionePoloHome = (AmministrazionePoloHome) ctx
				.lookup(AmministrazionePoloHome.JNDI_NAME);
		polo = amministrazionePoloHome.create();
		return polo;
	}

	public synchronized AmministrazioneGestioneCodici getAmministrazioneGestioneCodici()
			throws NamingException, RemoteException, CreateException {
		if (amministrazioneGestioneCodici != null)
			return amministrazioneGestioneCodici;

		AmministrazioneGestioneCodiciHome amministrazioneGestioneCodiciHome = (AmministrazioneGestioneCodiciHome) ctx
				.lookup(AmministrazioneGestioneCodiciHome.JNDI_NAME);
		this.amministrazioneGestioneCodici = amministrazioneGestioneCodiciHome
				.create();
		return amministrazioneGestioneCodici;
	}

	public synchronized Acquisizioni getAcquisizioni() throws NamingException,
			RemoteException, CreateException {
		if (acquisizioni != null)
			return acquisizioni;

		AcquisizioniHome acquisizioniHome = (AcquisizioniHome) ctx
				.lookup(AcquisizioniHome.JNDI_NAME);
		this.acquisizioni = acquisizioniHome.create();
		return acquisizioni;
	}

	public synchronized AmministrazioneMail getAmministrazioneMail() throws NamingException,
			RemoteException, CreateException {
		if (amministrazioneMail != null)
			return amministrazioneMail;

		AmministrazioneMailHome mailhome = (AmministrazioneMailHome) ctx
				.lookup(AmministrazioneMailHome.JNDI_NAME);
		this.amministrazioneMail = mailhome.create();
		return amministrazioneMail;
	}

	public synchronized Inventario getInventario() throws NamingException, RemoteException,
			CreateException {
		if (inventario != null)
			return inventario;

		InventarioHome inventarioHome = (InventarioHome) ctx
				.lookup(InventarioHome.JNDI_NAME);
		this.inventario = inventarioHome.create();
		return inventario;
	}

	public synchronized Interrogazione getInterrogazione() throws NamingException,
			RemoteException, CreateException {
		if (interrogazione != null)
			return interrogazione;

		InterrogazioneHome interrogazionehome = (InterrogazioneHome) ctx
				.lookup(InterrogazioneHome.JNDI_NAME);
		this.interrogazione = interrogazionehome.create();
		return interrogazione;
	}

	public synchronized Profiler getProfiler() throws NamingException, RemoteException,
			CreateException {
		if (profiler != null)
			return profiler;

		ProfilerHome profilerhome = (ProfilerHome) ctx
				.lookup(ProfilerHome.JNDI_NAME);
		this.profiler = profilerhome.create();
		return profiler;
	}

	public synchronized AmministrazioneGestioneDefault getAmministrazioneGestioneDefault() throws NamingException,
			RemoteException, CreateException {
		if (AmministrazioneGestioneDefault != null)
			return AmministrazioneGestioneDefault;

		AmministrazioneGestioneDefaultHome home = (AmministrazioneGestioneDefaultHome) ctx
				.lookup(AmministrazioneGestioneDefaultHome.JNDI_NAME);
		this.AmministrazioneGestioneDefault = home.create();
		return AmministrazioneGestioneDefault;
	}

	public synchronized Collocazione getCollocazione() throws NamingException,
			RemoteException, CreateException {
		if (collocazione != null)
			return collocazione;

		CollocazioneHome collocazioneHome = (CollocazioneHome) ctx
				.lookup(CollocazioneHome.JNDI_NAME);
		this.collocazione = collocazioneHome.create();
		return collocazione;
	}

	public synchronized DocumentoFisicoCommon getDocumentoFisicoCommon()
			throws NamingException, RemoteException, CreateException {
		if (documentoFisicoCommon != null)
			return documentoFisicoCommon;

		DocumentoFisicoCommonHome documentoFisicoCommonHome = (DocumentoFisicoCommonHome) ctx
				.lookup(DocumentoFisicoCommonHome.JNDI_NAME);
		this.documentoFisicoCommon = documentoFisicoCommonHome.create();

		return documentoFisicoCommon;
	}

	public synchronized Possessori getPossessori() throws NamingException, RemoteException,
			CreateException {
		if (possessori != null)
			return possessori;

		PossessoriHome possessoriHome = (PossessoriHome) ctx
				.lookup(PossessoriHome.JNDI_NAME);
		this.possessori = possessoriHome.create();

		return possessori;
	}

	public synchronized Repertorio getRepertorio() throws NamingException, RemoteException,
			CreateException {
		if (repertorio != null)
			return repertorio;

		RepertorioHome dhome = (RepertorioHome) ctx
				.lookup(RepertorioHome.JNDI_NAME);
		this.repertorio = dhome.create();

		return repertorio;
	}

	public synchronized Semantica getSemantica() throws NamingException, RemoteException,
			CreateException {
		if (semantica != null)
			return semantica;

		SemanticaHome semanticahome = (SemanticaHome) ctx
				.lookup(SemanticaHome.JNDI_NAME);
		this.semantica = semanticahome.create();

		return semantica;
	}

	public synchronized Servizi getServizi() throws NamingException, RemoteException,
			CreateException {
		if (servizi != null)
			return servizi;

		ServiziHome serviziHome = (ServiziHome) ctx
				.lookup(ServiziHome.JNDI_NAME);
		this.servizi = serviziHome.create();

		return servizi;
	}

	public synchronized ServiziWeb getServiziWeb() throws NamingException, RemoteException,
			CreateException {
		if (serviziWeb != null)
			return serviziWeb;

		ServiziWebHome serviziWHome = (ServiziWebHome) ctx
				.lookup(ServiziWebHome.JNDI_NAME);
		this.serviziWeb = serviziWHome.create();

		return serviziWeb;
	}

	public synchronized UtenteLettore getUtenteLettore() throws NamingException, RemoteException, CreateException {
		if (utenteLettore != null)
			return utenteLettore;

		UtenteLettoreHome utenteHome = (UtenteLettoreHome) ctx.lookup(UtenteLettoreHome.JNDI_NAME);
		this.utenteLettore = utenteHome.create();
		return utenteLettore;
	}

	public synchronized ServiziErogazioneServizi getSrvErogazione() throws NamingException,
			RemoteException, CreateException {
		if (srvErogazione != null)
			return srvErogazione;

		ServiziErogazioneServiziHome homeSRV = (ServiziErogazioneServiziHome) ctx
				.lookup(ServiziErogazioneServiziHome.JNDI_NAME);
		this.srvErogazione = homeSRV.create();

		return srvErogazione;
	}

	public synchronized ServiziAcquisizioni getSrvAcquisizioni() throws NamingException,
			RemoteException, CreateException {
		if (srvAcquisizioni != null)
			return srvAcquisizioni;

		ServiziAcquisizioniHome homeACQ = (ServiziAcquisizioniHome) ctx
				.lookup(ServiziAcquisizioniHome.JNDI_NAME);
		this.srvAcquisizioni = homeACQ.create();

		return srvAcquisizioni;
	}

	public synchronized Semantica getSemanticaBMT() throws NamingException, RemoteException,
			CreateException {
		if (semanticaBMT != null)
			return semanticaBMT;

		SemanticaBMTHome homeSEM = (SemanticaBMTHome) ctx
				.lookup(SemanticaBMTHome.JNDI_NAME);
		this.semanticaBMT = homeSEM.create();

		return semanticaBMT;
	}

	public synchronized ServiziBibliografici getSrvBibliografica() throws NamingException,
			RemoteException, CreateException {
		if (srvBibliografica != null)
			return srvBibliografica;

		ServiziBibliograficiHome homeGB = (ServiziBibliograficiHome) ctx
				.lookup(ServiziBibliograficiHome.JNDI_NAME);
		this.srvBibliografica = homeGB.create();
		return srvBibliografica;
	}

	public synchronized StatisticheSBN getStatistiche() throws NamingException,
			RemoteException, CreateException {
		if (statistiche != null)
			return statistiche;

		StatisticheSBNHome statisticheSBNHome = (StatisticheSBNHome) ctx
				.lookup(StatisticheSBNHome.JNDI_NAME);
		this.statistiche = statisticheSBNHome.create();
		return statistiche;
	}

	public synchronized ServiziBMT getServiziBMT() throws NamingException, RemoteException,
			CreateException {
		if (serviziBMT != null)
			return serviziBMT;

		ServiziBMTHome serviziBMTHome = (ServiziBMTHome) ctx
				.lookup(ServiziBMTHome.JNDI_NAME);
		this.serviziBMT = serviziBMTHome.create();

		return serviziBMT;
	}

	public synchronized ServiziCommon getServiziCommon() throws NamingException,
			RemoteException, CreateException {
		if (serviziCommon != null)
			return serviziCommon;

		ServiziCommonHome serviziCommonHome = (ServiziCommonHome) PortableRemoteObject
				.narrow(ctx.lookup(ServiziCommonHome.JNDI_NAME),
						ServiziCommonHome.class);
		this.serviziCommon = serviziCommonHome.create();

		return serviziCommon;
	}

	public synchronized PeriodiciSBN getPeriodici() throws NamingException, RemoteException,
			CreateException {
		if (periodici != null)
			return periodici;

		PeriodiciSBNHome periodiciSBNHome = (PeriodiciSBNHome) ctx
				.lookup(PeriodiciSBNHome.JNDI_NAME);
		this.periodici = periodiciSBNHome.create();
		return periodici;
	}

	public synchronized Soggetti getSoggetti() throws NamingException, RemoteException, CreateException {
		if (soggetti != null)
			return soggetti;

		SoggettiHome home = (SoggettiHome) JNDIUtil.getContext().lookup(SoggettiHome.JNDI_NAME);
		Soggetti soggetti = home.create();
		return soggetti;
	}

	public synchronized Classi getClassi() throws NamingException, RemoteException, CreateException {
		if (classi != null)
			return classi;

		ClassiHome home = (ClassiHome) JNDIUtil.getContext().lookup(ClassiHome.JNDI_NAME);
		Classi classi = home.create();
		return classi;
	}

	public synchronized Codici getCodici() throws NamingException, RemoteException,
			CreateException {
		if (codici != null)
			return codici;

		CodiciHome codiciHome = (CodiciHome) PortableRemoteObject.narrow(
				ctx.lookup(CodiciHome.JNDI_NAME), CodiciHome.class);
		this.codici = codiciHome.create();

		return codici;
	}

	public synchronized AmministrazioneGestioneCodici getCodiciBMT() throws NamingException,
			RemoteException, CreateException {
		if (codiciBMT != null)
			return codiciBMT;

		AmministrazioneGestioneCodiciBMTHome gestioneCodiciBMTHome = (AmministrazioneGestioneCodiciBMTHome) ctx
				.lookup(AmministrazioneGestioneCodiciBMTHome.JNDI_NAME);
		this.codiciBMT = gestioneCodiciBMTHome.create();

		return codiciBMT;
	}

	public synchronized SBNStatistiche getSbnStatistiche() throws NamingException,
			RemoteException, CreateException {
		if (sbnStatistiche != null)
			return sbnStatistiche;

		SBNStatisticheHome sbnStatisticheHome = (SBNStatisticheHome) ctx
				.lookup(SBNStatisticheHome.JNDI_NAME);
		this.sbnStatistiche = sbnStatisticheHome.create();

		return sbnStatistiche;
	}

	public synchronized DocumentoFisicoBMT getDocumentoFisicoBMT() throws NamingException,
			RemoteException, CreateException {

		if (documentoFisicoBMT != null)
			return documentoFisicoBMT;

		DocumentoFisicoBMTHome fisicoBMTHome = (DocumentoFisicoBMTHome) ctx
				.lookup(DocumentoFisicoBMTHome.JNDI_NAME);
		this.documentoFisicoBMT = fisicoBMTHome.create();

		return documentoFisicoBMT;
	}

	public synchronized AcquisizioniBMT getAcquisizioniBMT() throws NamingException,
			RemoteException, CreateException {
		if (acquisizioniBMT != null)
			return acquisizioniBMT;

		AcquisizioniBMTHome acquisizioniBMTHome = (AcquisizioniBMTHome) ctx
				.lookup(AcquisizioniBMTHome.JNDI_NAME);
		this.acquisizioniBMT = acquisizioniBMTHome.create();

		return acquisizioniBMT;
	}

	public synchronized PeriodiciSBN getPeriodiciBMT() throws NamingException,
			RemoteException, CreateException {
		if (periodiciBMT != null)
			return periodiciBMT;

		PeriodiciBMTHome periodiciBMTHome = (PeriodiciBMTHome) ctx
				.lookup(PeriodiciBMTHome.JNDI_NAME);
		this.periodiciBMT = periodiciBMTHome.create();
		return periodiciBMT;
	}

	public synchronized InventarioBMT getInventarioBMT() throws NamingException, RemoteException, CreateException {
		if (InventarioBMT != null)
			return InventarioBMT;

		InventarioBMTHome inventarioHome = (InventarioBMTHome) ctx.lookup(InventarioBMTHome.JNDI_NAME);
		InventarioBMT= inventarioHome.create();
		return InventarioBMT;
	}

	public synchronized CollocazioneBMT getCollocazioneBMT() throws NamingException, RemoteException, CreateException {
		if (CollocazioneBMT != null)
			return CollocazioneBMT;

		CollocazioneBMTHome collocazioneHome = (CollocazioneBMTHome) ctx.lookup(CollocazioneBMTHome.JNDI_NAME);
		CollocazioneBMT = collocazioneHome.create();
		return CollocazioneBMT;
	}

	public synchronized Sale getSale() throws NamingException, RemoteException, CreateException {
		if (sale != null)
			return sale;

		SaleHome home = (SaleHome) JNDIUtil.getContext().lookup(SaleHome.JNDI_NAME);
		sale = home.create();
		return sale;
	}

	public synchronized Calendario getCalendario() throws NamingException, RemoteException, CreateException {
		if (calendario != null)
			return calendario;

		CalendarioHome home = (CalendarioHome) JNDIUtil.getContext().lookup(CalendarioHome.JNDI_NAME);
		calendario = home.create();
		return calendario;
	}

}
