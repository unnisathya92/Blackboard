package edu.uic.model.bean;

import java.io.Serializable;
import java.sql.ResultSet;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.Connection;

import edu.uic.dao.DBDao;
import edu.uic.dao.DBService;

@ManagedBean
@SessionScoped
public class LoginUserBean implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;
	public static String dbUserNameForTestSchema;
	public static String dbPasswordForTestSchema;
	public static String host;
	public static final String mysqlJdbcDriver = "com.mysql.jdbc.Driver";
	public static String testSchema;
	private Connection connection;
	private String schema;
	private String pwd;
	private String newPwd;

	@ManagedProperty(value = "#{dBDao}")
	private DBDao dbDao;

	// public LoginUserBean(){
	// this.connection = (Connection) dbDao.getConnection();
	// this.schema = dbDao.getDatabaseSchema();
	// }

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	// public DBDao getDbDao() {
	// return dbDao;
	// }
	//
	// public void setDbDao(DBDao dbDao) {
	// this.dbDao = dbDao;
	// }

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	private String msg;
	private String user;

	public String getUserHomePage() {
		return userHomePage;
	}

	public void setUserHomePage(String userHomePage) {
		this.userHomePage = userHomePage;
	}

	private String role;
	private String userHomePage;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	// validate login
	public String validate() {
		boolean isError = false;
		String returnMsg = "";
		DBDao dao1 = new DBDao(dbUserNameForTestSchema, dbPasswordForTestSchema,
				"jdbc:mysql://" + LoginUserBean.host + ":3306/", testSchema, mysqlJdbcDriver);
		try {
			dao1.createConnection();
			boolean valid = false;
			if (dao1.getConnection() != null) {
				DBService serv = new DBService(dao1);
				String firstname = "";
				String dbRole = "";
				String wherClause = "username = '" + user + "'";
				try {
					ResultSet resultSet = serv.fetchDataFromTable("f16g324_user", wherClause);

					while (resultSet.next()) {
						if (resultSet.getString(3).equals(pwd)) {
							valid = true;
							firstname = resultSet.getString(4);
							dbRole = resultSet.getString(6);
						}

					}
				} catch (Exception e) {

					if (e.getMessage().contains("f16g324_user' doesn't exist")) {
						returnMsg = "Admin";
						if (user.equalsIgnoreCase("admin") && pwd.equalsIgnoreCase("admin")) {
							return returnMsg;
						}
					}
				}

				if (valid) {
					HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
							.getSession(false);
					session.setAttribute("username", user);
					session.setAttribute("firstname", firstname);
					int activityId = serv.addActivity(user);
					session.setAttribute("actID", activityId);

					returnMsg = role + "";
					
					if(role.equalsIgnoreCase("Admin")||role.equalsIgnoreCase("Teacher"))
						if(dbRole.equalsIgnoreCase("Student"))
						 {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"*User Registered as Student. Please select role as student", "Please enter correct username and Password"));
							returnMsg = "login";
							this.role = "Student";
						}

						
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
							"*Incorrect Username and Password", "Please enter correct username and Password"));
					returnMsg = "login";
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN,
								"DataBase server is Down. Please try after sometime",
								"DataBase server is Down. Please try after sometime"));

				returnMsg = "login";
			}
		} catch (Exception e) {
			e.printStackTrace();
			isError = true;
			returnMsg = "error";
		} finally {
			dao1.closeConnection();
			if (isError) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Unexpected Error. Please contact system admninistrator.",
								"Please contact system administrator"));
			}
		}
		return returnMsg;

	}

	// logout event, invalidate session
	public String logout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		String actID =  session.getAttribute("actID") +"";
		DBDao dao1 = new DBDao(dbUserNameForTestSchema, dbPasswordForTestSchema,
				"jdbc:mysql://" + LoginUserBean.host + ":3306/", testSchema, mysqlJdbcDriver);
		try {
			dao1.createConnection();
			DBService s = new DBService(dao1);
			s.addEndTime(actID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.invalidate();
		return "login";
	}

}
