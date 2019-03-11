<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>

<html:xhtml />
<layout:page>

<sbn:navform action="/acquisizioni/ordini/importaDa.do">

  <div id="divForm">
  	<div id="divMessaggio">
		<div align="center" class="messaggioInfo"><sbn:errors bundle="acquisizioniMessages" /></div>
	</div>
					<br><br>
	<form action="#" method="post" name="sezione" id="sezione">

			<table  align="center" width="100%"  border="0">
              <tr>
                <td>
				<table width="100%"   cellpadding="0" cellspacing="0" border="0" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
	    		   <tr class="etichetta" bgcolor="#dde8f0">
	                      <td colspan="2" class="etichetta" align="center">Importa da </td>
	                  </tr>
		       		<tr class="testoNormale" bgcolor="#FFCC99">
	                    <td width="85%" align="left" class="testoNormale">
							<bean:message  key="ordine.label.documenti" bundle="acquisizioniLabels" />
	                    </td>
	                    <td width="15%">
							<html:radio property="selectedImportaDa" value="documenti" />
	                    </td>
	                </tr>
		       		<tr class="testoNormale" bgcolor="#FEF1E2">
	                    <td width="85%" align="left" class="testoNormale">
							<bean:message  key="ordine.label.suggerimenti" bundle="acquisizioniLabels" />
	                    </td>
	                    <td width="15%">
							<html:radio property="selectedImportaDa" value="suggerimenti" />
	                    </td>
	                </tr>
	                <!--
		       		<tr class="testoNormale" bgcolor="#FFCC99">
	                    <td width="85%" align="left" class="testoNormale">
							<bean:message  key="ordine.label.offerte" bundle="acquisizioniLabels" />
	                    </td>
	                    <td width="15%">
							<html:radio property="selectedImportaDa" value="offerte" />
	                    </td>
	                </tr>
		       		-->
		       		<tr class="testoNormale" bgcolor="#FFCC99">
	                    <td width="85%" align="left" class="testoNormale">
							<bean:message  key="ordine.label.gare" bundle="acquisizioniLabels" />
	                    </td>
	                    <td width="15%">
							<html:radio property="selectedImportaDa" value="gare" />
	                    </td>
	                </tr>
	   	            </table>



	         </td>
        </tr>
      </table>
      <!-- </form> -->
		<c:choose>
			<c:when test="${importaDaForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>
		      	<table align="center" border="0" style="height:40px" cellspacing="0"; cellpadding="0">
		            <tr>
		             <td >
					<html:submit styleClass="pulsanti" property="methodImportaDa">
						<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodImportaDa">
						<bean:message key="ricerca.button.conferma" bundle="acquisizioniLabels" />
					</html:submit>

		             </td>
					 </tr>
		      	  </table>
    		</c:otherwise>
		</c:choose>

	</div>
	</sbn:navform>
</layout:page>
