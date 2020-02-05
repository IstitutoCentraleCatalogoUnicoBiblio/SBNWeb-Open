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
package it.iccu.sbn.util.validation;

import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.documentofisico.AggDispVO;
import it.iccu.sbn.ejb.vo.documentofisico.CodiceVO;
import it.iccu.sbn.ejb.vo.documentofisico.CollocazioneVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsameCollocRicercaVO;
import it.iccu.sbn.ejb.vo.documentofisico.EsemplareVO;
import it.iccu.sbn.ejb.vo.documentofisico.FormatiSezioniVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloDefaultVO;
import it.iccu.sbn.ejb.vo.documentofisico.ModelloEtichetteVO;
import it.iccu.sbn.ejb.vo.documentofisico.ProvenienzaInventarioVO;
import it.iccu.sbn.ejb.vo.documentofisico.SerieVO;
import it.iccu.sbn.ejb.vo.documentofisico.SezioneCollocazioneVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRegistroTopograficoVO;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaRegistroVO;
import it.iccu.sbn.web.constant.DocumentoFisicoCostant;
import it.iccu.sbn.web.constant.ServiziConstant;

import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Pattern;


public class ValidazioniDocumentoFisico {

	public	void validaSezione (SezioneCollocazioneVO rec)
	throws ValidationException {

		if (rec.getCodPolo()== null || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
			throw new ValidationException("validaSezioneCodPoloObbligatorio", ValidationException.errore);
		}
		if (rec.getCodBib()== null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
			throw new ValidationException("validaSezioneCodBibObbligatorio", ValidationException.errore);
		}
		if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
			if (rec.getCodPolo().length()>3)	{
				throw new ValidationException("validaSezioneCodPoloEccedente", ValidationException.errore);
			}
		}
		if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
			if (rec.getCodBib().length()>3)	{
				throw new ValidationException("validaSezioneCodBibEccedente", ValidationException.errore);
			}
		}
		if (rec.getCodSezione()== null){
			throw new ValidationException("validaSezioneCodSezioneObbligatorio", ValidationException.errore);
		}
		if (rec.getCodSezione()!=null && rec.getCodSezione().trim().length()!=0) {
			if (rec.getCodSezione().length()>10) {
				throw new ValidationException("validaSezioneCodSezioneEccedente", ValidationException.errore);
			}
		}
		if (rec.getNoteSezione()!=null && rec.getNoteSezione().trim().length()!=0) {
			if (rec.getNoteSezione().length()>255) {
				throw new ValidationException("validaSezioneNoteSezioneEccedente", ValidationException.errore);
			}
		}
		if (!this.strIsNull(String.valueOf(rec.getInventariCollocati()))) {
			if (!this.strIsNumeric(String.valueOf((rec.getInventariCollocati())))) {
				throw new ValidationException("validaSezioneNumericoInventariCollocati", ValidationException.errore);
			}
			if ((String.valueOf(rec.getInventariCollocati()).length() > 9)) {
				throw new ValidationException("validaSezioneEccedenteInventariCollocati", ValidationException.errore);
			}
		}
		if (rec.getDescrSezione()!=null && rec.getDescrSezione().trim().length()!=0) {
			if (rec.getDescrSezione().length()>30) {
				throw new ValidationException("validaSezioneDescrSezioneEccedente", ValidationException.errore);
			}
		}
		if (rec.getTipoColloc()!=null && rec.getTipoColloc().trim().length()!=0) {
			if (rec.getTipoColloc().length()>1) {
				throw new ValidationException("validaSezioneTipoCollocEccedente", ValidationException.errore);
			}
		}
		if (rec.getTipoSezione()!=null && rec.getTipoSezione().trim().length()!=0) {
			if (rec.getTipoSezione().length()>1) {
				throw new ValidationException("validaSezioneTipoSezioneEccedente", ValidationException.errore);
			}
		}
		if (rec.getClassific()!=null && rec.getClassific().trim().length()!=0) {
			if (rec.getClassific().length()>30) {
				throw new ValidationException("validaSezioneClassificEccedente", ValidationException.errore);
			}
		}
		if (!this.strIsNull(String.valueOf(rec.getInventariPrevisti()))) {
			if ((String.valueOf(rec.getInventariPrevisti()).length() > 9)) {
				throw new ValidationException("validaSezioneEccedenteInventariPrevisti", ValidationException.errore);
			}
			if (!this.strIsNumeric(String.valueOf((rec.getInventariPrevisti())))) {
				throw new ValidationException("validaSezioneNumericoInventariPrevisti", ValidationException.errore);
			}
		}
		if (rec.getUteIns()!=null && rec.getUteIns().trim().length()!=0) {
			if (rec.getUteIns().length()>12) {
				throw new ValidationException("validaSezioneUteInsEccedente", ValidationException.errore);
			}
		}
		if (rec.getUteAgg()!=null && rec.getUteAgg().trim().length()!=0) {
			if (rec.getUteAgg().length()>12) {
				throw new ValidationException("validaSezioneUteAggEccedente", ValidationException.errore);
			}
		}
		if (rec.getInventariPrevisti() <  rec.getInventariCollocati()){
			throw new ValidationException("invPrevErrato",ValidationException.errore);
		}
	}

	public	void validaPoloBib (CodiceVO rec)
	throws ValidationException {

		try{
			if (rec.getCodice()== null || (rec.getCodice()!=null && rec.getCodice().trim().length()==0)){
				throw new ValidationException("codPoloObbligatorio", ValidationException.errore);
			}
			if (rec.getDescrizione()== null || (rec.getCodice()!=null && rec.getCodice().trim().length()==0)){
				throw new ValidationException("codBibObbligatorio", ValidationException.errore);
			}
			if (rec.getCodice() == null &&  rec.getDescrizione().length()!=0)	{
				if (rec.getDescrizione().length()>3)	{
					throw new ValidationException("codBibEccedente", ValidationException.errore);
				}
			}
			if (rec.getDescrizione() == null &&  rec.getDescrizione().length()!=0)	{
				if (rec.getDescrizione().length()>3)	{
					throw new ValidationException("codPoloEccedente", ValidationException.errore);
				}
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
	}

	public	void validaBidBib (CodiceVO rec)
	throws ValidationException {

		try{
			if (rec.getCodice() == null || (rec.getCodice()!=null && rec.getCodice().trim().length()==0)){
				throw new ValidationException("validaCollBidObbligatorio", ValidationException.errore);
			}
			if (rec.getCodice() !=null &&  rec.getCodice().length()!=0)	{
				if (rec.getCodice().length()>10)	{
					throw new ValidationException("validaInvBidEccedente", ValidationException.errore);
				}
			}
			if (rec.getDescrizione() != null){
				if (rec.getDescrizione() == null || (rec.getDescrizione()!=null && rec.getDescrizione().trim().length()==0)){
					throw new ValidationException("validaCollcodBibObbligatorio", ValidationException.errore);
				}
				if (rec.getDescrizione() !=null &&  rec.getDescrizione().length()!=0)	{
					if (rec.getDescrizione().length()>3)	{
						throw new ValidationException("validaInvcodBibEccedente", ValidationException.errore);
					}
				}
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
	}
	public	void validaFormatiSezioni (String codPolo, String codBib)
	throws ValidationException {

		if (codPolo ==(null) || ( codPolo.trim().length()==0)){
			throw new ValidationException("validaFormatiSezioniCodPoloObbligatorio", ValidationException.errore);
		}
		if (codBib == (null) || (codBib!=null && codBib.trim().length()==0)){
			throw new ValidationException("validaFormatiSezioniCodBibObbligatorio",ValidationException.errore);
		}
		if (codPolo !=null &&  codPolo.length()!=0)	{
			if (codPolo.length()>3)	{
				throw new ValidationException("validaFormatiSezioniCodPoloEccedente", ValidationException.errore);
			}
		}
		if (codBib !=null &&  codBib.length()!=0)	{
			if (codBib.length()>3)	{
				throw new ValidationException("validaFormatiSezioniCodBibEccedente", ValidationException.errore);
			}
		}
	}
	public	void validaFormatiSezioni (String codPolo, String codBib, String codSez)
	throws ValidationException {

		if (codPolo ==(null) || ( codPolo.trim().length()==0)){
			throw new ValidationException("validaFormatiSezioniCodPoloObbligatorio", ValidationException.errore);
		}
		if (codBib == (null) || (codBib!=null && codBib.trim().length()==0)){
			throw new ValidationException("validaFormatiSezioniCodBibObbligatorio",ValidationException.errore);
		}
		if (codPolo !=null &&  codPolo.length()!=0)	{
			if (codPolo.length()>3)	{
				throw new ValidationException("validaFormatiSezioniCodPoloEccedente", ValidationException.errore);
			}
		}
		if (codBib !=null &&  codBib.length()!=0)	{
			if (codBib.length()>3)	{
				throw new ValidationException("validaFormatiSezioniCodBibEccedente", ValidationException.errore);
			}
		}
		if (codSez == (null)){
			throw new ValidationException("validaFormatiSezioniCodSezObbligatorio",ValidationException.errore);
		}
		if (codSez !=null &&  codSez.length()!=0)	{
			if (codSez.length()>10)	{
				throw new ValidationException("validaFormatiSezioniCodSezEccedente", ValidationException.errore);
			}
		}
	}
	public	void validaCodPoloCodBibBid (String codPolo, String codBib, String bid)
	throws ValidationException {

		if (codPolo ==(null) || ( codPolo.trim().length()==0)){
			throw new ValidationException("validaFormatiSezioniCodPoloObbligatorio", ValidationException.errore);
		}
		if (codBib == (null) || (codBib!=null && codBib.trim().length()==0)){
			throw new ValidationException("validaFormatiSezioniCodBibObbligatorio",ValidationException.errore);
		}
		if (codPolo !=null &&  codPolo.length()!=0)	{
			if (codPolo.length()>3)	{
				throw new ValidationException("validaFormatiSezioniCodPoloEccedente", ValidationException.errore);
			}
		}
		if (codBib !=null &&  codBib.length()!=0)	{
			if (codBib.length()>3)	{
				throw new ValidationException("validaFormatiSezioniCodBibEccedente", ValidationException.errore);
			}
		}
		if (bid == (null) || (bid!=null && bid.trim().length()==0)){
			throw new ValidationException("bidObbligatorio",ValidationException.errore);
		}
		if (bid !=null &&  bid.length()!=0)	{
			if (bid.length()>10)	{
				throw new ValidationException("bidEccedente", ValidationException.errore);
			}
		}
	}
	public	void validaCodPoloListaBilioBid (String codPolo, List<String> listaBiblio, String bid)
	throws ValidationException {

		if (codPolo ==(null) || ( codPolo.trim().length()==0)){
			throw new ValidationException("validaInvCodPoloObbligatorio", ValidationException.errore);
		}
		if (listaBiblio == (null) || (listaBiblio!=null && listaBiblio.size()==0)){
			throw new ValidationException("validaInvCodBibObbligatorio",ValidationException.errore);
		}
		if (codPolo !=null &&  codPolo.length()!=0)	{
			if (codPolo.length()>3)	{
				throw new ValidationException("validaInvCodPoloEccedente", ValidationException.errore);
			}
		}
		if (bid == (null) || (bid!=null && bid.trim().length()==0)){
			throw new ValidationException("bidObbligatorio",ValidationException.errore);
		}
		if (bid !=null &&  bid.length()!=0)	{
			if (bid.length()>10)	{
				throw new ValidationException("bidEccedente", ValidationException.errore);
			}
		}
	}
	public	void validaCodPoloCodBibSerie (String codPolo, String codBib, String serie)
	throws ValidationException {
		try{

			if (codPolo ==(null) || ( codPolo.trim().length()==0)){
				throw new ValidationException("codPoloObbligatorio", ValidationException.errore);
			}
			if (codBib == (null) || (codBib!=null && codBib.trim().length()==0)){
				throw new ValidationException("codBibObbligatorio",ValidationException.errore);
			}
			if (codPolo !=null &&  codPolo.length()!=0)	{
				if (codPolo.length()>3)	{
					throw new ValidationException("codPoloEccedente", ValidationException.errore);
				}
			}
			if (codBib !=null &&  codBib.length()!=0)	{
				if (codBib.length()>3)	{
					throw new ValidationException("codBibEccedente", ValidationException.errore);
				}
			}
			if (serie!=null && serie.trim().length()!=0) {
				if (serie.length()>3) {
					throw new ValidationException("codSerieEccedente", ValidationException.errore);
				}
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
	}
	public	void validaCodPoloCodBibSezione (String codPolo, String codBib, String sezione)
	throws ValidationException {

		if (codPolo ==(null) || ( codPolo.trim().length()==0)){
			throw new ValidationException("codPoloObbligatorio", ValidationException.errore);
		}
		if (codPolo !=null &&  codPolo.length()!=0)	{
			if (codPolo.length()>3)	{
				throw new ValidationException("codPoloEccedente", ValidationException.errore);
			}
		}
		if (codBib == (null) || (codBib!=null && codBib.trim().length()==0)){
			throw new ValidationException("codBibObbligatorio",ValidationException.errore);
		}
		if (codBib !=null &&  codBib.length()!=0)	{
			if (codBib.length()>3)	{
				throw new ValidationException("codBibEccedente", ValidationException.errore);
			}
		}
		if (sezione!=null && sezione.trim().length()!=0) {
			if (sezione.length()>10) {
				throw new ValidationException("codSezioneEccedente", ValidationException.errore);
			}
		}
	}
	public	void validaFormatiSezioni (String codPolo, String codBib, String codSez, String codFor,
			boolean isSezSpazio)
	throws ValidationException {

		try{
			if (!ValidazioneDati.isFilled(codPolo)){
				throw new ValidationException("validaFormatiSezioniCodPoloObbligatorio", ValidationException.errore);
			}
			if (!ValidazioneDati.isFilled(codBib)){
				throw new ValidationException("validaFormatiSezioniCodBibObbligatorio",ValidationException.errore);
			}

			if (codPolo.length()>3)	{
				throw new ValidationException("validaFormatiSezioniCodPoloEccedente", ValidationException.errore);
			}


			if (codBib.length()>3)	{
				throw new ValidationException("validaFormatiSezioniCodBibEccedente", ValidationException.errore);
			}

			//almaviva5_20151013 fix per cod sezione formato a spazio
			if (codSez == (null) ||
					(!isSezSpazio && !ValidazioneDati.isFilled(codSez)) ) { //|| (codSez!=null && codSez.trim().length()==0)){
				throw new ValidationException("validaFormatiSezioniCodSezObbligatorio",ValidationException.errore);
			}

			if (codSez.length()>10)	{
				throw new ValidationException("validaFormatiSezioniCodSezEccedente", ValidationException.errore);
			}

			if (codFor!=null && codFor.trim().length()==0) {
				throw new ValidationException("validaFormatiSezioniCodFormatoObbligatorio", ValidationException.errore);
			}
			if (codFor!=null && codFor.trim().length()!=0) {
				if (codFor.length()>2) {
					throw new ValidationException("validaFormatiSezioniCodFormatoEccedente", ValidationException.errore);
				}
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
	}
	public	void validaFormatiSezioni (FormatiSezioniVO rec)
	throws ValidationException {

		if (rec.getCodPolo() ==(null) || ( rec.getCodPolo().trim().length()==0)){
			throw new ValidationException("validaFormatiSezioniCodPoloObbligatorio", ValidationException.errore);
		}
		if (rec.getCodBib() == (null) || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
			throw new ValidationException("validaFormatiSezioniCodBibObbligatorio",ValidationException.errore);
		}
		if (rec.getCodSez() == (null)){
			throw new ValidationException("validaFormatiSezioniCodSezObbligatorio",ValidationException.errore);
		}
		if (rec.getCodFormato()!=null && rec.getCodFormato().trim().length()==0) {
			throw new ValidationException("validaFormatiSezioniCodFormatoObbligatorio", ValidationException.errore);
		}
		if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
			if (rec.getCodPolo().length()>3)	{
				throw new ValidationException("validaFormatiSezioniCodPoloEccedente", ValidationException.errore);
			}
		}
		if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
			if (rec.getCodBib().length()>3)	{
				throw new ValidationException("validaFormatiSezioniCodBibEccedente", ValidationException.errore);
			}
		}
		if (rec.getCodFormato()!=null && rec.getCodFormato().trim().length()!=0) {
			if (rec.getCodFormato().length()>2) {
				throw new ValidationException("validaFormatiSezioniCodFormatoEccedente", ValidationException.errore);
			}
		}
		if (rec.getDescrFor()!=null && rec.getDescrFor().trim().length()!=0) {
			if (rec.getDescrFor().length()>30) {//10+20(dimensione formato + descrizione)
				throw new ValidationException("validaFormatiSezioniDescrFormatoEccedente", ValidationException.errore);
			}
		}
		if (!this.strIsNull(String.valueOf(rec.getNumeroPezzi()))) {
			if  (!this.strIsNumeric(String.valueOf((rec.getNumeroPezzi())))) {
				throw new ValidationException("validaFormatiSezioniNumericoNumeroPezzi", ValidationException.errore);
			}
			if ((String.valueOf(rec.getProgSerie()).length() > 6)) {
				throw new ValidationException("validaFormatiSezioniNumeroPezziEccedente", ValidationException.errore);
			}
		}
		if (!this.strIsNull(String.valueOf(rec.getProgNum()))) {
			if (!this.strIsNumeric(String.valueOf((rec.getProgNum())))) {
				throw new ValidationException("validaFormatiSezioniNumericoProgNum", ValidationException.errore);
			}
			if ((String.valueOf(rec.getProgSerie()).length() > 6)) {
				throw new ValidationException("validaFormatiSezioniProgSerieEccedente", ValidationException.errore);
			}
		}
		if (!this.strIsNull(String.valueOf(rec.getDimensioneDa()))) {
			if  (!this.strIsNumeric(String.valueOf((rec.getDimensioneDa())))) {
				throw new ValidationException("validaFormatiSezioniDimensioneDa", ValidationException.errore);
			}
			if ((String.valueOf(rec.getDimensioneDa()).length() > 4)) {
				throw new ValidationException("validaFormatiSezioniDimensioneDaEccedente", ValidationException.errore);
			}
		}
		if (!this.strIsNull(String.valueOf(rec.getDimensioneA()))) {
			if  (!this.strIsNumeric(String.valueOf((rec.getDimensioneA())))) {
				throw new ValidationException("validaFormatiSezioniDimensioneA", ValidationException.errore);
			}
			if ((String.valueOf(rec.getDimensioneA()).length() > 4)) {
				throw new ValidationException("validaFormatiSezioniDimensioneAEccedente", ValidationException.errore);
			}
		}
		if (rec.getUteIns()!=null && rec.getUteIns().trim().length()!=0) {
			if (rec.getUteIns().length()>12) {
				throw new ValidationException("validaFormatiSezioniUteInsEccedente", ValidationException.errore);
			}
		}
		if (rec.getUteAgg()!=null && rec.getUteAgg().trim().length()!=0) {
			if (rec.getUteAgg().length()>12) {
				throw new ValidationException("validaFormatiSezioniUteAggEccedente", ValidationException.errore);
			}
		}
	}
	public	void validaSerie (SerieVO rec)
	throws ValidationException {

		try {
			if (rec.getCodPolo() == null || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
				throw new ValidationException("validaSerieCodPoloObbligatorio", ValidationException.errore);
			}
			if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
				if (rec.getCodPolo().length()>3)	{
					throw new ValidationException("validaSerieCodPoloEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBib() == null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
				throw new ValidationException("validaSerieCodBibObbligatorio", ValidationException.errore);
			}
			if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
				if (rec.getCodBib().length()>3)	{
					throw new ValidationException("validaSerieCodBibEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodSerie()!=null && rec.getCodSerie().trim().length()!=0) {
				if (rec.getCodSerie().length()>3) {
					throw new ValidationException("validaSerieCodSerieEccedente", ValidationException.errore);
				}
			}
			if (rec.getDescrSerie()!=null && rec.getDescrSerie().trim().length()!=0) {
				if (rec.getDescrSerie().trim().length()>80) {
					throw new ValidationException("validaSerieDescrSerieEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(rec.getProgAssInv())) {
				if (!this.strIsNumeric(rec.getProgAssInv())) {
					throw new ValidationException("validaSerieNumericoProgAssInv", ValidationException.errore);
				}
				if (((rec.getProgAssInv()).length() > 9)) {
					throw new ValidationException("validaSerieEccedenteProgAssInv", ValidationException.errore);
				}
			}
			if (!this.strIsNull((rec.getPregrAssInv()))) {
				if (!this.strIsNumeric(((rec.getPregrAssInv())))) {
					throw new ValidationException("validaSerieNumericoPregrAssInv", ValidationException.errore);
				}
				if (((rec.getPregrAssInv()).length() > 9)) {
					throw new ValidationException("validaSerieEccedentePregrAssInv", ValidationException.errore);
				}
				if (Integer.parseInt(rec.getPregrAssInv()) < 900000000) {
					throw new ValidationException("errPregrAssInvMinore", ValidationException.errore);
				}
			}
			if (rec.getDataIngrPregr()!=null && rec.getDataIngrPregr().length()!=0) {
				int codRitorno = ValidazioneDati.validaData_1(rec.getDataIngrPregr());
				if (codRitorno != ValidazioneDati.DATA_OK)
					throw new ValidationException("validaInvDataIngrPregrErrata", ValidationException.errore);
			}
			if (!this.strIsNull((rec.getNumMan()))) {
				if (!this.strIsNumeric(((rec.getNumMan())))) {
					throw new ValidationException("validaSerieNumericoNumMan", ValidationException.errore);
				}
				if (((rec.getNumMan()).length() > 9)) {
					throw new ValidationException("validaSerieEccedenteNumMan", ValidationException.errore);
				}
				if (Integer.parseInt(rec.getNumMan()) > (Integer.parseInt(rec.getProgAssInv()))) {
					throw new ValidationException("errNumManMaggiore", ValidationException.errore);
				}
			}
			if (rec.getDataIngrMan()!=null && rec.getDataIngrMan().length()!=0) {
				int codRitorno = ValidazioneDati.validaData_1(rec.getDataIngrMan());
				if (codRitorno != ValidazioneDati.DATA_OK)
					throw new ValidationException("validaInvDataIngrManErrata", ValidationException.errore);
			}
			if (!this.strIsNull((rec.getInizioMan1()))) {
				if (!this.strIsNumeric(((rec.getInizioMan1())))) {
					throw new ValidationException("validaSerieNumericoInizioMan1", ValidationException.errore);
				}
				if (((rec.getInizioMan1()).length() > 9)) {
					throw new ValidationException("validaSerieEccedenteInizioMan1", ValidationException.errore);
				}
				if (Integer.parseInt(rec.getInizioMan1()) > (Integer.parseInt(rec.getFineMan1()))) {
					throw new ValidationException("errInizioMan1Maggiore", ValidationException.errore);
				}
			}
			if (!this.strIsNull((rec.getFineMan1()))) {
				if (!this.strIsNumeric(((rec.getFineMan1())))) {
					throw new ValidationException("validaSerieNumericoFineMan1", ValidationException.errore);
				}
				if ((String.valueOf(rec.getFineMan1()).length() > 9)) {
					throw new ValidationException("validaSerieEccedenteFineMan1", ValidationException.errore);
				}
			}
			if (rec.getDataIngrRis1()!=null && rec.getDataIngrRis1().length()!=0) {
				int codRitorno = ValidazioneDati.validaData_1(rec.getDataIngrRis1());
				if (codRitorno != ValidazioneDati.DATA_OK)
					throw new ValidationException("validaInvDataIngrRis1Errata", ValidationException.errore);
			}

			if (!this.strIsNull((rec.getInizioMan2()))) {
				if (!this.strIsNumeric(((rec.getInizioMan2())))) {
					throw new ValidationException("validaSerieNumericoInizioMan2", ValidationException.errore);
				}
				if (((rec.getInizioMan2()).length() > 9)) {
					throw new ValidationException("validaSerieEccedenteInizioMan2", ValidationException.errore);
				}
				if (Integer.parseInt(rec.getInizioMan2()) > (Integer.parseInt(rec.getFineMan2()))) {
					throw new ValidationException("errInizioMan2Maggiore", ValidationException.errore);
				}
			}
			if (!this.strIsNull((rec.getFineMan2()))) {
				if (!this.strIsNumeric(((rec.getFineMan2())))) {
					throw new ValidationException("validaSerieNumericoFineMan2", ValidationException.errore);
				}
				if ((String.valueOf(rec.getFineMan2()).length() > 9)) {
					throw new ValidationException("validaSerieEccedenteFineMan2", ValidationException.errore);
				}
			}
			if (rec.getDataIngrRis2()!=null && rec.getDataIngrRis2().length()!=0) {
				int codRitorno = ValidazioneDati.validaData_1(rec.getDataIngrRis2());
				if (codRitorno != ValidazioneDati.DATA_OK)
					throw new ValidationException("validaInvDataIngrRis2Errata", ValidationException.errore);
			}

			if (!this.strIsNull((rec.getInizioMan3()))) {
				if (!this.strIsNumeric(((rec.getInizioMan1())))) {
					throw new ValidationException("validaSerieNumericoInizioMan3", ValidationException.errore);
				}
				if (((rec.getInizioMan3()).length() > 9)) {
					throw new ValidationException("validaSerieEccedenteInizioMan3", ValidationException.errore);
				}
				if (Integer.parseInt(rec.getInizioMan3()) > (Integer.parseInt(rec.getFineMan3()))) {
					throw new ValidationException("errInizioMan3Maggiore", ValidationException.errore);
				}
			}
			if (!this.strIsNull((rec.getFineMan3()))) {
				if (!this.strIsNumeric(((rec.getFineMan3())))) {
					throw new ValidationException("validaSerieNumericoFineMan3", ValidationException.errore);
				}
				if ((String.valueOf(rec.getFineMan3()).length() > 9)) {
					throw new ValidationException("validaSerieEccedenteFineMan3", ValidationException.errore);
				}
			}
			if (rec.getDataIngrRis3()!=null && rec.getDataIngrRis3().length()!=0) {
				int codRitorno = ValidazioneDati.validaData_1(rec.getDataIngrRis3());
				if (codRitorno != ValidazioneDati.DATA_OK)
					throw new ValidationException("validaInvDataIngrRis3Errata", ValidationException.errore);
			}

			if (!this.strIsNull((rec.getInizioMan4()))) {
				if (!this.strIsNumeric(((rec.getInizioMan4())))) {
					throw new ValidationException("validaSerieNumericoInizioMan4", ValidationException.errore);
				}
				if (((rec.getInizioMan4()).length() > 9)) {
					throw new ValidationException("validaSerieEccedenteInizioMan4", ValidationException.errore);
				}
				if (Integer.parseInt(rec.getInizioMan4()) > (Integer.parseInt(rec.getFineMan4()))) {
					throw new ValidationException("errInizioMan4Maggiore", ValidationException.errore);
				}
			}
			if (!this.strIsNull((rec.getFineMan4()))) {
				if (!this.strIsNumeric(((rec.getFineMan4())))) {
					throw new ValidationException("validaSerieNumericoFineMan4", ValidationException.errore);
				}
				if ((String.valueOf(rec.getFineMan4()).length() > 9)) {
					throw new ValidationException("validaSerieEccedenteFineMan4", ValidationException.errore);
				}
			}
			if (rec.getDataIngrRis4()!=null && rec.getDataIngrRis4().length()!=0) {
				int codRitorno = ValidazioneDati.validaData_1(rec.getDataIngrRis4());
				if (codRitorno != ValidazioneDati.DATA_OK)
					throw new ValidationException("validaInvDataIngrRis4Errata", ValidationException.errore);
			}

			if (rec.getFlChiusa()!=null && rec.getFlChiusa().trim().length()!=0) {
				if (rec.getFlChiusa().length()>1) {
					throw new ValidationException("validaSerieFlChiusaEccedente", ValidationException.errore);
				}
			}
			if (rec.getFlDefault()!=null && rec.getFlDefault().trim().length()!=0) {
				if (rec.getFlDefault().length()>1) {
					throw new ValidationException("validaSerieFlDefaultEccedente", ValidationException.errore);
				}
			}
//			if (!this.strIsNull((rec.getBuonoCarico()))) {
//				if (!this.strIsNumeric(((rec.getBuonoCarico())))) {
//					throw new ValidationException("validaSerieNumericoBuonoCarico", ValidationException.errore);
//				}
//				if ((String.valueOf(rec.getBuonoCarico()).length() > 9)) {
//					throw new ValidationException("validaSerieEccedenteBuonoCarico", ValidationException.errore);
//				}
//			}
			if (rec.getUteIns()!=null && rec.getUteIns().trim().length()!=0) {
				if (rec.getUteIns().length()>12) {
					throw new ValidationException("validaSerieUteInsEccedente", ValidationException.errore);
				}
			}
			if (rec.getUteAgg()!=null && rec.getUteAgg().trim().length()!=0) {
				if (rec.getUteAgg().length()>12) {
					throw new ValidationException("validaSerieUteAggEccedente", ValidationException.errore);
				}
			}
					//progressivo automatico
					if (rec.getProgAssInv() != null){
						if (rec.getProgAssInv().trim().equals("")){
							rec.setProgAssInv("0");
						}
					}
//					//progressivo manuale
//					if (rec.getNumMan() != null){
//						if (rec.getProgAssInv().trim().equals("")){
//							rec.setProgAssInv("0");
//						}
//						if(Integer.parseInt(rec.getNumMan()) > Integer.parseInt(rec.getProgAssInv())){
//							if (rec.getProgAssInv().trim().equals("0")) {
//								rec.setProgAssInv("");
//							}
//							if (rec.getNumMan().trim().equals("0")){
//								rec.setNumMan("");
//							}
//							throw new ValidationException("errNumManMaggiore");
//						}
//					}
					//controllo date
					Timestamp dataMan = null;
					Timestamp dataIntRis1 = null;
					Timestamp dataIntRis2 = null;
					Timestamp dataIntRis3 = null;
					Timestamp dataIntRis4 = null;
					if (rec.getDataIngrMan() != null && !rec.getDataIngrMan().trim().equals("")){
						dataMan = DateUtil.toTimestamp(rec.getDataIngrMan());
					}
					if (rec.getDataIngrRis1() != null && !rec.getDataIngrRis1().trim().equals("")){
						dataIntRis1 = DateUtil.toTimestamp(rec.getDataIngrRis1());
					}
					if (rec.getDataIngrRis2() != null && !rec.getDataIngrRis2().trim().equals("")){
						dataIntRis2 = DateUtil.toTimestamp(rec.getDataIngrRis2());
					}
					if (rec.getDataIngrRis3() != null && !rec.getDataIngrRis3().trim().equals("")){
						dataIntRis3 = DateUtil.toTimestamp(rec.getDataIngrRis3());
					}
					if (rec.getDataIngrRis4() != null && !rec.getDataIngrRis4().trim().equals("")){
						dataIntRis4 = DateUtil.toTimestamp(rec.getDataIngrRis4());
					}
					//data man
					//Se indicata deve essere minore di tutte le ulteriori date
					//convenzionali definite per gli altri intervalli riservati.
					if (rec.getDataIngrMan() != null && !rec.getDataIngrMan().trim().equals("") && dataMan != null){
						if (dataIntRis1 != null){
							if (dataMan.after(dataIntRis1)){
								throw new DataException("dataManDeveEssereInferioreAlleAltreDateRis");
							}
						}

						if (dataIntRis2 != null){
							if (dataMan.after(dataIntRis2)){
								throw new DataException("dataManDeveEssereInferioreAlleAltreDateRis");
							}
						}
						if (dataIntRis3 != null){
							if (dataMan.after(dataIntRis3)){
								throw new DataException("dataManDeveEssereInferioreAlleAltreDateRis");
							}
						}
						if (dataIntRis4 != null){
							if (dataMan.after(dataIntRis4)){
								throw new DataException("dataManDeveEssereInferioreAlleAltreDateRis");
							}
						}

					}
					//data primo intervallo riservato
					/*
					 * Se indicata deve essere maggiore o uguale di  dt_ingr_inv_man
					 * e minore o uguale delle ulteriori date convenzionali definite
					 * per altri intervalli riservati con numerazione più alta
					 */
					if (rec.getDataIngrRis1() != null && !rec.getDataIngrRis1().trim().equals("") && dataIntRis1 != null){
						if (dataIntRis2 != null){
							if (dataIntRis1.after(dataIntRis2)){
								throw new DataException("dataIntRis1DeveEssereInferioreAlleDateDegliIntSuperiori");
							}
						}

						if (dataIntRis3 != null){
							if (dataIntRis1.after(dataIntRis3)){
								throw new DataException("dataIntRis1DeveEssereInferioreAlleDateDegliIntSuperiori");
							}
						}
						if (dataIntRis4 != null){
							if (dataIntRis1.after(dataIntRis4)){
								throw new DataException("dataIntRis1DeveEssereInferioreAlleDateDegliIntSuperiori");
							}
						}
					}
					//data secondo intervallo riservato
					/*
					 * Se indicata deve essere non minore di  dt_ingr_inv_man
					 * e non maggiore delle ulteriori date convenzionali definite
					 * per altri intervalli riservati con numerazione più alta
					 */
					if (rec.getDataIngrRis2() != null && !rec.getDataIngrRis2().trim().equals("") && dataIntRis2 != null){
						if (dataIntRis1 != null){
							if (dataIntRis1.after(dataIntRis2)){
								throw new DataException("dataIntRis2DeveEssereSuperioreAlleDateDegliIntInferiori");
							}
						}
						if (dataIntRis3 != null){
							if (dataIntRis2.after(dataIntRis3)){
								throw new DataException("dataIntRis2DeveEssereInferioreAlleDateDegliIntSuperiori");
							}
						}
						if (dataIntRis4 != null){
							if (dataIntRis2.after(dataIntRis4)){
								throw new DataException("dataIntRis2DeveEssereInferioreAlleDateDegliIntSuperiori");
							}
						}
					}
					//data terzo intervallo riservato
					/*
					 * Se indicata deve essere non minore di  dt_ingr_inv_man
					 * e non maggiore delle ulteriori date convenzionali definite
					 * per altri intervalli riservati con numerazione più alta
					 */
					if (rec.getDataIngrRis3() != null && !rec.getDataIngrRis3().trim().equals("") && dataIntRis3 != null){
						if (dataIntRis1 != null){
							if (dataIntRis1.after(dataIntRis3)){
								throw new DataException("dataIntRis3DeveEssereSuperioreAlleDateDegliIntInferiori");
							}
						}
						if (dataIntRis2 != null){
							if (dataIntRis2.after(dataIntRis3)){
								throw new DataException("dataIntRis3DeveEssereSuperioreAlleDateDegliIntInferiori");
							}
						}
						if (dataIntRis4 != null){
							if (dataIntRis3.after(dataIntRis4)){
								throw new DataException("dataIntRis3DeveEssereInferioreAlleDateDegliIntSuperiori");
							}
						}
					}

					//data quarto intervallo riservato
					/*
					 * Se indicata deve essere non minore di  dt_ingr_inv_man
					 * e non maggiore delle ulteriori date convenzionali definite
					 * per altri intervalli riservati con numerazione più alta
					 */
					if (rec.getDataIngrRis4() != null && !rec.getDataIngrRis4().trim().equals("") && dataIntRis4 != null){
						if (dataIntRis1 != null){
							if (dataIntRis1.after(dataIntRis4)){
								throw new DataException("dataIntRis4DeveEssereSuperioreAlleDateDegliIntInferiori");
							}
						}
						if (dataIntRis2 != null){
							if (dataIntRis2.after(dataIntRis4)){
								throw new DataException("dataIntRis4DeveEssereSuperioreAlleDateDegliIntInferiori");
							}
						}
						if (dataIntRis3 != null){
							if (dataIntRis3.after(dataIntRis4)){
								throw new DataException("dataIntRis4DeveEssereSuperioreAlleDateDegliIntInferiori");
							}
						}
					}
					int i1 = 0;
					int f1 = 0;
					int i2 = 0;
					int f2 = 0;
					int i3 = 0;
					int f3 = 0;
					int i4 = 0;
					int f4 = 0;
					if (rec.getInizioMan1() != null){
						if (Integer.valueOf(rec.getInizioMan1()) > 0){
							if (ValidazioneDati.strIsNumeric(rec.getInizioMan1())){
								i1 = Integer.valueOf(rec.getInizioMan1());
							}else {
								throw new DataException("iValoriDegliIntervalliDevonoEssereNumerici");
							}
						}else{
							i1 = Integer.MAX_VALUE;
						}
					}
					if (rec.getFineMan1() != null){
						if (Integer.valueOf(rec.getFineMan1()) > 0){
							if (ValidazioneDati.strIsNumeric(rec.getFineMan1())){
								f1 = Integer.valueOf(rec.getFineMan1());
							}else {
								throw new DataException("iValoriDegliIntervalliDevonoEssereNumerici");
							}
						}else{
							f1 = Integer.MAX_VALUE;
						}
					}
					if (rec.getInizioMan2() != null) {
						if (Integer.valueOf(rec.getInizioMan2()) > 0){
							if (ValidazioneDati.strIsNumeric(rec.getInizioMan2())){
								i2 = Integer.valueOf(rec.getInizioMan2());
							}else {
								throw new DataException("iValoriDegliIntervalliDevonoEssereNumerici");
							}
						}else{
							i2 = Integer.MAX_VALUE;
						}
			}
					if (rec.getFineMan2() != null){
						if (Integer.valueOf(rec.getFineMan2()) > 0){
						if (ValidazioneDati.strIsNumeric(rec.getFineMan2())){
								f2 = Integer.valueOf(rec.getFineMan2());
							}else {
								throw new DataException("iValoriDegliIntervalliDevonoEssereNumerici");
							}
						}else{
							f2 = Integer.MAX_VALUE;
						}
					}
					if (rec.getInizioMan3() != null){
						if (Integer.valueOf(rec.getInizioMan3()) > 0){
						if (ValidazioneDati.strIsNumeric(rec.getInizioMan3())){
								i3 = Integer.valueOf(rec.getInizioMan3());
							}else {
								throw new DataException("iValoriDegliIntervalliDevonoEssereNumerici");
							}
						}else{
							i3 = Integer.MAX_VALUE;
						}
					}
					if (rec.getFineMan3() != null){
						if (Integer.valueOf(rec.getFineMan3()) > 0){
						if (ValidazioneDati.strIsNumeric(rec.getFineMan3())){
								f3 = Integer.valueOf(rec.getFineMan3());
							}else {
								throw new DataException("iValoriDegliIntervalliDevonoEssereNumerici");
							}
						}else{
							f3 = Integer.MAX_VALUE;
						}
					}
					if (rec.getInizioMan4() != null){
						if (Integer.valueOf(rec.getInizioMan4()) > 0){
						if (ValidazioneDati.strIsNumeric(rec.getInizioMan4())){
								i4 = Integer.valueOf(rec.getInizioMan4());
							}else{
								throw new DataException("iValoriDegliIntervalliDevonoEssereNumerici");
							}
						}else{
							i4 = Integer.MAX_VALUE;
						}
					}
					if (rec.getFineMan4() != null){
						if (Integer.valueOf(rec.getFineMan4()) > 0 ){
							if (ValidazioneDati.strIsNumeric(rec.getFineMan4())){
								f4 = Integer.valueOf(rec.getFineMan4());
							}else {
								throw new DataException("iValoriDegliIntervalliDevonoEssereNumerici");
							}
						}else{
							f4 = Integer.MAX_VALUE;
						}
					}
					//numero inizio primo intervallo riservato
					// Se indicato deve essere maggiore di num_man,
					if (i1 != Integer.MAX_VALUE){
						if(i1 >= (Integer.parseInt(rec.getNumMan()))){
							//deve essere minore di 900000000 ed esterno agli estremi
							if (i1 < 900000000){
								//deve essere minore di fineMan
								if (i1 <= f1){
									//deve essere esterno agli estremi degli altri intervalli riservati (non compreso tra)
									if ((i1 < i2) || (i1 > f2 && i1 < i3) || (i1 > f3 && i1 < i4) || (i1 > i4)){
										//Può essere minore di prg_inv_corrente
										if (i1 <= Integer.parseInt(rec.getProgAssInv())){
											//ma in questo caso anche fine_man deve esserlo
											if (f1 != Integer.MAX_VALUE){
												if (f1 > Integer.parseInt(rec.getProgAssInv())){
													throw new DataException("errDefIntRis1EstrSupDeveEssereMinoreProgrAut");
												}
											}
										}
									}else{
										throw new DataException("numintRis1InizioDeveAppartenereANessunIntRis");
									}
								}else{
									//<=
									throw new DataException("numintRis1InizioDeveEssereMinoreDinumIntRis1Fine");
								}

							}else{
								throw new DataException("num1intRisDeveEssereMinoreDi900000000");
							}
						}else{
							throw new DataException("num1intRisDeveEssereMaggioreDelNumMan");
						}
					}
					//numero fine primo intervallo riservato
					//Obbligatorio se è stato indicato inizio_man;
					if (f1 != Integer.MAX_VALUE){
						if(i1 > 0 && f1 > 0){
							//il valore deve essere maggiore di inizio_man
							if (f1 > i1){
								//in ogni caso minore di 900000000
								if (f1 < 900000000) {
									//minore del minore degli estremi inferiori degli altri eventuali intervalli riservati definiti
									if ((f1 < i2) || (f1 > f2 && f1 < i3) || (f1 > f3 && f1 < i4) || (f1 > i4)){
									}else{

										throw new DataException("num1intRisFineDeveEssereMinoreDelMinoreDegliAltriIntRisFineDefiniti");
									}
								}else{
									throw new DataException("num1intRisFineDeveEssereMinoreDi900000000");
								}
							}else{
								throw new DataException("num1intRisFineDeveEssereMaggioreDiNum1intRisInizio");
							}
						}else{
							throw new DataException("num1intRisFineEObbligSeEPresnum1intRisInizio");
						}
					}

					//numero inizio secondo intervallo riservato
					// Se indicato deve essere maggiore di num_man,
					if (i2 != Integer.MAX_VALUE){
						if(i2 > Integer.parseInt(rec.getNumMan())){
							//deve essere minore di 900000000 ed esterno agli estremi
							if (i2 < 900000000){
								//deve essere minore di fineMan
								if (i2 < f2){
									//deve essere esterno agli estremi degli altri intervalli riservati (non compreso tra)
									if ((i2 < i1) || (i2 > f1 && i2 < i3) || (i2 > f3 && i2 < i4) || (i2 > f4)){
										//Può essere minore di prg_inv_corrente
										if (i2 <= Integer.parseInt(rec.getProgAssInv())){
											//ma in questo caso anche fine_man deve esserlo
											if (f2 != Integer.MAX_VALUE){
												if (f2 > Integer.parseInt(rec.getProgAssInv())){
													throw new DataException("errDefIntRis2EstrSupDeveEssereMinoreProgrAut");
												}
											}
										}//nessun else di errore
									}else{
										throw new DataException("numintRis2InizioDeveAppartenereANessunIntRis");
									}
								}else{
									//<=
									throw new DataException("numintRis2InizioDeveEssereMinoreDinumIntRis1Fine");
								}

							}else{
								throw new DataException("num21intRisDeveEssereMinoreDi900000000");
							}
						}else{
							throw new DataException("num2intRisDeveEssereMaggioreDelNumMan");
						}
					}
					//numero fine secondo intervallo riservato
					//Obbligatorio se è stato indicato inizio_man;
					if (f2 != Integer.MAX_VALUE){
						if(i2 > 0 && f2 > 0){
							//il valore deve essere maggiore di inizio_man
							if (f2 > i2){
								//in ogni caso minore di 900000000
								if (f2 < 900000000) {
									//minore del minore degli estremi inferiori degli altri eventuali intervalli riservati definiti
//									if (f2 < f4
//											&& f2 < f3
//											&& f2 > f1){
									if ((f2 < i1) || (f2 > f1 && f2 < i3) || (f2 > f3 && f2 < i4) || (f2 > f4)){
									}else{
										throw new DataException("num2intRisFineDeveEssereMinoreDelMaggioreDegliAltriIntRisFineDefiniti");
									}
								}else{
									throw new DataException("num2intRisFineDeveEssereMinoreDelMinoreDi900000000");
								}
							}else{
								throw new DataException("num2intRisFineDeveEssereMaggioreDiNum1intRisInizio");
							}
						}else{
							throw new DataException("num21intRisFineEObbligSeEPresnum1intRisInizio");
						}
					}
					//numero inizio terzo intervallo riservato
					// Se indicato deve essere maggiore di num_man,
					if (i3 != Integer.MAX_VALUE){
						if(i3 > Integer.parseInt(rec.getNumMan())){
							//deve essere minore di 900000000 ed esterno agli estremi
							if (i3 < 900000000){
								//deve essere minore di fineMan
								if (i3 < f3){
									//deve essere esterno agli estremi degli altri intervalli riservati (non compreso tra)
									if ((i3 < i1) || (i3 > f1 && i3 < i2) || (i3 > f2 && i3 < i4) || (i3 > f4)){
													//Può essere minore di prg_inv_corrente
										if (i3 <= Integer.parseInt(rec.getProgAssInv())){
											//ma in questo caso anche fine_man deve esserlo
											if (f3 != Integer.MAX_VALUE){
												if (f3 > Integer.parseInt(rec.getProgAssInv())){
													throw new DataException("errDefIntRis3EstrSupDeveEssereMinoreProgrAut");
												}
											}
										}
									}else{
										throw new DataException("numintRis3InizioDeveAppartenereANessunIntRis");
									}
								}else{
									//<=
									throw new DataException("numintRis3InizioDeveEssereMinoreDinumIntRis1Fine");
								}

							}else{
								throw new DataException("num3intRisDeveEssereMinoreDi900000000");
							}
						}else{
							throw new DataException("num3intRisDeveEssereMaggioreDelNumMan");
						}
					}
					//numero fine terzo intervallo riservato
					//Obbligatorio se è stato indicato inizio_man;
					if (f3 != Integer.MAX_VALUE){
						if(i3 > 0 && f3 > 0){
							//il valore deve essere maggiore di inizio_man
							if (f3 > i3){
								//in ogni caso minore di 900000000
								if (f3 < 900000000) {
									//minore del minore degli estremi inferiori degli altri eventuali intervalli riservati definiti
									if ((f3 < i1) || (f3 > f1 && f3 < i2) || (f3 > f2 && f3 < i4) || (f3 > f4)){
//									if (f3 < f4
//											&& f3 > f2
//											&& f3 > f1){
									}else{
										throw new DataException("num3intRisFineDeveEssereMinoreDelMaggioreDegliAltriIntRisFineDefiniti");
									}
								}else{
									throw new DataException("num3intRisFineDeveEssereMinoreDelMinoreDi900000000");
								}
							}else{
								throw new DataException("num3intRisFineDeveEssereMaggioreDiNum1intRisInizio");
							}
						}else{
							throw new DataException("num31intRisFineEObbligSeEPresnum1intRisInizio");
						}
					}
					//numero inizio quarto intervallo riservato
					// Se indicato deve essere maggiore di num_man,
					if (i4 != Integer.MAX_VALUE){
						if(i4 > Integer.parseInt(rec.getNumMan())){
							//deve essere minore di 900000000 ed esterno agli estremi
							if (i4 < 900000000){
								//deve essere minore di fineMan
								if (i4 <= f4){
									//deve essere esterno agli estremi degli altri intervalli riservati (non compreso tra)
									if ((i4 < i1) || (i4 > f1 && i4 < i2) || (i4 > f2 && i4 < i3) || (i4 > f3)){
													//Può essere minore di prg_inv_corrente
										if (i4 <= Integer.parseInt(rec.getProgAssInv())){
											//ma in questo caso anche fine_man deve esserlo
											if (f4 != Integer.MAX_VALUE){
												if (f4 > Integer.parseInt(rec.getProgAssInv())){
													throw new DataException("errDefIntRis4EstrSupDeveEssereMinoreProgrAut");
												}
											}

										}
									}else{
										throw new DataException("numintRis4InizioDeveAppartenereANessunIntRis");
									}
								}else{
									//<=
									throw new DataException("numintRis4InizioDeveEssereMinoreDinumIntRis4Fine");
								}


							}else{
								throw new DataException("num4intRisDeveEssereMinoreDi900000000");
							}
						}else{
							throw new DataException("num4intRisDeveEssereMaggioreDelNumMan");
						}
					}
					//numero fine quarto intervallo riservato
					//Obbligatorio se è stato indicato inizio_man;
					if (f4 != Integer.MAX_VALUE){
						if(i4 > 0 && f4 > 0){
							//il valore deve essere maggiore di inizio_man
							if (f4 > i4){
								//in ogni caso minore di 900000000
								if (f4 < 900000000) {
									//maggiore degli estremi inferiori degli altri eventuali intervalli riservati definiti che lo precedono
//									if (f4 > f1
//											&& f4 > f2
//											&& f4 > f3){
									if ((f4 < i1) || (f4 > f1 && f4 < i2) || (f4 > f2 && f4 < i3) || (f4 > f3)){
									}else{
										throw new DataException("num3intRisFineDeveEssereMinoreDelMaggioreDegliAltriIntRisFineDefiniti");
									}
								}else{
									throw new DataException("num3intRisFineDeveEssereMinoreDelMinoreDi900000000");
								}
							}else{
								throw new DataException("num3intRisFineDeveEssereMaggioreDiNum1intRisInizio");
							}
						}else{
							throw new DataException("num31intRisFineEObbligSeEPresnum1intRisInizio");
						}
					}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
	}

	public	void validaInventario (InventarioVO rec, String tipoOperazione, int nInv)
	throws ValidationException {
		try {

			if (rec.getCodPolo() == null || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
				throw new ValidationException("validaInvCodPoloObbligatorio", ValidationException.errore);
			}
			if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
				if (rec.getCodPolo().length()>3)	{
					throw new ValidationException("validaInvCodPoloEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBib() == null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
				throw new ValidationException("validaInvCodBibObbligatorio", ValidationException.errore);
			}
			if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
				if (rec.getCodBib().length()>3)	{
					throw new ValidationException("validaInvCodBibEccedente", ValidationException.errore);
				}
			}
//			if (rec.getCodSerie()!=null && rec.getCodSerie().length()==0){
//				throw new ValidationException("validaInvCodSerieObbligatorio", ValidationException.errore);
//			}
			if (rec.getCodSerie()!=null && rec.getCodSerie().length()!=0) {
				if (rec.getCodSerie().length()>3) {
					throw new ValidationException("validaInvCodSerieEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getCodInvent()))) {
				if (String.valueOf(rec.getCodInvent()).length()>9) {
					throw new ValidationException("validaInvCodInventEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodProven()!=null && rec.getCodProven().trim().length()!=0) {
				if (rec.getCodProven().length()>5) {
					throw new ValidationException("validaInvCodProvenEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodPoloProven() !=null &&  rec.getCodPoloProven().length()!=0)	{
				if (rec.getCodPoloProven().length()>3)	{
					throw new ValidationException("validaInvCodPoloProvenEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBibProven() !=null &&  rec.getCodBibProven().length()!=0)	{
				if (rec.getCodBibProven().length()>3)	{
					throw new ValidationException("validaInvCodBibProvenEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getKeyLoc()))) {
				if (String.valueOf(rec.getKeyLoc()).length()>9) {
					throw new ValidationException("validaInvKeyLocEccedente", ValidationException.errore);
				}
			}
			if (rec.getBid() !=null &&  rec.getBid().length()!=0)	{
				if (rec.getBid().length()>10)	{
					throw new ValidationException("validaInvBidEccedente", ValidationException.errore);
				}
			}
//			if (rec.getSeqColl()!=null && rec.getSeqColl().trim().length()==0){
//			throw new ValidationException("validaInvSeqCollObbligatorio", ValidationException.errore);
//			}
			if (rec.getSeqColl() !=null &&  rec.getSeqColl().length()!=0)	{
				if (rec.getSeqColl().trim().length()>20)	{
					throw new ValidationException("validaInvSeqCollEccedente", ValidationException.errore);
				}
			}
			if (rec.getPrecInv() !=null &&  rec.getPrecInv().length()!=0)	{
//				if (rec.getPrecInv().trim().length()>255)	{//bug 4728 collaudo
//					throw new ValidationException("validaInvPrecInvEccedente", ValidationException.errore);
//				}
			}
			if (rec.getCodSit()!=null && rec.getCodSit().trim().length()==0){
				throw new ValidationException("validaInvCodSitObbligatorio", ValidationException.errore);
			}
			if (rec.getCodSit() !=null &&  rec.getCodSit().length()!=0)	{
				if (rec.getCodSit().trim().length()>1)	{
					throw new ValidationException("validaInvCodSitEccedente", ValidationException.errore);
				}
			}
			if (this.strIsNull(String.valueOf(rec.getValore())) ||
					(String.valueOf(rec.getValore()).trim().equals("0"))) {
				throw new ValidationException("validaInvValoreObbligatorio", ValidationException.errore);
			}
			if (!this.strIsNull(String.valueOf(rec.getValore()))) {
				if (String.valueOf(rec.getValore()).length()>9) {
					throw new ValidationException("validaInvValoreEccedente", ValidationException.errore);
				}
			}
			if (this.strIsNull(String.valueOf(rec.getImporto())) ||
					(String.valueOf(rec.getImporto()).trim().equals("0"))) {
				throw new ValidationException("validaInvImportoObbligatorio", ValidationException.errore);
			}
			if (!this.strIsNull(String.valueOf(rec.getImporto()))) {
				if (String.valueOf(rec.getImporto()).length()>9) {
					throw new ValidationException("validaInvImportoEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getNumVol()))) {
				if (String.valueOf(rec.getNumVol()).length()>9) {
					throw new ValidationException("validaInvNumVolEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getTotLoc()))) {
				if (String.valueOf(rec.getTotLoc()).length()>9) {
					throw new ValidationException("validaInvTotLocEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getTotInter()))) {
				if (String.valueOf(rec.getTotInter()).length()>9) {
					throw new ValidationException("validaInvTotInterEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getAnnoAbb()))) {
				if (String.valueOf(rec.getAnnoAbb()).length()>9) {
					throw new ValidationException("validaInvAnnoAbbEccedente", ValidationException.errore);
				}
			}
			if (rec.getFlagDisp()!=null && rec.getFlagDisp().trim().length()==0){
				throw new ValidationException("validaInvFlagDispObbligatorio", ValidationException.errore);
			}
			if (rec.getFlagDisp() !=null &&  rec.getFlagDisp().length()!=0)	{
				if (rec.getFlagDisp().trim().length()>1)	{
					throw new ValidationException("validaInvFlagDispEccedente", ValidationException.errore);
				}
			}
			if (rec.getFlagNU()!=null && rec.getFlagNU().trim().length()==0){
				throw new ValidationException("validaInvFlagNUObbligatorio", ValidationException.errore);
			}
			if (rec.getFlagNU() !=null &&  rec.getFlagNU().length()!=0)	{
				if (rec.getFlagNU().trim().length()>1)	{
					throw new ValidationException("validaInvFlagNUEccedente", ValidationException.errore);
				}
			}
//			if (rec.getStatoConser()!=null && rec.getStatoConser().trim().length()==0){
//			throw new ValidationException("validaInvStatoConserObbligatorio", ValidationException.errore);
//			}
			if (rec.getStatoConser() !=null &&  rec.getStatoConser().length()!=0)	{
				if (rec.getStatoConser().trim().length()>2)	{
					throw new ValidationException("validaInvStatoConserEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getCodFornitore()))) {
				if (String.valueOf(rec.getCodFornitore()).length()>9) {
					throw new ValidationException("validaInvCodFornitoreEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodMatInv()!=null && rec.getCodMatInv().trim().length()==0){
				throw new ValidationException("validaInvCodMatInvObbligatorio", ValidationException.errore);
			}
			if (rec.getCodMatInv() !=null &&  rec.getCodMatInv().length()!=0)	{
				if (rec.getCodMatInv().trim().length()>2)	{
					throw new ValidationException("validaInvCodMatInvEccedente", ValidationException.errore);
				}
			}
			if (rec.getTipoAcquisizione() !=null && rec.getTipoAcquisizione().trim().length()==0){
				throw new ValidationException("validaInvTipoAcquisizioneObbligatorio", ValidationException.errore);
			}
			if (rec.getTipoAcquisizione() !=null &&  rec.getTipoAcquisizione().length()!=0)	{
				if (rec.getTipoAcquisizione().trim().length()>1)	{
					throw new ValidationException("validaInvTipoAcquisizioneEccedente", ValidationException.errore);
				}
			}
			if (!ValidazioneDati.strIsNull((rec.getCodOrd()))) {
				if (rec.getCodTipoOrd()!=null && rec.getCodTipoOrd().trim().length()==0){
					throw new ValidationException("validaInvCodTipoOrdObbligatorio", ValidationException.errore);
				}
				if (rec.getCodTipoOrd() !=null &&  rec.getCodTipoOrd().length()!=0)	{
					if (rec.getCodTipoOrd().trim().length()>1)	{
						throw new ValidationException("validaInvCodTipoOrdEccedente", ValidationException.errore);
					}
				}
				if (rec.getCodBibO() !=null &&  rec.getCodBibO().length()!=0)	{
					if (rec.getCodBibO().trim().length()>3)	{
						throw new ValidationException("validaInvCodBibOEccedente", ValidationException.errore);
					}
				}
				if (!this.strIsNull(String.valueOf(rec.getAnnoOrd()))) {
					if (String.valueOf(rec.getAnnoOrd()).length()>9) {
						throw new ValidationException("validaInvAnnoOrdEccedente", ValidationException.errore);
					}
				}
				if (!this.strIsNull(String.valueOf(rec.getCodOrd()))) {
					if (String.valueOf(rec.getCodOrd()).length()>9) {
						throw new ValidationException("validaInvCodOrdEccedente", ValidationException.errore);
					}
				}
				if (!this.strIsNull(String.valueOf(rec.getRigaOrd()))) {
					if (String.valueOf(rec.getRigaOrd()).length()>9) {
						throw new ValidationException("validaInvRigaOrdEccedente", ValidationException.errore);
					}
				}
			}
			if (rec.getCodBibF() !=null &&  rec.getCodBibF().length()!=0)	{
				if (rec.getCodBibF().trim().length()>3)	{
					throw new ValidationException("validaInvCodBibFEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getAnnoFattura()))) {
				if (String.valueOf(rec.getAnnoFattura()).length()>9) {
					throw new ValidationException("validaInvAnnoFatturaEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getProgrFattura()))) {
				if (String.valueOf(rec.getProgrFattura()).length()>9) {
					throw new ValidationException("validaInvProgrFatturaEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getRigaFattura()))) {
				if (String.valueOf(rec.getRigaFattura()).length()>9) {
					throw new ValidationException("validaInvRigaFatturaEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodNoDisp() !=null &&  rec.getCodNoDisp().length()!=0)	{
				if (rec.getCodNoDisp().trim().length()>2)	{
					throw new ValidationException("validaInvCodNoDispEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodFrui() !=null &&  rec.getCodFrui().length()!=0)	{
				if (rec.getCodFrui().trim().length()>2)	{
					throw new ValidationException("validaInvCodFruiEccedente", ValidationException.errore);
				}
				if (rec.getCodFrui().trim().equals("$"))	{
					rec.setCodFrui("");
				}
			}
			if (rec.getCodCarico() !=null &&  rec.getCodCarico().length()!=0)	{
				if (rec.getCodCarico().trim().length()>1)	{
					throw new ValidationException("validaInvCodCaricoEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getNumCarico()))) {
				if (String.valueOf(rec.getNumCarico()).length()>9) {
					throw new ValidationException("validaInvNumCaricoEccedente", ValidationException.errore);
				}
			}
			if (rec.getDataCarico()!=null && rec.getDataCarico().length()!=0) {
				if (rec.getDataCarico().equals("00/00/0000")){
				}else{
					int codRitorno = ValidazioneDati.validaDataPassata(rec.getDataCarico());
					if (codRitorno != ValidazioneDati.DATA_OK)
						throw new ValidationException("validaInvDataCaricoErrata", ValidationException.errore);
				}
			}
			if (rec.getCodPoloScar() !=null &&  rec.getCodPoloScar().length()!=0)	{
				if (rec.getCodPoloScar().trim().length()>3)	{
					throw new ValidationException("validaInvCodPoloScarEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBibS() !=null &&  rec.getCodBibS().length()!=0)	{
				if (rec.getCodBibS().trim().length()>3)	{
					throw new ValidationException("validaInvCodBibSEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodScarico() !=null &&  rec.getCodScarico().length()!=0)	{
				if (rec.getCodScarico().trim().length()>1)	{
					throw new ValidationException("validaInvCodScaricoEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getNumScarico()))) {
				if (String.valueOf(rec.getNumScarico()).length()>9) {
					throw new ValidationException("validaInvNumScaricoEccedente", ValidationException.errore);
				}
			}
			if (rec.getDataScarico()!=null && rec.getDataScarico().length()!=0) {
				if (rec.getDataScarico().equals("00/00/0000")){
				}else{
					int codRitorno = ValidazioneDati.validaDataPassata(rec.getDataScarico());
					if (codRitorno != ValidazioneDati.DATA_OK)
						throw new ValidationException("validaInvDataScaricoErrata", ValidationException.errore);
				}
			}
			if (rec.getDataDelibScar()!=null && rec.getDataDelibScar().length()!=0) {
				if (rec.getDataDelibScar().equals("00/00/0000")){
				}else{
					int codRitorno = ValidazioneDati.validaDataPassata(rec.getDataDelibScar());
					if (codRitorno != ValidazioneDati.DATA_OK)
						throw new ValidationException("validaInvDataDelibScarErrata", ValidationException.errore);
				}
			}
			if (rec.getDeliberaScarico() !=null &&  rec.getDeliberaScarico().length()!=0)	{
				if (rec.getDeliberaScarico().trim().length()>50)	{
					throw new ValidationException("validaInvDeliberaScaricoEccedente", ValidationException.errore);
				}
			}
			if (rec.getSezOld() !=null &&  rec.getSezOld().length()!=0)	{
				if (rec.getSezOld().trim().length()>10)	{
					throw new ValidationException("validaInvSezOldEccedente", ValidationException.errore);
				}
			}
			if (rec.getLocOld() !=null &&  rec.getLocOld().length()!=0)	{
				if (rec.getLocOld().trim().length()>24)	{
					throw new ValidationException("validaInvLocOldEccedente", ValidationException.errore);
				}
			}
			if (rec.getSpecOld() !=null &&  rec.getSpecOld().length()!=0)	{
				if (rec.getSpecOld().trim().length()>12)	{
					throw new ValidationException("validaInvSpecOldEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodSupporto() !=null &&  rec.getCodSupporto().length()!=0)	{
				if (rec.getCodSupporto().trim().length()>2)	{
					throw new ValidationException("validaInvCodSupportoEccedente", ValidationException.errore);
				}
			}
//			if (rec.getUteInsPrimaColl()!=null && rec.getUteInsPrimaColl().trim().length()==0){
//			throw new ValidationException("validaInvUteInsPrimaCollObbligatorio", ValidationException.errore);
//			}
			if (rec.getUteInsPrimaColl() !=null &&  rec.getUteInsPrimaColl().length()!=0)	{
				if (rec.getUteInsPrimaColl().trim().length()>12)	{
					throw new ValidationException("validaInvUteInsPrimaCollEccedente", ValidationException.errore);
				}
			}
			if (rec.getUteIns()!=null && rec.getUteIns().trim().length()==0){
				throw new ValidationException("validaInvUteInsObbligatorio", ValidationException.errore);
			}
			if (rec.getUteIns() !=null &&  rec.getUteIns().length()!=0)	{
				if (rec.getUteIns().trim().length()>12)	{
					throw new ValidationException("validaInvUteInsEccedente", ValidationException.errore);
				}
			}
			if ((DocumentoFisicoCostant.C_PROGRESSIVO_CORRENTE.equals(tipoOperazione) ||
					DocumentoFisicoCostant.P_PROGRESSIVO_PREGRESSO.equals(tipoOperazione))
					&&  (rec.getCodInvent()>0)){
				throw new ValidationException("invIncongrTipoOpNumInv34", ValidationException.errore);
			}
			if ((DocumentoFisicoCostant.S_INVENTARIO_PRESENTE.equals(tipoOperazione) ||
					DocumentoFisicoCostant.N_INVENTARIO_NON_PRESENTE.equals(tipoOperazione))
					&&  (rec.getCodInvent()==0)){
				throw new ValidationException("invNumObbl",	ValidationException.errore);
			}
//			if (nInv == 0) {
//			throw new ValidationException("invNumObbl",	ValidationException.errore);
//			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
		//
	}
	public	void validaInvFattura (InventarioVO rec)
	throws ValidationException {
		try {

			if (rec.getCodBibF() == null || (rec.getCodBibF()!=null && rec.getCodBibF().trim().length()==0)){
				throw new ValidationException("validaInvCodBibObbligatorio", ValidationException.errore);
			}
			if (rec.getCodBibF() !=null &&  rec.getCodBibF().length()!=0)	{
				if (rec.getCodBibF().length()>3)	{
					throw new ValidationException("validaInvCodBibFEccedente", ValidationException.errore);
				}
			}
			if (this.strIsNull((rec.getValore())) ||
					(String.valueOf(rec.getValore()).trim().equals("0.000") ||
							String.valueOf(rec.getValore()).trim().equals("0")
//							||
//							!this.strIsNumeric(rec.getValore().replace(".",""))
					)) {
				throw new ValidationException("validaInvValoreObbligatorio", ValidationException.errore);
			}
			if (!this.strIsNull((rec.getImporto()))) {
				if ((rec.getImporto()).length()>12 ) {
					throw new ValidationException("validaInvImportoEccedente", ValidationException.errore);
				}
			}
//			if (rec.getImportoDouble()==0) {
//			throw new ValidationException("validaInvImportoObbligatorio", ValidationException.errore);
//			}
			if (rec.getDataFattura() == null || (rec.getDataFattura()!=null && rec.getDataFattura().trim().length()==0)){
				throw new ValidationException("validaInvDataFatturaObbligatorio", ValidationException.errore);
			}
			if (!this.strIsNull(String.valueOf(rec.getDataFattura()))) {
				if (rec.getDataFattura()!=null && rec.getDataFattura().length()!=0) {
					int codRitorno = ValidazioneDati.validaDataPassata(rec.getDataFattura());
					if (codRitorno != ValidazioneDati.DATA_OK)
						throw new ValidationException("validaInvDataFatturaErrata", ValidationException.errore);
				}
				if (String.valueOf(rec.getDataFattura()).trim().length()>10) {
					throw new ValidationException("validaInvDataFatturaEccedente", ValidationException.errore);
				}
			}
			if (rec.getNumFattura() == null || (rec.getNumFattura()!=null && rec.getNumFattura().trim().length()==0)){
				throw new ValidationException("validaInvNumFatturaObbligatorio", ValidationException.errore);
			}
			if (!this.strIsNull(String.valueOf(rec.getNumFattura()))) {
				if (String.valueOf(rec.getNumFattura()).length()>20) {
					throw new ValidationException("validaInvNumFatturaEccedente", ValidationException.errore);
				}
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
		//
	}
	public	void validaRegistroIngresso (StampaRegistroVO rec)
	throws ValidationException {
		try {

			if (rec.getCodPolo() == null || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
				throw new ValidationException("codBibObbligatorio", ValidationException.errore);
			}
			if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
				if (rec.getCodPolo().length()>3)	{
					throw new ValidationException("codPoloEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodeBiblioteca() == null || (rec.getCodeBiblioteca()!=null && rec.getCodeBiblioteca().trim().length()==0)){
				throw new ValidationException("codBibObbligatorio", ValidationException.errore);
			}
			if (rec.getCodeBiblioteca() !=null &&  rec.getCodeBiblioteca().length()!=0)	{
				if (rec.getCodeBiblioteca().length()>3)	{
					throw new ValidationException("codBibEccedente", ValidationException.errore);
				}
			}
			if (rec.getSerieInv()!=null && rec.getSerieInv().length()!=0) {
				if (rec.getSerieInv().length()>3) {
					throw new ValidationException("codSerieEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getCodeInvDa()))) {
				if (rec.getCodeInvDa().trim().length()>0){
					if (this.strIsNumeric(String.valueOf(rec.getCodeInvDa()))){
						if (String.valueOf(rec.getCodeInvDa()).length()>9) {
							throw new ValidationException("codInventDaEccedente", ValidationException.errore);
						}
					}else{
						throw new ValidationException("codInventNumerico", ValidationException.errore);
					}
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getCodeInvA()))) {
				if (String.valueOf(rec.getCodeInvA()).trim().length()>0){
					if (this.strIsNumeric(String.valueOf(rec.getCodeInvA()))){
						if (String.valueOf(rec.getCodeInvA()).length()>9) {
							throw new ValidationException("codInventAEccedente", ValidationException.errore);
						}
					}else{
						throw new ValidationException("codInventNumerico", ValidationException.errore);
					}
				}
			}
			if (rec.getDataDa()!=null && rec.getDataDa().length()!=0) {
				int codRitorno = ValidazioneDati.validaData(rec.getDataDa());
				if (codRitorno != ValidazioneDati.DATA_OK)
					throw new ValidationException("dataDaErrataDa", ValidationException.errore);
			}
			if (rec.getDataA()!=null && rec.getDataA().length()!=0) {
				int codRitorno = ValidazioneDati.validaData(rec.getDataA());
				if (codRitorno != ValidazioneDati.DATA_OK)
					throw new ValidationException("dataAErrataA", ValidationException.errore);
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
		//
	}
	public	void validaCollocazione (CollocazioneVO rec)
	throws ValidationException {
		try {

			if (rec.getCodPolo() == null || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
				throw new ValidationException("validaCollCodPoloObbligatorio", ValidationException.errore);
			}
			if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
				if (rec.getCodPolo().length()>3)	{
					throw new ValidationException("validaCollCodPoloEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBib() == null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
				throw new ValidationException("validaCollCodBibObbligatorio", ValidationException.errore);
			}
			if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
				if (rec.getCodBib().length()>3)	{
					throw new ValidationException("validaCollCodBibEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodSistema()!=null && rec.getCodSistema().length()!=0) {
				if (rec.getCodSistema().length()>1) {
					throw new ValidationException("validaCollCodSistemaEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodEdizione()!=null && rec.getCodEdizione().length()!=0) {
				if (rec.getCodEdizione().length()>2) {
					throw new ValidationException("validaCollCodEdizioneEccedente", ValidationException.errore);
				}
			}
			if (rec.getClasse()!=null && rec.getClasse().length()!=0) {
				if (rec.getClasse().length()>31) {
					throw new ValidationException("validaCollClasseEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodPoloSez() == null || (rec.getCodPoloSez()!=null && rec.getCodPoloSez().trim().length()==0)){
				throw new ValidationException("validaCollCodPoloSezObbligatorio", ValidationException.errore);
			}
			if (rec.getCodPoloSez()!=null && rec.getCodPoloSez().length()!=0) {
				if (rec.getCodPoloSez().length()>3) {
					throw new ValidationException("validaCollCodPoloSezEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBibSez() == null || (rec.getCodBibSez()!=null && rec.getCodBibSez().trim().length()==0)){
				throw new ValidationException("validaCollCodBibSezObbligatorio", ValidationException.errore);
			}
			if (rec.getCodBibSez()!=null && rec.getCodBibSez().length()!=0) {
				if (rec.getCodBibSez().length()>3) {
					throw new ValidationException("validaCollCodBibSezEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodSez() == null){
				throw new ValidationException("validaCollCodSezObbligatorio", ValidationException.errore);
			}
			if (rec.getCodSez() == null || rec.getCodSez()!=null && rec.getCodSez().length()!=0) {
				if (rec.getCodSez().length()>10) {
					throw new ValidationException("validaCollCodSezEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodPoloDoc()!=null && rec.getCodPoloDoc().length()!=0) {
				if (rec.getCodPoloDoc().length()>3) {
					throw new ValidationException("validaCollCodPoloDocEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBibDoc()!=null && rec.getCodBibDoc().length()!=0) {
				if (rec.getCodBibDoc().length()>3) {
					throw new ValidationException("validaCollCodBibDocEccedente", ValidationException.errore);
				}
			}
			if (rec.getBidDoc()!=null && rec.getBidDoc().length()!=0) {
				if (rec.getBidDoc().length()>10) {
					throw new ValidationException("validaCollBidDocEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getCodDoc()))) {
				if (String.valueOf(rec.getCodDoc()).length()>9) {
					throw new ValidationException("validaCollCodDocEccedente", ValidationException.errore);
				}
			}
			if (String.valueOf(rec.getKeyColloc()) == null) {
				throw new ValidationException("validaCollKeyCollocObbligatorio", ValidationException.errore);
			}
			if (!this.strIsNull(String.valueOf(rec.getKeyColloc()))) {
				if (String.valueOf(rec.getKeyColloc()).length()>9) {
					throw new ValidationException("validaCollKeyCollocEccedente", ValidationException.errore);
				}
			}
			if (rec.getBid() == null || (rec.getBid()!=null && rec.getBid().trim().length()==0)){
				throw new ValidationException("validaCollBidObbligatorio", ValidationException.errore);
			}
			if (rec.getBid()!=null && rec.getBid().length()!=0) {
				if (rec.getBid().length()>10) {
					throw new ValidationException("validaCollBidEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodColloc() == null/* || (rec.getCodColloc()!=null && rec.getCodColloc().trim().length()==0)*/){
				throw new ValidationException("validaCollCodCollocObbligatorio", ValidationException.errore);
			}
			if (rec.getCodColloc()!=null && rec.getCodColloc().length()!=0) {
				if (rec.getCodColloc().length()>24) {
					throw new ValidationException("validaCollCodCollocEccedente", ValidationException.errore);
				}
			}
			if (rec.getSpecColloc() == null /*|| (rec.getSpecColloc()!=null && rec.getSpecColloc().trim().length()==0)*/){
				throw new ValidationException("validaCollSpecCollocObbligatorio", ValidationException.errore);
			}
			if (rec.getSpecColloc()!=null && rec.getSpecColloc().length()!=0) {
				if (rec.getSpecColloc().length()>12) {
					throw new ValidationException("validaCollCodCollocEccedente", ValidationException.errore);
				}
			}
			//almaviva5_20140314 segnalazione RMR
			if (ValidazioneDati.length(rec.getConsistenza()) > 255)
				throw new ValidationException("validaCollConsistenzaEccedente", ValidationException.errore);

			if (String.valueOf(rec.getTotInv()) == null) {
				throw new ValidationException("validaCollTotInvObbligatorio", ValidationException.errore);
			}
			if (!this.strIsNull(String.valueOf(rec.getTotInv()))) {
				if (String.valueOf(rec.getTotInv()).length()>9) {
					throw new ValidationException("validaCollTotInvEccedente", ValidationException.errore);
				}
			}
			if (rec.getIndice()!=null && rec.getIndice().length()!=0) {
				if (rec.getIndice().length()>31) {
					throw new ValidationException("validaCollIndiceEccedente", ValidationException.errore);
				}
			}
			if (rec.getOrdLoc() == null/* || (rec.getOrdLoc()!=null && rec.getOrdLoc().trim().length()==0)*/){
				throw new ValidationException("validaCollOrdLocObbligatorio", ValidationException.errore);
			}
			if (rec.getOrdLoc()!=null && rec.getOrdLoc().length()!=0) {
				if (rec.getOrdLoc().length()>80) {
					throw new ValidationException("validaCollOrdLocEccedente", ValidationException.errore);
				}
			}
			if (rec.getOrdSpec() == null/* || (rec.getOrdSpec()!=null && rec.getOrdSpec().trim().length()==0)*/){
				throw new ValidationException("validaCollOrdSpecObbligatorio", ValidationException.errore);
			}
			if (rec.getOrdSpec()!=null && rec.getOrdSpec().length()!=0) {
				if (rec.getOrdSpec().length()>40) {
					throw new ValidationException("validaCollOrdSpecEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getTotInvProv()))) {
				if (String.valueOf(rec.getTotInvProv()).length()>9) {
					throw new ValidationException("validaCollTotInvProvEccedente", ValidationException.errore);
				}
			}
			if (rec.getUteIns()!=null && rec.getUteIns().trim().length()==0){
				throw new ValidationException("validaCollUteInsObbligatorio", ValidationException.errore);
			}
			if (rec.getUteIns() !=null &&  rec.getUteIns().length()!=0)	{
				if (rec.getUteIns().trim().length()>12)	{
					throw new ValidationException("validaCollUteInsEccedente", ValidationException.errore);
				}
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
	}
	public	void validaCollocazionePerListaCollocazioniDiEsemplare(CollocazioneVO rec)
	throws ValidationException {
		try {
			if (rec.getCodPolo() == null || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
				throw new ValidationException("validaCollCodPoloObbligatorio", ValidationException.errore);
			}
			if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
				if (rec.getCodPolo().length()>3)	{
					throw new ValidationException("validaCollCodPoloEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBib() == null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
				throw new ValidationException("validaCollCodBibObbligatorio", ValidationException.errore);
			}
			if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
				if (rec.getCodBib().length()>3)	{
					throw new ValidationException("validaCollCodBibEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodSistema()!=null && rec.getCodSistema().length()!=0) {
				if (rec.getCodSistema().length()>1) {
					throw new ValidationException("validaCollCodSistemaEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodEdizione()!=null && rec.getCodEdizione().length()!=0) {
				if (rec.getCodEdizione().length()>2) {
					throw new ValidationException("validaCollCodEdizioneEccedente", ValidationException.errore);
				}
			}
			if (rec.getClasse()!=null && rec.getClasse().length()!=0) {
				if (rec.getClasse().length()>31) {
					throw new ValidationException("validaCollClasseEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodPoloSez() == null || (rec.getCodPoloSez()!=null && rec.getCodPoloSez().trim().length()==0)){
				throw new ValidationException("validaCollCodPoloSezObbligatorio", ValidationException.errore);
			}
			if (rec.getCodPoloSez()!=null && rec.getCodPoloSez().length()!=0) {
				if (rec.getCodPoloSez().length()>3) {
					throw new ValidationException("validaCollCodPoloSezEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBibSez() == null || (rec.getCodBibSez()!=null && rec.getCodBibSez().trim().length()==0)){
				throw new ValidationException("validaCollCodBibSezObbligatorio", ValidationException.errore);
			}
			if (rec.getCodBibSez()!=null && rec.getCodBibSez().length()!=0) {
				if (rec.getCodBibSez().length()>3) {
					throw new ValidationException("validaCollCodBibSezEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodSez() == null){
				throw new ValidationException("validaCollCodSezObbligatorio", ValidationException.errore);
			}
			if (rec.getCodSez() == null || rec.getCodSez()!=null && rec.getCodSez().length()!=0) {
				if (rec.getCodSez().length()>10) {
					throw new ValidationException("validaCollCodSezEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodPoloDoc()!=null && rec.getCodPoloDoc().length()!=0) {
				if (rec.getCodPoloDoc().length()>3) {
					throw new ValidationException("validaCollCodPoloDocEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBibDoc()!=null && rec.getCodBibDoc().length()!=0) {
				if (rec.getCodBibDoc().length()>3) {
					throw new ValidationException("validaCollCodBibDocEccedente", ValidationException.errore);
				}
			}
			if (rec.getBidDoc()!=null && rec.getBidDoc().length()!=0) {
				if (rec.getBidDoc().length()>10) {
					throw new ValidationException("validaCollBidDocEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getCodDoc()))) {
				if (String.valueOf(rec.getCodDoc()).length()>9) {
					throw new ValidationException("validaCollCodDocEccedente", ValidationException.errore);
				}
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
	}
	public	void validaInventario (InventarioVO rec)
	throws ValidationException {
		try {

			this.validaInventarioKey(rec);
			if (rec.getCodProven()!=null && rec.getCodProven().trim().length()!=0) {
				if (rec.getCodProven().length()>5) {
					throw new ValidationException("validaInvCodProvenEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodPoloProven() !=null &&  rec.getCodPoloProven().length()!=0)	{
				if (rec.getCodPoloProven().length()>3)	{
					throw new ValidationException("validaInvCodPoloProvenEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBibProven() !=null &&  rec.getCodBibProven().length()!=0)	{
				if (rec.getCodBibProven().length()>3)	{
					throw new ValidationException("validaInvCodBibProvenEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getKeyLoc()))) {
				if (String.valueOf(rec.getKeyLoc()).length()>9) {
					throw new ValidationException("validaInvKeyLocEccedente", ValidationException.errore);
				}
			}
			if (rec.getBid() !=null &&  rec.getBid().length()!=0)	{
				if (rec.getBid().length()>10)	{
					throw new ValidationException("validaInvBidEccedente", ValidationException.errore);
				}
			}
//			if (rec.getSeqColl()!=null && rec.getSeqColl().trim().length()==0){
//			throw new ValidationException("validaInvSeqCollObbligatorio", ValidationException.errore);
//			}
			if (rec.getSeqColl() !=null &&  rec.getSeqColl().length()!=0)	{
				if (rec.getSeqColl().trim().length()>20)	{
					throw new ValidationException("validaInvSeqCollEccedente", ValidationException.errore);
				}
			}
			if (rec.getPrecInv() !=null &&  rec.getPrecInv().length()!=0)	{
//				if (rec.getPrecInv().trim().length()>255)	{//bug 4728 collaudo
//					throw new ValidationException("validaInvPrecInvEccedente", ValidationException.errore);
//				}
			}
			if (rec.getCodSit()!=null && rec.getCodSit().trim().length()==0){
				throw new ValidationException("validaInvCodSitObbligatorio", ValidationException.errore);
			}
			if (rec.getCodSit() !=null &&  rec.getCodSit().length()!=0)	{
				if (rec.getCodSit().trim().length()>1)	{
					throw new ValidationException("validaInvCodSitEccedente", ValidationException.errore);
				}
			}
			if (this.strIsNull((rec.getValore())) ||
					(String.valueOf(rec.getValore()).trim().equals("0.000") ||
							String.valueOf(rec.getValore()).trim().equals("0")
//							||
//							!this.strIsNumeric(rec.getValore().replace(".",""))
					)) {
				throw new ValidationException("validaInvValoreObbligatorio", ValidationException.errore);
			}
			if (!this.strIsNull((rec.getValore()))) {
				if ((rec.getValore()).length()>14 ) {
					throw new ValidationException("validaInvValoreEccedente", ValidationException.errore);
				}
				if (rec.getValoreDouble() > ServiziConstant.MAX_VALORE){
					throw new ValidationException("validaInvValoreEccedente", ValidationException.errore);
				}
			}
			if (rec.getValoreDouble()==0) {
				throw new ValidationException("validaInvValoreObbligatorio", ValidationException.errore);
			}
			if (!this.strIsNull((rec.getImporto()))) {
				if ((rec.getImporto()).length()>14) {
					throw new ValidationException("validaInvImportoEccedente", ValidationException.errore);
				}
				if (rec.getImportoDouble() > ServiziConstant.MAX_VALORE){
					throw new ValidationException("validaInvImportoEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getNumVol()))) {
				if (String.valueOf(rec.getNumVol()).length()>9) {
					throw new ValidationException("validaInvNumVolEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getTotLoc()))) {
				if (String.valueOf(rec.getTotLoc()).length()>9) {
					throw new ValidationException("validaInvTotLocEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getTotInter()))) {
				if (String.valueOf(rec.getTotInter()).length()>9) {
					throw new ValidationException("validaInvTotInterEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getAnnoAbb()))) {
				if (String.valueOf(rec.getAnnoAbb()).length()>9) {
					throw new ValidationException("validaInvAnnoAbbEccedente", ValidationException.errore);
				}
			}
//			if (rec.getFlagDisp()!=null && rec.getFlagDisp().trim().length()==0){
//			throw new ValidationException("validaInvFlagDispObbligatorio", ValidationException.errore);
//			}
			if (rec.getFlagDisp() !=null &&  rec.getFlagDisp().length()!=0)	{
				if (rec.getFlagDisp().trim().length()>1)	{
					throw new ValidationException("validaInvFlagDispEccedente", ValidationException.errore);
				}
			}
//			if (rec.getFlagNU()!=null && rec.getFlagNU().trim().length()==0){
//			throw new ValidationException("validaInvFlagNUObbligatorio", ValidationException.errore);
//			}
			if (rec.getFlagNU() !=null &&  rec.getFlagNU().length()!=0)	{
				if (rec.getFlagNU().trim().length()>1)	{
					throw new ValidationException("validaInvFlagNUEccedente", ValidationException.errore);
				}
			}
//			if (rec.getStatoConser()!=null && rec.getStatoConser().trim().length()==0){
//			throw new ValidationException("validaInvStatoConserObbligatorio", ValidationException.errore);
//			}
			if (rec.getStatoConser() !=null &&  rec.getStatoConser().length()!=0)	{
				if (rec.getStatoConser().trim().length()>2)	{
					throw new ValidationException("validaInvStatoConserEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull((rec.getCodFornitore()))) {
				if ((rec.getCodFornitore()).length()>9) {
					throw new ValidationException("validaInvCodFornitoreEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodMatInv()!=null && rec.getCodMatInv().trim().length()==0){
				throw new ValidationException("validaInvCodMatInvObbligatorio", ValidationException.errore);
			}
			if (rec.getCodMatInv() !=null &&  rec.getCodMatInv().length()!=0)	{
				if (rec.getCodMatInv().trim().length()>2)	{
					throw new ValidationException("validaInvCodMatInvEccedente", ValidationException.errore);
				}
			}
			//if (rec.getCodTipoOrd()!=null && rec.getCodTipoOrd().trim().length()==0){
			if (ValidazioneDati.strIsNull(rec.getTipoAcquisizione()) ) {
				throw new ValidationException("validaInvTipoAcquisizioneObbligatorio", ValidationException.errore);
			}
			if (rec.getTipoAcquisizione() !=null &&  rec.getTipoAcquisizione().length()!=0)	{
				if (rec.getTipoAcquisizione().trim().length()>1)	{
					throw new ValidationException("validaInvTipoAcquisizioneEccedente", ValidationException.errore);
				}
			}
			if (!ValidazioneDati.strIsNull((rec.getCodOrd()))){
				if (rec.getCodBibO() !=null &&  rec.getCodBibO().length()!=0)	{
					if (rec.getCodBibO().trim().length()>3)	{
						throw new ValidationException("validaInvCodBibOEccedente", ValidationException.errore);
					}
				}
				if (ValidazioneDati.strIsNull(rec.getCodTipoOrd()) ) {
					throw new ValidationException("validaInvCodTipoOrdObbligatorio", ValidationException.errore);
				}
				if (rec.getCodTipoOrd() !=null &&  rec.getCodTipoOrd().length()!=0)	{
					if (rec.getCodTipoOrd().trim().length()>1)	{
						throw new ValidationException("validaInvCodTipoOrdEccedente", ValidationException.errore);
					}
				}
				if (!this.strIsNull((rec.getAnnoOrd()))) {
					if ((rec.getAnnoOrd()).length()>9) {
						throw new ValidationException("validaInvAnnoOrdEccedente", ValidationException.errore);
					}
				}
				if (!this.strIsNull((rec.getCodOrd()))) {
					if ((rec.getCodOrd()).length()>9) {
						throw new ValidationException("validaInvCodOrdEccedente", ValidationException.errore);
					}
				}
				if (!this.strIsNull((rec.getRigaOrd()))) {
					if ((rec.getRigaOrd()).length()>9) {
						throw new ValidationException("validaInvRigaOrdEccedente", ValidationException.errore);
					}
				}
			}
			if (rec.getCodBibF() !=null &&  rec.getCodBibF().length()!=0)	{
				if (rec.getCodBibF().trim().length()>3)	{
					throw new ValidationException("validaInvCodBibFEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull((rec.getAnnoFattura()))) {
				if ((rec.getAnnoFattura()).length()>9) {
					throw new ValidationException("validaInvAnnoFatturaEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull((rec.getProgrFattura()))) {
				if ((rec.getProgrFattura()).length()>9) {
					throw new ValidationException("validaInvProgrFatturaEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull((rec.getRigaFattura()))) {
				if ((rec.getRigaFattura()).length()>9) {
					throw new ValidationException("validaInvRigaFatturaEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodNoDisp() !=null &&  rec.getCodNoDisp().length()!=0)	{
				if (rec.getCodNoDisp().trim().length()>2)	{
					throw new ValidationException("validaInvCodNoDispEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodFrui()!=null && rec.getCodFrui().trim().length()==0){
				throw new ValidationException("validaInvCodFruiObbligatorio", ValidationException.errore);
			}
			if (rec.getCodFrui() !=null &&  rec.getCodFrui().length()!=0)	{
				if (rec.getCodFrui().trim().length()>2)	{
					throw new ValidationException("validaInvCodFruiEccedente", ValidationException.errore);
				}
				if (rec.getCodFrui().trim().equals("$"))	{
					rec.setCodFrui("");
				}
			}
			if (rec.getCodCarico() !=null &&  rec.getCodCarico().length()!=0)	{
				if (rec.getCodCarico().trim().length()>1)	{
					throw new ValidationException("validaInvCodCaricoEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull((rec.getNumCarico()))) {
				if ((rec.getNumCarico()).length()>9) {
					throw new ValidationException("validaInvNumCaricoEccedente", ValidationException.errore);
				}
			}
			if (rec.getDataCarico()!=null && rec.getDataCarico().length()!=0) {
				if (rec.getDataCarico().equals("00/00/0000")){
				}else{
					int codRitorno = ValidazioneDati.validaDataPassata(rec.getDataCarico());
					if (codRitorno != ValidazioneDati.DATA_OK)
						throw new ValidationException("validaInvDataCaricoErrata", ValidationException.errore);
				}
			}
			if (rec.getCodPoloScar() !=null &&  rec.getCodPoloScar().length()!=0)	{
				if (rec.getCodPoloScar().trim().length()>3)	{
					throw new ValidationException("validaInvCodPoloScarEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBibS() !=null &&  rec.getCodBibS().length()!=0)	{
				if (rec.getCodBibS().trim().length()>3)	{
					throw new ValidationException("validaInvCodBibSEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodScarico() !=null &&  rec.getCodScarico().length()!=0)	{
				if (rec.getCodScarico().trim().length()>1)	{
					throw new ValidationException("validaInvCodScaricoEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull((rec.getNumScarico()))) {
				if ((rec.getNumScarico()).length()>9) {
					throw new ValidationException("validaInvNumScaricoEccedente", ValidationException.errore);
				}
			}
			if (rec.getDataScarico()!=null && rec.getDataScarico().length()!=0) {
				if (rec.getDataScarico().equals("00/00/0000")){
				}else{
					int codRitorno = ValidazioneDati.validaData_1(rec.getDataScarico());
					if (codRitorno != ValidazioneDati.DATA_OK)
						throw new ValidationException("validaInvDataScaricoErrata", ValidationException.errore);
				}
			}
			if (rec.getDataDelibScar()!=null && rec.getDataDelibScar().length()!=0) {
				if (rec.getDataDelibScar().equals("00/00/0000")){
				}else{
					int codRitorno = ValidazioneDati.validaData_1(rec.getDataDelibScar());
					if (codRitorno != ValidazioneDati.DATA_OK)
						throw new ValidationException("validaInvDataDelibScarErrata", ValidationException.errore);
				}
			}
			if (rec.getDeliberaScarico() !=null &&  rec.getDeliberaScarico().length()!=0)	{
				if (rec.getDeliberaScarico().trim().length()>50)	{
					throw new ValidationException("validaInvDeliberaScaricoEccedente", ValidationException.errore);
				}
			}
			if (rec.getSezOld() !=null &&  rec.getSezOld().length()!=0)	{
				if (rec.getSezOld().trim().length()>10)	{
					throw new ValidationException("validaInvSezOldEccedente", ValidationException.errore);
				}
			}
			if (rec.getLocOld() !=null &&  rec.getLocOld().length()!=0)	{
				if (rec.getLocOld().trim().length()>24)	{
					throw new ValidationException("validaInvLocOldEccedente", ValidationException.errore);
				}
			}
			if (rec.getSpecOld() !=null &&  rec.getSpecOld().length()!=0)	{
				if (rec.getSpecOld().trim().length()>12)	{
					throw new ValidationException("validaInvSpecOldEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodSupporto() !=null &&  rec.getCodSupporto().length()!=0)	{
				if (rec.getCodSupporto().trim().length()>2)	{
					throw new ValidationException("validaInvCodSupportoEccedente", ValidationException.errore);
				}
			}
//			if (rec.getUteInsPrimaColl()!=null && rec.getUteInsPrimaColl().trim().length()==0){
//			throw new ValidationException("validaInvUteInsPrimaCollObbligatorio", ValidationException.errore);
//			}
			if (rec.getUteInsPrimaColl() !=null &&  rec.getUteInsPrimaColl().length()!=0)	{
				if (rec.getUteInsPrimaColl().trim().length()>12)	{
					throw new ValidationException("validaInvUteInsPrimaCollEccedente", ValidationException.errore);
				}
			}
			if (rec.getUteIns()!=null && rec.getUteIns().trim().length()==0){
				throw new ValidationException("validaInvUteInsObbligatorio", ValidationException.errore);
			}
			if (rec.getUteIns() !=null &&  rec.getUteIns().length()!=0)	{
				if (rec.getUteIns().trim().length()>12)	{
					throw new ValidationException("validaInvUteInsEccedente", ValidationException.errore);
				}
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
	}
	//
	public	void validaProvenienzaInventarioLista (ProvenienzaInventarioVO rec)
	throws ValidationException {

		if (rec.getCodPolo() == null || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
			throw new ValidationException("validaProvenienzaInventarioCodPoloObbligatorio", ValidationException.errore);
		}
		if (rec.getCodBib() == null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
			throw new ValidationException("validaProvenienzaInventarioCodBibObbligatorio", ValidationException.errore);
		}
		if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
			if (rec.getCodPolo().length()>3)	{
				throw new ValidationException("validaProvenienzaInventarioCodPoloEccedente", ValidationException.errore);
			}
		}
		if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
			if (rec.getCodBib().length()>3)	{
				throw new ValidationException("validaProvenienzaInventarioCodBibEccedente", ValidationException.errore);
			}
		}
	}
	public	void validaProvenienzaInventario (ProvenienzaInventarioVO rec)
	throws ValidationException {
		try{

			if (rec.getCodPolo() == null || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
				throw new ValidationException("validaProvenienzaInventarioCodPoloObbligatorio", ValidationException.errore);
			}
			if (rec.getCodBib() == null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
				throw new ValidationException("validaProvenienzaInventarioCodBibObbligatorio", ValidationException.errore);
			}
			if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
				if (rec.getCodPolo().length()>3)	{
					throw new ValidationException("validaProvenienzaInventarioCodPoloEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
				if (rec.getCodBib().length()>3)	{
					throw new ValidationException("validaProvenienzaInventarioCodBibEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodProvenienza() == null || (rec.getCodProvenienza()!=null && rec.getCodProvenienza().trim().length()==0)){
				throw new ValidationException("validaProvenienzaInventarioCodProvenienzaObbligatorio", ValidationException.errore);
			}
			if (rec.getCodProvenienza()!=null && rec.getCodProvenienza().trim().length()!=0) {
				if (rec.getCodProvenienza().length()>5) {
					throw new ValidationException("validaProvenienzaInventarioCodProvenienzaEccedente", ValidationException.errore);
				}
			}
			if (rec.getDescrProvenienza()!=null && rec.getDescrProvenienza().trim().length()!=0) {
				if (rec.getDescrProvenienza().length()>255) {
					throw new ValidationException("validaProvenienzaInventarioDescrProvenienzaEccedente", ValidationException.errore);
				}
			}
			if (rec.getUteIns()!=null && rec.getUteIns().trim().length()!=0) {
				if (rec.getUteIns().length()>12) {
					throw new ValidationException("validaProvenienzaInventarioUteInsEccedente", ValidationException.errore);
				}
			}
			if (rec.getUteAgg()!=null && rec.getUteAgg().trim().length()!=0) {
				if (rec.getUteAgg().length()>12) {
					throw new ValidationException("validaProvenienzaInventarioUteAggEccedente", ValidationException.errore);
				}
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
	}

	public	void validaEsemplare (EsemplareVO rec)
	throws ValidationException {
		try{

			if (rec.getCodPolo() == null || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
				throw new ValidationException("validaEsemplareCodPoloObbligatorio", ValidationException.errore);
			}
			if (rec.getCodBib() == null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
				throw new ValidationException("validaEsemplareCodBibObbligatorio", ValidationException.errore);
			}
			if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
				if (rec.getCodPolo().length()>3)	{
					throw new ValidationException("validaEsemplareCodPoloEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
				if (rec.getCodBib().length()>3)	{
					throw new ValidationException("validaEsemplareCodBibEccedente", ValidationException.errore);
				}
			}
			if (rec.getBid() == null || (rec.getBid()!=null && rec.getBid().trim().length()==0)){
				throw new ValidationException("validaEsemplareBidObbligatorio", ValidationException.errore);
			}
			if (rec.getBid()!=null && rec.getBid().trim().length()!=0) {
				if (rec.getBid().length()>10) {
					throw new ValidationException("validaEsemplareBidEccedente", ValidationException.errore);
				}
			}
			//		if (rec.getCodDoc()== null/* || (getCollCodLoc()!=null && getCollCodLoc().trim().length()==0)*/){
			//		throw new ValidationException("validaEsemplareCodLocObbligatorio", ValidationException.errore);
			//		}
			if (rec.getCodDoc()!=null && String.valueOf(rec.getCodDoc()).length() != 0) {
				if (String.valueOf(rec.getCodDoc()).length()>9) {
					throw new ValidationException("validaEsemplareCodLocEccedente", ValidationException.errore);
				}
			}
			if (rec.getConsDoc()!=null && rec.getConsDoc().trim().length()!=0) {
				if (rec.getConsDoc().length() > 800) {
					throw new ValidationException("validaEsemplareBidEccedente", ValidationException.errore);
				}
			}
			//		if (rec.getUteIns()!=null && rec.getUteIns().trim().length()!=0) {
			//		if (rec.getUteIns().length()>12) {
			//		throw new ValidationException("validaEsemplareUteInsEccedente", ValidationException.errore);
			//		}
			//		}
			if (rec.getUteAgg()!=null && rec.getUteAgg().trim().length()!=0) {
				if (rec.getUteAgg().length()>12) {
					throw new ValidationException("validaEsemplareUteAggEccedente", ValidationException.errore);
				}
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
	}

//	public void validaInventarioDettaglio(InventarioVO rec) throws ValidationException
//	{
//		if ((rec.getCodPolo() ==null) || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
//			throw new ValidationException("validaInvDettCodPoloObbligatorio", ValidationException.errore);
//		}
//		if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
//			if (rec.getCodPolo().length()>3)	{
//				throw new ValidationException("validaInvDettCodPoloEccedente", ValidationException.errore);
//			}
//		}
//		if (rec.getCodBib() == null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
//			throw new ValidationException("validaInvDettCodBibObbligatorio", ValidationException.errore);
//		}
//		if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
//			if (rec.getCodBib().length()>3)	{
//				throw new ValidationException("validaInvDettCodBibEccedente", ValidationException.errore);
//			}
//		}
//		if (rec.getCodSerie()!=null && rec.getCodSerie().length()==0){
//			throw new ValidationException("validaInvDettCodSerieObbligatorio", ValidationException.errore);
//		}
//		if (rec.getCodSerie()!=null && rec.getCodSerie().length()!=0) {
//			if (rec.getCodSerie().length()>3) {
//				throw new ValidationException("validaInvDettCodSerieEccedente", ValidationException.errore);
//			}
//		}
//		if (!this.strIsNull(String.valueOf(rec.getCodInvent()))) {
//			if (String.valueOf(rec.getCodInvent()).length()>9) {
//				throw new ValidationException("validaInvDettCodInventEccedente", ValidationException.errore);
//			}
//		}
//
//	}
	public void validaInventarioKey(InventarioVO rec)
	throws ValidationException {
		try{
			if ((rec.getCodPolo() ==null) || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
				throw new ValidationException("validaInvDettCodPoloObbligatorio", ValidationException.errore);
			}
			if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
				if (rec.getCodPolo().length()>3)	{
					throw new ValidationException("validaInvDettCodPoloEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBib() == null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
				throw new ValidationException("validaInvDettCodBibObbligatorio", ValidationException.errore);
			}
			if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
				if (rec.getCodBib().length()>3)	{
					throw new ValidationException("validaInvDettCodBibEccedente", ValidationException.errore);
				}
			}
//			if (rec.getCodSerie()!=null && rec.getCodSerie().length()==0){
//				throw new ValidationException("validaInvDettCodSerieObbligatorio", ValidationException.errore);
//			}
			if (rec.getCodSerie()!=null && rec.getCodSerie().length()!=0) {
				if (rec.getCodSerie().length()>3) {
					throw new ValidationException("validaInvDettCodSerieEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getCodInvent()))) {
				if (String.valueOf(rec.getCodInvent()).length()>9) {
					throw new ValidationException("validaInvDettCodInventEccedente", ValidationException.errore);
				}
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
	}

//	public	void validaInventarioDettaglio (InventarioDettaglioVO rec)
//	throws ValidationException {
//	try {
//	this.validaInventarioKey(rec);
//	if (rec.getKeyLoc() != 0){

//	if (rec.getCollCodSistema()!=null && rec.getCollCodSistema().length()!=0) {
//	if (rec.getCollCodSistema().length()>1) {
//	throw new ValidationException("validaInvDettCodSistemaEccedente", ValidationException.errore);
//	}
//	}
//	if (rec.getCollCodEdizione()!=null && rec.getCollCodEdizione().length()!=0) {
//	if (rec.getCollCodEdizione().length()>2) {
//	throw new ValidationException("validaInvDettCodEdizioneEccedente", ValidationException.errore);
//	}
//	}
//	if (rec.getCollClasse()!=null && rec.getCollClasse().length()!=0) {
//	if (rec.getCollClasse().length()>31) {
//	throw new ValidationException("validaInvDettClasseEccedente", ValidationException.errore);
//	}
//	}
////	if (rec.getCollCodPoloSez().equals(null) || (rec.getCollCodPoloSez()!=null && rec.getCollCodPoloSez().trim().length()==0)){
////	throw new ValidationException("validaCollCodPoloSezObbligatorio", ValidationException.errore);
////	}
//	if (rec.getCollCodPoloSez()!=null && rec.getCollCodPoloSez().length()!=0) {
//	if (rec.getCollCodPoloSez().length()>3) {
//	throw new ValidationException("validaInvDettCodPoloSezEccedente", ValidationException.errore);
//	}
//	}
////	if (rec.getCollCodBibSez().equals(null) || (rec.getCollCodBibSez()!=null && rec.getCollCodBibSez().trim().length()==0)){
////	throw new ValidationException("validaCollCodBibSezObbligatorio", ValidationException.errore);
////	}
//	if (rec.getCollCodBibSez()!=null && rec.getCollCodBibSez().length()!=0) {
//	if (rec.getCollCodBibSez().length()>3) {
//	throw new ValidationException("validaInvDettCodBibSezEccedente", ValidationException.errore);
//	}
//	}
////	if (rec.getCollCodSez().equals(null) || (rec.getCollCodSez()!=null && rec.getCollCodSez().trim().length()==0)){
////	throw new ValidationException("validaCollCodSezObbligatorio", ValidationException.errore);
////	}
//	if (rec.getCollCodSez().equals(null) || rec.getCollCodSez()!=null && rec.getCollCodSez().length()!=0) {
//	if (rec.getCollCodSez().length()>10) {
//	throw new ValidationException("validaInvDettCodSezEccedente", ValidationException.errore);
//	}
//	}
//	if (rec.getCollCodPoloDoc()!=null && rec.getCollCodPoloDoc().length()!=0) {
//	if (rec.getCollCodPoloDoc().length()>3) {
//	throw new ValidationException("validaInvDettCodPoloDocEccedente", ValidationException.errore);
//	}
//	}
//	if (rec.getCollCodBibDoc()!=null && rec.getCollCodBibDoc().length()!=0) {
//	if (rec.getCollCodBibDoc().length()>3) {
//	throw new ValidationException("validaInvDettCodBibDocEccedente", ValidationException.errore);
//	}
//	}
//	if (rec.getCollBidDoc()!=null && rec.getCollBidDoc().length()!=0) {
//	if (rec.getCollBidDoc().length()>10) {
//	throw new ValidationException("validaInvDettBidDocEccedente", ValidationException.errore);
//	}
//	}
//	if (!this.strIsNull(String.valueOf(rec.getCollCodDoc()))) {
//	if (String.valueOf(rec.getCollCodDoc()).length()>9) {
//	throw new ValidationException("validaInvDettCodDocEccedente", ValidationException.errore);
//	}
//	}
//	if (String.valueOf(rec.getCollKeyLoc()).equals(null)) {
//	throw new ValidationException("validaInvDettKeyLocObbligatorio", ValidationException.errore);
//	}
//	if (!this.strIsNull(String.valueOf(rec.getCollKeyLoc()))) {
//	if (String.valueOf(rec.getCollKeyLoc()).length()>9) {
//	throw new ValidationException("validaInvDettKeyLocEccedente", ValidationException.errore);
//	}
//	}
////	if (rec.getCollBidLoc().equals(null) || (rec.getCollBidLoc()!=null && rec.getCollBidLoc().trim().length()==0)){
////	throw new ValidationException("validaCollBidObbligatorio", ValidationException.errore);
////	}
//	if (rec.getCollBidLoc()!=null && rec.getCollBidLoc().length()!=0) {
//	if (rec.getBid().length()>10) {
//	throw new ValidationException("validaInvDettBidLocEccedente", ValidationException.errore);
//	}
//	}
//	if (rec.getCollCodLoc().equals(null)/* || (getCollCodLoc()!=null && getCollCodLoc().trim().length()==0)*/){
//	throw new ValidationException("validaInvDettCodLocObbligatorio", ValidationException.errore);
//	}
//	if (rec.getCollCodLoc()!=null && rec.getCollCodLoc().length()!=0) {
//	if (rec.getCollCodLoc().length()>24) {
//	throw new ValidationException("validaInvDettCodLocEccedente", ValidationException.errore);
//	}
//	}
//	if (rec.getCollSpecLoc().equals(null) /*|| (getCollSpecLoc()!=null && getCollSpecLoc().trim().length()==0)*/){
//	throw new ValidationException("validaInvDettSpecLocObbligatorio", ValidationException.errore);
//	}
//	if (rec.getCollSpecLoc()!=null && rec.getCollSpecLoc().length()!=0) {
//	if (rec.getCollSpecLoc().length()>12) {
//	throw new ValidationException("validaInvDettCodLocEccedente", ValidationException.errore);
//	}
//	}
//	if (rec.getCollConsis()!=null && rec.getCollConsis().length()!=0) {
//	if (rec.getCollConsis().length()>255) {
//	throw new ValidationException("validaInvDettConsistenzaEccedente", ValidationException.errore);
//	}
//	}
//	if (rec.getCollIndice()!=null && rec.getCollIndice().length()!=0) {
//	if (rec.getCollIndice().length()>31) {
//	throw new ValidationException("validaInvDettIndiceEccedente", ValidationException.errore);
//	}
//	}
//	}

//	} catch (Exception e) {
//	throw new ValidationException(e.getMessage(), ValidationException.errore);
//	}
//	//
//	}
	public void validaParamRicercaDaSezioni(EsameCollocRicercaVO rec)
	throws ValidationException {
		try{

			if (rec.getCodPolo() == null || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
				throw new ValidationException("validaParamRicercaDaSezCodPoloObbligatorio", ValidationException.errore);
			}
			if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
				if (rec.getCodPolo().length()>3)	{
					throw new ValidationException("validaParamRicercaDaSezCodPoloEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBib() == null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
				throw new ValidationException("validaParamRicercaDaSezCodBibObbligatorio", ValidationException.errore);
			}
			if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
				if (rec.getCodBib().length()>3)	{
					throw new ValidationException("validaParamRicercaDaSezCodBibEccedente", ValidationException.errore);
				}
			}
			//			if (rec.getCodSerie()!=null && rec.getCodSerie().length()==0){
			//			throw new ValidationException("validaParamRicercaDaSezCodSerieObbligatorio", ValidationException.errore);
			//			}
			if (rec.getTipoOperazione() != null && rec.getTipoOperazione().equals("Inventari")){
				if (rec.getCodSerie()!=null && rec.getCodSerie().length()!=0) {
					if (rec.getCodSerie().length()>3) {
						throw new ValidationException("validaParamRicercaDaSezCodSerieEccedente", ValidationException.errore);
					}
				}
				if (!this.strIsNull(String.valueOf(rec.getCodInvent()))) {
					if (String.valueOf(rec.getCodInvent()).length()>9) {
						throw new ValidationException("validaParamRicercaDaSezCodInventEccedente", ValidationException.errore);
					}
				}
			}
			if (rec.getTipoOperazione() != null && rec.getTipoOperazione().equals("Collocazioni")){
				if (rec.getCodPoloSez() == null || (rec.getCodPoloSez()!=null && rec.getCodPoloSez().trim().length()==0)){
					throw new ValidationException("validaParamRicercaDaSezCodPoloObbligatorio", ValidationException.errore);
				}
				if (rec.getCodPoloSez() !=null &&  rec.getCodPoloSez().length()!=0)	{
					if (rec.getCodPoloSez().length()>3)	{
						throw new ValidationException("validaParamRicercaDaSezCodPoloSezEccedente", ValidationException.errore);
					}
				}
				if (rec.getCodBibSez() == null || (rec.getCodBibSez()!=null && rec.getCodBibSez().trim().length()==0)){
					throw new ValidationException("validaParamRicercaDaSezCodBibObbligatorio", ValidationException.errore);
				}
				if (rec.getCodBibSez() !=null &&  rec.getCodBibSez().length()!=0)	{
					if (rec.getCodBibSez().length()>3)	{
						throw new ValidationException("validaParamRicercaDaSezCodBibSezEccedente", ValidationException.errore);
					}
				}
				if (rec.getCodSez()!=null && rec.getCodSez().length()!=0) {
					if (rec.getCodSez().length()>10) {
						throw new ValidationException("validaParamRicercaDaSezCodSezEccedente", ValidationException.errore);
					}
				}
				if (rec.getCodLoc()!=null && rec.getCodLoc().length()!=0) {
					if (rec.getCodLoc().length()>24) {
						throw new ValidationException("validaParamRicercaDaSezCodLocEccedente", ValidationException.errore);
					}
				}
				if (rec.getCodSpec()!=null && rec.getCodSpec().length()!=0) {
					if (rec.getCodSpec().length()>12) {
						throw new ValidationException("validaParamRicercaDaSezSpecLocEccedente", ValidationException.errore);
					}
				}
			}
			if (rec.getCodBib()!=null && rec.getCodBib().length()==3){
				if (rec.getCodBibSez() != null && rec.getCodBibSez().trim().length() > 0){
					if (rec.getTipoRicerca().equals("6") || rec.getTipoRicerca().equals("7") || rec.getTipoRicerca().equals("8")
							|| rec.getTipoRicerca().equals("9") || rec.getTipoRicerca().equals("10")  || rec.getTipoRicerca().equals("11")
							|| rec.getTipoRicerca().equals("12")){
					}else{


						/*
						 *				|	Sezione		|	Collocazione	|	Spec	|	esatto	 |	tipoRichiesta	|	tipoRichiesta/segueLista  |
						 * 	------------|---------------|-------------------|-----------|------------|------------------|-----------------------------|
						 * 				|		x		|	"" or not = "" 	|			|			 |		0			|			1				  |
						 * 	------------|---------------|-------------------|-----------|------------|------------------|-----------------------------|
						 * 				|		x		|		x			|			|		x			2			|			3			      |
						 * 	------------|---------------|-------------------|-----------|------------|------------------|-----------------------------|
						 * 				|		x		|		x			|	x		|		x	 |		4			|			5				  |
						 * 	------------|---------------|-------------------|-----------|------------|------------------|-----------------------------|
						 * 	lenteColl	|				|					|			|			 |		6			|			7                 |
						 * 	------------|---------------|-------------------|-----------|------------|------------------|-----------------------------|
						 * 	lenteSpec	|				|					|			|			 |		8			|			9				  |
						 * 	------------|---------------|-------------------|-----------|------------|------------------|-----------------------------|
						 * 	lenteUltCoSp|				|					|			|			 |		10			|                             |
						 * 	------------|---------------|-------------------|-----------|------------|------------------|-----------------------------|
						 */

						if((rec.getCodLoc().equals("") || !rec.getCodLoc().equals("")) && rec.getCodSpec().equals("") && !rec.isEsattoColl() && rec.getUltLoc().equals("") && rec.getUltKeyLoc() == 0){
							rec.setTipoRicerca("0");
						}else if((rec.getCodLoc().equals("") || !rec.getCodLoc().equals("")) && rec.getCodSpec().equals("") && !rec.isEsattoColl() && rec.getUltKeyLoc() != 0 && !rec.getUltLoc().equals("")){
							rec.setTipoRicerca("1");
						}else if(!rec.getCodLoc().equals("") && rec.getCodSpec().equals("") && rec.isEsattoColl()&& rec.getUltLoc().equals("")  && rec.getUltKeyLoc() == 0){
							rec.setTipoRicerca("2");
						}else if(!rec.getCodLoc().equals("") && rec.getCodSpec().equals("") && rec.isEsattoColl()&& rec.getUltLoc().equals("")  && !rec.getUltLoc().equals("")){
							rec.setTipoRicerca("3");
						}else if(!rec.getCodLoc().equals("") && !rec.getCodSpec().equals("") && rec.isEsattoColl() && rec.isEsattoSpec() && rec.getUltLoc().equals("") && rec.getUltKeyLoc() == 0){
							rec.setTipoRicerca("4");
						}else if(!rec.getCodLoc().equals("") && !rec.getCodSpec().equals("") && rec.isEsattoColl() && rec.isEsattoSpec() && rec.getUltLoc().equals("") && !rec.getUltLoc().equals("")){
							rec.setTipoRicerca("5");
						}
					}
				}
			}else{
				throw new ValidationException("errCodBib");
			}
		}catch (ValidationException e) {
			throw e;
		}catch (Exception e) {
			throw new ValidationException(e);
		}
	}

	public void validaParamRicercaPerEsameInventario(EsameCollocRicercaVO rec)
	throws ValidationException {

		if (rec.getCodBib()!=null && rec.getCodBib().length()==3){
			if (rec.getCodSerie()!=null && rec.getCodSerie().trim().length() <= 3){
				if (rec.getCodInvent() == 0 ){
					throw new ValidationException("errCodInv");
				}
			}else{
				throw new ValidationException("errCodSerie");
			}
		}else{
			throw new ValidationException("errCodBib");
		}
	}

	public void validaParamRicercaPerInvNonColl(EsameCollocRicercaVO rec)
	throws ValidationException {

		if (rec.getCodPolo() == null || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
			throw new ValidationException("validaRicercaEsameCodPoloObbligatorio", ValidationException.errore);
		}
		if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
			if (rec.getCodPolo().length()>3)	{
				throw new ValidationException("validaRicercaEsameCodPoloEccedente", ValidationException.errore);
			}
		}
		if (rec.getCodBib() == null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
			throw new ValidationException("validaRicercaEsameCodBibObbligatorio", ValidationException.errore);
		}
		if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
			if (rec.getCodBib().length()>3)	{
				throw new ValidationException("validaRicercaEsameCodBibEccedente", ValidationException.errore);
			}
		}
		if (rec.getCodSerie() == null){
			throw new ValidationException("validaRicercaEsameCodSerieObbligatorio", ValidationException.errore);
		}
		if (rec.getCodSerie() !=null &&  rec.getCodSerie().length()!=0)	{
			if (rec.getCodBib().length()>3)	{
				throw new ValidationException("validaRicercaEsameCodSerieEccedente", ValidationException.errore);
			}
		}
//		else{
//		if (rec.getCodInvent() != 0){
//		throw new ValidationException("selAltern");
//		}
//		}
//		if (String.valueOf(rec.getCodInvent()) == null) {
//		throw new ValidationException("validaRicercaEsamecodInvObbligatorio", ValidationException.errore);
//		}
//		if (!this.strIsNull(String.valueOf(rec.getCodInvent()))) {
//		if (String.valueOf(rec.getCodInvent()).length()>9) {
//		throw new ValidationException("validaRicercaEsamecodInvEccedente", ValidationException.errore);
//		}
//		}
//		if (rec.getCodBib()!=null && rec.getCodBib().length()==3){
//		if (rec.getCodSerie() != null && rec.getCodSerie().length() <= 3){
//		if (rec.getCodInvent() != 0){
//		throw new ValidationException("selAltern");
//		}else{
//		if (rec.getCodBibSez() != null && rec.getCodBibSez().trim().length() > 0){
//		throw new ValidationException("selAltern");
//		}
//		}
//		}else{
//		throw new ValidationException("errCodSerie");
//		}
//		}else{
//		if (rec.getCodInvent() != 0){
//		throw new ValidationException("selAltern");
//		}else{
//		if (rec.getCodBibSez() != null){
//		throw new ValidationException("selAltern");
//		}
//		}
//		}
//		}
	}

	public void validaParamRicercaPerInvDiColloc(EsameCollocRicercaVO rec)
	throws ValidationException {

//		if (rec.getCodPolo().equals(null) || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
//		throw new ValidationException("validaListaInvDiCOllocCodPoloObbligatorio", ValidationException.errore);
//		}
//		if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
//		if (rec.getCodPolo().length()>3)	{
//		throw new ValidationException("validaListaInvDiCOllocCodPoloEccedente", ValidationException.errore);
//		}
//		}
//		if (rec.getCodBib().equals(null) || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
//		throw new ValidationException("validaListaInvDiCOllocCodBibObbligatorio", ValidationException.errore);
//		}
//		if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
//		if (rec.getCodBib().length()>3)	{
//		throw new ValidationException("validaListaInvDiCOllocCodBibEccedente", ValidationException.errore);
//		}
//		}
		if (!this.strIsNull(String.valueOf(rec.getKeyLoc()))) {
			if (String.valueOf(rec.getKeyLoc()).length()>9) {
				throw new ValidationException("validaListaInvDiCOllocKeyLocEccedente", ValidationException.errore);
			}
		}else{
			throw new ValidationException("validaListaInvDiCOllocKeyLocNonPresente", ValidationException.errore);
		}
	}

	private  boolean strIsNull(String data) {
		return (data == null);
	}
	private  boolean strIsNumeric(String data) {
		return (Pattern.matches("[0-9&&[^a-z]]+", data));
	}
	public static boolean strIsAlfabetic(String data) {
		return (Pattern.matches("[^0-9]+", data));
	}
	public	void validaListaInventariOrdine (InventarioVO rec)
	throws ValidationException {
		try {
			if (rec.getCodPolo() == null || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
				throw new ValidationException("validaListaInvOrdInvCodPoloObbligatorio", ValidationException.errore);
			}
			if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
				if (rec.getCodPolo().length()>3)	{
					throw new ValidationException("validaListaInvOrdCodPoloEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBib() == null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
				throw new ValidationException("validaListaInvOrdCodBibObbligatorio", ValidationException.errore);
			}
			if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
				if (rec.getCodBib().length()>3)	{
					throw new ValidationException("validaListaInvOrdCodBibEccedente", ValidationException.errore);
				}
			}
//			if (rec.getCodBibO() !=null &&  rec.getCodBibO().length()!=0)	{
//			if (rec.getCodBibO().trim().length()>3)	{
//			throw new ValidationException("validaListaInvOrdCodBibOEccedente", ValidationException.errore);
//			}
//			}
			if (rec.getCodTipoOrd()!=null && rec.getCodTipoOrd().trim().length()==0){
				throw new ValidationException("validaListaInvOrdCodTipoOrdObbligatorio", ValidationException.errore);
			}
			if (rec.getCodTipoOrd() !=null &&  rec.getCodTipoOrd().length()!=0)	{
				if (rec.getCodTipoOrd().trim().length()>1)	{
					throw new ValidationException("validaListaInvOrdCodTipoOrdEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getAnnoOrd()))) {
				if (String.valueOf(rec.getAnnoOrd()).length()>9) {
					throw new ValidationException("validaListaInvOrdAnnoOrdEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getCodOrd()))) {
				if (String.valueOf(rec.getCodOrd()).length()>9) {
					throw new ValidationException("validaListaInvOrdCodOrdEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBibF() !=null &&  rec.getCodBibF().length()!=0)	{
				if (rec.getCodBibF().trim().length()>3)	{
					throw new ValidationException("validaListaInvOrdCodBibFEccedente", ValidationException.errore);
				}
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
		//
	}
	public	void validaListaInventariRigaFattura (InventarioVO rec)
	throws ValidationException {
		try {
			if (rec.getCodPolo() == null || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
				throw new ValidationException("validaListaInvOrdInvCodPoloObbligatorio", ValidationException.errore);
			}
			if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
				if (rec.getCodPolo().length()>3)	{
					throw new ValidationException("validaListaInvOrdCodPoloEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBib() == null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
				throw new ValidationException("validaListaInvOrdCodBibObbligatorio", ValidationException.errore);
			}
			if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
				if (rec.getCodBib().length()>3)	{
					throw new ValidationException("validaListaInvOrdCodBibEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodTipoOrd()!=null && rec.getCodTipoOrd().trim().length()==0){
				throw new ValidationException("validaListaInvOrdCodTipoOrdObbligatorio", ValidationException.errore);
			}
			if (rec.getCodTipoOrd() !=null &&  rec.getCodTipoOrd().length()!=0)	{
				if (rec.getCodTipoOrd().trim().length()>1)	{
					throw new ValidationException("validaListaInvOrdCodTipoOrdEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getAnnoOrd()))) {
				if (String.valueOf(rec.getAnnoOrd()).length()>9) {
					throw new ValidationException("validaListaInvOrdAnnoOrdEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getCodOrd()))) {
				if (String.valueOf(rec.getCodOrd()).length()>9) {
					throw new ValidationException("validaListaInvOrdCodOrdEccedente", ValidationException.errore);
				}
			}
			if (rec.getCodBibF() == null ){
				throw new ValidationException("validaListaInvOrdCodBibOObbligatorio", ValidationException.errore);
			}
			if (rec.getCodBibF() !=null)	{
				if (rec.getCodBibF().length()>3)	{
					throw new ValidationException("validaListaInvOrdCodBibOEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getAnnoFattura()))) {
				if (String.valueOf(rec.getAnnoFattura()).length()>9) {
					throw new ValidationException("validaListaInvOrdAnnoFatturaEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getProgrFattura()))) {
				if (String.valueOf(rec.getProgrFattura()).length()>9) {
					throw new ValidationException("validaListaInvOrdPrgFatturaEccedente", ValidationException.errore);
				}
			}
			if (!this.strIsNull(String.valueOf(rec.getRigaFattura()))) {
				if (String.valueOf(rec.getRigaFattura()).length()>9) {
					throw new ValidationException("validaListaInvOrdRigaFatturaEccedente", ValidationException.errore);
				}
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
		//
	}
	public	void validaPossessori (CodiceVO rec)
	throws ValidationException {
		try {

			if (rec.getCodice() == null || (rec.getCodice()!=null && rec.getCodice().trim().length()==0)){
				throw new ValidationException("validaInvCodPoloObbligatorio", ValidationException.errore);
			}
			if (rec.getCodice() !=null &&  rec.getCodice().length()!=0)	{
				if (rec.getCodice().length()>3)	{
					throw new ValidationException("validaInvCodPoloEccedente", ValidationException.errore);
				}
			}
			if (rec.getDescrizione() == null || (rec.getDescrizione()!=null && rec.getDescrizione().trim().length()==0)){
				throw new ValidationException("validaInvCodBibObbligatorio", ValidationException.errore);
			}
			if (rec.getDescrizione() !=null &&  rec.getDescrizione().length()!=0)	{
				if (rec.getDescrizione().length()>3)	{
					throw new ValidationException("validaInvCodBibEccedente", ValidationException.errore);
				}
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
	}

	public void validaParamRegistroTopografico(StampaRegistroTopograficoVO rec)
	throws ValidationException {

		if (rec.getCodBib()!=null && rec.getCodBib().length()==3){
			throw new ValidationException("validaInvCodBibObbligatorio");
		}
	}
	public	void validaBid (InventarioVO rec)
	throws ValidationException {
		try {

			if (rec.getBid() !=null &&  rec.getBid().length()!=0)	{
				if (rec.getBid().length()>10)	{
					throw new ValidationException("validaInvBidEccedente", ValidationException.errore);
				}
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
	}
	public	void validaModelloDefault(ModelloDefaultVO rec)
	throws ValidationException {

		if (rec.getCodPolo() == null || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
			throw new ValidationException("CodPoloObbligatorio", ValidationException.errore);
		}
		if (rec.getCodBib() == null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
			throw new ValidationException("CodBibObbligatorio", ValidationException.errore);
		}
		if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
			if (rec.getCodPolo().length()>3)	{
				throw new ValidationException("CodPoloEccedente", ValidationException.errore);
			}
		}
		if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
			if (rec.getCodBib().length()>3)	{
				throw new ValidationException("CodBibEccedente", ValidationException.errore);
			}
		}
		if (rec.getCodModello() == null || (rec.getCodModello()!=null && rec.getCodModello().trim().length()==0)){
			throw new ValidationException("nomeModelloObbligatorio", ValidationException.errore);
		}
		if (rec.getCodModello()!=null && rec.getCodModello().trim().length()!=0) {
			if (rec.getCodModello().length()>30) {
				throw new ValidationException("modelloEccedente", ValidationException.errore);
			}
		}
		if (rec.getCodScarico() == null || (rec.getCodScarico()!=null && rec.getCodScarico().trim().length()==0)){
			throw new ValidationException("codScaricoObbligatorio", ValidationException.errore);
		}
		if (rec.getCodScarico()!=null && rec.getCodScarico().trim().length()!=0) {
			if (rec.getCodScarico().length()>30) {
				throw new ValidationException("codScaricoEccedente", ValidationException.errore);
			}
		}
	}
	public	void validaCodPoloCodBibCodModello (String codPolo, String codBib, String codModello)
	throws ValidationException {

		if (codPolo ==(null) || ( codPolo.trim().length()==0)){
			throw new ValidationException("codPoloObbligatorio", ValidationException.errore);
		}
		if (codBib == (null) || (codBib!=null && codBib.trim().length()==0)){
			throw new ValidationException("codBibObbligatorio",ValidationException.errore);
		}
		if (codPolo !=null &&  codPolo.length()!=0)	{
			if (codPolo.length()>3)	{
				throw new ValidationException("codPoloEccedente", ValidationException.errore);
			}
		}
		if (codBib !=null &&  codBib.length()!=0)	{
			if (codBib.length()>3)	{
				throw new ValidationException("codBibEccedente", ValidationException.errore);
			}
		}
		if (codModello == (null) || (codModello!=null && codModello.trim().length()==0)){
			throw new ValidationException("codModelloObbligatorio",ValidationException.errore);
		}
		if (codModello !=null &&  codModello.length()!=0)	{
			if (codModello.length()>10)	{
				throw new ValidationException("codModelloEccedente", ValidationException.errore);
			}
		}
	}
	public	void validaModello(ModelloEtichetteVO rec)
	throws ValidationException {

		if (rec.getCodPolo() == null || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
			throw new ValidationException("CodPoloObbligatorio", ValidationException.errore);
		}
		if (rec.getCodBib() == null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
			throw new ValidationException("CodBibObbligatorio", ValidationException.errore);
		}
		if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
			if (rec.getCodPolo().length()>3)	{
				throw new ValidationException("CodPoloEccedente", ValidationException.errore);
			}
		}
		if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
			if (rec.getCodBib().length()>3)	{
				throw new ValidationException("CodBibEccedente", ValidationException.errore);
			}
		}
		if (rec.getCodModello() == null || (rec.getCodModello()!=null && rec.getCodModello().trim().length()==0)){
			throw new ValidationException("nomeModelloObbligatorio", ValidationException.errore);
		}
		if (rec.getCodModello()!=null && rec.getCodModello().trim().length()!=0) {
			if (rec.getCodModello().length()>30) {
				throw new ValidationException("modelloEccedente", ValidationException.errore);
			}
		}
		if (rec.getDescrModello() == null || (rec.getDescrModello()!=null && rec.getDescrModello().trim().length()==0)){
			throw new ValidationException("descrModelloObbligatorio", ValidationException.errore);
		}
		if (rec.getCodModello()!=null && rec.getCodModello().trim().length()!=0) {
			if (rec.getCodModello().length()>50) {
				throw new ValidationException("descrEccedente", ValidationException.errore);
			}
		}
		if (rec.getDatiForm() == null || (rec.getDatiForm()!=null && rec.getDatiForm().trim().length()==0)){
			throw new ValidationException("datiModelloObbligatorio", ValidationException.errore);
		}
		if (rec.getTipoModello() == null || (rec.getTipoModello()!=null && rec.getTipoModello().trim().length()==0)){
			throw new ValidationException("tipoModelloObbligatorio", ValidationException.errore);
		}
		if (rec.getTipoModello()!=null && rec.getTipoModello().trim().length()!=0) {
			if (rec.getTipoModello().length()>1) {
				throw new ValidationException("tipoModelloEccedente", ValidationException.errore);
			}
		}
		if (rec.getUteAgg() == null || (rec.getUteAgg()!=null && rec.getUteAgg().trim().length()==0)){
			throw new ValidationException("uteAggObbligatorio", ValidationException.errore);
		}
		if (rec.getUteAgg()!=null && rec.getUteAgg().trim().length()!=0) {
			if (rec.getUteAgg().length()>12) {
				throw new ValidationException("uteAggEccedente", ValidationException.errore);
			}
		}
	}
	public	void validaModelloKey(ModelloEtichetteVO rec)
	throws ValidationException {

		if (rec.getCodPolo() == null || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
			throw new ValidationException("CodPoloObbligatorio", ValidationException.errore);
		}
		if (rec.getCodBib() == null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
			throw new ValidationException("CodBibObbligatorio", ValidationException.errore);
		}
		if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
			if (rec.getCodPolo().length()>3)	{
				throw new ValidationException("CodPoloEccedente", ValidationException.errore);
			}
		}
		if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
			if (rec.getCodBib().length()>3)	{
				throw new ValidationException("CodBibEccedente", ValidationException.errore);
			}
		}
		if (rec.getCodModello() == null || (rec.getCodModello()!=null && rec.getCodModello().trim().length()==0)){
			throw new ValidationException("nomeModelloObbligatorio", ValidationException.errore);
		}
		if (rec.getCodModello()!=null && rec.getCodModello().trim().length()!=0) {
			if (rec.getCodModello().length()>30) {
				throw new ValidationException("modelloEccedente", ValidationException.errore);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public	AggDispVO validaInputAggiornamentoDisponibilitaCMT(AggDispVO rec)
	throws ValidationException {
		AggDispVO output = new AggDispVO(rec);

		try{
			List<String> errori = output.getErrori();
			if (rec.getCodPolo() == null || (rec.getCodPolo()!=null && rec.getCodPolo().trim().length()==0)){
				errori.add("ERRORE: CodPoloObbligatorio");
			}
			if (rec.getCodBib() == null || (rec.getCodBib()!=null && rec.getCodBib().trim().length()==0)){
				errori.add("ERRORE: CodBibObbligatorio");
			}
			if (rec.getCodPolo() !=null &&  rec.getCodPolo().length()!=0)	{
				if (rec.getCodPolo().length()>3)	{
					errori.add("ERRORE: CodPoloEccedente");
				}
			}
			if (rec.getCodBib() !=null &&  rec.getCodBib().length()!=0)	{
				if (rec.getCodBib().length()>3)	{
					errori.add("ERRORE: CodBibEccedente");
				}
			}

			if (rec.getListaInventari() == null || rec.getListaInventari().size() < 1){
				errori.add("ERRORE: listaInventariVuota");
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
		return output;
	}
	public void validaCodPoloCodBibSerieInvDaInvA(String codPolo, String codBib, String serie, String invDa, String invA)
	throws ValidationException {
		try{
			this.validaCodPoloCodBibSerie(codPolo, codBib, serie);

			if (!this.strIsNull(invDa)) {
				if (this.strIsNumeric(invDa)){
					if ((invDa).length()>9) {
						throw new ValidationException("codInventEccedente", ValidationException.errore);
					}
				}else{
					throw new ValidationException("codInventDeveEssereNumerico", ValidationException.errore);
				}
			}

			if (!this.strIsNull(invA)) {
				if (this.strIsNumeric(invA)){
					if ((invA).length()>9) {
						throw new ValidationException("codInventEccedente", ValidationException.errore);
					}
				}else{
					throw new ValidationException("codInventDeveEssereNumerico", ValidationException.errore);
				}
			}

			if (Integer.valueOf(invA) < Integer.valueOf(invDa) ) {
				throw new ValidationException("codInventDaDeveEssereMinoreDiCodInventA", ValidationException.errore);
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
	}

	public	void validaParamRicercaPerInvCollDa (String codPolo, String codBib, String codUtente, String dataDa, String dataA)
	throws ValidationException {
		try {

			if (codPolo == null || (codPolo!=null && codPolo.trim().length()==0)){
				throw new ValidationException("codBibObbligatorio", ValidationException.errore);
			}
			if (codPolo !=null &&  codPolo.length()!=0)	{
				if (codPolo.length()>3)	{
					throw new ValidationException("codPoloEccedente", ValidationException.errore);
				}
			}
			if (codBib == null || (codBib!=null && codBib.trim().length()==0)){
				throw new ValidationException("codBibObbligatorio", ValidationException.errore);
			}
			if (codBib !=null &&  codBib.length()!=0)	{
				if (codBib.length()>3)	{
					throw new ValidationException("codBibEccedente", ValidationException.errore);
				}
			}
			if (ValidazioneDati.strIsNull(codUtente)){
				throw new ValidationException("codiceBibliotecarioObbligatorio");
			}
			if (ValidazioneDati.strIsNull(dataDa)){
				throw new ValidationException("dataInizioObbligatoria");
			}
			if (ValidazioneDati.strIsNull(dataA)){
				throw new ValidationException("dataFineObbligatoria");
			}
			if (ValidazioneDati.isFilled(dataDa.trim()) && ValidazioneDati.validaData(dataDa.trim()) != ValidazioneDati.DATA_OK){
				throw new ValidationException("dataInizioErrata");
			}
			if (ValidazioneDati.isFilled(dataA.trim()) && ValidazioneDati.validaData(dataA.trim()) != ValidazioneDati.DATA_OK){
				throw new ValidationException("datafineErrata");
			}

//			if (dataDa!=null && dataDa.length()!=0) {
//				int codRitorno = ValidazioneDati.validaData(dataDa);
//				if (codRitorno != ValidazioneDati.DATA_OK)
//					throw new ValidationException("dataDaErrataDa", ValidationException.errore);
//			}
//			if (dataA !=null && dataA.length()!=0) {
//				int codRitorno = ValidazioneDati.validaData(dataA);
//				if (codRitorno != ValidazioneDati.DATA_OK)
//					throw new ValidationException("dataAErrataA", ValidationException.errore);
//			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage(), ValidationException.errore);
		}
		//
	}
	public	void validaContatore (String codPolo, String codBib, String codCont)
	throws ValidationException {

		if (codPolo == null || (codPolo!=null && codPolo.trim().length()==0)){
			throw new ValidationException("validaProvenienzaInventarioCodPoloObbligatorio", ValidationException.errore);
		}
		if (codBib == null || (codBib!=null && codBib.trim().length()==0)){
			throw new ValidationException("validaProvenienzaInventarioCodBibObbligatorio", ValidationException.errore);
		}
		if (codPolo !=null &&  codPolo.length()!=0)	{
			if (codPolo.length()>3)	{
				throw new ValidationException("validaProvenienzaInventarioCodPoloEccedente", ValidationException.errore);
			}
		}
		if (codBib !=null &&  codBib.length()!=0)	{
			if (codBib.length()>3)	{
				throw new ValidationException("validaProvenienzaInventarioCodBibEccedente", ValidationException.errore);
			}
		}
		if (codCont == null || (codCont!=null && codCont.trim().length()==0)){
			throw new ValidationException("validaCodContObbligatorio", ValidationException.errore);
		}
		if (codCont !=null &&  codCont.length()!=0)	{
			if (codCont.length()>3)	{
				throw new ValidationException("validaCodContEccedente", ValidationException.errore);
			}
		}
	}
}
