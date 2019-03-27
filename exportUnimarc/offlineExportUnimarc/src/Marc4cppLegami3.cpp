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
 * Marc4cppLegami.cpp
 *
 *  Created on: 31-aug-2011
 *      Author: Arge
 */


#include "Marc4cppLegami.h"
#include "BinarySearch.h"
#include <bits/stringfwd.h>
#include "TbReticoloTit.h"
#include "../include/library/CTokenizer.h"
#include "TbfBibliotecaInPolo.h"
#include "../include/library/CString.h"
#include "library/CMisc.h"
#include "library/macros.h"
#include "MarcGlobals.h"
#include "TbReticoloAut.h"

#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

#define VOCE_STRUMENTO

using namespace std;

extern CKeyValueVector *codiciOrgaKV; // 26/01/2015

extern void SignalAnError(const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);
extern void SignalAWarning(const OrsChar *Module, OrsInt Line, const OrsChar * MsgFmt, ...);

/*!
\brief <b>Tag 314 - Nota legame titolo/autore</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>314</td></tr>
<tr><td valign=top>Descrizione</td><td>Nota legame titolo/autore</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Testo della nota. Non ripetibile.
        <li>9 - identificativo titolo di arrivo del legame. Non ripetibile.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppLegami::creaTag314_NotaLegameTitoloAutore(char *bid, char *entryReticoloPtr, int pos) {
	DataField *df = 0;

	Subfield *sf;
	//	bool retb;
	char vid[10 + 1];
	vid[10] = 0;

	char cdRelazione[3 + 1];
	cdRelazione[3]=0; // eol

	// Costruiamo la chiave di ricerca del titolo/autore
	CString key = bid;
	CString key1 = bid;
	// Prendiamo il VID

	char *BufTailPtr, *aString;
	BufTailPtr = vid;
	aString =  entryReticoloPtr + pos + 5;
	MACRO_COPY_FAST(10);
//	memcpy(vid, entryReticoloPtr + pos + 5, 10);
	key.AppendString(vid, 10);
	key1.AppendString((char *)"&$%", 3);
	key1.AppendString(vid, 10);

	// Carichiamo il record della tr_tit_aut
//	if (!trTitAut->loadRecord(key.data()))
//		return df;

	bool found = false;


	if (trTitAut->existsRecordNonUnique(key.data())) {
			while (trTitAut->loadNextRecord(key1.data()))
		{
				cdRelazione[0] = *(aString+13);
				cdRelazione[1] = *(aString+14);
				cdRelazione[2] = *(aString+15);
//printf ("\ncd rel: %s", cdRelazione);
				if (!strcmp(cdRelazione, trTitAut->getField(trTitAut->cd_relazione)))
				{
					found = true;
					break;
				}
		}
	}

	if (!found)
		return df;


	CString *nota = trTitAut->getFieldString(trTitAut->nota_tit_aut); // Length
	if (nota->IsEmpty()) // Se il campo nota e' vuoto non generare il tag
		return df;

	df = new DataField((char *)"314", 3);

	sf = new Subfield('a', nota);
	//sf->setData(); // trTitAut->getField(trTitAut->nota_tit_aut)
	df->addSubfield(sf);

	if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
		sf = new Subfield('9', vid, 10);
		//sf->setData();
	else // TIPO_SCARICO_OPAC
	{
		pos = 4;
		CString id((char *)"IT\\ICCU\\", 8);
		id.AppendString(vid, pos); // Prendi il Polo
		id.AppendChar('\\');
		id.AppendString(vid+pos, 10-pos);	 // Prendi resto del bid
		//sf->setData();
		sf = new Subfield('9', &id);
	}
	df->addSubfield(sf);
	return df;
} // End creaTag314_NotaLegameTitoloAutore


/*!
\brief <b>Tag 410 - Campi legame titolo collana</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>410</td></tr>
<tr><td valign=top>Descrizione</td><td>Campi legame titolo collana</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 non stampare la nota
        <li>1 stampare la nota.
    </UL>


</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Dati di legame
        <li>v - Sequenza
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/


void Marc4cppLegami::creaTag410_TitoliCollane(
		//CKeyValueVector *reticoliTitTit410KV, // non rieso a passarmi i reticoli
		CKeyValueVector *sottolivelliTitTit410KV,
		CKeyValueVector *legamiTitTit410KV,
		CKeyValueVector *sequenzeTitTit410KV) {

	tree<std::string>::sibling_iterator ch = reticolo.begin().begin(); // Primo figlio di root


	int sottoLivelliTitolo410;
	int occorrenze;
	char bidColl[10 + 1];
	bidColl[10] = 0;
	string str;
	unsigned int pos;
	char *entryReticoloPtr;
	int reticoli410 = legamiTitTit410KV->Length();

	int reticoloEntry = 0;
	char *BufTailPtr, *aString;
	while (ch != reticolo.end().end() && reticoloEntry < (reticoli410)) {
		str = *ch;
		// Troviamo il separatore della gerarchia
		if ((pos = str.find(':')) != string::npos) {
			if (str.find("TIT:", pos + 1) != string::npos) {
				// Prendiamo il BID coll
				entryReticoloPtr = (char *) str.data();

				BufTailPtr = bidColl;
				aString =  entryReticoloPtr + pos + 5;
				MACRO_COPY_FAST(10);
//				memcpy(bidColl, entryReticoloPtr + pos + 5, 10);

				// E' uno dei bid nell'elenco dei legami?
				if (!strcmp(bidColl, (char*) legamiTitTit410KV->GetKey(	reticoloEntry))) {
					// Costruiamo la 410 solo se non esiste gia un legame di livello inferiore in uno degli altri legami 410
					occorrenze = 0;

//					contaOccorrenzeBidInReticoloPer410(reticolo, bidColl,&occorrenze, legamiTitTit410KV, true); // 24/09/2010 ANA0000069 per generare le 2 410
//					if (occorrenze == 1) {
						sottoLivelliTitolo410 = *(int *) sottolivelliTitTit410KV->GetValue(reticoloEntry);
						if (DATABASE_ID == DATABASE_INDICE)
							creaTag410_InCascata_Indice(sottoLivelliTitolo410, ch, legamiTitTit410KV, sequenzeTitTit410KV);
						else
							creaTag410_InCascata_Polo(sottoLivelliTitolo410, ch, legamiTitTit410KV, sequenzeTitTit410KV);

//						if (IS_TAG_TO_GENERATE(225))
						if (TIPO_SCARICO != TIPO_SCARICO_OPAC && IS_TAG_TO_GENERATE(225)) // 04/12/2012 225 NO per OPAC mail Roveri
							creaTag225_AreaCollezione(sottoLivelliTitolo410, ch, legamiTitTit410KV, sequenzeTitTit410KV);
//					}
					reticoloEntry++;
				}
			} // End if "TIT:"
		} // End if ':'
		++ch;
	} // end while


	//print_tree(reticolo, reticolo.begin(), reticolo.end());

	if (DATABASE_ID != DATABASE_INDICE)
		remove_225_410(reticolo);


} // End creaTag410_TitoliCollane


/*!
\brief <b>Tag 422 - Unita' padre del supplemento</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>422</td></tr>
<tr><td valign=top>Descrizione</td><td>Unita' padre del supplemento</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - non stampare la nota
        <li>1 - stampare la nota
    </UL>
</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> 1 - Tag 200 embedded

    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppLegami::creaTag422_PadreSupplemento() {
	DataField *df, *dfSubtag;
	df = new DataField();
	if (notaAlLegame_311)
		df->setIndicator2('1');
	else
		df->setIndicator2('0');

	df->setTag((char *)"422");
	dfSubtag = creaSubtag200(df);
	return df;
} // creaTag422_PadreSupplemento



/*!
\brief <b>Tag 423 - Pubblicato con (Allegato a)</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>423</td></tr>
<tr><td valign=top>Descrizione</td><td>Pubblicato con (Allegato a)</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
 <tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - non stampare la nota
        <li>1 - stampare la nota
    </UL>
</td></tr>
<tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 embedded . Tag 7xx embedded (dati legame titolo autore)
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

DataField * Marc4cppLegami::creaTag423_PubblicatoCon(tree<std::string>::sibling_iterator ch) //
{
	DataField *df, *df7xx, *dfSubtag; // , *df200
	Subfield *sf;
	string str;
	ATTValVector<Subfield*> *subfieldsVector;
	CString s;

	df = new DataField();
	if (notaAlLegame_311)
		df->setIndicator2('1');
	else
		df->setIndicator2('0');

	df->setTag((char *)"423");

	if (TIPO_SCARICO == TIPO_SCARICO_OPAC) // 29/04/10
		dfSubtag = creaSubtag200(df); // per creare anche il sottotag 001
	else
		dfSubtag = creaSubtag200Area1(df, true, true);

	// Costruiamo la parte relativa all'autore
	unsigned int pos;
	while (ch != ch.end()) { // h1
		str = *ch;
		// Troviamo il separatore della gerarchia
		if ((pos = str.find(':')) != string::npos) {
			if (str.find("AUT:", pos + 1) != string::npos) {
				//					std::cout << std::endl << str << std::endl;
				df7xx = creaLegameTitoloAutore((char *) str.data(), pos);
				if (df7xx) {
					subfieldsVector = df7xx->getSubfields();
					Subfield* sf7xx;
					// s = " ";
					s.Clear(); // 15/01/2010 13.50
					s.AppendString(df7xx->getTagString());
					s.AppendChar(df7xx->getIndicator1());
					s.AppendChar(df7xx->getIndicator2());

					sf = new Subfield('1', &s);
					//sf->setData();
					df->addSubfield(sf);

					for (int i = 0; i < subfieldsVector->length(); i++) {
						sf7xx = subfieldsVector->Entry(i);

						sf = new Subfield(sf7xx->getCode(), sf7xx->getDataString());
						//sf->setData();
						df->addSubfield(sf);
					}
					delete df7xx;
				}
			}
		}
		++ch;
		siArg = 0; // CLEAR just in case we forget reassignement
	} // end while

	return df;
} // End creaTag423

/*!
\brief <b>Tag 430 - Continuazione di</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>430</td></tr>
<tr><td valign=top>Descrizione</td><td>Continuazione di</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
 <tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - non stampare la nota
        <li>1 - stampare la nota
    </UL>
</td></tr>
<tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 embedded . Tag 7xx embedded (dati legame titolo autore)
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppLegami::creaTag430_ContinuazioneDi() {
	DataField *df, *dfSubtag;
	df = new DataField();
	if (notaAlLegame_311)
		df->setIndicator2('1');
	else
		df->setIndicator2('0');
	df->setTag((char *)"430");

	dfSubtag = creaSubtag200(df);
	return df;
} // creaTag430ContinuaIn


/*!
\brief <b>Tag 431 - Continuazione in</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>431</td></tr>
<tr><td valign=top>Descrizione</td><td>Continuazione in</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
 <tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - non stampare la nota
        <li>1 - stampare la nota
    </UL>
</td></tr>
<tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 embedded.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/



DataField * Marc4cppLegami::creaTag431_ContinuaInParteIn() {
	DataField *df, *dfSubtag;
	df = new DataField();
	if (notaAlLegame_311)
		df->setIndicator2('1');
	else
		df->setIndicator2('0');
	df->setTag((char *)"431");

	dfSubtag = creaSubtag200(df);
	return df;
} // creaTag431_ContinuaInParteIn





/*!
\brief <b>Tag 434 - Ha assorbito</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>434</td></tr>
<tr><td valign=top>Descrizione</td><td>Ha assorbito</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
 <tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - non stampare la nota
        <li>1 - stampare la nota
    </UL>
</td></tr>
<tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 embedded.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/


DataField * Marc4cppLegami::creaTag434_AssorbitoDa() {
	DataField *df, *dfSubtag;
	df = new DataField();
	if (notaAlLegame_311)
		df->setIndicator2('1');
	else
		df->setIndicator2('0');
	df->setTag((char *)"434");

	dfSubtag = creaSubtag200(df);
	return df;
} // creaTag434_AssorbitoDa



/*!
\brief <b>Tag 447 - Si fonde con per formare</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>447</td></tr>
<tr><td valign=top>Descrizione</td><td>Si fonde con per formare</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
 <tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - non stampare la nota
        <li>1 - stampare la nota
    </UL>
</td></tr>
<tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 embedded.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppLegami::creaTag447_SiFondeConPerFormare() {
	DataField *df, *dfSubtag;
	df = new DataField();
	if (notaAlLegame_311)
		df->setIndicator2('1');
	else
		df->setIndicator2('0');
	df->setTag((char *)"447");

	dfSubtag = creaSubtag200(df);
	return df;
} // creaTag447_SiFondeConPerFormare

/*!
\brief <b>Tag 451 - Altra edizione su stesso supporto</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>451</td></tr>
<tr><td valign=top>Descrizione</td><td>Altra edizione su stesso supporto</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
 <tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - non stampare la nota
        <li>1 - stampare la nota
    </UL>
</td></tr>
<tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 embedded.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppLegami::creaTag451_AltraEdizioneStessoSupporto() {
	DataField *df, *dfSubtag;
	df = new DataField();
	if (notaAlLegame_311)
		df->setIndicator2('1');
	else
		df->setIndicator2('0');
	df->setTag((char *)"451");

	dfSubtag = creaSubtag200(df);
	return df;
} // creaTag451_AltraEdizioneStessoSupporto

/*!
\brief <b>Tag 454 - Titolo originale</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>454</td></tr>
<tr><td valign=top>Descrizione</td><td>Titolo originale</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
 <tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - non stampare la nota
        <li>1 - stampare la nota
    </UL>
</td></tr>
<tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 embedded.
        <li>1 - Tag 7xx embedded.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
//DataField * Marc4cppLegami::creaTag454_TitoloOriginale(const tree<std::string>& tr) {
DataField * Marc4cppLegami::creaTag454_TraduzioneDi(const tree<std::string>& tr) {
	DataField *df = 0;
	DataField *dfSubtag;
	CString entryReticolo;
	//	char vid[10+1];
	//	vid[10]=0;


	// Troviamo l'autore principale del BID
	//	int pos = getVidAutoreResponsabilitaPrincipale(tr, entryReticolo);
	//	memcpy (vid, autoreResponsabilitaPrincipale.data()+pos+4, 10);

// 25/11/2010
	int pos = getVidFirstAutore(tr, entryReticolo); // 26/01/2010 9.57.
//	if (pos == -1)
//		return df;


	df = new DataField();
	if (notaAlLegame_311)
		df->setIndicator2('1');
	else
		df->setIndicator2('0');

	df->setTag((char *)"454");


	CString *lingua = tbTitolo->getFieldString(tbTitolo->cd_lingua_1); // 30/05/2016
	if (lingua && !lingua->IsEmpty())
	{
		Subfield *sf;
		sf = new Subfield('x', lingua); // Etichetta di servizio per la 101 $b. Da rimuovere una volta impostata la 101
		df->addSubfield(sf);

		lingua = tbTitolo->getFieldString(tbTitolo->cd_lingua_2); // 30/05/2016
		if (lingua && !lingua->IsEmpty())
		{
			Subfield *sf;
			sf = new Subfield('y', lingua); // Etichetta di servizio per la 101 $b. Da rimuovere una volta impostata la 101
			df->addSubfield(sf);

			lingua = tbTitolo->getFieldString(tbTitolo->cd_lingua_3); // 30/05/2016
			if (lingua && !lingua->IsEmpty())
			{
				Subfield *sf;
				sf = new Subfield('z', lingua); // Etichetta di servizio per la 101 $b. Da rimuovere una volta impostata la 101
				df->addSubfield(sf);
			}
		}
	}


	dfSubtag = creaSubtag200(df);

// 25/11/2010
//	//	dfSubtag = creaSubtag7xx(df);
	if (pos != -1)
	{
		DataField *dfLegame;
		dfLegame = creaLegameTitoloAutore(entryReticolo.data(), pos);
		if (dfLegame) {
			Subfield *sf;
			sf = new Subfield('1', dfLegame->toSubTag());
			//char *subtag = dfLegame->toSubTag();
			//sf->setData();
			df->addSubfield(sf);
			delete dfLegame;
		}
	}

	return df;
} // end creaTag454_TraduzioneDi


/*!
\brief <b>Tag 488 - Continuazione di</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>488</td></tr>
<tr><td valign=top>Descrizione</td><td>Legame continuazione di</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
 <tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - non stampare la nota
        <li>1 - stampare la nota
    </UL>
</td></tr>
<tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 embedded.
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppLegami::creaTag488_ContinuazioneDi(char *bid, char *bidColl,
		char naturaBase, CString *tpLegame, char naturaColl) {
	DataField *df = 0;
	if (tpLegame->isEqual("04") && naturaColl == 'M') {
		//		Subfield *sf;

		df = new DataField();
		if (notaAlLegame_311)
			df->setIndicator2('1');
		else
			df->setIndicator2('0');

		df->setTag((char *)"488");
		creaSubtag200(df);
	}

	return df;
} // End creaTag488_ContinuazioneDi






/*!
\brief <b>Tag 500 - Legame a Titolo uniforme</b>
\brief <b>Tag 506 - Legame a Titolo dell'opera (02/02/2017, sostituisce la 500)</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>500</td></tr>
<tr><td valign=top>Descrizione</td><td>Legame a Titolo uniforme (dicasi Titolo di raggruppamento nel verdone)</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>
    <UL>
        <li>0 titolo non significativo
        <li>1 titolo significativo
    </UL>
</td></tr>
<tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 titolo non usato come intestazione
        <li>1 titolo usato come intestazione.<BR>
        Se il titolo di natura A non ha autori collegati e il titolo di natura M non ha autori collegati di tipo 1 o 2, il 2ndo indicatore deve essere 1. Vedi anche es. UBO3006166.<BR>
        Se il titolo di natura A non ha autori collegati ma il titolo di natura M ha un autore collegato di tipo 1 o 2, il 2ndo indicatore deve essere 0.
    </UL>
</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 area 1 embedded
        <li>3 - Bid
        <li>9 - Legame all'autore
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppLegami::creaTag500_TitoloUniforme(
		const tree<std::string>& tr, bool responsabilitaM1o2) {
	DataField *df = 0;
	Subfield *sf;


	df = new DataField();

	df->setTag((char *)"500"); // Fix 16/01/2010 21.40

	df->setIndicator1('1');

	if (responsabilitaM1o2)
		df->setIndicator2('0');
	else
		df->setIndicator2('1');

	CString * strIndiceAree = tbTitolo->getFieldString(tbTitolo->indice_isbd);
	ATTValVector<CString *> areeVect;
	bool retb;
	retb = strIndiceAree->Split(areeVect, ';');
	int length, startOffset;
	char *areaStartPtr, *areaEndPtr; // , *areaNotePtr
	const char *isbd = tbTitolo->getField(tbTitolo->isbd);

	CString *lingua = tbTitolo->getFieldString(tbTitolo->cd_lingua_1); // 30/05/2016
	if (lingua && !lingua->IsEmpty())
	{
		Subfield *sf;
		sf = new Subfield('x', lingua); // Etichetta di servizio per la 101 $b. Da rimuovere una volta impostata la 101
		df->addSubfield(sf);

		lingua = tbTitolo->getFieldString(tbTitolo->cd_lingua_2); // 30/05/2016
		if (lingua && !lingua->IsEmpty())
		{
			Subfield *sf;
			sf = new Subfield('y', lingua); // Etichetta di servizio per la 101 $b. Da rimuovere una volta impostata la 101
			df->addSubfield(sf);

			lingua = tbTitolo->getFieldString(tbTitolo->cd_lingua_3); // 30/05/2016
			if (lingua && !lingua->IsEmpty())
			{
				Subfield *sf;
				sf = new Subfield('z', lingua); // Etichetta di servizio per la 101 $b. Da rimuovere una volta impostata la 101
				df->addSubfield(sf);
			}

		}
	}



//tbTitolo->dumpRecord();

	for (int i = 0; i < areeVect.Length(); i++) {
		if (areeVect.Entry(i)->IsEmpty())
			continue;;

		startOffset = atoi(areeVect.Entry(i)->data() + 4) - 1;
		areaStartPtr = (char *) isbd + startOffset;

		if ((i + 1) == areeVect.Length() || areeVect.Entry(i + 1)->IsEmpty()) {
			length = tbTitolo->getFieldString(tbTitolo->isbd)->Length() - startOffset; // Ultima area

		} else {
			length = atoi(areeVect.Entry(i + 1)->data() + 4) - atoi(areeVect.Entry(i)->data() + 4) - 3; // Non ultima area
		}

		areaEndPtr = areaStartPtr + length; // ' - ' + EOS
		*areaEndPtr = 0;

//		if (areeVect.Entry(i)->StartsWith("200-")) // Area 1 - Titolo e responsabilit�
		if (areeVect.Entry(i)->StartsWith("200-") || areeVect.Entry(i)->StartsWith("A-")) // 09/09/2015 Mantis 5975. Titolo uniforme
		{
			//	creaTag500Area1(areaStartPtr, df);
			creaSubtag200Area1(df, false, true);
			Subfield *sfC, *sfA; // Mantis 4787
			sfA = df->getSubfield('a');
			// Viene generata anche la $c che non esiste nella 500. Far confluire la $c nella $a
			sfC = df->getSubfield('c');
			if (sfC)
			{
				sfA->appendData(sfC->getData());
				df->removeSubfield('c');
			}
			// Viene generata anche la $e che non esiste nella 500. Far confluire la $e nella $a
			Subfield *sfE; // Barbieri mail 05/12/2016
			sfE = df->getSubfield('e');
			if (sfE)
			{
				//sfA = df->getSubfield('a');
				sfA->appendData((char *)" : ");
				sfA->appendData(sfE->getData());
				df->removeSubfield('e');
			}

		}

	} // End for
	areeVect.DeleteAndClear();

	//--------
	//	char *bid = tbTitolo->getField(tbTitolo->bid);
	//	sf = new Subfield('3');
	//	sf->setData(bid);
	//	df->addSubfield(sf);
	//--------
	// 15/02/2010 12.02 Rimesso in occasione del fix CFI per Nella scheda MUS0316888 manca l'etichetta per il collegamento con il titolo di natura A

	//sf->setData(tbTitolo->getField(tbTitolo->bid));

	char *bid = tbTitolo->getField(tbTitolo->bid); // 28/04/10 // const
//printf("\nMarc4cppLegami::creaTag500_TitoloUniforme bid=%s", bid);

	if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
		sf = new Subfield('3', bid, 10);
		//sf->setData();
	else // TIPO_SCARICO_OPAC
	{
		CString id((char *)"IT\\ICCU\\", 8);
		int pos =3;
		if (isAnticoE(bid) || isAutore(bid))
			pos = 4;

		id.AppendString(bid, pos); // Prendi il Polo

		id.AppendChar('\\');
		id.AppendString(bid+pos, 10-pos);	 // Prendi resto del bid

		sf = new Subfield('3', &id);
		//sf->setData();
	}


	df->addSubfield(sf);

	// Mettiamo i lriferimento all'autore 15/02/2010 12.15
	tree<std::string>::sibling_iterator ch = tr.begin().begin(); //h1
//	if (ch != tr.end().end()) {
	while (ch != tr.end().end()) {
		string str = *ch;
		long pos;
		if ((pos = str.find("AUT:")) != -1) {

			// Prendiamo l'autore con responsabilita' principale
			if (*(str.data()+pos+15) != '1')
			{
				ch++;
				continue;
			}

			idBuffer[10] = 0;
			getEntityId((char *) str.data(), idBuffer);

			// 10/08/2010 15.00 Prendiamo la descrizione dell'autore
			bool retb = tbAutore->loadRecord(idBuffer);
			if (!retb)
			{
				SignalAnError(__FILE__, __LINE__,"Non trovo autore con vid %s", idBuffer);
			}
			else
			{
				if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
					sf = new Subfield('9', idBuffer, 10);
				else // TIPO_SCARICO_OPAC
				{
					char *ptr = strchr (tbAutore->getField(tbAutore->ds_nome_aut), '*');
					if (ptr)
						sf = new Subfield('9', ptr+1,tbAutore->getFieldLength(tbAutore->ds_nome_aut) - ((ptr+1)-tbAutore->getField(tbAutore->ds_nome_aut)) ); // skip no sort
						//sf->setData();
					else
						sf = new Subfield('9', tbAutore->getFieldString(tbAutore->ds_nome_aut));
						//sf->setData();
					} // end else
				df->addSubfield(sf);
			} //end else
		break;
		} // End if AUT
		else
			ch++;
	} // End loop relazioni

	return df;
} // End creaTag500_TitoloUniforme




/*!
\brief <b>Tag 510 - Titolo parallelo</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>510</td></tr>
<tr><td valign=top>Descrizione</td><td>Titolo parallelo</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>
    <UL>
        <li>0 titolo non significativo
        <li>1 titolo significativo
    </UL>
</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 area 1 embedded
        <li>9 - Legame al titolo
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

DataField * Marc4cppLegami::creaTag510_TitoloParallelo() {
	DataField *df;
	Subfield *sf;

	df = new DataField();
	df->setIndicator1('1');
	df->setIndicator2(' ');
	df->setTag((char *)"510");


	creaSubtag200Area1(df, false, true); // Non prendere il tag 200


	char *bid = tbTitolo->getField(tbTitolo->bid); // const
	if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
		sf = new Subfield('9', bid, 10);
	else
//	if (TIPO_SCARICO == TIPO_SCARICO_OPAC)
	{
		CString id((char *)"IT\\ICCU\\", 8);
        int pos =3;
        if (isAnticoE(bid))
            pos = 4;
        id.AppendString(bid, pos); // Prendi il Polo
        id.AppendChar('\\');
        id.AppendString(bid+pos, 10-pos);     // Prendi resto del bid

		sf = new Subfield('9', &id);
	}
	df->addSubfield(sf);

	return df;
} // End creaTag510_TitoloParallelo

/*!
\brief <b>Tag 517 - Altre varianti del titolo</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>517</td></tr>
<tr><td valign=top>Descrizione</td><td>Titolo parallelo</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>
    <UL>
        <li>0 titolo non significativo
        <li>1 titolo significativo
    </UL>
</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 area 1 embedded
        <li>3 - Legame a musica (anche noto come sottotipo legame)
        <li>9 - Legame al titolo
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppLegami::creaTag517_AltreVariantiTitolo(CString* tp_legame_musica) {
	DataField *df;
	Subfield *sf;

	df = new DataField();
	df->setIndicator1('1');
	df->setIndicator2(' ');
	df->setTag((char *)"517");

	creaSubtag200Area1(df, false, true); // Non prendere il tag 200

	if (!tp_legame_musica->IsEmpty())
	{
		sf = new Subfield('e', tp_legame_musica); // anche noto come sottotipo legame
		df->addSubfield(sf);
	}


	char *bid = tbTitolo->getField(tbTitolo->bid); // const

	if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
		sf = new Subfield('9', bid, 10);
	else
//	if (TIPO_SCARICO == TIPO_SCARICO_OPAC)
	{
		CString id((char *)"IT\\ICCU\\", 8);
		int pos = 3;
		if (isAnticoE(bid))
			pos = 4;

		id.AppendString(bid, pos); // Prendi il Polo+1
		id.AppendChar('\\');
		id.AppendString(bid+pos, 10-pos);	 // Prendi resto del bid
		sf = new Subfield('9', &id);
		//sf->setData();
	}
	df->addSubfield(sf);
	return df;
} // end creaTag517_AltreVariantiTitolo



/*!
\brief <b>Tag 520 - Continuazione di (periodici)</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>520</td></tr>
<tr><td valign=top>Descrizione</td><td>Continuazione di (periodici)</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>
    <UL>
        <li>0 titolo non significativo
        <li>1 titolo significativo
    </UL>
</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 area 1 embedded
        <li>9 - Legame al titolo
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppLegami::creaTag520_ContinuazioneDiPeriodici(char *bid,
		char *bidColl, char naturaBase, CString * tpLegame, char naturaColl) {
	DataField *df = 0;
	Subfield *sf;

	if (naturaBase != 'S')
		return df;
	if ((tpLegame->isEqual("04") || tpLegame->isEqual("41") || tpLegame->isEqual(
			"42") || tpLegame->isEqual("43")) && naturaColl == 'S') {
		//		Subfield *sf;

		df = new DataField();
		df->setIndicator1('1');
		df->setTag((char *)"520");
		//		creaSubtag200(df);
		creaSubtag200Area1(df, false, true); // Non prendere il tag 200

		bid = tbTitolo->getField(tbTitolo->bid); // 03/12/2010 CFI Tutte le 5xx  devono avere il bid.
		if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
			sf = new Subfield('9', bid, 10);
		else
		{
			CString id((char *)"IT\\ICCU\\", 8);
	        int pos =3;
	        if (isAnticoE(bid))
	            pos = 4;
	        id.AppendString(bid, pos); // Prendi il Polo
	        id.AppendChar('\\');
	        id.AppendString(bid+pos, 10-pos);     // Prendi resto del bid

			sf = new Subfield('9', &id);
		}
		df->addSubfield(sf);
	}
	return df;
} // end creaTag520_ContinuazioneDiPeriodici


/*!
\brief <b>Tag 530 - Titolo chiave (periodici)</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>530</td></tr>
<tr><td valign=top>Descrizione</td><td>Titolo chiave (periodici)</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>
    <UL>
        <li>0 titolo non significativo
        <li>1 titolo significativo
    </UL>
</td></tr>
<tr><td valign=top>Indicatore 2</td><td>se il titolo ha autore principale vale '1' altrimenti vale '0'.</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 area 1 embedded
        <li>9 - Legame al titolo
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

DataField * Marc4cppLegami::creaTag530_TitoloChiavePeriodici(const tree<
		std::string>& tr) {
	DataField *df;
	Subfield *sf;

	df = new DataField();
	df->setIndicator1('1');
	if (haResponsabilitaPrincipale(tr))
		df->setIndicator2('1');
	else
		df->setIndicator2(' ');

	df->setTag((char *)"530");
	creaSubtag200Area1(df, false, true);

	char *bid = tbTitolo->getField(tbTitolo->bid); // 03/12/2010 CFI Tutte le 5xx  devono avere il bid.
	if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
		sf = new Subfield('9', bid, 10);
	else
	{
		CString id((char *)"IT\\ICCU\\", 8);
        int pos =3;
        if (isAnticoE(bid))
            pos = 4;
        id.AppendString(bid, pos); // Prendi il Polo
        id.AppendChar('\\');
        id.AppendString(bid+pos, 10-pos);     // Prendi resto del bid

		sf = new Subfield('9', &id);
	}
	df->addSubfield(sf);

	return df;
} // end creaTag530_TitoloChiavePeriodici


/*!
\brief <b>Tag 532 - Titolo estrapolato</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>532</td></tr>
<tr><td valign=top>Descrizione</td><td>Titolo estrapolato</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>
    <UL>
        <li>0 titolo non significativo
        <li>1 titolo significativo
    </UL>
</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito.</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>1 - Tag 200 area 1 embedded
        <li>9 - Legame al titolo
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppLegami::creaTag532_TitoloEstrapolato(char *bid,
		char *bidColl, char naturaBase, CString * tpLegame, char naturaColl) {
	DataField *df = 0;
	if (tpLegame->isEqual("08") && naturaColl == 'D' && naturaBase != 'M') {
		//		Subfield *sf;
		Subfield *sf;

		df = new DataField();
		df->setIndicator1('1');
		df->setTag((char *)"532");

		creaSubtag200Area1(df, false, true);

		char *bid = tbTitolo->getField(tbTitolo->bid); // 03/12/2010 CFI Tutte le 5xx  devono avere il bid.
		if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
			sf = new Subfield('9', bid, 10);
		else
		{
			CString id((char *)"IT\\ICCU\\", 8);
	        int pos =3;
	        if (isAnticoE(bid))
	            pos = 4;
	        id.AppendString(bid, pos); // Prendi il Polo
	        id.AppendChar('\\');
	        id.AppendString(bid+pos, 10-pos);     // Prendi resto del bid

			sf = new Subfield('9', &id);
		}
		df->addSubfield(sf);
	}

	return df;
} // end creaTag532_TitoloEstrapolato


/*!
\brief <b>Tag 606 - Soggetti ordinari</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>606</td></tr>
<tr><td valign=top>Descrizione</td><td>Soggetti ordinari</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>
    <UL>
        <li>0 Nessun livello specificato
        <li>1 Termine principale
        <li>2 Termine secondario
        <li>' ' Informazione non disponibile
    </UL>
</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Elemento principale
        <li>x - Suddivisione generale
        <li>2 - Codice di sistema di soggettazione (Soggettario)
 Il $2 delle etichette 6xx dovrebbe contenere, per il nuovo
 soggettario (codice NFI di SBN) la codifica "nsogi" come dichiarato
 nell'Appendice G del Manuale UNIMARC, 3. ed. Il codice FIR rimane uguale.
        <li>3 - Numero record nell'Authority File (CID)

    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

DataField * Marc4cppLegami::creaTag606_SoggettiOrdinari() {
	DataField *df;
	Subfield *sf;

	df = new DataField();
	df->setTag((char *)"606");

	ATTValVector<CString *> stringVect; // 01/02/2010 14.04
	CString *sPtr;

//tbSoggetto->dumpRecord();

// Nota 01/10/2013 evidentemente i campi di indice sono scaricati nello stesso ordine dei campi di polo
//	if (DATABASE_ID == DATABASE_INDICE)
//		sPtr = tbSoggetto->getFieldString(tbSoggetto->ds_soggetto_indice); // 01/10/2013
//	else
		sPtr = tbSoggetto->getFieldString(tbSoggetto->ds_soggetto);
	sPtr->Split(stringVect, " - ");

	sf = new Subfield('a', stringVect.Entry(0));
	df->addSubfield(sf);

	for (int i = 1; i < stringVect.length(); i++) {
		sf = new Subfield('x', stringVect.Entry(i));
		df->addSubfield(sf);
	}


//	if (DATABASE_ID == DATABASE_INDICE)
//		sPtr = tbSoggetto->getFieldString(tbSoggetto->cd_soggettario_indice);
//	else
		sPtr = tbSoggetto->getFieldString(tbSoggetto->cd_soggettario);

//	if ((CString*) tbSoggetto->getFieldString(tbSoggetto->cd_soggettario)->isEqual("NFI"))
	if (sPtr->isEqual("NFI"))

		sf = new Subfield('2', (char *)"nsogi", 5);
	else
		//sf = new Subfield('2', tbSoggetto->getFieldString(tbSoggetto->cd_soggettario));
		sf = new Subfield('2', sPtr);

	df->addSubfield(sf);



	char *cid = tbSoggetto->getField(tbSoggetto->cid); // 28/04/10 // const
	if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
		sf = new Subfield('3', cid, 10);
	else // TIPO_SCARICO_OPAC
	{
		CString id((char *)"IT\\ICCU\\", 8);
		id.AppendString(cid, 4); // Prendi il Polo+1
		id.AppendChar('\\');
		id.AppendString(cid+4, 10-4);	 // Prendi resto del bid
		sf = new Subfield('3', &id);
		//sf->setData();
	}
	df->addSubfield(sf);


if (DATABASE_ID != DATABASE_INDICE) // 01/10/2013 (solo per il POLO)
{
	sf = new Subfield('9', tbSoggetto->getFieldString(tbSoggetto->cd_edizione)); // 05/06/2012
	df->addSubfield(sf);
}


	stringVect.DeleteAndClear();
	return df;
} // end creaTag606_SoggettiNormali


/*!
\brief <b>Tag 700 - Autori personali, responsabilita' principale</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>700</td></tr>
<tr><td valign=top>Descrizione</td><td>Autori personali, responsabilita' principale</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Non ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - Nome in forma diretta
        <li>1 - Cognome
    </UL>
</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Elemento principale. Parte principale del nome dell'autore. (Non ripetibile)
        <li>b - Secondo elemento del nome. Il resto del nome dell'autore. (Ripetibile)
        <li>c - Qualificazione (Ripetibile)
        <li>d - Numerali romani
        <li>f - Date
        <li>g - Iniziali del nome per esteso
        <li>3 - Numero di record nell'Authority File
        <li>4 - Codice del relatore
        <li>
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

DataField * Marc4cppLegami::creaTag700_NomiPersonaliResponsabilitaPrincipale(CString *cdRelazione, CString *cdStrumentoMusicale) {
	DataField *df;
	df = new DataField();
	df->setTag((char *)"700");
	//	creaTag70x(df);

	//char *ds_nome = (char *) tbAutore->getField(tbAutore->ds_nome_aut); // 01/02/2010 9.55.
	char tp_nome = *tbAutore->getField(tbAutore->tp_nome_aut);
	//char *vid = (char *) tbAutore->getField(tbAutore->vid);

	creaTag70xNew(df, tbAutore->getFieldString(tbAutore->ds_nome_aut), tp_nome, tbAutore->getFieldString(tbAutore->vid));

	if (cdRelazione && !cdRelazione->IsEmpty()) {
		Subfield *sf = new Subfield('4', cdRelazione);
		df->addSubfield(sf);

		// 26/01/2015 Gestione del codice strumento musicale (valido anche per la voce  a dire il vero)
		if (cdStrumentoMusicale && cdStrumentoMusicale->Length())
			creaField7xxVoceStrumento(df, cdStrumentoMusicale);
	}

	return df;

} // End creaTag700_NomiPersonaliResponsabilitaPrincipale




/*!
\brief <b>Tag 701 - Autori personali, responsabilita' alternativa</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>701</td></tr>
<tr><td valign=top>Descrizione</td><td>Autori personali, responsabilita' alternativa</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - Nome in forma diretta
        <li>1 - Cognome
    </UL>
</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Elemento principale. Parte principale del nome dell'autore. (Non ripetibile)
        <li>b - Secondo elemento del nome. Il resto del nome dell'autore. (Ripetibile)
        <li>c - Qualificazione (Ripetibile)
        <li>d - Numerali romani
        <li>f - Date
        <li>g - Iniziali del nome per esteso
        <li>3 - Numero di record nell'Authority File
        <li>4 - Codice del relatore
        <li>
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td>Come la 700</td></tr>
</table>
*/

