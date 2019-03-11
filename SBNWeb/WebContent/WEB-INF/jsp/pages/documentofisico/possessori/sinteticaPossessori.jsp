<!--	SBNWeb - Rifacimento ClientServer
		JSP di sintetica autori
		Alessandro Segnalini - Inizio Codifica aprile 2008
-->
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/documentofisico/possessori/sinteticaPossessori.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>


		<c:choose>
			<c:when test="${possessoriSinteticaForm.prospettaDatiOggColl eq 'SI'}">
				<table border="0">
					<tr>
						<td class="etichetta">
							<bean:message key="ricerca.titoloRiferimento"
								bundle="gestioneBibliograficaLabels" />
							:
						</td>
						<td width="20" class="testoNormale">
							<html:text property="idOggColl" size="10" readonly="true"></html:text>
						</td>
						<td width="150" class="etichetta">
							<html:text property="descOggColl" size="50" readonly="true"></html:text>
						</td>
					</tr>
				</table>
				<hr color="#dde8f0" />
			</c:when>
		</c:choose>



		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe" totBlocchi="totBlocchi"
			elementiPerBlocco="elemPerBlocchi" parameter="methodPossSintetica"></sbn:blocchi>
		<table class="sintetica">
			<tr bgcolor="#dde8f0">
				<th class="etichetta"><bean:message key="documentofisico.sintetica.progressivo"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta"><bean:message key="documentofisico.sintetica.PID"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta"><bean:message key="documentofisico.sintetica.nome"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta"><bean:message key="documentofisico.sintetica.forma"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta"><bean:message key="documentofisico.sintetica.tipoNome"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta"><bean:message key="documentofisico.sintetica.livello"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta"></th>
				<th class="etichetta"></th>
			</tr>
			<logic:iterate id="item" property="listaPossessori" name="possessoriSinteticaForm"
				indexId="riga">
				<sbn:rowcolor var="color" index="riga" />
				<tr bgcolor="${color}">
					<td bgcolor="${color}"><sbn:anchor name="item" property="prg" />
					<sbn:linkbutton index="pid" name="item" value="prg"
						key="sintetica.button.analitica" bundle="documentoFisicoLabels" title="analitica"
						property="selezRadio"
						withAnchor="false" /></td>
					<td bgcolor="${color}" ><sbn:linkbutton index="pid" name="item"
						value="pid" key="sintetica.button.analitica" bundle="documentoFisicoLabels"
						title="analitica" property="selezRadio"
						withAnchor="false" /></td>
					<c:choose>
						<c:when test="${item.nomeFormaAccettata ne null}">
							<td bgcolor="${color}" ><bean-struts:write name="item"
								property="nome" /> <BR>&nbsp;&nbsp;&nbsp;-->&nbsp;&nbsp;&nbsp;<bean-struts:write name="item"
								property="nomeFormaAccettata" /></BR></td>
						</c:when>
						<c:otherwise>
							<td bgcolor="${color}" ><bean-struts:write name="item"
								property="nome" /></td>
						</c:otherwise>
					</c:choose>
					<td bgcolor="${color}" ><bean-struts:write name="item"
						property="forma" /></td>
					<td bgcolor="${color}" ><bean-struts:write name="item"
						property="tipoNome" /></td>
					<td bgcolor="${color}" ><bean-struts:write name="item"
						property="livello" /></td>
					<td bgcolor="${color}"><html:radio property="selezRadio" value="${item.pid}" /></td>
					<td bgcolor="${color}"><html:multibox property="selezCheck" value="${item.pid}" /></td>
				</tr>
			</logic:iterate>
		</table>
		</div>
		<div id="divFooterCommon"><sbn:blocchi numBlocco="bloccoSelezionato"
			numNotizie="totRighe" totBlocchi="totBlocchi" elementiPerBlocco="elemPerBlocchi"
			parameter="methodPossSintetica" bottom="true"></sbn:blocchi></div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<c:choose>
					<c:when
						test="${possessoriSinteticaForm.prov eq 'normale' or possessoriSinteticaForm.prov eq 'attivaLegame'}">
						<td align="center"><sbn:checkAttivita idControllo="possessori">
							<html:submit property="methodPossSintetica">
								<bean:message key="sintetica.button.analiticaPossessori"
									bundle="documentoFisicoLabels" />
							</html:submit>
							<html:submit property="methodPossSintetica">
								<bean:message key="button.creaPoss" bundle="documentoFisicoLabels" />
							</html:submit>
							<sbn:checkAttivita idControllo="inventari">
								<html:submit property="methodPossSintetica">
									<bean:message key="analitica.button.inventari" bundle="documentoFisicoLabels" />
								</html:submit>
							</sbn:checkAttivita>
						</sbn:checkAttivita></td>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${possessoriSinteticaForm.prov eq 'attivaLegame'}">
						<td align="center"><sbn:checkAttivita idControllo="possessori"><html:submit property="methodPossSintetica">
							<bean:message key="sintetica.button.legameInvPoss" bundle="documentoFisicoLabels" />
						</html:submit></sbn:checkAttivita></td>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${possessoriSinteticaForm.prov eq 'SIMILI'}">
						<td align="center"><sbn:checkAttivita idControllo="possessori"><html:submit property="methodPossSintetica">
							<bean:message key="documentofisico.bottone.indietro" bundle="documentoFisicoLabels" />
						</html:submit></sbn:checkAttivita></td>

						 <!--tasti supplememtari sulla mappa di lista simili trovati per gestire la fusione fra Possessori
						  in uscita da una richiesta di variazioneDescrizione (nuove funzionalità: Torna al dettaglio; Fondi oggetti;)-->
						<td align="center"><sbn:checkAttivita idControllo="possessori"><html:submit property="methodPossSintetica">
							<bean:message key="possessori.tornaAldettaglio" bundle="documentoFisicoLabels" />
						</html:submit></sbn:checkAttivita></td>
						<td align="center"><sbn:checkAttivita idControllo="possessori"><html:submit property="methodPossSintetica">
							<bean:message key="possessori.fondiPossessori" bundle="documentoFisicoLabels" />
						</html:submit></sbn:checkAttivita></td>


					</c:when>
				</c:choose>
				<c:choose>
					<c:when
						test="${possessoriSinteticaForm.prov eq 'normale' or possessoriSinteticaForm.prov eq 'attivaLegame'}">
						<td align="right"><sbn:checkAttivita idControllo="possessori"><html:submit styleClass="buttonSelezTutti"
							property="methodPossSintetica" title="Seleziona tutto">
							<bean:message key="sintetica.button.selAllPossessori"
								bundle="gestioneBibliograficaLabels" />
						</html:submit> <html:submit styleClass="buttonSelezNessuno"
							property="methodPossSintetica" title="Deseleziona tutto">
							<bean:message key="sintetica.button.deSelAllPossessori"
								bundle="gestioneBibliograficaLabels" />
						</html:submit></sbn:checkAttivita></td>
					</c:when>
				</c:choose>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
