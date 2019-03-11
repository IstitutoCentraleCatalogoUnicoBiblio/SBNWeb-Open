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
package it.iccu.sbn.SbnMarcFactory.util.semantica;

import it.iccu.sbn.SbnMarcFactory.factory.semantica.SbnSoggettiDAO;
import it.iccu.sbn.ejb.dao.DAOException;
import it.iccu.sbn.ejb.domain.bibliografica.Profiler;
import it.iccu.sbn.ejb.exception.InfrastructureException;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoRicerca;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.CreaVariaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DatiLegameSoggettoDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioDescrittoreVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.LegameSogDesVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaComuneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.RicercaDescrittoreVO;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.web.vo.TreeElementView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * @author 
 *
 */
public class CatturaReticoloSoggetto {

	private static Log log = LogFactory.getLog(CatturaReticoloSoggetto.class);
	private DettaglioOggettoSemantico dettagli;
	private SbnSoggettiDAO dao;
	private final Profiler profiler;
	private final String ticket;


	private class CoppiaLegame {

		private HashSet<String> legami;

		public CoppiaLegame() {
			this.legami = new HashSet<String>();
		}

		public void addCoppia(String idPartenza, String idArrivo) {
			this.legami.add(idPartenza + idArrivo);
			this.legami.add(idArrivo + idPartenza); // inverso
		}

		public boolean contains(String idPartenza, String idArrivo) {
			return this.legami.contains(idPartenza + idArrivo) ||
				this.legami.contains(idArrivo + idPartenza);
		}
	}

	private static String getEsito(SBNMarc risposta) {
		SbnMessageType sbnMessage = risposta.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();
		String esito = sbnResult.getEsito();

		return esito;
	}

	private static String getTestoEsito(SBNMarc risposta) {
		SbnMessageType sbnMessage = risposta.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();
		String testoEsito = sbnResult.getTestoEsito();

		return testoEsito;
	}

	public CatturaReticoloSoggetto(SbnSoggettiDAO dao, Profiler profiler, String ticket) {
		super();
		this.dao = dao;
		this.profiler = profiler;
		this.ticket = ticket;
		this.dettagli = new DettaglioOggettoSemantico(ticket);
	}

	public SBNMarc eseguiCatturaReticoloSoggetto(
			TreeElementViewSoggetti reticolo, int maxLevel, CreaVariaSoggettoVO richiesta, String ticket)
			throws DAOException, Exception, DaoManagerException {

		log.info("Cattura reticolo completo del cid: " + reticolo.getKey());

//		String livAutProfiloSO = profiler.getLivelloAuthority(SbnAuthority.SO, ticket);
//		if (reticolo.getLivelloAutorita().compareTo(livAutProfiloSO) > 0)
//			throw new DAOException("Livello autorità non sufficiente per l'oggetto: " + reticolo.getKey());

		TreeElementViewSoggetti myReticolo = reticolo.copy();
		Queue<TreeElementView> elementi = myReticolo.getNodesByLevel(true);
		String livAutProfiloDE = profiler.getLivelloAuthority(SbnAuthority.DE, ticket);

		DettaglioSoggettoVO dettaglioSoggetto = (DettaglioSoggettoVO) reticolo.getDettaglio();
		String codSoggUnimarc = CodiciProvider.SBNToUnimarc(
				CodiciType.CODICE_SOGGETTARIO, dettaglioSoggetto
						.getCampoSoggettario());

		SBNMarc risposta = null;
		//almaviva5_20120627 evolutive CFI
		String edizione = ValidazioneDati.isFilled(richiesta.getEdizioneSoggettario()) ?
				richiesta.getEdizioneSoggettario() :
				dettaglioSoggetto.getEdizioneSoggettario();
		if (!richiesta.isCreaDescrittori()) {
			risposta = dao.importaSoggettoConLegami(codSoggUnimarc,
					edizione,
					dettaglioSoggetto.getTesto(),
					richiesta.getLivello(),
					richiesta.getTipoSoggetto(),
					richiesta.getNote(),
					!reticolo.isLivelloPolo(),
					dettaglioSoggetto.getCid(),
					null);
			return risposta;
		}

		// 1. controllo i descrittori del reticolo
		HashSet<String> descrittoriDaCreare = controllaEsistenzaOggetti(elementi, livAutProfiloDE);

		// 2. creo i descrittori mancanti
		if (descrittoriDaCreare.size() > 0)
			creaDescrittori(elementi, descrittoriDaCreare);

		// 3. creo i legami tra descrittori
		List<LegameSogDesVO> legamiDescrAutomatici = creaLegamiTraDescrittori(elementi);

		// 4. creo il soggetto con i suoi legami 931
		DatiLegameSoggettoDescrittoreVO datiLegami = new DatiLegameSoggettoDescrittoreVO();
		datiLegami.setCodiceSoggettario(codSoggUnimarc);
		datiLegami.setCid(dettaglioSoggetto.getCid());
		datiLegami.setCidLivelloAut(dettaglioSoggetto.getLivAut());
		datiLegami.setLegami(legamiDescrAutomatici);
		risposta = dao.importaSoggettoConLegami(codSoggUnimarc,
				edizione,
				dettaglioSoggetto.getTesto(), dettaglioSoggetto.getLivAut(),
				dettaglioSoggetto.getCategoriaSoggetto(), dettaglioSoggetto
						.getNote(), !reticolo.isLivelloPolo(),
				dettaglioSoggetto.getCid(), datiLegami);

		return risposta;

	}


