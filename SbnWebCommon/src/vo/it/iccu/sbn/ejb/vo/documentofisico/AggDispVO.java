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
package it.iccu.sbn.ejb.vo.documentofisico;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ParametriRichiestaElaborazioneDifferitaVO;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.ArrayList;
import java.util.List;

public class AggDispVO extends DocumentoFisicoElaborazioniDifferiteOutputVO {

	private static final long serialVersionUID = -830821346474457368L;

	private String codTipoFruizione;
	private String codNoDispo;
	private String codRip;
	private String codiceStatoConservazione;
	private String codDigitalizzazione;
	//filtri
	private String filtroCodNatura;
	private String filtroLivAut;
	private String filtroDataIngressoDa;
	private String filtroDataIngressoA;
	private String filtroCodTipoFruizione;
	private String filtroCodNoDisp;
	private String filtroCodRip;
	private String filtroStatoConservazione;

	// almaviva2 Evolutiva marzo 2017 Filtro su data di Pubblicazione su batch di Aggiornamento disponibilità
	private String tipoDataPubb;
	public String getTipoDataPubb() {
		return tipoDataPubb;
	}

	public void setTipoDataPubb(String tipoDataPubb) {
		this.tipoDataPubb = tipoDataPubb;
	}

	public String getAaPubbFrom() {
		return aaPubbFrom;
	}

	public void setAaPubbFrom(String aaPubbFrom) {
		this.aaPubbFrom = aaPubbFrom;
	}

	public String getAaPubbTo() {
		return aaPubbTo;
	}

	public void setAaPubbTo(String aaPubbTo) {
		this.aaPubbTo = aaPubbTo;
	}

	private String aaPubbFrom;
	private String aaPubbTo;

	public AggDispVO(ParametriRichiestaElaborazioneDifferitaVO input) {
		super(input);
	}

