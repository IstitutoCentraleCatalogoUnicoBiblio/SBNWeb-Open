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
/*
 * Marc4cppDocumento.cpp
 *
 *  Created on: 29-dic-2008
 *      Author: Arge
 */
#include <stdio.h>
#include <stdlib.h>

#include "Marc4cppDocumento.h"
#include "BinarySearch.h"
#include "library/CMisc.h"
#include <time.h>
#include "../include/library/CString.h"
#include "TbfBibliotecaInPolo.h"
#include "MarcGlobals.h"

#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif


#ifdef DEBUG_ARGE
	extern	int	eccSoloTondaApertaCtr;
	extern	int	eccSoloTondaChiusaCtr;
	extern	int	eccDataNonPrecedutaDaVirgolaCtr;
	extern	int	eccTestoDopoVirgolaSpazioCtr;
	extern	int	eccParentesiTondeNonSottoarea;
	extern	int	dataPrecedutaDaVirgolaCtr;
#endif


//#include "C210.h"
extern void SignalAnError(const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(const OrsChar *Module, OrsInt Line,	const OrsChar * MsgFmt, ...);


/*
  DAL MANUALE UNIMARC
  	$a Title proper I.I New area
	$a (repeated) Title proper by the same author 1.6
	$b General material designation I.2 [ ]
	$c Title proper by another author 1.6
	$d Parallel title proper 1.3
	$e Other title information 1.4
	$f First statement ofresponsibility 1.5
	$g Subsequent statement of responsibility 1.5
	$h N umber of a part I. 1.4 ISI3D(S)
	$i Name of a part 1.1.4ISBD(S) , if after $h, else.

	Grammatica area titolo:
	Occorrenze 	* =0 o piu,
				1 = 1 o piu,
				? = 0 o 1,
				+ = 1 o +
				. = qualsiasi carattere


VERDONE
Punteggiatura
A. La prima parola del titolo (articolo escIuso), e sempre preceduta da asterisco (*).
B. Per gli spazi da osservare prima e dopo la punteggiatura convenzionale, vedi OEI.
C. Ogni complemento del titolo e preceduto da due punti ( : ).
D. La prima parola del complemento del titolo ritenuto significativo e preceduta da asterisco (*).
E. La prima indicazione di responsabilita che segue un titolo e preceduta da una barra diagonale ( I).
F. Le successive indicazioni di responsabilita sono precedute ciascuna da punto e virgola ( ; ),
    a meno che non costituiscano un'unica frase, nel qual caso si usa l'interpunzione piu appropriata.
    Qualora piu persone o enti svolgano la stessa funzione e non siano uniti da
    congiunzioni o simili, il segno di interpunzione usato sara la virgola (, ).
G. I titoIi di opere distinte, anonime 0 di autori diversi, contenute in una  stessa pubblicazione,
	sono separati da spazio, punto, spazio ( . ),
    a meno che non siano uniti da una parola o frase di collegamento.
I. titoli di opere distinte di uno stesso autore contenuti in una stessa  pubblicazione sono separati da punto e virgola ( ; ).
    Nei titoli costituiti da titolo comune e titolo dipendente, I'eventuale designazione di titolo dipendente 0 il titolo dipendente
    sono separati dal titolo comune da punto (. ).
L. II titolo dipendente che segue una designazione di titolo dipendente e preceduto da virgola (, ).




	AreaTitolo:	Titolo, TitoliOpereDistinteDiversoAutore?


	Titolo									TitoloProprio, Titolo parallelo*, FormulazioneDiResponsabilit�?

	TitoloProprio:							AreaNoSort*, Data, TitoliOpereDistinteStessoAutore* DesignazioneGeneraleDelMateriale ?, ComplementoDelTitolo?

	AreaNoSort								".+\*"

	DesignazioneGeneraleDelMateriale:		" ["Data"] "

	Titolo parallelo						" = "AreaNoSort, Data, ComplementoDelTitolo?

	ComplementoDelTitolo					" : "Data

	FormulazioneDiResponsabilit�			PrimaFormulazioneDiResponsabilit�?,

	PrimaFormulazioneDiResponsabilit�		" / "Data, SuccessivaFormulazioneDiResponsabilit�?

	SuccessivaFormulazioneDiResponsabilit�	" ; "Data

	TitoliOpereDistinteStessoAutore			" ; "Data

	TitoliOpereDistinteDiversoAutore		" . "Titolo

*/

// 28/01/2011 Mantis 4175 Riscritto metodo!

//void Marc4cppDocumento::creaTag200_NonAnticoNew(DataField *df, char *area200, int len, char natura) {
void Marc4cppDocumento::creaTag200_NonAnticoNew(DataField *df, CString *areaTitolo, char natura) {

	CString a200, *sPtr;
	CString dest;

	char chr;
	long from = 0; // , start
	ATTValVector<CString *> opereDistintAutoriDiversiVect;
	ATTValVector<CString *> titoloResponsabilitaVect;

	bool retb;

	char subfieldIdTitolo = '?';
//	a200.assign(area200, len);
	a200.assign(areaTitolo);

	// Troviamo i titoli di opere distinte e di autori diversi
	retb = a200.Split(opereDistintAutoriDiversiVect,  PUNTEGGIATURA_AREA_TITOLO_INIZIO_TITOLO_AUTORE_DIVERSO); //". "
	// Gestiamo gli altri titoli di opere distinte e di autori diversi
	for (int i=0; i < opereDistintAutoriDiversiVect.Length(); i++)
	{
		if (!i)
			subfieldIdTitolo = 'a';
		else
			subfieldIdTitolo = 'c';
		sPtr = opereDistintAutoriDiversiVect.Entry(i);
		from = 0;
		dest.Clear();
		int stato = STATO_INIZIO_TITOLO;

		// Suddividiamo il titolo dalla responsabilita'
		retb = sPtr->Split(titoloResponsabilitaVect, PUNTEGGIATURA_AREA_TITOLO_INIZIO_PRIMA_FORMULAZIONE_RESPONSABILITA);
		sPtr = titoloResponsabilitaVect.Entry(0);




		// Lavoriamo sul titolo
		// --------------------
		do {
		// E' un titolo composto? Eg. Gli *acamesi ; Le nuvole ; Le vespe ; Gli uccelli / Aristofane
        chr = sPtr->GetChar(from);

// Mantis 4836 20/01/2012. Confermato da Paolucci/Contardi a dispetto delle direttive Unimarc
//        if (stato == STATO_INIZIO_TITOLO && sPtr->StartsWithFrom(PUNTEGGIATURA_AREA_TITOLO_INIZIO_DESIGANZIONE_MATERIALE, from)
//        		&& DATABASE_ID != DATABASE_INDICE) // 24/09/2010 indice
//        {
//    		if (!i)
//    			subfieldIdTitolo = 'a';
//    		else
//    			subfieldIdTitolo = 'c';
//
//            addSubfield(df, subfieldIdTitolo, dest); // Salviamo titolo proprio
//            stato = STATO_IN_DESIGANZIONE_MATERIALE;
//            from += 2; // skip " ["
//            continue;
//        }
//        else
        	if (stato == STATO_IN_DESIGANZIONE_MATERIALE)
            {
            if (sPtr->StartsWithFrom(PUNTEGGIATURA_AREA_TITOLO_FINE_DESIGANZIONE_MATERIALE, from)
                    || sPtr->StartsWithFrom("!", from) // al posto dell ]
            ) // chr == "]"
                {
                //dest.ExtractLastChar(); // Mantis 4189
                addSubfield(df, 'b', dest);
                stato = STATO_INIZIO_TITOLO;
                from += 1; // skip " ["
                continue;
                }
            else
                from ++;
//            continue; // Mantis 4189
            }
        else if (  stato == STATO_INIZIO_TITOLO &&
                    sPtr->StartsWithFrom(PUNTEGGIATURA_AREA_TITOLO_INIZIO_TITOLO_PARALLELO, from)) // " = "
        {
            if (stato == STATO_INIZIO_TITOLO)
            {
        		if (!i)
        			subfieldIdTitolo = 'a';
        		else
        			subfieldIdTitolo = 'c';

                addSubfield(df, subfieldIdTitolo, dest);
            }
            stato = STATO_IN_TITOLO_PARALLELO;
            from += 3; // skip " = "
            continue;
        }
        else if ((  stato == STATO_INIZIO_TITOLO ||
                    stato == STATO_IN_COMPLEMENTO_TITOLO ||
                    stato == STATO_IN_TITOLO_PARALLELO ) &&
                sPtr->StartsWithFrom(PUNTEGGIATURA_AREA_TITOLO_INIZIO_ALTRO_TITOLO_STESSO_AUTORE, from)) // " ; "
        {
            // Salviamo titolo proprio
            if (stato == STATO_INIZIO_TITOLO)
            {
        		if (!i)
        			subfieldIdTitolo = 'a';
        		else
        			subfieldIdTitolo = 'c';

                addSubfield(df, subfieldIdTitolo, dest);
            }

            else if (stato == STATO_IN_COMPLEMENTO_TITOLO)
                addSubfield(df, 'e', dest);

            else if (stato == STATO_IN_TITOLO_PARALLELO)
                addSubfield(df, 'd', dest);

            stato = STATO_INIZIO_TITOLO;

            from += 3; // skip " ; "
            continue;
        }
        else if ((stato == STATO_INIZIO_TITOLO || stato == STATO_IN_TITOLO_PARALLELO || stato == STATO_IN_COMPLEMENTO_TITOLO) &&
                sPtr->StartsWithFrom(PUNTEGGIATURA_AREA_TITOLO_INIZIO_COMPLEMENTO_TITOLO, from)) // " : "
        {
            // Se stavamo facendo il titolo salviamolo
            if (stato == STATO_INIZIO_TITOLO)
            {
        		if (!i)
        			subfieldIdTitolo = 'a';
        		else
        			subfieldIdTitolo = 'c';
                addSubfield(df, subfieldIdTitolo, dest); // Salviamo titolo proprio
            }
            else if (stato == STATO_IN_TITOLO_PARALLELO)
                addSubfield(df, 'd', dest);
            else if (stato == STATO_IN_COMPLEMENTO_TITOLO)
                addSubfield(df, 'e', dest);

            from += 3; // skip " : "
            stato = STATO_IN_COMPLEMENTO_TITOLO;
            continue;
        }

        else if (chr == '<') // inizio qualificazione
        {
            // Nessuna gestione per le qualificazioni
            from++;
            dest.AppendChar(chr);

            while (chr = sPtr->GetChar(from))
            {
                if (chr == '>') // fine qualificazione
                    break;
                from++;
                dest.AppendChar(chr);
            }
            continue;
        }

        else
            from++;
        dest.AppendChar(chr);
		} while (from < sPtr->Length());


    	if (stato == STATO_INIZIO_TITOLO)
    	{
    		if (!i)
    			subfieldIdTitolo = 'a';
    		else
    			subfieldIdTitolo = 'c';

    		addSubfield(df, subfieldIdTitolo, dest); // Salviamo titolo proprio
    	}
    	else if (stato == STATO_IN_TITOLO_PARALLELO)
    		addSubfield(df, 'd', dest); // Salviamo titolo parallelo
    	else if (stato == STATO_IN_COMPLEMENTO_TITOLO)
    		addSubfield(df, 'e', dest);

		// Fine lavoriamo sul titolo




		// Lavoriamo sulla responsabilita'
		// -------------------------------
    	if (titoloResponsabilitaVect.length() > 1)
    	{

		sPtr = titoloResponsabilitaVect.Entry(1);
		stato = STATO_IN_PRIMA_FORMULAZIONE_RESPONSABILITA;
		from = 0;
		dest.Clear();
		do {
	        chr = sPtr->GetChar(from);
	        if (stato == STATO_IN_PRIMA_FORMULAZIONE_RESPONSABILITA  &&
	                sPtr->StartsWithFrom(PUNTEGGIATURA_AREA_TITOLO_INIZIO_SUCCESSIVA_FORMULAZIONE_RESPONSABILITA, from)) // " ; "
	        {
	            if (stato == STATO_IN_PRIMA_FORMULAZIONE_RESPONSABILITA)
	                addSubfield(df, 'f', dest);

	            from += 3; // skip " / "
	            stato = STATO_IN_SUCCESSIVA_FORMULAZIONE_RESPONSABILITA;
	            continue;
	        }

	        else if (sPtr->StartsWithFrom(PUNTEGGIATURA_AREA_TITOLO_INIZIO_SUCCESSIVA_FORMULAZIONE_RESPONSABILITA, from)) // " ; "
	        {
                addSubfield(df, 'g', dest);
	            from += 3; // skip " / "
	            // Non si cambia stato perche' e' solo un'altra occorrenza dello stesso stato
	            continue;
	        }

	        else if (chr == '<') // inizio qualificazione
	        {
	            // Nessuna gestione per le qualificazioni
	            from++;
	            dest.AppendChar(chr);

	            while (chr = sPtr->GetChar(from))
	            {
	                if (chr == '>') // fine qualificazione
	                    break;
	                from++;
	                dest.AppendChar(chr);
	            }
	            continue;
	        }

	        else
	            from++;
	        dest.AppendChar(chr);

		} while (from < sPtr->Length());

		if (stato == STATO_IN_PRIMA_FORMULAZIONE_RESPONSABILITA)
			addSubfield(df, 'f', dest);
		else if (STATO_IN_SUCCESSIVA_FORMULAZIONE_RESPONSABILITA)
			addSubfield(df, 'g', dest);
    	} // Abbioamo delle responsabilit'?


		// Fine Lavoriamo sulla responsabilita
		titoloResponsabilitaVect.DeleteAndClear();
	} // End for Titoli di autori diversi

	opereDistintAutoriDiversiVect.DeleteAndClear();
} // end creaTag200_NonAnticoNew



//void Marc4cppDocumento::creaTag200_AnticoNew(DataField *df, char *area200, int len) {
void Marc4cppDocumento::creaTag200_AnticoNew(DataField *df, CString *areaTitolo) {

	char *ptr1 = strstr(areaTitolo->data(), " / "); // area200
	Subfield *sf;

	if (ptr1) {
		*ptr1 = 0;
//		c200->addA200(new CString(area200));
		sf = new Subfield('a', areaTitolo->data(), ptr1-areaTitolo->data());
		df->addSubfield(sf);

		ptr1 += 3;
//		sf = new Subfield('f', ptr1, len - (ptr1-area200));
		sf = new Subfield('f', ptr1, areaTitolo->Length() - (ptr1-areaTitolo->data()));
		//sf->setData();
		df->addSubfield(sf);
	} else {
//		c200->addA200(new CString(area200));
//		sf = new Subfield('a', area200, len);
		sf = new Subfield('a', areaTitolo);
		//sf->setData();
		df->addSubfield(sf);

	}

} // End  creaTag200Antico


/*!

\brief <b>Tag 200 - Titolo e indicazione di responsabilita' (area 1 dell'ISBD)</b>

 <table>
 <tr>
 <th valign=top>Definizione</th><th>Descrizione</th>
 </tr>

 <tr><td valign=top>Tag</td><td>200</td></tr>
 <tr><td valign=top>Descrizione</td>
	 <td>Titolo e indicazione di responsabilita' (area 1 dell'ISBD)</td></tr>
 <tr><td valign=top>Obbligatorieta'</td>
	 <td>Obbligatorio</td></tr>
 <tr><td valign=top>Ripetibilita'</td>
	 <td>Non Ripetibile</td></tr>
 <tr><td valign=top>Indicatore 1</td>
	 <td>\li 0 titolo non significativo
		 \li 1 titolo significativo</td></tr>
 <tr><td valign=top>Indicatore 2</td>
	 <td>Non definito</td></tr>
  <tr><td valign=top>Sottocampi</td>
	 <td>
		\li a - titolo proprio. Ripetibile.
		\li b - designazione generale del materiale. Ripetibile.
		\li c - titolo proprio di altro autore. Ripetibile.
		\li d - titolo parllelo. Ripetibile.
		\li e - complemento del titolo. Ripetibile.
		\li f - prima indicazione di responsabilita'. Ripetibile.
		\li g - seconda e successive indicazioni di responsabilita'. Ripetibile.
		\li h - numero di una parte. Ripetibile.
		\li i - nome di una parte. Ripetibile.
		\li v - indicazione di volume. Non Ripetibile.
		\li z - lingua del titolo parallelo. Ripetibile.
	 </td></tr>
  <tr><td valign=top>NOTE</td><td></td></tr>

 </table>
 */

//DataField * Marc4cppDocumento::creaTag200_AreaTitoloEResponsabilita(char *areaStartPtr, char *areaEndPtr) {
DataField * Marc4cppDocumento::creaTag200_AreaTitoloEResponsabilita(CString *areaTitolo) {
	DataField *df = 0;
	string str;
	Subfield *sf;

	// 23/12/2015 Gestione .. -> ...
	if (areaTitolo->EndsWith(" .."))
		areaTitolo->AppendChar('.');


	//I manoscritti musicali vanno trattati diversamente: tutto in A200
//	char *bid = tbTitolo->getField(tbTitolo->bid_link);
	char *bid = tbTitolo->getField(tbTitolo->bid); // 10/04/2015


	char natura = *(tbTitolo->getField(tbTitolo->cd_natura));
	char tipoRecordUni = *(tbTitolo->getField(tbTitolo->tp_record_uni));
	char tipoMateriale = *(tbTitolo->getField(tbTitolo->tp_materiale));

//tbTitolo->dumpRecord();

	df = new DataField("200", 3);
	if (natura == 'w' || natura == 'W') // ma la naura w in sbn diventa m con 200 0 b
	{
		df->setIndicator1('0');
		df->setIndicator2(' ');
	} else {
		df->setIndicator1('1'); // indica che il titolo a' significativo
		df->setIndicator2(' ');
	}


	if (natura == 'D' || (tipoMateriale == 'U' && tipoRecordUni == 'D')) {
//		sf = new Subfield('a', areaStartPtr, areaEndPtr-areaStartPtr);
		sf = new Subfield('a', areaTitolo);

		//sf->setData();
		df->addSubfield(sf);

	} else if (*(bid + 3) == 'E') // Antico
//	} else if (*bid && *(bid + 3) == 'E') // Antico 10/04/2015
	{
		creaTag200_AnticoNew(df, areaTitolo); // areaStartPtr, areaEndPtr-areaStartPtr
	}
	else
		creaTag200_NonAnticoNew(df, areaTitolo, natura); // areaStartPtr, areaEndPtr-areaStartPtr




	// Controlliamo il NO SORTING BLOCK per la $a e $e
	CString *sPtr;
	long pos;
	sf = df->getSubfield('a');
	if (sf)
	{
		sPtr = sf->getDataString();
		if ((pos = sPtr->First('*')) != -1) // abbiamo almeno un asterisco?
		{
			if (pos) // Se non primo carattere
			{
				sPtr->Replace(pos, 1, NSE.data());
				sPtr->ChangeTo('*', ' '); // Replace all other *

				sPtr->PrependString(NSB.data());

			} else
				sPtr->ExtractFirstChar();
		}
	}
	else
	{
//		SignalAnError(__FILE__,__LINE__,"$a non ricostruita per BID=%s", tbTitolo->getField(tbTitolo->bid));
	}


	sf = df->getSubfield('e');
	if (sf)
	{
		sPtr = sf->getDataString();
		if ((pos = sPtr->First('*')) != -1) // abbiamo almeno un asterisco?
		{
			if (pos) // Se non primo carattere
			{
				sPtr->Replace(pos, 1, NSE.data());
				sPtr->PrependString(NSB.data());
			} else
				sPtr->ExtractFirstChar();
		}
	}

	return df;
} // End creaTag200




/*!
\brief <b>Tag 205 - Area dell'edizione</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>205</td></tr>
<tr><td valign=top>Descrizione</td><td>Area dell'edizione</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> a - indicazione di edizione. Non ripetibile.
        <li> b - indicazione aggiuntiva di edizione (volume). Ripetibile.
        <li> d - indicazione parallela di edizione. Ripetibile.
        <li> f - indicazione di responsabilita' relativa all'edizione. Ripetibile.
        <li> g - seconda e successive indicazioni di responsabilita' relative all'edizione. Ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td>I delimitatori non vengono riportati nei sottocampi.</td></tr>
</table>
 */
DataField * Marc4cppDocumento::creaTag205_AreaEdizione(char *areaStartPtr, char *areaEndPtr) {
	DataField *df = 0;

	if(!*areaStartPtr) // 205 vuoata  vedi bid MIL0551698
		return df;

	df = new DataField();
	df->setTag("205");

	C205 *c205 = new C205();

	int amu = isAnticoModernoUndefined();

	if (amu == 1) // Antico
		creaTag205Antico(c205, areaStartPtr);
	else if (amu == 2)
		creaTag205NonAntico(c205, areaStartPtr);
	else
	{
		// undefined!!!!
		SignalAnError(__FILE__,__LINE__,"creaTag205_AreaEdizione. Impossibile stabilire trattasi di antico o moderno: %s", tbTitolo->getField(tbTitolo->bid));
	}

	// Dalla c200 crea il datafield
	elabora205(df, c205);

	delete c205;
	return df;

} // End creaTag205


/*!
\brief <b>Tag 206 - Area materiale cartografico - Dati numerici</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>206</td></tr>
<tr><td valign=top>Descrizione</td><td>Area relativa al materiale specifico: Materiale cartografico - Dati numerici</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Obbligatorio per il materiale cartografico</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> a - Indicazione dati matematici. Ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppDocumento::creaTag206_AreaSpecificaDelMateriale(
		char *areaStartPtr, char *areaEndPtr) {
	DataField *df = 0;
	Subfield *sf;

	df = new DataField();
	df->setTag("206");
	sf = new Subfield('a', areaStartPtr, areaEndPtr-areaStartPtr); // a - Indicazione dati matematici
	//sf->setData();
	df->addSubfield(sf);
	return df;
} // End creaTag206

/*!
\brief <b>Tag 207 - Area periodici - nomi e date</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>207</td></tr>
<tr><td valign=top>Descrizione</td><td>Area relativa al materiale specifico: periodici - nomi e date</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Non Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Nomi e date formattate<BR>
 0 formattato<BR>
 1 non formattato
</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> a - Indicazione di date e/o volume. Area ISBD della numerazione. Ripetibile.
<!--
		<li> z - Fonte della numerazione
-->
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/



DataField * Marc4cppDocumento::creaTag207_AreaSpecificaMateriali_PeriodiciNomiDate(
		char *areaStartPtr, char *areaEndPtr) {
	DataField *df;
	Subfield *sf;
	CString s = areaStartPtr;
	ATTValVector<CString *> a207vect;
	long from = 1, start = 0;

	df = new DataField();
	df->setTag("207");

	df->setIndicator1(' ');
	df->setIndicator2('0'); // formattato

	CString *sPtr;
	while (s.StartsWithFrom(" ; ", from)) {
		start = from++;
		while (from < s.Length()) {
			if (s.StartsWithFrom(" ; ", from))
				break;
			from++;
		}
		sPtr = new CString(s.SubstringData(start, from - start));
		a207vect.Add(sPtr);
	} // End while (" ; ")
	sPtr = new CString(s.SubstringData(start));
	a207vect.Add(sPtr); // l'ultimo

	for (int i = 0; i < a207vect.length(); i++) {
		sf = new Subfield('a', a207vect.Entry(i)); // a - Indicazione di date e/o volume
		//sf->setData();
		df->addSubfield(sf);
	}

	a207vect.DeleteAndClear();
	return df;
} // End creaTag207





void Marc4cppDocumento::creaTag205Antico(C205 *c205, char *area205) {
	char *ptr1 = strstr(area205, " / ");
	CString *sPtr;
	if (ptr1) {
		*ptr1 = 0;
		sPtr = new CString(area205);
		c205->setA205(sPtr);
		sPtr = new CString(ptr1 + 3);
		c205->addF205(sPtr);
	} else {
		sPtr = new CString(area205);
		c205->setA205(sPtr);
	}

} // End  creaTag205Antico

void Marc4cppDocumento::creaTag205NonAntico(C205 *c205, char *area205) {
	CString s = area205;
	CString *sPtr;
	char chr;
	long from = 1, start;
	//	long pos;
	ATTValVector<CString *> stringVect;
	//	bool retb;

	while (from < s.Length()) {
		chr = s.GetChar(from);
		if (chr == '=' || s.StartsWithFrom(", ", from) || s.StartsWithFrom(
				" / ", from) || s.StartsWithFrom(" ; ", from))
			break;
		from++;
	}
	sPtr = new CString(s.SubstringData(0, from));
	c205->setA205(sPtr);
	while (s.StartsWithFrom(", ", from)) {
		from += 2;
		start = from;
		while (from < s.Length()) {
			if (s.StartsWithFrom(", ", from) || s.StartsWithFrom(" = ", from)
					|| s.StartsWithFrom(" / ", from) || s.StartsWithFrom(" ; ",
					from))
				break;
			from++;
		}
		sPtr = new CString(s.SubstringData(start, from - start));
		c205->addB205(sPtr);
	} // End while ", "


	//Questo non esiste piu'
	while (s.StartsWithFrom(" = ", from)) {
		start = from++;
		while (from < s.Length()) {
			chr = s.GetChar(from);
			if (s.StartsWithFrom(" = ", from) || s.StartsWithFrom(" / ", from)
					|| s.StartsWithFrom(" ; ", from))
				break;
			from++;
		}
		//c205->addD205(new CString (s.SubstringData(start, from-start)));
	} // End while (" = ")

	sPtr;
	while (s.StartsWithFrom(" / ", from)) {
		from += 3;
		start = from;
		while (from < s.Length()) {
			if (s.StartsWithFrom(" / ", from) || s.StartsWithFrom(" ; ", from))
				break;
			from++;
		}
		sPtr = new CString(s.SubstringData(start, from - start));
		c205->addF205(sPtr);
	} // End while " / "

	while (s.StartsWithFrom(" ; ", from)) {
		from += 3;
		start = from;
		while (from < s.Length()) {
			if (s.StartsWithFrom(" ; ", from))
				break;
			from++;
		}
		sPtr = new CString(s.SubstringData(start, from - start));
		c205->addG205(sPtr);
	} // End while " ; "

} // End creaTag205NonAntico

void Marc4cppDocumento::elabora205(DataField *df, C205 *c205) {
	ATTValVector<CString *> *vectPtr;
	CString *sPtr;
	Subfield *sf;
	int i;

	sPtr = c205->getA205();
	if (sPtr) {
		sf = new Subfield('a', sPtr); // a - indicazione di edizione
		//sf->setData();
		df->addSubfield(sf);
	} else
		return;
	vectPtr = c205->getB205Vect();
	for (i = 0; i < vectPtr->length(); i++) {
		sPtr = vectPtr->Entry(i);
		sf = new Subfield('b', sPtr); // b - indicazione aggiuntiva di edizione (volume).
		//sf->setData();
		df->addSubfield(sf);
	}

	vectPtr = c205->getD205Vect();
	for (i = 0; i < vectPtr->length(); i++) {
		sPtr = vectPtr->Entry(i);
		sf = new Subfield('d', sPtr); // d - indicazione parallela di edizione
		//sf->setData();
		df->addSubfield(sf);
	}

	vectPtr = c205->getF205Vect();
	for (i = 0; i < vectPtr->length(); i++) {
		sPtr = vectPtr->Entry(i);
		sf = new Subfield('f', sPtr); // f - indicazione di responsabilita' relativa all'edizione
		//sf->setData();
		df->addSubfield(sf);
	}
	vectPtr = c205->getG205Vect();
	for (i = 0; i < vectPtr->length(); i++) {
		sPtr = vectPtr->Entry(i);
		sf = new Subfield('g', sPtr); // g - seconda e successive indicazioni di responsabilita' relative all'edizione
		//sf->setData();
		df->addSubfield(sf);
	}
} // End elabora205

/*!
\brief <b>Tag 208 - Area specifica della musica a stampa</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>208</td></tr>
<tr><td valign=top>Descrizione</td><td>Area relativa al materiale specifico: musica a stampa</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Non ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> a - Specifica per musica a stampa. Non ripetibile
        <li> d - Specifica parallela per musica a stampa. Ripetibile
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppDocumento::creaTag208_AreaSpecifica_MusicaAStampa(
		char *areaStartPtr, char *areaEndPtr) {
	DataField *df;
	Subfield *sf;
	CString s = areaStartPtr;
//	CString * a208 = 0;
	ATTValVector<CString *> d208vect;
	long from = 1, start = 0;

	df = new DataField();
	df->setTag("208");
//	df->setIndicator1(' '); //  27/09/2010 12.06
//	df->setIndicator2('1');

	s.Split(d208vect, " = ");

//	while (s.StartsWithFrom(" = ", from)) {
//		start = from++;
//		while (from < s.Length()) {
//			if (s.StartsWithFrom(" = ", from))
//				break;
//			from++;
//		}
//		if (!a208)
//			a208 = new CString(s.SubstringData(start, from - start));
//		else
//			d208vect.Add(new CString(s.SubstringData(start, from - start)));
//
//	} // End while (" = ")
//	if (!a208)
//		a208 = new CString(areaStartPtr); // s.SubstringData(start, from-start
//	else
//		d208vect.Add(new CString(s.SubstringData(start))); // l'ultimo

//	sf = new Subfield('a', a208); // a - Printed music specific statement
	sf = new Subfield('a', d208vect.Entry(0)); // a - Printed music specific statement

	df->addSubfield(sf);

	for (int i = 1; i < d208vect.length(); i++) {
		sf = new Subfield('d', d208vect.Entry(i)); // d - Specifica parallela per musica a stampa
		//sf->setData();
		df->addSubfield(sf);
	}

//	delete a208;
	d208vect.DeleteAndClear();
	return df;
} // End creaTag208


/*!
\brief <b>Tag 215 - Descrizione fisica </b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>215</td></tr>
<tr><td valign=top>Descrizione</td><td>Descrizione fisica</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> a - Designazione specifica ed estensione del materiale. Ripetibile
        <li> c - Altre descrizioni fisiche. Non ripetibile
        <li> d - Dimensioni. Ripetibile
        <li> e - Materiale allegato. Ripetibile
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td>I delimitatori non vengono riportati nei sottocampi.</td></tr>
</table>
*/
DataField * Marc4cppDocumento::creaTag215_AreaDescrizioneFisica(char *areaStartPtr, char *areaEndPtr) {
	DataField *df;
	Subfield *sf;
    char *ptrArea, *ptrStop;
    df = new DataField("215", 3);
//    df->setTag();

    ptrArea = ptrStop = areaStartPtr;
    while (*ptrStop) {
        if (!strncmp(ptrStop, " : ", 3) || !strncmp(ptrStop, " ; ", 3)
                || !strncmp(ptrStop, " + ", 3)) {
            *ptrStop = 0;
            ptrStop++;
            break;
        }
        ptrStop++;
    }
    sf = new Subfield('a'); // Designazione specifica ed estensione del materiale
    sf->setData(ptrArea, strlen(ptrArea));
    df->addSubfield(sf);
    ptrArea = ptrStop + 2;

    // Troviamo le Illustrazioni
    if (*ptrStop == ':') {
        ptrStop = ptrArea;
        while (*ptrStop) {
            if (!strncmp(ptrStop, " ; ", 3) || !strncmp(ptrStop, " + ", 3)) {
                *ptrStop = 0;
                ptrStop++;
                break;
            }
            ptrStop++;
        }
        sf = new Subfield('c'); // Altre descrizioni fisiche
        sf->setData(ptrArea, strlen(ptrArea));
        df->addSubfield(sf);
        ptrArea = ptrStop + 2;
    }

    if (*ptrStop == ';') // Dimensioni
    {
        ptrStop = ptrArea;
        while (*ptrStop) {
            if (!strncmp(ptrStop, " + ", 3)) {
                *ptrStop = 0;
                ptrStop++;
                break;
            }
            ptrStop++;
        }
        sf = new Subfield('d'); // Dimensioni
        sf->setData(ptrArea, strlen(ptrArea));
        df->addSubfield(sf);
        ptrArea = ptrStop + 2;
    }

    if (*ptrStop == '+') // Materiale allegato
    {
        sf = new Subfield('e');
        sf->setData(ptrArea, strlen(ptrArea));
        df->addSubfield(sf);
    }

    //  marcRecord->addDataField(df);
    return df;
} // End creaTag215


/*!
\brief <b>Tag 230 - Area risorse elettroniche</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>230</td></tr>
<tr><td valign=top>Descrizione</td><td>Area risorse elettroniche</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Obbligatorio per i computer files</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile solo quando le caratteristiche del file di pia' di un file sono descritte in uno singolo record</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Tipo risorsa. Non ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppDocumento::creaTag230_AreaRisorseElettroniche(
		char *areaStartPtr, char *areaEndPtr) {
	DataField *df;
	Subfield *sf;

	df = new DataField();
	df->setTag("230");

	sf = new Subfield('a', areaStartPtr, areaEndPtr-areaStartPtr);
	//sf->setData();
	df->addSubfield(sf);

	return df;
}



/*!
\brief <b>Tag 300 - Note generali</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>300</td></tr>
<tr><td valign=top>Descrizione</td><td>Note generali relative a qualsiasi aspetto dell'unita' bibliografica</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Non ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Testo della nota. Non ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppDocumento::creaTag300_NoteGenerali(char *areaStartPtr, char *areaEndPtr) {
	DataField *df;
	Subfield *sf;
	//	CString s = areaStartPtr;
	long start = 0;
	char *ptr = areaStartPtr;
	char *ptr2;

	while (ptr = strstr(areaStartPtr + start, ". - ")) {
		df = new DataField();
		df->setTag("300");
		//		df->setIndicator1('#');
		//		df->setIndicator2('#');

		*ptr = 0;

		ptr2 = areaStartPtr + start;
		if (*ptr2 == '(')
			ptr2++;

		sf = new Subfield('a', ptr2, ptr-ptr2);
		//sf->setData();
		df->addSubfield(sf);
		start = (ptr + 4) - areaStartPtr;

		marcRecord->addDataField(df);
	}

	df = new DataField();
	df->setTag("300");

	ptr2 = areaStartPtr + start;
	if (*ptr2 == '(')
		ptr2++;

	sf = new Subfield('a', ptr2, areaEndPtr-ptr2);
	//sf->setData();
	df->addSubfield(sf);

	marcRecord->addDataField(df);

	return df;
} // End creaTag300_NoteGenerali







/*!
\brief <b>Tag 327 - Note di contenuto</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>327</td></tr>
<tr><td valign=top>Descrizione</td><td>Note di contenuto</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Testo della nota. Non ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
void Marc4cppDocumento::creaTag327_NoteDiContenuto()
{ // Vedi elabora note
}



/*!
\brief <b>Tag 330 - Abstract</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>330</td></tr>
<tr><td valign=top>Descrizione</td><td>Abstract</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Testo dell'abstract. Non ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
void Marc4cppDocumento::creaTag330_Abstract()
{ // Vedi elabora note
}


/*!
\brief <b>Tag 336 - Note sulla risorsa elettronica</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>336</td></tr>
<tr><td valign=top>Descrizione</td><td>Note sulla risorsa elettronica</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Testo della nota. Non ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
void Marc4cppDocumento::creaTag336_NoteRisorsaElettronica()
{ // Vedi elabora note
}



/*!
\brief <b>Tag 337 - Note tecniche sulla risorsa elettronica</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>337</td></tr>
<tr><td valign=top>Descrizione</td><td>Note tecniche sulla risorsa elettronica</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Testo della nota. Non ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
void Marc4cppDocumento::creaTag337_NoteTecnicheRisorsaElettronica()
{ // Vedi elabora note
}


/*!
\brief <b>Tag 323 - Note al cast</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>323</td></tr>
<tr><td valign=top>Descrizione</td><td>Note al cast</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Testo della nota. Non ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
void Marc4cppDocumento::creaTag323_NoteAlCast()
{ // Vedi elabora note
}




/*
 Tag+Def:		326 Periodicita'
 --------
 Obbligatorieta':	Facoltativo
 Ripetibilita':	Non Ripetibile
 Indicatore1:	Non definito
 Indicatore2:	Non definito
 Sottocampi:
 a - Testo della nota
 */


/*!
\brief <b>Tag 326 - Note sulla periodicita'</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>326</td></tr>
<tr><td valign=top>Descrizione</td><td>Note sulla periodicita'</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Testo della nota. Non ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

DataField * Marc4cppDocumento::creaTag326_NotePeriodicita() {
	DataField *df = 0;
//	CString cd_periodicita;


//	cd_periodicita = tbTitolo->getField(tbTitolo->cd_periodicita);
	char cd_periodicita = *tbTitolo->getField(tbTitolo->cd_periodicita);
	if (!cd_periodicita || cd_periodicita == ' ')
		return df; // No periodicita'

	CString descPeriodicita;

	if (cd_periodicita >= 'A' && cd_periodicita <= 'Z')
		descPeriodicita = descTipoPeriodicita [cd_periodicita-'A'];
	else
		descPeriodicita = cd_periodicita;

	if (descPeriodicita.IsEmpty())
		return df; // No periodicita'

	//	bool retb;
	Subfield *sf;

	df = new DataField();
	df->setIndicator1(' ');
	df->setIndicator2(' ');
	df->setTag("326");


	sf = new Subfield('a', &descPeriodicita);
	//sf->setData();
	df->addSubfield(sf);

	return df;
} // End creaTag326Periodicita




/*!
\brief <b>Tag 801 - Fonte di provenienza del record</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>801</td></tr>
<tr><td valign=top>Descrizione</td><td>Fonte di provenienza del record</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Non ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>3 - L'agenzia emittente</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Nazione dell'agenzia
        <li>b - Agenzia
        <li>c - Data di inserimento notizia per POLO o data di creazione Unimatc per INDICE
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppDocumento::creaTag801_FonteDiProvenienza() {
	//	bool retb;
	DataField *df = 0;
	Subfield *sf;
	char dateBuf[8 + 1];
	*(dateBuf + 8) = 0;

	df = new DataField();
//	df->setIndicator2('0');
//	df->setIndicator2('|'); // CFI 04/11/2009 14.57
	df->setIndicator2('3'); // ICCU Roveri unimarc_11_08_2010.doc 14/09/2010 10.35
	df->setTag("801");

	sf = new Subfield('a', "IT", 2);
	//sf->setData();
	df->addSubfield(sf);


	sf = new Subfield('b', &bibliotecaRichiedenteScarico);
	//sf->setData();
	df->addSubfield(sf);


if (DATABASE_ID != DATABASE_INDICE) // 02/08/2010
{
	CMisc::formatDate1(tbTitolo->getField(tbTitolo->ts_ins), dateBuf);
	sf = new Subfield('c', dateBuf, 8);

	//sf->setData(); // tbTitolo->getField(tbTitolo->ts_ins)
	df->addSubfield(sf);
}

else
{ // 801 per indice
//	char dateBuf[8+ 1];
//	*(dateBuf + 8) = 0;

	// CFI vuole il time stamp dell'export (Verifica UNIMARC1)
	time_t rawtime;
	time(&rawtime);
	struct tm * timeinfo = localtime(&rawtime);
	CMisc::formatDate4(timeinfo, dateBuf);

	sf = new Subfield('c', dateBuf, 8);
	//sf->setData();
	df->addSubfield(sf);
}


//	sf = new Subfield('g');
//	sf->setData("SBN");
//	df->addSubfield(sf);

//	sf = new Subfield('g'); // Richiesta CFI 04/02/2010 15.29
//	sf->setData("SBN");		// Cassata da ICCU 05/02/2010 10.06
//	df->addSubfield(sf);


	return df;
} // End creaTag801FonteDiProvenienza


/*!
\brief <b>Tag 850 - Istituto detentore</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>850</td></tr>
<tr><td valign=top>Descrizione</td><td>Istituto detentore</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Codice ISIL
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppDocumento::creaTag850_IstitutoDetentore() {
	//	bool retb;
	DataField *df = 0;
	Subfield *sf;
	const char *bid = tbTitolo->getField(tbTitolo->bid);



//	const char *cd_ana_biblioteca;
	if (trTitBib->existsRecord(bid)) {

		df = new DataField();
		df->setTag("850");

		CString key, s;
		while (trTitBib->loadNextRecord(bid)) {

//			const char *cdBib =  trTitBib->getField(trTitBib->cd_biblioteca);
//			TbfBibliotecaInPolo *tbfBibliotecaInPolo = (TbfBibliotecaInPolo *) tbfBibliotecaKV->GetValueFromKey(cdBib);
//
//			if (!tbfBibliotecaInPolo) {
//				SignalAnError(__FILE__, __LINE__, "Biblioteca non trovata per '%s'",cdBib);
//				return df;
//			}
//
//			sf = new Subfield('a'); // Codice anagrafe biblioteche
//			cd_ana_biblioteca = tbfBibliotecaInPolo->getField(tbfBibliotecaInPolo->cd_ana_biblioteca);
//			sf->setData(cd_ana_biblioteca);
//			df->addSubfield(sf);


		    key = getPolo(); // prendi il polo
		    key.AppendString(trTitBib->getFieldString(trTitBib->cd_biblioteca)); // prendi la biblioteca

		    if (tbfBiblioteca->loadRecord(key.data()))
		        {
		        s = tbfBiblioteca->getField(tbfBiblioteca->paese);
		        s.AppendChar('-');
		        s.AppendString(tbfBiblioteca->getFieldString(tbfBiblioteca->cd_ana_biblioteca));
		        }
		    else
		        s = "???-??????";

			sf = new Subfield('a', &s); // Codice ISIL
			//sf->setData();
			df->addSubfield(sf);


		} // end while
	}
	return df;
} // End creaTag850_IstitutoDetentore




DataField * Marc4cppDocumento::creaTag115_audiovisivo() {
	DataField *df = 0;
	const char *bid = tbTitolo->getField(tbTitolo->bid);
	if (!tbAudiovideo->loadRecord(bid))
		return df;

//tbAudiovideo->dumpRecord();

	Subfield *sf;
	CString a,b; // sottocampi

	// $a
	// ------
	a.AppendChar(*(tbAudiovideo->getField(tbAudiovideo->tp_mater_audiovis))); // Pos. 0 tipo di materiale audiovisivo OBBLIGATORIO
	if (*(tbAudiovideo->getField(tbAudiovideo->lunghezza)))
		{
		CString *len = tbAudiovideo->getFieldString(tbAudiovideo->lunghezza);
		len->leftPadding(' ', 3);
		a.AppendString(len); // Pos. 1 lunghezza
		}
	else
		a.AppendString("|||"); // Pos. 1-3

	a.AppendChar(*(tbAudiovideo->getField(tbAudiovideo->cd_colore))); 	// Pos. 4 indicatore di colore OBBLIGATORIO
	a.AppendChar(*(tbAudiovideo->getField(tbAudiovideo->cd_suono))); 	// Pos. 5 indicatore di suono OBBLIGATORIO

	if (*tbAudiovideo->getField(tbAudiovideo->tp_media_suono))
		a.AppendChar(*tbAudiovideo->getField(tbAudiovideo->tp_media_suono)); // Pos. 6
	else
		a.AppendChar('|');

	if (*tbAudiovideo->getField(tbAudiovideo->cd_dimensione))
		a.AppendChar(*tbAudiovideo->getField(tbAudiovideo->cd_dimensione)); // Pos. 7
	else
		a.AppendChar('|');

	if (*tbAudiovideo->getField(tbAudiovideo->cd_forma_video))
		a.AppendChar(*tbAudiovideo->getField(tbAudiovideo->cd_forma_video)); // Pos. 8
	else
		a.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->cd_tecnica))
		a.AppendChar(*tbAudiovideo->getField(tbAudiovideo->cd_tecnica)); // Pos. 9
	else
		a.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->tp_formato_film))
		a.AppendChar(*tbAudiovideo->getField(tbAudiovideo->tp_formato_film)); // Pos. 10
	else
		a.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->cd_mat_accomp_1)) // Pos. 11
		a.AppendChar(*tbAudiovideo->getField(tbAudiovideo->cd_mat_accomp_1));
	else
		a.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->cd_mat_accomp_2)) // Pos. 12
		a.AppendChar(*tbAudiovideo->getField(tbAudiovideo->cd_mat_accomp_2));
	else
		a.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->cd_mat_accomp_3)) // Pos. 13
		a.AppendChar(*tbAudiovideo->getField(tbAudiovideo->cd_mat_accomp_3));
	else
		a.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->cd_mat_accomp_4)) // Pos. 14
		a.AppendChar(*tbAudiovideo->getField(tbAudiovideo->cd_mat_accomp_4));
	else
		a.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->cd_forma_regist))
		a.AppendChar(*tbAudiovideo->getField(tbAudiovideo->cd_forma_regist)); // Pos. 15
	else
		a.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->tp_formato_video))
		a.AppendChar(*tbAudiovideo->getField(tbAudiovideo->tp_formato_video)); // Pos. 16
	else
		a.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->cd_materiale_base))
		a.AppendChar(*tbAudiovideo->getField(tbAudiovideo->cd_materiale_base)); // Pos. 17
	else
		a.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->cd_supporto_second))
		a.AppendChar(*tbAudiovideo->getField(tbAudiovideo->cd_supporto_second)); // Pos. 18
	else
		a.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->cd_broadcast))
		a.AppendChar(*tbAudiovideo->getField(tbAudiovideo->cd_broadcast)); // Pos. 19
	else
		a.AppendChar('|');

	// $b
	// -----
	if (*tbAudiovideo->getField(tbAudiovideo->tp_generazione))
		b.AppendChar(*tbAudiovideo->getField(tbAudiovideo->tp_generazione)); // Pos. 0
	else
		b.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->cd_elementi))
		b.AppendChar(*tbAudiovideo->getField(tbAudiovideo->cd_elementi)); // Pos. 1
	else
		b.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->cd_categ_colore))
		b.AppendChar(*tbAudiovideo->getField(tbAudiovideo->cd_categ_colore)); // Pos. 2
	else
		b.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->cd_polarita))
		b.AppendChar(*tbAudiovideo->getField(tbAudiovideo->cd_polarita)); // Pos. 3
	else
		b.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->cd_pellicola))
		b.AppendChar(*tbAudiovideo->getField(tbAudiovideo->cd_pellicola)); // Pos. 4
	else
		b.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->tp_suono))
		b.AppendChar(*tbAudiovideo->getField(tbAudiovideo->tp_suono)); // Pos. 5
	else
		b.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->tp_stampa_film ))
		b.AppendChar(*tbAudiovideo->getField(tbAudiovideo->tp_stampa_film )); // Pos. 6
	else
		b.AppendChar('|');
/* NON USATE (Aste/Scognemilo 17/02/2014)
	if (*tbAudiovideo->getField(tbAudiovideo->cd_deteriore))
		b.AppendChar(*tbAudiovideo->getField(tbAudiovideo->cd_deteriore)); // Pos. 7
	else
		b.AppendChar('|');
	if (*tbAudiovideo->getField(tbAudiovideo->cd_completo))
		b.AppendChar(*tbAudiovideo->getField(tbAudiovideo->cd_completo)); // Pos. 8
	else
		b.AppendChar('|');
	if (*(tbAudiovideo->getField(tbAudiovideo->dt_ispezione)))// Pos. 9-14
		{
		CString *dt_ispezione = tbAudiovideo->getFieldString(tbAudiovideo->dt_ispezione);
		dt_ispezione->leftPadding(' ', 6);
		b.AppendString(dt_ispezione);
		}
	else
		b.AppendString("||||||");
*/
	b.AppendString("||||||||");

	if (a.IsEmpty() && b.IsEmpty())
		return df;

	df = new DataField("115", 3);
	if (a.Length())
	{
		sf = new Subfield('a', &a);
		df->addSubfield(sf);
	}
	if (b.Length())
	{
		sf = new Subfield('b', &b);
		df->addSubfield(sf);
	}

	marcRecord->addDataField(df);

	return df;
}// End creaTag115_audiovideo


