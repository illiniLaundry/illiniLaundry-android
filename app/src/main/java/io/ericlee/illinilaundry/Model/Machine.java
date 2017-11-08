package io.ericlee.illinilaundry.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * Machine object for GSON.
 *
 * @author dl-eric
 */
public class Machine implements Serializable {
    private String label;
    private String description;
    private String status;
    private String timeRemaining;
    private boolean isOutOfService;

    @Override
    public boolean equals(Object obj) {
        return label.equals(((Machine) obj).getLabel())
                && description.equals(((Machine) obj).getDescription())
                && status.equals(((Machine) obj).getStatus())
                && timeRemaining.equals(((Machine) obj).getTimeRemaining());
    }

    public boolean isOutOfService() { return isOutOfService; }

    public void setIsOutOfService(boolean status) { this.isOutOfService = status; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(String timeRemaining) {
        this.timeRemaining = timeRemaining;
    }
}
