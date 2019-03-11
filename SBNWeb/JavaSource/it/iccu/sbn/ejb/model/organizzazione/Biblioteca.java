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
package it.iccu.sbn.ejb.model.organizzazione;

import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecarioVO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Biblioteca {
    private Biblioteca biblioteca;

    private BibliotecaVO valueObject;

    private Map bibliotecheDipendenti = new HashMap(5);

    private Map<String, BibliotecarioVO> bibliotecari = new HashMap<String, BibliotecarioVO>();

    private Biblioteca() {
    }

    public Biblioteca(BibliotecaVO vo) {
        this.setValueObject(vo);
    }

    public void setValueObject(BibliotecaVO vo) {
        this.valueObject = vo;
    }

    public BibliotecaVO getValueObject() {
        return valueObject;
    }

    public Collection getBibliotecheDipendenti() {
        return bibliotecheDipendenti.values();
    }

    public Biblioteca getBibliotecaDipendente(String codiceBiblioteca) {
        return (Biblioteca) bibliotecheDipendenti.get(codiceBiblioteca);
    }

    public Collection getBibliotecari() {
        return bibliotecari.values();
    }

    public BibliotecarioVO getBibliotecario(String codice) {
        return bibliotecari.get(codice);
    }

    public void addBibliotecario(BibliotecarioVO bibliotecario) {
        bibliotecari.put(bibliotecario.getCodiceBibliotecario(), bibliotecario);
    }

    public String toString() {
        return valueObject.toString();
    }
}
