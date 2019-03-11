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
package it.iccu.sbn.web.actionforms.documentofisico.elaborazioniDifferite;

import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.web.actionforms.common.documentofisico.RicercaInventariCollocazioniForm;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

public class SpostamentoCollocazioniForm extends RicercaInventariCollocazioniForm {


	private static final long serialVersionUID = 5451127580493786458L;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private SezioneCollocazioneVO recSezioneP;
	private SezioneCollocazioneVO recSezioneA;

	private String tipoOperazione;
	private String tipoSezioneP;
	private String codPoloSezArrivo;
	private String codBibSezArrivo;
	private String codSezArrivo;
	private String tipoSezioneArrivo;
	private String collocazioneProvvisoria;
	private String specificazioneProvvisoria;
	private boolean disableTastoCollProvv;
	private boolean disableTastoSpecifProvv;
	private boolean disableCollProvv;
	private boolean disableSpecProvv;
	private boolean disableSezP;
	private boolean disableSezioneDiArrivo;
	private boolean allaSez;
	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );

	private boolean prestito;
	private boolean ristampa;
	private String ristEtichette;
	private List listaBiblioteche;

	private boolean disable;
	private String ticket;
	private boolean sessione;

	private boolean sezANonEsiste;
	private boolean sezPNonEsiste;
	private boolean flNessunaSezione;
	private boolean disableNessunaSezione = true;
	private boolean flNessunaCollocazione;
	private boolean disableNessunaCollocazione = true;
	private boolean collProvv;
	private boolean specProvv;


	private String azione;

	public boolean isSessione() {
		return sessione;
	}
	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getCodBib() {
		return codBib;
	}
	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}
	public String getCollocazioneProvvisoria() {
		return collocazioneProvvisoria;
	}
	public void setCollocazioneProvvisoria(String collocazioneProvvisoria) {
		this.collocazioneProvvisoria = collocazioneProvvisoria;
	}
	public String getDescrBib() {
		return descrBib;
	}
	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public List getListaBiblioteche() {
		return listaBiblioteche;
	}
	public void setListaBiblioteche(List listaBiblioteche) {
		this.listaBiblioteche = listaBiblioteche;
	}
	public boolean isPrestito() {
		return prestito;
	}
	public void setPrestito(boolean prestito) {
		this.prestito = prestito;
	}
	public boolean isRistampa() {
		return ristampa;
	}
	public void setRistampa(boolean ristampa) {
		this.ristampa = ristampa;
	}
	public String getRistEtichette() {
		return ristEtichette;
	}
	public void setRistEtichette(String ristEtichette) {
		this.ristEtichette = ristEtichette;
	}
	public String getSpecificazioneProvvisoria() {
		return specificazioneProvvisoria;
	}
	public void setSpecificazioneProvvisoria(String specificazioneProvvisoria) {
		this.specificazioneProvvisoria = specificazioneProvvisoria;
	}
