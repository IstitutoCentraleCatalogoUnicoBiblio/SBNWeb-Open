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
package it.finsiel.sbn.polo.factoring.base;

import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnData;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.orm.Tb_abstract;
import it.iccu.sbn.ejb.model.unimarcmodel.A100;
import it.iccu.sbn.ejb.model.unimarcmodel.A999;
import it.iccu.sbn.ejb.model.unimarcmodel.AbstractType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

import org.apache.log4j.Category;

/**
 * @author
 *
 */
public class FormatoAbstract {

	private String tipoOrd;

	private SbnUserType user;

	// file di log
	static Category log = Category.getInstance("iccu.box.FormatoAbstract");

	public FormatoAbstract() {

	}

	public ElementAutType formattaAbstract(Tb_abstract abs,
			SbnTipoOutput tipoOut) throws IllegalArgumentException,
			InvocationTargetException, Exception {

		if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType())
			return formattaAbstractPerEsameAnalitico(abs);
		else
			return formattaAbstractPerLista(abs);
	}

	private ElementAutType formattaAbstractPerLista(Tb_abstract abs)
			throws EccezioneDB, EccezioneSbnDiagnostico, EccezioneFactoring {
		ElementAutType elemento = new ElementAutType();
		elemento.setDatiElementoAut(creaAbstractPerListaSintetica(abs));
		return elemento;
	}

	public ElementAutType formattaAbstractPerEsameAnalitico(Tb_abstract abs)
			throws IllegalArgumentException, InvocationTargetException,
			Exception {
		ElementAutType elemento = new ElementAutType();
		elemento.setDatiElementoAut(creaAbstractPerEsameAnalitico(abs));
		return elemento;
	}

	private AbstractType creaAbstractPerListaSintetica(Tb_abstract abs)
			throws EccezioneFactoring, EccezioneSbnDiagnostico, EccezioneDB {
		AbstractType datiElemento = new AbstractType();
		datiElemento.setTipoAuthority(SbnAuthority.AB);
		datiElemento.setLivelloAut(SbnLivello.valueOf(Decodificatore
				.livelloSoglia(abs.getCD_LIVELLO())));
		datiElemento.setT001(abs.getBID());
		SbnDatavar sbnDatavar = new SbnDatavar(abs.getTS_VAR());
		datiElemento.setT005(sbnDatavar.getT005Date());
		A999 t999 = new A999();
		t999.setA_999(abs.getDS_ABSTRACT());
		datiElemento.setT999(t999);
		return datiElemento;
	}

	public AbstractType creaAbstractPerEsameAnalitico(Tb_abstract abs) {
		AbstractType datiElemento = new AbstractType();
		datiElemento.setTipoAuthority(SbnAuthority.AB);
		datiElemento.setLivelloAut(SbnLivello.valueOf(Decodificatore
				.livelloSoglia(abs.getCD_LIVELLO())));
		SbnDatavar sbnDatavar = new SbnDatavar(abs.getTS_VAR());
		datiElemento.setT005(sbnDatavar.getT005Date());
		// datiElemento.setT005(abs.getTS_VAR().getDate());
		datiElemento.setT001(abs.getBID());
		A100 a100 = new A100();
		try {
			SbnData date = new SbnData(abs.getTS_INS());
			a100.setA_100_0(org.exolab.castor.types.Date.parseDate(date
					.getXmlDate()));
		} catch (ParseException ecc) {
			log.error("Formato data non corretto: abs.ts_ins:"
					+ abs.getTS_INS());
		}

		datiElemento.setT100(a100);
		A999 t999 = new A999();
		t999.setA_999(abs.getDS_ABSTRACT());
		datiElemento.setT999(t999);
		return datiElemento;
	}

}
