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
package it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio;

import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.ElementoSinteticaServizioVO;

public class ServizioVO extends BaseVO {

	private static final long serialVersionUID = 9044114232826766591L;
	public static final int NEW = 1;
	public static final int MOD = 2;
	public static final int OLD = 3;
	public static final int DELMOD = 4;
	public static final int ELI = 5;
	public static final int DELOLD = 6;
	public static final int DELELI = 7;
	public static final int DELDELMOD = 8;
	public static final int DELDELOLD = 9;

	private int progressivo;
	private String cancella = "";

	private String codice = "";
	private String servizio = "";
	private String autorizzazione = "";
	private String note = "";
	private String dataInizio = "";
	private String dataFine = "";
	private String sospDataInizio = "";
	private String sospDataFine = "";
	private int stato = ServizioVO.OLD;
	private String descrizione = "";
	private String codPolo = "";
	private String codBib = "";
	private String codPoloUte = "";
	private String codBibUte = "";
	private String codUte = "";
	private String descrizioneTipoServizio = "";
	private String flag_aut_ereditato = "";

	private int idServizio;
	private int idUtente;


	public ServizioVO(String codice, String servizio,
			String autorizzazione, String note, String dataInizio,
			String dataFine, String sospDataInizio, String sospDataFine,
			int stato) throws Exception {
		super();

		this.codice = codice;
		this.servizio = servizio;
		this.autorizzazione = autorizzazione;
		this.note = note;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
		this.sospDataInizio = sospDataInizio;
		this.sospDataFine = sospDataFine;
		this.stato = stato;
	}

	public ServizioVO() {
	}

	public ServizioVO(ElementoSinteticaServizioVO ess) {
		super();
		this.codPolo = ess.getCodPolo();
		this.codBib = ess.getCodBiblioteca();
		this.codice = ess.getTipServizio();
		this.servizio = ess.getCodServizio();
		this.autorizzazione = ess.getCodAut();
		this.stato = NEW;
	}

	public int getStato() {
		return stato;
	}

	public void setStato(int stato) {
		this.stato = stato;
	}

	public String getAutorizzazione() {
		return autorizzazione;
	}

	public void setAutorizzazione(String autorizzazione) {
		this.autorizzazione = autorizzazione;
	}

	public String getDataFine() {
		return dataFine;
	}

	public void setDataFine(String dataFine) {
		if (dataFine == null) dataFine = "";
		if (!this.dataFine.equals(dataFine)) {
			if (this.stato == ServizioVO.OLD || this.stato == ServizioVO.MOD) {
				this.stato = ServizioVO.MOD;
			} else if (this.stato == ServizioVO.NEW) {
				this.stato = ServizioVO.NEW;
			}
			this.dataFine = dataFine;
		}
		this.dataFine = dataFine;
	}

	public String getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(String dataInizio) {
		if (dataInizio == null) dataInizio = "";
		if ( !this.dataInizio.equals(dataInizio)) {
			if (this.stato == ServizioVO.OLD || this.stato == ServizioVO.MOD) {
				this.stato = ServizioVO.MOD;
			} else if (this.stato == ServizioVO.NEW) {
				this.stato = ServizioVO.NEW;
			}
			this.dataInizio = dataInizio;
		}
	}

	public String getServizio() {
		return servizio;
	}


	public String getComponi() {
		// diritto
		String descrComposta="";
		if (descrizione == null) descrizione = "";
		if (descrizione.trim().equals(""))
		{
			descrComposta=servizio.trim() ;
		}
		else
		{
			descrComposta=servizio.trim()+ "-" + descrizione.trim() ;
		}
		return descrComposta ;
	}

	public String getComponiTipoServizio() {
		// serrvizio
		String descrComposta="";
		if (descrizioneTipoServizio == null) descrizioneTipoServizio = "";
		if (descrizioneTipoServizio.trim().equals(""))
		{
			descrComposta=codice.trim() ;
		}
		else
		{
			descrComposta=codice.trim()+ "-" + descrizioneTipoServizio.trim() ;
		}
		return descrComposta ;
	}

	public boolean getEsisteNota() {
		boolean esiste=false;
		if (note == null) note = "";
		if (!note.trim().equals(""))
		{
			esiste=true;
		}
		return esiste ;
	}

	public void setServizio(String servizio) {
		this.servizio = servizio;
	}

	public String getSospDataFine() {
		return sospDataFine;
	}

	public void setSospDataFine(String sospDataFine) {
		if (sospDataFine == null) sospDataFine = "";
		if (!this.sospDataFine.equals(sospDataFine)) {
			if (this.stato == ServizioVO.OLD || this.stato == ServizioVO.MOD) {
				this.stato = ServizioVO.MOD;
			} else if (this.stato == ServizioVO.NEW) {
				this.stato = ServizioVO.NEW;
			}
			this.sospDataFine = sospDataFine;
		}
	}

