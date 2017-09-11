package edu.uic.model.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class NavigationBean implements Serializable {

	/**
	 * @author Sathyanarayan
	 */
	private static final long serialVersionUID = 1L;
	

	public String roaster() {		
		return "viewuploadRoaster";
	}
	public String test() {		
		return "viewuploadTest";
	}

	public String questions() {		
		return "uploadQuestions";
	}
	public String statisticalAnalysis() {		
		return "statisticalAnalysis";
	}
	public String graphicalAnalysis() {		
		return "graphicalAnalysis";
	}
}
