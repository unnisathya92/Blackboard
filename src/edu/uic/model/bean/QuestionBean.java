package edu.uic.model.bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.myfaces.custom.fileupload.UploadedFile;

import edu.uic.dao.DBDao;
import edu.uic.dao.DBService;

@ManagedBean
@SessionScoped
public class QuestionBean implements Serializable {
	private static final long serialVersionUID = 1094801825228386363L;
	private int qno;
	private int testNumber;
	private int rowNumberToUpdate;
	private ArrayList<TestBean> testList;
	private TestBean testBean;
	private QuestionBean q;

	public TestBean getTestBean() {
		return testBean;
	}

	public void setTestBean(TestBean testBean) {
		this.testBean = testBean;
	}

	public QuestionBean getQ() {
		return q;
	}

	public void setQ(QuestionBean q) {
		this.q = q;
	}

	public ArrayList<TestBean> getTestList() {
		return testList;
	}

	public void setTestList(ArrayList<TestBean> testList) {
		this.testList = testList;
	}

	public int getRowNumberToUpdate() {
		return rowNumberToUpdate;
	}

	public void setRowNumberToUpdate(int rowNumberToUpdate) {
		this.rowNumberToUpdate = rowNumberToUpdate;
	}

	public int getTestNumber() {
		return testNumber;
	}

	public void setTestNumber(int testNumber) {
		this.testNumber = testNumber;
	}

	public QuestionBean() {
		testList = new ArrayList<TestBean>();
		DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
				"jdbc:mysql://" + LoginUserBean.host + ":3306/", LoginUserBean.testSchema,
				LoginUserBean.mysqlJdbcDriver);
		try {
			dao1.createConnection();
			TestBean t;
			ResultSet r = dao1.getConnection().createStatement().executeQuery("select testnumber from f16g324_test");
			while (r.next()) {
				t = new TestBean();
				t.setTestNumber(r.getInt(1));
				testList.add(t);
				calculateTotalMarks(r.getInt(1), dao1);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			dao1.closeConnection();
		}
		retreiveQuestions();
	}

	public QuestionBean(int i) {
		// TODO Auto-generated constructor stub
	}

	private String questiontype;
	private String questiontext;
	private double correctanswer;
	private double tolerance;
	private int weightage;

	public int getWeightage() {
		return weightage;
	}

	public void setWeightage(int weightage) {
		this.weightage = weightage;
	}

	private double givenanswer;

	public double getGivenanswer() {
		return givenanswer;
	}

	public void setGivenanswer(double givenanswer) {
		this.givenanswer = givenanswer;

	}

	private boolean isRendered;

	public boolean getIsRendered() {
		return isRendered;
	}

	public void setIsRendered(boolean isRendered) {
		this.isRendered = isRendered;
	}

	private List<QuestionBean> questionBeanList;

	private static int tNO;

	@ManagedProperty(value = "#{param.test}")
	private Long test;

	public Long getTest() {
		return test;
	}

	public void setTest(Long test) {
		this.test = test;
	}

	public int getQno() {
		return qno;
	}

	public void setQno(int qno) {
		this.qno = qno;
	}

	public String getQuestiontype() {
		return questiontype;
	}

	public void setQuestiontype(String questiontype) {
		this.questiontype = questiontype;
	}

	public String getQuestiontext() {
		return questiontext;
	}

	public void setQuestiontext(String questiontext) {
		this.questiontext = questiontext;
	}

	public double getCorrectanswer() {
		return correctanswer;
	}

	public void setCorrectanswer(double correctanswer) {
		this.correctanswer = correctanswer;
	}

