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
package it.iccu.sbn.servizi.z3950.jzkit;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.servizi.z3950.Z3950Client;
import it.iccu.sbn.web.vo.SbnErrorTypes;
import it.iccu.sbn.web2.util.Constants;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jzkit.a2j.codec.util.OIDRegister;
import org.jzkit.a2j.gen.AsnUseful.EXTERNAL_type;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.Record;

import com.k_int.IR.InvalidQueryException;
import com.k_int.IR.SearchException;
import com.k_int.gen.Z39_50_APDU_1995.DefaultDiagFormat_type;
import com.k_int.gen.Z39_50_APDU_1995.InitializeResponse_type;
import com.k_int.gen.Z39_50_APDU_1995.NamePlusRecord_type;
import com.k_int.gen.Z39_50_APDU_1995.PresentResponse_type;
import com.k_int.gen.Z39_50_APDU_1995.Records_type;
import com.k_int.gen.Z39_50_APDU_1995.SearchResponse_type;
import com.k_int.gen.Z39_50_APDU_1995.record_inline13_type;
import com.k_int.z3950.client.SynchronousOriginBean;

/**
 * ZClient : A Simple Z3950 command line client to test the toolkit
 *
 * @version: $Id$
 * @author: Ian Ibbotson (ian.ibbotson@k-int.com)
 *
 */
public class JZKitClient extends SynchronousOriginBean implements Z3950Client {

	private Logger log = Logger.getLogger(JZKitClient.class);

	public static final char ISO2709_RS = 035;
	public static final char ISO2709_FS = 036;
	public static final char ISO2709_IDFS = 037;
	private static final String PREFIX_QUERY_TYPE = "PREFIX";
	// private static final String CCL_QUERY_TYPE = "CCL";

	boolean verbose = false;

	private int auth_type = 0; // 0=none, 1=anonymous, 2=open, 3=idpass
	private String principal = null;
	private String group = null;
	private String credentials = null;
	private String querytype = PREFIX_QUERY_TYPE;
	private String current_result_set_name = null;
	private int result_set_count = 0;
	private String element_set_name = null; // Default to a null (no) element
											// set name
	private OIDRegister reg = OIDRegister.getRegister();

	private int maxrecs;

	// private com.k_int.z3950.IRClient.Z3950Origin orig = null;

	public List<Record> search(String zurl, String database,
			String syntax, String query, int maxrecs) throws Exception {

		log.debug("z-url:   " + zurl);
		log.debug("z-db:    " + database);
		log.debug("syntax:  " + syntax);
		log.debug("query:   " + query);
		log.debug("maxrecs: " + maxrecs);

		this.maxrecs = maxrecs;

		String host;
		String port = Integer.toString(Constants.Z3950_DEFAULT_PORT);
		if (ValidazioneDati.isFilled(zurl)) {
			String[] tokens = zurl.split("\\:");
			if (ValidazioneDati.size(tokens) == 1)
				host = zurl;
			else {
				host = tokens[0];
				port = tokens[1];
			}
		} else
			throw new ApplicationException(SbnErrorTypes.SRV_ILL_ERRORE_CONNESSIONE_OPAC);

		List<Record> output = new ArrayList<Record>();

		try {
			openConnection(host, port);
			cmdBase(database);
			cmdElements("F");
			cmdFormat(syntax);
			verbose = true;

			Iterator<Record> i = attrset_bib1_find(query);
			while (i.hasNext()) {
				output.add(i.next());
			}

		} catch (ApplicationException e) {
			throw e;

		} catch (Exception e) {
			log.error("", e);
			throw new ApplicationException(SbnErrorTypes.SRV_ILL_ERRORE_CONNESSIONE_OPAC, e);

		} finally {
			this.disconnect();
		}

		return output;

	}

	// Commands

