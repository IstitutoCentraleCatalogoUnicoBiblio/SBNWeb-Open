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
package it.iccu.sbn.ejb.vo.servizi.documenti;

import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.util.cloning.ClonePool;

import java.util.List;

public class DocumentoNonSbnVO extends BaseVO {

	private static final long serialVersionUID = -8771608034220197550L;

	private int idDocumento;

	private String cod_tipo_doc;
	private String desc_tipo_doc;

	private String codBib;
	private String codPolo;
	private char tipo_doc_lett;

	private long cod_doc_lett;
	private char fonte;
	private char natura;
	private char stato_sugg = ' ';
	private java.util.Date data_sugg_lett;
	private String autore;
	protected String titolo;
	private String listaTipoDoc;
	private String luogoEdizione;
	private String editore;
	private String annoEdizione = "";

	protected String segnatura;
	protected String ord_segnatura;

	private String denominazioneBib;
	protected String codFruizione;
	protected String codNoDisp;

	private String bid;
	private String note;
	private String paese;
	private String lingua;

	private String messaggioAlLettore;
	private String cod_bib_inv;
	private String cod_serie;
	private Integer cod_inven;
	private String num_volume;

	protected String utente;
	protected String cognomeNome;

	private String dataIns;
	private String dataAgg;

	private List<EsemplareDocumentoNonSbnVO> esemplari;

	private int numeroEsemplari;
	private String annata;

	// almaviva5_20141125 servizi ill
	private String tipoNumStd = "";
	private String numeroStd = "";

	private List<BibliotecaVO> biblioteche;

	protected String marcRecord;
	private Character tipoRecord;

	// <Sponsoring-body>ente curatore</Sponsoring-body>
	private String enteCuratore;
	// <Series-title-number>fa parte di</Series-title-number>
	private String faParte;
	// <Issue>fascicolo</Issue>
	private String fascicolo;
	// <Publication-date>data pubb</Publication-date>
	private String dataPubb;
	// <Author-of-article>autore articolo</Author-of-article>
	private String autoreArticolo;
	// <Title-of-article>titolo articolo</Title-of-article>
	private String titoloArticolo;
	// <Pagination>pagine</Pagination>
	private String pagine;
	// <Verification-reference-source>fonte</Verification-reference-source>
	private String fonteRif;

	public DocumentoNonSbnVO() {
		super();
	}

	public DocumentoNonSbnVO(DocumentoNonSbnVO doc) {
		super(doc);
		this.idDocumento = doc.idDocumento;
		this.cod_tipo_doc = doc.cod_tipo_doc;
		this.desc_tipo_doc = doc.desc_tipo_doc;
		this.codBib = doc.codBib;
		this.codPolo = doc.codPolo;
		this.tipo_doc_lett = doc.tipo_doc_lett;
		this.cod_doc_lett = doc.cod_doc_lett;
		this.fonte = doc.fonte;
		this.natura = doc.natura;
		this.stato_sugg = doc.stato_sugg;
		this.data_sugg_lett = doc.data_sugg_lett;
		this.autore = doc.autore;
		this.titolo = doc.titolo;
		this.listaTipoDoc = doc.listaTipoDoc;
		this.luogoEdizione = doc.luogoEdizione;
		this.editore = doc.editore;
		this.annoEdizione = doc.annoEdizione;
		this.segnatura = doc.segnatura;
		this.ord_segnatura = doc.ord_segnatura;
		this.denominazioneBib = doc.denominazioneBib;
		this.codFruizione = doc.codFruizione;
		this.codNoDisp = doc.codNoDisp;
		this.bid = doc.bid;
		this.note = doc.note;
		this.paese = doc.paese;
		this.lingua = doc.lingua;
		this.messaggioAlLettore = doc.messaggioAlLettore;
		this.cod_bib_inv = doc.cod_bib_inv;
		this.cod_serie = doc.cod_serie;
		this.cod_inven = doc.cod_inven;
		this.num_volume = doc.num_volume;
		this.utente = doc.utente;
		this.cognomeNome = doc.cognomeNome;
		this.dataIns = doc.dataIns;
		this.dataAgg = doc.dataAgg;
		this.numeroEsemplari = doc.numeroEsemplari;
		this.annata = doc.annata;

		// almaviva5_20141125 servizi ill
		this.tipoNumStd = doc.tipoNumStd;
		this.numeroStd = doc.numeroStd;
		this.biblioteche = doc.biblioteche;
		this.marcRecord = doc.marcRecord;
		this.tipoRecord = doc.tipoRecord;

		this.enteCuratore = doc.enteCuratore;
		this.faParte = doc.faParte;
		this.fascicolo = doc.fascicolo;
		this.dataPubb = doc.dataPubb;
		this.autoreArticolo = doc.autoreArticolo;
		this.titoloArticolo = doc.titoloArticolo;
		this.pagine = doc.pagine;
		this.fonteRif = doc.fonteRif;

		try {
			this.esemplari = ClonePool.deepCopy(doc.esemplari);
		} catch (Exception e) {
			this.esemplari = null;
		}
	}

