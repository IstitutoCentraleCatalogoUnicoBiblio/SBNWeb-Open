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
/****************************************************************************
* Module  : LibraryClassId.h                                                       *
* Author  : Argentino Trombin                                               *
* Desc.   : Define the IDs for the classes in library                       *
* Date    :                                                                 *
****************************************************************************/

#ifndef __LIBRARY_CLASS_ID_H
#define __LIBRARY_CLASS_ID_H

// SGML classes that use the production number as ID


// SGML classes that DO NOT use the production number as ID
//typedef enum LibraryClassId_Enum
typedef enum
	{
	CBaseClassID = 	100,
	CBTreeInMemID,
	CBTreeInMemNodeID,
	CBufferedDataID,
	CBufferedDataIteratorID,
	CCollectionID,
    CCollectableDoublyLinkedListID,
    CCollectableDoublyLinkedListIteratorID,
	CCollectableElementID,
	CCollectableTextID,

	CDoublyLinkedListID,
	CDoublyLinkedListIteratorID,

	CFactoryID,
	CHashDictionaryID,
	CHashDictionaryIteratorID,
	CHashDictionaryIteratorKeyValueID,
	CHashDictionaryKeyValueID,
	CHashTableID,
	CHashTableIteratorID,
	CInputManagerID,
	CIntID,
	CLongID,
    CCollectableIntID,
    CCollectableLongID,
	CIteratorID,
	CKeyAndValueID,
	COrderedCollectionID,
	COrderedCollectionIteratorID,
	CLinkedListID,
	CSequenceableID,
	CSetID,
	CSetIteratorID,
	CSetIdentityID,
	CSinglyLinkedListID,
    CCollectableSinglyLinkedListID,
	CSinglyLinkedListIteratorID,
    CCollectableSinglyLinkedListIteratorID,
    CCollectableAssociationID,
    CCollectableID,
    CCollectableIdAssociationID,
	CStoreEntryID,
	CStoreTableID,
	CStringID,
    CCollectableStringID,
	CSteerAttributeID,
	CSteerAttributeListID,
	CTextID,
	CTokenizerID,
	ATTStackID,
	ATTValVectorID,
	CTPointerVectorID,
	CTValueVectorID

	//	};
	}LibraryClassId_Enum;




#endif // __LIBRARY_CLASS_ID_H

