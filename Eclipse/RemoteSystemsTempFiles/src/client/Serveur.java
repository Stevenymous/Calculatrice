/*__________________________________________________*/
/*__________________________________________________*/
/**
 * Fichier : Serveur.java
 *
 * Crée le 21 janv. 2015 à 18:38:38
 *
 * Auteur: Steven MARTINS 
 */
package client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*__________________________________________________*/
/**
 * @author steven
 * 
 */
public class Serveur
{
	public static int portEcoute = 5555;

	public static void main(String[] args)
	{
		ServerSocket socketServeur = null;
		
		if(portEcoute > 1024)
			portEcoute = portEcoute;
		else
			throw new IllegalArgumentException("Erreur Port < 1024");
		
		try
		{
			socketServeur = new ServerSocket(portEcoute);
		} catch (IOException e)
		{
			System.err.println("Erreur creation socket: " + e);
			System.exit(-1);
		}

		// Attente clients
		try
		{
			Socket socketClient;
			while (true)
			{
				socketClient = socketServeur.accept();
				Connexion t = new Connexion(socketClient);
				System.out.println("Adresse client: "
						+ socketClient.getInetAddress() + " sur le port: "
						+ socketClient.getPort() + ".");
				t.start();
			}
		} catch (IOException e)
		{
			System.err.println("Erreur attente connexion : "
					+ e);
			System.exit(-1);
		}

		// Fermeture socket
		try
		{
			socketServeur.close();
		} catch (IOException e)
		{
			System.err.println("Erreur fermeture socket : "
					+ e);
			System.exit(-1);
		}
	}

}

/* __________________________________________________ */
/*
 * Fin du fichier Serveur.java
 * /*__________________________________________________
 */
