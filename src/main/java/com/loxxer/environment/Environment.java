package com.loxxer.environment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * To support enviroment and bindings in the lox interpreter
 */
public class Environment {
    private Environment parent;
    private HashMap<String, Object> env;

    public Environment() {
        this.env = new HashMap<String, Object>();
        // For global environment, parent will be null
        this.parent = null;
    }

    public Environment(Environment parent) {
        this.env = new HashMap<String, Object>();
        this.parent = parent;
    }

    public void set(String variable, Object value) {
        this.env.put(variable, value);
    }

    public Object get(String variable) {
        if (this.env.containsKey(variable)) {
            return this.env.get(variable);
        } else if (this.parent != null) {
            return this.parent.get(variable);
        } else {
            return null;
        }
    }

    public Iterator<Entry<String, Object>> getIterator() {
        return env.entrySet().iterator();
    }
}
