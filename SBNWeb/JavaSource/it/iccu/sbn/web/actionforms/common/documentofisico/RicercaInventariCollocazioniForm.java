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
package it.iccu.sbn.web.actionforms.common.documentofisico;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.web.actionforms.acquisizioni.AcquisizioniBaseFormIntf;
import it.iccu.sbn.web.actionforms.gestionestampe.common.StampaForm;
import it.iccu.sbn.web.constant.ConstantDefault;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.upload.FormFile;

public class RicercaInventariCollocazioniForm extends StampaForm implements
		AcquisizioniBaseFormIntf {


	private static final long serialVersionUID = -8197828024449281440L;
	// private EsameCollocRicercaVO ricerca = new EsameCollocRicercaVO();
	private String codPolo;
	private String codBib;
	private String ticket;
	private String descrBib;
	private List listaBiblio;
	private List listaComboSerie = new ArrayList();
	private List listaSerie;
	private boolean noSerie;
	private boolean disable;
	private boolean sessione = false;
	private String folder;
	private String dataIngressoDa;
	private String dataIngressoA;
	private String nomeFileAppoggioInv;
	private String nomeFileAppoggioBid;
	private int nRec = Integer.valueOf(ConstantDefault.ELEMENTI_BLOCCHI.getDefault());
	private boolean esattoColl = false;
	private boolean esattoSpec = false;
	private boolean disableEsattoColl;
	private boolean disableEsattoSpec;
	//
	private String selezione;
	private boolean disableInv;
	private boolean disableColl;
	//
	private String tipoOperazione;
	//
	private String codSerieDefault;
	// Ricerca per collocazioni
	protected String codPoloSez;
	protected String codBibSez;
	protected String sezione;
	protected boolean sezPNonEsiste;
	protected String dallaCollocazione;
	protected String dallaSpecificazione;
	protected String allaCollocazione;
	protected String allaSpecificazione;
	protected String qualeColl;
	protected String qualeSpec;
	protected boolean disableDallaColl;
	protected boolean disableDallaSpec;
	protected boolean disableAllaColl;
	protected boolean disableAllaSpec;
	protected boolean disableTastoDallaColl;
	protected boolean disableTastoAllaColl;
	protected boolean disableTastoDallaSpecif;
	protected boolean disableTastoAllaSpecif;
	protected boolean disableSez;
	protected String tipoColloc;
	protected int numCopie;
	protected boolean disableSerie;
	protected boolean disableDalNum;
	protected boolean disableAlNum;

	private String codAttivita = "";

	public int getNumCopie() {
		return numCopie;
	}

	public void setNumCopie(int numCopie) {
		this.numCopie = numCopie;
	}

	// Ricerca per range di inventari
	protected String serie;
	protected String endInventario;
	protected String startInventario;

	protected FormFile fileEsterno;

	// Ricerca per inventari
	protected String numero01;
	protected String numero02;
	protected String numero03;
	protected String numero04;
	protected String numero05;
	protected String numero06;
	protected String numero07;
	protected String numero08;
	protected String numero09;
	protected String numero10;
	protected String numero11;
	protected String numero12;
	protected String numero13;
	protected String numero14;
	protected String numero15;
	protected String numero16;
	protected String numero17;
	protected String numero18;
	protected String numero19;
	protected String numero20;
	protected String numero21;
	protected String numero22;
	protected String numero23;
	protected String numero24;
	protected String numero25;
	protected String numero26;
	protected String numero27;
	protected String numero28;
	protected String numero29;
	protected String numero30;
	protected String numero31;
	protected String numero32;
	protected String numero33;
	protected String numero34;
	protected String numero35;
	protected String numero36;
	protected String serie01;
	protected String serie02;
	protected String serie03;
	protected String serie04;
	protected String serie05;
	protected String serie06;
	protected String serie07;
	protected String serie08;
	protected String serie09;
	protected String serie10;
	protected String serie11;
	protected String serie12;
	protected String serie13;
	protected String serie14;
	protected String serie15;
	protected String serie16;
	protected String serie17;
	protected String serie18;
	protected String serie19;
	protected String serie20;
	protected String serie21;
	protected String serie22;
	protected String serie23;
	protected String serie24;
	protected String serie25;
	protected String serie26;
	protected String serie27;
	protected String serie28;
	protected String serie29;
	protected String serie30;
	protected String serie31;
	protected String serie32;
	protected String serie33;
	protected String serie34;
	protected String serie35;
	protected String serie36;

	protected List listaInventari;
	private String fornitore;
	private String codFornitore;
	private FornitoreVO fornitoreVO;
	//Ricerca per date
	protected String dataDa;
	protected String dataA;
	protected String check;

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
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

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getEndInventario() {
		return endInventario;
	}

	public void setEndInventario(String endInventario) {
		this.endInventario = endInventario;
	}

	public String getStartInventario() {
		return startInventario;
	}

	public void setStartInventario(String startInventario) {
		this.startInventario = startInventario;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
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

	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String descrBib) {
		this.descrBib = ValidazioneDati.trimOrEmpty(descrBib);
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public List getListaBiblio() {
		return listaBiblio;
	}

	public void setListaBiblio(List listaBiblio) {
		this.listaBiblio = listaBiblio;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getDataIngressoDa() {
		return dataIngressoDa;
	}

	public void setDataIngressoDa(String dataIngressoDa) {
		this.dataIngressoDa = dataIngressoDa;
	}

	public String getDataIngressoA() {
		return dataIngressoA;
	}

	public void setDataIngressoA(String dataIngressoA) {
		this.dataIngressoA = dataIngressoA;
	}

	public List getListaComboSerie() {
		return listaComboSerie;
	}

	public void setListaComboSerie(List listaComboSerie) {
		this.listaComboSerie = listaComboSerie;
	}

	public List getListaSerie() {
		return listaSerie;
	}

	public void setListaSerie(List listaSerie) {
		this.listaSerie = listaSerie;
	}

	public boolean isNoSerie() {
		return noSerie;
	}

	public void setNoSerie(boolean noSerie) {
		this.noSerie = noSerie;
	}

	public List<CodiceVO> getListaInventariInput() throws Exception {

		List<CodiceVO> output = new ArrayList<CodiceVO>();
		// HashSet<CodiceVO> hm = new HashSet();

		Class<?> zuper = this.getClass();
		while (zuper != RicercaInventariCollocazioniForm.class)
			zuper = zuper.getSuperclass();

		for (int i = 1; i <= 36; i++) {
			try {
				String format = String.format("%02d", i);
				Field field1 = zuper.getDeclaredField("serie" + format);
				Field field2 = zuper.getDeclaredField("numero" + format);
				String serie = (String) field1.get(this);
				String inventario = (String) field2.get(this);
				if (ValidazioneDati.strIsEmpty(serie)
						&& ValidazioneDati.strIsEmpty(inventario))
					continue;
				if (!serie.equals("  ") && !inventario.trim().equals("")) {
					output.add(new CodiceVO(serie, inventario));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		// output.addAll(hm);
		return output;
	}

	public int getNRec() {
		return nRec;
	}

	public void setNRec(int rec) {
		nRec = rec;
	}

	public String getCodPoloSez() {
		return codPoloSez;
	}

	public void setCodPoloSez(String codPoloSez) {
		this.codPoloSez = codPoloSez;
	}

	public String getCodBibSez() {
		return codBibSez;
	}

	public void setCodBibSez(String codBibSez) {
		this.codBibSez = codBibSez;
	}

	// public EsameCollocRicercaVO getRicerca() {
	// return ricerca;
	// }
	// public void setRicerca(EsameCollocRicercaVO ricerca) {
	// this.ricerca = ricerca;
	// }
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

	public String getQualeColl() {
		return qualeColl;
	}

	public void setQualeColl(String qualeColl) {
		this.qualeColl = qualeColl;
	}

	public String getQualeSpec() {
		return qualeSpec;
	}

	public void setQualeSpec(String qualeSpec) {
		this.qualeSpec = qualeSpec;
	}

	public String getSelezione() {
		return selezione;
	}

	public void setSelezione(String selezione) {
		this.selezione = selezione;
	}

	public boolean isDisableInv() {
		return disableInv;
	}

	public void setDisableInv(boolean disableInv) {
		this.disableInv = disableInv;
	}

	public boolean isDisableColl() {
		return disableColl;
	}

	public void setDisableColl(boolean disableColl) {
		this.disableColl = disableColl;
	}

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public boolean isSezPNonEsiste() {
		return sezPNonEsiste;
	}

	public void setSezPNonEsiste(boolean sezPNonEsiste) {
		this.sezPNonEsiste = sezPNonEsiste;
	}

	public List getListaInventari() {
		return listaInventari;
	}

	public void setListaInventari(List<CodiceVO> listaInventari) {
		this.listaInventari = listaInventari;
	}

	public String getCodSerieDefault() {
		return codSerieDefault;
	}

	public void setCodSerieDefault(String codSerieDefault) {
		this.codSerieDefault = codSerieDefault;
	}

	public boolean isDisableTastoDallaColl() {
		return disableTastoDallaColl;
	}

	public void setDisableTastoDallaColl(boolean disableTastoDallaColl) {
		this.disableTastoDallaColl = disableTastoDallaColl;
	}

	public boolean isDisableTastoAllaColl() {
		return disableTastoAllaColl;
	}

	public void setDisableTastoAllaColl(boolean disableTastoAllaColl) {
		this.disableTastoAllaColl = disableTastoAllaColl;
	}

	public boolean isDisableTastoDallaSpecif() {
		return disableTastoDallaSpecif;
	}

	public void setDisableTastoDallaSpecif(boolean disableTastoDallaSpecif) {
		this.disableTastoDallaSpecif = disableTastoDallaSpecif;
	}

	public boolean isDisableTastoAllaSpecif() {
		return disableTastoAllaSpecif;
	}

	public void setDisableTastoAllaSpecif(boolean disableTastoAllaSpecif) {
		this.disableTastoAllaSpecif = disableTastoAllaSpecif;
	}

	public boolean isEsattoColl() {
		return esattoColl;
	}

	public void setEsattoColl(boolean esattoColl) {
		this.esattoColl = esattoColl;
	}

	public boolean isEsattoSpec() {
		return esattoSpec;
	}

	public void setEsattoSpec(boolean esattoSpec) {
		this.esattoSpec = esattoSpec;
	}

	public boolean isDisableEsattoColl() {
		return disableEsattoColl;
	}

	public void setDisableEsattoColl(boolean disableEsattoColl) {
		this.disableEsattoColl = disableEsattoColl;
	}

	public boolean isDisableEsattoSpec() {
		return disableEsattoSpec;
	}

	public void setDisableEsattoSpec(boolean disableEsattoSpec) {
		this.disableEsattoSpec = disableEsattoSpec;
	}

	public boolean isDisableSez() {
		return disableSez;
	}

	public void setDisableSez(boolean disableSez) {
		this.disableSez = disableSez;
	}

	public FormFile getFileEsterno() {
		return fileEsterno;
	}

	public void setFileEsterno(FormFile fileEsterno) {
		this.fileEsterno = fileEsterno;
	}

	public String getNumero01() {
		return numero01;
	}

	public void setNumero01(String numero01) {
		this.numero01 = numero01;
	}

	public String getNumero02() {
		return numero02;
	}

	public void setNumero02(String numero02) {
		this.numero02 = numero02;
	}

	public String getNumero03() {
		return numero03;
	}

	public void setNumero03(String numero03) {
		this.numero03 = numero03;
	}

	public String getNumero04() {
		return numero04;
	}

	public void setNumero04(String numero04) {
		this.numero04 = numero04;
	}

	public String getNumero05() {
		return numero05;
	}

	public void setNumero05(String numero05) {
		this.numero05 = numero05;
	}

	public String getNumero06() {
		return numero06;
	}

	public void setNumero06(String numero06) {
		this.numero06 = numero06;
	}

	public String getNumero07() {
		return numero07;
	}

	public void setNumero07(String numero07) {
		this.numero07 = numero07;
	}

	public String getNumero08() {
		return numero08;
	}

	public void setNumero08(String numero08) {
		this.numero08 = numero08;
	}

	public String getNumero09() {
		return numero09;
	}

	public void setNumero09(String numero09) {
		this.numero09 = numero09;
	}

	public String getNumero10() {
		return numero10;
	}

	public void setNumero10(String numero10) {
		this.numero10 = numero10;
	}

	public String getNumero11() {
		return numero11;
	}

	public void setNumero11(String numero11) {
		this.numero11 = numero11;
	}

	public String getNumero12() {
		return numero12;
	}

	public void setNumero12(String numero12) {
		this.numero12 = numero12;
	}

	public String getNumero13() {
		return numero13;
	}

	public void setNumero13(String numero13) {
		this.numero13 = numero13;
	}

	public String getNumero14() {
		return numero14;
	}

	public void setNumero14(String numero14) {
		this.numero14 = numero14;
	}

	public String getNumero15() {
		return numero15;
	}

	public void setNumero15(String numero15) {
		this.numero15 = numero15;
	}

	public String getNumero16() {
		return numero16;
	}

	public void setNumero16(String numero16) {
		this.numero16 = numero16;
	}

	public String getNumero17() {
		return numero17;
	}

	public void setNumero17(String numero17) {
		this.numero17 = numero17;
	}

	public String getNumero18() {
		return numero18;
	}

	public void setNumero18(String numero18) {
		this.numero18 = numero18;
	}

	public String getNumero19() {
		return numero19;
	}

	public void setNumero19(String numero19) {
		this.numero19 = numero19;
	}

	public String getNumero20() {
		return numero20;
	}

	public void setNumero20(String numero20) {
		this.numero20 = numero20;
	}

	public String getNumero21() {
		return numero21;
	}

	public void setNumero21(String numero21) {
		this.numero21 = numero21;
	}

	public String getNumero22() {
		return numero22;
	}

	public void setNumero22(String numero22) {
		this.numero22 = numero22;
	}

	public String getNumero23() {
		return numero23;
	}

	public void setNumero23(String numero23) {
		this.numero23 = numero23;
	}

	public String getNumero24() {
		return numero24;
	}

	public void setNumero24(String numero24) {
		this.numero24 = numero24;
	}

	public String getNumero25() {
		return numero25;
	}

	public void setNumero25(String numero25) {
		this.numero25 = numero25;
	}

	public String getNumero26() {
		return numero26;
	}

	public void setNumero26(String numero26) {
		this.numero26 = numero26;
	}

	public String getNumero27() {
		return numero27;
	}

	public void setNumero27(String numero27) {
		this.numero27 = numero27;
	}

	public String getNumero28() {
		return numero28;
	}

	public void setNumero28(String numero28) {
		this.numero28 = numero28;
	}

	public String getNumero29() {
		return numero29;
	}

	public void setNumero29(String numero29) {
		this.numero29 = numero29;
	}

	public String getNumero30() {
		return numero30;
	}

	public void setNumero30(String numero30) {
		this.numero30 = numero30;
	}

	public String getNumero31() {
		return numero31;
	}

	public void setNumero31(String numero31) {
		this.numero31 = numero31;
	}

	public String getNumero32() {
		return numero32;
	}

	public void setNumero32(String numero32) {
		this.numero32 = numero32;
	}

	public String getNumero33() {
		return numero33;
	}

	public void setNumero33(String numero33) {
		this.numero33 = numero33;
	}

	public String getNumero34() {
		return numero34;
	}

	public void setNumero34(String numero34) {
		this.numero34 = numero34;
	}

	public String getNumero35() {
		return numero35;
	}

	public void setNumero35(String numero35) {
		this.numero35 = numero35;
	}

	public String getNumero36() {
		return numero36;
	}

	public void setNumero36(String numero36) {
		this.numero36 = numero36;
	}

	public String getSerie01() {
		return serie01;
	}

	public void setSerie01(String serie01) {
		this.serie01 = serie01;
	}

	public String getSerie02() {
		return serie02;
	}

	public void setSerie02(String serie02) {
		this.serie02 = serie02;
	}

	public String getSerie03() {
		return serie03;
	}

	public void setSerie03(String serie03) {
		this.serie03 = serie03;
	}

	public String getSerie04() {
		return serie04;
	}

	public void setSerie04(String serie04) {
		this.serie04 = serie04;
	}

	public String getSerie05() {
		return serie05;
	}

	public void setSerie05(String serie05) {
		this.serie05 = serie05;
	}

	public String getSerie06() {
		return serie06;
	}

	public void setSerie06(String serie06) {
		this.serie06 = serie06;
	}

	public String getSerie07() {
		return serie07;
	}

	public void setSerie07(String serie07) {
		this.serie07 = serie07;
	}

	public String getSerie08() {
		return serie08;
	}

	public void setSerie08(String serie08) {
		this.serie08 = serie08;
	}

	public String getSerie09() {
		return serie09;
	}

	public void setSerie09(String serie09) {
		this.serie09 = serie09;
	}

	public String getSerie10() {
		return serie10;
	}

	public void setSerie10(String serie10) {
		this.serie10 = serie10;
	}

	public String getSerie11() {
		return serie11;
	}

	public void setSerie11(String serie11) {
		this.serie11 = serie11;
	}

	public String getSerie12() {
		return serie12;
	}

	public void setSerie12(String serie12) {
		this.serie12 = serie12;
	}

	public String getSerie13() {
		return serie13;
	}

	public void setSerie13(String serie13) {
		this.serie13 = serie13;
	}

	public String getSerie14() {
		return serie14;
	}

	public void setSerie14(String serie14) {
		this.serie14 = serie14;
	}

	public String getSerie15() {
		return serie15;
	}

	public void setSerie15(String serie15) {
		this.serie15 = serie15;
	}

	public String getSerie16() {
		return serie16;
	}

	public void setSerie16(String serie16) {
		this.serie16 = serie16;
	}

	public String getSerie17() {
		return serie17;
	}

	public void setSerie17(String serie17) {
		this.serie17 = serie17;
	}

	public String getSerie18() {
		return serie18;
	}

	public void setSerie18(String serie18) {
		this.serie18 = serie18;
	}

	public String getSerie19() {
		return serie19;
	}

	public void setSerie19(String serie19) {
		this.serie19 = serie19;
	}

	public String getSerie20() {
		return serie20;
	}

	public void setSerie20(String serie20) {
		this.serie20 = serie20;
	}

	public String getSerie21() {
		return serie21;
	}

	public void setSerie21(String serie21) {
		this.serie21 = serie21;
	}

	public String getSerie22() {
		return serie22;
	}

	public void setSerie22(String serie22) {
		this.serie22 = serie22;
	}

	public String getSerie23() {
		return serie23;
	}

	public void setSerie23(String serie23) {
		this.serie23 = serie23;
	}

	public String getSerie24() {
		return serie24;
	}

	public void setSerie24(String serie24) {
		this.serie24 = serie24;
	}

	public String getSerie25() {
		return serie25;
	}

	public void setSerie25(String serie25) {
		this.serie25 = serie25;
	}

	public String getSerie26() {
		return serie26;
	}

	public void setSerie26(String serie26) {
		this.serie26 = serie26;
	}

	public String getSerie27() {
		return serie27;
	}

	public void setSerie27(String serie27) {
		this.serie27 = serie27;
	}

	public String getSerie28() {
		return serie28;
	}

	public void setSerie28(String serie28) {
		this.serie28 = serie28;
	}

	public String getSerie29() {
		return serie29;
	}

	public void setSerie29(String serie29) {
		this.serie29 = serie29;
	}

	public String getSerie30() {
		return serie30;
	}

	public void setSerie30(String serie30) {
		this.serie30 = serie30;
	}

	public String getSerie31() {
		return serie31;
	}

	public void setSerie31(String serie31) {
		this.serie31 = serie31;
	}

	public String getSerie32() {
		return serie32;
	}

	public void setSerie32(String serie32) {
		this.serie32 = serie32;
	}

	public String getSerie33() {
		return serie33;
	}

	public void setSerie33(String serie33) {
		this.serie33 = serie33;
	}

	public String getSerie34() {
		return serie34;
	}

	public void setSerie34(String serie34) {
		this.serie34 = serie34;
	}

	public String getSerie35() {
		return serie35;
	}

	public void setSerie35(String serie35) {
		this.serie35 = serie35;
	}

	public String getSerie36() {
		return serie36;
	}

	public void setSerie36(String serie36) {
		this.serie36 = serie36;
	}

	public String getTipoColloc() {
		return tipoColloc;
	}

	public void setTipoColloc(String tipoColloc) {
		this.tipoColloc = tipoColloc;
	}

	public String getNomeFileAppoggioInv() {
		return nomeFileAppoggioInv;
	}

	public void setNomeFileAppoggioInv(String nomeFileAppoggioInv) {
		this.nomeFileAppoggioInv = nomeFileAppoggioInv;
	}

	public String getNomeFileAppoggioBid() {
		return nomeFileAppoggioBid;
	}

	public void setNomeFileAppoggioBid(String nomeFileAppoggioBid) {
		this.nomeFileAppoggioBid = nomeFileAppoggioBid;
	}

	public boolean isDisableSerie() {
		return disableSerie;
	}

	public void setDisableSerie(boolean disableSerie) {
		this.disableSerie = disableSerie;
	}

	public boolean isDisableDalNum() {
		return disableDalNum;
	}

	public void setDisableDalNum(boolean disableDalNum) {
		this.disableDalNum = disableDalNum;
	}

	public boolean isDisableAlNum() {
		return disableAlNum;
	}

	public void setDisableAlNum(boolean disableAlNum) {
		this.disableAlNum = disableAlNum;
	}

	public String getFornitore() {
		return fornitore;
	}

	public void setFornitore(String fornitore) {
		this.fornitore = fornitore;
	}

	public String getCodFornitore() {
		return codFornitore;
	}

	public void setCodFornitore(String codFornitore) {
		this.codFornitore = codFornitore;
	}

	public FornitoreVO getFornitoreVO() {
		return fornitoreVO;
	}

	public void setFornitoreVO(FornitoreVO fornitoreVO) {
		this.fornitoreVO = fornitoreVO;
	}

	public String getDataDa() {
		return dataDa;
	}

	public void setDataDa(String dataDa) {
		this.dataDa = dataDa;
	}

	public String getDataA() {
		return dataA;
	}

	public void setDataA(String dataA) {
		this.dataA = dataA;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getCodAttivita() {
		return codAttivita;
	}

	public void setCodAttivita(String codAttivita) {
		this.codAttivita = codAttivita;
	}

}
