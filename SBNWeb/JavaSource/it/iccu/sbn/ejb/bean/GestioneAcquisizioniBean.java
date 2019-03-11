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
package it.iccu.sbn.ejb.bean;

import it.iccu.sbn.command.CommandInvokeVO;
import it.iccu.sbn.command.CommandResultVO;
import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.SbnBusinessSessionBean;
import it.iccu.sbn.ejb.domain.acquisizioni.Acquisizioni;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneMail;
import it.iccu.sbn.ejb.domain.documentofisico.Inventario;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ResourceNotFoundException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.remote.GestioneAcquisizioni;
import it.iccu.sbn.ejb.vo.acquisizioni.BilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.BuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.CambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneBOVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ConfigurazioneORDVO;
import it.iccu.sbn.ejb.vo.acquisizioni.DocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.FornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.GaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBilancioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppBuoniOrdineVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppCambioVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppComunicazioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppDocumentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFatturaVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppGaraVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOffertaFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppOrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSpeseVO;
import it.iccu.sbn.ejb.vo.acquisizioni.ListaSuppSuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OffertaFornitoreVO;
import it.iccu.sbn.ejb.vo.acquisizioni.OrdiniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.RicercaTitCollEditoriVO;
import it.iccu.sbn.ejb.vo.acquisizioni.RigheVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SezioneVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StampaBuoniVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaInventariOrdVO;
import it.iccu.sbn.ejb.vo.acquisizioni.StrutturaProfiloVO;
import it.iccu.sbn.ejb.vo.acquisizioni.SuggerimentoVO;
import it.iccu.sbn.ejb.vo.acquisizioni.TitoloACQVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.AreaDatiVariazioneReturnVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiLegameTitoloVO;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.AreaDatiPassaggioInterrogazioneTitoloReturnVO;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.hibernate.exception.DataException;

public class GestioneAcquisizioniBean extends SbnBusinessSessionBean implements GestioneAcquisizioni {


	private static final long serialVersionUID = 8181560747224732577L;


	private static Logger logger = Logger.getLogger(GestioneAcquisizioni.class);


    private Acquisizioni acquisizioni;

	private Inventario inventario;

	private it.iccu.sbn.ejb.domain.bibliografica.Interrogazione interrogazione;

	private AmministrazioneMail amministrazioneMail;


    public void ejbCreate() {
        logger.info("creato ejb");
        try {
    		this.acquisizioni = DomainEJBFactory.getInstance().getAcquisizioni();

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

    public AmministrazioneMail getAmministrazioneMailBean() throws EJBException, ValidationException {

    	if (amministrazioneMail != null)
    		return amministrazioneMail;
    	try{

			this.amministrazioneMail = DomainEJBFactory.getInstance().getAmministrazioneMail();

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        } catch (DataException e) {
            throw new EJBException(e);
		} catch (ValidationException e) {
      	  //logger.error("Errore in getBibliteche",e);
      	  throw e;
            //throw new EJBException(e);
		} catch (ApplicationException e) {
            throw new EJBException(e);
        } catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
        }

		return amministrazioneMail;
    }

    public Inventario getInventarioBean() throws EJBException, ValidationException {

    	if (inventario != null)
    		return inventario;
    	try{
    		this.inventario = DomainEJBFactory.getInstance().getInventario();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        } catch (DataException e) {
            throw new EJBException(e);
		} catch (ValidationException e) {
      	  //logger.error("Errore in getBibliteche",e);
      	  throw e;
            //throw new EJBException(e);
		} catch (ApplicationException e) {
            throw new EJBException(e);
        } catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
        }

		return inventario;
    }



	public boolean inserisciCambio(CambioVO cambio)  throws EJBException, ValidationException {
		boolean valRitorno = false;
		try {
			//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
			valRitorno =  this.acquisizioni.inserisciCambio(cambio);

        } catch (DataException e) {
            throw new EJBException(e);
		} catch (ValidationException e) {
      	  //logger.error("Errore in getBibliteche",e);
      	  throw e;
            //throw new EJBException(e);
		} catch (ApplicationException e) {
            throw new EJBException(e);
        } catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
        }