	public void validate(List<String> errori) throws ValidationException {
		super.validate(errori);

		if ((this.getCodTipoFruizione() == null || (this.getCodTipoFruizione() != null && this
				.getCodTipoFruizione().trim().length() == 0))
				&& (this.getCodNoDispo() == null || (this.getCodNoDispo() != null && this
						.getCodNoDispo().trim().length() == 0))
									&& (this.getCodRip() == null || (this.getCodRip() != null && this
						.getCodRip().trim().length() == 0))
						&& (this.getCodiceStatoConservazione() == null || (this.getCodiceStatoConservazione() != null && this
								.getCodiceStatoConservazione().trim().length() == 0))
								&& (this.getCodDigitalizzazione() == null || (this.getCodDigitalizzazione() != null && this
										.getCodDigitalizzazione().trim().length() == 0))) {
			errori.add("ERRORE: validaInvIndicareAlmenoUnoTraCodFruiECodNoDisp");
		}
		//valori da assegnare
		if (this.getCodTipoFruizione() != null
				&& this.getCodTipoFruizione().length() != 0) {
			if (this.getCodTipoFruizione().length() > 1) {
				errori.add("ERRORE: tipoFruizioneEccedente");
			}
		}

		if (this.getCodNoDispo() != null && this.getCodNoDispo().length() != 0) {
			if (this.getCodNoDispo().length() > 1) {
				errori.add("ERRORE: codNoDispoEccedente");
			}
		}

		if (this.getCodRip() != null && this.getCodRip().length() != 0) {
			if (this.getCodRip().length() > 1) {
				errori.add("ERRORE: codRiproducibilitaEccedente");
			}
		}

		if (this.getCodiceStatoConservazione() != null && this.getCodiceStatoConservazione().length() != 0) {
			if (this.getCodiceStatoConservazione().length() > 1) {
				errori.add("ERRORE: CodiceStatoConservazioneEccedente");
			}
		}

		if (this.getCodDigitalizzazione() != null && this.getCodDigitalizzazione().length() != 0) {
			if (this.getCodDigitalizzazione().length() > 1) {
				errori.add("ERRORE: getCodDigitalizzazioneEccedente");
			}
		}

		//filtri
		if (this.getFiltroCodRip()== null) {
			errori.add("ERRORE: filtroCodRipNull");
		}
		if (this.getFiltroCodRip() != null && this.getFiltroCodRip().length() != 0) {
			if (this.getFiltroCodRip().length() > 1) {
				errori.add("ERRORE: FiltroCodRipEccedente");
			}
		}

		if (this.getFiltroCodNoDisp()== null) {
			errori.add("ERRORE: FiltroCodNoDispEccedenteNull");
		}
		if (this.getFiltroCodNoDisp() != null && this.getFiltroCodNoDisp().length() != 0) {
			if (this.getFiltroCodNoDisp().length() > 1) {
				errori.add("ERRORE: FiltroCodNoDispEccedente");
			}
		}

		if (this.getFiltroCodTipoFruizione()== null) {
			errori.add("ERRORE: FiltroCodStatoConservazioneNull");
		}
		if (this.getFiltroCodTipoFruizione() != null && this.getFiltroCodTipoFruizione().length() != 0) {
			if (this.getFiltroCodTipoFruizione().length() > 1) {
				errori.add("ERRORE: FiltroCodTipoFruizioneEccedente");
			}
		}

		if (this.getFiltroStatoConservazione()== null) {
			errori.add("ERRORE: FiltroCodStatoConservazioneNull");
		}
		if (this.getFiltroStatoConservazione() != null && this.getFiltroStatoConservazione().length() != 0) {
			if (this.getFiltroStatoConservazione().length() > 1) {
				errori.add("ERRORE: FiltroCodStatoConservazioneEccedente");
			}
		}

		if (this.getFiltroLivAut()== null) {
			errori.add("ERRORE: FiltroLivAutNull");
		}
		if (this.getFiltroLivAut() != null && this.getFiltroLivAut().length() != 0) {
			if (this.getFiltroLivAut().length() > 1) {
				errori.add("ERRORE: filtroLivAutEccedente");
			}
		}

		if (this.getFiltroCodNatura() == null) {
			errori.add("ERRORE: codNaturaObbligatorio");
		}
		if (this.getFiltroCodNatura() != null && this.getFiltroCodNatura().length() != 0) {
			if (this.getFiltroCodNatura().length() > 1) {
				errori.add("ERRORE: codNaturaEccedente");
			}
		}

		// almaviva2 Evolutiva marzo 2017 Filtro su data di Pubblicazione su batch di Aggiornamento disponibilità
		String testoErr="";
		//validazione anni pubblicazione
		if (isFilled(aaPubbFrom)) {
			// marzo 2017 Evolutiva per adeguare la ricerca con filtro per data al nuovo formato delle date che ora possono
			// contenere il carattere"." nel terzo e quarto carattere
//			if (!isNumeric(aaPubbFrom) || aaPubbFrom.length() != 4)	//non é un anno valido
//				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FORMATO_ANNO);
			testoErr = DateUtil.verificaFormatoDataParziale(aaPubbFrom, "Data1Da");
			if (testoErr.length()>0) {
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FORMATO_ANNO);
			}
			if (aaPubbFrom.endsWith("..")) {
				aaPubbFrom = aaPubbFrom.substring(0,2) + "00";
				if (!isFilled(aaPubbTo)) {
					aaPubbTo = aaPubbFrom.substring(0,2) + "99";
				}
			} else if (aaPubbFrom.endsWith(".")) {
				aaPubbFrom = aaPubbFrom.substring(0,3) + "0";
				if (!isFilled(aaPubbTo)) {
					aaPubbTo = aaPubbFrom.substring(0,3) + "9";
				}
			}
		}

