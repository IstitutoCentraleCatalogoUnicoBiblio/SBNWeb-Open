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
package it.iccu.sbn.web.actionforms.documentofisico.modelliEtichette;

import it.iccu.sbn.ejb.vo.documentofisico.ModelloEtichetteVO;
import it.iccu.sbn.ejb.vo.gestionestampe.etichette.FormattazioneVO;
import it.iccu.sbn.ejb.vo.gestionestampe.etichette.FormattazioneVOCampoEtichetta;
import it.iccu.sbn.ejb.vo.gestionestampe.etichette.FormattazioneVOImmagini;
import it.iccu.sbn.ejb.vo.stampe.StrutturaCombo;
import it.iccu.sbn.web.actionforms.gestionestampe.common.StampaForm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMapping;

public class ModelliEtichetteGestioneForm extends StampaForm {


	private static final long serialVersionUID = -7584685161287426535L;
	private String codPolo;
	private String codBib;
	private String descrBib;
	private String descrBibEtichetta;
	private boolean disableCodModello;
	private boolean disableBib;
	private boolean disableDescrBib;
	private String codModello;
	private String descrModello;
	private ModelloEtichetteVO recModello = new ModelloEtichetteVO();
	private boolean disable;
	private boolean esamina;
	private boolean sessione;
	private String prov;
	private boolean conferma;
	private boolean salva;
	private boolean date=false;
	private String ticket;

	//-----------------
//	protected float larghezzaPagina;
//	protected float altezzaPagina;
//	protected float margineSup;
//	protected float margineSin;
//	protected float margineDes;
//	protected float margineInf;
//	protected String unitaDiMisura;
//	//-----------------
//	private boolean bordiEtichetta;
//	private float altezzaEtichetta;
//	private float larghezzaEtichetta;
//	private float margineSupEtichetta;
//	private float margineSinEtichetta;
//	private float margineDesEtichetta;
//	private float margineInfEtichetta;
//	private float spaziaturaEtichetteX;
//	private float spaziaturaEtichetteY;
	//-----------------
	private FormattazioneVO formattazioneModello;
	//-----------------
//	private List<FormattazioneVOCampoEtichetta> campiEtichetta;
//	//-----------------
//	private List<FormattazioneVOImmagini> campiImmagine;
	//-----------------

	//-----------------
	// gv campi che vengono dal navigation fine

	private float[] dimensioneOrizzontale;

	private float[] dimensioneVerticale;

	private String elemBlocco;

	private String endInventario;
	// gv campi per richiesta tipo "I" fine

	private String formatoEtichetta;

	private List listaPosizioni;
	// private List listaStili;
	// private List listaFont;

	// private List listaPunti;
	private List listaSerie;

	private boolean modifica;
	private byte[] reportCompilato;

	// gv campi per richiesta tipo "I"
	private String serie;


	private int stampaDa;

	private String startInventario;

	protected String[] concatena;

	// string per multibox
	//protected String[] corsivo;
	//protected String[] font;

	// string per multibox
	//protected String[] grassetto;

	protected List listaFont;
	protected List listaModelli;

	protected List listaPunti;

	//protected String[] posizione;
	//protected String[] presente;

	//protected String[] punti;

	//protected String[] verticale;
	//protected float[] x;

	//protected float[] y;

	public ModelliEtichetteGestioneForm() throws Exception {
		super();
		inizializza();
	}

	/*private void azzeraCheckBox() {
		String[] vuota = { "" };
		presente = vuota;
		concatena = vuota;
		grassetto = vuota;
		corsivo = vuota;
		verticale = vuota;
	}*/

