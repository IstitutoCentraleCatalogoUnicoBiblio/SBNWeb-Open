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
//	SBNWeb - Rifacimento ClientServer
//		Classi di tipo VO
//		almaviva2 - Inizio Codifica Agosto 2006
package it.iccu.sbn.ejb.vo.gestionebibliografica.titolo;

import it.iccu.sbn.ejb.vo.BaseVO;

import java.util.ArrayList;
import java.util.List;

public class DettaglioTitoloMusicaVO extends BaseVO {

	// = DettaglioTitoloMusicaVO.class.hashCode();

	/**
	 *
	 */
	private static final long serialVersionUID = 6582424296505877752L;

	private String livAutSpec;

	private String elabor;
	private String orgSint;
	private String orgAnal;
	private String present;
	private String formaMusic1;
	private String formaMusic2;
	private String formaMusic3;
	private String tonalita;
	private String numOpera;
	private String numOrdine;
	private String numCatTem;
	private String dataCompIni;
	private String dataCompFin;
	private String orgSintComp;
	private String orgAnalComp;
	private String titOrdinam;
	private String titEstratto;
	private String appellativo;

	private String tipoElabor;
	private String tipoTesto;
	private String stesura;
	private String composito;
	private String palinsesto;
	private String materia;

	private String datazione;
	private String sezioni;

	private String genereRappr;
	private String annoIRappr;
	private String periodoIRappr;
	private String localitaIRappr;
	private String sedeIRappr;
	private String occasioneIRappr;
	private String noteIRappr;

	private String illustrazioni;
	private String notazioneMusicale;
	private String legatura;
	private String conservazione;
	private String presenzaPersonaggi;
	private String presenzaIncipit;

	private List<DettaglioIncipitVO> listaDettagliIncipit;


	private List<TabellaNumSTDImpronteVO> listaIncipit;
	private List<TabellaNumSTDImpronteVO> listaPersonaggi;

	// Lista degli interpreti nuovi da inserire in Polo prima di effettuare la variazione descrizione
	private List listaInterpreti;

	public DettaglioTitoloMusicaVO() {
		super();
		// TODO Auto-generated constructor stub
		this.listaPersonaggi = new ArrayList<TabellaNumSTDImpronteVO>();
		this.listaIncipit = new ArrayList<TabellaNumSTDImpronteVO>();
		this.listaDettagliIncipit = new ArrayList<DettaglioIncipitVO>();
	}

	public List getListaPersonaggi() {
		return listaPersonaggi;
	}

	public void setListaPersonaggi(List listaPersonaggi) {
		this.listaPersonaggi = listaPersonaggi;
	}

	public String getPresenzaPersonaggi() {
		return presenzaPersonaggi;
	}

	public void setPresenzaPersonaggi(String presenzaPersonaggi) {
		this.presenzaPersonaggi = presenzaPersonaggi;
	}

	public String getAppellativo() {
		return appellativo;
	}

	public void setAppellativo(String appellativo) {
		this.appellativo = appellativo;
	}

	public String getDataCompFin() {
		return dataCompFin;
	}

	public void setDataCompFin(String dataCompFin) {
		this.dataCompFin = dataCompFin;
	}

	public String getDataCompIni() {
		return dataCompIni;
	}

	public void setDataCompIni(String dataCompIni) {
		this.dataCompIni = dataCompIni;
	}

	public String getElabor() {
		return elabor;
	}

	public void setElabor(String elabor) {
		this.elabor = elabor;
	}

	public String getFormaMusic1() {
		return formaMusic1;
	}

	public void setFormaMusic1(String formaMusic1) {
		this.formaMusic1 = formaMusic1;
	}

	public String getFormaMusic2() {
		return formaMusic2;
	}

	public void setFormaMusic2(String formaMusic2) {
		this.formaMusic2 = formaMusic2;
	}

	public String getFormaMusic3() {
		return formaMusic3;
	}

	public void setFormaMusic3(String formaMusic3) {
		this.formaMusic3 = formaMusic3;
	}

	public String getNumCatTem() {
		return numCatTem;
	}

	public void setNumCatTem(String numCatTem) {
		this.numCatTem = numCatTem;
	}

	public String getNumOpera() {
		return numOpera;
	}

	public void setNumOpera(String numOpera) {
		this.numOpera = numOpera;
	}

	public String getNumOrdine() {
		return numOrdine;
	}

	public void setNumOrdine(String numOrdine) {
		this.numOrdine = numOrdine;
	}

