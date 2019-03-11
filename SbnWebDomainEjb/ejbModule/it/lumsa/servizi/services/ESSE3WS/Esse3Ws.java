/**
 * Esse3Ws.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.lumsa.servizi.services.ESSE3WS;

public interface Esse3Ws extends java.rmi.Remote {
    public int fn_dologin(java.lang.String username, java.lang.String password, javax.xml.rpc.holders.StringHolder sid) throws java.rmi.RemoteException;
    public int fn_retrieve_xml_x(java.lang.String sid, java.lang.String xmlIn, javax.xml.rpc.holders.StringHolder xmlOut) throws java.rmi.RemoteException;
    public int fn_retrieve_xml_lx(java.lang.String username, java.lang.String password, java.lang.String xmlIn, javax.xml.rpc.holders.StringHolder xmlOut) throws java.rmi.RemoteException;
    public int fn_retrieve_xml_px(java.lang.String xmlIn, javax.xml.rpc.holders.StringHolder xmlOut) throws java.rmi.RemoteException;
    public int fn_dologout(java.lang.String sid) throws java.rmi.RemoteException;
    public int fn_retrieve_xml(java.lang.String sid, java.lang.String retrieve, java.lang.String params, javax.xml.rpc.holders.StringHolder xml) throws java.rmi.RemoteException;
    public int fn_retrieve_xml_l(java.lang.String username, java.lang.String password, java.lang.String retrieve, java.lang.String params, javax.xml.rpc.holders.StringHolder xml) throws java.rmi.RemoteException;
    public int fn_retrieve_xml_p(java.lang.String retrieve, java.lang.String params, javax.xml.rpc.holders.StringHolder xml) throws java.rmi.RemoteException;
}
