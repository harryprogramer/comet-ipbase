package io.lagpixel.comet.worker;

import java.lang.reflect.Method;
import java.util.Objects;

public final class CancellationSource {
    public enum Reason {
        USER_DEFINED(null),
        WORKER_CLOSED("worker was closed"),
        TASK_LIMIT("the worker reached the task limit and cannot handle the job"),
        WORKER_ERROR("the worker encountered an error while performing this task")
        ;

        private String s;

        Reason(String s){
            this.s = s;
        }

        public String string(){
            return s;
        }

        public void string(String new_s){
            if(this != USER_DEFINED){
                throw new IllegalStateException("only " + Reason.USER_DEFINED + " reason has custom message");
            }

            this.s = new_s;
        }
    }

    private final Reason reason;
    private final Method method;
    private final String source;
    private final Class<?> clazzSource;

    public CancellationSource(String source){
        this(source, null);
    }

    public CancellationSource(Class<?> clazz, Method method){
        this(clazz, method, null);
    }

    public CancellationSource(Class<?> clazz, Method method, Reason reason){
        this.method = method;
        this.reason = reason;
        this.source = Objects.requireNonNull(clazz).getName();
        this.clazzSource = clazz;
    }

    public CancellationSource(String source, Reason reason){
        this.source = Objects.requireNonNull(source);
        this.reason = reason;
        this.clazzSource = null;
        this.method = null;
    }

    public boolean hasSourceClass(){
        return Objects.isNull(clazzSource);
    }

    public boolean hasSourceMethod(){
        return Objects.isNull(method);
    }

    public boolean hasSourceString(){
        return Objects.isNull(source);
    }

    public boolean hasReason(){
        return Objects.isNull(reason);
    }

    public Reason getReason(){
        return reason;
    }

    public String getSource(){
        return source;
    }

    public Class<?> getClassSource(){
        return clazzSource;
    }
}
