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

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;


public class FatturaVO extends SerializableVO {

	private static final long serialVersionUID = -3055593577113483005L;

	private String ticket;
	private Integer IDFatt;
	private Integer progressivo=0;
	private String utente;
	private String codPolo;
	private String codBibl;
	private String denoBibl;
	private String annoFattura;
	private String progrFattura;
	private String numFattura;
	private String dataFattura;
	private String dataRegFattura;
	private double importoFattura;
	private String importoFatturaStr;
	private double scontoFattura;
	private String scontoFatturaStr;
	private String valutaFattura;
	private double cambioFattura;
	private String statoFattura;
	private String denoStatoFattura; // NEW
	private String tipoFattura;
	private StrutturaCombo fornitoreFattura;
	private FornitoreVO anagFornitore; // NEW
	private List<StrutturaFatturaVO> righeDettaglioFattura;
	private String tipoVariazione;
	private boolean flag_canc=false;
	private boolean fatturaVeloce=false;
	private Timestamp dataUpd;
	private boolean  gestBil=true;




	public FatturaVO (){};
	public FatturaVO (String codP, String codB, String annoF, String prgF , String numF, String dataF, String dataRegF, double impF, double scoF, String valF, double camF,String  staF,String tipF , StrutturaCombo fornFatt, String tipoVar) throws Exception {
		if (annoF == null) {
			throw new Exception("Anno fattura non valido");
		}
		if (prgF == null) {
			throw new Exception("Progressivo fattura non valido");
		}
		this.codPolo = codP;
		this.codBibl = codB;
		this.annoFattura = annoF;
		this.progrFattura = prgF;
		this.numFattura = numF;
		this.dataFattura = dataF;
		this.dataRegFattura = dataRegF;
		this.importoFattura = impF;
		this.scontoFattura = scoF;
		this.valutaFattura = valF;
		this.cambioFattura = camF;
		this.statoFattura = staF;
		this.tipoFattura = tipF;
		this.fornitoreFattura = fornFatt;
		this.tipoVariazione=tipoVar;

	}

	public String getChiave() {
		String chiave=getCodBibl()+ "|" + getAnnoFattura()+ "|" +  getProgrFattura() ;
		chiave=chiave.trim();
		return chiave;
	}


	public String getAnnoFattura() {
		return annoFattura;
	}



	public void setAnnoFattura(String annoFattura) {
		this.annoFattura = annoFattura;
	}



	public double getCambioFattura() {
		return cambioFattura;
	}



