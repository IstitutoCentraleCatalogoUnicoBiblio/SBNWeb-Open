<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="noinput" value="false" />
<bean-struts:define id="noBil" value="false" />
<bean-struts:define id="noSez" value="false" />
<bean-struts:define id="noProf" value="false" />

<logic:equal name="configurazioneORDForm" property="enableBil"
	value="false">
	<bean-struts:define id="noBil" value="true" />
</logic:equal>
<logic:equal name="configurazioneORDForm" property="enableSez"
	value="false">
	<bean-struts:define id="noSez" value="true" />
	<bean-struts:define id="noProf" value="true" />
</logic:equal>
<logic:equal name="configurazioneORDForm" property="enableProf"
	value="false">
	<bean-struts:define id="noProf" value="true" />
</logic:equal>

<c:choose>
	<c:when test="${configurazioneORDForm.disabilitaTutto}">
		<bean-struts:define id="noinput" value="true" />
		<bean-struts:define id="noBil" value="true" />
		<bean-struts:define id="noSez" value="true" />
		<bean-struts:define id="noProf" value="true" />

	</c:when>
</c:choose>


<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/configurazione/configurazioneORD.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>

			<jsp:include
				page="/WEB-INF/jsp/subpages/acquisizioni/configurazione/configurazioneTabORD.jsp" />

			<table width="100%" border="0">
				<tr>
					<td scope="col" class="etichetta" align="left" width="30%"><bean:message
							key="ricerca.label.codBiblImpost" bundle="acquisizioniLabels" />
					</td>
					<td scope="col" align="left"><html:text styleId="testoNormale"
							property="datiConfigORD.codBibl" size="3" readonly="true"></html:text>
						<html:submit title="elenco" styleClass="buttonImageListaSezione"
							property="methodConfigurazioneORD" disabled="${noinput}">
							<bean:message key="ricerca.label.bibliolist"
								bundle="acquisizioniLabels" />
						</html:submit></td>
					<td scope="col" class="etichetta" align="left"></td>

				</tr>
				<tr>
					<td scope="col" class="etichetta" align="right"><bean:message
							key="configurazione.label.gestBil" bundle="acquisizioniLabels" />
					</td>
					<td scope="col" align="left"><html:checkbox property="gestBil"
							onchange="this.form.submit();" disabled="${noinput}"></html:checkbox>

					</td>
					<td scope="col" class="etichetta" align="left"></td>

				</tr>
				<tr>
					<td scope="col" class="etichetta" align="right"><bean:message
							key="configurazione.label.gestSez" bundle="acquisizioniLabels" />
					</td>
					<td scope="col" align="left"><html:checkbox property="gestSez"
							onchange="this.form.submit();" disabled="${noinput}"></html:checkbox>
					</td>
					<td scope="col" class="etichetta" align="left"></td>

				</tr>
				<tr>
					<td scope="col" class="etichetta" align="right"><bean:message
							key="configurazione.label.gestProf" bundle="acquisizioniLabels" />
					</td>
					<td scope="col" align="left"><html:checkbox
							property="gestProf" onchange="this.form.submit();"
							disabled="${noinput}"></html:checkbox></td>
					<td scope="col" class="etichetta" align="left"></td>
				</tr>
				<tr>
					<td colspan="3">
						<hr></hr>
					</td>
				</tr>
				<!--
				     <tr>
                        <td   scope="col" class="etichetta" align="right">
							<bean:message  key="ricerca.label.capitoloBilDefault" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							<html:text styleId="testoNormale"  property="datiConfigORD.chiaveBilancio.codice1" size="4" readonly="${noBil}" ></html:text>
							<html:text styleId="testoNormale"   property="datiConfigORD.chiaveBilancio.codice2"  size="2" maxlength="16" readonly="${noBil}" ></html:text>
				            <html:submit  styleClass="buttonImage" property="methodConfigurazioneORD" disabled="${noBil}"  onchange="this.form.submit();" >
								<bean:message  key="ordine.label.bilancio" bundle="acquisizioniLabels" />
							</html:submit>
                        </td>
                        <td   scope="col" class="etichetta" align="left">
                        </td>

       	     		</tr>


				     <tr>
                        <td   scope="col" class="etichetta" align="right">
 							<bean:message  key="configurazione.label.sezDefault" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
	                        <div align="left" valign="top">
	              			<html:text styleId="testoNormale" property="datiConfigORD.codiceSezione" size="4" maxlength="4" readonly="${noSez}"></html:text>
	                        <html:submit  styleClass="buttonImage" property="methodConfigurazioneORD" disabled="${noSez}">
								<bean:message  key="ricerca.label.sezione" bundle="acquisizioniLabels" />
							</html:submit>
	                        </div>

                        </td>
                        <td   scope="col" class="etichetta" align="left">
                        </td>
       	     		</tr>




				     <tr>
                        <td   scope="col" class="etichetta" align="right">
							<bean:message  key="ordine.label.fornitoreDefault" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
                        </td>
                        <td   scope="col" class="etichetta" align="left">
                        </td>

       	     		</tr>

				     <tr>
                        <td   scope="col"  align="right">
							<bean:message  key="ordine.label.acquisto" bundle="acquisizioniLabels" />
                        </td>
                        <td   scope="col" class="etichetta" align="left">
				 		  <html:text styleId="testoNormale" property="datiConfigORD.fornitoriDefault[0].descrizione" size="5"  maxlength="10" readonly="${noinput}"></html:text>
	                        <html:submit  styleClass="buttonImage" property="methodConfigurazioneORD" disabled="${noinput}">
								<bean:message  key="ordine.label.fornitore0" bundle="acquisizioniLabels" />
							</html:submit>
                        </td>

                        <td scope="col" align="left" >

                        </td>

       	     		</tr>
				     <tr>
                        <td   scope="col"  align="right">
							<bean:message  key="ordine.label.depositoLegale" bundle="acquisizioniLabels" />
                        </td>
                        <td   scope="col" align="left">
				 		  <html:text styleId="testoNormale" property="datiConfigORD.fornitoriDefault[1].descrizione" size="5"  maxlength="10" readonly="${noinput}"></html:text>
	                        <html:submit  styleClass="buttonImage" property="methodConfigurazioneORD" disabled="${noinput}">
								<bean:message  key="ordine.label.fornitore1" bundle="acquisizioniLabels" />
							</html:submit>
                        </td>

                        <td scope="col" align="left" >
                        </td>
       	     		</tr>
				     <tr>
                        <td   scope="col"  align="right">
							<bean:message  key="ordine.label.dono" bundle="acquisizioniLabels" />
                        </td>
                        <td   scope="col" class="etichetta" align="left">
				 		  <html:text styleId="testoNormale" property="datiConfigORD.fornitoriDefault[2].descrizione" size="5"  maxlength="10" readonly="${noinput}"></html:text>
	                        <html:submit  styleClass="buttonImage" property="methodConfigurazioneORD" disabled="${noinput}">
								<bean:message  key="ordine.label.fornitore2" bundle="acquisizioniLabels" />
							</html:submit>
                        </td>
                        <td scope="col" align="left" >

                        </td>

       	     		</tr>
				     <tr>
                        <td   scope="col"  align="right">
							<bean:message  key="ordine.label.visioneTrattenuta" bundle="acquisizioniLabels" />
                        </td>
                        <td   scope="col" class="etichetta" align="left">
				 		  <html:text styleId="testoNormale" property="datiConfigORD.fornitoriDefault[3].descrizione" size="5"  maxlength="10" readonly="${noinput}"></html:text>
	                        <html:submit  styleClass="buttonImage" property="methodConfigurazioneORD" disabled="${noinput}">
								<bean:message  key="ordine.label.fornitore3" bundle="acquisizioniLabels" />
							</html:submit>
                        </td>
                        <td scope="col" align="left" >
                        </td>

       	     		</tr>
				     <tr>
                        <td   scope="col"  align="right">
							<bean:message  key="ordine.label.cambio" bundle="acquisizioniLabels" />
                        </td>
                        <td   scope="col" class="etichetta" align="left" >
				 		  <html:text styleId="testoNormale" property="datiConfigORD.fornitoriDefault[4].descrizione" size="5"  maxlength="10" readonly="${noinput}"></html:text>
	                        <html:submit  styleClass="buttonImage" property="methodConfigurazioneORD" disabled="${noinput}">
								<bean:message  key="ordine.label.fornitore4" bundle="acquisizioniLabels" />
							</html:submit>
                        </td>
                        <td scope="col" align="left" >

                        </td>

       	     		</tr>
				     <tr>
                        <td   scope="col"  align="right">
							<bean:message  key="ordine.label.rilegatura" bundle="acquisizioniLabels" />
                        </td>
                        <td   scope="col" class="etichetta" align="left">
				 		  <html:text styleId="testoNormale" property="datiConfigORD.fornitoriDefault[5].descrizione" size="5"  maxlength="10" readonly="${noinput}"></html:text>
	                        <html:submit  styleClass="buttonImage" property="methodConfigurazioneORD" disabled="${noinput}">
								<bean:message  key="ordine.label.fornitore5" bundle="acquisizioniLabels" />
							</html:submit>
                        </td>
                        <td scope="col" align="left" >

                        </td>
       	     		</tr>