	public int getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(int idDocumento) {
		this.idDocumento = idDocumento;
	}

	public String getDenominazioneBib() {
		return denominazioneBib;
	}

	public void setDenominazioneBib(String denominazioneBib) {
		this.denominazioneBib = denominazioneBib;
	}

	public String getCodFruizione() {
		return codFruizione;
	}

	public void setCodFruizione(String codFruizione) {
		this.codFruizione = trimAndSet(codFruizione);
	}

	public String getCodNoDisp() {
		return codNoDisp;
	}

	public void setCodNoDisp(String codNoDisp) {
		this.codNoDisp = trimAndSet(codNoDisp);
	}

	public String getOrd_segnatura() {
		return ord_segnatura;
	}

	public void setOrd_segnatura(String ord_segnatura) {
		this.ord_segnatura = trimAndSet(ord_segnatura);
	}

	public java.util.Date getData_sugg_lett() {
		return data_sugg_lett;
	}

	public void setData_sugg_lett(java.util.Date data_sugg_lett) {
		this.data_sugg_lett = data_sugg_lett;
	}

	public char getFonte() {
		return fonte;
	}

	public void setFonte(char fonte) {
		this.fonte = fonte;
	}

	public char getNatura() {
		return natura;
	}

	public void setNatura(char natura) {
		this.natura = natura;
	}

	public char getStato_sugg() {
		return stato_sugg;
	}

	public void setStato_sugg(char stato_sugg) {
		this.stato_sugg = stato_sugg;
	}

	public long getCod_doc_lett() {
		return cod_doc_lett;
	}

	public void setCod_doc_lett(long cod_doc_lett) {
		this.cod_doc_lett = cod_doc_lett;
	}

	public char getTipo_doc_lett() {
		return tipo_doc_lett;
	}

