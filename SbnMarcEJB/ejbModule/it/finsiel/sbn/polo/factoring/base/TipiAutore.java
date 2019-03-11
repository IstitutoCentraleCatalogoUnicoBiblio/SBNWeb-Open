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
package it.finsiel.sbn.polo.factoring.base;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.util.AnnoDate;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.SbnData;
import it.finsiel.sbn.polo.oggetti.estensione.AutoreValida;
import it.finsiel.sbn.polo.orm.Tb_autore;
import it.iccu.sbn.ejb.model.unimarcmodel.A200;
import it.iccu.sbn.ejb.model.unimarcmodel.A210;
import it.iccu.sbn.ejb.model.unimarcmodel.A210_GType;
import it.iccu.sbn.ejb.model.unimarcmodel.A300;
import it.iccu.sbn.ejb.model.unimarcmodel.AutorePersonaleType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.EnteType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.Indicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoNomeAutore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;

/**
 * Classe ChiaviAutori
 * <p>
 * Estratta dalle routine progress sbeleaut.p Simile a Isbd, ma relativa ad
 * autori.
 * </p>
 *
 * @author Akros Informatica s.r.l.
 * @author Ragazzini Taymer
 *
 * @version 30-ott-02
 */
public class TipiAutore {

	static Category log = Category.getInstance("iccu.box.TipiAutore");
	boolean sw_fine_intest = false;
	boolean sw_fine_qualif = false;
	String aa_nascita;
	String aa_morte;

	public TipiAutore() {
	}

