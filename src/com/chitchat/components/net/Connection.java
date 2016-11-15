/**
* @author Kyle Ratti (PC18)
* @version 1.0, 01/09/14
*/

package com.chitchat.components.net;

import com.chitchat.components.user.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** Represents a threaded connection */
public abstract class Connection extends Thread
{
	private User user;

	/**
	* Creates a new <code>Connection</code>
	*
	* @apram strName The name of this connection
	*/
	public Connection(String strName)
	{
		super(strName);
		this.user = null;
	}

	/**
	* Gets the <code>ObjectInputStream</code> this connection uses
	*
	* @return The <code>ObjectInputStream</code>
	*/
	public abstract ObjectInputStream getInputStream();

	/**
	* Gets the <code>ObjectOutputStream</code> this connection uses
	*
	* @return The <code>ObjectOutputStream</code>
	*/
	public abstract ObjectOutputStream getOutputStream();

	/**
	* Sets the <code>User</code> associated with this connection
	*
	* @param objUser The <code>User</code>
	*/
	public void setUser(User objUser)
	{
		this.user = objUser;
	}

	/**
	* Gets the <code>User</code> associated with this connection
	*
	* @return The <code>User</code>, or <code>null</code> if there is none
	*/
	public User getUser()
	{
		return this.user;
	}
}