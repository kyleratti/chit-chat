/**
* @author Kyle Ratti (PC18)
* @version 1.0, 12/16/13
*/

package com.chitchat.components.user;

import java.io.Serializable;

import java.util.ArrayList;

/** Represents a user */
public class User implements Serializable
{
	private static final long serialVersionUID = 7526472295622476147L;
	private final int id;
	private final String username;
	private final boolean admin;

	/**
	* Creates a new user with the specified data
	*
	* @param iUserID The user's unique ID
	* @param strUsername The user's username
	* @param bAdmin Is the user an admin?
	*/
	public User(int iUserID, String strUsername, boolean bAdmin)
	{
		this.id = iUserID;
		this.username = strUsername;
		this.admin = bAdmin;
	}

	/**
	* Gets the user's unique ID
	*
	* @return The user's unique ID
	*/
	public int getID()
	{
		return this.id;
	}

	/**
	* Gets the user's username
	*
	* @return The user's username
	*/
	public String getUsername()
	{
		return this.username;
	}

	/**
	* Checks if the <code>User</code> is an admin
	*
	* @return <code>true</code> if the <code>User</code> is an admin
	*/
	public boolean isAdmin()
	{
		return this.admin;
	}
}