	public String getOrgAnal() {
		return orgAnal;
	}

	public void setOrgAnal(String orgAnal) {
		this.orgAnal = orgAnal;
	}

	public String getOrgAnalComp() {
		return orgAnalComp;
	}

	public void setOrgAnalComp(String orgAnalComp) {
		this.orgAnalComp = orgAnalComp;
	}

	public String getOrgSint() {
		return orgSint;
	}

	public void setOrgSint(String orgSint) {
		this.orgSint = orgSint;
	}

	public String getOrgSintComp() {
		return orgSintComp;
	}

	public void setOrgSintComp(String orgSintComp) {
		this.orgSintComp = orgSintComp;
	}

	public String getPresent() {
		return present;
	}

	public void setPresent(String present) {
		this.present = present;
	}

	public String getTitEstratto() {
		return titEstratto;
	}

	public void setTitEstratto(String titEstratto) {
		this.titEstratto = titEstratto;
	}

	public String getTitOrdinam() {
		return titOrdinam;
	}

	public void setTitOrdinam(String titOrdinam) {
		this.titOrdinam = titOrdinam;
	}

	public String getTonalita() {
		return tonalita;
	}

	public void setTonalita(String tonalita) {
		this.tonalita = tonalita;
	}

	public String getComposito() {
		return composito;
	}

