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

#ifndef __CDATE_H_
#define __CDATE_H_

/*
 * Declarations for CDate Class
 *
 * $Header: /var/cvs/offlineExportUnimarc/include/library/CDate.h,v 1.2 2015/04/28 13:52:45 argentino Exp $
 *
 ****************************************************************************
 *
 * Rogue Wave Software, Inc.
 * P.O. Box 2328
 * Corvallis, OR 97339
 * Voice: (503) 754-3010	FAX: (503) 757-6650
 *
 * Copyright (C) 1989, 1990, 1991. This software is subject to copyright
 * protection under the laws of the United States and other countries.
 *
 ***************************************************************************
 *
 * An CDate can be tested for validity using the member function isValid().
 *
 ***************************************************************************
 *
 * $Log: CDate.h,v $
 * Revision 1.2  2015/04/28 13:52:45  argentino
 * version 6.4.8
 *
 * Revision 1.1  2014/11/12 09:01:18  argentino
 * Spostato dalla 67 perche' non mi faceva committare
 *
 * Revision 1.2  2013/11/14 09:42:42  trombin
 * Cambio CVS
 *
 * Revision 1.1  2012/05/03 08:37:38  trombin
 * Versione 3.5.1
 *
 *
 *    Rev 1.8   07 Jun 1992 16:06:58   KEFFER
 * Introduced HAS_POSTFIX macro
*
 *    Rev 1.7   17 Mar 1992 11:59:16   KEFFER
 * Pre- and post-increment and decrement operators now have a return type.
 *
 *    Rev 1.6   04 Mar 1992 10:21:20   KEFFER
 * Instance manager used in multi-thread situation.
 *
 *    Rev 1.3   13 Nov 1991 11:10:24   keffer
 * Static variables maintained by a manager for multi-thread version
 *
 *    Rev 1.2   29 Oct 1991 13:57:32   keffer
 * Added postfix ++ and --.
 *
 *    Rev 1.1   28 Oct 1991 09:08:20   keffer
 * Changed inclusions to <rw/xxx.h>
 *
 */

//#include "rw/tooldefs.h"
//#include "rw/procinit.h"	/* Uses instance manager */
#include "ors/const.h"

class  CTime;
class  CString;

typedef unsigned	dayTy;
typedef unsigned	monthTy;
typedef unsigned	yearTy;
typedef unsigned long	julTy;
static  julTy jul1901 = 2415386L;	// Julian day for 1/1/1901





class  CDate {

public:
	enum			howToPrint {normal, terse, numbers, europeanNumbers, european};
private:
	julTy 		julnum;			// Julian Day Number (Not same as Julian date.)
	#ifndef RW_MULTI_THREAD
		/* If not compiling for an MT situation, then use static data--- */
		static howToPrint  	printOption;		// Printing with different formats
	#endif

private:
//  void 			parseFrom(istream&);	// Reading dates
	void mdy(monthTy&, dayTy&, yearTy&) ;
	CDate(julTy j)	{julnum = j; }
protected:
	static OrsBool 	assertWeekDayNumber(dayTy d) {return d>=1 && d<=7;}
	static OrsBool 	assertIndexOfMonth(monthTy m) {return m>=1 && m<=12;}
public:
	CDate(); // Construct a CDate with the current date
	/*	Construct a CDate with a given day of the year and a given year.
		The base date for this computation is Dec. 31 of the previous year.
		If year == 0, Construct a CDate with Jan. 1, 1901 as the "day zero".
        i.e., CDate(-1) = Dec. 31, 1900 and CDate(1) = Jan. 2, 1901. */
	CDate(dayTy day, yearTy year);
	CDate(dayTy, char* month, yearTy); //  Construct a CDate for the given day, month, and year.
	CDate(dayTy, monthTy, yearTy);
//  CDate(istream& s) 	{parseFrom(s);}  // Read date from stream.
	CDate		(CTime&);			  // Construct an CDate from an CTime
	CDate(char *aDate, char *aFormat = "dmy"); 
			// Construct a CDate from a formatted string [dd?]mm?yy[yy]
			// Format can be "dmy" or "mdy"


//	CString		asString() ;
	void		Equals (char* aDate, char *aFormat = "dmy");
	char *		GetStringDate(char *aFmt = "dmy", int YearDigits = 2, char aSep = '/');


