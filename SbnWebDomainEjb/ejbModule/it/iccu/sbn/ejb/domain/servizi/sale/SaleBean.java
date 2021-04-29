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
package it.iccu.sbn.ejb.domain.servizi.sale;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.DomainEJBFactory.Reference;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.domain.servizi.calendario.Calendario;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.unicode.OrdinamentoUnicode;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.RichiestaRecordType;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PostoSalaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.RicercaPrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.sale.RicercaSalaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.StatoPrenotazionePosto;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DAOConcurrentException;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.persistence.dao.servizi.CalendarioDAO;
import it.iccu.sbn.persistence.dao.servizi.SaleDAO;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;
import it.iccu.sbn.polo.orm.servizi.Tbl_modello_calendario;
import it.iccu.sbn.polo.orm.servizi.Tbl_posti_sala;
import it.iccu.sbn.polo.orm.servizi.Tbl_prenotazione_posto;
import it.iccu.sbn.polo.orm.servizi.Tbl_richiesta_servizio;
import it.iccu.sbn.polo.orm.servizi.Tbl_sale;
import it.iccu.sbn.polo.orm.servizi.Trl_posto_sala_categoria_mediazione;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.ThreeState;
import it.iccu.sbn.util.ConvertiVo.ConvertToHibernate;
import it.iccu.sbn.util.ConvertiVo.ConvertToWeb;
import it.iccu.sbn.util.mail.MailBodyBuilder;
import it.iccu.sbn.util.mail.MailUtil;
import it.iccu.sbn.util.mail.MailUtil.AddressPair;
import it.iccu.sbn.util.mail.servizi.ServiziMail;
import it.iccu.sbn.util.servizi.SaleUtil;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.servizi.sale.CatMediazione;
import it.iccu.sbn.vo.custom.servizi.sale.Mediazione;
import it.iccu.sbn.vo.custom.servizi.sale.PrenotazionePostoDecorator;
import it.iccu.sbn.vo.custom.servizi.sale.StatoPrenotazionePosto2;
import it.iccu.sbn.vo.domain.mail.MailVO;
import it.iccu.sbn.vo.validators.servizi.PrenotazionePostoValidator;
import it.iccu.sbn.vo.validators.servizi.SalaValidator;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO.OperatoreType;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;
import org.hibernate.exception.DataException;

import com.annimon.stream.Optional;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.trimOrEmpty;

public class SaleBean extends TicketChecker implements Sale {

	private static final long serialVersionUID = 1924451128338880241L;

	private static Logger log = Logger.getLogger(Sale.class);

	private SaleDAO dao = new SaleDAO();
	private CalendarioDAO cdao = new CalendarioDAO();

	static Reference<Calendario> calendario = new Reference<Calendario>() {
		@Override
		protected Calendario init() throws Exception {
			return DomainEJBFactory.getInstance().getCalendario();
		}};

	static Reference<Servizi> servizi = new Reference<Servizi>() {
		@Override
		protected Servizi init() throws Exception {
			return DomainEJBFactory.getInstance().getServizi();
		}};

	static Map<String, Comparator<PrenotazionePostoVO>> ordinamentoPrenPosto = new HashMap<String, Comparator<PrenotazionePostoVO>>();

	static {
		final OrdinamentoUnicode ou = new OrdinamentoUnicode();
		//DATA
		ordinamentoPrenPosto.put("1", new Comparator<PrenotazionePostoVO>() {
			public int compare(PrenotazionePostoVO pp1, PrenotazionePostoVO pp2) {
				return pp1.getTs_inizio().compareTo(pp2.getTs_inizio());
			}
		});

		//UTENTE
		ordinamentoPrenPosto.put("2", new Comparator<PrenotazionePostoVO>() {
			public int compare(PrenotazionePostoVO pp1, PrenotazionePostoVO pp2) {
				UtenteBaseVO u1 = pp1.getUtente();
				UtenteBaseVO u2 = pp2.getUtente();
				return ou.convert(u1.getCognomeNome()).compareTo(ou.convert(u2.getCognomeNome()));
			}
		});

		//SALA
		ordinamentoPrenPosto.put("3", new Comparator<PrenotazionePostoVO>() {
			public int compare(PrenotazionePostoVO pp1, PrenotazionePostoVO pp2) {
				SalaVO s1 = pp1.getPosto().getSala();
				SalaVO s2 = pp2.getPosto().getSala();
				return ou.convert(s1.getDescrizione()).compareTo(ou.convert(s2.getDescrizione()));
			}
		});
	}

