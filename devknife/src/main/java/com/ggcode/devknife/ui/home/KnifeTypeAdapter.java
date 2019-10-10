package com.ggcode.devknife.ui.home;

import android.app.Application;
import android.content.Context;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.ggcode.devknife.manager.init.Knife;

/**
 * @author: zbb 33775
 * @date: 2019/4/27 16:30
 * @desc:
 */
public class KnifeTypeAdapter extends TypeAdapter<Knife> {

    @Override
    public void write(JsonWriter out, Knife value) throws IOException {
        out.beginObject();
        out.name("tag").value(value.getTag());
        out.name("priority").value(value.getPriority());
        out.endObject();
    }

    @Override
    public Knife read(JsonReader in) throws IOException {
        final Knife knife = new Knife() {
            @Override
            public int getCategory() {
                return 0;
            }

            @Override
            public int getName() {
                return 0;
            }

            @Override
            public int getIcon() {
                return 0;
            }

            @Override
            public void onClick(Context context) {

            }

            @Override
            public void onInit(Application application) {

            }
        };
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "tag":
                    knife.setTag(in.nextString());
                    break;
                case "priority":
                    knife.setPriority(in.nextInt());
                    break;
                default:
                    break;
            }
        }
        in.endObject();
        return knife;
    }
}
