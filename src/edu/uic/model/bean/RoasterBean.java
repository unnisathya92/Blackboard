package edu.uic.model.bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.fileupload.UploadedFile;

import edu.uic.dao.DBDao;
import edu.uic.dao.DBService;

@ManagedBean
@SessionScoped
public class RoasterBean implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	private void parseXML(String xmlString, RoasterBean b, int noOfTests) {
		noOfTests= noOfTests -1;
		scores = new HashMap<String, String>();
		if (xmlString.indexOf("<project>") != -1) {
			String project = xmlString.substring(xmlString.indexOf("<project>") + 9, xmlString.indexOf("</project>"));
			scores.put("Project", project);
		} else
			scores.put("Project", "NA");

		for (int i = 1; i <= noOfTests; i++) {
			if (xmlString.indexOf("<test" + i + ">") != -1) {
				String test = xmlString.substring(xmlString.indexOf("<test" + i + ">") + 7,
						xmlString.indexOf("</test" + i + ">"));
				
				scores.put("Exam " + i, test);
			} else
				scores.put("Exam " + i, "NA");
		}
		if (xmlString.indexOf("<total>") != -1) {
			String total = xmlString.substring(xmlString.indexOf("<total>") + 7, xmlString.indexOf("</total>"));
			scores.put("Total", total);
		} else
			scores.put("Total", "NA");