		return valRitorno;
	}

	public boolean inserisciCambioHib(CambioVO cambio)  throws EJBException, ValidationException {
		boolean valRitorno = false;
		try {
			//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
			valRitorno =  this.acquisizioni.inserisciCambioHib(cambio);
        } catch (DataException e) {
            throw new EJBException(e);
		} catch (ValidationException e) {
      	  //logger.error("Errore in getBibliteche",e);
      	  throw e;
            //throw new EJBException(e);
		} catch (ApplicationException e) {
            throw new EJBException(e);
        } catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
        }
		return valRitorno;
	}

	public List<CambioVO> getRicercaListaCambi(ListaSuppCambioVO ricercaCambi) throws EJBException, ValidationException
	 {
		List<CambioVO> elenco = null;
       try {

    	   //AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
       	elenco = this.acquisizioni.getRicercaListaCambi(ricercaCambi);
       } catch (ResourceNotFoundException e) {
           throw new EJBException(e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
           throw new EJBException(e);
		} catch (ValidationException e) {
//			logger.error("Errore in getBibliteche",e);
           throw e;
           //throw new EJBException(e);

		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}

		return elenco;
	}

	public List<CambioVO> getRicercaListaCambiHib(ListaSuppCambioVO ricercaCambi) throws  EJBException, ValidationException
	 {
		List<CambioVO> elenco = null;
      try {
    	// checkTicket(ricercaCambi.getTicket());
   	   //AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
    	  elenco = this.acquisizioni.getRicercaListaCambiHib(ricercaCambi);

      } catch (ResourceNotFoundException e) {
          throw new EJBException(e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
          throw new EJBException(e);
		} catch (ValidationException e) {
//			logger.error("Errore in getBibliteche",e);
          throw e;
          //throw new EJBException(e);

		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}

		return elenco;
	}

	public List<SezioneVO> getRicercaListaSezioniHib(ListaSuppSezioneVO ricercaSezioni) throws  EJBException, ValidationException
	 {
		List<SezioneVO> elenco = null;
     try {

  	   //AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
   	  elenco = this.acquisizioni.getRicercaListaSezioniHib(ricercaSezioni);

     } catch (ResourceNotFoundException e) {
         throw new EJBException(e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
         throw new EJBException(e);
		} catch (ValidationException e) {
//			logger.error("Errore in getBibliteche",e);
         throw e;
         //throw new EJBException(e);

		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}

		return elenco;
	}

	public boolean  cancellaSezioneHib(SezioneVO sezione) throws EJBException {
		boolean valRitorno = false;

		try {
			//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
			valRitorno = this.acquisizioni.cancellaSezioneHib(sezione);

	    } catch (DataException e) {
	        throw new EJBException(e);
	    } catch (ApplicationException e) {
	        throw new EJBException(e);

	    } catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}

			return valRitorno;
		}

		public boolean  modificaSezioneHib(SezioneVO sezione) throws EJBException, ValidationException {
		boolean valRitorno = false;

		try {
			//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
			valRitorno = this.acquisizioni.modificaSezioneHib(sezione);

		} catch (DataException e) {
		    throw new EJBException(e);
		} catch (ValidationException e) {
	  	  //logger.error("Errore in getBibliteche",e);
	  	  throw e;
	        //throw new EJBException(e);
		} catch (ApplicationException e) {
		    throw new EJBException(e);
		} 	catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
	    }

	    return valRitorno;
		}


		public boolean inserisciSezioneHib(SezioneVO sezione)  throws EJBException, ValidationException {
			boolean valRitorno = false;
			try {
				//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
				valRitorno =  this.acquisizioni.inserisciSezioneHib(sezione);
	        } catch (DataException e) {
	            throw new EJBException(e);
			} catch (ValidationException e) {
	      	  //logger.error("Errore in getBibliteche",e);
	      	  throw e;
	            //throw new EJBException(e);
			} catch (ApplicationException e) {
	            throw new EJBException(e);
	        } catch (RemoteException e) {
//				logger.error("Errore in getBibliteche",e);
				throw new EJBException(e);
	        }
			return valRitorno;
		}


	public boolean  cancellaCambio(CambioVO cambio) throws EJBException {
	boolean valRitorno = false;

	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.cancellaCambio(cambio);

    } catch (DataException e) {
        throw new EJBException(e);
    } catch (ApplicationException e) {
        throw new EJBException(e);

    } catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);
	}

		return valRitorno;
	}

	public boolean  modificaCambio(CambioVO cambio) throws EJBException, ValidationException {
	boolean valRitorno = false;

	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.modificaCambio(cambio);

	} catch (DataException e) {
	    throw new EJBException(e);
	} catch (ValidationException e) {
  	  //logger.error("Errore in getBibliteche",e);
  	  throw e;
        //throw new EJBException(e);
	} catch (ApplicationException e) {
	    throw new EJBException(e);
	} 	catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);
    }

    return valRitorno;
	}

	public boolean  cancellaCambioHib(CambioVO cambio) throws EJBException, ValidationException  {
		boolean valRitorno = false;

		try {
			//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
			valRitorno = this.acquisizioni.cancellaCambioHib(cambio);

	    } catch (DataException e) {
	        throw new EJBException(e);
		} catch (ValidationException e) {
		  	  //logger.error("Errore in getBibliteche",e);
		  	  throw e;
	    } catch (ApplicationException e) {
	        throw new EJBException(e);
	    } catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}

			return valRitorno;
		}

		public boolean  modificaCambioHib(CambioVO cambio) throws EJBException, ValidationException {
		boolean valRitorno = false;

		try {
			//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
			valRitorno = this.acquisizioni.modificaCambioHib(cambio);

		} catch (DataException e) {
		    throw new EJBException(e);
		} catch (ValidationException e) {
	  	  //logger.error("Errore in getBibliteche",e);
	  	  throw e;
	        //throw new EJBException(e);
		} catch (ApplicationException e) {
		    throw new EJBException(e);
		} 	catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
	    }

	    return valRitorno;
		}

	public List getListaOrdini() throws EJBException
	 {
		List elenco = null;
       try {
       	//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
       	elenco = this.acquisizioni.getListaOrdini();
       } catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
           throw new EJBException(e);
       } catch (ResourceNotFoundException e) {
           throw new EJBException(e);
       } catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}

		return elenco;
	}




	public List<OrdiniVO> getRicercaListaOrdini(ListaSuppOrdiniVO ricercaOrdini) throws EJBException, ValidationException
	 {
		List<OrdiniVO> elenco = null;
      try {
      	//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
      	elenco = this.acquisizioni.getRicercaListaOrdini(ricercaOrdini);
      } catch (ResourceNotFoundException e) {
          throw new EJBException(e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
          throw new EJBException(e);
		} catch (ValidationException e) {
//			logger.error("Errore in getBibliteche",e);
          throw e;
          //throw new EJBException(e);
		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);

		}

		return elenco;
	}

	public List<FornitoreVO> getRicercaListaFornitori(ListaSuppFornitoreVO ricercaFornitori) throws EJBException, ValidationException
	 {
		List<FornitoreVO> elenco = null;
     try {
     	//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
     	elenco = this.acquisizioni.getRicercaListaFornitori(ricercaFornitori);
     } catch (ResourceNotFoundException e) {
         throw new EJBException(e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
         throw new EJBException(e);

		} catch (ValidationException e) {
//			logger.error("Errore in getBibliteche",e);
         throw e;
         //throw new EJBException(e);
		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);

		}

		return elenco;
	}


	// Evolutiva Ba1 - Editori almaviva2 Novembre 2012
	// Inserito nuovo metodo per la ricerca dei titoli collegati in modo esplicito o implicito all'editore selezionato
	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getRicercaTitCollEditori(RicercaTitCollEditoriVO ricercaTitCollEditoriVO, String ticket)
			throws EJBException, ValidationException	 {

		List elenco = null;

	    try {
	    	elenco = this.acquisizioni.getRicercaTitCollEditori(ricercaTitCollEditoriVO, ticket);
		    } catch (ResourceNotFoundException e) {
		        throw new EJBException(e);
			} catch (ApplicationException e) {
		        throw new EJBException(e);
			} catch (ValidationException e) {
		        throw e;
			} catch (RemoteException e) {
				throw new EJBException(e);
		}
		int elencoSize = elenco.size();

		if (elencoSize == 0) {
			AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
			areaDatiPassReturn.setCodErr("3001");
			areaDatiPassReturn.setTotRighe(0);
			areaDatiPassReturn.setNumNotizie(0);
			return areaDatiPassReturn;
		}


		List lista = null;
		try {
			lista = this.acquisizioni.getAreeIsbdListaBid(elenco);
	    } catch (DataException e) {
	        throw new EJBException(e);
		} catch (ApplicationException e) {
	        throw new EJBException(e);
		} catch (ValidationException e) {
	    	  throw e;
		} catch (RemoteException e) {
			throw new EJBException(e);
		}

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();

		areaDatiPassReturn.setListaSintetica(lista);
		areaDatiPassReturn.setCodErr("0000");
		areaDatiPassReturn.setTotRighe(lista.size());
		areaDatiPassReturn.setNumNotizie(lista.size());
		areaDatiPassReturn.setLivelloTrovato("P");
		return areaDatiPassReturn;
	}

	public AreaDatiPassaggioInterrogazioneTitoloReturnVO getRicercaEditCollTitolo(RicercaTitCollEditoriVO ricercaTitCollEditoriVO, String ticket)
		throws EJBException, ValidationException	 {

		List elenco = null;

		try {
			elenco = this.acquisizioni.getRicercaEditCollTitolo(ricercaTitCollEditoriVO, ticket);
		    } catch (ResourceNotFoundException e) {
		        throw new EJBException(e);
			} catch (ApplicationException e) {
		        throw new EJBException(e);
			} catch (ValidationException e) {
		        throw e;
			} catch (RemoteException e) {
				throw new EJBException(e);
		}
		int elencoSize = elenco.size();

		if (elencoSize == 0) {
			AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();
			areaDatiPassReturn.setCodErr("3001");
			areaDatiPassReturn.setTotRighe(0);
			areaDatiPassReturn.setNumNotizie(0);
			return areaDatiPassReturn;
		}

		AreaDatiPassaggioInterrogazioneTitoloReturnVO areaDatiPassReturn = new AreaDatiPassaggioInterrogazioneTitoloReturnVO();

		areaDatiPassReturn.setListaSintetica(elenco);
		areaDatiPassReturn.setCodErr("0000");
		areaDatiPassReturn.setTotRighe(elenco.size());
		areaDatiPassReturn.setNumNotizie(elenco.size());

		return areaDatiPassReturn;
	}


	public AreaDatiVariazioneReturnVO gestioneLegameTitEdit(AreaDatiLegameTitoloVO areaDatiLegameTitoloVO, String ticket) throws RemoteException, ValidationException {

	AreaDatiVariazioneReturnVO areaDatiVariazioneReturnVO = new AreaDatiVariazioneReturnVO();
	try {
		areaDatiVariazioneReturnVO = this.acquisizioni.gestioneLegameTitEdit(areaDatiLegameTitoloVO, ticket);

	    } catch (DataException e) {
	        throw new EJBException(e);
		} catch (ApplicationException e) {
	        throw new EJBException(e);
		} catch (ValidationException e) {
	    	  throw e;
		} catch (RemoteException e) {
			throw new EJBException(e);
		}
		return areaDatiVariazioneReturnVO;
	}

	public List<BilancioVO> getRicercaListaBilanci(ListaSuppBilancioVO ricercaBilanci) throws EJBException, ValidationException
	 {
		List<BilancioVO> elenco = null;
    try {
    	//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
    	elenco = this.acquisizioni.getRicercaListaBilanci(ricercaBilanci);
    } catch (ResourceNotFoundException e) {
        throw new EJBException(e);
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (ValidationException e) {
        throw e;
		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);

		}
		return elenco;
	}
	public List<FatturaVO> getRicercaListaFatture(ListaSuppFatturaVO ricercaFatture) throws EJBException, ValidationException
	 {
		List<FatturaVO> elenco = null;
   try {
   	//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
   	elenco = this.acquisizioni.getRicercaListaFatture(ricercaFatture);
   } catch (ResourceNotFoundException e) {
       throw new EJBException(e);
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}
		return elenco;
	}

	public List<SezioneVO> getRicercaListaSezioni(ListaSuppSezioneVO ricercaSezioni) throws EJBException, ValidationException
	 {
		List<SezioneVO> elenco = null;
   try {
   	//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
   	elenco = this.acquisizioni.getRicercaListaSezioni(ricercaSezioni);
   } catch (ResourceNotFoundException e) {
       throw new EJBException(e);
		} catch (ApplicationException e) {
       throw new EJBException(e);

		} catch (ValidationException e) {
       throw e;
		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);

		}
		return elenco;
	}


	public List<BuoniOrdineVO> getRicercaListaBuoniOrd(ListaSuppBuoniOrdineVO ricercaBuoniOrd) throws EJBException, ValidationException
	 {
		List<BuoniOrdineVO> elenco = null;
  try {
  	//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
  	elenco = this.acquisizioni.getRicercaListaBuoniOrd(ricercaBuoniOrd);
  } catch (ResourceNotFoundException e) {
      throw new EJBException(e);
		} catch (ApplicationException e) {
      throw new EJBException(e);
		} catch (ValidationException e) {
      throw e;
		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);

		}
		return elenco;
	}


	public List<StrutturaCombo> getRicercaBiblAffiliate(String codiceBiblioteca, String codiceAttivita)  throws EJBException, ValidationException
	 {
		List<StrutturaCombo> elenco = null;
     try {
     	//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
     	elenco = this.acquisizioni.getRicercaBiblAffiliate(codiceBiblioteca,  codiceAttivita);
     } catch (ResourceNotFoundException e) {
         throw new EJBException(e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
         throw new EJBException(e);
		} catch (ValidationException e) {
//			logger.error("Errore in getBibliteche",e);
         throw e;
         //throw new EJBException(e);
		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);


		}

		return elenco;
	}

	public boolean inserisciOrdine(OrdiniVO ordine)   throws EJBException, ValidationException {
		boolean valRitorno = false;
		try {
			//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
			valRitorno = this.acquisizioni.inserisciOrdine(ordine);

        } catch (DataException e) {
            throw new EJBException(e);
		} catch (ApplicationException e) {
            throw new EJBException(e);
		} catch (ValidationException e) {
        	  //logger.error("Errore in getBibliteche",e);
        	  throw e;
              //throw new EJBException(e);
		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);


          }

		return valRitorno;
	}

	public boolean inserisciOrdineBiblHib(OrdiniVO ordine, List listaBiblAff)    throws EJBException, ValidationException {
		boolean valRitorno = false;
		try {
			//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
			valRitorno = this.acquisizioni.inserisciOrdineBiblHib(ordine, listaBiblAff);

        } catch (DataException e) {
            throw new EJBException(e);
		} catch (ApplicationException e) {
            throw new EJBException(e);
		} catch (ValidationException e) {
        	  //logger.error("Errore in getBibliteche",e);
        	  throw e;
              //throw new EJBException(e);
		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);


          }

		return valRitorno;
	}

	public boolean  cancellaOrdine(OrdiniVO ordine) throws EJBException, ValidationException, ApplicationException {
		boolean valRitorno = false;

		try {
			//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
			valRitorno = this.acquisizioni.cancellaOrdine(ordine);

	    } catch (DataException e) {
	        throw new EJBException(e);
		} catch (ApplicationException e) {
	        throw e;

		} catch (ValidationException e) {
		  	  //logger.error("Errore in getBibliteche",e);
		  	  throw e;
		        //throw new EJBException(e);
		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);

		}

		return valRitorno;
		}

	public boolean  modificaOrdine(OrdiniVO ordine) throws EJBException, ValidationException {
		boolean valRitorno = false;

		try {
			//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
			valRitorno = this.acquisizioni.modificaOrdine(ordine);

		} catch (DataException e) {
		    throw new EJBException(e);
		} catch (ApplicationException e) {
		    throw new EJBException(e);
		}   catch (ValidationException e) {
	  	  //logger.error("Errore in getBibliteche",e);
	  	  throw e;
	        //throw new EJBException(e);
		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);


		}

		return valRitorno;
		}