DataField * Marc4cppDocumento::creaTag126_DiscoSonoro() {
	DataField *df = 0;

	const char *bid = tbTitolo->getField(tbTitolo->bid);
	if (!tbDiscoSonoro->loadRecord(bid))
		return df;

//tbAudiovideo->dumpRecord();

	Subfield *sf;
	CString a,b; // sottocampi

	// $a
	// ------
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->cd_forma))
		a.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->cd_forma)); // Pos. 0
	else
		a.AppendChar('|');
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->cd_velocita))
		a.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->cd_velocita)); // Pos. 1
	else
		a.AppendChar('|');
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->tp_suono))
		a.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->tp_suono)); // Pos. 2
	else
		a.AppendChar('|');
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->cd_pista))
		a.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->cd_pista)); // Pos. 3
	else
		a.AppendChar('|');
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->cd_dimensione))
		a.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->cd_dimensione)); // Pos. 4
	else
		a.AppendChar('|');
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->cd_larg_nastro))
		a.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->cd_larg_nastro)); // Pos. 5
	else
		a.AppendChar('|');
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->cd_configurazione))
		a.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->cd_configurazione)); // Pos. 6
	else
		a.AppendChar('|');
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->cd_mater_accomp_1))
		a.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->cd_mater_accomp_1)); // Pos. 7
	else
		a.AppendChar('|');
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->cd_mater_accomp_2))
		a.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->cd_mater_accomp_2)); // Pos. 8
	else
		a.AppendChar('|');
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->cd_mater_accomp_3))
		a.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->cd_mater_accomp_3)); // Pos. 9
	else
		a.AppendChar('|');
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->cd_mater_accomp_4))
		a.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->cd_mater_accomp_4)); // Pos. 10
	else
		a.AppendChar('|');
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->cd_mater_accomp_5))
		a.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->cd_mater_accomp_5)); // Pos. 11
	else
		a.AppendChar('|');
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->cd_mater_accomp_6))
		a.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->cd_mater_accomp_6)); // Pos. 12
	else
		a.AppendChar('|');
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->cd_tecnica_regis))
		a.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->cd_tecnica_regis)); // Pos. 13
	else
		a.AppendChar('|');
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->cd_riproduzione))
		a.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->cd_riproduzione)); // Pos. 14
	else
		a.AppendChar('|');


	// $b
	// ------
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->tp_disco))
		b.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->tp_disco)); // Pos. 0
	else
		b.AppendChar('|');
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->tp_materiale))
		b.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->tp_materiale)); // Pos. 1
	else
		b.AppendChar('|');
	if (*tbDiscoSonoro->getField(tbDiscoSonoro->tp_taglio))
		b.AppendChar(*tbDiscoSonoro->getField(tbDiscoSonoro->tp_taglio)); // Pos. 2
	else
		b.AppendChar('|');

	if (a.IsEmpty() && b.IsEmpty())
		return df;

	df = new DataField("126", 3);
	if (a.Length())
	{
		sf = new Subfield('a', &a);
		df->addSubfield(sf);
	}
	if (b.Length())
	{
		sf = new Subfield('b', &b);
		df->addSubfield(sf);
	}


	marcRecord->addDataField(df);
	return df;
}// End creaTag126_DiscoSonoro


