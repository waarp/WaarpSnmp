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

import org.snmp4j.smi.OID;

import goldengate.snmp.interf.GgGauge32;

/**
 * Example of GgGauge32 Usage
 * 
 * @author Frederic Bregier
 * 
 */
@SuppressWarnings("serial")
public class ExampleImplGauge32 extends GgGauge32 {
    public OID oid;

    long _internalValue = 42;

    protected void setInternalValue() {
        _internalValue ++;
        setValue(_internalValue);
    }

    protected void setInternalValue(long value) {
        _internalValue = value;
        setValue(_internalValue);
    }

    /**
     * 
     */
    public ExampleImplGauge32(OID oid) {
        super();
        this.oid = oid;
    }

    /**
     * 
     */
    public ExampleImplGauge32(OID oid, long value) {
        super(value);
        this.oid = oid;
    }
}