		studentScores.add(scores);
	}

	public String submit() {

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
				String[] tokens;
				DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
						"jdbc:mysql://"+LoginUserBean.host+":3306/", LoginUserBean.testSchema, LoginUserBean.mysqlJdbcDriver);
				List<RoasterBean> rosterBeanList = new ArrayList<RoasterBean>();
				RoasterBean r;
				String returnMSG = "success";
				String errorMessage = "";
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(uploadedFile.getInputStream()));
					String line;
					StringBuilder errors = new StringBuilder();
					while ((line = reader.readLine()) != null) {
						if (uploadedFile.getName().split("\\.")[uploadedFile.getName().split("\\.").length - 1]
								.equalsIgnoreCase("csv")){
							 tokens = line.toString().split(",");
						}
						else {
							 tokens = line.toString().split("\t");
						}
						try {
							r = new RoasterBean();
							r.setLastName(tokens[0]);
							r.setFirstName(tokens[1]);
							r.setUserName(tokens[2]);
							r.setStudentID(Integer.parseInt(tokens[3]));
							r.setLastaccess(tokens[4]);
							r.setAvailability(tokens[5]);
							parse(tokens, r);
							rosterBeanList.add(r);
						} catch (Exception e) {
							e.printStackTrace();
							errorMessage = e.getMessage();
							if (!(tokens[0].equalsIgnoreCase("Last_Name") || tokens[3].contains("tudent"))) {
								errors.append("Error while uploading column with Last_Name " + tokens[0] + " :: "
										+ e.getMessage());
								errors.append("\n");
							}
						}
					}

					reader.close();
					dao1.createConnection();
					int count = 0;
					createUsers(rosterBeanList, dao1);
					for (RoasterBean r1 : rosterBeanList) {
						try {
							String query = "INSERT INTO f16g324_student VALUES (" + "'" + r1.getStudentID() + "'," + "'"
									+ r1.getFirstName() + "'," + "'" + r1.getLastName() + "'," + "'" + r1.getUserName()
									+ "'," + "'" + r1.getXmlScores() + "'," + "''," + "'" + r1.getAvailability() + "','" + r1.getLastaccess()+"')";
							dao1.getConnection().createStatement().executeUpdate(query);
//							DBService s = new DBService(dao1);
//							int highValue = s.fetchMaxValueForActivity() + 1;
//							query = "INSERT INTO f16g324_activity VALUES (" + "'" + r1.getStudentID() + "'," + "'"
//									+ r1.getLastaccess() + "'," + "'" + r1.getLastaccess() + "'," + "''," + highValue
//									+ ")";
//							dao1.getConnection().createStatement().executeUpdate(query);

							count++;
						} catch (Exception e) {
							e.printStackTrace();
							errors.append("Error while uploading column with Last_Name " + r1.getLastName() + "   "
									+ e.getMessage());
							errors.append("\n");
							errorMessage = e.getMessage();
						}
					}
					if (errors.length() > 0) {
						FacesContext.getCurrentInstance().addMessage("uploadForm", new FacesMessage(
								FacesMessage.SEVERITY_ERROR, "Error While importing the file " + errorMessage, null));
					} else {
						FacesContext.getCurrentInstance().addMessage("uploadForm",
								new FacesMessage(FacesMessage.SEVERITY_INFO, count + "Record(s) Updated.", null));
					}
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
				retrieveRoster();
				return returnMSG;
			}
		}
	}

	private void createUsers(List<RoasterBean> rosterBeanList, DBDao dao1) {
		for (RoasterBean r1 : rosterBeanList) {
			String query = "INSERT INTO f16g324_user VALUES (" + "'" + r1.getStudentID() + "'," + "'" + r1.getUserName() + "',"
					+ "'Password'," + "'" + r1.getFirstName() + "'," + "'" + r1.getLastName() + "'," + "'Student'"
					+ ")";
			try {
				dao1.getConnection().createStatement().executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	public String retrieveRoster() {

		DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
				"jdbc:mysql://"+LoginUserBean.host+":3306/", LoginUserBean.testSchema, LoginUserBean.mysqlJdbcDriver);
		try {
			dao1.createConnection();
			if (dao1.getConnection() != null) {
				DBService serv = new DBService(dao1);
				ResultSet resultSet = serv.fetchDataFromTable("f16g324_student", -1, -1);
				int noOfTests = serv.returnNoOfTests();
				this.roasterBeanList = new ArrayList<RoasterBean>();
				RoasterBean b = new RoasterBean();
				studentScores = new ArrayList<HashMap<String, String>>();
				while (resultSet.next()) {

					b.setStudentID(Integer.parseInt(resultSet.getString(1)));
					b.setFirstName(resultSet.getString(2));
					b.setLastName(resultSet.getString(3));
					b.setUserName(resultSet.getString(4));
					parseXML(resultSet.getString(5) + "", b, noOfTests);
					b.setAvailability(resultSet.getString(7));
					b.setLastaccess(resultSet.getString(8));
					this.roasterBeanList.add(b);
					b = new RoasterBean();

				}
				if (roasterBeanList.isEmpty())
					isRosterAvailable = false;
				else
					isRosterAvailable = true;

			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
		} finally {
			dao1.closeConnection();
		}

		return "viewuploadRoaster";

	}

	private boolean parse(String[] inputs, RoasterBean r) {
		StringBuilder s = new StringBuilder();
		s.append("<total>" + inputs[6] + "</total>");
		for (int i = 7; i < inputs.length - 1; i++) {
			try {
				s.append("<test" + (i - 6) + ">" + inputs[i] + "</test" + (i - 6) + ">");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		s.append("<project>" + inputs[inputs.length - 1] + "</project>");

		r.setXmlScores(s.toString());
		return true;
	}

public String update(){
	


	DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
			"jdbc:mysql://"+LoginUserBean.host+":3306/", LoginUserBean.testSchema, LoginUserBean.mysqlJdbcDriver);
	try {
		dao1.createConnection();
		if (dao1.getConnection() != null) {
			//[{Project=NA, Exam 3=q2e, Exam 2=NA, Exam 1=NA, Total=NA}, {Project=NA, Exam 3=NA, Exam 2=NA, Exam 1=NA, Total=NA}]
			StringBuilder s = new StringBuilder();
			s.append("<total>" + studentScores.get(Integer.parseInt(rowNumberToUpdate)).get("Total") + "</total>");
			s.append("<test1>" + studentScores.get(Integer.parseInt(rowNumberToUpdate)).get("Exam 1") + "</test1>");
			s.append("<test2>" + studentScores.get(Integer.parseInt(rowNumberToUpdate)).get("Exam 2") + "</test2>");
			s.append("<test3>" + studentScores.get(Integer.parseInt(rowNumberToUpdate)).get("Exam 3") + "</test3>");
			s.append("<project>" + studentScores.get(Integer.parseInt(rowNumberToUpdate)).get("Project") + "</project>");
			
			dao1.getConnection().createStatement().executeUpdate("Update f16g324_student SET testscore_xml='"+s.toString()+"' where student_uin_fk='"+studentID+"'");

		}
	} catch (NumberFormatException e) {
		e.printStackTrace();
	} catch (SQLException e) {
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
	} finally {
		dao1.closeConnection();
	}

	return "Success1";
}
	private List<RoasterBean> roasterBeanList;
	private UploadedFile uploadedFile;
	private boolean isRosterAvailable;
	private String lastName;
	private String firstName;
	private String userName;
	private int studentID;
	private String lastaccess;
	private String rowNumberToUpdate;
	private String availability;
	private String xmlScores;
	private HashMap<String, String> scores;
	private List<HashMap<String, String>> studentScores;
	private String header;

	public String getRowNumberToUpdate() {
		return rowNumberToUpdate;
	}

	public void setRowNumberToUpdate(String rowNumberToUpdate) {
		this.rowNumberToUpdate = rowNumberToUpdate;
	}

	public String getHeader() {

		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public List<HashMap<String, String>> getStudentScores() {
		return studentScores;
	}

	public void setStudentScores(List<HashMap<String, String>> studentScores) {
		this.studentScores = studentScores;
	}

	public HashMap<String, String> getScores() {
		return scores;
	}

	public void setScores(HashMap<String, String> scores) {
		this.scores = scores;
	}

	public List<RoasterBean> getRoasterBeanList() {
		return roasterBeanList;
	}

	public void setRoasterBeanList(List<RoasterBean> roasterBeanList) {
		this.roasterBeanList = roasterBeanList;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public boolean getIsRosterAvailable() {
		return isRosterAvailable;
	}

	public void setIsRosterAvailable(boolean isRosterAvailable) {
		this.isRosterAvailable = isRosterAvailable;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getStudentID() {
		return studentID;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	public String getLastaccess() {
		return lastaccess;
	}

	public void setLastaccess(String lastaccess) {
		this.lastaccess = lastaccess;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public String getXmlScores() {
		return xmlScores;
	}

	public void setXmlScores(String xmlScores) {
		this.xmlScores = xmlScores;
	}

}
