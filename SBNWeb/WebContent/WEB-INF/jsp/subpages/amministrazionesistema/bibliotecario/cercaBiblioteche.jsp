<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sbn" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<table summary="">
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.cod.polo" bundle="amministrazioneSistemaLabels" />:
		</td>
		<td>
			<html:text styleId="codPolo"
					property="codPolo" size="10" maxlength="10"></html:text>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.cod.biblio" bundle="amministrazioneSistemaLabels" />:
		</td>
		<td>
			<html:text styleId="codBiblio"
					property="codBiblio" size="10" maxlength="10"></html:text>
		</td>
	</tr>

	<tr>
		<td>
			<label for="tipoBiblio">
				<bean:message key="ricerca.biblioteca.tipo" bundle="amministrazioneSistemaLabels" />:
			</label>
		</td>
		<td>
			<html:select styleId="tipoBiblio" property="tipo">
				<html:option value=""></html:option>
				<html:optionsCollection property="tipiBiblioteche"
					value="CD_TABELLA" label ="DS_TABELLA" />
			</html:select>
		</td>
	</tr>

	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.nome.biblio" bundle="amministrazioneSistemaLabels" />:
		</td>
		<td>
			<html:text styleId="nomeBiblio"
					property="nomeBiblio" size="60" maxlength="50"></html:text>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.ente" bundle="amministrazioneSistemaLabels" />:
		</td>
		<td>
			<html:text styleId="ente"
					property="ente" size="60" maxlength="50" ></html:text>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.provincia" bundle="amministrazioneSistemaLabels" />:
		</td>
		<td>
			<html:select styleId="provincie" property="provincie" >
				<html:option value="">&nbsp;</html:option>
				<html:optionsCollection property="provincieCollection"
					value="CD_TABELLA" label="DS_TABELLA" />
			</html:select>
		</td>
	</tr>
	<tr>
		<td><label for="paese"> <bean:message key="ricerca.biblioteca.paese"
			bundle="amministrazioneSistemaLabels" />:</label>
		</td>
		<td>
			<html:select styleId="paese" property="paese">
				<html:option value="" >&nbsp;</html:option>
				<html:optionsCollection property="paesiCollection"
					value="CD_TABELLA" label="DS_TABELLA" />
			</html:select>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>
			<html:submit styleClass="submit" property="cercaBtn" title="Ricerca biblioteche">
				<bean:message key='ricerca.biblioteca.button.cerca' bundle='amministrazioneSistemaLabels' />
			</html:submit>
			<html:submit styleClass="submit" property="nuovaBtn" title="Nuova biblioteca">
				<bean:message key='ricerca.button.biblioteca.nuova' bundle='amministrazioneSistemaLabels' />
			</html:submit>
			<html:submit styleClass="reset" property="resetBtn" title="Pulisci schermata">
				<bean:message key='button.reset' bundle='amministrazioneSistemaLabels' />
			</html:submit>
		</td>
	</tr>
</table>
