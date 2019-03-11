<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<table width="77%">

	<!--
	Intervento del 13.02.2013 almaviva2 Modifiche durante scrittura Manuale ICCU
	. eliminare il check stampaTitNonPoss; si possono stampare solo i posseduti

	<tr>
		<td width="30%">
		<div align="left"><label for="stampaTitNonPoss"><bean:message
			key="schede.label.stampaTitNonPoss" bundle="gestioneStampeLabels" /> </label></div>
		</td>
		<td  colspan="5">
			<html:checkbox styleId="chkStampaTitNonPoss" property="chkStampaTitNonPoss" />
			<html:hidden property="chkStampaTitNonPoss" value="false" />
			</td>
	</tr>
	-->



	<tr>
		<th  colspan="6"><div align="left"><bean:message key="schede.label.cataloghiAttivi"
			bundle="gestioneStampeLabels" /></div>
		</th>
	</tr>
	<tr>
		<td>
		<div align="right"><label for="catAttAutori"><bean:message
			key="schede.label.catAttAutori" bundle="gestioneStampeLabels" /> </label></div>
		</td>
		<td width="3%">
		<div align="left">
			<html:checkbox styleId="chkCatAttAutori" property="chkCatAttAutori" />
			<html:hidden property="chkCatAttAutori" value="false" />
			</div>
		</td>
		<td colspan="4">


		<!--
		Intervento del 17.04.2012 almaviva2 telefonata con Carla Scognamiglio con accordo verbale:
		. eliminare la selezione su quali autori stampare
		. obbligatorietà dalla presenza della collocazione dulla scheda
		. eliminazione della scelta sul numero di copie da stampare;
		-->

		<div align="left"><html:select styleClass="testoNormale" property="tipoAutore">
			<html:optionsCollection property="listaTipoAutore" value="codice" label="descrizione" />
		</html:select></div>



		</td>
	</tr>
	<tr>
		<td>
		<div align="right"><label for="catAttTopografico"><bean:message
			key="schede.label.catAttTopografico" bundle="gestioneStampeLabels" /> </label></div>
		</td>
		<td  width="3%">
		<div align="left">
			<html:checkbox styleId="chkCatAttTopografico" property="chkCatAttTopografico" />
			<html:hidden property="chkCatAttTopografico" value="false" />
			</div>
		</td>
		<td>
		<div align="right"><label for="catAttSoggetti"><bean:message
			key="schede.label.catAttSoggetti" bundle="gestioneStampeLabels" /> </label></div>
		</td>
		<td  width="3%">
		<div align="left">
			<html:checkbox styleId="chkCatAttSoggetti" property="chkCatAttSoggetti" />
			<html:hidden property="chkCatAttSoggetti" value="false" />
		</div>
		</td>
		<td>
		<div align="right"><label for="catAttTitoli"><bean:message
			key="schede.label.catAttTitoli" bundle="gestioneStampeLabels" /> </label></div>
		</td>
		<td  width="3%">
		<div align="left">
			<html:checkbox styleId="chkCatAttTitoli" property="chkCatAttTitoli" />
			<html:hidden property="chkCatAttTitoli" value="false" />
			</div>
		</td>
	</tr>
	<tr>
		<td>
		<div align="right"><label for="catAttClassificazioni"><bean:message
			key="schede.label.catAttClassificazioni" bundle="gestioneStampeLabels" /> </label></div>
		</td>
		<td>
		<div align="left">
			<html:checkbox styleId="chkCatAttClassificazioni" property="chkCatAttClassificazioni" />
			<html:hidden property="chkCatAttClassificazioni" value="false" />
			</div>
		</td>
		<td>
		<div align="right"><label for="catAttEditori"><bean:message
			key="schede.label.catAttEditori" bundle="gestioneStampeLabels" /> </label></div>
		</td>
		<td>
		<div align="left">
			<html:checkbox styleId="chkCatAttEditori" property="chkCatAttEditori" />
			<html:hidden property="chkCatAttEditori" value="false" />
			</div>
		</td>
		<!--
	Intervento del 09.05.2013 almaviva2 concordata con Carla Scognamiglio con accordo verbale:
	. eliminare la selezione per possessori per la stampa schede
		<td>
		<div align="right"><label for="catAttPossessori"><bean:message
			key="schede.label.catAttPossessori" bundle="gestioneStampeLabels" /> </label></div>
		</td>
		<td>
		<div align="left">
			<html:checkbox styleId="chkCatAttPossessori" property="chkCatAttPossessori" />
			<html:hidden property="chkCatAttPossessori" value="false" />
			</div>
		</td>
		-->
	</tr>
</table>