// 28/01/2016
/*!

\brief <b>127   CODED DATA FIELD –– Duration of sound recordings and printed music</b>

 <table>
 <tr>
 <th valign=top>Definizione</th>
 <th>Descrizione</th>
 </tr>

 <tr><td valign=top>Tag</td><td>181</td></tr>
 <tr><td valign=top>Descrizione</td>
	 <td>The field contains one or more six - character numbers corresponding to the duration of a manifestation
	 consisting in or containing sound recordings or a portion of a sound recording, or to the estimated duration
	 of a composition as stated on the printed music score that is being described.</td></tr>
 <tr><td valign=top>Obbligatorieta'</td>
	 <td>Optional.  </td></tr>
 <tr><td valign=top>Ripetibilita'</td>
	 <td>NON Repeatable</td></tr>
 <tr><td valign=top>Indicatore 1</td>
	 <td>\li blank (not defined)</td></tr>
 <tr><td valign=top>Indicatore 2</td>
	 <td>\li blank (not defined)</td></tr>
  <tr><td valign=top>Sottocampi</td>
	 <td>
		<UL>
		\li a - Duration
		</UL>

	 </td></tr>
  <tr><td valign=top>NOTE</td><td></td></tr>

 </table>
 */

DataField * Marc4cppDocumento::creaTag127_DiscoSonoroDurata() {
	DataField *df = 0;

	// Record gia caricato dalla 126
//tbDiscoSonoro->dumpRecord();

//	printf ("tbDiscoSonoro->getField(tbDiscoSonoro->durata) = %s", tbDiscoSonoro->getField(tbDiscoSonoro->durata));

	if (!*tbDiscoSonoro->getField(tbDiscoSonoro->durata))
		return df;

	Subfield *sf;
	CString a; // sottocampi

	// $a
	// ------
	a.AppendString(tbDiscoSonoro->getField(tbDiscoSonoro->durata)); // Pos. 0

	df = new DataField("127", 3);
	sf = new Subfield('a', &a);
	df->addSubfield(sf);

	marcRecord->addDataField(df);
	return df;
}// End creaTag127_DiscoSonoroDurata