DataField * Marc4cppLegami::creaTag701_NomiPersonaliResponsabilitaAlternativa(CString *cdRelazione, CString *cdStrumentoMusicale) {
	DataField *df;
	df = new DataField();
	df->setTag((char *)"701");
	//	creaTag70x(df);

	//char *ds_nome = (char *) tbAutore->getField(tbAutore->ds_nome_aut); // 01/02/2010 9.55.
	char tp_nome = *tbAutore->getField(tbAutore->tp_nome_aut);
	//char *vid = (char *) tbAutore->getField(tbAutore->vid);

	creaTag70xNew(df, tbAutore->getFieldString(tbAutore->ds_nome_aut), tp_nome, tbAutore->getFieldString(tbAutore->vid));


	if (cdRelazione && !cdRelazione->IsEmpty()) { // 22/09/2010
		Subfield *sf;
		sf = new Subfield('4', cdRelazione);
		df->addSubfield(sf);

		// 26/01/2015 Gestione del codice strumento musicale (valido anche per la voce  a dire il vero)
		if (cdStrumentoMusicale && cdStrumentoMusicale->Length())
			creaField7xxVoceStrumento(df, cdStrumentoMusicale);

	}

	return df;
} // End creaTag701

/*!
\brief <b>Tag 702 - Autori personali, responsabilita' secondaria</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>702</td></tr>
<tr><td valign=top>Descrizione</td><td>Autori personali, responsabilita' secondaria</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - Nome in forma diretta
        <li>1 - Cognome
    </UL>
</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Elemento principale. Parte principale del nome dell'autore. (Non ripetibile)
        <li>b - Secondo elemento del nome. Il resto del nome dell'autore. (Ripetibile)
        <li>c - Qualificazione (Ripetibile)
        <li>d - Numerali romani
        <li>f - Date
        <li>g - Iniziali del nome per esteso
        <li>3 - Numero di record nell'Authority File
        <li>4 - Codice del relatore
        <li>
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td>Come la 700</td></tr>
</table>
*/

