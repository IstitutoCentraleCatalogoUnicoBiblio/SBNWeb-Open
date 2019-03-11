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

import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.oggetti.AllineamentoTitolo;
import it.finsiel.sbn.polo.oggetti.TitoloBiblioteca;
import it.finsiel.sbn.polo.oggetti.TitoloTitolo;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_tit_bib;
import it.finsiel.sbn.polo.orm.Tr_tit_tit;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * Classe TitoloGestisceAllineamento
 * <p>
 * Gestisce l'allineamento dei titoli.
 * Utilizza la classe TitoloAllineamento.
 * </p>
 * @author
 * @author
 *
 * @version 10-giu-03
 */
public class TitoloGestisceAllineamento extends TitoloBiblioteca {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6461874861051542515L;
	TitoloBiblioteca titBib;

    /**
     * Costruttore
     * @param conn la connessione al DB.
     */
    public TitoloGestisceAllineamento() {
        super();
        titBib = new TitoloBiblioteca();
    }


    /**
     * Provvede ad aggiornare i flag di allineamento di un titolo modificato.
     * @param flagAllineamento
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void aggiornaFlagAllineamento(AllineamentoTitolo flagAllineamento, String utente)
        throws IllegalArgumentException, InvocationTargetException, Exception {
      aggiornaFlagAllineamentoDati(flagAllineamento, utente);
      if (flagAllineamento.isModificatoTipoLegame()) {
        aggiornaFlagAllineamentoLegami(flagAllineamento.getTitolo().getBID(),utente);
      }
    }

    public void aggiornaFlagAllineamentoDati(AllineamentoTitolo flagAllineamento, String utente)
      throws IllegalArgumentException, InvocationTargetException, Exception {
      String polo = utente.substring(0,3);
        String bid = flagAllineamento.getTitolo().getBID();

        List v = titBib.cercaPerAllineamento(bid, polo);
        String prec_polo = "";
        boolean prec_polo_allinea = false;

        for (int i = 0; i < v.size(); i++) {
            Tr_tit_bib tb = (Tr_tit_bib) v.get(i);
            String curr_polo = tb.getCD_POLO();
            if (curr_polo.equals(prec_polo)) {
                if (prec_polo_allinea)
                    aggiornaFlagPolo(curr_polo, flagAllineamento, utente,tb);
            } else {
                prec_polo = curr_polo;
                if (verificaTipoAllineamento(curr_polo)) {
                    prec_polo_allinea = true;
                    aggiornaFlagPolo(curr_polo, flagAllineamento, utente,tb);
                } else {
                    prec_polo_allinea = false;
                }
            }
        }
    }

    public void aggiornaFlagAllineamentoLegami(String bid, String utente) throws IllegalArgumentException, InvocationTargetException, Exception {
      TitoloTitolo tt = new TitoloTitolo();
      List v = tt.cercaLegamiPerBidColl(bid,null);
      for (int i = 0;i<v.size();i++) {
         //aggiornaFlAllinea(utente,((Tr_tit_tit)v.get(i)).getBid_base()) ;
        Tb_titolo tit = new Tb_titolo();
        tit.setFL_CANC(" ");
        tit.setTP_MATERIALE(" ");
        tit.setBID(((Tr_tit_tit)v.get(i)).getBID_BASE());
        AllineamentoTitolo fl = new AllineamentoTitolo(tit);
        fl.setTr_tit_tit(true);
        aggiornaFlagAllineamentoDati(fl, utente);
      }
    }

    /**
     * Verifica se il tipo di allineamento relativo al polo è uguale a 1.
     * Se non si tratta di polo dovrebbe ritornare false.
     * @param polo
     * @return true se verificato
     */
    public boolean verificaTipoAllineamento(String polo) {
        Tbf_parametro par = ValidatorProfiler.getInstance().getTb_parametro(polo);
        if (par != null)
            return par.getTp_all_pref().trim().equals("1");
        else
            return false;
    }

    /**
     * Aggiorna un tr_tit_bib settando tutti i flag di allineamento nella maniera opportuna.
     * @param polo Il codice polo
     * @param flagAll oggetto allineamentoTitolo contenente le informazioni di modifica
     * @param utente Codice utente da inserire in ute_var
     * @param tb oggetto tr_tit_bib da modificare
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void aggiornaFlagPolo(String polo, AllineamentoTitolo flagAll, String utente,Tr_tit_bib tb) throws IllegalArgumentException, InvocationTargetException, Exception {
        tb.setCD_POLO(polo);
        tb.setUTE_VAR(utente);
        tb.setFL_ALLINEA(flagAll.getFlagAllinea(tb.getFL_ALLINEA()));
        tb.setFL_ALLINEA_SBNMARC(flagAll.getFlagSbnmarc());
        tb.setFL_ALLINEA_CLA(flagAll.getFlagClasse());
        tb.setFL_ALLINEA_SOG(flagAll.getFlagSoggetto());
        tb.setFL_ALLINEA_LUO(flagAll.getFlagLuogo());
        tb.setFL_ALLINEA_REP(flagAll.getFlagRepertorio());

        titBib.aggiornaTuttiFlAllinea(tb);
    }


}