	/**
	 * Controlla se un autore è di tipo Personale
	 */
	public boolean isPersonale(Tb_autore autore) {
		String tipo = autore.getTP_NOME_AUT();
		if (tipo.equals("A") || tipo.equals("B") || tipo.equals("C")
				|| tipo.equals("D"))
			return true;
		return false;
	}

/**
     * Legge le informazioni contenute in Tb_autore e ne costruisce l'elemento XML A200
     * Questo metodo toglie la punteggiatura, ne faccio un altro che la lascia.
     * /
    public A200 calcolaT200(Tb_autore autore) {
    	String tipoAut = autore.getTP_NOME_AUT();
    	String ds_nome = autore.getDs_nome_aut();
    	A200 a200 = new A200();
    	if (tipoAut.equals("A") || tipoAut.equals("B")) {
    		a200.setId2(Indicatore.valueOf("0"));
    		int lun = ds_nome.indexOf(" <");
    		if (lun >= 0)
    			a200.setA_200(ds_nome.substring(0, lun));
    		else
    			a200.setA_200(ds_nome);
    	} else if (tipoAut.equals("C") || tipoAut.equals("D")) {
    		a200.setId2(Indicatore.valueOf("1"));
    		int lun = ds_nome.indexOf(", ");
    		if (lun >= 0) {
    			a200.setA_200(ds_nome.substring(0, lun));
    			int lun2 = ds_nome.indexOf(" <");
    			/////////////non so se ci va lo spazio
    			if (lun2 >= 0)
    				a200.setB_200(ds_nome.substring(lun + 2, lun2 - 1));
    			else
    				a200.setB_200(ds_nome.substring(lun + 2));
    		} else
    			a200.setA_200(ds_nome);
    	}
    	int lt = ds_nome.indexOf("<");
    	int gt = ds_nome.indexOf(">");
    	if (lt >= 0 && gt >= 0) {
            Note note = new Note(ds_nome.substring(lt+1, gt ));
            note.elabora();
            a200.setC_200(note.getC200());
            a200.setF_200(note.getF200());
    	}

    	return a200;
    }

    /**
     * Legge le informazioni contenute in Tb_autore e ne costruisce l'elemento XML A200
     */
	public A200 calcolaT200(Tb_autore autore) {
		String tipoAut = autore.getTP_NOME_AUT();
		String ds_nome = autore.getDS_NOME_AUT();
		A200 a200 = new A200();
		if (tipoAut.equals("A") || tipoAut.equals("B")) {
			a200.setId2(Indicatore.valueOf("0"));
			int lun = ds_nome.indexOf(" <");
			if (lun >= 0)
				a200.setA_200(ds_nome.substring(0, lun));
			else
				a200.setA_200(ds_nome);
		} else if (tipoAut.equals("C") || tipoAut.equals("D")) {
			a200.setId2(Indicatore.valueOf("1"));
			// MANTIS 4993 almaviva TEST  ERRORE SPAZI
			// int lun = ds_nome.indexOf(", ");
			int lun = ds_nome.indexOf(",");
			if (lun >= 0) {
				a200.setA_200(ds_nome.substring(0, lun));
				// MANTIS 4993 almaviva TEST  ERRORE SPAZI
				// int lun2 = ds_nome.indexOf(" <");
				int lun2 = ds_nome.indexOf("<");
				// ///////////non so se ci va lo spazio
				if (lun2 >= 0) {
					if (lun < lun2)
						a200.setB_200(ds_nome.substring(lun, lun2));
				} else
					a200.setB_200(ds_nome.substring(lun));
			} else
				a200.setA_200(ds_nome);
		}
		int lt = ds_nome.indexOf("<");
		int gt = ds_nome.indexOf(">");
		if (lt >= 0 && gt >= 0) {
			Note note = new Note(ds_nome.substring(lt + 1, gt));
			note.elabora();
			a200.setC_200(note.getC200());
			a200.setF_200(note.getF200());
		}

		return a200;
	}

/**
     * Legge le informazioni contenute in Tb_autore e ne costruisce l'elemento XML A210
     * Non mette la punteggiatura, lo rifaccio
     * /
    public A210 calcolaT210(Tb_autore autore) {
    	String tipoAut = autore.getTP_NOME_AUT(); //E,R o G
    	String ds_nome = autore.getDs_nome_aut();
    	A210 a210 = new A210();
    	if (tipoAut.equals("E")) {
    		a210.setId1(Indicatore.valueOf("0"));
    		a210.setId2(Indicatore.valueOf("2"));
    	}
    	if (tipoAut.equals("R"))
    		a210.setId1(Indicatore.valueOf("1"));
    	a210.setId2(Indicatore.valueOf("0")); ///Viene ignorato ?
    	if (tipoAut.equals("E") || tipoAut.equals("R")) {
    		int lun = ds_nome.indexOf(" <");
    		if (lun >= 0) {
    			a210.setA_210(ds_nome.substring(0, lun));
    			ds_nome = ds_nome.substring(lun + 2);
    			sw_fine_qualif = false;
    			sw_fine_intest = false;
    			while (!sw_fine_qualif)
    				ds_nome = elab_qualif(a210, ds_nome);
    		} else
    			a210.setA_210(ds_nome);
    	} else if (tipoAut.equals("G")) {
    		a210.setId1(Indicatore.valueOf("0"));
    		a210.setId2(Indicatore.valueOf("1"));
    		int min = ds_nome.indexOf(" <");
    		int duep = ds_nome.indexOf(" : ");
    		int sub = -1, caratt = 0;
    		if (duep >= 0) {
    			sub = duep;
    			caratt = 3;
    		}
    		if (min >= 0) {
    			sub = min;
    			caratt = 2;
    		}
    		if (sub >= 0) {
    			a210.setA_210(ds_nome.substring(0, sub));
    			ds_nome = ds_nome.substring(sub + caratt);
    			if (sub == min) {
    				sw_fine_qualif = false;
    				sw_fine_intest = false;
    				while (!sw_fine_qualif && !sw_fine_intest)
    					ds_nome = elab_qualif(a210, ds_nome);
    				while (!sw_fine_intest)
    					ds_nome = elab_subordinati(a210, ds_nome);
    			} else if (sub == duep) {
    				while (!sw_fine_intest)
    					ds_nome = elab_subordinati(a210, ds_nome);
    			}
    		} else
    			a210.setA_210(ds_nome);
    		int total = 0, counter = 0;
    		/*while ((counter = ds_nome.indexOf(":", counter + 1)) > 0)
    		    total++;
    		String [] c200 = new String[total];
    		counter = 0;
    		while (min > 0)  {
    		    if ((min = ds_nome.indexOf(":",min+1))>0) a210.setB_210(counter,ds_nome.substring(sub,min));
    		    else if (duep>0) a210.setB_210(counter,ds_nome.substring(min,duep));
    		    counter++;
    		}
    		* /
    	}
    	int lt = ds_nome.indexOf("<");
    	int gt = ds_nome.indexOf(">");
    	if (lt >= 0 && gt >= 0) {
            Note note = new Note(ds_nome.substring(lt, gt + 1));
            note.elabora();
            a200.setC_200(note.getC200());
            a200.setF_200(note.getF200());
    	}
    	return a210;
    }

    /**
     * Legge le informazioni contenute in Tb_autore e ne costruisce l'elemento XML A210
     */
	public A210 calcolaT210(Tb_autore autore) {
		String tipoAut = autore.getTP_NOME_AUT(); // E,R o G
		String ds_nome = autore.getDS_NOME_AUT();
		A210 a210 = new A210();
		if (tipoAut.equals("R")) {
			a210.setId1(Indicatore.valueOf("1"));
			a210.setId2(Indicatore.valueOf("2"));
			int lun = ds_nome.indexOf(" <");
			if (lun >= 0) {
				a210.setA_210(ds_nome.substring(0, lun));
				elab_qualifR(a210, ds_nome.substring(lun + 2));
			} else
				a210.setA_210(ds_nome);
			return a210;
		} else if (tipoAut.equals("E")) {
			a210.setId1(Indicatore.valueOf("0"));
			a210.setId2(Indicatore.valueOf("2"));
			int lun = ds_nome.indexOf(" <");
			if (lun >= 0 && ds_nome.indexOf('>', lun) == ds_nome.length() - 1) {
				// se c'è ' <' e non c'è qualcosa dopo '>'
				a210.setA_210(ds_nome.substring(0, lun));
				ds_nome = ds_nome.substring(lun + 2);
				sw_fine_qualif = false;
				sw_fine_intest = false;
				while (!sw_fine_qualif)
					ds_nome = elab_qualif(a210, ds_nome);
			} else {
				// se c'è qualcosa dopo '>'
				a210.setA_210(ds_nome);
			}
			return a210;
		} else if (tipoAut.equals("G")) {
			a210.setId1(Indicatore.valueOf("0"));
			a210.setId2(Indicatore.valueOf("2"));
			int ind1;
			int counter = 0;
			do {
				ind1 = ds_nome.indexOf(" : ");
				if (ind1 >= 0) {
					elab_subordinati(a210, ds_nome.substring(0, ind1), counter);
					ds_nome = ds_nome.substring(ind1 + 3);
				} else
					elab_subordinati(a210, ds_nome, counter);
				counter++;
			} while (ind1 >= 0);
			// Non ci va nient'altro
			return a210;
		}
		int lt = ds_nome.indexOf("<");
		int gt = ds_nome.indexOf(">");
		if (lt >= 0 && gt >= 0) {
			Note note = new Note(ds_nome.substring(lt + 1, gt));
			note.elabora();
			// Forse andrebbero aggiunti, se ce ne sono altri.
			a210.setC_210(note.getC200());
			a210.setF_210(note.getF200());
		}
		return a210;
	}

	private String elab_qualif(A210 a210, String residua) {
		String c;
		int ind1 = residua.indexOf(" ; ");
		int ind2 = residua.indexOf(">");
		int min = ind2;
		int lung = 3;
		if (ind1 >= 0)
			if (ind2 >= 0) {
				if (ind1 < ind2)
					min = ind1;
			} else
				min = ind1;
		if (min == ind2) {
			sw_fine_qualif = true;
			lung = 1;
		}
		if (min < 0) {
			sw_fine_intest = true;
			sw_fine_qualif = true;
			c = residua.substring(0, residua.length());
			residua = "";
		} else {
			c = residua.substring(0, min);
			residua = residua.substring(min + lung);
		}
		if (Note.isDate(c))
			a210.setF_210(c);
		else
			a210.addC_210(c);
		return residua;
	}

