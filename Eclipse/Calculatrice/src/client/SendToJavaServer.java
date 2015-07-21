package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import conf.Conf;

/*__________________________________________________*/
/**
 * @author steven
 *
 */
public class SendToJavaServer {
	
	/**
	 * Socket du serveur
	 */
	private Socket socket = null;
	
	/**
	 * Buffer de reception
	 */
	private BufferedReader input = null;
	
	/**
	 * Buffer d'envoie
	 */
	private PrintWriter output = null;
	
	
	/**
	 * Constructeur: Initialise la socket et les buffers
	 */
	public SendToJavaServer() {
		try{
			socket = new Socket("localhost", Conf.PORTJAVA);
		} catch (UnknownHostException e){
			System.err.println("Erreur hote inconnue: " + e);
			System.exit(-1);
		} catch (IOException e){
			System.err.println("Erreur creation socket, le serveur est indisponible: " + e);
			System.exit(-1);
		}
		
		try{
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException e){
			System.err.println("Erreur de flux: " + e);
			System.exit(-1);
		}
	}
	
	
	/**
	 * Envoie et reception des donnees au serveur Java
	 * @param entier1 : Premier nombre
	 * @param entier2 : Second nombre
	 * @param operation : Operation a effectuer
	 * @return le resultat emis par le serveur
	 */
	public String send(double entier1, double entier2, String operation){
		//System.out.println("Votre calcul est une: " + operation + " de " + Integer.toString(entier1) + " par " + Integer.toString(entier2));
		
		// Envoi de l'operation avec "~" entre chaque parametres pour le protocole
		String message = Double.toString(entier1) + "~" + Double.toString(entier2) + "~" + operation;
		output.println(message);

		// Lecture du resultat + erreur si division par 0
		try{
			message = input.readLine();
			if (message.equals("null")){
				System.out.println("Vous avez essaye de diviser par 0! Veuillez recommencer\n");
			}
		} catch (IOException e){
			System.err.println("Erreur lecture : " + e);
			System.exit(-1);
		}
		close();
		return message;
	}
	
	
	/**
	 * 	Fermeture flux et sockets
	 */
	public void close(){
		try{
			input.close();
			output.close();
			socket.close();
		} catch (IOException e){
			System.err.println("Erreur fermeture flux et socket : " + e);
			System.exit(-1);
		}
	}
}
