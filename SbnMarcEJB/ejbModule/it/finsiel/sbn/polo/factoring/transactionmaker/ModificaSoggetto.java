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
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.FormatoErrore;
import it.finsiel.sbn.polo.factoring.base.FormatoSoggetto;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ResourceLoader;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Descrittore;
import it.finsiel.sbn.polo.oggetti.Soggetto;
import it.finsiel.sbn.polo.oggetti.SoggettoDescrittore;
import it.finsiel.sbn.polo.oggetti.estensione.DescrittoreValida;
import it.finsiel.sbn.polo.oggetti.estensione.SoggettoValida;
import it.finsiel.sbn.polo.orm.Tb_descrittore;
import it.finsiel.sbn.polo.orm.Tb_soggetto;
import it.finsiel.sbn.polo.orm.Tr_sog_des;
import it.finsiel.sbn.util.CodiciAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.A250;
import it.iccu.sbn.ejb.model.unimarcmodel.A600;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.LegamiType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModificaType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOutputType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseTypeChoice;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SoggettoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.DatiElementoTypeCondivisoType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOperazione;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoOutput;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ModificaSoggetto extends ModificaElementoAutFactoring {

	private SbnTipoOutput 		_tipoOut;
	private ElementAutType 	_elementoAut;
	private DatiElementoType 	_datiElementoAut;
	private String 			_T001 = null;

	private ModificaType 		_modificaType;
	private StatoRecord 		_statoRecord;
	private String 			_t005;
	private SbnLivello 		_livelloAut;
	private LegamiType[] 		_arrivoLegame;
	private A250 				_t250;
	private Tb_soggetto 		_soggettoResponse;
    private Tb_descrittore      _descrittoreResponse;
    private List<Tb_soggetto> _vettoreDiSimili = new ArrayList<Tb_soggetto>();
    private SbnSimile           _tipoControllo;

    private String  _condiviso;
    private A600    _t600;
    private String  _t601;
    private String  _t602;
    private int _idErroreSimile;

	public ModificaSoggetto(SBNMarc sbnmarcObj) {
		super(sbnmarcObj);
		_modificaType = sbnmarcObj.getSbnMessage().getSbnRequest().getModifica();
		_elementoAut =  _modificaType.getElementoAut();
		_datiElementoAut = _elementoAut.getDatiElementoAut();
        _tipoControllo = _modificaType.getTipoControllo();
		_T001 = _datiElementoAut.getT001();
		_livelloAut = _datiElementoAut.getLivelloAut();
		_arrivoLegame = _modificaType.getElementoAut().getLegamiElementoAut();
		_t005 = _datiElementoAut.getT005();
		_t250 = ((SoggettoType)_datiElementoAut).getT250();
		_t600 = ((SoggettoType)_datiElementoAut).getT600();
		_statoRecord = ((SoggettoType)_datiElementoAut).getStatoRecord();

        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
        if (datiElementoAut.getCondiviso() == null ){
        	_condiviso = "n";
        }
        else if ((datiElementoAut.getCondiviso().getType() == DatiElementoTypeCondivisoType.S_TYPE)){
        	_condiviso = "s";
        }
        else if ((datiElementoAut.getCondiviso().getType() == DatiElementoTypeCondivisoType.N_TYPE)){
        	_condiviso = "n";
        }
        // gestione campi aggiuntivi Nota soggetto e catt_sogg
        // se il campo è vuoto verifico che non sono in cattura
        // se sono in cattura setto di default 1 su catt_sogg altrimanti mando
        // in errore perchè il campo è obbligatorio
        if (_t600 != null){
			if (_t600.getA_601() != null){
				_t601 = _t600.getA_601();
			}
			if (_t600.getA_602() != null){
				_t602 = _t600.getA_602();
			}
		}
		if ((_t601 == null) && (_cattura)){
			_t601 = "1";

		}
        // IL TAG CONDIVISO é OPZIONALE QUINDI FACCIO UN TEST PRIMA DI ASSEGNARLO
//        if (datiElementoAut.getCondiviso() != null && (datiElementoAut.getCondiviso().equals(DatiElementoTypeCondivisoType.S)) ) {
//        	_condiviso = datiElementoAut.getCondiviso().toString();
//        }else{
//        	_condiviso = "n";
//        }
	}
	protected void executeModifica()
	throws IllegalArgumentException, InvocationTargetException, Exception {
		SoggettoValida sv = new SoggettoValida(_livelloAut.toString());
		sv.validaCatt_sogg_Nota(_t601,_t602,_cattura);

		Tb_soggetto tb_soggetto = sv.validaPerModifica(getCdUtente(),_T001,_t005,_cattura);
		//almaviva5_20091117
		String cd = Decodificatore.getCd_unimarc("Tb_soggetto", "cd_soggettario", tb_soggetto.getCD_SOGGETTARIO().trim());
		sv.controllaVettoreParametriSemantici(cd, this.cd_utente);

        if ( (_statoRecord != null) && (_statoRecord.getType() != StatoRecord.C_TYPE) && (_arrivoLegame != null))
            throw new EccezioneSbnDiagnostico(3090,"non è stata inoltrata alcuna richiesta di modifica");

        if (_statoRecord != null && _statoRecord.getType() == StatoRecord.C_TYPE) {
            if (tb_soggetto != null) {
            	String edizione = _t250.getEdizione() != null ? _t250.getEdizione().toString() : null;
                Soggetto soggetto = new Soggetto();
    			String[] vettoreDescrittori = SoggettoValida.preparaDescrittori(_t250);
                boolean dsModified = !ValidazioneDati.equals(tb_soggetto.getDS_SOGGETTO(), FormatoSoggetto.componiSoggetto(_t250));
                boolean edModified = !ValidazioneDati.equals(tb_soggetto.getCD_EDIZIONE(), edizione);
				if (dsModified || edModified) {
                    if (_tipoControllo.getType() == SbnSimile.SIMILE_TYPE || _tipoControllo.getType() == SbnSimile.SIMILEIMPORT_TYPE)
						_vettoreDiSimili = soggetto.cercaSoggettiSimili(_T001, _t250, vettoreDescrittori);
                    else if (_tipoControllo.getType() == SbnSimile.CONFERMA_TYPE)
                        _vettoreDiSimili = sv.verificaSimiliConferma(_T001, _t250);

                }
                int size = ValidazioneDati.size(_vettoreDiSimili);
				if (size == 0) {
        			//può essere cambiato solamente il livello del soggetto
        			soggetto.updateSoggetto(tb_soggetto,_t250,_livelloAut, getCdUtente(),_t601,_t602,_condiviso);
        			SoggettoDescrittore soggettoDescrittore = new SoggettoDescrittore();

        			soggettoDescrittore.leggiLegami(_T001, _t250.getEdizione(), getCdUtente() );

        			sv.controllaDescrittori(_T001, _t250, _t601, _condiviso, vettoreDescrittori, getCdUtente());

        			_soggettoResponse = soggetto.cercaSoggettoPerId(_T001);
                }
				//trovato un simile con edizione diversa
                if (size > 0) {
                	Tb_soggetto simile = ValidazioneDati.first(_vettoreDiSimili);
                	if (!ValidazioneDati.equals(edizione, simile.getCD_EDIZIONE()) )
                		_idErroreSimile = 3344;
                	else
    	                //trovati simili
   	                	_idErroreSimile = 3004;
                }
            }
        }
        // almaviva TEST PER CAPIRE SE DEVO SOLO INSERIRE IL DESCRITTORE MANUALE
        if (ValidazioneDati.isFilled(_arrivoLegame) ) {
            Soggetto soggetto = new Soggetto();
            inserisciLegamiSoggettoDescrittore(_arrivoLegame);
            _soggettoResponse = soggetto.cercaSoggettoPerId(_T001);
        }

        object_response = formattaOutput();
	}

	public void inserisciLegamiSoggettoDescrittore(LegamiType[] _arrivoLegame)
			throws IllegalArgumentException, IndexOutOfBoundsException,
			InvocationTargetException, InfrastructureException, Exception {
		DescrittoreValida dv = new DescrittoreValida();
		for (int i = 0; i < _arrivoLegame.length; i++) {
			if (dv.validaPerCreaLegameSoggettoDescrittore(_elementoAut))
				for (int j = 0; j < _arrivoLegame[i].getArrivoLegame().length; j++) {
					String idArrivo = _arrivoLegame[i].getArrivoLegame(j).getLegameElementoAut().getIdArrivo();
					SoggettoDescrittore sd = new SoggettoDescrittore();
					Tr_sog_des tr_sog_des = sd.controllaEsistenzaLegame(_T001, idArrivo);
					switch (_arrivoLegame[i].getTipoOperazione().getType()) {
					case SbnTipoOperazione.CREA_TYPE:
						if (tr_sog_des == null)
							//legame manuale
							sd.inserisciLegame(
									_T001,
									idArrivo,
									"M",
									ResourceLoader.getPropertyInteger("POSIZIONE_DESCRITTORE_MANUALE"),
									getCdUtente());
						else
							throw new EccezioneSbnDiagnostico(3030);	//legame già esistente
						break;

					case SbnTipoOperazione.CANCELLA_TYPE:
						sd.cancellaLegame(_T001, idArrivo, getCdUtente());
						break;
					}
				}
		}
	}
    /*
     *
                <Modifica tipoControllo="Conferma">
                    <ElementoAut>
                        <DatiElementoAut tipoAuthority="SO" livelloAut="97" statoRecord="c" xsi:type="SoggettoType" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                            <T001>SBNC000684</T001>
                            <T005>20070115140658.0</T005>
                        </DatiElementoAut>
                        <LegamiElementoAut tipoOperazione="Crea">
                            <idPartenza>SBNC000684</idPartenza>
                            <ArrivoLegame>
                                    <LegameElementoAut tipoAuthority="DE" tipoLegame="931">
                                    <idArrivo>SBND001543</idArrivo>
                                </LegameElementoAut>
                            </ArrivoLegame>
                        </LegamiElementoAut>
                    </ElementoAut>
                </Modifica>
            </SbnRequest>
        </SbnMessage>
    </SBNMarc> */


	private SBNMarc formattaOutput() throws IllegalArgumentException,
			InvocationTargetException, Exception {
		SBNMarc sbnmarc = new SBNMarc();
		SbnMessageType message = new SbnMessageType();
		SbnResponseType response = new SbnResponseType();
		SbnResultType result = new SbnResultType();
		SbnResponseTypeChoice responseChoice = new SbnResponseTypeChoice();
		SbnOutputType output = new SbnOutputType();
		result.setEsito("0000");
		if (ValidazioneDati.isFilled(_vettoreDiSimili))
			result = FormatoErrore.buildSbnResult(_idErroreSimile);
		else
			result.setTestoEsito("OK");
		sbnmarc.setSbnMessage(message);
		sbnmarc.setSbnUser(user_object);
		sbnmarc.setSchemaVersion(schemaVersion);
		message.setSbnResponse(response);
		response.setSbnResult(result);
		response.setSbnResponseTypeChoice(responseChoice);
		responseChoice.setSbnOutput(output);
		int size = 1;
		if (_vettoreDiSimili.size() > 1)
			size = _vettoreDiSimili.size();
		FormatoSoggetto formatoSoggetto = new FormatoSoggetto();
		LegamiType[] legamiType;
		output.setNumPrimo(1);
		output.setTotRighe(1);
		if (_vettoreDiSimili.size() > 0) {
			output.setTotRighe(_vettoreDiSimili.size());
			for (int i = 0; i < size; i++) {
				Tb_soggetto tb_soggetto = new Tb_soggetto();
				tb_soggetto = _vettoreDiSimili.get(i);
				ElementAutType elemento = new ElementAutType();
				int num_tit_coll_bib = formatoSoggetto.cercaNum_tit_coll_bib(tb_soggetto.getCID(), user_object);
				int num_tit_coll = formatoSoggetto.cercaNum_tit_coll_unique(tb_soggetto.getCID());
		    	Descrittore d = new Descrittore();
		    	List dd = d.cercaDescrittorePerSoggetto(tb_soggetto.getCID());
				elemento.setDatiElementoAut(formatoSoggetto.formattaSoggettoPerLista(tb_soggetto, num_tit_coll_bib, num_tit_coll, dd));
				output.addElementoAut(elemento);
			}
		} else if (_tipoControllo.getType() != SbnSimile.SIMILEIMPORT_TYPE) {
			SbnTipoOutput _tipoOut = SbnTipoOutput.VALUE_0;
			ElementAutType elemento = new ElementAutType();
			int num_tit_coll_bib = formatoSoggetto.cercaNum_tit_coll_bib(_soggettoResponse.getCID(), user_object);
			int num_tit_coll = formatoSoggetto.cercaNum_tit_coll_unique(_soggettoResponse.getCID());
	    	Descrittore d = new Descrittore();
	    	List dd = d.cercaDescrittorePerSoggetto(_soggettoResponse.getCID());
			elemento.setDatiElementoAut(formatoSoggetto.formattaSoggetto(_soggettoResponse, _tipoOut, num_tit_coll_bib, num_tit_coll, dd));
			output.addElementoAut(elemento);
			if (_tipoOut.getType() == SbnTipoOutput.VALUE_0.getType()) {
				legamiType = formatoSoggetto.formattaSoggettoConLegamiDescrittore(_soggettoResponse);
				if (legamiType != null)
					elemento.setLegamiElementoAut(legamiType);
			}
		}

		output.setTipoOutput(SbnTipoOutput.VALUE_0);
		return sbnmarc;
	}

    public String getIdAttivitaSt(){
        return CodiciAttivita.getIstance().MODIFICA_SOGGETTO_1265;
    }

}
