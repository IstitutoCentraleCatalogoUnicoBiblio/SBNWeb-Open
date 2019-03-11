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
package it.iccu.sbn.util;

import gnu.trove.THashMap;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.stampe.StampeUtil;
import it.iccu.sbn.ejb.vo.gestionestampe.StampaBuoniDiOrdineVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.ModelloStampaVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.SubReportVO;
import it.iccu.sbn.ejb.vo.gestionestampe.common.TipoStampa;
import it.iccu.sbn.servizi.batch.BatchManager;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.file.FileUtil;
import it.iccu.sbn.util.jasper.JRSerializedObjectDataSource;
import it.iccu.sbn.web2.util.Constants;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.FontKey;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.PdfFont;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRImageLoader;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.engine.util.JRSwapFile;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;



/**
 * @author almaviva almaviva
 *
 * attenzione: il codice è stato parzialmente riscritto, ma
 * contiene ancora delle parti non scritte da me di cui non
 * conosco la funzione (se ne hanno)
 *
 */
public class SbnStampe {

	static Logger log = Logger.getLogger(SbnStampe.class);

	//il nome completo del file fisico jasper
	private String pathJasper = null;
	//il jasper (design compilato)
	private byte[] jasperReport;
	//il print (stampa da esportare poi nei vari formati)
	private JasperPrint jasperPrint = null;
	//il tipo stampa utilizzato per l'esportazione
	private TipoStampa formato = TipoStampa.PDF;// PDF;

	private static final int BUFFER_SIZE = 65536;

	private static final String TMP_PATH = FileUtil.getTempFilesDir();
	private static final String JRXML_PATH = "/jrxml/";

	/**
	 * costruttore da Design contenuto in un file jrxml. Compilo se
	 * necessario (nella directory jasper) e carico quindi il Report
	 * da filesystem.
	 *
	 * Qualora un report necessiti di subreports ci assicuriamo
	 * della presenza dei subReports compilati nella directory jasper
	 *
	 * @param pathJrxml
	 * path completo del file jrxml da leggere sul filesystem
	 * @throws FileNotFoundException
	 * @throws JRException
	 */
	public SbnStampe(String pathJrxml) throws FileNotFoundException,
			JRException {

		//verifico i subreport e li compilo se presenti
		int l1 = pathJrxml.lastIndexOf(File.separator);
		int l2 = pathJrxml.lastIndexOf(".");
		try{
			ModelloStampaVO modelloStampa = CodiciProvider.getModelloStampa(pathJrxml.substring(l1 + 1, l2));
			if (modelloStampa != null) {
				List<String> subs = modelloStampa.getSubReports();
				if (ValidazioneDati.isFilled(subs))
					for (String jrxml : subs)
						this.makeJasperFile(pathJrxml.substring(0, l1 + 1) + jrxml + ".jrxml");

			}

		} catch(Exception e) {
			log.error("errore compilazione subreport: ", e);
		}

		//creo il file jasper se necessario
		this.pathJasper = this.makeJasperFile(pathJrxml);
		// leggo comunque il jasper da filesystem
		File fileJasper = new File(pathJasper);
		this.jasperReport = JRLoader.loadBytes(fileJasper);
	}

	protected String makeJasperFile(String pathJrxml) throws FileNotFoundException, JRException {

		String fileName = pathJrxml.substring(pathJrxml.lastIndexOf(File.separator) + 1);
		//almaviva5_20100728 file jasper nella cartella temp di sistema
		pathJasper = (TMP_PATH + File.separator + fileName).replace("jrxml", "jasper");
		InputStream in = this.getClass().getResourceAsStream(JRXML_PATH + fileName);
		// il design va compilato su disco e usato da disco
		JasperDesign jasperDesign = JRXmlLoader.load(in);
		// almaviva5_20100112
		jasperDesign.setProperty(
				JRStyledText.PROPERTY_AWT_IGNORE_MISSING_FONT,
				Boolean.TRUE.toString());

		// creo il jasper effettuando la compilazione
		JasperCompileManager.compileReportToFile(jasperDesign, pathJasper);

		return pathJasper;
	}

