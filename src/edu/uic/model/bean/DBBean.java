package edu.uic.model.bean;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import edu.uic.dao.DBDao;

@ManagedBean
@SessionScoped
public class DBBean implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;
	private String driver ;
	private String schema ;
	private String pwd;
	private String host ;
	private String user;
	private ArrayList<String> deleteTablesList = new ArrayList<String>();
	private String tableToBeDeleted;
	private String tableName;
	private String[] chosenTables;

	public ArrayList<String> getDeleteTablesList() {
		return deleteTablesList;
	}

	public void setDeleteTablesList(ArrayList<String> deleteTablesList) {
		this.deleteTablesList = deleteTablesList;
	}

	public String getTableToBeDeleted() {
		return tableToBeDeleted;
	}

	public void setTableToBeDeleted(String tableToBeDeleted) {
		this.tableToBeDeleted = tableToBeDeleted;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String[] getChosenTables() {
		return chosenTables;
	}

	public void setChosenTables(String[] chosenTables) {
		this.chosenTables = chosenTables;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	private String role ;

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String testSchema) {
		this.schema = testSchema;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	// validate login
	public String validateDBAccess() {
		String errorMsg = "";
		boolean isError = false;
		String returnMsg = "success";
		
	DBDao dao1 = new DBDao(this.user, this.pwd, "jdbc:mysql://"+this.host+":3306/",this.schema, this.driver);
	LoginUserBean.dbUserNameForTestSchema=this.user;
	LoginUserBean.dbPasswordForTestSchema=this.pwd;
	LoginUserBean.testSchema=this.schema;
	LoginUserBean.host=this.host;

		try {

			dao1.createConnection();
			if (dao1.getConnection() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"DataBase server is Down. Please try after sometime",
								"DataBase server is Down. Please try after sometime"));
				returnMsg = "index";
			}
		} catch (SQLException e) {
			isError = true;
			errorMsg = e.getMessage();
			e.printStackTrace();
			if(errorMsg.contains("Communications link failure"))
				errorMsg = "*Unable to connect to the database at "+this.host;

			returnMsg = "index";
		} catch (ClassNotFoundException e) {
			isError = true;
			errorMsg = '*'+e.getMessage()+" not found";
			returnMsg = "index";
			
		} finally {
			dao1.closeConnection();
			if (isError) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMsg, "Please contact system administrator"));
			}
	
		}
		return returnMsg;
	}

	// logout event, invalidate session
	public String logout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();
		return "login";
	}
	

}