	OrsBool		between( CDate& d1,  CDate& d2) { return julnum >= d1.julnum && julnum <= d2.julnum; }
	int 		compareTo( CDate*) ;
	dayTy    	day() ;		// 1-365
	dayTy 		dayOfMonth() ;	// 1-31
	dayTy    	firstDayOfMonth()  {return firstDayOfMonth(month());}
	dayTy    	firstDayOfMonth(monthTy) ;
	unsigned	hash() ;
	OrsBool		isValid()  { return julnum>0; }
	OrsBool 	leap() 	 { return leapYear(year());}  // leap year?
	CDate		max( CDate& dt) ;
	CDate		min( CDate& dt) ;
	monthTy	   	month() ;
	char* 		nameOfDay()    {return dayName(weekDay());}
	char* 		nameOfMonth()  {return monthName(month());}
	CDate  		previous( char* dayName) ;	// Return date of previous dayName
	CDate  		previous(dayTy) ; 			// Same as above, but use day of week
	dayTy    	weekDay() ;
	yearTy    	year() ;

	// Date comparisons:
	OrsBool	operator<( CDate& date)  { return julnum < date.julnum; }
	OrsBool operator<=( CDate& date) { return julnum <= date.julnum; }
	OrsBool operator>( CDate& date)  { return julnum > date.julnum; }
	OrsBool operator>=( CDate& date) { return julnum >= date.julnum; }
	OrsBool operator==( CDate& date) { return julnum == date.julnum; }
	OrsBool	operator!=( CDate& date) { return julnum != date.julnum; }

	// Arithmetic operators:
	julTy		 operator-( CDate& dt) { return julnum - dt.julnum; }
	friend CDate operator+( CDate& dt, int dd) { return CDate(dt.julnum + dd); }
	friend CDate operator+(int dd,  CDate& dt) { return CDate(dt.julnum + dd); }
	friend CDate operator-( CDate& dt, int dd) { return CDate(dt.julnum - dd); }
	CDate		 operator++()			{ return CDate(++julnum); }
	CDate		 operator--()			{ return CDate(--julnum); }
/*
	#ifdef HAS_POSTFIX
	CDate		 operator++(int)			{ return CDate(julnum++); }
	CDate		 operator--(int)			{ return CDate(julnum--); }
	#endif
*/
	CDate 		 operator+=(int dd)		{ return CDate(julnum += dd); }
	CDate 		 operator-=(int dd)		{ return CDate(julnum -= dd); }



/**
  // Read or write dates:
  friend 		ostream& operator<<(ostream&,  CDate&);
  friend 		istream& operator>>(istream& s, CDate& d)
    				{d.parseFrom(s); return s;}
  unsigned		binaryStoreSize()  {return sizeof(julnum);}
  void			restoreFrom(RWvistream& s);
  void	  		restoreFrom(RWFile&);
  void			saveOn(RWvostream& s) ;
  void			saveOn(RWFile&) ;
***/
	// Static member functions:
	static  char* 	dayName(dayTy weekDayNumber);
	static  dayTy 	dayOfWeek( char* dayName);
	static  OrsBool dayWithinMonth(monthTy, dayTy, yearTy);
	static  dayTy   daysInYear(yearTy);
	static  monthTy indexOfMonth( char* monthName);
	static  julTy	jday(monthTy, dayTy, yearTy);
	static  OrsBool	leapYear(yearTy year);
	static  char* 	monthName(monthTy monthNumber);
	static  howToPrint	setPrintOption(howToPrint h);
	#ifdef RW_MULTI_THREAD
		// Just declarations --- static data must be retrieved from the instance manager.
		static  howToPrint	getPrintOption();
	#else
		static  howToPrint	getPrintOption()	{return printOption;}
	#endif

	char StringDate[20];
	}; // End CDate

#endif	 /* __CDATE_H_ */
