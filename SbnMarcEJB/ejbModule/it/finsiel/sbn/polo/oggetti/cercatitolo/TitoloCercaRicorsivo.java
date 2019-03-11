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

import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoTitolo;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_c;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameTitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameDocTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameElementoAutTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LegameTitAccessoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameAut;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLegameTitAccesso;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSpecLegameDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * Classe TitoloCerca.java
 * <p>
 * Esegue la ricerca ricorsiva dei titoli nel DB per costruire l'output del
 * factoring CercaTitolo. E' sorto un problema particolare con le monografie,
 * per le quali è necessario ricercare i titoli verso il basso. Per evitare
 * possibili loop anche questa ricerca deve essere inserita nel sistema
 * ricorsivo. Sono sorti problemi poichè si utilizza una vista diversa (la
 * Vl_titolo_tit_c) per cui ho modificato l'oggetto nodo inserendovi
 * l'alternativa. Chiamando il metodo
 * cercaTuttiLegamiDocumento(nodo.getTitolo(), ordinamento) se il titolo in
 * questione è una monografia (natura M) si invoca anche la chiamata verso il
 * basso.
 * </p>
 *
 * @author
 * @author
 *
 * @version 28-mar-03
 */
public class TitoloCercaRicorsivo extends Titolo {

	private static final long serialVersionUID = 3541225405095908068L;

	private static Logger log = Logger.getLogger("it.finsiel.sbn.polo.oggetti.cercatitolo.TitoloCercaRicorsivo");
    SbnTipoOutput tipoOut;

    String ordinamento;

    Map<String, Nodo> nodi;

    FormatoTitolo formatoTitolo;

    SbnUserType user;

    List<Legame> legamiInseriti;

    /** Costruttore che inizializza i vari campi */
    public TitoloCercaRicorsivo(SbnTipoOutput tipoOut, String ordinamento,
            SbnUserType user, FormatoTitolo formatoTitolo) {
        super();
        this.user = user;
        this.tipoOut = tipoOut;
        this.ordinamento = ordinamento;
        this.formatoTitolo = formatoTitolo;
    }

    /** Formatta i legami di un documento, probabilmente andrà spostato.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException */
    public LegamiType formattaLegamiDocDoc(Tb_titolo titolo_partenza)
            throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {

        nodi = cercaRicorsivo(titolo_partenza, ordinamento);
        LegamiType legame = new LegamiType();
        Nodo nodo = nodi.get(titolo_partenza.getBID());
        legame.setIdPartenza(titolo_partenza.getBID());
        // LegameDocType legamedoc = new LegameDocType();

        // Utilizzo un sistema ricorsivo: per ogni nodo se nuovo = true lo
        // espando.
        legamiInseriti = new ArrayList();
        int size = ValidazioneDati.size(nodo.legamiTitoli);
		for (int i = 0; i < size; i++) {
			 // Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
            // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
			// Costanti per la gestione dei legami della natura V quando radice (SI) e quando nodo (NO)
			boolean GESTISCI_NATURA_V_NO = false;
            ArrivoLegame arrivo = espandiLegame((Nodo) nodo.legamiTitoli.get(i), titolo_partenza.getBID(), GESTISCI_NATURA_V_NO);
            arrivoLegameValido(arrivo);
            legame.addArrivoLegame(arrivo);
        }

        return legame;
    }

    private static final void arrivoLegameValido(ArrivoLegame al) throws EccezioneSbnDiagnostico {
		try {
			LegameDocType doc = al.getLegameDoc();
			LegameElementoAutType aut = al.getLegameElementoAut();
			LegameTitAccessoType acc = al.getLegameTitAccesso();

			if (doc != null && aut == null && acc == null)
				return;

			if (doc == null && aut != null && acc == null)
				return;

			if (doc == null && aut == null && acc != null)
				return;

		} catch (Exception e) {
			log.error("", e);
		}
		// Inizio modifica almaviva2 13.07.2010 - Prova per verifica ritorno dei bid fra i quali è presente il legame non valido
//		throw new EccezioneSbnDiagnostico(3031);
		throw new EccezioneSbnDiagnostico(3031,	"Non esiste alcun oggetto di arrivo del legame");
		// Fine modifica almaviva2 13.07.2010

	}



