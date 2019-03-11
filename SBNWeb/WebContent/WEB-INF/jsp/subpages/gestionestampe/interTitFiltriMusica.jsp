<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>



<table width="100%"  border="0" bgcolor="#FFCC99">
  <tr>
    <td>
	<table width="100%"  border="0">
      <tr>
        <td width="20%" class="etichetta">
        	<bean:message key="ricerca.musica.elaborazione" bundle="gestioneBibliograficaLabels" />
        </td>
        <td width="80%">
			<html:select property="interrMusic.elaborazioneSelez" style="width:40px">
			<html:optionsCollection property="interrMusic.listaElaborazioni" value="codice"
				label="descrizioneCodice" />
			</html:select>
		</td>
      </tr>
    </table>
</table>
