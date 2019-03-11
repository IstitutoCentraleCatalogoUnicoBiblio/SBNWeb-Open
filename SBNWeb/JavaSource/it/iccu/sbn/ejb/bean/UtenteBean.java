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
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBibliotecario;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.remote.Utente;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.UserVOImpl;
import it.iccu.sbn.exception.AttivitaNotFoundException;
import it.iccu.sbn.exception.DefaultNotFoundException;
import it.iccu.sbn.exception.TicketExpiredException;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.exception.UtenteNotFoundException;
import it.iccu.sbn.exception.UtenteNotProfiledException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.util.profiler.SbnWebProfileCache;
import it.iccu.sbn.vo.custom.amministrazione.AttivitaAffiliata;
import it.iccu.sbn.vo.custom.amministrazione.Default;
import it.iccu.sbn.vo.custom.amministrazione.ParametriAuthorityVO;
import it.iccu.sbn.vo.custom.amministrazione.ParametriDocumentiVO;
import it.iccu.sbn.vo.custom.amministrazione.UserProfile;
import it.iccu.sbn.web.constant.ConstantDefault;
import it.iccu.sbn.web.vo.UserVO;

import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

public class UtenteBean extends AbstractStatelessSessionBean implements Utente, InvocationHandler {

	private static final long serialVersionUID = 8332450277949041864L;

	private static Logger log = Logger.getLogger(Utente.class);
	private AmministrazioneBibliotecario amministrazione;

	private SoftReference<UserProfile> utenteRef;
	private String userName;
	private String ticket;

	@Override
	public void ejbPassivate() throws EJBException, RemoteException {
		//almaviva5_20111003 non serializzabile
		utenteRef = null;
	}

	public AmministrazioneBibliotecario getAmministrazione() {
		if (amministrazione != null)
			return amministrazione;

		try {
			amministrazione = DomainEJBFactory.getInstance().getBibliotecario();

		} catch (Exception e) {
			log.error("", e);
		}
		return amministrazione;
	}

	public boolean changePassword(String username, String password)
			throws EJBException, UtenteNotFoundException, UtenteNotProfiledException,
			InfrastructureException {
		try {
			//almaviva5_20111116 pulizia profilo utente
			SbnWebProfileCache.getInstance().clear(ValidazioneDati.asSingletonList(username));
			this.utenteRef = null;
			//

			boolean changePassword = getAmministrazione().changePassword(username, password);
			return changePassword;

		} catch (UtenteNotFoundException e) {
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
			throw new InfrastructureException("error");
		} catch (DaoManagerException e) {

			log.error("", e);
			throw new InfrastructureException("error");
		}
	}

	@Override
	public void ejbCreate() {
		logger.info("creato ejb");
	}

	public Object getDefault(ConstantDefault _default) throws EJBException,
			DefaultNotFoundException, InfrastructureException {
		Default def = getUtente().getDefault().get(_default.toString());
		Object value = null;
		if (def == null)
			// Recupero il default hard-coded
			value = _default.getDefault();
		else
			value = def.getValue();

		if (value == null)
			throw new DefaultNotFoundException();

		return value;
	}

	public String getLogin() {
		return userName;
	}

	public UserVO getUtente(String username, String pwd, InetAddress addr) throws EJBException,
			UtenteNotFoundException, UtenteNotProfiledException, InfrastructureException {
		try {
			UserProfile utente = getAmministrazione().login(username, pwd, addr);
			ticket = utente.getTicket();

			UserVO user = new UserVOImpl();
			user.setBiblioteca(utente.getDescrizioneBiblioteca());
			user.setCodBib(utente.getBiblioteca());
			user.setNome_cognome(utente.getNome_cognome());
			user.setPassword(utente.getPassword());
			user.setCodPolo(utente.getPolo());
			user.setUserId(utente.getUserName());


			user.setTicket(ticket);
			user.setNewPassword(utente.isNewPassword());
			user.setIdUtenteProfessionale(utente.getIdUtenteProfessionale());
			//almaviva5_20100107

			user.setRemoto(utente.isRemoto() );
			userName = username;

			utenteRef = new SoftReference<UserProfile>(utente);

			return user;

		} catch (UtenteNotFoundException e) {
			throw e;
		} catch (UtenteNotProfiledException e) {
			throw e;
		} catch (RemoteException e) {
			log.error("", e);
			throw new InfrastructureException("error");
		} catch (DaoManagerException e) {
			log.error("", e);
			throw new InfrastructureException("error");
		}
	}

	/**
	 * it.iccu.sbn.ejb.bean UtenteBean.java Verifica se il bibliotecario è
	 * abilitato all'attivita richiesta checkAttivita void
	 *
	 * @param cod_attivita
	 * @throws AttivitaNotFoundException
	 *
	 *
	 */
	public void checkAttivita(String cod_attivita)
			throws UtenteNotAuthorizedException {
		if (getUtente().getAttivita().containsKey(cod_attivita))
			return;

		throw new UtenteNotAuthorizedException();
	}

