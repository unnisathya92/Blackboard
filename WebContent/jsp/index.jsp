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
		<h3>DB-Login</h3>
	</center>
	<hr>
	<center>
	
	<h:form>
	
			<div style="text-align: right">

				&nbsp;&nbsp;&nbsp;
				<h:commandLink action="/doc/OTTWA.pdf" value="Programmer's Doc"></h:commandLink>
				&nbsp;&nbsp;&nbsp;
				<h:commandLink action="/doc/UserGuide.pdf" value="User Guide"></h:commandLink>
				&nbsp;&nbsp;&nbsp;
				<h:commandLink action="/jsp/AboutUs.jsp" value="About Us"></h:commandLink>
		
			</div>
			<br><br><br><br>
				<h:messages globalOnly="false" layout="table" style="color:red" />
			
			<table>
				<tbody>
					<tr>
						<td><h:outputText value="Username" /></td>
						<td><h:inputText id="username" value="#{DBBean.user}"
								required="true" requiredMessage="Please enter the username">
							</h:inputText></td>
					</tr>
					<tr>
						<td><h:outputText value="Password" /></td>
						<td><h:inputSecret id="password" value="#{DBBean.pwd}"
								required="true" requiredMessage="Please enter the password"></h:inputSecret>
						</td>
					</tr>
					<tr>
						<td><h:outputText value="Host" /></td>
						<td><h:selectOneMenu value="#{DBBean.host}">
								<f:selectItem itemValue="131.193.209.54" />
								<f:selectItem itemValue="131.193.209.57" />
								<f:selectItem itemValue="131.193.143.143" />
								<f:selectItem itemValue="localhost" />
								<f:selectItem itemValue="132.168.11.121" />
							</h:selectOneMenu></td>
					</tr>
					<tr>
						<td><h:outputText value="RDBMS" /></td>
						<td><h:selectOneMenu value="#{DBBean.driver}">
								<f:selectItem itemValue="com.mysql.jdbc.Driver"
									itemLabel="MySQL" />
								<f:selectItem itemValue="com.ibm.db2.jcc.DB2Driver"
									itemLabel="DB2" />
								<f:selectItem itemValue="oracle.jdbc.driver.OracleDriver"
									itemLabel="Oracle" />
							</h:selectOneMenu></td>
					</tr>
					<tr>
						<td><h:outputText value="DB Schema" /></td>
						<td><h:inputText id="dbSchema" value="#{DBBean.schema}"
								required="true" requiredMessage="Please enter the DBSchema">
							</h:inputText></td>
					</tr>
			</table>
<br>
			<center>
				<h:commandButton action="#{DBBean.validateDBAccess}" value="Login"></h:commandButton>
			</center>


			</tbody>
		</h:form>
	</center>
</f:view>
</body>
</html>