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
package it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico;

import it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.impl.IdDocumentoAUCDExecutor;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.impl.IdDocumentoUriAUCDExecutor;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.impl.InventarioAUCDExecutor;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.impl.InventarioUriAUCDExecutor;
import it.iccu.sbn.ejb.domain.elaborazioniDifferite.documentoFisico.impl.ShippingManifestAUCDExecutor;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.utils.unicode.OrdinamentoUnicode;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.AcquisizioneUriCopiaDigitaleVO;
import it.iccu.sbn.ejb.vo.elaborazioniDifferite.ElaborazioniDifferiteOutputVo;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.servizi.batch.BatchLogWriter;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.util.file.CsvWriter2;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.sbnmarc.SBNMarcUtil;
import it.iccu.sbn.vo.validators.documentofisico.AcquisizioneUriCopiaDigitaleValidator;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;
import org.jumpmind.symmetric.csv.CsvWriter;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.fillLeft;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.fillRight;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.trimOrEmpty;

public class AcquisizioneUriCopiaDigitale {

	public static final String REGEX_URI_LOC_SEPARATORE = "\\s\\|\\s";
	public static final String URI_LOC_SEPARATORE = " | ";

	public static final String REGEX_AUCD_FILE_SEPARATOR = "\\t";
	public static final char AUCD_FILE_SEPARATOR = '\t';

	private static final char BLANK = ' ';

	public interface AUCDExecutor {
		public void execute(AcquisizioneUriCopiaDigitaleVO richiesta,
				UserTransaction tx, BufferedReader r, BufferedWriter w,
				CsvWriter csv, Logger log) throws ApplicationException,
				IOException;
	}

	public enum TipoFileInput {
		Inventario						(true, true, InventarioAUCDExecutor.class),
		BID								(true, false, IdDocumentoAUCDExecutor.class),
		Inventario_URI					(false, true, InventarioUriAUCDExecutor.class),
		BID_URI							(false, false, IdDocumentoUriAUCDExecutor.class),
		CodBib_BID_Inventario_URI		(false, true, null),
		ShippingManifest				(true, true, ShippingManifestAUCDExecutor.class);

		private final boolean wantsModel;
		private final boolean hasInv;
		private final Class<? extends AUCDExecutor> executorClass;

		private TipoFileInput(boolean wantsModel, boolean hasInv, Class<? extends AUCDExecutor> executorClass) {
			this.wantsModel = wantsModel;
			this.hasInv = hasInv;
			this.executorClass = executorClass;
		}

		public boolean wantsModel() {
			return wantsModel;
		}

		public Class<? extends AUCDExecutor> getExecutorClass() {
			return executorClass;
		}

		public boolean hasInv() {
			return hasInv;
		}
	}

	private static final Pattern URI_FIELD_REGEX = Pattern.compile("(\\[.+?\\])", Pattern.CASE_INSENSITIVE);

	private final AcquisizioneUriCopiaDigitaleVO richiesta;
	private final Logger log;


	public AcquisizioneUriCopiaDigitale(
			AcquisizioneUriCopiaDigitaleVO richiesta, BatchLogWriter blog) {
		this.richiesta = richiesta;
		this.log = blog.getLogger();
	}

	public ElaborazioniDifferiteOutputVo execute(UserTransaction tx) throws ApplicationException, ValidationException {

		richiesta.validate(new AcquisizioneUriCopiaDigitaleValidator() );
		ElaborazioniDifferiteOutputVo output = new ElaborazioniDifferiteOutputVo(richiesta);
		BufferedReader r = null;
		BufferedWriter w = null;
		CsvWriter2 csv = null;

		try {
			try {
				output.setStato(ConstantsJMS.STATO_ERROR);
				String model = richiesta.getModel();
				if (isFilled(model))
					log.debug("modello per costruzione URI: " + model);


				String inputFile = richiesta.getInputFile();
				if (!FileUtil.exists(inputFile))
					throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_NOT_FOUND);

				File f = new File(inputFile);
				try {
					BatchManager.getBatchManagerInstance().markForDeletion(f);
				} catch (Exception e) {
					throw new ApplicationException(SbnErrorTypes.ERROR_IO_READ_FILE);
				}

				r = new BufferedReader(new FileReader(f));

				//setup report file
				String fileName = richiesta.getFirmaBatch() + ".htm";
				FileOutputStream out = new FileOutputStream(StampeUtil.getBatchFilesPath() + File.separator + fileName);
				w = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
				output.addDownload(fileName, fileName);

				try {
					//setup file csv indice
					if (richiesta.isPreparaFileIndice()) {
						log.debug("preparazione file per importazione URI in Indice");
						String csvFileName = richiesta.getFirmaBatch() + ".csv";
						Writer wr = new BufferedWriter(new FileWriter(StampeUtil.getBatchFilesPath() + File.separator + csvFileName));
						csv = new CsvWriter2(wr, AUCD_FILE_SEPARATOR);
						csv.setUseTextQualifier(false);

						output.addDownload(csvFileName, csvFileName);
					}
				} catch (Exception e) {
					throw new ApplicationException(SbnErrorTypes.ERROR_IO_READ_FILE);
				}

				TipoFileInput type = TipoFileInput.values()[Integer.valueOf(richiesta.getTipoInput())];
				log.debug("Tipo file input: " + type);
				Class<? extends AUCDExecutor> executorClass = type.getExecutorClass();
				AUCDExecutor executor = null;
				try {
					executor = executorClass.newInstance();
					log.debug("Istanziato executor: " + executor.getClass().getSimpleName() );
				} catch (Exception e) {
					throw new ApplicationException(SbnErrorTypes.BATCH_CONFIGURATION_ERROR);
				}

				executor.execute(richiesta, tx, r, w, csv, log);

				output.setStato(ConstantsJMS.STATO_OK);

			} catch (ApplicationException e) {
				DaoManager.rollback(tx);
				throw e;
			} catch (FileNotFoundException e) {
				DaoManager.rollback(tx);
				throw new ApplicationException(SbnErrorTypes.ERROR_IO_READ_FILE);
			} catch (IOException e) {
				DaoManager.rollback(tx);
				throw new ApplicationException(SbnErrorTypes.ERROR_IO_READ_FILE);
			}

		} finally {
			try {
				DaoManager.commit(tx);
			} catch (Exception e) {}

			FileUtil.close(r);
			FileUtil.flush(w);
			FileUtil.close(w);
			FileUtil.flush(csv);
			FileUtil.close(csv);
		}

