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
import it.finsiel.sbn.polo.dao.entity.viste.Ve_titolo_aut_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_titolo_aut_nstdResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_titolo_cla_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_titolo_luo_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_titolo_mar_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_titolo_sog_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_titolo_the_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_titolo_tit_c_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vf_titolo_autResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_autResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.ChiaviAutore;
import it.finsiel.sbn.polo.factoring.transactionmaker.CercaTitolo;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Formattazione;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_aut_aut;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_aut_nstd;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_cla_aut;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_luo_aut;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_mar_aut;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_sog_aut;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_the_aut;
import it.finsiel.sbn.polo.orm.viste.Ve_titolo_tit_c_aut;
import it.finsiel.sbn.polo.orm.viste.Vf_titolo_aut;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_aut;
import it.iccu.sbn.ejb.model.unimarcmodel.CanaliCercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaTitoloType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementoAutLegatoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;

import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Category;

/**
 * Classe TitoloAutCerca
 * <p>
 * Esegue quelle ricerche di titoli che valorizzano i filtri relativi ad un autore
 * Utilizza le viste di tipo VE , VL e VF.
 * Estende la classe Titolo, dalla quale eredita diversi metodi di utilità e la possibilità
 * di valorizzare i filtri sul titolo cercato.
 * </p>
 * @author
 * @author
 *
 * @version 11-apr-04
 */
public class TitoloAutCerca extends Titolo {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5258776452942382407L;
	private static Category log = Category.getInstance("it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloAutore");
    private String ord;
    private CercaTitoloType cercaTitolo;
    private CercaTitolo factoring;

