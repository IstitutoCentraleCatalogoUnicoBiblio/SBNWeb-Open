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
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBiblioteca;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazionePolo;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.OperazioneSuOrdiniMassivaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.documentofisico.AggDispVO;
import it.iccu.sbn.ejb.vo.documentofisico.ScaricoInventarialeVO;
import it.iccu.sbn.ejb.vo.documentofisico.SpostamentoCollocazioniVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.CodaJMSVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.DownloadVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElementoSinteticaElabDiffVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.RichiestaElaborazioniDifferiteVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.ElaborazioniDifferiteConfig;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.config.TipoAttivita;
import it.iccu.sbn.ejb.vo.validation.Validator;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.jms.JMSUtil;
import it.iccu.sbn.util.jndi.JNDIUtil;
import it.iccu.sbn.vo.custom.amministrazione.BatchAttivazioneVO;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class ElaborazioniDifferiteBean extends SbnBusinessSessionBean {


	private static final long serialVersionUID = 6515695810061013066L;
	private static Logger log = Logger.getLogger(ElaborazioniDifferiteBean.class);
	private static final Map<String, Comparator<ElementoSinteticaElabDiffVO> > ordinamenti;

	private AmministrazionePolo amministrazionePolo;
	private AmministrazioneBiblioteca amministrazioneBiblioteca;
	//private WeakReference<ElaborazioniDifferiteConfig> configRef;

	public void ejbCreate() {
		log.info("creato ejb");
		return;
	}


	private AmministrazionePolo getAmministrazionePolo() throws Exception {

		if (amministrazionePolo != null)
			return amministrazionePolo;

		amministrazionePolo = DomainEJBFactory.getInstance().getPolo();

		return amministrazionePolo;

	}

	private AmministrazioneBiblioteca getAmministrazioneBiblioteca() throws Exception {
		if (amministrazioneBiblioteca != null)
			return amministrazioneBiblioteca;

		amministrazioneBiblioteca = DomainEJBFactory.getInstance().getBiblioteca();

		return amministrazioneBiblioteca;

	}

	// Codice Funzione (asc) + Data della richiesta (desc)
	private static final Comparator<ElementoSinteticaElabDiffVO> ordBatch1 = new Comparator<ElementoSinteticaElabDiffVO>() {

		public int compare(ElementoSinteticaElabDiffVO o1,
				ElementoSinteticaElabDiffVO o2) {
			try {
				int codAttivita = o1.getProcedura().compareTo(o2.getProcedura());
				if (codAttivita != 0)
					return codAttivita;

				Date d1 = DateUtil.dataOra(o1.getDataRichiesta());
				Date d2 = DateUtil.dataOra(o2.getDataRichiesta());
				return d2.compareTo(d1);

			} catch (RuntimeException e) {
				log.error("", e);
				return 0;
			}
		};
	};

	// Richiedente (asc) + Data della richiesta (desc)
	private static final Comparator<ElementoSinteticaElabDiffVO> ordBatch2 = new Comparator<ElementoSinteticaElabDiffVO>() {

		public int compare(ElementoSinteticaElabDiffVO o1,
				ElementoSinteticaElabDiffVO o2) {
			try {
				int utente = o1.getRichiedente().compareTo(o2.getRichiedente());
				if (utente != 0)
					return utente;

				Date d1 = DateUtil.dataOra(o1.getDataRichiesta());
				Date d2 = DateUtil.dataOra(o2.getDataRichiesta());
				return d2.compareTo(d1);

			} catch (RuntimeException e) {
				log.error("", e);
				return 0;
			}
		};
	};

	// Progressivo della richiesta (desc)
	private static final Comparator<ElementoSinteticaElabDiffVO> ordBatch3 = new Comparator<ElementoSinteticaElabDiffVO>() {

		public int compare(ElementoSinteticaElabDiffVO o1,
				ElementoSinteticaElabDiffVO o2) {
			try {
				int id1 = Integer.parseInt(o1.getIdRichiesta());
				int id2 = Integer.parseInt(o2.getIdRichiesta());
				return id2 - id1;
			} catch (RuntimeException e) {
				log.error("", e);
				return 0;
			}
		};
	};

	// Data della richiesta (desc)
	private static final Comparator<ElementoSinteticaElabDiffVO> ordBatch4 = new Comparator<ElementoSinteticaElabDiffVO>() {

		public int compare(ElementoSinteticaElabDiffVO o1,
				ElementoSinteticaElabDiffVO o2) {
			try {
				Date d1 = DateUtil.dataOra(o1.getDataRichiesta());
				Date d2 = DateUtil.dataOra(o2.getDataRichiesta());
				return d2.compareTo(d1);

			} catch (RuntimeException e) {
				log.error("", e);
				return 0;
			}
		};
	};

	static {
		ordinamenti = new HashMap<String, Comparator<ElementoSinteticaElabDiffVO> >();
		ordinamenti.put("1", ordBatch1);
		ordinamenti.put("2", ordBatch2);
		ordinamenti.put("3", ordBatch3);
		ordinamenti.put("4", ordBatch4);
	}


	private void aggiungiRichiesteDiCoda(Set<ElementoSinteticaElabDiffVO> listaSintetica,
			List<Message> elenco) throws Exception {

		Map<Object, Object> cache = new HashMap<Object, Object>();
		Iterator<Message> i = elenco.iterator();

		while (i.hasNext()) {

			Message message = i.next();
			try {
				if (!(message instanceof ObjectMessage))
					continue;
				Object object = ((ObjectMessage) message).getObject();
				ParametriRichiestaElaborazioneDifferitaVO dettaglio = null;
				if (object instanceof ParametriRichiestaElaborazioneDifferitaVO)
					dettaglio = (ParametriRichiestaElaborazioneDifferitaVO) object;
				else
					continue;

				Enumeration<String> propertyNames = message.getPropertyNames();

				ElementoSinteticaElabDiffVO batch = new ElementoSinteticaElabDiffVO();
				batch.setIdRichiesta(dettaglio.getIdBatch());
				batch.setCognomeNome(dettaglio.getCognomeNome());
				batch.setListaDownload(new ArrayList<DownloadVO>());

				while (propertyNames.hasMoreElements()) {
					String key = propertyNames.nextElement();
					if (!ConstantsJMS.contains(key))
						continue;

					Object v = message.getObjectProperty(key);
					String value;
					if (v == null)
						value = "--";
					else
						value = v.toString();

					if (key.compareToIgnoreCase(ConstantsJMS.ID_CODA) == 0) {
						Integer idCoda = (Integer)v;
						batch.setIdCoda(idCoda);
						CodaJMSVO coda = (CodaJMSVO) cache.get(idCoda);
						if (coda == null) {
							coda = getAmministrazionePolo().getCodaBatch(idCoda);
							cache.put(idCoda, coda);
						}
						if (coda != null)
							batch.setNomeCoda(coda.getId_descrizione());
						continue;
					}

					if (key.compareToIgnoreCase(ConstantsJMS.ID_BATCH) == 0) {
						batch.setIdRichiesta(value);
						continue;
					}

					if (key.compareToIgnoreCase(ConstantsJMS.STATO) == 0) {
						batch.setStato(value);
						continue;
					}

					if (key.compareToIgnoreCase(ConstantsJMS.COD_ATTIVITA) == 0) {
						batch.setProcedura(value);
						String descrAtt = (String) cache.get(value);
						if (descrAtt == null) {
							descrAtt = getAmministrazionePolo().getDescrizioneAttivita(value);
							cache.put(value, descrAtt);
						}
						batch.setDescrizioneProcedura(descrAtt);
						continue;
					}

					if (key.compareToIgnoreCase(ConstantsJMS.BIBLIOTECA) == 0) {
						batch.setBiblioteca(value);
						BibliotecaVO bib = (BibliotecaVO) cache.get(value);
						if (bib == null) {
							bib = getAmministrazioneBiblioteca().getBiblioteca(dettaglio.getCodPolo(), dettaglio.getCodBib());
							if (bib != null)
								cache.put(value, bib);
						}

						if (bib != null)
							batch.setDescrizioneBiblioteca(bib.getNom_biblioteca());
						continue;
					}

					if (key.compareToIgnoreCase(ConstantsJMS.DATA_ORA_RICHIESTA) == 0) {
						batch.setDataRichiesta(value);
						continue;
					}

					if (key.compareToIgnoreCase(ConstantsJMS.DATA_ORA_ESECUZIONE_PROGRAMMATA) == 0) {
						batch.setDataEsecuzioneProgrammata(value);
						continue;
					}

					if (key.compareToIgnoreCase(ConstantsJMS.DATA_ELABORAZIONE) == 0) {
						batch.setDataInizioEsecuzione(value);
						continue;
					}

					if (key.compareToIgnoreCase(ConstantsJMS.DATA_FINE_ELABORAZIONE) == 0) {
						batch.setDataFineEsecuzione(value);
						continue;
					}

					if (key.compareToIgnoreCase(ConstantsJMS.BIBLIOTECARIO) == 0)
						batch.setRichiedente(value);
				}

				// il batch possiede un output
				if (object instanceof ElaborazioniDifferiteOutputVo) {
					ElaborazioniDifferiteOutputVo outputVo = (ElaborazioniDifferiteOutputVo) object;
					List<DownloadVO> elaborazDiff = outputVo.getDownloadList();
					if (ValidazioneDati.isFilled(elaborazDiff))
						batch.setListaDownload(outputVo.getDownloadList()); // listaDownload

					// Prepara l'elenco dei parametri
					Map<?, ?> hm = null;
					if (dettaglio != null) {
						hm = outputVo.getParametriDiRicercaMap();
					}

					StringBuilder sb = new StringBuilder();
					if (hm != null) {
						int ctr = 0;
						for (Map.Entry<?, ?> entry : hm.entrySet()) {
							if (ctr > 0)
								sb.append(", ");
							sb.append(entry.getKey() + "=" + entry.getValue());
							ctr++;
						}
					}
					batch.setParametri(sb.toString());
				}
				listaSintetica.add(batch);

			} catch (JMSException e) {
				log.error("", e);
			}

		}

	}


	public String spostaCollocazioni(SpostamentoCollocazioniVO spostamentoCollocazioniVO) throws EJBException {
		String idMessInoltro = "0";
		try {
			return getAmministrazionePolo().prenotaElaborazioneDifferita(
					spostamentoCollocazioniVO, null);
		} catch (Exception e) {
			log.error("", e);
		}
		return idMessInoltro;
	}

	public String aggiornaDisponibilita(AggDispVO aggDispVO) throws EJBException {
		String idMessInoltro = "0";
		try {
			return getAmministrazionePolo().prenotaElaborazioneDifferita(aggDispVO, null);
		} catch (Exception e) {
			log.error("", e);
		}
		return idMessInoltro;
	}

	public String scaricoInventariale(ScaricoInventarialeVO scaricoInventarialeVO) throws EJBException {
		String idMessInoltro = "0";
		try {
			return getAmministrazionePolo().prenotaElaborazioneDifferita(scaricoInventarialeVO, null);
		} catch (Exception e) {
			log.error("", e);
		}
		return idMessInoltro;
	}

	public String operazioneSuOrdine(OperazioneSuOrdiniMassivaVO operazioneSuOrdiniVO) throws EJBException {
		JMSUtil manager;
		String idMessInoltro = "0";
		try {

			InitialContext iniCtx = new InitialContext();
			Queue queA = (Queue) iniCtx.lookup("queue/sbn_20_00");
			manager = new JMSUtil(iniCtx);
			Map<String, String> param = new HashMap<String, String>();
			param.put("STATO", "HELD");
			param.put("AZIONE", "ELABORAZIONE_DIFFERITA");
			param.put("COD_ATTIVITA", "OPERAZIONE_SU_ORDINE");
			param.put("BIBLIOTECA", operazioneSuOrdiniVO.getCodBib());
			param.put("BIBLIOTECARIO", operazioneSuOrdiniVO.getUser());
			param.put("DATA_ORA_RICHIESTA", DateUtil.getDate() + " " + DateUtil.getTime());
			param.put("DATA_ORA_ESECUZIONE_PROGRAMMATA", "00:00:00");
			idMessInoltro = manager.sendQueue(queA, operazioneSuOrdiniVO, param);
		} catch (NamingException e) {
			log.error("", e);
			return idMessInoltro;
		} catch (JMSException e) {
			log.error("", e);
			return idMessInoltro;
		}
		return idMessInoltro;
	}


	/**
	 * Return an array list of
	 * @throws ValidationException
	 */

	public DescrittoreBloccoVO getRichiesteElaborazioniDifferite(String ticket,
			RichiestaElaborazioniDifferiteVO richiesta, int elementiPerBlocco)
			throws ValidationException, EJBException {

		richiesta.validate();

		Comparator<ElementoSinteticaElabDiffVO> comp = null;
		String ordinamento = richiesta.getOrdinamento();
		if (ValidazioneDati.strIsNull(ordinamento))
			comp = ordBatch3;

		comp = ordinamenti.get(ordinamento);

		Set<ElementoSinteticaElabDiffVO> listaSintetica =
			new TreeSet<ElementoSinteticaElabDiffVO>(comp);


		List<Message> elenco = null;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			InitialContext ctx = JNDIUtil.getContext();
			Queue queue;
			String codBib = DaoManager.codBibFromTicket(ticket);
			String BASE_SELECTOR =
				"(" + ConstantsJMS.VISIBILITA + "='" + ConstantsJMS.VISIBILITA_POLO + "'" +
				" OR " + ConstantsJMS.BIBLIOTECA + " ='" + codBib + "')";

			//visibilita
			if (ValidazioneDati.equalsIgnoreCase(richiesta.getVisibilita(), ConstantsJMS.VISIBILITA_BIB)) {
				BASE_SELECTOR = "(" + ConstantsJMS.BIBLIOTECA + " ='" + codBib + "')";
			}

			//filtro su data
			String dataFrom = richiesta.getDataFrom();
			if (ValidazioneDati.isFilled(dataFrom)) {
				Timestamp from = DateUtil.toTimestamp(dataFrom);
				BASE_SELECTOR += " AND " + ConstantsJMS.JMSTimestamp + ">=" + from.getTime();
			}

			String dataTo = richiesta.getDataTo();
			if (ValidazioneDati.isFilled(dataTo)) {
				Timestamp to   = DateUtil.toTimestampA(dataTo);
				BASE_SELECTOR += " AND " + ConstantsJMS.JMSTimestamp + "<=" + to.getTime();
			}

			//ID batch
			String idRichiesta = richiesta.getIdRichiesta();
			if (ValidazioneDati.isFilled(idRichiesta))
				params.put(ConstantsJMS.ID_BATCH, new Integer(idRichiesta) );

			//cod attivita batch
			if (ValidazioneDati.isFilled(richiesta.getProcedura()))
				params.put(ConstantsJMS.COD_ATTIVITA, richiesta.getProcedura());

			//stato elaborazione
			if (ValidazioneDati.isFilled(richiesta.getStato()))
				params.put(ConstantsJMS.STATO, richiesta.getStato());

			//bibliotecario richiedente
			if (ValidazioneDati.isFilled(richiesta.getRichiedente()))
				params.put(ConstantsJMS.BIBLIOTECARIO, richiesta.getRichiedente());

			//filtro su coda
			String dataEsecuzioneProgrammata = richiesta.getDataEsecuzioneProgrammata();
			if (ValidazioneDati.isFilled(dataEsecuzioneProgrammata) )
				params.put(ConstantsJMS.ID_CODA, new Integer(dataEsecuzioneProgrammata) );

			Set<String> listaCode = new HashSet<String>();
			List<CodaJMSVO> listaCodeBatch = getAmministrazionePolo().getListaCodeBatch();
			for (CodaJMSVO coda : listaCodeBatch)
				listaCode.add(coda.getNome());

			//aggiungo code di output
			List<String> listaCodeBatchOutput = getAmministrazionePolo().getListaCodeBatchOutput();
			for (String coda : listaCodeBatchOutput)
				listaCode.add(coda);

			// per ogni coda definita leggo il contenuto dal server JMS
			for (String nomeCoda : listaCode) {
				queue = (Queue) ctx.lookup(nomeCoda);
				elenco = getAmministrazionePolo().getRichieste(ticket, queue, BASE_SELECTOR, params);
				aggiungiRichiesteDiCoda(listaSintetica, elenco);
			}

		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (Exception e) {
			throw new EJBException(e);
		}

		List<ElementoSinteticaElabDiffVO> output = new ArrayList<ElementoSinteticaElabDiffVO>();
		if (listaSintetica.size() > 0) {
			int progressivo = 0;
			for (ElementoSinteticaElabDiffVO batch : listaSintetica) {
				batch.setProgressivo(++progressivo);
				output.add(batch);
			}
		}

		return saveBlocchi(ticket, output, elementiPerBlocco);
	} // End getRichiesteElaborazioniDifferite


	public List<BatchAttivazioneVO> getBatchAttivabili() throws EJBException  {
		try {
			return getAmministrazionePolo().getBatchServiziAll();
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (Exception e) {
			throw new EJBException(e);

		}
	}

	public List<CodaJMSVO> getListaCodeBatch() throws EJBException  {
		try {
			return getAmministrazionePolo().getListaCodeBatch();
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (Exception e) {
			throw new EJBException(e);

		}

	}

	public String prenotaElaborazioneDifferita(String ticket,
			ParametriRichiestaElaborazioneDifferitaVO richiesta, Map<String, Object> properties)
			throws ValidationException, ApplicationException {
		return prenotaElaborazioneDifferita(ticket, richiesta, properties, null);
	}

	public String prenotaElaborazioneDifferita(String ticket,
			ParametriRichiestaElaborazioneDifferitaVO richiesta, Map<String, Object> properties,
			Validator<? extends ParametriRichiestaElaborazioneDifferitaVO> validator)
			throws ValidationException, ApplicationException {

		checkTicket(ticket);
		ParametriRichiestaElaborazioneDifferitaVO req = richiesta.copy();
		req.setTicket(ticket);
		try {
			return getAmministrazionePolo().prenotaElaborazioneDifferita(req, properties, validator);
		} catch (ApplicationException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public void eliminaRichiesteElaborazioniDifferite(String ticket, String[] idBatch, Boolean deleteOutputs) throws ValidationException, ApplicationException {
		checkTicket(ticket);
		try {
			log.debug("L'utente " + DaoManager.getFirmaUtente(ticket)
					+ " ha richiesto la cancellazione di "
					+ ValidazioneDati.size(idBatch) + " record batch");
			getAmministrazionePolo().eliminaRichiesteElaborazioniDifferite(idBatch, deleteOutputs);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public ElaborazioniDifferiteConfig getConfigurazioneElaborazioniDifferite(TipoAttivita tipo) throws EJBException {
		try {
			return getAmministrazionePolo().getConfigurazioneElaborazioniDifferite(tipo);
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		} catch (Exception e) {
			throw new EJBException(e);
		}

	}

}
