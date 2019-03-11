<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sbn" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<html:xhtml />
<sbn:page>
	<sbn:errors bundle="stampeLabels" />
	<html:form action="/stampaCodici.do">
	<table width="100%"  border="0">
		<tr>
			<td>
				<label for="tipoMateriale">Tipo Materiale</label>
			</td>
			<td>
				<html:text styleId="tipoMateriale" property="tipoMateriale" size="5" maxlength="4"></html:text>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<html:submit styleClass="submit" value="Stampa" property="btnStampa" title="Stampa" />
          </td>
        </tr>

	</table>

</html:form>
</sbn:page>
