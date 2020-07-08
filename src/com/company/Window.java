package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.LinkedList;

import static com.company.Main.*;

public class Window extends JPanel implements ActionListener, KeyListener, MouseMotionListener, MouseWheelListener
{

    public static final int DELAY = 10;
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    public static Timer timer;

    private boolean increasing;
    private boolean decreasing;

    private double current;
    public static double setpoint;
    private double ymax;

    private Pid ctrl;
    private LinkedList<Double> dots;

    public Window()
    {
        increasing = decreasing = false;
        current = 0;
        ymax = 0;
        setpoint = SETPOINT;
        ctrl = Main.getBestController();
        dots = new LinkedList<>();
        dots.add(current);
        ctrl.setCurrent(0);
        ctrl.setSetpoint(SETPOINT);

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);

        addKeyListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void update()
    {

        if (increasing)
        {
            setpoint++;
        }

        if (decreasing)
        {
            setpoint--;
        }

        ctrl.setSetpoint(setpoint);
        current = ctrl.getCurrent();
        ctrl.run(10);
        dots.add(current);
        if (dots.size() > WIDTH)
        {
            dots.remove(0);

        }

        ymax = max(dots);
        ymax = Math.max(ymax, setpoint);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        render(g);
    }

    private void render(Graphics g)
    {
        g.setColor(Color.RED);

        g.fillRect(0, getYPos(setpoint, ymax), WIDTH, 1);

        g.setColor(Color.BLACK);
        int i = 0;
        double previous = dots.get(0);
        for (double current : dots)
        {
            g.fillRect(i, getYPos(current, ymax), 1, 1);
            g.drawLine(i, getYPos(current, ymax), i - 1, getYPos(previous, ymax));
            previous = current;
            i++;
        }

        g.drawString(current + "/" + setpoint, 0, 10);
        g.drawString("p: " + getBestController().getKp(), 0, 20);
        g.drawString("i: " + getBestController().getKi(), 0, 30);
        g.drawString("d: " + getBestController().getKd(), 0, 40);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        update();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        switch (key)
        {
            case KeyEvent.VK_UP:
                increasing = true;
                break;
            case KeyEvent.VK_DOWN:
                decreasing = true;
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();
        switch (key)
        {
            case KeyEvent.VK_UP:
                increasing = false;
                break;
            case KeyEvent.VK_DOWN:
                decreasing = false;
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        //setpoint = getYPos(e.getY(), ymax);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        setpoint += e.getScrollAmount() * -e.getWheelRotation();
    }
}
