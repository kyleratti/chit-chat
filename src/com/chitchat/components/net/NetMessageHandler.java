/**
* @author Kyle Ratti (PC18)
* @version 1.0, 01/08/14
*/

package com.chitchat.components.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** A handler for a net message response */
public interface NetMessageHandler
{
	/**
	* Called when data is received over the network
	*
	* @param strAction The action being handled
	* @param objConnection The connection that is sending this data
	* @param objMsg The <code>NetMessage</code> received
	*/
	public void onDataReceived(String strAction, Connection objConnection, NetMessage objMsg);
}