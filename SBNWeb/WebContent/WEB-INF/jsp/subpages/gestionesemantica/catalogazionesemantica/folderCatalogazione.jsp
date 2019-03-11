<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<table width="100%" border="0" class="SchedaImg1">
	<tr>
		<sbn:checkAttivita idControllo="FOLDER_SOGGETTI">
			<c:choose>
				<c:when
					test="${CatalogazioneSemanticaForm.catalogazioneSemanticaComune.folder eq 'FOLDER_SOGGETTI'}">
					<td width="25%" class="schedaOn" align="center"><bean:message
						key="button.tag.soggetti" bundle="gestioneSemanticaLabels" /></td>
				</c:when>
				<c:otherwise>
					<td width="25%" class="schedaOff" align="center"><html:submit
						property="methodCatalogazione" styleClass="sintButtonLinkDefault">
						<bean:message key="button.tag.soggetti"
							bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</c:otherwise>
			</c:choose>
		</sbn:checkAttivita>
		<sbn:checkAttivita idControllo="FOLDER_CLASSI">
			<c:choose>
				<c:when
					test="${CatalogazioneSemanticaForm.catalogazioneSemanticaComune.folder eq 'FOLDER_CLASSI'}">
					<td width="25%" class="schedaOn" align="center"><bean:message
						key="button.tag.classificazioni" bundle="gestioneSemanticaLabels" /></td>
				</c:when>
				<c:otherwise>
					<td width="25%" class="schedaOff" align="center"><html:submit
						property="methodCatalogazione" styleClass="sintButtonLinkDefault">
						<bean:message key="button.tag.classificazioni"
							bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</c:otherwise>
			</c:choose>
		</sbn:checkAttivita>
		<sbn:checkAttivita idControllo="CHECK_FOLDER_INDICE">
			<sbn:checkAttivita idControllo="FOLDER_THESAURO">
				<c:choose>
					<c:when
						test="${CatalogazioneSemanticaForm.catalogazioneSemanticaComune.folder eq 'FOLDER_THESAURO'}">
						<td width="25%" class="schedaOn" align="center"><bean:message
							key="button.tag.thesauro" bundle="gestioneSemanticaLabels" /></td>
					</c:when>
					<c:otherwise>
						<td width="25%" class="schedaOff" align="center"><html:submit
							property="methodCatalogazione" styleClass="sintButtonLinkDefault">
							<bean:message key="button.tag.thesauro"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</sbn:checkAttivita>
			<sbn:checkAttivita idControllo="FOLDER_ABSTRACT">
				<c:choose>
					<c:when
						test="${CatalogazioneSemanticaForm.catalogazioneSemanticaComune.folder eq 'FOLDER_ABSTRACT'}">
						<td width="25%" class="schedaOn" align="center"><bean:message
							key="button.tag.abstracto" bundle="gestioneSemanticaLabels" /></td>
					</c:when>
					<c:otherwise>
						<td width="25%" class="schedaOff" align="center"><html:submit
							styleClass="sintButtonLinkDefault" property="methodCatalogazione">
							<bean:message key="button.tag.abstracto"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</sbn:checkAttivita>
		</sbn:checkAttivita>
	</tr>
</table>
