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

import it.finsiel.gateway.exception.SbnMarcDiagnosticoException;
import it.finsiel.gateway.intf.KeySoggetto;
import it.finsiel.gateway.local.SbnMarcLocalGateway;
import it.iccu.sbn.ejb.DomainEJBFactory.Reference;
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
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnEdizioneSoggettario;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.utils.semantica.SoggettiUtil;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ElementoSinteticaSoggettoVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.TreeElementViewSoggetti;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.util.jndi.JNDIUtil;
import it.iccu.sbn.vo.custom.semantica.TreeElementTextDecoratorSogDesImpl;
import it.iccu.sbn.web.vo.TreeElementView.KeyDisplayMode;

import java.util.List;

import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;

public class ReticoloSoggetti {

	static Reference<SbnMarcLocalGateway> gateway = new Reference<SbnMarcLocalGateway>() {
		@Override
		protected SbnMarcLocalGateway init() throws Exception {
			InitialContext ctx = JNDIUtil.getContext();
			return (SbnMarcLocalGateway) ctx.lookup(SbnMarcLocalGateway.JNDI_NAME);
		}};

	private static Logger log = Logger.getLogger(ReticoloSoggetti.class);
	private DettaglioOggettoSemantico dettagli;

	public static KeySoggetto getChiaveSoggetto(String codSogg, SbnEdizioneSoggettario edizione, String testoSoggetto)
			throws SbnMarcDiagnosticoException {
		return gateway.get().getChiaveSoggetto(codSogg, edizione, testoSoggetto);
	}

	public static boolean isSoggettoEquals(String testoSoggetto1, String testoSoggetto2) throws SbnMarcDiagnosticoException {
		KeySoggetto k1 = getChiaveSoggetto(null, null, testoSoggetto1);
		KeySoggetto k2 = getChiaveSoggetto(null, null, testoSoggetto2);

		log.debug("soggetto1: " + k1);
		log.debug("soggetto2: " + k2);

		return k1.equals(k2);
	}


	public static ElementoSinteticaSoggettoVO trovaSimileConTestoEsatto(String testoSoggetto,
			List<ElementoSinteticaSoggettoVO> listaSimili) throws SbnMarcDiagnosticoException {
		if (isFilled(listaSimili))
			for (ElementoSinteticaSoggettoVO simile : listaSimili) {
				if (isSoggettoEquals(testoSoggetto, simile.getTesto()))
					return simile;
			}
		return null;
	}

	public ReticoloSoggetti(String ticket) {
		super();
		this.dettagli = new DettaglioOggettoSemantico(ticket);
	}

