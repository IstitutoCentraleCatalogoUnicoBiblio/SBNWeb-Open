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
package it.finsiel.sbn.polo.oggetti.estensione;

import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.oggetti.TitoloBiblioteca;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_mat;

import java.lang.reflect.InvocationTargetException;

/**
 * Classe TitoloValidaCancella
 * <p>
 *
 * </p>
 * @author
 * @author
 *
 * @version 20-giu-03
 */
public class TitoloValidaCancella extends TitoloValida {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5363928722031480548L;

	public TitoloValidaCancella() {
        super();
    }

    /**
     *
     * validaPerCancella:
     * - controllo l'esistenza per identificativo
     * - controllo che non esistano localizzazioni diverse dal polo dell'utente che
     *   sta cancellando (tr_tit_bib) altrimenti segnalo "Titolo localizzato in altri
     *   poli, cancellazione impossibile"
     * - controllo l'esistenza di legami inferiori.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     *
     */
    public Tb_titolo validaPerCancella(String bid, String utente, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Tb_titolo tb_titolo = verificaEsistenzaID(bid);
        if (tb_titolo == null)
            throw new EccezioneSbnDiagnostico(3013, "Titolo non esistente");
        if (tb_titolo.getCD_NATURA().equals("A"))
            throw new EccezioneSbnDiagnostico(3096, "Impossibile cancellare un titolo 'A'");

        verificaLocalizzazioniCancellazione(tb_titolo, utente);
        if (contaLegamiVersoBasso(bid) > 0)
            throw new EccezioneSbnDiagnostico(3095, "Il documento è un livello di raggruppamento");
        //timeHash.putTimestamp("Tb_titolo",tb_titolo.getBID(),tb_titolo.getTS_VAR());
        //[TODO: se si usano i timestamp si devono leggere tutte le tavole collegate.
        return tb_titolo;
    }

    /**
     * metodo che verifica se il titolo è localizzato presso il polo.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void verificaLocalizzazioniCancellazione(Tb_titolo tb_titolo, String utente)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if (!ValidatorProfiler.getInstance().isPolo(utente)) {
            return;
        }

        TitoloBiblioteca titBib = new TitoloBiblioteca();
        if (!titBib.verificaLocalizzazioniPoloUguale(tb_titolo.getBID(), utente.substring(0, 6)))
            throw new EccezioneSbnDiagnostico(3115, "Titolo non localizzato nel polo");

        if (titBib.verificaLocalizzazioniPoloDiverso(tb_titolo.getBID(), utente))
            throw new EccezioneSbnDiagnostico(
                3092,
                "Titolo localizzato in altri poli, cancellazione impossibile");
    }

    protected void verificaLivelloCancellazione(Tb_titolo tb_titolo, String utente)
        throws EccezioneSbnDiagnostico {
        Tbf_par_mat par =
        	ValidatorProfiler.getInstance().getParametriUtentePerMateriale(
                utente,
                tb_titolo.getTP_MATERIALE());
        if (par == null || par.getTp_abilitaz()!='S')
            throw new EccezioneSbnDiagnostico(3245, "Utente non abilitato a modificare il materiale");
        String livelloUtente = par.getCd_livello();
        if (Integer.parseInt(tb_titolo.getCD_LIVELLO()) > Integer.parseInt(livelloUtente))
            throw new EccezioneSbnDiagnostico(3007, "Livello di autorità utente non consente l'operazione");



        if (!livelloUtente.equals("97")) {
            if (tb_titolo.getCD_LIVELLO().equals("90")
                || tb_titolo.getCD_LIVELLO().equals("95")
                || tb_titolo.getCD_LIVELLO().equals("97"))
//           Inizio bug mantis 2674 (controllo su polo e non su biblioteca) COPIATO DALL'OGGETTO DI INDICE
//                if (!utente.equals(tb_titolo.getUTE_VAR()))
            	 if (!utente.startsWith(tb_titolo.getUTE_VAR().substring(0,3)))
                    throw new EccezioneSbnDiagnostico(3116, "Titolo portato a max da altro utente");
//       	 Fine bug mantis 2674 (controllo su polo e non su biblioteca) COPIATO DALL'OGGETTO DI INDICE
        }
        if (tb_titolo.getCD_NATURA().equals("N"))
            if (ValidatorProfiler.getInstance().getTb_parametro(utente).getFl_spogli()!='S')
                throw new EccezioneSbnDiagnostico(3244, "Impossibile modificare natura N");
    }

}
