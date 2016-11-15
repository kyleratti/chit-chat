/**
* @author Kyle Ratti (PC18)
* @version 1.0, 01/10/14
*/

package com.chitchat.components.net;

import java.util.HashMap;

import java.io.IOException;

/** A handler for net messages */
public class NetMessageManager
{
	private final HashMap<String, NetMessageHandler> requests;
	private final HashMap<String, NetMessageHandler> responses;
	private final HashMap<String, NetMessageHandler> pings;

	/** Creates a new <code>NetMessageManager</code> */
	public NetMessageManager()
	{
		this.requests = new HashMap<String, NetMessageHandler>();
		this.responses = new HashMap<String, NetMessageHandler>();
		this.pings = new HashMap<String, NetMessageHandler>();
	}

	/**
	* Adds a request handler
	*
	* @param strAction The action to handle
	* @param objHandler The handler
	*/
	public void addRequestHandler(String strAction, NetMessageHandler objHandler)
	{
		this.requests.put(strAction, objHandler);
	}

	/**
	* Adds a response handler
	*
	* @param strAction The action to handle
	* @param objHandler The handler
	*/
	public void addResponseHandler(String strAction, NetMessageHandler objHandler)
	{
		this.responses.put(strAction, objHandler);
	}

	/**
	* Adds a ping handler
	*
	* @param strAction The action to handle
	* @param objHandler The handler
	*/
	public void addPingHandler(String strAction, NetMessageHandler objHandler)
	{
		this.pings.put(strAction, objHandler);
	}

	/**
	* Handles the specified request
	*
	* @param strAction The action to handle
	* @param objConnection The connection sending the data
	* @param objMsg The <code>NetMessage</code>
	*/
	public void handleRequest(String strAction, Connection objConnection, NetMessage objMsg)
	{
		if(this.requests.containsKey(strAction))
			this.requests.get(strAction).onDataReceived(strAction, objConnection, objMsg);
		else
			System.err.println("Received unhandled request '" + strAction + "'");
	}

	/**
	* Handles the specified response
	*
	* @param strAction The action to handle
	* @param objConnection The connection sending the data
	* @param objMsg The <code>NetMessage</code>
	*/
	public void handleResponse(String strAction, Connection objConnection, NetMessage objMsg)
	{
		if(this.responses.containsKey(strAction))
			this.responses.get(strAction).onDataReceived(strAction, objConnection, objMsg);
		else
			System.err.println("Received unhandled response '" + strAction + "'");
	}

	/**
	* Handles the specified ping
	*
	* @param strAction The action to handle
	* @param objConnection The connection sending the data
	* @param objMsg The <code>NetMessage</code>
	*/
	public void handlePing(String strAction, Connection objConnection, NetMessage objMsg)
	{
		if(this.pings.containsKey(strAction))
			this.pings.get(strAction).onDataReceived(strAction, objConnection, objMsg);
		else
			System.err.println("Received unhandled ping '" + strAction + "'");
	}
}