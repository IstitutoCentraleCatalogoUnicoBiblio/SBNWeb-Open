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
package it.iccu.sbn.web.actions.gestionesemantica.utility;

import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.FormaNomeType;
import it.iccu.sbn.ejb.vo.gestionesemantica.PosizioneDescrittore;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamento;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoOrdinamentoClasse;
import it.iccu.sbn.ejb.vo.gestionesemantica.TipoRicerca;
import it.iccu.sbn.ejb.vo.gestionesemantica.classificazione.SistemaClassificazioneVO;
import it.iccu.sbn.ejb.vo.gestionesemantica.soggetto.ricerca.SoggettarioVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.Constants;
import it.iccu.sbn.web.integration.bd.FactoryEJBDelegate;
import it.iccu.sbn.web.integration.bd.semantica.ClassiDelegate;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public final class CaricamentoComboSemantica {

	private static final String LIVELLO_MINIMO_INDICE = "05";

	public static final List<ComboCodDescVO> loadComboEdizioneDewey() throws Exception {

		List<ComboCodDescVO> lista = new ArrayList<ComboCodDescVO>();
		List<TB_CODICI> codici = CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_CLASSE);
		for (TB_CODICI cod : codici)
			lista.add(new ComboCodDescVO(cod.getCd_unimarcTrim(), cod.getDs_tabella()));

		return lista;
	}

	public static final List<SoggettarioVO> loadComboFormaNome()
			throws Exception {
		// throw new Exception();
		List<SoggettarioVO> lista = new ArrayList<SoggettarioVO>();
		SoggettarioVO ordDesc = new SoggettarioVO();
		ordDesc.setCodice("A");
		ordDesc.setDescrizione("Accettata");
		lista.add(ordDesc);
		ordDesc = new SoggettarioVO();
		ordDesc.setCodice("R");
		ordDesc.setDescrizione("Rinvio");
		lista.add(ordDesc);
		return lista;

	}

	public static final List<SistemaClassificazioneVO> loadComboOrdClasse()
			throws Exception {

		List<SistemaClassificazioneVO> lista = new ArrayList<SistemaClassificazioneVO>();
		for (TipoOrdinamentoClasse ord : TipoOrdinamentoClasse.values()) {
			SistemaClassificazioneVO elem = new SistemaClassificazioneVO();
			elem.setCodice(ord.toString());
			elem.setDescrizione(ord.getDescrizione());
			lista.add(elem);
		}
		return lista;
	}

	public static final List<SoggettarioVO> loadComboOrdDesc() throws Exception {
		List<SoggettarioVO> lista = new ArrayList<SoggettarioVO>();
		for (TipoOrdinamento ord : TipoOrdinamento.values()) {
			SoggettarioVO elem = new SoggettarioVO();
			elem.setCodice(ord.toString());
			elem.setDescrizione(ord.getDescrizione());
			lista.add(elem);
		}

		return lista;
	}

	public static final List<SoggettarioVO> loadComboOrdSogg() throws Exception {
		List<SoggettarioVO> lista = new ArrayList<SoggettarioVO>();
		for (TipoOrdinamento ord : TipoOrdinamento.values()) {
			SoggettarioVO elem = new SoggettarioVO();
			elem.setCodice(ord.toString());
			elem.setDescrizione(ord.getDescrizione());
			lista.add(elem);
		}

		return lista;
	}

	public static final List<SoggettarioVO> loadComboOrdThes() throws Exception {
		List<SoggettarioVO> lista = new ArrayList<SoggettarioVO>();
		for (TipoOrdinamento ord : TipoOrdinamento.values()) {
			SoggettarioVO elem = new SoggettarioVO();
			elem.setCodice(ord.toString());
			elem.setDescrizione(ord.getDescrizione());
			lista.add(elem);
		}

		return lista;
	}

	public static final List<SoggettarioVO> loadComboRicercaPerUnDescrittore()
			throws Exception {
		List<SoggettarioVO> lista = new ArrayList<SoggettarioVO>();
		for (PosizioneDescrittore pos : PosizioneDescrittore.values()) {
			SoggettarioVO elem = new SoggettarioVO();
			elem.setCodice(pos.toString());
			elem.setDescrizione(pos.getDescrizione());
			lista.add(elem);
		}

		return lista;
	}

	public static final List<SoggettarioVO> loadComboRicercaTipo()
			throws Exception {

		List<SoggettarioVO> lista = new ArrayList<SoggettarioVO>();
		for (TipoRicerca ord : TipoRicerca.values()) {
			SoggettarioVO elem = new SoggettarioVO();
			elem.setCodice(ord.toString());
			elem.setDescrizione(ord.getDescrizione());
			lista.add(elem);
		}
		return lista;
	}

	public static final List<ComboCodDescVO> loadComboStato(String maxLivAut) throws Exception {

		CaricamentoCombo carCombo = new CaricamentoCombo();
		List<ComboCodDescVO> lista = carCombo
				.loadComboCodiciDesc(CodiciProvider
						.getCodici(CodiciType.CODICE_LIVELLO_AUTORITA, false));
		lista = CaricamentoCombo.cutFirst(lista);
		List<ComboCodDescVO> listaFiltrata = new ArrayList<ComboCodDescVO>();

		if (maxLivAut != null)	// filtro per liv. aut
			for (ComboCodDescVO liv : lista) {
				if ((liv.getCodice().compareTo(LIVELLO_MINIMO_INDICE) < 0)
						|| (liv.getCodice().compareTo(maxLivAut) > 0))
					continue;
				listaFiltrata.add(liv);
			}
		else
			for (ComboCodDescVO liv : lista) {
				if ((liv.getCodice().compareTo(LIVELLO_MINIMO_INDICE) < 0))
					continue;
				listaFiltrata.add(liv);
			}
		return listaFiltrata;

	}

	public static final List<ComboCodDescVO> loadComboThesauro(String ticket, boolean soloGestiti) throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		CaricamentoCombo carCombo = new CaricamentoCombo();
		if (soloGestiti)
			return carCombo.loadComboCodiciDesc(factory
				.getGestioneSemantica().getCodiciThesauro(ticket));
		else
			return carCombo.loadComboCodiciDesc(factory
				.getCodici().getCodici(CodiciType.CODICE_THESAURO));

	}

	public static final List<ComboCodDescVO> loadComboSistemaClassificazioneBiblioteca(String codPolo,
			String codBib, boolean soloGestiti) throws Exception {
		return loadComboSistemaClassificazione(codPolo + codBib, soloGestiti);
	}


	public static final List<ComboCodDescVO> loadComboSistemaClassificazione(String ticket, boolean soloGestiti)
			throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		CaricamentoCombo carCombo = new CaricamentoCombo();
		if (soloGestiti) {
			List<ComboCodDescVO> lista = carCombo
					.loadComboCodiciDesc(factory.getGestioneSemantica()
							.getSistemiClassificazione(ticket));
			lista = CaricamentoCombo.cutFirst(lista);
			return lista;
		} else {
			List<ComboCodDescVO> lista = carCombo
					.loadComboCodiciDesc(CodiciProvider.getCodici(
							CodiciType.CODICE_SISTEMA_CLASSE, true));
			lista = CaricamentoCombo.cutFirst(lista);
			return lista;
		}

	}

	public static final List<ComboCodDescVO> loadComboSoggettario(String ticket, boolean soloGestiti)
			throws Exception {

		FactoryEJBDelegate factory = FactoryEJBDelegate.getInstance();

		CaricamentoCombo carCombo = new CaricamentoCombo();
		if (soloGestiti)
			return carCombo.loadComboCodiciDesc(factory.getGestioneSemantica()
					.getCodiciSoggettario(true, ticket));
		else
			return carCombo.loadComboCodiciDesc(CodiciProvider.getCodici(
					CodiciType.CODICE_SOGGETTARIO, true));

	}

	public static final List<TB_CODICI> loadComboCategoriaSogDes(SbnAuthority auth) throws Exception {
		List<TB_CODICI> codici = CodiciProvider.getCodici(CodiciType.CODICE_CATEGORIA_SOGGETTO);
		List<TB_CODICI> output = new ArrayList<TB_CODICI>();

		for (TB_CODICI cod : codici) {
			String flg2 = cod.getCd_flg2();	//tipo authority
			if (ValidazioneDati.isFilled(flg2) && !flg2.equals(auth.toString()))
				continue;

			output.add(cod);
		}
		if (ValidazioneDati.isFilled(output))
			output = CaricamentoCombo.cutFirst(output);

		return output;
	}

	public static final List<ComboCodDescVO> loadComboCategoriaSoggetto(SbnAuthority auth) throws Exception {

		List<ComboCodDescVO> output = new ArrayList<ComboCodDescVO>();

		List<TB_CODICI> codici = loadComboCategoriaSogDes(auth);
		for (TB_CODICI cod : codici)
			output.add(new ComboCodDescVO(cod.getCd_tabellaTrim(), cod.getDs_tabella()));

		return output;
	}

	private static final List<ComboCodDescVO> internalLoadComboTipoLegame(
			CodiciType type, String forma1, String forma2) throws Exception {

		List<ComboCodDescVO> lista = new ArrayList<ComboCodDescVO>();
		List<TB_CODICI> codici = CodiciProvider.getCodici(type, true);
		for (TB_CODICI cod : codici) {
			String cd_flg7 = cod.getCd_flg7();
			if (ValidazioneDati.strIsNull(cd_flg7))
				continue;

			switch (FormaNomeType.fromString(cd_flg7)) {
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
			ComboCodDescVO elem = new ComboCodDescVO();
			elem.setCodice(cod.getCd_tabella().trim());
			elem.setDescrizione(cod.getDs_tabella().trim());
			lista.add(elem);
		}

		return lista;
	}

	public static final List<ComboCodDescVO> loadComboTipoLegameDescrittoriFiltrato(
			String primaForma, String secondaForma) throws Exception {
		return internalLoadComboTipoLegame(CodiciType.CODICE_LEGAME_DESCRITTORE_DESCRITTORE, primaForma, secondaForma);
	}

	public static final List<ComboCodDescVO> loadComboTipoLegameTerminiFiltrato(
			String primaForma, String secondaForma) throws Exception {
		return internalLoadComboTipoLegame(CodiciType.CODICE_TIPO_LEGAME_TERMINI_THESAURO, primaForma, secondaForma);
	}

	public static final List<TB_CODICI> loadComboEdizioneDeweyPerGestione(HttpServletRequest request)  throws Exception {
		//almaviva5_20130128 #5238
		List<TB_CODICI> output = new ArrayList<TB_CODICI>();
		output.add(new TB_CODICI("", ""));

		UserVO utente = Navigation.getInstance(request).getUtente();

		List<String> edizioni = ClassiDelegate
				.getInstance(request).getListaSistemaEdizionePerBiblioteca(
						utente.getCodPolo(), utente.getCodBib(),
						Constants.Semantica.Classi.SISTEMA_CLASSE_DEWEY);
		if (!ValidazioneDati.isFilled(edizioni))
			return output;


		List<TB_CODICI> codici = CodiciProvider.getCodici(CodiciType.CODICE_EDIZIONE_CLASSE);
		for (TB_CODICI cod : codici) {
			String edizione = cod.getCd_tabellaTrim();
			for (String ed : edizioni)
				if (ValidazioneDati.equals(edizione, ed))
					output.add(cod);
		}

		return output;
	}

}
