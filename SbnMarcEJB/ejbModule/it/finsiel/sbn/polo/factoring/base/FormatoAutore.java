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

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnData;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Marca;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.oggetti.RepertorioAutore;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tr_rep_aut;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_aut;
import it.finsiel.sbn.polo.orm.viste.Vl_marca_aut;
import it.iccu.sbn.ejb.model.unimarcmodel.A010;
import it.iccu.sbn.ejb.model.unimarcmodel.A100;
import it.iccu.sbn.ejb.model.unimarcmodel.A152;
import it.iccu.sbn.ejb.model.unimarcmodel.A300;
import it.iccu.sbn.ejb.model.unimarcmodel.A801;
import it.iccu.sbn.ejb.model.unimarcmodel.A830;
import it.iccu.sbn.ejb.model.unimarcmodel.AllineaInfoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.AutorePersonaleType;
import it.iccu.sbn.ejb.model.unimarcmodel.C101;
import it.iccu.sbn.ejb.model.unimarcmodel.C102;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.EnteType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.OggettoVariatoType;
import it.iccu.sbn.ejb.model.unimarcmodel.OggettoVariatoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoModifica;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoNomeAutore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOrd;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe FormatoAutore.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 20-nov-02
 */
public class FormatoAutore {
    //tipoOut: tipo di output richiesto: Esame, Lista
    protected SbnTipoOutput tipoOut = null;
    String tipoOrd;

    SbnUserType user;

    //protected static ValidatorAdmin validator = ValidatorAdmin.getInstance();
    protected static ValidatorProfiler validator = ValidatorProfiler.getInstance();

    //file di log
    static Category log = Category.getInstance("iccu.box.FormatoAutore");

    /**
     * @param: tipoOrd
     */
    public FormatoAutore(SbnTipoOutput tipoOut, String tipoOrd, SbnUserType user) {
        this.tipoOut = tipoOut;

        this.tipoOrd = tipoOrd;
        this.user = user;
    }

    /**
     * Crea un oggetto xml di tipo DatiElementoType, specializzato in AutorePersonaleType
     * o in EnteType.
     */
    public ElementAutType preparaAutore(Tb_autore autore) throws EccezioneDB {
        DatiElementoType datiElementoAut;
        TipiAutore tipiAutore = new TipiAutore();
        if (tipiAutore.isPersonale(autore))
            datiElementoAut = formattaAutorePersonale(autore);
        else
            datiElementoAut = creaAutoreCollettivo(autore);
        ElementAutType elemento = new ElementAutType();
        elemento.setDatiElementoAut(datiElementoAut);
        return elemento;
    }

