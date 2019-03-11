<%@ taglib uri="http://struts.apache.org/tags-html-el"       prefix="html"       %>
<%@ taglib uri="http://struts.apache.org/tags-bean-el"       prefix="bean"       %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"            prefix="c"          %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"      %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"  prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn"           prefix="sbn"        %>

<%--
<div style="font-weight:bold;">
	<bean:message key="servizi.utenti.listaServizi" bundle="serviziLabels" />
</div>
<br/>
--%>

<div id="content" style="width:100%;">
	    <table class="sintetica">
	    	<tr class="etichetta" bgcolor="#dde8f0">
			    <th class="etichetta" scope="col" style="width:7%; text-align:center;">
			    	<bean:message	key="servizi.utenti.titCodice"
									bundle="serviziLabels" />
			    </th>
			    <th class="testoNormale" scope="col" style="text-align:center;">
			    	<bean:message	key="servizi.utenti.descrizioneServizio"
			    					bundle="serviziLabels" />
			    </th>
				<th style="width:3%; text-align:center;">
				<!--
					<bean:message	key="servizi.utenti.headerSelezionata"
									bundle="serviziLabels" />
				-->
				&nbsp;
				</th>
        	</tr>

        	<c:choose>
				<c:when test="${not empty navForm.lstServizi}">
		        	<logic:iterate	id       = "item"
		        					property = "lstServizi"
									name     = "navForm"
									indexId  = "riga">
						<sbn:rowcolor var="color" index="riga" />
						<tr>

							<!-- <td bgcolor="${color}" class="testoNormale" style="text-align:center;">
								<bean-struts:write	name="item" property="codServ" /></td> -->

							<td bgcolor="${color}" class="testoNormale" style="text-align:center;">
								<sbn:linkbutton index="riga"
								name="item" value="codServ" key="servizi.bottone.esamina"
								bundle="serviziLabels" title="esamina" property="selectedServizio" disabled="${navForm.conferma}" /></td>

							<td bgcolor="${color}" class="testoNormale">
								<bean-struts:write	name="item" property="descr" />
							</td>
							<td bgcolor="${color}" class="testoNoBold" title="Seleziona singolo servizio" style="text-align:center;">
								<html:radio property="selectedServizio" value="${riga}" />
							</td>
				        </tr>
					</logic:iterate>
				</c:when>
			</c:choose>
	    </table>
</div>
<br/>
