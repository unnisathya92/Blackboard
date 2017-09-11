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
<script>
	ID = window.setTimeout("update();", 1000);
	minutes =<%=session.getAttribute("minutes")%>-1;
	seconds = 60;
	hidden =<%=session.getAttribute("isTimeHide")%>;


	
	function update() {
		if(hidden){
			document.form1.minutes.value = "NA";
			document.form1.seconds.value = "NA";	
			document.getElementById('blink').hidden = true;
		}else{
		if (seconds == 0) {
			seconds = 60;
			minutes = minutes - 1;
			ID = window.setTimeout("update();", 1000);
		}
		
		if (seconds == 0 && minutes == 0) {
			alert("Time Up!!");
			
		} else if (minutes <= 1) {
			seconds = seconds - 1;
			document.form1.minutes.value = minutes;
			document.form1.seconds.value = seconds;
			document.form1.minutes.style = "color:red;";
			document.form1.seconds.style = "color:red;";
			document.getElementById('blink').hidden = false;
			ID = window.setTimeout("update();", 1000);
		} else {
			seconds = seconds - 1;
			document.form1.minutes.value = minutes;
			document.form1.seconds.value = seconds;
			ID = window.setTimeout("update();", 1000);
		}
		}
	}
</script>

<f:view>
	<center>

		<header>
			<h2>f16g324 Online Test Taking System</h2>
		</header>
	</center>
	<center>
		<h3>Test</h3>
	</center>
	<hr>
	<h:form>
	
	Welcome <%=session.getAttribute("firstname")%>


		<div style="text-align: right">
			<a href="StudentHome.jsp">Home</a>&nbsp;&nbsp;&nbsp; <a
				href="/Online_Test_Taking_System/faces/doc/UserGuide.pdf"> User
				Guide</a> &nbsp;&nbsp;&nbsp; <a href="AboutUs.jsp">About Us</a>
			&nbsp;&nbsp;&nbsp;
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
	<%-- 	hidden="<%=session.getAttribute("isTimed")%>"
 --%>
	<form name="form1" id="form1">
		<p>
			Minutes <input type="text" name="minutes" value="0" size="7"
				style="color: black;" disabled="disabled">Seconds <input
				type="text" name="seconds" value="0" size="7" style="color: black;"
				disabled="disabled">
			<output id="blink" style="color: red;" hidden=true>Please Hurry up!!!</output>
		</p>
	</form>

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
					<h:inputText value="#{rowNumber.givenanswer}" converter="javax.faces.Double"  converterMessage="Please enter a number ;since this is a numeric question"/>
				</h:column>
			</t:dataTable>
			<br>
			<h:commandButton  id ="sumbit"	action="#{QuestionBean.submit}" rendered="#{QuestionBean.isRendered}" value="Submit Test" />
		</center>
	</h:form>
</f:view>
