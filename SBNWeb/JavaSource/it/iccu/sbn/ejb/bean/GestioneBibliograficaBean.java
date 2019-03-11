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
import it.iccu.sbn.ejb.AbstractStatelessSessionBean;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.acquisizioni.Acquisizioni;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBiblioteca;
import it.iccu.sbn.ejb.domain.documentofisico.Inventario;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.AuthenticationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.remote.GestioneBibliografica;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AllineaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityMultiplaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPassaggioCancAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPassaggioGetIdSbnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiPropostaDiCorrezioneVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazionePropostaDiCorrezioneVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaParametriStampaSchedeVo;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCatturareVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCondividereVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.AreaDatiPassaggioInterrogazioneLuogoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.altre.AreaDatiVariazioneLuogoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiPassaggioInterrogazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.AreaDatiVariazioneAutoreVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.AreaDatiPassaggioInterrogazioneMarcaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.marca.AreaDatiVariazioneMarcaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioElenchiListeConfrontoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioSchedaDocCiclicaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiScambiaResponsLegameTitAutVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiVariazioneTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.CatturaMassivaBatchVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class GestioneBibliograficaBean extends AbstractStatelessSessionBean implements GestioneBibliografica {


	private static final long serialVersionUID = -1216474555988016614L;

	private static Logger logger = Logger.getLogger(GestioneBibliografica.class);

	private it.iccu.sbn.ejb.domain.bibliografica.Interrogazione interrogazione;

	// Chiamata per interrogazione area Documento Fisico per Interrogazione da ID Gestionali per Inventario
	private Inventario inventarioGestDocFisico;
	private Acquisizioni acquisizioniGestDocFisico;


	private AmministrazioneBiblioteca amministrazioneBiblioteca;
	private it.iccu.sbn.ejb.domain.bibliografica.Profiler profiler;

    public void ejbCreate() {
        logger.info("creato ejb");
        try {
        	this.interrogazione = DomainEJBFactory.getInstance().getInterrogazione();
			this.amministrazioneBiblioteca = DomainEJBFactory.getInstance().getBiblioteca();
			this.inventarioGestDocFisico = DomainEJBFactory.getInstance().getInventario();
			this.acquisizioniGestDocFisico = DomainEJBFactory.getInstance().getAcquisizioni();
			this.profiler = DomainEJBFactory.getInstance().getProfiler();

        } catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


	public SBNMarc eseguiRichiestaServerSBN(SBNMarc marc) throws EJBException {
		SBNMarc marcOut = null;
		try {
			//marcOut = XMLFactory.eseguiRichiestaServerSBN(marc, false, "P");
			logger.info(marcOut);
		} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return marcOut;
	}

	// AREA RICHIESTA PROGRESSIVO SBN (Bid-Vid ...)
	public AreaDatiPassaggioGetIdSbnVO getIdSbn(
			AreaDatiPassaggioGetIdSbnVO areaDatiPass, String ticket) {
		AreaDatiPassaggioGetIdSbnVO areaOut = null;
		try {
			areaOut = this.interrogazione.getIdSbn(areaDatiPass,ticket);
		} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	// AREA ACCORPAMENTO FRA OGGETTI
	public AreaDatiAccorpamentoReturnVO richiestaAccorpamento(
			AreaDatiAccorpamentoVO areaDatiPass, String ticket) {
		AreaDatiAccorpamentoReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.richiestaAccorpamento(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiAccorpamentoReturnVO richiestaSpostamentoLegami(
			AreaDatiAccorpamentoVO areaDatiPass, String ticket) {
		AreaDatiAccorpamentoReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.richiestaSpostamentoLegami(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	// AREA LOCALIZZAZIONI
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO cercaLocalizzazioni(
			AreaDatiLocalizzazioniAuthorityVO areaDatiPass, boolean soloPresenzaPolo, String ticket) {
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.cercaLocalizzazioni(areaDatiPass,soloPresenzaPolo,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiVariazioneReturnVO localizzaAuthorityMultipla(
			AreaDatiLocalizzazioniAuthorityMultiplaVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.localizzaAuthorityMultipla(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiVariazioneReturnVO localizzaUnicoXML(
			AreaDatiLocalizzazioniAuthorityMultiplaVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.localizzaUnicoXML(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}


	public AreaDatiVariazioneReturnVO localizzaAuthority(
			AreaDatiLocalizzazioniAuthorityVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.localizzaAuthority(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	// AREA CANCELLAZIONE AUTHORITY
	public AreaDatiPassaggioCancAuthorityVO cancellaAuthority(
			AreaDatiPassaggioCancAuthorityVO areaDatiPass, String ticket) {

		AreaDatiPassaggioCancAuthorityVO areaOut = null;

		try {
			areaOut = this.interrogazione.cancellaAuthority(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	// AREA CATTURA
	public AreaDatiVariazioneReturnVO catturaReticolo(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.catturaReticolo(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiVariazioneReturnVO catturaAutore(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.catturaAutore(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiVariazioneReturnVO catturaMarca(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.catturaMarca(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiVariazioneReturnVO catturaLuogo(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.catturaLuogo(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiVariazioneReturnVO scatturaReticolo(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.scatturaReticolo(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}


	public AreaDatiVariazioneReturnVO inserisciReticoloCatturato(
			AreaTabellaOggettiDaCatturareVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.inserisciReticoloCatturato(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaTabellaOggettiDaCondividereVO ricercaDocumentoPerCondividi(
			AreaTabellaOggettiDaCondividereVO areaDatiPass, String ticket) {
		AreaTabellaOggettiDaCondividereVO areaOut = null;
		try {
			areaOut = this.interrogazione.ricercaDocumentoPerCondividi(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaTabellaOggettiDaCondividereVO ricercaAutorePerCondividi(
			AreaTabellaOggettiDaCondividereVO areaDatiPass, String ticket) {
		AreaTabellaOggettiDaCondividereVO areaOut = null;
		try {
			areaOut = this.interrogazione.ricercaAutorePerCondividi(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}


	// AREA TITOLI
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaTitoli(
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass, String ticket) {
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.ricercaTitoli(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}


	// AREA SCHEDE TITOLI

	public AreaDatiPassaggioElenchiListeConfrontoVO getElenchiListeConfronto(
			AreaDatiPassaggioElenchiListeConfrontoVO areaDatiPass, String ticket) {
		AreaDatiPassaggioElenchiListeConfrontoVO areaOut = null;
		try {
			areaOut = this.interrogazione.getElenchiListeConfronto(areaDatiPass, ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}


	public AreaDatiPassaggioSchedaDocCiclicaVO getSchedaDocCiclica(
			AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPass, String ticket) {
		AreaDatiPassaggioSchedaDocCiclicaVO areaOut = null;
		try {
			areaOut = this.interrogazione.getSchedaDocCiclica(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiPassaggioSchedaDocCiclicaVO insertTbReportIndice(
			AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPass, String ticket) {
		AreaDatiPassaggioSchedaDocCiclicaVO areaOut = null;
		try {
			areaOut = this.interrogazione.insertTbReportIndice(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiPassaggioSchedaDocCiclicaVO cancellaTabelleTbReportIndice(
			AreaDatiPassaggioSchedaDocCiclicaVO areaDatiPass, String ticket) {
		AreaDatiPassaggioSchedaDocCiclicaVO areaOut = null;
		try {
			areaOut = this.interrogazione.cancellaTabelleTbReportIndice(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}


	// Modifica almaviva2 04.08.2010 - Gestione periodici nuovo servizio per ricavare, dato un bid,  tutti i legami
	// fra periodici sia verso l'alto che verso il basso
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaLegamiFraPeriodici(
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass, String ticket) {
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.ricercaLegamiFraPeriodici(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}



	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaTitoliPerGestionali(
			AreaDatiPassaggioInterrogazioneTitoloVO areaDatiPass, Locale locale, String ticket) {
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaOut = null;

		try {
			if (areaDatiPass.getTipoProspettazioneGestionali().equals("LocFonSeg")) {
					areaOut = this.interrogazione.ricercaTitoliPerGestionali(areaDatiPass,ticket);
				} else if (areaDatiPass.getTipoProspettazioneGestionali().equals("Inventario")) {
					InventarioVO inventarioVO = null;
					try {
					inventarioVO = this.inventarioGestDocFisico.getInventario(areaDatiPass.getCodPolo(), areaDatiPass.getCodBiblio(), areaDatiPass.getSerieInventario(), areaDatiPass.getNumeroInventario(), locale, ticket);
					} catch (DataException de) {
						areaOut = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
						areaOut.setCodErr("9999");
						areaOut.setTestoProtocollo("Attenzione non esistono Documenti legati all'inventario indicato");
						return areaOut;
					}
					if (inventarioVO == null ||
							(inventarioVO.getBid() == null || inventarioVO.getBid().equals(""))) {
						areaOut = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
						areaOut.setCodErr("9999");
						areaOut.setTestoProtocollo("Attenzione non esistono Documenti legati all'inventario indicato");
						return areaOut;
					}
					areaDatiPass.getInterTitGen().setBid(inventarioVO.getBid());
					areaOut = this.interrogazione.ricercaTitoliPerGestionali(areaDatiPass,ticket);
				} else if (areaDatiPass.getTipoProspettazioneGestionali().equals("Ordine")) {
/*					if (areaDatiPass.getCodTipoOrdine()!=null && areaDatiPass.getCodTipoOrdine().trim().length()>0 && areaDatiPass.getAnnoOrdine()!=null && areaDatiPass.getAnnoOrdine().trim().length()>0 && areaDatiPass.getNumeroOrdine()!=null && areaDatiPass.getNumeroOrdine().trim().length()>0)
					{
*/						ListaSuppOrdiniVO listaSuppOrdiniVO = new ListaSuppOrdiniVO();
						listaSuppOrdiniVO.setAnnoOrdine(areaDatiPass.getAnnoOrdine());
						listaSuppOrdiniVO.setCodBibl(areaDatiPass.getCodBiblio());
						listaSuppOrdiniVO.setCodPolo(areaDatiPass.getCodPolo());
						listaSuppOrdiniVO.setCodOrdine(areaDatiPass.getNumeroOrdine());
						listaSuppOrdiniVO.setTipoOrdine(areaDatiPass.getCodTipoOrdine());
						listaSuppOrdiniVO.setTicket(ticket);
						List<OrdiniVO> ordini = this.acquisizioniGestDocFisico.getRicercaListaOrdini(listaSuppOrdiniVO);
						if (ordini.size() != 1) {
							areaOut.setCodErr("9999");
							areaOut.setTestoProtocollo("Attenzione non esiste l'ordine indicato");
							return areaOut;
						} else {
							if (ordini.get(0).getTitolo().getCodice() == null	|| ordini.get(0).getTitolo().getCodice().equals("")) {
								areaOut.setCodErr("9999");
								areaOut.setTestoProtocollo("Attenzione non esiste il Documento legato all'ordine indicato");
								return areaOut;
							} else {
								areaDatiPass.getInterTitGen().setBid(ordini.get(0).getTitolo().getCodice());
								areaOut = this.interrogazione.ricercaTitoliPerGestionali(areaDatiPass,ticket);
							}
						}

					}
/*					else
					{

					}

			} */
		} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);

		}
		return areaOut;
	}


	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoTitoliPerBID(
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass, String ticket) {
		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.creaRichiestaAnaliticoTitoliPerBID(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getNextBloccoTitoli(
			AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass, String ticket) {
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.getNextBloccoTitoli(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiVariazioneReturnVO inserisciTitolo(
			AreaDatiVariazioneTitoloVO areaDatiPass, String ticket)throws AuthenticationException {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {

			if (areaDatiPass.isVariazioneCompAntico() || areaDatiPass.isVariazioneTuttiComp()) {
				/*
				 * AGGIUNGERE IL CONTROLLO PER LA FORZATURA
				 */

				// Inizio modifica almaviva2 09.07.2010 Si verifica se siamo in presenza di un titoloDocumento o un Autority (TU/UM)
				// almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)
				if (areaDatiPass.getNaturaProfilazione().equals("A") || areaDatiPass.getNaturaProfilazione().equals("V")) {
					if (areaDatiPass.getDetTitoloPFissaVO().getTipoMat().equals("U")) {
						if(!this.profiler.isLivelloAuthority(SbnAuthority.UM, areaDatiPass.getDetTitoloPFissaVO().getLivAut(), ticket))	{
//									throw new AuthenticationException("Utente non Autorizzato");
							if (areaOut == null) {
								areaOut = new AreaDatiVariazioneReturnVO ();
							}
							areaOut.setCodErr("9999");
							areaOut.setTestoProtocollo("Operazione non consentita - Il livello di autorità dell'utente è inferiore a quello dell'oggetto selezionato");
							return areaOut;
						}
					} else {
						if(!this.profiler.isLivelloAuthority(SbnAuthority.TU, areaDatiPass.getDetTitoloPFissaVO().getLivAut(), ticket)) {
//									throw new AuthenticationException("Utente non Autorizzato");
							if (areaOut == null) {
								areaOut = new AreaDatiVariazioneReturnVO ();
							}
							areaOut.setCodErr("9999");
							areaOut.setTestoProtocollo("Operazione non consentita - Il livello di autorità dell'utente è inferiore a quello dell'oggetto selezionato");
							return areaOut;
						}
					}
				} else {
					if (!this.profiler.isLivelloDocumento(areaDatiPass.getNaturaProfilazione(),
							areaDatiPass.getDetTitoloPFissaVO().getTipoMat(),
							areaDatiPass.getDetTitoloPFissaVO().getLivAut(), ticket))		{
//								throw new AuthenticationException("Utente non Autorizzato");
						if (areaOut == null) {
							areaOut = new AreaDatiVariazioneReturnVO ();
						}
						areaOut.setCodErr("9999");
						areaOut.setTestoProtocollo("Operazione non consentita - Il livello di autorità dell'utente è inferiore a quello dell'oggetto selezionato");
						return areaOut;
					}
				}

//				if(!this.profiler.isLivelloDocumento(areaDatiPass.getNaturaProfilazione(),
//						areaDatiPass.getDetTitoloPFissaVO().getTipoMat(),
//						areaDatiPass.getDetTitoloPFissaVO().getLivAut(), ticket))		{
//					throw new AuthenticationException("Utente non Autorizzato");
//				}

				// Fine modifica almaviva2 09.07.2010 Si verifica se siamo in presenza di un titoloDocumento o un Autority (TU/UM)

			}
			areaOut = this.interrogazione.inserisciTitolo(areaDatiPass,ticket);
			} catch (Exception e) {
			if (areaOut == null)
				areaOut = new AreaDatiVariazioneReturnVO ();
			areaOut.setCodErr("9999");
			// Inizio modifica almaviva2 14.07.2010 Modificato messaggio di errore
			areaOut.setTestoProtocollo("Errore nella richiesta al Server SBN: " + e.getMessage().toString());
//			areaOut.setTestoProtocollo("Errore nella richiesta al Server SBN: " + e.toString());
			// Fine modifica almaviva2 14.07.2010 Modificato messaggio di errore
			e.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", e);
		}
		return areaOut;
	}

	public AreaDatiVariazioneReturnVO inserisciLegameTitolo(
			AreaDatiLegameTitoloVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.inserisciLegameTitolo(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiVariazioneReturnVO trascinaLegameAutore(
			AreaDatiLegameTitoloVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.trascinaLegameAutore(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiVariazioneReturnVO scambiaResponsabilitaLegameTitoloAutore(
			AreaDatiScambiaResponsLegameTitAutVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.scambiaResponsabilitaLegameTitoloAutore(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}


	// AREA AUTORI
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaAutori(
			AreaDatiPassaggioInterrogazioneAutoreVO areaDatiPass, String ticket) {
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.ricercaAutori(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoAutorePerVid(
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass, String ticket) {
		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.creaRichiestaAnaliticoAutorePerVid(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getNextBloccoAutori(
			AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass, String ticket) {
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.getNextBloccoAutori(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiVariazioneReturnVO inserisciAutore(
			AreaDatiVariazioneAutoreVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.inserisciAutore(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaAutoriCollegati(
			AreaDatiPassaggioInterrogazioneAutoreVO areaDatiPass, String ticket) {
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.ricercaAutoriCollegati(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiVariazioneReturnVO collegaElementoAuthority(
			AreaDatiLegameTitoloVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.collegaElementoAuthority(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiVariazioneReturnVO creaFormaRinvio(
			AreaDatiLegameTitoloVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.creaFormaRinvio(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiVariazioneReturnVO scambiaForma(
			AreaDatiLegameTitoloVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.scambiaForma(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}
	// AREA MARCHE
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaMarche(
			AreaDatiPassaggioInterrogazioneMarcaVO areaDatiPass, String ticket) {
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.ricercaMarche(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoMarchePerMid(
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass, String ticket) {
		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.creaRichiestaAnaliticoMarchePerMid(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getNextBloccoMarche(
			AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass, String ticket) {
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.getNextBloccoMarche(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiVariazioneReturnVO inserisciMarca(
			AreaDatiVariazioneMarcaVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.inserisciMarca(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaMarcheCollegate(
			AreaDatiPassaggioInterrogazioneMarcaVO areaDatiPass, String ticket) {
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.ricercaMarcheCollegate(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}


	public AreaDatiPassaggioInterrogazioneTitoloReturnVO ricercaLuoghi(
			AreaDatiPassaggioInterrogazioneLuogoVO areaDatiPass, String ticket) {
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.ricercaLuoghi(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}


	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getNextBloccoLuoghi(
			AreaDatiPassaggioInterrogazioneTitoloNextBloccoVO areaDatiPass, String ticket) {
		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.getNextBloccoLuoghi(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoLuoghiPerLid(
			AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass, String ticket) {
		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.creaRichiestaAnaliticoLuoghiPerLid(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiVariazioneReturnVO inserisciLuogo(
			AreaDatiVariazioneLuogoVO areaDatiPass, String ticket) {
		AreaDatiVariazioneReturnVO areaOut = null;
		try {
			areaOut = this.interrogazione.inserisciLuogo(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}


	public List getComboBibliotechePolo(String cd_polo, String ticket) throws EJBException {
		List<ComboCodDescVO> elenco = null;
		try {

			elenco = this.amministrazioneBiblioteca.getAllBiblioteche(cd_polo);
			//almaviva5_20140519 evolutive google3
			Iterator<ComboCodDescVO> i = elenco.iterator();
			while(i.hasNext())
				if (i.next().getCodice().equals(Constants.ROOT_BIB)) {
					i.remove();
					break;
				}

		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DaoManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return elenco;
	}


	public AllineaVO richiediListaAllineamenti(
			AllineaVO areaDatiPass, String ticket) {
		AllineaVO areaOut = null;
			try {
				areaOut = this.interrogazione.richiediListaAllineamenti(areaDatiPass,ticket);
				} catch (Exception ve) {
				ve.printStackTrace();
				logger.error("Errore nella richiesta al Server SBN", ve);
			}
			return areaOut;
		}


	public AllineaVO allineaBaseLocale(
			AllineaVO areaDatiPass, String ticket) {
		AllineaVO areaOut = null;
		try {
			areaOut = this.interrogazione.allineaBaseLocale(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}


	public AllineaVO allineamentoRepertoriDaIndice(String ticket) {
		AllineaVO areaOut = null;
		try {
			areaOut = this.interrogazione.allineamentoRepertoriDaIndice(ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public CatturaMassivaBatchVO catturaMassivaBatch(
			CatturaMassivaBatchVO areaDatiPass, String ticket) {
		CatturaMassivaBatchVO areaOut = null;
		try {
			areaOut = this.interrogazione.catturaMassivaBatch(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiPropostaDiCorrezioneVO cercaPropostaDiCorrezione(
			AreaDatiPropostaDiCorrezioneVO areaDatiPass, String ticket) {
		AreaDatiPropostaDiCorrezioneVO areaOut = null;
		try {
			areaOut = this.interrogazione.cercaPropostaDiCorrezione(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaDatiVariazionePropostaDiCorrezioneVO inserisciPropostaDiCorrezione(
			AreaDatiVariazionePropostaDiCorrezioneVO areaDatiPass, String ticket) {
		AreaDatiVariazionePropostaDiCorrezioneVO areaOut = null;
		try {
			areaOut = this.interrogazione.inserisciPropostaDiCorrezione(areaDatiPass,ticket);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaOut;
	}

	public AreaParametriStampaSchedeVo schedulatorePassiStampaSchede(
			AreaParametriStampaSchedeVo areaDatiPass, String ticket, BatchLogWriter log) {
		try {
			areaDatiPass = this.interrogazione.schedulatorePassiStampaSchede(areaDatiPass,ticket, log);
			} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nella richiesta al Server SBN", ve);
		}
		return areaDatiPass;
	}

	public CommandResultVO invoke(CommandInvokeVO command) throws ApplicationException {
		try {
			CommandResultVO invoke = this.interrogazione.invoke(command);
			if (invoke.getError() != null)
				return invoke;

			switch (invoke.getCommand()) {}

			return invoke;

		} catch (ApplicationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (Exception e) {
			return CommandResultVO.build(command, null, new ApplicationException(SbnErrorTypes.UNRECOVERABLE, e));
		}
	}

}
