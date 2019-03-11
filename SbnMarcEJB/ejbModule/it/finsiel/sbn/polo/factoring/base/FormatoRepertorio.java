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

import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.iccu.sbn.ejb.model.unimarcmodel.A930;
import it.iccu.sbn.ejb.model.unimarcmodel.RepertorioType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoRepertorio;

/**
 * Classe FormatoRepertorio.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 11-mar-03
 */
public class FormatoRepertorio {

    //Vl_repertorio_tyep
    public RepertorioType formattaRepertorioPerEsame(Tb_repertorio rep) {
        RepertorioType dati = new RepertorioType();
        A930 a930 = new A930();
        a930.setA_930(rep.getDS_REPERTORIO());
        a930.setB_930(TipoRepertorio.valueOf(rep.getTP_REPERTORIO()));
        a930.setC2_930(rep.getCD_SIG_REPERTORIO());
        dati.setT930(a930);
        dati.setTipoAuthority(SbnAuthority.RE);
        dati.setLivelloAut(SbnLivello.valueOf("97"));
        dati.setT001(rep.getCD_SIG_REPERTORIO());
        return dati;
    }

}
