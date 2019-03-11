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
package it.iccu.sbn.web.integration.bd;

import it.iccu.sbn.ejb.home.AmministrazioneSistemaHome;
import it.iccu.sbn.ejb.home.ElaborazioniDifferiteHome;
import it.iccu.sbn.ejb.home.GestioneAcquisizioniHome;
import it.iccu.sbn.ejb.home.GestioneBibliograficaHome;
import it.iccu.sbn.ejb.home.GestioneCodiciHome;
import it.iccu.sbn.ejb.home.GestioneDefaultHome;
import it.iccu.sbn.ejb.home.GestioneDocumentoFisicoHome;
import it.iccu.sbn.ejb.home.GestioneRepertorioHome;
import it.iccu.sbn.ejb.home.GestioneSemanticaHome;
import it.iccu.sbn.ejb.home.GestioneServiziHome;
import it.iccu.sbn.ejb.home.GestioneServiziWebHome;
import it.iccu.sbn.ejb.home.MenuHome;
import it.iccu.sbn.ejb.home.StampeOnlineHome;
import it.iccu.sbn.ejb.remote.AmministrazioneSistema;
import it.iccu.sbn.ejb.remote.ElaborazioniDifferite;
import it.iccu.sbn.ejb.remote.GestioneAcquisizioni;
import it.iccu.sbn.ejb.remote.GestioneBibliografica;
import it.iccu.sbn.ejb.remote.GestioneCodici;
import it.iccu.sbn.ejb.remote.GestioneDefault;
import it.iccu.sbn.ejb.remote.GestioneDocumentoFisico;
import it.iccu.sbn.ejb.remote.GestioneRepertorio;
import it.iccu.sbn.ejb.remote.GestioneSemantica;
import it.iccu.sbn.ejb.remote.GestioneServizi;
import it.iccu.sbn.ejb.remote.GestioneServiziWeb;
import it.iccu.sbn.ejb.remote.Menu;
import it.iccu.sbn.ejb.remote.StampeOnline;
import it.iccu.sbn.util.jndi.JNDIUtil;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

public class ServiziFactoryEJBDelegate {

	private static ServiziFactoryEJBDelegate instance;

	private GestioneCodici codici;
	private AmministrazioneSistema amministrazioneSistema;
	private GestioneDocumentoFisico gestioneDocumentoFisico;
	private GestioneServizi gestioneServizi;
	private GestioneServiziWeb gestioneServiziWeb;
	private GestioneAcquisizioni gestioneAcquisizioni;
	private GestioneSemantica gestioneSemantica;
	private GestioneBibliografica gestioneBibliografica;
	private StampeOnline stampeOnline;
	private GestioneDefault gestioneDefault;
	private GestioneRepertorio gestioneRepertorio;
	private ElaborazioniDifferite elaborazioniDifferite;
	private Menu gestioneMenu;

