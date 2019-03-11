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

import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoTitolo;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.oggetti.AllineaTitoloBiblioteca;
import it.finsiel.sbn.polo.oggetti.Biblioteca;
import it.finsiel.sbn.polo.orm.viste.Vl_allinea_tit_bib;
import it.iccu.sbn.ejb.model.unimarcmodel.AllineaInfoType;
import it.iccu.sbn.ejb.model.unimarcmodel.FiltraAllineaTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.OggettoVariatoType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoLocalizza;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoModifica;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

/**
 * Esegue le operazioni necessarie ad estrarre i titoli da allineare per il polo
 * richiedente:
 * Sequenza operativa:
 * Legge il tipo di allineamento scelto dal polo con il validator
 *
 * SE VALE 'flag' (1):
 * Si utilizza la vista VL_Allinea_tit_bib con la condizione su cd_polo, cd_
 * biblioteca in input:
 *
 * se tipoInfo='Possesso' si aggiunge alla where: AND fl_possesso = 'S'
 * se tipoInfo='Gestione' si aggiunge alla where: AND fl_gestione = 'S'
 *
 * Se ci sono degli elementi in FiltraAllineaTit si aggiungono le condizioni su
 * tb_titolo.
 *
 * Si effettua la count per verificare se il servizio può essere svolto on-line o
 * batch: se la count supera il valore soglia (da gestire nelle properties: lo
 * stesso delle liste analitiche titoli ) si registra l'elaborazione come batch e
 * si restituisce il numero del batch in output, altrimenti si restituisce la
 * lista secondo il tipo output richiesto.
 *
 * Se fl_allinea_sbnmarc = 'A' e fl_allinea = ' ' deve essere impostato
 * uno o più tra fl_allinea_cla, fl_allinea_sog,fl_allinea_luo,fl_allinea_rep.
 * In questo caso occorre verificare se il polo gestisce i legami aggiornati;
 * si verifica utilizzando le funzioni del validator (validator.getParametriAuthority(...utente))
 * che restituisce un vettore da cui vengono estratti i cd_par_auth ("CL","SO","RE","LU")
 * con il relativo fl_leg_auth di tb_par_auth; per l'elemento corrispondente al fl_allinea_.. acceso
 * deve essere 'S' altrimenti si scarta il bid.
 *
 * Se SbnTipoOutput 001 si restituisce la lista sintetica  in
 * AllineaInfoType impostando tipoModifica come descritto di seguito .
 *
 * Se SbnTipoOutput 000
 * Si utilizzano i metodi del servizio di ricerca per la preparazione della lista
 * sintetica o analitica.
 *
 * Si imposta l'attributo tipoModifica secondo la regola seguente:
 * se fl_allinea o fl_allinea_sbnmarc= C si imposta 'Legami' (VALUE_0)
 * se fl_allinea o fl_allinea_sbnmarc= S si imposta 'Dati' (VALUE_1)
 * se fl_allinea o fl_allinea_sbnmarc= Z si imposta 'Dati e Legami' (VALUE_5)
 * se fl_allinea o fl_allinea_sbnmarc= X si imposta 'Natura' (VALUE_6)
 * se S o Z e tb_titolo.fl_canc='S' si si imposta 'Cancellato' (VALUE_2)
 * se S o Z e tb_titolo.tp_link='F' si si imposta 'Fusione' (VALUE_4)
 * se fl_allinea= ' ' e fl_allinea_sbnmarc= 'A' si imposta 'Legami' (VALUE_0)
 *
 * Se si tratta di Fusione, T001 di AllineaInfoType deve contenere il bid del
 * titolo cancellato (bid) e Documento deve contenere l'esame analitico del titolo di
 * arrivo della fusione (bid_link).
 *
 * Viene passato un oggetto di tipo AllineaInfoType con id, tipoOutput e tipoModifica
 * valorizzato nel metodo 'preparaOutputPerAllinea'
 *
 * SE VALE 'data' (2):
 * la differenza dall'allineamento per flag è la modalità di select dei dati, che
 * dipende dal range  di data selezionato con fl_gestione = 'S'
 *
 * Parte sospesa: tipoAllinea
 * 		Filtro su tipoAllinea se <> 'tutti':
 * 		Se 'Descrizione': si selezionano tr_tit_bib con fl_allinea_sbnmarc o fl_allinea
 * 		= 'S' o 'Z' o 'X'
 * 		Se 'Legami': si selezionano tr_tit_bib con fl_allinea_sbnmarc o fl_allinea=
 * 		'C' o Z
 * Fine parte sospesa
 *
 * Da valutare se è sempre elaborazione differita oppure se per intervalli di data
 * piccoli può essere effettuata on-line. L'intervallo di giorni non deve superare
 * un valore di soglia da definire nelle properties
 *
 * L'output prodotto è uguale al caso precedente, l'impostazione di
 * tipoModifica è legata al tp_link per Fusione, al flag_canc per Cancellato
 * altrimenti sono Dati e Legami
 *
 * usa la stessa vista con where su dataInizio e dataFine su ts_var, e su
 * cd_polo/ cd_biblioteca in input, e eventuali filtri in FiltraAllineaTit
 *
 *
 * @author
 * @version marzo 2003
 */
