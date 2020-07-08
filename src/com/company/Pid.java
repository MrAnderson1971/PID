/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company;

import java.util.*;

/**
 *
 * @author Thomas Anderson
 */
public class Pid
{
    private double kp;
    private double ki;
    private double kd;

    private double finalError;
    private double current;
    private double setpoint;

    private ArrayList<Double> progress;

    private static final double COEFFICIENT = 1.0 / 3;

    public Pid(double kp, double ki, double kd)
    {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;

        this.current = 0;
    }

    public Pid()
    {
        this(COEFFICIENT * Math.random(), COEFFICIENT * Math.random(), COEFFICIENT * Math.random());
    }

    public double getCurrent()
    {
        return current;
    }

    public double getSetpoint()
    {
        return setpoint;
    }

    public void setCurrent(double current)
    {
        this.current = current;
    }

    public void setSetpoint(double setpoint)
    {
        this.setpoint = setpoint;
    }

    public void setKp(double kp)
    {
        this.kp = kp;
    }

    public void setKi(double ki)
    {
        this.ki = ki;
    }

    public void setKd(double kd)
    {
        this.kd = kd;
    }

    public void run(int times)
    {

        double integral = 0;
        double error = setpoint - current;
        double previousError = error;
        double derivative;
        double output;

        double dt = Window.DELAY / 1000.0;

        progress = new ArrayList<>();

        for (int i = 0; i < times; i++)
        {
            error = setpoint - current;
            integral += error * dt;
            derivative = (error - previousError) / dt;
            previousError = error;

            output = kp * error + ki * integral + kd * derivative;
            progress.add(current);
            updateCurrent(output);
        }

        finalError = error;
    }

    private void updateCurrent(double output)
    {
        current += 10 * output;
        if (Double.isNaN(current))
        {
            current = 0;
        }
    }

    public double getFinalError()
    {
        return finalError;
    }

    public double getKp()
    {
        return kp;
    }

    public double getKi()
    {
        return ki;
    }

    public double getKd()
    {
        return kd;
    }

    public ArrayList<Double> getProgress()
    {
        return progress;
    }

}
