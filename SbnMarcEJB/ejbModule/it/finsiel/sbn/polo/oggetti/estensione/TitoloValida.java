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
package it.finsiel.sbn.polo.oggetti.estensione;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.common.tavole.TableDao;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_improntaResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_numero_stdResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tb_titoloResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_autResult;
import it.finsiel.sbn.polo.dao.entity.tavole.Tr_tit_marResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.AnnoDateTitolo;
import it.finsiel.sbn.polo.factoring.base.ChiaviTitolo;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.transactionmaker.Factoring;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnData;
import it.finsiel.sbn.polo.factoring.util.SbnDatavar;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Impronta;
import it.finsiel.sbn.polo.oggetti.NumeroStd;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.polo.orm.Tb_impronta;
import it.finsiel.sbn.polo.orm.Tb_numero_std;
import it.finsiel.sbn.polo.orm.Tb_titolo;
import it.finsiel.sbn.polo.orm.Tr_tit_aut;
import it.finsiel.sbn.polo.orm.Tr_tit_mar;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_mat;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_imp;
import it.finsiel.sbn.polo.orm.viste.Vl_titolo_num_std;
import it.iccu.sbn.ejb.model.unimarcmodel.AnticoType;
import it.iccu.sbn.ejb.model.unimarcmodel.AudiovisivoType;
import it.iccu.sbn.ejb.model.unimarcmodel.C012;
import it.iccu.sbn.ejb.model.unimarcmodel.C100;
import it.iccu.sbn.ejb.model.unimarcmodel.C101;
import it.iccu.sbn.ejb.model.unimarcmodel.C102;
import it.iccu.sbn.ejb.model.unimarcmodel.C105bis;
import it.iccu.sbn.ejb.model.unimarcmodel.C115;
import it.iccu.sbn.ejb.model.unimarcmodel.C116;
import it.iccu.sbn.ejb.model.unimarcmodel.C120;
import it.iccu.sbn.ejb.model.unimarcmodel.C121;
import it.iccu.sbn.ejb.model.unimarcmodel.C123;
import it.iccu.sbn.ejb.model.unimarcmodel.C124;
import it.iccu.sbn.ejb.model.unimarcmodel.C125;
import it.iccu.sbn.ejb.model.unimarcmodel.C126;
import it.iccu.sbn.ejb.model.unimarcmodel.C127;
import it.iccu.sbn.ejb.model.unimarcmodel.C128;
import it.iccu.sbn.ejb.model.unimarcmodel.C135;
import it.iccu.sbn.ejb.model.unimarcmodel.C140bis;
import it.iccu.sbn.ejb.model.unimarcmodel.C181;
import it.iccu.sbn.ejb.model.unimarcmodel.C182;
import it.iccu.sbn.ejb.model.unimarcmodel.C183;
import it.iccu.sbn.ejb.model.unimarcmodel.C200;
import it.iccu.sbn.ejb.model.unimarcmodel.C922;
import it.iccu.sbn.ejb.model.unimarcmodel.C923;
import it.iccu.sbn.ejb.model.unimarcmodel.C926;
import it.iccu.sbn.ejb.model.unimarcmodel.C927;
import it.iccu.sbn.ejb.model.unimarcmodel.CartograficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiDocType;
import it.iccu.sbn.ejb.model.unimarcmodel.DocumentoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElementAutType;
import it.iccu.sbn.ejb.model.unimarcmodel.ElettronicoType;
import it.iccu.sbn.ejb.model.unimarcmodel.GraficoType;
import it.iccu.sbn.ejb.model.unimarcmodel.ModernoType;
import it.iccu.sbn.ejb.model.unimarcmodel.MusicaType;
import it.iccu.sbn.ejb.model.unimarcmodel.NumStdType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.Indicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.LivelloBibliografico;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnNaturaDocumento;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnSimile;
import it.iccu.sbn.ejb.model.unimarcmodel.types.StatoRecord;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoRecord;
import it.iccu.sbn.ejb.model.unimarcmodel.types.TipoSeriale;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;


/**
 *
 * Esegue i vari controlli per la creazione o la modifica di un titolo
 * e anche dei vari legami che vengono coinvolti.
 *
 * @author
 * @version febbraio 2003
 */

/**
 Versione 1.65 di TitoloValida Esercizio_sviluppo 24.02.2015
 Versione 1.67 di TitoloValida Esercizio_sviluppo 27.02.2015
 Versione 1.68 di TitoloValida Esercizio_sviluppo 05.03.2015
 Versione 1.69 di TitoloValida Esercizio_sviluppo 13.03.2015
*/


public class TitoloValida extends Titolo {

	private static final long serialVersionUID = -7862074844288140799L;

	protected SbnSimile tipoControllo = null;
    protected String diagnostico = null;
    protected List elencoDiagnostico = null;
    protected DocumentoType documentoType = null;
    protected ElementAutType elementAutType = null;
    protected DatiDocType datiDoc = null;
    protected C200 c200 = null;
    //protected C012 c012 = null;
    protected String t005 = null;
    String id = null;
    protected StatoRecord statoRecord = null;
    protected boolean scheduled = false;

	protected static ValidatorProfiler validator = ValidatorProfiler.getInstance();
	static String SIGLA_INDICE = validator.getCodicePolo();


    //static String SIGLA_INDICE = ResourceLoader.getPropertyString("SIGLA_INDICE");

	// Sequenze univoche per area0.
	// Dopo l'underscore i tipi record per i quali la combinazione vale

	String area0_sorted_unique_permutations[] = { // 19/02/2015
			"a   e", // dataset, na, na, na, visivo
			"b a2e", // immagine, na, in movimento, bidimensionale, visivo
			"b a3e", // immagine, na, in movimento, tridimensionale, visivo
			"b b2e", // immagine, na, fissa, bidimensionale, visivo
			"b b3e", // immagine, na, fissa, tridimensionale, visivo
			"bca2d", // immagine, cartografica, in movimento, bidimensionale, tattile
			"bca2e", // immagine, cartografica, in movimento, bidimensionale, visivo
			"bca3d", // immagine, cartografica, in movimento, tridimensionale, tattile
			"bca3e", // immagine, cartografica, in movimento, tridimensionale, visivo
			"bcb2d", // immagine, cartografica, fissa, bidimensionale, tattile
			"bcb2e", // immagine, cartografica, fissa, bidimensionale, visivo
			"bcb3d", // immagine, cartografica, fissa, tridimensionale, tattile
			"bcb3e", // immagine, cartografica, fissa, tridimensionale, visivo
			"ca  e", // movimento, notato, na, na, visivo
			"da  d", // musica, notato, na, na, tattile
			"da  e", // musica, notato, na, na, visivo
			"db  a", // musica, eseguito, na, na, uditivo
			"e   a", // oggetto, na, na, na, uditivo
			"e   b", // oggetto, na, na, na, gustativo
			"e   c", // oggetto, na, na, na, olfattivo
			"e   d", // oggetto, na, na, na, tattile
			"e   e", // oggetto, na, na, na, visivo
			"ec  a", // oggetto, cartografico, na, na, uditivo
			"ec  b", // oggetto, cartografico, na, na, gustativo
			"ec  c", // oggetto, cartografico, na, na, olfattivo
			"ec  d", // oggetto, cartografico, na, na, tattile
			"ec  e", // oggetto, cartografico, na, na, visivo
			"f   e", // programmi, na, na, na, visivo
			"g   a", // suoni, na, na, na, uditivo
			"h   a", // parlato, na, na, na, uditivo
			"i   c", // testo, na, na, na, olfattivo
			"i   d", // testo, na, na, na, tattile
			"i   e", // testo, na, na, na, visivo
			"m   a", // forme di contenuto multiple, na, na, na, uditivo
			"m   b", // forme di contenuto multiple, na, na, na, gustativo
			"m   c", // forme di contenuto multiple, na, na, na, olfattivo
			"m   d", // forme di contenuto multiple, na, na, na, tattile
			"m   e", // forme di contenuto multiple, na, na, na, visivo
	}; // End area0_sorted_unique_permutations



	/*
	 La stringa e' composta da $
		0. Tipo di video
		1. Lunghezza
		2. Lunghezza
		3. Lunghezza
		4. Indicatore di colore
		5. Indicatore di suono
		6. Supporto del suono
		7. Larghezza o Dimensioni (per diapositive, lucidi e videotape)
		8. Forma di pubblicazione/distribuzione (per film e videoproiezioni)
		9. Tecnica di videoregistrazioni e film
		10. Forma di presentazione di immagini in movimento
		11. Materiale di accompagnamento
		12. Materiale di accompagnamento
		13. Materiale di accompagnamento
		14. Materiale di accompagnamento
		15. Forma di pubblicazione di videoregistrazioni
		16. Forma di presentazione di videoregistrazioni
		17. Materiale di emulsione di base (per videoproiezioni)
		18. Materiale di supporto secondario
		19. Standard di trasmissione
	*/
String seqBase115   = ".---.......----.....";
String audiovisivi_115_permutations[] = {
		"a---??..?..----.....",
		"a---??..?.?----.....",
		"a---??..??.----.....",
		"a---??..???----.....",
		"a---??.a?..----.....",
		"a---??.a?.?----.....",
		"a---??.a??.----.....",
		"a---??.a???----.....",
		"a---??.b?..----.....",
		"a---??.b?.?----.....",
		"a---??.b??.----.....",
		"a---??.b???----.....",
		"a---??.c?..----.....",
		"a---??.c?.?----.....",
		"a---??.c??.----.....",
		"a---??.c???----.....",
		"a---??.d?..----.....",
		"a---??.d?.?----.....",
		"a---??.d??.----.....",
		"a---??.d???----.....",
		"a---??.e?..----.....",
		"a---??.e?.?----.....",
		"a---??.e??.----.....",
		"a---??.e???----.....",
		"a---??.f?..----.....",
		"a---??.f?.?----.....",
		"a---??.f??.----.....",
		"a---??.f???----.....",
		"a---??.g?..----.....",
		"a---??.g?.?----.....",
		"a---??.g??.----.....",
		"a---??.g???----.....",
		"a---??.z?..----.....",
		"a---??.z?.?----.....",
		"a---??.z??.----.....",
		"a---??.z???----.....",
		"b---??..?..----.....",
		"b---??..?..----...?.",
		"b---??..?..----..?..",
		"b---??..?..----..??.",
		"b---??.k?..----.....",
		"b---??.k?..----...?.",
		"b---??.k?..----..?..",
		"b---??.k?..----..??.",
		"b---??.l?..----.....",
		"b---??.l?..----...?.",
		"b---??.l?..----..?..",
		"b---??.l?..----..??.",
		"b---??.r?..----.....",
		"b---??.r?..----...?.",
		"b---??.r?..----..?..",
		"b---??.r?..----..??.",
		"b---??.s?..----.....",
		"b---??.s?..----...?.",
		"b---??.s?..----..?..",
		"b---??.s?..----..??.",
		"b---??.t?..----.....",
		"b---??.t?..----...?.",
		"b---??.t?..----..?..",
		"b---??.t?..----..??.",
		"b---??.u?..----.....",
		"b---??.u?..----...?.",
		"b---??.u?..----..?..",
		"b---??.u?..----..??.",
		"b---??.v?..----.....",
		"b---??.v?..----...?.",
		"b---??.v?..----..?..",
		"b---??.v?..----..??.",
		"b---??.w?..----.....",
		"b---??.w?..----...?.",
		"b---??.w?..----..?..",
		"b---??.w?..----..??.",
		"b---??.x?..----.....",
		"b---??.x?..----...?.",
		"b---??.x?..----..?..",
		"b---??.x?..----..??.",
		"b---??.z?..----.....",
		"b---??.z?..----...?.",
		"b---??.z?..----..?..",
		"b---??.z?..----..??.",
		"b---???.?..----.....",
		"b---???.?..----...?.",
		"b---???.?..----..?..",
		"b---???.?..----..??.",
		"b---???k?..----.....",
		"b---???k?..----...?.",
		"b---???k?..----..?..",
		"b---???k?..----..??.",
		"b---???l?..----.....",
		"b---???l?..----...?.",
		"b---???l?..----..?..",
		"b---???l?..----..??.",
		"b---???r?..----.....",
		"b---???r?..----...?.",
		"b---???r?..----..?..",
		"b---???r?..----..??.",
		"b---???s?..----.....",
		"b---???s?..----...?.",
		"b---???s?..----..?..",
		"b---???s?..----..??.",
		"b---???t?..----.....",
		"b---???t?..----...?.",
		"b---???t?..----..?..",
		"b---???t?..----..??.",
		"b---???u?..----.....",
		"b---???u?..----...?.",
		"b---???u?..----..?..",
		"b---???u?..----..??.",
		"b---???v?..----.....",
		"b---???v?..----...?.",
		"b---???v?..----..?..",
		"b---???v?..----..??.",
		"b---???w?..----.....",
		"b---???w?..----...?.",
		"b---???w?..----..?..",
		"b---???w?..----..??.",
		"b---???x?..----.....",
		"b---???x?..----...?.",
		"b---???x?..----..?..",
		"b---???x?..----..??.",
		"b---???z?..----.....",
		"b---???z?..----...?.",
		"b---???z?..----..?..",
		"b---???z?..----..??.",
		"c---??.....----??...",
		"c---??.....----??..?",
		"c---??...?.----??...",
		"c---??...?.----??..?",
		"c---??.m...----??...",
		"c---??.m...----??..?",
		"c---??.m.?.----??...",
		"c---??.m.?.----??..?",
		"c---??.n...----??...",
		"c---??.n...----??..?",
		"c---??.n.?.----??...",
		"c---??.n.?.----??..?",
		"c---??.o...----??...",
		"c---??.o...----??..?",
		"c---??.o.?.----??...",
		"c---??.o.?.----??..?",
		"c---??.p...----??...",
		"c---??.p...----??..?",
		"c---??.p.?.----??...",
		"c---??.p.?.----??..?",
		"c---??.q...----??...",
		"c---??.q...----??..?",
		"c---??.q.?.----??...",
		"c---??.q.?.----??..?",
		"c---??.z...----??...",
		"c---??.z...----??..?",
		"c---??.z.?.----??...",
		"c---??.z.?.----??..?",
	}; // audiovisivi_115_permutations

//Ordinati con sort e LC_COLLATE=C
String seqBase126   = "**---..--------";
String audiovisivi_126_permutations [] = {
		"?u---..--------",
		"?x---..--------",
		"?z---..--------",
		"aa---..--------",
		"ab---..--------",
		"ac---..--------",
		"ad---..--------",
		"ae---..--------",
		"ag---..--------",
		"b8---..--------",
		"b8---.?--------",
		"b8---?.--------",
		"b8---??--------",
		"bk---..--------",
		"bk---.?--------",
		"bk---?.--------",
		"bk---??--------",
		"bl---..--------",
		"bl---.?--------",
		"bl---?.--------",
		"bl---??--------",
		"bm---..--------",
		"bm---.?--------",
		"bm---?.--------",
		"bm---??--------",
		"bn---..--------",
		"bn---.?--------",
		"bn---?.--------",
		"bn---??--------",
		"bo---..--------",
		"bo---.?--------",
		"bo---?.--------",
		"bo---??--------",
		"bp---..--------",
		"bp---.?--------",
		"bp---?.--------",
		"bp---??--------",
		"bq---..--------",
		"bq---.?--------",
		"bq---?.--------",
		"bq---??--------",
		"br---..--------",
		"br---.?--------",
		"br---?.--------",
		"br---??--------",
		"c8---..--------",
		"c8---.?--------",
		"c8---?.--------",
		"c8---??--------",
		"ck---..--------",
		"ck---.?--------",
		"ck---?.--------",
		"ck---??--------",
		"cl---..--------",
		"cl---.?--------",
		"cl---?.--------",
		"cl---??--------",
		"cm---..--------",
		"cm---.?--------",
		"cm---?.--------",
		"cm---??--------",
		"cn---..--------",
		"cn---.?--------",
		"cn---?.--------",
		"cn---??--------",
		"co---..--------",
		"co---.?--------",
		"co---?.--------",
		"co---??--------",
		"cp---..--------",
		"cp---.?--------",
		"cp---?.--------",
		"cp---??--------",
		"cq---..--------",
		"cq---.?--------",
		"cq---?.--------",
		"cq---??--------",
		"cr---..--------",
		"cr---.?--------",
		"cr---?.--------",
		"cr---??--------",
		"d8---..--------",
		"d8---.?--------",
		"d8---?.--------",
		"d8---??--------",
		"dk---..--------",
		"dk---.?--------",
		"dk---?.--------",
		"dk---??--------",
		"dl---..--------",
		"dl---.?--------",
		"dl---?.--------",
		"dl---??--------",
		"dm---..--------",
		"dm---.?--------",
		"dm---?.--------",
		"dm---??--------",
		"dn---..--------",
		"dn---.?--------",
		"dn---?.--------",
		"dn---??--------",
		"do---..--------",
		"do---.?--------",
		"do---?.--------",
		"do---??--------",
		"dp---..--------",
		"dp---.?--------",
		"dp---?.--------",
		"dp---??--------",
		"dq---..--------",
		"dq---.?--------",
		"dq---?.--------",
		"dq---??--------",
		"dr---..--------",
		"dr---.?--------",
		"dr---?.--------",
		"dr---??--------",
		"fh---..--------",
		"fi---..--------",
} ; // audiovisivi_126_permutations



    public static int binarySearch(String[] words, String value) {
        return binarySearch(words, value, 0, words.length - 1);
    }

    /**
     * Searches an array of words for a given value using a recursive binary
     * search.  Returns the index that contains the value or -1 if the value
     * is not found.
     * @param words
     * @param value
     * @return
     */
    public static int binarySearch(String[] words, String value, int min, int max) {
        if (min > max) {
            return -1;
        }

        int mid = (max + min) / 2;

        if (words[mid].equals(value)) {
            return mid;
        } else if(words[mid].compareTo(value) > 0) {
            return binarySearch(words, value, min, mid - 1);
        } else {
            return binarySearch(words, value, mid + 1, max);
        }
    }


	private int binarySearch(String[] words, String value, int keyLength) {
	    return binarySearch(words, value, 0, words.length - 1, keyLength);
	}
	private int binarySearch(String[] words, String value, int min, int max, int keyLength) {
	    if (min > max) {
	        return -1;
	    }

	    int mid = (max + min) / 2;

	    String seq = words[mid].substring(0, keyLength);
	    if (seq.equals(value)) {
	        return mid;
	    } else if(words[mid].compareTo(value) > 0) {
	        return binarySearch(words, value, min, mid - 1, keyLength);
	    } else {
	        return binarySearch(words, value, mid + 1, max, keyLength);
	    }
	}


    public TitoloValida() {
        super();
    }

    public void setScheduled (boolean valore) {
        scheduled = valore;
    }

