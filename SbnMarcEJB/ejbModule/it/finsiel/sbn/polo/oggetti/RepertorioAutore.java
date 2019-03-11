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

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_rep_autResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tr_rep_aut;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

public class RepertorioAutore extends Tr_rep_aut {


	private static final long serialVersionUID = -5360902526997379235L;

	private static Category log = Category.getInstance(RepertorioAutore.class);

    private boolean filtriValorizzati;

    public RepertorioAutore() {
        super();
    }

    /**
    * Method cercaCitazioniInRepertori.
    * Se avessi una vista Vl_repertorio_aut potrei resituire un elenco di repertori legati
    * all'autore. invece restituisce un vettore di Tr_rep_aut.
    * @param vid
    * @return TableDao di Tr_rep_aut (magari di Vl_repertorio_aut)
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
    */
    public List cercaCitazioniInRepertori(String vid, String ordinamento) throws IllegalArgumentException, InvocationTargetException, Exception {
        setVID(vid);

        Tr_rep_autResult tavola = new Tr_rep_autResult(this);


        tavola.executeCustom("selectPerAutore", ordinamento);
        List v = tavola.getElencoRisultati();

        return v;
    }

    private List cercaLegame(String idAutore, String idRep) throws IllegalArgumentException, InvocationTargetException, Exception {
        Repertorio repDB = new Repertorio();
        Tb_repertorio rep = repDB.cercaRepertorioPerCdSig(idRep);
        setVID(idAutore);
        setID_REPERTORIO(rep.getID_REPERTORIO());
        Tr_rep_autResult tr_autResult = new Tr_rep_autResult(this);


        tr_autResult.valorizzaElencoRisultati(tr_autResult.selectPerKey(this.leggiAllParametro()));
        List v = tr_autResult.getElencoRisultati();

        return v;

    }

    /**
     * metodo per la ricerca dei repertori a partire da un Autore
     * Utilizzato all'interno di CercaRepertorio
     * Restituisce un vettore di legami
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public TableDao cercaRepertorioPerAutore(String idAutore) throws IllegalArgumentException, InvocationTargetException, Exception {
        setVID(idAutore);
        Tr_rep_autResult tr_autResult = new Tr_rep_autResult(this);


        tr_autResult.executeCustom("selectRepertorioPerAutore");
        return tr_autResult;
    }

    public int contaRepertorioPerAutore(String idAutore, CercaDatiAutType cercaRepertorio)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        setVID(idAutore);
        Tr_rep_autResult tavola = new Tr_rep_autResult(this);

        if (!filtriValorizzati)
            valorizzaFiltri(cercaRepertorio);
        tavola.executeCustom("countRepertorioPerAutore");
        int n = conta(tavola);

        return n;
    }

    public void valorizzaFiltri(CercaDatiAutType cerca) {
        valorizzaFiltri(this, cerca);
    }

    /** Valorizza i filtri */
    public Tr_rep_aut valorizzaFiltri(Tr_rep_aut tr_rep_aut, CercaDatiAutType cerca) {
        filtriValorizzati = true;
        if (cerca == null)
            return tr_rep_aut;
        if (cerca.getT005_Range() != null) {
            int filtro = cerca.getT005_Range().getTipoFiltroDate();
            if (filtro < 2) {
                tr_rep_aut.settaParametro(TableDao.XXXdata_var_Da, cerca.getT005_Range().getDataDa().toString());
                tr_rep_aut.settaParametro(TableDao.XXXdata_var_A, cerca.getT005_Range().getDataA().toString());
            } else if (filtro == 2) {
                tr_rep_aut.settaParametro(TableDao.XXXdata_ins_Da, cerca.getT005_Range().getDataDa().toString());
                tr_rep_aut.settaParametro(TableDao.XXXdata_ins_A, cerca.getT005_Range().getDataA().toString());
            }
        }
        return tr_rep_aut;
    }

    /** Legge da una tavola il valore del COUNT(*) */
    private int conta(TableDao tavola) throws EccezioneDB {
    	return tavola.getCount();
    }

