package model;

import org.json.JSONObject;

/**
 * An abstract class to indicate toJson method for
 * classes that convert its information into
 * a JSONObject
 */
public abstract class JsonObjectFile {

    private String type;

    public JsonObjectFile(String type) {
        this.type = type;
    }

    public abstract JSONObject toJson();
}
