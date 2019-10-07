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
package it.finsiel.sbn.polo.factoring.transactionmaker;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_musica_bibResult;
import it.finsiel.sbn.polo.dao.entity.viste.Vl_titolo_num_stdResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneFactoring;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoTitolo;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.Formattazione;
import it.finsiel.sbn.polo.factoring.util.SimboloDewey;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.oggetti.cercatitolo.AudiovisivoAutCerca;
import it.finsiel.sbn.polo.oggetti.cercatitolo.CartografiaAutCerca;
import it.finsiel.sbn.polo.oggetti.cercatitolo.CartografiaLuoCerca;
import it.finsiel.sbn.polo.oggetti.cercatitolo.GraficaAutCerca;
import it.finsiel.sbn.polo.oggetti.cercatitolo.GraficaLuoCerca;
import it.finsiel.sbn.polo.oggetti.cercatitolo.MusicaAutCerca;
import it.finsiel.sbn.polo.oggetti.cercatitolo.MusicaComCerca;
import it.finsiel.sbn.polo.oggetti.cercatitolo.MusicaLuoCerca;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloAudiovisivo;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloAutCerca;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCartografia;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCerca;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloElettronico;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloGrafica;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloLuoCerca;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloMarCerca;
import it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloMusica;
import it.finsiel.sbn.polo.orm.viste.V_audiovisivo;
import it.finsiel.sbn.polo.orm.viste.V_audiovisivo_com;
import it.finsiel.sbn.polo.orm.viste.V_cartografia;
import it.finsiel.sbn.polo.orm.viste.V_cartografia_com;
import it.finsiel.sbn.polo.orm.viste.V_daticomuni;
import it.finsiel.sbn.polo.orm.viste.V_elettronico;
import it.finsiel.sbn.polo.orm.viste.V_grafica;
import it.finsiel.sbn.polo.orm.viste.V_grafica_com;
import it.finsiel.sbn.polo.orm.viste.V_musica;
import it.finsiel.sbn.polo.orm.viste.V_musica_com;
import it.finsiel.sbn.polo.orm.viste.Vl_cartografia_cla;
import it.finsiel.sbn.polo.orm.viste.Vl_cartografia_mar;
import it.finsiel.sbn.polo.orm.viste.Vl_cartografia_sog;
import it.finsiel.sbn.polo.orm.viste.Vl_cartografia_the;
import it.finsiel.sbn.polo.orm.viste.Vl_cartografia_tit_c;
import it.finsiel.sbn.polo.orm.viste.Vl_grafica_cla;
import it.finsiel.sbn.polo.orm.viste.Vl_grafica_mar;
import it.finsiel.sbn.polo.orm.viste.Vl_grafica_sog;
import it.finsiel.sbn.polo.orm.viste.Vl_grafica_the;
import it.finsiel.sbn.polo.orm.viste.Vl_grafica_tit_c;
import it.finsiel.sbn.polo.orm.viste.Vl_musica_bib;
import it.finsiel.sbn.polo.orm.viste.Vl_musica_cla;
import it.finsiel.sbn.polo.orm.viste.Vl_musica_imp;
import it.finsiel.sbn.polo.orm.viste.Vl_musica_mar;
import it.finsiel.sbn.polo.orm.viste.Vl_musica_tit_c;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_cla;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_imp;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_num_std;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_sog;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_the;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_c;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.C012;
import it.iccu.sbn.ejb.model.unimarcmodel.C123;
import it.iccu.sbn.ejb.model.unimarcmodel.C899;
import it.iccu.sbn.ejb.model.unimarcmodel.CdBibType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDatiTitType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocAnticoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocAudiovisivoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocCartograficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocElettronicoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocGraficaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaDocMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.CercaTitoloType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementoAutLegatoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.NumStdType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.StringaCercaTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloCercaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Classe CercaTitoloFactoring
 * <p>
 * Classe per la gestoine del servizio di ricerca dei titoli. Contiene le
 * funzionalità di ricerca per documenti e titoli. Restituisce la lista
 * sintetica o analitica. la preparazione dell'output avviene principalmente
 * sulla tabella tb_titolo L'output può essere composto da documenti, titoli di
 * accesso o da elementiAut. La distinzione avviene secondo la natura del titolo
 * trovato: A è elemento di authority, B,P,T,D è titolo di accesso, C,M,W,S,N è
 * documento.
 *
 * I tioli si specializzano in: documenti, titoli di accsso, titoli uniformi,
 * titoli uniformi musica.
 *
 * I documenti si specializzano per tipo materiale: moderno, musica, grafico,
 * cartografico.
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 16-mar-02
 */
public class CercaTitolo extends CercaFactoring {
    static Map<String, String> tpLegami = new HashMap<String, String>();
    static {
		tpLegami.put("517", "08");
		tpLegami.put("410", "01");
		tpLegami.put("430", "04");
		tpLegami.put("440", "05");
		tpLegami.put("451", "07");
		tpLegami.put("500", "09");
		tpLegami.put("510", "08");
		tpLegami.put("422", "02");
		tpLegami.put("423", "03");
		tpLegami.put("454", "06");
		tpLegami.put("461", "01");
		tpLegami.put("463", "01");
		tpLegami.put("464", "01");
		tpLegami.put("431", "43");
		tpLegami.put("434", "41");
		tpLegami.put("447", "42");
    }
    // file di log
    private static Logger log = Logger.getLogger("iccu.sbnmarcserver.CercaTitoloFactoring");

    // Inizio Dicembre 2015 almaviva2 : Ricerca collane con filtro Editore (reperito sul canpo Area di pubblicazione)
    // e opportunamente salvato sulla tb_titolo campo ky_editore
    private String[] paroleEditore = null;

    private boolean ricercaSenzaCount = false;

    private boolean musicale = false;

    private boolean cartografico = false;

    private boolean grafico = false;

    private boolean audiovisivo = false;
    private boolean elettronico = false;

    private boolean tit_uni_mus = false;

    // Attributi da XML-input
    private CercaTitoloType cercaTitolo = null;

    private CercaDatiTitType cercaDatiTit = null;

    // Dati di ricerca
    // Identificativo: T001
    private String t001 = null;

    // Impronta
    private C012 c012 = null;

    // Numero standard
    private NumStdType numStd = null;

    // titoloCerca
    private TitoloCercaType titoloCerca = null;

    // entità collegata: ArrivoLegame
    private ArrivoLegame arrivoLegame = null;

    //filtro loc bib
    private CdBibType[] locBib = null;

    public CercaTitolo(SBNMarc input_root_object) {
        super(input_root_object);
        cercaTitolo = factoring_object.getCercaTitolo();
        cercaDatiTit = cercaTitolo.getCercaDatiTit();
        arrivoLegame = cercaTitolo.getArrivoLegame();
        locBib = cercaTitolo.getLocBib();

        if (cercaDatiTit != null
                && cercaDatiTit.getCercaDatiTitTypeChoice() != null) {
            t001 = cercaDatiTit.getCercaDatiTitTypeChoice().getT001();
            numStd = cercaDatiTit.getCercaDatiTitTypeChoice().getNumSTD();
            titoloCerca = cercaDatiTit.getCercaDatiTitTypeChoice().getTitoloCerca();
         // Inizio Dicembre 2015 almaviva2 : Ricerca collane con filtro Editore (reperito sul canpo Area di pubblicazione)
         // e opportunamente salavto sulla tb_titolo campo ky_editore
            paroleEditore = cercaDatiTit.getParoleEditore();
        }
        // tipoSchedaDatiAut = Validator.get...
    }

    /**
     * Esamina i parametri di ricerca ricevuti via xml e attiva il metodo di
     * ricerca opportuno. gestisce il tipo di risposta richiesto (lista o esame)
     * e produce il file xml di output richiesto
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void executeCerca() throws IllegalArgumentException, InvocationTargetException, Exception {

        boolean ricercaPerLastra = false;
        boolean ricercaPerEditor = false;
        boolean localizzazione = false;
        boolean composizione = false;

        /**
         * almaviva2 - Settembre 2014 - Evolutiva per la gestione delle date del titolo per gestire il carattere punto -
         * Controlla che le date siano formalmente corrette in termini dei primi due caratteri numerici e dei successivi due numerici
         * o uguale al carattere punto '.';
         * Inserito anche controllo sul null della data da1 (cercaDatiTit.getT100_Da().getA_100_9() != null) perchè se si valorizza
         * solo la data2 andava in errore
         * @return string che conterrà eventuale diagnostico da inviare al bibliotecario
         */
        if (cercaDatiTit != null) {
            if (cercaDatiTit.getT100_Da() != null && cercaDatiTit.getT100_A() != null) {
            	if (cercaDatiTit.getT100_Da().getA_100_9() != null) {
            		if (cercaDatiTit.getT100_Da().getA_100_9().endsWith(".") || cercaDatiTit.getT100_A().getA_100_9().endsWith(".")) {
                		if (cercaDatiTit.getT100_Da().getA_100_9().equals(cercaDatiTit.getT100_A().getA_100_9())) {
                			cercaDatiTit.getT100_Da().setA_100_9(cercaDatiTit.getT100_Da().getA_100_9().replace('.', '0'));
                			cercaDatiTit.getT100_A().setA_100_9(cercaDatiTit.getT100_A().getA_100_9().replace('.', '9'));
                		} else {
                			throw new EccezioneSbnDiagnostico(412, "Data di ricerca con carattere jolly invalida");
                		}
                	}
            	}
            }
        }
        // End almaviva7 05/05/2014


