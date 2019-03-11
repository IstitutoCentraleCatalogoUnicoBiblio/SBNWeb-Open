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
package it.finsiel.sbn.polo.oggetti;

import it.finsiel.sbn.exception.InfrastructureException;
import it.finsiel.sbn.polo.dao.entity.tavole.Ts_link_multimResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.orm.Ts_link_multim;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Classe Link_multim.java
 * <p>
 *
 * </p>
 *
 * @author
 * @author
 *
 * @version 7-mar-03
 */
public class Link_multim extends Ts_link_multim {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6519490649565253212L;
	protected Category log =
        Category.getInstance("iccu.serversbnmarc.Link_multim");

    public Link_multim() {

    }

    /**
     * Inserisce nella tabella un record
     * gli attributi normali devono essere
     * valorizzati nell'oggetto link_multi, mentre l'immagine viene passata come
     * array di byte.
     * @throws InfrastructureException
     */
    public void insert(Ts_link_multim link_multi, byte[] blob)
        throws EccezioneDB, InfrastructureException {
    	Ts_link_multim new_link_multi = new Ts_link_multim();
    	Ts_link_multimResult tabella = new Ts_link_multimResult(link_multi);

//    	ByteArrayOutputStream os1 = new ByteArrayOutputStream();
//    	os1.write(blob);
//    	Hibernate.createBlob(blob);
//    	link_multi.setIMMAGINE(Hibernate.createBlob(blob));
    	link_multi.setIMMAGINE(blob);


    	tabella.insert(link_multi);
        new_link_multi.setID_LINK_MULTIM(link_multi.getID_LINK_MULTIM());
        tabella.valorizzaElencoRisultati(tabella.selectPerKey(new_link_multi.leggiAllParametro()));
        //tabella.valorizzaElencoRisultati(tabella.selectPerKey(link_multi.leggiAllParametro()));
        //tabella.selectPerKey(link_multi.leggiAllParametro());
        List v = tabella.getElencoRisultati();

        if (v.size() == 0) {
            log.error("Errore inserimento di un blob");
            throw new EccezioneDB(1050, "Inserimento blob fallito");
        }
//        if (blob != null) {
//            Ts_link_multim lm = (Ts_link_multim) v.get(0);
//            Blob curr_blob = lm.getIMMAGINE();
//            try {
//                ByteArrayOutputStream os = new ByteArrayOutputStream();
//                //OutputStream os = ((BLOB) curr_blob).getBinaryOutputStream();
//                for (int i = 0; i < blob.length; i++)
//                    os.write(blob[i]);
//                os.close();
//                //((BLOB) curr_blob).close();
//            } catch (IOException e) {
//                log.error("Errore scrittura blob", e);
//                throw new EccezioneDB(1050, "Errore scrittura del blob",e);
//            }
//        }



        //        if (blob != null) {
//            Ts_link_multim lm = (Ts_link_multim) v.get(0);
//            Blob curr_blob = lm.getIMMAGINE();
//            try {
//
//                OutputStream os = ((BLOB) curr_blob).getBinaryOutputStream();
//                for (int i = 0; i < blob.length; i++)
//                    os.write(blob[i]);
//                os.close();
//                //((BLOB) curr_blob).close();
//            } catch (SQLException e) {
//                log.error("Errore gestione blob", e);
//                throw new EccezioneDB(1050, "Gestione blob fallita",e);
//            } catch (IOException e) {
//                log.error("Errore scrittura blob", e);
//                throw new EccezioneDB(1050, "Errore scrittura del blob",e);
//            }
//        }

    }

    public Ts_link_multim cercaPerId(int id) throws EccezioneDB, InfrastructureException {
        setID_LINK_MULTIM(id);
        Ts_link_multimResult tabella = new Ts_link_multimResult(this);


        tabella.valorizzaElencoRisultati(tabella.selectPerKey(this.leggiAllParametro()));
        List v = tabella.getElencoRisultati(1);

        if (v.size() == 0) {
            return null;
        }
        return (Ts_link_multim) v.get(0);
    }

