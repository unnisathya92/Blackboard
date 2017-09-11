package edu.uic.model.bean;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import edu.uic.dao.DBDao;

@ManagedBean
@SessionScoped
public class ChartBean {

	/*
	 * Note: in this example all methods are static - not appropriate in
	 * general.
	 */
	public ChartBean() {
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
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

	public String piechart() {
		JFreeChart chart = createPieChart();

		if (stu_marks.isEmpty()) {
			FacesMessage deleteMessage = new FacesMessage("There are no charts to view !!");
			FacesContext.getCurrentInstance().addMessage(null, deleteMessage);
			barrender = false;
			pierender = false;
		} else {
			try {
				FacesContext fc = FacesContext.getCurrentInstance();
				String path = fc.getExternalContext().getRealPath("/temp");
				File out = null;
				File f;
				try {
					f = new File(path);
					if (!f.exists())
						f.mkdirs();
				} catch (Exception e) {
					e.printStackTrace();
				}
				out = new File(path + "/" + "pieChart.png");
				ChartUtilities.saveChartAsPNG(out, chart, 1200, 800);
				finalPath = "/temp/pieChart.png";
				barrender = false;
				pierender = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "Success";
	}

	public String barChart() {

		JFreeChart chart = createBarChart();

		if (stu_marks.isEmpty()) {
			FacesMessage deleteMessage = new FacesMessage("There are no charts to view !!");
			FacesContext.getCurrentInstance().addMessage(null, deleteMessage);
			barrender = false;
			pierender = false;
		} else {
			try {
				FacesContext fc = FacesContext.getCurrentInstance();
				String path = fc.getExternalContext().getRealPath("/temp");
				File out = null;
				File f;
				try {
					f = new File(path);
					if (!f.exists())
						f.mkdirs();
				} catch (Exception e) {
					e.printStackTrace();
				}
				out = new File(path + "/" + "barChart.png");
				ChartUtilities.saveChartAsPNG(out, chart, 1200, 800);
				finalPath = "/temp/barChart.png";
				barrender = true;
				pierender = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "Success";
	}

	public JFreeChart createBarChart() {
		calculate();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int i = 0;
		while (i < stu_grades.size()) {

			dataset.addValue(stu_marks.get(i), stu_grades.get(i), "Students Grade");
			i++;
		}
		JFreeChart chart = ChartFactory.createBarChart3D("Bar Chart", "Grades", "Number Of Students", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		return chart;
	}

	private void calculate() {
		try {
			int a = 0, b = 0, c = 0, d = 0, e = 0;
			DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
					"jdbc:mysql://" + LoginUserBean.host + ":3306/", LoginUserBean.testSchema,
					LoginUserBean.mysqlJdbcDriver);
			dao1.createConnection();
			String query = "select testscore_xml from f16g324_student ";
			ResultSet rs = dao1.getConnection().createStatement().executeQuery(query);
			query = "select sum(totalmarks) from f16g324_test ";
			ResultSet rs1 = dao1.getConnection().createStatement().executeQuery(query);
			Double total = new Double(0);
			while (rs1.next()) {
				total = (double) rs1.getInt(1);
			}
			stu_grades = new ArrayList<String>();
			stu_marks = new ArrayList<Integer>();
			Double mark = new Double(0);

			while (rs.next()) {
				try {
					if ((mark = parseTestScore(rs.getString(1))) != -1) {
					
						System.err.println(mark / total * 100);

						if (mark / total * 100 > 90)
							a++;
						else if (mark / total * 100 > 80)
							b++;
						else if (mark/ total * 100 > 70)
							c++;
						else if (mark / total * 100 > 60)
							d++;
						else
							e++;

					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
			stu_grades.add("A");
			stu_marks.add(a);
			stu_grades.add("B");
			stu_marks.add(b);
			stu_grades.add("C");
			stu_marks.add(c);
			stu_grades.add("D");
			stu_marks.add(d);
			stu_grades.add("E");
			stu_marks.add(e);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private Double parseTestScore(String toParse) {
		Double returnValue = new Double(-1);
		try {
			String t = toParse.substring(toParse.indexOf("<test1>") + 7, toParse.indexOf("</test1>"));
			String t1 = toParse.substring(toParse.indexOf("<test2>") + 7, toParse.indexOf("</test2>"));

			String t2 = toParse.substring(toParse.indexOf("<test3>") + 7, toParse.indexOf("</test3>"));

			String t3 = toParse.substring(toParse.indexOf("<project>") + 9, toParse.indexOf("</project>"));

			returnValue = Double.parseDouble(t)+Double.parseDouble(t1)+Double.parseDouble(t2)+Double.parseDouble(t3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	public JFreeChart createPieChart() {

		calculate();
		DefaultPieDataset data = new DefaultPieDataset();
		int i = 0;
		while (i < stu_grades.size()) {

			data.setValue(stu_grades.get(i), new Double(stu_marks.get(i)));
			i++;
		}
		JFreeChart chart = ChartFactory.createPieChart("Pie Chart", data, true, true, false);
		return chart;

	}

	public String graphicalAnalysis() {

		return "graphical";

	}

	private String xySeriesChartFile;
	private String timeSeriesChartFile;
	String pathWebContent = "/temp/";
	private int testNumber;

	public static ArrayList<String> getAssess_names() {
		return stu_grades;
	}

	public static void setAssess_names(ArrayList<String> assess_names) {
		ChartBean.stu_grades = assess_names;
	}

	public static ArrayList<Integer> getAssess_marks() {
		return stu_marks;
	}

	public static void setAssess_marks(ArrayList<Integer> assess_marks) {
		ChartBean.stu_marks = assess_marks;
	}

	private boolean pierender = false;
	private boolean barrender = false;

	public boolean isPierender() {
		return pierender;
	}

	public void setPierender(boolean pierender) {
		this.pierender = pierender;
	}

	public boolean isBarrender() {
		return barrender;
	}

	public void setBarrender(boolean barrender) {
		this.barrender = barrender;
	}

	public static ArrayList<String> getStu_names() {
		return stu_grades;
	}

	public static void setStu_names(ArrayList<String> stu_names) {
		ChartBean.stu_grades = stu_names;
	}

	public static ArrayList<Integer> getStu_marks() {
		return stu_marks;
	}

	public static void setStu_marks(ArrayList<Integer> stu_marks) {
		ChartBean.stu_marks = stu_marks;
	}

	private static ArrayList<String> stu_grades;
	private static ArrayList<Integer> stu_marks;

	public String getTimeSeriesChartFile() {
		return timeSeriesChartFile;
	}

	public void setTimeSeriesChartFile(String timeSeriesChartFile) {
		this.timeSeriesChartFile = timeSeriesChartFile;
	}

	public String getPathWebContent() {
		return pathWebContent;
	}

	public void setPathWebContent(String pathWebContent) {
		this.pathWebContent = pathWebContent;
	}

	public int getTestNumber() {
		return testNumber;
	}

	public void setTestNumber(int testNumber) {
		this.testNumber = testNumber;
	}

	public void setXySeriesChartFile(String xySeriesChartFile) {
		this.xySeriesChartFile = xySeriesChartFile;
	}

	public String getXySeriesChartFile() {
		return xySeriesChartFile;
	}

	private String finalPath;

	public String getFinalPath() {
		return finalPath;
	}

	private ArrayList<TestBean> testList;

	public ArrayList<TestBean> getTestList() {
		return testList;
	}

	public void setTestList(ArrayList<TestBean> testList) {
		this.testList = testList;
	}

	public void setFinalPath(String finalPath) {
		this.finalPath = finalPath;
	}

}
