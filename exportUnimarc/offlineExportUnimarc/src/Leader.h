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
 * Leader.h
 *
 *  Created on: 3-dic-2008
 *      Author: Arge
 */


#ifndef LEADER_H_
#define LEADER_H_
#include "library/CString.h"
#include "MarcConstants.h"

class Leader {

private:
	// Leader can be either for Document or Authority. Format DocElement_AuthorityElement

	long 	id;
	int 	recordLength;		// The logical record length (Position 0-4).
	char 	recordStatus; 		// The record status (Position 5).
								//	c Corretto
								//	d Cancellato
								//  n Nuovo
								//	o Record a livello piu' alto
								//	p Precedentemente incompleto

	char 	typeOfRecord; 		// Type of record (Position 6).
								//	a Materiale a stampa
								//	b Materiale manoscritto
								//	c Spartiti musicali a stampa
								//	d Spartiti musicali manoscritti
								//	e Materiale cartografico a stampa
								//	f Materiale cartografico manoscritto
								//	g Video
								//	i Audio registrazioni, esecuzioni non musicali
								//	j Audio registrazioni, esecuzioni musicali
								//	k Grafica bidimensionale (disegni, dipinti etc.)
								//	l Computer media
								//	m Multimedia
								//	r Opere d'arte tridimensionali
								//  u Default quando tp_record_uni non valorizzato
		char livelloBibliografico;
								// 07 Livello bibliografico
								//	a Analitico
								//	m Monografia
								//	s Seriale / Collane
								//	c Collezioni fattizie
	char livelloGerarchico;
								// 08 Livello gerarchico
								//	# Non definito
								//	0 Senza relazione gerarchica
								//	1 Livello piu' alto
								//	2 Al di sotto del livello piu' alto

	char 	pos9Undefined; //charCodingScheme_typeOfEntity;	// Character coding scheme (Position 9).

	int 	indicatorCount;		// The indicator count (Position 10).
								// Sempre 2 in UNIMARC

	int 	subfieldCodeLength;	// The subfield code length (Position 11).
								// Sempre 2 in UNIMARC

	int 	baseAddressOfData;	// The base address of data (Position 12-16).

//	CString implDefined2;		// Implementation defined (Position 17-18)
	char livelloDiCodifica;		// 17 Livello di codifica
								//	# Completamente codificato
								//	1 Codifica senza l'esame del documento
								//	2 CIP
								//	3 Sottolivello 3

	char tipoDiCatalogazioneDescrittiva;// 18 Tipo di catalogazione descrittiva
								//	# Interamente conforme ISBD
								//	i Parzialmente conforme ISBD
								//	n Non conforme ISBD



	CString entryMap;		// Entry map (Position 19-23).
							// 19 Non definito
							// 20 Posizioni per la lunghezza del campo
							//    Sempre 4 in UNIMARC
							// 21 Posizioni per il carattere di partenza del campo
							//	  Sempre 5 in UNIMARC
							// 22 Lunghezza della parte dell'Indice definita dall'implementazione
							//	  Sempre 0 in UNIMARC
							// 23 Non definito


	CString stringed;
	void buildLeader();

	void init();
public:
	Leader();
	virtual ~Leader();
    long getId() const;
    void setId(long  id);
    int getRecordLength() const;
    void setRecordLength(int recordLength);
    char getRecordStatus() const;
    void setRecordStatus(char recordStatus);
    char getTypeOfRecord() const;
    void setTypeOfRecord(char typeOfRecord);
    char* getImplDefined1();
    void setImplDefined1(char implDefined1[2]);

    char getPos9Undefined() const; // CharCodingScheme_typeOfEntity
    void setPos9Undefined(char charCodingScheme); // CharCodingScheme_typeOfEntity

    int getIndicatorCount() const;
    void setIndicatorCount(int indicatorCount);
    int getSubfieldCodeLength() const;
    void setSubfieldCodeLength(int subfieldCodeLength);
    int getBaseAddressOfData() const;
    void setBaseAddressOfData(int baseAddressOfData);
    char* getImplDefined2() ;
    void setImplDefined2(char implDefined2[2]);
    char* getEntryMap() ;
    void setEntryMap(char entryMap[5]);
    void clear();
    char *toString();
    CString *toCString();
    char *toXml();
    CString *toCStringXml();

    CString *getLeader();

    void setLivelloBibliografico(char unLivelloBibliografico);
    char getLivelloBibliografico();
    void setLivelloGerarchico(char unLivelloGerarchico);
    char getLivelloGerarchico();

	void setLivelloDiCodifica(char code);
	char getLivelloDiCodifica();
	void setTipoDiCatalogazioneDescrittiva(char code);
	char getTipoDiCatalogazioneDescrittiva();

};

#endif /* LEADER_H_ */