	public void checkAttivita(String cdBibAff, String cod_attivita)
			throws RemoteException, UtenteNotAuthorizedException {

		//check per utente
		checkAttivita(cod_attivita);

		UserProfile utente = getUtente();
		if (ValidazioneDati.equals(cdBibAff, utente.getBiblioteca()))
			return;	//stessa biblioteca

		AttivitaAffiliata aa = new AttivitaAffiliata(cod_attivita, utente.getBiblioteca(), cdBibAff);
		List<AttivitaAffiliata> list = utente.getAttivitaAffiliate().get(cod_attivita);
		if (!ValidazioneDati.isFilled(list) || !list.contains(aa))
			throw new UtenteNotAuthorizedException();
	}

	public void checkAttivitaBib(String cod_attivita)
			throws UtenteNotAuthorizedException {
		if (getUtente().getAttivitaBib().contains(cod_attivita))
			return;

		throw new UtenteNotAuthorizedException();
	}

	public void checkAttivitaAut(String cod_attivita, String tipoAuthority)
			throws UtenteNotAuthorizedException {

		if (getUtente().getAttivita().containsKey(cod_attivita)
				&& getUtente().getParametriAuthority().containsKey(tipoAuthority)) {
			//isAbilitatoAuthority(tipoAuthority);
			return;
		}

		throw new UtenteNotAuthorizedException();
	}

	public void checkAttivitaMat(String cod_attivita, String tipoMat)
			throws UtenteNotAuthorizedException {
		if (getUtente().getAttivita().containsKey(cod_attivita)
				&& getUtente().getParametriDocumenti().containsKey(tipoMat)) {
			isAbilitatoTipoMateriale(tipoMat);	//almaviva5_20080505
			return;
		} else
			throw new UtenteNotAuthorizedException();
	}

	public Map<?, ?> getListaAttivita() {
		return Collections.unmodifiableMap(getUtente().getAttivita());
	}

	public void checkLivAutAuthority(String tipoAuthority, int livelloAuthority)
			throws UtenteNotAuthorizedException {
		if (getUtente().getParametriAuthority().containsKey(tipoAuthority)
				&& getUtente().getParametriAuthority()
						.get(tipoAuthority).getLivelloAut() >= livelloAuthority) {
			return;
		} else
			throw new UtenteNotAuthorizedException();
	}

	public void checkLivAutDocumenti(String tipoMateriale, int livelloAuthority)
			throws UtenteNotAuthorizedException {
		if (tipoMateriale == null || tipoMateriale.equals("")
				|| tipoMateriale.equals(" ")) {
			tipoMateriale = "M";
		}
		if (getUtente().getParametriDocumenti().containsKey(tipoMateriale)
				&& getUtente().getParametriDocumenti()
						.get(tipoMateriale).getLivelloAut() >= livelloAuthority) {
			return;
		} else
			throw new UtenteNotAuthorizedException();
	}

	public void isAuthorityForzatura(String tipoAuthority)
			throws UtenteNotAuthorizedException {
		if (getUtente().getParametriAuthority().containsKey(tipoAuthority)
				&& getUtente().getParametriAuthority()
						.get(tipoAuthority).getAbilitatoForzatura()
						.equals("S")) {
			return;
		} else
			throw new UtenteNotAuthorizedException();
	}

	public String getLivAutAuthority(String tipoAuthority)
			throws UtenteNotAuthorizedException {
		return String.valueOf(getUtente()
				.getParametriAuthority().get(tipoAuthority).getLivelloAut());
	}

	public String getLivAutDocumenti(String tipoMateriale)
			throws UtenteNotAuthorizedException {
		return String.valueOf(getUtente()
				.getParametriDocumenti().get(tipoMateriale).getLivelloAut());
	}

	public void isAbilitatoLegameTitoloAuthority(String tipoAuthority)
			throws UtenteNotAuthorizedException {
		ParametriAuthorityVO paramAuth = getUtente().getParametriAuthority().get(tipoAuthority);
		if (paramAuth == null)
			throw new UtenteNotAuthorizedException();
		if (paramAuth.getAbilitaLegamiDoc().equalsIgnoreCase("S"))
			return;

		throw new UtenteNotAuthorizedException();
	}

	public void isAbilitatoAuthority(String tipoAuthority)
			throws UtenteNotAuthorizedException {
		ParametriAuthorityVO paramAuthority = getUtente()
				.getParametriAuthority().get(tipoAuthority);
		if (paramAuthority == null)
			throw new UtenteNotAuthorizedException();
		if (paramAuthority.getAbilitaAuthority().equalsIgnoreCase("S"))
			return;

		throw new UtenteNotAuthorizedException();
	}

