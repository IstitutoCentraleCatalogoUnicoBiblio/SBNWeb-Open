/**
 * TBConnectorWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.iccu.sbn.webservices.esse3.sync;

@SuppressWarnings("rawtypes")
public class TBConnectorWSServiceLocator extends org.apache.axis.client.Service implements it.iccu.sbn.webservices.esse3.sync.TBConnectorWSService {

	private static final long serialVersionUID = 1L;

	public TBConnectorWSServiceLocator() {
    }


    public TBConnectorWSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TBConnectorWSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TBConnectorWS
    private java.lang.String TBConnectorWS_address = "https://koha-urbs.reteurbs.org:8082/services/kohaService.php";

    public java.lang.String getTBConnectorWSAddress() {
        return TBConnectorWS_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TBConnectorWSWSDDServiceName = "TBConnectorWS";

    public java.lang.String getTBConnectorWSWSDDServiceName() {
        return TBConnectorWSWSDDServiceName;
    }

    public void setTBConnectorWSWSDDServiceName(java.lang.String name) {
        TBConnectorWSWSDDServiceName = name;
    }

    public it.iccu.sbn.webservices.esse3.sync.TBConnectorWS getTBConnectorWS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TBConnectorWS_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTBConnectorWS(endpoint);
    }

    public it.iccu.sbn.webservices.esse3.sync.TBConnectorWS getTBConnectorWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.iccu.sbn.webservices.esse3.sync.TBConnectorWSSoapBindingStub _stub = new it.iccu.sbn.webservices.esse3.sync.TBConnectorWSSoapBindingStub(portAddress, this);
            _stub.setPortName(getTBConnectorWSWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTBConnectorWSEndpointAddress(java.lang.String address) {
        TBConnectorWS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.iccu.sbn.webservices.esse3.sync.TBConnectorWS.class.isAssignableFrom(serviceEndpointInterface)) {
                it.iccu.sbn.webservices.esse3.sync.TBConnectorWSSoapBindingStub _stub = new it.iccu.sbn.webservices.esse3.sync.TBConnectorWSSoapBindingStub(new java.net.URL(TBConnectorWS_address), this);
                _stub.setPortName(getTBConnectorWSWSDDServiceName());
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
        if ("TBConnectorWS".equals(inputPortName)) {
            return getTBConnectorWS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:TBConnectorWSService", "TBConnectorWSService");
    }

	private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:TBConnectorWSService", "TBConnectorWS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {

if ("TBConnectorWS".equals(portName)) {
            setTBConnectorWSEndpointAddress(address);
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
