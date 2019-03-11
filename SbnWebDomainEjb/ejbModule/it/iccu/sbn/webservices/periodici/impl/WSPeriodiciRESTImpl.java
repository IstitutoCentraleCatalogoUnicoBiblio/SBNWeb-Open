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
package it.iccu.sbn.webservices.periodici.impl;

import it.iccu.sbn.batch.unimarc.Marc950;
import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.domain.periodici.PeriodiciSBN;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieCollocazioneVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieInventarioVO;
import it.iccu.sbn.ejb.vo.periodici.esame.SerieTitoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.KardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.periodici.kardex.RicercaKardexPeriodicoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.sip2.SbnSIP2Ticket;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.rfid.InventarioRFIDParser;
import it.iccu.sbn.vo.custom.periodici.FascicoloDecorator;
import it.iccu.sbn.vo.custom.periodici.KardexPeriodicoOpacVO;
import it.iccu.sbn.vo.xml.binding.sbnwebws.CollocazioneType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.DisponibilitaType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.DocumentoType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.EsitoType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.InventarioType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.KardexType;
import it.iccu.sbn.vo.xml.binding.sbnwebws.SbnwebType;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloDisponibilitaVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.webservices.WSRESTBaseImpl;
import it.iccu.sbn.webservices.periodici.WSPeriodiciREST;

import java.net.InetAddress;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;



public class WSPeriodiciRESTImpl extends WSRESTBaseImpl implements WSPeriodiciREST {

	private static Logger log = Logger.getLogger(WSPeriodiciREST.class);

	private static PeriodiciSBN periodiciSBN;
	private static AmministrazionePolo amministrazionePolo;
	private static Servizi servizi;

	private AmministrazionePolo getPolo() throws Exception {

		if (amministrazionePolo != null)
			return amministrazionePolo;

		amministrazionePolo = DomainEJBFactory.getInstance().getPolo();

		return amministrazionePolo;
	}

