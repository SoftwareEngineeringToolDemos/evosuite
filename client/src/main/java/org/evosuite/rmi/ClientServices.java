/**
 * Copyright (C) 2010-2015 Gordon Fraser, Andrea Arcuri and EvoSuite
 * contributors
 *
 * This file is part of EvoSuite.
 *
 * EvoSuite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser Public License as published by the
 * Free Software Foundation, either version 3.0 of the License, or (at your
 * option) any later version.
 *
 * EvoSuite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License along
 * with EvoSuite. If not, see <http://www.gnu.org/licenses/>.
 */
package org.evosuite.rmi;

import java.rmi.NoSuchObjectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.evosuite.Properties;
import org.evosuite.rmi.service.ClientNodeImpl;
import org.evosuite.rmi.service.ClientNodeLocal;
import org.evosuite.rmi.service.ClientNodeRemote;
import org.evosuite.rmi.service.DummyClientNodeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class should be used only in the Client processes, not the master.
 * Used to initialize and store all the RMI services in the clients
 * 
 * @author arcuri
 *
 */
public class ClientServices {

	private static Logger logger = LoggerFactory.getLogger(ClientServices.class);
	
	private static ClientServices instance = new ClientServices();
	
	private volatile ClientNodeImpl clientNode = new DummyClientNodeImpl();
	
	protected ClientServices(){		
	}
	
	public static ClientServices getInstance(){
		return instance;
	}

	public boolean registerServices() {

		UtilsRMI.ensureRegistryOnLoopbackAddress();
		
		try{
			int port = Properties.PROCESS_COMMUNICATION_PORT;
			Registry registry = LocateRegistry.getRegistry(port);
			clientNode = new ClientNodeImpl(registry);
			ClientNodeRemote stub = (ClientNodeRemote) UtilsRMI.exportObject(clientNode);
			registry.rebind(clientNode.getClientRmiIdentifier(), stub);
			return clientNode.init();
		} catch(Exception e){
			logger.error("Failed to register client services",e);
			return false;
		}
	}

	public ClientNodeLocal getClientNode() {
		return clientNode;
	}
	
	public void stopServices(){
		if(clientNode!=null){
			clientNode.stop();
			int i = 0;
			final int tries = 10;
			boolean done = false;
			try {
				while(!done){
					/*
					 * A call from Master could still be active on this node. so we cannot
					 * forcely stop the client, we need to wait
					 */
					done = UnicastRemoteObject.unexportObject(clientNode, false);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {	
						break;
					}
					i++;
					if(i>=tries){
						logger.error("Tried "+tries+" times to stop RMI ClientNode, giving up");
						break;
					}
				}
			} catch (NoSuchObjectException e) {
				//this could happen if Master has removed the registry
				logger.debug("Failed to delete ClientNode RMI instance",e);
			}
			clientNode = new DummyClientNodeImpl();
		}
	}
}
