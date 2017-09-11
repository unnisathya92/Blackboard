package edu.uic.model.bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.io.IOUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;

import edu.uic.dao.DBDao;
import edu.uic.dao.DBService;

@ManagedBean
@SessionScoped
public class CourseBean implements Serializable {
	private UploadedFile uploadedFile;
    private String fileName;
	private static final long serialVersionUID = 1094801825228386363L;
	private String coursename;
	private String coursedescription;
	private int crn;
	private int courseCode;
	


	private List<CourseBean> courseBeanList;

	private static final String dbUserNameForTestSchema = "f16g324";
	private static final String dbPasswordForTestSchema = "g324Xp1bjr";
	private static final String mysqlJdbcDriver = "com.mysql.jdbc.Driver";
	private static final String testSchema = "f16g324";


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

	

	public List<CourseBean> getCourseBeanList() {
		return courseBeanList;
	}

	public void setCourseBeanList(List<CourseBean> courseBeanList) {
		this.courseBeanList = courseBeanList;
	}

	public CourseBean(int k) {
	}

	public CourseBean() {
		DBDao dao1 = new DBDao(dbUserNameForTestSchema, dbPasswordForTestSchema, "jdbc:mysql://"+LoginUserBean.host+":3306/",
				testSchema, mysqlJdbcDriver);
		try {

			dao1.createConnection();
			if (dao1.getConnection() != null) {
				DBService serv = new DBService(dao1);
				ResultSet resultSet = serv.fetchDataFromTable("f16g324_course", -1, -1);
				this.courseBeanList = new ArrayList<CourseBean>();
				CourseBean b = new CourseBean(1);
				while (resultSet.next()) {
					if (resultSet.getString(1) != null) {
						b.setCrn(Integer.parseInt(resultSet.getString(1)));
						b.setCourseCode(Integer.parseInt(resultSet.getString(2)));
						b.setCoursename(resultSet.getString(3));
						b.setCoursedescription(resultSet.getString(4));

						this.courseBeanList.add(b);
						b = new CourseBean(1);
					}

				}

			}
		} catch (NumberFormatException e) {
		} catch (SQLException e) {
		} catch (ClassNotFoundException e) {
		} finally {
			dao1.closeConnection();
		}

	}

	public String getCoursename() {
		return coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}

	public String getCoursedescription() {
		return coursedescription;
	}

	public void setCoursedescription(String coursedescription) {
		this.coursedescription = coursedescription;
	}



	public String submit() {
		
		DBDao dao1 = new DBDao(dbUserNameForTestSchema, dbPasswordForTestSchema, "jdbc:mysql://"+LoginUserBean.host+":3306/",
				testSchema, mysqlJdbcDriver);
		
        OutputStream output = null;
      //  String returnMSG="success";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(uploadedFile.getInputStream()));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            reader.close();
            String[] tokens = out.toString().split("\t");
            dao1.createConnection();
		String query= "INSERT INTO f16g324_course VALUES (";
		
				Statement stmt;
				int count =1;
            for(int i=4;i<tokens.length;i++){
            	if(count==4){            		
            		query=query+"'"+tokens[i]+"')";
            		 stmt = dao1.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            		 stmt.executeUpdate(query);
            		 count=0;
            		 query= "INSERT INTO course VALUES (";
            	}
            	else {query=query+"'"+tokens[i]+"',";
            	count++;
            	}
            }
            new CourseBean();
            
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage("uploadForm", new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "File upload failed with I/O error.", null));

            e.printStackTrace();
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			 FacesContext.getCurrentInstance().addMessage("uploadForm", new FacesMessage(
		                FacesMessage.SEVERITY_ERROR, e.getMessage(), null));		
		} finally {
            IOUtils.closeQuietly(output);
        }
		return "success";
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

}

