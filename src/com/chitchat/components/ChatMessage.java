/**
* @author Kyle Ratti (PC18)
* @version 1.0, 01/14/14
*/

package com.chitchat.components;

import com.chitchat.components.user.User;

import java.io.Serializable;

import java.util.Date;

public class ChatMessage implements Serializable
{
	private static final long serialVersionUID = 7526472295622576147L;

	private final String message;
	private final User user;
	private final Date dateTime;

	/**
	* Creates a new <code>ChatMessage</code> with the specified message
	*
	* @param strMsg The message
	*/
	public ChatMessage(String strMsg)
	{
		this(strMsg, null);
	}

	/**
	* Creates a new <code>ChatMessage</code> with the specified message and user
	*
	* @param strMsg The message
	* @param objUser The <code>User</code>
	*/
	public ChatMessage(String strMsg, User objUser)
	{
		this.message = strMsg;
		this.user = objUser;
		this.dateTime = new Date();
	}

	/**
	* Gets the message
	*
	* @return The message
	*/
	public String getMessage()
	{
		return this.message;
	}

	/**
	* Gets the <code>User</code> associated with this message, if any
	*
	* @return The <code>User</code>, or <code>null</code> if there isn't one
	*/
	public User getUser()
	{
		return this.user;
	}

	/**
	* Gets the <code>Date</code> this message was sent
	*
	* @return The <code>Date</code>
	*/
	public Date getDateTime()
	{
		return this.dateTime;
	}
}