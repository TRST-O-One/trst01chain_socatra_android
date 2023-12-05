package com.socatra.excutivechain.api.interceptors;

import java.io.IOException;

/**
 * Created by telekha on 15/3/17.
 */

public class OfflineException extends IOException {

    @Override
    public String getMessage() {
        return "No connectivity exception";
    }
}