	public boolean openConnection(String hostname, String portnum) {
		boolean respVal = false;

		try {
			if (verbose)
				log.debug("Attempting connection to " + hostname + " : "
						+ portnum);
			InitializeResponse_type resp = connect(hostname, portnum,
					auth_type, principal, group, credentials);

			if (verbose)
				log.debug("Received response from connect");
			respVal = resp.result.booleanValue();
			if (respVal != true) {
				if (verbose)
					log.error("  Failed to establish association");
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return (respVal);
	}

	public boolean openConnection(String hostname, String portnum,
			String userID, String password) {
		boolean respVal = false;

		try {
			if (verbose)
				log.debug("Attempting connection to " + hostname + " : "
						+ portnum);
			InitializeResponse_type resp = connect(hostname, portnum,
					((userID == null) ? 0 : 3), userID, group, password);

			if (verbose)
				log.debug("Received response from connect");
			respVal = resp.result.booleanValue();
			if (respVal != true) {
				if (verbose)
					log.error("  Failed to establish association");
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return (respVal);
	}

	public int cmdFind(String args, String resultSetName) {
		SearchResponse_type resp = null;
		int numResults = 0;
		current_result_set_name = resultSetName;

		if (verbose)
			log.debug("Calling find, query= " + args);
		try {
			if (querytype.equalsIgnoreCase("CCL")) {
				resp = sendSearch(new com.k_int.IR.QueryModels.CCLString(args),
						null, current_result_set_name, element_set_name);
			} else {
				resp = sendSearch(new com.k_int.IR.QueryModels.PrefixString(
						args), null, current_result_set_name, element_set_name);
			}
		} catch (SearchException se) {
			cat.warn("Problem processing query", se);
			if (verbose)
				log.error(se.toString());

			// If possible, the search response information will be passed along
			// with
			// the exception
			if (se.additional != null) {
				resp = (SearchResponse_type) (se.additional);
			}
		} catch (InvalidQueryException iqe) {
			cat.warn("Problem parsing query", iqe);
			if (verbose)
				log.error(iqe);
		} catch (Exception e) {
			cat.warn("Problem processing query", e);
			if (verbose)
				log.error(e.toString());
		}

		if (resp != null) {
			numResults = resp.resultCount.intValue();
			if (verbose)
				log.debug("NumResults = " + numResults);

			if ((resp.records != null)
					&& (resp.numberOfRecordsReturned.intValue() > 0)) {
				if (verbose)
					log.debug("  Search has piggyback records");
			}
		}
		return (numResults);
	}

	public int cmdFind(String args) {
		String curResSetName = "RS" + (result_set_count++);
		return (cmdFind(args, curResSetName));
	}

	private Iterator<Record> attrset_bib1_find(String query) throws Exception {
		int numFound = cmdFind("@attrset bib-1 " + query, "default");
		if (maxrecs < numFound)
			throw new ApplicationException(SbnErrorTypes.ERROR_DB_MAX_ROWS_EXCEEDED, Integer.toString(numFound) );

		Z3950RecordIterator iter = new Z3950RecordIterator(this, "default",	numFound, "u" + query);
		return iter;
	}

	public Record getRecord(int startAt) {
		return (getRecord(startAt, current_result_set_name));
	}

	public Record getRecord(int startAt, String resultSetName) {
		// Format for present command is n [ + n ]
		// log.error("Present "+args);
		String setname = resultSetName;

		try {
			PresentResponse_type resp = sendPresent(startAt, 1,
					element_set_name, setname);

			// log.error("\n  Present Response");

			if (resp.referenceId != null) {
				// log.error("  Reference ID : " + new
				// String(resp.referenceId));
			}

			// log.error("  Number of records : " +
			// resp.numberOfRecordsReturned);
			// log.error("  Next RS Position : " +
			// resp.nextResultSetPosition);
			// log.error("  Present Status : " + resp.presentStatus);
			// log.error("");

			Records_type r = resp.records;

			if (r.which == Records_type.responserecords_CID) {
				List<?> v = (List<?>) (r.o);
				//int num_records = v.size();
				// log.error("Response contains " + num_records +
				// " Response Records");
				Iterator<?> i = v.iterator();
				while (i.hasNext()) {
					NamePlusRecord_type npr = (NamePlusRecord_type) (i.next());

					if (null != npr) {
						// log.error("[" + npr.name + "] ");

						if (npr.record.which == record_inline13_type.retrievalrecord_CID) {
							// RetrievalRecord is an external
							EXTERNAL_type et = (EXTERNAL_type) npr.record.o;
							// log.error(" Direct
							// Reference="+et.direct_reference+"] ");
							// dumpOID(et.direct_reference);
							// Just rely on a toString method for now
							if (et.direct_reference.length == 6) {
								if (et.direct_reference[(et.direct_reference.length) - 1] == 1) // USMarc
								{
									byte[] raw = (byte[]) et.encoding.o;
									InputStream in = new ByteArrayInputStream(raw);
									MarcReader mr = new MarcStreamReader(in, "UTF-8");
									Record marcRec = mr.next();
								/*
									if (verbose)
										log.debug("record: " + marcRec.toString());
									iso2709 rec = new iso2709(raw);
									Record marcRec = rec.getRecord();
								*/
									return marcRec;
								}
							}
						}
					}
				}

			}
		} catch (Exception e) {
			log.error("", e);
			if (verbose)
				log.error("Exception processing show command " + e);
		}
		return (null);
	}

	public byte[] getBytesByIDNum(int idnum) {
		log.debug("Calling getBytesByIDNum id=" + idnum);
		cmdFind("@attrset bib-1 @attr 1=1016 \"^C" + idnum + "\"");
		log.debug("Calling getBytes id=" + idnum);
		byte[] rec = getBytes(1);
		log.debug("bytes count=" + rec.length);
		return (rec);
	}

	public byte[] getBytes(int startAt) {
		// Format for present command is n [ + n ]
		// log.error("Present "+args);
		String setname = current_result_set_name;

		try {
			PresentResponse_type resp = sendPresent(startAt, 1,
					element_set_name, setname);

			// log.error("\n  Present Response");

			if (resp.referenceId != null) {
				// log.error("  Reference ID : " + new
				// String(resp.referenceId));
			}

			// log.error("  Number of records : " +
			// resp.numberOfRecordsReturned);
			// log.error("  Next RS Position : " +
			// resp.nextResultSetPosition);
			// log.error("  Present Status : " + resp.presentStatus);
			// log.error("");

			Records_type r = resp.records;

			if (r.which == Records_type.responserecords_CID) {
				List<?> v = (List<?>) (r.o);
				//int num_records = v.size();
				Iterator<?> i = v.iterator();
				while (i.hasNext()) {
					NamePlusRecord_type npr = (NamePlusRecord_type) (i.next());

					if (null != npr) {
						// log.error("[" + npr.name + "] ");

						if (npr.record.which == record_inline13_type.retrievalrecord_CID) {
							// RetrievalRecord is an external
							EXTERNAL_type et = (EXTERNAL_type) npr.record.o;
							// log.error(" Direct
							// Reference="+et.direct_reference+"] ");
							// dumpOID(et.direct_reference);
							// Just rely on a toString method for now
							if (et.direct_reference.length == 6) {
								if (et.direct_reference[(et.direct_reference.length) - 1] == 10) // USMarc
								{
									return (((byte[]) et.encoding.o));
								}
							}
						}
					}
				}

			}
		} catch (Exception e) {
			log.error("", e);
			log.error("Exception processing show command " + e);
		}
		return (null);
	}

	public void cmdBase(String args) {
		clearAllDatabases();

		try {
			java.util.StringTokenizer st = new java.util.StringTokenizer(args,
					" ");

			clearAllDatabases();

			while (st.hasMoreTokens()) {
				addDatabase(st.nextToken());
			}

			// log.error("dbnames:"+db_names);
		} catch (Exception e) {
			log.error("Exception processing base command " + e);
		}
	}

	//
	// Change the element set name in use (Normally "F" or "B")
	//
	public void cmdElements(String args) {
		// Right now, args is prefixed with a space... So junk it.
		try {
			java.util.StringTokenizer st = new java.util.StringTokenizer(args,
					" ");
			if (st.hasMoreTokens()) {
				setElementSetName(st.nextToken());
			} else {
				setElementSetName(null);
			}
		} catch (Exception e) {
			log.error("Exception processing base command " + e);
		}
	}

	//
	// Change the format
	//
	public void cmdFormat(String args) {
		// Right now, args is prefixed with a space... So junk it.
		try {
			java.util.StringTokenizer st = new java.util.StringTokenizer(args,
					" ");
			if (st.hasMoreTokens()) {
				String requested_syntax = st.nextToken();
				if (reg.oidByName(requested_syntax) != null) {
					setRecordSyntax(requested_syntax);
				} else {
					log.error("Unknown Record Syntax");
				}
			}
		} catch (Exception e) {
			log.error("Exception processing format command " + e);
		}
	}

	// //
	// // Change the element set name in use (Normally "F" or "B")
	// //
	// public void cmdAuth(String args)
	// {
	// try
	// {
	// java.util.StringTokenizer st = new java.util.StringTokenizer(args," ,");
	//
	// if ( st.hasMoreTokens() )
	// {
	// String type = st.nextToken();
	// if ( type.equals("anon") )
	// {
	// log.error("Will use anonymous authentication");
	// auth_type = 1;
	// }
	// else if ( type.equals("open") )
	// {
	// log.error("Will use open authentication");
	// if ( st.hasMoreTokens() )
	// {
	// auth_type = 2;
	// principal = st.nextToken();
	// log.error("Open auth string will be "+principal);
	// }
	// else
	// {
	// log.error("Asked for open authentication but no open string supplied, No auth will be used");
	// }
	// }
	// else if ( type.equals("idpass") )
	// {
	// log.error("Will use idpass authentication");
	// auth_type = 3;
	// if ( st.hasMoreTokens() )
	// principal = st.nextToken();
	// if ( st.hasMoreTokens() )
	// group = st.nextToken();
	// if ( st.hasMoreTokens() )
	// credentials = st.nextToken();
	// }
	// else
	// {
	// log.error("Unrecognised auth type, no authentication will be used");
	// auth_type = 0;
	// }
	// }
	// else
	// {
	// log.error("No auth type, no authentication will be used");
	// }
	// }
	// catch ( Exception e )
	// {
	// log.error("Exception processing base command "+e);
	// }
	// }

	// public void cmdQueryType(String args)
	// {
	// // Right now, args is prefixed with a space... So junk it.
	// try
	// {
	// java.util.StringTokenizer st = new java.util.StringTokenizer(args," ");
	// if ( st.hasMoreTokens() )
	// {
	// String type = st.nextToken();
	// log.error("Set query type to "+type);
	//
	// if ( type.equals("CCL") )
	// querytype=CCL_QUERY_TYPE;
	// else
	// querytype=PREFIX_QUERY_TYPE;
	// }
	// }
	// catch ( Exception e )
	// {
	// log.error("Exception processing base command "+e);
	// }
	// }

	// public void dumpOID(int[] oid)
	// {
	// System.err.print("{");
	// for ( int i = 0; i < oid.length; i++ )
	// {
	// System.err.print(oid[i]+" ");
	// }
	// log.error("}");
	// }

	public void displayRecords(Records_type r) {
		if (r != null) {
			switch (r.which) {
			case Records_type.responserecords_CID:
				List<?> v = (List<?>) (r.o);
				int num_records = v.size();
				log.error("Response contains " + num_records
						+ " Response Records");
				Iterator<?> i = v.iterator();
				while (i.hasNext()) {
					NamePlusRecord_type npr = (NamePlusRecord_type) (i.next());

					if (null != npr) {
						System.err.print("[" + npr.name + "] ");

						switch (npr.record.which) {
						case record_inline13_type.retrievalrecord_CID:
							// RetrievalRecord is an external
							EXTERNAL_type et = (EXTERNAL_type) npr.record.o;
							// log.error("  Direct Reference="+et.direct_reference+"] ");
							// dumpOID(et.direct_reference);
							// Just rely on a toString method for now
							if (et.direct_reference.length == 6) {
								switch (et.direct_reference[(et.direct_reference.length) - 1]) {
								// case 1: // Unimarc
								// System.err.print("Unimarc ");
								// DisplayISO2709((byte[])et.encoding.o);
								// break;
								// case 3: // CCF
								// System.err.print("CCF ");
								// break;
								case 10: // US Marc
									// System.err.print("USMarc: ");
									DisplayISO2709((byte[]) et.encoding.o);
									// byte[] ba = (byte[])et.encoding.o;
									// log.error("Bytes:");
									// for ( int i=0; i<ba.length;i++ ) {
									// System.err.print(ba[i]);
									// }
									// log.error("");
									break;
								// case 11: // UK Marc
								// System.err.print("UkMarc ");
								// DisplayISO2709((byte[])et.encoding.o);
								// break;
								// case 12: // Normarc
								// System.err.print("Normarc ");
								// DisplayISO2709((byte[])et.encoding.o);
								// break;
								// case 13: // Librismarc
								// System.err.print("Librismarc ");
								// DisplayISO2709((byte[])et.encoding.o);
								// break;
								// case 14: // Danmarc
								// System.err.print("Danmarc ");
								// DisplayISO2709((byte[])et.encoding.o);
								// break;
								// case 15: // Finmarc
								// System.err.print("Finmarc ");
								// DisplayISO2709((byte[])et.encoding.o);
								// break;
								// case 100: // Explain
								// cat.debug("Explain record");
								// // Write display code....
								// break;
								// case 101: // SUTRS
								// System.err.print("SUTRS ");
								// log.error((String)et.encoding.o);
								// break;
								// case 102: // Opac
								// cat.debug("Opac record");
								// // Write display code....
								// break;
								// case 105: // GRS1
								// System.err.print("GRS1 ");
								// displayGRS((java.util.List)et.encoding.o);
								// break;
								default:
									System.err.print("Unknown.... ");
									System.err
											.println(et.encoding.o.toString());
									break;
								}
							} else if ((et.direct_reference.length == 7)
									&& (et.direct_reference[5] == 109)) {
								switch (et.direct_reference[6]) {
								case 3: // HTML
									System.err.print("HTML ");
									String html_rec = null;
									if (et.encoding.o instanceof byte[])
										html_rec = new String(
												(byte[]) et.encoding.o);
									else
										html_rec = et.encoding.o.toString();
									log.error(html_rec.toString());
									break;
								case 9: // SGML
									System.err.print("SGML ");
									System.err
											.println(et.encoding.o.toString());
									break;
								case 10: // XML
									System.err.print("XML ");
									log.error(new String(
											(byte[]) (et.encoding.o)));
									break;
								default:
									System.err
											.println(et.encoding.o.toString());
									break;
								}
							} else
								System.err
										.println("Unknown direct reference OID: "
												+ et.direct_reference);
							break;
						case record_inline13_type.surrogatediagnostic_CID:
							log.error("SurrogateDiagnostic");
							break;
						case record_inline13_type.startingfragment_CID:
							log.error("StartingFragment");
							break;
						case record_inline13_type.intermediatefragment_CID:
							log.error("IntermediateFragment");
							break;
						case record_inline13_type.finalfragment_CID:
							log.error("FinalFragment");
							break;
						default:
							System.err
									.println("Unknown Record type for NamePlusRecord");
							break;
						}
					} else {
						log.error("Error... record ptr is null");
					}
				}
				break;
			case Records_type.nonsurrogatediagnostic_CID:
				DefaultDiagFormat_type diag = (DefaultDiagFormat_type) r.o;
				log.error("    Non surrogate diagnostics : " + diag.condition);
				if (diag.addinfo != null) {
					// addinfo is VisibleString in v2, InternationalString in V3
					log.error("Additional Info: " + diag.addinfo.o.toString());
				}
				break;
			case Records_type.multiplenonsurdiagnostics_CID:
				log.error("    Multiple non surrogate diagnostics");
				break;
			default:
				log.error("    Unknown choice for records response : "
						+ r.which);
				break;
			}
		}

		// if ( null != e.getPDU().presentResponse.otherInfo )
		// log.error("  Has other information");
		log.error("");

	}

	private void DisplayISO2709(byte[] octets) {
		com.k_int.IR.Syntaxes.marc.iso2709 rec = new com.k_int.IR.Syntaxes.marc.iso2709(
				octets);
		log.error(rec);
	}

	public void displayGRS(List<?> v) {
		com.k_int.IR.Syntaxes.GRS1 grs_rec = new com.k_int.IR.Syntaxes.GRS1(
				"Repository", "Coll", "", v, null);
		log.error(grs_rec.toString());
	}

	public void setElementSetName(String element_set_name) {
		this.element_set_name = element_set_name;
	}

	public String getElementSetName() {
		return element_set_name;
	}
}