<!--
	Intervento del 17.04.2012 almaviva2 telefonata con Carla Scognamiglio con accordo verbale:
	. eliminare la selezione su quali autori stampare
	. obbligatorietà dalla presenza della collocazione dulla scheda
	. eliminazione della scelta sul numero di copie da stampare;


	<table width="100%">
		<tr>
			<th align="left" colspan = "8">
			<div><bean:message key="schede.label.collScheda" bundle="gestioneStampeLabels" /></div>
			</th>
		</tr>
		<tr>
			<td width="23%">
			<div align="right"><label for="collSchPrincipale"><bean:message
				key="schede.label.collSchPrincipale" bundle="gestioneStampeLabels" /> </label></div>
			</td>
			<td width="3%">
			<div align="left">
				<html:checkbox styleId="chkCollSchPrincipale" property="chkCollSchPrincipale" /></div>
			</td>
			<td width="13%">
			<div align="right"><bean:message key="schede.label.nCopie"	bundle="gestioneStampeLabels" /></div>
			</td>
			<td width="13%">
			<div align="left">
				<html:text styleId="testoNormale"	property="numCollSchPrincipale" size="10" maxlength="10"></html:text>
			</div>
			</td>
			<td width="23%">
			<div align="right"><label for="collSchTopografico"><bean:message
				key="schede.label.collSchTopografico" bundle="gestioneStampeLabels" /> </label></div>
			</td>
			<td width="3%">
			<div align="left">
				<html:checkbox styleId="chkCollSchTopografico"	property="chkCollSchTopografico" />
				</div>
			</td>
			<td width="11%">
			<div align="right"><bean:message key="schede.label.nCopie"	bundle="gestioneStampeLabels" /></div>
			</td>
			<td width="11%">
			<div align="left"><html:text styleId="testoNormale"	property="numCollSchTopografico" size="10" maxlength="10"></html:text></div>
			</td>
		</tr>
		<tr>
			<td>
			<div align="right"><label for="collSchSoggetti"><bean:message key="schede.label.collSchSoggetti" bundle="gestioneStampeLabels" /> </label></div>
			</td>
			<td>
			<div align="left">
				<html:checkbox styleId="chkCollSchSoggetti" property="chkCollSchSoggetti" /></div>
			</td>
			<td>
			<div align="right"><bean:message key="schede.label.nCopie" bundle="gestioneStampeLabels" /></div>
			</td>
			<td>
			<div align="left"><html:text styleId="testoNormale" property="numCollSchSoggetti" size="10" maxlength="10"></html:text></div>
			</td>
			<td>
			<div align="right"><label for="collSchTitoli"><bean:message key="schede.label.collSchTitoli" bundle="gestioneStampeLabels" /> </label></div>
			</td>
			<td>
			<div align="left"><html:checkbox styleId="chkCollSchTitoli" property="chkCollSchTitoli" /></div>
			</td>
			<td>
			<div align="right"><bean:message key="schede.label.nCopie"	bundle="gestioneStampeLabels" /></div>
			</td>
			<td>
			<div align="left"><html:text styleId="testoNormale"	property="numCollSchTitoli" size="10" maxlength="10"></html:text></div>
			</td>
		</tr>
		<tr>
			<td>
			<div align="right"><label for="collSchClassificazioni"><bean:message key="schede.label.collSchClassificazioni" bundle="gestioneStampeLabels" /> </label></div>
			</td>
			<td>
			<div align="left"><html:checkbox styleId="chkCollSchClassificazioni" property="chkCollSchClassificazioni" /></div>
			</td>
			<td>
			<div align=right><bean:message key="schede.label.nCopie" bundle="gestioneStampeLabels" /></div>
			</td>
			<td>
			<div align="left"><html:text styleId="testoNormale" property="numCollSchClassificazioni" size="10" maxlength="10"></html:text></div>
			</td>
			<td>
			<div align="right"><label for="collSchRichiami"><bean:message key="schede.label.collSchRichiami" bundle="gestioneStampeLabels" /> </label></div>
			</td>
			<td>
			<div align="left"><html:checkbox styleId="chkCollSchRichiami" property="chkCollSchRichiami" /></div>
			</td>
			<td>
			<div align="right"><bean:message key="schede.label.nCopie" bundle="gestioneStampeLabels" /></div>
			</td>
			<td>
			<div align="left"><html:text styleId="testoNormale" property="numCollSchRichiami" size="10" maxlength="10"></html:text></div>
			</td>
		</tr>
		<tr>
			<td>
			<div align="right"><label for="collSchEditori"><bean:message key="schede.label.collSchEditori" bundle="gestioneStampeLabels" /> </label></div>
			</td>
			<td>
			<div align="left"><html:checkbox styleId="chkCollSchEditori" property="chkCollSchEditori" /></div>
			</td>
			<td>
			<div align="right"><bean:message key="schede.label.nCopie" bundle="gestioneStampeLabels" /></div>
			</td>
			<td>
			<div align="left"><html:text styleId="testoNormale" property="numCollSchEditori" size="10" maxlength="10"></html:text></div>
			</td>
			<td>
			<div align="right"><label for="collSchPossessori"><bean:message key="schede.label.collSchPossessori" bundle="gestioneStampeLabels" /> </label></div>
			</td>
			<td>
			<div align="left"><html:checkbox styleId="chkCollSchPossessori" property="chkCollSchPossessori" /></div>
			</td>
			<td>
			<div align="right"><bean:message key="schede.label.nCopie"	bundle="gestioneStampeLabels" /></div>
			</td>
			<td>
			<div align="left"><html:text styleId="testoNormale"	property="numCollSchPossessori" size="10" maxlength="10"></html:text></div>
			</td>
		</tr>
	</table>
-->
