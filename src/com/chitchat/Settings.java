/**
* @author Kyle Ratti (PC18)
* @version 1.0, 12/14/13
*/

package com.chitchat;

import java.util.Date;
import java.text.SimpleDateFormat;

/** Settings for Chit Chat */
public interface Settings
{
	/** The IP/hostname the server is running on */
	public static final String SERVER_HOST = "127.0.0.1";
	/** The port the server is running on */
	public static final int SERVER_PORT = 25565;
	/** The database the server should use */
	public static final String SERVER_DB = "ChitChatDB";
	/** The maximum message length */
	public static final int MAX_MSG_LEN = 128;
	/** The object to format date/times with */
	public static final SimpleDateFormat dateTime = new SimpleDateFormat("hh:mm:ss");
}