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

import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TimestampHash;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.iccu.sbn.ejb.model.unimarcmodel.AudiovisivoType;
import it.iccu.sbn.ejb.model.unimarcmodel.CreaType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModernoType;
import it.iccu.sbn.ejb.model.unimarcmodel.MusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * Classe TitoloValidaCreazione
 * <p>
 *
 * </p>
 * @author
 * @author
 *
 * @version 27-mag-03
 */
public class TitoloValidaCreazione extends TitoloValida {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2431137326453087961L;
	protected TitoloValidaLegami validaLegami = null;

    public TitoloValidaCreazione(CreaType datiType, boolean scheduled) throws IllegalArgumentException, InvocationTargetException, Exception {
        super();
        this.scheduled = scheduled;
        tipoControllo = datiType.getTipoControllo();
        elementAutType = datiType.getCreaTypeChoice().getElementoAut();
        documentoType = datiType.getCreaTypeChoice().getDocumento();
        LegamiType[] legami = null;
        if (documentoType != null) {
            legami = documentoType.getLegamiDocumento();
            statoRecord = documentoType.getStatoRecord();
            datiDoc = documentoType.getDocumentoTypeChoice().getDatiDocumento();
            if (datiDoc != null) {
                id = datiDoc.getT001();
                c200 = datiDoc.getT200();
            }
        } else if (elementAutType != null) {
            legami = elementAutType.getLegamiElementoAut();
            id = elementAutType.getDatiElementoAut().getT001();
            statoRecord = elementAutType.getDatiElementoAut().getStatoRecord();
        }

        validaLegami = new TitoloValidaLegami(id, legami);
    }

