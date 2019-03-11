<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
<div>
		<div id="divForm">
<sbn:navform action="/amministrazionesistema/nuovaBiblioteca.do">
			<div id="divMessaggio">
					<sbn:errors bundle="amministrazioneSistemaMessages" />
			</div>

<!--table border="1" width="40%" frame="box" rules="none" bgcolor="#FFCA80" align="center" cellspacing="15"-->
<table border="0" width="100%" align="center" cellspacing="15" cellpadding="0">

	<tr>
		<td width="28%">
		</td>
		<td width="22%">
		</td>
		<td width="5%">
		</td>
		<td width="8%">
		</td>
		<td width="10%">
		</td>
		<td width="10%">
		</td>
		<td width="27%">
		</td>
	</tr>
	<c:choose>
		<c:when test="${navForm.abilitato eq 'FALSE'}">
			<tr>
				<td>
					<bean:message key="nuovo.biblioteca.cdbib" bundle="amministrazioneSistemaLabels"/> <font color="#FF4500">**</font> :
				</td>
				<td colspan="5">
					<html:text property="polo" size="10" maxlength="3"></html:text>
					<html:text property="codBib" size="10" maxlength="2"></html:text>
					<font style="font-size: 11px">
						(<bean:message key="nuovo.bibliotecario.limite3.2" bundle="amministrazioneSistemaLabels"/>)
					</font>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="ricerca.biblioteca.cdana" bundle="amministrazioneSistemaLabels"/> <font color="#FF4500">**</font> :
				</td>
				<td colspan="6">
					<html:text property="codAna" size="10" maxlength="6"></html:text>
					<font style="font-size: 11px">
						(<bean:message key="nuovo.bibliotecario.limite6" bundle="amministrazioneSistemaLabels"/>)
					</font>
				</td>
			</tr>
		</c:when>
		<c:otherwise>
			<tr>
				<td>
					<bean:message key="nuovo.biblioteca.cdbib" bundle="amministrazioneSistemaLabels"/> <font color="#FF4500">**</font> :
				</td>
				<td colspan="5">
					<html:text property="polo" size="10" maxlength="3" disabled="true"></html:text>
					<html:text property="codBib" size="10" maxlength="2" disabled="true"></html:text>
					<font style="font-size: 11px">
						(<bean:message key="nuovo.bibliotecario.limite3.2" bundle="amministrazioneSistemaLabels"/>)
					</font>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="ricerca.biblioteca.cdana" bundle="amministrazioneSistemaLabels"/> <font color="#FF4500">**</font> :
				</td>
				<td colspan="6">
					<html:text property="codAna" size="10" maxlength="6" ></html:text>
					<font style="font-size: 11px">
						(<bean:message key="nuovo.bibliotecario.limite6" bundle="amministrazioneSistemaLabels"/>)
					</font>
				</td>
			</tr>
		</c:otherwise>
	</c:choose>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.nome" bundle="amministrazioneSistemaLabels"/> <font color="#FF0000">*</font> :
		</td>
		<td colspan="6">
			<html:text property="nome" size="70" maxlength="80"></html:text>
			<font style="font-size: 11px">
				(<bean:message key="nuovo.bibliotecario.limite80" bundle="amministrazioneSistemaLabels"/>)
			</font>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.unita" bundle="amministrazioneSistemaLabels"/> :
		</td>
		<td colspan="6">
			<html:text property="unitaOrganizzativa" size="70" maxlength="50"></html:text>
			<font style="font-size: 11px">
				(<bean:message key="nuovo.bibliotecario.limite50" bundle="amministrazioneSistemaLabels"/>)
			</font>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.tipo" bundle="amministrazioneSistemaLabels"/> <font color="#FF0000">*</font> :
		</td>
		<td colspan="6">
			<html:select property="selTipoBib">
				<html:optionsCollection property="elencoTipiBib" value="codice" label="descrizione"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.gruppo" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="6">
			<c:choose>
				<c:when test="${navForm.abilitato eq 'TRUE'}">
					<html:select property="selGruppo">
						<html:optionsCollection property="elencoGruppi" value="codice" label="descrizione"/>
					</html:select>
				</c:when>
				<c:otherwise>
					<html:select property="selGruppo" disabled="true">
						<html:optionsCollection property="elencoGruppi" value="codice" label="descrizione"/>
					</html:select>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.indirizzo" bundle="amministrazioneSistemaLabels"/> <font color="#FF0000">*</font> :
		</td>
		<td colspan="6">
			<html:select property="selDug">
				<html:optionsCollection property="elencoDug" value="codice" label="descrizione"/>
			</html:select>
			<html:text property="indirizzo" size="56" maxlength="70"></html:text>
			<font style="font-size: 11px">
				(<bean:message key="nuovo.bibliotecario.limite70" bundle="amministrazioneSistemaLabels"/>)
			</font>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.cap" bundle="amministrazioneSistemaLabels"/> <font color="#FF0000">***</font> :
		</td>
		<td colspan="2">
			<html:text property="cap" size="10" maxlength="5" />
			<font style="font-size: 11px">
				(<bean:message key="nuovo.bibliotecario.limite5" bundle="amministrazioneSistemaLabels"/>)
			</font>
		</td>
		<td>
			<bean:message key="ricerca.biblioteca.localita" bundle="amministrazioneSistemaLabels"/> <font color="#FF0000">*</font> :
		</td>
		<td colspan="5">
			<html:text property="citta" size="38" maxlength="30"></html:text>
			<font style="font-size: 11px">
				(<bean:message key="nuovo.bibliotecario.limite30" bundle="amministrazioneSistemaLabels"/>)
			</font>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.provincia" bundle="amministrazioneSistemaLabels"/> <font color="#FF0000">***</font> :
		</td>
		<td colspan="2">
			<html:select property="selProvincia" style="width:120px">
				<html:optionsCollection property="elencoProvince" value="codice" label="descrizione"/>
			</html:select>
		</td>
		<td>
			<bean:message key="ricerca.biblioteca.paese" bundle="amministrazioneSistemaLabels"/> <font color="#FF0000">*</font> :
		</td>
		<td colspan="2">
			<html:select property="paese" style="width:224px">
				<html:optionsCollection property="elencoPaesi" value="codice" label="descrizione"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.telefono" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="5">
			<html:text property="prefissoTel" size="6" maxlength="7"></html:text> -
			<html:text property="telefono" size="20" maxlength="11"></html:text>
			<font style="font-size: 11px">
				(<bean:message key="nuovo.bibliotecario.limite18" bundle="amministrazioneSistemaLabels"/>)
			</font>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.fax" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="5">
			<html:text property="prefissoFax" size="6" maxlength="7"></html:text> -
			<html:text property="fax" size="20" maxlength="11"></html:text>
			<font style="font-size: 11px">
				(<bean:message key="nuovo.bibliotecario.limite18" bundle="amministrazioneSistemaLabels"/>)
			</font>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.email" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="6">
			<html:text property="email" size="70" maxlength="160"></html:text>
			<font style="font-size: 11px">
				(<bean:message key="nuovo.bibliotecario.limite160" bundle="amministrazioneSistemaLabels"/>)
			</font>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.casella" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="5">
			<html:text property="casPostale" size="40" maxlength="20"></html:text>
			<font style="font-size: 11px">
				(<bean:message key="nuovo.bibliotecario.limite20" bundle="amministrazioneSistemaLabels"/>)
			</font>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.iva" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="5">
			<html:text property="iva" size="40" maxlength="18"></html:text>
			<font style="font-size: 11px">
				(<bean:message key="nuovo.bibliotecario.limite18" bundle="amministrazioneSistemaLabels"/>)
			</font>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.codfis" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="5">
			<html:text property="codFiscale" size="40" maxlength="18"></html:text>
			<font style="font-size: 11px">
				(<bean:message key="nuovo.bibliotecario.limite18" bundle="amministrazioneSistemaLabels"/>)
			</font>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.note" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="5">
			<html:textarea property="note" rows="4" cols="66"></html:textarea>
			<font style="font-size: 11px">
				(<bean:message key="nuovo.bibliotecario.limite160" bundle="amministrazioneSistemaLabels"/>)
			</font>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.flag.sbn" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="5">
			<html:radio property="checkFlag" value="S"></html:radio>
			<bean:message key="ricerca.biblioteca.flag.sbn" bundle="amministrazioneSistemaLabels"/>
			<html:radio property="checkFlag" value="N"></html:radio>
			<bean:message key="ricerca.biblioteca.flag.nonsbn" bundle="amministrazioneSistemaLabels"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.sistema.metro" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="5">
			<html:select property="codSistemaMetro" >
				<html:optionsCollection property="elencoMetro" value="codice" label="descrizione"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<c:choose>
			<c:when test="${navForm.inPolo eq 'TRUE'}">
				<td>
					<bean:message key="ricerca.biblioteca.flag" bundle="amministrazioneSistemaLabels"/>:
				</td>
				<td colspan="5">
					<c:choose>
						<c:when test="${navForm.checkCentro eq 'N'}">
							<b><bean:message key="profilo.biblioteca.button.no" bundle="amministrazioneSistemaLabels"/></b>
						</c:when>
						<c:when test="${navForm.checkCentro eq 'C'}">
							<b><bean:message key="profilo.biblioteca.button.si" bundle="amministrazioneSistemaLabels"/></b>
						</c:when>
						<c:when test="${navForm.checkCentro eq 'A'}">
							<bean:message key="ricerca.biblioteca.flag.affiliataradio" bundle="amministrazioneSistemaLabels"/>:
							<b><c:out value="${navForm.selBibCentroSistema}"></c:out></b>
						</c:when>
					</c:choose>
				</td>
			</c:when>
		</c:choose>
	</tr>

	<sbn:checkAttivita idControllo="BIBLIOTECA_ILL">
		<tr>
			<td colspan="6">
				<h3>
					<bean:message
						key="servizi.configurazione.parametriBiblioteca.ill.servizi"
						bundle="serviziLabels" />
				</h3>
			</td>
		</tr>
		<tr>
			<td><bean:message
					key="servizi.configurazione.parametriBiblioteca.ill.serviziAttivi"
					bundle="serviziLabels" /></td>
			<td colspan="5">
				<html:checkbox property="abilitataILL" styleId="check_servizi_ill"/>
				<html:hidden property="abilitataILL" value="false" />
			</td>
		</tr>
		<tbody id="servizi_ill">
		<tr>
			<td>
				Ruolo biblioteca
			</td>
			<td colspan="6">
				<html:select property="biblioteca.bibliotecaILL.ruolo">
					<html:optionsCollection property="listaRuoloILL" value="codice" label="descrizione"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td>
				Prestito
			</td>
			<td colspan="2">
				<html:select property="biblioteca.bibliotecaILL.tipoPrestito">
					<html:optionsCollection property="listaAdesioneILL" value="codice" label="descrizione"/>
				</html:select>
			</td>
			<td>
				Riproduzione (document-delivery)
			</td>
			<td colspan="2">
				<html:select property="biblioteca.bibliotecaILL.tipoDocDelivery">
					<html:optionsCollection property="listaAdesioneILL" value="codice" label="descrizione"/>
				</html:select>
			</td>
		</tr>
		</tbody>
	</sbn:checkAttivita>