	public double getTolerance() {
		return tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	public List<QuestionBean> getQuestionBeanList() {
		return questionBeanList;
	}

	public void setQuestionBeanList(List<QuestionBean> questionBeanList) {
		this.questionBeanList = questionBeanList;
	}

	public String submit() {

		DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
				"jdbc:mysql://" + LoginUserBean.host + ":3306/", LoginUserBean.testSchema,
				LoginUserBean.mysqlJdbcDriver);
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

		String username = (String) session.getAttribute("username");
		String[] xml = null;
		try {
			dao1.createConnection();
			if (dao1.getConnection() != null) {
				Statement statement = dao1.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = statement.executeQuery(
						"select feedback_xml,testscore_xml from f16g324_student where username_fk='" + username + "'");
				
				String existingXmlFeedback = "";
				String existingscoreXML = "";
				while (rs.next()) {
					existingXmlFeedback = rs.getString(1);
					existingscoreXML = rs.getString(2);
				}
				xml = returnXML(existingXmlFeedback, existingscoreXML);
				
				int k=0;
				for(TestBean b : testBean.getTestBeanList()	){
					try{
					k+=Integer.parseInt(b.getScore());
					}
					catch(Exception e){
						e.printStackTrace();
					}					
				}
				if(k!=0)
				{
					int start = xml[0].indexOf("<total>");
					int end = xml[0].indexOf("</total>") + 8;
					String s = xml[0].substring(start, end);
					xml[0] = xml[0].replaceAll(s, "<total>"+k+"</total>");
					
				}
				if (xml != null) {
					statement.executeUpdate("UPDATE f16g324_student SET `feedback_xml`='" + xml[1]
							+ "',`testscore_xml`='" + xml[0] + "' where username_fk='" + username + "'");
				}
				statement.executeUpdate(
						"UPDATE f16g324_test SET `isExamAvailable`='0' WHERE `testnumber`='" + tNO + "'");

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao1.closeConnection();
		}
		
			if(xml!=null && row != null)				
				row.setScore(xml[2]);
			
			
		return "success";

	}
private TestBean row;

	public TestBean getRow() {
	return row;
}

public void setRow(TestBean row) {
	this.row = row;
}

	public String takeTest() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

		DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
				"jdbc:mysql://" + LoginUserBean.host + ":3306/", LoginUserBean.testSchema,
				LoginUserBean.mysqlJdbcDriver);

		try {
			dao1.createConnection();
			if (dao1.getConnection() != null) {
				ResultSet rs = dao1.getConnection().createStatement()
						.executeQuery("select durationmin from f16g324_test where testnumber='" + test + "'");
				rs.next();
				int duration = -1;
				try {
					duration = rs.getInt(1);
				} catch (Exception e) {
					session.setAttribute("isTimeHide", true);
				}
				if (duration != -1) {
					session.setAttribute("minutes", duration);
					session.setAttribute("isTimeHide", false);
				}
				DBService serv = new DBService(dao1);
				ResultSet resultSet = serv.fetchDataFromTable("f16g324_question", "testid = " + test);
				this.questionBeanList = new ArrayList<QuestionBean>();
				QuestionBean b = new QuestionBean(1);
				while (resultSet.next()) {
					if (resultSet.getString(1) != null) {
						b.setQuestiontext(resultSet.getString(4));
						b.setTestNumber(Integer.parseInt(resultSet.getString(2)));
						b.setQno(Integer.parseInt(resultSet.getString(7)));
						b.setCorrectanswer(Double.parseDouble(resultSet.getString(5)));
						b.setWeightage(Integer.parseInt(resultSet.getString(8)));
						b.setTolerance(Double.parseDouble(resultSet.getString(6)));
						this.questionBeanList.add(b);
						b = new QuestionBean(1);
					}
					isRendered = true;
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		} finally {
			dao1.closeConnection();
		}
		if (isFeedBack != null) {
			if (isFeedBack.equalsIgnoreCase("Yes")) {
				return "feedback";

			}
		}

		return "takeTest";

	}

	private String[] returnXML(String existingXmlFeedback, String existingscoreXML) {
		String[] returnString = new String[3];
		StringBuilder xml;
		if (existingXmlFeedback != null) {
			xml = new StringBuilder(existingXmlFeedback);
		} else
			xml = new StringBuilder();
		if (xml.length() > 0) {
			int st = xml.indexOf("<test" + test + ">");
			int en = xml.indexOf("</test" + test + ">")+8;
			if(st !=-1)
			xml = xml.replace(st, en, "");
			
		}
		xml.append("<test" + test + ">");
		int marks = 0;
		int count = 1;
		for (QuestionBean q : questionBeanList) {
			xml.append("<Q" + q.getQno() + ">");
			xml.append("<correct-answer>" + q.getCorrectanswer() + "</correct-answer>");
			xml.append("<given-answer" + count + ">" + q.getGivenanswer() + "</given-answer" + count + ">");
			xml.append("<weightage>" + q.getWeightage() + "</weightage>");
			xml.append("</Q" + q.getQno() + ">");

			if (q.getGivenanswer() >= (q.getCorrectanswer() - q.getTolerance())
					&& q.getGivenanswer() <= (q.getCorrectanswer() + q.getTolerance())) {
				marks += q.getWeightage();
			}
			count++;
		}
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

		session.setAttribute("score", marks);
		xml.append("<marks>" + marks + "</marks>");
		xml.append("</test" + test + ">");
		if(test != 4){
		int start = existingscoreXML.indexOf("<test" + test + ">");
		int end = existingscoreXML.indexOf("</test" + test + ">") + 8;
		String s = existingscoreXML.substring(start, end);
		existingscoreXML = existingscoreXML.replaceAll(s, "<test" + test + ">" + marks + "</test" + test + ">");
		}
		else {
			int start = existingscoreXML.indexOf("<project>");
			int end = existingscoreXML.indexOf("</project>") + 10;
			String s = existingscoreXML.substring(start, end);
			existingscoreXML = existingscoreXML.replaceAll(s, "<project>" + marks + "</project>");
			}
				
		
		
		returnString[0] = existingscoreXML;
		returnString[1] = xml.toString();
		returnString[2] = marks + "";

		return returnString;

	}

	public String retreiveQuestions() {
		DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
				"jdbc:mysql://" + LoginUserBean.host + ":3306/", LoginUserBean.testSchema,
				LoginUserBean.mysqlJdbcDriver);
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			String s = (String) session.getAttribute("username");
			dao1.createConnection();
			if (dao1.getConnection() != null) {
				String fd = "";
				ResultSet rt = dao1.getConnection().createStatement()
						.executeQuery("select feedback_xml from f16g324_student where username_fk ='" + s + "'");
				if (rt.next())
					fd = rt.getString(1);

				ResultSet resultSet = dao1.getConnection().createStatement()
						.executeQuery("select * from f16g324_question where testid ='" + testNumber + "'");
				this.questionBeanList = new ArrayList<QuestionBean>();
				QuestionBean b = new QuestionBean(1);
				int count = 1;
				while (resultSet.next()) {

					if (resultSet.getString(1) != null) {
						b.setQno(Integer.parseInt(resultSet.getString(7)));
						b.setTestNumber(Integer.parseInt(resultSet.getString(2)));
						b.setQuestiontype(resultSet.getString(3));
						b.setQuestiontext(resultSet.getString(4));
						b.setCorrectanswer(Double.parseDouble(resultSet.getString(5)));
						b.setTolerance(Double.parseDouble(resultSet.getString(6)));
						b.setWeightage(Integer.parseInt(resultSet.getString(8)));
						if (fd.length() > 0)
							parseFeedBack(fd, Integer.parseInt(resultSet.getString(2)), b, count);

						this.questionBeanList.add(b);
						b = new QuestionBean(1);
						count++;
					}

				}

			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			dao1.closeConnection();
		}

		return "feedback";
	}

