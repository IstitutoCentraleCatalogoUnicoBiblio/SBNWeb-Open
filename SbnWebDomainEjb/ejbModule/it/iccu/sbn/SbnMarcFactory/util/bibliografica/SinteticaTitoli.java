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
package it.iccu.sbn.SbnMarcFactory.util.bibliografica;

import it.iccu.sbn.SbnMarcFactory.util.UtilityCastor;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.model.unimarcmodel.A100;
import it.iccu.sbn.ejb.model.unimarcmodel.ArrivoLegame;
import it.iccu.sbn.ejb.model.unimarcmodel.C100;
import it.iccu.sbn.ejb.model.unimarcmodel.C110;
import it.iccu.sbn.ejb.model.unimarcmodel.C200;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.GraficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.GuidaDoc;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegameElementoAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.MusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.TitAccessoType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeMusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.TitoloUniformeType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiDocTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoRecord;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TitAccessoTypeCondivisoType;
import it.iccu.sbn.ejb.services.Codici;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.SinteticaTitoliView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;

public class SinteticaTitoli {

	UtilityCastor utilityCastor = new UtilityCastor();

	private Codici codici;

	public SinteticaTitoli() {
		try {
			this.codici = DomainEJBFactory.getInstance().getCodici();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List getSintetica(SBNMarc sbnMarcType, boolean remove, int ultProgressivo, String visualNumSequenza) throws RemoteException {

        List<SinteticaTitoliView> listaSintentica = new ArrayList<SinteticaTitoliView>();
        SbnMessageType sbnMessage = sbnMarcType.getSbnMessage();
        SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
        SbnResponseTypeChoice sbnResponseChoice = sbnResponse.getSbnResponseTypeChoice();
        SbnOutputType sbnOutPut = sbnResponseChoice.getSbnOutput();

        int progressivo = ultProgressivo;
        int numeroElementiDoc1 = getNumeroElementiDocumento(sbnOutPut);
        int numeroElementiAut1 = getNumeroElementiAutority(sbnOutPut);
        int totElementi = numeroElementiDoc1 + numeroElementiAut1;

        //System.out.println("numTotDoc=" + numeroElementiDoc1);
        //System.out.println("numTotAut=" + numeroElementiAut1);
        SinteticaTitoliView sinteticaTitoliView;
        for (int t = 0; t < totElementi; t++) {

        	//System.out.println("Valore T =" + t);
            for (int i = 0; i < numeroElementiDoc1; i++) {
                if ((sbnOutPut.getDocumento(i).getNLista() == t)) {
                    ++progressivo;
                    //sinteticaTitoliView = new SinteticaTitoliView();
                    sinteticaTitoliView = creaElementoLista(sbnOutPut, i, progressivo, visualNumSequenza);
                    listaSintentica.add(sinteticaTitoliView);
                    break;
                }
            }

            for (int s = 0; s < numeroElementiAut1; s++) {
                if ((sbnOutPut.getElementoAut(s).getNLista() == t)) {
                    ++progressivo;
                    //sinteticaTitoliView = new SinteticaTitoliView();
                    sinteticaTitoliView = creaElementoListaAutority(sbnOutPut, s, progressivo);
                    listaSintentica.add(sinteticaTitoliView);
                    break;
                }
            }

        }
        return listaSintentica;
    }

    public SinteticaTitoliView creaElementoListaAutority(
            SbnOutputType sbnOutPut, int elementIndex, int progressivo) {

    	UtilityCampiTitolo utilityCampiTitolo = new UtilityCampiTitolo();
        SinteticaTitoliView data = new SinteticaTitoliView();
        String descrizioneLegami = "";
        String titoloLocale = "";

        // Leggo Elementi di autority
        ElementAutType elementoAut = sbnOutPut.getElementoAut(elementIndex);

        if (elementoAut.getDatiElementoAut() != null) {
            DatiElementoType datiElemento = elementoAut.getDatiElementoAut();
            data.setImageUrl("");
            // PROGRESSIVO: Numero del elemento
            data.setProgressivo(progressivo);
            // BID
            String bid = datiElemento.getT001();
            data.setBid(bid.trim());

            if (datiElemento.getCondiviso() == null){
            	data.setFlagCondiviso(true);
            } else {
            	 if (datiElemento.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE){
            		titoloLocale = "[loc]";
                 	data.setFlagCondiviso(false);
     			} else {
     				data.setFlagCondiviso(true);
     			}
            }

            // Numero di elementi trovati
            data.setNumNotizie(sbnOutPut.getTotRighe());

            // Data inserimento
            A100 a100 = datiElemento.getT100();
            String dataIns = "";
            if (a100 != null){
                if (a100.getA_100_0() != null) {
                	dataIns = a100.getA_100_0().toString();
                    data.setDataIns(dataIns);
                } else {
                	data.setDataIns("");
                }
            } else {
            	data.setDataIns("");
            }

            // Tipo Materiale
            // Viene forzato a U nel caso in cui tipoAutority e == UM Uniforme Musica
            if (datiElemento.getTipoAuthority().toString().equals("UM")) {
                data.setTipoMateriale("musica");
                data.setImageUrl("sintMusica.gif");
            } else {
                data.setTipoMateriale("bianco");
                // Intervento interno Febbraio 2014 per inviare la corretta iconcina nelle sintetiche per i TitoliUniformi NON MUSICALI
                data.setImageUrl("sintVuota.png");
            }
            String tipoAuthority = datiElemento.getTipoAuthority().toString();
            // Tipo Record !! NON DISPONIBILE Attualmente
            data.setTipoRecord("");
            data.setTipoRecordDesc("");


            // almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
            // con tipo legame 431 (A08V)
            // Natura la forzo
            String natura = "A";
//            data.setNatura(natura);
            if (datiElemento instanceof TitoloUniformeType) {
                TitoloUniformeType titoloUniformeType = (TitoloUniformeType) datiElemento;
                natura = titoloUniformeType.getNaturaTU();
            }

            data.setNatura(natura);



            // Livello Autorità
            String livelloAut = "";
            if (datiElemento.getLivelloAut().toString() != null) {
                livelloAut = datiElemento.getLivelloAut().toString();
            }

            String infogenerali = "";
//            infogenerali = bid + " " + natura + " " + livelloAut + " " + SinteticaTitoliView.HTML_NEW_LINE;
            infogenerali = bid + " " + titoloLocale + " " + natura + " " + livelloAut + " " + SinteticaTitoliView.HTML_NEW_LINE;

            String stringArrivoLegame = "";
            String nominativo = "";
            String titoloResponsabilita = "";
            // Descrizione e legami
            if (datiElemento instanceof TitoloUniformeType) {
                TitoloUniformeType titoloUniformeType = (TitoloUniformeType) datiElemento;
                // Ciclare per legameelementoAutType
                // Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
                // trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione
                // almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
                // con tipo legame 431 (A08V)
//                if (titoloUniformeType.getT230().getA_230().toString() != null) {
//                    titoloResponsabilita = titoloUniformeType.getT230().getA_230().toString();
//                }
                if (titoloUniformeType.getT230()  != null) {
                	titoloResponsabilita = titoloUniformeType.getT230().getA_230().toString();
                } else if (titoloUniformeType.getT231()  != null) {
                	titoloResponsabilita = titoloUniformeType.getT231().getA_231().toString();
                } else if (titoloUniformeType.getT431()  != null) {
                	titoloResponsabilita = titoloUniformeType.getT431().getA_431().toString();
                }
            }
            if (datiElemento instanceof TitoloUniformeMusicaType) {
                TitoloUniformeMusicaType titoloUniformeMusicaType = (TitoloUniformeMusicaType) datiElemento;
                // Ciclare per legameelementoAutType
                if (titoloUniformeMusicaType.getT230().getA_230().toString() != null) {
                    titoloResponsabilita = titoloUniformeMusicaType.getT230()
                            .getA_230().toString();
                }
            }

            if (elementoAut.getLegamiElementoAut() != null) {

                // Default a false.
                // Va a true quando viene trovato un legame
                // con TipoRespons=1, in quel caso viene aggiunta
                // una sola riga (-->).
                boolean isTipoRespons1 = false;
                // Contatore del nr. legami da visualizzare.
                // Se non ci sono legami con TipoRespons=1,
                // vengono aggiunte solo nr.2 righe (-->).
                int numLegami = 0;

                // ////////////// //
                // Ciclo per il controllo di eventuali
                // legami con TipoRespons=1
                for (int i = 0; i < elementoAut.getLegamiElementoAutCount(); i++) {

                    // "TipoRespons 1" trovato: ESCE DAL CICLO
                    if (isTipoRespons1)
                        break;

                    LegamiType legamiType = elementoAut.getLegamiElementoAut(i);

                    for (int k = 0; k < legamiType.getArrivoLegameCount(); k++) {

                        // "TipoRespons 1" trovato: ESCE DAL CICLO
                        if (isTipoRespons1)
                            break;

                        ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(k);

                        if (arrivoLegame.getLegameElementoAut() != null) {
                            LegameElementoAutType legameElemento = arrivoLegame.getLegameElementoAut();

                            // almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V
                            // con tipo legame 431 (A08V)
                            if (legameElemento.getTipoRespons() != null) {
                            	if (legameElemento.getTipoRespons().toString().equals("1")) {

	                                ElementAutType elementAutType = legameElemento.getElementoAutLegato();
	                                DatiElementoType datiElemento2 = elementAutType.getDatiElementoAut();
	                                datiElemento = elementAutType .getDatiElementoAut();
	                                if (datiElemento.getTipoAuthority().toString().equals("AU")) {

	                                    isTipoRespons1 = true;
	                                    stringArrivoLegame = stringArrivoLegame
	                                            + SinteticaTitoliView.HTML_NEW_LINE + "--> "
	                                            + legameElemento.getIdArrivo()
	                                                    .toString();
	                                    stringArrivoLegame = stringArrivoLegame
	                                            + " "
	                                            + legameElemento.getTipoRespons()
	                                                    .toString();

	                                    nominativo = utilityCastor.getNominativoDatiElemento(datiElemento);
	                                    stringArrivoLegame = stringArrivoLegame + " " + nominativo;
	                                }

                            	}// end if su tipoRespons==1
                            } else {
                            	ElementAutType elementAutType = legameElemento.getElementoAutLegato();
                                datiElemento = elementAutType .getDatiElementoAut();
                                if (datiElemento.getTipoAuthority().toString().equals("TU")) {

                                	stringArrivoLegame = stringArrivoLegame
                                			+ SinteticaTitoliView.HTML_NEW_LINE + "--> " + legameElemento.getIdArrivo().toString();

                                	if (datiElemento instanceof TitoloUniformeType) {
                                        TitoloUniformeType titoloUniformeType = (TitoloUniformeType) datiElemento;
                                        if (titoloUniformeType.getT231()  != null) {
                                        	stringArrivoLegame = stringArrivoLegame + " " +  titoloUniformeType.getT231().getA_231().toString();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // ////////////// //

                // Controlla gli altri eventuali legami solo se
                // non ne è stato trovato alcuno con TipoRespons=1
                if (!isTipoRespons1) {

                    for (int i = 0; i < elementoAut.getLegamiElementoAutCount(); i++) {

                        // Trovati 2 legami: ESCE DAL CICLO
                        if (numLegami == 2)
                            break;

                        LegamiType legamiType = elementoAut.getLegamiElementoAut(i);

                        for (int k = 0; k < legamiType.getArrivoLegameCount(); k++) {

                            // Trovati 2 legami: ESCE DAL CICLO
                            if (numLegami == 2)
                                break;

                            ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(k);

                            if (arrivoLegame.getLegameElementoAut() != null) {
                                LegameElementoAutType legameElemento = arrivoLegame.getLegameElementoAut();
                                ElementAutType elementAutType = legameElemento.getElementoAutLegato();
                                DatiElementoType datiElemento2 = elementAutType.getDatiElementoAut();
                                datiElemento = elementAutType.getDatiElementoAut();
                                if (datiElemento.getTipoAuthority().toString().equals("AU")) {
                                    stringArrivoLegame = stringArrivoLegame
                                            + SinteticaTitoliView.HTML_NEW_LINE + "--> "
                                            + legameElemento.getIdArrivo()
                                                    .toString();
                                    if (legameElemento.getTipoRespons() != null) {
                                        stringArrivoLegame = stringArrivoLegame
                                                + " "
                                                + legameElemento
                                                        .getTipoRespons()
                                                        .toString();
                                    }

                                    nominativo = utilityCastor.getNominativoDatiElemento(datiElemento);
                                    stringArrivoLegame = stringArrivoLegame + " " + nominativo;

                                    // Incrementa il contatore
                                    numLegami++;
                                }
                            }
                        }
                    }

                }// end if (!isTipoRespons1)

                for (int i = 0; i < elementoAut.getLegamiElementoAutCount(); i++) {

                    LegamiType legamiType = elementoAut.getLegamiElementoAut(i);

                    for (int k = 0; k < legamiType.getArrivoLegameCount(); k++) {

                        ArrivoLegame arrivoLegame = legamiType
                                .getArrivoLegame(k);

                        // LegamiDocumento.ArrivoLegame.LegameDoc
                        if (arrivoLegame.getLegameDoc() != null) {
                            LegameDocType legameDocType = arrivoLegame
                                    .getLegameDoc();

                            stringArrivoLegame = stringArrivoLegame + SinteticaTitoliView.HTML_NEW_LINE + "--> "
                                    + legameDocType.getIdArrivo();
                            DocumentoType documentoType2 = legameDocType
                                    .getDocumentoLegato();
                            DocumentoTypeChoice documentoTypeChoice2 = documentoType2
                                    .getDocumentoTypeChoice();
                            DatiDocType datiDocType2 = documentoTypeChoice2
                                    .getDatiDocumento();
                            GuidaDoc guidaDoc2 = datiDocType2.getGuida();

                            stringArrivoLegame = stringArrivoLegame + " "
                                    + legameDocType.getTipoLegame();

                            stringArrivoLegame = stringArrivoLegame + " "
                                    + datiDocType2.getNaturaDoc().toString();
                            C110 c110 = datiDocType2.getT110();

                            if (c110 != null) {
                                String A110 = c110.getA_110_0().toString();
                                stringArrivoLegame = stringArrivoLegame
                                        + " "
                                        + c110.getA_110_0().toString()
                                                .toString();
                            }
                            stringArrivoLegame = stringArrivoLegame
                                    + " "
                                    + utilityCampiTitolo
                                            .getTitoloResponsabilita(datiDocType2);
                        }// end legameDoc
                    }
                }
                descrizioneLegami = infogenerali + titoloResponsabilita
                	+ stringArrivoLegame;
                //data.setDescrizioneLegami(infogenerali + titoloResponsabilita);
                data.setDescrizioneLegami(titoloResponsabilita);

            } else {
            	descrizioneLegami = infogenerali + titoloResponsabilita;
                //data.setDescrizioneLegami(infogenerali + titoloResponsabilita);
                data.setDescrizioneLegami(titoloResponsabilita);
            }

            data.setLivelloAutorita(livelloAut);
            //data.setKeyBidDesc(data.getBid() + descrizioneLegami);
            //data.setTipoRecDescrizioneLegami(data.getTipoRecordDesc() + SinteticaTitoliView.HTML_NEW_LINE + descrizioneLegami);
            data.setTipoRecDescrizioneLegami(descrizioneLegami);

            data.setParametri("ParametriAuthority");
            data.setTipoAutority(tipoAuthority);
            if (tipoAuthority.equals("UM")) {
                data.setAlfaMateriale("U");
            } else {
                data.setAlfaMateriale("");
            }
        }
        return data;
    }

    public SinteticaTitoliView creaElementoLista(SbnOutputType sbnOutPut,
            int elementIndex, int progressivo, String visualNumSequenza) throws RemoteException {

        // List contenente la nuova riga alla tabella
        SinteticaTitoliView data = new SinteticaTitoliView();
        UtilityCampiTitolo utilityCampiTitolo = new UtilityCampiTitolo();
        String descrizioneLegami = "";
        String titoloLocale = "";
        // DATI MUSICALI
        String organicoSintetico = "  ";
        String tipoElaborazione = " ";
        String presentazione = " ";
        String datazione = " ";
        String designazioneMateriale = " ";
        // DA INSERIRE E LEGGERE
        String libretto = " ";
        // almaviva LEGGO DATI TROVATI
        DocumentoType documentoType = sbnOutPut.getDocumento(elementIndex);
        DocumentoTypeChoice documentoTypeChoice = documentoType
                .getDocumentoTypeChoice();
        DatiDocType datiDocType = documentoTypeChoice.getDatiDocumento();

        // Leggo DatiDocumento
        // " ", "N°", "BID", "Materiale", "Tipo Rec.", "Natura", "Descrizione e
        // legami", "Liv. Aut.", "Date" ,};
        if (datiDocType != null) {
            // ICONA della prima colonna della tabella
            data.setImageUrl("sintVuota.png");
            // PROGRESSIVO: Numero del elemento
            data.setProgressivo(progressivo);
            // data.addElement(new Integer(progressivo).toString());

            // Numero di elementi trovati
            data.setNumNotizie(sbnOutPut.getTotRighe());

            // BID
            String bid = datiDocType.getT001();
            data.setBid(bid.trim());

            if (datiDocType.getCondiviso() == null){
            	data.setFlagCondiviso(true);
            } else {
                if (datiDocType.getCondiviso().getType() == DatiDocTypeCondivisoType.N_TYPE){
                	data.setFlagCondiviso(false);
                	titoloLocale = "[loc]";
    			} else {
    				data.setFlagCondiviso(true);
    			}
            }

            //almaviva5_20080729
            data.setLocalizzato(datiDocType.getInfoLocBibCount() > 0 );

            // Tipo Materiale
            SbnMateriale sbnMateriale = datiDocType.getTipoMateriale();
            // TODO:verificare la gestione della immagine
            if (sbnMateriale.toString().equals("E")) {
                data.setTipoMateriale("antico");
                data.setImageUrl("sintAntico.gif");
            } else if (sbnMateriale.toString().equals("C")) {
                data.setTipoMateriale("cartografia");
                data.setImageUrl("sintCartografia.gif");
            //  almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
            } else if (sbnMateriale.toString().equals("H")) {
                data.setTipoMateriale("audiovisivo");
                data.setImageUrl("sintAudiovisivo.gif");
            } else if (sbnMateriale.toString().equals("L")) {
                data.setTipoMateriale("elettronico");
                data.setImageUrl("sintElettronico.png");

            } else if (sbnMateriale.toString().equals("G")) {
                data.setTipoMateriale("grafica");
                data.setImageUrl("sintGrafica.gif");
                if (datiDocType instanceof GraficoType) {
                    GraficoType graficoType = (GraficoType) datiDocType;
                    // LEGGO I DATI SPECIFICI DELLA MUSICA
                    if (graficoType.getT116() != null) {
                        if (graficoType.getT116().getA_116_0() != null) {
                            designazioneMateriale = graficoType.getT116()
                                    .getA_116_0();
                            designazioneMateriale =
                            	codici.cercaDescrizioneCodice(graficoType.getT116().getA_116_0().toString(),
                            			CodiciType.CODICE_MATERIALE_GRAFICO,
                            			CodiciRicercaType.RICERCA_CODICE_UNIMARC);
                        }
                    }
                }
            } else if (sbnMateriale.toString().equals("M")) {
                data.setTipoMateriale("moderno");
                data.setImageUrl("sintModerno.gif");
            } else if (sbnMateriale.toString().equals("U")) {
                data.setTipoMateriale("musica");
                data.setImageUrl("sintMusica.gif");
                if (datiDocType instanceof MusicaType) {
                    MusicaType musicaType = (MusicaType) datiDocType;
                    // LEGGO I DATI SPECIFICI DELLA MUSICA
                    if (musicaType.getT128() != null) {
                        if (musicaType.getT128().getD_128() != null) {
                            tipoElaborazione = musicaType.getT128().getD_128();
                            tipoElaborazione = codici.cercaDescrizioneCodice(musicaType.getT128().getD_128().toString(),
                            		CodiciType.CODICE_ELABORAZIONE,
                            		CodiciRicercaType.RICERCA_CODICE_UNIMARC);
                        }
                        if (musicaType.getT128().getB_128() != null) {
                            organicoSintetico = musicaType.getT128().getB_128();
                        }
                    }
                    if (musicaType.getT125() != null) {
                        if (musicaType.getT125().getA_125_0() != null) {
                            presentazione = musicaType.getT125().getA_125_0();
                            presentazione = codici.cercaDescrizioneCodice(musicaType.getT125().getA_125_0().toString(),
                            		CodiciType.CODICE_PRESENTAZIONE,
                            		CodiciRicercaType.RICERCA_CODICE_UNIMARC);
                        }
                        if (musicaType.getT125().getB_125() != null) {
                            libretto = musicaType.getT125().getB_125();
                            libretto = codici.cercaDescrizioneCodice(musicaType.getT125().getB_125().toString(),
                            		CodiciType.CODICE_TIPO_TESTO_LETTERARIO,
                            		CodiciRicercaType.RICERCA_CODICE_UNIMARC);
                            if (libretto == null) {
                                libretto = " ";
                            }
                        }

                    }
                    if (musicaType.getT923() != null) {
                        if (musicaType.getT923().getE_923() != null) {
                            datazione = musicaType.getT923().getE_923();
                            if (datazione == null) {
                                datazione = " ";
                            }

                        }
                    }

                }
            } else {
                data.setTipoMateriale("bianco");
            }
            // Tipo Record
            GuidaDoc guidaDoc = datiDocType.getGuida();
            String myTipoRecord = "";
            String myTipoRecordDesc = "";
            if (guidaDoc != null) {
                TipoRecord tipoRecord = guidaDoc.getTipoRecord();
                if (guidaDoc.getTipoRecord() != null) {
                    myTipoRecord = tipoRecord.toString();
                    // DECODIFICO IL CAMPO DELLA TABELLA TIPO RECORD
                    myTipoRecordDesc = codici.cercaDescrizioneCodice(myTipoRecord,
                    		CodiciType.CODICE_GENERE_MATERIALE_UNIMARC,
                    		CodiciRicercaType.RICERCA_CODICE_UNIMARC);
                    if (myTipoRecordDesc == null) {
                    	myTipoRecordDesc = "";
                    }
                    data.setTipoRecord(myTipoRecord);
                    data.setTipoRecordDesc(myTipoRecordDesc);
                } else {
                    data.setTipoRecord("");
                    data.setTipoRecordDesc(myTipoRecordDesc);
                }
            } else {
                data.setTipoRecord("");
                data.setTipoRecordDesc(myTipoRecordDesc);
            }
            // Natura
            String natura = "";
            if (datiDocType.getNaturaDoc() != null) {
                natura = datiDocType.getNaturaDoc().toString();
                data.setNatura(natura);
            } else {
                data.setNatura("");
            }


            // Livello Autorita
            String livelloAut = "";
            if (datiDocType.getLivelloAutDoc() != null) {
                livelloAut = datiDocType.getLivelloAutDoc().toString();
            }
            C100 c100 = datiDocType.getT100();
            // Data inserimento
            String dataIns = "";
            if (c100 != null){
                if (c100.getA_100_0() != null) {
                	dataIns = c100.getA_100_0().toString();
                    data.setDataIns(dataIns);
                } else {
                	data.setDataIns("");
                }
            } else {
            	data.setDataIns("");
            }

            // Tipo Data
            String tipodata = "";
            if (c100.getA_100_8() != null) {
                tipodata = c100.getA_100_8().toString();
            }
            // Data 1
            String data1 = "";
            if (c100.getA_100_9() != null) {
                data1 = c100.getA_100_9().toString();
//              Inizio Modifica almaviva2 30.11.2009 BUG MANTIS 3251 - si imposta la data separatamente per le eventuali fusioni
                data.setDataPubbl1Da(data1);
                if (data.getDataIns().equals("")) {
                	data.setDataIns(data1);
                }
            }
            // Data 2
            String data2 = "";
            if (c100.getA_100_13() != null) {
                data2 = c100.getA_100_13().toString();
            }
            String infogenerali = "";
            if ((datiDocType.getTipoMateriale().toString().equals("M")) || (datiDocType.getTipoMateriale().toString().equals("C"))) {
            	infogenerali = bid + " " + titoloLocale + " " + natura + " " + livelloAut + " " + data1 + " " +
            						myTipoRecordDesc + " " + SinteticaTitoliView.HTML_NEW_LINE;
            } else if ((datiDocType.getTipoMateriale().toString().equals("E"))) {
                infogenerali = bid + " " + titoloLocale + " " + natura + " " + livelloAut + " " + tipodata + " " + data1 + " " +
                					data2 + " " + myTipoRecordDesc + " " + SinteticaTitoliView.HTML_NEW_LINE;
            } else if ((datiDocType.getTipoMateriale().toString().equals("G"))) {
                infogenerali = bid + " " + titoloLocale + " " + natura + " " + livelloAut + " " + data1 + " " +
                					designazioneMateriale + " " + myTipoRecordDesc + " " + SinteticaTitoliView.HTML_NEW_LINE;
            // 	almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
            } else if ((datiDocType.getTipoMateriale().toString().equals("H"))) {
                infogenerali = bid + " " + titoloLocale + " " + natura + " " + livelloAut + " " +
                					data1 + " " + myTipoRecordDesc + " " + SinteticaTitoliView.HTML_NEW_LINE;
            } else if ((datiDocType.getTipoMateriale().toString().equals("L"))) {
                infogenerali = bid + " " + titoloLocale + " " + natura + " " + livelloAut + " " +
                					data1 + " " + myTipoRecordDesc + " " + SinteticaTitoliView.HTML_NEW_LINE;

            } else if ((datiDocType.getTipoMateriale().toString().equals("U"))) {
            	infogenerali = bid + " " + titoloLocale + " " + natura + " " + livelloAut + " " +
                        data1 + " " + libretto + " " + tipoElaborazione + " "
                        + organicoSintetico + " " + presentazione + " "
                        + datazione + " " + myTipoRecordDesc + " " + SinteticaTitoliView.HTML_NEW_LINE;
            } else {
                // DEFAULT
                infogenerali = bid + " " + titoloLocale + " " + natura + " " + livelloAut + " " +
                    data1 + " " + myTipoRecordDesc + " " + SinteticaTitoliView.HTML_NEW_LINE;

            }

            // Descrizione e legami
            String titoloResponsabilita = utilityCampiTitolo
                    .getTitoloResponsabilita(datiDocType);
            String areaPubblicazione = utilityCampiTitolo
                    .getAreaPubblicazione(datiDocType);
            // Leggo LegamiDocumento
            String stringIdPartenza = "";
            String stringArrivoLegame = "";
            String nominativo = "";

            if (documentoType.getLegamiDocumento() != null) {

                // LEGAMI A DOCUMENTO
                boolean isTipoRespons1 = false;
                int numLegami = 0;
                boolean thereAreAutoriLegati = false;

                for (int i = 0; i < documentoType.getLegamiDocumentoCount(); i++) {
                    if (isTipoRespons1)
                        break;

                    LegamiType legamiType = documentoType.getLegamiDocumento(i);
                    for (int k = 0; k < legamiType.getArrivoLegameCount(); k++) {
                        if (isTipoRespons1)
                            break;

                        ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(k);
                        if (arrivoLegame.getLegameElementoAut() != null) {
                            LegameElementoAutType legameElemento = arrivoLegame.getLegameElementoAut();
                            if (legameElemento.getTipoRespons() != null) {

                                // Ci sono autori legati
                                if (legameElemento.getElementoAutLegato().getDatiElementoAut()
                                        .getTipoAuthority().toString().equals("AU")) {

                                    thereAreAutoriLegati = true;
                                }

                                if (legameElemento.getTipoRespons().toString().equals("1")) {

                                    stringArrivoLegame = "";
                                    ElementAutType elementAutType = legameElemento.getElementoAutLegato();
                                    DatiElementoType datiElemento = elementAutType.getDatiElementoAut();
                                    datiElemento = elementAutType.getDatiElementoAut();

                                    if (datiElemento.getTipoAuthority().toString().equals("AU")) {
                                        isTipoRespons1 = true;

                                        stringArrivoLegame = stringArrivoLegame
                                                + SinteticaTitoliView.HTML_NEW_LINE + "--> "
                                                + legameElemento.getIdArrivo()
                                                        .toString();
                                        stringArrivoLegame = stringArrivoLegame
                                                + " "
                                                + legameElemento
                                                        .getTipoRespons()
                                                        .toString();

                                        nominativo = utilityCastor.getNominativoDatiElemento(datiElemento);
                                        stringArrivoLegame = stringArrivoLegame
                                                + " " + nominativo;
                                    }
                                }// end if TipoRespons==1
                            }// end if TipoRespons != null

                        }
                    }
                }
                // CONTROLLA GLI EVENTUALI ALTRI LEGAMI AD
                // AUTORE SOLO SE NON NE E' STATO TROVATO
                // ALCUNO CON TIPORESPONS==1
                if ((!isTipoRespons1) && (thereAreAutoriLegati)) {

                    for (int i = 0; i < documentoType.getLegamiDocumentoCount(); i++) {

                        // Trovati 2 legami: ESCE DAL CICLO
                        if (numLegami == 2)
                            break;

                        LegamiType legamiType = documentoType.getLegamiDocumento(i);

                        for (int k = 0; k < legamiType.getArrivoLegameCount(); k++) {
                            if (numLegami == 2)
                                break;

                            ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(k);

                            if (arrivoLegame.getLegameElementoAut() != null) {

                                LegameElementoAutType legameElemento = arrivoLegame.getLegameElementoAut();
                                ElementAutType elementAutType = legameElemento.getElementoAutLegato();
                                DatiElementoType datiElemento = elementAutType.getDatiElementoAut();
                                datiElemento = elementAutType.getDatiElementoAut();

                                if (datiElemento.getTipoAuthority().toString().equals("AU")) {
                                    stringArrivoLegame = stringArrivoLegame
                                            + SinteticaTitoliView.HTML_NEW_LINE + "--> "
                                            + legameElemento.getIdArrivo()
                                                    .toString();
                                    if (legameElemento.getTipoRespons() != null) {
                                        stringArrivoLegame = stringArrivoLegame
                                                + " "
                                                + legameElemento
                                                        .getTipoRespons()
                                                        .toString();
                                    }

                                    nominativo = utilityCastor.getNominativoDatiElemento(datiElemento);
                                    stringArrivoLegame = stringArrivoLegame  + " " + nominativo;

                                    // Incrementa il contatore
                                    numLegami++;

                                } else {
                                    stringArrivoLegame = "";
                                }

                            }// end legameElementoAut

                        }
                    }// end for esterno sui legami

                }// end if su TipoRespons e su thereAreAutoriLegati


                // ////////////////////////////////////////////////////////////////////
            	// Modifica almaviva2 04.08.2010 - Gestione periodici Inserito if per evitare la prospettazione dei legami
                // che vengono ora inviati per le S e che devono essere prospettati solo per W  e N;

            	// Modifica almaviva2 2010.10.01 BUG MANTIS 3919 - L'if sopra descritto inserito per periodici deve essere
                // fatto anche per le nature M (titoli collegati a Collana)

                if (natura.equals("W") || natura.equals("N") || natura.equals("M")) {
                    for (int i = 0; i < documentoType.getLegamiDocumentoCount(); i++) {

                        LegamiType legamiType = documentoType.getLegamiDocumento(i);

                        for (int k = 0; k < legamiType.getArrivoLegameCount(); k++) {

                            ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(k);

                            // LegamiDocumento.ArrivoLegame.LegameDoc
                            if (arrivoLegame.getLegameDoc() != null) {
                                LegameDocType legameDocType = arrivoLegame.getLegameDoc();

                                stringArrivoLegame = stringArrivoLegame + SinteticaTitoliView.HTML_NEW_LINE + "--> "
                                        + legameDocType.getIdArrivo();

                                DocumentoType documentoType2 = legameDocType.getDocumentoLegato();
                                DocumentoTypeChoice documentoTypeChoice2 = documentoType2.getDocumentoTypeChoice();
                                DatiDocType datiDocType2 = documentoTypeChoice2.getDatiDocumento();
                                GuidaDoc guidaDoc2 = datiDocType2.getGuida();


                            	// Inizio modifica almaviva2 2010.11.05 BUG Mantis 3971
                                // anche l'oggetto Documento legato al documento deve avere l'indicazione di [loc] de non condiviso
                                if (datiDocType2.getCondiviso() != null && datiDocType2.getCondiviso().getType() == DatiDocTypeCondivisoType.N_TYPE){
                                    	stringArrivoLegame = stringArrivoLegame + "[loc] ";
                                }
                				// fine modifica almaviva2 2010.11.05 BUG Mantis 3971


                                // Decodifica del Tipo Legame
                                int tipoLegameNumerico = 0;
                                try {
                                    tipoLegameNumerico = Integer.parseInt(legameDocType.getTipoLegame().toString());
                                } catch (NumberFormatException exx) {
                                }
                                String codiceLegameCompleto = datiDocType
                                        .getNaturaDoc().toString()
                                        + tipoLegameNumerico
                                        + datiDocType2.getNaturaDoc().toString();
                                codiceLegameCompleto = codici.cercaDescrizioneCodice(codiceLegameCompleto,
                                		CodiciType.CODICE_LEGAME_NATURA_TITOLO_TITOLO,
                                		CodiciRicercaType.RICERCA_CODICE_UNIMARC);
                                //codiceLegameCompleto = Codici.getDescrizioneCodiceTabella("LICR",codiceLegameCompleto);

                                stringArrivoLegame = stringArrivoLegame + " "
                                        + codiceLegameCompleto;
                                stringArrivoLegame = stringArrivoLegame + " "
                                        + datiDocType2.getNaturaDoc().toString();

                                C110 c110 = datiDocType2.getT110();

                                if (c110 != null) {
                                    String A110 = c110.getA_110_0().toString();
                                    stringArrivoLegame = stringArrivoLegame
                                            + " "
                                            + c110.getA_110_0().toString()
                                                    .toString();
                                }
                                stringArrivoLegame = stringArrivoLegame
                                        + " "
                                        + utilityCampiTitolo
                                                .getTitoloResponsabilita(datiDocType2);


                                if (visualNumSequenza.equals("SI")
                                		&& (tipoLegameNumerico == 410 || tipoLegameNumerico == 461)
                                		&& legameDocType.getSequenza() != null) {
                                	stringArrivoLegame = stringArrivoLegame + " [sequenza:" + legameDocType.getSequenza().trim() + "]";

                                }

                            }// end legameDoc

                        }
                    }// end for esterno sui legami
                }




                if (!titoloResponsabilita.equals("")) {
                	descrizioneLegami = infogenerali + titoloResponsabilita + ". - " + areaPubblicazione
                    + stringArrivoLegame;
                    data.setDescrizioneLegami(titoloResponsabilita);
                } else {
                	descrizioneLegami = infogenerali + titoloResponsabilita + stringArrivoLegame;
                	data.setDescrizioneLegami(titoloResponsabilita );
                }

            } else {
                if (!titoloResponsabilita.equals("")) {
                    // NESSUN LEGAME A DOCUMENTO
                	descrizioneLegami = infogenerali + titoloResponsabilita + ". - " + areaPubblicazione;
                    data.setDescrizioneLegami(titoloResponsabilita);
                } else {
                    // NESSUN LEGAME A DOCUMENTO
                	descrizioneLegami = infogenerali + titoloResponsabilita;
                    data.setDescrizioneLegami(titoloResponsabilita);
                }
            }

            data.setLivelloAutorita(livelloAut);
            //data.setKeyBidDesc(data.getBid() + descrizioneLegami);
            //data.setTipoRecDescrizioneLegami(data.getTipoRecordDesc() + SinteticaTitoliView.HTML_NEW_LINE + descrizioneLegami);
            data.setTipoRecDescrizioneLegami(descrizioneLegami);

            if (sbnMateriale.toString().equals(" ")) {
                data.setParametri("Parametri");
            } else {
                data.setParametri("ParametriDocumenti");
            }

            data.setTipoAutority("");
            data.setAlfaMateriale(sbnMateriale.toString());

        }// end if (datiDocType != null)

        // Leggo DatiTitAccesso
        if (documentoTypeChoice.getDatiTitAccesso() != null) {
            TitAccessoType titAccessoType = documentoTypeChoice
                    .getDatiTitAccesso();
            // Immagine
            data.setImageUrl("sintVuota.png");
            // PROGRESSIVO: Numero del elemento
            data.setProgressivo(progressivo);
            // BID
            String bid = titAccessoType.getT001();
            data.setBid(bid.trim());

            if (titAccessoType.getCondiviso() == null){
            	data.setFlagCondiviso(true);
            } else {
            	 if (titAccessoType.getCondiviso().getType() == TitAccessoTypeCondivisoType.N_TYPE){
            		titoloLocale = "[loc]";
                 	data.setFlagCondiviso(false);
     			} else {
     				data.setFlagCondiviso(true);
     			}
            }

            // Numero di elementi trovati
            data.setNumNotizie(sbnOutPut.getTotRighe());

            // tipo Materiale
            // FORZO A NULLA
            data.setTipoMateriale("bianco");
            // Tipo Record
            String myTipoRecord = "";
            data.setTipoRecord("");
            data.setTipoRecordDesc("");
            String natura = "";
            // Natura
            if (titAccessoType.getNaturaTitAccesso().toString() != null) {
                natura = titAccessoType.getNaturaTitAccesso().toString();
                data.setNatura(natura);
            } else {
                data.setNatura("");
            }
            // Livello Autorita
            String livelloAut = "";
            if (titAccessoType.getLivelloAut().toString() != null) {
                livelloAut = titAccessoType.getLivelloAut().toString();
            }

            // Data inserimento
            String data1 = "";
            data.setDataIns(data1);

//            String infogenerali = bid + " " + natura + " " + livelloAut + " " + SinteticaTitoliView.HTML_NEW_LINE;
            String infogenerali = bid + " " + titoloLocale + " " + natura + " " + livelloAut + " " + SinteticaTitoliView.HTML_NEW_LINE;

            String titoloResponsabilita="";
            // se i Dati sono di natura B leggo T454
         // Evolutiva SCHEMA 2.02 - Febbraio 2016 - Gli oggetti bibliografici di natura "B" hanno obbligatorietà sulla lingua
            if (titAccessoType.getNaturaTitAccesso().toString().equals("B")) {
                //C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT454();
            	C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT454A().getT200();
                titoloResponsabilita = utilityCampiTitolo.getTitoloResponsabilitaDatiTitAccesso(titAccessoType, c200);


            } else
            // se i Dati sono di natura D leggo T517
            if (titAccessoType.getNaturaTitAccesso().toString().equals("D")) {
                C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT517();
                titoloResponsabilita = utilityCampiTitolo
                        .getTitoloResponsabilitaDatiTitAccesso(titAccessoType,
                                c200);
            } else
            // se i Dati sono di natura P leggo T510
            if (titAccessoType.getNaturaTitAccesso().toString().equals("P")) {
                C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT510();
                titoloResponsabilita = utilityCampiTitolo
                        .getTitoloResponsabilitaDatiTitAccesso(titAccessoType,
                                c200);
            } else
            // se i Dati sono di natura T leggo T423
            if (titAccessoType.getNaturaTitAccesso().toString().equals("T")) {
                C200 c200 = titAccessoType.getTitAccessoTypeChoice().getT423()
                        .getT200();
                titoloResponsabilita = utilityCampiTitolo
                        .getTitoloResponsabilitaDatiTitAccesso(titAccessoType,
                                c200);
            }
            // almaviva TEST
            String areaPubblicazione = utilityCampiTitolo
                    .getAreaPubblicazione(datiDocType);

            String stringIdPartenza = "";
            String stringArrivoLegame = "";
            String nominativo = "";

            if (documentoType.getLegamiDocumento() != null) {

                // LEGAMI A DOCUMENTO

                // Default a false.
                // Va a true quando viene trovato un legame
                // con TipoRespons=1, in quel caso viene aggiunta
                // una sola riga (-->).
                boolean isTipoRespons1 = false;
                // Contatore del nr. legami da visualizzare.
                // Se non ci sono legami con TipoRespons=1,
                // vengono aggiunte solo nr.2 righe (-->).
                int numLegami = 0;

                // ////////////////
                // Ciclo per il controllo di eventuali
                // legami con TipoRespons=1
                for (int i = 0; i < documentoType.getLegamiDocumentoCount(); i++) {

                    // "TipoRespons 1" trovato: ESCE DAL CICLO
                    if (isTipoRespons1)
                        break;

                    LegamiType legamiType = documentoType.getLegamiDocumento(i);
                    for (int k = 0; k < legamiType.getArrivoLegameCount(); k++) {

                        // "TipoRespons 1" trovato: ESCE DAL CICLO
                        if (isTipoRespons1)
                            break;

                        ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(k);
                        if (arrivoLegame.getLegameElementoAut() != null) {
                            LegameElementoAutType legameElemento = arrivoLegame.getLegameElementoAut();

                            if (legameElemento.getTipoRespons() != null) {
                                if (legameElemento.getTipoRespons().toString().equals("1")) {

                                    isTipoRespons1 = true;
                                    stringArrivoLegame = "";
                                    ElementAutType elementAutType = legameElemento.getElementoAutLegato();
                                    DatiElementoType datiElemento = elementAutType.getDatiElementoAut();
                                    datiElemento = elementAutType.getDatiElementoAut();

                                    if (datiElemento.getTipoAuthority()
                                            .toString().equals("AU")) {
                                        stringArrivoLegame = stringArrivoLegame
                                                + SinteticaTitoliView.HTML_NEW_LINE + "--> "
                                                + legameElemento.getIdArrivo()
                                                        .toString();
                                        if (legameElemento.getTipoRespons() != null) {
                                            stringArrivoLegame = stringArrivoLegame
                                                    + " "
                                                    + legameElemento
                                                            .getTipoRespons()
                                                            .toString();
                                        }

                                        nominativo = utilityCastor.getNominativoDatiElemento(datiElemento);
                                        stringArrivoLegame = stringArrivoLegame + " " + nominativo;
                                    }
                                    if (datiElemento instanceof TitoloUniformeType) {
                                        TitoloUniformeType titoloUniformeType = (TitoloUniformeType) datiElemento;
                                        stringArrivoLegame = stringArrivoLegame
                                                + SinteticaTitoliView.HTML_NEW_LINE + "--> "
                                                + legameElemento.getIdArrivo()
                                                        .toString();
                                        if (legameElemento.getTipoRespons() != null) {
                                            stringArrivoLegame = stringArrivoLegame
                                                    + " "
                                                    + legameElemento.getTipoRespons().toString();
                                        }
                                        nominativo = titoloUniformeType.getT230().getA_230().toString();
                                        stringArrivoLegame = stringArrivoLegame
                                                + " " + nominativo;
                                    }
                                    if (datiElemento instanceof TitoloUniformeMusicaType) {
                                        TitoloUniformeMusicaType titoloUniformeMusicaType = (TitoloUniformeMusicaType) datiElemento;
                                        stringArrivoLegame = stringArrivoLegame
                                                + SinteticaTitoliView.HTML_NEW_LINE + "--> "
                                                + legameElemento.getIdArrivo()
                                                        .toString();
                                        if (legameElemento.getTipoRespons() != null) {
                                            stringArrivoLegame = stringArrivoLegame
                                                    + " "
                                                    + legameElemento.getTipoRespons().toString();
                                        }
                                        nominativo = titoloUniformeMusicaType.getT230().getA_230().toString();
                                        stringArrivoLegame = stringArrivoLegame
                                                + " " + nominativo;
                                    }
                                }
                            }
                        }
                    }
                }
                // ////////////////

                // Controlla gli altri eventuali legami solo se
                // non ne è stato trovato alcuno con TipoRespons=1
                if (!isTipoRespons1) {

                    for (int i = 0; i < documentoType.getLegamiDocumentoCount(); i++) {

                        // Trovati 2 legami: ESCE DAL CICLO
                        if (numLegami == 2)
                            break;

                        LegamiType legamiType = documentoType
                                .getLegamiDocumento(i);

                        for (int k = 0; k < legamiType.getArrivoLegameCount(); k++) {

                            // Trovati 2 legami: ESCE DAL CICLO
                            if (numLegami == 2)
                                break;

                            ArrivoLegame arrivoLegame = legamiType.getArrivoLegame(k);
                            if (arrivoLegame.getLegameTitAccesso() != null) {
                                stringArrivoLegame = "";
                            }

                            // LegamiDocumento.ArrivoLegame.LegameDoc
                            if (arrivoLegame.getLegameDoc() != null) {
                                LegameDocType legameDocType = arrivoLegame.getLegameDoc();
                                stringArrivoLegame = stringArrivoLegame
                                        + SinteticaTitoliView.HTML_NEW_LINE + "--> "
                                        + legameDocType.getIdArrivo();

                                stringArrivoLegame = stringArrivoLegame + " "
                                        + legameDocType.getTipoLegame();
                                DocumentoType documentoType2 = legameDocType.getDocumentoLegato();
                                DocumentoTypeChoice documentoTypeChoice2 = documentoType2.getDocumentoTypeChoice();
                                DatiDocType datiDocType2 = documentoTypeChoice2.getDatiDocumento();
                                GuidaDoc guidaDoc2 = datiDocType2.getGuida();
                                stringArrivoLegame = stringArrivoLegame + " "
                                        + datiDocType2.getTipoMateriale();
                                stringArrivoLegame = stringArrivoLegame
                                        + " "
                                        + guidaDoc2.getLivelloBibliografico().toString();
                                C110 c110 = datiDocType2.getT110();

                                if (c110 != null) {
                                    String A110 = c110.getA_110_0().toString();
                                    stringArrivoLegame = stringArrivoLegame
                                            + " "
                                            + c110.getA_110_0().toString();
                                }
                                stringArrivoLegame = stringArrivoLegame
                                        + " "
                                        + utilityCampiTitolo.getTitoloResponsabilita(datiDocType2);

                                // Incrementa il contatore
                                numLegami++;
                            }

                            // LegamiDocumento.ArrivoLegame.LegameElementoAut
                            if (arrivoLegame.getLegameElementoAut() != null) {
                                LegameElementoAutType legameElemento = arrivoLegame.getLegameElementoAut();
                                ElementAutType elementAutType = legameElemento.getElementoAutLegato();
                                DatiElementoType datiElemento = elementAutType.getDatiElementoAut();
                                datiElemento = elementAutType.getDatiElementoAut();

                                if (datiElemento.getTipoAuthority().toString().equals("AU")) {
                                    stringArrivoLegame = stringArrivoLegame
                                            + SinteticaTitoliView.HTML_NEW_LINE + "--> "
                                            + legameElemento.getIdArrivo().toString();
                                    if (legameElemento.getTipoRespons() != null) {
                                        stringArrivoLegame = stringArrivoLegame
                                                + " "
                                                + legameElemento.getTipoRespons().toString();
                                    }
                                    nominativo = utilityCastor.getNominativoDatiElemento(datiElemento);
                                    stringArrivoLegame = stringArrivoLegame
                                            + " " + nominativo;
                                }
                                if (datiElemento instanceof TitoloUniformeType) {
                                    TitoloUniformeType titoloUniformeType = (TitoloUniformeType) datiElemento;
                                    stringArrivoLegame = stringArrivoLegame
                                            + SinteticaTitoliView.HTML_NEW_LINE + "--> "
                                            + legameElemento.getIdArrivo().toString();
                                    if (legameElemento.getTipoRespons() != null) {
                                        stringArrivoLegame = stringArrivoLegame
                                                + " "
                                                + legameElemento.getTipoRespons().toString();
                                    }
                                    nominativo = titoloUniformeType.getT230().getA_230().toString();
                                    stringArrivoLegame = stringArrivoLegame
                                            + " " + nominativo;
                                }
                                if (datiElemento instanceof TitoloUniformeMusicaType) {
                                    TitoloUniformeMusicaType titoloUniformeMusicaType = (TitoloUniformeMusicaType) datiElemento;
                                    stringArrivoLegame = stringArrivoLegame
                                            + SinteticaTitoliView.HTML_NEW_LINE + "--> "
                                            + legameElemento.getIdArrivo().toString();
                                    if (legameElemento.getTipoRespons() != null) {
                                        stringArrivoLegame = stringArrivoLegame
                                                + " "
                                                + legameElemento.getTipoRespons().toString();
                                    }
                                    nominativo = titoloUniformeMusicaType.getT230().getA_230().toString();
                                    stringArrivoLegame = stringArrivoLegame + " " + nominativo;
                                }

                                // Incrementa il contatore
                                numLegami++;

                            }
                        }
                    }// end for esterno

                }// end if sul TipoRespons
                if (!titoloResponsabilita.equals("")) {
                    // NESSUN LEGAME A DOCUMENTO
                	descrizioneLegami = infogenerali + titoloResponsabilita + ". - " + areaPubblicazione
                		+ stringArrivoLegame;
                    data.setDescrizioneLegami(titoloResponsabilita);
                } else {
                    // NESSUN LEGAME A DOCUMENTO
                	descrizioneLegami = infogenerali + titoloResponsabilita + stringArrivoLegame;
                    data.setDescrizioneLegami(titoloResponsabilita);
                }

            } else {
                // NESSUN LEGAME A DOCUMENTO
                if (!titoloResponsabilita.equals("")) {
                    // NESSUN LEGAME A DOCUMENTO
                	descrizioneLegami = infogenerali
                    + titoloResponsabilita + ". - "
                    + areaPubblicazione;
                    data.setDescrizioneLegami(titoloResponsabilita);
                } else {
                    // NESSUN LEGAME A DOCUMENTO
                	descrizioneLegami = infogenerali + areaPubblicazione;
                    data.setDescrizioneLegami(areaPubblicazione);
                }

            }

            // END almaviva TEST

            data.setLivelloAutorita(livelloAut);
            data.setTipoRecDescrizioneLegami(descrizioneLegami);
            data.setParametri("Parametri");
            data.setTipoAutority("");
            data.setAlfaMateriale("");
        }

        return data;
    }

    public int getNumeroElementiDocumento(SbnOutputType sbnOutPut) {
        return sbnOutPut.getDocumentoCount();
    }

    public int getNumeroElementiAutority(SbnOutputType sbnOutPut) {
        return sbnOutPut.getElementoAutCount();
    }

}
