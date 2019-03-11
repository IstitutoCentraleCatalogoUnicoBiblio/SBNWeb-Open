<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Area Cartografia
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


<!-- Modifica almaviva2 05.10.2010 BUG MANTIS punto 1 : modificata in struts l'iniziale definizione in sql nativo
		dei campi relativi alle longitudini e latitudini -->
<!-- Modifica almaviva2 05.10.2010 BUG MANTIS punto 2 : la longitudine Ovest deve essere riportata con la O di Ovest
		e non con la W di West in inglese -->



<table width="100%" border="0" bgcolor="#FFCC99">
	<tr>
		<td>
		<table border="0">
			<tr>
				<td width="130" class="etichetta"><bean:message
					key="ricerca.cartog.meridianoOrig"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:select
					property="interrCartog.meridianoOrigineSelez" style="width:40px">
					<html:optionsCollection
						property="interrCartog.listaMeridianiOrigine" value="codice"
						label="descrizioneCodice" />
				</html:select></td>
			</tr>
		</table>


		<!-- Inizio modifica almaviva2 24.06.2011 - BUG MANTIS collaudo 4386 (adeguamento della mappa di interrogazione per parametri
		di cartografia a quella di creazione (drop per tipologia di latitudine/longitudine invece dei RadioButton) -->


		<table border="0">
		<tr>
			<td width="160"class="etichetta"><bean:message
								key="ricerca.cartog.longitudine"
								bundle="gestioneBibliograficaLabels" /></td>
			<td class="testoNormale">
				<html:select property="interrCartog.longitTipo1" style="width:40px">
					<html:optionsCollection property="interrCartog.listaLongitudine" value="codice" label="descrizioneCodice" />
				</html:select>
			</td>
			<td>
				<html:text property="interrCartog.longInput1gg" size="1" maxlength="3"></html:text>°
				<html:text property="interrCartog.longInput1pp" size="1" maxlength="2"></html:text>'
				<html:text property="interrCartog.longInput1ss" size="1" maxlength="2"></html:text>"
			</td>

			<td>----</td>

			<td class="testoNormale">
				<html:select property="interrCartog.longitTipo2" style="width:40px">
					<html:optionsCollection property="interrCartog.listaLongitudine" value="codice" label="descrizioneCodice" />
				</html:select>
			</td>

			<td>
				<html:text property="interrCartog.longInput2gg" size="1" maxlength="3"></html:text>°
				<html:text property="interrCartog.longInput2pp" size="1" maxlength="2"></html:text>'
				<html:text property="interrCartog.longInput2ss" size="1" maxlength="2"></html:text>"
			</td>
		</tr>

		<tr>
			<td class="etichetta"><bean:message	key="ricerca.cartog.latitudine" bundle="gestioneBibliograficaLabels" /></td>
			<td class="testoNormale">
				<html:select property="interrCartog.latitTipo1"	style="width:40px">
					<html:optionsCollection property="interrCartog.listaLatitudine" value="codice" label="descrizioneCodice" />
				</html:select>
			</td>
			<td>
				<html:text property="interrCartog.latiInput1gg" size="1" maxlength="3"></html:text>°
				<html:text property="interrCartog.latiInput1pp" size="1" maxlength="2"></html:text>'
				<html:text property="interrCartog.latiInput1ss" size="1" maxlength="2"></html:text>"
			</td>
			<td>----</td>
			<td class="testoNormale">
				<html:select property="interrCartog.latitTipo2" style="width:40px">
					<html:optionsCollection property="interrCartog.listaLatitudine" value="codice" label="descrizioneCodice" />
				</html:select>
			</td>
			<td>
				<html:text property="interrCartog.latiInput2gg" size="1" maxlength="3"></html:text>°
				<html:text property="interrCartog.latiInput2pp" size="1" maxlength="2"></html:text>'
				<html:text property="interrCartog.latiInput2ss" size="1" maxlength="2"></html:text>"
			</td>

		</tr>

		</table>

		<!-- Fine modifica almaviva2 24.06.2011 - BUG MANTIS collaudo 4386  -->


		<table border="0">
			<tr>
				<td width="75" class="etichetta"><bean:message
					key="ricerca.cartog.tipoScala" bundle="gestioneBibliograficaLabels" />
				</td>
				<td width="200" class="etichetta"><bean:message
					key="ricerca.cartog.lineare" bundle="gestioneBibliograficaLabels" />
				<html:radio property="interrCartog.tipoScalaRadio" value="lineare" />
				<bean:message key="ricerca.cartog.angolare"
					bundle="gestioneBibliograficaLabels" /> <html:radio
					property="interrCartog.tipoScalaRadio" value="angolare" /> <bean:message
					key="ricerca.cartog.altro" bundle="gestioneBibliograficaLabels" />
				<html:radio property="interrCartog.tipoScalaRadio" value="altro" />
				</td>
				<td width="30" class="etichetta"><bean:message
					key="ricerca.cartog.scala" bundle="gestioneBibliograficaLabels" />
				</td>
				<td class="etichetta"><bean:message
					key="ricerca.cartog.scalaH" bundle="gestioneBibliograficaLabels" />
				<html:text property="interrCartog.scalaH" size="15"></html:text></td>
				<td width="5"></td>
				<td class="etichetta"><bean:message
					key="ricerca.cartog.scalaV" bundle="gestioneBibliograficaLabels" /></td>
				<td class="etichetta">
				<html:text property="interrCartog.scalaV" size="15"></html:text></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