	private double marksAwarded;

	public double getMarksAwarded() {
		return marksAwarded;
	}

	public void setMarksAwarded(double marksAwarded) {
		this.marksAwarded = marksAwarded;
	}

	private double parseFeedBack(String fd, int i, QuestionBean b, int count) {
		double returnValue = 0;
		int start = fd.indexOf("<test" + i + ">") + 7;
		int end = fd.indexOf("</test" + i + ">");

		String t = fd.substring(start, end);
		int toAdd = 15;
		if (count > 9) {
			toAdd = 16;
		}
		int startIndex = t.indexOf("<given-answer" + count + ">") + toAdd;
		int endIndex = t.indexOf("</given-answer" + count + ">");
		String givA = t.substring(startIndex, endIndex);

		try {
			returnValue = Double.parseDouble(givA);
		} catch (Exception e) {
			e.printStackTrace();
		}
		b.setGivenanswer(returnValue);
		double M;
		double max = b.getCorrectanswer() + b.getTolerance();
		double min = b.getCorrectanswer() - b.getTolerance();
	

		if (b.getGivenanswer() >= min && b.getGivenanswer() <= max) {
			System.err.println("Correct");
			M = b.getWeightage();
		} else
			M = 0;

		b.setMarksAwarded(M);
		return returnValue;
	}

