package org.edward.turbosnail.xml.model;

import java.util.List;

public class Decode extends Element {
    private Type type;
    private List<Option> optionList;
    private String protocolId;
    private String foreignProtocolId;

    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public List<Option> getOptionList() {
        return optionList;
    }
    public void setOptionList(List<Option> optionList) {
        this.optionList = optionList;
    }
    public String getProtocolId() {
        return protocolId;
    }
    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }
    public String getForeignProtocolId() {
        return foreignProtocolId;
    }
    public void setForeignProtocolId(String foreignProtocolId) {
        this.foreignProtocolId = foreignProtocolId;
    }

    public enum Type {
        EQUATION,
        HEX,
        ASCII,
        NUMBER,
        NUMBER_BE,
        NUMBER_LE,
        OPTION,
        PROTOCOL;

        public static Type get(String type) {
            for(Type t : Type.values()) {
                if(type.equalsIgnoreCase(t.name())) {
                    return t;
                }
            }
            return null;
        }
    }
}