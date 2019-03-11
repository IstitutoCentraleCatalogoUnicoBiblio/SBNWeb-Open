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
package it.iccu.sbn.batch.unimarc;

import gnu.trove.THashMap;

import it.iccu.sbn.persistence.dao.common.DaoManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.isFilled;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.size;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.substring;

public class Marc950 {

	private static final Pattern _YYYYMMDD = Pattern.compile("\\d{8}");
	private static final SimpleDateFormat _DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	private final String campoBiblioteca = "a";
	private final String campoConsistenzaEse = "b";
	private final String campoConsistenzaColl = "c";
	private final String campoCollocazione = "d";
	private final String campoInventario = "e";
	private final String campoCatFruizione = "f";
	private final String campoDataIngresso = "h";

	private String id;											//unimarc 001
	private String biblioteca;									//unimarc 950$a
	private Map<Integer, String> consistenzaEsemplare;	//unimarc 950$b
	private Map<Integer, String> consistenzaCollocazione;	//unimarc 950$c
	private Map<Integer, String> collocazione;			//unimarc 950$d
	private Map<Integer, List<Inventario950>> inventario;	//unimarc 950$e + 950$f + 950$h

	private static Map<String, Integer> campi950;
	static {
		campi950 = new THashMap<String, Integer>();
		campi950.put("a", 0);
		campi950.put("b", 1);
		campi950.put("c", 2);
		campi950.put("d", 3);
		campi950.put("e", 4);
//		campi950.put("f", 5);
//		campi950.put("h", 6);
//		campi950.put("i", 7);
//		campi950.put("l", 8);

		campi950.put("f", 4);
		campi950.put("h", 4);
		campi950.put("i", 4);
		campi950.put("l", 4);
		//almaviva5_20180904 note generiche (?)
		campi950.put("m", 4);
	}