/*!

\brief <b>181   CODED DATA FIELD –– Content Form (area zer0)</b>

 <table>
 <tr>
 <th valign=top>Definizione</th>
 <th>Descrizione</th>
 </tr>

 <tr><td valign=top>Tag</td><td>181</td></tr>
 <tr><td valign=top>Descrizione</td>
	 <td>This field contains fixed-length data specifying the content form and content qualification of the resource being described, corresponding to the first two elements of ISBD Area 0.</td></tr>
 <tr><td valign=top>Obbligatorieta'</td>
	 <td>Optional.  </td></tr>
 <tr><td valign=top>Ripetibilita'</td>
	 <td>Repeatable when more than one system code is used or when a resource consists of different content forms in different media types (e.g., a kit resource).</td></tr>
 <tr><td valign=top>Indicatore 1</td>
	 <td>\li blank (not defined)</td></tr>
 <tr><td valign=top>Indicatore 2</td>
	 <td>The second indicator specifies whether this field is used to generate the ISBD Area 0 for print or other displays of the record.
	 	\li 0	Do not generate area 0
		\li 1	Generate area 0
		\li #	Information not provided (use when the field contains subfield $c).

	 </td></tr>

  <tr><td valign=top>Sottocampi</td>
	 <td>
		<UL>
		\li a - ISBD Content form Code. Optional.  Repeatable for different Content Forms contained in one Media Type.
			<UL>
			<li>pos 0 = ISBD Content Form Code:
				<UL>
				<li>a = dataset,
				<li>b = immagine,
				<li>c = movimento,
				<li>d = musica,
				<li>e = oggetto,
				<li>f = programmi,
				<li>g = suoni,
				<li>h = parlato,
				<li>i = testo,
				<li>m = forme contenuto multiplo,
				<li>z = altro (per ora non gestito);
				</UL>
			<li>pos 1 = Extent of applicability:
				<UL>
				<li>0 = none
				<li>1 = some
				<li>2 = substantial
				<li>3 = predominate
				<li>4 = full
				<li># = position not used
				</UL>
		\li b - ISBD Content Qualification : Specification of Type
			<UL>
			<li>pos 0 = Specificazione del tipo di contenuto
				<UL>
				<li>a  =  notato
				<li>b  =  eseguito
				<li>c  =  cartografico
				<li>x  =  not applicable
				<li>#  =  position not used
				</UL>
			<li>pos 1 = Specificazione del tipo del movimento
				<UL>
				<li>a  =  movimento
				<li>b  =  fissa
				<li>x  =  non applicabile, la risorsa non e' un'immagine
				<li>#  =  position not used
				</UL>
			<li>pos 2 = Specificazione della dimensionalita'
				<UL>
				<li>2  =  bi-dimensionale
				<li>3  =  tri-dimensionale
				<li>x  =  non applicabile, la risorsa non e' un'immagine
				</UL>
			<li>pos 3-5 = Specificazione sensoriale
				<UL>
				<li>a  =  uditiva
				<li>b  =  gustativa
				<li>c  =  olfattiva
				<li>d  =  tattile
				<li>e  =  visuale
				<li>#  =  position not used
				</UL>
			</UL>
		\li c - Other Coding for Type of Expression
		\li 2 - System code
		\li 6 - Interfield Linking Data

		</UL>

	 </td></tr>
  <tr><td valign=top>NOTE</td><td></td></tr>

 </table>
 */


