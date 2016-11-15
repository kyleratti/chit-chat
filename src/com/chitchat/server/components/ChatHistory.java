/**
* @author Kyle Ratti (PC18)
* @version 1.0, 01/14/14
*/

package com.chitchat.server.components;

import com.chitchat.components.ChatMessage;

public class ChatHistory
{
	private final int limit;
	private final ChatMessage[] history;
	private int index;

	/**
	* Creates a new <code>ChatHistory</code>
	*
	* @param iLimit The maximum number of <code>ChatMessage</code> objects to keep
	*/
	public ChatHistory(int iLimit)
	{
		this.limit = iLimit;
		this.history = new ChatMessage[iLimit];
		this.index = 0;
	}

	/**
	* Add the specified message
	*
	* @param objMsg The <code>ChatMessage</code> to add
	*/
	public void addMessage(ChatMessage objMsg)
	{
		this.history[this.index] = objMsg;

		if(this.index >= this.limit - 1)
			this.index = 0;
		else
			this.index++;
	}

	/**
	* Gets all of the <code>ChatMessage</code> objects
	*
	* @return The <code>ChatMessage</code> objects
	*/
	public ChatMessage[] getAll()
	{
		return this.history;
	}

	/**
	* Gets the last <code>ChatMessage</code> added
	*
	* @return The last <code>ChatMessage</code>
	*/
	public ChatMessage getLast()
	{
		return this.history[this.index];
	}
}