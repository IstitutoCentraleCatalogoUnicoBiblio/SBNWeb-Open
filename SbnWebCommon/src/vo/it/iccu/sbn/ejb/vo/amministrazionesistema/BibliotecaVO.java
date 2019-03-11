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
package it.iccu.sbn.ejb.vo.amministrazionesistema;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.unicode.OrdinamentoUnicode;
import it.iccu.sbn.ejb.vo.BaseVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ill.BibliotecaILLVO;
import it.iccu.sbn.util.Isil;

import java.util.Comparator;

public class BibliotecaVO extends BaseVO {


	private static final long serialVersionUID = 5599709707589163005L;

	public static final Comparator<BibliotecaVO> ORDINAMENTO_PER_PROGRESSIVO =
		new Comparator<BibliotecaVO>() {
		public int compare(BibliotecaVO o1, BibliotecaVO o2) {
			return o1.prg - o2.prg;
		}
	};

	public static final Comparator<BibliotecaVO> ORDINAMENTO_PER_INDIRIZZO_COMPOSTO =
		new Comparator<BibliotecaVO>() {
		private OrdinamentoUnicode u = new OrdinamentoUnicode();
		public int compare(BibliotecaVO o1, BibliotecaVO o2) {
			return u.convert(o1.getIndirizzoComposto()).compareTo(u.convert(o2.getIndirizzoComposto()));
		}
	};

	private int prg = 0;
	private Integer id_utenti_biblioteca;
	private String cod_bib_ins;
	private String cod_bib_agg;
	private int idBiblioteca;
	private String cd_ana_biblioteca;
	private String cod_polo;
	private String cod_bib;
	private String nom_biblioteca;
	private String unit_org;
	private String gruppo;
	private String indirizzo;
	private String cpostale;
	private String cap;
	private String telefono;
	private String fax;
	private String note;
	private String p_iva;
	private String cod_fiscale;
	private String e_mail;
	private String tipo_biblioteca;
	private String paese;
	private String provincia;
	private String cod_bib_cs;
	private String cod_bib_ut;
	private long cod_utente;
	private String flag_bib;
	private String localita;
	private String chiave_bib;
	private String chiave_ente;
	private int inserito;
	private boolean abilitata;
	private String codSistemaMetropolitano;

	//almaviva5_20160510 servizi ILL
	private int priorita;
	private BibliotecaILLVO bibliotecaILL = new BibliotecaILLVO();

	public BibliotecaVO() {
		super();
	}

	public BibliotecaVO(BibliotecaVO bib) {
		super(bib);
	    this.prg = bib.prg;
	    this.id_utenti_biblioteca = bib.id_utenti_biblioteca;
	    this.cod_bib_ins = bib.cod_bib_ins;
	    this.cod_bib_agg = bib.cod_bib_agg;
	    this.idBiblioteca = bib.idBiblioteca;
	    this.cd_ana_biblioteca = bib.cd_ana_biblioteca;
	    this.cod_polo = bib.cod_polo;
	    this.cod_bib = bib.cod_bib;
	    this.nom_biblioteca = bib.nom_biblioteca;
	    this.unit_org = bib.unit_org;
	    this.gruppo = bib.gruppo;
	    this.indirizzo = bib.indirizzo;
	    this.cpostale = bib.cpostale;
	    this.cap = bib.cap;
	    this.telefono = bib.telefono;
	    this.fax = bib.fax;
	    this.note = bib.note;
	    this.p_iva = bib.p_iva;
	    this.cod_fiscale = bib.cod_fiscale;
	    this.e_mail = bib.e_mail;
	    this.tipo_biblioteca = bib.tipo_biblioteca;
	    this.paese = bib.paese;
	    this.provincia = bib.provincia;
	    this.cod_bib_cs = bib.cod_bib_cs;
	    this.cod_bib_ut = bib.cod_bib_ut;
	    this.cod_utente = bib.cod_utente;
	    this.flag_bib = bib.flag_bib;
	    this.localita = bib.localita;
	    this.chiave_bib = bib.chiave_bib;
	    this.chiave_ente = bib.chiave_ente;
	    this.inserito = bib.inserito;
	    this.abilitata = bib.abilitata;
	    this.codSistemaMetropolitano = bib.codSistemaMetropolitano;
	    this.priorita = bib.priorita;
	    this.bibliotecaILL = bib.bibliotecaILL;
	}

