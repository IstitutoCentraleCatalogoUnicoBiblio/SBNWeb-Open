<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
<div>
		<div id="divForm">
<sbn:navform action="/amministrazionesistema/abilitazioneBibliotecario/profilazioneBibliotecario.do">
			<div id="divMessaggio">
					<sbn:errors bundle="amministrazioneSistemaMessages" />
			</div>

<table border="0" width="100%">
	<tr>
		<td align="left" width="75%" style="font-weight: bold; font-size: 15px">
			<bean:message key="profilo.bibliotecario.titolo" bundle="amministrazioneSistemaLabels"/>:
		</td>
	</tr>
	<tr>
		<td>
			<b><bean:message key="profilo.bibliotecario.parametri.nome" bundle="amministrazioneSistemaLabels"/>: </b>
			<c:out value="${profilazioneBibliotecarioForm.utente}"></c:out>
			<b><bean:message key="profilo.bibliotecario.parametri.biblioteca" bundle="amministrazioneSistemaLabels"/>: </b>
			<c:out value="${profilazioneBibliotecarioForm.bibli}"></c:out>
			<b><bean:message key="profilo.bibliotecario.parametri.user" bundle="amministrazioneSistemaLabels"/>: </b>
			<c:out value="${profilazioneBibliotecarioForm.usernam}"></c:out>
		</td>
	</tr>
</table>

<br/>

<c:choose>
	<c:when test="${profilazioneBibliotecarioForm.modelloOp eq 'SALVA'}">
		<sbn:tree root="profilazioneTreeElementView"
			divClass="analitica"
			propertyCheck="checkItemSelez"
			propertyRadio="radioItemSelez"
			imagesPath="/sbn/images/tree"
			enableNodeSubmit="true"
			enabled="false"
			enableSelectAll="false">
			</sbn:tree>

		<div id="divFooter">
			<table border="0" style="height:40px" align="center">
				<tr>
					<td width="100%" align="center">
						<c:choose>
							<c:when test="${profilazioneBibliotecarioForm.nuovoModello eq 'TRUE'}">
								<b><bean:message key="profilo.bibliotecario.modello.salva.nuovo" bundle="amministrazioneSistemaLabels"/></b>:
								<html:text property="modello" size="40" maxlength="25"></html:text>
							</c:when>
							<c:otherwise>
								<b><bean:message key="profilo.bibliotecario.modello.salva" bundle="amministrazioneSistemaLabels"/></b>:
								<html:select property="modello" style="width:350px">
									<html:optionsCollection property="elencoModelli" value="codice" label="descrizione"/>
								</html:select>
							</c:otherwise>
						</c:choose>
						<html:submit styleClass="pulsanti" property="methodProfilazione">
							<bean:message key="profilo.bibliotecario.button.ok" bundle="amministrazioneSistemaLabels" />
						</html:submit>
						<html:submit styleClass="pulsanti" property="methodProfilazione">
							<bean:message key="profilo.bibliotecario.button.modello.annulla" bundle="amministrazioneSistemaLabels" />
						</html:submit>
					</td>
				</tr>
			</table>
		</div>
	</c:when>
	<c:when test="${profilazioneBibliotecarioForm.modelloOp eq 'CARICA'}">
		<sbn:tree root="profilazioneTreeElementView"
			divClass="analitica"
			propertyCheck="checkItemSelez"
			propertyRadio="radioItemSelez"
			imagesPath="/sbn/images/tree"
			enableNodeSubmit="true"
			enabled="false"
			enableSelectAll="false">
			</sbn:tree>

		<div id="divFooter">
			<table border="0" style="height:40px" align="center">
				<tr>
					<td width="100%" align="center">
						<b><bean:message key="profilo.bibliotecario.modello.carica" bundle="amministrazioneSistemaLabels"/></b>:
						<html:select property="modello" style="width:350px">
							<html:optionsCollection property="elencoModelli" value="codice" label="descrizione"/>
						</html:select>
						<html:submit styleClass="pulsanti" property="methodProfilazione">
							<bean:message key="profilo.bibliotecario.button.ok" bundle="amministrazioneSistemaLabels" />
						</html:submit>
						<html:submit styleClass="pulsanti" property="methodProfilazione">
							<bean:message key="profilo.bibliotecario.button.modello.annulla" bundle="amministrazioneSistemaLabels" />
						</html:submit>
					</td>
				</tr>
			</table>
		</div>
	</c:when>
	<c:otherwise>
		<sbn:tree root="profilazioneTreeElementView"
			divClass="analitica"
			propertyCheck="checkItemSelez"
			propertyRadio="radioItemSelez"
			imagesPath="/sbn/images/tree"
			enableNodeSubmit="true"
			enableSelectAll="true">
			</sbn:tree>

		<div id="divFooter">
			<table border="0" style="height:40px" align="center">
				<tr>
					<td width="100%" align="center">
						<c:choose>
							<c:when test="${profilazioneBibliotecarioForm.abilitatoWrite eq 'TRUE'}">
								<html:submit styleClass="pulsanti" property="methodProfilazione">
									<bean:message key="profilo.polo.salva" bundle="amministrazioneSistemaLabels" />
								</html:submit>
							</c:when>
						</c:choose>

						<html:submit styleClass="pulsanti" property="methodProfilazione">
							<bean:message key="profilo.polo.button.auth" bundle="amministrazioneSistemaLabels" />
						</html:submit>

						<html:submit styleClass="pulsanti" property="methodProfilazione">
							<bean:message key="profilo.polo.button.mat" bundle="amministrazioneSistemaLabels" />
						</html:submit>

						<html:submit styleClass="pulsanti" property="methodProfilazione">
							<bean:message key="profilo.polo.annulla" bundle="amministrazioneSistemaLabels" />
						</html:submit>

					<!--/td-->
					<!--td width="50"/-->
					<!--td align="right"-->
						<c:choose>
							<c:when test="${profilazioneBibliotecarioForm.abilitatoWrite eq 'TRUE'}">
								<html:submit styleClass="buttonSelezTutti" property="methodProfilazione" title="Seleziona tutto">
									<bean:message key="profilo.polo.selezionaTutti" bundle="amministrazioneSistemaLabels" />
								</html:submit>

								<html:submit styleClass="buttonSelezNessuno" property="methodProfilazione" title="Deseleziona tutto">
									<bean:message key="profilo.polo.deSelezionaTutti" bundle="amministrazioneSistemaLabels" />
								</html:submit>
							</c:when>
						</c:choose>
					</td>
				</tr>
				<c:choose>
					<c:when test="${profilazioneBibliotecarioForm.abilitatoWrite eq 'TRUE'}">
						<tr>
							<td width="100%" align="center">
								<html:submit styleClass="pulsanti" property="methodProfilazione">
									<bean:message key="profilo.bibliotecario.button.modello.carica" bundle="amministrazioneSistemaLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti" property="methodProfilazione">
									<bean:message key="profilo.bibliotecario.button.modello.salva" bundle="amministrazioneSistemaLabels" />
								</html:submit>
							</td>
						</tr>
					</c:when>
				</c:choose>
			</table>
		</div>
	</c:otherwise>
</c:choose>

    </sbn:navform>
  </layout:page>