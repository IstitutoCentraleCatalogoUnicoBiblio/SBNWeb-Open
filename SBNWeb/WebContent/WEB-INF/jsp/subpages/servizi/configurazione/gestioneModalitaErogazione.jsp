<%@ taglib uri="http://struts.apache.org/tags-html-el"       prefix="html"       %>
<%@ taglib uri="http://struts.apache.org/tags-bean-el"       prefix="bean"       %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"            prefix="c"          %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"      %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"  prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn"           prefix="sbn"        %>

<div id="content" style="width:100%;">
	    <table class="sintetica">
	    	<tr class="etichetta" bgcolor="#dde8f0">
			    <th class="testoNormale" scope="col" style="width:7%; text-align:center;">
			    	<bean:message	key="servizi.utenti.titCodice"
									bundle="serviziLabels" />
			    </th>
			    <th class="testoNormale" scope="col" style="text-align:center;">
			    	<bean:message	key="servizi.label.descrizione"
			    					bundle="serviziLabels" />
			    </th>
			     <th class="testoNormale" scope="col" style="width:9%; text-align:center;">
	    			<bean:message	key="servizi.erogazione.contesto"
	    							bundle="serviziLabels" />
	    		</th>
			    <th class="testoNormale" scope="col" style="width:9%; text-align:center;">
			    	<bean:message	key="servizi.label.tariffaBase"
			    					bundle="serviziLabels" />
			    </th>
			    <th class="testoNormale" scope="col" style="width:9%; text-align:center;">
			    	<bean:message	key="servizi.label.costoUnitario"
			    					bundle="serviziLabels" />
			    </th>
				<th style="width:3%; text-align:center;">&nbsp;</th>
        	</tr>

        	<c:choose>
				<c:when test="${not empty ConfigurazioneForm.lstTariffeModalitaErogazione}">
		        	<logic:iterate	id       = "item"
		        					property = "lstTariffeModalitaErogazione"
									name     = "ConfigurazioneForm"
									indexId  = "riga">
						<sbn:rowcolor var="color" index="riga" />
						<tr>
							<!-- <td bgcolor="${color}" class="testoNormale" style="text-align:center;">
								<bean-struts:write	name="item" property="codErog" /></td> -->

							<td bgcolor="${color}" class="testoNormale" style="text-align:center;">
								<sbn:linkbutton index="riga"
								name="item" value="codErog" key="servizi.bottone.esamina"
								bundle="serviziLabels" title="esamina" property="selectedModalitaErogazione" /></td>

							<td bgcolor="${color}" class="testoNormale">
								<bean-struts:write	name="item" property="desModErog" />
							</td>
							<td bgcolor="${color}" class="testoNormale" style="text-align:center;">
								<c:if test="${item.locale}"><bean:message key="servizi.erogazione.contesto.locale" bundle="serviziLabels" /></c:if>
								<c:if test="${not item.locale}"><bean:message key="servizi.erogazione.contesto.ill" bundle="serviziLabels" /></c:if>
							</td>
							<td bgcolor="${color}" class="testoNormale" style="text-align:center;">
								<bean-struts:write	name="item" property="tarBase" />
							</td>
							<td bgcolor="${color}" class="testoNormale" style="text-align:center;">
								<bean-struts:write	name="item" property="costoUnitario" />
							</td>
							<td bgcolor="${color}" class="testoNoBold" style="text-align:center;">
								<html:radio property="selectedModalitaErogazione" value="${riga}" titleKey="servizi.configurazione.selezioneSingolaModalita" bundle="serviziLabels" />
							</td>
				        </tr>
					</logic:iterate>
				</c:when>
			</c:choose>
	    </table>
</div>
<br/>

