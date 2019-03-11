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
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UtenteBibliotecaVO extends BaseVO {

	private static final long serialVersionUID = -5691903255537544874L;

	public static final char SMS_SU_FISSO = 'F';
	public static final char SMS_SU_MOBILE = 'M';
	public static final char NO_SMS = 'X';
	public static final String DEFAULT_PASSWORD = "12345678";

	public static boolean NEW = true;
	public static boolean OLD = false;
	private boolean nuovoUte;
	private String parametro = null;
	private String codBibSer;
	private String codPoloSer;
	// campi Tabelle
	private String codPolo;
	private String codiceBiblioteca;

	private String codiceUtente;
	private String cognome;
	private String nome;
	private String password;
	private String idUtente;
	// timbro utente anagrafica Utente biblio è ereditato dal base
	private Timestamp tsInsAna;
	private String uteInsAna;
	private Timestamp tsVarAna;
	private String uteVarAna;
	private String flCancAna;
	private String changePassword;
	private String idUtenteBiblioteca;
	private char tipoSMS = UtenteBibliotecaVO.NO_SMS;
	private List<DocumentoVO> documento = new ArrayList<DocumentoVO>();
	private AnagrafeVO anagrafe = new AnagrafeVO();
	private AutorizzazioniVO autorizzazioni = new AutorizzazioniVO();
	private BiblioPoloVO bibliopolo = new BiblioPoloVO();
	private ProfessioniVO professione = new ProfessioniVO();
	// gestisce i blocchi e l'ordinamento liste
	private String tipoOrdinamento;
	// private int elemPerBlocchi;
	private List<ComboCodDescVO> lstTipiOrdinamento;
	private String tipoIscrizione="N";
	// aggiunto per il tesserino
	private String descrBiblioteca;
	private Timestamp lastAccess;
	private boolean importato=false;
	private String chiaveUte="";

	private String tipoUtente;


	public UtenteBibliotecaVO() {
		super();
	}

	public List<ComboCodDescVO> getLstTipiOrdinamento() {
		return lstTipiOrdinamento;
	}

	public void setLstTipiOrdinamento(List<ComboCodDescVO> lstTipiOrdinamento) {
		this.lstTipiOrdinamento = lstTipiOrdinamento;
	}

	public String getTipoOrdinamento() {
		return tipoOrdinamento;
	}

	public void setTipoOrdinamento(String tipoOrdinamento) {
		this.tipoOrdinamento = tipoOrdinamento;
	}

	public AnagrafeVO getAnagrafe() {
		return anagrafe;
	}

	public void setAnagrafe(AnagrafeVO anagrafe) {
		this.anagrafe = anagrafe;
	}

	public AutorizzazioniVO getAutorizzazioni() {
		return autorizzazioni;
	}

	public void setAutorizzazioni(AutorizzazioniVO autorizzazioni) {
		this.autorizzazioni = autorizzazioni;
	}

	public BiblioPoloVO getBibliopolo() {
		return bibliopolo;
	}

	public void setBibliopolo(BiblioPoloVO bibliopolo) {
		this.bibliopolo = bibliopolo;
	}

	public String getCodiceBiblioteca() {
		return codiceBiblioteca;
	}

	public void setCodiceBiblioteca(String codiceBiblioteca) {
		this.codiceBiblioteca = codiceBiblioteca;
	}

	public String getCodiceUtente() {
		return codiceUtente;
	}

	public void setCodiceUtente(String codiceUtente) {
		this.codiceUtente = trimOrEmpty(codiceUtente);
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = trimOrEmpty(cognome);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = trimOrEmpty(nome);
	}

	public String getCognomeNome() {
		return trimOrEmpty(cognome + " " + nome);
	}

	public List<DocumentoVO> getDocumento() {
		if (isFilled(documento))
			return documento;

		try {
			this.documento.add(new DocumentoVO("", "", ""));
			this.documento.add(new DocumentoVO("", "", ""));
			this.documento.add(new DocumentoVO("", "", ""));
			this.documento.add(new DocumentoVO("", "", ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return documento;
	}

	public void setDocumento(List<DocumentoVO> documento) {
		this.documento = documento;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = trimAndSet(password);
	}

	public ProfessioniVO getProfessione() {
		return professione;
	}

	public void setProfessione(ProfessioniVO professione) {
		this.professione = professione;
	}

	public boolean isNuovoUte() {
		return nuovoUte;
	}

	public void setNuovoUte(boolean nuovoUte) {
		this.nuovoUte = nuovoUte;
	}

	public void clearUtenteBiblioteca() throws Exception {
		this.cognome = "";
		this.nome = "";
		this.codiceBiblioteca = "";
		this.codiceUtente = "";
		this.password = "";
		this.idUtente = "";
		this.anagrafe.clear();
		this.autorizzazioni.clear();
		this.bibliopolo.clear();
		this.professione.clear();
	}

	public String getCodBibSer() {
		return codBibSer;
	}

	public void setCodBibSer(String codBibSer) {
		this.codBibSer = codBibSer;
	}

	public String getCodPolo() {
		return codPolo;
	}

	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final UtenteBibliotecaVO other = (UtenteBibliotecaVO) obj;
		if (anagrafe == null) {
			if (other.anagrafe != null)
				return false;
		} else if (!anagrafe.equals(other.anagrafe))
			return false;
		if (autorizzazioni == null) {
			if (other.autorizzazioni != null)
				return false;
		} else if (!autorizzazioni.equals(other.autorizzazioni))
			return false;
		if (bibliopolo == null) {
			if (other.bibliopolo != null)
				return false;
		} else if (!bibliopolo.equals(other.bibliopolo))
			return false;
		if (codBibSer == null) {
			if (other.codBibSer != null)
				return false;
		} else if (!codBibSer.equals(other.codBibSer))
			return false;
		if (codPolo == null) {
			if (other.codPolo != null)
				return false;
		} else if (!codPolo.equals(other.codPolo))
			return false;
		if (codiceBiblioteca == null) {
			if (other.codiceBiblioteca != null)
				return false;
		} else if (!codiceBiblioteca.equals(other.codiceBiblioteca))
			return false;
		if (codiceUtente == null) {
			if (other.codiceUtente != null)
				return false;
		} else if (!codiceUtente.equals(other.codiceUtente))
			return false;
		if (cognome == null) {
			if (other.cognome != null)
				return false;
		} else if (!cognome.equals(other.cognome))
			return false;
		if (documento == null) {
			if (other.documento != null)
				return false;
		} else if (!listEquals(this.documento, other.documento,
				DocumentoVO.class))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (nuovoUte != other.nuovoUte)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (professione == null) {
			if (other.professione != null)
				return false;
		} else if (!professione.equals(other.professione))
			return false;
		if (idUtente == null) {
			if (other.idUtente != null)
				return false;
		} else if (!idUtente.equals(other.idUtente))
			return false;

		return (tipoSMS == other.tipoSMS);
		// return true;
	}

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	public String getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(String idUtente) {
		this.idUtente = idUtente;
	}

	public String getCodPoloSer() {
		return codPoloSer;
	}

	public void setCodPoloSer(String codPoloSer) {
		this.codPoloSer = codPoloSer;
	}

	public String getIdUtenteBiblioteca() {
		return idUtenteBiblioteca;
	}

	public void setIdUtenteBiblioteca(String idUtenteBiblioteca) {
		this.idUtenteBiblioteca = idUtenteBiblioteca;
	}

	public String getFlCancAna() {
		return flCancAna;
	}

	public void setFlCancAna(String flCancAna) {
		this.flCancAna = flCancAna;
	}

	public Timestamp getTsInsAna() {
		return tsInsAna;
	}

	public void setTsInsAna(Timestamp tsInsAna) {
		this.tsInsAna = tsInsAna;
	}

	public Timestamp getTsVarAna() {
		return tsVarAna;
	}

	public void setTsVarAna(Timestamp tsVarAna) {
		this.tsVarAna = tsVarAna;
	}

	public String getUteInsAna() {
		return uteInsAna;
	}

	public void setUteInsAna(String uteInsAna) {
		this.uteInsAna = uteInsAna;
	}

	public String getUteVarAna() {
		return uteVarAna;
	}

	public void setUteVarAna(String uteVarAna) {
		this.uteVarAna = uteVarAna;
	}

	public char getTipoSMS() {
		return tipoSMS;
	}

	public void setTipoSMS(char tipoSMS) {
		this.tipoSMS = tipoSMS;
	}

	public char getSmsSuMobile() {
		return UtenteBibliotecaVO.SMS_SU_MOBILE;
	}

	public char getSmsSuFisso() {
		return UtenteBibliotecaVO.SMS_SU_FISSO;
	}

	public char getNoSms() {
		return UtenteBibliotecaVO.NO_SMS;
	}

	public void setChangePassword(String changePassword) {
		this.changePassword = changePassword;
	}

	public String getChangePassword() {
		return changePassword;
	}

	public String getTipoIscrizione() {
		return tipoIscrizione;
	}

	public void setTipoIscrizione(String tipoIscrizione) {
		this.tipoIscrizione = tipoIscrizione;
	}

	public String getDescrBiblioteca() {
		return descrBiblioteca;
	}

	public void setDescrBiblioteca(String descrBiblioteca) {
		this.descrBiblioteca = descrBiblioteca;
	}

	public boolean isImportato() {
		return importato;
	}

	public void setImportato(boolean importato) {
		this.importato = importato;
	}

	public void setLastAccess(Timestamp lastAccess) {
		this.lastAccess = lastAccess;
	}

	public Timestamp getLastAccess() {
		return lastAccess;
	}

	public String getChiaveUte() {
		return chiaveUte;
	}

	public void setChiaveUte(String chiaveUte) {
		this.chiaveUte = trimAndSet(chiaveUte);
	}

	@Override
	public boolean isNuovo() {
		return !isFilled(idUtenteBiblioteca);
	}

	public String getTipoUtente() {
		return tipoUtente;
	}

	public void setTipoUtente(String tipoUtente) {
		this.tipoUtente = tipoUtente;
	}

}
