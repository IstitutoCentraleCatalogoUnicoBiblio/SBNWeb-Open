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
package it.iccu.sbn.web.actionforms.gestionestampe.cataloghi;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.web.actionforms.common.documentofisico.RicercaInventariCollocazioniForm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.upload.FormFile;

//public class StampaSchedeForm extends StampaSingoloFontDinamicoForm {
	public class StampaCataloghiForm extends RicercaInventariCollocazioniForm {
    /**
		 * 
		 */
		private static final long serialVersionUID = -4976296350160351248L;


	public StampaCataloghiForm() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

    private String provenienza;
    private FormFile uploadImmagine;
    private String tipoFormato;
	private String nomeModello;
	private String elemBlocco;

    private String codicePolo;
    private String ticket;


    private List listaBiblio;
    private String natura;
    private List listaNatura;
    private String status;
    private List listaStatus;
    private String tipoAutore;
    private List listaTipoAutore;

    private List listaPaese;
	private String descPaese;
	private List listaLivAut;
	private String descLivAut;
	private List listaGenere;
	private String descGenere;
	private List listaLingua;
	private String descLingua;
	private List listaTipoData;
	private String descTipoData;

    // Opzioni di stampa
    private boolean intestTitoloAdAutore;
    private boolean titoloCollana;
    private boolean titoliAnalitici;
    private boolean datiCollocazione;

    private String numIdentificativo01;
    private String numIdentificativo02;
    private String numIdentificativo03;
    private String numIdentificativo04;
    private String numIdentificativo05;
    private String numIdentificativo06;
    private String numIdentificativo07;
    private String numIdentificativo08;
    private String numIdentificativo09;
    private String numIdentificativo10;
    private String numIdentificativo11;
    private String numIdentificativo12;
    private String numIdentificativo13;
    private String numIdentificativo14;
    private String numIdentificativo15;
    private String numIdentificativo16;
    private String numIdentificativo17;
    private String numIdentificativo18;
    private String numIdentificativo19;
    private String numIdentificativo20;
    private String numIdentificativo21;
    private String numIdentificativo22;
    private String numIdentificativo23;
    private String numIdentificativo24;
    private String numIdentificativo25;
    private String numIdentificativo26;
    private String numIdentificativo27;
    private String numIdentificativo28;
    private String numIdentificativo29;
    private String numIdentificativo30;
    private String numIdentificativo31;
    private String numIdentificativo32;
    private String numIdentificativo33;
    private String numIdentificativo34;
    private String numIdentificativo35;
    private String numIdentificativo36;
    private String numIdentificativo37;
    private String numIdentificativo38;
    private String numIdentificativo39;
    private String numIdentificativo40;


    private List listaBidDaFile;
    private List listaBid;


    public List getListaBid() {
		return listaBid;
	}

	public void setListaBid(List listaBid) {
		this.listaBid = listaBid;
	}

//	private boolean chkStampaPiuInventari;
//    private boolean chkStampaTitNonPoss;
    private boolean chkCatAttAutori;
    private boolean chkCatAttTopografico;
    private boolean chkCatAttSoggetti;
    private boolean chkCatAttTitoli;
    private boolean chkCatAttClassificazioni;
    private boolean chkCatAttEditori;
    private boolean chkCatAttPossessori;

//    private boolean chkCollSchPrincipale;
//    private boolean chkCollSchTopografico;
//    private boolean chkCollSchSoggetti;
//    private boolean chkCollSchTitoli;
//    private boolean chkCollSchClassificazioni;
//    private boolean chkCollSchRichiami;
//    private boolean chkCollSchEditori;
//    private boolean chkCollSchPossessori;

//    private int numCollSchPrincipale;
//    private int numCollSchTopografico;
//    private int numCollSchSoggetti;
//    private int numCollSchTitoli;
//    private int numCollSchClassificazioni;
//    private int numCollSchRichiami;
//    private int numCollSchEditori;
//    private int numCollSchPossessori;

	public boolean isChkCatAttAutori() {
		return chkCatAttAutori;
	}

	public void setChkCatAttAutori(boolean chkCatAttAutori) {
		this.chkCatAttAutori = chkCatAttAutori;
	}

	public boolean isChkCatAttClassificazioni() {
		return chkCatAttClassificazioni;
	}

	public void setChkCatAttClassificazioni(boolean chkCatAttClassificazioni) {
		this.chkCatAttClassificazioni = chkCatAttClassificazioni;
	}

	public boolean isChkCatAttEditori() {
		return chkCatAttEditori;
	}

	public void setChkCatAttEditori(boolean chkCatAttEditori) {
		this.chkCatAttEditori = chkCatAttEditori;
	}

	public boolean isChkCatAttPossessori() {
		return chkCatAttPossessori;
	}

	public void setChkCatAttPossessori(boolean chkCatAttPossessori) {
		this.chkCatAttPossessori = chkCatAttPossessori;
	}

	public boolean isChkCatAttSoggetti() {
		return chkCatAttSoggetti;
	}

	public void setChkCatAttSoggetti(boolean chkCatAttSoggetti) {
		this.chkCatAttSoggetti = chkCatAttSoggetti;
	}

	public boolean isChkCatAttTitoli() {
		return chkCatAttTitoli;
	}

	public void setChkCatAttTitoli(boolean chkCatAttTitoli) {
		this.chkCatAttTitoli = chkCatAttTitoli;
	}

	public boolean isChkCatAttTopografico() {
		return chkCatAttTopografico;
	}

	public void setChkCatAttTopografico(boolean chkCatAttTopografico) {
		this.chkCatAttTopografico = chkCatAttTopografico;
	}


	public String getCodiceBibl() {
		return super.getCodBib();
	}

	public void setCodiceBibl(String codiceBibl) {
		super.setCodBib(codiceBibl);
	}

	public String getDescrBibl() {
		return super.getDescrBib();
	}

	public void setDescrBibl(String descrBibl) {
		super.setDescrBib(descrBibl);
	}

	public String getDallaCollocazione() {
		return dallaCollocazione;
	}

	public void setDallaCollocazione(String dallaCollocazione) {
		this.dallaCollocazione = dallaCollocazione;
	}

	public String getElemBlocco() {
		return elemBlocco;
	}

	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}



	/**
	 * @return Returns the listaStatus.
	 */
	public List getListaStatus() {
		return listaStatus;
	}

	/**
	 * @param listaStatus The listaStatus to set.
	 */
	public void setListaStatus(List listaStatus) {
		this.listaStatus = listaStatus;
	}

	/**
	 * @return Returns the listaTipoAutore.
	 */
	public List getListaTipoAutore() {
		return listaTipoAutore;
	}

	/**
	 * @param listaTipoAutore The listaTipoAutore to set.
	 */
	public void setListaTipoAutore(List listaTipoAutore) {
		this.listaTipoAutore = listaTipoAutore;
	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}


	public String getNumIdentificativo01() {
		return numIdentificativo01;
	}

	public void setNumIdentificativo01(String numIdentificativo01) {
		this.numIdentificativo01 = numIdentificativo01;
	}

	public String getNumIdentificativo02() {
		return numIdentificativo02;
	}

	public void setNumIdentificativo02(String numIdentificativo02) {
		this.numIdentificativo02 = numIdentificativo02;
	}

	public String getNumIdentificativo03() {
		return numIdentificativo03;
	}

	public void setNumIdentificativo03(String numIdentificativo03) {
		this.numIdentificativo03 = numIdentificativo03;
	}

	public String getNumIdentificativo04() {
		return numIdentificativo04;
	}

	public void setNumIdentificativo04(String numIdentificativo04) {
		this.numIdentificativo04 = numIdentificativo04;
	}

	public String getNumIdentificativo05() {
		return numIdentificativo05;
	}

	public void setNumIdentificativo05(String numIdentificativo05) {
		this.numIdentificativo05 = numIdentificativo05;
	}

	public String getNumIdentificativo06() {
		return numIdentificativo06;
	}

	public void setNumIdentificativo06(String numIdentificativo06) {
		this.numIdentificativo06 = numIdentificativo06;
	}

	public String getNumIdentificativo07() {
		return numIdentificativo07;
	}

	public void setNumIdentificativo07(String numIdentificativo07) {
		this.numIdentificativo07 = numIdentificativo07;
	}

	public String getNumIdentificativo08() {
		return numIdentificativo08;
	}

	public void setNumIdentificativo08(String numIdentificativo08) {
		this.numIdentificativo08 = numIdentificativo08;
	}

	public String getNumIdentificativo09() {
		return numIdentificativo09;
	}

	public void setNumIdentificativo09(String numIdentificativo09) {
		this.numIdentificativo09 = numIdentificativo09;
	}

	public String getNumIdentificativo10() {
		return numIdentificativo10;
	}

	public void setNumIdentificativo10(String numIdentificativo10) {
		this.numIdentificativo10 = numIdentificativo10;
	}

	public String getNumIdentificativo11() {
		return numIdentificativo11;
	}

	public void setNumIdentificativo11(String numIdentificativo11) {
		this.numIdentificativo11 = numIdentificativo11;
	}

	public String getNumIdentificativo12() {
		return numIdentificativo12;
	}

	public void setNumIdentificativo12(String numIdentificativo12) {
		this.numIdentificativo12 = numIdentificativo12;
	}

	public String getNumIdentificativo13() {
		return numIdentificativo13;
	}

	public void setNumIdentificativo13(String numIdentificativo13) {
		this.numIdentificativo13 = numIdentificativo13;
	}

	public String getNumIdentificativo14() {
		return numIdentificativo14;
	}

	public void setNumIdentificativo14(String numIdentificativo14) {
		this.numIdentificativo14 = numIdentificativo14;
	}

	public String getNumIdentificativo15() {
		return numIdentificativo15;
	}

	public void setNumIdentificativo15(String numIdentificativo15) {
		this.numIdentificativo15 = numIdentificativo15;
	}

	public String getNumIdentificativo16() {
		return numIdentificativo16;
	}

	public void setNumIdentificativo16(String numIdentificativo16) {
		this.numIdentificativo16 = numIdentificativo16;
	}

	public String getNumIdentificativo17() {
		return numIdentificativo17;
	}

	public void setNumIdentificativo17(String numIdentificativo17) {
		this.numIdentificativo17 = numIdentificativo17;
	}

	public String getNumIdentificativo18() {
		return numIdentificativo18;
	}

	public void setNumIdentificativo18(String numIdentificativo18) {
		this.numIdentificativo18 = numIdentificativo18;
	}

	public String getNumIdentificativo19() {
		return numIdentificativo19;
	}

	public void setNumIdentificativo19(String numIdentificativo19) {
		this.numIdentificativo19 = numIdentificativo19;
	}

	public String getNumIdentificativo20() {
		return numIdentificativo20;
	}

	public void setNumIdentificativo20(String numIdentificativo20) {
		this.numIdentificativo20 = numIdentificativo20;
	}

	public String getNumIdentificativo21() {
		return numIdentificativo21;
	}

	public void setNumIdentificativo21(String numIdentificativo21) {
		this.numIdentificativo21 = numIdentificativo21;
	}

	public String getNumIdentificativo22() {
		return numIdentificativo22;
	}

	public void setNumIdentificativo22(String numIdentificativo22) {
		this.numIdentificativo22 = numIdentificativo22;
	}

	public String getNumIdentificativo23() {
		return numIdentificativo23;
	}

	public void setNumIdentificativo23(String numIdentificativo23) {
		this.numIdentificativo23 = numIdentificativo23;
	}

	public String getNumIdentificativo24() {
		return numIdentificativo24;
	}

	public void setNumIdentificativo24(String numIdentificativo24) {
		this.numIdentificativo24 = numIdentificativo24;
	}

	public String getNumIdentificativo25() {
		return numIdentificativo25;
	}

	public void setNumIdentificativo25(String numIdentificativo25) {
		this.numIdentificativo25 = numIdentificativo25;
	}

	public String getNumIdentificativo26() {
		return numIdentificativo26;
	}

	public void setNumIdentificativo26(String numIdentificativo26) {
		this.numIdentificativo26 = numIdentificativo26;
	}

	public String getNumIdentificativo27() {
		return numIdentificativo27;
	}

	public void setNumIdentificativo27(String numIdentificativo27) {
		this.numIdentificativo27 = numIdentificativo27;
	}

	public String getNumIdentificativo28() {
		return numIdentificativo28;
	}

	public void setNumIdentificativo28(String numIdentificativo28) {
		this.numIdentificativo28 = numIdentificativo28;
	}

	public String getNumIdentificativo29() {
		return numIdentificativo29;
	}

	public void setNumIdentificativo29(String numIdentificativo29) {
		this.numIdentificativo29 = numIdentificativo29;
	}

	public String getNumIdentificativo30() {
		return numIdentificativo30;
	}

	public void setNumIdentificativo30(String numIdentificativo30) {
		this.numIdentificativo30 = numIdentificativo30;
	}

	public String getNumIdentificativo31() {
		return numIdentificativo31;
	}

	public void setNumIdentificativo31(String numIdentificativo31) {
		this.numIdentificativo31 = numIdentificativo31;
	}

	public String getNumIdentificativo32() {
		return numIdentificativo32;
	}

	public void setNumIdentificativo32(String numIdentificativo32) {
		this.numIdentificativo32 = numIdentificativo32;
	}

	public String getNumIdentificativo33() {
		return numIdentificativo33;
	}

	public void setNumIdentificativo33(String numIdentificativo33) {
		this.numIdentificativo33 = numIdentificativo33;
	}

	public String getNumIdentificativo34() {
		return numIdentificativo34;
	}

	public void setNumIdentificativo34(String numIdentificativo34) {
		this.numIdentificativo34 = numIdentificativo34;
	}

	public String getNumIdentificativo35() {
		return numIdentificativo35;
	}

	public void setNumIdentificativo35(String numIdentificativo35) {
		this.numIdentificativo35 = numIdentificativo35;
	}

	public String getNumIdentificativo36() {
		return numIdentificativo36;
	}

	public void setNumIdentificativo36(String numIdentificativo36) {
		this.numIdentificativo36 = numIdentificativo36;
	}

	public String getNumIdentificativo37() {
		return numIdentificativo37;
	}

	public void setNumIdentificativo37(String numIdentificativo37) {
		this.numIdentificativo37 = numIdentificativo37;
	}

	public String getNumIdentificativo38() {
		return numIdentificativo38;
	}

	public void setNumIdentificativo38(String numIdentificativo38) {
		this.numIdentificativo38 = numIdentificativo38;
	}

	public String getNumIdentificativo39() {
		return numIdentificativo39;
	}

	public void setNumIdentificativo39(String numIdentificativo39) {
		this.numIdentificativo39 = numIdentificativo39;
	}

	public String getNumIdentificativo40() {
		return numIdentificativo40;
	}

	public void setNumIdentificativo40(String numIdentificativo40) {
		this.numIdentificativo40 = numIdentificativo40;
	}

	public List<String> getListaIdentificativi() {

		List<String> output = new ArrayList<String>();

		for (int i = 1; i <= 40; i++) {
			try {
				String format = String.format("%02d", i);
				Field field1 = this.getClass().getDeclaredField("numIdentificativo" + format );
				String bid      =  (String) field1.get(this);
				if (ValidazioneDati.strIsEmpty(bid))
					continue;
				output.add(bid.toUpperCase());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return output;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTipoAutore() {
		return tipoAutore;
	}

	public void setTipoAutore(String tipoAutore) {
		this.tipoAutore = tipoAutore;
	}

	public String getNomeModello() {
		return nomeModello;
	}

	public void setNomeModello(String nomeModello) {
		this.nomeModello = nomeModello;
	}

	public String getTipoFormato() {
		return tipoFormato;
	}

	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}

	public String getCodicePolo() {
		return codicePolo;
	}

	public void setCodicePolo(String codicePolo) {
		this.codicePolo = codicePolo;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public List getListaBiblio() {
		return listaBiblio;
	}

	public void setListaBiblio(List listaBiblio) {
		this.listaBiblio = listaBiblio;
	}


	public List addListaBiblio(ComboCodDescVO elemBiblio) {
		listaBiblio.add(elemBiblio);
		return listaBiblio;
	}

	public FormFile getUploadImmagine() {
		return uploadImmagine;
	}

	public void setUploadImmagine(FormFile uploadImmagine) {
		this.uploadImmagine = uploadImmagine;
	}

	public List getListaBidDaFile() {
		return listaBidDaFile;
	}

	public void setListaBidDaFile(List listaBidDaFile) {
		this.listaBidDaFile = listaBidDaFile;
	}

	public List addListaBidDaFile(String identificativo) {
		listaBidDaFile.add(identificativo);
		return listaBidDaFile;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public boolean isIntestTitoloAdAutore() {
		return intestTitoloAdAutore;
	}

	public void setIntestTitoloAdAutore(boolean intestTitoloAdAutore) {
		this.intestTitoloAdAutore = intestTitoloAdAutore;
	}

	public boolean isTitoloCollana() {
		return titoloCollana;
	}

	public void setTitoloCollana(boolean titoloCollana) {
		this.titoloCollana = titoloCollana;
	}

	public boolean isTitoliAnalitici() {
		return titoliAnalitici;
	}

	public void setTitoliAnalitici(boolean titoliAnalitici) {
		this.titoliAnalitici = titoliAnalitici;
	}

	public boolean isDatiCollocazione() {
		return datiCollocazione;
	}

	public void setDatiCollocazione(boolean datiCollocazione) {
		this.datiCollocazione = datiCollocazione;
	}

	public List getListaPaese() {
		return listaPaese;
	}

	public void setListaPaese(List listaPaese) {
		this.listaPaese = listaPaese;
	}

	public String getDescPaese() {
		return descPaese;
	}

	public void setDescPaese(String descPaese) {
		this.descPaese = descPaese;
	}

	public List getListaLivAut() {
		return listaLivAut;
	}

	public void setListaLivAut(List listaLivAut) {
		this.listaLivAut = listaLivAut;
	}

	public String getDescLivAut() {
		return descLivAut;
	}

	public void setDescLivAut(String descLivAut) {
		this.descLivAut = descLivAut;
	}

	public List getListaGenere() {
		return listaGenere;
	}

	public void setListaGenere(List listaGenere) {
		this.listaGenere = listaGenere;
	}

	public String getDescGenere() {
		return descGenere;
	}

	public void setDescGenere(String descGenere) {
		this.descGenere = descGenere;
	}

	public List getListaLingua() {
		return listaLingua;
	}

	public void setListaLingua(List listaLingua) {
		this.listaLingua = listaLingua;
	}

	public String getDescLingua() {
		return descLingua;
	}

	public void setDescLingua(String descLingua) {
		this.descLingua = descLingua;
	}

	public List getListaTipoData() {
		return listaTipoData;
	}

	public void setListaTipoData(List listaTipoData) {
		this.listaTipoData = listaTipoData;
	}

	public String getDescTipoData() {
		return descTipoData;
	}

	public void setDescTipoData(String descTipoData) {
		this.descTipoData = descTipoData;
	}

	public List getListaNatura() {
		return listaNatura;
	}

	public void setListaNatura(List listaNatura) {
		this.listaNatura = listaNatura;
	}

}
