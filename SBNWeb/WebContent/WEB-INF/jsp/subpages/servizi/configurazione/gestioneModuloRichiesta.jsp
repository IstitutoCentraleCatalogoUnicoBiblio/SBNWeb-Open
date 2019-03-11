<%@ taglib uri="http://struts.apache.org/tags-html-el"       prefix="html"       %>
<%@ taglib uri="http://struts.apache.org/tags-bean-el"       prefix="bean"       %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"            prefix="c"          %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"      %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"  prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn"           prefix="sbn"        %>

<div id="content" style="width:100%;">
	<sbn:disableAll checkAttivita="GESTIONE">
	<table  class="sintetica">
	    	<tr class="etichetta" bgcolor="#dde8f0">
			    <th class="etichetta" scope="col" style="width:40%; text-align:center;">
			    	<bean:message	key="servizi.utenti.headerEtichetta"
									bundle="serviziLabels" />
			    </th>
			    <th class="etichetta" scope="col" style="width:10%; text-align:center;">
			    	<bean:message	key="servizi.utenti.headerVisibile"
			    					bundle="serviziLabels" />
			    </th>
			    <th class="etichetta" scope="col" style="width:10%; text-align:center;">
			    	<bean:message	key="servizi.utenti.headerObbligatorio"
			    					bundle="serviziLabels" />
			    </th>
			    <th class="etichetta" scope="col" style="width:40%; text-align:center;">
			    	<bean:message	key="servizi.utenti.headerNotaEsplicativa"
			    					bundle="serviziLabels" />
			    </th>
        	</tr>

        	<logic:iterate	id       = "item"
        					property = "lstServizioWebDatiRichiesti"
							name     = "ConfigurazioneTipoServizioForm"
							indexId  = "riga">
				<sbn:rowcolor var="color" index="riga" />
				<tr>
					<td bgcolor="${color}" class="testoNormale">
						<bean-struts:write name="item" property="descrizione" />
					</td>

					<c:choose>
    					<c:when test="${item.obbligatorioTabCodici}">
							<td bgcolor="${color}" class="testoNormale" style="text-align:center;">
								<html:checkbox name="item" property="utilizzato" disabled="true" indexed="true"></html:checkbox>
							</td>
							<td bgcolor="${color}" class="testoNormale" style="text-align:center;">
								<html:checkbox name="item" property="obbligatorio" disabled="true" indexed="true"></html:checkbox>
							</td>
						</c:when>
						<c:otherwise>
							<td bgcolor="${color}" class="testoNormale" style="text-align:center;">
								<html:hidden property="visibileModuloRichiesta" />
								<html:checkbox name="item" property="utilizzato" indexed="true" onclick="validateSubmit('visibileModuloRichiesta', 'SI');" ></html:checkbox>
								<html:hidden name="item" property="utilizzato" value="false" indexed="true"></html:hidden>
							</td>
							<c:choose>
    							<c:when test="${item.utilizzato}">
									<td bgcolor="${color}" class="testoNormale" style="text-align:center;">
										<html:checkbox name="item" property="obbligatorio" indexed="true"></html:checkbox>
										<html:hidden name="item" property="obbligatorio" value="false" indexed="true"></html:hidden>
									</td>
								</c:when>
								<c:otherwise>
									<td bgcolor="${color}" class="testoNormale" style="text-align:center;">
										<%--<html:checkbox name="item" property="obbligatorio" disabled="true" indexed="true"></html:checkbox>--%>
										<html:hidden name="item" property="obbligatorio" value="false" indexed="true"></html:hidden>
									</td>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>

					<td bgcolor="${color}" class="testoNormale">
						<bean-struts:write	name="item" property="note" />
					</td>
		        </tr>
			</logic:iterate>

	    </table>
	    </sbn:disableAll>

</div>