//	public RicercaSerieVO getFrameColl() {
//		return frameColl;
//	}
//	public void setFrameColl(RicercaSerieVO frameColl) {
//		this.frameColl = frameColl;
//	}
	public String getCodPolo() {
		return codPolo;
	}
	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}
	public boolean isDisableSezioneDiArrivo() {
		return disableSezioneDiArrivo;
	}
	public void setDisableSezioneDiArrivo(boolean disableSezioneDiArrivo) {
		this.disableSezioneDiArrivo = disableSezioneDiArrivo;
	}
	public boolean isDisableCollProvv() {
		return disableCollProvv;
	}
	public void setDisableCollProvv(boolean disableCollProvv) {
		this.disableCollProvv = disableCollProvv;
	}
	public boolean isDisableSpecProvv() {
		return disableSpecProvv;
	}
	public void setDisableSpecProvv(boolean disableSpecProvv) {
		this.disableSpecProvv = disableSpecProvv;
	}
	public int getNRec() {
		return nRec;
	}
	public void setNRec(int rec) {
		nRec = rec;
	}
	public boolean isDisableTastoCollProvv() {
		return disableTastoCollProvv;
	}
	public void setDisableTastoCollProvv(boolean disableTastoCollProvv) {
		this.disableTastoCollProvv = disableTastoCollProvv;
	}
	public boolean isDisableTastoSpecifProvv() {
		return disableTastoSpecifProvv;
	}
	public void setDisableTastoSpecifProvv(boolean disableTastoSpecifProvv) {
		this.disableTastoSpecifProvv = disableTastoSpecifProvv;
	}
	public String getCodBibSezArrivo() {
		return codBibSezArrivo;
	}
	public void setCodBibSezArrivo(String codBibSezArrivo) {
		this.codBibSezArrivo = codBibSezArrivo;
	}
	public String getCodPoloSezArrivo() {
		return codPoloSezArrivo;
	}
	public void setCodPoloSezArrivo(String codPoloSezArrivo) {
		this.codPoloSezArrivo = codPoloSezArrivo;
	}
	public String getTipoOperazione() {
		return tipoOperazione;
	}
	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}
	public String getTipoSezioneArrivo() {
		return tipoSezioneArrivo;
	}
	public void setTipoSezioneArrivo(String tipoSezioneArrivo) {
		this.tipoSezioneArrivo = tipoSezioneArrivo;
	}
	public String getTipoSezioneP() {
		return tipoSezioneP;
	}
	public void setTipoSezioneP(String tipoSezioneP) {
		this.tipoSezioneP = tipoSezioneP;
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
	public boolean isDisableTastoDallaColl() {
		return disableTastoDallaColl;
	}
	public void setDisableTastoDallaColl(boolean disableTastoDallaColl) {
		this.disableTastoDallaColl = disableTastoDallaColl;
	}
	public boolean isDisableTastoDallaSpecif() {
		return disableTastoDallaSpecif;
	}
	public void setDisableTastoDallaSpecif(boolean disableTastoDallaSpecif) {
		this.disableTastoDallaSpecif = disableTastoDallaSpecif;
	}
	public boolean isDisableTastoAllaColl() {
		return disableTastoAllaColl;
	}
	public void setDisableTastoAllaColl(boolean disableTastoAllaColl) {
		this.disableTastoAllaColl = disableTastoAllaColl;
	}
	public boolean isDisableTastoAllaSpecif() {
		return disableTastoAllaSpecif;
	}
	public void setDisableTastoAllaSpecif(boolean disableTastoAllaSpecif) {
		this.disableTastoAllaSpecif = disableTastoAllaSpecif;
	}
	public boolean isDisableSezP() {
		return disableSezP;
	}
	public void setDisableSezP(boolean disableSezP) {
		this.disableSezP = disableSezP;
	}
	public boolean isDisableDallaColl() {
		return disableDallaColl;
	}
	public void setDisableDallaColl(boolean disableDallaColl) {
		this.disableDallaColl = disableDallaColl;
	}
	public boolean isDisableDallaSpec() {
		return disableDallaSpec;
	}
	public void setDisableDallaSpec(boolean disableDallaSpec) {
		this.disableDallaSpec = disableDallaSpec;
	}
	public boolean isDisableAllaColl() {
		return disableAllaColl;
	}
	public void setDisableAllaColl(boolean disableAllaColl) {
		this.disableAllaColl = disableAllaColl;
	}
	public boolean isDisableAllaSpec() {
		return disableAllaSpec;
	}
	public void setDisableAllaSpec(boolean disableAllaSpec) {
		this.disableAllaSpec = disableAllaSpec;
	}
	public boolean isSezANonEsiste() {
		return sezANonEsiste;
	}
	public void setSezANonEsiste(boolean sezANonEsiste) {
		this.sezANonEsiste = sezANonEsiste;
	}
	public boolean isSezPNonEsiste() {
		return sezPNonEsiste;
	}
	public void setSezPNonEsiste(boolean sezPNonEsiste) {
		this.sezPNonEsiste = sezPNonEsiste;
	}
	public boolean isFlNessunaSezione() {
		return flNessunaSezione;
	}
	public void setFlNessunaSezione(boolean flNessunaSezione) {
		this.flNessunaSezione = flNessunaSezione;
	}
	public boolean isDisableNessunaSezione() {
		return disableNessunaSezione;
	}
	public void setDisableNessunaSezione(boolean disableNessunaSezione) {
		this.disableNessunaSezione = disableNessunaSezione;
	}
	public SezioneCollocazioneVO getRecSezioneA() {
		return recSezioneA;
	}
	public void setRecSezioneA(SezioneCollocazioneVO recSezioneA) {
		this.recSezioneA = recSezioneA;
	}
	public String getCodSezArrivo() {
		return codSezArrivo;
	}
	public void setCodSezArrivo(String codSezArrivo) {
		this.codSezArrivo = codSezArrivo;
	}
	public boolean isDisableNessunaCollocazione() {
		return disableNessunaCollocazione;
	}
	public void setDisableNessunaCollocazione(boolean disableNessunaCollocazione) {
		this.disableNessunaCollocazione = disableNessunaCollocazione;
	}
	public boolean isFlNessunaCollocazione() {
		return flNessunaCollocazione;
	}
	public void setFlNessunaCollocazione(boolean flNessunaCollocazione) {
		this.flNessunaCollocazione = flNessunaCollocazione;
	}
	public SezioneCollocazioneVO getRecSezioneP() {
		return recSezioneP;
	}
	public void setRecSezioneP(SezioneCollocazioneVO recSezioneP) {
		this.recSezioneP = recSezioneP;
	}
	public boolean isAllaSez() {
		return allaSez;
	}
	public void setAllaSez(boolean allaSez) {
		this.allaSez = allaSez;
	}
	public boolean isCollProvv() {
		return collProvv;
	}
	public void setCollProvv(boolean collProvv) {
		this.collProvv = collProvv;
	}
	public boolean isSpecProvv() {
		return specProvv;
	}
	public void setSpecProvv(boolean specProvv) {
		this.specProvv = specProvv;
	}
	public String getAzione() {
		return azione;
	}
	public void setAzione(String azione) {
		this.azione = azione;
	}

}