    public void cancellaPerKy(String id,String user) throws IllegalArgumentException, InvocationTargetException, Exception {
        setKY_LINK_MULTIM(id);
        setUTE_VAR(user);
        Ts_link_multimResult tabella = new Ts_link_multimResult(this);
        tabella.executeCustom("cancellaPerKy");
    }

    /**
     * Estrae da un'immagine contenente un blob il relativo array di byte
     * @param ts_link_multim l'oggetto contenente il blob
     * @return array di byte estratti da db
     */
    public byte[] estraiImmagine(Ts_link_multim ts_link_multim)
        throws EccezioneDB {
   //     try {
            if (ts_link_multim.getIMMAGINE() == null)
                return null;
            /*
            BufferedInputStream is =
                new BufferedInputStream(
                    ((Blob) ts_link_multim.getIMMAGINE()).getBinaryStream());
            byte[] buff = new byte[2048];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bytesread = 0;

            bytesread = ts_link_multim.getIMMAGINE().getBinaryStream().read(buff);

//            while ((bytesread = is.read(buff, 0, buff.length)) != -1) {
            while ((bytesread = ts_link_multim.getIMMAGINE().getBinaryStream().read(buff, 0, buff.length)) != -1) {
                baos.write(buff, 0, bytesread);
            }
            is.close();

            byte[] b =baos.toByteArray();
            baos.close();
            */
            return ts_link_multim.getIMMAGINE();
            //return b;
//        } catch (IOException e) {
//            log.error("Errore lettura blob ", e);
//            throw new EccezioneDB(1050, "Errore lettura blob",e);
//        } catch (SQLException e) {
//            log.error("Errore estrazione blob ", e);
//            throw new EccezioneDB(1050, "Errore operazione sul blob",e);
//        }

    }

    public List cercaLinkMultim(String id) throws IllegalArgumentException, InvocationTargetException, Exception{
        Ts_link_multim ts_link_multim = new Ts_link_multim();
        ts_link_multim.setKY_LINK_MULTIM(id);
        Ts_link_multimResult ts_link_multimResult = new Ts_link_multimResult(ts_link_multim);


        ts_link_multimResult.executeCustom("selectPerKy");
        List v = ts_link_multimResult.getElencoRisultati();

        return v;
    }
/*
    public static void main(String[] arg) {
        try {
            ConsoleAppender ca =
                new ConsoleAppender(new SimpleLayout(), "System.out");
            ca.setThreshold(Priority.DEBUG);
            //ca.setThreshold(Priority.INFO);
            ca.setName("iccu");

            BasicConfigurator.resetConfiguration();
            BasicConfigurator.configure(ca);
            //  new Decodificatore();

            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn =
                DriverManager.getConnection(
                    "jdbc:oracle:thin:@sebiccu.akros.it:1521:DBORICCU",
                    "iccu_ragta",
                    "iccu_ragta");
            conn.setAutoCommit(false);

            String file = "E:\\tmp\\provaLink.xml";
            log.debug("Apro il file: " + file);
            InputSource xml_source = new InputSource(new FileInputStream(file));
            log.debug("Aperto");
            SBNMarc rootObject = SbnmarcUnmarshaller.unmarshal(xml_source);

            MarcaType mar =
                (MarcaType) rootObject
                    .getSbnMessage()
                    .getSbnRequest()
                    .getCrea()
                    .getCreaTypeChoice()
                    .getElementoAut()
                    .getDatiElementoAut();

            Ts_link_multim lm1 = new Ts_link_multim();
            lm1.setId_link_multim(123);
            lm1.setKy_link_multim("123");
            lm1.setFL_CANC(" ");
            Link_multim lm = new Link_multim();
            byte[] b_in = mar.getT856(0).getC9_856_1();
            lm.insert(lm1, mar.getT856(0).getC9_856_1());
            System.out.print("Inserimento avvenuto con successo");

            lm1 = lm.cercaPerId(123);

            byte out[] = lm.estraiImmagine(lm1);
            System.out.print("Inserimento ed estrazione avvenuto con successo");
            System.out.print("Non eseguo la commit");
        } catch (EccezioneIccu e) {
            log.error("Errore " + e);
            log.error("Descrizione:" + e.getMessaggio());
        } catch (Exception e) {
            log.error("Errore " + e);
        }
    }
*/
}