	private String elab_qualifR(A210 a210, String residua) {
		int indfine = residua.indexOf(">");
		if (indfine > 0)
			residua = residua.substring(0, indfine);
		int ind1 = residua.indexOf(". ");
		boolean tuttoInC = false;
		if (ind1 >= 0) {
			int end = residua.indexOf(" ; ", ind1);
			if (end > 0) { // Termina con " ; "
				ind1 = end;
				end = end + 3;
			} else { // Termina con ". "
				end = ind1 + 2;
			}
			a210.addD_210(residua.substring(0, ind1));
			residua = residua.substring(end);
			// }
			// Inizio modifica almaviva2 27.09.2010 BUG MANTIS 3902
			// preso da Indice l'else e la gestione del campo tuttoInC
		} else {
			// Se ci sono tre campi ma manca il punto metto tutto in c
			int counter = 0;
			ind1 = -1;
			while ((ind1 = residua.indexOf(" ; ", ind1 + 1)) >= 0) {
				counter++;
			}
			if (counter == 2) {
				tuttoInC = true;
			}
		}
		// Fine modifica almaviva2 27.09.2010 BUG MANTIS 3902

		while (residua.length() > 0) {
			String temp;
			int end = residua.indexOf(" ; ");
			if (end > 0) {
				temp = residua.substring(0, end);
				residua = residua.substring(end + 3);
			} else {
				temp = residua;
				residua = "";
			}
			// Inizio modifica almaviva2 27.09.2010 BUG MANTIS 3902
			// preso da Indice l'else e la gestione del campo tuttoInC
			if (tuttoInC) {
				a210.addC_210(temp);
			} else {
				if (Note.isDate(temp))
					a210.setF_210(temp);
				else
					a210.addE_210(temp);
			}
		}
		return residua;
	}

	/**
	 * Gestisce gli autori di tipo G: nome dell'ente <qualif. 1 ; qualif2 ; ...>
	 * : nome dell'ente <qualif ....
	 */
	private void elab_subordinati(A210 a210, String residua, int counter) {
		A210_GType a210G = new A210_GType();

		int ind = residua.indexOf(" <");
		if (ind >= 0) {
			int fine = residua.indexOf(">");
			boolean elabora = true;
			if (counter == 0) {
				if (fine > 0 && fine < residua.length() - 1) {
					a210.setA_210(residua);
					elabora = false;
				} else {
					a210.setA_210(residua.substring(0, ind));
				}
			} else {
				a210G.setB_210(residua.substring(0, ind));
			}
			if (elabora) {
				if (fine < 0)
					fine = residua.length();
				String qualif = residua.substring(ind + 2, fine);
				int ind2 = qualif.indexOf(" ; ");
				while (ind2 >= 0) {
					if (counter == 0)
						a210.addC_210(qualif.substring(0, ind2));
					else
						a210G.addC_210(qualif.substring(0, ind2));
					qualif = qualif.substring(ind2 + 3);
					ind2 = qualif.indexOf(" ; ");
				}
				if (counter == 0)
					a210.addC_210(qualif);
				else
					a210G.addC_210(qualif);
			}
		} else {
			if (counter == 0)
				a210.setA_210(residua);
			else
				a210G.setB_210(residua);
		}
		if (counter != 0)
			a210.addA210_G(a210G);
	}

