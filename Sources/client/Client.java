/*__________________________________________________*/
/*__________________________________________________*/
/**
 * Fichier : Client.java
 *
 * Cr�e le 21 janv. 2015 � 18:35:23
 *
 * Auteur: Steven MARTINS 
 */
package client;

import java.util.Scanner;

/*__________________________________________________*/
/**
 * @author steven
 * 
 */
public class Client
{
	/**
	 * Choix du menu
	 */
	private static double choix = -1;
	/**
	 * Scanner de lecture de l'ecran
	 */
	private static Scanner scanner = null;
	
	
	/**
	 * Permet de lancer l'application client
	 * @param args
	 */
	public static void main(String[] args){
		scanner = new Scanner(System.in);
		
		while(choix != 0){
			menu();
		}
		
		scanner.close();
	}
	
	
	/**
	 * Menu de l'application
	 */
	private static void menu(){
		double entier1, entier2;
		String resultat;
		
		System.out.println("\n\t** MENU **\n");
		System.out.println("[1] Addition");
		System.out.println("[2] Soustraction");
		System.out.println("[3] Multiplication");
		System.out.println("[4] Division");
		System.out.println("[0] Quitter");
		choix = demandeClient(null, "Merci de choisir un chiffre entre 0 et 4");

		switch ((int)choix){
			// Addition
			case 1:
				entier1 = demandeClient("Vous avez choisi une addition\nPremier nombre : ", "Ce n'est pas un nombre");
				entier2 = demandeClient("Second nombre : ", "Ce n'est pas un nombre");
				resultat = new SentToWebServer().send(entier1, entier2, "Addition");
				System.out.println("Le resultat est: " + resultat);
				break;
				
			// Soustraction
			case 2:
				entier1 = demandeClient("Vous avez choisi une soustraction\nPremier nombre : ", "Ce n'est pas un nombre");
				entier2 = demandeClient("Second nombre : ", "Ce n'est pas un nombre");
				resultat = new SentToWebServer().send(entier1, entier2, "Soustraction");
				System.out.println("Le resultat est: " + resultat);
				break;
				
			// Multiplication
			case 3:
				entier1 = demandeClient("Vous avez choisi une multiplication\nPremier nombre : ", "Ce n'est pas un nombre");
				entier2 = demandeClient("Second nombre : ", "Ce n'est pas un nombre");
				resultat = new SendToJavaServer().send(entier1, entier2, "Multiplication");
				System.out.println("Le resultat est: " + resultat);
				break;
				
			// Division
			case 4:
				entier1 = demandeClient("Vous avez choisi une division\nPremier nombre : ", "Ce n'est pas un nombre");
				entier2 = demandeClient("Second nombre : ", "Ce n'est pas un nombre");
				resultat = new SendToJavaServer().send(entier1, entier2, "Division");
				if (resultat.equals("null"))
				{break;}
				System.out.println("Le resultat est: " + resultat);
				break;
				
			// Quitter
			case 0:
				System.out.println("Vous avez quitte le programme");
				break;
				
			// Erreur
			default:
				System.out.println("Veuillez saisir un chiffre entre 0 et 4\n");
				break;
		}
	}

	/**
	 * Permet de demander un nombre au client
	 * @param message : Message a afficher avant la demande
	 * @param erreur : Message a afficher en cas d'erreur
	 * @return : Le nombre saisie par l'utilisateur
	 */
	public static double demandeClient(String message, String erreur){
		String reponse;
		double resultat = -1;

		if(message != null){
			System.out.println(message);
		}
		
		while(resultat == -1){
			try {
		    	reponse = scanner.next();
		        resultat = Double.parseDouble(reponse);
		    } catch (NumberFormatException nfe) {
		        System.out.println(erreur);
		    } catch (NullPointerException nfe) {
		        System.out.println(erreur);
		    }
		}
		
		return resultat;
	}
	

}

/* __________________________________________________ */
/*
 * Fin du fichier Client.java
 * /*__________________________________________________
 */
