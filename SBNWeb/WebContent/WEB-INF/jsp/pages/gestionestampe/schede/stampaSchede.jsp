<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<layout:page>
	<sbn:navform action="/gestionestampe/schede/stampaSchede.do"
		enctype="multipart/form-data">
		<div id="divMessaggio"><sbn:errors /></div>
		<div id="content">



		<table width="80%" border="0">
			<tr>


			<td class="etichetta" width="10%"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /></td>
				<td><html:text disabled="true" styleId="testoNormale" property="codiceBibl" size="5"
					maxlength="3"></html:text> <html:submit disabled="false" title="Lista Biblioteche"
					styleClass="buttonImageListaSezione" property="methodStampaSchede">
					<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
				</html:submit><bean-struts:write name="stampaSchedeForm" property="descrBibl" /></td>


				<td></td>
				<td><label for="stampaPiuInventari"><bean:message
					key="schede.label.stampaPiuInventari" bundle="gestioneStampeLabels" /> </label> <html:checkbox
					styleId="chkStampaPiuInventari" property="chkStampaPiuInventari" /></td>
			</tr>
			<tr>
				<td>
				<div class="etichetta"><bean:message key="schede.label.status"
					bundle="gestioneStampeLabels" /></div>
				</td>
				<td><html:select styleClass="testoNormale" property="status">
					<html:optionsCollection property="listaStatus" value="codice" label="descrizioneCodice" />
				</html:select></td>
				<td>
				<div class="etichetta"><bean:message key="cataloghi.label.natura"
					bundle="gestioneStampeLabels" /></div>
				</td>
				<td><html:select styleClass="testoNormale" property="natura">
					<html:optionsCollection property="listaNatura" value="codice" label="descrizioneCodice" />
				</html:select></td>
			</tr>
		</table>
		<jsp:include flush="true"
			page="/WEB-INF/jsp/subpages/gestionestampe/schede/stampaSchedeParametri.jsp" />
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr height="30">
				<c:choose>
					<c:when test="${stampaSchedeForm.folder eq 'RangeInv'}">
						<td width="25%" class="schedaOn">
						<div align="center">Intervallo di Inventari</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="25%" class="schedaOff">
						<div align="center"><html:submit property="methodStampaSchede"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerRangeInv" bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${stampaSchedeForm.folder eq 'Collocazioni'}">
						<td width="25%" class="schedaOn">
						<div align="center">Collocazione</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="25%" class="schedaOff">
						<div align="center"><html:submit property="methodStampaSchede"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerCollocazione"
								bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${stampaSchedeForm.folder eq 'Inventari'}">
						<td width="25%" class="schedaOn">
						<div align="center">Inventari</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="25%" class="schedaOff">
						<div align="center" styleId="etichetta"><html:submit
							property="methodStampaSchede" styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerInventari" bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${stampaSchedeForm.folder eq 'IdentificativiTitoli'}">
						<td width="25%" class="schedaOn">
						<div align="center">Identificativi Titoli</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="25%" class="schedaOff">
						<div align="center" styleId="etichetta"><html:submit
							property="methodStampaSchede" styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerIdentificativiTitoli"
								bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr></tr>
		</table>
		<c:choose>
			<c:when test="${stampaSchedeForm.folder eq 'RangeInv'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selRangeInv.jsp" />
			</c:when>
			<c:when test="${stampaSchedeForm.folder eq 'Collocazioni'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selColloc.jsp" />
			</c:when>
			<c:when test="${stampaSchedeForm.folder eq 'Inventari'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selInv.jsp" />
			</c:when>
			<c:when test="${stampaSchedeForm.folder eq 'IdentificativiTitoli'}">
				<jsp:include flush="true"
					page="/WEB-INF/jsp/subpages/gestionestampe/schede/stampaSchedeTitoli.jsp" />
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose></div>
		<HR>
		<jsp:include flush="true" page="../common/tipoStampa.jsp" />
		<HR>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center" border="0" style="height: 40px">
			<tr>
				<c:choose>
					<c:when test="${stampaSchedeForm.disable == false}">
						<td><html:submit property="methodStampaSchede">
							<bean:message key="documentofisico.bottone.conferma" bundle="documentoFisicoLabels" />
						</html:submit><html:submit property="methodStampaSchede">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<td><html:submit property="methodStampaSchede">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>