	private String calculateTotalMarks(int i, DBDao dao1) {

		try {

			dao1.createConnection();
			if (dao1.getConnection() != null) {
				ResultSet resultSet = dao1.getConnection().createStatement()
						.executeQuery("select sum(weightage) from f16g324_question where testid ='" + i + "'");

				resultSet.next();
				int totalMarks = resultSet.getInt(1);
				dao1.getConnection().createStatement().executeUpdate(
						"update f16g324_test set totalmarks = '" + totalMarks + "' where testnumber ='" + i + "'");

			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return "success";
	}

	private UploadedFile uploadedFile;

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	private String header;

	public String getHeader() {

		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String update() {
		DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
				"jdbc:mysql://" + LoginUserBean.host + ":3306/", LoginUserBean.testSchema,
				LoginUserBean.mysqlJdbcDriver);
		try {
			dao1.createConnection();
			if (dao1.getConnection() != null) {

				String qu = "Update f16g324_question SET weightage='" + q.weightage + "' where qno='" + q.qno
						+ "' and testid ='" + testNumber + "'";
				dao1.getConnection().createStatement().executeUpdate(qu);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
		} finally {
			calculateTotalMarks(testNumber, dao1);
			dao1.closeConnection();
		}
		retreiveQuestions();

		return "Success1";

	}

	private String isFeedBack;
	private Boolean isQuestionsRendered;

	public Boolean getIsQuestionsRendered() {
		return isQuestionsRendered;
	}

	public void setIsQuestionsRendered(Boolean isQuestionsRendered) {
		this.isQuestionsRendered = isQuestionsRendered;
	}

	public String getIsFeedBack() {
		return isFeedBack;
	}

	public void setIsFeedBack(String isFeedBack) {
		this.isFeedBack = isFeedBack;
	}

	public String submitQuestions() {
		String returnMSG = "";
		DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
				"jdbc:mysql://" + LoginUserBean.host + ":3306/", LoginUserBean.testSchema,
				LoginUserBean.mysqlJdbcDriver);
		if (uploadedFile == null) {
			FacesContext.getCurrentInstance().addMessage("uploadForm",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please upload a file before continuing.", null));
			return "FAIL";
		} else {
			if (!(uploadedFile.getName().split("\\.")[uploadedFile.getName().split("\\.").length - 1]
					.equalsIgnoreCase("csv")
					|| uploadedFile.getName().split("\\.")[uploadedFile.getName().split("\\.").length - 1]
							.equalsIgnoreCase("txt"))) {
				FacesContext.getCurrentInstance().addMessage("uploadForm", new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Please upload either .CSV or .TXT files.", null));
				return "FAIL";

			} else {
				BufferedReader reader;
				try {
					reader = new BufferedReader(new InputStreamReader(uploadedFile.getInputStream()));
					String line;
					dao1.createConnection();
					int count = 0;

					String[] tokens;
					int k = uploadedFile.getName().length() - 5;
					if (header.equalsIgnoreCase("True"))
						reader.readLine();

					while ((line = reader.readLine()) != null) {
						if (uploadedFile.getName().split("\\.")[uploadedFile.getName().split("\\.").length - 1]
								.equalsIgnoreCase("csv")) {
							tokens = line.toString().split(",");
						} else {
							tokens = line.toString().split("\t");
						}
						try {
							// NUM Question 1 a 10.5 0.1
							// id tid ty te ca to qno we

							if (uploadedFile.getName().charAt(k) == '1') {
								testNumber = 1;
							} else if (uploadedFile.getName().charAt(k) == '2') {
								testNumber = 2;
							} else if (uploadedFile.getName().charAt(k) == '3') {
								testNumber = 3;
							} else if (uploadedFile.getName().charAt(k) == 't') {
								testNumber = 4;
							}
							String q = "select max(id) from f16g324_question";
							ResultSet r = dao1.getConnection().createStatement().executeQuery(q);
							r.next();
							int id = r.getInt(1) + 1;
							q = "select max(qno) from f16g324_question where testid=" + testNumber;
							r = dao1.getConnection().createStatement().executeQuery(q);
							r.next();
							int qNo = r.getInt(1) + 1;
							Double.parseDouble(tokens[2]);
							String query = "INSERT INTO f16g324_question VALUES ('" + id + "','" + testNumber + "','"
									+ tokens[0] + "','" + tokens[1] + "','" + tokens[2] + "'," + tokens[3] + "," + qNo
									+ "," + 10 + ")";
							dao1.getConnection().createStatement().executeUpdate(query);
							count++;
						} catch (Exception e) {
							FacesContext.getCurrentInstance().addMessage("uploadForm",
									new FacesMessage(FacesMessage.SEVERITY_ERROR,
											"Error While Importing row : " + count + " " + e.getMessage(), null));

							e.printStackTrace();
						}

					}
					String query = "UPDATE f16g324_test SET totalmarks=" + count * 10 + " WHERE testnumber="
							+ testNumber;
					dao1.getConnection().createStatement().executeUpdate(query);

					FacesContext.getCurrentInstance().addMessage("uploadForm",
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									"File imported Successfully." + count + " record(s) imported", null));
					retreiveQuestions();
					isQuestionsRendered = true;
				} catch (IOException e) {

					FacesContext.getCurrentInstance().addMessage("uploadForm",
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload failed with I/O error.", null));

					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
					FacesContext.getCurrentInstance().addMessage("uploadForm",
							new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
					returnMSG = "error";
				}
				return returnMSG;
			}
		}
	}
}