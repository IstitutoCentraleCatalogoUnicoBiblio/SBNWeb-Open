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

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.SbnBusinessSessionBean;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.exception.AlreadyExistsException;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.IntervalloSegnaturaNonValidoException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.GestioneServizi;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.common.TipoAggiornamentoIter;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.RinnovoDirittiDiffVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.AttivitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.ElementoSinteticaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.RicercaAutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModalitaPagamentoVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ServizioWebDatiRichiestiVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnRicercaVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.EsemplareDocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.FaseControlloIterVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.PenalitaServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SupportiModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.OccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.RicercaOccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.relazioni.RelazioniVO;
import it.iccu.sbn.ejb.vo.servizi.segnature.RangeSegnatureVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.RicercaTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.SpecTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.SinteticaUtenteVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.AutorizzazioniVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.MateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaMateriaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloDisponibilitaVO;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;
import org.hibernate.exception.DataException;

public class GestioneServiziBean extends SbnBusinessSessionBean implements GestioneServizi {


	private static final long serialVersionUID = 3982211538848422318L;

	private static Logger log = Logger.getLogger(GestioneServizi.class);

	private Servizi servizi;


	private Servizi getServizi() {
		if (servizi == null) {
			try {
				servizi = DomainEJBFactory.getInstance().getServizi();
			} catch (Exception e) {
				log.error("", e);
			}
		}
		return servizi;
	}

	public void ejbCreate() {
		log.info("creato ejb");
	}


	public boolean cancellaModalitaPagamento(String ticket, String[] idModalita, Integer[] idModalitaNonCancellate, BaseVO utenteVar)
	throws EJBException, RemoteException {
		return this.getServizi().cancellaModalitaPagamento(ticket, idModalita, idModalitaNonCancellate, utenteVar);
	}

	public ModalitaPagamentoVO aggiornaModalitaPagamento(String ticket,
			ModalitaPagamentoVO modalitaVO) throws ApplicationException {
		try {
			return this.getServizi().aggiornaModalitaPagamento(ticket, modalitaVO);
		} catch (ApplicationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		}
	}

	public List getModalitaPagamento(String ticket, String codPolo, String codBib)
	throws EJBException, RemoteException {
		return this.getServizi().getModalitaPagamento(ticket, codPolo, codBib);
	}

	public List getSupportiBiblioteca(String ticket, String codPolo, String codBib, String fl_svolg)
	throws EJBException, RemoteException {
		return this.getServizi().getSupportiBiblioteca(ticket, codPolo, codBib, fl_svolg);
	}

	public boolean cancellaSupportiBiblioteca(String ticket, String[] idSupporti, Integer[] idSupportiNonCancellati, BaseVO utenteVar)
	throws EJBException, RemoteException {
		return this.getServizi().cancellaSupportiBiblioteca(ticket, idSupporti, idSupportiNonCancellati, utenteVar);
	}

	public boolean aggiornaSupportoBiblioteca(String ticket, SupportoBibliotecaVO supportoVO)
	throws EJBException, RemoteException {
		return this.getServizi().aggiornaSupportoBiblioteca(ticket, supportoVO);
	}

	public boolean verificaAutoregistrazione(String ticket, String codPolo, String codBib)
	throws EJBException, RemoteException {
		return this.getServizi().verificaAutoregistrazione(ticket, codPolo, codBib);
	}

	public SupportoBibliotecaVO getSupportoBiblioteca(String ticket, String codPolo, String codBib, String codSupporto)
	throws EJBException, RemoteException {
		return this.getServizi().getSupportoBiblioteca(ticket, codPolo, codBib, codSupporto);
	}

	public ParametriBibliotecaVO aggiornaParametriBiblioteca(String ticket, ParametriBibliotecaVO parametriVO)
	throws ApplicationException {
		try {
			return this.getServizi().aggiornaParametriBiblioteca(ticket, parametriVO);
		} catch (ApplicationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		}
	}

	public ParametriBibliotecaVO getParametriBiblioteca(String ticket, String codPolo, String codBib)
	throws EJBException, RemoteException {
		return this.getServizi().getParametriBiblioteca(ticket, codPolo, codBib);
	}

	public boolean cancellaControlloIter(String ticket, String[] codiciControllo, int idTipoServizio, String codAttivita, String utenteVar)
	throws EJBException, RemoteException {
		return this.getServizi().cancellaControlloIter(ticket, codiciControllo, idTipoServizio, codAttivita, utenteVar);
	}

	public boolean aggiornaControlloIter(String ticket, FaseControlloIterVO controlloIterVO, int idTipoServizio,
										String codAttivita, boolean nuovo, short posizione, TipoAggiornamentoIter tipoOperazione)
	throws EJBException, RemoteException {
		return this.getServizi().aggiornaControlloIter(ticket, controlloIterVO, idTipoServizio, codAttivita, nuovo, posizione, tipoOperazione);
	}

	public List getControlloIter(String ticket, int idTipoServizio, String codAttivita)
	throws EJBException, RemoteException {
		return this.getServizi().getControlloIter(ticket, idTipoServizio, codAttivita);
	}

	public boolean aggiornaIter(String ticket, int idTipoServizio, int progr_iter_selezionato, IterServizioVO iter, TipoAggiornamentoIter tipoOperazione, boolean nuovo)
	throws EJBException, RemoteException {
		return this.getServizi().aggiornaIter(ticket, idTipoServizio, progr_iter_selezionato, iter, tipoOperazione, nuovo);
	}

	public boolean cancellaIterServizio(String ticket, String[] progressiviIter, int idTipoServizio, String utenteVar)
	throws EJBException, RemoteException {
		return this.getServizi().cancellaIterServizio(ticket, progressiviIter, idTipoServizio, utenteVar);
	}

