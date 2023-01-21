package org.edward.turbosnail.xml.model;

public class Option extends Element {
    private String value;
    private boolean range;
    private int min;
    private int max;
    private String unit;

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public boolean isRange() {
        return range;
    }
    public void setRange(boolean range) {
        this.range = range;
    }
    public int getMin() {
        return min;
    }
    public void setMin(int min) {
        this.min = min;
    }
    public int getMax() {
        return max;
    }
    public void setMax(int max) {
        this.max = max;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
}