	private List loadListaCampi() {
		FormattazioneVOCampoEtichetta campo;
		List listaCampi = new ArrayList();

		campo = new FormattazioneVOCampoEtichetta();
		campo.setNomeCampo("biblioteca");// descrizione della biblioteca
		// campo.setNomeCampo("descrBib1");//descrizione della biblioteca
		campo.setPunti("09");
		campo.setPosizione("01");
		campo.setX(1f);
		listaCampi.add(campo);

		campo = new FormattazioneVOCampoEtichetta();
		campo.setNomeCampo("sezione");// sezione (10)
		// campo.setNomeCampo("codSez");//sezione (10)
		campo.setPunti("12");
		campo.setPosizione("02");
		campo.setX(1f);
		listaCampi.add(campo);

		campo = new FormattazioneVOCampoEtichetta();
		campo.setNomeCampo("collocazione");// collocazione (24)
		// campo.setNomeCampo("codLoc");//collocazione (24)
		campo.setPunti("14");
		campo.setPosizione("03");
		listaCampi.add(campo);

		campo = new FormattazioneVOCampoEtichetta();
		campo.setNomeCampo("specificazione");// specificazione (12)
		// campo.setNomeCampo("specLoc");//specificazione (12)
		campo.setPunti("10");
		campo.setPosizione("04");
		listaCampi.add(campo);

		campo = new FormattazioneVOCampoEtichetta();
		campo.setNomeCampo("sequenza");// sequenza della collocazione (24?)
		// campo.setNomeCampo("seqColl");//sequenza della collocazione (24?)
		campo.setPunti("10");
		campo.setPosizione("05");
		listaCampi.add(campo);

		campo = new FormattazioneVOCampoEtichetta();
		campo.setNomeCampo("serie");// serie (3)
		// campo.setNomeCampo("codSerie");//serie (3)
		campo.setPunti("10");
		campo.setPosizione("06");
		listaCampi.add(campo);

		campo = new FormattazioneVOCampoEtichetta();
		campo.setNomeCampo("inventario");// inventario (9)
		// campo.setNomeCampo("codInv");//inventario (9)
		campo.setPunti("10");
		campo.setPosizione("07");
		listaCampi.add(campo);

		return listaCampi;
	}

	// loadListaImmagini
	private List loadListaImmagini() {
		FormattazioneVOImmagini campo;
		List listaImmagini = new ArrayList();

		campo = new FormattazioneVOImmagini();
		campo.setNomeImmagine("immagine01");
		campo.setDimensioneOrizzontale(2f);
		campo.setDimensioneVerticale(3f);
		campo.setX(1f);
		listaImmagini.add(campo);

		campo = new FormattazioneVOImmagini();
		campo.setNomeImmagine("immagine02");
		campo.setDimensioneOrizzontale(2f);
		campo.setDimensioneVerticale(3f);
		campo.setX(1f);
		listaImmagini.add(campo);

		return listaImmagini;
	}

