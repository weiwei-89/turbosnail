package org.edward.turbosnail.xml.model;

public class Position extends Element {
    private int length = -1;
    private boolean variableLength;
    private String lengthFormula;

    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
    }
    public boolean isVariableLength() {
        return variableLength;
    }
    public void setVariableLength(boolean variableLength) {
        this.variableLength = variableLength;
    }
    public String getLengthFormula() {
        return lengthFormula;
    }
    public void setLengthFormula(String lengthFormula) {
        this.lengthFormula = lengthFormula;
    }
}