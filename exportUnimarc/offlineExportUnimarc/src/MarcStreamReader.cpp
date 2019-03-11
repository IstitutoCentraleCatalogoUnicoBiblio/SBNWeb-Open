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
 * MarcStreamReader.cpp
 *
 *  Created on: 8-dic-2008
 *      Author: Arge
 */


#include <stdio.h>
#include <stdlib.h>

#include "MarcStreamReader.h"
#include "MarcConstants.h"
#ifdef TRACK_MEMORY_LEAKS
    #include "nvwa/debug_new.h"
#endif

MarcStreamReader::MarcStreamReader() {
	init();
}

MarcStreamReader::MarcStreamReader(CFile *in)
{
	init();
	this->in = in;
}
MarcStreamReader::MarcStreamReader(CFile *in, char * encoding)
{
	init();
	this->in = in;
	this->encoding = encoding;
}

void MarcStreamReader::init()
{
	record = new MarcRecord();
	//ldr = new Leader();
	directoryEntries = new ATTValVector <DirectoryEntry*>();
}

MarcStreamReader::~MarcStreamReader() {
  if (record)
    delete record;
//  if (ldr)
//    delete ldr;
  if (directoryEntries)
    delete directoryEntries;
}

/**
 * Returns true if the iteration has more records, false otherwise.
 */
bool MarcStreamReader::hasNext()
{
  if (in->Eof())
    return false;
  return true;
}



MarcRecord* MarcStreamReader::next()
{
  record->clear();
//  ldr->clear();
  unsigned char buf[24+1];
  memset (buf, 0, sizeof(buf));
  //buf[24]=0; // End of string
  char chr;
  DataField* dataField;
  bool retb;
//  int bytesRead = 0;
//  record = factory.newRecord();

//  try {

//    byte[] byteArray = new byte[24];
//    bytesRead = input.read(byteArray);
    retb = in->Read(buf, sizeof(buf)-1);

//    if (bytesRead == -1)
//      throw new MarcException("no data to read");
    if (!retb)
    {
      printf ("\nno data to read");
      return 0;
    }

//    while (bytesRead != -1 && bytesRead != byteArray.length)
//      bytesRead += input.read(byteArray, bytesRead, byteArray.length - bytesRead);

//    try {
//      ldr = parseLeader(byteArray);
//    } catch (IOException e) {
//      throw new MarcException("error parsing leader with data: " + new String(byteArray), e);
//    }
    parseLeader(buf);


//    switch (record->getLeader()->getCharCodingScheme_typeOfEntity()) {
//    case '#':
//      if (encoding.IsEmpty())
//        encoding = "ISO8859_1";
//      break;
//    case 'a':
//      if (encoding.IsEmpty())
//        encoding = "UTF8";
//      break;
//    default:
//        encoding = "UTF8";
//      break;
//    }

    encoding = "UTF8";


 //   record->setLeader(ldr);

    int directoryLength = (record->getLeader()->getBaseAddressOfData() - (24 + 1));
    if ((directoryLength % 12) != 0)
    {
      //        System.out.println("invalid directory length: " + directoryLength);
      //throw new MarcException("invalid directory length: " + directoryLength);
      printf("\ninvalid directory length: %d", directoryLength);
    }
    int size = directoryLength / 12;

//    String[] tags = new String[size];
//    int[] lengths = new int[size];
//    byte[] tag = new byte[3];
//    byte[] length = new byte[4];
//    byte[] start = new byte[5];

    directoryEntries->DeleteAndClear();
    unsigned char tag[3+1], length4[4+1], start5[5+1];
    tag[3] = 0; // End of String
    length4[4]=0; // End of String
    start5[5]=0; // End of String

//    String tmp;
    char fieldTerminator;

    // Leggi la directory (tag/length/start)
    for (int i = 0; i < size; i++) {
      // Read tag
//      bytesRead = input.read(tag);
//      while (bytesRead != -1 && bytesRead != tag.length)
//        bytesRead += input.read(tag, bytesRead, tag.length - bytesRead);
//      tmp = new String(tag);
//      tags[i] = tmp;
      DirectoryEntry *de = new DirectoryEntry();
      in->Read(tag, 3);
      de->tag = (char*)tag;

      // Read record length
//      bytesRead = input.read(length);
//      while (bytesRead != -1 && bytesRead != length.length)
//        bytesRead += input.read(length, bytesRead, length.length - bytesRead);
//      tmp = new String(length);
//      lengths[i] = Integer.parseInt(tmp);
      in->Read(length4, 4);
      de->length = atoi((char*)length4);


      // Read record start (sembra non essere usato?)
//      bytesRead = input.read(start);
//      while (bytesRead != -1 && bytesRead != start.length)
//        bytesRead += input.read(start, bytesRead, start.length - bytesRead);
      in->Read(start5, 5);
      de->start = atoi((char*)start5);

      directoryEntries->Add(de);
    } // Fine della lettura della directory

    // Leggi il FIELD_TERMINATOR
//    if (input.read() != Constants.FT)
//      throw new MarcException("expected field terminator at end of directory");

    in->Read(fieldTerminator);
    if (fieldTerminator != (char)FIELD_TERMINATOR)
      {
      printf ("\nexpected field terminator at end of directory");
      return 0;
      }

    // Leggiamo i vari record
    DirectoryEntry *de;

    for (int i = 0; i < size; i++) {
      de = directoryEntries->Entry(i);

      // E' un CONTROL FIELD?
      if (isControlField(de->tag.Data())) {
        // Leggi il campo
//        byteArray = new byte[lengths[i] - 1];
//        bytesRead = input.read(byteArray);
//        while (bytesRead != -1 && bytesRead != byteArray.length)
//          bytesRead += input.read(byteArray, bytesRead, byteArray.length - bytesRead);

        retb = in->Read(readBuffer, de->length-1);
        if (!retb)
        {
        printf ("\ncould not read control field");
        return 0;
        }

        readBuffer[de->length-1] = 0; // EOS

//        if (input.read() != Constants.FT) // fine campo
//          throw new MarcException("expected field terminator at end of field");

        in->Read(fieldTerminator);
        if (fieldTerminator != FIELD_TERMINATOR)
          {
          printf ("\nexpected field terminator at end of directory");
          return 0;
          }


        // Instanzia un control field
//        ControlField field = factory.newControlField();
//        field.setTag(tags[i]);
//        field.setData(getDataAsString(byteArray));
        ControlField *cf = new ControlField();
        cf->setTag(de->tag.data());
//        cf->setData((char*)readBuffer);
        cf->setData((char*)readBuffer, de->length-1);




        // Aggiungi control field a record
//        record.addVariableField(field);
        record->addControlField(cf);

      } else
      { // DATA FIELD
        // Leggi il campo
//        byteArray = new byte[lengths[i]];
//        bytesRead = input.read(byteArray);
//        while (bytesRead != -1 && bytesRead != byteArray.length)
//          bytesRead += input.read(byteArray, bytesRead, byteArray.length - bytesRead);

        in->Read(readBuffer, de->length);
        readBuffer[de->length] = 0; // EOS


//        try {
//          record.addVariableField(parseDataField(tags[i],byteArray));
//        } catch (IOException e) {
//          throw new MarcException(
//              "error parsing data field for tag: " + tags[i]);
////                    + " with data: "
////                    + new String(byteArray), e);
//        }
        dataField = parseDataField(de->tag.data());
        record->addDataField(dataField);
      }
    }

//    if (input.read() != Constants.RT)
//      throw new MarcException("expected record terminator");

    in->Read(chr);
    if (chr != RECORD_TERMINATOR)
    {
      printf ("expected record terminator");
      return 0;
    }

    // Read the CR/LF (non Standard Unimarc)
//byte[] CrLf = new byte[2];
//bytesRead = input.read(CrLf);


//  } catch (IOException e) {
//    throw new MarcException("\nan error occured reading input", e);
//  }


  return record;

} // End next


