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
package it.iccu.sbn.batch.servizi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.iccu.sbn.web2.util.Constants;




public class ImportaUtentiLettori
{
	private static final String ERROR_COD_AUT_DIVERSO = "error.cod.aut.diverso";

	private static String DATAAUT = null;

    static int recordCounter = 0; //record totali
    static int recordCounterErr = 0; // record con errori

    static String cf[];
    static String ar[];
	static List sqlExecutedOk = new ArrayList();
	static List sqlExecutedKo = new ArrayList();
	static List selectUtentiExecutedOk = new ArrayList();
	static List selectUtentiExecutedKo = new ArrayList();
	static List selectUtentiTrovati = new ArrayList();
	static List selectUtentiNonTrovati = new ArrayList();
	static List selectUtentiBibTrovati = new ArrayList();
	static List selectUtentiBibNonTrovati = new ArrayList();
	static List insertUtentiOk = new ArrayList();
	static List insertUtentiKo = new ArrayList();
	static List updateUtentiOk = new ArrayList();
	static List updateUtentiKo = new ArrayList();
	static List insertUtentiBibOk = new ArrayList();
	static List updateUtentiBibOk = new ArrayList();
	static List insertDirittiUtenteOk = new ArrayList();
	static List updateDirittiUtenteOk = new ArrayList();
	static List updateDirittiUtenteKo = new ArrayList();
	static List updateUtentiBibKo = new ArrayList();



	boolean setCurCfg=false;
	Connection con = null;
	static String codPolo="";
	static String charSepDiCampoInArray = "\\|";
    static String jdbcDriver="";
	static String connectionUrl = ""; // "jdbc:postgresql://localhost:5432/sbnwebArge"
	static String userName="";
	static String userPassword="";
	static String searchPath="";
	static String schema="";

    //String jdbcDriver="org.postgresql.Driver";
	//String connectionUrl = "jdbc:postgresql://193.206.221.14:5432/sbnwebCollaudo2"; // "jdbc:postgresql://localhost:5432/sbnwebArge"
	//String userName="sbnweb";
	//String userPassword="sbnweb";
	//String searchPath="sbnweb, pg_catalog, public;";
	//String schema="sbnweb";
	//BufferedWriter OutLog;
	private String sql;




