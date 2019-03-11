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
package it.iccu.sbn.ejb.vo.documentofisico;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;

import java.util.List;

public class DocumentoFisicoElaborazioniDifferiteOutputVO extends ElaborazioniDifferiteOutputVo {

	private static final long serialVersionUID = 6865772697839425389L;

	//Ricerca per collocazioni - tipoOperazione = S
	private String codPoloSez;
	private String codBibSez;
	protected String sezione;
	private String tipoSezione;
	protected String dallaCollocazione;
	protected String dallaSpecificazione;
	protected String allaCollocazione;
	protected String allaSpecificazione;

	//Ricerca per range di inventari - tipoOperazione = I
	private String serie;
	private String endInventario;
	private String startInventario;

	//Ricerca per inventari
	private List listaInventari;

	// almaviva2 Settembre 2017: modifica a Stampa Strumenti Patrimonio per consentire la stampa per una lista di titoli proveniente
	// da file o dalla valorizzazione dei campindi bid sulla mappa
	private List listaBid;

	//
	private String nomeFileAppoggioInv;
	private String nomeFileAppoggioBid;

	//Ricerca per date
	private String dataDa;
	private String dataA;
	private boolean nuoviTitoli;
	private boolean nuoviEsemplari;


	//Canale
	private String tipoOperazione;//R, selezione range inventari; S, selezione intervallo collocazioni, I, selezione listaInventari;
	private String selezione; // per tipoOperazione = I, selezione = "F" (file), Selezione = "N" (codici indicati sula mappa)
	//strumenti sul patrimonio
	private String tipoOrdinamento;
	//
	private String tipoMateriale;
	private String descrizioneTP;
	private String statoConservazione;
	private String descrizioneSC;
	private String digitalizzazione;
	private String descrizioneDigitalizzazione;
	private String noDispo;
	private String descrizioneND;

	public DocumentoFisicoElaborazioniDifferiteOutputVO(
			ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
	}

