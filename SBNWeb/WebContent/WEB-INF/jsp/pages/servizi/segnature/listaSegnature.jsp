<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/segnature/ListaSegnature">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />

			</div>
			<br>

			<c:if test="${not empty ListaSegnatureForm.listaSegnature}">

				<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"
							elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
							parameter="methodListaSegna">
				</sbn:blocchi>
				<table class="sintetica">
					<tr>
						<th class="etichetta" style="text-align: center; font-size: 90%;" scope="col" bgcolor="#dde8f0" width="4%">
							<bean:message key="servizi.autorizzazioni.header.bib" bundle="serviziLabels" />
						</th>
						<th class="etichetta" style="text-align: center; font-size: 90%;" scope="col" bgcolor="#dde8f0" width="5%">
							<bean:message key="servizi.segnature.header.codSegn" bundle="serviziLabels" />
						</th>
						<th class="etichetta" style="text-align: center; font-size: 90%;" scope="col" bgcolor="#dde8f0" width="10%">
							<bean:message key="servizi.segnature.header.iniSegn" bundle="serviziLabels" />
						</th>
						<th class="etichetta" style="text-align: center; font-size: 90%;" scope="col" bgcolor="#dde8f0" width="10%">
							<bean:message key="servizi.segnature.header.finSegn" bundle="serviziLabels" />
						</th>
						<th class="etichetta" style="text-align: center; font-size: 90%;" scope="col" bgcolor="#dde8f0" width="5%">
							<bean:message key="servizi.segnature.header.fruizSegn" bundle="serviziLabels" />
						</th>
						<th class="etichetta" style="text-align: center; font-size: 90%;" scope="col" bgcolor="#dde8f0" width="5%">
							<bean:message key="servizi.segnature.header.nonDispSegn" bundle="serviziLabels" />
						</th>
						<th class="etichetta" style="text-align: center; font-size: 90%;" scope="col" bgcolor="#dde8f0" width="26%">
							<bean:message key="servizi.segnature.header.daSegn" bundle="serviziLabels" />
						</th>
						<th class="etichetta" style="text-align: center; font-size: 90%;" scope="col" bgcolor="#dde8f0" width="26%">
							<bean:message key="servizi.segnature.header.aSegn" bundle="serviziLabels" />
						</th>
						<!--
						<th class="etichetta" style="text-align: center; font-size: 90%;" scope="col" bgcolor="#dde8f0" width="3%">
							<bean:message key="servizi.utenti.headerSelezionata" bundle="serviziLabels" />
						</th>
						-->
						<th class="etichetta" style="text-align: center; font-size: 90%;" scope="col" bgcolor="#dde8f0" width="3%">
							<bean:message key="servizi.utenti.headerSelezionataMultipla" bundle="serviziLabels" />
						</th>
					</tr>
					<logic:iterate id="item" property="listaSegnature" name="ListaSegnatureForm" indexId="progr">
						<sbn:rowcolor var="color" index="progr" />

						<bean-struts:define id="idSegnatura">
							<bean-struts:write name="item" property="id" />
						</bean-struts:define>

						<tr bgcolor="${color}">
							<td class="testoNoBold" style="font-size: 90%;">
								<sbn:anchor name="item" property="progr"/>
								<bean-struts:write name="item" property="codBiblioteca" />
							</td>
							<td class="testoNoBold" style="font-size: 90%;">
								<bean-struts:write name="item" property="id"   />
							</td>
							<td class="testoNoBold" style="font-size: 90%;" >
								<!--bean-struts:write name="item" property="segnInizio" /-->
								<html:text size="10" styleClass="wide_input" name="item" property="segnInizioTrim" readonly="true"></html:text>
							</td>
							<td class="testoNoBold" style="font-size: 90%;" >
								<html:text size="10" styleClass="wide_input" name="item" property="segnFineTrim" readonly="true"></html:text>
							</td>
							<td class="testoNoBold" style="font-size: 90%;">
								<bean-struts:write name="item" property="fruizione" />
							</td>
							<td class="testoNoBold" style="font-size: 90%;">
								<bean-struts:write name="item" property="indisponibile" />
							</td>
							<td class="testoNoBold" style="font-size: 90%;" >
								<html:text size="25" styleClass="wide_input" name="item" property="segnDaTrim" readonly="true"></html:text>
							</td>
							<td class="testoNoBold" style="font-size: 90%;" >
								<html:text size="25" styleClass="wide_input" name="item" property="segnATrim" readonly="true"></html:text>
							</td>
							<!--
							<td class="testoNoBold" >
								<html:radio property="codSelSegnSing" value="${progr}" disabled="${ListaSegnatureForm.conferma}" />
							</td>
							-->
							<td class="testoNoBold" >
								<html:multibox property="codSelSegn" value="${idSegnatura}" disabled="${ListaSegnatureForm.conferma}" />
							</td>
						</tr>
					</logic:iterate>
				</table>
			</div>

			<div id="divFooterCommon">
				<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"
								elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
								parameter="methodListaSegna" bottom="true" >
				</sbn:blocchi>
			</div>
			<br/>
			<br/>
		</c:if>

		<div id="divFooter">
			<c:choose>
				<c:when test="${ListaSegnatureForm.conferma}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
				</c:when>
				<c:otherwise>
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/segnature/footerListaSegnature.jsp" />
				</c:otherwise>
			</c:choose>
		</div>
	</sbn:navform>
</layout:page>
