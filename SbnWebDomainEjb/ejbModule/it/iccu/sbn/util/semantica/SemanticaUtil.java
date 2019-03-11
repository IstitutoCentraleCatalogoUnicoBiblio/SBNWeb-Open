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
package it.iccu.sbn.util.semantica;

import static it.iccu.sbn.util.Constants.Semantica.Soggetti.SOGGETTARI_FIRENZE;

import it.iccu.sbn.ejb.model.unimarcmodel.A250;
import it.iccu.sbn.ejb.model.unimarcmodel.A931;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.unicode.OrdinamentoUnicode;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionesemantica.FormaNomeType;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.DettaglioDescrittoreVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class SemanticaUtil {

	private static Logger log = Logger.getLogger(SemanticaUtil.class);

	public static final String getSoggettarioSBN(A250 a250) {
		try {
			String soggSBN = null;
			String soggUNI = a250.getC2_250();
			if (ValidazioneDati.in(soggUNI, SOGGETTARI_FIRENZE) ) {
				String edizione = a250.getEdizione() != null ? a250.getEdizione().toString() : null;
				if (ValidazioneDati.isFilled(edizione))
					//se l'edizione Ã¨ valorizzata cerco il soggettario partendo dall'edizione
					soggUNI = getSoggettarioIndiceDaEdizione(edizione);
			} else
				 a250.setEdizione(null);

			soggSBN = CodiciProvider.unimarcToSBN(CodiciType.CODICE_SOGGETTARIO, soggUNI);
			if (!ValidazioneDati.isFilled(soggSBN))
				//sogg. da indice?
				soggSBN = CommonConfiguration.getProperty(Configuration.CODICE_SOGGETTARIO_FIRENZE, "FIR");

			return soggSBN;
		} catch (Exception e) {
			log.error("", e);
			return "";
		}
	}

	public static final String getSoggettarioSBN(A931 a931) {
		try {
			String soggSBN = null;
			String soggUNI = a931.getC2_931();
			if (ValidazioneDati.in(soggUNI, SOGGETTARI_FIRENZE) ) {
				String edizione = a931.getEdizione() != null ? a931.getEdizione().toString() : null;
				if (ValidazioneDati.isFilled(edizione))
					//se l'edizione ï¿½ valorizzata cerco il soggettario partendo dall'edizione
					soggUNI = getSoggettarioIndiceDaEdizione(edizione);
			} else
				a931.setEdizione(null);

			soggSBN = CodiciProvider.unimarcToSBN(CodiciType.CODICE_SOGGETTARIO, soggUNI);
			if (!ValidazioneDati.isFilled(soggSBN))
				//sogg. da indice?
				soggSBN = CommonConfiguration.getProperty(Configuration.CODICE_SOGGETTARIO_FIRENZE, "FIR");

			return soggSBN;
		} catch (Exception e) {
			log.error("", e);
			return "";
		}
	}

	public static final String getEdizioneSoggettarioIndice(String soggettario) {
		try {
			List<TB_CODICI> codici = CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_SOGGETTARIO);
			for (TB_CODICI cod : codici) {
				if (ValidazioneDati.equals(cod.getCd_unimarc(), soggettario))
					return cod.getCd_tabellaTrim();
			}

			log.warn("getEdizioneSoggettarioIndice(): edizione soggettario indice non trovata");
			return "";

		} catch (Exception e) {
			log.error("", e);
			return "";
		}
	}

	public static final String getEdizioneSoggettarioIndice(A250 a250) {
		return getEdizioneSoggettarioIndice(a250.getC2_250());
	}

	public static final String getEdizioneSoggettarioIndice(A931 a931) {
		return getEdizioneSoggettarioIndice(a931.getC2_931());
	}

	public static final String getSoggettarioIndiceDaEdizione(String edSoggettario) {
		try {
			TB_CODICI cod = CodiciProvider.cercaCodice(edSoggettario,
					CodiciType.CODICE_EDIZIONE_SOGGETTARIO,
					CodiciRicercaType.RICERCA_CODICE_SBN);

			return cod.getCd_unimarc();

		} catch (Exception e) {
			log.error("", e);
			return "";
		}
	}

	public static final List<TB_CODICI> getListaTipoLegameTraDescrittori(
			DettaglioDescrittoreVO d1, DettaglioDescrittoreVO d2) throws Exception {

		String sog1 = d1.getCampoSoggettario();
		String sog2 = d2.getCampoSoggettario();
		boolean isFIR = ValidazioneDati.equals(sog1, CommonConfiguration.getProperty(Configuration.CODICE_SOGGETTARIO_FIRENZE) )
			&& ValidazioneDati.equals(sog1, sog2);

		SbnEdizioneSoggettario ed1 = isFIR && ValidazioneDati.isFilled(d1.getEdizioneSoggettario()) ?
				SbnEdizioneSoggettario.valueOf(d1.getEdizioneSoggettario()) :
				null;
		SbnEdizioneSoggettario ed2 = isFIR && ValidazioneDati.isFilled(d2.getEdizioneSoggettario()) ?
				SbnEdizioneSoggettario.valueOf(d2.getEdizioneSoggettario()) :
				null;

		List<TB_CODICI> output = new ArrayList<TB_CODICI>();
		List<TB_CODICI> codici = CodiciProvider.getCodici(CodiciType.CODICE_LEGAME_DESCRITTORE_DESCRITTORE, true);
		for (TB_CODICI cod : codici) {
			//check forma nome
			String forma = cod.getCd_flg7();
			if (!ValidazioneDati.isFilled(forma))
				continue;

			String forma1 = d1.getFormaNome();
			String forma2 = d2.getFormaNome();
			switch (FormaNomeType.fromString(forma)) {
			case ACCETTATA_ACCETTATA:
				if (!forma1.equalsIgnoreCase("A") || !forma2.equalsIgnoreCase("A") )
					continue;
				break;

			case ACCETTATA_RINVIO:
				if (!forma1.equalsIgnoreCase("A") || !forma2.equalsIgnoreCase("R") )
					continue;
				break;

			case RINVIO_ACCETTATA:
				if (!forma1.equalsIgnoreCase("R") || !forma2.equalsIgnoreCase("A") )
					continue;
				break;
			}

			SbnLegameAut tpLegame = SbnLegameAut.valueOf(cod.getCd_tabellaTrim());
			//check legami storici
			boolean isStorico = ValidazioneDati.equals(cod.getCd_flg9(), "S");
			if (isStorico) {
				if (!isFIR || ed1 == null || ed2 == null)
					//senza edizione non sono MAI ammessi legami storici
					continue;

				switch (ed1.getType()) {
				case SbnEdizioneSoggettario.I_TYPE:
					if (!ValidazioneDati.equals(ed2, SbnEdizioneSoggettario.N))
						//legame storico ammesso solo se l'edizione Ã¨ nuova
						continue;

					if (!ValidazioneDati.in(tpLegame, SbnLegameAut.valueOf("HSEE")) )
						continue;

					break;

				case SbnEdizioneSoggettario.N_TYPE:
					if (!ValidazioneDati.equals(ed2, SbnEdizioneSoggettario.I))
						//legame storico ammesso solo se l'edizione Ã¨ vecchia
						continue;

					if (!ValidazioneDati.in(tpLegame, SbnLegameAut.valueOf("HSF")) )
						continue;

					break;

				case SbnEdizioneSoggettario.E_TYPE:
					//non ammessi
					continue;
				}

			} else
				if (isFIR) {
					if (ed1 == null)
						//senza edizione non sono MAI ammessi legami storici
						continue;
					//legami non storici
					switch (ed1.getType()) {
					case SbnEdizioneSoggettario.I_TYPE:
						if (ValidazioneDati.equals(ed2, SbnEdizioneSoggettario.N))
							//legame non ammesso con edizione diversa
							continue;

						break;

					case SbnEdizioneSoggettario.N_TYPE:
						if (ValidazioneDati.equals(ed2, SbnEdizioneSoggettario.I))
							//legame non ammesso con edizione diversa
							continue;

						break;

					case SbnEdizioneSoggettario.E_TYPE:
						break;
					}
				}

			output.add(cod);
		}

		return output;

	}

	public static final String[] getEdizioniRicerca(SbnEdizioneSoggettario cd_edizione) {
		String[] values = null;
		switch (cd_edizione.getType()) {
		case SbnEdizioneSoggettario.E_TYPE:
			values = new String[] { "E" };
			break;
		case SbnEdizioneSoggettario.I_TYPE:
			values = new String[] { "E", "I" };
			break;
		case SbnEdizioneSoggettario.N_TYPE:
			values = new String[] { "E", "N" };
			break;
		}
		return values;
	}

	public static final String[] normalizzaParolePerRicerca(String input) {

		if (!ValidazioneDati.isFilled(input))
			return null;

		String[] parole = input.split("\\s+");
		List<String> tmp = new ArrayList<String>();
		int length = ValidazioneDati.size(parole);
		for (int p = 0; p < length; p++) {
			String norm = ValidazioneDati.trimOrEmpty(OrdinamentoUnicode.getInstance().convert(parole[p]));
			// rieseguo lo split sulla singola stringa, questo perchÃ©
			// la normalizzazione puÃ² aver sostituito dei separatori con spazio
			// e risulta necessario trattare le parole composte (es. "nord-ovest", "d'amato")
			// come due parole distinte
			tmp.addAll(Arrays.asList(norm.split("\\s+")));
		}

		Iterator<String> i = tmp.iterator();
		while (i.hasNext())
			// cancello le stringhe vuote e quelle incluse nella stoplist
			if (ValidazioneDati.strIsNull(i.next()))
				i.remove();

		return tmp.toArray(new String[0]);
	}

}
