/**
* @author Kyle Ratti (PC18)
* @version 1.0, 01/07/14
*/

package com.chitchat.client.components;

import com.chitchat.Settings;
import com.chitchat.components.net.*;
import com.chitchat.components.user.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.UnknownHostException;

import java.util.HashMap;

import utilx.Prompt;
import utilx.Numbers;

/** Represents the client in the Server-Client model */
public class Client
{
	/** The version of the client */
	public static final double VERSION = 1.0;

	private final HashMap<String, String> serverVars;
	private final NetMessageManager netMessageManager;
	private ServerConnection serverConnection;
	private User user;

	/** Creates a new <code>Client</code> */
	public Client()
	{
		this.serverVars = new HashMap<String, String>();
		this.netMessageManager = new NetMessageManager();
		this.serverConnection = null;
		this.user = null;
	}

	/**
	* Gets the <code>NetMessageManager</code>
	*
	* @return The <code>NetMessageManager</code>
	*/
	public NetMessageManager getNetMessageManager()
	{
		return this.netMessageManager;
	}

	/**
	* Attempts to connect to the server
	*
	* @throws UnknownHostException, IOException
	*/
	public void connect() throws UnknownHostException, IOException
	{
		this.serverConnection = new ServerConnection(Settings.SERVER_HOST, Settings.SERVER_PORT, this);
		this.serverConnection.start();
	}

	/** Disconnects from the server */
	public void disconnect()
	{
		NetMessage objDisco = new NetMessage("Client.disconnect");
		objDisco.sendTo(this.serverConnection);
	}

	/** Sets up the Client */
	public void setup()
	{
		this.netMessageManager.addResponseHandler("Server.var", new NetMessageHandler()
		{
			@Override
			public void onDataReceived(String strAction, Connection objConnection, NetMessage objMsg)
			{
				NetData objData = objMsg.getData();
				String strKey = objData.getString();
				String strValue = objData.getString();

				Client.this.serverVars.put(strKey, strValue);

				System.out.println("Server Var received\n\t'" + strKey + "' => '" + strValue + "'");
			}
		});
	}

	/**
	* Sets up the <code>User</code> associated with this <code>Client</code>
	*
	* @param objUser The <code>User</code>
	*/
	public void setUser(User objUser)
	{
		this.user = objUser;
	}

	/**
	* Gets the <code>User</code> associated with this <code>Client</code>
	*
	* @return The <code>User</code>, or <code>null</code> if there isn't one
	*/
	public User getUser()
	{
		return this.user;
	}

	/**
	* Sends a login request to the server
	*
	* @param strUsername The username to log in with
	* @param strPassword The password to log in with
	* @param objHandler The handler for the server response
	*/
	public void doLogin(String strUsername, String strPassword, NetMessageHandler objHandler)
	{
		this.netMessageManager.addResponseHandler("User.login", objHandler);

		NetData objData = new NetData();
		objData.add(strUsername);
		objData.add(strPassword);

		NetMessage objLoginReq = new NetMessage("User.login");
			objLoginReq.setData(objData);
		objLoginReq.sendRequestTo(this.serverConnection);
	}

	/**
	* Sends a create account request to the server
	*
	* @param strUsername The username to create an account with
	* @param strPassword The password to create an account with
	* @param objHandler The handler for the server response
	*/
	public void doCreateAccount(String strUsername, String strPassword, NetMessageHandler objHandler)
	{
		this.netMessageManager.addResponseHandler("User.createAccount", objHandler);

		NetData objData = new NetData();
		objData.add(strUsername);
		objData.add(strPassword);

		NetMessage objCreateReq = new NetMessage("User.createAccount");
			objCreateReq.setData(objData);
		objCreateReq.sendRequestTo(this.serverConnection);
	}

	/**
	* Sends a chat message
	*
	* @param strMsg The message to send
	*/
	public void doSendMessage(String strMsg)
	{
		NetData objData = new NetData();
		objData.add(strMsg.substring(0, Numbers.clamp(strMsg.length(), 0, Settings.MAX_MSG_LEN + 1)));

		NetMessage objSendMsg = new NetMessage("User.sendMessage");
			objSendMsg.setData(objData);
		objSendMsg.sendTo(this.serverConnection);
	}

	/** Sends a request to the server for shared variables */
	public void getServerVars()
	{
		NetMessage objServerInfo = new NetMessage("Server.var");
		objServerInfo.sendRequestTo(this.serverConnection);
	}
}