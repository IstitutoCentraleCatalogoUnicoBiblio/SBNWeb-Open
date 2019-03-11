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
package it.iccu.sbn.vo.custom.servizi;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.collocazioni.OrdinamentoCollocazione2;
import it.iccu.sbn.ejb.utils.unicode.OrdinamentoUnicode;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.DatiRichiestaILLVO;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.vo.custom.servizi.ill.DatiRichiestaILLDecorator;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MovimentoListaVO extends MovimentoVO {

	private static final long serialVersionUID = 62780133576865738L;

	public static final String ORDINAMENTO_COGNOME_NOME_ASC = "01";
	public static final String ORDINAMENTO_COGNOME_NOME_DESC = "02";
	//03
	//04
	public static final String ORDINAMENTO_SERVIZIO_ASC = "05";
	public static final String ORDINAMENTO_SERVIZIO_DESC = "06";
	//07
	//08
	//09
	//10
	public static final String ORDINAMENTO_DATA_INIZIO_COLL_DATA_RICH_ASC = "11";
	public static final String ORDINAMENTO_DATA_INIZIO_COLL_DATA_RICH_DESC = "12";
	public static final String ORDINAMENTO_TITOLO_ASC = "13";
	public static final String ORDINAMENTO_TITOLO_DESC = "14";
	public static final String ORDINAMENTO_INVENTARIO_ASC = "15";
	public static final String ORDINAMENTO_INVENTARIO_DESC = "16";
	public static final String ORDINAMENTO_DATA_SCADENZA_ASC = "17";
	public static final String ORDINAMENTO_DATA_SCADENZA_DESC = "18";
	public static final String ORDINAMENTO_DATA_FINE_PREVISTA_ASC = "19";
	public static final String ORDINAMENTO_DATA_FINE_PREVISTA_DESC = "20";
	public static final String ORDINAMENTO_DATA_RICHIESTA_ASC = "21";
	public static final String ORDINAMENTO_DATA_RICHIESTA_DESC = "22";
	public static final String ORDINAMENTO_NRO_SOLL_DATA_SOLLECITO_DATA_SCADENZA_ASC = "23";
	public static final String ORDINAMENTO_NRO_SOLL_DATA_SOLLECITO_DATA_SCADENZA_DESC = "24";
	public static final String ORDINAMENTO_DATA_INIZIO_CONSEGNA_ASC = "25";
	public static final String ORDINAMENTO_DATA_INIZIO_CONSEGNA_DESC = "26";

	//almaviva5_20160526 #6113 data scadenza (asc) + n. solleciti (desc) + data invio (asc).
	public static final String ORDINAMENTO_DATA_SCADENZA_NRO_SOLL_DATA_INVIO = "27";

	public static final String ORDINAMENTO_STATO_RICHIESTA_ASC = "STATO_RICH_A";
	public static final String ORDINAMENTO_STATO_RICHIESTA_DESC = "STATO_RICH_D";

	private String cognomeNome;
	private String tipoServizio;
	private String attivita;

	private String titolo;
	private String copia_inventario;
	private String stato_richiesta;
	private String stato_movimento;
	private boolean scaduto = false;
	private int numSolleciti = 0;
	private String dataUltimoSollecito;
	private Date data_invio_soll;
	private boolean rinnovabile = false;
	private String numeroCopieStampaModulo;
	private String testoStampaModulo;

	private String kcles;

	//almaviva5_20150701
	private int prenotazioni;

	private String inventario;

	//usato per sort
	private static final OrdinamentoUnicode u = new OrdinamentoUnicode();

	public MovimentoListaVO() {
		super();
	}

	public MovimentoListaVO(MovimentoVO mov) {
		super(mov);
	}

	public MovimentoListaVO(MovimentoListaVO mov) {
		super(mov);
	    this.cognomeNome = mov.cognomeNome;
	    this.tipoServizio = mov.tipoServizio;
	    this.attivita = mov.attivita;
	    this.titolo = mov.titolo;
	    this.copia_inventario = mov.copia_inventario;
	    this.stato_richiesta = mov.stato_richiesta;
	    this.stato_movimento = mov.stato_movimento;
	    this.scaduto = mov.scaduto;
	    this.numSolleciti = mov.numSolleciti;
	    this.dataUltimoSollecito = mov.dataUltimoSollecito;
	    this.data_invio_soll = mov.data_invio_soll;
	    this.rinnovabile = mov.rinnovabile;
	    this.numeroCopieStampaModulo = mov.numeroCopieStampaModulo;
	    this.testoStampaModulo = mov.testoStampaModulo;
	    this.kcles = mov.kcles;
	}


	public static final Comparator<MovimentoListaVO> ORDINA_PER_DATA_RICHIESTA = new Comparator<MovimentoListaVO>() {
		public int compare(MovimentoListaVO o1, MovimentoListaVO o2) {
			return o1.dataRichiesta.compareTo(o2.dataRichiesta);
		}
	};

	public static final Comparator<MovimentoListaVO> ORDINA_PER_COGNOME_NOME = new Comparator<MovimentoListaVO>() {
		public int compare(MovimentoListaVO o1, MovimentoListaVO o2) {
			return u.convert(o1.cognomeNome).compareTo(u.convert(o2.cognomeNome));
		}
	};

	public static final Comparator<MovimentoListaVO> ORDINA_PER_TITOLO = new Comparator<MovimentoListaVO>() {
		public int compare(MovimentoListaVO o1, MovimentoListaVO o2) {
			return (o1.kcles + u.convert(o1.titolo)).compareTo(o2.kcles + u.convert(o2.titolo));
		}
	};

	public static final Comparator<MovimentoListaVO> ORDINA_PER_SEGNATURA = new Comparator<MovimentoListaVO>() {
		public int compare(MovimentoListaVO o1, MovimentoListaVO o2) {
			String s1 = OrdinamentoCollocazione2.normalizza(o1.segnatura);
			String s2 = OrdinamentoCollocazione2.normalizza(o2.segnatura);
			return ValidazioneDati.compare(s1, s2);
		}
	};

	public static final Comparator<MovimentoListaVO> ORDINA_PER_INVENTARIO = new Comparator<MovimentoListaVO>() {
		public int compare(MovimentoListaVO o1, MovimentoListaVO o2) {
			String inv1 = OrdinamentoCollocazione2.normalizza(o1.getDatiDocumento());
			String inv2 = OrdinamentoCollocazione2.normalizza(o2.getDatiDocumento());
			return ValidazioneDati.compare(inv1, inv2);
		}
	};

	public static final Comparator<MovimentoListaVO> ORDINA_PER_STATO_RICHIESTA = new Comparator<MovimentoListaVO>() {
		public int compare(MovimentoListaVO o1, MovimentoListaVO o2) {
			return ValidazioneDati.compare(o1.stato_richiesta, o2.stato_richiesta);
		}
	};

	public static final Comparator<MovimentoListaVO> ORDINA_PER_SERVIZIO = new Comparator<MovimentoListaVO>() {
		public int compare(MovimentoListaVO o1, MovimentoListaVO o2) {
			return ValidazioneDati.compare(o1.tipoServizio, o2.tipoServizio);
		}
	};

	public static final Comparator<MovimentoListaVO> ORDINA_PER_DATA_SCADENZA = new Comparator<MovimentoListaVO>() {
		public int compare(MovimentoListaVO o1, MovimentoListaVO o2) {
			Timestamp ts1 = o1.dataInizioEff != null ? o1.dataFinePrev : o1.dataInizioPrev;
			Timestamp ts2 = o2.dataInizioEff != null ? o2.dataFinePrev : o2.dataInizioPrev;
			return ValidazioneDati.compare(ts1, ts2);
		}
	};

	public static final Comparator<MovimentoListaVO> ORDINA_PER_DATA_INIZIO_PREVISTA = new Comparator<MovimentoListaVO>() {
		public int compare(MovimentoListaVO o1, MovimentoListaVO o2) {
			return ValidazioneDati.compare(o1.dataInizioPrev, o2.dataInizioPrev);
		}
	};

	public static final Comparator<MovimentoListaVO> ORDINA_PER_DATA_FINE_PREVISTA = new Comparator<MovimentoListaVO>() {
		public int compare(MovimentoListaVO o1, MovimentoListaVO o2) {
			return ValidazioneDati.compare(o1.dataFinePrev, o2.dataFinePrev);
		}
	};


	public static final Comparator<MovimentoListaVO> ORDINA_PER_ID_RICHIESTA = new Comparator<MovimentoListaVO>() {
		public int compare(MovimentoListaVO o1, MovimentoListaVO o2) {
			return (int) (o1.idRichiesta - o2.idRichiesta);
		}
	};

	public static final Comparator<MovimentoListaVO> ORDINA_PER_ATTIVITA = new Comparator<MovimentoListaVO>() {
		public int compare(MovimentoListaVO o1, MovimentoListaVO o2) {
			return ValidazioneDati.compare(o1.attivita, o2.attivita);
		}
	};

	public static final Comparator<MovimentoListaVO> ORDINA_PER_NRO_SOLLECITO = new Comparator<MovimentoListaVO>() {
		public int compare(MovimentoListaVO o1, MovimentoListaVO o2) {
			return o1.numSolleciti - o2.numSolleciti;
		}
	};

	public static final Comparator<MovimentoListaVO> ORDINA_PER_DATA_SOLLECITO = new Comparator<MovimentoListaVO>() {
		public int compare(MovimentoListaVO o1, MovimentoListaVO o2) {
			return ValidazioneDati.compare(o1.data_invio_soll, o2.data_invio_soll);
		}
	};

	//almaviva5_20100309 #3623
	public static final Comparator<MovimentoListaVO> ORDINA_PER_DATA_INIZIO_FLAG_CONSEGNA = new Comparator<MovimentoListaVO>() {
		public int compare(MovimentoListaVO o1, MovimentoListaVO o2) {
			//il campo da confrontare:
			// - data_inizio_eff se il documento risulta consegnato
			// - data_richiesta negli altri casi
			Timestamp ts1 = o1.consegnato ? o1.dataInizioEff : o1.dataRichiesta;
			Timestamp ts2 = o2.consegnato ? o2.dataInizioEff : o2.dataRichiesta;
			return ValidazioneDati.compare(ts1, ts2);
		}
	};

	//almaviva5_20160526 #6113 data scadenza (asc) + n. solleciti (desc) + data invio (asc).
	public static final Comparator<MovimentoListaVO> ORDINA_PER_DATA_SCADENZA_NRO_SOLL_DATA_INVIO = new Comparator<MovimentoListaVO>() {
		public int compare(MovimentoListaVO m1, MovimentoListaVO m2) {
			int cmp = m1.getDataFinePrev().compareTo(m2.getDataFinePrev());
			cmp = cmp != 0 ? cmp : -(m1.getNumSolleciti() - m2.getNumSolleciti());	//discendente
			cmp = cmp != 0 ? cmp : ValidazioneDati.compare(m1.getData_invio_soll(), m2.getData_invio_soll());
			return cmp;
		}
	};

	public String getStato_movimento() {
		return stato_movimento;
	}

	public void setStato_movimento(String stato_movimento) {
		this.stato_movimento = stato_movimento;
	}

	public String getUltimaAttivita() {
		if (isRichiestaLocale())
			return getAttivita();
		//richiesta ILL, si visualizza l'ultima attività (locale o ILL)
		DatiRichiestaILLDecorator datiILL = (DatiRichiestaILLDecorator) getDatiILL();
		Timestamp tsVarILL = datiILL.getUltimoCambioStato();
		Timestamp tsVarLoc = this.getTsVar();
		if (tsVarILL.after(tsVarLoc))
			return datiILL.getDescrizioneStatoRichiesta();
		else
			return getAttivita();
	}

	public String getAttivita() {
		return attivita;
	}

	public void setAttivita(String attivita) {
		this.attivita = attivita;
	}

	public String getCognomeNome() {
		return cognomeNome;
	}

	public void setCognomeNome(String cognomeNome) {
		this.cognomeNome = trimAndSet(cognomeNome);
	}

	public String getTipoServizio() {
		return tipoServizio;
	}

	public void setTipoServizio(String tipoServizio) {
		this.tipoServizio = tipoServizio;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getCopia_inventario() {
		return copia_inventario;
	}

	public void setCopia_inventario(String copia_inventario) {
		this.copia_inventario = copia_inventario;
	}

	public String getStato_richiesta() {
		return stato_richiesta;
	}

	public void setStato_richiesta(String stato_richiesta) {
		this.stato_richiesta = stato_richiesta;
	}

	public boolean isScaduto() {
		return scaduto;
	}

	public void setScaduto(boolean scaduto) {
		this.scaduto = scaduto;
	}

	public boolean isEsisteSollecito() {
		return (numSolleciti > 0);
	}

	public boolean isRinnovabile() {
		return rinnovabile;
	}

	public void setRinnovabile(boolean rinnovabile) {
		this.rinnovabile = rinnovabile;
	}

	public int getNumSolleciti() {
		return numSolleciti;
	}

	public void setNumSolleciti(int numSolleciti) {
		this.numSolleciti = numSolleciti;
	}

	public String getDataUltimoSollecito() {
		return dataUltimoSollecito;
	}

	public void setDataUltimoSollecito(String dataUltimoSollecito) {
		this.dataUltimoSollecito = dataUltimoSollecito;
	}

	public String getNumeroCopieStampaModulo() {
		return numeroCopieStampaModulo;
	}

	public void setNumeroCopieStampaModulo(String numeroCopieStampaModulo) {
		this.numeroCopieStampaModulo = numeroCopieStampaModulo;
	}

	public String getTestoStampaModulo() {
		return testoStampaModulo;
	}

	public void setTestoStampaModulo(String testoStampaModulo) {
		this.testoStampaModulo = testoStampaModulo;
	}

	public Date getData_invio_soll() {
		return data_invio_soll;
	}

	public void setData_invio_soll(Date dataInvioSoll) {
		data_invio_soll = dataInvioSoll;
	}

	public String getKcles() {
		return kcles;
	}

	public void setKcles(String kcles) {
		this.kcles = trimAndSet(kcles);
	}

	public int getPrenotazioni() {
		return prenotazioni;
	}

	public void setPrenotazioni(int prenotazioni) {
		this.prenotazioni = prenotazioni;
	}

	public String getDenominazioneBibFornitrice() {
		DatiRichiestaILLVO dati = getDatiILL();
		if (!isRichiestaILL() || dati.isFornitrice())
			return "";

		List<BibliotecaVO> bibFornitrici = dati.getBibliotecheFornitrici();
		if (!isFilled(bibFornitrici))
			return "";

		return ValidazioneDati.first(bibFornitrici).getNom_biblioteca();
	}

	public void decode() throws Exception {
		setAttivita(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_ATTIVITA_ITER, getCodAttivita()));
		setTipoServizio(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_TIPO_SERVIZIO, getCodTipoServ()));
		setStato_richiesta(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_STATO_RICHIESTA, getCodStatoRic()));
		setStato_movimento(CodiciProvider.getDescrizioneCodiceSBN(CodiciType.CODICE_STATO_MOVIMENTO, getCodStatoMov()));

		if (isRichiestaILL()) {
			DatiRichiestaILLVO datiILL = getDatiILL();
			setDatiILL(new DatiRichiestaILLDecorator(datiILL));
		}
	}

	public String getInventario() {
		return inventario;
	}

	public void setInventario(String inventario) {
		this.inventario = inventario;
	}

}