	private List<LegameSogDesVO> creaLegamiTraDescrittori(Queue<TreeElementView> elementi) throws DAOException, InfrastructureException {

		CoppiaLegame coppie = new CoppiaLegame();
		List<LegameSogDesVO> listaLegami931 = new ArrayList<LegameSogDesVO>();

		while (elementi.size() > 1) {

			TreeElementViewSoggetti oggetto = (TreeElementViewSoggetti) elementi.poll();

			DettaglioDescrittoreVO dettaglio = oggetto.getAreaDatiDettaglioOggettiVO().getDettaglioDescrittoreGeneraleVO();

			// controllo se è un descrittore automatico:
			// nel qual caso aggiungo il legame nella lista dei legami 931
			// e procedo oltre
			if (oggetto.getNodeLevel() == 1) {
				LegameSogDesVO legame931 = new LegameSogDesVO();
				legame931.setDidArrivo(dettaglio.getDid());
				legame931.setNotaLegame(oggetto.getNotaLegame());
				listaLegami931.add(legame931);
				continue;
			}

			DettaglioDescrittoreVO dettaglioPadre = ((TreeElementViewSoggetti)oggetto.getParent()).getAreaDatiDettaglioOggettiVO().getDettaglioDescrittoreGeneraleVO();

			// controllo se questo legame (o il suo inverso) è stato già inserito
			if (coppie.contains(dettaglio.getDid(), dettaglioPadre.getDid()))
				continue;

			SBNMarc risposta = dao.creaLegameDescrittori(
					dettaglioPadre.getDid(),
					dettaglioPadre.getFormaNome(),
					dettaglioPadre.getT005(),
					dettaglioPadre.getLivAut(),
					dettaglio.getDid(),
					oggetto.getDatiLegame().toString(),
					oggetto.getNotaLegame(),
					true,	//condiviso
					!oggetto.isLivelloPolo());

			if (risposta == null)
				throw new InfrastructureException();

			String esito = getEsito(risposta);
			if (!esito.equals("0000") && !esito.equals("3030") )
				throw new DAOException(getTestoEsito(risposta));

			coppie.addCoppia(dettaglio.getDid(), dettaglioPadre.getDid());

			// aggiorno il T005 del descrittore padre
			CreaVariaDescrittoreVO legameDesDes = dettagli.fillCreaVariaDescrittoreVO(risposta);
			if (legameDesDes.isEsitoOk()) {
				dettaglioPadre.setT005(legameDesDes.getT005());
			}
		}

		return listaLegami931;
	}


