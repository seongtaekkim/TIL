package me.staek.jvm;

import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 * https://stackoverflow.com/questions/62433660/is-it-possible-to-get-jdis-current-stackframe-in-java-at-the-debuggee-side
 * https://alvinalexander.com/scala/fp-book/recursion-jvm-stacks-stack-frames/
 *
 * jvm 관련 옵션같은걸 찾아보자
 */
public class VirtualMachineManagerTest {
    public static void main(String[] args) throws Exception {
        Object o = null;
        int test = 42;
        String s = "hello";
        Map<String, Object> vars = variables();
        System.out.println(vars);
    }
    // get the variables in the caller’s frame
    static Map<String,Object> variables() throws Exception {
        Thread th = Thread.currentThread();
        String oldName = th.getName(), tmpName = UUID.randomUUID().toString();
        th.setName(tmpName);
        long depth = StackWalker.getInstance(
                StackWalker.Option.SHOW_HIDDEN_FRAMES).walk(Stream::count) - 1;

        ExecutorService es = Executors.newSingleThreadExecutor();
        try {
            return es.<Map<String,Object>>submit(() -> {
                VirtualMachineManager m = Bootstrap.virtualMachineManager();
                for(var ac: m.attachingConnectors()) {
                    Map<String, Connector.Argument> arg = ac.defaultArguments();
                    Connector.Argument a = arg.get("pid");
                    if(a == null) continue;
                    a.setValue(String.valueOf(ProcessHandle.current().pid()));
                    VirtualMachine vm = ac.attach(arg);
                    return getVariableValues(vm, tmpName, depth);
                }
                return Map.of();
            }).get();
        } finally {
            th.setName(oldName);
            es.shutdown();
        }
    }

    private static Map<String,Object> getVariableValues(
            VirtualMachine vm, String tmpName, long depth)
            throws IncompatibleThreadStateException, AbsentInformationException {

        for(ThreadReference r: vm.allThreads()) {
            if(!r.name().equals(tmpName)) continue;
            r.suspend();
            try {
                StackFrame frame = r.frame((int)(r.frameCount() - depth));
                return frame.getValues(frame.visibleVariables())
                        .entrySet().stream().collect(HashMap::new,
                                (m,e) -> m.put(e.getKey().name(), t(e.getValue())), Map::putAll);
            } finally {
                r.resume();
            }
        }
        return Map.of();
    }
    private static Object t(Value v) {
        if(v == null) return null;
        switch(v.type().signature()) {
            case "Z": return ((PrimitiveValue)v).booleanValue();
            case "B": return ((PrimitiveValue)v).byteValue();
            case "S": return ((PrimitiveValue)v).shortValue();
            case "C": return ((PrimitiveValue)v).charValue();
            case "I": return ((PrimitiveValue)v).intValue();
            case "J": return ((PrimitiveValue)v).longValue();
            case "F": return ((PrimitiveValue)v).floatValue();
            case "D": return ((PrimitiveValue)v).doubleValue();
            case "Ljava/lang/String;": return ((StringReference)v).value();
        }
        if(v instanceof ArrayReference)
            return ((ArrayReference)v).getValues().stream().map(e -> t(e)).toArray();
        return v.type().name()+'@'+Integer.toHexString(v.hashCode());
    }
}
