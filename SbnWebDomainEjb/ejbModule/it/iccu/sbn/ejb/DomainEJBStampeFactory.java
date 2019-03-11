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

import it.iccu.sbn.ejb.domain.elaborazioniDifferite.importa.StampaReportImport;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.importa.StampaReportImportHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampaStatistiche;
import it.iccu.sbn.ejb.domain.stampe.SBNStampaStatisticheHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeBiblioteche;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeBibliotecheHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeBollettario;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeBollettarioHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeBollettino;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeBollettinoHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeBuoniCarico;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeBuoniCaricoHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeBuoniOrdine;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeBuoniOrdineHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeComunicazione;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeComunicazioneHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeEtichette;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeEtichetteHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeFattura;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeFatturaHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeFornitori;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeFornitoriHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeListaFascicoli;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeListaFascicoliHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeRegistriIngresso;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeRegistriIngressoHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeRegistroConservazione;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeRegistroConservazioneHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeRegistroTopografico;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeRegistroTopograficoHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeRichiesta;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeRichiestaHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeSchede;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeSchedeHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeSemantica;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeSemanticaHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeServiziCorrenti;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeServiziCorrentiHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeSpese;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeSpeseHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeStrumentiPatrimonio;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeStrumentiPatrimonioHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeSuggerimentiBibliotecario;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeSuggerimentiBibliotecarioHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeSuggerimentiLettore;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeSuggerimentiLettoreHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeTerminiThesauro;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeTerminiThesauroHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeTitoliEditore;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeTitoliEditoreHome;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeUtenti;
import it.iccu.sbn.ejb.domain.stampe.SBNStampeUtentiHome;
import it.iccu.sbn.util.jndi.JNDIUtil;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

public class DomainEJBStampeFactory {

	private static DomainEJBStampeFactory instance;

	private final Context ctx;

	private SBNStampeUtenti SBNStampeUtenti;
	private SBNStampeRichiesta SBNStampeRichiesta;
	private SBNStampeEtichette SBNStampeEtichette;
	private SBNStampeSchede SBNStampeSchede;
	private SBNStampeBiblioteche SBNStampeBiblioteche;
	private SBNStampeTerminiThesauro SBNStampeTerminiThesauro;
	private SBNStampeFornitori SBNStampeFornitori;
	private SBNStampeBuoniOrdine SBNStampeBuoniOrdine;
	private SBNStampeBollettario SBNStampeBollettario;
	private SBNStampeFattura SBNStampeFattura;
	private SBNStampeComunicazione SBNStampeComunicazione;
	private SBNStampeSuggerimentiBibliotecario SBNStampeSuggerimentiBibliotecario;
	private SBNStampeSuggerimentiLettore SBNStampeSuggerimentiLettore;
	private SBNStampeListaFascicoli SBNStampeListaFascicoli;
	private SBNStampeSemantica SBNStampeSemantica;
	private SBNStampeSpese SBNStampeSpese;
	private SBNStampeBollettino SBNStampeBollettino;
	private SBNStampeBuoniCarico SBNStampeBuoniCarico;
	private SBNStampaStatistiche SBNStampaStatistiche;
	private SBNStampeStrumentiPatrimonio SBNStampeStrumentiPatrimonio;
	private SBNStampeRegistroConservazione SBNStampeRegistroConservazione;
	private SBNStampeRegistriIngresso SBNStampeRegistriIngresso;
	private SBNStampeRegistroTopografico SBNStampeRegistroTopografico;
	private SBNStampeServiziCorrenti SBNStampeServiziCorrenti;
	private SBNStampeTitoliEditore SBNStampeTitoliEditore;
	private StampaReportImport StampaReportImport;

	static {
		try {
			instance = new DomainEJBStampeFactory();
		} catch (NamingException e) {}
	}

	private DomainEJBStampeFactory() throws NamingException {
		ctx = JNDIUtil.getContext();
	}

	public static final DomainEJBStampeFactory getInstance()
			throws NamingException, RemoteException, CreateException {
		return instance;
	}

