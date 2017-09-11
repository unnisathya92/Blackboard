package edu.uic.model.bean;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import edu.uic.dao.DBDao;

@ManagedBean
@SessionScoped
public class StatisticalBean implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	public StatisticalBean() {
		testList = new ArrayList<TestBean>();
		DBDao dao1 = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
				"jdbc:mysql://"+LoginUserBean.host+":3306/", LoginUserBean.testSchema, LoginUserBean.mysqlJdbcDriver);
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

	public String numericAnalysis() {
		try{
			examMarks = returnMarksForExam();
			minValue = StatUtils.min(examMarks);
			maxValue = StatUtils.max(examMarks);
			mean = StatUtils.mean(examMarks);
			variance = StatUtils.variance(examMarks, mean);
			std = Math.sqrt(variance);
			median = StatUtils.percentile(examMarks, 50.0);
			q1 = StatUtils.percentile(examMarks, 25.0);
			q3 = StatUtils.percentile(examMarks, 75.0);
			iqr = q3 - q1;
			range = maxValue - minValue;
			rendered = true;
			renderedForRegressionAnalysis = false;}
			catch(Exception e ){
				e.printStackTrace();
			}
			return "statistical";
	}

	public String descriptiveAnalysis() {
		
		rendered = true;
		renderedForRegressionAnalysis = false;


		return "statistical";

	}

	public String numericalAnalysis() {
		rendered = false;
		renderedForRegressionAnalysis = true;
Object returnValue[] = returnTwoMarksForExam();

PearsonsCorrelation c = new PearsonsCorrelation();
SimpleRegression sr = new SimpleRegression();

sr.clear();
double[] first = (double[]) returnValue[0];
double[] second = (double[]) returnValue[1];

for(int k=0; k<first.length; k++) {
sr.addData(first[k], second[k]);
}
	intercept = sr.getIntercept();
	slope=sr.getSlope();
	rSquare=sr.getRSquare();
	significance=sr.getSignificance();
	interceptStdErr=sr.getInterceptStdErr();
	slopeStdErr=sr.getSlopeStdErr();
	slopeConfidenceLevel=sr.getSlopeConfidenceInterval();
	regressionSumSquares=sr.getRegressionSumSquares();
	totalSumSquares=sr.getTotalSumSquares();
	sumSquaredErrors=sr.getSumSquaredErrors();
	
	
	correlation = c.correlation(first, second);
		return "statistical";

	}

	private double[] returnMarksForExam() {
		DBDao dao = null;
		double[] returnValue = null;
		try {
			dao = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
					"jdbc:mysql://"+LoginUserBean.host+":3306/", LoginUserBean.testSchema, LoginUserBean.mysqlJdbcDriver);
			dao.createConnection();
			ResultSet rs = dao.getConnection().createStatement()
					.executeQuery("select count(student_uin_fk) from f16g324_student");
			rs.next();
			returnValue = new double[rs.getInt(1)];
			int count = 0;
			rs = dao.getConnection().createStatement().executeQuery("select testscore_xml from f16g324_student");
			while (rs.next()) {
				try {
					String toParse = rs.getString(1);
					int start = toParse.indexOf("<total>") + 7;
					int end = toParse.indexOf("</total>");

					String s = toParse.substring(start, end);
					returnValue[count] = Double.parseDouble(s);
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dao != null)
				dao.closeConnection();
		}
		return returnValue;
	}

	private Object[] returnTwoMarksForExam() {
		DBDao dao = null;
		Object[] returnValue = new Object[2];
		double[] first = null;
		double[] second = null;

		try {
			dao = new DBDao(LoginUserBean.dbUserNameForTestSchema, LoginUserBean.dbPasswordForTestSchema,
					"jdbc:mysql://"+LoginUserBean.host+":3306/", LoginUserBean.testSchema, LoginUserBean.mysqlJdbcDriver);
			dao.createConnection();
			ResultSet rs = dao.getConnection().createStatement()
					.executeQuery("select count(student_uin_fk) from f16g324_student");
			rs.next();
			first = new double[rs.getInt(1)];
			second = new double[rs.getInt(1)];

			int count = 0;
			rs = dao.getConnection().createStatement().executeQuery("select testscore_xml from f16g324_student");
			while (rs.next()) {
				try {
					int start,end,secStart,secEnd;
					String toParse = rs.getString(1);
				

					if(testNumber != 4){
					 start = toParse.indexOf("<test" + testNumber + ">") + 7;
					 end = toParse.indexOf("</test" + testNumber + ">");
					}
					else {
						 start = toParse.indexOf("<project>") + 9;
						 end = toParse.indexOf("</project>");
					}
					if(secTestNumber != 4){

					 secStart = toParse.indexOf("<test" + secTestNumber + ">") + 7;
					 secEnd = toParse.indexOf("</test" + secTestNumber + ">");
					} else {
						 secStart = toParse.indexOf("<project>") + 9;
						 secEnd = toParse.indexOf("</project>");
					}
					  

					String s = toParse.substring(start, end);
					String ss = toParse.substring(secStart, secEnd);
					if (s.equalsIgnoreCase("NA"))
						s = "0";
					if (ss.equalsIgnoreCase("NA"))
						ss = "0";
					first[count] = Double.parseDouble(s);
					second[count] = Double.parseDouble(ss);

					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dao != null)
				dao.closeConnection();
		}
		returnValue[0]=first;
		returnValue[1]=second;

		return returnValue;
	}

	private int testNumber;
	private int secTestNumber;

	private double minValue;
	private double maxValue;
	private double mean;
	private double variance;
	private double std;
	private double median;
	private double q1;
	private double q3;
	private double iqr;
	private double range;
	double[] examMarks;
	
	private double intercept;
	private double slope;
	private double rSquare;
	private double significance;
	private double interceptStdErr;
	private double slopeStdErr;
	private double slopeConfidenceLevel;
	private double  regressionSumSquares;
	private double  totalSumSquares;
	private double sumSquaredErrors;
	private double correlation;
	public double getCorrelation() {
		return correlation;
	}

	public void setCorrelation(double correlation) {
		this.correlation = correlation;
	}

	public double getRegressionSumSquares() {
		return regressionSumSquares;
	}

	public void setRegressionSumSquares(double regressionSumSquares) {
		this.regressionSumSquares = regressionSumSquares;
	}

	public double getTotalSumSquares() {
		return totalSumSquares;
	}

	public void setTotalSumSquares(double totalSumSquares) {
		this.totalSumSquares = totalSumSquares;
	}

	public double getSumSquaredErrors() {
		return sumSquaredErrors;
	}

	public void setSumSquaredErrors(double sumSquaredErrors) {
		this.sumSquaredErrors = sumSquaredErrors;
	}

	
	
	public double getSlopeConfidenceLevel() {
		return slopeConfidenceLevel;
	}

	public void setSlopeConfidenceLevel(double slopeConfidenceLevel) {
		this.slopeConfidenceLevel = slopeConfidenceLevel;
	}

	public double getIntercept() {
		return intercept;
	}

	public void setIntercept(double intercept) {
		this.intercept = intercept;
	}

	public double getSlope() {
		return slope;
	}

	public void setSlope(double slope) {
		this.slope = slope;
	}

	public double getrSquare() {
		return rSquare;
	}

	public void setrSquare(double rSquare) {
		this.rSquare = rSquare;
	}

	public double getSignificance() {
		return significance;
	}

	public void setSignificance(double significance) {
		this.significance = significance;
	}

	public double getInterceptStdErr() {
		return interceptStdErr;
	}

	public void setInterceptStdErr(double interceptStdErr) {
		this.interceptStdErr = interceptStdErr;
	}

	public double getSlopeStdErr() {
		return slopeStdErr;
	}

	public void setSlopeStdErr(double slopeStdErr) {
		this.slopeStdErr = slopeStdErr;
	}

	private ArrayList<TestBean> testList;

	public int getSecTestNumber() {
		return secTestNumber;
	}

	public void setSecTestNumber(int secTestNumber) {
		this.secTestNumber = secTestNumber;
	}

	public double[] getExamMarks() {
		return examMarks;
	}

	public void setExamMarks(double[] examMarks) {
		this.examMarks = examMarks;
	}

	private boolean rendered = false;
	private boolean renderedForRegressionAnalysis =false;
	public boolean isRenderedForRegressionAnalysis() {
		return renderedForRegressionAnalysis;
	}

	public void setRenderedForRegressionAnalysis(boolean renderedForRegressionAnalysis) {
		this.renderedForRegressionAnalysis = renderedForRegressionAnalysis;
	}

	public boolean getRendered() {
		return rendered;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

	public ArrayList<TestBean> getTestList() {
		return testList;
	}

	public void setTestList(ArrayList<TestBean> testList) {
		this.testList = testList;
	}

	public int getTestNumber() {
		return testNumber;
	}

	public void setTestNumber(int testNumber) {
		this.testNumber = testNumber;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getVariance() {
		return variance;
	}

	public void setVariance(double variance) {
		this.variance = variance;
	}

	public double getStd() {
		return std;
	}

	public void setStd(double std) {
		this.std = std;
	}

	public double getMedian() {
		return median;
	}

	public void setMedian(double median) {
		this.median = median;
	}

	public double getQ1() {
		return q1;
	}

	public void setQ1(double q1) {
		this.q1 = q1;
	}

	public double getQ3() {
		return q3;
	}

	public void setQ3(double q3) {
		this.q3 = q3;
	}

	public double getIqr() {
		return iqr;
	}

	public void setIqr(double iqr) {
		this.iqr = iqr;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

}