public boolean inserisciFornitore(FornitoreVO fornitore)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.inserisciFornitore(fornitore);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);
	}      catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);


	}
	return valRitorno;
}

public boolean modificaFornitore(FornitoreVO fornitore)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.modificaFornitore(fornitore);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);
	}      catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);


	}
	return valRitorno;
}

public boolean inserisciBilancio(BilancioVO bilancio)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.inserisciBilancio(bilancio);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);
	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);


	}
	return valRitorno;
}


public boolean inserisciFattura(FatturaVO fattura)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.inserisciFattura(fattura);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}

public boolean modificaFattura(FatturaVO fattura)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.modificaFattura(fattura);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}

public boolean  cancellaFattura(FatturaVO fattura) throws EJBException, ValidationException {
	boolean valRitorno = false;

	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.cancellaFattura(fattura);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	}catch (ValidationException e) {
	  	  //logger.error("Errore in getBibliteche",e);
	  	  throw e;
	        //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}

public boolean modificaBilancio(BilancioVO bilancio)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.modificaBilancio(bilancio);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);
	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);


	}
	return valRitorno;
}

public boolean  cancellaBilancio(BilancioVO bilancio) throws  EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.cancellaBilancio(bilancio);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);
	} catch (ValidationException e) {
	  	  //logger.error("Errore in getBibliteche",e);
	  	  throw e;
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}

		return valRitorno;
}



