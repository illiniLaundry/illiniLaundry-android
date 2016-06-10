package io.ericlee.illinilaundry.Model;

/**
 * Created by Eric on 6/9/2016.
 */
public class Machine {
    private String machineNumber;
    private String machineType;
    private String machineStatus;

    public Machine(String machineNumber, String machineType, String machineStatus) {
        this.machineNumber = machineNumber;
        this.machineType = machineType;
        this.machineStatus = machineStatus;
    }

    public String getMachineNumber() { return machineNumber; }
    public String getMachineType() { return machineType; }
    public String getMachineStatus() { return machineStatus; }
}
