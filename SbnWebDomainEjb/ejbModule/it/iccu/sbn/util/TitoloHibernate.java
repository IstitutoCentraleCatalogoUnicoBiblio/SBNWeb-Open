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
package it.iccu.sbn.util;

import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.DataException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.vo.documentofisico.TitoloVO;
import it.iccu.sbn.persistence.dao.documentofisico.Tbc_inventarioDao;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.bibliografica.Tb_titolo;

import java.util.ArrayList;
import java.util.List;

public class TitoloHibernate {

	public static final List<TitoloVO> getTitoloHib(Tb_titolo titolo, String ticket)
			throws DataException, ApplicationException, ValidationException {
		List<TitoloVO> lista = new ArrayList<TitoloVO>(); // output di un singolo titolo
											// ricavato
		TitoloVO recTit = null;
		String tit_isbd = "";
		try {
			if (titolo == null) {
				throw new DataException("titoloNonTrovatoSuBDPolo");
			} else {
				recTit = new TitoloVO();
				tit_isbd = titolo.getIsbd();
				recTit.setBid(titolo.getBid()); // almaviva2 31/08/2010
				recTit.setIsbd(titolo.getIsbd());
				if (!tit_isbd.equals("") && tit_isbd.length() > 200) {
					// gestione composizione titolo
					String indice_isbd = titolo.getIndice_isbd();

					String[] isbd_composto = tit_isbd.split("\\. -");
					// se si splitta (lunghe>0) allora considero la prima parte,
					// altrimenti tutto
					String tit_primaParte = isbd_composto[0];
					String tit_secondaParte = "";

					if (isbd_composto.length > 1) {
						tit_secondaParte = isbd_composto[1];
					}
					String[] indice_isbd_composto = indice_isbd.split("\\;");
					String tit_secondaParte_finale = "";
					String tit_primaParte_finale = "";
					String tit_isbd_finale = "";

					// if (indice_isbd_composto.length>0 &&
					// indice_isbd_composto.length>=1){
					if (indice_isbd_composto.length > 0) {
						// for (int y = 0; y < indice_isbd_composto.length; y++)
						// {
						if (indice_isbd_composto[0] != null
								&& indice_isbd_composto[0].length() != 0) {
							String primaParte = indice_isbd_composto[0];
							String[] primaParte_composto = primaParte
									.split("-");
							String pos_primaParte = primaParte_composto[1];

							if (tit_primaParte.length() > 100) {
								tit_primaParte_finale = tit_primaParte
										.substring(0, 100);
							} else {
								tit_primaParte_finale = tit_primaParte;
							}
						}
						if (indice_isbd_composto.length > 1) {
							// if (indice_isbd_composto[1]!=null &&
							// indice_isbd_composto[1].length()!=0){
							String secondaParte = indice_isbd_composto[1];
							String[] secondaParte_composto = secondaParte
									.split("\\-");
							String pos_secondaParte = secondaParte_composto[1];
							if (tit_secondaParte.length() > 100) {
								tit_secondaParte_finale = tit_secondaParte
										.substring(0, 100);
							} else {
								tit_secondaParte_finale = tit_secondaParte;
							}
						}
					}
					if (!tit_primaParte_finale.equals("")
							&& tit_primaParte_finale.length() > 0) {
						tit_isbd_finale = tit_primaParte_finale.trim();
						if (!tit_secondaParte_finale.equals("")
								&& tit_secondaParte_finale.length() > 0) {
							tit_isbd_finale = tit_isbd_finale + ". - "
									+ tit_secondaParte_finale.trim();
						}
					}
					if (!tit_isbd_finale.equals("")
							&& tit_isbd_finale.length() > 0) {
						recTit.setIsbd(tit_isbd_finale);
					}
				}
				recTit.setNatura(String.valueOf(titolo.getCd_natura()));
				if (String.valueOf(titolo.getFl_condiviso()).equalsIgnoreCase(
						"S")) {
					recTit.setFlagCondiviso(true);
				} else {
					recTit.setFlagCondiviso(false);
				}
				recTit.setTipoMateriale(String.valueOf(titolo.getTp_materiale()));

			}
			lista.add(recTit);

		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException(e);
		}
		return lista;
	}