	public List<SalaVO> getListaSale(String ticket, RicercaSalaVO ricerca) throws SbnBaseException {
		try {
			checkTicket(ticket);

			List<SalaVO> output = new ArrayList<SalaVO>();

			List<Tbl_sale> sale = dao.getListaSale(ricerca.getCodPolo(), ricerca.getCodBib(), ricerca.getCodSala(),
					ricerca.getDescrizione(), ricerca.isConPostiLiberi(), ricerca.getOrdinamento());
			int progr = 0;
			for (Tbl_sale sala : sale) {
				List<Tbl_posti_sala> posti = dao.getPostiSala(sala);
				List<Tbl_modello_calendario> calendari = cdao.getCalendariSala(sala);
				SalaVO salaVO = ConvertToWeb.Sale.sala(sala, posti, calendari);
				salaVO.setProgr(++progr);
				output.add(salaVO);
			}

			BaseVO.sortAndEnumerate(output, null);

			return output;

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		}
	}

	public SalaVO aggiornaSala(String ticket, SalaVO sala) throws RemoteException {
		try {
			checkTicket(ticket);
			sala.validate(new SalaValidator());

			ModelloCalendarioVO mc = sala.getCalendario();
			List<PostoSalaVO> posti = sala.getPosti();

			String firmaUtente = DaoManager.getFirmaUtente(ticket);
			if (sala.isNuovo()) {
				sala.setTsIns(DaoManager.now());
				sala.setUteIns(firmaUtente);
				sala.setFlCanc("N");

				//creazione posti
				for (short num_posto = 1; num_posto <= sala.getNumeroPosti(); num_posto++) {
					PostoSalaVO posto = new PostoSalaVO();
					posto.setNum_posto(num_posto);
					posti.add(posto);
				}
			}
			sala.setUteVar(firmaUtente);

			Tbl_sale tbl_sala = ConvertToHibernate.Sale.sala(null, sala);

			Tbl_sale old = dao.esisteSala(tbl_sala);
			if (old != null) {
				if (old.getFl_canc() != 'S')
					throw new ApplicationException(SbnErrorTypes.SRV_SALA_ESISTENTE);

				sala.setIdSala(old.getId_sale());
				tbl_sala = ConvertToHibernate.Sale.sala(old, sala);
			}

			dao.aggiornaSala(tbl_sala);

			for (PostoSalaVO posto : posti) {
				posto.setIdSala(tbl_sala.getId_sale());
				posto.setSala(sala);
				SaleUtil.assegnaGruppo(sala.getGruppi(), posto);
				this.aggiornaPosto(ticket, posto);
			}

			if (mc != null) {
				mc.setSala(sala);
				calendario.get().aggiornaModelloCalendario(ticket, mc);
			}

			tbl_sala = dao.getSalaById(tbl_sala.getId_sale());
			List<Tbl_posti_sala> tbl_posti_sala = dao.getPostiSala(tbl_sala);
			List<Tbl_modello_calendario> calendari = cdao.getCalendariSala(tbl_sala);

			return ConvertToWeb.Sale.sala(tbl_sala, tbl_posti_sala, calendari);

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		} catch (DAOConcurrentException e) {
			throw new ApplicationException(SbnErrorTypes.DB_CONCURRENT_MODIFICATION, e);
		}
	}

	public SalaVO getSalaById(String ticket, int idSala) throws SbnBaseException {
		try {
			checkTicket(ticket);

			Tbl_sale tbl_sale = dao.getSalaById(idSala);
			List<Tbl_posti_sala> tbl_posti_sala = dao.getPostiSala(tbl_sale);
			List<Tbl_modello_calendario> calendari = cdao.getCalendariSala(tbl_sale);

			return ConvertToWeb.Sale.sala(tbl_sale, tbl_posti_sala, calendari);

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		}
	}

