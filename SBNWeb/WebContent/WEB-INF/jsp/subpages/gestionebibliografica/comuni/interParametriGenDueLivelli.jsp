<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione autore - Area inferiore con i criteri di ricerca
		almaviva2 - Inizio Codifica Agosto 2006
-->
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>

		   <table border="0">
              <tr>
                <td width="80" class="etichetta">
                	<bean:message key="ricerca.elementiBlocco" bundle="gestioneBibliograficaLabels" />
                </td>
                <td width="200" class="testoNormale">
	                <html:text property="interrGener.elemXBlocchi" size="5"></html:text>
                </td>
                <td width="80" class="etichetta">
                	<bean:message key="ricerca.ordinamento" bundle="gestioneBibliograficaLabels" />
                </td>
                <td width="200" class="testoNormale">
                	<html:select property="interrGener.tipoOrdinamSelez" style="width:150px">
					<html:optionsCollection property="interrGener.listaTipiOrdinam" value="codice"
						label="descrizione" />
					</html:select>
				</td>
              </tr>
	      </table>

            <table border="0">
              <tr >
                <td width="80" class="etichetta">
                	<bean:message key="ricerca.livelloRicerca" bundle="gestioneBibliograficaLabels" />
                </td>

                <td width="30" class="etichetta">
                  	<bean:message key="ricerca.locale" bundle="gestioneBibliograficaLabels" />
                </td>
                <td width="30">
                  <html:checkbox property="interrGener.ricLocale"></html:checkbox>
                </td>
                <td width="30" class="etichetta">
                	<bean:message key="ricerca.indice" bundle="gestioneBibliograficaLabels" />
                </td>
                <td width="30">
                  <html:checkbox property="interrGener.ricIndice"></html:checkbox>
                </td>
              </tr>
            </table>