	public TreeElementViewSoggetti setReticolo(SBNMarc sbnMarcType,
			TreeElementViewSoggetti root, boolean completo) throws DaoManagerException {

		log.debug("inizio costruzione reticolo soggetti");

		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();

		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();
		SoggettoType soggettoType = null;

		ElementAutType elementoAut = sbnOutPut.getElementoAut(0);
		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();

		// ////// / COMPOSIZIONE DEL NODO PADRE DEL RETICOLO: SOGGETTO TYPE
		soggettoType = (SoggettoType) datiElemento;

		// CID
		String cidRadice = soggettoType.getT001();

		// STRINGA DI SOGGETTO
		// Composta da a_250 sommato agli x_250, separati da " - ".
		String stringaSoggetto = SoggettiUtil.costruisciStringaSoggetto(soggettoType);

		//almaviva5_20120424 evolutive CFI
		root.setTextDecorator(new TreeElementTextDecoratorSogDesImpl());
		root.setKey(cidRadice);
		root.setText(stringaSoggetto);
		root.setTesto(stringaSoggetto);
		root.setDescription(cidRadice);
		root.setFlagCondiviso(true);
		root.setTipoAuthority(SbnAuthority.SO);

		root.getAreaDatiDettaglioOggettiVO().setDettaglioSoggettoGeneraleVO(
				dettagli.getDettaglioSoggetto(root.isLivelloPolo(),
						soggettoType, completo));

		if (root.isLivelloPolo())
			root.setFlagCondiviso(datiElemento.getCondiviso().getType() == DatiElementoTypeCondivisoType.S_TYPE);
		else
			root.setFlagCondiviso(true);	//livello indice

		root.setT005(datiElemento.getT005());
		// inserire il trattamento del tipo di soggetto
		root.setIdNode(0);
		root.setRigaReticoloCtr(0);
		root.open();
		root.setCheckVisible(false);
		root.setRadioVisible(true);
		root.setPlusVisible(false);

		root.setKeyDisplayMode(KeyDisplayMode.SHOW_AS_LINK);

		// IMPOSTO I LEGAMI DEL RETICOLO
		setLegamiReticolo(elementoAut, root, completo);

		log.debug("fine   costruzione reticolo soggetti");

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
				LegameElementoAutType legameElemento = arrivoLegame
						.getLegameElementoAut();

				// prendo il tipo legame
				SbnLegameAut sbnLegameAut = legameElemento.getTipoLegame();
				String tipoLegameString = sbnLegameAut.toString();

				// prendo la nota al legame
				String notaLegame = legameElemento.getNoteLegame();

				// PRENDE L'ELEMENTO LEGATO
				ElementAutType elementAutType = legameElemento.getElementoAutLegato();

				// DID del legame
				String CIDLegato;
				// Stinga di Soggetto
				String strSoggetto;
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
					root.setPlusVisible(true);

					if (elementAutType.getDatiElementoAut() instanceof DescrittoreType) {

						DescrittoreType descrittore = (DescrittoreType) elementAutType.getDatiElementoAut();

						// DID
						DIDLegato = descrittore.getT001();
						// Descrittore
						A931 a931 = descrittore.getT931();
						strDescrittore = a931.getA_931();
						if (tipoLegameString.equals("931"))
							valueDelNodo = strDescrittore;
						 else
							valueDelNodo = tipoLegameString + " " + strDescrittore;


						child.setKey(DIDLegato);
						child.setText(valueDelNodo);
						child.setDescription(DIDLegato);
						child.setNote(descrittore.getT931().getB_931());
						child.setT005(descrittore.getT005());

						child.setTesto(strDescrittore);
						child.setFlagCondiviso(true);
						// nodoAppoggio.setRadioVisible(false);
						// nodoAppoggio.setPlusVisible(true);
						// inserire il trattamento del tipo di soggetto
						if (tipoLegameString.equals("UF") || tipoLegameString.equals("USE"))
							child.setPlusVisible(false);

						child.setIdNode(root.getIdNode() + 1);

						child.setTipoAuthority(SbnAuthority.DE);
						child.setDatiLegame(sbnLegameAut);
						child.setFormaNome(descrittore.getFormaNome().toString());
						child.setNotaLegame(notaLegame);
						child.setLivelloPolo(root.isLivelloPolo());

						child.getAreaDatiDettaglioOggettiVO()
								.setDettaglioDescrittoreGeneraleVO(
										dettagli.getDettaglioDescrittore(child
												.isLivelloPolo(), descrittore, completo));

						// RICORSIONE: SE L'ELEMENTO LEGATO HA
						// ULTERIORI LEGAMI, RICHIAMA SE STESSO
						if (elementAutType.getLegamiElementoAutCount() > 0) {
							setLegamiReticolo(elementAutType, child, completo);
							child.setPlusVisible(true);
						}

						//almaviva5_20080601
						if (child.getNodeLevel() == 1) {// descrittore automatico?
							//DettaglioSoggettoVO dettaglioRoot = (DettaglioSoggettoVO) root.getDettaglio();
							int posizione = descrittore.getRank(); //SoggettiUtil.getPosizioneDescrittoreAutomatico(dettaglioRoot.getTesto(), strDescrittore);
							child.setPosizione(posizione);
						}
					} else if (elementAutType.getDatiElementoAut() instanceof SoggettoType) {

						SoggettoType soggettoType = (SoggettoType) elementAutType.getDatiElementoAut();
						// CID
						CIDLegato = soggettoType.getT001();

						//almaviva5_20121005 evolutive CFI
						strSoggetto = SoggettiUtil.costruisciStringaSoggetto(soggettoType);

						valueDelNodo = tipoLegameString + " " + strSoggetto;

						child.setKey(CIDLegato);
						child.setText(valueDelNodo);
						child.setDescription(CIDLegato);

						child.setT005(soggettoType.getT005());

						child.setTesto(strSoggetto);
						child.setRadioVisible(true);
						child.setFlagCondiviso(true);
						child.setLivelloPolo(root.isLivelloPolo());
						if (child.isLivelloPolo()) {
							child.setFlagCondiviso(soggettoType
									.getCondiviso().toString()
									.equalsIgnoreCase("S"));
						}

						// aggiungo un nodo all'albero
						child.setIdNode(root.getIdNode() + 1);

						child.setTipoAuthority(SbnAuthority.SO);
						child.setDatiLegame(sbnLegameAut);

						child.getAreaDatiDettaglioOggettiVO()
								.setDettaglioSoggettoGeneraleVO(
										dettagli.getDettaglioSoggetto(
												child.isLivelloPolo(),
												soggettoType, completo));
						// RICORSIONE: SE L'ELEMENTO LEGATO HA
						// ULTERIORI LEGAMI, RICHIAMA SE STESSO
						if (elementAutType.getLegamiElementoAutCount() > 0) {
							setLegamiReticolo(elementAutType, child, completo);
						}

					}
				}// end if

			}// end for interno
		}// end for esterno

	}// end setLegamiReticolo

}
