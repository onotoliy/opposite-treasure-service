package com.github.onotoliy.opposite.treasure;

import com.github.onotoliy.opposite.treasure.services.notifications.TelegramNotificationExecutor;
import com.github.onotoliy.opposite.treasure.services.notifications.schedule.NotificationObject;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

public class TelegramNotificationExecutorTest {

    @Test
    @Ignore
    public void test() {
        new TelegramNotificationExecutor(
            "api.telegram.org",
            "1328298814:AAG4bgSetN1amlU96X1P1Yb0LWMTysfO9Xo",
            "-1001371677365"
        ).notify("Title", "<b>Body</b>", new HashMap<>());
    }

}