	public static void main(String[] args) throws Exception {
//		Marc950 _950 = new Marc950("PUV0320378", " 0$aBIBLIOTECA ICPAL$dPLBIBL      05.B        00074       $ePL   0000521095        005                                                                                                                                                                                 ");
//		Marc950 _950 = new Marc950("PUV0320378", " 1$aBiblioteca Polo Roio - Facoltà di Economia        $d ECECPER     FORO ITALI              2005        $e EC   0000240925                            2005. v.130$d ECECPER     FORO ITALI              2006        $e EC   0000244955                            2006. v.131$b2004. v.129                                              $c2004. v.129$d ECECPER     FORO ITALI              2004        $e EC   0000221765                            2004. v.129");
//		Marc950 _950 = new Marc950("PUV0320378", " 1$aBiblioteca Polo Roio - Facoltà di Economia        $d ECECPER     FORO ITALI              2005        $e EC   0000240925                            2005. v.130		$d ECECPER     FORO ITALI              2006        $e EC   0000244955                            2006. v.131		$b2004. v.129                                              $c2004. v.129$d ECECPER     FORO ITALI              2004        $e EC   0000221765                            2004. v.129		$b2003. v.128                                              $c2003. v.128$d ECECPER     FORO ITALI              2003        $e EC   0000209535                            2003. v.128		$b2002. v.27                                              $c2002. v.27$d ECECPER     FORO ITALI              2002        $e EC   0000203125                            2002. v.27		$b2001. v.126                                              $c2001. v.126$d ECECPER     FORO ITALI              2001        $e EC   0000186035                            2001. v.126		$b1995.                                              $c1995.$d ECECPER     FORO ITALI              1995        $e EC   0000068815                            1995.$b1994.                                              $c1994.$d ECECPER     FORO ITALI              1994        $e EC   0000053745                            1994.$b1993.                                              $c1993.$d ECECPER     FORO ITALI              1993        $e EC   0000039645                            1993.$b1992.                                              $c1992.$d ECECPER     FORO ITALI              1992        $e EC   0000030835                            1992.$b117(1992)-                                              $c117(1992)-$d ECECPER     FORO ITALI              FORI        $e EC   0000088305                             ");
//		Marc950 _950 = new Marc950("PUV0320378", " 1$aBiblioteca Polo Roio - Facoltà di Economia        $d ECECPER     FORO ITALI              2005        $e EC   0000240925                            2005. v.130		$h20120101$d ECECPER     FORO ITALI$e EC   0000244955                            2006. v.131		$b2004. v.129                                              $c2004. v.129$e EC   0000221765                            2004. v.129		$b2003. v.128                                              $c2003. v.128$d ECECPER     FORO ITALI              2003        $e EC   0000209535                            2003. v.128		$b2002. v.27                                              $c2002. v.27$d ECECPER     FORO ITALI              2002        $e EC   0000203125                            2002. v.27		$b2001. v.126                                              $c2001. v.126$d ECECPER     FORO ITALI              2001        $e EC   0000186035                            2001. v.126		$c1995.$d ECECPER     FORO ITALI              1995        $e EC   0000068815                            1995.$b1994.                                              $d ECECPER     FORO ITALI              1994        $e EC   0000053745                            1994.$b1993.                                              $c1993.$d ECECPER     FORO ITALI              1993        $e EC   0000039645                            1993.$e EC   0000039645                            1993(bis).$b1992.                                              $c1992.$d ECECPER     FORO ITALI              1992        $e EC   0000030835                            1992.$b117(1992)-                                              $c117(1992)-$d ECECPER     FORO ITALI              FORI        $e EC   0000088305                             $fZ$h20050505$e EC   0000088306                          bis$fA$h19990101$e EC   0000088307                         tris");
//		Marc950 _950 = new Marc950("PUV0320378", " 1$aBiblioteca Polo Roio - Facoltà di Economia        $d ECECPER     FORO ITALI              2005        $e EC   0000240925                            2005. v.130		$h20120101$d ECECPER     FORO ITALI$e EC   0000244955                            2006. v.131		$b2004. v.129                                              $c2004. v.129$e EC   0000221765                            2004. v.129		$b2003. esemp                                              $c2003. v.128$d ECECPER     FORO ITALI              2003        $e EC   0000209535                            2003. v.128		$c2002. v.27$d ECECPER     FORO ITALI              2002        $e EC   0000203125                            2002. v.27		$besem. v.126                                              $c2001. v.126$d ECECPER     FORO ITALI              2001        $e EC   0000186035                            2001. v.126		$c1995.$d ECECPER     FORO ITALI              1995        $e EC   0000068815                            1995.$b1994.                                              $d ECECPER     FORO ITALI              1994        $e EC   0000053745                            1994.$b1993.                                              $c1993.$d ECECPER     FORO ITALI              1993        $e EC   0000039645                            1993.$e EC   0000039645                            1993(bis).$b1992.                                              $c1992.$d ECECPER     FORO ITALI              1992        $e EC   0000030835                            1992.$b117(1992)-                                              $c117(1992)-$d ECECPER     FORO ITALI              FORI        $e EC   0000088305                             $fZ$h20050505$e EC   0000088306                          bis$fA$h19990101$e EC   0000088307                         tris$d ECECPER1    FORO ITALz              zzzz        $e EC   0000088308                             $fZ$h20050505");
//		Marc950 _950 = new Marc950("POL0180708", " 1$a S8$d S8fs        MAG.        XXVII G     $e S8   0000196775        15");
//		Marc950 _950 = new Marc950("POL0180708", " 1$a CR$b$c1 v.$d CRISIAO     REFRD-306.089           EVA$e CRIAO0000680285  VM                        1 v. (già IIA 29789)$fB $h20060208$i20060208 ");
		Marc950 _950 = new Marc950("POL0180708", " 1$aBiblioteca ICCU Collaudo$d ICPER       1 0                     56$e IC   0000521985  VPB                       $fD $h20120830$i20120830");

		for (int i = 0; i < _950.getNumeroElementi(); i++) {
			System.out.println("- " + i);
			System.out.println(String.format("cons ese : '%s'", _950.getConsistenzaEsemplare().get(i)));
			System.out.println(String.format("cons coll: '%s'", _950.getConsistenzaCollocazione().get(i)));
			System.out.println(String.format("coll     : '%s'", _950.getCollocazione().get(i)));
			System.out.println(String.format("cdsez    : '%s'", _950.getCodSez(i)));
			System.out.println(String.format("cdloc    : '%s'", _950.getCodLoc(i)));
			System.out.println(String.format("cdspec   : '%s'", _950.getSpecLoc(i)));

			for (int j = 0; j < _950.getInventario().get(i).size(); j++) {
				Inventario950 inv = _950.getInventario().get(i).get(j);
				System.out.println(String.format("\tinvent   : '%s'", inv.getCodInventario()));
				System.out.println(String.format("\tcod.frui : '%s'", inv.getCodFrui()));
				System.out.println(String.format("\tdata ing.: '%s'", inv.getDataIngresso()));
				System.out.println(String.format("\tseq.coll.: '%s'", _950.getSequenzaCollocazione(i, j)));
				System.out.println(String.format("\ttipo.mat.: '%s'", _950.getCodMatInv(i, j)));
			}

			System.out.println();
		}
	}

