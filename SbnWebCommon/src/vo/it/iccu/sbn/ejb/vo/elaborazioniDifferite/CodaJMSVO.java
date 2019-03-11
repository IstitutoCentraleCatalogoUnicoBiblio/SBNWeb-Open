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
package it.iccu.sbn.ejb.vo.elaborazioniDifferite;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.polo.orm.amministrazione.Tbf_coda_jms;

public class CodaJMSVO extends SerializableVO {

	private static final long serialVersionUID = -7236884176551672157L;

	private int id_coda;
	private String nome;
	private boolean codaSincrona = false;
	private String cron_expression;
	private String id_descrizione;
	private String id_descr_orario_attivazione;
	private String id_orario_di_attivazione;

	public CodaJMSVO(Tbf_coda_jms codaJms) {
		copyCommonProperties(this, codaJms);
		this.nome = trimAndSet(codaJms.getNome_jms());
		this.codaSincrona = (codaJms.getSincrona() == 'S');
	}

	public int getId_coda() {
		return id_coda;
	}

	public void setId_coda(int id_coda) {
		this.id_coda = id_coda;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = trimAndSet(nome);
	}

	public boolean isCodaSincrona() {
		return codaSincrona;
	}

	public void setCodaSincrona(boolean sincrona) {
		this.codaSincrona = sincrona;
	}

	public String getCron_expression() {
		return cron_expression;
	}

	public void setCron_expression(String cron_expression) {
		this.cron_expression = trimAndSet(cron_expression);
	}

	public String getId_descrizione() {
		return id_descrizione;
	}

	public void setId_descrizione(String id_descrizione) {
		this.id_descrizione = trimAndSet(id_descrizione);
	}

	public String getId_descr_orario_attivazione() {
		return id_descr_orario_attivazione;
	}

	public void setId_descr_orario_attivazione(
			String id_descr_orario_attivazione) {
		this.id_descr_orario_attivazione = trimAndSet(id_descr_orario_attivazione);
	}

	public String getId_orario_di_attivazione() {
		return id_orario_di_attivazione;
	}

	public void setId_orario_di_attivazione(String id_orario_di_attivazione) {
		this.id_orario_di_attivazione = trimAndSet(id_orario_di_attivazione);
	}

}
