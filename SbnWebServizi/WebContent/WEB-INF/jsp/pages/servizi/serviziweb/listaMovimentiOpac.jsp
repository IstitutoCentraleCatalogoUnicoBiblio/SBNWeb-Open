<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


<html:xhtml />
<layout:page>
	<sbn:navform action="/serviziweb/listaMovimentiOpac.do">

		<div id="divForm">
			<div id="divMessaggio"> <sbn:errors bundle="serviziWebMessages" /></div>
		<br>

		</div>
		<table cellspacing="0" width="100%" border="0">
		<tr>
			<th colspan="4" class="etichetta" align="right">
				<c:out value="${ListaMovimentiOpacForm.biblioSel}"> </c:out>-
				<c:out value="${ListaMovimentiOpacForm.ambiente}"> </c:out>-
				<bean:message key="servizi.utenti.utenteConn" bundle="serviziWebLabels" />
				<c:out value="${ListaMovimentiOpacForm.utenteCon}"> </c:out>
			<hr>
			</th>
		</tr>

		<tr>
		    <th colspan="4" class="etichetta" align="right">
				<em><strong><bean:message key="servizi.web.datiRichiesta"  bundle="serviziWebLabels" /></strong></em>

			</th>
		</tr>

		<tr>
		    <td class="etichetta" align="left">
		    	<bean:message key="servizi.nome.biblioteca.servizio"  bundle="serviziWebLabels" /> :
				<c:out value="${ListaMovimentiOpacForm.codBibInv}"> </c:out><c:out value="${listaMovimentiOpac.autore}"> </c:out>

		  	</td>
		</tr>

		<tr>
	    	<td class="etichetta" align="left">
	    		<bean:message key="servizi.autoreLogin"  bundle="serviziWebLabels" /> :
	         	<c:out value="${ListaMovimentiOpacForm.autore}"> </c:out>
	        </td>
		</tr>

		<tr>
	    	<td class="etichetta" align="left">
	    		<bean:message key="servizi.titoloLogin"  bundle="serviziWebLabels" /> :
	         	<c:out value="${ListaMovimentiOpacForm.titolo}"> </c:out>
	         	<c:out value="${ListaMovimentiOpacForm.anno}"> </c:out>
	        </td>
		</tr>
		<c:if test="${empty ListaMovimentiOpacForm.codSegnatura}">
			<tr>
		    	<td class="etichetta" align="left">
		    		<bean:message key="servizi.inventario"  bundle="serviziWebLabels" /> :
		         	<c:out value="${ListaMovimentiOpacForm.codSerieInv}"> </c:out> /
		         	<c:out value="${ListaMovimentiOpacForm.codInvenInv}"> </c:out>
		        </td>
			</tr>
		</c:if>

		<c:if test="${not empty ListaMovimentiOpacForm.codSegnatura}">
			<tr>
		    	<td class="etichetta" align="left">
		    		<bean:message key="servizi.utente.datiDocumento"  bundle="serviziWebLabels" /> :
		         	<c:out value="${ListaMovimentiOpacForm.codSegnatura}"> </c:out>
		        </td>
			</tr>
		</c:if>
		<c:if test="${not empty ListaMovimentiOpacForm.tipoDoc and (ListaMovimentiOpacForm.tipoDoc eq 'S' or ListaMovimentiOpacForm.tipoDoc eq 's')}">
			<tr>
		    	<td class="etichetta" align="left">
		    		<bean:message key="servizi.erogazione.movimento.annata"  bundle="serviziWebLabels" /> :
		         	<html:text name="ListaMovimentiOpacForm" property="annoPeriodico" maxlength="4" size="5" />
		        </td>
			</tr>
		</c:if>
		<tr>
	       	<td width="60%" align="left">
         		<bean:message key="servizi.servizio.Documento"  bundle="serviziWebLabels" />
				<html:select property="servizioScelto" disabled="${ListaMovimentiOpacForm.conferma}">
					<html:optionsCollection property="lstCodiciServizio" value="idServizio" label="descrTipoServ" />
				</html:select>
				<hr>
			</td>
		</tr>
	<sbn:checkAttivita idControllo="RICHIESTA_LOCALE">
		<tr>
			<td>
			<c:if test="${not ListaMovimentiOpacForm.flgServDisDoc}">
				<img border="0" src='<c:url value="/styles/images/freccia_chiusa.jpg" />'>
				<html:link page="/serviziweb/listaMovimentiOpac.do?param=true">Servizi disponibili per il documento</html:link>
			</c:if>
			<c:if test="${ListaMovimentiOpacForm.flgServDisDoc}">
				<img border="0" src='<c:url value="/styles/images/freccia_aperta.jpg" />'>
				<html:link page="/serviziweb/listaMovimentiOpac.do?param=false">Servizi disponibili per il documento</html:link>
			</c:if>


			</td>
			<c:if test="${ListaMovimentiOpacForm.flgServDisDoc}">
				<logic:iterate id="item" property="lstCodiciServizioDoc" name="ListaMovimentiOpacForm">
					<tr>
						<td><c:out value="${item.descrTipoServ}"> </c:out> </td>
					</tr>
				</logic:iterate>

			</c:if>
		</tr>
	</sbn:checkAttivita>
		</table>
		<hr>
		<html:submit styleClass="submit" property="paramSelMovOpac">

			<bean:message key="servizi.bottone.indietro" bundle="serviziWebLabels" />
		</html:submit>
		<c:if test="${not ListaMovimentiOpacForm.flg}">
			<html:submit styleClass="submit" property="paramSelMovOpac">
				<bean:message key="button.avanti" bundle="serviziWebLabels" />
			</html:submit>
		</c:if>

	</sbn:navform>
</layout:page>
