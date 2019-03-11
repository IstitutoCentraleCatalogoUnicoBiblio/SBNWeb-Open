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
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaQuinquies;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SezioneVO extends SerializableVO {

	private static final long serialVersionUID = -5534775807651430420L;

	private String ticket;
	private Integer progressivo = 0;
	private String utente;
	private String codPolo;
	private String codBibl;
	private String codiceSezione;
	private int idSezione;
	private String descrizioneSezione;
	private double sommaDispSezione;
	private String sommaDispSezioneStr;
	private String noteSezione;
	private String annoValiditaSezione;
	private double budgetSezione;
	private double acquisito;
	private double ordinato;
	private String budgetSezioneStr;
	private String tipoVariazione;
	private boolean flag_canc = false;
	private String dataAgg;
	private String dataVal;
	private double budgetLetto;
	private double importoDelta;
	private List<RigheVO> righeEsameSpesa;
	private List<StrutturaQuinquies> righeEsameStoria;
	private Timestamp dataUpd;
	private boolean chiusa = false;

	public SezioneVO() {
		super();
	}

	public SezioneVO(String codP, String codB, String codSez, String desSez,
			double sommaSez, String noteSez, String annoValSez, double bdgSez,
			String tipoVar) throws Exception {
		// if (ese == null) {
		// throw new Exception("Esercizio non valido");
		// }
		// n.b sommaSez posso adoperarlo come valore di acquisito o ordinato per
		// il calcolo della disponibilità
		this.codPolo = codP;
		this.codBibl = codB;
		this.codiceSezione = codSez;
		this.descrizioneSezione = desSez;
		this.sommaDispSezione = bdgSez - sommaSez;
		this.noteSezione = noteSez;
		this.annoValiditaSezione = annoValSez;
		this.budgetSezione = bdgSez;
		this.tipoVariazione = tipoVar;
	}

	public String getChiave() {
		String chiave = getCodBibl() + "|" + getCodiceSezione();
		chiave = chiave.trim();
		return chiave;
	}

	public String getAnnoValiditaSezione() {
		return annoValiditaSezione;
	}

	public void setAnnoValiditaSezione(String annoValiditaSezione) {
		this.annoValiditaSezione = annoValiditaSezione;
	}

	public double getBudgetSezione() {
		return budgetSezione;
	}

	public void setBudgetSezione(double budgetSezione) throws Exception {
		this.budgetSezione = budgetSezione;
		if (budgetSezione != this.budgetLetto) {
			setImportoDelta(budgetSezione - this.budgetLetto);
		}
	}

	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}

	public String getCodiceSezione() {
		return codiceSezione;
	}

	public void setCodiceSezione(String codiceSezione) {
		this.codiceSezione = codiceSezione;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getDescrizioneSezione() {
		return descrizioneSezione;
	}

	public void setDescrizioneSezione(String descrizioneSezione) {
		this.descrizioneSezione = descrizioneSezione;
	}

	public String getNoteSezione() {
		return noteSezione;
	}

	public void setNoteSezione(String noteSezione) {
		this.noteSezione = noteSezione;
	}

	public double getSommaDispSezione() {
		return sommaDispSezione;
	}

	public void setSommaDispSezione(double sommaDispSezione) {
		this.sommaDispSezione = sommaDispSezione;
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

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
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

	public String getBudgetSezioneStr() {
		return budgetSezioneStr;
	}

	public void setBudgetSezioneStr(String budgetSezioneStr) {
		this.budgetSezioneStr = budgetSezioneStr;
	}

	public double getAcquisito() {
		return acquisito;
	}

	public void setAcquisito(double acquisito) {
		this.acquisito = acquisito;
	}

	public double getOrdinato() {
		return ordinato;
	}

	public void setOrdinato(double ordinato) {
		this.ordinato = ordinato;
	}

	public String getDataAgg() {
		return dataAgg;
	}

	public void setDataAgg(String dataAgg) {
		this.dataAgg = dataAgg;
	}

	public String getSommaDispSezioneStr() {
		return sommaDispSezioneStr;
	}

	public void setSommaDispSezioneStr(String sommaDispSezioneStr) {
		this.sommaDispSezioneStr = sommaDispSezioneStr;
	}

	public int getIdSezione() {
		return idSezione;
	}

	public void setIdSezione(int idSezione) {
		this.idSezione = idSezione;
	}

	public List<RigheVO> getRigheEsameSpesa() {
		return righeEsameSpesa;
	}

	public void setRigheEsameSpesa(List<RigheVO> righeEsameSpesa) {
		this.righeEsameSpesa = righeEsameSpesa;
	}

	public Timestamp getDataUpd() {
		return dataUpd;
	}

	public void setDataUpd(Timestamp dataUpd) {
		this.dataUpd = dataUpd;
	}

	public double getBudgetLetto() {
		return budgetLetto;
	}

	public void setBudgetLetto(double budgetLetto) {
		this.budgetLetto = budgetLetto;
	}

	public String getDataVal() {
		return dataVal;
	}

	public void setDataVal(String dataVal) {
		this.dataVal = dataVal;
		Date dataodierna = new Date(System.currentTimeMillis());
		// if (date.after(oggi)) {
		// Date dataodierna=new Date();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		try {
			// formato.setLenient(false); // Date date =
			// DateParser.parseDate(data);
			// l'istruzione sottostante va in errore se non non riesce a fare il
			// parsing del rispetto del formato
			java.util.Date data = formato.parse(dataVal);
			if (data.before(dataodierna)) {
				this.chiusa = true;
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}

	}

	public double getImportoDelta() {
		return importoDelta;
	}

	public void setImportoDelta(double importoDelta) {
		this.importoDelta = importoDelta;
	}

	public String getImportoDeltaStr() {
		String importoDeltaStr = null;
		try {
			double importoDeltaStrAppo = this.getImportoDelta();
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setGroupingSeparator('.');
			dfs.setDecimalSeparator(',');
			// controllo formattazione con virgola separatore dei decimali
			DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
			// importo
			String stringa = df.format(importoDeltaStrAppo);
			NumberFormat formatIT = NumberFormat.getCurrencyInstance();
			String strCurrency = "\u20AC ";
			strCurrency = strCurrency + stringa;
			// Number imp=formatIT.parse(strCurrency); // va in errore se non è
			// riconosciuto come formato euro
			importoDeltaStr = formatIT.format(importoDeltaStrAppo);
			importoDeltaStr = importoDeltaStr.substring(2); // elimina il
															// simbolo

		} catch (Exception e) {
			e.printStackTrace();
		}
		return importoDeltaStr;
	}

	public boolean isChiusa() {
		return chiusa;
	}

	public void setChiusa(boolean chiusa) {
		this.chiusa = chiusa;
	}

	public List<StrutturaQuinquies> getRigheEsameStoria() {
		return righeEsameStoria;
	}

	public void setRigheEsameStoria(
			List<StrutturaQuinquies> righeEsameStoria) {
		this.righeEsameStoria = righeEsameStoria;
	}

}
