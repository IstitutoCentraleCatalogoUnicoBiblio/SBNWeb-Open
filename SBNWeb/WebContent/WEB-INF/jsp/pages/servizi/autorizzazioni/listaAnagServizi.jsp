<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/autorizzazioni/ListaAnagServizi">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<br>

		<table width="100%" border="0">
			<tr>
				<td align="center" class="etichetta"><bean:message
					key="servizi.utenti.codicePolo" bundle="serviziLabels" /> <html:text
					styleId="testoNoBold" property="codPolo" disabled="true" size="5"></html:text>
				</td>
				<td align="center" class="etichetta"><bean:message
					key="servizi.utenti.codiceBiblioteca" bundle="serviziLabels" /> <html:text
					styleId="testoNoBold" property="codBib" disabled="true" size="5"></html:text>
				</td>
			</tr>
		</table>
		<br>
		<table class="sintetica">
			<tr>
				<th width="15" class="etichetta"   scope="col"
					bgcolor="#dde8f0"><bean:message
					key="servizi.utenti.bibliotecaUtente" bundle="serviziLabels" /></th>
				<th width="40" class="etichetta"   scope="col"	bgcolor="#dde8f0">
<!--					<bean:message	key="servizi.autorizzazioni.header.tipSer" bundle="serviziLabels" />-->
					<bean:message	key="servizi.erogazione.tipoServizio" bundle="serviziLabels" />
				</th>
				<th width="40" class="etichetta"   scope="col" bgcolor="#dde8f0">
<!--					<bean:message	key="servizi.autorizzazioni.header.codSer" bundle="serviziLabels" />-->
					<bean:message	key="servizi.utenti.titServizio" bundle="serviziLabels" />
				</th>
				<!--
				<th width="15" class="etichetta"   scope="col"
					bgcolor="#dde8f0"><bean:message
					key="servizi.autorizzazioni.header.desSer" bundle="serviziLabels" />
				</th>
				-->
				<th width="5%" class="etichetta" align="center" scope="col"
					bgcolor="#dde8f0"><bean:message
					key="servizi.utenti.headerSelezionataMultipla"
					bundle="serviziLabels" /></th>
			</tr>
			<logic:iterate id="listaAnagServizi" property="servDaAssociare"
				name="ListaAnagServiziForm" indexId="index">
				<sbn:rowcolor var="color" index="index" />
				<tr bgcolor="${color}">
					<td class="testoNoBold"   width="4%"><bean-struts:write
						name="ListaAnagServiziForm"
						property='<%="servDaAssociare[" + index
										+ "].codBiblioteca"%>' />
					</td>
					<td class="testoNoBold"  ><bean-struts:write
						name="ListaAnagServiziForm"
						property='<%="servDaAssociare[" + index
										+ "].componiTipoServizio"%>' /></td>
					<td class="testoNoBold"  ><bean-struts:write
						name="ListaAnagServiziForm"
						property='<%="servDaAssociare[" + index
										+ "].componi"%>' />
					</td>
					<%--
					<td class="testoNoBold"  ><bean-struts:write
						name="ListaAnagServiziForm"
						property='<%="servDaAssociare[" + index
										+ "].desServizio"%>' />
					</td>
					--%>
					<td class="testoNoBold" align="center"><html:checkbox
						styleId="testoNoBold" name="ListaAnagServiziForm"
						property='<%="servDaAssociare[" + index
										+ "].cancella"%>'
						value="C" /></td>
				</tr>
			</logic:iterate>
		</table>
		</div>
		<br>
		<div id="divFooterCommon"></div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodAnagListaSer">
					<bean:message key="servizi.bottone.scegli" bundle="serviziLabels" />
				</html:submit> <html:submit property="methodAnagListaSer">
					<bean:message key="servizi.bottone.annullaOperazione"
						bundle="serviziLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
