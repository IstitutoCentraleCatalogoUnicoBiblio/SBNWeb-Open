<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>


<%@page import="it.iccu.sbn.web.actionforms.amministrazionesistema.sistemimetropolitani.SistemiMetropolitaniForm;"%>
<html:xhtml/>
<layout:page>

<sbn:navform action="/amministrazionesistema/sistemiMetropolitani.do">

		<div id="divForm">
			<div id="divMessaggio">
					<sbn:errors bundle="amministrazioneSistemaMessages" />
			</div>
		</div>

	</sbn:navform>
</layout:page>