		if (isFilled(aaPubbTo)) {
			// marzo 2017 Evolutiva per adeguare la ricerca con filtro per data al nuovo formato delle date che ora possono
			// contenere il carattere"." nel terzo e quarto carattere
//			if (!isNumeric(aaPubbTo) || aaPubbTo.length() != 4)	//non é un anno valido
//				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FORMATO_ANNO);
			testoErr = DateUtil.verificaFormatoDataParziale(aaPubbTo, "Data1A");
			if (testoErr.length()>0) {
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_FORMATO_ANNO);
			}
			if (aaPubbTo.endsWith("..")) {
				aaPubbTo = aaPubbTo.substring(0,2) + "99";
			} else if (aaPubbTo.endsWith(".")) {
				aaPubbTo = aaPubbTo.substring(0,3) + "9";
			}
		}

		if (isFilled(aaPubbFrom) && isFilled(aaPubbTo)) {
			if (Integer.valueOf(aaPubbTo) < Integer.valueOf(aaPubbFrom))
				throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_INTERVALLO_DATA);
		} else {
			if (isFilled(aaPubbFrom) && !isFilled(aaPubbTo)) {
				// se l'anno di pubblicazione finale è vuoto viene inserito uguale a quello di partenza in quanto
				// significa che la ricerca è secca su un solo anno
				aaPubbTo = aaPubbFrom;
			}
		}


		// FINE almaviva2 Evolutiva marzo 2017 Filtro su data di Pubblicazione su batch di Aggiornamento disponibilità



		if (this.getFiltroDataIngressoDa() != null
				&& this.getFiltroDataIngressoDa().length() != 0) {
			int codRitorno = ValidazioneDati.validaDataPassata(this
					.getFiltroDataIngressoDa());
			if (codRitorno != ValidazioneDati.DATA_OK)
				errori.add("ERRORE: dataIngressoDaErrata");
		}

		if (this.getFiltroDataIngressoA() != null
				&& this.getFiltroDataIngressoA().length() != 0) {
			int codRitorno = ValidazioneDati.validaDataPassata(this
					.getFiltroDataIngressoA());
			if (codRitorno != ValidazioneDati.DATA_OK)
				errori.add("ERRORE: dataIngressoAErrata");
		}

	}

	// output
	private List errori = new ArrayList();
	private String msg;

	public String getFiltroCodNatura() {
		return filtroCodNatura;
	}

	public void setFiltroCodNatura(String codNatura) {
		this.filtroCodNatura = codNatura;
	}

	public String getCodNoDispo() {
		return codNoDispo;
	}

	public void setCodNoDispo(String codNoDispo) {
		this.codNoDispo = codNoDispo;
	}

	public String getCodTipoFruizione() {
		return codTipoFruizione;
	}

	public void setCodTipoFruizione(String codTipoFruizione) {
		this.codTipoFruizione = codTipoFruizione;
	}

	public String getFiltroDataIngressoA() {
		return filtroDataIngressoA;
	}

	public void setFiltroDataIngressoA(String dataIngressoA) {
		this.filtroDataIngressoA = dataIngressoA;
	}

	public String getFiltroDataIngressoDa() {
		return filtroDataIngressoDa;
	}

	public void setFiltroDataIngressoDa(String dataIngressoDa) {
		this.filtroDataIngressoDa = dataIngressoDa;
	}

	public List getErrori() {
		return errori;
	}

	public void setErrori(List errori) {
		this.errori = errori;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getStringaToPrint() {

		if (this.msg == null || this.msg.equals("")) {
			return "<br>";
		} else {
			return msg + "<br>";
		}
	}

	public String getCodRip() {
		return codRip;
	}

	public void setCodRip(String codRip) {
		this.codRip = codRip;
	}

	public String getCodiceStatoConservazione() {
		return codiceStatoConservazione;
	}

	public void setCodiceStatoConservazione(String codiceStatoConservazione) {
		this.codiceStatoConservazione = codiceStatoConservazione;
	}

	public String getFiltroCodTipoFruizione() {
		return filtroCodTipoFruizione;
	}

	public void setFiltroCodTipoFruizione(String filtroCodTipoFruizione) {
		this.filtroCodTipoFruizione = filtroCodTipoFruizione;
	}

	public String getFiltroCodNoDisp() {
		return filtroCodNoDisp;
	}

	public void setFiltroCodNoDisp(String filtroCodNoDisp) {
		this.filtroCodNoDisp = filtroCodNoDisp;
	}

	public String getFiltroCodRip() {
		return filtroCodRip;
	}

	public void setFiltroCodRip(String filtroCodRip) {
		this.filtroCodRip = filtroCodRip;
	}

	public String getCodDigitalizzazione() {
		return codDigitalizzazione;
	}

	public void setCodDigitalizzazione(String codDigitalizzazione) {
		this.codDigitalizzazione = codDigitalizzazione;
	}

	public String getFiltroLivAut() {
		return filtroLivAut;
	}

	public void setFiltroLivAut(String filtroLivAut) {
		this.filtroLivAut = filtroLivAut;
	}

	public String getFiltroStatoConservazione() {
		return filtroStatoConservazione;
	}

	public void setFiltroStatoConservazione(String filtroStatoConservazione) {
		this.filtroStatoConservazione = filtroStatoConservazione;
	}

}