    /**
     * Method validaPerModificaLegame
     * @param elementoAut
     * @return boolean
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void validaPerModificaLegame(
        String vid,
        ArrivoLegame legame,
        SbnTipoOperazione operazione,
        LegamiType[] legami,
        TimestampHash timeHash,
        boolean cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if (operazione == null)
            throw new EccezioneSbnDiagnostico(3102, "Tipo operazione sul legame non specificato");
        if (operazione.getType() == SbnTipoOperazione.CREA_TYPE)
            validaPerCreaLegame(vid, legame, legami);
        else if (
            operazione.getType() == SbnTipoOperazione.CANCELLA_TYPE || operazione.getType() == SbnTipoOperazione.MODIFICA_TYPE)
            validaPerModificaLegame(vid, legame, timeHash);
    }

    /**
     * Verifica la presenza della marca e che esista il legame.
     * @param elementoAut
     * @return boolean
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void validaPerModificaLegame(String vid, ArrivoLegame legame, TimestampHash timeHash)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        Repertorio repertorio = new Repertorio();
        LegameElementoAutType legamedoc = legame.getLegameElementoAut();
        Tb_repertorio tbm = repertorio.cercaRepertorioPerCdSig(legamedoc.getIdArrivo());
        if (tbm == null)
            throw new EccezioneSbnDiagnostico(3023, "repertorio non esistente");
        timeHash.putTimestamp("Tb_repertorio", "" + tbm.getID_REPERTORIO(), new SbnDatavar( tbm.getTS_VAR()));
        List v = cercaLegame(vid, legamedoc.getIdArrivo());
        if (v.size() == 0)
            //Non esiste il legame
            throw new EccezioneSbnDiagnostico(3029, "Legame non esistente");
        timeHash.putTimestamp("Tr_aut_rep", vid + tbm.getID_REPERTORIO(), new SbnDatavar( tbm.getTS_VAR()));
    }

    /**
     * Metodo per la validazione della creazione di un legame repertorio autore
     * @param vid
     * @param legame
     * @param legami Deve essere passato solo per il modifica: server per controllare le cancellazioni.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void validaPerCreaLegame(String vid, ArrivoLegame legame, LegamiType[] legami)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        LegameElementoAutType legEl = legame.getLegameElementoAut();

        if (!(legEl.getTipoLegame().getType() == SbnLegameAut.valueOf("810").getType()
            || legEl.getTipoLegame().getType() == SbnLegameAut.valueOf("815").getType()))
            throw new EccezioneSbnDiagnostico(3031, "Legame con repertorio non valido");
        //Tr_rep_aut tr_aut_aut = new Tr_aut_mar();
        Tb_autore tb_aut1 = new Tb_autore();
        Repertorio repertorio = new Repertorio();

        if (repertorio.cercaRepertorioPerCdSig(legEl.getIdArrivo()) == null)
            throw new EccezioneSbnDiagnostico(3023, "Repertorio da collegare non esistente");
        //solo per il modifica:
        if (legami != null) {
            boolean cancellato = false;
            for (int i = 0; !cancellato && i < legami.length; i++) {
                LegamiType leg = legami[i];
                if (leg.getTipoOperazione().getType() == SbnTipoOperazione.CANCELLA_TYPE)
                    for (int j = 0; !cancellato && j < leg.getArrivoLegameCount(); j++)
                        if (leg.getArrivoLegame(j).getLegameElementoAut() != null)
                            if (leg
                                .getArrivoLegame(j)
                                .getLegameElementoAut()
                                .getTipoAuthority()
                                .getType() == SbnAuthority.RE_TYPE
                                && leg.getArrivoLegame(j).getLegameElementoAut().getIdArrivo().equals(
                                    legEl.getIdArrivo()))
                                cancellato = true;
            }
            if (!cancellato)
                if (cercaLegame(vid, legEl.getIdArrivo()).size() > 0)
                    throw new EccezioneSbnDiagnostico(3030, "Legame con repertorio già esistente");
        }
    }

    /**
     * Inserisce un legame autore-repertorio nel DB
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public boolean inserisciRepertorioAutore(Tr_rep_aut repaut) throws IllegalArgumentException, InvocationTargetException, Exception {
        Tr_rep_autResult tr_rep_autResult = new Tr_rep_autResult(repaut);


        repaut.setFL_CANC(" ");
        if (!controllaEsistenzaLegame(repaut)) {
            repaut.setFL_CANC(" ");
            return tr_rep_autResult.insert(repaut);
        } else {
            repaut.setFL_CANC(" ");
            return tr_rep_autResult.update(repaut);
        }
    }

    /**
     * Method controllaEsistenzaLegame.
     * @param repaut
     * @return boolean
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private boolean controllaEsistenzaLegame(Tr_rep_aut legame) throws IllegalArgumentException, InvocationTargetException, Exception {
        //		boolean esito;
        Tr_rep_autResult tavola = new Tr_rep_autResult(legame);


        tavola.executeCustom("selectPerKeyCancellato");
        List vec = tavola.getElencoRisultati();

        return vec.size() > 0;

    }

    /**
     * Method cancellaAutoreRepertorio.
     * Tr_aut_mar deve avere settati utevar, ts_var vid e id.
     * @param tr_aut_mar
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public boolean cancellaAutoreRepertorio(Tr_rep_aut tr_rep_aut) throws IllegalArgumentException, InvocationTargetException, Exception {
        tr_rep_aut.setFL_CANC("S");
        Tr_rep_autResult t_autmar = new Tr_rep_autResult(tr_rep_aut);

        return t_autmar.executeCustomUpdate("updateCancella");

    }

    /**
     * Method modifica AutoreRepertorio.
     * Tr_aut_mar deve tutti i campi settati( a parte ts_ins e ute_ins)
     * @param tr_aut_mar
     * @throws InfrastructureException
     */
    public boolean modificaAutoreRepertorio(Tr_rep_aut tr_rep_aut) throws EccezioneDB, InfrastructureException {
        tr_rep_aut.setFL_CANC(" ");
        Tr_rep_autResult t_autmar = new Tr_rep_autResult(tr_rep_aut);


        return t_autmar.update(tr_rep_aut);

    }

