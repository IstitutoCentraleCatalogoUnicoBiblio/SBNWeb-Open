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
//	SBNWeb - Rifacimento ClientServer
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006

package it.iccu.sbn.ejb.vo.gestionebibliografica.autore;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.exception.ValidationExceptionCodici;
import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.List;
import java.util.StringTokenizer;

public class InterrogazioneAutoreGeneraleVO extends SerializableVO {

	// = InterrogazioneAutoreGeneraleVO.class.hashCode();


	/**
	 *
	 */
	private static final long serialVersionUID = 8300391627606025741L;

	private String nome;

	private String tipoRicerca;

	private String vid;
	private String isadn;

	private boolean chkTipoNomeA;
	private boolean chkTipoNomeB;
	private boolean chkTipoNomeC;
	private boolean chkTipoNomeD;
	private boolean chkTipoNomeE;
	private boolean chkTipoNomeR;
	private boolean chkTipoNomeG;

	private List listaPaese;
	private String paeseSelez;

	private String formaAutore;

	private String dataAnnoNascitaDa;
	private String dataAnnoNascitaA;
	private String dataAnnoMorteDa;
	private String dataAnnoMorteA;
	private String dataInserimentoDa;
	private String dataInserimentoA;
	private String dataAggDa;
	private String dataAggA;

	private List listaLingue;
	private String linguaSelez;

	private String datazioni;

	private int elemXBlocchi;
	private int numPrimo;
	private int maxRighe;
	private List listaTipiOrdinam;
	private String tipoOrdinamSelez;
	private List listaFormatoLista;
	private String formatoListaSelez;


	private boolean ricLocale;
	private boolean ricLocale_old;
	private boolean ricIndice;
	private boolean ricIndice_old;

	String[] parole;
//	String[] tipoNome;
	boolean[] tipoNomeBoolean;

	public String[] getParole() {
		return parole;
	}

	public void setParole(String[] parole) {
		this.parole = parole;
	}

	public String getDataAggA() {
		return dataAggA;
	}

	public void setDataAggA(String dataAggA) {
		this.dataAggA = dataAggA;
	}

	public String getDataAggDa() {
		return dataAggDa;
	}

	public void setDataAggDa(String dataAggDa) {
		this.dataAggDa = dataAggDa;
	}

	public String getDataAnnoMorteA() {
		return dataAnnoMorteA;
	}

	public void setDataAnnoMorteA(String dataAnnoMorteA) {
		this.dataAnnoMorteA = dataAnnoMorteA;
	}

	public String getDataAnnoMorteDa() {
		return dataAnnoMorteDa;
	}

	public void setDataAnnoMorteDa(String dataAnnoMorteDa) {
		this.dataAnnoMorteDa = dataAnnoMorteDa;
	}

	public String getDataAnnoNascitaA() {
		return dataAnnoNascitaA;
	}

	public void setDataAnnoNascitaA(String dataAnnoNascitaA) {
		this.dataAnnoNascitaA = dataAnnoNascitaA;
	}

	public String getDataAnnoNascitaDa() {
		return dataAnnoNascitaDa;
	}

	public void setDataAnnoNascitaDa(String dataAnnoNascitaDa) {
		this.dataAnnoNascitaDa = dataAnnoNascitaDa;
	}

	public String getDataInserimentoA() {
		return dataInserimentoA;
	}

	public void setDataInserimentoA(String dataInserimentoA) {
		this.dataInserimentoA = dataInserimentoA;
	}

	public String getDataInserimentoDa() {
		return dataInserimentoDa;
	}

	public void setDataInserimentoDa(String dataInserimentoDa) {
		this.dataInserimentoDa = dataInserimentoDa;
	}

	public String getDatazioni() {
		return datazioni;
	}

	public void setDatazioni(String datazioni) {
		this.datazioni = datazioni;
	}

	public int getElemXBlocchi() {
		return elemXBlocchi;
	}

	public void setElemXBlocchi(int elemXBlocchi) {
		this.elemXBlocchi = elemXBlocchi;
	}

	public String getFormaAutore() {
		return formaAutore;
	}

	public void setFormaAutore(String formaAutore) {
		this.formaAutore = formaAutore;
	}

