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
package it.iccu.sbn.web.actionforms.acquisizioni.comunicazioni;

import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppComunicazioneVO;
import it.iccu.sbn.web.actionforms.acquisizioni.AcquisizioniBaseFormIntf;

import java.util.List;

public class EsaminaComForm extends ComunicazioneBaseForm implements AcquisizioniBaseFormIntf {


	private static final long serialVersionUID = 7616584616851687236L;
	private List<ListaSuppComunicazioneVO> listaDaScorrere;
	private int posizioneScorrimento;
	private boolean enableScorrimento = false;
	private boolean esaminaInibito = false;

	public List<ListaSuppComunicazioneVO> getListaDaScorrere() {
		return listaDaScorrere;
	}

	public void setListaDaScorrere(
			List<ListaSuppComunicazioneVO> listaDaScorrere) {
		this.listaDaScorrere = listaDaScorrere;
	}

	public int getPosizioneScorrimento() {
		return posizioneScorrimento;
	}

	public void setPosizioneScorrimento(int posizioneScorrimento) {
		this.posizioneScorrimento = posizioneScorrimento;
	}

	public boolean isEnableScorrimento() {
		return enableScorrimento;
	}

	public void setEnableScorrimento(boolean enableScorrimento) {
		this.enableScorrimento = enableScorrimento;
	}

	public boolean isEsaminaInibito() {
		return esaminaInibito;
	}

	public void setEsaminaInibito(boolean esaminaInibito) {
		this.esaminaInibito = esaminaInibito;
	}

}
