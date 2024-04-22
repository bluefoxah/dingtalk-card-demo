package org.bluefoxah.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;

import lombok.extern.slf4j.Slf4j;
import org.bluefoxah.demo.manager.CardManager;
import org.bluefoxah.demo.manager.GroupManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class TestService {

    private final String outTrackIdPrefix = "MyOutTrackId-";
    @Value("${group.owner}")
    private String owner;
    @Value("#{'${group.members}'.split(',')}")
    private List<String> members;
    @Value("${group.robotcode}")
    private String robotCode;
    @Value("${card.templateid}")
    private String templateId;
    @Autowired
    private GroupManager groupManager;
    @Autowired
    private CardManager cardManager;
    private String openConvId = "";
    private String curentOutTrackId = UUID.randomUUID().toString();

    public String createGroup() {
        String groupName = "测试群组";
        String groupOwner = owner;
        List<String> groupMembers = members;

        String openConversationId = groupManager.createGroup(groupName, groupOwner, groupMembers);
        if (StringUtils.isEmpty(openConversationId)) {
            log.warn("create group fail, groupName:{}, groupOwner:{}", groupName, groupOwner);
        }
        openConvId = openConversationId;
        log.info("create group success, groupName:{}, groupOwner:{}, openConvId:{}", groupName, groupOwner, openConvId);
        return openConversationId;
    }

    public String createGroupByTemplate() {
        String groupName = "测试群组";
        String groupOwner = owner;
        List<String> groupMembers = members;

        String openConversationId = groupManager.createGroupByTemplate(groupName, groupOwner, groupMembers);
        if (StringUtils.isEmpty(openConversationId)) {
            log.warn("create group by template fail, groupName:{}, groupOwner:{}", groupName, groupOwner);
        }
        openConvId = openConversationId;
        log.info("create group by template success, groupName:{}, groupOwner:{}, openConvId:{}", groupName, groupOwner,
            openConvId);
        return openConversationId;
    }

    public String createCard() {
        String cardCreator = owner;

        curentOutTrackId = UUID.randomUUID().toString();
        String outTrackId = outTrackIdPrefix + curentOutTrackId;
        Map<String, String> cardData = new HashMap<>();
        JSONObject info = new JSONObject();
        JSONObject basic = new JSONObject();
        basic.put("flight", "MU2278");
        basic.put("from", "宝安机场");
        basic.put("to", "萧山机场");
        basic.put("arrive", "21:25");
        basic.put("time", "01:55");
        basic.put("distance", "1200km");

        info.put("basic", basic);

        JSONArray unit = new JSONArray();
        JSONObject caption = new JSONObject();
        caption.put("name", "张三");
        caption.put("job", "机长");
        caption.put("phone", "18666668888");
        unit.add(caption);
        JSONObject purser = new JSONObject();
        purser.put("name", "李四");
        purser.put("job", "乘务长");
        purser.put("phone", "18666668886");
        unit.add(purser);

        info.put("unit", unit);

        cardData.put("info", info.toString());
        cardData.put("status", "舱门开启");
        log.info("create card success, outTrackId:{}, cardData:{}", outTrackId, JSON.toJSONString(cardData));

        Boolean ret = cardManager.createCard(cardCreator, outTrackId, templateId, cardData);
        if (ret) {
            return outTrackId;
        }
        return "";
    }

    public String deliverCardToGroup() {
        String openSpaceId = getGroupOpenSpaceId();
        String outTrackId = outTrackIdPrefix + curentOutTrackId;

        Boolean ret = cardManager.deliverCardToGroup(outTrackId, robotCode, openSpaceId);
        if (ret) {
            return openSpaceId;
        }
        return "";
    }

    public String deliverCardToTop() {
        String openSpaceId = getTopOpenSpaceId();
        String outTrackId = outTrackIdPrefix + curentOutTrackId;

        Boolean ret = cardManager.deliverCardToTop(outTrackId, openSpaceId);
        if (ret) {
            return openSpaceId;
        }
        return "";
    }

    public String updateCard() {
        String outTrackId = outTrackIdPrefix + curentOutTrackId;

        Map<String, String> cardData = new HashMap<>();

        cardData.put("status", "舱门关闭");
        Boolean ret = cardManager.updateCard(outTrackId, cardData);
        if (ret) {
            return outTrackId;
        }
        return "";
    }

    private String getGroupOpenSpaceId() {
        return "dtv1.card//IM_GROUP." + openConvId;
    }

    private String getTopOpenSpaceId() {
        return "dtv1.card//ONE_BOX." + openConvId;
    }
}