    private ArrivoLegame espandiLegame(Nodo nodo, String id_partenza)
    throws  IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
   	 // Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
        // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
		// Costanti per la gestione dei legami della natura V quando radice (SI) e quando nodo (NO)
		boolean GESTISCI_NATURA_V_NO = false;
    	return espandiLegame(nodo, id_partenza, GESTISCI_NATURA_V_NO);
    }

    private ArrivoLegame espandiLegame(Nodo nodo, String id_partenza, boolean gestisciNaturaV)
            throws IndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException, Exception {
        ArrivoLegame arrLeg;
        DocumentoType docType;
        ElementAutType elemAutType;
        FormatoTitolo formatoNodo = FormatoTitolo.getInstance(tipoOut,
                ordinamento, user,formatoTitolo.getSchema_version() , nodo.getTitolo());

        // Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
        // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
        // Object documento = formatoNodo.formattaElemento(nodo.getTitolo());
        Object documento = formatoNodo.formattaElemento(nodo.getTitolo(),formatoTitolo.getSchema_version(), gestisciNaturaV);


        legamiInseriti.add(new Legame(id_partenza, nodo.getTitolo().getBID()));

        arrLeg = new ArrivoLegame();
        if (documento instanceof DocumentoType) {
            docType = (DocumentoType) documento;
            // Controllo se è un documento o un titolo di accesso
            if (docType.getDocumentoTypeChoice().getDatiDocumento() != null) {

                // Devo utilizzare questo
                // formatoTitolo.formattaLegameDocumento(nodo.getTitolo());
                LegameDocType legDoc = new LegameDocType();
                legDoc.setIdArrivo(nodo.getTitolo().getBID());
                // legDoc.setTipoLegame(SbnLegameDoc.valueOf("410"));
                // //nodo.titolo.getTP_LEGAME()));
                if(nodo.getFl_condiviso_legame() != null)
                	legDoc.setCondiviso(LegameDocTypeCondivisoType.valueOf(nodo.getFl_condiviso_legame()));

                String tp_legame;
                if (nodo.tit_b)
                    tp_legame = formatoTitolo.convertiTpLegame(nodo
                            .getTp_legame(), nodo.getCd_natura_base(), nodo
                            .getCd_natura_coll());
                else
                    tp_legame = formatoTitolo.convertiTpLegame("51", nodo
                            .getCd_natura_coll(), nodo.getCd_natura_base());
                if (tp_legame != null) {
                    legDoc.setTipoLegame(SbnLegameDoc.valueOf(tp_legame));
                    if (nodo.getNota_tit_tit() != null
                            && !nodo.getNota_tit_tit().trim().equals(""))
                        legDoc.setNoteLegame(nodo.getNota_tit_tit());
                    if (nodo.getSequenza() != null
                            && !nodo.getSequenza().trim().equals(""))
                        legDoc.setSequenza(nodo.getSequenza());
                    if (nodo.getSici() != null
                            && !nodo.getSici().trim().equals(""))
                        legDoc.setSici(nodo.getSici());
                    legDoc.setDocumentoLegato(docType);
                    arrLeg.setLegameDoc(legDoc);
                    Nodo nodo2 = nodi.get(nodo.getTitolo().getBID());
                    if (nodo2.legamiTitoli.size() > 0) {
                        LegamiType legType = new LegamiType();
                        legType.setIdPartenza(nodo.getTitolo().getBID());
                        if (nodo.nuovo) {
                            // Nodo nodo2 = (Nodo)
                            // nodi.get(nodo.titolo.getBID());
                            for (int c = 0; c < nodo2.legamiTitoli.size(); c++) {
                                if (legameNonInserito(
                                        nodo.getTitolo().getBID(),
                                        ((Nodo) nodo2.legamiTitoli.get(c))
                                                .getTitolo().getBID())) {
									ArrivoLegame legame = espandiLegame(
													        (Nodo) nodo2.legamiTitoli.get(c),
													        nodo.getTitolo().getBID());
									if (legame != null)
										legType.addArrivoLegame(legame);
								}
                            }
                        }
                        if (legType.getArrivoLegameCount() > 0) {
                            docType.addLegamiDocumento(legType);
                        }
                    }
                }
                else	//almaviva5_20091120
					throw new EccezioneSbnDiagnostico(3031,
							"Oggetti interessati: " + id_partenza + " e "
									+ nodo.getTitolo().getBID());
            } else {
                // Titolo di accesso
                LegameTitAccessoType legDoc = new LegameTitAccessoType();
                if (nodo.getTp_legame_musica() != null
                        && nodo.getTp_legame_musica().trim().length() > 0) {
                    String lega_mus = Decodificatore.getCd_unimarc(
                            "Tr_tit_tit", "tp_legame_musica", nodo
                                    .getTp_legame_musica());
                    if (lega_mus != null)
                        legDoc.setSottoTipoLegame(SbnSpecLegameDoc
                                .valueOf(lega_mus));
                }
                legDoc.setIdArrivo(nodo.getTitolo().getBID());
                if(nodo.getFl_condiviso_legame() != null)
                	legDoc.setCondiviso(LegameTitAccessoTypeCondivisoType.valueOf(nodo.getFl_condiviso_legame()));

                String cd_natura = nodo.getTitolo().getCD_NATURA();
                legDoc.setTipoLegame(SbnLegameTitAccesso
                        .valueOf(formatoTitolo.convertiTpLegame(nodo
                                .getTp_legame(), nodo.getCd_natura_base(), nodo
                                .getCd_natura_coll())));
                if (nodo.getNota_tit_tit() != null
                        && !nodo.getNota_tit_tit().trim().equals(""))
                    legDoc.setNoteLegame(nodo.getNota_tit_tit());
                legDoc.setTitAccessoLegato(docType);
                arrLeg.setLegameTitAccesso(legDoc);
                // Da qui uguale a quello sopra
                Nodo nodo2 = nodi.get(nodo.getTitolo().getBID());
                if (nodo2.legamiTitoli.size() > 0) {
                    LegamiType legType = new LegamiType();
                    legType.setIdPartenza(nodo.getTitolo().getBID());
                    if (nodo.nuovo) {
                        // Nodo nodo2 = (Nodo) nodi.get(nodo.titolo.getBID());
                        for (int c = 0; c < nodo2.legamiTitoli.size(); c++) {
                            if (legameNonInserito(nodo.getTitolo().getBID(),
                                    ((Nodo) nodo2.legamiTitoli.get(c))
                                            .getTitolo().getBID())) {
								ArrivoLegame legame = espandiLegame(
												        (Nodo) nodo2.legamiTitoli.get(c), nodo
												                .getTitolo().getBID());
								if (legame != null)
									legType.addArrivoLegame(legame);
							}
                        }
                    }
                    if (legType.getArrivoLegameCount() > 0)
                        docType.addLegamiDocumento(legType);
                    // Sino qui
                }
            }
            formatoNodo.aggiungiAltriLegami(nodo.getTitolo(),docType);

        } else if (documento instanceof ElementAutType) {
            // Ultimo tipo: element aut type
            String tipoLegame = formatoTitolo.convertiTpLegame(nodo
                    .getTp_legame(), nodo.getCd_natura_base(), nodo
                    .getCd_natura_coll());

            if (tipoLegame != null) {
                elemAutType = (ElementAutType) documento;
                LegameElementoAutType legDoc = new LegameElementoAutType();
                legDoc.setTipoAuthority(elemAutType.getDatiElementoAut()
                        .getTipoAuthority());
                legDoc.setIdArrivo(nodo.getTitolo().getBID());
                if(nodo.getFl_condiviso_legame() != null)
                	legDoc.setCondiviso(LegameElementoAutTypeCondivisoType.valueOf(nodo.getFl_condiviso_legame()));

                if (nodo.getNota_tit_tit() != null
                        && !nodo.getNota_tit_tit().trim().equals(""))
                    legDoc.setNoteLegame(nodo.getNota_tit_tit());
                legDoc.setElementoAutLegato(elemAutType);
                legDoc.setTipoLegame(checkTipoLegameAut(tipoLegame, id_partenza, nodo.getTitolo().getBID()));
                arrLeg.setLegameElementoAut(legDoc);
                Nodo nodo2 = nodi.get(nodo.getTitolo().getBID());
                if (nodo2.legamiTitoli.size() > 0) {
                    LegamiType legType = new LegamiType();
                    legType.setIdPartenza(nodo.getTitolo().getBID());
                    if (nodo.nuovo) {
                        // Nodo nodo2 = (Nodo) nodi.get(nodo.titolo.getBID());
                        for (int c = 0; c < nodo2.legamiTitoli.size(); c++) {
                            if (legameNonInserito(nodo.getTitolo().getBID(),
                                    ((Nodo) nodo2.legamiTitoli.get(c))
                                            .getTitolo().getBID())) {
								ArrivoLegame legame = espandiLegame((Nodo) nodo2.legamiTitoli.get(c), nodo.getTitolo().getBID());
								if (legame != null)
									legType.addArrivoLegame(legame);
							}
                        }
                    }
                    //almaviva5_20090928 #3123 #3158
                    if (legType.getArrivoLegameCount() > 0)
                    	elemAutType.addLegamiElementoAut(legType);
                }
            }
            else
            	//almaviva5_20120222
				throw new EccezioneSbnDiagnostico(3031,
						"Oggetti interessati: " + id_partenza + " e "
								+ nodo.getTitolo().getBID());
        }

        return arrLeg;
    }

	private SbnLegameAut checkTipoLegameAut(String tipoLegame, String id_partenza, String id_arrivo) throws EccezioneSbnDiagnostico {
		try {
			return SbnLegameAut.valueOf(tipoLegame);
		} catch (Exception e) {
			throw new EccezioneSbnDiagnostico(3031, String.format("%s \u2190 (%s) \u2192 %s", id_partenza, tipoLegame, id_arrivo));
		}
	}

    /**
     * Esegue la ricerca ricorsiva dei titoli per estrarre dal db tutti i titoli
     * legati tra di loro. Particolare attenzione è rivolta ad evitare i
     * possibili loop.
     *
     * @param l'ordinamento
     *            non so se abbia significato
     * @return Map di oggetti nodo : coppie <bid-nodo>
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public Map<String, Nodo> cercaRicorsivo(Tb_titolo tit_partenza, String ordinamento)
            throws IllegalArgumentException, InvocationTargetException, Exception {
        Vl_titolo_tit_b titolo = new Vl_titolo_tit_b();
        titolo.setBID(tit_partenza.getBID());
        titolo.setCD_NATURA(tit_partenza.getCD_NATURA());
        List elenco = new ArrayList<Tb_titolo>();
        // cercaLegamiDocumento(titolo, ordinamento);
        elenco.add(titolo);
        // hashtable bid-nodo
        Map nodi = new HashMap();
        Nodo nodo, nodo2;
        // inizializzo i nodi
        int counter;
        List legami;

        // Scorro l'elenco fino alla fine
        for (counter = 0; counter < elenco.size(); counter++) {
            Object obj = elenco.get(counter);
            nodo = new Nodo((Tb_titolo) obj);
            nodo.nuovo = true;
            nodi.put(nodo.getTitolo().getBID(), nodo);
            // Questo ricerca anche in profondità per le monografie (solo per le
            // ricerche
            // del titolo di riferimento)
            legami = cercaTuttiLegamiDocumento(nodo.getTitolo(), counter == 0);
            // legami = cercaLegamiDocumento(nodo.getTitolo(), ordinamento);
            // Scorro tutti i legami, aggiungendoli nell'elenco opportuno dei
            // legami del nodo
            for (int i = 0; i < legami.size(); i++) {
                obj = legami.get(i);
                nodo2 = new Nodo((Tb_titolo) obj);
                nodo.legamiTitoli.add(nodo2);
                if (contenuto(elenco, nodo2.getTitolo())) {
                    nodo2.nuovo = false;
                } else {
                    nodo2.nuovo = true;
                    elenco.add(nodo2.getTitolo());
                }
            }
        }
        return nodi;
    }

    private boolean legameNonInserito(String id1, String id2) {
        Legame leg;
        for (int i = 0; i < legamiInseriti.size(); i++) {
            leg = legamiInseriti.get(i);
            if ((id1.equals(leg.id1) && id2.equals(leg.id2))
                    || (id1.equals(leg.id2) && id2.equals(leg.id1)))
                return false;
        }
        return true;
    }

    /**
     * Verifica se il titolo è contenuto nel vettore di titoli, confrontando il
     * bid
     */
    protected boolean contenuto(List v, Tb_titolo tit) {
        for (int i = 0; i < v.size(); i++)
            if (((Tb_titolo) v.get(i)).getBID().equals(tit.getBID()))
                return true;
        return false;
    }

    /** Classe di utilizzo personale */
    class Nodo {
        private Vl_titolo_tit_b titolo_b = null;

        private Vl_titolo_tit_c titolo_c = null;

        public final boolean tit_b;

        public List legamiTitoli = new ArrayList();

        public Nodo(Tb_titolo titolo) {
            if (titolo instanceof Vl_titolo_tit_b) {
                titolo_b = (Vl_titolo_tit_b) titolo;
                tit_b = true;
            } else {
                titolo_c = (Vl_titolo_tit_c) titolo;
                tit_b = false;
            }
        }

        public boolean nuovo = false;

        public Tb_titolo getTitolo() {
            if (titolo_b != null)
                return titolo_b;
            return titolo_c;
        }

        public String getFl_condiviso() {
            if (titolo_b != null)
                return titolo_b.getFL_CONDIVISO();
            return titolo_c.getFL_CONDIVISO();
        }

        public String getFl_condiviso_legame() {
            if (titolo_b != null)
                return titolo_b.getFL_CONDIVISO_LEGAME();
            return titolo_c.getFL_CONDIVISO_LEGAME();
        }

        public String getCd_natura_coll() {
            if (titolo_b != null)
                return titolo_b.getCD_NATURA_COLL();
            return titolo_c.getCD_NATURA_COLL();
        }

        public String getCd_natura_base() {
            if (titolo_b != null)
                return titolo_b.getCD_NATURA_BASE();
            return titolo_c.getCD_NATURA_BASE();
        }

        public String getTp_legame() {
            if (titolo_b != null)
                return titolo_b.getTP_LEGAME();
            return titolo_c.getTP_LEGAME();
        }

        public String getTp_legame_musica() {
            if (titolo_b != null)
                return titolo_b.getTP_LEGAME_MUSICA();
            return titolo_c.getTP_LEGAME_MUSICA();
        }

        public String getNota_tit_tit() {
            if (titolo_b != null)
                return titolo_b.getNOTA_TIT_TIT();
            return titolo_c.getNOTA_TIT_TIT();
        }

        public String getSequenza() {
            if (titolo_b != null)
                return titolo_b.getSEQUENZA();
            return titolo_c.getSEQUENZA();
        }

        public String getSici() {
            if (titolo_b != null)
                return titolo_b.getSICI();
            return titolo_c.getSICI();
        }
    }

    class Legame {
        String id1 = null;

        String id2 = null;

        public Legame(String id1, String id2) {
            this.id1 = id1;
            this.id2 = id2;
        }
    }


    /**
     * Formatta i campi in output per il timbro di condivisione
     */
//	public boolean formattaTimbroCondivisione(Tb_titolo tabella, boolean legame) {
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
//    /**
//     * Formatta i campi in output per il timbro di condivisione
//     */
//	public boolean formattaTimbroCondivisione(Tb_autore tabella, boolean legame) {
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

}
