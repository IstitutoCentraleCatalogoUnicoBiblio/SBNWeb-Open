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
package it.iccu.sbn.ejb.vo.servizi.configurazione;

import gnu.trove.THashMap;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;

import java.util.Map;

public class ModelloSollecitoVO extends SerializableVO {

	public enum TipoModello {
		LETTERA,
		EMAIL,
		SMS;
	}

	public enum CampoSollecito {
		NOME_UTENTE		("nomeutente", "servizi.configurazione.sollecito.nomeutente"),
		COD_UTENTE		("codutente", "servizi.configurazione.sollecito.codutente"),
		TIPO_SERVIZIO	("tiposervizio", "servizi.configurazione.sollecito.tiposervizio"),
		INDIRIZZO		("indirizzo", "servizi.configurazione.sollecito.indirizzo"),
		CAP				("cap", "servizi.configurazione.sollecito.cap"),
		CITTA_NAZIONE	("cittanazione", "servizi.configurazione.sollecito.cittanazione"),
		SEGNATURA		("segnatura", "servizi.configurazione.sollecito.segnatura"),
		DATA_SCADENZA	("datascadenza", "servizi.configurazione.sollecito.datascadenza"),
		NUM_SOLLECITO	("numsollecito", "servizi.configurazione.sollecito.numsollecito"),
		TITOLO			("titolo", "servizi.configurazione.sollecito.titolo"),
		INVENTARIO		("inventario", "servizi.configurazione.sollecito.inventario"),
		DATA_PRESTITO	("dataprestito", "servizi.configurazione.sollecito.dataprestito"),
		PROVINCIA		("provincia", "servizi.configurazione.sollecito.provincia"),
		NUM_RICHIESTA	("numrichiesta", "servizi.configurazione.sollecito.numrichiesta");

		private static Map<String, CampoSollecito> values;
		private final String field;
		private final String descr;

		static {
			CampoSollecito[] cs = CampoSollecito.class.getEnumConstants();
			values = new THashMap<String, CampoSollecito>();
			for (int i = 0; i < cs.length; i++)
				values.put(cs[i].field.toLowerCase(), cs[i]);
		}

		public static final CampoSollecito of(String value) {
			if (ValidazioneDati.strIsNull(value) )
				return null;
			return CampoSollecito.values.get(value.trim().toLowerCase() );
		}

		private CampoSollecito(String field, String descr) {
			this.field = field;
			this.descr = descr;
		}

		public String getDescr() {
			return descr;
		}

		public String getField() {
			return field;
		}
	}

	public enum FormattazioneCampo {
		GRASSETTO		("g", "servizi.configurazione.sollecito.grassetto"),
		SOTTOLINEATO	("s", "servizi.configurazione.sollecito.sottolineato"),
		ITALICO			("i", "servizi.configurazione.sollecito.corsivo"),
		DESTRA			("d", "servizi.configurazione.sollecito.allinea.destra"),
		CENTRO			("c", "servizi.configurazione.sollecito.allinea.centro");

		private final String tag;
		private final String descr;

		private FormattazioneCampo(String tag, String descr) {
			this.tag = tag;
			this.descr = descr;
		}

		public String getTag() {
			return tag;
		}

		public String getDescr() {
			return descr;
		}
	}

	private static final long serialVersionUID = 8533765107031624152L;

	private String modello;
	private String modelloMail;
	private String modelloSms;

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = trimAndSet(modello);
	}

	public String getModelloMail() {
		return modelloMail;
	}

	public void setModelloMail(String modelloMail) {
		this.modelloMail = trimAndSet(modelloMail);
	}

	public String getModelloSms() {
		return modelloSms;
	}

	public void setModelloSms(String modelloSms) {
		this.modelloSms = trimAndSet(modelloSms);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ModelloSollecitoVO other = (ModelloSollecitoVO) obj;
		if (modello == null) {
			if (other.modello != null) {
				return false;
			}
		} else if (!modello.equals(other.modello)) {
			return false;
		}
		if (modelloMail == null) {
			if (other.modelloMail != null) {
				return false;
			}
		} else if (!modelloMail.equals(other.modelloMail)) {
			return false;
		}
		if (modelloSms == null) {
			if (other.modelloSms != null) {
				return false;
			}
		} else if (!modelloSms.equals(other.modelloSms)) {
			return false;
		}
		return true;
	}

}
