<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

	<table width="100%" align="center">
			<tr>
				<td scope="col" align="left">
				<div class="etichetta"><bean:message
					key="etichette.label.ricercaPerCollocazioni"
					bundle="gestioneStampeLabels" /></div>
				</td>
			</tr>
			<tr>
				<td scope="col" align="left">
				<div class="etichetta"><bean:message
					key="etichette.label.sezione" bundle="gestioneStampeLabels" />
				</div>
				</td>
				<td scope="col" align="left">
				<div class="etichetta"><html:text styleId="testoNormale"
					property="sezione" size="10"></html:text> <html:link
					action="/pathDaDefinire/actionDaDefinire.do">
					<img border="0" align="middle" alt="Ricerca formato etichetta"
						src='<c:url value="/images/lente.GIF" />' />
				</html:link></div>
				</td>
			</tr>
			<tr>
				<td scope="col" align="left">
				<div class="etichetta"><bean:message
					key="etichette.label.dallaCollocazione"
					bundle="gestioneStampeLabels" /></div>
				</td>
				<td scope="col" align="left">
				<div class="etichetta"><html:text styleId="testoNormale"
					property="dallaCollocazione" size="10"></html:text> <html:link
					action="/pathDaDefinire/actionDaDefinire.do">
					<img border="0" align="middle" alt="Ricerca formato etichetta"
						src='<c:url value="/images/lente.GIF" />' />
				</html:link></div>
				</td>
				<td scope="col" align="left">
				<div class="etichetta"><bean:message
					key="etichette.label.dallaSpecificazione"
					bundle="gestioneStampeLabels" /></div>
				</td>
				<td scope="col" align="left">
				<div class="etichetta"><html:text styleId="testoNormale"
					property="dallaSpecificazione" size="10"></html:text> <html:link
					action="/pathDaDefinire/actionDaDefinire.do">
					<img border="0" align="middle" alt="Ricerca formato etichetta"
						src='<c:url value="/images/lente.GIF" />' />
				</html:link></div>
				</td>
			</tr>
			<tr>
				<td scope="col" align="left">
				<div class="etichetta"><bean:message
					key="etichette.label.allaCollocazione"
					bundle="gestioneStampeLabels" /></div>
				</td>
				<td scope="col" align="left">
				<div class="etichetta"><html:text styleId="testoNormale"
					property="allaCollocazione" size="10"></html:text> <html:link
					action="/pathDaDefinire/actionDaDefinire.do">
					<img border="0" align="middle" alt="Ricerca formato etichetta"
						src='<c:url value="/images/lente.GIF" />' />
				</html:link></div>
				</td>
				<td scope="col" align="left">
				<div class="etichetta"><bean:message
					key="etichette.label.allaSpecificazione"
					bundle="gestioneStampeLabels" /></div>
				</td>
				<td scope="col" align="left">
				<div class="etichetta"><html:text styleId="testoNormale"
					property="allaSpecificazione" size="10"></html:text> <html:link
					action="/pathDaDefinire/actionDaDefinire.do">
					<img border="0" align="middle" alt="Ricerca formato etichetta"
						src='<c:url value="/images/lente.GIF" />' />
				</html:link></div>
				</td>
			</tr>
		</table>