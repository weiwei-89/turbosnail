package org.edward.turbosnail;

import org.edward.turbosnail.xml.ProtocolXmlHandler;
import org.edward.turbosnail.xml.model.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

public class ProtocolLoader {
    private static final Logger logger = LoggerFactory.getLogger(ProtocolLoader.class);

    public static synchronized Map<String, Papers> load(Path[] pathArray) throws Exception {
        if(pathArray==null || pathArray.length==0) {
            throw new Exception("path is not specified");
        }
        Map<String, Papers> papersMap = new HashMap<>(pathArray.length);
        for(int p=0; p<pathArray.length; p++) {
            logger.info("start reading protocol[{}]......", pathArray[p].getProtocolId());
            try {
                papersMap.put(pathArray[p].getProtocolId(), load(pathArray[p].getPath()));
            } catch(Exception e) {
                logger.error("failed", e);
            }
        }
        return papersMap;
    }

    private static Papers load(String path) throws Exception {
        File papersFile = new File(path);
        if(!papersFile.isDirectory()) {
            throw new Exception("\""+path+"\" is not a directory");
        }
        File[] protocolFileArray = papersFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        });
        if(protocolFileArray==null || protocolFileArray.length==0) {
            throw new Exception("there is no any xml files in the directory \""+path+"\"");
        }
        Papers papers = new Papers();
        for(int p=0; p<protocolFileArray.length; p++) {
            ProtocolXmlHandler protocolXmlHandler = new ProtocolXmlHandler();
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            saxParser.parse(protocolFileArray[p], protocolXmlHandler);
            Protocol protocol = protocolXmlHandler.getProtocol();
            papers.put(protocol.getId(), protocol);
        }
        return papers;
    }
}