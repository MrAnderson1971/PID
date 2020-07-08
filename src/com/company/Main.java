/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company;


import javax.swing.*;
import javax.swing.tree.ExpandVetoException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * @author Thomas Anderson
 */
public class Main extends JFrame
{

    public static final double SETPOINT = 100;

    public static final int POPULATION_SIZE = 10000;

    private static Pid bestController;

    public static JTextField setpointTextField;
    public static JTextField pTextField;
    public static JTextField iTextField;
    public static JTextField dTextField;

    public Main()
    {

        init();
    }

    public JPanel getContent()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel setpointText = new JLabel();
        setpointText.setText("setpoint: ");
        setpointTextField = new JTextField(8);
        setpointTextField.addActionListener(e ->
        {
            try
            {
                Window.setpoint = Double.parseDouble(setpointTextField.getText());
                setpointTextField.setText("");
            } catch (NumberFormatException ex)
            {
                setpointTextField.setText("");
            }
        });
        panel.add(setpointText);
        panel.add(setpointTextField);

        pTextField = new JTextField(8);
        pTextField.addActionListener(e ->
        {
            try
            {
                bestController.setKp(Double.parseDouble(pTextField.getText()));
                pTextField.setText("");
            } catch (NumberFormatException ex)
            {
                pTextField.setText("");
            }
        });
        JLabel pLabel = new JLabel();
        pLabel.setText("p: ");
        panel.add(pLabel);
        panel.add(pTextField);

        iTextField = new JTextField(8);
        iTextField.addActionListener(e ->
        {
            try
            {
                bestController.setKi(Double.parseDouble(iTextField.getText()));
                iTextField.setText("");
            } catch (NumberFormatException ex)
            {
                iTextField.setText("");
            }
        });
        JLabel iLabel = new JLabel();
        iLabel.setText("i: ");
        panel.add(iLabel);
        panel.add(iTextField);

        dTextField = new JTextField(8);
        dTextField.addActionListener(e ->
        {
            try
            {
                bestController.setKd(Double.parseDouble(dTextField.getText()));
                dTextField.setText("");
            } catch (NumberFormatException ex)
            {
                dTextField.setText("");
            }
        });
        JLabel dLabel = new JLabel();
        dLabel.setText("d: ");
        panel.add(dLabel);
        panel.add(dTextField);

        JButton toggleButton = new JButton();
        toggleButton.setText("Pause");
        toggleButton.addActionListener(e ->
        {
            if (toggleButton.getText().equals("Pause"))
            {
                Window.timer.stop();
                toggleButton.setText("Resume");
            } else
            {
                Window.timer.start();
                toggleButton.setText("Pause");
            }
        });
        panel.add(toggleButton);

        return panel;
    }

    private void init()
    {

        setLayout(new FlowLayout());
        add(getContent());
        add(new Window());

        setResizable(false);
        pack();

        setTitle("PID");
        //setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static Pid getBestController()
    {
        return bestController;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {

        /*
        This creates a bunch of random controllers, simulates them, then finds the best one.
         */
        ArrayList<Pid> controllers = new ArrayList<>();

        for (int i = 0; i < POPULATION_SIZE; i++)
        {
            Pid newPID = new Pid();
            newPID.setSetpoint(SETPOINT);
            controllers.add(newPID);
        }

        for (Pid ctrl : controllers)
        {
            ctrl.run(100);
            System.out.println(String.format("%s: %s, %s, %s", ctrl.getFinalError(), ctrl.getKp(), ctrl.getKi(), ctrl.getKd()));
        }

        bestController = controllers.get(0);
        for (Pid ctrl : controllers)
        {
            if (Math.abs(ctrl.getFinalError()) < Math.abs(bestController.getFinalError()))
            {
                bestController = ctrl;
            }
        }

        System.out.println("Best:");
        System.out.println(String.format("%s: %s, %s, %s", bestController.getFinalError(), bestController.getKp(), bestController.getKi(), bestController.getKd()));

        EventQueue.invokeLater(() ->
        {
            JFrame ex = new Main();
            ex.setVisible(true);
        });

    }

    public static int getYPos(double y, double ymax)
    {
        if (ymax <= Window.HEIGHT)
        {
            return Window.HEIGHT - (int) y;
        }
        return Window.HEIGHT - (int) ((Window.HEIGHT / ymax) * y);
    }

    public static double max(List<Double> list)
    {
        double max = list.get(0);
        for (double d : list)
        {
            if (d > max)
            {
                max = d;
            }
        }
        return max;
    }

}
