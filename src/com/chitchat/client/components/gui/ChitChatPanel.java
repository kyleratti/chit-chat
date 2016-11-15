/**
* @author Kyle Ratti (PC18)
* @version 1.0, 01/09/14
*/

package com.chitchat.client.components.gui;

/** A panel used in ChitChat */
public abstract class ChitChatPanel extends javax.swing.JPanel
{
	/**
	* Gets the desired width of this panel
	*
	* @return The desired width
	*/
	public abstract int getWidth();

	/**
	* Gets the desired height of this panel
	*
	* @return The desired height
	*/
	public abstract int getHeight();

	/** An event fired when the panel is shown */
	public abstract void onShow();
}