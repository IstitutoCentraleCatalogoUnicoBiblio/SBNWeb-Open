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


<table width="100%" border="0" bgcolor="#FFCC99">
	<tr>
		<td>
		<table width="100%" border="0">
			<tr>

			<!-- Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
 			   12) Il campo tipo testo letterario (105 o 140) dovrebbe evidenziarsi soltanto in presenza di tipo reord a/b
				<td width="130" class="etichetta"><bean:message
					key="ricerca.musica.tipoTestoLetter"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:select
					property="interrMusic.tipoTestoLetterarioSelez" style="width:40px">
					<html:optionsCollection
						property="interrMusic.listaTipoTestoLetterario" value="codice"
						label="descrizioneCodice" />
				</html:select></td>
			-->
			</tr>
			<tr>
				<td width="130" class="etichetta"><bean:message
					key="ricerca.musica.elaborazione"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:select
					property="interrMusic.elaborazioneSelez" style="width:40px">
					<html:optionsCollection property="interrMusic.listaElaborazioni"
						value="codice" label="descrizioneCodice" />
				</html:select></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message key="ricerca.musica.orgSint"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><input type="text"
					name="interrMusic.organicoSintet" size="85" maxlength="80"
					id="testoNormale"> <sbn:tastiera limit="80" property="interrMusic.organicoSintet" name="interrogazioneTitoloForm"></sbn:tastiera></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message key="ricerca.musica.orgAnal"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><input type="text"
					name="interrMusic.organicoAnalit" size="85" maxlength="80" value=""
					id="testoNormale"> <sbn:tastiera limit="80" property="interrMusic.organicoAnalit" name="interrogazioneTitoloForm"></sbn:tastiera></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message
					key="ricerca.musica.presentazione"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:select
					property="interrMusic.presentazioneSelez" style="width:40px">
					<html:optionsCollection property="interrMusic.listaPresentazione"
						value="codice" label="descrizioneCodice" />
				</html:select></td>
			</tr>
		</table>

		<table width="100%" border="0">
			<tr>
				<td width="130" class="etichetta"><bean:message
					key="ricerca.musica.formaMusicale"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="70" class="testoNormale"><html:select
					property="interrMusic.formaMusicaleSelez1" style="width:40px">
					<html:optionsCollection property="interrMusic.listaFormeMusicali"
						value="codice" label="descrizioneCodice" />
				</html:select></td>
				<td width="70" class="testoNormale"><html:select
					property="interrMusic.formaMusicaleSelez2" style="width:40px">
					<html:optionsCollection property="interrMusic.listaFormeMusicali"
						value="codice" label="descrizioneCodice" />
				</html:select></td>
				<td width="70" class="testoNormale"><html:select
					property="interrMusic.formaMusicaleSelez3" style="width:40px">
					<html:optionsCollection property="interrMusic.listaFormeMusicali"
						value="codice" label="descrizioneCodice" />
				</html:select></td>
				<td width="40" class="etichetta"><bean:message
					key="ricerca.musica.tonalita" bundle="gestioneBibliograficaLabels" />
				</td>
				<td class="testoNormale"><html:select
					property="interrMusic.tonalitaSelez" style="width:40px">
					<html:optionsCollection property="interrMusic.listaTonalita"
						value="codice" label="descrizioneCodice" />
				</html:select></td>
			</tr>
		</table>

		<table width="100%" border="0">
			<tr>
				<td width="130" class="etichetta"><bean:message
					key="ricerca.musica.numOpera" bundle="gestioneBibliograficaLabels" />
				</td>
				<td class="testoNormale"><input type="text"
					name="interrMusic.numOpera" size="85" maxlength="20" id="testoNormale"> <sbn:tastiera limit="85" property="interrMusic.numOpera" name="interrogazioneTitoloForm"></sbn:tastiera></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message key="ricerca.musica.numOrdine"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><input type="text"
					name="interrMusic.numOrdine" size="85" maxlength="20" value="" id="testoNormale">
				<sbn:tastiera limit="85" property="interrMusic.numOrdine" name="interrogazioneTitoloForm"></sbn:tastiera></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message key="ricerca.musica.numCatTem"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><input type="text"
					name="interrMusic.numCatalogoTem" size="85" maxlength="20" value=""
					id="testoNormale"><sbn:tastiera limit="85" property="interrMusic.numCatalogoTem" name="interrogazioneTitoloForm"></sbn:tastiera></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="130" class="etichetta"><bean:message
					key="ricerca.musica.dataComp" bundle="gestioneBibliograficaLabels" />
				</td>

				<td width="45" class="etichetta"><bean:message key="ricerca.inizio"
					bundle="gestioneBibliograficaLabels" /> <bean:message
					key="ricerca.da" bundle="gestioneBibliograficaLabels" /></td>


				<td width="30" class="testoNormale"><html:text
					property="interrMusic.dataCompInizioDa" size="4"></html:text></td>
				<td width="10" class="etichetta"><bean:message key="ricerca.a"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="55" class="testoNormale"><html:text
					property="interrMusic.dataCompInizioA" size="4"></html:text></td>

				<td width="45" class="etichetta"><bean:message key="ricerca.fine"
					bundle="gestioneBibliograficaLabels" /> <bean:message
					key="ricerca.da" bundle="gestioneBibliograficaLabels" /></td>
				<td width="30" class="testoNormale"><html:text
					property="interrMusic.dataCompFineDa" size="4"></html:text></td>
				<td width="10" class="etichetta"><bean:message key="ricerca.a"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="30" class="testoNormale"><html:text
					property="interrMusic.dataCompFineA" size="4"></html:text></td>
			</tr>
		</table>

		<table width="100%" border="0">
			<tr>
				<td width="130" class="etichetta"><bean:message
					key="ricerca.musica.orgSintComp"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><input type="text"
					name="interrMusic.organicoSintetCompl" size="85" maxlength="80" value=""
					id="testoNormale"> <sbn:tastiera limit="85" property="interrMusic.organicoSintetCompl" name="interrogazioneTitoloForm"></sbn:tastiera></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message key="ricerca.musica.orgAnalComp"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><input type="text"
					name="interrMusic.organicoAnalitCompl" size="85" maxlength="80" value=""
					id="testoNormale"> <sbn:tastiera limit="85" property="interrMusic.organicoAnalitCompl" name="interrogazioneTitoloForm"></sbn:tastiera></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message key="ricerca.musica.titOrdinam"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><input type="text"
					name="interrMusic.titoloOrdinamento" size="85" maxlength="50" value=""
					id="testoNormale"> <sbn:tastiera limit="85" property="interrMusic.titoloOrdinamento" name="interrogazioneTitoloForm"></sbn:tastiera></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message key="ricerca.musica.titEstratto"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><input type="text"
					name="interrMusic.titoloEstratto" size="85" maxlength="50" value=""
					id="testoNormale"> <sbn:tastiera limit="85" property="interrMusic.titoloEstratto" name="interrogazioneTitoloForm"></sbn:tastiera></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message key="ricerca.musica.appellativo"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><input type="text"
					name="interrMusic.appellativo" size="85" maxlength="50" value="" id="testoNormale">
				<sbn:tastiera limit="85" property="interrMusic.appellativo" name="interrogazioneTitoloForm"></sbn:tastiera></td>
			</tr>
		</table>
</table>