	private PeriodiciSBN getPeriodici() {
		try {
			if (periodiciSBN != null)
				return periodiciSBN;

			periodiciSBN = DomainEJBFactory.getInstance().getPeriodici();
			return periodiciSBN;

		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

	private Servizi getServizi() {
		try {
			if (servizi != null)
				return servizi;

			servizi = DomainEJBFactory.getInstance().getServizi();
			return servizi;

		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

	public Response getKardex(HttpServletRequest request, String cd_bib, String idDoc, UriInfo info) throws Exception {

		try {
			addClient(request);
			if (!ValidazioneDati.leggiXID(idDoc))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "bid");

			BibliotecaVO bib = new BibliotecaVO();
			String codBib = ValidazioneDati.fillLeft(cd_bib, ' ', 3).toUpperCase();
			bib.setCod_bib(codBib);
			String codPolo = getPolo().getInfoPolo().getCd_polo();
			bib.setCod_polo(codPolo);

			RicercaKardexPeriodicoVO<FascicoloVO> richiesta = new RicercaKardexPeriodicoVO<FascicoloVO>();
			richiesta.setBiblioteca(bib);
			SerieTitoloVO st = new SerieTitoloVO();
			st.setBid(idDoc);
			richiesta.setOggettoRicerca(st);
			richiesta.setComparator(ValidazioneDati.invertiComparatore(FascicoloDecorator.ORDINAMENTO_FASCICOLO));

			MultivaluedMap<String, String> params = info.getQueryParameters();
			if (params.containsKey("from"))
				richiesta.setDataFrom(ValidazioneDati.first(params.get("from")));
			if (params.containsKey("to"))
				richiesta.setDataTo(ValidazioneDati.first(params.get("to")));

			String ticket = SbnSIP2Ticket.getUtenteSIP2Ticket(codPolo, codBib, InetAddress.getLocalHost());
			CommandInvokeVO command = CommandInvokeVO.build(ticket, CommandType.PER_KARDEX_PERIODICO_OPAC, richiesta);

			CommandResultVO result = getPeriodici().invoke(command);
			result.throwError();

			KardexPeriodicoVO kp = (KardexPeriodicoVO) result.getResult();
			List<FascicoloVO> fascicoli = kp.getFascicoli();

			SbnwebType sbnweb = new SbnwebType();
			KardexType k = new KardexType();
			EsitoType esito = new EsitoType();
			esito.setReturnCode(0);
			esito.setMessage("OK");
			esito.setNumeroRighe(fascicoli.size());

			sbnweb.setEsito(esito);
			sbnweb.setKardex(k);
			DocumentoType doc = new DocumentoType();
			doc.setBid(kp.getBid());
			doc.setIsbd(kp.getIntestazione().getDescrizionePeriodico() );
			sbnweb.setDocumento(doc);

			for (FascicoloVO f : fascicoli)
				k.getFascicolo().add(ConversioneHibernateVO.toXML().fascicolo((FascicoloDecorator)f));

			return Response.ok(sbnweb).build();

		} catch (SbnBaseException e) {
			return Response.ok().entity(ConversioneHibernateVO.toXML().formattaErrore(e)).build();

		} catch (Exception e) {
			throw new WebApplicationException(e);

		} finally {
			removeClient();
		}
	}

	public Response getKardexTitolo(HttpServletRequest request, String cd_bib, String idDoc, UriInfo info) throws Exception {

		try {
			addClient(request);
			if (!ValidazioneDati.leggiXID(idDoc))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "bid");

			BibliotecaVO bib = new BibliotecaVO();
			String codBib = ValidazioneDati.fillLeft(cd_bib, ' ', 3).toUpperCase();
			bib.setCod_bib(codBib);
			String codPolo = getPolo().getInfoPolo().getCd_polo();
			bib.setCod_polo(codPolo);

			RicercaKardexPeriodicoVO<FascicoloVO> richiesta = new RicercaKardexPeriodicoVO<FascicoloVO>();
			richiesta.setBiblioteca(bib);
			SerieTitoloVO st = new SerieTitoloVO();
			st.setBid(idDoc);
			richiesta.setOggettoRicerca(st);
			richiesta.setComparator(ValidazioneDati.invertiComparatore(FascicoloDecorator.ORDINAMENTO_FASCICOLO));

			String ticket = SbnSIP2Ticket.getUtenteSIP2Ticket(codPolo, codBib, InetAddress.getLocalHost());
			CommandInvokeVO command = CommandInvokeVO.build(ticket, CommandType.PER_KARDEX_PERIODICO_OPAC, richiesta);

			CommandResultVO result = getPeriodici().invoke(command);
			result.throwError();

			KardexPeriodicoOpacVO kp = (KardexPeriodicoOpacVO) result.getResult();
			List<InventarioType> inventari = kp.getInventari();
			int size = ValidazioneDati.size(inventari);

			//if (size < 1)
			//	throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);

			SbnwebType sbn = new SbnwebType();
			EsitoType esito = new EsitoType();
			esito.setReturnCode(0);
			esito.setNumeroRighe(size);
			esito.setMessage("OK");
			sbn.setEsito(esito);
			KardexType k = new KardexType();

			DocumentoType doc = new DocumentoType();
			doc.setBid(kp.getBid());
			doc.setIsbd(kp.getIntestazione().getDescrizionePeriodico() );
			sbn.setDocumento(doc);
			k.getInventario().addAll(inventari);

			sbn.setKardex(k);

			return Response.ok(sbn).build();

		} catch (SbnBaseException e) {
			return Response.ok().entity(ConversioneHibernateVO.toXML().formattaErrore(e)).build();

		} catch (Exception e) {
			throw new WebApplicationException(e);

		} finally {
			removeClient();
		}
	}

	public Response getKardexCollocazione(HttpServletRequest request, String cd_bib, String idDoc, String tag950, UriInfo info) throws Exception {

		try {
			addClient(request);
			if (!ValidazioneDati.leggiXID(idDoc))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "bid");

			Marc950 m950 = Marc950.parse(idDoc, tag950);
			if (m950 == null)
				throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "id");

			BibliotecaVO bib = new BibliotecaVO();
			String codBib = ValidazioneDati.fillLeft(cd_bib, ' ', 3).toUpperCase();
			bib.setCod_bib(codBib);
			String codPolo = getPolo().getInfoPolo().getCd_polo();
			bib.setCod_polo(codPolo);

			RicercaKardexPeriodicoVO<FascicoloVO> richiesta = new RicercaKardexPeriodicoVO<FascicoloVO>();
			richiesta.setBiblioteca(bib);
			SerieCollocazioneVO coll = new SerieCollocazioneVO();
			coll.setBid(idDoc);
			coll.setCodSez(m950.getCodSez(0));
			coll.setCd_loc(m950.getCodLoc(0));
			coll.setSpec_loc(m950.getSpecLoc(0));

			richiesta.setOggettoRicerca(coll);
			richiesta.setComparator(ValidazioneDati.invertiComparatore(FascicoloDecorator.ORDINAMENTO_FASCICOLO));

			MultivaluedMap<String, String> params = info.getQueryParameters();
			if (params.containsKey("from"))
				richiesta.setDataFrom(ValidazioneDati.first(params.get("from")));
			if (params.containsKey("to"))
				richiesta.setDataTo(ValidazioneDati.first(params.get("to")));

			String ticket = SbnSIP2Ticket.getUtenteSIP2Ticket(codPolo, codBib, InetAddress.getLocalHost());
			CommandInvokeVO command = CommandInvokeVO.build(ticket, CommandType.PER_KARDEX_PERIODICO_OPAC, richiesta);

			CommandResultVO result = getPeriodici().invoke(command);
			result.throwError();

			KardexPeriodicoOpacVO kp = (KardexPeriodicoOpacVO) result.getResult();
			List<InventarioType> inventari = kp.getInventari();
			int size = ValidazioneDati.size(inventari);

			//if (size < 1)
			//	throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);

			SbnwebType sbn = new SbnwebType();
			EsitoType esito = new EsitoType();
			esito.setReturnCode(0);
			esito.setNumeroRighe(size);
			esito.setMessage("OK");
			sbn.setEsito(esito);
			KardexType k = new KardexType();
			CollocazioneType c = kp.getCollocazione();

			DocumentoType doc = new DocumentoType();
			doc.setBid(kp.getBid());
			doc.setIsbd(kp.getIntestazione().getDescrizionePeriodico() );
			c.setDocumento(doc);
			c.getInventario().addAll(inventari);

			k.setCollocazione(c);
			sbn.setKardex(k);

			return Response.ok(sbn).build();

		} catch (SbnBaseException e) {
			return Response.ok().entity(ConversioneHibernateVO.toXML().formattaErrore(e)).build();

		} catch (Exception e) {
			throw new WebApplicationException(e);

		} finally {
			removeClient();
		}
	}

