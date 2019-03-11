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
	<sbn:navform action="/documentofisico/possessori/variazioneDescrizioneFormaAccettata.do">

		<div id="divForm">

		<div id="divMessaggio">
			<sbn:errors />
		</div>

		<table border="0">
			<tr>
				<td width="80" class="etichetta">
					<bean:message key="documentofisico.dettaglio.PID" bundle="documentoFisicoLabels" />
				</td>
				<td class="testoNormale">
					<html:text property="possDettVO.pid" size="10" maxlength="10" readonly="true"></html:text>
				</td>
				<td  class="etichetta" style="text-align: right">
					<bean:message key="documentofisico.dettaglio.livello" bundle="documentoFisicoLabels" />
				</td>
				<td class="testoNormale">
					<html:select property="possDettVO.livello" style="width:40px">
						<html:optionsCollection property="possDettVO.listalivAutority" value="codice" label="descrizioneCodice" />
					</html:select>
				</td>
				<td  class="etichetta">
					<bean:message key="documentofisico.dettaglio.forma" bundle="documentoFisicoLabels" />
				</td>
				<td class="testoNormale">
					<html:text property="possDettVO.forma" size="5" maxlength="1" readonly="true" value="A"></html:text>
				</td>
				<td class="etichetta">
					<bean:message key="documentofisico.dettaglio.tipoNome" bundle="documentoFisicoLabels" />
				</td>
				<td class="testoNormale">
					<html:select property="possDettVO.tipoTitolo" style="width:40px">
						<html:optionsCollection property="possDettVO.listaTipoTitolo" value="codice" label="descrizioneCodice" />
					</html:select>
				</td>
			</tr>
		</table>
		<table>

			<tr>
				<td width="80" class="etichetta">
					<bean:message key="documentofisico.dettaglio.nome" bundle="documentoFisicoLabels" />
				</td>
				<td width="500" class="testoNormale">
					<html:textarea property="possDettVO.nome" rows="3" cols="70" ></html:textarea><sbn:tastiera limit="80" property="possDettVO.nome" name="possessoriDettaglioForm"></sbn:tastiera>
				</td>
			</tr>
			<tr>
				<td width="80" class="etichetta">
					<bean:message key="documentofisico.dettaglio.nota" bundle="documentoFisicoLabels" />
				</td>
				<td width="500" class="testoNormale">
					<html:textarea property="possDettVO.nota" rows="3" cols="70" ></html:textarea><sbn:tastiera limit="80" property="possDettVO.nota" name="possessoriDettaglioForm"></sbn:tastiera>
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
					<html:submit property="methodPossDettaglioFA">
						<bean:message key="dettaglio.button.salva" bundle="documentoFisicoLabels" />
					</html:submit>
					<html:submit property="methodPossDettaglioFA">
						<bean:message key="dettaglio.button.annulla" bundle="documentoFisicoLabels" />
					</html:submit>
				</td>

			</tr>
		</table>
		</div>

	</sbn:navform>
</layout:page>

