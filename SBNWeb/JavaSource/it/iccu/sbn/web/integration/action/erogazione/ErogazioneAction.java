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
package it.iccu.sbn.web.integration.action.erogazione;


import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.ejb.vo.servizi.ParametriBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaCercaType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaGestioneType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.batch.ParametriBatchSollecitiVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.SupportoBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.TipoServizioVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.EsemplareDocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.sif.SIFListaDocumentiNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.sif.SIFListaDocumentiNonSbnVO.TipoSIF;
import it.iccu.sbn.ejb.vo.servizi.erogazione.InfoDocumentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.IterServizioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.RichiestaRecordType;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SupportiModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.RuoloBiblioteca;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;
import it.iccu.sbn.exception.UtenteNotAuthorizedException;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ill.ILLConfiguration2;
import it.iccu.sbn.servizi.ill.ILLConfiguration2.Action;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.servizi.CodTipoServizio;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.vo.domain.CodiciAttivita;
import it.iccu.sbn.vo.xml.binding.ill.apdu.ILLServiceType;
import it.iccu.sbn.web.actions.common.ConfermaDati;
import it.iccu.sbn.web.constant.NavigazioneServizi;
import it.iccu.sbn.web.exception.SbnBaseException;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ListaMovimentiForm;
import it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ListaMovimentiForm.RichiestaListaMovimentiType;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.amministrazionesistema.BibliotecaDelegate;
import it.iccu.sbn.web.integration.bd.gestioneservizi.ServiziDelegate;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizio;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.StatoIterRichiesta;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO.OperatoreType;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public abstract class ErogazioneAction extends ServiziBaseAction implements SbnAttivitaChecker {

	/**
	 * Crea una nuova richiesta
	  * it.iccu.sbn.web.actions.servizi.erogazione
	  * ErogazioneAction.java
	  * creaNuovaRichiesta
	  * MovimentoVO
	  * @param request
	 * @param template
	  * @param codPolo
	  * @param codBib
	  * @param utenteVO
	  * @param infoVO
	  * @param servizioVO
	  * @param primoIter
	  * @return
	  * @throws Exception
	  *
	  *
	 */
	protected MovimentoVO preparaMovimento(HttpServletRequest request,
			MovimentoVO template, String codPolo, String codBib, UtenteBaseVO utenteVO,
			InfoDocumentoVO infoVO, ServizioBibliotecaVO servizioVO,
			ControlloAttivitaServizio primoIter, OperatoreType richiedente)
			throws Exception {
		boolean ok = true;


		//check dati ingresso
		if (servizioVO == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.nuovaRichiesta.servizioNonPresente"));
			ok = false;
		}

		if (primoIter == null) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.nuovaRichiesta.passoIterNonPresente"));
			ok = false;
		}

		if (!ok) {

			throw new ValidationException("Errore durante creazione nuova richiesta");
		}

		//Impostazione dati nuova richiesta
		UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
		String utente = utenteCollegato.getFirmaUtente();

		MovimentoVO mov = template != null ? template : new MovimentoVO();
		Timestamp dtReq = (template == null || template.getDataRichiesta() == null) ?
				DaoManager.now() :
				template.getDataRichiesta();

		mov.setFlCanc("N");
		mov.setUteIns(utente);
		mov.setUteVar(utente);
		mov.setTsIns(dtReq);
		//mov.setTsVar(dtReq);

		mov.setCodPolo(codPolo);
		mov.setCodBibOperante(codBib);
		mov.setCodBibDest(codBib);

		if (utenteVO != null) {
			mov.setCodBibUte(utenteVO.getCodBib());
			mov.setCodUte(utenteVO.getCodUtente().trim());
		}

		if (infoVO == null)
			throw new ValidationException("Errore durante creazione nuova richiesta");

		String annata = infoVO.getAnnoPeriodico();
		if (ValidazioneDati.isFilled(annata))
			mov.setAnnoPeriodico(annata);

		DocumentoNonSbnVO documentoNonSbnVO = infoVO.getDocumentoNonSbnVO();
		InventarioTitoloVO inventarioTitoloVO = infoVO.getInventarioTitoloVO();

		if (infoVO.isRichiestaSuSegnatura() ) {
			mov.setCodBibDocLett(documentoNonSbnVO.getCodBib());
			mov.setTipoDocLett(documentoNonSbnVO.getTipo_doc_lett() + "");
			mov.setCodDocLet(documentoNonSbnVO.getCod_doc_lett() + "");
			//almaviva5_20091230
			mov.setBid(documentoNonSbnVO.getBid());
			mov.setNatura(String.valueOf(documentoNonSbnVO.getNatura()) );

			List<EsemplareDocumentoNonSbnVO> esemplari = documentoNonSbnVO.getEsemplari();
			if (ValidazioneDati.isFilled(esemplari) )
				//almaviva5_20130319 #5286
				mov.setProgrEsempDocLet(ServiziUtil.selezionaEsemplareAttivo(documentoNonSbnVO).getPrg_esemplare() + "");
		}

		if (infoVO.isRichiestaSuInventario() ) {
			mov.setCodBibInv(inventarioTitoloVO.getCodBib());
			mov.setCodInvenInv(inventarioTitoloVO.getCodInvent()+"");
			mov.setCodSerieInv(inventarioTitoloVO.getCodSerie());
			//almaviva5_20091230
			mov.setBid(inventarioTitoloVO.getBid());
			mov.setNatura(String.valueOf(inventarioTitoloVO.getNatura()) );
		}

		if ( (richiedente != OperatoreType.BIBLIOTECARIO) ||
				primoIter.bibliotecarioAbilitato(utenteCollegato)) {

			IterServizioVO passoIter = primoIter.getPassoIter();
			mov.setCodStatoMov(passoIter.getCodStatoMov());
			mov.setCodStatoRic(passoIter.getCodStatoRich());
			mov.setCodAttivita(passoIter.getCodAttivita());
			mov.setCodTipoServ(passoIter.getCodTipoServ());
			mov.setProgrIter(passoIter.getProgrIter().toString());
			mov.setCodServ(servizioVO.getCodServ());
			mov.setIdServizio(servizioVO.getIdServizio());

			mov.setDataRichiesta(dtReq);

			// estraggo i parametri di biblioteca
			// che saranno utilizzati successivamente
			// nell'eventuale somma con la prelazione e
			// la categoria di Fruizione e di Riproduzione
			ParametriBibliotecaVO parametri = this.getParametriBiblioteca(codPolo, codBib, request);

			Timestamp tsInizioPrev = mov.getDataInizioPrev();
			if (tsInizioPrev == null) { //data inizio prevista da calcolare

				//almaviva5_20160408 #6150
				mov.setDataInizioPrev(ServiziUtil.calcolaDataInizioPrevista(parametri, servizioVO, dtReq));

			} else	////data inizio prevista già calcolata
				mov.setDataInizioPrev(tsInizioPrev);

			// la data di fine prevista è data dalla somma della data inizio prevista con
			// la durata del servizio
			mov.setDataFinePrev(ServiziUtil.calcolaDataFinePrevista(servizioVO, mov.getDataInizioPrev()));

			mov.setFlTipoRec(RichiestaRecordType.FLAG_RICHIESTA);

			//boolean flgErrCatRiprodDocSBN = false;
			boolean flgErrCatRiprodDefault = false;

			// il successivo flag indica se la categoria di riproduzione
			// è presente sul documento
			boolean flgCatRiprodDocumento = false;

			// Recupero la Categoria di Fruizione e la Categoria di Riproduzione
			// dal Documento e dai Parametri di Biblioteca per impostarla in MovimentoVO

			if (infoVO != null	&& infoVO.isRichiestaSuSegnatura() ) {
				if (ValidazioneDati.isFilled(documentoNonSbnVO.getCodFruizione()) ) {
					// imposto la Categoria di Fruizione a partire dal documento non SBN
					mov.setCat_fruizione(documentoNonSbnVO.getCodFruizione());
				}
				else {
					// se il documento non SBN non ha impostata la Categoria di Fruizione
					// la imposto da quella di default dei Parametri di Biblioteca
					mov.setCat_fruizione(parametri.getCodFruizione());
				}
				// la successiva "if" non è significativa perchè la
				// categoria di riproduzione di default deve essere sempre presente
				// (vedi parametri di biblioteca)
				// Viene lasciata nel caso in cui non sono stati ancora creati i codici
				// con le categorie di riproduzione e quindi non è stata ancora
				// assegnata la categoria di riproduzione di default
				if (!ValidazioneDati.isFilled(parametri.getCodRiproduzione())) {
					// se non presente la Categoria di Riproduzione di default nei Parametri di Biblioteca
					// imposto il flag opportuno
					flgErrCatRiprodDefault = true;
				}
				else {
					// imposto la Categoria di Riproduzione dalla Categoria di Riproduzione di default
					// nei Parametri di Biblioteca perchè non presente sul documento non SBN
					mov.setCat_riproduzione(parametri.getCodRiproduzione());
				}
			}
			else {
				if (infoVO != null	&& inventarioTitoloVO != null) {
					// imposto la Categoria di Fruizione a partire dal documento SBN
					mov.setCat_fruizione(inventarioTitoloVO.getCodFrui());

					if (!ValidazioneDati.isFilled(inventarioTitoloVO.getCodRiproducibilita())) {
						// se non presente la Categoria di Riproduzione nel documento SBN
						// imposto il flag opportuno
						//flgErrCatRiprodDocSBN = true;

						// la successiva "if" non è significativa perchè la
						// categoria di riproduzione di default deve essere sempre presente
						// (vedi parametri di biblioteca)
						// Viene lasciata nel caso in cui non sono stati ancora creati i codici
						// con le categorie di riproduzione e quindi non è stata ancora
						// assegnata la categoria di riproduzione di default
						if (!ValidazioneDati.isFilled(parametri.getCodRiproduzione())) {
							// se non presente la Categoria di Riproduzione di default nei Parametri di Biblioteca
							// imposto il flag opportuno
							flgErrCatRiprodDefault = true;
						}
						else {
							// imposto la Categoria di Riproduzione dalla Categoria di Riproduzione di default
							// nei Parametri di Biblioteca perchè non presente sul documento non SBN
							mov.setCat_riproduzione(parametri.getCodRiproduzione());
						}

					}
					else {
						// imposto la Categoria di Riproduzione a partire dal documento SBN
						mov.setCat_riproduzione(inventarioTitoloVO.getCodRiproducibilita());
						flgCatRiprodDocumento = true;
					}
				}
			}

			if (ok) {

				boolean ill = false;
				DatiRichiestaILLVO datiILL = null;

				// carico i valori della tabella tb_codici relativi a quel servizio
				CodTipoServizio ts = CodTipoServizio.of(this.getCodice(servizioVO.getCodTipoServ(),
						CodiciType.CODICE_TIPO_SERVIZIO, CodiciRicercaType.RICERCA_CODICE_SBN, request));

				if (ts != null) {

					//almaviva5_20151216 servizi ill
					if (servizioVO.isILL() ) {
						//dati minimali richiesta ill
						datiILL = preparaDatiILL(request, codPolo, codBib, mov, passoIter, documentoNonSbnVO);
						mov.setCodSupporto(datiILL.getCd_supporto());
						ill = true;
					}

					String flg_ult_supp = "";
					String flg_ult_mod = "";

					if (ts.richiedeSupporto() || (ill && datiILL.isFornitrice()) ) {
						// se il flag relativo a ult_supp sulla tabella codici è "S"
						flg_ult_supp = "S";
						flg_ult_mod = "N";
						//se si sta inserendo una richiesta ILL come richiedente il supporto sarà
						//determinato dalla biblioteca fornitrice, quindi non va impostato anche se richiesto dalla
						//configurazione locale
						if (ill && datiILL.isRichiedente()) {
							flg_ult_supp = "N";
							flg_ult_mod = "S";
						}
					}
					else{
						// altrimenti
						if (ts.richiedeModalitaErogazione()) {
							// se il flag relativo a ult_supp sulla tabella codici non è "S"
							// e  il flag relativo a ult_mod  sulla tabella codici è "S"
							flg_ult_supp = "N";
							flg_ult_mod = "S";
						}
						else {
							// il flag relativo a ult_supp sulla tabella codici non è "S"
							// e  il flag relativo a ult_mod  sulla tabella codici non è "S"
							flg_ult_supp = "N";
							flg_ult_mod = "N";
						}
					}

					log.debug("preparaMovimento(): tipoServizio: "
							+ ts.getCd_tabellaTrim()
							+ ", flg_ult_supp: " + flg_ult_supp
							+ ", flg_ult_mod: " + flg_ult_mod);

					// inizializzo INS_RICHIESTA_LISTA_SUPPORTI e INS_RICHIESTA_LISTA_MOD_EROGAZIONE
					request.setAttribute(NavigazioneServizi.INS_RICHIESTA_LISTA_SUPPORTI, new ArrayList<SupportoBibliotecaVO>());
					request.setAttribute(NavigazioneServizi.INS_RICHIESTA_LISTA_MOD_EROGAZIONE, new ArrayList<TariffeModalitaErogazioneVO>());

					String fl_svolg = ts.getCd_flg4();
					if (ValidazioneDati.strIsNull(fl_svolg) ) {
						LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.flagSvolgimentoNonImpostato"));
						ok = false;
					} else {
						//conterrà 'L' Locale o 'I' ILL o 'T' (Sia Locale che ILL)
						mov.setFlSvolg(fl_svolg);

						List<SupportoBibliotecaVO> listaSupporti = new ArrayList<SupportoBibliotecaVO>();
						List<TariffeModalitaErogazioneVO> listaTariffe = new ArrayList<TariffeModalitaErogazioneVO>();

						if (flg_ult_supp.equals("S")) {
							// se flg_ult_supp è "S"

							// la successiva if non viene effettuata perchè ora
							// c'è la categoria di riproduzione di default
							// anche per i documenti SBN
							/*
							if (flgErrCatRiprodDocSBN == true) {
								// non presente la Categoria di Riproduzione nel documento SBN
								// imposto il messaggio
								LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.nonCategoriaRiproduzDocSbn"));
								ok=false;
							}
							*/
							if (flgErrCatRiprodDefault) {
								// non presente la Categoria di Riproduzione di default nei Parametri di Biblioteca
								// imposto il messaggio

								// tale eventualità non dovrebbe essere mai verificata
								// perchè la categoria di riproduzione di default deve essere sempre presente
								// (vedi parametri di biblioteca)
								// Viene lasciata nel caso in cui non sono stati ancora creati i codici
								// con le categorie di riproduzione e quindi non è stata ancora
								// assegnata la categoria di riproduzione di default

								//LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.nonCategoriaRiproduzDefaultNonSbn"));
								LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.nonCategoriaRiproduzDefault"));
								ok=false;
							}

							if (ok) {

								if (flgCatRiprodDocumento || richiedente != OperatoreType.BIBLIOTECARIO) {

									// se la categoria di riproduzione è quella del documento
									// oppure il richiedente non è il bibliotecario

									// la lista dei Supporti è estratta a partire dalla Categoria di Riproduzione
									// Ricerca della Categoria di Riproduzione del documento per estrarre
									// i supporti ad essa associati
									// viene utilizzata la tabella "tb_codici" relativamente al codice LSUP
									List<TB_CODICI> listaCodici = CodiciProvider.getCodiciCross(CodiciType.CODICE_TIPI_RIPRODUZIONE_CODICE_SUPPORTO, mov.getCat_riproduzione(), true);
									if (ValidazioneDati.isFilled(listaCodici) ) {
										for (TB_CODICI cod : listaCodici) {
											SupportoBibliotecaVO sup = this.getSupportoBiblioteca(codPolo, codBib, cod.getCd_tabella().trim(), request);
											if (sup != null)
												listaSupporti.add(sup);
										}
									}

								} else {

									// altrimenti la categoria di riproduzione non è quella del documento
									// e il richiedente è il bibliotecario

									// in tal caso la lista dei supporti
									// viene estratta dai supporti configurati per quel polo-biblioteca

									listaSupporti = ServiziDelegate.getInstance(request).getSupportiBiblioteca(codPolo, codBib, fl_svolg);

									// e ripulisco la categoria di riproduzione
									// impostata precedentemente con la categoria di riproduzione di default
									mov.setCat_riproduzione("");
								}

								if (ValidazioneDati.isFilled(listaSupporti) ) {

									// salvo la lista dei Supporti
									request.setAttribute(NavigazioneServizi.INS_RICHIESTA_LISTA_SUPPORTI, listaSupporti);

									boolean sup_found = false;
									for (SupportoBibliotecaVO sup : listaSupporti)
										if ((sup_found = ValidazioneDati.equals(sup.getCodSupporto(), mov.getCodSupporto())))
											break;
									if (!sup_found)
										// imposto nel movimento da inserire il primo supporto
										mov.setCodSupporto(listaSupporti.get(0).getCodSupporto());

									//estraggo le modalità di erogazione associate al primo supporto
									List<SupportiModalitaErogazioneVO> listaSupportiModalitaErogazione = this.getSupportiModalitaErogazione(codPolo,
											codBib, mov.getCodSupporto(), request);

									if (ValidazioneDati.isFilled(listaSupportiModalitaErogazione) ) {

										listaTariffe = new ArrayList<TariffeModalitaErogazioneVO>();
										for (SupportiModalitaErogazioneVO sme : listaSupportiModalitaErogazione) {
											TariffeModalitaErogazioneVO tme = new TariffeModalitaErogazioneVO();
											ClonePool.copyCommonProperties(tme, sme);
											tme.setCodTipoServ(mov.getCodTipoServ());
											listaTariffe.add(tme);
										}

										// salvo la lista delle Modalità di Erogazione
										request.setAttribute(NavigazioneServizi.INS_RICHIESTA_LISTA_MOD_EROGAZIONE, listaTariffe);

										boolean tar_found = false;
										for (TariffeModalitaErogazioneVO tme : listaTariffe)
											if ((tar_found = ValidazioneDati.equals(tme.getCodErog(), mov.getCod_erog())))
												break;
										if (!tar_found)
											// imposto nel movimento da inserire il primo codice di erogazione associato al primo supporto
											mov.setCod_erog(listaTariffe.get(0).getCodErog());
									} else {
										LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.modalitaErogazioneNonConfigSupporti"));
										ok = false;
									}
								} else {
									LinkableTagUtils.addError(request, ill
											? new ActionMessage("errors.servizi.listaMovimenti.nonConfigSupporti.ill")
											: new ActionMessage("errors.servizi.listaMovimenti.nonConfigSupporti"));
									ok = false;
								}
							}

						}
						else {

							// se flg_ult_supp non è "S"
							// estraggo le modalità di erogazione associate al servizio
							// per impostarne la prima
							// (potrà essere variata dopo l'inserimento della richiesta)

							if (ill) {
								//se sto inserendo come richiedente devo selezionare solo le modalità locali, come fornitrice quelle ILL
								String flsvolg_erog = datiILL.isRichiedente() ? "L" : "I";
								listaTariffe = ServiziDelegate.getInstance(request).getTariffeModalitaErogazioneServizio(codPolo, codBib, servizioVO.getCodTipoServ(), flsvolg_erog);
							}
							else
								listaTariffe = this.getTariffeModalitaErogazione(codPolo, codBib, servizioVO.getCodTipoServ(), request);

							if (ValidazioneDati.isFilled(listaTariffe) ) {

								// salvo la lista delle Modalità di Erogazione
								request.setAttribute(NavigazioneServizi.INS_RICHIESTA_LISTA_MOD_EROGAZIONE, listaTariffe);

								boolean tar_found = false;
								for (TariffeModalitaErogazioneVO tme : listaTariffe)
									if ((tar_found = ValidazioneDati.equals(tme.getCodErog(), mov.getCod_erog())))
										break;
								if (!tar_found)
									// imposto nel movimento da inserire il primo codice di erogazione associato al primo supporto
									mov.setCod_erog(listaTariffe.get(0).getCodErog());

							} else {
								LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.modalitaErogazioneNonAssociateAlServizio"));
								ok = false;
							}

						}


						if (ok) {

							// se presente il supporto:
							// costo del servizio = (costo unitario(supporto) * n.ro-pezzi + costo fisso (supporto) +
							//                       costo unitario(erogazione)* n.ro-pezzi + costo fisso (erogazione))

							// se non presente il supporto:
							// costo del servizio = costo unitario(erogazione)* n.ro-pezzi + costo fisso (erogazione)

							// NB: all’atto dell’inserimento della richiesta, il n.ro pezzi è un campo ammesso solo per le riproduzione,
							// negli altri casi è assunto dal sistema uguale a zero.

							// Meglio precisare:
							// a) il n. pezzi è ricavato (=conteggiato) dal sistema sulla base di quanto inserito dall’utente,
							// con una particolare sintassi, nel campo intervallo pagine, che è liberamente implementabile
							// nella configurazione del modulo di richiesta;
							// b)	il costo unitario può essere diverso da 0 per qualsiasi servizio,
							// che abbia una modalità di erogazione a pagamento. Es. Prestito con spedizione per posta:
							// Intendo dire che il costo della modalità di erogazione dipende dal supporto se il servizio prevede più supporti,
							// ciascuno con la propria modalità di erogazione, ma può esistere anche direttamente legata al servizio,
							// se questo non prevede ulteriori supporti.

							// imposto il numero pezzi a 0 perchè nuova richiesta
							// se si tratta di un servizio ILL e sono fonitrice va impostato a 1
							Short numPezzi = (short) ((ill && mov.isFornitriceRichiestaILL() ) ? 1 : 0);
							mov.setNumPezzi(numPezzi);

							// imposto il VO con la modalità di Erogazione
							TariffeModalitaErogazioneVO tariffe = listaTariffe.get(0);

							if (!ValidazioneDati.isFilled(listaSupporti) ) {
								// se non presenti i supporti
								ServiziUtil.calcolaCostoServizio(mov, null, tariffe);
							}
							else {
								// se presenti i supporti

								// imposto il VO con il Supporto
								SupportoBibliotecaVO supporto = listaSupporti.get(0);
								ServiziUtil.calcolaCostoServizio(mov, supporto, tariffe);
							}

						}

					}

				} else {
					LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.tipoServizioNonTrovato"));
					ok = false;
				}
			}

		} else {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erogazione.bibliotecarioNonAbilitato"));
			ok = false;
		}

		if (ok) {
			return mov;
		}
		else {

			throw new ValidationException("Errore durante creazione nuova richiesta");
		}
	}

	protected DatiControlloVO eseguiControlli(HttpServletRequest request,
			MovimentoVO movimento, ControlloAttivitaServizio passoIter, OperatoreType operatore,
			boolean inoltraPrenotazione, ControlloAttivitaServizioResult previousResult)
			throws ValidationException, Exception {

		DatiControlloVO datiControllo = new DatiControlloVO(
				Navigation.getInstance(request).getUserTicket(),
				movimento,
				operatore,
				inoltraPrenotazione, previousResult);

		List<String> messaggi = new ArrayList<String>();

		ControlloAttivitaServizioResult checkAttivita = passoIter.controlloDefault(datiControllo);

		List<String> msgSupplementari = datiControllo.getCodiciMsgSupplementari();
		if (ValidazioneDati.isFilled(msgSupplementari) ) {
			//Esistono dei codici messaggi supplementari che il controllo eseguito ha restituito
			this.addErrors(request, msgSupplementari);
			msgSupplementari.clear();
		}

		if (ControlloAttivitaServizioResult.isOK(checkAttivita) ) {

			boolean esitoControllo = passoIter.controlloIter(datiControllo, messaggi, false, inoltraPrenotazione);

			if (ValidazioneDati.isFilled(messaggi) ) {
				String[] msg = new String[messaggi.size()];
				messaggi.toArray(msg);
				this.addErrors(request, msg);
			}

			if (ValidazioneDati.isFilled(msgSupplementari) ) {
				//Esistono dei codici messaggi supplementari che il controllo eseguito ha restituito
				this.addErrors(request, msgSupplementari);
				msgSupplementari.clear();
			}

			if (!esitoControllo)
				throw new ValidationException(SbnErrorTypes.SRV_CONTROLLO_ITER_BLOCCANTE);

		} else
			if (ValidazioneDati.isFilled(messaggi) ) {
				String[] msg = new String[messaggi.size()];
				messaggi.toArray(msg);
				this.addErrors(request, msg);
			}

		return datiControllo;

	}


	protected void filtroErroreControlloDefault(HttpServletRequest request,
			ListaMovimentiForm currentForm, ActionMapping mapping,
			DatiControlloVO datiControllo) {


		if (ValidazioneDati.equals(datiControllo.getControlloEseguito(), StatoIterRichiesta.CONTROLLO_DISPONIBILITA)) {
			// Non c'è la disponibilità del documento, si chiede se si vuole
			// provare a inserire una richiesta
			// di prenotazione
			currentForm.setConferma(true);
			currentForm.setRichiesta(RichiestaListaMovimentiType.PRENOTAZIONE);
			LinkableTagUtils.addError(request, new ActionMessage(
					"message.servizi.erogazione.richiestaPrenotazione"));

			this.saveMessages(request, ConfermaDati.preparaConferma(this,
					mapping, request));
		}

	}


	protected MovimentoListaVO creaDettaglioMovimento(HttpServletRequest request, UtenteBaseVO utenteVO, InfoDocumentoVO infoDocumentoVO, MovimentoVO mov)
	throws Exception {
		MovimentoListaVO dettaglio = new MovimentoListaVO(mov);

		dettaglio.decode();

		if (utenteVO != null) {
			dettaglio.setCognomeNome(utenteVO.getCognomeNome());
			//dettaglio.setEmail(ServiziUtil.getEmailUtente(utenteVO));
		}

		if (infoDocumentoVO != null) {
			dettaglio.setTitolo(infoDocumentoVO.getTitolo());
			dettaglio.setSegnatura(infoDocumentoVO.getSegnatura());
		}

		String inventario = "";
		if (dettaglio.isRichiestaSuInventario())
			inventario = dettaglio.getCodBibInv() + " "
				+ dettaglio.getCodSerieInv() + " "
				+ dettaglio.getCodInvenInv();
		else
			inventario = dettaglio.getCodBibDocLett() + " "
				+ dettaglio.getTipoDocLett() + " "
				+ dettaglio.getCodDocLet() + " "
				+ dettaglio.getProgrEsempDocLet();

		dettaglio.setInventario(inventario);

		return dettaglio;
	}


	protected ServizioBibliotecaVO getServizio(
			List<ServizioBibliotecaVO> listaServizi, String codTipoServ, String codServ) {

		for (ServizioBibliotecaVO srv : listaServizi)
			if (ValidazioneDati.equals(srv.getCodTipoServ(), codTipoServ) &&
				ValidazioneDati.equals(srv.getCodServ(), codServ))
				return srv;

		return null;
	}


	protected TariffeModalitaErogazioneVO getModalitaErogazione(
			List<TariffeModalitaErogazioneVO> listaTariffe,
			String codiceErogazione) {

		for (TariffeModalitaErogazioneVO tariffa : listaTariffe) {
			if (tariffa.getCodErog() != null
					&& tariffa.getCodErog().equals(codiceErogazione))
				return tariffa;
		}

		return null;
	}

	protected SupportoBibliotecaVO getSupporto(
			List<SupportoBibliotecaVO> listaSupporti, String codiceSupporto) {

		for (SupportoBibliotecaVO supporto : listaSupporti) {
			if (supporto.getCodSupporto() != null
					&& supporto.getCodSupporto().equals(codiceSupporto))
				return supporto;
		}
		return null;
	}


	protected void cancellaMultipla(List<Long> list, String uteVar, HttpServletRequest request) throws Exception {
		if (!ValidazioneDati.isFilled(list))
			return;

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		delegate.cancellaRichieste(list.toArray(new Long[0]), uteVar);
	}

	protected void rifiutaMultipla(List<Long> list, String uteVar, HttpServletRequest request) throws Exception {
		if (!ValidazioneDati.isFilled(list))
			return;

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		delegate.rifiutaRichieste(list.toArray(new Long[0]), uteVar, getOperatore(request) == OperatoreType.BIBLIOTECARIO);
	}

	protected ActionForward prenotaSolleciti(ActionMapping mapping, Long[] codSolMul,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String idBatch = null;

		try {
			resetToken(request);

			// Prova export
			FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

			// Setting biblioteca corrente (6 caratteri)
			// codice polo + codice biblioteca
			UserVO utenteCollegato = Navigation.getInstance(request).getUtente();
			ParametriBatchSollecitiVO richiesta = new ParametriBatchSollecitiVO();
			richiesta.setCodPolo(utenteCollegato.getCodPolo());
			richiesta.setCodBib(utenteCollegato.getCodBib());
			richiesta.setUser(utenteCollegato.getUserId());
			richiesta.setDataScadenza(DaoManager.now());
			richiesta.setDescrizioneBiblioteca(utenteCollegato.getBiblioteca());
			richiesta.setCodAttivita(CodiciAttivita.getIstance().SERVIZI_SOLLECITI);

			richiesta.setListaRichieste(codSolMul);

			richiesta.validate();

			List<ModelloStampaVO> modelli = CodiciProvider.getModelliStampaPerAttivita(CodiciAttivita.getIstance().SERVIZI_SOLLECITI);
			if (!ValidazioneDati.isFilled(modelli)) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.stampe.modelli.conf"));

				resetToken(request);
				return mapping.getInputForward();
			}

			String jrxml = modelli.get(0).getJrxml();

			String basePath = this.servlet.getServletContext().getRealPath(File.separator);
			// percorso dei file template: webroot/jrxml/\tab\tab\tab\par
			String pathJrxml = basePath + File.separator + "jrxml" + File.separator + jrxml;
			richiesta.setTemplate(pathJrxml);
			richiesta.setTipoStampa(TipoStampa.PDF.name());

			idBatch = factory.getElaborazioniDifferite().prenotaElaborazioneDifferita(utenteCollegato.getTicket(), richiesta, null);

			if (ValidazioneDati.isFilled(idBatch) )
				LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.ok", idBatch));
			 else
				LinkableTagUtils.addError(request, new ActionMessage("errors.prenotazione.failed"));

		} catch (SbnBaseException e) {
			log.error("", e);
			LinkableTagUtils.addError(request, e);

		}

		return mapping.getInputForward();

	}

	protected ControlloAttivitaServizio primoPassoIter(
			List<ControlloAttivitaServizio> listaAttivita) {

		Iterator<ControlloAttivitaServizio> iterator = listaAttivita.iterator();
		ControlloAttivitaServizio attivita = null;
		boolean trovato = false;
		while (iterator.hasNext() && !trovato) {
			attivita = iterator.next();
			if (attivita.getPassoIter().getProgrIter() == 1) {
				trovato = true;
			}
		}

		if (trovato)
			return attivita;
		else
			return null;
	}


	protected MovimentoListaVO preparaMovimentoPerEsame(HttpServletRequest request, MovimentoListaVO movimentoSelezionato,
			MovimentoVO movRicerca, UtenteBaseVO utenteBaseVO, InfoDocumentoVO infoDocumentoVO) throws Exception {
		// Recupero la Categoria di Fruizione e la Categoria di Riproduzione
		// dal Documento e dai Parametri di Biblioteca per impostarla in MovimentoVO

		ParametriBibliotecaVO parametri = this.getParametriBiblioteca(
				movimentoSelezionato.getCodPolo(), movimentoSelezionato
						.getCodBibOperante(), request);

		if (movimentoSelezionato.isRichiestaSuInventario()) {
			// la Categoria di Fruizione del documento SBN è già impostata

			if (ValidazioneDati.isFilled(movimentoSelezionato.getCat_riproduzione()) ) {
				// la Categoria di Riproduzione del documento SBN è già impostata
			}
			else {
				// se il documento SBN non ha impostata la Categoria di Riproduzione
				// la imposto da quella di default dei Parametri di Biblioteca
				movimentoSelezionato.setCat_riproduzione(parametri.getCodRiproduzione());
			}

		}
		if (movimentoSelezionato.isRichiestaSuSegnatura()) {
			if (ValidazioneDati.isFilled(movimentoSelezionato.getCat_fruizione()) ) {
				// la Categoria di Fruizione del documento non SBN è già impostata
			}
			else {
				// se il documento non SBN non ha impostata la Categoria di Fruizione
				// la imposto da quella di default dei Parametri di Biblioteca
				movimentoSelezionato.setCat_fruizione(parametri.getCodFruizione());
			}
			// imposto la Categoria di Riproduzione dalla Categoria di Riproduzione di default
			// dei Parametri di Biblioteca perchè non presente sul documento non SBN
			movimentoSelezionato.setCat_riproduzione(parametri.getCodRiproduzione());
		}


		movimentoSelezionato.setNuovoMov(false);
		request.setAttribute(NavigazioneServizi.MOVIMENTO_SELEZIONATO, movimentoSelezionato.clone());
		request.setAttribute(ServiziDelegate.PARAMETRI_RICERCA_AUTORIZZAZIONI, movRicerca);
		//request.setAttribute(ServiziBaseAction.LISTA_SERVIZI_ATTR, currentForm.getLstCodiciServizio());
		request.setAttribute(NavigazioneServizi.DATI_UTENTE_LETTORE,  utenteBaseVO);
		request.setAttribute(NavigazioneServizi.DATI_DOCUMENTO, infoDocumentoVO);

		request.setAttribute(NavigazioneServizi.PROVENIENZA, "ListaMov");

		return movimentoSelezionato;
	}

	protected ActionForward preparaRicercaPuntualeSegnatura(
			HttpServletRequest request, ActionMapping mapping,
			MovimentoVO movimento, InfoDocumentoVO info, String segnRicerca,
			String titoloDocAltraBib) throws Exception {

		//da attivare solo se segnatura é valorizzato e non ho già in sessione un doc. completo
		boolean ricercaSegn = ValidazioneDati.isFilled(segnRicerca);
		boolean ricercaTit = ValidazioneDati.isFilled(titoloDocAltraBib);
		if (!ricercaSegn && !ricercaTit)
			return null;

		if (ricercaSegn && ricercaTit) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.listaMovimenti.impostareSoloUnCanale"));
			return mapping.getInputForward();
		}

		String bibDocLett = movimento.getCodBibDocLett();
		if (ricercaSegn && ValidazioneDati.strIsNull(bibDocLett)) {
			LinkableTagUtils.addError(request, new ActionMessage("errors.servizi.erroreCodBibliotecaCampoObbligtorio"));
			return mapping.getInputForward();
		} else
			bibDocLett = movimento.getCodBibOperante();

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		String ordSegn = OrdinamentoCollocazione2.normalizza(segnRicerca);
		DocumentoNonSbnVO docNoSbn = delegate.getDettaglioDocumentoNonSbn(movimento.getCodPolo(), bibDocLett, ordSegn);
		if (docNoSbn == null) {
			return mapping.getInputForward();
		} else {
			if (docNoSbn.isNuovo() && (Navigation.getInstance(request).getCache().getElementAt(0).getName().equals("invioElaborazioniDifferiteForm"))){
				throw new ValidationException("DocumentoNonEsistente");
			}
		}

		//preparo vo per invocazione sif
		SIFListaDocumentiNonSbnVO richiesta = new SIFListaDocumentiNonSbnVO();
		richiesta.setCodPolo(movimento.getCodPolo() );
		richiesta.setCodBib(movimento.getCodBibOperante() );
		richiesta.setPuntuale(true);


		if (ricercaSegn) {
			richiesta.setTipoSIF(TipoSIF.DOCUMENTO_POSSEDUTO);
			richiesta.setRequestAttribute(ServiziDelegate.SIF_DOCUMENTI_NON_SBN);
		} else {
			richiesta.setTipoSIF(TipoSIF.DOCUMENTO_ALTRA_BIB);
			richiesta.setRequestAttribute(ServiziDelegate.SIF_DOCUMENTI_NON_SBN_ALTRA_BIB);
			docNoSbn.setTipo_doc_lett('D');
			docNoSbn.setTitolo(titoloDocAltraBib);
		}

		//preparo vo per passaggio parametri gestione doc. non sbn
		ParametriServizi parametri = new ParametriServizi();
		parametri.put(ParamType.MODALITA_CERCA_DOCUMENTO, ModalitaCercaType.CERCA_PER_EROGAZIONE);
		parametri.put(ParamType.PARAMETRI_SIF_DOCNONSBN, richiesta);
		Navigation navi = Navigation.getInstance(request);
		if (docNoSbn.isNuovo() ) {
			// la segnatura ha una categoria di fruizione ma il doc. reale non esiste
			//passo alla mappa di inserimento
			parametri.put(ParamType.MODALITA_GESTIONE_DOCUMENTO, ModalitaGestioneType.CREA_SIF);
			docNoSbn.setSegnatura(segnRicerca);
			docNoSbn.setCodFruizione(null);
			parametri.put(ParamType.DETTAGLIO_DOCUMENTO, docNoSbn );
			ParametriServizi.send(request, parametri);

			navi.addBookmark(richiesta.getRequestAttribute() );
			return navi.goForward(mapping.findForward("nuovoDocumento"));
		}

		if (docNoSbn.countEsemplariAttivi() == 1) {
			//la segnatura immessa ha un doc. esistente con un solo inventario. ho finito
			movimento.setCodBibDocLett(docNoSbn.getCodBib());
			movimento.setTipoDocLett(String.valueOf(docNoSbn.getTipo_doc_lett()));
			movimento.setCodDocLet(docNoSbn.getCod_doc_lett() + "");
			//almaviva5_20130319 #5286
			movimento.setProgrEsempDocLet(ServiziUtil.selezionaEsemplareAttivo(docNoSbn).getPrg_esemplare() + "");

			if (info != null) {
				info.clear();
				info.setDocumentoNonSbnVO(docNoSbn);
				info.setTitolo(docNoSbn.getTitolo());
			}

			return null;

		} else {
			//la segnatura immessa ha più inventari
			//visualizzo il dettaglio per sceglierne uno
			if (Navigation.getInstance(request).getCache().getElementAt(0).getName().equals("invioElaborazioniDifferiteForm")){
				parametri.put(ParamType.MODALITA_GESTIONE_DOCUMENTO, ModalitaGestioneType.GESTIONE_SIF_BATCH);
			}else{
				parametri.put(ParamType.MODALITA_GESTIONE_DOCUMENTO, ModalitaGestioneType.GESTIONE_SIF);
			}
			parametri.put(ParamType.DETTAGLIO_DOCUMENTO, docNoSbn );
			ParametriServizi.send(request, parametri);
			navi.addBookmark(richiesta.getRequestAttribute() );
			return navi.goForward(mapping.findForward("nuovoDocumento"));
		}

	}

	protected DatiControlloVO eseguiPostControlli(HttpServletRequest request,
			MovimentoVO movimento, ControlloAttivitaServizio passoIter, OperatoreType operatore,
			boolean inoltraPrenotazione, ControlloAttivitaServizioResult previousResult, MessaggioVO messaggio)
			throws ValidationException, Exception {

		DatiControlloVO datiControllo = new DatiControlloVO(
				Navigation.getInstance(request).getUserTicket(),
				movimento,
				operatore,
				inoltraPrenotazione, previousResult);
		datiControllo.setUltimoMessaggio(messaggio);

		passoIter.post(datiControllo);

		List<String> msgSupplementari = datiControllo.getCodiciMsgSupplementari();
		if (ValidazioneDati.isFilled(msgSupplementari) ) {
			//Esistono dei codici messaggi supplementari che il controllo eseguito ha restituito
			this.addErrors(request, msgSupplementari);
			msgSupplementari.clear();
		}

		return datiControllo;

	}

	protected void invioMailUtentiPrenotati(HttpServletRequest request,
			MovimentoVO movimento) throws Exception {

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		//almaviva5_20150701
		MovimentoVO nuovoMov = delegate.inviaNotificaUtentePrenotazione(movimento);
		//almaviva5_20151013 gestione priorità prenotazioni
		if (nuovoMov != null) {
			if (nuovoMov.getIdRichiesta() != movimento.getIdRichiesta()) {
				//una delle prenotazioni sul documento è stata trasformata in movimento.
				LinkableTagUtils.addError(request, new ActionMessage("message.servizi.erogazione.mov.da.prenotazione",
						nuovoMov.getIdRichiesta(), nuovoMov.getCodUte()));
			}
		}
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {

		ServiziDelegate delegate = ServiziDelegate.getInstance(request);
		try {
			if (ValidazioneDati.equals(idCheck, NavigazioneServizi.GESTIONE))
				return delegate.isAbilitatoErogazione();
		} catch (UtenteNotAuthorizedException e) {
			return false;
		} catch (Exception e) {
			log.error("", e);
			return false;
		}

		return true;
	}

	private DatiRichiestaILLVO preparaDatiILL(HttpServletRequest request,
			String codPolo, String codBib, MovimentoVO mov,
			IterServizioVO passoIter, DocumentoNonSbnVO doc) throws Exception {

		DatiRichiestaILLVO dati = mov.getDatiILL() != null ? mov.getDatiILL() : new DatiRichiestaILLVO();

		StatoIterRichiesta statoIniziale = getOperatore(request) == OperatoreType.BIBLIOTECARIO ?
				StatoIterRichiesta.F111_DEFINIZIONE_RICHIESTA :
					StatoIterRichiesta.F100_DEFINIZIONE_RICHIESTA_DA_UTENTE;

		//dati bib
		String reqId = dati.getRequesterId();
		if (!ValidazioneDati.isFilled(reqId)) {
			dati.setRuolo(RuoloBiblioteca.RICHIEDENTE);
			dati.setCurrentState(statoIniziale.getISOCode());
			BibliotecaVO bib = BibliotecaDelegate.getInstance(request).getBiblioteca(codPolo, codBib);
			dati.setRequesterId(bib.getIsil());
			dati.setVia(bib.getIndirizzo());
			dati.setComune(bib.getLocalita());
			dati.setProv(bib.getProvincia());
			dati.setCap(bib.getCap());
			dati.setCd_paese(bib.getPaese());
			dati.setRequester_email(bib.getE_mail());
		}
		//dati utente
		UtenteBaseVO utente = ServiziDelegate.getInstance(request).getUtente(mov.getCodUte());
		dati.setCodUtente(utente.getCodUtente());
		dati.setCognomeNome(utente.getCognomeNome());
		dati.setUtente_email(ServiziUtil.getEmailUtente(utente));

		if (doc != null && !doc.isNuovo())
			dati.setDocumento(doc);

		//servizio ill da serv. locale
		TipoServizioVO tipoServizio = getTipoServizio(codPolo, passoIter.getCodBib(), passoIter.getCodTipoServ(), request);
		ILLServiceType illSrv = ILLServiceType.valueOf(tipoServizio.getCodServizioILL());
		if (illSrv == null)
			throw new ApplicationException(SbnErrorTypes.SRV_TIPO_SERVIZIO_NON_TROVATO,
					CodiciProvider.cercaDescrizioneCodice(
							passoIter.getCodTipoServ(),
							CodiciType.CODICE_TIPO_SERVIZIO_ILL,
							CodiciRicercaType.RICERCA_CODICE_SBN));
		dati.setServizio(illSrv.name());

		dati.setUteIns(mov.getUteIns());
		dati.setTsIns(mov.getTsIns());

		BibliotecaVO bibForn = ValidazioneDati.first(doc.getBiblioteche());
		if (bibForn != null) {
			dati.setResponderId(bibForn.getIsil());
			dati.setBibliotecheFornitrici(ServiziUtil.controllaPrioritaBibliotecheILL(doc.getBiblioteche(),
					Constants.MAX_BIBLIOTECHE_RICHIESTA_ILL));
		}

		//supporto ILL (SupplyMediumType)
		if (!ValidazioneDati.isFilled(dati.getCd_supporto())) {
			Character tipoRecord = doc.getTipoRecord();
			if (ValidazioneDati.isFilled(tipoRecord)) {
				String mediumType = ILLConfiguration2.getInstance().getMediumTypeFromTipoRecord(tipoRecord);
				// se il transactionId è valorizzato sto importando e non inviando una richiesta
				Action action = ValidazioneDati.isFilled(dati.getTransactionId()) ? Action.RECEIVE : Action.SEND;
				String supp = ILLConfiguration2.getInstance().getCodiceSbnFromSupplyMediumType(illSrv, mediumType, action);
				dati.setCd_supporto(supp);
			}
		}

		//TODO mod. erogazione ILL (DeliveryService)
		if (!ValidazioneDati.isFilled(dati.getCod_erog())) {
			//???
		}

		mov.setDatiILL(dati);

		//dati periodico
		if (dati.isInventarioPresente() ) {
			// inventario
			InventarioTitoloVO inv = dati.getInventario();
			mov.setAnnoPeriodico(inv.getAnnoAbb());
			mov.setNumVolume(inv.getNumVol());
		} else {
			//doc non sbn
			doc = dati.getDocumento();
			String annata = doc.getAnnata();
			if (ValidazioneDati.isFilled(annata))
				mov.setAnnoPeriodico(annata);
			mov.setNumVolume(doc.getNum_volume());
			mov.setNumFascicolo(doc.getFascicolo());
			mov.setIntervalloCopia(doc.getPagine());
		}

		Date dataMax = mov.getDataMax();
		if (dataMax == null)
			mov.setDataMax(ValidazioneDati.coalesce(dati.getDataMassima(), dati.getDataScadenza()));

		return dati;
	}

	protected ILLServiceType checkConfigurazioneServizioILL(TipoServizioVO tipoSrv) throws Exception {
		CodTipoServizio ts = CodTipoServizio.of(CodiciProvider.cercaCodice(tipoSrv.getCodiceTipoServizio(), CodiciType.CODICE_TIPO_SERVIZIO,
				CodiciRicercaType.RICERCA_CODICE_SBN));
		boolean ill = ts != null && ts.isILL();
		if (ill) {
			if (tipoSrv.isILL())
				return ILLServiceType.valueOf(tipoSrv.getCodServizioILL());
		}

		return null;
	}

}
