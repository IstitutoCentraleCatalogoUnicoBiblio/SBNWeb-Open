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

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.remote.GestioneCodici;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ticket.TicketChecker;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

public class GestioneCodiciBean extends TicketChecker implements GestioneCodici {


	private static final long serialVersionUID = -6925315193342105790L;
	private static Logger log = Logger.getLogger(GestioneCodici.class);

	public void setSessionContext(SessionContext context) throws EJBException,
			RemoteException {
		return;
	}

	public void ejbActivate() throws EJBException, RemoteException {
		return;
	}

	public void ejbPassivate() throws EJBException, RemoteException {
		return;
	}

	public void ejbRemove() throws EJBException, RemoteException {
		return;
	}

	public void ejbCreate() {
		log.debug("creata istanza ejb");
		return;
	}

	public void reload() throws EJBException {
		CodiciProvider.invalidate();
	}


	public List getCodici(CodiciType TB_TABELLA) throws EJBException {

		return internalGetCodici(TB_TABELLA, true, false);
	}

	public List getCodici(CodiciType TB_TABELLA, boolean soloAttivi) throws EJBException {

		return internalGetCodici(TB_TABELLA, true, soloAttivi);
	}

	public List<TB_CODICI> getCodiciCross(CodiciType tpTabellaP,
			String codiceP, boolean soloAttivi) throws EJBException {
		try {
			return CodiciProvider.getCodiciCross(tpTabellaP, codiceP, soloAttivi);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public List<TB_CODICI> getCodiciCross(CodiciType tpTabellaP,
			String codiceP) throws EJBException {
		return getCodiciCross(tpTabellaP, codiceP, false);
	}

	private List internalGetCodici(CodiciType TB_TABELLA, boolean withBlank, boolean soloAttivi) throws EJBException {
		List<TB_CODICI> listaCodici;
		try {
			listaCodici = CodiciProvider.getCodici(TB_TABELLA, soloAttivi);

		if (!withBlank) {
			List<TB_CODICI> clone = listaCodici.subList(1, listaCodici.size());
			return Collections.unmodifiableList(clone);
		}

		return
			Collections.unmodifiableList(listaCodici);

		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public List<ModelloStampaVO> getModelliStampaPerAttivita(String codAttivita) throws EJBException {

		if (ValidazioneDati.strIsNull(codAttivita))
			return null;

		try {
			return CodiciProvider.getModelliStampaPerAttivita(codAttivita);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public ModelloStampaVO getModelloStampa(int idModello) throws EJBException {
		try {
			return CodiciProvider.getModelloStampa(idModello);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public ModelloStampaVO getModelloStampa(String jrxml) throws EJBException {

		if (ValidazioneDati.strIsNull(jrxml))
			return null;

		try {
			return CodiciProvider.getModelloStampa(jrxml);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}


	public List getTipoLegameTraDescrittori() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_LEGAME_DESCRITTORE_DESCRITTORE);
		return lst;
	}

	/**
	 * codici documento fisico
	 *
	 * @return lst
	 */
	public List getCodiceTipoSezione() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPO_SEZIONE);
		return lst;
	}

	public List getCodiceTipoCollocazione() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPO_COLLOCAZIONE);
		return lst;
	}

	public List getCodiceStatoConservazione() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_STATI_DI_CONSERVAZIONE);
		return lst;
	}

	public List getCodiceMotivoScarico() throws EJBException {
		List lst = this
				.getCodici(CodiciType.CODICE_MOTIVI_DI_SCARICO_INVENTARIALE);
		return lst;
	}

	public List getCodiceMotivoCarico() throws EJBException {
		List lst = this
				.getCodici(CodiciType.CODICE_MOTIVI_DI_CARICO_INVENTARIALE);
		return lst;
	}

	public List getCodiceTipoMaterialeInventariale() throws EJBException {
		List lst = this
				.getCodici(CodiciType.CODICE_TIPO_MATERIALE_INVENTARIALE);
		return lst;
	}

	public List getCodiceTitoloStudio() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TITOLO_STUDIO);
		return lst;
	}

	public List getCodiceProfessioni() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_PROFESSIONI);
		return lst;
	}

	public List getCodiceNonDisponibilita() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_NON_DISPONIBILITA);
		return lst;
	}

	public List getCodiceCategoriaDiFruizione() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_CATEGORIA_FRUIZIONE);
		return lst;
	}

	public List getCodiceSupportoCopia() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_SUPPORTO_COPIA);
		return lst;
	}

	public List getCodiceTipiAmmessiPerDocum() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPI_RIPRODUZIONE_AMMESSI_PER_DOCUM);
		return lst;
	}

	public List getCodiceAgenziaCatalogazione() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_AGENZIA_CATALOGAZIONE);
		return lst;
	}

	public List getCodiceAltitudineSensore() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_ALTITUDINE_SENSORE);
		return lst;
	}

	public List getCodiceCarattereImmagine() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_CARATTERE_IMMAGINE);
		return lst;
	}

	public List getCodiceCategoriaSatellite() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_CATEGORIA_SATELLITE);
		return lst;
	}

	public List getCodiceColore116() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_COLORE116);
		return lst;
	}

	public List getCodiceColore120() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_COLORE120);
		return lst;
	}

	// Servizi
	public List getCodiceProvenienza() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_PROVENIENZA);
		return lst;
	}

	public List getCodiceTipoPersonaGiuridica() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPO_PERSONA_GIURIDICA);
		return lst;
	}

	public List getCodiceTipoDocumentoDiRiconoscimento() throws EJBException {
		List lst = this
				.getCodici(CodiciType.CODICE_TIPO_DOCUMENTO_DI_RICONOSCIMENTO);
		return lst;
	}

	public List getCodiceProvincia() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_PROVINCE);
		return lst;
	}

	public List getCodiceAteneo() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_ATENEI);
		return lst;
	}


	public List getCodiceStatoMovimento() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_STATO_MOVIMENTO);
		return lst;
	}

	public List getCodiceAttivita() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_ATTIVITA_ITER);
		return lst;
	}

	public List getCodiceModalitaErogazione() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_MODALITA_EROGAZIONE);
		return lst;
	}

	public List getCodiceTipoServizio() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPO_SERVIZIO);
		return lst;
	}

	public List getCodiceTipoSupporto() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_SUPPORTO_COPIA);
		return lst;
	}


	// Fine Servizi

	public List getCodiceDesignazioneFunzione() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_DESIGNAZIONE_FUNZIONE);
		return lst;
	}

	public List getCodiceEdizioneClasse() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_EDIZIONE_CLASSE);
		return lst;
	}

	public List getCodiceElaborazione() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_ELABORAZIONE);
		return lst;
	}

	public List getCodiceFormaDocumentoCartografico() throws EJBException {
		List lst = this
				.getCodici(CodiciType.CODICE_FORMA_DOCUMENTO_CARTOGRAFICO);
		return lst;
	}

	public List getCodiceFormaMusicale() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_FORMA_MUSICALE);
		return lst;
	}

	public List getCodiceFormaPubblicazione() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_FORMA_PUBBLICAZIONE);
		return lst;
	}

	public List getCodiceFormaRiproduzione() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_FORMA_RIPRODUZIONE);
		return lst;
	}

	public List getCodiceGenereMaterialeUnimarc() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_GENERE_MATERIALE_UNIMARC);
		return lst;
	}

	public List getCodiceGenerePubblicazione() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_GENERE_PUBBLICAZIONE);
		return lst;
	}

	public List getCodiceGenereRappresentazione() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_GENERE_RAPPRESENTAZIONE);
		return lst;
	}

	public List getCodiceIndicatoreIncipit() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_INDICATORE_INCIPIT);
		return lst;
	}

	public List getCodiceIndicatoreTiposcala() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_INDICATORE_TIPOSCALA);
		return lst;
	}

	public List getCodiceLegameNaturaTitoloTitolo() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO);
		return lst;
	}

	public List getCodiceLegameTitoloAutore() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_LEGAME_TITOLO_AUTORE);
		return lst;
	}

	public List getCodiceLegameTitoloLuogo() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_LEGAME_TITOLO_LUOGO);
		return lst;
	}

	public List getCodiceLegameTitoloMusica() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_LEGAME_TITOLO_MUSICA);
		return lst;
	}

	public List getCodiceLegameTitoloTitolo() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_LEGAME_TITOLO_TITOLO);
		return lst;
	}

	public List getCodiceLingua() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_LINGUA);
		return lst;
	}

	public List getCodiceLivelloAutorita() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_LIVELLO_AUTORITA);
		return lst;
	}

	public List getCodiceMateria() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_MATERIA);
		return lst;
	}

	public List getCodiceMaterialeBibliografico() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_MATERIALE_BIBLIOGRAFICO, true);
		return lst;
	}

	public List getCodiceMaterialeBibliograficoTutti() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_MATERIALE_BIBLIOGRAFICO);
		return lst;
	}

	public List getCodiceMaterialeGrafico() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_MATERIALE_GRAFICO);
		return lst;
	}

	public List getCodiceMeridianoOrigine() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_MERIDIANO_ORIGINE);
		return lst;
	}

	public List getCodiceNaturaBibliografica() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_NATURA_BIBLIOGRAFICA);
		return lst;
	}

	public List getCodicePaese() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_PAESE);
		return lst;
	}

	public List getCodicePiattaforma() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_PIATTAFORMA);
		return lst;
	}

	public List getCodicePresentazione() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_PRESENTAZIONE);
		return lst;
	}

	public List getCodicePubblicazioneGovernativa() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_PUBBLICAZIONE_GOVERNATIVA);
		return lst;
	}

	public List getCodiceResponsabilita() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_RESPONSABILITA);
		return lst;
	}

	public List getCodiceSistemaClasse() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_SISTEMA_CLASSE);
		return lst;
	}

	public List getCodiceSoggettario() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_SOGGETTARIO);
		return lst;
	}

	public List getCodiceStesura() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_STESURA);
		return lst;
	}

	public List getCodiceStrumentiVociOrganico() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_STRUMENTI_VOCI_ORGANICO);
		return lst;
	}

	public List getCodiceSupportoFisicoCartografico() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_SUPPORTO_FISICO_PER_CARTOGRAFICO);
		return lst;
	}

	public List getCodiceSupportoFisicoGrafica() throws EJBException {
		List lst = this
				.getCodici(CodiciType.CODICE_SUPPORTO_FISICO_PER_GRAFICA);
		return lst;
	}

	public List getCodiceTecnicaCartografia() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TECNICA_CARTOGRAFIA);
		return lst;
	}

	public List getCodiceTecnicaDisegni() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TECNICA_DISEGNI);
		return lst;
	}

	public List getCodiceTecnicaStampe() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TECNICA_STAMPE);
		return lst;
	}

	public List getCodiceTipoAuthority() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPO_AUTHORITY);
		return lst;
	}

	public List getCodiceTipoAutore() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPO_AUTORE);
		return lst;
	}

	public List getCodiceTipoDataPubblicazione() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPO_DATA_PUBBLICAZIONE);
		return lst;
	}

	public List getCodiceTipoNumeroStandard() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPO_NUMERO_STANDARD);
		return lst;
	}

	public List getCodiceTipoTestoLetterario() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPO_TESTO_LETTERARIO);
		return lst;
	}

	public List getCodiceTiposcala() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPOSCALA);
		return lst;
	}

	public List getCodiceTonalita() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TONALITA);
		return lst;
	}

	public List getCodiceValuta() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_VALUTA);
		return lst;
	}

	public List getCodiceStatoMessaggi() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_STATO_MESSAGGI);
		return lst;
	}

	public List getCodiceStatoOrdine() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_STATO_ORDINE);
		return lst;
	}

	public List getCodiceStatoSuggerimento() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_STATO_SUGGERIMENTO);
		return lst;
	}

	public List getCodiceTipoFattura() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPO_FATTURA);
		return lst;
	}

	public List getCodiceIva() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_IVA);
		return lst;
	}

	public List getCodiceTipoInvio() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPO_INVIO);
		return lst;
	}

	public List getCodiceTipoOrdine() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPO_ORDINE);
		return lst;
	}

	public List getCodiceTipiMessaggio() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPI_MESSAGGIO);
		return lst;
	}

	public List getCodiceStatoPartecipantiGara() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_STATO_PARTECIPANTI_GARA);
		return lst;
	}

	public List getCodiceProvenienzeOfferte() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_PROVENIENZE_OFFERTE);
		return lst;
	}

	public List getCodiceTipiBiblioteca() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPI_BIBLIOTECA);
		return lst;
	}

	public List getCodiceTipoUrgenza() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPO_URGENZA);
		return lst;
	}

	public List getCodiceTipoMateriale() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPO_IMPEGNO_ACQUISTO_MATERIALE);
		return lst;
	}

	public List getCodiceNatura() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_NATURA_BIBLIOGRAFICA);
		return lst;
	}

	public List getCodiceNaturaOrdine() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_NATURA_ORDINE);
		return lst;
	}

	public List getCodiceOrdinamentoListaOrdini() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_ORDINAMENTO_LISTA_ORDINI);
		return lst;
	}
	public List getCodiceStatiContinuativo() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_STATI_CONTINUATIVO);
		return lst;
	}

	public List getCodiceProvince() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_PROVINCE);
		return lst;
	}

	public List getCodiceTipoPartner() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPO_PARTNER);
		return lst;
	}

	// THESAURO
	public List getCodiceThesauro() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_THESAURO);
		return lst;
	}

	public List getCodiceMaterialeBibliografico2() throws EJBException {
		List ret = new ArrayList<TB_CODICI>();
		TB_CODICI elementom1 = new TB_CODICI();
		elementom1.setCd_tabella("Altro");
		elementom1.setDs_tabella("");
		TB_CODICI elementom2 = new TB_CODICI();
		elementom2.setCd_tabella("Musica");
		elementom2.setDs_tabella("");
		ret.add(elementom1);
		ret.add(elementom2);
		return ret;
	}

	public List getCodiceSiNo() throws EJBException {
		// Creazione lista codici artificiale(non dedotta da TB_Codici.xml)
		// per la scelta Si/No
		List ret = new ArrayList<TB_CODICI>();
		TB_CODICI elementos1 = new TB_CODICI();
		elementos1.setCd_tabella("S");
		elementos1.setDs_tabella("S");
		TB_CODICI elementos2 = new TB_CODICI();
		elementos2.setCd_tabella("N");
		elementos2.setDs_tabella("N");
		ret.add(elementos1);
		ret.add(elementos2);
		return ret;
	}

	public List getCodiceTipoDigitalizzazione() throws EJBException {
		List lst = this.getCodici(CodiciType.CODICE_TIPO_DIGITALIZZAZIONE);
		return lst;
	}

	public List getCodiceSpecificita() throws EJBException {
		// Creazione lista codici artificiale(non dedotta da TB_Codici.xml)
		// per la scelta MIN/MAX
		List listAppo = new ArrayList();
		listAppo.add(new TB_CODICI("000", ""));
		listAppo.add(new TB_CODICI("001", "presenti"));
		listAppo.add(new TB_CODICI("002", "assenti"));
		return listAppo;
	}

	public List getCodiceNordSud() throws EJBException {
		// Creazione lista codici artificiale(non dedotta da TB_Codici.xml)
		// per la scelta NORD/SUD
		List listAppo = new ArrayList();
		listAppo.add(new TB_CODICI("", ""));
		listAppo.add(new TB_CODICI("N", "NORD"));
		listAppo.add(new TB_CODICI("S", "SUD"));
		return listAppo;
	}
	public List getCodiceEstOvest() throws EJBException {
		// Creazione lista codici artificiale(non dedotta da TB_Codici.xml)
		// per la scelta EST/OVEST
		List listAppo = new ArrayList();
		listAppo.add(new TB_CODICI("", ""));
		listAppo.add(new TB_CODICI("E", "EST"));
		listAppo.add(new TB_CODICI("O", "OVEST"));
		return listAppo;
	}

	public List getCodiceSessoUtente() throws EJBException {
		// Creazione lista codici artificiale(non dedotta da TB_Codici.xml)
		// per la scelta EST/OVEST
		List listAppo = new ArrayList();
		listAppo.add(new TB_CODICI("", ""));
		listAppo.add(new TB_CODICI("M", "Maschio"));
		listAppo.add(new TB_CODICI("F", "Femmina"));
		return listAppo;
	}

	public List getCodiceMinMax() throws EJBException {
		return internalGetCodici(CodiciType.CODICE_TIPO_LISTA_MIN_MAX, false, false);
	}

	public List getCodiceOrdinamentoTitoli() throws EJBException {
		return internalGetCodici(CodiciType.CODICE_ORDINAMENTO_TITOLI, false, false);
	}

	public List getCodiceOrdinamentoAutori() throws EJBException {
		return internalGetCodici(CodiciType.CODICE_ORDINAMENTO_AUTORI, false, false);
	}

	public List getCodiceOrdinamentoMarche() throws EJBException {
		return internalGetCodici(CodiciType.CODICE_ORDINAMENTO_MARCHE, false, false);
	}

	public List getCodiceOrdinamentoLuoghi() throws EJBException {
		return internalGetCodici(CodiciType.CODICE_ORDINAMENTO_LUOGHI, false, false);
	}

	public List getCodiceLivelloRicercaLocale() throws EJBException {
		// Creazione lista codici artificiale(non dedotta da TB_Codici.xml)
		// per la scelta del livello di ricerca
		List listAppo = new ArrayList();
		listAppo.add(new TB_CODICI("", "Polo"));
		listAppo.add(new TB_CODICI("", "02 Centro Sistema"));
		listAppo.add(new TB_CODICI("", "03 Biblioteca Comunale1"));
		listAppo.add(new TB_CODICI("", "AB Biblioteca di AB"));
		listAppo.add(new TB_CODICI("", "CM Centro Multimediale"));
		return listAppo;
	}

	public List getCodiceTipoLocalizzazione(String tipologia) throws EJBException {
		// Creazione lista codici artificiale(non dedotta da TB_Codici.xml)
		// per la scelta del livello di ricerca

 		// Evolutiva Google3: Revisione Operazioni di Servizio che verranno suddivise fra operatori che potranno effettuare SOLO
		// localizza/delocalizza per Gestione per tutte le Biblio del Polo e operatori che potranno effettuare SOLO localizza/delocalizza
		// per Possesso e variare gli attributi di possesso Solo per la biblioteca di appartenenza
		// Inseriti i nuovi caricamenti NESGES, POSTUTTI, NESPOS, GESTUTTI;

		List listAppo = new ArrayList();

		if (tipologia.equals("") || tipologia.equals("ALL")) {
			listAppo.add(new TB_CODICI("", "Nessuna"));
			listAppo.add(new TB_CODICI("", "Gestione"));
			listAppo.add(new TB_CODICI("", "Possesso"));
			listAppo.add(new TB_CODICI("", "Possesso/Gestione"));
		} else if (tipologia.equals("POS")) {
			listAppo.add(new TB_CODICI("", "Nessuna"));
			listAppo.add(new TB_CODICI("", "Gestione"));
		} else if (tipologia.equals("POLO")) {
			listAppo.add(new TB_CODICI("", "Nessuna"));
			listAppo.add(new TB_CODICI("", "Possesso"));
		} else if (tipologia.equals("NESSUNA")) {
			listAppo.add(new TB_CODICI("", "Nessuna"));
		} else if (tipologia.equals("NESGES")) {
			listAppo.add(new TB_CODICI("", "Nessuna"));
			listAppo.add(new TB_CODICI("", "Gestione"));
		} else if (tipologia.equals("POSTUTTI")) {
			listAppo.add(new TB_CODICI("", "Possesso"));
			listAppo.add(new TB_CODICI("", "Possesso/Gestione"));
		} else if (tipologia.equals("NESPOS")) {
			listAppo.add(new TB_CODICI("", "Nessuna"));
			listAppo.add(new TB_CODICI("", "Possesso"));
		} else if (tipologia.equals("GESTUTTI")) {
			listAppo.add(new TB_CODICI("", "Gestione"));
			listAppo.add(new TB_CODICI("", "Possesso/Gestione"));
		} else if (tipologia.equals("SOLOPOS")) {
			listAppo.add(new TB_CODICI("", "Possesso"));
		} else if (tipologia.equals("SOLOTUTTI")) {
			listAppo.add(new TB_CODICI("", "Possesso/Gestione"));
		}
		return listAppo;
	}

	/**
	 * Utilizzato per richiedere i valori possibili di una determinata tipologia
	 * di codici, solitamente per popolare dei JComboBox.
	 *
	 * @return un List di String relative ai codici del tipo specificato
	 */
	public List getVettoreCodici(CodiciType tipoCodice) throws EJBException {
		List codici = new ArrayList();
		codici.add(" ");
		List lista = this.getCodici(tipoCodice);
		Iterator iterator = lista.iterator();
		while (iterator.hasNext()) {
			TB_CODICI elementoCodice = (TB_CODICI) iterator.next();
			String e1 = (elementoCodice.getCd_tabella());
			codici.add(e1);
		}
		return codici;
	}

	/**
	 * Ritorna una List(List) di un solo elemento di tipo ElementoCodice
	 *
	 * @param tipoCodice
	 *            tipo del codice da cercare
	 * @param codice
	 *            valore del codice da cercare
	 * @return una List contenente un solo elemento di tipo ElementoCodice
	 *         corrispondente al codice cercato.
	 */
	public List getListaCodice(CodiciType tipoCodice, String codice)
			throws EJBException {
		List lista = this.getCodici(tipoCodice);
		List listaFiltrata = new ArrayList();
		Iterator iterator = lista.iterator();
		TB_CODICI elementoCodice = null;
		while (iterator.hasNext()) {
			elementoCodice = (TB_CODICI) iterator.next();
			if (elementoCodice.getCd_tabella().trim().equals(codice)) {
				listaFiltrata.add(elementoCodice);
				break;
			}
		}
		return
			Collections.unmodifiableList(listaFiltrata);
	}

	/**
	 * Ritorna una List(List) di elementi di tipo ElementoCodice
	 *
	 * @param tipoCodice
	 *            tipo del codice da cercare
	 * @param codici
	 *            lista di codici da cercare
	 * @return una List contenente elementi di tipo ElementoCodice
	 *         corrispondenti ai codici cercati.
	 */
	public List getListaCodice(CodiciType tipoCodice, List codici)
	throws EJBException {
		List lista = this.getCodici(tipoCodice);
		List listaFiltrata = new ArrayList();
		Iterator iterator = lista.iterator();
		TB_CODICI elementoCodice = null;
		while (iterator.hasNext()) {
			elementoCodice = (TB_CODICI) iterator.next();

			if ( codici.contains(elementoCodice.getCd_tabella().trim()) ) {
				listaFiltrata.add(elementoCodice);
			}
		}
		return 	Collections.unmodifiableList(listaFiltrata);
	}

	/**
	 * Ritorna una List(List) di elementi di tipo ElementoCodice
	 *
	 * @param tipoCodice
	 *            tipo del codice da cercare
	 * @param codici
	 *            lista di codici da cercare
	 * @return una List contenente elementi di tipo ElementoCodice
	 *         i cui codici tipo non sono contenuti in codici
	 */
	public List getListaCodiceDiversiDa(CodiciType tipoCodice, List codici)
	throws EJBException {
		List lista = this.getCodici(tipoCodice);
		List listaFiltrata = new ArrayList();
		Iterator iterator = lista.iterator();
		TB_CODICI elementoCodice = null;
		while (iterator.hasNext()) {
			elementoCodice = (TB_CODICI) iterator.next();

			if ( !codici.contains(elementoCodice.getCd_tabella().trim()) ) {
				listaFiltrata.add(elementoCodice);
			}
		}
		return 	Collections.unmodifiableList(listaFiltrata);
	}


	/**
	 * Ritorna una lista di codici richiesti mediante una delle tipologie
	 * possibili rappresentate mediante le costanti presenti in questa classe, i
	 * codici ritornati sono però solamente quelli che possiedono il tipo
	 * materiale specificato. Tipi Materiale:
	 * <ul>
	 * <li>SbnMateriale.VALUE_0: Moderno M</li>
	 * <li>SbnMateriale.VALUE_1: Antico E</li>
	 * <li>SbnMateriale.VALUE_2: Musica U</li>
	 * <li>SbnMateriale.VALUE_3: Grafico G</li>
	 * <li>SbnMateriale.VALUE_4: Cartografico C</li>
	 * </ul>
	 *
	 * @param tipoCodice
	 *            tipo di codici dei quali la lista sarà composta
	 * @param tipoMateriale
	 *            tipo materiale dei codici dei quali la lista sarà composta
	 * @return una List di ElementoCodice relativa al tipo di codice e al tipo
	 *         materiale specificati
	 */
	public List getListaCodice(CodiciType tipoCodice, SbnMateriale tipoMateriale)
			throws EJBException {

		// almaviva2 febbraio 2015: cambiato l'oggetto SbnMateriale tutti i "VALUE" fissi devono esssere modificati con il "valueOf"
		// if (tipoMateriale == SbnMateriale.VALUE_5)
		if (tipoMateriale == SbnMateriale.valueOf(" "))
			return this.getCodici(tipoCodice);

		List lista = this.getCodici(tipoCodice);
		List listaFiltrata = new ArrayList();
		listaFiltrata.add(new TB_CODICI("", ""));
		String codice = tipoMateriale.toString();
		Iterator iterator = lista.iterator();
		while (iterator.hasNext()) {
			TB_CODICI elementoCodice = (TB_CODICI) iterator.next();
			if (codice.equals(elementoCodice.getTp_materiale())) {
				listaFiltrata.add(elementoCodice);
			}
		}
		return
			Collections.unmodifiableList(listaFiltrata);
	}

	/**
	 * Ritorna una lista di codici richiesti mediante una delle tipologie
	 * possibili rappresentate mediante le costanti presenti in questa classe.
	 *
	 * @param tipo
	 *            di codici dei quali la lista sarà composta
	 * @return una List di ElementoCodice relativa al tipo di codice specificato
	 */
	public List getListaCodice(CodiciType tipoCodice) throws EJBException {
		try {
			return this.getCodici(tipoCodice);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Dato il valore di una costante intera che rappresenta un tipo di codice
	 * fornisce il nome della relativa tabella del DB
	 *
	 * @param costante
	 *            che indica il tipo di codice
	 * @return il nome della tabella del DB che corrisponde al codice
	 */
	private String getNomeTabellaDaTipoCodice(CodiciType tipoCodice)
			throws EJBException {
		return tipoCodice.getTp_Tabella();
	}

	/**
	 * Cerca la descrizione di un determinato codice
	 *
	 * @param codiceUnimarc
	 *            codice unimarc
	 * @param tipoCodice
	 *            tipo del codice
	 * @return la descrizione di un codice
	 */
	public String getDescrizioneCodice(CodiciType tipoCodice,
			String codiceUnimarc) throws EJBException {
		TB_CODICI elem = this.cercaCodice(codiceUnimarc, tipoCodice, CodiciRicercaType.RICERCA_CODICE_UNIMARC);
		if (elem != null)
			return (elem.getDs_tabella().trim());
		else
			return null;
	}

	public String getDescrizioneCodiceSBN(CodiciType tipoCodice,
			String codiceSBN) throws EJBException {
		TB_CODICI elem = this.cercaCodice(codiceSBN, tipoCodice, CodiciRicercaType.RICERCA_CODICE_SBN);
		if (elem != null)
			return (elem.getDs_tabella().trim());
		else
			return null;
	}

	/**
	 *
	 * @param tipoCodice
	 *            nome della tabella contenente il codice cercato
	 * @param codiceUnimarc
	 *            valore unimarc del codice
	 * @return la descrizione del codice di Cd_tabella
	 */
	public String getDescrizioneCodiceTabella(CodiciType tipoCodice,
			String codiceUnimarc) throws EJBException {
		CodiciType tipoCod = tipoCodice;
		if (tipoCod != null) {
			// return getDescrizioneCodice(tipoCod, codiceUnimarc);
			TB_CODICI elem = cercaCodice(codiceUnimarc, tipoCod,
					CodiciRicercaType.RICERCA_CODICE_UNIMARC);
			if (elem != null) {
				String elementoCompleto = (elem.getCd_tabella());
				elementoCompleto = elementoCompleto.substring(1, 3);
				return elementoCompleto;
			}

		}
		return null;
	}

	/**
	 * Ricerca di un codice nelle liste dei codici
	 *
	 * @param codiceRicerca
	 *            codice da ricercare
	 * @param tipoRicerca
	 *            ricerca per codice SBN o per codice UNIMARC(RICERCA_CODICE_SBN
	 *            o RICERCA_CODICE_UNIMARC)
	 * @return una riga della tabella dei codici, rappresentata da una classe di
	 *         tipo ElementoCodice
	 */
	public TB_CODICI cercaCodice(String codiceRicerca, CodiciType tipoCodice,
			CodiciRicercaType tipoRicerca) throws EJBException {
		try {
			return CodiciProvider.cercaCodice(codiceRicerca, tipoCodice, tipoRicerca);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Conversione di un codice UNIMARC nel corrispondente codice SBN
	 *
	 * @param tipoCodice
	 *            tipo di codice da convertire
	 * @param codiceUnimarc
	 *            codice unimarc da convertire
	 * @return il codice SBN corrispondente
	 */
	public String unimarcToSBN(CodiciType tipoCodice, String codiceUnimarc)
			throws EJBException {
		// La stringa vuota non corrisponde ad alcun codice e viene ritornata
		// non convertita
		if (codiceUnimarc == null)
			return null;
		if (("".equals(codiceUnimarc)) || (!tipoCodiceEffettivo(tipoCodice)))
			return codiceUnimarc;
		// Ricerca del codice UNIMARC
		TB_CODICI codice = cercaCodice(codiceUnimarc, tipoCodice,
				CodiciRicercaType.RICERCA_CODICE_UNIMARC);
		try {
			// Viene ritornato il codice SBN corrispondente
			return
				(codice.getCd_tabella().trim());
		} catch (NullPointerException e) {
			return codiceUnimarc;
		}
	}

	/**
	 * Conversione di un codice SBN in un codice UNIMARC
	 *
	 * @param tipoCodice
	 *            tipo di codice da convertire
	 * @param codiceSBN
	 *            il codice SBN da convertire
	 * @return il codice UNIMARC corrispondente
	 */
	public String SBNToUnimarc(CodiciType tipoCodice, String codiceSBN)
			throws EJBException {
		// La stringa vuota non corrisponde ad alcun codice e viene ritornata
		// non convertita
		if (codiceSBN == null)
			return null;
		if (("".equals(codiceSBN)) || (!tipoCodiceEffettivo(tipoCodice)))
			return codiceSBN;

		// Ricerca del codice SBN
		TB_CODICI codice = cercaCodice(codiceSBN, tipoCodice,
				CodiciRicercaType.RICERCA_CODICE_SBN);
		try {
			// Viene ritornato il codice UNIMARC corrispondente
			return
				(codice.getCd_unimarc().trim());
		} catch (NullPointerException e) {
			return codiceSBN;
		}
	}

	/**
	 * Filtra la tabella LICR prendendo solamente i codici SBN che iniziano per
	 * <code>prefisso</code> e finiscono per <code>suffisso</code>.
	 *
	 * @param prefisso
	 *            carattere iniziale dei codici che si desiderano
	 * @param suffisso
	 *            carattere finale dei codici che si desiderano
	 * @return la lista dei codici LICR filtrata per il carattere iniziale ed il
	 *         carattere finale specificati
	 */
	public List getLICR(String prefisso, String suffisso) throws EJBException {
		// Lista completa dei codici LICR
		List legami = this.getCodiceLegameNaturaTitoloTitolo();
		// Vettore ritornato contenente i soli codici che rispettano sia il
		// prefisso che il suffisso
		List out = new ArrayList();
		for (int i = 0; i < legami.size(); i++) {
			TB_CODICI elem = (TB_CODICI) legami.get(i);
			// Il test si effettua sul codice SBN(Cd_tabella)
			String codice = elem.getCd_tabella().trim();
			// Se il codice rispetta il prefisso ed il suffisso allora viene
			// inserito nel vettore
			if (codice.startsWith(prefisso) && codice.endsWith(suffisso)) {
				out.add(elem);
			}
		}
		return Collections.unmodifiableList(out);
	}

	public List getLICR(String suffisso) throws EJBException {
		// Lista completa dei codici LICR
		List legami = this.getCodiceLegameNaturaTitoloTitolo();
		// Vettore ritornato contenente i soli codici che rispettano sia il
		// prefisso che il suffisso
		List out = new ArrayList();
		for (int i = 0; i < legami.size(); i++) {
			TB_CODICI elem = (TB_CODICI) legami.get(i);
			// Il test si effettua sul codice SBN(Cd_tabella)
			String codice = elem.getCd_tabella();
			// Se il codice rispetta il prefisso ed il suffisso allora viene
			// inserito nel vettore
			if (codice.endsWith(suffisso)) {
				out.add(elem);
			}
		}
		return Collections.unmodifiableList(out);
	}

	/**
	 *
	 * @param tipoCodice
	 *            una delle costanti di tipo codice
	 * @return true se il codice è contenuto in TB_Codici.xml false se è un tipo
	 *         codice "sintetizzato"
	 */
	public boolean tipoCodiceEffettivo(CodiciType tipoCodice)
			throws EJBException {
		return (CodiciType.CODICE_MATERIALE_BIBLIOGRAFICO2 != tipoCodice)
				&& (CodiciType.CODICE_SI_NO != tipoCodice)
				&& (CodiciType.CODICE_REPERTORIO_MARCHE != tipoCodice)
				&& (CodiciType.CODICE_MATERIALE_BIBLIOGRAFICO_TUTTI != tipoCodice);
	}

	public String cercaDescrizioneCodice(String codiceRicerca,
			CodiciType tipoCodice, CodiciRicercaType tipoRicerca) throws EJBException {
		try {
			return CodiciProvider.cercaDescrizioneCodice(codiceRicerca, tipoCodice, tipoRicerca);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

}
