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
package it.iccu.sbn.SbnMarcFactory.factory.bibliografica;

import it.iccu.sbn.SbnMarcFactory.exception.SbnMarcException;
import it.iccu.sbn.SbnMarcFactory.factory.FactorySbn;
import it.iccu.sbn.ejb.model.unimarcmodel.FondeType;
import it.iccu.sbn.ejb.model.unimarcmodel.SBNMarc;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnMessageType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnOggetto;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnRequestType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResponseType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnResultType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoReturnVO;
import it.iccu.sbn.ejb.vo.common.AreaDatiAccorpamentoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaTabellaOggettiDaCatturareVO;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;
import it.iccu.sbn.util.bibliografica.FusioneDelegateFactory;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Funzionalità di creazione/sintesi di alberi DOM Castor
 * per l'accorpamento e trascinamento legami a titoli.
 * L'accorpamento dei titoli necessita inoltre del tipo materiale.
 * @author Corrado Di Pietro
 */
public class SbnGestioneAccorpamentoDao extends DaoManager {

	private static Logger log = Logger.getLogger(SbnGestioneAccorpamentoDao.class);

	private FactorySbn indice;
	private FactorySbn polo;
	private SbnUserType user;


	public SbnGestioneAccorpamentoDao(FactorySbn indice, FactorySbn polo, SbnUserType user) {
		this.user = user;
		this.indice = indice;
		this.polo = polo;
	}

