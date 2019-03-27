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
 * DataField.cpp
 *
 *  Created on: 3-dic-2008
 *      Author: Arge
 */


#include "DataField.h"
#include "MarcConstants.h"
#include "MarcGlobals.h"
#include <stdlib.h>

#define MAX_SUBFIELDS 150

DataField::DataField() {
    subfieldsVector = new ATTValVector<Subfield *>();
    this->indicator1 = INDICATOR_UNDEFINED;
    this->indicator2 = INDICATOR_UNDEFINED;
    embedded = 0;
}

DataField::DataField(char* tag, int len)
{
    subfieldsVector = new ATTValVector<Subfield *>();
    this->tag.assign(tag, len);
    this->tagId = atoi(tag);
    this->indicator1 = INDICATOR_UNDEFINED;
    this->indicator2 = INDICATOR_UNDEFINED;
    embedded = 0;
}



DataField::DataField(char* tag, char ind1, char ind2) {
//        this();
		subfieldsVector = new ATTValVector<Subfield *>();
        this->tag = tag;
        this->tagId = atoi(tag);
        this->indicator1 = ind1;
        this->indicator2 = ind2;
        embedded = 0;
    }

DataField::DataField(CString* tag, char ind1, char ind2) {
//        this();
		subfieldsVector = new ATTValVector<Subfield *>();
        this->tag.assign(tag);
        this->tagId = atoi(tag->data());
        this->indicator1 = ind1;
        this->indicator2 = ind2;
        embedded = 0;
    }


// Crea un'istanza di un datafield a partire da un'altro datafield
bool DataField::setDataField(DataField* aDf)
{
	Subfield *sfNew, *sfOld;
//	subfieldsVector = new ATTValVector<Subfield *>();
	ATTValVector <Subfield *> *sfv;
//	this->tag = aDf->tag;
	this->tag = aDf->tag.data();
	this->tagId = aDf->tagId;
    this->indicator1 = aDf->indicator1;
    this->indicator2 = aDf->indicator2;

    // Copiamo i subfields
    sfv = aDf->getSubfields();
    for (int i=0; i < sfv->length(); i++)
    {
    	sfOld = sfv->Entry(i);

//    	sfNew = new Subfield();
//    	sfNew->setId(sfOld->getId());
//    	sfNew->setCode(sfOld->getCode());
//    	sfNew->setData(sfOld->getDataString());
    	sfNew = new Subfield(sfOld->getId(), sfOld->getCode(), sfOld->getDataString());
    	this->addSubfield(sfNew);
    }
    return true;
} // End DataField::setDataField


DataField::~DataField() {
	stop();
}

void DataField::stop() {
	if (subfieldsVector)
	{
		subfieldsVector->DeleteAndClear();
		delete subfieldsVector;
	}

	if (embedded)
		delete embedded;

}



/**
 * Sets the first indicator.
 *
 * @param ind1
 *            the first indicator
 */
void DataField::setIndicator1(char ind1) {
        this->indicator1 = ind1;
    }

/**
 * Returns the first indicator
 *
 * @return char - the first indicator
 */
char DataField::getIndicator1() {
        return indicator1;
    }


/**
 * Sets the second indicator.
 *
 * @param ind2
 *            the second indicator
 */
void DataField::setIndicator2(char ind2) {
        this->indicator2 = ind2;
    }

/**
 * Returns the second indicator
 *
 * @return char - the second indicator
 */
 char DataField::getIndicator2() {
        return indicator2;
    }


/**
 * Adds a <code>Subfield</code>.
 *
 * @param subfield
 *            the <code>Subfield</code> object
 * @throws IllegalAddException
 *             when the parameter is not a <code>Subfield</code> instance
 */
void DataField::addSubfield(Subfield *subfield) {
            subfieldsVector->Add(subfield);
    }


void DataField::insertSubfield(int pos, Subfield *subfield) {
            subfieldsVector->Insert(pos, subfield);
    }

    /**
     * Inserts a <code>Subfield</code> at the specified position.
     *
     * @param index
     *            the position within the list
     * @param subfield
     *            the <code>Subfield</code> object
     * @throws IllegalAddException
     *             when the parameter is not a <code>Subfield</code> instance
     */
/*
void DataField::addSubfield(int index, Subfield *subfield) {
        //subfieldsVector->Add(index, subfield);
	printf("TO DO: DataField::addSubfield(int index, Subfield subfield)");
    }
*/
    /**
     * Removes a <code>Subfield</code>.
     */
void DataField::removeSubfield(char code) {

//	int i = 0;
//	subfieldsVector->DeleteAndRemoveByEntry(i);

	for (int i=0; i < subfieldsVector->Length(); i++)
    {
		Subfield *sf = subfieldsVector->Entry(i);
		if (sf->getCode() == code)
		{
			subfieldsVector->DeleteAndRemoveByEntry(i);
			break;
		}
    }

    }
