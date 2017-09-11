<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<style>
.preformatted {
	white-space: pre-wrap;
	word-break: break-all;
}

.column1 {
	width: 5%;
}

.column2 {
	width: 95%;
}
</style>

<f:view>
	<center>

		<header>
			<h2>f16g324 Online Test Taking System</h2>
		</header>
	</center>
	<center>
		<h3>FeedBack</h3>
	</center>
	<hr>
	<h:form>
	
	Welcome <%=session.getAttribute("firstname")%>


		<div style="text-align: right">
		<a href="/Online_Test_Taking_System/faces/doc/UserGuide.pdf"> User
				Guide</a> &nbsp;&nbsp;&nbsp; <a href="AboutUs.jsp">About Us</a>
			&nbsp;&nbsp;
			<a href="StudentHome.jsp">Home</a>&nbsp;&nbsp;
			<h:commandLink action="#{LoginUserBean.logout}" value="Logout"
				style="text-align: right"></h:commandLink>
		</div>
	</h:form>

	<br>
	<br>
	<br>
	<br>
	<p>
		<font face="Arial" size="3"> </font>
	</p>

	<h:form>
		<center>
					<h:messages globalOnly="false" layout="table" errorStyle="color:red" />
		
			<t:dataTable value="#{QuestionBean.questionBeanList}" var="rowNumber"
				 border="1" cellspacing="0" cellpadding="5"
				width="1000">
				<h:column>
					<f:facet name="header">
						<h:outputText>QNO</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.qno}" />
				</h:column>
				<h:column headerClass="column2">
					<f:facet name="header">
						<h:outputText>Question</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.questiontext}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>Your Answer</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.givenanswer}"/>
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>Correct Answer</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.correctanswer}"/>
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>Marks Given</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.marksAwarded}"/>
				</h:column>
			</t:dataTable>
			<br>
		</center>
	</h:form>
</f:view>
