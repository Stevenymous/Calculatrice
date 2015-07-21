/*__________________________________________________*/
/*__________________________________________________*/
/**
 * Fichier : Connexion.java
 *
 * Crée le 21 janv. 2015 à 18:39:35
 *
 * Auteur: Steven MARTINS 
 */
package client;

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
public class Connexion extends Thread
{
	private BufferedReader entree;
	private PrintWriter sortie;
	private Socket socketClient;

	public Connexion(Socket socketClient)
	{
		this.socketClient = socketClient;

		try
		{
			entree = new BufferedReader(new InputStreamReader(
					socketClient.getInputStream()));
			sortie = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socketClient.getOutputStream())), true);
		} catch (IOException e)
		{
			System.err.println("Erreur de flux:" + e);
			System.exit(-1);
		}
	}

	@Override
	public void run()
	{
		String message = null;
		try
		{
			message = entree.readLine();
		} catch (IOException e)
		{
			System.err.println("Erreur lecture : " + e);
			System.exit(-1);
		}

		try
		{
		String[] partie = message.split("~");
		System.out.println("Serveur a reçu une: " + partie[2] + " de "
				+ Integer.parseInt(partie[0]) + " par "
				+ Integer.parseInt(partie[1]));

		int resultat;
		if (partie[2].equals("Multiplication"))
		{
			resultat = Integer.parseInt(partie[0])
					* Integer.parseInt(partie[1]);
		} else
		{
			try{
		
			resultat = Integer.parseInt(partie[0])
					/ Integer.parseInt(partie[1]);
			}catch(ArithmeticException a){
				System.err.println("Division par 0 impossible!: " + a);
				String zero = null;
				sortie.println(zero);
				System.out.println("envoi d'une erreur au client");
				throw new ArithmeticException();
				}

		}
		System.out.println("Le resultat de ce calcul est: " + resultat);
		
		// Envoi
		sortie.println(resultat);
		}catch(NumberFormatException n)
		{
			System.err.println("Erreur entier non parsable : " + n);
			throw new NumberFormatException();
		}
		

		// Fermeture flux et sockets
		try
		{
			entree.close();
			sortie.close();
			socketClient.close();
		} catch (IOException e)
		{
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
