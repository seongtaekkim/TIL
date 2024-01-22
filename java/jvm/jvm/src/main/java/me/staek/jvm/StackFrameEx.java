package me.staek.jvm;

import java.lang.StackWalker.StackFrame;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class StackFrameEx {
    public static void main(String args[]) throws Exception {
        Method test1Method = Helper1.class.getDeclaredMethod("test1", (Class[])null);
        test1Method.invoke(null, (Object[]) null);
    }
}

// Helper1 class
class Helper1 {
    protected static void test1() {
        int a = 1;
        Helper2.test2();
    }
}

// Helper2 class
class Helper2 {
    protected static void test2() {
        List<StackFrame> stack = StackWalker.getInstance().walk((s) -> s.collect(Collectors.toList()));
        for(StackFrame frame : stack) {
            System.out.println(frame.getClassName() + " " + frame.getLineNumber() + " " +    frame.getMethodName());
        }
    }
}
