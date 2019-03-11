<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
	<sbn:errors bundle="gestioneStampeMessages" />
	<sbn:navform action="/gestionestampe/utenti/Tesserino.do">

		<div id="content">
		<hr>

			<jsp:include flush="true" page="../common/tipoStampa.jsp" />

			<hr>

			<table align="center" border="0" style="height:40px">
				<tr>
					<td>
						<html:submit styleClass="pulsanti" property="methodStampaTesserino">
							<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
						</html:submit>

						<html:submit styleClass="pulsanti" property="methodStampaTesserino">
							<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
						</html:submit>
					</td>
				</tr>
			</table>

		</div>

	</sbn:navform>
</layout:page>
