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


#undef DEBUG_ARGE



//#include "C210.h"
extern void SignalAnError(const OrsChar *Module, OrsInt Line,
		const OrsChar * MsgFmt, ...);
extern void SignalAWarning(const OrsChar *Module, OrsInt Line,
		const OrsChar * MsgFmt, ...);

Marc4cppDocumento::Marc4cppDocumento(MarcRecord *marcRecord,
		TbTitolo *tbTitolo, TbImpronta * tbImpronta,
		TbNumeroStd *tbNumeroStandard, TbGrafica *tbGrafica,
		TbCartografia *tbCartografia, TbMusica *tbMusica,
		TbTitSet1 *tbTitSet1, TbTitSet2 *tbTitSet2,
		TbAudiovideo *tbAudiovideo,
		TbDiscoSonoro *tbDiscoSonoro,
		TrTitMar *trTitMar, TrTitBib *trTitBib,
		TbfBiblioteca *tbfBiblioteca,
		TbNota *tbNota,
		TbNota300 *tbNota300, TbNota321 *tbNota321, TrBidAltroid *trBidAltroId,
		TsLinkWeb *tsLinkWeb, char *polo, char *descPolo, char *bibliotecaRichiedenteScarico, char *tagsToGenerateBufferPtr,
		int tagsToGenerate) {
	this->tagsToGenerateBufferPtr = tagsToGenerateBufferPtr;
	this->tagsToGenerate = tagsToGenerate;

	this->marcRecord = marcRecord;
	this->tbTitolo = tbTitolo;
	this->tbImpronta = tbImpronta;
	this->tbNumeroStandard = tbNumeroStandard;

	this->tbGrafica = tbGrafica;
	this->tbCartografia = tbCartografia;
	this->tbMusica = tbMusica;

	this->tbTitset1 = tbTitSet1;
	this->tbTitset2 = tbTitSet2;
	this->tbAudiovideo = tbAudiovideo;
	this->tbDiscoSonoro = tbDiscoSonoro;

	this->trTitMarRel = trTitMar;
	this->trTitBib = trTitBib;
	this->tbfBiblioteca = tbfBiblioteca;
	//this->tbfBibliotecaKV = tbfBibliotecaKV;
	this->tbNota = tbNota;
	this->tbNota300 = tbNota300;
	this->tbNota321 = tbNota321,
	this->trBidAltroId = trBidAltroId;

	this->tsLinkWeb = tsLinkWeb;
	this->polo = polo;
	this->descPolo = descPolo;

	this->bibliotecaRichiedenteScarico = bibliotecaRichiedenteScarico;
	init(polo);
}

void Marc4cppDocumento::init(char *polo)
{

	if (!strcmp(polo, "AQ1"))  // Fix 14/05/10
		poloId = POLO_AQ1;
	else if (!strcmp(polo, "BA1"))
		poloId = POLO_BA1;
	else if (!strcmp(polo, "BIA"))
		poloId = POLO_BIA;
	else if (!strcmp(polo, "BVE"))
		poloId = POLO_BVE;
	else if (!strcmp(polo, "CAM"))
		poloId = POLO_CAM;
	else if (!strcmp(polo, "CFI"))
		poloId = POLO_CFI;
	else if (!strcmp(polo, "CSA"))
		poloId = POLO_CSA;
	else if (!strcmp(polo, "CSW"))
		poloId = POLO_CSW;
	else if (!strcmp(polo, "IEI"))
		poloId = POLO_IEI;
	else if (!strcmp(polo, "INDICE"))
		poloId = POLO_INDICE;
	else if (!strcmp(polo, "LIG"))
		poloId = POLO_LIG;
	else if (!strcmp(polo, "MIL"))
		poloId = POLO_MIL;
	else if (!strcmp(polo, "MO1"))
		poloId = POLO_MO1;
	else if (!strcmp(polo, "PA1"))
		poloId = POLO_PA1;
	else if (!strcmp(polo, "PAL"))
		poloId = POLO_PAL;
	else if (!strcmp(polo, "RML"))
		poloId = POLO_RML;
	else if (!strcmp(polo, "RMR"))
		poloId = POLO_RMR;
	else if (!strcmp(polo, "SBR"))
		poloId = POLO_SBR;
	else if (!strcmp(polo, "SBW"))
		poloId = POLO_SBW;
	else if (!strcmp(polo, "SNT"))
		poloId = POLO_SNT;
	else if (!strcmp(polo, "TER"))
		poloId = POLO_TER;
	else if (!strcmp(polo, "TO0"))
		poloId = POLO_TO0;
	else if (!strcmp(polo, "UM1"))
		poloId = POLO_UM1;
	else if (!strcmp(polo, "UM2"))
		poloId = POLO_UM2;
	else if (!strcmp(polo, "PIS"))
		poloId = POLO_PIS;
	else if (!strcmp(polo, "TEL"))
		poloId = POLO_TEL;
	else if (!strcmp(polo, "DDS")) // 12/01/2016
		poloId = POLO_DDS;
	else if (!strcmp(polo, "UPG")) // 14/11/2016
		poloId = POLO_UPG;
	else
	{
		SignalAWarning(__FILE__, __LINE__, "ERRORE: ID mancante per polo '%s'",polo);

		poloId = POLO_SCONOSCIUTO;
	}
}



Marc4cppDocumento::~Marc4cppDocumento() {
	//	stop();
}
//void Marc4cppDocumento::~stop()
//{
//
//}


/*
 * Se ci sono le parentesi a' h (con numrei) o e (senza numeri)
 * senza parentesi a' d (con numeri) o a (senza numeri)
 * @param stringa
 * @return
 *
12/01/2016
void Marc4cppDocumento::estraiUnicoElemento210(CString &s, C210 * c210) {
	bool numero = contieneAnno(s);

	if (s.GetFirstChar() == '(' && s.GetLastChar() == ')') {
		if (numero)
			c210->addH210(new CString(s.data()));
		else
			c210->addE210(new CString(s.data()));
	} else {
		if (numero)
			c210->addD210(new CString(s.data()));
		else {
			C210ac * c210ac = new C210ac();
			c210ac->addA210(new CString(s.data()));
			c210->getAC210Vect()->Add(c210ac);
		}
	}
}
*/

bool Marc4cppDocumento::isAnnoBeforeSeparatore(CString &stringa) {
	int n = 2;
	//        CString coda = "";
	long from = stringa.Length() - 1;

	while (n < from) {
		//coda = stringa.substring(n);
		if (stringa.StartsWithFrom(" ; ", from) || stringa.StartsWithFrom(
				" : ", from) || stringa.StartsWithFrom(", ", from))
			break;
		from--;
	}
	CString anno = stringa.SubstringData(from);
	return contieneAnno(anno);
} // End isAnnoBeforeSeparatore

/*
 * Verifica se esiste una sequenza consecutiva di 4 caratteri numerici, il cui
 * valore sia > 1450.
 * Su richiesta di ICCU modificato: controlla che esista un numero di 2 cifre.
 * @return
 */
bool Marc4cppDocumento::contieneAnno(CString &stringa) {
	int numeri = 0;
	int x = 0;
	for (int i = 0; i < stringa.Length(); i++) {
		//MANTIS 2206 commentato queste righe di codice percha' non hanno senso
		//soprattutto la frase: "Se ho una X lo considero un numero romano"
		//percha' se trova una parola tipo "Xunta" (BID PUV0572091) va in tilt!!!
		//          if (stringa.charAt(i) == 'X') {
		//              //Se ho una X lo considero un numero romano
		//              return true;
		//            }
		if (isdigit(stringa.GetChar(i))) {
			numeri++;
			if (numeri == 2) {
				//MANTIS 1750 Aggiunta if x > -1. se non c'a' il trattino da errore
				x = i;
				x -= 2;
				if (x > -1) {
					//Per evitare che i numeri civici vengano considerati anni scriviamo
					//questa boiata.
					if (stringa.GetChar(x) == '-') {
						//Solo per il caso 'Il sole -24 ore'
						return false;
					}
					while (x >= 0) {
						char c = stringa.GetChar(x);
						if (isalnum(c)) {
							break;
						} else {
							x--;
						}
					}
					while (x >= 0) {
						char c = stringa.GetChar(x);
						if (isalnum(c)) {
							x--;
						} else {
							break;
						}
					}
					x++;
					char c = stringa.GetChar(x);
					if (isalnum(c)) {
						if (c == 'n' || c == 'N') {
							return false;
						}
					}
					//fine boiata
					return true;
				}
			}
		} else
			numeri = 0;
	}
	//Mantis 1750 - 1832
	//Se ho una stringa vuota (length = 0) allora ritorno false cioa' la
	//stringa non contiene alcun anno altrimenti faccio il controllo
	if (stringa.Length() != 0) {
		if (stringa.Length() == numeri) {
			return true;
		} else {
			return false;
		}
	} else
		return false;
} // End contieneAnno










bool Marc4cppDocumento::isMusica() {
	char tp_materiale = *(tbTitolo->getField(tbTitolo->tp_materiale));

	if (tp_materiale == TP_MATERIALE_MUSICA_U)
		return true;
	else
		return false;
}
















/*
 Tag+Def:		225 Collana
 Area ISBD della collezione. In SBN non viene riportata nell'ISBD e
 viene creato un titolo collegato. Inoltre in SBN se vi sono sottocollane possono essere
 creati pia' titoli ciascuno per un livello della collana.

 Le sottocollane riportano la descrizione della collana superiore seguita da '. ' e poi
 la descrizione della sottocollana.

 Se il titolo a' legato a pia' collane si considera che le collane facciano parte di una
 gerarchia di collane e si crea un solo campo 225 dove la descrizione della collana
 superiore viene riportata nel sottocampo $a e la descrizione delle collane inferiori in
 sottocampi $i. Si prendono in considerazione al massimo 3 collane.
 --------
 Obbligatorieta':	Facoltativo
 Ripetibilita':	Ripetibile. Ripetibile solo se la copia fa parte di pia' collane
 Indicatore1:	forma del titolo
 0 diverso dalla forma stabilita
 1 non esiste una forma stabilita
 2 uguale alla forma stabilita (indica che il campo a' identico al campo 410)
 Indicatore2:	Non definito

 Sottocampi:
 a - Titolo della collana. Se ci sono piu' collane a' il titolo della collana superiore. Non ripetibile.
 Area1 dell'ISBD della collana superiore fino a ' : ' o ' / ' o '. ' o ' = '.
 d - Titolo parallelo. Ripetibile.
 Parte successiva dell'area1 dell'ISBD da ' = ' fino a ' : ' o ' / ' o ' = '.
 e - Complementi del titolo. Ripetibile.
 Parte successiva dell'area1 dell'ISBD da ' : ' fino a ' : ' o ' / ' o '= ' o ' ='.
 f - Prima indicazione di responsabilita'. Ripetibile.
 Parte successiva dell'area1 dell'ISBD da ' / ' fino a ' : ' o ' / ' o '= ' o ' = ' o ' ; '.
 g - Altra indicazione di responsabilita'. Ripetibile.
 Parte successiva dell'area1 dell'ISBD da ' ; ' fino a ' : ' o ' / ' o ' = '.
 h - Numerazione della sottocollana nella collana superiore. Ripetibile.
 Per la prima sottocollana si scarta la parte dell'area1 dell'ISBD che precede '. '.
 Per la seconda sottocollana si scarta la parte dell'area1 dell'ISBD che precede '. ' ed anche quella che precede il secondo '. '.
 Parte successiva dell'area1 dell'ISBD della sottocollana da ', ' fino a ' : ' o ' / ' o ' = '.
 i - Titolo della sottocollana. Ripetibile.
 Per la prima sottocollana si scarta la parte dell'area1 dell'ISBD che precede '. '.
 Per la seconda sottocollana si scarta la parte dell'area1 dell'ISBD che precede '. ' ed
 anche quella che precede il secondo '. '.
 Parte successiva dell'area1 dell'ISBD della sottocollana da '. ' fino a ' : ' o ' / ' o ' = '.
 v - Indicazione di volume (Posizione in sequenza). Ripetibile.
 x - ISSN della collana (NON USATI)
 z - Lingua del titolo parallelo (NON USATI)

 NOTA BENE
 Se si trova il delimitatore '= ' si considera la parte dell'ISBD successiva fino a ' : ' o ' / ' o
 ' = ' come un altro sottocampo dello stesso tipo dell'ultimo sottocampo appena creato.
 I delimitatori non vengono riportati nei sottocampi.
 */
//DataField * Marc4cppDocumento::creaTag225(char *areaStartPtr, char *areaEndPtr) {
//	DataField *df;
//	Subfield *sf;
//
//	df = new DataField();
//	df->setTag("225");
//
//	sf = new Subfield('a');
//	sf->setData("!!!!!Collana da fare!!!!");
//	df->addSubfield(sf);
//
//	//	marcRecord->addDataField(df);
//	return df;
//} // End Marc4cppDocumento::creaTag225





bool Marc4cppDocumento::elaboraAltriDatiDocumento() {
	DataField *df;

	if (IS_TAG_TO_GENERATE(801))
		if (df = creaTag801_FonteDiProvenienza())
			marcRecord->addDataField(df);

	if (IS_TAG_TO_GENERATE(850))
		if (df = creaTag850_IstitutoDetentore())
			marcRecord->addDataField(df);
	return true;
} // End Marc4cppDocumento::elabora|AltriDatiDocumento



//bool Marc4cppDocumento::IS_TAG_TO_GENERATE(const char *nomeTag) {
//	char *entryPtr;
//	long position;
//	bool retb;
//
//	retb = BinarySearch::search(tagsToGenerateBufferPtr, tagsToGenerate, 3,
//			nomeTag, 3, position, &entryPtr);
//	return retb;
//}
/**
bool Marc4cppDocumento::IS_TAG_TO_GENERATE(int tag) {
	return *(tagsToGenerateBufferPtr + tag);
}
**/



char *Marc4cppDocumento::getDescPolo()
{
	return descPolo;
}

char *Marc4cppDocumento::getPolo()
{
	return polo.data();
}

CString *Marc4cppDocumento::getPoloString()
{
	return &polo;
}

int Marc4cppDocumento::getPoloId()
{
	return poloId;
}

CString *Marc4cppDocumento::getBibliotecaRichiedenteScarico()
{
	return &bibliotecaRichiedenteScarico;
}





















