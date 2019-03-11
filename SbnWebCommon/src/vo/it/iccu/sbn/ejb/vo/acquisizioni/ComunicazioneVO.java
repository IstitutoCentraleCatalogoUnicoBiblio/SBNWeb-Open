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
package it.iccu.sbn.ejb.vo.acquisizioni;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaQuinquies;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;

import java.sql.Timestamp;
import java.util.List;

public class ComunicazioneVO extends SerializableVO {

	private static final long serialVersionUID = 2877840014330726391L;

	private String ticket;
	private Integer progressivo=0;
	private String utente;
	private String codPolo;
	private String codBibl;
	private String denoBibl="";
	private String mittenteIndirizzo="";
	private String mittenteCap="";
	private String mittenteCitta="";
	private String mittenteTelefono="";
	private String mittenteFax="";

	private String codiceMessaggio;
	private String desMessaggio;
	private String tipoDocumento;
	private String tipoMessaggio;
	private StrutturaCombo fornitore;
	private FornitoreVO anagFornitore; // NEW
	private String codCliFornitore="";

	private StrutturaTerna idDocumento;
	private String statoComunicazione;
	private String desStato;
	private String dataComunicazione;
	private String direzioneComunicazione;
	private String direzioneComunicazioneLabel;
	private String tipoInvioComunicazione;
	private String tipoInvioDes;
	private String noteComunicazione;
	private String tipoVariazione;
	private boolean flag_canc=false;
	private Timestamp dataUpd;
	private StrutturaQuinquies docORDINE; // anno_abb, data_fasc , data_fine, titolo , rinnovo di origine
	private String etichetta_ISBN=""; // COD I
	private String etichetta_ISSN=""; // COD J
	private String etichetta_ISMN=""; // COD M
	private String etichetta_NUMEROEDITORIALE=""; // COD E
	private String etichetta_NUMERODILASTRA=""; // COD L
	private String etichetta_NPUBBLICAZIONEGOVERNATIVA=""; // COD G
	private List<StrutturaTerna> numStandardArr; // tipo, numero, denotipo

	private List<FascicoloVO> fascicoli;


	public ComunicazioneVO() {
		super();
	};

	public ComunicazioneVO (String codP, String codB, String codMsg, String tipoDoc,String tipoMsg, StrutturaCombo forn, StrutturaTerna idDoc, String statoCom,String dataCom, String dirCom, String tipoInvioCom, String noteCom) throws Exception {
		//if (ese == null) {
		//	throw new Exception("Esercizio non valido");
		//}
		this.codPolo=codP;
		this.codBibl=codB;
		this.codiceMessaggio=codMsg;
		this.tipoDocumento=tipoDoc;
		this.tipoMessaggio=tipoMsg;
		this.fornitore=forn;
		this.idDocumento=idDoc;
		this.statoComunicazione=statoCom;
		this.dataComunicazione=dataCom;
		this.direzioneComunicazione=dirCom;
		this.tipoInvioComunicazione=tipoInvioCom;
		this.noteComunicazione=trimAndSet(noteCom);

	}

	public String getChiave() {
		String chiave=getCodBibl()+ "|" + getCodiceMessaggio() ;
		chiave=chiave.trim();
		return chiave;
	}
	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodiceMessaggio() {
		return codiceMessaggio;
	}

	public void setCodiceMessaggio(String codiceMessaggio) {
		this.codiceMessaggio = codiceMessaggio;
	}

	public String getDataComunicazione() {
		return dataComunicazione;
	}

	public void setDataComunicazione(String dataComunicazione) {
		this.dataComunicazione = dataComunicazione;
	}

	public String getDirezioneComunicazione() {
		return direzioneComunicazione;
	}

	public void setDirezioneComunicazione(String direzioneComunicazione) {
		this.direzioneComunicazione = direzioneComunicazione;
	}

	public StrutturaCombo getFornitore() {
		return fornitore;
	}

	public void setFornitore(StrutturaCombo fornitore) {
		this.fornitore = fornitore;
	}

	public StrutturaTerna getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(StrutturaTerna idDocumento) {
		this.idDocumento = idDocumento;
	}

	public String getNoteComunicazione() {
		return noteComunicazione;
	}

	public void setNoteComunicazione(String noteComunicazione) {
		this.noteComunicazione = trimAndSet(noteComunicazione);
	}

