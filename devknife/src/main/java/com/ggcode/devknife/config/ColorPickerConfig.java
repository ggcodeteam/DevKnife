package com.ggcode.devknife.config;

/**
 * @author: zbb 33775
 * @date: 2019/5/23 17:22
 * @desc:
 */
public class ColorPickerConfig {

    /**
     * 每个像素的间隔
     */
    private int pixelIntervalPx;
    /**
     * 水平距离的像素总数只能为偶数。
     */
    private int diamPixelAmount;

    public ColorPickerConfig(int pixelIntervalPx, int diamPixelAmount) {
        this.pixelIntervalPx = pixelIntervalPx;
        if (diamPixelAmount % 2 != 0) {
            diamPixelAmount++;
        }
        this.diamPixelAmount = diamPixelAmount;
    }

    public int getPixelIntervalPx() {
        return pixelIntervalPx;
    }

    public int getDiamPixelNumber() {
        return diamPixelAmount;
    }
}
