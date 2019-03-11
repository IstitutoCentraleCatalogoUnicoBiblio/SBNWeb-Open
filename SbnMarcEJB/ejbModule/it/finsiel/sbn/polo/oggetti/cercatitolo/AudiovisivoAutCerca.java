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
package it.finsiel.sbn.polo.oggetti.cercatitolo;

import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.common.viste.Ve_audiovisivo_aut_autResult;
import it.finsiel.sbn.polo.dao.common.viste.Vl_audiovisivo_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_cartografia_cla_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_cartografia_luo_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_cartografia_mar_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_cartografia_sog_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_cartografia_tit_c_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vf_cartografia_autResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.ChiaviAutore;
import it.finsiel.sbn.polo.factoring.transactionmaker.CercaTitolo;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Formattazione;
import it.finsiel.sbn.polo.factoring.util.SimboloDewey;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.viste.Ve_audiovisivo_aut_aut;
import it.finsiel.sbn.polo.orm.viste.Ve_cartografia_aut_aut;
import it.finsiel.sbn.polo.orm.viste.Ve_cartografia_cla_aut;
import it.finsiel.sbn.polo.orm.viste.Ve_cartografia_luo_aut;
import it.finsiel.sbn.polo.orm.viste.Ve_cartografia_mar_aut;
import it.finsiel.sbn.polo.orm.viste.Ve_cartografia_sog_aut;
import it.finsiel.sbn.polo.orm.viste.Ve_cartografia_tit_c_aut;
import it.finsiel.sbn.polo.orm.viste.Vf_cartografia_aut;
import it.finsiel.sbn.polo.orm.viste.Vl_audiovisivo_aut;
import it.iccu.sbn.ejb.model.unimarcmodel.CanaliCercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaTitoloType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementoAutLegatoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Category;

/**
 * Classe AudiovisivoAutCerca
 * <p>
 * Esegue quelle ricerche di titoli che valorizzano i filtri relativi ad un autore
 * Utilizza le viste di tipo VE , VL e VF.
 * Estende la classe TitoloCartografia, dalla quale eredita diversi metodi di utilità e la possibilità
 * di valorizzare i filtri sul titolo cercato.
 * </p>
 * @author Akros Informatica s.r.l.
 * @author Ragazzini Taymer
 *
 * @version 22-mag-03
 */
public class AudiovisivoAutCerca extends TitoloAudiovisivo {

	private static final long serialVersionUID = -3182131431514443732L;

	private static Category log = Category.getInstance("iccu.serversbnmarc.AudiovisivoAutCerca");
    private String ord;
    private CercaTitoloType cercaTitolo;
    private CercaTitolo factoring;

    /** Costruttore, la connessione al db serve per tutti gli accessi al DB */
    public AudiovisivoAutCerca(
        CercaTitolo factoring,
        CercaTitoloType cercaTitolo,
        String ord) {

        this.ord = ord;
        this.cercaTitolo = cercaTitolo;
        this.factoring = factoring;
    }