	/**
	 * Metodo principale di creazione di un autore da un elemento xml.
	 * Costruisce un oggetto Tb_autore leggendo i dati da datiEl
	 * @throws InfrastructureException
	 */
	public Tb_autore costruisciAutore(DatiElementoType datiEl, Tb_autore autore)
			throws EccezioneSbnDiagnostico, EccezioneDB, InfrastructureException {
		String tipo;
		String nome;
		String paese = null;
		String lingua = null;
		String isadn = null;
		aa_nascita = null;
		aa_morte = null;
		if (autore == null)
			autore = new Tb_autore();
		if (datiEl instanceof AutorePersonaleType) {
			AutorePersonaleType autoreType = (AutorePersonaleType) datiEl;
			tipo = valutaTipoAutore(autoreType);
			nome = valutaNomeAutore(autoreType, tipo);
			// Marzo 2016: gestione ISNI (International standard number identifier)
			if (autoreType.getT010() != null)
                isadn = autoreType.getT010().getA_010();

			if (autoreType.getT102() != null)
				paese = autoreType.getT102().getA_102();
			if (autoreType.getT101() != null
					&& autoreType.getT101().getA_101Count() > 0)
				lingua = autoreType.getT101().getA_101(0);
			estraiAnni(autoreType.getT300());
			autore = calcolaAutore(autoreType, autore);
		} else if (datiEl instanceof EnteType) {
			EnteType ente = (EnteType) datiEl;
			tipo = valutaTipoAutore(ente);
			nome = valutaNomeAutore(ente, tipo);
			// Marzo 2016: gestione ISNI (International standard number identifier)
			 if (ente.getT010() != null)
	             isadn = ente.getT010().getA_010();

			if (ente.getT102() != null)
				paese = ente.getT102().getA_102();
			if (ente.getT101() != null && ente.getT101().getA_101Count() > 0)
				lingua = ente.getT101().getA_101(0);
			new AutoreValida().validaDescrizioneEnti(nome);
			estraiAnni(ente.getT300());
			autore = calcolaAutore(ente, autore);
		} else {
			log.error("Tipo di autore non conosciuto");
			throw new EccezioneSbnDiagnostico(3045);
		}
		if (paese != null) {
			if (Decodificatore.getCd_tabella("Tb_autore", "cd_paese",
					paese.toUpperCase()) == null)
				throw new EccezioneSbnDiagnostico(3205); //Codice paese errato

			Date now = new Date(System.currentTimeMillis());
			SbnData dataNow = new SbnData(now);

			SbnData dtFineVal = Decodificatore.getDt_finevalidita("PAES", paese.toUpperCase());
			if (dtFineVal.getAnno() != null) {
				if (dtFineVal.compareTo(dataNow) < 0) {
					throw new EccezioneSbnDiagnostico(7020,
							"Errore: Il Codice Paese utilizzato non è più valido");
				}
			}

		}

		if (lingua != null)
			if (Decodificatore.getCd_tabella("Tb_autore", "cd_lingua",
					lingua.toUpperCase()) == null)
				throw new EccezioneSbnDiagnostico(3204, "Codice lingua errato");
		ChiaviAutore ca = new ChiaviAutore(tipo, nome);
		if (!ca.calcolaChiavi()) {
			log.error("Errore nella generazione delle chiavi dell'autore: "
					+ nome);
			throw new EccezioneSbnDiagnostico(3043);
		}
		// il cd livello potrebbe forse essere spostato.
		autore.setCD_LIVELLO(datiEl.getLivelloAut().toString());
		autore.setTP_NOME_AUT(tipo);
		autore.setDS_NOME_AUT(nome);
		autore.setKY_AUTEUR(ca.getKy_auteur());
		autore.setKY_CAUTUN(ca.getKy_cautun());
		autore.setKY_CLES1_A(ca.getKy_cles1_A());
		autore.setKY_CLES2_A(ca.getKy_cles2_A());
		autore.setKY_EL1_A(ca.getKy_el1a());
		autore.setKY_EL1_B(ca.getKy_el1b());
		autore.setKY_EL2_A(ca.getKy_el2a());
		autore.setKY_EL2_B(ca.getKy_el2b());
		autore.setKY_EL3(ca.getKy_el3());
		autore.setKY_EL4(ca.getKy_el4());
		autore.setKY_EL5(ca.getKy_el5());
		autore.setFL_SPECIALE(" ");
		if (aa_nascita != null) {
			while (aa_nascita.length() < 4)
				aa_nascita = " " + aa_nascita;
			if (aa_nascita.length() > 4) {
				aa_nascita = aa_nascita.substring(0, 4);
			}
			autore.setAA_NASCITA(aa_nascita);
			AnnoDate anno = new AnnoDate(autore.getAA_NASCITA());
			if (anno.getIntAnnoDate() == 0) {
				autore.setAA_NASCITA(null);
			}
		} else {
			autore.setAA_NASCITA(null);
		}
		if (aa_morte != null) {
			while (aa_morte.length() < 4)
				aa_morte = " " + aa_morte;
			if (aa_morte.length() > 4) {
				aa_morte = aa_morte.substring(0, 4);
			}
			autore.setAA_MORTE(aa_morte);
		} else {
			autore.setAA_MORTE(null);
		}

		// Marzo 2016: gestione ISNI (International standard number identifier)
		//il campo ISNI è di 16 caratteri: i primi 15 solo numerici e l'ultimo può essere anche 'x'
		if (isadn != null) {
			String patternA = "[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9xX]";
			org.apache.oro.text.regex.Perl5Compiler compiler = new org.apache.oro.text.regex.Perl5Compiler();
			org.apache.oro.text.regex.Perl5Matcher matcher = new org.apache.oro.text.regex.Perl5Matcher();
			Pattern pattern = null;
			try {
				   pattern = compiler.compile(patternA);
				 } catch(MalformedPatternException e) {
				   System.out.println("Bad pattern.");
				 }
				boolean foundA  = matcher.matches(isadn, pattern);
				if(foundA || isadn.equals("")) { // TUTTO OK
				} else {
					throw new EccezioneSbnDiagnostico(2900, "Numero ISNI errato");
				}
        }


		autore.setFL_CANC("N");
		if (datiEl.getFormaNome() != null)
			autore.setTP_FORMA_AUT(datiEl.getFormaNome().toString());
		else
			autore.setTP_FORMA_AUT("A");
		// autore.setVid(datiEl.getT001());

		return autore;
	}

	/**
	 * Setta i parametri di un autore caratteristici del tipo
	 * AutorePersonaleType
	 * @throws InfrastructureException
	 */
	private Tb_autore calcolaAutore(AutorePersonaleType datiEl, Tb_autore autore)
			throws EccezioneDB, InfrastructureException {
		if (datiEl.getT101() != null && datiEl.getT101().getA_101Count() > 0) {
			autore.setCD_LINGUA(Decodificatore.getCd_tabella("Tb_autore",
					"cd_lingua", datiEl.getT101().getA_101(0).toUpperCase()));
		} else {
			autore.setCD_LINGUA(null);
		}
		if (datiEl.getT102() != null) {
			autore.setCD_PAESE(Decodificatore.getCd_tabella("Tb_autore",
					"cd_paese", datiEl.getT102().getA_102().toUpperCase()));
		} else {
			autore.setCD_PAESE(null);
		}
		if (datiEl.getT801() != null
				&& Integer.parseInt(datiEl.getLivelloAut().toString()) > 95) {
			autore.setCD_AGENZIA(datiEl.getT801().getA_801()
					+ datiEl.getT801().getB_801());
		} else {
			autore.setCD_AGENZIA("ITICCU");
		}
		if (datiEl.getT152() != null) {
			autore.setCD_NORME_CAT(datiEl.getT152().getA_152());
		} else {
			autore.setCD_NORME_CAT(null);
		}

		// Inizio Mantis esercizio 0005896: almaviva2: Il trattamento dell'ISADN è stato ripreso da quello dell'Indice ma
		// deve seguire regole particolari: nei casi di variazione, allineamento, cattura  il valore DEVE essere quello portato
		// dall'INDICE contenuto nel campo datiEl.getT015(); negli altri casi deve essere sempre uguale a null

//		if (autore.getISADN() != null) {
//			autore.setISADN(autore.getISADN());
//		} else if (autore.getISADN() == null
//				&& datiEl.getLivelloAut().getType() == SbnLivello.valueOf("97")
//						.getType()) {
//			autore.setISADN(calcolaIsadn());
//		} else {
//			autore.setISADN(null);
//		}


		// Marzo 2016: gestione ISNI (International standard number identifier)
//		if (datiEl.getT015() != null) {
//			autore.setISADN(datiEl.getT015().getA_015());
//		} else {
//			autore.setISADN(null);
//		}
		// Fine Mantis esercizio 0005896: almaviva2:

		 if(datiEl.getT010() != null ){
	       	autore.setISADN(datiEl.getT010().getA_010());
	     } else {
	    	 autore.setISADN(null);
	     }
		// Marzo 2016: gestione ISNI (International standard number identifier)



		if (datiEl.getT300() != null) {
			autore.setNOTA_AUT(datiEl.getT300().getA_300());
		} else {
			autore.setNOTA_AUT(null);
		}
		if (datiEl.getT830() != null) {
			autore.setNOTA_CAT_AUT(datiEl.getT830().getA_830());
		} else {
			autore.setNOTA_CAT_AUT(null);
		}
		return autore;
	}