DataField * Marc4cppDocumento::creaTag181_Area0_DatiCodificati(char *bid) {
	DataField *df = 0;
	Subfield *sf;
	CString a,b; // sottocampi
//	const char *bid = tbTitolo->getField(tbTitolo->bid);
	if (!tbTitset1->loadRecord(bid))
		return df;

//tbTitset1->dumpRecord();


	// $a
	// ------
	a.AppendChar(*(tbTitset1->getField(tbTitset1->s181_tp_forma_contenuto_1))); // Pos. 0 ISBD Content Form Code:
	a.AppendChar(' '); // 05/01/2016											// Pos 1 Extent of applicability:

	// $b
	// -----
	if (*tbTitset1->getField(tbTitset1->s181_cd_tipo_contenuto_1))
		b.AppendChar(*tbTitset1->getField(tbTitset1->s181_cd_tipo_contenuto_1)); // Pos. 0 ISBD Content Qualification : Specification of Type
	else
//		b.AppendChar('|');
		b.AppendChar('x'); // 05/01/2016

	if (*tbTitset1->getField(tbTitset1->s181_cd_movimento_1))
		b.AppendChar(*tbTitset1->getField(tbTitset1->s181_cd_movimento_1)); // Pos. 1 Specificazione del tipo del movimento
	else
	{
		if (a.GetChar(0) != 'b')
			b.AppendChar('x');
		else
			b.AppendChar(' ');
	}

	if (*tbTitset1->getField(tbTitset1->s181_cd_dimensione_1))
		b.AppendChar(*tbTitset1->getField(tbTitset1->s181_cd_dimensione_1)); // Pos. 2 Specificazione della dimensionalita'
	else
	{
		if (a.GetChar(0) != 'b')
			b.AppendChar('x');
		else
			b.AppendChar(' ');
	}
	if (*tbTitset1->getField(tbTitset1->s181_cd_sensoriale_1_1))
		b.AppendChar(*tbTitset1->getField(tbTitset1->s181_cd_sensoriale_1_1)); // Pos. 3 Specificazione sensoriale
	else
	{
		if (a.GetChar(0) != 'b')
			b.AppendChar('x');
		else
			b.AppendChar(' ');
	}

	if (*tbTitset1->getField(tbTitset1->s181_cd_sensoriale_2_1))
		b.AppendChar(*tbTitset1->getField(tbTitset1->s181_cd_sensoriale_2_1)); // Pos. 4 Specificazione sensoriale
	else
		b.AppendChar(' ');
	if (*tbTitset1->getField(tbTitset1->s181_cd_sensoriale_3_1))
		b.AppendChar(*tbTitset1->getField(tbTitset1->s181_cd_sensoriale_3_1)); // Pos. 5 Specificazione sensoriale
	else
		b.AppendChar(' ');

	if (a.IsEmpty() && b.IsEmpty())
		return df;

	df = new DataField("181", 3);
	df->setIndicator1(' ');
//	df->setIndicator2('0');
	df->setIndicator2('1'); // ‘1’ (used to generate displays);

	// Prima istanza
	// -------------
//	sf = new Subfield('6', "01", 2);
	sf = new Subfield('6', "z01", 3);
	df->addSubfield(sf);
	if (a.Length())
	{
		sf = new Subfield('a', &a);
		df->addSubfield(sf);
	}
	if (b.Length())
	{
		sf = new Subfield('b', &b);
		df->addSubfield(sf);
	}
	marcRecord->addDataField(df); // Prima istanza



	// Abbiamo una seconda istanza?
	char tfc = *(tbTitset1->getField(tbTitset1->s181_tp_forma_contenuto_2));
	if (tfc) //  != ' '
	{ // Abbiamo una seconsa istanza
		a.Clear();
		b.Clear();
		// $a
		// ------
		a.AppendChar(*(tbTitset1->getField(tbTitset1->s181_tp_forma_contenuto_2))); // Pos. 0

		// $b
		// -----
		if (*tbTitset1->getField(tbTitset1->s181_cd_tipo_contenuto_2))
			b.AppendChar(*tbTitset1->getField(tbTitset1->s181_cd_tipo_contenuto_2)); // Pos. 0
		else
			b.AppendChar('|');
		if (*tbTitset1->getField(tbTitset1->s181_cd_movimento_2))
			b.AppendChar(*tbTitset1->getField(tbTitset1->s181_cd_movimento_2)); // Pos. 1
		else
			b.AppendChar('|');
		if (*tbTitset1->getField(tbTitset1->s181_cd_dimensione_2))
			b.AppendChar(*tbTitset1->getField(tbTitset1->s181_cd_dimensione_2)); // Pos. 2
		else
			b.AppendChar('|');
		if (*tbTitset1->getField(tbTitset1->s181_cd_sensoriale_1_2))
			b.AppendChar(*tbTitset1->getField(tbTitset1->s181_cd_sensoriale_1_2)); // Pos. 3
		else
			b.AppendChar('|');
		if (*tbTitset1->getField(tbTitset1->s181_cd_sensoriale_2_2))
			b.AppendChar(*tbTitset1->getField(tbTitset1->s181_cd_sensoriale_2_2)); // Pos. 4
		else
			b.AppendChar('|');
		if (*tbTitset1->getField(tbTitset1->s181_cd_sensoriale_3_2))
			b.AppendChar(*tbTitset1->getField(tbTitset1->s181_cd_sensoriale_3_2)); // Pos. 5
		else
			b.AppendChar('|');

		df = new DataField("181", 3);
		df->setIndicator1(' ');
		df->setIndicator2('0');

		// Seconsa istanza
		// -------------
//		sf = new Subfield('6', "02", 2);
		sf = new Subfield('6', "z02", 3);
		df->addSubfield(sf);
		if (a.Length())
		{
			sf = new Subfield('a', &a);
			df->addSubfield(sf);
		}
		if (b.Length())
		{
			sf = new Subfield('b', &b);
			df->addSubfield(sf);
		}
		marcRecord->addDataField(df); // Seconda istanza

	}

	return df;
}// End creaTag181_DatiComuniArea0




