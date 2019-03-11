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

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.SbnBusinessSessionBean;
import it.iccu.sbn.ejb.domain.documentofisico.Collocazione;
import it.iccu.sbn.ejb.domain.documentofisico.DocumentoFisicoCommon;
import it.iccu.sbn.ejb.domain.documentofisico.Inventario;
import it.iccu.sbn.ejb.domain.documentofisico.Possessori;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.GestioneDocumentoFisico;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.blocchi.docfisico.EsameCollocazioniBloccoInterceptor;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoInterceptor;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoNumCollVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneTitoloVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneUltKeyLocVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.DatiBibliograficiCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareVO;
import it.iccu.sbn.ejb.vo.documentofisico.FormatiSezioniVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloDefaultVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloEtichetteVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriDiInventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.PossessoriRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.ProvenienzaInventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaEtichetteVO;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.web.keygenerator.GeneraChiave;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class GestioneDocumentoFisicoBean extends SbnBusinessSessionBean implements GestioneDocumentoFisico {


	private static final long serialVersionUID = 7177946640725474609L;

	private static Logger logger = Logger.getLogger(GestioneDocumentoFisico.class);

//	private DocumentoFisico documentoFisico;
	private Collocazione collocazione;
	private Inventario inventario;
	private Possessori possessori;

	private DocumentoFisicoCommon documentoFisicoCommon;

	public void ejbCreate() {
		super.ejbCreate();
		logger.info("creato ejb");
		try {
			DomainEJBFactory factory = DomainEJBFactory.getInstance();
			this.collocazione = factory.getCollocazione();
			this.inventario = factory.getInventario();
			this.documentoFisicoCommon = factory.getDocumentoFisicoCommon();
			this.possessori = factory.getPossessori();


		} catch (NamingException e) {


		} catch (RemoteException e) {


		} catch (CreateException e) {


		}
	}

//	sezioni di collocazione
	public DescrittoreBloccoVO getListaSezioni(String codPolo, String codiceBiblioteca, String ticket, int nRec)
	throws EJBException, RemoteException, ValidationException, DaoManagerException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.collocazione.getListaSezioni(codPolo, codiceBiblioteca, ticket);
			blocco1 = this.saveBlocchi(ticket, elenco, nRec);
//			if (blocco1 == null)
//				throw new EJBException("Errore in generazione blocchi");

		} catch (ResourceNotFoundException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		} catch (ValidationException e) {
//			logger.error("Errore in getBibliteche",e);
			throw (e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}
		return blocco1;
	}

	public SezioneCollocazioneVO getSezioneDettaglio(String codPolo, String codBib, String codSez, String ticket)
	throws EJBException, ValidationException, DataException {
		SezioneCollocazioneVO rec = null;

		try {
			rec = this.collocazione.getSezioneDettaglio(codPolo, codBib, codSez, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}

		return rec;

	}
	public boolean insertSezione(SezioneCollocazioneVO sezione, String ticket)
	throws EJBException, DataException, ValidationException {
		boolean ret = false;

		try {
			ret = this.collocazione.insertSezione(sezione, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (ValidationException e) {
			throw e;
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}

		return ret;

	}
	public boolean deleteSezione(SezioneCollocazioneVO sezione, String ticket)
	throws EJBException, ValidationException, DataException {
		boolean ret = false;

		try {
			ret = this.collocazione.deleteSezione(sezione, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}

		return ret;

	}
	public boolean updateSezione(SezioneCollocazioneVO sezione, String ticket)
	throws EJBException, ValidationException, DataException {
		boolean ret = false;

		try {
			ret = this.collocazione.updateSezione(sezione, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}

		return ret;

	}

	public DescrittoreBloccoVO getListaFormatiSezioni(String codPolo, String codiceBiblioteca, String codiceSezione,
			String ticket, int nRec)
	throws EJBException, RemoteException, DataException, ValidationException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.collocazione.getListaFormatiSezioni(codPolo, codiceBiblioteca, codiceSezione, ticket);
			blocco1 = this.saveBlocchi(ticket, elenco, nRec);

		} catch (ValidationException e) {
			throw (e);
		} catch (DataException e) {
			throw (e);
		} catch (RemoteException e) {

		} catch (Exception ve) {

		}
		return blocco1;
	}
	public DescrittoreBloccoVO getListaFormatiBib(String codPolo, String codiceBiblioteca, String codiceSezione,
			String ticket, int nRec)
	throws EJBException, RemoteException, DataException, ValidationException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.collocazione.getListaFormatiBib(codPolo, codiceBiblioteca, codiceSezione, ticket);
			blocco1 = this.saveBlocchi(ticket, elenco, nRec);

		} catch (ValidationException e) {
			throw (e);
		} catch (DataException e) {
			throw (e);
		} catch (RemoteException e) {

		} catch (Exception ve) {

		}
		return blocco1;
	}
	public DescrittoreBloccoVO getListaFormatiSezioni(String codPolo, String codiceBiblioteca, String ticket, int nRec)
	throws EJBException, RemoteException, DataException, ValidationException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.collocazione.getListaFormatiSezioni(codPolo, codiceBiblioteca, ticket);
			blocco1 = this.saveBlocchi(ticket, elenco, nRec);
//			if (blocco1 == null)
//				throw new EJBException("Errore in generazione blocchi");

		} catch (ValidationException e) {
//			logger.error("Errore in getBibliteche",e);
			throw (e);
		} catch (DataException e) {
//			logger.error("Errore in getBibliteche",e);
			throw (e);
		} catch (RemoteException e) {


		} catch (Exception ve) {

		}
		return blocco1;
	}
	public List getListaFormatiSezioni(String codPolo, String codiceBiblioteca, String codiceSezione,
			String ticket)
	throws EJBException, RemoteException, DataException, ValidationException {
		List elenco = null;
		try {
			elenco = this.collocazione.getListaFormatiSezioni(codPolo, codiceBiblioteca, codiceSezione, ticket);
		} catch (ValidationException e) {
			throw (e);
		} catch (DataException e) {
			throw (e);
		} catch (RemoteException e) {

		} catch (Exception ve) {

		}
		return elenco;
	}

