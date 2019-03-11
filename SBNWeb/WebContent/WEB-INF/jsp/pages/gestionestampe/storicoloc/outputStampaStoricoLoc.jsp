<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="beanT"%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<%@ page contentType="text/html; charset=UTF-8" %>




<html:xhtml />
<layout:page>
	<sbn:errors bundle="gestioneStampeMessages" />
	<sbn:navform action="/gestionestampe/storicoloc/outputStampaStoricoLoc.do">
	<div id="content">

OUTPUT STAMPA CATALOGO
<HR>
<beanT:write name="outputStampaStoricoLocForm" property="stampaStoricoLoc" />


<HR>
	</div>

	</sbn:navform>
</layout:page>