    public void cancellaLegamiRepertori(String idAutore, String user) throws IllegalArgumentException, InvocationTargetException, Exception {
        setVID(idAutore);
        Tr_rep_autResult tavola = new Tr_rep_autResult(this);


        tavola.executeCustom("selectRepertorioPerAutore");
        List vec = tavola.getElencoRisultati();

        Tr_rep_aut tr_rep_aut;
        for (int i = 0; i < vec.size(); i++) {
            tr_rep_aut = new Tr_rep_aut();
            tr_rep_aut = (Tr_rep_aut) vec.get(i);
            tr_rep_aut.setFL_CANC("S");
            cancellaAutoreRepertorio((Tr_rep_aut) vec.get(i));
        }
    }

    public void cancellaLegamiRepertorio(String idRepertorio, String user) throws IllegalArgumentException, InvocationTargetException, Exception {
        setID_REPERTORIO(Long.parseLong(idRepertorio));
        setUTE_VAR(user);
        Tr_rep_autResult tavola = new Tr_rep_autResult(this);
        tavola.executeCustom("cancellaLegamiRepertorio");
    }

    /**
     * Method spostaLegami.
     * @param idPartenza
     * @param idArrivo
     * @param user
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public void spostaLegami(String idPartenza, String idArrivo, String user) throws IllegalArgumentException, InvocationTargetException, Exception {
        List elenco = cercaCitazioniInRepertori(idPartenza,null);
        cancellaLegamiRepertori(idPartenza, user);
        for (int i = 0;i<elenco.size();i++) {
            Tr_rep_aut legame = (Tr_rep_aut)elenco.get(i);
            legame.setUTE_VAR(user);
            legame.setVID(idArrivo);
            legame.setFL_CANC(" ");
            Tr_rep_autResult tabella = new Tr_rep_autResult(legame);


            tabella.executeCustom("selectEsistenza");
            List v = tabella.getElencoRisultati();
            if (v.size()>0) {
                legame = (Tr_rep_aut)v.get(0);
                tabella = new Tr_rep_autResult(legame);
                legame.setFL_CANC(" ");
                legame.setUTE_VAR(user);
                tabella.update(legame);
            } else {
                tabella.insert(legame);
            }

        }

    }

}
