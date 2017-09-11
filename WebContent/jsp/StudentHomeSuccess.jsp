<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<script>

	score =<%=session.getAttribute("score")%>;
alert("Your score for the test is "+score);

</script>
<f:view>
	<center>
		<header>
			<h2>f16g324 Online Test Taking System</h2>
		</header>

		<h3>Student Home</h3>
	</center>
	<hr>
	<h:form>
	
	Welcome <%=session.getAttribute("firstname")%>
		<div style="text-align: right;">
			<a href="/Online_Test_Taking_System/faces/doc/UserGuide.pdf"> User
				Guide</a> &nbsp;&nbsp;&nbsp; <a href="AboutUs.jsp">About Us</a>
			&nbsp;&nbsp;
			<h:commandLink action="#{LoginUserBean.logout}" value="Logout"
				style="text-align: right"></h:commandLink>
		</div>
		<center>
			List of available Tests &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <br>


			<t:dataTable value="#{testBean.testBeanList}" var="rowNumber"
				rendered="true" border="1" cellspacing="0" cellpadding="5"
				width="1000">
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
				Score</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.score}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>
				Action</h:outputText>
					</f:facet>
					<center>
						<h:commandLink action="#{QuestionBean.takeTest}" value="Take Test">
							<f:setPropertyActionListener target="#{QuestionBean.test}"
								value="#{rowNumber.testNumber}" />
								<f:setPropertyActionListener target="#{QuestionBean.isFeedBack}"
								value="No" />
						</h:commandLink>
					</center>
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>
				Feed Back</h:outputText>
					</f:facet>
					<center>
						<h:commandLink action="#{QuestionBean.retreiveQuestions}"
							value="View Feedback">
							<f:setPropertyActionListener target="#{QuestionBean.isFeedBack}"
								value="Yes" />
							<f:setPropertyActionListener target="#{QuestionBean.testNumber}"
								value="#{rowNumber.testNumber}" />
						</h:commandLink>

					</center>
				</h:column>
			</t:dataTable>
		</center>
	</h:form>
</f:view>