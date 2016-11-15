/**
* @author Kyle Ratti (PC18)
* @version 1.0, 12/06/13
*/

package com.chitchat.server.components;

import com.chitchat.Settings;

import utilx.Prompt;

import utilx.sql.ClassLoadException;
import utilx.sql.Derpy;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.HashMap;

/** An interface for the database */
public class DBInterface
{
	private static Derpy connection;
	private static HashMap<String, String> cols;

	/** Sets up the connection to the database */
	public static void setup()
	{
		try
		{
			DBInterface.connection = new Derpy(Settings.SERVER_DB);
			DBInterface.connection.connect();
		}
		catch(ClassLoadException e)
		{
			System.out.println("!!! Derby driver not found, please install it");
		}
		catch(SQLException e)
		{
			System.out.println("!!! Error connecting to database");
		}

		DBInterface.cols = new HashMap<String, String>();

		// Users
		DBInterface.addCol("Users");
		DBInterface.addCol("Users.id", "id");
		DBInterface.addCol("Users.username", "username");
		DBInterface.addCol("Users.password", "password");
		DBInterface.addCol("Users.admin", "admin");
	}

	/**
	* Maps the specified internal name to its lowercase counterpart in the database
	*
	* @param strInternal The internal name of the table (ie. Users.id)
	*/
	private static void addCol(String strInternal)
	{
		DBInterface.addCol(strInternal, strInternal.toLowerCase());
	}

	/**
	* Maps the specified internal name to the table's name in the database
	*
	* @param strInternal The internal name of the table (ie. Users.id)
	* @param strDB The table's name inside the database (ie. id)
	*/
	private static void addCol(String strInternal, String strDB)
	{
		DBInterface.cols.put(strInternal, strDB);
	}

	/**
	* Gets the specified table's name in the database
	*
	* @param strInternal The internal name of the table
	* @return The table's name in the database
	*/
	public static String getCol(String strInternal)
	{
		return (DBInterface.cols.containsKey(strInternal) ? DBInterface.cols.get(strInternal) : "");
	}

	/**
	* Prepare a statement with the database
	*
	* @param strQuery The query to the database
	* @param args The data to format the query with
	* @return A <code>PreparedStatement</code>
	*/
	public static PreparedStatement prepare(String strQuery, Object... args)
	{
		return DBInterface.prepare(String.format(strQuery, args));
	}

	/**
	* Prepare a statement with the database
	*
	* @param strQuery The query to the database
	* @return A <code>PreparedStatement</code>
	*/
	public static PreparedStatement prepare(String strQuery)
	{
		if(DBInterface.connection == null) return null;

		try
		{
			return DBInterface.connection.prepare(strQuery);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	* Executes a query on the database
	*
	* @param strQuery The query to the database
	* @param args The data to format the query with
	* @return A <code>ResultSet</code> containing the query results, if any
	*/
	public static ResultSet execute(String strQuery, Object... args)
	{
		return DBInterface.execute(String.format(strQuery, args));
	}

	/**
	* Executes a query on the database
	*
	* @param strQuery The query to the database
	* @return A <code>ResultSet</code> containing the query results, if any
	*/
	public static ResultSet execute(String strQuery)
	{
		if(DBInterface.connection == null) return null;

		try
		{
			return DBInterface.connection.prepare(strQuery).executeQuery();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}

		return null;
	}
}