void Marc4cppDocumento::elaboraDatiTipoNumeroStandard() {
	const char *bid = tbTitolo->getField(tbTitolo->bid);
	//const char natura = *tbTitolo->getField(tbTitolo->cd_natura);
	//	bool retb;
	DataField *df;
	char tipoNumeroStd;

//		if (tbNumeroStandard->existsRecordNonUnique(bid)) {
//		while (tbNumeroStandard->loadNextRecord(bid)) // bid

// 06/09/2010 Raillineata gestione polo
//	if (tbNumeroStandard->existsRecordNonUniqueDaIndice(bid)) {
//		while (tbNumeroStandard->loadNextRecordDaIndice(bid)) // bid


	if (DATABASE_ID != DATABASE_INDICE && IS_TAG_TO_GENERATE(35)) // Solo per POLI
	{
		if (trBidAltroId->existsRecordNonUnique(bid))
			while (trBidAltroId->loadNextRecord(bid))
				df = creaTag035(); // Riattivato per OCLC Numbers
	}


	if (tbNumeroStandard->existsRecordNonUnique(bid)) {
			while (tbNumeroStandard->loadNextRecord(bid)) // bid

		{
			tipoNumeroStd = *(tbNumeroStandard->getField(tbNumeroStandard->tp_numero_std));
			df = 0;

			if (tipoNumeroStd == TP_NUMERO_STANDARD_I_ISBN
//#ifndef EVOLUTIVA_2014
//				|| tipoNumeroStd == TP_NUMERO_STANDARD_K_ISBN_978
//				|| tipoNumeroStd == TP_NUMERO_STANDARD_N_ISBN_979
//#endif
				)
				{
				if (IS_TAG_TO_GENERATE(10))
					df = creaTag010_Isbn(tipoNumeroStd); // Codice ISBN
				}
//			else if (tipoNumeroStd == TP_NUMERO_STANDARD_J_ISSN || TP_NUMERO_STANDARD_Z_ISSN_L)
			else if (tipoNumeroStd == TP_NUMERO_STANDARD_J_ISSN || tipoNumeroStd == TP_NUMERO_STANDARD_Z_ISSN_L) // 26/11/14 Mantis 5668
			{
				if (IS_TAG_TO_GENERATE(11))
					df = creaTag011_Issn(); // Codice ISSN fix 15/03/2013
			}
			else if (tipoNumeroStd == TP_NUMERO_STANDARD_M_ISMN)
			{
				if (IS_TAG_TO_GENERATE(13))
					df = creaTag013_Ismn(); // International Standard Music Number (ISMN)
			}
			else if (tipoNumeroStd == TP_NUMERO_STANDARD_B_BNI)
			{
				if (IS_TAG_TO_GENERATE(20))
					df = creaTag020_NumeroDiBibliografiaNazionale(); //  020 Numero di bibliografia nazionale (BNI)
			}
			else if (tipoNumeroStd == TP_NUMERO_STANDARD_G_N_PUBBLICAZIONE_GOVERNATIVA)
			{
				if (IS_TAG_TO_GENERATE(22))
					df = creaTag022_NumeroDiPubblicazioneGovernativa();
			}
			else if (tipoNumeroStd == TP_NUMERO_STANDARD_L_NUMERO_DI_LASTRA
					|| tipoNumeroStd == TP_NUMERO_STANDARD_E_NUMERO_EDITORIALE
//#ifdef EVOLUTIVA_2014
					|| tipoNumeroStd == TP_NUMERO_STANDARD_A_NUMERO_EDIZIONE_REGISTRAZIONI_SONORE
					|| tipoNumeroStd == TP_NUMERO_STANDARD_F_NUMERO_MATRICE_REGISTRAZIONI_SONORE
					|| tipoNumeroStd == TP_NUMERO_STANDARD_H_NUMERO_VIDEOREGISTRAZIONE
					|| tipoNumeroStd == TP_NUMERO_STANDARD_O_NUMERO_DI_RISORSA_ELETTRONICA
//#endif
					)
			{
				if (IS_TAG_TO_GENERATE(71))
					df = creaTag071_NumeroEditoreNonStandard(tipoNumeroStd);
			}
			else if (tipoNumeroStd == TP_NUMERO_STANDARD_U_CUBI && DATABASE_ID != DATABASE_INDICE)
			{
				if (IS_TAG_TO_GENERATE(90))
					df = creaTag090_NumeroDiBibliografiaCubi();
			}
//#ifdef EVOLUTIVA_2014
			else if (tipoNumeroStd == TP_NUMERO_STANDARD_Q_UPC)
			{
				if (IS_TAG_TO_GENERATE(72))
					df = creaTag072_Upc();
			}
			else if (tipoNumeroStd == TP_NUMERO_STANDARD_T_EAN)
			{
				if (IS_TAG_TO_GENERATE(73))
					df = creaTag073_Ean();
			}
			else if (tipoNumeroStd == TP_NUMERO_STANDARD_V_ISRC)
			{
				if (IS_TAG_TO_GENERATE(16))
					df = creaTag016_Isrc();
			}

//#endif
			else
			{
//				 SignalAWarning(__FILE__, __LINE__, "tipoNumeroStd '%c' da fare", tipoNumeroStd );
				// df = creaTag035(); Cassato 13/10/2009 15.19 Rossana

				if (IS_TAG_TO_GENERATE(17)) // per P, X, Y e W + altre se ci sono e non gestite
					df = creaTag017_OtherStandardIdentifier();
			}

		if (df)
			marcRecord->addDataField(df); // 21/11/2012 Memory leak se df non aggiunto in record

		} // end while
	}
} // end elaboraDatiTipoNumeroStandard






void Marc4cppDocumento::addSubfield(DataField *df, char subfieldId, CString &data)
{
	if (data.IsEmpty())
	{
		if (data.Length())
			data.Clear();
		return;
	}
	Subfield *sf = new Subfield(subfieldId, &data);
	//sf->setData();
	df->addSubfield(sf);
	data.Clear();

}



























































/**
 \brief <b>Tag 001 - Identificatico del record</b>
 <table>
 <tr>
 <th valign=top>Definizione</th><th>Descrizione</th></tr>
 <tr><td valign=top>Tag</td>
     <td>001</td></tr>
 <tr><td valign=top>Descrizione</td>
     <td>Identificatico del record</td></tr>
 <tr><td valign=top>Obbligatorieta'</td>
     <td>Obbligatorio</td></tr>
 <tr><td valign=top>Ripetibilita'</td>
     <td>Non Ripetibile</td></tr>
 <tr><td valign=top>Indicatore 1</td>
     <td>Nessuno</td></tr>
 <tr><td valign=top>Indicatore 2</td>
     <td>Nessuno</td></tr>
  <tr><td valign=top>Sottocampi</td>
     <td>Nessuno</td></tr>
  <tr><td valign=top>Formato</td>
     <td>
         \li  Unimarc - identificativo di 10 caratteri.<BR>
         \li  Opac - Moderno IT\\ICCU\\XXX\\YYYYYYY, Antico IT\\ICCU\\XXXX\\YYYYYY<BR>
     </td></tr>

  <tr><td valign=top>NOTE</td>
     <td>Nessuna restrizione sul formato dell'identificativo</td></tr>
 </table>
 */
ControlField * Marc4cppDocumento::creaTag001_IdRecord() {
	ControlField *cf;
	cf = new ControlField();
	cf->setTag((char *)"001");

//tbTitolo->dumpRecord();

	char *bid = tbTitolo->getField(tbTitolo->bid);
	if (TIPO_SCARICO == TIPO_SCARICO_UNIMARC)
		cf->setData(bid, 10);
	else // TIPO_SCARICO_OPAC
	{
		int pos =3;
		CString id((char *)"IT\\ICCU\\", 8);
		if (isAnticoE())
			pos = 4;
		id.AppendString(bid, pos); // Prendi il Polo
		id.AppendChar('\\');
		id.AppendString(bid+pos, 10-pos);	 // Prendi resto del bid
		cf->setData(&id);
	}

	marcRecord->addControlField(cf);
	return cf;
} // End creaTag001

/**
 \brief <b>Tag 003 - Permanent link</b>
 <table>
 <tr>
 <th valign=top>Definizione</th><th>Descrizione</th></tr>
 <tr><td valign=top>Tag</td>
     <td>001</td></tr>
 <tr><td valign=top>Descrizione</td>
     <td>Identificatico del record</td></tr>
 <tr><td valign=top>Obbligatorieta'</td>
     <td>Opzionale</td></tr>
 <tr><td valign=top>Ripetibilita'</td>
     <td>Non Ripetibile</td></tr>
 <tr><td valign=top>Indicatore 1</td>
     <td>Nessuno</td></tr>
 <tr><td valign=top>Indicatore 2</td>
     <td>Nessuno</td></tr>
  <tr><td valign=top>Sottocampi</td>
     <td>Nessuno</td></tr>
  <tr><td valign=top>Formato</td>
     <td>
         \li  URL
     </td></tr>

  <tr><td valign=top>NOTE</td>
     <td>Nessuna restrizione sul formato dell'identificativo</td></tr>
 </table>
 */

ControlField * Marc4cppDocumento::creaTag003_Permalink() { // 21/01/2016
	ControlField *cf;
	cf = new ControlField();
	cf->setTag((char *)"003");

//tbTitolo->dumpRecord();

	char *bid = tbTitolo->getField(tbTitolo->bid);

//	CString pl("http://10.10.10.19/permalink/Permalink?bid=");
//	CString pl("http://id.sbn.it/permalink/Permalink?bid=");
//	CString pl("http://id.sbn.it/permalink="); Richiesta Paolucci 26/01/16
	CString pl((char *)"http://id.sbn.it/bid/"); // Richiesta Paolucci 27/01/16

	pl.AppendString(bid);
	cf->setData(&pl);

	marcRecord->addControlField(cf);
	return cf;
} // End creaTag003_Permalink



/**
\brief <b>Tag 005 - Identificatore della versione</b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>005</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Identificatore della versione</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo (raccomandato)</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Non ripetibile</td></tr>
 <tr><td valign=top>Indicatore 1</td>
     <td>Nessuno</td></tr>
 <tr><td valign=top>Indicatore 2</td>
     <td>Nessuno</td></tr>
  <tr><td valign=top>Sottocampi</td>
     <td>Nessuno
     </td></tr>
  <tr><td valign=top>Formato</td>
     <td>
         \li  Polo - Data corrente dell'export.<BR>
         \li  Indice - Data di variazione in tb_titolo<BR>
     </td></tr>

 <tr><td valign=top>NOTE</td>
    <td>
    </td></tr>
</table>
 */
ControlField * Marc4cppDocumento::creaTag005_IdentificatoreVersione() {
	ControlField *cf;
	//	Subfield *sf;

	char dateBuf[8 + 6 + 2 + 1];
	*(dateBuf + 8 + 6 + 2) = 0;

	cf = new ControlField();
	cf->setTag((char *)"005");

	// Non buono pe rCFI
	// CMisc::formatDate2(tbTitolo->getField(tbTitolo->ts_ins), dateBuf);

	if (DATABASE_ID != DATABASE_INDICE) // 02/08/2010
	{
		// CFI vuole il time stamp dell'export (Verifica UNIMARC1)
		time_t rawtime;
		time(&rawtime);
		struct tm * timeinfo = localtime(&rawtime);
		CMisc::formatDate3(timeinfo, dateBuf);

		cf->setData(dateBuf, 16); // YYYYMMDDHHMMSS.T
	}
	else
	{ // INDICE
//		cf->setData(tbTitolo->getField(tbTitolo->ts_var)); // come da DB

//tbTitolo->dumpRecord();

		CMisc::formatDate2(tbTitolo->getField(tbTitolo->ts_var), dateBuf); // YYYYMMDDHHMMSS.T
		cf->setData(dateBuf, 16);
	}

	marcRecord->addControlField(cf);
	return cf;
} // End creaTag005


/**
\brief <b>Tag 010 - Codice ISBN </b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>010</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>ISBN (International Standard Book Number).</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    \li a - Numero ISBN corretto (in alternativa con sottocampo 'z'). Non ripetibile.
    \li z - Numero ISBN errato (in alternativa con sottocampo 'a'). Non ripetibile.
    \li b - Qualificazione (Nota sul numero ISBN). Non ripetibile.
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
    </td></tr>
</table>
 */
DataField * Marc4cppDocumento::creaTag010_Isbn(char tipoNumeroStd) {
	CString *notaPtr = tbNumeroStandard->getFieldString(tbNumeroStandard->nota_numero_std);
	CString numeroStandard;


//#ifndef EVOLUTIVA_2014
//	// Tipo numero standard K e N non esistono piu' con l'evolutiva 2014.
//	// ed i numeri in basedati sono stati prefissati per K e N
//	if (tipoNumeroStd == TP_NUMERO_STANDARD_K_ISBN_978)
//		numeroStandard.assign("978", 3);
//
//	else if (tipoNumeroStd == TP_NUMERO_STANDARD_N_ISBN_979)
//		numeroStandard.assign("979"), 3;
//
//	numeroStandard.AppendString(tbNumeroStandard->getFieldString(tbNumeroStandard->numero_std));
//#endif
	numeroStandard.AppendString(tbNumeroStandard->getFieldString(tbNumeroStandard->numero_std)); // 14/01/15 Rimesso. Era stato tolto per sbaglio

	if (ISBN_CON_TRATTINI) // Mantis 4361 12/04/2011
		CMisc::gestisciTrattiniNumStandardIsbn(&numeroStandard, &numeroStandard);

	// Se non esistono dati non creare l'etichetta
	if (notaPtr->IsEmpty() && numeroStandard.IsEmpty())
		return 0;

	DataField *df;
	Subfield *sf;
	df = new DataField();

	df->setTag((char *)"010");
	if (notaPtr->IndexSubStringCaseInsensitive("err") != -1) // 18/10/2010
		sf = new Subfield('z');
	else
		sf = new Subfield('a');

	numeroStandard.Strip(numeroStandard.trailing, ' ');
	sf->setData(&numeroStandard);

	if (sf->getCode() =='a')
		df->addSubfield(sf);
		if (!notaPtr->IsEmpty()) {
			Subfield *sf1;
			notaPtr->Strip(CString::trailing, ' ');
			sf1 = new Subfield('b', notaPtr);
			//sf1->setData();
			df->addSubfield(sf1);
		}

	if (sf->getCode() =='z')
		df->addSubfield(sf); // $z

	//	marcRecord->addDataField(df);
	// marcRecord->addDataField(df); // 19/11/2012
									//fatto dal chiamante 21/11/2012
	return df;
}// End creaTag010



/**
\brief <b>Tag 011 - Codice ISSN </b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>011</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>ISSN (International Standard Serial Number)</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    \li a - Numero ISSN corretto (in alternativa con sottocampo 'z'). Non ripetibile.
    \li z - Numero ISSN errato (in alternativa con sottocampo 'a'). Non ripetibile.
    \li b - Qualificazione (Nota sul numero ISBN). Non ripetibile.

    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
    </td></tr>
</table>

 */
DataField * Marc4cppDocumento::creaTag011_Issn() {
	DataField *df;
	Subfield *sf;

	CString *sPtrNota = tbNumeroStandard->getFieldString(tbNumeroStandard->nota_numero_std);
	CString *sPtrNumStd = tbNumeroStandard->getFieldString(tbNumeroStandard->numero_std);
	char tipoNumeroStd = *(tbNumeroStandard->getField(tbNumeroStandard->tp_numero_std));
	df = 0;


	if (sPtrNota->IsEmpty() && sPtrNumStd->IsEmpty())
		return 0;


	df = new DataField();
	df->setTag((char *)"011");

	if (sPtrNota->IndexSubStringCaseInsensitive("err") != -1) // 18/10/2010
		sf = new Subfield('z');
	else
	{
		if (tipoNumeroStd == TP_NUMERO_STANDARD_J_ISSN)
			sf = new Subfield('a');
		else if (TP_NUMERO_STANDARD_Z_ISSN_L)
			sf = new Subfield('f');	// 17/09/14
		else
			sf = new Subfield('a'); // Qui non si dovrebbe mail arrivare
	}
	sPtrNumStd->Strip(sPtrNumStd->trailing, ' ');

	if (sPtrNumStd->Length() >= 4)
		sPtrNumStd->InsertCharAt('-', 5);

	sf->setData(sPtrNumStd);
	df->addSubfield(sf);

	if (!sPtrNota->IsEmpty()) {
		sf = new Subfield('b', sPtrNota);
		df->addSubfield(sf);
	}
	//marcRecord->addDataField(df); // fatto dal chiamante 21/11/2012
	return df;

	//return true;
}// End creaTag011




/**

\brief <b>Tag 012 - Impronta (Libro antico)</b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>012</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Impronta (solo per il materiale antico).</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo (raccomandato)</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    \li a - Impronta completa. Non ripetibile.
    \li 2 - Codifica forzata a 'fei'. Non ripetibile.
    \li 9 - Nota. Non ripetibile.
    </td></tr>
 <tr><td valign=top>NOTE</td><td>Il tag 012 viene generato anche per record con tipo materiale 'U' (musica)</td></tr>

 */
