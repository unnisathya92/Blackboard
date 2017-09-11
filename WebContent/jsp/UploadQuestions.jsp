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
		<h3>View/Upload Questions</h3>
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
					<td>Upload Test :</td>

					<td><t:inputFileUpload id="file"
							value="#{QuestionBean.uploadedFile}" required="true"
							requiredMessage="Please select a file to continue. " /></td>
				</tr>
				<tr height="50">
					<td>Is Header Available?</td>
					<td><h:selectOneRadio id="header"
							value="#{QuestionBean.header}" required="true"
							requiredMessage="Please Choose if the header is available or not"
							label="Is Header Available?">
							<f:selectItem itemValue="True" />
							<f:selectItem itemValue="False" />
						</h:selectOneRadio></td>
				</tr>

			</table>
			<h:commandButton value="Submit"
				action="#{QuestionBean.submitQuestions}" />


		</h:form>
		<h:form>
			<br>
			<br>
			<hr>
	View Questions for 	<h:selectOneMenu value="#{QuestionBean.testNumber}">
				<f:selectItems value="#{QuestionBean.testList}" var="c"
					itemLabel="Test #{c.testNumber}" itemValue="#{c.testNumber}" />
			</h:selectOneMenu>
			<h:commandButton value="Submit"
				action="#{QuestionBean.retreiveQuestions}" />

			<br>
			<t:dataTable value="#{QuestionBean.questionBeanList}" var="rowNumber"
				rendered="#{QuestionBean.isQuestionsRendered}" border="1"
				cellspacing="0" cellpadding="5" width="1200">
				<h:column>
					<f:facet name="header">
						<h:outputText>Question Number</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.qno}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>Question Type</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.questiontype}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>Question</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.questiontext}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>Answer</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.correctanswer}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>
				Tolerance</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.tolerance}" />

				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>
				Weightage</h:outputText>
					</f:facet>
					<h:inputText value="#{rowNumber.weightage}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>Update</h:outputText>
					</f:facet>
					<center>
						<h:commandLink action="#{QuestionBean.update}"
							value="Update Weightage">
							<f:setPropertyActionListener target="#{QuestionBean.q}"
								value="#{rowNumber}" />
						</h:commandLink>
					</center>

				</h:column>
			</t:dataTable>

		</h:form>
	</center>

</f:view>
