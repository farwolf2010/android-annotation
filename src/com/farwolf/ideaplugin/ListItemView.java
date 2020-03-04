package com.farwolf.ideaplugin;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ListItemView extends JPanel {

//    protected EntryList mParent;
//    protected Element mElement;

    IdBean bean;

//    protected OnCheckBoxStateChangedListener mListener;
    // ui
    protected JCheckBox mCheck;
    protected JLabel mType;
    protected JLabel mID;
    protected JCheckBox mEvent;
    protected JCheckBox mBean;
    protected JTextField mName;
    protected Color mNameDefaultColor;
    protected Color mNameErrorColor = new Color(0x880000);

    public JCheckBox getCheck() {
        return mCheck;
    }

//    public void setListener(final OnCheckBoxStateChangedListener onStateChangedListener) {
//        this.mListener = onStateChangedListener;
//    }

    public ListItemView(IdBean bean) {

       this.bean=bean;

        mCheck = new JCheckBox();
        mCheck.setPreferredSize(new Dimension(40, 26));
//        if (!mGeneratedIDs.contains(element.getFullID())) {
//            mCheck.setSelected(mElement.used);
//        } else {
//            mCheck.setSelected(false);
//        }

        mCheck.setSelected(bean.used);
        mCheck.addChangeListener(new CheckListener());

        mEvent = new JCheckBox();
        mEvent.setPreferredSize(new Dimension(50, 26));
        mEvent.setSelected(bean.isClick);
        mEvent.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                ListItemView.this.bean.isClick=mEvent.isSelected();
            }
        });

        mBean = new JCheckBox();
        mBean.setPreferredSize(new Dimension(50, 26));
        mBean.setSelected(bean.createBean);
        mBean.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                ListItemView.this.bean.createBean=mBean.isSelected();
            }
        });



        mType = new JLabel(bean.getDisplayFullName());
        mType.setPreferredSize(new Dimension(100, 26));

        mID = new JLabel(bean.id);
        mID.setPreferredSize(new Dimension(100, 26));

        mName = new JTextField(bean.varName, 10);
        mNameDefaultColor = mName.getBackground();
        mName.setPreferredSize(new Dimension(100, 26));
        mName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // empty
            }

            @Override
            public void focusLost(FocusEvent e) {
                syncElement();
            }
        });

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setMaximumSize(new Dimension(Short.MAX_VALUE, 54));
        add(mCheck);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mType);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mID);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mBean);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mEvent);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mName);
        add(Box.createHorizontalGlue());

        checkState();
    }

    public IdBean syncElement() {

        bean.used = mCheck.isSelected();
        bean.isClick = mEvent.isSelected();
        bean.varName = mName.getText();
        bean.createBean=mBean.isSelected();
        if (bean.checkValidity()) {
            mName.setBackground(mNameDefaultColor);
        } else {
            mName.setBackground(mNameErrorColor);
        }

        return bean;
    }


    public void setUsed(boolean used)
    {
        bean.used=used;
        mCheck.setSelected(used);
        checkState();

    }

    private void checkState() {
        if (mCheck.isSelected()) {
            mType.setEnabled(true);
            mID.setEnabled(true);
            mName.setEnabled(true);
            mEvent.setEnabled(true);
            mBean.setEnabled(true);
        } else {
            mType.setEnabled(false);
            mID.setEnabled(false);
            mName.setEnabled(false);
            mEvent.setEnabled(false);
            mBean.setEnabled(false);
        }
        syncElement();
//        if (mListener != null) {
//            mListener.changeState(mCheck.isSelected());
//        }
    }

    // classes

    public class CheckListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent event) {

            checkState();
        }
    }

}
