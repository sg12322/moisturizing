package com.sdydj.moisturizing.product.vo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class teste {


        public static void test1(){
            JFrame f = new JFrame("面板");
            f.setSize(500,700);
            f.setVisible(true);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLayout(new GridLayout(15,1,5,0));

            JLabel jl = new JLabel("姓名");
            JPanel jp1 = new JPanel();
            JTextField jTextField = new JTextField(20);
            jp1.add(jl);
            jp1.add(jTextField);
            f.add(jp1);

            JPanel jp2 = new JPanel();
            JLabel jl2 = new JLabel("性别");
            JRadioButton jb1 = new JRadioButton("男");
            JRadioButton jb2 = new JRadioButton("女");
            ButtonGroup group = new ButtonGroup();
            group.add(jb1);
            group.add(jb2);
            jp2.add(jl2);
            jp2.add(jb1);
            jp2.add(jb2);
            f.add(jp2);

            JPanel jp3 = new JPanel();
            JLabel jl3 = new JLabel("爱好");
            JCheckBox c1 = new JCheckBox("篮球");
            JCheckBox c2 = new JCheckBox("乒乓球");
            JCheckBox c3 = new JCheckBox("足球");
            jp3.add(jl3);
            jp3.add(c1);
            jp3.add(c2);
            jp3.add(c3);
            f.add(jp3);

            JPanel jp4 = new JPanel();
            JLabel jl4 = new JLabel("学历");
            jp4.add(jl4);
            JComboBox<String> comboBox = new JComboBox<>();
            comboBox.addItem("本科");
            comboBox.addItem("硕士");
            comboBox.addItem("博士");
            jp4.add(comboBox);
            f.add(jp4);

            JPanel jp5 = new JPanel();
            JLabel jl5 = new JLabel("备注");
            jp5.add(jl5);
            JTextArea jt = new JTextArea(20,20);
            jp5.add(jl5);
            jp5.add(jt);
            f.add(jp5);
            JButton jButton = new JButton("提交");
            f.add(jButton);

            JLabel jl6 = new JLabel();

            f.add(new JPanel().add(jl6));

            jButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String str = "姓名："+jTextField.getText()+"   ";


                    if(jb1.isSelected()){
                        str+="性别："+jb1.getText()+"   ";

                    }if(jb2.isSelected()){
                        str+="性别："+jb2.getText()+"   ";

                    }if(c1.isSelected()){

                        str+="爱好:"+c1.getText()+" ";

                    }
                    if(c2.isSelected()){
                        if(c1.isSelected()) {
                            str += c2.getText() + " ";
                        }else{
                            str+="爱好:"+c2.getText()+" ";
                        }
                    }if(c3.isSelected()){
                        if(c1.isSelected() || c2.isSelected())
                        {
                            str+=c3.getText()+" ";
                        }else{
                            str+="爱好:"+c3.getText()+" ";
                        }
                    }
                    str+="  ";
                    str+="学历:"+(String) comboBox.getSelectedItem()+"   ";

                    str+="备注:"+jt.getText()+"   ";

                    jl6.setText(str);

                }
            });


        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(teste::test1);

        }


}
