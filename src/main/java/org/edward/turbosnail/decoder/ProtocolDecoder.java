package org.edward.turbosnail.decoder;

import org.edward.turbosnail.Papers;
import org.edward.turbosnail.decoder.model.Data;
import org.edward.turbosnail.decoder.model.Info;
import org.edward.turbosnail.util.DataUtil;
import org.edward.turbosnail.xml.model.Decode;
import org.edward.turbosnail.xml.model.Position;
import org.edward.turbosnail.xml.model.Protocol;
import org.edward.turbosnail.xml.model.Segment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProtocolDecoder {
    private static final Logger logger = LoggerFactory.getLogger(ProtocolDecoder.class);

    private final Papers papers;
    private final String protocolId;

    public ProtocolDecoder(Papers papers, String protocolId) {
        this.papers = papers;
        this.protocolId = protocolId;
    }

    public Info decode(byte[] bytes) throws Exception {
        logger.info("start decoding......");
        Info info = new Info();
        this.decode(new Data(bytes), this.papers.get(this.protocolId), info);
        logger.info("decode done");
        return info;
    }

    private void decode(Data data, Protocol protocol, Info info) throws Exception {
        List<Segment> segmentList = protocol.getSegmentList();
        if(segmentList==null || segmentList.size()==0) {
            return;
        }
        for(int i=0; i<segmentList.size(); i++) {
            this.decode(data, segmentList.get(i), protocol, info);
        }
    }

    private void decode(Data data, Segment segment, Protocol protocol, Info info) throws Exception {
        Decode.Type decodeType = Decode.Type.HEX;
        Decode decode = segment.getDecode();
        if(decode != null) {
            decodeType = decode.getType();
        }
        if(decodeType == Decode.Type.PROTOCOL) {
            Info infoForProtocol = new Info();
            this.decode(data, this.papers.get(decode.getProtocolId()), infoForProtocol);
            info.put(segment.getId(), infoForProtocol);
            return;
        }
        Position position = segment.getPosition();
        if(position == null) {
            // TODO 在这里再次进行了xml文件格式的校验，考虑考虑统一在xmlHandler中处理是否合适
            throw new Exception("there is no [position] element in segment ["+segment.getId()+"]");
        }
        if(position.getLength() < 0) {
            throw new Exception("the param \"length\" in [position] element is less than 0");
        }
        byte[] partBytes = data.read(position.getLength());
        // TODO 不要使用这么多的if-else，搞个接口或者设计模式重构一下
        if(decodeType == Decode.Type.EQUATION) {
            String hexValue = DataUtil.toHexString(partBytes);
            if(!hexValue.equalsIgnoreCase(segment.getValue())) {
                throw new Exception("the value of the param \"value\" in segment ["+segment.getId()
                        +"] is not equal to \""+segment.getValue()+"\"");
            }
            info.put(segment.getId(), hexValue);
        } else if(decodeType == Decode.Type.HEX) {
            info.put(segment.getId(), DataUtil.toHexString(partBytes));
        } else if(decodeType == Decode.Type.ASCII) {
            info.put(segment.getId(), DataUtil.toAscii(partBytes));
        } else if(decodeType == Decode.Type.NUMBER) {
            info.put(segment.getId(), DataUtil.toNumberForLittleEndian(partBytes));
        } else if(decodeType == Decode.Type.NUMBER_BE) {
            info.put(segment.getId(), DataUtil.toNumberForBigEndian(partBytes));
        } else if(decodeType == Decode.Type.NUMBER_LE) {
            info.put(segment.getId(), DataUtil.toNumberForLittleEndian(partBytes));
        } else if(decodeType == Decode.Type.OPTION) {

        }
    }
}