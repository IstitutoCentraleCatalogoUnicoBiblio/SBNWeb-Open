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
package it.finsiel.gateway.local;

import it.finsiel.gateway.exception.SbnMarcDiagnosticoException;
import it.finsiel.gateway.intf.KeyAutore;
import it.finsiel.gateway.intf.KeySoggetto;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;

import java.io.Serializable;

import javax.ejb.Local;

@Local
public interface SbnMarcLocalGateway {

	public final String JNDI_NAME = "sbnMarc/SbnMarcLocalGateway/local";

	public SBNMarc execute(SBNMarc sbnmarc) throws Exception;

	//almaviva5_20131104
	public KeyAutore getChiaveAutore(DatiElementoType dati, String tipoAut) throws SbnMarcDiagnosticoException;

	public Serializable service(Serializable... payload) throws Exception;

	public String execute(String xml_text) throws Exception;

	public String definisciIsbdTitUniMusicale(String a_929, String b_929,
			String c_929, String e_929, String f_929, String g_929,
			String h_929, String i_929, String a_928[], String b_928,
			String c_928) throws SbnMarcDiagnosticoException;

	public KeySoggetto getChiaveSoggetto(String codSoggettario, SbnEdizioneSoggettario edizione, String testoSoggetto) throws SbnMarcDiagnosticoException;

}
