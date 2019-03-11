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
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class StrutturaFatturaVO extends SerializableVO {


	/**
	 *
	 */
	private static final long serialVersionUID = 6808143043756337485L;
	private String codPolo;
	private String codBibl;
	private StrutturaTerna fattura; // per note di credito (anno, prog, num. riga )
	private int progrRigaFattura; // serve solo per view della lista su jsp
	private int rigaFattura;
	private String noteRigaFattura;
	private double importoRigaFattura;
	private double sconto1RigaFattura;
	private double sconto2RigaFattura;
	private String importoRigaFatturaStr;
	private String sconto1RigaFatturaStr;
	private String sconto2RigaFatturaStr;
	private String codIvaRigaFattura="00";
	private StrutturaTerna bilancio;
	private StrutturaTerna ordine;
	private double prezzoOrdine=0.00;
	private String titOrdine;
	private String bidOrdine;
	private String codSerieRigaFatt; // NEW
	private String invRigaFatt; // NEW
	private Integer IDOrd;
	private Integer IDBil;
	private Integer IDFatt;
	private Integer IDFattNC; // per note di credito

	private boolean flag_canc=false;
	private Timestamp dataUpd;


	public StrutturaFatturaVO (){};
	public StrutturaFatturaVO (String codP, String codB, StrutturaTerna fatt, int rigaFatt , String noteRigaFatt, double importoRigaFatt, double sconto1RigaFatt, double sconto2RigaFatt,  String codIvaRigaFatt, StrutturaTerna bil, StrutturaTerna ord ) throws Exception {
		//if (importoRigaFatt == 0 ) {
		//	throw new Exception("Riga fattura non valida");
		//}
		this.codPolo = codP;
		this.codBibl = codB;
		this.fattura=fatt;
		this.ordine=ord;
		this.bilancio=bil;
		this.rigaFattura=rigaFatt;
		this.noteRigaFattura=noteRigaFatt;
		this.importoRigaFattura=importoRigaFatt;
		this.sconto1RigaFattura=sconto1RigaFatt;
		this.sconto2RigaFattura=sconto2RigaFatt;
		this.codIvaRigaFattura=codIvaRigaFatt;
	}

	public String getCodBibl() {
		return codBibl;
	}

	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}

	public String getCodIvaRigaFattura() {
		return codIvaRigaFattura;
	}

	public void setCodIvaRigaFattura(String codIvaRigaFattura) {
		this.codIvaRigaFattura = codIvaRigaFattura;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}


	public double getImportoRigaFattura() {
		return importoRigaFattura;
	}

	public void setImportoRigaFattura(double importoRigaFattura) {
		this.importoRigaFattura = importoRigaFattura;
	}

	public String getNoteRigaFattura() {
		return noteRigaFattura;
	}

	public void setNoteRigaFattura(String noteRigaFattura) {
		this.noteRigaFattura = noteRigaFattura;
	}


	public int getRigaFattura() {
		return rigaFattura;
	}

	public void setRigaFattura(int rigaFattura) {
		this.rigaFattura = rigaFattura;
	}

	public double getSconto1RigaFattura() {
		return sconto1RigaFattura;
	}

	public void setSconto1RigaFattura(double sconto1RigaFattura) {
		this.sconto1RigaFattura = sconto1RigaFattura;
	}

	public double getSconto2RigaFattura() {
		return sconto2RigaFattura;
	}

	public void setSconto2RigaFattura(double sconto2RigaFattura) {
		this.sconto2RigaFattura = sconto2RigaFattura;
	}
	public StrutturaTerna getBilancio() {
		return bilancio;
	}
	public void setBilancio(StrutturaTerna bilancio) {
		this.bilancio = bilancio;
	}
	public StrutturaTerna getOrdine() {
		return ordine;
	}
	public void setOrdine(StrutturaTerna ordine) {
		this.ordine = ordine;
	}
	public StrutturaTerna getFattura() {
		return fattura;
	}
	public void setFattura(StrutturaTerna fattura) {
		this.fattura = fattura;
	}
	public Integer getIDBil() {
		return IDBil;
	}
	public void setIDBil(Integer bil) {
		IDBil = bil;
	}
	public Integer getIDOrd() {
		return IDOrd;
	}
	public void setIDOrd(Integer ord) {
		IDOrd = ord;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}
	public Integer getIDFatt() {
		return IDFatt;
	}
	public void setIDFatt(Integer fatt) {
		IDFatt = fatt;
	}
	public String getImportoRigaFatturaStr() {
		return importoRigaFatturaStr;
	}
	public void setImportoRigaFatturaStr(String importoRigaFatturaStr) {
		this.importoRigaFatturaStr = importoRigaFatturaStr;
	}
	public String getSconto1RigaFatturaStr() {
		return sconto1RigaFatturaStr;
	}
	public void setSconto1RigaFatturaStr(String sconto1RigaFatturaStr) {
		this.sconto1RigaFatturaStr = sconto1RigaFatturaStr;
	}
	public String getSconto2RigaFatturaStr() {
		return sconto2RigaFatturaStr;
	}
	public void setSconto2RigaFatturaStr(String sconto2RigaFatturaStr) {
		this.sconto2RigaFatturaStr = sconto2RigaFatturaStr;
	}
	public String getTitOrdine() {
		return titOrdine;
	}
	public void setTitOrdine(String titOrdine) {
		this.titOrdine = titOrdine;
	}
	public String getBidOrdine() {
		return bidOrdine;
	}
	public void setBidOrdine(String bidOrdine) {
		this.bidOrdine = bidOrdine;
	}
	public double getPrezzoOrdine() {
		return prezzoOrdine;
	}

	public void setPrezzoOrdine(double prezzoOrdine) {
		this.prezzoOrdine = prezzoOrdine;
	}

	public double getCalcoloIvaRigaDouble(double scontoFattura) {
	    double ivaDouble=0.00;
	    try {

	    	double importo=0;
		    double sconto1=0;
		    double sconto2=0;
		    double sconto3=0;
		    double iva=0;

		    double importo1=0; //(importo scontato 1)
		    double importo2=0; //(importo scontato 2)
		    double importo3=0; //(importo scontato 3) sconto di fattura
		    double importo4=0; //(importo con aggiunta dell'iva)

		    importo=this.importoRigaFattura;
		    sconto1=this.importoRigaFattura* this.sconto1RigaFattura/100;
		    importo1=this.importoRigaFattura - sconto1;
		    sconto2=importo1*this.sconto2RigaFattura/100;
			importo2=importo1 - sconto2;
		    // 12.11.08
			sconto3=importo2*scontoFattura/100;
			importo3=importo2 - sconto3;
			// calcolo dell'iva che va aggiunto
			ivaDouble=importo3*Double.valueOf(this.codIvaRigaFattura.trim())/100;

	    } catch (Exception e) {
	    	e.printStackTrace();
		}
		return ivaDouble;
	}

	public String getImportoScontatoConIvaStr() {
	    String importoScontatoConIvaStr=null;
	    try {

	    	double importoScontatoConIvaDoubleAppo=this.getImportoScontatoConIvaDouble();

			// conversione in stringa
			//importoScontatoConIvaStr=Pulisci.VisualizzaImporto(importo4);
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		    dfs.setGroupingSeparator('.');
		    dfs.setDecimalSeparator(',');
		    // controllo formattazione con virgola separatore dei decimali
	    	 DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
	    	 // importo
	    	String stringa  = df.format(importoScontatoConIvaDoubleAppo);
	    	NumberFormat formatIT = NumberFormat.getCurrencyInstance();
		    String strCurrency="\u20AC ";
		    strCurrency=strCurrency+stringa;
		    //Number imp=formatIT.parse(strCurrency); // va in errore se non è riconosciuto come formato euro
		    importoScontatoConIvaStr=formatIT.format(importoScontatoConIvaDoubleAppo);
		    importoScontatoConIvaStr=importoScontatoConIvaStr.substring(2); // elimina il simbolo


	    } catch (Exception e) {
	    	e.printStackTrace();
		}
		return importoScontatoConIvaStr;
	}
	public double getImportoScontatoConIvaDouble() {
	    double importoScontatoConIvaDouble=0.00;
	    try {

	    	double importo=0;
		    double sconto1=0;
		    double sconto2=0;
		    double sconto3=0;
		    double iva=0;

		    double importo1=0; //(importo scontato 1)
		    double importo2=0; //(importo scontato 2)
		    double importo3=0; //(importo scontato 3) sconto di fattura
		    double importo4=0; //(importo con aggiunta dell'iva)

		    importo=this.importoRigaFattura;
		    sconto1=this.importoRigaFattura* this.sconto1RigaFattura/100;
		    importo1=this.importoRigaFattura - sconto1;
		    sconto2=importo1*this.sconto2RigaFattura/100;
			importo2=importo1 - sconto2;
		    //sconto3=importo2*scontoFattura/100;
			//importo3=importo2 - sconto3;
			// calcolo dell'iva che va aggiunto
			iva=importo2*Double.valueOf(this.codIvaRigaFattura.trim())/100;
			importoScontatoConIvaDouble=importo2 + iva;

	    } catch (Exception e) {
	    	e.printStackTrace();
		}
		return importoScontatoConIvaDouble;
	}

	public String getImportoScontatoNOIvaStr() {
	    String importoScontatoNOIvaStr=null;
	    try {
		    double importoScontatoNOIvaDoubleAppo=this.getImportoScontatoNOIvaDouble();


			// conversione in stringa
			//importoScontatoConIvaStr=Pulisci.VisualizzaImporto(importo4);
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		    dfs.setGroupingSeparator('.');
		    dfs.setDecimalSeparator(',');
		    // controllo formattazione con virgola separatore dei decimali
	    	 DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
	    	 // importo
	    	String stringa  = df.format(importoScontatoNOIvaDoubleAppo);
	    	NumberFormat formatIT = NumberFormat.getCurrencyInstance();
		    String strCurrency="\u20AC ";
		    strCurrency=strCurrency+stringa;
		    //Number imp=formatIT.parse(strCurrency); // va in errore se non è riconosciuto come formato euro
		    importoScontatoNOIvaStr=formatIT.format(importoScontatoNOIvaDoubleAppo);
		    importoScontatoNOIvaStr=importoScontatoNOIvaStr.substring(2); // elimina il simbolo


	    } catch (Exception e) {
	    	e.printStackTrace();
		}
		return importoScontatoNOIvaStr;
	}
	public double getImportoScontatoNOIvaDouble() {
	    double importoScontatoNOIvaDouble=0.00;
	    try {

	    	double importo=0;
		    double sconto1=0;
		    double sconto2=0;
		    double sconto3=0;
		    double iva=0;

		    double importo1=0; //(importo scontato 1)
		    double importo2=0; //(importo scontato 2)
		    double importo3=0; //(importo scontato 3) sconto di fattura
		    double importo4=0; //(importo con aggiunta dell'iva)

		    importo=this.importoRigaFattura;
		    sconto1=this.importoRigaFattura* this.sconto1RigaFattura/100;
		    importo1=this.importoRigaFattura - sconto1;
		    sconto2=importo1*this.sconto2RigaFattura/100;
		    importoScontatoNOIvaDouble=importo1 - sconto2;
		    //sconto3=importo2*scontoFattura/100;
			//importo3=importo2 - sconto3;
			// calcolo dell'iva che va aggiunto
			//iva=importo2*Double.valueOf(this.codIvaRigaFattura.trim())/100;
			//importo4=importo2 + iva;

			// conversione in stringa
			//importoScontatoConIvaStr=Pulisci.VisualizzaImporto(importo4);


	    } catch (Exception e) {
	    	e.printStackTrace();
		}
		return importoScontatoNOIvaDouble;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((IDBil == null) ? 0 : IDBil.hashCode());
		result = prime * result + ((IDFatt == null) ? 0 : IDFatt.hashCode());
		result = prime * result + ((IDOrd == null) ? 0 : IDOrd.hashCode());
		result = prime * result
				+ ((bidOrdine == null) ? 0 : bidOrdine.hashCode());
		result = prime * result
				+ ((bilancio == null) ? 0 : bilancio.hashCode());
		result = prime * result + ((codBibl == null) ? 0 : codBibl.hashCode());
		result = prime
				* result
				+ ((codIvaRigaFattura == null) ? 0 : codIvaRigaFattura
						.hashCode());
		result = prime * result + ((codPolo == null) ? 0 : codPolo.hashCode());
		result = prime * result + ((fattura == null) ? 0 : fattura.hashCode());
		result = prime * result + (flag_canc ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(importoRigaFattura);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((importoRigaFatturaStr == null) ? 0 : importoRigaFatturaStr
						.hashCode());
		result = prime * result
				+ ((noteRigaFattura == null) ? 0 : noteRigaFattura.hashCode());
		result = prime * result + ((ordine == null) ? 0 : ordine.hashCode());
		temp = Double.doubleToLongBits(prezzoOrdine);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + rigaFattura;
		temp = Double.doubleToLongBits(sconto1RigaFattura);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((sconto1RigaFatturaStr == null) ? 0 : sconto1RigaFatturaStr
						.hashCode());
		temp = Double.doubleToLongBits(sconto2RigaFattura);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((sconto2RigaFatturaStr == null) ? 0 : sconto2RigaFatturaStr
						.hashCode());
		result = prime * result
				+ ((titOrdine == null) ? 0 : titOrdine.hashCode());
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
		final StrutturaFatturaVO other = (StrutturaFatturaVO) obj;
		if (IDBil == null) {
			if (other.IDBil != null)
				return false;
		} else if (!IDBil.equals(other.IDBil))
			return false;
/*		if (IDFatt == null) {
			if (other.IDFatt != null)
				return false;
		} else if (!IDFatt.equals(other.IDFatt))
			return false;
*/		if (IDOrd == null) {
			if (other.IDOrd != null)
				return false;
		} else if (!IDOrd.equals(other.IDOrd))
			return false;
/*		if (bidOrdine == null) {
			if (other.bidOrdine != null)
				return false;
		} else if (!bidOrdine.equals(other.bidOrdine))
			return false;
*/		if (bilancio == null) {
			if (other.bilancio != null)
				return false;
		} else if (!bilancio.equals(other.bilancio))
			return false;
/*		if (codBibl == null) {
			if (other.codBibl != null)
				return false;
		} else if (!codBibl.equals(other.codBibl))
			return false;*/
if (codIvaRigaFattura == null  ) {
	if (other.codIvaRigaFattura != null)
		return false;
} else if (!codIvaRigaFattura.equals(other.codIvaRigaFattura))
	return false;

/*		if (codIvaRigaFattura == null || !codIvaRigaFattura.trim().equals("0") || !codIvaRigaFattura.trim().equals("") ) {
			if (other.codIvaRigaFattura != null && !other.codIvaRigaFattura.trim().equals("0") && !other.codIvaRigaFattura.trim().equals(""))
				return false;
		} else if (!codIvaRigaFattura.trim().equals(other.codIvaRigaFattura.trim()))
			return false;
*/
/*		if (codPolo == null) {
			if (other.codPolo != null)
				return false;
		} else if (!codPolo.equals(other.codPolo))
			return false;
*/
		if (fattura == null) {
			if (other.fattura != null)
				return false;
		} else if (!fattura.equals(other.fattura))
			return false;
		if (flag_canc != other.flag_canc)
			return false;
		if (Double.doubleToLongBits(importoRigaFattura) != Double
				.doubleToLongBits(other.importoRigaFattura))
			return false;
		if (importoRigaFatturaStr == null) {
			if (other.importoRigaFatturaStr != null)
				return false;
		} else if (!importoRigaFatturaStr.equals(other.importoRigaFatturaStr))
			return false;
		if (noteRigaFattura == null) {
			if (other.noteRigaFattura != null)
				return false;
		} else if (!noteRigaFattura.equals(other.noteRigaFattura))
			return false;
		if (ordine == null) {
			if (other.ordine != null)
				return false;
		} else if (!ordine.equals(other.ordine))
			return false;
/*		if (Double.doubleToLongBits(prezzoOrdine) != Double
				.doubleToLongBits(other.prezzoOrdine))
			return false;*/
		if (rigaFattura != other.rigaFattura)
			return false;
		if (Double.doubleToLongBits(sconto1RigaFattura) != Double
				.doubleToLongBits(other.sconto1RigaFattura))
			return false;
		if (sconto1RigaFatturaStr == null) {
			if (other.sconto1RigaFatturaStr != null)
				return false;
		} else if (!sconto1RigaFatturaStr.equals(other.sconto1RigaFatturaStr))
			return false;
		if (Double.doubleToLongBits(sconto2RigaFattura) != Double
				.doubleToLongBits(other.sconto2RigaFattura))
			return false;
		if (sconto2RigaFatturaStr == null) {
			if (other.sconto2RigaFatturaStr != null)
				return false;
		} else if (!sconto2RigaFatturaStr.equals(other.sconto2RigaFatturaStr))
			return false;
/*		if (titOrdine == null) {
			if (other.titOrdine != null)
				return false;
		} else if (!titOrdine.equals(other.titOrdine))
			return false;*/
		return true;
	}
	public Integer getIDFattNC() {
		return IDFattNC;
	}
	public void setIDFattNC(Integer fattNC) {
		IDFattNC = fattNC;
	}
	public Timestamp getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Timestamp dataUpd) {
		this.dataUpd = dataUpd;
	}
	public String getInvRigaFatt() {
		return invRigaFatt;
	}
	public void setInvRigaFatt(String invRigaFatt) {
		this.invRigaFatt = invRigaFatt;
	}
	public int getProgrRigaFattura() {
		return progrRigaFattura;
	}
	public void setProgrRigaFattura(int progrRigaFattura) {
		this.progrRigaFattura = progrRigaFattura;
	}
	public String getCodSerieRigaFatt() {
		return codSerieRigaFatt;
	}
	public void setCodSerieRigaFatt(String codSerieRigaFatt) {
		this.codSerieRigaFatt = codSerieRigaFatt;
	}



}
