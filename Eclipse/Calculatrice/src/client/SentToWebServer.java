package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import conf.Conf;

/*__________________________________________________*/
/**
 * @author steven
 *
 */
public class SentToWebServer {
	
	/**
	 * Permet d'envoyer les donnees au serveur Web
	 * @param entier1 : Premier nombre
	 * @param entier2 : Second nombre
	 * @param operation : Operation a effectuer
	 * @return le resultat
	 */
	public String send(double entier1, double entier2, String operation){
		String resultat = null;
		
		// En fonction de l'operation a effectuer, on appel une fonction
		switch (operation) {
		case "Addition":
			resultat = Addition(entier1, entier2);
			break;
			
		case "Soustraction":
			resultat = Soustraction(entier1, entier2);
			break;

		default:
			break;
		}
		
		return resultat;
	}
	
	/**
	 * Envoie d'une addition au serveur
	 * @param entier1 : Premier nombre
	 * @param entier2 : Second nombre
	 * @return le resultat emis par le serveur
	 */
	private String Addition(double entier1, double entier2){
		try{
			// Parametre emis en POST
			String valeurs= "nb1="+entier1+"&nb2="+entier2;
			// Ouverture de la socket
			Socket socket = new Socket(InetAddress.getLocalHost(), Conf.PORTWEB);
			//Ouverture du buffer d'envoie
			BufferedWriter ecriture = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
			
			//Preparation, et envoie la requete stockee dans un buffer puis forcee en ecriture pour eviter les erreurs
			String buffer = "";
			buffer += "POST " + Conf.PATHADDITION + " HTTP/1.1\r\n";
			buffer += "Host: " + Conf.PATHHOTE + "\r\n";
			
			buffer += "Content-Length: " + valeurs.length() + "\r\n";
			buffer += "Content-Type: application/x-www-form-urlencoded\r\n";
			buffer += "\r\n";
			buffer += valeurs + "\r\n";
			buffer += "\r\n";
			
			ecriture.write(buffer);
			ecriture.flush();
			
			//Lecture du retour du serveur, avec "~@~" entre chaque parametres pour le protocole
			BufferedReader lecture = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;
			String result = null;
			while ((line = lecture.readLine()) != null){
				result += line;
			}
			String[] resultat = result.split("~@~");
			
			//Fermeture de la connexion
			ecriture.close();
			lecture.close();
			socket.close();
			
			// envoie du resultat
			return resultat[2];
		}catch (IOException io){
			System.err.println("Erreur input/output (addition) : " + io);
		}catch (Exception e){
			System.err.println("Erreur autre (client - addition)  : " + e);
		}
		
		return null;
	}
	
	/**
	 * Envoie d'une soustraction au serveur
	 * @param entier1 : Premier nombre
	 * @param entier2 : Second nombre
	 * @return le resultat emis par le serveur
	 */
	private String Soustraction(double entier1, double entier2){
		try{
			// Parametre emis en GET
			String valeurs= "?nb1=" + entier1 + "&nb2=" + entier2;
			// Ouverture de la socket
			Socket socket = new Socket(InetAddress.getLocalHost(), Conf.PORTWEB);
			//Ouverture du buffer d'envoie
			PrintWriter output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			
			//Preparation, et envoie la requete stockee dans un buffer puis forcee en ecriture pour eviter les erreurs
			String buffer = "";
			buffer += "GET " + Conf.PATHSOUSTRACTION + valeurs + " HTTP/1.1 \r\n";
			buffer += "Host: " + Conf.PATHHOTE + "\r\n";
			buffer += "\r\n";
			output.println(buffer);
			
			output.flush();

			//Lecture du retour du serveur, avec "~@~" entre chaque parametres pour le protocole
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String line;
			String result = null;
			while ((line = input.readLine()) != null){
				result += line;
			}
			String[] resultat = result.split("~@~");
			
			//Fermeture de la connexion
			output.close();
			input.close();
			socket.close();
			
			// envoie du resultat
			return resultat[2];
		}catch (IOException io){
			System.err.println("Erreur input/output (soustraction) : " + io);
		}catch (Exception e){
			System.err.println("Erreur autre (client - soustraction) : " + e);
		}
		
		return null;
	}
}
