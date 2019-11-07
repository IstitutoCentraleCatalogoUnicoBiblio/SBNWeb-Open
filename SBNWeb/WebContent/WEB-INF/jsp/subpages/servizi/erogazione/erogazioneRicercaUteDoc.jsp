<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<br>
<br>
<table width="100%" border="0">
	<tr>
		<td width="7%" align="right">
			<bean:message key="servizi.erog.Utente" bundle="serviziLabels" />
		</td>
		<td  width="1%" align="left">
			<%--
			<html:radio title="ricerca per utente" styleId="radioScelta" property="tipoRicercaStr" value="RICERCA_PER_UTENTE" />
			--%>
		</td>
		<td width="50%" align="left">
			<html:text styleId="testoNoBold" property="anaMov.codUte"  titleKey="servizi.erogazione.codiceUtente" bundle="serviziLabels" size="30"
				maxlength="25" onkeydown="submitOnEnter(event, 'btnAvanti')" />&nbsp;&nbsp;&nbsp;
			<html:submit styleClass="buttonImage" property="methodErogazione" titleKey="servizi.erogazione.ricercaUtente" bundle="serviziLabels">
				<bean:message key="servizi.bottone.hlputente" bundle="serviziLabels" />
			</html:submit>
		</td>

	</tr>

	<tr><td>&nbsp;</td></tr>

	<tr>
		<td width="7%" align="right">
			<bean:message key="servizi.erog.Inventario" bundle="serviziLabels" />
		</td>
		<td width="1%"  align="left">
			<%--
			<html:radio title="ricerca per inventario" styleId="radioScelta" property="tipoRicercaStr" value="RICERCA_PER_INVENTARIO" />
			--%>
		</td>
		<td width="50%" align="left">

			<html:select property="anaMov.codBibInv" titleKey="servizi.erogazione.bibliotecaInventario" bundle="serviziLabels" style="width:38px">
				<html:optionsCollection property="elencoBib" value="codice" label="descrizione"/>
			</html:select>

			<%--
			<html:text title="codice biblioteca inventario" styleId="testoNoBold" property="anaMov.codBibInv" size="8" maxlength="3"></html:text>
			--%>

			&nbsp;&nbsp;&nbsp;
			<html:text styleId="testoNoBold" property="anaMov.codSerieInv" titleKey="servizi.erogazione.codiceSerie" bundle="serviziLabels"
				size="8"  maxlength="3"></html:text>
			&nbsp;&nbsp;&nbsp;
			<html:text styleId="testoNoBold" property="anaMov.codInvenInv" titleKey="servizi.erogazione.codiceInventario" bundle="serviziLabels"
				size="14" maxlength="9" onkeydown="submitOnEnter(event, 'btnAvanti')" />&nbsp;&nbsp;&nbsp;
			<html:submit styleClass="buttonImage" property="methodErogazione" titleKey="servizi.erogazione.ricercaTitolo" bundle="serviziLabels">
				<bean:message key="servizi.bottone.hlpinventario"
					bundle="serviziLabels" />
			</html:submit>
			<sbn:checkAttivita idControllo="RFID">
				&nbsp;&nbsp;&nbsp;RFID&nbsp;<html:text styleId="testoNoBold" property="anaMov.rfidChiaveInventario" size="18"
					onkeydown="submitOnEnter(event, 'btnAvanti')"/>
			</sbn:checkAttivita>
		</td>
	</tr>

	<tr><td>&nbsp;</td></tr>

	<tr>

		<td width="7%" align="right">
			<bean:message key="servizi.erog.Segnatura" bundle="serviziLabels" />
		</td>
		<td width="1%"  align="left">
			<%--
			<html:radio title="ricerca per segnatura" styleId="radioScelta" property="tipoRicercaStr" value="RICERCA_PER_SEGNATURA" />
			--%>
		</td>

		<td width="50%" align="left">

			<html:select property="anaMov.codBibDocLett" disabled="${not empty ErogazioneRicercaForm.anaMov.codBibDocLett}" titleKey="servizi.erogazione.bibliotecaSegnatura" bundle="serviziLabels" style="width:38px">
				<html:optionsCollection property="elencoBib" value="codice" label="descrizione"/>
			</html:select>
			&nbsp;&nbsp;&nbsp;

			<html:text styleId="testoNoBold" property="segnaturaRicerca" size="30"  maxlength="40" onkeydown="submitOnEnter(event, 'btnAvanti')" />
			&nbsp;&nbsp;&nbsp;
			<html:submit styleClass="buttonImage" property="methodErogazione" titleKey="servizi.erogazione.ricercaDocumentiNonSBN" bundle="serviziLabels">
				<bean:message key="servizi.bottone.hlpsegnatura"
					bundle="serviziLabels"/>
			</html:submit>
			&nbsp;&nbsp;&nbsp;
			<html:submit styleClass="buttonDelete" property="methodErogazione" titleKey="servizi.erogazione.ripulisceSegnatura" bundle="serviziLabels">
				<bean:message key="servizi.bottone.ripulisciSegnatura"
					bundle="serviziLabels"/>
			</html:submit>
			&nbsp;&nbsp;&nbsp;
		</td>
	</tr>

	<tr><td>&nbsp;</td></tr>


	<tr>

		<td width="7%" align="right">
		</td>

		<td width="1%"  align="left">
		</td>

		<td>

			<div>
				<bean:message
				key="servizi.erogazione.richieste"
				bundle="serviziLabels" />&nbsp;&nbsp;
				<bean:message
				key="servizi.erogazione.richieste.incorso"
				bundle="serviziLabels" />&nbsp;
				<html:checkbox
				property="anaMov.richiesteInCorso"></html:checkbox>
				<html:hidden property="anaMov.richiesteInCorso" value="false"></html:hidden>&nbsp;&nbsp;
				<bean:message
				key="servizi.erogazione.richieste.respinte"
				bundle="serviziLabels" />&nbsp;
				<html:checkbox
				property="anaMov.richiesteRespinte"></html:checkbox>
				<html:hidden property="anaMov.richiesteRespinte" value="false"></html:hidden>&nbsp;&nbsp;
				<bean:message
				key="servizi.erogazione.richieste.evase"
				bundle="serviziLabels" />&nbsp;
				<html:checkbox
				property="anaMov.richiesteEvase"></html:checkbox>
				<html:hidden property="anaMov.richiesteEvase" value="false"></html:hidden>
			</div>

		</td>
	</tr>

	<tr>
		<td colspan="3">
			<hr/>
		</td>
	</tr>

	<tr>
		<td width="7%" align="right">
			<bean:message key="servizi.label.elementiPerBlocco" bundle="serviziLabels" />
		</td>
		<td width="1%"  align="left">
		<%--
			&nbsp;
		--%>
		</td>
		<td width="50%" align="left">
			<html:text  styleId="testoNoBold" property="anaMov.elemPerBlocchi"
				size="5"  maxlength="3" />
		</td>
		<%--
		<td>
			&nbsp;
		</td>
		--%>
	</tr>
</table>