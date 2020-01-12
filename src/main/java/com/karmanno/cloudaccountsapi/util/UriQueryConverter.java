package com.karmanno.cloudaccountsapi.util;

import com.karmanno.cloudaccountsapi.dto.GoogleConfirmRequest;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Component
public class UriQueryConverter {

    @SneakyThrows
    public GoogleConfirmRequest convertGoogleRequest(String query) {
        String[] fields = query.split("&");
        GoogleConfirmRequest request = new GoogleConfirmRequest();
        for (String field : fields) {
            String[] fieldsMeta = field.split("=");
            Field fieldObj = GoogleConfirmRequest.class.getDeclaredField(fieldsMeta[0]);
            fieldObj.setAccessible(true);
            ReflectionUtils.setField(fieldObj, request, fieldsMeta[1]);
        }
        return request;
    }
}
