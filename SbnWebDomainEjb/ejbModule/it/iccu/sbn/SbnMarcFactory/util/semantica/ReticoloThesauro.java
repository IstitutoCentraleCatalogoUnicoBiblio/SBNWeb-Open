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
package it.iccu.sbn.SbnMarcFactory.util.semantica;

import it.iccu.sbn.ejb.model.unimarcmodel.A935;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.TermineType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.web.vo.TreeElementView.KeyDisplayMode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReticoloThesauro {

	private DettaglioOggettoSemantico dettagli;
	private static Log log = LogFactory.getLog(ReticoloThesauro.class);

	public ReticoloThesauro(String ticket) {
		super();
		this.dettagli = new DettaglioOggettoSemantico(ticket);
	}

	public TreeElementViewSoggetti setReticolo(SBNMarc sbnMarcType,
			TreeElementViewSoggetti root) {

		return this.setReticolo(sbnMarcType, root, 0);
	}

	public TreeElementViewSoggetti setReticolo(SBNMarc sbnMarcType,
			TreeElementViewSoggetti root, int startIndex) {

		log.info("Creazione analitica thesauro");

		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();

		TermineType termine = null;

		ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();

		// ////// / COMPOSIZIONE DEL NODO PADRE DEL RETICOLO: DESCRITTORE TYPE
		termine = (TermineType) datiElemento;
		A935 a935 = termine.getT935();

		// DID
		String didRadice = termine.getT001();

		// Nota del Did
		String nota = a935.getB_935();

		// STRINGA DI Descrittore
		// Composta da a_931
		String testo = a935.getA_935();

		root.setText(testo);
		root.setKey(didRadice);
		root.setDescription(didRadice);
		root.setTesto(testo);
		root.setT005(datiElemento.getT005());
		root.setNote(nota);

		// inserire il trattamento del tipo di soggetto
		root.setIdNode(0);
		root.setTipoAuthority(SbnAuthority.TH);
		root.setFormaNome(datiElemento.getFormaNome().toString());
		root.open();
		root.setCheckVisible(false);
		root.setRadioVisible(true);
		root.setPlusVisible(true);
		root.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);

		root.getAreaDatiDettaglioOggettiVO().setDettaglioTermineThesauroVO(
				dettagli.getDettaglioTermine(root.isLivelloPolo(), termine));

		// IMPOSTO I LEGAMI DEL RETICOLO
		setLegamiReticolo(elementoAut, root);

		return root;
	}// end setReticolo

	/**
	 * Richiamato dal metodo setReticolo di JPanelReticoloSoggetti. Questo
	 * metodo gestisce la composizione su più livelli del reticolo dei Soggetti,
	 * gestendo in maniera ricorsiva i suoi legami a elementi di Authority.
	 *
	 * @param elementoAut
	 *            ElementAutType
	 * @param nodo
	 *            IIDDefaultMutableTreeNode
	 * @param tree
	 *            IIDTreeReticoloArea
	 */
	private void setLegamiReticolo(ElementAutType elementoAut,
			TreeElementViewSoggetti root) {

		TreeElementViewSoggetti nodoAppoggio = null;

		for (int i = 0; i < elementoAut.getLegamiElementoAutCount(); i++) {
			LegamiType legamiType = elementoAut.getLegamiElementoAut(i);

			for (int j = 0; j < legamiType.getArrivoLegameCount(); j++) {

				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
				LegameElementoAutType legameElemento = arrivoLegame
						.getLegameElementoAut();

				// prendo il tipo legame
				SbnLegameAut sbnLegameAut = legameElemento.getTipoLegame();
				String tipoLegameString = sbnLegameAut.toString();

				// prendo la nota al legame
				String notaLegame = legameElemento.getNoteLegame();

				// PRENDE L'ELEMENTO LEGATO
				ElementAutType elementAutType = legameElemento
						.getElementoAutLegato();

				// DID del legame
				String DIDLegato;
				// Descrittore del legame
				String testo;
				// valore del nodo
				String valueDelNodo;

				if (elementAutType != null) {

					nodoAppoggio = (TreeElementViewSoggetti) root.addChild();
					nodoAppoggio.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
					nodoAppoggio.setCheckVisible(false);
					nodoAppoggio.setRadioVisible(true);

					if (elementAutType.getDatiElementoAut() instanceof TermineType) {

						TermineType termine = (TermineType) elementAutType
								.getDatiElementoAut();

						// DID
						DIDLegato = termine.getT001();

						// Descrittore
						A935 a935 = termine.getT935();
						testo = a935.getA_935();
						if (tipoLegameString.equals("935")) {
							valueDelNodo = testo;
						} else {
							valueDelNodo = tipoLegameString + " " + testo;
						}

						if (tipoLegameString.equals("UF")
								|| tipoLegameString.equals("USE"))
							nodoAppoggio.setPlusVisible(false);


						nodoAppoggio.setKey(DIDLegato);
						nodoAppoggio.setText(valueDelNodo);
						nodoAppoggio.setDescription(DIDLegato);
						nodoAppoggio.setNote(a935.getB_935());
						nodoAppoggio.setFormaNome(termine.getFormaNome()
								.toString());
						nodoAppoggio.setT005(termine.getT005());
						nodoAppoggio.setTesto(testo);
						nodoAppoggio.setNotaLegame(notaLegame);

						// inserire il trattamento del tipo di soggetto

						nodoAppoggio.setIdNode(root.getIdNode() + 1);
						nodoAppoggio.setTipoAuthority(SbnAuthority.TH);
						nodoAppoggio.setDatiLegame(sbnLegameAut);
						nodoAppoggio.setLivelloPolo(root.isLivelloPolo());
						nodoAppoggio.getAreaDatiDettaglioOggettiVO()
								.setDettaglioTermineThesauroVO(
										dettagli.getDettaglioTermine(
												nodoAppoggio.isLivelloPolo(),
												termine));

						// RICORSIONE: SE L'ELEMENTO LEGATO HA
						// ULTERIORI LEGAMI, RICHIAMA SE STESSO
						if (elementAutType.getLegamiElementoAutCount() > 0) {
							setLegamiReticolo(elementAutType, nodoAppoggio);
						}
						// reticolo esplorabile se in forma accettata
						nodoAppoggio.setPlusVisible("A".equals(nodoAppoggio.getFormaNome()));

					}
				}// end if

			}// end for interno
		}// end for esterno

	}// end setLegamiReticolo

}
