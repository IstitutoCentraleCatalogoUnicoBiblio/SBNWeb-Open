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
package it.iccu.sbn.util.stampe.excel;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.acquisizioni.ParametriExcelVO;
import it.iccu.sbn.ejb.vo.acquisizioni.common.StrutturaCombo;
import it.iccu.sbn.ejb.vo.gestionestampe.common.excel.ExcelPrintable;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.File;
import java.math.BigInteger;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import jxl.HeaderFooter;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.PageOrientation;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableFont.FontName;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelPrintUtil {

	private static final int JXL_SHEET_MAX_ROWS = 65536 - 1;

	private static Logger log = Logger.getLogger(ExcelPrintUtil.class);

	public static final ParametriExcelVO getExcelParametriDefault() {
		ParametriExcelVO passa = new ParametriExcelVO();
		passa.setOrientamentoPagina("PORTRAIT");
		passa.setTipoCarattereDat("ARIAL");
		passa.setDimensioneCarattereDat("10");
		passa.setBordiDat("ALL");
		passa.setSpessoreBordoDat("THIN");
		passa.setColorIntestazioniBckDat("AUTOMATIC");
		passa.setAllineamentoDat("LEFT");
		passa.setOrientamentoDat("HORIZONTAL");
		passa.setTipoCarattereInt("ARIAL");
		passa.setDimensioneCarattereInt("14");
		passa.setStileCarattereInt("NO_BOLD");
		passa.setBordiInt("ALL");
		passa.setSpessoreBordoInt("THIN");
		passa.setColorIntestazioniBckInt("AUTOMATIC");
		passa.setAllineamentoInt("LEFT");
		passa.setOrientamentoInt("HORIZONTAL");
		passa.setTipoCarattereTit("ARIAL");
		passa.setDimensioneCarattereTit("10");
		passa.setStileCarattereTit("BOLD");
		passa.setBordiTit("ALL");
		passa.setSpessoreBordoTit("THIN");
		passa.setColorIntestazioniBckTit("AUTOMATIC");
		passa.setAllineamentoTit("LEFT");
		passa.setOrientamentoTit("HORIZONTAL");
		return passa;
	}

	private static final WritableSheet setupSheet(ParametriExcelVO params, final WritableWorkbook workbook, int idx) {
		WritableSheet sheet = workbook.createSheet("Foglio " + (idx + 1), idx);

		//imposta intestazione di foglio
		SheetSettings ss = new SheetSettings(sheet) ;
		HeaderFooter  hf = new HeaderFooter();

		hf.getLeft().append(params.getQuery());

		//sulla seconda riga a sinistra vanno le variabili di input
		hf.getCentre().append("");
		hf.getRight().appendDate();
		ss.setHeader(hf);
		sheet.getSettings().setHeader(ss.getHeader());

		sheet.setPageSetup(PageOrientation.LANDSCAPE);

		String orientamentoPagina = params.getOrientamentoPagina();
		if (ValidazioneDati.isFilled(orientamentoPagina) ) {
			if (orientamentoPagina.equals("LANDSCAPE")) {
				sheet.setPageSetup(PageOrientation.LANDSCAPE);
			} else if (orientamentoPagina.equals("PORTRAIT")) {
				sheet.setPageSetup(PageOrientation.PORTRAIT);
			}

		}

		return sheet;
	}

	@SuppressWarnings("unchecked")
	public static final void createExcelFile(ParametriExcelVO params, List<StrutturaCombo> intestazioni, List<?> risultati, File xlsFile) throws Exception {
		try {
			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("it", "IT"));
			ws.setEncoding("ISO-8859-1");
			WritableWorkbook workbook = Workbook.createWorkbook(xlsFile, ws);

			int sheetIndex = 0;

			WritableSheet sheet = setupSheet(params, workbook, sheetIndex);

			NumberFormat numeriFormat = new NumberFormat("###############");
			NumberFormat percentFormat = new NumberFormat("0.00");
			//	 formato per i dati
			WritableFont arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);

			FontName fontTipoDati=WritableFont.ARIAL;
			int fontSizeDati=10;

			if (params.getTipoCarattereDat()!=null && params.getTipoCarattereDat().trim().length()>0)
			{
				if (params.getTipoCarattereDat().equals("ARIAL"))
				{
					fontTipoDati=WritableFont.ARIAL;
				}
				else if (params.getTipoCarattereDat().equals("TAHOMA"))
				{
					fontTipoDati=WritableFont.TAHOMA;
				}
				else if (params.getTipoCarattereDat().equals("TAHOMA"))
				{
					fontTipoDati=WritableFont.TAHOMA;
				}
				else if (params.getTipoCarattereDat().equals("COURIER"))
				{
					fontTipoDati=WritableFont.COURIER;
				}
			}

			fontSizeDati=10;

			if (params.getDimensioneCarattereDat()!=null && params.getDimensioneCarattereDat().trim().length()>0)
			{
				if (params.getDimensioneCarattereDat().equals("10"))
				{
					fontSizeDati=10;
				}
				else if (params.getDimensioneCarattereDat().equals("12"))
				{
					fontSizeDati=12;
				}
				else if (params.getDimensioneCarattereDat().equals("14"))
				{
					fontSizeDati=14;
				}
			}

			arial10font = new WritableFont(fontTipoDati, fontSizeDati, WritableFont.BOLD);

			if (params.getStileCarattereDat().equals("BOLD"))
			{
				arial10font = new WritableFont(fontTipoDati, fontSizeDati, WritableFont.BOLD);
			}
			else
			{
				arial10font = new WritableFont(fontTipoDati, fontSizeDati, WritableFont.NO_BOLD);
			}



			WritableCellFormat formatDati = new WritableCellFormat(arial10font,percentFormat);


			formatDati.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);


			jxl.format.Border bordoDati=jxl.format.Border.ALL;
			jxl.format.BorderLineStyle bordoStileDati = jxl.format.BorderLineStyle.THIN;

			if (params.getBordiDat()!=null && params.getBordiDat().trim().length()>0)
			{
				if (params.getBordiDat().equals("ALL"))
				{
					bordoDati=jxl.format.Border.ALL;
				}
				else if (params.getBordiDat().equals("NONE"))
				{
					bordoDati=jxl.format.Border.NONE;
				}
				else if (params.getBordiDat().equals("BOTTOM"))
				{
					bordoDati=jxl.format.Border.BOTTOM;
				}
				else if (params.getBordiDat().equals("LEFT"))
				{
					bordoDati=jxl.format.Border.LEFT;
				}
				else if (params.getBordiDat().equals("RIGHT"))
				{
					bordoDati=jxl.format.Border.RIGHT;
				}
				else if (params.getBordiDat().equals("TOP"))
				{
					bordoDati=jxl.format.Border.TOP;
				}
			}
			if (params.getSpessoreBordoDat()!=null && params.getSpessoreBordoDat().trim().length()>0)
			{
				if (params.getSpessoreBordoDat().equals("THIN"))
				{
					bordoStileDati = jxl.format.BorderLineStyle.THIN;
				}
				else if (params.getSpessoreBordoDat().equals("MEDIUM"))
				{
					bordoStileDati = jxl.format.BorderLineStyle.MEDIUM;
				}
				else if (params.getSpessoreBordoDat().equals("NONE"))
				{
					bordoStileDati = jxl.format.BorderLineStyle.NONE;
				}
			}
			formatDati.setBorder(bordoDati, bordoStileDati);




			formatDati.setBackground(jxl.format.Colour.WHITE);
			if (params.getColorIntestazioniBckDat()!=null && params.getColorIntestazioniBckDat().trim().length()>0)
			{
				if (params.getColorIntestazioniBckDat().equals("LIGHT_TURQUOISE"))
				{
					formatDati.setBackground(jxl.format.Colour.LIGHT_TURQUOISE);
				}
				else if (params.getColorIntestazioniBckDat().equals("LIGHT_GREEN"))
				{
					formatDati.setBackground(jxl.format.Colour.LIGHT_GREEN);
				}
				else if (params.getColorIntestazioniBckDat().equals("VERY_LIGHT_YELLOW"))
				{
					formatDati.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);
				}
				else if (params.getColorIntestazioniBckDat().equals("AUTOMATIC"))
				{
					formatDati.setBackground(jxl.format.Colour.WHITE);
				}

			}

			formatDati.setAlignment(jxl.format.Alignment.LEFT);
			if (params.getAllineamentoDat()!=null && params.getAllineamentoDat().trim().length()>0)
			{
				if (params.getAllineamentoDat().equals("CENTRE"))
				{
					formatDati.setAlignment(jxl.format.Alignment.CENTRE);  // solo per i titoli
				}
				else if (params.getAllineamentoDat().equals("JUSTIFIY"))
				{
					formatDati.setAlignment(jxl.format.Alignment.JUSTIFY);  // solo per i titoli
				}
				else if (params.getAllineamentoDat().equals("LEFT"))
				{
					formatDati.setAlignment(jxl.format.Alignment.LEFT);  // solo per i titoli
				}
				else if (params.getAllineamentoDat().equals("RIGHT"))
				{
					formatDati.setAlignment(jxl.format.Alignment.RIGHT);  // solo per i titoli
				}
			}

			formatDati.setOrientation(jxl.format.Orientation.HORIZONTAL);
			if (params.getOrientamentoDat()!=null && params.getOrientamentoDat().trim().length()>0)
			{
				if (params.getOrientamentoDat().equals("HORIZONTAL"))
				{
					formatDati.setOrientation(jxl.format.Orientation.HORIZONTAL);
				}
				else if (params.getOrientamentoDat().equals("VERTICAL"))
				{
					formatDati.setOrientation(jxl.format.Orientation.VERTICAL);
				}

			}

			formatDati.setWrap(true);
			formatDati.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			formatDati.setLocked(false);

			// formato per le intestazioni
			WritableFont arial10font1 = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);

			FontName fontTipoInt=WritableFont.ARIAL;
			int fontSizeInt=10;

			if (params.getTipoCarattereInt()!=null && params.getTipoCarattereInt().trim().length()>0)
			{
				if (params.getTipoCarattereInt().equals("ARIAL"))
				{
					fontTipoInt=WritableFont.ARIAL;
				}
				else if (params.getTipoCarattereInt().equals("TAHOMA"))
				{
					fontTipoInt=WritableFont.TAHOMA;
				}
				else if (params.getTipoCarattereInt().equals("TAHOMA"))
				{
					fontTipoInt=WritableFont.TAHOMA;
				}
				else if (params.getTipoCarattereInt().equals("COURIER"))
				{
					fontTipoInt=WritableFont.COURIER;
				}
			}

			fontSizeInt=10;

			if (params.getDimensioneCarattereInt()!=null && params.getDimensioneCarattereInt().trim().length()>0)
			{
				if (params.getDimensioneCarattereInt().equals("10"))
				{
					fontSizeInt=10;
				}
				else if (params.getDimensioneCarattereInt().equals("12"))
				{
					fontSizeInt=12;
				}
				else if (params.getDimensioneCarattereInt().equals("14"))
				{
					fontSizeInt=14;
				}
			}

			arial10font1 = new WritableFont(fontTipoInt, fontSizeInt, WritableFont.BOLD);

			if (params.getStileCarattereInt().equals("BOLD"))
			{
				arial10font1 = new WritableFont(fontTipoInt, fontSizeInt, WritableFont.BOLD);
			}
			else
			{
				arial10font1 = new WritableFont(fontTipoInt, fontSizeInt, WritableFont.NO_BOLD);
			}



			WritableCellFormat formatIntestazione = new WritableCellFormat(arial10font1,numeriFormat);
			formatIntestazione.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
			jxl.format.Border bordoInt=jxl.format.Border.ALL;
			jxl.format.BorderLineStyle bordoStileInt = jxl.format.BorderLineStyle.THIN;

			if (params.getBordiInt()!=null && params.getBordiInt().trim().length()>0)
			{
				if (params.getBordiInt().equals("ALL"))
				{
					bordoInt=jxl.format.Border.ALL;
				}
				else if (params.getBordiInt().equals("NONE"))
				{
					bordoInt=jxl.format.Border.NONE;
				}
				else if (params.getBordiInt().equals("BOTTOM"))
				{
					bordoInt=jxl.format.Border.BOTTOM;
				}
				else if (params.getBordiInt().equals("LEFT"))
				{
					bordoInt=jxl.format.Border.LEFT;
				}
				else if (params.getBordiInt().equals("RIGHT"))
				{
					bordoInt=jxl.format.Border.RIGHT;
				}
				else if (params.getBordiInt().equals("TOP"))
				{
					bordoInt=jxl.format.Border.TOP;
				}
			}
			if (params.getSpessoreBordoInt()!=null && params.getSpessoreBordoInt().trim().length()>0)
			{
				if (params.getSpessoreBordoInt().equals("THIN"))
				{
					bordoStileInt = jxl.format.BorderLineStyle.THIN;
				}
				else if (params.getSpessoreBordoInt().equals("MEDIUM"))
				{
					bordoStileInt = jxl.format.BorderLineStyle.MEDIUM;
				}
				else if (params.getSpessoreBordoInt().equals("NONE"))
				{
					bordoStileInt = jxl.format.BorderLineStyle.NONE;
				}
			}
			formatIntestazione.setBorder(bordoInt, bordoStileInt);




			formatIntestazione.setBackground(jxl.format.Colour.WHITE);

			if (params.getColorIntestazioniBckInt()!=null && params.getColorIntestazioniBckInt().trim().length()>0)
			{
				if (params.getColorIntestazioniBckInt().equals("LIGHT_TURQUOISE"))
				{
					formatIntestazione.setBackground(jxl.format.Colour.LIGHT_TURQUOISE);
				}
				else if (params.getColorIntestazioniBckInt().equals("LIGHT_GREEN"))
				{
					formatIntestazione.setBackground(jxl.format.Colour.LIGHT_GREEN);
				}
				else if (params.getColorIntestazioniBckInt().equals("VERY_LIGHT_YELLOW"))
				{
					formatIntestazione.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);
				}
				else if (params.getColorIntestazioniBckInt().equals("AUTOMATIC"))
				{
					formatIntestazione.setBackground(jxl.format.Colour.WHITE);
				}

			}


			formatIntestazione.setAlignment(jxl.format.Alignment.JUSTIFY);
			if (params.getAllineamentoInt()!=null && params.getAllineamentoInt().trim().length()>0)
			{
				if (params.getAllineamentoInt().equals("CENTRE"))
				{
					formatIntestazione.setAlignment(jxl.format.Alignment.CENTRE);
				}
				else if (params.getAllineamentoInt().equals("JUSTIFIY"))
				{
					formatIntestazione.setAlignment(jxl.format.Alignment.JUSTIFY);
				}
				else if (params.getAllineamentoInt().equals("LEFT"))
				{
					formatIntestazione.setAlignment(jxl.format.Alignment.LEFT);
				}
				else if (params.getAllineamentoInt().equals("RIGHT"))
				{
					formatIntestazione.setAlignment(jxl.format.Alignment.RIGHT);
				}

			}
			formatIntestazione.setOrientation(jxl.format.Orientation.HORIZONTAL);
			if (params.getOrientamentoInt()!=null && params.getOrientamentoInt().trim().length()>0)
			{
				if (params.getOrientamentoInt().equals("HORIZONTAL"))
				{
					formatIntestazione.setOrientation(jxl.format.Orientation.HORIZONTAL);
				}
				else if (params.getOrientamentoInt().equals("VERTICAL"))
				{
					formatIntestazione.setOrientation(jxl.format.Orientation.VERTICAL);
				}

			}
			formatIntestazione.setWrap(true);
			formatIntestazione.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			formatIntestazione.setLocked(false);


			// formato per i titoli
			WritableFont arial10font2 = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);

			FontName fontTipoTit=WritableFont.ARIAL;
			int fontSizeTit=10;

			if (params.getTipoCarattereTit()!=null && params.getTipoCarattereTit().trim().length()>0)
			{
				if (params.getTipoCarattereTit().equals("ARIAL"))
				{
					fontTipoTit=WritableFont.ARIAL;
				}
				else if (params.getTipoCarattereTit().equals("TAHOMA"))
				{
					fontTipoTit=WritableFont.TAHOMA;
				}
				else if (params.getTipoCarattereTit().equals("TAHOMA"))
				{
					fontTipoTit=WritableFont.TAHOMA;
				}
				else if (params.getTipoCarattereTit().equals("COURIER"))
				{
					fontTipoTit=WritableFont.COURIER;
				}
			}

			if (params.getDimensioneCarattereTit()!=null && params.getDimensioneCarattereTit().trim().length()>0)
			{
				if (params.getDimensioneCarattereTit().equals("10"))
				{
					fontSizeTit=10;
				}
				else if (params.getDimensioneCarattereTit().equals("12"))
				{
					fontSizeTit=12;
				}
				else if (params.getDimensioneCarattereTit().equals("14"))
				{
					fontSizeTit=14;
				}
			}

			if (params.getStileCarattereTit().equals("BOLD"))
			{
				arial10font2 = new WritableFont(fontTipoTit, fontSizeTit, WritableFont.BOLD);
			}
			else
			{
				arial10font2 = new WritableFont(fontTipoTit, fontSizeTit, WritableFont.NO_BOLD);
			}


			WritableCellFormat formatTitoli = new WritableCellFormat(arial10font2,percentFormat);
			formatTitoli.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
			jxl.format.Border bordoTit=jxl.format.Border.ALL;
			jxl.format.BorderLineStyle bordoStileTit = jxl.format.BorderLineStyle.THIN;

			if (params.getBordiTit()!=null && params.getBordiTit().trim().length()>0)
			{

				if (params.getBordiTit().equals("ALL"))
				{
					bordoTit=jxl.format.Border.ALL;
				}
				else if (params.getBordiTit().equals("NONE"))
				{
					bordoTit=jxl.format.Border.NONE;
				}
				else if (params.getBordiTit().equals("BOTTOM"))
				{
					bordoTit=jxl.format.Border.BOTTOM;
				}
				else if (params.getBordiTit().equals("LEFT"))
				{
					bordoTit=jxl.format.Border.LEFT;
				}
				else if (params.getBordiTit().equals("RIGHT"))
				{
					bordoTit=jxl.format.Border.RIGHT;
				}
				else if (params.getBordiTit().equals("TOP"))
				{
					bordoTit=jxl.format.Border.TOP;
				}
			}
			if (params.getSpessoreBordoTit()!=null && params.getSpessoreBordoTit().trim().length()>0)
			{
				if (params.getSpessoreBordoTit().equals("THIN"))
				{
					bordoStileTit = jxl.format.BorderLineStyle.THIN;
				}
				else if (params.getSpessoreBordoTit().equals("MEDIUM"))
				{
					bordoStileTit = jxl.format.BorderLineStyle.MEDIUM;
				}
				else if (params.getSpessoreBordoTit().equals("NONE"))
				{
					bordoStileTit = jxl.format.BorderLineStyle.NONE;
				}
			}

			formatTitoli.setBorder(bordoTit, bordoStileTit);

			formatTitoli.setBackground(jxl.format.Colour.WHITE);
			if (params.getColorIntestazioniBckTit()!=null && params.getColorIntestazioniBckTit().trim().length()>0)
			{
				if (params.getColorIntestazioniBckTit().equals("LIGHT_TURQUOISE"))
				{
					formatTitoli.setBackground(jxl.format.Colour.LIGHT_TURQUOISE);
				}
				else if (params.getColorIntestazioniBckTit().equals("LIGHT_GREEN"))
				{
					formatTitoli.setBackground(jxl.format.Colour.LIGHT_GREEN);
				}
				else if (params.getColorIntestazioniBckTit().equals("VERY_LIGHT_YELLOW"))
				{
					formatTitoli.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);
				}
				else if (params.getColorIntestazioniBckTit().equals("AUTOMATIC"))
				{
					formatTitoli.setBackground(jxl.format.Colour.WHITE);
				}

			}

			formatTitoli.setAlignment(jxl.format.Alignment.LEFT);
			if (params.getAllineamentoTit()!=null && params.getAllineamentoTit().trim().length()>0)
			{
				if (params.getAllineamentoTit().equals("CENTRE"))
				{
					formatTitoli.setAlignment(jxl.format.Alignment.CENTRE);  // solo per i titoli
				}
				else if (params.getAllineamentoTit().equals("JUSTIFIY"))
				{
					formatTitoli.setAlignment(jxl.format.Alignment.JUSTIFY);  // solo per i titoli
				}
				else if (params.getAllineamentoTit().equals("LEFT"))
				{
					formatTitoli.setAlignment(jxl.format.Alignment.LEFT);  // solo per i titoli
				}
				else if (params.getAllineamentoTit().equals("RIGHT"))
				{
					formatTitoli.setAlignment(jxl.format.Alignment.RIGHT);  // solo per i titoli
				}
			}
			formatTitoli.setOrientation(jxl.format.Orientation.HORIZONTAL);
			if (params.getOrientamentoTit()!=null && params.getOrientamentoTit().trim().length()>0)
			{
				if (params.getOrientamentoTit().equals("HORIZONTAL"))
				{
					formatTitoli.setOrientation(jxl.format.Orientation.HORIZONTAL);
				}
				else if (params.getOrientamentoTit().equals("VERTICAL"))
				{
					formatTitoli.setOrientation(jxl.format.Orientation.VERTICAL);
				}

			}
			formatTitoli.setWrap(false); // consente di scrivere il testo oltre il limite della cella
			formatTitoli.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			formatTitoli.setLocked(false);

			WritableCellFormat formatDatiInteger = new WritableCellFormat(arial10font, NumberFormats.INTEGER);
			formatDatiInteger.setBorder(bordoDati, bordoStileDati);

			WritableCellFormat formatDatiFloat = new WritableCellFormat(arial10font, NumberFormats.FLOAT);
			formatDatiFloat.setBorder(bordoDati, bordoStileDati);

			WritableCellFormat formatDatiPercent = new WritableCellFormat(arial10font, percentFormat);
			formatDatiPercent.setBorder(bordoDati, bordoStileDati);


			int sheetRow = 0;
			for (int x = 0; x < params.getTitoli().size(); x++) {
				Label labelEle = new Label(0, 0 + x, params.getTitoli().get(x),	formatTitoli);
				WritableCellFeatures wcf = new WritableCellFeatures();
				wcf.setComment("titolo"); // per aggiungere un tooltip su cella
				labelEle.setCellFeatures(wcf);
				sheet.addCell(labelEle);
				sheetRow = 0 + x;
			}

			for (int x = 0; x < intestazioni.size(); x++) {
				if (x == 0)
					sheetRow += 3;

				Label labelEle = new Label(x, sheetRow, intestazioni.get(x).getCodice(), formatIntestazione);
				sheet.addCell(labelEle);
			}

			int size = ValidazioneDati.size(risultati);
			for (int riga = 0; riga < size; riga++) {
				sheetRow++;
				//almaviva5_20140124
				if ( (sheetRow % JXL_SHEET_MAX_ROWS) == 0) {
					sheet = setupSheet(params, workbook, ++sheetIndex);
					sheetRow = 1;
				}

				Object row = risultati.get(riga);
				if (row instanceof ExcelPrintable)
					row = ((ExcelPrintable)row).getValues();

				if (row != null	&& row instanceof Object[]) {
					Object[] rigaEle = (Object[]) row;
					for (int col = 0; col < rigaEle.length; col++) {
						Label labelEle;
						jxl.write.Number n;
						if (rigaEle[col] != null) {
							if (rigaEle[col] instanceof Number) {
								if (ValidazioneDati.in(rigaEle[col].getClass(), Integer.class, Long.class, BigInteger.class, Short.class) )
									n = new jxl.write.Number(col, sheetRow, ((Number)rigaEle[col]).doubleValue(), formatDatiInteger);
								else
									n = new jxl.write.Number(col, sheetRow, ((Number)rigaEle[col]).doubleValue(), formatDatiFloat);
								sheet.addCell(n);
							} else {
								if (ValidazioneDati.strIsFloat(rigaEle[col].toString().trim())) {
									n = new jxl.write.Number(col, sheetRow, (new Double(rigaEle[col].toString())).doubleValue(), formatDatiPercent);
									sheet.addCell(n);
								} else {
									labelEle = new Label(col, sheetRow, rigaEle[col].toString(), formatDati /* formatIntestazione */);
									sheet.addCell(labelEle);
								}
							}
						} else {
							labelEle = new Label(col, sheetRow, "", formatIntestazione);
							sheet.addCell(labelEle);
						}
					}
				} else {
					Label labelEle;

					if (row != null)
						labelEle = new Label(0, sheetRow, row.toString(), formatDati);
					else
						labelEle = new Label(0, sheetRow, "", formatDati);

					sheet.addCell(labelEle);
				}
			}

			workbook.write();
			workbook.close();

		} catch (Exception e) {
			log.error("", e);
			throw new ApplicationException(SbnErrorTypes.UNRECOVERABLE);
		}

	}

}