DataField * Marc4cppLegami::creaTag702_NomiPersonaliResponsabilitaSecondaria(CString *cdRelazione, char tipoResponsabilita, CString *cdStrumentoMusicale) {
	DataField *df;
	Subfield *sf;
	df = new DataField();
	df->setTag((char *)"702");
	//	creaTag70x(df);

//printf ("\nALTRA 702");

	//char *ds_nome = (char *) tbAutore->getField(tbAutore->ds_nome_aut); // 01/02/2010 9.55.
	char tp_nome = *tbAutore->getField(tbAutore->tp_nome_aut);
	//char *vid = (char *) tbAutore->getField(tbAutore->vid);

	creaTag70xNew(df, tbAutore->getFieldString(tbAutore->ds_nome_aut), tp_nome, tbAutore->getFieldString(tbAutore->vid));

	// $4
//	if (cdRelazione && !cdRelazione->IsEmpty()) { // 05/08/2010
//		sf = new Subfield('4', cdRelazione);
//		df->addSubfield(sf);
//	}
	cdRelazione->Strip(cdRelazione->trailing, ' ');
	if (cdRelazione && !cdRelazione->IsEmpty()) {
		sf = new Subfield('4', cdRelazione);
		df->addSubfield(sf);

		// 26/01/2015 Gestione del codice strumento musicale (valido anche per la voce  a dire il vero)
		if (cdStrumentoMusicale && cdStrumentoMusicale->Length())
			creaField7xxVoceStrumento(df, cdStrumentoMusicale);

//		{
//			sf = new Subfield('4', cdStrumentoMusicale);
//			sf->appendData("=");
//
//			char *descStrumento = codiciOrgaKV->GetValueFromKey(cdStrumentoMusicale->data());
//			if (!descStrumento)
//			{
//				sf->appendData("DESCRIZIONE NON TROVATA PER CODICE ");
//				sf->appendData(cdStrumentoMusicale);
//			}
//			else
//				sf->appendData(descStrumento);
//
//			df->addSubfield(sf);
//		}
	}
	else
	{
		if (tipoResponsabilita == TP_RESP_4_RESPONSABILITA_NELLA_PRODUZIONE_MATERIALE
			&& DATABASE_ID == DATABASE_INDICE
			&& TIPO_SCARICO == TIPO_SCARICO_UNIMARC) // UNIMARC Relator code tipografi/editori Mail Contardi 26 Apr 2011
		{
			cdRelazione->assign((char *)"650");
			sf = new Subfield('4', cdRelazione);
			df->addSubfield(sf);
		}

	}

	return df;
} // End creaTag702_NomiPersonaliResponsabilitaSecondaria


