package com.hopoong.eventhubboard.api;

import com.hopoong.eventhubboard.response.CommonResponseCodeEnum;
import com.hopoong.eventhubboard.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/")
    public SuccessResponse test() {
        return new SuccessResponse(CommonResponseCodeEnum.SERVER, "tests");
    }
}