public class TitoloAllineamento extends AllineaTitoloBiblioteca {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3027842582457842623L;
	Connection _db_conn = null;
    static Category log = Category.getInstance("iccu.serversbnmarc.titoloAllinea");
    BigDecimal schema_version;

    public TitoloAllineamento( BigDecimal schema_V) {
        super();
        this.
        schema_version = schema_V;
    }

    public TableDao allineatoTitolo1(
        String flagAllineamento,
        String utente,
        SbnTipoLocalizza tipoInfo,
        List biblioV,
        SbnDatavar dataInizio,
        SbnDatavar dataFine,
        FiltraAllineaTitType filtraAllinea,
        boolean differita)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        List bibliotecaTableDao = new ArrayList();
        List codiceBiblioteca = new ArrayList();

        String codicePolo = null;
        log.info("VERIFICA TITOLO " + biblioV.size());
        for (int i = 0; i < biblioV.size(); i++) {
            //verifico esistenza di tb_biblioteca
            Biblioteca biblioteca = new Biblioteca();

            bibliotecaTableDao = biblioteca.verificaEsistenza(new String(), (String) biblioV.get(i));
            if (bibliotecaTableDao != null) {
                //verifico le abilitazioni dell'utente per gestire tb_biblioteca
                if (i > 0) {
                } else {
                    log.info("STO PER VERIFICARE abilitazioni polo");
                    codicePolo = ((String) biblioV.get(i)).substring(0, 3);
                    biblioteca.verificaAbilitazioni(codicePolo);
                    log.info("VERIFICATO abilitazioni polo " + codicePolo);
                }
                if ((((String) biblioV.get(i)).trim()).length() > 3) {
                    codiceBiblioteca.add(((String) biblioV.get(i)).substring(3, 6));
                } else {
                    codiceBiblioteca = null;
                }
            } else {
                throw new EccezioneSbnDiagnostico(3101);
            }
        }