	public BibliotecaVO(BibliotecaILLVO bibIll) {
		this.cod_polo = "";
		this.flCanc = "N";
		this.nom_biblioteca = ValidazioneDati.trunc(bibIll.getDescrizione(), 80);
		Isil isil = Isil.parse(bibIll.getIsil());

		if (isil.withPaese()) {
			this.paese = isil.getPaese();
			this.cd_ana_biblioteca = isil.getSuffisso();
		} else {
		//senza codice
			this.paese = "UN";
			this.cd_ana_biblioteca = isil.getSuffisso();
		}

		this.setUnit_org("n\\a");
		this.setIndirizzo("n\\a");
		this.setCap("00000");
		this.setProvincia("EE");
		this.setTipo_biblioteca("Z");
		this.setLocalita("n\\a");
		this.setCod_bib_cs("");
		this.setChiave_bib(" ");
		this.setChiave_ente(" ");

	}

	public Integer getId_utenti_biblioteca() {
		return id_utenti_biblioteca;
	}

	public void setId_utenti_biblioteca(Integer id_utenti_biblioteca) {
		this.id_utenti_biblioteca = id_utenti_biblioteca;
	}

	public int getInserito() {
		return inserito;
	}

	public void setInserito(int inserito) {
		this.inserito = inserito;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = trimAndSet(cap);
	}

	public String getChiave_bib() {
		return chiave_bib;
	}

	public void setChiave_bib(String chiave_bib) {
		this.chiave_bib = trimAndSet(chiave_bib);
	}

	public String getChiave_ente() {
		return chiave_ente;
	}

	public void setChiave_ente(String chiave_ente) {
		this.chiave_ente = trimAndSet(chiave_ente);
	}

	public String getCod_bib() {
		return cod_bib;
	}

	public void setCod_bib(String cod_bib) {
		//almaviva5_20120319 #4842
		if (cod_bib == null)
			this.cod_bib = null;
		else
			this.cod_bib = ValidazioneDati.fillLeft(trimOrEmpty(cod_bib), ' ', 3);
	}

	public String getCod_bib_agg() {
		return cod_bib_agg;
	}

	public void setCod_bib_agg(String cod_bib_agg) {
		this.cod_bib_agg = cod_bib_agg;
	}

	public String getCod_bib_cs() {
		return cod_bib_cs;
	}

	public void setCod_bib_cs(String cod_bib_cs) {
		this.cod_bib_cs = ValidazioneDati.fillLeft(trimOrEmpty(cod_bib_cs), ' ', 3);
	}

	public String getCod_bib_ins() {
		return cod_bib_ins;
	}

	public void setCod_bib_ins(String cod_bib_ins) {
		this.cod_bib_ins = cod_bib_ins;
	}

	public String getCod_bib_ut() {
		return cod_bib_ut;
	}

	public void setCod_bib_ut(String cod_bib_ut) {
		this.cod_bib_ut = cod_bib_ut;
	}

	public String getCod_fiscale() {
		return cod_fiscale;
	}

	public void setCod_fiscale(String codFiscale) {
		this.cod_fiscale = trimOrEmpty(codFiscale).toUpperCase();
	}

	public String getCod_polo() {
		return cod_polo;
	}

	public void setCod_polo(String cod_polo) {
		this.cod_polo = trimAndSet(cod_polo);
	}

	public long getCod_utente() {
		return cod_utente;
	}

	public void setCod_utente(long cod_utente) {
		this.cod_utente = cod_utente;
	}

	public String getCpostale() {
		return cpostale;
	}

	public void setCpostale(String cpostale) {
		this.cpostale = trimAndSet(cpostale);
	}

	public String getE_mail() {
		return e_mail;
	}

	public void setE_mail(String e_mail) {
		this.e_mail = trimAndSet(e_mail);
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = trimAndSet(fax);
	}

	public String getFlag_bib() {
		return flag_bib;
	}

	public void setFlag_bib(String flag_bib) {
		this.flag_bib = flag_bib;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = trimAndSet(indirizzo);
	}

	public String getLocalita() {
		return localita;
	}

	public void setLocalita(String localita) {
		this.localita = trimAndSet(localita);
	}

