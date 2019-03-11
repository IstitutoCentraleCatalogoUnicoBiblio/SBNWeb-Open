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
package it.iccu.sbn.ejb.vo.gestionebibliografica.autore;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.LinkableViewVO;

import java.util.Comparator;

public class SinteticaAutoriView extends LinkableViewVO {

	private static final long serialVersionUID = 4073698868905505072L;

	private int numNotizie;
	private int progressivo;
	private String imageUrl;
	private String descrizioneLegami;
	private String livelloAutorita;
	private String parametri;
	private String tipoAutority;
	private String dataIns;

	private String vid;
	private String nome;
	private String forma;
	private String tipoNome;
	private String datazione;
	private boolean flagCondiviso;

	private String vidAccettata;
	private String nomeAccettata;
	private String tipoNomeAccettata;

	private String keyVidNome;

	private String original_nome;
	private String nomeTitColl;


	public SinteticaAutoriView() {
		super();
	}


    public static final Comparator<SinteticaAutoriView> sortListaSinteticaAut = new Comparator<SinteticaAutoriView>() {
    	public int compare(SinteticaAutoriView o1, SinteticaAutoriView o2) {
    		return o1.getProgressivo() - o2.getProgressivo();
    	}
    };


	public String getKeyVidNome() {
		return keyVidNome;
	}

	public void setKeyVidNome(String keyVidNome) {
		this.keyVidNome = keyVidNome;
	}

	public String getParametri() {
		return parametri;
	}

	public void setParametri(String parametri) {
		this.parametri = parametri;
	}

	public String getDescrizioneLegami() {
		return descrizioneLegami;
	}

	public void setDescrizioneLegami(String descrizioniLegami) {
		this.descrizioneLegami = htmlFilter(descrizioniLegami);
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLivelloAutorita() {
		return livelloAutorita;
	}

	public void setLivelloAutorita(String livelloAutorita) {
		this.livelloAutorita = livelloAutorita;
	}

	public int getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}

	public String getTipoAutority() {
		return tipoAutority;
	}

	public void setTipoAutority(String tipoAutority) {
		this.tipoAutority = tipoAutority;
	}

	public int getNumNotizie() {
		return numNotizie;
	}

	public void setNumNotizie(int numNotizie) {
		this.numNotizie = numNotizie;
	}

	public String getDataIns() {
		return dataIns;
	}

	public void setDataIns(String dataIns) {
		this.dataIns = dataIns;
	}

	public String getDatazione() {
		return datazione;
	}

	public void setDatazione(String datazione) {
		this.datazione = datazione;
	}

	public String getForma() {
		return forma;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.original_nome = nome;
		this.nome = htmlFilter(nome);
	}

	public String getTipoNome() {
		return tipoNome;
	}

	public void setTipoNome(String tipoNome) {
		this.tipoNome = tipoNome;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}


	public boolean isFlagCondiviso() {
		return flagCondiviso;
	}


	public void setFlagCondiviso(boolean flagCondiviso) {
		this.flagCondiviso = flagCondiviso;
	}


	public String getNomeAccettata() {
		return nomeAccettata;
	}


	public void setNomeAccettata(String nomeAccettata) {
		this.nomeAccettata = nomeAccettata;
	}


	public String getVidAccettata() {
		return vidAccettata;
	}


	public void setVidAccettata(String vidAccettata) {
		this.vidAccettata = vidAccettata;
	}


	public String getTipoNomeAccettata() {
		return tipoNomeAccettata;
	}


	public void setTipoNomeAccettata(String tipoNomeAccettata) {
		this.tipoNomeAccettata = tipoNomeAccettata;
	}


	public boolean isRinvio() {
		return ValidazioneDati.equalsIgnoreCase(forma, "R");
	}

	public boolean isAccettata() {
		return ValidazioneDati.equalsIgnoreCase(forma, "A");
	}

	public String getOriginal_nome() {
		return original_nome;
	}

	public String getNomeTitColl() {
		return nomeTitColl;
	}

	public void setNomeTitColl(String nomeTitColl) {
		this.nomeTitColl = nomeTitColl;
	}

}
