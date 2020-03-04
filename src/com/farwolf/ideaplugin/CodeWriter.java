package com.farwolf.ideaplugin;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengjiangrong on 16/7/29.
 */
public class CodeWriter extends WriteCommandAction.Simple {
    protected PsiFile mFile;
    protected Project mProject;
    protected PsiClass mClass;
    protected PsiElementFactory mFactory;
    List<IdBean>data=new ArrayList<IdBean>();
    boolean createOneClick=false;
    boolean createViewHolder=false;
    public CodeWriter(PsiFile file, PsiClass clazz, String command, List<IdBean>data, boolean createOneClick)
    {
        super(clazz.getProject(), command);
        mFile = file;
        mProject = clazz.getProject();
        mClass = clazz;
        mFactory = JavaPsiFacade.getElementFactory(mProject);
        this.data=data;
        this.createOneClick=createOneClick;


    }


    @Override
    protected void run() throws Throwable {

        if(data==null||data.size()==0)
            return;
         if(createOneClick)
         creatOneClick();
         for(IdBean id:data)
         {
             if(id.createBean)
             gennerateField(id);
             if(!createOneClick&&id.isClick)
             gennerateMethod(id);

         }
//         if(createViewHolder)
//             gennerateViewHolder();
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mProject);
        styleManager.optimizeImports(mFile);
        styleManager.shortenClassReferences(mClass);

    }


    public void creatOneClick()
    {
        StringBuilder method = new StringBuilder();

        method.append("@org.androidannotations.annotations.Click({");
        boolean need=false;
        List<IdBean>temp=new ArrayList<IdBean>();
        for(IdBean id: data)
        {
            if(id.isClick)
                temp.add(id);
        }

        if(temp.size()==0)
        {
            return;
        }
        for(IdBean id: temp)
        {

            if(temp.indexOf(id)==temp.size()-1)
            {
                method.append(""+id.getFullID()+"})");
            }
            else
            {
                method.append(""+id.getFullID()+",");
            }

        }
        method.append("public void onClick(android.view.View view) {switch (view.getId()){");
        for (IdBean element : data) {
            if (element.isClick) {
                method.append("case " + element.getFullID() + ": break;");
            }
        }
        method.append("}}");
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mClass.getProject());
        styleManager.shortenClassReferences(mClass.add(mFactory.createMethodFromText(method.toString(), mClass)));
    }

    public void gennerateMethod(IdBean id)
    {
        String s="@org.androidannotations.annotations.Click  ";
        if(id.isChangeVarName())
        {
            s=s+"("+id.getFullID()+")";
        }
        s+=  "public void "+id.varName+"Clicked()\n"+
        "{\n" +

             "\n"+
             "\n"+
             "\n"+
        "}";
        PsiMethod[] methods = mClass.findMethodsByName(id.varName+"Clicked", false);
       if(methods.length>0)
       {
           return;
       }

        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mClass.getProject());
        styleManager.shortenClassReferences(mClass.add(mFactory.createMethodFromText(s, mClass)));

    }


//    public void gennerateViewHolder()
//    {
//        StringBuilder holderBuilder = new StringBuilder();
//        holderBuilder.append("ViewHolder");
//        holderBuilder.append("(android.view.View view) {");
//
//        holderBuilder.append("(this, view);");
//        holderBuilder.append("}");
//
//        PsiClass viewHolder = mFactory.createClassFromText(holderBuilder.toString(), mClass);
//        viewHolder.setName("ViewHolder");
//
//        // add injections into view holder
//        for (IdBean element : data) {
//            if (!element.used) {
//                continue;
//            }
//            StringBuilder injection = new StringBuilder();
//
//            injection.append("@org.androidannotations.annotations.ViewById");
//            if(element.isChangeVarName())
//            {
//
//                injection.append("("+element.getFullID()+")");
//            }
//            injection.append("\n");
//            injection.append( element.getGenerateFullName()+" "+element.varName+";\n\n");
//
//            viewHolder.add(mFactory.createFieldFromText(injection.toString(), mClass));
//        }
//
//        mClass.add(viewHolder);
//        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mClass.getProject());
//        styleManager.shortenClassReferences( mClass.addBefore(mFactory.createKeyword("static", mClass), mClass.findInnerClassByName("ViewHolder", true)));
//    }



    public void gennerateField(IdBean id)
    {
        String s="@org.androidannotations.annotations.ViewById";
        if(id.isChangeVarName())
        {
            s=s+"("+id.getFullID()+")";
        }
        s+="\n";
        s+=id.getGenerateFullName()+" "+id.varName+";\n\n";
        if(mClass.findFieldByName(id.varName,true)!=null)
        {
            return;
        }
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mClass.getProject());

        styleManager.shortenClassReferences(mClass.add(mFactory.createFieldFromText(s, mClass)));



    }
}
