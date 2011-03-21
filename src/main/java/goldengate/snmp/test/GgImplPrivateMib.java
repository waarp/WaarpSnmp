/**
   This file is part of GoldenGate Project (named also GoldenGate or GG).

   Copyright 2009, Frederic Bregier, and individual contributors by the @author
   tags. See the COPYRIGHT.txt in the distribution for a full listing of
   individual contributors.

   All GoldenGate Project is free software: you can redistribute it and/or 
   modify it under the terms of the GNU General Public License as published 
   by the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   GoldenGate is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with GoldenGate .  If not, see <http://www.gnu.org/licenses/>.
 */
package goldengate.snmp.test;

import goldengate.common.logging.GgInternalLogger;
import goldengate.common.logging.GgInternalLoggerFactory;
import goldengate.snmp.r66.GgPrivateMib;
import goldengate.snmp.utils.GgMOScalar;

import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOScope;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;


/**
 * Example of Implementation of GgPrivateMib
 * 
 * @author Frederic Bregier
 *
 */
public class GgImplPrivateMib extends GgPrivateMib {
    /**
     * Internal Logger
     */
    private static GgInternalLogger logger = GgInternalLoggerFactory
            .getLogger(GgImplPrivateMib.class);

    /**
     * @param sysdesc
     * @param port
     * @param smiPrivateCodeFinal
     * @param typeGoldenGateObject
     * @param scontactName
     * @param stextualName
     * @param saddress
     * @param iservice
     */
    public GgImplPrivateMib(String sysdesc, int port, int smiPrivateCodeFinal,
            int typeGoldenGateObject, String scontactName, String stextualName,
            String saddress, int iservice) {
        super(sysdesc, port, smiPrivateCodeFinal, typeGoldenGateObject, scontactName,
                stextualName, saddress, iservice);
    }
    /* (non-Javadoc)
     * @see goldengate.snmp.GgInterfaceMib#updateServices()
     */
    @Override
    public void updateServices(GgMOScalar scalar) {
        // 3 groups to check
        OID oid = scalar.getOid();
        if (oid.startsWith(rootOIDGoldenGateGlobal)) {
            // UpTime
            if (oid.equals(rootOIDGoldenGateGlobalUptime)) {
                return;
            }
            ((GgPrivateMonitor) agent.monitor).generalValuesUpdate();
        } else if (oid.startsWith(rootOIDGoldenGateDetailed)) {
            ((GgPrivateMonitor) agent.monitor).detailedValuesUpdate();
        } else if (oid.startsWith(rootOIDGoldenGateError)) {
            ((GgPrivateMonitor) agent.monitor).errorValuesUpdate();
        }
    }
    /* (non-Javadoc)
     * @see goldengate.snmp.GgInterfaceMib#updateServices(org.snmp4j.agent.MOScope)
     */
    @Override
    public void updateServices(MOScope range) {
        // UpTime first
        OID low = range.getLowerBound();
        
        boolean okGeneral = true;
        boolean okDetailed = true;
        boolean okError = true;
        if (low != null) {
            logger.debug("low: {}:{} "+rootOIDGoldenGateGlobal+":"+
                    rootOIDGoldenGateDetailed+":"+rootOIDGoldenGateError,
                    low,range.isLowerIncluded());
            if (low.size() <= rootOIDGoldenGate.size() && low.startsWith(rootOIDGoldenGate)) {
                // test for global requests
                okGeneral = okDetailed = okError = true;
            } else {
                // Test for sub requests
                okGeneral &= low.startsWith(rootOIDGoldenGateGlobal);
                okDetailed &= low.startsWith(rootOIDGoldenGateDetailed);
                okError &= low.startsWith(rootOIDGoldenGateError);
            }
        }
        logger.debug("General:"+okGeneral+" Detailed:"+okDetailed+" Error:"+okError);
        if (okGeneral) {
            ((GgPrivateMonitor) agent.monitor).generalValuesUpdate();
        }
        if (okDetailed) {
            ((GgPrivateMonitor) agent.monitor).detailedValuesUpdate();
        }
        if (okError) {
            ((GgPrivateMonitor) agent.monitor).errorValuesUpdate();
        }
    }
    /* (non-Javadoc)
     * @see goldengate.snmp.GgPrivateMib#agentRegisterGoldenGateMib()
     */
    @Override
    protected void agentRegisterGoldenGateMib()
            throws DuplicateRegistrationException {
        defaultAgentRegisterGoldenGateMib();
    }
    
    /**
     * Example of trap for All
     * @param element
     * @param message
     * @param message2
     * @param number
     */
    public void notifyInfo(String message, String message2, int number) {
        if (!TrapLevel.All.isLevelValid(agent.trapLevel))
            return;
        logger.warn("Notify: "+NotificationElements.InfoTask+":"+message+":"+number);
        agent.getNotificationOriginator().notify(
                new OctetString("public"), 
                NotificationElements.InfoTask.getOID(rootOIDGoldenGateNotif),
                new VariableBinding[] {
                    new VariableBinding(
                            NotificationElements.InfoTask.getOID(rootOIDGoldenGateNotif,
                            NotificationTasks.stepStatusInfo.getOID()),
                            new OctetString(message)),
                    new VariableBinding(
                            NotificationElements.InfoTask.getOID(rootOIDGoldenGateNotif,
                            NotificationTasks.filenameInfo.getOID()), 
                            new OctetString(message2)),
                    new VariableBinding(
                            NotificationElements.InfoTask.getOID(rootOIDGoldenGateNotif,
                            NotificationTasks.specialIdInfo.getOID()), 
                            new Counter64(number)),
                    new VariableBinding(
                            NotificationElements.InfoTask.getOID(rootOIDGoldenGateNotif,
                            NotificationTasks.idRuleInfo.getOID()),
                            new OctetString(NotificationElements.InfoTask.name()))
            });
    }    
    /**
     * Example of trap for Error
     * @param element
     * @param message
     * @param message2
     * @param number
     */
    public void notifyError(String message, String message2, int number) {
        if (!TrapLevel.Alert.isLevelValid(agent.trapLevel))
            return;
        logger.warn("Notify: "+NotificationElements.TrapError+":"+message+":"+number);
        agent.getNotificationOriginator().notify(
                new OctetString("public"), 
                NotificationElements.TrapError.getOID(rootOIDGoldenGateNotif),
                new VariableBinding[] {
                    new VariableBinding(
                            NotificationElements.TrapError.getOID(rootOIDGoldenGateNotif,
                            1),
                            new OctetString(message)),
                    new VariableBinding(
                            NotificationElements.TrapError.getOID(rootOIDGoldenGateNotif,
                            1), 
                            new OctetString(message2)),
                    new VariableBinding(
                            NotificationElements.TrapError.getOID(rootOIDGoldenGateNotif,
                            1), 
                            new Counter64(number)),
                    new VariableBinding(
                            NotificationElements.TrapError.getOID(rootOIDGoldenGateNotif,
                            1),
                            new OctetString(NotificationElements.TrapError.name()))
            });
    }    
}
