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
//	SBNWeb - Rifacimento ClientServer
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006
package it.iccu.sbn.web.actions.gestionebibliografica.utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabellaEsaminaVO implements Serializable {



	private static final long serialVersionUID = -3464439256466960917L;
	Map<String, List<MyLabelValueBean>> tabellaEsamina = new HashMap<String, List<MyLabelValueBean>>();

	public List<MyLabelValueBean> getLista(String keyProva) {

		List<MyLabelValueBean> listaOut = new ArrayList<MyLabelValueBean>();
		if (tabellaEsamina.containsKey(keyProva)) {
			listaOut = tabellaEsamina.get(keyProva);
		}
		return listaOut;
	}

	public String getMyAction(String keyProva, String descProva) {

		List<MyLabelValueBean> listaOut = new ArrayList<MyLabelValueBean>();
		MyLabelValueBean oggetto = new MyLabelValueBean();

		if (tabellaEsamina.containsKey(keyProva)) {
			listaOut = tabellaEsamina.get(keyProva);
			int i;
			int maxListaOut = listaOut.size();
			for ( i=0 ; i<maxListaOut ; i++ ) {
				oggetto = listaOut.get(i);
				if (oggetto.getMyLabel().equals(descProva) ) {
					return oggetto.getMyAction();
				}
			}
		}
		return "FALLITO";
	}

	public String getMyForwardName(String keyProva, String descProva) {

		List<MyLabelValueBean> listaOut = new ArrayList<MyLabelValueBean>();
		MyLabelValueBean oggetto = new MyLabelValueBean();

		if (tabellaEsamina.containsKey(keyProva)) {
			listaOut = tabellaEsamina.get(keyProva);
			int i;
			int maxListaOut = listaOut.size();
			for ( i=0 ; i<maxListaOut ; i++ ) {
				oggetto = listaOut.get(i);
				if (oggetto.getMyLabel().equals(descProva) ) {
					return oggetto.getMyForwardName();
				}
			}
		}
		return "FALLITO";
	}

	public String getMyLivelloBaseDati(String keyProva, String descProva) {

		List<MyLabelValueBean> listaOut = new ArrayList<MyLabelValueBean>();
		MyLabelValueBean oggetto = new MyLabelValueBean();

		if (tabellaEsamina.containsKey(keyProva)) {
			listaOut = tabellaEsamina.get(keyProva);
			int i;
			int maxListaOut = listaOut.size();
			for ( i=0 ; i<maxListaOut ; i++ ) {
				oggetto = listaOut.get(i);
				if (oggetto.getMyLabel().equals(descProva) ) {
					return oggetto.getMyLivelloBaseDati();
				}
			}
		}
		return "FALLITO";
	}


	public String getMyForwardPath(String keyProva, String descProva) {

		List<MyLabelValueBean> listaOut = new ArrayList<MyLabelValueBean>();
		MyLabelValueBean oggetto = new MyLabelValueBean();

		if (tabellaEsamina.containsKey(keyProva)) {
			listaOut = tabellaEsamina.get(keyProva);
			int i;
			int maxListaOut = listaOut.size();
			for ( i=0 ; i<maxListaOut ; i++ ) {
				oggetto = listaOut.get(i);
				if (oggetto.getMyLabel().equals(descProva) ) {
					return oggetto.getMyForwardPath();
				}
			}
		}
		return "FALLITO";
	}

	public String getMyModeCall(String keyProva, String descProva) {

		List<MyLabelValueBean> listaOut = new ArrayList<MyLabelValueBean>();
		MyLabelValueBean oggetto = new MyLabelValueBean();

		if (tabellaEsamina.containsKey(keyProva)) {
			listaOut = tabellaEsamina.get(keyProva);
			int i;
			int maxListaOut = listaOut.size();
			for ( i=0 ; i<maxListaOut ; i++ ) {
				oggetto = listaOut.get(i);
				if (oggetto.getMyLabel().equals(descProva) ) {
					return oggetto.getMyModeCall();
				}
			}
		}
		return "FALLITO";
	}


	public void add(Object schema) {

		MyLabelValueBean bean = (MyLabelValueBean) schema;
		if (tabellaEsamina.containsKey(bean.getMyAction())) {
			List<MyLabelValueBean> listaOut = new ArrayList<MyLabelValueBean>();
			listaOut = tabellaEsamina.get(bean.getMyAction());
			listaOut.add(bean);
			tabellaEsamina.put(bean.getMyAction(), listaOut);
		} else {
			List<MyLabelValueBean> listaOut = new ArrayList<MyLabelValueBean>();
			listaOut.add(bean);
			tabellaEsamina.put(bean.getMyAction(), listaOut);
		}
	}

}