void Marc4cppDocumento::creaTag012_Impronta() {
	DataField *df = 0;
	char *bid = tbTitolo->getField(tbTitolo->bid);

	if (tbImpronta->existsRecordNonUniqueDaIndice(bid)) { // 21/09/2010
		CString impronta;
		while (tbImpronta->loadNextRecordDaIndice(bid)) // bid
		{
			Subfield *sf;
			df = new DataField();
			df->setTag((char *)"012");
			impronta.assign(tbImpronta->getFieldString(tbImpronta->impronta_1));
			impronta.AppendString(tbImpronta->getFieldString(tbImpronta->impronta_2));
			impronta.AppendString(tbImpronta->getFieldString(tbImpronta->impronta_3));
			sf = new Subfield('a', &impronta);
			df->addSubfield(sf);
			sf = new Subfield('2', (char *)"fei", 3);
			df->addSubfield(sf);
			if (!tbImpronta->getFieldString(tbImpronta->nota_impronta)->IsEmpty())
			{
				sf = new Subfield('9', tbImpronta->getFieldString(tbImpronta->nota_impronta));
				df->addSubfield(sf);
			}
			marcRecord->addDataField(df);
		} // End while
	}
}// End creaTag012


/**
\brief <b>Tag 013 - Codice ISMN </b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>013</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>ISMN (International Standard Music Number)</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    \li a - Numero ISMN corretto (in alternativa con sottocampo 'z'). Non ripetibile.
    \li z - Numero ISMN errato (in alternativa con sottocampo 'a'). Non ripetibile.
    \li b - Qualificazione (Nota sul numero ISMN). Non ripetibile.
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
    </td></tr>
</table>
*/
DataField * Marc4cppDocumento::creaTag013_Ismn() {
	DataField *df;
	Subfield *sf;
	//char *ptr;
	df = new DataField();
	df->setTag((char *)"013");

	if (tbNumeroStandard->getFieldLength(tbNumeroStandard->numero_std)) {
		CString *notaPtr = tbNumeroStandard->getFieldString(tbNumeroStandard->nota_numero_std);

//		notaPtr->AppendString("Prova di errore");

		if (notaPtr->IndexSubStringCaseInsensitive("err") != -1) // 20/02/2015 (Mail Mataloni)
			sf = new Subfield('z');
		else
			sf = new Subfield('a');


// 21/01/2015 979 gia' gestito con in nuovo schema (2.x)
//		if (*tbNumeroStandard->getField(tbNumeroStandard->numero_std) != 'M')
//		{
//			CString s = "979"; // Numero ISMN forzato a 13 cifre
//			s.AppendString(tbNumeroStandard->getFieldString(tbNumeroStandard->numero_std));
//			sf->setData(&s);
//		}
//		else

		sf->setData(tbNumeroStandard->getFieldString(tbNumeroStandard->numero_std));
		df->addSubfield(sf);

//		if (!tbNumeroStandard->getFieldString(tbNumeroStandard->nota_numero_std)->IsEmpty()) {
		if (!notaPtr->IsEmpty()) {
			sf = new Subfield('b', tbNumeroStandard->getFieldString(tbNumeroStandard->nota_numero_std));
			df->addSubfield(sf);
		}


	} else {
		sf = new Subfield('z');
		df->addSubfield(sf);
	}
	//	marcRecord->addDataField(df);
	return df;
}// End creaTag013Ismn

/**
\brief <b>Tag 015 - Codice ISRN </b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>015</td></tr>
<tr><td valign=top>Descrizione</td>
    <td> ISRN (International Standard Technical Report Number)</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Optional.</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Repeatable.</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    \li a - Numero (ISRN)
    \li b - Qualificazione
    \li d - Disponibilita' e/o prezzo
    \li z - Cancellato/Invalido/Errato ISRN
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>CASSATO</td></tr>
</table>

 */
DataField * Marc4cppDocumento::creaTag015_Isrn() {
	DataField *df = 0;
	//	Subfield *sf;
	//	marcRecord->addDataField(df);
	return df;
}// End creaTag015_ISRN


/**
\brief <b>Tag 017 - Other standard identifier</b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>017</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Altri numeri standard.</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Opzionale</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Ripetibile </td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Tipo numero standard
				\li 7 Fonte specificata in sottocampo $2
				\li 8 Non specificato</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Indicatore di differenza.
				\li 0 Non specificato
				\li 1 Nessuna differenza
				\li 2 Differenza</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    \li a - Numero standard (in alternativa al $z). Non Ripetibile.
    \li z - Numero standard errato (in alternativa al $a). Non Ripetibile.
    \li b - Qualificazione. Non Ripetibile.
    \li 2 - Tipo numero standard. Non Ripetibile.
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
    </td></tr>
</table>

 */

DataField * Marc4cppDocumento::creaTag017_OtherStandardIdentifier() {
	DataField *df;
	Subfield *sf;
	df = new DataField((char *)"017", '7', '0'); // 21/09/2010 Roveri IEI0151247

	CString numeroStandard; numeroStandard = tbNumeroStandard->getField(tbNumeroStandard->numero_std);
	CString nota; nota = tbNumeroStandard->getField(	tbNumeroStandard->nota_numero_std);

	numeroStandard.Strip(numeroStandard.trailing, ' ');

	sf = new Subfield('a', &numeroStandard);
	df->addSubfield(sf);
	if (!nota.IsEmpty()) {
		sf = new Subfield('b', &nota);
		//sf->setData();
		df->addSubfield(sf);
	}
	sf = new Subfield('2', tbNumeroStandard->getFieldString(tbNumeroStandard->tp_numero_std));
	df->addSubfield(sf);

	return df;
}// End creaTag017_OtherStandardIdentifier



/**
\brief <b>Tag 020 - Numero di Bibliografia Nazionale (BNI) </b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>020</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Numero di Bibliografia Nazionale Italiana del titolo.</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    \li a - Codice paese. Non ripetibile
    \li b - Numero di bibliografia nazionale italiana. Non ripetibile
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
    </td></tr>
</table>

 */
//\li z - Numero di bibliografia nazionale italiana errato. Il sottocampo viene creato se i primi 10 caratteri della nota sono diversi da spazio, utilizzando tali 10 caratteri.

DataField * Marc4cppDocumento::creaTag020_NumeroDiBibliografiaNazionale() {
	DataField *df;
	Subfield *sf;
	df = new DataField();
	df->setTag((char *)"020");
	//	df->setIndicator1(INDICATOR_UNDEFINED);
	//	df->setIndicator2(INDICATOR_UNDEFINED);

	sf = new Subfield('a', (char *)"IT", 2);
	//sf->setData(); // tbNumeroStandard->getField(tbNumeroStandard->cd_paese)
	df->addSubfield(sf);

//	const char *nota = tbNumeroStandard->getField(tbNumeroStandard->nota_numero_std);
//	if (nota && strncmp(nota, "          ", 10))
		sf = new Subfield('b', tbNumeroStandard->getFieldString(tbNumeroStandard->numero_std));
//	else
//		sf = new Subfield('z');

	//	sf = new Subfield('b');
	//sf->setData();
	df->addSubfield(sf);
	//	marcRecord->addDataField(df);
	return df;

}// End creaTag020


/**
\brief <b>Tag 022 - Numero di pubblicazione governativa </b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>022</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Numero di pubblicazione governativa</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    \li a - Codice paese. Non ripetibile.
    \li b - Numero. Non ripetibile
    \li z - Numero errato. Non ripetibile.
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
    </td></tr>
</table>

 */
DataField * Marc4cppDocumento::creaTag022_NumeroDiPubblicazioneGovernativa() {
	DataField *df;
	Subfield *sf;
	df = new DataField((char *)"022", 3);

//	df->setTag();

	if (tbNumeroStandard->getFieldString(tbNumeroStandard->nota_numero_std)->IndexSubStringCaseInsensitive("err") != -1) { // 04/10/2010 Roveri
		sf = new Subfield('b', tbNumeroStandard->getFieldString(tbNumeroStandard->nota_numero_std));
		df->addSubfield(sf);
		sf = new Subfield('z', tbNumeroStandard->getFieldString(tbNumeroStandard->numero_std));
		df->addSubfield(sf);
	}
	else
	{
		if (tbNumeroStandard->getFieldLength(tbNumeroStandard->cd_paese)) {
			sf = new Subfield('a', tbNumeroStandard->getFieldString(tbNumeroStandard->cd_paese));
			df->addSubfield(sf);
		}

		sf = new Subfield('b', tbNumeroStandard->getFieldString(tbNumeroStandard->numero_std));
		df->addSubfield(sf);
	}
	return df;
}// End creaTag022



/**
\brief <b>Tag 071 - Numero di lastra (Musica) </b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>071</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Numero di lastra (Musica)</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Tipo di numero dell'editore.<BR>
	\li 0 Registrazione sonora: Numero di emissione.
	\li 1 Registrazione sonora: numero di matrice
	\li 2 Musica a stampa: numero di lastra
	\li 3 Musica a stampa: altro numero di pubblicazione
	\li 4 Numero di registrazione video
	\li 5 Altro tipo di numero dell-editore
	\li 6 Numero di risorsa elettronica (e.g. CD-ROM)
<tr><td valign=top>Indicatore 2</td>
    <td>Indicatore della nota.<BR>
	 \li 0 non stampare la nota
	 \li 1 stampare la nota
 <tr><td valign=top>Sottocampi</td>
    <td>
    \li a - Numero standard. Non ripetibile.
    \li c - Qualificazione. Non ripetibile.
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
    </td></tr>
</table>
 */
//\li b - Source
//\li d - Terms of availability and/or price
//\li z - Erroneous publisher's number


DataField * Marc4cppDocumento::creaTag071_NumeroEditoreNonStandard(char tipoNumeroStd) {
	DataField *df;
	Subfield *sf;

	df = new DataField((char *)"071", 3);
	// 15/01/2015 mail Roveri.
	//- per tutti i tag 071, il secondo indicatore (almeno nello scarico per l'opac SBN)
	// dovrebbe essere posto a '1', dal momento che il contenuto dei sottocampi viene prospettato direttamente come 'nota'.
	df->setIndicator2('1');

	if (tipoNumeroStd == TP_NUMERO_STANDARD_L_NUMERO_DI_LASTRA) {
		df->setIndicator1('2');
//		df->setIndicator2('0'); // 20/0910 comne da indice
	} else if (tipoNumeroStd == TP_NUMERO_STANDARD_E_NUMERO_EDITORIALE) {
//		df->setIndicator1('4');
		df->setIndicator1('3'); // 03/09/2014 (non capisco perche' ci fosse il 4)
//		df->setIndicator2('0');
	}
//#ifdef EVOLUTIVA_2014
	else if (tipoNumeroStd == TP_NUMERO_STANDARD_A_NUMERO_EDIZIONE_REGISTRAZIONI_SONORE) {
		df->setIndicator1('0');
//		df->setIndicator2('0');
	}
	else if (tipoNumeroStd == TP_NUMERO_STANDARD_F_NUMERO_MATRICE_REGISTRAZIONI_SONORE) {
		df->setIndicator1('1');
//		df->setIndicator2('0');
	}
	else if (tipoNumeroStd == TP_NUMERO_STANDARD_H_NUMERO_VIDEOREGISTRAZIONE) {
		df->setIndicator1('4');
//		df->setIndicator2('0');
	}
	else if (tipoNumeroStd == TP_NUMERO_STANDARD_O_NUMERO_DI_RISORSA_ELETTRONICA) {
		df->setIndicator1('6');
//		df->setIndicator2('0');
	}
//#endif

	//	sf = new Subfield('a', tbNumeroStandard->getFieldString(tbNumeroStandard->numero_std));
	CString *notaPtr =  tbNumeroStandard->getFieldString(tbNumeroStandard->nota_numero_std); // 15/01/2015 mail Roveri
	if (notaPtr->IndexSubStringCaseInsensitive("err") != -1)
		sf = new Subfield('z');
	else
		sf = new Subfield('a');
	sf->setData(tbNumeroStandard->getFieldString(tbNumeroStandard->numero_std));

	df->addSubfield(sf);

	if (!tbNumeroStandard->getFieldString(tbNumeroStandard->nota_numero_std)->IsEmpty())
	{
		tbNumeroStandard->getFieldString(tbNumeroStandard->nota_numero_std)->Strip(CString::trailing);
		sf = new Subfield('c', tbNumeroStandard->getFieldString(tbNumeroStandard->nota_numero_std));
		df->addSubfield(sf);
	}
	return df;
}// End creaTag071


/**
\brief <b>Tag 090 - Numero di Bibliografia Cubi </b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>090</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Numero di Bibliografia Cubi</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    \li a - Numero standard. Non ripetibile.
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
    </td></tr>
</table>
*/
DataField * Marc4cppDocumento::creaTag090_NumeroDiBibliografiaCubi() {
	DataField *df;
	Subfield *sf;
	df = new DataField((char *)"090", 3);

	sf = new Subfield('a', tbNumeroStandard->getFieldString(tbNumeroStandard->numero_std));
	df->addSubfield(sf);
	return df;
}// End creaTag090_NumeroDiBibliografiaCubi



/**
\brief <b>Tag 100 - Carta da'Identita'</b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>100</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Dati generali di elaborazione.</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Obbligatorio</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Non Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
<UL>
	<li> a - Dati per l'elaborazione
		<ul>
		<li> 0-7 data di registrazione
		<li>  8 tipo di data di pubblicazione
			<ul>
			 <li> a -periodico pubblicato correntemente
			 <li> b -periodico la cui pubblicazione a' cessata
			 <li> c -periodico di cui non si conosce lo "stato" della pubbl.
			 <li> d -monografia
			 <li> e -riproduzione di un documento
			 <li> f -monografia con data incerta
			 <li> g -monografia la cui pubbl. continua per pia' di un anno
			 <li> h -monografia con data attuale e di copyright
			 <li> i -monografia con data di uscita e di produzione
			 <li> j -documento con data di pubblicazione dettagliata
			 <li> ' ' - spazio
			</ul>
	<li>	9-12	prima data di pubbl.<br>
			 se tipo_data = spazio allora valore = spazio, altrimenti valore = anno dalla data1
	<li>	13-16	seconda data di pubbl.<br>
			 se anno_data2 > 0 allora valore = anno_data2
			 altrimenti se tipo_data = 'a' o 'F' o 'G' allora valore = 9999
			 altrimenti valore = spazi.
	<li>	17-19 destinatario della pubblicazione (NON USATI, carattere di riempimento)
			<ul>
			 <li> a -giovani
			 <li> b -in eta' pre-scolare, 0-5 anni
			 <li> c -in eta' scolare, 5-10 anni
			 <li> d -bambini dai 9 ai 14 anni
			 <li> e -giovani, 14-20 anni
			 <li> k -adulti, serio
			 <li> m -adulti, generale
			 <li> u -sconosciuto
			</ul>
	<li>	20 pubblicazione governativa (NON USATO, carattere di riempimento)
			<ul>
			 <li> a -statale
			 <li> b -enti locali
			 <li> c -dipartimento
			 <li> d -municipio ecc.
			 <li> e -dipartimenti intergovernativi
			 <li> f -intergovernativa
			 <li> g -governi in esilio o clandestini
			 <li> h -livello non determinato
			 <li> u -sconosciuto
			 <li> y -pubblicazione non governativa
			 <li> z -altri livelli
			</ul>
	<li>	21 record modificato
			<ul>
			 <li> 0 -record non modificato
			 <li> 1 -record modificato
			</ul>
	<li>	22-24 lingua della catalogazione
	<li>	25 codice di traslitterazione (carattere di riempimento)
			<ul>
			 <li> a -traslitterazione secondo le norme ISO
			 <li> b -altro
			 <li> c -traslitt. multipla; ISO o altri schemi
			 <li> y -non traslitterazione
			</ul>
	<li>	26-29 serie di caratteri
			<ul>
			 <li> 01 -ISO 646 (Versione IRV) (Basic Latin ISO 646)*
			 <li> 02 -ISO Registrazione 37 (Cirillico di base)
			 <li> 03 -ISO 5426 (Latino esteso)*
			 <li> 04 -ISO DIS 5427 (Cirillico esteso)
			 <li> 05 -ISO 5428 (Greco)
			 <li> 06 -ISO 6438 (Caratteri africani)
			</ul>
	<li>	30-31 - primo set addizionale<br> se e' utilizzato il set ISO/DIS 6937 allora valore = '10' altrimenti valore = spazi.
	<li>	32-33 - secondo set addizionale<br> se e' utilizzato Greek ISO 5428 allora valore = '05' altrimenti valore = spazi.
	<li>	34-35 alfabeto del titolo
			<ul>
			 <li> ba -Latino *
			 <li> ca -Cirillico
			 <li> da -Giapponese - alfabeto non specificato
			 <li> db -Giapponese - kanji
			 <li> dc -Giapponese - kana
			 <li> ea -Cinese
			 <li> fa a'Arabo
			 <li> ga -Greco
			 <li> ha -Ebraico
			 <li> ia -Thai
			 <li> ja -Devanagari
			 <li> ka -Coreano
			 <li> la -Tamil
			 <li> zz -Altri
			</ul>
		</UL>
    </td></tr>

 <tr><td valign=top>Formato</td>
    <td>Se tp_aa_pubb == 'a' allora seconda data di pubbl diventa
    \li Polo - '9999'
    \li Indice - '&nbsp;&nbsp;&nbsp;&nbsp;'
    </td>
    </tr>



 <tr><td valign=top>NOTE</td>
    <td>le posizioni 31-32 33-34 vengono impostate inizialmente a spazi e poi vengono corrette alla fine dell'elaborazione di ogni record unimarc, secondo il set di caratteri riscontrato nel record.</td>
    </tr>
</table>

 */