		return output;
	}

	private enum Field {
		polo,
		bib,
		serie,
		num,
		bid,
		opacid;
	}

	public static final String costruisciUri(TipoFileInput type, String prefix,
			String model, String suffix, DatiModelloUri data,
			boolean eliminaSpaziUri) throws ApplicationException {

		StringBuilder uri = new StringBuilder(1024);
		if (isFilled(prefix))
			uri.append(prefix);

		try {
			String tmp = new String(model);
			Matcher m = URI_FIELD_REGEX.matcher(tmp);
			while (m.find()) {
				String g = m.group().toLowerCase().replaceAll("\\[|\\]", "");
				Field field = Field.valueOf(g);
				String value = "";

				switch (type) {
				case Inventario:
				case ShippingManifest:
					switch (field) {
					case polo:
						value = fillRight(data.codPolo, ' ', 3);
						break;
					case bib:
						value = fillLeft(data.codBib, ' ', 3);
						break;
					case serie:
						value = fillRight(data.serie, ' ', 3);
						break;
					case num:
						value = String.format("%09d", data.num);
						break;
					case bid:
						value = data.bid;
						break;
					case opacid:
						value = SBNMarcUtil.formattaSbnId(data.bid);
						break;
					}
					break;

				case BID:
					switch (field) {
					case polo:
						value = fillRight(data.codPolo, ' ', 3);
						break;
					case bib:
						value = fillLeft(data.codBib, BLANK, 3);
						break;
					case bid:
						value =  data.bid;
						break;
					case opacid:
						value = SBNMarcUtil.formattaSbnId(data.bid);
						break;
					case serie:
					case num:
						throw new Exception();
					}
					break;

				default:
					break;
				}

				tmp = tmp.substring(0, m.start()) + value + tmp.substring(m.end());
				m = URI_FIELD_REGEX.matcher(tmp);
			}
			uri.append(tmp);
		} catch (Exception e) {
			throw new ApplicationException(SbnErrorTypes.ERROR_GENERIC_FIELD_FORMAT, "label.documentofisico.uri.model");
		}

		if (isFilled(suffix))
			uri.append(suffix);

		String out = uri.toString();
		//almaviva5_20131001 evolutive google2
		if (eliminaSpaziUri)
			out = out.replaceAll("\\s+", "");

		return out;
	}

	public static final List<TB_CODICI> getListaTipoFileInput() {
		List<TB_CODICI> output = new ArrayList<TB_CODICI>();
		//output.add(new TB_CODICI("0", "Inventario"));
		//output.add(new TB_CODICI("1", "BID"));
		output.add(new TB_CODICI("2", "Inventario + URI"));
		output.add(new TB_CODICI("3", "BID + URI"));
//		output.add(new TB_CODICI("4", "Biblioteca + BID + [Inventario] + URI"));
//		output.add(new TB_CODICI("5", "Shipping Manifest");
		return output;
	}

	public enum TipoAggiornamentoUri {
		 AGGIORNA_SPECIFICHE,
		 AGGIUNGI_URI,
		 SOSTITUISCI_URI,
		 NON_AGGIORNARE;
	}

	public static class AggiornamentoUri {
		final TipoAggiornamentoUri tipoAggiornamento;
		final String uri;

		private AggiornamentoUri(TipoAggiornamentoUri tipoAggiornamento, String uri) {
			this.tipoAggiornamento = tipoAggiornamento;
			this.uri = uri;
		}

		public static AggiornamentoUri build(TipoAggiornamentoUri tipoAggiornamento, String uri) {
			return new AggiornamentoUri(tipoAggiornamento, uri);
		}

		public TipoAggiornamentoUri getTipoAggiornamento() {
			return tipoAggiornamento;
		}

		public String getUri() {
			return uri;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("AggiornamentoUri [");
			if (tipoAggiornamento != null)
				builder.append("tipo=").append(tipoAggiornamento).append(", ");
			if (uri != null)
				builder.append("uri=").append(uri);
			builder.append("]");
			return builder.toString();
		}
	}

	public static class DatiModelloUri extends SerializableVO {

		private static final long serialVersionUID = 6003515989207146776L;

		String codPolo;
		String codBib;
		String anaBib;
		String serie;
		int num;
		String bid;
		String sbnId;

		public static DatiModelloUri build(InventarioVO inv) {
			DatiModelloUri dmu = new DatiModelloUri();
			dmu.codPolo = inv.getCodPolo();
			dmu.codBib = inv.getCodBib();
			dmu.serie = inv.getCodSerie();
			dmu.num = inv.getCodInvent();
			dmu.bid = inv.getBid();
			return dmu;
		}

		public static DatiModelloUri build(String codPolo, String codBib, String bid) {
			DatiModelloUri dmu = new DatiModelloUri();
			dmu.codPolo = codPolo;
			dmu.codBib = codBib;
			dmu.bid = bid;
			return dmu;
		}

	}

	private static class NormalizedUri extends SerializableVO {

		private static final long serialVersionUID = -3766864480912827446L;

		private static final OrdinamentoUnicode u = OrdinamentoUnicode.getInstance();

		final String uri;
		final int key;

		public NormalizedUri(String aUri) {
			this.uri = trimOrEmpty(aUri);
			this.key = u.convert(uri).hashCode();
		}

		@Override
		public int hashCode() {
			return key;
		}

		@Override
		public boolean equals(Object obj) {
			NormalizedUri other = (NormalizedUri) obj;
			if (key != other.key)
				return false;
			return true;
		}
	}

	public static final AggiornamentoUri trattamentoUri(String oldUri, String newUri, boolean aggiungi) {

		if (!isFilled(oldUri))
			return AggiornamentoUri.build(TipoAggiornamentoUri.AGGIUNGI_URI, newUri);

		if (!isFilled(newUri))
			return AggiornamentoUri.build(TipoAggiornamentoUri.AGGIORNA_SPECIFICHE, oldUri);

		Set<NormalizedUri> uris = new LinkedHashSet<NormalizedUri>();	//mantiene ordine inserimento

		//check uri multipli
		String[] tokens = trimOrEmpty(oldUri).split(REGEX_URI_LOC_SEPARATORE);

		for (String token : tokens)
			uris.add(new NormalizedUri(token));

		NormalizedUri uri = new NormalizedUri(newUri);

		boolean hasMany = (tokens.length > 1);
		if (hasMany) {
			//uri multipli
			if (uris.contains(uri))
				return AggiornamentoUri.build(TipoAggiornamentoUri.AGGIORNA_SPECIFICHE, oldUri);
			if (aggiungi) {
				StringBuilder buf = new StringBuilder(Constants.URI_MAX_LENGTH);
				buf.append(oldUri).append(URI_LOC_SEPARATORE).append(newUri);
				return AggiornamentoUri.build(TipoAggiornamentoUri.AGGIUNGI_URI, buf.toString() );
			} else
				return AggiornamentoUri.build(TipoAggiornamentoUri.NON_AGGIORNARE, oldUri );
		} else {
			//uri singolo
			if (uris.contains(uri))
				return AggiornamentoUri.build(TipoAggiornamentoUri.AGGIORNA_SPECIFICHE, oldUri);
			if (aggiungi) {
				StringBuilder buf = new StringBuilder(Constants.URI_MAX_LENGTH);
				buf.append(oldUri).append(URI_LOC_SEPARATORE).append(newUri);
				return AggiornamentoUri.build(TipoAggiornamentoUri.AGGIUNGI_URI, buf.toString() );
			} else
				return AggiornamentoUri.build(TipoAggiornamentoUri.SOSTITUISCI_URI, newUri);
		}

	}

	public static void main(String[] args) throws ApplicationException {

		InventarioVO inv = new InventarioVO();
		inv.setCodPolo("ZZZ");
		inv.setCodBib(" YY");
		inv.setCodSerie("   ");
		inv.setCodInvent(5377);
		inv.setBid("ABC123456");
		DatiModelloUri dati = DatiModelloUri.build(inv);
		//String uri = costruisciUri(TipoFileInput.Inventario, "http://test.org", "[Bid]/[bib][serie][num]", "data", inv, true);
		String uri = costruisciUri(TipoFileInput.BID, "http://test.org", "/[Bid]/[bib]/", "data", dati, true);
		System.out.println(uri);

		AggiornamentoUri newUri = trattamentoUri("http://test.org/ABC123456/YY000005377/data2 | http://test.org/ABC123456/YY/DATA", uri, true);
		System.out.println(newUri);
	}
}
