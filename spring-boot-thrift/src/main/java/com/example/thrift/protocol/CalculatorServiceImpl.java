package com.example.thrift.protocol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yuweijun 2016-06-13.
 */
@Component
public class CalculatorServiceImpl implements TCalculatorService {

    @Autowired
    CalculatorService calculatorService;

    @Override
    public int calculate(int num1, int num2, TOperation op) throws TDivisionByZeroException {
        switch(op) {
            case ADD:
                return calculatorService.add(num1, num2);
            case SUBTRACT:
                return calculatorService.subtract(num1, num2);
            case MULTIPLY:
                return calculatorService.multiply(num1, num2);
            case DIVIDE:
                try {
                    return calculatorService.divide(num1, num2);
                } catch(IllegalArgumentException e) {
                    throw new TDivisionByZeroException();
                }
            default:
                throw new IllegalArgumentException("Unknown operation " + op);
        }
    }

}
