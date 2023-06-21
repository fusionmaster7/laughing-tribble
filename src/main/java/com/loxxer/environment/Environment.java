package com.loxxer.environment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * To support enviroment and bindings in the lox interpreter
 */
public class Environment {
    private HashMap<String, Object> env;

    public Environment() {
        this.env = new HashMap<String, Object>();
    }

    public void set(String variable, Object value) {
        this.env.put(variable, value);
    }

    public Object get(String variable) {
        if (this.env.containsKey(variable)) {
            return this.env.get(variable);
        } else {
            return null;
        }
    }

    public Iterator<Entry<String, Object>> getIterator() {
        return env.entrySet().iterator();
    }
}