	private void creaDescrittori(Queue<TreeElementView> elementi, HashSet<String> descrittoriDaCreare) throws Exception, DAOException {

		for (TreeElementView n : elementi) {
			TreeElementViewSoggetti oggetto = (TreeElementViewSoggetti) n;
			if (oggetto.isRoot()) // salto il soggetto
				continue;

			DettaglioDescrittoreVO dettaglio = oggetto
					.getAreaDatiDettaglioOggettiVO()
					.getDettaglioDescrittoreGeneraleVO();

			if (!descrittoriDaCreare.contains(dettaglio.getDid()) )
				continue;

			CreaVariaDescrittoreVO nuovoDescrittoreManuale = trasformaVODescrittore(
					!oggetto.isLivelloPolo(), oggetto);
			SBNMarc risposta = dao
				.creaDescrittoreManuale(nuovoDescrittoreManuale);
			if (risposta == null)
				throw new InfrastructureException();

			String esito = getEsito(risposta);
			if (!esito.equals("0000"))
				throw new DAOException(getTestoEsito(risposta));

			CreaVariaDescrittoreVO descrittoreCreato = dettagli.fillCreaVariaDescrittoreVO(risposta);
			//Aggiorno il did, il testo, il T005 e il livAut del descrittore trovato
			dettaglio.setDid(descrittoreCreato.getDid());
			//dettaglio.setTesto(descrittoreCreato.getTesto());
			dettaglio.setT005(descrittoreCreato.getT005());
			dettaglio.setLivAut(descrittoreCreato.getLivelloAutorita());
		}
	}


	private HashSet<String> controllaEsistenzaOggetti(Queue<TreeElementView> elementi, String livAutProfiloDE)
			throws Exception, DAOException, DaoManagerException {

		HashSet<String> descrittoriDaCreare = new HashSet<String>();

		for (TreeElementView n : elementi) {

			TreeElementViewSoggetti oggetto = (TreeElementViewSoggetti) n;
			if (oggetto.isRoot()) // salto il soggetto
				continue;

			log.info("controllo esistenza: " + oggetto.toString());
			DettaglioDescrittoreVO dettaglio = oggetto
					.getAreaDatiDettaglioOggettiVO()
					.getDettaglioDescrittoreGeneraleVO();
			// controllo se il descrittore è troppo alto
//			if (dettaglio.getLivAut().compareTo(livAutProfiloDE) > 0)
//				throw new DAOException("Livello autorità non sufficiente per l'oggetto: " + dettaglio.getDid());

			String codSoggUnimarc = CodiciProvider.SBNToUnimarc(
					CodiciType.CODICE_SOGGETTARIO, dettaglio
							.getCampoSoggettario());
			CreaVariaDescrittoreVO descrittoreTrovato = trovaDescrittore(
					!oggetto.isLivelloPolo(), codSoggUnimarc,
					dettaglio.getDid(), dettaglio.getTesto());

			// ho trovato il descrittore, devo controllare che la forma nome sia compatibile
			if (descrittoreTrovato.isEsitoOk()) {
				if (descrittoreTrovato.isRinvio() != oggetto.isRinvio())
					throw new DAOException("Forma Nome Descrittore incompatibile");

				String T005 = recuperaT005Descrittore(descrittoreTrovato);
				if (T005 == null)
					throw new DAOException("Errore lettura dati Descrittore");
				//Aggiorno il did, il testo, il T005 e il livAut del descrittore trovato
				dettaglio.setDid(descrittoreTrovato.getDid());
				dettaglio.setTesto(descrittoreTrovato.getTesto());
				dettaglio.setT005(T005);
				dettaglio.setLivAut(descrittoreTrovato.getLivelloAutorita());
				continue;
			}

			if (descrittoreTrovato.getEsito().equals("3001")) {// non trovato
				descrittoriDaCreare.add(dettaglio.getDid() );
				continue;
			}

			// errore
			throw new DAOException(descrittoreTrovato.getTestoEsito());

		}
		return descrittoriDaCreare;
	}

