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
package it.finsiel.sbn.polo.factoring.util;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

/**
 * Classe che ha il compito di caricare i file di risorse
 * @author
 */
public class ResourceLoader {

        /**
         * Directory contenente le immagini dell'applicazione
         */
        public static final String imageDirectory       = "images";
        public static final String soundDirectory       = "sound";

        /**
         * File contenente le parametrizzazioni dell'applicazione
         */
        public static final String NOME_FILE_PROPRIETA  = "iidsettings";


        /**
         * File contenente la documentazione dei caratteri unicode, in particolare
         * ad ogni codice esadecimale corrisponde una descrizione testuale del
         * carattere
         */
        public static final String NOME_FILE_UNICODE    = "unicodeDescriptions.txt";

        private static ResourceLoader loader    = null;
        private static ClassLoader classLoader  = null;

        /**
         * Inizializza il class loader di questa classe se necessario,
         * tale class loader serve a caricare i file di risorse.
         */
        private static void checkClassLoader(){
            if ( loader == null ) {
                // Aggiunta per il supporto del caricamento in webstart
                loader = new ResourceLoader();
                classLoader = loader.getClass().getClassLoader();
            }
        }


        public static String getPropertyString(String propertyName){
            return ResourceBundle.getBundle(NOME_FILE_PROPRIETA).getString(propertyName);
        }

        public static int getPropertyInteger(String propertyName){
            try{
                return Integer.parseInt(ResourceBundle.getBundle(NOME_FILE_PROPRIETA).getString(propertyName));
            } catch ( Exception ex ){
                return -1;
            }
        }
        public static BigDecimal getPropertyBigDecimal(String propertyName){
            try{
                return new BigDecimal(ResourceBundle.getBundle(NOME_FILE_PROPRIETA).getString(propertyName));
            } catch ( Exception ex ){
                return null;
            }
        }
        /**
         *
         * @param propertyName nome della proprietà nel file .properties
         * @return true se il valore della proprietà è la stringa "true" case insensitive
         */
        public static boolean getPropertyBoolean(String propertyName){
            try{
                return (new Boolean(ResourceBundle.getBundle(NOME_FILE_PROPRIETA).getString(propertyName))).booleanValue();
            } catch ( Exception ex ){
                return false;
            }
        }

        /**
         * Creazione di un ImageIcon il cui file si trova nella cartella images la
         * cartella images si trova all'interno della cartella classes allo stesso
         * livello della cartella com: classes\images
         *
         * @param fileName nome del file immagine da caricare nella cartella images
         *
         * @return l'ImageIcon creata dal file
         */
        public static ImageIcon getImageIcon(String fileName) {
            checkClassLoader();
            ImageIcon image = null;
            try{
                URL url = classLoader.getResource(imageDirectory + "/" + fileName);
                image = new ImageIcon(url);
            } catch(NullPointerException ex){
            }
            return image;
        }

        /**
         * Apre in lettura il file contente la tabella dei codici in formato XML
         */
        public static Reader getTBCodici() {
            checkClassLoader();
            // Prendo dal file .properties il nome del file
            ResourceBundle bundle = ResourceBundle.getBundle(NOME_FILE_PROPRIETA);
            String tbCodiciFileName = bundle.getString("NOME_FILE_CODICI_XML");
            Reader reader = null;
            checkClassLoader();
            try{
                InputStream istream = classLoader.getResourceAsStream(tbCodiciFileName);
                reader = new InputStreamReader(istream);
            } catch(NullPointerException ex){
            }
            return reader;
        }

    /**
     * Apre in lettura un file presente nella cartella classes/
     * @param nomeFile nome del file da aprire in lettura
     * @return il Reader relativo al file aperto
     */
    public static InputStreamReader getFile(String nomeFile) {
            checkClassLoader();
            // Prendo dal file .properties il nome del file
            ResourceBundle bundle = ResourceBundle.getBundle(NOME_FILE_PROPRIETA);
            InputStreamReader reader = null;
            try{
                InputStream istream = classLoader.getResourceAsStream(nomeFile);
                reader = new InputStreamReader(istream);
            } catch(NullPointerException ex){
            }
            return reader;
        }



    /**
     * Apre in lettura un file presente nella cartella classes/
     * @param nomeFile nome del file da aprire in lettura
     * @return l'InputStream relativo al file aperto
     */
    public static InputStream getFileInputStream(String nomeFile) {
        checkClassLoader();
        // Prendo dal file .properties il nome del file
        ResourceBundle bundle = ResourceBundle.getBundle(NOME_FILE_PROPRIETA);
        InputStream istream = classLoader.getResourceAsStream(nomeFile);
        return istream;
    }



/**
 * Apre in lettura il file contente la tabella dei repertori in formato XML
 */
    public static Reader getTBRepertori() {
            checkClassLoader();
            // Prendo dal file .properties il nome del file
            ResourceBundle bundle = ResourceBundle.getBundle(NOME_FILE_PROPRIETA);
            String tbRepertoriFileName = bundle.getString("NOME_FILE_REPERTORI_XML");
            Reader reader = null;
            try{
                InputStream istream = classLoader.getResourceAsStream(tbRepertoriFileName);
                reader = new InputStreamReader(istream);
            } catch(NullPointerException ex){
            }
            return reader;
        }
    /**
     * Apre in lettura il file contente la tabella dei luoghi in formato XML
     */
    public static Reader getTBLuoghi() {
            checkClassLoader();
            // Prendo dal file .properties il nome del file
            ResourceBundle bundle = ResourceBundle.getBundle(NOME_FILE_PROPRIETA);
            String tbLuoghiFileName = bundle.getString("NOME_FILE_LUOGHI_XML");
            Reader reader = null;
            try{
                InputStream istream = classLoader.getResourceAsStream(tbLuoghiFileName);
                reader = new InputStreamReader(istream);
            } catch(NullPointerException ex){
            }
            return reader;
        }

    /**
     * Apre in lettura il file unicodeDescriptions.txt
     * contenente le descrizioni dei caratteri unicode,
     * serve alla tastiera virtuale.
     */
    public static BufferedReader getDBUnicode() {
        checkClassLoader();
        BufferedReader reader = null;
        try{
            InputStream istream = classLoader.getResourceAsStream(NOME_FILE_UNICODE);
            reader              = new BufferedReader(new InputStreamReader(istream));
        } catch(NullPointerException ex){
            ex.printStackTrace();
        }
        return reader;
        }


    public static AudioClip getAudioClip(String fileName) {
        checkClassLoader();
        AudioClip clip = null;
        URL url = classLoader.getResource(soundDirectory + "/" + fileName);
        clip = Applet.newAudioClip(url);
        return clip;
    }

    public static void playErrorSound(){
        try {
            AudioClip audioError = ResourceLoader.getAudioClip("error.wav");
            audioError.play();
        } catch (Exception e) {
        }
    }

}
