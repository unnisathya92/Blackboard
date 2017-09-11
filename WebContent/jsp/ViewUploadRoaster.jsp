<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<script>
	function goBack() {
		window.history.back();
	}
</script>
<style>
.column1 {
	width: "110";
}
</style>
<f:view>
	<center>
		<header>
			<h2>f16g324 Online Test Taking System</h2>
		</header>
	</center>
	<center>
		<h3>View/Upload Roster</h3>
	</center>
	<hr>
	<h:form>
	
	Welcome <%=session.getAttribute("firstname")%>


		<div style="text-align: right;"><a href="/Online_Test_Taking_System/faces/doc/UserGuide.pdf"> User
				Guide</a> &nbsp;&nbsp;&nbsp; <a href="AboutUs.jsp">About Us</a>
			&nbsp;&nbsp;
			<a href="TeacherHome.jsp">Home</a> &nbsp;&nbsp;
			<h:commandLink action="#{LoginUserBean.logout}" value="Logout"
				style="text-align: right"></h:commandLink>
		</div>

	</h:form>
	<center>
		<h:form enctype="multipart/form-data">
			<h:messages globalOnly="false" layout="table" errorStyle="color:red" />
			<table>
				<tr height="50">
					<td>Upload Roaster :</td>

					<td><t:inputFileUpload id="file"
							value="#{RoasterBean.uploadedFile}" required="true"
							requiredMessage="Please select a file to continue. " /></td>
				</tr>
				<tr height="50">
					<td>Is Header Available?</td>
					<td><h:selectOneRadio id="header"
							value="#{RoasterBean.header}" required="true"
							requiredMessage="Please Choose if the header is available or not"
							label="Is Header Available?">
							<f:selectItem itemValue="True" />
							<f:selectItem itemValue="False" />
						</h:selectOneRadio></td>
				</tr>
			</table>
			<h:commandButton value="Submit" action="#{RoasterBean.submit}" />


		</h:form>
		<h:form>
			<br>
			<br>
			<hr>
			Roster
				
			<br>


			<t:dataTable value="#{RoasterBean.roasterBeanList}" var="rowNumber"
				rendered="#{RoasterBean.isRosterAvailable}" border="1"
				cellspacing="0" cellpadding="5" width="1200" rowIndexVar="r">
				<h:column>
					<f:facet name="header">
						<h:outputText>Last Name</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.lastName}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>First Name</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.firstName}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>User Name</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.userName}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>Student ID</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.studentID}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>
				Last_Access</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.lastaccess}" />

				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>
				Availability</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.availability}" />

				</h:column>

				<c:forEach
					items="#{RoasterBean.studentScores[0].keySet().toArray()}"
					var="key">
					<h:column headerClass="column1">
						<f:facet name="header">
							<h:outputText value="#{key}" />
						</f:facet>
						<h:inputText value="#{RoasterBean.studentScores[r][key]}" />
					</h:column>
				</c:forEach>
				<h:column>
					<f:facet name="header">
						<h:outputText>Update</h:outputText>
					</f:facet>
					<center>
						<h:commandLink action="#{RoasterBean.update}" value="Update">
							<f:setPropertyActionListener
								target="#{RoasterBean.rowNumberToUpdate}" value="#{r}" />
							<f:setPropertyActionListener target="#{RoasterBean.studentID}"
								value="#{rowNumber.studentID}" />

						</h:commandLink>
					</center>

				</h:column>


			</t:dataTable>

		</h:form>
	</center>

</f:view>