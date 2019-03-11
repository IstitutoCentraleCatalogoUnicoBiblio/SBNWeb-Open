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
package it.iccu.sbn.ejb.vo.servizi;

import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.servizi.autorizzazioni.AutorizzazioneVO;
import it.iccu.sbn.ejb.vo.servizi.calendario.ModelloCalendarioVO;
import it.iccu.sbn.ejb.vo.servizi.configurazione.ModelloSollecitoVO;

public class ParametriBibliotecaVO extends BaseVO {

	private static final long serialVersionUID = 1670594149710610593L;
	private int      id;
	private String   codPolo;
	private String   codBib;
	private short    numeroLettere;
	private short    numeroPrenotazioni;
	private short    ggRitardo1;
	private short    ggRitardo2;
	private short    ggRitardo3;
	private String   codFruizione;
	private String   codRiproduzione;
	private boolean  codFruiDaIntervallo;
	private String   codModalitaInvio1Sollecito1;
	private String   codModalitaInvio2Sollecito1;
	private String   codModalitaInvio3Sollecito1;
	private String   codModalitaInvio1Sollecito2;
	private String   codModalitaInvio2Sollecito2;
	private String   codModalitaInvio3Sollecito2;
	private String   codModalitaInvio1Sollecito3;
	private String   codModalitaInvio2Sollecito3;
	private String   codModalitaInvio3Sollecito3;
	private short    ggValiditaPrelazione;
	private boolean  ammessaAutoregistrazioneUtente;
	private boolean  ammessoInserimentoUtente;
	private boolean  ancheDaRemoto;

	private ModelloSollecitoVO modelloSollecito;

	private String catMediazioneDigit;
	private ModelloCalendarioVO calendario;

	private char tipoRinnovo;
	private short ggRinnovoRichiesta;

	private boolean prioritaPrenotazioni;
	private String mailNotifica;

	private AutorizzazioneVO autorizzazioneILL = new AutorizzazioneVO();
	private boolean serviziILLAttivi;

	public String getCodModalitaInvio1Sollecito1() {
		return codModalitaInvio1Sollecito1;
	}

	public void setCodModalitaInvio1Sollecito1(String codModalitaInvio1Sollecito1) {
		this.codModalitaInvio1Sollecito1 = codModalitaInvio1Sollecito1;
	}

	public String getCodModalitaInvio2Sollecito1() {
		return codModalitaInvio2Sollecito1;
	}

	public void setCodModalitaInvio2Sollecito1(String codModalitaInvio2Sollecito1) {
		this.codModalitaInvio2Sollecito1 = codModalitaInvio2Sollecito1;
	}

	public String getCodModalitaInvio3Sollecito1() {
		return codModalitaInvio3Sollecito1;
	}

	public void setCodModalitaInvio3Sollecito1(String codModalitaInvio3Sollecito1) {
		this.codModalitaInvio3Sollecito1 = codModalitaInvio3Sollecito1;
	}

	public String getCodModalitaInvio1Sollecito2() {
		return codModalitaInvio1Sollecito2;
	}

	public void setCodModalitaInvio1Sollecito2(String codModalitaInvio1Sollecito2) {
		this.codModalitaInvio1Sollecito2 = codModalitaInvio1Sollecito2;
	}

	public String getCodModalitaInvio2Sollecito2() {
		return codModalitaInvio2Sollecito2;
	}

	public void setCodModalitaInvio2Sollecito2(String codModalitaInvio2Sollecito2) {
		this.codModalitaInvio2Sollecito2 = codModalitaInvio2Sollecito2;
	}

	public String getCodModalitaInvio3Sollecito2() {
		return codModalitaInvio3Sollecito2;
	}

	public void setCodModalitaInvio3Sollecito2(String codModalitaInvio3Sollecito2) {
		this.codModalitaInvio3Sollecito2 = codModalitaInvio3Sollecito2;
	}

	public String getCodModalitaInvio1Sollecito3() {
		return codModalitaInvio1Sollecito3;
	}

	public void setCodModalitaInvio1Sollecito3(String codModalitaInvio1Sollecito3) {
		this.codModalitaInvio1Sollecito3 = codModalitaInvio1Sollecito3;
	}

	public String getCodModalitaInvio2Sollecito3() {
		return codModalitaInvio2Sollecito3;
	}

	public void setCodModalitaInvio2Sollecito3(String codModalitaInvio2Sollecito3) {
		this.codModalitaInvio2Sollecito3 = codModalitaInvio2Sollecito3;
	}

	public String getCodModalitaInvio3Sollecito3() {
		return codModalitaInvio3Sollecito3;
	}

