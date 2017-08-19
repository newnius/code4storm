package com.newnius.code4storm.dblp;

/**
 * Created by newnius on 4/26/17.
 *
 */
public class Main {
    public static void main(String[] args){

    }

    public static void sleep(){
        while(true){
            try {
                System.out.println("Finished");
                Thread.sleep(3600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
