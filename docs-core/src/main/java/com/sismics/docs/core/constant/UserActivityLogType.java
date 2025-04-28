package com.sismics.docs.core.constant;

import java.util.HashMap;
import java.util.Map;

public enum UserActivityLogType {
    /**
     * View
     */
    VIEW_DOCUMENT(1),

    /**
     * Download
     */
    DOWNLOAD(2),

    /**
     * Search
     */
    SEARCH(3),

    /**
     * Login
     */
    LOGIN(4),

    /**
     * Register
     */
    REGISTER(5);

    private final int code;
    private static final Map<Integer, UserActivityLogType> CODE_MAP = new HashMap<>();

    static {
        for (UserActivityLogType type : UserActivityLogType.values()) {
            CODE_MAP.put(type.getCode(), type);
        }
    }

    public static UserActivityLogType getType(final int code) {
        return CODE_MAP.get(code);
    }

    UserActivityLogType(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
