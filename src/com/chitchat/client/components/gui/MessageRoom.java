package com.chitchat.client.components.gui;

import com.chitchat.Settings;
import com.chitchat.components.net.*;
import com.chitchat.components.user.*;
import com.chitchat.components.ChatMessage;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.util.Date;

import utilx.Prompt;

public class MessageRoom extends ChitChatPanel {
    private static final int WIDTH = 850;
    private static final int HEIGHT = 330;
    private final StringBuilder chatHistory;
    
    private final MainGUI parent;

    /**
     * Creates new form MessageRoom
     */
    public MessageRoom(MainGUI objParent) {
        initComponents();

        this.parent = objParent;
        this.chatHistory = new StringBuilder("<body><font face=\"Courier New\">");

        this.setup();
    }

    /**
    * Gets the desired width of this panel
    *
    * @return The desired width
    */
    public int getWidth()
    {
        return MessageRoom.WIDTH;
    }

    /**
    * Gets the desired height of this panel
    *
    * @return The desired height
    */
    public int getHeight()
    {
        return MessageRoom.HEIGHT;
    }

    /** An event fired when the panel is shown */
    public void onShow()
    {
        this.jTextField.requestFocus();
    }

    private void setup()
    {
        NetMessageHandler objHandler = new NetMessageHandler()
        {
           @Override
            public void onDataReceived(String strAction, Connection objConnection, NetMessage objMsg)
            {
                NetData objData = objMsg.getData();

                try
                {
                    Object obj = objData.getObject();
                    ChatMessage objChatMsg = (ChatMessage) obj;

                    MessageRoom.this.addMessage(objChatMsg);
                }
                catch(ClassCastException e)
                {
                    System.err.println("Error reading user");
                }
            } 
        };

        this.parent.getClient().getNetMessageManager().addPingHandler("Chat.userMessage", objHandler);
        this.parent.getClient().getNetMessageManager().addPingHandler("Chat.systemMessage", objHandler);

        this.parent.getClient().getNetMessageManager().addPingHandler("User.connected", new NetMessageHandler()
        {
            @Override
            public void onDataReceived(String strAction, Connection objConnection, NetMessage objMsg)
            {
                NetData objData = objMsg.getData();

                try
                {
                    User objUser = (User) objData.getObject();

                    MessageRoom.this.onUserConnected(objUser);
                }
                catch(ClassCastException e)
                {
                    System.err.println("Unable to receive user");
                }
            }
        });

        this.parent.getClient().getNetMessageManager().addPingHandler("User.disconnected", new NetMessageHandler()
        {
            @Override
            public void onDataReceived(String strAction, Connection objConnection, NetMessage objMsg)
            {
                NetData objData = objMsg.getData();

                try
                {
                    User objUser = (User) objData.getObject();

                    MessageRoom.this.onUserDisconnected(objUser);
                }
                catch(ClassCastException e)
                {
                    System.err.println("Unable to receive user");
                }
            }
        });

        this.jTextField.requestFocus();
    }

    private void onUserConnected(User objUser)
    {
        StringBuilder objMsg = new StringBuilder();

        objMsg.append("<font size=\"-2\" color=\"green\"><b>");
        objMsg.append(objUser.getUsername());
        objMsg.append("</b> connected</font>");
        objMsg.append("<br/>");

        this.chatHistory.append(objMsg.toString());
    }

    private void onUserDisconnected(User objUser)
    {
        StringBuilder objMsg = new StringBuilder();

        objMsg.append("<font size=\"-2\" color=\"red\"><b>");
        objMsg.append(objUser.getUsername());
        objMsg.append("</b> disconnected");
        objMsg.append("<br/>");

        this.chatHistory.append(objMsg.toString());
    }

    private void addMessage(ChatMessage objChatMsg)
    {
        StringBuilder objMsg = new StringBuilder();

        String strMsg = objChatMsg.getMessage();
        User objUser = objChatMsg.getUser();
        Date objDate = objChatMsg.getDateTime();

        objMsg.append("<font color=\"grey\" size=\"-2\">[");
        objMsg.append(Settings.dateTime.format(objDate));
        objMsg.append("]</font> ");

        objMsg.append("<font color=\"");
        objMsg.append((objUser == null ? "red" : "blue"));
        objMsg.append("\">");

        if(objUser == null)
            objMsg.append("<b>SYSTEM</b>");
        else
        {
            if(objUser.isAdmin())
                objMsg.append("<i>");

            objMsg.append(objUser.getUsername());

            if(objUser.isAdmin())
                objMsg.append("</i>");
        }

        objMsg.append(":</font> ");
        objMsg.append(strMsg); // TODO: fix HTML injection (whoops)

        objMsg.append("<br/>");

        this.chatHistory.append(objMsg.toString());
        this.jMessagePane.setText(this.chatHistory.toString());

        this.jMessagePane.getCaret().setDot(this.jMessagePane.getDocument().getLength());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jMessagePane = new javax.swing.JEditorPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jOnlineList = new javax.swing.JList();
        jTextField = new javax.swing.JTextField();
        jSendButton = new javax.swing.JButton();

        jMessagePane.setEditable(false);
        jMessagePane.setContentType("text/html"); // NOI18N
        jScrollPane1.setViewportView(jMessagePane);

        jOnlineList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Online Users" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jOnlineList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setViewportView(jOnlineList);

        jSendButton.setText("Send");
        jSendButton.setEnabled(false);
        jSendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSendButtonActionPerformed(evt);
            }
        });

        this.jTextField.setDocument(new JTextFieldLimit(Settings.MAX_MSG_LEN));

        this.jTextField.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(MessageRoom.this.jTextField.getText().length() > 0)
                    MessageRoom.this.sendMessage();
            }
        });

        this.jTextField.getDocument().addDocumentListener(new DocumentListener()
            {
                @Override
                public void changedUpdate(DocumentEvent e)
                {
                    System.out.println("Text changed");
                    this.checkButton();
                }

                @Override
                public void removeUpdate(DocumentEvent e)
                {
                    this.checkButton();
                }

                @Override
                public void insertUpdate(DocumentEvent e)
                {
                    this.checkButton();
                }

                private void checkButton()
                {
                    String strText = MessageRoom.this.jTextField.getText().trim();
                    JButton objButton = MessageRoom.this.jSendButton;

                    if(strText.length() > 0)
                        objButton.setEnabled(true);
                    else
                        objButton.setEnabled(false);
                }
            });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTextField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSendButton))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sendMessage()
    {
        this.jTextField.requestFocus();
        String strMsg = this.jTextField.getText().trim();

        if(strMsg.length() <= 0)
        {
            Prompt.showError("Message must be > 0 characters");
            return;
        }

        this.parent.getClient().doSendMessage(strMsg);

        this.jTextField.setText("");
    }

    private void jSendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSendButtonActionPerformed
        this.sendMessage();
    }//GEN-LAST:event_jSendButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane jMessagePane;
    private javax.swing.JList jOnlineList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jSendButton;
    private javax.swing.JTextField jTextField;
    // End of variables declaration//GEN-END:variables
}
