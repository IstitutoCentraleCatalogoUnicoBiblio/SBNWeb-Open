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
package it.iccu.sbn.web.plugins;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

public class Log4jPlugin implements PlugIn {

    private String config;

    public void init(ActionServlet servlet, ModuleConfig config)
            throws ServletException {

        String configPath = getConfig();
        // if the log4j-config parameter is not set, then no point in trying
        if (configPath != null) {
            // if the configPath is an empty string, then no point in trying
            if (configPath.length() >= 1) {

                // check if the config file is XML or a Properties file
                boolean isXMLConfigFile = (configPath.endsWith(".xml")) ? true : false;

                ServletContext context = servlet.getServletContext();
				String contextPath = context.getRealPath("/");
                if (contextPath != null) {
                    // The webapp is deployed directly off the filesystem,
                    // not from a .war file so we *can* do File IO.
                    // This means we can use configureAndWatch() to re-read
                    // the the config file at defined intervals.
                    // Now let's check if the given configPath actually exists.
                    String systemConfigPath = configPath.replace('/', File.separatorChar);
                    File log4jFile = new File(contextPath + systemConfigPath);

                    if (log4jFile.canRead()) {
                        LogLog.debug("Configuring Log4j from File: " + log4jFile.getAbsolutePath());
                        if (isXMLConfigFile) {
                            DOMConfigurator.configure(log4jFile.getAbsolutePath());
                        } else {
                            PropertyConfigurator.configure(log4jFile.getAbsolutePath());
                       }
                        log4jFile = null;
                    }
                    LogLog.debug("SIGED - Log4j successfully configured!!");
                } else {
                    // The webapp is deployed from a .war file, not directly
                    // off the file system so we *cannot* do File IO.
                    // Note that we *won't* be able to use configureAndWatch()
                    // here
                    // because that requires an absolute system file path.
                    // Now let's check if the given configPath actually exists.

                    URL log4jURL = null;
                    try {
                        log4jURL = context.getResource("/" + configPath);
                    } catch (MalformedURLException e) {
                        LogLog.error("MalformedURLException");
                    }
                    if (log4jURL != null) {
                        LogLog.debug("Configuring Log4j from URL at path: / " + configPath);
                        if (isXMLConfigFile) {
                            try {
                                DOMConfigurator.configure(log4jURL);
                            } catch (Exception e) {
                                //report errors to server logs
                                LogLog.error(e.getMessage());
                            }
                        } else {
                            Properties log4jProps = new Properties();
                            try {
                                log4jProps.load(log4jURL.openStream());
                                PropertyConfigurator.configure(log4jProps);
                            } catch (Exception e) {
                                //report errors to server logs
                                LogLog.error(e.getMessage());
                            }
                        }
                        LogLog.debug("SBN - Log4j successfully configured!!");
                    } else {
                        // The given configPath does not exist. So, let's just
                        // let Log4j look for the default files
                        // (log4j.properties or log4j.xml) on its own.
                        displayConfigNotFoundMessage();
                    }

                }
            } else {
                LogLog.error("Zero length Log4j config file path given.");
                displayConfigNotFoundMessage();
            }

        } else {
            LogLog.error("Missing log4j-config init parameter missing.");
            displayConfigNotFoundMessage();
        }

    }

    private void displayConfigNotFoundMessage() {
        LogLog.warn("No Log4j configuration file found at given path. Falling back.");
    }

    /**
     * Cleans up the Logger information from this context.
     *
     * @see org.apache.struts.action.PlugIn#destroy()
     */
    public void destroy() {

        //shutdown this webapp's logger repository
        LogLog.debug("Cleaning up Log4j resources for context...");
        LogLog.debug("Shutting down all loggers and appenders...");
        org.apache.log4j.LogManager.shutdown();
        LogLog.debug("Log4j cleaned up!!!");

    }

    /**
     * @return config
     */
    public String getConfig() {
        return config;
    }

    /**
     * @param string
     */
    public void setConfig(String string) {
        config = string;
    }

}

