package main.exemple;

import impl.CsvDeclarserFactory;

public class App {

    public static void main(String[] args){

        final var declarserFactory = CsvDeclarserFactory.defaultFactory();
        final var declarser = declarserFactory.declarserOf(DataSample.class, ";");
        System.out.println(declarser.isSuccess());

        final var dataSample = declarser.getValue().parse("Sandro;10");
        System.out.println(dataSample.getValue().getName());

    }

}