    /**
     * metodo di validazione per operazione di creazione autore:
     * - verificaEsistenzaID: se trovato ritorna diagnostico 'Luogo esistente'
     * se tipoControllo <> 'Conferma'
     * - verifica EsistenzaPerNome: se trovato/i ritorna la lista  sintetica dei
     * luoghi/luogo trovato al client
     * se non esistono luoghi simili ritorna ok al chiamante
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public boolean validaPerCrea(String utente, boolean ignoraId, TimestampHash timeHash,boolean _cattura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        elencoDiagnostico = null;
        Tb_titolo tb_titolo = null;
        if (!ignoraId) {
            tb_titolo = verificaEsistenzaID(id);
            //se tb_titolo è stato trovato significa che non posso crearlo nuovamente
            if (tb_titolo != null) {
                //ritorno un diagnostico
            	if(!_cattura)
            		throw new EccezioneSbnDiagnostico(3012, "Id di titolo già esistente nel DB");
            }
        }
        String cd_natura = "";
        if (documentoType != null) {
            if (datiDoc != null) {
                List listaTitoli;
                cd_natura = estraiNatura(datiDoc);
                SbnMateriale tipoMateriale = datiDoc.getTipoMateriale();
                if (cd_natura.equals("C")
                    && tipoMateriale != null
                    && tipoMateriale.getType() != SbnMateriale.valueOf(" ").getType())
                    throw new EccezioneSbnDiagnostico(3207, "Materiale non previsto");
                if (tipoMateriale == null || tipoMateriale.getType() == SbnMateriale.valueOf(" ").getType())
                    tipoMateriale = SbnMateriale.valueOf("M");
                // almaviva PER LA CATTURA NON DEVO EFFETTUARE I CONTROLLI
                verificaLivelloCreazione(utente, tipoMateriale.toString(), cd_natura, datiDoc.getLivelloAutDoc().toString(),_cattura);
                //Per il simile import non serve controllare il bid.
                if (!ignoraId && SbnSimile.SIMILEIMPORT.getType() != tipoControllo.getType())
                    validaBid(tipoMateriale,id, utente);

                // Inizio Intervento per Google3: l’interrogazione effettuata da un Polo non Abilitato ad uno specifico Materiale
                // non propone le specificità ma invia il DocType del moderno mantenendo il tipo Materiale corretto, quindi si deve
                // gestire l’inserimento nel DB di polo di documenti con tipo materiale ‘U’, ‘C’ o ‘G’ senza specificità
                // Inserito il Primo if per verificare le casistiche in oggetto

                // almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro

                if (datiDoc instanceof ModernoType &&
                	tipoMateriale.getType() != SbnMateriale.valueOf("M").getType()) {
                	   validaPerCrea(datiDoc, cd_natura,_cattura);
                } else if (tipoMateriale.getType() == SbnMateriale.valueOf("M").getType()) {
                    validaPerCrea(datiDoc, cd_natura,_cattura);
                } else if (tipoMateriale.getType() == SbnMateriale.valueOf("E").getType()) {
                    validaPerCreaAntico(datiDoc, cd_natura,_cattura);
                } else if (tipoMateriale.getType() == SbnMateriale.valueOf("U").getType()) {
                    validaPerCreaMusica(datiDoc, cd_natura,utente,_cattura);
                } else if (tipoMateriale.getType() == SbnMateriale.valueOf("G").getType()) {
                    validaPerCreaGrafica(datiDoc, cd_natura,utente,_cattura);
                } else if (tipoMateriale.getType() == SbnMateriale.valueOf("C").getType()) {
                    validaPerCreaCartografia(datiDoc, cd_natura,utente,_cattura);
                } else if (tipoMateriale.getType() == SbnMateriale.valueOf("H").getType()) {
                	// Intervento settembre 2015 : quando il Polo è abilitatao alla sola Musica Indice manda Tipo Materiale H
                	// ma type Musica: si inserisce il controllo
                	 if (datiDoc instanceof AudiovisivoType) {
                		 validaPerCreaAudiovisivo(datiDoc, cd_natura,utente,_cattura);
                	 } else  if (datiDoc instanceof MusicaType) {
                		 validaPerCreaMusica(datiDoc, cd_natura,utente,_cattura);
                	 } else {
                		 validaPerCrea(datiDoc, cd_natura,_cattura);
                	 }
                	// validaPerCreaAudiovisivo(datiDoc, cd_natura,utente,_cattura);

                } else if (tipoMateriale.getType() == SbnMateriale.valueOf("L").getType()) {
                    validaPerCreaElettronico(datiDoc, cd_natura,utente,_cattura);
                }
                if (c200 != null && c200.getA_200Count() > 0) {
                    if (cd_natura.equals("W")) {
                        if (c200.getA_200(0).indexOf("*") > 0)
                            throw new EccezioneSbnDiagnostico(3080, "Asterisco presente");
                    } else {
                        if (SbnSimile.CONFERMA.getType() != tipoControllo.getType()) {
                            listaTitoli = cercaTitoliSimili(null,cd_natura,0);
                            if (listaTitoli.size() != 0) {
                                //se lista è vuota significa che non esistono autori simili
                                elencoDiagnostico = listaTitoli;
                                return false;
                            }
                        } else {
                            //listaTitoli = cercaTitoliSimiliConferma(null, null,cd_natura);
                        }
                    }
                }
                //Ora valida i legami.
                String tp_record = null;
                if (datiDoc.getGuida() != null && datiDoc.getGuida().getTipoRecord() != null)
                    tp_record = datiDoc.getGuida().getTipoRecord().toString();
                validaLegami.validaLegamiDocumentoCrea(
                    utente,
                    timeHash,
                    id,
                    cd_natura,
                    tp_record,
                    tipoMateriale,
                    _cattura);
            }
        } else {
            throw new EccezioneSbnDiagnostico(3103, "Dati non consistenti");
        }
        return true;
    }

    private void validaBid(SbnMateriale materiale, String bid, String user) throws EccezioneSbnDiagnostico {
        if (!verificaBid(bid, user))
            throw new EccezioneSbnDiagnostico(3902, "Bid non valido");
        // evolutive Schema 2.01-ottobre 2015 almaviva2 - Vengono eliminati i controlli sulla data e sul quarto carattere
        //dell'identificativo (bid) per il materiale 'E' così da consentire di fatto il cambio materiale (M-->E oppure E-->M)
//        if (materiale.getType() == SbnMateriale.VALUE_1.getType())
//            if (bid.charAt(3) != 'E')
//                throw new EccezioneSbnDiagnostico(3902, "Bid non valido");
    }

}
