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
package it.iccu.sbn.vo.custom.periodici;

import gnu.trove.THashMap;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.common.SerializableComparator;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.FascicoloVO;
import it.iccu.sbn.ejb.vo.periodici.fascicolo.StatoFascicolo;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.util.periodici.PeriodiciUtil;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FascicoloDecorator extends FascicoloVO {

	public static final SerializableComparator<FascicoloVO> ORDINAMENTO_FASCICOLO = new SerializableComparator<FascicoloVO>() {

		private static final long serialVersionUID = -228832091510641389L;

		private int ordinaSuRicezione(FascicoloDecorator fd1, FascicoloDecorator fd2) {
			if (!fd1.isRicevuto() && !fd2.isRicevuto()) return 0;
			if (!fd1.isRicevuto() && fd2.isRicevuto()) return -1;
			if (fd1.isRicevuto() && !fd2.isRicevuto()) return 1;

			return ValidazioneDati.compare(fd1.esemplare.getData_arrivo(), fd2.esemplare.getData_arrivo());
		}

		public int compare(FascicoloVO f1, FascicoloVO f2) {
			int cmp = ValidazioneDati.compare(f1.getData_conv_pub(), f2.getData_conv_pub());
			cmp = (cmp != 0) ? cmp : ValidazioneDati.compare(f1.getAnnata(), f2.getAnnata());
			cmp = (cmp != 0) ? cmp : ValidazioneDati.compare(f1.getNum_volume(), f2.getNum_volume());
			if (cmp == 0)
				if (f1 instanceof FascicoloDecorator && f2 instanceof FascicoloDecorator) {
					FascicoloDecorator fd1 = (FascicoloDecorator) f1;
					FascicoloDecorator fd2 = (FascicoloDecorator) f2;
					cmp = (cmp != 0) ? cmp : ValidazioneDati.compare(fd1.getNumFascicolo(), fd2.getNumFascicolo());
					cmp = (cmp != 0) ? cmp : ordinaSuRicezione(fd1, fd2);
				} else
					cmp = (cmp != 0) ? cmp : ValidazioneDati.compare(f1.getNum_in_fasc(), f2.getNum_in_fasc());

			cmp = (cmp != 0) ? cmp : f1.compareTo(f2);
			return cmp;
		}
	};


	public static final SerializableComparator<FascicoloDecorator> ORDINAMENTO_ESEMPLARE = new SerializableComparator<FascicoloDecorator>() {

		private static final long serialVersionUID = 2721948684214641928L;

		public int compare(FascicoloDecorator f1, FascicoloDecorator f2) {
			int cmp = ValidazioneDati.compare(f1.getCodBib(), f2.getCodBib());
			cmp = (cmp != 0) ? cmp : ValidazioneDati.compare(f1.esemplare.getData_arrivo(), f2.esemplare.getData_arrivo());
			cmp = (cmp != 0) ? cmp : ValidazioneDati.compare(f1.getData_conv_pub(), f2.getData_conv_pub());
			return cmp;
		}
	};

	private static final long serialVersionUID = 7979764770806864007L;
	private String periodicita;
	private String tipoFascicolo;

	private boolean smarrito;
	private boolean rilegatura;

	private List<String> listaBibRicezione;

	private int numEsemplariBib;
	private int numEsemplariPolo;

	//almaviva5_20111102 #4708
	private Map<String, ValidationException> validator_exceptions;

	private String numVolume;
	private String iniFascicolo;
	private String fineFascicolo;

	private String isbd;

	private void addException(String field, ValidationException v) {
		if (validator_exceptions == null)
			validator_exceptions = new THashMap<String, ValidationException>();

		validator_exceptions.put(field, v);
	}

	private void removeException(String field) {
		if (validator_exceptions != null)
			validator_exceptions.remove(field);
	}

	public FascicoloDecorator(FascicoloVO f) {
		super(f);

		this.smarrito = (esemplare != null && ValidazioneDati.equals(esemplare.getCd_no_disp(), 'S'));
		this.rilegatura = (esemplare != null && ValidazioneDati.equals(esemplare.getCd_no_disp(), 'R'));

		try {
			this.periodicita = CodiciProvider.cercaDescrizioneCodice(cd_per, CodiciType.CODICE_PERIODICITA, CodiciRicercaType.RICERCA_CODICE_SBN);
			this.tipoFascicolo = CodiciProvider.cercaDescrizioneCodice(cd_tipo_fasc, CodiciType.CODICE_TIPO_FASCICOLI, CodiciRicercaType.RICERCA_CODICE_SBN);

			this.numVolume = num_volume != null ? num_volume.toString() : null;
			this.iniFascicolo = num_in_fasc != null ? num_in_fasc.toString() : null;
			this.fineFascicolo = num_fi_fasc != null ? num_fi_fasc.toString() : null;
		} catch (Exception e) {
			return;
		}
	}


	public FascicoloDecorator(FascicoloDecorator fd) {
		super(fd);
	    this.periodicita = fd.periodicita;
	    this.tipoFascicolo = fd.tipoFascicolo;
	    this.smarrito = fd.smarrito;
	    this.rilegatura = fd.rilegatura;
	    this.numEsemplariBib = fd.numEsemplariBib;
	    this.numEsemplariPolo = fd.numEsemplariPolo;

	    this.numVolume = fd.numVolume;
	    this.iniFascicolo = fd.iniFascicolo;
	    this.fineFascicolo = fd.fineFascicolo;

	    this.isbd = fd.isbd;

	    try {
			this.listaBibRicezione = fd.listaBibRicezione == null ? null : ClonePool.deepCopy(fd.listaBibRicezione);
			this.validator_exceptions = fd.validator_exceptions == null ? null : ClonePool.deepCopy(fd.validator_exceptions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isSalvato() {
		return (fid != 0);
	}

	public String getPeriodicita() {
		return periodicita;
	}

	public String getTipoFascicolo() {
		return tipoFascicolo;
	}

	public String getDettaglioRicezione() {
		if (!StatoFascicolo.isPosseduto(stato) || esemplare == null)
			return "";

		StringBuilder buf = new StringBuilder(512);
		String ord = getChiaveOrdine();
		String inv = getChiaveInventario();
		Integer grp = esemplare.getGrp_fasc();

		if (isFilled(ord)) {
			buf.append(ord);
			if (esemplare.getCd_inven() > 0)
				buf.append(" - ");
		}

		if (esemplare.getCd_inven() > 0) {
			buf.append(inv);
			if (isFilled(grp))
				buf.append(" / ").append(grp);
		}

		return buf.toString();
	}

	public String getDettaglioRicezioneSenzaOrdine() {
		//almaviva5_20111121 #4709
		if (!StatoFascicolo.isPosseduto(stato) || esemplare == null)
			return "";

		if (esemplare.getCd_inven() <= 0)
			return "";

		StringBuilder buf = new StringBuilder(512);
		Integer grp = esemplare.getGrp_fasc();
		buf.append(getChiaveInventario());
		if (isFilled(grp))
			buf.append(" / ").append(grp);

		return buf.toString();
	}

	public String getChiaveInventario() {
		if (!StatoFascicolo.isPosseduto(stato) || esemplare == null)
			return "";

		if (esemplare.getCd_inven() <= 0)
			return "";

		StringBuilder buf = new StringBuilder(512);
		buf.append(trimOrEmpty(esemplare.getCodBib()));
		buf.append(" ");
		buf.append(esemplare.getCd_serie());
		buf.append(" ");
		buf.append(esemplare.getCd_inven());

		return buf.toString();
	}

	public String getChiaveOrdine() {
		if (!StatoFascicolo.isPosseduto(stato) || esemplare == null)
			return "";

		if (esemplare.getCod_ord() <= 0)
			return "";

		StringBuilder buf = new StringBuilder(512);
		buf.append(esemplare.getAnno_ord());
		buf.append(" ");
		buf.append(esemplare.getCod_tip_ord());
		buf.append(" ");
		buf.append(esemplare.getCod_ord());

		return buf.toString();
	}

	public String getChiaveCollocazione() {
		if (!StatoFascicolo.isPosseduto(stato) || esemplare == null)
			return "";

		if (esemplare.getKey_loc() <= 0)
			return "";

		StringBuilder buf = new StringBuilder(512);
		buf.append(esemplare.getCodSez());
		buf.append(" ");
		buf.append(esemplare.getCd_loc());
		buf.append(" ");
		buf.append(esemplare.getSpec_loc());

		return buf.toString();
	}

	public String getNumFascicolo() {
		return PeriodiciUtil.formattaNumeroFascicolo(this);
	}

	public String getDataInizioPub() {
		return (data_in_pubbl == null ? null : DateUtil.formattaData(data_in_pubbl));
	}

	public void setDataInizioPub(String dataInizio) {
		this.data_in_pubbl = DateUtil.toDate(dataInizio);
		this.setData_conv_pub(data_in_pubbl);
	}

	public String getDataFinePub() {
		return (data_fi_pubbl == null ? null : DateUtil.formattaData(data_fi_pubbl));
	}

	public void setDataFinePub(String dataFine) {
		this.data_fi_pubbl = DateUtil.toDate(dataFine);
	}

	public String getDataRicezione() {
		return (esemplare == null || esemplare.getData_arrivo() == null) ? null : DateUtil.formattaData(esemplare.getData_arrivo());
	}

	public void setDataRicezione(String dataRicezione) {
		if (esemplare == null)
			return;

		esemplare.setData_arrivo(DateUtil.toDate(dataRicezione));
	}

	public String getNumVolume() {
		return numVolume;
	}

	public void setNumVolume(String numVolume) {
		this.numVolume = numVolume;
		if (isFilled(numVolume)) {
			if (!isNumeric(numVolume)) {
				this.num_volume = null;
				addException("numVolume", new ValidationException(SbnErrorTypes.ERROR_GENERIC_NUMERIC_NOT_MANDATORY_FIELD, "periodici.kardex.volume"));
			} else {
				this.num_volume = Short.valueOf(numVolume);
				removeException("numVolume");
			}
		} else {
			this.num_volume = null;
			removeException("numVolume");
		}
	}

	public String getIniFascicolo() {
		return iniFascicolo;
	}

	public void setIniFascicolo(String iniFascicolo) {
		this.iniFascicolo = iniFascicolo;
		if (isFilled(iniFascicolo)) {
			if (!isNumeric(iniFascicolo)) {
				this.num_in_fasc = null;
				addException("iniFascicolo", new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "periodici.fascicolo.numerazione"));
			} else {
				this.num_in_fasc = Integer.valueOf(iniFascicolo);
				removeException("iniFascicolo");
			}

		} else {
			this.num_in_fasc = null;
			removeException("iniFascicolo");
		}
	}

	public String getFineFascicolo() {
		return fineFascicolo;
	}

	public void setFineFascicolo(String fineFascicolo) {
		this.fineFascicolo = fineFascicolo;
		if (isFilled(fineFascicolo)) {
			if (!isNumeric(fineFascicolo)) {
				this.num_fi_fasc = null;
				addException("fineFascicolo", new ValidationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "periodici.fascicolo.numerazione"));
			} else {
				this.num_fi_fasc = Integer.valueOf(fineFascicolo);
				removeException("fineFascicolo");
			}
		} else {
			this.num_fi_fasc = null;
			removeException("fineFascicolo");
		}

	}

	public void setGruppoFascicolo(String gruppoFascicolo) {
		if (esemplare != null)
			esemplare.setGrp_fasc(isNumeric(gruppoFascicolo) && isFilled(Integer.valueOf(gruppoFascicolo)) ? Integer.valueOf(gruppoFascicolo) : null);
	}

	public String getGruppoFascicolo() {
		return (esemplare != null && isFilled(esemplare.getGrp_fasc()) ) ? esemplare.getGrp_fasc().toString() : "";
	}

	@Override
	public int getRepeatableId() {
		return PeriodiciUtil.getRepeatableId(this);
	}

	public boolean isSmarrito() {
		return smarrito;
	}

	public void setSmarrito(boolean smarrito) {
		this.smarrito = smarrito;
	}

	public boolean isRilegatura() {
		return rilegatura;
	}

	public void setRilegatura(boolean rilegatura) {
		this.rilegatura = rilegatura;
	}

	public boolean isPosseduto() {
		return StatoFascicolo.isPosseduto(stato);
	}

	public String getDescrizioneStato() {
		String cd_stato = String.valueOf(stato.getStato());
		try {
			return trimOrEmpty(CodiciProvider.cercaDescrizioneCodice(cd_stato,
					CodiciType.CODICE_STATO_FASCICOLI,
					CodiciRicercaType.RICERCA_CODICE_SBN));
		} catch (Exception e) {
			return cd_stato;
		}
	}

	public String getDescrizioneFascicolo() {
		StringBuilder buf = new StringBuilder(512);
		buf.append("Fasc. ").append(getNumFascicolo());
		buf.append(" - ").append(getDataInizioPub());
		/*
		String dataFinePub = getDataFinePub();
		if (isFilled(dataFinePub))
			buf.append(" - ").append(dataFinePub);
		*/
		if (isFilled(annata))
			buf.append(" - Anno ").append(annata);
		if (isFilled(num_volume))
			buf.append(" - Vol. ").append(num_volume);

		return buf.toString();
	}

	public boolean isRicevuto() {
		return (esemplare != null && !esemplare.isCancellato());
	}

	public List<String> getListaBibRicezione() {
		return listaBibRicezione;
	}

	public void setListaBibRicezione(List<String> listaBibRicezione) {
		this.listaBibRicezione = listaBibRicezione;
	}

	public int getNumEsemplariBib() {
		return numEsemplariBib;
	}

	public void setNumEsemplariBib(int numEsemplariBib) {
		this.numEsemplariBib = numEsemplariBib;
	}

	public int getNumEsemplariPolo() {
		return numEsemplariPolo;
	}

	public void setNumEsemplariPolo(int numEsemplariPolo) {
		this.numEsemplariPolo = numEsemplariPolo;
	}

	@Override
	public FascicoloVO copyThis() {
		return new FascicoloDecorator(this);
	}

	@Override
	public ValidationException[] getValidationExceptions() {
		if (!isFilled(validator_exceptions) )
			return null;

		int size = validator_exceptions.size();
		ValidationException[] output = new ValidationException[size];
		int idx = 0;
		for (Entry<String, ValidationException> entry : validator_exceptions.entrySet())
			output[idx++] = entry.getValue();

		return output;
	}

	public String getIsbd() {
		return isbd;
	}

	public void setIsbd(String isbd) {
		this.isbd = trimAndSet(isbd);
	}

}
