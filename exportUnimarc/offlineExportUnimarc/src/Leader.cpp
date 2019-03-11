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
 * Leader.cpp
 *
 *  Created on: 3-dic-2008
 *      Author: Arge
 */


#include "Leader.h"
#include "MarcConstants.h"
#include "MarcGlobals.h"



Leader::Leader() {
	init();
}
void Leader::init() {
	typeOfRecord = ' ';
	recordStatus = ' ';
	livelloGerarchico = ' ';
	livelloBibliografico = ' ';
	tipoDiCatalogazioneDescrittiva='i'; // -1

	indicatorCount = 2;
	subfieldCodeLength = 2;
	entryMap = " 450 ";
}

Leader::~Leader() {
}



long Leader::getId() const
{
    return id;
}

void Leader::setId(long  id)
{
    this->id = id;
}

int Leader::getRecordLength() const
{
    return recordLength;
}

void Leader::setRecordLength(int recordLength)
{
    this->recordLength = recordLength;
}

char Leader::getRecordStatus() const
{
    return recordStatus;
}

void Leader::setRecordStatus(char recordStatus)
{
    this->recordStatus = recordStatus;
}

char Leader::getTypeOfRecord() const
{
    return typeOfRecord;
}

void Leader::setTypeOfRecord(char typeOfRecord)
{
    this->typeOfRecord = typeOfRecord;
}
/*
char* Leader::getImplDefined1()
{
    return implDefined1.data();
}

void Leader::setImplDefined1(char implDefined1[2])
{
    this->implDefined1 = implDefined1;
}
*/
char Leader::getPos9Undefined() const
{
    return pos9Undefined; //charCodingScheme_typeOfEntity;
}

void Leader::setPos9Undefined(char pos9Undefined) // charCodingScheme
{
    this->pos9Undefined = pos9Undefined; //charCodingScheme_typeOfEntity = charCodingScheme;
}

int Leader::getIndicatorCount() const
{
    return indicatorCount;
}

void Leader::setIndicatorCount(int indicatorCount)
{
    this->indicatorCount = indicatorCount;
}

int Leader::getSubfieldCodeLength() const
{
    return subfieldCodeLength;
}

void Leader::setSubfieldCodeLength(int subfieldCodeLength)
{
    this->subfieldCodeLength = subfieldCodeLength;
}

int Leader::getBaseAddressOfData() const
{
    return baseAddressOfData;
}

void Leader::setBaseAddressOfData(int baseAddressOfData)
{
    this->baseAddressOfData = baseAddressOfData;
}
/*
char* Leader::getImplDefined2()
{
    return implDefined2.data();
}

void Leader::setImplDefined2(char* implDefined2)
{
    this->implDefined2 = implDefined2;
}
*/
char* Leader::getEntryMap()
{
    return entryMap.data();
}

void Leader::setEntryMap(char* entryMap)
{
    this->entryMap = entryMap;
}




void Leader::clear()
{
	id=-1;
	recordLength=-1;
//	recordStatus=-1;
//	typeOfRecord=-1;
//	implDefined1.Clear();
//	livelloBibliografico = -1;
//	livelloGerarchico = -1;



	typeOfRecord = ' ';
	recordStatus = ' ';
	livelloGerarchico = ' ';
	livelloBibliografico = ' ';



	pos9Undefined = BLANK; //charCodingScheme_typeOfEntity=-1;
//	indicatorCount=-1;
//	subfieldCodeLength=-1;
	baseAddressOfData=-1;
//	implDefined2.Clear();
	livelloDiCodifica= ' ' ; // default per licello di authority 72-97
	tipoDiCatalogazioneDescrittiva='i'; // -1




	//	entryMap.Clear();
	stringed.Clear();
}

void Leader::setLivelloBibliografico(char unLivelloBibliografico)
{
	livelloBibliografico = unLivelloBibliografico;
}
char Leader::getLivelloBibliografico()
{
	return livelloBibliografico;
}
void Leader::setLivelloGerarchico(char unLivelloGerarchico)
{
	livelloGerarchico = unLivelloGerarchico;
}
char Leader::getLivelloGerarchico()
{
	return livelloGerarchico;
}


void Leader::setLivelloDiCodifica(char code)
{
	livelloDiCodifica = code;
}
char Leader::getLivelloDiCodifica()
{
	return livelloDiCodifica;
}
void Leader::setTipoDiCatalogazioneDescrittiva(char code)
{
	tipoDiCatalogazioneDescrittiva = code;
}
char Leader::getTipoDiCatalogazioneDescrittiva()
{
	return tipoDiCatalogazioneDescrittiva;
}


