package io.ericlee.illinilaundry.Model;

import java.io.Serializable;

public class Alarm extends Object implements Serializable{
    private Machine machine;
    private int hashcode;

    public Alarm(Machine machine) {
        this.machine = machine;

        // Every alarm needs a unique ID to set it apart from other alarms. We're using a hashcode
        // here because it does the job perfectly: it is unique to others, while the same if it's
        // the same alarm. The latter of the benefits is so that we won't have two identical alarms.
        this.hashcode = (machine.getDorm().getName() + machine.getMachineNumber()).hashCode();
    }

    public String getAlarmName() { return machine.getDorm().getName() + ": " + machine.getMachineNumber(); }
    public Machine getMachine() { return machine; }
    public Dorm getDorm() { return machine.getDorm(); }
    public int getHashcode() { return hashcode; }
}
