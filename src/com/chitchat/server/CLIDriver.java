/**
* @author Kyle Ratti (PC18)
* @version 1.0, 12/16/13
*/

package com.chitchat.server;

import com.chitchat.Settings;

import com.chitchat.components.user.User;
import com.chitchat.components.user.UserExistsException;

import com.chitchat.server.components.Server;

import java.io.IOException;

import java.sql.SQLException;

import java.util.HashMap;

import utilx.cli.*;
import utilx.forms.*;
import utilx.Prompt;
import utilx.Numbers;
import utilx.sql.ClassLoadException;

public class CLIDriver
{
	private static final HashMap<String, Object> serverVars = new HashMap<String, Object>();
	private static Server server;

	public static void main(String[] args)
	{
		CLIDriver.setup();
	}

	private static void setup()
	{
		CLIDriver.serverVars.put("port", Settings.SERVER_PORT);
		CLI objCLI = new CLI("chit chat v" + Server.VERSION + "\n\twritten by Kyle Ratti\n\n");

		CLICommand objServerCmd = new CLICommand("server", "Server-related stuff");
		objServerCmd.addArgument(new CLIArgument("status", "Server status")
		{
			@Override
			public void handle(String strCmd, String strArg, String[] arrArgs)
			{
				CLIDriver.showServerStatus();
			}
		});
		objServerCmd.addArgument(new CLIArgument("start", "Start the server")
		{
			@Override
			public void handle(String strCmd, String strArg, String[] arrArgs)
			{
				CLIDriver.startServer();
			}
		});
		objServerCmd.addArgument(new CLIArgument("stop", "Stop the server")
		{
			@Override
			public void handle(String strCmd, String strArg, String[] arrArgs)
			{
				CLIDriver.stopServer();
			}
		});
		objServerCmd.addArgument(new CLIArgument("get", "Get a server variable")
		{
			@Override
			public void handle(String strCmd, String strArg, String[] arrArgs)
			{
				if(arrArgs.length != 1)
					throw new InvalidUsageException("Expected 1 argument (key)");

				String strKey = arrArgs[0];

				if(CLIDriver.serverVars.containsKey(strKey))
					Prompt.println("%s = %s", strKey, CLIDriver.serverVars.get(strKey));
				else
					Prompt.println("Unknown key '%s'", strKey);
			}
		});
		objServerCmd.addArgument(new CLIArgument("set", "Set a server variable")
		{
			@Override
			public void handle(String strCmd, String strArg, String[] arrArgs)
			{
				if(arrArgs.length != 2)
					throw new InvalidUsageException("Unsupported number of arguments (expected 'key' 'value')");
				else if(CLIDriver.server != null && CLIDriver.server.isRunning())
					throw new InvalidUsageException("Server must be stopped to set variables");
				else
				{
					String strKey = arrArgs[0].toLowerCase();
					Object objValue = arrArgs[1];

					if(strKey.equalsIgnoreCase("port"))
					{
						try
						{
							objValue = Numbers.clamp(Integer.parseInt(arrArgs[2]), 1, 65565);
						}
						catch(NumberFormatException e)
						{
							throw new InvalidUsageException("Received unexpected input (expected number)");
						}
					}

					CLIDriver.serverVars.put(strKey, objValue);
					Prompt.println("Server var '%s' set to '%s'", strKey, objValue);
				}
			}
		});
		objServerCmd.addArgument(new CLIArgument("console", "See the live server console")
		{
			@Override
			public void handle(String strCmd, String strArg, String[] arrArgs)
			{
				CLIDriver.showServerConsole();
			}
		});

		CLICommand objExitCmd = new CLICommand("exit", "Exits the application")
		{
			@Override
			public void handle(String strCmd, String[] arrArgs)
			{
				CLIDriver.stopServer();
				Prompt.println("Goodbye :)");
				System.exit(0);
			}
		};

		CLICommand objUserCmd = new CLICommand("user", "User-related stuff");
		objUserCmd.addArgument(new CLIArgument("add", "Add a user")
		{
			@Override
			public void handle(String strCmd, String strArg, String[] arrArgs)
			{
				if(arrArgs.length != 1)
					throw new InvalidUsageException("Unsupported number of arguments (expected 'username')");

				String strUsername = arrArgs[0];

				if(strUsername.length() < 3)
					throw new InvalidUsageException("Expected a username >= 3 characters long");

				// TODO: check if specified username exists

				final PForm objForm = new PForm("New User Data");

				final PPasswordField objPassField = new PPasswordField("Password: ")
				{
					@Override
					public void validateInput() throws InvalidInputException
					{
						String strText = this.getResponse();

						if(strText == null)
							throw new InvalidInputException("Expected a password");
						else if(strText.length() < 3)
							throw new InvalidUsageException("Expected a password >= 3 characters long");
					}
				};

				final PPasswordField objPassConfirmField = new PPasswordField("Password (confirm): ")
				{
					@Override
					public void validateInput() throws InvalidInputException
					{
						String strText = this.getResponse();

						if(strText == null)
							throw new InvalidInputException("Expected a password");
						else if(!strText.equals(objPassField.getResponse()))
							throw new InvalidUsageException("Passwords don't match");
					}
				};

				final PCheckbox objIsAdmin = new PCheckbox("Is Admin (y/[n])? ");

				objForm.add(objPassField);
				objForm.add(objPassConfirmField);
				objForm.add(objIsAdmin);

				objForm.show();

				System.out.println("Username: " + strUsername);
				System.out.println("Password: " + objPassField.getResponse());
				System.out.println("Password (confirm): " + objPassConfirmField.getResponse());
				System.out.println("Is Admin? " + objIsAdmin.getResponse());
			}
		});

		objCLI.addCommand(objServerCmd);
		objCLI.addCommand(objUserCmd);
		objCLI.addCommand(objExitCmd);
		objCLI.start();
	}

	private static void showServerConsole()
	{
		Prompt.println("TODO: implement server live console (shouldn't be too hard!!!)");
		/*while(true)
		{
			// show live server console here!
		}*/
	}

	private static void showServerStatus()
	{
		boolean bRunning = (CLIDriver.server != null && CLIDriver.server.isRunning());
		Prompt.println("Server is %s", (bRunning ? "running" : "STOPPED!"));

		if(bRunning)
		{
			Prompt.println("TODO: show server stats (running on port, connected clients, messages sent, etc)");
		}
	}

	private static void startServer()
	{
		if(CLIDriver.server != null && CLIDriver.server.isRunning())
		{
			Prompt.println("Server already running!");
			return;
		}

		try
		{
			Server.initialize();
			CLIDriver.server = new Server(CLIDriver.serverVars);
			CLIDriver.server.setup();
			CLIDriver.server.start();
			Prompt.println("Server started!");
		}
		catch(IOException e)
		{
			Prompt.println("Unable to start server (already running?)");
		}
		catch(SQLException e)
		{
			Prompt.println("Unable to initialize database (something/someone already connected?");
		}
		catch(ClassLoadException e)
		{
			Prompt.println("Unable to locate Apache Derby drivers");
		}
	}

	private static void stopServer()
	{
		if(CLIDriver.server == null || !CLIDriver.server.isRunning())
		{
			Prompt.println("Server already stopped!");
			return;
		}

		CLIDriver.server.stop();
		Prompt.println("Server stopped!");
	}
}