	/** Setta i parametri di un autore caratteristici del tipo EnteType
	 * @throws InfrastructureException */
	public Tb_autore calcolaAutore(EnteType datiEl, Tb_autore autore)
			throws EccezioneDB, InfrastructureException {
		if (datiEl.getT101() != null && datiEl.getT101().getA_101Count() > 0) {
			// Da modificare
			autore.setCD_LINGUA(Decodificatore.getCd_tabella("Tb_autore",
					"cd_lingua", datiEl.getT101().getA_101(0).toUpperCase()));
		} else {
			autore.setCD_LINGUA(null);
		}
		if (datiEl.getT102() != null) {
			autore.setCD_PAESE(Decodificatore.getCd_tabella("Tb_autore",
					"cd_paese", datiEl.getT102().getA_102().toUpperCase()));
		} else {
			autore.setCD_PAESE(null);
		}
		if (datiEl.getT801() != null
				&& Integer.parseInt(datiEl.getLivelloAut().toString()) > 95) {
			autore.setCD_AGENZIA(datiEl.getT801().getA_801()
					+ datiEl.getT801().getB_801());
		} else {
			autore.setCD_AGENZIA("ITICCU");
		}
		if (datiEl.getT152() != null) {
			autore.setCD_NORME_CAT(datiEl.getT152().getA_152());
		} else {
			autore.setCD_NORME_CAT(null);
		}

		// Inizio Mantis esercizio 0005896: almaviva2: Il trattamento dell'ISADN è stato ripreso da quello dell'Indice ma
		// deve seguire regole particolari: nei casi di variazione, allineamento, cattura  il valore DEVE essere quello portato
		// dall'INDICE contenuto nel campo datiEl.getT015(); negli altri casi deve essere sempre uguale a null

//		if (autore.getISADN() != null) {
//			autore.setISADN(autore.getISADN());
//		} else if (autore.getISADN() == null
//				&& datiEl.getLivelloAut().getType() == SbnLivello.valueOf("97")
//						.getType()) {
//			autore.setISADN(calcolaIsadn());
//		} else {
//			autore.setISADN(null);
//		}

		if (datiEl.getT015() != null) {
			autore.setISADN(datiEl.getT015().getA_015());
		} else {
			autore.setISADN(null);
		}
		// Fine Mantis esercizio 0005896: almaviva2:

		if (datiEl.getT300() != null) {
			autore.setNOTA_AUT(datiEl.getT300().getA_300());
		} else {
			autore.setNOTA_AUT(null);
		}
		if (datiEl.getT830() != null) {
			autore.setNOTA_CAT_AUT(datiEl.getT830().getA_830());
		} else {
			autore.setNOTA_CAT_AUT(null);
		}
		return autore;
	}

/*
	private String calcolaIsadn() throws EccezioneDB, InfrastructureException {
		Progressivi progr = new Progressivi();
		return progr.getNextIsadnAutore();
	}
*/

	public String valutaTipoAutore(AutorePersonaleType autore)
			throws EccezioneSbnDiagnostico {
		SbnTipoNomeAutore tipo = autore.getTipoNome();
		if (tipo != null)
			return tipo.toString();
		if (autore.getT200() == null || autore.getT200().getA_200() == null)
			throw new EccezioneSbnDiagnostico(3045,
					"Tipo di autore non conosciuto.");
		String a_200 = autore.getT200().getA_200().trim();
		int n = a_200.indexOf(" : ");
		if (n >= 0)
			a_200 = a_200.substring(0, n);
		if (autore.getT200().getId2() == null
				|| autore.getT200().getId2().getType() == Indicatore.valueOf(
						" ").getType()
				|| autore.getT200().getId2().getType() == Indicatore.valueOf(
						"0").getType())
			if (a_200.indexOf(" ") > 0)
				return "B";
			else
				return "A";
		else if (autore.getT200().getId2().getType() == Indicatore.valueOf("1")
				.getType()) {
			if (autore.getT200().getB_200() == null
					|| autore.getT200().getB_200().trim().equals("")) {
				int virgola = a_200.indexOf(",");
				if (virgola >= 0) {
					a_200 = a_200.substring(0, virgola);
				} else {
					throw new EccezioneSbnDiagnostico(3056,
							"Tipo di autore incongruente.");
				}
			}
			if (a_200.indexOf(" ") > 0 || a_200.indexOf("-") > 0
					|| a_200.indexOf("#") > 0)
				return "D";
			else
				return "C";
		}
		throw new EccezioneSbnDiagnostico(3045,
				"Tipo di autore non conosciuto.");

	}