public boolean inserisciSezione(SezioneVO sezione)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.inserisciSezione(sezione);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);
	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);


	}
	return valRitorno;
}

public boolean modificaSezione(SezioneVO sezione)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.modificaSezione(sezione);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);


	}
	return valRitorno;
}
public boolean  cancellaSezione(SezioneVO sezione) throws EJBException, ValidationException {
	boolean valRitorno = false;

	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.cancellaSezione(sezione);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);
	}catch (ValidationException e) {
	  	  //logger.error("Errore in getBibliteche",e);
	  	  throw e;
	        //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}

public boolean inserisciBuonoOrd(BuoniOrdineVO buonoOrd)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.inserisciBuonoOrd(buonoOrd);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);
	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);

    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);


	}
	return valRitorno;
}

public boolean modificaBuonoOrd(BuoniOrdineVO buonoOrd)  throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.modificaBuonoOrd(buonoOrd);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}
public boolean  cancellaBuonoOrd(BuoniOrdineVO buonoOrd) throws EJBException, ValidationException {
	boolean valRitorno = false;

	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.cancellaBuonoOrd(buonoOrd);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	}catch (ValidationException e) {
	  	  //logger.error("Errore in getBibliteche",e);
	  	  throw e;
	        //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
	}

public List<StrutturaProfiloVO> getRicercaListaProfili(ListaSuppProfiloVO ricercaProfili) throws EJBException, ValidationException
{
	List<StrutturaProfiloVO> elenco = null;
  try {

	   //AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
  	elenco = this.acquisizioni.getRicercaListaProfili(ricercaProfili);
  } catch (ResourceNotFoundException e) {
      throw new EJBException(e);
	} catch (ApplicationException e) {
//		logger.error("Errore in getBibliteche",e);
      throw new EJBException(e);
	} catch (ValidationException e) {
//		logger.error("Errore in getBibliteche",e);
      throw e;
      //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}

	return elenco;
}

public boolean inserisciProfilo(StrutturaProfiloVO profilo)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.inserisciProfilo(profilo);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);

    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}

