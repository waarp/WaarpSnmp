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
package goldengate.snmp;

import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.MOServer;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

/**
 * MORow implementation for GoldenGate
 * 
 * @author Frederic Bregier
 *
 */
public class GgMORow implements MOGroup {
    public GgMOScalar [] row;
    public int []type;
    public OID reference;
    public GgInterfaceMib mib;
    
    public GgMORow(GgInterfaceMib mib, OID reference, GgEntry []entries) {
        this.mib = mib;
        this.reference = reference;
        row = new GgMOScalar[entries.length];
        type = new int[entries.length];
        int []ref = this.reference.getValue();
        int []add = new int[2];
        add[1] = 0;
        for (int i = 0; i < entries.length; i++) {
            GgEntry entry = entries[i];
            type[i] = entry.smiConstantsType;
            add[0] = i+1;
            OID oid = new OID(ref, add);
            // the value is null at the creation, meaning values have to be setup once just after
            row[i] = GgMOFactory.create(oid, null, 
                    entry.smiConstantsType, entry.access, this);
        }
    }
    /**
     * 
     * @param index
     * @param value
     * @throws IllegalArgumentException
     */
    public void setValue(int index, Object value) throws IllegalArgumentException {
        if (index >= row.length)
            throw new IllegalArgumentException("Index exceed Row size");
        
        Variable var = GgMOFactory.getVariable(value, type[index]);
        row[index].setValue(var);
    }

    /* (non-Javadoc)
     * @see org.snmp4j.agent.MOGroup#registerMOs(org.snmp4j.agent.MOServer, org.snmp4j.smi.OctetString)
     */
    @Override
    public void registerMOs(MOServer server, OctetString context)
            throws DuplicateRegistrationException {
        for (int i = 0; i < row.length; i++) {
            GgMOScalar scalar = row[i];
            server.register(scalar, context);
        }
    }
    /* (non-Javadoc)
     * @see org.snmp4j.agent.MOGroup#unregisterMOs(org.snmp4j.agent.MOServer, org.snmp4j.smi.OctetString)
     */
    @Override
    public void unregisterMOs(MOServer server, OctetString context) {
        for (int i = 0; i < row.length; i++) {
            GgMOScalar scalar = row[i];
            server.unregister(scalar, context);
        }
    }
}