	public static final List<TitoloVO> getTitolo(String bid, String ticket) throws DataException,
			ApplicationException, ValidationException {
		List<TitoloVO> lista = new ArrayList<TitoloVO>(); // output di un singolo titolo
											// ricavato
		Tbc_inventarioDao daoInv = new Tbc_inventarioDao();
		TitoloVO recTit = null;
		String tit_isbd = "";
		try {
			if (bid == null || (bid != null && bid.trim().length() == 0)) {
				throw new ValidationException("validaCollBidObbligatorio",
						ValidationException.errore);
			}
			if (bid != null && bid.length() != 0) {
				if (bid.length() > 10) {
					throw new ValidationException("validaInvBidEccedente",
							ValidationException.errore);
				}
			}
			Tb_titolo titolo = daoInv.getTitoloDF1(bid);
			if (titolo == null) {
				throw new DataException("titoloNonTrovatoSuBDPolo");
			} else {
				recTit = new TitoloVO();
				tit_isbd = titolo.getIsbd();
				recTit.setBid(titolo.getBid());
				recTit.setIsbd(titolo.getIsbd());
				//
				String indice_isbd = titolo.getIndice_isbd();
				String[] indice = indice_isbd.split(";");

				String tit_terzaParte = null;
				String[] isbd_composto = tit_isbd.split("\\. -");
				// se si splitta (lunghe>0) allora considero la prima parte,
				// altrimenti tutto
				String[] indice_isbd_composto = indice_isbd.split("\\;");
				if (indice.length > 1) {
					for (int y = 1; y < indice.length; y++) {
						if (indice[y].startsWith("215")) {
							String[] iDueValori = indice[y].split("-");
							String secondoValore = iDueValori[1];
							int numeroAree = indice.length;
							int posizioneSuccessiva = y + 1;
							if (posizioneSuccessiva < numeroAree) {
								String[] gliAltriDueValori = indice[posizioneSuccessiva].split("-");
								String altroSecondoValore = gliAltriDueValori[1];
								tit_terzaParte = ". - "	+ titolo.getIsbd().substring(
														(Integer.parseInt(secondoValore) - 1),
														(Integer.parseInt(altroSecondoValore) - 3));
							} else {
								tit_terzaParte = ". - "
										+ titolo.getIsbd()
												.substring(
														Integer.parseInt(secondoValore) - 1);
							}
						}
					}
					String tit_primaParte = isbd_composto[0];
					String tit_secondaParte = "";
					if (isbd_composto.length > 1) {
						tit_secondaParte = isbd_composto[1];
					}
					String tit_secondaParte_finale = "";
					String tit_primaParte_finale = "";
					String tit_isbd_finale = "";
					if (!tit_isbd.equals("") && tit_isbd.length() > 200) {
						// gestione composizione titolo

						if (indice_isbd_composto.length > 0) {
							if (indice_isbd_composto[0] != null
									&& indice_isbd_composto[0].length() != 0) {
								String primaParte = indice_isbd_composto[0];
								String[] primaParte_composto = primaParte
										.split("-");
								String pos_primaParte = primaParte_composto[1];

								if (tit_primaParte.length() > 100) {
									tit_primaParte_finale = tit_primaParte
											.substring(0, 100);
								} else {
									tit_primaParte_finale = tit_primaParte;
								}
							}
							if (indice_isbd_composto.length > 1) {
								String secondaParte = indice_isbd_composto[1];
								String[] secondaParte_composto = secondaParte
										.split("\\-");
								String pos_secondaParte = secondaParte_composto[1];
								if (tit_secondaParte.length() > 100) {
									tit_secondaParte_finale = tit_secondaParte
											.substring(0, 100);
								} else {
									tit_secondaParte_finale = tit_secondaParte;
								}
							}
						}
						if (!tit_primaParte_finale.equals("")
								&& tit_primaParte_finale.length() > 0) {
							tit_isbd_finale = tit_primaParte_finale.trim();
							if (!tit_secondaParte_finale.equals("")
									&& tit_secondaParte_finale.length() > 0) {
								tit_isbd_finale = tit_isbd_finale + ". - "
										+ tit_secondaParte_finale.trim();
							}
						}
						if (!tit_isbd_finale.equals("")
								&& tit_isbd_finale.length() > 0) {
							recTit.setIsbd(tit_isbd_finale);
						}
					}
					if (tit_terzaParte != null) {
						if (!tit_isbd_finale.equals("")
								&& tit_isbd_finale.length() > 0) {
							recTit.setIsbd(tit_isbd_finale + tit_terzaParte);
						}
					}
					recTit.setTerzaParte(tit_terzaParte);
				}

				recTit.setNatura(String.valueOf(titolo.getCd_natura()));
				if (String.valueOf(titolo.getFl_condiviso()).equalsIgnoreCase(
						"S")) {
					recTit.setFlagCondiviso(true);
				} else {
					recTit.setFlagCondiviso(false);
				}
				recTit.setTipoMateriale(String.valueOf(titolo.getTp_materiale()));

			}
			lista.add(recTit);

		} catch (ValidationException e) {
			throw e;
		} catch (DataException e) {
			throw e;
		} catch (DaoManagerException e) {
			e.printStackTrace();
			throw new DataException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException(e);
		}
		return lista;
	}

}