	public Response getKardexInventario(HttpServletRequest request, String cd_bib, String idDoc, String kinv,
			UriInfo info, boolean withDisponibilita) throws Exception {
		try {
			addClient(request);
			if (!ValidazioneDati.leggiXID(idDoc))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "bid");

			BibliotecaVO bib = new BibliotecaVO();
			String codBib = ValidazioneDati.fillLeft(cd_bib, ' ', 3).toUpperCase();
			bib.setCod_bib(codBib);
			String codPolo = getPolo().getInfoPolo().getCd_polo();
			bib.setCod_polo(codPolo);

			RicercaKardexPeriodicoVO<FascicoloVO> richiesta = new RicercaKardexPeriodicoVO<FascicoloVO>();
			richiesta.setBiblioteca(bib);
			InventarioVO inv = InventarioRFIDParser.parse(kinv);
			SerieInventarioVO si = new SerieInventarioVO(inv);
			si.setBid(idDoc);
			richiesta.setOggettoRicerca(si);
			richiesta.setComparator(ValidazioneDati.invertiComparatore(FascicoloDecorator.ORDINAMENTO_FASCICOLO));

			MultivaluedMap<String, String> params = info.getQueryParameters();
			if (params.containsKey("from"))
				richiesta.setDataFrom(ValidazioneDati.first(params.get("from")));
			if (params.containsKey("to"))
				richiesta.setDataTo(ValidazioneDati.first(params.get("to")));

			String ticket = SbnSIP2Ticket.getUtenteSIP2Ticket(codPolo, codBib, InetAddress.getLocalHost());
			CommandInvokeVO command = CommandInvokeVO.build(ticket, CommandType.PER_KARDEX_PERIODICO_OPAC, richiesta);
			CommandResultVO result = getPeriodici().invoke(command);
			result.throwError();

			KardexPeriodicoOpacVO kpo = (KardexPeriodicoOpacVO) result.getResult();
			List<FascicoloVO> fascicoli = ValidazioneDati.coalesce(kpo.getFascicoli(), ValidazioneDati.emptyList(FascicoloVO.class) );

			SbnwebType sbn = new SbnwebType();
			KardexType k = new KardexType();
			EsitoType esito = new EsitoType();
			esito.setReturnCode(0);
			esito.setMessage("OK");
			esito.setNumeroRighe(fascicoli.size());
			sbn.setEsito(esito);

			InventarioType i = kpo.getInventario();

			for (FascicoloVO f : fascicoli)
				i.getFascicolo().add(ConversioneHibernateVO.toXML().fascicolo((FascicoloDecorator)f));

			sbn.setKardex(k);
			DocumentoType doc = new DocumentoType();
			doc.setBid(kpo.getBid());
			doc.setIsbd(kpo.getIntestazione().getDescrizionePeriodico() );
			i.setDocumento(doc);
			k.getInventario().add(i);

			//almaviva5_20170607
			if (withDisponibilita) {
				MovimentoVO mov = new MovimentoVO();
				mov.setCodPolo(codPolo);
				mov.setCodBibOperante(codBib);
				mov.setCodBibInv(codBib);
				mov.setCodSerieInv(i.getSerie());
				mov.setCodInvenInv(i.getNumero() + "");
				mov.setDataInizioPrev(DaoManager.now());

				ControlloDisponibilitaVO cdvo = new ControlloDisponibilitaVO(mov, false);
				cdvo.setNoPrenotazione(true);

				CommandInvokeVO cmd = CommandInvokeVO.build(ticket, CommandType.SRV_CONTROLLO_DISPONIBILITA_WS, cdvo, false);
				CommandResultVO response = getServizi().invoke(cmd);
				response.throwError();

				DisponibilitaType dtype = (DisponibilitaType) response.getResult();
				i.setDisponibilita(dtype);

			}

			return Response.ok(sbn).build();

		} catch (SbnBaseException e) {
			return Response.ok().entity(ConversioneHibernateVO.toXML().formattaErrore(e)).build();

		} catch (Exception e) {
			throw new WebApplicationException(e);

		} finally {
			removeClient();
		}
	}

}
