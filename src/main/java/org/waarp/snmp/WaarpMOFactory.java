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
package org.waarp.snmp;


import org.snmp4j.agent.MOAccess;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Opaque;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.Variable;
import org.waarp.snmp.interf.WaarpInterfaceVariableFactory;
import org.waarp.snmp.utils.WaarpDefaultVariableFactory;
import org.waarp.snmp.utils.WaarpMORow;
import org.waarp.snmp.utils.WaarpMOScalar;

/**
 * This class creates and returns ManagedObjects
 * 
 * @author Frederic Bregier
 * 
 */
public class WaarpMOFactory {
    /**
     * To be setup to default Factory to be used or kept as null for default one
     */
    public static WaarpInterfaceVariableFactory factory = null;

    /**
     * Default one
     */
    private static WaarpInterfaceVariableFactory defaultFactory = new WaarpDefaultVariableFactory();

    /**
     * 
     * @param oid
     * @param value
     * @param type
     * @return an MOScalar according to the argument
     */
    public static WaarpMOScalar createReadOnly(OID oid, Object value, int type,
            WaarpMORow row, int mibLevel, int entry) {
        return new WaarpMOScalar(oid, MOAccessImpl.ACCESS_READ_ONLY, getVariable(
                oid, value, type, mibLevel, entry), row);
    }

    /**
     * 
     * @param oid
     * @param value
     * @param type
     * @param access
     * @return an MOScalar according to the argument
     */
    public static WaarpMOScalar create(OID oid, Object value, int type,
            MOAccess access, WaarpMORow row, int mibLevel, int entry) {
        return new WaarpMOScalar(oid, access, getVariable(oid, value, type,
                mibLevel, entry), row);
    }

    /**
     * Create a Variable using the arguments
     * 
     * @param oid
     * @param value
     * @param type
     * @param mibLevel
     * @param entry
     * @return a Variable using the arguments
     */
    public static Variable getVariable(OID oid, Object value, int type,
            int mibLevel, int entry) {
        Variable var = null;
        WaarpInterfaceVariableFactory vf;
        if (factory == null) {
            vf = defaultFactory;
        } else {
            vf = factory;
        }
        var = vf.getVariable(oid, type, mibLevel, entry);
        if (value != null) {
            switch (type) {
                case SMIConstants.SYNTAX_INTEGER:
                    // case SMIConstants.SYNTAX_INTEGER32:
                    ((Integer32) var).setValue((Integer) value);
                    break;
                case SMIConstants.SYNTAX_OCTET_STRING:
                    // case SMIConstants.SYNTAX_BITS:
                    ((OctetString) var).setValue(value.toString());
                    break;
                case SMIConstants.SYNTAX_NULL:
                    break;
                case SMIConstants.SYNTAX_OBJECT_IDENTIFIER:
                    ((OID) var).setValue(value.toString());
                    break;
                case SMIConstants.SYNTAX_IPADDRESS:
                    ((IpAddress) var).setValue(value.toString());
                    break;
                case SMIConstants.SYNTAX_COUNTER32:
                    ((Counter32) var).setValue((Long) value);
                    break;
                case SMIConstants.SYNTAX_GAUGE32:
                    // case SMIConstants.SYNTAX_UNSIGNED_INTEGER32:
                    ((Gauge32) var).setValue((Long) value);
                    break;
                case SMIConstants.SYNTAX_TIMETICKS:
                    if (value instanceof TimeTicks) {
                        ((TimeTicks) var).setValue(((TimeTicks) value)
                                .toString());
                    } else {
                        ((TimeTicks) var).setValue((Long) value);
                    }
                    break;
                case SMIConstants.SYNTAX_OPAQUE:
                    ((Opaque) var).setValue((byte[]) value);
                    break;
                case SMIConstants.SYNTAX_COUNTER64:
                    ((Counter64) var).setValue((Long) value);
                    break;
                default:
                    throw new IllegalArgumentException("Unmanaged Type: " +
                            value.getClass());
            }
        }
        return var;
    }

    /**
     * Set a Variable value
     * 
     * @param var
     * @param value
     * @param type
     */
    public static void setVariable(Variable var, Object value, int type) {
        if (value != null) {
            switch (type) {
                case SMIConstants.SYNTAX_INTEGER:
                    // case SMIConstants.SYNTAX_INTEGER32:
                    ((Integer32) var).setValue((Integer) value);
                    break;
                case SMIConstants.SYNTAX_OCTET_STRING:
                    // case SMIConstants.SYNTAX_BITS:
                    ((OctetString) var).setValue(value.toString());
                    break;
                case SMIConstants.SYNTAX_NULL:
                    break;
                case SMIConstants.SYNTAX_OBJECT_IDENTIFIER:
                    ((OID) var).setValue(value.toString());
                    break;
                case SMIConstants.SYNTAX_IPADDRESS:
                    ((IpAddress) var).setValue(value.toString());
                    break;
                case SMIConstants.SYNTAX_COUNTER32:
                    ((Counter32) var).setValue((Long) value);
                    break;
                case SMIConstants.SYNTAX_GAUGE32:
                    // case SMIConstants.SYNTAX_UNSIGNED_INTEGER32:
                    ((Gauge32) var).setValue((Long) value);
                    break;
                case SMIConstants.SYNTAX_TIMETICKS:
                    if (value instanceof TimeTicks) {
                        ((TimeTicks) var).setValue(((TimeTicks) value)
                                .toString());
                    } else {
                        ((TimeTicks) var).setValue((Long) value);
                    }
                    break;
                case SMIConstants.SYNTAX_OPAQUE:
                    ((Opaque) var).setValue((byte[]) value);
                    break;
                case SMIConstants.SYNTAX_COUNTER64:
                    ((Counter64) var).setValue((Long) value);
                    break;
                default:
                    throw new IllegalArgumentException("Unmanaged Type: " +
                            value.getClass());
            }
        }
    }
}
