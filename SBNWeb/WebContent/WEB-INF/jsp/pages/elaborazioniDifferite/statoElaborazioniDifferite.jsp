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
<c:choose>
	<c:when test="${statoElaborazioniDifferiteForm.disabilitaTutto}">
		<script type="text/javascript">
		<bean-struts:define id="noinput"  value="true"/>
      </script>
	</c:when>
</c:choose>


<html:xhtml />
<layout:page>
	<sbn:navform
		action="/elaborazioniDifferite/statoElaborazioniDifferite.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>

		<br />
		<table width="100%" border="0">
			<tr>
				<td scope="col" class="etichetta" colspan="2" width="30%"><bean:message
					key="label.ricerca.visibilita" bundle="elaborazioniDifferiteLabels" />
				</td>
				<td scope="col" class="etichetta"><html:select
					property="ricerca.visibilita">
					<html:optionsCollection property="listaVisibilita" value="codice"
						label="descrizione" />
				</html:select></td>
			</tr>
			<tr>
				<td scope="col" class="etichetta" colspan="2"><bean:message
					key="label.ricerca.procedura" bundle="elaborazioniDifferiteLabels" />
				</td>
				<td scope="col" class="etichetta"><html:select
					property="ricerca.procedura">
					<html:optionsCollection property="listaProcedure" value="codice"
						label="descrizione" />
				</html:select></td>
			</tr>
			<tr>
				<td scope="col" class="etichetta" colspan="2"><bean:message
					key="label.ricerca.identificativo"
					bundle="elaborazioniDifferiteLabels" /></td>
				<td scope="col" class="etichetta"><html:text
					styleId="testoNormale" property="ricerca.idRichiesta" size="10" maxlength="9"
					readonly="${noinput}"></html:text></td>
			</tr>

			<tr>
				<td scope="col" class="etichetta" colspan="2"><bean:message
					key="label.ricerca.dataEsecuzioneProgrammata"
					bundle="elaborazioniDifferiteLabels" /></td>
				<td scope="col" class="etichetta"><html:select
					property="ricerca.dataEsecuzioneProgrammata">
					<html:optionsCollection property="listaDataEsecuzioneProgrammata"
						value="codice" label="descrizione" />
				</html:select></td>
			</tr>
			<tr>
				<td scope="col" class="etichetta" colspan="2"><bean:message
					key="label.ricerca.richiedente"
					bundle="elaborazioniDifferiteLabels" /></td>
				<td scope="col" class="etichetta">
					<html:text styleId="testoNormale" property="ricerca.richiedente" size="8" maxlength="6" readonly="${noinput}"></html:text>
					<html:link action="/amministrazionesistema/ricercaBibliotecario.do?SIF=TRUE" >
						<img border="0" alt="ricerca bibliotecari" src='<c:url value="/images/lente.GIF" />'/>
					</html:link>
				</td>
			</tr>
			<tr>
				<td scope="col" class="etichetta" colspan="2"><bean:message
					key="label.ricerca.stato" bundle="elaborazioniDifferiteLabels" />
				</td>
				<td scope="col" class="etichetta"><html:select
					property="ricerca.stato">
					<html:optionsCollection property="listaStato" value="codice"
						label="descrizione" />
				</html:select></td>
			</tr>
			<tr>
			<td colspan="3">&nbsp;</td>
			</tr>
			<tr>
				<td scope="col" class="etichetta"><bean:message
					key="label.ricerca.dataFrom" bundle="elaborazioniDifferiteLabels" />
					<html:text
					styleId="testoNormale" property="ricerca.dataFrom" size="10" maxlength="10"
					readonly="${noinput}"></html:text></td>
				<td scope="col" class="etichetta" style="text-align: right;"><bean:message
					key="label.ricerca.dataTo" bundle="elaborazioniDifferiteLabels" />&nbsp;</td>
					<td>
					<html:text
					styleId="testoNormale" property="ricerca.dataTo" size="10" maxlength="10"
					readonly="${noinput}"></html:text></td>
			</tr>

		</table>
		<br />
		</div>

		<div id="divFooterCommon">
		<table width="70%" border="0" style="height: 40px">
			<tr>
				<td class="etichetta"><bean:message
					key="label.elementiPerBloccoShort"
					bundle="elaborazioniDifferiteLabels" />&nbsp;<html:text
					styleId="testoNormale" property="ricerca.numeroElementiBlocco" maxlength="4"
					size="4" /></td>

			<td>&nbsp;</td>
				<td class="etichetta"><bean:message
					key="label.ordinamento" bundle="elaborazioniDifferiteLabels" />&nbsp;<html:select
					property="ricerca.ordinamento">
					<html:optionsCollection property="listaTipiOrdinamento"
						value="codice" label="descrizione" />
				</html:select></td>
			</tr>
		</table>
		</div>

		<div id="divFooter">
		<table align="center" border="0" style="height: 40px">
			<tr>
				<td><html:submit styleClass="pulsanti"
					property="methodMap_statoElaborazioniDifferite">
					<bean:message key="button.cerca"
						bundle="elaborazioniDifferiteLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>

	</sbn:navform>
</layout:page>