public boolean modificaProfilo(StrutturaProfiloVO profilo)  throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.modificaProfilo(profilo);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}

public boolean  cancellaProfilo(StrutturaProfiloVO profilo) throws  EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.cancellaProfilo(profilo);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);
	} catch (ValidationException e) {
	  	  //logger.error("Errore in getBibliteche",e);
	  	  throw e;
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}

		return valRitorno;
}

public List<ComunicazioneVO> getRicercaListaComunicazioni(ListaSuppComunicazioneVO ricercaComunicazioni) throws EJBException, ValidationException
{
	List<ComunicazioneVO> elenco = null;
  try {

	   //AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
  	elenco = this.acquisizioni.getRicercaListaComunicazioni(ricercaComunicazioni);
  } catch (ResourceNotFoundException e) {
      throw new EJBException(e);
	} catch (ApplicationException e) {
//		logger.error("Errore in getBibliteche",e);
      throw new EJBException(e);
	} catch (ValidationException e) {
//		logger.error("Errore in getBibliteche",e);
      throw e;
      //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}

	return elenco;
}
public boolean inserisciComunicazione(ComunicazioneVO comunicazione)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.inserisciComunicazione(comunicazione);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}

public boolean modificaComunicazione(ComunicazioneVO comunicazione)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.modificaComunicazione(comunicazione);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}

public boolean  cancellaComunicazione(ComunicazioneVO comunicazione) throws EJBException, ValidationException {
	boolean valRitorno = false;

	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.cancellaComunicazione(comunicazione);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	}catch (ValidationException e) {
	  	  //logger.error("Errore in getBibliteche",e);
	  	  throw e;
	        //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}

public List<SuggerimentoVO> getRicercaListaSuggerimenti(ListaSuppSuggerimentoVO ricercaSuggerimenti) throws EJBException, ValidationException
{
	List<SuggerimentoVO> elenco = null;
  try {

	   //AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
  	elenco = this.acquisizioni.getRicercaListaSuggerimenti(ricercaSuggerimenti);
  } catch (ResourceNotFoundException e) {
      throw new EJBException(e);
	} catch (ApplicationException e) {
//		logger.error("Errore in getBibliteche",e);
      throw new EJBException(e);
	} catch (ValidationException e) {
//		logger.error("Errore in getBibliteche",e);
      throw e;
      //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}

	return elenco;
}

public boolean inserisciSuggerimento(SuggerimentoVO suggerimento)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.inserisciSuggerimento(suggerimento);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);
	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);


	}
	return valRitorno;
}

public boolean modificaSuggerimento(SuggerimentoVO suggerimento)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.modificaSuggerimento(suggerimento);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}

public boolean  cancellaSuggerimento(SuggerimentoVO suggerimento) throws EJBException, ValidationException {
	boolean valRitorno = false;

	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.cancellaSuggerimento(suggerimento);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	}catch (ValidationException e) {
	  	  //logger.error("Errore in getBibliteche",e);
	  	  throw e;
	        //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}
public boolean modificaConfigurazione(ConfigurazioneBOVO configurazione)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.modificaConfigurazione(configurazione);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}

