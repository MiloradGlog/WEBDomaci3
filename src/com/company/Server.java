package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server extends Thread{

    private ServerSocket serverSocket;
    private int port;
    private QuoteGenerator generator;

    public Server(){

        generator = new QuoteGenerator();

        try {
            port = 8081;
            serverSocket = new ServerSocket(port);
            System.err.println("Server slusa na portu: " + port);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nova konekcija");

                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                sendQuote(generator.generate(), out);

                out.close();
                clientSocket.close();

                System.out.println("Zatvorena konekcija");
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void sendQuote(Quote izreka, BufferedWriter out) throws IOException, InterruptedException {
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Host: localhost\r\n");
        out.write("Date: "+ new Date().toString() +"\r\n");
        out.write("Content-Type: text/html\r\n");
        out.write("\r\n");
        out.write("<h1>"+ izreka.getText() +"</h1>");
        out.write("<P>"+ izreka.getAuthor() +"</P>");

        sleep(50);
    }
}
