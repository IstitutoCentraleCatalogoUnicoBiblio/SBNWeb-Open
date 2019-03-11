<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>
<!--
<html:xhtml />
<layout:page>
	<sbn:errors bundle="gestioneBibliograficaMessages" />

	<sbn:navform action="/gestionebibliografica/titolo/catalogazioneTitolo.do">

		<div id="content">

		   <table width="100%"  border="0">
              <tr>
                <td width="20%" class="etichetta" >
               		<bean:message key="catalogazione.natura" bundle="gestioneBibliograficaLabels" />:
                </td>
                <td width="80%" class="testoNormale" >
					<html:select property="naturaSelez" style="width:40px">
					<html:optionsCollection property="listaNature" value="codice"
						label="descrizioneCodice" />
					</html:select>
                </td>
			  </tr>
			  <tr>
                <td width="20%" class="etichetta">
                <bean:message key="catalogazione.tipMateriale" bundle="gestioneBibliograficaLabels" />
				</td>
			  </tr>
            </table>

            <table width="606"  border="0" class="SchedaImg1" >
              <tr >
                <td  width="86" class="schedaOn"  align="center">
					<html:submit property="methodCatalogTit">
						<bean:message key="ricerca.tabModerno" bundle="gestioneBibliograficaLabels" />
					</html:submit>
                </td>
                <td  width="86" class="schedaOff" align="center">
					<html:submit property="methodCatalogTit">
						<bean:message key="ricerca.tabAntico" bundle="gestioneBibliograficaLabels" />
					</html:submit>
               	</td>
                <td  width="86" class="schedaOff" align="center">
					<html:submit property="methodCatalogTit">
						<bean:message key="ricerca.tabCartografia" bundle="gestioneBibliograficaLabels" />
					</html:submit>
                </td>
                <td  width="86" class="schedaOff" align="center">
					<html:submit property="methodCatalogTit">
						<bean:message key="ricerca.tabGrafica" bundle="gestioneBibliograficaLabels" />
					</html:submit>
                </td>
                <td  width="86" class="schedaOff" align="center">
					<html:submit property="methodCatalogTit">
						<bean:message key="ricerca.tabMusica" bundle="gestioneBibliograficaLabels" />
					</html:submit>
                </td>
                <td  width="86" class="schedaOff" align="center">
					<html:submit property="methodCatalogTit">
						<bean:message key="ricerca.tabNessuno" bundle="gestioneBibliograficaLabels" />
					</html:submit>
                </td>
                <td  class="schedaOff" align="center"></td>
              </tr>
            </table>
			<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/catalTitParteGenerale.jsp" />


			<c:choose>
			  <c:when test="${catalogazioneTitoloForm.tipoMatSelez eq 'C'}">
					<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/catalTitParteCartografia.jsp" />
			  </c:when>
			  <c:when test="${catalogazioneTitoloForm.tipoMatSelez eq 'G'}">
					<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/catalTitParteGrafica.jsp" />
			  </c:when>
			  <c:when test="${catalogazioneTitoloForm.tipoMatSelez eq 'U'}">
					<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/catalTitParteMusica.jsp" />
			  </c:when>
			  <c:otherwise>
			  </c:otherwise>
			</c:choose>

			<table width="100%"  border="0" >
              <tr>
	            <td width="31%" class="etichetta"><bean:message key="ricerca.dataInserimento" bundle="gestioneBibliograficaLabels" />: </td>
                <td width="16%">
                	<html:text styleId="testoNormale" property="catalGener.dataInserimento" size="20"></html:text>
                </td>
	            <td width="18%" class="etichetta"><bean:message key="ricerca.dataAgg" bundle="gestioneBibliograficaLabels" />: </td>
                <td width="35%">
                	<html:text styleId="testoNormale" property="catalGener.dataAggiornamento" size="20"></html:text>
                </td>
              </tr>
           </table>
		</div>

		<div id="footer">

			<table align="center">
		        <tr>
					<td  align="center">
						<html:submit property="methodCatalogTit">
						<bean:message key="catalogazione.button.ok" bundle="gestioneBibliograficaLabels" />
						</html:submit>
					</td>
					<td  align="center">
						<html:submit property="methodCatalogTit">
						<bean:message key="catalogazione.button.annulla" bundle="gestioneBibliograficaLabels" />
						</html:submit>
					</td>
		        </tr>
		    </table>

		</div>

	</sbn:navform>
</layout:page>
-->