	public String getNom_biblioteca() {
		return nom_biblioteca;
	}

	public void setNom_biblioteca(String nom_biblioteca) {
		this.nom_biblioteca = trimAndSet(nom_biblioteca);
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = trimAndSet(note);
	}

	public String getP_iva() {
		return p_iva;
	}

	public void setP_iva(String partIva) {
		this.p_iva = trimOrEmpty(partIva).toUpperCase();
	}

	public String getPaese() {
		return paese;
	}

	public void setPaese(String paese) {
		this.paese = paese;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = trimAndSet(provincia);
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = trimAndSet(telefono);
	}

	public String getTipo_biblioteca() {
		return tipo_biblioteca;
	}

	public void setTipo_biblioteca(String tipo_biblioteca) {
		this.tipo_biblioteca = tipo_biblioteca;
	}

	public String getUnit_org() {
		return unit_org;
	}

	public void setUnit_org(String unit_org) {
		this.unit_org = trimAndSet(unit_org);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((cd_ana_biblioteca == null) ? 0 : cd_ana_biblioteca
						.hashCode());
		result = prime * result + ((cod_bib == null) ? 0 : cod_bib.hashCode());
		result = prime * result
				+ ((cod_polo == null) ? 0 : cod_polo.hashCode());
		result = prime * result + ((paese == null) ? 0 : paese.hashCode());
		return result;
	}

	public int getIdBiblioteca() {
		return idBiblioteca;
	}

	public void setIdBiblioteca(int idBiblioteca) {
		this.idBiblioteca = idBiblioteca;
	}

	public String getCd_ana_biblioteca() {
		return cd_ana_biblioteca;
	}

	public void setCd_ana_biblioteca(String cd_ana_biblioteca) {
		this.cd_ana_biblioteca = trimAndSet(cd_ana_biblioteca);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		final BibliotecaVO other = (BibliotecaVO) obj;

		if (cod_polo == null) {
			if (other.cod_polo != null)	return false;
		} else
			if (!cod_polo.equals(other.cod_polo)) return false;

		if (cod_bib == null) {
			if (other.cod_bib != null)	return false;
		} else
			if (!cod_bib.equals(other.cod_bib))	return false;

		if (cd_ana_biblioteca == null) {
			if (other.cd_ana_biblioteca != null)	return false;
		} else
			if (!cd_ana_biblioteca.equals(other.cd_ana_biblioteca))	return false;

		if (nom_biblioteca == null) {
			if (other.nom_biblioteca != null)	return false;
		} else
			if (!nom_biblioteca.equals(other.nom_biblioteca))	return false;

		if (unit_org == null) {
			if (other.unit_org != null)	return false;
		} else
			if (!unit_org.equals(other.unit_org))	return false;

		if (indirizzo == null) {
			if (other.indirizzo != null)	return false;
		} else
			if (!indirizzo.equals(other.indirizzo))	return false;

		if (cpostale == null) {
			if (other.cpostale != null)	return false;
		} else
			if (!cpostale.equals(other.cpostale))	return false;

		if (cap == null) {
			if (other.cap != null)	return false;
		} else
			if (!cap.equals(other.cap))	return false;

		if (telefono == null) {
			if (other.telefono != null)	return false;
		} else
			if (!telefono.equals(other.telefono))	return false;

		if (fax == null) {
			if (other.fax != null)	return false;
		} else
			if (!fax.equals(other.fax))	return false;

		if (note == null) {
			if (other.note != null)	return false;
		} else
			if (!note.equals(other.note))	return false;

		if (p_iva == null) {
			if (other.p_iva != null)	return false;
		} else
			if (!p_iva.equals(other.p_iva))	return false;

		if (cod_fiscale == null) {
			if (other.cod_fiscale != null)	return false;
		} else
			if (!cod_fiscale.equals(other.cod_fiscale))	return false;

		if (e_mail == null) {
			if (other.e_mail != null)	return false;
		} else
			if (!e_mail.equals(other.e_mail))	return false;

		if (tipo_biblioteca == null) {
			if (other.tipo_biblioteca != null)	return false;
		} else
			if (!tipo_biblioteca.equals(other.tipo_biblioteca))	return false;

		if (paese == null) {
			if (other.paese != null)	return false;
		} else
			if (!paese.equals(other.paese))	return false;

		if (provincia == null) {
			if (other.provincia != null)	return false;
		} else
			if (!provincia.equals(other.provincia))	return false;

		if (cod_bib_cs == null) {
			if (other.cod_bib_cs != null)	return false;
		} else
			if (!cod_bib_cs.equals(other.cod_bib_cs))	return false;

		if (cod_bib_ut == null) {
			if (other.cod_bib_ut != null)	return false;
		} else
			if (!cod_bib_ut.equals(other.cod_bib_ut))	return false;

		if (other.cod_utente != cod_utente)	return false;

		if (flag_bib == null) {
			if (other.flag_bib != null)	return false;
		} else
			if (!flag_bib.equals(other.flag_bib))	return false;

		if (localita == null) {
			if (other.localita != null)	return false;
		} else
			if (!localita.equals(other.localita))	return false;

		if (chiave_bib == null) {
			if (other.chiave_bib != null)	return false;
		} else
			if (!chiave_bib.equals(other.chiave_bib))	return false;

		if (chiave_ente == null) {
			if (other.chiave_ente != null)	return false;
		} else
			if (!chiave_ente.equals(other.chiave_ente))	return false;

		if (flCanc == null) {
			if (other.flCanc != null)	return false;
		} else
			if (!flCanc.equals(other.flCanc))	return false;

		if (priorita != other.priorita)
			return false;

		return true;
	}