	public Marc950(String id, String unimarc950) throws Exception{
		setId(id);
		init(unimarc950);
	}

	private void init(String unimarc950) throws Exception{
		// inizializzazione
		consistenzaEsemplare = new THashMap<Integer, String>();
		consistenzaCollocazione = new THashMap<Integer, String>();
		collocazione = new THashMap<Integer, String>();
		inventario = new THashMap<Integer, List<Inventario950>>();
		// valorizzazione
		setBiblioteca(MarcRecord.getDocFisicoNomeBiblioteca(unimarc950));
		splitRecords(unimarc950);
	}

	private void splitRecords(String unimarc950) throws Exception{
		String record = null;
		String consEse = "";	//esemplare di riferimento (consistenza => $b unimarc)
		String prevSubf = null; //sottocampo precedente
		String currSubf = null; //sottocampo corrente
		int pos = unimarc950.indexOf(MarcRecord._SUBFIELD_SEPARATOR);
		int pos2 = 0;
		if (pos > -1) {
			int counter = 0;
			currSubf = ((unimarc950.length() > (pos + 1))) ? unimarc950.substring(pos + 1, pos + 2) : " ";
			// non considera il sottocampo 'a' (nome biblioteca)
			pos = (currSubf.equalsIgnoreCase(campoBiblioteca))
					? unimarc950.substring(pos+2).indexOf(MarcRecord._SUBFIELD_SEPARATOR)
					: pos;
			List<Inventario950> tmpInventari = new ArrayList<Inventario950>();
			Inventario950 currInv = null;
			boolean newRecord;
			while (pos > -1) {
				pos2 = pos;
				record = unimarc950.substring(pos);
				pos = record.indexOf(MarcRecord._SUBFIELD_SEPARATOR);
				record = unimarc950.substring(pos2 + pos + 2); // substring da inizio a fine stringa
				currSubf = ((unimarc950.length() > (pos2 + pos + 1))) ? unimarc950.substring(pos2 + pos + 1, pos2 + pos + 2) : " ";
				prevSubf = (prevSubf == null) ? currSubf : prevSubf;
				pos = record.indexOf(MarcRecord._SUBFIELD_SEPARATOR);
				if (pos > -1) {
					// presenti ulteriori sottocampi
					record = record.substring(0, pos); 			// substring da inizio a separatore successivo
					pos += (pos == 0) ? (pos2 + 2) : pos2; 		// avanza di posizione (+2 se sf vuoto)
				}

				newRecord = smaller(currSubf, prevSubf);

				if (pos == -1) {
					// aggiunge il contenuto del sottocampo corrente al proprio List di riferimento
					if (campoConsistenzaEse.equalsIgnoreCase(currSubf)){
						if (!record.equals(consEse)) consEse = record; // esemplare diverso
						consistenzaEsemplare.put(counter, consEse);
					} else if (campoConsistenzaColl.equalsIgnoreCase(currSubf)){
						consistenzaCollocazione.put(counter, record);
					} else if (campoCollocazione.equalsIgnoreCase(currSubf)){
						collocazione.put(counter, record);
					} else if (campoInventario.equalsIgnoreCase(currSubf)){
						currInv = new Inventario950(record, "B", DaoManager.now() );
						currInv.setCodInventario(record);
						tmpInventari.add(currInv);
					} else if (campoCatFruizione.equalsIgnoreCase(currSubf)){
						currInv.setCodFrui(record);
					} else if (campoDataIngresso.equalsIgnoreCase(currSubf)){
						currInv.setDataIngresso(getDataIngresso(record));
					}
					// normalizza lunghezza arraylist
					if (consistenzaEsemplare.size()==(counter)) consistenzaEsemplare.put(counter, consEse); //esemplare di riferimento
					if (consistenzaCollocazione.size()==(counter)) consistenzaCollocazione.put(counter, "");
					if (collocazione.size()==(counter)) collocazione.put(counter, "");
					if (inventario.size()==(counter)) inventario.put(counter, tmpInventari);
				} else {
					if (newRecord) {
						counter++;
						// normalizza lunghezza arraylist
						if (consistenzaEsemplare.size()==(counter-1)) consistenzaEsemplare.put(counter-1, consEse); //esemplare di riferimento
						if (consistenzaCollocazione.size()==(counter-1)) consistenzaCollocazione.put(counter-1, "");
						if (collocazione.size()==(counter-1)) collocazione.put(counter-1, "");
						if (inventario.size()==(counter-1)) inventario.put(counter-1, tmpInventari);
						tmpInventari = new ArrayList<Inventario950>();
					}
					// aggiunge il contenuto del sottocampo corrente al proprio List di riferimento
					if (campoConsistenzaEse.equalsIgnoreCase(currSubf)){
						if (!record.equals(consEse)) consEse = record; // esemplare diverso
						consistenzaEsemplare.put(counter, consEse);
					} else if (campoConsistenzaColl.equalsIgnoreCase(currSubf)){
						consistenzaCollocazione.put(counter, record);
					} else if (campoCollocazione.equalsIgnoreCase(currSubf)){
						collocazione.put(counter, record);
					} else if (campoInventario.equalsIgnoreCase(currSubf)){
						currInv = new Inventario950(record, "B", new Date() );
						currInv.setCodInventario(record);
						tmpInventari.add(currInv);
					} else if (campoCatFruizione.equalsIgnoreCase(currSubf)){
						currInv.setCodFrui(record);
					} else if (campoDataIngresso.equalsIgnoreCase(currSubf)){
						currInv.setDataIngresso(getDataIngresso(record));
					}
				}

				prevSubf = currSubf;
			}
		}
	}

