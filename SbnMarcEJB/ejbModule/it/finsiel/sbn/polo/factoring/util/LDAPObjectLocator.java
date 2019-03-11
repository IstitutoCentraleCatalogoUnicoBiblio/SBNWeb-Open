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
import it.finsiel.sbn.polo.exception.ServiceLocatorException;

import java.util.Hashtable;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NoInitialContextException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Category;

public class LDAPObjectLocator {

    /** log del factoring */
    private static Category log = Category
            .getInstance("iccu.box.servicelocator.ejb.LDAPObjectLocator");

    private static int contatore_accessi_ldap = 0;

    private static int LDAP_CLOSE_DOPO_OPERAZIONI = 100;

    private DirContext idc;

    private Hashtable env;

    private static LDAPObjectLocator me;

    private static void init() {
        try {
            log.info("Eseguo init del LDAPObjectLocator");
            me = new LDAPObjectLocator();
            log.info("Init LDAPObjectLocator riuscita");
        } catch (Exception se) {
            log.error("Eccezione init", se);
        }
    }

    private static void reinit() {
        try {
            log.info("Eseguo reinit del LDAPObjectLocator");
            getInstance().idc = new InitialDirContext(getInstance().env);
            log.info("Reinit LDAPObjectLocator riuscita");
        } catch (Exception se) {
            log.error("Eccezione reinit", se);
        }
    }

    static {
        init();
        try {
            LDAP_CLOSE_DOPO_OPERAZIONI = Integer.parseInt(IndiceServiceLocator.getProperty("LDAP_CLOSE_DOPO_OPERAZIONI"));
        } catch(Exception e) {
            log.error("Errore init LDAPObjectLocator LDAP_CLOSE_DOPO_OPERAZIONI, utilizzo valore di default. " + e);
        }
    }

