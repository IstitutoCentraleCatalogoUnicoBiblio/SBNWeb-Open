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
package it.finsiel.gateway.local.impl;

import it.finsiel.gateway.exception.SbnMarcDiagnosticoException;
import it.finsiel.gateway.intf.KeyAutore;
import it.finsiel.gateway.intf.KeySoggetto;
import it.finsiel.gateway.local.SbnMarcLocalGateway;
import it.finsiel.sbn.exception.ApplicationException;
import it.finsiel.sbn.polo.ejb.Profiler;
import it.finsiel.sbn.polo.ejb.factory.SbnMarcEJBFactory;
import it.finsiel.sbn.polo.exception.EccezioneIccu;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.exception.EccezioneSbnMessage;
import it.finsiel.sbn.polo.exception.EccezioneXml;
import it.finsiel.sbn.polo.factoring.base.ChiaviAutore;
import it.finsiel.sbn.polo.factoring.base.ChiaviSoggetto;
import it.finsiel.sbn.polo.factoring.base.CostruttoreIsbd;
import it.finsiel.sbn.polo.factoring.base.FormatoErrore;
import it.finsiel.sbn.polo.factoring.base.TipiAutore;
import it.finsiel.sbn.polo.factoring.transactionmaker.Factoring;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.util.MarshallingUtil;
import it.finsiel.sbn.util.RandomIdGenerator;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;

import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.ValidationException;


