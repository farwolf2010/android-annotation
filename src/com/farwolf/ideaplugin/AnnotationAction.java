package com.farwolf.ideaplugin;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengjiangrong on 16/7/28.
 */
public class AnnotationAction extends BaseGenerateAction
{

    public AnnotationAction(CodeInsightActionHandler handler) {
        super(handler);
    }
    public AnnotationAction( ) {
        super(null);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);


        actionPerformedImpl(project, editor);
    }


    void test()
    {



//        PsiElementFactory mFactory = JavaPsiFacade.getElementFactory(project);
//        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
//        PsiClass cls= getTargetClass(editor,file);
//        List<IdBean> data=new ArrayList<IdBean>();
//        for(int i=0;i<10;i++)
//        {
//            data.add(new IdBean(i+"","a"+i));
//        }
//        new CodeWriter(file,cls,"make",data).execute();
    }

    @Override
    public void actionPerformedImpl(Project project, Editor editor) {
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        PsiFile layout = Utils.getLayoutFileFromCaret(editor, file);

        if (layout == null) {
            Utils.showErrorNotification(project, "No layout found");
            return; // no layout found
        }
        ArrayList<IdBean> elements = Utils.getIDsFromLayout(layout);
        if (!elements.isEmpty()) {
            showDialog(project, editor, elements);
        } else {
            Utils.showErrorNotification(project, "No IDs found in layout");
        }


    }


    public void showDialog(Project project, Editor editor,List<IdBean> data)
    {

        PsiElementFactory mFactory = JavaPsiFacade.getElementFactory(project);
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        PsiClass cls= getTargetClass(editor,file);

        JFrame mDialog = new JFrame();
        mDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


        mDialog.getContentPane().add(new ListForm(mDialog,data,file,cls,project));
        mDialog.pack();
        mDialog.setLocationRelativeTo(null);
        mDialog.setVisible(true);

    }



    public static void main(String arg[])
    {





        JFrame mDialog = new JFrame();
        mDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


        mDialog.getContentPane().add(new ListForm(mDialog,new ArrayList<IdBean>(),null,null,null));
        mDialog.pack();
        mDialog.setLocationRelativeTo(null);
        mDialog.setVisible(true);

    }




}