DataField * Marc4cppDocumento::creaTag100_CartaDIdentita() {
	CString s;
	DataField *df;
	Subfield *sf;
	df = new DataField((char *)"100", 3);
	char dateBuf[8 + 1];
	*(dateBuf + 8) = 0;
	char tp_aa_pubb;

	CMisc::formatDate1(tbTitolo->getField(tbTitolo->ts_ins), dateBuf);
	s.AppendString(dateBuf, 8);
	tp_aa_pubb = *tbTitolo->getField(tbTitolo->tp_aa_pubb);
	if (tp_aa_pubb < 'a')
		tp_aa_pubb += 0x20; // make lower case
	s.AppendChar(tp_aa_pubb);

	CString *sPtr =  tbTitolo->getFieldString(tbTitolo->aa_pubb_1);
	if (	tp_aa_pubb == ' ' ||
			sPtr->IsEmpty() ||
			sPtr->GetFirstChar() == ' ' || //  *(tbTitolo->getField(tbTitolo->aa_pubb_2))
			sPtr->GetFirstChar() == '0' // *(tbTitolo->getField(tbTitolo->aa_pubb_2))
			)
		s.AppendString((char *)"    ", 4);
	else
		s.AppendString(tbTitolo->getFieldString(tbTitolo->aa_pubb_1));
	sPtr =  tbTitolo->getFieldString(tbTitolo->aa_pubb_2);
	if (tp_aa_pubb == 'a')
		{
		if (DATABASE_ID != DATABASE_INDICE)
			s.AppendString((char *)"9999", 4); // richiesta CFI
		else
			s.AppendString((char *)"    ", 4); // 24/09/2010 Indice AQ10003128
		}
	else if (sPtr->IsEmpty() ||
			sPtr->GetFirstChar() == ' ' || //  *(tbTitolo->getField(tbTitolo->aa_pubb_2))
			sPtr->GetFirstChar() == '0' // *(tbTitolo->getField(tbTitolo->aa_pubb_2))
			// sPtr->isEqual("null")
				)
		s.AppendString((char *)"    ", 4);
	else
		s.AppendString(tbTitolo->getFieldString(tbTitolo->aa_pubb_2));
	s.AppendString((char *)"||||", 4); // 17-19,20 non usati filler
	s.AppendChar('0'); // 21 // 07/09/2010 11.43 come da indice
	s.AppendString((char *)"itac50      ba", 14); // 06/09/2010 15.48
	sf = new Subfield('a', &s);
	df->addSubfield(sf);
	marcRecord->addDataField(df);
	return df;
}// End creaTag100


/**
\brief <b>Tag 101 - Lingua della pubblicazione </b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>101</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Lingua della pubblicazione</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Non Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>\li 0 Lingua originale
	 \li 1 Traduzione dalla'originale o in lavoro intermedio
	 \li 2 Contiene traduzioni other than translated summaries</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    \li a - Lingua del testo. Ripetibile.
	\li b - Lingua del testo intermedio. Ripetibile.
	\li c - Lingua del testo originale. Ripetibile.
		</td></tr>
 <tr><td valign=top>NOTE</td><td>Sottocampo 'b' e 'c' per l'export di Indice confluiscono nel sottocampo 'a'</td></tr>
</table>

 */
//\li d - Lingua del compendio
//\li e - Lingua degli indici
//\li f - Lingua del frontespizio
//\li g - Lingua del titolo proprio
//\li h - Lingua del libretto, ecc.
//\li i - Lingua del materiale allegato
//\li j - Lingua dei sottotitoli




DataField * Marc4cppDocumento::creaTag101_LinguaPubblicazione() {
	DataField *df = 0;
	CString *sPtr;

	sPtr = tbTitolo->getFieldString(tbTitolo->cd_lingua_1);
	if (sPtr->IsEmpty())
		return df;
	sPtr->ToLower();
	if (DATABASE_ID != DATABASE_INDICE && (sPtr->isEqual("ABS") || sPtr->isEqual("abs")) ) // abs segnalato da CFI. Per indice 23/09/2010
		return df;

	Subfield *sf;
	df = new DataField((char *)"101", INDICATOR_FILLER, INDICATOR_UNDEFINED);
	sf = new Subfield('a', sPtr);
	df->addSubfield(sf);

	sPtr = tbTitolo->getFieldString(tbTitolo->cd_lingua_2); // // Testo intermedio
	if (!sPtr->IsEmpty() ) { // &&	!cdLingua.isEqual("null")
//		if (DATABASE_ID == DATABASE_INDICE) // 03/08/2010
//			sf = new Subfield('a');
//		else
//			sf = new Subfield('b');


		sf = new Subfield('a'); // 06/06/2016 Sia per Polo che Per indice
		sPtr->ToLower();
		sf->setData(sPtr);
		df->addSubfield(sf);
	}
	sPtr = tbTitolo->getFieldString(tbTitolo->cd_lingua_3); // // Testo originale
	if (!sPtr->IsEmpty() ) {
//		if (DATABASE_ID == DATABASE_INDICE) // 03/08/2010
//			sf = new Subfield('a');
//		else
//			sf = new Subfield('c');
		sf = new Subfield('a'); // 06/06/2016 Sia per Polo che Per indice
		sPtr->ToLower();
		sf->setData(sPtr);
		df->addSubfield(sf);
	}
	marcRecord->addDataField(df);
	return df;
}// End creaTag101


/**
\brief <b>Tag 102 - Paese di pubblicazione </b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>102</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Paese di pubblicazione</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Non Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    \li a - Paese di pubblicazione. Non ripetibille
    </td></tr>
 <tr><td valign=top>NOTE</td><td>Per l'export di Polo il sottocampo 'a' viene reso minuscolo</td></tr>
</table>

 */
//\li b - Localita' di pubblicazione (NON USATO)

DataField * Marc4cppDocumento::creaTag102_PaeseDiPubblicazione() {
	DataField *df = 0;

	if (tbTitolo->getFieldString(tbTitolo->cd_paese)->IsEmpty())
		return df;

	Subfield *sf;
	df = new DataField((char *)"102", 3);

	CString *sPtr = tbTitolo->getFieldString(tbTitolo->cd_paese);
	if (DATABASE_ID != DATABASE_INDICE) // 02/08/2010
		sPtr->ToLower();
	sf = new Subfield('a', sPtr);
	df->addSubfield(sf);
	marcRecord->addDataField(df);
	return df;
}// End creaTag102


/**
\brief <b>Tag  105 - Dati codificati: monografie</b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>105</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Dati codificati per contenuto: monografie</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Non Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <ul>
	 <li> a - Dati codificati monografie (Non ripetibile). NON USATO PER ORA (spazi)
	 <ul>
	 <li> 0-3 codici illustrazioni
	 	<ul>
		 <li> a - illustrazioni
		 <li> b - mappe
		 <li> c - ritratti
		 <li> d - carte
		 <li> e - piante
		 <li> f - incisioni
		 <li> g - musica
		 <li> h - facsimili
		 <li> i - stemmi
		 <li> j - tavole genealogiche
		 <li> k - schede
		 <li> l - campioni
		 <li> m - registrazioni sonore
		 <li> n - diapositive
		 <li> o - miniature
		 <li> y - nessuna illustrazione
		 <li> # - valore non necessario
		 </ul>
	 <li> 4-7 forma del contenuto
		<ul>
		 <li> a - bibliografie
		 <li> b - cataloghi
		 <li> c - indici
		 <li> d - abstract o sommari
		 <li> e - dizionari
		 <li> f - enciclopedie
		 <li> g - liste
		 <li> h - descrizioni di progetto
		 <li> i - statistiche
		 <li> j - manuali
		 <li> k - brevetti
		 <li> l - norme standardizzate
		 <li> m - dissertazioni o tesi
		 <li> n - leggi e legislazione
		 <li> o - tabelle
		 <li> p - rendiconti tecnici
		 <li> q - questionari
		 <li> r - recensioni
		 <li> s - trattati
		 <li> t - cartoni animati o vignette
		 <li> z - altro
		 <li> # - valore non necessario
		</ul>
	 <li> 8 pubblicazione di congresso
		<ul>
		 <li> 0 - pubblicazione non relativa a congresso
		 <li> 1 - pubblicazione relativa a congresso
		</ul>
	 <li> 9 Festschrift
		<ul>
		 <li> 0 - non festschrift
		 <li> 1 - festschrift
		</ul>
	 <li> 10 indice (codice di disponibilita')
		<ul>
		 <li> 0 - nessun indice
		 <li> 1 - indice
		</ul>
	 <li> 11 tipo di testo letterario
		<ul>
		 <li> a - romanzo
		 <li> b - testo teatrale
		 <li> c - saggi
		 <li> d - satira e umorismo
		 <li> e - epistolari
		 <li> f - racconti
		 <li> g - poesia
		 <li> h - orazioni, retorica

		 <li> i - libretto

		 <li> y - testo non letterario
		 <li> z - miscellanea o altre forme letterarie
		</ul>
	 <li> 12 tipo di biografia
		<ul>
		 <li> a - autobiografia
		 <li> b - biografia individuale
		 <li> c - biografia collettiva
		 <li> d - contiene informazioni biografiche
		 <li> y - materiale non biografico
		</ul>
    </ul>
    </td></tr>
 <tr><td valign=top>NOTE</td><td>Se codice genere = 'z' allora posizione 8 valorizzata a '1' altrimenti valorizzata a '|'</td></tr>
</table>


 */
DataField * Marc4cppDocumento::creaTag105_DatiCodificati() { // bool recordInTbTitset1
	DataField *df = 0;
	char genere[4+1];
	genere[4]=0;
	char ch = ' ';

//	if (!recordInTbTitset1)
//		return df;

	char tipoTestoLetterario=0;
	tipoTestoLetterario = *tbTitset1->getField(tbTitset1->s105_tp_testo_letterario);
	if (!tipoTestoLetterario)
		return df;


	genere[0] = *tbTitolo->getField(tbTitolo->cd_genere_1); // 4-7 forma del contenuto (codici di genere)
	genere[1] = *tbTitolo->getField(tbTitolo->cd_genere_2);
	genere[2] = *tbTitolo->getField(tbTitolo->cd_genere_3);
	genere[3] = *tbTitolo->getField(tbTitolo->cd_genere_4);

	const char *includiGenere = "abcdefgiklmno"; // 05/07/2011 Mail franco/Roveri
	bool hasGenere=false;

//tbTitolo->dumpRecord();

// 05/05/2015 Paolucci, dato non obbligatorio
//	if (!genere[0]) // 06/10/2010  && !genere[1] && !genere[2] && !genere[3]
//		return df;



	// make lowercase
	for (int i=0; i< 4; i++)
	{
		if (genere[i] >= 'A' && genere[i] <= 'Z' )
			genere[i]+=0x20;

//		if (!genere[i] || (genere[i] >= '0' && genere[i] <= '9') ||  genere[i]=='x' || genere[i]=='y' || genere[i]=='r') // 05/07/2011 Mail franco/Roveri
//			genere[i] = ' ';

		if (genere[i]=='z')
			ch=genere[i];

//		if (!strchr(includiGenere, genere[i])) // 05/07/2011 Mail franco/Roveri
		if (!genere[i] || !strchr(includiGenere, genere[i])) // 08/08/2011 problema generi a null (0 binario)
			genere[i] = ' ';


	}
	int i=0;
	for (; i< 4; i++)
	{
		if (genere[i] != ' ')
		{
			hasGenere;
			break;
		}
	}

// 05/05/2015 Paolucci, dato non obbligatorio
//	if (i == 4) // 05/11/2010
//		return df;

	Subfield *sf;
	CString s ((char *)"||||", 4); // 0-3 codici illustrazioni. Mettere filler
//	df = new DataField("105", 3);

	s.AppendString(genere, 4); // 04-07


//	if (ch == 'z')	// Conme da indice 24/09/2010
//		s.AppendString("1||||", 5); //08-12
//									// 05/07/2011 Mail franco/Roveri. Franco dice che deve rimanere. Io l'avevo tolto!
//	else
//		s.AppendString("|||||", 5); // 8-12 filler. Richiseto da CFI

	// 04/05/2015
	// 28/04/2015 Mail Barbieri : unimarc 105 $a posizione 11 con valore "i" (moderno)
	if (ch == 'z')	// Conme da indice 24/09/2010
		s.AppendString((char *)"1||", 3); //08-10

	else
		s.AppendString((char *)"|||", 3); // 08-10

//	if (poloId == POLO_INDICE)
//	{
	// 12/05/2015 Gestione anche per POLO
//		if (tbTitset1->loadRecord(tbTitolo->getField(tbTitolo->bid)))
//		if (recordInTbTitset1) // 30/06/2016
//		{
//			tipoTestoLetterario = *tbTitset1->getField(tbTitset1->s105_tp_testo_letterario);
//			if (tipoTestoLetterario)
//			{
				s.AppendChar(tipoTestoLetterario); // 11
				s.AppendChar('|'); // 12
//			}
//			else
//				s.AppendString("||", 2); // 11-12
//		}
//		else
//			s.AppendString("||", 2); // 11-12
//	}
//	else
//		s.AppendString("||", 2); // 11-12


//	if (!tipoTestoLetterario && !hasGenere) // 19/05/2015 Mail mataloni
//		return df;

//	if (!tipoTestoLetterario) // 19/05/2015 Mail mataloni
//		return df;

	df = new DataField((char *)"105", 3);
	sf = new Subfield('a', &s);
	df->addSubfield(sf);
	marcRecord->addDataField(df);
	return df;
}// End creaTag105



