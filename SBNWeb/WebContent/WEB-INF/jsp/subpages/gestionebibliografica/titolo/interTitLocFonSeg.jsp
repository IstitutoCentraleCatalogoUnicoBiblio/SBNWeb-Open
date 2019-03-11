<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Area Musica
		almaviva2 - Inizio Codifica Agosto 2006
-->
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>


<table width="100%" border="0">
	<tr>
		<td width="130" class="etichetta"><bean:message
			key="ricerca.musica.localizzazione"
			bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><input type="text"
			name="interrMusic.localizzazione" size="40" maxlength="6"
			id="testoNormale"></td>
	</tr>
	<tr>
		<td width="130" class="etichetta"><bean:message
			key="ricerca.musica.fondo" bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><input type="text" name="interrMusic.fondo"
			size="85" maxlength="55" value="" id="testoNormale"> <sbn:tastiera limit="80" property="interrMusic.fondo" name="interrogazioneTitoloPerGestionaliForm"></sbn:tastiera></td>
	</tr>
	<tr>
		<td width="130" class="etichetta"><bean:message
			key="ricerca.musica.segnatura" bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><input type="text"
			name="interrMusic.segnatura" size="85" maxlength="30" value=""
			id="testoNormale"> <sbn:tastiera limit="80" property="interrMusic.segnatura" name="interrogazioneTitoloPerGestionaliForm"></sbn:tastiera></td>
	</tr>
</table>
