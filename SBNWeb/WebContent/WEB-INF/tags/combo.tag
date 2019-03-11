<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>

<%@ attribute name="name" required="true"%>
<%@ attribute name="combo" required="true"%>
<%@ attribute name="button" required="true"%>
<%@ attribute name="parameter" required="true"%>
<%@ attribute name="bundle" required="true"%>
<%@ attribute name="label" required="true"%>
<%@ attribute name="property" required="true"%>

<bean-struts:size id="comboSize" name="${name}" property="${combo}" />

<logic:greaterThan name="comboSize" value="1">
	<div style="float:left; display:inline;">
	<bean:message key="${label}" bundle="${bundle}" />:&nbsp;
	<html:select styleClass="testoNormale" property="${property}">
		<sbn:localOptionsCollection property="${combo}" value="codice"
			label="descrizione" />
	</html:select>

	<html:submit property="${parameter}">
		<bean:message key="${button}" bundle="${bundle}" />
	</html:submit>
	</div>
</logic:greaterThan>