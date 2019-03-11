<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>

<div id="header"></div>
<div id="data">
	<sbn:navform action="/serviziweb/riproduzioneTariffa.do" >
	<table cellspacing="0" width="100%" border="0">
				<tr>
				<th colspan="4" class="etichetta" align="right">
					<c:out value="${menuServiziForm.biblioSel}"> </c:out>
				<hr>
				</th>
			</tr>

			<tr>
				<th colspan="4" class="etichetta" align="right">
				<bean:message key="servizi.documento.poloEserWeb"
					bundle="serviziWebLabels" /> -
				<bean:message key="servizi.utenti.utenteConn"
					bundle="serviziWebLabels" />

					<c:out value="${selezioneBibliotecaForm.utenteCon}"> </c:out>
				<hr>
				</th>
			</tr>

			<tr>
				<th colspan="4" class="etichetta" align="center">
				<bean:message key="servizi.documento.richServLocale"
					bundle="serviziWebLabels" />
				</th>
			</tr>
			<tr>
				<td >
					&nbsp;
				</td>
			</tr>
			<tr>
				<td colspan="4" align="center">
					<bean:message key="servizi.documento.richServLocalePerVolume"
					bundle="serviziWebLabels" /><br>
					- <b><c:out value="${riproduzioneForm.titolo}"> </c:out></b>
				</td>
			</tr>
			<tr>
				<td colspan="4" align="center">
					<sbn:errors />
				</td>
			</tr>
			<tr>
			<!-- data richiesta -->
				<td  class="etichetta" align="right">
					<bean:message key="servizi.documento.dataRic"
					bundle="serviziWebLabels" />
				</td>
				<td>
					<html:text property="dataRichiesta"
							styleId="testoNormale" size="10" maxlength="10" readonly="true"></html:text>
				</td>
			</tr>

			<tr>
				<!-- Spesa Max-->
				<td  class="etichetta"  align="right">
					<bean:message key="servizi.documento.spesa"
					bundle="serviziWebLabels" />
				</td>
				<td>
					<html:text property="spesaMassima"
							styleId="testoNormale" size="20" maxlength="20"></html:text>
				</td>
			</tr>
			<tr>
				<!-- Volume Interesse -->
				<td  class="etichetta" align="right">
					<bean:message key="servizi.documento.volInter"
					bundle="serviziWebLabels" />
				</td>
				<td>
					<html:text property="volumeInterresse"
							styleId="testoNormale" size="20" maxlength="20"></html:text>
				</td>
			</tr>
			<tr>
				<!-- Numero Fascicolo -->
				<td  class="etichetta" align="right">
					<bean:message key="servizi.documento.numFasc"
					bundle="serviziWebLabels" />
				</td>
				<td>
					<html:text property="numFascicolo"
							styleId="testoNormale" size="20" maxlength="20"></html:text>
				</td>
			</tr>
			<tr>
				<!-- Anno Periodico -->
				<td  class="etichetta" align="right">
					<bean:message key="servizi.documento.annoPeriodico"
					bundle="serviziWebLabels" />
				</td>
				<td>
					<html:text property="annoPeriodico"
							styleId="testoNormale" size="4" maxlength="4"></html:text>
				</td>
			</tr>

			<tr>
				<!-- Pagine da riprodurre -->
				<td  class="etichetta" align="right">
					<bean:message key="servizi.documento.pagineRiprod"
					bundle="serviziWebLabels" />
				</td>
				<td>
					<html:text property="pagineDaRiprod"
							styleId="testoNormale" size="20" maxlength="20"></html:text>

				</td>
			</tr>
			<!--  Mod erogazione -->
			<tr>
				<td  class="etichetta" align="right">
					<bean:message key="servizi.documento.modErog"
					bundle="serviziWebLabels" />
				</td>

				<td>
					<html:select styleClass="testoNormale" disabled="true"
						property="serv.cod_mod_erog">

						<html:optionsCollection property="modErogazione"
								value="cod_mod_erog" label="descr_mod_erog" />
						</html:select>

				</td>

			</tr>

			<!--  Supporto -->
			<tr>
				<td  class="etichetta" align="right">
					<bean:message key="servizi.documento.supporto"
					bundle="serviziWebLabels" />
				</td>

				<td>
					<html:select styleClass="testoNormale" disabled="true"
						property="serv1.cod_supporto">

						<html:optionsCollection property="supporto"
								value="cod_supporto" label="descr_supporto" />
						</html:select>

				</td>

			</tr>
			<tr>
				<!-- Tariffa -->
				<td  class="etichetta" align="right">
					<bean:message key="servizi.documento.tariffa"
					bundle="serviziWebLabels" />
				</td>
				<td>
					<html:text property="tariffa"
							styleId="testoNormale" size="20" maxlength="20" readonly="true"></html:text>
				</td>
			</tr>
			<tr>
				<!-- Note utente -->
				<td  class="etichetta" align="right">
					<bean:message key="servizi.documento.noteUte"
					bundle="serviziWebLabels" />
				</td>
				<td>
					<html:textarea property="noteUtente"
							styleId="testoNormale" rows="5" cols="20"></html:textarea>
				</td>
			</tr>

			<tr>
				<td align="center" colspan="2">

					<html:submit styleClass="submit" property="paramRiproduzioneTariffa">
						<bean:message key="servizi.bottone.ok" bundle="serviziWebLabels" />
					</html:submit>

					<html:submit styleClass="submit" property="paramRiproduzioneTariffa" >
						<bean:message key="servizi.bottone.indietro" bundle="serviziWebLabels" />
					</html:submit>

				</td>
			</tr>
		</table>
	</sbn:navform>
</div>
</layout:page>