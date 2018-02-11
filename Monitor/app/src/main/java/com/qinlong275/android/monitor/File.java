package com.qinlong275.android.monitor;

/**
 * Created by 秦龙 on 2017/10/23.
 */

public class File {
    private String name;
    private String id;

    public File(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