    /**Esegue la ricerca del titolo discriminando quale vista utilizzare.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitolo() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (cercaTitolo.getArrivoLegame() != null) {
            LegameElementoAutType legameElemento = cercaTitolo.getArrivoLegame().getLegameElementoAut();
            if (legameElemento != null) {
                if (legameElemento.getTipoAuthority().getType() == SbnAuthority.valueOf("AU").getType()) {
                    return cercaTitoloPerAutore();
                } else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.valueOf("LU").getType()) {
                    return cercaTitoloPerLuogo();
                } else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.valueOf("MA").getType()) {
                    return cercaTitoloPerMarca();
                } else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.valueOf("SO").getType()) {
                    return cercaTitoloPerSoggetto();
                } else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.valueOf("CL").getType()) {
                    return cercaTitoloPerClasse();
                } else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.valueOf("TU").getType()) {
                    return cercaTitoloPerLegameTitolo(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());
                } else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.valueOf("UM").getType()) {
                    //[TODO da sistemare il titolo uniforme e musicale
                    return cercaTitoloPerLegameTitolo(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());
                }
            } else if (cercaTitolo.getArrivoLegame().getLegameDoc() != null) {
                return cercaTitoloPerLegameTitolo(cercaTitolo.getArrivoLegame().getLegameDoc().getIdArrivo());
            } else if (cercaTitolo.getArrivoLegame().getLegameTitAccesso() != null) {
                return cercaTitoloPerLegameTitolo(cercaTitolo.getArrivoLegame().getLegameTitAccesso().getIdArrivo());
            }

        } else { //non ho l'arrivo legame, uso solo i filtri su autore.
            return cercaTitoloConFiltriAutore();
        }
        return null;
    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_cartografia_aut
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerSoggetto() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ve_cartografia_sog_aut tit = new Ve_cartografia_sog_aut();
        Ve_cartografia_sog_autResult tavola = new Ve_cartografia_sog_autResult(tit);
        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        tit.setCID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());

        factoring.controllaNumeroRecord(conta(tavola, "countPerSoggetto"));

        tavola.executeCustom("selectPerSoggetto", ord);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_cartografia_aut
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerAutore() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (cercaTitolo.getCercaDatiTit() == null
            || cercaTitolo.getCercaDatiTit().getElementoAutLegato() == null) {
            Vl_audiovisivo_aut aud = new Vl_audiovisivo_aut();
            Vl_audiovisivo_autResult tavola = new Vl_audiovisivo_autResult(aud);
            aud.setVID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());
            valorizzaFiltri(aud, cercaTitolo.getCercaDatiTit());

            factoring.controllaNumeroRecord(conta(tavola, "countPerAutore"));

            tavola.executeCustom("selectPerAutore", ord);
            return tavola;
        } else {
            Ve_audiovisivo_aut_aut aud = new Ve_audiovisivo_aut_aut();
            Ve_audiovisivo_aut_autResult tavola = new Ve_audiovisivo_aut_autResult(aud);
            aud.setVID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());
            valorizzaFiltri(aud, cercaTitolo.getCercaDatiTit());

            factoring.controllaNumeroRecord(conta(tavola, "countPerAutore"));

            tavola.executeCustom("selectPerAutore", ord);
            return tavola;
        }
    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * Cerca per stringaEsatta, stringaLike o Clet.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * */
    protected TableDao cercaTitoloConFiltriAutore() throws IllegalArgumentException, InvocationTargetException, Exception {
        StringaCercaType stringaCerca =
            cercaTitolo.getCercaDatiTit().getCercaDatiTitTypeChoice().getTitoloCerca().getStringaCerca();
        if (stringaCerca != null) {
        	StringaCercaTypeChoice choice = stringaCerca.getStringaCercaTypeChoice();
            if (choice.getStringaEsatta() != null)
                return cercaTitoloPerNomeEsatto();
            else if (choice.getStringaLike() != null)
                return cercaTitoloPerNomeLike();
            else
                throw new EccezioneSbnDiagnostico(3040);
        } else if (
            cercaTitolo.getCercaDatiTit().getCercaDatiTitTypeChoice().getTitoloCerca().getTitoloCLET()
                != null) {
            //Se arrivo qui è da solo, altrimenti è da utilizzarsi in combinazione
            //con altri elementi
            return cercaTitoloPerClet(
                cercaTitolo.getCercaDatiTit().getCercaDatiTitTypeChoice().getTitoloCerca().getTitoloCLET());
        }
        throw new EccezioneSbnDiagnostico(3001, "Ricerca fallita");
    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerClet(String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
        Vf_cartografia_aut tit = new Vf_cartografia_aut();
        Vf_cartografia_autResult tavola = new Vf_cartografia_autResult(tit);

        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        tit.settaParametro("stringaClet", clet);
        factoring.controllaNumeroRecord(conta(tavola, "countPerClet"));

        tavola.executeCustom("selectPerClet", ord);
        return tavola;

    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerNomeLike() throws IllegalArgumentException, InvocationTargetException, Exception {
        Vf_cartografia_aut tit = new Vf_cartografia_aut();
        Vf_cartografia_autResult tavola = new Vf_cartografia_autResult(tit);

        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        factoring.controllaNumeroRecord(conta(tavola, "countPerNomeLike"));

        tavola.executeCustom("selectPerNomeLike", ord);
        return tavola;

    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo esatto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerNomeEsatto() throws IllegalArgumentException, InvocationTargetException, Exception {
        Vf_cartografia_aut tit = new Vf_cartografia_aut();
        Vf_cartografia_autResult tavola = new Vf_cartografia_autResult(tit);

        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        factoring.controllaNumeroRecord(conta(tavola, "countPerNomeEsatto"));

        tavola.executeCustom("selectPerNomeEsatto", ord);
        return tavola;

    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_cartografia_aut
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerLuogo() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ve_cartografia_luo_aut tit = new Ve_cartografia_luo_aut();
        Ve_cartografia_luo_autResult tavola = new Ve_cartografia_luo_autResult(tit);

        tit.setLID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());
        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        factoring.controllaNumeroRecord(conta(tavola, "countPerLuogo"));

        tavola.executeCustom("selectPerLuogo", ord);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_cartografia_aut
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerLegameTitolo(String bid/*String cd_natura*/) throws IllegalArgumentException, InvocationTargetException, Exception {
        Ve_cartografia_tit_c_aut tit = new Ve_cartografia_tit_c_aut();
        Ve_cartografia_tit_c_autResult tavola = new Ve_cartografia_tit_c_autResult(tit);

        tit.setBID(bid);
        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        factoring.controllaNumeroRecord(conta(tavola, "countPerTitolo"));

        tavola.executeCustom("selectPerTitolo", ord);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_cartografia_aut
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerLegameDoc() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ve_cartografia_tit_c_aut tit = new Ve_cartografia_tit_c_aut();
        Ve_cartografia_tit_c_autResult tavola = new Ve_cartografia_tit_c_autResult(tit);

        tit.setBID(cercaTitolo.getArrivoLegame().getLegameDoc().getIdArrivo());
        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        factoring.controllaNumeroRecord(conta(tavola, "countPerTitolo"));

        tavola.executeCustom("selectPerTitolo", ord);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_cartografia_aut
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerClasse() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ve_cartografia_cla_aut tit = new Ve_cartografia_cla_aut();
        Ve_cartografia_cla_autResult tavola = new Ve_cartografia_cla_autResult(tit);
        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        String chiave = cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo();
        if (chiave.length() < 3)
            throw new EccezioneSbnDiagnostico(3071, "Identificativo classe non corretto");

        //almaviva5_20150217
    	String sistema = null;
    	String edizione = null;
    	String simbolo = null;

    	//almaviva5_20141114 edizioni ridotte
        SimboloDewey sd = SimboloDewey.parse(chiave);
        if (sd.isDewey()) {
        	edizione = Decodificatore.convertUnimarcToSbn("ECLA", sd.getEdizione());
        	if (!ValidazioneDati.isFilled(edizione))
            	throw new EccezioneSbnDiagnostico(7022);	//edizione non esistente

        	sistema = sd.getSistema() + edizione;
        } else {
        	sistema = sd.getSistema();
        	edizione = sd.getEdizione();
        }
    	simbolo = sd.getSimbolo();

		tit.setCD_SISTEMA(sistema);
		tit.setCD_EDIZIONE(edizione);
		tit.setCLASSE(simbolo);

        factoring.controllaNumeroRecord(conta(tavola, "countPerClasse"));

        tavola.executeCustom("selectPerClasse", ord);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_cartografia_aut
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerMarca() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ve_cartografia_mar_aut tit = new Ve_cartografia_mar_aut();
        Ve_cartografia_mar_autResult tavola = new Ve_cartografia_mar_autResult(tit);
        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        tit.setMID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());

        factoring.controllaNumeroRecord(conta(tavola, "countPerMarca"));

        tavola.executeCustom("selectPerMarca", ord);
        return tavola;
    }

    protected void valorizzaFiltri(Vf_cartografia_aut autore, CercaDatiTitType cerca)
        throws EccezioneSbnDiagnostico {
        super.valorizzaFiltri(autore, cerca);
        ElementoAutLegatoType elAut = cerca.getElementoAutLegato();
        if (elAut != null) {
            if (elAut.getRelatorCode() != null)
                autore.setCD_RELAZIONE(
                    Decodificatore.getCd_tabella(
                        "Tr_tit_aut",
                        "cd_relazione",
                        elAut.getRelatorCode().toString()));
            if (elAut.getTipoRespons() != null)
                autore.setTP_RESPONSABILITA(
                    Decodificatore.getCd_tabella(
                        "Tr_tit_aut",
                        "tp_responsabilita",
                        elAut.getTipoRespons().toString()));
            if (elAut.getChiaviAutoreCerca() != null) {
                autore.setKY_AUTEUR(elAut.getChiaviAutoreCerca().getAutoreAUTEUR());
                autore.setKY_CAUTUN(elAut.getChiaviAutoreCerca().getAutoreCAUT());
            }
            valorizzaFiltriSpecifici(autore,elAut);
        }
    }

    protected void valorizzaFiltri(Ve_cartografia_aut_aut autore, CercaDatiTitType cerca)
        throws EccezioneSbnDiagnostico {
        super.valorizzaFiltri(autore, cerca);
        ElementoAutLegatoType elAut = cerca.getElementoAutLegato();
        if (elAut != null) {
            if (elAut.getRelatorCode() != null)
                autore.setCD_RELAZIONE(
                    Decodificatore.getCd_tabella(
                        "Tr_tit_aut",
                        "cd_relazione",
                        elAut.getRelatorCode().toString()));
            if (elAut.getTipoRespons() != null)
                autore.setTP_RESPONSABILITA(
                    Decodificatore.getCd_tabella(
                        "Tr_tit_aut",
                        "tp_responsabilita",
                        elAut.getTipoRespons().toString()));
            if (elAut.getChiaviAutoreCerca() != null) {
                autore.setKY_AUTEUR_F(elAut.getChiaviAutoreCerca().getAutoreAUTEUR());
                autore.setKY_CAUTUN_F(elAut.getChiaviAutoreCerca().getAutoreCAUT());
            }
            valorizzaFiltriSpecifici(autore,elAut);
        }
    }

    protected void valorizzaFiltri(Ve_cartografia_luo_aut autore, CercaDatiTitType cerca)
        throws EccezioneSbnDiagnostico {
        super.valorizzaFiltri(autore, cerca);
        ElementoAutLegatoType elAut = cerca.getElementoAutLegato();
        if (elAut != null) {
            if (elAut.getRelatorCode() != null)
                autore.setCD_RELAZIONE(
                    Decodificatore.getCd_tabella(
                        "Tr_tit_aut",
                        "cd_relazione",
                        elAut.getRelatorCode().toString()));
            if (elAut.getTipoRespons() != null)
                autore.setTP_RESPONSABILITA(
                    Decodificatore.getCd_tabella(
                        "Tr_tit_aut",
                        "tp_responsabilita",
                        elAut.getTipoRespons().toString()));
            if (elAut.getChiaviAutoreCerca() != null) {
                autore.setKY_AUTEUR(elAut.getChiaviAutoreCerca().getAutoreAUTEUR());
                autore.setKY_CAUTUN(elAut.getChiaviAutoreCerca().getAutoreCAUT());
            }
            valorizzaFiltriSpecifici(autore,elAut);
        }
    }

    protected void valorizzaFiltri(Ve_cartografia_mar_aut autore, CercaDatiTitType cerca)
        throws EccezioneSbnDiagnostico {
        super.valorizzaFiltri(autore, cerca);
        ElementoAutLegatoType elAut = cerca.getElementoAutLegato();
        if (elAut != null) {
            if (elAut.getRelatorCode() != null)
                autore.setCD_RELAZIONE(
                    Decodificatore.getCd_tabella(
                        "Tr_tit_aut",
                        "cd_relazione",
                        elAut.getRelatorCode().toString()));
            if (elAut.getTipoRespons() != null)
                autore.setTP_RESPONSABILITA(
                    Decodificatore.getCd_tabella(
                        "Tr_tit_aut",
                        "tp_responsabilita",
                        elAut.getTipoRespons().toString()));
            if (elAut.getChiaviAutoreCerca() != null) {
                autore.setKY_AUTEUR(elAut.getChiaviAutoreCerca().getAutoreAUTEUR());
                autore.setKY_CAUTUN(elAut.getChiaviAutoreCerca().getAutoreCAUT());
            }
            valorizzaFiltriSpecifici(autore,elAut);
        }
    }

    protected void valorizzaFiltri(Ve_cartografia_cla_aut autore, CercaDatiTitType cerca)
        throws EccezioneSbnDiagnostico {
        super.valorizzaFiltri(autore, cerca);
        ElementoAutLegatoType elAut = cerca.getElementoAutLegato();
        if (elAut != null) {
            if (elAut.getRelatorCode() != null)
                autore.setCD_RELAZIONE(
                    Decodificatore.getCd_tabella(
                        "Tr_tit_aut",
                        "cd_relazione",
                        elAut.getRelatorCode().toString()));
            if (elAut.getTipoRespons() != null)
                autore.setTP_RESPONSABILITA(
                    Decodificatore.getCd_tabella(
                        "Tr_tit_aut",
                        "tp_responsabilita",
                        elAut.getTipoRespons().toString()));
            if (elAut.getChiaviAutoreCerca() != null) {
                autore.setKY_AUTEUR(elAut.getChiaviAutoreCerca().getAutoreAUTEUR());
                autore.setKY_CAUTUN(elAut.getChiaviAutoreCerca().getAutoreCAUT());
            }
            valorizzaFiltriSpecifici(autore,elAut);
        }
    }

    protected void valorizzaFiltri(Ve_cartografia_tit_c_aut autore, CercaDatiTitType cerca)
        throws EccezioneSbnDiagnostico {
        super.valorizzaFiltri(autore, cerca);
        ElementoAutLegatoType elAut = cerca.getElementoAutLegato();
        if (elAut != null) {
            if (elAut.getRelatorCode() != null)
                autore.setCD_RELAZIONE(
                    Decodificatore.getCd_tabella(
                        "Tr_tit_aut",
                        "cd_relazione",
                        elAut.getRelatorCode().toString()));
            if (elAut.getTipoRespons() != null)
                autore.setTP_RESPONSABILITA(
                    Decodificatore.getCd_tabella(
                        "Tr_tit_aut",
                        "tp_responsabilita",
                        elAut.getTipoRespons().toString()));
            if (elAut.getChiaviAutoreCerca() != null) {
                autore.setKY_AUTEUR(elAut.getChiaviAutoreCerca().getAutoreAUTEUR());
                autore.setKY_CAUTUN(elAut.getChiaviAutoreCerca().getAutoreCAUT());
            }
            valorizzaFiltriSpecifici(autore,elAut);
        }
    }

    protected void valorizzaFiltriSpecifici(Tb_titolo autore, ElementoAutLegatoType elAut)
        throws EccezioneSbnDiagnostico {
        if (elAut != null) {
            CanaliCercaDatiAutType canali = elAut.getCanaliCercaDatiAut();
            if (canali != null) {
                //autore.setVid(canali.getT001());
                StringaCercaType stringaCerca = canali.getStringaCerca();
                if (stringaCerca != null) {
                	StringaCercaTypeChoice choice = stringaCerca.getStringaCercaTypeChoice();
                    String stringa = choice.getStringaEsatta();
                    if (stringa != null) {
                        stringa = Formattazione.formatta(ChiaviAutore.rimuoviApostrofi(stringa));
                        if (stringa.length() > 50) {
                            autore.settaParametro("StringaEsattaAutore1", stringa.substring(0, 50));
                            autore.settaParametro("StringaEsattaAutore2", stringa.substring(50));
                        } else
                            autore.settaParametro("StringaEsattaAutore1", stringa);
                    }
                    stringa = choice.getStringaLike();
                    if (stringa != null) {
                        stringa = Formattazione.formatta(ChiaviAutore.rimuoviApostrofi(stringa));
                        if (stringa.length() > 50) {
                            autore.settaParametro("StringaLikeAutore1", stringa.substring(0, 50));
                            autore.settaParametro("StringaLikeAutore2", stringa.substring(50));
                        } else
                            autore.settaParametro("StringaLikeAutore1", stringa);
                    }
                }
            }
        }

    }


}
