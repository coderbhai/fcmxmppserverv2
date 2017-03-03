package com.wedevol.xmpp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import com.wedevol.xmpp.bean.CcsOutMessage;
import com.wedevol.xmpp.server.CcsClient;
import com.wedevol.xmpp.server.MessageHelper;
import com.wedevol.xmpp.util.Util;

/**
 * Entry Point class for the XMPP Server in dev mode for debugging and testing
 * purposes
 */
public class EntryPoint {
	public static final Logger logger = Logger.getLogger(EntryPoint.class.getName());

	public static void main(String[] args) throws SmackException, IOException {
	    if (args.length < 2) {
	        System.out.println("Usage:\n" + 
	           "java -jar target/xmpp-server-jar-with-dependencies.jar " +
	           "FIREBASE_SENDER_ID FIREBASE_SERVER_KEY OPTIONAL_SEND_TO_REGISTRATION_ID");
           return;
	    }

		final String fcmProjectSenderId = args[0];
		final String fcmServerKey = args[1];
		String toRegId = null;
		if (args.length == 3) {
		    toRegId = args[2];
		}
		
		logger.log(Level.INFO, "Running with FCB sender ID {0}", fcmProjectSenderId);
		logger.log(Level.INFO, "Running with FCB server key {0}", fcmServerKey);

		CcsClient ccsClient = CcsClient.prepareClient(fcmProjectSenderId, fcmServerKey, true);

		try {
			ccsClient.connect();
		} catch (XMPPException e) {
			e.printStackTrace();
		}

        if (toRegId != null) {
            // Send a sample downstream message to a device
    		String messageId = Util.getUniqueMessageId();
    		Map<String, String> dataPayload = new HashMap<String, String>();
    		dataPayload.put(Util.PAYLOAD_ATTRIBUTE_MESSAGE, "This is the simple sample message");
    		CcsOutMessage message = new CcsOutMessage(toRegId, messageId, dataPayload);
    		String jsonRequest = MessageHelper.createJsonOutMessage(message);
    		ccsClient.send(jsonRequest);    
        }
		
		while (true) {
			// TODO: Improve this because the app closes itself after the
			// execution of the connect method
		}
	}
}