	private List loadListaPosizioni() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("01", "sinistra");
		lista.add(elem);
		elem = new StrutturaCombo("02", "destra");
		lista.add(elem);
		elem = new StrutturaCombo("03", "centrato");
		lista.add(elem);
		elem = new StrutturaCombo("04", "giustificato");
		lista.add(elem);
		return lista;
	}

	/*private void ricaricaTabellaCampi() {

		// con questo ciclio imposto nei default i valori della prima tabella
		// (campi)
		int i = 0;
		for (i = 0; i < font.length; i++) {
			FormattazioneVOCampoEtichetta campoCorrente = this.getFormattazioneModello().getCampiEtichetta().get(i);

			campoCorrente.setX(x[i]);
			campoCorrente.setY(y[i]);
			campoCorrente.setFont(font[i]);
			campoCorrente.setPunti(punti[i]);
			campoCorrente.setPosizione(posizione[i]);
			this.getFormattazioneModello().getCampiEtichetta().set(i, campoCorrente);
		}

		// con questo ciclio imposto nei default i valori della seconda tabella
		// (immagini)
		for (int j = 0; j < dimensioneOrizzontale.length; j++) {
			FormattazioneVOImmagini campoImmagine = this.getFormattazioneModello().getCampiImmagine().get(j);

			campoImmagine.setX(x[i + j]);
			campoImmagine.setY(y[i + j]);
			campoImmagine.setDimensioneOrizzontale(dimensioneOrizzontale[j]);
			campoImmagine.setDimensioneVerticale(dimensioneVerticale[j]);

			this.getFormattazioneModello().getCampiImmagine().set(j, campoImmagine);
		}

	}*/

	protected void inizializza() throws Exception {

		listaFont = loadListaFont();
		listaPunti = loadListaPunti();
		FormattazioneVO  formatta= new FormattazioneVO();
		formatta.setAltezzaPagina(29.7f);
		formatta.setLarghezzaPagina(21f);

		formatta.setUnitaDiMisura("cm");
		formatta.setMargineSup(0.9f);
		formatta.setMargineSin(0.8f);
		formatta.setMargineDes(1.3f);
		formatta.setMargineInf(1.6f);

		formatta.setCampiEtichetta(loadListaCampi());
		formatta.setCampiImmagine(loadListaImmagini());

		formatta.setBordiEtichetta(false);
		formatta.setAltezzaEtichetta(7f);
		formatta.setLarghezzaEtichetta(5f);

		formatta.setAltezzaPagina(29.7f);
		formatta.setLarghezzaPagina(21f);

		formatta.setMargineSup(0.9f);
		formatta.setMargineSin(0.8f);
		formatta.setMargineDes(1.3f);
		formatta.setMargineInf(1.6f);

		formatta.setMargineSupEtichetta(0.1f);
		formatta.setMargineSinEtichetta(0.1f);
		formatta.setMargineDesEtichetta(0.1f);
		formatta.setMargineInfEtichetta(0.1f);

		formatta.setSpaziaturaEtichetteX(0.2f);
		formatta.setSpaziaturaEtichetteY(0.3f);

		listaPunti = loadListaPunti();
		listaPosizioni = loadListaPosizioni();
		listaFont = loadListaFont();
		this.setFormattazioneModello(formatta);
	}

	protected List loadListaFont() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("Arial", "Arial");
		lista.add(elem);
		elem = new StrutturaCombo("Times New Roman", "Times");
		lista.add(elem);
		elem = new StrutturaCombo("Courier New", "Courier");
		lista.add(elem);
		return lista;
	}

	protected List loadListaPunti() throws Exception {
		List lista = new ArrayList();
		StrutturaCombo elem = new StrutturaCombo("09", "9");
		lista.add(elem);
		elem = new StrutturaCombo("10", "10");
		lista.add(elem);
		elem = new StrutturaCombo("11", "11");
		lista.add(elem);
		elem = new StrutturaCombo("12", "12");
		lista.add(elem);
		elem = new StrutturaCombo("13", "13");
		lista.add(elem);
		elem = new StrutturaCombo("14", "14");
		lista.add(elem);
		return lista;
	}