	private Date getDataIngresso(String data) throws ParseException {
		try {
			if (isFilled(data)) {
				//almaviva5_20130716
				data = data.trim();
				if (_YYYYMMDD.matcher(data).matches())
					return _DATE_FORMAT.parse(data);
			}
		} catch (ParseException e) {}

		return new Date();
	}

	private boolean smaller(String v1, String v2) {
		if (v1.equalsIgnoreCase(v2))
			return false;
		else {
			int w1 = campi950.get(v1.toLowerCase());
			int w2 = campi950.get(v2.toLowerCase());
			return (w1 < w2);
		}
	}

	public int getNumeroElementi() {
		return size(this.consistenzaEsemplare);
	}

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

	public String getBiblioteca() {
		return biblioteca;
	}

	private void setBiblioteca(String biblioteca) {
		this.biblioteca = biblioteca;
	}

	public Map<Integer, String> getConsistenzaEsemplare() {
		return consistenzaEsemplare;
	}

	public Map<Integer, String> getConsistenzaCollocazione() {
		return consistenzaCollocazione;
	}

	public Map<Integer, String> getCollocazione() {
		return collocazione;
	}

	public Map<Integer, List<Inventario950>> getInventario() {
		return inventario;
	}

	private boolean shiftColl(int idxColl){
		return (getCollocazione().get(idxColl).length() > 0) &&
				(" ".equals(getCollocazione().get(idxColl).substring(0, 1)))
					? false	//presente spazio prima del cod biblio : no shift
					: true;	//nessuno  spazio prima del cod biblio : shift
	}
	private boolean shiftInv(int idxColl, int idxInv){
		String codInventario = getInventario().get(idxColl).get(idxInv).getCodInventario();
		return (codInventario.length() > 0)	&&
				(" ".equals(codInventario.substring(0, 1)))
					? false	//presente spazio prima del cod biblio : no shift
					: true;	//nessuno  spazio prima del cod biblio : shift
	}

