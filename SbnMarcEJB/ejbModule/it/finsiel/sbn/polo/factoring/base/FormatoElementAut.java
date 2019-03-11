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
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Repertorio;
import it.finsiel.sbn.polo.oggetti.RepertorioTitoloUniforme;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCercaRicorsivo;
import it.finsiel.sbn.polo.orm.Tb_repertorio;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_rep_tit;
import it.finsiel.sbn.polo.orm.viste.Vl_autore_tit;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnRespons;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe FormatoDocumentoModerno
 * <p>
 * Si occupa della formattazione di un documento di tipo moderno, che è analoga a quella
 * di un titolo, poichè è la più generica.
 * </p>
 *
 * @author
 * @author
 *
 * @version 16-gen-03
 */
public abstract class FormatoElementAut extends FormatoTitolo {

    public static FormatoTitolo getInstance(
        SbnTipoOutput tipoOut,
        String tipoOrd,
        Tb_titolo titolo, SbnUserType user) {
        FormatoElementAut formato = null;

        if (titolo.getTP_MATERIALE().equals("U") && specializzaMateriale(titolo.getTP_MATERIALE(), user)) {
            formato = new FormatoTitoloUniformeMusica();

        } else
            formato = new FormatoTitoloUniforme();


        formato.tipoOrd = tipoOrd;
        formato.tipoOut = tipoOut;
        return formato;

    }

    /** formatta documento
     * Da Sistemare
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException */
    public Object formattaDocumentoLegatoPerEsame(Tb_titolo titolo, BigDecimal versione)
        throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
        ElementAutType elAut = new ElementAutType();
        elAut.setDatiElementoAut(formattaDocumentoPerEsameAnalitico(titolo));
        LegamiType[] lega = formattaLegamiPerEsameAnalitico(titolo, versione);
        elAut.setLegamiElementoAut(lega);
        TitoloCercaRicorsivo titoloCerca =
            new TitoloCercaRicorsivo(tipoOut, tipoOrd, user, this);
        LegamiType leg = titoloCerca.formattaLegamiDocDoc(titolo);
        //Se ci sono legami li aggiungo? Si, non di altri documenti.
        if (leg.enumerateArrivoLegame().hasMoreElements())
            elAut.addLegamiElementoAut(leg);

