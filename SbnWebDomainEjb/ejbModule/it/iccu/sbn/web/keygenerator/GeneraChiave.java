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
package it.iccu.sbn.web.keygenerator;

import it.finsiel.gateway.exception.SbnMarcDiagnosticoException;
import it.finsiel.gateway.intf.KeyAutore;
import it.iccu.sbn.SbnMarcFactory.util.bibliografica.UtilityAutori;
import it.iccu.sbn.ejb.model.unimarcmodel.A200;
import it.iccu.sbn.ejb.model.unimarcmodel.A210;
import it.iccu.sbn.ejb.model.unimarcmodel.AutorePersonaleType;
import it.iccu.sbn.ejb.model.unimarcmodel.DatiElementoType;
import it.iccu.sbn.ejb.model.unimarcmodel.EnteType;
import it.iccu.sbn.ejb.model.unimarcmodel.types.Indicatore;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnAuthority;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnLivello;
import it.iccu.sbn.ejb.model.unimarcmodel.types.SbnTipoNomeAutore;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.web.integration.sbnmarc.SbnMarcGateway;

import org.jboss.logging.Logger;



public class GeneraChiave extends SbnMarcGateway implements KeyAutore {

	private static final long serialVersionUID = -8013628369215885211L;

	private static Logger log = Logger.getLogger(GeneraChiave.class);

	private String nome;
	private String tipoNome;
	private KeyAutore key;

	public GeneraChiave(String nome, String tipoNome) {
		this.nome = nome;
		this.tipoNome = tipoNome;
	}

	public GeneraChiave() {
		super();
	}

	public void estraiChiavi(String tipoNome, String nome) throws Exception {
		try {
			DatiElementoType dati = costruisciSbnMarcAutore(tipoNome, nome);
			key = getGateway().getChiaveAutore(dati, tipoNome);

		} catch (SbnMarcDiagnosticoException e) {
			key = null;
			log.error("", e);
			String msg = "";
			switch (e.getErrorID()) {
			case 3054:
			case 3259:
				msg = "errors.documentofisico.asteriscoObbligatorio";
				break;

			case 3055:
				msg = "errors.documentofisico.quattroAsterischi";
				break;

			case 3260:
				msg = "errors.documentofisico.doppioAsterisco";
				break;

			case 3265:
				msg = "errors.documentofisico.asteriscoNonPresenteSecondo";
				break;

			case 3065:
				msg = "errors.documentofisico.asteriscoObbligatorioUnico";
				break;

			case 3099:
				msg = "errors.documentofisico.carattereNonValido";
				break;

			case 3043:
				msg = "errors.documentofisico.PossessoriGeneraChiaveErrata";
				break;

			case 56:
			case 3045:
			case 3056:
			case 3290:
			case 3235:
			case 3262:
				msg = e.getMessage();
				break;

			default:
				msg = "error.documentofisico.generico";
				break;
			}

			throw new Exception(msg);

		} catch (Exception e) {
			key = null;
			gateway = null;
			log.error("", e);
		}

	}