    /**
     * Invia una richiesta di accorpamento al server
     * @param idElementoEliminato id dell'oggetto che sarà eliminato
     * @param idElementoAccorpante id dell'oggetto accorpante
     * @param tipoAuthority tipo dei due elementi di authority accorpanti, null se si tratta di un titolo
     * @param tipoMateriale tipo del materiale degli eventualei due titoli da accorpare
     *
     * @param frame frame root per la messaggistica
     */
    public AreaDatiAccorpamentoReturnVO richiestaAccorpamento(AreaDatiAccorpamentoVO areaDatiPassAccorpamento){

    	AreaDatiAccorpamentoReturnVO areaDatiAccorpamentoReturnVO = new AreaDatiAccorpamentoReturnVO();

      	// Non ci sono ID di titoli collegati, per cui si tratta di una richiesta di accorpamento
    	// e non di spostamento legami
    	areaDatiPassAccorpamento.setIdTitoliLegati(null);

    	/* Elenco operazioni da effettuare per fondere due oggetti:
    	 	0. almaviva5_20100614 #3797 il bid da eliminare deve essere marcato come solo locale (non condiviso)
    	 	..... SI .... ma solo nel casi Fusione Massiva !
    	 	1. cattura dell'oggetto di arrivo della fusione dall'Indice in Polo: in questo modo
    	 		se la notizia è assente dal Polo viene correttamente catturata e viene inviata
    	 		la localizzazione in indice; se è già presente viene effettuato	un allineamento della notizia
    	 	2. fusione in Indice dei due oggetti
    	 	3. fusione in Polo dei due oggetti
    	*/

    	// punto 1.
		SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
		AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = new AreaDatiVariazioneReturnVO();

		if (areaDatiPassAccorpamento.getLivelloBaseDati().equals("I")) {
	    	if (areaDatiPassAccorpamento.getTipoAuthority() == null
	    			|| areaDatiPassAccorpamento.getTipoAuthority().toString().equals("TU")
	    			|| areaDatiPassAccorpamento.getTipoAuthority().toString().equals("UM")) {

	    		// Inizio Modifica 22.07.2010 almaviva2 per Bug MANTIS 3797 - si identifica la chiamata per fusione massiva perchè
	    		// il bid da eliminare deve essere marcato come solo locale (non condiviso)
	    		if (areaDatiPassAccorpamento.isChiamataFusioneMassiva()) {
		        	// punto 0.
		        	// richiesta analitica di polo, se il bid é condiviso va scartato
		        	Boolean condiviso = testCondivisioneTitolo(areaDatiPassAccorpamento.getIdElementoEliminato());
		        	if (condiviso == null) {
		        		areaDatiAccorpamentoReturnVO.setCodErr("9999");
		        		areaDatiAccorpamentoReturnVO.setTestoProtocollo("Errore durante la fase di lettura da Indice/Polo");
		        		return areaDatiAccorpamentoReturnVO;
		        	}
		        	if (condiviso) {	// da scartare
		        		areaDatiAccorpamentoReturnVO.setCodErr("9999");
		        		areaDatiAccorpamentoReturnVO.setTestoProtocollo("Impossibile eliminare bid '" + areaDatiPassAccorpamento.getIdElementoEliminato() +
		        				"' poiché risulta condiviso con l'Indice");
		        		return areaDatiAccorpamentoReturnVO;
		        	}
	    		}
	    		// Fine Modifica 22.07.2010 almaviva2 per Bug MANTIS 3797

	    		AreaTabellaOggettiDaCatturareVO areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
	    		areaTabellaOggettiDaCatturareVO.setIdPadre(areaDatiPassAccorpamento.getIdElementoAccorpante());
	    		if (areaDatiPassAccorpamento.getTipoAuthority() != null) {
	    			areaTabellaOggettiDaCatturareVO.setTipoAuthority(areaDatiPassAccorpamento.getTipoAuthority().toString());
	    		}
	    		if (areaDatiPassAccorpamento.getTipoMateriale() != null) {
	    			areaTabellaOggettiDaCatturareVO.setTipoMateriale(areaDatiPassAccorpamento.getTipoMateriale().toString());
	    		}

	    		// BUG INTERNO Marzo 2015: almaviva2
	    		// Viene impostato il flag che indica la provenienza dall'allineamento per tutte le successive operazioni
	    		areaTabellaOggettiDaCatturareVO.setProvenienzaAllineamento(areaDatiPassAccorpamento.isChiamataAllineamento());


	    		areaDatiVariazioneReturnVO = gestioneAllAuthority.catturaReticolo(areaTabellaOggettiDaCatturareVO);
	        	if (!areaDatiVariazioneReturnVO.getCodErr().equals("0000") &&
	        			!areaDatiVariazioneReturnVO.getCodErr().equals("")) {
	        		areaDatiAccorpamentoReturnVO.setCodErr(areaDatiVariazioneReturnVO.getCodErr());
	        		areaDatiAccorpamentoReturnVO.setTestoProtocollo(areaDatiVariazioneReturnVO.getTestoProtocollo());
	        		return areaDatiAccorpamentoReturnVO;
	    		}
	    	} else if (areaDatiPassAccorpamento.getTipoAuthority().toString().equals("AU")) {
	    		AreaTabellaOggettiDaCatturareVO areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
	    		areaTabellaOggettiDaCatturareVO.setIdPadre(areaDatiPassAccorpamento.getIdElementoAccorpante());
    			areaTabellaOggettiDaCatturareVO.setTipoAuthority("AU");

	    		areaDatiVariazioneReturnVO = gestioneAllAuthority.catturaAutore(areaTabellaOggettiDaCatturareVO);
	        	if (!areaDatiVariazioneReturnVO.getCodErr().equals("0000") &&
	        			!areaDatiVariazioneReturnVO.getCodErr().equals("")) {
	        		areaDatiAccorpamentoReturnVO.setCodErr(areaDatiVariazioneReturnVO.getCodErr());
	        		areaDatiAccorpamentoReturnVO.setTestoProtocollo(areaDatiVariazioneReturnVO.getTestoProtocollo());
	        		return areaDatiAccorpamentoReturnVO;
	    		}

	    	} else if (areaDatiPassAccorpamento.getTipoAuthority().toString().equals("MA")) {
	    		AreaTabellaOggettiDaCatturareVO areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
	    		areaTabellaOggettiDaCatturareVO.setIdPadre(areaDatiPassAccorpamento.getIdElementoAccorpante());
    			areaTabellaOggettiDaCatturareVO.setTipoAuthority("MA");

	    		areaDatiVariazioneReturnVO = gestioneAllAuthority.catturaMarca(areaTabellaOggettiDaCatturareVO);
	        	if (!areaDatiVariazioneReturnVO.getCodErr().equals("0000") &&
	        			!areaDatiVariazioneReturnVO.getCodErr().equals("")) {
	        		areaDatiAccorpamentoReturnVO.setCodErr(areaDatiVariazioneReturnVO.getCodErr());
	        		areaDatiAccorpamentoReturnVO.setTestoProtocollo(areaDatiVariazioneReturnVO.getTestoProtocollo());
	        		return areaDatiAccorpamentoReturnVO;
	    		}

	    	} else if (areaDatiPassAccorpamento.getTipoAuthority().toString().equals("LU")) {
	    		AreaTabellaOggettiDaCatturareVO areaTabellaOggettiDaCatturareVO = new AreaTabellaOggettiDaCatturareVO();
	    		areaTabellaOggettiDaCatturareVO.setIdPadre(areaDatiPassAccorpamento.getIdElementoAccorpante());
    			areaTabellaOggettiDaCatturareVO.setTipoAuthority("LU");

	    		areaDatiVariazioneReturnVO = gestioneAllAuthority.catturaLuogo(areaTabellaOggettiDaCatturareVO);
	        	if (!areaDatiVariazioneReturnVO.getCodErr().equals("0000") &&
	        			!areaDatiVariazioneReturnVO.getCodErr().equals("")) {
	        		areaDatiAccorpamentoReturnVO.setCodErr(areaDatiVariazioneReturnVO.getCodErr());
	        		areaDatiAccorpamentoReturnVO.setTestoProtocollo(areaDatiVariazioneReturnVO.getTestoProtocollo());
	        		return areaDatiAccorpamentoReturnVO;
	    		}

	    	}

	    	if (areaDatiPassAccorpamento.isChiamataAllineamento()) {
		    	areaDatiPassAccorpamento.setLivelloBaseDati("I");
		    	areaDatiAccorpamentoReturnVO = this.richiestaSpostamentoLegami(areaDatiPassAccorpamento);
	    		return areaDatiAccorpamentoReturnVO;
	    	}

	    	areaDatiPassAccorpamento.setLivelloBaseDati("I");
	    	areaDatiAccorpamentoReturnVO = this.richiestaSpostamentoLegami(areaDatiPassAccorpamento);
	    	if (!areaDatiAccorpamentoReturnVO.getCodErr().equals("0000") &&
	    			!areaDatiAccorpamentoReturnVO.getCodErr().equals("")) {
	    		return areaDatiAccorpamentoReturnVO;
			}
	    	areaDatiPassAccorpamento.setLivelloBaseDati("P");
	    	areaDatiAccorpamentoReturnVO = this.richiestaSpostamentoLegami(areaDatiPassAccorpamento);
	    	if (!areaDatiAccorpamentoReturnVO.getCodErr().equals("0000") &&
	    			!areaDatiAccorpamentoReturnVO.getCodErr().equals("")) {
	    		return areaDatiAccorpamentoReturnVO;
			}
		} else if (areaDatiPassAccorpamento.getLivelloBaseDati().equals("P")) {
			areaDatiPassAccorpamento.setLivelloBaseDati("P");
	    	areaDatiAccorpamentoReturnVO = this.richiestaSpostamentoLegami(areaDatiPassAccorpamento);
	    	if (!areaDatiAccorpamentoReturnVO.getCodErr().equals("0000") &&
	    			!areaDatiAccorpamentoReturnVO.getCodErr().equals("")) {
	    		return areaDatiAccorpamentoReturnVO;
			}
		}

    	return areaDatiAccorpamentoReturnVO;
    }

