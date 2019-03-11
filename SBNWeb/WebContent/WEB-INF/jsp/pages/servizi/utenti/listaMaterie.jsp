<%@ taglib uri="http://struts.apache.org/tags-html-el"       prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el"       prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"            prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"  prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn"           prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags"                            prefix="layout"%>

<html:xhtml />

<layout:page>

<sbn:navform action="/servizi/utenti/ListaMaterie.do">
	<div id="divForm">
		<div id="divMessaggio">
			<sbn:errors bundle="serviziMessages" />
		</div>
		<br>


		<div>
			<strong><bean:message key="servizi.utenti.materieDiInteresse" bundle="serviziLabels" /></strong>
		</div>
		<br/>

		<table class="sintetica">
			<tr>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center;">
					<bean:message key="servizi.utenti.headerProgressivo" bundle="serviziLabels" />
				</th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center;">
					<bean:message key="servizi.materieinteresse.header.codMateria" bundle="serviziLabels" />
				</th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center;">
					<bean:message key="servizi.materieinteresse.header.desMateria" bundle="serviziLabels" />
				</th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center;">
					&nbsp;
				</th>
			</tr>
			<logic:iterate	id="listaMat"
							property="listaMaterie"
							name="navForm"
							indexId="index">

				<sbn:rowcolor var="color" index="index" />

				<tr>
				    <td bgcolor="${color}" class="testoNoBold" style="width:10%; text-align:center;">
		        		 <bs:write name="listaMat" property="progressivo" />
		      		</td>
				    <td bgcolor="${color}" class="testoNoBold" style="width:10%; text-align:center;">
		        		 <bs:write name="listaMat" property="codice" />
		      		</td>
				    <td bgcolor="${color}" class="testoNoBold">
		        		 <bs:write name="listaMat" property="descrizione" />
		      		</td>
				    <td bgcolor="${color}" class="testoNoBold" style="width:3%;">
				    	<sbn:disableAll checkAttivita="GESTIONE">
		        			<html:multibox property="indiciSelezionate" value="${index}" style="text-align:center;" />
		        		</sbn:disableAll>
		      		</td>
				</tr>
			</logic:iterate>
		</table>
	</div>
	<br/>

	<div id="divFooter" style="width:100%;">
		<table align="center" border="0">
			<tr>
				<td align="center">
					<sbn:checkAttivita idControllo="GESTIONE">
						<html:submit property="methodListaMaterieUtente">
							<bean:message key="servizi.bottone.conferma" bundle="serviziLabels" />
						</html:submit>
					</sbn:checkAttivita>
					<html:submit property="methodListaMaterieUtente">
						<bean:message key="servizi.bottone.annulla" bundle="serviziLabels" />
					</html:submit>
				</td>
			</tr>
		</table>
	</div>
</sbn:navform>
</layout:page>