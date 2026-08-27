package com.socatra.excutivechain.api.interceptors;

import java.io.IOException;

public class OfflineException extends IOException {

    @Override
    public String getMessage() {
        return "No connectivity exception";
    }
}
