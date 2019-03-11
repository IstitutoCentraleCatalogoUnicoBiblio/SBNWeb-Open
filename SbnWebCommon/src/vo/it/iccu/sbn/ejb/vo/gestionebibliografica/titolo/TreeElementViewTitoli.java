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
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.semantica.ClassiUtil;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiDettaglioOggettiVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.DatiLegame;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SimboloDeweyVO;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.vo.TreeElementView;

public class TreeElementViewTitoli extends TreeElementView {

	private static final int NO_AUTH = -1;

	private static final long serialVersionUID = 6846240566371166373L;

	private String natura;
	private String tipoMateriale = null;
	private boolean autoreFormaRinvio = false;
	private boolean luogoFormaRinvio = false;
	private boolean possessoreFormaRinvio = false;
	private String livelloAutorita = null;
	private DatiLegame datiLegame;
	AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO = new AreaDatiDettaglioOggettiVO();

	// Inizio intervento almaviva2/ almaviva5 01.04.2011 - Modifica relativa al nuovo
	// protocollo (nuovo campo prioritàPoli il quale conterrà delle stringhe così composte:
	//	<prioritaPoli>S - BVE</prioritaPoli>
	//	<prioritaPoli>N - CSW</prioritaPoli>
	// Dove Si e No rappresentano la possibilità di “sostituibilità del legame” mentre dopo il trattino viene indicato
	// come informazione aggiuntiva il polo “proprietario”.
	private boolean proprietarioSoggettazioneIndice = true;
	private String poloSoggettazioneIndice = "";

	// Inizio intervento almaviva2/ almaviva5 27.07.2011 BUG MANTIS 4583 (classi e soggetti vanno trattate separatamente
	private boolean proprietarioClassificazioneIndice = true;
	private String poloClassificazioneIndice = "";


	public boolean isAutoreFormaRinvio() {
		return autoreFormaRinvio;
	}

	public void setAutoreFormaRinvio(boolean autoreFormaRinvio) {
		this.autoreFormaRinvio = autoreFormaRinvio;
	}

	public DatiLegame getDatiLegame() {
		return datiLegame;
	}

	public void setDatiLegame(DatiLegame datiLegame) {
		this.datiLegame = datiLegame;
	}

	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	public void setLivelloAutorita(String livelloAutorita) {
		this.livelloAutorita = livelloAutorita;
	}

	public String getNatura() {
		return natura;
	}

	public void setNatura(String natura) {
		this.natura = natura;
	}

	public String getTipoMateriale() {
		return tipoMateriale;
	}

	public void setTipoMateriale(String tipoMateriale) {
		this.tipoMateriale = tipoMateriale;
	}

	@Override
	public String getImageStyle() {
		String imageStyle = null;

		int localizzazione = getLocalizzazione();
		SbnAuthority tipoAuthority = getTipoAuthority();
		int type = tipoAuthority != null ? tipoAuthority.getType() : NO_AUTH;
		switch (type) {
		//titolo / titolo uniforme
		case NO_AUTH:
		case SbnAuthority.UM_TYPE:
		case SbnAuthority.TU_TYPE:
			if (localizzazione == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE) {
				imageStyle = "titoloInd";
			} else if (localizzazione == TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO
					|| localizzazione == TitoliCollegatiInvoke.LOCAL_BIB_GESTIONE
					|| localizzazione == TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO) {
				imageStyle = "titoloPol";
			} else if (localizzazione == TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA) {
				imageStyle = "titoloBib";
			} else if (localizzazione == TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO) {
				imageStyle = "titoloIndPos";
			} else if (localizzazione == 0) {
				imageStyle = "titoloReticolo";
			}
			break;

		//autore
		case SbnAuthority.AU_TYPE:
			if (!isAutoreFormaRinvio()) {
				if (localizzazione == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE
						|| localizzazione == 0) {
					imageStyle = "formaAccettataInd";
				} else {
					imageStyle = "formaAccettataLoc";
				}
			} else {
				imageStyle = "formaRinvio";
			}
			break;

		//possessore
		case SbnAuthority.PP_TYPE:
			if (!isPossessoreFormaRinvio()) {
				if (localizzazione == TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO
						|| localizzazione == 0) {
					imageStyle = "formaAccettataInd";
				} else {
					imageStyle = "formaAccettataLoc";
				}
			} else {
				imageStyle = "formaRinvio";
			}
			break;

		//soggetto
		case SbnAuthority.SO_TYPE:
			imageStyle = "soggettoReticolo";
			break;

		//descrittore
		case SbnAuthority.DE_TYPE:
			imageStyle = "descrittoreReticolo";
			break;

		//classe
		case SbnAuthority.CL_TYPE:
			imageStyle = "classeReticolo";
			break;

		//termine thesauro
		case SbnAuthority.TH_TYPE:
			imageStyle = "descrittoreReticolo";
			break;

		//luogo
		case SbnAuthority.LU_TYPE:
			if (!isLuogoFormaRinvio()) {
				if (localizzazione == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE
						|| localizzazione == 0) {
					imageStyle = "luogoReticoloInd";
				} else {
					imageStyle = "luogoReticoloLoc";
				}
			} else {
				imageStyle = "formaRinvio";
			}
			break;

		//marca
		case SbnAuthority.MA_TYPE:
			if (localizzazione == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE
					|| localizzazione == 0) {
				imageStyle = "marcaReticoloInd";
			} else {
				imageStyle = "marcaReticoloLoc";
			}
			break;

		//repertorio
		case SbnAuthority.RE_TYPE:
			imageStyle = "none";
			break;

		}

		return imageStyle;
	}

