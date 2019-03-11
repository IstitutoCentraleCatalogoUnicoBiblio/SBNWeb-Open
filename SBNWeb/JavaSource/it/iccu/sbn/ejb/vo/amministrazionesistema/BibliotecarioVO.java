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
package it.iccu.sbn.ejb.vo.amministrazionesistema;

import it.iccu.sbn.ejb.vo.BaseVO;

public class BibliotecarioVO extends BaseVO{

    /**
	 *
	 */
	private static final long serialVersionUID = 6388558771051713081L;

	private String codiceBibliotecario;

    private String codiceBiblioteca;

    private String nominativo;

    private String ufficioAppartenenza;

    private String livelloAutA;

    private String livelloAutC;

    private String livelloAutS;

    private String livelloAutT;

    private String noteCompetenza;
    private String password;

    public BibliotecarioVO() {
    }

    public String getCodiceBibliotecario() {
        return codiceBibliotecario;
    }

    public void setCodiceBibliotecario(String codice) {
        this.codiceBibliotecario = codice;
    }

    public String getCodiceBiblioteca() {
        return codiceBiblioteca;
    }

    public void setCodiceBiblioteca(String codiceBiblioteca) {
        this.codiceBiblioteca = codiceBiblioteca;
    }

    public String getLivelloAutA() {
        return livelloAutA;
    }

    public void setLivelloAutA(String livelloAutA) {
        this.livelloAutA = livelloAutA;
    }

    public String getLivelloAutC() {
        return livelloAutC;
    }

    public void setLivelloAutC(String livelloAutC) {
        this.livelloAutC = livelloAutC;
    }

    public String getLivelloAutS() {
        return livelloAutS;
    }

    public void setLivelloAutS(String livelloAutS) {
        this.livelloAutS = livelloAutS;
    }

    public String getLivelloAutT() {
        return livelloAutT;
    }

    public void setLivelloAutT(String livelloAutT) {
        this.livelloAutT = livelloAutT;
    }

    public String getNominativo() {
        return nominativo;
    }

    public void setNominativo(String nominativo) {
        this.nominativo = nominativo;
    }

    public String getNoteCompetenza() {
        return noteCompetenza;
    }

    public void setNoteCompetenza(String noteCompetenza) {
        this.noteCompetenza = noteCompetenza;
    }

    public String getUfficioAppartenenza() {
        return ufficioAppartenenza;
    }

    public void setUfficioAppartenenza(String ufficioAppartenenza) {
        this.ufficioAppartenenza = ufficioAppartenenza;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
