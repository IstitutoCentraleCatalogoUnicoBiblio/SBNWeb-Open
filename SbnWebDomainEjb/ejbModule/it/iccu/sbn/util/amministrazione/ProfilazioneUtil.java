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
package it.iccu.sbn.util.amministrazione;

import static org.hamcrest.Matchers.equalTo;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.GruppoParametriVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.ParametroVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.profilazionepolo.ParametroVO.ParametroType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_par_mat;
import it.iccu.sbn.vo.custom.amministrazione.MergedParAut;
import it.iccu.sbn.vo.custom.amministrazione.MergedParMat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.setToMap;


public class ProfilazioneUtil {

	public enum Origine {
		POLO,
		BIBLIOTECA,
		UTENTE;
	}

	public static class OrigineParametro<T> {

		final T value;
		final Origine origine;

		public OrigineParametro(T value, Origine origine) {
			super();
			this.value = value;
			this.origine = origine;
		}

	}


	public static final <T extends Comparable<T>> OrigineParametro<T> minP(T max, T polo, T bib, T ute) {
		if (polo.compareTo(bib) < 0)
			return new OrigineParametro<T>(polo, Origine.POLO);

		boolean isBib = (ute == null);
		if (!isBib) {	//utente
			if (bib.compareTo(ute) < 0)
				return new OrigineParametro<T>(bib, Origine.BIBLIOTECA);

			if (bib.compareTo(max) < 0)
				return new OrigineParametro<T>(bib, Origine.BIBLIOTECA);

			return new OrigineParametro<T>(ute, Origine.UTENTE);
		}

		//biblioteca
		if (polo.compareTo(max) < 0)
			return new OrigineParametro<T>(polo, Origine.POLO);

		return new OrigineParametro<T>(bib, Origine.BIBLIOTECA);
	}

	public static final <T extends Comparable<T>> OrigineParametro<T> maxP(T min, T polo, T bib, T ute) {
		if (polo.compareTo(bib) > 0)
			return new OrigineParametro<T>(polo, Origine.POLO);

		boolean isBib = (ute == null);
		if (!isBib) {	//utente
			if (bib.compareTo(ute) > 0)
				return new OrigineParametro<T>(bib, Origine.BIBLIOTECA);

			if (bib.compareTo(min) > 0)
				return new OrigineParametro<T>(bib, Origine.BIBLIOTECA);

			return new OrigineParametro<T>(ute, Origine.UTENTE);
		}

		//biblioteca
		if (polo.compareTo(min) > 0)
			return new OrigineParametro<T>(polo, Origine.POLO);

		return new OrigineParametro<T>(bib, Origine.BIBLIOTECA);
	}

	public static final List<MergedParMat> mergeParMat(Set<Tbf_par_mat> polo, Set<Tbf_par_mat> bib, Set<Tbf_par_mat> user) {
		Map<Character, Tbf_par_mat> matPolo = setToMap(polo, Character.class, "cd_par_mat");
		Map<Character, Tbf_par_mat> matBibl = setToMap(bib, Character.class, "cd_par_mat");
		Map<Character, Tbf_par_mat> matUser = null;
		boolean isBib = (user == null);
		if (isBib)
			matUser = matBibl;
		else
			matUser = setToMap(user, Character.class, "cd_par_mat");

		List<MergedParMat> out = new ArrayList<MergedParMat>();
		for (Tbf_par_mat parUser : matUser.values()) {

			char mat = parUser.getCd_par_mat();
			Tbf_par_mat parPolo = matPolo.get(mat);
			Tbf_par_mat parBibl = matBibl.get(mat);

			MergedParMat merged = new MergedParMat();
			merged.setCd_par_mat(mat);
			merged.setCd_contr_sim("1");

			/*
				il profilo viene unificato in cascata dando precedenza ai profili di polo e biblioteca.
				Si sfrutta l'ordinamento ASCII dei campi in tabella. Esempio:
					min('S', 'N')='N'
					max('05', '71')='71'
			*/
			OrigineParametro<Character> op1 = minP('S', parPolo.getTp_abilitaz(), parBibl.getTp_abilitaz(), isBib ? null : parUser.getTp_abilitaz());
			merged.setTp_abilitaz(op1.value);
			merged.setTp_abilitaz_Origine(op1.origine);

			OrigineParametro<Character> op2 = minP('S', parPolo.getFl_abil_forzat(), parBibl.getFl_abil_forzat(), isBib ? null : parUser.getFl_abil_forzat());
			merged.setFl_abil_forzat(op2.value);
			merged.setFl_abil_forzat_Origine(op2.origine);

			//almaviva5_20140908 #5637
//			OrigineParametro<String> op3 = minP("97", parPolo.getCd_livello(), parBibl.getCd_livello(), isBib ? null : parUser.getCd_livello());
//			merged.setCd_livello(op3.value);
//			merged.setCd_livello_Origine(op3.origine);
			String liv = ValidazioneDati.min(parPolo.getCd_livello(), parBibl.getCd_livello(), isBib ? null : parUser.getCd_livello());
			merged.setCd_livello(liv);
			merged.setCd_livello_Origine(Origine.UTENTE);

			OrigineParametro<Character> op4 = maxP('N', parPolo.getSololocale(), parBibl.getSololocale(), isBib ? null : parUser.getSololocale());
			merged.setSololocale(op4.value);
			merged.setSololocale_Origine(op4.origine);

			out.add(merged);
		}

		return out;
	}

