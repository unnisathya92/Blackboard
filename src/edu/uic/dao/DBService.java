package edu.uic.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBService {

	Connection connection;
	DatabaseMetaData databaseMetaData;
	Statement statement;
	ResultSet resultSet, rs;
	ResultSetMetaData resultSetMetaData;
	String userName;

	public DBService(DBDao dao) {
		this.connection = dao.getConnection();
		this.userName = dao.getUserName();
	}

	public ResultSet fetchDataFromTable(String tablename, int limit, int offset) throws SQLException {

		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		if (limit != -1 && offset != -1)
			resultSet = statement.executeQuery("select * from " + tablename + " limit " + limit + " offset " + offset);
		else
			resultSet = statement.executeQuery("select * from " + tablename);

		return resultSet;
	}

	public int returnNoOfTests() throws SQLException {

		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		resultSet = statement.executeQuery("select count(testnumber) from f16g324_test");
		if (resultSet.next())
			return resultSet.getInt(1);
		else
			return 0;
	}

	public String returnLastAccess(String UIN) throws SQLException {

		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		resultSet = statement.executeQuery("SELECT enddatetime,max(id) FROM f16g324_activity where username = " + UIN);
		if (resultSet.next())
			return resultSet.getString(1);
		else
			return "";
	}

	public ResultSet fetchDataFromTable(String tablename, String wherClause) throws SQLException {

		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		resultSet = statement.executeQuery("select * from " + tablename + " where " + wherClause);

		return resultSet;
	}

	public void closeStmtResultSet() {
		try {
			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int fetchMaxValueForActivity() throws SQLException {
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		resultSet = statement.executeQuery("select max(id) from f16g324_activity");
		if (resultSet.next())
			return resultSet.getInt(1);
		else
			return 0;
	}

	public String createTable(String tableName) {
		try {
			if (tableName.equalsIgnoreCase("f16g324_user")) {
				String sql = "CREATE TABLE `f16g324_user` (  `UIN` int(11) NOT NULL,  `username` varchar(45) NOT NULL,  `password` varchar(45) NOT NULL,  `firstname` varchar(45) NOT NULL,  `lastname` varchar(45) NOT NULL,  `role` varchar(45) DEFAULT NULL,  PRIMARY KEY (`UIN`),  UNIQUE KEY `username_UNIQUE` (`username`),  UNIQUE KEY `UIN_UNIQUE` (`UIN`))";
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				statement.executeUpdate(sql);
				sql = "INSERT INTO  f16g324_user (`UIN`, `username`, `password`, `firstname`, `lastname`, `role`) VALUES ('1', 'admin', 'admin', 'admin', 'admin', 'Admin')";
				statement.executeUpdate(sql);
				sql = "INSERT INTO f16g324_user (`UIN`, `username`, `password`, `firstname`, `lastname`, `role`) VALUES ('2', 'teacher', 'teacher', 'teacher', 'teacher', 'Teacher')";
				statement.executeUpdate(sql);
			} else if (tableName.equalsIgnoreCase("f16g324_course")) {
				String sql = "CREATE TABLE `f16g324_course` (  `crn` int(11) NOT NULL,  `coursecode` int(11) NOT NULL,  `coursename` varchar(45) NOT NULL,  `coursedescription` varchar(500) DEFAULT NULL,  PRIMARY KEY (`crn`),  UNIQUE KEY `crn_UNIQUE` (`crn`))";
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				statement.executeUpdate(sql);
				sql = "INSERT INTO f16g324_course (`crn`, `coursecode`, `coursename`, `coursedescription`) VALUES ('12345', '1', 'IDS517', 'EAD')";
				statement.executeUpdate(sql);
			} else if (tableName.equalsIgnoreCase("f16g324_test")) {
				String sql = "CREATE TABLE `f16g324_test` (  `testnumber` int(11) NOT NULL,  `crn` int(11) NOT NULL,  `coursecode` int(11) NOT NULL,  `startdate` varchar(25) NOT NULL,  `enddate` varchar(25) NOT NULL,  `isExamAvailable` int(1) DEFAULT '1',  `durationmin` int(5) DEFAULT NULL,  `totalmarks` int(5) DEFAULT NULL,  PRIMARY KEY (`testnumber`),  UNIQUE KEY `testnumber_UNIQUE` (`testnumber`),  KEY `crn_idx` (`crn`),  CONSTRAINT `crn` FOREIGN KEY (`crn`) REFERENCES `f16g324_course` (`crn`) ON DELETE NO ACTION ON UPDATE NO ACTION)";
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				statement.executeUpdate(sql);
				//
				sql = "INSERT INTO f16g324_test (`testnumber`, `crn`, `coursecode`, `startdate`, `enddate`, `isExamAvailable`, `durationmin`, `totalmarks`) VALUES ('1', '12345', '1', '2016-10-10', '2016-12-12', '1', '30', '250')";
				statement.executeUpdate(sql);
				sql = "INSERT INTO f16g324_test (`testnumber`, `crn`, `coursecode`, `startdate`, `enddate`, `isExamAvailable`, `durationmin`, `totalmarks`) VALUES ('2', '12345', '1', '2016-10-10', '2016-12-12', '1', '30', '250')";
				statement.executeUpdate(sql);
				sql = "INSERT INTO f16g324_test (`testnumber`, `crn`, `coursecode`, `startdate`, `enddate`, `isExamAvailable`, `durationmin`, `totalmarks`) VALUES ('3', '12345', '1', '2016-10-10', '2016-12-12', '1', '30', '250')";
				statement.executeUpdate(sql);
				sql = "INSERT INTO f16g324_test (`testnumber`, `crn`, `coursecode`, `startdate`, `enddate`, `isExamAvailable`, `durationmin`, `totalmarks`) VALUES ('4', '12345', '1', '2016-10-10', '2016-12-12', '1', '30', '250')";
				statement.executeUpdate(sql);
				
			} else if (tableName.equalsIgnoreCase("f16g324_question")) {
				String sql = "CREATE TABLE `f16g324_question` (  `id` int(11) NOT NULL AUTO_INCREMENT,  `testid` int(11) NOT NULL,  `questiontype` varchar(15) DEFAULT NULL,`questiontext` varchar(1000) NOT NULL,  `correctanswer` varchar(100) NOT NULL,  `tolerance` double DEFAULT NULL,  `qno` int(15) NOT NULL,  `weightage` int(10) DEFAULT '1',  PRIMARY KEY (`id`),  KEY `testid_idx` (`testid`),  CONSTRAINT `testid` FOREIGN KEY (`testid`) REFERENCES `f16g324_test` (`testnumber`) ON DELETE NO ACTION ON UPDATE NO ACTION)";
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				statement.executeUpdate(sql);
			} else if (tableName.equalsIgnoreCase("f16g324_student")) {
				String sql = "CREATE TABLE `f16g324_student` (  `student_uin_fk` int(11) NOT NULL, `firstname` varchar(45) NOT NULL, `lastname` varchar(45) NOT NULL, `username_fk` varchar(45) NOT NULL, `testscore_xml` longtext , `feedback_xml` longtext, `availability` varchar(10) DEFAULT NULL,`lastaccess` varchar(45) DEFAULT NULL, UNIQUE KEY `student_uin_fk_UNIQUE` (`student_uin_fk`), KEY `username_idx` (`username_fk`),  KEY `student_id_idx` (`student_uin_fk`,`username_fk`),CONSTRAINT `student_id` FOREIGN KEY (`student_uin_fk`) REFERENCES `f16g324_user` (`UIN`) ON DELETE NO ACTION ON UPDATE NO ACTION,  CONSTRAINT `username_fk` FOREIGN KEY (`username_fk`) REFERENCES `f16g324_user` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION	)";
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				statement.executeUpdate(sql);
			} else if (tableName.equalsIgnoreCase("f16g324_activity")) {
				String sql = "CREATE TABLE `f16g324_activity` (  `username` varchar(45) DEFAULT NULL,  `startdatetime` varchar(75) DEFAULT NULL,  `enddatetime` varchar(70) DEFAULT NULL,  `actionxml` varchar(1000) DEFAULT NULL,  `id` int(11) NOT NULL,  PRIMARY KEY (`id`))";
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				statement.executeUpdate(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "FAIL";
		}
		return "SUCCESS";
	}

	public int addActivity(String user) throws SQLException {
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

		int max = fetchMaxValueForActivity() + 1;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String s = dateFormat.format(date);
		String sql = "INSERT INTO f16g324_activity VALUES ('" + user + "', '" + s + "', '', '' ,'" + max + "')";
		statement.executeUpdate(sql);
		return max;
	}

	public void addEndTime(String actID) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String s = dateFormat.format(date);
			String sql = "update f16g324_activity set enddatetime ='" + s + "' where id = "+actID;
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
