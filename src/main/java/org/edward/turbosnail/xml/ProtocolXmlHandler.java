package org.edward.turbosnail.xml;

import org.edward.turbosnail.xml.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class ProtocolXmlHandler extends DefaultHandler {
    private static final Logger logger = LoggerFactory.getLogger(ProtocolXmlHandler.class);
    private static final int DEFAULT_SIZE = 10;

    private Protocol protocol;

    private Segment currentSegment;

    private Position currentPosition;

    private Decode currentDecode;

    private List<Option> currentOptionList;

    private Option currentOption;

    public Protocol getProtocol() {
        return this.protocol;
    }

    @Override
    public void startDocument() throws SAXException {
        logger.info("reading xml......");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if("protocol".equalsIgnoreCase(qName)) {
            this.protocol = new Protocol();
            this.protocol.setId(attributes.getValue("id"));
            this.protocol.setName(attributes.getValue("name"));
            this.protocol.setDescription(attributes.getValue("description"));
        } else if("segment".equalsIgnoreCase(qName)) {
            if(this.protocol == null) {
                throw new SAXException("invalid [segment] element: there's not a [protocol] element as parent element");
            }
            this.currentSegment = new Segment();
            this.currentSegment.setId(attributes.getValue("id"));
            this.currentSegment.setName(attributes.getValue("name"));
            this.currentSegment.setDescription(attributes.getValue("description"));
            this.currentSegment.setValue(attributes.getValue("value"));
            this.currentSegment.setSkip(Boolean.parseBoolean(attributes.getValue("skip")));
        } else if("position".equalsIgnoreCase(qName)) {
            if(this.currentSegment == null) {
                throw new SAXException("invalid [position] element: there's not a [segment] element as parent element");
            }
            this.currentPosition = new Position();
            this.currentPosition.setId(attributes.getValue("id"));
            this.currentPosition.setName(attributes.getValue("name"));
            this.currentPosition.setDescription(attributes.getValue("description"));
            this.currentPosition.setLength(Integer.parseInt(attributes.getValue("length")));
            this.currentPosition.setVariableLength(Boolean.parseBoolean(attributes.getValue("variableLength")));
            this.currentPosition.setLengthFormula(attributes.getValue("lengthFormula"));
        } else if("decode".equalsIgnoreCase(qName)) {
            if(this.currentSegment == null) {
                throw new SAXException("invalid [decode] element: there's not a [segment] element as parent element");
            }
            this.currentDecode = new Decode();
            this.currentDecode.setId(attributes.getValue("id"));
            this.currentDecode.setName(attributes.getValue("name"));
            this.currentDecode.setDescription(attributes.getValue("description"));
            this.currentDecode.setType(Decode.Type.get(attributes.getValue("type")));
            this.currentDecode.setProtocolId(attributes.getValue("protocolId"));
            this.currentDecode.setForeignProtocolId(attributes.getValue("foreignProtocolId"));
        } else if("options".equalsIgnoreCase(qName)) {
            if(this.currentDecode == null) {
                throw new SAXException("invalid [options] element: there's not a [decode] element as parent element");
            }
            this.currentOptionList = new ArrayList<>(DEFAULT_SIZE);
        } else if("option".equalsIgnoreCase(qName)) {
            if(this.currentOptionList == null) {
                throw new SAXException("invalid [option] element: there's not a [options] element as parent element");
            }
            this.currentOption = new Option();
            this.currentOption.setId(attributes.getValue("id"));
            this.currentOption.setName(attributes.getValue("name"));
            this.currentOption.setDescription(attributes.getValue("description"));
            this.currentOption.setValue(attributes.getValue("value"));
            boolean range = Boolean.parseBoolean(attributes.getValue("range"));
            this.currentOption.setRange(range);
            if(range) {
                this.currentOption.setMin(Integer.parseInt(attributes.getValue("min")));
                this.currentOption.setMax(Integer.parseInt(attributes.getValue("max")));
            }
            this.currentOption.setUnit(attributes.getValue("unit"));
        } else {
            logger.warn("unknown element[{}]", qName);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if("segment".equalsIgnoreCase(qName)) {
            if(this.protocol.getSegmentList()==null || this.protocol.getSegmentList().size()==0) {
                this.protocol.setSegmentList(new ArrayList<>(DEFAULT_SIZE));
            }
            this.protocol.getSegmentList().add(this.currentSegment);
        } else if("position".equalsIgnoreCase(qName)) {
            this.currentSegment.setPosition(this.currentPosition);
        } else if("decode".equalsIgnoreCase(qName)) {
            this.currentSegment.setDecode(this.currentDecode);
        } else if("options".equalsIgnoreCase(qName)) {
            this.currentDecode.setOptionList(this.currentOptionList);
        } else if("option".equalsIgnoreCase(qName)) {
            this.currentOptionList.add(this.currentOption);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        logger.info("reading done");
    }
}