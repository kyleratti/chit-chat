/**
* @author Kyle Ratti (PC18)
* @version 1.0, 01/07/14
*/

package com.chitchat.components.net;

import java.io.IOException;
import java.io.ObjectOutputStream;

import java.util.ArrayList;

/** Represents a network message */
public class NetMessage
{
	/** Represents a request */
	public static final int REQUEST = 0;
	/** Represents a response */
	public static final int RESPONSE = 1;
	/** Represents a ping */
	public static final int PING = 2;

	private final String action;
	private NetData netData;

	/**
	* Creates a new <code>NetMessage</code> for the specified action
	*
	* @param strAction The action
	*/
	public NetMessage(String strAction)
	{
		this.action = strAction;
	}

	/**
	* Sets the <code>NetData</code> associated with this <code>NetMessage</code>
	*
	* @param objData The <code>NetData</code>
	*/
	public void setData(NetData objData)
	{
		this.netData = objData;
	}

	/**
	* Gets the <code>NetData</code> associated with this <code>NetMessage</code>, if any
	*
	* @return The <code>NetData</code>, if any
	*/
	public NetData getData()
	{
		return this.netData;
	}

	/**
	* Checks if the <code>NetMessage</code> is empty (has no <code>NetData</code>)
	*
	* @return <code>true</code> if the message is empty
	*/
	public boolean isEmpty()
	{
		return this.netData.size() == 0;
	}

	/**
	* Sends this <code>NetMessage</code> to the specified <code>Connection</code>
	*
	* @param objConnection The <code>Connection</code>
	*/
	public void sendRequestTo(Connection objConnection)
	{
		ObjectOutputStream objOutput = objConnection.getOutputStream();
		
		try
		{
			objOutput.writeInt(NetMessage.REQUEST);
			this.writeDataTo(objConnection);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.err.println("Error writing (socket dead?)");
		}
	}

	/**
	* Sends this <code>NetMessage</code> to the specified <code>Connection</code>
	*
	* @param objConnections The <code>Connection</code>
	*/
	public void sendRequestTo(ArrayList<Connection> objConnections)
	{
		for(Connection c : objConnections)
		{
			this.sendRequestTo(c);
		}
	}

	/**
	* Sends this <code>NetMessage</code> to the specified <code>Connection</code>
	*
	* @param objConnection The <code>Connection</code>
	*/
	public void sendResponseTo(Connection objConnection)
	{
		ObjectOutputStream objOutput = objConnection.getOutputStream();
		
		try
		{
			objOutput.writeInt(NetMessage.RESPONSE);
			this.writeDataTo(objConnection);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.err.println("Error writing (socket dead?)");
		}
	}

	/**
	* Sends this <code>NetMessage</code> to the specified <code>Connection</code>
	*
	* @param objConnections The <code>Connection</code>
	*/
	public void sendResponseTo(ArrayList<Connection> objConnections)
	{
		for(Connection c : objConnections)
		{
			this.sendResponseTo(c);
		}
	}

	/**
	* Sends this <code>NetMessage</code> to the specified <code>Connection</code>
	*
	* @param objConnection The <code>Connection</code>
	*/
	public void sendTo(Connection objConnection)
	{
		ObjectOutputStream objOutput = objConnection.getOutputStream();

		try
		{
			objOutput.writeInt(NetMessage.PING);
			this.writeDataTo(objConnection);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.err.println("Error writing (socket dead?)");
		}
	}

	/**
	* Sends this <code>NetMessage</code> to the specified <code>Connection</code>
	*
	* @param objConnections The <code>Connection</code>
	*/
	public void sendTo(ArrayList<Connection> objConnections)
	{
		for(Connection c : objConnections)
		{
			this.sendTo(c);
		}
	}

	private void writeDataTo(Connection objConnection)
	{
		ObjectOutputStream objOutput = objConnection.getOutputStream();

		try
		{
			objOutput.writeUTF(this.action);

			if(this.netData != null && !this.isEmpty())
			{
				objOutput.writeInt(this.netData.size());
				//objOutput.writeObject(this.netData);
			}
			else
				objOutput.writeInt(0);

			if(this.netData != null && !this.isEmpty())
			{
				for(Object obj : this.netData.getAll())
				{
					objOutput.writeObject(obj);
				}
			}

			objOutput.flush();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.err.println("Error writing (socket dead?)");
		}
	}
}