    /**
     * Verifica se un titolo che si intende modificare esiste tramite ricerca per ID
     * @return Nel caso esista restituisce il relativo oggetto Tb_titolo.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public Tb_titolo verificaEsistenzaID(String id) throws IllegalArgumentException, InvocationTargetException, Exception {

        Tb_titolo tb_titolo = new Tb_titolo();
        tb_titolo.setBID(id);
        Tb_titoloResult tabella = new Tb_titoloResult(tb_titolo);


        tabella.executeCustom("selectEsistenzaId");
        List lista = tabella.getElencoRisultati();

        tb_titolo = null;
        if (lista.size() != 0)
            tb_titolo = (Tb_titolo) lista.get(0);
        //se tb_titolo è stato trovato significa che non posso crearlo nuovamente
        return tb_titolo;
    }

    private List cercaTitoliSimiliPerNumeroStd(String bid, String clet) throws IllegalArgumentException, InvocationTargetException, Exception {
        List ret = new ArrayList();
        if (datiDoc != null && datiDoc.getNumSTDCount() > 0) {
            NumStdType[] num = datiDoc.getNumSTD();
            for (int i = 0; i < num.length; i++) {
                String tipo = num[i].getTipoSTD().toString();
                // Ottobre 2014 almaviva2 - Eliminazione ENUMERATE su protocollo per il Numero Standard ed eliminazione classe SbnTipoSTD
                if (tipo.equals(("010"))
                    || tipo.equals(("011"))
                    || tipo.equals(("013"))) {
                    String aa_pubb1 = null;
                    if (tipo.equals(("010")))
                        aa_pubb1 = "BO";
                    ret.addAll(
                        new NumeroStd().cercaTitoliPerNumStd(
                            num[i].getNumeroSTD(),
                            tipo,
                            num[i].getPaeseSTD(),
                            num[i].getNotaSTD(),
                            bid,
                            aa_pubb1));
                }
            }
        }
        return ret;
    }
    /** Esegue la ricerca per nome del titolo con confronto di tipo esatto
     * Se è una ricerca per modifica si setta il bid, altrimenti va lasciato null
     * tipoModifica: 0 -> crezione
     * 1 -> modificate solo le chiavi
     * 2 -> modificato solo il resto (numero std o impronta)
     * 3 -> modificati entrambi
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * */
    protected List cercaTitoliSimili(Tb_titolo titolo, String cd_natura, int tipoModifica)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        //settaParametro(TableDao.XXXstringaEsatta", nome);
        String bid = null;
        ChiaviTitolo chiavi = new ChiaviTitolo();
        if (titolo != null) {
            bid = titolo.getBID();
        }
        if (titolo != null && cd_natura.equals("W")) {
            chiavi.estraiChiavi(titolo);
        } else {
            chiavi.estraiChiavi(c200);
        }
        List v;
        //Se è antico cambio i-j e v-u
        if (datiDoc != null && datiDoc instanceof AnticoType) {
            v = verificaAntichi(bid, cd_natura, chiavi, tipoModifica);
        } else {
            v = cercaTitoliSimiliNonAntico(bid, cd_natura, chiavi, tipoModifica);
        }
        return v;
    }

    protected List cercaTitoliSimiliNonAntico(
        String bid,
        String cd_natura,
        ChiaviTitolo chiavi,
        int tipoModifica)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        List v;
        if (tipoModifica == 1) {
            //Modifica delle sole chiavi
            v = cercaTitoliSimiliPerChiavi(bid, cd_natura, chiavi);
        } else if (tipoModifica == 2) {
            //Modifica del numero std.
            v = cercaTitoliSimiliPerNumeroStd(bid, (chiavi.getKy_cles1_t() + chiavi.getKy_cles2_t()).trim());
        } else {
            //Creazione o modifica totale.
            v = cercaTitoliSimiliPerChiavi(bid, cd_natura, chiavi);
            v.addAll(
                cercaTitoliSimiliPerNumeroStd(bid, (chiavi.getKy_cles1_t() + chiavi.getKy_cles2_t()).trim()));
        }
        return v;
    }



    protected List cercaTitoliSimiliPerChiavi(String bid, String cd_natura, ChiaviTitolo chiavi)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        List v;
        Tb_titoloResult tavola = new Tb_titoloResult(this);


        setKY_CLES1_T(chiavi.getKy_cles1_t());
        setKY_CLES2_T(chiavi.getKy_cles2_t());
        setKY_CLET1_T(chiavi.getKy_clet1_t());
        setKY_CLET2_T(chiavi.getKy_clet2_t());
        if (cd_natura.equals("C") || cd_natura.equals("S"))
            settaParametro(TableDao.XXXnaturaSoC, "CS");
        else if (cd_natura.equals("M")) //|| cd_natura.equals("W"))
            settaParametro(TableDao.XXXnaturaMoW, "MW");
        else if (cd_natura.equals("W"))
            setCD_NATURA("W");
        else if (cd_natura.equals("B"))
            settaParametro(TableDao.XXXnaturaBoA, "BA");
        else
            setCD_NATURA(cd_natura);
        if (datiDoc != null) {
            if (datiDoc.getT100() != null && datiDoc.getT100().getA_100_8() != null)
                if (!datiDoc.getT100().getA_100_8().equals("F"))
                    settaParametro(TableDao.XXXaa_pubb_1_s, datiDoc.getT100().getA_100_9());
            if (datiDoc.getT102() != null && datiDoc.getT102().getA_102() != null)
                setCD_PAESE(
                    Decodificatore.getCd_tabella(
                        "Tb_titolo",
                        "cd_paese",
                        datiDoc.getT102().getA_102().toUpperCase()));
            if (datiDoc.getT101() != null && datiDoc.getT101().getA_101Count() > 0)
                setCD_LINGUA_1(
                    Decodificatore.getCd_tabella(
                        "Tb_titolo",
                        "cd_lingua_1",
                        datiDoc.getT101().getA_101(0).toUpperCase()));
         // Inizio Dicembre 2015 almaviva2 : Ricerca collane con filtro Editore (reperito sul canpo Area di pubblicazione)
         // e opportunamente salvato sulla tb_titolo campo ky_editore
            if (datiDoc.getNaturaDoc() != null){
				if (datiDoc.getNaturaDoc().toString().equals("C")) {
		                //setKY_EDITORE(new ChiaviTitolo().estraiChiaveEditoreCollana(datiDoc, db_conn, true));
					setKY_EDITORE(new ChiaviTitolo().estraiChiaveEditoreCollana(datiDoc, true));
		        }
			}
         // Fine Dicembre 2015 almaviva2 : Ricerca collane con filtro Editore
        }
        tavola.executeCustom("selectSimili");
        v = tavola.getElencoRisultati();

        return v;
    }
    /** Metodo non più utilizzato:
     * vedi mail di Daniela del 12 Marzo 2004
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected List cercaTitoliSimiliConferma(String ordinamento, String bid, String cd_natura)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        if (datiDoc != null) {
            //Prima si cerca per impronta
            C012[] t012 = null;
            if (datiDoc instanceof AnticoType)
                t012 = ((AnticoType) datiDoc).getT012();
            if (t012 != null && t012.length > 0) {
                Impronta impronta = new Impronta();
                List v = new ArrayList();
                for (int i = 0; i < t012.length; i++)
                    v.add(t012[i].getA_012_2());
                v = impronta.cercaPerSecondaImpronta(v, bid, null);
                if (v.size() > 0)
                    return v;
            }
        }
        List ret = new ArrayList();
        ChiaviTitolo chiavi = new ChiaviTitolo();
        chiavi.estraiChiavi(c200);
        List v =
            cercaTitoliSimiliPerNumeroStd(bid, (chiavi.getKy_cles1_t() + chiavi.getKy_cles2_t()).trim());
        String aa_pubb = null;
        if (datiDoc != null && datiDoc.getT100() != null && datiDoc.getT100().getA_100_9() != null)
            aa_pubb = datiDoc.getT100().getA_100_9();
        for (int i = 0; i < v.size(); i++) {
            Vl_titolo_num_std tit = (Vl_titolo_num_std) v.get(i);
            if (tit.getTP_NUMERO_STD().trim().equals("J"))
                ret.add(tit);
            else if (
                tit.getTP_NUMERO_STD().trim().equals("I")
                    && (aa_pubb == null || aa_pubb.equals(tit.getAA_PUBB_1())))
//                && (aa_pubb == null || aa_pubb.equals(tit.getAA_PUBB_1().getAnnoDate())))
                ret.add(tit);

        }
        return ret;
    }

    /**
     * Verifica la presenza di titoli antichi simili
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected List verificaAntichi(String bid, String cd_natura, ChiaviTitolo chiavi, int tipoModifica)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        List v;
        String cles1 = chiavi.getKy_cles1_t();
        String cles2 = chiavi.getKy_cles2_t();
        if (tipoModifica == 1) {
            //Modifica Chiavi
            v = verificaAntichiChiave(bid, cd_natura, cles1, cles2);
        } else if (tipoModifica == 2) {
            //Modifica impronta
            v = verificaAntichiImpronta(bid, cd_natura, cles1, cles2);
        } else {
            //Creazione o modifica totale
            v = verificaAntichiImpronta(bid, cd_natura, cles1, cles2);
            v.addAll(verificaAntichiChiave(bid, cd_natura, cles1, cles2));
        }
        return v;
    }

    protected List verificaAntichiImpronta(String bid, String cd_natura, String cles1, String cles2)
        throws IllegalArgumentException, InvocationTargetException, Exception {
        List v = new ArrayList();
        if (datiDoc != null) {
            //Prima si cerca per impronta
            C012[] t012 = null;
            if (datiDoc instanceof AnticoType)
                t012 = ((AnticoType) datiDoc).getT012();
            if (t012 != null && t012.length > 0) {
                Impronta impronta = new Impronta();
                for (int i = 0; i < t012.length; i++)
                    v.add(t012[i].getA_012_2());
                v = impronta.cercaPerSecondaImpronta(v, bid, null);
                for (int i = 0; i < v.size(); i++) {
                    Vl_titolo_imp tit = (Vl_titolo_imp) v.get(i);
                    //Ce ne deve essere almeno una con la data uguale o il carattere Q.
                    boolean simile = false;
                    for (int c = 0; c < t012.length; c++) {
                        if (tit.getIMPRONTA_3().substring(0, 4).equals(t012[c].getA_012_3().substring(0, 4))
                            || ( tit.getIMPRONTA_3().charAt(6) == 'Q' && t012[c].getA_012_3().charAt(6) == 'Q')) {
                            simile = true;
                            break;
                        }
                    }
                    if (!simile) {
                        v.remove(i);
                        i--;
                    }
                }
            }
        }
        return v;
    }

    /**
     * Verifica la presenza di titoli con cles analogo:
     * provando con le cinque possibilità: uguale alla stringa oppure
     * tutte i e j come i e come j, tutte le v e u in u o v.
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    protected List verificaAntichiChiave(String bid, String cd_natura, String cles1, String cles2)
        throws IllegalArgumentException, InvocationTargetException, Exception {

        Tb_titoloResult tavola = new Tb_titoloResult(this);

        String cles = cles1;
        //BAsta la cles1
        if (cles2 != null)
            cles += cles2;
        cles = cles.toUpperCase();

        List allCles = new ArrayList();
        allCles.add(cles);
        boolean contieneI = cles.indexOf("I") >= 0;
        boolean contieneJ = cles.indexOf("J") >= 0;
        boolean contieneV = cles.indexOf("V") >= 0;
        boolean contieneU = cles.indexOf("U") >= 0;

        //NON è ottimale. per ora è un buon compromesso.
        if (contieneJ || contieneV) {
            cles = cles.replace('J', 'I');
            cles = cles.replace('V', 'U');
            allCles.add(cles);
        }
        if (contieneI || (contieneJ && contieneU)) {
            cles = cles.replace('I', 'J');
            allCles.add(cles);
        }
        if (contieneU || (contieneV && contieneI)) {
            cles = cles.replace('U', 'V');
            allCles.add(cles);
        }
        if ((contieneV && ((contieneJ && contieneU) || (contieneI && contieneJ)))
            || (contieneI && contieneU)) {
            cles = cles.replace('J', 'I');
            allCles.add(cles);
        }

        for (int j = 0; j < allCles.size(); j++) {
            String t = (String) allCles.get(j);
            if (t.length() > 6) {
                settaParametro("XXXcles1_" + j, t.substring(0, 6));
                settaParametro("XXXcles2_" + j, t.substring(6));
            } else {
                settaParametro("XXXcles1_" + j, t);
            }
        }
        if (cd_natura.equals("C") || cd_natura.equals("S"))
            settaParametro(TableDao.XXXnaturaSoC, "CS");
        else if (cd_natura.equals("M") || cd_natura.equals("W"))
            settaParametro(TableDao.XXXnaturaMoW, "MW");
        else if (cd_natura.equals("B"))
            settaParametro(TableDao.XXXnaturaBoA, "BA");
        else
            setCD_NATURA(cd_natura);
        if (datiDoc != null) {
            if (datiDoc.getT100() != null && datiDoc.getT100().getA_100_8() != null)
                if (!datiDoc.getT100().getA_100_8().equals("F"))
                    settaParametro(TableDao.XXXaa_pubb_1_s, datiDoc.getT100().getA_100_9());
            if (datiDoc.getT102() != null && datiDoc.getT102().getA_102() != null)
                setCD_PAESE(
                    Decodificatore.getCd_tabella(
                        "Tb_titolo",
                        "cd_paese",
                        datiDoc.getT102().getA_102().toUpperCase()));
            if (datiDoc.getT101() != null && datiDoc.getT101().getA_101Count() > 0)
                setCD_LINGUA_1(
                    Decodificatore.getCd_tabella(
                        "Tb_titolo",
                        "cd_lingua_1",
                        datiDoc.getT101().getA_101(0).toUpperCase()));
        }


        tavola.executeCustom("selectSimiliAntico");
        List res = tavola.getElencoRisultati();

        return res;
    }
    /**
     * SOSTTUITO DAL Metodo precedente (perchè è troppo oneroso)
     * Verifica la presenza di titoli con cles analogo: provando tutte le possibilità che derivano
     * dalla reciproca sostituzione di I con J e di U con V.
     * @param ordinamento
     * @return
     * /
    protected TableDao verificaAntichi(String cles1, String cles2, String ordinamento) throws EccezioneDB {
        Tb_titoloResult tavola = new Tb_titoloResult(this);

        String cles = cles1;
        //BAsta la cles1
        if (cles2 != null)
            cles += cles2;
        cles = cles.toUpperCase();
        cles = cles.replace('J', 'I');
        cles = cles.replace('V', 'U');
        TableDao v = new TableDao();
        TableDao res = null;
        char c;
        for (int i = 0; i < cles.length(); i++) {
            c = cles.charAt(i);
            if (c == 'I' || c == 'U')
                v.add(new Integer(i));
        }
        int lett;
        int max = (int) Math.pow(2, v.size());
        TableDao allCles = new TableDao();
        int i;
        for (i = 0; i < max; i++) {
            //utilizzo un sistema di conteggio binario.
            char[] ch = Integer.toBinaryString(i).toCharArray();
            String tmp = cles;
            for (int n = 0; n < ch.length; n++) {
                //Dove c'e' '1' devo sostituire il reciproco.
                if (ch[n] == '1') {
                    lett = ((Integer) v.get(ch.length - n - 1)).intValue();
                    if (tmp.charAt(lett) == 'I')
                        tmp = tmp.substring(0, lett) + 'J' + tmp.substring(lett + 1);
                    else
                        tmp = tmp.substring(0, lett) + 'V' + tmp.substring(lett + 1);
                }
            }
            allCles.add(tmp);
            if ((i + 1) % 1000 == 0) {
                for (int j = 0; j < allCles.size(); j++)
                    settaParametro(TableDao.XXXcles_" + j, (String) allCles.get(j));

                tavola.executeCustom("selectSimiliAntico", ordinamento);
                res = tavola.getElencoRisultati();

                if (res.size() > 0)
                    return res;
                allCles.clear();
                azzeraParametri();
            }
        }
        if (i % 1000 != 0) {
            for (int j = 0; j < allCles.size(); j++)
                settaParametro(TableDao.XXXcles_" + j, (String) allCles.get(j));

            tavola.executeCustom("selectSimiliAntico", ordinamento);
            res = tavola.getElencoRisultati();

        }
        return res;
    }

    /**
     * metodo che controlla il vincolo di versione in modifica:
     * T005 deve essere uguale a ts_var
     * NB: questo metodo è comune, può essere spostato in Factoring
     */
    public boolean verificaVersioneTitolo(Tb_titolo tb_titolo) throws EccezioneSbnDiagnostico {
        if (t005 != null){
            SbnDatavar data = new SbnDatavar(tb_titolo.getTS_VAR());
            return data.getT005Date().equals(t005);
        }
        throw new EccezioneSbnDiagnostico(3017, "Manca l'informazione sul ts_var");
    }

    /**
     * Method verificaBid.
     * Verifica se il polo presente nei primi 3 caratteri del BID, esiste.
     * La lunghezza deve essere di 10 caratteri
     * @param bid
     * @return boolean
     */
    public boolean verificaBid(String bid, String user) {
        boolean b = false;
        if (bid.length() != 10) {
            return false;
        }
        if (!bid.startsWith(SIGLA_INDICE) && ! bid.startsWith("MUS") && !bid.startsWith("MSM") && !bid.startsWith("CMP")) {
          // if (user != null && !user.substring(0,3).equals(bid.substring(0, 3)))
          // return false;
//        	 LA CHAIMATA AL CONTROLLO E' STATO TOLTO SU INDICAZIONE DI almaviva2 12/12/2007
//            if (Decodificatore.getCd_tabella("POLO", bid.substring(0, 3)) == null)
//                return false;
        }
        char[] c_bid = bid.toCharArray();
        if ((Character.isDigit(c_bid[3]) || c_bid[3] == 'E')) {
            boolean c = true;
            for (int i = 4; i < c_bid.length && c; i++) {
                c = Character.isDigit(c_bid[i]);
            }
            b = c;
        }
        return b;
    }

    public String estraiNatura(DatiDocType datiDoc) throws EccezioneSbnDiagnostico {
        if (datiDoc.getNaturaDoc() != null) {
            return datiDoc.getNaturaDoc().toString();
        }
        if (datiDoc.getGuida() == null)
            throw new EccezioneSbnDiagnostico(3060, "Guida non definita");
        if (datiDoc.getGuida().getLivelloBibliografico() == null)
            throw new EccezioneSbnDiagnostico(3192, "Livello bibliografico non definito");
        if (datiDoc.getGuida().getLivelloBibliografico().getType() == LivelloBibliografico.M_TYPE) {
            if (datiDoc.getT200() == null)
                throw new EccezioneSbnDiagnostico(3193, "Tag 200 non definito");
            if (datiDoc.getT200().getId1().getType() == Indicatore.VALUE_1.getType())
                return "W";
            else if (datiDoc.getT200().getId1().getType() == Indicatore.VALUE_2.getType())
                return "M";
        }
        if (datiDoc.getGuida().getLivelloBibliografico().getType() == LivelloBibliografico.A_TYPE)
            return "N";
        if (datiDoc.getGuida().getLivelloBibliografico().getType() == LivelloBibliografico.S_TYPE) {
            if (datiDoc.getT110() == null)
                throw new EccezioneSbnDiagnostico(3194, "Tag 110 non definito");
            if (datiDoc.getT110().getA_110_0().getType() == TipoSeriale.B_TYPE)
                return "C";
            else
                return "S";
        }
        throw new EccezioneSbnDiagnostico(3061, "Impossibile stabilire la natura del titolo");
    }

    /** Ritorna l'elenco diagnostico i documenti simili */
    public List getElencoDiagnostico() {
        return elencoDiagnostico;
    }

