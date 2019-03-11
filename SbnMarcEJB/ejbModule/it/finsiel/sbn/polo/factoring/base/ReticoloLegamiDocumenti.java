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

import it.finsiel.sbn.polo.orm.viste.Vl_titolo_tit_b;

/**
 * Classe VerificaLivelloLegami.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 10-mar-03
 */
public class ReticoloLegamiDocumenti {

    public static boolean verificaLivello(Vl_titolo_tit_b tit_tit, String livello) {
        if (livello.equals("003")) {
            return verificaLivello003(tit_tit);
        } else if (livello.equals("002")) {
            return verificaLivello002(tit_tit);
        } else if (livello.equals("001")) {
            return verificaLivello001(tit_tit);
        }
        return false;
    }

    protected static boolean verificaLivello003(Vl_titolo_tit_b tit_tit) {
        String nat_1 = tit_tit.getCD_NATURA_BASE();
        String nat_2 = tit_tit.getCD_NATURA_COLL();
        String leg = tit_tit.getTP_LEGAME();
        if (nat_1.equals("M")) {
            if (leg.equals("01")) {
                if (nat_2.equals("C"))
                    return true;
                if (nat_2.equals("M"))
                    return true;
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("51")) {
                if (nat_2.equals("M"))
                    return true;
                if (nat_2.equals("W"))
                    return true;
            } else if (leg.equals("03")) {
                if (nat_2.equals("T"))
                    return true;
            } else if (leg.equals("06")) {
                if (nat_2.equals("B"))
                    return true;
            } else if (leg.equals("08")) {
                if (nat_2.equals("D"))
                    return true;
                if (nat_2.equals("P"))
                    return true;
            } else if (leg.equals("09")) {
                if (nat_2.equals("A"))
                    return true;
            }
        } else if (nat_1.equals("W")) {
            if (leg.equals("01")) {
                if (nat_2.equals("C"))
                    return true;
                if (nat_2.equals("M"))
                    return true;
            } else if (leg.equals("03")) {
                if (nat_2.equals("T"))
                    return true;
            }
        } else if (nat_1.equals("C")) {
            if (leg.equals("08")) {
                if (nat_2.equals("D"))
                    return true;
                if (nat_2.equals("P"))
                    return true;
            }
        } else if (nat_1.equals("S")) {
            if (leg.equals("01")) {
                if (nat_2.equals("C"))
                    return true;
            } else if (leg.equals("08")) {
                if (nat_2.equals("D"))
                    return true;
                if (nat_2.equals("P"))
                    return true;
            } else if (leg.equals("09")) {
                if (nat_2.equals("A"))
                    return true;
            }
        } else if (nat_1.equals("N")) {
            if (leg.equals("01")) {
                if (nat_2.equals("M"))
                    return true;
                if (nat_2.equals("S"))
                    return true;
                if (nat_2.equals("W"))
                    return true;
            } else if (leg.equals("06")) {
                if (nat_2.equals("B"))
                    return true;
            } else if (leg.equals("08")) {
                if (nat_2.equals("D"))
                    return true;
                if (nat_2.equals("P"))
                    return true;
                if (nat_2.equals("A"))
                    return true;
            }
        }
        return false;
    }

    protected static boolean verificaLivello002(Vl_titolo_tit_b tit_tit) {
        String nat_1 = tit_tit.getCD_NATURA_BASE();
        String nat_2 = tit_tit.getCD_NATURA_COLL();
        String leg = tit_tit.getTP_LEGAME();
        if (nat_1.equals("M")) {
            if (leg.equals("01")) {
                if (nat_2.equals("C"))
                    return true;
                if (nat_2.equals("M"))
                    return true;
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("51")) {
                if (nat_2.equals("M"))
                    return true;
                if (nat_2.equals("W"))
                    return true;
            } else if (leg.equals("02")) {
                if (nat_2.equals("M"))
                    return true;
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("03")) {
                if (nat_2.equals("T"))
                    return true;
            } else if (leg.equals("04")) {
                if (nat_2.equals("M"))
                    return true;
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("06")) {
                if (nat_2.equals("B"))
                    return true;
            } else if (leg.equals("08")) {
                if (nat_2.equals("D"))
                    return true;
                if (nat_2.equals("P"))
                    return true;
            } else if (leg.equals("09")) {
                if (nat_2.equals("A"))
                    return true;
            }
        } else if (nat_1.equals("W")) {
            if (leg.equals("01")) {
                if (nat_2.equals("C"))
                    return true;
                if (nat_2.equals("M"))
                    return true;
            } else if (leg.equals("03")) {
                if (nat_2.equals("T"))
                    return true;
            }
        } else if (nat_1.equals("C")) {
            if (leg.equals("01")) {
                if (nat_2.equals("C"))
                    return true;
            } else if (leg.equals("04")) {
                if (nat_2.equals("C"))
                    return true;
            } else if (leg.equals("08")) {
                if (nat_2.equals("D"))
                    return true;
                if (nat_2.equals("P"))
                    return true;
            } else if (leg.equals("09")) {
                if (nat_2.equals("A"))
                    return true;
            }
        } else if (nat_1.equals("S")) {
            if (leg.equals("01")) {
                if (nat_2.equals("C"))
                    return true;
            } else if (leg.equals("02")) {
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("04")) {
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("41")) {
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("42")) {
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("43")) {
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("08")) {
                if (nat_2.equals("D"))
                    return true;
                if (nat_2.equals("P"))
                    return true;
            } else if (leg.equals("09")) {
                if (nat_2.equals("A"))
                    return true;
            }
        } else if (nat_1.equals("N")) {
            if (leg.equals("01")) {
                if (nat_2.equals("M"))
                    return true;
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("06")) {
                if (nat_2.equals("B"))
                    return true;
            } else if (leg.equals("08")) {
                if (nat_2.equals("D"))
                    return true;
                if (nat_2.equals("P"))
                    return true;
            } else if (leg.equals("09")) {
                if (nat_2.equals("A"))
                    return true;
            }
        } else if (nat_1.equals("T")) {
            if (leg.equals("06")) {
                if (nat_2.equals("B"))
                    return true;
            } else if (leg.equals("09")) {
                if (nat_2.equals("A"))
                    return true;
            }
        } else if (nat_1.equals("A")) {
            if (leg.equals("08")) {
                if (nat_2.equals("D"))
                    return true;
            }
        }
        return false;
    }

