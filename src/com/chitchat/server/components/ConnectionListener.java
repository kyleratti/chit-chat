/**
* @author Kyle Ratti (PC18)
* @version 1.0, 12/14/13
*/

package com.chitchat.server.components;

import com.chitchat.components.net.*;
import com.chitchat.server.components.Server;

import java.util.ArrayList;

import java.io.IOException;

import java.net.Socket;
import java.net.ServerSocket;

/** The connection listener for Chit Chat */
public class ConnectionListener extends Thread
{
	private final Server server;
	private final ServerSocket serverSocket;
	private final ArrayList<ClientConnection> connections;
	private boolean running;

	/**
	* Creates a new connection listener for the specified <code>ServerSocket</code>
	*
	* @param objServer The <code>ServerSocket</code> to listen on
	*/
	public ConnectionListener(Server objServer)
	{
		this.server = objServer;
		this.serverSocket = objServer.getServerSocket();
		this.connections = new ArrayList<ClientConnection>();
		this.running = true;
	}

	public ArrayList<Connection> getConnections()
	{
		ArrayList<Connection> objConnections = new ArrayList<Connection>();

		for(Connection c : this.connections)
			objConnections.add(c);

		return objConnections;
	}

	public Server getServer()
	{
		return this.server;
	}

	public void onClientDisconnected(ClientConnection objConnection)
	{
		this.server.onClientDisconnected(objConnection);
		objConnection.close();
		this.connections.remove(objConnection);
	}

	@Override
	public void run()
	{
		while(this.running)
		{
			try
			{
				Socket objClientSocket = this.serverSocket.accept();
				ClientConnection objClient = new ClientConnection(objClientSocket, this);
				objClient.start();
				System.out.println("Client connected");
				this.connections.add(objClient);
			}
			catch(IOException e)
			{
				//System.out.println("Unable to accept socket (already dead?)");
			}
		}
	}

	/** Closes this Connection Listener and all of the sockets connected to it */
	public void close()
	{
		if(this.running)
		{
			this.running = false;

			for(ClientConnection objClient : this.connections)
			{
				if(objClient != null)
				{
					objClient.close();
				}
			}
			
			this.connections.clear();
		}
	}
}