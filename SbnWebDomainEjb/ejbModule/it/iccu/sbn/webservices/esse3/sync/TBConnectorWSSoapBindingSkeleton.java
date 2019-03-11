/**
 * TBConnectorWSSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.iccu.sbn.webservices.esse3.sync;

@SuppressWarnings("rawtypes")
public class TBConnectorWSSoapBindingSkeleton implements it.iccu.sbn.webservices.esse3.sync.TBConnectorWS, org.apache.axis.wsdl.Skeleton {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private it.iccu.sbn.webservices.esse3.sync.TBConnectorWS impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:TBConnectorWSService", "pass"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:TBConnectorWSService", "entity"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:TBConnectorWSService", "op"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:TBConnectorWSService", "sourceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false),
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:TBConnectorWSService", "cv-xml"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, false, false),
        };
        _oper = new org.apache.axis.description.OperationDesc("sync", _params, new javax.xml.namespace.QName("urn:TBConnectorWSService", "syncReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:TBConnectorWSService", "sync"));
        _oper.setSoapAction("urn:TBConnectorWSService/sync");
        _myOperationsList.add(_oper);
        if (_myOperations.get("sync") == null) {
            _myOperations.put("sync", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("sync")).add(_oper);
    }

    public TBConnectorWSSoapBindingSkeleton() {
        this.impl = new it.iccu.sbn.webservices.esse3.sync.TBConnectorWSSoapBindingImpl();
    }

    public TBConnectorWSSoapBindingSkeleton(it.iccu.sbn.webservices.esse3.sync.TBConnectorWS impl) {
        this.impl = impl;
    }
    public java.lang.String sync(java.lang.String pass, java.lang.String entity, java.lang.String op, java.lang.String sourceId, byte[] cvXml) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.sync(pass, entity, op, sourceId, cvXml);
        return ret;
    }

}