	public static final List<MergedParAut> mergeParAuth(Set<Tbf_par_auth> polo, Set<Tbf_par_auth> bib, Set<Tbf_par_auth> user) {
		Map<String, Tbf_par_auth> authPolo = setToMap(polo, String.class, "cd_par_auth");
		Map<String, Tbf_par_auth> authBibl = setToMap(bib, String.class, "cd_par_auth");
		Map<String, Tbf_par_auth> authUser = null;
		boolean isBib = (user == null);
		if (isBib)
			authUser = authBibl;
		else
			authUser = setToMap(user, String.class, "cd_par_auth");

		List<MergedParAut> out = new ArrayList<MergedParAut>();
		for (Tbf_par_auth parUser : authUser.values()) {

			String auth = parUser.getCd_par_auth();
			Tbf_par_auth parPolo = authPolo.get(auth);
			Tbf_par_auth parBibl = authBibl.get(auth);

			MergedParAut merged = new MergedParAut();
			merged.setCd_par_auth(auth);
			merged.setCd_contr_sim("1");
			/*
				il profilo viene unificato in cascata dando precedenza ai profili di polo e biblioteca.
				Si sfrutta l'ordinamento ASCII dei campi in tabella. Esempio:
					min('S', 'N')='N'
					max('05', '71')='71'
			*/
			OrigineParametro<Character> op1 = minP('S', parPolo.getTp_abil_auth(), parBibl.getTp_abil_auth(), isBib ? null : parUser.getTp_abil_auth());
			merged.setTp_abil_auth(op1.value);
			merged.setTp_abil_auth_Origine(op1.origine);

			OrigineParametro<Character> op2 = minP('S', parPolo.getFl_abil_legame(), parBibl.getFl_abil_legame(), isBib ? null : parUser.getFl_abil_legame());
			merged.setFl_abil_legame(op2.value);
			merged.setFl_abil_legame_Origine(op2.origine);

			OrigineParametro<Character> op3 = minP('S', parPolo.getFl_leg_auth(), parBibl.getFl_leg_auth(), isBib ? null : parUser.getFl_leg_auth());
			merged.setFl_leg_auth(op3.value);
			merged.setFl_leg_auth_Origine(op3.origine);

			//almaviva5_20140908 #5637
//			OrigineParametro<String> op4 = minP("97", parPolo.getCd_livello(), parBibl.getCd_livello(), isBib ? null : parUser.getCd_livello());
//			merged.setCd_livello(op4.value);
//			merged.setCd_livello_Origine(op4.origine);
			String liv = ValidazioneDati.min(parPolo.getCd_livello(), parBibl.getCd_livello(), isBib ? null : parUser.getCd_livello());
			merged.setCd_livello(liv);
			merged.setCd_livello_Origine(Origine.UTENTE);

			OrigineParametro<Character> op5 = minP('S', parPolo.getFl_abil_forzat(), parBibl.getFl_abil_forzat(), isBib ? null : parUser.getFl_abil_forzat());
			merged.setFl_abil_forzat(op5.value);
			merged.setFl_abil_forzat_Origine(op5.origine);

			OrigineParametro<Character> op6 = maxP('N', parPolo.getSololocale(), parBibl.getSololocale(), isBib ? null : parUser.getSololocale());
			merged.setSololocale(op6.value);
			merged.setSololocale_Origine(op6.origine);

			out.add(merged);
		}

		return out;
	}

	public static final List<TB_CODICI> livelloSoglia(List<TB_CODICI> values, String soglia) {
		List<TB_CODICI> output = new ArrayList<TB_CODICI>(values);
		Iterator<TB_CODICI> i = output.iterator();
		while (i.hasNext() ) {
			String cd_tabella = i.next().getCd_tabella().trim();
			if (cd_tabella.compareTo(soglia) > 0)
				i.remove();
		}
		return output;
	}

	public static final void resetLivelloParametri(List<GruppoParametriVO> parametri) {
		if (!ValidazioneDati.isFilled(parametri))
			return;

		//almaviva5_20140702 segnalazione PAL: i parametri per i nuovi utenti vanno impostati a livello 01
		ParametroVO p = on(ParametroVO.class);
		for (GruppoParametriVO grp : parametri) {
			List<ParametroVO> livelli = select(grp.getElencoParametri(),
					having(p.getType(), equalTo(ParametroType.LIVELLO_AUTORITA)));
			for (ParametroVO lvl : livelli)
				lvl.setSelezione("01");
		}
	}

}
