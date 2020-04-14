package skywolf46.CommandAnnotation.v1_4R1.Data;

import skywolf46.CommandAnnotation.v1_4R1.Annotations.$P;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;

public class ReflectionData {
    private String targetArgs = null;

    private Class reqClass;

    public ReflectionData(Parameter param) {
        reqClass = param.getType();
        $P mparam = param.getAnnotation($P.class);
        if (mparam != null)
            this.targetArgs = mparam.value();
    }


    public Object matchObject(CommandArgument cArgs, HashMap<String, Integer> vals) {
        if (targetArgs == null) {
            return cArgs.get(reqClass);
        }
        try {
            List<Object> le = cArgs.getNamed(targetArgs, vals.getOrDefault(targetArgs, 0));
            vals.put(targetArgs, vals.getOrDefault(targetArgs, 0) + 1);
            return le == null ? null :
                    (le.size() == 1 ? le.get(0) : le);
        } catch (Exception ex) {
//            return null;
        }
        return null;
//        return targetArgs == null ? cArgs.get(reqClass) : cArgs.getNamed(targetArgs);
    }
}