	// Dati esemplare
	public String getConsistenzaDocumento(int idxColl) {
		return (consistenzaEsemplare.get(idxColl).length() > 800)
			? consistenzaEsemplare.get(idxColl).substring(0, 800)	// troncamento 0-799
			: consistenzaEsemplare.get(idxColl);					// lunghezza corretta (<=800)
	}

	// Dati di collocazione
	public String getCodBibColl(int idxColl) {
		// pos 0-2
		if (shiftColl(idxColl))
			return (getCollocazione().get(idxColl).length() >= 2) ? " "
					+ getCollocazione().get(idxColl).substring(0, 2) : "";
		else
			return (getCollocazione().get(idxColl).length() >= 3) ? getCollocazione()
					.get(idxColl).substring(0, 3) : "";
	}

	public String getCodSez(int idxColl) {
		// pos 3-12
		String cdSez;
		int len = getCollocazione().get(idxColl).length();
		if (shiftColl(idxColl)) {
			cdSez = (len >= 12) ? getCollocazione().get(idxColl).substring(2, 12) : "";

		} else {
			cdSez = (len >= 13) ? getCollocazione().get(idxColl).substring(3, 13) : "";
		}

		return cdSez.trim();
	}


	public String getCodLoc(int idxColl) {
		// pos 13-36
		int len = getCollocazione().get(idxColl).length();
		if (shiftColl(idxColl))
			if (len >= 36)
				return getCollocazione().get(idxColl).substring(12, 36);
			else
				if (len > 12)
					return getCollocazione().get(idxColl).substring(12);
				else
					return "";
		else
			//return (len >= 37) ? getCollocazione().get(idxColl).substring(13, 37) : "";
			if (len >= 37)
				return getCollocazione().get(idxColl).substring(13, 37);
			else
				if (len > 13)
					return getCollocazione().get(idxColl).substring(13);
				else
					return "";
	}

	public String getSpecLoc(int idxColl) {
		// pos 37-fine
		int len = getCollocazione().get(idxColl).length();
		if (shiftColl(idxColl))
			return (len >= 37) ? getCollocazione().get(idxColl).substring(36) : "";
		else
			return (len >= 38) ? getCollocazione().get(idxColl).substring(37) : "";
	}

	// Dati di inventario
	public String getCodBiblioteca(int idxColl, int idxInv) {
		// pos 0-2
		String codInventario = getInventario().get(idxColl).get(idxInv).getCodInventario();
		if (shiftInv(idxColl, idxInv))
			return (codInventario.length() >= 2) ? " " + codInventario.substring(0, 2) : "";
		else
			return (codInventario.length() >= 3) ? codInventario.substring(0, 3) : "";
	}

	public String getCodSerie(int idxColl, int idxInv) {
		// pos 3-5
		String codInventario = getInventario().get(idxColl).get(idxInv).getCodInventario();
		if (shiftInv(idxColl, idxInv))
			return ((codInventario.length() >= 5) ? codInventario.substring(2, 5) : "").toUpperCase();
		else
			return ((codInventario.length() >= 6) ? codInventario.substring(3, 6) : "").toUpperCase();
	}