@Stateless(name="SbnMarcLocalGateway")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SbnMarcLocalGatewayBean implements SbnMarcLocalGateway {

	private static Logger log = Logger.getLogger("sbnmarcPolo");

	@Resource
	private SessionContext ctx;

	public SBNMarc execute(SBNMarc sbnmarc) throws Exception {

		SBNMarc result = null;
		Factoring main_factoring = null;

		SbnUserType user = FormatoErrore.FAKE_SBN_USER;

		try {
			log.debug("gateway locale per richiesta: " + sbnmarc);
			if (sbnmarc == null)
				throw new Exception();

			sbnmarc.validate();

			user = sbnmarc.getSbnUser(); //utente chiamante

			if (log.isDebugEnabled()) {
				String xml = MarshallingUtil.marshal(sbnmarc);
				log.debug("XML INPUT:  " + xml);
			}

			main_factoring = Factoring.getFactoring(sbnmarc);
			main_factoring.eseguiTransazione();
			main_factoring.proseguiTransazione();
			result = main_factoring.getSBNMarcResult();

			result.validate();

			if (log.isDebugEnabled()) {
				String xml = MarshallingUtil.marshal(result);
				log.debug("XML OUTPUT: " + xml);
			}

		} catch (EccezioneIccu e) {
			//almaviva5_20120209 fix errore 3336 (ripristino forma rinvio autore/luogo)
			if (e.getErrorID() != 3336)
				ctx.setRollbackOnly();
			// Leggo comunque la response
			log.info("Messaggio informativo: " + main_factoring.getClass() + "; ecc: " + e + "; " + e.getMessaggio());
			log.error(String.format("incidentId: %s; stacktrace: ", e.getIncidentId() ), e);
			if (main_factoring != null && main_factoring.getSBNMarcResult() != null)
				result = main_factoring.getSBNMarcResult();
			else
				result = FormatoErrore.buildMessaggioErrore(e, user);

		} catch(EccezioneSbnMessage e) {
			//almaviva5_20120209 fix errore 3336 (ripristino forma rinvio autore/luogo)
			if (e.getErrorID() != 3336)
				ctx.setRollbackOnly();
			log.info("Eccezione durante azione di Factoring: " + main_factoring.getClass() + "; ecc: " + e + "; " + e.getMessaggio());
			log.error("stacktrace: ", e);
			if (main_factoring != null && main_factoring.getSBNMarcResult() != null)
				result = main_factoring.getSBNMarcResult();
			else
				result = FormatoErrore.buildMessaggioDiagnostico(e, user);

		} catch (ValidationException e) {
			ctx.setRollbackOnly();
			String incidentId = RandomIdGenerator.getId();
			log.error(String.format("Errore validation [incidentId: %s]: ", incidentId), e);
			result = FormatoErrore.buildMessaggioErrore(101, user, incidentId);

		} catch (Exception e) {
			ctx.setRollbackOnly();
			log.info("Eccezione durante azione di Factoring: " +  main_factoring.getClass());
			String incidentId = RandomIdGenerator.getId();
			log.error(String.format("Errore transaction [incidentId: %s]: ", incidentId), e);
			// Errore interno durante l'elaborazione del Factoring.
			result = FormatoErrore.buildMessaggioErrore(56, user, incidentId);
		}

		return result;
	}

	//almaviva5_20131104
	public KeyAutore getChiaveAutore(DatiElementoType dati, String tipoAutInput)
			throws SbnMarcDiagnosticoException {
		ChiaviAutore key =  null;
		try {
			if (!ValidazioneDati.isFilled(tipoAutInput)) {
				 key = new ChiaviAutore("", dati.getT001() );

			} else {
				//almaviva5_20140710
				Tb_autore autore = TipiAutore.controllaNomeAutore(dati, tipoAutInput);
				String tipoAutCalcolato = autore.getTP_NOME_AUT();
				if (!ValidazioneDati.equals(tipoAutInput, tipoAutCalcolato))
					throw new EccezioneSbnDiagnostico(3262); //tipo nome fornito non valido

				key = new ChiaviAutore(tipoAutInput, autore.getDS_NOME_AUT() );
			}

			if (!key.calcolaChiavi())
				throw new EccezioneSbnDiagnostico(3043,	"Errore nella generazione delle chiavi");

			return key;

		} catch (EccezioneSbnDiagnostico e) {
			throw new SbnMarcDiagnosticoException(e.getErrorID(), e.getMessaggio());
		}
	}

	private enum TipoAzioneServlet {
		init,
		reloadProfili,
		reloadProfilo,
		reloadCodici,
		rimuoviProfili,
		rimuoviProfilo,
		buildTime;
	}

	public Serializable service(Serializable... payload) throws Exception {
		try {
			String azione = (String) payload[0];
			TipoAzioneServlet tipoAzione = TipoAzioneServlet.valueOf(azione);
			log.debug("gateway locale per richiesta di servizio: " + tipoAzione);
			Profiler profiler = SbnMarcEJBFactory.getFactory().getProfiler();
			switch (tipoAzione) {
			case init:
				profiler.init();
				break;
			case reloadProfili:
				profiler.reloadProfili();
				break;
			case reloadProfilo:
				profiler.reloadProfilo((String) payload[1]);
				break;
			case reloadCodici:
				profiler.reloadCodici();
				break;
			case rimuoviProfili:
				profiler.rimuoviProfili(payload.length > 1 ? (String) payload[1] : null);
				break;
			case rimuoviProfilo:
				profiler.rimuoviProfilo((String) payload[1]);
				break;
			case buildTime:
				return getDeployBuildTime();

			}
		} catch (Exception e) {
			log.error("", e);
		}
		return null;

	}

	private static final String SBNMARC_BUILD_TIME = "SBNMARC_BUILD_TIME";

	private String getDeployBuildTime() {
		try {
			Properties props = new Properties() ;
			props.load(this.getClass().getResourceAsStream("/sbnmarc_build_time.properties"));
			return props.getProperty(SBNMARC_BUILD_TIME);

		} catch (Exception e) {	}

		return (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(new Date());
	}

	private SBNMarc estraiSbnmarc(String param) throws EccezioneXml {
		SBNMarc sbnmarcObj;
		try {
			sbnmarcObj = SBNMarc.unmarshalSBNMarc(new StringReader(param));

		} catch (org.exolab.castor.xml.ValidationException e) {
			log.error("Errore di castor validator: " + e.getMessage());
			throw new EccezioneXml(2001, e);
		} catch (org.exolab.castor.xml.MarshalException e) {
			log.error("Errore di marshalling: " + e.getMessage());
			EccezioneXml ecc = new EccezioneXml(2002, e);
			ecc.appendMessaggio(": " + e.getMessage());
			throw ecc;
		} catch (Exception e) {
			log.error("Errore nel unmarshalling: " + e.getMessage());
			if (e instanceof org.xml.sax.SAXException && e.toString().indexOf("Parsing Error : Character reference ") > 0) {
				throw new EccezioneXml(3914, e);
			} else {
				throw new EccezioneXml(101, e);
			}
		}
		return sbnmarcObj;
	}

	public String execute(String xml_text) throws Exception {
		SBNMarc sbnmarc = null;
		try {
			log.debug("XML INPUT: " + xml_text);
            sbnmarc = estraiSbnmarc(xml_text);

        } catch (EccezioneXml exml) {
            log.error("unmarshal PARSER ERROR: In root object - " + exml);

            ctx.setRollbackOnly();
            throw new ApplicationException(FormatoErrore.preparaMessaggioErrore(exml));
        } catch (Exception e) {
            log.error("Errore durante l'elaborazione", e);
            // da vedere cosa ritornare
        }

		String xml_response = "";
		Factoring main_factoring = null;
        try {
			main_factoring = Factoring.getFactoring(sbnmarc);
			main_factoring.eseguiTransazione();
			main_factoring.proseguiTransazione();
			xml_response = main_factoring.getXMLResult();

			log.debug("XML OUTPUT: " + xml_response);

		} catch (EccezioneIccu e) {
			//almaviva5_20120209 fix errore 3336 (ripristino forma rinvio autore/luogo)
			if (e.getErrorID() != 3336)
				ctx.setRollbackOnly();
			// Leggo comunque la response
			log.debug("Messaggio informativo: " + main_factoring.getClass() + "; ecc: " + e + "; " + e.getMessaggio());
			log.debug("stacktrace: ", e);
			if (main_factoring != null && main_factoring.getXMLResult() != null)
				xml_response = main_factoring.getXMLResult();
			else
				xml_response = FormatoErrore.preparaMessaggioErrore(e, sbnmarc.getSbnUser());

			throw new ApplicationException(xml_response);

		}catch(EccezioneSbnMessage e) {
			//almaviva5_20120209 fix errore 3336 (ripristino forma rinvio autore/luogo)
			if (e.getErrorID() != 3336)
				ctx.setRollbackOnly();
			log.debug("Eccezione durante azione di Factoring: " + main_factoring.getClass() + "; ecc: " + e + "; " + e.getMessaggio());
			log.debug("stacktrace: ", e);
			if (main_factoring != null && main_factoring.getXMLResult() != null)
				xml_response = main_factoring.getXMLResult();
			else
				xml_response = FormatoErrore.preparaMessaggioDiagnostico(e, sbnmarc.getSbnUser());

		} catch (Exception e) {
			log.debug("Eccezione durante azione di Factoring: " +  main_factoring.getClass());
			log.error("Errore transaction: " + e, e);
			// Errore interno durante l'elaborazione del Factoring.
			xml_response = FormatoErrore.preparaMessaggioErrore(56, sbnmarc.getSbnUser());

			ctx.setRollbackOnly();
			throw new ApplicationException(xml_response);
		}

		return xml_response;
	}

	public String definisciIsbdTitUniMusicale(String a_929, String b_929,
			String c_929, String e_929, String f_929, String g_929,
			String h_929, String i_929, String[] a_928, String b_928,
			String c_928) throws SbnMarcDiagnosticoException {

		//almaviva5_20140711 #5606
		CostruttoreIsbd isbd = new CostruttoreIsbd();
		try {
			isbd.definisciISBDtitUniMusicale(new Tb_titolo(), true, a_929,
					b_929, c_929, e_929, f_929, g_929, h_929, i_929, a_928,
					b_928, c_928);
		} catch (EccezioneSbnDiagnostico e) {
			throw new SbnMarcDiagnosticoException(e.getErrorID(), e.getMessaggio());
		}

		return isbd.getIsbd();
	}

	public KeySoggetto getChiaveSoggetto(String codSoggettario, SbnEdizioneSoggettario edizione, String testoSoggetto)
			throws SbnMarcDiagnosticoException {
		if (!ValidazioneDati.isFilled(testoSoggetto))
			return null;

		try {
			return ChiaviSoggetto.build(codSoggettario, edizione, testoSoggetto);

		} catch (EccezioneSbnDiagnostico e) {
			throw new SbnMarcDiagnosticoException(e.getErrorID(), e.getMessaggio());
		}

	}

}