	public String getIsadn() {
		return isadn;
	}

	public void setIsadn(String isadn) {
		this.isadn = isadn;
	}

	public String getLinguaSelez() {
		return linguaSelez;
	}

	public void setLinguaSelez(String linguaSelez) {
		this.linguaSelez = linguaSelez;
	}

	public List getListaLingue() {
		return listaLingue;
	}

	public void setListaLingue(List listaLingue) {
		this.listaLingue = listaLingue;
	}

	public List getListaPaese() {
		return listaPaese;
	}

	public void setListaPaese(List listaPaese) {
		this.listaPaese = listaPaese;
	}

	public List getListaTipiOrdinam() {
		return listaTipiOrdinam;
	}

	public void setListaTipiOrdinam(List listaTipiOrdinam) {
		this.listaTipiOrdinam = listaTipiOrdinam;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPaeseSelez() {
		return paeseSelez;
	}

	public void setPaeseSelez(String paeseSelez) {
		this.paeseSelez = paeseSelez;
	}

	public boolean isRicIndice() {
		return ricIndice;
	}

	public void setRicIndice(boolean ricIndice) {
		this.ricIndice = ricIndice;
	}

	public boolean isRicLocale() {
		return ricLocale;
	}

	public void setRicLocale(boolean ricLocale) {
		this.ricLocale = ricLocale;
	}

	public String getTipoOrdinamSelez() {
		return tipoOrdinamSelez;
	}

	public void setTipoOrdinamSelez(String tipoOrdinamSelez) {
		this.tipoOrdinamSelez = tipoOrdinamSelez;
	}

	public String getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public boolean isChkTipoNomeA() {
		return chkTipoNomeA;
	}

	public void setChkTipoNomeA(boolean chkTipoNomeA) {
		this.chkTipoNomeA = chkTipoNomeA;
	}

	public boolean isChkTipoNomeB() {
		return chkTipoNomeB;
	}

	public void setChkTipoNomeB(boolean chkTipoNomeB) {
		this.chkTipoNomeB = chkTipoNomeB;
	}

	public boolean isChkTipoNomeC() {
		return chkTipoNomeC;
	}

	public void setChkTipoNomeC(boolean chkTipoNomeC) {
		this.chkTipoNomeC = chkTipoNomeC;
	}

	public boolean isChkTipoNomeD() {
		return chkTipoNomeD;
	}

	public void setChkTipoNomeD(boolean chkTipoNomeD) {
		this.chkTipoNomeD = chkTipoNomeD;
	}

	public boolean isChkTipoNomeE() {
		return chkTipoNomeE;
	}

	public void setChkTipoNomeE(boolean chkTipoNomeE) {
		this.chkTipoNomeE = chkTipoNomeE;
	}

	public boolean isChkTipoNomeG() {
		return chkTipoNomeG;
	}

	public void setChkTipoNomeG(boolean chkTipoNomeG) {
		this.chkTipoNomeG = chkTipoNomeG;
	}

	public boolean isChkTipoNomeR() {
		return chkTipoNomeR;
	}

	public void setChkTipoNomeR(boolean chkTipoNomeR) {
		this.chkTipoNomeR = chkTipoNomeR;
	}

	public void validaCanaliPrim() throws ValidationException {
		int combinazioni = 0;

		if (this.nome.length() > 0 ) combinazioni = combinazioni +1;
		if (this.vid.length()  > 0 ) combinazioni = combinazioni +1;
		if (this.isadn.length() > 0 ) combinazioni = combinazioni +1;

		if (combinazioni == 0)
			throw new ValidationException("noCanPrim", ValidationExceptionCodici.noCanPrim);
		if (combinazioni > 1)
			throw new ValidationException("soloUnCanPrim", ValidationExceptionCodici.soloUnCanPrim);

		if ((this.vid.length() > 0 && this.vid.length() < 10) || this.vid.length() > 10 )
			throw new ValidationException("vidErrato", ValidationExceptionCodici.vidErrato);

//		if (this.isadn.length() > 10 )
//			throw new ValidationException("isadnErrato", ValidationExceptionCodici.isadnErrato);

	}

	public void validaParametriGener() throws ValidationException {
		if (this.ricLocale == false && this.ricIndice == false)
			throw new ValidationException("livRicObblig", ValidationExceptionCodici.livRicObblig);

		if (this.getTipoRicerca() == null) {
			this.setTipoRicerca("inizio");
		}
		if (this.getFormaAutore() == null) {
			this.setFormaAutore("tutti");
		}

		if (!this.getNome().equals("") && this.getTipoRicerca().equals("parole")) {
			StringTokenizer st = new StringTokenizer(this.getNome());

			if (st.countTokens() > 4) {
				// inserire diagnostico di troppe parole (al massimo 4)
				throw new ValidationException("ric021", ValidationExceptionCodici.livRicObblig);
			} else {
				// devono essere massimo 4 parole
				parole = new String[4];
				int      i = 0;
				while (st.hasMoreTokens()) {
					parole[i] = st.nextToken();
					i++;
				}
			}
		}
		// TIPO NOME

		int i = -1;
		int countTipoNome = 0;
		tipoNomeBoolean = new boolean[7];

		if (this.chkTipoNomeA){
			this.tipoNomeBoolean[++i] = true;
			countTipoNome++;
		} else {
			this.tipoNomeBoolean[++i] = false;
		}
		if (this.chkTipoNomeB){
			this.tipoNomeBoolean[++i] = true;
		countTipoNome++;
		} else {
			this.tipoNomeBoolean[++i] = false;
		}
		if (this.chkTipoNomeC){
			this.tipoNomeBoolean[++i] = true;
		countTipoNome++;
		} else {
			this.tipoNomeBoolean[++i] = false;
		}
		if (this.chkTipoNomeD){
			this.tipoNomeBoolean[++i] = true;
		countTipoNome++;
		} else {
			this.tipoNomeBoolean[++i] = false;
		}
		if (this.chkTipoNomeE){
			this.tipoNomeBoolean[++i] = true;
		countTipoNome++;
		} else {
			this.tipoNomeBoolean[++i] = false;
		}
		if (this.chkTipoNomeR) {
			this.tipoNomeBoolean[++i] = true;
		countTipoNome++;
		} else {
			this.tipoNomeBoolean[++i] = false;
		}
		if (this.chkTipoNomeG){
			this.tipoNomeBoolean[++i] = true;
		countTipoNome++;
		} else {
			this.tipoNomeBoolean[++i] = false;
		}

		if (countTipoNome > 4 ) {
			// inserire diagnostico di troppi tipi nome (al massimo 4)
			throw new ValidationException("ric022", ValidationExceptionCodici.livRicObblig);
		}
	}

	public int getNumPrimo() {
		return numPrimo;
	}

	public void setNumPrimo(int numPrimo) {
		this.numPrimo = numPrimo;
	}

	public int getMaxRighe() {
		return maxRighe;
	}

	public void setMaxRighe(int maxRighe) {
		this.maxRighe = maxRighe;
	}

	public boolean[] getTipoNomeBoolean() {
		return tipoNomeBoolean;
	}

	public void setTipoNomeBoolean(boolean[] tipoNomeBoolean) {
		this.tipoNomeBoolean = tipoNomeBoolean;
	}

	public String getFormatoListaSelez() {
		return formatoListaSelez;
	}

	public void setFormatoListaSelez(String formatoListaSelez) {
		this.formatoListaSelez = formatoListaSelez;
	}

	public List getListaFormatoLista() {
		return listaFormatoLista;
	}

	public void setListaFormatoLista(List listaFormatoLista) {
		this.listaFormatoLista = listaFormatoLista;
	}

	public boolean isRicIndice_old() {
		return ricIndice_old;
	}

	public void setRicIndice_old(boolean ricIndice_old) {
		this.ricIndice_old = ricIndice_old;
	}

	public boolean isRicLocale_old() {
		return ricLocale_old;
	}

	public void setRicLocale_old(boolean ricLocale_old) {
		this.ricLocale_old = ricLocale_old;
	}


	public void save() {
		this.ricLocale_old = this.ricLocale;
		this.ricIndice_old = this.ricIndice;
	}

	public void restore()
	{
		this.ricLocale = this.ricLocale_old;
		this.ricIndice = this.ricIndice_old;
	}

}
