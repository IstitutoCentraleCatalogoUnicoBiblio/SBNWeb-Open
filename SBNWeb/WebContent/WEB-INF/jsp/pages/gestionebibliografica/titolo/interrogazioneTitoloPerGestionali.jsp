<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Tipo materiale Moderno/Antico
		almaviva2 - Inizio Codifica Agosto 2006
-->

<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<%@ page
	import="it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.InterrogazioneTitoloPerGestionaliForm"%>

<html:xhtml/>
<layout:page>

	<sbn:navform
		action="/gestionebibliografica/titolo/interrogazioneTitoloPerGestionali.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors /></div>

		<c:choose>
			<c:when
				test="${interrogazioneTitoloPerGestionaliForm.provenienza eq 'NEWLEGAME'}">
				<table border="0">
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.titoloRiferimento"
							bundle="gestioneBibliograficaLabels" />:</td>
						<td width="20" class="testoNormale"><html:text
							property="areaDatiLegameTitoloVO.bidPartenza" size="10"
							readonly="true"></html:text></td>
						<td width="150" class="etichetta"><html:text
							property="areaDatiLegameTitoloVO.descPartenza" size="50"
							readonly="true"></html:text></td>
					</tr>
				</table>
				<hr color="#dde8f0" />
			</c:when>
			<c:when
				test="${interrogazioneTitoloPerGestionaliForm.provenienza eq 'INTERFILTRATA'}">
				<table border="0">
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.titoloRiferimento"
							bundle="gestioneBibliograficaLabels" />:</td>
						<td width="20" class="testoNormale"><html:text
							property="xidDiRicerca" size="10" readonly="true"></html:text></td>
						<td width="150" class="etichetta"><html:text
							property="xidDiRicercaDesc" size="50" readonly="true"></html:text></td>
					</tr>
				</table>
				<hr color="#dde8f0" />
			</c:when>
		</c:choose>




		<c:choose>
			<c:when
				test="${interrogazioneTitoloPerGestionaliForm.tipoProspettazione eq 'LocFonSeg'}">
			    <table width="100%"  border="0" class="SchedaImg1" >
		    		<tr>
				        <td  width="86" class="schedaOff" align="center">
			    			<input type="submit" name="methodInterrogPerGestionaliTit" value="Ricerca titolo per Inventario" class="sintButtonLinkDefault">
				        </td>
			  		   <td  width="86" class="schedaOff" align="center">
			    			<input type="submit" name="methodInterrogPerGestionaliTit" value="Ricerca titolo per Ordine" class="sintButtonLinkDefault">
				        </td>
		     		   <td  width="86" class="schedaOn" align="center">
							<div align="center">Ricerca titolo per Localizzazione/Fondo/Segnatura</div>
				        </td>
					</tr>
			    </table>
				<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitLocFonSeg.jsp" />
			</c:when>
			<c:when
				test="${interrogazioneTitoloPerGestionaliForm.tipoProspettazione eq 'Inventario'}">
				<table width="100%"  border="0" class="SchedaImg1" >
		    		<tr>
				        <td  width="86" class="schedaOn" align="center">
				        	<div align="center">Ricerca titolo per Inventario</div>
				        </td>
			  		   <td  width="86" class="schedaOff" align="center">
			    			<input type="submit" name="methodInterrogPerGestionaliTit" value="Ricerca titolo per Ordine" class="sintButtonLinkDefault">
				        </td>
		     		   <td  width="86" class="schedaOff" align="center">
			     		   	<input type="submit" name="methodInterrogPerGestionaliTit" value="Ricerca titolo per Localizzazione/Fondo/Segnatura" class="sintButtonLinkDefault">
				        </td>
					</tr>
			    </table>
				<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitInventari.jsp" />
			</c:when>
			<c:when
				test="${interrogazioneTitoloPerGestionaliForm.tipoProspettazione eq 'Ordine'}">
				<table width="100%"  border="0" class="SchedaImg1" >
		    		<tr>
				        <td  width="86" class="schedaOff" align="center">
			    			<input type="submit" name="methodInterrogPerGestionaliTit" value="Ricerca titolo per Inventario" class="sintButtonLinkDefault">
				        </td>
						<td  width="86" class="schedaOn" align="center">
				  		   <div align="center">Ricerca titolo per Ordine</div>
				        </td>
		     		   <td  width="86" class="schedaOff" align="center">
		     		   	<input type="submit" name="methodInterrogPerGestionaliTit" value="Ricerca titolo per Localizzazione/Fondo/Segnatura" class="sintButtonLinkDefault">
				        </td>
					</tr>
			    </table>
			    <jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitOrdini.jsp" />

			</c:when>

		</c:choose></div>

		<div id="divFooterCommon">
			<c:choose>
				<c:when	test="${interrogazioneTitoloPerGestionaliForm.tipoProspettazione eq 'LocFonSeg'}">
					<table align="center" border="0">
						<tr>
							<td width="80" class="etichetta"><bean:message
								key="ricerca.elementiBlocco" bundle="gestioneBibliograficaLabels" /></td>
							<td width="100" class="testoNormale"><html:text
								property="interrGener.elemXBlocchi" size="5"></html:text></td>
							<td width="75" class="etichetta"><bean:message
								key="ricerca.ordinamento" bundle="gestioneBibliograficaLabels" /></td>
							<td width="200" class="testoNormale"><html:select
								property="interrGener.tipoOrdinamSelez" style="width:130px">
								<html:optionsCollection property="interrGener.listaTipiOrdinam"
									value="codice" label="descrizione" />
							</html:select></td>
							<td width="80" class="etichetta"><bean:message
								key="ricerca.formatoLista" bundle="gestioneBibliograficaLabels" /></td>
							<td width="150" class="testoNormale"><html:select
								property="interrGener.formatoListaSelez" style="width:70px">
								<html:optionsCollection property="interrGener.listaFormatoLista"
									value="codice" label="descrizione" />
							</html:select></td>
						</tr>
					</table>

				</c:when>
			</c:choose>

			<c:choose>
				<c:when
					test="${interrogazioneTitoloPerGestionaliForm.tipoProspettazione eq 'LocFonSeg'}">
						<table align="center" border="0">
							<tr>
								<td width="85" class="etichetta"><bean:message
									key="ricerca.livelloRicerca" bundle="gestioneBibliograficaLabels" />
								</td>
								<td width="30" class="etichetta"><bean:message key="ricerca.locale"
									bundle="gestioneBibliograficaLabels" /></td>
								<td width="30"><html:checkbox property="interrGener.ricLocale">
								</html:checkbox></td>

								<c:choose>
									<c:when
										test="${interrogazioneTitoloPerGestionaliForm.provenienza ne 'SERVIZI'}">
										<td width="30" class="etichetta"><bean:message
											key="ricerca.indice" bundle="gestioneBibliograficaLabels" /></td>
										<td width="20"><html:checkbox property="interrGener.ricIndice"></html:checkbox>
										</td>
									</c:when>
								</c:choose>
							</tr>
						</table>
				</c:when>
				<c:otherwise>
					<table align="center" border="0">
						<tr>
							<td width="85" class="etichetta"><bean:message
								key="ricerca.livelloRicerca" bundle="gestioneBibliograficaLabels" />
							</td>
							<td width="30" class="etichetta"><bean:message key="ricerca.locale"
								bundle="gestioneBibliograficaLabels" /></td>
							<td width="30"><html:checkbox property="interrGener.ricLocale" disabled="true">
							</html:checkbox></td>
						</tr>
					</table>
				</c:otherwise>
			</c:choose>
		</div>

		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit
					property="methodInterrogPerGestionaliTit">
					<bean:message key="ricerca.button.cerca"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>

<%((InterrogazioneTitoloPerGestionaliForm) session
					.getAttribute("interrogazioneTitoloPerGestionaliForm"))
					.getInterrGener().setRicLocale(false);
			((InterrogazioneTitoloPerGestionaliForm) session
					.getAttribute("interrogazioneTitoloPerGestionaliForm"))
					.getInterrGener().setRicIndice(false);
		%>
