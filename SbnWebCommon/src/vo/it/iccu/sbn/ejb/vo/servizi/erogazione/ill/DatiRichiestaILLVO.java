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
package it.iccu.sbn.ejb.vo.servizi.erogazione.ill;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.unicode.OrdinamentoUnicode;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.util.cloning.ClonePool;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DatiRichiestaILLVO extends BaseVO {

	private static final long serialVersionUID = 3222402319847954883L;

	// Utenti locali
	// Biblioteche richiedenti e destinatarie, è impossibile vista che il dato non sta né nella sintetica né nel dettaglio?
	// Tipo di servizio (prestito o riproduzioni)
	// Date (una volta che ci siamo chiariti).

	public static final Comparator<DatiRichiestaILLVO> ORDINA_PER_COGNOME_NOME = new Comparator<DatiRichiestaILLVO>() {
		OrdinamentoUnicode u = new OrdinamentoUnicode();
		public int compare(DatiRichiestaILLVO o1, DatiRichiestaILLVO o2) {
			return u.convert(o1.cognomeNome).compareTo(u.convert(o2.cognomeNome));
		}
	};

	public static final Comparator<DatiRichiestaILLVO> ORDINA_PER_BIB_RICHIEDENTE = new Comparator<DatiRichiestaILLVO>() {
		public int compare(DatiRichiestaILLVO o1, DatiRichiestaILLVO o2) {
			return ValidazioneDati.compare(o1.requesterId, o2.requesterId);
		}
	};

	public static final Comparator<DatiRichiestaILLVO> ORDINA_PER_BIB_FORNITRICE = new Comparator<DatiRichiestaILLVO>() {
		public int compare(DatiRichiestaILLVO o1, DatiRichiestaILLVO o2) {
			return ValidazioneDati.compare(o1.responderId, o2.responderId);
		}
	};

	public static final Comparator<DatiRichiestaILLVO> ORDINA_PER_SERVIZIO = new Comparator<DatiRichiestaILLVO>() {
		public int compare(DatiRichiestaILLVO o1, DatiRichiestaILLVO o2) {
			return ValidazioneDati.compare(o1.servizio, o2.servizio);
		}
	};

	public final static Comparator<DatiRichiestaILLVO> ORDINA_PER_DATA_AGG = new Comparator<DatiRichiestaILLVO>() {
		public int compare(DatiRichiestaILLVO dri1, DatiRichiestaILLVO dri2) {
			return dri1.getTsVar().compareTo(dri2.getTsVar());
		}
	};

	private int idRichiestaILL;

	private long cod_rich_serv;

	private long transactionId;
	private String requesterId;
	private String responderId;
	private String currentState;
	private String servizio;

	private String codUtente;
	private String cognomeNome;
	private String utente_email;

	private String via;
	private String comune;
	private String prov;
	private String cap;
	private String cd_paese;
	private String requester_email;
	private String responder_email;

	private Date dataDesiderata;
	private Date dataMassima;
	private Date dataScadenza;

	private String cod_erog;
	private String cd_supporto;
	private String cd_valuta;
	private Number importo;
	private Number costoMax;

	private RuoloBiblioteca ruolo;
	private String codUtenteBibRichiedente;
	private String denominazioneBibRichiedente;

	private Timestamp ultimoCambioStato;

	private DocumentoNonSbnVO documento;
	private InventarioTitoloVO inventario;
	private List<BibliotecaVO> bibliotecheFornitrici = new ArrayList<BibliotecaVO>();

	private List<MessaggioVO> messaggio = new ArrayList<MessaggioVO>();

	private Timestamp dataInizio;
	private Timestamp dataFine;

	private String intervalloCopia;

	private MovimentoVO movimento;

	private Date dataRinnovo;
	private Date dataRientroPrevisto;


	public int getIdRichiestaILL() {
		return idRichiestaILL;
	}

	public void setIdRichiestaILL(int idRichiestaILL) {
		this.idRichiestaILL = idRichiestaILL;
	}

	public long getCod_rich_serv() {
		return cod_rich_serv;
	}

	public void setCod_rich_serv(long cod_rich_serv) {
		this.cod_rich_serv = cod_rich_serv;
	}

	public DatiRichiestaILLVO(DatiRichiestaILLVO dr) {
		super(dr);
		this.idRichiestaILL = dr.idRichiestaILL;
		this.cod_rich_serv = dr.cod_rich_serv;
		this.transactionId = dr.transactionId;
		this.requesterId = dr.requesterId;
		this.responderId = dr.responderId;
		this.currentState = dr.currentState;
		this.servizio = dr.servizio;
		this.codUtente = dr.codUtente;
		this.cognomeNome = dr.cognomeNome;
		this.utente_email = dr.utente_email;
		this.via = dr.via;
		this.comune = dr.comune;
		this.prov = dr.prov;
		this.cap = dr.cap;
		this.cd_paese = dr.cd_paese;
		this.requester_email = dr.requester_email;
		this.responder_email = dr.responder_email;
		this.dataDesiderata = dr.dataDesiderata;
		this.dataMassima = dr.dataMassima;
		this.dataScadenza = dr.dataScadenza;
		this.cod_erog = dr.cod_erog;
		this.cd_supporto = dr.cd_supporto;
		this.cd_valuta = dr.cd_valuta;
		this.importo = dr.importo;
		this.costoMax = dr.costoMax;
		this.ruolo = dr.ruolo;
		this.codUtenteBibRichiedente = dr.codUtenteBibRichiedente;
		this.denominazioneBibRichiedente = dr.denominazioneBibRichiedente;
		this.ultimoCambioStato = dr.ultimoCambioStato;
		this.documento = dr.documento;
		this.inventario = dr.inventario;

		this.messaggio = dr.messaggio;
		this.dataInizio = dr.dataInizio;
		this.dataFine = dr.dataFine;
		this.movimento = dr.movimento;

		this.intervalloCopia = dr.intervalloCopia;

		this.dataRinnovo = dr.dataRinnovo;
		this.dataRientroPrevisto = dr.dataRientroPrevisto;

		try {
			this.bibliotecheFornitrici = ClonePool.deepCopy(dr.bibliotecheFornitrici);
		} catch (Exception e) {
			this.bibliotecheFornitrici = null;
		}
	}

	public DatiRichiestaILLVO() {
		super();
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long id) {
		this.transactionId = id;
	}

	public String getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(String requesterId) {
		this.requesterId = trimAndSet(requesterId);
	}

	public String getResponderId() {
		return responderId;
	}

	public void setResponderId(String responderId) {
		this.responderId = trimAndSet(responderId);
	}

	public String getCodUtente() {
		return codUtente;
	}

	public void setCodUtente(String codUtente) {
		this.codUtente = trimAndSet(codUtente);
	}

	public String getCognomeNome() {
		return cognomeNome;
	}

	public void setCognomeNome(String cognomeNome) {
		this.cognomeNome = trimOrEmpty(cognomeNome);
	}

	public String getUtente_email() {
		return utente_email;
	}

	public void setUtente_email(String utente_email) {
		this.utente_email = trimAndSet(utente_email);
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = trimAndSet(via);
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = trimAndSet(comune);
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = trimAndSet(prov);
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = trimAndSet(cap);
	}

	public String getCd_paese() {
		return cd_paese;
	}

	public void setCd_paese(String cd_paese) {
		this.cd_paese = trimAndSet(cd_paese);
	}

	public String getRequester_email() {
		return requester_email;
	}

	public void setRequester_email(String requester_email) {
		this.requester_email = trimAndSet(requester_email);
	}

	public String getResponder_email() {
		return responder_email;
	}

	public void setResponder_email(String responder_email) {
		this.responder_email = trimAndSet(responder_email);
	}

	public Date getDataDesiderata() {
		return dataDesiderata;
	}

	public void setDataDesiderata(Date dataDesiderata) {
		this.dataDesiderata = dataDesiderata;
	}

	public Date getDataMassima() {
		return dataMassima;
	}

	public void setDataMassima(Date dataMassima) {
		this.dataMassima = dataMassima;
	}

	public Date getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public String getCod_erog() {
		return cod_erog;
	}

	public void setCod_erog(String cod_erog) {
		this.cod_erog = trimAndSet(cod_erog);
	}

	public String getCd_supporto() {
		return cd_supporto;
	}

	public void setCd_supporto(String cd_supporto) {
		this.cd_supporto = trimAndSet(cd_supporto);
	}

	public String getCd_valuta() {
		return cd_valuta;
	}

	public void setCd_valuta(String cd_valuta) {
		this.cd_valuta = trimAndSet(cd_valuta);
	}

	public Number getImporto() {
		return importo;
	}

	public void setImporto(Number importo) {
		this.importo = importo;
	}

	public Number getCostoMax() {
		return costoMax;
	}

	public void setCostoMax(Number costoMax) {
		this.costoMax = costoMax;
	}

	public RuoloBiblioteca getRuolo() {
		return ruolo;
	}

	public void setRuolo(RuoloBiblioteca ruolo) {
		this.ruolo = ruolo;
	}

	public Timestamp getUltimoCambioStato() {
		return ultimoCambioStato;
	}

	public void setUltimoCambioStato(Timestamp ts) {
		this.ultimoCambioStato = ts;
	}

	public String getCurrentState() {
		return currentState;
	}

	public void setCurrentState(String currentState) {
		this.currentState = trimAndSet(currentState);
	}

	public String getServizio() {
		return servizio;
	}

	public void setServizio(String servizio) {
		this.servizio = trimAndSet(servizio);
	}

	public DocumentoNonSbnVO getDocumento() {
		return documento;
	}

	public void setDocumento(DocumentoNonSbnVO documento) {
		this.documento = documento;
	}

	public InventarioTitoloVO getInventario() {
		return inventario;
	}

	public void setInventario(InventarioTitoloVO inv) {
		this.inventario = inv;
	}

	public List<BibliotecaVO> getBibliotecheFornitrici() {
		return bibliotecheFornitrici;
	}

	public void setBibliotecheFornitrici(List<BibliotecaVO> bibliotecheFornitrici) {
		this.bibliotecheFornitrici = bibliotecheFornitrici;
	}

	public List<MessaggioVO> getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(List<MessaggioVO> messaggio) {
		this.messaggio = messaggio;
	}

	public boolean isRichiedente() {
		return ruolo == RuoloBiblioteca.RICHIEDENTE;
	}

	public String getCodUtenteBibRichiedente() {
		return codUtenteBibRichiedente;
	}

	public void setCodUtenteBibRichiedente(String codUtenteBibRichiedente) {
		this.codUtenteBibRichiedente = trimAndSet(codUtenteBibRichiedente);
	}

	public String getDenominazioneBibRichiedente() {
		return denominazioneBibRichiedente;
	}

	public void setDenominazioneBibRichiedente(
			String denominazioneBibRichiedente) {
		this.denominazioneBibRichiedente = trimAndSet(denominazioneBibRichiedente);
	}

	public boolean isNuovo() {
		return (idRichiestaILL == 0);
	}

	public boolean isFornitrice() {
		return ruolo == RuoloBiblioteca.FORNITRICE;
	}

	public boolean isInventarioPresente() {
		return (inventario != null && inventario.getCodInvent() > 0);
	}

	public void addUltimoMessaggio(MessaggioVO msg) {
		if (msg != null) {
			if (!isFilled(msg.getRequesterId()))
				msg.setRequesterId(requesterId);
			if (!isFilled(msg.getResponderId()))
				msg.setResponderId(responderId);
			msg.setRuolo(ruolo);

			this.messaggio.add(0, msg);
		}
	}

	public Timestamp getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Timestamp dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Timestamp getDataFine() {
		return dataFine;
	}

	public void setDataFine(Timestamp dataFine) {
		this.dataFine = dataFine;
	}

	public MovimentoVO getMovimento() {
		return movimento;
	}

	public void setMovimento(MovimentoVO movimento) {
		this.movimento = movimento;
	}

	public MessaggioVO getUltimoMessaggio() {
		return ValidazioneDati.first(messaggio);
	}

	public String getIntervalloCopia() {
		return intervalloCopia;
	}

	public void setIntervalloCopia(String intervalloCopia) {
		this.intervalloCopia = trimAndSet(intervalloCopia);
	}

	public Date getDataRinnovo() {
		return dataRinnovo;
	}

	public void setDataRinnovo(Date dataRinnovo) {
		this.dataRinnovo = dataRinnovo;
	}

	public Date getDataRientroPrevisto() {
		return dataRientroPrevisto;
	}

	public void setDataRientroPrevisto(Date dataRientroPrevisto) {
		this.dataRientroPrevisto = dataRientroPrevisto;
	}

}
