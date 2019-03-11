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
package it.iccu.sbn.web.actionforms.documentofisico.esameCollocazioni;

import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreePassaggioSifVO;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.util.List;

import org.apache.struts.action.ActionForm;

public class EsameCollocEsaminaPossedutoForm extends ActionForm {


	private static final long serialVersionUID = 7183792181614415664L;
	InventarioTitoloVO recInvTit = new InventarioTitoloVO();
	EsameCollocRicercaVO paramRicerca = new EsameCollocRicercaVO();
	private boolean posseduto[] = {false, false, false, false};
	AreePassaggioSifVO DatiGestioneBibliografica = null;

	private String ticket;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private String bid;
	private String titolo;
	private boolean sessione;

	private String folder;
	private boolean tab1 = false;
	private boolean tab2 = false;
	private boolean tab3 = false;
	private boolean tastoInv = false;
	private boolean tastoColl = false;
	private boolean tastoEsempl = false;
	private boolean tastoAltreBib = false;
	private boolean noPosseduto = false;

	private List listaInventariDelTitolo;
	private String selectedInv;
	private String selectedPrg;
	private List listaCollocazioniDelTitolo;
	private String selectedColl;
	private List listaEsemplariDelTitolo;
	private String selectedEsem;
//	private List listaInventariDiColloc;
//	private List listaCollocazioniDiEsempl;
//	private List listaEsemplariDiColloc;

	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault() );
	private String idLista1;
	private int totBlocchi1;
	private int totRighe1;
	private int bloccoSelezionato1;
	private boolean abilitaBottoneCarBlocchi1 = false;

	private String idLista2;
	private int totBlocchi2;
	private int totRighe2;
	private int bloccoSelezionato2;
	private boolean abilitaBottoneCarBlocchi2 = false;

	private String idLista3;
	private int totBlocchi3;
	private int totRighe3;
	private int bloccoSelezionato3;
	private boolean abilitaBottoneCarBlocchi3 = false;

	private boolean noInv = false;
	private boolean noColl = false;
	private boolean noEsem = false;

	public boolean isNoInv() {
		return noInv;
	}

	public void setNoInv(boolean noInv) {
		this.noInv = noInv;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
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

	public int getElemPerBlocchi() {
		return nRec;
	}
	public void setElemPerBlocchi(int elemPerBlocchi) {
		this.nRec = elemPerBlocchi;
	}

	public String getIdLista1() {
		return idLista1;
	}

	public void setIdLista1(String idLista1) {
		this.idLista1 = idLista1;
	}

	public int getTotBlocchi1() {
		return totBlocchi1;
	}

	public void setTotBlocchi1(int totBlocchi1) {
		this.totBlocchi1 = totBlocchi1;
	}

	public int getTotRighe1() {
		return totRighe1;
	}

	public void setTotRighe1(int totRighe1) {
		this.totRighe1 = totRighe1;
	}

	public int getBloccoSelezionato1() {
		return bloccoSelezionato1;
	}

	public void setBloccoSelezionato1(int bloccoSelezionato1) {
		this.bloccoSelezionato1 = bloccoSelezionato1;
	}

	public boolean isAbilitaBottoneCarBlocchi1() {
		return abilitaBottoneCarBlocchi1;
	}

	public void setAbilitaBottoneCarBlocchi1(boolean abilitaBottoneCarBlocchi1) {
		this.abilitaBottoneCarBlocchi1 = abilitaBottoneCarBlocchi1;
	}

	public String getIdLista2() {
		return idLista2;
	}

	public void setIdLista2(String idLista2) {
		this.idLista2 = idLista2;
	}

	public int getTotBlocchi2() {
		return totBlocchi2;
	}

	public void setTotBlocchi2(int totBlocchi2) {
		this.totBlocchi2 = totBlocchi2;
	}

	public int getTotRighe2() {
		return totRighe2;
	}

	public void setTotRighe2(int totRighe2) {
		this.totRighe2 = totRighe2;
	}

	public int getBloccoSelezionato2() {
		return bloccoSelezionato2;
	}

	public void setBloccoSelezionato2(int bloccoSelezionato2) {
		this.bloccoSelezionato2 = bloccoSelezionato2;
	}

	public boolean isAbilitaBottoneCarBlocchi2() {
		return abilitaBottoneCarBlocchi2;
	}

	public void setAbilitaBottoneCarBlocchi2(boolean abilitaBottoneCarBlocchi2) {
		this.abilitaBottoneCarBlocchi2 = abilitaBottoneCarBlocchi2;
	}

	public String getIdLista3() {
		return idLista3;
	}

	public void setIdLista3(String idLista3) {
		this.idLista3 = idLista3;
	}

	public int getTotBlocchi3() {
		return totBlocchi3;
	}

	public void setTotBlocchi3(int totBlocchi3) {
		this.totBlocchi3 = totBlocchi3;
	}

	public int getTotRighe3() {
		return totRighe3;
	}

	public void setTotRighe3(int totRighe3) {
		this.totRighe3 = totRighe3;
	}

	public int getBloccoSelezionato3() {
		return bloccoSelezionato3;
	}

	public void setBloccoSelezionato3(int bloccoSelezionato3) {
		this.bloccoSelezionato3 = bloccoSelezionato3;
	}

	public boolean isAbilitaBottoneCarBlocchi3() {
		return abilitaBottoneCarBlocchi3;
	}

	public void setAbilitaBottoneCarBlocchi3(boolean abilitaBottoneCarBlocchi3) {
		this.abilitaBottoneCarBlocchi3 = abilitaBottoneCarBlocchi3;
	}

	public boolean isTab1() {
		return tab1;
	}

	public void setTab1(boolean tab1) {
		this.tab1 = tab1;
	}

	public boolean isTab2() {
		return tab2;
	}

	public void setTab2(boolean tab2) {
		this.tab2 = tab2;
	}

	public boolean isTab3() {
		return tab3;
	}

	public void setTab3(boolean tab3) {
		this.tab3 = tab3;
	}

	public boolean[] getPosseduto() {
		return posseduto;
	}

	public void setPosseduto(boolean[] posseduto) {
		this.posseduto = posseduto;
	}

	public boolean isNoPosseduto() {
		return noPosseduto;
	}

	public void setNoPosseduto(boolean noPosseduto) {
		this.noPosseduto = noPosseduto;
	}

	public boolean isTastoColl() {
		return tastoColl;
	}

	public void setTastoColl(boolean tastoColl) {
		this.tastoColl = tastoColl;
	}

	public boolean isTastoEsempl() {
		return tastoEsempl;
	}

	public void setTastoEsempl(boolean tastoEsempl) {
		this.tastoEsempl = tastoEsempl;
	}

	public boolean isTastoInv() {
		return tastoInv;
	}

	public void setTastoInv(boolean tastoInv) {
		this.tastoInv = tastoInv;
	}

	public boolean isTastoAltreBib() {
		return tastoAltreBib;
	}

	public void setTastoAltreBib(boolean tastoAltreBib) {
		this.tastoAltreBib = tastoAltreBib;
	}

//	public List getListaCollocazioniDiEsempl() {
//		return listaCollocazioniDiEsempl;
//	}
//
//	public void setListaCollocazioniDiEsempl(List listaCollocazioniDiEsempl) {
//		this.listaCollocazioniDiEsempl = listaCollocazioniDiEsempl;
//	}
//
//	public List getListaEsemplariDiColloc() {
//		return listaEsemplariDiColloc;
//	}
//
//	public void setListaEsemplariDiColloc(List listaEsemplariDiColloc) {
//		this.listaEsemplariDiColloc = listaEsemplariDiColloc;
//	}
//
//	public List getListaInventariDiColloc() {
//		return listaInventariDiColloc;
//	}
//
//	public void setListaInventariDiColloc(List listaInventariDiColloc) {
//		this.listaInventariDiColloc = listaInventariDiColloc;
//	}

	public InventarioTitoloVO getRecInvTit() {
		return recInvTit;
	}

	public void setRecInvTit(InventarioTitoloVO recInvTit) {
		this.recInvTit = recInvTit;
	}

	public int getNRec() {
		return nRec;
	}

	public void setNRec(int rec) {
		nRec = rec;
	}

	public EsameCollocRicercaVO getParamRicerca() {
		return paramRicerca;
	}

	public void setParamRicerca(EsameCollocRicercaVO paramRicerca) {
		this.paramRicerca = paramRicerca;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}

	public boolean isNoColl() {
		return noColl;
	}

	public void setNoColl(boolean noColl) {
		this.noColl = noColl;
	}

	public boolean isNoEsem() {
		return noEsem;
	}

	public void setNoEsem(boolean noEsem) {
		this.noEsem = noEsem;
	}

	public List getListaInventariDelTitolo() {
		return listaInventariDelTitolo;
	}

	public void setListaInventariDelTitolo(List listaInventariDelTitolo) {
		this.listaInventariDelTitolo = listaInventariDelTitolo;
	}

	public List getListaCollocazioniDelTitolo() {
		return listaCollocazioniDelTitolo;
	}

	public void setListaCollocazioniDelTitolo(List listaCollocazioniDelTitolo) {
		this.listaCollocazioniDelTitolo = listaCollocazioniDelTitolo;
	}


	public List getListaEsemplariDelTitolo() {
		return listaEsemplariDelTitolo;
	}

	public void setListaEsemplariDelTitolo(List listaEsemplariDelTitolo) {
		this.listaEsemplariDelTitolo = listaEsemplariDelTitolo;
	}

	public String getSelectedInv() {
		return selectedInv;
	}

	public void setSelectedInv(String selectedInv) {
		this.selectedInv = selectedInv;
	}

	public String getSelectedPrg() {
		return selectedPrg;
	}

	public void setSelectedPrg(String selectedPrg) {
		this.selectedPrg = selectedPrg;
	}

	public String getSelectedColl() {
		return selectedColl;
	}

	public void setSelectedColl(String selectedColl) {
		this.selectedColl = selectedColl;
	}

	public String getSelectedEsem() {
		return selectedEsem;
	}

	public void setSelectedEsem(String selectedEsem) {
		this.selectedEsem = selectedEsem;
	}

	public AreePassaggioSifVO getDatiGestioneBibliografica() {
		return DatiGestioneBibliografica;
	}

	public void setDatiGestioneBibliografica(
			AreePassaggioSifVO datiGestioneBibliografica) {
		DatiGestioneBibliografica = datiGestioneBibliografica;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

}
