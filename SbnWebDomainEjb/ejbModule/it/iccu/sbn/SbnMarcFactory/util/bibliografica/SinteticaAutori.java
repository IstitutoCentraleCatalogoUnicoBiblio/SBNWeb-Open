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
package it.iccu.sbn.SbnMarcFactory.util.bibliografica;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
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
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.vo.gestionebibliografica.autore.SinteticaAutoriView;

import java.util.ArrayList;
import java.util.List;

public class SinteticaAutori {

	private final String IID_FORMA_RINVIO = "R";

	private final String IID_STRINGAVUOTA = "";

	private final String IID_FORMA_ACCETTATA = "A";

	private final String IID_SPAZIO = " ";

	UtilityCastor utilityCastor = new UtilityCastor();

	public List getSintetica(SBNMarc sbnMarcType, boolean remove) {

		List<SinteticaAutoriView> listaSintentica = new ArrayList<SinteticaAutoriView>();
		SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
		SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
		SbnResultType sbnResult = sbnResponse.getSbnResult();
		SbnResponseTypeChoice sbnResponseChoice = sbnResponse
				.getSbnResponseTypeChoice();
		SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();

		int progressivo = 0;
		int numeroElementiAut = getNumeroElementiAutority(sbnOutPut);

		SinteticaAutoriView sinteticaAutoriView;

		for (int t = 0; t < numeroElementiAut; t++) {
			System.out.println("Valore T =" + t);
			++progressivo;
			sinteticaAutoriView = new SinteticaAutoriView();
			sinteticaAutoriView = creaElementoLista(sbnOutPut, t, progressivo);
			listaSintentica.add(sinteticaAutoriView);
		}

		return listaSintentica;
	}

	public SinteticaAutoriView creaElementoLista(SbnOutputType sbnOutPut,
			int elementIndex, int progressivo) {

		// List contenente la nuova riga alla tabella
		SinteticaAutoriView data = new SinteticaAutoriView();

		// icona della prima colonna della tabella
		data.setImageUrl("blank.png");

		// PROGRESSIVO: Numero del elemento
		data.setProgressivo(progressivo);

		ElementAutType elementoAut = sbnOutPut.getElementoAut(elementIndex);
		DatiElementoType datiElemento = elementoAut.getDatiElementoAut();

		// prendo il nominativo
		String nominativo = utilityCastor
				.getNominativoDatiElemento(datiElemento);

		// prendo il tipo nome
		String tipoNome = utilityCastor.getTipoNomeDatiElemento(datiElemento);

		// prendo il VID
		String vid = datiElemento.getT001();
		data.setVid(vid.trim());

		// prendo la forma
		SbnFormaNome sbnForma = datiElemento.getFormaNome();
		String forma = IID_FORMA_RINVIO;
		String datazione = IID_STRINGAVUOTA;

		if (sbnForma.getType() == SbnFormaNome.A_TYPE) {
			forma = IID_FORMA_ACCETTATA;
		} else {

			if (elementoAut.getLegamiElementoAutCount() > 0) {
				String nominativoFormaRinvio = nominativo;

				LegamiType legamiType = elementoAut.getLegamiElementoAut(0);
				ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(0);
				LegameElementoAutType legameElemento = arrivoLegame
						.getLegameElementoAut();
				ElementAutType elementAutType = legameElemento
						.getElementoAutLegato();
				datiElemento = elementAutType.getDatiElementoAut();

				// LEGAMI
				String nominativoLegato = utilityCastor
						.getNominativoDatiElemento(datiElemento);

				// prendo il VID
				String VIDLegato = datiElemento.getT001();
				nominativo = new String(nominativoFormaRinvio + "\n--> "
						+ VIDLegato + IID_SPAZIO + nominativoLegato);
			}
		}

		// prendo la nota informativa
		String notaInformativa = utilityCastor
				.getNotaInformativaDatiElemento(datiElemento);
		if (notaInformativa != null) {
			int index = notaInformativa.indexOf(" //");
			if (index != -1) {
				// prendo la datazione prima
				// della "stringa-spazio-doppio slash"
				datazione = notaInformativa.substring(0, index);
			}
		}

		// aggiungo il nominativo alla tabella
		data.setNome(nominativo);

		// aggiungo la forma nome
		data.setForma(forma);

		// aggiungo il tipo nome alla tabella
		data.setTipoNome(tipoNome);

		SbnLivello livello = datiElemento.getLivelloAut();

		// aggiungo il Livello di autorità
		data.setLivelloAutorita(livello.toString());

		// aggiungo la datazione
		data.setDatazione(datazione);

		return data;
	}

	public int getNumeroElementiAutority(SbnOutputType sbnOutPut) {
		return sbnOutPut.getElementoAutCount();
	}

}