	public void upload(String[] ar) throws UnsupportedEncodingException {

		boolean res = true;
		String idutenti = "";
		String sql = "";

		ResultSet rs = null;
		PreparedStatement prepStatement = null;


		// verifica esistenza su tbl_utenti di un record con un dato CODICE FISCALE
		// se non esiste si deve inserire, se esiste si deve aggiornare

		try {

			sql = "select * from tbl_utenti where cod_fiscale = '" + ar[20-1] + "'";

			String sqlUtf8 = null;
			try {
				sqlUtf8 = new String (sql.getBytes(), "UTF-8");
				prepStatement = con.prepareStatement(sqlUtf8.toString());

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    rs = prepStatement.executeQuery();
        	if (rs.next())
        	   	{
        			//System.out.println("\nTrovato in tbl_utenti record per "+ ar[20-1] + " (id=" +  rs.getString("id_utenti")
        			//     + ") - UPDATE");
        			selectUtentiTrovati.add(rs.getString("id_utenti")+" " + rs.getString("cod_utente")+" " + rs.getString("cod_fiscale"));
        			//necessario update
        			idutenti=rs.getString("id_utenti");
            		res = updateUtente(idutenti, ar);
        			if (res)
        			{
						//fl_canc='S' per tutti i legami pre-esistenti in trl_utenti_biblioteca e
        				//fl_canc='S' per tutti i record pre-esistenti in trl_diritti_utente
						if (!UpdateFlagUtentiBiblioteca(idutenti, ar))
							return;
						//procede al trattamento dei singoli legami
            			trattaUtenteBiblioteca(idutenti, ar, DATAAUT);
            			//updateUtenteBiblioteca(idutenti, ar, DATAAUT);
        			}
        		}
            else
            	{
            		//System.out.println("NON trovato nel db utente con codice fiscale= " + ar[20-1] + " - INSERT");
            		selectUtentiNonTrovati.add(ar[20-1]);
        			//necessario insert
        			idutenti = insertUtente(ar);
        			if (idutenti != null)
            			trattaUtenteBiblioteca(idutenti, ar, DATAAUT);
            			//insertUtenteBiblioteca(idutenti, ar, DATAAUT);
            	}


		} catch (SQLException e) {
			System.out.println("\n\tupload - Select controllo tbl_utenti errata. " + e.getMessage() + " ---> " + sql);
			System.out.println ("EXCEPTION: " +e.getMessage());
			sqlExecutedKo.add(sql);
			return;
		}



	} // End upload




	private void insertUtenteBiblioteca(String id, String[] ar2, String dt) {

		String[] biblio;
		biblio = ricercaBiblioteche(ar2[21-1], ar2[26-1], " "+ar2[1-1]);

		if (biblio.length > 0)    //array: bib|id_aut|cod_aut
		{
			String[] aut=null;
			for (int i=0; i < biblio.length; i++)
				{
					// inserimento in trl_utenti_biblioteca di un legame per
					// la biblioteca di inserimento in input + tutte le altre biblioteche trovate

					Statement stmt = null;
					try {
						stmt = con.createStatement();
					} catch (SQLException e) {

						e.printStackTrace();
					}

					aut = biblio[i].split("\\|");
							//aut[0] biblioteca
							//aut[1] id tipo autorizzazione
							//aut[2] cod tipo autorizzazione
					//if (!aut[1].equals("NULL") || (aut[1].equals("NULL") && i == 0))
							//inserisco comunque una relazione tra la bib di riferimento e l'utente anche senza aut
							//{

						try {
							sql = componiInsertUtentiBiblioteca(id, dt, ar2, aut);

							String sqlUtf8 = null;
							try {
								sqlUtf8 = new String (sql.getBytes(), "UTF-8");
								stmt.execute(sqlUtf8);
								insertUtentiBibOk.add(sql);
								//sqlExecutedOk.add(sql);
			        			//System.out.println(">>>>> Insert in trl_utenti_biblioteca per id="+id+" bib="+aut[0]+" aut="+aut[2]);
			        			try {
			        				stmt.execute("COMMIT;");
			        				//System.out.println("Committing insert trl_utenti_biblioteca. ="+id+" bib="+aut[0]+" aut="+aut[2]);
			         			} catch (SQLException e1) {
			        				// TODO Auto-generated catch block
			        				e1.printStackTrace();
			        			}

								// ricerca in trl_autorizzazioni_servizi tutti gli id dei servizi
			         			// legati ad un dato id_tipo_autorizzazione

			         			if (!aut[1].equals("NULL")) {
					        			String[] id_serv;
					        			id_serv = ricercaIdServizi(aut[1]);

					        			int j=0;
					        		    while (j< id_serv.length) {
					        				insertDirittiUtente(DATAAUT, id, id_serv[j], aut[2], ar2);
					        				j++;
					            	    }
					        			try {
					        				stmt.execute("COMMIT;");
					        				//System.out.println("Committing insert trl_diritti_utente per aut " + aut[2]);
					         			} catch (SQLException e1) {
					        				// TODO Auto-generated catch block
					        				e1.printStackTrace();
					        			}
			         			}
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}


						} catch (SQLException e) {
							System.out.println("\n\tinsertUtenteBiblioteca - Errore: " + e.getMessage() + " ---> " + sql);
							System.out.println ("EXCEPTION: " +e.getMessage());
							sqlExecutedKo.add(sql);
							try {
								stmt.execute("ROLLBACK;");
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					//}
			}//End for

		}
		else
		{
			System.out.println("\n\tNessuna biblioteca associata a ateneo " + ar2[21-1]);
			return;
		}


		// TODO Auto-generated method stub
		return;
	} //End insertUtenteBiblioteca



private String componiInsertUtentiBiblioteca(String id, String dt, String[] ar2, String[] aut) {

			/* STRUTTURA INSERT tabella trl_utenti_biblioteca
			  "id_utenti_biblioteca"  //sequence:trl_utenti_biblioteca_id_utenti_biblioteca_seq
			  "id_utenti" INTEGER NOT NULL,
			  "cd_polo" CHAR(3) NOT NULL,
			  "cd_biblioteca" CHAR(3) NOT NULL,
			  "id_specificita_titoli_studio" INTEGER,
			  "id_occupazioni" INTEGER,
			  "credito_bib" NUMERIC(12,3),
			  "note_bib" CHAR(50),
			  "data_inizio_aut" DATE,
			  "data_fine_aut" DATE,
			  "data_inizio_sosp" DATE,
			  "data_fine_sosp" DATE,
			  "cod_tipo_aut" CHAR(3),
			  "ute_ins" CHAR(12) NOT NULL,
			  "ts_ins" TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
			  "ute_var" CHAR(12) NOT NULL,
			  "ts_var" TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
			  "fl_canc" CHAR(1) NOT NULL,
			*/

			if (aut[2].equals("NULL"))
				{
					aut[2] = "";
				}

			String sql = "";
			sql = "insert into trl_utenti_biblioteca values(" +
				"nextval('trl_utenti_biblioteca_id_utenti_biblioteca_seq')," + 	//id_utenti_biblioteca
				"'" + id + "'," +  			//id_utenti
				"'" + codPolo + "'," +    //polo  ar2[33-1]
				"'" + aut[0] + "',"  + 		//biblioteca
				"NULL," +  //id_specificita_titoli_studio
				"NULL," +  //id_occupazioni
				"NULL," +  //credito_bib
				"NULL," +  //note_bib
				"to_date('" + dt + "', 'DD/mm/YYYY')," +  		//data_inizio_aut
				"to_date('" + ar2[31-1] + "', 'DDmmYYYY')," +  	//data_fine_aut
				"NULL," +  //data_inizio_sosp
				"NULL," +  //data_fine_sosp
				"'" + aut[2] + "'," +    //cod_tipo_aut
				"'" + codPolo + " " + ar2[1-1] + "000009'," + //ute_ins
				"now()," +  //ts_ins
				"'" + codPolo + " " + ar2[1-1] + "000009'," + //ute_var
				"now()," +  //ts_var
				"'N'" +  //fl_canc
				")";
		return sql;
	}




private void updateUtenteBiblioteca(String id, String[] ar2, String dt) {

		String[] biblio;
		biblio = ricercaBiblioteche(ar2[21-1], ar2[26-1], " "+ar2[1-1]);

		if (biblio.length > 0)    //array: bib|id_aut|cod_aut
		{
			String[] aut=null;
			for (int i=0; i < biblio.length; i++)
				{
					// aggiornamento/inserimento in trl_utenti_biblioteca di un legame per
					// la biblioteca di inserimento in input + tutte le altre biblioteche trovate

					Statement stmt = null;
					try {
						stmt = con.createStatement();
					} catch (SQLException e) {

						e.printStackTrace();
					}

					aut = biblio[i].split("\\|");
							//aut[0] biblioteca
							//aut[1] id tipo autorizzazione
							//aut[2] cod tipo autorizzazione
					//if (!aut[1].equals("NULL") || (aut[1].equals("NULL") && i == 0))
						//inserisco comunque una relazione tra la bib di riferimento e l'utente anche senza aut
						//{

						String id_ub= selectUtentiBiblioteca(id, aut);  //ritorna id_utenti_biblioteca se esiste
						if (id_ub == null)
						{
						try {
							sql = componiInsertUtentiBiblioteca(id, dt, ar2, aut);

							String sqlUtf8 = null;
							try {
								sqlUtf8 = new String (sql.getBytes(), "UTF-8");
								stmt.execute(sqlUtf8);
								insertUtentiBibOk.add(sql);
								//sqlExecutedOk.add(sql);
			        			//System.out.println(">>>>> Insert in trl_utenti_biblioteca per id="+id+" bib="+aut[0]+" aut="+aut[2]);
			        			try {
			        				stmt.execute("COMMIT;");
			        				//System.out.println("Committing insert trl_utenti_biblioteca. ="+id+" bib="+aut[0]+" aut="+aut[2]);
			         			} catch (SQLException e1) {
			        				// TODO Auto-generated catch block
			        				e1.printStackTrace();
			        			}

								// ricerca in trl_autorizzazioni_servizi tutti gli id dei servizi
			         			// legati ad un dato id_tipo_autorizzazione

			         			if (!aut[1].equals("NULL")) {
				        			String[] id_serv;
				        			id_serv = ricercaIdServizi(aut[1]);

				        			int j=0;
				        		    while (j< id_serv.length) {
				        				insertDirittiUtente(DATAAUT, id, id_serv[j], aut[2], ar2);
				        				j++;
				            	    }
				        			try {
				        				stmt.execute("COMMIT;");
				        				//System.out.println("Committing insert trl_diritti_utente per aut " + aut[2]);
				         			} catch (SQLException e1) {
				        				// TODO Auto-generated catch block
				        				e1.printStackTrace();
				        			}
			         			}

							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}


						} catch (SQLException e) {
							System.out.println("\n\tupdateUtenteBiblioteca - Errore: " + e.getMessage() + " ---> " + sql);
							System.out.println ("EXCEPTION: " +e.getMessage());
							sqlExecutedKo.add(sql);
							try {
								stmt.execute("ROLLBACK;");
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
					else
					{
					//update trl_utenti_biblioteca

						try {

							sql = componiUpdateUtentiBiblioteca(id_ub, dt, ar2, aut);

							String sqlUtf8 = null;
							try {
								sqlUtf8 = new String (sql.getBytes(), "UTF-8");
								stmt.execute(sqlUtf8);
								updateUtentiBibOk.add(sql);
								//sqlExecutedOk.add(sql);
			        			//System.out.println(">>>>> Update in trl_utenti_biblioteca per id="+id+" bib="+aut[0]+" aut="+aut[2]);

			        			 try {
			        			 	stmt.execute("COMMIT;");
			        				//System.out.println("Committing update trl_utenti_biblioteca per id="+id+" bib="+aut[0]+" aut="+aut[2]);
			         			} catch (SQLException e1) {
			        				// TODO Auto-generated catch block
			        				e1.printStackTrace();
			        			}


								// ricerca in trl_autorizzazioni_servizi tutti gli id dei servizi
			         			// legati ad un dato id_tipo_autorizzazione

			        			String[] id_serv;
			        			id_serv = ricercaIdServizi(aut[1]);

			        			int j=0;
			        		    while (j< id_serv.length) {
			        				verificaDirittiUtente(DATAAUT, id, id_serv[j], aut[2], ar2);
			        				j++;
			            	    }
			        			try {
			        				stmt.execute("COMMIT;");
			        				//System.out.println("Committing operazioni su trl_diritti_utente per aut" + aut[2]);
			         			} catch (SQLException e1) {
			        				// TODO Auto-generated catch block
			        				e1.printStackTrace();
			        			}

							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}


						} catch (SQLException e) {
							System.out.println("\n\tupdateUtenteBiblioteca - Errore: " + e.getMessage() + " ---> " + sql);
							System.out.println ("EXCEPTION: " +e.getMessage());
							sqlExecutedKo.add(sql);
							try {
								stmt.execute("ROLLBACK;");
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

					}
			//	} //Enf if aut
			}//End for

		}
		else
		{
			System.out.println("\n\tNessuna biblioteca associata a ateneo " + ar2[21-1]);
			return;
		}


		// TODO Auto-generated method stub
		return;
	} //End updateUtenteBiblioteca



private void trattaUtenteBiblioteca(String id, String[] ar2, String dt) {

		String[] biblio;
		biblio = ricercaBiblioteche(ar2[21-1], ar2[26-1], " "+ar2[1-1]);

		if (biblio.length > 0)    //array: bib|id_aut|cod_aut
		{
			String[] aut=null;
			for (int i=0; i < biblio.length; i++)
				{
					// aggiornamento/inserimento in trl_utenti_biblioteca di un legame per
					// la biblioteca di inserimento in input + tutte le altre biblioteche trovate

					Statement stmt = null;
					try {
						stmt = con.createStatement();
					} catch (SQLException e) {

						e.printStackTrace();
					}

					aut = biblio[i].split("\\|");
							//aut[0] biblioteca
							//aut[1] id tipo autorizzazione
							//aut[2] cod tipo autorizzazione
					//if (!aut[1].equals("NULL") || (aut[1].equals("NULL") && i == 0))
						//inserisco comunque una relazione tra la bib di riferimento e l'utente anche senza aut
						//{
						String id_ub="";
						if (id != null) {
							id_ub= selectUtentiBiblioteca(id, aut);  //ritorna id_utenti_biblioteca se esiste
						}

						if (id_ub == null)
						{
						try {
							sql = componiInsertUtentiBiblioteca(id, dt, ar2, aut);

							String sqlUtf8 = null;
							try {
								sqlUtf8 = new String (sql.getBytes(), "UTF-8");
								stmt.execute(sqlUtf8);
								insertUtentiBibOk.add(sql);
								//sqlExecutedOk.add(sql);
			        			//System.out.println(">>>>> Insert in trl_utenti_biblioteca per id="+id+" bib="+aut[0]+" aut="+aut[2]);
			        			try {
			        				stmt.execute("COMMIT;");
			        				//System.out.println("Committing insert trl_utenti_biblioteca. id="+id+" bib="+aut[0]+" aut="+aut[2]);
			         			} catch (SQLException e1) {
			        				// TODO Auto-generated catch block
			        				e1.printStackTrace();
			        			}

			         			// verifica se flag-chiudi è impostato e fa scadere tutti i diritti presenti in
			         			// trl_autorizzazioni_servizi
			         			if (ar2[30-1].equals("S")) {
				        				chiudiDirittiUtente(id, ar2);
				        			try {
				        				stmt.execute("COMMIT;");
				        				//System.out.println("Committing update trl_diritti_utente per flag_chiudi impostato");
				         			} catch (SQLException e1) {
				        				// TODO Auto-generated catch block
				        				e1.printStackTrace();
				        			}
			         			}

			         			// verifica e inserisce i diritti
								// ricerca in trl_autorizzazioni_servizi tutti gli id dei servizi
			         			// legati ad un dato id_tipo_autorizzazione

			         			if (!aut[1].equals("NULL")) {
				        			String[] id_serv;
				        			id_serv = ricercaIdServizi(aut[1]);

				        			int j=0;
				        		    while (j< id_serv.length) {
				        				insertDirittiUtente(DATAAUT, id, id_serv[j], aut[2], ar2);
				        				j++;
				            	    }
				        			try {
				        				stmt.execute("COMMIT;");
				        				//System.out.println("Committing insert trl_diritti_utente per aut " + aut[2]);
				         			} catch (SQLException e1) {
				        				// TODO Auto-generated catch block
				        				e1.printStackTrace();
				        			}
			         			}



							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}


						} catch (SQLException e) {
							System.out.println("\n\tupdateUtenteBiblioteca - Errore: " + e.getMessage() + " ---> " + sql);
							System.out.println ("EXCEPTION: " +e.getMessage());
							sqlExecutedKo.add(sql);
							try {
								stmt.execute("ROLLBACK;");
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
					else
					{
					//update trl_utenti_biblioteca

						try {
							if (id_ub.equals(ERROR_COD_AUT_DIVERSO)) {
								//almaviva5_20140714 #5578
								//l'utente ha associato un altro profilo di autorizzazione e non viene aggiornato.
								String nome = ar2[3-1].trim() + " " + ar2[4-1].trim();
								System.out.println("Attenzione: Utente '" + nome + "' già presente con altro profilo di autorizzazione. Non verrà aggiornato.");
								updateUtentiBibKo.add(nome);
								continue;
							}
							sql = componiUpdateUtentiBiblioteca(id_ub, dt, ar2, aut);

							String sqlUtf8 = null;
							try {
								sqlUtf8 = new String (sql.getBytes(), "UTF-8");
								int updated = stmt.executeUpdate(sqlUtf8);
								updateUtentiBibOk.add(sql);
								//sqlExecutedOk.add(sql);
			        			//System.out.println(">>>>> Update in trl_utenti_biblioteca per id="+id+" bib="+aut[0]+" aut="+aut[2]);

			        			 try {
			        			 	stmt.execute("COMMIT;");
			        				//System.out.println("Committing update trl_utenti_biblioteca per id="+id+" bib="+aut[0]+" aut="+aut[2]);
			         			} catch (SQLException e1) {
			        				// TODO Auto-generated catch block
			        				e1.printStackTrace();
			        			}


								// ricerca in trl_autorizzazioni_servizi tutti gli id dei servizi
			         			// legati ad un dato id_tipo_autorizzazione
			         			if (!aut[1].equals("NULL")) {
					        			String[] id_serv;
					        			id_serv = ricercaIdServizi(aut[1]);

					        			int j=0;
					        		    while (j< id_serv.length) {
					        				verificaDirittiUtente(DATAAUT, id, id_serv[j], aut[2], ar2);
					        				j++;
					            	    }
					        			try {
					        				stmt.execute("COMMIT;");
					        				//System.out.println("Committing insert trl_diritti_utente per aut " + aut[2]);
					         			} catch (SQLException e1) {
					        				// TODO Auto-generated catch block
					        				e1.printStackTrace();
					        			}
			         			}

							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}


						} catch (SQLException e) {
							System.out.println("\n\tupdateUtenteBiblioteca - Errore: " + e.getMessage() + " ---> " + sql);
							System.out.println ("EXCEPTION: " +e.getMessage());
							sqlExecutedKo.add(sql);
							try {
								stmt.execute("ROLLBACK;");
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

					}
			//	} //Enf if aut
			}//End for

		}
		else
		{
			System.out.println("\n\tNessuna biblioteca associata a ateneo " + ar2[21-1]);
			return;
		}


		// TODO Auto-generated method stub
		return;
	} //End trattaUtenteBiblioteca



	private void chiudiDirittiUtente(String id, String[] ar2) {

		Statement stmt = null;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {

			e.printStackTrace();
		}

			sql = "update trl_diritti_utente set " +
						" data_fine_serv = to_date('" + ar2[31-1] + "', 'DDmmYYYY')," +  //data_fine_serv (da file input)
						//" fl_canc = 'S', " +
						" ute_var = '" + codPolo + " " + ar2[1-1] + "000009'," + //ute_var
						" ts_var = now()" +  //ts_var
						" where id_utenti =" + id + ";";

			String sqlUtf8 = null;
			try {
				sqlUtf8 = new String (sql.getBytes(), "UTF-8");
				stmt.execute(sqlUtf8);
				//updateDirittiUtenteOk.add(sql);
				//sqlExecutedOk.add(sql);
				//System.out.println("\n>>>>> Update per flag_chiudi='S' in trl_diritti_utente per id="+id);
				/*
				 try {
				 	stmt.execute("COMMIT;");
					System.out.print("Committing insert trl_utenti_biblioteca");
	 			} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				*/
				} catch (SQLException e) {
					System.out.println("\n\tUpdateFlagUtentiBiblioteca - Errore: " + e.getMessage() + " ---> " + sql);
					System.out.println ("EXCEPTION: " +e.getMessage());
					sqlExecutedKo.add(sql);
					try {
						stmt.execute("ROLLBACK;");
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					return;
			} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			return;

} //chiudiDirittiUtente




	private boolean UpdateFlagUtentiBiblioteca(String id, String[] ar2) throws UnsupportedEncodingException {

		Statement stmt = null;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {

			e.printStackTrace();
		}


		String sql="";
		sql = "update trl_utenti_biblioteca set " +
					" fl_canc = 'S', " +
					" ute_var = '" + codPolo + " " + ar2[1-1] + "000009'," + //ute_var
					" ts_var = now()" +  //ts_var
					" where id_utenti_biblioteca =" + id + ";";

		String sqlUtf8 = null;
		try {
			sqlUtf8 = new String (sql.getBytes(), "UTF-8");
			stmt.execute(sqlUtf8);
			//updateUtentiBibOk.add(sql);
			//sqlExecutedOk.add(sql);
			//System.out.println(">>>>> Update fl_canc=S in trl_utenti_biblioteca per id="+id);
			/*
			 try {
			 	stmt.execute("COMMIT;");
				System.out.print("Committing insert trl_utenti_biblioteca");
 			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			*/
			} catch (SQLException e) {
				System.out.println("\n\tUpdateFlagUtentiBiblioteca - Errore: " + e.getMessage() + " ---> " + sql);
				System.out.println ("EXCEPTION: " +e.getMessage());
				sqlExecutedKo.add(sql);
				try {
					stmt.execute("ROLLBACK;");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return false;
		}


			sql = "update trl_diritti_utente set " +
						" fl_canc = 'S', " +
						" ute_var = '" + codPolo + " " + ar2[1-1] + "000009'," + //ute_var
						" ts_var = now()" +  //ts_var
						" where id_utenti =" + id + ";";

			sqlUtf8 = null;
			try {
				sqlUtf8 = new String (sql.getBytes(), "UTF-8");
				stmt.execute(sqlUtf8);
				//updateDirittiUtenteOk.add(sql);
				//sqlExecutedOk.add(sql);
				//System.out.println(">>>>> Update fl_canc=S in trl_diritti_utente per id="+id);
				/*
				 try {
				 	stmt.execute("COMMIT;");
					System.out.print("Committing insert trl_utenti_biblioteca");
	 			} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				*/
				} catch (SQLException e) {
					System.out.println("\n\tUpdateFlagUtentiBiblioteca - Errore: " + e.getMessage() + " ---> " + sql);
					System.out.println ("EXCEPTION: " +e.getMessage());
					sqlExecutedKo.add(sql);
					try {
						stmt.execute("ROLLBACK;");
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					return false;
			}


			return true;
	}//End UpdateFlagUtentiBiblioteca



	private void verificaDirittiUtente(String DATAAUT, String id, String id_serv, String aut, String[] ar2) {

		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement prepStatement = null;

		sql = "select * from trl_diritti_utente where id_utenti = '" + id + "'"
			+ " and id_servizio=" + id_serv;

		String sqlUtf8 = null;
		try {
			sqlUtf8 = new String (sql.getBytes(), "UTF-8");
			prepStatement = con.prepareStatement(sqlUtf8.toString());

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    try {
			rs = prepStatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	try {
			if (rs.next())
			   	{
					//System.out.println("Trovato in trl_diritti_utente record per "+ id + "  - UPDATE");
					//selectUtentiTrovati.add(rs.getString("id_utenti")+" " + rs.getString("cod_utente")+" " + rs.getString("cognome"));
					//necessario update
					updateDirittiUtente(DATAAUT, id, id_serv, aut, ar2);
				}
			else
				{
					//System.out.println("Non trovato in trl_diritti_utente record per "+ id + "  - INSERT");
					//selectUtentiTrovati.add(rs.getString("id_utenti")+" " + rs.getString("cod_utente")+" " + rs.getString("cognome"));
					//necessario insert
					insertDirittiUtente(DATAAUT, id, id_serv, aut, ar2);

				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	// TODO Auto-generated method stub

}//End verificaDirittiUtente







	private String componiUpdateUtentiBiblioteca(String id, String dt, String[] ar2, String[] aut) {

		String sql="";
		sql = "update trl_utenti_biblioteca set " +
					" data_inizio_aut = to_date('" + dt + "', 'DD/mm/YYYY')," + 		//data_inizio_aut
					" data_fine_aut = to_date('" + ar2[31-1] + "', 'DDmmYYYY')," +  	//data_fine_aut
					" ute_var = '" + codPolo + " " + ar2[1-1] + "000009'," + //ute_var
					" ts_var = now()," +  //ts_var
					" fl_canc = 'N' " +  //fl_canc
					" where id_utenti_biblioteca =" + id + " and cd_biblioteca='" + " " + ar2[1-1]+ "' " +
					" and cod_tipo_aut='" + aut[2] + "';";

	return sql;
}




	private String selectUtentiBiblioteca(String idutenti, String[] aut) {

		ResultSet rs = null;
		PreparedStatement prepStatement = null;

		if (aut[2].equals("NULL"))
		{
			aut[2] = "";
		}

		// verifica esistenza in trl_utenti_biblioteca di un record utente/bib
		// se non esiste si deve inserire, se esiste si deve aggiornare
		try {

			sql = "select * from trl_utenti_biblioteca where id_utenti = '" + idutenti + "' and cd_biblioteca = '" + aut[0] + "' ";
			//	+ "	and cod_tipo_aut = '" + aut[2] + "' order by cd_biblioteca;";

			String sqlUtf8 = null;
			try {
				sqlUtf8 = new String (sql.getBytes(), "UTF-8");
				prepStatement = con.prepareStatement(sqlUtf8.toString());

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    rs = prepStatement.executeQuery();
        	if (rs.next())
        	   	{
        			selectUtentiBibTrovati.add(rs.getString("id_utenti_biblioteca"));
        			String codAutDB = rs.getString("cod_tipo_aut");
        			if (!aut[2].equals(codAutDB)) {
        				//l'utente ha associato un altro profilo di autorizzazione
        				return ERROR_COD_AUT_DIVERSO;
        			}
        			//System.out.println("Trovato in trl_utenti_biblioteca record con id="
        			//		+ idutenti + " id_utenti_bib=" + rs.getString("id_utenti_biblioteca") + " e bib=" + aut[0] + " - UPDATE");

        			//necessario update
        			return rs.getString("id_utenti_biblioteca");
        	   	}
            else
            	{
            		//System.out.println("NON trovato in trl_utenti_biblioteca record con id= " + idutenti+ " - INSERT");
            		selectUtentiBibNonTrovati.add(idutenti);
        			//necessario insert
        			return null;
            	}


		} catch (SQLException e) {
			System.out.println("\n\tselectUtentiBiblioteca - Errore: " + e.getMessage() + " ---> " + sql);
			System.out.println ("EXCEPTION: " +e.getMessage());
			sqlExecutedKo.add(sql);
			return null;
		}

	}//End selectUtentiBiblioteca


	private String[] ricercaBiblioteche(String ateneo, String prof, String bib) {

		ResultSet rs = null;
		PreparedStatement prepStatement = null;
		String biblio = "";
		String tipoaut = "";
		String[] bib1 = null;
		//String[][] bib2 = null;

		//aggiunge all'array la biblioteca di input
		biblio = bib + "|";
		//System.out.println("\n-----------BIBLIOTECHE-------------");
		//System.out.println("Bib di inserimento: " +bib);

		// estrae le altre biblioteche associate all'ateneo
		try {

			sql = "select cd_biblioteca from tbf_biblioteca_in_polo where cd_ana_biblioteca = '"
				+ ateneo + "' and cd_biblioteca <> '" + bib + "' order by cd_biblioteca;";

			String sqlUtf8 = null;
			try {
				sqlUtf8 = new String (sql.getBytes(), "UTF-8");
				prepStatement = con.prepareStatement(sqlUtf8.toString());

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    rs = prepStatement.executeQuery();
		    while (rs.next()) {
    			//System.out.println("Bib associata ateneo " + ateneo + ": " +rs.getString("cd_biblioteca"));
    			biblio = biblio + rs.getString("cd_biblioteca")+ "|";
    	    }

		    bib1 = biblio.split("\\|");


		    // per ogni biblioteca trovata cerca il tipo autorizzazione associato in base alla PROFESSIONE
			//System.out.println("-----------AUTORIZZAZIONE-------------");
		    int i=0;
		    while (i< bib1.length) {
				tipoaut = ricercaTipiAutorizzazioni(prof, bib1[i]);
				//if (tipoaut != null)
					bib1[i] = bib1[i]+"|"+tipoaut;
					i++;
    	    }
			//System.out.println("--------------------------------------\n");
		    return bib1;


		} catch (SQLException e) {
			System.out.println("\n\tricercaBiblioteche - Errore: " + e.getMessage() + " ---> " + sql);
			System.out.println ("EXCEPTION: " +e.getMessage());
			sqlExecutedKo.add(sql);
			return null;
		}

	} //End ricercaBiblioteche


	private String ricercaTipiAutorizzazioni(String professione, String bib) {

		ResultSet rs = null;
		PreparedStatement prepStatement = null;

		// estrae tipi autorizzazione associati alla coppia BIB/Professione
		try {

			sql = "select id_tipi_autorizzazione,cod_tipo_aut from tbl_tipi_autorizzazioni "
				+ "where cd_bib = '" + bib + "' and flag_aut = '" + professione + "' and fl_canc<>'S';";

			String sqlUtf8 = null;
			try {
				sqlUtf8 = new String (sql.getBytes(), "UTF-8");
				prepStatement = con.prepareStatement(sqlUtf8.toString());

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    rs = prepStatement.executeQuery();
		    if (rs.next()) {
				//System.out.println("tipo autorizzazione=" +rs.getString("cod_tipo_aut")
    			//		+ " per bib="+ bib + " e professione=" + professione);
    			return rs.getString("id_tipi_autorizzazione")+"|"+rs.getString("cod_tipo_aut");
    	    }
		    else
		    {
		    	//System.out.println("Nessun tipo autorizzazione per bib="+ bib + " e professione=" + professione);
		    	return "NULL|NULL";
		    }

		} catch (SQLException e) {
			System.out.println("\n\tricercaTipiAutorizzazioni - Errore: " + e.getMessage() + " ---> " + sql);
			System.out.println ("EXCEPTION: " +e.getMessage());
			sqlExecutedKo.add(sql);
			return null;
		}

	} //End ricercaTipiAutorizzazioni


	private String[] ricercaIdServizi(String id_aut) {

		String[] id_serv=null;
		String servizi="";
		int s=0;
		ResultSet rs = null;
		PreparedStatement prepStatement = null;

		//System.out.println("\n-----------SERVIZI-------------");
		// estrae gli id dei servizi associati a un dato tipo autorizzazione per inserire i diritti utente
		// Se non esistono servizi associati l’elaborazione si conclude senza inserimento di diritti
		try {

			sql = "Select id_servizio from trl_autorizzazioni_servizi "
				+ "where id_tipi_autorizzazione = '" + id_aut + "' "
				+ "and fl_canc<>'S' order by 1;";

			String sqlUtf8 = null;
			try {
				sqlUtf8 = new String (sql.getBytes(), "UTF-8");
				prepStatement = con.prepareStatement(sqlUtf8.toString());

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    rs = prepStatement.executeQuery();
		    while (rs.next()) {
		    	//System.out.println("Trovato id servizio " +rs.getString("id_servizio")
    			//		+ " per id autorizzazione="+ id_aut);
    			servizi = servizi + rs.getString("id_servizio")+ "|";
    	    }
		    id_serv = servizi.split("\\|");

		    if (id_serv.length <= 0)
		    	{
		    	//System.out.println("Nessun id servizio per id autorizzazione="+ id_aut);
		    	return null;
		    	}
			//System.out.println("--------------------------------------");

		} catch (SQLException e) {
			System.out.println("\n\tricercaIdServizi - Errore: " + e.getMessage() + " ---> " + sql);
			System.out.println ("EXCEPTION: " +e.getMessage());
			sqlExecutedKo.add(sql);
			return null;
		}

		return id_serv;

	} //End ricercaIdServizi


	private void insertDirittiUtente(String DATAAUT, String id_utente, String id_serv, String cod_aut, String[] ar3) {

		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement prepStatement = null;


		try {
			stmt = con.createStatement();
		} catch (SQLException e) {

			e.printStackTrace();
		}

		// Per ogni id servizio trovato si inserisce un record in trl_diritti_utenti
		try {

			sql = "insert into trl_diritti_utente values(" +
				"'" + id_utente + "'," +
				"'" + id_serv + "'," +
				"to_date('" + DATAAUT + "', 'DD/mm/YYYY')," +  //data_inizio_serv (parametro)
				"to_date('" + ar3[31-1] + "', 'DDmmYYYY')," +  //data_fine_serv (da file input)
				"NULL," +
				"NULL," +
				"NULL," +   //note
				"'" + codPolo + " " + ar3[1-1] + "000009'," + //ute_ins
				"now()," +  //ts_ins
				"'" + codPolo + " " + ar3[1-1] + "000009'," + //ute_var
				"now()," +  //ts_var
				"'N'," +  //fl_canc
				"'" + cod_aut + "'" +
				");";

			String sqlUtf8 = null;
			try {
				sqlUtf8 = new String (sql.getBytes(), "UTF-8");
				stmt.execute(sqlUtf8);
				insertDirittiUtenteOk.add(sql);
				//sqlExecutedOk.add(sql);
    			//System.out.println(">>>>> Insert in trl_diritti_utenti per id="+id_utente+" servizio="+id_serv);


			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		} catch (SQLException e) {
			System.out.println("\n\tinsertDirittiUtente - Errore: " + e.getMessage() + " ---> " + sql);
			System.out.println ("EXCEPTION: " +e.getMessage());
			sqlExecutedKo.add(sql);
			try {
				stmt.execute("ROLLBACK;");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return;

	} //End insertDirittiUtente



	private void updateDirittiUtente(String DATAAUT, String id_utente, String id_serv, String cod_aut, String[] ar3) {

		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement prepStatement = null;


		try {
			stmt = con.createStatement();
		} catch (SQLException e) {

			e.printStackTrace();
		}

		// Per ogni id servizio trovato si aggiorna un record in trl_diritti_utenti
		try {

			sql = "update trl_diritti_utente set " +
				" data_inizio_serv = to_date('" + DATAAUT + "', 'DD/mm/YYYY')," +  //data_inizio_serv (parametro)
				" data_fine_serv = to_date('" + ar3[31-1] + "', 'DDmmYYYY')," +  //data_fine_serv (da file input)
				" ute_var = '" + codPolo + " " + ar3[1-1] + "000009'," + //ute_var
				" ts_var = now()," +  //ts_var
				" fl_canc = 'N', " +  //fl_canc
				" cod_tipo_aut='" + cod_aut + "'" +
				" where id_utenti='" + id_utente + "' and id_servizio=" + id_serv;
				//+ "' and cod_tipo_aut='" + cod_aut + "';";

			String sqlUtf8 = null;
			try {
				sqlUtf8 = new String (sql.getBytes(), "UTF-8");
				int updated = stmt.executeUpdate(sqlUtf8);
				updateDirittiUtenteOk.add(sql);
				//sqlExecutedOk.add(sql);
    			//System.out.println(">>>>> Update in trl_diritti_utenti per id="+id_utente+" servizio="+id_serv);


			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		} catch (SQLException e) {
			System.out.println("\n\tupdateDirittiUtente - Errore: " + e.getMessage() + " ---> " + sql);
			System.out.println ("EXCEPTION: " +e.getMessage());
			sqlExecutedKo.add(sql);
			try {
				stmt.execute("ROLLBACK;");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


		}
		return;

	} //End updateDirittiUtente


	private String insertUtente(String[] ar2) {

		String idutenti=null;
		//int rowCtr = 0;
		//int commitRowCtr = 0;
		Statement stmt = null;
		//int rollbackRecordsFrom = 1;

		ResultSet rs = null;
		PreparedStatement prepStatement = null;

		try {
			stmt = con.createStatement();
		} catch (SQLException e) {

			e.printStackTrace();
		}


		try {
			/*
			// Commit every tot records
			if ((rowCtr  & 0x3FF) == 0) // Mettiamo ogni 1024 records
			{
				System.out.print("\rCommitting at row " + rowCtr);
				stmt.execute("COMMIT;");
				sqlExecutedOk.clear();
				rollbackRecordsFrom = rowCtr;
			}
			*/

			/* STRUTTURA INSERT tbl_utenti
			insert into tbl_utenti values(
			'id','polo',' bib','codutente','password','cognome','nome',
			'indres','cittares','capres','telres','faxres','inddom','cittadom','capdom','teldom','faxdom',
			'sesso','dtnascita','luogonasc','codfiscale',
			'codateneo','matricola','corsolaurea','postael','persgiur','nomeref',
			'datareg','credito','notepolo','note','codproven','tipopersg','paeseres','paesecitt',
			'tpdoc1','ndoc1','autdoc1','tpdoc2','ndoc2','autdoc2',
			'tpdoc3','ndoc3','autdoc3','tpdoc4','ndoc4','autdoc4',
			'bib','provres','provdom','polobib','allinea','chiaveutente',
			'uteins','tsins','utevar','tsvar','flcanc',
			'changepass','lastaccess','tsvarpass','codana','titstudio','prof','vector');
			*/

			// CONTROLLO e variazione dei campi di INPUT

			//campo posta elettronica a NULL se vuoto
			String posta="";
			if (ar2[24-1].trim().equals(""))
				posta = "NULL";
			else
				posta = "'"  + ar2[24-1].trim() + "'";

			// CONTROLLO e variazione dei campi di INPUT - fine

			sql = "insert into tbl_utenti values(" +
				"nextval('tbl_utenti_id_utenti_seq')," + 	//id_utenti
				"'" + codPolo + "'," +    		//polo
				"' " + ar2[1-1] + "'," +    		//bib
				"'"  + ar2[1-1] + "'||lpad(CAST(nextval('seq_cod_utente_lettore') as text),10,'0')," + //cod_utente
				"'"  + ar2[2-1] + "'," +  			//password
				"'"  + ar2[3-1].trim() + "'," +		//cognome
				"'"  + ar2[4-1].trim() + "'," + 	//nome
				"'"  + ar2[5-1].trim() + "'," + 	//dati residenza da [5-1] a [9-1]
				"'"  + ar2[6-1].trim() + "'," +
				"'"  + ar2[7-1] + "'," +  //cap_res
				"'"  + ar2[8-1].trim() + "'," +
				"'"  + ar2[9-1].trim() + "'," +
				"'"  + ar2[11-1].trim() + "'," + 	//dati domicilio da [11-1] a [15-1]
				"'"  + ar2[12-1].trim() + "'," +
				"'"  + ar2[13-1] + "'," +  //cap_dom
				"'"  + ar2[14-1].trim() + "'," +
				"'"  + ar2[15-1].trim() + "'," +
				"'"  + ar2[17-1] + "'," +  			//sesso
				"'"  + ar2[18-1] + "'," + 			//data nascita (mm/gg/aaaa)
				"'"  + ar2[19-1].trim() + "'," +	//luogo nascita
				"'"  + ar2[20-1] + "'," +  			//codice fiscale
				"'"  + ar2[21-1] + "'," + 			//codice ateneo
				"'"  + ar2[22-1] + "'," + 			//matricola
				"'"  + ar2[23-1].trim() + "'," + 	//corso laurea
				posta + "," +	//posta elettronica (precedentemente impostato a null se vuoto)
				"'N',"  +  //persona_giuridica
				"NULL," +  //nome_referente
				//"NULL," +  //data_reg
				"CURRENT_DATE," +  //data_reg (gg/mm/aaaa)
				"NULL," +  //credito_polo
				"NULL," +  //note_polo
				"NULL," +  //note
				"'"  + ar2[25-1] + "'," +  //cod_proven
				"NULL," +  //tipo_pers_giur
				"'"  + ar2[28-1] + "'," + 	//paese_res
				"'"  + ar2[29-1] + "'," +  	//paese_citt
				"NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL," + //dati documenti da 1 a 4
				"' " + ar2[1-1] + "'," +  //cod_polo_bib
				"'"  + ar2[16-1] + "'," + //prov_dom
				"'"  + ar2[10-1] + "'," + //prov_res
				"'" + codPolo + "'," + //cod_polo
				"'X'," +  //allinea
				"'" + ar2[32-1] + "'," +  //chiave_utente (calcolata in fase di acquisizione del file)
				"'" + codPolo + " " + ar2[1-1] + "000009'," + //ute_ins
				"now()," +  //ts_ins
				"'" + codPolo + " " + ar2[1-1] + "000009'," + //ute_var
				"now()," +  //ts_var
				"'N'," +  //fl_canc
				"'S'," +  //change_password
				"now()," +  //last_access
				"now()," +  //ts_var_password
				"NULL," +  //codice_anagrafe
				"'"  + ar2[27-1] + "'," + 	//tit_studio
				"'"  + ar2[26-1] + "'," +	//professione
				"NULL" +  //tidx_vector
				");";

			String sqlUtf8 = null;
			try {
				sqlUtf8 = new String (sql.getBytes(), "UTF-8");
				stmt.execute(sqlUtf8);
				insertUtentiOk.add(sql);
				//sqlExecutedOk.add(sql);
				//commitRowCtr++;

				//Recupero id utente appena inserito
				sql = "select * from tbl_utenti where cod_fiscale = '" + ar2[20-1] + "';";
				sqlUtf8 = null;
				try {
					sqlUtf8 = new String (sql.getBytes(), "UTF-8");
					prepStatement = con.prepareStatement(sqlUtf8.toString());
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    rs = prepStatement.executeQuery();
	        	if (rs.next())
	        	   	{
	        			//System.out.println("Inserito in tbl_utenti record: " + rs.getString("id_utenti") + " - " + rs.getString("cod_utente"));
	        			idutenti = rs.getString("id_utenti");
	        			//System.out.println(">>>>> Insert in tbl_utenti per id="+idutenti);
	        			try {
	        				stmt.execute("COMMIT;");
	        				//System.out.print("\n\nCommitting insert tbl_utenti per: " + rs.getString("id_utenti") + " - " + rs.getString("cod_utente"));
	        			} catch (SQLException e1) {
	        				// TODO Auto-generated catch block
	        				e1.printStackTrace();
	        			}

	        	   	}

			} catch (UnsupportedEncodingException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
			}



		} catch (SQLException e) {
			//System.out.println("\n\tRecord errato alla riga " + rowCtr + ": " + e.getMessage() + " ---> " + sql);
			System.out.println("\nRecord errato alla riga " + recordCounter + ": " + sql);
			System.out.println ("EXCEPTION: " +e.getMessage());
			sqlExecutedKo.add(sql);
			insertUtentiKo.add(sql);


			try {
				if (sqlExecutedKo.size() > 0)
				{
					System.out.println("rolled back records;");
					stmt.execute("ROLLBACK;"); // Commit what had been rolled back before tje exception
					sqlExecutedKo.clear(); // Get ready for next round
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

		}
		return idutenti;
	} //End insertUtente


	private boolean updateUtente(String id, String[] ar2) {

		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement prepStatement = null;

		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		try {
			//prepara update tbl_utenti

			//campo posta elettronica a NULL se vuoto
			String posta="";
			if (ar2[24-1].trim().equals(""))
				posta = "NULL";
			else
				posta = "'"  + ar2[24-1].trim() + "'";

			sql = "update tbl_utenti set " +
						"password = '" + ar2[2-1] + "'," +
						"cognome = '" + ar2[3-1] + "'," +
						"nome = '" + ar2[4-1] + "'," +
						"indirizzo_res = '" + ar2[5-1] + "'," +
						"citta_res = '" + ar2[6-1] + "'," +
						"cap_res = '" + ar2[7-1] + "'," +
						"tel_res = '" + ar2[8-1] + "'," +
						"fax_res = '" + ar2[9-1] + "'," +
						"indirizzo_dom = '" + ar2[11-1] + "'," +
						"citta_dom = '" + ar2[12-1] + "'," +
						"cap_dom = '" + ar2[13-1] + "'," +
						"tel_dom = '" + ar2[14-1] + "'," +
						"fax_dom = '" + ar2[15-1] + "'," +
						"sesso = '" + ar2[17-1] + "'," +
						"data_nascita = '" + ar2[18-1] + "'," +
						"luogo_nascita = '" + ar2[19-1] + "'," +
						"cod_ateneo = '" + ar2[21-1] + "'," +
						"cod_matricola = '" + ar2[22-1] + "'," +
						"corso_laurea = '" + ar2[23-1] + "'," +
						//"ind_posta_elettr = '" + ar2[24-1] + "'," +
						"ind_posta_elettr = " + posta + "," +
						"cod_proven = '" + ar2[25-1] + "'," +
						"paese_res = '" + ar2[28-1] + "'," +
						"paese_citt = '" + ar2[29-1] + "'," +
						"prov_dom = '" + ar2[16-1] + "'," +
						"prov_res = '" + ar2[10-1] + "'," +
						"chiave_ute = '" + ar2[32-1] + "'," +  //calcolata in fase di acquisizione del file
						"ute_var = '" + codPolo + " " + ar2[1-1] + "000009'," + //ute_var
						"ts_var = now()," +  //ts_var
						"tit_studio = '" + ar2[27-1] + "'," +
						"professione = '" + ar2[26-1] + "' " +
						" where cod_fiscale ='" + ar2[20-1] + "' and id_utenti='" + id + "';";


			String sqlUtf8 = null;
			try {
				sqlUtf8 = new String (sql.getBytes(), "UTF-8");
				stmt.execute(sqlUtf8);
    			//System.out.println(">>>>> Update in tbl_utenti per id_utenti=" + id);
    			updateUtentiOk.add(sql);
				//sqlExecutedOk.add(sql);
				try {
    				stmt.execute("COMMIT;");
    				//System.out.println("Committing update tbl_utenti per id=" + id);
    			} catch (SQLException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (SQLException e) {
			//System.out.println("\n\tRecord errato alla riga " + recordCounter + ": " + e.getMessage() + " ---> " + sql);
			System.out.println("\nRecord errato alla riga " + recordCounter + ": " + sql);
			System.out.println ("EXCEPTION: " +e.getMessage());
			sqlExecutedKo.add(sql);
			updateUtentiKo.add(sql);


			//try {
			//if (sqlExecutedKo.size() > 0)
			//{
				System.out.println("rolled back records;");
				try {
					stmt.execute("ROLLBACK;");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//sqlExecutedKo.clear(); // Get ready for next round
				return false;
			//}



		}
		return true;
	}  //End updateUtente





	// estrae i campi del record in base ad un dato separatore
	public static String[] estraiCampi(String rec, String sep)
	{

		String[] appoReturn = new String[1];
		appoReturn[0] = " ";
		if (rec == null) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR001");
			return appoReturn;
		}
		if (rec.equals("")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR002");
			return appoReturn;
		}
		if (rec.equalsIgnoreCase("null")) {
			// System.out.println("[" + getDate() + " " + getTime() + "] " +
			// "Funzioni.estraiCampi: ERR003");
			return appoReturn;
		}


		String[] recordFields = rec.split(sep);
//		int sizeOfArray = recordFields.length;

		//System.out.println( "sizeOfArray ==>" + sizeOfArray );
//		if (sizeOfArray != 33) {
//			System.out.println("Record alla riga " + recordCounter + " con numero di campi errato");
//			recordCounterErr++;
//			return null;
//		}


		return recordFields;
	} // End estraiCampi


	public static boolean emptyString(String textLine) {

		for (int i = 0; i < textLine.length(); i++) {
			if (textLine.charAt(i) != '\n' && textLine.charAt(i) != '\r'
					&& textLine.charAt(i) != '\t' && textLine.charAt(i) != ' ')
				return false;
		}
		return true;
	}


	public boolean openConnection()
	{

		try {
			 //Class.forName("org.postgresql.Driver");
			 Class.forName ( jdbcDriver);

		} catch(java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		try {

				con = DriverManager.getConnection(connectionUrl,userName, userPassword);
				Statement st = con.createStatement();
				if (st.execute("select version()") )  {
					ResultSet resultSet = st.getResultSet();
					resultSet.next();
					String version = resultSet.getString(1);
					st.close();
					if (version.substring(11).compareTo(Constants.POSTGRES_VERSION_83) < 0) {// config TSearch2 (solo se ver. < 8.3)
						Statement st2 = con.createStatement();
						st2.execute("select set_curcfg('default')");
						st2.close();
					}
				}

				//con = getConnection();
				boolean autoCommit = con.getAutoCommit();
				con.setAutoCommit(false);

				Statement stmt = null;
				try {
					stmt = con.createStatement();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (jdbcDriver.contains("postgres"))
				{
					stmt.execute("SET search_path = "+searchPath); // sbnweb, pg_catalog, public
					if (setCurCfg == true)
						stmt.execute("select set_curcfg('default')"); // Esegui questa select per attivare gli indici testuali
				}

				return true;
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		return false;
	} // End openConnection


	public void closeConnection()
	{
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // End closeConnection

	/*
	public Connection getConnection() throws SQLException {
		Connection conn = null;

		try {

			if (ds == null) {
				Context initContext = new InitialContext();
				ds = (DataSource) initContext.lookup(DATA_SOURCE);
			}

			conn = ds.getConnection();
			Statement st = conn.createStatement();
			if (st.execute("select version()") )  {
				ResultSet resultSet = st.getResultSet();
				resultSet.next();
				String version = resultSet.getString(1);
				st.close();
				if (version.substring(11).compareTo(Constants.POSTGRES_VERSION_83) < 0) {// config TSearch2 (solo se ver. < 8.3)
					Statement st2 = conn.createStatement();
					st2.execute("select set_curcfg('default')");
					st2.close();
				}
			}


		} catch (NamingException e) {
			e.printStackTrace();
		}
		return conn;
	} //End getConnection
	*/


    public static void main(String args[])
    {
		System.out.println("\n\nInizio programma ImportaUtentiLettori: ");
    	//System.out.println("  args[0]: "+args[0]);
    	//System.out.println("  args[1]: "+args[1]);
    	//System.out.println("  args[2]: "+args[2]);

        if(args.length < 3)
        {
            System.out.println("Parametri di input mancanti o incompleti");
            System.out.println("Uso: ImportaUtentiLettori <fileConfig> <fileInput> <dataInizio>");
            //System.out.println("Parametri di input mancanti o incompleti. >>>>> args[0]"+args[0]+" args[1]"+args[1]+"<<<<<");
            System.exit(1);
        }

        String charSepArrayEquals = "=";

        String configFile = args[0];
        String inputFile = args[1];
        DATAAUT = args[2];

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Parametri di input:");
    	System.out.println("       File di configurazione: "+configFile);
    	System.out.println("       File input utenti: "+inputFile);
    	System.out.println("       Data inizio autorizzazione: "+DATAAUT);
    	System.out.println("-------------------------------------------------------------------------\n\n");



		//inizio elaborazione file lettori
    	ImportaUtentiLettori lettori = new ImportaUtentiLettori();

 		BufferedReader in;
 		String s;

    	try {
    		in = new BufferedReader(new FileReader(configFile));
    		/*
    		#==================================================
    		# File di Configurazione Import Utenti Lettori
    		#==================================================
    		codPolo=RML
    		jdbcDriver=org.postgresql.Driver
    		connectionUrl=jdbc:postgresql://193.206.221.14:5432/sbnwebCollaudo2
    		userName=sbnweb
    		userPassword=sbnweb
    		searchPath=sbnweb, pg_catalog, public;
    		schema=sbnweb
    		endConfig
    		*/
    		while (true) {
    			try {
    				s = in.readLine();
    				if (s == null)
    					break;
    				else if (emptyString(s))
    					continue;
    				else if ((	s.length() == 0)
    							||  (s.charAt(0) == '#')
    							|| (emptyString(s) == true))
    						continue;
    				else {
    					cf = estraiCampi(s,  charSepArrayEquals); // "="
    					if (cf[0].startsWith("codPolo"))
    						ImportaUtentiLettori.codPolo = cf[1];
    					else if (cf[0].startsWith("jdbcDriver"))
    						ImportaUtentiLettori.jdbcDriver = cf[1];
    					else if (cf[0].startsWith("connectionUrl"))
    						ImportaUtentiLettori.connectionUrl = cf[1];
    					else if (cf[0].startsWith("userName"))
    						ImportaUtentiLettori.userName = cf[1];
    					else if (cf[0].startsWith("userPassword"))
    						ImportaUtentiLettori.userPassword = cf[1];
    					else if (cf[0].startsWith("searchPath"))
    						ImportaUtentiLettori.searchPath = cf[1];
    					else if (cf[0].startsWith("schema"))
    						ImportaUtentiLettori.schema = cf[1];
    					else if (cf[0].startsWith("endConfig"))
    						break;
    					else
    						System.out.println("ERRORE: parametro configurazione sconosciuto: " + cf[0]+ "=" + cf[1]);
    				}
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}



		// Apriamo il DB
		if (!lettori.openConnection())
			{
			System.out.println("Failed to open DB of URL: " + lettori.connectionUrl);
			return;
			}


 		//BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(inputFile));
			//String s;

			while(true) {
				try {
					s = in.readLine();
					if (s == null)
						break;
					else
					{
						//System.out.println("\nReading: "+s);
						s = s.replaceAll("'", "''");
						//modifico eventuale carattere "\" in "/" (syntax error in elaborazione se presente /|)
						s = s.replace('\\', '/');

						recordCounter++;
						String charSepArraySpace = "\\|";
						ar = estraiCampi(s, charSepArraySpace);

						int sizeOfArray = ar.length;

						//System.out.println( "sizeOfArray ==>" + sizeOfArray );
						if (sizeOfArray != 32) {
							System.out.println("Record alla riga " + recordCounter + " (" + ar[20-1] + ") con numero di campi errato");
							recordCounterErr++;
						}

						if (ar != null)
							// controlla la lunghezza di alcuni campi di input e imposta domicilio se assente
							lettori.controllaRec(ar);


						if (ar != null)
							lettori.upload(ar); // aggiorna il db con l'utente letto

					}


				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		System.out.println("\n\n----------------------------------------------------------------------------");
		System.out.println(" Record letti in input : " + Integer.toString(recordCounter));
		System.out.println(" Record errati in input: " + Integer.toString(recordCounterErr));
		System.out.println("----------------------------------------------");
	    System.out.println(" Record non trovati in tbl_utenti           : " + selectUtentiNonTrovati.size());
	    System.out.println(" Record inseriti in tbl_utenti              : " + insertUtentiOk.size());
	    System.out.println(" Record errati/non inseriti in tbl_utenti   : " + insertUtentiKo.size());
		System.out.println("----------------------------------------------");
	    System.out.println(" Record trovati in tbl_utenti               : " + selectUtentiTrovati.size());
	    System.out.println(" Record aggiornati in tbl_utenti            : " + updateUtentiOk.size());
	    System.out.println(" Record errati/non aggiornati in tbl_utenti : " + updateUtentiKo.size());
	    System.out.println("----------------------------------------------");
	    System.out.println(" Record non trovati in trl_utenti_biblioteca: " + selectUtentiBibNonTrovati.size());
	    System.out.println(" Record inseriti in trl_utenti_biblioteca   : " + insertUtentiBibOk.size());
	    System.out.println(" Record trovati in trl_utenti_biblioteca    : " + selectUtentiBibTrovati.size());
	    System.out.println(" Record aggiornati in trl_utenti_biblioteca : " + updateUtentiBibOk.size());
	    System.out.println(" Record non agg. in trl_utenti_biblioteca   : " + updateUtentiBibKo.size());
	    System.out.println("----------------------------------------------");
	    System.out.println(" Record inseriti in trl_diritti_utente      : " + insertDirittiUtenteOk.size());
	    System.out.println(" Record aggiornati in trl_diritti_utente    : " + updateDirittiUtenteOk.size());
		System.out.println("----------------------------------------------------------------------------");
	    System.out.println(" Totale query con errori: " + sqlExecutedKo.size());
		System.out.println("----------------------------------------------------------------------------");

		if (selectUtentiTrovati.size() > 0)
			System.out.println(" Utenti già presenti in tbl_utenti\n" + selectUtentiTrovati.toString());


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		// Chiudiamo il DB
		lettori.closeConnection();
        System.exit(0);

    	} catch (FileNotFoundException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();

    	}

    } // End main




	private void controllaRec(String[] ar) {

		boolean res = true;

		if (ar[3-1].trim().length() > 80 //cognome
			|| ar[4-1].trim().length() > 25  //nome
			|| ar[5-1].trim().length() > 50  //indirizzo res
			|| ar[11-1].trim().length() > 50  //indirizzo dom
			|| ar[24-1].trim().length() > 80)  //posta elettronica
		{
			System.out.println("\n----- Record alla riga " + recordCounter + " (" + ar[20-1] + ") con campi troppo lunghi troncati in inserimento");
			if (ar[3-1].trim().length() > 80) {
				System.out.println("----- cognome='" + ar[3-1] + "' cognome inserito='" + ar[3-1].substring(0, 80) + "'");
				ar[3-1] = ar[3-1].substring(0, 80);
			}
			if (ar[4-1].trim().length() > 25) {
				System.out.println("----- nome='" + ar[4-1] + "' nome inserito='" + ar[4-1].substring(0, 25) + "'");
				ar[4-1] = ar[4-1].substring(0, 25);
			}
			if (ar[5-1].trim().length() > 50) {
				System.out.println("----- ind res='" + ar[5-1] + "' ind res inserito='" + ar[5-1].substring(0, 50) + "'");
				ar[5-1] = ar[5-1].substring(0, 50);
			}
			if (ar[11-1].trim().length() > 50) {
				System.out.println("----- ind dom='" + ar[11-1] + "' ind dom inserito='" + ar[11-1].substring(0, 50) + "'");
				ar[11-1] = ar[11-1].substring(0, 50);
			}
			if (ar[24-1].trim().length() > 80) {
				System.out.println("----- posta el='" + ar[24-1] + "' posta el inserita='" + ar[24-1].substring(0, 80) + "'");
				ar[24-1] = ar[24-1].substring(0, 80);
			}
		}

			// imposta i dati di DOMICILIO (SE ASSENTI) con i dati di RESIDENZA

			if (ar[11-1].trim().equals(""))
				ar[11-1] = ar[5-1];
			if (ar[12-1].trim().equals(""))
				ar[12-1] = ar[6-1];
			if (ar[13-1].trim().equals(""))
				ar[13-1] = ar[7-1];
			if (ar[14-1].trim().equals(""))
				ar[14-1] = ar[8-1];
			if (ar[15-1].trim().equals(""))
				ar[15-1] = ar[9-1];
			if (ar[16-1].trim().equals(""))
				ar[16-1] = ar[10-1];
		return;
		}



} // End ImportaUtentiLettori