public boolean insertFormatiSezioni(FormatiSezioniVO formatiSezioni, String ticket)
	throws EJBException, ValidationException, DataException {
		boolean ret = false;

		try {
			ret = this.collocazione.insertFormatiSezioni(formatiSezioni, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}
		return ret;
	}

	public boolean updateFormatiSezioni(FormatiSezioniVO formatiSezioni, String ticket) throws EJBException, ValidationException, DataException {
		boolean ret = false;

		try {
			ret = this.collocazione.updateFormatiSezioni(formatiSezioni, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}
		return ret;
	}

	public FormatiSezioniVO getFormatiSezioniDettaglio(String codPolo, String codBib, String codSez,
			String codFormato,String ticket) throws EJBException, ValidationException, DataException {
		FormatiSezioniVO rec = null;

		try {
			rec = this.collocazione.getFormatiSezioniDettaglio(codPolo, codBib, codSez,
					codFormato, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}
		return rec;
	}

	public DescrittoreBloccoVO getListaProvenienze(String codPolo, String codiceBiblioteca, String ticket, int nRec, String filtroProvenienza)
	throws EJBException, DataException, ValidationException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.inventario.getListaProvenienze(codPolo, codiceBiblioteca, ticket, filtroProvenienza);
			blocco1 = this.saveBlocchi(ticket, elenco, nRec);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (Exception e) {

		}
		return blocco1;
	}

	public ProvenienzaInventarioVO getProvenienza(String codPolo, String codBib, String codProven, String ticket)
	throws EJBException, ValidationException, DataException {
		ProvenienzaInventarioVO rec = null;

		try {
			rec = this.inventario.getProvenienza(codPolo, codBib, codProven, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}

		return rec;

	}

	public boolean insertProvenienza(ProvenienzaInventarioVO provenienza, String ticket)
	throws EJBException, ValidationException, DataException {
		boolean ret = false;

		try {
			ret = this.inventario.insertProvenienza(provenienza, ticket);
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}
		return ret;
	}

	public boolean updateProvenienza(ProvenienzaInventarioVO provenienza, String ticket) throws EJBException, ValidationException, DataException {
		boolean ret = false;

		try {
			ret = this.inventario.updateProvenienza(provenienza, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}
		return ret;
	}

	public DescrittoreBloccoVO getListaSerie(String codPolo, String codiceBiblioteca, String ticket, int nRec)
	throws EJBException, RemoteException, ValidationException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.inventario.getListaSerie(codPolo, codiceBiblioteca, ticket);
			blocco1 = this.saveBlocchi(ticket, elenco, nRec);
//			if (blocco1 == null)
//				throw new EJBException("Errore in generazione blocchi");

		} catch (ResourceNotFoundException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		}catch (RemoteException e) {


		}
		return blocco1;
	}

	public List getListaSerie(String codPolo, String codBib, String ticket)
	throws EJBException, RemoteException, ValidationException {
		List elenco = null;
		try {
			elenco = this.inventario.getListaSerie(codPolo, codBib, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ResourceNotFoundException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}
		return elenco;
	}

	public List getListaSerieDate(String codPolo, String codBib, String ticket)
	throws EJBException, RemoteException, ValidationException {
		List elenco = null;
		try {
			elenco = this.inventario.getListaSerieDate(codPolo, codBib, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ResourceNotFoundException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}
		return elenco;
	}

	public SerieVO getSerieDettaglio(String codPolo, String codBib, String codSez, String ticket)
	throws EJBException, ValidationException, DataException {
		SerieVO rec = null;

		try {
			rec = this.inventario.getSerieDettaglio(codPolo, codBib, codSez, ticket);
		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}

		return rec;

	}
	public boolean insertSerie(SerieVO serie, String ticket)
	throws EJBException, ValidationException, DataException {
		boolean ret = false;

		try {
			ret = this.inventario.insertSerie(serie, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}
		return ret;
	}

	public boolean updateSerie(SerieVO serie, String ticket) throws EJBException, ValidationException, DataException {
		boolean ret = false;

		try {
			ret = this.inventario.updateSerie(serie, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}
		return ret;
	}
	public DescrittoreBloccoVO getListaInventariTitolo(String codPolo, String codBib, String bid, String ticket, int nRec)
	throws EJBException, RemoteException, ValidationException, DataException {
		List inventari = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			inventari = this.inventario.getListaInventariTitolo(codPolo, codBib, bid, ticket);
//			if (inventari != null){
				blocco1 = this.saveBlocchi(ticket, inventari, nRec);
//				if (blocco1 == null)
//					throw new EJBException("Errore in generazione blocchi");
//			}
		}  catch (DataException e) {


			throw e;
		} catch (ValidationException e) {


			throw e;
		}catch (RemoteException e) {


			throw e;
		}
		return blocco1;
	}

	public DescrittoreBloccoVO getListaInventariTitolo(String codPolo, List<String> listaBiblio, String bid, String ticket, int nRec)
	throws EJBException, RemoteException, ValidationException, DataException {
		List inventari = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			inventari = this.inventario.getListaInventariTitolo(codPolo, listaBiblio, bid, ticket);
//			if (inventari != null){
				blocco1 = this.saveBlocchi(ticket, inventari, nRec);
//				if (blocco1 == null)
//					throw new EJBException("Errore in generazione blocchi");
//			}
		}  catch (DataException e) {


			throw e;
		} catch (ValidationException e) {


			throw e;
		}catch (RemoteException e) {


			throw e;
		}
		return blocco1;
	}

	public DescrittoreBloccoVO getListaInventariNonCollocati(EsameCollocRicercaVO paramRicerca,  String ticket, int nRec)
	throws EJBException, DataException, ValidationException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.inventario.getListaInventariNonCollocati(paramRicerca, ticket);
			blocco1 = this.saveBlocchi(ticket, elenco, nRec);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}catch (RemoteException e) {


			throw new EJBException(e);
		}
		return blocco1;
	}

	public DescrittoreBloccoVO getListaInventariDiCollocazione(EsameCollocRicercaVO paramRicerca,  String ticket, int nRec)
	throws EJBException, DataException, ValidationException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.inventario.getListaInventariDiCollocazione(paramRicerca, ticket);
			blocco1 = this.saveBlocchi(ticket, elenco, nRec);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}catch (RemoteException e) {


			throw new EJBException(e);
		}
		return blocco1;
	}

	public DescrittoreBloccoVO getListaInventariOrdine(String codPolo, String codBib, String codBibO,
			String codTipOrd, int annoOrd, int codOrd, String codBibF, Locale locale, String ticket, int nRec)
	throws EJBException, DataException, ValidationException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.inventario.getListaInventariOrdine(codPolo, codBib, codBibO,
					codTipOrd, annoOrd, codOrd, codBibF, locale, ticket, nRec);
			blocco1 = this.saveBlocchi(ticket, elenco, nRec);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}catch (RemoteException e) {


			throw new EJBException(e);
		}
		return blocco1;
	}

	public DescrittoreBloccoVO getListaInventariOrdini(List listaOrdini, Locale locale, String ticket, int nRec)
	throws EJBException, DataException, ValidationException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.inventario.getListaInventariOrdini(listaOrdini,locale,ticket,nRec );
			blocco1 = this.saveBlocchi(ticket, elenco, nRec);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}catch (RemoteException e) {


			throw new EJBException(e);
		}
		return blocco1;
	}

	public DescrittoreBloccoVO getListaInventariOrdiniNonFatturati(String codPolo, String codBib, String codBibO,
			String codTipOrd, int annoOrd, int codOrd, String codBibF, Locale locale, String ticket, int nRec)
	throws EJBException, DataException, ValidationException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.inventario.getListaInventariOrdiniNonFatturati(codPolo, codBib, codBibO,
					codTipOrd, annoOrd, codOrd, codBibF, locale, ticket, nRec);
			blocco1 = this.saveBlocchi(ticket, elenco, nRec);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}catch (RemoteException e) {


			throw new EJBException(e);
		}
		return blocco1;
	}

	public DescrittoreBloccoVO getListaInventariFattura(String codPolo, String codBibFattura,
			int annoFattura, int progrFattura, Locale locale, String ticket, int nRec)
	throws EJBException, DataException, ValidationException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.inventario.getListaInventariFattura(codPolo, codBibFattura,
					annoFattura, progrFattura, locale, ticket);
			blocco1 = this.saveBlocchi(ticket, elenco, nRec);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}catch (RemoteException e) {


			throw new EJBException(e);
		}
		return blocco1;
	}

	public DescrittoreBloccoVO getListaInventariRigaFattura(String codPolo, String codBib, String codBibO,
			String codTipOrd, int annoOrd, int codOrd, String codBibF,  int annoF, int prgF, int rigaF,
			Locale locale, String ticket, int nRec)
	throws EJBException, DataException, ValidationException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.inventario.getListaInventariRigaFattura(codPolo, codBib, codBibO,
					codTipOrd, annoOrd, codOrd, codBibF,  annoF, prgF, rigaF, locale, ticket);
			blocco1 = this.saveBlocchi(ticket, elenco, nRec);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}catch (RemoteException e) {


			throw new EJBException(e);
		}
		return blocco1;
	}

	public List insertInventario(InventarioVO inventario, String tipoOperazione, int nInv, Locale locale, String ticket)
	throws EJBException, ValidationException, RemoteException  {
		List inventari = null;
		try {
			inventari = this.inventario.insertInventario(inventario, tipoOperazione, nInv, locale, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		}
//		catch (ValidationException e) {
//		//logger.error("Errore in getBibliteche",e);
//		throw e;
//		}
		return inventari;
	}
	/*
	 public boolean insertCollocazione(CollocazioneVO recColl, String tipoOperazione, InventarioKeyVO invKey,
	 String livColl, List reticolo, String chiave, String confermaCodLoc)
	 throws EJBException, ValidationException  {
	 boolean ret = false;
	 try {
	 ret = dao.insertCollocazione(recColl, tipoOperazione, invKey, livColl, reticolo, chiave, confermaCodLoc);
	 } catch (DataException e) {
	 throw new EJBException(e);
	 } catch (ApplicationException e) {
	 throw new EJBException(e);
	 }
	 catch (ValidationException e) {
	 //logger.error("Errore in getBibliteche",e);
	  throw e;
	  }
	  return ret;
	  }
	  */
	public int updateInvColl(InventarioVO recInvColl, CollocazioneVO recColl,
			String tipoOperazione, String ticket)
	throws EJBException, it.iccu.sbn.ejb.exception.DataException, ValidationException {
		int ret = 0;

		try {
			ret = this.inventario.updateInvColl(recInvColl, recColl, tipoOperazione, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}
		return ret;
	}

	public boolean aggiornaInventarioFattura(InventarioVO recInv, String tipoOp, String ticket)
	throws EJBException, DataException, ValidationException {
		boolean ret = false;

		try {
			ret = this.inventario.aggiornaInventarioFattura(recInv, tipoOp, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}
		return ret;
	}

	public boolean deleteInventario(InventarioVO recInv, CollocazioneVO recColl, String tipoOperazione, String ticket)
	throws EJBException, DataException, ValidationException {
		boolean ret = false;

		try {
			ret = this.inventario.deleteInventario(recInv, recColl, tipoOperazione, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}
		return ret;
	}

	public boolean updateCollocazioneScolloca(InventarioVO recInv, CollocazioneVO recColl, String tipoOperazione, String ticket)
	throws EJBException, DataException, ValidationException {
		boolean ret = false;

		try {
			ret = this.inventario.updateCollocazioneScolloca(recInv, recColl, tipoOperazione, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}
		return ret;
	}

	//	esame collocazione
	public InventarioVO getInventario(String codPolo, String codBib, String serie, int codInv, Locale locale, String ticket)
	throws EJBException, ValidationException, DataException {
		InventarioVO rec = null;
		try {
			rec = this.inventario.getInventario(codPolo, codBib, serie, codInv, locale, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {


		} catch (DaoManagerException e) {


		}
		return rec;
	}

	public CollocazioneVO getCollocazione(int keyLoc, String ticket)
	throws EJBException, ValidationException, DataException, RemoteException {
		CollocazioneVO rec = null;
		try {
			rec = this.collocazione.getCollocazione(keyLoc, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		}
		return rec;
	}

	public CollocazioneDettaglioVO getCollocazioneDettaglio(int keyLoc, String ticket)
	throws EJBException, ApplicationException,	ValidationException, DataException {
		CollocazioneDettaglioVO rec = null;
		try {
			rec = this.collocazione.getCollocazioneDettaglio(keyLoc, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
			throw new EJBException(e);
		}
		return rec;
	}

	public InventarioDettaglioVO getInventarioDettaglio(String codPolo, String codBib, String serie, int codInv,
			Locale locale, String ticket)
	throws EJBException, ValidationException, RemoteException {
		InventarioDettaglioVO rec = null;
		try {
			rec = this.inventario.getInventarioDettaglio(codPolo, codBib, serie, codInv, locale, ticket);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		}
		return rec;
	}

	public DescrittoreBloccoVO getListaCollocazioniReticolo(String codPolo, String codBib, CollocazioneTitoloVO[] reticolo,
			String tipoOperazione, String ticket, int nRec)
	throws EJBException, ApplicationException, ValidationException, RemoteException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			 elenco = this.collocazione.getListaCollocazioniReticolo(codPolo, codBib, reticolo, tipoOperazione, ticket);
				blocco1 = this.saveBlocchi(ticket, elenco, nRec);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		}
		return blocco1;
	}

	public CollocazioneVO getCollocazione(CollocazioneVO recColl, String ticket)
	throws EJBException, ValidationException, RemoteException, DaoManagerException {
		CollocazioneVO rec = null;

		try {
			rec = this.collocazione.getCollocazione(recColl, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		}
		return rec;
	}

	public DatiBibliograficiCollocazioneVO getAnaliticaPerCollocazione(String bid, String bibliotecaOperante, String ticket)
	throws EJBException, DataException, ValidationException {
		DatiBibliograficiCollocazioneVO reticolo = null;
		try {
			reticolo = this.collocazione.getAnaliticaPerCollocazione(bid, bibliotecaOperante, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}
		return reticolo;
	}

//	public boolean updateCollocazione(CollocazioneDettaglioVO recColl, boolean datiMappaModificati, boolean collocazioneEsistente, String ticket)
//	throws EJBException, DataException, ValidationException {
//		boolean ret = false;
//
//		try {
//			ret = this.collocazione.updateCollocazione(recColl, datiMappaModificati, collocazioneEsistente, ticket);
//		} catch (DataException e) {
//			throw e;
//		} catch (ValidationException e) {
//			throw e;
//		} catch (Exception ve) {
//
//			logger.error("Errore nel bean", ve);
//		}
//		return ret;
//	}
	public boolean updateCollocazione(CollocazioneDettaglioVO recColl, boolean datiMappaModificati, boolean collocazioneEsistente, String ticket)
	throws EJBException, DataException, ValidationException {
		boolean ret = false;

		try {
			ret = this.collocazione.updateCollocazione(recColl, datiMappaModificati, collocazioneEsistente, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}
		return ret;
	}
	public DescrittoreBloccoVO getListaCollocazioniSezione(EsameCollocRicercaVO paramRicerca, String tipoRichiesta, String ticket, int nRec)
	throws EJBException, DataException, ValidationException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		DescrittoreBloccoNumCollVO blocco1bis = null;
		int n = 0;
		CodiceVO recNum = null;

		try {
			elenco = this.collocazione.getListaCollocazioniSezione(paramRicerca, tipoRichiesta, ticket);
			n = elenco.size() - 1;
			if (elenco.get(n) instanceof CodiceVO){
				recNum = (CodiceVO)elenco.get(n);
				elenco.remove(n);
			}
			if (paramRicerca.getOldIdLista() == null && paramRicerca.getUltimoBlocco() == 0){
				//check tipo oggetto
				DescrittoreBloccoInterceptor interceptor = (ValidazioneDati.first(elenco) instanceof CollocazioneUltKeyLocVO) ?
						new EsameCollocazioniBloccoInterceptor(ticket, this.collocazione) : null;
				blocco1 = saveBlocchi(ticket, elenco, nRec, interceptor);

			} else if (paramRicerca.getOldIdLista() != null && paramRicerca.getUltimoBlocco() > 0){
				blocco1 = appendBlocchi(ticket, elenco, nRec, paramRicerca.getOldIdLista(), paramRicerca.getUltimoBlocco());
			}
			if (recNum != null) {
				int num = recNum.getNumero();
				String ultColl = recNum.getCodice();
				String ultSpec = recNum.getTerzo();
				String ultOrdLoc = recNum.getQuarto();
				String ultKeyLoc = recNum.getDescrizione();
				blocco1bis = new DescrittoreBloccoNumCollVO(blocco1, num, ultColl, ultSpec, ultOrdLoc, ultKeyLoc);
				return blocco1bis;
			}

		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return blocco1;
	}

	public List getAltreBib(String bid, String codBib, String ticket)
	throws EJBException, DataException, ValidationException {
		List listaAltreBib = null;
		try {
			listaAltreBib = this.collocazione.getAltreBib(bid, codBib, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}
		return listaAltreBib;
	}

	public EsemplareDettaglioVO getEsemplareDettaglio(String codPolo, String codBib, String bid, int codDoc, String ticket)
	throws EJBException, ApplicationException, ValidationException, DataException, RemoteException {
		EsemplareDettaglioVO rec = null;
		try {
			rec = this.collocazione.getEsemplareDettaglio(codPolo, codBib, bid, codDoc, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		}
		return rec;
	}

	public DescrittoreBloccoVO getListaEsemplariReticolo(String codPolo, String codBib, CollocazioneTitoloVO[] reticolo, int nRec, String ticket)
	throws EJBException, ApplicationException, ValidationException, RemoteException {
		List listaEsemplari = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			listaEsemplari = this.collocazione.getListaEsemplariReticolo(codPolo, codBib, reticolo, ticket);
			blocco1 = this.saveBlocchi(ticket, listaEsemplari, nRec);
		} catch (ResourceNotFoundException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		}
		return blocco1;
	}

	public boolean insertEsemplare(EsemplareVO recEsempl, int keyLoc, String ticket)
	throws EJBException, ApplicationException, ValidationException, DataException {
		boolean ret = false;

		try {
			ret = this.collocazione.insertEsemplare(recEsempl, keyLoc, ticket);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}
		return ret;
	}

	public boolean updateEsemplare(EsemplareVO recEsempl, int keyLoc, String tipoOper, String ticket)
	throws EJBException, DataException, ValidationException{
		boolean ret = false;

		try {
			ret = this.collocazione.updateEsemplare(recEsempl, keyLoc, tipoOper, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}
		return ret;
	}

	//	titolo
	public List<TitoloVO> getTitolo(String bid, String ticket)
	throws EJBException, ValidationException, DataException {
		List<TitoloVO> lista = null;
		try {
			lista = this.documentoFisicoCommon.getTitolo(bid, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {


		}
		return lista;
	}
	//	chiave titolo e autore
	public String getChiaviTitoloAutore(String tipo, String bidVid, String ticket)
	throws EJBException, ValidationException, DataException {
		String chiave = null;
		try {
			chiave = this.inventario.getChiaviTitoloAutore(tipo, bidVid, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {


		}
		return chiave;
	}
	public boolean [] getDatiPosseduto(String codPolo, String codBib, String bid, String ticket)
	throws EJBException, DataException, ValidationException {
		boolean [] ret = {false, false, false, false, false};

		try {
			ret = this.collocazione.getDatiPosseduto(codPolo, codBib, bid, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (ValidationException e) {
			throw e;
		} catch (Exception ve) {

			logger.error("Errore nel bean", ve);
		}

		return ret;

	}
	public DescrittoreBloccoVO getListaEsemplariDiCollocazione(String codPolo, String codBib, String bibDoc, String ticket, int nRec)
	throws EJBException, DataException, ValidationException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.collocazione.getListaEsemplariDiCollocazione(codPolo, codBib, bibDoc, ticket);
			blocco1 = this.saveBlocchi(ticket, elenco, nRec);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}catch (RemoteException e) {


			throw new EJBException(e);
		}
		return blocco1;
	}
	public DescrittoreBloccoVO getListaCollocazioniTitolo(String codPolo, String codBib, String bid, String ticket, int nRec)
	throws EJBException, RemoteException, ValidationException, DataException {
		List collocazioni = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			collocazioni = this.collocazione.getListaCollocazioniTitolo(codPolo, codBib, bid, ticket);
//			if (inventari != null){
				blocco1 = this.saveBlocchi(ticket, collocazioni, nRec);
//				if (blocco1 == null)
//					throw new EJBException("Errore in generazione blocchi");
//			}
		}  catch (DataException e) {


			throw e;
		} catch (ValidationException e) {


			throw e;
		}catch (RemoteException e) {


			throw e;
		}
		return blocco1;
	}
	public DescrittoreBloccoVO getListaEsemplariTitolo(String codPolo, String codBib, String bid, String ticket, int nRec)
	throws EJBException, RemoteException, ValidationException, DataException {
		List esemplari = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			esemplari = this.collocazione.getListaEsemplariTitolo(codPolo, codBib, bid, ticket);
//			if (inventari != null){
				blocco1 = this.saveBlocchi(ticket, esemplari, nRec);
//				if (blocco1 == null)
//					throw new EJBException("Errore in generazione blocchi");
//			}
		}  catch (DataException e) {


			throw e;
		} catch (ValidationException e) {


			throw e;
		}catch (RemoteException e) {


			throw e;
		}
		return blocco1;
	}

	public DescrittoreBloccoVO getListaPossessori(String codPolo, String codBib, PossessoriRicercaVO possRic,String ticket , int nRec,GeneraChiave key)
	throws EJBException, RemoteException, ValidationException, DataException {
		List possessori = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			possessori = this.possessori.getListaPossessori(codPolo, codBib,possRic,nRec, key);
				blocco1 = this.saveBlocchi(ticket, possessori, nRec);
		}  catch (DataException e) {


			throw e;
		} catch (ValidationException e) {


			throw e;
		}catch (RemoteException e) {


			throw e;
		}
		return blocco1;
	}

	public DescrittoreBloccoVO getListaPossessori_1(String codPolo, String codBib, PossessoriRicercaVO possRic,String ticket , int nRec,GeneraChiave key)
	throws EJBException, RemoteException, ValidationException, DataException {
		List possessori = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			possessori = this.possessori.getListaPossessori_1(codPolo, codBib,possRic,nRec, key);
				blocco1 = this.saveBlocchi(ticket, possessori, nRec);
		}  catch (DataException e) {


			throw e;
		} catch (ValidationException e) {


			throw e;
		}catch (RemoteException e) {


			throw e;
		}
		return blocco1;
	}

	public String inserisciPossessori (PossessoriDettaglioVO possDett, String codPolo, String codBib , String ticket,GeneraChiave key)
	throws EJBException, ValidationException, RemoteException  {
		String ret = null  ;
		try {
			ret = this.possessori.inserisciPossessori(possDett, codPolo, codBib, ticket , key);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		}
//		catch (ValidationException e) {
//		//logger.error("Errore in getBibliteche",e);
//		throw e;
//		}
		return ret;
	}

	public List getEtichette(StampaEtichetteVO input, String tipoOperazione, String ticket)
	throws EJBException, RemoteException, ValidationException, DataException {
		List output = null;
		try {
			output = this.inventario.getEtichette(input, tipoOperazione, ticket);
		}  catch (DataException e) {


			throw e;
		} catch (ValidationException e) {


			throw e;
		}catch (RemoteException e) {


			throw e;
		}
		return output;
	}
	public DescrittoreBloccoVO getListaInventariPossessori(String codPolo, String codBib, String pid, String ticket, int nRec, EsameCollocRicercaVO paramRicerca)
	throws EJBException, RemoteException, ValidationException, DataException {

		DescrittoreBloccoNumCollVO blocco1bis = null;
		int n = 0;
		CodiceVO recNum = null;

		List inventari = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			inventari = this.inventario.getListaInventariPossessori(codPolo, codBib, pid, ticket, paramRicerca);

			n = inventari.size() - 1;
			if (inventari.get(n) instanceof CodiceVO){
				recNum = (CodiceVO)inventari.get(n);
				inventari.remove(n);
			}
			if (paramRicerca.getOldIdLista() == null && paramRicerca.getUltimoBlocco() == 0){
				blocco1 = saveBlocchi(ticket, inventari, nRec);
			}else if (paramRicerca.getOldIdLista() != null && paramRicerca.getUltimoBlocco() > 0){
				blocco1 = appendBlocchi(ticket, inventari, nRec, paramRicerca.getOldIdLista(), paramRicerca.getUltimoBlocco());
			}
			if (recNum != null){
				int num = recNum.getNumero();
				String ultCodSerie = recNum.getCodice();
				String ultCodInv = recNum.getTerzo();
				blocco1bis = new DescrittoreBloccoNumCollVO(blocco1, num, ultCodSerie, ultCodInv, null, null);
				return blocco1bis;
			}
		}  catch (DataException e) {


			throw e;
		} catch (ValidationException e) {


			throw e;
		}catch (RemoteException e) {


			throw e;
		}
		return blocco1;
	}

	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO creaRichiestaAnaliticoPossessoriPid (AreaDatiPassaggioInterrogazioneTitoloAnaliticaVO areaDatiPass, String polo ,String bib , String ticket)	throws RemoteException {

		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO ret  ;
		try {
			ret = this.possessori.creaRichiestaAnaliticoPossessoriPid(areaDatiPass, polo, bib, ticket);
		}catch (RemoteException e) {


			throw e;
		}
		return ret;
	}
	public String inserisciLegameAlPossessori (PossessoriDettaglioVO possDett, String codPolo, String codBib , String ticket,GeneraChiave key,String notaLegame,String tipoLegame)
	throws EJBException, ValidationException, RemoteException  {
		String ret = null  ;
		try {
			ret = this.possessori.inserisciLegameAlPossessori(possDett, codPolo, codBib, ticket , key, notaLegame, tipoLegame);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		}
//		catch (ValidationException e) {
//		//logger.error("Errore in getBibliteche",e);
//		throw e;
//		}
		return ret;
	}

	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO cancellaLegamePossessore (String pid_padre,String pid_figlio, String polo ,String bib , String ticket)	throws RemoteException {

		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO ret  ;
		try {
			ret = this.possessori.cancellaLegamePossessore(pid_padre,pid_figlio, polo, bib, ticket);
		}catch (RemoteException e) {


			throw e;
		}
		return ret;
	}
	public String modificaNotaLegame (String pid_padre,String pid_figlio, String notaLegame,String uteFirma)	throws RemoteException {

		String ret  ;
		try {
			ret = this.possessori.modificaNotaLegame(pid_padre,pid_figlio, notaLegame,uteFirma);
		}catch (RemoteException e) {


			throw e;
		}
		return ret;
	}
	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO scambioLegame (String pid_padre,String pid_figlio, String uteFirma)	throws RemoteException {

		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO ret  ;
		try {
			ret = this.possessori.scambioLegame(pid_padre,pid_figlio, uteFirma);
		}catch (RemoteException e) {


			throw e;
		}
		return ret;
	}
	public String modificaPossessori (PossessoriDettaglioVO possDett, String codPolo, String codBib , String ticket,GeneraChiave key)
	throws EJBException, ValidationException, RemoteException  {
		String ret = null  ;
		try {
			ret = this.possessori.modificaPossessori(possDett, codPolo, codBib, ticket , key);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		}
//		catch (ValidationException e) {
//		//logger.error("Errore in getBibliteche",e);
//		throw e;
//		}
		return ret;
	}

	public String fondiPossessori (PossessoriDettaglioVO possDett, String codPolo, String codBib , String ticket, GeneraChiave key)
	throws EJBException, ValidationException, RemoteException  {
		String ret = null  ;
		try {
			ret = this.possessori.fondiPossessori(possDett, codPolo, codBib, ticket, key);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		}

		return ret;
	}



	public String modificaLegameAlPossessori (PossessoriDettaglioVO possDett, String codPolo, String codBib , String ticket,GeneraChiave key,String notaLegame,String tipoLegame,String pidOrigine)
	throws EJBException, ValidationException, RemoteException  {
		String ret = null  ;
		try {
			ret = this.possessori.modificaLegameAlPossessori(possDett, codPolo, codBib, ticket , key, notaLegame, tipoLegame,pidOrigine);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		}
//		catch (ValidationException e) {
//		//logger.error("Errore in getBibliteche",e);
//		throw e;
//		}
		return ret;
	}
	public DescrittoreBloccoVO getListaPossessoriDiInventario(String codSerie, String codInv , String codBib,String codPolo , String ticket ,int nRec)
	throws EJBException, RemoteException, ValidationException, DataException {
		List possessori = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			possessori = this.possessori.getListaPossessoriDiInventario(codSerie, codInv, codBib, codPolo, ticket, nRec);
				blocco1 = this.saveBlocchi(ticket, possessori, nRec);
		}  catch (DataException e) {


			throw e;
		} catch (ValidationException e) {


			throw e;
		}catch (RemoteException e) {


			throw e;
		}
		return blocco1;
	}

	public String legamePossessoreInventario (PossessoriDiInventarioVO possInv)
	throws EJBException, ValidationException, RemoteException  {
		String ret = null  ;
		try {
			ret = this.possessori.legamePossessoreInventario(possInv);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		}
//		catch (ValidationException e) {
//		//logger.error("Errore in getBibliteche",e);
//		throw e;
//		}
		return ret;
	}

	// almaviva7 02/07/2008
	public String modificaLegamePossessoreInventario (PossessoriDiInventarioVO possInv)
	throws EJBException, ValidationException, RemoteException  {
		String ret = null  ;
		try {
			ret = this.possessori.modificaLegamePossessoreInventario(possInv);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		}
		return ret;
	}

	public AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO cancellaLegameInventario (String pid, String codiceInventario, String polo ,String bib , String firma, String ticket)	throws RemoteException {

		AreaDatiPassaggioInterrogazioneTitoloAnaliticaReturnVO ret  ;
		try {
			ret = this.possessori.cancellaLegameInventario(pid, codiceInventario, polo, bib, firma);
		}catch (RemoteException e) {


			throw e;
		}
		return ret;
	}

//modello default
	public ModelloDefaultVO getModelloDefault(String codPolo, String codBib, String ticket)
	throws EJBException, RemoteException, ValidationException, DataException {
		ModelloDefaultVO modello = null;
		try {
			modello = this.inventario.getModelloDefault(codPolo, codBib, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ResourceNotFoundException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}
		return modello;
	}

//	modelli etichette
	public DescrittoreBloccoVO getListaModelli(String codPolo, String codiceBiblioteca, String ticket, int nRec)
	throws EJBException, RemoteException, ValidationException, DataException {
		List elenco = null;
		DescrittoreBloccoVO blocco1 = null;
		try {
			elenco = this.inventario.getListaModelli(codPolo, codiceBiblioteca, ticket);
			blocco1 = this.saveBlocchi(ticket, elenco, nRec);
		} catch (DataException e) {
			throw (e);
		} catch (ValidationException e) {
			throw (e);
		} catch (RemoteException e) {
			throw e;
		}
		return blocco1;
	}
	public boolean insertModelloDefault(ModelloDefaultVO modello, String ticket)
	throws EJBException, RemoteException, ValidationException, DataException {
		boolean ret = false;

		try {
			ret = this.inventario.insertModelloDefault(modello, ticket);
		} catch (DataException e) {
			throw (e);
		} catch (ValidationException e) {
			throw (e);
		} catch (RemoteException e) {
			throw e;
		}

		return ret;

	}

	public ModelloEtichetteVO getModello(String codPolo, String codBib, String codModello, String ticket)
	throws EJBException, RemoteException, ValidationException, DataException {
		ModelloEtichetteVO rec = null;

		try {
			rec = this.inventario.getModello(codPolo, codBib, codModello, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (Exception ve) {

		}
		return rec;
	}

	public boolean deleteModello(ModelloEtichetteVO modello, String ticket)
	throws EJBException, RemoteException, ValidationException, DataException {
		boolean ret = false;

		try {
			ret = this.inventario.deleteModello(modello, ticket);
		} catch (DataException e) {
			throw (e);
		} catch (ValidationException e) {
			throw (e);
		} catch (RemoteException e) {
			throw e;
		}
		return ret;
	}
	public boolean insertModello(ModelloEtichetteVO modello, String ticket)
	throws EJBException, RemoteException, ValidationException, DataException {
		boolean ret = false;

		try {
			ret = this.inventario.insertModello(modello, ticket);
		} catch (DataException e) {
			throw (e);
		} catch (ValidationException e) {
			throw (e);
		} catch (RemoteException e) {
			throw e;
		}

		return ret;

	}

	public boolean updateModello(ModelloEtichetteVO modello, String ticket)
	throws EJBException, RemoteException, ValidationException, DataException {
		boolean ret = false;

		try {
			ret = this.inventario.updateModello(modello, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (Exception ve) {

		}
		return ret;
	}

	public List getListaInventariCollocatiDa(String codPolo, String codBib, String codUtente, String dataDa, String dataA, String ticket)
	throws EJBException, RemoteException, ValidationException {
		List elenco = null;
		try {
			elenco = this.inventario.getListaInventariCollocatiDa(codPolo, codBib, codUtente, dataDa, dataA, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ResourceNotFoundException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}
		return elenco;
	}

	public boolean getSerieNumeroCarico(String codPolo, String codBib, String codSerie, String numCarico, String ticket)
	throws EJBException, RemoteException, ValidationException {
		boolean num = false;
		try {
			num = this.inventario.getSerieNumeroCarico(codPolo, codBib, codSerie, numCarico, ticket);
		} catch (DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ResourceNotFoundException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}
		return num;
	}


} // End GestioneDocumentoFisicoBean
