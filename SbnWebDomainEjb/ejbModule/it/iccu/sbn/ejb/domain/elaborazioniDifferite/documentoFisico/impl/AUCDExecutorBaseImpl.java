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
package it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.impl;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.bibliografica.Interrogazione;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.AcquisizioneUriCopiaDigitale.AUCDExecutor;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAzioneLocalizza;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AcquisizioneUriCopiaDigitaleVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityMultiplaVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiLocalizzazioniAuthorityVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.polo.orm.documentofisico.Tbc_inventario;

import org.apache.log4j.Logger;

public abstract class AUCDExecutorBaseImpl implements AUCDExecutor {

	protected static final String HTML_NBSP = "&nbsp;";

	private AcquisizioneUriCopiaDigitaleVO richiesta;

	private Interrogazione interrogazione;

	private Interrogazione getInterrogazione() throws Exception {
		if (interrogazione != null)
			return interrogazione;

		this.interrogazione = DomainEJBFactory.getInstance().getInterrogazione();
		return interrogazione;
	}

	protected void eseguiLocalizzazione(String ticket, AreaDatiLocalizzazioniAuthorityVO adl, Logger log) {
		// localizzazione
		try {
			AreaDatiLocalizzazioniAuthorityMultiplaVO loc = new AreaDatiLocalizzazioniAuthorityMultiplaVO();
			loc.setListaAreaLocalizVO(ValidazioneDati.asSingletonList(adl));

			// Evolutiva Google3: Viene creato il nuovo metodo localizzaUnicoXML che effettua la chiamata per localizzazione tramite
			// un unica chiamata ai protocollo di Indice/Polo utilizzando sempre l'area di passaggio AreaDatiLocalizzazioniAuthorityMultiplaVO
			// questo metodo sostituirà localizzaAuthorityMultipla e localizzaAuthority
//			AreaDatiVariazioneReturnVO advr = getInterrogazione().localizzaAuthorityMultipla(loc, ticket);
			AreaDatiVariazioneReturnVO advr = getInterrogazione().localizzaUnicoXML(loc, ticket);

			// gestione errori
			if (advr != null
					&& (advr.getCodErr().equals("9999") || advr.getTestoProtocollo() != null)) {
				log.debug("Localizzazione non riuscita: " + advr.getTestoProtocollo());
			} else if (advr != null && (!advr.getCodErr().equals("0000"))) {
				if (advr.getCodErr().equals("noServerInd")) {
					log.debug("Localizzazione non riuscita: Il server di Indice non è attualmente disponibile");
				} else if (advr.getCodErr().equals("noServerPol")) {
					log.debug("Localizzazione non riuscita: Il server Locale non è attualmente disponibile");
				} else if (!advr.getCodErr().equals("7017")) {
					log.debug("Localizzazione non riuscita");
				}
			}
		} catch (Exception e) {
			log.error("Localizzazione non riuscita");
		}

	}

	protected AreaDatiLocalizzazioniAuthorityVO preparaDatiLocalizzazione(String codPolo, String codBib, Tbc_inventario i) {
		Tb_titolo t = i.getB();
		if (t == null)
			return null;

		AreaDatiLocalizzazioniAuthorityVO al = new AreaDatiLocalizzazioniAuthorityVO();
		al.setIndice(false);//ValidazioneDati.in(t.getFl_condiviso(), 's', 'S'));
		al.setPolo(true);
		al.setIdLoc(t.getBid());
		al.setNatura(String.valueOf(t.getCd_natura()));
		al.setAuthority("");
		al.setTipoMat(String.valueOf(t.getTp_materiale()));
		//al.setTipoOpe("Localizza");
		al.setTipoOpe(SbnAzioneLocalizza.CORREGGI.toString());
		//al.setSbnTipoLoc(SbnTipoLocalizza.TUTTI);
		al.setTipoLoc(SbnTipoLocalizza.TUTTI.toString());
		al.setTipoDigitalizzazione(String.valueOf(i.getDigitalizzazione()));
		al.setUriAccesso(i.getId_accesso_remoto());
		al.setCodiceSbn(codPolo + codBib);

		return al;
	}

	protected void setRichiesta(AcquisizioneUriCopiaDigitaleVO richiesta) {
		this.richiesta = richiesta;
	}

	protected AcquisizioneUriCopiaDigitaleVO getRichiesta() {
		return richiesta;
	}

}
