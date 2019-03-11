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
package it.iccu.sbn.ejb.vo.gestionebibliografica;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.documentofisico.DocumentoFisicoElaborazioniDifferiteOutputVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.EsportaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.importa.ImportaVO;

import java.util.ArrayList;
import java.util.List;


public class AreaParametriStampaSchedeVo extends DocumentoFisicoElaborazioniDifferiteOutputVO {

	private static final long serialVersionUID = 2641944405729323338L;

	private boolean inventariMultipli;
	private boolean titoliNonPosseduti;

	private List listaBid;
//	private List<ComboCodDescVO> listaSerieInventari;
//	private StampaEtichetteVO stampaEtichetteVO;

	private List listaPerAutoreSchedeVO;
	private List listaPerTopograficoSchedeVO;
	private List listaPerSoggettoSchedeVO;
	private List listaPerTitoloSchedeVO;
	private List listaPerClassificazioneSchedeVO;
	private List listaPerEditoreSchedeVO;
	private List listaPerPossessoreSchedeVO;


	private boolean richListaPerAutore;
	// Valori ammessi: "TUTTI", "RESP1" o "RESP2"
	private String tipoResponsabilitaRich;
	private boolean richListaPerTopografico;
	private boolean richListaPerSoggetto;
	private boolean richListaPerTitolo;
	private boolean richListaPerClassificazione;
	private boolean richListaPerEditore;
	private boolean richListaPerPossessore;
	private boolean richListaPerTipografo;

	private int numCopiePerAutore;
	private int numCopiePerTopografico;
	private int numCopiePerSoggetto;
	private int numCopiePerTitolo;
	private int numCopiePerClassificazione;
	private int numCopiePerEditore;
	private int numCopiePerPossessore;

	// aree relative alla sola stampa cataloghi
	private ParametriSelezioneOggBiblioVO parametriSelezioneOggBiblioVO;
	private EsportaVO esportaVO;
	private ImportaVO importaVO;


    // Opzioni di stampa
    private String intestTitoloAdAutore;
    private String titoloCollana;
    private String titoliAnalitici;
    private String datiCollocazione;
	//

	private List<String> errori = new ArrayList<String>();
	private String msg;

	private String descrizioneBiblioteca;

	public List<String> getErrori() {
		return errori;
	}

