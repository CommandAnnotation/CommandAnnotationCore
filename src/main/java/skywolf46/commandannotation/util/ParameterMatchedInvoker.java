package skywolf46.commandannotation.util;

import skywolf46.commandannotation.annotations.CommandParam;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class ParameterMatchedInvoker {
    private Method mtd;
    private ParameterMatcher matcher;

    public ParameterMatchedInvoker(Method target) {
        this.mtd = target;
        this.matcher = new ParameterMatcher();
    }


    public void invoke(ParameterStorage storage) throws Throwable {
        matcher.invoke(storage);
    }

    public Method getMethod() {
        return mtd;
    }

    class ParameterMatcher {
        private HashMap<Class, List<Integer>> matched = new HashMap<>();
        private HashMap<Integer, DataPair<String, Class>> namedMatched = new HashMap<>();
        private int paramLength;
        private boolean exceptionWhenMismatch = false;

        public ParameterMatcher() {
            Parameter[] pl = mtd.getParameters();
            this.paramLength = pl.length;
            for (int i = 0; i < pl.length; i++) {
                Parameter p = pl[i];
                CommandParam tl = p.getAnnotation(CommandParam.class);
                if (tl != null) {
                    namedMatched.put(i, new DataPair<>(tl.value(), p.getType()));
                } else {
                    matched.computeIfAbsent(p.getType(), a -> new ArrayList<>()).add(i);
                }
            }
            System.out.println(matched);
        }

        public void invoke(ParameterStorage storage) throws Throwable {
            Object[] params = new Object[paramLength];
            // 1차 - 네임드 파라미터 적용
            for (int i = 0; i < paramLength; i++) {
                // 네임드 파라미터는 강제 적용
                if (namedMatched.containsKey(i)) {
                    DataPair<String, Class> cl = namedMatched.get(i);
                    Object ox = storage.get(cl.getKey());
                    if (ox == null) {
                        // 대상 없음
                        params[i] = null;
                    } else if (!cl.getValue().isAssignableFrom(ox.getClass())) {
                        // 클래스타입 미스매칭
                        if (!exceptionWhenMismatch)
                            params[i] = null;
                    } else {
                        // 정상 적용
                        params[i] = ox;
                    }
                }
            }
            // 2차 - 기본 파라미터 적용
            for (Map.Entry<Class, List<Integer>> cl : matched.entrySet()) {
                List<Integer> ri = cl.getValue();
                List<Object> slot = storage.getAll(cl.getKey());
                System.out.println(slot);
                System.out.println(ri);
                for (int i = 0; i < ri.size(); i++) {
                    if (slot.size() <= i) {
                        if (cl.getKey().isPrimitive()) {
                            params[ri.get(i)] = 0;
                        } else {
                            params[ri.get(i)] = null;
                        }
                    } else {
                        System.out.println("Setting " + ri.get(i) + " to " + slot.get(i));
                        params[ri.get(i)] = slot.get(i);
                        System.out.println("Params=" + Arrays.toString(params));
                    }
                }
            }
            System.out.println(Arrays.toString(params));
            // 최종 - 메서드 실행
            try {
                mtd.invoke(null, params);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
    }


}