	public String getSospDataInizio() {
		return sospDataInizio;
	}

	public void setSospDataInizio(String sospDataInizio) {
		if (sospDataInizio == null) sospDataInizio = "";
		if (!this.sospDataInizio.equals(sospDataInizio)) {
			if (this.stato == ServizioVO.OLD || this.stato == ServizioVO.MOD) {
				this.stato = ServizioVO.MOD;
			} else if (this.stato == ServizioVO.NEW) {
				this.stato = ServizioVO.NEW;
			}
			this.sospDataInizio = sospDataInizio;
		}
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		if (note == null) note = "";
		if (!this.note.equals(note)) {
			if (this.stato == ServizioVO.OLD || this.stato == ServizioVO.MOD) {
				this.stato = ServizioVO.MOD;
			} else if (this.stato == ServizioVO.NEW) {
				this.stato = ServizioVO.NEW;
			}
			this.note = note;
		}
	}

	public String getCancella() {
		return cancella;
	}

	public void resetCancella() {
		this.cancella = "";
    }

	public void setCancella(String cancella) {
		if (cancella == null) cancella = "";
		this.cancella = cancella;
        if (this.cancella.equals("C")) {
        	switch(this.stato) {
        		case ServizioVO.OLD:
            		this.stato = ServizioVO.DELOLD;
        			break;
        		case ServizioVO.MOD:
            		this.stato = ServizioVO.DELMOD;
        			break;
        		case ServizioVO.NEW:
               		this.stato = ServizioVO.ELI;
        			break;
        	}
         }
        else {
         	switch(this.stato) {
	    		case ServizioVO.DELOLD:
	        		this.stato = ServizioVO.OLD;
	    			break;
	    		case ServizioVO.DELMOD:
	        		this.stato = ServizioVO.MOD;
	    			break;
	    		case ServizioVO.ELI:
	           		this.stato = ServizioVO.NEW;
	    			break;
	    	}
        }

	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public String getCodBibUte() {
		return codBibUte;
	}

	public void setCodBibUte(String codBibUte) {
		this.codBibUte = codBibUte;
	}

	public String getCodUte() {
		return codUte;
	}

	public void setCodUte(String codUte) {
		this.codUte = codUte;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ServizioVO other = (ServizioVO) obj;
		if (autorizzazione == null) {
			if (other.autorizzazione != null)
				return false;
		} else if (!autorizzazione.equals(other.autorizzazione))
			return false;
		if (cancella == null) {
			if (other.cancella != null)
				return false;
		} else if (!cancella.equals(other.cancella))
			return false;
		if (codBib == null) {
			if (other.codBib != null)
				return false;
		} else if (!codBib.equals(other.codBib))
			return false;
		if (codBibUte == null) {
			if (other.codBibUte != null)
				return false;
		} else if (!codBibUte.equals(other.codBibUte))
			return false;
		if (codUte == null) {
			if (other.codUte != null)
				return false;
		} else if (!codUte.equals(other.codUte))
			return false;
		if (codice == null) {
			if (other.codice != null)
				return false;
		} else if (!codice.equals(other.codice))
			return false;
		if (dataFine == null) {
			if (other.dataFine != null)
				return false;
		} else if (!dataFine.equals(other.dataFine))
			return false;
		if (dataInizio == null) {
			if (other.dataInizio != null)
				return false;
		} else if (!dataInizio.equals(other.dataInizio))
			return false;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (servizio == null) {
			if (other.servizio != null)
				return false;
		} else if (!servizio.equals(other.servizio))
			return false;
		if (sospDataFine == null) {
			if (other.sospDataFine != null)
				return false;
		} else if (!sospDataFine.equals(other.sospDataFine))
			return false;
		if (sospDataInizio == null) {
			if (other.sospDataInizio != null)
				return false;
		} else if (!sospDataInizio.equals(other.sospDataInizio))
			return false;
		if (stato != other.stato)
			return false;
		return true;
	}

	public int getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodPoloUte() {
		return codPoloUte;
	}

	public void setCodPoloUte(String codPoloUte) {
		this.codPoloUte = codPoloUte;
	}

	public int getIdServizio() {
		return idServizio;
	}

	public void setIdServizio(int idServizio) {
		this.idServizio = idServizio;
	}

	public int getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(int idUtente) {
		this.idUtente = idUtente;
	}

	public String getDescrizioneTipoServizio() {
		return descrizioneTipoServizio;
	}

	public void setDescrizioneTipoServizio(String descrizioneTipoServizio) {
		this.descrizioneTipoServizio = descrizioneTipoServizio;
	}

	public String getFlag_aut_ereditato() {
		return flag_aut_ereditato;
	}

	public void setFlag_aut_ereditato(String flag_aut_ereditato) {
		this.flag_aut_ereditato = flag_aut_ereditato;
	}


}