	public void setCodModalitaInvio3Sollecito3(String codModalitaInvio3Sollecito3) {
		this.codModalitaInvio3Sollecito3 = codModalitaInvio3Sollecito3;
	}

	public short getGgValiditaPrelazione() {
		return ggValiditaPrelazione;
	}

	public void setGgValiditaPrelazione(short ggValiditaPrelazione) {
		this.ggValiditaPrelazione = ggValiditaPrelazione;
	}

	public boolean isAmmessaAutoregistrazioneUtente() {
		return ammessaAutoregistrazioneUtente;
	}

	public void setAmmessaAutoregistrazioneUtente(
			boolean ammessaAutoregistrazioneUtente) {
		this.ammessaAutoregistrazioneUtente = ammessaAutoregistrazioneUtente;
	}

	public boolean isAmmessoInserimentoUtente() {
		return ammessoInserimentoUtente;
	}

	public void setAmmessoInserimentoUtente(boolean ammessoInserimentoUtente) {
		this.ammessoInserimentoUtente = ammessoInserimentoUtente;
	}

	public boolean isAncheDaRemoto() {
		return ancheDaRemoto;
	}

	public void setAncheDaRemoto(boolean ancheDaRemoto) {
		this.ancheDaRemoto = ancheDaRemoto;
	}

	public String getCodFruizione() {
		return codFruizione;
	}

	public void setCodFruizione(String codFruizione) {
		this.codFruizione = trimAndSet(codFruizione);
	}

	public String getCodRiproduzione() {
		return codRiproduzione;
	}

	public void setCodRiproduzione(String codRiproduzione) {
		this.codRiproduzione = trimAndSet(codRiproduzione);
	}

	public boolean isCodFruiDaIntervallo() {
		return codFruiDaIntervallo;
	}

	public void setCodFruiDaIntervallo(boolean codFruiDaIntervallo) {
		this.codFruiDaIntervallo = codFruiDaIntervallo;
	}

	public ParametriBibliotecaVO() {
		super();
	}

	public ParametriBibliotecaVO(BaseVO base) {
		super(base);
	}



	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	public String getCodBib() {
		return codBib;
	}

	public void setCodBib(String codBib) {
		this.codBib = codBib;
	}

	public short getNumeroLettere() {
		return numeroLettere;
	}

	public void setNumeroLettere(short numeroLettere) {
		this.numeroLettere = numeroLettere;
	}

	public short getNumeroPrenotazioni() {
		return numeroPrenotazioni;
	}

	public void setNumeroPrenotazioni(short numeroPrenotazioni) {
		this.numeroPrenotazioni = numeroPrenotazioni;
	}

	public short getGgRitardo1() {
		return ggRitardo1;
	}

	public void setGgRitardo1(short ggRitardo1) {
		this.ggRitardo1 = ggRitardo1;
	}

	public short getGgRitardo2() {
		return ggRitardo2;
	}

	public void setGgRitardo2(short ggRitardo2) {
		this.ggRitardo2 = ggRitardo2;
	}

	public short getGgRitardo3() {
		return ggRitardo3;
	}

	public void setGgRitardo3(short ggRitardo3) {
		this.ggRitardo3 = ggRitardo3;
	}

	public ModelloSollecitoVO getModelloSollecito() {
		return modelloSollecito;
	}

	public void setModelloSollecito(ModelloSollecitoVO modelloSollecito) {
		this.modelloSollecito = modelloSollecito;
	}

	public String getCatMediazioneDigit() {
		return catMediazioneDigit;
	}

	public void setCatMediazioneDigit(String catMediazioneDigit) {
		this.catMediazioneDigit = catMediazioneDigit;
	}

	public ModelloCalendarioVO getCalendario() {
		return calendario;
	}

