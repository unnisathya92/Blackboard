package edu.uic.model.bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.myfaces.custom.fileupload.UploadedFile;

import edu.uic.dao.DBDao;
import edu.uic.dao.DBService;

@ManagedBean
@SessionScoped
public class TestBean implements Serializable {
	private UploadedFile uploadedFile;
	private String fileName;
	private static final long serialVersionUID = 1094801825228386363L;
	private int testNumber;
	private int crn;
	private int courseCode;
	private String score;

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	private String startDate;
	private String endDate;


	

	private String append;
	private String duration;

	public TestBean(int i) {

	}

	public TestBean() {
		retreiveTest();
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getAppend() {
		return append;
	}

	public void setAppend(String append) {
		this.append = append;
	}


	private List<TestBean> testBeanList;

	public int getTestNumber() {
		return testNumber;
	}

	public void setTestNumber(int testNumber) {
		this.testNumber = testNumber;
	}

	public int getCrn() {
		return crn;
	}

	public void setCrn(int crn) {
		this.crn = crn;
	}

	public int getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(int courseCode) {
		this.courseCode = courseCode;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<TestBean> getTestBeanList() {
		return testBeanList;
	}

	public void setTestBeanList(List<TestBean> testBeanList) {
		this.testBeanList = testBeanList;
	}



	public String takeTest() {
		return "success";
	}

	public String retreiveTest() {

		DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
				"jdbc:mysql://"+LoginUserBean.host+":3306/", LoginUserBean.testSchema, LoginUserBean.mysqlJdbcDriver);
		try {

			dao1.createConnection();
			if (dao1.getConnection() != null) {
				DBService serv = new DBService(dao1);
				ResultSet resultSet = serv.fetchDataFromTable("f16g324_test", -1, -1);
				this.testBeanList = new ArrayList<TestBean>();
				TestBean b = new TestBean(1);
				while (resultSet.next()) {

					if (resultSet.getString(1) != null) {
						b.setTestNumber(Integer.parseInt(resultSet.getString(1)));
						b.setCrn(Integer.parseInt(resultSet.getString(2)));
						b.setCourseCode(Integer.parseInt(resultSet.getString(3)));
						b.setStartDate(resultSet.getString(4));
						b.setEndDate(resultSet.getString(5));
						b.setDuration(resultSet.getString(7));
//						boolean[] isAvailable = checkForTest(serv, Integer.parseInt(resultSet.getString(1)),
//								 resultSet.getString(4),resultSet.getString(5));
						b.setTotal(resultSet.getString(8));
						b.setScore(returnScoreForStudent(Integer.parseInt(resultSet.getString(1))));
						this.testBeanList.add(b);
						b = new TestBean(1);
					}
				}
			}
		} catch (NumberFormatException e) {
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
		} finally {
			dao1.closeConnection();
		}
		return "viewuploadTest";

	}
	private String total;
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	private String returnScoreForStudent(int test) {
		String t = "NA";
		DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
				"jdbc:mysql://"+LoginUserBean.host+":3306/", LoginUserBean.testSchema, LoginUserBean.mysqlJdbcDriver);try{
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

			String username = (String) session.getAttribute("username");

	       	dao1.createConnection();
	       	ResultSet s = dao1.getConnection().createStatement().executeQuery("select testscore_xml from f16g324_student where username_fk='"+username+"'");
            if(s.next()){
           String toParse =  s.getString(1);
           System.err.println(toParse);
           System.err.println(test);

           if(test == 4)
           {
    			 t = toParse.substring(toParse.indexOf("<project>") + 9,
             			toParse.indexOf("</project>")); 
    		 }
           else
      		 t = toParse.substring(toParse.indexOf("<test" + test + ">") + 7,
          			toParse.indexOf("</test" + test + ">"));
      		
      		 }
         
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			dao1.closeConnection();
		}
				return t;
		
	}

	
	public String submit() {
		String returnMSG = "";
		DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
				"jdbc:mysql://"+LoginUserBean.host+":3306/", LoginUserBean.testSchema, LoginUserBean.mysqlJdbcDriver);
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
					
					while ((line = reader.readLine()) != null) {
						if (uploadedFile.getName().split("\\.")[uploadedFile.getName().split("\\.").length - 1]
								.equalsIgnoreCase("csv")) {
							tokens = line.toString().split(",");
						} else {
							tokens = line.toString().split("\t");
						}
						try {

							String query = "INSERT INTO f16g324_test VALUES ('" + tokens[0] + "','" + tokens[1] + "','"
									+ tokens[2] + "','" + tokens[3] + "','" + tokens[4] + "',0," + tokens[5] + ",0)";

							dao1.getConnection().createStatement().executeUpdate(query);
							count++;
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					FacesContext.getCurrentInstance().addMessage("uploadForm",
							new FacesMessage(FacesMessage.SEVERITY_INFO,
									"File imported Successfully." + count + " record(s) imported", null));
					retreiveTest();
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

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String update() {
		DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
				"jdbc:mysql://"+LoginUserBean.host+":3306/", LoginUserBean.testSchema, LoginUserBean.mysqlJdbcDriver);
		try {
			dao1.createConnection();
			if (dao1.getConnection() != null) {

				String qu = "Update f16g324_test SET durationmin='" + t.duration + "' where testnumber='" + t.testNumber + "'";
				dao1.getConnection().createStatement().executeUpdate(qu);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
		} finally {
			dao1.closeConnection();
		}
		retreiveTest();

		return "Success1";

	}

	TestBean t;

	public TestBean getT() {
		return t;
	}

	public void setT(TestBean t) {
		this.t = t;
	}
}