<tr><td colspan="3">
<hr></hr>
</td></tr>

				     <tr>
                        <td   scope="col" class="etichetta" align="right">
							<bean:message  key="configurazione.label.visualizzazione" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" colspan="2" align="left" class="etichetta">
							<bean:message  key="ordine.label.aperti" bundle="acquisizioniLabels" />
							<html:checkbox name="configurazioneORDForm" property="ordAperti" disabled="${noinput}" onchange="this.form.submit();" ></html:checkbox>
							&nbsp;
							<bean:message  key="ordine.label.chiusi" bundle="acquisizioniLabels" />
							<html:checkbox name="configurazioneORDForm" property="ordChiusi" disabled="${noinput}" onchange="this.form.submit();" ></html:checkbox>
							&nbsp;
							<bean:message  key="ordine.label.annullati" bundle="acquisizioniLabels" />
							<html:checkbox name="configurazioneORDForm" property="ordAnnullati" disabled="${noinput}" onchange="this.form.submit();"></html:checkbox>
                        </td>
       	     		</tr>
-->
				<tr>
					<td scope="col" class="etichetta" align="right"><bean:message
							key="configurazione.label.allineamento"
							bundle="acquisizioniLabels" />&nbsp;&#x00B9;</td>
					<td scope="col" align="left" class="etichetta" colspan="2"><html:select
							style="width:40px" styleClass="testoNormale"
							property="datiConfigORD.allineamento" disabled="${noinput}">
							<html:optionsCollection property="listaAllineamento"
								value="codice" label="descrizione" />
						</html:select></td>
				</tr>
				<!-- codice biblio google -->
				<tr>
					<td colspan="3">
						<hr></hr>
					</td>
				</tr>
				<tr>
					<td class="etichetta" valign="top"><bean:message
							key="ordine.label.fornitore" bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left" valign="top"><html:text
							styleId="testoNormale" property="codFornitore" size="5"
							maxlength="10" disabled="${noinput}"></html:text> <html:text
							styleId="testoNormale" property="fornitore" size="50"
							maxlength="50" disabled="${noinput}"></html:text> <html:submit
							styleClass="buttonImage"
							property="${navButtons}"
							disabled="${noinput}">
							<bean:message key="ordine.label.fornitore"
								bundle="acquisizioniLabels" />
						</html:submit></td>
				</tr>
				<tr>
					<td scope="col" class="etichetta" align="right"><bean:message
							key="configurazione.label.cd_bib_google"
							bundle="acquisizioniLabels" /></td>
					<td scope="col" align="left" class="etichetta" colspan="2"><html:text
							maxlength="255" size="30" styleClass="testoNormale"
							property="datiConfigORD.cd_bib_google" disabled="${noinput}" />
					</td>
				</tr>

				<tr>
					<td colspan="3">
						<hr></hr>
					</td>
				</tr>

			</table>
			<br />
			<div class="msgOK1n">&#x00B9;&nbsp;</div>
			<div class="msgOK1">
				<bean:message
					key="configurazione.label.warning.allineamento"	bundle="acquisizioniLabels" />
			</div>
		</div>
		<div id="divFooter">
			<c:choose>
				<c:when test="${configurazioneORDForm.conferma}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />
				</c:when>
				<c:otherwise>

					<table border="0" style="height: 40px" align="center">
						<tr>
							<td scope="col"><sbn:checkAttivita idControllo="GESTIONE">

									<html:submit styleClass="pulsanti"
										property="methodConfigurazioneORD">
										<bean:message key="ricerca.button.salva"
											bundle="acquisizioniLabels" />
									</html:submit>
									<html:submit styleClass="pulsanti"
										property="methodConfigurazioneORD">
										<bean:message key="ricerca.button.ripristina"
											bundle="acquisizioniLabels" />
									</html:submit>
								</sbn:checkAttivita> <!--
					<html:submit styleClass="pulsanti" property="methodconfigurazioneORD">
						<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
					</html:submit>
					 --></td>
						</tr>
					</table>
				</c:otherwise>
			</c:choose>
		</div>
	</sbn:navform>
</layout:page>
