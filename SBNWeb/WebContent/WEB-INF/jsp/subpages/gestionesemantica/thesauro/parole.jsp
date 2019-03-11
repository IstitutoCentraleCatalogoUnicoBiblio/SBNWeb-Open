<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>


			<tr>
				<td class="etichetta"><bean:message
					key="gestionesemantica.thesauro.parole"
					bundle="gestioneSemanticaLabels" /></td>
				<td colspan="3"><html:text
					property="ricercaComune.ricercaThesauro.parola0"
					styleId="testoNormale" size="50"></html:text></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><html:text
					property="ricercaComune.ricercaThesauro.parola1"
					styleId="testoNormale" size="50"></html:text></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><html:text
					property="ricercaComune.ricercaThesauro.parola2"
					styleId="testoNormale" size="50"></html:text></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><html:text
					property="ricercaComune.ricercaThesauro.parola3"
					styleId="testoNormale" size="50"></html:text></td>
			</tr>


