<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<%@page import="it.iccu.sbn.web.actionforms.amministrazionesistema.polo.ProfilazioneMaterialiForm;"%>
<html:xhtml />
<layout:page>
<div>
<sbn:navform action="/amministrazionesistema/abilitazionePolo/profilazionePoloMateriali.do">
	<div id="divMessaggio">
			<sbn:errors bundle="amministrazioneSistemaMessages" />
	</div>

		<table align="center" border="0" width="100%">
			<tr>
				<td align="left" style="font-weight: bold; font-size: 15px" width="100%">
					<bean:message key="profilo.polo.parametri.mat" bundle="amministrazioneSistemaLabels"/>:
				</td>
			</tr>
		</table>

			<br/>

			 <table class="sintetica">
			 	<tr>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center">
						<bean:message key="profilo.polo.parametri.nome"
							bundle="amministrazioneSistemaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center">
						<bean:message key="profilo.polo.parametri.abil"
							bundle="amministrazioneSistemaLabels" />
					</th>
					<!--th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center"-->
						<!--bean:message key="profilo.polo.parametri.contr_sim"
							bundle="amministrazioneSistemaLabels" /-->
					<!--/th-->
					<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center">
						<bean:message key="profilo.polo.parametri.abil_forzat"
							bundle="amministrazioneSistemaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center">
						<bean:message key="profilo.polo.parametri.livello"
							bundle="amministrazioneSistemaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align: center">
						<bean:message key="profilo.polo.parametri.sololocale"
							bundle="amministrazioneSistemaLabels" />
					</th>
			    </tr>
				<bean-struts:define id="color" value="#FEF1E2" />

				<logic:iterate id="item" property="elencoParMat" name="profilazionePoloMaterialiForm" indexId="riga">
		 			<c:if test="${item.acceso eq 'TRUE'}">
				 		<sbn:rowcolor var="color" index="riga" />
							<tr bgcolor="${color}">
								<td  style="text-align: left">
									<b><c:out value="${item.codice}"></c:out> - <c:out value="${item.descrizione}"></c:out></b>
								</td>
								<logic:iterate id="pmtr" property="elencoParametri" name="item">
										<c:choose>
											<c:when test="${pmtr.tipo eq 'MENU'}">
												<td  style="text-align: center">
													<html:select property='elencoParMat[${item.indice}].elencoParametri[${pmtr.index}].selezione'>
														<html:optionsCollection property="elencoParMat[${item.indice}].elencoParametri[${pmtr.index}].elencoScelte" value="codice" label="descrizione"/>
													</html:select>
												</td>
											</c:when>
											<c:when test="${pmtr.tipo eq 'RADIO'}">
												<td  style="text-align: center">
													<logic:iterate id="opzione" property="radioOptions" name="pmtr">
														<html:radio property="elencoParMat[${item.indice}].elencoParametri[${pmtr.index}].selezione" value="${opzione}"></html:radio>
														<c:out value="${opzione}"></c:out>
													</logic:iterate>
												</td>
											</c:when>
										</c:choose>
								</logic:iterate>
							</tr>
					</c:if>
				</logic:iterate>
			</table>


	<hr/>

		<div id="divFooter">
			<table align="center" border="0" style="height:40px">
				<tr>
					<td align="center">
						<html:submit styleClass="pulsanti" property="methodProfilazioneParametriMat">
							<bean:message key="profilo.polo.torna" bundle="amministrazioneSistemaLabels" />
						</html:submit>
						<html:submit styleClass="pulsanti" property="methodProfilazioneParametriMat">
							<bean:message key="profilo.polo.annulla" bundle="amministrazioneSistemaLabels" />
						</html:submit>
					</td>
				</tr>
			</table>
		</div>

    </sbn:navform>
  </layout:page>