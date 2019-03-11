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
	<sbn:navform
		action="/documentofisico/esameCollocazioni/esameCollocEsaminaPosseduto.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<!--  biblioteca -->
		<table width="100%" border="0">
			<tr>
				<td colspan="3">
				<div class="etichetta"><bean:message
					key="documentofisico.bibliotecaT" bundle="documentoFisicoLabels" />
				<html:text readonly="true" styleId="testoNormale" property="codBib"
					size="5" maxlength="3"></html:text> <bean-struts:write
					name="esameCollocEsaminaPossedutoForm" property="descrBib" /></div>
				</td>
			</tr>
		</table>
		<br>

		<table width="100%"
			style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
			<tr valign="top">
				<td class="etichetta"><bean:message
					key="documentofisico.notiziaCorrT" bundle="documentoFisicoLabels" />:
				<span class="etichetta"></span>
					<bean-struts:write
					name="esameCollocEsaminaPossedutoForm" property="bid" />
					<bean-struts:write
					name="esameCollocEsaminaPossedutoForm" property="titolo" /></td>
			</tr>
		</table>
		<br>
		<table width="100%" border="0">
			<tr height="30">
				<c:choose>
					<c:when test="${esameCollocEsaminaPossedutoForm.tab1}">
						<td width="27%" class="schedaOn">
						<div align="center">Inventari del Titolo</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="27%" class="schedaOff">
						<div align="center"><html:submit property="methodEsaminaPoss" styleClass="sintButtonLinkDefault">
											<bean:message key="documentofisico.esaminaPossTab1" bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${esameCollocEsaminaPossedutoForm.tab2}">
						<td width="39%" class="schedaOn">
						<div align="center">Collocazioni del Titolo</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="39%" class="schedaOff">
						<div align="center"><html:submit property="methodEsaminaPoss" styleClass="sintButtonLinkDefault">
											<bean:message key="documentofisico.esaminaPossTab2" bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${esameCollocEsaminaPossedutoForm.tab3}">
						<td width="39%" class="schedaOn">
						<div align="center">Esemplari del Titolo</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="39%" class="schedaOff">
						<div align="center"><html:submit property="methodEsaminaPoss" styleClass="sintButtonLinkDefault">
											<bean:message key="documentofisico.esaminaPossTab3" bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		<c:choose>
			<c:when test="${esameCollocEsaminaPossedutoForm.tab1}">
				<sbn:blocchi numBlocco="bloccoSelezionato1" numNotizie="totRighe1"
					totBlocchi="totBlocchi1" elementiPerBlocco="elemPerBlocchi"
					parameter="methodEsaminaPoss"></sbn:blocchi>
				<jsp:include
					page="/WEB-INF/jsp/subpages/documentofisico/esameCollocazioni/esaminaPossedutoTab1.jsp" />
				<sbn:blocchi numBlocco="bloccoSelezionato1" numNotizie="totRighe1"
					totBlocchi="totBlocchi1" elementiPerBlocco="elemPerBlocchi"
					parameter="methodEsaminaPoss" bottom="true"></sbn:blocchi>
			</c:when>
				<c:when test="${esameCollocEsaminaPossedutoForm.tab2}">
				<sbn:blocchi numBlocco="bloccoSelezionato2" numNotizie="totRighe2"
					totBlocchi="totBlocchi2" elementiPerBlocco="elemPerBlocchi"
					parameter="methodEsaminaPoss"></sbn:blocchi>
				<jsp:include
					page="/WEB-INF/jsp/subpages/documentofisico/esameCollocazioni/esaminaPossedutoTab2.jsp" />
				<sbn:blocchi numBlocco="bloccoSelezionato2" numNotizie="totRighe2"
					totBlocchi="totBlocchi2" elementiPerBlocco="elemPerBlocchi"
					parameter="methodEsaminaPoss" bottom="true"></sbn:blocchi>
			</c:when>
				<c:when test="${esameCollocEsaminaPossedutoForm.tab3}">
				<sbn:blocchi numBlocco="bloccoSelezionato3" numNotizie="totRighe3"
					totBlocchi="totBlocchi3" elementiPerBlocco="elemPerBlocchi"
					parameter="methodEsaminaPoss"></sbn:blocchi>
				<jsp:include
					page="/WEB-INF/jsp/subpages/documentofisico/esameCollocazioni/esaminaPossedutoTab3.jsp" />
				<sbn:blocchi numBlocco="bloccoSelezionato3" numNotizie="totRighe3"
					totBlocchi="totBlocchi3" elementiPerBlocco="elemPerBlocchi"
					parameter="methodEsaminaPoss" bottom="true"></sbn:blocchi>
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose></div>

		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center">
			<tr>
				<td><c:choose>
					<c:when test="${esameCollocEsaminaPossedutoForm.tastoAltreBib}">
						<html:submit styleClass="pulsanti" property="methodEsaminaPoss">
							<bean:message key="documentofisico.bottone.altreBib" bundle="documentoFisicoLabels" />
						</html:submit>
					</c:when>
				</c:choose> <c:choose>
					<c:when test="${esameCollocEsaminaPossedutoForm.tab1}">
						<html:submit styleClass="pulsanti" property="methodEsaminaPoss">
							<bean:message key="documentofisico.bottone.disponibilita"
								bundle="documentoFisicoLabels" />
						</html:submit>
						<html:submit styleClass="pulsanti" property="methodEsaminaPoss">
							<bean:message key="documentofisico.bottone.possessori"
								bundle="documentoFisicoLabels" />
						</html:submit>
						<html:submit styleClass="pulsanti" property="methodEsaminaPoss">
							<bean:message key="documentofisico.bottone.esamina" bundle="documentoFisicoLabels" />
						</html:submit>
					</c:when>
					<c:when test="${esameCollocEsaminaPossedutoForm.tab2}">
						<html:submit styleClass="pulsanti" property="methodEsaminaPoss">
							<bean:message key="documentofisico.bottone.inventari"
								bundle="documentoFisicoLabels" />
						</html:submit>
						<html:submit styleClass="pulsanti" property="methodEsaminaPoss">
							<bean:message key="documentofisico.bottone.esEsempl" bundle="documentoFisicoLabels" />
						</html:submit>
					</c:when>
					<c:when test="${esameCollocEsaminaPossedutoForm.tab3}">
						<html:submit styleClass="pulsanti" property="methodEsaminaPoss">
							<bean:message key="documentofisico.bottone.collocazioni"
								bundle="documentoFisicoLabels" />
						</html:submit>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
				<html:submit styleClass="pulsanti" property="methodEsaminaPoss">
					<bean:message key="documentofisico.bottone.indietro"
						bundle="documentoFisicoLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>
	</sbn:navform>

</layout:page>