//    protected void validaNumeroStandard(NumStdType[] numStd, String cd_natura)
//        throws EccezioneSbnDiagnostico {
//    	// almaviva2 Giugno 2016 - Metodo cancellato perchè obsoleto -
//    }


    /**
     * almaviva2 - Settembre 2014 - Evolutiva per la gestione delle date del titolo per gestire il carattere punto -
     * Controlla che le date siano formalmente corrette in termini dei primi due caratteri numerici e dei successivi due numerici
     * o uguale al carattere punto '.';
     * @return string che conterrà eventuale diagnostico da inviare al bibliotecario
     */

    /** Verifica il formato di una data: devono essere 4 caratteri numerici > 1830 */
    public void verificaFormatoData(String data, String tipoData, String fieldName) throws EccezioneSbnDiagnostico {
        if (data.length() != 4)
            throw new EccezioneSbnDiagnostico(3320, "Data malformata");

//        if (tipoData.equals("F"))
//            if (data.equals("    "))
//                return;
//        for (int i = 0; i < 4; i++)
//            if (!Character.isDigit(data.charAt(i)))
//                throw new EccezioneSbnDiagnostico(3320, "Data malformata");

        if (tipoData.equals("F")||tipoData.equals("G"))
			verificaFormatoDataFG(data, tipoData, fieldName);
    }
    /** Verifica il formato di una data: devono essere 4 caratteri numerici <1831*/
    public void verificaFormatoDataModerno(String data, String tipoData, String fieldName) throws EccezioneSbnDiagnostico {
//        verificaFormatoData(data, tipoData);
//        if (!tipoData.equals("F"))
//            if (Integer.parseInt(data) < 1831)
//                throw new EccezioneSbnDiagnostico(3270, "Data malformata");
		verificaFormatoData(data, tipoData, fieldName);
		if (data.equals("    "))
				throw new EccezioneSbnDiagnostico(3355, "La "+fieldName+" non puo' essere spazi '    '");
		if (data.compareTo("1831") < 0)
				throw new EccezioneSbnDiagnostico(3357, fieldName + " non puo' essere inferiore a 1831");
    }

    /** Verifica il formato di una data: devono essere 4 caratteri numerici <1831*/
    public void verificaFormatoDataAntico(String data, String tipoData, String fieldName) throws EccezioneSbnDiagnostico {
//        verificaFormatoData(data, tipoData);
//        if (!tipoData.equals("F"))
//            if (Integer.parseInt(data) >= 1831)
//                throw new EccezioneSbnDiagnostico(3058, "Data malformata");

    	verificaFormatoData(data, tipoData, fieldName);
    	if (data.equals("    "))
    			throw new EccezioneSbnDiagnostico(3355, "La "+fieldName+" non puo' essere spazi '    '");
    	if (data.equals("0000"))
    			throw new EccezioneSbnDiagnostico(3357, "La "+fieldName+" non puo' essere '0000'");
    	if (data.compareTo("1831") >= 0)
    			throw new EccezioneSbnDiagnostico(3357, fieldName + " non puo' essere maggiore di 1830");
    }
    // almaviva2 - Settembre 2014 - FINE Evolutiva per la gestione delle date del titolo per gestire il carattere punto -


    protected void validaPerCrea(DatiDocType datiDoc, String cd_natura, boolean _cattura)
        throws EccezioneDB, EccezioneSbnDiagnostico {

    	// almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
    	// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
    	// In questo caso il controllo sul valore null della 183 viene effettuato ma se siamo in cattura si assume come
        // corretto anche il null (nel caso in cui l'Indice non lo invii)
    	validaPerArea0(datiDoc, cd_natura, _cattura); // almaviva7/almaviva4 31/07/14
        // almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
    	// Inizio Adeguamenti alla nuova gestione dei controlli fatta in Indice
    	validaPerTitolo(datiDoc);
    	validaPerMaterialeRecord(datiDoc, cd_natura);
    	validaPerLivelloBibliografico(datiDoc);

		boolean m_moderno = true;   // DEFAULT
        SbnMateriale tipoMateriale = datiDoc.getTipoMateriale();


        // Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi3 di Carla Scognamiglio
        // il controllo sul tipoMateriale deve essere fatto inserendo il toString altrimenti non viene riconosciuto
		if (tipoMateriale == null) {
			// Se tipo materiale assente se data presente controlliamo se antico o moderno

			// BUG MANTIS ESERCIZIO 0006021 - Inizio Modifica NOVEMBRE 2015, almaviva2
			// adeguativa ad Indice per controllo su data spoglio
			C100 t100 = datiDoc.getT100();
			boolean hasT100 = (t100 != null);
			boolean hasTipoData = false;
			boolean hasData1 = false;
//			boolean hasTipoData = (t100.getA_100_8() != null);
//			boolean hasData1 = (t100.getA_100_9() != null);
			if (hasT100)
				hasTipoData = (t100.getA_100_8() != null ? true : false);
			if (hasTipoData)
				hasData1 = (t100.getA_100_9() != null ? true : false);
			// BUG MANTIS ESERCIZIO 0006021 - Fine Modifica NOVEMBRE 2015, almaviva2
			if (hasT100 == true && hasTipoData == true && hasData1 == true) {
				String data1 = t100.getA_100_9();
				if (AnnoDateTitolo.isYearAntico(data1))
					m_moderno = false;
			}
		} else if (tipoMateriale.toString().equals("M") && !cd_natura.equals("S") //almaviva4 periodici antichi
				|| tipoMateriale.toString().equals("H")) {
			m_moderno = true;
		} else if (tipoMateriale.toString().equals("E")) {
			m_moderno = false;
		} else if (tipoMateriale.toString().equals("C")
				|| tipoMateriale.toString().equals("G")
				|| tipoMateriale.toString().equals("U")
				|| (tipoMateriale.toString().equals("M") && cd_natura.equals("S")) ) //almaviva4 periodici antichi	{
		{
			m_moderno = true;

			// Solo se data presente controlliamo se antico o moderno
			// BUG MANTIS ESERCIZIO 0006021 - Inizio Modifica NOVEMBRE 2015, almaviva2
			// adeguativa ad Indice per controllo su data spoglio
			C100 t100 = datiDoc.getT100();
			boolean hasT100 = (t100 != null);
			boolean hasTipoData = false;
			boolean hasData1 = false;
//			boolean hasTipoData = (t100.getA_100_8() != null);
//			boolean hasData1 = (t100.getA_100_9() != null);
			if (hasT100)
				hasTipoData = (t100.getA_100_8() != null ? true : false);
			if (hasTipoData)
				hasData1 = (t100.getA_100_9() != null ? true : false);
			// BUG MANTIS ESERCIZIO 0006021 - Fine Modifica NOVEMBRE 2015, almaviva2
			if (hasT100 == true && hasTipoData == true && hasData1 == true)
			{
				String data1 = t100.getA_100_9();
				if (AnnoDateTitolo.isYearAntico(data1))
					m_moderno = false;
			}
		}
		else  {
			// manutenzione BUG esercizio 5783 allinaemanto di collane con data A e valore 18.. in annoPubbl1
			// Se tipo materiale è assente ma non con valore null ma con valore " " (SbnMateriale = "7")
			// BUG MANTIS ESERCIZIO 0006021 - Inizio Modifica NOVEMBRE 2015, almaviva2
			// adeguativa ad Indice per controllo su data spoglio
			C100 t100 = datiDoc.getT100();
			boolean hasT100 = (t100 != null);
			boolean hasTipoData = false;
			boolean hasData1 = false;
//			boolean hasTipoData = (t100.getA_100_8() != null);
//			boolean hasData1 = (t100.getA_100_9() != null);
			if (hasT100)
				hasTipoData = (t100.getA_100_8() != null ? true : false);
			if (hasTipoData)
				hasData1 = (t100.getA_100_9() != null ? true : false);
			// BUG MANTIS ESERCIZIO 0006021 - Fine Modifica NOVEMBRE 2015, almaviva2
			if (hasT100 == true && hasTipoData == true && hasData1 == true) {
				String data1 = t100.getA_100_9();
				if (AnnoDateTitolo.isYearAntico(data1))
					m_moderno = false;
			}
		}
//		else
//			m_moderno = true; // Default


		//==============================================================================================================
		// INIZIO INTERVENTO almaviva2 SETTEMBRE 2015
		// TUTTI I CONTROLLI SULLE DATE VENGONO SALTATI IN CASO DI CATTURA/AGGIORNAMENTO
		// QUANDO IL BIBLIOTECARIO VORRA' AGGIORNARE LA NOTIZIA PROVVEDERA' ANCHE A CORREGGERE I PROBLEMI DI DATA
//		if (m_moderno == true)
//			validaPerDate_AM(DATA_MODERNA, datiDoc, cd_natura, _cattura);
//		else
//			validaPerDate_AM(DATA_ANTICA, datiDoc, cd_natura, _cattura);

		if (!_cattura) {
			if (m_moderno == true) {
				validaPerDate_AM(DATA_MODERNA, datiDoc, cd_natura, _cattura);
			} else {
				validaPerDate_AM(DATA_ANTICA, datiDoc, cd_natura, _cattura);
			}
		}


		// FINE INTERVENTO almaviva2 SETTEMBRE 2015
		//==============================================================================================================

		validaPerLingua(datiDoc, cd_natura);
		validaPerPaese(datiDoc, cd_natura);
		validaPerFormaContenuto(datiDoc, cd_natura);
		validaPerGeneri(datiDoc);
		validaPerAreeISBD(datiDoc, cd_natura);
		validaPerNumStd(datiDoc, cd_natura);

		// Controllo congruenza dati di collezione
		// ---------------------------------------
		if (cd_natura.equals("C")) {
			if (datiDoc.getT110() == null && datiDoc.getNaturaDoc() == null)
				throw new EccezioneSbnDiagnostico(3224, "Tipo seriale mancante");
		}
		// Controllo luogo di pubblicazione
		// --------------------------------
		// Intervento MAGGIO 2016: tutti i controlli sul luogo di pubblicazione devono essere eliminati

//		if (m_moderno && datiDoc.getT210Count() > 0) {
//			if (datiDoc.getT210(0).getAc_210Count() == 0
//				|| datiDoc.getT210(0).getAc_210(0).getA_210Count() == 0) {
//				throw new EccezioneSbnDiagnostico(3263, "Luogo di pubblicazione obbligatorio");
//			}
//		}
    	// Fine Adeguamenti alla nuova gestione dei controlli fatta in Indice
    }

    protected void validaPerCreaAntico(DatiDocType datiDoc, String cd_natura, boolean _cattura)
        throws EccezioneDB, EccezioneSbnDiagnostico {

        // almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi

    	// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
    	// In questo caso il controllo sul valore null della 183 viene effettuato ma se siamo in cattura si assume come
        // corretto anche il null (nel caso in cui l'Indice non lo invii)
    	validaPerArea0(datiDoc, cd_natura, _cattura); // almaviva7/almaviva4 31/07/14


    	// Inizio Adeguamenti alla nuova gestione dei controlli fatta in Indice
		validaPerTitolo(datiDoc);
		if (cd_natura.equals("S"))
		  throw new EccezioneSbnDiagnostico(3069, "Natura errata");
		//CONTROLLO IMPRONTA
		AnticoType antico = (AnticoType) datiDoc;
		ImprontaValida ci = new ImprontaValida();
		for (int i = 0; i < antico.getT012Count(); i++)
			ci.verificaCorrettezza(antico.getT012(i));
		validaPerFormaContenuto(datiDoc, cd_natura);
		validaPerGeneri(datiDoc);


		//==============================================================================================================
		// INIZIO INTERVENTO almaviva2 SETTEMBRE 2015
		// TUTTI I CONTROLLI SULLE DATE VENGONO SALTATI IN CASO DI CATTURA/AGGIORNAMENTO
		// QUANDO IL BIBLIOTECARIO VORRA' AGGIORNARE LA NOTIZIA PROVVEDERA' ANCHE A CORREGGERE I PROBLEMI DI DATA

		//validaPerDateAntico(datiDoc, cd_natura);
		// validaPerDate_AM(DATA_ANTICA, datiDoc, cd_natura, _cattura);

		if (!_cattura) {
			validaPerDate_AM(DATA_ANTICA, datiDoc, cd_natura, _cattura);
		}

		// FINE INTERVENTO almaviva2 SETTEMBRE 2015
		//==============================================================================================================




		validaPerLingua(datiDoc, cd_natura);
		validaPerPaese(datiDoc, cd_natura);
		validaPerNumStd(datiDoc, cd_natura);

    	// Fine Adeguamenti alla nuova gestione dei controlli fatta in Indice
    }

    protected void validaPerCreaMusica(DatiDocType datiDoc, String cd_natura, String utente,boolean _cattura)
        throws EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
        validaPerCrea(datiDoc, cd_natura,_cattura);

        if (!(datiDoc instanceof MusicaType))
            throw new EccezioneSbnDiagnostico(3120, "Informazioni mancanti");
        MusicaType musica = (MusicaType) datiDoc;
        verificaLivelloCreazione(utente, "U", cd_natura, musica.getLivelloAut().toString(),_cattura);

        //CONTROLLO IMPRONTA
        ImprontaValida ci = new ImprontaValida();
        boolean mss = false;
        if (musica.getTipoMateriale() != null
            && musica.getTipoMateriale().getType() == SbnMateriale.valueOf("U").getType()
            && datiDoc.getGuida() != null
            && datiDoc.getGuida().getTipoRecord() != null
            // almaviva2 Riportata correzione dell'Indice per BUG MANTIS esercizio 4775 (vedi sotto commento esercizio)
			//Mantis 2283: aggiunta la condizione in || datiDoc.getGuida().getTipoRecord().equals(TipoRecord.valueOf("b"))
			// per consentire la valorizzaione dei campi t923 anche per le notizie con tipo record = 'b' (testo manoscritto)
//            && datiDoc.getGuida().getTipoRecord().equals(TipoRecord.valueOf("d")))
            && (datiDoc.getGuida().getTipoRecord().getType() == TipoRecord.valueOf("d").getType() || datiDoc.getGuida().getTipoRecord().getType() == TipoRecord.valueOf("b").getType()))
            mss = true;
        for (int i = 0; i < musica.getT012Count(); i++)
            ci.verificaCorrettezza(musica.getT012(i));
        C125 c125 = musica.getT125();
        if (mss && (c125 == null || c125.getA_125_0() == null))
            throw new EccezioneSbnDiagnostico(3227, "Codice presentazione obbligatorio");
        if (c125 != null) {
            if (c125.getA_125_0() != null)
                if (Decodificatore.getCd_tabella("Tb_musica", "cd_presentazione", c125.getA_125_0()) == null)
                    throw new EccezioneSbnDiagnostico(3152, "Codice presentazione errato");
            if (c125.getB_125() != null)
                if (Decodificatore.getCd_tabella("Tb_musica", "tp_testo_letter", c125.getB_125()) == null)
                    throw new EccezioneSbnDiagnostico(3153, "Tipo testo letterario errato");
        }
        C128 c128 = musica.getT128();
        if (c128 != null) {
            if (c128.getD_128() != null) {
            	//almaviva4 evolutiva TUM   almaviva2 REPLICATA il 14.05.2015
//                if (c128.getB_128() == null)
//                    throw new EccezioneSbnDiagnostico(3226, "Organico sintetico mancante");
              //almaviva4 fine evolutiva TUM  almaviva2 REPLICATA il 14.05.2015
                if (Decodificatore.getCd_tabella("Tb_musica", "tp_elaborazione", c128.getD_128()) == null)
                    throw new EccezioneSbnDiagnostico(3225, "Tipo elaborazione errato");
            }
        }
        C922 c922 = musica.getT922();
        if (c922 != null && c922.getA_922() != null)
            if (Decodificatore.getCd_tabella("Tb_rappresent", "tp_genere", c922.getA_922()) == null)
                throw new EccezioneSbnDiagnostico(3229, "Tipo genere errato");

        C923 c923 = musica.getT923();
        if (c923 != null) {
            //tbm.setDs_traduzione(c923.getA_923());
            if (c923.getB_923() != null)
                if (Decodificatore.getCd_tabella("Tb_musica", "cd_stesura", c923.getB_923()) == null)
                    throw new EccezioneSbnDiagnostico(3155, "Codice stesura errato");
            if (mss && c923.getE_923() == null && !cd_natura.equals("N"))
                throw new EccezioneSbnDiagnostico(3156, "Datazione mancante");
            if (c923.getG_923() != null)
                if (Decodificatore.getCd_tabella("Tb_musica", "cd_materia", c923.getG_923()) == null)
                    throw new EccezioneSbnDiagnostico(3157, "Codice materia errato");
            if (!mss
                && (c923.getB_923() != null
                    || c923.getC_923() != null
                    || c923.getD_923() != null
                    || c923.getE_923() != null
                    || c923.getG_923() != null
                    || c923.getH_923() != null
                    || c923.getL_923() != null
                    || c923.getM_923() != null))
                throw new EccezioneSbnDiagnostico(
                    3228,
                    "Presenti elementi specifici di manoscritti musicali");

        }

        C926[] c926 = musica.getT926();
        for (int i = 0; i < c926.length; i++) {
            if (c926[i].getA_926() != null)
                if (Decodificatore
                    .getCd_tabella("Tb_incipit", "tp_indicatore", c926[i].getA_926().toString())
                    == null)
                    throw new EccezioneSbnDiagnostico(3158, "Tipo indicatore errato");
            verificaNumero(c926[i].getB_926());
            verificaNumero(c926[i].getF_926());
            verificaNumero(c926[i].getG_926());
            // Inizio Intervento Febbraio 2014: il campo Voce/Strumento presente nell'incipit puo contenere anche il numero
            // di Voci/Strumento quindi deve essere gestita anche la parte numerica da inserire negli appositi campi di Db;
            if (c926[i].getH_926() != null) {
            	String strumento = c926[i].getH_926();
            	int posiz = 0;
            	for (int y = 0; y < strumento.length(); y++) {
    				if (Character.isDigit(strumento.charAt(y))) {
    					posiz = y;
    					break;
    				}
                }

            	if (posiz > 0) {
            		strumento = strumento.substring(0, posiz);
            	}
//               if (Decodificatore.getCd_tabella("ORGA", c926[i].getH_926()) == null) {
            	 if (Decodificatore.getCd_tabella("ORGA", strumento) == null) {
                    throw new EccezioneSbnDiagnostico(3136, "Voce / strumento errato");
                }
            }
            // Fine Intervento Febbraio 2014

            if (c926[i].getI_926() != null)
                if (Decodificatore.getCd_tabella("Tb_incipit", "cd_forma", c926[i].getI_926()) == null)
                    throw new EccezioneSbnDiagnostico(3230, "Codice forma errato");
            if (c926[i].getL_926() != null)
            	// almaviva2 18.12.2009 BUG MANTIS 3248 errore nel codice della tabella
//                if (Decodificatore.getCd_tabella("Tb_incipit", "cd_tonalita", c926[i].getL_926()) == null)
            	if (Decodificatore.getCd_tabella("Tb_composizione", "cd_tonalita", c926[i].getL_926()) == null)
                    throw new EccezioneSbnDiagnostico(3160, "Codice tonalita' errato");
            if (c926[i].getN_926() != null)
                if (c926[i].getN_926().length() > 8)
                    throw new EccezioneSbnDiagnostico(3137, "Stringa alterazione troppo lunga");
            if (c926[i].getR_926() != null) {
                Tb_titolo titoloLegato = new Titolo().estraiTitoloPerID(c926[i].getR_926());
                if (titoloLegato == null)
                    throw new EccezioneSbnDiagnostico(3233, "Titolo non presente in basedati");
                if (!titoloLegato.getCD_NATURA().equals("D"))
                    throw new EccezioneSbnDiagnostico(3069, "Natura del titolo errata");
            }
        }

        C927[] c927 = musica.getT927();
        for (int i = 0; i < c927.length; i++) {
            if (c927[i].getA_927() == null){
                throw new EccezioneSbnDiagnostico(3231, "Personaggio obbligatorio");
            }
            if (c927[i].getB_927() != null){
                if (Decodificatore.getCd_tabella("Tb_personaggio", "cd_timbro_vocale", c927[i].getB_927()) == null){
                    throw new EccezioneSbnDiagnostico(3161, "Codice timbro vocale errato");
                }
            }
            if (c927[i].getC3_927() != null){
            	if (new Autore().estraiAutorePerID(c927[i].getC3_927()) == null){
            		throw new EccezioneSbnDiagnostico(3232, "Autore non presente in base dati");
            	}
            }
        }

        //CONTROLLO NUM STD
        if (datiDoc.getNumSTDCount() > 0)

        	validaNumeroStandard2016(datiDoc.getNumSTD(), cd_natura, datiDoc.getGuida().getTipoRecord());
    }

    private void verificaNumero(String stringa) throws EccezioneSbnDiagnostico {
        if (stringa != null) {
            for (int i = 0; i < stringa.length(); i++)
                if (!Character.isDigit(stringa.charAt(i)))
                    throw new EccezioneSbnDiagnostico(3135, "Stringa non numerica");
        }
    }

    protected void validaPerCreaGrafica(DatiDocType datiDoc, String cd_natura,String utente, boolean _cattura)
        throws EccezioneDB, EccezioneSbnDiagnostico {
        if (!(datiDoc instanceof GraficoType))
            throw new EccezioneSbnDiagnostico(3120, "Tipo di doc errato");
        validaPerCrea(datiDoc, cd_natura,_cattura);
        //Validazioni sul T116: campi obbligatori e verifica tb_codici
        GraficoType gr = (GraficoType) datiDoc;
        verificaLivelloCreazione(utente, "G", cd_natura, gr.getLivelloAut().toString(),_cattura);

        // Inizio Segnalazione Carla del 10/03/2015:
        // inserito sulle mappe di variazione dettaglio parte fissa la gestione del Impronta anche nei Materiale Grafico e Cartografico
		ImprontaValida ci = new ImprontaValida();
		boolean mss = false;
		if (gr.getTipoMateriale() != null
			&& gr.getTipoMateriale().equals(SbnMateriale.valueOf("U"))
			&& datiDoc.getGuida() != null
			&& datiDoc.getGuida().getTipoRecord() != null
			//Mantis 2283: aggiunta la condizione in || datiDoc.getGuida().getTipoRecord().equals(TipoRecord.valueOf("b"))
			// per consentire la valorizzaione dei campi t923 anche per le notizie con tipo record = 'b' (testo manoscritto)
			&& (datiDoc.getGuida().getTipoRecord().equals(TipoRecord.valueOf("d")) || datiDoc.getGuida().getTipoRecord().equals(TipoRecord.valueOf("b"))))
			mss = true;
		for (int i = 0; i < gr.getT012Count(); i++)
			ci.verificaCorrettezza(gr.getT012(i));
		// almaviva7 19/06/14
		// Facciamo i test sulla data dell'impronta se esiste una impronta
		if (gr.getT012Count() > 0)
		{
			String date1, date2;
			C100 t100 = gr.getT100();
			if (t100 != null && t100.getA_100_8() != null) {

				date1 = t100.getA_100_9();
				date2 = t100.getA_100_13();
				if (date1 != null)
					AnnoDateTitolo.verifyYearAntico(date1, "data 1");
				if (date2 != null)
					AnnoDateTitolo.verifyYearAntico(date2, "data 2");

			}
		}
		 // Fine Segnalazione Carla del 10/03/2015:


        C116 c116 = gr.getT116();
        if (c116 == null
            || c116.getA_116_0() == null
            || c116.getA_116_1() == null
            || c116.getA_116_3() == null
            || c116.getA_116_16() == null)
            throw new EccezioneSbnDiagnostico(3138, "Mancano informazioni: T116");
        if (Decodificatore.getCd_tabella("Tb_grafica", "tp_materiale_gra", c116.getA_116_0()) == null)
            throw new EccezioneSbnDiagnostico(3162, "Tipo materiale grafico errato");
        if (Decodificatore.getCd_tabella("Tb_grafica", "cd_supporto", c116.getA_116_1()) == null)
            throw new EccezioneSbnDiagnostico(3171, "Codice supporto errato");
        if (Decodificatore.getCd_tabella("Tb_grafica", "cd_colore", c116.getA_116_3()) == null)
            throw new EccezioneSbnDiagnostico(3168, "Codice colore errato");
        if (c116.getA_116_4Count() > 0)
            if (Decodificatore.getCd_tabella("Tb_grafica", "cd_tecnica_dis_1", c116.getA_116_4(0)) == null)
                throw new EccezioneSbnDiagnostico(3163, "Codice tecnica disegno errato");
        if (c116.getA_116_4Count() > 1)
            if (Decodificatore.getCd_tabella("Tb_grafica", "cd_tecnica_dis_2", c116.getA_116_4(1)) == null)
                throw new EccezioneSbnDiagnostico(3163, "Codice tecnica disegno errato");
        if (c116.getA_116_4Count() > 2)
            if (Decodificatore.getCd_tabella("Tb_grafica", "cd_tecnica_dis_3", c116.getA_116_4(2)) == null)
                throw new EccezioneSbnDiagnostico(3163, "Codice tecnica disegno errato");
        if (c116.getA_116_10Count() > 0)
            if (Decodificatore.getCd_tabella("Tb_grafica", "cd_tecnica_sta_1", c116.getA_116_10(0)) == null)
                throw new EccezioneSbnDiagnostico(3164, "Codice tecnica sta errato");
        if (c116.getA_116_10Count() > 1)
            if (Decodificatore.getCd_tabella("Tb_grafica", "cd_tecnica_sta_2", c116.getA_116_10(1)) == null)
                throw new EccezioneSbnDiagnostico(3164, "Codice tecnica sta errato");
        if (c116.getA_116_10Count() > 2)
            if (Decodificatore.getCd_tabella("Tb_grafica", "cd_tecnica_sta_3", c116.getA_116_10(2)) == null)
                throw new EccezioneSbnDiagnostico(3164, "Codice tecnica sta errato");
        if (Decodificatore.getCd_tabella("Tb_grafica", "cd_design_funz", c116.getA_116_16()) == null)
            throw new EccezioneSbnDiagnostico(3165, "Codice design funz errato");

    }

    protected void validaPerCreaCartografia(DatiDocType datiDoc, String cd_natura,String utente, boolean _cattura)
        throws EccezioneDB, EccezioneSbnDiagnostico {
        if (!(datiDoc instanceof CartograficoType))
            throw new EccezioneSbnDiagnostico(3120, "Tipo di doc errato");
        validaPerCrea(datiDoc, cd_natura, _cattura);
        CartograficoType cartografico = (CartograficoType) datiDoc;
        verificaLivelloCreazione(utente, "C", cd_natura, cartografico.getLivelloAut().toString(),_cattura);


        // Inizio Segnalazione Carla del 10/03/2015:
        // inserito sulle mappe di variazione dettaglio parte fissa la gestione del Impronta anche nei Materiale Grafico e Cartografico
		ImprontaValida ci = new ImprontaValida();
		boolean mss = false;
		if (cartografico.getTipoMateriale() != null
			&& cartografico.getTipoMateriale().equals(SbnMateriale.valueOf("U"))
			&& datiDoc.getGuida() != null
			&& datiDoc.getGuida().getTipoRecord() != null
			//Mantis 2283: aggiunta la condizione in || datiDoc.getGuida().getTipoRecord().equals(TipoRecord.valueOf("b"))
			// per consentire la valorizzaione dei campi t923 anche per le notizie con tipo record = 'b' (testo manoscritto)
			&& (datiDoc.getGuida().getTipoRecord().equals(TipoRecord.valueOf("d")) || datiDoc.getGuida().getTipoRecord().equals(TipoRecord.valueOf("b"))))
			mss = true;
		for (int i = 0; i < cartografico.getT012Count(); i++)
			ci.verificaCorrettezza(cartografico.getT012(i));

		// almaviva7 19/06/14
		// Facciamo i test sulla data dell'impronta se esiste una impronta
		if (cartografico.getT012Count() > 0)
		{
			String date1, date2;
			C100 t100 = cartografico.getT100();
			if (t100 != null && t100.getA_100_8() != null) {

				date1 = t100.getA_100_9();
				date2 = t100.getA_100_13();
				if (date1 != null)
					AnnoDateTitolo.verifyYearAntico(date1, "data 1");
				if (date2 != null)
					AnnoDateTitolo.verifyYearAntico(date2, "data 2");

			}
		}
		// Fine Segnalazione Carla del 10/03/2015:


        C100 c100 = cartografico.getT100();

        // Inizio intervento per bug Liguria 4912  almaviva2 Riportato da Indice con commento:
		//Controllo per data null MANTIS 0001699
    	if(c100!=null){
    		if (c100.getA_100_20() != null)
                if (Decodificatore.getCd_tabella("Tb_cartografia", "tp_pubb_gov", c100.getA_100_20()) == null)
                    throw new EccezioneSbnDiagnostico(3166, "Tipo pubblicazione govenativa errato");
    	}
    	// Fine intervento per bug Liguria 4912


        C120 c120 = cartografico.getT120();
        if (c120 == null)
            throw new EccezioneSbnDiagnostico(3167, "Campo t120 obbligatorio");
        if (Decodificatore.getCd_tabella("Tb_cartografia", "cd_colore", c120.getA_120_0()) == null)
            throw new EccezioneSbnDiagnostico(3168, "Codice colore errato");

     // almaviva2 agosto 2017 - gestione nuovo campo proiezione carte (tabella PROE) su evolutiva indice
        if (c120.getA_120_7() != null) {
    		if (Decodificatore.getCd_tabella("Tb_cartografia", "tp_proiezione", c120.getA_120_7()) == null)
    			throw new EccezioneSbnDiagnostico(3278); //, "Codice tipo proiezione errato");
    	}


      // Inizio modifica almaviva2 BUG MANTIS 4202 09.02.2011 (ripresa da Protocollo Indice bug mantis 3931)
      // Il protocollo SBNMARC è stato corretto e non richiede più per il tipo materiale 'C' il meridiano d'origine.
      // Il campo infatti è da intendersi obbligatorio solo in presenza delle coordinate geografiche,
      // che non sono un campo obbligatorio.
//        if (Decodificatore.getCd_tabella("Tb_cartografia", "cd_meridiano", c120.getA_120_9()) == null)
//            throw new EccezioneSbnDiagnostico(3169, "Codice meridiano errato");
		if (Decodificatore.getCd_tabella("Tb_cartografia", "cd_meridiano", c120.getA_120_9()) == null) {
			if (c120.getA_120_9().trim().length() > 0)
				throw new EccezioneSbnDiagnostico(3169, "Codice meridiano errato");
		}
      // Fine modifica almaviva2 BUG MANTIS 4202 09.02.2011 (ripresa da Protocollo Indice bug mantis 3931)



        C121 c121 = cartografico.getT121();
        if (c121 == null)
            throw new EccezioneSbnDiagnostico(3170, "Campo c121 obbligatorio");
        if (c121.getA_121_3() == null)
            throw new EccezioneSbnDiagnostico(3172, "Supporto fisico obbligatorio");
        if (Decodificatore.getCd_tabella("Tb_cartografia", "cd_supporto_fisico", c121.getA_121_3()) == null)
            throw new EccezioneSbnDiagnostico(3171, "Supporto fisico errato");
        if (c121.getA_121_5() != null)
            if (Decodificatore.getCd_tabella("Tb_cartografia", "cd_tecnica", c121.getA_121_5()) == null)
                throw new EccezioneSbnDiagnostico(3188, "Codice tecnica errato");
        if (c121.getA_121_6() != null)
            if (Decodificatore.getCd_tabella("Tb_cartografia", "cd_forma_ripr", c121.getA_121_6()) == null)
                throw new EccezioneSbnDiagnostico(3173, "Codice forma ripr. errato");
        if (c121.getA_121_8() != null)
            if (Decodificatore.getCd_tabella("Tb_cartografia", "cd_forma_pubb", c121.getA_121_8()) == null)
                throw new EccezioneSbnDiagnostico(3174, "Codice forma pubb errato");
        if (c121.getB_121_0() != null)
            if (Decodificatore.getCd_tabella("Tb_cartografia", "cd_altitudine", c121.getB_121_0()) == null)
                throw new EccezioneSbnDiagnostico(3175, "Codice altitudine errato");
        C123 c123 = cartografico.getT123();
        if (c123 == null)
            throw new EccezioneSbnDiagnostico(3176, "Campo t123 obbligatorio");
        if (c123.getId1() == null)
            throw new EccezioneSbnDiagnostico(3206, "Id1 obbligatorio nel t123");
        if (Decodificatore.getCd_tabella("Tb_cartografia", "cd_tiposcala", c123.getId1().toString()) == null)
            throw new EccezioneSbnDiagnostico(3177, "Codice tipo scala errato");
        if (c123.getA_123() != null)
            if (Decodificatore.getCd_tabella("Tb_cartografia", "tp_scala", c123.getA_123()) == null)
                throw new EccezioneSbnDiagnostico(3178, "Tipo scala errato");

      // Inizio Modifica almaviva2 26.06.2012 Bug Mantis Collaudo 5015
      //  (In Polo (creazione in locale) risulta possibile inserire nel campo Scala il punto dopo le migliaia (es.: 100.000).
      // In Indice il controllo c'è: 'Protocollo di INDICE: 3348 La scala deve essere numerica'; occorrerebbe replicarlo in Polo. )
      // si riporta a correzione del bug la modifica fatta in Indice
      //almaviva4 15/03/2010
  		if (c123.getB_123() != null)
			verificaNumeroScala(c123.getB_123());

  		if (c123.getC_123()!= null)
			verificaNumeroScala(c123.getC_123());
  		//almaviva4 15/03/2010 fine
        // Fine Modifica almaviva2 26.06.2012 Bug Mantis Collaudo 5015



//		 Bug collaudo 5009: il formato corretto dele coordinate geografiche non è: 000° 000' 000'' ma 000° 00' 00''
//       Inserite etichetta Max nord e cosi' via per uniformare a Interfaccia diretta
//       Modificato controllo su latitudine affinche in presenza di 000° 00' 00'' cioe EQUATORE non si richieda l'emisfero (N/S)
//       Inizio Riportata anche modifica effettuata da Indice

//        if (c123.getD_123() != null) {
//            verificaCoordinate("EO", c123.getD_123());
//        }
//        if (c123.getE_123() != null) {
//            verificaCoordinate("EO", c123.getE_123());
//        }
//        if (c123.getF_123() != null) {
//            verificaCoordinate("NS", c123.getF_123());
//        }
//        if (c123.getG_123() != null) {
//            verificaCoordinate("NS", c123.getG_123());
//        }
      //almaviva CONTROLLO GREENWICH
        if( (c123.getD_123() != null) && (!(c123.getD_123().toString().equals(" 0000000")))){
      		if (c123.getD_123() != null) {
      			verificaCoordinate180("EO", c123.getD_123());
      		}
      		if (c123.getE_123() != null) {
      			verificaCoordinate180("EO", c123.getE_123());
      		}
      		if ((c123.getD_123() != null) && (c123.getE_123() != null)) {
      			verificaCoordinateEO(c123.getD_123(), c123.getE_123());
      		}
        }
      // almaviva CONTROLLO EQUATORE
      	if( (c123.getF_123() != null) && (!(c123.getF_123().toString().equals(" 0000000")))){
      		if (c123.getF_123() != null) {
      			verificaCoordinate90("NS", c123.getF_123());
      		}
      		if (c123.getG_123() != null) {
      			verificaCoordinate90("NS", c123.getF_123());
      		}
      		if ((c123.getF_123() != null) && (c123.getG_123() != null)) {
      			verificaCoordinateNS(c123.getF_123(), c123.getG_123());
      		}
      } // END almaviva CONTROLLO EQUATORE


//      Fine Riportata anche modifica effettuata da Indice




        // Inizio modifica almaviva2 BUG MANTIS 4202 09.02.2011 (ripresa da Protocollo Indice bug mantis 3931)
        // Il protocollo SBNMARC è stato corretto e non richiede più per il tipo materiale 'C' il meridiano d'origine.
        // Il campo infatti è da intendersi obbligatorio solo in presenza delle coordinate geografiche,
        // che non sono un campo obbligatorio.
  		if (  (c123.getD_123() != null) || (c123.getE_123() != null) || (c123.getF_123() != null) || (c123.getG_123() != null)) {
			if (c120.getA_120_9().trim().length() == 0)
				throw new EccezioneSbnDiagnostico(3169, "Codice meridiano obbligatorio");
		}
        // Inizio modifica almaviva2 BUG MANTIS 4202 09.02.2011 (ripresa da Protocollo Indice bug mantis 3931)



        C124 c124 = cartografico.getT124();
        if (c124 != null) {
            if (c124.getA_124() != null)
                if (Decodificatore.getCd_tabella("Tb_cartografia", "tp_immagine", c124.getA_124()) == null)
                    throw new EccezioneSbnDiagnostico(3183, "Tipo immagine errato");
            if (c124.getB_124() != null)
                if (Decodificatore.getCd_tabella("Tb_cartografia", "cd_forma_cart", c124.getB_124()) == null)
                    throw new EccezioneSbnDiagnostico(3184, "Codice forma cartografica errato");
            //if (c124.getD_124() == null)
            //    throw new EccezioneSbnDiagnostico(3185, "Tipo piattaforma obbligatorio");
            if (c124.getD_124() != null && Decodificatore.getCd_tabella("Tb_cartografia", "tp_piattaforma", c124.getD_124()) == null)
                throw new EccezioneSbnDiagnostico(3186, "Tipo piattaforma errato");
            if (c124.getE_124() != null)
                if (Decodificatore.getCd_tabella("Tb_cartografia", "cd_categ_satellite", c124.getE_124())
                    == null)
                    throw new EccezioneSbnDiagnostico(3187, "Codice categoria satellite errato");
        }
    }


    //almaviva4 15/03/2010 // Vedi commento nel punto dove viene chiamata;
	private void verificaNumeroScala(String stringa) throws EccezioneSbnDiagnostico {
		if (stringa != null) {
			for (int i = 0; i < stringa.length(); i++)
				if (!Character.isDigit(stringa.charAt(i)))
					throw new EccezioneSbnDiagnostico(3348, "La scala deve essere numerica");
		}
	}
	//almaviva4 15/03/2010 fine




    private void verificaCoordinate(String st, String coord) throws EccezioneSbnDiagnostico {
        //so già che è lungo otto.deve essere E o O + 7 caratteri
        if (st.indexOf(coord.charAt(0)) < 0)
            throw new EccezioneSbnDiagnostico(3331, "Coordinata malformata");
        for (int i = 1; i < coord.length(); i++) {
            if (!Character.isDigit(coord.charAt(i)))
                throw new EccezioneSbnDiagnostico(3331, "Coordinata malformata");
        }
    }
    public boolean verificaLivelloCreazione(String utente, String materiale, String natura, String livelloAut,boolean _cattura)
        throws EccezioneSbnDiagnostico {
        if (_cattura)
            return true;
        ValidatorProfiler validator = ValidatorProfiler.getInstance();
        Tbf_par_mat par =
            validator.getParametriUtentePerMateriale(utente, materiale);
        if (par == null || par.getTp_abilitaz()!='S')
            throw new EccezioneSbnDiagnostico(3245, "Utente non abilitato a modificare il materiale");
        String livelloUtente = par.getCd_livello();
        if (livelloUtente == null || Integer.parseInt(livelloAut) > Integer.parseInt(livelloUtente))
            throw new EccezioneSbnDiagnostico(3007, "Livello di autorità utente non consente l'operazione");
        if (tipoControllo != null && tipoControllo.getType() == SbnSimile.CONFERMA_TYPE)
            if (par.getFl_abil_forzat()!='S')
                throw new EccezioneSbnDiagnostico(3008); //Utente non abilitato per la forzatura
        if (natura.equals("N"))
            if (validator.getTb_parametro(utente).getFl_spogli()!='S')
                throw new EccezioneSbnDiagnostico(3244, "Impossibile modificare natura N");
        return true;
    }

    public void verificaFormatoDataFG(String data, String tipoData, String fieldName) throws EccezioneSbnDiagnostico {
		char chr;
		boolean cancellettoPos2 = false;
		if (tipoData.equals("F") || tipoData.equals("G")){
			for (int i = 0; i < 2; i++)
				if (!Character.isDigit(data.charAt(i)))
					throw new EccezioneSbnDiagnostico(3355, "I primi due caratteri devono essere numerici per " + fieldName);
			chr = data.charAt(2);
			if (!Character.isDigit(chr))
			{
				if (chr != '.')
					throw new EccezioneSbnDiagnostico(3355, "Il terzo carattere deve essere numerico o '.' per " + fieldName);
				else
					cancellettoPos2=true;
			}
			chr = data.charAt(3);
			if (!Character.isDigit(chr))
			{
				if (chr != '.')
					throw new EccezioneSbnDiagnostico(3355, "Il quarto carattere deve essere numerico o '.' per " + fieldName);
			}
			else
			{
				if(cancellettoPos2 == true)
					throw new EccezioneSbnDiagnostico(3355, "Il quarto carattere, '"+chr+"', deve essere preceduto da un numero per " + fieldName);
			}
		}
	}

    public void verificaDataF (String data1, String data2)  throws EccezioneSbnDiagnostico{
    	verificaFormatoDataFG(data1, "F", "data 1");

    	if (data2 != null)
    	{
    		for (int i = 0; i < 4; i++)
    		{
    			if (!Character.isDigit(data2.charAt(i)))
    				throw new EccezioneSbnDiagnostico(3355, "Data 2 deve essere numerica");
    		}
    	}
    }

    public void verificaDataG (String data1, String data2)  throws EccezioneSbnDiagnostico{
    	verificaFormatoDataFG(data1, "G", "data 1");
    	verificaFormatoDataFG(data2, "G", "data 2");
    }




    private void verificaCoordinate90(String st, String coord) throws EccezioneSbnDiagnostico {

  	  if (st.indexOf(coord.charAt(0)) < 0) {
  		throw new EccezioneSbnDiagnostico(3331, "Coordinata malformata");
  	  }
  	  if (coord.length() != 8) {
  		throw new EccezioneSbnDiagnostico(3331, "Coordinata malformata");

  	  }
  	  for (int i = 1; i < coord.length(); i++) {
  		if (!Character.isDigit(coord.charAt(i)))
  			throw new EccezioneSbnDiagnostico(3331, "Coordinata malformata");
  		  }

  	  if ((Integer.parseInt(coord.substring(1,4))) > 90)
  		throw new EccezioneSbnDiagnostico(3331, "Coordinata malformata");
  	  if ((Integer.parseInt(coord.substring(4,6))) > 59)
  		throw new EccezioneSbnDiagnostico(3331, "Coordinata malformata");
  	  if ((Integer.parseInt(coord.substring(6,8))) > 59)
  		throw new EccezioneSbnDiagnostico(3331, "Coordinata malformata");
    }


    private void verificaCoordinate180(String st, String coord) throws EccezioneSbnDiagnostico {

  	  if (st.indexOf(coord.charAt(0)) < 0) {
  		throw new EccezioneSbnDiagnostico(3331, "Coordinata malformata");
  	  }
  	  if (coord.length() != 8) {
  		throw new EccezioneSbnDiagnostico(3331, "Coordinata malformata");

  	  }
  	  for (int i = 1; i < coord.length(); i++) {
  		if (!Character.isDigit(coord.charAt(i)))
  			throw new EccezioneSbnDiagnostico(3331, "Coordinata malformata");

  		  }
  	  if ((Integer.parseInt(coord.substring(1,4))) > 180)
  	 	throw new EccezioneSbnDiagnostico(3331, "Coordinata malformata");
  	  if ((Integer.parseInt(coord.substring(4,6))) > 59)
  		throw new EccezioneSbnDiagnostico(3331, "Coordinata malformata");
  	  if ((Integer.parseInt(coord.substring(6,8))) > 59)
  		throw new EccezioneSbnDiagnostico(3331, "Coordinata malformata");
    }

  //la longitudine "a sinistra" deve essere più ad ovest di quella "a destra"
  //in una coppia O-O il numero a sinistra deve essere più alto
  //in una coppia E-E il numero a sinistra deve essere più basso

    private void verificaCoordinateEO(String coordO, String coordE) throws EccezioneSbnDiagnostico {
  	  if (coordO.charAt(0) == 'O' && coordE.charAt(0) == 'O')  {
  		  if ((Integer.parseInt(coordO.substring(1,7)))
  			 < (Integer.parseInt(coordE.substring(1,7))))  {
  			throw new EccezioneSbnDiagnostico(3345, "Coppia di coordinate longitudine errata");
  		  }
  	  }
	  	if (coordO.charAt(0) == 'E' && coordE.charAt(0) == 'E')  {
	  	  if ((Integer.parseInt(coordO.substring(1,7)))
	  		   > (Integer.parseInt(coordE.substring(1,7))))  {
	  		  throw new EccezioneSbnDiagnostico(3345, "Coppia di coordinate longitudine errata");
	  	  }
	  	}
	  	if (coordO.charAt(0) == 'E' && coordE.charAt(0) == 'O')  {
	  		throw new EccezioneSbnDiagnostico(3347, "Coppia di coordinate longitudine errata");
	  	}
    }

  //la latitudine "a sinistra" deve essere più a sud di quella "a destra"  (dal 24/03/2011 è il contrario)
  //in una coppia N-N il numero a sinistra deve essere più basso           (dal 24/03/2011 è il contrario)
  //in una coppia S-S il numero a sinistra deve essere più alto            (dal 24/03/2011 è il contrario)

    /* Donatella Roveri 24/03/2011
     * Qualora i due estremi di latitudine si trovino ambedue nell'emisfero settentrionale (da lat. N ... a lat. N ...),
     * i valori numerici (corrispondenti a gradi, primi, secondi) del primo estremo (massima distanza dall'equatore)
     * devono essere maggiori del secondo estremo (minima distanza dall'equatore)
     * Qualora i due estremi di latitudine si trovino ambedue nell'emisfero meridionale (da lat. S ... a lat S ...),
     * i valori numerici (corrispondenti a gradi, primi, secondi) del primo estremo (minima distanza dall'equatore)
     * devono essere minori del secondo estremo (massima distanza dall'equatore)
     * Qualora i due estremi di latitudine si trovino a cavallo dell'equatore (da lat. N ... a lat. S ...),
     * il dato che si controlla è che la sequenza sia N-S
     */

  /*
   * O 012° 20' 30''....... O 015° 30' 15''    ==> Errore : la longitudine "a sinistra" deve essere più ad ovest di quella "a destra"
   * (in una coppia O-O il numero a sinistra deve essere più alto)
   * E 039° 11' 02''....... E 020° 23' 32''    ==> Errore : la longitudine "a sinistra" deve essere più ad ovest di quella "a destra"
   * (in una coppia E-E il numero a sinistra deve essere più basso)
   * inibite tutte le possibili coppie E-O

   */
    private void verificaCoordinateNS(String coordN, String coordS) throws EccezioneSbnDiagnostico {
  	  if (coordN.charAt(0) == 'N' && coordS.charAt(0) == 'N')  {
  		if ((Integer.parseInt(coordN.substring(1,7)))
  			 < (Integer.parseInt(coordS.substring(1,7))))  {
  			throw new EccezioneSbnDiagnostico(3347, "Coppia di coordinate latitudine errata");
  		}
  	  }
  	  if (coordN.charAt(0) == 'S' && coordS.charAt(0) == 'S')  {
  	  	if ((Integer.parseInt(coordN.substring(1,7)))
  			 > (Integer.parseInt(coordS.substring(1,7))))  {
  		  throw new EccezioneSbnDiagnostico(3347, "Coppia di coordinate latitudine errata");
  	  	}
  	  }
  	  if (coordN.charAt(0) == 'S' && coordS.charAt(0) == 'N')  {
  		throw new EccezioneSbnDiagnostico(3347, "Coppia di coordinate latitudine errata");
  	  }
    }


	protected void validaPerArea0(DatiDocType datiDoc, String cd_natura, boolean _cattura)
	throws EccezioneDB, EccezioneSbnDiagnostico {

		// CONTROLLI AREA0 - a partire dallo schema 2.00
		// ---------------
        Double sv = Double.parseDouble(datiDoc.getVersioneSchema());
        if (sv.compareTo(2.00) > -1 )
        {
    		if (cd_natura.equals("M")
    			|| cd_natura.equals("S")
    			|| cd_natura.equals("W")
    			|| cd_natura.equals("N"))
    		{
	        	if (datiDoc.getT181Count() >0) {
			        C181[] c181 = datiDoc.getT181();
			        C182[] c182 = datiDoc.getT182();

			        // Correttiva Gennaio 2016  almaviva2 area 0: se si inserisce una seconda forma del contenuto e poi la si elimina
			        // tramite click sul cestino, il sistema va in errore http 500.
			        if (c181.length > c182.length) {
			        	throw new EccezioneSbnDiagnostico(3359); //, "Tipo di mediazione obbligatoria");
			        }


			        for (int i=0; i < c181.length ;i++) // cicliamo sulle istanze per controllare i campi obbligatori
			        {
			    		if (c181[i].getA_181_0() == null)
			    			throw new EccezioneSbnDiagnostico(3358); //, "Forma del contenuto obbligatoria");
			    		if (c182[i] == null || c182[i].getA_182_0() == null)
			    			throw new EccezioneSbnDiagnostico(3359); //, "Tipo di mediazione obbligatoria");
			        }

				}
				else
				{
	    			throw new EccezioneSbnDiagnostico(3358); //, "Forma del contenuto obbligatoria");
				}

				//controlli congruenza area 0 (181/182)
				if (datiDoc.getGuida() != null && datiDoc.getGuida().getTipoRecord() != null) {
					char tipo_record = datiDoc.getGuida().getTipoRecord().toString().charAt(0);

	       		C181[] c181 = datiDoc.getT181();
        		C182[] c182 = datiDoc.getT182();
                int istanze=0;
                if (c181.length == 2) {
                    // Controlliamo che la forma di contenuto delle due istanze sia diverso
                	// e che la mediazione sia uguale se abbiamo due istanze
                    if (c181[1].getA_181_0() == null)
                        istanze = 1;
                    else
                    {
//                        if (c181[0].getA_181_0().equals(c181[1].getA_181_0()))
//                            throw new EccezioneSbnDiagnostico(9999, "La forma di contenuto per le due istanze deve essere diversa");
//
//                        if (!c182[0].getA_182_0().equals(c182[1].getA_182_0()))
//                            throw new EccezioneSbnDiagnostico(9999, "Il tipo di mediazione deve essere uguale");
                        istanze = 2;
                    }

                   }
                else
                    istanze = 1;

                //cicliamo sulle due istanze
//                boolean istanzaObbligatoria = false;

                for (int i=0; i < istanze; i++)
                {
		    		StringBuffer sb = new StringBuffer();
		    		// Componiamo l'istanza utente
//		    		char chr;
//		    		chr = c181[i].getA_181_0() == null ? ' ' : c181[i].getA_181_0().charAt(0);
		    		char chr, formaContenuto, tipoMediazione;
		    		chr = formaContenuto = c181[i].getA_181_0() == null ? ' ' : c181[i].getA_181_0().charAt(0);

		    		sb.append(chr); // Forma del contenuto
		    		chr = c181[i].getB_181_0() == null ? ' ' : c181[i].getB_181_0().charAt(0);
		    		sb.append(chr); // Specificazione del tipo di contenuto
		    		chr = c181[i].getB_181_1() == null ? ' ' : c181[i].getB_181_1().charAt(0);
		    		sb.append(chr); // Specificazione del movimento
		    		chr = c181[i].getB_181_2() == null ? ' ' : c181[i].getB_181_2().charAt(0);
		    		sb.append(chr); // Specificazione della dimensionalita'
		    		chr = c181[i].getB_181_3() == null ? ' ' : c181[i].getB_181_3().charAt(0);
		    		sb.append(chr); // Specificazione sensoriale


//		    		chr = c182[i].getA_182_0() == null ? ' ' : c182[i].getA_182_0().charAt(0);
		    		chr = tipoMediazione = c182[i].getA_182_0() == null ? ' ' : c182[i].getA_182_0().charAt(0);
		    		//sb.append(chr); // Tipo di mediazione
		    		// Controllo congruenza tra forma del contenuto e tipo di mediazione
		    		if (!coerenzaMediazioneFormaContenuto(formaContenuto, tipoMediazione))
                    	throw new EccezioneSbnDiagnostico(3380, "Forma del contenuto '" + formaContenuto + "' non congruente con tipo di mediazione '"+tipoMediazione+"'"); // Area zero invalida



		    		String area0 = sb.toString();

//	                int index = binarySearch(area0_sorted_unique_permutations, area0, 5);
//	                if (index < 0)
//	                    //throw new EccezioneSbnDiagnostico(3380, "Area zero (spazio = assente): "+area0); // Area zero invalida
//                    	throw new EccezioneSbnDiagnostico(3380); // Area zero invalida

					matchRestOfArea0almaviva(area0); // 24/02/2015


//	                // Controlliamo obbligatorieta'
//	                String tipoRecord = area0_sorted_unique_permutations[index].substring(7);
//	                if (tipoRecord.indexOf(tipo_record) >= 0)
//	                	istanzaObbligatoria = true;

                } // End for istanze
//                if (istanzaObbligatoria == false)
//                    throw new EccezioneSbnDiagnostico(3379); // Istanza obbligatoria'


				} //end controlli congruenza area 0 (181/182)

				// INIZIO evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				// (almaviva) CONTROLLI AREA0 - a partire dallo schema 2.01 tag 182 e 183 tipo mediazione -- tipo supporto
				//
				// ---------------

				// almaviva2 Febbraio 2016 - Intervento interno - eliminato il controllo sul numero di versione e sostituito con controllo
				// su natura diversa da N percè lo Spoglio non ha il tipo supporto
//		        Double sv1 = Double.parseDouble(datiDoc.getVersioneSchema());
//		        if (sv1.compareTo(2.01) == 0 ){
				if (cd_natura.equals("M") || cd_natura.equals("S") || cd_natura.equals("W")) {
					C182[] c182 = datiDoc.getT182(); // TIPO MEDIAZIONE
					C183[] c183 = datiDoc.getT183(); // TIPO SUPPORTO

				     for (int i=0; i < c182.length ;i++) // cicliamo sulle istanze per controllare i campi obbligatori
				        {
//				    		if (c182[i].getA_182_0() == null){
//				    			throw new EccezioneSbnDiagnostico(3359); //, "Tipo di mediazione obbligatoria");
//				    		}
//				    		// evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
//				    		// In questo caso il controllo sul valore null della 183 viene effettuato ma se siamo in cattura si assume come
//				            // corretto anche il null (nel caso in cui l'Indice non lo invii)
//				    		if( c183.length > i){
//					    		if( (c183[i] == null || c183[i].getA_183_0() == null))
//					    			if (!_cattura)
//					    				throw new EccezioneSbnDiagnostico(3380, "Campo: Tipo di Supporto obbligatorio ");
//				    		}else{
//				    			if (!_cattura)
//				    				throw new EccezioneSbnDiagnostico(3380, "Campo: Tipo di Supporto obbligatorio ");
//				    		}

				    		if (c182[i].getA_182_0() == null){
				    			throw new EccezioneSbnDiagnostico(3359); //, "Tipo di mediazione obbligatoria");
				    		}
//tolto controllo di obbligatorietà del secondo tipo di supporto
				    		if (i == 0){
//mail Carla 06/11/2015 tipo supporto obbligatorio solo per nature 'M' 'S' 'W'
//				    			if( (c183.length == 0 || c183[i] == null || c183[i].getA_183_0() == null))
				    			if( (c183.length == 0 || c183[i] == null || c183[i].getA_183_0() == null)
				    				&& (cd_natura.equals("M") || cd_natura.equals("S") || cd_natura.equals("W")))
				    				if (!_cattura)
				    					throw new EccezioneSbnDiagnostico(3380, "Campo: Tipo di Supporto obbligatorio ");
			    		}

//				    		if( c183.length > i){
//					    		if( (c183[i] == null || c183[i].getA_183_0() == null))
//					    			 throw new EccezioneSbnDiagnostico(3380, "Campo: Tipo di Supporto obbligatorio ");
//				    		}else{
//				    			throw new EccezioneSbnDiagnostico(3380, "Campo: Tipo di Supporto obbligatorio ");
//				    		}

				        }

				     // evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)
				     // In questo caso il controllo sul valore null della 183 viene effettuato ma se siamo in cattura si assume come
			         // corretto anche il null (nel caso in cui l'Indice non lo invii)
				     // INIZIO IF CATTURA
				     if (!_cattura) {

			        	 String s_Tipo_mediazione="";
			        	 String s_Tipo_supporto="";


			        	 // Manutenzione MANTIS esercizio 6631. in coerenza con Protocollo di Indice almaviva2 - Luglio 2018
//			        	 for (int i=0; i < c182.length ;i++){
			        	 for (int i=0; c182 != null && i < c182.length ;i++){ // almaviva7 16/02/2016
			        		 if (c182[i] != null || c182[i].getA_182_0() != null){
			        			 s_Tipo_mediazione=c182[i].getA_182_0();
//			        			 if( c183.length >= i){
//			        			 if( c183 != null && c183.length >= i){ // almaviva7 16/02/2016
			        			 if( c183 != null && c183.length > i){ // almaviva7 17/02/2016
				        			 if (c183[i] != null || c183[i].getA_183_0() != null){
				        				 s_Tipo_supporto=c183[i].getA_183_0();
				        			 }
			        			 }
			        		 }
			        		 if(datiDoc.getT001().equals(Factoring.SBNMARC_DEFAULT_ID)){
			        			 // SIAMO IN CREAZIONE
			        			 if((s_Tipo_mediazione != null) && (s_Tipo_supporto.equals(""))){
			        				 throw new EccezioneSbnDiagnostico(3380, "Campo: Tipo di Supporto obbligatorio ");
			        			 }
			        		 }
			        		 if((s_Tipo_mediazione != null) && (s_Tipo_supporto != null)){
			        			 						// CONTROLLO CHE IL TIPO_SUPPORTO E' CORRETTO
			        			 String tp_Supporto = Decodificatore.getCd_tabella("SUPP", s_Tipo_supporto);
			 						if(tp_Supporto == null)
			 							 throw new EccezioneSbnDiagnostico(3380, "Codice Tipo di Supporto non valido");

			        			 // CONTROLLI DI CONGRUENZA
			        			 // CODICE TIPO DI MEDIAZIONE          CODICI  TIPO SUPPORTO AMMESSI
			        			 // a=Audio 							"sg","se","sd","si","sq","ss","st","sz"
			        			 // b=elettronico 						"ck","cb","cd","ce","ca","cf","ch","cr","cz"
			        			 // c=microforma 						"ha","he","hf","hb","hc","hd","hj","hh","hg","hz"
			        			 // d=microscopio 						"pp","pz"
			        			 // e=proiettato 						"mc","mf","mr","mo","gd","gf","gc","gt","gs","mz"
			        			 // f=sterografico 						"eh","es","ez"
			        			 // n=senza mediazione					"no","nn","na","nb","nc","nr","nz")
			        			 // g= video 							"vc","vf","vd","vr","vz"
			        			 // z=altre medizioni					"zu"
			        			 // m = Mediazioni Multiple non viene effettuato nessun controllo


			        			 List AUDIO = 				Arrays.asList("sg","se","sd","si","sq","ss","st","sz");
			        			 List ELETTRONICO = 		Arrays.asList("ck","cb","cd","ce","ca","cf","ch","cr","cz");
			        			 List MICROFORMA = 			Arrays.asList("ha","he","hf","hb","hc","hd","hj","hh","hg","hz");
			        			 List MICROSCOPIO = 		Arrays.asList("pp","pz");
			        			 List PROIETTATO = 			Arrays.asList("mc","mf","mr","mo","gd","gf","gc","gt","gs","mz");
			        			 List STEREOGRAFICO = 		Arrays.asList("eh","es","ez");
			        			 List SENZA_MEDIAZIONE = 	Arrays.asList("no","nn","na","nb","nc","nr","nz");
			        			 List VIDEO = 				Arrays.asList("vc","vf","vd","vr","vz");
			        			 List ALTRE_MEDIAZIONI = 	Arrays.asList("zu");
			        			 boolean check_182_183 = true;
			        			 if(s_Tipo_mediazione.equals("a")  && (!AUDIO.contains( s_Tipo_supporto ))) 			{ check_182_183=false; }
			        			 if(s_Tipo_mediazione.equals("b") &&  (!ELETTRONICO.contains( s_Tipo_supporto ))) 		{ check_182_183=false; }
			        			 if(s_Tipo_mediazione.equals("c") &&  (!MICROFORMA.contains( s_Tipo_supporto ))) 		{ check_182_183=false; }
			        			 if(s_Tipo_mediazione.equals("d") &&  (!MICROSCOPIO.contains( s_Tipo_supporto ))) 		{ check_182_183=false; }
			        			 if(s_Tipo_mediazione.equals("e") &&  (!PROIETTATO.contains( s_Tipo_supporto ))) 		{ check_182_183=false; }
			        			 if(s_Tipo_mediazione.equals("f") &&  (!STEREOGRAFICO.contains( s_Tipo_supporto ))) 	{ check_182_183=false; }
			        			 if(s_Tipo_mediazione.equals("n") &&  (!SENZA_MEDIAZIONE.contains( s_Tipo_supporto ))) 	{ check_182_183=false; }
			        			 if(s_Tipo_mediazione.equals("g") &&  (!VIDEO.contains( s_Tipo_supporto ))) 			{ check_182_183=false; }
			        			 if(s_Tipo_mediazione.equals("z") &&  (!ALTRE_MEDIAZIONI.contains( s_Tipo_supporto ))) 	{ check_182_183=false; }

			        			 if(!check_182_183){
			        				 throw new EccezioneSbnDiagnostico(3380, "Tipo di Supporto '" + s_Tipo_supporto + "' non congruente con tipo di mediazione '"+s_Tipo_mediazione+"'");
			        			 }
			        		 }
			        	 }// FINE (almaviva) CONTROLLI AREA0 - a partire dallo schema 2.01 tag 182 e 183 tipo mediazione -- tipo supporto

		        	}// FINE IF CATTURA
		        }
				// FINE evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183)


    		} //end if su natura

    		// almaviva7 28/07/14
    		C100 c100 = datiDoc.getT100();
    		if (c100 != null)
    		{
    			String data1 = c100.getA_100_9();
    			if (data1 != null)
    			  {
    				C100 t100 = datiDoc.getT100();
    				String tipo_data = Decodificatore.getCd_tabella("Tb_titolo", "tp_aa_pubb", t100.getA_100_8());
    				if (tipo_data == null)
						throw new EccezioneSbnDiagnostico(3276); // Tipo data non valido

    		    	// controlliamo che data 1 sia moderno per la 105bis
    				C105bis c105bis = datiDoc.getT105bis();
    				if (c105bis != null)
    					verificaFormatoDataModerno(data1, tipo_data, "C105bis richiede una data del moderno (> 1830)");

    				// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
    				// 12) Il campo tipo testo letterario (105 o 140) dovrebbe evidenziarsi soltanto in presenza di tipo record a/b.
    				//tag 105bis ammesso solo per tipo record 'a','b'
    				if (datiDoc.getGuida() != null && datiDoc.getGuida().getTipoRecord() != null) {
    						String tipoRecord = datiDoc.getGuida().getTipoRecord().toString();
							if (!tipoRecord.equals("a")	&& !tipoRecord.equals("b")) {
    							if (c105bis != null) {
									if (c105bis.getA_105_11()!= null)
										throw new EccezioneDB(3364); //"Tipo testo letterario ammesso solo per tipo record 'a' e 'b'");
    							}
    						}

    				}

    		    	// controlliamo che data 1 sia antico per la 140bis
    				C140bis c140bis = datiDoc.getT140bis();
    				if (c140bis != null)
    					verificaFormatoDataAntico(data1, tipo_data, "C140bis richiede una data dell'antico ( < 1831)");

    				// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
    				// 12) Il campo tipo testo letterario (105 o 140) dovrebbe evidenziarsi soltanto in presenza di tipo record a/b.
					//tag 140bis ammesso solo per tipo record 'a','b'
					if (datiDoc.getGuida() != null && datiDoc.getGuida().getTipoRecord() != null) {
							String tipoRecord = datiDoc.getGuida().getTipoRecord().toString();
							if (!tipoRecord.equals("a")	&& !tipoRecord.equals("b")) {
								if (c140bis != null) {
									if (c140bis.getA_140_17()!= null)
										throw new EccezioneDB(3364); //"Tipo testo letterario ammesso solo per tipo record 'a' e 'b'");
								}
							}
					} //end if tipo record !=null
    			  } //end if data1 != null

    		} // End if c100 != null
    		// End almaviva7 28/07/14

    		// Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
    		// 13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
    		//tag 125bis ammesso solo per tipo record 'i'

//tag 125bis ammesso solo per tipo record 'i'
			if (datiDoc.getGuida() != null && datiDoc.getGuida().getTipoRecord() != null) {
					String tipoRecord = datiDoc.getGuida().getTipoRecord().toString();
					if (!tipoRecord.equals("i"))
						{
						if (datiDoc.getT125bis() != null) {
//						C125bis c125bis = datiDoc.getT125bis();
//						if (c125bis.getB_125() != null) {
							throw new EccezioneDB(3365); //"Indicatore testo letterario ammesso solo per tipo record 'i'");
						}
					}
			}

        } //end if sv.compareTo(2.00)
		//fine almaviva4 16/07/2014 controlli obbligatorietà etichette 181-182
	}


	protected void validaPerTitolo(DatiDocType datiDoc)
	throws EccezioneDB, EccezioneSbnDiagnostico {
		// CONTROLLI SU TITOLO OBBLIGATORIO
		// --------------------------------
				if (datiDoc.getT200() == null)
				{
					throw new EccezioneSbnDiagnostico(3104, "Informazioni del titolo obbligatorie");
				}
		//almaviva4 06/06/2013
				else
				{
				C200 c200 = datiDoc.getT200();
				String a = c200.getA_200(0);
				if (!ValidazioneDati.isFilled(a))
					throw new EccezioneSbnDiagnostico(3104, "Informazioni del titolo obbligatorie");
				} //almaviva4 06/06/2013 fine

	} // End validaPerTitolo

	protected void validaPerMaterialeRecord(DatiDocType datiDoc, String cd_natura)
	throws EccezioneDB, EccezioneSbnDiagnostico {
	// Controllo preventivo sul tipo materiale e tipo record
	// -----------------------------------------------------
		if (cd_natura.equals("M")
			|| cd_natura.equals("S")
			|| cd_natura.equals("N")
			|| cd_natura.equals("W")) {
			if (datiDoc.getTipoMateriale() == null || datiDoc.getTipoMateriale().equals(SbnMateriale.valueOf(" ")))
				throw new EccezioneSbnDiagnostico(3105, "Tipo materiale errato");
			if (datiDoc.getGuida() == null || datiDoc.getGuida().getTipoRecord() == null)
				throw new EccezioneSbnDiagnostico(3106, "Tipo record mancante");
			else if (
				Decodificatore.getCd_tabella(
					"Tb_titolo",
					"tp_record_uni",
					datiDoc.getGuida().getTipoRecord().toString())
					== null)
				throw new EccezioneSbnDiagnostico(3107, "Tipo record errato");

		} else {
			if (datiDoc.getTipoMateriale() != null
				&& !datiDoc.getTipoMateriale().equals(SbnMateriale.valueOf(" ")))
				throw new EccezioneSbnDiagnostico(3105, "Tipo materiale errato");
			if (datiDoc.getGuida() != null && datiDoc.getGuida().getTipoRecord() != null)
				throw new EccezioneSbnDiagnostico(3108, "Tipo record non richiesto");
		}
	} // End validaPerMaterialeRecord

	protected void validaPerLivelloBibliografico(DatiDocType datiDoc)
	throws EccezioneDB, EccezioneSbnDiagnostico {
		// Controllo livello bibliografico - obbligatorio
		SbnNaturaDocumento naturaDoc = datiDoc.getNaturaDoc();
		if (naturaDoc == null) {
			if (datiDoc.getGuida() == null || datiDoc.getGuida().getLivelloBibliografico() == null)
				throw new EccezioneSbnDiagnostico(3109, "Livello bibliografico mancante");
		}
	} // End validaPerLivelloBibliografico

	protected void validaPerCreaElettronico(DatiDocType datiDoc, String cd_natura, String utente, boolean _cattura)
	throws EccezioneDB, EccezioneSbnDiagnostico {
	//validaPerCrea(false, datiDoc, cd_natura);
	validaPerCrea(datiDoc, cd_natura, _cattura);

	if (!(datiDoc instanceof ElettronicoType))
		throw new EccezioneSbnDiagnostico(3120, "Informazioni mancanti");

	ElettronicoType elettronico = (ElettronicoType) datiDoc;
	verificaLivelloCreazione(utente, "L", cd_natura, elettronico.getLivelloAut().toString(), _cattura);

	// almaviva2 Gennaio 2018 - Evolutiva per completamento attività su materiale elettronico
	// Gestione nuovi campi specifici per etichetta 135 (preso da nuova versione controlli Indice
	// almaviva elettronico controllo tipo record corretto solo tipo 'l'
	if ((datiDoc.getGuida() != null) && (datiDoc.getGuida().getTipoRecord() != null)){

		String tipoRecord = datiDoc.getGuida().getTipoRecord().toString();
		if ( (tipoRecord.equals("l"))) {
			C135 c135 = elettronico.getT135();
			// almaviva Elettronico
			if (c135 == null){
				throw new EccezioneDB(3383, "");
			}
			//l'unico tag obbligatori per Materiale Elettronico 'L' e tipo record 'l' o 'm' Ã¨ c135.getA_135_0() Campo tipo di risorsa elettronica
	        if (c135.getA_135_0() == null) {
	        	throw new EccezioneDB(3362,""); // Campo tipo di risorsa elettronica obbligatorio
	        }

//	        if (c135.getA_135_1() == null) {
//	        	throw new EccezioneDB(3363, "Campo indicazione specifica obbligatorio");
//	        }
	     // END almaviva Elettronico
	//		if (c115.getA_115_6() != null) {
	//			if (!c115.getA_115_5().equals("b"))
	//					throw new EccezioneSbnDiagnostico(3049, "Dati incongruenti");
	//		}
	//
		}else{
			throw new EccezioneSbnDiagnostico(3384); // AMMESSE SOLO
		}
	}





	//	//CONTROLLO NUM STD
	//	if (datiDoc.getNumSTDCount() > 0)
	//		validaNumeroStandardMus(datiDoc.getNumSTD(), cd_natura);

	} //end validaPerCreaElettronico


	public final int DATA_MODERNA = 1;
	public final int DATA_ANTICA = 2;
	public final boolean DEBUG_DATE = false;

	protected void validaPerDate_AM(int anticoModerno, DatiDocType datiDoc, String cd_natura, boolean _cattura)
	throws EccezioneDB, EccezioneSbnDiagnostico {
		// CONTROLLO DATE (Antico e Moderno)
		// ---------------------------------
		// Parte comune (antico o moderno)
		C100 t100 = datiDoc.getT100();

		boolean hasT100 = false;
		boolean hasTipoData = false;
		boolean hasData1 = false;

		if (t100 != null) {
			hasT100 = true;
//			hasTipoData = (t100.getA_100_8() != null);
//			hasData1 = (t100.getA_100_9() != null);
			if (hasT100)
				hasTipoData = (t100.getA_100_8() != null ? true : false);
			if (hasTipoData)
				hasData1 = (t100.getA_100_9() != null ? true : false);
		}

		if (t100 == null && cd_natura.equals("N")) // Nessun controllo date se Spoglio senza data
		{
			// Do nothing
			if (DEBUG_DATE == true)
			{
			throw new EccezioneSbnDiagnostico(1200, "CONTROLLO DATE OK per spoglio senza data");
			}
			return;
		}

		// Parte specifica (antico o moderno)
	     // Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
		else if (anticoModerno == DATA_ANTICA)
		{
			if (cd_natura.equals("M")
				|| cd_natura.equals("W")
				|| cd_natura.equals("N")
				|| cd_natura.equals("C")
				|| cd_natura.equals("R")
				)
				{
				if (cd_natura.equals("M") || cd_natura.equals("W") || cd_natura.equals("N"))
				{
					// Natura M,W (con data obbligatoria) ed N ( con data opzionale)
					if (!cd_natura.equals("N") && (t100 == null || hasTipoData == false || hasData1 == false))
						throw new EccezioneSbnDiagnostico(3354, "Data 1 mancante per natura " + cd_natura);

					String tipo_data = Decodificatore.getCd_tabella("Tb_titolo", "tp_aa_pubb", t100.getA_100_8());
					if (tipo_data == null || tipo_data.equals("C") || tipo_data.equals("I"))
						  throw new EccezioneSbnDiagnostico(3276); // Tipo data non valido
					String data1 = t100.getA_100_9();
					String data2 = t100.getA_100_13();
					if (hasTipoData)
					{
						// Controllo congruenza natura/tipo data
						if (!tipo_data.equals("D") && !tipo_data.equals("E") && !tipo_data.equals("F") && !tipo_data.equals("G"))
					  		throw new EccezioneSbnDiagnostico(3355, "Per nature M,W,N sono ammessi i tipo data D,E,F,G "); // Formato data non valido
					}
					if (hasData1)
					{
						/* almaviva7 29/10/14
						Mail almaviva 28/10/14
						Fase 1:
						    Gli attuali controlli sulle date attualmente operanti in ambiente di collaudo (con in più quello
						    aggiuntivo di obbligatorietà anche di data2 per tipo data F) saranno trasferiti in esercizio.
						    In particolare potranno essere recepite le date con '.' (punto) eccetto che per i tipi data D e F.
						    Agli attuali record in Indice di natura M e W e tipo data F con la sola data1 impostata
						    (circa 874.000) verrà modificato il valore di tipo data in D.
						*/
//						if (tipo_data.equals("D"))
						if (tipo_data.equals("D") || tipo_data.equals("F"))
						// End almaviva7
							AnnoDateTitolo.verifyYearNumeric(data1, "data 1"); // data deve esssere numerica
						else
							AnnoDateTitolo.verifyYear(data1, "data 1");	// controllo data con punti (max ultimi 2 ..)
						AnnoDateTitolo.verifyYearAntico(data1, "data 1"); // data < 1831
					}

					/* almaviva7 29/10/14
					Mail almaviva 28/10/14
					Fase 1:
					    Gli attuali controlli sulle date attualmente operanti in ambiente di collaudo (con in più quello
					    aggiuntivo di obbligatorietà anche di data2 per tipo data F) saranno trasferiti in esercizio.
					    In particolare potranno essere recepite le date con '.' (punto) eccetto che per i tipi data D e F.
					    Agli attuali record in Indice di natura M e W e tipo data F con la sola data1 impostata
					    (circa 874.000) verrà modificato il valore di tipo data in D.
					*/
					if (tipo_data.equals("F") && data2 == null)
					{
				  		throw new EccezioneSbnDiagnostico(3326, ""); // La data 2 e' obbligatoria per tipo data F
					}
					// End almaviva7 29/10/14

					// Controllo data 2 (obbligatoria per F, opzionale per E,G e non ammessa per D)
					if (data2 != null)
						{
						if (	tipo_data.equals("E") ||
								tipo_data.equals("F") ||
								tipo_data.equals("G"))
								{
							// almaviva7 29/10/14
							// Mail almaviva 28/10/14
							// In particolare potranno essere recepite le date con '.' (punto) eccetto che per i tipi data D e F.
								if (tipo_data.equals("F"))
									AnnoDateTitolo.verifyYearNumeric(data2, "data 2");	// controllo data con numeri
								else
									AnnoDateTitolo.verifyYear(data2, "data 2");	// controllo data con punti (max ultimi 2 ..)

								// Controllo che data 2 sia > o < di data 1 in base al tipo data
								  if (tipo_data.equals("E"))
								  { // E
									  if (AnnoDateTitolo.compareToAnni(data2, data1) > 0)
										  throw new EccezioneSbnDiagnostico(3357, "data 2 deve essere minore o uguale a data 1");
								  }
								  else
								  { // F,G
//									  if (AnnoDateTitolo.compareToAnno1Anno2(data2, data1) < 0)
//										  throw new EccezioneSbnDiagnostico(3357, "data 2 deve essere maggiore o uguale a data 1");
									  // if (AnnoDateTitolo.compareToAnni(data2, data1) < 1) // 09/01/2015
									// almaviva2 adeguamenti a protocollo di Indice fatta il 12.05.2015
									  if (AnnoDateTitolo.compareToAnniUpperLimit(data2, data1) < 1) // 06/05/2015 Mantis SbnWeb 5871
										  throw new EccezioneSbnDiagnostico(3357, "data 2 deve essere maggiore di data 1");
								  }
								} // End if E,F,G
						else if (tipo_data.equals("D"))
							  throw new EccezioneSbnDiagnostico(3356, "Data 2 non prevista per tipo data D");
						} // End if D
				} // End M,W,N
				else
				{ // C-collane (data non obbligatoria)
					String tipo_data = Decodificatore.getCd_tabella("Tb_titolo", "tp_aa_pubb", t100.getA_100_8());
					if (tipo_data == null || tipo_data.equals("C") || tipo_data.equals("I"))
						  throw new EccezioneSbnDiagnostico(3276); // Titpo data non valido

					String data1 = t100.getA_100_9();
					String data2 = t100.getA_100_13();
					if (hasTipoData)
					{
						// Controllo congruenza natura/tipo data
						if (!tipo_data.equals("A") && !tipo_data.equals("B") )
							//almaviva5_20160909 #6268
					  		//throw new EccezioneSbnDiagnostico(3355, "Per natura C sono ammessi i tipo data A,B"); // Formato data non valido
							throw new EccezioneSbnDiagnostico(3355, "Per nature C, R sono ammessi i tipo data A,B");
					}
					if (hasData1)
					{
						AnnoDateTitolo.verifyYear(data1, "data 1");	// controllo data con punti (max ultimi 2 ..)
						AnnoDateTitolo.verifyYearAntico(data1, "data 1"); // data > 1830
					}
					// Controllo data 2 (opzionale per B e non ammessa per A)
					if (data2 != null)
						{
						if (	tipo_data.equals("B"))
								{
								AnnoDateTitolo.verifyYear(data2, "data 2");	// controllo data con punti (max ultimi 2 ..)
								// Controllo che data 2 sia > o < di data 1 in base al tipo data
//								  if (AnnoDateTitolo.compareToAnno1Anno2(data2, data1) < 0)
//									  throw new EccezioneSbnDiagnostico(3357, "data 2 deve essere maggiore o uguale a data 1");
//								  if (AnnoDateTitolo.compareToAnni(data2, data1) < 1) // 09/01/2015
//									  throw new EccezioneSbnDiagnostico(3357, "data 2 deve essere maggiore di data 1");
								  //if (AnnoDateTitolo.compareToAnni(data2, data1) < 0) // 13/01/2015
								// almaviva2 adeguamenti a protocollo di Indice fatta il 12.05.2015
								if (AnnoDateTitolo.compareToAnniUpperLimit(data2, data1) < 0) // 06/05/2015 Mantis SbnWeb 5871
									throw new EccezioneSbnDiagnostico(3357, "data 2 deve essere maggiore o uguale a data 1");
								} // End if B

						else if (tipo_data.equals("A"))
							  throw new EccezioneSbnDiagnostico(3356, "Data 2 non prevista per tipo data A");
						} // End if A
				} // End C-collana
			} // End natura M,W,N,C
		} // End data antica
		else if (anticoModerno == DATA_MODERNA)
		{

			if (cd_natura.equals("M")
					|| cd_natura.equals("W")
					|| cd_natura.equals("N")
					|| cd_natura.equals("S")
					|| cd_natura.equals("C")
					|| cd_natura.equals("R")
					) {
				// Intervento su mail Contardi : i controlli di data per natura R (raccolta fittizia) devono essere
				// equiparati a quelli dei tipi Documento
				// if (cd_natura.equals("M") || cd_natura.equals("W") || cd_natura.equals("N"))
				if (cd_natura.equals("M") || cd_natura.equals("W") || cd_natura.equals("N")  || cd_natura.equals("R"))
				{ // Natura M,W (con data obbligatoria) ed N ( con data opzionale)

//					if (!cd_natura.equals("N") && (t100 == null || t100.getA_100_8() == null || t100.getA_100_9() == null))
					if (!cd_natura.equals("N") && (t100 == null || hasTipoData == false || hasData1 == false))
						throw new EccezioneSbnDiagnostico(3354, "Data 1 mancante per natura " + cd_natura);

					String tipo_data = Decodificatore.getCd_tabella("Tb_titolo", "tp_aa_pubb", t100.getA_100_8());
					if (tipo_data == null)
						throw new EccezioneSbnDiagnostico(3276); // Tipo data non valido

					// Inizio Modifica Gennaio 2015- SOLO PER IL POLO
					// nel caso di natura N è ammesso il null in tutti i campi relativi al tipo e valori data pubbl.
					// quindi l'if viene messo all'interno del controllo
					if (cd_natura.equals("N") && (t100 == null || (hasTipoData == false && hasData1 == false))) {
						// Caso ACCETTATO !!!!!!
					} else {
						if (tipo_data == null || tipo_data.equals("C") || tipo_data.equals("I"))
							  throw new EccezioneSbnDiagnostico(3276, " "); // Titpo data non valido
					}

//					if (tipo_data == null || tipo_data.equals("C") || tipo_data.equals("I"))
//					  throw new EccezioneSbnDiagnostico(3276, " "); // Titpo data non valido
					// Fine Modifica Gennaio 2015 - SOLO PER IL POLO



					String data1 = t100.getA_100_9();
					String data2 = t100.getA_100_13();

					if (hasTipoData)
					{
						// Controllo congruenza natura/tipo data
						if (!tipo_data.equals("D") && !tipo_data.equals("E") && !tipo_data.equals("F") && !tipo_data.equals("G"))
					  		throw new EccezioneSbnDiagnostico(3355, "Per nature M,W,N sono ammessi i tipo data D,E,F,G "); // Formato data non valido
					}
					if (hasData1)
					{
						/* almaviva7 29/10/14
						Mail almaviva 28/10/14
						Fase 1:
						    Gli attuali controlli sulle date attualmente operanti in ambiente di collaudo (con in più quello
						    aggiuntivo di obbligatorietà anche di data2 per tipo data F) saranno trasferiti in esercizio.
						    In particolare potranno essere recepite le date con '.' (punto) eccetto che per i tipi data D e F.
						    Agli attuali record in Indice di natura M e W e tipo data F con la sola data1 impostata
						    (circa 874.000) verrà modificato il valore di tipo data in D.
						*/
//						if (tipo_data.equals("D"))
						if (tipo_data.equals("D") || tipo_data.equals("F"))
						// End almaviva7
							AnnoDateTitolo.verifyYearNumeric(data1, "data 1"); // data deve esssere numerica
						else
							AnnoDateTitolo.verifyYear(data1, "data 1");	// controllo data con punti (max ultimi 2 ..)
						AnnoDateTitolo.verifyYearModerno(data1, "data 1"); // data > 1830
					}

					/* almaviva7 29/10/14
					Mail almaviva 28/10/14
					Fase 1:
					    Gli attuali controlli sulle date attualmente operanti in ambiente di collaudo (con in più quello
					    aggiuntivo di obbligatorietà anche di data2 per tipo data F) saranno trasferiti in esercizio.
					    In particolare potranno essere recepite le date con '.' (punto) eccetto che per i tipi data D e F.
					    Agli attuali record in Indice di natura M e W e tipo data F con la sola data1 impostata
					    (circa 874.000) verrà modificato il valore di tipo data in D.
					*/

					// Inizio Modifica Gennaio 2015 - SOLO PER IL POLO
					// Inserito if su nullità del tipo data percè per nature N è accettabile
					if (hasTipoData != false) {
						if (tipo_data.equals("F") && data2 == null)
						{
					  		throw new EccezioneSbnDiagnostico(3326, ""); // La data 2 e' obbligatoria per tipo data F
						}
					}

					// End almaviva7 29/10/14

					// Controllo data 2 (Obbligatoria per F, opzionale per B,E,G e non ammessa per A,D)
					if (data2 != null)
						{
						if (	tipo_data.equals("B") ||
								tipo_data.equals("E") ||
								tipo_data.equals("F") ||
								tipo_data.equals("G"))
								{
							// almaviva7 29/10/14
							// Mail almaviva 28/10/14
							// In particolare potranno essere recepite le date con '.' (punto) eccetto che per i tipi data D e F.
								if (tipo_data.equals("F"))
									AnnoDateTitolo.verifyYearNumeric(data2, "data 2");	// controllo data con numeri
								else
									AnnoDateTitolo.verifyYear(data2, "data 2");	// controllo data con punti (max ultimi 2 ..)


								// Controllo che data 2 sia > o < di data 1 in base al tipo data
								  if (tipo_data.equals("E"))
								  { // E
									  //if (data2.compareTo(data1) > 0)
									  if (AnnoDateTitolo.compareToAnni(data2, data1) > 0)
										  throw new EccezioneSbnDiagnostico(3357, "data 2 deve essere minore o uguale a data 1");
								  }
								  else if (tipo_data.equals("B")) // 13/01/2015
								  { // B
									// almaviva2 adeguamenti a protocollo di Indice fatta il 12.05.2015
									  //if (AnnoDateTitolo.compareToAnni(data2, data1) < 0)
									  if (AnnoDateTitolo.compareToAnniUpperLimit(data2, data1) < 0) // 06/05/2015 Mantis SbnWeb 5871
										  throw new EccezioneSbnDiagnostico(3357, "data 2 deve essere maggiore o uguale a data 1");
								  }
								  else
								  { // F,G
//									  if (data2.compareTo(data1) < 0)
//									  if (AnnoDateTitolo.compareToAnno1Anno2(data2, data1) < 0)
//										  throw new EccezioneSbnDiagnostico(3357, "data 2 deve essere maggiore o uguale a data 1");
									// almaviva2 adeguamenti a protocollo di Indice fatta il 12.05.2015
									  //if (AnnoDateTitolo.compareToAnni(data2, data1) < 1) // 09/01/2015
									  if (AnnoDateTitolo.compareToAnniUpperLimit(data2, data1) < 1) // 06/05/2015 Mantis SbnWeb 5871
										  throw new EccezioneSbnDiagnostico(3357, "data 2 deve essere maggiore di data 1");
								  }
								} // End if B,E,F,G

						else if (tipo_data.equals("A") ||
								 tipo_data.equals("D"))
							  throw new EccezioneSbnDiagnostico(3356, "Data 2 non prevista per tipo data A e D");
						} // End if A,D

				} // end natura M,W,N
				else
				{ // Natura S,C data1 obbligatoria // 27/02/2015
					String tipo_data = Decodificatore.getCd_tabella("Tb_titolo", "tp_aa_pubb", t100.getA_100_8());
					if (tipo_data == null || tipo_data.equals("C") || tipo_data.equals("I"))
						  throw new EccezioneSbnDiagnostico(3276, " "); // Titpo data non valido

					String data1 = t100.getA_100_9();
					String data2 = t100.getA_100_13();
					if (hasTipoData)
					{
						// Controllo congruenza natura/tipo data
						if (!tipo_data.equals("A") && !tipo_data.equals("B") )
					  		throw new EccezioneSbnDiagnostico(3355, "Per nature S,C sono ammessi i tipo data A,B"); // Formato data non valido
					}
					if (hasData1)
					{
						AnnoDateTitolo.verifyYear(data1, "data 1");	// controllo data con punti (max ultimi 2 ..)
						AnnoDateTitolo.verifyYearModerno(data1, "data 1"); // data > 1830
					}
					else
					{
						throw new EccezioneSbnDiagnostico(3354, "Data 1 obbligatoria per natura " + cd_natura); // 27/02/2015
					}

					// Controllo data 2 (opzionale per B e non ammessa per A)
					if (data2 != null) {
						if (	tipo_data.equals("B"))
								{
								AnnoDateTitolo.verifyYear(data2, "data 2");	// controllo data con punti (max ultimi 2 ..)
								// Controllo che data 2 sia > o < di data 1 in base al tipo data
//								  if (data2.compareTo(data1) < 0)
//								  if (AnnoDateTitolo.compareToAnno1Anno2(data2, data1) < 0)
//									  throw new EccezioneSbnDiagnostico(3357, "data 2 deve essere maggiore o uguale a data 1");
//								  if (AnnoDateTitolo.compareToAnni(data2, data1) < 1) // 09/01/2015
//									  throw new EccezioneSbnDiagnostico(3357, "data 2 deve essere maggiore di data 1");

								// almaviva2 adeguamenti a protocollo di Indice fatta il 12.05.2015
								// if (AnnoDateTitolo.compareToAnni(data2, data1) < 0) // 13/01/2015
							// if (AnnoDateTitolo.compareToAnniUpperLimit(data2, data1) < 0) // 06/05/2015 Mantis SbnWeb 5871

							// almaviva2 bug #5991 adeguamenti a protocollo di Indice fatta il 07.10.2015
							String anno1 = data1.replace('.', '0'); // Replace . con 0
							String anno2 = data2.replace('.', '0'); // Replace . con 0
							if (AnnoDateTitolo.compareToAnniUpperLimit(anno2, anno1) < 0) // 06/05/2015 almaviva
									  throw new EccezioneSbnDiagnostico(3357, "data 2 deve essere maggiore o uguale a data 1");
								} // End if B

						else if (tipo_data.equals("A"))
							  throw new EccezioneSbnDiagnostico(3356, "Data 2 non prevista per tipo data A");
						} // End if A
				} // end natura S,C
			} // End natura M,W,N,S,C
		} // End if data moderna

	if (DEBUG_DATE == true)
		{
		throw new EccezioneSbnDiagnostico(1200, "CONTROLLO DATE OK ");
		}

	} // validaPerDate_AM

	protected void validaPerLingua(DatiDocType datiDoc, String cd_natura)
	throws EccezioneDB, EccezioneSbnDiagnostico {
	// CONTROLLO LINGUA
	// ----------------
	// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
	if (cd_natura.equals("M")
		|| cd_natura.equals("S")
		|| cd_natura.equals("W")
		|| cd_natura.equals("T")
		|| cd_natura.equals("N")
		|| cd_natura.equals("R")
		) {
		if (datiDoc.getT101() == null || datiDoc.getT101().getA_101Count() == 0) {
			//Per l'import creo una lingua di default
			if (scheduled) {
				C101 c101 = new C101();
				c101.addA_101("ITA");
				datiDoc.setT101(c101);
			} else {
				throw new EccezioneSbnDiagnostico(3195, "Lingua mancante");
			}
		}
		for (int i = 0; i < datiDoc.getT101().getA_101Count(); i++)
			if (Decodificatore
				.getCd_tabella("Tb_titolo", "cd_lingua_1", datiDoc.getT101().getA_101(i).toUpperCase())
				== null)
				throw new EccezioneSbnDiagnostico(3204, "Codice lingua errato");
	} else if (datiDoc.getT101() != null && datiDoc.getT101().getA_101Count() > 0) {
		throw new EccezioneSbnDiagnostico(3196, "Lingua non prevista");
	}

	} // End validaPerLingua


	protected void validaPerPaese(DatiDocType datiDoc, String cd_natura)
	throws EccezioneDB, EccezioneSbnDiagnostico {
		// CONTROLLO PAESE
		// ---------------
		// Modifica almaviva2 del 21.07.2011 - Intervento per RACCOLTE FATTIZIE ( cod natura R) si comportano come le C per la tipologia
		if (cd_natura.equals("M")
			|| cd_natura.equals("S")
			|| cd_natura.equals("C")
			|| cd_natura.equals("W")
			|| cd_natura.equals("R")
			) {
			if (datiDoc.getT102() == null) {
				//Per l'import ne creo uno di default
				if (scheduled) {
					C102 c102 = new C102();
					c102.setA_102("IT");
					datiDoc.setT102(c102);
				} else {
					throw new EccezioneSbnDiagnostico(3197, "Paese mancante");
				}
			}
			if (Decodificatore.getCd_tabella("Tb_titolo", "cd_paese", datiDoc.getT102().getA_102().toUpperCase()) == null)
				throw new EccezioneSbnDiagnostico(3205, "Codice paese errato");
//				MANTIS 2402 CONTROLLO VALIDITA' PAESE
			if (datiDoc.getT102().getA_102()!=null){
				SbnData datavalidita;
				String original_date;
				Date now = new Date(System.currentTimeMillis()) ;
				SimpleDateFormat Format = new SimpleDateFormat("yyyyMMdd");
				original_date = Format.format(now) ;
				SbnData dataNow = new SbnData(original_date);
				datavalidita = Decodificatore.getDt_finevalidita("PAES", datiDoc.getT102().getA_102().toUpperCase());
				if (datavalidita.getAnno()!= null){
					if (datavalidita.compareTo(dataNow) == -1){
						throw new EccezioneSbnDiagnostico(7020, "Errore: Il Codice Paese utilizzato non è più valido");
					}
			   }
			}
		} else if (datiDoc.getT102() != null) {
			throw new EccezioneSbnDiagnostico(3198, "Paese non previsto");
		}
	}	// validaPerPaese

	protected void validaPerFormaContenuto(DatiDocType datiDoc, String cd_natura)
	throws EccezioneDB, EccezioneSbnDiagnostico {
		// CONTROLLO Forma contenuto (solo per moderno)
		// --------------------------------------------
		if (datiDoc instanceof ModernoType) {
			if (!(cd_natura.equals("M")
				|| cd_natura.equals("S")
				|| cd_natura.equals("C")
				|| cd_natura.equals("N")
				|| cd_natura.equals("T")
				|| cd_natura.equals("W"))) {
				ModernoType moderno = (ModernoType) datiDoc;
				if (moderno.getT105() != null && moderno.getT105().getA_105_4Count() > 0) {
					throw new EccezioneSbnDiagnostico(3199, "Forma contenuto non prevista");
				}
			}
		}// end if ModernoType

		//CONTROLLO Forma contenuto per antico
		else if (datiDoc instanceof AnticoType) {
			if (!(cd_natura.equals("M")
					|| cd_natura.equals("C")
					|| cd_natura.equals("T")
					|| cd_natura.equals("W"))) {
				AnticoType antico = (AnticoType)datiDoc;
				if (antico.getT140() != null && antico.getT140().getA_140_9Count() > 0) {
					throw new EccezioneSbnDiagnostico(3199, "Forma contenuto non prevista");
					}
				}
		} // end if AnticoType


	} // End validaPerFormaContenuto

	protected void validaPerGeneri(DatiDocType datiDoc)
	throws EccezioneDB, EccezioneSbnDiagnostico {
		// Controllo sui generi
		// --------------------
		if (datiDoc instanceof ModernoType) {
			ModernoType moderno = (ModernoType) datiDoc;
			if (moderno.getT105() != null && moderno.getT105().getA_105_4Count() > 0) {
				String[] generi = moderno.getT105().getA_105_4();
				if (generi[0].equals("2") || generi[0].equals("3")) {
					if (datiDoc.getT208()!=null) {
						throw new EccezioneSbnDiagnostico(3332, "Area musica non ammessa");
					}
				}
				for (int i = 0; i < generi.length; i++) {
					if (moderno.getTipoMateriale().equals(SbnMateriale.valueOf("C"))) {
						if (Decodificatore.getCd_tabella("Tb_titolo", "cd_genere_1", generi[i]) == null)
							throw new EccezioneSbnDiagnostico(3189, "Codice genere errato");
					} else {
						if (Decodificatore.getCd_tabella("Tb_titolo", "cd_genere_1", generi[i], "M") == null)
							throw new EccezioneSbnDiagnostico(3189, "Codice genere errato");
					}
				}
			}
		} // end if ModernoType
		else if (datiDoc instanceof AnticoType) {
			AnticoType antico = (AnticoType) datiDoc;
			if (antico.getT140() != null && antico.getT140().getA_140_9Count() > 0) {
				String[] generi = antico.getT140().getA_140_9();
				for (int i = 0; i < generi.length; i++) {
					if (antico.getTipoMateriale().equals(SbnMateriale.valueOf("C"))) {
						if (Decodificatore.getCd_tabella("Tb_titolo", "cd_genere_1", generi[i]) == null)
							throw new EccezioneSbnDiagnostico(3189, "Codice genere errato");
					} else {
						if (Decodificatore
							.getCd_tabella(
								"Tb_titolo",
								"cd_genere_1",
								generi[i],
								antico.getTipoMateriale().toString())
							== null)
							throw new EccezioneSbnDiagnostico(3189, "Codice genere errato");
					}
				}
			}
		} // end if AnticoType

	} // End validaPerGeneri

	protected void validaPerAreeISBD(DatiDocType datiDoc, String cd_natura)
	throws EccezioneDB, EccezioneSbnDiagnostico {
		// Controllo areee ISBD
		// ---------------------
		if (cd_natura.equals("A")
			|| cd_natura.equals("B")
			|| cd_natura.equals("P")
			|| cd_natura.equals("D")
			|| cd_natura.equals("T")
			|| cd_natura.equals("N")) {
			if (datiDoc.getT205() != null)
				throw new EccezioneSbnDiagnostico(3110, "Area dell'edizione non prevista");
			if (datiDoc.getT207() != null)
				throw new EccezioneSbnDiagnostico(3111, "Area della numerazione non prevista");
			if (datiDoc.getT210Count() > 0 && !cd_natura.equals("N"))
				throw new EccezioneSbnDiagnostico(3112, "Area della pubblicazione non prevista");
			if (datiDoc.getT215() != null && !cd_natura.equals("N"))
				throw new EccezioneSbnDiagnostico(3113, "Area della descrizione fisica non prevista");
		} else if (cd_natura.equals("S"))
			if (datiDoc.getT210Count() == 0)
				throw new EccezioneSbnDiagnostico(3114, "Area della pubblicazione obbligatoria");
		if (cd_natura.equals("C")) {
			if (datiDoc.getT205() != null)
				throw new EccezioneSbnDiagnostico(3110, "Area dell'edizione non prevista");
			if (datiDoc.getT207() != null)
				throw new EccezioneSbnDiagnostico(3111, "Area della numerazione non prevista");
			if (datiDoc.getT215() != null)
				throw new EccezioneSbnDiagnostico(3113, "Area della descrizione fisica non prevista");
		}
		if (datiDoc.getT205() != null && datiDoc.getT205().getB_205Count()>0 && datiDoc.getT205().getA_205() == null)  {
		  //Se c'è B205 ci deve essere anche A205
		  throw new EccezioneSbnDiagnostico(3335, "Area dell'edizione incompleta");
		}

		if (cd_natura.equals("W") || cd_natura.equals("S"))
			if (datiDoc.getT215() == null)
				throw new EccezioneSbnDiagnostico(3119, "Area della descrizione fisica obbligatoria");
	} // End validaPerAreeISBD

	protected void validaPerNumStd(DatiDocType datiDoc, String cd_natura)
	throws EccezioneDB, EccezioneSbnDiagnostico {
		//CONTROLLO NUM STD
		//almaviva4 bug mantis 4603 29/08/2011
//				if (datiDoc.getNumSTDCount() > 0)
//					validaNumeroStandard(datiDoc.getNumSTD(), cd_natura);
				if (datiDoc.getNumSTDCount() > 0) {
//					if (datiDoc instanceof MusicaType)
//						validaNumeroStandardMus(datiDoc.getNumSTD(), cd_natura);
//		          else
//almaviva4 evolutiva numeri standard 2014 fine
//						validaNumeroStandard(datiDoc.getNumSTD(), cd_natura);


					// almaviva2 Giugno 2016 - Chiamata al nuovo Metodo per verifiche Numero Standard -
					if (datiDoc.getGuida() == null || datiDoc.getGuida().getTipoRecord() == null) // 13/01/15
						validaNumeroStandard2016(datiDoc.getNumSTD(), cd_natura, null);
					else
						validaNumeroStandard2016(datiDoc.getNumSTD(), cd_natura, datiDoc.getGuida().getTipoRecord());
		     	}
	} // End validaPerNumStd

	//almaviva4 evolutiva 26/05/2011 aggiunto metodo


