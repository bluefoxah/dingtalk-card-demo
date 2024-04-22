package org.bluefoxah.demo.manager;

import java.util.HashMap;
import java.util.Map;

import com.aliyun.dingtalkcard_1_0.Client;
import com.aliyun.dingtalkcard_1_0.models.CreateCardHeaders;
import com.aliyun.dingtalkcard_1_0.models.CreateCardRequest;
import com.aliyun.dingtalkcard_1_0.models.CreateCardResponse;
import com.aliyun.dingtalkcard_1_0.models.CreateCardResponseBody;
import com.aliyun.dingtalkcard_1_0.models.DeliverCardHeaders;
import com.aliyun.dingtalkcard_1_0.models.DeliverCardRequest;
import com.aliyun.dingtalkcard_1_0.models.DeliverCardResponse;
import com.aliyun.dingtalkcard_1_0.models.DeliverCardResponseBody;
import com.aliyun.dingtalkcard_1_0.models.UpdateCardHeaders;
import com.aliyun.dingtalkcard_1_0.models.UpdateCardRequest;
import com.aliyun.dingtalkcard_1_0.models.UpdateCardResponse;
import com.aliyun.dingtalkcard_1_0.models.UpdateCardResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CardManager {

    @Autowired
    private DingtalkOpenManager dingtalkOpenManager;

    private Client cardClient;
    private String accessToken;

    @PostConstruct
    public void init() {
        cardClient = createClient();
        accessToken = dingtalkOpenManager.getAccessToken();
    }

    public Boolean createCard(String creator, String outTrackId, String templateId, Map<String, String> data) {

        CreateCardHeaders createCardHeaders = new CreateCardHeaders();
        createCardHeaders.xAcsDingtalkAccessToken = accessToken;

        // 离线通知
        CreateCardRequest.CreateCardRequestImGroupOpenSpaceModelNotification notificationConfig
            = new CreateCardRequest.CreateCardRequestImGroupOpenSpaceModelNotification()
            .setAlertContent("你收到1条新消息")// 离线通知文案
            .setNotificationOff(false);// 是否关闭离线通知

        Map<String, String> lastMsg = new HashMap<>();
        lastMsg.put("ZH_CN", "航班状态");// 会话中列表展示的最后一条消息摘要

        CreateCardRequest.CreateCardRequestImGroupOpenSpaceModel imGroupOpenSpaceModel
            = new CreateCardRequest.CreateCardRequestImGroupOpenSpaceModel()
            .setSupportForward(true) // 允许消息被转发
            .setLastMessageI18n(lastMsg)
            .setNotification(notificationConfig);
        CreateCardRequest.CreateCardRequestTopOpenSpaceModel topOpenSpaceModel
            = new CreateCardRequest.CreateCardRequestTopOpenSpaceModel()
            .setSpaceType("ONE_BOX");

        CreateCardRequest.CreateCardRequestCardData cardData = new CreateCardRequest.CreateCardRequestCardData()
            .setCardParamMap(data);

        CreateCardRequest createCardRequest = new CreateCardRequest()
            .setUserId(creator)
            .setCardTemplateId(templateId)
            .setOutTrackId(outTrackId)
            .setCardData(cardData)
            .setImGroupOpenSpaceModel(imGroupOpenSpaceModel)
            .setTopOpenSpaceModel(topOpenSpaceModel)
            .setUserIdType(1);
        try {
            CreateCardResponse resp = cardClient.createCardWithOptions(createCardRequest, createCardHeaders,
                new RuntimeOptions());
            if (resp.getStatusCode().equals(200)) {
                CreateCardResponseBody body = resp.getBody();
                if (body.getSuccess()) {
                    return true;
                }
            }
            log.warn("createCard failed. requestId:{}, code:{}", resp.getStatusCode());
            return false;
        } catch (TeaException err) {
            if (!Common.empty(err.code) && !Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                log.error("createCard get tea exception. code:{}, msg:{}", err.code, err.message);
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!Common.empty(err.code) && !Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                log.error("createCard get exception. code:{}, msg:{}", err.code, err.message);
            }

        }
        return false;
    }

    public Boolean deliverCardToGroup(String outTrackId, String robotCode, String openSpaceId) {
        DeliverCardHeaders deliverCardHeaders = new DeliverCardHeaders();
        deliverCardHeaders.xAcsDingtalkAccessToken = accessToken;

        DeliverCardRequest.DeliverCardRequestImGroupOpenDeliverModel imGroupOpenDeliverModel
            = new DeliverCardRequest.DeliverCardRequestImGroupOpenDeliverModel()
            .setRobotCode(robotCode);
        DeliverCardRequest deliverCardRequest = new DeliverCardRequest()
            .setOutTrackId(outTrackId)
            .setOpenSpaceId(openSpaceId)
            .setImGroupOpenDeliverModel(imGroupOpenDeliverModel)
            .setUserIdType(1);
        try {
            DeliverCardResponse resp = cardClient.deliverCardWithOptions(deliverCardRequest, deliverCardHeaders,
                new RuntimeOptions());
            if (resp.getStatusCode().equals(200)) {
                DeliverCardResponseBody body = resp.getBody();
                if (body.getSuccess()) {
                    return true;
                }
            }
            log.warn("deliverCardToGroup failed. requestId:{}, code:{}", resp.getStatusCode());
            return false;
        } catch (TeaException err) {
            if (!Common.empty(err.code) && !Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                log.error("deliverCardToGroup get tea exception. code:{}, msg:{}", err.code, err.message);
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!Common.empty(err.code) && !Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                log.error("deliverCardToGroup get exception. code:{}, msg:{}", err.code, err.message);
            }
        }
        return false;
    }

    public Boolean deliverCardToTop(String outTrackId, String openSpaceId) {
        DeliverCardHeaders deliverCardHeaders = new DeliverCardHeaders();
        deliverCardHeaders.xAcsDingtalkAccessToken = accessToken;
        Long expire = System.currentTimeMillis() + 10 * 60 * 1000;
        DeliverCardRequest.DeliverCardRequestTopOpenDeliverModel topOpenDeliverModel
            = new DeliverCardRequest.DeliverCardRequestTopOpenDeliverModel()
            .setExpiredTimeMillis(expire);

        DeliverCardRequest deliverCardRequest = new DeliverCardRequest()
            .setOutTrackId(outTrackId)
            .setOpenSpaceId(openSpaceId)
            .setTopOpenDeliverModel(topOpenDeliverModel)
            .setUserIdType(1);
        try {
            DeliverCardResponse resp = cardClient.deliverCardWithOptions(deliverCardRequest, deliverCardHeaders,
                new RuntimeOptions());
            if (resp.getStatusCode().equals(200)) {
                DeliverCardResponseBody body = resp.getBody();
                if (body.getSuccess()) {
                    return true;
                }
            }
            log.warn("deliverCardToTop failed. requestId:{}, code:{}", resp.getStatusCode());
            return false;
        } catch (TeaException err) {
            if (!Common.empty(err.code) && !Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                log.error("deliverCardToTop get tea exception. code:{}, msg:{}", err.code, err.message);
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!Common.empty(err.code) && !Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                log.error("deliverCardToTop get exception. code:{}, msg:{}", err.code, err.message);
            }
        }
        return false;
    }

    public Boolean updateCard(String outTrackId, Map<String, String> data) {
        UpdateCardHeaders updateCardHeaders = new UpdateCardHeaders();
        updateCardHeaders.xAcsDingtalkAccessToken = accessToken;
        UpdateCardRequest.UpdateCardRequestCardUpdateOptions cardUpdateOptions
            = new UpdateCardRequest.UpdateCardRequestCardUpdateOptions()
            .setUpdateCardDataByKey(true);
        UpdateCardRequest.UpdateCardRequestCardData cardData = new UpdateCardRequest.UpdateCardRequestCardData()
            .setCardParamMap(data);
        UpdateCardRequest updateCardRequest = new UpdateCardRequest()
            .setOutTrackId(outTrackId)
            .setCardData(cardData)
            .setCardUpdateOptions(cardUpdateOptions)
            .setUserIdType(1);
        try {
            UpdateCardResponse resp = cardClient.updateCardWithOptions(updateCardRequest, updateCardHeaders,
                new RuntimeOptions());
            if (resp.getStatusCode().equals(200)) {
                UpdateCardResponseBody body = resp.getBody();
                if (body.getSuccess()) {
                    return true;
                }
            }
            log.warn("updateCard failed. requestId:{}, code:{}", resp.getStatusCode());
            return false;
        } catch (TeaException err) {
            if (!Common.empty(err.code) && !Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                log.error("updateCard get tea exception. code:{}, msg:{}", err.code, err.message);
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!Common.empty(err.code) && !Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                log.error("updateCard get exception. code:{}, msg:{}", err.code, err.message);
            }
        }
        return false;
    }

    protected Client createClient() {
        try {
            Config config = new Config();
            config.protocol = "https";
            config.regionId = "central";
            return new Client(config);
        } catch (Exception e) {
            log.error("create client failed, msg:{}", e.getMessage());
        }
        return null;
    }
}