	public void setCambioFattura(double cambioFattura) {
		this.cambioFattura = cambioFattura;
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



	public String getDataFattura() {
		return dataFattura;
	}



	public void setDataFattura(String dataFattura) {
		this.dataFattura = dataFattura;
	}



	public String getDataRegFattura() {
		return dataRegFattura;
	}



	public void setDataRegFattura(String dataRegFattura) {
		this.dataRegFattura = dataRegFattura;
	}



	public double getImportoFattura() {
		return importoFattura;
	}



	public void setImportoFattura(double importoFattura) {
		this.importoFattura = importoFattura;
	}



	public String getNumFattura() {
		return numFattura;
	}



	public void setNumFattura(String numFattura) {
		this.numFattura = numFattura;
	}



	public String getProgrFattura() {
		return progrFattura;
	}



	public void setProgrFattura(String progrFattura) {
		this.progrFattura = progrFattura;
	}



	public double getScontoFattura() {
		return scontoFattura;
	}



	public void setScontoFattura(double scontoFattura) {
		this.scontoFattura = scontoFattura;
	}



	public String getStatoFattura() {
		return statoFattura;
	}



	public void setStatoFattura(String statoFattura) {
		this.statoFattura = statoFattura;
	}



	public String getTipoFattura() {
		return tipoFattura;
	}



	public void setTipoFattura(String tipoFattura) {
		this.tipoFattura = tipoFattura;
	}



	public String getValutaFattura() {
		return valutaFattura;
	}



	public void setValutaFattura(String valutaFattura) {
		this.valutaFattura = valutaFattura;
	}



	public StrutturaCombo getFornitoreFattura() {
		return fornitoreFattura;
	}



	public void setFornitoreFattura(StrutturaCombo fornitoreFattura) {
		this.fornitoreFattura = fornitoreFattura;
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
	public List<StrutturaFatturaVO> getRigheDettaglioFattura() {
		return righeDettaglioFattura;
	}
	public void setRigheDettaglioFattura(
			List<StrutturaFatturaVO> righeDettaglioFattura) {
		this.righeDettaglioFattura = righeDettaglioFattura;
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
	public Integer getIDFatt() {
		return IDFatt;
	}
	public void setIDFatt(Integer fatt) {
		IDFatt = fatt;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}
	public String getImportoFatturaStr() {
		return importoFatturaStr;
	}
	public void setImportoFatturaStr(String importoFatturaStr) {
		this.importoFatturaStr = importoFatturaStr;
	}
	public String getScontoFatturaStr() {
		return scontoFatturaStr;
	}
	public void setScontoFatturaStr(String scontoFatturaStr) {
		this.scontoFatturaStr = scontoFatturaStr;
	}
	public boolean isFatturaVeloce() {
		return fatturaVeloce;
	}
	public void setFatturaVeloce(boolean fatturaVeloce) {
		this.fatturaVeloce = fatturaVeloce;
	}
	public double getImponibileDouble() {
	    double imponibileDouble=0.00;
	    try {
	    	if (this.righeDettaglioFattura!=null && this.righeDettaglioFattura.size()>0)
	    	{
				for (int w=0;  w < righeDettaglioFattura.size(); w++)
				{
					imponibileDouble= imponibileDouble + righeDettaglioFattura.get(w).getImportoScontatoNOIvaDouble();
				}
	    	}

	    } catch (Exception e) {
	    	e.printStackTrace();
		}
		return imponibileDouble;
	}
	public String getImponibileStr() {
		String imponibileStr=null;
	    try {
			double imponibileDouble=this.getImponibileDouble();
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		    dfs.setGroupingSeparator('.');
		    dfs.setDecimalSeparator(',');
		    // controllo formattazione con virgola separatore dei decimali
	    	 DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
	    	 // importo
	    	String stringa  = df.format(imponibileDouble);
	    	NumberFormat formatIT = NumberFormat.getCurrencyInstance();
		    String strCurrency="\u20AC ";
		    strCurrency=strCurrency+stringa;
		    //Number imp=formatIT.parse(strCurrency); // va in errore se non è riconosciuto come formato euro
		    imponibileStr=formatIT.format(imponibileDouble);
		    imponibileStr=imponibileStr.substring(2); // elimina il simbolo

	    } catch (Exception e) {
	    	e.printStackTrace();
		}
		return imponibileStr;
	}

	public double getTotIvaDouble() {
	    double totIvaDouble=0.00;
	    try {
	    	if (this.righeDettaglioFattura!=null && this.righeDettaglioFattura.size()>0)
	    	{
				for (int w=0;  w < righeDettaglioFattura.size(); w++)
				{
					totIvaDouble= totIvaDouble + righeDettaglioFattura.get(w).getCalcoloIvaRigaDouble(this.scontoFattura);
				}
	    	}

	    } catch (Exception e) {
	    	e.printStackTrace();
		}
		return totIvaDouble;
	}

	public String getTotIvaStr() {
		String totaleIvaStr=null;
	    try {
			double totIvaDoubleAppo=this.getTotIvaDouble();
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		    dfs.setGroupingSeparator('.');
		    dfs.setDecimalSeparator(',');
		    // controllo formattazione con virgola separatore dei decimali
	    	 DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
	    	 // importo
	    	String stringa  = df.format(totIvaDoubleAppo);
	    	NumberFormat formatIT = NumberFormat.getCurrencyInstance();
		    String strCurrency="\u20AC ";
		    strCurrency=strCurrency+stringa;
		    //Number imp=formatIT.parse(strCurrency); // va in errore se non è riconosciuto come formato euro
		    totaleIvaStr=formatIT.format(totIvaDoubleAppo);
		    totaleIvaStr=totaleIvaStr.substring(2); // elimina il simbolo

	    } catch (Exception e) {
	    	e.printStackTrace();
		}
		return totaleIvaStr;
	}

	public double getTotaleFattInValutaDouble() {
	    double totFattInValutaDouble=0.00;
	    try {
	    	//totFattEuroDouble=this.importoFattura * this.getCambioFattura();
	    	totFattInValutaDouble=this.getImponibileDouble() - this.getTotaleSconto()+this.getTotIvaDouble() * this.getCambioFattura();

	    } catch (Exception e) {
	    	e.printStackTrace();
		}
		return totFattInValutaDouble;
	}
	public String getTotaleFattInValutaStr() {
		String totaleFattInValutaStr=null;
	    try {
			double totFattInValutaDoubleAppo=this.getTotaleFattInValutaDouble();
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		    dfs.setGroupingSeparator('.');
		    dfs.setDecimalSeparator(',');
		    // controllo formattazione con virgola separatore dei decimali
	    	 DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
	    	 // importo
	    	String stringa  = df.format(totFattInValutaDoubleAppo);
	    	NumberFormat formatIT = NumberFormat.getCurrencyInstance();
		    String strCurrency="\u20AC ";
		    strCurrency=strCurrency+stringa;
		    //Number imp=formatIT.parse(strCurrency); // va in errore se non è riconosciuto come formato euro
		    totaleFattInValutaStr=formatIT.format(totFattInValutaDoubleAppo);
		    totaleFattInValutaStr=totaleFattInValutaStr.substring(2); // elimina il simbolo

	    } catch (Exception e) {
	    	e.printStackTrace();
		}
		return totaleFattInValutaStr;
	}


	public double getTotaleFattEuroDouble() {
	    double totFattEuroDouble=0.00;
	    try {
	    	//totFattEuroDouble=this.importoFattura * this.getCambioFattura();
	    	totFattEuroDouble=this.getTotaleFattInValutaDouble()* this.getCambioFattura();

	    } catch (Exception e) {
	    	e.printStackTrace();
		}
		return totFattEuroDouble;
	}

	public String getTotaleFattEuroStr() {
		String totaleFattEuroStr=null;
	    try {
			double totFattEuroDoubleAppo=this.getTotaleFattEuroDouble();
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		    dfs.setGroupingSeparator('.');
		    dfs.setDecimalSeparator(',');
		    // controllo formattazione con virgola separatore dei decimali
	    	 DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
	    	 // importo
	    	String stringa  = df.format(totFattEuroDoubleAppo);
	    	NumberFormat formatIT = NumberFormat.getCurrencyInstance();
		    String strCurrency="\u20AC ";
		    strCurrency=strCurrency+stringa;
		    //Number imp=formatIT.parse(strCurrency); // va in errore se non è riconosciuto come formato euro
		    totaleFattEuroStr=formatIT.format(totFattEuroDoubleAppo);
		    totaleFattEuroStr=totaleFattEuroStr.substring(2); // elimina il simbolo

	    } catch (Exception e) {
	    	e.printStackTrace();
		}
		return totaleFattEuroStr;
	}


	public double getTotaleSconto() {
	    double totScontoFattDouble=0.00;
	    try {
	    	totScontoFattDouble=(this.getImponibileDouble() * this.getScontoFattura())/100;
	    } catch (Exception e) {
	    	e.printStackTrace();
		}
		return totScontoFattDouble;
	}
	public String getTotaleScontoStr() {
		String totaleScontoStr=null;
	    try {
			double totScontoFattDoubleAppo=this.getTotaleSconto();
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		    dfs.setGroupingSeparator('.');
		    dfs.setDecimalSeparator(',');
		    // controllo formattazione con virgola separatore dei decimali
	    	 DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
	    	 // importo
	    	String stringa  = df.format(totScontoFattDoubleAppo);
	    	NumberFormat formatIT = NumberFormat.getCurrencyInstance();
		    String strCurrency="\u20AC ";
		    strCurrency=strCurrency+stringa;
		    //Number imp=formatIT.parse(strCurrency); // va in errore se non è riconosciuto come formato euro
		    totaleScontoStr=formatIT.format(totScontoFattDoubleAppo);
		    totaleScontoStr=totaleScontoStr.substring(2); // elimina il simbolo

	    } catch (Exception e) {
	    	e.printStackTrace();
		}
		return totaleScontoStr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((IDFatt == null) ? 0 : IDFatt.hashCode());
		result = prime * result
				+ ((annoFattura == null) ? 0 : annoFattura.hashCode());
		long temp;
		temp = Double.doubleToLongBits(cambioFattura);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((codBibl == null) ? 0 : codBibl.hashCode());
		result = prime * result + ((codPolo == null) ? 0 : codPolo.hashCode());
		result = prime * result
				+ ((dataFattura == null) ? 0 : dataFattura.hashCode());
		result = prime * result
				+ ((dataRegFattura == null) ? 0 : dataRegFattura.hashCode());
		result = prime * result + (fatturaVeloce ? 1231 : 1237);
		result = prime * result + (flag_canc ? 1231 : 1237);
		result = prime
				* result
				+ ((fornitoreFattura == null) ? 0 : fornitoreFattura.hashCode());
		temp = Double.doubleToLongBits(importoFattura);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((importoFatturaStr == null) ? 0 : importoFatturaStr
						.hashCode());
		result = prime * result
				+ ((numFattura == null) ? 0 : numFattura.hashCode());
		result = prime * result
				+ ((progrFattura == null) ? 0 : progrFattura.hashCode());
		result = prime * result
				+ ((progressivo == null) ? 0 : progressivo.hashCode());
		result = prime
				* result
				+ ((righeDettaglioFattura == null) ? 0 : righeDettaglioFattura
						.hashCode());
		temp = Double.doubleToLongBits(scontoFattura);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((scontoFatturaStr == null) ? 0 : scontoFatturaStr.hashCode());
		result = prime * result
				+ ((statoFattura == null) ? 0 : statoFattura.hashCode());
		result = prime * result + ((ticket == null) ? 0 : ticket.hashCode());
		result = prime * result
				+ ((tipoFattura == null) ? 0 : tipoFattura.hashCode());
		result = prime * result
				+ ((tipoVariazione == null) ? 0 : tipoVariazione.hashCode());
		result = prime * result + ((utente == null) ? 0 : utente.hashCode());
		result = prime * result
				+ ((valutaFattura == null) ? 0 : valutaFattura.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final FatturaVO other = (FatturaVO) obj;
		if (IDFatt == null) {
			if (other.IDFatt != null)
				return false;
		} else if (!IDFatt.equals(other.IDFatt))
			return false;
		if (annoFattura == null) {
			if (other.annoFattura != null)
				return false;
		} else if (!annoFattura.equals(other.annoFattura))
			return false;
		if (Double.doubleToLongBits(cambioFattura) != Double
				.doubleToLongBits(other.cambioFattura))
			return false;
		if (codBibl == null) {
			if (other.codBibl != null)
				return false;
		} else if (!codBibl.equals(other.codBibl))
			return false;
		if (codPolo == null) {
			if (other.codPolo != null)
				return false;
		} else if (!codPolo.equals(other.codPolo))
			return false;
		if (dataFattura == null) {
			if (other.dataFattura != null)
				return false;
		} else if (!dataFattura.equals(other.dataFattura))
			return false;
		if (dataRegFattura == null) {
			if (other.dataRegFattura != null)
				return false;
		} else if (!dataRegFattura.equals(other.dataRegFattura))
			return false;
/*		if (fatturaVeloce != other.fatturaVeloce)
			return false;*/
/*		if (flag_canc != other.flag_canc)
			return false;
*/		if (fornitoreFattura == null) {
			if (other.fornitoreFattura != null)
				return false;
		} else if (!fornitoreFattura.equals(other.fornitoreFattura))
			return false;
		if (Double.doubleToLongBits(importoFattura) != Double
				.doubleToLongBits(other.importoFattura))
			return false;
		if (importoFatturaStr == null) {
			if (other.importoFatturaStr != null)
				return false;
		} else if (!importoFatturaStr.equals(other.importoFatturaStr))
			return false;
		if (numFattura == null) {
			if (other.numFattura != null)
				return false;
		} else if (!numFattura.trim().equals(other.numFattura.trim()))
			return false;
		if (progrFattura == null) {
			if (other.progrFattura != null)
				return false;
		} else if (!progrFattura.equals(other.progrFattura))
			return false;
/*		if (progressivo == null) {
			if (other.progressivo != null)
				return false;
		} else if (!progressivo.equals(other.progressivo))
			return false;
*/		if (righeDettaglioFattura == null) {
			if (other.righeDettaglioFattura != null)
				return false;
		} else if (!listEquals(righeDettaglioFattura, other.righeDettaglioFattura, StrutturaFatturaVO.class))
			return false;
		if (Double.doubleToLongBits(scontoFattura) != Double
				.doubleToLongBits(other.scontoFattura))
			return false;
		if (scontoFatturaStr == null) {
			if (other.scontoFatturaStr != null)
				return false;
		} else if (!scontoFatturaStr.equals(other.scontoFatturaStr))
			return false;
		if (statoFattura == null) {
			if (other.statoFattura != null)
				return false;
		} else if (!statoFattura.equals(other.statoFattura))
			return false;
/*		if (ticket == null) {
			if (other.ticket != null)
				return false;
		} else if (!ticket.equals(other.ticket))
			return false;*/
		if (tipoFattura == null) {
			if (other.tipoFattura != null)
				return false;
		} else if (!tipoFattura.equals(other.tipoFattura))
			return false;
/*		if (tipoVariazione == null) {
			if (other.tipoVariazione != null)
				return false;
		} else if (!tipoVariazione.equals(other.tipoVariazione))
			return false;*/
/*		if (utente == null) {
			if (other.utente != null)
				return false;
		} else if (!utente.equals(other.utente))
			return false;*/
		if (valutaFattura == null) {
			if (other.valutaFattura != null)
				return false;
		} else if (!valutaFattura.equals(other.valutaFattura))
			return false;
		return true;
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
	public String getDenoStatoFattura() {
		return denoStatoFattura;
	}
	public void setDenoStatoFattura(String denoStatoFattura) {
		this.denoStatoFattura = denoStatoFattura;
	}
	public String getDenoBibl() {
		return denoBibl;
	}
	public void setDenoBibl(String denoBibl) {
		this.denoBibl = denoBibl;
	}
	public boolean isGestBil() {
		return gestBil;
	}
	public void setGestBil(boolean gestBil) {
		this.gestBil = gestBil;
	}


}
