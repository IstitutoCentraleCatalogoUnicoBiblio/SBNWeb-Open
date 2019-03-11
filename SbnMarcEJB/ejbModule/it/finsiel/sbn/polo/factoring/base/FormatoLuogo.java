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
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnData;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Luogo;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.oggetti.RepertorioLuogo;
import it.finsiel.sbn.polo.orm.Tb_luogo;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tr_rep_luo;
import it.finsiel.sbn.polo.orm.viste.Vl_luogo_luo;
import it.iccu.sbn.ejb.model.unimarcmodel.A100;
import it.iccu.sbn.ejb.model.unimarcmodel.A260;
import it.iccu.sbn.ejb.model.unimarcmodel.A300;
import it.iccu.sbn.ejb.model.unimarcmodel.A830;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.LuogoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnFormaNome;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe FormatoLuogo.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 * @author
 *
 * @version 16-gen-2003
 */

public class FormatoLuogo {

    private SbnTipoOutput _tipoOutput;
    private String _tipoOrd;
    static Category log = Category.getInstance("iccu.box.FormatoLuogo");

    public FormatoLuogo(
        SbnTipoOutput tipoOutput,
        String tipoOrdinamento) {
        _tipoOutput = tipoOutput;
        _tipoOrd = tipoOrdinamento;
    }

    public FormatoLuogo() {
        _tipoOutput = SbnTipoOutput.VALUE_1;
        _tipoOrd = null;

    }