void DataField::removeLastSubfield(char code) {

//	int i = 0;
//	subfieldsVector->DeleteAndRemoveByEntry(i);

	for (int i=subfieldsVector->Length()-1; i > -1; i--)
    {
		Subfield *sf = subfieldsVector->Entry(i);
		if (sf->getCode() == code)
		{
			subfieldsVector->DeleteAndRemoveByEntry(i);
			break;
		}
    }

    }



    /**
     * Returns the list of <code>Subfield</code> objects.
     *
     * @return List - the list of <code>Subfield</code> objects
     */
ATTValVector<Subfield*> * DataField::getSubfields() {
        return subfieldsVector;
    }

/*
 * Non dimenticare di CANCELLARE (DELETE) l'oggetto una volta finito l'uso
 */
ATTValVector <Subfield *> * DataField::getSubfields(char code) {

		ATTValVector <Subfield *> *retSubfieldsVect = new ATTValVector<Subfield *>();

		for (int i=0; i < subfieldsVector->Length(); i++)
        {
			Subfield *sf = subfieldsVector->Entry(i);
			if (sf->getCode() == code)
				retSubfieldsVect->Add(sf);
        }
        return retSubfieldsVect;
    }

Subfield *DataField::getSubfield(char code) {
    for (int i=0; i < subfieldsVector->Length(); i++)
    {
		Subfield *sf = subfieldsVector->Entry(i);
		if (sf->getCode() == code)
			return sf;
    }
    return 0;
}

Subfield *DataField::getNthSubfield(char code, int nth) { // start at 1

	int ctr=0;
	for (int i=0; i < subfieldsVector->Length(); i++)
    {
		Subfield *sf = subfieldsVector->Entry(i);
		if (sf->getCode() == code)
		{
			ctr++;
			if (ctr == nth)
				return sf;
		}
    }
    return 0;
}




int DataField::getId()
{
    return tagId;
}

//void DataField::setId(long  id)
//{
//    this->id = id;
//}

bool DataField::find(char* pattern) {
//        Pattern p = Pattern.compile(pattern);
//        Matcher m = p.matcher(getData());
//        return m.find();
    printf ("TODO");
    return false;
}





//char* DataField::toSubTag() {
CString* DataField::toSubTag() {
	Subfield *subfield;

// 	stringed = " "; 15/01/2010 11.31
	stringed.AppendString(&tag);
	stringed.AppendChar(indicator1);
	stringed.AppendChar(indicator2);

	for (int i=0; i < subfieldsVector->Length(); i++)
	{
		subfield = subfieldsVector->Entry(i);
		stringed.AppendChar(SUBFIELD_DELIMITER); // '$' diventa 0x1f 18/01/2010 20.59
		stringed.AppendChar(subfield->getCode());
		stringed.AppendString(subfield->getDataString());
	}
    return &stringed;
    }

/**
 * Returns a string representation of this subfield.
 *
 * <p>
 * Example:
 *
 * <pre>
 * $aSummerland /
 * </pre>
 *
 * @return String - a string representation of this subfield
 */
char* DataField::toString() {
	Subfield *subfield;
	stringed.assign("\n=", 2);
	stringed.AppendString(&tag);
	stringed.AppendString("  ", 2);
		stringed.AppendChar(indicator1);
		stringed.AppendChar(indicator2);
	int len = subfieldsVector->Length();
	if (len > MAX_SUBFIELDS)
	{
		char buf[200];
		sprintf (buf, "!~!RECORD TRONCATO!~!Tag = %s ha + di %d sottocampi (%d). Faccio vedere solo i primi %d", this->getTagString()->data(), MAX_SUBFIELDS, len, MAX_SUBFIELDS);
		printf ("%s", buf);
//		stringed.AppendString ("!~!RECORD TRONCATO!~!PRESENTI SOLO I PRIMI 100 SOTTOCAMPI!~!");
		stringed.AppendString (buf);
		len = MAX_SUBFIELDS;
	}

	//char *ptr;
	for (int i=0; i < len; i++)
	{
		subfield = subfieldsVector->Entry(i);
		stringed.AppendString(subfield->toCString());
	}
    return stringed.data();
    } // End toString()

CString * DataField::toCString(char *recId) {
	Subfield *subfield;
	stringed.assign("\n=", 2);
	stringed.AppendString(&tag);
	stringed.AppendString("  ", 2);
	stringed.AppendChar(indicator1);
	stringed.AppendChar(indicator2);
	int len = subfieldsVector->Length();
/* 19/03/2015 gestito a livello superiore
	if (len > MAX_SUBFIELDS)
	{
		char buf[200];
		sprintf (buf, "!~!RECORD TRONCATO!~!Tag = %s ha + di %d sottocampi (%d). Faccio vedere solo i primi %d", this->getTagString()->data(), MAX_SUBFIELDS, len, MAX_SUBFIELDS);
		printf ("\n%s: %s", recId, buf);
		stringed.AppendString (buf);
		len = MAX_SUBFIELDS;
	}
*/
	//char *ptr;
	for (int i=0; i < len; i++)
	{
		subfield = subfieldsVector->Entry(i);
		stringed.AppendString(subfield->toCString());
	}
    return &stringed;
    } // End toString()