    protected static boolean verificaLivello001(Vl_titolo_tit_b tit_tit) {
        String nat_1 = tit_tit.getCD_NATURA_BASE();
        String nat_2 = tit_tit.getCD_NATURA_COLL();
        String leg = tit_tit.getTP_LEGAME();
        if (nat_1.equals("M")) {
            if (leg.equals("01")) {
                if (nat_2.equals("C"))
                    return true;
                if (nat_2.equals("M"))
                    return true;
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("51")) {
                if (nat_2.equals("M"))
                    return true;
                if (nat_2.equals("W"))
                    return true;
            } else if (leg.equals("02")) {
                if (nat_2.equals("M"))
                    return true;
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("03")) {
                if (nat_2.equals("T"))
                    return true;
            } else if (leg.equals("04")) {
                if (nat_2.equals("M"))
                    return true;
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("05")) {
                if (nat_2.equals("M"))
                    return true;
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("06")) {
                if (nat_2.equals("B"))
                    return true;
            } else if (leg.equals("08")) {
                if (nat_2.equals("D"))
                    return true;
                if (nat_2.equals("P"))
                    return true;
            } else if (leg.equals("09")) {
                if (nat_2.equals("A"))
                    return true;
            }
        } else if (nat_1.equals("W")) {
            if (leg.equals("01")) {
                if (nat_2.equals("C"))
                    return true;
                if (nat_2.equals("M"))
                    return true;
            } else if (leg.equals("03")) {
                if (nat_2.equals("T"))
                    return true;
            }
        } else if (nat_1.equals("C")) {
            if (leg.equals("01")) {
                if (nat_2.equals("C"))
                    return true;
            } else if (leg.equals("04")) {
                if (nat_2.equals("C"))
                    return true;
            } else if (leg.equals("05")) {
                if (nat_2.equals("C"))
                    return true;
            } else if (leg.equals("07")) {
                if (nat_2.equals("C"))
                    return true;
            } else if (leg.equals("08")) {
                if (nat_2.equals("D"))
                    return true;
                if (nat_2.equals("P"))
                    return true;
            } else if (leg.equals("09")) {
                if (nat_2.equals("A"))
                    return true;
            }
        } else if (nat_1.equals("S")) {
            if (leg.equals("01")) {
                if (nat_2.equals("C"))
                    return true;
            } else if (leg.equals("02")) {
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("04")) {
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("41")) {
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("42")) {
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("43")) {
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("05")) {
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("07")) {
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("08")) {
                if (nat_2.equals("D"))
                    return true;
                if (nat_2.equals("P"))
                    return true;
            } else if (leg.equals("09")) {
                if (nat_2.equals("A"))
                    return true;
            }
        } else if (nat_1.equals("N")) {
            if (leg.equals("01")) {
                if (nat_2.equals("M"))
                    return true;
                if (nat_2.equals("S"))
                    return true;
            } else if (leg.equals("06")) {
                if (nat_2.equals("B"))
                    return true;
            } else if (leg.equals("08")) {
                if (nat_2.equals("D"))
                    return true;
                if (nat_2.equals("P"))
                    return true;
            } else if (leg.equals("09")) {
                if (nat_2.equals("A"))
                    return true;
            }
        } else if (nat_1.equals("T")) {
            if (leg.equals("06")) {
                if (nat_2.equals("B"))
                    return true;
            } else if (leg.equals("08")) {
                if (nat_2.equals("D"))
                    return true;
                if (nat_2.equals("P"))
                    return true;
            } else if (leg.equals("09")) {
                if (nat_2.equals("A"))
                    return true;
            }
        } else if (nat_1.equals("B")) {
            if (leg.equals("08")) {
                if (nat_2.equals("D"))
                    return true;
            }
        } else if (nat_1.equals("A")) {
            if (leg.equals("08")) {
                if (nat_2.equals("D"))
                    return true;
            }
        }
        return false;
    }

}