//	public boolean cambiaQualcosa(ModelliEtichetteGestioneForm vecchio) {
//
//		if (bordiEtichetta != vecchio.bordiEtichetta)
//			return true;
//		if (altezzaEtichetta != vecchio.altezzaEtichetta)
//			return true;
//		if (larghezzaEtichetta != vecchio.larghezzaEtichetta)
//			return true;
//
//		if (altezzaPagina != vecchio.altezzaPagina)
//			return true;
//		if (larghezzaPagina != vecchio.larghezzaPagina)
//			return true;
//
//		if (margineSup != vecchio.margineSup)
//			return true;
//		if (margineSin != vecchio.margineSin)
//			return true;
//		if (margineDes != vecchio.margineDes)
//			return true;
//		if (margineInf != vecchio.margineInf)
//			return true;
//
//		if (margineSupEtichetta != vecchio.margineSupEtichetta)
//			return true;
//		if (margineSinEtichetta != vecchio.margineSinEtichetta)
//			return true;
//		if (margineDesEtichetta != vecchio.margineDesEtichetta)
//			return true;
//		if (margineInfEtichetta != vecchio.margineInfEtichetta)
//			return true;
//
//		if (spaziaturaEtichetteX != vecchio.spaziaturaEtichetteX)
//			return true;
//		if (spaziaturaEtichetteY != vecchio.spaziaturaEtichetteY)
//			return true;
//
//		// TODO: capire come mai si incarta qua (radio)
//		// if (unitaDiMisura!=vecchio.unitaDiMisura) return true;
//
//		// if (stampaDa!=vecchio.stampaDa) return true;
//
//		if (!((StampeUtil.concatena(presente)).equals(StampeUtil
//				.concatena(vecchio.presente))))
//			return true;
//
//		if (!((StampeUtil.concatena(concatena)).equals(StampeUtil
//				.concatena(vecchio.concatena))))
//			return true;
//
//		if (!((StampeUtil.concatena(grassetto)).equals(StampeUtil
//				.concatena(vecchio.grassetto))))
//			return true;
//
//		if (!((StampeUtil.concatena(corsivo)).equals(StampeUtil
//				.concatena(vecchio.corsivo))))
//			return true;
//
//		if (!((StampeUtil.concatena(verticale)).equals(StampeUtil
//				.concatena(vecchio.verticale))))
//			return true;
//
//		if (!((StampeUtil.concatena(dimensioneOrizzontale)).equals(StampeUtil
//				.concatena(vecchio.dimensioneOrizzontale))))
//			return true;
//
//		if (!((StampeUtil.concatena(dimensioneVerticale)).equals(StampeUtil
//				.concatena(vecchio.dimensioneVerticale))))
//			return true;
//
//		if (!((StampeUtil.concatena(x)).equals(StampeUtil.concatena(vecchio.x))))
//			return true;
//
//		if (!((StampeUtil.concatena(y)).equals(StampeUtil.concatena(vecchio.y))))
//			return true;
//
//		if (!((StampeUtil.concatena(font)).equals(StampeUtil
//				.concatena(vecchio.font))))
//			return true;
//
//		if (!((StampeUtil.concatena(punti)).equals(StampeUtil
//				.concatena(vecchio.punti))))
//			return true;
//
//		if (!((StampeUtil.concatena(posizione)).equals(StampeUtil
//				.concatena(vecchio.posizione))))
//			return true;
//
//		return false;
//	}
//

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		HttpSession session = request.getSession();

		if (modifica && request.getParameter("bordiEtichetta") == null)
			this.formattazioneModello.setBordiEtichetta(false);

//		if (!("ok".equals(session.getAttribute("conferma")))) {
//			azzeraCheckBox();
//		}
		//if (x != null) {
		//	ricaricaTabellaCampi();
		//}
	}