/*!

\brief <b>182   CODED DATA FIELD –– Media Type  (area zer0)</b>

 <table>
 <tr>
 <th valign=top>Definizione</th>
 <th>Descrizione</th>
 </tr>

 <tr><td valign=top>Tag</td><td>181</td></tr>
 <tr><td valign=top>Descrizione</td>
	 <td>This field contains fixed-length data specifying the media type of the resource being described. </td></tr>
 <tr><td valign=top>Obbligatorieta'</td>
	 <td>Optional.  </td></tr>
 <tr><td valign=top>Ripetibilita'</td>
	 <td>Repeatable when more than one system code is used or when a resource consists of mixed media (e.g., a kit resource).</td></tr>
 <tr><td valign=top>Indicatore 1</td>
	 <td>\li blank (not defined)</td></tr>
 <tr><td valign=top>Indicatore 2</td>
	 <td>The second indicator specifies whether this field is used to generate the ISBD Area 0 for print or other displays of the record.
	 	\li 0	Do not generate area 0
		\li 1	Generate area 0
		\li #	Information not provided (use when the field contains subfield $c).

	 </td></tr>

  <tr><td valign=top>Sottocampi</td>
	 <td>
		<UL>
		\li a - ISBD Media Type Code (Tipo di mediazione).
			<UL>
			<li>pos 0 = ISBD Content Form Code:
				<UL>
				<li>a  =  audio
				<li>b  =  electronic
				<li>c  =  microform
				<li>d  =  microscopic
				<li>e  =  projected
				<li>f  =  stereographic
				<li>g  =  video
				<li>m  =  multiple media
				<li>n  =  unmediated
				<li>z = altro (per ora non gestito);
				</UL>
		\li c - Other Coding for Media Type
			<UL>
			<li>pos 0 =
			</UL>
		\li 2 - System code
		\li 6 - Interfield Linking Data

		</UL>

	 </td></tr>
  <tr><td valign=top>NOTE</td><td></td></tr>

 </table>
 */


