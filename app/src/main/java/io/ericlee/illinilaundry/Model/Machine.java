package io.ericlee.illinilaundry.Model;

/**
 * Created by Eric on 6/9/2016.
 */
public class Machine {
    private String machineNumber;
    private String machineType;
    private String machineStatus;
    private String machineTimeRemaining;

    public Machine(String machineNumber, String machineType, String machineStatus, String machineTimeRemaining) {
        this.machineNumber = machineNumber;
        this.machineType = machineType;
        this.machineStatus = machineStatus;
        this.machineTimeRemaining = machineTimeRemaining;
    }

    public String getMachineNumber() { return machineNumber; }
    public String getMachineType() { return machineType; }
    public String getMachineStatus() { return machineStatus; }
    public String getMachineTimeRemaining() { return machineTimeRemaining; }
}
