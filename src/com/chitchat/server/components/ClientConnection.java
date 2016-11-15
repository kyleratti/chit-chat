/**
* @author Kyle Ratti (PC18)
* @version 1.0, 12/14/13
*/

package com.chitchat.server.components;

import com.chitchat.components.*;
import com.chitchat.components.net.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;

/** A client's connection to the server */
public class ClientConnection extends Connection
{
	private final Socket socket;
	private final ConnectionListener connectionListener;
	private final ObjectInputStream inputStream;
	private final ObjectOutputStream outputStream;

	/**
	* Creates a new <code>ClientConnection</code> for the specified <code>Socket</code>
	*
	* @param objSocket The <code>Socket</code>
	* @param objListener The <code>ConnectionListener</code>
	* @throws IOException
	*/
	public ClientConnection(Socket objSocket, ConnectionListener objListener) throws IOException
	{
		super("Client Connection");
		this.socket = objSocket;
		this.connectionListener = objListener;
		this.outputStream = new ObjectOutputStream(objSocket.getOutputStream());
		this.outputStream.flush();
		this.inputStream = new ObjectInputStream(objSocket.getInputStream());
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

	/**
	* Gets the <code>Socket</code>
	*
	* @return The <code>Socket</code>
	*/
	public Socket getSocket()
	{
		return this.socket;
	}

	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				int iType = this.inputStream.readInt();
				String strAction = this.inputStream.readUTF();
				int iSize = this.inputStream.readInt();
				NetData objData = new NetData();

				if(iSize > 0)
				{
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
				}

				NetMessage objMessage = new NetMessage(strAction);
				objMessage.setData(objData);

				NetMessageManager objManager = this.connectionListener.getServer().getNetMessageManager();

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
				//e.printStackTrace();
				this.connectionListener.onClientDisconnected(this);
				break;
			}
		}
	}

	/** Attempts to close this connection */
	public void close()
	{
		try
		{
			this.socket.close();
		}
		catch(IOException e)
		{
			System.out.println("Unable to close ClientConnection (???)");
		}
	}
}