        C123 c123 = null;
        int counter = 0;
        if (tipoOrd != null && tipoOrd.equals("3"))
            throw new EccezioneSbnDiagnostico(3242, "Ordinamento errato");
        if (t001 != null)
            counter++;
        if (numStd != null)
            counter++;
        if (titoloCerca != null) {
            StringaCercaType stringaCerca = titoloCerca.getStringaCerca();
            if (stringaCerca != null) {
                if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null) {
//
                    // Inzio MANTIS 2256 -  Modifica copiata da Indice per corretta gestione ricerca per stringa esatta
                	//   	StringaCercaTypeChoice cercaTypeChoice = new StringaCercaTypeChoice();
                    // cercaTypeChoice.setStringaEsatta(dopoAsterisco(stringaCerca.getStringaCercaTypeChoice().getStringaEsatta()));
                    String stringaEsatta = dopoAsterisco(stringaCerca.getStringaCercaTypeChoice().getStringaEsatta());
                    String nome = Formattazione.formattaPerAreaTitolo(stringaEsatta);

                    // Inizio modifica almaviva2 2010.11.04 BUG MANTIS 3970 ricerca di un titolo di soli due caratteri
                    // con ricerca puntuale non deve einviare diagnostico ma concludersi correttamente (vedi stesso oggetto di Indice)
//                    if (nome.length() < 3 && !esporta) {
//                    	throw new EccezioneSbnDiagnostico(3139, "Comunicare almeno 3 caratteri");
//                    }
                    // 	Fine modifica almaviva2 2010.11.04 BUG MANTIS 3970

                    if(nome.length() > 50) {
                    	nome = nome.substring(0, 50);
                    }
                    stringaCerca.getStringaCercaTypeChoice().setStringaEsatta(nome);
//                    stringaCerca.setStringaCercaTypeChoice(cercaTypeChoice);
                    // Fine Modifica copiata da Indice per corretta gestione ricerca per stringa esatta

                    //stringaCerca.setStringaEsatta(dopoAsterisco(stringaCerca.getStringaCercaTypeChoice().getStringaEsatta()));
                } else if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null) {
                    String stringaLike = dopoAsterisco(stringaCerca.getStringaCercaTypeChoice().getStringaLike());

                    // Inizio Intervento interno 23.01.2012 Inizio modifica riportata da Indice:
                 	// MANTIS 2256 - il metodo responsabile della formattazione
                 	// deve togliere dalla stringa di ricerca, sulla quale vengono calcolate
                 	// le chiavi, tutti quelli che sono separatori catalografici (" : ", " / ", " ; ")
                 	// e tutto ciò che segue; il resto della formattazione rimane lo stesso
	                // String nome = Formattazione.formatta(stringaLike);
					String nome = Formattazione.formattaPerAreaTitolo(stringaLike);
                    // Fine Intervento interno 23.01.2012 Inizio modifica riportata da Indice:


                    if (nome.length() < 3 && !esporta)
                        throw new EccezioneSbnDiagnostico(3139,"Comunicare almeno 3 caratteri");
//                    if (nome.equalsIgnoreCase("EXPORT OPAC"))
//                        stringaLike = "";

                    // Inizio modifica almaviva2 05.10.2009 per ricerca titolo con più di 50 caratteri
                    // Seconda richiesta per bug 3144 Mantis
                    if(nome.length() > 50) {
                    	nome = nome.substring(0, 50);
                    }
                    StringaCercaTypeChoice cercaTypeChoice = new StringaCercaTypeChoice();
//                    cercaTypeChoice.setStringaLike(stringaLike);
                    cercaTypeChoice.setStringaLike(nome);
                    stringaCerca.setStringaCercaTypeChoice(cercaTypeChoice);
                    //stringaCerca.setStringaLike(stringaLike);

                    // Fine modifica almaviva2 05.10.2009 per ricerca titolo con più di 50 caratteri
                    // Seconda richiesta per bug 3144 Mantis
                } else
                    throw new EccezioneSbnDiagnostico(3040);
            }
            if (arrivoLegame == null)
                counter++;
        }
        if (arrivoLegame != null) {
            counter++;
        }
        if (cercaDatiTit != null) {
            if (cercaDatiTit instanceof CercaDocAnticoType) {
                c012 = ((CercaDocAnticoType) cercaDatiTit).getT012();
                setTp_materiale("E");
            } else if (cercaDatiTit instanceof CercaDocMusicaType) {
                setTp_materiale("U");
                CercaDocMusicaType musica = (CercaDocMusicaType) cercaDatiTit;
                musicale = true;
                c012 = musica.getT012();
                if (musica.hasNumLastra_A() || musica.hasNumLastra_Da()) {
                    ricercaPerLastra = true;
                    counter++;
                }
                if (musica.hasNumEditor_A() || musica.hasNumEditor_Da()) {
                    ricercaPerEditor = true;
                    counter++;
                }
                // GESTIONE DATE COMPOSIZIONE
                if (musica.getDataFine_A() != null
						|| musica.getDataFine_Da() != null
						|| musica.getDataInizio_A() != null
						|| musica.getDataInizio_Da() != null) {
					composizione = true;
				}
                if (musica.getT928() != null)
                    composizione = true;
                if (musica.getT929() != null)
                    composizione = true;
                C899 c899 = musica.getT899();
                if (c899 != null) {
                    counter++;
                    localizzazione = true;
                    if (c899.getC2_899() == null)
                        throw new EccezioneSbnDiagnostico(3067,
                                "Manca la localizzazione");
                    if (c899.getB_899() == null && c899.getG_899() == null)
                        throw new EccezioneSbnDiagnostico(3068,
                                "Valorizzare il fondo o la segnatura");
                }
            } else if (cercaDatiTit instanceof CercaDocGraficaType) {
                setTp_materiale("G");
                grafico = true;

            // almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
            } else if (cercaDatiTit instanceof CercaDocAudiovisivoType) {
                setTp_materiale("H");
                audiovisivo = true;
            } else if (cercaDatiTit instanceof CercaDocElettronicoType) {
                setTp_materiale("L");
                elettronico = true;
            // Fine almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
            } else if (cercaDatiTit instanceof CercaDocCartograficoType) {
                setTp_materiale("C");
                cartografico = true;
                c123 = ((CercaDocCartograficoType) cercaDatiTit).getT123();
                if (c123 != null ) {
                  if ((c123.getD_123() != null && !c123.getD_123().equals("")) ||
                      (c123.getE_123() != null && !c123.getE_123().equals("")) ||
                      (c123.getF_123() != null && !c123.getF_123().equals("")) ||
                      (c123.getG_123() != null && !c123.getG_123().equals(""))) {
                    counter++;
                  }
               }
            } else if (cercaDatiTit.getTipoMaterialeCount() == 1) {
                setTp_materiale(cercaDatiTit.getTipoMateriale(0).toString());
            }
        }
        if (c012 != null)
            counter++;

        if (counter != 1) {
            // segnala errore
            throw new EccezioneSbnDiagnostico(3039);
        }
        if ((musicale || grafico || cartografico)
                && cercaDatiTit.getSottoTipoLegame() != null)
            throw new EccezioneSbnDiagnostico(3074,
                    "Specificare un minor numero di canali di ricerca");
        if (t001 != null)
            cercaPerId();
        else if (numStd != null)
            cercaPerNumStd();
        else if (ricercaPerLastra)
            cercaTitoloPerLastra("L");
        else if (ricercaPerEditor)
            cercaTitoloPerLastra("E");
        else if (localizzazione)
            cercaTitoloPerLocalizzazione();
        else if (composizione)
            cercaConFiltriComposizione();
        else if (c012 != null)
            cercaPerImpronta();
        else if (c123 != null)
            cercaPerCoordinate(c123);
        else if (cercaDatiTit != null
                && cercaDatiTit.getElementoAutLegato() != null) {
            // Ricerche per legame
            ElementoAutLegatoType legameElemento = cercaDatiTit
                    .getElementoAutLegato();
            if (legameElemento.getTipoAuthority().getType() ==
                    SbnAuthority.valueOf("AU").getType()) {
                cercaConFiltriAutore();
            } else if (legameElemento.getTipoAuthority().getType() ==
                    SbnAuthority.valueOf("LU").getType()) {
                cercaConFiltriLuogo();
            } else if (legameElemento.getTipoAuthority().getType() ==
                    SbnAuthority.valueOf("SO").getType()) {
                // cercaConFiltriSoggetto();
            } else if (legameElemento.getTipoAuthority().getType() ==
                    SbnAuthority.valueOf("MA").getType()) {
                cercaConFiltriMarca();
            } else if (legameElemento.getTipoAuthority().getType() ==
                    SbnAuthority.valueOf("CL").getType()) {
                // cercaCrClasse();
            } else if (legameElemento.getTipoAuthority().getType() ==
                    SbnAuthority.valueOf("TU").getType()) {
                // cercaPerLegameTitolo(legameElemento.getIdArrivo());
            }
        } else if (arrivoLegame != null) {
            if (arrivoLegame.getLegameDoc() != null)
                cercaPerLegameDoc(arrivoLegame.getLegameDoc());
            else if (arrivoLegame.getLegameTitAccesso() != null) {
                String tpLegame = arrivoLegame.getLegameTitAccesso()
                        .getTipoLegame() == null ? null : arrivoLegame
                        .getLegameTitAccesso().getTipoLegame().toString();
                cercaPerLegameTitolo(arrivoLegame.getLegameTitAccesso()
                        .getIdArrivo(), tpLegame);
            } else if (arrivoLegame.getLegameElementoAut() != null) {
                cercaPerLegameAuthority();
            }
        } else if (titoloCerca != null) {
        	// Inizio Modifica febbraio 2015 - Vedi Doc test SbnWEB audiovisivi3 di Carla Scognamiglio
        	// Testo letterario per nuovo protocollo SCHEMA 2.0 inserito nuovo metodo cercaPerStringaTitoloCom
            // cercaPerStringaTitolo();
        	if (cercaDatiTit.getFiltriDatiComuniCerca() != null)
        		cercaPerStringaTitoloCom();
        	else
        		cercaPerStringaTitolo();
        }
    }

    /** Anche le ricerche su musica, grafica e cartografia
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaPerLegameAuthority() throws IllegalArgumentException, InvocationTargetException, Exception {
        SbnAuthority authority = arrivoLegame.getLegameElementoAut()
                .getTipoAuthority();
        String idArrivo = arrivoLegame.getLegameElementoAut().getIdArrivo();
        SbnLegameAut tpLegame = arrivoLegame.getLegameElementoAut()
                .getTipoLegame();
        String leg = null;
        if (tpLegame != null) {
            leg = tpLegame.toString();
        }
        if (authority.getType() == SbnAuthority.CL_TYPE) {
            cercaConClasse(idArrivo);
        } else if (authority.getType() == SbnAuthority.SO_TYPE) {
            cercaConSoggetto(idArrivo);
        } else if (authority.getType() == SbnAuthority.TH_TYPE) {
            cercaConThesauro(idArrivo);
        } else if (authority.getType() == SbnAuthority.AU_TYPE) {
            cercaConFiltriAutore();
        } else if (authority.getType() == SbnAuthority.LU_TYPE) {
            cercaConFiltriLuogo();
        } else if (authority.getType() == SbnAuthority.MA_TYPE) {
            cercaConMarca(idArrivo);
        } else if (authority.getType() == SbnAuthority.TU_TYPE) {
            cercaPerLegameTitolo(idArrivo, leg);
        } else if (authority.getType() == SbnAuthority.UM_TYPE) {
            cercaPerLegameTitolo(idArrivo, leg);
        }
    }

    /** Cerca per legame documento
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaPerLegameDoc(LegameDocType legame)
            throws IllegalArgumentException, InvocationTargetException, Exception {
        String idArrivo = legame.getIdArrivo();
        TitoloCerca titolo = new TitoloCerca();
        Vl_titolo_tit_c tit = new Vl_titolo_tit_c();
        //almaviva5_20091012
        tit.setFILTRO_LOC_BIB(locBib);
        titolo.valorizzaFiltri(tit, cercaDatiTit);
        String tpLeg = legame.getTipoLegame() == null ? null : legame
                .getTipoLegame().toString();
        if (tpLeg != null) {
            tit.setTP_LEGAME(tpLegami.get(tpLeg));
        }
        controllaNumeroRecord(titolo.contaTitoloPerDocumento(idArrivo, legame
                .getSici(), tit));
        tavola_response = titolo.cercaTitoloPerDocumento(idArrivo, legame
                .getSici(), tit, tipoOrd);
    }

    /**
     * bisogna modificare la ricerca per titolo di accesso o uniforme o quello
     * che è. Cerca per legame titolo uniforme o accesso
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void cercaPerLegameTitolo(String idArrivo, String tpLegame)
            throws IllegalArgumentException, InvocationTargetException, Exception {
        if (cercaDatiTit != null && cercaDatiTit.getSottoTipoLegame() != null) {
            Vl_titolo_tit_b tit_doc = new Vl_titolo_tit_b();
            tit_doc.setTP_LEGAME_MUSICA(cercaDatiTit.getSottoTipoLegame().toString());
            //almaviva5_20091012
            tit_doc.setFILTRO_LOC_BIB(locBib);
            TitoloCerca titolo = new TitoloCerca();
            titolo.valorizzaFiltri(tit_doc, cercaDatiTit);
            tit_doc.setBID(idArrivo);
            if (tpLegame != null) {
                tit_doc.setTP_LEGAME(tpLegami.get(tpLegame));
            }
            controllaNumeroRecord(titolo
                    .contaTitoloPerNomeEsattoPerTpLegame(tit_doc));
            tavola_response = titolo.cercaTitoloPerNomeEsattoPerTpLegame(
                    tit_doc, tipoOrd);
        } else if (grafico) {
            TitoloGrafica titolo = new TitoloGrafica();
            Vl_grafica_tit_c tit = new Vl_grafica_tit_c();
            tit.setBID_COLL(idArrivo);
            //almaviva5_20091012
            tit.setFILTRO_LOC_BIB(locBib);
            if (tpLegame != null) {
                tit.setTP_LEGAME(tpLegami.get(tpLegame));
            }
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerTit(tit));
            tavola_response = titolo.cercaTitoloPerTit(tit, tipoOrd);
        } else if (cartografico) {
            TitoloCartografia titolo = new TitoloCartografia();
            Vl_cartografia_tit_c tit = new Vl_cartografia_tit_c();
            tit.setBID_COLL(idArrivo);
            //almaviva5_20091012
            tit.setFILTRO_LOC_BIB(locBib);
            if (tpLegame != null) {
                tit.setTP_LEGAME(tpLegami.get(tpLegame));
            }
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerTit(tit));
            tavola_response = titolo.cercaTitoloPerTit(tit, tipoOrd);
//       } else if (audiovisivo) {
         // Evolutiva febbraio 2015: ricerche sui nuovi tipi materiale audiovisivo/elettronico
//          TitoloAudiovisivo titolo = new TitoloAudiovisivo(db_conn);
//          Vl_audiovisivo_tit_c tit = new Vl_audiovisivo_tit_c();
//          tit.setBid_coll(idArrivo);
//          if (tpLegame != null) {
//              tit.setTp_legame((String)tpLegami.get(tpLegame));
//          }
//          titolo.valorizzaFiltri(tit, cercaDatiTit);
//          controllaNumeroRecord(titolo.contaTitoloPerTit(tit));
//          tavola_response = titolo.cercaTitoloPerTit(tit, tipoOrd);
        } else if (musicale) {
            TitoloMusica titolo = new TitoloMusica();
            Vl_musica_tit_c tit = new Vl_musica_tit_c();
            tit.setBID_COLL(idArrivo);
            //almaviva5_20091012
            tit.setFILTRO_LOC_BIB(locBib);
            if (tpLegame != null) {
                tit.setTP_LEGAME(tpLegami.get(tpLegame));
            }
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerTit(tit));
            tavola_response = titolo.cercaTitoloPerTit(tit, tipoOrd);
        } else {
            TitoloCerca titolo = new TitoloCerca();
            Vl_titolo_tit_c tit = new Vl_titolo_tit_c();
            //almaviva5_20091012
            tit.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            if (tpLegame != null) {
                tit.setTP_LEGAME(tpLegami.get(tpLegame));
            }
            controllaNumeroRecord(titolo.contaTitoloPerDocumento(idArrivo,
                    null, tit));
            tavola_response = titolo.cercaTitoloPerDocumento(idArrivo, null,
                    tit, tipoOrd);
        }
    }

    /** Ricerca per chiave identificatrice
     * @throws InfrastructureException */
    private void cercaPerId() throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        TitoloCerca titolo = new TitoloCerca();
        String id = Formattazione.formatta(t001);
        ricercaSenzaCount = true;
        //almaviva5_20091012
        titolo.setFILTRO_LOC_BIB(locBib);
        tavola_response = titolo.cercaTitoloPerID(id);
    }

    /** Ricerca per chiave identificatrice
     * @throws InfrastructureException */
    public TableDao cercaPerIdLista(String bid) throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        TitoloCerca titolo = new TitoloCerca();
        String id = Formattazione.formatta(bid);
        ricercaSenzaCount = true;
        return tavola_response = titolo.cercaTitoloPerID(id);

    }
    public TableDao cercaPerIdLista2(String[] bid) throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
        TitoloCerca titolo = new TitoloCerca();
        for (int s = 0; s < bid.length; s++) {
	        String id = Formattazione.formatta(bid[s]);
	        ricercaSenzaCount = true;
	        tavola_response = titolo.cercaTitoloPerID(id);
	        try {
				SBNMarc response2_TableDao = preparaOutput();
				String pp = null;
				pp = marshal(response2_TableDao);
				log.debug(pp);
			} catch (EccezioneDB e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EccezioneFactoring e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EccezioneSbnDiagnostico e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
        return tavola_response;

    }
    public void setTableDao(TableDao tavola) throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
    	this.tavola_response = tavola;

    }

    /** Ricerca in base ad un numero Standard.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaPerNumStd() throws IllegalArgumentException, InvocationTargetException, Exception {
        String tipoStd = numStd.getTipoSTD().toString();
        String num = numStd.getNumeroSTD();
        String paeseStd = Formattazione.formatta(numStd.getPaeseSTD());
        String nota = numStd.getNotaSTD();

        TitoloCerca titolo = new TitoloCerca();
        // titolo.valorizzaFiltri(cercaDatiTit); Non li valorizzo: non c'entrano
        
        // Inizio manutenzione correttiva 27.09.2019 Bug MANTIS 7124 almaviva2
        // Vedi anche Protocollo Indice:MANTIS 2108 poich� non fa la count non imposta "numRecord"
        // Il campo ricercaSenzaCount non deve essere impostato altrimenti vengono portati in sintetica solo le prime 10 (maxrighe)
        // richieste perdendo la paginazione sulle successive occorrenze.
     		//ricercaSenzaCount = true;
        controllaNumeroRecord(titolo.contaTitoloPerNumStd(num, tipoStd, paeseStd, nota));
     // Fine manutenzione correttiva 27.09.2019 Bug MANTIS 7124 
        
        //almaviva5_20091012
        titolo.setFILTRO_LOC_BIB(locBib);
        tavola_response = titolo.cercaTitoloPerNumStd(num, tipoStd, paeseStd, nota);
    }

    /**
     * Esegue la conta dei record che vengono trovati: ritorna una tavola
     * contenente un elenco di oggetti vista vl_titolo_num_std
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected void cercaTitoloPerLastra(String tp_std) throws IllegalArgumentException, InvocationTargetException, Exception {
        if (cercaDatiTit.getElementoAutLegato() != null) {
            TitoloAutCerca titAut = new TitoloAutCerca(this,cercaTitolo, tipoOrd);
            //almaviva5_20091012
            titAut.setFILTRO_LOC_BIB(locBib);
            tavola_response = titAut.cercaTitoloPerNumStd((CercaDocMusicaType) cercaDatiTit);

        } else {
            Vl_titolo_num_std tit = new Vl_titolo_num_std();
            //almaviva5_20091012
            tit.setFILTRO_LOC_BIB(locBib);
            Vl_titolo_num_stdResult tavola = new Vl_titolo_num_stdResult(tit);
            Titolo titolo = new Titolo();

            CercaDocMusicaType musica = (CercaDocMusicaType) cercaDatiTit;
            if (tp_std.equals("L")) {
                if (musica.hasNumLastra_Da())
                    tit.settaParametro(TableDao.XXXnumeroLastraDa, new Integer(musica.getNumLastra_Da()));
                if (musica.hasNumLastra_A())
                    tit.settaParametro(TableDao.XXXnumeroLastraA, new Integer(musica.getNumLastra_A()));
            } else {
                if (musica.hasNumEditor_Da())
                    tit.settaParametro(TableDao.XXXnumeroLastraDa, new Integer(musica.getNumEditor_Da()));
                if (musica.hasNumEditor_A())
                    tit.settaParametro(TableDao.XXXnumeroLastraA, new Integer(musica.getNumEditor_A()));
            }
            tit.setTP_NUMERO_STD(Decodificatore.getCd_tabella("Tb_numero_std",
                    "tp_numero_std", tp_std));
            controllaNumeroRecord(titolo.conta(tavola, "countPerLastra"));
            tavola.executeCustom("selectPerLastra", tipoOrd);
            tavola_response = tavola;
        }
    }

    protected void cercaTitoloPerLocalizzazione() throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_musica_bib tit = new Vl_musica_bib();
        //almaviva5_20091012
        tit.setFILTRO_LOC_BIB(locBib);
        Vl_musica_bibResult tavola = new Vl_musica_bibResult(tit);
        TitoloMusica titolo = new TitoloMusica();

        CercaDocMusicaType musica = (CercaDocMusicaType) cercaDatiTit;
        tit.setCD_POLO(musica.getT899().getC2_899().substring(0, 3));
        tit.setCD_BIBLIOTECA(musica.getT899().getC2_899().substring(3));
        tit.setDS_FONDO(musica.getT899().getB_899());
        String ds_segn = "";
        // se finisce con * è una ricerca like
        if (musica.getT899().getG_899() != null) {
            if (musica.getT899().getG_899().indexOf("*")>=0)
            	ds_segn = musica.getT899().getG_899().trim().replace('*', '%');
            else
            	ds_segn = musica.getT899().getG_899().trim();
            tit.setDS_SEGN(ds_segn);
        }

        //tit.setDs_segn(musica.getT899().getG_899());
        titolo.valorizzaFiltri(tit, cercaDatiTit);
        controllaNumeroRecord(titolo.conta(tavola, "countLocalizzazione"));
        tavola.executeCustom("selectLocalizzazione", tipoOrd);
        tavola_response = tavola;
    }

    /** Esegue una ricerca utilizzando il campo titoloCerca;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaPerStringaTitolo() throws IllegalArgumentException, InvocationTargetException, Exception {
        StringaCercaType stringaCerca = titoloCerca.getStringaCerca();
        if (stringaCerca != null) {
            if (stringaCerca.getStringaCercaTypeChoice().getStringaEsatta() != null) {
                cercaPerStringaEsatta();
            } else if (stringaCerca.getStringaCercaTypeChoice().getStringaLike() != null) {
                cercaPerStringaLike();
            } else
                throw new EccezioneSbnDiagnostico(3040);
        } else if (titoloCerca.getTitoloCLET() != null) {
            // Se arrivo qui è da solo, altrimenti è da utilizzarsi in
            // combinazione
            // con altri elementi
            cercaPerClet();
        }

    }

    // Inizio Modifica febbraio 2015 - Vedi Doc test SbnWEB audiovisivi3 di Carla Scognamiglio
	// Testo letterario per nuovo protocollo SCHEMA 2.0
    private void cercaPerStringaTitoloCom() throws IllegalArgumentException, InvocationTargetException, Exception {
    		StringaCercaType stringaCerca = titoloCerca.getStringaCerca();

    	if (stringaCerca != null) {
    		StringaCercaTypeChoice choice = stringaCerca.getStringaCercaTypeChoice();
    		if (choice.getStringaEsatta() != null) {
    			cercaPerStringaEsattaCom();
    		} else if (choice.getStringaLike() != null) {
    			cercaPerStringaLikeCom();
    		} else
    			throw new EccezioneSbnDiagnostico(3040);
    	} else if (titoloCerca.getTitoloCLET() != null) {
		    // Se arrivo qui è da solo, altrimenti è da utilizzarsi in
		    // combinazione
		    // con altri elementi
		    cercaPerCletCom();
    	}
    }



    /**
     * Esegue una ricerca utilizzando il campo stringa esatta, solo senza filtri
     * sui legami;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    private void cercaPerStringaEsatta() throws IllegalArgumentException, InvocationTargetException, Exception {

        if (cercaDatiTit.getSottoTipoLegame() != null) {
            Vl_titolo_tit_b tit_doc = new Vl_titolo_tit_b();
            tit_doc.setTP_LEGAME_MUSICA(cercaDatiTit.getSottoTipoLegame()
                    .toString());
            TitoloCerca titolo = new TitoloCerca();
            //almaviva5_20091012
            tit_doc.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(tit_doc, cercaDatiTit);
            controllaNumeroRecord(titolo
                    .contaTitoloPerNomeEsattoPerTpLegame(tit_doc));
            tavola_response = titolo.cercaTitoloPerNomeEsattoPerTpLegame(
                    tit_doc, tipoOrd);
        } else if (musicale) {
            TitoloMusica titolo = new TitoloMusica();
            V_musica mus = new V_musica();
            //almaviva5_20091012
            mus.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(mus, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerNomeEsatto(mus));
            tavola_response = titolo.cercaTitoloPerNomeEsatto(mus, tipoOrd);
        } else if (grafico) {
            TitoloGrafica titolo = new TitoloGrafica();
            V_grafica gra = new V_grafica();
            //almaviva5_20091012
            gra.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(gra, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerNomeEsatto(gra));
            tavola_response = titolo.cercaTitoloPerNomeEsatto(gra, tipoOrd);
        } else if (cartografico) {
            TitoloCartografia titolo = new TitoloCartografia();
            V_cartografia car = new V_cartografia();
            //almaviva5_20091012
            car.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(car, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerNomeEsatto(car));
            tavola_response = titolo.cercaTitoloPerNomeEsatto(car, tipoOrd);
        } else if (audiovisivo) {
        	// Evolutiva febbraio 2015: ricerche sui nuovi tipi materiale audiovisivo/elettronico
            TitoloAudiovisivo titolo = new TitoloAudiovisivo();
            V_audiovisivo car = new V_audiovisivo();
            titolo.valorizzaFiltri(car, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerNomeEsatto(car));
            tavola_response = titolo.cercaTitoloPerNomeEsatto(car, tipoOrd);
        } else if (elettronico) {
            TitoloElettronico titolo = new TitoloElettronico();
            V_elettronico car = new V_elettronico();
            titolo.valorizzaFiltri(car, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerNomeEsatto(car));
            tavola_response = titolo.cercaTitoloPerNomeEsatto(car, tipoOrd);
        } else {
            TitoloCerca titolo = new TitoloCerca();
            //almaviva5_20091012
            titolo.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerNomeEsatto());
            tavola_response = titolo.cercaTitoloPerNomeEsatto(tipoOrd);
        }
    }


    // Inizio Modifica febbraio 2015 - Vedi Doc test SbnWEB audiovisivi3 di Carla Scognamiglio
	// Testo letterario per nuovo protocollo SCHEMA 2.0
    private void cercaPerStringaEsattaCom() throws IllegalArgumentException, InvocationTargetException, Exception {

		if (cercaDatiTit.getSottoTipoLegame() != null) {
		    Vl_titolo_tit_b tit_doc = new Vl_titolo_tit_b();
		    tit_doc.setTP_LEGAME_MUSICA(cercaDatiTit.getSottoTipoLegame()
		            .toString());
		    TitoloCerca titolo = new TitoloCerca();
		    titolo.valorizzaFiltri(tit_doc, cercaDatiTit);
		    controllaNumeroRecord(titolo
		            .contaTitoloPerNomeEsattoPerTpLegame(tit_doc));
		    tavola_response = titolo.cercaTitoloPerNomeEsattoPerTpLegame(
		            tit_doc, tipoOrd);
		} else if (musicale) {
		    TitoloMusica titolo = new TitoloMusica();
		    V_musica_com com = new V_musica_com();
		    titolo.valorizzaFiltri(com, cercaDatiTit);
		    controllaNumeroRecord(titolo.contaTitoloPerNomeEsattoCom(com));
		    tavola_response = titolo.cercaTitoloPerNomeEsattoCom(com, tipoOrd);
		} else if (grafico) {
		    TitoloGrafica titolo = new TitoloGrafica();
		    V_grafica_com gra = new V_grafica_com();
		    titolo.valorizzaFiltri(gra, cercaDatiTit);
		    controllaNumeroRecord(titolo.contaTitoloPerNomeEsattoCom(gra));
		    tavola_response = titolo.cercaTitoloPerNomeEsattoCom(gra, tipoOrd);
		} else if (cartografico) {
		    TitoloCartografia titolo = new TitoloCartografia();
		    V_cartografia_com car = new V_cartografia_com();
		    titolo.valorizzaFiltri(car, cercaDatiTit);
		    controllaNumeroRecord(titolo.contaTitoloPerNomeEsattoCom(car));
		    tavola_response = titolo.cercaTitoloPerNomeEsattoCom(car, tipoOrd);
		} else if (audiovisivo) {
		    TitoloAudiovisivo titolo = new TitoloAudiovisivo();
		    V_audiovisivo_com com = new V_audiovisivo_com();
		    titolo.valorizzaFiltri(com, cercaDatiTit);
		    controllaNumeroRecord(titolo.contaTitoloPerNomeEsattoCom(com));
		    tavola_response = titolo.cercaTitoloPerNomeEsattoCom(com, tipoOrd);
	//	} else if (elettronico) { //vista da fare
	//	    TitoloElettronico titolo = new TitoloElettronico(db_conn);
	//	    V_elettronico car = new V_elettronico();
	//	    titolo.valorizzaFiltri(car, cercaDatiTit);
	//	    controllaNumeroRecord(titolo.contaTitoloPerNomeEsatto(car));
	//	    tavola_response = titolo.cercaTitoloPerNomeEsatto(car, tipoOrd);
		} else {
		    TitoloCerca titolo = new TitoloCerca();
		    V_daticomuni com = new V_daticomuni();
		    titolo.valorizzaFiltriCom(com, cercaDatiTit);
		    controllaNumeroRecord(titolo.contaTitoloPerNomeEsattoCom(com));
		    tavola_response = titolo.cercaTitoloPerNomeEsattoCom(com, tipoOrd);
		}
	}



    /** Esegue una ricerca utilizzando il campo stringalike;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaPerStringaLike() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (cercaDatiTit.getSottoTipoLegame() != null) {
            Vl_titolo_tit_b tit_doc = new Vl_titolo_tit_b();
            tit_doc.setTP_LEGAME_MUSICA(cercaDatiTit.getSottoTipoLegame()
                    .toString());
            //almaviva5_20091012
            tit_doc.setFILTRO_LOC_BIB(locBib);
            TitoloCerca titolo = new TitoloCerca();
            titolo.valorizzaFiltri(tit_doc, cercaDatiTit);
            controllaNumeroRecord(titolo
                    .contaTitoloPerNomeLikePerTpLegame(tit_doc));
            tavola_response = titolo.cercaTitoloPerNomeLikePerTpLegame(tit_doc,
                    tipoOrd);
        } else if (musicale) {
            TitoloMusica titolo = new TitoloMusica();
            V_musica mus = new V_musica();
            //almaviva5_20091012
            mus.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(mus, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerNomeLike(mus));
            tavola_response = titolo.cercaTitoloPerNomeLike(mus, tipoOrd);
        } else if (audiovisivo) {
        	// Evolutiva febbraio 2015: ricerche sui nuovi tipi materiale audiovisivo/elettronico
            TitoloAudiovisivo titolo = new TitoloAudiovisivo();
            V_audiovisivo aud = new V_audiovisivo();
            titolo.valorizzaFiltri(aud, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerNomeLike(aud));
            tavola_response = titolo.cercaTitoloPerNomeLike(aud, tipoOrd);
        } else if (elettronico) { // almaviva7 18/06/2014
            TitoloElettronico titolo = new TitoloElettronico();
            V_elettronico ele = new V_elettronico();
            titolo.valorizzaFiltri(ele, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerNomeLike(ele));
            tavola_response = titolo.cercaTitoloPerNomeLike(ele, tipoOrd);
        } else if (grafico) {
            TitoloGrafica titolo = new TitoloGrafica();
            V_grafica gra = new V_grafica();
            //almaviva5_20091012
            gra.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(gra, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerNomeLike(gra));
            tavola_response = titolo.cercaTitoloPerNomeLike(gra, tipoOrd);
        } else if (cartografico) {
            TitoloCartografia titolo = new TitoloCartografia();
            V_cartografia car = new V_cartografia();
            //almaviva5_20091012
            car.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(car, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerNomeLike(car));
            tavola_response = titolo.cercaTitoloPerNomeLike(car, tipoOrd);
        } else {
            TitoloCerca titolo = new TitoloCerca();
            // La ricerca per esporta per ts_var utilizza solo questo caso.
            titolo.setEsporta(esporta);
            //almaviva5_20091012
            titolo.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerNomeLike());
            tavola_response = titolo.cercaTitoloPerNomeLike(tipoOrd);
        }
    }


    private void cercaPerStringaLikeCom() throws IllegalArgumentException, InvocationTargetException, Exception {

		if (cercaDatiTit.getSottoTipoLegame() != null) {
		    Vl_titolo_tit_b tit_doc = new Vl_titolo_tit_b();
		    tit_doc.setTP_LEGAME_MUSICA(cercaDatiTit.getSottoTipoLegame().toString());
		    TitoloCerca titolo = new TitoloCerca();
		    titolo.valorizzaFiltri(tit_doc, cercaDatiTit);
		    controllaNumeroRecord(titolo.contaTitoloPerNomeLikePerTpLegame(tit_doc));
		    tavola_response = titolo.cercaTitoloPerNomeLikePerTpLegame(tit_doc, tipoOrd);
		} else if (musicale) {
		    TitoloMusica titolo = new TitoloMusica();
		    V_musica_com com = new V_musica_com();
		    titolo.valorizzaFiltri(com, cercaDatiTit);
		    controllaNumeroRecord(titolo.contaTitoloPerNomeLikeCom(com));
		    tavola_response = titolo.cercaTitoloPerNomeLikeCom(com, tipoOrd);
		} else if (audiovisivo) {
		    TitoloAudiovisivo titolo = new TitoloAudiovisivo();
		    V_audiovisivo_com aud = new V_audiovisivo_com();
		    titolo.valorizzaFiltri(aud, cercaDatiTit);
		    controllaNumeroRecord(titolo.contaTitoloPerNomeLikeCom(aud));
		    tavola_response = titolo.cercaTitoloPerNomeLikeCom(aud, tipoOrd);
	//	} else if (elettronico) { //vista da fare
	//	    TitoloElettronico titolo = new TitoloElettronico(db_conn);
	//	    V_elettronico ele = new V_elettronico();
	//	    titolo.valorizzaFiltri(ele, cercaDatiTit);
	//	    controllaNumeroRecord(titolo.contaTitoloPerNomeLike(ele));
	//	    tavola_response = titolo.cercaTitoloPerNomeLike(ele, tipoOrd);
		} else if (grafico) {
		    TitoloGrafica titolo = new TitoloGrafica();
		    V_grafica_com gra = new V_grafica_com();
		    titolo.valorizzaFiltri(gra, cercaDatiTit);
		    controllaNumeroRecord(titolo.contaTitoloPerNomeLikeCom(gra));
		    tavola_response = titolo.cercaTitoloPerNomeLikeCom(gra, tipoOrd);
		} else if (cartografico) {
		    TitoloCartografia titolo = new TitoloCartografia();
		    V_cartografia_com car = new V_cartografia_com();
		    titolo.valorizzaFiltri(car, cercaDatiTit);
		    controllaNumeroRecord(titolo.contaTitoloPerNomeLikeCom(car));
		    tavola_response = titolo.cercaTitoloPerNomeLikeCom(car, tipoOrd);
		} else {
		    TitoloCerca titolo = new TitoloCerca();
	        titolo.setEsporta(esporta);
		    V_daticomuni com = new V_daticomuni();
		    titolo.valorizzaFiltriCom(com, cercaDatiTit);
		    if(esporta){
				if(titolo.valorizzaFiltriPerExport(cercaDatiTit)){
					tavola_response = titolo.cercaTitoloPerNomeLikeSuperindex(tipoOrd);
			  	}else{
					tavola_response = titolo.cercaTitoloPerNomeLikeSoloBid(tipoOrd);
				}
		    }else{
				controllaNumeroRecord(titolo.contaTitoloPerNomeLikeCom(com));
				tavola_response = titolo.cercaTitoloPerNomeLikeCom(com, tipoOrd);
		    }
		}
	}



    /** Esegue una ricerca utilizzando il campo stringalike;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaPerClet() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (cercaDatiTit.getSottoTipoLegame() != null) {
            Vl_titolo_tit_b tit_doc = new Vl_titolo_tit_b();
            tit_doc.setTP_LEGAME_MUSICA(cercaDatiTit.getSottoTipoLegame()
                    .toString());
            //almaviva5_20091012
            tit_doc.setFILTRO_LOC_BIB(locBib);
            TitoloCerca titolo = new TitoloCerca();
            titolo.valorizzaFiltri(tit_doc, cercaDatiTit);
            String clet = Formattazione.formatta(titoloCerca.getTitoloCLET());
            controllaNumeroRecord(titolo.contaTitoloPerCletPerTpLegame(tit_doc,
                    clet));
            tavola_response = titolo.cercaTitoloPerCletPerTpLegame(tit_doc,
                    clet, tipoOrd);
        } else if (musicale) {
            TitoloMusica titolo = new TitoloMusica();
            V_musica mus = new V_musica();
            //almaviva5_20091012
            mus.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(mus, cercaDatiTit);
            String clet = Formattazione.formatta(titoloCerca.getTitoloCLET());
            controllaNumeroRecord(titolo.contaTitoloPerClet(mus, clet));
            tavola_response = titolo.cercaTitoloPerClet(mus, clet, tipoOrd);
        } else if (grafico) {
            TitoloGrafica titolo = new TitoloGrafica();
            V_grafica gra = new V_grafica();
            //almaviva5_20091012
            gra.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(gra, cercaDatiTit);
            String clet = Formattazione.formatta(titoloCerca.getTitoloCLET());
            controllaNumeroRecord(titolo.contaTitoloPerClet(gra, clet));
            tavola_response = titolo.cercaTitoloPerClet(gra, clet, tipoOrd);
        } else if (cartografico) {
            TitoloCartografia titolo = new TitoloCartografia();
            V_cartografia car = new V_cartografia();
            //almaviva5_20091012
            car.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(car, cercaDatiTit);
            String clet = Formattazione.formatta(titoloCerca.getTitoloCLET());
            controllaNumeroRecord(titolo.contaTitoloPerClet(car, clet));
            tavola_response = titolo.cercaTitoloPerClet(car, clet, tipoOrd);
        } else if (audiovisivo) {
        	// Evolutiva febbraio 2015: ricerche sui nuovi tipi materiale audiovisivo/elettronico
            TitoloAudiovisivo titolo = new TitoloAudiovisivo();
            V_audiovisivo car = new V_audiovisivo();
            titolo.valorizzaFiltri(car, cercaDatiTit);
            String clet = Formattazione.formatta(titoloCerca.getTitoloCLET());
            controllaNumeroRecord(titolo.contaTitoloPerClet(car, clet));
            tavola_response = titolo.cercaTitoloPerClet(car, clet, tipoOrd);
        } else if (elettronico) {
            TitoloElettronico titolo = new TitoloElettronico();
            V_elettronico ele = new V_elettronico();
            titolo.valorizzaFiltri(ele, cercaDatiTit);
            String clet = Formattazione.formatta(titoloCerca.getTitoloCLET());
            controllaNumeroRecord(titolo.contaTitoloPerClet(ele, clet));
            tavola_response = titolo.cercaTitoloPerClet(ele, clet, tipoOrd);
        } else {
            TitoloCerca titolo = new TitoloCerca();
            String clet = Formattazione.formatta(titoloCerca.getTitoloCLET());
            //almaviva5_20091012
            titolo.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerClet(clet));
            tavola_response = titolo.cercaTitoloPerClet(clet, tipoOrd);
        }
    }


    private void cercaPerCletCom() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (cercaDatiTit.getSottoTipoLegame() != null) { //da verificare
            Vl_titolo_tit_b tit_doc = new Vl_titolo_tit_b();
            tit_doc.setTP_LEGAME_MUSICA(cercaDatiTit.getSottoTipoLegame()
                    .toString());
            TitoloCerca titolo = new TitoloCerca();
            titolo.valorizzaFiltri(tit_doc, cercaDatiTit);
            String clet = Formattazione.formatta(titoloCerca.getTitoloCLET());
            controllaNumeroRecord(titolo.contaTitoloPerCletPerTpLegame(tit_doc,
                    clet));
            tavola_response = titolo.cercaTitoloPerCletPerTpLegame(tit_doc,
                    clet, tipoOrd);
        } else if (musicale) {
            TitoloMusica titolo = new TitoloMusica();
            V_musica_com com = new V_musica_com();
            titolo.valorizzaFiltri(com, cercaDatiTit);
            String clet = Formattazione.formatta(titoloCerca.getTitoloCLET());
            controllaNumeroRecord(titolo.contaTitoloPerCletCom(com, clet));
            tavola_response = titolo.cercaTitoloPerCletCom(com, clet, tipoOrd);
        } else if (grafico) {
            TitoloGrafica titolo = new TitoloGrafica();
            V_grafica_com gra = new V_grafica_com();
            titolo.valorizzaFiltri(gra, cercaDatiTit);
            String clet = Formattazione.formatta(titoloCerca.getTitoloCLET());
            controllaNumeroRecord(titolo.contaTitoloPerCletCom(gra, clet));
            tavola_response = titolo.cercaTitoloPerCletCom(gra, clet, tipoOrd);
        } else if (cartografico) {
            TitoloCartografia titolo = new TitoloCartografia();
            V_cartografia_com car = new V_cartografia_com();
            titolo.valorizzaFiltri(car, cercaDatiTit);
            String clet = Formattazione.formatta(titoloCerca.getTitoloCLET());
            controllaNumeroRecord(titolo.contaTitoloPerCletCom(car, clet));
            tavola_response = titolo.cercaTitoloPerCletCom(car, clet, tipoOrd);
        } else if (audiovisivo) {
            TitoloAudiovisivo titolo = new TitoloAudiovisivo();
            V_audiovisivo_com com = new V_audiovisivo_com();
            titolo.valorizzaFiltri(com, cercaDatiTit);
            String clet = Formattazione.formatta(titoloCerca.getTitoloCLET());
            controllaNumeroRecord(titolo.contaTitoloPerCletCom(com, clet));
            tavola_response = titolo.cercaTitoloPerCletCom(com, clet, tipoOrd);
//        } else if (elettronico) { //vista da fare
//            TitoloElettronico titolo = new TitoloElettronico(db_conn);
//            V_elettronico ele = new V_elettronico();
//            titolo.valorizzaFiltri(ele, cercaDatiTit);
//            String clet = Formattazione.formatta(titoloCerca.getTitoloCLET());
//            controllaNumeroRecord(titolo.contaTitoloPerClet(ele, clet));
//            tavola_response = titolo.cercaTitoloPerClet(ele, clet, tipoOrd);
        } else {
            TitoloCerca titolo = new TitoloCerca();
            String clet = Formattazione.formatta(titoloCerca.getTitoloCLET());
            titolo.valorizzaFiltri(cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerCletCom(clet));
            tavola_response = titolo.cercaTitoloPerCletCom(clet, tipoOrd);
        }
    }


    /** Esegue una ricerca utilizzando il campo T012 (impronta editoriale);
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaPerImpronta() throws IllegalArgumentException, InvocationTargetException, Exception {
        C012 c012 = null;
     // Evolutiva febbraio 2015: ricerche sui nuovi tipi materiale audiovisivo/elettronico
        if (cercaDatiTit instanceof CercaDocAnticoType)
            c012 = ((CercaDocAnticoType) cercaDatiTit).getT012();
        if (cercaDatiTit instanceof CercaDocMusicaType)
            c012 = ((CercaDocMusicaType) cercaDatiTit).getT012();
        if (cercaDatiTit instanceof CercaDocCartograficoType)
            c012 = ((CercaDocCartograficoType) cercaDatiTit).getT012();
        if (cercaDatiTit instanceof CercaDocGraficaType)
            c012 = ((CercaDocGraficaType) cercaDatiTit).getT012();
        if (musicale) {
            TitoloMusica titolo = new TitoloMusica();
            Vl_musica_imp tit = new Vl_musica_imp();
            tit.settaParametro(TableDao.XXXimpronta1, (c012.getA_012_1()));
            tit.settaParametro(TableDao.XXXimpronta2, (c012.getA_012_2()));
            tit.settaParametro(TableDao.XXXimpronta3, (c012.getA_012_3()));
            //almaviva5_20091012
            tit.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerImpronta(tit));
            tavola_response = titolo.cercaTitoloPerImpronta(tit, tipoOrd);
        } else {
            TitoloCerca titolo = new TitoloCerca();
            Vl_titolo_imp tit = new Vl_titolo_imp();
            tit.settaParametro(TableDao.XXXimpronta1, (c012.getA_012_1()));
            tit.settaParametro(TableDao.XXXimpronta2, (c012.getA_012_2()));
            tit.settaParametro(TableDao.XXXimpronta3, (c012.getA_012_3()));
            //almaviva5_20091012
            tit.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerImpronta(tit));
            tavola_response = titolo.cercaTitoloPerImpronta(tit, tipoOrd);
        }
    }

    /** Esegue la ricerca per coordinate
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaPerCoordinate(C123 c123) throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloCartografia titolo = new TitoloCartografia();
        V_cartografia car = new V_cartografia();
        //almaviva5_20091012
        car.setFILTRO_LOC_BIB(locBib);
        titolo.valorizzaFiltri(car, cercaDatiTit);
        controllaNumeroRecord(titolo.contaTitoloPerCoordinate(car));
        tavola_response = titolo.cercaTitoloPerCoordinate(car, tipoOrd);
    }

    /** Esegue una ricerca utilizzando un legame con autore;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaConFiltriAutore() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (musicale) {
            MusicaAutCerca titAut = new MusicaAutCerca(this,cercaTitolo, tipoOrd);
            //almaviva5_20091012
            titAut.setFILTRO_LOC_BIB(locBib);
            tavola_response = titAut.cercaTitolo();
        } else if (grafico) {
            GraficaAutCerca titAut = new GraficaAutCerca(this,cercaTitolo, tipoOrd);
            //almaviva5_20091012
            titAut.setFILTRO_LOC_BIB(locBib);
            tavola_response = titAut.cercaTitolo();
        } else if (cartografico) {
            CartografiaAutCerca titAut = new CartografiaAutCerca(this,cercaTitolo, tipoOrd);
            //almaviva5_20091012
            titAut.setFILTRO_LOC_BIB(locBib);
            tavola_response = titAut.cercaTitolo();
        } else if (audiovisivo) {
        	// Evolutiva febbraio 2015: ricerche sui nuovi tipi materiale audiovisivo/elettronico
            AudiovisivoAutCerca titAut = new AudiovisivoAutCerca(this,cercaTitolo, tipoOrd);
            tavola_response = titAut.cercaTitolo();
//        } else if (elettronico) {
//            ElettronicoAutCerca titAut = new ElettronicoAutCerca(this, db_conn,
//                    cercaTitolo, tipoOrd);
//            tavola_response = titAut.cercaTitolo();
        } else {
            TitoloAutCerca titAut = new TitoloAutCerca(this, cercaTitolo, tipoOrd);
            //almaviva5_20091012
            titAut.setFILTRO_LOC_BIB(locBib);
            tavola_response = titAut.cercaTitolo();
        }
    }

    /** Esegue una ricerca utilizzando un legame con soggetto;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaConSoggetto(String idArrivo)
            throws IllegalArgumentException, InvocationTargetException, Exception {
        if (grafico) {
            TitoloGrafica titolo = new TitoloGrafica();
            Vl_grafica_sog tit = new Vl_grafica_sog();
            tit.setCID(idArrivo);
            //almaviva5_20091007
            tit.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerSoggetto(tit));
            tavola_response = titolo.cercaTitoloPerSoggetto(tit, tipoOrd);
        } else if (cartografico) {
            TitoloCartografia titolo = new TitoloCartografia();
            Vl_cartografia_sog tit = new Vl_cartografia_sog();
            tit.setCID(idArrivo);
            //almaviva5_20091007
            tit.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerSoggetto(tit));
            tavola_response = titolo.cercaTitoloPerSoggetto(tit, tipoOrd);
        } else {
            TitoloCerca titolo = new TitoloCerca();
            Vl_titolo_sog tit = new Vl_titolo_sog();
            tit.setCID(idArrivo);
            //almaviva5_20091007
            tit.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerSoggetto(tit));
            tavola_response = titolo.cercaTitoloPerSoggetto(tit, tipoOrd);
        }
    }

    /** Esegue una ricerca utilizzando un legame con thesauro;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaConThesauro(String idArrivo)
            throws IllegalArgumentException, InvocationTargetException, Exception {
        if (grafico) {
            TitoloGrafica titolo = new TitoloGrafica();
            Vl_grafica_the tit = new Vl_grafica_the();
            tit.setDID(idArrivo);
            //almaviva5_20091007
            tit.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerThesauro(tit));
            tavola_response = titolo.cercaTitoloPerThesauro(tit, tipoOrd);
        } else if (cartografico) {
            TitoloCartografia titolo = new TitoloCartografia();
            Vl_cartografia_the tit = new Vl_cartografia_the();
            tit.setDID(idArrivo);
            //almaviva5_20091007
            tit.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerThesauro(tit));
            tavola_response = titolo.cercaTitoloPerThesauro(tit, tipoOrd);
        } else {
            TitoloCerca titolo = new TitoloCerca();
            Vl_titolo_the tit = new Vl_titolo_the();
            tit.setDID(idArrivo);
            //almaviva5_20091007
            tit.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerThesauro(tit));
            tavola_response = titolo.cercaTitoloPerThesauro(tit, tipoOrd);
        }
    }



    /** Esegue una ricerca utilizzando un legame con autore;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaConClasse(String idArrivo)
            throws IllegalArgumentException, InvocationTargetException, Exception {

    	//almaviva5_20090414
    	String sistema = null;
    	String edizione = null;
    	String simbolo = null;

    	//almaviva5_20141114 edizioni ridotte
        SimboloDewey sd = SimboloDewey.parse(idArrivo);
        //almaviva5_20090414
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

        if (musicale) {
            TitoloMusica titolo = new TitoloMusica();
            Vl_musica_cla tit = new Vl_musica_cla();
            tit.setCD_SISTEMA(sistema);
            tit.setCD_EDIZIONE(edizione);
            tit.setCLASSE(simbolo);
            //almaviva5_20091007
            tit.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerClasse(tit));
            tavola_response = titolo.cercaTitoloPerClasse(tit, tipoOrd);
        } else if (grafico) {
            TitoloGrafica titolo = new TitoloGrafica();
            Vl_grafica_cla tit = new Vl_grafica_cla();
            tit.setCD_SISTEMA(sistema);
            tit.setCD_EDIZIONE(edizione);
            tit.setCLASSE(simbolo);
            //almaviva5_20091007
            tit.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerClasse(tit));
            tavola_response = titolo.cercaTitoloPerClasse(tit, tipoOrd);
        } else if (cartografico) {
            TitoloCartografia titolo = new TitoloCartografia();
            Vl_cartografia_cla tit = new Vl_cartografia_cla();
            tit.setCD_SISTEMA(sistema);
            tit.setCD_EDIZIONE(edizione);
            tit.setCLASSE(simbolo);
            //almaviva5_20091007
            tit.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerClasse(tit));
            tavola_response = titolo.cercaTitoloPerClasse(tit, tipoOrd);
        } else {
            TitoloCerca titolo = new TitoloCerca();
            Vl_titolo_cla tit = new Vl_titolo_cla();
            tit.setCD_SISTEMA(sistema);
            tit.setCD_EDIZIONE(edizione);
            tit.setCLASSE(simbolo);
            //almaviva5_20091007
            tit.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerClasse(tit));
            tavola_response = titolo.cercaTitoloPerClasse(tit, tipoOrd);
        }
    }

    /** Esegue una ricerca utilizzando un legame con autore;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaConFiltriLuogo() throws IllegalArgumentException, InvocationTargetException, Exception {
        if (musicale) {
            MusicaLuoCerca titAut = new MusicaLuoCerca(this,cercaTitolo, tipoOrd);
            //almaviva5_20091012
            titAut.setFILTRO_LOC_BIB(locBib);
            tavola_response = titAut.cercaTitolo();
        } else if (grafico) {
            GraficaLuoCerca titAut = new GraficaLuoCerca(this, cercaTitolo, tipoOrd);
            //almaviva5_20091012
            titAut.setFILTRO_LOC_BIB(locBib);
            tavola_response = titAut.cercaTitolo();
        } else if (cartografico) {
            CartografiaLuoCerca titAut = new CartografiaLuoCerca(this, cercaTitolo, tipoOrd);
            //almaviva5_20091012
            titAut.setFILTRO_LOC_BIB(locBib);
            tavola_response = titAut.cercaTitolo();
        } else {

            TitoloLuoCerca titAut = new TitoloLuoCerca(this, cercaTitolo, tipoOrd);
            //almaviva5_20091012
            titAut.setFILTRO_LOC_BIB(locBib);
            tavola_response = titAut.cercaTitolo();
        }
    }

    /** Esegue una ricerca utilizzando un legame con autore;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaConFiltriMarca() throws IllegalArgumentException, InvocationTargetException, Exception {
        TitoloMarCerca titMar = new TitoloMarCerca(this, cercaTitolo,tipoOrd);
        //almaviva5_20091012
        titMar.setFILTRO_LOC_BIB(locBib);
        tavola_response = titMar.cercaTitolo();
    }

    /** Esegue una ricerca utilizzando un legame con autore;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaConMarca(String idArrivo) throws IllegalArgumentException, InvocationTargetException, Exception {
        if (grafico) {
            TitoloGrafica titolo = new TitoloGrafica();
            Vl_grafica_mar tit = new Vl_grafica_mar();
            tit.setMID(idArrivo);
            //almaviva5_20091012
            tit.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerMarca(tit));
            tavola_response = titolo.cercaTitoloPerMarca(tit, tipoOrd);
        } else if (cartografico) {
            TitoloCartografia titolo = new TitoloCartografia();
            Vl_cartografia_mar tit = new Vl_cartografia_mar();
            tit.setMID(idArrivo);
            //almaviva5_20091012
            tit.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerMarca(tit));
            tavola_response = titolo.cercaTitoloPerMarca(tit, tipoOrd);
        } else if (musicale) {
            TitoloMusica titolo = new TitoloMusica();
            Vl_musica_mar tit = new Vl_musica_mar();
            tit.setMID(idArrivo);
            //almaviva5_20091012
            tit.setFILTRO_LOC_BIB(locBib);
            titolo.valorizzaFiltri(tit, cercaDatiTit);
            controllaNumeroRecord(titolo.contaTitoloPerMarca(tit));
            tavola_response = titolo.cercaTitoloPerMarca(tit, tipoOrd);
        } else {
            TitoloMarCerca titAut = new TitoloMarCerca(this, cercaTitolo, tipoOrd);
            //almaviva5_20091012
            titAut.setFILTRO_LOC_BIB(locBib);
            tavola_response = titAut.cercaTitolo();
        }
    }

    /** Esegue una ricerca utilizzando un legame con autore;
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException */
    private void cercaConFiltriComposizione() throws IllegalArgumentException, InvocationTargetException, Exception {
        MusicaComCerca titAut = new MusicaComCerca(this, cercaTitolo,tipoOrd);
        //almaviva5_20091012
        titAut.setFILTRO_LOC_BIB(locBib);
        tavola_response = titAut.cercaTitolo();
    }

    /** Prepara la stringa di output in formato xml */
    protected SBNMarc preparaOutput() throws EccezioneDB, EccezioneFactoring,
            EccezioneSbnDiagnostico {
        if (tavola_response == null) {
            log
                    .error("Errore nel factoring ricorsivo: oggetto tavola_response nullo");
            throw new EccezioneFactoring(201);
        }
        // Deve utilizzare il numero di richieste che servono
        //TableDao response = tavola_response.getElencoRisultati(maxRighe);
        List response = eliminaBidDuplicati();
        SBNMarc risultato = formattaOutput(response);
        rowCounter += response.size();
        return risultato;
    }

    public String getIdAttivita() {
        return CodiciAttivita.getIstance().CERCA_DOCUMENTO_1002;
    }

    public String getIdAttivitaSt() {
        return CodiciAttivita.getIstance().CERCA_DOCUMENTO_1002;
    }

    /**
     * Prepara l'xml di risposta utilizzando il vettore TableDao_response
     *
     * @return Stringa contenente l'xml
     */
    private SBNMarc formattaOutput(List TableDao_response) throws EccezioneDB,
            EccezioneFactoring, EccezioneSbnDiagnostico {
        if (ricercaSenzaCount) {
            numeroRecord = TableDao_response.size();
        }
        return FormatoTitolo.formattaLista(TableDao_response,
                user_object, factoring_object.getTipoOutput(), factoring_object
                        .getTipoOrd(), idLista, rowCounter, maxRighe,
                numeroRecord, schemaVersion, null, null, esporta);
    }
 

}
