<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<layout:page>
	<sbn:navform action="/gestionestampe/cataloghi/stampaCataloghi.do"
		enctype="multipart/form-data">
		<div id="divMessaggio"><sbn:errors /></div>
		<div id="content">
		<table width="80%" border="0">

		<!-- almaviva2 01.02.2010 - asteriscato per il momento
			<tr>
				<td>
				<div align="left" class="etichetta"><bean:message key="ricerca.label.codiceBibl"
					bundle="gestioneStampeLabels" /></div>
				</td>
				<td><html:select property="codiceBibl" style="width:200px">
					<html:optionsCollection property="listaBiblio" value="codice"
						label="descrizioneCodice" /></html:select>
				</td>
				<td></td>
				<td></td>
			</tr>

			-->

			<tr>
				<td>
				<div align="left" class="etichetta"><bean:message key="cataloghi.label.datiDiStampa"
					bundle="gestioneStampeLabels" /></div>
				</td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td>
				<div align="left" class="etichetta"><bean:message key="cataloghi.label.intestazioneTitoloautorePrinc"
					bundle="gestioneStampeLabels" /></div>
				</td>
				<td width="30">
					<html:checkbox property="intestTitoloAdAutore"></html:checkbox>
					<html:hidden property="intestTitoloAdAutore" value="false" /></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td>
				<div align="left" class="etichetta"><bean:message key="cataloghi.label.stampaTitoloCollana"
					bundle="gestioneStampeLabels" /></div>
				</td>
				<td width="30">
					<html:checkbox property="titoloCollana"></html:checkbox>
					<html:hidden property="titoloCollana" value="false" />
				</td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td>
				<div align="left" class="etichetta"><bean:message key="cataloghi.label.stampaTitoliAnalitici"
					bundle="gestioneStampeLabels" /></div>
				</td>
				<td width="30">
					<html:checkbox property="titoliAnalitici"></html:checkbox>
					<html:hidden property="titoliAnalitici" value="false" />
				</td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td>
				<div align="left" class="etichetta"><bean:message key="cataloghi.label.stampaDatiCollocazione"
					bundle="gestioneStampeLabels" /></div>
				</td>
				<td width="30">
					<html:checkbox property="datiCollocazione"></html:checkbox>
					<html:hidden property="datiCollocazione" value="false" />
				</td>
				<td></td>
				<td></td>
			</tr>

		</table>


		<table width="80%" border="0">
			<tr>


			<td class="etichetta" width="10%"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /></td>
				<td><html:text disabled="true" styleId="testoNormale" property="codiceBibl" size="5"
					maxlength="3"></html:text> <html:submit disabled="false" title="Lista Biblioteche"
					styleClass="buttonImageListaSezione" property="methodStampaCataloghi">
					<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
				</html:submit><bean-struts:write name="stampaCataloghiForm" property="descrBibl" /></td>
			</tr>
		</table>





		<jsp:include flush="true"
			page="/WEB-INF/jsp/subpages/gestionestampe/cataloghi/stampaCataloghiParametri.jsp" />
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr height="30">
				<c:choose>
					<c:when test="${stampaCataloghiForm.folder eq 'RangeInv'}">
						<td width="20%" class="schedaOn">
						<div align="center">Intervallo di Inventari</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="20%" class="schedaOff">
						<div align="center"><html:submit property="methodStampaCataloghi"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerRangeInv" bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${stampaCataloghiForm.folder eq 'Collocazioni'}">
						<td width="20%" class="schedaOn">
						<div align="center">Collocazione</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="20%" class="schedaOff">
						<div align="center"><html:submit property="methodStampaCataloghi"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerCollocazione"
								bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${stampaCataloghiForm.folder eq 'Inventari'}">
						<td width="20%" class="schedaOn">
						<div align="center">Inventari</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="20%" class="schedaOff">
						<div align="center" styleId="etichetta"><html:submit property="methodStampaCataloghi"
							styleClass="sintButtonLinkDefault">
							<bean:message key="documentofisico.selPerInventari" bundle="documentoFisicoLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${stampaCataloghiForm.folder eq 'IdentificativiTitoli'}">
						<td width="20%" class="schedaOn">
						<div align="center">Catalogo per Titoli</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="20%" class="schedaOff">
						<div align="center" styleId="etichetta"><html:submit property="methodStampaCataloghi"
							styleClass="sintButtonLinkDefault">
							<bean:message key="cataloghi.label.titoli" bundle="gestioneStampeLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${stampaCataloghiForm.folder eq 'IdentificativiAutori'}">
						<td width="20%" class="schedaOn">
						<div align="center">Catalogo per Autori</div>
						</td>
					</c:when>
					<c:otherwise>
						<td width="20%" class="schedaOff">
						<div align="center" styleId="etichetta"><html:submit property="methodStampaCataloghi"
							styleClass="sintButtonLinkDefault">
							<bean:message key="cataloghi.label.autori" bundle="gestioneStampeLabels" />
						</html:submit></div>
						</td>
					</c:otherwise>
				</c:choose>

			</tr>
			<tr></tr>
		</table>
		<c:choose>
			<c:when test="${stampaCataloghiForm.folder eq 'RangeInv'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selRangeInv.jsp" />
			</c:when>
			<c:when test="${stampaCataloghiForm.folder eq 'Collocazioni'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selColloc.jsp" />
			</c:when>
			<c:when test="${stampaCataloghiForm.folder eq 'Inventari'}">
				<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selInv.jsp" />
			</c:when>
			<c:when test="${stampaCataloghiForm.folder eq 'IdentificativiTitoli'}">
				<jsp:include flush="true"
					page="/WEB-INF/jsp/subpages/gestionestampe/cataloghi/stampaCataloghiTitoli.jsp" />
			</c:when>
			<c:when test="${stampaCataloghiForm.folder eq 'IdentificativiAutori'}">
				<jsp:include flush="true"
					page="/WEB-INF/jsp/subpages/gestionestampe/cataloghi/stampaCataloghiAutori.jsp" />
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
					<c:when test="${stampaCataloghiForm.disable == false}">
						<td><html:submit property="methodStampaCataloghi">
							<bean:message key="documentofisico.bottone.conferma" bundle="documentoFisicoLabels" />
						</html:submit><html:submit property="methodStampaCataloghi">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<td><html:submit property="methodStampaCataloghi">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>