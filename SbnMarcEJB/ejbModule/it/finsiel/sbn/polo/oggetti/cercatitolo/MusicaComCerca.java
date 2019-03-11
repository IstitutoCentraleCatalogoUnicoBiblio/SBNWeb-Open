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
import it.finsiel.sbn.polo.dao.entity.viste.Ve_musica_aut_comResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_musica_cla_comResult;
import it.finsiel.sbn.polo.dao.entity.viste.Ve_musica_tit_c_comResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vf_musica_comResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vf_musica_comautResult;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.NormalizzaNomi;
import it.finsiel.sbn.polo.factoring.transactionmaker.CercaTitolo;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Formattazione;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.viste.Ve_musica_aut_com;
import it.finsiel.sbn.polo.orm.viste.Ve_musica_cla_com;
import it.finsiel.sbn.polo.orm.viste.Ve_musica_tit_c_com;
import it.finsiel.sbn.polo.orm.viste.Vf_musica_com;
import it.finsiel.sbn.polo.orm.viste.Vf_musica_comaut;
import it.iccu.sbn.ejb.model.unimarcmodel.A928;
import it.iccu.sbn.ejb.model.unimarcmodel.A929;
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
 * Classe MusicaComCerca
 * <p>
 * Esegue quelle ricerche di titoli che valorizzano i filtri relativi alla musica e composizione
 * Utilizza le viste di tipo VE , VL e VF.
 * Estende la classe Titolo, dalla quale eredita diversi metodi di utilità e la possibilità
 * di valorizzare i filtri sul titolo cercato.
 * </p>
 * @author
 * @author
 *
 * @version 23-apr-03
 */
public class MusicaComCerca extends TitoloMusica {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4853712358367961524L;
	private static Category log = Category.getInstance("it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloAutore");
    private String ord;
    private CercaTitoloType cercaTitolo;
    private CercaTitolo factoring;

    /** Costruttore, la connessione al db serve per tutti gli accessi al DB */
    public MusicaComCerca(CercaTitolo factoring, CercaTitoloType cercaTitolo, String ord) {
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
                    //return cercaTitoloPerLuogo();
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

        } else { //non ho l'arrivo legame, uso solo i filtri su titolo (e magari su autore)
            if (cercaTitolo.getCercaDatiTit().getElementoAutLegato() != null
                && cercaTitolo.getCercaDatiTit().getElementoAutLegato().getTipoAuthority().getType() ==
                    SbnAuthority.AU_TYPE)
                return cercaComposizioneConAutore();
            else
                return cercaTitoloConFiltriComposizione();
        }
        return null;
    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * Cerca per stringaEsatta, stringaLike o Clet.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * */
    protected TableDao cercaTitoloConFiltriComposizione() throws IllegalArgumentException, InvocationTargetException, Exception {
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
        Vf_musica_com tit = new Vf_musica_com();
        Vf_musica_comResult tavola = new Vf_musica_comResult(tit);

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
        Vf_musica_com tit = new Vf_musica_com();
        Vf_musica_comResult tavola = new Vf_musica_comResult(tit);

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
        Vf_musica_com tit = new Vf_musica_com();
        Vf_musica_comResult tavola = new Vf_musica_comResult(tit);

        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        factoring.controllaNumeroRecord(conta(tavola, "countPerNomeEsatto"));

        tavola.executeCustom("selectPerNomeEsatto", ord);
        return tavola;

    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_titolo_luo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerAutore() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ve_musica_aut_com tit = new Ve_musica_aut_com();
        Ve_musica_aut_comResult tavola = new Ve_musica_aut_comResult(tit);

        tit.setVID(cercaTitolo.getArrivoLegame().getLegameElementoAut().getIdArrivo());
        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        factoring.controllaNumeroRecord(conta(tavola, "countPerAutore"));

        tavola.executeCustom("selectPerAutore", ord);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_titolo_luo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaComposizioneConAutore() throws IllegalArgumentException, InvocationTargetException, Exception {
        Vf_musica_comaut tit = new Vf_musica_comaut();
        Vf_musica_comautResult tavola = new Vf_musica_comautResult(tit);

        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        valorizzaFiltriAutore(tit, cercaTitolo.getCercaDatiTit());

        StringaCercaType stringaCerca =
            cercaTitolo.getCercaDatiTit().getCercaDatiTitTypeChoice().getTitoloCerca().getStringaCerca();
        if (stringaCerca != null) {
            if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null) {
                factoring.controllaNumeroRecord(conta(tavola, "countPerNomeEsatto"));

                tavola.executeCustom("selectPerNomeEsatto", ord);
            } else if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null) {
                factoring.controllaNumeroRecord(conta(tavola, "countPerNomeLike"));

                tavola.executeCustom("selectPerNomeLike", ord);
            } else
                throw new EccezioneSbnDiagnostico(3040);
        } else if (
            cercaTitolo.getCercaDatiTit().getCercaDatiTitTypeChoice().getTitoloCerca().getTitoloCLET()
                != null) {
            //Se arrivo qui è da solo, altrimenti è da utilizzarsi in combinazione
            //con altri elementi
            tit.settaParametro(TableDao.XXXstringaClet,
                cercaTitolo.getCercaDatiTit().getCercaDatiTitTypeChoice().getTitoloCerca().getTitoloCLET());
            factoring.controllaNumeroRecord(conta(tavola, "countPerClet"));

            tavola.executeCustom("selectPerClet", ord);
        }
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_titolo_luo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerLegameTitolo(String bid) throws IllegalArgumentException, InvocationTargetException, Exception {
        Ve_musica_tit_c_com tit = new Ve_musica_tit_c_com();
        Ve_musica_tit_c_comResult tavola = new Ve_musica_tit_c_comResult(tit);

        tit.setBID(bid);
        valorizzaFiltri(tit, cercaTitolo.getCercaDatiTit());
        factoring.controllaNumeroRecord(conta(tavola, "countPerTitolo"));

        tavola.executeCustom("selectPerTitolo", ord);
        return tavola;
    }

