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
package it.iccu.sbn.web.actionforms.gestionestampe.strumentiPatrimonio;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.FakeParamRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaStrumentiPatrimonioVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.web.actionforms.common.documentofisico.RicercaInventariCollocazioniForm;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.upload.FormFile;

public class StampaStrumentiPatrimonioForm extends RicercaInventariCollocazioniForm {

	private static final long serialVersionUID = -8773906635764414296L;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private String ticket;
	private List listaTipiOrdinamento;

	private String tipoOperazione;
	private String tipoOrdinamento;
	private String elemBlocco;
	private String tipoRicerca;
	private EsameCollocRicercaVO ricerca = new EsameCollocRicercaVO();
	private List listaBiblio;
	private boolean disable;
	private boolean sessione = false;
	private String folder;
	private String codiceStatoConservazione;
	private List listaCodStatoConservazione;
	private String codiceTipoMateriale;
	private List listaTipoMateriale;
	private String codNoDispo;
	private List listaCodNoDispo;//
	private boolean disableModPrel;
	private boolean disableModRegistri;
	private boolean modPrel = false;
	private boolean modRegistri = false;
	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault());

	private String tipoModello;
	private String tipoModello2;
	private List<ModelloStampaVO> elencoModelli = new ArrayList<ModelloStampaVO>();
	private String tipoFormato;
	private List<ModelloStampaVO> modelloPrelievo = new ArrayList<ModelloStampaVO>();
	private String dataPrelievo;
	private String motivoPrelievo;

	private String dataPrimaCollDa;
	private String dataPrimaCollA;

	private StampaStrumentiPatrimonioVO richiesta = new StampaStrumentiPatrimonioVO(FakeParamRichiestaElaborazioneDifferitaVO.FAKE_INSTANCE);



	// almaviva2 Settembre 2017: modifica a Stampa Strumenti Patrimonio per consentire la stampa per una lista di titoli proveniente
	// da file o dalla valorizzazione dei campindi bid sulla mappa
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
    private FormFile uploadImmagine;



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

	public List getListaBid() {
		return listaBid;
	}

	public void setListaBid(List listaBid) {
		this.listaBid = listaBid;
	}

	public EsameCollocRicercaVO getRicerca() {
		return ricerca;
	}

	public void setRicerca(EsameCollocRicercaVO ricerca) {
		this.ricerca = ricerca;
	}

	public List getListaBiblio() {
		return listaBiblio;
	}

	public void setListaBiblio(List listaBiblio) {
		this.listaBiblio = listaBiblio;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public int getNRec() {
		return nRec;
	}

	public void setNRec(int rec) {
		nRec = rec;
	}



	public String getTipoFormato() {
		return tipoFormato;
	}

	public void setTipoFormato(String tipoFormato) {
		this.tipoFormato = tipoFormato;
	}

	public String getCodiceStatoConservazione() {
		return codiceStatoConservazione;
	}

	public void setCodiceStatoConservazione(String codiceStatoConservazione) {
		this.codiceStatoConservazione = codiceStatoConservazione;
	}

	public List getListaCodStatoConservazione() {
		return listaCodStatoConservazione;
	}

	public void setListaCodStatoConservazione(
			List listaCodStatoConservazione) {
		this.listaCodStatoConservazione = listaCodStatoConservazione;
	}

	public String getCodiceTipoMateriale() {
		return codiceTipoMateriale;
	}

	public void setCodiceTipoMateriale(String codiceTipoMateriale) {
		this.codiceTipoMateriale = codiceTipoMateriale;
	}

	public List getListaTipoMateriale() {
		return listaTipoMateriale;
	}

	public void setListaTipoMateriale(List listaTipoMateriale) {
		this.listaTipoMateriale = listaTipoMateriale;
	}

	public String getElemBlocco() {
		return elemBlocco;
	}

	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}

	public String getTipoRicerca() {
		return tipoRicerca;
	}

	public void setTipoRicerca(String tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public List getListaTipiOrdinamento() {
		return listaTipiOrdinamento;
	}

	public void setListaTipiOrdinamento(List listaTipiOrdinamento) {
		this.listaTipiOrdinamento = listaTipiOrdinamento;
	}

	public String getTipoModello() {
		return tipoModello;
	}

	public void setTipoModello(String tipoModello) {
		this.tipoModello = tipoModello;
	}

	public List<ModelloStampaVO > getElencoModelli() {
		return elencoModelli;
	}

	public void setElencoModelli(List<ModelloStampaVO> elencoModelli) {
		this.elencoModelli = elencoModelli;
	}

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}

	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}

	public String getCodNoDispo() {
		return codNoDispo;
	}

	public void setCodNoDispo(String codNoDispo) {
		this.codNoDispo = codNoDispo;
	}

	public List getListaCodNoDispo() {
		return listaCodNoDispo;
	}

	public void setListaCodNoDispo(List listaCodNoDispo) {
		this.listaCodNoDispo = listaCodNoDispo;
	}

	public boolean isDisableModPrel() {
		return disableModPrel;
	}

	public void setDisableModPrel(boolean disableModPrel) {
		this.disableModPrel = disableModPrel;
	}

	public boolean isDisableModRegistri() {
		return disableModRegistri;
	}

	public void setDisableModRegistri(boolean disableModRegistri) {
		this.disableModRegistri = disableModRegistri;
	}

	public boolean isModPrel() {
		return modPrel;
	}

	public void setModPrel(boolean modPrel) {
		this.modPrel = modPrel;
	}

	public boolean isModRegistri() {
		return modRegistri;
	}

	public void setModRegistri(boolean modRegistri) {
		this.modRegistri = modRegistri;
	}

	public List<ModelloStampaVO> getModelloPrelievo() {
		return modelloPrelievo;
	}

	public void setModelloPrelievo(List<ModelloStampaVO> modelloPrelievo) {
		this.modelloPrelievo = modelloPrelievo;
	}

	public String getDataPrelievo() {
		return dataPrelievo;
	}

	public void setDataPrelievo(String dataPrelievo) {
		this.dataPrelievo = dataPrelievo;
	}

	public String getMotivoPrelievo() {
		return motivoPrelievo;
	}

	public void setMotivoPrelievo(String motivoPrelievo) {
		this.motivoPrelievo = motivoPrelievo;
	}

	public String getTipoModello2() {
		return tipoModello2;
	}

	public void setTipoModello2(String tipoModello2) {
		this.tipoModello2 = tipoModello2;
	}

	public String getDataPrimaCollA() {
		return dataPrimaCollA;
	}

	public void setDataPrimaCollA(String dataPrimaCollA) {
		this.dataPrimaCollA = dataPrimaCollA;
	}

	public String getDataPrimaCollDa() {
		return dataPrimaCollDa;
	}

	public void setDataPrimaCollDa(String dataPrimaCollDa) {
		this.dataPrimaCollDa = dataPrimaCollDa;
	}

	public StampaStrumentiPatrimonioVO getRichiesta() {
		return richiesta;
	}

	public void setRichiesta(StampaStrumentiPatrimonioVO richiesta) {
		this.richiesta = richiesta;
	}

	public FormFile getUploadImmagine() {
		return uploadImmagine;
	}

	public void setUploadImmagine(FormFile uploadImmagine) {
		this.uploadImmagine = uploadImmagine;
	}
}
