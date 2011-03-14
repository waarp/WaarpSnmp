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

import org.snmp4j.agent.MOAccess;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.Null;
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
    /**
     * 
     * @param oid
     * @param value
     * @return an MOScalar according to the argument
     */
    public static GgMOScalar createReadOnlyString(OID oid, Object value, GgMORow row) {
        return new GgMOScalar(oid, MOAccessImpl.ACCESS_READ_ONLY,
                getVariable(value), row);
    }
    /**
     * 
     * @param oid
     * @param value
     * @param type
     * @return an MOScalar according to the argument
     */
    public static GgMOScalar createReadOnly(OID oid, Object value, int type, GgMORow row) {
        return new GgMOScalar(oid, MOAccessImpl.ACCESS_READ_ONLY,
                getVariable(value, type), row);
    }
    /**
     * 
     * @param oid
     * @param value
     * @param access
     * @return an MOScalar according to the argument
     */
    public static GgMOScalar createString(OID oid, Object value, MOAccess access, GgMORow row) {
        return new GgMOScalar(oid, access,
                getVariable(value), row);
    }
    /**
     * 
     * @param oid
     * @param value
     * @param type
     * @param access
     * @return an MOScalar according to the argument
     */
    public static GgMOScalar create(OID oid, Object value, int type, MOAccess access, GgMORow row) {
        return new GgMOScalar(oid, access,
                getVariable(value, type), row);
    }
    /**
     * 
     * @param value
     * @return a Variable containing the String
     */
    private static Variable getVariable(Object value) {
        if (value instanceof String) {
            return new OctetString((String) value);
        }
        throw new IllegalArgumentException("Unmanaged Type: " +
                value.getClass());
    }
    
    public static Variable getVariable(Object value, int type) {
        Variable var = null;
        switch (type) {
            case SMIConstants.SYNTAX_INTEGER:
            //case SMIConstants.SYNTAX_INTEGER32:
                if (value == null) {
                    var = new Integer32(1);
                } else {
                    var = new Integer32((Integer) value);
                }
                break;
            case SMIConstants.SYNTAX_OCTET_STRING:
            //case SMIConstants.SYNTAX_BITS:
                if (value == null) {
                    var = new OctetString("a");
                } else {
                    var = new OctetString(value.toString());
                }
                break;
            case SMIConstants.SYNTAX_NULL:
                if (value == null) {
                    var = new Null();
                } else {
                    var = new Null((Integer) value);
                }
                break;
            case SMIConstants.SYNTAX_OBJECT_IDENTIFIER:
                if (value == null) {
                    var = new OID(".1.3.6.1.4.1.66666.2");
                } else {
                    var = new OID(value.toString());
                }
                break;
            case SMIConstants.SYNTAX_IPADDRESS:
                if (value == null) {
                    var = new IpAddress();
                } else {
                    var = new IpAddress(value.toString());
                }
                break;
            case SMIConstants.SYNTAX_COUNTER32:
                if (value == null) {
                    var = new Counter32(1);
                } else {
                    var = new Counter32((Long) value);
                }
                break;
            case SMIConstants.SYNTAX_GAUGE32:
            //case SMIConstants.SYNTAX_UNSIGNED_INTEGER32:
                if (value == null) {
                    var = new Gauge32(1);
                } else {
                    var = new Gauge32((Long) value);
                }
                break;
            case SMIConstants.SYNTAX_TIMETICKS:
                if (value == null) {
                    var = new TimeTicks(10000);
                } else {
                    if (value instanceof TimeTicks) {
                        var = new TimeTicks((TimeTicks) value);
                    } else {
                        var = new TimeTicks((Long) value);
                    }
                }
                break;
            case SMIConstants.SYNTAX_OPAQUE:
                if (value == null) {
                    byte [] test = {'t', 'e', 's', 't'};
                    var = new Opaque(test);
                } else {
                    var = new Opaque((byte [])value);
                }
                break;
            case SMIConstants.SYNTAX_COUNTER64:
                if (value == null) {
                    var = new Counter64(1);
                } else {
                    var = new Counter64((Long) value);
                }
                break;
            default:
                throw new IllegalArgumentException("Unmanaged Type: " +
                        value.getClass());
        }
        return var;
    }
}
