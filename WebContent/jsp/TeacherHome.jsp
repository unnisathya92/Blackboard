<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>

<f:view>
	<center>
		<header>
			<h2>f16g324 Online Test Taking System</h2>
		</header>
	</center>
	<center>
		<h3>Teacher Home</h3>
	</center>
	<hr>
	
	Welcome <%=session.getAttribute("firstname")%>

	<h:form>
		<div style="text-align: right;">
			<a href="/Online_Test_Taking_System/faces/doc/UserGuide.pdf"> User
				Guide</a> &nbsp;&nbsp;&nbsp; <a href="AboutUs.jsp">About Us</a>
			&nbsp;&nbsp;
			<h:commandLink action="#{LoginUserBean.logout}" value="Logout"
				style="text-align: right"></h:commandLink>
		</div>
	</h:form>
	<center>
		<br> <br> <br>
		<h:form>


			<h:commandLink value="View/Upload Roaster"
				action="#{RoasterBean.retrieveRoster}" />
			<br>
			<br>
			<h:commandLink value="View Test"
				action="#{TestBean.retreiveTest}" />
			<br>
			<br>
			<h:commandLink value="Upload Questions"
				action="#{NavigationBean.questions}" />
			<br>
			<br>
			<h:commandLink value="Statistical Analysis"
				action="#{StatisticalBean.numericAnalysis}" />&nbsp;&nbsp;&nbsp;&nbsp;
			<h:commandLink value="Graphical analysis"
				action="#{ChartBean.graphicalAnalysis}" />


		</h:form>

	</center>

</f:view>