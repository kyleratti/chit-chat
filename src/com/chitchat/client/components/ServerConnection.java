/**
* @author Kyle Ratti (PC18)
* @version 1.0, 01/07/14
*/

package com.chitchat.client.components;

import com.chitchat.components.*;
import com.chitchat.components.net.*;
import com.chitchat.components.user.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.UnknownHostException;

/** Represents a connection to the server */
public class ServerConnection extends Connection
{
	private final String host;
	private final int port;
	private final Client client;
	private final Socket socket;
	private final ObjectInputStream inputStream;
	private final ObjectOutputStream outputStream;

	/**
	* Creates a new <code>ServerConnection</code> on the specified host/port
	*
	* @param strHost The host to connect to
	* @param iPort The port to connect to the host on
	* @param objClient The client this connection is associated with
	* @throws UnknownHostException, IOException
	*/
	public ServerConnection(String strHost, int iPort, Client objClient) throws UnknownHostException, IOException
	{
		super("Server Connection");
		this.host = strHost;
		this.port = iPort;
		this.client = objClient;
		this.socket = new Socket(strHost, iPort);
		this.inputStream = new ObjectInputStream(this.socket.getInputStream());
		this.outputStream = new ObjectOutputStream(this.socket.getOutputStream());
		this.outputStream.flush();
	}

	/**
	* Gets the host IP address
	*
	* @return The host IP address
	*/
	public String getHost()
	{
		return this.host;
	}

	/**
	* Gets the port the connection should be made to
	*
	* @return The port
	*/
	public int getPort()
	{
		return this.port;
	}

	/**
	* Gets the <code>ObjectInputStream</code> this connection uses
	*
	* @return The <code>ObjectInputStream</code>
	*/
	public ObjectInputStream getInputStream()
	{
		return this.inputStream;
	}

	/**
	* Gets the <code>ObjectOutputStream</code> this connection uses
	*
	* @return The <code>ObjectOutputStream</code>
	*/
	public ObjectOutputStream getOutputStream()
	{
		return this.outputStream;
	}

	@Override
	public void run()
	{
		while(this.socket != null && this.socket.isConnected())
		{
			try
			{
				int iType = this.inputStream.readInt();
				String strAction = this.inputStream.readUTF();
				int iSize = this.inputStream.readInt();
				NetData objData = new NetData();

				try
				{
					for(int i = 0; i < iSize; i++)
					{
						objData.add(this.inputStream.readObject());
					}
				}
				catch(ClassNotFoundException e)
				{
					System.err.println("Error receiving NetData (class not found!)");
				}

				NetMessage objMessage = new NetMessage(strAction);
				objMessage.setData(objData);

				NetMessageManager objManager = this.client.getNetMessageManager();

				if(iType == NetMessage.REQUEST)
					objManager.handleRequest(strAction, this, objMessage);
				else if(iType == NetMessage.RESPONSE)
					objManager.handleResponse(strAction, this, objMessage);
				else if(iType == NetMessage.PING)
					objManager.handlePing(strAction, this, objMessage);
				else
					System.err.println("Error handling type?!?!?");
			}
			catch(IOException e)
			{
				System.err.println("Lost connection to server");
				break;
			}
		}
	}
}