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
package it.iccu.sbn.ejb.vo.gestionebibliografica.marca;

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiDettaglioOggettiVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.DatiLegame;
import it.iccu.sbn.web.constant.TitoliCollegatiInvoke;
import it.iccu.sbn.web.vo.TreeElementView;

public class TreeElementViewMarche extends TreeElementView {

    /**
	 *
	 */
	private static final long serialVersionUID = 3469456309784964465L;

	private SbnAuthority tipoAuthority = null;

    private String livelloAutorita = null;

    private DatiLegame datiLegame;

    AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO = new AreaDatiDettaglioOggettiVO();


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

    @Override
	public SbnAuthority getTipoAuthority() {
        return tipoAuthority;
    }

    @Override
	public void setTipoAuthority(SbnAuthority tipoAuthority) {
        this.tipoAuthority = tipoAuthority;
    }

    @Override
	public String getImageStyle() {
        String imageStyle = null;

		if (getRigaReticoloCtr() == 1) {
			if (getLocalizzazione() == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE) {
				imageStyle = "marcaReticoloInd";
			} else {
				imageStyle = "marcaReticoloLoc";
			}
		} else {
			if (getTipoAuthority() == null || getTipoAuthority().toString().equals("TU") || getTipoAuthority().toString().equals("UM")) {
				if (getLocalizzazione() == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE) {
					imageStyle = "titoloInd";
				} else if (getLocalizzazione() == TitoliCollegatiInvoke.LIV_DI_RICERCA_POLO) {
					imageStyle = "titoloPol";
				} else if (getLocalizzazione() == TitoliCollegatiInvoke.LIV_DI_RICERCA_BIBLIO
						|| getLocalizzazione() == TitoliCollegatiInvoke.LOCAL_BIB_COMPLETA
						|| getLocalizzazione() == TitoliCollegatiInvoke.LOCAL_BIB_GESTIONE
						|| getLocalizzazione() == TitoliCollegatiInvoke.LOCAL_BIB_POSSESSO) {
					imageStyle = "titoloBib";
				}
			} else if (getTipoAuthority().toString().equals("AU")) {
				if (getLocalizzazione() == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE) {
					imageStyle = "formaAccettataInd";
				} else {
					imageStyle = "formaAccettataLoc";
				}
			} else if (getTipoAuthority().toString().equals("SO")) {
				imageStyle = "soggettoReticolo";
			} else if (getTipoAuthority().toString().equals("DE")) {
				imageStyle = "descrittoreReticolo";
			} else if (getTipoAuthority().toString().equals("LU")) {
				imageStyle = "luogoReticolo";
			} else if (getTipoAuthority().toString().equals("CL")) {
				imageStyle = "classeReticolo";
			} else if (getTipoAuthority().toString().equals("MA")) {
				if (getLocalizzazione() == TitoliCollegatiInvoke.LIV_DI_RICERCA_INDICE) {
					imageStyle = "marcaReticoloInd";
				} else {
					imageStyle = "marcaReticoloLoc";
				}
			} else if (getTipoAuthority().toString().equals("RE")) {
				imageStyle = "none";
			}
		}
		return imageStyle;
	    }


    @Override
	public boolean isRadioVisible() {
        return ("AU".equals(getTipoAuthority()));
    }
    @Override
	public boolean isCheckVisible() {
        return (!"AU".equals(getTipoAuthority()));
    }

	public AreaDatiDettaglioOggettiVO getAreaDatiDettaglioOggettiVO() {
		return areaDatiDettaglioOggettiVO;
	}

	public void setAreaDatiDettaglioOggettiVO(
			AreaDatiDettaglioOggettiVO areaDatiDettaglioOggettiVO) {
		this.areaDatiDettaglioOggettiVO = areaDatiDettaglioOggettiVO;
	}

}