public ConfigurazioneBOVO loadConfigurazione(ConfigurazioneBOVO configurazione) throws EJBException, ValidationException
{
	ConfigurazioneBOVO oggetto = null;
  try {

	   //AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
	  oggetto = this.acquisizioni.loadConfigurazione(configurazione);
  } catch (ResourceNotFoundException e) {
      throw new EJBException(e);
	} catch (ApplicationException e) {
//		logger.error("Errore in getBibliteche",e);
      throw new EJBException(e);
	} catch (ValidationException e) {
//		logger.error("Errore in getBibliteche",e);
      throw e;
      //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return oggetto;
}


public ConfigurazioneORDVO loadConfigurazioneOrdini(ConfigurazioneORDVO configurazioneORD) throws EJBException, ValidationException
{
	ConfigurazioneORDVO oggetto = null;
  try {

	   //AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
	  oggetto = this.acquisizioni.loadConfigurazioneOrdini(configurazioneORD);
  } catch (ResourceNotFoundException e) {
      throw new EJBException(e);
	} catch (ApplicationException e) {
//		logger.error("Errore in getBibliteche",e);
      throw new EJBException(e);
	} catch (ValidationException e) {
//		logger.error("Errore in getBibliteche",e);
      throw e;
      //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return oggetto;
}

public boolean modificaConfigurazioneOrdini(ConfigurazioneORDVO configurazioneORD)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.modificaConfigurazioneOrdini(configurazioneORD);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}



public List<DocumentoVO> getRicercaListaDocumenti(ListaSuppDocumentoVO ricercaDocumenti) throws EJBException, ValidationException
{
	List<DocumentoVO> elenco = null;
  try {

	   //AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
  	elenco = this.acquisizioni.getRicercaListaDocumenti(ricercaDocumenti);
  } catch (ResourceNotFoundException e) {
      throw new EJBException(e);
	} catch (ApplicationException e) {
//		logger.error("Errore in getBibliteche",e);
      throw new EJBException(e);
	} catch (ValidationException e) {
//		logger.error("Errore in getBibliteche",e);
      throw e;
      //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}

	return elenco;
}

public boolean modificaDocumento(DocumentoVO documento)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.modificaDocumento(documento);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}

public List<GaraVO> getRicercaListaGare(ListaSuppGaraVO ricercaGare) throws EJBException, ValidationException
{
	List<GaraVO> elenco = null;
  try {

	   //AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
  	elenco = this.acquisizioni.getRicercaListaGare(ricercaGare);
  } catch (ResourceNotFoundException e) {
      throw new EJBException(e);
	} catch (ApplicationException e) {
//		logger.error("Errore in getBibliteche",e);
      throw new EJBException(e);
	} catch (ValidationException e) {
//		logger.error("Errore in getBibliteche",e);
      throw e;
      //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}

	return elenco;
}

public boolean inserisciGara(GaraVO gara)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.inserisciGara(gara);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}

public boolean modificaGara(GaraVO gara)   throws EJBException, ValidationException {
	boolean valRitorno = false;
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.modificaGara(gara);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}

public boolean  cancellaGara(GaraVO gara) throws EJBException, ValidationException {
	boolean valRitorno = false;

	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		valRitorno = this.acquisizioni.cancellaGara(gara);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	}catch (ValidationException e) {
	  	  //logger.error("Errore in getBibliteche",e);
	  	  throw e;
	        //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	return valRitorno;
}
	 //public DescrittoreBloccoVO getRicercaListaOfferte(ListaSuppOffertaFornitoreVO ricercaOfferte, int nRec) throws EJBException, ValidationException
 public List<OffertaFornitoreVO> getRicercaListaOfferte(ListaSuppOffertaFornitoreVO ricercaOfferte) throws EJBException, ValidationException
{
	List<OffertaFornitoreVO> elenco = null;
	//DescrittoreBloccoVO blocco1 = null;

	try {

	   //AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
  	elenco = this.acquisizioni.getRicercaListaOfferte(ricercaOfferte);
  	//String ticket=ricercaOfferte.getTicket();
  	//blocco1 = this.saveBlocchi(ticket, elenco, nRec);

	} catch (ResourceNotFoundException e) {
      throw new EJBException(e);
	} catch (ApplicationException e) {
//		logger.error("Errore in getBibliteche",e);
      throw new EJBException(e);
	} catch (ValidationException e) {
//		logger.error("Errore in getBibliteche",e);
      throw e;
      //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);

	}
	//return blocco1;
	return elenco;
}


/*public DescrittoreBloccoVO  getRicercaListaOfferte(ListaSuppOffertaFornitoreVO ricercaOfferte) throws EJBException, ValidationException
{
	List<OffertaFornitoreVO> elenco = null;
	DescrittoreBloccoVO blocco1 = null;
	try {
		AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		elenco = dao.getRicercaListaOfferte(ricercaOfferte);
		//elenco = this.acquisizioni.getRicercaListaOfferte(ricercaOfferte);
		blocco1 = this.saveBlocchi(ricercaOfferte.getTicket(), elenco, 2);
		if (blocco1 == null)
			throw new EJBException("Errore in generazione blocchi");
      } catch (ResourceNotFoundException e) {
          throw new EJBException(e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
          throw new EJBException(e);
	} catch (ValidationException e) {
//			logger.error("Errore in getBibliteche",e);
          throw e;
          //throw new EJBException(e);

		}
	return blocco1;
}*/

public  DescrittoreBloccoVO  gestBlock(String ticket,List listaRis,int numxpag) throws ResourceNotFoundException
{
	DescrittoreBloccoVO blocco1 = null;
	blocco1 = this.saveBlocchi(ticket, listaRis, numxpag);
	//if (blocco1 == null)
	//	throw new EJBException("Errore in generazione blocchi");
	return blocco1;
}

public  DescrittoreBloccoVO  caricaBlock(String ticket, String idLista,int numBlocco) throws ResourceNotFoundException, ValidationException
{
	DescrittoreBloccoVO blocco1 = null;
	blocco1 = this.nextBlocco( ticket,  idLista, numBlocco);
	//if (blocco1 == null)
	//	throw new EJBException("Errore in generazione blocchi");
	return blocco1;
}

public void  migrazioneCStoSBNWEBcateneRinnoviBis() throws ResourceNotFoundException,ValidationException
{
	try {
		this.acquisizioni.migrazioneCStoSBNWEBcateneRinnoviBis();

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);
	}
}


public 	void ValidaSezioneVO (SezioneVO oggettoVO) throws ResourceNotFoundException,ValidationException
{
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		this.acquisizioni.ValidaSezioneVO(oggettoVO);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);
	}

}

public 	void ValidaBilancioVO (BilancioVO oggettoVO) throws ResourceNotFoundException,ValidationException
{
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		this.acquisizioni.ValidaBilancioVO(oggettoVO);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);
	}

}
public 	void ValidaComunicazioneVO (ComunicazioneVO oggettoVO) throws ResourceNotFoundException,ValidationException
{
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		this.acquisizioni.ValidaComunicazioneVO(oggettoVO);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);
	}

}
public 	void ValidaStrutturaProfiloVO (StrutturaProfiloVO oggettoVO) throws ResourceNotFoundException,ValidationException
{
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		this.acquisizioni.ValidaStrutturaProfiloVO(oggettoVO);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);
	}

}
public 	void ValidaFatturaVO (FatturaVO oggettoVO) throws ResourceNotFoundException,ValidationException
{
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		this.acquisizioni.ValidaFatturaVO(oggettoVO);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);
	}

}
public 	void ValidaOrdiniVO (OrdiniVO oggettoVO) throws ResourceNotFoundException,ValidationException
{
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		this.acquisizioni.ValidaOrdiniVO(oggettoVO);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);
	}

}
public 	void ValidaFornitoreVO (FornitoreVO oggettoVO) throws ResourceNotFoundException,ValidationException
{
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		this.acquisizioni.ValidaFornitoreVO(oggettoVO);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);
	}

}
public 	void ValidaSuggerimentoVO (SuggerimentoVO oggettoVO) throws ResourceNotFoundException,ValidationException
{
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		this.acquisizioni.ValidaSuggerimentoVO(oggettoVO);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);
	}

}
public 	void ValidaCambioVO (CambioVO prova) throws ResourceNotFoundException,ValidationException
{
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		this.acquisizioni.ValidaCambioVO(prova);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);
	}

}
public 	void ValidaBuoniOrdineVO (BuoniOrdineVO oggettoVO) throws ResourceNotFoundException,ValidationException
{
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		this.acquisizioni.ValidaBuoniOrdineVO(oggettoVO);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);
	}

}
public 	void ValidaDocumentoVO (DocumentoVO oggettoVO) throws ResourceNotFoundException,ValidationException
{
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		this.acquisizioni.ValidaDocumentoVO(oggettoVO);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);
	}

}
public 	void ValidaGaraVO (GaraVO oggettoVO) throws ResourceNotFoundException,ValidationException
{
	try {
		//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
		this.acquisizioni.ValidaGaraVO(oggettoVO);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);

	} catch (ValidationException e) {
    	  //logger.error("Errore in getBibliteche",e);
    	  throw e;
          //throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);
	}
}
	//	titolo
