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
package it.iccu.sbn.ejb.remote;

import it.iccu.sbn.ejb.SbnBusinessSessionRemote;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaRicercaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.PoloVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.UtenteVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceConfigVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiciPermessiType;
import it.iccu.sbn.ejb.vo.amministrazionesistema.custom.BibliotecaSearch;
import it.iccu.sbn.ejb.vo.common.ComboVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.servizi.pagination.QueryPage;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.EJBObject;

public interface AmministrazioneSistema extends EJBObject, SbnBusinessSessionRemote {

	public List<BibliotecaVO> getBiblioteche(BibliotecaSearch search,
			int offset) throws RemoteException;

	public BibliotecaVO getBiblioteca(String Cod_polo, String Cod_bib)
			throws RemoteException;

	public List<ComboCodDescVO> getBibliotecheCentroSistema(String ticket)
			throws RemoteException;

    public DescrittoreBloccoVO getListaBibliotecheAffiliatePerAttivita(
			String ticket, String codPolo, String codBib, String codAttivita,
			int elemBlocco) throws RemoteException;

    public List<ComboVO> getListaComboBibliotecheAffiliatePerAttivita(String ticket, String codPolo, String codBib, String codAttivita)
    	throws RemoteException;

	public DescrittoreBloccoVO getListaBibliotechePolo(String ticket,
			String codPolo, int elementiPerBlocco) throws RemoteException;

	public void updateBiblioteca(BibliotecaVO bibliotecaVO)
	throws RemoteException;

	public DescrittoreBloccoVO cercaUtenti(String ticket, String cognome, String nome, String username, String biblioteca, String dataAccesso, String abilitato, String ordinamento, int numElemBlocco)
	throws EJBException, DaoManagerException, RemoteException;

    public boolean isCentroSistema(String polo, String bib)
    throws EJBException, DaoManagerException, RemoteException;

	public DescrittoreBloccoVO cercaBiblioteche(String ticket, BibliotecaRicercaVO richiesta)
	throws EJBException, DaoManagerException, RemoteException;

	public List<ComboVO> getElencoBiblioteche()
	throws EJBException, DaoManagerException, RemoteException;

	public UtenteVO creaBibliotecario(UtenteVO bibliotecario, int utenteInseritore, boolean forzaInserimento, boolean abilitazione)
	throws EJBException, DaoManagerException, RemoteException;

    public UtenteVO caricaBibliotecario(int idUtente)
    throws EJBException, DaoManagerException, RemoteException;

    public boolean controllaAbilitazioneBibliotecario(int idUtente)
    throws EJBException, DaoManagerException, RemoteException;

    public int getDurataPassword()
    throws EJBException, DaoManagerException, RemoteException;

    public List<BibliotecaVO> getBibliotecheCentroSistema()
    throws EJBException, DaoManagerException, RemoteException;

    public String getCodicePoloCorrente()
    throws EJBException, DaoManagerException, RemoteException;

	public BibliotecaVO creaBiblioteca(BibliotecaVO biblioteca, String utenteInseritore, boolean forzaInserimento, boolean abilitazione, String codPoloCorrente)
	throws DaoManagerException, ApplicationException, RemoteException;

	public BibliotecaVO caricaBiblioteca(int idBiblioteca)
	throws EJBException, DaoManagerException, RemoteException;

	public boolean controllaAbilitazioneBiblioteca(int idBib)
	throws EJBException, DaoManagerException, RemoteException;

	public void removeUserTicket(String ticket) throws EJBException, DaoManagerException, RemoteException;

    public DescrittoreBloccoVO cercaConfigTabelleCodici(String ticket, String user, int numElemBlocco)
    throws EJBException, DaoManagerException, RemoteException;

    public DescrittoreBloccoVO cercaTabellaCodici(String ticket, int numElemBlocco, String cdTabella)
    throws EJBException, DaoManagerException, RemoteException;

    public boolean salvaTabellaCodici(CodiceVO codice, boolean validate) throws ValidationException, RemoteException;

	public PoloVO getPolo() throws RemoteException;

	public boolean abilitaTabella(String ticket, CodiceConfigVO config,	CodiciPermessiType permessi) throws RemoteException;

	public List<ComboVO> getListaComboBibliotecheSistemaMetropolitano(String ticket, String codPolo, String codBib) throws RemoteException;

	public DescrittoreBloccoVO getListaBibliotecheSistemaMetropolitano(
			String ticket, String codPolo, String codBib, int elemBlocco)
			throws RemoteException;

	public QueryPage testPaginazione() throws RemoteException;

}