	public void isAbilitatoTipoMateriale(String tipoMateriale)
			throws UtenteNotAuthorizedException {
		ParametriDocumentiVO paramDocumenti = getUtente()
				.getParametriDocumenti().get(tipoMateriale);
		if (paramDocumenti == null)
			throw new UtenteNotAuthorizedException();
		if (paramDocumenti.getAbilitaOggetto().equalsIgnoreCase("S"))
			return;

		throw new UtenteNotAuthorizedException();
	}

	public void isDocumentoForzatura(String tipoMateriale)
			throws UtenteNotAuthorizedException {
		ParametriDocumentiVO paramDocumenti = getUtente()
				.getParametriDocumenti().get(tipoMateriale);
		if (paramDocumenti == null)
			throw new UtenteNotAuthorizedException();
		if (paramDocumenti.getAbilitatoForzatura().equalsIgnoreCase("S"))
			return;

		throw new UtenteNotAuthorizedException();
	}

	public void checkAttivita(String cod_attivita, String codPolo, String codBibOperante, List<Object> parametri)
			throws UtenteNotAuthorizedException {

		//doppio controllo biblio + utente
		if (getUtente().getAttivitaBib().contains(cod_attivita))
			if (getUtente().getAttivita().containsKey(cod_attivita))
				return;

		throw new UtenteNotAuthorizedException();
	}

	public void reloadDefault() throws EJBException {
		try {
			getUtente().setDefault(getAmministrazione().reloadDefault(getUtente().getIdUtenteProfessionale()));
		} catch (RemoteException e) {
			log.error("", e);
			throw new EJBException((Exception) e.detail);
		}
	}

	private UserProfile getUtente() {
		UserProfile utente = null;
		if (utenteRef != null)
			utente = utenteRef.get();

		if (utente == null) {
			try {
				log.warn("profilo per ticket '" + ticket + "' non trovato.");
				utente = getAmministrazione().getProfile(ticket);
			} catch (Exception e) {
				log.error("", e);
				return null;
			}
			utenteRef = new SoftReference<UserProfile>(utente);
		}

		utente.setTicket(ticket);
		return utente;
	}

	public void refresh() throws TicketExpiredException {
		if (ticket != null)
			checkTicket(ticket);
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		try {
			return method.invoke(this, args);
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	public void isAuthorityForzaturaIndice(String tipoAuthority)
			throws RemoteException, UtenteNotAuthorizedException {

		Map<String, ParametriAuthorityVO> paramsIndice = getUtente().getParametriAuthorityIndice();
		if (ValidazioneDati.isFilled(paramsIndice)
				&& paramsIndice.containsKey(tipoAuthority)
				&& (paramsIndice.get(tipoAuthority)).getAbilitatoForzatura().equals("S"))
			return;
		else
			throw new UtenteNotAuthorizedException();

	}

	public void isAbilitatoAuthorityIndice(SbnAuthority auth)
			throws RemoteException, UtenteNotAuthorizedException {
		Map<String, ParametriAuthorityVO> paramsIndice = getUtente().getParametriAuthorityIndice();
		if (!ValidazioneDati.isFilled(paramsIndice) )
			throw new UtenteNotAuthorizedException();

		ParametriAuthorityVO paramAuthority = paramsIndice.get(auth.toString());
		if (paramAuthority == null)
			throw new UtenteNotAuthorizedException();

		if (paramAuthority.getAbilitaAuthority().equalsIgnoreCase("S"))
			return;

		throw new UtenteNotAuthorizedException();
	}

	public void checkAttivitaAutIndice(String attivita, SbnAuthority auth)
			throws RemoteException, UtenteNotAuthorizedException {
		Map<String, ParametriAuthorityVO> paramsIndice = getUtente().getParametriAuthorityIndice();
		String cod_attivita = ValidazioneDati.trimOrEmpty(attivita).toUpperCase().replace(' ', '_');
		log.debug("check attivita indice: " + cod_attivita);
		if (getUtente().getAttivitaIndice().containsKey(cod_attivita) && paramsIndice.containsKey(auth.toString()))
			return;

		throw new UtenteNotAuthorizedException();
	}

	public boolean isAuthoritySoloLocale(SbnAuthority auth) throws UtenteNotAuthorizedException {

		Map<String, ParametriAuthorityVO> params = getUtente().getParametriAuthority();
		if (!ValidazioneDati.isFilled(params) )
			throw new UtenteNotAuthorizedException();

		ParametriAuthorityVO paramAuthority = params.get(auth.toString());
		if (paramAuthority == null)
			throw new UtenteNotAuthorizedException();

		return paramAuthority.isSololocale();
	}

	public boolean isTipoMaterialeSoloLocale(String tipoMateriale) throws UtenteNotAuthorizedException {

		ParametriDocumentiVO paramDocumenti = getUtente().getParametriDocumenti().get(tipoMateriale);
		if (paramDocumenti == null)
			throw new UtenteNotAuthorizedException();

		return paramDocumenti.isSololocale();
	}
}
