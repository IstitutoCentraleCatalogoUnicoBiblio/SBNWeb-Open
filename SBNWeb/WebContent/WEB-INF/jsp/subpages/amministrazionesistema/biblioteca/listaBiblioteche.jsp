<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display"%>

<html:xhtml />
<logic:notEmpty name="ricercaBibliotecheForm" property="listaBiblio">
<div>
	<table width="100%">
		<tr>
			<td>
				<html:text styleId="bloccoSelezionato" styleClass="obbligatorio" property="bloccoSelezionato" size="5" maxlength="10">
				</html:text>
			</td>
			<td>
				<html:submit styleClass="submit" property="vaiBloccoBtn" title="Vai al blocco selezionato">Conferma
				</html:submit>
			</td>
			<td>
				Blocchi:&nbsp;${requestScope.RicercaBibliotecheForm.totPagineOut}
				&nbsp;&nbsp;&nbsp;Numero elementi:&nbsp;${totRighe}
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<display:table class="simple" width="100%" border="1"
						requestURI="/amministrazionesistema/ricercaBiblioteche.do"
						name="requestScope.ricercaBibliotecheForm.listaBiblio"
						sort="list" pagesize="15" id="row">

						<display:column title="Selezione" >
							<html:radio name="ricercaBibliotecheForm" property="bibliotecaSelezionata" value="${row.cod_bib}_${row.cod_polo}"></html:radio>
						</display:column>
						<display:column property="cod_polo" title="Codice polo" />
						<display:column property="cod_bib" title="Codice biblioteca" />
						<display:column property="nom_biblioteca" title="Descrizione" />
				</display:table>
		</td>
	</tr>
	<tr>
		<td>
			<html:submit styleClass="submit" property="annullaBtn" title="Annulla ricerca">
				<bean:message key='button.annulla' bundle='amministrazioneSistemaLabels' />
			</html:submit>
		</td>
		<td>
			<html:submit styleClass="submit" property="nuovaBtn" title="Nuova biblioteca">
				<bean:message key='ricerca.button.biblioteca.nuova' bundle='amministrazioneSistemaLabels' />
			</html:submit>
		</td>
		<td>
			<html:submit styleClass="submit" property="dettaglioBtn" title="Dettaglio biblioteca">
				<bean:message key='ricerca.biblioteca.button.dettaglio' bundle='amministrazioneSistemaLabels' />
			</html:submit>
		</td>
	</tr>
	</table>
</div>
</logic:notEmpty>
