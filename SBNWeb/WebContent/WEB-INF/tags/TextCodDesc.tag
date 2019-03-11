<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>

<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"      prefix="bs"%>

 
<%@ attribute name="codice" required="true"%>
<%@ attribute name="descrizione" required="true"%>
<%@ attribute name="size" required="false"%>

<c:set var="tmp">
      <bs:write name="navForm" property="${codice}"/>&#160;<bs:write name="navForm" property="${descrizione}" />
</c:set>


<input type="text" name="dummy_txt" value="${tmp}" size="${size}" readonly="readonly" />