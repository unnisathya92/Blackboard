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
		<h3>Graphical Analysis</h3>
	</center>
	<hr>
	
	Welcome <%=session.getAttribute("firstname")%>

	<h:form>
		<div style="text-align: right;">
		<a href="/Online_Test_Taking_System/faces/doc/UserGuide.pdf"> User
				Guide</a> &nbsp;&nbsp;&nbsp; <a href="AboutUs.jsp">About Us</a>
			&nbsp;&nbsp;
			<a href="TeacherHome.jsp">Home</a> &nbsp;&nbsp;
			<h:commandLink action="#{LoginUserBean.logout}" value="Logout"
				style="text-align: right"></h:commandLink>
		</div>
	</h:form>
	<center>
		<br> <br> <br>

		<h:form>
			<table>
				<tr>
					<td><h:commandButton id="barchart"
							action="#{ChartBean.barChart}" value="View Bar Chart" /></td>
					<td width="33%"></td>
					<td><h:commandButton id="piechart"
							action="#{ChartBean.piechart}" value="View Pie Chart" /></td>
				</tr>
			</table>
			<hr>
			<br>
			<h:graphicImage value="#{ChartBean.finalPath}"
				rendered="#{ChartBean.barrender}" alt="BarChart" />

			<h:graphicImage value="#{ChartBean.finalPath}"
				rendered="#{ChartBean.pierender}" alt="BarChart" />
		</h:form>

	</center>

</f:view>