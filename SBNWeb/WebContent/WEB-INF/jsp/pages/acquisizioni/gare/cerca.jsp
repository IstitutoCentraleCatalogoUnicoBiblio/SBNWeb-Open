<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:errors bundle="acquisizioniMessages" />
	<sbn:navform action="/acquisizioni/gare/cerca.do">
		<div id="divForm">
	content GARE
		</div>

		<div id="divFooter">
	bottoni
		</div>
	</sbn:navform>
</layout:page>