//	protected void validaNumeroStandard2014(NumStdType[] numStd, String cd_natura, TipoRecord tp_record) throws EccezioneSbnDiagnostico{
//		// almaviva2 Giugno 2016 - Metodo cancellato perchè obsoleto -
//	}


	// Febbraio 2015 almaviva2
	// Adeguamento al Protocollo di Indice per Diversa Gestione delle segnalazioni incongruenze nei dati specifici
	protected void validaPerCreaAudiovisivo(DatiDocType datiDoc, String cd_natura, String utente, boolean _cattura)
	throws 	EccezioneDB, EccezioneSbnDiagnostico, InfrastructureException {
//		validaPerCrea(false, datiDoc, cd_natura);
		validaPerCrea(datiDoc, cd_natura, _cattura);

		if (!(datiDoc instanceof AudiovisivoType))
			throw new EccezioneSbnDiagnostico(3120, "Informazioni mancanti");

		AudiovisivoType audiovisivo = (AudiovisivoType) datiDoc;
		verificaLivelloCreazione(utente, "H", cd_natura, audiovisivo.getLivelloAut().toString(), _cattura);

//tag 115 ammesso solo per tipo record 'g'
		if (datiDoc.getGuida() != null && datiDoc.getGuida().getTipoRecord() != null) {
			String tipoRecord = datiDoc.getGuida().getTipoRecord().toString();
			if (tipoRecord.equals("g")) {
				C115 c115 = audiovisivo.getT115();

				// Adeguativa a Protocollo di Indice
	            if (c115 == null) {
	            	throw new EccezioneDB(3360); // "Tipo di video obbligatorio"
	            }

	            if (c115.getA_115_0() == null) {
	            	throw new EccezioneDB(3360); // "Tipo di video obbligatorio"
	            }
				char a0 = c115.getA_115_0().charAt(0);

				if (c115.getA_115_4() == null) {
	            	throw new EccezioneDB(3381); // "Indicatore di colore obbligatorio"
	            }
	            if (c115.getA_115_5() == null) {
	            	throw new EccezioneDB(3382); // "Indicatore di suono obbligatorio"
	            }
	            if (c115.getA_115_6() != null && c115.getA_115_5().charAt(0) != 'b') {
					throw new EccezioneSbnDiagnostico(3387, "Incongruenza supporto suono/indicatore di suono"); //"Dati audiovisivi incongruenti"
				}
				if (c115.getA_115_7() != null)
				{
					char a7 = c115.getA_115_7().charAt(0);
					if (
						("abcdefg".indexOf(a7) != -1 && a0 != 'a') ||
						("klstuvwx".indexOf(a7) != -1 && a0 != 'b') ||
						("mnopqr".indexOf(a7) != -1 && a0 != 'c')
						)
//		            	throw new EccezioneDB(2900, "115 Incongruenza a7/a0");
						throw new EccezioneSbnDiagnostico(3387, "Incongruenza larghezza o dimensioni/tipo video"); //"Dati audiovisivi incongruenti"

				}
				if (c115.getA_115_8() != null)
				{
					if (a0 == 'c')
						throw new EccezioneSbnDiagnostico(3387, "Incongruenza formato distribuzione (proiezione)/tipo video"); //"Dati audiovisivi incongruenti"
//		            	throw new EccezioneDB(2900, "115 Incongruenza a8/a0");
				}
				else
				{
					if (a0 != 'c')
						throw new EccezioneSbnDiagnostico(3387, "Incongruenza formato distribuzione (proiezione)/tipo video"); //"Dati audiovisivi incongruenti"
//		            	throw new EccezioneDB(2900, "115 Incongruenza a8/a0");

				}
				if (c115.getA_115_9() != null)
				{
					if (a0 == 'b')
						throw new EccezioneSbnDiagnostico(3387, "Incongruenza tecnica (film, videoreg.)/tipo video"); //"Dati audiovisivi incongruenti"
//		            	throw new EccezioneDB(2900, "115 Incongruenza a9/a0");
				}
				if (c115.getA_115_10() != null)
				{
					if (a0 != 'a')
						throw new EccezioneSbnDiagnostico(3387, "Incongruenza formato presentazione/tipo video"); //"Dati audiovisivi incongruenti"
//		            	throw new EccezioneDB(2900, "115 Incongruenza a10/a0");
				}

				if (c115.getA_115_15() != null)
				{
					if (a0 != 'c')
						throw new EccezioneSbnDiagnostico(3387, "Incongruenza formato distribuzione (videoreg.)/tipo video"); //"Dati audiovisivi incongruenti"
//		            	throw new EccezioneDB(2900, "115 Incongruenza a15/a0");
				}
				else
				{
					if (a0 == 'c')
						throw new EccezioneSbnDiagnostico(3387, "Incongruenza formato distribuzione (videoreg.)/tipo video"); //"Dati audiovisivi incongruenti"
//		            	throw new EccezioneDB(2900, "115 Incongruenza a15/a0");

				}
				if (c115.getA_115_16() != null)
				{
					if (a0 != 'c')
						throw new EccezioneSbnDiagnostico(3387, "Incongruenza formato presentazione (videoreg.)/tipo video"); //"Dati audiovisivi incongruenti"
//		            	throw new EccezioneDB(2900, "115 Incongruenza a16/a0");
				}
				//Intervento interno su MAIL Scognamiglio - Aprile 2016
				// Compilando tutti i campi obbligatori specifici della videoregistrazione (materiale ‘H’, tipo record ‘g’) il formato di presentazione
				// NON è obbligatorio quindi non deve essere inviato messagio di errore
				// Intervento riprso da protocollo di indice
//				else
//				{
//					if (a0 == 'c')
//						throw new EccezioneSbnDiagnostico(3387, "Incongruenza formato presentazione (videoreg.)/tipo video"); //"Dati audiovisivi incongruenti"
////		            	throw new EccezioneDB(2900, "115 Incongruenza a16/a0");
//
//				}
				if (c115.getA_115_17() != null)
				{
					if (a0 != 'b')
						throw new EccezioneSbnDiagnostico(3387, "Incongruenza base emulsione/tipo video"); //"Dati audiovisivi incongruenti"
//		            	throw new EccezioneDB(2900, "115 Incongruenza a17/a0");
				}
				if (c115.getA_115_18() != null)
				{
					if (a0 != 'b')
						throw new EccezioneSbnDiagnostico(3387, "Incongruenza supporto secondario/tipo video"); //"Dati audiovisivi incongruenti"
//		            	throw new EccezioneDB(2900, "115 Incongruenza a18/a0");
				}
				if (c115.getA_115_19() != null)
				{
					if (a0 != 'c')
						throw new EccezioneSbnDiagnostico(3387, "Incongruenza standard televisivo/tipo video"); //"Dati audiovisivi incongruenti"
//		            	throw new EccezioneDB(2900, "115 Incongruenza a19/a0");
				}

				if (c115.getB_115_0() != null && (a0 != 'a') )
					throw new EccezioneSbnDiagnostico(3387, "Incongruenza versione/tipo video"); //"Dati audiovisivi incongruenti"
				if (c115.getB_115_1() != null && (a0 != 'a') )
					throw new EccezioneSbnDiagnostico(3387, "Incongruenza elementi produzione (film)/tipo video"); //"Dati audiovisivi incongruenti"
				if (c115.getB_115_2() != null && (a0 != 'a') )
					throw new EccezioneSbnDiagnostico(3387, "Incongruenza caratteristiche colore (film)/tipo video"); //"Dati audiovisivi incongruenti"
				if (c115.getB_115_3() != null && (a0 != 'a') )
					throw new EccezioneSbnDiagnostico(3387, "Incongruenza polarità (film)/tipo video"); //"Dati audiovisivi incongruenti"
				if (c115.getB_115_4() != null && (a0 != 'a') )
					throw new EccezioneSbnDiagnostico(3387, "Incongruenza supporto pellicola (film)/tipo video"); //"Dati audiovisivi incongruenti"
				if (c115.getB_115_5() != null && (a0 != 'a') )
					throw new EccezioneSbnDiagnostico(3387, "Incongruenza caratteristiche suono (film)/tipo video"); //"Dati audiovisivi incongruenti"
				if (c115.getB_115_6() != null && (a0 != 'a') )
					throw new EccezioneSbnDiagnostico(3387, "Incongruenza tipo pellicola (film)/tipo video"); //"Dati audiovisivi incongruenti"

		} //end if tipo record g per tag 115

		//tag 126 ammesso solo per tipo record 'i' 'j'
		if (tipoRecord.equals("i") || tipoRecord.equals("j")) {
			C126 c126 = audiovisivo.getT126();

	        // Controlli 126
            if (c126.getA_126_0() == null) {
            	throw new EccezioneDB(3361);
            }

            char a0 = c126.getA_126_0().charAt(0);

			if (c126.getA_126_1() != null)
			{
				char a1 = c126.getA_126_1().charAt(0);
				if (
					(
					("abcdeg".indexOf(a1) != -1 && a0 != 'a') ||
					("hi".indexOf(a1) != -1 && a0 != 'f') ||
					("klmnopqr".indexOf(a1) != -1 && "bcd".indexOf(a0) == -1)
					)
					)
					throw new EccezioneSbnDiagnostico(3387, "Incongruenza velocità/formato distribuzione"); //"Dati audiovisivi incongruenti"
//	            	throw new EccezioneDB(2900, "126 Incongruenza a1/a0");
			}
            if (c126.getA_126_5() != null) {
				if ("bcd".indexOf(a0) == -1)
					throw new EccezioneSbnDiagnostico(3387, "Incongruenza larghezza nastro/formato distribuzione"); //"Dati audiovisivi incongruenti"
//	            	throw new EccezioneDB(2900, "126 Incongruenza a5/a0");
            }
            if (c126.getA_126_6() != null) {
				if ("bcd".indexOf(a0) == -1)
					throw new EccezioneSbnDiagnostico(3387, "Incongruenza configurazione nastro/formato distribuzione"); //"Dati audiovisivi incongruenti"
//	            	throw new EccezioneDB(2900, "126 Incongruenza a6/a0");
            }

            if (c126.getB_126_1() != null)
			{
				char b1 = c126.getB_126_1().charAt(0);

				if (
					(
					("abcde".indexOf(b1) != -1 && a0 != 'a') ||
					("gh".indexOf(b1) != -1 && a0 != 'f') ||
					("ijkl".indexOf(b1) != -1 && "bcd".indexOf(a0) == -1)
					)
					)
					throw new EccezioneSbnDiagnostico(3387, "Incongruenza tipo materiale/formato distribuzione"); //"Dati audiovisivi incongruenti"
//	            	throw new EccezioneDB(2900, "126 Incongruenza b1/a0");
			}

		} // end if tipo record i j per tag 126


//tag 127 ammesso solo per tipo record 'i' 'j'
		if (tipoRecord.equals("i") || tipoRecord.equals("j")) {
			C127 c127 = audiovisivo.getT127();
		} // end if tag 127

//tag 128 ammesso solo per tipo record 'g' 'j'
		if (tipoRecord.equals("g") || tipoRecord.equals("j")) {
			C128 c128 = audiovisivo.getT128();
			if (c128 != null) {
				if (c128.getD_128() != null) {
					if (Decodificatore.getCd_tabella("Tb_musica", "tp_elaborazione", c128.getD_128()) == null)
						throw new EccezioneSbnDiagnostico(3225, "Tipo elaborazione errato");
				}
			}
		}

//tag 922 ammesso solo per tipo record 'g' 'j' 'i'
		C922 c922 = audiovisivo.getT922();
		if (c922 != null && c922.getA_922() != null)
			if (Decodificatore.getCd_tabella("Tb_rappresent", "tp_genere", c922.getA_922()) == null)
				throw new EccezioneSbnDiagnostico(3229, "Tipo genere errato");

//tag 927 ammesso solo per tipo record 'g' 'j' 'i'
		C927[] c927 = audiovisivo.getT927();
		for (int i = 0; i < c927.length; i++) {
			if (c927[i].getA_927() == null)
				throw new EccezioneSbnDiagnostico(3231, "Personaggio obbligatorio");
			if (c927[i].getB_927() != null)
				if (Decodificatore.getCd_tabella("Tb_personaggio", "cd_timbro_vocale", c927[i].getB_927())
					== null)
					throw new EccezioneSbnDiagnostico(3161, "Codice timbro vocale errato");
			if (c927[i].getC3_927() != null)
				if (new Autore().estraiAutorePerID(c927[i].getC3_927()) == null)
					throw new EccezioneSbnDiagnostico(3232, "Autore non presente in base dati");
			}
		} //end if (datiDoc.getGuida() != null && datiDoc.getGuida().getTipoRecord() != null)

//		//CONTROLLO NUM STD
//		if (datiDoc.getNumSTDCount() > 0)
//			validaNumeroStandardMus(datiDoc.getNumSTD(), cd_natura);



	}


	// Modifica del 19.05.2015 almaviva2 (adeguamento all'Indice) per accettare il valore n del tipoMediazione
	//boolean congruenzaMediazioneFormaContenuto(char formaContenuto, char tipoMediazione)
	boolean coerenzaMediazioneFormaContenuto(char formaContenuto, char tipoMediazione)

	{
		switch (formaContenuto)
		{
		case 'b':
//			if("bcgefy".indexOf(tipoMediazione) == -1)
			if("bcgefyn".indexOf(tipoMediazione) == -1) // 19/05/2015 accettare sia y che n temporaneamente. Poi solo 'n'
				return false;
			break;
		case 'c':
		case 'i':
			if("bcyn".indexOf(tipoMediazione) == -1) // 19/05/2015 accettare sia y che n temporaneamente. Poi solo 'n'
				return false;
			break;
		case 'e':
			if(tipoMediazione != 'y' && tipoMediazione != 'n') // 19/05/2015 accettare sia y che n temporaneamente. Poi solo 'n'
				return false;
			break;
		case 'd':
			if("abcyn".indexOf(tipoMediazione) == -1) // 19/05/2015 accettare sia y che n temporaneamente. Poi solo 'n'
				return false;
			break;
		case 'f':
			if(tipoMediazione != 'b')
				return false;
			break;
		case 'g':
		case 'h':
			if("ab".indexOf(tipoMediazione) == -1)
				return false;
			break;
		case 'm':
			if(tipoMediazione != 'm')
				return false;
			break;
		case 'a':
			if(tipoMediazione != 'b')
				return false;
			break;
		}
		return true;
	} // End coerenzaMediazioneFormaContenuto

	public void matchRestOfArea0almaviva(String permutation) throws EccezioneDB, EccezioneSbnDiagnostico
	{
		char formaContenuto = permutation.charAt(0);
		char specContenuto = permutation.charAt(1);
		char specMovimento = permutation.charAt(2);
		char specDimensionalita = permutation.charAt(3);
		char specSensoriale = permutation.charAt(4);



		// A. Coerenza Forma/Specificazione del tipo di contenuto
		// ===================================================
		//	1. e' obbligatoria almeno una forma
		//	2. per le forme 'b=immagine' e 'e=oggetto' la sp-cont deve essere 'c=cartografico' oppure assente
		//	3. per la forma 'd=musica'                 la sp-cont deve essere 'a='notato'      oppure 'b=eseguito'
		//	4. per la forma 'c=movimento'              la sp-cont deve essere 'a=notato'
		//	5. la sp-cont deve essere assente per le forme  'i=testo'
		//	                                                'g=suoni'
		//	                                                'h=parlato'
		//	                                                'a=dataset'
		//	                                                'f=programmi'
		//	                                                'm=forme multiple.'

		//	SE (forma[1]='b' OR forma[1]='e') AND NOT (sp-cont[1]='c' OR sp-cont[1]=null)                    OR
		//	    forma[1]='d'                  AND NOT (sp-cont[1]='a' OR sp-cont[1]='b')                     OR
		//	    forma[1]='c'                  AND NOT  sp-cont[1]='a'                                        OR
		//	   (forma[1]='i' OR forma[1]='g' OR forma[1]='h' OR forma[1]='a' OR forma[1]='f' OR forma[1]='m')

		if (
			(
			(formaContenuto == 'b' || formaContenuto=='e') && (specContenuto != 'c' && specContenuto != ' ')) ||
			(formaContenuto == 'd' && !(specContenuto == 'a' || specContenuto == 'b')) ||
			(formaContenuto == 'c' && specContenuto != 'a') ||
			("ighafm".indexOf(formaContenuto) != -1 && specContenuto != ' ')
		)
//			throw new EccezioneSbnDiagnostico(3380, "Incoerenza Forma '"+formaContenuto+ "' / specificazione di contenuto '"+specContenuto+"'");
	    	throw new EccezioneSbnDiagnostico(3380, "Forma del contenuto '" + formaContenuto + "' non congruente con tipo di contenuto '" + specContenuto + "'"); // Area zero invalida



		//B. Coerenza Forma/Specificazione del movimento (sp-mov)/Specificazione della dimensionalita' (sp-dim)
		// 1. sp-mov e sp-dim sono richieste (e obbligatorie) solo per la forma 'b=immagine'

		//	SE     forma[1]='b' AND     (sp-mov[1]=null OR  sp-dim[1]=null)    OR
		//	NOT forma[1]='b' AND NOT (sp-mov[1]=null AND sp-dim[1]=null)    OR
	   if (formaContenuto == 'b' && (specMovimento == ' ' || specDimensionalita == ' ') ||
			  formaContenuto != 'b' && (specMovimento != ' ' || specDimensionalita != ' '))
		throw new EccezioneSbnDiagnostico(3380, "Movimento e dimensionalita' richieste solo per la forma 'immagine'");

		//C. Coerenza specificazione sensoriale in funzione della forma di contenuto
		//==========================================================================
		coerenzaSpecSensoriale(formaContenuto, specContenuto, specSensoriale);

	} // End matchArea0almaviva

	void coerenzaSpecSensoriale(char formaContenuto, char specContenuto, char specSensoriale) throws EccezioneDB, EccezioneSbnDiagnostico
	{
		//1. se la forma è 'b=immagine' oppure
		//la forma è 'd=musica' con sp-cont 'a=notato'
		//allora la sp-sens deve essere 'e=visivo' oppure 'd=tattile'
		if ((formaContenuto == 'b' || (formaContenuto == 'd' && specContenuto == 'a')) && "de".indexOf(specSensoriale) == -1)
			throw new EccezioneSbnDiagnostico(3380, "La specificazione sensoriale '" +  specSensoriale +"' incongruente per forma del contenuto '"+formaContenuto+"'");

		//2. se la forma e' 'g=suoni' o 'h=parlato'                oppure
		//la forma e' 'd=musica' con sp-cont 'b=eseguito'
		//allora la sp-sens deve essere 'a=uditivo'
		if ((formaContenuto == 'g' || formaContenuto == 'h' || (formaContenuto == 'd' && specContenuto == 'b')) && specSensoriale != 'a')
			throw new EccezioneSbnDiagnostico(3380, "La specificazione sensoriale '" +  specSensoriale +"' incongruente per forma del contenuto '"+formaContenuto+"'");



		//3. se la forma e' 'c=movimento' o 'a=dataset' o 'f=programmi'
//		      la sp-sens deve essere 'e=visivo'
		if ((formaContenuto == 'c' || formaContenuto == 'a' || formaContenuto == 'f') && specSensoriale != 'e')
			throw new EccezioneSbnDiagnostico(3380, "La specificazione sensoriale '" +  specSensoriale +"' incongruente per forma del contenuto '"+formaContenuto+"'");

		//4. se la forma 'e 'i=testo'
		//la sp-sens deve essere 'e=visivo' oppure 'd=tattile' oppure 'c=olfattivo'
		if ((formaContenuto == 'i') && "cde".indexOf(specSensoriale) == -1)
			throw new EccezioneSbnDiagnostico(3380, "La specificazione sensoriale '" +  specSensoriale +"' incongruente per forma del contenuto '"+formaContenuto+"'");

		//5. se la forma e' 'm=forme contenuto multiplo '
		if ((formaContenuto == 'm' || formaContenuto == 'e') && "abcde".indexOf(specSensoriale) == -1)
			throw new EccezioneSbnDiagnostico(3380, "La specificazione sensoriale '" +  specSensoriale +"' incongruente per forma del contenuto '"+formaContenuto+"'");


	} // End coerenzaSpecSensoriale

    // INIZIO Evolutiva Novembre 2015 - almaviva2 cambio Materiale da antico/moderno e viceversa:
	// Evolutiva riguardante le funzionalità di modifica e allineamento titoli così da consentire di fatto
	// il cambio materiale (M-->E oppure E-->M) (modifiche riportate da software di Indice)
    // Nuovi metodi per effettuare i controlli di congruenza

	public Tb_impronta estraiImprontePerBid(String id) throws Exception {

		Tb_impronta tb_impronta = new Tb_impronta();
		tb_impronta.setBID(id);
		Tb_improntaResult tabella = new Tb_improntaResult(tb_impronta);
		tabella.executeCustom("selectPerBid");
		List lista = tabella.getElencoRisultati();
		tb_impronta = null;
		if (lista.size() != 0)
			tb_impronta = (Tb_impronta) lista.get(0);
		return tb_impronta;
	}

	public Tr_tit_mar estraiLegameMarca(String id) throws Exception {

		Tr_tit_mar tr_tit_mar = new Tr_tit_mar();
		tr_tit_mar.setBID(id);
		Tr_tit_marResult tabella = new Tr_tit_marResult(tr_tit_mar);
		tabella.executeCustom("selectPerTitolo");
		List lista = tabella.getElencoRisultati();
		tr_tit_mar = null;
		if (lista.size() != 0)
			tr_tit_mar = (Tr_tit_mar) lista.get(0);
		return tr_tit_mar;
	}

	public Tr_tit_aut estraiLegameAutore4(String id) throws Exception {

		Tr_tit_aut tr_tit_aut = new Tr_tit_aut();
		tr_tit_aut.setBID(id);
		Tr_tit_autResult tabella = new Tr_tit_autResult(tr_tit_aut);
		tabella.executeCustom("selectPerRespRelazione");
		List lista = tabella.getElencoRisultati();
		tr_tit_aut = null;
		if (lista.size() != 0)
			tr_tit_aut = (Tr_tit_aut) lista.get(0);
		return tr_tit_aut;
	}

	public Tb_numero_std estraiNumeriStandardPerBid(String id) throws Exception {

		Tb_numero_std tb_numero_std = new Tb_numero_std();
		tb_numero_std.setBID(id);
		Tb_numero_stdResult tabella = new Tb_numero_stdResult(tb_numero_std);
		tabella.executeCustom("selectPerBid");
		List lista = tabella.getElencoRisultati();
		tb_numero_std = null;
		if (lista.size() != 0)
			tb_numero_std = (Tb_numero_std) lista.get(0);
		return tb_numero_std;
	}

	public int estraiNumeriStandardPerBidMus(String id) throws Exception {

		Tb_numero_std tb_numero_std = new Tb_numero_std();
		tb_numero_std.setBID(id);
		Tb_numero_stdResult tabella = new Tb_numero_stdResult(tb_numero_std);
		tabella.executeCustom("selectCountPerBidMus");
	    int n = conta(tabella);
		tb_numero_std = null;
		return n;
	}

	// FINE Evolutiva Novembre 2015 - almaviva2 cambio Materiale da antico/moderno e viceversa:


	// almaviva2 Giugno 2016 - Nuovo Metodo per verifiche Numero Standard -
	protected void validaNumeroStandard2016(NumStdType[] numStd, String cd_natura, TipoRecord tp_record) throws EccezioneSbnDiagnostico{

		  if (numStd.length > 0) {  //presenza numeri standard in array
			  	for (int i = 0; i < numStd.length; i++) { //verifica esistenza in TB_CODICI
					String tp_NSTD = Decodificatore.getCd_tabella("NSTD", numStd[i].getTipoSTD());
					if(tp_NSTD == null)
						throw new EccezioneSbnDiagnostico(7021); //, "Numero standard Inesistente"

				}
	//CONTROLLI CONGRUENZA NATURA E TIPO RECORD PER TIPOLOGIA DI NUMERO STANDARD

				if (cd_natura.equals("N")) {  //controllo congruenza con natura
					  for (int i = 0; i < numStd.length; i++) {
						  if (numStd[i].getTipoSTD().equals("L") == false) {
						  throw new EccezioneSbnDiagnostico(3064); //, "Numero standard non ammesso"
						}
					  }
					} else {
					  if (!(cd_natura.equals("S") || cd_natura.equals("C") || cd_natura.equals("M") || cd_natura.equals("W")))
						throw new EccezioneSbnDiagnostico(3064); //, "Tipo numero standard non ammesso per la natura del documento"
					} //controllo congruenza con natura fine


			    // NATURE AMMESSE per NUMERO STANDARD
			    for (int i = 0; i < numStd.length; i++) {
			    	  if (numStd[i].getTipoSTD().equals("010")) {
			    		  if (!(cd_natura.equals("M") || cd_natura.equals("W"))){
			    			  throw new EccezioneSbnDiagnostico(3064, "  -  ISBN"); //, "Numero standard non ammesso" Tipo numero standard non ammesso per la natura del documento
			    		  }
			    	  }
					  if (numStd[i].getTipoSTD().equals("011")) {  //controlli 'ISSN'
						  if (!(cd_natura.equals("S") || cd_natura.equals("C"))){
							  throw new EccezioneSbnDiagnostico(3064, "  -  ISSN"); //, "Numero standard non ammesso"
						  }
					  }
					  // almaviva2 Bug mantis esercizio 6509
					  // adeguamento alle modifiche fatte in Indice
					  //da mail Aste 21/06/2017
					  //Confermiamo che il numero standard ACNP si possa inserire sia sulle nature S che sulle nature C
					  if (numStd[i].getTipoSTD().equals("P")) {  //controlli 'ACNP'
						  if (!(cd_natura.equals("S")|| cd_natura.equals("C"))){
							  throw new EccezioneSbnDiagnostico(3064, "  -  ACNP"); //, "Numero standard non ammesso"
						  }
					  }

					  if (numStd[i].getTipoSTD().equals("Z")) {  //controlli 'ISSN-L'
						  if (!(cd_natura.equals("S") || cd_natura.equals("C"))){
							 throw new EccezioneSbnDiagnostico(3064, "  -  ISSN-L"); //, "Numero standard non ammesso"
						  }
					  }

			    }
			    // TIPO_RECORD AMMESSI per NUMERO STANDARD
			    for (int i = 0; i < numStd.length; i++) {
			    	  if (numStd[i].getTipoSTD().equals("L")) {  //controlli 'numero di lastra'
			    		  if (tp_record != null &&  !tp_record.toString().equals("c")){
							throw new EccezioneSbnDiagnostico(2900, "Tipo numero standard NUMERO DI LASTRA non ammesso per questo tipo record");
			    		  }
			    	  }
					  if (numStd[i].getTipoSTD().equals("013")) {
						  if (tp_record != null &&  (!(tp_record.toString().equals("a")
								|| tp_record.toString().equals("c") || tp_record.toString().equals("j")
								|| tp_record.toString().equals("l"))) ){
							throw new EccezioneSbnDiagnostico(2900, "Tipo numero standard ISMN non ammesso per questo tipo record");
						  }
					  }
					  if (numStd[i].getTipoSTD().equals("X")) { // controlli 'RISM'
						  if (tp_record != null	&& (!(tp_record.toString().equals("c") || tp_record.toString().equals("d")))){
							  throw new EccezioneSbnDiagnostico(2900,	"Tipo numero standard RISM non ammesso per questo tipo record");
						  }
					  }

					  if (numStd[i].getTipoSTD().equals("Y")) {  //controlli 'SARTORI'
						  if (tp_record != null &&  tp_record.toString().equals("a")) {
						  } else {
							  throw new EccezioneSbnDiagnostico(2900, "Tipo numero standard SARTORI non ammesso per questo tipo record");
						  }
					  }
			    }
				for (int i = 0; i < numStd.length; i++) {  //controlli specifici
					// CONTROLLO SE NELLA NOTA NON SIA INDICAT LA PAROLA ERRORE IN CASO POSITIVO SALTO I CONTROLLI FORMALI DEI NUMERI STANDART
					boolean controlli = true;
					if(numStd[i].getNotaSTD() != null){
						String NOTA_NUM_STD = numStd[i].getNotaSTD().toUpperCase();
						if(NOTA_NUM_STD.contains("ERRATO")){
							controlli = false;
						}
					}
					if(controlli){
					if (numStd[i].getTipoSTD().equals("A")) {  //controlli 'numero edizione registrazioni sonore'
	//eliminati controlli congruenza secondo mail 04/02/2015
//						if (tp_record != null &&  (!(tp_record.toString().equals("i") || tp_record.toString().equals("j"))))
//							throw new EccezioneSbnDiagnostico(2900, "Tipo numero standard non ammesso per questo tipo record");
						if( (numStd[i].getNumeroSTD().length() > 0 ) && (numStd[i].getNumeroSTD().length() < 26 ) ){
							}else{
							throw new EccezioneSbnDiagnostico(2900, "Lunghezza Numero standard ACNP errata. La lunghezza deve essere compresa tra 1 e 25 caratteri");
						}
					} //controlli 'numero edizione registrazioni sonore' fine

					if (numStd[i].getTipoSTD().equals("020")) {  //controlli 'BNI'
						if (numStd[i].getNumeroSTD().length() > 10){
							throw new EccezioneSbnDiagnostico(2900, "Lunghezza Numero standard BNI errata. La lunghezza deve essere tra 1 e 10 caratteri");
						}
					} //controlli 'BNI' fine

					if (numStd[i].getTipoSTD().equals("E")) {  //controlli 'numero editoriale musica a stampa'
	//eliminati controlli congruenza secondo mail 04/02/2015
//						if (tp_record != null &&  !tp_record.toString().equals("c"))
//							throw new EccezioneSbnDiagnostico(2900, "Tipo numero standard non ammesso per questo tipo record");
						if( (numStd[i].getNumeroSTD().length() > 0 ) && (numStd[i].getNumeroSTD().length() < 26 ) ){
						}else{
							throw new EccezioneSbnDiagnostico(2900, "Lunghezza Numero standard NUMERO EDITORIALE  errata. La lunghezza deve essere compresa tra 1 e 25 caratteri");
						}
					} //controlli 'numero editoriale musica a stampa' fine

					if (numStd[i].getTipoSTD().equals("F")) {  //controlli 'numero matrice registrazioni sonore'
	//eliminati controlli congruenza secondo mail 04/02/2015
//						if (tp_record != null &&  (!(tp_record.toString().equals("i") || tp_record.toString().equals("j"))))
//							throw new EccezioneSbnDiagnostico(2900, "Tipo numero standard non ammesso per questo tipo record");
						if( (numStd[i].getNumeroSTD().length() > 0 ) && (numStd[i].getNumeroSTD().length() < 26 ) ){
						}else{
							throw new EccezioneSbnDiagnostico(2900, "Lunghezza Numero standard NUMERO MATRICE errata. La lunghezza deve essere compresa tra 1 e 25 caratteri");
						}
					} //controlli 'numero matrice registrazioni sonore' fine

					if (numStd[i].getTipoSTD().equals("022")) {  //controlli 'numero pubblicazione governativa'
						if( (numStd[i].getNumeroSTD().length() > 0 ) && (numStd[i].getNumeroSTD().length() < 26 ) ){
						}else{
							throw new EccezioneSbnDiagnostico(2900, "Lunghezza Numero standard NUMERO PUBBLICAZIONE errata. La lunghezza deve essere compresa tra 1 e 25 caratteri");
						}
					} //controlli 'numero pubblicazione governativa fine

					if (numStd[i].getTipoSTD().equals("H")) {  //controlli 'numero videoregistrazione'
	//eliminati controlli congruenza secondo mail 04/02/2015
//						if (tp_record != null &&  !tp_record.toString().equals("g"))
//							throw new EccezioneSbnDiagnostico(2900, "Tipo numero standard non ammesso per questo tipo record");
						if( (numStd[i].getNumeroSTD().length() > 0 ) && (numStd[i].getNumeroSTD().length() < 26 ) ){
						}else{
							throw new EccezioneSbnDiagnostico(2900, "Lunghezza Numero standard NUMERO VIDEOREGISTRAZIONE errata. La lunghezza deve essere compresa tra 1 e 25 caratteri");
						}
					} //controlli 'numero videoregistrazione' fine

					if (numStd[i].getTipoSTD().equals("010")) {  //controlli 'ISBN'
//						if (rimuoviTrattini(numStd[i].getNumeroSTD()).length() > 13) {
//							throw new EccezioneSbnDiagnostico(3328); //, "Numero standard troppo lungo"
//						}
						if( (rimuoviTrattini(numStd[i].getNumeroSTD()).length() == 10 ) ||
							(rimuoviTrattini(numStd[i].getNumeroSTD()).length() == 13 ) ){
						}else{
							// UN ISBN puï¿½ essere solo 10 o 13 chr
							throw new EccezioneSbnDiagnostico(2900, "Lunghezza Numero ISBN errata. La lunghezza deve essere di 10 caratteri o 13 caratteri");
						} //controllo lunghezza '010' fine
						if ( rimuoviTrattini(numStd[i].getNumeroSTD()).length() == 13 ){
							if( (!numStd[i].getNumeroSTD().startsWith("978")) && (!numStd[i].getNumeroSTD().startsWith("979")) ){
								// un  ISBN di 13 chr puï¿½ iniziare solo con 978 o 979
								throw new EccezioneSbnDiagnostico(2900, "Numero ISBN errato. Prefisso 979 / 978 non presente");
							}
						}
						String TpSTD = numStd[i].getTipoSTD().toString();
						String NumSTD = numStd[i].getNumeroSTD();

//						String patternA = "[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]";
						String patternA = "[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9xX]"; // 11/12/14 almaviva7
//						String patternB = "[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]";
						String patternB = "[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9xX]"; //23/01/2015 almaviva4
						String patternC = "M[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]";
						org.apache.oro.text.regex.Perl5Compiler compiler = new org.apache.oro.text.regex.Perl5Compiler();
						org.apache.oro.text.regex.Perl5Matcher matcher = new org.apache.oro.text.regex.Perl5Matcher();
						Pattern pattern = null;
						try {
							   pattern = compiler.compile(patternA);
							 } catch(MalformedPatternException e) {
							   System.out.println("Bad pattern.");
							 }
							boolean foundA  = matcher.matches(NumSTD, pattern);
							try {
							   pattern = compiler.compile(patternB);
							 } catch(MalformedPatternException e) {
							   System.out.println("Bad pattern.");
							 }
							boolean foundB  = matcher.matches(NumSTD, pattern);
							if( (foundA) || (foundB) ){ // TUTTO OK
							} else {
								throw new EccezioneSbnDiagnostico(2900, "Numero ISBN errato. Ammessi solo caratteri numerici\n");
							}
//						if (!(cd_natura.equals("M") || cd_natura.equals("W")))
//							throw new EccezioneSbnDiagnostico(3064); //, "Numero standard non ammesso"
						} //controlli 'ISBN' fine

					if (numStd[i].getTipoSTD().equals("011")) {  //controlli 'ISSN'
//						if (!(cd_natura.equals("S") || cd_natura.equals("C")))
//							throw new EccezioneSbnDiagnostico(3064); //, "Numero standard non ammesso"
//						if (rimuoviTrattini(numStd[i].getNumeroSTD()).length() > 8) {
//							throw new EccezioneSbnDiagnostico(3388); //, "Numero standard troppo lungo"
//						  }
						if(rimuoviTrattini(numStd[i].getNumeroSTD()).length() == 8 ) {
						}else{
							// UN ISSN puï¿½ essere solo 8 chr
							throw new EccezioneSbnDiagnostico(2900, "Lunghezza Numero ISSN errata. La lunghezza deve essere di 8 caratteri");
						}
					} //controlli '011' fine

					if (numStd[i].getTipoSTD().equals("L")) {  //controlli 'numero di lastra'
//						if (tp_record != null &&  !tp_record.toString().equals("c"))
//							throw new EccezioneSbnDiagnostico(2900, "Tipo numero standard non ammesso per questo tipo record");
						if( (numStd[i].getNumeroSTD().length() > 0 ) && (numStd[i].getNumeroSTD().length() < 26 ) ){
						}else{
							throw new EccezioneSbnDiagnostico(2900, "Lunghezza Numero standard NUMERO DI LASTRA errata. La lunghezza deve essere compresa tra 1 e 25 caratteri");
						}
					} //controlli 'numero di lastra' fine

					if (numStd[i].getTipoSTD().equals("013")) {  //controlli 'ISMN'
//						if (tp_record != null &&  !tp_record.toString().equals("c")) //20/01/2015 MGT
						if (tp_record != null &&  (!(tp_record.toString().equals("a")
								|| tp_record.toString().equals("c") || tp_record.toString().equals("j")
								|| tp_record.toString().equals("l"))) )
							throw new EccezioneSbnDiagnostico(2900, "Tipo numero standard ISMN non ammesso per questo tipo record");
//						if (rimuoviTrattini(numStd[i].getNumeroSTD()).length() > 13) {
//							throw new EccezioneSbnDiagnostico(3328); //, "Numero standard troppo lungo"
//						  }
						if( (rimuoviTrattini(numStd[i].getNumeroSTD()).length() == 10 ) ||
								(rimuoviTrattini(numStd[i].getNumeroSTD()).length() == 13 ) ){
						}else{
							// UN ISMN puï¿½ essere solo 10 o 13 chr
							throw new EccezioneSbnDiagnostico(2900, "Lunghezza Numero ISMN errata. La lunghezza deve essere di 10 caratteri o 13 caratteri");
						}

						String TpSTD = numStd[i].getTipoSTD().toString();
						String NumSTD = numStd[i].getNumeroSTD();
						String patternA = "[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]";
						String patternB = "[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]";
						String patternC = "M[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]";
						org.apache.oro.text.regex.Perl5Compiler compiler = new org.apache.oro.text.regex.Perl5Compiler();
						org.apache.oro.text.regex.Perl5Matcher matcher = new org.apache.oro.text.regex.Perl5Matcher();
						Pattern pattern = null;
						try {
						   pattern = compiler.compile(patternC);
						 } catch(MalformedPatternException e) {
						   System.out.println("Bad pattern.");
						 }
						boolean foundC  = matcher.matches(NumSTD, pattern);
						try {
						   pattern = compiler.compile(patternB);
						 } catch(MalformedPatternException e) {
						   System.out.println("Bad pattern.");
						 }
						boolean foundB  = matcher.matches(NumSTD, pattern);
						if( (foundC) || (foundB) ){
						}else{
							throw new EccezioneSbnDiagnostico(2900, "Numero ISMN errato. Forme ammesse 979+10 caratteri numerici o M+9 caratteri numerici\n");
						}
						if ( rimuoviTrattini(NumSTD).length() == 13){
							if(!NumSTD.startsWith("979")) {
								// un  ISMN di 13 chr deve iniziare con 979
								throw new EccezioneSbnDiagnostico(2900, "Numero ISMN errato. Prefisso 979 non presente");
							}
						}
						if ( rimuoviTrattini(NumSTD).length() == 10){
							if (!NumSTD.toUpperCase().startsWith("M")) {
								// un  ISMN di 10 chr deve iniziare con "M"
								throw new EccezioneSbnDiagnostico(2900, "Numero ISMN errato. Prefisso M non presente");
							}
						}
					} //controlli 'ISMN' fine

					if (numStd[i].getTipoSTD().equals("O")) {  //controlli 'numero di risorsa elettronica'
						if( (numStd[i].getNumeroSTD().length() > 0 ) && (numStd[i].getNumeroSTD().length() < 26 ) ){
						}else{
							throw new EccezioneSbnDiagnostico(2900, "Lunghezza Numero standard NUMERO RISORSA ELETTRONICA errata. La lunghezza deve essere compresa tra 1 e 25 caratteri");
						}
					} //controlli 'numero di risorsa elettronica' fine

					if (numStd[i].getTipoSTD().equals("P")) {  //controlli 'ACNP'
//						if (!cd_natura.equals("S"))
//							throw new EccezioneSbnDiagnostico(3064); //, "Numero standard non ammesso"
						if (numStd[i].getNumeroSTD().length() != 10){
							throw new EccezioneSbnDiagnostico(2900, "Lunghezza Numero standard ACPN errata. La lunghezza deve essere di 10 caratteri");
						}
						if (numStd[i].getNumeroSTD().length() == 10){
							if (!numStd[i].getNumeroSTD().toUpperCase().startsWith("P")) {
								// un  ACNP deve iniziare con "P"
								throw new EccezioneSbnDiagnostico(2900, "Numero ACNP errato. Prefisso P non presente");
							}
						}
					} //controlli 'ACNP' fine

					if (numStd[i].getTipoSTD().equals("Q")) {  //controlli 'UPC'
						if (numStd[i].getNumeroSTD().length() != 12){
							throw new EccezioneSbnDiagnostico(2900, "Lunghezza Numero standard UCP errata. La lunghezza deve essere maggiore di 11 caratteri");
						}
					} //controlli 'UPC' fine

					if (numStd[i].getTipoSTD().equals("T")) {  //controlli 'EAN'
						if (numStd[i].getNumeroSTD().length() != 13){
							throw new EccezioneSbnDiagnostico(2900, "Lunghezza Numero standard EAN errata. La lunghezza deve essere di 13 caratteri");
						}
					} //controlli 'EAN' fine

					if (numStd[i].getTipoSTD().equals("U")) {  //controlli 'CUBI'
						if (numStd[i].getNumeroSTD().length() > 10){
							throw new EccezioneSbnDiagnostico(2900, " Lunghezza Numero standard CUBI errata. La lunghezza deve essere tra 1 e 10 caratteri");
						}
					} //controlli 'CUBI' fine

					if (numStd[i].getTipoSTD().equals("V")) {  //controlli 'ISRC'
	//eliminati controlli congruenza secondo mail 04/02/2015
//						if (tp_record != null &&  (!(tp_record.toString().equals("g") || tp_record.toString().equals("i") || tp_record.toString().equals("j"))))
//							throw new EccezioneSbnDiagnostico(2900, "Tipo numero standard non ammesso per questo tipo record");
						if(rimuoviTrattini(numStd[i].getNumeroSTD()).length() == 12 ) {
						}else{
							throw new EccezioneSbnDiagnostico(2900, " Lunghezza Numero ISRC errata. La lunghezza deve essere di 12 caratteri");
						}
					} //controlli 'ISRC' fine

					if (numStd[i].getTipoSTD().equals("X")) {  //controlli 'RISM'
						if (tp_record != null &&  (!(tp_record.toString().equals("c") || tp_record.toString().equals("d"))))
							throw new EccezioneSbnDiagnostico(2900, "Tipo numero standard non ammesso per questo tipo record");
						if( (numStd[i].getNumeroSTD().length() > 0 ) && (numStd[i].getNumeroSTD().length() < 26 ) ){
						}else{
							throw new EccezioneSbnDiagnostico(2900, " Lunghezza Numero standard RISM errata. La lunghezza deve essere compresa tra 1 e 25 caratteri");
						}
					} //controlli 'RISM' fine

					if (numStd[i].getTipoSTD().equals("Y")) {  //controlli 'SARTORI'
						//solo per libretti a stampa
						//tipo record 'a', a_105_11 = 'i' (per moderno), a_140_17 = 'da' (per antico)
						if (tp_record != null &&  tp_record.toString().equals("a")) {
//							if (datiDoc.getT105bis() != null) {
//								if (!datiDoc.getT105bis().getA_105_11().equals("i"))
//									throw new EccezioneSbnDiagnostico(2900, "Tipo numero standard non ammesso");
//							} else if (datiDoc.getT140bis() != null){
//								if (!datiDoc.getT140bis().getA_140_17().equals("da"))
//									throw new EccezioneSbnDiagnostico(2900, "Tipo numero standard non ammesso");
//							} else {
//								throw new EccezioneSbnDiagnostico(2900, "Tipo numero standard non ammesso");
//							}
						} else {
							throw new EccezioneSbnDiagnostico(2900, "Tipo numero standard non ammesso per questo tipo record");
						}
						// Maggio 2018 almaviva2 Bug 6608 - modifica riportata da Indice
						//come richiesto da mail Aste del 30/08/2016 il numero standard Sartori viene portato a max 6 caratteri
						if (numStd[i].getNumeroSTD().length() > 6){
							throw new EccezioneSbnDiagnostico(2900, "Lunghezza Numero standard SARTORI errata. La lunghezza deve essere tra 1 e 6 caratteri");
						}
					} //controlli 'SARTORI' fine

					if (numStd[i].getTipoSTD().equals("Z")) {  //controlli 'ISSN-L'
//						if (!(cd_natura.equals("S") || cd_natura.equals("C")))
//							throw new EccezioneSbnDiagnostico(3064); //, "Numero standard non ammesso"
//						if (rimuoviTrattini(numStd[i].getNumeroSTD()).length() > 8) {
//							throw new EccezioneSbnDiagnostico(3388); //, "Numero standard troppo lungo"
//						  }
						if( rimuoviTrattini(numStd[i].getNumeroSTD()).length() == 8 ) {
						}else{
							// UN ISSN puo' essere solo 8 chr
							throw new EccezioneSbnDiagnostico(2900, "Lunghezza Numero ISSN-L errata. La lunghezza deve essere di 8 caratteri");
						}
					} //controlli 'ISSN-L' fine

					if (numStd[i].getPaeseSTD() != null) {
						  if (!numStd[i].getTipoSTD().equals("020"))
							throw new EccezioneSbnDiagnostico(3063); //, "Numero standard malformato"
						  if (Decodificatore.getCd_tabella("Tb_titolo", "cd_paese", numStd[i].getPaeseSTD()) == null)
							throw new EccezioneSbnDiagnostico(3205); //, "Codice paese del numero standard malformato"
						}
				} //controlli specifici fine
				}
		  } //presenza numeri standard in array fine


	}

}