/*!
\brief <b>Tag 710 - Autori collettivi, responsabilita' principale</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>710</td></tr>
<tr><td valign=top>Descrizione</td><td>Autori collettivi, responsabilita' principale</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Non ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>
    <UL>
        <li>0 - Autore collettivo
        <li>1 - Congresso
    </UL>
</td></tr>
<tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - Nome in forma inversa.  Viene usato quando la prima parola del nome collettivo o congresso inizia con un'iniziale o nome di persona
        <li>1 - Nome di localita' o di autorita'.  Viene usato per gli autori collettivi quando sono relativi ad agenzie di autorita' o governative
        <li>2 - Nome in forma diretta. Viene usato per tutti gli altri casi
    </UL>
</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Elemento principale. (Non ripetibile)
        <li>b - Suddivisione. (Ripetibile)
        <li>c - Qualificazione. (Ripetibile)
        <li>d - Numero del congresso. (Ripetibile)
        <li>e - Luogo del congresso. (Non ripetibile)
        <li>f - Data del congresso. (Non ripetibile)
        <li>g - Elemento permutato. (Non ripetibile)
        <li>3 - Numero di record nell'Authority File. (Non ripetibile)
        <li>4 - Codice del relatore. (Non ripetibile)
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppLegami::creaTag710_NomeDiEnte_ResponsabilitaPrincipale(CString *cdRelazione, CString *cdStrumentoMusicale) {
	DataField *df;

	df = new DataField();
	df->setTag((char *)"710");

	creaTag71x(df, cdRelazione, 0, cdStrumentoMusicale);

	return df;
} // End creaTag710_NomeDiEnte





/*!
\brief <b>Tag 711 - Autori collettivi, responsabilita' alternativa</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>711</td></tr>
<tr><td valign=top>Descrizione</td><td>Autori collettivi, responsabilita' alternativa</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
    <UL>
        <li>0 - Autore collettivo
        <li>1 - Congresso
    </UL>
<tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - Nome in forma inversa.  Viene usato quando la prima parola del nome collettivo o congresso inizia con un'iniziale o nome di persona
        <li>1 - Nome di localita' o di autorita'.  Viene usato per gli autori collettivi quando sono relativi ad agenzie di autorita' o governative
        <li>2 - Nome in forma diretta. Viene usato per tutti gli altri casi
    </UL>
</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Elemento principale. (Non ripetibile)
        <li>b - Suddivisione. (Ripetibile)
        <li>c - Qualificazione. (Ripetibile)
        <li>d - Numero del congresso. (Ripetibile)
        <li>e - Luogo del congresso. (Non ripetibile)
        <li>f - Data del congresso. (Non ripetibile)
        <li>g - Elemento permutato. (Non ripetibile)
        <li>3 - Numero di record nell'Authority File. (Non ripetibile)
        <li>4 - Codice del relatore. (Non ripetibile)
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppLegami::creaTag711_NomeDiEnte_ResponsabilitaAlternativa(CString *cdRelazione, CString *cdStrumentoMusicale) {
	DataField *df;

	df = new DataField();
	df->setTag((char *)"711");

	creaTag71x(df, cdRelazione, 0, cdStrumentoMusicale);
	return df;
} // End creaTag711_NomeDiEnte_ResponsabilitaAlternativa


/*!
\brief <b>Tag 712 - Autori collettivi, responsabilita' secondaria</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>712</td></tr>
<tr><td valign=top>Descrizione</td><td>Autori collettivi, responsabilita' secondaria</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>
    <UL>
        <li>0 - Autore collettivo
        <li>1 - Congresso
    </UL>
</td></tr>
<tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - Nome in forma inversa.  Viene usato quando la prima parola del nome collettivo o congresso inizia con un'iniziale o nome di persona
        <li>1 - Nome di localita' o di autorita'.  Viene usato per gli autori collettivi quando sono relativi ad agenzie di autorita' o governative
        <li>2 - Nome in forma diretta. Viene usato per tutti gli altri casi
    </UL>
</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Elemento principale. (Non ripetibile)
        <li>b - Suddivisione. (Ripetibile)
        <li>c - Qualificazione. (Ripetibile)
        <li>d - Numero del congresso. (Ripetibile)
        <li>e - Luogo del congresso. (Non ripetibile)
        <li>f - Data del congresso. (Non ripetibile)
        <li>g - Elemento permutato. (Non ripetibile)
        <li>3 - Numero di record nell'Authority File. (Non ripetibile)
        <li>4 - Codice del relatore. (Non ripetibile)
        <li>5 - codice ISIL (solo per POLI)
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppLegami::creaTag712_NomeDiEnte_ResponsabilitaSecondaria(CString *cdRelazione, char tipoResponsabilita, CString *cdStrumentoMusicale) {
	DataField *df;
	Subfield *sf;


	df = new DataField();
	df->setTag((char *)"712");

/*
	creaTag71x(df, cdRelazione, tipoResponsabilita);
	if (DATABASE_ID != DATABASE_INDICE) // 06/08/2010
	{
		sf = new Subfield('5',marc4cppDocumento->getBibliotecaRichiedenteScarico()); // 15/02/2010 12.47 DinaP CFI il $5 deve contenere il codice ISIL (IT-FI0098) al posto di BNCF.
		df->addSubfield(sf);
	}
*/
	char tp_nome = *tbAutore->getField(tbAutore->tp_nome_aut); // 25/08/2011 Problma segnalato da Rossana/Pavoletti. $5 errata e superflua
	creaTag71xNew(df, cdRelazione, tbAutore->getFieldString(tbAutore->ds_nome_aut), tp_nome, tbAutore->getFieldString(tbAutore->vid), cdStrumentoMusicale);


	return df;
} // End creaTag712