	public void setComposito(String composito) {
		this.composito = composito;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public String getPalinsesto() {
		return palinsesto;
	}

	public void setPalinsesto(String palinsesto) {
		this.palinsesto = palinsesto;
	}

	public String getStesura() {
		return stesura;
	}

	public void setStesura(String stesura) {
		this.stesura = stesura;
	}

	public String getTipoTesto() {
		return tipoTesto;
	}

	public void setTipoTesto(String tipoTesto) {
		this.tipoTesto = tipoTesto;
	}

	public String getTipoElabor() {
		return tipoElabor;
	}

	public void setTipoElabor(String tipoElabor) {
		this.tipoElabor = tipoElabor;
	}

	public String getGenereRappr() {
		return genereRappr;
	}

	public void setGenereRappr(String genereRappr) {
		this.genereRappr = genereRappr;
	}

	public String getDatazione() {
		return datazione;
	}

	public void setDatazione(String datazione) {
		//almaviva5_20200128 #7335
		this.datazione = trimOrEmpty(datazione);
	}

	public String getSezioni() {
		return sezioni;
	}

	public void setSezioni(String sezioni) {
		this.sezioni = sezioni;
	}

	public String getAnnoIRappr() {
		return annoIRappr;
	}

	public void setAnnoIRappr(String annoIRappr) {
		this.annoIRappr = annoIRappr;
	}

	public String getLocalitaIRappr() {
		return localitaIRappr;
	}

	public void setLocalitaIRappr(String localitaIRappr) {
		this.localitaIRappr = localitaIRappr;
	}

	public String getNoteIRappr() {
		return noteIRappr;
	}

	public void setNoteIRappr(String noteIRappr) {
		this.noteIRappr = noteIRappr;
	}

	public String getOccasioneIRappr() {
		return occasioneIRappr;
	}

	public void setOccasioneIRappr(String occasioneIRappr) {
		this.occasioneIRappr = occasioneIRappr;
	}

	public String getPeriodoIRappr() {
		return periodoIRappr;
	}

	public void setPeriodoIRappr(String periodoIRappr) {
		this.periodoIRappr = periodoIRappr;
	}

	public String getSedeIRappr() {
		return sedeIRappr;
	}

	public void setSedeIRappr(String sedeIRappr) {
		this.sedeIRappr = sedeIRappr;
	}

	public List getListaIncipit() {
		return listaIncipit;
	}

	public void setListaIncipit(List listaIncipit) {
		this.listaIncipit = listaIncipit;
	}

	public String getPresenzaIncipit() {
		return presenzaIncipit;
	}

	public void setPresenzaIncipit(String presenzaIncipit) {
		this.presenzaIncipit = presenzaIncipit;
	}

	public List addListaPersonaggi(TabellaNumSTDImpronteVO tabPer) {
		listaPersonaggi.add(tabPer);
		return listaPersonaggi;
	}

	public List addListaIncipit(TabellaNumSTDImpronteVO tabInc) {
		listaIncipit.add(tabInc);
		return listaIncipit;
	}

	public String getConservazione() {
		return conservazione;
	}

	public void setConservazione(String conservazione) {
		this.conservazione = conservazione;
	}

	public String getIllustrazioni() {
		return illustrazioni;
	}

	public void setIllustrazioni(String illustrazioni) {
		this.illustrazioni = illustrazioni;
	}

	public String getLegatura() {
		return legatura;
	}

	public void setLegatura(String legatura) {
		this.legatura = legatura;
	}

	public String getNotazioneMusicale() {
		return notazioneMusicale;
	}

	public void setNotazioneMusicale(String notazioneMusicale) {
		this.notazioneMusicale = notazioneMusicale;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DettaglioTitoloMusicaVO other = (DettaglioTitoloMusicaVO) obj;
		if (annoIRappr == null) {
			if (other.annoIRappr != null)
				return false;
		} else if (!annoIRappr.equals(other.annoIRappr))
			return false;
		if (appellativo == null) {
			if (other.appellativo != null)
				return false;
		} else if (!appellativo.equals(other.appellativo))
			return false;
		if (composito == null) {
			if (other.composito != null)
				return false;
		} else if (!composito.equals(other.composito))
			return false;
		if (conservazione == null) {
			if (other.conservazione != null)
				return false;
		} else if (!conservazione.equals(other.conservazione))
			return false;
		if (dataCompFin == null) {
			if (other.dataCompFin != null)
				return false;
		} else if (!dataCompFin.equals(other.dataCompFin))
			return false;
		if (dataCompIni == null) {
			if (other.dataCompIni != null)
				return false;
		} else if (!dataCompIni.equals(other.dataCompIni))
			return false;
		if (datazione == null) {
			if (other.datazione != null)
				return false;
		} else if (!datazione.equals(other.datazione))
			return false;
		if (elabor == null) {
			if (other.elabor != null)
				return false;
		} else if (!elabor.equals(other.elabor))
			return false;
		if (formaMusic1 == null) {
			if (other.formaMusic1 != null)
				return false;
		} else if (!formaMusic1.equals(other.formaMusic1))
			return false;
		if (formaMusic2 == null) {
			if (other.formaMusic2 != null)
				return false;
		} else if (!formaMusic2.equals(other.formaMusic2))
			return false;
		if (formaMusic3 == null) {
			if (other.formaMusic3 != null)
				return false;
		} else if (!formaMusic3.equals(other.formaMusic3))
			return false;
		if (genereRappr == null) {
			if (other.genereRappr != null)
				return false;
		} else if (!genereRappr.equals(other.genereRappr))
			return false;
		if (illustrazioni == null) {
			if (other.illustrazioni != null)
				return false;
		} else if (!illustrazioni.equals(other.illustrazioni))
			return false;
		if (legatura == null) {
			if (other.legatura != null)
				return false;
		} else if (!legatura.equals(other.legatura))
			return false;
		if (localitaIRappr == null) {
			if (other.localitaIRappr != null)
				return false;
		} else if (!localitaIRappr.equals(other.localitaIRappr))
			return false;
		if (materia == null) {
			if (other.materia != null)
				return false;
		} else if (!materia.equals(other.materia))
			return false;
		if (notazioneMusicale == null) {
			if (other.notazioneMusicale != null)
				return false;
		} else if (!notazioneMusicale.equals(other.notazioneMusicale))
			return false;
		if (noteIRappr == null) {
			if (other.noteIRappr != null)
				return false;
		} else if (!noteIRappr.equals(other.noteIRappr))
			return false;
		if (numCatTem == null) {
			if (other.numCatTem != null)
				return false;
		} else if (!numCatTem.equals(other.numCatTem))
			return false;
		if (numOpera == null) {
			if (other.numOpera != null)
				return false;
		} else if (!numOpera.equals(other.numOpera))
			return false;
		if (numOrdine == null) {
			if (other.numOrdine != null)
				return false;
		} else if (!numOrdine.equals(other.numOrdine))
			return false;
		if (occasioneIRappr == null) {
			if (other.occasioneIRappr != null)
				return false;
		} else if (!occasioneIRappr.equals(other.occasioneIRappr))
			return false;
		if (orgAnal == null) {
			if (other.orgAnal != null)
				return false;
		} else if (!orgAnal.equals(other.orgAnal))
			return false;
		if (orgAnalComp == null) {
			if (other.orgAnalComp != null)
				return false;
		} else if (!orgAnalComp.equals(other.orgAnalComp))
			return false;
		if (orgSint == null) {
			if (other.orgSint != null)
				return false;
		} else if (!orgSint.equals(other.orgSint))
			return false;
		if (orgSintComp == null) {
			if (other.orgSintComp != null)
				return false;
		} else if (!orgSintComp.equals(other.orgSintComp))
			return false;
		if (palinsesto == null) {
			if (other.palinsesto != null)
				return false;
		} else if (!palinsesto.equals(other.palinsesto))
			return false;
		if (periodoIRappr == null) {
			if (other.periodoIRappr != null)
				return false;
		} else if (!periodoIRappr.equals(other.periodoIRappr))
			return false;
		if (present == null) {
			if (other.present != null)
				return false;
		} else if (!present.equals(other.present))
			return false;

		if (presenzaIncipit == null) {
			if (other.presenzaIncipit != null)
				return false;
		} else if (presenzaIncipit.equals("") && other.presenzaIncipit.equals("")) {
			// MODIFICA 23 SETTEMBRE 2015 Si deve controllare il caso in cui entrambi i valori siano uguali a vuoto cioè "";
			// in questo caso non si ritorna il valore false che ha il significato di differenza presente.
		} else if (presenzaIncipit.equals("SI") && other.presenzaIncipit.equals("SI")) {
			// MODIFICA 23 SETTEMBRE 2015 Si deve controllare il caso in cui entrambi i valori siano uguali a "SI";
			// in questo caso si controlla la lunghezza dell'array contenente gli Incipit; se uguali OK altrimenti ritorna il valore false
			if (listaDettagliIncipit.size() != other.listaDettagliIncipit.size()) {
				return false;
			}
		} else {
			//if (!presenzaIncipit.equals(other.presenzaIncipit))
			return false;
		}

		if (presenzaPersonaggi == null) {
			if (other.presenzaPersonaggi != null)
				return false;
		} else if (!presenzaPersonaggi.equals(other.presenzaPersonaggi))
			return false;
		if (sedeIRappr == null) {
			if (other.sedeIRappr != null)
				return false;
		} else if (!sedeIRappr.equals(other.sedeIRappr))
			return false;
		if (sezioni == null) {
			if (other.sezioni != null)
				return false;
		} else if (!sezioni.equals(other.sezioni))
			return false;
		if (stesura == null) {
			if (other.stesura != null)
				return false;
		} else if (!stesura.equals(other.stesura))
			return false;
		if (tipoElabor == null) {
			if (other.tipoElabor != null)
				return false;
		} else if (!tipoElabor.equals(other.tipoElabor))
			return false;

		// Inizio almaviva2 modifica 26 febbraio 2010 - Intervento interno perchè il tipo testo diviene Vuoto in fase in impostazione
		// della mappa quindi il confronto deve essere cambiato (inserito l'AND)
		if (tipoTesto == null) {
			if (other.tipoTesto != null && other.tipoTesto != "")
				return false;
		} else if (!tipoTesto.equals(other.tipoTesto))
			return false;
		if (titEstratto == null) {
			if (other.titEstratto != null)
				return false;
		} else if (!titEstratto.equals(other.titEstratto))
			return false;
		if (titOrdinam == null) {
			if (other.titOrdinam != null)
				return false;
		} else if (!titOrdinam.equals(other.titOrdinam))
			return false;
		if (tonalita == null) {
			if (other.tonalita != null)
				return false;
		} else if (!tonalita.equals(other.tonalita))
			return false;

		if (!listEquals(this.listaPersonaggi, other.listaPersonaggi,
				TabellaNumSTDImpronteVO.class))
			return false;

		if (!listEquals(this.listaIncipit, other.listaIncipit,
				TabellaNumSTDImpronteVO.class))
			return false;

		return true;
	}

	public List<DettaglioIncipitVO> getListaDettagliIncipit() {
		return listaDettagliIncipit;
	}

	public void setListaDettagliIncipit(
			List<DettaglioIncipitVO> listaDettagliIncipit) {
		this.listaDettagliIncipit = listaDettagliIncipit;
	}


	public List addListaDettagliIncipit(DettaglioIncipitVO tabDetInc) {
		listaDettagliIncipit.add(tabDetInc);
		return listaDettagliIncipit;
	}


	public String getLivAutSpec() {
		return livAutSpec;
	}

	public void setLivAutSpec(String livAutSpec) {
		this.livAutSpec = livAutSpec;
	}

	public List getListaInterpreti() {
		return listaInterpreti;
	}

	public void setListaInterpreti(List listaInterpreti) {
		this.listaInterpreti = listaInterpreti;
	}

}