	public PostoSalaVO aggiornaPosto(String ticket, PostoSalaVO posto) throws SbnBaseException {
		try {
			checkTicket(ticket);

			String firmaUtente = DaoManager.getFirmaUtente(ticket);
			if (posto.isNuovo()) {
				posto.setTsIns(DaoManager.now());
				posto.setUteIns(firmaUtente);
				posto.setFlCanc("N");
			}
			posto.setUteVar(firmaUtente);

			Tbl_posti_sala tbl_posti_sala = ConvertToHibernate.Sale.posto(null, posto);

			Tbl_posti_sala old = dao.esistePosto(tbl_posti_sala);
			if (old != null) {
				if (old.getFl_canc() != 'S')
					throw new ApplicationException(SbnErrorTypes.SRV_SALA_ESISTENTE);

				posto.setId_posto(old.getId_posti_sala());
				tbl_posti_sala = ConvertToHibernate.Sale.posto(old, posto);
			}

			//controllo prenotazioni
			if (!posto.isNuovo() && !posto.isCancellato()) {
				Collection<String> cat_med_prenotazioni = this.getCategoriaMediazionePrenotazioniPosto(posto);
				//le cat mediazione del posto devono includere tutte quelle previste per le prenotazioni attive su quel posto
				if (isFilled(cat_med_prenotazioni) && !posto.getCategorieMediazione().containsAll(cat_med_prenotazioni))
					throw new ApplicationException(SbnErrorTypes.SRV_ERROR_SALE_CAT_MEDIAZIONE_POSTO_NON_MODIFICABILE);
			}

			dao.aggiornaPosto(tbl_posti_sala);

			this.aggiornaMediazioni(ticket, tbl_posti_sala, posto.getCategorieMediazione() );

			return ConvertToWeb.Sale.posto(tbl_posti_sala);

		} catch (SbnBaseException e) {
			throw e;
		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		} catch (DAOConcurrentException e) {
			throw new ApplicationException(SbnErrorTypes.DB_CONCURRENT_MODIFICATION, e);
		} catch (Exception e) {
			throw new ApplicationException(SbnErrorTypes.UNRECOVERABLE, e);
		}
	}

	private Collection<String> getCategoriaMediazionePrenotazioniPosto(PostoSalaVO posto) throws Exception {
		Set<String> catMediazione = new HashSet<String>();

		SalaVO sala = posto.getSala();
		List<Character> stati = new ArrayList<Character>();
		stati.add(StatoPrenotazionePosto.IMMESSA.getStato());
		stati.add(StatoPrenotazionePosto.FRUITA.getStato());

		List<Tbl_prenotazione_posto> prenotazioniPosto = dao.getListaPrenotazioniPosto(sala.getCodPolo(),
				sala.getCodBib(), posto.getId_posto(), null, null, null, null, null, stati, null, 0, false, null);

		//per ogni prenotazione si ricavano le cat di mediazione coinvolte
		for (Tbl_prenotazione_posto pp : prenotazioniPosto) {
			catMediazione.add(pp.getCd_cat_mediazione());

			//cat mediazione da inventario (se presente)
			for (Tbl_richiesta_servizio rs : pp.getRichieste()) {
				Tbc_inventario inv = rs.getCd_polo_inv();
				if (inv != null)
					catMediazione.addAll(SaleUtil.tipoMatInv2CatMediazione(inv.getCd_mat_inv()));
			}
		}

		return catMediazione;
	}

	public SalaVO cancellaSala(String ticket, SalaVO sala) throws RemoteException {

		try {
			//check esistenza prenotazioni attive
			if (dao.countPrenotazioniSala(sala.getIdSala() ) > 0)
				throw new ApplicationException(SbnErrorTypes.SRV_ERROR_SALE_PRENOTAZIONE_ATTIVA);

			sala.setFlCanc("S");
			for (PostoSalaVO ps : sala.getPosti())
				ps.setFlCanc("S");

			ModelloCalendarioVO mc = sala.getCalendario();
			if (mc != null) {
				calendario.get().cancellaModelloCalendario(ticket, mc);
				sala.setCalendario(null);
			}

			return aggiornaSala(ticket, sala);

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		}
	}

