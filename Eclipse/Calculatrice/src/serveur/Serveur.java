/*__________________________________________________*/
/*__________________________________________________*/
/**
 * Fichier : Serveur.java
 *
 * Cr�e le 21 janv. 2015 � 18:38:38
 *
 * Auteur: Steven MARTINS 
 */
package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import conf.Conf;

/*__________________________________________________*/
/**
 * @author steven
 * 
 */
public class Serveur{
	/**
	 * Lancement du serveur
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Socket d'ecoute
		ServerSocket socketServeur = null;
		
		// Initiallisation de la socket
		try{
			socketServeur = new ServerSocket(Conf.PORTJAVA);
		} catch (IOException e){
			System.err.println("Erreur creation socket: " + e);
			System.exit(-1);
		}catch (IllegalArgumentException iae){
			System.err.println("Erreur creation socket , port non compris entre 0 et 65 535: " + iae);
			System.exit(-2);
		}
		// Attente clients et lancement de l'action
		try{
			Socket socketClient;
			while (true){
				socketClient = socketServeur.accept();
				Connexion t = new Connexion(socketClient);
				System.out.println("Adresse client: " + socketClient.getInetAddress() + " sur le port: " + socketClient.getPort() + ".");
				t.start();
			}
		} catch (IOException e){
			System.err.println("Erreur attente connexion : " + e);
			System.exit(-1);
		} finally{
			// Fermeture socket
			try {
				socketServeur.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}

/* __________________________________________________ */
/*
 * Fin du fichier Serveur.java
 * /*__________________________________________________
 */