    /**
     * Method formattaAutore.
     * Se tipoOut = lista
     *  Se un autore è in forma di rinvio (tp_forma = 'R') legge l'autore in forma
     *  accettata (attiva cercaRinviiAutore ), compila la struttura AutoreType con
     *  l'autore in forma accettata e la struttura ArrivoLegame con l'autore in forma
     *  di rinvio
     *
     * Se tipoOut = esame
     *  Se un autore è in forma di rinvio (tp_forma = 'R') legge l'autore in forma
     *  accettata (attiva cercaRinviiAutore), compila la struttura AutoreType con l'
     *  autore in forma accettata e la struttura ArrivoLegame con l'autore in forma di
     *  rinvio
     *  Se un autore è in forma accettata (tp_forma = 'A') cerca i rinvii dell'autore
     *  con
     *  il metodo cercaRinviiAutore
     *  Per ogni rinvio trovato compila la struttura ArrivoLegame
     * @param autore con i parametri
     * @param esteso se l'output è esteso, altrimenti sintetico
     * @return ElementAutType
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public ElementAutType formattaAutorePerListaSintetica(Tb_autore autore)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        ElementAutType elemento = null;
        List rinvii = cercaRinviiAutore(autore.getVID());

        if (autore.getTP_FORMA_AUT().equals("R")) { //forma di rinvio
            if (rinvii.size() == 1) {
                Vl_autore_aut vl_autore = (Vl_autore_aut) rinvii.get(0);
                elemento = (preparaAutore(autore));
                elemento.addLegamiElementoAut(
                    formattaRinvioAutore(autore, vl_autore, vl_autore.getTP_LEGAME()));
            } else if (rinvii.size() > 1) {
                log.warn("Autore R con più forme accettate (autore:" + autore.getVID() + ")");
                //throw new EccezioneDB(3037);
                for (int i = 0; i < rinvii.size(); i++) {
                    Vl_autore_aut vl_autore = (Vl_autore_aut) rinvii.get(i);
                    elemento = (preparaAutore(autore));
                    elemento.addLegamiElementoAut(
                        formattaRinvioAutore(autore, vl_autore, vl_autore.getTP_LEGAME()));
                    if (output != null && i < rinvii.size() - 1) {
                        //L'ultimo non lo aggiungo perchè viene aggiunto dopo.
                        output.addElementoAut(elemento);
                        output.setTotRighe(output.getTotRighe()+1);
                    }
                }
            } else {
                log.warn("Rinvio di autore non trovato (autore:" + autore.getVID() + ")");
                //throw new EccezioneDB(3037);
                elemento = (preparaAutore(autore));
            }
        } else if (autore.getTP_FORMA_AUT().equals("A")) {

            elemento = (preparaAutore(autore));
        }
        return elemento;
    }

    int loop_counter;
    protected ElementAutType formattaAutorePerEsameAnalitico(Tb_autore autore)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        ElementAutType elemento = new ElementAutType();
        List rinvii = cercaRinviiAutore(autore.getVID());
        //Codice per evitare i loop se ci sono legami errati
        loop_counter++;
        if (loop_counter > 10)
            return preparaAutore(autore);
        //Fine Codice per evitare i loop se ci sono legami errati
        if (autore.getTP_FORMA_AUT().equals("R")) { //forma di rinvio
            if (rinvii.size() < 1) {
                log.error("Rinvio di autore non trovato (autore:" + autore.getVID() + ")");
                //throw new EccezioneDB(3037);
                elemento = (preparaAutore(autore));
            } else {
                Vl_autore_aut vl_autore = (Vl_autore_aut) rinvii.get(0);
                //elemento.setDatiElementoAut(creaAutore(vl_autore));
                //elemento.addLegamiElementoAut(creaRinvioAutore(vl_autore, autore, vl_autore.getTP_LEGAME()));
                return formattaAutorePerEsameAnalitico(vl_autore);
            }
        } else if (autore.getTP_FORMA_AUT().equals("A")) {

            elemento = (preparaAutore(autore));
            for (int c = 0; c < rinvii.size(); c++) {
                Vl_autore_aut aut = (Vl_autore_aut) rinvii.get(c);
                LegamiType lt =
                    formattaRinvioAutore(
                        autore,
                        aut,
                        aut.getTP_LEGAME());
                elemento.addLegamiElementoAut(lt);
                if (aut.getTP_FORMA_AUT().equals("A")) {
                    List rinvii2 = cercaRinviiAutore(aut.getVID());
                    for (int cc = 0; cc < rinvii2.size(); cc++) {
                        Vl_autore_aut aut2 = (Vl_autore_aut) rinvii2.get(cc);
                        if (aut2.getTP_LEGAME().equals("8")) {
                            LegamiType lt2 =
                                formattaRinvioAutore(
                                    aut,
                                    aut2,
                                    aut2.getTP_LEGAME());
                            lt.getArrivoLegame(0).getLegameElementoAut().getElementoAutLegato().addLegamiElementoAut(
                                lt2);
                        }
                    }
                }
            }
        }
        aggiungiLegami(autore, elemento);
        return elemento;
    }
    /** Aggiunge i legami a marca e repertorio
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    protected void aggiungiLegami(Tb_autore autore, ElementAutType elemento)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        //Legami con Marca
        List vettore = cercaMarcheAutore(autore.getVID());
        if (vettore.size() > 0) {
            LegamiType legamiType = new LegamiType();
            legamiType.setIdPartenza(autore.getVID());
            for (int i = 0; i < vettore.size(); i++) {
                Vl_marca_aut mar = (Vl_marca_aut) vettore.get(i);
                legamiType.addArrivoLegame(formattaLegameMarca(mar));
            }
            elemento.addLegamiElementoAut(legamiType);
        }
        RepertorioAutore repAut = new RepertorioAutore();
        vettore = repAut.cercaCitazioniInRepertori(autore.getVID(), tipoOrd);
        if (vettore.size() > 0) {
            LegamiType legamiType = new LegamiType();
            legamiType.setIdPartenza(autore.getVID());
            for (int i = 0; i < vettore.size(); i++) {
                //Da sostituire con la vista Vl_repertorio_aut
                Tr_rep_aut rep_aut = (Tr_rep_aut) vettore.get(i);
                ArrivoLegame arr = formattaLegameRepertorio(rep_aut);
                if (arr != null)
                    legamiType.addArrivoLegame(arr);
            }
            if (legamiType.getArrivoLegameCount() > 0)
                elemento.addLegamiElementoAut(legamiType);
        }
    }
    public ElementAutType formattaAutore(Tb_autore autore) throws IllegalArgumentException, InvocationTargetException, Exception {
        ElementAutType elemento;
        //elemento.setLocalizzaInfo(new LocalizzaInfoType[0]);

        loop_counter = 0;
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) {
            elemento = formattaAutorePerEsameAnalitico(autore);
        } else
            elemento = formattaAutorePerListaSintetica(autore);
        return elemento;
    }

    protected AutorePersonaleType formattaAutorePersonale(Tb_autore autore) {
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType())
            return formattaAutorePersonalePerEsame(autore);
        return formattaAutorePersonalePerLista(autore);
    }


    /**
     * Crea un oggetto autorePersonaleType Castor: riempie tutti i campi leggendoli
     * dall'oggetto Tb_autore passato come parametro
     */
    protected AutorePersonaleType formattaAutorePersonalePerLista(Tb_autore autore) {
        AutorePersonaleType datiElementoAut = new AutorePersonaleType();
        if(autore.getFL_CONDIVISO() != null)
        	datiElementoAut.setCondiviso(DatiElementoTypeCondivisoType.valueOf(autore.getFL_CONDIVISO()));
        datiElementoAut.setTipoAuthority(SbnAuthority.AU);
        datiElementoAut.setTipoNome(SbnTipoNomeAutore.valueOf(autore.getTP_NOME_AUT()));
        datiElementoAut.setT001(autore.getVID());
        datiElementoAut.setFormaNome(SbnFormaNome.valueOf(autore.getTP_FORMA_AUT()));
        if (autore.getNOTA_AUT() != null && autore.getNOTA_AUT().trim().length() > 0) {
            int n = autore.getNOTA_AUT().indexOf("//");
            if (n >= 0) {
                A300 t300 = new A300();
                t300.setA_300(autore.getNOTA_AUT().substring(0, n + 2));
                datiElementoAut.setT300(t300);
            }
        }
        datiElementoAut.setLivelloAut(
            SbnLivello.valueOf(Decodificatore.livelloSoglia(autore.getCD_LIVELLO())));
        TipiAutore ca = new TipiAutore();
        datiElementoAut.setT200(ca.calcolaT200(autore));
        if (!validator.isPolo(user.getBiblioteca())){
            SbnDatavar datevar = new SbnDatavar(autore.getTS_VAR());
            datiElementoAut.setT005(datevar.getT005Date());
            //datiElementoAut.setT005(autore.getTS_VAR().getOriginalDate());
        }
        return datiElementoAut;
    }

