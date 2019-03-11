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

import it.iccu.sbn.ejb.model.unimarcmodel.A931;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DescrittoreType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SoggettoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.utils.semantica.SoggettiUtil;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.vo.custom.semantica.TreeElementTextDecoratorSogDesImpl;
import it.iccu.sbn.web.vo.TreeElementView.KeyDisplayMode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReticoloDescrittori {

	private DettaglioOggettoSemantico dettagli;
	private static Log log = LogFactory.getLog(ReticoloDescrittori.class);

	public ReticoloDescrittori(String ticket) {
		super();
		this.dettagli = new DettaglioOggettoSemantico(ticket);
	}

	public TreeElementViewSoggetti setReticolo(SBNMarc sbnMarcType,
			TreeElementViewSoggetti root, boolean completo) throws DaoManagerException {

		return this.setReticolo(sbnMarcType, root, 0, completo);
	}

	public TreeElementViewSoggetti setReticolo(SBNMarc sbnMarcType,
			TreeElementViewSoggetti root, int startIndex, boolean completo) throws DaoManagerException {

		log.info("Creazione analitica descrittore");

		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();

		DescrittoreType descrittore = null;

		ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();

		// ////// / COMPOSIZIONE DEL NODO PADRE DEL RETICOLO: DESCRITTORE TYPE
		descrittore = (DescrittoreType) datiElemento;
		A931 a931 = new A931();
		a931 = descrittore.getT931();

		// DID
		String didRadice = descrittore.getT001();

		// Nota del Did
		String nota = descrittore.getT931().getB_931();

		//almaviva5_20120424 evolutive CFI
		root.setTextDecorator(new TreeElementTextDecoratorSogDesImpl());

		// STRINGA DI Descrittore
		// Composta da a_931
		root.setText(a931.getA_931());
		root.setKey(didRadice);
		root.setDescription(didRadice);
		root.setTesto(a931.getA_931());

		root.setT005(datiElemento.getT005());
		root.setNote(nota);

		// inserire il trattamento del tipo di soggetto
		root.setIdNode(0);
		root.setTipoAuthority(SbnAuthority.DE);
		root.setFormaNome(datiElemento.getFormaNome().toString());
		root.open();
		root.setCheckVisible(false);
		root.setRadioVisible(true);
		root.setPlusVisible(true);
		root.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);

		root.getAreaDatiDettaglioOggettiVO().setDettaglioDescrittoreGeneraleVO(
				dettagli.getDettaglioDescrittore(root.isLivelloPolo(),
						descrittore, completo));

		// IMPOSTO I LEGAMI DEL RETICOLO
		setLegamiReticolo(elementoAut, root, completo);

		return root;
	}// end setReticolo

	/**
	 * Richiamato dal metodo setReticolo di JPanelReticoloSoggetti. Questo
	 * metodo gestisce la composizione su più livelli del reticolo dei Soggetti,
	 * gestendo in maniera ricorsiva i suoi legami a elementi di Authority.
	 *
	 * @param elementoAut
	 *            ElementAutType
	 * @param completo
	 * @param nodo
	 *            IIDDefaultMutableTreeNode
	 * @param tree
	 *            IIDTreeReticoloArea
	 * @throws DaoManagerException
	 */
	private void setLegamiReticolo(ElementAutType elementoAut,
			TreeElementViewSoggetti root, boolean completo) throws DaoManagerException {

		for (int i = 0; i < elementoAut.getLegamiElementoAutCount(); i++) {
			LegamiType legamiType = elementoAut.getLegamiElementoAut(i);

			for (int j = 0; j < legamiType.getArrivoLegameCount(); j++) {

				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(j);
				LegameElementoAutType legameElemento = arrivoLegame.getLegameElementoAut();

				// prendo il tipo legame
				SbnLegameAut sbnLegameAut = legameElemento.getTipoLegame();
				String tipoLegameString = sbnLegameAut.toString();

				// prendo la nota al legame
				String notaLegame = legameElemento.getNoteLegame();

				// PRENDE L'ELEMENTO LEGATO
				ElementAutType elementAutType = legameElemento
						.getElementoAutLegato();

				// DID del legame
				String CIDLegato;
				// Stinga di Soggetto
				String strSoggetto;
				// Separatore Soggetto-Descrittori
				String separatoreLegame;
				// DID del legame
				String DIDLegato;
				// Descrittore del legame
				String strDescrittore;
				// valore del nodo
				String valueDelNodo;

				if (elementAutType != null) {

					TreeElementViewSoggetti child = (TreeElementViewSoggetti) root.addChild();
					//almaviva5_20120424 evolutive CFI
					child.setTextDecorator(new TreeElementTextDecoratorSogDesImpl());
					child.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);
					child.setCheckVisible(false);
					child.setRadioVisible(true);

					if (elementAutType.getDatiElementoAut() instanceof DescrittoreType) {

						DescrittoreType descrittore = (DescrittoreType) elementAutType.getDatiElementoAut();

						// DID
						DIDLegato = descrittore.getT001();

						// Descrittore
						strDescrittore = descrittore.getT931().getA_931();

						if (tipoLegameString.equals("UF")
								|| tipoLegameString.equals("USE"))
							child.setPlusVisible(false);


						child.setKey(DIDLegato);
						String strDescrittore2 = strDescrittore;
						child.setText(strDescrittore2);
						child.setDescription(DIDLegato);

						child.setNote(descrittore.getT931().getB_931());
						child.setFormaNome(descrittore.getFormaNome().toString());
						child.setT005(descrittore.getT005());
						child.setTesto(strDescrittore2);
						child.setNotaLegame(notaLegame);

						// inserire il trattamento del tipo di soggetto

						child.setIdNode(root.getIdNode() + 1);
						child.setTipoAuthority(SbnAuthority.DE);
						child.setDatiLegame(sbnLegameAut);
						child.setLivelloPolo(root.isLivelloPolo());
						child.getAreaDatiDettaglioOggettiVO()
								.setDettaglioDescrittoreGeneraleVO(
										dettagli.getDettaglioDescrittore(
												child.isLivelloPolo(),
												descrittore, completo));

						// RICORSIONE: SE L'ELEMENTO LEGATO HA
						// ULTERIORI LEGAMI, RICHIAMA SE STESSO
						if (elementAutType.getLegamiElementoAutCount() > 0) {
							setLegamiReticolo(elementAutType, child, completo);
						}

					} else if (elementAutType.getDatiElementoAut() instanceof SoggettoType) {

						SoggettoType soggettoType = (SoggettoType) elementAutType.getDatiElementoAut();

						// CID
						CIDLegato = soggettoType.getT001();

						//almaviva5_20121005 evolutive CFI
						strSoggetto = SoggettiUtil.costruisciStringaSoggetto(soggettoType);

						// valueDelNodo = tipoLegameString + " " + CIDLegato + "
						// " + strSoggetto;
						valueDelNodo = tipoLegameString + " " + strSoggetto;

						child.setKey(CIDLegato);
						child.setText(valueDelNodo);
						child.setDescription(CIDLegato);

						child.setFormaNome(soggettoType.getFormaNome().toString());
						child.setT005(soggettoType.getT005());
						child.setTesto(strSoggetto);

						// aggiungo un nodo all'albero
						child.setIdNode(root.getIdNode() + 1);

						child.setTipoAuthority(SbnAuthority.SO);
						child.setDatiLegame(sbnLegameAut);
						child.setLivelloPolo(root.isLivelloPolo());
						child.getAreaDatiDettaglioOggettiVO()
								.setDettaglioSoggettoGeneraleVO(
										dettagli.getDettaglioSoggetto(
												child.isLivelloPolo(),
												soggettoType, completo));

						// RICORSIONE: SE L'ELEMENTO LEGATO HA
						// ULTERIORI LEGAMI, RICHIAMA SE STESSO
						if (elementAutType.getLegamiElementoAutCount() > 0) {
							setLegamiReticolo(elementAutType, child, completo);
							child.setPlusVisible(true);
						}

					}
				}// end if

			}// end for interno
		}// end for esterno

	}// end setLegamiReticolo

}