void MarcStreamReader::parseLeader(unsigned char* leaderData){
  char buf2[2+1], buf3[3+1], buf4[4+1], buf5[5+1];
  buf2[2]=0; // End of String
  buf3[3]=0; // End of String
  buf4[4]=0; // End of String
  buf5[5]=0; // End of String


    memcpy (buf5, leaderData, 5);
    record->getLeader()->setRecordLength(atoi (buf5));    // primi 5 caratteri

    record->getLeader()->setRecordStatus(*(leaderData+5)); // 6sto carattere
    record->getLeader()->setTypeOfRecord(*(leaderData+6)); // 7mo carattere


//    memcpy (buf2, leaderData+7, 2); // ottavo e nono
//    record->getLeader()->setImplDefined1(buf2);
    record->getLeader()->setLivelloBibliografico(*(leaderData+7));
    record->getLeader()->setLivelloGerarchico(*(leaderData+8));

//    ldr.setCharCodingScheme((char) isr.read());
    record->getLeader()->setPos9Undefined(*(leaderData+9)); // CharCodingScheme_typeOfEntity(*(leaderData+9)); // decimo


//    try {
//      ldr.setIndicatorCount(Integer.parseInt(String.valueOf((char) isr.read())));
//    } catch (NumberFormatException e) {
//      throw new MarcException("unable to parse indicator count", e);
//    }

    record->getLeader()->setIndicatorCount(*(leaderData+10)-0x30); // undicesimo


//    try {
//      ldr.setSubfieldCodeLength(Integer.parseInt(String.valueOf((char) isr.read())));
//    } catch (NumberFormatException e) {
//      throw new MarcException("unable to parse subfield code length", e);
//    }

    record->getLeader()->setSubfieldCodeLength(*(leaderData+11)-0x30); // dodicesimo


//    tmp = new char[5];
//    isr.read(tmp);
//    try {
//      ldr.setBaseAddressOfData(Integer.parseInt(new String(tmp)));
//    } catch (NumberFormatException e) {
//      throw new MarcException("unable to parse base address of data", e);
//    }

    memcpy (buf5, leaderData+12, 5);
    record->getLeader()->setBaseAddressOfData(atoi(buf5));


//    tmp = new char[3];
//    isr.read(tmp);
//    ldr.setImplDefined2(tmp);

//    memcpy (buf3, leaderData+17, 3);
//    record->getLeader()->setImplDefined2(buf3);

    record->getLeader()->setLivelloDiCodifica(*(leaderData+17));
    record->getLeader()->setTipoDiCatalogazioneDescrittiva(*(leaderData+18));


//    tmp = new char[4];
//    isr.read(tmp);
//    ldr.setEntryMap(tmp);

//    memcpy (buf5, leaderData+19, 5);
//    record->getLeader()->setEntryMap(buf5);

//    isr.close();
//    return ldr;
  } // End parseLeader