/*!
\brief <b>Tag 790 - Rinvii autori personali</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>790</td></tr>
<tr><td valign=top>Descrizione</td><td>Rinvii autori personali</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>
    <UL>
        <li>0 - Nome in forma diretta
        <li>1 - Cognome
    </UL>
</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Elemento principale. Parte principale del nome dell'autore. (Non ripetibile)
        <li>b - Secondo elemento del nome. Il resto del nome dell'autore. (Ripetibile)
        <li>c - Qualificazione (Ripetibile)
        <li>d - Numerali romani
        <li>f - Date
        <li>g - Iniziali del nome per esteso
        <li>3 - Numero di record nell'Authority File
        <li>4 - Codice del relatore
        <li>z - Autore in forma accettata, per tipo legame 4xx (Non ripetibile)
        <li>y - Autore in forma accettata, per tipo legame 5xx (Non ripetibile)
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

DataField * Marc4cppLegami::creaTag790_RinvioAutorePersonale(char tipoLegame, char *vidPadre) {
	DataField *df;

	df = new DataField();

	df->setTag((char *)"790");
	//	creaTag70x(df);


	//char *ds_nome = (char *) tbAutore->getField(tbAutore->ds_nome_aut); // 01/02/2010 9.55.
	char tp_nome = *tbAutore->getField(tbAutore->tp_nome_aut);
	//char *vid = (char *) tbAutore->getField(tbAutore->vid);

	creaTag70xNew(df, tbAutore->getFieldString(tbAutore->ds_nome_aut), tp_nome, tbAutore->getFieldString(tbAutore->vid));

	addAutoreFormaAccettata(df,tipoLegame, vidPadre);

	return df;
} // End creaTag790RinvioAutorePersonale


/*!
\brief <b>Tag 791 - Rinvio autore collettivo</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>791</td></tr>
<tr><td valign=top>Descrizione</td><td>Rinvio autore collettivo</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Non ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>
    <UL>
        <li>0 - Autore collettivo
        <li>1 - Congresso
    </UL>
</td></tr>
<tr><td valign=top>Indicatore 2</td><td>
    <UL>
        <li>0 - Nome in forma inversa.  Viene usato quando la prima parola del nome collettivo o congresso inizia con un'iniziale o nome di persona
        <li>1 - Nome di localita' o di autorita'.  Viene usato per gli autori collettivi quando sono relativi ad agenzie di autorita' o governative
        <li>2 - Nome in forma diretta. Viene usato per tutti gli altri casi
    </UL>
</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li>a - Elemento principale. (Non ripetibile)
        <li>b - Suddivisione. (Ripetibile)
        <li>c - Qualificazione. (Ripetibile)
        <li>d - Numero del congresso. (Ripetibile)
        <li>e - Luogo del congresso. (Non ripetibile)
        <li>f - Data del congresso. (Non ripetibile)
        <li>g - Elemento permutato. (Non ripetibile)
        <li>3 - Numero di record nell'Authority File. (Non ripetibile)
        <li>4 - Codice del relatore. (Non ripetibile)
        <li>z - Autore in forma accettata, per tipo legame 4xx (Non ripetibile)
        <li>y - Autore in forma accettata, per tipo legame 5xx (Non ripetibile)
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
DataField * Marc4cppLegami::creaTag791_RinvioAutoreCollettivo(char tipoLegame, char *vidPadre) {
	DataField *df;

	df = new DataField();
	df->setTag((char *)"791");

	creaTag71x(df, 0, 0, 0);

	addAutoreFormaAccettata(df, tipoLegame, vidPadre);

	return df;
} // End creaTag791RinvioAutoreCollettivo


/*!
\brief <b>Tag 921 - Marca editoriale</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>921</td></tr>
<tr><td valign=top>Descrizione</td><td>Marca editoriale</td></tr>
<tr><td valign=top>Obbligatorieta'</td><td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td><td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td><td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> a - Numero MID. Non ripetibile
        <li> b - Descrizione della marca editoriale. Non ripetibile
        <li> c - Citazione della marca editoriale. Ripetibile
        <li> d - Nota della marca editoriale. Non ripetibile
        <li> e - Motto. Non ripetibile
        <li> f - Parola chiave. Ripetibile
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/

DataField * Marc4cppLegami::creaTag921_Marca() {
	DataField *df;
	Subfield *sf;
	char *midPtr;
	CString s, s1;

	df = new DataField();
	df->setIndicator1(' ');
	df->setIndicator2(' ');
	df->setTag((char *)"921");

	midPtr = tbMarca->getField(tbMarca->mid);

//tbMarca->dumpRecord();

	if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
		sf = new Subfield('a', midPtr, 10);
		//sf->setData();
	else // TIPO_SCARICO_OPAC
	{
		int pos =3;
		CString id((char *)"IT\\ICCU\\", 8);
		if (isAnticoE(midPtr))
			pos = 4;
		id.AppendString(midPtr, pos); // Prendi il Polo
		id.AppendChar('\\');
		id.AppendString(midPtr+pos, 10-pos);	 // Prendi resto del bid
		sf = new Subfield('a', &id);
		//sf->setData();
	}
	df->addSubfield(sf);
	sf = new Subfield('b', tbMarca->getFieldString(tbMarca->ds_marca));
	df->addSubfield(sf);

	// Cerchiamo le citazione nel repertorio
	if (trRepMar->existsRecord(midPtr)) {
		// Cerchiamo le n citazioni per questo mid
		while (trRepMar->loadNextRecord(midPtr))
		{
			s.assign(trRepMar->getFieldString(trRepMar->id_repertorio));
			if (!tbRepertorio->loadRecord(s.data()))
			{
				 SignalAWarning(__FILE__, __LINE__, "Repertorio non trovato per marca %s, id_repertorio%s", midPtr, s.data());
			}
			else
			{
				trRepMar->getFieldString(trRepMar->progr_repertorio)->Strip(CString::leading, '0'); // 29/09/2010 Verificare se si possono omettere i leading 0 nello scarico
				s1.assign(tbRepertorio->getFieldString(tbRepertorio->cd_sig_repertorio));
				s1.AppendString(trRepMar->getFieldString(trRepMar->progr_repertorio));
				sf = new Subfield('c', &s1);
				//sf->setData();
			}
			df->addSubfield(sf);
		}
	}

	CString key(rootBid, 10);
	key.AppendString(midPtr, 10);
	if (trTitMarTb->loadRecord(key.data()))	// TODO Qui si pu� ottimizzare evitando la ricerca se mettiamo un flag di nota esite nel reticolo (.rel)
	{
		if (!trTitMarTb->getFieldString(trTitMarTb->nota_tit_mar)->IsEmpty()) {
			sf = new Subfield('d', trTitMarTb->getFieldString(trTitMarTb->nota_tit_mar));
			//sf->setData();
			df->addSubfield(sf);
		}

	}
	if (DATABASE_ID == DATABASE_INDICE)
	{
		if (!tbMarca->getFieldString(tbMarca->ds_motto)->IsEmpty())
		{
			sf = new Subfield('e', tbMarca->getFieldString(tbMarca->ds_motto));
			df->addSubfield(sf);
		}

		if (tbParola->existsRecord(midPtr))
			{
			while (tbParola->loadNextRecord(midPtr))
			{
				tbParola->getFieldString(tbParola->parola)->Strip(CString::trailing, ' ');
				sf = new Subfield('f', tbParola->getFieldString(tbParola->parola));
				df->addSubfield(sf);
			}
			}
	} // end if indice
	return df;
} // End creaTag921_Marca



/*
21/07/2014

Scaricare per i record facenti parte di una raccolta fattizia anche l’etichetta 560.


Definizione del campo

L’etichetta viene generata in presenza di una relazione a “raccolta fattizia”
(natura SBN = ‘R’; relazione 01R fa parte della raccolta).

Occorrenza

Opzionale. Ripetibile.

Indicatori

Indicatore 1: 1

Indicatore 2: spazio (non definito)

Sottocampi

$a Titolo della raccolta. Non ripetibile.

  Il testo viene recuperato dal titolo proprio della raccolta (area 1 fino a fine area ‘. –’ )

$e Al informazioni descrittive. Ripetibile

   Viene generato
   	   - un sottocampo per l’area della descrizione fisica a cui si antepone la label ‘Descr. Fisica: ’
   	   - e uno per l’area note a cui si antepone la label ‘Note: ’.

$5 Codice ISIL (IT- + codice anagrafico della biblioteca) della biblioteca che ha creato la raccolta seguito
   dalla eventuale collocazione della copia del documento cui si riferisce la 200. Non ripetibile.

   Il contenuto del sottocampo risulta quindi dall’accodamento, eseguito previa eliminazione degli spazi in
   testa e coda dei seguenti campi:
   	   - codice ISIL della biblioteca che possiede il documento seguito da un carattere a spazio o,
   	   	   in alternativa,
   	   	   "cd_polo_sezione"+"cd_biblioteca_sezione",  "cd_sez", "cd_loc", "spec_loc"
   	   	   (da recuperare da tbc_collocazione imponendo la condizione tbc_collocazione.key_loc= tbc_inventario.key_loc)
   	   	   e "seq_coll" (da recuperare da tbc_inventario), solo se in seq_coll è presente almeno un carattere diverso
   	   	   da spazio dopo "spec_loc" inserire il carattere “/”.

$9 codice identificativo della raccolta (BID del titolo di natura R: anche se non previsto da UNIMARC
   lo si introduce per consentire una più semplice navigazione in OPAC dalla raccolta ai documenti contenuti).

NOTA

Se l’estrazione interessa un sottoinsieme delle biblioteche del polo per la produzione delle etichette 560 debbono essere prese in considerazione solo le relazioni titolo-titolo(R) relative a raccolte inserite da una delle biblioteche del sottoinsieme (primi 6 chr del campo tb_titolo.ute_ins = ‘codice polo + codice biblioteca’).

*/
DataField * Marc4cppLegami::creaTag560_RaccoltaFattizia(char *bid_base) {
	DataField *df;
	Subfield *sf;

	df = new DataField();
	df->setIndicator1('1');
	df->setIndicator2(' ');
//
	df->setTag((char *)"560");
	// $a
	creaSubtag200Area1(df, false, true);


	// Elabora dati relativi all'ISBD
	// 200, 205,206, 207,208,210,215, 225, 300
	CString * strIndiceAree = tbTitolo->getFieldString(tbTitolo->indice_isbd);
	ATTValVector<CString *> areeVect;
	bool retb;

	if (strIndiceAree->GetLastChar() == ';')
		strIndiceAree->ExtractLastChar();

	retb = strIndiceAree->Split(areeVect, ';');

	int length, startOffset;
	char *areaStartPtr, *areaEndPtr;
	char *isbd = tbTitolo->getField(tbTitolo->isbd);


	while (areeVect.Length())
	{
		int lastAreaPos = atoi(areeVect.Last()->data()+4);
		int isbdLength = tbTitolo->getFieldString(tbTitolo->isbd)->Length();
		if (lastAreaPos > isbdLength)
		{
			areeVect.deleteAndRemoveLastEntry();
		}
		else
			break;
	}

	for (int i = 0; i < areeVect.Length(); i++) {

		if (areeVect.Entry(i)->IsEmpty())
			continue;;

		startOffset = atoi(areeVect.Entry(i)->data() + 4) - 1;
		areaStartPtr = (char *) isbd + startOffset;

		if ((i + 1) == areeVect.Length() || areeVect.Entry(i + 1)->IsEmpty()) {
			length = tbTitolo->getFieldString(tbTitolo->isbd)->Length() - startOffset; // Ultima area

		} else {
			length = atoi(areeVect.Entry(i + 1)->data() + 4) - atoi(areeVect.Entry(i)->data() + 4) - 4;  // Non ultima area - 4 finale per ". - "
		}
		if (length < 0)
		{
			SignalAWarning(	__FILE__, __LINE__,	"Lunnghezza area ISBD (%d) invalida per area %s, bid=%s", length, areeVect.Entry(i)->data(), tbTitolo->getField(tbTitolo->bid));
			continue;
		}

		areaEndPtr = areaStartPtr + length; // '. - ' o '. (('
        if (*areaEndPtr && *areaEndPtr != '.')
            areaEndPtr++; // problema ' (('

		*areaEndPtr = 0;

		if ((areeVect.Entry(i)->StartsWith("215-"))) // Area 5 - Area della descrizione fisica
		{
			// $e area descrizione fisica
			CString descFisica = (char *)"Descr. Fisica: ";
			descFisica.AppendString(areaStartPtr);
			sf = new Subfield('e', &descFisica);
			df->addSubfield(sf);
		}
		else if ((areeVect.Entry(i)->StartsWith("300-"))) // Area 7 - Area delle note
		{
			// $e area note
			CString note = (char *)"Note: ";
			note.AppendString(areaStartPtr);
			sf = new Subfield('e', &note);
			df->addSubfield(sf);
		}
	} // End for
	areeVect.DeleteAndClear();

	char *bid = tbTitolo->getField(tbTitolo->bid);

	// $5 Codice ISIL
	// --------------
	if (trTitBib->existsRecord(bid)) {
		CString key, s;
		char *trTitBib_cdBib;
		while (trTitBib->loadNextRecord(bid)) {
			// Codice ISIL
		    key = trTitBib->getField(trTitBib->cd_polo); // prendi il polo
		    key.AppendString(trTitBib->getFieldString(trTitBib->cd_biblioteca)); // prendi la biblioteca
		    trTitBib_cdBib = trTitBib->getField(trTitBib->cd_biblioteca);

		    if (tbfBiblioteca->loadRecord(key.data()))
		        {
		        s = tbfBiblioteca->getField(tbfBiblioteca->paese);
		        s.AppendChar('-');
		        s.AppendString(tbfBiblioteca->getFieldString(tbfBiblioteca->cd_ana_biblioteca));
		        }
		    else
		    {
		        s = "\?\?\?-??????";
		    }

			/*
			2014_09_11 (mail calogiuri/giangregorio)

			I dati per la $5 vanno recuperati accedendo alla tabella collocazione (tbc_collocazione)
			con il BID della raccolta e il codice della biblioteca cui si riferisce la raccolta,
			se esiste una collocazione va verificato che sia utilizzata per la collocazione di
			almeno un inventario del documento base (tr_tit_tit.bid_base)
			accedendo alla tabella inventari (tbc_inventario) per key_loc

			se per il documento base (quello che stiamo scaricando) esiste un inventario
			con key_loc= key_loc della raccolta allora va predisposta la $5 partendo dalla
			collocazione della raccolta e le stesse regole  utilizzate per l’analoga informazione nella $5 del TAG 317
			 */

		    // Dal bid cerco l'inventario per questa biblioteca (950inv.out.srt.bid.off)

			// Abbiamo inventari per questo titolo (BID)?
		    char *recordBib;
		    char *recordBid;
			if (*trTitBib_cdBib)
			{
			    if (tb950Inv->existsRecordNonUnique(bid_base)) {
					while (tb950Inv->loadNextRecord(bid_base)) // bid
					{
						// Cambio biblioteca?
						recordBib = tb950Inv->getField(tb950Inv->tbinv_cd_bib);

						if (*recordBib && !strcmp(recordBib, trTitBib_cdBib))  	// se biblioteca uguale
							 {
							// prendiamo la keyloc
							CString key_loc = tb950Inv->getField(tb950Inv->tbinv_key_loc);
							key_loc.leftPadding('0', 9);
//printf("\nkey_loc=%s", key_loc.data());


							// Con la key_loc cerchiamo la collocazione
							if (tb950Coll->loadRecordByKeyLoc(key_loc.data()))
							{
								// Same bid and same bilio?
								char *locBib = tb950Coll->getField(tb950Coll->cd_biblioteca_sezione);
								recordBid = tb950Coll->getField(tb950Coll->bid);

								if (!strcmp(recordBid, bid) && !strcmp(recordBib, locBib))
								{
									//printf("\nLOCALIZED RECORD with bid=%s recordbid=%s bib=%s key_loc=%s", bid, recordBid, locBib, key_loc.data());

									if (marc4cppDocumento->getPoloId() != POLO_RML) // 09/03/2010
										s.AppendChar(' ');

									s.AppendString(tb950Coll->getFieldString(tb950Coll->tbcol_cd_sez));
									s.Strip(s.trailing, ' ');
									if (marc4cppDocumento->getPoloId() != POLO_RML) // 09/03/2010
										s.AppendChar(' ');

									s.AppendString(tb950Coll->getFieldString(tb950Coll->tbcol_cd_loc));
									s.Strip(s.trailing, ' ');

									if (marc4cppDocumento->getPoloId() != POLO_RML) // 09/03/2010
										s.AppendChar(' ');
									s.AppendString(tb950Coll->getFieldString(tb950Coll->tbcol_spec_loc));
									s.Strip(s.trailing, ' ');

									if (!tb950Inv->getFieldString(tb950Inv->tbinv_seq_coll)->IsEmpty())
									{
										s.AppendChar('/');
										s.AppendString(tb950Inv->getFieldString(tb950Inv->tbinv_seq_coll));
									}

								}
								else
								{
									//printf("\nNOT LOCALIZED RECORD with bid=%s recordbid=%s bib=%s key_loc=%s", bid, recordBid, locBib, key_loc.data());
								}

							}

							break;
							}
						//ctr++;
					} // end while loadNextRecord
			    } // End if existsRecordNonUnique
			} // end if trTitBib_cdBib





		    // Se trovo l'inventario prendo il key_loc
		    // e lo cerco nelle collocazioni (950coll.out.srt.kloc.off.srt)


		    // Se lo trovo aggiungo info alla $5



			sf = new Subfield('5', &s);
			df->addSubfield(sf);


		} // end while tr_tit_bib
	} // end $5



	sf = new Subfield('9', bid, 10);
	df->addSubfield(sf);

	return df;
} // end creaTag560_RaccoltaFattizia


