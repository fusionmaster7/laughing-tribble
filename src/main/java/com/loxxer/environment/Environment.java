package com.loxxer.environment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.loxxer.error.ErrorHandler;

import com.loxxer.parser.RuntimeError;

import com.loxxer.lexical.LexicalToken;
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

    public boolean set(String variable, Object value) {
	if(this.env.containsKey(variable)) {
	    this.env.put(variable, value);
	    return true;
	} else if (this.parent != null) {
	    return this.parent.set(variable, value);
	} else {
	    return false;
	}
    }

    public void define(LexicalToken token, Object value) {
	String lexemme = token.getLexemme();
	if(get(lexemme) == null) {
	    this.env.put(lexemme, value);
	} else {
	    // TODO - Use proper error handling here. Create a static error reporting class
	    System.out.println("Variable already declared");
	}
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
