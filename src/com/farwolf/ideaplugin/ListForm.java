package com.farwolf.ideaplugin;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengjiangrong on 16/7/29.
 */
public class ListForm extends JPanel {

    List<IdBean> data=new ArrayList<IdBean>();

    List<ListItemView>items=new  ArrayList<ListItemView>();

    JFrame parent;

    protected PsiFile p;
    Project project;

    PsiClass cls;
    public ListForm(JFrame parent, List<IdBean>data, PsiFile p, PsiClass cls, Project project) {
        this.parent=parent;
        this.data=data;
        this.p=p;
        this.project=project;
        this.cls=cls;
        setPreferredSize(new Dimension(640, 360));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        addInjections();
        addButtons();
    }

    protected void addInjections() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ListHeader mEntryHeader = new ListHeader();
        contentPanel.add(mEntryHeader);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mEntryHeader.setSelectChangeListener(new  ListHeader.OnAllSelectChangeListener(){
            public void onSelectChange(boolean select){

                for(ListItemView e:items)
                {
                    e.setUsed(select);
                }

            }
        });

        JPanel injectionsPanel = new JPanel();
        injectionsPanel.setLayout(new BoxLayout(injectionsPanel, BoxLayout.PAGE_AXIS));
        injectionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        int cnt = 0;

        for (IdBean bean:data) {
            ListItemView entry = new ListItemView(bean);
//            entry.setListener(singleCheckListener);

            if (cnt > 0) {
                injectionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
            injectionsPanel.add(entry);
            cnt++;
            items.add(entry);

//            selectAllCheck &= entry.getCheck().isSelected();
        }
//        mEntryHeader.getAllCheck().setSelected(selectAllCheck);
//        mEntryHeader.setAllListener(allCheckListener);
        injectionsPanel.add(Box.createVerticalGlue());
        injectionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JBScrollPane scrollPane = new JBScrollPane(injectionsPanel);
        contentPanel.add(scrollPane);

        add(contentPanel, BorderLayout.CENTER);
        revalidate();
    }


    JCheckBox mClickCheck;
    JCheckBox mholderCheck;
    protected void addButtons() {


        mClickCheck = new JCheckBox();
        mClickCheck.setPreferredSize(new Dimension(32, 26));
        mClickCheck.setSelected(false);
        JLabel mClickLabel = new JLabel();
        mClickLabel.setText("Create Clicks in One method");
        JPanel clickPanel = new JPanel();
        clickPanel.setLayout(new BoxLayout(clickPanel, BoxLayout.LINE_AXIS));
        clickPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        clickPanel.add(mClickCheck);
        clickPanel.add(mClickLabel);
        clickPanel.add(Box.createHorizontalGlue());
        add(clickPanel, BorderLayout.PAGE_END);



//        mholderCheck = new JCheckBox();
//        mholderCheck.setPreferredSize(new Dimension(32, 26));
//        mholderCheck.setSelected(false);
//        JLabel mholderLabel = new JLabel();
//        mholderLabel.setText("Create ViewHolder");
//        JPanel holderPanel = new JPanel();
//        holderPanel.setLayout(new BoxLayout(holderPanel, BoxLayout.LINE_AXIS));
//        holderPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
//        holderPanel.add(mholderCheck);
//        holderPanel.add(mholderLabel);
//        holderPanel.add(Box.createHorizontalGlue());
//        add(holderPanel, BorderLayout.PAGE_END);





        JButton mCancel = new JButton();
        mCancel.setAction(new CancelAction());
        mCancel.setPreferredSize(new Dimension(120, 26));
        mCancel.setText("Cancel");
        mCancel.setVisible(true);

        JButton mConfirm = new JButton();
        mConfirm.setAction(new ConfirmAction());
        mConfirm.setPreferredSize(new Dimension(120, 26));
        mConfirm.setText("Confirm");
        mConfirm.setVisible(true);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(mCancel);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(mConfirm);

        add(buttonPanel, BorderLayout.PAGE_END);
        revalidate();
    }

    protected class CancelAction extends AbstractAction {

        public void actionPerformed(ActionEvent event) {
            if (parent == null) {
                return;
            }

            parent.setVisible(false);
            parent.dispose();
        }
    }

    protected class ConfirmAction extends AbstractAction {

        public void actionPerformed(ActionEvent event) {
            if (parent == null) {
                return;
            }

            parent.setVisible(false);
            List<IdBean> temp=new ArrayList<IdBean>();
            for(IdBean bean:data)
            {
                if(!bean.used)
                    continue;
                if(!bean.isClick&&!bean.createBean)
                    continue;
                if(!bean.checkValidity())
                {
                    Utils.showErrorNotification(project,"'"+bean.varName+"'   is not a valid var name!");
                    return;
                }
                temp.add(bean);

            }
            if(temp.size()==0)
            {
                Utils.showErrorNotification(project,"没有要生成的id!");
                return;
            }
            new CodeWriter(p,cls,"make",temp,mClickCheck.isSelected()).execute();
            parent.dispose();


        }
    }
}
