<!--	SBNWeb - Rifacimento ClientServer
		JSP di dettaglio Possessori
		Alessandro Segnalini - Inizio Codifica Marzo 2008
-->
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>


<html:xhtml/>
<layout:page>
	<sbn:navform action="/documentofisico/possessori/variazioneLegame.do">

		<div id="divForm">

		<div id="divMessaggio">
			<sbn:errors />
		</div>

		<table border="0">
			<tr>
				<td width="130" class="etichetta">
					<bean:message key="documentofisico.legame.PIDlegame" bundle="documentoFisicoLabels" />
				</td>
				<td class="testoNormale">
					<html:text property="possDettVO.pid" size="10" maxlength="10" readonly="true"></html:text>
				</td>
				<td class="testoNormale">
					<html:text property="possDettVO.nome" size="50" maxlength="100" readonly="true"></html:text>
				</td>
			</tr>
		</table>
		<table border="0">
			<tr>
				<td width="100" class="etichetta">
					<bean:message key="documentofisico.legame.tipoLegame" bundle="documentoFisicoLabels" />
				</td>
				<td class="testoNormale">
					<html:text property="tipoCollegamento" size="5" maxlength="1" readonly="true"></html:text>
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td width="30" class="etichetta">
					<bean:message key="documentofisico.legame.PID" bundle="documentoFisicoLabels" />
				</td>
				<td class="testoNormale">
					<html:text property="pidLegame" size="10" maxlength="10" readonly="true"></html:text>
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td width="100" class="etichetta">
					<bean:message key="documentofisico.legame.notaLegame" bundle="documentoFisicoLabels" />
				</td>
				<td class="testoNormale">
					<html:textarea property="notaAlLegame" rows="3" cols="70" ></html:textarea><sbn:tastiera limit="80" property="possDettVO.nota" name="possessoriVariaLegameForm"></sbn:tastiera>
				</td>
			</tr>
		</table>

		</div>

		<div id="divFooterCommon">

		</div>
		<div id="divFooter">

		<table align="center">
			<tr>
				<td align="center">
					<html:submit property="methodPossNotaLegame">
						<bean:message key="dettaglio.button.salva" bundle="documentoFisicoLabels" />
					</html:submit>
					<html:submit property="methodPossNotaLegame">
						<bean:message key="dettaglio.button.annulla" bundle="documentoFisicoLabels" />
					</html:submit>
				</td>

			</tr>
		</table>
		</div>

	</sbn:navform>
</layout:page>

