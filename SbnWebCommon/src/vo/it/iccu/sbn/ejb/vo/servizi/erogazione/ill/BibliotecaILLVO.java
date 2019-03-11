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
package it.iccu.sbn.ejb.vo.servizi.erogazione.ill;

import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.UtenteBaseVO;

public class BibliotecaILLVO extends BaseVO {

	private static final long serialVersionUID = -1074037820413633367L;

	private int id_biblioteca;
	private String isil;
	private BibliotecaVO biblioteca;
	private UtenteBaseVO utente;
	private String descrizione;
	private String tipoPrestito;
	private String tipoDocDelivery;
	private String ruolo;

	public int getId_biblioteca() {
		return id_biblioteca;
	}

	public void setId_biblioteca(int id_biblioteca) {
		this.id_biblioteca = id_biblioteca;
	}

	public String getIsil() {
		return isil;
	}

	public void setIsil(String isil) {
		this.isil = trimAndSet(isil);
	}

	public BibliotecaVO getBiblioteca() {
		return biblioteca;
	}

	public void setBiblioteca(BibliotecaVO biblioteca) {
		this.biblioteca = biblioteca;
	}

	public UtenteBaseVO getUtente() {
		return utente;
	}

	public void setUtente(UtenteBaseVO utente) {
		this.utente = utente;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = trimAndSet(descrizione);
	}

	public String getTipoPrestito() {
		return tipoPrestito;
	}

	public void setTipoPrestito(String tipoPrestito) {
		this.tipoPrestito = tipoPrestito;
	}

	public String getTipoDocDelivery() {
		return tipoDocDelivery;
	}

	public void setTipoDocDelivery(String tipoDocDelivery) {
		this.tipoDocDelivery = tipoDocDelivery;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public boolean isNuovo() {
		return (id_biblioteca == 0);
	}

	public String getCodPolo() {
		return (biblioteca != null) ? biblioteca.getCod_polo() : "";
	}

	public String getCodBib() {
		return (biblioteca != null) ? biblioteca.getCod_bib() : "";
	}

}