/**
\brief <b>Tag 110 - Dati codificati: Periodici</b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>110</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Dati codificati per contenuto: periodici</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Non Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
<ul>
</ul>
	<li>a - dati codificati contenuto: periodici
		<ul>
		<li>0 tipo di periodico
			<ul>
			 <li>a - periodico
			 <li>b - serie monografica (collana)
			 <li>c - giornale
			 <li>z - altro
			</ul>
		<li>1 frequenza
		<ul>
			 <li>a - quotidiano
			 <li>b - due volte la settimana
			 <li>c - settimanale
			 <li>d - ogni due settimane
			 <li>e - bimensile
			 <li>f - mensile
			 <li>g - bimestrale
			 <li>h - trimestrale
			 <li>i - quadrimestrale
			 <li>j - semestrale
			 <li>k - annuale
			 <li>l - biennale
			 <li>m - triennale
			 <li>n - 3 volte alla settimana
			 <li>o - 3 volte al mese
			 <li>u - non conosciuta
			 <li>y - non regolare
			 <li>z - altro
		</ul>
		<li>2 regolarita'
		<ul>
			 <li>a - regolare
			 <li>b - irregolare normalizzato
			 <li>u - non conosciuta
			 <li>y - non regolare
		</ul>
		<li>3 tipo di materiale
		<ul>
		 <li>a - bibliografie
		 <li>b - cataloghi
		 <li>c - indici
		 <li>d - abstract o sommari
		 <li>e - dizionari
		 <li>f - enciclopedie
		 <li>g - liste
		 <li>h - annuari
		 <li>i - statistiche
		 <li>j - testi programmati
		 <li>k - recensioni
		 <li>l - leggi e legislazione
		 <li>m - rendiconti legali e digesti
		 <li>n - articoli legali
		 <li>o - processi e note ai processi
		 <li>p a' biografie
		 <li>r - recensioni
		 <li>t - cartoni animati o vignette
		 <li>z - altro
		 <li># - valore non necessario
		</ul>
		<li>4-6 natura del contenuto
		<li>7 pubblicazione di congresso
		<ul>
		 <li>0 - periodico non relativo a congresso
		 <li>1 - periodico relativo a congresso
		</ul>
		<li>8 disponibilita' del frontespizio
		<ul>
		 <li>a - nell'ultimo fascicolo del volume - sciolto
		 <li>b - nell'ultimo fascicolo del volume - unito
		 <li>c - nel primo fascicolo del prossimo volume - sciolto
		 <li>d - nel primo fascicolo del prossimo volume - unito
		 <li>e - pubblicato separatamente - gratis- su richiesta
		 <li>f - pubblicato separatamente - gratis- invio automatico
		 <li>g - pubblicato separatamente - da acquistare - su richiesta
		 <li>u - non conosciuta al momento della creazione del record
		 <li>x - non applicabile
		 <li>y - nessun frontespizio
		 <li>z - altro
		</ul>
		<li>9 disponibilita' dell'indice
		<ul>
		 <li>a - ogni fasc. contiene un indice dei contenuti - sciolto
		 <li>b - nell'ultimo fascicolo del volume - sciolto - numerato separatamente
		 <li>c - nell'ultimo fascicolo del volume - non numerato
		 <li>d - nell'ultimo fascicolo del volume - unito
		 <li>e - nel primo fascicolo del volume - sciolto - numerato separatamente
		 <li>f - nel primo fascicolo del volume - sciolto - non numerato
		 <li>g - nel primo fascicolo del volume - unito
		 <li>h - pubblicato separatamente - gratis - invio automatico
		 <li>i - pubblicato separatamente - gratis - invio su richiesta
		 <li>j - pubbl. separatamente - rilegato dall'editore - gratis a' invio automatico
		 <li>k - pubblicato separatamente - rilegato dall'editore - gratis - invio su richiesta
		 <li>l - pubbl. separatamente - rilegato dall'editore - da acquistare
		 <li>m - questo periodico a' un supplemento o una sottoserie indicizzato nel periodico parente
		 <li>u - non conosciuta al momento della creazione del record
		 <li>x - non applicabile
		 <li>y - indice non disponibile
		 <li>z - altro
		</ul>
		 <li>10 disponibilita' dell'indice cumulativo
		<ul>
		 <li>0 - indice cumulativo non disponibile
		 <li>1 - indice cumulativo disponibile
		</ul>
	</ul>
     </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr></table>

 */

DataField * Marc4cppDocumento::creaTag110_Periodici(char natura) {
	DataField *df;
	Subfield *sf;

	df = new DataField((char *)"110", 3);

	sf = new Subfield('a');

	char periodicita, genere1;
	CString s;

	if (natura == 'S')
		s = 'a'; // 0 tipo periodico: periodico (eg. rivista)
	else
		s = 'b'; // 0 tipo di periodico:  serie monografica (collana, natura C)

	periodicita = *tbTitolo->getField(tbTitolo->cd_periodicita);
	if (periodicita >= 'A' && periodicita <= 'Z')
		periodicita+=0x20; // 1 Periodicita'

	if (periodicita) // 11/10/2010
	{
	if (periodicita == 'y' || periodicita == 'u') // 1 frequenza
		s.AppendChar('b');
	else
		//s.AppendChar('a');
		s.AppendChar('z'); // 09/12/2010
	}
	else
		s.AppendChar('|'); // 11/10/2010

	s.AppendChar('u');	// 09/12/2010 2 regolarita u - non conosciuta

	genere1 = *tbTitolo->getField(tbTitolo->cd_genere_1); 	// 3 tipo di materiale (codici di genere)
															// 4-6 natura del contenuto (codici di genere)
	if (!genere1 || (genere1 >= '0' && genere1 <= '9'))
		genere1 = '|'; // 11/10/2010 mail Roveri
	else if (genere1 != ' ')
		genere1+=0x20; // make lowercase

	s.AppendChar(genere1); // 3 tipo di materiale (codici di genere). Riportare solo il primo codice di genere 28/01/2010 14.49
	// 4-6 natura del contenuto (codici di genere) 28/01/2010 14.49
	// 7 pubblicazione di congresso
	// 8 disponibilita' del frontespizio (carattere di riempimento)
	// 9 disponibilita' dell'indice (carattere di riempimento)
	// 10 disponibilita' dell'indice cumulativo (carattere di riempimento)
	s.AppendString((char *)"|||||||", 7);

	sf->setData(&s);
	df->addSubfield(sf);
	marcRecord->addDataField(df);
	return df;
}// End creaTag110






/**
\brief <b>Tag 116 - Dati codificati: arte grafica</b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>116</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Dati codificati contenuto: arte grafica</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <ul>
    <li>a - dati codificati per l'arte grafica (0-17)
		<ul>
		 <li>0 materiale specifico
			<ul>
			 <li>a - collage
			 <li>b - disegno
			 <li>c - pittura
			 <li>d - riproduzione fotomeccanica
			 <li>e - fotonegativo
			 <li>f - riproduzione fotografica
			 <li>h - immagine
			 <li>i - stampa
			 <li>k - disegno tecnico
			 <li>z - altro
			</ul>
		<li>1 materiale del supporto principale
			<ul>
			 <li>a - tela
			 <li>b - cartoncino
			 <li>c - cartone
			 <li>d - vetro
			 <li>e - sintetico
			 <li>f - pellami
			 <li>g - tessuti
			 <li>h - metallo
			 <li>i - carta
			 <li>j - gesso
			 <li>k - legno compresso
			 <li>l - porcellana
			 <li>m - pietra
			 <li>n - legno
			 <li>u - non conosciuto
			 <li>v - collana mista
			 <li>z - altro
			</ul>
		<li>2 materiale del supporto secondario
			<ul>
			 <li>a - tela
			 <li>b - cartoncino
			 <li>c - cartone
			 <li>d - vetro
			 <li>e - sintetico
			 <li>f - pellami
			 <li>g - tessuti
			 <li>h - metallo
			 <li>i - carta
			 <li>j - gesso
			 <li>k - legno compresso
			 <li>l - porcellana
			 <li>m - pietra
			 <li>n - legno
			 <li>u - non conosciuto
			 <li>v - collana mista
			 <li>y - nessun supporto secondario
			 <li>z - altro
			</ul>
		<li>3 colore
			<ul>
			 <li>a - un colore, monocromatico
			 <li>b - bianco e nero
			 <li>c - multicolore
			 <li>d - colorato a mano
			 <li>u - non conosciuto
			 <li>v - misto
			 <li>x - non applicabile
			 <li>z - altro
			</ul>
		<li>4-9 tecniche (del disegno, pittoriche)
			<ul>
			 <li>aa - matita
			 <li>ab - grafite
			 <li>ac - matita colorata
			 <li>ad - inchiostro di china
			 <li>ae - inchiostro di china lavierung
			 <li>af - carboncino
			 <li>ag - gessetto
			 <li>ah - gessetto nero
			 <li>ai - sanguigna
			 <li>aj - acquarello
			 <li>ak - tempera
			 <li>al - guazzo
			 <li>am - pastello
			 <li>an - olio
			 <li>ba - pennarello
			 <li>bb - macchia
			 <li>bc - crayon
			 <li>bd - seppia
			 <li>be - inchiostro
			 <li>bf - caseina
			 <li>bg - doratura
			 <li>bh - encaustico
			 <li>bi - acrilico
			 <li>bj - collage
			 <li>bk - punta d'argento
			 <li>bl - pennello
			 <li>uu - non conosciuto
			 <li>vv - misto
			 <li>xx - non applicabile
			 <li>zz - altro
			</ul>
		<li>10-15 tecniche (stampe)
			<ul>
			 <li>ba - xilografia
			 <li>bb - xilografia a chiaroscuro
			 <li>bc - xilografia a linea bianca
			 <li>bd - camaiu
			 <li>be - eliotipia
			 <li>bf - cromolithografia
			 <li>bg - incisione su linoleum
			 <li>bh - incisione all'acquaforte
			 <li>bi - litografia
			 <li>bj - fotolitografia
			 <li>bk - zincografia
			 <li>bl - algraphy
			 <li>bm - acquatinta
			 <li>bn - reservage
			 <li>ca - vernis-mou
			 <li>cb - incisione
			 <li>cc - incisione a crayon
			 <li>cd - incisione a bulino
			 <li>ce - puntasecca
			 <li>cf - mezzatinta
			 <li>cg - monotipo
			 <li>ch - silkscreen
			 <li>ci - steel engraving
			 <li>cj - grafica al computer
			 <li>ck - potocopia
			 <li>uu - non conosciuto
			 <li>vv - misto
			 <li>xx - non applicabile
			 <li>zz - altro
			</ul>
		<li> 16-17 designazione delle funzioni
    </ul>
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
    </td></tr>
</table>


 */
DataField * Marc4cppDocumento::creaTag116_ArteGrafica() {
	DataField *df = 0;
	const char *bid = tbTitolo->getField(tbTitolo->bid);
	if (!tbGrafica->loadRecord(bid))
		return df;

	Subfield *sf;
	CString s;

	s.AppendChar(*(tbGrafica->getField(tbGrafica->tp_materiale_gra))); // Pos. 0 materiale specifico
	s.AppendChar(*(tbGrafica->getField(tbGrafica->cd_supporto))); // Pos. 1 materiale del supporto principale
	s.AppendChar(' '); // Pos. 2 materiale del supporto secondario
	s.AppendChar(*(tbGrafica->getField(tbGrafica->cd_colore))); // Pos. 3 colore

	if (*tbGrafica->getField(tbGrafica->cd_tecnica_dis_1))
		s.AppendString(tbGrafica->getFieldString(tbGrafica->cd_tecnica_dis_1)); // Pos. 4-9 tecniche (del disegno, pittoriche)
	else
		s.AppendString((char *)"xx", 2);
	if (*tbGrafica->getField(tbGrafica->cd_tecnica_dis_2))
		s.AppendString(tbGrafica->getFieldString(tbGrafica->cd_tecnica_dis_2));
	else
		s.AppendString((char *)"  ", 2);
	if (*tbGrafica->getField(tbGrafica->cd_tecnica_dis_3))
		s.AppendString(tbGrafica->getFieldString(tbGrafica->cd_tecnica_dis_3));
	else
		s.AppendString((char *)"  ", 2);

	if (*tbGrafica->getField(tbGrafica->cd_tecnica_sta_1))
		s.AppendString(tbGrafica->getFieldString(tbGrafica->cd_tecnica_sta_1)); // Pos. 10-15 tecniche (stampe)
	else
		s.AppendString((char *)"xx", 2);
	if (*tbGrafica->getField(tbGrafica->cd_tecnica_sta_2))
		s.AppendString(tbGrafica->getFieldString(tbGrafica->cd_tecnica_sta_2));
	else
		s.AppendString((char *)"  ", 2);
	if (*tbGrafica->getField(tbGrafica->cd_tecnica_sta_3))
		s.AppendString(tbGrafica->getFieldString(tbGrafica->cd_tecnica_sta_3));
	else
		s.AppendString((char *)"  ", 2);
	s.AppendString(tbGrafica->getFieldString(tbGrafica->cd_design_funz)); // Pos 16-17 designazione delle funzioni designazione delle funzioni
	if (s.IsEmpty())
		return df;
	df = new DataField((char *)"116", 3);
	sf = new Subfield('a', &s);
	df->addSubfield(sf);
	marcRecord->addDataField(df);
	return df;
}// End creaTag116


/**
\brief <b>Tag 120 - Dati codificati: materiali cartografici </b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>120</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Dati codificati per contenuto: materiali cartografici</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td> Non ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
	<ul>
	 <li>a - dati codificati dal contenuto Materiale cartografico
		<ul>
		 <li>0 colore
		 <li>1 indice
		 <li>2 testo narrativo
		 <li>3-6 codici del rilievo
		 <li>7-8 proiezione della mappa
		 <li>9-12 meridiano di riferimento
		</ul>
	</ul>
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
    </td></tr>
</table>

 */
DataField * Marc4cppDocumento::creaTag120_MaterialiCartografici() {
	DataField *df = 0;
	Subfield *sf;
	CString s;
	CString *sPtr;

	s = tbCartografia->getField(tbCartografia->cd_colore);	// 0
//	s.AppendString((char *)"        ");  // 1 - 8

	// 10/09/2017 JIRA 17/43
	if (s.Length() > 0)
		s.AppendString((char *)"      ");  // 1 - 6
	else
		s.AppendString((char *)"       ");  // 0 - 6
	sPtr = tbCartografia->getFieldString(tbCartografia->tp_proiezione);
	if (sPtr->Length() == 2)
		s.AppendString(sPtr); // 7-8 tp_proiezione
	else
		s.AppendString((char *)"  ");  // 7-8 filler


	//	s.AppendString(tbCartografia->getFieldString(tbCartografia->cd_meridiano)); // 9-10 (Solo 2 caratteri sul DB)
	sPtr = tbCartografia->getFieldString(tbCartografia->cd_meridiano); // 10/09/2018
	if (sPtr->Length() == 2)
		s.AppendString(sPtr); // 9-10 cd_meridiano
	else
		s.AppendString((char *)"  ");  // 9-10 filler

	s.AppendString((char *)"  "); // 11-12
	if (s.IsEmpty())
		return df;

	df = new DataField((char *)"120", 3);
	sf = new Subfield('a', &s);
	df->addSubfield(sf);
	marcRecord->addDataField(df);
	return df;
}// End creaTag120



/**
\brief <b>Tag 121 - Dati codificati: materiale cartografico,  caratteristiche fisiche </b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>121</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Dati codificati contenuto: Materiale cartografico - Caratteristiche fisiche</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Non ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
    <ul>
    <li>a - dati codificati contenuto: materiale cartografico - caratteristiche fisiche
		<ul>
		 <li>0 dimensioni fisiche
		 <li>1-2 immagine cartografica principale
		 <li>3-4 supporto fisico
		 <li>5 creazione tecnica
		 <li>6 formato di riproduzione
		 <li>7 regolazione geodetica
		 <li>8 formato della pubblicazione
		</ul>
    <li>b - Aerofotografia e dati codificati sui rilevamenti a distanza (Altitudie del sensore)
		<ul>
		 <li>0 altitudine del sensore
<!--
		 <li>1 assetto del sensore
		 <li>2-3 bande dello spettro
		 <li>4 qualita' dell'immagine
		 <li>5 nuvolosita'
		 <li>6-7 valore medio della risoluzione del suolo
-->
		</ul>
    </ul>

    </td></tr>
</table>
 */