//	public void rimpiazzaValoriFormattazione(ModelliEtichetteGestioneForm caricato)
//			throws Exception {
//
//		bordiEtichetta = caricato.bordiEtichetta;
//		altezzaEtichetta = caricato.altezzaEtichetta;
//		larghezzaEtichetta = caricato.larghezzaEtichetta;
//
//		altezzaPagina = caricato.altezzaPagina;
//		larghezzaPagina = caricato.larghezzaPagina;
//
//		margineSup = caricato.margineSup;
//		margineSin = caricato.margineSin;
//		margineDes = caricato.margineDes;
//		margineInf = caricato.margineInf;
//
//		margineSupEtichetta = caricato.margineSupEtichetta;
//		margineSinEtichetta = caricato.margineSinEtichetta;
//		margineDesEtichetta = caricato.margineDesEtichetta;
//		margineInfEtichetta = caricato.margineInfEtichetta;
//
//		spaziaturaEtichetteX = caricato.spaziaturaEtichetteX;
//		spaziaturaEtichetteY = caricato.spaziaturaEtichetteY;
//
//		unitaDiMisura = caricato.unitaDiMisura;
//		stampaDa = caricato.stampaDa;
//
//		presente = caricato.presente.clone();
//		concatena = caricato.concatena.clone();
//		grassetto = caricato.grassetto.clone();
//		corsivo = caricato.corsivo.clone();
//		verticale = caricato.verticale.clone();
//
//		dimensioneOrizzontale = caricato.dimensioneOrizzontale.clone();
//		dimensioneVerticale = caricato.dimensioneVerticale.clone();
//
//		x = caricato.x.clone();
//		y = caricato.y.clone();
//		font = caricato.font.clone();
//		punti = caricato.punti.clone();
//		posizione = caricato.posizione.clone();
//
//		reportCompilato = caricato.reportCompilato.clone();
//	}
//
	//-----------------

	public FormattazioneVO copiaValoriFormattazione () throws Exception
	{
		FormattazioneVO destinazione= new FormattazioneVO();
		destinazione = this.getFormattazioneModello().copy();
//		destinazione.bordiEtichetta=bordiEtichetta;
//		destinazione.altezzaEtichetta=altezzaEtichetta;
//		destinazione.larghezzaEtichetta=larghezzaEtichetta;
//
//		destinazione.altezzaPagina=altezzaPagina;
//		destinazione.larghezzaPagina=larghezzaPagina;
//
//		destinazione.margineSup=margineSup;
//		destinazione.margineSin=margineSin;
//		destinazione.margineDes=margineDes;
//		destinazione.margineInf=margineInf;
//
//		destinazione.margineSupEtichetta=margineSupEtichetta;
//		destinazione.margineSinEtichetta=margineSinEtichetta;
//		destinazione.margineDesEtichetta=margineDesEtichetta;
//		destinazione.margineInfEtichetta=margineInfEtichetta;
//
//
//		destinazione.spaziaturaEtichetteX=spaziaturaEtichetteX;
//		destinazione.spaziaturaEtichetteY=spaziaturaEtichetteY;
//
//		destinazione.unitaDiMisura=unitaDiMisura;
//
//
//		if (presente!=null) destinazione.presente=presente.clone();
//		if (concatena!=null) destinazione.concatena=concatena.clone();
//		if (grassetto!=null) destinazione.grassetto=grassetto.clone();
//		if (corsivo!=null) destinazione.corsivo=corsivo.clone();
//		if (verticale!=null) destinazione.verticale=verticale.clone();
//		if (x!=null) destinazione.x=x.clone();
//		if (y!=null) destinazione.y=y.clone();
//		if (font!=null) destinazione.font=font.clone();
//		if (punti!=null) destinazione.punti=punti.clone();
//		if (posizione!=null) destinazione.posizione=posizione.clone();
//		if (dimensioneOrizzontale!=null) destinazione.dimensioneOrizzontale=dimensioneOrizzontale.clone();
//		if (dimensioneVerticale!=null) destinazione.dimensioneVerticale=dimensioneVerticale.clone();
//		if (reportCompilato!=null) destinazione.reportCompilato=reportCompilato.clone();
//
		return destinazione;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public ModelloEtichetteVO getRecModello() {
		return recModello;
	}

	public void setRecModello(ModelloEtichetteVO recModello) {
		this.recModello = recModello;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public boolean isEsamina() {
		return esamina;
	}

	public void setEsamina(boolean esamina) {
		this.esamina = esamina;
	}

	public boolean isSessione() {
		return sessione;
	}

	public void setSessione(boolean sessione) {
		this.sessione = sessione;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public boolean isConferma() {
		return conferma;
	}

	public void setConferma(boolean conferma) {
		this.conferma = conferma;
	}

	public boolean isSalva() {
		return salva;
	}

	public void setSalva(boolean salva) {
		this.salva = salva;
	}

	public boolean isDate() {
		return date;
	}

	public void setDate(boolean date) {
		this.date = date;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public float[] getDimensioneOrizzontale() {
		return dimensioneOrizzontale;
	}

	public void setDimensioneOrizzontale(float[] dimensioneOrizzontale) {
		this.dimensioneOrizzontale = dimensioneOrizzontale;
	}

	public float[] getDimensioneVerticale() {
		return dimensioneVerticale;
	}

	public void setDimensioneVerticale(float[] dimensioneVerticale) {
		this.dimensioneVerticale = dimensioneVerticale;
	}

	public String getElemBlocco() {
		return elemBlocco;
	}

	public void setElemBlocco(String elemBlocco) {
		this.elemBlocco = elemBlocco;
	}

	public String getEndInventario() {
		return endInventario;
	}

	public void setEndInventario(String endInventario) {
		this.endInventario = endInventario;
	}

	public String getFormatoEtichetta() {
		return formatoEtichetta;
	}

	public void setFormatoEtichetta(String formatoEtichetta) {
		this.formatoEtichetta = formatoEtichetta;
	}

	public List getListaPosizioni() {
		return listaPosizioni;
	}

	public void setListaPosizioni(List listaPosizioni) {
		this.listaPosizioni = listaPosizioni;
	}

	public List getListaSerie() {
		return listaSerie;
	}

	public void setListaSerie(List listaSerie) {
		this.listaSerie = listaSerie;
	}

	public boolean isModifica() {
		return modifica;
	}

	public void setModifica(boolean modifica) {
		this.modifica = modifica;
	}

	public void setReportCompilato(byte[] reportCompilato) {
		this.reportCompilato = reportCompilato;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public int getStampaDa() {
		return stampaDa;
	}

	public void setStampaDa(int stampaDa) {
		this.stampaDa = stampaDa;
	}

	public String getStartInventario() {
		return startInventario;
	}

	public void setStartInventario(String startInventario) {
		this.startInventario = startInventario;
	}

	public String[] getConcatena() {
		return concatena;
	}

	public void setConcatena(String[] concatena) {
		this.concatena = concatena;
	}

	public List getListaFont() {
		return listaFont;
	}

	public void setListaFont(List listaFont) {
		this.listaFont = listaFont;
	}

	public List getListaModelli() {
		return listaModelli;
	}

	public void setListaModelli(List listaModelli) {
		this.listaModelli = listaModelli;
	}

	public List getListaPunti() {
		return listaPunti;
	}

	public void setListaPunti(List listaPunti) {
		this.listaPunti = listaPunti;
	}

	public String getDescrModello() {
		return descrModello;
	}

	public void setDescrModello(String descrModello) {
		this.descrModello = descrModello;
	}

	public boolean isDisableCodModello() {
		return disableCodModello;
	}

	public void setDisableCodModello(boolean disableCodModello) {
		this.disableCodModello = disableCodModello;
	}

	public boolean isDisableBib() {
		return disableBib;
	}

	public void setDisableBib(boolean disableBib) {
		this.disableBib = disableBib;
	}

	public FormattazioneVO getFormattazioneModello() {
		return formattazioneModello;
	}

	public void setFormattazioneModello(FormattazioneVO formattazioneModello) {
		this.formattazioneModello = formattazioneModello;
	}

	public FormattazioneVOCampoEtichetta getRigaEtichetta(int index) {

        // automatically grow List size
        List<FormattazioneVOCampoEtichetta> campiEtichetta = this.formattazioneModello.getCampiEtichetta();
		while (index >= campiEtichetta.size()) {
			campiEtichetta.add(new FormattazioneVOCampoEtichetta());
        }
        return campiEtichetta.get(index);
    }

	public FormattazioneVOImmagini getRigaImmagine(int index) {

        // automatically grow List size
        List<FormattazioneVOImmagini> campiImmagine = this.formattazioneModello.getCampiImmagine();
		while (index >= campiImmagine.size()) {
			campiImmagine.add(new FormattazioneVOImmagini());
        }
        return campiImmagine.get(index);
    }
	public byte[] getReportCompilato() {
		return reportCompilato;
	}

	public boolean isDisableDescrBib() {
		return disableDescrBib;
	}

	public void setDisableDescrBib(boolean disableDescrBib) {
		this.disableDescrBib = disableDescrBib;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodModello() {
		return codModello;
	}

	public void setCodModello(String codModello) {
		this.codModello = codModello;
	}

	public String getDescrBib() {
		return descrBib;
	}

	public void setDescrBib(String descrBib) {
		this.descrBib = descrBib;
	}

	public String getDescrBibEtichetta() {
		return descrBibEtichetta;
	}

	public void setDescrBibEtichetta(String descrBibEtichetta) {
		this.descrBibEtichetta = descrBibEtichetta;
	}

}
