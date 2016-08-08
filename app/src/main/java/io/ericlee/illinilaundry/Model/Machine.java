package io.ericlee.illinilaundry.Model;

/**
 * Created by Eric on 6/9/2016.
 */
public class Machine {
    private String machineNumber;
    private String machineType;
    private String machineStatus;
    private String machineTimeRemaining;
    private String machineDorm;

    public Machine(String machineNumber, String machineType, String machineStatus,
                   String machineTimeRemaining, String machineDorm) {
        this.machineNumber = machineNumber;
        this.machineType = machineType;
        this.machineStatus = machineStatus;
        this.machineTimeRemaining = machineTimeRemaining;
        this.machineDorm = machineDorm;
    }

    public String getMachineNumber() { return machineNumber; }
    public String getMachineType() { return machineType; }
    public String getMachineStatus() { return machineStatus; }
    public String getMachineTimeRemaining() { return machineTimeRemaining; }
    public String getMachineDorm() { return machineDorm; }
}