	public synchronized SBNStampeUtenti getSBNStampeUtenti() throws RemoteException, CreateException, NamingException {
		if (SBNStampeUtenti != null)
			return SBNStampeUtenti;

		SBNStampeUtentiHome home = (SBNStampeUtentiHome) ctx
				.lookup(SBNStampeUtentiHome.JNDI_NAME);
		SBNStampeUtenti = home.create();

		return SBNStampeUtenti;
	}

	public synchronized SBNStampeRichiesta getSBNStampeRichiesta() throws NamingException, RemoteException, CreateException {
		if (SBNStampeRichiesta != null)
			return SBNStampeRichiesta;

		SBNStampeRichiestaHome home = (SBNStampeRichiestaHome) ctx.lookup(SBNStampeRichiestaHome.JNDI_NAME);
		SBNStampeRichiesta = home.create();
		return SBNStampeRichiesta;
	}

	public synchronized SBNStampeEtichette getSBNStampeEtichette() throws NamingException, RemoteException, CreateException {
		if (SBNStampeEtichette != null)
			return SBNStampeEtichette;

		SBNStampeEtichetteHome home = (SBNStampeEtichetteHome) ctx.lookup(SBNStampeEtichetteHome.JNDI_NAME);
		SBNStampeEtichette = home.create();
		return SBNStampeEtichette;
	}

	public synchronized SBNStampeSchede getSBNStampeSchede() throws NamingException, RemoteException, CreateException {
		if (SBNStampeSchede != null)
			return SBNStampeSchede;

		SBNStampeSchedeHome home = (SBNStampeSchedeHome) ctx.lookup(SBNStampeSchedeHome.JNDI_NAME);
		SBNStampeSchede = home.create();
		return SBNStampeSchede;
	}

	public synchronized SBNStampeBiblioteche getSBNStampeBiblioteche() throws NamingException, RemoteException, CreateException {
		if (SBNStampeBiblioteche != null)
			return SBNStampeBiblioteche;

		SBNStampeBibliotecheHome home = (SBNStampeBibliotecheHome) ctx.lookup(SBNStampeBibliotecheHome.JNDI_NAME);
		SBNStampeBiblioteche = home.create();
		return SBNStampeBiblioteche;
	}

	public synchronized SBNStampeTerminiThesauro getSBNStampeTerminiThesauro() throws NamingException, RemoteException, CreateException {
		if (SBNStampeTerminiThesauro != null)
			return SBNStampeTerminiThesauro;

		SBNStampeTerminiThesauroHome home = (SBNStampeTerminiThesauroHome) ctx.lookup(SBNStampeTerminiThesauroHome.JNDI_NAME);
		SBNStampeTerminiThesauro = home.create();
		return SBNStampeTerminiThesauro;
	}

	public synchronized SBNStampeFornitori getSBNStampeFornitori() throws NamingException, RemoteException, CreateException {
		if (SBNStampeFornitori != null)
			return SBNStampeFornitori;

		SBNStampeFornitoriHome home = (SBNStampeFornitoriHome) ctx.lookup(SBNStampeFornitoriHome.JNDI_NAME);
		SBNStampeFornitori = home.create();
		return SBNStampeFornitori;
	}

	public synchronized SBNStampeBuoniOrdine getSBNStampeBuoniOrdine() throws NamingException, RemoteException, CreateException {
		if (SBNStampeBuoniOrdine != null)
			return SBNStampeBuoniOrdine;

		SBNStampeBuoniOrdineHome home = (SBNStampeBuoniOrdineHome) ctx.lookup(SBNStampeBuoniOrdineHome.JNDI_NAME);
		SBNStampeBuoniOrdine = home.create();
		return SBNStampeBuoniOrdine;
	}

	public synchronized SBNStampeBollettario getSBNStampeBollettario() throws NamingException, RemoteException, CreateException {
		if (SBNStampeBollettario != null)
			return SBNStampeBollettario;

		SBNStampeBollettarioHome home = (SBNStampeBollettarioHome) ctx.lookup(SBNStampeBollettarioHome.JNDI_NAME);
		SBNStampeBollettario = home.create();
		return SBNStampeBollettario;
	}