DataField * Marc4cppDocumento::creaTag182_Area0_TipoMediazione(char *bid) {
	DataField *df = 0;
	Subfield *sf;
	CString a,b; // sottocampi

	char tm = *(tbTitset1->getField(tbTitset1->s182_tp_mediazione_1));
	if (!tm)
		return df;
	df = new DataField("182", 3);
	df->setIndicator1(' ');
//	df->setIndicator2('0');
	df->setIndicator2('1'); // ‘1’ (used to generate displays);

	// Prima istanza (obbligatoria)
//	sf = new Subfield('6', "01", 2);
	sf = new Subfield('6', "z01", 3);
	df->addSubfield(sf);
	sf = new Subfield('a', tbTitset1->getFieldString(tbTitset1->s182_tp_mediazione_1));
	df->addSubfield(sf);
	marcRecord->addDataField(df);

	// Abbiamo una seconda istanza?
	tm = *(tbTitset1->getField(tbTitset1->s182_tp_mediazione_2));
	if (tm)
	{
		df = new DataField("182", 3);
		df->setIndicator1(' ');
		df->setIndicator2('0');

//		sf = new Subfield('6', "02", 2);
		sf = new Subfield('6', "z02", 3);
		df->addSubfield(sf);
		sf = new Subfield('a', tbTitset1->getFieldString(tbTitset1->s182_tp_mediazione_2));
		df->addSubfield(sf);
		marcRecord->addDataField(df);
	}
	return df;
} // End creaTag182_DatiComuniArea0



/*!
\brief <b>Tag 210 - Area ISBD della pubblicazione, distribuzione</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>210</td></tr>
<tr><td valign=top>Descrizione</td><td>Area ISBD della pubblicazione</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Non Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
	    <li>a - Primo luogo di pubblicazione. Ripetibile
	    <li>b - Indirizzo dell'editore. Ripetibile
	    <li>c - Nome dell'editore. Ripetibile
    </UL>
	 Secondo e successivi luoghi di pubblicazione
    <UL>
	    <li>d - Data di pubblicazione. Ripetibile
	    <li>e - Luogo di stampa. Ripetibile
	    <li>g - Nome del tipografo. Ripetibile
	    <li>h - Data di stampa. Ripetibile
    </UL>
    </td>
    </tr>
 <tr><td valign=top>NOTE</td><td>I delimitatori non vengono riportati nei sottocampi.</td></tr>
</table>
*/
//<li>f - Indirizzo del tipografo

#define SOTTOAREA_PUBBLICAZIONE	0
#define SOTTOAREA_STAMPA		1


DataField * Marc4cppDocumento::creaTag210_AreaPubblicazioneProduzioneDistribuzione(
		char *areaStartPtr, char *areaEndPtr) {
	DataField *df = 0;
	Subfield *sf;
	CString *sPtr;
	ATTValVector<CString *> stringVect;
	ATTValVector<CString *> stringLuoghiPubblicazioneVect;
	ATTValVector<CString *> stringLuoghiStampaVect;
	ATTValVector<CString *> stringEditoreVect;
	ATTValVector<CString *> stringTipografoVect;
	bool retb;
	CString s(areaStartPtr, areaEndPtr-areaStartPtr);
	//CString luogoDiStampa;
	CString daData;
	int from, len;


//	printf ("\nareaStartPtr='%s' areaEndPtr='%s' len=%d", areaStartPtr, areaEndPtr, areaEndPtr-areaStartPtr);

	// Rimozione whitespace in coda
	s.Strip(s.trailing, " \t\n");

//	s.removeCharacterOccurances('['); // 02/11/2015 Parlato con Mataloni (CFI0916458)
//	s.removeCharacterOccurances(']');

// 12/09/27 VAFF... e' rimato scommentato !!! s="[S.l.] : Mariotti 1820, 2017";

	if (s.GetLastChar() == ')') // fine sottoarea luogo stampa?
	{

		int idx = s.IndexCharFrom('(', s.Length()-1, CString::backward); // 26/07/2014
		if (idx == -1)
		{ // Manca la parentesi aperta.
//			s.PrependChar('('); // Aggiungiamola noi per gestire luogo di stampa
//			retb = s.Split(stringVect, "(");

			// Gestiamo come PRIMA sottoarea. Mail Mataloni 11/20/2015
			CString *aNewString = new CString (s.data());
			stringVect.Add(aNewString);
#ifdef DEBUG_ARGE
			eccSoloTondaChiusaCtr++;
#endif
		}
		else
		{
			retb = s.Split(stringVect, "(");
			// 11/02/2016 Elena Ravelli RMLE030371 le parentesi che contengono il punto escalamativo dovrebbero essere quadre e non tonde.
			if (stringVect.Length() > 2)
			{
				// Accodiamo l'eccesso nel secondo elemento
				CString *s = stringVect.Entry(1);
				for (int i=2; i < stringVect.Length(); i++ )
				{
					s->AppendChar('(');
					s->AppendString(stringVect.Entry(i));
				}

			}
		}
	}
	else
	{
		// abbiamo una parentesi chiusa non come ultimo carattere?
		if (s.Find(')'))
			{ // Si, non consideriamo sottoarea cio' che sta tra le ()
			CString *aNewString = new CString (s.data());
			stringVect.Add(aNewString);
#ifdef DEBUG_ARGE
	eccParentesiTondeNonSottoarea++;
#endif

			}
// Da non gestire. Mail Mataloni 11/20/2015
		else if (s.IndexCharFrom('(', s.Length()-1, CString::backward) > -1 )
			{ // abbiamo una parentesi aperta ma non chiusa.
//			retb = s.Split(stringVect, "("); // 21/09/2015 Dividiamo la pubblicazione dalla stampa

			// Consideriamo come PRIMA sottoarea. Mail Mataloni 11/20/2015
			CString *aNewString = new CString (s.data());
			stringVect.Add(aNewString);

#ifdef DEBUG_ARGE
	eccSoloTondaApertaCtr++;
#endif
			}
		else // nessuna sottoarea
			{
			CString *aNewString = new CString (s.data());
			stringVect.Add(aNewString);
			}


	}


	df = new DataField("210", ' ', ' ');
	if (stringVect.length() && !stringVect.Entry(0)->IsEmpty()) // 13/08/2015
		creaTag210_sottoarea(df, stringVect.Entry(0), SOTTOAREA_PUBBLICAZIONE);

	if (stringVect.length() > 1 && !stringVect.Entry(1)->IsEmpty())
		creaTag210_sottoarea(df, stringVect.Entry(1), SOTTOAREA_STAMPA);

#undef DEBUG_ARGE

#ifdef DEBUG_ARGE
s.PrependString("-->>");
sf = new Subfield('Z', &s);
df->addSubfield(sf);
#endif

#define DEBUG_ARGE

	stringVect.DeleteAndClear();

	return df;
} // End creaTag210_PubblicazioneProduzioneDistribuzione


