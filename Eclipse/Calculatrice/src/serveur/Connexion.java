/*__________________________________________________*/
/*__________________________________________________*/
/**
 * Fichier : Connexion.java
 *
 * Cr�e le 21 janv. 2015 � 18:39:35
 *
 * Auteur: Steven MARTINS 
 */
package serveur;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/*__________________________________________________*/
/**
 * @author steven
 * 
 */
public class Connexion extends Thread{
	/**
	 * Buffer d'entree
	 */
	private BufferedReader entree;
	
	/**
	 * Buffer de sortie
	 */
	private PrintWriter sortie;
	
	/**
	 * Socket
	 */
	private Socket socketClient;
	
	/**
	 * Constructeur: initialisation de la socket et des buffers
	 * @param socket : socket de connexion
	 */
	public Connexion(Socket socket){
		this.socketClient = socket;

		try{
			entree = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
			sortie = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream())), true);
		} catch (IOException e){
			System.err.println("Erreur de flux:" + e);
			System.exit(-1);
		}
	}
	
	/**
	 * Lancement de l'action executee par le Thread
	 */
	@Override
	public void run(){
		String message = null;
		
		// Lecture du message
		try{
			message = entree.readLine();
		} catch (IOException e){
			System.err.println("Erreur lecture : " + e);
			System.exit(-1);
		}
		
		//Lancement du calcul
		try{
			String[] partie = message.split("~");
			System.out.println("Serveur a recu une: " + partie[2] + " de " + Double.parseDouble(partie[0]) + " par " + Double.parseDouble(partie[1]));
			
			double resultat = 0;
			
			switch (partie[2]) {
				case "Multiplication":
					resultat = Double.parseDouble(partie[0]) * Double.parseDouble(partie[1]);
					break;
					
				case "*":
					resultat = Double.parseDouble(partie[0]) * Double.parseDouble(partie[1]);
					break;
					
				case "Division":
					
						resultat = Double.parseDouble(partie[0]) / Double.parseDouble(partie[1]);
					
						if(Double.isInfinite(resultat))
						{
							System.err.println("Division par 0 impossible! Envoi d'une erreur au client\n");
							String zero = null;
							sortie.println(zero);
							resultat = 0;
						}					
					break;
				
				case "/":
					
					resultat = Double.parseDouble(partie[0]) / Double.parseDouble(partie[1]);
				
					if(Double.parseDouble(partie[1]) == 0)
					{
						System.err.println("Division par 0 impossible! Envoi d'une erreur au client\n");
						String zero = "Division par 0 impossible!";
						sortie.println(zero);
						resultat = 0;
					}					
				break;
					
				
			}
			if (resultat != 0 && partie[2].equals("*"))
			{
				resultat = (double) Math.round(resultat * 1000) / 1000;
				sortie.println(resultat);
			}
			if (resultat != 0)
			{
				resultat = (double) Math.round(resultat * 1000) / 1000;
				System.out.println("Le resultat de ce calcul est: " + resultat);
				sortie.println(resultat);
			}	
			// Envoi du resultat
			sortie.println(resultat);
			
		}catch(NumberFormatException n){
			System.err.println("Erreur entier non parsable : " + n);
			throw new NumberFormatException();
		}
		

		// Fermeture flux et sockets
		try{
			entree.close();
			sortie.close();
			socketClient.close();
		} catch (IOException e){
			System.err.println("Erreur fermeture flux et socket : " + e);
			System.exit(-1);
		}
	}

}

/* __________________________________________________ */
/*
 * Fin du fichier Connexion.java
 * /*__________________________________________________
 */
