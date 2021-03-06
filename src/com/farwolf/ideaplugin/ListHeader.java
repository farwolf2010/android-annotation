package com.farwolf.ideaplugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ListHeader extends JPanel {

    protected JCheckBox mAllCheck;
    protected JLabel mType;
    protected JLabel mID;
    protected JLabel mEvent;
    protected JLabel mBean;
    protected JLabel mName;


    public ListHeader() {
        mAllCheck = new JCheckBox();
        mAllCheck.setPreferredSize(new Dimension(40, 26));
        mAllCheck.setSelected(true);
        mAllCheck.addItemListener(new AllCheckListener());

        mType = new JLabel("Element");
        mType.setPreferredSize(new Dimension(100, 26));
        mType.setFont(new Font(mType.getFont().getFontName(), Font.BOLD, mType.getFont().getSize()));

        mID = new JLabel("ID");
        mID.setPreferredSize(new Dimension(100, 26));
        mID.setFont(new Font(mID.getFont().getFontName(), Font.BOLD, mID.getFont().getSize()));

        mBean = new JLabel("bean");
        mBean.setPreferredSize(new Dimension(50, 26));
        mBean.setFont(new Font(mBean.getFont().getFontName(), Font.BOLD, mBean.getFont().getSize()));

        mEvent = new JLabel("click");
        mEvent.setPreferredSize(new Dimension(50, 26));
        mEvent.setFont(new Font(mEvent.getFont().getFontName(), Font.BOLD, mEvent.getFont().getSize()));

        mName = new JLabel("Variable Name");
        mName.setPreferredSize(new Dimension(100, 26));
        mName.setFont(new Font(mName.getFont().getFontName(), Font.BOLD, mName.getFont().getSize()));

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(Box.createRigidArea(new Dimension(1, 0)));
        add(mAllCheck);
        add(Box.createRigidArea(new Dimension(11, 0)));
        add(mType);
        add(Box.createRigidArea(new Dimension(12, 0)));
        add(mID);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(mBean);
        add(Box.createRigidArea(new Dimension(12, 0)));
        add(mEvent);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(mName);
        add(Box.createHorizontalGlue());
    }

    public JCheckBox getAllCheck() {
        return mAllCheck;
    }

    // classes

    private class AllCheckListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent itemEvent) {
            if (selectChangeListener != null) {
                selectChangeListener.onSelectChange(itemEvent.getStateChange() == ItemEvent.SELECTED);
            }
        }
    }



    public void setSelectChangeListener(OnAllSelectChangeListener selectChangeListener) {
        this.selectChangeListener = selectChangeListener;
    }

    OnAllSelectChangeListener selectChangeListener;

    public static interface  OnAllSelectChangeListener
    {
            public void onSelectChange(boolean select);
    }

}