	public void setErrori(List<String> errori) {
		this.errori = errori;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	private String codErr;
	private String testoProtocollo;

	private String ticket;

	public AreaParametriStampaSchedeVo(ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
		listaPerAutoreSchedeVO  = new ArrayList();
		listaPerClassificazioneSchedeVO = new ArrayList();
		listaPerEditoreSchedeVO = new ArrayList();
		listaPerPossessoreSchedeVO = new ArrayList();
		listaPerSoggettoSchedeVO = new ArrayList();
		listaPerTopograficoSchedeVO = new ArrayList();
		listaPerTitoloSchedeVO = new ArrayList();

		richListaPerAutore = false;
		tipoResponsabilitaRich = "TUTTI";
		richListaPerClassificazione = false;
		richListaPerEditore = false;
		richListaPerPossessore = false;
		richListaPerSoggetto = false;
		richListaPerTitolo = false;
		richListaPerTopografico = false;
		richListaPerTipografo = false;

		numCopiePerAutore = 0;
		numCopiePerClassificazione = 0;
		numCopiePerEditore = 0;
		numCopiePerPossessore = 0;
		numCopiePerSoggetto = 0;
		numCopiePerTitolo = 0;
		numCopiePerTopografico = 0;

		parametriSelezioneOggBiblioVO = new ParametriSelezioneOggBiblioVO();
		esportaVO = new EsportaVO();
	}

	public void validate(List<String> errori) throws ValidationException {
		super.validate(errori);
	}

	public List getListaBid() {
		return listaBid;
	}

	public void setListaBid(List listaBid) {
		this.listaBid = listaBid;
	}

	public List addListaBid(String newBid) {
		listaBid.add(newBid);
		return listaBid;
	}

	public String getCodErr() {
		return codErr;
	}

	public void setCodErr(String codErr) {
		this.codErr = codErr;
	}

	public String getTestoProtocollo() {
		return testoProtocollo;
	}

	public void setTestoProtocollo(String testoProtocollo) {
		this.testoProtocollo = testoProtocollo;
	}


	public List getListaPerAutoreSchedeVO() {
		return listaPerAutoreSchedeVO;
	}

	public void setListaPerAutoreSchedeVO(List listaPerAutoreSchedeVO) {
		this.listaPerAutoreSchedeVO = listaPerAutoreSchedeVO;
	}


	public List getListaPerClassificazioneSchedeVO() {
		return listaPerClassificazioneSchedeVO;
	}

	public void setListaPerClassificazioneSchedeVO(
			List listaPerClassificazioneSchedeVO) {
		this.listaPerClassificazioneSchedeVO = listaPerClassificazioneSchedeVO;
	}

	public List getListaPerEditoreSchedeVO() {
		return listaPerEditoreSchedeVO;
	}

	public void setListaPerEditoreSchedeVO(List listaPerEditoreSchedeVO) {
		this.listaPerEditoreSchedeVO = listaPerEditoreSchedeVO;
	}

	public List getListaPerPossessoreSchedeVO() {
		return listaPerPossessoreSchedeVO;
	}

	public void setListaPerPossessoreSchedeVO(List listaPerPossessoreSchedeVO) {
		this.listaPerPossessoreSchedeVO = listaPerPossessoreSchedeVO;
	}

	public List getListaPerSoggettoSchedeVO() {
		return listaPerSoggettoSchedeVO;
	}

	public void setListaPerSoggettoSchedeVO(List listaPerSoggettoSchedeVO) {
		this.listaPerSoggettoSchedeVO = listaPerSoggettoSchedeVO;
	}

	public List getListaPerTitoloSchedeVO() {
		return listaPerTitoloSchedeVO;
	}

	public void setListaPerTitoloSchedeVO(List listaPerTitoloSchedeVO) {
		this.listaPerTitoloSchedeVO = listaPerTitoloSchedeVO;
	}

	public List getListaPerTopograficoSchedeVO() {
		return listaPerTopograficoSchedeVO;
	}

	public void setListaPerTopograficoSchedeVO(List listaPerTopograficoSchedeVO) {
		this.listaPerTopograficoSchedeVO = listaPerTopograficoSchedeVO;
	}

	public int getNumCopiePerAutore() {
		return numCopiePerAutore;
	}

	public void setNumCopiePerAutore(int numCopiePerAutore) {
		this.numCopiePerAutore = numCopiePerAutore;
	}

	public int getNumCopiePerClassificazione() {
		return numCopiePerClassificazione;
	}

	public void setNumCopiePerClassificazione(int numCopiePerClassificazione) {
		this.numCopiePerClassificazione = numCopiePerClassificazione;
	}

	public int getNumCopiePerEditore() {
		return numCopiePerEditore;
	}

	public void setNumCopiePerEditore(int numCopiePerEditore) {
		this.numCopiePerEditore = numCopiePerEditore;
	}

	public int getNumCopiePerPossessore() {
		return numCopiePerPossessore;
	}

	public void setNumCopiePerPossessore(int numCopiePerPossessore) {
		this.numCopiePerPossessore = numCopiePerPossessore;
	}

	public int getNumCopiePerSoggetto() {
		return numCopiePerSoggetto;
	}

	public void setNumCopiePerSoggetto(int numCopiePerSoggetto) {
		this.numCopiePerSoggetto = numCopiePerSoggetto;
	}

	public int getNumCopiePerTitolo() {
		return numCopiePerTitolo;
	}

	public void setNumCopiePerTitolo(int numCopiePerTitolo) {
		this.numCopiePerTitolo = numCopiePerTitolo;
	}

	public int getNumCopiePerTopografico() {
		return numCopiePerTopografico;
	}

	public void setNumCopiePerTopografico(int numCopiePerTopografico) {
		this.numCopiePerTopografico = numCopiePerTopografico;
	}

	public List addListaPerAutoreSchedeVO(TracciatoStampaSchedeVO newScheda) {
		listaPerAutoreSchedeVO.add(newScheda);
		return listaPerAutoreSchedeVO;
	}

	public List addListaPerClassificazioneSchedeVO(TracciatoStampaSchedeVO newScheda) {
		listaPerClassificazioneSchedeVO.add(newScheda);
		return listaPerClassificazioneSchedeVO;
	}
	public List addListaPerEditoreSchedeVO(TracciatoStampaSchedeVO newScheda) {
		listaPerEditoreSchedeVO.add(newScheda);
		return listaPerEditoreSchedeVO;
	}
	public List addListaPerPossessoreSchedeVO(TracciatoStampaSchedeVO newScheda) {
		listaPerPossessoreSchedeVO.add(newScheda);
		return listaPerPossessoreSchedeVO;
	}
	public List addListaPerSoggettoSchedeVO(TracciatoStampaSchedeVO newScheda) {
		listaPerSoggettoSchedeVO.add(newScheda);
		return listaPerSoggettoSchedeVO;
	}

	public List addListaPerTitoloSchedeVO(TracciatoStampaSchedeVO newScheda) {
		listaPerTitoloSchedeVO.add(newScheda);
		return listaPerTitoloSchedeVO;
	}

	public List addListaPerTopograficoSchedeVO(TracciatoStampaSchedeVO newScheda) {
		listaPerTopograficoSchedeVO.add(newScheda);
		return listaPerTopograficoSchedeVO;
	}

	public boolean isRichListaPerAutore() {
		return richListaPerAutore;
	}

	public void setRichListaPerAutore(boolean richListaPerAutore) {
		this.richListaPerAutore = richListaPerAutore;
	}

	public boolean isRichListaPerClassificazione() {
		return richListaPerClassificazione;
	}

	public void setRichListaPerClassificazione(boolean richListaPerClassificazione) {
		this.richListaPerClassificazione = richListaPerClassificazione;
	}

	public boolean isRichListaPerEditore() {
		return richListaPerEditore;
	}

	public void setRichListaPerEditore(boolean richListaPerEditore) {
		this.richListaPerEditore = richListaPerEditore;
	}

	public boolean isRichListaPerPossessore() {
		return richListaPerPossessore;
	}

	public void setRichListaPerPossessore(boolean richListaPerPossessore) {
		this.richListaPerPossessore = richListaPerPossessore;
	}

	public boolean isRichListaPerSoggetto() {
		return richListaPerSoggetto;
	}

	public void setRichListaPerSoggetto(boolean richListaPerSoggetto) {
		this.richListaPerSoggetto = richListaPerSoggetto;
	}

	public boolean isRichListaPerTitolo() {
		return richListaPerTitolo;
	}

	public void setRichListaPerTitolo(boolean richListaPerTitolo) {
		this.richListaPerTitolo = richListaPerTitolo;
	}

	public boolean isRichListaPerTopografico() {
		return richListaPerTopografico;
	}

	public void setRichListaPerTopografico(boolean richListaPerTopografico) {
		this.richListaPerTopografico = richListaPerTopografico;
	}

	public String getTipoResponsabilitaRich() {
		return tipoResponsabilitaRich;
	}

	public void setTipoResponsabilitaRich(String tipoResponsabilitaRich) {
		this.tipoResponsabilitaRich = tipoResponsabilitaRich;
	}

	public boolean isInventariMultipli() {
		return inventariMultipli;
	}

	public void setInventariMultipli(boolean inventariMultipli) {
		this.inventariMultipli = inventariMultipli;
	}

	public boolean isTitoliNonPosseduti() {
		return titoliNonPosseduti;
	}

	public void setTitoliNonPosseduti(boolean titoliNonPosseduti) {
		this.titoliNonPosseduti = titoliNonPosseduti;
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

	public String getIntestTitoloAdAutore() {
		return intestTitoloAdAutore;
	}

	public void setIntestTitoloAdAutore(String intestTitoloAdAutore) {
		this.intestTitoloAdAutore = intestTitoloAdAutore;
	}

	public String getTitoloCollana() {
		return titoloCollana;
	}

	public void setTitoloCollana(String titoloCollana) {
		this.titoloCollana = titoloCollana;
	}

	public String getTitoliAnalitici() {
		return titoliAnalitici;
	}

	public void setTitoliAnalitici(String titoliAnalitici) {
		this.titoliAnalitici = titoliAnalitici;
	}

	public String getDatiCollocazione() {
		return datiCollocazione;
	}

	public void setDatiCollocazione(String datiCollocazione) {
		this.datiCollocazione = datiCollocazione;
	}

	public ParametriSelezioneOggBiblioVO getParametriSelezioneOggBiblioVO() {
		return parametriSelezioneOggBiblioVO;
	}

	public void setParametriSelezioneOggBiblioVO(
			ParametriSelezioneOggBiblioVO parametriSelezioneOggBiblioVO) {
		this.parametriSelezioneOggBiblioVO = parametriSelezioneOggBiblioVO;
	}

	public EsportaVO getEsportaVO() {
		return esportaVO;
	}

	public void setEsportaVO(EsportaVO esportaVO) {
		this.esportaVO = esportaVO;
	}

	public ImportaVO getImportaVO() {
		return importaVO;
	}

	public void setImportaVO(ImportaVO importaVO) {
		this.importaVO = importaVO;
	}

	public String getDescrizioneBiblioteca() {
		return descrizioneBiblioteca;
	}

	public void setDescrizioneBiblioteca(String descrizioneBiblioteca) {
		this.descrizioneBiblioteca = descrizioneBiblioteca;
	}

	public boolean isRichListaPerTipografo() {
		return richListaPerTipografo;
	}

	public void setRichListaPerTipografo(boolean richListaPerTipografo) {
		this.richListaPerTipografo = richListaPerTipografo;
	}

}
