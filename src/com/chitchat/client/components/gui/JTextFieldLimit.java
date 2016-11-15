/**
* @author Kyle Ratti (PC18)
* @version 1.0, 01/14/14
*/

package com.chitchat.client.components.gui;

import javax.swing.*;
import javax.swing.text.*;

public class JTextFieldLimit extends PlainDocument
{
	private final int limit;

	/**
	* Creates a new <code>JTextFieldLimit</code> with the specified character limit
	*
	* @param iLimit The maximum amount of characters allowed
	*/
	public JTextFieldLimit(int iLimit)
	{
		super();
		this.limit = iLimit;
	}

	/**
	* Called when text should be added to the text box (ie when typed)
	*
	* @param iOffset The offset
	* @param str The <code>String</code> to insert
	* @param objAttr The <code>AttributeSet</code>
	*/
	@Override
	public void insertString(int iOffset, String str, AttributeSet objAttr) throws BadLocationException
	{
		if(str == null) return;

		if(this.getLength() + str.length() <= this.limit)
			super.insertString(iOffset, str, objAttr);
	}
}