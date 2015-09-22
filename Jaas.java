/*
 * "@(#)Jaas.java	1.1	05/06/15 SMI"
 *
 * Copyright 2006 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or 
 * without modification, are permitted provided that the following 
 * conditions are met:
 * 
 * -Redistributions of source code must retain the above copyright  
 * notice, this  list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduct the above copyright 
 * notice, this list of conditions and the following disclaimer in 
 * the documentation and/or other materials provided with the 
 * distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of 
 * contributors may be used to endorse or promote products derived 
 * from this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any 
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND 
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY 
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY 
 * DAMAGES OR LIABILITIES  SUFFERED BY LICENSEE AS A RESULT OF  OR 
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR 
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE 
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, 
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER 
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF 
 * THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that Software is not designed, licensed or 
 * intended for use in the design, construction, operation or 
 * maintenance of any nuclear facility. 
 */

import javax.security.auth.Subject;
import javax.security.auth.login.*;
import javax.security.auth.callback.CallbackHandler;
import java.security.*;
import com.sun.security.auth.callback.TextCallbackHandler;
import java.io.File;

import java.io.*;
import java.util.*;
//import javax.security.auth.login.*;
import javax.security.auth.*;
import javax.security.auth.callback.*;

public class Jaas {
    private static String name;

    public static void main(String[] args) throws Exception {
	if (args.length > 0) {
	    name = args[0];
	} else {
	    name = "client";
	}

	loginAndAction(name);
    }

    static void loginAndAction(String name)
	throws LoginException {

	// Create a callback handler
	CallbackHandler callbackHandler = new MyCallbackHandler();

	LoginContext context = null;

	try {
	    // Create a LoginContext with a callback handler
	    context = new LoginContext(name, callbackHandler);

	    // Perform authentication
	    context.login();
	} catch (LoginException e) {
	    System.err.println("Login failed");
	    e.printStackTrace();
	    System.exit(-1);
	}

	// Perform action as authenticated user
	Subject subject = context.getSubject();
	// System.out.println(subject.toString());
        System.out.println("Authenticated principal: " + subject.getPrincipals());

	context.logout();
    }
}

class MyCallbackHandler implements CallbackHandler {

    /**
     * Invoke an array of Callbacks.
     *
     * <p>
     *
     * @param callbacks an array of <code>Callback</code> objects which contain
     *                  the information requested by an underlying security
     *                  service to be retrieved or displayed.
     *
     * @exception UnsupportedCallbackException if the implementation of this
     *                  method does not support one or more of the Callbacks
     *                  specified in the <code>callbacks</code> parameter.
     */
    public void handle(Callback[] callbacks)
    throws UnsupportedCallbackException {

        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof PasswordCallback) {

                // Or prompt the user for sensitive information
                PasswordCallback pc = (PasswordCallback)callbacks[i];
                pc.setPassword("xxx".toCharArray());

            } else {
                throw new UnsupportedCallbackException
                        (callbacks[i], "Unrecognized Callback");
            }
        }
    }
}