	private void aggiornaMediazioni(String ticket, Tbl_posti_sala tbl_posti_sala, List<String> categorieMediazione) throws DaoManagerException {
		String firmaUtente = DaoManager.getFirmaUtente(ticket);

		dao.cancellaMediazioni(tbl_posti_sala);

		//se non cancellato si reinseriscono le relazioni
		if (tbl_posti_sala.getFl_canc() != 'S')
			for (String cm : categorieMediazione) {

				Trl_posto_sala_categoria_mediazione cat_med = new Trl_posto_sala_categoria_mediazione();
				cat_med.setTs_ins(DaoManager.now());
				cat_med.setUte_ins(firmaUtente);
				cat_med.setFl_canc('N');
				cat_med.setUte_var(firmaUtente);
				cat_med.setId_posto_sala(tbl_posti_sala);
				cat_med.setCd_cat_mediazione(cm);

				dao.inserisciMediazione(cat_med);
			}

		dao.flush();
	}

	public List<Mediazione> getListaCategorieMediazione(String ticket, String codPolo, String codBib,
			boolean legataPosto, ThreeState richiedeSupp) throws SbnBaseException {

		List<Mediazione> output = new ArrayList<Mediazione>();

		try {
			List<TB_CODICI> codici = CodiciProvider.getCodici(CodiciType.CODICE_CATEGORIA_STRUMENTO_MEDIAZIONE);
			codici = CaricamentoCombo.cutFirst(codici);
			int size = ValidazioneDati.size(codici);

			for (int idx = 0; idx < size; idx++) {
				Optional<CatMediazione> catMed = CatMediazione.of(codici.get(idx));
				Mediazione m = new Mediazione();
				String cd_cat_mediazione = catMed.get().getCd_tabellaTrim();
				if (!isFilled(cd_cat_mediazione) )
					continue;

				//filtra per categorie che richiedono supporto
				boolean richiedeSupporto = catMed.get().isRichiedeSupporto();
				if (richiedeSupp == ThreeState.TRUE && !richiedeSupporto)
					continue;
				if (richiedeSupp == ThreeState.FALSE && richiedeSupporto)
					continue;

				m.setRichiedeSupporto(richiedeSupporto);
				//filtra risultato per quelle categorie configurate in biblioteca
				if (legataPosto && dao.countPostiSalaByCatMediazione(codPolo, codBib, cd_cat_mediazione) == 0)
					continue;

				m.setCd_cat_mediazione(cd_cat_mediazione);
				m.setDescr(catMed.get().getDs_tabella());
				List<TB_CODICI> tipiMatInv = CodiciProvider.getCodiciCross(CodiciType.CODICE_CAT_STRUMENTO_MEDIAZIONE_TIPO_MAT_INV, cd_cat_mediazione, false);
				if (isFilled(tipiMatInv)) {
					List<String> supporti = new ArrayList<String>();
					for (TB_CODICI cod : tipiMatInv) {
						supporti.add(cod.getDs_tabella());
					}

					m.setSupporti(supporti);
				}

				//cerca calendario associato
				ModelloCalendarioVO mc = calendario.get().getCalendarioCategoriaMediazione(ticket, codPolo, codBib, cd_cat_mediazione);
				m.setCalendario(mc);

				output.add(m);
			}
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return output;

	}

	public List<PrenotazionePostoVO> getListaPrenotazioniPosto(String ticket, RicercaPrenotazionePostoVO ricerca)
			throws SbnBaseException {
		try {
			checkTicket(ticket);

			List<PrenotazionePostoVO> output = new ArrayList<PrenotazionePostoVO>();

			PostoSalaVO posto = Optional.ofNullable(ricerca.getPosto()).orElse(new PostoSalaVO());
			UtenteBaseVO utente = Optional.ofNullable(ricerca.getUtente()).orElse(new UtenteBaseVO());
			InventarioVO inv = ricerca.getInventario();

			List<Character> stati = new ArrayList<Character>();
			stati.add(StatoPrenotazionePosto.IMMESSA.getStato());
			stati.add(StatoPrenotazionePosto.FRUITA.getStato());

			boolean chiuse = ricerca.isChiuse();
			if (chiuse)
				stati.add(StatoPrenotazionePosto.CONCLUSA.getStato());

			boolean respinte = ricerca.isRespinte();
			if (respinte) {
				stati.add(StatoPrenotazionePosto.ANNULLATA.getStato());
				stati.add(StatoPrenotazionePosto.DISDETTA.getStato());
			}

			boolean scadute = ricerca.isScadute();

			String ordinamento = trimOrEmpty(ricerca.getOrdinamento());
			List<Tbl_prenotazione_posto> prenotazioni = dao.getListaPrenotazioniPosto(ricerca.getCodPolo(),
					ricerca.getCodBib(), posto.getId_posto(), utente.getCodUtente(),
					ricerca.getTs_inizio(), ricerca.getTs_fine(), ordinamento, inv,
					stati, ricerca.getListaCatMediazione(), ricerca.getMaxRichiesteAssociate(),
					ricerca.isEscludiPrenotSenzaSupporto(),
					ricerca.getPrenotazioniEscluse() );
			final Timestamp now = DaoManager.now();

			for (Tbl_prenotazione_posto pp : prenotazioni) {
				StatoPrenotazionePosto2 statoDinamico = SaleUtil.getStatoDinamico(StatoPrenotazionePosto.of(pp.getCd_stato()), pp.getTs_fine(), now);
				if (!scadute) {
					if (statoDinamico == StatoPrenotazionePosto2.NON_FRUITA)
						continue;
				}
				if (!chiuse) {
					if (statoDinamico == StatoPrenotazionePosto2.CONCLUSA)
						continue;
				}

				PrenotazionePostoVO prenotazioneVO = ConvertToWeb.Sale.prenotazione(pp, null);
				PrenotazionePostoDecorator decorated = new PrenotazionePostoDecorator(prenotazioneVO);
				if (ricerca.isEscludiPrenotConSupporto()) {
					if (isFilled(prenotazioneVO.getMovimenti()))
						continue;
				}

				output.add(decorated);
			}

			BaseVO.sortAndEnumerate(output, ordinamentoPrenPosto.get(ordinamento));

			return output;

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		}
	}

	public PrenotazionePostoVO getPrenotazionePostoById(String ticket, int idPrenotazione) throws SbnBaseException {
		Tbl_prenotazione_posto tbl_prenotazione_posto;
		try {
			tbl_prenotazione_posto = dao.getPrenotazionePostoById(idPrenotazione);
			return ConvertToWeb.Sale.prenotazione(tbl_prenotazione_posto, null);

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		}
	}

	public PrenotazionePostoVO aggiornaPrenotazionePosto(String ticket, PrenotazionePostoVO prenotazione, boolean aggiornaRichieste)
			throws SbnBaseException {

		Tbl_prenotazione_posto old_prenotazione_posto = null;
		StatoPrenotazionePosto old_stato;

		try {
			checkTicket(ticket);
			log.debug(String.format("aggiornaPrenotazionePosto(): id=%d, stato=%s, nuova=%b, utente=%s",
					prenotazione.getId_prenotazione(), prenotazione.getStato(), prenotazione.isNuovo(), prenotazione.getUtente().getCodUtente()));
			prenotazione.validate(new PrenotazionePostoValidator());
			boolean nuovaPrenotazione = prenotazione.isNuovo();
			if (nuovaPrenotazione) {
				prenotazione.getAltriUtenti().add(prenotazione.getUtente());
				old_stato = prenotazione.getStato();
			} else {
				//verifica cambio di stato
				old_prenotazione_posto = dao.getPrenotazionePostoById(prenotazione.getId_prenotazione());
				old_stato = StatoPrenotazionePosto.of(old_prenotazione_posto.getCd_stato());
			}

			if (ValidazioneDati.in(prenotazione.getStato(),
					StatoPrenotazionePosto.IMMESSA,
					StatoPrenotazionePosto.FRUITA)) {
				this.verificaDisponibilitaPosto(prenotazione);
			}

			prenotazione.setUteVar(DaoManager.getFirmaUtente(ticket));
			Tbl_prenotazione_posto tbl_pren_posto = ConvertToHibernate.Sale.prenotazione(old_prenotazione_posto, prenotazione);
			tbl_pren_posto = dao.aggiornaPrenotazionePosto(tbl_pren_posto);
			PrenotazionePostoVO prenotazioneAggiornata = ConvertToWeb.Sale.prenotazione(tbl_pren_posto, null);

			//invio mail notifica alla biblioteca se la prenotazone è inserita da remoto e
			//senza movimenti associati (in questo caso sarà inviata mail per la richiesta)
			if (nuovaPrenotazione)
				invioMailNotificaNuovaPrenotazionePosto(ticket, prenotazioneAggiornata);

			if (aggiornaRichieste) {
				checkCambiamentoStatoPrenotazione(ticket, prenotazione, old_stato);
			}

			return prenotazioneAggiornata;

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		} catch (DAOConcurrentException e) {
			throw new ApplicationException(SbnErrorTypes.DB_CONCURRENT_MODIFICATION, e);
		}

	}

	private void checkCambiamentoStatoPrenotazione(String ticket, PrenotazionePostoVO prenotazione,
			StatoPrenotazionePosto old_stato) throws SbnBaseException {
		try {
			if (old_stato != prenotazione.getStato()) {
				switch (prenotazione.getStato()) {
				case FRUITA: {
					// i movimenti associati vanno portati allo stato 03 (consegna del documento)
					for (MovimentoVO mov : prenotazione.getMovimenti()) {
						if (StatoIterRichiesta.of(mov.getCodAttivita()) != StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE) {

							IterServizioVO iter03 = servizi.get().getIterServizio(ticket, mov.getCodPolo(), mov.getCodBibOperante(),
									mov.getCodTipoServ(), StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE);

							//se il passo iter 03 non è configurato o il mov. ha superato questo passo, il mov. va ignorato
							if (iter03 == null || iter03.getProgrIter() <= Integer.valueOf(mov.getProgrIter()) )
								continue;

							mov.setFlTipoRec(RichiestaRecordType.FLAG_RICHIESTA);
							mov.setCodAttivita(StatoIterRichiesta.CONSEGNA_DOCUMENTO_AL_LETTORE.getISOCode());
							DatiControlloVO datiControllo = new DatiControlloVO(ticket, mov, OperatoreType.BIBLIOTECARIO, true, null);
							ServizioBibliotecaVO servizio = servizi.get().getServizioBiblioteca(ticket, mov.getCodPolo(),
									mov.getCodBibOperante(), mov.getCodTipoServ(), mov.getCodServ());
							datiControllo.setServizio(servizio);

							datiControllo = servizi.get().eseguiControlliIterRichiesta(datiControllo);
							if (!ControlloAttivitaServizioResult.isOK(datiControllo) )
								throw new ApplicationException(SbnErrorTypes.SRV_CONTROLLO_DEFAULT_ATTIVITA_NON_SUPERATO);

							servizi.get().aggiornaRichiesta(ticket, mov, servizio.getIdServizio(), false );
						}
					}
					break;
				}

				case CONCLUSA: {
					// i movimenti associati vanno portati allo stato 04 (restituzione del documento)
					for (MovimentoVO mov : prenotazione.getMovimenti()) {
						if (StatoIterRichiesta.of(mov.getCodAttivita()) != StatoIterRichiesta.RESTITUZIONE_DOCUMENTO) {

							IterServizioVO iter04 = servizi.get().getIterServizio(ticket, mov.getCodPolo(), mov.getCodBibOperante(),
									mov.getCodTipoServ(), StatoIterRichiesta.RESTITUZIONE_DOCUMENTO);

							//se il passo iter 04 non è configurato o il mov. ha superato questo passo, il mov. va ignorato
							if (iter04 == null || iter04.getProgrIter() <= Integer.valueOf(mov.getProgrIter()) )
								continue;

							mov.setFlTipoRec(RichiestaRecordType.FLAG_RICHIESTA);
							mov.setCodAttivita(StatoIterRichiesta.RESTITUZIONE_DOCUMENTO.getISOCode());
							DatiControlloVO datiControllo = new DatiControlloVO(ticket, mov, OperatoreType.BIBLIOTECARIO, true, null);
							ServizioBibliotecaVO servizio = servizi.get().getServizioBiblioteca(ticket, mov.getCodPolo(),
									mov.getCodBibOperante(), mov.getCodTipoServ(), mov.getCodServ());
							datiControllo.setServizio(servizio);

							datiControllo = servizi.get().eseguiControlliIterRichiesta(datiControllo);
							if (!ControlloAttivitaServizioResult.isOK(datiControllo) )
								throw new ApplicationException(SbnErrorTypes.SRV_CONTROLLO_DEFAULT_ATTIVITA_NON_SUPERATO);

							servizi.get().aggiornaRichiesta(ticket, mov, servizio.getIdServizio(), false );
						}
					}
					break;
				}

				default:
					break;
				}
			}
		} catch (SbnBaseException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE);
		}
	}

