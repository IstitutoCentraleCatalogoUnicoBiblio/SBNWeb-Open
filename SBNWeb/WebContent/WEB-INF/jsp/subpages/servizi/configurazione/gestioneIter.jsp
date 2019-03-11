<%@ taglib uri="http://struts.apache.org/tags-html-el"       prefix="html"       %>
<%@ taglib uri="http://struts.apache.org/tags-bean-el"       prefix="bean"       %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"            prefix="c"          %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"      %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"  prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn"           prefix="sbn"        %>

<div id="content" style="width:100%;">

    <c:choose>

       	<c:when test="${ConfigurazioneTipoServizioForm.stringaMessaggioNoIter ne ''}">
			<div class="msgWarning1"><bean-struts:write name="ConfigurazioneTipoServizioForm" property="stringaMessaggioNoIter" />
			</div>

			<c:choose>
       			<c:when test="${ConfigurazioneTipoServizioForm.stringaMessaggioNoIterSeAncoraPresente ne ''}">
       				<br/>
					<div class="msgKO1"><bean-struts:write name="ConfigurazioneTipoServizioForm" property="stringaMessaggioNoIterSeAncoraPresente" />
					</div>
       			</c:when>
			</c:choose>
		</c:when>

		<c:otherwise>

			<table  class="sintetica">
			    	<tr class="etichetta" bgcolor="#dde8f0">
					    <th class="etichetta" scope="col" style="width:7%; text-align:center;">
					    	<bean:message	key="servizi.utenti.headerProgressivo"
											bundle="serviziLabels" />
					    </th>
					    <th class="etichetta" scope="col" style="text-align:center;">
					    	<bean:message	key="servizi.erogazione.attivita"
					    					bundle="serviziLabels" />
					    </th>
						<th style="width:5%; text-align:center;">
						&nbsp;
						</th>
						<!--
						<th class="etichetta" scope="col"  style="width:4%; text-align:center;">
							<bean:message	key="servizi.utenti.headerSelezionataMultipla" bundle="serviziLabels" />
						</th>
						-->
		        	</tr>

		        	<c:choose>
						<c:when test="${not empty ConfigurazioneTipoServizioForm.lstIter}">
				        	<logic:iterate	id       = "item"
				        					property = "lstIter"
											name     = "ConfigurazioneTipoServizioForm"
											indexId  = "riga">
								<sbn:rowcolor var="color" index="riga" />
								<tr>
									<td bgcolor="${color}" class="testoNormale" style="text-align:center;">
										<sbn:linkbutton index="progrIter"
										name="item" value="progrIter" key="servizi.bottone.esamina"
										bundle="serviziLabels" title="esamina" property="progrIter" disabled="${navForm.conferma}"/>
									</td>
									<td bgcolor="${color}" class="testoNormale">
										<bean-struts:write	name="item" property="descrizione" />
									</td>
									<td bgcolor="${color}" class="testoNoBold" align="center">
										<html:radio property="progrIter" value="${item.progrIter}" titleKey="servizi.configurazione.controlloIter.selezioneSingola" bundle="serviziLabels"/>
									</td>
									<!--
									<td bgcolor="${color}" align="center">
										<html:multibox property="progressiviIterSelezionati" titleKey="servizi.configurazione.controlloIter.selezioneMultipla" bundle="serviziLabels" value="${item.progrIter}"></html:multibox>
									</td>
									-->
						        </tr>
							</logic:iterate>

						</c:when>

					</c:choose>
			    </table>

				<c:if test="${ConfigurazioneTipoServizioForm.stringaMessaggioIterOK ne ''}">
					<br/>
					<div class="msgOK1"><bean-struts:write name="ConfigurazioneTipoServizioForm" property="stringaMessaggioIterOK" />
					</div>
				</c:if>

				<c:if test="${ConfigurazioneTipoServizioForm.stringaMessaggioIterKO ne ''}">
					<br/>
					<div class="msgKO1"><bean-struts:write name="ConfigurazioneTipoServizioForm" property="stringaMessaggioIterKO" />
					</div>
				</c:if>

		</c:otherwise>

    </c:choose>

</div>

