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
package org.waarp.snmp.utils;


import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.MOServer;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.waarp.snmp.WaarpMOFactory;
import org.waarp.snmp.interf.WaarpInterfaceMib;

/**
 * MORow implementation for GoldenGate
 * 
 * @author Frederic Bregier
 * 
 */
public class WaarpMORow implements MOGroup {
    /**
     * Row access
     */
    public WaarpMOScalar[] row;
    /**
     * Type access
     */
    public int[] type;
    /**
     * Base OID
     */
    public OID reference;
    /**
     * MIB from which this Row is issued
     */
    public WaarpInterfaceMib mib;
    /**
     * Mib Level entry identification
     */
    public int mibLevel;
    /**
     * 
     * @param mib
     * @param reference
     * @param entries
     * @param mibLevel this integer identifies this Row in the MIB
     */
    public WaarpMORow(WaarpInterfaceMib mib, OID reference, WaarpEntry[] entries,
            int mibLevel) {
        this.mib = mib;
        this.reference = reference;
        this.mibLevel = mibLevel;
        row = new WaarpMOScalar[entries.length];
        type = new int[entries.length];
        int[] ref = this.reference.getValue();
        int[] add = new int[2];
        add[1] = 0;
        for (int i = 0; i < entries.length; i ++) {
            WaarpEntry entry = entries[i];
            type[i] = entry.smiConstantsType;
            add[0] = i + 1;
            OID oid = new OID(ref, add);
            // the value is null at the creation, meaning values have to be
            // setup once just after
            row[i] = WaarpMOFactory.create(oid, null, entry.smiConstantsType,
                    entry.access, this, mibLevel, i);
        }
    }

    /**
     * Set a Value in this Row
     * @param index
     * @param value
     * @throws IllegalArgumentException
     */
    public void setValue(int index, Object value)
            throws IllegalArgumentException {
        if (index >= row.length)
            throw new IllegalArgumentException("Index exceed Row size");
        Variable var = row[index].getValue();
        WaarpMOFactory.setVariable(var, value, type[index]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.snmp4j.agent.MOGroup#registerMOs(org.snmp4j.agent.MOServer,
     * org.snmp4j.smi.OctetString)
     */
    @Override
    public void registerMOs(MOServer server, OctetString context)
            throws DuplicateRegistrationException {
        for (int i = 0; i < row.length; i ++) {
            WaarpMOScalar scalar = row[i];
            server.register(scalar, context);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.snmp4j.agent.MOGroup#unregisterMOs(org.snmp4j.agent.MOServer,
     * org.snmp4j.smi.OctetString)
     */
    @Override
    public void unregisterMOs(MOServer server, OctetString context) {
        for (int i = 0; i < row.length; i ++) {
            WaarpMOScalar scalar = row[i];
            server.unregister(scalar, context);
        }
    }
}
