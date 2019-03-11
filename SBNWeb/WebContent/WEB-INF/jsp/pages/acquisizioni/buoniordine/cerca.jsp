<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:errors bundle="acquisizioniMessages" />
	<sbn:navform action="/acquisizioni/buoniordine/cerca.do">
		<div id="divForm">
	content BUONI ORDINE
		</div>

		<div id="divFooter">
	bottoni
		</div>
	</sbn:navform>
</layout:page>
