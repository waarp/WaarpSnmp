/**
 * This file is part of Waarp Project.
 * 
 * Copyright 2009, Frederic Bregier, and individual contributors by the @author
 * tags. See the COPYRIGHT.txt in the distribution for a full listing of
 * individual contributors.
 * 
 * All Waarp Project is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Waarp is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Waarp. If not, see <http://www.gnu.org/licenses/>.
 */
package org.waarp.snmp.test;


import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOScope;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.waarp.common.logging.WaarpInternalLogger;
import org.waarp.common.logging.WaarpInternalLoggerFactory;
import org.waarp.snmp.r66.WaarpPrivateMib;
import org.waarp.snmp.utils.WaarpMOScalar;

/**
 * Example of Implementation of WaarpPrivateMib
 * 
 * @author Frederic Bregier
 * 
 */
public class WaarpImplPrivateMib extends WaarpPrivateMib {
    /**
     * Internal Logger
     */
    private static WaarpInternalLogger logger = WaarpInternalLoggerFactory
            .getLogger(WaarpImplPrivateMib.class);

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
    public WaarpImplPrivateMib(String sysdesc, int port, int smiPrivateCodeFinal,
            int typeGoldenGateObject, String scontactName, String stextualName,
            String saddress, int iservice) {
        super(sysdesc, port, smiPrivateCodeFinal, typeGoldenGateObject,
                scontactName, stextualName, saddress, iservice);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.waarp.snmp.GgInterfaceMib#updateServices()
     */
    @Override
    public void updateServices(WaarpMOScalar scalar) {
        // 3 groups to check
        OID oid = scalar.getOid();
        if (oid.startsWith(rootOIDGoldenGateGlobal)) {
            // UpTime
            if (oid.equals(rootOIDGoldenGateGlobalUptime)) {
                return;
            }
            ((WaarpPrivateMonitor) agent.monitor).generalValuesUpdate();
        } else if (oid.startsWith(rootOIDGoldenGateDetailed)) {
            ((WaarpPrivateMonitor) agent.monitor).detailedValuesUpdate();
        } else if (oid.startsWith(rootOIDGoldenGateError)) {
            ((WaarpPrivateMonitor) agent.monitor).errorValuesUpdate();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.waarp.snmp.GgInterfaceMib#updateServices(org.snmp4j.agent.MOScope)
     */
    @Override
    public void updateServices(MOScope range) {
        // UpTime first
        OID low = range.getLowerBound();

        boolean okGeneral = true;
        boolean okDetailed = true;
        boolean okError = true;
        if (low != null) {
            logger.debug("low: {}:{} " + rootOIDGoldenGateGlobal + ":" +
                    rootOIDGoldenGateDetailed + ":" + rootOIDGoldenGateError,
                    low, range.isLowerIncluded());
            if (low.size() <= rootOIDGoldenGate.size() &&
                    low.startsWith(rootOIDGoldenGate)) {
                // test for global requests
                okGeneral = okDetailed = okError = true;
            } else {
                // Test for sub requests
                okGeneral &= low.startsWith(rootOIDGoldenGateGlobal);
                okDetailed &= low.startsWith(rootOIDGoldenGateDetailed);
                okError &= low.startsWith(rootOIDGoldenGateError);
            }
        }
        logger.debug("General:" + okGeneral + " Detailed:" + okDetailed +
                " Error:" + okError);
        if (okGeneral) {
            ((WaarpPrivateMonitor) agent.monitor).generalValuesUpdate();
        }
        if (okDetailed) {
            ((WaarpPrivateMonitor) agent.monitor).detailedValuesUpdate();
        }
        if (okError) {
            ((WaarpPrivateMonitor) agent.monitor).errorValuesUpdate();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.waarp.snmp.GgPrivateMib#agentRegisterGoldenGateMib()
     */
    @Override
    protected void agentRegisterGoldenGateMib()
            throws DuplicateRegistrationException {
        defaultAgentRegisterGoldenGateMib();
    }

    /**
     * Example of trap for All
     * 
     * @param message
     * @param message2
     * @param number
     */
    public void notifyInfo(String message, String message2, int number) {
        if (!TrapLevel.All.isLevelValid(agent.trapLevel)) return;
        logger.warn("Notify: " + NotificationElements.InfoTask + ":" + message +
                ":" + number);
        agent.getNotificationOriginator().notify(
                new OctetString("public"),
                NotificationElements.InfoTask.getOID(rootOIDGoldenGateNotif),
                new VariableBinding[] {
                        new VariableBinding(NotificationElements.InfoTask
                                .getOID(rootOIDGoldenGateNotif,
                                        NotificationTasks.stepStatusInfo
                                                .getOID()), new OctetString(
                                message)),
                        new VariableBinding(
                                NotificationElements.InfoTask
                                        .getOID(rootOIDGoldenGateNotif,
                                                NotificationTasks.filenameInfo
                                                        .getOID()),
                                new OctetString(message2)),
                        new VariableBinding(NotificationElements.InfoTask
                                .getOID(rootOIDGoldenGateNotif,
                                        NotificationTasks.specialIdInfo
                                                .getOID()), new Counter64(
                                number)),
                        new VariableBinding(NotificationElements.InfoTask
                                .getOID(rootOIDGoldenGateNotif,
                                        NotificationTasks.idRuleInfo.getOID()),
                                new OctetString(NotificationElements.InfoTask
                                        .name())) });
    }

    /**
     * Example of trap for Error
     * 
     * @param message
     * @param message2
     * @param number
     */
    public void notifyError(String message, String message2, int number) {
        if (!TrapLevel.Alert.isLevelValid(agent.trapLevel)) return;
        logger.warn("Notify: " + NotificationElements.TrapError + ":" +
                message + ":" + number);
        agent.getNotificationOriginator().notify(
                new OctetString("public"),
                NotificationElements.TrapError.getOID(rootOIDGoldenGateNotif),
                new VariableBinding[] {
                        new VariableBinding(NotificationElements.TrapError
                                .getOID(rootOIDGoldenGateNotif, 1),
                                new OctetString(message)),
                        new VariableBinding(NotificationElements.TrapError
                                .getOID(rootOIDGoldenGateNotif, 1),
                                new OctetString(message2)),
                        new VariableBinding(NotificationElements.TrapError
                                .getOID(rootOIDGoldenGateNotif, 1),
                                new Counter64(number)),
                        new VariableBinding(NotificationElements.TrapError
                                .getOID(rootOIDGoldenGateNotif, 1),
                                new OctetString(NotificationElements.TrapError
                                        .name())) });
    }
}
