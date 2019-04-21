package model;

import java.util.Arrays;

public class Method {
    String name;
    private String pack;
    String[] params;
    String ret;

    public Method(String pack, String ret, String name, String[] params) {
        this.ret = ret.trim();
        this.pack = pack.trim().replace(";", "").replaceAll("\\.", "/");
        this.name = name.trim();
        for (int i = 0; i < params.length; i++) params[i] = params[i].trim();
        this.params = params;
    }

    @Override
    public String toString() {
        return "Method{" +
                "name='" + name + '\'' +
                ", params=" + Arrays.toString(params) +
                ", ret='" + ret + '\'' +
                '}';
    }

    public String getPackage() {
        return pack.replaceAll("\\.", "/");
    }



}
