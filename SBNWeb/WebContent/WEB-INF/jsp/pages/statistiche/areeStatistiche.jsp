<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>


<%@page import="it.iccu.sbn.web.actionforms.statistiche.AreeStatisticheForm;"%>
<html:xhtml/>
<layout:page>
	<sbn:navform action="/statistiche/areeStatistiche.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<table border="0">
			<tr>
				<td width=800 align="left" style="font-weight: bold; font-size: 15px"><bean:message
					key="statistiche.intestazioneAree" bundle="statisticheLabels" /></td>
			</tr>
		</table>
		<br />
		<logic:iterate id="item" property="elencoAree" name="areeStatisticheForm" indexId="idx">
			<sbn:checkAttivita idControllo="${item.idAreaSezione}">
				<table border="0" cellspacing="15">
					<tr>
						<td class="etichetta" width="300"><bean-struts:write name="item"
							property="descrizione" /></td>
						<td><html:submit styleClass="pulsanti"
							property="${sbn:getUniqueButtonName(areeStatisticheForm.SUBMIT_TESTO_PULSANTE, idx)}">&gt;
								</html:submit></td>
					</tr>
				</table>
			</sbn:checkAttivita>
		</logic:iterate></div>
	</sbn:navform>
</layout:page>