	private CreaVariaDescrittoreVO trasformaVODescrittore(boolean livelloPolo,
			TreeElementViewSoggetti nodo) throws Exception {
		DettaglioDescrittoreVO dettaglioDescrittore = nodo
				.getAreaDatiDettaglioOggettiVO()
				.getDettaglioDescrittoreGeneraleVO();
		CreaVariaDescrittoreVO nuovoDescrittore = new CreaVariaDescrittoreVO();
		nuovoDescrittore.setLivelloPolo(livelloPolo);
		String codSoggUnimarc = CodiciProvider.SBNToUnimarc(
				CodiciType.CODICE_SOGGETTARIO, dettaglioDescrittore
						.getCampoSoggettario());
		nuovoDescrittore.setCodiceSoggettario(codSoggUnimarc);
		nuovoDescrittore.setDid(dettaglioDescrittore.getDid());
		nuovoDescrittore.setFormaNome(dettaglioDescrittore.getFormaNome());
		nuovoDescrittore.setTesto(dettaglioDescrittore.getTesto());
		nuovoDescrittore.setLivelloAutorita(dettaglioDescrittore.getLivAut());
		nuovoDescrittore.setT005(dettaglioDescrittore.getT005());
		nuovoDescrittore.setNote(dettaglioDescrittore.getNote());
		nuovoDescrittore.setCondiviso(true);
		nuovoDescrittore.setCattura(true);
		nuovoDescrittore.setCategoriaTermine(dettaglioDescrittore.getCategoriaTermine());
		nuovoDescrittore.setEdizioneSoggettario(dettaglioDescrittore.getEdizioneSoggettario());
		return nuovoDescrittore;
	}

	private CreaVariaDescrittoreVO trovaDescrittore(boolean livelloPolo,
			String codSoggettario, String did, String testoEsatto)
			throws RemoteException, DAOException {

		SBNMarc ricercaSoggetti = null;
		RicercaComuneVO ricerca = new RicercaComuneVO();
		//ricerca.setCodSoggettario(codSoggettario);
		ricerca.setPolo(livelloPolo);
		ricerca.setRicercaTipoD(TipoRicerca.STRINGA_ESATTA);
		ricerca.setRicercaSoggetto(null);

		// cerco prima per testo esatto
		RicercaDescrittoreVO ricercaDescrittore = new RicercaDescrittoreVO();
		ricercaDescrittore.setTestoDescr(testoEsatto);
		ricerca.setRicercaDescrittore(ricercaDescrittore);

		ricercaSoggetti = dao.ricercaSoggetti(ricerca);
		if (ricercaSoggetti == null)
			throw new InfrastructureException();

		if (getEsito(ricercaSoggetti).equals("3001")) {	// non trovato

			// cerco per DID
			RicercaDescrittoreVO ricercaDID = new RicercaDescrittoreVO();
			ricercaDID.setDid(did);
			ricerca.setRicercaDescrittore(ricercaDID);
			ricercaSoggetti = dao.ricercaSoggetti(ricerca);
			if (ricercaSoggetti == null)
				throw new InfrastructureException();
		}

		if (!getEsito(ricercaSoggetti).equals("3001") &&
				!getEsito(ricercaSoggetti).equals("0000") )
				throw new DAOException(getTestoEsito(ricercaSoggetti));

		CreaVariaDescrittoreVO didTrovato = dettagli
				.fillCreaVariaDescrittoreVO(ricercaSoggetti);
		didTrovato.setLivelloPolo(livelloPolo);

		return didTrovato;
	}


	private String recuperaT005Descrittore(CreaVariaDescrittoreVO descrittore)
			throws Exception, DAOException, DaoManagerException {

		// recupero i dati aggiornati dalla base dati
		SBNMarc analitica = dao.creaAnaliticaSoggettoPerDid(descrittore
				.isLivelloPolo(), descrittore.getDid());
		if (!getEsito(analitica).equals("0000"))
			return null;

		TreeElementViewSoggetti root = new TreeElementViewSoggetti();
		root.setLivelloPolo(descrittore.isLivelloPolo());
		ReticoloDescrittori reticolo = new ReticoloDescrittori(ticket);
		reticolo.setReticolo(analitica, root, 0, true);
		CreaVariaDescrittoreVO tmp = trasformaVODescrittore(root.isLivelloPolo(), root);

		return tmp.getT005();
	}

}
