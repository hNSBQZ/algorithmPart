package org.example;
import myLearn.dataHandling;
import org.ejml.simple.SimpleMatrix;

import java.io.*;
import java.util.HashMap;

import static myLearn.dataHandling.loadMatrixFromCsv;
import static myLearn.dataHandling.loadMatrixFromCsv_xy;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        SimpleMatrix x=loadMatrixFromCsv_xy("JDT.csv").get("x");
        SimpleMatrix y=loadMatrixFromCsv_xy("JDT.csv").get("y");


    }
}
