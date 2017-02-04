package io.ericlee.illinilaundry.Model;

import java.util.Date;

/**
 * Machine object for GSON.
 *
 * @author dl-eric
 */
public class Machine {
    private String label;
    private String description;
    private String status;
    private String timeRemaining;

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