void Marc4cppLegami::creaField7xxVoceStrumento(DataField *df, CString *cdStrumentoMusicale) {

#ifdef VOCE_STRUMENTO
//	if (DATABASE_ID == DATABASE_INDICE) // 16/02/2015 solo indice per il momento
//	{
	// 06/03/2015 Attivazione export anche per polo
		Subfield *sf = new Subfield('4', cdStrumentoMusicale);
		sf->appendData((char *)"=");

		char *descStrumento = codiciOrgaKV->GetValueFromKey(cdStrumentoMusicale->data());
		if (!descStrumento)
		{
			sf->appendData((char *)"DESCRIZIONE NON TROVATA PER CODICE ");
			sf->appendData(cdStrumentoMusicale);
		}
		else
			sf->appendData(descStrumento);

		df->addSubfield(sf);
//	}
#endif
}



// In canna abbiamo il bid_coll
DataField * Marc4cppLegami::creaTag_TitoloDellOpera(const tree<std::string>& tr, bool responsabilitaM1o2)
{
	DataField *df = 0;

	// Costruiamo la chiave di ricerca del titolo/autore
	char *bid_coll = tbTitolo->getField(tbTitolo->bid);
	CString *bidCollSPtr = tbTitolo->getFieldString(tbTitolo->bid);

	bool retb = false;
	long position;
	char *entryPtr;
	CString entryFile;

    // Troviamo le relazioni agli autori
    // ---------------------------------
	if (trTitAutRel->getOffsetBufferTbPtr())
		retb = BinarySearch::search(
				trTitAutRel->getOffsetBufferTbPtr(),
				trTitAutRel->getElementsTb(),
				trTitAutRel->getKeyPlusOffsetPlusLfLength(),
				bid_coll,
				trTitAutRel->getKeyLength(), position, &entryPtr);
	else
	{
		retb = BinarySearch::search(
				trTitAutRel->getTbOffsetIn(),
				trTitAutRel->getElementsTb(),
				trTitAutRel->getKeyPlusOffsetPlusLfLength(),
				bid_coll,
				trTitAutRel->getKeyLength(), position, &entryFile);
		entryPtr = entryFile.data();
	}


	if (!retb && IS_TAG_TO_GENERATE(506))
		return creaTag506_TitoloDellOpera(); // tr, responsabilitaM1o2 Nessun legame ad autori
	else if (IS_TAG_TO_GENERATE(576))
		return creaTag576_TitoloDellOpera(bidCollSPtr); // bidCollSPtr, tr, responsabilitaM1o2 Legami ad autori
	return df;
}


