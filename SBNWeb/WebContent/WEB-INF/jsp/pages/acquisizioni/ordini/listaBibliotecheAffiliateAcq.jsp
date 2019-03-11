<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>

	<sbn:navform action="/acquisizioni/ordini/listaBibliotecheAffiliateAcq.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
			<br>
			   <div class="testoNormale">
					<bean:message  key="ricerca.label.intSezList" bundle="acquisizioniLabels" />
			   </div>
			<br>
		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodListaBibliotecheAffiliateAcq"	 ></sbn:blocchi>

			<table width="100%"  border="0">
              <tr>
                <td>
				<table width="100%"   cellpadding="0" cellspacing="0" border="0" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">

                    <tr class="etichetta" bgcolor="#dde8f0">
                      <td width="25%" class="etichetta" align="center">Cod. Biblioteca</td>
                      <td width="50%"class="etichetta" align="center">Descrizione</td>
                      <td width="15%">&nbsp;</td>
                    </tr>
					<logic:iterate id="prova" property="listaBiblAff" name="listaBibliotecheAffiliateAcqForm">
					   <c:set var="color" >
							<c:choose>
						        <c:when test='${color == "#FFCC99"}'>
						            #FEF1E2
						        </c:when>
						        <c:otherwise>
									#FFCC99
						        </c:otherwise>
						    </c:choose>
					    </c:set>
				 	  	<tr class="testoNormale" bgcolor="${color}">
		                    <td class="testoNormale" align="center">
								<bean-struts:write name="prova" property="codice" />
							</td>
		                    <td class="testoNormale" align="left">
								<bean-struts:write name="prova" property="descrizione" />
		                    </td>
		                    <td>
							<html:multibox property="selectedBiblAff">
								<bean-struts:write name="prova" property="codice" />
							</html:multibox>
		                    </td>
	                    </tr>
					</logic:iterate>
				</table>
			</table>
  </div>

<div id="divFooterCommon">
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodListaBibliotecheAffiliateAcq" bottom="true" ></sbn:blocchi>
		</div>
 <div id="divFooter">

  <div id="divFooter">

	           <table align="center"  border="0" style="height:40px" >
                <tr >
                     <td valign="top" align="center">
					<html:submit styleClass="pulsanti" property="methodListaBibliotecheAffiliateAcq">
						<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
					</html:submit>
		 			<html:submit styleClass="pulsanti" property="methodListaBibliotecheAffiliateAcq">
						<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
					</html:submit>
                     </td>
                  </tr>
                </table>
  </div>
	</sbn:navform>
</layout:page>
