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
package it.iccu.sbn.ejb.vo.gestionestampe.etichette;

import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.ArrayList;
import java.util.List;

public class FormattazioneVO extends SerializableVO {

	private static final long serialVersionUID = 224501076534892116L;

	private String nomeModello;
	// -----------------
	private float larghezzaPagina;
	private float altezzaPagina;
	private float margineSup;
	private float margineSin;
	private float margineDes;
	private float margineInf;
	private String unitaDiMisura;
	// -----------------
	private boolean bordiEtichetta;
	private float altezzaEtichetta;
	private float larghezzaEtichetta;
	private float margineSupEtichetta;
	private float margineSinEtichetta;
	private float margineDesEtichetta;
	private float margineInfEtichetta;
	private float spaziaturaEtichetteX;
	private float spaziaturaEtichetteY;
	// -----------------
	private List<FormattazioneVOCampoEtichetta> campiEtichetta;
	// -----------------
	private List<FormattazioneVOImmagini> campiImmagine;

	public FormattazioneVO() {
		this.campiEtichetta = new ArrayList<FormattazioneVOCampoEtichetta>();
		this.campiImmagine = new ArrayList<FormattazioneVOImmagini>();
	}

	/**
	 * trasforma tutti i campi di questa classe che definiscono il modello di
	 * stampa in una stringa che potrà poi essere scritta sul db String
	 *
	 * @return
	 */
	public String esportaModello() {
		String modello = "";

		modello += "|altezzaPagina::" + this.altezzaPagina;
		modello += "|larghezzaPagina::" + this.larghezzaPagina;
		modello += "|margineSup::" + this.margineSup;
		modello += "|margineSin::" + this.margineSin;
		modello += "|margineDes::" + this.margineDes;
		modello += "|margineInf::" + this.margineInf;
		modello += "|unitaDiMisura::" + this.unitaDiMisura;
		modello += "|bordiEtichetta::"
				+ (this.bordiEtichetta ? "true" : "false");
		modello += "|altezzaEtichetta::" + this.altezzaEtichetta;
		modello += "|larghezzaEtichetta::" + this.larghezzaEtichetta;
		modello += "|margineSupEtichetta::" + this.margineSupEtichetta;
		modello += "|margineSinEtichetta::" + this.margineSinEtichetta;
		modello += "|margineDesEtichetta::" + this.margineDesEtichetta;
		modello += "|margineInfEtichetta::" + this.margineInfEtichetta;
		modello += "|spaziaturaEtichetteX::" + this.spaziaturaEtichetteX;
		modello += "|spaziaturaEtichetteY::" + this.spaziaturaEtichetteY;

		for (int i = 0; i < campiEtichetta.size(); i++) {
			FormattazioneVOCampoEtichetta campoCorrente = campiEtichetta.get(i);
			modello += "|campiEtichetta(" + i + "):nomeCampo:"
					+ campoCorrente.getNomeCampo();
			modello += "|campiEtichetta(" + i + "):Presente:"
					+ (campoCorrente.isPresente() ? "true" : "false");
			modello += "|campiEtichetta(" + i + "):Concatena:"
					+ (campoCorrente.isConcatena() ? "true" : "false");
			modello += "|campiEtichetta(" + i + "):X:" + campoCorrente.getX();
			modello += "|campiEtichetta(" + i + "):Y:" + campoCorrente.getY();
			modello += "|campiEtichetta(" + i + "):Verticale:"
					+ (campoCorrente.isVerticale() ? "true" : "false");
			modello += "|campiEtichetta(" + i + "):Font:"
					+ campoCorrente.getFont();
			modello += "|campiEtichetta(" + i + "):Punti:"
					+ campoCorrente.getPunti();
			modello += "|campiEtichetta(" + i + "):Grassetto:"
					+ (campoCorrente.isGrassetto() ? "true" : "false");
			modello += "|campiEtichetta(" + i + "):Corsivo:"
					+ (campoCorrente.isCorsivo() ? "true" : "false");
			modello += "|campiEtichetta(" + i + "):Posizione:"
					+ campoCorrente.getPosizione();
		}
		for (int i = 0; i < campiImmagine.size(); i++) {
			FormattazioneVOImmagini campoCorrente = campiImmagine.get(i);
			modello += "|campiImmagine(" + i + "):NomeImmagine:"
					+ campoCorrente.getNomeImmagine();
			modello += "|campiImmagine(" + i + "):Presente:"
					+ (campoCorrente.isPresente() ? "true" : "false");
			modello += "|campiImmagine(" + i + "):Verticale:"
					+ (campoCorrente.isVerticale() ? "true" : "false");
			modello += "|campiImmagine(" + i + "):X:" + campoCorrente.getX();
			modello += "|campiImmagine(" + i + "):Y:" + campoCorrente.getY();
			modello += "|campiImmagine(" + i + "):DimensioneOrizzontale:"
					+ campoCorrente.getDimensioneOrizzontale();
			modello += "|campiImmagine(" + i + "):DimensioneVerticale:"
					+ campoCorrente.getDimensioneVerticale();
		}
		modello += "|";
		return modello;
	}

