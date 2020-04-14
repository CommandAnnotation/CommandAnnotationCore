package skywolf46.CommandAnnotation.v1_4R1.Data;

import skywolf46.CommandAnnotation.v1_4R1.Util.ClassChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CommandAttribute {
    private static final Class<Object> OBJ_CLASS = Object.class;
    private boolean autoComplete;
    private List<Class<?>> requireParameter = new ArrayList<>();
    private String[] cmd;
    private boolean fallback = true;

    public CommandAttribute(String... cmd) {
        this.cmd = cmd;
    }

    public String[] getCmd() {
        return cmd;
    }

    public CommandAttribute autoComplete(boolean b) {
        this.autoComplete = b;
        return this;
    }

    public CommandAttribute requireParameter(Class... cl) {
        for (Class c : cl)
            requireParameter.add(c);
        return this;
    }

    public CommandAttribute fallBack(boolean b) {
        this.fallback = b;
        return this;
    }

    public boolean isAutoComplete() {
        return autoComplete;
    }

    public boolean isParameterMatch(List<Class<?>> req) {
        req = req.stream().distinct().collect(Collectors.toList());
        for (Class c : requireParameter)
            if (!req.contains(c))
                return false;
        return true;
    }

    public boolean isParameterObjectMatch(List<Object> o) {
        List<Class> lc = new ArrayList<>(requireParameter);
        for (Object obj : o) {
            Class c = ClassChecker.getCompatibleClass(obj.getClass());
            do {
                if (lc.contains(c)) {
                    lc.remove(c);
                    break;
                }
            } while ((c = getSuperClass(c)) != null);
        }
        return lc.size() == 0;
    }

    public Class<?> getSuperClass(Class<?> cl) {
        if (cl.equals(OBJ_CLASS))
            return null;
        cl = cl.getSuperclass();
        if (cl.equals(OBJ_CLASS))
            return null;
        return cl;
    }

    public Object[] sortParameter(CommandArgument c, List<Class> methodWrapper, List<Object> list) {
        HashMap<Class,Integer> indx = new HashMap<>();
        Object[] oa = new Object[methodWrapper.size()];
        List<Integer> matched = new ArrayList<>();
        for(Object o : list){
            for(int i = 0;i < methodWrapper.size();i++){
                if(matched.contains(i))
                    continue;

                if(o.getClass().isInstance(methodWrapper.get(i))){
                    matched.add(i);
                    oa[i] = o;
                }else if(methodWrapper.get(i).isInterface()){
                    Class cd = o.getClass();
                    do{
                        List<Class> ca = new ArrayList<>(Arrays.asList(cd.getInterfaces()));
                        if (ca.contains(methodWrapper.get(i))) {
                            matched.add(i);
                            oa[i] = o;
                        }
                    }while ((cd = cd.getSuperclass()) != null && !cd.equals(OBJ_CLASS));

                }
            }
        }
        return oa;
    }
}