	/**
	 * Estrae il nome dell'autore partendo da un tipo autorepersonaleType
	 */
	public String valutaNomeAutore(AutorePersonaleType autore, String tipoAutore)
			throws EccezioneSbnDiagnostico {
		if (autore.getT200() == null)
			throw new EccezioneSbnDiagnostico(3290, "Nome errato");
		// return null;
		if (tipoAutore == null)
			tipoAutore = valutaTipoAutore(autore);
		String nome = autore.getT200().getA_200().trim();
		if (nome.length() == 0) {
			throw new EccezioneSbnDiagnostico(3290, "Nome errato");
		}
		String b_200 = autore.getT200().getB_200();
		String[] c_200 = autore.getT200().getC_200();
		String f_200 = autore.getT200().getF_200();
		if (b_200 != null && !b_200.equals("")) {
			if (tipoAutore.equals("C") || tipoAutore.equals("D")) {
				if (b_200.startsWith(", "))
					b_200 = b_200.substring(2);
				else if (b_200.startsWith(",") && b_200.length() > 1)
					throw new EccezioneSbnDiagnostico(3235,
							"Punteggiatura errata");
				if (!nome.endsWith(","))
					nome += ",";
			} else {
				throw new EccezioneSbnDiagnostico(3262, "Nome non congruente");
			}
			nome += " " + b_200;
		} else {
			if ((tipoAutore.equals("C") || tipoAutore.equals("D"))
					&& nome.indexOf(",") < 0) {
				throw new EccezioneSbnDiagnostico(3262, "Nome non congruente");
			} else if ((tipoAutore.equals("A") || tipoAutore.equals("B"))
					&& nome.indexOf(",") >= 0) {
				throw new EccezioneSbnDiagnostico(3262, "Nome non congruente");
			}
		}
		nome = nome.trim();
		// per tipoAutore "A" opp "C"
		// non deve sostituire il carattere "-"
		// String nomeTemp = nome.replace('-', ' ').replace('#', ' ');
		// MANTIS 1763
		String nomeTemp = nome.replace('#', ' ');
		if (!tipoAutore.equals("C") && !tipoAutore.equals("A"))
			nomeTemp = nomeTemp.replace('-', ' ');

		if (nomeTemp.indexOf(" : ") >= 0) {
			nomeTemp = nomeTemp.substring(0, nomeTemp.indexOf(" : "));
		}
		if (nomeTemp.indexOf(" <") >= 0) {
			nomeTemp = nomeTemp.substring(0, nomeTemp.indexOf(" <"));
		}
		int spazio = nomeTemp.indexOf(' ');
		int virgola = nomeTemp.indexOf(',');
		if (tipoAutore.equals("D")) {
			if (spazio < 0 || spazio > virgola) {
				throw new EccezioneSbnDiagnostico(3262, "Nome non congruente");
			}
		} else if (tipoAutore.equals("C")) {
			if ((spazio < 0 || spazio > virgola) == false) {
				throw new EccezioneSbnDiagnostico(3262, "Nome non congruente");
			}
		} else if (tipoAutore.equals("B")) {
			if (virgola > 0 || spazio < 0) {
				throw new EccezioneSbnDiagnostico(3262, "Nome non congruente");
			}
		} else if (tipoAutore.equals("A")) {
			// almaviva4 28/07/2011
			if (nomeTemp.indexOf("*") >= 0) {
				nomeTemp = nomeTemp.substring(nomeTemp.indexOf("*") + 1);
				spazio = nomeTemp.indexOf(' ');
				virgola = nomeTemp.indexOf(',');
			}
			// almaviva4 28/07/2011 fine
			if (virgola > 0 || spazio > 0) {
				throw new EccezioneSbnDiagnostico(3262, "Nome non congruente");
			}
		}
		String tra_angolari = "";
		if (c_200 != null && c_200.length > 0) {
			for (int i = 0; i < c_200.length; i++)
				if (i == 0)
					tra_angolari += c_200[i];
				else
					tra_angolari += " ; " + c_200[i];
		}
		if (f_200 != null) {
			if (tra_angolari.length() > 0)
				tra_angolari += " ; ";
			tra_angolari += f_200;
		}
		if (tra_angolari.length() > 0) {
			// Controllo che le parentesi angolari non siano già nel nome
			int unc = tra_angolari.indexOf('<');
			if (unc >= 0) {
				tra_angolari = tra_angolari.substring(unc + 1);
			}
			unc = tra_angolari.indexOf('>');
			if (unc >= 0) {
				tra_angolari = tra_angolari.substring(0, unc);
			}
			nome += " <" + tra_angolari + ">";
		}
		return nome;
	}

	/**
	 * Estrae gli anni di nasicta e di morte dalle note
	 *
	 */
	protected void estraiAnni(A300 a300) { // throws eccezione ...
		if (a300 == null)
			return;
		String nota = a300.getA_300();
		if (nota == null)
			return;
		nota = nota.trim();
		int end = nota.indexOf("//");
		if (end > 0)
			nota = nota.substring(0, end);
		else
			return;
		// Tolgo la '<' iniziale e gli spazi che la seguono:
		nota = nota.trim();
		int lung = nota.length();
		if (nota.startsWith("m.")) {
			int n = 3;
			while (!Character.isDigit(nota.charAt(n)))
				n++;
			aa_morte = nota.substring(n);
		} else if (nota.startsWith("n.")) {
			int n = 3;
			// Manutenzione Gestione Bibliografica 19.09.2011 almaviva2 BUG
			// MANTIS esercizio 4624
			// si inserisce il controllo sulla lunghezza del campo nota
			// altrimenti si incorre nell'errore di sfondamento
			// nell'estrazione dell'anno di nascita dalla nota;
			// while (!Character.isDigit(nota.charAt(n)))
			while (n < lung && !Character.isDigit(nota.charAt(n)))
				n++;
			aa_nascita = nota.substring(n);
		} else if (nota.startsWith("sec.")) {
			calcolaSecolo(nota.substring(4).trim());
		} else {
			int in = 0;
			while (in < lung && !Character.isDigit(nota.charAt(in)))
				in++;
			int fine = in;
			while (fine < lung && Character.isDigit(nota.charAt(fine)))
				fine++;
			aa_nascita = nota.substring(in, fine);
			in = fine;
			if (in < lung) {
				char car = nota.charAt(in);
				while (in < lung && !Character.isDigit(car) && car != ';') {
					car = nota.charAt(in);
					in++;
				}
				in--;
				if (Character.isDigit(car)) {
					fine = in;
					while (fine < lung && Character.isDigit(nota.charAt(fine)))
						fine++;
					aa_morte = nota.substring(in, fine);
				}
			}
		}
	}

