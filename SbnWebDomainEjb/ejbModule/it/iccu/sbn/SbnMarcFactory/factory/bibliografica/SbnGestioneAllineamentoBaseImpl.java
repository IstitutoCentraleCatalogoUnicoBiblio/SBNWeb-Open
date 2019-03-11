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

import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbn;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaTitoloType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRequestType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;

import java.io.File;

public abstract class SbnGestioneAllineamentoBaseImpl {

	private static final String NOMEFILE_TEMPLATE = "_diff_";

	protected FactorySbn indice;
	protected SbnUserType user;

	public SbnGestioneAllineamentoBaseImpl() {
		super();
	}

	protected String costruisciNomeFile(String identAllineamento, int progressivoInizio) {
		String path;
		try {
			path = CommonConfiguration.getProperty(Configuration.SBNWEB_BATCH_ALLINEAMENTO_DA_LOCALE);
		} catch (Exception e) {
			path = ".";
		}
		String nomeFile = path + File.separator + identAllineamento + NOMEFILE_TEMPLATE + progressivoInizio;
		return nomeFile;
	}

	public SBNMarc scaricoOggettiDaIndice(String identAllineamento, int progressivoInizio, String prefisso) {

		SBNMarc sbnRisposta = null;

		try {

			SbnMessageType sbnmessage = new SbnMessageType();
			SbnRequestType sbnrequest = new SbnRequestType();

			CercaType cercaType = new CercaType();
			CercaTitoloType cercaTitoloType = new CercaTitoloType();
			CercaDatiTitType cercaDatiTitType = new CercaDatiTitType();
			CercaDatiTitTypeChoice cercaDatiTitTypeChoice = new CercaDatiTitTypeChoice();
			TitoloCercaType titoloCercaType = new TitoloCercaType();
			StringaCercaType stringaCercaType = new StringaCercaType();

			cercaType.setIdLista(prefisso + identAllineamento);
			cercaType.setTipoOutput(SbnTipoOutput.VALUE_0);
			cercaType.setTipoOrd(SbnTipoOrd.VALUE_0);
			cercaType.setMaxRighe(10);
			cercaType.setNumPrimo(progressivoInizio);

			titoloCercaType.setStringaCerca(stringaCercaType);
			cercaDatiTitTypeChoice.setTitoloCerca(titoloCercaType);
			cercaDatiTitType.setCercaDatiTitTypeChoice(cercaDatiTitTypeChoice);
			cercaTitoloType.setCercaDatiTit(cercaDatiTitType);
			cercaType.setCercaTitolo(cercaTitoloType);

			sbnmessage.setSbnRequest(sbnrequest);
			sbnrequest.setCerca(cercaType);

			this.indice.setMessage(sbnmessage, this.user);
			sbnRisposta = this.indice.eseguiRichiestaServer();

		} catch (SbnMarcException ve) {
			System.out.println("ERROR >>" + ve.getMessage());
		} catch (IllegalArgumentException ie) {
			System.out.println("ERROR >>" + ie.getMessage());
		} catch (Exception e) {
			System.out.println("ERROR >>" + e.getMessage());
		}

		return sbnRisposta;
	}

}
