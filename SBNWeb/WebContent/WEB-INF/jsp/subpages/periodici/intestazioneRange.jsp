<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>



<td align="right">
	<sbn:checkAttivita idControllo="KARDEX_RANGE">
		<span class="testoBold">
			<bean:message key="periodici.kardex.inizio" bundle="periodiciLabels" />
		</span>&nbsp; <bs:write name="navForm" property="kardex.intestazione.from" />
	</sbn:checkAttivita>
</td>
<td align="right">
	<sbn:checkAttivita idControllo="KARDEX_RANGE">
		<span class="testoBold">
			<bean:message key="periodici.kardex.fine" bundle="periodiciLabels" />
		</span>&nbsp; <bs:write name="navForm" property="kardex.intestazione.to" />
	</sbn:checkAttivita>
</td>