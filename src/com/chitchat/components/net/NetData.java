/**
* @author Kyle Ratti (PC18)
* @version 1.0, 12/16/13
*/

package com.chitchat.components.net;

import java.io.Serializable;

import java.util.ArrayList;

public class NetData implements Serializable
{
	private static final long serialVersionUID = 7526471155622776147L;

	private final ArrayList<Object> data;
	private int index;
	
	/** Creates an empty <code>NetData</code> object */
	public NetData()
	{
		this.data = new ArrayList<Object>();
		this.index = 0;
	}

	/**
	* Adds the specified <code>Object</code> to this <code>NetData</code>
	*/
	public void add(Object obj)
	{
		this.data.add(obj);
	}

	/**
	* Gets all of the data
	*
	* @return All of the data
	*/
	public ArrayList<Object> getAll()
	{
		return this.data;
	}

	/**
	* Gets the number of objects this object holds
	*
	* @return The size of this object
	*/
	public int size()
	{
		return this.data.size();
	}

	/**
	* Gets the next <code>Boolean</code>
	*
	* @return The next <code>Boolean</code>
	*/
	public Boolean getBoolean()
	{
		Boolean obj = null;

		try
		{
			obj = (Boolean) this.data.get(this.index);
		}
		catch(ClassCastException e)
		{
			e.printStackTrace();
			System.err.println("Error getting type (is it the wrong data type?)");
		}
		finally
		{
			this.index++;
		}

		return obj;
	}

	/**
	* Gets the next <code>Character</code>
	*
	* @return The next <code>Character</code>
	*/
	public Character getChar()
	{
		Character obj = null;

		try
		{
			obj = (Character) this.data.get(this.index);
		}
		catch(ClassCastException e)
		{
			e.printStackTrace();
			System.err.println("Error getting type (is it the wrong data type?)");
		}
		finally
		{
			this.index++;
		}

		return obj;
	}
	
	/**
	* Gets the next <code>Double</code>
	*
	* @return The next <code>Double</code>
	*/
	public Double getDouble()
	{
		Double obj = null;

		try
		{
			obj = (Double) this.data.get(this.index);
		}
		catch(ClassCastException e)
		{
			e.printStackTrace();
			System.err.println("Error getting type (is it the wrong data type?)");
		}
		finally
		{
			this.index++;
		}

		return obj;
	}
	
	/**
	* Gets the next <code>Float</code>
	*
	* @return The next <code>Float</code>
	*/
	public Float getFloat()
	{
		Float obj = null;

		try
		{
			obj = (Float) this.data.get(this.index);
		}
		catch(ClassCastException e)
		{
			e.printStackTrace();
			System.err.println("Error getting type (is it the wrong data type?)");
		}
		finally
		{
			this.index++;
		}

		return obj;
	}
	
	/**
	* Gets the next <code>Integer</code>
	*
	* @return The next <code>Integer</code>
	*/
	public Integer getInt()
	{
		Integer obj = null;

		try
		{
			obj = (Integer) this.data.get(this.index);
		}
		catch(ClassCastException e)
		{
			e.printStackTrace();
			System.err.println("Error getting type (is it the wrong data type?)");
		}
		finally
		{
			this.index++;
		}

		return obj;
	}
	
	/**
	* Gets the next <code>Long</code>
	*
	* @return The next <code>Long</code>
	*/
	public Long getLong()
	{
		Long obj = null;

		try
		{
			obj = (Long) this.data.get(this.index);
		}
		catch(ClassCastException e)
		{
			e.printStackTrace();
			System.err.println("Error getting type (is it the wrong data type?)");
		}
		finally
		{
			this.index++;
		}

		return obj;
	}
	
	/**
	* Gets the next <code>Short</code>
	*
	* @return The next <code>Short</code>
	*/
	public Short getShort()
	{
		Short obj = null;

		try
		{
			obj = (Short) this.data.get(this.index);
		}
		catch(ClassCastException e)
		{
			e.printStackTrace();
			System.err.println("Error getting type (is it the wrong data type?)");
		}
		finally
		{
			this.index++;
		}

		return obj;
	}
	
	/**
	* Gets the next <code>String</code>
	*
	* @return The next <code>String</code>
	*/
	public String getString()
	{
		String obj = null;

		try
		{
			obj = (String) this.data.get(this.index);
		}
		catch(ClassCastException e)
		{
			e.printStackTrace();
			System.err.println("Error getting type (is it the wrong data type?)");
		}
		finally
		{
			this.index++;
		}

		return obj;
	}

	/**
	* Gets the next <code>Object</code>
	*
	* @return The next <code>Object</code>
	*/
	public Object getObject()
	{
		int iOld = this.index;
		this.index++;

		return this.data.get(iOld);
	}
}