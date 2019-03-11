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

import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

/**
 * Classe FormatoAutoreTitolo.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 16-gen-2003
 */
public class FormatoAutoreTitolo extends FormatoAutore {

    public FormatoAutoreTitolo(
        SbnTipoOutput tipoOut,
        String tipoOrd,
        SbnUserType user) {
        super(tipoOut, tipoOrd, user);
    }
    /** formatta i rinvii di un autore. Si presuppone che l'autore di un titolo non sia un rinvio * /
    public ElementAutType formattaRinvioAutorePerTitolo(
        ElementAutType elemento,
        Tb_autore autore)
        throws EccezioneDB {
        if (autore.getTp_forma_aut().equals("R")) { //forma di rinvio
            log.error(
                "L'autore: " + autore.getVID() + " è sotto forma di rinvio");
            throw new EccezioneDB(3038);
        } else {
            TableDao rinvii = cercaRinviiAutore(autore.getVID());
            elemento.setDatiElementoAut(preparaAutore(autore));
            for (int c = 0; c < rinvii.size(); c++)
                elemento.addLegamiElementoAut(
                    formattaRinvioAutore(
                        autore,
                        (Vl_autore_aut) rinvii.get(c),
                        ((Vl_autore_aut) rinvii.get(c)).getTP_LEGAME()));
        }
        return elemento;
    }
*/
}
