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
package it.finsiel.sbn.polo.oggetti;

import it.finsiel.sbn.polo.orm.Tb_titolo;

/**
 * Classe AllineamentoTitolo
 * <p>
 * Oggetto di utilità per gestire l'allineamento di un titolo.
 * Contiene le informazioni di cosa è stato modificato relativamente al titolo.
 * </p>
 * @author
 * @author
 *
 * @version 10-giu-03
 */
public class AllineamentoTitolo {
    private Tb_titolo titolo;
    private boolean materiale = false;
    private boolean natura = false;
    private boolean tp_legame = false;
    private boolean tb_titolo = false;
    private boolean tb_impronta = false;
    private boolean tb_numero_std = false;
    private boolean tb_musica = false;
    private boolean tb_grafica = false;
    private boolean tb_cartografia = false;

    private boolean tb_audiovideo = false;
    private boolean tb_risorsa_elettr = false;
    private boolean tb_disco_sonoro = false;

    private boolean tb_composizione = false;
    private boolean tb_incipit = false;
    private boolean tb_rappresentazione = false;
    private boolean tb_personaggio = false;
    private boolean tb_nota = false;
    private boolean tr_tit_tit = false;
    private boolean tr_tit_aut = false;
    private boolean tr_tit_mar = false;
    private boolean tr_tit_luo = false;
    private boolean tr_rep_tit = false;
    private boolean tr_tit_sog = false;
    private boolean tr_tit_cla = false;
    private boolean trs_termini_titoli_biblioteche = false;

    public AllineamentoTitolo(Tb_titolo titolo) {
        this.titolo = titolo;
    }

    /**
     * Controlla se sono stati modificati i dati
     * @return true se sono avvenute modifiche dei dati
     */
    public boolean isModificatoDati() {
        if (tb_titolo || tb_impronta || tb_numero_std)
            return true;
        if (titolo.getTP_MATERIALE().equals("E") && tr_tit_luo)
            return true;
        return false;
    }

    /**
     * Controlla se sono stati modificati i legami
     * @return true se sono avvenute modifiche dei legami
     */
    public boolean isModificatoLegami() {
        if (tr_tit_tit || tr_tit_aut || tr_tit_mar)
            return true;
        return false;
    }

    /**
     * Controlla se sono stati modificati i tipi di legami
     * @return true se sono avvenute modifiche
     */
    public boolean isModificatoTipoLegame() {
        return tp_legame;
    }


    /**
     * Controlla se il titolo ha subito una cancellazione
     * @return
     */
    public boolean isCancellato() {
        if (titolo.getFL_CANC().equals("S"))
            return isModificatoDati();
        return false;
    }

    /**
     * Calcola il valore del flag di allineamento sbnmarc
     * @return il valore del flag
     */
    public String getFlagSbnmarc() {
        if (tb_musica
            || tb_grafica
            || tb_cartografia
            || tb_composizione
            || tb_incipit
            || tb_rappresentazione
            || tb_personaggio
            || tb_nota)
            return "S";
        if (materiale)
            return "M";
        if (tr_tit_luo || tr_rep_tit || tr_tit_sog || tr_tit_cla || trs_termini_titoli_biblioteche)
            return "A";
        return " ";
    }

    /**
     * Calcola il valore del flag di allineamento classe
     * @return il valore del flag
     */
    public String getFlagClasse() {
        if (tr_tit_cla)
            return "S";
        return " ";
    }

    public String getFlagThesauro() {
        if (trs_termini_titoli_biblioteche)
            return "S";
        return " ";
    }

    /**
     * Calcola il valore del flag di allineamento soggetto
     * @return il valore del flag
     */
    public String getFlagSoggetto() {
        if (tr_tit_sog)
            return "S";
        return " ";
    }

    /**
     * Calcola il valore del flag di allineamento luogo
     * @return il valore del flag
     */
    public String getFlagLuogo() {
        if (tr_tit_luo)
            return "S";
        return " ";
    }

    /**
     * Calcola il valore del flag di allineamento repertorio
     * @return il valore del flag
     */
    public String getFlagRepertorio() {
        if (tr_rep_tit)
            return "S";
        return " ";
    }

