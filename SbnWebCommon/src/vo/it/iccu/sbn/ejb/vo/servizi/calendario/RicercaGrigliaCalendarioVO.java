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
package it.iccu.sbn.ejb.vo.servizi.calendario;

import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;

import java.util.ArrayList;
import java.util.List;

public class RicercaGrigliaCalendarioVO extends CalendarioVO {

	private static final long serialVersionUID = -6544957888020087260L;

	private ServizioBibliotecaVO servizio;
	private List<String> cd_cat_mediazione = new ArrayList<String>();
	private UtenteBaseVO utente = new UtenteBaseVO();
	private InventarioVO inventario = new InventarioVO();

	//indicazione di inserimento remoto da parte dell'utente
	private boolean remoto;

	private List<PrenotazionePostoVO> prenotazioniEscluse = new ArrayList<PrenotazionePostoVO>();

	public ServizioBibliotecaVO getServizio() {
		return servizio;
	}

	public void setServizio(ServizioBibliotecaVO servizio) {
		this.servizio = servizio;
	}

	public List<String> getCd_cat_mediazione() {
		return cd_cat_mediazione;
	}

	public void setCd_cat_mediazione(List<String> cd_cat_mediazione) {
		this.cd_cat_mediazione = cd_cat_mediazione;
	}

	public UtenteBaseVO getUtente() {
		return utente;
	}

	public void setUtente(UtenteBaseVO utente) {
		this.utente = utente;
	}

	public InventarioVO getInventario() {
		return inventario;
	}

	public void setInventario(InventarioVO inventario) {
		this.inventario = inventario;
	}

	public boolean isRemoto() {
		return remoto;
	}

	public void setRemoto(boolean remoto) {
		this.remoto = remoto;
	}

	public List<PrenotazionePostoVO> getPrenotazioniEscluse() {
		return prenotazioniEscluse;
	}

	public void setPrenotazioniEscluse(List<PrenotazionePostoVO> prenotazioniEscluse) {
		this.prenotazioniEscluse = prenotazioniEscluse;
	}

}
