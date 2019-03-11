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
package it.iccu.sbn.SbnMarcFactory.factory.bibliografica;

import it.iccu.sbn.SbnMarcFactory.factory.FactorySbn;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.vo.common.SbnMarcEsitoType;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AllineaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.TracciatoStampaOutputAllineamentoVO;
import it.iccu.sbn.util.file.FileUtil;

import java.io.StringWriter;
import java.io.Writer;

/**
 * <p>
 * Title: SbnWEB
 * </p>
 * <p>
 * Description: Interfaccia web per il sistema bibliotecario nazionale
 * </p>
 * <p>
	// almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
	// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
	// del flag di avvenuto allineamento.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Finsiel
 * </p>
 *
 * @author Finsiel
 * @version 1.0
 */
public class SbnScaricaFileAllineamentiDaIndiceDao extends SbnGestioneAllineamentoBaseImpl {

	FactorySbn polo;

	public SbnScaricaFileAllineamentiDaIndiceDao(FactorySbn indice, FactorySbn polo,
			SbnUserType user) {
		this.user = user;
		this.indice = indice;
		this.polo = polo;
	}


	public AllineaVO trasferisciFileInPolo(AllineaVO areaDatiPass) {

		TracciatoStampaOutputAllineamentoVO outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<html><head>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("</head>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<body>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<table>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		String dataInizioRichTrasf = "";
		String dataFineRichTrasf = "";

		String numListaDaAllineare = areaDatiPass.getIdFileLocaleAllineamenti();
		boolean continuaCiclo = true;
		int progressivoBlocco = 1;
		int numBlocchi = 0;
		int numRigheTot = 0;
		int maxRighe = 0;

		SBNMarc sbnRisposta = null;

		while (continuaCiclo) {
			String messaggio = "";
			dataInizioRichTrasf = DateUtil.getDate() + " " + DateUtil.getTime();
			sbnRisposta = scaricoOggettiDaIndice(String.valueOf(numListaDaAllineare), progressivoBlocco, "BIS");
			dataFineRichTrasf = DateUtil.getDate() + " " + DateUtil.getTime();

			if (sbnRisposta == null
					|| sbnRisposta.getSbnMessage() == null
					|| sbnRisposta.getSbnMessage().getSbnResponse() == null
					|| sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult() == null
					|| sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito() == null) {

				String testoDaProspettare = "Il file per allineamento locale non è presente. Trasferimento file terminato";
				messaggio = messaggio + testoDaProspettare;

				outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR><TD>" +
						dataInizioRichTrasf + "</TD><TD>" +	dataFineRichTrasf +
						"</TD><TD>Errore in richiesta:" + testoDaProspettare + "</TD><TD>" + " " + "</TD></TR>");
				areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

//				areaDatiPass.setCodErr("9999");
//				areaDatiPass.setTestoProtocollo(testoDaProspettare);
//				return areaDatiPass;
				if (numBlocchi > progressivoBlocco) {
					progressivoBlocco++;
				} else {
					continuaCiclo = false;
				}
				continue;
			}

			if (SbnMarcEsitoType.of(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getEsito()) != SbnMarcEsitoType.OK) {

				messaggio = messaggio + sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito();

				outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR><TD>" +
						dataInizioRichTrasf + "</TD><TD>" +	dataFineRichTrasf +
						"</TD><TD>Errore in richiesta:" + sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito() +"</TD><TD>" + " " + "</TD></TR>");
				areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

//				areaDatiPass.setCodErr("9999");
//				areaDatiPass.setTestoProtocollo(sbnRisposta.getSbnMessage().getSbnResponse().getSbnResult().getTestoEsito());
//				return areaDatiPass;
				if (numBlocchi > progressivoBlocco) {
					progressivoBlocco++;
				} else {
					continuaCiclo = false;
				}
				continue;
			}

			scriviXMLSuFileLocale(numListaDaAllineare, progressivoBlocco, sbnRisposta);

			outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("<TR><TD>" +
					dataInizioRichTrasf + "</TD><TD>" +	dataFineRichTrasf +
					"</TD><TD>Scaricato file id " + numListaDaAllineare + " n. " + progressivoBlocco + "</TD><TD>&nbsp;</TD></TR>");
			areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

			numRigheTot = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getTotRighe();
			maxRighe = sbnRisposta.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getMaxRighe();
			if (maxRighe > 0) {
				numBlocchi = (int) (Math.ceil((double) numRigheTot	/ (double) maxRighe));
			}
			if (numBlocchi > progressivoBlocco) {
				progressivoBlocco++;
			} else {
				continuaCiclo = false;
			}
		}

		outputAllineamentoVO = new TracciatoStampaOutputAllineamentoVO("</table></body></html>");
		areaDatiPass.getLogAnalitico().add(outputAllineamentoVO);

		return areaDatiPass;
	}


	public SBNMarc scriviXMLSuFileLocale(final String identAllineamento, int progressivoInizio, SBNMarc sbnRisposta) {

		try {
			String nomeFile = costruisciNomeFile(identAllineamento, progressivoInizio);

			Writer writer = new StringWriter();
			sbnRisposta.marshal(writer);
			String xml = writer.toString();

			FileUtil.writeStringToFile(nomeFile, xml);

		} catch (IllegalArgumentException ie) {
			System.out.println("ERROR >>" + ie.getMessage());
		} catch (Exception e) {
			return null;
		}

		return sbnRisposta;
	}

}

