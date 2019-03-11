<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<hr />

<table>
	<tr>
		<td class="testoBold testoTop" bgcolor="#FFCC99"><bean:message
			key="periodici.esame.titolo" bundle="periodiciLabels" />
		</td>
		<td><sbn:linkwrite name="navForm"
			property="esame.titolo" keyProperty="bidSelezionato"
			buttonKey="button.periodici.esamina.bid" bundle="periodiciLabels"
			title="Esame" checkAttivita="CAMBIO_BID"/>
		</td>
	</tr>
	<tr>
		<td class="testoBold" bgcolor="#FFCC99"><bean:message
			key="periodici.esame.numero_std" bundle="periodiciLabels" />
		</td>
		<td>
			<bs:write name="navForm" property="esame.numero_std" />
		</td>
	</tr>
	<tr>
		<td class="testoBold" bgcolor="#FFCC99"><bean:message
			key="periodici.esame.periodicita" bundle="periodiciLabels" />
		</td>
		<td>
			<bs:write name="navForm" property="esame.tipoPeriodicita" />
		</td>
	</tr>
	<logic:notEmpty name="navForm" property="esame.descrizioneLegami" >
		<tr>
			<td class="testoBold testoTop" bgcolor="#FFCC99"><bean:message
				key="periodici.esame.legami" bundle="periodiciLabels" />
			</td>
			<td><sbn:linkwrite name="navForm"
				property="esame.descrizioneLegami" keyProperty="bidSelezionato"
				buttonKey="button.periodici.esamina.bid" bundle="periodiciLabels"
				title="Esame" checkAttivita="CAMBIO_BID"/>
			</td>
		</tr>
	</logic:notEmpty>
</table>

<hr />