	/**
	 * Calcolo gli anni di nascita a partire dal secolo, varie forme: sec. 14.
	 * --> 1301 - 1400 sec. 14. 1. metà --> 1301 - 1350 sec. 14. 2. metà -->
	 * 1351 - 1400 sec. 14. - 15. --> 1301 - 1500
	 *
	 * @param nota
	 */
	private void calcolaSecolo(String nota) {
		String nascita;
		String morte;
		int meno = nota.indexOf("-");
		if (meno > 0) {
			nascita = nota.substring(0, meno).trim();
			morte = nota.substring(meno + 1).trim();
			// manutenzione correttiva Bug mantis 0006245 esercizio almaviva2
			// corretti i calcoli della data di nascita e morte ricomposti dalla datazione
			// (Ripreso da Indice)
			// almaviva TOLGO SPAZIO IN TESTA ed elimino la parola Sec.
			morte = morte.replaceAll("^\\s+", "");
			if (morte.toLowerCase().startsWith("sec.") ) {
				morte = morte.substring(4);
				morte = morte.replaceAll("^\\s+", "");
			}

		} else {
			nascita = nota.trim();
			morte = null;
		}
		int n = 0;
		while (n < nascita.length() && Character.isDigit(nascita.charAt(n))) {
			n++;
		}
		int sec_nas = Integer.parseInt(nascita.substring(0, n));
		int sec_mor = -1;
		if (morte != null) {
			n = 0;
			while (n < morte.length() && Character.isDigit(morte.charAt(n))) {
				n++;
			}
			sec_mor = Integer.parseInt(morte.substring(0, n));
		}
		n = 2;
		while (n < nascita.length() && !Character.isDigit(nascita.charAt(n))) {
			n++;
		}
		if (n < nascita.length()) {// ho la metà
			char meta = nascita.charAt(n);
			if (meta == '1') {
				aa_nascita = "" + (sec_nas - 1) + "01";
				aa_morte = "" + (sec_nas - 1) + "50";
			} else {
				aa_nascita = "" + (sec_nas - 1) + "51";
				aa_morte = "" + sec_nas + "00";
			}
		} else {// non ho la metà, posso avere la morte
			aa_nascita = "" + (sec_nas - 1) + "01";
			if (sec_mor >= 0) {
				aa_morte = "" + sec_mor + "00";
			} else {
				aa_morte = "" + sec_nas + "00";
			}
		}
	}

	/** Valuta di quale tipo di autore si tratta */
	public String valutaTipoAutore(EnteType ente)
			throws EccezioneSbnDiagnostico {
		SbnTipoNomeAutore tipo = ente.getTipoNome();
		if (tipo != null)
			return tipo.toString();
		if (ente.getT210() != null) {
			if (ente.getT210().getId1() == null
					|| ente.getT210().getId1().getType() == Indicatore.valueOf(
							" ").getType()
					|| ente.getT210().getId1().getType() == Indicatore.valueOf(
							"0").getType()) {
				if (ente.getT210().getId2().getType() == Indicatore
						.valueOf("0").getType())
					return "E";
				else if (ente.getT210().getId2().getType() == Indicatore
						.valueOf("1").getType())
					if (ente.getT210().getA210_GCount() > 0)
						return "G";
					else
						return "E";
				else if (ente.getT210().getId2().getType() == Indicatore
						.valueOf("2").getType()) {
					if (ente.getT210().getA210_GCount() > 0)
						return "G";
					else
						return "E";
				}
			} else if (ente.getT210().getId1().getType() == Indicatore.valueOf(
					"1").getType())
				return "R";
		}
		log.error("Tipo di autore non conosciuto.");
		throw new EccezioneSbnDiagnostico(3045);
	}

	/** Valuta il nome dell'autore per un tipo EnteType */
	public String valutaNomeAutore(EnteType autore, String tipo)
			throws EccezioneSbnDiagnostico {
		A210 t210 = autore.getT210();
		if (t210 == null || t210.getA_210() == null
				|| t210.getA_210().trim().length() == 0)
			throw new EccezioneSbnDiagnostico(3290, "Nome errato");
		if (tipo == null)
			tipo = valutaTipoAutore(autore);
		String nome = (t210.getA_210() == null ? "" : t210.getA_210().trim());
		A210_GType[] a_210G = t210.getA210_G();
		String tra_angolari = "";
		for (int i = 0; i < t210.getC_210Count(); i++) {
			if (tra_angolari.length() > 0)
				tra_angolari += " ; ";
			tra_angolari += t210.getC_210(i);
		}
		if (tipo.equals("R")) {
			if (t210.getD_210Count() > 0) {
				if (tra_angolari.length() > 0)
					tra_angolari += " ; ";
				for (int i = 0; i < t210.getD_210Count(); i++) {
					tra_angolari += t210.getD_210(i);
					if (!tra_angolari.endsWith("."))
						if (!tra_angolari.endsWith(". ;"))
							tra_angolari += ".";
				}
			}
			if (t210.getF_210() != null) {
				if (tra_angolari.length() > 0
						&& !tra_angolari.trim().endsWith(" ;")
						&& !t210.getF_210().startsWith("; "))
					tra_angolari += " ; ";
				else if (tra_angolari.endsWith(" ;")
						|| t210.getF_210().startsWith("; "))
					tra_angolari += " ";
				tra_angolari += t210.getF_210();
			}
			for (int i = 0; i < t210.getE_210Count(); i++) {
				if (tra_angolari.length() > 0
						&& !tra_angolari.trim().endsWith(" ;")
						&& !t210.getE_210(i).startsWith("; "))
					tra_angolari += " ; ";
				else if (tra_angolari.endsWith(" ;")
						|| t210.getE_210(i).startsWith("; "))
					tra_angolari += " ";
				tra_angolari += t210.getE_210(i);
			}
		} else if (tipo.equals("E")) {
			if (t210.getD_210Count() > 0) {
				if (tra_angolari.length() > 0)
					tra_angolari += " ; ";
				for (int i = 0; i < t210.getD_210Count(); i++) {
					tra_angolari += t210.getD_210(i);
					if (!tra_angolari.endsWith("."))
						if (!tra_angolari.endsWith(". ;"))
							tra_angolari += ".";
				}
			}
			for (int i = 0; i < t210.getE_210Count(); i++) {
				if (tra_angolari.length() > 0
						&& !tra_angolari.trim().endsWith(" ;")
						&& !t210.getE_210(i).startsWith("; "))
					tra_angolari += " ; ";
				else if (tra_angolari.endsWith(" ;")
						|| t210.getE_210(i).startsWith("; "))
					tra_angolari += " ";
				tra_angolari += t210.getE_210(i);
			}
			if (t210.getF_210() != null) {
				if (tra_angolari.length() > 0
						&& !tra_angolari.trim().endsWith(" ;")
						&& !t210.getF_210().startsWith("; "))
					tra_angolari += " ; ";
				else if (tra_angolari.endsWith(" ;")
						|| t210.getF_210().startsWith("; "))
					tra_angolari += " ";
				tra_angolari += t210.getF_210();
			}
		}

		if (tra_angolari.length() > 0) {
			// Controllo che le parentesi angolari non siano già nel nome
			int unc = tra_angolari.indexOf('<');
			if (unc >= 0) {
				tra_angolari = tra_angolari.substring(unc + 1);
			}
			unc = tra_angolari.indexOf('>');
			if (unc >= 0) {
				tra_angolari = tra_angolari.substring(0, unc);
			}
			nome += " <" + tra_angolari + ">";
		}

		// Questo ci dovrebbe essere se non c'è la parte prima
		if (a_210G != null && a_210G.length > 0) {
			for (int i = 0; i < a_210G.length; i++) {
				if (a_210G[i].getB_210() != null) {
					if ((a_210G[i].getB_210().startsWith(" : ") == false)) {
						nome += " : ";
					}
					nome += a_210G[i].getB_210();
				}
				if (a_210G[i].getC_210Count() > 0) {
					nome += " <";
					for (int c = 0; c < a_210G[i].getC_210Count(); c++) {
						if (c == 0)
							nome += a_210G[i].getC_210(c);
						else
							nome += " ; " + a_210G[i].getC_210(c);
					}
					nome += ">";
				}
			}
		} else {
			if (tipo.equals("G") && (a_210G == null || a_210G.length == 0)) {
				if (nome.indexOf(" : ") < 0) {
					throw new EccezioneSbnDiagnostico(3262, "Nome errato");
				}
			}
		}
		return nome;
	}

