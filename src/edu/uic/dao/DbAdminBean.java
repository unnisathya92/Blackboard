package edu.uic.dao;

import java.io.Serializable;
//import java.util.DatabaseUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;

import edu.uic.model.bean.ActivityBean;
import edu.uic.model.bean.ColumnBean;
import edu.uic.model.bean.LoginUserBean;

@ManagedBean
@SessionScoped
public class DbAdminBean implements Serializable {

	public static String dbUserNameForTestSchema;
	public static String dbPasswordForTestSchema;
	public static final String mysqlJdbcDriver = "com.mysql.jdbc.Driver";
	public static String testSchema;
	private List<ColumnBean> listOfClmns = new ArrayList<ColumnBean>();;
	private List<ActivityBean> activities = new ArrayList<ActivityBean>();;

	public List<ActivityBean> getActivities() {
		return activities;
	}

	public void setActivities(List<ActivityBean> activities) {
		this.activities = activities;
	}

	public List<ColumnBean> getListOfClmns() {
		return listOfClmns;
	}

	public void setListOfClmns(List<ColumnBean> listOfClmns) {
		this.listOfClmns = listOfClmns;
	}

	public static String getDbUserNameForTestSchema() {
		return dbUserNameForTestSchema;
	}

	public static void setDbUserNameForTestSchema(String dbUserNameForTestSchema) {
		DbAdminBean.dbUserNameForTestSchema = dbUserNameForTestSchema;
	}

	public static String getDbPasswordForTestSchema() {
		return dbPasswordForTestSchema;
	}

	public static void setDbPasswordForTestSchema(String dbPasswordForTestSchema) {
		DbAdminBean.dbPasswordForTestSchema = dbPasswordForTestSchema;
	}

	public static String getTestSchema() {
		return testSchema;
	}

	public static void setTestSchema(String testSchema) {
		DbAdminBean.testSchema = testSchema;
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	


	// public void setTableSelected(String tableSelected) {
	// this.tableSelected = tableSelected;
	// }

	private Statement statement;
	private ResultSet rs;
	private ResultSetMetaData meta;


	public List<String> getListOfTables() {
		return listOfTables;
	}

	public void setListOfTables(List<String> listOfTables) {
		this.listOfTables = listOfTables;
	}

	public List<String> getListOfColumns() {
		return listOfColumns;
	}

	public void setListOfColumns(List<String> listOfColumns) {
		this.listOfColumns = listOfColumns;
	}



	List<String> listOfTables;
	List<String> listOfColumns;


	private String process;

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	

	private String tableNames;

	public String getTableNames() {
		return tableNames;
	}

	public void setTableNames(String tableNames) {
		this.tableNames = tableNames;
	}

	private String list;

	public void setList(String list) {
		this.list = list;
	}

	public String getList() {
		return list;
	}


	private Boolean isColumnTableRendered = false;

	public Boolean getIsColumnTableRendered() {
		return isColumnTableRendered;
	}

	public void setIsColumnTableRendered(Boolean isColumnTableRendered) {
		this.isColumnTableRendered = isColumnTableRendered;
	}

	public String columnList() {
		int count = 1;
		if (listOfTables.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Please select atleast one table to Proceed", "Connection failed"));

		} else {
			DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
					"jdbc:mysql://" + LoginUserBean.host + ":3306/", LoginUserBean.testSchema,
					LoginUserBean.mysqlJdbcDriver);
			try {

				listOfClmns = new ArrayList<ColumnBean>();
				for (String tableName : listOfTables) {
					dao1.createConnection();

					statement = (Statement) dao1.getConnection().createStatement(ResultSet.CONCUR_UPDATABLE,
							ResultSet.TYPE_SCROLL_INSENSITIVE);
					try {
						rs = statement.executeQuery("select * from " + tableName);
						meta = (ResultSetMetaData) rs.getMetaData();
						ColumnBean b;
						for (int i = 1; i <= meta.getColumnCount(); i++) {
							b = new ColumnBean();
							b.setSrNo(count + "");
							b.setColumnName(meta.getColumnName(i));
							b.setTableName(tableName);
							b.setColumnType(meta.getColumnTypeName(i));
							listOfClmns.add(b);
							count++;
						}
					} catch (SQLException e) {
						e.printStackTrace();
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										tableName + " is not Present . Please create tables before you proceed",
										"Connection failed"));

					}

				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				dao1.closeConnection();
			}
		}
		if (count > 0)
			{isColumnTableRendered = true;
			isActivityRendered = false;
			}
		else
			isColumnTableRendered = false;

