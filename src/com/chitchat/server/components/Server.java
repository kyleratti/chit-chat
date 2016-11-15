/**
* @author Kyle Ratti (PC18)
* @version 1.0, 12/14/13
*/

package com.chitchat.server.components;

import com.chitchat.Settings;
import com.chitchat.components.*;
import com.chitchat.components.net.*;
import com.chitchat.components.user.*;
import com.chitchat.server.components.*;

import java.net.ServerSocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.HashMap;

import utilx.Numbers;
import utilx.sql.Derpy;
import utilx.sql.ClassLoadException;

/** The server for Chit Chat */
public class Server
{
	/** The version of the server */
	public static final double VERSION = 1.0;

	private final HashMap<String, Object> vars;
	private final NetMessageManager netMessageManager;
	private final int port;
	private final ServerSocket serverSocket;
	private final ChatHistory chatHistory;
	private ConnectionListener connectionListener;

	/**
	* Creates a new Chit Chat server on the specified port
	*
	* @param objVars The server variables
	* @throws IOException, SQLException, ClassLoadException
	*/
	public Server(HashMap<String, Object> objVars) throws IOException, SQLException, ClassLoadException
	{
		this.vars = objVars;
		this.netMessageManager = new NetMessageManager();
		this.port = (this.vars.containsKey("port") ? (Integer) this.vars.get("port") : Settings.SERVER_PORT);
		this.serverSocket = new ServerSocket(this.port);
		this.chatHistory = new ChatHistory(25);
	}

	/** Initializes stuff for the server */
	public static void initialize()
	{
		DBInterface.setup();
	}

	/** Sets up the server */
	public void setup()
	{
		this.netMessageManager.addRequestHandler("Server.var", new NetMessageHandler()
		{
			@Override
			public void onDataReceived(String strAction, Connection objConnection, NetMessage objMsg)
			{
				NetData objData = new NetData();
				objData.add("motd");
				objData.add("America is all about speed. Hot, nasty, bad*** speed. -Eleanor Roosevelt, 1936");

				NetMessage objServerData = new NetMessage("Server.var");
					objServerData.setData(objData);
				objServerData.sendResponseTo(objConnection);
			}
		});

		this.netMessageManager.addRequestHandler("User.login", new NetMessageHandler()
		{
			@Override
			public void onDataReceived(String strAction, Connection objConnection, NetMessage objMsg)
			{
				NetData objData = objMsg.getData();

				String strUsername = objData.getString();
				String strPassword = objData.getString();

				NetMessage objLoginResponse = new NetMessage("User.login");
				NetData objResponseData = new NetData();

				if(UserDAO.exists(strUsername))
				{
					try
					{
						User objUser = UserDAO.get(strUsername, strPassword);

						objResponseData.add(objUser);
						Server.this.onUserLoggedIn(objConnection, objUser);
					}
					catch(UserNotFoundException e)
					{
						objResponseData.add(new UserNotFoundException());
					}
					catch(InvalidPasswordException e)
					{
						objResponseData.add(new InvalidPasswordException());
					}
				}
				else
					objResponseData.add(new UserNotFoundException());

				objLoginResponse.setData(objResponseData);
				objLoginResponse.sendResponseTo(objConnection);
			}
		});

		this.netMessageManager.addRequestHandler("User.createAccount", new NetMessageHandler()
		{
			@Override
			public void onDataReceived(String strAction, Connection objConnection, NetMessage objMsg)
			{
				NetData objData = objMsg.getData();

				String strUsername = objData.getString();
				String strPassword = objData.getString();

				NetMessage objCreateResponse = new NetMessage("User.createAccount");
				NetData objResponseData = new NetData();

				if(UserDAO.exists(strUsername))
				{
					objResponseData.add(new UserExistsException());
					System.out.println("User exists!");
				}
				else
				{
					try
					{
						User objUser = UserDAO.add(strUsername, strPassword);
						objResponseData.add(objUser);
						System.out.println("Added user");
					}
					catch(UserExistsException e)
					{
						objResponseData.add(new UserExistsException());
					}
				}

				objCreateResponse.setData(objResponseData);
				objCreateResponse.sendResponseTo(objConnection);
			}
		});

		this.netMessageManager.addPingHandler("User.sendMessage", new NetMessageHandler()
		{
			@Override
			public void onDataReceived(String strAction, Connection objConnection, NetMessage objMsg)
			{
				NetData objData = objMsg.getData();
				String strMsg = objData.getString();

				if(strMsg == null) return;

				strMsg = strMsg.substring(0, Numbers.clamp(strMsg.length(), 0, Settings.MAX_MSG_LEN + 1));

				ChatMessage objChatMsg = new ChatMessage(strMsg, objConnection.getUser());
				Server.this.chatHistory.addMessage(objChatMsg);

				Server.this.onChatMessage(objChatMsg);
			}
		});
	}