	public int getCodInventario(int idxColl, int idxInv) {
		// pos 6-14
		int codInv;
		String codInvStr;
		String codInventario = getInventario().get(idxColl).get(idxInv).getCodInventario();
		if (shiftInv(idxColl, idxInv))
			codInvStr = (codInventario.length() >= 14) ? codInventario.substring(5, 14).trim() : "0";
		else
			codInvStr = (codInventario.length() >= 15) ? codInventario.substring(6, 15).trim() : "0";
		try {
			codInv = Integer.parseInt(codInvStr);

		} catch (Exception e) {
			codInv = 0;
		}
		return codInv;
	}

	public String getCodSit(int idxColl, int idxInv) {
		// pos 15-15 (se 1->2 ; se 2->5)
		String codInventario = getInventario().get(idxColl).get(idxInv).getCodInventario();
		if (shiftInv(idxColl, idxInv))
			return (codInventario.length() >= 15) ? codInventario.substring(14,	15) : "";
		else
			return (codInventario.length() >= 16) ? codInventario.substring(15,	16) : "";
	}

	public boolean isCollocato(int idxColl, int idxInv) {
		// 2: precisato; 5: collocato
		return ("5".equals(getCodSit(idxColl, idxInv))) ? true : false;
	}

	public String getCodNoDisp(int idxColl, int idxInv) {
		// pos 16-17
		String codInventario = getInventario().get(idxColl).get(idxInv).getCodInventario();
		if (shiftInv(idxColl, idxInv))
			return (codInventario.length() >= 17) ? codInventario.substring(15,	17) : "";
		else
			return (codInventario.length() >= 18) ? codInventario.substring(16,	18) : "";
	}

	public String getCodMatInv(int idxColl, int idxInv) {
		// pos 18-19
		String codInventario = getInventario().get(idxColl).get(idxInv).getCodInventario();
		if (shiftInv(idxColl, idxInv))
			return (codInventario.length() >= 19) ? codInventario.substring(17,	19) : "";
		else
			return (codInventario.length() >= 20) ? codInventario.substring(18,	20) : "";
	}

	public String getStatoCon(int idxColl, int idxInv) {
		// pos 20-21
		String codInventario = getInventario().get(idxColl).get(idxInv).getCodInventario();
		if (shiftInv(idxColl, idxInv))
			return (codInventario.length() >= 21) ? codInventario.substring(19,	21) : "";
		else
			return (codInventario.length() >= 22) ? codInventario.substring(20,	22) : "";
	}

	public String getSequenzaCollocazione(int idxColl, int idxInv) {
		// pos 24-43
		String codInventario = getInventario().get(idxColl).get(idxInv).getCodInventario();
		// almaviva5_20140220
		if (shiftInv(idxColl, idxInv))
			return (codInventario.length() >= 25) ? substring(codInventario, 23, 43) : "";
		else
			return (codInventario.length() >= 26) ? substring(codInventario, 24, 44) : "";
	}

	public String getPrecisazioneInventario(int idxColl, int idxInv) {
		// pos 44-fine
		String codInventario = getInventario().get(idxColl).get(idxInv).getCodInventario();
		if (shiftInv(idxColl, idxInv))
			return (codInventario.length() >= 44) ? codInventario.substring(43)	: "";
		else
			return (codInventario.length() >= 45) ? codInventario.substring(44)	: "";
	}

	public Date getDataIngresso(int idxColl, int idxInv) {
		return inventario.get(idxColl).get(idxInv).getDataIngresso();
	}

	public String getCodFruizione(int idxColl, int idxInv) {
		return inventario.get(idxColl).get(idxInv).getCodFrui();
	}

	public static final Marc950 parse(final String bid, final String tag950) {
		if (!isFilled(tag950))
			return null;

		try {
			String $d = null;
			if (tag950.startsWith(" "))
				$d = "$d" + tag950;
			else
				$d = "$d " + tag950;
			Marc950 m950 = new Marc950(bid, $d);
			if (m950.getNumeroElementi() != 1)
				return null;

			return m950;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

//	h data ingresso
//	i data accessionamento
//	l note all'inventario

}
