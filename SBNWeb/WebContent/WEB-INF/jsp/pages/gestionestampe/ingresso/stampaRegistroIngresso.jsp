<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>


<html:xhtml />
<layout:page>

	<!-- sbn:errors bundle="gestioneStampeMessages" /> -->
	<sbn:navform
		action="/gestionestampe/ingresso/stampaRegistroIngresso.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<table width="100%" align="center">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="etichetta" width="10%"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /></td>
				<td><html:text disabled="true" styleId="testoNormale" property="codBib" size="5"
					maxlength="3"></html:text> <html:submit disabled="false" title="Lista Biblioteche"
					styleClass="buttonImageListaSezione" property="methodStampaRegistroIngresso">
					<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
				</html:submit><bean-struts:write name="stampaRegistroIngressoForm" property="descrBib" /></td>
			</tr>
			</table>
			<BR>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr height="30">
					<c:choose>
						<c:when
							test="${stampaRegistroIngressoForm.tipoDiStampa eq 'RegistroIngresso'}">
							<td width="50%" class="schedaOn">
							<div align="center">Registro d'ingresso</div>
							</td>
						</c:when>
						<c:otherwise>
							<td width="50%" class="schedaOff">
							<div align="center" styleId="etichetta"><html:submit
								property="methodStampaRegistroIngresso"	styleClass="sintButtonLinkDefault">
								<bean:message key="reg.selRegistroIngresso"
									bundle="gestioneStampeLabels" />
							</html:submit></div>
							</td>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when
							test="${stampaRegistroIngressoForm.tipoDiStampa eq 'StatisticheRegistro'}">
							<td width="50%" class="schedaOn">
							<div align="center">Statistiche d'Ingresso</div>
							</td>
						</c:when>
						<c:otherwise>
							<td width="50%" class="schedaOff">
							<div align="center" styleId="etichetta"><html:submit
								property="methodStampaRegistroIngresso"	styleClass="sintButtonLinkDefault">
								<bean:message key="reg.selRegistroStatistiche"
									bundle="gestioneStampeLabels" />
							</html:submit></div>
							</td>
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
			<c:choose>
				<c:when
					test="${stampaRegistroIngressoForm.tipoDiStampa eq 'RegistroIngresso'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionestampe/ingresso/tabRegistroIngresso.jsp" />
				</c:when>
				<c:when
					test="${stampaRegistroIngressoForm.tipoDiStampa eq 'StatisticheRegistro'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionestampe/ingresso/tabRegistroStatistiche.jsp" />
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
		</table>
		<HR>
		<jsp:include flush="true" page="../common/tipoStampa.jsp" />
		<HR>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center" border="0" style="height: 40px">
			<tr>
				<c:choose>
					<c:when test="${stampaRegistroIngressoForm.disable == false}">
						<td><html:submit property="methodStampaRegistroIngresso">
							<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
						</html:submit><html:submit property="methodStampaRegistroIngresso">
							<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<td><html:submit property="methodStampaRegistroIngresso">
							<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>

		</div>
	</sbn:navform>
</layout:page>