	public void setCalendario(ModelloCalendarioVO calendario) {
		this.calendario = calendario;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public char getTipoRinnovo() {
		return tipoRinnovo;
	}

	public void setTipoRinnovo(char tipoRinnovo) {
		this.tipoRinnovo = tipoRinnovo;
	}

	public short getGgRinnovoRichiesta() {
		return ggRinnovoRichiesta;
	}

	public void setGgRinnovoRichiesta(short ggRinnovoRichiesta) {
		this.ggRinnovoRichiesta = ggRinnovoRichiesta;
	}

	public boolean isPrioritaPrenotazioni() {
		return prioritaPrenotazioni;
	}

	public void setPrioritaPrenotazioni(boolean prioritaPrenotazioni) {
		this.prioritaPrenotazioni = prioritaPrenotazioni;
	}

	public String getMailNotifica() {
		return mailNotifica;
	}

	public void setMailNotifica(String mailNotifica) {
		this.mailNotifica = trimAndSet(mailNotifica);
	}

	public AutorizzazioneVO getAutorizzazioneILL() {
		return autorizzazioneILL;
	}

	public void setAutorizzazioneILL(AutorizzazioneVO autorizzazioneILL) {
		this.autorizzazioneILL = autorizzazioneILL;
	}

	public boolean isServiziILLAttivi() {
		return serviziILLAttivi;
	}

	public void setServiziILLAttivi(boolean serviziILLAttivi) {
		this.serviziILLAttivi = serviziILLAttivi;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ (ammessaAutoregistrazioneUtente ? 1231 : 1237);
		result = prime * result + (ammessoInserimentoUtente ? 1231 : 1237);
		result = prime * result + (ancheDaRemoto ? 1231 : 1237);
		result = prime * result + ((codBib == null) ? 0 : codBib.hashCode());
		result = prime * result + (codFruiDaIntervallo ? 1231 : 1237);
		result = prime * result
				+ ((codFruizione == null) ? 0 : codFruizione.hashCode());
		result = prime
				* result
				+ ((codModalitaInvio1Sollecito1 == null) ? 0
						: codModalitaInvio1Sollecito1.hashCode());
		result = prime
				* result
				+ ((codModalitaInvio1Sollecito2 == null) ? 0
						: codModalitaInvio1Sollecito2.hashCode());
		result = prime
				* result
				+ ((codModalitaInvio1Sollecito3 == null) ? 0
						: codModalitaInvio1Sollecito3.hashCode());
		result = prime
				* result
				+ ((codModalitaInvio2Sollecito1 == null) ? 0
						: codModalitaInvio2Sollecito1.hashCode());
		result = prime
				* result
				+ ((codModalitaInvio2Sollecito2 == null) ? 0
						: codModalitaInvio2Sollecito2.hashCode());
		result = prime
				* result
				+ ((codModalitaInvio2Sollecito3 == null) ? 0
						: codModalitaInvio2Sollecito3.hashCode());
		result = prime
				* result
				+ ((codModalitaInvio3Sollecito1 == null) ? 0
						: codModalitaInvio3Sollecito1.hashCode());
		result = prime
				* result
				+ ((codModalitaInvio3Sollecito2 == null) ? 0
						: codModalitaInvio3Sollecito2.hashCode());
		result = prime
				* result
				+ ((codModalitaInvio3Sollecito3 == null) ? 0
						: codModalitaInvio3Sollecito3.hashCode());
		result = prime * result + ((codPolo == null) ? 0 : codPolo.hashCode());
		result = prime * result
				+ ((codRiproduzione == null) ? 0 : codRiproduzione.hashCode());
		result = prime * result + ggRinnovoRichiesta;
		result = prime * result + ggRitardo1;
		result = prime * result + ggRitardo2;
		result = prime * result + ggRitardo3;
		result = prime * result + ggValiditaPrelazione;
		result = prime * result + id;
		result = prime * result
				+ ((mailNotifica == null) ? 0 : mailNotifica.hashCode());
		result = prime
				* result
				+ ((modelloSollecito == null) ? 0 : modelloSollecito.hashCode());
		result = prime * result + numeroLettere;
		result = prime * result + numeroPrenotazioni;
		result = prime * result + (prioritaPrenotazioni ? 1231 : 1237);
		result = prime * result + tipoRinnovo;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ParametriBibliotecaVO other = (ParametriBibliotecaVO) obj;
		if (ammessaAutoregistrazioneUtente != other.ammessaAutoregistrazioneUtente) {
			return false;
		}
		if (ammessoInserimentoUtente != other.ammessoInserimentoUtente) {
			return false;
		}
		if (ancheDaRemoto != other.ancheDaRemoto) {
			return false;
		}
		if (codBib == null) {
			if (other.codBib != null) {
				return false;
			}
		} else if (!codBib.equals(other.codBib)) {
			return false;
		}
		if (codFruiDaIntervallo != other.codFruiDaIntervallo) {
			return false;
		}
		if (codFruizione == null) {
			if (other.codFruizione != null) {
				return false;
			}
		} else if (!codFruizione.equals(other.codFruizione)) {
			return false;
		}
		if (codModalitaInvio1Sollecito1 == null) {
			if (other.codModalitaInvio1Sollecito1 != null) {
				return false;
			}
		} else if (!codModalitaInvio1Sollecito1
				.equals(other.codModalitaInvio1Sollecito1)) {
			return false;
		}
		if (codModalitaInvio1Sollecito2 == null) {
			if (other.codModalitaInvio1Sollecito2 != null) {
				return false;
			}
		} else if (!codModalitaInvio1Sollecito2
				.equals(other.codModalitaInvio1Sollecito2)) {
			return false;
		}
		if (codModalitaInvio1Sollecito3 == null) {
			if (other.codModalitaInvio1Sollecito3 != null) {
				return false;
			}
		} else if (!codModalitaInvio1Sollecito3
				.equals(other.codModalitaInvio1Sollecito3)) {
			return false;
		}
		if (codModalitaInvio2Sollecito1 == null) {
			if (other.codModalitaInvio2Sollecito1 != null) {
				return false;
			}
		} else if (!codModalitaInvio2Sollecito1
				.equals(other.codModalitaInvio2Sollecito1)) {
			return false;
		}
		if (codModalitaInvio2Sollecito2 == null) {
			if (other.codModalitaInvio2Sollecito2 != null) {
				return false;
			}
		} else if (!codModalitaInvio2Sollecito2
				.equals(other.codModalitaInvio2Sollecito2)) {
			return false;
		}
		if (codModalitaInvio2Sollecito3 == null) {
			if (other.codModalitaInvio2Sollecito3 != null) {
				return false;
			}
		} else if (!codModalitaInvio2Sollecito3
				.equals(other.codModalitaInvio2Sollecito3)) {
			return false;
		}
		if (codModalitaInvio3Sollecito1 == null) {
			if (other.codModalitaInvio3Sollecito1 != null) {
				return false;
			}
		} else if (!codModalitaInvio3Sollecito1
				.equals(other.codModalitaInvio3Sollecito1)) {
			return false;
		}
		if (codModalitaInvio3Sollecito2 == null) {
			if (other.codModalitaInvio3Sollecito2 != null) {
				return false;
			}
		} else if (!codModalitaInvio3Sollecito2
				.equals(other.codModalitaInvio3Sollecito2)) {
			return false;
		}
		if (codModalitaInvio3Sollecito3 == null) {
			if (other.codModalitaInvio3Sollecito3 != null) {
				return false;
			}
		} else if (!codModalitaInvio3Sollecito3
				.equals(other.codModalitaInvio3Sollecito3)) {
			return false;
		}
		if (codPolo == null) {
			if (other.codPolo != null) {
				return false;
			}
		} else if (!codPolo.equals(other.codPolo)) {
			return false;
		}
		if (codRiproduzione == null) {
			if (other.codRiproduzione != null) {
				return false;
			}
		} else if (!codRiproduzione.equals(other.codRiproduzione)) {
			return false;
		}
		if (ggRinnovoRichiesta != other.ggRinnovoRichiesta) {
			return false;
		}
		if (ggRitardo1 != other.ggRitardo1) {
			return false;
		}
		if (ggRitardo2 != other.ggRitardo2) {
			return false;
		}
		if (ggRitardo3 != other.ggRitardo3) {
			return false;
		}
		if (ggValiditaPrelazione != other.ggValiditaPrelazione) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (mailNotifica == null) {
			if (other.mailNotifica != null) {
				return false;
			}
		} else if (!mailNotifica.equals(other.mailNotifica)) {
			return false;
		}
		if (catMediazioneDigit == null) {
			if (other.catMediazioneDigit != null) {
				return false;
			}
		} else if (!catMediazioneDigit.equals(other.catMediazioneDigit)) {
			return false;
		}
		if (modelloSollecito == null) {
			if (other.modelloSollecito != null) {
				return false;
			}
		} else if (!modelloSollecito.equals(other.modelloSollecito)) {
			return false;
		}
		if (calendario == null) {
			if (other.calendario != null) {
				return false;
			}
		} else if (!calendario.equals(other.calendario)) {
			return false;
		}
		if (numeroLettere != other.numeroLettere) {
			return false;
		}
		if (numeroPrenotazioni != other.numeroPrenotazioni) {
			return false;
		}
		if (prioritaPrenotazioni != other.prioritaPrenotazioni) {
			return false;
		}
		if (tipoRinnovo != other.tipoRinnovo) {
			return false;
		}
		if (serviziILLAttivi != other.serviziILLAttivi) {
			return false;
		}
		if (autorizzazioneILL == null) {
			if (other.autorizzazioneILL != null) {
				return false;
			}
		} else if (!autorizzazioneILL.equals(other.autorizzazioneILL)) {
			return false;
		}
		return true;
	}

}