	public void setTipo_doc_lett(char tipo_doc_lett) {
		this.tipo_doc_lett = tipo_doc_lett;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getAnnoEdizione() {
		return annoEdizione;
	}

	public void setAnnoEdizione(String annoEdi) {
		this.annoEdizione = trimAndSet(annoEdi);
	}

	public String getAutore() {
		return autore;
	}

	public void setAutore(String autore) {
		this.autore = trimAndSet(autore);
	}

	public String getEditore() {
		return editore;
	}

	public void setEditore(String editore) {
		this.editore = trimAndSet(editore);
	}

	public String getListaTipoDoc() {
		return listaTipoDoc;
	}

	public void setListaTipoDoc(String listaTipoDoc) {
		this.listaTipoDoc = listaTipoDoc;
	}

	public String getLuogoEdizione() {
		return luogoEdizione;
	}

	public void setLuogoEdizione(String luogoEdizione) {
		this.luogoEdizione = trimAndSet(luogoEdizione);
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = trimAndSet(titolo);
	}

	public String getSegnatura() {
		return segnatura;
	}

	public void setSegnatura(String segnatura) {
		this.segnatura = trimAndSet(segnatura);
	}

	public String getCod_tipo_doc() {
		return cod_tipo_doc;
	}

	public void setCod_tipo_doc(String cod_tipo_doc) {
		this.cod_tipo_doc = cod_tipo_doc;
	}

	public String getDesc_tipo_doc() {
		return desc_tipo_doc;
	}

	public void setDesc_tipo_doc(String desc_tipo_doc) {
		this.desc_tipo_doc = desc_tipo_doc;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = trimAndSet(utente);
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = trimAndSet(bid);
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = trimAndSet(note);
	}

	public String getCognomeNome() {
		return cognomeNome;
	}

	public void setCognomeNome(String cognomeNome) {
		this.cognomeNome = cognomeNome;
	}

	public String getPaese() {
		return paese;
	}

	public void setPaese(String paese) {
		this.paese = trimAndSet(paese);
	}

	public String getLingua() {
		return lingua;
	}

	public void setLingua(String lingua) {
		this.lingua = trimAndSet(lingua);
	}

	public String getMessaggioAlLettore() {
		return messaggioAlLettore;
	}

	public void setMessaggioAlLettore(String messaggioAlLettore) {
		this.messaggioAlLettore = trimAndSet(messaggioAlLettore);
	}

	public String getCod_bib_inv() {
		return cod_bib_inv;
	}

	public void setCod_bib_inv(String cod_bib_inv) {
		this.cod_bib_inv = cod_bib_inv;
	}

	public String getCod_serie() {
		return cod_serie;
	}

	public void setCod_serie(String cod_serie) {
		this.cod_serie = cod_serie;
	}

	public Integer getCod_inven() {
		return cod_inven;
	}

	public void setCod_inven(Integer cod_inven) {
		this.cod_inven = cod_inven;
	}

	public String getNum_volume() {
		return num_volume;
	}

	public void setNum_volume(String num_volume) {
		this.num_volume = trimAndSet(num_volume);
	}

	public String getDataIns() {
		return dataIns;
	}

	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}

	public String getDataAgg() {
		return dataAgg;
	}

	public void setDataAgg(String dataAgg) {
		this.dataAgg = dataAgg;
	}

	public boolean isNuovo() {
		return (idDocumento == 0);
	}

	public List<EsemplareDocumentoNonSbnVO> getEsemplari() {
		return esemplari;
	}

	public void setEsemplari(List<EsemplareDocumentoNonSbnVO> esemplari) {
		this.esemplari = esemplari;
	}

	public int getNumeroEsemplari() {
		return numeroEsemplari;
	}

	public boolean isSuggerimento() {
		return (isFilled(tipo_doc_lett) && tipo_doc_lett == 'S');
	}

	public boolean isDocumentoILL() {
		return (isFilled(tipo_doc_lett) && tipo_doc_lett == 'D');
	}

	public boolean isPosseduto() {
		return (isFilled(tipo_doc_lett) && tipo_doc_lett == 'P');
	}

	public boolean hasInventario() {
		return isFilled(cod_bib_inv) && length(cod_serie) > 0 && isFilled(cod_inven);
	}

	public void setNumeroEsemplari(int numeroEsemplari) {
		this.numeroEsemplari = numeroEsemplari;
	}

	public int countEsemplariAttivi() {
		if (!isFilled(esemplari))
			return 0;

		int count = 0;
		for (EsemplareDocumentoNonSbnVO e : esemplari)
			count += e.isCancellato() ? 0 : 1;

		return count;
	}

	public String getAnnata() {
		return annata;
	}

	public void setAnnata(String annata) {
		this.annata = trimAndSet(annata);
	}

	@Override
	public int getRepeatableId() {
		return idDocumento;
	}

	public String getTipoNumStd() {
		return tipoNumStd;
	}

	public void setTipoNumStd(String tipoNumStd) {
		this.tipoNumStd = trimAndSet(tipoNumStd);
	}

	public String getNumeroStd() {
		return numeroStd;
	}

	public void setNumeroStd(String numeroStd) {
		this.numeroStd = trimAndSet(numeroStd);
	}