	/**
	* Gets the <code>ServerSocket</code>
	*
	* @return The <code>ServerSocket</code>
	*/
	public ServerSocket getServerSocket()
	{
		return this.serverSocket;
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

	private void onChatMessage(ChatMessage objChatMsg)
	{
		NetData objData = new NetData();
		objData.add(objChatMsg);

		NetMessage objBroadcast = new NetMessage("Chat.userMessage");

		if(objChatMsg.getUser() == null);
			objBroadcast = new NetMessage("Chat.systemMessage");

		objBroadcast.setData(objData);
		objBroadcast.sendTo(this.connectionListener.getConnections());
	}

	private void onUserLoggedIn(Connection objConnection, User objUser)
	{
		objConnection.setUser(objUser);

		NetData objData = new NetData();
		objData.add(objUser);

		NetMessage objUserLoggedIn = new NetMessage("User.connected");
		objUserLoggedIn.setData(objData);
		objUserLoggedIn.sendTo(this.connectionListener.getConnections());
	}

	/**
	* Called when the client disconnects from the server
	*
	* @param objConnection The <code>Connection</code>
	*/
	public void onClientDisconnected(Connection objConnection)
	{
		System.out.println("Client disconnected");

		User objUser = objConnection.getUser();

		if(objUser == null) return;

		NetData objData = new NetData();
		objData.add(objUser);

		NetMessage objUserDisco = new NetMessage("User.disconnected");
		objUserDisco.sendTo(this.connectionListener.getConnections());
	}

	/**
	* Gets the specified variable
	*
	* @param strKey The key
	* @return The <code>Object</code>
	*/
	public Object getVar(String strKey)
	{
		return this.vars.containsKey(strKey) ? this.vars.get(strKey) : null;
	}

	/**
	* Starts the server and begins listening for connections
	*
	* @throws ServerRunningException
	*/
	public void start() throws ServerRunningException
	{
		if(this.serverSocket == null)
			throw new RuntimeException("Something is seriously wrong");

		if(this.connectionListener == null)
		{
			this.connectionListener = new ConnectionListener(this);
			this.connectionListener.start();
		}
		else
			throw new ServerRunningException();
	}

	/**
	* Stops the server and closes all sockets
	*
	* @throws ServerNotRunningException
	*/
	public void stop() throws ServerNotRunningException
	{
		if(this.serverSocket == null)
			throw new RuntimeException("Something is seriously wrong");
		else
		{
			if(this.serverSocket.isClosed())
				throw new ServerNotRunningException();
			else
			{
				try
				{
					this.serverSocket.close();
				}
				catch(IOException e)
				{
					System.out.println("Unable to close ServerSocket (already dead?)");
				}
			}
		}

		if(this.connectionListener == null)
			throw new ServerNotRunningException();
		else
			this.connectionListener.close();
	}

	/**
	* Checks if the server is running
	*
	* @return <code>true</code> if the server is running
	*/
	public boolean isRunning()
	{
		return (this.serverSocket != null && !this.serverSocket.isClosed());
	}
}