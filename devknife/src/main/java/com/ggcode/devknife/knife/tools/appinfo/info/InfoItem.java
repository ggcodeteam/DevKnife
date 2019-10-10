package com.ggcode.devknife.knife.tools.appinfo.info;

/**
 * @author: zbb 33775
 * @date: 2019/5/28 9:57
 * @desc:
 */
public class InfoItem {

    private final String name;
    private String value;
    protected int type;

    public InfoItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public int getType() {
        return type;
    }
}
