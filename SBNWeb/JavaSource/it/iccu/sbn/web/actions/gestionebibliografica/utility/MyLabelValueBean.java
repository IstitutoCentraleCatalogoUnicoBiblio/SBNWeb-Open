/*
 * MyLabelValueBean.java
 *
 * Created on September 21, 2006, 4:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.iccu.sbn.web.actions.gestionebibliografica.utility;

import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author Antonio
 */
public class MyLabelValueBean implements Comparable<MyLabelValueBean>, Serializable  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5458663264735909421L;
	/**
     * Comparator that can be used for a case insensitive sort of
     * <code>LabelValueBean</code> objects.
     */
    public static final Comparator<MyLabelValueBean> CASE_INSENSITIVE_ORDER = new Comparator<MyLabelValueBean>() {
    	public int compare(MyLabelValueBean o1, MyLabelValueBean o2) {
    		String myAction1 = o1.getMyAction();
    		String myAction2 = o2.getMyAction();
    		return myAction1.compareToIgnoreCase(myAction2);
    	}
    };


    // ----------------------------------------------------------- Constructors


    /**
     * Default constructor.
     */
    public MyLabelValueBean() {
        super();
    }

    /**
     * Construct an instance with the supplied property values.
     *
     * @param label The label to be displayed to the user.
     * @param value The value to be returned to the server.
     */
    public MyLabelValueBean(String myAction, String myLivelloBaseDati, String myLabel, String myForwardName, String myForwardPath, String myModeCall) {
        this.myAction = myAction;
        this.myLivelloBaseDati = myLivelloBaseDati;
        this.myLabel = myLabel;
        this.myForwardName = myForwardName;
        this.myForwardPath = myForwardPath;
        this.myModeCall = myModeCall;
    }


    // ------------------------------------------------------------- Properties


    /**
     * The property which supplies the option label visible to the end user.
     */
    private String myAction = null;
    private String myLivelloBaseDati = null;
    private String myLabel = null;
    private String myForwardName = null;
	private String myForwardPath = null;
	private String myModeCall = null;

    public String getMyAction() {
        return this.myAction;
    }

    public void setMyAction(String myAction) {
        this.myAction = myAction;
    }

	public String getMyLabel() {
		return myLabel;
	}

	public void setMyLabel(String myLabel) {
		this.myLabel = myLabel;
	}

	public String getMyForwardName() {
		return myForwardName;
	}

	public void setMyForwardName(String myForwardName) {
		this.myForwardName = myForwardName;
	}

	public String getMyForwardPath() {
		return myForwardPath;
	}

	public void setMyForwardPath(String myForwardPath) {
		this.myForwardPath = myForwardPath;
	}

    public String getMyModeCall() {
		return myModeCall;
	}

	public void setMyModeCall(String myModeCall) {
		this.myModeCall = myModeCall;
	}

	// --------------------------------------------------------- Public Methods
	/**
	 * Compare LabelValueBeans based on the label, because that's the human
     * viewable part of the object.
	 * @see Comparable
	 */
	public int compareTo(MyLabelValueBean o) {
		// Implicitly tests for the correct type, throwing
        // ClassCastException as required by interface
		String otherLabel = o.getMyAction();

		return this.getMyAction().compareTo(otherLabel);
	}

    /**
     * Return a string representation of this object.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("MyLabelValueBean[");
        sb.append(this.myAction);
        sb.append(", ");
        sb.append(this.myLivelloBaseDati);
        sb.append(", ");
        sb.append(this.myLabel);
        sb.append(", ");
        sb.append(this.myForwardName);
        sb.append(", ");
        sb.append(this.myForwardPath);
        sb.append(", ");
        sb.append(this.myModeCall);
        sb.append("]");
        return (sb.toString());
    }

    /**
     * LabelValueBeans are equal if their values are both null or equal.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof MyLabelValueBean)) {
            return false;
        }

        MyLabelValueBean bean = (MyLabelValueBean) obj;
        int nil = (this.getMyLabel() == null) ? 1 : 0;
        nil += (bean.getMyLabel() == null) ? 1 : 0;

        if (nil == 2) {
            return true;
        } else if (nil == 1) {
            return false;
        } else {
            return this.getMyLabel().equals(bean.getMyLabel());
        }

    }

    /**
     * The hash code is based on the object's value.
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (this.getMyLabel() == null) ? 17 : this.getMyLabel().hashCode();
    }

	public String getMyLivelloBaseDati() {
		return myLivelloBaseDati;
	}

	public void setMyLivelloBaseDati(String myLivelloBaseDati) {
		this.myLivelloBaseDati = myLivelloBaseDati;
	}

}
