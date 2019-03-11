<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<html:xhtml />
<head>
<title>SBN Web</title>

<link rel="stylesheet" href='<c:url value="/styles/sbnweb.css" />'
	type="text/css" />
</head>

<body>
	<div id="data">
		<div>
			<l:present name="POLO_NAME" scope="session">
				<p class="IntestazioneBiblioteca" id="polo">
					<bs:write scope="session" name="POLO_NAME" />
				</p>
			</l:present>
		</div>
		<html:form action="/servicePad.do" enctype="multipart/form-data">
			<div id="divForm">
				<div id="divMessaggio">
					<sbn:errors />
				</div>

				<table width="100%">

					<tr>
						<td width="1%">password:</td>
						<td><html:password property="password" maxlength="255"
								size="30" /></td>
					</tr>

					<tr>
						<td width="1%">sql:</td>
						<td><sbn:disableAll disabled="${servicePadForm.conferma}">
								<html:textarea styleClass="expandedLabel" property="command"
									cols="90" rows="5" style="witdh: 100%;" />
							</sbn:disableAll></td>
					</tr>
					<tr>
						<td colspan="2"><sbn:bottoniera buttons="pulsanti" /></td>
					</tr>
				</table>

				<table class="sintetica">
					<thead style="font-weight: bold;">
						<tr>
							<td>#</td>
							<c:forEach items="${servicePadForm.rows[0]}" var="column">
								<td><c:out value="${column.key}" /></td>
							</c:forEach>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${servicePadForm.rows}" var="columns"
							varStatus="loop">
							<c:set var="idx">${loop.index}</c:set>
							<sbn:rowcolor var="color" index="idx" />
							<tr bgcolor="${color}">
								<td width="1%"><c:out value="${loop.index + 1}" /></td>
								<c:forEach items="${columns}" var="column">
									<td><c:out value="${column.value}" /></td>
								</c:forEach>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

		</html:form>
	</div>
</body>