	public synchronized SBNStampeFattura getSBNStampeFattura() throws NamingException, RemoteException, CreateException {
		if (SBNStampeFattura != null)
			return SBNStampeFattura;

		SBNStampeFatturaHome home = (SBNStampeFatturaHome) ctx.lookup(SBNStampeFatturaHome.JNDI_NAME);
		SBNStampeFattura = home.create();
		return SBNStampeFattura;
	}

	public synchronized SBNStampeComunicazione getSBNStampeComunicazione() throws RemoteException, CreateException, NamingException {
		if (SBNStampeComunicazione != null)
			return SBNStampeComunicazione;

    	SBNStampeComunicazioneHome home = (SBNStampeComunicazioneHome) ctx.lookup(SBNStampeComunicazioneHome.JNDI_NAME);
		SBNStampeComunicazione = home.create();
		return SBNStampeComunicazione;
	}

	public synchronized SBNStampeSuggerimentiBibliotecario getSBNStampeSuggerimentiBibliotecario() throws NamingException, RemoteException, CreateException {
		if (SBNStampeSuggerimentiBibliotecario != null)
			return SBNStampeSuggerimentiBibliotecario;

		SBNStampeSuggerimentiBibliotecarioHome home = (SBNStampeSuggerimentiBibliotecarioHome) ctx.lookup(SBNStampeSuggerimentiBibliotecarioHome.JNDI_NAME);
		SBNStampeSuggerimentiBibliotecario = home.create();
		return SBNStampeSuggerimentiBibliotecario;
	}

	public synchronized SBNStampeSuggerimentiLettore getSBNStampeSuggerimentiLettore() throws NamingException, RemoteException, CreateException {
		if (SBNStampeSuggerimentiLettore != null)
			return SBNStampeSuggerimentiLettore;

    	SBNStampeSuggerimentiLettoreHome home = (SBNStampeSuggerimentiLettoreHome) ctx.lookup(SBNStampeSuggerimentiLettoreHome.JNDI_NAME);
		SBNStampeSuggerimentiLettore = home.create();
		return SBNStampeSuggerimentiLettore;
	}

	public synchronized SBNStampeListaFascicoli getSBNStampeListaFascicoli() throws NamingException, RemoteException, CreateException {
		if (SBNStampeListaFascicoli != null)
			return SBNStampeListaFascicoli;

		SBNStampeListaFascicoliHome home = (SBNStampeListaFascicoliHome) ctx.lookup(SBNStampeListaFascicoliHome.JNDI_NAME);
		SBNStampeListaFascicoli = home.create();
		return SBNStampeListaFascicoli;
	}

	public synchronized SBNStampeSemantica getSBNStampeSemantica() throws NamingException, RemoteException, CreateException {
		if (SBNStampeSemantica != null)
			return SBNStampeSemantica;

		SBNStampeSemanticaHome home = (SBNStampeSemanticaHome) JNDIUtil.lookup(SBNStampeSemanticaHome.JNDI_NAME, SBNStampeSemanticaHome.class);
		SBNStampeSemantica = home.create();
		return SBNStampeSemantica;
	}

	public synchronized SBNStampeSpese getSBNStampeSpese() throws NamingException, RemoteException, CreateException {
		if (SBNStampeSpese != null)
			return SBNStampeSpese;

		SBNStampeSpeseHome home = (SBNStampeSpeseHome) ctx.lookup(SBNStampeSpeseHome.JNDI_NAME);
		SBNStampeSpese = home.create();
		return SBNStampeSpese;
	}

	public synchronized SBNStampeBollettino getSBNStampeBollettino() throws NamingException, RemoteException, CreateException {
		if (SBNStampeBollettino != null)
			return SBNStampeBollettino;

		SBNStampeBollettinoHome home = (SBNStampeBollettinoHome) ctx.lookup(SBNStampeBollettinoHome.JNDI_NAME);
		SBNStampeBollettino = home.create();
		return SBNStampeBollettino;
	}

	public synchronized SBNStampeBuoniCarico getSBNStampeBuoniCarico() throws NamingException, RemoteException, CreateException {
		if (SBNStampeBuoniCarico != null)
			return SBNStampeBuoniCarico;

		SBNStampeBuoniCaricoHome home = (SBNStampeBuoniCaricoHome) ctx.lookup(SBNStampeBuoniCaricoHome.JNDI_NAME);
		SBNStampeBuoniCarico = home.create();
		return SBNStampeBuoniCarico;
	}

