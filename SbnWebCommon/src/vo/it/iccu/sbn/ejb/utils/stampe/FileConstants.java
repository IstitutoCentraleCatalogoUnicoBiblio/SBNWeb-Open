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
package it.iccu.sbn.ejb.utils.stampe;

import java.awt.Color;
import java.awt.Point;

public class FileConstants {

    public static final String MIME_TYPE_PDF = "application/pdf";

    public static final String MIME_TYPE_RTF = "application/rtf";

    public static final String MIME_TYPE_XP7M = "application/x-pkcs7-mime";

    public static final String MIME_TYPE_P7M = "application/pkcs7-mime";

    public static final int BUFFER_SIZE = 8192;

    public static final int BARCODE_DPI = 180;

    public static final double BARCODE_HEIGHT = 15;// mm

    public static final Point PROTOCOLLO_HEADER_LEFT_TOP = new Point(30, 70);

    public static final String PROTOCOLLO_HEADER_FONTNAME = "Courier";

    public static final float PROTOCOLLO_HEADER_FONTSIZE = 10;

    public static final Color PROTOCOLLO_HEADER_FONTCOLOR = Color.BLACK;

    public static final String SHA = "SHA-1";

    // public static final String STAMPA_PROVA_COMPILED =
    // "WEB-INF/reports/StampaProva.jasper";
    //
    // public static final String STAMPA_PROVA_TEMPLATE=
    // "WEB-INF/reports/StampaProva.jrxml";
    // public static final String STAMPA_PROVA_COMPILED =
    // "/reports/BarCode.jasper";
    //
    // public static final String STAMPA_PROVA_TEMPLATE=
    // "/reports/BarCode.jrxml";

    public static final String STAMPA_PROVA_COMPILED = "/reports/pippo.jasper";

    public static final String STAMPA_ETICHETTE_COMPILED = "/reports/StampaEtichette.jasper";

    public static final String STAMPA_PROVA_TEMPLATE = "/reports/StampaProva.jrxml";

}