    /** Costruttore, la connessione al db serve per tutti gli accessi al DB */
    public TitoloAutCerca(CercaTitolo factoring, CercaTitoloType cercaTitolo, String ord) {
        super();
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
                if (legameElemento.getTipoAuthority().getType() == SbnAuthority.AU_TYPE) {
                    return cercaTitoloPerAutore();
                } else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.LU_TYPE) {
                    return cercaTitoloPerLuogo();
                } else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.SO_TYPE) {
                    return cercaTitoloPerSoggetto();
                } else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.TH_TYPE) {
                    return cercaTitoloPerThesauro();
                } else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.MA_TYPE) {
                    return cercaTitoloPerMarca();
                } else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.CL_TYPE) {
                    return cercaTitoloPerClasse();
                } else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.TU_TYPE) {
                    return cercaTitoloPerLegameTitolo(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());
                } else if (legameElemento.getTipoAuthority().getType() == SbnAuthority.UM_TYPE) {
                    //[TODO manca l'UM
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
     * contenente un elenco di oggetti vista vl_titolo_aut
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerAutore() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (cercaTitolo.getCercaDatiTit() == null
            || cercaTitolo.getCercaDatiTit().getElementoAutLegato() == null) {
            Vl_titolo_aut tit = new Vl_titolo_aut();
            Vl_titolo_autResult tavola = new Vl_titolo_autResult(tit);
            tit.setVID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());
            valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());

            factoring.controllaNumeroRecord(conta(tavola, "countPerAutore"));

            tavola.executeCustom("selectPerAutore", ord);
            return tavola;
        } else {
            Ve_titolo_aut_aut tit = new Ve_titolo_aut_aut();
            Ve_titolo_aut_autResult tavola = new Ve_titolo_aut_autResult(tit);
            tit.setVID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());
            valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());

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
            if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null)
                return cercaTitoloPerNomeEsatto();
            else if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null)
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
        Vf_titolo_aut tit = new Vf_titolo_aut();
        Vf_titolo_autResult tavola = new Vf_titolo_autResult(tit);

        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        tit.settaParametro(TableDao.XXXstringaClet, clet);
        factoring.controllaNumeroRecord(conta(tavola, "countPerClet"));

        tavola.executeCustom("selectPerClet", ord);
        return tavola;

    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerNomeLike() throws IllegalArgumentException, InvocationTargetException, Exception {
        Vf_titolo_aut tit = new Vf_titolo_aut();
        Vf_titolo_autResult tavola = new Vf_titolo_autResult(tit);

        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        factoring.controllaNumeroRecord(conta(tavola, "countPerNomeLike"));

        tavola.executeCustom("selectPerNomeLike", ord);
        return tavola;

    }

    /** Esegue la ricerca per nome del titolo con confronto di tipo like
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    public TableDao cercaTitoloPerNumStd(CercaDocMusicaType musica) throws IllegalArgumentException, InvocationTargetException, Exception {
        Ve_titolo_aut_nstd tit = new Ve_titolo_aut_nstd();
        Ve_titolo_aut_nstdResult tavola = new Ve_titolo_aut_nstdResult(tit);

        valorizzaFiltri(tit, musica);
        factoring.controllaNumeroRecord(conta(tavola, "countPerNumero"));

        tavola.executeCustom("selectPerNumero", ord);
        return tavola;

    }
    /** Esegue la ricerca per nome del titolo con confronto di tipo esatto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerNomeEsatto() throws IllegalArgumentException, InvocationTargetException, Exception {
        Vf_titolo_aut tit = new Vf_titolo_aut();
        Vf_titolo_autResult tavola = new Vf_titolo_autResult(tit);

        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        factoring.controllaNumeroRecord(conta(tavola, "countPerNomeEsatto"));

        tavola.executeCustom("selectPerNomeEsatto", ord);
        return tavola;

    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_titolo_aut
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerLuogo() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ve_titolo_luo_aut tit = new Ve_titolo_luo_aut();
        Ve_titolo_luo_autResult tavola = new Ve_titolo_luo_autResult(tit);

        tit.setLID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());
        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        factoring.controllaNumeroRecord(conta(tavola, "countPerLuogo"));

        tavola.executeCustom("selectPerLuogo", ord);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_titolo_aut
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerLegameTitolo(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        Ve_titolo_tit_c_aut tit = new Ve_titolo_tit_c_aut();
        Ve_titolo_tit_c_autResult tavola = new Ve_titolo_tit_c_autResult(tit);

        tit.setBID(bid);
        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        factoring.controllaNumeroRecord(conta(tavola, "countPerTitolo"));

        tavola.executeCustom("selectPerTitolo", ord);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_titolo_aut
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerClasse() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ve_titolo_cla_aut tit = new Ve_titolo_cla_aut();
        Ve_titolo_cla_autResult tavola = new Ve_titolo_cla_autResult(tit);
        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        String chiave = cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo();
        if (chiave.length() < 3)
            throw new EccezioneSbnDiagnostico(3071, "Identificativo classe non corretto");
        tit.setCD_SISTEMA(chiave.substring(0, 3));
        tit.setCD_EDIZIONE(chiave.substring(1, 3));
        //tit.setCD_EDIZIONE(Decodificatore.getCd_tabella("Tb_classe","cd_edizione",chiave.substring(1, 3)));
        tit.setCLASSE(chiave.substring(3));

        factoring.controllaNumeroRecord(conta(tavola, "countPerClasse"));

        tavola.executeCustom("selectPerClasse", ord);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_titolo_aut
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerSoggetto() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ve_titolo_sog_aut tit = new Ve_titolo_sog_aut();
        Ve_titolo_sog_autResult tavola = new Ve_titolo_sog_autResult(tit);
        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        tit.setCID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());
        factoring.controllaNumeroRecord(conta(tavola, "countPerSoggetto"));
        tavola.executeCustom("selectPerSoggetto", ord);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_titolo_aut
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerThesauro() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ve_titolo_the_aut tit = new Ve_titolo_the_aut();
        Ve_titolo_the_autResult tavola = new Ve_titolo_the_autResult(tit);
        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        tit.setDID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());
        factoring.controllaNumeroRecord(conta(tavola, "countPerThesauro"));
        tavola.executeCustom("selectPerThesauro", ord);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_titolo_aut
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerMarca() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ve_titolo_mar_aut tit = new Ve_titolo_mar_aut();
        Ve_titolo_mar_autResult tavola = new Ve_titolo_mar_autResult(tit);
        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        tit.setMID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());

        factoring.controllaNumeroRecord(conta(tavola, "countPerMarca"));

        tavola.executeCustom("selectPerMarca", ord);
        return tavola;
    }

    protected void valorizzaFiltri(Vf_titolo_aut autore, CercaDatiTitType cerca)
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

    protected void valorizzaFiltri(Ve_titolo_aut_nstd tit,
            CercaDocMusicaType musica) throws EccezioneSbnDiagnostico {
        super.valorizzaFiltri(tit, musica);
        if (musica.hasNumLastra_Da()) {
            tit.setTP_NUMERO_STD("L");
            if (musica.hasNumLastra_Da())
                tit.settaParametro(TableDao.XXXnumeroLastraDa, new Integer(
                         musica.getNumLastra_Da()));
            if (musica.hasNumLastra_A())
                tit.settaParametro(TableDao.XXXnumeroLastraA, new Integer(
                        musica.getNumLastra_A()));
        } else {
            tit.setTP_NUMERO_STD("E");
            if (musica.hasNumEditor_Da())
                tit.settaParametro(TableDao.XXXnumeroLastraDa, new Integer(
                		musica.getNumEditor_Da()));
            if (musica.hasNumEditor_A())
                tit.settaParametro(TableDao.XXXnumeroLastraA, new Integer(
                		musica.getNumEditor_A()));
        }

        ElementoAutLegatoType elAut = musica.getElementoAutLegato();
        if (elAut != null) {
            valorizzaFiltriSpecifici(tit, elAut);
        }
    }

    protected void valorizzaFiltri(Ve_titolo_aut_aut autore, CercaDatiTitType cerca)
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

    protected void valorizzaFiltri(Ve_titolo_luo_aut autore, CercaDatiTitType cerca)
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
    protected void valorizzaFiltri(Ve_titolo_mar_aut autore, CercaDatiTitType cerca)
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

    protected void valorizzaFiltri(Ve_titolo_sog_aut autore, CercaDatiTitType cerca)
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

    protected void valorizzaFiltri(Ve_titolo_cla_aut autore, CercaDatiTitType cerca)
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

    protected void valorizzaFiltri(Ve_titolo_tit_c_aut autore, CercaDatiTitType cerca)
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
                //autore.setVID(canali.getT001());
                StringaCercaType stringaCerca = canali.getStringaCerca();
                if (stringaCerca != null) {
                    String stringa = stringaCerca.getStringaCercaTypeChoice().getStringaEsatta();
                    if (stringa != null) {
                        stringa = Formattazione.formatta(ChiaviAutore.rimuoviApostrofi(stringa));
                        if (stringa.length() > 50) {
                            autore.settaParametro(TableDao.XXXStringaEsattaAutore1, stringa.substring(0, 50));
                            autore.settaParametro(TableDao.XXXStringaEsattaAutore2, stringa.substring(50));
                        } else
                            autore.settaParametro(TableDao.XXXStringaEsattaAutore1, stringa);
                    }
                    stringa = stringaCerca.getStringaCercaTypeChoice().getStringaLike();
                    if (stringa != null) {
                        stringa = Formattazione.formatta(ChiaviAutore.rimuoviApostrofi(stringa));
                        if (stringa.length() > 50) {
                            autore.settaParametro(TableDao.XXXStringaLikeAutore1, stringa.substring(0, 50));
                            autore.settaParametro(TableDao.XXXStringaLikeAutore2, stringa.substring(50));
                        } else
                            autore.settaParametro(TableDao.XXXStringaLikeAutore1, stringa);
                    }
                }
            }
        }

    }

}