DataField * Marc4cppLegami::creaTag506_TitoloDellOpera() { // const tree<std::string>& tr, bool responsabilitaM1o2
	DataField *df;
	Subfield * sf;
	df = new DataField();

	df->setTag((char *)"506");

	df->setIndicator1('1');
	df->setIndicator2(' ');

	CString * sPtr = tbTitolo->getFieldString(tbTitolo->isbd);

	int pos = sPtr->IndexSubString(". - "); // Fermati a fine area titolo
	if (pos != -1)
		sPtr->CropRightFrom(pos);

	int idx;	// 20/05/2014
    if ((idx = sPtr->IndexCharFrom('*', 0, CString::forward)) > 0) // abbiamo almeno un asterisco?
    {
    	sPtr->PrependString(NSB.data());
    	sPtr->Replace("*", NSE.data(), 1);
    }
    else
    {
    	if (sPtr->GetFirstChar() == '*')
    		sPtr->ExtractFirstChar();
    }

	sf = new Subfield('a', sPtr);
	df->addSubfield(sf);


	char *bid = tbTitolo->getField(tbTitolo->bid); // 28/04/10 // const

	if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
		sf = new Subfield('3', bid, 10);
	else // TIPO_SCARICO_OPAC
	{
		CString id((char *)"IT\\ICCU\\", 8);
		int pos =3;
		if (isAnticoE(bid) || isAutore(bid))
			pos = 4;

		id.AppendString(bid, pos); // Prendi il Polo

		id.AppendChar('\\');
		id.AppendString(bid+pos, 10-pos);	 // Prendi resto del bid

		sf = new Subfield('3', &id);
		df->addSubfield(sf);
	}

	return df;

} // End creaTag_TitoloDellOpera


