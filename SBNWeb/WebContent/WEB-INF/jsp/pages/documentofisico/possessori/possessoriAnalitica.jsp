<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/documentofisico/possessori/analiticaPossessori.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<!-- Visualizza reticolo del pid selezionato --> <sbn:tree
			root="treeElementViewPossessori" divClass="analitica" propertyRadio="selezRadio"
			propertyCheck="selezCheck" imagesPath="/sbn/images/tree/" enableSubmit="true" /> <!-- Fine Visualizza reticolo del pid selezionato -->
		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><sbn:checkAttivita idControllo="inventari">
					<html:submit property="methodPossAnalitica">
						<bean:message key="analitica.button.inventari" bundle="documentoFisicoLabels" />
					</html:submit>
				</sbn:checkAttivita> <sbn:checkAttivita idControllo="possessori">
					<html:submit property="methodPossAnalitica">
						<bean:message key="analitica.button.nuovolegame" bundle="documentoFisicoLabels" />
					</html:submit>
					<html:submit property="methodPossAnalitica">
						<bean:message key="analitica.button.canclegame" bundle="documentoFisicoLabels" />
					</html:submit>
					<html:submit property="methodPossAnalitica">
						<bean:message key="analitica.button.modlegame" bundle="documentoFisicoLabels" />
					</html:submit>
				</sbn:checkAttivita></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
