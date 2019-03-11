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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class StatisticheVO extends SerializableVO {

	/**
	 *
	 */
	private static final long serialVersionUID = -4785033706661889184L;
	private String tipoOrdine;
	private int totaleOrdXTipo;
	private String condizione1;
	private int totCondizione1;
	private double percTotCondizione1;
	private String percTotCondizione1Str;
	private String condizione2;
	private int totCondizione2;
	private double percTotCondizione2;
	private String percTotCondizione2Str;
	private String condizione3;
	private int totCondizione3;
	private double percTotCondizione3;
	private String percTotCondizione3Str;
	private String condizione4;
	private int totCondizione4;
	private double percTotCondizione4;
	private String percTotCondizione4Str;
	private String condizione5;
	private int totCondizione5;
	private double percTotCondizione5;
	private String percTotCondizione5Str;

	private Integer annoOrdine;
	private String annoOrdineStr;
	private String codPolo;
	private String codBibl;
	private String denoBibl;
	private String dataOrdineDa;
	private String dataOrdineA;
	private String tipoOrdineCercato;
	private double media;
	private String mediaStr;
	private String intestazione;


	public StatisticheVO (){
		this.annoOrdineStr="";
		this.codPolo="";
		this.codBibl="";
		this.denoBibl="";
		this.dataOrdineDa="";
		this.dataOrdineA="";
		this.tipoOrdineCercato="";
		this.tipoOrdine="";
		this.totaleOrdXTipo=0;
		this.condizione1="";
		this.totCondizione1=0;
		this.percTotCondizione1=0;
		this.condizione2="";
		this.totCondizione2=0;
		this.percTotCondizione2=0;
		this.condizione3="";
		this.totCondizione3=0;
		this.percTotCondizione3=0;
		this.condizione4="";
		this.totCondizione4=0;
		this.percTotCondizione4=0;
		this.condizione5="";
		this.totCondizione5=0;
		this.percTotCondizione5=0;
		this.percTotCondizione1Str="";
		this.percTotCondizione2Str="";
		this.percTotCondizione3Str="";
		this.percTotCondizione4Str="";
		this.percTotCondizione5Str="";

	}


	public double getPercTotCondizione1() {
		return percTotCondizione1;
	}


	public void setPercTotCondizione1(double percTotCondizione1) {
		this.percTotCondizione1 = percTotCondizione1;
	}


	public double getPercTotCondizione2() {
		return percTotCondizione2;
	}


	public void setPercTotCondizione2(double percTotCondizione2) {
		this.percTotCondizione2 = percTotCondizione2;
	}


	public double getPercTotCondizione3() {
		return percTotCondizione3;
	}


	public void setPercTotCondizione3(double percTotCondizione3) {
		this.percTotCondizione3 = percTotCondizione3;
	}


	public double getPercTotCondizione4() {
		return percTotCondizione4;
	}


	public void setPercTotCondizione4(double percTotCondizione4) {
		this.percTotCondizione4 = percTotCondizione4;
	}


	public String getTipoOrdine() {
		return tipoOrdine;
	}


	public void setTipoOrdine(String tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}


	public int getTotaleOrdXTipo() {
		return totaleOrdXTipo;
	}


	public void setTotaleOrdXTipo(int totaleOrdXTipo) {
		this.totaleOrdXTipo = totaleOrdXTipo;
	}


	public int getTotCondizione1() {
		return totCondizione1;
	}


	public void setTotCondizione1(int totCondizione1) {
		this.totCondizione1 = totCondizione1;
		this.percTotCondizione1 = Double.valueOf(totCondizione1*100)/this.totaleOrdXTipo;
		this.percTotCondizione1Str = getStr(this.percTotCondizione1) + " %";
	}

	public String getStr(double cifra) {
		String totaleCifraStr=null;
	    try {
			double totCifraDoubleAppo=cifra;
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		    dfs.setGroupingSeparator('.');
		    dfs.setDecimalSeparator(',');
		    // controllo formattazione con virgola separatore dei decimali
	    	 DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
	    	 // importo
	    	String stringa  = df.format(totCifraDoubleAppo);
	    	NumberFormat formatIT = NumberFormat.getCurrencyInstance();
		    String strCurrency="\u20AC ";
		    strCurrency=strCurrency+stringa;
		    //Number imp=formatIT.parse(strCurrency); // va in errore se non è riconosciuto come formato euro
		    totaleCifraStr=formatIT.format(totCifraDoubleAppo);
		    totaleCifraStr=totaleCifraStr.substring(2); // elimina il simbolo

	    } catch (Exception e) {
	    	e.printStackTrace();
		}
		return totaleCifraStr;
	}



	public int getTotCondizione2() {
		return totCondizione2;
	}


	public void setTotCondizione2(int totCondizione2) {
		this.totCondizione2 = totCondizione2;
		this.percTotCondizione2 = Double.valueOf(totCondizione2*100)/this.totaleOrdXTipo;
		this.percTotCondizione2Str = getStr(this.percTotCondizione2) + " %";;

	}


	public int getTotCondizione3() {
		return totCondizione3;
	}


	public void setTotCondizione3(int totCondizione3) {
		this.totCondizione3 = totCondizione3;
		this.percTotCondizione3 = Double.valueOf(totCondizione3*100)/this.totaleOrdXTipo;
		this.percTotCondizione3Str = getStr(this.percTotCondizione3) + " %";;

	}


	public int getTotCondizione4() {
		return totCondizione4;
	}


	public void setTotCondizione4(int totCondizione4) {
		this.totCondizione4 = totCondizione4;
		this.percTotCondizione4 = Double.valueOf(totCondizione4*100)/this.totaleOrdXTipo;
		this.percTotCondizione4Str = getStr(this.percTotCondizione4) + " %";;

	}


	public String getCondizione1() {
		return condizione1;
	}


	public void setCondizione1(String condizione1) {
		this.condizione1 = condizione1;
	}


	public String getCondizione2() {
		return condizione2;
	}


	public void setCondizione2(String condizione2) {
		this.condizione2 = condizione2;
	}


	public String getCondizione3() {
		return condizione3;
	}


	public void setCondizione3(String condizione3) {
		this.condizione3 = condizione3;
	}


	public String getCondizione4() {
		return condizione4;
	}


	public void setCondizione4(String condizione4) {
		this.condizione4 = condizione4;
	}


	public Integer getAnnoOrdine() {
		return annoOrdine;
	}


	public void setAnnoOrdine(Integer annoOrdine) {
		this.annoOrdine = annoOrdine;
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


	public String getDataOrdineA() {
		return dataOrdineA;
	}


	public void setDataOrdineA(String dataOrdineA) {
		this.dataOrdineA = dataOrdineA;
	}


	public String getDataOrdineDa() {
		return dataOrdineDa;
	}


	public void setDataOrdineDa(String dataOrdineDa) {
		this.dataOrdineDa = dataOrdineDa;
	}


	public String getDenoBibl() {
		return denoBibl;
	}


	public void setDenoBibl(String denoBibl) {
		this.denoBibl = denoBibl;
	}


	public String getTipoOrdineCercato() {
		return tipoOrdineCercato;
	}


	public void setTipoOrdineCercato(String tipoOrdineCercato) {
		this.tipoOrdineCercato = tipoOrdineCercato;
	}


	public String getAnnoOrdineStr() {
		return annoOrdineStr;
	}


	public void setAnnoOrdineStr(String annoOrdineStr) {
		this.annoOrdineStr = annoOrdineStr;
	}


	public String getPercTotCondizione1Str() {
		return percTotCondizione1Str;
	}


	public void setPercTotCondizione1Str(String percTotCondizione1Str) {
		this.percTotCondizione1Str = percTotCondizione1Str;
	}


	public String getPercTotCondizione2Str() {
		return percTotCondizione2Str;
	}


	public void setPercTotCondizione2Str(String percTotCondizione2Str) {
		this.percTotCondizione2Str = percTotCondizione2Str;
	}


	public String getPercTotCondizione3Str() {
		return percTotCondizione3Str;
	}


	public void setPercTotCondizione3Str(String percTotCondizione3Str) {
		this.percTotCondizione3Str = percTotCondizione3Str;
	}


	public String getPercTotCondizione4Str() {
		return percTotCondizione4Str;
	}


	public void setPercTotCondizione4Str(String percTotCondizione4Str) {
		this.percTotCondizione4Str = percTotCondizione4Str;
	}


	public double getMedia() {
		return media;
	}


	public void setMedia(double media) {
		this.media = media;
		this.mediaStr = getStr(this.media) + " gg";

	}


	public String getMediaStr() {
		return mediaStr;
	}


	public void setMediaStr(String mediaStr) {
		this.mediaStr = mediaStr;
	}


	public String getCondizione5() {
		return condizione5;
	}


	public void setCondizione5(String condizione5) {
		this.condizione5 = condizione5;
	}


	public double getPercTotCondizione5() {
		return percTotCondizione5;
	}


	public void setPercTotCondizione5(double percTotCondizione5) {
		this.percTotCondizione5 = percTotCondizione5;
	}


	public String getPercTotCondizione5Str() {
		return percTotCondizione5Str;
	}


	public void setPercTotCondizione5Str(String percTotCondizione5Str) {
		this.percTotCondizione5Str = percTotCondizione5Str;
	}


	public int getTotCondizione5() {
		return totCondizione5;
	}


	public void setTotCondizione5(int totCondizione5) {
		this.totCondizione5 = totCondizione5;
		this.percTotCondizione5 = Double.valueOf(totCondizione5*100)/this.totaleOrdXTipo;
		this.percTotCondizione5Str = getStr(this.percTotCondizione5) + " %";;

	}


	public String getIntestazione() {
		return intestazione;
	}


	public void setIntestazione(String intestazione) {
		this.intestazione = intestazione;
	}
}
