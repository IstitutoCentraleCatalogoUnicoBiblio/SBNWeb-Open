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
package it.iccu.sbn.batch.unimarc;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.esporta.EsportaVO;
import it.iccu.sbn.servizi.batch.SchedulableBatchVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.file.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class ExportSchedulableBatchVO extends SchedulableBatchVO {

	private static final long serialVersionUID = -2883771863550034442L;

	public ExportSchedulableBatchVO(SchedulableBatchVO sb) {
		super(sb.getConfig());
	}

	public String getTipoScarico() throws Exception {
		String cd_flg11 = trimOrEmpty(config.getCd_flg11());
		String[] tokens = cd_flg11.split("\\|");
		//il flag11 contiene sia il cod.scarico che l'elenco delle biblioteche, viene gestito il codice solo in prima
		//posizione
		if (tokens.length > 2)
			return null;

		String tipoScarico = tokens[0].trim().toUpperCase();
		TB_CODICI cod = CodiciProvider.cercaCodice(tipoScarico, CodiciType.CODICE_TIPO_ESTRAZIONE_UNIMARC,
			CodiciRicercaType.RICERCA_CODICE_SBN, true);
	
		return (cod != null) ? tipoScarico : null;
	}

	public List<String> getBiblioteche() throws Exception {
		Set<String> biblioteche = new HashSet<String>();
		String cd_flg11 = trimOrEmpty(config.getCd_flg11());

		//il flag11 contiene sia il cod.scarico che l'elenco delle biblioteche,
		//le biblioteche sono gestite solo in seconda posizione
		if (isFilled(getTipoScarico()) ) {
			String[] tokens = cd_flg11.split("\\|");
			if (tokens.length != 2)	//non ci sono biblioteche
				return Collections.emptyList();

			cd_flg11 = tokens[1]; //biblioteche nel secondo blocco
		} else
			//almaviva5_20170712 #6439 eliminazione delimitatore
			cd_flg11 = StringUtils.stripStart(cd_flg11, "|");

		for (String token : cd_flg11.split(",|;")) {
			token = token.trim();
			if (token.length() != 2)	//cod.bib senza spazio
				continue;

			biblioteche.add(ValidazioneDati.fillLeft(token.toUpperCase(), ' ', 3));
		}

		return new ArrayList<String>(biblioteche);
	}

	public EsportaVO getTemplate() {
		EsportaVO esportaVO = null;
		String cd_flg11 = trimOrEmpty(config.getCd_flg11());
		if (cd_flg11.startsWith("template=")) {
			final String filename = cd_flg11.substring("template=".length());
			File f = new File(filename);
			if (f.exists()) {
				try {
					String template = FileUtil.streamToString(new FileInputStream(f));
					esportaVO = ClonePool.fromJson(template, EsportaVO.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return esportaVO;
	}

}