	private ServiziFactoryEJBDelegate() throws NamingException, RemoteException,
			CreateException {

		Context ic = JNDIUtil.getContext();

		GestioneCodiciHome codiciHome = (GestioneCodiciHome) PortableRemoteObject
				.narrow(ic.lookup(GestioneCodiciHome.JNDI_NAME),
						GestioneCodiciHome.class);
		this.codici = codiciHome.create();

		AmministrazioneSistemaHome home = (AmministrazioneSistemaHome) PortableRemoteObject
				.narrow(ic.lookup(AmministrazioneSistemaHome.JNDI_NAME),
						AmministrazioneSistemaHome.class);
		this.amministrazioneSistema = home.create();

		GestioneDocumentoFisicoHome gestioneDocumentoFisicoHome = (GestioneDocumentoFisicoHome) PortableRemoteObject
				.narrow(ic.lookup(GestioneDocumentoFisicoHome.JNDI_NAME),
						GestioneDocumentoFisicoHome.class);
		this.gestioneDocumentoFisico = gestioneDocumentoFisicoHome.create();

		GestioneServiziHome gestioneServiziHome = (GestioneServiziHome) PortableRemoteObject
				.narrow(ic.lookup(GestioneServiziHome.JNDI_NAME),
						GestioneServiziHome.class);
		this.gestioneServizi = gestioneServiziHome.create();

		GestioneServiziWebHome gestioneServiziWebHome = (GestioneServiziWebHome) PortableRemoteObject
		.narrow(ic.lookup(GestioneServiziWebHome.JNDI_NAME),
				GestioneServiziWebHome.class);
		this.gestioneServiziWeb = gestioneServiziWebHome.create();

		GestioneAcquisizioniHome gestioneAcquisizioniHome = (GestioneAcquisizioniHome) PortableRemoteObject
				.narrow(ic.lookup(GestioneAcquisizioniHome.JNDI_NAME),
						GestioneAcquisizioniHome.class);
		this.gestioneAcquisizioni = gestioneAcquisizioniHome.create();

		GestioneSemanticaHome gestioneSemanticaHome = (GestioneSemanticaHome) PortableRemoteObject
				.narrow(ic.lookup(GestioneSemanticaHome.JNDI_NAME),
						GestioneSemanticaHome.class);
		this.gestioneSemantica = gestioneSemanticaHome.create();

		GestioneBibliograficaHome gestioneBibliograficaHome = (GestioneBibliograficaHome) PortableRemoteObject
				.narrow(ic.lookup(GestioneBibliograficaHome.JNDI_NAME),
						GestioneBibliograficaHome.class);
		this.gestioneBibliografica = gestioneBibliograficaHome.create();

		StampeOnlineHome stampeOnlineHome = (StampeOnlineHome) PortableRemoteObject
				.narrow(ic.lookup(StampeOnlineHome.JNDI_NAME),
						StampeOnlineHome.class);
		this.stampeOnline = stampeOnlineHome.create();

		GestioneDefaultHome gestioneDefaultHome = (GestioneDefaultHome) PortableRemoteObject
				.narrow(ic.lookup(GestioneDefaultHome.JNDI_NAME),
						GestioneDefaultHome.class);
		this.gestioneDefault = gestioneDefaultHome.create();

		GestioneRepertorioHome gestioneRepertorioHome = (GestioneRepertorioHome) PortableRemoteObject
				.narrow(ic.lookup(GestioneRepertorioHome.JNDI_NAME),
						GestioneRepertorioHome.class);
		this.gestioneRepertorio = gestioneRepertorioHome.create();
		this.gestioneRepertorio.getAllRepertori();

		ElaborazioniDifferiteHome elaborazioniDifferiteHome = (ElaborazioniDifferiteHome) PortableRemoteObject
				.narrow(ic.lookup(ElaborazioniDifferiteHome.JNDI_NAME),
						ElaborazioniDifferiteHome.class);
		this.elaborazioniDifferite = elaborazioniDifferiteHome.create();

		MenuHome gestioneMenuHome = (MenuHome) PortableRemoteObject.narrow(ic
				.lookup(MenuHome.JNDI_NAME), MenuHome.class);
		this.gestioneMenu = gestioneMenuHome.create();

	}

	synchronized public static ServiziFactoryEJBDelegate getInstance()
			throws NamingException, RemoteException, CreateException {
		if (instance == null)
			instance = new ServiziFactoryEJBDelegate();

		return instance;
	}

	public GestioneCodici getCodici() throws RemoteException, CreateException {
		return this.codici;
	}

	public AmministrazioneSistema getSistema() throws RemoteException,
			CreateException {
		return this.amministrazioneSistema;
	}

	public GestioneDocumentoFisico getGestioneDocumentoFisico() {
		return this.gestioneDocumentoFisico;
	}

	public GestioneServizi getGestioneServizi() {
		return this.gestioneServizi;
	}

	public GestioneAcquisizioni getGestioneAcquisizioni() {
		return this.gestioneAcquisizioni;
	}

	public GestioneSemantica getGestioneSemantica() {
		return this.gestioneSemantica;
	}

	public GestioneBibliografica getGestioneBibliografica() {
		return this.gestioneBibliografica;
	}

	public StampeOnline getStampeOnline() {
		return this.stampeOnline;
	}

	public GestioneRepertorio getGestioneRepertorio() {
		return this.gestioneRepertorio;
	}

	public ElaborazioniDifferite getElaborazioniDifferite() {
		return this.elaborazioniDifferite;
	}

	public Menu getMenu() {
		return this.gestioneMenu;
	}

	public GestioneDefault getGestioneDefault() {
		return this.gestioneDefault;
	}

	public GestioneServiziWeb getGestioneServiziWeb() {
		return gestioneServiziWeb;
	}

}
