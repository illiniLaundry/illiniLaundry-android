package io.ericlee.illinilaundry.Model;

import java.io.Serializable;

public class Alarm extends Object implements Serializable{
    private Machine machine;
    private Dorm dorm;
    private int hashcode;

    public Alarm(Machine machine, Dorm dorm) {
        this.dorm = dorm;
        this.machine = machine;

        // Every alarm needs a unique ID to set it apart from other alarms. We're using a hashcode
        // here because it does the job perfectly: it is unique to others, while the same if it's
        // the same alarm. The latter of the benefits is so that we won't have two identical alarms.
        this.hashcode = (dorm.getName() + machine.getMachineNumber()).hashCode();
    }

    public String getAlarmName() { return dorm.getName() + ": " + machine.getMachineNumber(); }
    public Machine getMachine() { return machine; }
    public Dorm getDorm() { return dorm; }
    public int getHashcode() { return hashcode; }
}