/*
 * Mail Aste del 07/06/17
 *
     576: nome dell’autore per opera con autore all’interno della manifestazione
		Indicatori: 1 valore 0 (nel catalogo non è usato il concetto di accesso principale)
					2 valore 1 (titolo strutturato)
		Sottocampi:  embedded (vedi esempi nella documentazione UNIMARC 2012 del tag 576)

	Sottocampi:

		$1001 BID dell’opera
		$4 codice di relazione opera/autore
		7xx  con relativi sottocampi
		506 con relativi sottocampi

	Nota: in base al manuale Unimarc agg. 2012 il secondo indicatore in caso di tecnica embedded deve essere blank


 */

DataField * Marc4cppLegami::creaTag576_TitoloDellOpera(CString *bidCollSPtr) // , const tree<std::string>& tr, bool responsabilitaM1o2
{
	DataField *df = 0;
	Subfield *sf =0;

	df = new DataField((char *)"576", 3);

	df->setIndicator1('0');
	df->setIndicator2(' '); // 1

	CString s = (char *)"001 ";
	s.AppendString(bidCollSPtr);
    sf = new Subfield('1', &s);
    df->addSubfield(sf);

    creatTag7xxNidificato(df, bidCollSPtr);

	DataField *df506 = creaTag506_TitoloDellOpera(); // tr, responsabilitaM1o2
	if (df506)
	{
		df->embedDataField(df506);
		delete df506;
	}
	return df;
}