	public void importaModello(String modello) {
		int l1, l2;
		String campoSer;
		l1 = 0;
		l2 = modello.indexOf("|", l1 + 1);
		do {
			campoSer = modello.substring(l1 + 1, l2);
			importaCampo(campoSer);
			l1 = modello.indexOf("|", l1 + 1);
			l2 = modello.indexOf("|", l1 + 1);
		} while (l2 > 0);
	}

	private void importaCampo(String campoSer) {
		// ricevo un campo sottoforma di stringa e imposto il relativo valore

		// due formati:
		// nomecampo::valore
		// nomelista(i):nomecampo:valore
		int l1, l2, i;
		String f1, f2, f3;
		l1 = campoSer.indexOf(":", 1);
		l2 = campoSer.indexOf(":", l1 + 1);
		f1 = campoSer.substring(0, l1);
		f2 = campoSer.substring(l1 + 1, l2);
		f3 = campoSer.substring(l2 + 1);
		if (f1.equals("bordiEtichetta")) {
			this.setBordiEtichetta(f3.equals("true"));
		} else if (f1.equals("altezzaEtichetta")) {
			this.setAltezzaEtichetta(new Float(f3).floatValue());
		} else if (f1.equals("larghezzaEtichetta")) {
			this.setLarghezzaEtichetta(new Float(f3).floatValue());
		} else if (f1.equals("altezzaPagina")) {
			this.setAltezzaPagina(new Float(f3).floatValue());
		} else if (f1.equals("larghezzaPagina")) {
			this.setLarghezzaPagina(new Float(f3).floatValue());
		} else if (f1.equals("margineSup")) {
			this.setMargineSup(new Float(f3).floatValue());
		} else if (f1.equals("margineSin")) {
			this.setMargineSin(new Float(f3).floatValue());
		} else if (f1.equals("margineDes")) {
			this.setMargineDes(new Float(f3).floatValue());
		} else if (f1.equals("margineInf")) {
			this.setMargineInf(new Float(f3).floatValue());
		} else if (f1.equals("unitaDiMisura")) {
			this.setUnitaDiMisura(f3);
		} else if (f1.equals("margineSupEtichetta")) {
			this.setMargineSupEtichetta(new Float(f3).floatValue());
		} else if (f1.equals("margineSinEtichetta")) {
			this.setMargineSinEtichetta(new Float(f3).floatValue());
		} else if (f1.equals("margineDesEtichetta")) {
			this.setMargineDesEtichetta(new Float(f3).floatValue());
		} else if (f1.equals("margineInfEtichetta")) {
			this.setMargineInfEtichetta(new Float(f3).floatValue());
		} else if (f1.equals("spaziaturaEtichetteX")) {
			this.setSpaziaturaEtichetteX(new Float(f3).floatValue());
		} else if (f1.equals("spaziaturaEtichetteY")) {
			this.setSpaziaturaEtichetteY(new Float(f3).floatValue());
		} else if (f1.startsWith("campiEtichetta(")) {
			l1 = f1.indexOf("(");
			l2 = f1.indexOf(")");
			i = new Integer(f1.substring(l1 + 1, l2)).intValue();
			// verifico se l'elemento esiste e se non esiste lo creo
			if (this.campiEtichetta.size() <= i) {
				FormattazioneVOCampoEtichetta campo = new FormattazioneVOCampoEtichetta();
				this.campiEtichetta.add(campo);
			}
			if (f2.equals("nomeCampo")) {
				this.campiEtichetta.get(i).setNomeCampo(f3);
			} else if (f2.equals("Font")) {
				this.campiEtichetta.get(i).setFont(f3);
			} else if (f2.equals("Posizione")) {
				this.campiEtichetta.get(i).setPosizione(f3);
			} else if (f2.equals("Punti")) {
				this.campiEtichetta.get(i).setPunti(f3);
			} else if (f2.equals("X")) {
				this.campiEtichetta.get(i).setX(new Float(f3).floatValue());
			} else if (f2.equals("Y")) {
				this.campiEtichetta.get(i).setY(new Float(f3).floatValue());
			} else if (f2.equals("Concatena")) {
				this.campiEtichetta.get(i).setConcatena(f3.equals("true"));
			} else if (f2.equals("Corsivo")) {
				this.campiEtichetta.get(i).setCorsivo(f3.equals("true"));
			} else if (f2.equals("Grassetto")) {
				this.campiEtichetta.get(i).setGrassetto(f3.equals("true"));
			} else if (f2.equals("Presente")) {
				this.campiEtichetta.get(i).setPresente(f3.equals("true"));
			} else if (f2.equals("Verticale")) {
				this.campiEtichetta.get(i).setVerticale(f3.equals("true"));
			}
		} else if (f1.startsWith("campiImmagine(")) {
			l1 = f1.indexOf("(");
			l2 = f1.indexOf(")");
			i = new Integer(f1.substring(l1 + 1, l2)).intValue();
			// verifico se l'elemento esiste e se non esiste lo creo
			if (this.campiImmagine.size() <= i) {
				FormattazioneVOImmagini campo = new FormattazioneVOImmagini();
				this.campiImmagine.add(campo);
			}
			if (f2.equals("NomeImmagine")) {
				this.campiImmagine.get(i).setNomeImmagine(f3);
			} else if (f2.equals("DimensioneOrizzontale")) {
				this.campiImmagine.get(i).setDimensioneOrizzontale(
						new Float(f3).floatValue());
			} else if (f2.equals("DimensioneVerticale")) {
				this.campiImmagine.get(i).setDimensioneVerticale(
						new Float(f3).floatValue());
			} else if (f2.equals("X")) {
				this.campiImmagine.get(i).setX(new Float(f3).floatValue());
			} else if (f2.equals("Y")) {
				this.campiImmagine.get(i).setY(new Float(f3).floatValue());
			} else if (f2.equals("Presente")) {
				this.campiImmagine.get(i).setPresente(f3.equals("true"));
			} else if (f2.equals("Verticale")) {
				this.campiImmagine.get(i).setVerticale(f3.equals("true"));
			}
		}
	}

