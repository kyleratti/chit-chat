/**
* @author Kyle Ratti (PC18)
* @version 1.0, 12/16/13
*/

package com.chitchat.server.components;

import com.chitchat.components.user.*;

import java.util.ArrayList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/** The Database Access Object for the <code>users</code> table */
public class UserDAO
{
	/**
	* Gets all users from the database
	*
	* @return An <code>ArrayList</code> of <code>User</code> objects
	*/
	public static ArrayList<User> getAll()
	{
		ArrayList<User> objUsers = new ArrayList<User>();

		try
		{
			ResultSet objResults = DBInterface.execute("SELECT * FROM users ORDER BY id ASC");

			while(objResults.next())
			{
				objUsers.add(new User(objResults.getInt("id"), objResults.getString("username"), objResults.getBoolean("admin")));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}

		return objUsers;
	}

	/**
	* Gets the specified user from the database
	*
	* @param strUsername The username
	* @param strPassword The password
	* @return The <code>User</code>
	*/
	public static User get(String strUsername, String strPassword) throws UserNotFoundException, InvalidPasswordException
	{
		if(!UserDAO.exists(strUsername))
			throw new UserNotFoundException();

		try
		{
			PreparedStatement objQuery = DBInterface.prepare("SELECT %s FROM %s WHERE %s = ? AND %s = ?", DBInterface.getCol("Users.id"), DBInterface.getCol("Users"), DBInterface.getCol("Users.username"), DBInterface.getCol("Users.password"));
			objQuery.setString(1, strUsername);
			objQuery.setString(2, strPassword);

			ResultSet objResults = objQuery.executeQuery();

			if(objResults.next())
			{
				return UserDAO.get(objResults.getInt(DBInterface.getCol("Users.id")));
			}
			else
				throw new InvalidPasswordException();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	* Gets the specified user and all of their permissions from the database
	*
	* @param iTarget The target UserID
	* @return The <code>User</code>
	*/
	public static User get(int iTarget) throws UserNotFoundException
	{
		try
		{
			PreparedStatement objQuery = DBInterface.prepare("SELECT * FROM %s WHERE %s = ?", DBInterface.getCol("Users"), DBInterface.getCol("Users.id"));
			objQuery.setInt(1, iTarget);

			ResultSet objResults = objQuery.executeQuery();

			if(objResults.next())
			{
				return new User(objResults.getInt(DBInterface.getCol("Users.id")), objResults.getString(DBInterface.getCol("Users.username")), objResults.getBoolean(DBInterface.getCol("Users.admin")));
			}
			else
				throw new UserNotFoundException();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	* Checks if a user exists
	*
	* @param strUsername The username to check for
	* @return <code>true</code> if the user exists
	*/
	public static boolean exists(String strUsername)
	{
		try
		{
			PreparedStatement objQuery = DBInterface.prepare("SELECT %s FROM %s WHERE %s = ?", DBInterface.getCol("Users.id"), DBInterface.getCol("Users"), DBInterface.getCol("Users.username"));
			objQuery.setString(1, strUsername);

			return objQuery.executeQuery().next();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}

		return false;
	}

	/**
	* Adds a new user to the database
	*
	* @param strUsername The username for the new user
	* @param strPassword The password for the new user
	* @return The new <code>User</code>
	*/
	public static User add(String strUsername, String strPassword) throws UserExistsException
	{
		User objUser = null;

		try
		{
			if(UserDAO.exists(strUsername))
				throw new UserExistsException();

			PreparedStatement objInsert = DBInterface.prepare("INSERT INTO %s (%s, %s, %s) VALUES(?, ?, ?)", DBInterface.getCol("Users"), DBInterface.getCol("Users.username"), DBInterface.getCol("Users.password"), DBInterface.getCol("Users.admin"));
			objInsert.setString(1, strUsername);
			objInsert.setString(2, strPassword);
			objInsert.setBoolean(3, false);
			objInsert.executeUpdate();

			return UserDAO.get(strUsername, strPassword);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}

		return objUser;
	}
}