	private void invioMailNotificaNuovaPrenotazionePosto(String ticket, PrenotazionePostoVO prenotazione) {

		//invio mail notifica alla biblioteca se la prenotazone è inserita da remoto e non ha
		//movimenti associati (in questo caso sarà inviata mail per la richiesta)
		if (ticket.contains(Constants.UTENTE_WEB_TICKET)	 	// solo da utente web
				&& !isFilled(prenotazione.getMovimenti())) {	// senza richieste associate
			try {
				ParametriBibliotecaVO paramBib = servizi.get().getParametriBiblioteca(ticket, prenotazione.getCodPolo(),
					prenotazione.getCodBib());
				if (paramBib == null)
					return;

				String mailNotifica = paramBib.getMailNotifica();
				if (!ValidazioneDati.isFilled(mailNotifica))
					return;

				log.debug("invio mail notifica nuova prenotazione posto a: " + mailNotifica);

				InternetAddress to = new InternetAddress(mailNotifica);

				ServiziMail util = new ServiziMail();
				AddressPair pair = util.getMailBiblioteca(prenotazione.getCodPolo(), prenotazione.getCodBib() );

				MailVO mail = new MailVO();
				mail.setFrom(pair.getFrom());
				mail.getReplyTo().add(pair.getReplyTo());
				mail.getTo().add(to);

				mail.setSubject("Inserimento nuova prenotazione posto");
				mail.setBody(MailBodyBuilder.Servizi.nuovaPrenotazionePosto(prenotazione));

				MailUtil.sendMailAsync(mail);

			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	private void invioMailNotificaRifiutoPrenotazionePosto(String ticket, PrenotazionePostoVO prenotazione) {
		try {
			ParametriBibliotecaVO paramBib = servizi.get().getParametriBiblioteca(ticket, prenotazione.getCodPolo(),
				prenotazione.getCodBib());
			if (paramBib == null)
				return;

			ServiziMail util = new ServiziMail();
			AddressPair pair = util.getMailBiblioteca(prenotazione.getCodPolo(), prenotazione.getCodBib() );

			MailVO mail = new MailVO();
			mail.setFrom(pair.getFrom());
			mail.getReplyTo().add(pair.getReplyTo());

			for (UtenteBaseVO u : prenotazione.getAltriUtenti() ) {
				String mailUtente = ServiziUtil.getEmailUtente(u);
				if (!isFilled(mailUtente))
					continue;

				log.debug("invio mail notifica rifiuto prenotazione posto a: " + mailUtente);

				InternetAddress to = new InternetAddress(mailUtente);
				mail.getTo().add(to);
			}

			if (!isFilled(mail.getTo()))
				return;

			mail.setSubject("Rifiuto prenotazione posto");
			mail.setBody(MailBodyBuilder.Servizi.rifiutoPrenotazionePosto(prenotazione));

			MailUtil.sendMailAsync(mail);

		} catch (Exception e) {
			log.error("", e);
		}

	}


	private void verificaDisponibilitaPosto(PrenotazionePostoVO prenotazione) throws SbnBaseException {
		List<Character> stati = new ArrayList<Character>();
		stati.add(StatoPrenotazionePosto.IMMESSA.getStato());
		stati.add(StatoPrenotazionePosto.FRUITA.getStato());

		int id_posto = prenotazione.getPosto().getId_posto();
		int id_utente = prenotazione.getUtente().getIdUtente();

		List<Integer> prenotazioniEscluse = ValidazioneDati.emptyList();
		if (!prenotazione.isNuovo() ) {
			//esclusione prenotazione in esame
			prenotazioniEscluse = ValidazioneDati.asList(prenotazione.getId_prenotazione());
		}

		try {
			List<Tbl_prenotazione_posto> prenotazioniPosto = dao.getListaPrenotazioniPosto(prenotazione.getCodPolo(),
					prenotazione.getCodBib(), 0, null, prenotazione.getTs_inizio(), prenotazione.getTs_fine(), null,
					null, stati, null, 0, false, prenotazioniEscluse);
			for (Tbl_prenotazione_posto pp : prenotazioniPosto) {

				//check estremi orario
				boolean before = pp.getTs_fine().compareTo(prenotazione.getTs_inizio()) <= 0;
				boolean after = pp.getTs_inizio().compareTo(prenotazione.getTs_fine()) >= 0;
				if (before || after)
					continue;

				// check disponibilità posto
				if (pp.getPosto().getId_posti_sala() == id_posto)
					throw new ValidationException(SbnErrorTypes.SRV_ERROR_SALE_POSTO_NON_DISPONIBILE);

				// check utente già prenotato
				if (pp.getUtente().getId_utenti() == id_utente)
					throw new ValidationException(SbnErrorTypes.SRV_ERROR_SALE_UTENTE_GIA_PRENOTATO);

				// check inventari
				//for (MovimentoVO mov : prenotazione.getMovimenti() ) {};
			}

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		}
	}

	public List<PostoSalaVO> getPostiByCatMediazione(String ticket, String codPolo, String codBib,
			String cd_cat_mediazione, boolean utenteRemoto) throws SbnBaseException {
		List<PostoSalaVO> posti = new ArrayList<PostoSalaVO>();
		try {
			List<Tbl_posti_sala> tbl_posti_sala = dao.getPostiSalaByCatMediazione(codPolo, codBib, cd_cat_mediazione, utenteRemoto);
			for (Tbl_posti_sala ps : tbl_posti_sala)
				posti.add(ConvertToWeb.Sale.posto(ps));

		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		}

		return posti;
	}

	public PrenotazionePostoVO rifiutaPrenotazionePosto(String ticket, PrenotazionePostoVO prenotazione, boolean inviaMailNotifica)
			throws SbnBaseException {

		try {
			boolean utenteWeb = ticket.contains(Constants.UTENTE_WEB_TICKET);
			prenotazione.setStato(utenteWeb ? StatoPrenotazionePosto.DISDETTA : StatoPrenotazionePosto.ANNULLATA);
			prenotazione = aggiornaPrenotazionePosto(ticket, prenotazione, false);

			//rifiuta le eventuali richieste associate (se ancora attive)
			List<Long> codRichieste = new ArrayList<Long>();
			for (MovimentoVO mov : prenotazione.getMovimenti()) {
				if (!ValidazioneDati.in(mov.getCodStatoRic(), "B", "D", "F", "H") ) {
					long idRichiesta = mov.getIdRichiesta();
					codRichieste.add(idRichiesta);
				}
			}

			if (isFilled(codRichieste) ) {
				servizi.get().rifiutaRichieste(ticket, codRichieste.toArray(new Long[0]),
						DaoManager.getFirmaUtente(ticket), inviaMailNotifica);
			} else {
				//almaviva5_20180511 prenotazione senza richieste associate, si invia mail di notifica all'utente
				if (!utenteWeb) {
					//solo se annullata dal bibliotecario
					invioMailNotificaRifiutoPrenotazionePosto(ticket, prenotazione);
				}
			}

			return prenotazione;

		} catch (DataException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		} catch (RemoteException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		}
	}

	public int getNumeroPostiLiberi(String ticket, RicercaSalaVO ricerca) throws SbnBaseException {
		try {
			return dao.countPostiLiberi(ricerca.getCodPolo(), ricerca.getCodBib());
		} catch (DaoManagerException e) {
			throw new ApplicationException(SbnErrorTypes.DB_FALUIRE, e);
		}
	}

}
