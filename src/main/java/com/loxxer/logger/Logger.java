package com.loxxer.logger;

import java.lang.Class;

public class Logger {
    Class loggingClass;

    public Logger(Class<?> loggingClass) {
	this.loggingClass = loggingClass;
    }

    public void log(String level, String message) {
	System.out.println("[" + loggingClass.getTypeName() + "]" + " [" + level + "] " + message);
    }

    public void info(String message) {
	log("INFO",message);
    }
}