		return "Success";

	}

	private Boolean isActivityRendered = false;

	public Boolean getIsActivityRendered() {
		return isActivityRendered;
	}

	public void setIsActivityRendered(Boolean isActivityRendered) {
		this.isActivityRendered = isActivityRendered;
	}

	public void activityLogs() {
		isColumnTableRendered = false;
		isActivityRendered = true;

		DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
				"jdbc:mysql://" + LoginUserBean.host + ":3306/", LoginUserBean.testSchema,
				LoginUserBean.mysqlJdbcDriver);
		try {

			dao1.createConnection();

			statement = (Statement) dao1.getConnection().createStatement(ResultSet.CONCUR_UPDATABLE,
					ResultSet.TYPE_SCROLL_INSENSITIVE);
			try {
				rs = statement.executeQuery("select * from f16g324_activity");
				activities = new ArrayList<ActivityBean>();
				ActivityBean b;
				while (rs.next()) {
					b = new ActivityBean();
					b.setId(rs.getString(5));
					b.setEndTime(rs.getString(3));
					b.setStartTime(rs.getString(2));
					b.setUsername(rs.getString(1));
					activities.add(b);
				}
				if(activities.isEmpty())
					isActivityRendered=false;
			} catch (SQLException e) {
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), "Connection failed"));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			dao1.closeConnection();
		}

	}

	public String createTable() {
		int count = 0;
		String list = "";
		if (listOfTables.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Please select atleast one table to Proceed", "Connection failed"));

		} else {
			DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
					"jdbc:mysql://" + LoginUserBean.host + ":3306/", LoginUserBean.testSchema,
					LoginUserBean.mysqlJdbcDriver);
			DBService serv;
			try {
				dao1.createConnection();
				serv = new DBService(dao1);
				int k = listOfTables.size() - 1;
				for (int i = k; i >= 0; i--) {
					String s = serv.createTable(listOfTables.get(i));
					if (s.equalsIgnoreCase("SUCCESS"))
						count++;
					else
						list = list + listOfTables.get(i) + " ,";
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				dao1.closeConnection();
			}
			if (count != 0) {
				if (list.length() > 0) {
					list = "and Tables " + list + "are already present";
				} else
					list = "";
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						count + " table(s) created successfully " + list, "Connection failed"));

			}
		}
		return "Success";
	}

	public String dropTable() {
		int count = 0;
		if (listOfTables.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Please select atleast one table to Proceed", "Connection failed"));

		} else {
			DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
					"jdbc:mysql://" + LoginUserBean.host + ":3306/", LoginUserBean.testSchema,
					LoginUserBean.mysqlJdbcDriver);
			try {

				listOfClmns = new ArrayList<ColumnBean>();
				for (String tableName : listOfTables) {
					dao1.createConnection();
					statement = (Statement) dao1.getConnection().createStatement(ResultSet.CONCUR_UPDATABLE,
							ResultSet.TYPE_SCROLL_INSENSITIVE);
					try {
						statement.executeUpdate("Drop table " + tableName);
						count++;
					} catch (SQLException e) {
						e.printStackTrace();
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										tableName + " is not Present . Please create tables before you proceed",
										"Connection failed"));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				dao1.closeConnection();
			}
			if (count != 0)
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						count + " table(s) dropped successfully", "Connection failed"));
		}
		return "Success";
	}
}
