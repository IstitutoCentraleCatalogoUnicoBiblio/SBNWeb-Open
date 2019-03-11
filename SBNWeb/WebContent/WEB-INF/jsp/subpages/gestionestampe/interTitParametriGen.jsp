<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

		   <table width="100%"  border="0">
              <tr bordercolor="#D4D0C8">
                <td width="20%" class="etichetta"><bean:message key="ricerca.elementiBlocco" bundle="gestioneBibliograficaLabels" /></td>
                <td >
                <html:text styleId="testoNormale" property="interrGener.elemXBlocchi" size="5"></html:text>
                </td>
                <td class="etichetta">
                	<bean:message key="ricerca.ordinamento" bundle="gestioneBibliograficaLabels" />
                	<html:select property="interrGener.tipoOrdinamSelez" style="width:70px">
					<html:optionsCollection property="interrGener.listaTipiOrdinam" value="descrizione"
						label="descrizione" />
					</html:select>
				</td>
                <td class="etichetta">
                   	<bean:message key="ricerca.formatoLista" bundle="gestioneBibliograficaLabels" />
                	<html:select property="interrGener.formatoListaSelez" style="width:70px">
					<html:optionsCollection property="interrGener.listaFormatoLista" value="descrizione"
						label="descrizione" />
					</html:select>
				</td>
              </tr>
	      </table>

            <table width="100%"  border="0">
              <tr>
                <td width="20%" class="etichetta">
                	<bean:message key="ricerca.livelloRicerca" bundle="gestioneBibliograficaLabels" />
                </td>

                <td width="15%" class="etichetta">
                  	<bean:message key="ricerca.locale" bundle="gestioneBibliograficaLabels" />
                </td>
                <td width="5%">
                  <html:checkbox property="interrGener.ricLocale"></html:checkbox>
                </td>
                <td width="31%">
					<html:select property="interrGener.bibliotecaSelez" style="width:70px">
					<html:optionsCollection property="interrGener.listaBiblioteche" value="codice"
						label="descrizioneCodice" />
					</html:select>
                </td>
                <td width="11%" class="etichetta">
                	<bean:message key="ricerca.indice" bundle="gestioneBibliograficaLabels" />
                </td>
                <td width="18%">
                  <html:checkbox property="interrGener.ricIndice"></html:checkbox>
                </td>
              </tr>
            </table>

			<table align="center">
		        <tr>
					<td  align="center">
						<html:submit property="Cerca0">
						<bean:message key="ricerca.button.cerca" bundle="gestioneBibliograficaLabels" />
						</html:submit>
					</td>
		        </tr>
	      </table>
