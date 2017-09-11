<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>OTTS</title>
<style>
.ui-input-invalid {
	font-color: red
}
</style>
<f:view>
	<center>
		<header>
			<h2>f16g324 Online Test Taking System</h2>
		</header>
	</center>
	<center>
		<h3>Login</h3>
	</center>
	<hr>
	<center>

		<h:form>


			<div style="text-align: right">
		
				<h:commandLink action="/doc/UserGuide.pdf" value="User Guide"></h:commandLink>
				&nbsp;&nbsp;&nbsp;
				<h:commandLink action="/jsp/AboutUs.jsp" value="About Us"></h:commandLink>

			</div>

			<br>
			<br>
			<br>
			<br>



			<h:messages globalOnly="false" layout="table" errorStyle="color:red" />

			<table>
				<tr>
					<td><h:outputText value="Username" /></td>
					<td><h:inputText id="username" value="#{LoginUserBean.user}"
							required="true" requiredMessage="Please enter the username">
						</h:inputText></td>
				</tr>

				<tr>
					<td><h:outputText value="Password" /></td>
					<td><h:inputSecret id="password" value="#{LoginUserBean.pwd}"
							required="true" requiredMessage="Please enter the password"></h:inputSecret></td>
				</tr>


				<tr>
					<td><h:outputText value="Role" /></td>
					<td><h:selectOneMenu value="#{LoginUserBean.role}">
							<f:selectItem itemValue="Student" />
							<f:selectItem itemValue="Teacher" />
							<f:selectItem itemValue="Admin" />
						</h:selectOneMenu></td>
				</tr>
			</table>
			<br />

			<h:commandButton action="#{LoginUserBean.validate}"
				value="Login"></h:commandButton>
		</h:form>
	</center>

</f:view>
</html>