/*!
\brief <b>Tag LDR - Leader</b>
<table>
<tr>
<th valign=top>Definizione</th><th>Descrizione</th></tr>
<tr><td valign=top>Tag</td><td>LDR</td></tr>
<tr><td valign=top>Descrizione</td><td>Leader</td></tr>
<tr><td valign=top>Obligatorieta'</td><td>Obbligatorio</td></tr>
<tr><td valign=top>Ripetibilita'</td><td>Non ripetibile</td></tr>

 <tr><td valign=top>Sottocampi</td>
    <td>
    <UL>
        <li> Posizione 00-04: Lunghezza record
        <li> Posizione     5: Stato del record
            <UL>
                   <li> d Cancellato (non usato per scarichi OPAC)
                   <li> n Nuovo
<!--
                   <li> c Corretto
                   <li> o Record a livello piu' alto
                   <li> p Precedentemente incompleto
-->
            </UL>

        <li> Posizione     6: Tipo record
            <UL>
                   <li> a Materiale a stampa
                   <li> b Materiale manoscritto
                   <li> c Spartiti musicali a stampa
                   <li> d Spartiti musicali manoscritti
                   <li> e Materiale cartografico a stampa
                   <li> f Materiale cartografico manoscritto
                   <li> g Video
                   <li> i Audio registrazioni, esecuzioni non musicali
                   <li> j Audio registrazioni, esecuzioni musicali
                   <li> k Grafica bidimensionale (disegni, dipinti etc.)
                   <li> l Computer media
                   <li> m Multimedia
                   <li> r Opere d'arte tridimensionali
                   <li> u Non valorizzato
            </UL>

        <li> Posizione     7: Livello bibliografico
            <UL>
                   <li> a Analitico
                   <li> m Monografia
                   <li> s Seriale / Collane
                   <!-- <li> c Collezioni fattizie -->
            </UL>

        <li> Posizione     8: Livello gerarchico
            <UL>
                   <li> # Non definito
                   <li> 0 Senza relazione gerarchica
                   <li> 1 Livello piu' alto
                   <li> 2 Al di sotto del livello piu' alto
            </UL>

        <li> Posizione     9: Non definita
        <li> Posizione    10: Contatore di indicatori (sempre 2 in unimarc)
        <li> Posizione    11: Lunghezza codice di sottocampo (sempre 2 in unimarc)
        <li> Posizione 12-16: Indirizzo base dei dati
        <li> Posizione    17: Livello di codifica
            <UL>
                   <li> # Completamente codificato
                   <li> 1 Codifica senza l'esame del documento
                   <li> 2 CIP
                   <li> 3 Sottolivello 3
            </UL>

        <li> Posizione    18: Tipo di catalogazione descrittiva
            <UL>
                   <li> # Interamente conforme ISBD
                   <li> i Parzialmente conforme ISBD
                   <li> n Non conforme ISBD
            </UL>
    </UL>
    </td></tr>
 <tr><td valign=top>NOTE</td><td></td></tr>
</table>
*/
void Leader::buildLeader()
{
	char buf[20];
	if (SISTEMA_NUMERICO_UNIMARC == SISTEMA_NUMERICO_DECIMALE) // 20/09/2017
		sprintf (buf, "%05d", recordLength);
	else
	{
		sprintf (buf, "%05x", recordLength); // Esadecimale
		buf[0] |= 0x80; // Setta il bit per dire che sto trattando il sistema esadecimale
	}
	stringed.AppendString(buf, 5);

	stringed.AppendChar(recordStatus);
	stringed.AppendChar(typeOfRecord);
	//stringed.AppendString(implDefined1.data());
	stringed.AppendChar(livelloBibliografico);
	stringed.AppendChar(livelloGerarchico);

	stringed.AppendChar(pos9Undefined); // charCodingScheme_typeOfEntity

	stringed.AppendChar(indicatorCount+0x30); // Ascii numeric
	stringed.AppendChar(subfieldCodeLength+0x30);

	sprintf (buf, "%05d", baseAddressOfData);
	stringed.AppendString(buf, 5);

	//	stringed.AppendString(implDefined2.data());
	stringed.AppendChar(livelloDiCodifica);
	stringed.AppendChar(tipoDiCatalogazioneDescrittiva);

	stringed.AppendString(&entryMap);
} // End buildLeader


char *Leader::toXml()
{
//	char buf[20];
	stringed = "\n\t<lab>";
	buildLeader();
	stringed.AppendString("</lab>", 6);
	return stringed.data();
}

CString *Leader::toCStringXml()
{
//	char buf[20];
	stringed = "\n\t<lab>";
	buildLeader();
	stringed.AppendString("</lab>", 6);
	return &stringed;
}



CString *Leader::getLeader()
{
	stringed = "";
	buildLeader();
	return &stringed;
}

#undef DEBUG_ARGE

char *Leader::toString()
{
#ifdef DEBUG_ARGE
	stringed.Clear();
#else
	stringed.assign("=LDR  ", 6);
	buildLeader();
#endif
	return stringed.data();
}

CString *Leader::toCString()
{
#ifdef DEBUG_ARGE
	stringed.Clear();
#else
	stringed.assign("=LDR  ", 6);
	buildLeader();
#endif
	return &stringed;
}