	public List<BibliotecaVO> getBiblioteche() {
		return biblioteche;
	}

	public void setBiblioteche(List<BibliotecaVO> biblioteche) {
		this.biblioteche = biblioteche;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((annoEdizione == null) ? 0 : annoEdizione.hashCode());
		result = prime * result + ((autore == null) ? 0 : autore.hashCode());
		result = prime * result + ((bid == null) ? 0 : bid.hashCode());
		result = prime * result + ((codBib == null) ? 0 : codBib.hashCode());
		result = prime * result + ((codFruizione == null) ? 0 : codFruizione.hashCode());
		result = prime * result + ((codNoDisp == null) ? 0 : codNoDisp.hashCode());
		result = prime * result + ((codPolo == null) ? 0 : codPolo.hashCode());
		result = prime * result + ((cod_bib_inv == null) ? 0 : cod_bib_inv.hashCode());
		result = prime * result + (int) (cod_doc_lett ^ (cod_doc_lett >>> 32));
		result = prime * result + ((cod_inven == null) ? 0 : cod_inven.hashCode());
		result = prime * result + ((cod_serie == null) ? 0 : cod_serie.hashCode());
		result = prime * result + ((cod_tipo_doc == null) ? 0 : cod_tipo_doc.hashCode());
		result = prime * result + ((cognomeNome == null) ? 0 : cognomeNome.hashCode());
		result = prime * result + ((dataAgg == null) ? 0 : dataAgg.hashCode());
		result = prime * result + ((dataIns == null) ? 0 : dataIns.hashCode());
		result = prime * result + ((data_sugg_lett == null) ? 0 : data_sugg_lett.hashCode());
		result = prime * result + ((denominazioneBib == null) ? 0 : denominazioneBib.hashCode());
		result = prime * result + ((desc_tipo_doc == null) ? 0 : desc_tipo_doc.hashCode());
		result = prime * result + ((editore == null) ? 0 : editore.hashCode());
		result = prime * result + ((esemplari == null) ? 0 : esemplari.hashCode());
		result = prime * result + fonte;
		result = prime * result + ((lingua == null) ? 0 : lingua.hashCode());
		result = prime * result + ((listaTipoDoc == null) ? 0 : listaTipoDoc.hashCode());
		result = prime * result + ((luogoEdizione == null) ? 0 : luogoEdizione.hashCode());
		result = prime * result + ((messaggioAlLettore == null) ? 0 : messaggioAlLettore.hashCode());
		result = prime * result + natura;
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((num_volume == null) ? 0 : num_volume.hashCode());
		result = prime * result + numeroEsemplari;
		result = prime * result + ((numeroStd == null) ? 0 : numeroStd.hashCode());
		result = prime * result + ((ord_segnatura == null) ? 0 : ord_segnatura.hashCode());
		result = prime * result + ((paese == null) ? 0 : paese.hashCode());
		result = prime * result + ((segnatura == null) ? 0 : segnatura.hashCode());
		result = prime * result + stato_sugg;
		result = prime * result + ((tipoNumStd == null) ? 0 : tipoNumStd.hashCode());
		result = prime * result + tipo_doc_lett;
		result = prime * result + ((titolo == null) ? 0 : titolo.hashCode());
		result = prime * result + ((utente == null) ? 0 : utente.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentoNonSbnVO other = (DocumentoNonSbnVO) obj;
		if (annoEdizione == null) {
			if (other.annoEdizione != null)
				return false;
		} else if (!annoEdizione.equals(other.annoEdizione))
			return false;
		if (autore == null) {
			if (other.autore != null)
				return false;
		} else if (!autore.equals(other.autore))
			return false;
		if (bid == null) {
			if (other.bid != null)
				return false;
		} else if (!bid.equals(other.bid))
			return false;
		if (codBib == null) {
			if (other.codBib != null)
				return false;
		} else if (!codBib.equals(other.codBib))
			return false;
		if (codFruizione == null) {
			if (other.codFruizione != null)
				return false;
		} else if (!codFruizione.equals(other.codFruizione))
			return false;
		if (codNoDisp == null) {
			if (other.codNoDisp != null)
				return false;
		} else if (!codNoDisp.equals(other.codNoDisp))
			return false;
		if (codPolo == null) {
			if (other.codPolo != null)
				return false;
		} else if (!codPolo.equals(other.codPolo))
			return false;
		if (cod_bib_inv == null) {
			if (other.cod_bib_inv != null)
				return false;
		} else if (!cod_bib_inv.equals(other.cod_bib_inv))
			return false;
		if (cod_doc_lett != other.cod_doc_lett)
			return false;
		if (cod_inven == null) {
			if (other.cod_inven != null)
				return false;
		} else if (!cod_inven.equals(other.cod_inven))
			return false;
		if (cod_serie == null) {
			if (other.cod_serie != null)
				return false;
		} else if (!cod_serie.equals(other.cod_serie))
			return false;
		if (cod_tipo_doc == null) {
			if (other.cod_tipo_doc != null)
				return false;
		} else if (!cod_tipo_doc.equals(other.cod_tipo_doc))
			return false;
		if (cognomeNome == null) {
			if (other.cognomeNome != null)
				return false;
		} else if (!cognomeNome.equals(other.cognomeNome))
			return false;
		if (dataAgg == null) {
			if (other.dataAgg != null)
				return false;
		} else if (!dataAgg.equals(other.dataAgg))
			return false;
		if (dataIns == null) {
			if (other.dataIns != null)
				return false;
		} else if (!dataIns.equals(other.dataIns))
			return false;
		if (data_sugg_lett == null) {
			if (other.data_sugg_lett != null)
				return false;
		} else if (!data_sugg_lett.equals(other.data_sugg_lett))
			return false;
		if (denominazioneBib == null) {
			if (other.denominazioneBib != null)
				return false;
		} else if (!denominazioneBib.equals(other.denominazioneBib))
			return false;
		if (desc_tipo_doc == null) {
			if (other.desc_tipo_doc != null)
				return false;
		} else if (!desc_tipo_doc.equals(other.desc_tipo_doc))
			return false;
		if (editore == null) {
			if (other.editore != null)
				return false;
		} else if (!editore.equals(other.editore))
			return false;
		if (esemplari == null) {
			if (other.esemplari != null)
				return false;
		} else if (!listEquals(esemplari, other.esemplari, EsemplareDocumentoNonSbnVO.class))
			return false;
		if (fonte != other.fonte)
			return false;
		if (lingua == null) {
			if (other.lingua != null)
				return false;
		} else if (!lingua.equals(other.lingua))
			return false;
		if (listaTipoDoc == null) {
			if (other.listaTipoDoc != null)
				return false;
		} else if (!listaTipoDoc.equals(other.listaTipoDoc))
			return false;
		if (luogoEdizione == null) {
			if (other.luogoEdizione != null)
				return false;
		} else if (!luogoEdizione.equals(other.luogoEdizione))
			return false;
		if (messaggioAlLettore == null) {
			if (other.messaggioAlLettore != null)
				return false;
		} else if (!messaggioAlLettore.equals(other.messaggioAlLettore))
			return false;
		if (natura != other.natura)
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (num_volume == null) {
			if (other.num_volume != null)
				return false;
		} else if (!num_volume.equals(other.num_volume))
			return false;
		if (numeroEsemplari != other.numeroEsemplari)
			return false;
		if (numeroStd == null) {
			if (other.numeroStd != null)
				return false;
		} else if (!numeroStd.equals(other.numeroStd))
			return false;
		if (ord_segnatura == null) {
			if (other.ord_segnatura != null)
				return false;
		} else if (!ord_segnatura.equals(other.ord_segnatura))
			return false;
		if (paese == null) {
			if (other.paese != null)
				return false;
		} else if (!paese.equals(other.paese))
			return false;
		if (segnatura == null) {
			if (other.segnatura != null)
				return false;
		} else if (!segnatura.equals(other.segnatura))
			return false;
		if (stato_sugg != other.stato_sugg)
			return false;
		if (tipoNumStd == null) {
			if (other.tipoNumStd != null)
				return false;
		} else if (!tipoNumStd.equals(other.tipoNumStd))
			return false;
		if (tipo_doc_lett != other.tipo_doc_lett)
			return false;
		if (titolo == null) {
			if (other.titolo != null)
				return false;
		} else if (!titolo.equals(other.titolo))
			return false;
		if (utente == null) {
			if (other.utente != null)
				return false;
		} else if (!utente.equals(other.utente))
			return false;
		if (biblioteche == null) {
			if (other.biblioteche != null)
				return false;
		} else if (!listEquals(biblioteche, other.biblioteche, BibliotecaVO.class))
			return false;
		if (tipoRecord == null) {
			if (other.tipoRecord != null)
				return false;
		} else if (!tipoRecord.equals(other.tipoRecord))
			return false;
		if (autoreArticolo == null) {
			if (other.autoreArticolo != null) {
				return false;
			}
		} else if (!autoreArticolo.equals(other.autoreArticolo)) {
			return false;
		}
		if (dataPubb == null) {
			if (other.dataPubb != null) {
				return false;
			}
		} else if (!dataPubb.equals(other.dataPubb)) {
			return false;
		}
		if (enteCuratore == null) {
			if (other.enteCuratore != null) {
				return false;
			}
		} else if (!enteCuratore.equals(other.enteCuratore)) {
			return false;
		}
		if (faParte == null) {
			if (other.faParte != null) {
				return false;
			}
		} else if (!faParte.equals(other.faParte)) {
			return false;
		}
		if (fascicolo == null) {
			if (other.fascicolo != null) {
				return false;
			}
		} else if (!fascicolo.equals(other.fascicolo)) {
			return false;
		}
		if (fonteRif == null) {
			if (other.fonteRif != null) {
				return false;
			}
		} else if (!fonteRif.equals(other.fonteRif)) {
			return false;
		}
		if (pagine == null) {
			if (other.pagine != null) {
				return false;
			}
		} else if (!pagine.equals(other.pagine)) {
			return false;
		}
		if (titoloArticolo == null) {
			if (other.titoloArticolo != null) {
				return false;
			}
		} else if (!titoloArticolo.equals(other.titoloArticolo)) {
			return false;
		}
		return true;
	}

	public String getMarcRecord() {
		return marcRecord;
	}

	public void setMarcRecord(String marcRecord) {
		this.marcRecord = marcRecord;
	}

	public Character getTipoRecord() {
		return tipoRecord;
	}

	public void setTipoRecord(Character tipoRecord) {
		this.tipoRecord = tipoRecord;
	}

	public String getEnteCuratore() {
		return enteCuratore;
	}

	public void setEnteCuratore(String enteCuratore) {
		this.enteCuratore = trimAndSet(enteCuratore);
	}

	public String getFaParte() {
		return faParte;
	}

	public void setFaParte(String faParte) {
		this.faParte = trimAndSet(faParte);
	}

	public String getFascicolo() {
		return fascicolo;
	}

	public void setFascicolo(String fascicolo) {
		this.fascicolo = trimAndSet(fascicolo);
	}

	public String getDataPubb() {
		return dataPubb;
	}

	public void setDataPubb(String dataPubb) {
		this.dataPubb = trimAndSet(dataPubb);
	}

	public String getAutoreArticolo() {
		return autoreArticolo;
	}

	public void setAutoreArticolo(String autoreArticolo) {
		this.autoreArticolo = trimAndSet(autoreArticolo);
	}

	public String getTitoloArticolo() {
		return titoloArticolo;
	}

	public void setTitoloArticolo(String titoloArticolo) {
		this.titoloArticolo = trimAndSet(titoloArticolo);
	}

	public String getPagine() {
		return pagine;
	}

	public void setPagine(String pagine) {
		this.pagine = trimAndSet(pagine);
	}

	public String getFonteRif() {
		return fonteRif;
	}

	public void setFonteRif(String fonteRif) {
		this.fonteRif = trimAndSet(fonteRif);
	}

}
