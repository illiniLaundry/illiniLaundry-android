package io.ericlee.illinilaundry.Model;

import java.io.Serializable;

public class Alarm extends Object implements Serializable{
    private Machine machine;
    private Dorm dorm;
    private int hashcode;

    public Alarm(Machine machine, Dorm dorm) {
        this.dorm = dorm;
        this.machine = machine;
        this.hashcode = (dorm.getName() + machine.getMachineNumber()).hashCode();
    }

    public String getAlarmName() { return dorm.getName() + ": " + machine.getMachineNumber(); }
    public Machine getMachine() { return machine; }
    public Dorm getDorm() { return dorm; }
    public int getHashcode() { return hashcode; }
}
