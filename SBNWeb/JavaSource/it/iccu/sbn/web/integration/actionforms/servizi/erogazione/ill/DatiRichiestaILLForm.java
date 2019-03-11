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
package it.iccu.sbn.web.integration.actionforms.servizi.erogazione.ill;

import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.servizi.documenti.DocumentoNonSbnVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.MessaggioVO;
import it.iccu.sbn.vo.custom.servizi.ill.MessaggioVODecorator;
import it.iccu.sbn.web.actionforms.servizi.documenti.GestioneDocumentoForm;

import java.util.List;

public class DatiRichiestaILLForm extends GestioneDocumentoForm {


	private static final long serialVersionUID = -7970343987585222274L;
	private MovimentoVO movimento;
	private DatiRichiestaILLVO dati;
	private MessaggioVO messaggio;

	private List<ComboCodDescVO> listaProvincia;

	private String[] pulsanti;
	private String[] pulsantiInventario;

	private List<TB_CODICI> tipiSupportoILL;
	private List<TB_CODICI> modoErogazioneILL;

	private ServizioBibliotecaVO servizioLocale = new ServizioBibliotecaVO();
	private List<ServizioBibliotecaVO> serviziLocali;

	public MovimentoVO getMovimento() {
		return movimento;
	}

	public void setMovimento(MovimentoVO movimento) {
		this.movimento = movimento;
	}

	public DatiRichiestaILLVO getDati() {
		return dati;
	}

	public void setDati(DatiRichiestaILLVO dati) {
		this.dati = dati;
	}

	public String[] getPulsanti() {
		return pulsanti;
	}

	public void setPulsanti(String[] pulsanti) {
		this.pulsanti = pulsanti;
	}

	public String[] getPulsantiInventario() {
		return pulsantiInventario;
	}

	public void setPulsantiInventario(String[] pulsantiInventario) {
		this.pulsantiInventario = pulsantiInventario;
	}

	public List<ComboCodDescVO> getListaProvincia() {
		return listaProvincia;
	}

	public void setListaProvincia(List<ComboCodDescVO> listaProvincia) {
		this.listaProvincia = listaProvincia;
	}

	public MessaggioVO getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(MessaggioVO msg) throws Exception {
		this.messaggio = (msg != null) ? new MessaggioVODecorator(msg) : null;
	}

	public List<TB_CODICI> getTipiSupportoILL() {
		return tipiSupportoILL;
	}

	public void setTipiSupportoILL(List<TB_CODICI> tipiSupportoILL) {
		this.tipiSupportoILL = tipiSupportoILL;
	}

	public List<TB_CODICI> getModoErogazioneILL() {
		return modoErogazioneILL;
	}

	public void setModoErogazioneILL(List<TB_CODICI> modoErogazioneILL) {
		this.modoErogazioneILL = modoErogazioneILL;
	}

	public ServizioBibliotecaVO getServizioLocale() {
		return servizioLocale;
	}

	public void setServizioLocale(ServizioBibliotecaVO servizioLocale) {
		this.servizioLocale = servizioLocale;
	}

	public List<ServizioBibliotecaVO> getServiziLocali() {
		return serviziLocali;
	}

	public void setServiziLocali(List<ServizioBibliotecaVO> serviziLocali) {
		this.serviziLocali = serviziLocali;
	}

	@Override
	public DocumentoNonSbnVO getDettaglio() {
		return dati.getDocumento();
	}

}