    protected AutorePersonaleType formattaAutorePersonalePerEsame(Tb_autore autore) {
        AutorePersonaleType datiElementoAut = new AutorePersonaleType();
        if(autore.getFL_CONDIVISO() != null)
        	datiElementoAut.setCondiviso(DatiElementoTypeCondivisoType.valueOf(autore.getFL_CONDIVISO()));
        datiElementoAut.setTipoAuthority(SbnAuthority.AU);
        datiElementoAut.setTipoNome(SbnTipoNomeAutore.valueOf(autore.getTP_NOME_AUT()));
        datiElementoAut.setT001(autore.getVID());
        datiElementoAut.setFormaNome(SbnFormaNome.valueOf(autore.getTP_FORMA_AUT()));
        datiElementoAut.setLivelloAut(
            SbnLivello.valueOf(Decodificatore.livelloSoglia(autore.getCD_LIVELLO())));
        if (autore.getNOTA_AUT() != null && autore.getNOTA_AUT().trim().length() > 0) {
            A300 t300 = new A300();
            int n = autore.getNOTA_AUT().indexOf("//");
            if (n >= 0)
                t300.setA_300(autore.getNOTA_AUT().substring(0, n + 2));
            else
                t300.setA_300(autore.getNOTA_AUT());
            datiElementoAut.setT300(t300);
        }
        TipiAutore ca = new TipiAutore();
        datiElementoAut.setT200(ca.calcolaT200(autore));
        SbnDatavar datevar = new SbnDatavar(autore.getTS_VAR());
        datiElementoAut.setT005(datevar.getT005Date());

        // Marzo 2016: gestione ISNI (International standard number identifier)
        if (autore.getISADN() != null) {
//            A015 a015 = new A015();
//            a015.setA_015(autore.getISADN());
//            datiElementoAut.setT015(a015);
        	  A010 a010 = new A010();
              a010.setA_010(autore.getISADN());
              datiElementoAut.setT010(a010);
        }

        if (autore.getCD_LINGUA() != null && !autore.getCD_LINGUA().trim().equals("")) {
            C101 c101 = new C101();
            c101.addA_101(autore.getCD_LINGUA());
            datiElementoAut.setT101(c101);
        }
        if (autore.getCD_PAESE() != null && !autore.getCD_PAESE().trim().equals("")) {
            C102 c102 = new C102();
            c102.setA_102(autore.getCD_PAESE());
            datiElementoAut.setT102(c102);
        }
        if (autore.getCD_NORME_CAT() != null && !autore.getCD_NORME_CAT().trim().equals("")) {
            A152 a152 = new A152();
            a152.setA_152(autore.getCD_NORME_CAT());
            datiElementoAut.setT152(a152);
        }
        if (autore.getCD_AGENZIA() != null && autore.getCD_AGENZIA().length() >= 2) {
            A801 c801 = new A801();
            c801.setA_801(autore.getCD_AGENZIA().substring(0, 2));
            c801.setB_801(autore.getCD_AGENZIA().substring(2));
            datiElementoAut.setT801(c801);
        }
        if (autore.getNOTA_CAT_AUT() != null && !autore.getNOTA_CAT_AUT().trim().equals("")) {
            A830 a830 = new A830();
            a830.setA_830(autore.getNOTA_CAT_AUT());
            datiElementoAut.setT830(a830);
        }

        if (autore.getNOTA_AUT() != null && !autore.getNOTA_AUT().trim().equals("")) {
            A300 a300 = new A300();
            a300.setA_300(autore.getNOTA_AUT());
            datiElementoAut.setT300(a300);
        }

        A100 a100 = new A100();
        try {
            SbnData date = new SbnData(autore.getTS_INS());
            a100.setA_100_0(org.exolab.castor.types.Date.parseDate(date.getXmlDate()));
            //a100.setA_100_0(org.exolab.castor.types.Date.parseDate(autore.getBID().getXmlDate()));
            datiElementoAut.setT100(a100);
        } catch (ParseException ecc) {
            log.error("Formato data non corretto: autore.ts_ins:" + autore.getTS_INS());
        }
        return datiElementoAut;
    }

