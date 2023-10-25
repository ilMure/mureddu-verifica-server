package it.fi.meucci;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Server mioServer = new Server();
        for (;;){ //il server non viene mai chiuso
            mioServer.attendi();
            mioServer.comunica();
        }
    }
}