        log.info("caricato polo " + codicePolo);
        log.info("caricato biblioteca " + codiceBiblioteca);
        if (dataInizio != null) {
            flagAllineamento = "2";
        }
        return verificaEsistenzaVl_allinea_tit_bib(
                flagAllineamento,
                codicePolo,
                codiceBiblioteca,
                tipoInfo,
                dataInizio,
                dataFine,
                filtraAllinea,
                differita);
    }

    private TableDao verificaEsistenzaVl_allinea_tit_bib(
			String flagAllineamento, String codicePolo,
			List codiceBiblioteca, SbnTipoLocalizza tipoInfo,
			SbnDatavar dataInizio, SbnDatavar dataFine,
			FiltraAllineaTitType filtraAllinea, boolean differita) throws EccezioneSbnDiagnostico {
    	throw new EccezioneSbnDiagnostico(501);
	}

	public List allineatoTitolo2(
            List vlTitBib,
        String flagAllineamento,
        String utente,
        SbnTipoOutput tipoOutput,
        Map parA)
        throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
        List TableDao_response = new ArrayList();
        if (vlTitBib.size() > 0) {
            log.info("esistenza vl_allinea_tit_bib ");
            for (int j = 0; j < vlTitBib.size(); j++) {
                Vl_allinea_tit_bib vl_tit_bib = (Vl_allinea_tit_bib) vlTitBib.get(j);
                log.info("bid                 >>" + j + "<<" + vl_tit_bib.getBID());
                log.info("fl_allinea" + "_sbnmarc >>" + "<<" + vl_tit_bib.getFL_ALLINEA_SBNMARC());
                log.info("fl_allinea" + "         >>" + "<<" + vl_tit_bib.getFL_ALLINEA());
                log.info("fl_allinea" + "_cla     >>" + "<<" + vl_tit_bib.getFL_ALLINEA_CLA());
                log.info("fl_allinea" + "_rep     >>" + "<<" + vl_tit_bib.getFL_ALLINEA_REP());
                log.info("fl_allinea" + "_sog     >>" + "<<" + vl_tit_bib.getFL_ALLINEA_SOG());
                log.info("tp_link   " + "         >>" + "<<" + vl_tit_bib.getTP_LINK());
                log.info("fl_canc   " + "         >>" + "<<" + vl_tit_bib.getFL_CANC());
                log.info("fl_allinea" + "_luo     >>" + "<<" + vl_tit_bib.getFL_ALLINEA_LUO());
                OggettoVariatoType outputV = new OggettoVariatoType();
                if (flagAllineamento.equals("1")) {
                    if ((vl_tit_bib.getFL_ALLINEA()).equals("C")
                        || (vl_tit_bib.getFL_ALLINEA_SBNMARC()).equals("C")) {
                        outputV.setTipoModifica(SbnTipoModifica.VALUE_0);
                    }
                    if ((vl_tit_bib.getFL_ALLINEA()).equals("S")
                        || (vl_tit_bib.getFL_ALLINEA_SBNMARC()).equals("S")) {
                        if ((vl_tit_bib.getFL_CANC()).equals("S")) {
                            if ((vl_tit_bib.getTP_LINK()) != null && (vl_tit_bib.getTP_LINK()).equals("F")) {
                                outputV.setTipoModifica(SbnTipoModifica.VALUE_4);
                            } else {
                                outputV.setTipoModifica(SbnTipoModifica.VALUE_2);
                            }
                        } else {
                                outputV.setTipoModifica(SbnTipoModifica.VALUE_1);
                            }
                        }
                    if ((vl_tit_bib.getFL_ALLINEA()).equals("Z")
                        || (vl_tit_bib.getFL_ALLINEA_SBNMARC()).equals("Z")) {
                        if ((vl_tit_bib.getFL_CANC()).equals("S")) {
                            outputV.setTipoModifica(SbnTipoModifica.VALUE_2);
                        } else {
                            if ((vl_tit_bib.getTP_LINK()) != null && (vl_tit_bib.getTP_LINK()).equals("F")) {
                                outputV.setTipoModifica(SbnTipoModifica.VALUE_4);
                            } else {
                                outputV.setTipoModifica(SbnTipoModifica.VALUE_5);
                            }
                        }
                    }
                    if ((vl_tit_bib.getFL_ALLINEA()).equals("X")
                        || (vl_tit_bib.getFL_ALLINEA_SBNMARC()).equals("X")) {
                        outputV.setTipoModifica(SbnTipoModifica.VALUE_6);
                    }
                    if ((vl_tit_bib.getFL_ALLINEA()).equals(" ")
                        && (vl_tit_bib.getFL_ALLINEA_SBNMARC()).equals("A")) {
                        outputV.setTipoModifica(SbnTipoModifica.VALUE_0);
                        if (( ! vl_tit_bib.getFL_ALLINEA_CLA().equals(" ")
                            && parA.get("CL") != null
                            && ((String) parA.get("CL")).equals("S"))
                            || (!vl_tit_bib.getFL_ALLINEA_SOG().equals(" ")
                                && parA.get("SO") != null
                                && ((String) parA.get("SO")).equals("S"))
                            || (!vl_tit_bib.getFL_ALLINEA_REP().equals(" ")
                                && parA.get("RE") != null
                                && ((String) parA.get("RE")).equals("S"))
                            || (!vl_tit_bib.getFL_ALLINEA_LUO().equals(" ")
                                && parA.get("LU") != null
                                && ((String) parA.get("LU")).equals("S"))) {
                            AllineaInfoType output = new AllineaInfoType();
                            output.setT001(vl_tit_bib.getBID());
                            output.setOggettoVariato(outputV);
                            output = preparaOutputPerAllinea(output, tipoOutput, utente);
                            TableDao_response.add(output);
                        } else {
                        }
                    } else {
                        AllineaInfoType output = new AllineaInfoType();
                        output.setT001(vl_tit_bib.getBID());
                        output.setOggettoVariato(outputV);
                        if (SbnTipoModifica.VALUE_2.getType() == outputV.getTipoModifica().getType()) {
                            TableDao_response.add(output);
                        } else {
                            if (SbnTipoModifica.VALUE_4.getType() == outputV.getTipoModifica().getType()) {
                                output.setT001(vl_tit_bib.getBID_LINK());
                                output = preparaOutputPerAllinea(output, tipoOutput, utente);
                                output.setT001(vl_tit_bib.getBID());
                                TableDao_response.add(output);
                            } else {
                                output = preparaOutputPerAllinea(output, tipoOutput, utente);
                                TableDao_response.add(output);
                            }
                        }
                    }
                } else {
                    //  flag allineamento = 2 per data
                    AllineaInfoType output = new AllineaInfoType();
                    output.setT001(vl_tit_bib.getBID());
                    outputV.setTipoModifica(SbnTipoModifica.VALUE_5);
                    if ((vl_tit_bib.getFL_CANC()).equals("S")) {
                        outputV.setTipoModifica(SbnTipoModifica.VALUE_2);
                    }
                    if ((vl_tit_bib.getTP_LINK()) != null && (vl_tit_bib.getTP_LINK()).equals("F")) {
                        outputV.setTipoModifica(SbnTipoModifica.VALUE_4);
                    }
                    output.setOggettoVariato(outputV);
                    if (SbnTipoModifica.VALUE_2.getType() == outputV.getTipoModifica().getType()) {
                        TableDao_response.add(output);
                    } else {
                        if (SbnTipoModifica.VALUE_4.getType() == outputV.getTipoModifica().getType()) {
                            output.setT001(vl_tit_bib.getBID_LINK());
                            output = preparaOutputPerAllinea(output, tipoOutput, utente);
                            output.setT001(vl_tit_bib.getBID());
                            TableDao_response.add(output);
                        } else {
                            output = preparaOutputPerAllinea(output, tipoOutput, utente);
                            TableDao_response.add(output);
                        }
                    }

                }
            }
        } else {
            log.info("non esiste su vl_allinea_tit_bib ");
            throw new EccezioneSbnDiagnostico(3001);
        }
        return TableDao_response;
    }

    public AllineaInfoType preparaOutputPerAllinea(AllineaInfoType output, SbnTipoOutput tipo, String utente)
        throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
        SbnUserType user = new SbnUserType();
        user.setBiblioteca(utente.substring(0, 6));
        user.setUserId(utente.substring(6));
        return FormatoTitolo.formattaElementoPerAllinea(output, user, tipo, schema_version);
    }

}
