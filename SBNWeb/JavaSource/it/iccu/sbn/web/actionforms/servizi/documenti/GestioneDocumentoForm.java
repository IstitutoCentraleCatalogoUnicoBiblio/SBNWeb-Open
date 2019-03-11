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
package it.iccu.sbn.web.actionforms.servizi.documenti;

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ModalitaGestioneType;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.TipoOperazione;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.EsemplareDocumentoNonSbnVO;

import java.util.List;

public class GestioneDocumentoForm extends ListaDocumentiForm {


	private static final long serialVersionUID = 4743016549894754005L;
	protected DocumentoNonSbnVO dettaglio = new DocumentoNonSbnVO();
	protected DocumentoNonSbnVO dettaglioOld = new DocumentoNonSbnVO();
	private ModalitaGestioneType modalita = ModalitaGestioneType.ESAMINA;
	private TipoOperazione operazione = null;

	private List<ComboCodDescVO> listaTipoCodFruizione;
	private List<ComboCodDescVO> listaTipoCodNoDisp;
	private List<ComboCodDescVO> listaPaesi;
	private List<ComboCodDescVO> listaLingue;

	private String cambiato = null;

	private List<DocumentoNonSbnVO> selectedDoc;
	private int posizioneScorrimento;
	private int numDoc;

	//almaviva5_20141125 servizi ill
	private List<TB_CODICI> listaTipoNumeroStd;
	private String[] bibSelezionate;
	private String tipoServizio;

	public boolean isFonteLettore() {
		return (dettaglio != null && dettaglio.getFonte() == 'L');
	}

	public String getCambiato() {
		return cambiato;
	}

	public void setCambiato(String cambiato) {
		this.cambiato = cambiato;
	}

	public DocumentoNonSbnVO getDettaglio() {
		return dettaglio;
	}

	public void setDettaglio(DocumentoNonSbnVO dettaglio) {
		this.dettaglio = dettaglio;
	}

	public DocumentoNonSbnVO getDocumento() {
		return getDettaglio();
	}

	public List<ComboCodDescVO> getListaTipoCodFruizione() {
		return listaTipoCodFruizione;
	}

	public void setListaTipoCodFruizione(
			List<ComboCodDescVO> listaTipoCodFruizione) {
		this.listaTipoCodFruizione = listaTipoCodFruizione;
	}

	public List<ComboCodDescVO> getListaTipoCodNoDisp() {
		return listaTipoCodNoDisp;
	}

	public void setListaTipoCodNoDisp(List<ComboCodDescVO> litaTipoCodNoDisp) {
		this.listaTipoCodNoDisp = litaTipoCodNoDisp;
	}

	public void setModalita(ModalitaGestioneType modalita) {
		this.modalita = modalita;
	}

	public ModalitaGestioneType getModalita() {
		return modalita;
	}

	public List<ComboCodDescVO> getListaPaesi() {
		return listaPaesi;
	}

	public void setListaPaesi(List<ComboCodDescVO> listaPaesi) {
		this.listaPaesi = listaPaesi;
	}

	public List<ComboCodDescVO> getListaLingue() {
		return listaLingue;
	}

	public void setListaLingue(List<ComboCodDescVO> listaLingue) {
		this.listaLingue = listaLingue;
	}

	public EsemplareDocumentoNonSbnVO getItemEsemplare(int index) {

		// automatically grow List size
		List<EsemplareDocumentoNonSbnVO> esemplari = dettaglio.getEsemplari();
		while (index >= esemplari.size()) {
			EsemplareDocumentoNonSbnVO esempl = new EsemplareDocumentoNonSbnVO(
					dettaglio.getIdDocumento(), dettaglio.getCodPolo(),
					dettaglio.getCodBib(), dettaglio.getTipo_doc_lett(),
					dettaglio.getCod_doc_lett());
			esemplari.add(esempl);
		}
		return esemplari.get(index);
	}

	public DocumentoNonSbnVO getDettaglioOld() {
		return dettaglioOld;
	}

	public void setDettaglioOld(DocumentoNonSbnVO dettaglioOld) {
		this.dettaglioOld = dettaglioOld;
	}

	public TipoOperazione getOperazione() {
		return operazione;
	}

	public void setOperazione(TipoOperazione operazione) {
		this.operazione = operazione;
	}

	public void setPosizioneScorrimento(int posizioneScorrimento) {
		this.posizioneScorrimento = posizioneScorrimento;
	}

	public List<DocumentoNonSbnVO> getSelectedDoc() {
		return selectedDoc;
	}

	public void setSelectedDoc(List<DocumentoNonSbnVO> selectedDoc) {
		this.selectedDoc = selectedDoc;
	}

	public int getNumDoc() {
		return numDoc;
	}

	public void setNumDoc(int numDoc) {
		this.numDoc = numDoc;
	}

	public int getPosizioneScorrimento() {
		return posizioneScorrimento;
	}

	public List<TB_CODICI> getListaTipoNumeroStd() {
		return listaTipoNumeroStd;
	}

	public void setListaTipoNumeroStd(List<TB_CODICI> listaTipoNumeroStd) {
		this.listaTipoNumeroStd = listaTipoNumeroStd;
	}

	public String[] getBibSelezionate() {
		return bibSelezionate;
	}

	public void setBibSelezionate(String[] bibSelezionate) {
		this.bibSelezionate = bibSelezionate;
	}

	public BibliotecaVO getItemBibOpac(int index) {

		// automatically grow List size
		List<BibliotecaVO> biblioteche = dettaglio.getBiblioteche();
		while (index >= biblioteche.size()) {
			biblioteche.add(new BibliotecaVO());
		}
		return biblioteche.get(index);
	}

	public void setTipoServizio(String tipoServizio) {
		this.tipoServizio = tipoServizio;
	}

	public String getTipoServizio() {
		return tipoServizio;
	}

}