</table>

<hr>

<table border="0" width="100%" align="center" cellspacing="15">
	<tr>
		<td colspan="3" style="font-size: 11px">
			<i><bean:message key="nuovo.bibliotecario.obbligo" bundle="amministrazioneSistemaLabels"/></i>.
		</td>
	</tr>
	<tr>
		<td colspan="3" style="font-size: 11px">
			<i><bean:message key="nuovo.biblioteca.obbligo" bundle="amministrazioneSistemaLabels"/></i>.
		</td>
	</tr>
	<tr>
		<td colspan="3" style="font-size: 11px">
			<i><bean:message key="nuovo.biblioteca.obbligo.italia" bundle="amministrazioneSistemaLabels"/></i>.
		</td>
	</tr>
</table>

<br/>
		<div id="divFooter">
			<table border="0" style="height:40px" align="center">
				<tr>
					<sbn:checkAttivita idControllo="NUOVA_RICHIESTA_SERVIZI_ILL">
						<td>
							<html:submit styleClass="pulsanti" property="methodNewBiblioteca" disabled="false">
								<bean:message key="ricerca.biblioteca.button.scegli" bundle="amministrazioneSistemaLabels" />
							</html:submit>
						</td>
					</sbn:checkAttivita>
					<c:choose>
						<c:when test="${navForm.abilitatoNuovo eq 'TRUE'}">
							<td>
								<html:submit styleClass="pulsanti" property="methodNewBiblioteca">
									<bean:message key="nuovo.biblioteca.salva" bundle="amministrazioneSistemaLabels" />
								</html:submit>
							</td>
						</c:when>
					</c:choose>
					<td>
						<html:submit styleClass="pulsanti" property="methodNewBiblioteca">
							<bean:message key="nuovo.biblioteca.annulla" bundle="amministrazioneSistemaLabels" />
						</html:submit>
					</td>
					<sbn:checkAttivita idControllo="PROFILO">
						<td>
							<html:submit styleClass="pulsanti" property="methodNewBiblioteca">
								<bean:message key="nuovo.biblioteca.abilitazioni" bundle="amministrazioneSistemaLabels" />
							</html:submit>
						</td>
					</sbn:checkAttivita>
				</tr>
			</table>
		</div>

    </sbn:navform>
  </layout:page>