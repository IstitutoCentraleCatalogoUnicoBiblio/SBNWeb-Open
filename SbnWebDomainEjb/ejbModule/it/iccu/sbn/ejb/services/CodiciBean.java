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
package it.iccu.sbn.ejb.services;

import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;

/**
 *
 * <!-- begin-user-doc --> A generated session bean <!-- end-user-doc -->
 *
 * <!-- begin-xdoclet-definition -->
 *
 * @ejb.bean name="Codici" description="A session bean named Servizi"
 *           display-name="Codici" jndi-name="sbnWeb/Codici" type="Stateless"
 *           transaction-type="Container" view-type = "remote"
 * @ejb.util generate="no"
 *
 *
 * <!-- end-xdoclet-definition -->
 * @generated
 */

@SuppressWarnings("unchecked")
public abstract class CodiciBean implements javax.ejb.SessionBean {

	private static final long serialVersionUID = 111728274015753625L;

	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		return;
	}

	public void ejbCreate() {
		return;
	}

	private List getCodiceLegameNaturaTitoloTitolo() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO);
		return lst;
	}

	public synchronized boolean initialize(boolean force) throws EJBException {
		CodiciProvider.invalidate();
		return true;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 *
	 */
	public TB_CODICI cercaCodice(String codiceRicerca, CodiciType tipoCodice,
			CodiciRicercaType tipoRicerca) throws EJBException {
		try {
			return CodiciProvider.cercaCodice(codiceRicerca, tipoCodice, tipoRicerca);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 *
	 */
	public String cercaDescrizioneCodice(String codiceRicerca,
			CodiciType tipoCodice, CodiciRicercaType tipoRicerca) throws EJBException {
		try {
			return CodiciProvider.cercaDescrizioneCodice(codiceRicerca,	tipoCodice, tipoRicerca);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 *
	 */
	public List getCodici(CodiciType TB_TABELLA) throws EJBException {

		return getCodici(TB_TABELLA, false);
	}

	public List getCodici(CodiciType TB_TABELLA, boolean soloAttivi) throws EJBException {
		try {
			return Collections.unmodifiableList(CodiciProvider.getCodici(TB_TABELLA, soloAttivi));
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public List<TB_CODICI> getCodiciCross(CodiciType tpTabellaP, String codiceP, boolean soloAttivi) throws EJBException {
		try {
			return Collections.unmodifiableList(CodiciProvider.getCodiciCross(tpTabellaP, codiceP, soloAttivi));
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 *
	 */
	public String getDescrizioneCodice(CodiciType tipoCodice,
			String codiceUnimarc) throws EJBException {
		TB_CODICI elem = this.cercaCodice(codiceUnimarc, tipoCodice,
				CodiciRicercaType.RICERCA_CODICE_UNIMARC);
		if (elem != null)
			return (elem.getDs_tabella().trim());
		else
			return null;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 *
	 */
	public String getDescrizioneCodiceSBN(CodiciType tipoCodice,
			String codiceSBN) throws EJBException {
		TB_CODICI elem = this.cercaCodice(codiceSBN, tipoCodice,
				CodiciRicercaType.RICERCA_CODICE_SBN);
		if (elem != null)
			return (elem.getDs_tabella().trim());
		else
			return null;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 *
	 */
	public String getDescrizioneCodiceTabella(CodiciType tipoCodice,
			String codiceUnimarc) throws EJBException {
		CodiciType tipoCod = tipoCodice;
		if (tipoCod != null) {
			// return getDescrizioneCodice(tipoCod, codiceUnimarc);
			TB_CODICI elem = cercaCodice(codiceUnimarc, tipoCod,
					CodiciRicercaType.RICERCA_CODICE_UNIMARC);
			if (elem != null) {
				String elementoCompleto = (elem.getCd_tabella());
				elementoCompleto = elementoCompleto.substring(1, 3);
				return elementoCompleto;
			}

		}
		return null;
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 *
	 */
	public List getLICR(String suffisso) throws EJBException {
		// Lista completa dei codici LICR
		List legami = this.getCodiceLegameNaturaTitoloTitolo();
		// Vettore ritornato contenente i soli codici che rispettano sia il
		// prefisso che il suffisso
		List out = new ArrayList();
		for (int i = 0; i < legami.size(); i++) {
			TB_CODICI elem = (TB_CODICI) legami.get(i);
			// Il test si effettua sul codice SBN(Cd_tabella)
			String codice = elem.getCd_tabella();
			// Se il codice rispetta il prefisso ed il suffisso allora viene
			// inserito nel vettore
			if (codice.endsWith(suffisso)) {
				out.add(elem);
			}
		}
		return Collections.unmodifiableList(out);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 *
	 */
	public List getLICR(String prefisso, String suffisso) throws EJBException {
		// Lista completa dei codici LICR
		List legami = this.getCodiceLegameNaturaTitoloTitolo();
		// Vettore ritornato contenente i soli codici che rispettano sia il
		// prefisso che il suffisso
		List out = new ArrayList();
		for (int i = 0; i < legami.size(); i++) {
			TB_CODICI elem = (TB_CODICI) legami.get(i);
			// Il test si effettua sul codice SBN(Cd_tabella)
			String codice = elem.getCd_tabella().trim();
			// Se il codice rispetta il prefisso ed il suffisso allora viene
			// inserito nel vettore
			if (codice.startsWith(prefisso) && codice.endsWith(suffisso)) {
				out.add(elem);
			}
		}
		return Collections.unmodifiableList(out);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 *
	 */
	public String SBNToUnimarc(CodiciType tipoCodice, String codiceSBN)
			throws EJBException {
		// La stringa vuota non corrisponde ad alcun codice e viene ritornata
		// non convertita
		if (codiceSBN == null)
			return null;
		if (("".equals(codiceSBN)) || (!tipoCodiceEffettivo(tipoCodice)))
			return codiceSBN;

		// Ricerca del codice SBN
		TB_CODICI codice = cercaCodice(codiceSBN, tipoCodice,
				CodiciRicercaType.RICERCA_CODICE_SBN);
		try {
			// Viene ritornato il codice UNIMARC corrispondente
			return (codice.getCd_unimarc().trim());
		} catch (NullPointerException e) {
			return codiceSBN;
		}
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 *
	 */
	public boolean tipoCodiceEffettivo(CodiciType tipoCodice)
			throws EJBException {
		return (CodiciType.CODICE_MATERIALE_BIBLIOGRAFICO2 != tipoCodice)
				&& (CodiciType.CODICE_SI_NO != tipoCodice)
				&& (CodiciType.CODICE_REPERTORIO_MARCHE != tipoCodice)
				&& (CodiciType.CODICE_MATERIALE_BIBLIOGRAFICO_TUTTI != tipoCodice);
	}

	/**
	 *
	 * <!-- begin-xdoclet-definition -->
	 *
	 * @throws EJBException {
	 * @ejb.interface-method view-type="remote" <!-- end-xdoclet-definition -->
	 * @generated
	 *
	 * //TODO: Must provide implementation for bean create stub
	 *
	 */
	public String unimarcToSBN(CodiciType tipoCodice, String codiceUnimarc)
			throws EJBException {
		// La stringa vuota non corrisponde ad alcun codice e viene ritornata
		// non convertita
		if (codiceUnimarc == null)
			return null;
		if (("".equals(codiceUnimarc)) || (!tipoCodiceEffettivo(tipoCodice)))
			return codiceUnimarc;
		// Ricerca del codice UNIMARC
		TB_CODICI codice = cercaCodice(codiceUnimarc, tipoCodice,
				CodiciRicercaType.RICERCA_CODICE_UNIMARC);
		try {
			// Viene ritornato il codice SBN corrispondente
			return (codice.getCd_tabella().trim());
		} catch (NullPointerException e) {
			return codiceUnimarc;
		}
	}

	public List<ModelloStampaVO> getModelliStampaPerAttivita(String codAttivita) throws EJBException {
		try {
			return CodiciProvider.getModelliStampaPerAttivita(codAttivita);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public ModelloStampaVO getModelloStampa(int idModello) throws EJBException {
		try {
			return CodiciProvider.getModelloStampa(idModello);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public ModelloStampaVO getModelloStampa(String jrxml) throws EJBException {
		try {
			return CodiciProvider.getModelloStampa(jrxml);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}


}
