<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<%@ attribute name="name" required="true"%>
<%@ attribute name="folders" required="true"%>
<%@ attribute name="parameter" required="true"%>
<%@ attribute name="bundle" required="true"%>
<%@ attribute name="property" required="true"%>

<c:set var="currentFolder">
	<bs:write name="${name}" property="${property}" />
</c:set>
<bean:size id="size" name="${name}" property="${folders}" />
<c:set var="width">${100 / size}</c:set>

<div class="folders">
	<l:iterate id="folder" name="${name}" property="${folders}" indexId="idx">
		<div style="width:${width}%; float:left;">
			<c:choose>
				<c:when test="${idx eq currentFolder}">
					<div class="schedaOn" style="text-align: center;">
						<bean:message key="${folder}" bundle="${bundle}" />
					</div>
				</c:when>
				<c:otherwise>
					<div class="schedaOff" align="center">
						<html:submit style="margin-left:auto; margin-right:auto;" property="${parameter}" styleClass="sintButtonLinkDefault">
							<bean:message key="${folder}" bundle="${bundle}" />
						</html:submit>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	
	</l:iterate>
</div>
