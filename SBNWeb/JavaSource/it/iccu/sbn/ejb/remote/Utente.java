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

import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.exception.DefaultNotFoundException;
import it.iccu.sbn.exception.TicketExpiredException;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.exception.UtenteNotFoundException;
import it.iccu.sbn.exception.UtenteNotProfiledException;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.vo.UserVO;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface Utente {

    public UserVO getUtente(String username, String pwd, InetAddress addr) throws RemoteException, UtenteNotFoundException, UtenteNotProfiledException, InfrastructureException;
    public boolean changePassword(String username,String password) throws RemoteException, UtenteNotFoundException, UtenteNotProfiledException, InfrastructureException;
    public Object getDefault(ConstantDefault _default) throws RemoteException, DefaultNotFoundException, InfrastructureException;

    public String getLogin() throws RemoteException;
    public Map<?, ?> getListaAttivita() throws RemoteException;

    public void checkAttivita(String cod_attivita) throws RemoteException, UtenteNotAuthorizedException;
    public void checkAttivita(String cod_bib, String cod_attivita) throws RemoteException, UtenteNotAuthorizedException;
    public void checkAttivitaBib(String cod_attivita) throws RemoteException, UtenteNotAuthorizedException;

    public void checkAttivitaAut(String cod_attivita,String tipoAuthority) throws RemoteException, UtenteNotAuthorizedException;
    public void checkLivAutAuthority(String tipoAuthority, int livelloAuthority) throws RemoteException, UtenteNotAuthorizedException;
    public void isAuthorityForzatura(String tipoAuthority) throws RemoteException ,UtenteNotAuthorizedException;
    public String getLivAutAuthority(String tipoAuthority) throws RemoteException, UtenteNotAuthorizedException;
    public void isAbilitatoLegameTitoloAuthority(String tipoAuthority) throws RemoteException, UtenteNotAuthorizedException;
	public void isAbilitatoAuthority(String tipoAuthority) throws RemoteException, UtenteNotAuthorizedException;

    public void checkAttivitaMat(String cod_attivita, String tipoMat) throws RemoteException, UtenteNotAuthorizedException;
    public void checkLivAutDocumenti(String tipoMateriale, int livelloAuthority) throws RemoteException, UtenteNotAuthorizedException;
    public String getLivAutDocumenti(String tipoMateriale) throws RemoteException, UtenteNotAuthorizedException;
    public void isAbilitatoTipoMateriale(String tipoMateriale) throws RemoteException, UtenteNotAuthorizedException;
    public void isDocumentoForzatura(String tipoMateriale) throws RemoteException, UtenteNotAuthorizedException;

	public void checkAttivita(String cod_attivita, String codPolo, String codBibOperante, List<Object> parametri) throws RemoteException, UtenteNotAuthorizedException;
	public void reloadDefault() throws RemoteException;

	public void refresh() throws TicketExpiredException;

	public void isAuthorityForzaturaIndice(String tipoAuthority) throws RemoteException, UtenteNotAuthorizedException;
	public void isAbilitatoAuthorityIndice(SbnAuthority auth) throws RemoteException, UtenteNotAuthorizedException;
	public void checkAttivitaAutIndice(String codAttivita, SbnAuthority auth) throws RemoteException, UtenteNotAuthorizedException;

	//almaviva5_20140128 evolutive google3
	public boolean isAuthoritySoloLocale(SbnAuthority auth) throws UtenteNotAuthorizedException;
	public boolean isTipoMaterialeSoloLocale(String tipoMateriale) throws UtenteNotAuthorizedException;

}

