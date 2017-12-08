package com.example.thrift.tutorial;

import com.example.thrift.tutorial.shared.SharedStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @author yuweijun 2016-06-14.
 */
public class CalculatorImpl implements Calculator.Iface {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private HashMap<Integer,SharedStruct> log;

    public CalculatorImpl() {
        log = new HashMap<Integer, SharedStruct>();
    }

    public void ping() {
        logger.info("ping()");
    }

    public int add(int n1, int n2) {
        logger.info("add(" + n1 + "," + n2 + ")");
        return n1 + n2;
    }

    public int calculate(int logid, Work work) throws InvalidOperation {
        logger.info("calculate(" + logid + ", {" + work.op + "," + work.num1 + "," + work.num2 + "})");
        int val = 0;
        switch (work.op) {
            case ADD:
                val = work.num1 + work.num2;
                break;
            case SUBTRACT:
                val = work.num1 - work.num2;
                break;
            case MULTIPLY:
                val = work.num1 * work.num2;
                break;
            case DIVIDE:
                if (work.num2 == 0) {
                    InvalidOperation io = new InvalidOperation();
                    io.whatOp = work.op.getValue();
                    io.why = "Cannot divide by 0";
                    throw io;
                }
                val = work.num1 / work.num2;
                break;
            default:
                InvalidOperation io = new InvalidOperation();
                io.whatOp = work.op.getValue();
                io.why = "Unknown operation";
                throw io;
        }

        SharedStruct entry = new SharedStruct();
        entry.key = logid;
        entry.value = Integer.toString(val);
        log.put(logid, entry);

        return val;
    }

    public SharedStruct getStruct(int key) {
        logger.info("getStruct(" + key + ")");
        return log.get(key);
    }

    public void zip() {
        logger.info("zip()");
    }

}