    /** Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_titolo_luo
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected TableDao cercaTitoloPerClasse() throws IllegalArgumentException, InvocationTargetException, Exception {
        Ve_musica_cla_com tit = new Ve_musica_cla_com();
        Ve_musica_cla_comResult tavola = new Ve_musica_cla_comResult(tit);
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

    public Tb_titolo valorizzaFiltri(Tb_titolo luogo, CercaDatiTitType cerca)
        throws EccezioneSbnDiagnostico {
        super.valorizzaFiltri(luogo, cerca);
        ElementoAutLegatoType elAut = cerca.getElementoAutLegato();
        if (elAut != null) {
            CanaliCercaDatiAutType canali = elAut.getCanaliCercaDatiAut();
            if (canali != null) {
                StringaCercaType stringaCerca = canali.getStringaCerca();
                if (stringaCerca != null) {
                    if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null)
                        luogo.settaParametro(TableDao.XXXStringaEsatta, stringaCerca.getStringaCercaTypeChoice().getStringaEsatta());
                    if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null)
                        luogo.settaParametro(TableDao.XXXStringaLike, stringaCerca.getStringaCercaTypeChoice().getStringaLike());
                }
            }
        }

        if (cerca instanceof CercaDocMusicaType) {
            CercaDocMusicaType musica = (CercaDocMusicaType) cerca;
            //Dati specifici di titolo uniforme musicale (composizione)
            if (musica.getT928() != null) {
                A928 a928 = musica.getT928();
                for (int i = 0; i < a928.getA_928Count(); i++)
                    luogo.settaParametro("XXXformaComposizione" + (i + 1),
                        Decodificatore.getCd_tabella("Tb_composizione", "cd_forma_1", a928.getA_928(i)));
                String orgSint = a928.getB_928();
                if (orgSint != null) {
                    orgSint = orgSint.replace('*', '%');
                    luogo.settaParametro(TableDao.XXXorganicoSinteticoComposizione, orgSint);
                }
                String orgAnal = a928.getC_928();
                if (orgAnal != null) {
                    orgAnal = orgAnal.replace('*', '%');
                    luogo.settaParametro(TableDao.XXXorganicoAnaliticoComposizione, orgAnal);
                }
            }
            if (musica.getT929() != null) {
                A929 a929 = musica.getT929();
                luogo.settaParametro(TableDao.XXXnumeroOrdine, a929.getA_929());
                luogo.settaParametro(TableDao.XXXnumeroOpera, a929.getB_929());
                luogo.settaParametro(TableDao.XXXnumeroCatalogo, a929.getC_929());
                if (a929.getE_929() != null)
                    luogo.settaParametro(TableDao.XXXtonalita,
                        Decodificatore.getCd_tabella("Tb_composizione", "cd_tonalita", a929.getE_929()));
                String st = a929.getG_929();
                if (st != null) {
                    st = NormalizzaNomi.normalizzazioneGenerica(st);
                    if (st.length() > 10) {
                        luogo.settaParametro(TableDao.XXXtitoloOrdinamento, st.substring(0, 10));
                        luogo.settaParametro(TableDao.XXXtitoloOrdinamentoLungo, st);
                    } else
                        luogo.settaParametro(TableDao.XXXtitoloOrdinamento, st);
                }
                st = a929.getH_929();
                if (st != null) {
                    st = NormalizzaNomi.normalizzazioneGenerica(st);
                    if (st.length() > 10) {
                        luogo.settaParametro(TableDao.XXXtitoloEstratto, st.substring(0, 10));
                        luogo.settaParametro(TableDao.XXXtitoloEstrattoLungo, st);
                    } else
                        luogo.settaParametro(TableDao.XXXtitoloEstratto, st);
                }
                st = a929.getI_929();
                if (st != null) {
                    st = NormalizzaNomi.normalizzazioneGenerica(st);
                    if (st.length() > 10) {
                        luogo.settaParametro(TableDao.XXXappellativo, st.substring(0, 10));
                        luogo.settaParametro(TableDao.XXXappellativoLungo, st);
                    } else
                        luogo.settaParametro(TableDao.XXXappellativo, st);
                }
                luogo.settaParametro(TableDao.XXXdataComposizione,a929.getD_929());
            }
            luogo.settaParametro(TableDao.XXXdataInizioDa, musica.getDataInizio_Da());
            luogo.settaParametro(TableDao.XXXdataInizioA, musica.getDataInizio_A());
            luogo.settaParametro(TableDao.XXXdataFineDa, musica.getDataFine_Da());
            luogo.settaParametro(TableDao.XXXdataFineA, musica.getDataFine_A());
        }
        return luogo;
    }

    protected void valorizzaFiltriAutore(Vf_musica_comaut autore, CercaDatiTitType cerca)
        throws EccezioneSbnDiagnostico {
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
            valorizzaFiltriSpecificiAutore(autore, elAut);
        }
    }

    protected void valorizzaFiltriSpecificiAutore(Tb_titolo autore, ElementoAutLegatoType elAut)
        throws EccezioneSbnDiagnostico {
        if (elAut != null) {
            CanaliCercaDatiAutType canali = elAut.getCanaliCercaDatiAut();
            if (canali != null) {
                //autore.setVID(canali.getT001());
                StringaCercaType stringaCerca = canali.getStringaCerca();
                if (stringaCerca != null) {
                    String stringa = stringaCerca.getStringaCercaTypeChoice().getStringaEsatta();
                    if (stringa != null) {
                        stringa = Formattazione.formatta(stringa);
                        if (stringa.length() > 50) {
                            autore.settaParametro(TableDao.XXXStringaEsattaAutore1, stringa.substring(0, 50));
                            autore.settaParametro(TableDao.XXXStringaEsattaAutore2, stringa.substring(50));
                        } else
                            autore.settaParametro(TableDao.XXXStringaEsattaAutore1, stringa);
                    }
                    stringa = stringaCerca.getStringaCercaTypeChoice().getStringaLike();
                    if (stringa != null) {
                        stringa = Formattazione.formatta(stringa);
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
