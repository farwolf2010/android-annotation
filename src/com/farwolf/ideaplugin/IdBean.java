package com.farwolf.ideaplugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhengjiangrong on 16/7/29.
 */
public class IdBean {
    private static final Pattern sValidityPattern = Pattern.compile("^([a-zA-Z_\\$][\\w\\$]*)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern sIdPattern = Pattern.compile("@\\+?(android:)?id/([^$]+)$", Pattern.CASE_INSENSITIVE);
    public IdBean()
    {

    }
    public IdBean(String fullName,String id)
    {

        final Matcher matcher = sIdPattern.matcher(id);
        if (matcher.find() && matcher.groupCount() > 0) {
            this.id = matcher.group(2);

            String androidNS = matcher.group(1);
            this.isAndroidNS = !(androidNS == null || androidNS.length() == 0);
        }

        this.fullName =fullName;
        if(id!=null)
        this.id=id.replace("@+id/"  ,"" );
        this.varName=this.id;

    }
    public boolean isAndroidNS = false;
    public String fullName ="";
    public String id="";
    public String varName="";
    public boolean isClick=false;
    public boolean createBean=true;
    public boolean used=true;
    public boolean isValid=true;

    public String getDisplayFullName()
    {
        if(!fullName.contains("."))
        {
            return fullName;
        }
        String q[]=fullName.split("\\.");
        return q[q.length-1];


    }

    public String getFullID() {
        StringBuilder fullID = new StringBuilder();
        String rPrefix;

        if (isAndroidNS) {
            rPrefix = "android.R.id.";
        } else {
            rPrefix = "R.id.";
        }

        fullID.append(rPrefix);
        fullID.append(id);

        return fullID.toString();
    }
    public String getGenerateFullName()
    {
        if(fullName.contains("."))
        {
            return fullName;
        }
        if("View".equals(fullName))
        {
            return "View";
        }
        if("WebView".equals(fullName))
        {
            return "android.webkit.WebView";
        }
        return "android.widget."+fullName;
    }

    public boolean isChangeVarName()
    {
         return !id.equals(varName);
    }

    public boolean checkValidity() {
        Matcher matcher = sValidityPattern.matcher(varName);
        isValid = matcher.find();

        return isValid;
    }
}
