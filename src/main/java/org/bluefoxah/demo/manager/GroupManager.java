package org.bluefoxah.demo.manager;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatCreateRequest;
import com.dingtalk.api.request.OapiImChatScenegroupCreateRequest;
import com.dingtalk.api.response.OapiChatCreateResponse;
import com.dingtalk.api.response.OapiImChatScenegroupCreateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSON;

@Component
@Slf4j
public class GroupManager {

    @Value("${group.template.id}")
    private String gropuTemplateId;

    @Autowired
    private DingtalkOpenManager dingtalkOpenManager;

    public String createGroup(String groupName, String groupOwner, List<String> groupMembers) {
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/chat/create");
            OapiChatCreateRequest req = new OapiChatCreateRequest();
            req.setName(groupName);
            req.setOwner(groupOwner);
            req.setUseridlist(groupMembers);
            req.setShowHistoryType(1L);
            req.setSearchable(0L);
            req.setValidationType(0L);
            req.setMentionAllAuthority(0L);
            req.setManagementType(0L);
            req.setChatBannedType(0L);
            OapiChatCreateResponse rsp = client.execute(req, dingtalkOpenManager.getAccessToken());
            if (rsp.isSuccess()) {
                return rsp.getOpenConversationId();
            }
        } catch (Exception e) {
            log.error("create group error", e);
        }
        return "";
    }

    public String createGroupByTemplate(String groupName, String groupOwner, List<String> groupMembers) {
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/im/chat/scenegroup/create");

            log.info("createTemplateGroup params, groupName:{}, groupOwner:{}, groupMembers:{}", groupName, groupOwner, JSON.toJSONString(groupMembers));
            OapiImChatScenegroupCreateRequest req = new OapiImChatScenegroupCreateRequest();
            req.setTitle(groupName);
            req.setTemplateId(gropuTemplateId);
            req.setOwnerUserId(groupOwner);
            String uids = StringUtils.join(groupMembers, ",");
            req.setUserIds(uids);
            String uuid = UUID.randomUUID().toString();
            req.setUuid(uuid);
            req.setMentionAllAuthority(0L);
            req.setShowHistoryType(0L);
            req.setValidationType(0L);
            req.setSearchable(0L);
            req.setChatBannedType(0L);
            req.setManagementType(0L);
            req.setOnlyAdminCanDing(0L);
            req.setAllMembersCanCreateMcsConf(1L);
            req.setAllMembersCanCreateCalendar(0L);
            req.setGroupEmailDisabled(0L);
            req.setOnlyAdminCanSetMsgTop(0L);
            req.setAddFriendForbidden(0L);
            req.setGroupLiveSwitch(1L);
            req.setMembersToAdminChat(0L);
            OapiImChatScenegroupCreateResponse rsp = client.execute(req, dingtalkOpenManager.getAccessToken());
            log.info("createTemplateGroup rsp:{}", JSON.toJSONString(rsp));
            if (rsp.isSuccess()) {
                return rsp.getResult().getOpenConversationId();
            }else {
                log.error("createTemplateGroup error, headers:{}, headers:{}", JSON.toJSON(req.getHeaderMap()), JSON.toJSON(rsp.getHeaderContent()));
            }
        } catch (Exception e) {
            log.error("createTemplateGroup error", e);
        }
        return "";
    }
}