	public synchronized SBNStampaStatistiche getSBNStampaStatistiche() throws NamingException, RemoteException, CreateException {
		if (SBNStampaStatistiche != null)
			return SBNStampaStatistiche;

		SBNStampaStatisticheHome home = (SBNStampaStatisticheHome) ctx.lookup(SBNStampaStatisticheHome.JNDI_NAME);
		SBNStampaStatistiche = home.create();
		return SBNStampaStatistiche;
	}

	public synchronized SBNStampeStrumentiPatrimonio getSBNStampeStrumentiPatrimonio() throws NamingException, RemoteException, CreateException {
		if (SBNStampeStrumentiPatrimonio != null)
			return SBNStampeStrumentiPatrimonio;

		SBNStampeStrumentiPatrimonioHome home = (SBNStampeStrumentiPatrimonioHome) ctx.lookup(SBNStampeStrumentiPatrimonioHome.JNDI_NAME);
		SBNStampeStrumentiPatrimonio = home.create();
		return SBNStampeStrumentiPatrimonio;
	}

	public synchronized SBNStampeRegistroConservazione getSBNStampeRegistroConservazione() throws NamingException, RemoteException, CreateException {
		if (SBNStampeRegistroConservazione != null)
			return SBNStampeRegistroConservazione;

		SBNStampeRegistroConservazioneHome home = (SBNStampeRegistroConservazioneHome) ctx
				.lookup(SBNStampeRegistroConservazioneHome.JNDI_NAME);
		SBNStampeRegistroConservazione = home.create();
		return SBNStampeRegistroConservazione;
	}

	public synchronized SBNStampeRegistriIngresso getSBNStampeRegistriIngresso() throws NamingException, RemoteException, CreateException {
		if (SBNStampeRegistriIngresso != null)
			return SBNStampeRegistriIngresso;

		SBNStampeRegistriIngressoHome home = (SBNStampeRegistriIngressoHome) ctx
				.lookup(SBNStampeRegistriIngressoHome.JNDI_NAME);
		SBNStampeRegistriIngresso = home.create();
		return SBNStampeRegistriIngresso;
	}

	public synchronized SBNStampeRegistroTopografico getSBNStampeRegistroTopografico() throws NamingException, RemoteException, CreateException {
		if (SBNStampeRegistroTopografico != null)
			return SBNStampeRegistroTopografico;

		SBNStampeRegistroTopograficoHome home = (SBNStampeRegistroTopograficoHome) ctx
				.lookup(SBNStampeRegistroTopograficoHome.JNDI_NAME);
		SBNStampeRegistroTopografico = home.create();
		return SBNStampeRegistroTopografico;
	}

	public synchronized SBNStampeServiziCorrenti getSBNStampeServiziCorrenti() throws NamingException, RemoteException, CreateException {
		if (SBNStampeServiziCorrenti != null)
			return SBNStampeServiziCorrenti;

		SBNStampeServiziCorrentiHome home = (SBNStampeServiziCorrentiHome) ctx
				.lookup(SBNStampeServiziCorrentiHome.JNDI_NAME);
		SBNStampeServiziCorrenti = home.create();
		return SBNStampeServiziCorrenti;
	}

	public synchronized SBNStampeTitoliEditore getSBNStampeTitoliEditore() throws NamingException, RemoteException, CreateException {
		if (SBNStampeTitoliEditore != null)
			return SBNStampeTitoliEditore;

		SBNStampeTitoliEditoreHome home = (SBNStampeTitoliEditoreHome) ctx
				.lookup(SBNStampeTitoliEditoreHome.JNDI_NAME);
		SBNStampeTitoliEditore = home.create();
		return SBNStampeTitoliEditore;
	}

	public synchronized StampaReportImport getStampaReportImport() throws NamingException, RemoteException, CreateException {
		if (StampaReportImport != null)
			return StampaReportImport;

		StampaReportImportHome home = (StampaReportImportHome) ctx.lookup(StampaReportImportHome.JNDI_NAME);
		StampaReportImport = home.create();
		return StampaReportImport;
	}

}
