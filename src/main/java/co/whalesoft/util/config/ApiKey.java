package co.whalesoft.util.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiKey {

    @Value("${FCM-URL}")
    private String fcm_url;
    @Value("${FCM-ANDROID-KEY}")
    private String fcm_android_key;
    @Value("${FCM-IOS-KEY}")
    private String fcm_ios_key;

    public String getFcm_url() {
        return fcm_url;
    }

    public String getFcm_android_key() {
        return fcm_android_key;
    }

    public String getFcm_ios_key() {
        return fcm_ios_key;
    }
}
