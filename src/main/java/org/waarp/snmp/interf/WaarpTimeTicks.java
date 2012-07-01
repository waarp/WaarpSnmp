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
package org.waarp.snmp.interf;

import org.snmp4j.smi.TimeTicks;

/**
 * Generic TimeTicks with update possibility for GoldenGate
 * 
 * @author Frederic Bregier
 * 
 */
@SuppressWarnings("serial")
public abstract class WaarpTimeTicks extends TimeTicks {
    /**
     * Function to set the data before it is accessed by SNMP4J. This function
     * MUST call setValue(long)
     */
    protected abstract void setInternalValue();

    /**
     * Function to set the data before it is accessed by SNMP4J. This function
     * MUST call setValue(long)
     */
    protected abstract void setInternalValue(long value);

    public WaarpTimeTicks() {
        setInternalValue();
    }

    public WaarpTimeTicks(long value) {
        setInternalValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.snmp4j.smi.UnsignedInteger32#getValue()
     */
    @Override
    public long getValue() {
        setInternalValue();
        return super.getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.snmp4j.smi.Gauge32#clone()
     */
    @Override
    public Object clone() {
        setInternalValue();
        return super.clone();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.snmp4j.smi.AbstractVariable#isDynamic()
     */
    @Override
    public boolean isDynamic() {
        return true;
    }

}