	public boolean aggiornaAttivitaBibliotecario(String ticket, List bibliotecariDaAggiungere, List bibliotecariDaRimuovere,
			BaseVO infoBase, int idTipoServizio, String codiceAttivita)
	throws EJBException, RemoteException {
		return this.getServizi().aggiornaAttivitaBibliotecario(ticket, bibliotecariDaAggiungere, bibliotecariDaRimuovere, infoBase, idTipoServizio, codiceAttivita);
	}

	public Map getListeAttivitaBibliotecari(String ticket, TipoServizioVO tipoServizioVO, String codAttivita)
	throws EJBException, RemoteException {
		Map mappaListe=null;
		try {
			mappaListe = this.getServizi().getListeAttivitaBibliotecari(ticket, tipoServizioVO, codAttivita);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return mappaListe;
	}

	public boolean aggiornaTariffeModalitaErogazione(String ticket, TariffeModalitaErogazioneVO tariffeVO, boolean nuovo, int idTipoServizio)
	throws EJBException, RemoteException {
		boolean ret = false;
		try {
			ret = this.getServizi().aggiornaTariffeModalitaErogazione(ticket, tariffeVO, nuovo, idTipoServizio);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public boolean aggiornaSupportiModalitaErogazione(String ticket, SupportiModalitaErogazioneVO tariffeVO, boolean nuovo, int idSupportiBiblioteca)
	throws EJBException, RemoteException {
		boolean ret = false;
		try {
			ret = this.getServizi().aggiornaSupportiModalitaErogazione(ticket, tariffeVO, nuovo, idSupportiBiblioteca);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public boolean aggiornaModalitaErogazione(String ticket, ModalitaErogazioneVO tariffeVO, boolean nuovo)
	throws EJBException, RemoteException {
		boolean ret = false;
		try {
			ret = this.getServizi().aggiornaModalitaErogazione(ticket, tariffeVO, nuovo);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public boolean aggiornaModuloRichiesta(String ticket, List<ServizioWebDatiRichiestiVO> lstServizioWebDatiRichiestiVO)
	throws EJBException, ApplicationException {
		boolean ret = false;
		try {
			ret = this.getServizi().aggiornaModuloRichiesta(ticket, lstServizioWebDatiRichiestiVO);
		} catch (ApplicationException e) {
			throw e;
		} catch (DataException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public boolean aggiornaTipoServizio(String ticket, TipoServizioVO tipoServizioVO)
	throws EJBException, RemoteException {
		boolean ret = false;
		try {
			ret = this.getServizi().aggiornaTipoServizio(ticket, tipoServizioVO);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public boolean aggiornaServizio(String ticket, ServizioBibliotecaVO servizioVO, int idTipoServizio)
	throws EJBException, RemoteException {
		boolean ret = false;
		try {
			ret = this.getServizi().aggiornaServizio(ticket, servizioVO, idTipoServizio);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public boolean cancellaServizio(String ticket, ServizioBibliotecaVO servizioVO, int idTipoServizio)
	throws EJBException, RemoteException {
		boolean ret = false;
		try {
			ret = this.getServizi().cancellaServizio(ticket, servizioVO, idTipoServizio);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public boolean cancellaTariffeModalitaErogazione(String ticket, TariffeModalitaErogazioneVO modErog, int idTipoServizio)
	throws EJBException, RemoteException {
		boolean ret = false;
		try {
			ret = this.getServizi().cancellaTariffeModalitaErogazione(ticket, modErog, idTipoServizio);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public boolean cancellaSupportiModalitaErogazione(String ticket, SupportiModalitaErogazioneVO modErog, int idSupportiBiblioteca)
	throws EJBException, RemoteException {
		boolean ret = false;
		try {
			ret = this.getServizi().cancellaSupportiModalitaErogazione(ticket, modErog, idSupportiBiblioteca);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public boolean cancellaModalitaErogazione(String ticket, ModalitaErogazioneVO modErog)
	throws EJBException, RemoteException {
		boolean ret = false;
		try {
			ret = this.getServizi().cancellaModalitaErogazione(ticket, modErog);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}


	// autorizzazioni
	public DescrittoreBloccoVO getListaAutorizzazioni(String ticket,
			RicercaAutorizzazioneVO ricercaAut) throws EJBException,
			RemoteException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			elenco = this.getServizi().getListaAutorizzazioni(ticket, ricercaAut);
			blocco1 = this.saveBlocchi(ricercaAut.getTicket(), elenco,
					ricercaAut.getNumeroElementiBlocco());
			// if (blocco1 == null)
			// throw new EJBException("Errore in generazione blocchi");
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		}
		return blocco1;
	}

	public boolean insertAutorizzazione(String ticket, AutorizzazioneVO recAutorizzazione)
			throws ApplicationException, EJBException {
		boolean ret = false;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			ret = this.getServizi().insertAutorizzazione(ticket, recAutorizzazione);
		} catch (ApplicationException e) {
			throw e;
		} catch (DataException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public boolean updateAutorizzazione(String ticket, AutorizzazioneVO recAutorizzazione)
			throws ApplicationException, EJBException {
		boolean ret = false;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			ret = this.getServizi().updateAutorizzazione(ticket, recAutorizzazione);
		} catch (ApplicationException e) {
			throw e;
		} catch (DataException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public boolean cancelAutorizzazione(String ticket, AutorizzazioneVO recAutorizzazione)
			throws EJBException {
		boolean ret = false;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			ret = this.getServizi().cancelAutorizzazione(ticket, recAutorizzazione);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}


	public boolean esistonoUtentiCon(String ticket, String codPolo, String codBib, String codAutorizzazione)
	throws EJBException {
		boolean ret;

		try {
			ret = this.getServizi().esistonoUtentiCon(ticket, codPolo, codBib, codAutorizzazione);
		} catch (DataException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException(e);
		}

		return ret;
	}

	public boolean esistonoUtentiConOcc(String ticket, String codPolo, String codBib, int idOcc)
	throws EJBException {
		boolean ret;

		try {
			ret = this.getServizi().esistonoUtentiConOcc(ticket, codPolo, codBib, idOcc);
		} catch (DataException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException(e);
		}

		return ret;
	}

	public boolean esistonoUtentiConSpecTit(String ticket, String codPolo, String codBib, int idSpecTit)
	throws EJBException {
		boolean ret;

		try {
			ret = this.getServizi().esistonoUtentiConSpecTit(ticket, codPolo, codBib, idSpecTit);
		} catch (DataException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException(e);
		}

		return ret;
	}


	public List getListaServiziPerTipoServizio(String ticket, String codicePolo, String codiceBiblioteca, String codTipoServizio)
	throws EJBException
	{
		List elenco = null;
		try {
			elenco = this.getServizi().getListaServiziPerTipoServizio(ticket, codicePolo, codiceBiblioteca,	codTipoServizio);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return elenco;
	}

	public List getListaServizi(String ticket, String codicePolo, String codiceBiblioteca, String codiceAutorizzazione)
	throws EJBException
	{
		List elenco = null;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			elenco = this.getServizi().getListaServizi(ticket, codicePolo, codiceBiblioteca,	codiceAutorizzazione);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return elenco;
	}

	public List getListaServiziAutorizzazione(String ticket, UtenteBibliotecaVO utente) throws EJBException {
		List elenco = null;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			elenco = this.getServizi().getListaServiziAutorizzazione(ticket, utente);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return elenco;
	}


	public String getListaAutorizzazioniServizio(String ticket, String codPoloSer, String codBibSer, int idServ ) throws EJBException {
		String elenco = null;
		try {
			elenco = this.getServizi().getListaAutorizzazioniServizio(ticket,  codPoloSer,  codBibSer, idServ);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return elenco;
	}


	public DescrittoreBloccoVO getListaServiziBiblioteca(String ticket, List serviziAssociati,
														 String codicePolo,
														 String codiceBiblioteca,
														 int elemBlocco)
		throws EJBException
	{
		List<ElementoSinteticaServizioVO> elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.getServizi().getListaServiziBiblioteca(ticket, serviziAssociati, codicePolo, codiceBiblioteca);
			blocco1 = this.saveBlocchi(ticket, elenco, elemBlocco);
			if (blocco1 == null)
				throw new EJBException("Errore in generazione blocchi");
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return blocco1;
	}


	public int getAutomaticoX(String ticket, String codicePolo, String codiceBib, String codAutorizzazione, char codiceAutomaticoX)
			throws EJBException {
		int numrec = 0;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			numrec = this.getServizi().getAutomaticoX(ticket, codicePolo, codiceBib, codAutorizzazione, codiceAutomaticoX);
			//(numAutomaticoX, codAutorizzazione);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numrec;
	}


	public AutorizzazioniVO getAutorizzazioneByProfessione(String ticket, String codicePolo, String codiceBib, int idOcc)
	throws EJBException {
		AutorizzazioniVO autorizzazioneVO = null;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			autorizzazioneVO = this.getServizi().getAutorizzazioneByProfessione(ticket, codicePolo, codiceBib,idOcc);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return autorizzazioneVO;
		}


	public void cancellaOccupazioni(String ticket, Integer[] id, String uteVar)
	throws EJBException {
		try {
			this.getServizi().cancellaOccupazioni(ticket, id, uteVar);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}


	public DescrittoreBloccoVO getListaOccupazioni(String ticket, RicercaOccupazioneVO ricerca)
	throws EJBException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			elenco = this.getServizi().getListaOccupazioni(ticket, ricerca);
			blocco1 = this.saveBlocchi(ricerca.getTicket(), elenco, ricerca
					.getNumeroElementiBlocco());
			if (blocco1 == null)
				throw new EJBException("Errore in generazione blocchi");
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return blocco1;
	}


	public void aggiornaOccupazione(String ticket, OccupazioneVO occupazioneVO, boolean  nuovo)
	throws  AlreadyExistsException, EJBException {
		try {
			this.getServizi().aggiornaOccupazione(ticket, occupazioneVO, nuovo);
		}  catch (AlreadyExistsException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public void cancellaMaterie(String ticket, Integer[] idMaterie, String uteVar)
	throws ApplicationException, EJBException {
		try {
			this.getServizi().cancellaMaterie(ticket, idMaterie, uteVar);
		} catch (ApplicationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public void aggiornaMateria(String ticket, MateriaVO materiaVO, boolean nuovo)
	throws  AlreadyExistsException, EJBException {
			try {
				this.getServizi().aggiornaMateria(ticket, materiaVO, nuovo);
			} catch (AlreadyExistsException e) {
				throw e;
			} catch (RemoteException e) {
				throw new EJBException((Exception)e.detail);
			}
	}

	public List getListaMaterieCompleta(String ticket, RicercaMateriaVO materia)
	throws EJBException {
		List elenco = null;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			elenco = this.getServizi().getListaMaterie(ticket, materia);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
		return elenco;
	}

	public DescrittoreBloccoVO getListaMaterie(String ticket, RicercaMateriaVO materia)
	throws EJBException {
		DescrittoreBloccoVO blocco1 = null;
		try {
			List elenco = this.getServizi().getListaMaterie(ticket, materia);
			blocco1 = this.saveBlocchi(ticket, elenco, materia.getNumeroElementiBlocco());
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
		if (blocco1 == null)
			throw new EJBException("Errore in generazione blocchi");

		return blocco1;
	}

	public void aggiornaSpecTitoloStudio(String ticket, SpecTitoloStudioVO specTitoloVO, boolean nuovo)
	throws AlreadyExistsException, EJBException {
		try {
			this.getServizi().aggiornaSpecTitoloStudio(ticket, specTitoloVO, nuovo);
		} catch (AlreadyExistsException e) {
			throw e;
		}  catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public void cancellaSpecTitoloStudio(String ticket, Integer[] id, String uteVar)
	throws EJBException {
		try {
			this.getServizi().cancellaSpecTitoloStudio(ticket, id, uteVar);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public DescrittoreBloccoVO getListaTitoloStudio(String ticket, RicercaTitoloStudioVO ricTDS)
			throws EJBException, Exception {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			elenco = this.getServizi().getListaTitoloStudio(ticket, ricTDS);
			blocco1 = this.saveBlocchi(ricTDS.getTicket(), elenco, ricTDS
					.getNumeroElementiBlocco());
			if (blocco1 == null)
				throw new EJBException("Errore in generazione blocchi");
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return blocco1;
	}


	public DescrittoreBloccoVO getListaUtenti(String ticket,
			RicercaUtenteBibliotecaVO uteRicerca, int elemBlocco)
			throws ValidationException, EJBException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			elenco = this.getServizi().getListaUtenti(ticket, uteRicerca);
			blocco1 = this.saveBlocchi(ticket, elenco, elemBlocco);
			if (blocco1 == null)
				throw new EJBException("Errore in generazione blocchi");

		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
		return blocco1;
	}

	public DescrittoreBloccoVO getListaAnagServiziUte(String ticket,
			UtenteBibliotecaVO utente, int elemBlocco) throws EJBException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			elenco = this.getServizi().getListaAnagServiziUte(ticket, utente);
			blocco1 = this.saveBlocchi(ticket, elenco, elemBlocco);
			if (blocco1 == null)
				throw new EJBException("Errore in generazione blocchi");
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return blocco1;
	}

	//public boolean updateDataRinnovoAut(String ticket, List lstUte, String dataRinnAut)
	public boolean updateDataRinnovoAut(String ticket, String[] lstCodUte, String dataRinnAut)
			throws EJBException {
		boolean ret = false;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			//ret = this.servizi.updateDataRinnovoAut(lstUte, dataRinnAut);
			ret = this.getServizi().updateDataRinnovoAut(ticket, lstCodUte, dataRinnAut);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public UtenteBibliotecaVO getDettaglioUtente(String ticket,
			RicercaUtenteBibliotecaVO recUte, String numberFormat, Locale locale) throws EJBException {
		UtenteBibliotecaVO ute = null;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			ute = this.getServizi().getDettaglioUtente(ticket, recUte, numberFormat, locale);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ute;
	}


	public UtenteBibliotecaVO getDettaglioUtenteBase(String ticket,
			RicercaUtenteBibliotecaVO recUte, String numberFormat, Locale locale) throws EJBException {
		UtenteBibliotecaVO ute = null;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			ute = this.getServizi().getDettaglioUtenteBase(ticket, recUte, numberFormat, locale);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ute;
	}


	public boolean insertUtente(String ticket, UtenteBibliotecaVO recUte) throws ApplicationException, RemoteException {
		boolean ret = false;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			ret = this.getServizi().insertUtente(ticket, recUte);
		} catch (ApplicationException e) {
			throw e;
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public boolean updateUtente(String ticket, UtenteBibliotecaVO recUte)
			throws ApplicationException, EJBException {
		boolean ret = false;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			ret = this.getServizi().updateUtente(ticket, recUte);
		} catch (ApplicationException e) {
			throw e;
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	public boolean importaUtente(String ticket, UtenteBibliotecaVO recUte)
			throws EJBException {
		boolean ret = false;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			ret = this.getServizi().importaUtente(ticket, recUte);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	public boolean aggiornaChiaveUtenteById(String ticket,  String idUtente, String chiaveUte )
	throws EJBException {
	boolean ret = false;
	try {
		// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
		ret = this.getServizi().aggiornaChiaveUtenteById(ticket, idUtente, chiaveUte);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}





	public UtenteBibliotecaVO importaBibliotecaComeUtente(String ticket, String codPolo, String codBib, BibliotecaVO bibVO, BaseVO vo, Locale locale)
	throws EJBException {
		try {
			return this.getServizi().importaBibliotecaComeUtente(ticket, codPolo, codBib, bibVO, vo, locale);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public boolean cancelUtenteBiblioteca(String ticket, UtenteBibliotecaVO recUte)
			throws EJBException {
		boolean ret = false;
		try {
			ret = this.getServizi().cancelUtenteBiblioteca(ticket, recUte);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public int verificaMovimentiUtente(String ticket, String idUte)
			throws EJBException {
		int ret = 0;
		try {
			// ServiziDAO dao = DAOFactory.getDAO(ServiziDAO.class);
			ret = this.getServizi().verificaMovimentiUtente(ticket, idUte);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		}
		return ret;
	}

	// erogazioni
	public ControlloAttivitaServizioResult esisteRichiesta(String ticket, MovimentoVO movimento) throws EJBException {
		try {
			return this.getServizi().controlloDisponibilita(ticket, new ControlloDisponibilitaVO(movimento, false)).getResult();
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public boolean esisteMovimentoAttivo(String ticket,	MovimentoVO movimento) throws EJBException {
		try {
			return this.getServizi().esisteMovimentoAttivo(ticket, movimento);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}


	public boolean movimentoStatoPrecedeConsegnaDocLett(String ticket, MovimentoListaVO movimento) throws EJBException {
		try {
			return this.getServizi().movimentoStatoPrecedeConsegnaDocLett(ticket, movimento);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}


	/**
	 *
	  * it.iccu.sbn.ejb.bean
	  * GestioneServiziBean.java
	  * getListaMovimentiPerErogazione
	  * Map
	  * @param filtroMov
	  * @param filtro
	  * @param ticket
	  * @param descrSegn
	  * @param locale
	  * @return Map con le seguenti chiavi:<br/>
	  * <ul>
	  * <li><strong>BLOCCO_RICHIESTE:</strong> Istanza di DescrittoreBloccoVO contenente la lista di richieste</li>
	  * <li><strong>SOLLECITI:</strong> List di SollecitiVO</li>
	  * </ul>
	  *
	  * @throws EJBException
	  *
	  *
	 */
	public Map getListaMovimentiPerErogazione(String ticket, MovimentoVO filtroMov,
			RicercaRichiesteType filtro, Locale locale)
			throws EJBException {

		Map richieste_solleciti = new HashMap();
		DescrittoreBloccoVO blocco1 = null;
		try {
			Map mappaRichieste = this.getServizi().getListaMovimentiPerErogazione(ticket, filtroMov, filtro, locale);

			blocco1 = this.saveBlocchi(ticket, (List<MovimentoListaVO>) mappaRichieste.get(ServiziConstant.RICHIESTE), filtroMov.getElemPerBlocchi());
			if (blocco1 == null)
				throw new EJBException("Errore in generazione blocchi");
			richieste_solleciti.put(ServiziConstant.BLOCCO_RICHIESTE, blocco1);
			richieste_solleciti.put(ServiziConstant.SOLLECITI, mappaRichieste.get(ServiziConstant.SOLLECITI));

		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);

		} catch (ApplicationException e) {
			throw new EJBException(e);

		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		}

		return richieste_solleciti;

	}


	/**
	 *
	  * it.iccu.sbn.ejb.bean
	  * GestioneServiziBean.java
	  * getListaMovimenti
	  * Map
	  * @param anaMov
	  * @param filtro
	  * @param ticket
	  * @param descrSegn
	  * @param locale
	  * @return Map con 2 chiavi<br/>
	 * <ul>
	 * <li><strong>RICHIESTE:</strong> lista delle richieste (istanze di MovimentoListaVO)</li>
	 * <li><strong>SOLLECITI:</strong> lista dei solleciti (istanze di SollecitiVO)</li>
	 * </ul>
	  * @throws EJBException
	  *
	  *
	 */
	public Map getListaMovimenti(String ticket, MovimentoVO anaMov,
			RicercaRichiesteType filtro, Locale locale) throws EJBException {
		try {
			return this.getServizi().getListaMovimentiPerErogazione(ticket, anaMov, filtro, locale);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public void cancellaRichieste(String ticket, Long[] codRichieste, String uteVar) throws EJBException {
		try {
			this.getServizi().cancellaRichieste(ticket, codRichieste, uteVar);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public void rifiutaRichieste(String ticket, Long[] codRichieste, String uteVar, boolean inviaMailNotifica) throws EJBException {
		try {
			this.getServizi().rifiutaRichieste(ticket, codRichieste, uteVar, inviaMailNotifica);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public int getNumeroMovimentiAttivi(String ticket, String codPolo, String codBibInv, String codSerieInv, int codInven)
	throws EJBException {
		try {
			return this.getServizi().getNumeroMovimentiAttivi(ticket, codPolo, codBibInv, codSerieInv, codInven);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}


	public int getNumeroRichiesteAttivePerUtente(String ticket, MovimentoVO anaMov)
	throws EJBException {
		try {
			return this.getServizi().getNumeroRichiesteAttivePerUtente(ticket, anaMov);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public int getNumeroMovimentiAttiviPerUtente(String ticket, MovimentoVO anaMov)
	throws EJBException {
		try {
			return this.getServizi().getNumeroMovimentiAttiviPerUtente(ticket, anaMov);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public int getNumeroRichiesteGiornalierePerServizio(String ticket, MovimentoVO anaMov)
	throws EJBException {
		try {
			return this.getServizi().getNumeroRichiesteGiornalierePerServizio(ticket, anaMov);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public List getListaSollecitiUte(String ticket, MovimentoVO anaMov, RicercaRichiesteType filtro) throws EJBException {
		List elenco = null;
		try {
			elenco = this.getServizi().getListaSollecitiUte(ticket, anaMov, filtro);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
		return elenco;
	}

	public ServizioBibliotecaVO getServizioBiblioteca(String ticket, String codPolo, String codBib, String codTipoServizio, String codServizio)
	throws EJBException {
		try {
			return this.getServizi().getServizioBiblioteca(ticket, codPolo, codBib, codTipoServizio, codServizio);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public MovimentoListaVO getMovimentoListaVO(String ticket, MovimentoVO movimentoSelezionato, Locale locale)
	throws EJBException {
		try {
			return this.getServizi().getMovimentoListaVO(ticket, movimentoSelezionato, locale);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public List<TariffeModalitaErogazioneVO >getTariffaModalitaErogazione(String ticket,
			String codicePolo, String codiceBiblioteca, String codTipoServ, String fl_svolg) throws EJBException {
		List<TariffeModalitaErogazioneVO> elenco = null;
		try {
			elenco = this.getServizi().getTariffaModalitaErogazione(ticket, codicePolo,
					codiceBiblioteca, codTipoServ, fl_svolg);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
		return elenco;
	}

	public List<SupportiModalitaErogazioneVO >getSupportoModalitaErogazione(String ticket,
			String codicePolo, String codiceBiblioteca, String codSupporto, String fl_svolg) throws EJBException {
		List<SupportiModalitaErogazioneVO> elenco = null;
		try {
			elenco = this.getServizi().getSupportoModalitaErogazione(ticket, codicePolo,
					codiceBiblioteca, codSupporto, fl_svolg);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
		return elenco;
	}

	public List<ModalitaErogazioneVO> getTariffaModalitaErogaz(String ticket,
			String codicePolo, String codiceBiblioteca) throws EJBException {
		List<ModalitaErogazioneVO> elenco = null;
		try {
			elenco = this.getServizi().getTariffaModalitaErogaz(ticket, codicePolo, codiceBiblioteca);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
		return elenco;
	}


	public List<IterServizioVO> getIterServizio(String ticket, String codicePolo, String codiceBiblioteca, String codTipoServ)
	throws EJBException {
		List<IterServizioVO> elenco = null;
		try {
			elenco = this.getServizi().getListaIterServizio(ticket, codicePolo, codiceBiblioteca, codTipoServ);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}

		return elenco;
	}

	public DescrittoreBloccoVO verificaEsistenzaUtentePolo(String ticket, UtenteBibliotecaVO uteAna) throws EJBException {
		List<SinteticaUtenteVO> elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.getServizi().verificaEsistenzaUtentePolo(ticket, uteAna);
			blocco1 = this.saveBlocchi(ticket, elenco, 4000);
			if (blocco1 == null)
				throw new EJBException("Errore generazione blocchi");
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
		return blocco1;
	}


	public DescrittoreBloccoVO verificaEsistenzaUtentePolo(String ticket, BibliotecaVO bibVO, String codPolo, String codBib, BaseVO vo)
	throws EJBException {
		List<SinteticaUtenteVO> elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.getServizi().verificaEsistenzaUtentePolo(ticket, bibVO, codPolo, codBib, vo);
			blocco1 = this.saveBlocchi(ticket, elenco, 4000);
			if (blocco1 == null)
				throw new EJBException("Errore generazione blocchi");
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
		return blocco1;
	}


	public int getNumMovimentiAttiviPerServizioUtente(String ticket, int idUtenteBiblioteca, int idServizio)
		throws EJBException {

	   int numeroServizi=0;
	   try {
		   numeroServizi = this.getServizi().getNumMovimentiAttiviPerServizioUtente(ticket, idUtenteBiblioteca, idServizio);
		   return numeroServizi;
	   } catch (Exception ex){
		   throw new EJBException(ex);
	   }
	}


	public AutorizzazioneVO getTipoAutorizzazione(String ticket, String codPolo, String codBib, String codTipoAut)
		throws EJBException {
		try {
			return this.getServizi().getTipoAutorizzazione(ticket, codPolo, codBib, codTipoAut);
		} catch (Exception ex){
			   throw new EJBException(ex);
		}

	}

	public java.util.List getListaTipiServizio(String ticket, String codPolo, String codBib)
	throws EJBException {
		try {
			return this.getServizi().getListaTipiServizio(ticket, codPolo, codBib);
		}catch (Exception ex){
			   throw new EJBException(ex);
		}
	}

	public java.util.List<String> getListaCodiciTipiServizio(String ticket, String codPolo, String codBib)
	   throws EJBException {
		try {
			return this.getServizi().getListaCodiciTipiServizio(ticket, codPolo, codBib);
		}catch (Exception ex){
			   throw new EJBException(ex);
		}
	}

	public List<ServizioWebDatiRichiestiVO> getServizioWebDatiRichiesti(String ticket, String codPolo, String codBib, String codTipoServizio, String natura)
	   throws EJBException {
		try {
			return this.getServizi().getServizioWebDatiRichiesti(ticket, codPolo, codBib, codTipoServizio, natura);
		}catch (Exception ex){
			   throw new EJBException(ex);
		}
	}

	public TipoServizioVO getTipoServizio(String ticket, String codPolo, String codBib, String codTipoServizio)
	   throws EJBException {
		try {
			return this.getServizi().getTipoServizio(ticket, codPolo, codBib, codTipoServizio);
		}catch (Exception ex){
			   throw new EJBException(ex);
		}
	}

	public List<ServizioBibliotecaVO> getServiziAttivi(String ticket,
			String codPolo, String codBibUte, String codUtente, String codBib,
			Timestamp data, boolean remoto)
	throws EJBException {
		try {
			return this.getServizi().getServiziAttivi(ticket, codPolo, codBibUte, codUtente, codBib, data, remoto);
		}catch (Exception ex){
			   throw new EJBException(ex);
		}
	}

	public boolean isUtenteAutorizzato(String ticket, String codPolo,
			   String codBibUte, String codUtente, String codBib, String codTipoServ, String codServ, Timestamp data)
	throws EJBException {
		try {
			return this.getServizi().isUtenteAutorizzato(ticket, codPolo, codBibUte, codUtente, codBib, codTipoServ, codServ, data);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public List<ServizioBibliotecaVO> getServiziAttivi(String ticket, String codPolo, String codBib,  String codFrui)
	throws EJBException {
		try {
			return this.getServizi().getServiziAttivi(ticket, codPolo, codBib, codFrui);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public void scambioControlliIter(String ticket, int idTipoServizio, String codAttivita, short progressivo, TipoAggiornamentoIter tipoOp)
	throws EJBException {
		try {
			this.getServizi().scambioControlliIter(ticket, idTipoServizio, codAttivita, progressivo, tipoOp);
		} catch (Exception ex){
			   throw new EJBException(ex);
		}
	}

	public void scambioIter(String ticket, int id_tipo_servizio, int progressivo, TipoAggiornamentoIter tipoOp)
	throws EJBException {
		try {
			this.getServizi().scambioIter(ticket, id_tipo_servizio, progressivo, tipoOp);
		} catch (Exception ex){
			   throw new EJBException(ex);
		}
	}

	public UtenteBaseVO getUtente(String ticket, String codPolo, String codBibUte, String codUtente, String codBib)
	throws EJBException {
		try {
			return this.getServizi().getUtente(ticket, codPolo, codBibUte, codUtente, codBib);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public UtenteBaseVO getUtente(String ticket, String codUtente)
	throws EJBException {
		try {
			return this.getServizi().getUtente(ticket, codUtente);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public MovimentoVO aggiornaRichiesta(String ticket, MovimentoVO movimento, int idServizio)
	throws ApplicationException, EJBException {
		try {
			return this.getServizi().aggiornaRichiesta(ticket, movimento, idServizio);
		} catch (ApplicationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public MovimentoVO aggiornaRichiestaPerCambioServizio(String ticket,
			MovimentoVO nuovaRichiesta, long codRichDaCancellare,
			int idServizio, String uteVar) throws ApplicationException {
		try {
			return this.getServizi().aggiornaRichiestaPerCambioServizio(ticket,
					nuovaRichiesta, codRichDaCancellare, idServizio, uteVar);
		} catch (ApplicationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public List<AttivitaServizioVO> getListaAttivitaSuccessive(String ticket, String codPolo, String codBib, String codTipoServ, int progrIter, DatiRichiestaILLVO datiILL)
	throws EJBException {
		try {
			return this.getServizi().getListaAttivitaSuccessive(ticket, codPolo, codBib, codTipoServ, progrIter, datiILL);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public AttivitaServizioVO getAttivita(String ticket, String codPolo, IterServizioVO iterServizioVO)
	throws EJBException {
		try {
			return this.getServizi().getAttivita(ticket, codPolo, iterServizioVO);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public PenalitaServizioVO getPenalitaServizio(String ticket, String codPolo, String codBib, String codTipoServizio, String codServizio)
	throws EJBException {
		try {
			return this.getServizi().getPenalitaServizio(ticket, codPolo, codBib, codTipoServizio, codServizio);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public void sospendiDirittoUtente(String ticket, MovimentoVO mov, Date dataSospensione, BaseVO datiModifica)
	throws ApplicationException, EJBException {
		try {
			this.getServizi().sospendiDirittoUtente(ticket, mov, dataSospensione, datiModifica);
		} catch (ApplicationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public boolean esistonoPrenotazioni(String ticket, UtenteBaseVO utenteBaseVO, MovimentoVO anaMov, Timestamp data)
	throws EJBException {
		try {
			return this.getServizi().esistonoPrenotazioni(ticket, utenteBaseVO, anaMov, data);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public boolean esistonoPrenotazioni(String ticket, MovimentoVO anaMov, Timestamp data)
	throws EJBException {
		try {
			return this.getServizi().esistonoPrenotazioni(ticket, anaMov, data);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public int getNumeroPrenotazioni(String ticket, MovimentoVO anaMov)
	throws EJBException {
		try {
			return this.getServizi().getNumeroPrenotazioni(ticket, anaMov);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public int getNumeroPrenotazioni(String ticket)
	throws EJBException {
		try {
			return this.getServizi().getNumeroPrenotazioni(ticket);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public DescrittoreBloccoVO getPrenotazioni(String ticket, MovimentoVO anaMov, Locale locale)
	throws EJBException {
		DescrittoreBloccoVO blocco1 = null;
		try {
			//almaviva5_20131105 #5426
			List prenotazioni = this.getServizi().getPrenotazioni(ticket, anaMov, anaMov.getCodBibOperante(), locale, anaMov.getTipoOrdinamento());
			blocco1 = this.saveBlocchi(ticket, prenotazioni, anaMov.getElemPerBlocchi());
			if (blocco1 == null)	throw new EJBException("Errore in generazione blocchi");

			return blocco1;
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public DescrittoreBloccoVO getPrenotazioni(String ticket, String codBibDest, int maxRighe, Locale locale, String tipoOrd)
	throws EJBException {
		DescrittoreBloccoVO blocco1 = null;
		try {
			List prenotazioni = this.getServizi().getPrenotazioni(ticket, null, codBibDest, locale, tipoOrd);
			blocco1 = this.saveBlocchi(ticket, prenotazioni, maxRighe);
			if (blocco1 == null)	throw new EJBException("Errore in generazione blocchi");

			return blocco1;
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public DescrittoreBloccoVO getProroghe(String ticket, String codBibDest, int maxRighe, Locale locale, String tipoOrd)
	throws EJBException {
		DescrittoreBloccoVO blocco1 = null;
		try {
			List proroghe = this.getServizi().getProroghe(ticket, null, codBibDest, locale, tipoOrd);
			blocco1 = this.saveBlocchi(ticket, proroghe, maxRighe);
			if (blocco1 == null)	throw new EJBException("Errore in generazione blocchi");

			return blocco1;
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public DescrittoreBloccoVO getGiacenze(String ticket, String codBibDest, int maxRighe, Locale locale, String tipoOrd)
	throws EJBException {
		DescrittoreBloccoVO blocco1 = null;
		try {
			List giacenze = this.getServizi().getGiacenze(ticket, null, codBibDest, locale, tipoOrd);
			blocco1 = this.saveBlocchi(ticket, giacenze, maxRighe);
			if (blocco1 == null)	throw new EJBException("Errore in generazione blocchi");

			return blocco1;
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public void aggiornaSegnatura(String ticket, RangeSegnatureVO segnaturaVO, boolean isNew)
	throws IntervalloSegnaturaNonValidoException, AlreadyExistsException, EJBException {
		try {
			this.getServizi().aggiornaSegnatura(ticket, segnaturaVO, isNew);
		} catch(IntervalloSegnaturaNonValidoException e) {
			throw e;
		} catch (AlreadyExistsException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public void cancellaSegnature(String ticket, Integer[] id, String uteVar)
	throws EJBException {
		try {
			this.getServizi().cancellaSegnature(ticket, id, uteVar);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public DescrittoreBloccoVO caricaSegnature(String ticket, String codPolo, String codBib, RangeSegnatureVO segnaturaVO)
	throws EJBException {
		List segnature;
		try {
			segnature = this.getServizi().caricaSegnature(ticket, codPolo, codBib, segnaturaVO);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}

		DescrittoreBloccoVO blocco1 = this.saveBlocchi(ticket, segnature, segnaturaVO.getElemPerBlocchi());
		if (blocco1 == null)	throw new EJBException("Errore in generazione blocchi");

		return blocco1;
	}


	public List caricaRelazioni(String ticket, String codPolo, String codBib, String codRelazione)
	throws EJBException {
		try {
			return this.getServizi().caricaRelazioni(ticket, codPolo, codBib, codRelazione);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public void cancellaRelazioni(String ticket, Integer[] id, String uteVar)
	throws EJBException {
		try {
			this.getServizi().cancellaRelazioni(ticket, id, uteVar);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public void riattivaRelazioni(String ticket, Integer[] id, String uteVar)
	throws EJBException {
		try {
			this.getServizi().riattivaRelazioni(ticket, id, uteVar);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public void aggiornaRelazione(String ticket, RelazioniVO relazioneVO)
	throws AlreadyExistsException, EJBException {
		try {
			this.getServizi().aggiornaRelazione(ticket, relazioneVO);
		} catch (AlreadyExistsException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public DescrittoreBloccoVO getListeTematiche(String ticket,	MovimentoVO filtroMov, boolean attivitaAttuale, int elemBlocco) throws EJBException {
		try {
			List<MovimentoListaVO> listeTematiche = this.getServizi().getListeTematiche(ticket, filtroMov, attivitaAttuale);
			return saveBlocchi(ticket, listeTematiche, elemBlocco);

		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public DescrittoreBloccoVO getListaDocumentiNonSbn(String ticket, DocumentoNonSbnRicercaVO filtro) throws ApplicationException, EJBException {
		try {
			List<DocumentoNonSbnVO> documenti = this.getServizi().getListaDocumentiNonSbn(ticket, filtro);
			return saveBlocchi(ticket, documenti, filtro.getElementiPerBlocco() );

		} catch (ApplicationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public DescrittoreBloccoVO getListaEsemplariDocumentiNonSbn(String ticket,
			DocumentoNonSbnRicercaVO filtro) throws EJBException {
		try {
			List<EsemplareDocumentoNonSbnVO> esemplari = this.getServizi()
					.getListaEsemplariDocumentiNonSbn(ticket, filtro);
			return saveBlocchi(ticket, esemplari, filtro.getElementiPerBlocco());

		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public List<DocumentoNonSbnVO>  aggiornaDocumentoNonSbn(String ticket, List<DocumentoNonSbnVO> documenti) throws EJBException, ApplicationException {
		try {
			return this.getServizi().aggiornaDocumentoNonSbn(ticket, documenti);

		} catch (ApplicationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public void aggiornaEsemplariDocumentoNonSbn(String ticket,	List<EsemplareDocumentoNonSbnVO> esemplari) throws EJBException, ApplicationException {
		try {
			this.getServizi().aggiornaEsemplariDocumentoNonSbn(ticket, esemplari);

		} catch (ApplicationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public DocumentoNonSbnVO getDettaglioDocumentoNonSbn(String ticket,
			DocumentoNonSbnVO documento) throws EJBException,
			ApplicationException {

		try {
			return this.getServizi().getDettaglioDocumentoNonSbn(ticket, documento);

		} catch (ApplicationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		}
	}

	public CommandResultVO invoke(CommandInvokeVO command) throws ApplicationException {
		try {
			CommandResultVO invoke = this.getServizi().invoke(command);
			if (invoke.getError() != null)
				return invoke;

			switch (invoke.getCommand()) {
			case LISTA_RICHIESTE_SCADUTE:
				MovimentoVO mov = (MovimentoVO) command.getParams()[0];
				DescrittoreBloccoVO blocco1 = saveBlocchi(invoke.getTicket(), (List<?>) invoke.getResult(), mov.getElemPerBlocchi() );
				invoke = CommandResultVO.build(command, blocco1, null);
			}

			return invoke;

		} catch (ApplicationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		}
	}

	public List<ComboCodDescVO> loadCodiciDiversiDaQuelliDefiniti(
			String ticket, String codPolo, String codBib, List codici, String flSvolg) throws EJBException {

		List<ModalitaErogazioneVO> lista = this.getTariffaModalitaErogaz(ticket, codPolo, codBib);
		List<ComboCodDescVO> listaFiltrata = new ArrayList<ComboCodDescVO>();
		Iterator<ModalitaErogazioneVO> iterator = lista.iterator();

		while (iterator.hasNext()) {
			ModalitaErogazioneVO modErog = iterator.next();

			if (!codici.contains(modErog.getCodErog().trim())) {
				// se il flSvolg non è valorizzato il codice viene aggiunto, altrimenti si controlla
				// se la mod.erog. è locale o ILL
				if (!ValidazioneDati.isFilled(flSvolg) || modErog.getFlSvolg().equals(flSvolg)) {
					listaFiltrata.add(new ComboCodDescVO(modErog.getCodErog(), modErog.getDesModErog()));
				}
			}
		}
		return listaFiltrata;
	}

	public RinnovoDirittiDiffVO gestioneDifferitaRinnovoDiritti(RinnovoDirittiDiffVO rinnDirVO )
	throws EJBException {
		try {
			return this.getServizi().gestioneDifferitaRinnovoDiritti(rinnDirVO);
		} catch (RemoteException e) {
			throw new EJBException((Exception)e.detail);
		}
	}

	public Date getDateAutorizzazione(String ticket, String codPolo, String codBib, String codAutorizzazione, int idutente, String tipo )
	throws EJBException {
		Date output = null;

		try {
			output= this.getServizi().getDateAutorizzazione( ticket,codPolo,  codBib,  codAutorizzazione,  idutente,  tipo );
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}


	public boolean checkEsistenzaUtente(String codfiscale, String mail, String[] ateneo_mat, String idAna) throws EJBException {
		boolean ritorno=false;
		try {
			ritorno = getServizi().checkEsistenzaUtente(codfiscale, mail, ateneo_mat, idAna) ;
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ritorno;
		}

	public DocumentoNonSbnVO getCategoriaFruizioneSegnatura(String ticket, String codPolo, String codBib, String ordSegn) throws ApplicationException {
		DocumentoNonSbnVO ritorno;
		try {
			ritorno=getServizi().getCategoriaFruizioneSegnatura(ticket, codPolo, codBib, ordSegn);
		}catch (ApplicationException e) {
				throw e;

		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		}
		return ritorno;
	}
	public List<DocumentoNonSbnVO>  esisteDocumentoNelRangeDiSegnatura(String ticket,  String codPolo, String codBib, RangeSegnatureVO segnaturaVO, String ordinamento) throws ApplicationException {
		List<DocumentoNonSbnVO> ritorno;
		try {
			ritorno=getServizi().esisteDocumentoNelRangeDiSegnatura(ticket, codPolo, codBib, segnaturaVO, ordinamento);
		}catch (ApplicationException e) {
				throw e;

		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		}
	return ritorno;
}


} // End GestioneSezioniBean
