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
			<a href="/Online_Test_Taking_System/faces/doc/UserGuide.pdf">
				User Guide</a> &nbsp;&nbsp;&nbsp; <a href="AboutUs.jsp">About Us</a>
			&nbsp;&nbsp; <a href="TeacherHome.jsp">Home</a> &nbsp;&nbsp;
			<h:commandLink action="#{LoginUserBean.logout}" value="Logout"
				style="text-align: right"></h:commandLink>
		</div>
	</h:form>
	<center>
		<br>

		<h:form>

			<h:commandButton id="Process"
				action="#{StatisticalBean.descriptiveAnalysis}"
				value="Analyse on Total" />
			<br>
			<hr>
Regression Analysis for Two Tests : <h:selectOneRadio
				value="#{StatisticalBean.testNumber}">
				<f:selectItems value="#{StatisticalBean.testList}" var="c"
					itemLabel="Test #{c.testNumber}" itemValue="#{c.testNumber}" />
			</h:selectOneRadio>
			<br />
			<h:selectOneRadio value="#{StatisticalBean.secTestNumber}"
				id="second">
				<f:selectItems value="#{StatisticalBean.testList}" var="c"
					itemLabel="Test #{c.testNumber}" itemValue="#{c.testNumber}" />
			</h:selectOneRadio>
			<h:commandButton action="#{StatisticalBean.numericalAnalysis}"
				value="Process" />
		</h:form>
		<hr>
		<h:form rendered="#{StatisticalBean.rendered}">
			<center>
				Descriptive analysis on total Marks of students <br>
				<table>
					<tr>
						<td><h3>Particulars</h3></td>
						<td><h3>Values</h3></td>
					</tr>

					<tr>
						<td>Minimum Values</td>
						<td><h:outputText value="#{StatisticalBean.minValue}" /></td>
					</tr>


					<tr>
						<td>First Quartile</td>
						<td><h:outputText value="#{StatisticalBean.q1}" /></td>
					</tr>
					<tr>
						<td>Second Quartile/Median
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td><h:outputText value="#{StatisticalBean.median}" /></td>
					</tr>
					<tr>
						<td>Third Quartile</td>
						<td><h:outputText value="#{StatisticalBean.q3}" /></td>
					</tr>
					<tr>
						<td>Maximum Value</td>
						<td><h:outputText value="#{StatisticalBean.maxValue}" /></td>
					</tr>
					<tr>
						<td>Mean</td>
						<td><h:outputText value="#{StatisticalBean.mean}" /></td>
					</tr>
					<tr>
						<td>Median</td>
						<td><h:outputText value="#{StatisticalBean.median}" /></td>
					</tr>
					<tr>
						<td>Variance</td>
						<td><h:outputText value="#{StatisticalBean.variance}" /></td>
					</tr>
					<tr>
						<td>Standard Deviation</td>
						<td><h:outputText value="#{StatisticalBean.std}" /></td>
					</tr>
					<tr>
						<td>Range</td>
						<td><h:outputText value="#{StatisticalBean.range}" /></td>
					</tr>
					<tr>
						<td>IQR</td>
						<td><h:outputText value="#{StatisticalBean.iqr}" /></td>
					</tr>


				</table>
			</center>

		</h:form>
		<h:form rendered="#{StatisticalBean.renderedForRegressionAnalysis}">
			<center>
				Regression Analysis for Test <br>
				<table>
					<tr>
						<td><h3>Particulars</h3></td>
						<td><h3>Values</h3></td>
					</tr>
					<tr>
						<td>Intercept</td>
						<td><h:outputText value="#{StatisticalBean.intercept}" /></td>

					</tr>
					<tr>
						<td>Slope</td>
						<td><h:outputText value="#{StatisticalBean.slope}" /></td>
					</tr>
					<tr>
						<td>R-Square</td>
						<td><h:outputText value="#{StatisticalBean.rSquare}" /></td>
					</tr>
					<tr>
						<td>Significance</td>
						<td><h:outputText value="#{StatisticalBean.significance}" /></td>
					</tr>
					<tr>
						<td>Intercept Std Error</td>
						<td><h:outputText value="#{StatisticalBean.interceptStdErr}" /></td>
					</tr>
					<tr>
						<td>Slope Standard Error</td>
						<td><h:outputText value="#{StatisticalBean.slopeStdErr}" /></td>
					</tr>
					<tr>
						<td>Slope Confidence Level</td>
						<td><h:outputText
								value="#{StatisticalBean.slopeConfidenceLevel}" /></td>
					</tr>
					<tr>
						<td>Regression Sum
							Squares&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td><h:outputText
								value="#{StatisticalBean.regressionSumSquares}" /></td>
					</tr>
					<tr>
						<td>Total Sum Squares</td>
						<td><h:outputText value="#{StatisticalBean.totalSumSquares}" /></td>
					</tr>
					<tr>
						<td>Sum Squared Errors</td>
						<td><h:outputText value="#{StatisticalBean.sumSquaredErrors}" /></td>
					</tr>
					<tr>
						<td>Correlation</td>
						<td><h:outputText value="#{StatisticalBean.correlation}" /></td>
					</tr>
				</table>

			</center>
		</h:form>
	</center>

</f:view>