DataField * Marc4cppDocumento::creaTag121_MaterialeCartografico_CaratteristicheFisiche() {
	DataField *df =0;
	Subfield *sf;

	CString s((char *)"   ", 3);	// 0, e 1-2

	s.AppendString(tbCartografia->getFieldString(tbCartografia->cd_supporto_fisico)); // 3-4

	if (!tbCartografia->getFieldString(tbCartografia->cd_tecnica)->IsEmpty())
		s.AppendChar(*(tbCartografia->getField(tbCartografia->cd_tecnica)));	// 5
	else
		s.AppendChar(' ');

	if (!tbCartografia->getFieldString(tbCartografia->cd_forma_ripr)->IsEmpty()) // 6
		s.AppendChar(*(tbCartografia->getField(tbCartografia->cd_forma_ripr)));
	else
		s.AppendChar(' ');

	s.AppendChar(' '); // 7

	if (!tbCartografia->getFieldString(tbCartografia->cd_forma_pubb)->IsEmpty()) // 8
		s.AppendChar(*(tbCartografia->getField(tbCartografia->cd_forma_pubb)));
	else
		s.AppendChar(' ');

	if (s.IsEmpty())
		return df;
	df = new DataField((char *)"121", 3);

	sf = new Subfield('a', &s);
	df->addSubfield(sf);
	if (!tbCartografia->getFieldString(tbCartografia->cd_altitudine)->IsEmpty())
	{
		sf = new Subfield('b', tbCartografia->getFieldString(tbCartografia->cd_altitudine));
		df->addSubfield(sf);
	}
	marcRecord->addDataField(df);
	return df;
}// End creaTag121



/**
\brief <b>Tag 123 - Dati codificati: materiale cartografico, scala e coordinate </b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>123</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Dati codificati contenuto: Materiale cartografico - scala e coordinate</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Obbligatorio per il materiale cartografico</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td> Indicatore nella prima posizione: tipo di scala
    <ul>
	 <li>0 scala indeterminata
	 <li>1 scala singola
	 <li>2 scala multipla
	 <li>3 intervallo di scala
	 <li>4 scala approssimata</td></tr>
    </ul>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <ul>
	 <li>a - Tipo di scala
	 <li>b - Scala orizzontale - Costante lineare
	 <li>c - Scala orizzontale - Costante verticale
	 <li>d - Coordinate - Massima estensione ad Ovest, Longitudine
	 <li>e - Coordinate - Massima estensione ad Est, Longitudine
	 <li>f - Coordinate - Massima estensione a Nord, Latitudine
	 <li>g - Coordinate - Massima estensione a Sud, Latitudine
<!--
	 <li>h - Scala angolare
	 <li>i,j - Declinazione
	 <li>k,m - Ascensione
	 <li>n - Equinozio
-->
    </ul>
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
    </td></tr>
</table>

 */
DataField * Marc4cppDocumento::creaTag123_MaterialeCartografico_ScalaECoordinate() {
	DataField *df=0;
	Subfield *sf;
	CString *sPtr;

	df = new DataField((char *)"123", 3);
	df->setIndicator1(*(tbCartografia->getField(tbCartografia->cd_tiposcala)));

	sPtr = tbCartografia->getFieldString(tbCartografia->tp_scala);
	if (!sPtr->IsEmpty())
	{
		sf = new Subfield('a', sPtr);
		df->addSubfield(sf);
	}
	sPtr = tbCartografia->getFieldString(tbCartografia->scala_oriz);
	if (!sPtr->IsEmpty())
	{
		sf = new Subfield('b', sPtr);
		df->addSubfield(sf);
	}
	sPtr = tbCartografia->getFieldString(tbCartografia->scala_vert);
	if (!sPtr->IsEmpty())
	{
		sf = new Subfield('c', sPtr);
		df->addSubfield(sf);
	}
	sPtr = tbCartografia->getFieldString(tbCartografia->longitudine_ovest);
	if (!sPtr->IsEmpty())
	{
		sf = new Subfield('d', sPtr);
		df->addSubfield(sf);
	}
	sPtr = tbCartografia->getFieldString(tbCartografia->longitudine_est);
	if (!sPtr->IsEmpty())
	{
		sf = new Subfield('e', sPtr);
		df->addSubfield(sf);
	}
	sPtr = tbCartografia->getFieldString(tbCartografia->latitudine_nord);
	if (!sPtr->IsEmpty())
	{
		sf = new Subfield('f', sPtr);
		df->addSubfield(sf);
	}
	sPtr = tbCartografia->getFieldString(tbCartografia->latitudine_sud);
	if (!sPtr->IsEmpty())
	{
		sf = new Subfield('g', sPtr);
		//sf->setData();
		df->addSubfield(sf);
	}

  // Mantis 0004627. 16/09/2011 Dopo aver parlato con Roveri e Paolucci si eccezione per la 123 e si tira fuori un record anche senza sottocampi!!!
  if (TIPO_SCARICO != TIPO_SCARICO_OPAC)
	  {
	  if (!df->getSubfields()->Length())
		{
			delete df;
			df = 0;
		}
	  }
	if (df)
		marcRecord->addDataField(df);
	return df;
}// End creaTag123




/**
\brief <b>Tag 124 - Dati codificati: Materiale cartografico, Materiale specifico </b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>124</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Dati codificati contenuto: Materiale cartografico - Materiale specifico</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td> Non ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <ul>
    <li> a - Carattere dell'immagine
	<li> b - Forma dell'unita' cartografica
	<li> c - Tecnica di presentazione...
	<li> d - Posizione della piattaforma
	<li> e - Categoria del satellite
<!--
	<li> f - Nome del satellite
	<li> g - Tecnica di registrazione
-->
    </ul>
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
    </td></tr>
</table>

 */
DataField * Marc4cppDocumento::creaTag124_MaterialeCartografico_MaterialeSpecifico() {
	DataField *df=0;
	Subfield *sf;

//	if (tbCartografia->getFieldString(tbCartografia->tp_immagine)->IsEmpty()) // Mantis 0004571 19/07011
//		return df;

	df = new DataField((char *)"124", 3);

	if (!tbCartografia->getFieldString(tbCartografia->tp_immagine)->IsEmpty())
		{
		sf = new Subfield('a', tbCartografia->getFieldString(tbCartografia->tp_immagine));
		df->addSubfield(sf);
		}
	if (!tbCartografia->getFieldString(tbCartografia->cd_forma_cart)->IsEmpty())
		{
		sf = new Subfield('b', tbCartografia->getFieldString(tbCartografia->cd_forma_cart));
		df->addSubfield(sf);
		}
	if (!tbCartografia->getFieldString(tbCartografia->cd_piattaforma)->IsEmpty())
		{
		sf = new Subfield('d', tbCartografia->getFieldString(tbCartografia->cd_piattaforma));
		df->addSubfield(sf);
		}
	if (!tbCartografia->getFieldString(tbCartografia->cd_categ_satellite)->IsEmpty())
		{
		sf = new Subfield('e', tbCartografia->getFieldString(tbCartografia->cd_categ_satellite));
		df->addSubfield(sf);
		}


	if (df->getSubfields()->length() > 0) // Mantis 0004571 19/07011
		marcRecord->addDataField(df);
	else
	{
		delete df;
		df = 0;
	}
	return df;
}// End creaTag124



/**
\brief <b>Tag 125 - Registrazioni sonore e musica a stampa </b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>125</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Dati codificati contenuto: Registrazioni sonore e musica a stampa</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Non ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
   <li>a - Formato della musica a stampa
   <ul>
	 <li>0 codice di presentazione
<!--
	 <li>1 indicatore delle parti
	 <li>2 caratteri - non ripetibile
-->
   </ul>
   <li>b - Indicatore del testo letterario, 2 caratteri

    </td></tr>
</table>
 */
DataField * Marc4cppDocumento::creaTag125_RegistrazioniSonoreMusicaAStampa(char *bid) {
	DataField *df = 0;
	Subfield *sf;

	if (!tbMusica->loadRecord(bid ))
		return df;

	df = new DataField((char *)"125", 3);
	if (!tbMusica->getFieldString(tbMusica->cd_presentazione)->IsEmpty())
		{
		sf = new Subfield('a', tbMusica->getFieldString(tbMusica->cd_presentazione));
		df->addSubfield(sf);
		}

//	if (!tbMusica->getFieldString(tbMusica->tp_testo_letter)->IsEmpty()) {
//		sf = new Subfield('b', tbMusica->getFieldString(tbMusica->tp_testo_letter));
//		df->addSubfield(sf);
//	}

	// 12/05/2015 (Riunione CED io, Paolucci, Roveri, Scognamiglio, Mataloni)
	if (tbTitset1->loadRecord(bid )) // tbTitolo->getField(tbTitolo->bid
	{
		char *indicatoreTesto = tbTitset1->getField(tbTitset1->s125_indicatore_testo);
		if (*indicatoreTesto)
		{
			sf = new Subfield('b', tbTitset1->getFieldString(tbTitset1->s125_indicatore_testo));
			df->addSubfield(sf);
		}

	}


	if (df->getSubfields()->length())
		marcRecord->addDataField(df);
	else
	{
		delete df;
		return 0;
	}
	return df;
} // End creaTag125




/**
\brief <b>Tag 128 - Esecuzioni musicali e partiture </b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>128</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Dati Codificati Contenuto: Esecuzioni musicali e partiture</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
<!--
	 <li>a - Forma della composizione,2 caratteri
-->
	 <li>b - Voci o strumenti per ensemble,2 caratteri (Organico sintetico)
	 <li>c - Voci o strumenti solisti,2 caratteri (Organico analitico)
	 <li>9 - Tipo elaborazione
     </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
    </td></tr>
</table>

 */
DataField * Marc4cppDocumento::creaTag128_EsecuzioniMusicaliEPartiture(char *bid) {
	DataField *df=0;
	Subfield *sf;
	df = new DataField((char *)"128", 3);

	bool hasOneField = false;


//tbMusica->dumpRecord();

	CString *bidMusicaPtr = tbMusica->getFieldString(tbMusica->bid);
	if (bidMusicaPtr && bidMusicaPtr->Compare(bid)) // 16/06/2016 abbiamo in canna il bid che ci interessa?
		if (!tbMusica->loadRecord(bid ))
		{
			delete df; // 22/05/2017
			df=0;
			return df;
		}

	if (!tbMusica->getFieldString(tbMusica->ds_org_sint)->IsEmpty()) {
		sf = new Subfield('b', tbMusica->getFieldString(tbMusica->ds_org_sint));
		//sf->setData();
		df->addSubfield(sf);
		hasOneField = true;
	}
	if (!tbMusica->getFieldString(tbMusica->ds_org_anal)->IsEmpty()) {
		sf = new Subfield('c', tbMusica->getFieldString(tbMusica->ds_org_anal));
		//sf->setData();
		df->addSubfield(sf);
		hasOneField = true;
	}
	if (!tbMusica->getFieldString(tbMusica->tp_elaborazione)->IsEmpty()) {
		tbMusica->getFieldString(tbMusica->tp_elaborazione)->ToLower();
		sf = new Subfield('9', tbMusica->getFieldString(tbMusica->tp_elaborazione));
		//sf->setData();
		df->addSubfield(sf);
		hasOneField = true;
	}
	if (hasOneField)
		marcRecord->addDataField(df);
	else
	{
		delete df;
		df=0;
	}
	return df;
} // End creaTag128



/**
\brief <b>Tag 140 - Codici per il libro antico</b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>140</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>Codici per il libro antico</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Facoltativo</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Non ripetibile</td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>Non definito</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Non definito</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> a - dati relativi al contenuto
		<UL>
			<li>pos 0-8 = filler;
			<li>pos 9-16 = valore del codice di genere SBN (da 0 a 9, i codici x e y non devono essere  riportati) le posizioni non utilizzate sono a spazio ;
			<li>pos 17-23 = filler;
			<li>pos 24 = 0 se non e' presente il legame con la marca, 1 se e' presente il legame con la marca;
			<li>pos 25 = filler;
			<li>pos 26-27 = spazi
		<UL>
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>Indicazioni dettate da CFI.<BR>Per poli diversi da CFI ed INDICE posizione 26 contiene 'E'</td></tr>
</table>
 */

DataField * Marc4cppDocumento::creaTag140_CodiciPerLibroAntico() { // bool recordInTbTitset1
	DataField *df = 0;
	Subfield *sf;
	bool hasLegameMarca = false;
	char *tipoTestoLetterario;

//	if (!recordInTbTitset1)
//		return df;

	tipoTestoLetterario = tbTitset1->getField(tbTitset1->s140_tp_testo_letterario);
	if (!*tipoTestoLetterario) // 30/06/2016
		return df;


	CString* genere[4];

	genere[0] = tbTitolo->getFieldString(tbTitolo->cd_genere_1);
	genere[1] = tbTitolo->getFieldString(tbTitolo->cd_genere_2);
	genere[2] = tbTitolo->getFieldString(tbTitolo->cd_genere_3);
	genere[3] = tbTitolo->getFieldString(tbTitolo->cd_genere_4);

	CString s((char *)"|||||||||", 9); // pos 0-8 filler
	for (int i=0; i< 4; i++) // pos 09-16
	{
		genere[i]->ToLower(); // // make lowercase
		if (genere[i]->IsEmpty() || isdigit(genere[i]->GetFirstChar()) || genere[i]->StartsWith("yy") || genere[i]->StartsWith("xx") )
			s.AppendString((char *)"  ", 2);
		else
			s.AppendString(genere[i]);
	}

	// s.AppendString("|||||||", 7); // pos 17-23 filler
	// 28/04/2015 Mail Barbieri : unimarc 140 $a posizione 17 - 18 con valore "da" (antico)
//	if (poloId == POLO_INDICE)
//		s.AppendString("da|||||", 7); // pos 17-18 "da", pos 19-23 filler


//	if (poloId == POLO_INDICE)
//	{
	// 12/05/2015 Gestione anche per POLO
//		if (tbTitset1->loadRecord(tbTitolo->getField(tbTitolo->bid)))
//		if (recordInTbTitset1) // 30/06/2016
//		{
//			tipoTestoLetterario = tbTitset1->getField(tbTitset1->s140_tp_testo_letterario);
//			if (*tipoTestoLetterario)
//			{
				s.AppendString(tipoTestoLetterario,2); // 17-18
				s.AppendString((char *)"|||||", 5); // 19-23
//			}
//			else
//				s.AppendString("|||||||", 7); // pos 17-23 filler
//		}
//		else
//			s.AppendString("|||||||", 7); // pos 17-23 filler
//	}
//	else
//		s.AppendString("|||||||", 7); // pos 17-23 filler


	// E' presente il legame con la marca?
	// pos 24 =	0 se non � presente il legame con la marca,
	//			1 se � presente il legame con la marca;
	char *bid = tbTitolo->getField(tbTitolo->bid);
	if (trTitMarRel->existsRecord(bid)) {
		s.AppendChar('1');
		hasLegameMarca = true;
	} else
		s.AppendChar('0');

	s.AppendChar(' '); // pos. 25 = filler;
	if (poloId != POLO_CFI && poloId != POLO_INDICE )
		s.AppendChar('E'); // pos. 26-27 = spazi (26 usato per segnalare antico)
	else
		s.AppendChar(' ');
	s.AppendChar(' ');

	df = new DataField();
	df->setTag((char *)"140");
	sf = new Subfield('a', &s);
	df->addSubfield(sf);

	marcRecord->addDataField(df);
	return df;
} // End creaTag140_CodiciPerLibroAntico




void Marc4cppDocumento::elaboraNote(char * bid)
{
	DataField *df = 0;
	Subfield *sf = 0;
	CString *sPtr;
	while (tbNota->loadNextRecord(bid))
	{
		sPtr = tbNota->getFieldString(tbNota->tp_nota);
		sPtr->Strip(sPtr->trailing, ' ');

		if (	(sPtr->isEqual("323") && IS_TAG_TO_GENERATE(323))
				|| (sPtr->isEqual("327") && IS_TAG_TO_GENERATE(327))
				|| (sPtr->isEqual("330") && IS_TAG_TO_GENERATE(330))
				|| (sPtr->isEqual("336") && IS_TAG_TO_GENERATE(336))
				|| (sPtr->isEqual("337") && IS_TAG_TO_GENERATE(337))
				)
		{

			df = new DataField();
			df->setTag(sPtr->data());	// una di cui sopra

			sf = new Subfield('a', tbNota->getFieldString(tbNota->ds_nota));
			df->addSubfield(sf);
			marcRecord->addDataField(df);
		}
	}
} // End elaboraNote





