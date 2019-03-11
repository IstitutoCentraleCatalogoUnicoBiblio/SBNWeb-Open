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

import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.profile.ValidatorContainerObject;
import it.finsiel.sbn.polo.factoring.profile.ValidatorProfiler;
import it.finsiel.sbn.polo.factoring.util.Decodificatore;
import it.finsiel.sbn.polo.factoring.util.ValidazioneDati;
import it.finsiel.sbn.polo.orm.Tr_soggettari_biblioteche;
import it.finsiel.sbn.polo.orm.Tr_thesauri_biblioteche;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_attivita;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_biblioteca_in_polo;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_bibliotecario;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_auth;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_mat;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_par_sem;
import it.finsiel.sbn.polo.orm.amministrazione.Tbf_parametro;
import it.finsiel.sbn.util.PoloUtility;
import it.iccu.sbn.ejb.model.unimarcmodel.Attivita;
import it.iccu.sbn.ejb.model.unimarcmodel.AttivitaAbilitateType;
import it.iccu.sbn.ejb.model.unimarcmodel.C2_250;
import it.iccu.sbn.ejb.model.unimarcmodel.Parametri;
import it.iccu.sbn.ejb.model.unimarcmodel.ParametriAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.ParametriDocumenti;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnProfileType;
import it.iccu.sbn.ejb.model.unimarcmodel.SbnUserType;
import it.iccu.sbn.ejb.model.unimarcmodel.SistemaClassificazione;
import it.iccu.sbn.ejb.model.unimarcmodel.SottoAttivita;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAbilitaOggetto;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAdesione;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnIndicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnMateriale;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoAllineamento;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoReticoloDoc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class FormatoSbnUser {


    private String _utente;
    private ValidatorProfiler validator;

    public FormatoSbnUser() {
       super();
    }

    private AttivitaAbilitateType[] formattaElencoAttivita(List _elencoAttivita) {
        int contaAttivita = 0;
        Tbf_attivita tavola, tb_tp_attivita;


        int size = ValidazioneDati.size(_elencoAttivita);
		for (int i = 0; i < size; i++) {
            tb_tp_attivita = new Tbf_attivita();
            tb_tp_attivita = (Tbf_attivita) _elencoAttivita.get(i);
            if (tb_tp_attivita.getCd_funzione_parent()== null)
                contaAttivita++;
        }


        AttivitaAbilitateType attivitaAbilitate[] = new AttivitaAbilitateType[contaAttivita];
        int j = 0;
        for (int i = 0; i < size; i++) {
            tavola = new Tbf_attivita();
            tavola = (Tbf_attivita) _elencoAttivita.get(i);
            if (tavola.getCd_funzione_parent()== null) {
                attivitaAbilitate[j] = new AttivitaAbilitateType();
                Attivita att = new Attivita();
                att.setContent(tavola.getId_attivita_sbnmarc().getDs_attivita().toString());
                att.setCodAttivita(tavola.getCd_attivita().toString());
                attivitaAbilitate[j].setAttivita(att);
                //attivitaAbilitate[j].setAttivita(tavola.getId_attivita_sbnmarc().getDs_attivita());
                List attivita_figlie_di = validator.getAttivitaFigli(_utente, tavola.getCd_attivita());
                if (attivita_figlie_di.size() != 0) {
                    //					log.debug("-- "+tavola.getDs_attivita());
                    for (int ind_att_sub = 0; ind_att_sub < attivita_figlie_di.size(); ind_att_sub++) {
                    	Tbf_attivita subAttivita = (Tbf_attivita)attivita_figlie_di.get(ind_att_sub);
                        SottoAttivita sAtt = new SottoAttivita();
                        sAtt.setContent(subAttivita.getId_attivita_sbnmarc().getDs_attivita());
                        sAtt.setCodAttivita(subAttivita.getCd_attivita());
                        attivitaAbilitate[j].addSottoAttivita(sAtt);
                    	 //attivitaAbilitate[j].addSottoAttivita(subAttivita.getId_attivita_sbnmarc().getDs_attivita());
                    }
                }
                j++;
            }
        }
        return attivitaAbilitate;
    }

    private Parametri formattaParametri(List _elencoParametri, SbnUserType sbnUserType) {
        Tbf_parametro tb_parametro;
        Parametri parametri = new Parametri();
        int size = ValidazioneDati.size(_elencoParametri);
		for (int i = 0; i < size; i++) {
            tb_parametro = (Tbf_parametro) _elencoParametri.get(i);
            String livello =
                Decodificatore.getCd_unimarc(
                    "Tb_parametro",
                    "cd_livello",
                    tb_parametro.getCd_livello().trim());
            parametri.setLivelloAutDoc(SbnLivello.valueOf(livello));
            String livelloAdesione =
                Decodificatore.getCd_unimarc(
                    "Tb_parametro",
                    "cd_liv_ade",
                    String.valueOf(tb_parametro.getCd_liv_ade()).trim());
            parametri.setLivelloAdesione(SbnAdesione.valueOf(livelloAdesione));
            parametri.setSpogliDiPeriodici(SbnIndicatore.valueOf(String.valueOf(tb_parametro.getFl_spogli())));
            parametri.setAutoriSuperflui(SbnIndicatore.valueOf(String.valueOf(tb_parametro.getFl_aut_superflui())));
            String tipoAllineamento =
                Decodificatore.getCd_unimarc(
                    "Tb_parametro",
                    "tp_all_pref",
                    tb_parametro.getTp_all_pref().trim());
            parametri.setTipoAllineamento(SbnTipoAllineamento.valueOf(tipoAllineamento));
            String tipoReticolo =
                Decodificatore.getCd_unimarc(
                    "Tb_parametro",
                    "tp_ret_doc",
                    tb_parametro.getTp_ret_doc().trim());
            parametri.setTipoReticoloDoc(SbnTipoReticoloDoc.valueOf(tipoReticolo));
        }


        List vettoreParametri =
            validator.getParametriSemantica(sbnUserType.getBiblioteca() + sbnUserType.getUserId());


        //LEGGO I TIPO DI SOGGETTARI DELLA BIBLIOTECA

        /*
         * 1) leggo la tabella tr_soggettari_biblioteche filtrata con la biblioteca dell'utente
         * 2) verifico record per record con par_sem per aggiungere il flag "sololocale"
         */

        Tbf_biblioteca_in_polo biblio = validator.getBiblioteca(sbnUserType.getBiblioteca().substring(0, 3), sbnUserType.getBiblioteca().substring(3));

        Iterator iter = biblio.getTr_soggettari_biblioteche().iterator();

        String user_profile = sbnUserType.getBiblioteca() +(sbnUserType.getUserId()==null?"      ":sbnUserType.getUserId());
        ValidatorContainerObject user =  validator.getAbilitazioni(user_profile);

        while(iter.hasNext())
		{
        	Tr_soggettari_biblioteche soggettari = (Tr_soggettari_biblioteche) iter.next();
        	boolean sololocale = user.isParametriSemantica("SOGG", soggettari.getCD_SOGG());
        	C2_250 c2_250 = new C2_250();
        	c2_250.setContent(Decodificatore.getCd_unimarc("Tb_soggetto", "cd_soggettario", soggettari.getCD_SOGG()));
        	c2_250.setSololocale(sololocale);
        	parametri.addC2_250(c2_250);
		}

        Iterator iter1 = biblio.getTr_thesauri_biblioteche().iterator();

        while(iter1.hasNext())
		{
        	Tr_thesauri_biblioteche thesauri = (Tr_thesauri_biblioteche) iter1.next();
        	boolean sololocale = user.isParametriSemantica("STHE", thesauri.getCD_THE());
        	C2_250 c2_250 = new C2_250();
        	c2_250.setContent(Decodificatore.getCd_unimarc("Tb_termine_thesauro", "cd_the", thesauri.getCD_THE()));
        	c2_250.setSololocale(sololocale);
        	parametri.addC2_250(c2_250);
		}


        for (int i = 0; i < vettoreParametri.size(); i++) {
            Tbf_par_sem parametro = (Tbf_par_sem) vettoreParametri.get(i);
            String tp_tabella_codici = parametro.getTp_tabella_codici().trim();
            String cd_tabella_codici = parametro.getCd_tabella_codici().trim();
//            if (tp_tabella_codici.equals("SOGG"))
//                parametri.addC2_250(
//                    Decodificatore.getCd_unimarc("Tb_soggetto", "cd_soggettario", cd_tabella_codici));
//            else
            	if (tp_tabella_codici.equals("SCLA"))
            	{
            		SistemaClassificazione sistema = new SistemaClassificazione();
            		sistema.setContent( Decodificatore.getCd_unimarc("Tb_classe", "cd_sistema", cd_tabella_codici));
            		boolean sololocale=false;
            		if(parametro.getSololocale()=='S')
        	        	sololocale=true;
            		sistema.setSololocale(sololocale);
                parametri.addSistemaClassificazione(sistema);
            	}
        }



        parametri.setSololocale(false);

        return parametri;
    }


    private ParametriAuthority[] formattaParametriAuthorityType(List _elencoParametriAuthority) {
        int size = ValidazioneDati.size(_elencoParametriAuthority);
		List<ParametriAuthority> parametriAuthorityType = new ArrayList<ParametriAuthority>(size);
        for (int i = 0; i < size; i++) {
            Tbf_par_auth tb_par_auth = (Tbf_par_auth) _elencoParametriAuthority.get(i);
            SbnAuthority tipoAut;
			try {
				tipoAut = SbnAuthority.valueOf(tb_par_auth.getCd_par_auth());
			} catch (IllegalArgumentException e) {
				continue;
			}

            ParametriAuthority parauth = new ParametriAuthority();
            parauth.setTipoAuthority(tipoAut);
            parauth.setAbilitaAuthority(SbnAbilitaOggetto.valueOf(String.valueOf(tb_par_auth.getTp_abil_auth())));
            parauth.setAbilitaLegamiDoc(SbnIndicatore.valueOf(String.valueOf(tb_par_auth.getFl_abil_legame())));
            parauth.setReticoloLegamiDoc(SbnIndicatore.valueOf(String.valueOf(tb_par_auth.getFl_leg_auth())));
            parauth.setLivelloAut(SbnLivello.valueOf(tb_par_auth.getCd_livello()));
            parauth.setAbilitatoForzatura(SbnIndicatore.valueOf(String.valueOf(tb_par_auth.getFl_abil_forzat())));
            //almaviva5_20140130 evolutive google3
            parauth.setSololocale(ValidazioneDati.in(tb_par_auth.getSololocale(), 's', 'S'));
            parametriAuthorityType.add(parauth);
        }

        return parametriAuthorityType.toArray(new ParametriAuthority[0]);
    }

    private ParametriDocumenti[] formattaParametriDocumentiType(List _elencoParametriMateriale) {
        int size = ValidazioneDati.size(_elencoParametriMateriale);
        List<ParametriDocumenti> parametriDocumentiType = new ArrayList<ParametriDocumenti>(size);
        for (int i = 0; i < size; i++) {
            Tbf_par_mat tb_par_mat = (Tbf_par_mat) _elencoParametriMateriale.get(i);
			SbnMateriale tipoMat;
			try {
	            char mat = tb_par_mat.getCd_par_mat();
				tipoMat = SbnMateriale.valueOf(String.valueOf(mat));
			} catch (IllegalArgumentException e) {
				continue;
			}

            ParametriDocumenti param = new ParametriDocumenti();
			param.setTipoMateriale(tipoMat);
            param.setAbilitaOggetto(SbnAbilitaOggetto.valueOf(String.valueOf(tb_par_mat.getTp_abilitaz())));
            param.setLivelloAut(SbnLivello.valueOf(tb_par_mat.getCd_livello()));
            param.setAbilitatoForzatura(SbnIndicatore.valueOf(String.valueOf(tb_par_mat.getFl_abil_forzat())));
            //almaviva5_20140130 evolutive google3
            param.setSololocale(ValidazioneDati.in(tb_par_mat.getSololocale(), 's', 'S'));
            parametriDocumentiType.add(param);
        }

        return parametriDocumentiType.toArray(new ParametriDocumenti[0]);
    }

    public SbnProfileType formattaSbnProfile(ValidatorProfiler validatorAdmin,
    //	ValidatorAdmin validatorAdmin,
    String utente,
        SbnUserType sbnUserType,
        List _elencoAttivita,
        List _elencoParametri,
        List _elencoParametriMateriale,
        List _elencoParametriAuthority) throws EccezioneSbnDiagnostico{
        SbnProfileType profile = new SbnProfileType();
        _utente = utente;
        validator = validatorAdmin;
        Tbf_bibliotecario user = validator.getUtente(sbnUserType.getBiblioteca() + sbnUserType.getUserId());
        if (user != null) {
			PoloUtility poloUtility = new PoloUtility();
			profile.setBibliotecaUtente(poloUtility.getCodPolo(user) + poloUtility.getCodBiblioteca(user));
		}

        profile.setAttivitaAbilitate(formattaElencoAttivita(_elencoAttivita));
        profile.setParametri(formattaParametri(_elencoParametri, sbnUserType));
        profile.setParametriAuthority(formattaParametriAuthorityType(_elencoParametriAuthority));
        profile.setParametriDocumenti(formattaParametriDocumentiType(_elencoParametriMateriale));
        profile.setSbnUser(sbnUserType);
        return profile;
    }

}
