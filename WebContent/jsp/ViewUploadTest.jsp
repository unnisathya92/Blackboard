<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<script>
	function goBack() {
		window.history.back();
	}
</script>
<f:view>
	<center>
		<header>
			<h2>f16g324 Online Test Taking System</h2>
		</header>
	</center>
	<center>
		<h3>View Test</h3>
	</center>
	<hr>
	<h:form>
	
	Welcome <%=session.getAttribute("firstname")%>


		<div style="text-align: right;">
			<a href="/Online_Test_Taking_System/faces/doc/UserGuide.pdf">
				User Guide</a> &nbsp;&nbsp;&nbsp; <a href="AboutUs.jsp">About Us</a>
			&nbsp;&nbsp; <a href="TeacherHome.jsp">Home</a>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<h:commandLink action="#{LoginUserBean.logout}" value="Logout"
				style="text-align: right"></h:commandLink>
		</div>

	</h:form>
	<center>

		<h:form>
			<br>
			<br>
			<hr>
			Test
			
			<br>
			<t:dataTable value="#{TestBean.testBeanList}" var="rowNumber"
				rendered="true" border="1" cellspacing="0" cellpadding="5"
				width="1200">
				<h:column>
					<f:facet name="header">
						<h:outputText>Test Number</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.testNumber}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>CRN #</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.crn}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>Course Code</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.courseCode}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>Start Date</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.startDate}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>
				End Date</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.endDate}" />

				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>
				Duration</h:outputText>
					</f:facet>
					<h:inputText value="#{rowNumber.duration}" />

				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>
				Total Marks</h:outputText>
					</f:facet>
					<center>
						<h:outputText value="#{rowNumber.total}" />
					</center>
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>Update</h:outputText>
					</f:facet>
					<center>
						<h:commandLink action="#{TestBean.update}" value="Update Duration">
							<f:setPropertyActionListener target="#{TestBean.t}"
								value="#{rowNumber}" />
						</h:commandLink>
					</center>

				</h:column>
			</t:dataTable>

		</h:form>
	</center>

</f:view>