    /**
     * Calcola il valore del flag di allineamento
     * @return il valore del flag
     */
    public String getFlagAllinea(String flag_stato) {
        if (isCancellato()) {
            return "S";
        }
        if (flag_stato.equals(" ")) {
            if (isModificatoLegami())
                return "C";
            if (isModificatoDati())
                return "S";
        } else if (flag_stato.equals("S")) {
            if (isModificatoLegami())
                return "Z";
        } else if (flag_stato.equals("C")) {
            if (isModificatoDati())
                return "Z";
        }
        if (natura){
          return "X";
        }
        //Ogni modifica aggiorna almeno tb_titolo, quindi qua non ci dovrebbe arrivare.
        return flag_stato;
    }

    /**
     * @return
     */
    public Tb_titolo getTitolo() {
        return titolo;
    }

    /**
     * @param tb_titolo
     */
    public void setTitolo(Tb_titolo tb_titolo) {
        titolo = tb_titolo;
    }

    /**
     * @param b
     */
    public void setMateriale(boolean b) {
        materiale = b;
    }

    /**
     * @param b
     */
    public void setNatura(boolean b) {
        natura = b;
    }

    /**
     * @param b
     */
    public void setTp_legame(boolean b) {
        tp_legame = b;
    }

    /**
     * @param b
     */
    public void setTb_cartografia(boolean b) {
        tb_cartografia = b;
    }

    /**
     * @param b
     */
    public void setTb_composizione(boolean b) {
        tb_composizione = b;
    }

    /**
     * @param b
     */
    public void setTb_grafica(boolean b) {
        tb_grafica = b;
    }

    /**
     * @param b
     */
    public void setTb_impronta(boolean b) {
        tb_impronta = b;
    }

    /**
     * @param b
     */
    public void setTb_incipit(boolean b) {
        tb_incipit = b;
    }

    /**
     * @param b
     */
    public void setTb_musica(boolean b) {
        tb_musica = b;
    }

    /**
     * @param b
     */
    public void setTb_nota(boolean b) {
        tb_nota = b;
    }

    /**
     * @param b
     */
    public void setTb_numero_std(boolean b) {
        tb_numero_std = b;
    }

    /**
     * @param b
     */
    public void setTb_personaggio(boolean b) {
        tb_personaggio = b;
    }

    /**
     * @param b
     */
    public void setTb_rappresentazione(boolean b) {
        tb_rappresentazione = b;
    }

    /**
     * @param b
     */
    public void setTb_titolo(boolean b) {
        tb_titolo = b;
    }

    /**
     * @param b
     */
    public void setTr_rep_tit(boolean b) {
        tr_rep_tit = b;
    }

    /**
     * @param b
     */
    public void setTr_tit_aut(boolean b) {
        tr_tit_aut = b;
    }

    /**
     * @param b
     */
    public void setTr_tit_cla(boolean b) {
        tr_tit_cla = b;
    }

    public void setTrs_termini_titoli_biblioteche(boolean b) {
        trs_termini_titoli_biblioteche = b;
    }

    /**
     * @param b
     */
    public void setTr_tit_luo(boolean b) {
        tr_tit_luo = b;
    }

    /**
     * @param b
     */
    public void setTr_tit_mar(boolean b) {
        tr_tit_mar = b;
    }

    /**
     * @param b
     */
    public void setTr_tit_sog(boolean b) {
        tr_tit_sog = b;
    }

    /**
     * @param b
     */
    public void setTr_tit_tit(boolean b) {
        tr_tit_tit = b;
    }

	public void setTb_audiovideo(boolean tbAudiovideo) {
		tb_audiovideo = tbAudiovideo;
	}

	public void setTb_risorsa_elettr(boolean tbRisorsaElettr) {
		tb_risorsa_elettr = tbRisorsaElettr;
	}

	public void setTb_disco_sonoro(boolean tbDiscoSonoro) {
		tb_disco_sonoro = tbDiscoSonoro;
	}

}