    private LDAPObjectLocator() throws javax.naming.NamingException {
        env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, IndiceServiceLocator
                .getProperty("LDAP_INITIAL_CONTEXT_FACTORY"));
        env.put(Context.PROVIDER_URL, IndiceServiceLocator
                .getProperty("LDAP_PROVIDER_URL"));
        env.put(Context.SECURITY_AUTHENTICATION, IndiceServiceLocator
                .getProperty("LDAP_SECURITY_AUTHENTICATION"));
        env.put(Context.SECURITY_PRINCIPAL, IndiceServiceLocator
                .getProperty("LDAP_SECURITY_PRINCIPAL")); // specify the
                                                            // username
        env.put(Context.SECURITY_CREDENTIALS, IndiceServiceLocator
                .getProperty("LDAP_SECURITY_CREDENTIALS")); // specify the
                                                            // password
        idc = new InitialDirContext(env);
    }

    static public LDAPObjectLocator getInstance() {
        return me;
    }

    public void bind(String fullyQualifiedName, Object obj, Attributes attrs)
            throws ServiceLocatorException {
            int commaPos = fullyQualifiedName.indexOf(",");
            String rdn = fullyQualifiedName.substring(3,
                    (commaPos == -1) ? fullyQualifiedName.length() : commaPos);
            attrs.put("cn", rdn);
            bindIntelligente(fullyQualifiedName, obj, attrs);
    }

    public void bind(String fullyQualifiedName, Object obj)
            throws ServiceLocatorException {
            bindIntelligente(fullyQualifiedName, obj, null);
    }

    public void unbind(String fullyQualifiedName)
            throws javax.naming.NamingException {
        //verificaNumeroAccessi();
        idc.unbind(fullyQualifiedName);
    }

    public DirContext getInitialDirContext() {
        return idc;
    }

    public Hashtable getEnv() {
        return env;
    }


    public Object lookup(String fullyQualifiedName)
            throws ServiceLocatorException {
        Object obj = null;
        //verificaNumeroAccessi();

        // Occassionally the directory context will timeout. Try one more
        // time before giving up.
        try {

            // Search the specified object
            obj = idc.lookup(fullyQualifiedName);

        } catch (CommunicationException e) {

            // If not a "Socket closed." error then rethrow.
            if (e.getMessage().indexOf("Socket closed") < 0) {
                // If contains the work closed. Then assume socket is closed.
                // If message is null, assume the worst and allow the
                // connection to be closed.
                if (e.getMessage() != null
                        && e.getMessage().indexOf("closed") < 0)
                    throw new ServiceLocatorException(e);
            }

            // log the exception so we know it's there.
            log.error("Eccezione CommunicationException", e);

            // Try 3 times to reopen
            for (int i = 0; i < 3; i++) {
                try {
                    // close the connection so we know it will be reopened.
                    idc.close();

                    // open a new directory context.
                    reinit();

                    // Try the lookup again.
                    obj = idc.lookup(fullyQualifiedName);
                    // if everything is ok exit
                    break;
                } catch (Exception ex) {
                    log.error("Reopen ldap non riuscita; " + ex);
                    if (i == 2) {
                        throw new ServiceLocatorException(ex);
                    }
                }
            }
        } catch (NoInitialContextException e) {


            // log the exception so we know it's there.
            log.error("Eccezione NoInitialContextException", e);

            // Try 3 times to reopen
            for (int i = 0; i < 3; i++) {
                try {
                    // close the connection so we know it will be reopened.
                    idc.close();

                    // open a new directory context.
                    reinit();

                    // Try the lookup again.
                    obj = idc.lookup(fullyQualifiedName);
                    // if everything is ok exit
                    break;
                } catch (Exception ex) {
                    log.error("Reopen ldap non riuscita; " + ex);
                    if (i == 2) {
                        throw new ServiceLocatorException(ex);
                    }
                }
            }
        } catch (NameNotFoundException nnfe) {
            log.info("Oggetto non trovato; " + nnfe);
        } catch (Exception e) {
            log.error("Eccezione sul lookup di LDAP", e);
            throw new ServiceLocatorException(e);
        }
        return obj;
    }

    public NamingEnumeration list(String fullyQualifiedName)
            throws ServiceLocatorException {
        NamingEnumeration obj = null;
        //verificaNumeroAccessi();

        // Occassionally the directory context will timeout. Try one more
        // time before giving up.
        try {

            // Search the specified object
            obj = idc.list(fullyQualifiedName);

        } catch (CommunicationException e) {

            // If not a "Socket closed." error then rethrow.
            if (e.getMessage().indexOf("Socket closed") < 0) {
                // If contains the work closed. Then assume socket is closed.
                // If message is null, assume the worst and allow the
                // connection to be closed.
                if (e.getMessage() != null
                        && e.getMessage().indexOf("closed") < 0)
                    throw new ServiceLocatorException(e);
            }

            // log the exception so we know it's there.
            log.error("Eccezione CommunicationException", e);

            // Try 3 times to reopen
            for (int i = 0; i < 3; i++) {
                try {
                    // close the connection so we know it will be reopened.
                    idc.close();

                    // open a new directory context.
                    reinit();

                    // Try the lookup again.
                    obj = idc.list(fullyQualifiedName);
                    // if everything is ok exit
                    break;
                } catch (Exception ex) {
                    log.error("Reopen ldap non riuscita; " + ex);
                    if (i == 2) {
                        throw new ServiceLocatorException(ex);
                    }
                }
            }
        } catch (NoInitialContextException e) {


            // log the exception so we know it's there.
            log.error("Eccezione NoInitialContextException", e);

            // Try 3 times to reopen
            for (int i = 0; i < 3; i++) {
                try {
                    // close the connection so we know it will be reopened.
                    idc.close();

                    // open a new directory context.
                    reinit();

                    // Try the lookup again.
                    obj = idc.list(fullyQualifiedName);
                    // if everything is ok exit
                    break;
                } catch (Exception ex) {
                    log.error("Reopen ldap non riuscita; " + ex);
                    if (i == 2) {
                        throw new ServiceLocatorException(ex);
                    }
                }
            }
        } catch (NameNotFoundException nnfe) {
            log.info("Oggetto non trovato; " + nnfe);
        } catch (Exception e) {
            log.error("Eccezione sul list di LDAP", e);
            throw new ServiceLocatorException(e);
        }
        return obj;
    }

    public NamingEnumeration listBindings(String fullyQualifiedName)
            throws ServiceLocatorException {
        NamingEnumeration obj = null;
        //verificaNumeroAccessi();

        // Occassionally the directory context will timeout. Try one more
        // time before giving up.
        try {

            // Search the specified object
            obj = idc.listBindings(fullyQualifiedName);

        } catch (CommunicationException e) {

            // If not a "Socket closed." error then rethrow.
            if (e.getMessage().indexOf("Socket closed") < 0) {
                // If contains the work closed. Then assume socket is closed.
                // If message is null, assume the worst and allow the
                // connection to be closed.
                if (e.getMessage() != null
                        && e.getMessage().indexOf("closed") < 0)
                    throw new ServiceLocatorException(e);
            }

            // log the exception so we know it's there.
            log.error("Eccezione CommunicationException", e);

            // Try 3 times to reopen
            for (int i = 0; i < 3; i++) {
                try {
                    // close the connection so we know it will be reopened.
                    idc.close();

                    // open a new directory context.
                    reinit();

                    // Try the lookup again.
                    obj = idc.listBindings(fullyQualifiedName);
                    // if everything is ok exit
                    break;
                } catch (Exception ex) {
                    log.error("Reopen ldap non riuscita; " + ex);
                    if (i == 2) {
                        throw new ServiceLocatorException(ex);
                    }
                }
            }
        } catch (NoInitialContextException e) {


            // log the exception so we know it's there.
            log.error("Eccezione NoInitialContextException", e);

            // Try 3 times to reopen
            for (int i = 0; i < 3; i++) {
                try {
                    // close the connection so we know it will be reopened.
                    idc.close();

                    // open a new directory context.
                    reinit();

                    // Try the lookup again.
                    obj = idc.listBindings(fullyQualifiedName);
                    // if everything is ok exit
                    break;
                } catch (Exception ex) {
                    log.error("Reopen ldap non riuscita; " + ex);
                    if (i == 2) {
                        throw new ServiceLocatorException(ex);
                    }
                }
            }
        } catch (NameNotFoundException nnfe) {
            log.info("Oggetto non trovato; " + nnfe);
        } catch (Exception e) {
            log.error("Eccezione sul listBindings di LDAP", e);
            throw new ServiceLocatorException(e);
        }
        return obj;
    }

    public NamingEnumeration search(String context,String name)
            throws ServiceLocatorException {
        NamingEnumeration obj = null;
        //verificaNumeroAccessi();

        // Occassionally the directory context will timeout. Try one more
        // time before giving up.
        try {

            // Search the specified object
            obj = idc.search(context,name,null);

        } catch (CommunicationException e) {

            // If not a "Socket closed." error then rethrow.
            if (e.getMessage().indexOf("Socket closed") < 0) {
                // If contains the work closed. Then assume socket is closed.
                // If message is null, assume the worst and allow the
                // connection to be closed.
                if (e.getMessage() != null
                        && e.getMessage().indexOf("closed") < 0)
                    throw new ServiceLocatorException(e);
            }

            // log the exception so we know it's there.
            log.error("Eccezione CommunicationException", e);

            // Try 3 times to reopen
            for (int i = 0; i < 3; i++) {
                try {
                    // close the connection so we know it will be reopened.
                    idc.close();

                    // open a new directory context.
                    reinit();

                    // Try the lookup again.
                    obj = idc.search(context,name,null);
                    // if everything is ok exit
                    break;
                } catch (Exception ex) {
                    log.error("Reopen ldap non riuscita; " + ex);
                    if (i == 2) {
                        throw new ServiceLocatorException(ex);
                    }
                }
            }
        } catch (NoInitialContextException e) {


            // log the exception so we know it's there.
            log.error("Eccezione NoInitialContextException", e);

            // Try 3 times to reopen
            for (int i = 0; i < 3; i++) {
                try {
                    // close the connection so we know it will be reopened.
                    idc.close();

                    // open a new directory context.
                    reinit();

                    // Try the lookup again.
                    obj = idc.search(context,name,null);
                    // if everything is ok exit
                    break;
                } catch (Exception ex) {
                    log.error("Reopen ldap non riuscita; " + ex);
                    if (i == 2) {
                        throw new ServiceLocatorException(ex);
                    }
                }
            }
        } catch (NameNotFoundException nnfe) {
            log.info("Oggetto non trovato; " + nnfe);
        } catch (Exception e) {
            log.error("Eccezione sul search di LDAP", e);
            throw new ServiceLocatorException(e);
        }
        return obj;
    }

    /**
     * Esegue la bind, se ci sono dei problemi riesegue la init del contesto.
     * @param fullyQualifiedName
     * @param obj
     * @param attrs
     * @throws ServiceLocatorException
     */
    public void bindIntelligente(String fullyQualifiedName, Object obj,
            Attributes attrs) throws ServiceLocatorException {
        //verificaNumeroAccessi();
        try {
            if (attrs == null) {
                idc.rebind(fullyQualifiedName, obj);
            } else {
                idc.rebind(fullyQualifiedName, obj, attrs);
            }
        } catch (CommunicationException e) {

            // If not a "Socket closed." error then rethrow.
            if (e.getMessage().indexOf("Socket closed") < 0) {
                // If contains the work closed. Then assume socket is closed.
                // If message is null, assume the worst and allow the
                // connection to be closed.
                if (e.getMessage() != null
                        && e.getMessage().indexOf("closed") < 0)
                    throw new ServiceLocatorException(e);
            }

            // log the exception so we know it's there.
            log.error("Eccezione CommunicationException", e);


            // Try 3 times to reopen
            for (int i = 0; i < 3; i++) {
                try {
                    // close the connection so we know it will be reopened.
                    idc.close();

                    // open a new directory context.
                    reinit();

                    // Try the bind again.
                    if (attrs == null) {
                        idc.rebind(fullyQualifiedName, obj);
                    } else {
                        idc.rebind(fullyQualifiedName, obj, attrs);
                    }
                    // if everything is ok exit
                    break;
                } catch (Exception ex) {
                    log.error("Reopen ldap non riuscita; " + ex);
                    if (i == 2) {
                        throw new ServiceLocatorException(ex);
                    }
                }
            }
        } catch (NoInitialContextException e) {


            // log the exception so we know it's there.
            log.error("Eccezione NoInitialContextException", e);

            // Try 3 times to reopen
            for (int i = 0; i < 3; i++) {
                try {
                    // close the connection so we know it will be reopened.
                    idc.close();

                    // open a new directory context.
                    reinit();

                    // Try the lookup again.
                    if (attrs == null) {
                        idc.rebind(fullyQualifiedName, obj);
                    } else {
                        idc.rebind(fullyQualifiedName, obj, attrs);
                    }
                    // if everything is ok exit
                    // if everything is ok exit
                    break;
                } catch (Exception ex) {
                    log.error("Reopen ldap non riuscita; " + ex);
                    if (i == 2) {
                        throw new ServiceLocatorException(ex);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Eccezione sul bind di LDAP", e);
            throw new ServiceLocatorException(e);
        }

    }

    /**
     * Verifica il numero di accessi ad ldap, e nel caso chiude e riapre il contesto.
     */
    private synchronized void verificaNumeroAccessi() {
        contatore_accessi_ldap++;
        if (contatore_accessi_ldap >= LDAP_CLOSE_DOPO_OPERAZIONI) {
            contatore_accessi_ldap = 0;
            // Try 3 times to reopen
            for (int i = 0; i < 3; i++) {
                try {
                    idc.close();

                    // open a new directory context.
                    reinit();

                    // if everything is ok exit
                    break;
                } catch (Exception ex) {
                    log.error("Reopen ldap non riuscita; " + ex);
                    if (i == 2) {
                        throw new ServiceLocatorException(ex);
                    }
                }
            }
        }
    }
}
