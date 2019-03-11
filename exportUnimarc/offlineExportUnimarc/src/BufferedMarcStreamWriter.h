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
 * BufferedMarcStreamWriter.h
 *
 *  Created on: 27-set-2010
 *      Author: Arge
 */

#ifndef BUFFEREDMARCSTREAMWRITER_H_
#define BUFFEREDMARCSTREAMWRITER_H_

#include "MarcStreamWriter.h"


class BufferedMarcStreamWriter: public MarcStreamWriter {

	CBufferedData *dataWarehouse; // Warehouse to hold tot records before writing to disk
	bool writeBuffer();
	void init(long aSize);
	void init(long bufSize,  long resizeBy);
	void stop();

	//long byteScritti;
	long writesCounter;

public:
	BufferedMarcStreamWriter(long bufSize);
	BufferedMarcStreamWriter(long bufSize, long resizeBy);
	BufferedMarcStreamWriter(CFile * out, long bufSize);
	BufferedMarcStreamWriter(CFile * out, long bufSize, long resizeBy);
	BufferedMarcStreamWriter(CFile *out, char* encoding, long bufSize);

	virtual ~BufferedMarcStreamWriter();
	bool write(MarcRecord* marcRecord);
	long getWritesCounter();

};

#endif /* BUFFEREDMARCSTREAMWRITER_H_ */
