/* **************************************************************************
 * $Novell: ConnectionPool.java,v 1.8 2003/01/14 02:56:12 $
 *
 * Copyright (C) 2002 - 2003 Novell, Inc. All Rights Reserved.
 *
 * THIS WORK IS SUBJECT TO U.S. AND INTERNATIONAL COPYRIGHT LAWS AND
 * TREATIES. USE, MODIFICATION, AND REDISTRIBUTION OF THIS WORK IS SUBJECT
 * TO VERSION 2.0.1 OF THE OPENLDAP PUBLIC LICENSE, A COPY OF WHICH IS
 * AVAILABLE AT HTTP://WWW.OPENLDAP.ORG/LICENSE.HTML OR IN THE FILE "LICENSE"
 * IN THE TOP-LEVEL DIRECTORY OF THE DISTRIBUTION. ANY USE OR EXPLOITATION
 * OF THIS WORK OTHER THAN AS AUTHORIZED IN VERSION 2.0.1 OF THE OPENLDAP
 * PUBLIC LICENSE, OR OTHER PRIOR WRITTEN CONSENT FROM NOVELL, COULD SUBJECT
 * THE PERPETRATOR TO CRIMINAL AND CIVIL LIABILITY.
 ******************************************************************************/
package com.novell.services.dsml;
import com.novell.ldap.LDAPSocketFactory;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPJSSESecureSocketFactory;
import java.security.Security;
//import java.util.LinkedList;
import java.lang.IndexOutOfBoundsException;

/**
 * Connection Pool
 *
 * <p></p>
 */
public class ConnectionPool 
{
    private String host = "localhost";
    private int port = 389;

    /** Contains all of the sharedConns that are in use */
    private CPListOfSharedConns inUseCPListOfSharedConns;
    /** Contains all of the available sharedConns */
    private CPListOfSharedConns availableCPListOfSharedConns;
    /** Set by finalize. This tells any waiting thread to shutdown.*/
    private boolean shuttingDown;
    
    /**
     * Initialize connection pool.
     *
     * <p>Initialize the connection pool by setting the host, port, max
     * number of connection and max number of shared connections.</p>
     *
     * @param host - Host name
     * @param port - Port number
     * @param maxConns - Max number of connection allowed to this host.
     * @param maxSharedConns - Max number of shared connections per DN
     * @param keystore - Used for keystore in LDAPConnection
     */
    public ConnectionPool(String host,
                          int port,
                          int maxConns,
                          int maxSharedConns,
                          String keyStore)    
        throws LDAPException
    {        
        LDAPSocketFactory fac = null;
        this.host = host;
        this.port = port;        
        
        // Use the keystore file if it is there.
        if(null != keyStore)
        {
            // Dynamically set JSSE as a security provider
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

            // Dynamically set the property that JSSE uses to identify
            // the keystore that holds trusted root certificates
            System.setProperty("javax.net.ssl.trustStore", keyStore);

            fac = new LDAPJSSESecureSocketFactory();
        }

        inUseCPListOfSharedConns = new CPListOfSharedConns();
        availableCPListOfSharedConns = new CPListOfSharedConns();
        // Set up the max connections and max shared connections
        // ( original + clones) in availableConnection.
        for (int i = 0; i < maxConns; i++)
        {
            CPSharedConns sharedConns = new CPSharedConns(maxSharedConns);
            // Create connection. Initialy anonymous
            CPConn conn = new CPConn(fac);
            // At this point all of the connections anonymous
            conn.connect(host, port);
            sharedConns.add(conn);
            // Clone the connections to make all of the sharedConns.
            for (int j = 1; j < maxSharedConns; j++)
            {
                CPConn cloneConn = (CPConn)conn.clone();
                sharedConns.add(cloneConn);
            }
            availableCPListOfSharedConns.add(i, sharedConns);
        }
        shuttingDown = false;
    }
    
    /**
     * getBoundConnection - Get a bound connection.
     * <p>This returns a bound (bind) connection for the desired DN and
     * password.</p>
     * @param DN  Authentication DN used for bind and key.
     * @param PW  Authentication password used for bind and key.     
     * @throws LDAPException if an LDAPConnection could not be bound.
     */
    public LDAPConnection getBoundConnection(String DN, byte[] PW)
            throws LDAPException, InterruptedException
    {
        
        CPConn  conn        = null;
        CPSharedConns sharedConns     = null;
        boolean           needToBind  = false;

        synchronized (inUseCPListOfSharedConns)
        {
            // See if there is a connection available in the in use list of
            // sharedConns, that are in use bound to DN,PW.
            conn = inUseCPListOfSharedConns.getAvailableConnection(DN, PW);
            if(null != conn)
            {
                // Set this connection inuse.
                conn.setInUse();
                return conn;
            }
        }
        
        synchronized (availableCPListOfSharedConns)
        {
            // See if there are shared connections that are available
            // bound to DN,PW.
            sharedConns = availableCPListOfSharedConns.getSharedConns(DN, PW);
            if(null == sharedConns) // No we need to rebind an available
            {
                // If there are no available sharedConns wait for one.
                if(0 == availableCPListOfSharedConns.size())
                {
                    // Wait for available Instances
                    availableCPListOfSharedConns.wait();
                    // If we are shutting down return null
                    if(shuttingDown) return null;
                }
                // Get connection from first available sharedConns
                sharedConns = (CPSharedConns)availableCPListOfSharedConns.get(0);
                sharedConns.setDN(DN);
                sharedConns.setPW(PW);
                needToBind = true;
            }
            
            // Remove sharedConns from available.
            availableCPListOfSharedConns.remove(sharedConns);
            // Get the first connection and mark it inuse
            conn = (CPConn)sharedConns.get(0);
            // Set this connection inuse.
            conn.setInUse();
        }
        // Do we need to rebind? Bind will do a connect if needed
        if(needToBind) conn.bind(LDAPConnection.LDAP_V3, DN, PW);

        synchronized (inUseCPListOfSharedConns)
        {
            // Move into inuse.
            inUseCPListOfSharedConns.add(sharedConns);
        }
        return conn;
    }

    /**
     * makeConnectionAvailable - Make this connection available.
     * <p></p>
     * @param baseConn  LDAPConnection to be made available.
     */
    public void makeConnectionAvailable(LDAPConnection baseConn)
    {
        CPSharedConns sharedConns = null;
        
        synchronized(inUseCPListOfSharedConns)
        {
            // Mark this connection available.        
            ((CPConn)baseConn).clearInUse();
            
            sharedConns = inUseCPListOfSharedConns.getSharedConns((CPConn)baseConn);
                
            // If all connections in this instance are available move to
            // from in use to available.
            if(sharedConns.allConnectionsAvailable())
            {
                inUseCPListOfSharedConns.remove(sharedConns);
            }
        }
        
        if(null != sharedConns)
        {
            synchronized(availableCPListOfSharedConns)
            {
                availableCPListOfSharedConns.add(sharedConns);
                // Notify anyone that might be waiting on this connection.
                availableCPListOfSharedConns.notify();
            }
        }
        return;
    }
    
    /**
     * finalize - free connections.
     * <p> Tell all waiting threads that we are shutting down.
     * Clean up the in use and available connections.</p>
     *
     * @throws Throwable when disconnect fails.
     */
    protected void finalize()
            throws Throwable

    {
        synchronized (availableCPListOfSharedConns)
        {
            // Notify all waiting threads.
            availableCPListOfSharedConns.notifyAll();
        }
    }
}
