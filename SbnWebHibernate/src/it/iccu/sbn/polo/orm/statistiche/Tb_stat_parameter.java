/**
 * "Visual Paradigm: DO NOT MODIFY THIS FILE!"
 *
 * This is an automatic generated file. It will be regenerated every time
 * you generate persistence class.
 *
 * Modifying its content may cause the program not work, or your work may lost.
 */

/**
 * Licensee: almaviva
 * License Type: Purchased
 */
package it.iccu.sbn.polo.orm.statistiche;

import java.io.Serializable;

import org.hibernate.proxy.HibernateProxyHelper;
/**
 * Statistiche parametri
 */
/**
 * ORM-Persistable Class
 */
public class Tb_stat_parameter implements Serializable {

	private static final long serialVersionUID = -3161061821471864822L;

	public Tb_stat_parameter() {
	}

	private it.iccu.sbn.polo.orm.statistiche.Tbf_config_statistiche id_stat;

	private String tipo;

	private String nome;

	private String valore;

	private String etichetta_nome;

	private String obbligatorio;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}



	public String getEtichetta_nome() {
		return etichetta_nome;
	}

	public void setEtichetta_nome(String etichettaNome) {
		etichetta_nome = etichettaNome;
	}

	public it.iccu.sbn.polo.orm.statistiche.Tbf_config_statistiche getId_stat() {
		return id_stat;
	}

	public void setId_stat(
			it.iccu.sbn.polo.orm.statistiche.Tbf_config_statistiche idStat) {
		id_stat = idStat;
	}

	public String getObbligatorio() {
		return obbligatorio;
	}

	public void setObbligatorio(String obbligatorio) {
		this.obbligatorio = obbligatorio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id_stat == null) ? 0 : id_stat.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(HibernateProxyHelper.getClassWithoutInitializingProxy(obj) == Tb_stat_parameter.class))
			return false;
		Tb_stat_parameter other = (Tb_stat_parameter) obj;
		if (id_stat == null) {
			if (other.id_stat != null)
				return false;
		} else if (!id_stat.equals(other.id_stat))
			return false;
		return true;
	}


}