bool Marc4cppDocumento::elaboraDatiDocumento() {
	DataField *df;

#ifdef DEBUG_ARGE
	if (IS_TAG_TO_GENERATE(001)) // rimosso solo quando voglio generare il minimo di etichette
#endif
	creaTag001_IdRecord(); // Identificativo univoco di record. Obbligatorio

	if (DATABASE_ID == DATABASE_INDICE && IS_TAG_TO_GENERATE(003)) // 21/01/2016
		creaTag003_Permalink();

	if (TIPO_UNIMARC == TIPO_UNIMARC_RIDOTTO)
		return true;

	char *bid = tbTitolo->getField(tbTitolo->bid);

	// Se record cancellato basta l-etichetta 001
	if (marcRecord->getLeader()->getRecordStatus() == 'd')
	{
		if (DATABASE_ID != DATABASE_INDICE && IS_TAG_TO_GENERATE(35)) // 17/10/2016
		{
			// OK bid = "ALE0001438"; // Per simulare un oclc agganciato ad un titolo
			if (trBidAltroId->existsRecordNonUnique(bid))
				while (trBidAltroId->loadNextRecord(bid))
					df = creaTag035(); // OCLC Numbers

		}
		return true;
	}



//tbTitolo->dumpRecord();
	if (IS_TAG_TO_GENERATE(5))
		creaTag005_IdentificatoreVersione(); // Identificatore della versione
	//	char tipoRecord = *(tbTitolo->getField(tbTitolo->tp_record_uni));
	char natura = *(tbTitolo->getField(tbTitolo->cd_natura));
	char tp_materiale = *(tbTitolo->getField(tbTitolo->tp_materiale));
	//CString bid;


//	if ((natura == 'M' || natura == 'W')) // && (tipoNumeroStd == 'I')   || tipoNumeroStd == 'J'
//		creaTag010Isbn(); // Codice ISBN

//	if ((natura == 'S')) // && (tipoNumeroStd == 'J')  tipoNumeroStd == 'I' ||
//		creaTag011Issn(); // Codice ISSN
	if ((isAnticoModernoUndefined()==1) || tp_materiale == TP_MATERIALE_MUSICA_U) {
//		if (df = creaTag012_Impronta())
//			marcRecord->addDataField(df);
		if (IS_TAG_TO_GENERATE(12))
			creaTag012_Impronta();
	}




	//	else if (tipoRecord == '??')
	//		creaTag015(); // International Standard Technical Report Number (ISRN)
	//	if ((tipoNumeroStd == 'L' || tipoNumeroStd == 'E'))

//tbTitolo->dumpRecord();

	elaboraDatiTipoNumeroStandard();



	//	else if (tipoRecord == '?')
	//		creaTag022(); //  Numero di pubblicazione governativa
	//	else if (tipoRecord == '?')
	//		creaTag071(); // Numero dell'editore

#ifdef DEBUG_ARGE
	if (IS_TAG_TO_GENERATE(100)) // rimosso solo quando voglio generare il minimo di etichette
#endif
		creaTag100_CartaDIdentita(); // Dati per l'elaborazione. Obbligatorio


	if (IS_TAG_TO_GENERATE(101))
		creaTag101_LinguaPubblicazione(); // Lingua della pubblicazione
	if (IS_TAG_TO_GENERATE(102))
		creaTag102_PaeseDiPubblicazione(); // Paese di pubblicazione

//tbTitolo->dumpRecord();

	char tp_record_uni = *(tbTitolo->getField(tbTitolo->tp_record_uni));

		if ( (IS_TAG_TO_GENERATE(105) || IS_TAG_TO_GENERATE(140))
//				&& (natura == 'M' || natura == 'W')
//				//&& !isAntico() // 05/07/2011 Mail franco
//	//			&& tp_materiale == 'M' // 05/07/2011 Mail franco Mon, 4 Jul 2011 11:06:21 +0200. Tesi di dottorato digitali in SBNWEB - proposta finale (o quasi)
//
//	//			&& (tp_materiale == 'M' || tp_materiale == 'U') // 12/05/2015 (Riunione CED io, Paolucci, Roveri, Scognamiglio, Mataloni)
//				&& (tp_materiale == 'M'
//					|| tp_materiale == 'E'
//					|| tp_materiale == 'U') // 19/05/2015 si perdeva la E
//
//				&& (tp_record_uni == TP_RECORD_TESTO_a
//					|| tp_record_uni == TP_RECORD_TESTO_MANOSCRITTO_b // Roveri 06/10/2010 deve essere prodotto solo per materiale monografico testuale, dunque solo quando leader pos. 6=�a� or b� e pos. 7=�m�
//					|| tp_record_uni == TP_RECORD_REGISTRAZIONE_SONORA_NON_MUSICALE_i
//					|| tp_record_uni == TP_RECORD_RISORSA_ELETTRONICA_l // 05/07/2011 Mail franco Mon, 4 Jul 2011 11:06:21 +0200. Tesi di dottorato digitali in SBNWEB - proposta finale (o quasi)
//				)
			)
		{

//			if (isModerno105())
//			{
//				if (IS_TAG_TO_GENERATE(105))
//					creaTag105_DatiCodificati(); // Dati codificati per contenuto: monografie
//			}
//			else // if (isAntico()) { // tp_materiale == TP_MATERIALE_ANTICO
//			{
//				if (IS_TAG_TO_GENERATE(140))
//					creaTag140_CodiciPerLibroAntico(); // Codici per il libro antico
//			}
		}

//		if (tbTitset1->loadRecord(tbTitolo->getField(tbTitolo->bid))) // 30/06/2016
//		if (tbTitset1->existsRecord(bid)) // 06/07/2016
		if (
			(natura == 'M' || natura == 'W' || natura == 'S' || natura == 'N')// Solo per nature in tb_titset_1 // 06/07/2016
			&& tbTitset1->loadRecord(bid)
			)
 		{
			//tbTitset1->loadNextRecord(bid); // 06/07/2016

			if (IS_TAG_TO_GENERATE(105))
				creaTag105_DatiCodificati(); // Dati codificati per contenuto: monografie
			if (IS_TAG_TO_GENERATE(140))
				creaTag140_CodiciPerLibroAntico(); // Codici per il libro antico
		}




	if (natura == 'S' || natura == 'C')
		if (IS_TAG_TO_GENERATE(110))
			creaTag110_Periodici(natura); // Dati codificati per contenuto: periodici

	if (tp_materiale == TP_MATERIALE_AUDIOVISIVO_H) // 16/02/2015
	{
		if (tp_record_uni == TP_RECORD_VIDEO_g)
		{
			if (IS_TAG_TO_GENERATE(115))
				creaTag115_audiovisivo();
		}
		else
		{
			if (IS_TAG_TO_GENERATE(126))	// 19/02/2015
			{
				DataField *df = creaTag126_DiscoSonoro();
				if (df && IS_TAG_TO_GENERATE(127)) // 28/01/2016
					creaTag127_DiscoSonoroDurata();

				if (IS_TAG_TO_GENERATE(128)) // 16/06/2015 (mail Barbieri)
					creaTag128_EsecuzioniMusicaliEPartiture(bid); // Dati Codificati Contenuto: Esecuzioni musicali e partiture
			}
		}

	}

	if (tp_materiale == TP_MATERIALE_GRAFICO_G) // 06/10/2010
		if (IS_TAG_TO_GENERATE(116))
			creaTag116_ArteGrafica(); // Dati codificati contenuto: arte grafica

	if (tp_materiale == TP_MATERIALE_CARTOGRAFICO_C) {
// 06/10/2010		creaTag116_ArteGrafica(); // Dati codificati contenuto: arte grafica

		tbCartografia->loadRecord(bid); // tbTitolo->getField(tbTitolo->bid)
//tbCartografia->dumpRecord();
		if (IS_TAG_TO_GENERATE(120))
			creaTag120_MaterialiCartografici(); // Dati codificati per contenuto: materiali cartografici
		if (IS_TAG_TO_GENERATE(121))
			creaTag121_MaterialeCartografico_CaratteristicheFisiche(); // Dati codificati contenuto: Materiale cartografico - Caratteristiche fisiche
		creaTag123_MaterialeCartografico_ScalaECoordinate(); // Dati codificati contenuto: Materiale cartografico - scala e coordinate. Obbligatorio
		if (IS_TAG_TO_GENERATE(124))
			creaTag124_MaterialeCartografico_MaterialeSpecifico(); // Dati codificati contenuto: Materiale cartografico - Materiale specifico
	}
//		else if (tp_materiale == TP_MATERIALE_MUSICA) {
	if (tp_materiale == TP_MATERIALE_MUSICA_U) {
		if (IS_TAG_TO_GENERATE(125))
			creaTag125_RegistrazioniSonoreMusicaAStampa(bid); // Dati codificati contenuto: Registrazioni sonore e musica a stampa
		if (IS_TAG_TO_GENERATE(128))
			creaTag128_EsecuzioniMusicaliEPartiture(bid); // Dati Codificati Contenuto: Esecuzioni musicali e partiture

	}

	// 09/03/2015 Dati comuni area zero. Non ci sono dati comuni per le collane
	if (natura != NATURA_C_COLLANA && IS_TAG_TO_GENERATE(181) && creaTag181_Area0_DatiCodificati(bid))
	{
		if (IS_TAG_TO_GENERATE(182))
			creaTag182_Area0_TipoMediazione(bid); // siccome evitiamo una ricerca in db non dobbiamo fare una vecchia 182 in caso di mancata 181 (come nel caso delle collane)
		if (IS_TAG_TO_GENERATE(183))
			creaTag183_Area0_TipoSupporto(bid); // 19/11/2015
	}



//tbTitolo->dumpRecord();
	// Elabora dati relativi all'ISBD
	// 200, 205,206, 207,208,210,215, 225, 300
	CString * strIndiceAree = tbTitolo->getFieldString(tbTitolo->indice_isbd);
//printf ("\nINDICE AREE ISBD: '%s'", strIndiceAree->Data());
	ATTValVector<CString *> areeVect;
	bool retb;

	if (strIndiceAree->GetLastChar() == ';')
		strIndiceAree->ExtractLastChar();

	retb = strIndiceAree->Split(areeVect, ';');

	int length, startOffset;
	char *areaStartPtr, *areaEndPtr; // , *areaNotePtr
	char *isbd = tbTitolo->getField(tbTitolo->isbd);

//printf ("\nISBD: '%s'", isbd);

//	ricalcolaAreeIsbdUtf8(isbd, &areeVect);

	// Controlliamo che le aree non sforino!
	// e scartiamo solo quelle che sforano

	while (areeVect.Length())
	{
		int lastAreaPos = atoi(areeVect.Last()->data()+4);
		int isbdLength = tbTitolo->getFieldString(tbTitolo->isbd)->Length();
		if (lastAreaPos > isbdLength)
		{
//			SignalAWarning(	__FILE__, __LINE__,	"Aree ISBD sforano la lunnghezza dell'ISBD per bid %s area=%s", bid.data(), areeVect.Last()->data());
//			areeVect.DeleteAndClear();
//			return false;
			areeVect.deleteAndRemoveLastEntry();
		}
		else
			break;
	}
	if (!areeVect.Length())
		return false;


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

//		areaEndPtr = areaStartPtr + length; // '. - ' + EOS

		areaEndPtr = areaStartPtr + length; // '. - ' o '. (('
        if (*areaEndPtr && *areaEndPtr != '.') // 04/11/2010 14.08 Bug CFI tag 215 BVEE010171
            areaEndPtr++; // problema ' (('

		*areaEndPtr = 0;

		if (areeVect.Entry(i)->StartsWith("200-")) // Area 1 - Titolo e responsabilita'
		{
			CString s(areaStartPtr);
#ifdef DEBUG_ARGE
			if (IS_TAG_TO_GENERATE(200)) // rimosso solo quando voglio generare il minimo di etichette
			{
#endif

//			if (df = creaTag200_AreaTitoloEResponsabilita(areaStartPtr, areaEndPtr)) // Obbligatorio
			if (df = creaTag200_AreaTitoloEResponsabilita(&s)) // Obbligatorio areaStartPtr, areaEndPtr
				marcRecord->addDataField(df);
#ifdef DEBUG_ARGE
			}
#endif

		}

		else if ((areeVect.Entry(i)->StartsWith("205-"))) // Area 2 - Edizione
		{
			if (IS_TAG_TO_GENERATE(205))
			{
			if (df = creaTag205_AreaEdizione(areaStartPtr, areaEndPtr))
				marcRecord->addDataField(df);
			}
		}

		else if ((areeVect.Entry(i)->StartsWith("206-"))) // Area 3 - Area specifica del materiale
		{
			if (IS_TAG_TO_GENERATE(206))
			{
			if (df = creaTag206_AreaSpecificaDelMateriale(areaStartPtr,		areaEndPtr))
				marcRecord->addDataField(df);
			}
		}
		else if ((areeVect.Entry(i)->StartsWith("207-"))) // Area ? - Area relativa al materiale specifico: periodici - nomi e date
		{
			if (IS_TAG_TO_GENERATE(207))
			{
			if (df = creaTag207_AreaSpecificaMateriali_PeriodiciNomiDate(areaStartPtr, areaEndPtr))
				marcRecord->addDataField(df);
			}
		}
		else if ((areeVect.Entry(i)->StartsWith("208-"))) // Area ? - Area relativa al materiale specifico: Printed music specific statement
		{
			if (IS_TAG_TO_GENERATE(208))
			{
				if (df = creaTag208_AreaSpecifica_MusicaAStampa(areaStartPtr, areaEndPtr))
				marcRecord->addDataField(df);
			}
		}
		else if ((areeVect.Entry(i)->StartsWith("210-"))) // Area 4 - Area della pubblicazione, distribuzione, ecc.
		{
			if (IS_TAG_TO_GENERATE(210))
			{
			if (df = creaTag210_AreaPubblicazioneProduzioneDistribuzione(areaStartPtr, areaEndPtr))
				{
#ifdef DEBUG_ARGE
Subfield *sf = new Subfield('0', bid, 10);
df->insertSubfield(0,sf);
#endif
				marcRecord->addDataField(df);
				}

			}
		}
		else if ((areeVect.Entry(i)->StartsWith("215-"))) // Area 5 - Area della descrizione fisica
		{
			if (IS_TAG_TO_GENERATE(215))
			{
				df = creaTag215_AreaDescrizioneFisica(areaStartPtr, areaEndPtr);
			if (df)
				marcRecord->addDataField(df);
			}
		}
		else if ((areeVect.Entry(i)->StartsWith("230-"))) // Area ? - Area relativa al materiale specifico: Risorse elettroniche
		{
			if (IS_TAG_TO_GENERATE(230))
				if (df = creaTag230_AreaRisorseElettroniche(areaStartPtr, areaEndPtr))
					marcRecord->addDataField(df);
		}
		else if ((areeVect.Entry(i)->StartsWith("300-"))) // Area 7 - Area delle note
		{
			if (IS_TAG_TO_GENERATE(300))
			{
				if (DATABASE_ID == DATABASE_INDICE)
				{
					if (!strnicmp(areaStartPtr, "Riferimenti", 11) // 14/09/2016
						|| !strnicmp(areaStartPtr, "Cfr", 3)
						|| !strnicmp(areaStartPtr, "Rif.", 4)
						|| !strnicmp(areaStartPtr, "Rif ", 4)
						)
						creaTag300_321_NoteGenerali_ConRiferimenti(bid); // df =
					else
					{
						creaTag300_NoteGenerali(areaStartPtr, areaEndPtr); // df =
//NO 22/05/2017			elaboraNote321(bid); // 10/03/2017 Gestiamo link della 321 (della tb_nota) anche in assenza di riferimenti
					}
#ifdef DEPLOY // 22/05/2017
				elaboraNote321(bid); // Gestiamo link della 321 (della tb_nota)
#endif
				}
				else
				{
					df = creaTag300_NoteGenerali(areaStartPtr, areaEndPtr);
				}
			}
		}



	} // End for



	areeVect.DeleteAndClear();




	if (IS_TAG_TO_GENERATE(326))
		if (df = creaTag326_NotePeriodicita())
			marcRecord->addDataField(df);

	// 16/04/2010 16.08 Elaboriamo le note al documento 323 ecc.
	if (tbNota->existsRecordNonUnique(tbTitolo->getField(tbTitolo->bid)))
		{
		//printf("\nTrovato nota per %s", bid.data());
		elaboraNote(tbTitolo->getField(tbTitolo->bid));
		}

