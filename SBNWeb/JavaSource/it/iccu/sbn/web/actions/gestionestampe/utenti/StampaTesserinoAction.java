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
package it.iccu.sbn.web.actions.gestionestampe.utenti;

import it.iccu.sbn.command.CommandType;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionestampe.CartellinoVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.servizi.occupazioni.OccupazioneVO;
import it.iccu.sbn.ejb.vo.servizi.spectitolostudio.SpecTitoloStudioVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.AnagrafeVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.actionforms.gestionestampe.utenti.StampaTesserinoForm;
import it.iccu.sbn.web.actions.gestionestampe.ReportAction;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StampaTesserinoAction extends ReportAction {

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		StampaTesserinoForm stampaTesserinoForm = ((StampaTesserinoForm) form);

		stampaTesserinoForm.setTipoFormato(TipoStampa.PDF.name());
		return mapping.getInputForward();
	}

	private String cercaDescrAteneo(String cod) throws Exception {
		return CodiciProvider.cercaDescrizioneCodice(cod,
				CodiciType.CODICE_ATENEI, CodiciRicercaType.RICERCA_CODICE_SBN);
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		StampaTesserinoForm currentForm = ((StampaTesserinoForm) form);
		try {
			String tipoFormato = currentForm.getTipoFormato();
			String fileJrxml = "cartellino_template.jrxml";

			UtenteBibliotecaVO utente = (UtenteBibliotecaVO) request.getSession().getAttribute("UtenteBibliotecaVO");

			CartellinoVO cartellino = new CartellinoVO();
			// cartellino.setBiblioteca(utente.getBibliopolo().getCodBibXUteBib());
			cartellino.setBiblioteca(utente.getDescrBiblioteca());
			cartellino.setTessera(utente.getCodiceUtente().trim()); // rox
			cartellino.setNomeCognome(utente.getCognomeNome());
			AnagrafeVO anagrafe = utente.getAnagrafe();
			cartellino.setDataNascita(anagrafe.getDataNascita());
			cartellino.setLuogoNascita(anagrafe.getLuogoNascita());
			cartellino.setCapDomicilio(anagrafe.getDomicilio().getCap());
			cartellino.setCapResidenza(anagrafe.getResidenza().getCap());
			cartellino.setCittaDomicilio(anagrafe.getDomicilio().getCitta());
			cartellino.setCittaResidenza(anagrafe.getResidenza().getCitta());
			cartellino.setProvinciaDomicilio(anagrafe.getDomicilio().getProvincia());
			cartellino.setProvinciaResidenza(anagrafe.getResidenza().getProvincia());
			cartellino.setIndirizzoDomicilio(anagrafe.getDomicilio().getVia());
			cartellino.setIndirizzoResidenza(anagrafe.getResidenza().getVia());
			cartellino.setTelefonoDomicilio(anagrafe.getDomicilio().getTelefono());
			cartellino.setNazioneResidenza(anagrafe.getResidenza().getNazionalita());
			cartellino.setTipoAutorizzazione(utente.getAutorizzazioni().getAutorizzazione());
			cartellino.setDataAutorizzazione(utente.getBibliopolo().getInizioAuto());
			cartellino.setScadenzaAutorizzazione(utente.getBibliopolo().getFineAuto());
			cartellino.setDataIscrizione(utente.getBibliopolo().getPoloDataRegistrazione());
			cartellino.setDocRilasciatoDa(utente.getDocumento().get(0).getAutRilascio());
			cartellino.setNumeroDocumento(utente.getDocumento().get(0).getNumero());
			cartellino.setEmail(anagrafe.getPostaElettronica());
			cartellino.setTelefonoResidenza(anagrafe.getResidenza().getTelefono());
			if (utente.getProfessione() != null) {
				String ateneo = utente.getProfessione().getAteneo();
				cartellino.setAteneo(ateneo);
				if (ValidazioneDati.isFilled(ateneo) )
					cartellino.setAteneo(this.cercaDescrAteneo(ateneo));

			}
			cartellino.setMatricola(utente.getProfessione().getMatricola());

			// ateneo e matr da professioni
			// tlefono(uteAna.anagrafe.residenza.telefon) cellu
			// (uteAna.anagrafe.domicilio.telefono) email da postaelettronica di
			// anagrafe
			// descrizione autorizzazione e descrizione biblioteca

			// DOCUMENTO
			cartellino.setTipoDocumento("");
			TB_CODICI cod = CodiciProvider.cercaCodice(utente
					.getDocumento().get(0).getDocumento(),
					CodiciType.CODICE_TIPO_DOCUMENTO_DI_RICONOSCIMENTO,
					CodiciRicercaType.RICERCA_CODICE_SBN);

			if (cod != null)
				cartellino.setTipoDocumento(cod.getDs_tabella());

			ServiziDelegate delegate = ServiziDelegate.getInstance(request);

			// OCCUPAZIONE
			cartellino.setOccupazione("");
			OccupazioneVO occupazione = null;
			if (utente.getProfessione().getIdOccupazione() != null
					&& Integer.parseInt(utente.getProfessione()
							.getIdOccupazione()) > 0) {
				// almaviva5_20101206 #4043 #4044
				Integer idOcc = new Integer(utente.getProfessione()
						.getIdOccupazione());
				occupazione = (OccupazioneVO) delegate.invoke(
						CommandType.SRV_OCCUPAZIONE_BY_ID, idOcc).getResult();
				if (occupazione != null) // dovrebbe essere =1
					cartellino.setOccupazione(occupazione.getDesOccupazione());

			}

			// PROFESSIONE
			cartellino.setProfessione("");
			if (occupazione != null) {
				cod = CodiciProvider.cercaCodice(occupazione.getProfessione(),
						CodiciType.CODICE_PROFESSIONI,
						CodiciRicercaType.RICERCA_CODICE_SBN);
				if (cod != null)
					cartellino.setProfessione(cod.getDs_tabella());
			} else if (utente.getProfessione().getProfessione() != null
					&& utente.getProfessione().getProfessione().trim().length() > 0)
			{
				cod = CodiciProvider.cercaCodice(utente.getProfessione()
						.getProfessione(), CodiciType.CODICE_PROFESSIONI,
						CodiciRicercaType.RICERCA_CODICE_SBN);
				if (cod != null)
					cartellino.setProfessione(cod.getDs_tabella());
			}

			// SPEC. TITOLO DI STUDIO
			cartellino.setSpecStudio("");
			SpecTitoloStudioVO specialitaTitolo = null;
			if (utente.getProfessione().getIdSpecTitoloStudio() != null
					&& Integer.parseInt(utente.getProfessione()
							.getIdSpecTitoloStudio()) > 0) {
				// almaviva5_20101206 #4043 #4044
				Integer idTit = new Integer(utente.getProfessione().getIdSpecTitoloStudio());
				specialitaTitolo = (SpecTitoloStudioVO) delegate.invoke(CommandType.SRV_TITOLO_STUDIO_BY_ID, idTit).getResult();
				if (specialitaTitolo != null)
					cartellino.setSpecStudio(specialitaTitolo.getDesSpecialita());
			}

			// TITOLO DI STUDIO
			cartellino.setTitoloStudio("");
			if (specialitaTitolo != null) {
				cod = CodiciProvider.cercaCodice(
						specialitaTitolo.getTitoloStudio(),
						CodiciType.CODICE_TITOLO_STUDIO,
						CodiciRicercaType.RICERCA_CODICE_SBN);
				if (cod != null)
					cartellino.setTitoloStudio(cod.getDs_tabella());
			} else if (utente.getProfessione().getTitoloStudio() != null
					&& utente.getProfessione().getTitoloStudio().trim()
							.length() > 0) // casi di migrazione senza
											// occupazione
			{
				cod = CodiciProvider.cercaCodice(utente.getProfessione()
						.getTitoloStudio(), CodiciType.CODICE_TITOLO_STUDIO,
						CodiciRicercaType.RICERCA_CODICE_SBN);
				if (cod != null)
					cartellino.setTitoloStudio(cod.getDs_tabella());
			}

			List<CartellinoVO> righedati = new ArrayList<CartellinoVO>();
			righedati.add(cartellino);
			try {
				preparaStampa(response, righedati, fileJrxml, tipoFormato,
						"tesserino");
			} catch (Exception e) {
				e.printStackTrace();
				return mapping.getInputForward();
			}

			// return mapping.findForward("indietro");
		} catch (Exception e) {
			e.printStackTrace();
			return mapping.getInputForward();
		}
		return null;
	}

}
