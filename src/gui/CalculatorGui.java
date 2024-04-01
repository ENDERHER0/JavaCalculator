package gui;

import constants.CommonConstants;
import service.CalculatorService;

import java.lang.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalculatorGui extends JFrame implements ActionListener {
    private final SpringLayout springLayout = new SpringLayout();
    private CalculatorService calculatorService;

    private JTextField displayField;

    private JButton[] buttons;

    private boolean pressedOperator = false;
    private boolean pressedEquals = false;
    public CalculatorGui(){
        super(CommonConstants.APP_NAME);
        setSize(CommonConstants.APP_SIZE[0], CommonConstants.APP_SIZE[1]);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(springLayout);

        calculatorService = new CalculatorService();

        addGuiComponents();
    }

    public void addGuiComponents() {
        addDisplayFieldComponents();

        addButtonComponents();
    }

    public void addDisplayFieldComponents() {
        JPanel displayFieldPanel = new JPanel();
        displayField = new JTextField(CommonConstants.TEXTFIELD_LENGTH);
        displayField.setFont(new Font("Dialog", Font.PLAIN, CommonConstants.TEXTFIELD_FONTSIZE));
        displayField.setText("0");
        displayField.setHorizontalAlignment(SwingConstants.RIGHT);

        displayFieldPanel.add(displayField);

        this.getContentPane().add(displayFieldPanel);
        springLayout.putConstraint(SpringLayout.NORTH, displayFieldPanel, CommonConstants.TEXTFIELD_SPRINGLAYOUT_NORTHPAD, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, displayFieldPanel, CommonConstants.TEXTFIELD_SPRINGLAYOUT_WESTPAD, SpringLayout.WEST, this);

    }

    public void addButtonComponents() {
        GridLayout gridLayout = new GridLayout(CommonConstants.BUTTON_ROWCOUNT, CommonConstants.BUTTON_COLCOUNT);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(gridLayout);
        buttons = new JButton[CommonConstants.BUTTON_COUNT];
        for(int i = 0; i < CommonConstants.BUTTON_COUNT; i++) {
            JButton button = new JButton(getButtonLabel(i));
            button.setFont(new Font("Dialog", Font.PLAIN, CommonConstants.BUTTON_FONTSIZE));
            button.addActionListener(this);

            buttonPanel.add(button);
        }

        gridLayout.setHgap(CommonConstants.BUTTON_HGAP);
        gridLayout.setVgap(CommonConstants.BUTTON_VGAP);

        this.getContentPane().add(buttonPanel);

        springLayout.putConstraint(SpringLayout.NORTH, buttonPanel, CommonConstants.BUTTON_SPRINGLAYOUT_NORTHPAD, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, buttonPanel, CommonConstants.BUTTON_SPRINGLAYOUT_WESTPAD, SpringLayout.WEST, this);


    }
    public String getButtonLabel(int buttonIndex) {
        switch (buttonIndex){
            case 0:
                return "7";
            case 1:
                return "8";
            case 2:
                return "9";
            case 3:
                return "*";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return "6";
            case 7:
                return "/";
            case 8:
                return "1";
            case 9:
                return "2";
            case 10:
                return "3";
            case 11:
                return "+";
            case 12:
                return ".";
            case 13:
                return "0";
            case 14:
                return "=";
            case 15:
                return "-";
        }
        return "";
    }


    @java.lang.Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        String buttonCommand = e.getActionCommand();
        if(buttonCommand.matches("[0-9]")) {
            if (pressedEquals || pressedOperator || displayField.getText().equals("0"))
                displayField.setText(buttonCommand);
            else
                displayField.setText(displayField.getText() + buttonCommand);
            
            pressedOperator = false;
            pressedEquals = false;
        } else if (buttonCommand.equals("=")) {
            calculatorService.setNum2(Double.parseDouble(displayField.getText()));

            double result = 0;
            switch (calculatorService.getMathSymbol()){
                case '+':
                    result = calculatorService.add();
                    break;
                case '-':
                    result = calculatorService.subtract();
                    break;
                case '*':
                    result = calculatorService.multiply();
                    break;
                case '/':
                    result = calculatorService.divide();
                    break;
            }

            displayField.setText(Double.toString(result));


            pressedEquals = true;
            pressedOperator = false;

        }else if(buttonCommand.equals(".")){
            if(!displayField.getText().contains(".")){
                displayField.setText(displayField.getText() + buttonCommand);
            }
        }else{
            if(!pressedOperator)
                calculatorService.setNum1(Double.parseDouble(displayField.getText()));

            calculatorService.setMathSymbol(buttonCommand.charAt(0));

            pressedOperator = true;
            pressedEquals = false;
        }
    }
}
