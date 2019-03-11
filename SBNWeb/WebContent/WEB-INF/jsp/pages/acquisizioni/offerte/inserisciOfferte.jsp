<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/offerte/inserisciOfferte.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>
<table   width="100%" style="	border-style: dotted; border-width: 1px;">
		     <tr>
                        <th  width="10%" scope="col" class="etichetta"><div align="left">Nome Fornitore</div></th>
                        <th scope="col"><div align="left">
                        <input  class="testoNormale" name="NumFattura" type="text" size="40"  value=""  >
                        </div></th>
             </tr>
		     <tr>
                        <th  width="20%" scope="col" class="etichetta"><div align="left">Tipo Fornitore</div></th>
                        <th scope="col"><div align="left">

                       <select class="testoNormale" name="Biblaff" style="width:100px">
                        <option value="" ></option>
                        <option value="01">B - Biblioteca</option>
                        <option value=" 02">D - Dipartimento</option>
                        <option value=" 03">C - Donatore</option>
                        <option value=" 03">E - Editore commerciale</option>
                        <option value=" 03">G - Editore non commerciale</option>
                        <option value=" 03" >F - Fornitore</option>
                        <option value=" 03">L - Librario</option>
                        <option value=" 03">P - Prefetttura</option>
                        <option value=" 03">T - Tipografo</option>
                        </select>
                        </div></th>

             </tr>
			 <tr>
 						<td  class="etichetta"  scope="col"><div align="left">Provincia</div></td>
                        <td scope="col"><div align="left">
                        <select class="testoNormale" name="Biblaff" style="width:100px">
                        <option value="" selected></option>
                        <option value="">AG - Agrigento</option>
                        <option value=" 02">AL - Alessandria</option>
                        <option value=" 03">CS - Cosenza</option>
                        <option value=" 03">RM - Roma</option>
                        <option value=" 03">SI - Siena</option>
                        </select>
                        </div></td>

 						<td  class="etichetta"  scope="col"><div align="left">Paese</div></td>
                        <td scope="col"><div align="left">
                        <select class="testoNormale" name="Biblaff" style="width:100px">
                        <option value="" ></option>
                        <option value="" 01 >IT - ITALIA</option>
                        <option value=" 02">FR - Francia</option>
                        <option value=" 03">FRE - Francese</option>
                        <option value=" 03">JP - Giappone</option>
                        </select>
                        </div></td>

             </tr>
			 <tr>
 						<td  class="etichetta"  scope="col"><div align="left">Profilo Acquisto</div></td>
                        <td scope="col"><div align="left">
                        <select class="testoNormale" name="Biblaff" style="width:100px">
                        <option value="" selected></option>
                        <option value="">1 - LETTERATURA ITALIANA</option>
                        <option value=" 02">2 - PERIODICI ITALIANI</option>
                        <option value=" 03">3 - LETTERATURA INGLESE</option>
                        <option value=" 03">4 - PERIODICI INGLESI</option>
                        <option value=" 03">6 - STORIA</option>
                        </select>
                        </div></td>

             </tr>

                  </table>


<table width="100%"  border="0" style="height:40px" height="98"  >
              <tr>

                <td align="center"><a href="OfferteRicercaParziale.htm">
                  <input name="Conferma" type="button" class="pulsanti" id="Cerca2" value="Ok" onclick="alert('operazione effettuata correttamente!');" >
                  </a>
                  <input onClick="javascript: history.back();"  name="Chiudi2" type="button" class="pulsanti" id="Chiudi" value="Chiudi">
                  <a href="#">
                  <input name="Insriga" type="button" class="pulsanti" id="Insriga" value="Ins. riga">
                  </a> <a href="#">
                  <input name="Cancriga" type="button" class="pulsanti" id="Cancriga" value="Canc. riga" ></a>
                  <a href="#"><input name="Cancella2" type="button" class="pulsanti" id="Cancella" value="Cancella"></a>
                </td>
			 </tr>
			 <tr>
                <td  align="center">
                 <input name="Annulla" type="submit" class="pulsanti" id="Annulla" value="Annulla">

 			<html:submit styleClass="pulsanti" property="indietro0">
				<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
			</html:submit>



             </td>
             </tr>
      	  </table>

     	  </div>
	</sbn:navform>
</layout:page>
