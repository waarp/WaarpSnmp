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

import org.snmp4j.smi.Gauge32;

/**
 * Specific Value for Gauge32 for Memory usage
 * 
 * @author Frederic Bregier
 *
 */
@SuppressWarnings("serial")
public class MemoryGauge32 extends Gauge32 {
    public static enum MemoryType {
        TotalMemory,
        FreeMemory,
        UsedMemory
    }
    // Runtime for Memory
    protected Runtime runtime = Runtime.getRuntime();
    protected MemoryType type;
    
    private void setInternalValue() {
        long mem;
        switch (type) {
            case TotalMemory:
                mem = runtime.totalMemory();
                setValue(mem>>10);
                return;
            case FreeMemory:
                mem = runtime.freeMemory();
                setValue(mem>>10);
                return;
            case UsedMemory:
                mem = runtime.totalMemory() - runtime.freeMemory();
                setValue(mem>>10);
                return;
        }
    }
    /**
     * 
     */
    public MemoryGauge32(MemoryType type) {
        this.type = type;
        setInternalValue();
    }

    /* (non-Javadoc)
     * @see org.snmp4j.smi.UnsignedInteger32#getValue()
     */
    @Override
    public long getValue() {
        setInternalValue();
        return super.getValue();
    }

    /* (non-Javadoc)
     * @see org.snmp4j.smi.Gauge32#clone()
     */
    @Override
    public Object clone() {
        setInternalValue();
        return super.clone();
    }

    /* (non-Javadoc)
     * @see org.snmp4j.smi.AbstractVariable#isDynamic()
     */
    @Override
    public boolean isDynamic() {
        return true;
    }

}