	private Boolean testCondivisioneTitolo(String bid) {
	  	try {
	  		Criteria c = getCurrentSession().createCriteria(Tb_titolo.class);
	  		c.add(Restrictions.idEq(bid));
	  		c.add(Restrictions.ne("fl_canc", 'S'));
	  		Tb_titolo t = (Tb_titolo) c.uniqueResult();
	  		if (t == null)
	  			return null;
    		//Tb_titolo t = (Tb_titolo) loadNoLazy(getCurrentSession(), Tb_titolo.class, bid);
    		char flCondiviso = t.getFl_condiviso();
			return ValidazioneDati.in(flCondiviso, 's', 'S');

    	} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

	/**
	 * Invia una richiesta di accorpamento/trascinamento legami al server.
	 * Se idTitoliLegati!=null si tratta di un trascinamento di legami verso titoli.
	 * @param idElementoPartenza id dell'oggetto che sarà eliminato
	 * @param idElementoArrivo id dell'oggetto accorpante
     * @param tipoAuthority tipo dei due elementi di authority accorpanti, null se si tratta di un titolo
	 * @param tipoMateriale tipo del materiale dei due titoli da accorpare
	 * @param idTitoliLegati ID dei titoli collegati da spostare
	 * @param frame frame root per la messaggistica
	 */
	public AreaDatiAccorpamentoReturnVO richiestaSpostamentoLegami(AreaDatiAccorpamentoVO areaDatiPass) {


		AreaDatiAccorpamentoReturnVO areaDatiAccorpamentoReturnVO = new AreaDatiAccorpamentoReturnVO();
		areaDatiAccorpamentoReturnVO.setCodErr("0000");

		// Spostamento legami Area Documento Fisico tramite chiamata a oggetti esterni solo nel caso di Documenti


		//if (areaDatiPass.isChiamataAllineamento() || areaDatiPass.getLivelloBaseDati().equals("I")) {
		if (areaDatiPass.isChiamataAllineamento() || areaDatiPass.getLivelloBaseDati().equals("P")) {
			if ( areaDatiPass.getTipoAuthority() == null ){
				String uteVar = this.user.getBiblioteca() + this.user.getUserId();
				try {
					// Spostamento legami Area Documento Fisico tramite chiamata a oggetti esterni solo nel caso di Documenti
					FusioneDelegateFactory.getDelegate().richiestaSpostamentoLegami(areaDatiPass.getIdElementoEliminato(),
							areaDatiPass.getIdElementoAccorpante(), uteVar);

				} catch (Exception e) {
					// almaviva2 15.10.2012 Bug Mantis esercizio 5140/5144: se l'oggetto partenza di fusione non esiste sulla
					// base dati di polo (assenza fisica non solo cancellazione logica) si prosegue spegnendo il flag allineamento
					// senza uscire
					if (!e.getMessage().contains("Oggetto non trovato")) {
						log.error("", e);
						areaDatiAccorpamentoReturnVO.setCodErr("9999");
						areaDatiAccorpamentoReturnVO.setTestoProtocollo("ERROR >>" + e.getMessage());
						return areaDatiAccorpamentoReturnVO;
					}
				}

			}
		}

		// Spostamento legami Bibliografici e fusione dei due oggetti tramite richiesta al protocollo SbnMarc
		SBNMarc sbnRisposta = null;
		try {
			SbnMessageType		sbnmessage	= new SbnMessageType();
			SbnRequestType		sbnrequest	= new SbnRequestType();
			FondeType 			fondeType	= new FondeType();
			fondeType.setIdArrivo(areaDatiPass.getIdElementoAccorpante());
			fondeType.setIdPartenza(areaDatiPass.getIdElementoEliminato());

			SbnOggetto sbnOggetto = new SbnOggetto();
			fondeType.setTipoOggetto(sbnOggetto);
			// L'accorpamento/trascinamento è tra due elementi di authority
			if ( areaDatiPass.getTipoAuthority() != null ){
				sbnOggetto.setTipoAuthority(areaDatiPass.getTipoAuthority());
			} else{
			// L'accorpamento/trascinamento è tra due titoli, cerco il tipo materiale:
				SbnMateriale tM = SbnMateriale.valueOf(" ");//tipoMateriale);
				sbnOggetto.setTipoMateriale(tM);
			}
			// Se idLegami != null si tratta di uno spostamento di legami
			if ( areaDatiPass.getIdTitoliLegati() != null ){
				// Aggancio degli ID dei titoli collegati da trascinare
				fondeType.setSpostaID(areaDatiPass.getIdTitoliLegati());
			}

			sbnrequest.setFonde(fondeType);
			sbnmessage.setSbnRequest(sbnrequest);

			if (areaDatiPass.isChiamataAllineamento()) {

				// 19.03.2015 Inizio almaviva2: richiesta  almaviva: "in caso di Fusione Massiva" Batch si deve comunque
				// chiamare la funzionalita di copiaLocalizzazPoloSuIndice perchè si devono importare in Indice le localizzazioni
				// che sono state create in Polo
				if (areaDatiPass.isChiamataFusioneMassiva()) {
					// Chiamata alla nuova funzione per copiare la localizzazione di Polo in Indice con tutti gli attributi
					SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);
					if (areaDatiPass.getTipoAuthority() == null) {
						// Intervento interno ottobre 2015 almaviva2 che allarga le funzionalità del metodo
						// copiaLocalizzazPoloSuIndice e lo rende generalizzato copiando le localizzazioni da un oggetto
						// ad un altro da una base dati ad un altra - La funzione viene rinominata copiaLocalizzazDocumento
						String origineArrivo = "PoloIndice";
						String codStringaErrore = gestioneAllAuthority.copiaLocalizzazDocumento(
								areaDatiPass.getIdElementoEliminato(),
								areaDatiPass.getIdElementoAccorpante(),
								areaDatiPass.getTipoMateriale(),
								this.user.getBiblioteca(),
								origineArrivo);
						if (!codStringaErrore.substring(0,4).equals("0000")) {
							areaDatiAccorpamentoReturnVO.setCodErr(codStringaErrore.substring(0,4));
							if (codStringaErrore.length() > 4) {
								areaDatiAccorpamentoReturnVO.setTestoProtocollo(codStringaErrore.substring(5));
							}
							return areaDatiAccorpamentoReturnVO;
						}
					}
				}

				this.polo.setMessage(sbnmessage,this.user);
				sbnRisposta = this.polo.eseguiRichiestaServer();
				if (sbnRisposta == null) {
					areaDatiAccorpamentoReturnVO.setCodErr("noServerPol");
					return areaDatiAccorpamentoReturnVO;
				}
				// 19.03.2015 Fine almaviva2: richiesta  almaviva

			} else {
				if (areaDatiPass.getLivelloBaseDati().equals("I")) {
					this.indice.setMessage(sbnmessage,this.user);
					sbnRisposta = this.indice.eseguiRichiestaServer();
					if (sbnRisposta == null) {
						areaDatiAccorpamentoReturnVO.setCodErr("noServerInd");
						return areaDatiAccorpamentoReturnVO;
					}
				} else {
					// Inizio Modifica almaviva2 27.08.2010 - BUG MANTIS 3871
					// Chiamata alla nuova funzione per copiare la localizzazione di Polo in Indice con tutti gli attributi
					SbnGestioneAllAuthorityDao gestioneAllAuthority = new SbnGestioneAllAuthorityDao(indice, polo, user);

					// ULTERIORE modifica almaviva2 BUG MANTIS 3871 del 25.10.2010
					// si inviano sia il bid di partenza che serve per interrogare in Polo che il bid di arrivo che serve per spedire
					// in Indice i dati di localizzazione copiati; nel caso di catalogazione in Indice di elemento locale il bid di partenza
					// e di arrivo coincidono
					// Modifica almaviva2 del 28.10.2010 BUG Mantis 3954 il copia local vale solo per i Documenti;
					if (areaDatiPass.getTipoAuthority() == null) {
						// Intervento interno ottobre 2015 almaviva2 che allarga le funzionalità del metodo
						// copiaLocalizzazPoloSuIndice e lo rende generalizzato copiando le localizzazioni da un oggetto
						// ad un altro da una base dati ad un altra - La funzione viene rinominata copiaLocalizzazDocumento
						String origineArrivo = "PoloIndice";
						String codStringaErrore = gestioneAllAuthority.copiaLocalizzazDocumento(
								areaDatiPass.getIdElementoEliminato(),
								areaDatiPass.getIdElementoAccorpante(),
								areaDatiPass.getTipoMateriale(),
								this.user.getBiblioteca(),
								origineArrivo);
						if (!codStringaErrore.substring(0,4).equals("0000")) {
							areaDatiAccorpamentoReturnVO.setCodErr(codStringaErrore.substring(0,4));
							if (codStringaErrore.length() > 4) {
								areaDatiAccorpamentoReturnVO.setTestoProtocollo(codStringaErrore.substring(5));
							}
							return areaDatiAccorpamentoReturnVO;
						}
					}
					// Fine Modifica almaviva2 27.08.2010 - BUG MANTIS 3871

					this.polo.setMessage(sbnmessage,this.user);
					sbnRisposta = this.polo.eseguiRichiestaServer();
					if (sbnRisposta == null) {
						areaDatiAccorpamentoReturnVO.setCodErr("noServerPol");
						return areaDatiAccorpamentoReturnVO;
					}
				}
			}

			SbnMessageType sbnMessage = sbnRisposta.getSbnMessage();
			SbnResponseType sbnResponse = sbnMessage.getSbnResponse();
			SbnResultType sbnResult = sbnResponse.getSbnResult();

			areaDatiAccorpamentoReturnVO.setCodErr(sbnResult.getEsito());
			areaDatiAccorpamentoReturnVO.setTestoProtocollo(sbnResult.getTestoEsito());

		} catch (SbnMarcException ve) {
			areaDatiAccorpamentoReturnVO.setCodErr("9999");
			areaDatiAccorpamentoReturnVO.setTestoProtocollo("ERROR >>" + ve.getMessage());
		} catch (IllegalArgumentException ie) {
			areaDatiAccorpamentoReturnVO.setCodErr("9999");
			areaDatiAccorpamentoReturnVO.setTestoProtocollo("ERROR >>" + ie.getMessage());
		} catch (Exception e) {
			areaDatiAccorpamentoReturnVO.setCodErr("9999");
			areaDatiAccorpamentoReturnVO.setTestoProtocollo("ERROR >>" + e.getMessage());
		}

		return areaDatiAccorpamentoReturnVO;

	}

}
