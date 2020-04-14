package skywolf46.CommandAnnotation.v1_4R1.Data.Auto;

import skywolf46.CommandAnnotation.v1_4R1.Data.ParameterIterator;

import java.util.ArrayList;
import java.util.List;

public class AutoMatchingDataType {
    private String type;
    private int repeat = 1;

    public AutoMatchingDataType(String data) {
        if (data.contains("...")) {
            int index = data.indexOf("...");
            type = data.substring(0, index);
            String next = data.substring(index + 3);
            repeat = Integer.parseInt(data.substring(index + 3));
        } else {
            this.type = data;
        }
        this.type = this.type.toLowerCase();
        if(!ParameterIterator.isParserExists(data)){
            throw new IllegalStateException("Parameter type" + data + " is not registered.");
        }
    }

    public String getType() {
        return type;
    }

    public int getRepeat() {
        return repeat;
    }

    public <T> List<T> parse(ParameterIterator iter){
        List<T> l = new ArrayList<>();
        for(int i = 0;i < repeat;i++)
            l.add(iter.next(type));
        return l;
    }
}
