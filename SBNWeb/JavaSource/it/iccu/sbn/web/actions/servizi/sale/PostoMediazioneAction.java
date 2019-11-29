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
package it.iccu.sbn.web.actions.servizi.sale;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.UniqueIdentifiableVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi;
import it.iccu.sbn.ejb.vo.servizi.ParametriServizi.ParamType;
import it.iccu.sbn.ejb.vo.servizi.sale.GruppoPostiVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.custom.servizi.sale.CatMediazione;
import it.iccu.sbn.vo.custom.servizi.sale.Mediazione;
import it.iccu.sbn.web.actionforms.servizi.sale.PostoMediazioneForm;
import it.iccu.sbn.web.integration.action.ServiziBaseAction;
import it.iccu.sbn.web.util.CaricamentoCombo;
import it.iccu.sbn.web2.navigation.Navigation;
import it.iccu.sbn.web2.util.SbnAttivitaChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.annimon.stream.Optional;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;

public class PostoMediazioneAction extends ServiziBaseAction implements SbnAttivitaChecker {

	private static final String[] BOTTONIERA = new String[] {
			"servizi.bottone.conferma",
			"servizi.bottone.annulla" };

	@Override
	protected Map<String, String> getKeyMethodMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("servizi.bottone.conferma", "conferma");
		map.put("servizi.bottone.annulla", "annulla");

		return map;
	}

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (Navigation.getInstance(request).isFromBar())
			return mapping.getInputForward();

		init(request, form);
		loadForm(request, form);

		return mapping.getInputForward();
	}

	@Override
	protected void init(HttpServletRequest request, ActionForm form) throws Exception {
		PostoMediazioneForm currentForm = (PostoMediazioneForm) form;
		if (currentForm.isInitialized())
			return;

		super.init(request, form);
		ParametriServizi params = ParametriServizi.retrieve(request);
		currentForm.setParametri(params);

		currentForm.setPulsanti(BOTTONIERA);

		currentForm.setInitialized(true);
	}

	protected void loadForm(HttpServletRequest request, ActionForm form) throws Exception {
		PostoMediazioneForm currentForm = (PostoMediazioneForm) form;
		ParametriServizi params = ValidazioneDati.coalesce(ParametriServizi.retrieve(request), currentForm.getParametri());

		GruppoPostiVO gruppo = (GruppoPostiVO) params.get(ParamType.GRUPPO_POSTI_SALA);
		currentForm.setGruppoPosti(gruppo);

		currentForm.setCategorieMediazione(generaListaCategorie(currentForm));
	}

	private List<Mediazione> generaListaCategorie(PostoMediazioneForm currentForm) throws Exception {
		List<Mediazione> output = new ArrayList<Mediazione>();

		GruppoPostiVO gruppo = currentForm.getGruppoPosti();
		List<String> categorieMediazione = gruppo.getCategorieMediazione();
		List<TB_CODICI> codici = CodiciProvider.getCodici(CodiciType.CODICE_CATEGORIA_STRUMENTO_MEDIAZIONE);
		codici = CaricamentoCombo.cutFirst(codici);
		int size = ValidazioneDati.size(codici);
		currentForm.setSelected(new Integer[size]);

		for (int idx = 0; idx < size; idx++) {
			Optional<CatMediazione> catMed = CatMediazione.of(codici.get(idx));
			Mediazione m = new Mediazione();
			String cd_cat_mediazione = catMed.get().getCd_tabellaTrim();
			if (!isFilled(cd_cat_mediazione) )
				continue;

			m.setRichiedeSupporto(catMed.get().isRichiedeSupporto());

			if (categorieMediazione.contains(cd_cat_mediazione))
				//selezione supporti già presenti
				currentForm.getSelected()[idx] = m.getUniqueId();
			m.setCd_cat_mediazione(cd_cat_mediazione);
			m.setDescr(catMed.get().getDs_tabella());
			List<TB_CODICI> tipiMatInv = CodiciProvider.getCodiciCross(CodiciType.CODICE_CAT_STRUMENTO_MEDIAZIONE_TIPO_MAT_INV, cd_cat_mediazione, false);
			if (isFilled(tipiMatInv)) {
				List<String> supporti = new ArrayList<String>();
				for (TB_CODICI cod : tipiMatInv) {
					supporti.add(cod.getDs_tabella());
				}

				m.setSupporti(supporti);
			}

			output.add(m);
		}

		return output;
	}

	public ActionForward conferma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		PostoMediazioneForm currentForm = (PostoMediazioneForm) form;
		List<Integer> selectedItems = getMultiBoxSelectedItems(currentForm.getSelected());
		List<Mediazione> mediazioni = UniqueIdentifiableVO.search(selectedItems.toArray(new Integer[0]), currentForm.getCategorieMediazione() );

		GruppoPostiVO gruppo = currentForm.getGruppoPosti();
		List<String> categorieMediazione = gruppo.getCategorieMediazione();
		categorieMediazione.clear();
		for (Mediazione m : mediazioni)
			categorieMediazione.add(m.getCd_cat_mediazione());

		ParametriServizi params = currentForm.getParametri().copy();
		params.put(ParamType.GRUPPO_POSTI_SALA, gruppo);
		ParametriServizi.send(request, params);

		Navigation navi = Navigation.getInstance(request);
		return navi.goBack();
	}

	public ActionForward annulla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Navigation navi = Navigation.getInstance(request);
		return navi.goBack(true);
	}

	public boolean checkAttivita(HttpServletRequest request, ActionForm form, String idCheck) {
		PostoMediazioneForm currentForm = (PostoMediazioneForm) form;
		if (ValidazioneDati.equals(idCheck, "servizi.bottone.conferma")) {
			List<Mediazione> mediazioni = currentForm.getCategorieMediazione();
			return ValidazioneDati.isFilled(mediazioni);
		}

		return true;
	}

}