/*	public List getTitolo(String bid, String ticket) throws ResourceNotFoundException,ValidationException
	{
		List lista = null;
		try {
			lista = this.acquisizioni.getTitolo(bid, ticket);
	    } catch (DataException e) {
	        throw new EJBException(e);
		} catch (ApplicationException e) {
	        throw new EJBException(e);

		} catch (ValidationException e) {
	    	  //logger.error("Errore in getBibliteche",e);
	    	  throw e;
	          //throw new EJBException(e);
		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}
		return lista;
	}
*/

public List getTitoloOrdine(String bidPassato) throws ResourceNotFoundException, DaoManagerException
{
	List lista = null;
	try {
		lista = this.acquisizioni.getTitoloOrdine(bidPassato);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);
	}

	return lista;
}

public TitoloACQVO getTitoloRox(String bidPassato) throws ResourceNotFoundException, DaoManagerException
{
	TitoloACQVO tit = null;
	try {
		tit = this.acquisizioni.getTitoloRox(bidPassato);

    } catch (DataException e) {
        throw new EJBException(e);
	} catch (ApplicationException e) {
        throw new EJBException(e);
	} catch (RemoteException e) {
//		logger.error("Errore in getBibliteche",e);
		throw new EJBException(e);
	}

	return tit;
}


	public List getTitolo(List listaBid, String ticket) throws ResourceNotFoundException,ValidationException
	{
		List lista = null;
		try {
			lista = this.acquisizioni.getTitolo(listaBid, ticket);
	    } catch (DataException e) {
	        throw new EJBException(e);
		} catch (ApplicationException e) {
	        throw new EJBException(e);

		} catch (ValidationException e) {
	    	  //logger.error("Errore in getBibliteche",e);
	    	  throw e;
	          //throw new EJBException(e);
		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}
		return lista;
	}

	public List<FornitoreVO> getRicercaListaFornitoriHib(ListaSuppFornitoreVO ricercaFornitori) throws  EJBException, ValidationException
	 {
		List<FornitoreVO> elenco = null;
    try {

 	   //AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
  	  elenco = this.acquisizioni.getRicercaListaFornitoriHib(ricercaFornitori);

    } catch (ResourceNotFoundException e) {
        throw new EJBException(e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
        throw new EJBException(e);
		} catch (ValidationException e) {
//			logger.error("Errore in getBibliteche",e);
        throw e;
        //throw new EJBException(e);

		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}

		return elenco;
	}

	public boolean  cancellaFornitoreHib(FornitoreVO fornitore) throws EJBException {
		boolean valRitorno = false;

		try {
			//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
			valRitorno = this.acquisizioni.cancellaFornitoreHib(fornitore);

	    } catch (DataException e) {
	        throw new EJBException(e);
	    } catch (ApplicationException e) {
	        throw new EJBException(e);

	    } catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}

			return valRitorno;
		}

	public boolean  cancellaFornitore(FornitoreVO fornitore) throws EJBException , ValidationException {
		boolean valRitorno = false;

		try {
			//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
			valRitorno = this.acquisizioni.cancellaFornitore(fornitore);

	    } catch (DataException e) {
	        throw new EJBException(e);
	    } catch (ApplicationException e) {
	        throw new EJBException(e);
		}catch (ValidationException e) {
		  	  //logger.error("Errore in getBibliteche",e);
		  	  throw e;
	    } catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}

			return valRitorno;
		}


	public boolean  modificaFornitoreHib(FornitoreVO fornitore) throws EJBException, ValidationException {
		boolean valRitorno = false;

		try {
			//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
			valRitorno = this.acquisizioni.modificaFornitoreHib(fornitore);

		} catch (DataException e) {
		    throw new EJBException(e);
		} catch (ValidationException e) {
	  	  //logger.error("Errore in getBibliteche",e);
	  	  throw e;
	        //throw new EJBException(e);
		} catch (ApplicationException e) {
		    throw new EJBException(e);
		} 	catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
	    }

	    return valRitorno;
		}


		public boolean inserisciFornitoreHib(FornitoreVO fornitore)  throws EJBException, ValidationException {
			boolean valRitorno = false;
			try {
				//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
				valRitorno =  this.acquisizioni.inserisciFornitoreHib(fornitore);
	        } catch (DataException e) {
	            throw new EJBException(e);
			} catch (ValidationException e) {
	      	  //logger.error("Errore in getBibliteche",e);
	      	  throw e;
	            //throw new EJBException(e);
			} catch (ApplicationException e) {
	            throw new EJBException(e);
	        } catch (RemoteException e) {
//				logger.error("Errore in getBibliteche",e);
				throw new EJBException(e);
	        }
			return valRitorno;
		}

	public ListaSuppFatturaVO gestioneFatturaDaDocFisico(ListaSuppFatturaVO ricercaFatture) throws EJBException, ValidationException
	 {
		ListaSuppFatturaVO elenco = null;
	   try {
	   	//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
	   	elenco = this.acquisizioni.gestioneFatturaDaDocFisico(ricercaFatture);
	   } catch (ResourceNotFoundException e) {
	       throw new EJBException(e);
			} catch (ValidationException e) {
				throw e;
			} catch (ApplicationException e) {
				throw new EJBException(e);
			} catch (RemoteException e) {
//				logger.error("Errore in getBibliteche",e);
				throw new EJBException(e);
			}
			return ricercaFatture;
		}


	public List costruisciCatenaRinnovati(StrutturaTerna ordRinn, String codBibl) throws EJBException, ValidationException {
		List elenco = null;
		try {
			//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
			elenco = this.acquisizioni.costruisciCatenaRinnovati( ordRinn,  codBibl);

	    } catch (DataException e) {
	        throw new EJBException(e);
		} catch (ApplicationException e) {
	        throw new EJBException(e);

		}catch (ValidationException e) {
		  	  //logger.error("Errore in getBibliteche",e);
		  	  throw e;
		        //throw new EJBException(e);
		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);

		}
		return elenco;
	}


	public List  gestioneStampato(List listaOggetti, String tipoOggetti, String bo) throws EJBException, ValidationException {
		List elenco = null;
		try {
			//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
			elenco = this.acquisizioni.gestioneStampato( listaOggetti,  tipoOggetti,  bo);

	    } catch (DataException e) {
	        throw new EJBException(e);
		} catch (ApplicationException e) {
	        throw new EJBException(e);

		}catch (ValidationException e) {
		  	  //logger.error("Errore in getBibliteche",e);
		  	  throw e;
		        //throw new EJBException(e);
		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);

		}
		return elenco;
	}

	public boolean  gestioneStampaOrdini(List listaOggetti, List idList, String tipoOggetti, String utente, String bo) throws EJBException, ValidationException {
		boolean valRitorno = false;

		try {
			//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
			valRitorno = this.acquisizioni.gestioneStampaOrdini( listaOggetti, idList, tipoOggetti, utente, bo);

	    } catch (DataException e) {
	        throw new EJBException(e);
		} catch (ApplicationException e) {
	        throw new EJBException(e);

		}catch (ValidationException e) {
		  	  //logger.error("Errore in getBibliteche",e);
		  	  throw e;
		        //throw new EJBException(e);
		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);

		}
		return valRitorno;
	}

	public List<StrutturaInventariOrdVO> getInventariOrdineRilegatura(ListaSuppOrdiniVO ricercaInvOrd) throws ResourceNotFoundException, ApplicationException, ValidationException
	 {
		List<StrutturaInventariOrdVO> elenco = null;
      try {

   	   //AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
      	elenco = this.acquisizioni.getInventariOrdineRilegatura(ricercaInvOrd);
      } catch (ResourceNotFoundException e) {
          throw new EJBException(e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
          throw new EJBException(e);
		} catch (ValidationException e) {
//			logger.error("Errore in getBibliteche",e);
          throw e;
          //throw new EJBException(e);

		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}

		return elenco;
	}

	public List<RigheVO>  ripartoSpese (ListaSuppSpeseVO criteriRiparto) throws ResourceNotFoundException, ApplicationException, ValidationException
	 {
		List<RigheVO> elenco = null;
     try {

  	   //AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
     	elenco = this.acquisizioni.ripartoSpese(criteriRiparto);
     } catch (ResourceNotFoundException e) {
         throw new EJBException(e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
         throw new EJBException(e);
		} catch (ValidationException e) {
//			logger.error("Errore in getBibliteche",e);
         throw e;
         //throw new EJBException(e);

		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}

		return elenco;
	}



	public List<StampaBuoniVO>  impostaBuoniOrdineDaStampare (ConfigurazioneBOVO configurazione, List listaOggetti, String tipoOggetti, Boolean ristampa, String ticket, String utente, String denoBibl) throws ResourceNotFoundException, ApplicationException, ValidationException
	 {
		List<StampaBuoniVO> elenco = null;
    try {

 	   //AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
    	elenco = this.acquisizioni.impostaBuoniOrdineDaStampare( configurazione,  listaOggetti,  tipoOggetti,  ristampa,  ticket,  utente,  denoBibl);
    } catch (ResourceNotFoundException e) {
        throw new EJBException(e);
		} catch (ApplicationException e) {
//			logger.error("Errore in getBibliteche",e);
        throw new EJBException(e);
		} catch (ValidationException e) {
//			logger.error("Errore in getBibliteche",e);
        throw e;
        //throw new EJBException(e);

		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}

		return elenco;
	}


	public 	int 	ValidaPreRicercaOrdini (ListaSuppOrdiniVO ricercaOrdini) throws ResourceNotFoundException,ValidationException
	{
		try {
			//AcquisizioniDAO dao = DAOFactory.getDAO(AcquisizioniDAO.class);
			return this.acquisizioni.ValidaPreRicercaOrdini(ricercaOrdini);
	    } catch (DataException e) {
	        throw new EJBException(e);
		} catch (ApplicationException e) {
	        throw new EJBException(e);

		} catch (ValidationException e) {
	    	  //logger.error("Errore in getBibliteche",e);
	    	  throw e;
	          //throw new EJBException(e);
		} catch (RemoteException e) {
//			logger.error("Errore in getBibliteche",e);
			throw new EJBException(e);
		}

	}

	public FornitoreVO getFornitore(String codPolo, String codBib, String codFornitore, String descr, String ticket)
	throws EJBException, ValidationException, DataException, it.iccu.sbn.ejb.exception.DataException {
		FornitoreVO rec = null;

		try {
			rec = this.acquisizioni.getFornitore(codPolo, codBib, codFornitore, descr, ticket);
		} catch (DataException e) {
			throw e;
		} catch (it.iccu.sbn.ejb.exception.DataException e) {
			throw e;
		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw new EJBException(e);
		} catch (Exception ve) {
			ve.printStackTrace();
			logger.error("Errore nel bean", ve);
		}

		return rec;

	}

	public CommandResultVO invoke(CommandInvokeVO command) throws ValidationException, ApplicationException {
		try {
			CommandResultVO invoke = this.acquisizioni.invoke(command);
			if (invoke.getError() != null)
				return invoke;

			return invoke;

		} catch (ValidationException e) {
			throw e;
		} catch (ApplicationException e) {
			throw e;
		} catch (RemoteException e) {
			throw new EJBException((Exception) e.detail);
		}
	}


}

// End GestioneSezioniBean
