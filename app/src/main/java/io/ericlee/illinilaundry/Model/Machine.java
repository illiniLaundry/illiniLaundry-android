package io.ericlee.illinilaundry.Model;

import java.io.Serializable;

/**
 * Created by Eric on 6/9/2016.
 */
public class Machine implements Serializable {
    private String machineNumber;
    private String machineType;
    private String machineStatus;
    private String machineTimeRemaining;
    private Dorm dorm;

    public Machine(String machineNumber, String machineType, String machineStatus,
                   String machineTimeRemaining, Dorm dorm) {
        this.machineNumber = machineNumber;
        this.machineType = machineType;
        this.machineStatus = machineStatus;
        this.machineTimeRemaining = machineTimeRemaining;
        this.dorm = dorm;
    }

    public String getMachineNumber() { return machineNumber; }
    public String getMachineType() { return machineType; }
    public String getMachineStatus() { return machineStatus; }
    public String getMachineTimeRemaining() { return machineTimeRemaining; }
    public Dorm getDorm() { return dorm; }
}
