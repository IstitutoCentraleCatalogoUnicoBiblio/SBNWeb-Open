<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>

<table align="center">
	<tr>
		<td align="center">
			<html:submit property="methodErogazione" styleId="btnAvanti" >
				<bean:message key="servizi.bottone.avanti" bundle="serviziLabels" />
			</html:submit>
		</td>
	</tr>
</table>