	public float getAltezzaEtichetta() {
		return altezzaEtichetta;
	}

	public void setAltezzaEtichetta(float altezzaEtichetta) {
		this.altezzaEtichetta = altezzaEtichetta;
	}

	public float getAltezzaPagina() {
		return altezzaPagina;
	}

	public void setAltezzaPagina(float altezzaPagina) {
		this.altezzaPagina = altezzaPagina;
	}

	public boolean isBordiEtichetta() {
		return bordiEtichetta;
	}

	public void setBordiEtichetta(boolean bordiEtichetta) {
		this.bordiEtichetta = bordiEtichetta;
	}

	public List<FormattazioneVOCampoEtichetta> getCampiEtichetta() {
		return campiEtichetta;
	}

	public void setCampiEtichetta(
			List<FormattazioneVOCampoEtichetta> campiEtichetta) {
		this.campiEtichetta = campiEtichetta;
	}

	public List<FormattazioneVOImmagini> getCampiImmagine() {
		return campiImmagine;
	}

	public void setCampiImmagine(List<FormattazioneVOImmagini> campiImmagine) {
		this.campiImmagine = campiImmagine;
	}

	public float getLarghezzaEtichetta() {
		return larghezzaEtichetta;
	}

	public void setLarghezzaEtichetta(float larghezzaEtichetta) {
		this.larghezzaEtichetta = larghezzaEtichetta;
	}

	public float getLarghezzaPagina() {
		return larghezzaPagina;
	}

	public void setLarghezzaPagina(float larghezzaPagina) {
		this.larghezzaPagina = larghezzaPagina;
	}

	public float getMargineDes() {
		return margineDes;
	}

	public void setMargineDes(float margineDes) {
		this.margineDes = margineDes;
	}

	public float getMargineDesEtichetta() {
		return margineDesEtichetta;
	}

	public void setMargineDesEtichetta(float margineDesEtichetta) {
		this.margineDesEtichetta = margineDesEtichetta;
	}

	public float getMargineInf() {
		return margineInf;
	}

	public void setMargineInf(float margineInf) {
		this.margineInf = margineInf;
	}

	public float getMargineInfEtichetta() {
		return margineInfEtichetta;
	}

	public void setMargineInfEtichetta(float margineInfEtichetta) {
		this.margineInfEtichetta = margineInfEtichetta;
	}

	public float getMargineSin() {
		return margineSin;
	}

	public void setMargineSin(float margineSin) {
		this.margineSin = margineSin;
	}

	public float getMargineSinEtichetta() {
		return margineSinEtichetta;
	}

	public void setMargineSinEtichetta(float margineSinEtichetta) {
		this.margineSinEtichetta = margineSinEtichetta;
	}

	public float getMargineSup() {
		return margineSup;
	}

	public void setMargineSup(float margineSup) {
		this.margineSup = margineSup;
	}

	public float getMargineSupEtichetta() {
		return margineSupEtichetta;
	}

	public void setMargineSupEtichetta(float margineSupEtichetta) {
		this.margineSupEtichetta = margineSupEtichetta;
	}

	public String getNomeModello() {
		return nomeModello;
	}

	public void setNomeModello(String nomeModello) {
		this.nomeModello = nomeModello;
	}

	public float getSpaziaturaEtichetteX() {
		return spaziaturaEtichetteX;
	}

	public void setSpaziaturaEtichetteX(float spaziaturaEtichetteX) {
		this.spaziaturaEtichetteX = spaziaturaEtichetteX;
	}

	public float getSpaziaturaEtichetteY() {
		return spaziaturaEtichetteY;
	}

	public void setSpaziaturaEtichetteY(float spaziaturaEtichetteY) {
		this.spaziaturaEtichetteY = spaziaturaEtichetteY;
	}

	public String getUnitaDiMisura() {
		return unitaDiMisura;
	}

	public void setUnitaDiMisura(String unitaDiMisura) {
		this.unitaDiMisura = unitaDiMisura;
	}

}