        return elAut;
    }

    /** formatta i legami per un esame analitico, cercando nel DB
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException */
    public LegamiType[] formattaLegamiPerEsameAnalitico(Tb_titolo titolo, BigDecimal versione)
        throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {

        //Da sistemare
        LegamiType[] legami = null;
        Titolo titDB = new Titolo();
        //legami con documenti
        List vettore;
        List legamiVec = new ArrayList();
        //Legami con autori
        Autore autDB = new Autore();
        vettore = autDB.cercaAutorePerTitolo(titolo.getBID());
        if (vettore.size() > 0) {
            LegamiType legamiType = new LegamiType();
            legamiType.setIdPartenza(titolo.getBID());
            for (int i = 0; i < vettore.size(); i++) {
                Vl_autore_tit aut_tit = (Vl_autore_tit) vettore.get(i);
                if (filtraLegame(aut_tit.getCD_RELAZIONE(), "Autore")) {
                    legamiType.addArrivoLegame(
                        formattaLegameAutoreEsameAnalitico(titolo, aut_tit, versione));
                }
            }
            if (legamiType.getArrivoLegameCount() > 0)
                legamiVec.add(legamiType);
        }

        legami = new LegamiType[legamiVec.size()];
        for (int i = 0; i < legami.length; i++) {
            legami[i] = (LegamiType) legamiVec.get(i);
        }
        return legami;
    }

 	//Inizio intervento almaviva2  - BUG Mantis 4515 collaudo. si inserisce la modifica già fatta su protocollo di Indice
    // per caricare i REPERTORI
	//Mantis 2223. Aggiunta la classe formattaLegamiPerEsameAnaliticoeRep in sostituzione a formattaLegamiPerEsameAnalitico
	//poichè la classe precedente non caricava i repertori e nel caso dei titoli uniformi musica questi non venivano
	//visualizzati.
	public LegamiType[] formattaLegamiPerEsameAnaliticoeRep(Tb_titolo titolo, BigDecimal versione)
		throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
			LegamiType[] legami = null;

			Titolo titDB = new Titolo();
			//legami con documenti -> viene fatto dall'esterno, credo
			//TitoloCerca titCerca = new TitoloCerca(tipoOut, tipoOrd, conn, this);
			//titCerca.formattaLegamiDocDoc(titolo);
			List legamiVec = new ArrayList();
			List vettore;
			//Legami con repertori
			RepertorioTitoloUniforme repAut = new RepertorioTitoloUniforme();
			vettore = repAut.cercaCitazioniInRepertori(titolo.getBID(), tipoOrd);
			if (vettore.size() > 0) {
				LegamiType legamiType = new LegamiType();
				legamiType.setIdPartenza(titolo.getBID());
				for (int i = 0; i < vettore.size(); i++) {
					//Da sostituire con la vista Vl_repertorio_aut
					Tr_rep_tit rep_tit = (Tr_rep_tit) vettore.get(i);
					legamiType.addArrivoLegame(formattaLegameRepertorio(rep_tit));
				}
				if (legamiType.getArrivoLegameCount() > 0)
					legamiVec.add(legamiType);
			}

			//Legami con autori
			Autore autDB = new Autore();
			vettore = autDB.cercaAutorePerTitolo(titolo.getBID());
			if (vettore.size() > 0) {
				LegamiType legamiType = new LegamiType();
				legamiType.setIdPartenza(titolo.getBID());
				for (int i = 0; i < vettore.size(); i++) {
					Vl_autore_tit aut_tit = (Vl_autore_tit) vettore.get(i);
					if (filtraLegameAutore(aut_tit)) {
						legamiType.addArrivoLegame(formattaLegameAutoreEsameAnalitico(titolo, aut_tit, versione));
					}
				}
				if (legamiType.getArrivoLegameCount() > 0)
					legamiVec.add(legamiType);
			}
			legami = new LegamiType[legamiVec.size()];
			for (int i = 0; i < legami.length; i++) {
				legami[i] = (LegamiType) legamiVec.get(i);
			}
			return legami;
		}

	// Formatta un legame tra repertorio e titolo
   protected ArrivoLegame formattaLegameRepertorio(Tr_rep_tit rep_tit) throws EccezioneDB, InfrastructureException {
	   ArrivoLegame arrLegame = new ArrivoLegame();

	   //Setto i valori del legame
	   LegameElementoAutType legameAut = new LegameElementoAutType();
	   legameAut.setTipoAuthority(SbnAuthority.RE);
	   Repertorio repertorio = new Repertorio();
	   arrLegame.setLegameElementoAut(legameAut);

	   // Modifica L.almaviva2 04.07.2011 - Mantis 4542  (Esercizio)
	   // Corretto errore che impediva la visualizzazione di Tit.Uniformi con repertori agganciati;
       Tb_repertorio rep = repertorio.cercaRepertorioId((int) rep_tit.getID_REPERTORIO());
       if (rep_tit.getFL_TROVATO().equals("S"))
           legameAut.setTipoLegame(SbnLegameAut.valueOf("810"));
       else
           legameAut.setTipoLegame(SbnLegameAut.valueOf("815"));
       //Setto i valori del documento legato
	   legameAut.setNoteLegame(rep_tit.getNOTA_REP_TIT());

	   if (rep != null) {
		   legameAut.setIdArrivo(rep.getCD_SIG_REPERTORIO());
		   ElementAutType el = new ElementAutType();
		   FormatoRepertorio fr = new FormatoRepertorio();
		   el.setDatiElementoAut(fr.formattaRepertorioPerEsame(rep));
		   legameAut.setElementoAutLegato(el);
	   } else
		   legameAut.setIdArrivo("");
	   arrLegame.setLegameElementoAut(legameAut);
	   return arrLegame;
   }

   //Fine intervento almaviva2  - BUG Mantis 4515 collaudo




    /**
     * Prepara legame tra titolo e autore
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public ArrivoLegame formattaLegameAutoreEsameAnalitico(
        Tb_titolo tit_partenza,
        Vl_autore_tit relaz, BigDecimal versione)
        throws IllegalArgumentException, InvocationTargetException, Exception{
        ArrivoLegame arrLegame = new ArrivoLegame();
        //Setto i valori del legame
        LegameElementoAutType legameAut = new LegameElementoAutType();
        legameAut.setTipoAuthority(SbnAuthority.valueOf("AU"));
        legameAut.setIdArrivo(relaz.getVID());
        if (!relaz.getCD_RELAZIONE().trim().equals(""))
            legameAut.setRelatorCode(relaz.getCD_RELAZIONE());
        if (!relaz.getFL_INCERTO().equals(" "))
            legameAut.setIncerto(SbnIndicatore.valueOf(relaz.getFL_INCERTO()));
        if (!relaz.getFL_SUPERFLUO().equals(" "))
            legameAut.setSuperfluo(SbnIndicatore.valueOf(relaz.getFL_SUPERFLUO()));
        legameAut.setNoteLegame(relaz.getNOTA_TIT_AUT());
        String tp_nome = relaz.getTP_NOME_AUT();
        String tp_respons = relaz.getTP_RESPONSABILITA();
        legameAut.setTipoRespons(SbnRespons.valueOf(tp_respons));
        legameAut.setTipoLegame(convertiTipoLegameAutore(tp_nome, tp_respons));
        arrLegame.setLegameElementoAut(legameAut);
        //Setto i valori del documento legato
        FormatoAutoreTitolo forAut =
            new FormatoAutoreTitolo(tipoOut, tipoOrd, user);
        legameAut.setElementoAutLegato(forAut.formattaAutore(relaz));
        arrLegame.setLegameElementoAut(legameAut);
        return arrLegame;
    }

    /** Filtra legami tra documenti */
    public boolean filtraLegame(String codiceLegame, String tipoDocumento) {
        //per ora non ci sono filtri.
        return true;
    }
    protected abstract DatiElementoType formattaDocumentoPerEsameAnalitico(Tb_titolo titolo) throws EccezioneDB, InfrastructureException;

}