/**
* Returns true if the given <code>String</code> value identifies a tag for
* a control field (001 through 009).
*/
bool MarcStreamReader::isControlField(char* tag) { // throws NumberFormatException
  int i = -1;
  i = atoi(tag);

  if (atoi(tag) == -1)
     return false;

  if (i >- 1 && i < 10)
      return true;

  return false;
  }

/**
* Returns true if the given <code>String</code> value identifies a tag for
* a control number field (001).
*/
bool MarcStreamReader::isControlNumberField(char* tag){ // throws NumberFormatException
  int i = -1;
  i = atoi(tag);

    if (i == 1)
      return true;
    return false;
  }

/**
* Returns true if the given <code>Collection</code> contains an instance of
* a <code>ControlField</code> with a control number field tag (001).
*
* @param col
*          the collection of <code>ControlField</code> objects.
*/
//bool hasControlNumberField(Collection col) {
//    Iterator i = col.iterator();
//    while (i.hasNext()) {
//      ControlField field = (ControlField) i.next();
//      String tag = field.getTag();
//      if (isControlNumberField(tag))
//        return true;
//    }
//    return false;
//  }

DataField *MarcStreamReader::parseDataField(char *tag)// throws IOException
    {
//    ByteArrayInputStream bais = new ByteArrayInputStream(field);
//    char ind1 = (char) bais.read();
//    char ind2 = (char) bais.read();
//    DataField dataField = factory.newDataField();
//    dataField.setTag(tag);
//    dataField.setIndicator1(ind1);
//    dataField.setIndicator2(ind2);
    int i=0;
    DataField *dataField = new DataField();
    dataField->setTag(tag);
    dataField->setIndicator1(readBuffer[i++]);
    dataField->setIndicator2(readBuffer[i++]);
    int size;

//    int code;
//    int size;
//    int readByte;
//    byte[] data;
//    Subfield subfield;
    Subfield *subfield;

    char chr, code;
    while (true) {
//      readByte = bais.read();

      chr = readBuffer[i++];

//      if (readByte < 0)
//        break;
      if (chr < 0)
        break;

//      switch (readByte) {
      switch (chr) {
      case SUBFIELD_DELIMITER: // Constants.US
//        code = bais.read();
        code = readBuffer[i++];
        if (code < 0)
        {
          //throw new IOException("unexpected end of data field");
          printf ("\nunexpected end of data field");
          delete dataField;
          return 0;
        }
//        if (code == Constants.FT)
        if (code == FIELD_TERMINATOR)
          break;

      size = getSubfield(i);
        i += size;

//        data = new byte[size];
//        bais.read(data);
//        subfield = factory.newSubfield();
        subfield = new Subfield(code, (char*)subfieldBuffer, strlen((char *)subfieldBuffer));

//        subfield.setCode((char) code);
//        subfield.setData(getDataAsString(data));
//        subfield->setCode();
 //       subfield->setData();

//        dataField.addSubfield(subfield);
        dataField->addSubfield(subfield);
        break;
      case FIELD_TERMINATOR: // Constants.FT
    	  return dataField;
//        break;
      }
    }

    return dataField;
  }



int MarcStreamReader::getSubfield(int pos)
{

  int bytesRead = 0;

  while (bytesRead < MAX_SUBFIELD_LENGTH) {
    if (readBuffer[pos] == SUBFIELD_DELIMITER ||  // Constants.US
      readBuffer[pos] ==  FIELD_TERMINATOR) // Constants.FT
    {
      subfieldBuffer[bytesRead] = 0; // EOS
      return bytesRead;
    }
    else
    {
      subfieldBuffer[bytesRead++] = readBuffer[pos++];
    }

    } // End while
  printf ("subfield not terminated");

  return-1;
  }