	public boolean isAbilitata() {
		return abilitata;
	}

	public void setAbilitata(boolean abilitata) {
		this.abilitata = abilitata;
	}

	public String getGruppo() {
		return gruppo;
	}

	public void setGruppo(String gruppo) {
		this.gruppo = gruppo;
	}

	public int getPrg() {
		return prg;
	}

	public void setPrg(int prg) {
		this.prg = prg;
	}

	public String getCodSistemaMetropolitano() {
		return codSistemaMetropolitano;
	}

	public void setCodSistemaMetropolitano(String codSistemaMetropolitano) {
		this.codSistemaMetropolitano = trimAndSet(codSistemaMetropolitano);
	}

	public int getPriorita() {
		return priorita;
	}

	public void setPriorita(int priorita) {
		this.priorita = priorita;
	}

	//almaviva5_20100202 #3492
	public String getIndirizzoComposto() {
		String indirizzoComposto = "";

		if (isFilled(paese))
			indirizzoComposto += " " + paese.trim();

		if (isFilled(provincia))
			indirizzoComposto += " " + provincia.trim();

		if (isFilled(cap))
			indirizzoComposto += " " + cap.trim();

		if (isFilled(localita))
			indirizzoComposto += " " + localita.trim();

		if (isFilled(indirizzo))
			indirizzoComposto += " " + indirizzo.trim();

		return indirizzoComposto.trim();

	}

	@Override
	public int getRepeatableId() {
		//almaviva5_20120313 #4899
		return (cod_polo + cod_bib + nom_biblioteca).hashCode();
	}

	public String getIsil() {
		if (!isFilled(cd_ana_biblioteca))
			return "";

		//check cod paese
		String[] tokens = cd_ana_biblioteca.split("\\u002D");
		switch (tokens.length) {
		case 1:	//senza codice
			return paese + "-" + cd_ana_biblioteca;
		default:
			return cd_ana_biblioteca;
		}
	}

	@Override
	public boolean isNuovo() {
		return idBiblioteca == 0;
	}

	public boolean isILLFornitrice() {
		return ValidazioneDati.in(bibliotecaILL.getRuolo(), "E", "D");
	}

	public boolean isILLRichiedente() {
		return ValidazioneDati.in(bibliotecaILL.getRuolo(), "E", "R");
	}
	
	public boolean isIll_prestito() {
		return ValidazioneDati.in(bibliotecaILL.getTipoPrestito(), "N", "E");
	}
	
	public boolean isIll_riproduzione() {
		return ValidazioneDati.in(bibliotecaILL.getTipoDocDelivery(), "N", "E");
	}

	public BibliotecaILLVO getBibliotecaILL() {
		return bibliotecaILL;
	}

	public void setBibliotecaILL(BibliotecaILLVO bibliotecaILL) {
		this.bibliotecaILL = bibliotecaILL;
	}

}
