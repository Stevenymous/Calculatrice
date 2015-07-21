/*__________________________________________________*/
/*__________________________________________________*/
/**
 * Fichier : Client.java
 *
 * Crée le 21 janv. 2015 à 18:35:23
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
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Scanner;

/*__________________________________________________*/
/**
 * @author steven
 * 
 */
public class Client
{

	public static void main(String[] args)
	{
		menu();
	}

	private static void menu()
	{
		Scanner scanner = new Scanner(System.in);
		int choix = 0;
		int entier1, entier2;

		StringBuffer sb = new StringBuffer();
		sb.append("Type d'operation:\n")
				.append("Addition = 1 | Soustraction =  2 | Multiplication = 3 | Division = 4 | Quitter = 5");
		System.out.println(sb);
		sb.setLength(0);
		choix = scanner.nextInt();

		switch (choix)
		{
		case 1:
			sb.append("Tu as choisis une Addition\n").append(
					"premier chiffre : ");
			System.out.println(sb);
			sb.setLength(0);
			entier1 = scanner.nextInt();
			sb.append("deuxième chiffre : ");
			System.out.println(sb);
			sb.setLength(0);
			entier2 = scanner.nextInt();
			serveurHttp(entier1, entier2, "Addition");
			break;

		case 2:
			sb.append("Tu as choisis une Soustraction\n").append(
					"premier chiffre : ");
			System.out.println(sb);
			sb.setLength(0);
			entier1 = scanner.nextInt();
			sb.append("deuxième chiffre : ");
			System.out.println(sb);
			sb.setLength(0);
			entier2 = scanner.nextInt();
			serveurHttp(entier1, entier2, "Soustraction");
			break;

		case 3:
			sb.append("Tu as choisis une Multiplication\n").append(
					"premier chiffre : ");
			System.out.println(sb);
			sb.setLength(0);
			entier1 = scanner.nextInt();
			sb.append("deuxième chiffre : ");
			System.out.println(sb);
			sb.setLength(0);
			entier2 = scanner.nextInt();
			serveurTcp(entier1, entier2, "Multiplication");
			break;
		case 4:
			sb.append("Tu as choisis une Division\n").append(
					"premier chiffre : ");
			System.out.println(sb);
			sb.setLength(0);
			entier1 = scanner.nextInt();
			sb.append("deuxième chiffre : ");
			System.out.println(sb);
			sb.setLength(0);
			entier2 = scanner.nextInt();
			serveurTcp(entier1, entier2, "Division");
			break;

		case 5:
			System.out.println("Vous avez quitté le programme");
			System.exit(0);

		default:
			System.out.println("Veuillez saisir un chiffre entre 1 et 5");
			menu();
		}
	}

	public static void serveurTcp(int entier1, int entier2, String operation)
	{
		Socket socket = null;
		try
		{
			socket = new Socket("localhost", Serveur.portEcoute);
		} catch (UnknownHostException e)
		{
			System.err.println("Erreur hite inconnue: " + e);
			System.exit(-1);
		} catch (IOException e)
		{
			System.err.println("Erreur creation socket, le serveur est indisponible: " + e);
			System.exit(-1);
		}

		BufferedReader input = null;
		PrintWriter output = null;
		try
		{
			input = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
		} catch (IOException e)
		{
			System.err.println("Erreur de flux: " + e);
			System.exit(-1);
		}

		// Envoi de l'operation
		String message = Integer.toString(entier1) + "~"
				+ Integer.toString(entier2) + "~" + operation;
		System.out.println("Votre calcul est une: " + operation + " de "
				+ Integer.toString(entier1) + " par "
				+ Integer.toString(entier2));
		output.println(message);

		// Lecture du résultat + erreur si division par 0
		try
		{
			message = input.readLine();
			if (message.equals("null"))
			{
				System.out.println("Vous avez essayer de diviser par 0! Veuillez recommencer\n");
				menu();
			}
		} catch (IOException e)
		{
			System.err.println("Erreur lecture : " + e);
			System.exit(-1);
		}
		System.out.println("Le résultat est: " + message);

		// Fermeture flux et socket
		try
		{
			input.close();
			output.close();
			socket.close();
		} catch (IOException e)
		{
			System.err.println("Erreur fermeture flux et socket : " + e);
			System.exit(-1);
		}
		menu();
	}

	public static void serveurHttp(int entier1, int entier2, String operation)
	{
		try
		{
			/*String params = URLEncoder.encode("addentier1", "UTF-8") + "="
					+ URLEncoder.encode(Integer.toString(entier1), "UTF-8");
			params += "&" + URLEncoder.encode("addentier2", "UTF-8") + "="
					+ URLEncoder.encode(Integer.toString(entier2), "UTF-8");*/
			
			String valeurs= "entier1="+entier1+"&entier2="+entier2;

			Socket socket = new Socket(InetAddress.getLocalHost(), 5555);
			String path = "/~stmartins/projetReseaux/resultat.php";

			BufferedWriter ecriture = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream(), "UTF8"));
			if (operation.equals("Addition"))
			{
				ecriture.write("POST " + path + " HTTP/1.1\r\n");
				ecriture.write("Content-Length: " + valeurs.length() + "\r\n");
				ecriture.write("Content-Type: application/x-www-form-urlencoded\r\n");
				ecriture.write("\r\n");

				//wr.write(params);
				ecriture.flush();

				BufferedReader lecture = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				String line;
				String result = null;
				while ((line = lecture.readLine()) != null)
				{
					result += line;
				}
				String[] resultat = result.split("~@~");
				System.out.println("Résultat: " + resultat[1]);
				ecriture.close();
				lecture.close();
				menu();
			} else
			{
				try
				{
					PrintWriter output = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(socket.getOutputStream())));
					output.println("GET " + path + "?&entier1="
							+ entier1 + "&entier2=" + entier2 + " HTTP/1.1 \r\n");
					
					output.println();
					output.flush();

					BufferedReader input = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));

					String line;
					String result = null;
					while ((line = input.readLine()) != null)
					{
						result += line;
					}
					String[] resultat = result.split("~@~");
					System.out.println("Résultat: " + resultat[1]);
					
					output.close();
					input.close();
					menu();
				} catch (IOException io)
				{
					System.err.println("Erreur input/output (soustraction) : " + io);
				}catch (Exception e)
				{
					System.err.println("Erreur autre (client - soustraction) : " + e);
				}
			}

		} catch (IOException io)
		{
			System.err.println("Erreur input/output (addition) : " + io);
		}catch (Exception e)
		{
			System.err.println("Erreur autre (client - addition)  : " + e);
		}
	}

}

/* __________________________________________________ */
/*
 * Fin du fichier Client.java
 * /*__________________________________________________
 */
