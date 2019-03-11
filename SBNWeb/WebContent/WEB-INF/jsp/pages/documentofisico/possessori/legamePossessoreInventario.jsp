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
	<sbn:navform action="/documentofisico/possessori/creaLegameInventarioPossessore.do">

		<div id="divForm">

		<div id="divMessaggio">
			<sbn:errors />
		</div>

		<table border="0">
			<tr>
				<td width="80" class="etichetta">
					<bean:message key="documentofisico.legame.Inventariolegame" bundle="documentoFisicoLabels" />
				</td>
				<td class="testoNormale">
					<html:text property="codeBib" size="5" maxlength="3" readonly="true"></html:text>
				</td>
				<td class="testoNormale">
					<html:text property="codeSerie" size="5" maxlength="3" readonly="true"></html:text>
				</td>
				<td class="testoNormale">
					<html:text property="codeInv" size="10" maxlength="10" readonly="true"></html:text>
				</td>
			</tr>
		</table>
		<hr color="#dde8f0" />
		<table border="0">
			<tr>
				<td width="100" class="etichetta">
					<bean:message key="documentofisico.legame.tipoLegame" bundle="documentoFisicoLabels" />
				</td>
				<td class="testoNormale">
					<html:select property="codeLegame" >
						<html:option value="" ></html:option>
						<html:option value="P" >Possessore</html:option>
						<html:option value="R" >Provenienza</html:option>
					</html:select>
				</td>
			</tr>
		</table>

		<table>
			<tr>
				<td width="50" class="etichetta">
					<bean:message key="documentofisico.legame.PIDlegame" bundle="documentoFisicoLabels" />
				</td>
				<td class="testoNormale">
					<html:text property="pidOrigine" size="10" maxlength="10" readonly="true"></html:text>
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td width="150" class="etichetta">
					<bean:message key="documentofisico.legame.Desclegame" bundle="documentoFisicoLabels" />
				</td>
				<td class="testoNormale">
					<html:textarea property="nomeOrigine" rows="3" cols="70" readonly="true"></html:textarea>
				</td>
			</tr>

			<tr>
				<td width="100" class="etichetta">
					<bean:message key="documentofisico.legame.notaLegame" bundle="documentoFisicoLabels" />
				</td>
				<td class="testoNormale">
					<html:textarea property="notaAlLegame" rows="5" cols="70" ></html:textarea><sbn:tastiera limit="80" property="notaAlLegame" name="possessoriLegameInventarioForm"></sbn:tastiera>
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
					<html:submit property="methodPossLegInv">
						<bean:message key="dettaglio.button.salva" bundle="documentoFisicoLabels" />
					</html:submit>
					<html:submit property="methodPossLegInv">
						<bean:message key="dettaglio.button.annulla" bundle="documentoFisicoLabels" />
					</html:submit>
				</td>

			</tr>
		</table>
		</div>

	</sbn:navform>
</layout:page>