    /**
     * metodo chiamato da CercaLuogo
     * restituisce l'elenco dei luoghi ordinati secondo il tipo di output richiesto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public ElementAutType formattaLuogoOutput(Tb_luogo luogo)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        ElementAutType elemento = new ElementAutType();
        //        Luogo cercaLuogo = new Luogo();
        elemento.setDatiElementoAut(formattaLuogo(luogo).getDatiElementoAut());
        return elemento;
    }

    /** Metodi per la gestione dei rinvii dei luoghi,
     * 	000: esame analitico
     * 	altro: lista sintetica
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public ElementAutType formattaLuogo(Tb_luogo luogo) throws IllegalArgumentException, InvocationTargetException, Exception {
        if (_tipoOutput.getType() == SbnTipoOutput.VALUE_0.getType())
            return formattaLuogoAnalitico(luogo);
        else
            return formattaLuogoSintetico(luogo);
    }

    private LegamiType formattaRinvioLuogo(
        Tb_luogo luogo,
        Tb_luogo rinvio,
        String tipoLegame) throws IllegalArgumentException, InvocationTargetException, Exception {
        LegamiType legame = new LegamiType();
        legame.setIdPartenza(luogo.getLID());
        LegameElementoAutType legElAut = new LegameElementoAutType();
        legElAut.setTipoAuthority(SbnAuthority.LU);
        legElAut.setIdArrivo(rinvio.getLID());
        if (tipoLegame.equals("4"))
            legElAut.setTipoLegame(SbnLegameAut.valueOf("5XX"));
        if (tipoLegame.equals("8"))
            legElAut.setTipoLegame(SbnLegameAut.valueOf("4XX"));
        ElementAutType elemento = new ElementAutType();
        //elemento.setLocalizzaInfo(new LocalizzaInfoType[0]);
        elemento.setDatiElementoAut(formattaLuogo(rinvio).getDatiElementoAut());
        legElAut.setElementoAutLegato(elemento);
        ArrivoLegame[] arrivoLegame = new ArrivoLegame[1];
        arrivoLegame[0] = new ArrivoLegame();
        arrivoLegame[0].setLegameElementoAut(legElAut);
        legame.setArrivoLegame(arrivoLegame);

        return legame;
    }

    public ElementAutType formattaRinviiLuogoPerTitolo(
        ElementAutType el,
        Tb_luogo luogo)
        throws IllegalArgumentException, Exception {
        if (luogo.getTP_FORMA().equals("R")) { //forma di rinvio
            //log.error("Il luogo: " + luogo.getLID() + " è sotto forma di rinvio");
            throw new EccezioneDB(3034);
        } else {
            Luogo luogoDB = new Luogo();
            List rinvii = luogoDB.cercaRinviiLuogo(luogo.getLID(), null);
            for (int c = 0; c < rinvii.size(); c++)
                el.addLegamiElementoAut(
                    formattaRinvioLuogo(
                        luogo,
                        (Vl_luogo_luo) rinvii.get(c),
                        ((Vl_luogo_luo) rinvii.get(c)).getTP_LEGAME()));
        }
        return el;
    }

    public ElementAutType formattaRinvii(Tb_luogo luogo)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        ElementAutType elemento = new ElementAutType();
        Luogo cercaLuogo = new Luogo();
        List rinvii = cercaLuogo.cercaRinviiLuogo(luogo.getLID(), null);

        if (luogo.getTP_FORMA().equals("R")) { //forma di rinvio
            if (rinvii.size() != 1) {
                throw new EccezioneSbnDiagnostico(3037);
            }
            Vl_luogo_luo vl_luogo = (Vl_luogo_luo) rinvii.get(0);
            // Inizio Modifica almaviva2 16 luglio 2009 BUG 3076 nella lista sintetica si mando due volte la forma accettata
            // invece che una accettata ed il suo rinvio con il legame La modifica dipenda dal tipo_output che si deve inviare
            // per la sintetica (_tipoOutput=003) si deve invertire l'autore con il rinvio;

            if (this._tipoOutput.getType() == SbnTipoOutput.VALUE_2.getType()) {
                elemento.setDatiElementoAut(formattaLuogo(luogo).getDatiElementoAut());
                elemento.addLegamiElementoAut(formattaRinvioLuogo(luogo, vl_luogo, vl_luogo.getTP_LEGAME()));
            } else {
            	elemento.setDatiElementoAut(formattaLuogo(vl_luogo).getDatiElementoAut());
            	elemento.addLegamiElementoAut(formattaRinvioLuogo(vl_luogo, luogo, vl_luogo.getTP_LEGAME()));
            }
            // Fine Modifica almaviva2 16 luglio 2009 BUG 3076

        } else if (luogo.getTP_FORMA().equals("A")) {

            elemento.setDatiElementoAut(formattaLuogo(luogo).getDatiElementoAut());
            elemento.setLegamiElementoAut(formattaLuogo(luogo).getLegamiElementoAut());
            if (!_tipoOutput.equals("000"))
                for (int c = 0; c < rinvii.size(); c++) {
                    elemento.addLegamiElementoAut(
                        formattaRinvioLuogo(
                            luogo,
                            (Vl_luogo_luo) rinvii.get(c),
                            ((Vl_luogo_luo) rinvii.get(c)).getTP_LEGAME()));
                    //questo si deve presentare come una scheda completa ... come se avessi cercato questo luogo
                    if (((Vl_luogo_luo) rinvii.get(c))
                        .getTP_FORMA()
                        .equals("A")) {
                        //cerco i rinvii anche di questo
                        String lidRinvio =
                            ((Vl_luogo_luo) rinvii.get(c)).getLID();
                        List rinviiDiRinvii =
                            cercaLuogo.cercaAltriRinviiLuogo(
                                lidRinvio,
                                luogo.getLID(),
                                null);
                        LegamiType legami;
                        legami = completaLegami(luogo, rinviiDiRinvii);
                        if (legami.getArrivoLegameCount() != 0)
                            elemento.addLegamiElementoAut(legami);
                    }
                }
        }

        return elemento;
    }

    //mi serve per la scheda completa
    private LegamiType completaLegami(Tb_luogo luogo, List rinvio)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        LegamiType legame = new LegamiType();
        legame.setIdPartenza(luogo.getLID());
        ArrivoLegame[] arrivoLegame = new ArrivoLegame[rinvio.size()];
        for (int j = 0; j < rinvio.size(); j++) {
            LegameElementoAutType legElAut = new LegameElementoAutType();
            legElAut.setTipoAuthority(SbnAuthority.LU);
            legElAut.setIdArrivo(((Vl_luogo_luo) rinvio.get(j)).getLID());
            ElementAutType elemento = new ElementAutType();

            Luogo luogoLegato = new Luogo();
            Tb_luogo tb_luogo = new Tb_luogo();
            tb_luogo =
                luogoLegato.cercaLuogoPerID(
                    ((Vl_luogo_luo) rinvio.get(j)).getLID());
            elemento.setDatiElementoAut(formattaLuogo(tb_luogo).getDatiElementoAut());
            String tipoLegame = ((Vl_luogo_luo) rinvio.get(j)).getTP_LEGAME();
            legElAut.setElementoAutLegato(elemento);
            if (tipoLegame.equals("4"))
                legElAut.setTipoLegame(SbnLegameAut.valueOf("5XX"));
            if (tipoLegame.equals("8"))
                legElAut.setTipoLegame(SbnLegameAut.valueOf("4XX"));

            arrivoLegame[j] = new ArrivoLegame();
            arrivoLegame[j].setLegameElementoAut(legElAut);
        }
        legame.setArrivoLegame(arrivoLegame);
        return legame;
    }

    private ElementAutType formattaLuogoSintetico(Tb_luogo tb_luogo){
        LuogoType datiElementoType = new LuogoType();
        datiElementoType.setTipoAuthority(SbnAuthority.LU);
        datiElementoType.setT001(tb_luogo.getLID());
        SbnDatavar datevar = new SbnDatavar(tb_luogo.getTS_VAR());
        datiElementoType.setT005(datevar.getT005Date());
        datiElementoType.setLivelloAut(
            SbnLivello.valueOf(Decodificatore.livelloSoglia(tb_luogo.getCD_LIVELLO())));
        datiElementoType.setFormaNome(
            SbnFormaNome.valueOf(tb_luogo.getTP_FORMA()));
        A260 risposta_t260 = new A260();
        risposta_t260.setD_260(tb_luogo.getDS_LUOGO());
        datiElementoType.setT260(risposta_t260);
        A100 a100 = new A100();
        try {
            SbnData date = new SbnData(tb_luogo.getTS_INS());
            a100.setA_100_0(
                org.exolab.castor.types.Date.parseDate(
                        date.getXmlDate()));
        } catch (ParseException ecc) {
            log.error(
                "Formato data non corretto: luogo.ts_ins:"
                    + tb_luogo.getTS_INS());
        }

        datiElementoType.setT100(a100);
        ElementAutType elemento = new ElementAutType();
        elemento.setDatiElementoAut(datiElementoType);
        return elemento;
    }

    private ElementAutType formattaLuogoAnalitico(Tb_luogo tb_luogo) throws IllegalArgumentException, InvocationTargetException, Exception {
        LuogoType datiElementoType = new LuogoType();
        datiElementoType.setTipoAuthority(SbnAuthority.LU);
        SbnDatavar datevar = new SbnDatavar(tb_luogo.getTS_VAR());
        datiElementoType.setT005(datevar.getT005Date());
        datiElementoType.setT001(tb_luogo.getLID());
        datiElementoType.setLivelloAut(
            SbnLivello.valueOf(Decodificatore.livelloSoglia(tb_luogo.getCD_LIVELLO())));
        datiElementoType.setFormaNome(
            SbnFormaNome.valueOf(tb_luogo.getTP_FORMA()));
        A260 risposta_t260 = new A260();
        risposta_t260.setA_260(tb_luogo.getCD_PAESE());
        risposta_t260.setD_260(tb_luogo.getDS_LUOGO());
        datiElementoType.setT260(risposta_t260);
        if (tb_luogo.getNOTA_LUOGO() != null) {
            A300 t300 = new A300();
            t300.setA_300(tb_luogo.getNOTA_LUOGO());
            datiElementoType.setT300(t300);
        }
     // evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità
     // di gestire i campi nota informativa , nota catalogatore e legame a repertori
        if (tb_luogo.getNOTA_CATALOGATORE() != null) {
             A830 a830 = new A830();
             a830.setA_830(tb_luogo.getNOTA_CATALOGATORE());
        	 datiElementoType.setT830(a830);
        }


        datiElementoType.setT260(risposta_t260);
        A100 a100 = new A100();
        try {
            SbnData date = new SbnData(tb_luogo.getTS_INS());
            a100.setA_100_0(
                org.exolab.castor.types.Date.parseDate(
                        date.getXmlDate()));
        } catch (ParseException ecc) {
            log.error(
                "Formato data non corretto: luogo.ts_ins:"
                    + tb_luogo.getTS_INS());
        }

        // evolutive ottobre 2015 almaviva2 -  Nella gestione dei luoghi viene data la possibilità
        // di gestire i campi nota informativa , nota catalogatore e legame a repertori
		datiElementoType.setT100(a100);
        ElementAutType elemento = new ElementAutType();
        elemento.setDatiElementoAut(datiElementoType);
        aggiungiLegami(tb_luogo, elemento);

        return elemento;
    }

    protected void aggiungiLegami(Tb_luogo luogo, ElementAutType elemento)
    throws IllegalArgumentException, InvocationTargetException, Exception {

        RepertorioLuogo repLuo = new RepertorioLuogo();
        List vettore = repLuo.cercaCitazioniInRepertori(luogo.getLID(), _tipoOrd);
        if (vettore.size() > 0) {
            LegamiType legamiType = new LegamiType();
            legamiType.setIdPartenza(luogo.getLID());
            for (int i = 0; i < vettore.size(); i++) {
                Tr_rep_luo rep_luo = (Tr_rep_luo) vettore.get(i);
                ArrivoLegame arr = formattaLegameRepertorio(rep_luo);
                if (arr != null)
                    legamiType.addArrivoLegame(arr);
            }
            if (legamiType.getArrivoLegameCount() > 0)
                elemento.addLegamiElementoAut(legamiType);
        }

    }

    protected ArrivoLegame formattaLegameRepertorio(Tr_rep_luo rep_luo)
    throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {

        ArrivoLegame arrLegame = new ArrivoLegame();

        //Setto i valori del legame
        LegameElementoAutType legameAut = new LegameElementoAutType();
        legameAut.setTipoAuthority(SbnAuthority.RE);
        Repertorio repertorio = new Repertorio();
        Tb_repertorio rep = repertorio.cercaRepertorioId((int) rep_luo.getID_REPERTORIO());
        if (rep != null) {
            legameAut.setIdArrivo(rep.getCD_SIG_REPERTORIO());
            ElementAutType el = new ElementAutType();

            FormatoRepertorio fr = new FormatoRepertorio();
            el.setDatiElementoAut(fr.formattaRepertorioPerEsame(rep));
            legameAut.setElementoAutLegato(el);
            legameAut.setTipoLegame(SbnLegameAut.valueOf("810"));
//            else
//                legameAut.setTipoLegame(SbnLegameAut.valueOf("815"));
            arrLegame.setLegameElementoAut(legameAut);
            //Setto i valori del documento legato
            legameAut.setNoteLegame(rep_luo.getNOTA_REP_LUO());

            arrLegame.setLegameElementoAut(legameAut);
        } else {
            log.warn(
                "Legame con repertorio cancellato. Luogo: "
                    + rep_luo.getLID()
                    + " Rep:"
                    + rep_luo.getID_REPERTORIO());
            arrLegame = null;
        }
        return arrLegame;

    }
}