void Marc4cppDocumento::creaTag210_sottoarea(DataField *df, CString *sottoarea, int type)
{
	ATTValVector<CString *> stringLuoghiVect;
	ATTValVector<CString *> stringEditoreTipografoVect;
	CString *sPtr;
	int from, len;
	CString daData;
	Subfield *sf;
	int virgolaInString=0; // 08/09/17

	char * subfieldsPubblicazione =	"abcd";
	char * subfieldsStampa 	=		"efgh";
	char * subfields;

	if (type == SOTTOAREA_PUBBLICAZIONE)
		{
		subfields = subfieldsPubblicazione;
		}
	else
		{ // SOTTOAREA_STAMPA
		subfields = subfieldsStampa;
		if (sottoarea->GetLastChar()== ')')
			sottoarea->ExtractLastChar();  // Remove )
		}

	// Individuiamo le sottoaree
	//  LIBRONE VERDE										UNIMARC
	//	Sottoarea 1
	//		$a Luogo di pubblicazione						luogo
	//		$b Nome delI'editore							indirizzo
	//		$c Indicazione delta funzione di distributore	nome dell'editore
	//		$d Data di pubblicazione						data
	//
	//	Sottoarea 2 (tra parentesi TONDE)
	//		$e Luogo di stampa								luogo
	//		$f Nome del tipografo							indirizzo
	//		$e Data di stampa								nome
	//		$h Riproduzioni facsimilari						data

	// E. L'indicazione della funzione di distributore è racchiusa tra	parentesi quadre ( [ ] ). (GUIDA_SBN_ottobre_2013.pdf)
	// Le parentesi quadre le lascciamo tali e quali

// Gestiamo la sottoarea
// ------------------------------

// Scomponiamo ora i vari luoghi
sottoarea->Split(stringLuoghiVect, " ; ");	// C. Un secondo o successivo luogo di pubblicazione è
											// preceduto da spazio, punto e virgola, spazio ( ; ). (GUIDA_SBN_ottobre_2013.pdf)

// Cicliamo sui luoghi
for (int i=0; i < stringLuoghiVect.length(); i++)
{
	sPtr = stringLuoghiVect.Entry(i);
	if (sPtr->GetLastChar() == ' ')
		sPtr->ExtractLastChar();

	// Troviamo tutti gli editori
	stringLuoghiVect.Entry(i)->Split(stringEditoreTipografoVect, " : "); // D. Il nome di ciascun editore (o distributore) è
																		 // preceduto da spazio, due punti, spazio ( : ). (GUIDA_SBN_ottobre_2013.pdf)
	for (int j =0; j < stringEditoreTipografoVect.length(); j++)
	{
//		if (stringEditoreTipografoVect.Entry(j)->IsEmpty())
//			continue;

		// Abbiamo una data?
		sPtr = stringEditoreTipografoVect.Entry(j);

		// 13/11/2015 Non e' detto che cio\ che segue una virgola sia una data
		// es. ". - Princeton, N. J." (GUIDA_SBN_ottobre_2013.pdf)
		// Controlliamo se dopo l'ultima virgola effettivamente abbiamo una data

// start 01/07/2016
//		int idx = sPtr->IndexLastSubString(", "); // F. La data di pubblicazione è preceduta da virgola, spazio (, ). (GUIDA_SBN_ottobre_2013.pdf)
//		if (idx > -1) {
//			// 13/11/2015 NON + VERO Tutto cio' che viene dopo l'ultima virgola e' considerata data (Mail Barbieri dle 21/07/2015)
//			from = idx + 2; len = sPtr->Length()-2-idx;
//			if (CMisc::isDate((sPtr->data()+from), len )) // 13/11/2015 Verifichiamo ce sia effettivamente una data
//				{ // Abbiamo una data
//				daData.assign(sPtr->SubstringData(from), len); // prendiamo l'area della data
//				sPtr->CropRightFrom(idx);
//#ifdef DEBUG_ARGE
//	dataPrecedutaDaVirgolaCtr++;
//#endif
//
//				}
//			else
//			{
//#ifdef DEBUG_ARGE
//	eccTestoDopoVirgolaSpazioCtr++;
//#endif
//			}
//		}
// end 01/07/2016

		// 01/07/2016 Gestione data nel mezzo del testo e non in coda (RMSE\097965 KO)
		// $eVenetijs$himpensis non minimis curaque cum emendatione ... Casparis Grosch et Stephani Roemer. Opera ... arte ... Johannis haman de Landoia dictus hertzog, 1496, pridie calendas Septembris

		int until = sPtr->Length()-1;
		int idx;
		while ((idx = sPtr->IndexLastSubStringUntil(", ", until)) != -1)
		{
			virgolaInString++;
			from = idx + 2; len = sPtr->Length()-2-idx;
			if (CMisc::isDate((sPtr->data()+from), len )) // 13/11/2015 Verifichiamo ce sia effettivamente una data
				{ // Abbiamo una data
				daData.assign(sPtr->SubstringData(from), len); // prendiamo l'area della data
				sPtr->CropRightFrom(idx);
				break;
			#ifdef DEBUG_ARGE
				dataPrecedutaDaVirgolaCtr++;
			#endif
				}
			until = idx-1;
		}

		if (daData.IsEmpty() &&
			CMisc::isDate(stringEditoreTipografoVect.Entry(j)->data(), stringEditoreTipografoVect.Entry(j)->Length())
			)
		{ // Gestione data non preceduta da virgola spazio ', '
			sf = new Subfield(*(subfields+3), stringEditoreTipografoVect.Entry(j)); // 'd' / 'h' Data pubblicazione / Data stampa
			df->addSubfield(sf);
#ifdef DEBUG_ARGE
	eccDataNonPrecedutaDaVirgolaCtr++;
#endif

		}
		else
		{
			if(!stringEditoreTipografoVect.Entry(j)->IsEmpty())
				{
				stringEditoreTipografoVect.Entry(j)->Strip('*');


//				if (CMisc::isDate(stringEditoreTipografoVect.Entry(j)->data(), stringEditoreTipografoVect.Entry(j)->Length()))
				// 08/09/17 AGR0000744	prova con 210 "[S.l.] : Mariotti 1820, 2017" deve sare $a,$c,$d invede da $a,$d,$d
				if (!virgolaInString && CMisc::isDate(stringEditoreTipografoVect.Entry(j)->data(), stringEditoreTipografoVect.Entry(j)->Length()))
				{ // Gestione data non preceduta da virgola spazio ', '
					sf = new Subfield(*(subfields+3), stringEditoreTipografoVect.Entry(j)); // 'd' / 'h' Data pubblicazione / Data stampa
					df->addSubfield(sf);
#ifdef DEBUG_ARGE
	eccDataNonPrecedutaDaVirgolaCtr++;
#endif
				}
				else
				{
					if (!j)
						sf = new Subfield(*subfields, stringEditoreTipografoVect.Entry(j)); // 'a' / 'e' Luogo pubblicazione / stampa
					else
						sf = new Subfield(*(subfields+2), stringEditoreTipografoVect.Entry(j)); // 'c' /'g' Editore / Stampatore
					df->addSubfield(sf);

				}


				}

			if (!daData.IsEmpty())
				{
				sf = new Subfield(*(subfields+3), &daData); // 'd' / 'h' Data pubblicazione / Data stampa
				df->addSubfield(sf);
				daData.Clear();
				}
		}
	} // End ciclo editori

	stringEditoreTipografoVect.DeleteAndClear();
}

stringLuoghiVect.DeleteAndClear();

} // End creaTag210_sottoarea





/*!

\brief <b>183  CODED DATA FIELD –– Carrier Type  (area zer0)</b>
 <table>
 <tr>
 <th valign=top>Definizione</th><th>Descrizione</th>
 </tr>

 <tr><td valign=top>Tag</td><td>200</td></tr>
 <tr><td valign=top>Descrizione</td>
	 <td>This field contains coded data specifying the carrier type of the resource being described.</td></tr>
 <tr><td valign=top>Obbligatorieta'</td>
	 <td>Facoltativo</td></tr>
 <tr><td valign=top>Ripetibilita'</td>
	 <td>Non Ripetibile</td></tr>
 <tr><td valign=top>Indicatore 1</td>
	 <td>\li blank (not defined)</td></tr>
 <tr><td valign=top>Indicatore 2</td>
	 <td>Display Indicator
		\li 0 - Not used to generate displays.
		\li 1 - Used to generate displays.
		\li # - Information not provided.
	 </td></tr>
  <tr><td valign=top>Sottocampi</td>
	 <td>
		\li a - [Reserved for ISBD Carrier Type Code].
		\li c - Carrier Type Code. Ripetibile.
				A coded value specifying the carrier type applying to the resource, according to the provisions of the
				system specified in subfield $2. Mandatory if $a is not present. Repeatable when several carrier types,
				associated with the same media type, are present in the resource.
		\li 2 -  System code. Source of the code used in $c.
				Mandatory if $c is present. NON Ripetibile.
		\li 6 - Interfield Linking Data.
				Data required to link the field with other fields in the record (for example with field 182 -
				Coded data Field: Media Type). Repeatable.
		\li 8 - Materials specified.
				Part of the described materials to which the field applies. Repeatable.
	 </td></tr>
  <tr><td valign=top>NOTE</td><td></td></tr>

 </table>

 */

DataField * Marc4cppDocumento::creaTag183_Area0_TipoSupporto(char *bid) {
	DataField *df = 0;
	Subfield *sf;
//	CString a,b; // sottocampi

//	tbTitset1->dumpRecord();

	char *ts = tbTitset1->getField(tbTitset1->s183_tp_supporto_1);
	if (!ts || !*ts)
		return df;
	df = new DataField("183", 3);
	df->setIndicator1(' ');
//	df->setIndicator2('0');
	df->setIndicator2('1'); // il 2. indicatore deve essere ‘1’, per generare una prospettazione (in mancanza di un 283 contenente una descrizione in forma testuale)

	// Prima istanza (obbligatoria)

//	sf = new Subfield('a', ts, 2); // take 1
//	df->addSubfield(sf);

//	sf = new Subfield('c', ts, 2); // 22/12/15 Roveri/Inera
//	df->addSubfield(sf);

	//	sf = new Subfield('6', "01", 2); // Riferimento alla prima istanza
	sf = new Subfield('6', "z01", 3);
	df->addSubfield(sf);


	sf = new Subfield('a', ts, 2); // 20/09/2017 mail Mataloni - applicare standard del 4-6 aprile 2016
	df->addSubfield(sf);



//	sf = new Subfield('2', "rdacontent", 10); // 22/12/15 Roveri/Inera
//	df->addSubfield(sf);

	sf = new Subfield('2', "RDAcarrier", 10); // 20/09/2017 mail Mataloni
	df->addSubfield(sf);






	marcRecord->addDataField(df);

	// Abbiamo una seconda istanza?
	ts = tbTitset1->getField(tbTitset1->s183_tp_supporto_2);
	if (ts && *ts)
	{
		df = new DataField("183", 3);
		df->setIndicator1(' ');
		df->setIndicator2('0');

//		sf = new Subfield('a', ts, 2);
//		df->addSubfield(sf);

//		sf = new Subfield('6', "02", 2);
		sf = new Subfield('6', "z02", 3);
		df->addSubfield(sf);

		sf = new Subfield('c', ts, 2); // 22/12/15 Verbale 23/04/2015
		df->addSubfield(sf);

		sf = new Subfield('2', "rdacontent", 10);
		df->addSubfield(sf);


		marcRecord->addDataField(df);
	}

	return df;
} // End creaTag183_Area0_TipoSupporto



DataField * Marc4cppDocumento::creaTag300_321_NoteGenerali_ConRiferimenti(char * bid) {
	DataField *df = 0;
	Subfield *sf;
	CString s;
	if (!tbNota300->loadRecord(bid))
        {
		 SignalAnError(__FILE__,__LINE__,"Nota generale con riferimento mancante per bid %s", bid);
		return df;
        }
//tbNota300->dumpRecord();

    s = tbNota300->getField(tbNota300->ds_nota);
	char *areaStartPtr = s.data();
	//char *areaEndPtr;

	//	CString s = areaStartPtr;
	long start = 0;
	char *ptr = areaStartPtr;
	char *ptr2;

	// Generiamo le 300 (la prima dei riferimenti ripulita dai link verso DB o REPERTORI)
	while (ptr = strstr(areaStartPtr + start, ". - ")) {
		df = new DataField();
		df->setTag("300");
		//		df->setIndicator1('#');
		//		df->setIndicator2('#');

		*ptr = 0;

		ptr2 = areaStartPtr + start;
		if (*ptr2 == '(')
			ptr2++;

		sf = new Subfield('a', ptr2, ptr-ptr2);
		//sf->setData();
		df->addSubfield(sf);
		start = (ptr + 4) - areaStartPtr;

		marcRecord->addDataField(df);
	}

	// Nota vuota perche' tutti riferimenti finiti in 321?
	if (*areaStartPtr)
	{ // No abbiamo qualcosa
		df = new DataField();
		df->setTag("300");

		ptr2 = areaStartPtr + start;
		sf = new Subfield('a', ptr2, s.Data()+s.Length()-ptr2); // , areaEndPtr-ptr2
		df->addSubfield(sf);
		marcRecord->addDataField(df);
	}



	// Generiamo i link verso DB o REPERTORI
	elaboraNote321(bid);




	return df;
} // End creaTag300_321_NoteGenerali_ConRiferimenti





