package org.bluefoxah.demo.manager;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiChatCreateRequest;
import com.dingtalk.api.response.OapiChatCreateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class GroupManager {

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
}
