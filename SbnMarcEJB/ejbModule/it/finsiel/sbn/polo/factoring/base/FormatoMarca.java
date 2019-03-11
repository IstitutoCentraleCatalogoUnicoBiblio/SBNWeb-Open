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

import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnData;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Link_multim;
import it.finsiel.sbn.polo.oggetti.Marca;
import it.finsiel.sbn.polo.oggetti.Parola;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_marca;
import it.finsiel.sbn.polo.orm.Tb_parola;
import it.finsiel.sbn.polo.orm.Ts_link_multim;
import it.finsiel.sbn.polo.orm.viste.Vl_marca_aut;
import it.finsiel.sbn.polo.orm.viste.Vl_repertorio_mar;
import it.iccu.sbn.ejb.model.unimarcmodel.A100;
import it.iccu.sbn.ejb.model.unimarcmodel.A921;
import it.iccu.sbn.ejb.model.unimarcmodel.AllineaInfoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.C856;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.MarcaType;
import it.iccu.sbn.ejb.model.unimarcmodel.OggettoVariatoType;
import it.iccu.sbn.ejb.model.unimarcmodel.OggettoVariatoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

/**
 * @author
 * Metodo per preparare l'output
 */
public class FormatoMarca {
    //tipoOut: tipo di output richiesto: Esame, Lista
    private SbnTipoOutput tipoOut = null;
    String tipoOrd;
    SbnUserType user;


    //file di log
    static Category log = Category.getInstance("iccu.box.FormatoMarca");

    public FormatoMarca(
        SbnTipoOutput tipoOut,
        String tipoOrd,
        SbnUserType user) {
        this.tipoOut = tipoOut;

        this.tipoOrd = tipoOrd;
        this.user = user;
    }
    /**
     * Formatta i campi in output per il timbro di condivisione
     */
//	public boolean formattaTimbroCondivisione(Tb_marca tabella, boolean legame) {
//		if(legame){
//			log.debug("TIMBRO CONDIVISIONE ---------- LEGAME-dati da tabella" + tabella.getClass().getName());
//		}else{
//			log.debug("TIMBRO CONDIVISIONE ---------- ELEMENTO-dati da tabella" + tabella.getClass().getName());
//		}
//		if (tabella.getFL_CONDIVISO() == null) {
//			System.out
//					.println("ERRORE"
//							+ "IL FLAG CONDIVISO NON e' ISTANZIATO CORRETTAMENTE NELLA VISTA"
//							+ tabella.getClass().getName());
//			return true;
//		} else {
//			if (tabella.getFL_CONDIVISO().equals("S"))
//				return true;
//			else
//				return false;
//		}
//	}

