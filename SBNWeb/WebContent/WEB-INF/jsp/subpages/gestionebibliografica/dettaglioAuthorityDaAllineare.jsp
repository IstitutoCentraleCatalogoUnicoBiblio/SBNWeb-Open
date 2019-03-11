<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Area superiore con canali e filtri
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

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message key="label.allineam.idLista.Doc" bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><html:text property="areaDatiAllineamentoPoloIndiceVO.numListaDaAllineareDocumento" size="10" readonly="true"></html:text></td>
		<td class="testoNormale">
			<html:submit property="methodRichAllineamenti">
			<bean:message key="button.allineam.idLista.Doc" bundle="gestioneBibliograficaLabels" />
			</html:submit>
		</td>
	</tr>
	<tr>
		<td width="100" class="etichetta"><bean:message key="label.allineam.idLista.Au" bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><html:text property="areaDatiAllineamentoPoloIndiceVO.numListaDaAllineareAutore" size="10" readonly="true"></html:text></td>
		<td class="testoNormale">
			<html:submit property="methodRichAllineamenti">
			<bean:message key="button.allineam.idLista.Au" bundle="gestioneBibliograficaLabels" />
			</html:submit>
		</td>
	</tr>
	<tr>
		<td width="100" class="etichetta"><bean:message key="label.allineam.idLista.Ma" bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><html:text property="areaDatiAllineamentoPoloIndiceVO.numListaDaAllineareMarca" size="10" readonly="true"></html:text></td>
		<td class="testoNormale">
			<html:submit property="methodRichAllineamenti">
			<bean:message key="button.allineam.idLista.Ma" bundle="gestioneBibliograficaLabels" />
			</html:submit>
		</td>
	</tr>
	<tr>
		<td width="100" class="etichetta"><bean:message key="label.allineam.idLista.Lu" bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><html:text property="areaDatiAllineamentoPoloIndiceVO.numListaDaAllineareLuogo" size="10" readonly="true"></html:text></td>
		<td class="testoNormale">
			<html:submit property="methodRichAllineamenti">
			<bean:message key="button.allineam.idLista.Lu" bundle="gestioneBibliograficaLabels" />
			</html:submit>
		</td>
	</tr>
	<tr>
		<td width="100" class="etichetta"><bean:message key="label.allineam.idLista.Tu" bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><html:text property="areaDatiAllineamentoPoloIndiceVO.numListaDaAllineareTitUniforme" size="10" readonly="true"></html:text></td>
		<td class="testoNormale">
			<html:submit property="methodRichAllineamenti">
			<bean:message key="button.allineam.idLista.Tu" bundle="gestioneBibliograficaLabels" />
			</html:submit>
		</td>
	</tr>
	<tr>
		<td width="100" class="etichetta"><bean:message key="label.allineam.idLista.Um" bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><html:text property="areaDatiAllineamentoPoloIndiceVO.numListaDaAllineareTitUniformeMus" size="10" readonly="true"></html:text></td>
		<td class="testoNormale">
			<html:submit property="methodRichAllineamenti">
			<bean:message key="button.allineam.idLista.Um" bundle="gestioneBibliograficaLabels" />
			</html:submit>
		</td>
	</tr>