	/**
	 * costruttore da Design passato come oggetto
	 * @param jasperDesign
	 * il design già istanziato e caricato
	 * @throws JRException
	 */
	public SbnStampe(JasperDesign jasperDesign) throws JRException {
		//mi sto instanziando a partire da un oggetto design creato "al volo" NON devo salvare il jasper
		this.pathJasper = null;
		//almaviva5_20100112
		jasperDesign.setProperty(JRStyledText.PROPERTY_AWT_IGNORE_MISSING_FONT, Boolean.TRUE.toString());
		//il design che ho nella classe va compilato in memoria
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JasperCompileManager.compileReportToStream(jasperDesign, baos);
		this.jasperReport = baos.toByteArray();//byte[]
	}

	/**
	 *
	 * il metodo ritorna sotto forma di byte[] la stampa richiesta
	 *
	 * @param dati l'arraylist di VO contenente i dati da stampare
	 * @param parametri gli eventuali parametri da passare al report
	 * @return la stampa realizzata
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public InputStream stampa(Object dati, Map parametri) throws Exception {
		if (parametri == null)
			parametri = new THashMap();
		// inibiamo la paginazione per i tipi che non la richiedono.
		// se non specificato formalmente dal chiamante
		if (ValidazioneDati.in(this.formato,
				TipoStampa.XLS,
				TipoStampa.HTML,
				TipoStampa.CSV)
			&& !parametri.containsKey(JRParameter.IS_IGNORE_PAGINATION))
			parametri.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);

		//almaviva5_20100704 swap file per report oltre 100 pagine
		JRSwapFile swapFile = new JRSwapFile(TMP_PATH, 2048, 1024);
		JRSwapFileVirtualizer cache = new JRSwapFileVirtualizer(32, swapFile, true);
		parametri.put(JRParameter.REPORT_VIRTUALIZER, cache);

		//almaviva5_20170824 #6472
		JRProperties.setProperty(JRParagraph.DEFAULT_TAB_STOP_WIDTH, "1");

		boolean existImage = false;

		File f;
		OutputStream output;
		try {
			//preparo i dati da passare al fill
			ByteArrayInputStream bais = new ByteArrayInputStream(this.getJasperReport());
			JRDataSource jrds = null;
			if (dati instanceof List) {
				jrds = new JRBeanCollectionDataSource((List) dati);
				Object o = ((List) dati).get(0);
				if (o instanceof StampaBuoniDiOrdineVO)
					existImage = true;

			} else if (dati instanceof SubReportVO)
				jrds = new JRSerializedObjectDataSource((SubReportVO) dati);
			else
				throw new JRException("Tipo oggetto non supportato come jasper data-source");

			// popolo il report
			this.jasperPrint = JasperFillManager.fillReport(bais, parametri, jrds);
			f = null;
			output = null;
			try {
				f = File.createTempFile(String.format("sbnweb_%d_%d", IdGenerator.getId(), System.currentTimeMillis()), ".tmp");
				output = new BufferedOutputStream(new FileOutputStream(f));
				BatchManager.getBatchManagerInstance().markForDeletion(f);
			} catch (Exception e) {
				throw new JRException(e);
			}

			//creiamo ora la stampa da ritornare utilizzando l'exporter opportuno a
			//seconda del tipo di stampa richiesto
			JRAbstractExporter exporter = null;
			switch (formato) {
			case PDF:
				exporter = new JRPdfExporter();
				Map fontMap = new HashMap();
				//gv patch grassetto 20081210
				fontMap.put(new FontKey("DejaVu Sans", true, false), new PdfFont("Helvetica-Bold", "Cp1252", false));
				exporter.setParameter(JRPdfExporterParameter.FONT_MAP, fontMap);
				break;

			case RTF:
				exporter = new JRRtfExporter();
				exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, new Integer(80));
				exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, new Integer(120));
				exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, Integer.valueOf(80));
				exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, Integer.valueOf(120));
				exporter.setParameter(JRTextExporterParameter.CHARACTER_ENCODING, "UTF-8");
				exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,	Boolean.TRUE);
				break;

			case TXT:
				exporter = new JRTextExporter();
				exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, new Integer(80));
				exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, new Integer(120));
				exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, Integer.valueOf(80));
				exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, Integer.valueOf(120));
				exporter.setParameter(JRTextExporterParameter.CHARACTER_ENCODING, "UTF-8");
				exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.TRUE);
				break;

			case XLS:
				//almaviva5_20100709 flag per abilitare caching su file di JXL 2.6
				System.setProperty("jxl.temporaryfileduringwritedirectory",	TMP_PATH);
				System.setProperty("jxl.usetemporaryfileduringwrite", Boolean.TRUE.toString());
				exporter = new JExcelApiExporter();
				if (!existImage)
					exporter.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);

				exporter.setParameter(JExcelApiExporterParameter.IS_FONT_SIZE_FIX_ENABLED,Boolean.TRUE);
				exporter.setParameter(JExcelApiExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
				exporter.setParameter(JExcelApiExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				exporter.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,	Boolean.TRUE);
				exporter.setParameter(JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET,	Boolean.FALSE);
				//almaviva5_20100701
				exporter.setParameter(JExcelApiExporterParameter.MAXIMUM_ROWS_PER_SHEET, new Integer(Short.MAX_VALUE));
				break;

			case HTML:
				exporter = new JRHtmlExporter();
				exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,	false);
				String pathImages = "";
				if (pathJasper != null) {
					int indiceFileS = this.pathJasper.lastIndexOf(File.separator);
					pathImages = this.pathJasper.substring(0, indiceFileS);
					indiceFileS = pathImages.lastIndexOf(File.separator);
					pathImages = pathImages.substring(0, indiceFileS);
					pathImages = pathImages + "images";// +File.separator+"on.jpg"
					exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR_NAME, pathImages);
				}

				exporter.setParameter(JRHtmlExporterParameter.SIZE_UNIT, JRHtmlExporterParameter.SIZE_UNIT_POINT);
				exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, true);
				String pathDownload = StampeUtil.getBatchFilesPath();
				// almaviva4 16.10.09
				exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR_NAME, pathDownload	+ "images" + File.separator); // "images" +  File.separator
				exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "images" + File.separator); // valore precedente al 15/10/09 "images?image="
				break;

			case CSV:
				exporter = new JRCsvExporter();
				String sep = CommonConfiguration.getProperty(Configuration.CSV_FIELD_SEPARATOR,
						String.valueOf(Constants.CSV_DEFAULT_SEPARATOR));
				exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, sep);
				break;

			default:
				exporter = new JRHtmlExporter();
				exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,	false);
				exporter.setParameter(JRHtmlExporterParameter.SIZE_UNIT, JRHtmlExporterParameter.SIZE_UNIT_POINT);
				break;
			}

			exporter.setParameter(JRExporterParameter.JASPER_PRINT, this.jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
			exporter.exportReport();

		} finally {
			cache.cleanup();
		}

		try {
			FileUtil.flush(output);
			FileUtil.close(output);

			return new BufferedInputStream(new FileInputStream(f));

		} catch (IOException e) {
			new JRException(e);
		}

		return null;
	}


	private static Image loadImage(byte[] bytes) throws JRException {
		Image image = Toolkit.getDefaultToolkit().createImage(bytes);

		MediaTracker traker = new MediaTracker(new Panel());
		traker.addImage(image, 0);
		try {
			traker.waitForID(0);
		} catch (Exception e) {
			throw new JRException(e);
		}

		return image;
	}

	private static byte[] loadImageDataFromInputStream(InputStream is)
			throws JRException {
		try {
			return JRLoader.loadBytes(is);
		} catch (JRException e) {
			throw new JRException(
					"Error loading image data from input stream.", e);
		}
	}

	public Image getImage(String image) throws JRException {
		try {
			image = image.replace('\\', '/');
			ClassLoader classLoader = Thread.currentThread()
					.getContextClassLoader();
			URL url = classLoader.getResource(image);
			if (url == null) {
				classLoader = JRImageLoader.class.getClassLoader();
			}
			InputStream is;
			if (classLoader == null) {
				String app = File.separator + image;
				is = JRImageLoader.class.getResourceAsStream(app);// "/" +
																	// image);
			} else {
				is = classLoader.getResourceAsStream(image);
			}

			return loadImage(loadImageDataFromInputStream(is));
		} catch (JRException e) {
			throw new JRException("Error loading image data : " + image, e);
		}
	}

	public void setFormato(TipoStampa formato) {
		this.formato = formato;
	}

	public TipoStampa getFormato() {
		return formato;
	}

	public JasperPrint getJasperPrint() {
		return jasperPrint;
	}

	public byte[] getJasperReport() {
		return jasperReport;
	}

	public static final void transferData(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[BUFFER_SIZE];
		int read;
		while ( (read = in.read(buf)) > 0)
			out.write(buf, 0, read);
	}
}
