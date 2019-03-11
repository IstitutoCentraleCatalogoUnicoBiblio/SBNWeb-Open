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
package it.finsiel.sbn.util;

import it.finsiel.sbn.polo.orm.amministrazione.Tbf_bibliotecario;
import it.finsiel.sbn.polo.orm.amministrazione.Trf_utente_professionale_biblioteca;

import java.util.Iterator;

public class PoloUtility {

	public String getCodPolo(Tbf_bibliotecario bibliotecario)
	{
		String codPolo = null;
		Iterator bibUte = bibliotecario.getId_utente_professionale().getTrf_utente_professionale_biblioteca().iterator();
		if(bibUte.hasNext())
		{
			Trf_utente_professionale_biblioteca bibUteProf = (Trf_utente_professionale_biblioteca) bibUte.next();
			codPolo = bibUteProf.getCd_polo().getCd_polo().getCd_polo();
		}
		return codPolo;
	}
	public String getCodBiblioteca(Tbf_bibliotecario bibliotecario)
	{
		String codBiblioteca = null;
		Iterator bibUte = bibliotecario.getId_utente_professionale().getTrf_utente_professionale_biblioteca().iterator();
		if(bibUte.hasNext())
		{
			Trf_utente_professionale_biblioteca bibUteProf = (Trf_utente_professionale_biblioteca) bibUte.next();
			codBiblioteca = bibUteProf.getCd_polo().getCd_biblioteca();
		}
		return codBiblioteca;
	}
}