	private DatiElementoType costruisciSbnMarcAutore(String tipoNome, String nome) {
		DatiElementoType dati = new DatiElementoType();
		if (!ValidazioneDati.isFilled(tipoNome)) {
			dati.setT001(nome);
			dati.setLivelloAut(SbnLivello.VALUE_0);
			return dati;
		}

		UtilityAutori ua = new UtilityAutori();

		// QUALIFICAZIONI
		String qualificazioni = ua.getNomeParteQualificazioni(nome, tipoNome);

		if (!ValidazioneDati.isFilled(tipoNome))
			tipoNome = SbnTipoNomeAutore.A.toString();

		if (ua.isEnte(tipoNome)) {
			// ENTE
			EnteType ente = new EnteType();
			ente.setTipoAuthority(SbnAuthority.AU);
			ente.setLivelloAut(SbnLivello.VALUE_0);

			// TIPO NOME
			ente.setTipoNome(SbnTipoNomeAutore.valueOf(tipoNome));

			// nominativo
			A210 a210 = new A210();

			// questi due sono required
			a210.setId1(Indicatore.VALUE_1);
			a210.setId2(Indicatore.VALUE_1);

			// NUOVA GESTIONE PARTE PRIMARIA E QUALIFICAZIONE
			String strA210 = nome;
			String strA210_Org = nome;
			String[] arrayQualificazioni = null;

			// L'eventuale qualificazione viene
			// accodata alla parte primaria.
			if (qualificazioni != null) {
				arrayQualificazioni = qualificazioni.split(" ; ");
			}
			if (arrayQualificazioni != null) {
				for (int i = 0; i < arrayQualificazioni.length; i++) {
					strA210 = strA210 + " " + arrayQualificazioni[i];
				}

				// Se l'autore è di tipo G, accoda anche
				// la parte <b_210>...</b_210> di <a_210_G>
				if (tipoNome.equals("G")) {
					String[] b210 = ua.getNomeTipoG_b_210(nome);
					if (b210 != null) {
						for (int j = 0; j < b210.length; j++) {
							if (b210[j] != null) {
								strA210 = strA210 + " " + b210[j];
							}
						}
					}
				}
			}

			// Comunque invio stringa originale
			a210.setA_210(strA210_Org);
			ente.setT210(a210);

			dati = ente;
		} else {
			// è un autore
			AutorePersonaleType apt = new AutorePersonaleType();
			apt.setTipoAuthority(SbnAuthority.AU);
			apt.setLivelloAut(SbnLivello.VALUE_0);

			// TIPO NOME
			apt.setTipoNome(SbnTipoNomeAutore.valueOf(tipoNome));

			// nominativo
			A200 a200 = new A200();
			a200.setId2(Indicatore.VALUE_1);

			// ESEMPIO: Pippo, Giuseppe = nomeCompleto
			// UNA PARTE PRINCIPALE C'E' SEMPRE
			a200.setA_200(nome);
			apt.setT200(a200);

			dati = apt;
		}

		return dati;
	}

	public String getKy_auteur() {
		return key != null ? key.getKy_auteur() : null;
	}

	public String getKy_cautun() {
		return key != null ? key.getKy_cautun() : null;
	}

	public String getKy_cles1_A() {
		return key != null ? key.getKy_cles1_A() : null;
	}

	public String getKy_cles2_A() {
		return key != null ? key.getKy_cles2_A() : null;
	}

	public String getKy_el1() {
		return key != null ? key.getKy_el1() : null;
	}

	public String getKy_el1a() {
		return key != null ? key.getKy_el1a() : null;
	}

	public String getKy_el1b() {
		return key != null ? key.getKy_el1b() : null;
	}

	public String getKy_el2() {
		return key != null ? key.getKy_el2() : null;
	}

	public String getKy_el2a() {
		return key != null ? key.getKy_el2a() : null;
	}

	public String getKy_el2b() {
		return key != null ? key.getKy_el2b() : null;
	}

	public String getKy_el3() {
		return key != null ? key.getKy_el3() : null;
	}

	public String getKy_el4() {
		return key != null ? key.getKy_el4() : null;
	}

	public String getKy_el5() {
		return key != null ? key.getKy_el5() : null;
	}

	public void calcoloClesToSearch(String nome) throws Exception {
		this.nome = nome;
		estraiChiavi(tipoNome, nome);
	}

	public String getNome() {
		return nome;
	}

	public String getTipoNome() {
		return tipoNome;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeneraChiave other = (GeneraChiave) obj;

		if (other.getKy_cautun() != null && !other.getKy_cautun().equals(this.getKy_cautun()))
			return false;
		if (other.getKy_auteur() != null && !other.getKy_auteur().equals(this.getKy_auteur()))
			return false;
		if (other.getKy_el1a() != null && !other.getKy_el1a().equals(this.getKy_el1a()))
			return false;
		if (other.getKy_el2a() != null && !other.getKy_el2a().equals(this.getKy_el2a()))
			return false;
		if (other.getKy_el3() != null && !other.getKy_el3().equals(this.getKy_el3()))
			return false;
		if (other.getKy_el4() != null && !other.getKy_el4().equals(this.getKy_el4()))
			return false;
		if (other.getKy_cles1_A() != null && !other.getKy_cles1_A().equals(this.getKy_cles1_A()))
			return false;
		if (other.getKy_cles2_A() != null && !other.getKy_cles2_A().equals(this.getKy_cles2_A()))
			return false;

		return true;
	}

}