/*
	// 25/11/2015 (temporaneo)
//	if (DATABASE_ID == DATABASE_INDICE  && IS_TAG_TO_GENERATE(321) && tsLinkWeb->existsRecordNonUnique(bid))
	if (DATABASE_ID == DATABASE_INDICE
		&& IS_TAG_TO_GENERATE(321)
		&& tbNota->existsRecordNonUnique(bid)
		) // 18/04/2016
		elaboraNote321(bid);
*/


	return true;
} // End Marc4cppDocumento::elaboraDatiDocumento



/**
\brief <b>Tag 017 - INTERNATIONAL STANDARD RECORDING CODE (ISRC)</b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>016</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>This field contains an International Standard Recording Code and a qualification which
distinguishes between ISRCs when more than one is contained in a record. The ISRC identifies
sound recordings and music video recordings and not physical products. There is no conflict
with existing product catalogue numbering systems (EAN. publisher's number) with which it
coexists.</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Opzionale</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Ripetibile </td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>blank (not defined)</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>blank (not defined)</td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    \li a - Number (ISRC) A correctly applied ISRC including hyphens. The ISRCs are assigned by the designated
agency in each country.Mandatory unless $z is present. Non Ripetibile.

    \li b - Qualificazione. Non Ripetibile.
    \li d - Terms of Availability and/or Price [Obsolete]
			The ISRC does not identify a manifestation. Ripetibile? (manuale non dice).
    \li z - Numero standard errato (in alternativa al $a). Non Ripetibile.
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
    </td></tr>
</table>


The ISRC consists of 4 segments:

COUNTRY CODE: The two characters allocated to you by the US ISRC Agency.
REGISTRANT CODE: Portion allocated to the Registrant by a National Agency (3 alphanumeric characters).
YEAR OF REFERENCE: The 2-digit year in which the ISRC was assigned to the recording.
DESIGNATION CODE: The code assigned to the sound recording by the registrant. This code may not be repeated within the same calendar year (5 digits).

Example: ISRC US-S1Z-09-00001

 */

DataField * Marc4cppDocumento::creaTag016_Isrc() {
	DataField *df;
	Subfield *sf;
	df = new DataField((char *)"016", 3);

	df->setIndicator1(' ');
	df->setIndicator2('1'); // 15/01/2015 mail Roveri

	CString numeroStandard; numeroStandard = tbNumeroStandard->getField(tbNumeroStandard->numero_std);
//	CString nota; nota = tbNumeroStandard->getField(	tbNumeroStandard->nota_numero_std);

	numeroStandard.Strip(numeroStandard.trailing, ' ');

	numeroStandard.InsertCharAt('-',3);
	numeroStandard.InsertCharAt('-',7);
	numeroStandard.InsertCharAt('-',10);

//	sf = new Subfield('a', &numeroStandard);
	CString *notaPtr =  tbNumeroStandard->getFieldString(tbNumeroStandard->nota_numero_std); // 15/01/2015 mail Roveri
	if (notaPtr->IndexSubStringCaseInsensitive("err") != -1)
		sf = new Subfield('z');
	else
		sf = new Subfield('a');
	sf->setData(&numeroStandard);

	df->addSubfield(sf);
	if (!notaPtr->IsEmpty()) {
		sf = new Subfield('b', notaPtr);
		df->addSubfield(sf);
	}
	return df;
}// End creaTag016_Isrc



/**
\brief <b>Tag 072 - UNIVERSAL PRODUCT CODE (UPC)</b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>016</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>This field contains the Universal Product Code. The field corresponds to the ISBD Standard
Number (or Alternative) and Tenns of Availability Area. The field may contain the terms of
availability and/or price, even ifit does not contain a number.</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Opzionale</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Ripetibile </td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>blank (not defined)</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Difference indicator
    The second indicator position contains a value that indicates whether there is a
	difference between a scanned number or code and the same number or code in eye
	readable form.
		0 No information provided
		1 No difference
		2 Difference
    </td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    \li a - Standard Number. A correctly formatted standard number or code. Number or code is formatted according to type. Not repeatable.
    \li b - Qualificazione. Non Ripetibile.
    \li c - Additional codes following standard number or codes. Contains any coded suffix to the identifier. Not repeatable.
    \li d - Terms of Availability and/or Price [Obsolete]
			The ISRC does not identify a manifestation. Non ripetibile.
    \li z - Numero standard errato (in alternativa al $a). Non Ripetibile.
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
    </td></tr>
</table>

 */

DataField * Marc4cppDocumento::creaTag072_Upc() {
	DataField *df;
	Subfield *sf;
	df = new DataField((char *)"072", 3);
	df->setIndicator1(' ');
//	df->setIndicator2('0');
	df->setIndicator2('1'); // 15/01/2015 mail Roveri

	CString numeroStandard; numeroStandard = tbNumeroStandard->getField(tbNumeroStandard->numero_std);
//	CString nota; nota = tbNumeroStandard->getField(	tbNumeroStandard->nota_numero_std);

	numeroStandard.Strip(numeroStandard.trailing, ' ');
//	numeroStandard.InsertCharAt('-',2); // Rimossa sgmentazione. Mail Roveri 27/01/2015
//	numeroStandard.InsertCharAt('-',8);
//	numeroStandard.InsertCharAt('-',14);

//	sf = new Subfield('a', &numeroStandard);
	CString *notaPtr =  tbNumeroStandard->getFieldString(tbNumeroStandard->nota_numero_std); // 15/01/2015 mail Roveri
	if (notaPtr->IndexSubStringCaseInsensitive("err") != -1)
		sf = new Subfield('z');
	else
		sf = new Subfield('a');
	sf->setData(&numeroStandard);

	df->addSubfield(sf);
	if (!notaPtr->IsEmpty()) {
		sf = new Subfield('b', notaPtr);
		df->addSubfield(sf);
	}
	return df;
}// End creaTag072_Upc



/**
\brief <b>Tag 073 - INTERNATIONAL ARTICLE NUMBER (EAN)</b>

<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td>
    <td>016</td></tr>
<tr><td valign=top>Descrizione</td>
    <td>This field contains the International Article Number. The field corresponds to the ISBD
Standard Number (or Alternative) and Terms of Availability Area. The field may contain the
terms of availability and/or price, even if it does not contain a number.</td></tr>
<tr><td valign=top>Obbligatorieta'</td>
    <td>Opzionale</td></tr>
<tr><td valign=top>Ripetibilita'</td>
    <td>Ripetibile </td></tr>
<tr><td valign=top>Indicatore 1</td>
    <td>blank (not defined)</td></tr>
<tr><td valign=top>Indicatore 2</td>
    <td>Difference indicator
    The second indicator position contains a value that indicates whether there is a
	difference between a scanned number or code and the same number or code in eye
	readable form.
		0 No information provided
		1 No difference
		2 Difference
    </td></tr>
 <tr><td valign=top>Sottocampi</td>
    <td>
    \li a - Standard NumberA correctly formatted standard number or code. Not repeatable.
    \li b - Qualificazione. Non Ripetibile.
    \li c - Additional codes following standard number or codes. Contains any coded suffix to the identifier. Not repeatable.
    \li d - Terms of Availability and/or Price. The price of the item and any comment on its availability. Not repeatable.
    \li z - Erroneous Number or Code
			A number or code that has been identified as being erroneously applied to an item or is
			otherwise invalid. It may have been allocated to two different publications or products
			and in this instance cancelled, or it may have been incorrectly printed. Repeatable.
    </td></tr>
 <tr><td valign=top>NOTE</td>
    <td>
    </td></tr>
</table>

 */

DataField * Marc4cppDocumento::creaTag073_Ean() {
	DataField *df;
	Subfield *sf;
	df = new DataField((char *)"073", 3);
	df->setIndicator1(' ');
//	df->setIndicator2('0');
	df->setIndicator2('1'); // 15/01/2015 mail Roveri

	CString numeroStandard; numeroStandard = tbNumeroStandard->getField(tbNumeroStandard->numero_std);
//	CString nota; nota = tbNumeroStandard->getField(	tbNumeroStandard->nota_numero_std);

	numeroStandard.Strip(numeroStandard.trailing, ' ');
//	numeroStandard.InsertCharAt('-',2); 20/02/2015 (Mail Mataloni)
//	numeroStandard.InsertCharAt('-',9);

//	sf = new Subfield('a', &numeroStandard);
	CString *notaPtr =  tbNumeroStandard->getFieldString(tbNumeroStandard->nota_numero_std); // 15/01/2015 mail Roveri
	if (notaPtr->IndexSubStringCaseInsensitive("err") != -1)
		sf = new Subfield('z');
	else
		sf = new Subfield('a');
	sf->setData(&numeroStandard);

	df->addSubfield(sf);
	if (!notaPtr->IsEmpty()) {
		sf = new Subfield('b', notaPtr);
		df->addSubfield(sf);
	}
	return df;
}// End creaTag073_Ean



/*
Rimosso 12/06/2015

bool Marc4cppDocumento::isModerno() {
	// 13/05/2015 Controlliamo il tipo materiale
	// a prescindere dalla data di pubblicazione (potrebbe avere data sbagliata)
	if (*tbTitolo->getField(tbTitolo->tp_materiale) == 'M')
		return true;
	if (*tbTitolo->getField(tbTitolo->tp_materiale) == 'E')
		return false;


	CString aa_pubb_1(tbTitolo->getField(tbTitolo->aa_pubb_1));
	if (aa_pubb_1.Length() != 4)	// data mancante
		return false;

	aa_pubb_1.Replace(".", "0");
	if (aa_pubb_1.CompareI("1830") > 0)
		return true;
	else
		return false;
} // End isModerno
bool Marc4cppDocumento::isAntico() {
	// 13/05/2015 Controlliamo il tipo materiale
	// a prescindere dalla data di pubblicazione (potrebbe avere data sbagliata)
	if (*tbTitolo->getField(tbTitolo->tp_materiale) == 'E')
		return true;
	if (*tbTitolo->getField(tbTitolo->tp_materiale) == 'M')
		return false;

	CString aa_pubb_1(tbTitolo->getField(tbTitolo->aa_pubb_1));
	if (aa_pubb_1.Length() != 4)	// data mancante
		return false;

	aa_pubb_1.Replace(".", "0");
	if (aa_pubb_1.CompareI("1830") < 1)
		return true;
	else
		return false;

} // End isAntico
***/

bool Marc4cppDocumento::isModernoE() {
	const char *bid = tbTitolo->getField(tbTitolo->bid);
	if (*(bid + 3) == 'E') // Antico
		return false;
	else
		return true; // Moderno
}

bool Marc4cppDocumento::isAnticoE() {
	const char *bid = tbTitolo->getField(tbTitolo->bid);
	if (*(bid + 3) == 'E') // Antico
		return true;
	else
		return false; // Moderno
}


bool Marc4cppDocumento::isModerno105() {


if (tbTitset1->loadRecord(tbTitolo->getField(tbTitolo->bid)))
{
//	tbTitset1->dumpRecord();

	char* tipoTestoLetterario = tbTitset1->getField(tbTitset1->s105_tp_testo_letterario);
	if (*tipoTestoLetterario)
		return true;

	// 07/07/2015 Rossana propone problema di quando non esiste il tipo tetso letterario. Vedi NAP0183559

	tipoTestoLetterario = tbTitset1->getField(tbTitset1->s140_tp_testo_letterario);
	if (*tipoTestoLetterario)
		return false;

	// In assenza del tipo testo letterario
	int amu = isAnticoModernoUndefined();
	if (amu == 2) // Moderno
		return true;
	if (amu == 1) // Antico
		return false;

	else
		// undefined!!!!
		SignalAnError(__FILE__,__LINE__,"isModerno105(). Impossibile stabilire se si tratta di antico o moderno: %s.", tbTitolo->getField(tbTitolo->bid));

}
return false;
} // End isModerno105


// 12/06/2015
// 	return  0 = undefined
// 	 		1 = antico
//			2 = moderno
int Marc4cppDocumento::isAnticoModernoUndefined() {
	// 13/05/2015 Controlliamo il tipo materiale
	// a prescindere dalla data di pubblicazione (potrebbe avere data sbagliata)
	if (*tbTitolo->getField(tbTitolo->tp_materiale) == 'E')
		return 1;
	if (*tbTitolo->getField(tbTitolo->tp_materiale) == 'M')
		return 2;

	CString aa_pubb_1(tbTitolo->getField(tbTitolo->aa_pubb_1));
	if (aa_pubb_1.Length() != 4)	// data mancante
		return -1;

	aa_pubb_1.Replace(".", "0");
	if (aa_pubb_1.CompareI((char *)"1830") < 1)
		return 1;
	else
		return 2;

} // End isAntico


// 15/09/2016
/*
Caso A. Link verso base dati digitale
	$a nome base dati
	$c identificativo record nella base dati di riferimento
	$u URL
Caso B. Riferimento a repertorio cartaceo
	$a autore/titolo
	$b data
	$c posizione (vol./p.)


G##DB##	Generato o da Input utente/DB o Repertorio
*/
void Marc4cppDocumento::elaboraNote321(char * bid)
{
	DataField *df = 0;
	Subfield *sf = 0;
	CString *sPtr;

	ATTValVector<CString *> linkAreas;


if (!tbNota321->existsRecordNonUnique(bid))
	return;

while (tbNota321->loadNextRecord(bid)) // 18/04/2016
	{
//tbNota321->dumpRecord();
//continue;

		sPtr = tbNota321->getFieldString(tbNota321->ds_nota);
		sPtr-> Split(linkAreas, "##");

		if (linkAreas.length() != 5)
		{
			printf("\nNota 321 invalida per bid %s: '%s'", bid, sPtr->data());
			linkAreas.DeleteAndClear(); // must be here else memory leak
			continue;
		}

		df = new DataField((char *)"321", 3);
		sf = new Subfield('a', linkAreas.Entry(1)); // Caso A nome della basedati/sito Obbligatorio/Non ripetibile
													// 		B autore/titolo
		df->addSubfield(sf);
		if (linkAreas.Entry(2)->Length())
		{
			sf = new Subfield('b', linkAreas.Entry(3)); // Caso B Data di recensione 17/02/2016 Opzionale/Non ripetibile
			df->addSubfield(sf);
		}

		if (linkAreas.Entry(3)->Length())
		{ // solo se esiste
			sf = new Subfield('c', linkAreas.Entry(3)); // Caso A identificativo record nella base dati di riferimento
														//      B posizione (vol./p.)
			df->addSubfield(sf);
		}

		if (linkAreas.Entry(4)->Length())
		{
			sf = new Subfield('u', linkAreas.Entry(4)); // Caso A URL Non ripetibile
			df->addSubfield(sf);
		}
		marcRecord->addDataField(df);
		linkAreas.DeleteAndClear(); // must be here else memory leak
	}
} // End elaboraNote321

/*
 035 Altri numeri standard
 Indicatori non definiti
 Sottocampi:
 $a Number
 */

DataField * Marc4cppDocumento::creaTag035() {
	DataField *df;
	Subfield *sf;

	df = new DataField();
	df->setTag((char *)"035");
	sf = new Subfield('a');
	//			sf = new Subfield('z');
	CString oclc=(char *)"(";

	oclc.AppendString (trBidAltroId->getField(trBidAltroId->cd_istituzione));

	oclc.Strip(CString::trailing, ' ');

	oclc.AppendChar(')');

	oclc.AppendString(trBidAltroId->getField(trBidAltroId->ist_id));

	sf->setData(&oclc);

	df->addSubfield(sf);

	marcRecord->addDataField(df);
	return df;
} // End creaTag035


