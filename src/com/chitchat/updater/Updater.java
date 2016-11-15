/**
* @author Kyle Ratti (PC18)
* @version 1.0, 01/20/14
*/


package com.chitchat.updater;

import javax.swing.*;

public class Updater extends Thread
{
    private final JPanel panel;
    private final JTextArea log;
    
    public Updater(UpdaterPanel objPanel)
    {
        this.panel = objPanel;
        this.log = objPanel.getLog();
    }
    
    @Override
    public void run()
    {
        try
        {
            this.log.append("Checking version...");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