char* DataField::toXml(int indent) {
	Subfield *subfield;
	stringed = '\n';
	for (int i=0; i< indent; i++)
		stringed.AppendChar('\t');
	stringed.AppendString("<df t=\"", 7);
	stringed.AppendString(&tag);
	stringed.AppendString("\" i1=\"", 6);
	stringed.AppendChar(indicator1);
	stringed.AppendString("\" i2=\"", 6);
	stringed.AppendChar(indicator2);
	stringed.AppendString("\">", 2);
	if (!embedded)
	{
		int len = subfieldsVector->Length();
		for (int i=0; i < len; i++)
		{
			subfield = subfieldsVector->Entry(i);
			stringed.AppendString(subfield->toXml(indent+1));
		}
	}
	else
	{
		ControlField* cf;
		DataField *df;
		// Scarichiamo i control field
		stringed.AppendString("\n\t\t<s1>", 7);
		for (int i=0; i < embedded->getControlFieldsVector()->length(); i++)
		{
			cf = embedded->getControlFieldsVector()->Entry(i);
			stringed.AppendString (cf->toXml(3));
		}
		stringed.AppendString("\n\t\t</s1>", 8);
		// Scarichiamo i data field
		stringed.AppendString("\n\t\t<s1>", 7);
		for (int i=0; i < embedded->getDataFieldsVector()->length(); i++)
		{
			df = (DataField *)embedded->getDataFieldsVector()->Entry(i);
			stringed.AppendString (df->toXml(3));
		}
		stringed.AppendString("\n\t\t</s1>", 8);
	}
	stringed.AppendChar ('\n');
	for (int i=0; i< indent; i++)
		stringed.AppendChar('\t');
    stringed.AppendString("</df>", 5);
	return stringed.data();
    } // End toXml

CString* DataField::toCStringXml(int indent) {
	Subfield *subfield;
	stringed = '\n';
	for (int i=0; i< indent; i++)
		stringed.AppendChar('\t');
	stringed.AppendString("<df t=\"", 7);
	stringed.AppendString(&tag);
	stringed.AppendString("\" i1=\"", 6);
	stringed.AppendChar(indicator1);
	stringed.AppendString("\" i2=\"", 6);
	stringed.AppendChar(indicator2);
	stringed.AppendString("\">", 2);
	if (!embedded)
	{
		int len = subfieldsVector->Length();
		for (int i=0; i < len; i++)
		{
			subfield = subfieldsVector->Entry(i);
			stringed.AppendString(subfield->toXml(indent+1));
		}
	}
	else
	{
		ControlField* cf;
		DataField *df;
		// Scarichiamo i control field
		stringed.AppendString("\n\t\t<s1>", 7);
		for (int i=0; i < embedded->getControlFieldsVector()->length(); i++)
		{
			cf = embedded->getControlFieldsVector()->Entry(i);
			stringed.AppendString (cf->toXml(3));
		}
		stringed.AppendString("\n\t\t</s1>", 8);
		// Scarichiamo i data field
		stringed.AppendString("\n\t\t<s1>", 7);
		for (int i=0; i < embedded->getDataFieldsVector()->length(); i++)
		{
			df = (DataField *)embedded->getDataFieldsVector()->Entry(i);
			stringed.AppendString (df->toXml(3));
		}
		stringed.AppendString("\n\t\t</s1>", 8);
	}
	stringed.AppendChar ('\n');
	for (int i=0; i< indent; i++)
		stringed.AppendChar('\t');
    stringed.AppendString("</df>", 5);
	return &stringed;
    } // End toCStringXml



long DataField::getToXmlLength() {
    return stringed.Length();
    }





void DataField::dump() {
	Subfield *sf;
	printf ("\nTag %s", this->getTagString()->data());
	for (int i=0; i < subfieldsVector->Length(); i++)
	{
		sf = subfieldsVector->Entry(i);
		printf ("\n\tSubfield '%c'=%s", sf->getCode(), sf->getData());
	}
    } // End dump()

void DataField::setTag(char * tag) {
        this->tag.assign (tag, 3);
        this->tagId = atoi(tag);
    }

//char *DataField::getTag() {
//        return this->tag.data();
//    }

CString *DataField::getTagString() {
        return &tag;
    }


void DataField::embedDataField(DataField * dfPtr) {
	CString s;
	Subfield *sf;
	// Embed
	ATTValVector<Subfield*> *subfieldsVector;
    subfieldsVector = dfPtr->getSubfields();
    Subfield* sfPtr;
//    s.Clear();
    s.AppendString(dfPtr->getTagString());
    s.AppendChar(dfPtr->getIndicator1());
    s.AppendChar(dfPtr->getIndicator2());

    sf = new Subfield('1', &s);
    addSubfield(sf);
    for (int i = 0; i < subfieldsVector->length(); i++) {
    	sfPtr = subfieldsVector->Entry(i);
        sf = new Subfield(sfPtr->getCode(), sfPtr->getDataString());
        addSubfield(sf);
    	}

    }


