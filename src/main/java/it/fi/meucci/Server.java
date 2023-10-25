package it.fi.meucci;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    ServerSocket server = null;
    Socket client = null;
    String stringaRicevuta = null;
    String stringaRisposta = null;
    BufferedReader inDalClient = null;
    DataOutputStream outVersoClient;

    public Socket attendi(){
        try {
            System.out.println("SERVER is running");
            if (server == null) server = new ServerSocket(6789);
            client = server.accept();
            inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outVersoClient = new DataOutputStream(client.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore durante istanza del server");
        }
        return client;
    }

    public void comunica(){
        try {
            System.out.println("ciao client, inserisci due operandi (se non interi, separarne la parte decimale con '.') e l'operatore (+,-,/,*) tuti separati da virgole. Sono in attesa...");
            for (;;){
                stringaRicevuta = inDalClient.readLine();
                //controllo contenuto stringa
                if (stringaRicevuta.equals("") || stringaRicevuta.equals("BYE")){ // messaggio per terminare la comunicazione
                    System.out.println("SERVER: ho terminato... " +stringaRicevuta);
                    break;
                }else{
                    boolean syntaxError = false;
                    boolean infiniteError = false; // per gestire la divisione per 0
                    String[] dati = stringaRicevuta.split(","); //gli indici dell'array dati vanno da 0 a 2 (0 = 1num, 1 = 2num, 2 = operatore)
                    if (dati.length < 2) syntaxError = true; //se non è avvenuto uno split completo/corretto
                    float num1 = 0;
                    float num2 = 0;
                    float result = 0;
                    if (!syntaxError){ //se non vi sono errori nella scrittura del client
                        try { //se il parse riguarda un valore non numerico si genera un eccezione
                            num1 = Float.parseFloat(dati[0]);
                        } catch (Exception e) {
                            syntaxError = true;
                        }
                        try {
                            num2 = Float.parseFloat(dati[1]);
                        } catch (Exception e) {
                            syntaxError = true;
                        }
                        String op = dati[2];
                        switch (op){ //gestione operazioni matematiche possibili
                            case "+":
                                result = num1 + num2;
                                break;
                            case "-":
                                result = num1 - num2;
                                break;
                            case "*":
                                result = num1 * num2;
                                break;
                            case "/":
                                if (num2 == 0) infiniteError = true;
                                else result = num1 / num2;
                                break;
                            default:
                                syntaxError = true;
                        }                    
                    }
                    

                    System.out.println("invio la stringa di risposta al client...");
                    if (syntaxError) stringaRisposta = "Errore nella scrittura dati";
                    else if(infiniteError) stringaRisposta = "Errore: non e' possibile dividere per 0";
                    else stringaRisposta = String.valueOf(result);
                    outVersoClient.writeBytes(stringaRisposta+"\n");
                }                  
            }
            client.close();
            
        } catch (Exception e) {
            System.out.println("Errore durante la comunicazione");
        }
        
    }

    
}
