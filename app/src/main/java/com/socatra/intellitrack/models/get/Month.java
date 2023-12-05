package com.socatra.intellitrack.models.get;

public class Month {
    private String monthName;
    private String monthId;

    public Month(String monthName, String monthId) {

        this.monthName = monthName;
        this.monthId = monthId;
    }

    public String getMonthName() {
        return monthName;
    }

    public String getMonthId() {
        return monthId;
    }

    @Override
    public String toString() {
        return monthName; // Display month name in the Spinner
    }
}