	/**
	 * Method calcolaAuteurECautun.
	 *
	 * @param autorePersonaleType
	 * @return Tb_autore
	 */
	public Tb_autore calcolaAuteurECautun(DatiElementoType datiEl)
			throws EccezioneSbnDiagnostico {
		String tipo, nome;
		if (datiEl instanceof AutorePersonaleType) {
			AutorePersonaleType autoreType = (AutorePersonaleType) datiEl;
			tipo = valutaTipoAutore(autoreType);
			nome = valutaNomeAutore(autoreType, tipo);
		} else if (datiEl instanceof EnteType) {
			EnteType ente = (EnteType) datiEl;
			tipo = valutaTipoAutore(ente);
			nome = valutaNomeAutore(ente, tipo);
		} else {
			log.error("Tipo di autore non conosciuto");
			throw new EccezioneSbnDiagnostico(3045);
		}
		ChiaviAutore ca = new ChiaviAutore(tipo, nome);
		if (!ca.calcolaChiavi()) {
			log.error("Errore nella generazione delle chiavi");
			throw new EccezioneSbnDiagnostico(3043);

		}
		Tb_autore autore = new Tb_autore();
		autore.setKY_AUTEUR(ca.getKy_auteur());
		autore.setKY_CAUTUN(ca.getKy_cautun());
		return autore;
	}

	public static Tb_autore controllaNomeAutore(DatiElementoType dati,
			String tipoAut) throws EccezioneSbnDiagnostico {
		try {
			TipiAutore ta = new TipiAutore();
			Tb_autore autore = new Tb_autore();
			ta.costruisciAutore(dati, autore);

			return autore;

		} catch (EccezioneSbnDiagnostico e) {
			throw e;
		} catch (EccezioneDB e) {
			throw new EccezioneSbnDiagnostico(1050);
		} catch (Exception e) {
			throw new EccezioneSbnDiagnostico(56);
		}
	}

}

class Note {
	protected String[] c200 = null;
	protected String f200 = null;
	private String stringa = null;

	public Note(String stringa) {
		this.stringa = stringa;
	}

	protected void elabora() {
		List<String> v = new ArrayList<String>();
		int n;
		String separatore = " ; ";
		int sepL = 3;
		while ((n = stringa.indexOf(separatore)) >= 0) {
			v.add(stringa.substring(0, n));
			stringa = stringa.substring(n + sepL);
		}
		v.add(stringa);
		String temp;
		for (int i = 0; i < v.size(); i++) {
			temp = v.get(i);
			if (isDate(temp)) {
				boolean tutteDate = true;
				for (int j = i; j < v.size(); j++) {
					if (!isDate(v.get(j))) {
						tutteDate = false;
						break;
					}
				}
				if (tutteDate) {
					v.remove(temp);
					f200 = temp;
					break;
				}
			}
		}
		c200 = new String[v.size()];
		for (int i = 0; i < c200.length; i++)
			c200[i] = v.get(i);
	}

	public static boolean isDate(String stringa) {
		// Correzione mantis 1154
		int slash = stringa.indexOf('/');
		if (slash > 0) {
			if (Character.isDigit(stringa.charAt(slash - 1))) {
				String s2 = stringa.substring(slash + 1);
				if (s2.length() > 0) {
					switch (s2.charAt(0)) {
					case 'i':
						;
					case 'p':
						;
					case 'm':
						;
					case 's':
						;
					case 'f':
						;
					case 't':
						;
						return true;
					default:
						break;
					}
				}
				// MANTIS 1861 inserito controllo se stringa diversa da ""
				if (!"".equals(s2)) {
					if (Character.isDigit(s2.charAt(0))) {
						return true;
					}
				}
			}
		}
		stringa = stringa.toLowerCase();
		if (stringa.startsWith("fl.") || stringa.startsWith("m.")
				|| stringa.startsWith("n.") || stringa.startsWith("sec.")
				|| stringa.startsWith("sec ") || stringa.indexOf("-nd") >= 0
				|| stringa.indexOf("nd-") >= 0 || stringa.startsWith("ca."))
			return true;
		if (stringa.startsWith(" -")) {
			// L'anno può essere preceduto da un ' -'
			stringa = stringa.substring(2);
		}
		for (int i = 0; i < 4 && i < stringa.length(); i++) {
			if (!Character.isDigit(stringa.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the c200.
	 *
	 * @return String[]
	 */
	public String[] getC200() {
		return c200;
	}

	/**
	 * Returns the f200.
	 *
	 * @return String
	 */
	public String getF200() {
		return f200;
	}

}
