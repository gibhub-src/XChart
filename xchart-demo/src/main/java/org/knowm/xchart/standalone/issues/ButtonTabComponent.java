/*
 * Copyright 2020 Knowm Inc..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.standalone.issues;

/**
 *
 * @author dimov
 */
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
 
/**
 * Component to be used as tabComponent;
 * Contains a JLabel to show the text and 
 * a JButton to close the tab it belongs to 
 */
public class ButtonTabComponent extends JPanel {
    private final JTabbedPane pane;
 
    public ButtonTabComponent(final JTabbedPane pane) {
        //unset default FlowLayout' gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
        setOpaque(false);
        
        // Title Editor Field
        JTextField titleEditor = new JTextField();
        titleEditor.setVisible(false);
        titleEditor.addKeyListener(textFieldKeyAdapter);
        add(titleEditor);
         
        //make JLabel read titles from JTabbedPane
//        JLabel label = new JLabel();
//        label.setText(pane.getTitleAt(pane.indexOfTabComponent(ButtonTabComponent.this)));
        JLabel label = new JLabel() {
            public String getText() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };
        
        label.addMouseListener(lblMouseListener);
        add(label);
        //add more space between the label and the button
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        
        // test Tab Title Editing
//            Component tabComponent = pane.getTabComponentAt(i);
//    //        tab1 = pane.getTabComponentAt(pane.getSelectedIndex());
//    //        tabComp = pane.getSelectedComponent();
//                tabComponent.addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mouseClicked(MouseEvent e) {
//                        if (e.getClickCount() == 2) {
//                            System.out.println("tab clicked twice @: "+pane.getTitleAt(i));
//    //                        JTextField editTitle = new JTextField("edit Tab title...");
//    //                        editTitle.setToolTipText("Rename Tab");
//    //                        editTitle.setSize(100, 10);
//    //                        String tabTitle = tab1.getName();
//    //                        System.out.println("tabTitle = " + tabTitle);
//    //                        JPanel tabPanel = new JPanel();
//    //                        tabPanel.add(editTitle);
//    //                        tabPanel.add(new ButtonTabComponent(pane));
//    //                        pane.setTabComponentAt(1, tabPanel);
//                        }
//                    }
//
//                });
        
        //

        //tab button
        JButton button = new TabButton();
        add(button);
        //add more space to the top of the component
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }
 
    private class TabButton extends JButton implements ActionListener {
        public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close this tab");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }
 
        public void actionPerformed(ActionEvent e) {
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1) {
                pane.remove(i);
            }
        }
 
        //we don't want to update UI for this button
        public void updateUI() {
        }
 
        //paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.MAGENTA);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }
    
    private final KeyAdapter textFieldKeyAdapter = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                System.out.println("ENTER");
                JTextField editor = (JTextField) e.getComponent();
                JPanel editorParent = (JPanel) editor.getParent();
                JLabel lbl = (JLabel) editorParent.getComponent(1);
                
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    pane.setTitleAt(i, editor.getText());
                }
                lbl.setText(editor.getText());
                System.out.println("lbl: "+lbl.getText());
                System.out.println("edit: "+editor.getText());
                lbl.setVisible(true);
                editor.setVisible(false);
            }
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.out.println("ESCAPE");
                JTextField editor = (JTextField) e.getComponent();
                JPanel editorParent = (JPanel) editor.getParent();
                JLabel lbl = (JLabel) editorParent.getComponent(1);
                
                lbl.setVisible(true);
                editor.setVisible(false);
            }
        }

    };
    
    private final MouseListener lblMouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof JLabel) {
                JLabel lbl = (JLabel) component;
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    pane.setSelectedIndex(i);
                }
                if (e.getClickCount() == 2) {
                    System.out.println("tab clicked twice @: "+lbl.getText());
                    JPanel lblParent = (JPanel) lbl.getParent();
                    JTextField editTitle = (JTextField) lblParent.getComponent(0);
                    editTitle.setText(lbl.getText());
                    editTitle.setToolTipText("Rename Tab");
                    editTitle.setVisible(true);
//                    editTitle.setSize(100, 10);

                    lbl.setVisible(false);
                }
                
            }
        }
        
    };
 
    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }
 
        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };
}