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
package it.iccu.sbn.ejb.vo.acquisizioni;

import gnu.trove.THashSet;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.FormulaIntroOrdineRVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaQuater;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaTerna;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConfigurazioneBOVO extends SerializableVO {

	private static final long serialVersionUID = -5555744733940065950L;

	private String ticket;
	private String codPolo;
	private String codBibl;
	private String denoBibl;

	private boolean numAutomatica;
	private boolean linguaIT=true;

	private StrutturaTerna[] listaDatiIntestazione;
	private StrutturaTerna[] listaDatiIntestazioneEng; // testi in inglese
	private StrutturaTerna[] listaDatiFineStampa;
	private StrutturaTerna[] listaDatiFineStampaEng; // testi in inglese

	private StrutturaCombo[] testoOggetto = new StrutturaCombo[6]; // distinzione per tipologia A, C, D, L,V,R (in codice2 c'è il tipo ordine non è posizionale)
	private StrutturaCombo[] testoOggettoEng = new StrutturaCombo[6]; // testi in inglese

	private StrutturaCombo[] testoIntroduttivo = new StrutturaCombo[6]; // distinzione per tipologia A, C, D, L,V,R (in codice2 c'è il tipo ordine non è posizionale)
	private StrutturaCombo[] testoIntroduttivoEng = new StrutturaCombo[6];  // testi in inglese

	private String[] areeIsbd=new String[7]; // indicazione dei numeri delle aree da considerare per il titolo (posizionale)

	private boolean flag_canc=false;
	private boolean presenzaLogoImg=false;
	private boolean presenzaFirmaImg=false;

	private String nomeLogoImg; // indicazione del file immagine del logo
	private String nomeFirmaImg; // indicazione del file immagine della firma

	private boolean presenzaPrezzo=false; // valido per tutti gli ordini
	private boolean indicaRistampa=false;
	private boolean etichettaProtocollo=false; // indicazione del protocollo (da aggiungere manualmente)
	private boolean etichettaDataProtocollo=false; //indicazione della data di protocollo (da aggiungere manualmente)
	private String tipoRinnovo; // O=originario, P=precedente, N=nessuno
 	private StrutturaTerna[] rinnovoOrigine; // chiave ordine
	private Timestamp dataUpd;
	private String utente;

	List<FormulaIntroOrdineRVO> formulaIntroOrdineR;


	//costruttore
	public ConfigurazioneBOVO() {
		super();
	};

	public ConfigurazioneBOVO (StrutturaTerna[] listaDatiInt, StrutturaTerna[] listaDatiFineStp)
	{
		this.listaDatiIntestazione = listaDatiInt;
		this.listaDatiFineStampa = listaDatiFineStp;
	}

	public ConfigurazioneBOVO (String codP, String codB, boolean numAuto,  StrutturaTerna[] listaDatiInt, StrutturaTerna[] listaDatiFineStp)
	throws Exception {

		this.codPolo = codP;
		this.codBibl = codB;
		this.numAutomatica = numAuto;
		this.listaDatiIntestazione = listaDatiInt;
		this.listaDatiFineStampa = listaDatiFineStp;

}


	public ConfigurazioneBOVO(OrdiniVO o) {
		this.codPolo = o.getCodPolo();
		this.codBibl = o.getCodBibl();
		this.ticket = o.getTicket();
	}

	public String getCodBibl() {
		return codBibl;
	}


	public void setCodBibl(String codBibl) {
		this.codBibl = codBibl;
	}


	public String getCodPolo() {
		return codPolo;
	}


	public void setCodPolo(String codPolo) {
		this.codPolo = codPolo;
	}





	public boolean isNumAutomatica() {
		return numAutomatica;
	}


	public void setNumAutomatica(boolean numAutomatica) {
		this.numAutomatica = numAutomatica;
	}
	public StrutturaTerna[] getListaDatiFineStampa() {
		return listaDatiFineStampa;
	}
	public void setListaDatiFineStampa(StrutturaTerna[] listaDatiFineStampa) {
		this.listaDatiFineStampa = listaDatiFineStampa;
	}
	public StrutturaTerna[] getListaDatiIntestazione() {
		return listaDatiIntestazione;
	}
	public void setListaDatiIntestazione(StrutturaTerna[] listaDatiIntestazione) {
		this.listaDatiIntestazione = listaDatiIntestazione;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public boolean isFlag_canc() {
		return flag_canc;
	}
	public void setFlag_canc(boolean flag_canc) {
		this.flag_canc = flag_canc;
	}
	public String getUtente() {
		return utente;
	}
	public void setUtente(String utente) {
		this.utente = utente;
	}
	public String[] getAreeIsbd() {
		return areeIsbd;
	}
	public void setAreeIsbd(String[] areeIsbd) {
		this.areeIsbd = areeIsbd;
	}
	public boolean isEtichettaDataProtocollo() {
		return etichettaDataProtocollo;
	}
	public void setEtichettaDataProtocollo(boolean etichettaDataProtocollo) {
		this.etichettaDataProtocollo = etichettaDataProtocollo;
	}
	public boolean isEtichettaProtocollo() {
		return etichettaProtocollo;
	}
	public void setEtichettaProtocollo(boolean etichettaProtocollo) {
		this.etichettaProtocollo = etichettaProtocollo;
	}
	public boolean isIndicaRistampa() {
		return indicaRistampa;
	}
	public void setIndicaRistampa(boolean indicaRistampa) {
		this.indicaRistampa = indicaRistampa;
	}
	public StrutturaTerna[] getListaDatiFineStampaEng() {
		return listaDatiFineStampaEng;
	}
	public void setListaDatiFineStampaEng(StrutturaTerna[] listaDatiFineStampaEng) {
		this.listaDatiFineStampaEng = listaDatiFineStampaEng;
	}
	public StrutturaTerna[] getListaDatiIntestazioneEng() {
		return listaDatiIntestazioneEng;
	}
	public void setListaDatiIntestazioneEng(StrutturaTerna[] listaDatiIntestazioneEng) {
		this.listaDatiIntestazioneEng = listaDatiIntestazioneEng;
	}
	public String getNomeFirmaImg() {
		return nomeFirmaImg;
	}
	public void setNomeFirmaImg(String nomeFirmaImg) {
		this.nomeFirmaImg = nomeFirmaImg;
	}
	public String getNomeLogoImg() {
		return nomeLogoImg;
	}
	public void setNomeLogoImg(String nomeLogoImg) {
		this.nomeLogoImg = nomeLogoImg;
	}
	public boolean isPresenzaFirmaImg() {
		return presenzaFirmaImg;
	}
	public void setPresenzaFirmaImg(boolean presenzaFirmaImg) {
		this.presenzaFirmaImg = presenzaFirmaImg;
	}
	public boolean isPresenzaLogoImg() {
		return presenzaLogoImg;
	}
	public void setPresenzaLogoImg(boolean presenzaLogoImg) {
		this.presenzaLogoImg = presenzaLogoImg;
	}
	public boolean isPresenzaPrezzo() {
		return presenzaPrezzo;
	}
	public void setPresenzaPrezzo(boolean presenzaPrezzo) {
		this.presenzaPrezzo = presenzaPrezzo;
	}
	public StrutturaTerna[] getRinnovoOrigine() {
		return rinnovoOrigine;
	}
	public void setRinnovoOrigine(StrutturaTerna[] rinnovoOrigine) {
		this.rinnovoOrigine = rinnovoOrigine;
	}
	public String getTipoRinnovo() {
		return tipoRinnovo;
	}
	public void setTipoRinnovo(String tipoRinnovo) {
		this.tipoRinnovo = tipoRinnovo;
	}
	public boolean isLinguaIT() {
		return linguaIT;
	}
	public void setLinguaIT(boolean linguaIT) {
		this.linguaIT = linguaIT;
	}
	public StrutturaCombo[] getTestoOggetto() {
		return testoOggetto;
	}
	public void setTestoOggetto(StrutturaCombo[] testoOggetto) {
		this.testoOggetto = testoOggetto;
	}
	public StrutturaCombo[] getTestoOggettoEng() {
		return testoOggettoEng;
	}
	public void setTestoOggettoEng(StrutturaCombo[] testoOggettoEng) {
		this.testoOggettoEng = testoOggettoEng;
	}

	public StrutturaCombo[] getTestoIntroduttivo() {
		return testoIntroduttivo;
	}
	public void setTestoIntroduttivo(StrutturaCombo[] testoIntroduttivo) {
		this.testoIntroduttivo = testoIntroduttivo;
	}
	public StrutturaCombo[] getTestoIntroduttivoEng() {
		return testoIntroduttivoEng;
	}
	public void setTestoIntroduttivoEng(StrutturaCombo[] testoIntroduttivoEng) {
		this.testoIntroduttivoEng = testoIntroduttivoEng;
	}
	public String  preparaStringa(StrutturaCombo[] testoIntr, StrutturaCombo[] testoIntrEng,String tipoCampo ) {
		String formulaI="";
		int numIta=0;
		int numEng=0;
		StrutturaCombo ele;
		try
		{
			ele=new StrutturaCombo("","");
	 	} catch (Exception e) {
			e.printStackTrace();
	 	}
		if (tipoCampo.equals("I"))
		{
			numIta=this.getTestoIntroduttivo().length;
			numEng=this.getTestoIntroduttivoEng().length;
		}
		if (tipoCampo.equals("O"))
		{
			numIta=this.getTestoOggetto().length;
			numEng=this.getTestoOggettoEng().length;
		}

		for (int s=0; s<numIta; s++)
		{
			if (tipoCampo.equals("I"))
			{
				ele=this.getTestoIntroduttivo()[s];
			}
			else  // (tipoCampo.equals("O"))
			{
				ele=this.getTestoOggetto()[s];
			}
			if(ele!=null)
			{
				formulaI=formulaI+ "ITA" + ele.getCodice() + ele.getDescrizione() +"|";
			}
		}
		for (int v=0; v<numEng; v++)
		{
			if (tipoCampo.equals("I"))
			{
				ele=this.getTestoIntroduttivoEng()[v];
			}
			else //if (tipoCampo.equals("O"))
			{
				ele=this.getTestoOggettoEng()[v];
			}

			if(ele!=null)
			{
				formulaI=formulaI+ "ENG" + ele.getCodice() + ele.getDescrizione()+"|";
			}
		}
		return formulaI;
	}

	public StrutturaQuater[]  assemblaFormulaIntro(StrutturaCombo[] testoIta, StrutturaCombo[] testoEng)
	{
		StrutturaQuater[] assemblato=null;
		List<StrutturaQuater> preAssemblato=new ArrayList<StrutturaQuater>();
		int conta=0;
		int contaSel=0;

		try
		{
			for (int r=0;  r < testoIta.length ; r++)
			{
				if (testoIta[r]!=null )
				{
					conta=conta+1;
					contaSel=conta;
					preAssemblato.add(new StrutturaQuater(String.valueOf(contaSel), testoIta[r].getDescrizione(), "ITA", testoIta[r].getCodice()));
				}
			}
			for (int s=0;  s < testoEng.length ; s++)
			{
				if (testoEng[s]!=null )
				{
					conta=conta+1;
					contaSel=conta;
					preAssemblato.add(new StrutturaQuater(String.valueOf(contaSel), testoEng[s].getDescrizione(), "ENG", testoEng[s].getCodice()));
				}

			}
			assemblato=new StrutturaQuater[conta];

			for (int t=0;  t < conta ; t++)
			{
				assemblato [t] = preAssemblato.get(t);
			}
	 	} catch (Exception e) {
			e.printStackTrace();
	 	}

		return assemblato;
	}
	public void  leggiStringa(String campoRecord, String tipoCampo) {
		// riceve la stringa memorizzata nel campo del record e la suddivide in array distinti per italiano e inglese
		// tipo campo: I (INTRODUZIONE), O (OGGETTO)
		String[] campoRicevente=campoRecord.split("\\|");

		String lingua="";
		String codTipo="";
		String testo="";

		List<StrutturaCombo> arrayIta=new ArrayList<StrutturaCombo>();
		List<StrutturaCombo> arrayEng=new ArrayList<StrutturaCombo>();

		StrutturaCombo[] testoIta=new StrutturaCombo[6]; // distinzione per tipologia A, C, D, L,V,R (in codice2 c'è il tipo ordine non è posizionale)
		StrutturaCombo[] testoEng=new StrutturaCombo[6];  // testi in inglese


		for (int s=0; s<campoRicevente.length; s++)
		{
			if(campoRicevente[s]!=null && campoRicevente[s].trim().length()>4 )
			{
				lingua=campoRicevente[s].substring(0,3);
				codTipo=campoRicevente[s].substring(3,4);
				testo=campoRicevente[s].substring(4,campoRicevente[s].length());
				// ita
				if (lingua.equals("ITA"))
				{
					try
					{
						arrayIta.add(new StrutturaCombo(codTipo,testo) );
				 	} catch (Exception e) {
						e.printStackTrace();
				 	}
				}
				// ENG
				if (lingua.equals("ENG"))
				{
					try
					{
						arrayEng.add(new StrutturaCombo(codTipo,testo) );
				 	} catch (Exception e) {
						e.printStackTrace();
				 	}
				}
			}
		}

		for (int r=0;  r < arrayIta.size() && r<6; r++)
		{
			testoIta [r] = arrayIta.get(r);
		}
		for (int r=0;  r < arrayEng.size() && r<6; r++)
		{
			testoEng [r] = arrayEng.get(r);
		}
		if (tipoCampo.equals("I"))
		{
			this.setTestoIntroduttivo(testoIta);
			this.setTestoIntroduttivoEng(testoEng);
		}
		if (tipoCampo.equals("O"))
		{
			this.setTestoOggetto(testoIta);
			this.setTestoOggettoEng(testoEng);
		}

		return ;
	}

	public String leggiIntro(String lingua, String tipoOrd, String cd_tipo_lav) {
		String intro = "";

		// almaviva5_20121119 evolutive google
		if (ValidazioneDati.equals(tipoOrd, "R") && isFilled(cd_tipo_lav) && isFilled(formulaIntroOrdineR)) {
			for (FormulaIntroOrdineRVO fio : formulaIntroOrdineR) {
				if (ValidazioneDati.equals(fio.getLang(), lingua) &&
						ValidazioneDati.equals(fio.getCd_tipo_lav(), cd_tipo_lav) )
					return fio.getIntro();
			}
		}

		StrutturaCombo[] ele = new StrutturaCombo[0];

		if (lingua.equals("ITA"))
			ele = this.getTestoIntroduttivo();

		if (lingua.equals("ENG"))
			ele = this.getTestoIntroduttivoEng();

		for (int r = 0; r < ele.length && r < 6; r++) {
			if (ele[r] != null && ele[r].getCodice() != null && tipoOrd != null
					&& tipoOrd.equals(ele[r].getCodice())) {
				if (ele[r].getDescrizione() != null
						&& ele[r].getDescrizione().trim().length() > 0)
					intro = ele[r].getDescrizione();
				break;
			}
		}

		return intro;
	}

	public String  leggiOgg(String lingua, String tipoOrd ) {
		String ogg="";
		StrutturaCombo[] ele=new StrutturaCombo[0];

		if (lingua.equals("ITA"))
		{
			ele=this.getTestoOggetto();
		}
		if (lingua.equals("ENG"))
		{
			ele=this.getTestoOggettoEng();
		}

		for (int r=0;  r < ele.length && r<6; r++)
		{
			if (ele[r]!=null &&  ele[r].getCodice()!=null && tipoOrd!=null && tipoOrd.equals(ele[r].getCodice()))
			{
				if (ele[r].getDescrizione()!=null && ele[r].getDescrizione().trim().length()>0)
				 ogg=ele[r].getDescrizione();
				break;
			}
		}
		return ogg;
	}

	public Boolean leggiAree(String area) { // T=titolo, E=edizione, N=numerazione, P=pubblicazione
		Boolean presenza=false;
		for (int r=0;  r < this.areeIsbd.length && r<7; r++)
		{
			if (area!=null && r==0 && area.equals("T"))
			{
				//almaviva5_20140709 #4527 il titolo va sempre stampato
				// if (this.areeIsbd[0]!=null &&  this.areeIsbd[0].equals("1"))
				// {
					 presenza=true;
				// }
			}
			if (area!=null && r==1 && area.equals("E"))
			{
				 if (this.areeIsbd[1]!=null && this.areeIsbd[1].equals("1"))
				 {
					 presenza=true;
				 }
			}
			if (area!=null && r==2 && area.equals("N"))
			{
				 if (this.areeIsbd[2]!=null && this.areeIsbd[2].equals("1"))
				 {
					 presenza=true;
				 }
			}
			if (area!=null && r==3 && area.equals("P"))
			{
				 if (this.areeIsbd[3]!=null && this.areeIsbd[3].equals("1"))
				 {
					 presenza=true;
				 }
			}
			// dovranno essere aggiunti (perchè già gestiti in stampa) i codici J=issn e M=ismn
		}
		return presenza;
	}


	public void caricaAree (String titolo, String edizione, String numerazione, String pubblicazione) {
		String[] strAreeIsbd=new String[7];
		strAreeIsbd[0]="0"; // titolo
		strAreeIsbd[1]="0";
		strAreeIsbd[2]="0";
		strAreeIsbd[3]="0";
		strAreeIsbd[4]="0";
		strAreeIsbd[5]="0";
		strAreeIsbd[6]="0";
		if (titolo!=null && titolo.trim().equals("S"))
		{
			strAreeIsbd[0]="1";
		}
		if (edizione!=null && edizione.trim().equals("S"))
		{
			strAreeIsbd[1]="1";
		}
		if (numerazione!=null && numerazione.trim().equals("S"))
		{
			strAreeIsbd[2]="1";
		}
		if (pubblicazione!=null && pubblicazione.trim().equals("S"))
		{
			strAreeIsbd[3]="1";
		}
		this.setAreeIsbd(strAreeIsbd);
		return;
	}


	public void validate() throws ValidationException {
		// controllo che non ci sia più di una riga con la stessa lingua e tipo ordine
		boolean valido = true;

		StrutturaCombo[]	ele;

		ele=this.getTestoOggetto();

		for (int r=0;  r < ele.length && r<6 && ele[r]!=null && ele[r].getCodice()!=null && valido==true ; r++)
		{
			String old=ele[r].getCodice();
			for (int s=r+1;  s < ele.length && s<6 && ele[s]!=null && ele[s].getCodice()!=null ; s++)
			{
				if (ele[r].getDescrizione()==null && ele[s].getDescrizione()==null )
				{
					valido=false;
					break;
				}
				if (ele[s].getCodice().equals(old))
				{
					valido=false;
					break;
				}
			}
		}

		if (valido)
		{
			ele=this.getTestoOggettoEng();

			for (int r=0;  r < ele.length && r<6 && ele[r]!=null && ele[r].getCodice()!=null && valido==true; r++)
			{
				String old=ele[r].getCodice();
				for (int s=r+1;  s < ele.length && s<6 && ele[s]!=null && ele[s].getCodice()!=null ; s++)
				{
					if (ele[r].getDescrizione()==null && ele[s].getDescrizione()==null )
					{
						valido=false;
						break;
					}

					if (ele[s].getCodice().equals(old))
					{
						valido=false;
						break;
					}
				}
			}
		}
		if (valido)
		{
			ele=this.getTestoIntroduttivo();

			for (int r=0;  r < ele.length && r<6 && ele[r]!=null && ele[r].getCodice()!=null && valido==true; r++)
			{
				String old=ele[r].getCodice();
				for (int s=r+1;  s < ele.length && s<6 && ele[s]!=null && ele[s].getCodice()!=null ; s++)
				{
					if (ele[r].getDescrizione()==null && ele[s].getDescrizione()==null )
					{
						valido=false;
						break;
					}

					if (ele[s].getCodice().equals(old))
					{
						valido=false;
						break;
					}
				}
			}
		}
		if (valido)
		{
			ele=this.getTestoIntroduttivoEng();

			for (int r=0;  r < ele.length && r<6 && ele[r]!=null && ele[r].getCodice()!=null && valido==true; r++)
			{
				String old=ele[r].getCodice();
				for (int s=r+1;  s < ele.length && s<6 && ele[s]!=null && ele[s].getCodice()!=null ; s++)
				{
					if (ele[r].getDescrizione()==null && ele[s].getDescrizione()==null )
					{
						valido=false;
						break;
					}

					if (ele[s].getCodice().equals(old))
					{
						valido=false;
						break;
					}
				}
			}
		}

		//almaviva5_20121119 evolutive google
		if (ValidazioneDati.isFilled(formulaIntroOrdineR) ) {
			Set<FormulaIntroOrdineRVO> tmp = new THashSet<FormulaIntroOrdineRVO>();
			for (FormulaIntroOrdineRVO fio : formulaIntroOrdineR) {
				if (tmp.contains(fio))
					throw new ValidationException(SbnErrorTypes.ACQ_CONFIG_ORDINE_TIPO_LAVORAZIONE_RIPETUTO);

				if (!isFilled(fio.getIntro()))
					throw new ValidationException(SbnErrorTypes.ERROR_GENERIC_MANDATORY_FIELD, "configurazione.label.StampaBO31");

				tmp.add(fio);
			}
		}

		if (!valido)
			throw new ValidationException(SbnErrorTypes.ACQ_CONFIG_ORDINE_RIGHE_RIPETUTE);

	}
	public Timestamp getDataUpd() {
		return dataUpd;
	}
	public void setDataUpd(Timestamp dataUpd) {
		this.dataUpd = dataUpd;
	}
	public String getDenoBibl() {
		return denoBibl;
	}
	public void setDenoBibl(String denoBibl) {
		this.denoBibl = denoBibl;
	}
	public List<FormulaIntroOrdineRVO> getFormulaIntroOrdineR() {
		return formulaIntroOrdineR;
	}
	public void setFormulaIntroOrdineR(
			List<FormulaIntroOrdineRVO> formulaIntroOrdineR) {
		this.formulaIntroOrdineR = formulaIntroOrdineR;
	}

}
