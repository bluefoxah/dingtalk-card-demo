package org.bluefoxah.demo.manager;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class DingtalkOpenManager {
    @Getter
    private String accessToken;

    private Client client;

    @Value("${app.key}")
    private String appKey;
    @Value("${app.secret}")
    private String appSecret;

    @PostConstruct
    public void init() {
        getCorpToken();
    }

    private void getCorpToken() {
        client = createClient();
        if (Objects.isNull(client)) {
            return ;
        }
        GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest()
                .setAppKey(appKey)
                .setAppSecret(appSecret);
        try {
            GetAccessTokenResponse resp = client.getAccessToken(getAccessTokenRequest);
            if (resp.getStatusCode().equals(200)) {
                accessToken = resp.getBody().getAccessToken();
            }
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
        }

    }

    private Client createClient()  {
        try {
            Config config = new Config();
            config.protocol = "https";
            config.regionId = "central";
            return new Client(config);
        } catch (Exception e){
            log.warn("DingtalkOpenManager#createClient() get exception", e);
        }
        return null;
    }
}
