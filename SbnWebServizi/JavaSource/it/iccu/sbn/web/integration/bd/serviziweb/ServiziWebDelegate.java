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
package it.iccu.sbn.web.integration.bd.serviziweb;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneMail;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.mail.MailUtil.AddressPair;
import it.iccu.sbn.vo.custom.servizi.CodTipoServizio;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.integration.bd.ServiziFactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.util.List;
import java.util.Locale;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

public class ServiziWebDelegate {

	private static Logger log = Logger.getLogger(ServiziWebDelegate.class);

	private ServiziFactoryEJBDelegate factory;
	private UserVO utenteCollegato;

	private final HttpServletRequest request;

	private static AmministrazioneMail amministrazioneMail;

	private AmministrazioneMail getAmministrazioneMail() throws Exception {

		if (amministrazioneMail != null)
			return amministrazioneMail;

		amministrazioneMail = DomainEJBFactory.getInstance().getAmministrazioneMail();
		return amministrazioneMail;
	}

	protected void saveErrors(HttpServletRequest request,
			ActionMessages errors, Throwable t) {
		if (t != null)
			Navigation.getInstance(request).setExceptionLog(t);

		if (errors == null || errors.isEmpty()) {
			request.removeAttribute("org.apache.struts.action.ERROR");
			return;
		} else {
			request.setAttribute("org.apache.struts.action.ERROR", errors);
			return;
		}
	}

	public static final ServiziWebDelegate getInstance(HttpServletRequest request) throws Exception {
		return new ServiziWebDelegate(request);
	}


	private ServiziWebDelegate(HttpServletRequest request) {
		this.request = request;
		try {
			this.factory = ServiziFactoryEJBDelegate.getInstance();
		} catch (Exception e) {
			log.error("", e);
		}
		this.utenteCollegato = Navigation.getInstance(request).getUtente();
	}

	public boolean isAbilitatoInserimentoRemoto(String polo, String bibCorrente, UtenteWeb utente) throws Exception {

		//leggo l'esistenza di almeno un Range di segnatura per la bib. selezionata dall'utente
		int rangeCnt = factory.getGestioneServiziWeb().contaRangeSegnature(polo, bibCorrente);
		String ticket = utenteCollegato.getTicket();
		//leggo i parametri di configurazione della bib. selezionata dall'utente
		ParametriBibliotecaVO parametri = factory.getGestioneServizi().getParametriBiblioteca(ticket, polo, bibCorrente);
		if (parametri == null)
			throw new ApplicationException(SbnErrorTypes.SRV_PARAMETRI_BIBLIOTECA_ASSENTI);

		if (ValidazioneDati.strIsNull(parametri.getCodFruizione()) && rangeCnt == 0) {//se non impostata categoria di default
			//non caricare voce di menù "Nuova richiesta"
			return false;
		}

		if (!utente.isRemoto())
			return true;

		boolean abilitaRemoto = true;

		//gestione
		if (!parametri.isAmmessoInserimentoUtente() ) {
			//non caricare voce di menù "Nuova richiesta"
			abilitaRemoto = false;
		} else if (!parametri.isAncheDaRemoto() ) {
			//non caricare voce di menù "Nuova richiesta"
			abilitaRemoto = false;
		}

		if (!abilitaRemoto) {
			ServiziDelegate delegate = ServiziDelegate.getInstance(request);
			List<ServizioBibliotecaVO> serviziAttivi = delegate.getServiziAttivi(polo,
					utente.getCodBib(), utente.getUserId(), bibCorrente,
					DaoManager.now(), true);
			abilitaRemoto = (ValidazioneDati.isFilled(serviziAttivi) );
		}

		return abilitaRemoto;
	}

	public UtenteBibliotecaVO getDettaglioUtente(String codBib, UtenteWeb utente) throws Exception {

		RicercaUtenteBibliotecaVO ricerca = new RicercaUtenteBibliotecaVO();
		ricerca.setCodPolo(utente.getCodPolo());
		ricerca.setCodBib(codBib);
		ricerca.setCodBibUte(utente.getCodBib());
		ricerca.setCodUte(utente.getUserId());
		//ricerca.setIdUte(utente.getIdUtente());

		UtenteBibliotecaVO dettaglio = factory.getGestioneServizi()
				.getDettaglioUtente(utenteCollegato.getTicket(), ricerca,
						ServiziConstant.NUMBER_FORMAT_CONVERTER,
						Locale.getDefault());

		return dettaglio;
	}

	public List<BibliotecaVO> getListaAltreBibPerAutoregistrazione(String codPolo, int idUtente) throws Exception {
		CommandInvokeVO command = new CommandInvokeVO(
				utenteCollegato.getTicket(),
				CommandType.SRV_LISTA_ALTRE_BIB_AUTOREG, codPolo, idUtente);
		try {
			CommandResultVO result = factory.getGestioneServizi().invoke(command);
			result.throwError();

			@SuppressWarnings("unchecked")
			List<BibliotecaVO> output = (List<BibliotecaVO>) result.getResult();

			return output;

		} catch (ApplicationException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);
		}

		return null;

	}

	public AddressPair getMailBiblioteca(String codPolo, String cod_bib) throws Exception {
		//almaviva5_20120920 #5117
		return getAmministrazioneMail().getMailBiblioteca(codPolo, cod_bib);
	}

	public List<BibliotecaVO> getListaBibliotecheRichiedentiILL(final UtenteWeb uteWeb, final List<BibliotecaVO> biblioteche) throws Exception {
		final ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		List<BibliotecaVO> richiedenti = Stream.of(uteWeb.getListaBiblio()).filter(new Predicate<BibliotecaVO>() {
			public boolean test(BibliotecaVO bib) {
				BibliotecaVO bibILL = UniqueIdentifiableVO.searchRepeatableId(bib.getRepeatableId(), biblioteche);
				boolean richiedente = bibILL != null && bibILL.isILLRichiedente();
				if (!richiedente)
					return false;

				//filtrati solo i servizi ILL
				long cnt;
				try {
					List<ServizioBibliotecaVO> lstDirittiUtente = delegate.getServiziAttivi(uteWeb.getCodPolo(), uteWeb.getCodBib(), uteWeb.getUserId(),
							bib.getCod_bib(), DaoManager.now());

					cnt = Stream.of(lstDirittiUtente).filter(new Predicate<ServizioBibliotecaVO>() {
						public boolean test(ServizioBibliotecaVO srv) {
							try {
								CodTipoServizio ts = CodTipoServizio.of(CodiciProvider.cercaCodice(srv.getCodTipoServ(),
										CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN));
								return ts == null || srv.isILL();
							} catch (Exception e) {
								return false;
							}
						}
					}).count();
				} catch (Exception e) {
					return false;
				}

				return cnt > 1;

			}}).toList();

		if (!ValidazioneDati.isFilled(richiedenti))
			return null;	//nessuna bib dell'utente aderisce a ILL come richiedente

		return richiedenti;
	}

}
