/**
 * This file is part of GoldenGate Project (named also GoldenGate or GG).
 * 
 * Copyright 2009, Frederic Bregier, and individual contributors by the @author
 * tags. See the COPYRIGHT.txt in the distribution for a full listing of
 * individual contributors.
 * 
 * All GoldenGate Project is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * GoldenGate is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * GoldenGate . If not, see <http://www.gnu.org/licenses/>.
 */
package goldengate.snmp.test;

import goldengate.common.logging.GgInternalLogger;
import goldengate.common.logging.GgInternalLoggerFactory;
import goldengate.snmp.GgSnmpAgent;
import goldengate.snmp.interf.GgInterfaceMonitor;

/**
 * This implementation show how to support SNMP.
 * 
 * @author Frederic Bregier
 * 
 */
public class GgPrivateMonitor implements GgInterfaceMonitor {
    /**
     * Internal Logger
     */
    private static GgInternalLogger logger = GgInternalLoggerFactory
            .getLogger(GgPrivateMonitor.class);

    public GgSnmpAgent agent;
    

    /**
     * @return the agent
     */
    public GgSnmpAgent getAgent() {
        return agent;
    }

    /**
     * @param agent the agent to set
     */
    public void setAgent(GgSnmpAgent agent) {
        this.agent = agent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see goldengate.snmp.GgInterfaceMonitor#initialize()
     */
    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        logger.warn("Call");
    }

    /*
     * (non-Javadoc)
     * 
     * @see goldengate.snmp.GgInterfaceMonitor#releaseResources()
     */
    @Override
    public void releaseResources() {
        // TODO Auto-generated method stub
        logger.warn("Call");

    }

    /*
     * function to test if the computations need to be redone
     * 
             // FIXME should test the OID
        if (lastcheck > System.currentTimeMillis()+1000) {
            // at least 1 second
            return;
        }
        lastcheck = System.currentTimeMillis();
        test--;
        if (test >=0){
            // no update
            return;
        }
        Variable var;
        lastChange = 1000;
        lastInBand = lastChange-500;
        lastOutBand = lastChange-100;

     */
    public void generalValuesUpdate() {
        synchronized (agent) {
            // TODO Auto-generated method stub

            logger.warn("Call");
        }
    }

    public void detailedValuesUpdate() {
        synchronized (agent) {
            // TODO Auto-generated method stub
            logger.warn("Call");

        }
    }

    public void errorValuesUpdate() {
        synchronized (agent) {
            // TODO Auto-generated method stub
            logger.warn("Call");

        }
    }

}
