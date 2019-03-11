/**
 * Esse3WsServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.lumsa.servizi.services.ESSE3WS;

public class Esse3WsServiceLocator extends org.apache.axis.client.Service implements it.lumsa.servizi.services.ESSE3WS.Esse3WsService {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4105478236284189528L;

	public Esse3WsServiceLocator() {
    }


    public Esse3WsServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Esse3WsServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ESSE3WS
    private java.lang.String ESSE3WS_address = "https://servizi.lumsa.it/services/ESSE3WS";

    public java.lang.String getESSE3WSAddress() {
        return ESSE3WS_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ESSE3WSWSDDServiceName = "ESSE3WS";

    public java.lang.String getESSE3WSWSDDServiceName() {
        return ESSE3WSWSDDServiceName;
    }

    public void setESSE3WSWSDDServiceName(java.lang.String name) {
        ESSE3WSWSDDServiceName = name;
    }

    public it.lumsa.servizi.services.ESSE3WS.Esse3Ws getESSE3WS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ESSE3WS_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getESSE3WS(endpoint);
    }

    public it.lumsa.servizi.services.ESSE3WS.Esse3Ws getESSE3WS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.lumsa.servizi.services.ESSE3WS.ESSE3WSSoapBindingStub _stub = new it.lumsa.servizi.services.ESSE3WS.ESSE3WSSoapBindingStub(portAddress, this);
            _stub.setPortName(getESSE3WSWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setESSE3WSEndpointAddress(java.lang.String address) {
        ESSE3WS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.lumsa.servizi.services.ESSE3WS.Esse3Ws.class.isAssignableFrom(serviceEndpointInterface)) {
                it.lumsa.servizi.services.ESSE3WS.ESSE3WSSoapBindingStub _stub = new it.lumsa.servizi.services.ESSE3WS.ESSE3WSSoapBindingStub(new java.net.URL(ESSE3WS_address), this);
                _stub.setPortName(getESSE3WSWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ESSE3WS".equals(inputPortName)) {
            return getESSE3WS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://servizi.lumsa.it/services/ESSE3WS", "Esse3WsService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://servizi.lumsa.it/services/ESSE3WS", "ESSE3WS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {

if ("ESSE3WS".equals(portName)) {
            setESSE3WSEndpointAddress(address);
        }
        else
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
