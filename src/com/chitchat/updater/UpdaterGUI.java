/**
* @author Kyle Ratti (PC18)
* @version 1.0, 01/17/14
*/

package com.chitchat.updater;

import javax.swing.*;

public class UpdaterGUI
{
	private static JFrame frame = null;

	public static void main(String[] args)
	{
		UpdaterGUI.frame = new JFrame("chit chat updater");
		UpdaterGUI.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UpdaterGUI.frame.setSize(300, 500);
		UpdaterGUI.frame.setResizable(false);

		UpdaterGUI.frame.getContentPane().add(new UpdaterPanel());
		UpdaterGUI.frame.pack();
		UpdaterGUI.frame.setLocationRelativeTo(null);
		UpdaterGUI.frame.setVisible(true);
	}	
}