<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns:t="http://myfaces.apache.org/tomahawk">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Database Access Menu</title>
</head>
<body>
	<center>
		<h2>Database Access Menu</h2>
		<br>
	</center>
	<f:view>
		<h:form>
					<div style="text-align: right">
			<a href="/Online_Test_Taking_System/faces/doc/UserGuide.pdf"> User
				Guide</a> &nbsp;&nbsp;&nbsp; <a href="AboutUs.jsp">About Us</a>
			&nbsp;&nbsp;&nbsp;
			<h:commandLink action="#{LoginUserBean.logout}" value="Logout"
				style="text-align: right"></h:commandLink>
		</div>
			<hr>
			<br>
			<center>

				<h:commandButton value="Column List" type="submit"
					action="#{DbAdminBean.columnList}" />
				&nbsp;&nbsp;&nbsp;
				<h:commandButton value="Create Table" type="submit"
					action="#{DbAdminBean.createTable}" />
				&nbsp;&nbsp;&nbsp;
				<h:commandButton value="Drop Tables" type="submit"
					action="#{DbAdminBean.dropTable}" />
				&nbsp;&nbsp;&nbsp;
				<h:commandButton value="Activity Logs" type="submit"
					action="#{DbAdminBean.activityLogs}" />
				&nbsp;&nbsp;&nbsp;
			</center>
			<br />
			<br />
			<h:messages globalOnly="true" />
			<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	<h:selectManyListbox value="#{DbAdminBean.listOfTables}" rendered="true">
				<f:selectItem itemValue="f16g324_activity"
					itemLabel="f16g324_activity" />			
				
				<f:selectItem itemValue="f16g324_student"
					itemLabel="f16g324_student" />
				<f:selectItem itemValue="f16g324_question"
					itemLabel="f16g324_question" />
					<f:selectItem itemValue="f16g324_test" itemLabel="f16g324_test" />
					<f:selectItem itemValue="f16g324_course" itemLabel="f16g324_course" />
				<f:selectItem itemValue="f16g324_user" itemLabel="f16g324_user" />	
						
			</h:selectManyListbox>
			<h:messages for="mybutton" infoStyle="color:darkgreen;"
				errorStyle="color: dark red;" />

			<br>
			<br />
			<hr>
			<center>
	<t:dataTable value="#{DbAdminBean.listOfClmns}" var="rowNumber"
				rendered="#{DbAdminBean.isColumnTableRendered}" border="1" cellspacing="0" cellpadding="5"
				>
				<h:column>
					<f:facet name="header">
						<h:outputText>Sr No</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.srNo}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>Table Name</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.tableName}" />
				</h:column>
					<h:column>
					<f:facet name="header">
						<h:outputText>Column Name</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.columnName}" />
				</h:column>
		
			</t:dataTable>
			
			<t:dataTable value="#{DbAdminBean.activities}" var="rowNumber"
				rendered="#{DbAdminBean.isActivityRendered}" border="1" cellspacing="0" cellpadding="5"
				>

				<h:column>
					<f:facet name="header">
						<h:outputText>Sr No</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.id}" />
				</h:column>
					<h:column>
					<f:facet name="header">
						<h:outputText>User Name</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.username}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText>Start Time</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.startTime}" />
				</h:column>
					<h:column>
					<f:facet name="header">
						<h:outputText>End Time</h:outputText>
					</f:facet>
					<h:outputText value="#{rowNumber.endTime}" />
				</h:column>
		
			</t:dataTable>
			</center>
			<br>
			
			<div
				style="background-attachment: scroll; overflow: auto; height: 400px; background-repeat: repeat">
			</div>
		</h:form>
	</f:view>
</body>
</html>