    /**
     * Formatta un legame tra repertorio e autore
     * @throws InfrastructureException
     */
    protected ArrivoLegame formattaLegameRepertorio(Tr_rep_aut rep_aut)
        throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        ArrivoLegame arrLegame = new ArrivoLegame();

        //Setto i valori del legame
        LegameElementoAutType legameAut = new LegameElementoAutType();
        legameAut.setTipoAuthority(SbnAuthority.RE);
        Repertorio repertorio = new Repertorio();
        Tb_repertorio rep = repertorio.cercaRepertorioId((int) rep_aut.getID_REPERTORIO());
        if (rep != null) {
            legameAut.setIdArrivo(rep.getCD_SIG_REPERTORIO());
            ElementAutType el = new ElementAutType();

            FormatoRepertorio fr = new FormatoRepertorio();
            el.setDatiElementoAut(fr.formattaRepertorioPerEsame(rep));
            legameAut.setElementoAutLegato(el);
            if (rep_aut.getFL_TROVATO().equals("S"))
                legameAut.setTipoLegame(SbnLegameAut.valueOf("810"));
            else
                legameAut.setTipoLegame(SbnLegameAut.valueOf("815"));
            arrLegame.setLegameElementoAut(legameAut);
            //Setto i valori del documento legato
            legameAut.setNoteLegame(rep_aut.getNOTE_REP_AUT());

            arrLegame.setLegameElementoAut(legameAut);
        } else {
            log.warn(
                "Legame con repertorio cancellato. Autore: "
                    + rep_aut.getVID()
                    + " Rep:"
                    + rep_aut.getID_REPERTORIO());
            arrLegame = null;
        }
        return arrLegame;
    }

    /**
     * Prepara legame tra autore e marca
     * Da togliere alcune eccezioni, che non vengono generate (rimane solo
     * EccezioneDB)
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected ArrivoLegame formattaLegameMarca(Vl_marca_aut relaz)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        ArrivoLegame arrLegame = new ArrivoLegame();
        //Setto i valori del legame
        LegameElementoAutType legameAut = new LegameElementoAutType();
        legameAut.setTipoAuthority(SbnAuthority.MA);
        legameAut.setIdArrivo(relaz.getMID());
        legameAut.setTipoLegame(SbnLegameAut.valueOf("921"));
        arrLegame.setLegameElementoAut(legameAut);
        //Setto i valori del documento legato
        FormatoMarca formatoMarca = new FormatoMarca(tipoOut, tipoOrd, user);
        legameAut.setElementoAutLegato(formatoMarca.formattaMarcaPerAutore(relaz));
        legameAut.setNoteLegame(relaz.getNOTA_AUT_MAR());
        arrLegame.setLegameElementoAut(legameAut);
        return arrLegame;
    }

    protected EnteType creaAutoreCollettivo(Tb_autore autore) throws EccezioneDB {
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType())
            return creaAutoreCollettivoPerEsame(autore);
        return creaAutoreCollettivoPerLista(autore);
    }

    /**
     * Crea un oggetto EnteType Castor: riempie tutti i campi leggendoli
     * dall'oggetto Tb_autore passato come parametro
     */
    protected EnteType creaAutoreCollettivoPerLista(Tb_autore autore) {
        EnteType datiElementoAut = new EnteType();
        if(autore.getFL_CONDIVISO() != null)
        	datiElementoAut.setCondiviso(DatiElementoTypeCondivisoType.valueOf(autore.getFL_CONDIVISO()));
        datiElementoAut.setTipoAuthority(SbnAuthority.AU);
        datiElementoAut.setTipoNome(SbnTipoNomeAutore.valueOf(autore.getTP_NOME_AUT()));
        datiElementoAut.setT001(autore.getVID());
        datiElementoAut.setFormaNome(SbnFormaNome.valueOf(autore.getTP_FORMA_AUT()));
        datiElementoAut.setLivelloAut(
            SbnLivello.valueOf(Decodificatore.livelloSoglia(autore.getCD_LIVELLO())));
        TipiAutore ca = new TipiAutore();
        datiElementoAut.setT210(ca.calcolaT210(autore));
        if (autore.getNOTA_AUT() != null && autore.getNOTA_AUT().trim().length() > 0) {
            int n = autore.getNOTA_AUT().indexOf("//");
            if (n >= 0) {
                A300 t300 = new A300();
                t300.setA_300(autore.getNOTA_AUT().substring(0, n + 2));
                datiElementoAut.setT300(t300);
            }
        }
        if (validator.isPolo(user.getBiblioteca())){
            SbnDatavar datevar = new SbnDatavar(autore.getTS_VAR());
            datiElementoAut.setT005(datevar.getT005Date());

        }

        return datiElementoAut;
    }

    protected EnteType creaAutoreCollettivoPerEsame(Tb_autore autore) throws EccezioneDB {
        EnteType datiElementoAut = new EnteType();
        if(autore.getFL_CONDIVISO() != null)
        	datiElementoAut.setCondiviso(DatiElementoTypeCondivisoType.valueOf(autore.getFL_CONDIVISO()));
        datiElementoAut.setTipoAuthority(SbnAuthority.AU);
        datiElementoAut.setTipoNome(SbnTipoNomeAutore.valueOf(autore.getTP_NOME_AUT()));
        datiElementoAut.setT001(autore.getVID());
        datiElementoAut.setFormaNome(SbnFormaNome.valueOf(autore.getTP_FORMA_AUT()));
        datiElementoAut.setLivelloAut(
            SbnLivello.valueOf(Decodificatore.livelloSoglia(autore.getCD_LIVELLO())));
        TipiAutore ca = new TipiAutore();
        datiElementoAut.setT210(ca.calcolaT210(autore));
        SbnDatavar datevar = new SbnDatavar(autore.getTS_VAR());
        datiElementoAut.setT005(datevar.getT005Date());

        //datiElementoAut.setT005(autore.getTS_VAR().getOriginalDate());

     // Marzo 2016: gestione ISNI (International standard number identifier)
        if (autore.getISADN() != null) {
//            A015 a015 = new A015();
//            a015.setA_015(autore.getISADN());
//            datiElementoAut.setT015(a015);
        	 A010 a010 = new A010();
             a010.setA_010(autore.getISADN());
             datiElementoAut.setT010(a010);
        }

        A100 a100 = new A100();
        try {
            SbnData date = new SbnData(autore.getTS_INS());
            a100.setA_100_0(org.exolab.castor.types.Date.parseDate(date.getXmlDate()));
            //a100.setA_100_0(org.exolab.castor.types.Date.parseDate(autore.getTS_INS().getXmlDate()));
            datiElementoAut.setT100(a100);
        } catch (ParseException ecc) {
            log.error("Formato data non corretto: autore.ts_ins:" + autore.getTS_INS());
        }
        if (autore.getCD_LINGUA() != null && !autore.getCD_LINGUA().trim().equals("")) {
            C101 c101 = new C101();
            c101.addA_101(autore.getCD_LINGUA());
            datiElementoAut.setT101(c101);
        }
        if (autore.getCD_PAESE() != null && !autore.getCD_PAESE().trim().equals("")) {
            C102 c102 = new C102();
            c102.setA_102(autore.getCD_PAESE());
            datiElementoAut.setT102(c102);
        }
        if (autore.getCD_NORME_CAT() != null && !autore.getCD_NORME_CAT().trim().equals("")) {
            A152 a152 = new A152();
            a152.setA_152(autore.getCD_NORME_CAT());
            datiElementoAut.setT152(a152);
        }
        if (autore.getCD_AGENZIA() != null && autore.getCD_AGENZIA().length() >= 2) {
            A801 c801 = new A801();
            c801.setA_801(autore.getCD_AGENZIA().substring(0, 2));
            c801.setB_801(autore.getCD_AGENZIA().substring(2));
            datiElementoAut.setT801(c801);
        }
        //datiElementoAut.setT810(formattaCitazioniInRepertori(autore.getVID()));
        if (autore.getNOTA_CAT_AUT() != null && !autore.getNOTA_CAT_AUT().trim().equals("")) {
            A830 a830 = new A830();
            a830.setA_830(autore.getNOTA_CAT_AUT());
            datiElementoAut.setT830(a830);
        }

        if (autore.getNOTA_AUT() != null && !autore.getNOTA_AUT().trim().equals("")) {
            A300 a300 = new A300();
            a300.setA_300(autore.getNOTA_AUT());
            datiElementoAut.setT300(a300);
        }
        return datiElementoAut;
    }

    /**
     * Method formattaCitazioniInRepertori.
     * Per riempire tutti i campi avrei bisogno di una vista del tipo: Vl_repertorio_aut
     * @return int
     * /
    protected A810[] formattaCitazioniInRepertori(String vid) throws EccezioneDB {
        A810[] a810 = null;
        TableDao v = cercaCitazioniInRepertori(vid);
        if (v.size() > 0) {
            a810 = new A810[v.size()];
            Tr_rep_aut rep_aut;
            for (int i = 0; i < v.size(); i++) {
                rep_aut = (Vl_rep_aut) v.get(i);
                a810[i].setC2_810(rep_aut.getCd_sig_repertorio());
            }
        }
        return a810;
    }

    /**
     * Method cercaCitazioniInRepertori.
     * @return TableDao
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected List formattaCitazioniInRepertori(String vid) throws IllegalArgumentException, InvocationTargetException, Exception {
        List v = new ArrayList();
        RepertorioAutore rep_autDB = new RepertorioAutore();
        return rep_autDB.cercaCitazioniInRepertori(vid, tipoOrd);
    }

    /**
     * Cerca i rinvii di un autore e ne costruisce il LegamiType
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected LegamiType formattaRinvioAutore(Tb_autore autore, Vl_autore_aut rinvio, String tipoLegame)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        LegamiType legame = new LegamiType();
        legame.setIdPartenza(autore.getVID());
        LegameElementoAutType legElAut = new LegameElementoAutType();
        legElAut.setTipoAuthority(SbnAuthority.AU);
        legElAut.setIdArrivo(rinvio.getVID());
        legElAut.setNoteLegame(rinvio.getNOTA_AUT_AUT());
        if (tipoLegame.equals("4"))
            legElAut.setTipoLegame(SbnLegameAut.valueOf("5XX"));
        else if (tipoLegame.equals("8"))
            legElAut.setTipoLegame(SbnLegameAut.valueOf("4XX"));
        ElementAutType elemento = new ElementAutType();
        //elemento.setLocalizzaInfo(new LocalizzaInfoType[0]);
        elemento = (preparaAutore(rinvio));
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType())
            aggiungiLegami(rinvio, elemento);
        legElAut.setElementoAutLegato(elemento);
        ArrivoLegame[] arrivoLegame = new ArrivoLegame[1];
        arrivoLegame[0] = new ArrivoLegame();
        arrivoLegame[0].setLegameElementoAut(legElAut);
        legame.setArrivoLegame(arrivoLegame);
        return legame;
    }

    public List cercaRinviiAutore(String vid) throws IllegalArgumentException, InvocationTargetException, Exception {
        Autore autoreDB = new Autore();
        return autoreDB.cercaRinviiAutore(vid);
    }

    public List cercaMarcheAutore(String vid) throws IllegalArgumentException, InvocationTargetException, Exception {
        Marca marcaDB = new Marca();
        TableDao tavola = marcaDB.cercaMarcaPerAutore(vid, tipoOrd);
        List v = tavola.getElencoRisultati();
        return v;
    }

    /**
     * Formatta una lista di autori, restituisce la stringa che rappresenta l'xml.
     * Usato per i report.
     */
    public SBNMarc formattaLista(
        List autori,
        SbnUserType utente,
        SbnTipoOutput tipoOut,
        SbnTipoOrd tipoOrd,
        String idLista,
        int primoElemento,
        int maxRighe,
        int totRighe,
        BigDecimal versione)
        throws EccezioneFactoring, EccezioneDB, EccezioneSbnDiagnostico {
        StringWriter sw = new StringWriter();
        try {
            return formattaListaE(
                autori,
                utente,
                tipoOut,
                tipoOrd,
                idLista,
                primoElemento,
                maxRighe,
                totRighe,
                versione);

        } catch (Exception e) {
            log.error("Errore marshalling:" + e);
            throw new EccezioneFactoring(101);
        }

    }

    SbnOutputType output = null;
    /**
     * Formatta una lista di autori, restituisce la stringa che rappresenta l'xml.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public SBNMarc formattaListaE(
            List autori,
        SbnUserType utente,
        SbnTipoOutput tipoOut,
        SbnTipoOrd tipoOrd,
        String idLista,
        int primoElemento,
        int maxRighe,
        int totRighe,
        BigDecimal versione)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        int totEl = autori.size();
        int ultimoEl = totEl > maxRighe ? maxRighe : totEl;
        SBNMarc sbnmarc = new SBNMarc();
        SbnMessageType message = new SbnMessageType();
        SbnResponseType response = new SbnResponseType();
        SbnResultType result = new SbnResultType();
        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
        output = new SbnOutputType();
        sbnmarc.setSbnMessage(message);
        SbnUserType user = new SbnUserType();
        sbnmarc.setSbnUser(utente);
        sbnmarc.setSchemaVersion(versione);
        message.setSbnResponse(response);
        response.setSbnResult(result);
        response.setSbnResponseTypeChoice(responseChoice);
        responseChoice.setSbnOutput(output);
        output.setTotRighe(totRighe);
        Tb_autore tb_autore;
        if (totRighe > 0) {
            if (idLista != null) {
                output.setIdLista(idLista);
                output.setMaxRighe(maxRighe);
                output.setNumPrimo(primoElemento + 1);
                //output.setTotRighe(totRighe);
            }
            output.setTipoOrd(tipoOrd);
            output.setTipoOutput(tipoOut);
            for (int i = 0; i < ultimoEl; i++) {
                tb_autore = (Tb_autore) autori.get(i);
                output.addElementoAut(formattaAutore(tb_autore));
            }
            result.setEsito("0000"); //Esito positivo
            result.setTestoEsito("OK");
        } else {
            result.setEsito("3001");
            //Esito non positivo: si potrebbe usare un'ecc.
            result.setTestoEsito("Nessun elemento trovato");
        }
        return sbnmarc;
    }
//  // formatta lista per bis
//    public SBNMarc formattaVectorFile(
//        VectorFileAutore autori,
//        SbnUserType utente,
//        SbnTipoOutput tipoOut,
//        SbnTipoOrd tipoOrd,
//        String idLista,
//        int primoElemento,
//        int maxRighe,
//        int totEl,
//        BigDecimal versione)
//        throws EccezioneFactoring, EccezioneDB, EccezioneSbnDiagnostico {
//        int ultimoEl = (totEl > (primoElemento + maxRighe) ? primoElemento + maxRighe : totEl);
//        SBNMarc sbnmarc = new SBNMarc();
//        SbnMessageType message = new SbnMessageType();
//        SbnResponseType response = new SbnResponseType();
//        SbnResultType result = new SbnResultType();
//        SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
//        output = new SbnOutputType();
//        sbnmarc.setSbnMessage(message);
//        SbnUserType user = new SbnUserType();
//        sbnmarc.setSbnUser(utente);
//        sbnmarc.setSchemaVersion(versione);
//        message.setSbnResponse(response);
//        response.setSbnResult(result);
//        response.setSbnResponseTypeChoice(responseChoice);
//        responseChoice.setSbnOutput(output);
//        output.setTotRighe(totEl);
//        Tb_autore tb_autore;
//        if (totEl > 0) {
//            if (idLista != null) {
//                output.setIdLista(idLista);
//                output.setMaxRighe(maxRighe);
//                output.setNumPrimo(primoElemento + 1);
//                //output.setTotRighe(totRighe);
//            }
//            output.setTipoOrd(tipoOrd);
//            output.setTipoOutput(tipoOut);
//            for (int i = primoElemento; i < ultimoEl; i++) {
//                tb_autore = (Tb_autore)autori.elementAt(i);
//                SbnOutputTypeChoiceItem item = new SbnOutputTypeChoiceItem();
//                SbnOutputTypeChoice choice = new SbnOutputTypeChoice();
//                choice.setSbnOutputTypeChoiceItem(item);
//                item.setElementoAut(formattaAutore(tb_autore));
//                output.addSbnOutputTypeChoice(choice);
//            }
//            result.setEsito("0000"); //Esito positivo
//            result.setTestoEsito("OK");
//        } else {
//            result.setEsito("3001");
//            //Esito non positivo: si potrebbe usare un'ecc.
//            result.setTestoEsito("Nessun elemento trovato");
//        }
//        return sbnmarc;
//    }
//
    /** Prepara un elemento di allineamento inserendovi i titoli estratti dal db in base al bid
     * già presente nell'elemento Allinea. Anche l'oggettoVariato deve già essere stato creato
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public static AllineaInfoType formattaElementoPerAllinea(
        AllineaInfoType allinea,
        SbnUserType utente,
        SbnTipoOutput tipoOut)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        Autore autore = new Autore();
        Tb_autore tb_autore = autore.estraiAutorePerID(allinea.getT001());
        if (tb_autore == null) {
            //throw new EccezioneDB(3001, "Autore non presente in base dati");
            allinea.getOggettoVariato().setTipoModifica(SbnTipoModifica.VALUE_2);
        } else {
            FormatoAutore forAut = new FormatoAutore(tipoOut, null, utente);
            OggettoVariatoTypeChoice oggettoVariatoTypeChoice = new OggettoVariatoTypeChoice();
            OggettoVariatoType oggVar = allinea.getOggettoVariato();
            oggettoVariatoTypeChoice.setElementoAut((forAut.formattaAutore(tb_autore)));
            oggVar.setOggettoVariatoTypeChoice(oggettoVariatoTypeChoice);
            //oggVar.setElementoAut(forAut.formattaAutore(tb_autore));
            allinea.setOggettoVariato(oggVar);
        }
        return allinea;
    }

}
