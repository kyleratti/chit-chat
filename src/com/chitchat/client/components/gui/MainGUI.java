/**
* @author Kyle Ratti (PC18)
* @version 1.0, 01/07/14
*/

package com.chitchat.client.components.gui;

import com.chitchat.components.user.*;
import com.chitchat.client.components.gui.*;
import com.chitchat.client.components.Client;

import net.miginfocom.swing.MigLayout;

import java.net.UnknownHostException;

import java.io.IOException;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.HashMap;

import utilx.Prompt;

/** Represents the MainGUI */
public class MainGUI
{
	/** The internal name of the Login Panel */
	public static final String PANEL_LOGIN = "Login";
	/** The internal name of the Create Account panel */
	public static final String PANEL_CREATE_ACCOUNT = "CreateAccount";
	/** The internal name of the Message Room panel */
	public static final String PANEL_MESSAGE_ROOM = "MessageRoom";

	private final HashMap<String, ChitChatPanel> panels;

	private final JFrame frame;
	private final JPanel cards;
	private Client client;
	private User user;

	/** Creates a new MainGUI object */
	public MainGUI()
	{
		Prompt.setUseDialogs(true);

		this.user = null;
		this.client = new Client();

		this.setup();

		this.client.setup();
		this.client.getServerVars();

		this.panels = new HashMap<String, ChitChatPanel>();
		this.panels.put(MainGUI.PANEL_LOGIN, new LoginForm(this));
		this.panels.put(MainGUI.PANEL_CREATE_ACCOUNT, new CreateAccountForm(this));
		this.panels.put(MainGUI.PANEL_MESSAGE_ROOM, new MessageRoom(this));

		this.frame = new JFrame("chit chat v" + Client.VERSION);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(550, 350);
		this.frame.setResizable(false);

		this.cards = new JPanel(new CardLayout());
		this.cards.add(this.panels.get(MainGUI.PANEL_LOGIN), MainGUI.PANEL_LOGIN);
		this.cards.add(this.panels.get(MainGUI.PANEL_CREATE_ACCOUNT), MainGUI.PANEL_CREATE_ACCOUNT);
		this.cards.add(this.panels.get(MainGUI.PANEL_MESSAGE_ROOM), MainGUI.PANEL_MESSAGE_ROOM);

		this.frame.getContentPane().add(this.cards);
		
		this.showPanel(MainGUI.PANEL_LOGIN);
	}

	/**
	* Shows (swaps to) the specified panel
	*
	* @param strName The internal name of the panel
	*/
	public void showPanel(String strName)
	{
		CardLayout objLayout = (CardLayout) this.cards.getLayout();
		objLayout.show(this.cards, strName);

		if(this.panels.containsKey(strName))
		{
			ChitChatPanel objPanel = this.panels.get(strName);

			this.frame.setSize(objPanel.getWidth(), objPanel.getHeight());
			objPanel.onShow();
			this.frame.setLocationRelativeTo(null);
		}
	}

	private void setup()
	{
		try
		{
			this.client = new Client();
			this.client.connect();
		}
		catch(UnknownHostException e)
		{
			Prompt.showError("Unknown host - is the server online?");
			System.exit(0);
		}
		catch(IOException e)
		{
			Character objResponse = Prompt.getYesNo("Connection to server failed. Would you like to retry?");

			if(objResponse != null && objResponse.equals('y'))
			{
				this.setup();
				return;
			}
			else
				System.exit(0);
		}
	}

	/** Shows the MainGUI */
	public void show()
	{
		this.frame.setVisible(true);
	}

	/** Hides the MainGUI */
	public void hide()
	{
		this.frame.setVisible(false);
	}

	/**
	* Sets this session's <code>User</code> object
	*
	* @param objUser The <code>User</code>
	*/
	public void setUser(User objUser)
	{
		this.client.setUser(objUser);
	}

	/**
	* Gets the <code>Client</code> that should be used
	*
	* @return The <code>Client</code>
	*/
	public Client getClient()
	{
		return this.client;
	}
}