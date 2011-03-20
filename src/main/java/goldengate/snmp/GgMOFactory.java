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
package goldengate.snmp;

import goldengate.snmp.interf.GgInterfaceVariableFactory;
import goldengate.snmp.utils.GgDefaultVariableFactory;
import goldengate.snmp.utils.GgMORow;
import goldengate.snmp.utils.GgMOScalar;

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

/**
 * This class creates and returns ManagedObjects
 * 
 * @author Frederic Bregier
 * 
 */
public class GgMOFactory {
    public static GgInterfaceVariableFactory factory = null;

    private static GgInterfaceVariableFactory defaultFactory = new GgDefaultVariableFactory();

    /**
     * 
     * @param oid
     * @param value
     * @param type
     * @return an MOScalar according to the argument
     */
    public static GgMOScalar createReadOnly(OID oid, Object value, int type,
            GgMORow row, int mibLevel, int entry) {
        return new GgMOScalar(oid, MOAccessImpl.ACCESS_READ_ONLY, getVariable(
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
    public static GgMOScalar create(OID oid, Object value, int type,
            MOAccess access, GgMORow row, int mibLevel, int entry) {
        return new GgMOScalar(oid, access, getVariable(oid, value, type, mibLevel, entry), row);
    }

    public static Variable getVariable(OID oid, Object value, int type, int mibLevel, int entry) {
        Variable var = null;
        GgInterfaceVariableFactory vf;
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