	@Override
	public boolean isRadioVisible() {
		return true;
	}

	@Override
	public boolean isCheckVisible() {
		// Aprile 2015 almaviva2: EVOLUTIVA DISCOTECA DI STATO:
		// Il trascinamento legami autore ma M a N/W (esempio disco M con tracce N) deve essere esteso anche ai legami 500
		// cioè ai titoli Uniformi sia natura A e B ed alle nature T (che sono di tipo documento)
		// Maggio 2015 almaviva2: EVOLUTIVA DISCOTECA DI STATO:
		// Il quadratino viene prospettato, per legami diversi da 463/464, solo nel caso in cui l'oggetto sia localizzato per
		// il Polo perchè solo in questo caso si puo richiedre il trascinamento legame Autore;

		// almaviva2 Evolutiva Maggio 2017: cattura soggetti/classi da indice tramite funzione di "Vai a"--> cattura;
		// la cattura avviene solo per le authority selzionate con il check (come per le W)
		if (getDatiLegame() != null) {
			String tipoLegame = getDatiLegame().getTipoLegame();
			if (tipoLegame.equals("463")
					|| tipoLegame.equals("464")) {
				return true;
			} else if (getNodeLevel() == 1 && (tipoLegame.equals("600") || tipoLegame.equals("601") || tipoLegame.equals("602")
					|| tipoLegame.equals("604")	|| tipoLegame.equals("605") || tipoLegame.equals("606") || tipoLegame.equals("607")
					|| tipoLegame.equals("610"))) {
				// legami a SOGGETTI
				return true;
			} else if (getNodeLevel() == 1 && (tipoLegame.equals("675") || tipoLegame.equals("676") || tipoLegame.equals("686"))) {
				// legami a CLASSI
				return true;
			} else {
				int localizzazione = getLocalizzazione();
				if (tipoLegame.equals("500") &&
						localizzazione != TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE) { // Legame a TITOLI UNIFORMI (natura A)
					return true;
				} else	if (tipoLegame.equals("454") &&
						localizzazione != TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE) { // Legame a TITOLI UNIFORMI (natura B)
					return true;
				} else	if (tipoLegame.equals("423") &&
						localizzazione != TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE) { // Legame a TITOLO ACCESSO (natura T)
					return true;
				}
			}


		}
		return false;
	}

	public AreaDatiDettaglioOggettiVO getAreaDatiDettaglioOggettiVO() {
		return areaDatiDettaglioOggettiVO;
	}

	public void setAreaDatiDettaglioOggettiVO(
			AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO) {
		this.areaDatiDettaglioOggettiVO = areaDatiDettaglioOggettiVO;
	}

	public boolean isLuogoFormaRinvio() {
		return luogoFormaRinvio;
	}

	public void setLuogoFormaRinvio(boolean luogoFormaRinvio) {
		this.luogoFormaRinvio = luogoFormaRinvio;
	}

	public boolean isPossessoreFormaRinvio() {
		return possessoreFormaRinvio;
	}

	public void setPossessoreFormaRinvio(boolean possessoreFormaRinvio) {
		this.possessoreFormaRinvio = possessoreFormaRinvio;
	}

	@Override
	public String getDecoratedKey() {

		String key = super.getKey();
		SbnAuthority tipoAut = getTipoAuthority();
		if (ValidazioneDati.eqAuthority(tipoAut, SbnAuthority.CL) && ClassiUtil.isT001Dewey(key)) {
			SimboloDeweyVO sd;
			try {
				sd = SimboloDeweyVO.parse(key);
			} catch (ValidationException e) {
				return "";
			}
			StringBuilder buf = new StringBuilder(32);
			buf.append(sd.getSistema());
			buf.append(" ");
			buf.append(sd.getEdizione());
			buf.append(" ");
			buf.append(sd.getSimbolo());
			key = buf.toString();
		}

		return key;
	}

	public boolean isProprietarioSoggettazioneIndice() {
		return proprietarioSoggettazioneIndice;
	}

	public void setProprietarioSoggettazioneIndice(
			boolean proprietarioSoggettazioneIndice) {
		this.proprietarioSoggettazioneIndice = proprietarioSoggettazioneIndice;
	}

	public String getPoloSoggettazioneIndice() {
		return poloSoggettazioneIndice;
	}

	public void setPoloSoggettazioneIndice(String poloSoggettazioneIndice) {
		this.poloSoggettazioneIndice = poloSoggettazioneIndice;
	}

	public boolean isProprietarioClassificazioneIndice() {
		return proprietarioClassificazioneIndice;
	}

	public void setProprietarioClassificazioneIndice(
			boolean proprietarioClassificazioneIndice) {
		this.proprietarioClassificazioneIndice = proprietarioClassificazioneIndice;
	}

	public String getPoloClassificazioneIndice() {
		return poloClassificazioneIndice;
	}

	public void setPoloClassificazioneIndice(String poloClassificazioneIndice) {
		this.poloClassificazioneIndice = poloClassificazioneIndice;
	}



}
