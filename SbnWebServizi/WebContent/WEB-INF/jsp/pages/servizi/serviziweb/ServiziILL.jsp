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
	<sbn:navform action="/serviziweb/serviziILL.do" >
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
				<bean:message key="servizi.documento.datiDoc"
					bundle="serviziWebLabels" /> - ILL
				</th>
			</tr>
			<tr>
				<td >
					&nbsp;
				</td>
			</tr>
			<tr>
				<td  class="etichetta" colspan="2" align="center">
					<bean:message key="servizi.documento.complRich"
					bundle="serviziWebLabels" />
				</td>
			</tr>
			<tr>
				<td colspan="4" align="center">
					<sbn:errors />
				</td>
			</tr>
			<tr>
			<!-- Segnatura -->
				<td  class="etichetta" align="right">
					<bean:message key="servizi.documento.segn"
					bundle="serviziWebLabels" />
				</td>
				<td>
					<html:text property="segnatura"
							styleId="testoNormale" size="30" maxlength="30"></html:text>
				</td>
			</tr>
			<tr>
				<!-- Tipo Doc -->
					<td  class="etichetta" align="right">
						<bean:message key="servizi.documento.headerDocumento"
						bundle="serviziWebLabels" />
					</td>

					<td align="left">
						<html:select styleClass="testoNormale"
							property="tipoD.cod_tipo_doc">

							<html:optionsCollection property="listaTipoDoc"
								value="cod_tipo_doc" label="desc_tipo_doc" />
							</html:select>
					</td>
				</tr>
			<tr>
				<!-- Autore -->
				<td  class="etichetta" align="right" >
					<bean:message key="servizi.documento.autore"
					bundle="serviziWebLabels" />
				</td>
				<td>
					<html:text property="autore"
							styleId="testoNormale" size="30" maxlength="30"></html:text>
				</td>
			</tr>
			<tr>
				<!-- Titolo -->
				<td  class="etichetta" align="right">
					<bean:message key="servizi.documento.titolo"
					bundle="serviziWebLabels" />
				</td>
				<td>
					<html:text property="titolo"
							styleId="testoNormale" size="30" maxlength="30"></html:text>
				</td>
			</tr>

			<tr>
				<!-- Luogo edi -->
				<td  class="etichetta"  align="right">
					<bean:message key="servizi.documento.luogoEdi"
					bundle="serviziWebLabels" />
				</td>
				<td>
					<html:text property="luogoEdizione"
							styleId="testoNormale" size="20" maxlength="20"></html:text>
				</td>
			</tr>
			<tr>
				<!-- Editore -->
				<td  class="etichetta" align="right">
					<bean:message key="servizi.documento.editore"
					bundle="serviziWebLabels" />
				</td>
				<td>
					<html:text property="editore"
							styleId="testoNormale" size="30" maxlength="30"></html:text>
				</td>
			</tr>
			<tr>
				<!-- Anno edi -->
				<td  class="etichetta" align="right">
					<bean:message key="servizi.documento.annoEdi"
					bundle="serviziWebLabels" />
				</td>
				<td>
					<html:text property="annoEdi"
							styleId="testoNormale" size="4" maxlength="4"></html:text>
				</td>
			</tr>

			<tr>
				<!-- Biblioteca dest -->
				<td  class="etichetta" align="right" rowspan="2" valign="top">
					<bean:message key="servizi.documento.biblioDest"
					bundle="serviziWebLabels" />
				</td>



					<td align="left" rowspan="2">
						<html:select styleClass="testoNormale"
							property="bib.cod_bib">

							<html:optionsCollection property="listabibDest"
								value="cod_bib" label="nom_biblioteca" />
							</html:select>
							<br>
							<bean:message key="servizi.documento.selBiblioILL"
					bundle="serviziWebLabels" />
					<br>

						<html:text property="biblioDest"
							styleId="testoNormale" size="30" maxlength="30"></html:text>
					</td>
		    </tr>
			<tr>

			</tr>
			<tr>
				<td align="center" colspan="2">

					<html:submit styleClass="submit" property="paramDatiServiziILL">
						<bean:message key="servizi.bottone.ok" bundle="serviziWebLabels" />
					</html:submit>
					<html:submit styleClass="submit" property="paramDatiServiziILL" >
						<bean:message key="servizi.bottone.indietro" bundle="serviziWebLabels" />
					</html:submit>

				</td>
			</tr>
		</table>
	</sbn:navform>
</div>
</layout:page>