	public void validate(List<String> errori) throws ValidationException {
		try{
			super.validate();
			if (this.getTipoOperazione() == null || (this.getTipoOperazione()!=null && this.getTipoOperazione().trim().length()==0)){
				errori.add("ERRORE: tipoOperazioneObbligatorio");
			}
			if (this.getTipoOperazione() !=null &&  this.getTipoOperazione().length()!=0)	{
				if (this.getTipoOperazione().length()>1)	{
					errori.add("ERRORE: tipoOperazioneEccedente");
				}
			}
			if (this.getCodPolo() == null || (this.getCodPolo()!=null && this.getCodPolo().trim().length()==0)){
				errori.add("ERRORE: CodPoloObbligatorio");
			}
			if (this.getCodBib() == null || (this.getCodBib()!=null && this.getCodBib().trim().length()==0)){
				errori.add("ERRORE: CodBibObbligatorio");
			}
			if (this.getCodPolo() != null && this.getCodPolo().length() != 0) {
				if (this.getCodPolo().length() > 3) {
					errori.add("ERRORE: CodPoloEccedente");
				}
			}
			if (this.getCodBib() != null && this.getCodBib().length() != 0) {
				if (this.getCodBib().length() > 3) {
					errori.add("ERRORE: CodBerroriEccedente");
				}
			}
			if (this.getTipoOperazione().equals("S")){
				if (this.getCodPoloSez() == (null) || (this.getCodPoloSez().length() == 0)) {
					errori.add("codPoloSezObbligatorio");
				}
				if (this.getCodPoloSez() != null && this.getCodPoloSez().length() != 0) {
					if (this.getCodPoloSez().length() > 3) {
						errori.add("codPoloSezEccedente");
					}
				}
				if (this.getCodBibSez() == (null) || (this.getCodBibSez()!=null && this.getCodBibSez().trim().length()==0)){
					errori.add("codBibSezObbligatorio");
				}
				if (this.getCodBibSez() != null && this.getCodBibSez().length() != 0) {
					if (this.getCodBibSez().length() > 3) {
						errori.add("codBibSezEccedente");
					}
				}
				if (this.getSezione() != null && this.getSezione().trim().length() != 0) {
					if (this.getSezione().length() > 10) {
						errori.add("codSezioneEccedente");
					}
				}

				if (this.getDallaCollocazione() == null) {
					errori.add("ERRORE: dallaCollNull");
				}
				if (this.getDallaCollocazione() != null && this.getDallaCollocazione().length() != 0) {
					if (this.getDallaCollocazione().length() > 48) {
						errori.add("ERRORE: dallaCollocazioneEccedente");
					}
				}
				if (this.getDallaSpecificazione() == null){
					errori.add("ERRORE: dallaSpecNull");
				}
				if (this.getDallaSpecificazione() != null && this.getDallaSpecificazione().length() != 0) {
					if (this.getDallaSpecificazione().length() > 24) {
						errori.add("ERRORE: dallaSpecificazioneEccedente");
					}
				}
				if (this.getAllaCollocazione() == null){
					errori.add("ERRORE: dallaCollNull");
				}
				if (this.getAllaCollocazione() != null && this.getAllaCollocazione().length() != 0) {
					if (this.getAllaCollocazione().length() > 48) {
						errori.add("ERRORE: dallaCollocazioneEccedente");
					}
				}
				if (this.getAllaSpecificazione() == null){
					errori.add("ERRORE: dallaSpecNull");
				}
				if (this.getAllaSpecificazione() !=null &&  this.getAllaSpecificazione().length()!=0)	{
					if (this.getAllaSpecificazione().length() > 24) {
						errori.add("ERRORE: dallaSpecificazioneEccedente");
					}
				}
			}else if (this.getTipoOperazione().equals("R")){
				if (this.getSerie() != null) {
					if (this.getSerie().length() > 3) {
						errori.add("ERRORE: codSerieEccedente");
					}
				}else{
					errori.add("ERRORE: codSerie = null");
				}

				if (!ValidazioneDati.strIsNull(this.getStartInventario())) {
					if (ValidazioneDati.strIsNumeric(this.getStartInventario())){
						if ((this.getStartInventario()).length() > 9) {
							errori.add("codInventDaEccedente");
						}
					}else{
						errori.add("ERRORE: codInventDaDeveEssereNumerico");
					}
				}else{
					errori.add("ERRORE: codInvent = null");
				}

				if (!ValidazioneDati.strIsNull(this.getEndInventario())) {
					if (ValidazioneDati.strIsNumeric(this.getEndInventario())){
						if ((this.getEndInventario()).length() > 9) {
							throw new ValidationException("codInventAEccedente");
						}
					}else{
						errori.add("ERRORE: codInventADeveEssereNumerico");
					}
				}else{
					errori.add("ERRORE: codInvent = null");
				}


				if (this.getEndInventario() != null && this.getStartInventario() != null){
					if (Integer.valueOf(this.getEndInventario()) < Integer.valueOf(this.getStartInventario()) ) {
						errori.add("codInventDaDeveEssereMinoreDiCodInventA");
					}
				} else {
					errori.add("ERRORE: codInvent = null");
				}

			} else if (this.getTipoOperazione().equals("N")) {
				// almaviva2 Settembre 2017: modifica a Stampa Strumenti Patrimonio per consentire la stampa per una lista di titoli proveniente
				// da file o dalla valorizzazione dei campindi bid sulla mappa
				if (ValidazioneDati.isFilled(this.getListaBid()) ) {
					// Non deve fare nulla e continuare con i controlli
				} else {
					if (this.getListaInventari() == null || this.getListaInventari().size() < 1){
						errori.add("ERRORE: listaInventariVuota");
					}
					if (this.getSelezione() == null){
						errori.add("ERRORE: Folder Inventari senza Selezione");
					}
				}

			}else if (this.getTipoOperazione().equals("D")){
				if (this.getDataDa() != null && this.getDataDa().length() != 0) {
					int codRitorno = ValidazioneDati.validaData(this.getDataDa());
					if (codRitorno != ValidazioneDati.DATA_OK)
						errori.add("ERRORE: data Da Errata");
				}
				if (this.getDataA() != null && this.getDataA().length() != 0) {
					int codRitorno = ValidazioneDati.validaData(this.getDataA());
					if (codRitorno != ValidazioneDati.DATA_OK)
						errori.add("ERRORE: data A Errata");
				}
				if (!this.getDataDa().trim().equals("") && !this.getDataDa().equals("00/00/0000")){
					if (this.getDataDa().compareTo(this.getDataA()) < 0){
						errori.add("ERRORE: data da " + this.getDataDa() + " < di data a "+ this.getDataA());
					}
				}
				if (!this.getDataA().trim().equals("") && !this.getDataA().equals("00/00/0000")){
					if (this.getDataDa().compareTo(this.getDataA()) == 0){
						errori.add("ERRORE: data A " + this.getDataDa() + " > di data da "+ this.getDataA());
					}
				}
				if (this.getTipoOrdinamento() == null ){
					errori.add("ERRORE: ordinamento non impostato");
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public String getCodPoloSez() {
		return codPoloSez;
	}

	public void setCodPoloSez(String codPoloSez) {
		this.codPoloSez = codPoloSez;
	}

	public String getCodBibSez() {
		return codBibSez;
	}

	public void setCodBibSez(String codBibSez) {
		this.codBibSez = codBibSez;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getTipoSezione() {
		return tipoSezione;
	}

	public void setTipoSezione(String tipoSezione) {
		this.tipoSezione = tipoSezione;
	}

	public String getDallaCollocazione() {
		return dallaCollocazione;
	}

	public void setDallaCollocazione(String dallaCollocazione) {
		this.dallaCollocazione = dallaCollocazione;
	}

	public String getDallaSpecificazione() {
		return dallaSpecificazione;
	}

	public void setDallaSpecificazione(String dallaSpecificazione) {
		this.dallaSpecificazione = dallaSpecificazione;
	}

	public String getAllaCollocazione() {
		return allaCollocazione;
	}

	public void setAllaCollocazione(String allaCollocazione) {
		this.allaCollocazione = allaCollocazione;
	}

	public String getAllaSpecificazione() {
		return allaSpecificazione;
	}

	public void setAllaSpecificazione(String allaSpecificazione) {
		this.allaSpecificazione = allaSpecificazione;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getEndInventario() {
		return endInventario;
	}

	public void setEndInventario(String endInventario) {
		this.endInventario = endInventario;
	}

	public String getStartInventario() {
		return startInventario;
	}

	public void setStartInventario(String startInventario) {
		this.startInventario = startInventario;
	}

	public List getListaInventari() {
		return listaInventari;
	}

	public void setListaInventari(List listaInventari) {
		this.listaInventari = listaInventari;
	}

	public List getListaBid() {
		return listaBid;
	}

	public void setListaBid(List listaBid) {
		this.listaBid = listaBid;
	}

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public String getSelezione() {
		return selezione;
	}

	public void setSelezione(String selezione) {
		this.selezione = selezione;
	}

	public String getNomeFileAppoggioInv() {
		return nomeFileAppoggioInv;
	}

	public void setNomeFileAppoggioInv(String nomeFileAppoggioInv) {
		this.nomeFileAppoggioInv = nomeFileAppoggioInv;
	}

	public String getNomeFileAppoggioBid() {
		return nomeFileAppoggioBid;
	}

	public void setNomeFileAppoggioBid(String nomeFileAppoggioBid) {
		this.nomeFileAppoggioBid = nomeFileAppoggioBid;
	}

	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}

	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}

	public String getTipoMateriale() {
		return tipoMateriale;
	}

	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
	}

	public String getStatoConservazione() {
		return statoConservazione;
	}

	public void setStatoConservazione(String statoConservazione) {
		this.statoConservazione = statoConservazione;
	}

	public String getDigitalizzazione() {
		return digitalizzazione;
	}

	public void setDigitalizzazione(String digitalizzazione) {
		this.digitalizzazione = digitalizzazione;
	}

	public String getNoDispo() {
		return noDispo;
	}

	public void setNoDispo(String noDispo) {
		this.noDispo = noDispo;
	}

	public String getDataDa() {
		return dataDa;
	}

	public void setDataDa(String dataDa) {
		this.dataDa = dataDa;
	}

	public String getDataA() {
		return dataA;
	}

	public void setDataA(String dataA) {
		this.dataA = dataA;
	}

	public boolean isNuoviTitoli() {
		return nuoviTitoli;
	}

	public void setNuoviTitoli(boolean nuoviTitoli) {
		this.nuoviTitoli = nuoviTitoli;
	}

	public boolean isNuoviEsemplari() {
		return nuoviEsemplari;
	}

	public void setNuoviEsemplari(boolean nuoviEsemplari) {
		this.nuoviEsemplari = nuoviEsemplari;
	}

	public String getDescrizioneND() {
		return descrizioneND;
	}

	public void setDescrizioneND(String descrizioneND) {
		this.descrizioneND = descrizioneND;
	}

	public String getDescrizioneTP() {
		return descrizioneTP;
	}

	public void setDescrizioneTP(String descrizioneTP) {
		this.descrizioneTP = descrizioneTP;
	}

	public String getDescrizioneSC() {
		return descrizioneSC;
	}

	public void setDescrizioneSC(String descrizioneSC) {
		this.descrizioneSC = descrizioneSC;
	}

	public String getDescrizioneDigitalizzazione() {
		return descrizioneDigitalizzazione;
	}

	public void setDescrizioneDigitalizzazione(String descrizioneDigitalizzazione) {
		this.descrizioneDigitalizzazione = descrizioneDigitalizzazione;
	}

}