	public String getStatoComunicazione() {
		return statoComunicazione;
	}

	public void setStatoComunicazione(String statoComunicazione) {
		this.statoComunicazione = statoComunicazione;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getTipoInvioComunicazione() {
		return tipoInvioComunicazione;
	}

	public void setTipoInvioComunicazione(String tipoInvioComunicazione) {
		this.tipoInvioComunicazione = tipoInvioComunicazione;
	}

	public String getTipoMessaggio() {
		return tipoMessaggio;
	}

	public void setTipoMessaggio(String tipoMessaggio) {
		this.tipoMessaggio = tipoMessaggio;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getTipoVariazione() {
		return tipoVariazione;
	}
	public void setTipoVariazione(String tipoVariazione) {
		this.tipoVariazione = tipoVariazione;
	}
	public Integer getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(Integer progressivo) {
		this.progressivo = progressivo;
	}
	public String getUtente() {
		return utente;
	}
	public void setUtente(String utente) {
		this.utente = utente;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}
	public String getDirezioneComunicazioneLabel() {
		return direzioneComunicazioneLabel;
	}
	public void setDirezioneComunicazioneLabel(String direzioneComunicazioneLabel) {
		this.direzioneComunicazioneLabel = direzioneComunicazioneLabel;
	}
	public String getDesMessaggio() {
		return desMessaggio;
	}
	public void setDesMessaggio(String desMessaggio) {
		this.desMessaggio = desMessaggio;
	}
	public String getTipoInvioDes() {
		return tipoInvioDes;
	}
	public void setTipoInvioDes(String tipoInvioDes) {
		this.tipoInvioDes = tipoInvioDes;
	}
	public String getDesStato() {
		return desStato;
	}
	public void setDesStato(String desStato) {
		this.desStato = desStato;
	}
	public Timestamp getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Timestamp dataUpd) {
		this.dataUpd = dataUpd;
	}
	public FornitoreVO getAnagFornitore() {
		return anagFornitore;
	}
	public void setAnagFornitore(FornitoreVO anagFornitore) {
		this.anagFornitore = anagFornitore;
	}
	public void leggiNumSdt()
	{
		if (this.numStandardArr!=null && this.numStandardArr.size()>0)
		{
			//ordineDaStampare.set
			for (int x=0; x< this.numStandardArr.size(); x++)
			{
				if (this.numStandardArr.get(x).getCodice1().equals("I"))
				{
					this.setEtichetta_ISBN(this.numStandardArr.get(x).getCodice2());
				}
				if (this.numStandardArr.get(x).getCodice1().equals("J"))
				{
					this.setEtichetta_ISSN(this.numStandardArr.get(x).getCodice2());
				}
				if (this.numStandardArr.get(x).getCodice1().equals("M"))
				{
					this.setEtichetta_ISMN(this.numStandardArr.get(x).getCodice2());
				}
				if (this.numStandardArr.get(x).getCodice1().equals("E"))
				{
					this.setEtichetta_NUMEROEDITORIALE(this.numStandardArr.get(x).getCodice2());
				}
				if (this.numStandardArr.get(x).getCodice1().equals("L"))
				{
					this.setEtichetta_NUMERODILASTRA(this.numStandardArr.get(x).getCodice2());
				}
				if (this.numStandardArr.get(x).getCodice1().equals("G"))
				{
					this.setEtichetta_NPUBBLICAZIONEGOVERNATIVA(this.numStandardArr.get(x).getCodice2());
				}
			}
		}
	return ;
	}


	public String getDocCodificato() {
		String ret;
		String sep="";
		if(this.tipoDocumento.equalsIgnoreCase("F")){
			ret="Fattura: ";
			if(this.idDocumento.getCodice2()!=null && !this.idDocumento.getCodice2().trim().equals("")){
				ret=ret+sep+this.idDocumento.getCodice2().trim();
				sep=" - ";
			}
			if(this.idDocumento.getCodice3()!=null && !this.idDocumento.getCodice3().trim().equals("")){
				ret=ret+sep+this.idDocumento.getCodice3().trim();
				sep=" - ";
			}
		} else if(this.tipoDocumento.equalsIgnoreCase("O")){
			ret="Ordine: ";
			if(this.idDocumento.getCodice1()!=null && !this.idDocumento.getCodice1().trim().equals("")){
				ret=ret+sep+this.idDocumento.getCodice1().trim();
				sep=" - ";
			}
			if(this.idDocumento.getCodice2()!=null && !this.idDocumento.getCodice2().trim().equals("")){
				ret=ret+sep+this.idDocumento.getCodice2().trim();
				sep=" - ";
			}
			if(this.idDocumento.getCodice3()!=null && !this.idDocumento.getCodice3().trim().equals("")){
				ret=ret+sep+this.idDocumento.getCodice3().trim();
				sep=" - ";
			}
		} else {
			ret="generico ";
		}
		return ret;
	}
	public String getDenoBibl() {
		return denoBibl;
	}
	public void setDenoBibl(String denoBibl) {
		this.denoBibl = denoBibl;
	}
	public String getCodCliFornitore() {
		return codCliFornitore;
	}
	public void setCodCliFornitore(String codCliFornitore) {
		this.codCliFornitore = codCliFornitore;
	}
	public String getMittenteCap() {
		return mittenteCap;
	}
	public void setMittenteCap(String mittenteCap) {
		this.mittenteCap = mittenteCap;
	}
	public String getMittenteCitta() {
		return mittenteCitta;
	}
	public void setMittenteCitta(String mittenteCitta) {
		this.mittenteCitta = mittenteCitta;
	}
	public String getMittenteFax() {
		return mittenteFax;
	}
	public void setMittenteFax(String mittenteFax) {
		this.mittenteFax = mittenteFax;
	}
	public String getMittenteIndirizzo() {
		return mittenteIndirizzo;
	}
	public void setMittenteIndirizzo(String mittenteIndirizzo) {
		this.mittenteIndirizzo = mittenteIndirizzo;
	}
	public String getMittenteTelefono() {
		return mittenteTelefono;
	}
	public void setMittenteTelefono(String mittenteTelefono) {
		this.mittenteTelefono = mittenteTelefono;
	}

	public String getEtichetta_ISBN() {
		return etichetta_ISBN;
	}
	public void setEtichetta_ISBN(String etichetta_ISBN) {
		this.etichetta_ISBN = etichetta_ISBN;
	}
	public String getEtichetta_ISMN() {
		return etichetta_ISMN;
	}
	public void setEtichetta_ISMN(String etichetta_ISMN) {
		this.etichetta_ISMN = etichetta_ISMN;
	}
	public String getEtichetta_ISSN() {
		return etichetta_ISSN;
	}
	public void setEtichetta_ISSN(String etichetta_ISSN) {
		this.etichetta_ISSN = etichetta_ISSN;
	}
	public String getEtichetta_NPUBBLICAZIONEGOVERNATIVA() {
		return etichetta_NPUBBLICAZIONEGOVERNATIVA;
	}
	public void setEtichetta_NPUBBLICAZIONEGOVERNATIVA(
			String etichetta_NPUBBLICAZIONEGOVERNATIVA) {
		this.etichetta_NPUBBLICAZIONEGOVERNATIVA = etichetta_NPUBBLICAZIONEGOVERNATIVA;
	}
	public String getEtichetta_NUMERODILASTRA() {
		return etichetta_NUMERODILASTRA;
	}
	public void setEtichetta_NUMERODILASTRA(String etichetta_NUMERODILASTRA) {
		this.etichetta_NUMERODILASTRA = etichetta_NUMERODILASTRA;
	}
	public String getEtichetta_NUMEROEDITORIALE() {
		return etichetta_NUMEROEDITORIALE;
	}
	public void setEtichetta_NUMEROEDITORIALE(String etichetta_NUMEROEDITORIALE) {
		this.etichetta_NUMEROEDITORIALE = etichetta_NUMEROEDITORIALE;
	}
	public List<StrutturaTerna> getNumStandardArr() {
		return numStandardArr;
	}
	public void setNumStandardArr(List<StrutturaTerna> numStandardArr) {
		this.numStandardArr = numStandardArr;
	}
	public StrutturaQuinquies getDocORDINE() {
		return docORDINE;
	}
	public void setDocORDINE(StrutturaQuinquies docORDINE) {
		this.docORDINE = docORDINE;
	}

	public List<FascicoloVO> getFascicoli() {
		return fascicoli;
	}

	public void setFascicoli(List<FascicoloVO> fascicoli) {
		this.fascicoli = fascicoli;
	}
}