    public ElementAutType formattaMarca(Tb_marca marca)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        ElementAutType elemento = new ElementAutType();
        if (tipoOut.getType() == SbnTipoOutput.VALUE_0.getType())
            return formattaMarcaPerEsameAnalitico(marca);
//        else if (tipoOut.equals(tipoOut.VALUE_2))
 //           return formattaMarcaPerListaRidotta(marca);
        else
            return formattaMarcaPerLista(marca);
    }

    /**
     * Method formattaMarcaPerListaRidotta.
     * @param marca
     * @return ElementAutType
     * @throws Exception
     * @throws InvocationTargetException
     */
    private ElementAutType formattaMarcaPerLista(Tb_marca marca)
        throws InvocationTargetException, Exception {
        ElementAutType elemento = new ElementAutType();
        elemento.setDatiElementoAut(creaMarcaPerListaSintetica(marca));
        LegamiType legami;
        legami = cercaLegamiMarcaRepertorio(marca.getMID());
        if (legami.getArrivoLegameCount() != 0)
            elemento.addLegamiElementoAut(legami);
        return elemento;
    }

    /**
     * Method formattaMarcaPerEsameAnalitico.
     * @param marca
     * @return ElementAutType
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private ElementAutType formattaMarcaPerEsameAnalitico(Tb_marca marca)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        ElementAutType elemento = new ElementAutType();
        //		DatiElementoType datiElementoAut;
        elemento.setDatiElementoAut(creaMarcaPerEsameAnalitico(marca));
        LegamiType legami;
        legami = cercaLegamiMarcaAutore(marca.getMID());
        if (legami.getArrivoLegameCount() != 0)
            elemento.addLegamiElementoAut(legami);
        legami = cercaLegamiMarcaRepertorio(marca.getMID());
        if (legami.getArrivoLegameCount() != 0)
        	elemento.addLegamiElementoAut(legami);
        return elemento;
    }

    private MarcaType creaMarcaPerListaSintetica(Tb_marca marca)
        throws   InvocationTargetException, Exception {
        MarcaType datiElemento = new MarcaType();
        datiElemento.setTipoAuthority(SbnAuthority.MA);
        if(marca.getFL_CONDIVISO() != null)
        	datiElemento.setCondiviso(DatiElementoTypeCondivisoType.valueOf(marca.getFL_CONDIVISO()));

        datiElemento.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(marca.getCD_LIVELLO())));
        datiElemento.setT001(marca.getMID());
        A921 a921 = new A921();
        a921.setA_921(marca.getDS_MARCA());
        a921.setE_921(marca.getDS_MOTTO());
        datiElemento.setT921(a921);
//        if (tipoOut.equals(tipoOut.VALUE_1)) { //massimo
//            datiElemento.setT005(marca.getTS_VAR().getOriginalDate());
//        }
        List link;
        Ts_link_multim ts_link_multim = new Ts_link_multim();
        Marca marcaCerca = new Marca();
        TableDao tavola = marcaCerca.cercaLinkMarcaImmagine(marca.getMID());
        link = tavola.getElencoRisultati();

        if (link.size() > 0) {
        	for (int j=0; j<link.size();j++){
            ts_link_multim = (Ts_link_multim) link.get(j);
            C856 c856 = new C856();
            Link_multim linkMultim = new Link_multim();
            c856.setC9_856_1(linkMultim.estraiImmagine(ts_link_multim));
            datiElemento.addT856(c856);
        	}
        }

        return datiElemento;
    }

    public MarcaType creaMarcaPerEsameAnalitico(Tb_marca marca)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        MarcaType datiElemento = new MarcaType();
        datiElemento.setTipoAuthority(SbnAuthority.MA);
        if(marca.getFL_CONDIVISO() != null)
        	datiElemento.setCondiviso(DatiElementoTypeCondivisoType.valueOf(marca.getFL_CONDIVISO()));

        datiElemento.setLivelloAut(SbnLivello.valueOf(Decodificatore.livelloSoglia(marca.getCD_LIVELLO())));
        datiElemento.setT001(marca.getMID());
        SbnDatavar date = new SbnDatavar(marca.getTS_VAR());
        datiElemento.setT005(date.getT005Date());
        A100 a100 = new A100();
        try {
            SbnData date1 = new SbnData(marca.getTS_INS());
            a100.setA_100_0(
                org.exolab.castor.types.Date.parseDate(
                    date1.getXmlDate()));
        } catch (ParseException ecc) {
            log.error(
                "Formato data non corretto: autore.ts_ins:"
                    + marca.getTS_INS());
        }
        datiElemento.setT100(a100);
        A921 a921 = new A921();
        a921.setA_921(marca.getDS_MARCA());
        a921.setD_921(marca.getNOTA_MARCA());
        a921.setE_921(marca.getDS_MOTTO());
        Marca marcaCerca = new Marca();
        Ts_link_multim ts_link_multim = new Ts_link_multim();
        List link;
        TableDao tavola = marcaCerca.cercaLinkMarcaImmagine(marca.getMID());
        link = tavola.getElencoRisultati();

        if (link.size() > 0) {
        	for (int j=0; j<link.size();j++){
        	ts_link_multim = new Ts_link_multim();
            ts_link_multim = (Ts_link_multim) link.get(j);
            C856 c856 = new C856();
            c856.setU_856(ts_link_multim.getURI_MULTIM());
            Link_multim linkMultim = new Link_multim();
            c856.setC9_856_1(linkMultim.estraiImmagine(ts_link_multim));
            datiElemento.addT856(c856);
        	}
        }
        Parola parola = new Parola();
        List parole = parola.cercaParoleDiMarca(marca.getMID());
        String[] paroleArray = new String[parole.size()];
        for (int i = 0; i < parole.size(); i++) {
            paroleArray[i] = ((Tb_parola) parole.get(i)).getPAROLA();
        }
        a921.setB_921(paroleArray);
        //cerca parole
        datiElemento.setT921(a921);
        SbnDatavar data = new SbnDatavar(marca.getTS_VAR());
        datiElemento.setT005(data.getT005Date());
        return datiElemento;
    }

    /**
     * Cerca i legami di una marca con un autore
     * viene utilizzata la vista vl_marca_aut
     * Questo tipo di legame deve essere inserito solo nell'esame analitico
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private LegamiType cercaLegamiMarcaAutore(String mid)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        List marcaAutore = new ArrayList();
        Marca marca = new Marca();
        TableDao tavola = marca.cercaMarcaAutore(mid);
        marcaAutore = tavola.getElencoRisultati();

        LegamiType legame = new LegamiType();
        legame.setIdPartenza(mid);
        LegameElementoAutType legElAut = new LegameElementoAutType();
        ArrivoLegame[] arrivoLegame = new ArrivoLegame[marcaAutore.size()];
        for (int i = 0; i < marcaAutore.size(); i++) {
            legElAut = new LegameElementoAutType();
            Vl_marca_aut ma = (Vl_marca_aut) marcaAutore.get(i);
            String vid = ma.getVID();
            legElAut.setIdArrivo(vid);
            legElAut.setTipoAuthority(SbnAuthority.AU);
            legElAut.setTipoLegame(SbnLegameAut.valueOf("921"));
            legElAut.setNoteLegame(ma.getNOTA_AUT_MAR());
            //ElementAutType elemento = new ElementAutType();
            FormatoAutore formatoAutore =
                new FormatoAutore(tipoOut, tipoOrd, user);
            Autore autore = new Autore();
            Tb_autore tb_autore = autore.estraiAutorePerID(vid);
            //legElAut.setElementoAutLegato(elemento);
            legElAut.setElementoAutLegato(
                formatoAutore.formattaAutore(tb_autore));
            arrivoLegame[i] = new ArrivoLegame();
            arrivoLegame[i].setLegameElementoAut(legElAut);
        }
        legame.setArrivoLegame(arrivoLegame);
        return legame;
    }

    /**
     * Cerca i legami di una marca con un repertorio
     * dato in input il mid della marca ottengo tutti i repertori a questa collegati
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     *
     */
    private LegamiType cercaLegamiMarcaRepertorio(String mid)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        List marcaRepertorio = new ArrayList();
        Marca marca = new Marca();
        TableDao tavola = marca.cercaMarcaPerRepertorio(mid, tipoOrd);
        marcaRepertorio = tavola.getElencoRisultati();

        LegamiType legame = new LegamiType();
        legame.setIdPartenza(mid);
        LegameElementoAutType legElAut = new LegameElementoAutType();
        ArrivoLegame[] arrivoLegame = new ArrivoLegame[marcaRepertorio.size()];
        Repertorio repertorio = new Repertorio();
        for (int i = 0; i < marcaRepertorio.size(); i++) {
            legElAut = new LegameElementoAutType();
            Vl_repertorio_mar rep = (Vl_repertorio_mar) marcaRepertorio.get(i);
            FormatoRepertorio fr = new FormatoRepertorio();
            ElementAutType el = new ElementAutType();
            el.setDatiElementoAut(fr.formattaRepertorioPerEsame(rep));
            legElAut.setElementoAutLegato(el);
            legElAut.setIdArrivo(rep.getCD_SIG_REPERTORIO());
            legElAut.setTipoAuthority(SbnAuthority.RE);
            legElAut.setTipoLegame(SbnLegameAut.valueOf("810"));
            String nota =
                ((Vl_repertorio_mar) marcaRepertorio.get(i)).getNOTA_REP_MAR();
            legElAut.setNoteLegame(nota);
            legElAut.setCitazione((int) rep.getPROGR_REPERTORIO());
            arrivoLegame[i] = new ArrivoLegame();
            arrivoLegame[i].setLegameElementoAut(legElAut);
        }
        legame.setArrivoLegame(arrivoLegame);
        return legame;
    }

    /**
     * Prepara un elemento di allineamento inserendovi le marche estratte dal db in base al mid
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
        Marca marca = new Marca();
        Tb_marca tb_marca = marca.estraiMarcaPerID(allinea.getT001());
        if (tb_marca == null)
            throw new EccezioneDB(3001, "Marca non presente in base dati");
        FormatoMarca forMar = new FormatoMarca(tipoOut,null,utente);
        OggettoVariatoType oggVar = allinea.getOggettoVariato();
        OggettoVariatoTypeChoice oggettoVariatoTypeChoice = new OggettoVariatoTypeChoice();
        oggettoVariatoTypeChoice.setElementoAut(forMar.formattaMarca(tb_marca));
        oggVar.setOggettoVariatoTypeChoice(oggettoVariatoTypeChoice);
        //oggVar.setElementoAut(forMar.formattaMarca(tb_marca));
        allinea.setOggettoVariato(oggVar);
        return allinea;
    }
    /** Formatta una marca per essere presentata legata ad un autore,
     * serve per evitare i loop: non ricerca ulteriormente gli autori legati.
     * @param marca
     * @return
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public ElementAutType formattaMarcaPerAutore(Tb_marca marca)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        ElementAutType elemento = new ElementAutType();
        //      DatiElementoType datiElementoAut;
        elemento.setDatiElementoAut(creaMarcaPerEsameAnalitico(marca));
        LegamiType legami;
        legami = cercaLegamiMarcaRepertorio(marca.getMID());
        if (legami.getArrivoLegameCount() != 0)
            elemento.addLegamiElementoAut(legami);
        return elemento;
    }


}
