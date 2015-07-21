#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>
#include <netdb.h>
#include <sys/socket.h>
#include <errno.h>


/***********************************************************************
 * GRANDET Jeremy - MARTINS Steven
 **********************************************************************/
 
/**
 * Constantes
 */
int PORT_SERVEUR_JAVA=5558;
int PORT_SERVEUR_WEB = 80;

char * LOCALHOST="localhost";
char * CHEMIN_SCRIPT_PHP_ADDITION="/~addition.php";
char * CHEMIN_SCRIPT_PHP_SOUSTRACTION="/~soustraction.php";

/**
 * Entree du programme
 */
int main(int argc, char *argv[])
{
	char bufferReception[200], buffer[200], convertionPOST[10];
	char valeur1[20], valeur2[20], operation[5], suite;
	struct sockaddr_in addr;
	struct hostent *entree;
	int sock;
	
    
	while(1)
	{	
		/**
		 *	Affichage et recuperation des valeurs
		 */
		printf("\t Bienvenue sur le client C, veuillez suivre les instructions affichées à l'écran \n");
		printf("Renseigner votre première opérande : ");
		scanf("%s%*c", valeur1);
		
		/**
		 *	Test de la valeur ASCII de ce qui est saisi par l'utilisateur, pour controler s'il renseigne bien un chiffre. 
		 */
		if (valeur1[0] > 57 || valeur1[0] < 48 )
		{
			printf("Vous n'avez pas saisi un nombre!\n\tTerminaison du programme, veuillez réesayer.\n");
			exit(-1);
		}		
		
		printf("Renseigner l'opération ( /,*,-,+, ) : ");
		scanf("%s%*c", operation);
		printf("Renseigner votre deuxième opérande : ");
		scanf("%s%*c", valeur2);
		
		if (valeur2[0] > 57 || valeur2[0] < 48 )
		{
			printf("Vous n'avez pas saisi un nombre!\n\tTerminaison du programme, veuillez réesayer.\n");
			exit(-1);
		}		
		
		
		/**
		 *	Filtrage de l'operation pour configurer le bon port (java ou web)
		 */
		if(strcmp(operation,"*") == 0 || strcmp(operation,"/") == 0)
		{
			addr.sin_port=htons(PORT_SERVEUR_JAVA);
		}
		else
		if(strcmp(operation,"+") == 0 || strcmp(operation,"-") == 0)
		{
			addr.sin_port=htons(PORT_SERVEUR_WEB);
		}else
		{
			printf("Vous avez saisi un mauvais opérateur. Rappel ( /,*,-,+, )\n\tTerminaison du programme, veuillez réesayer.\n");
			exit(-1);
			
		}
		
		addr.sin_family=AF_INET;
		entree=(struct hostent *)gethostbyname(LOCALHOST);
		bcopy((char *)entree->h_addr,(char *)&addr.sin_addr,entree->h_length);
	
		/**
		 *	Declaration de la socket 
		 */
		sock=socket(AF_INET, SOCK_STREAM,0);
	
		/**
		 *	Connection 
		 */
		if(connect(sock, (struct sockaddr*)&addr, sizeof(struct sockaddr_in)) == -1)
		{
			perror("connection");
			exit(errno);
		}
		
		/**
		 *	Met a zero les octets des buffers 
		 */
		bzero(buffer, sizeof(buffer));
		bzero(bufferReception, sizeof(bufferReception));
		
		/**
		 *	Filtrage de l'operation pour la dispatcher au serveur java ou au serveur web 
		 */
		if(strcmp(operation,"*") == 0 || strcmp(operation,"/") == 0)
		{
			strcpy(buffer, valeur1);
			strcat(buffer, "~");
			strcat(buffer, valeur2);
			strcat(buffer, "~");
			strcat(buffer, operation);
			printf("Récapitulatif : %s %s %s\n", valeur1,operation,valeur2);
			buffer[strlen(buffer)]='\n';
			printf("Requête envoyée au serveur Java, nom : %s, port : %d\n",LOCALHOST,PORT_SERVEUR_JAVA);
		}
		else
		/**
		 *	Requete GET 
		 */
		if(strcmp(operation,"-") == 0)
		{
			strcpy(buffer, "GET ");
			strcat(buffer, CHEMIN_SCRIPT_PHP_SOUSTRACTION);
			strcat(buffer, "?nb1=");
			strcat(buffer, valeur1);
			strcat(buffer, "&nb2=");
			strcat(buffer, valeur2);
			strcat(buffer, "HTTP/1.1\r\nHost:");
			strcat(buffer, LOCALHOST);
			strcat(buffer, "\r\n");
			strcat(buffer, "\r\n");
			printf("Récapitulatif : %s %s %s\n", valeur1,operation,valeur2);
			printf("Requête envoyée au serveur Web, port : %d\n",PORT_SERVEUR_WEB);
		}
		else 
		/**
		 *	Requete POST 
		 */
		if(strcmp(operation,"+") == 0)
		{
			strcpy(buffer, "POST ");
			strcat(buffer, CHEMIN_SCRIPT_PHP_ADDITION);
			strcat(buffer, " HTTP/1.1\r\nHost: ");
			strcat(buffer, LOCALHOST);
			strcat(buffer, "\r\nContent-length:");
			sprintf(convertionPOST, "%d", 11+strlen(valeur1)+strlen(valeur2));
			strcat(buffer, convertionPOST);
			strcat(buffer, "\r\nContent-type: application/x-www-form-urlencoded\r\n\r\nnb1=");
			strcat(buffer, valeur1);
			strcat(buffer, " &nb2=");
			strcat(buffer, valeur2);
			strcat(buffer, "\r\n");
			strcat(buffer, "\r\n");
			printf("Récapitulatif : %s %s %s\n", valeur1,operation,valeur2);
			printf("Requête envoyée au serveur Web, port : %d\n",PORT_SERVEUR_WEB);
		}
		
		/**
		 *	Envoi de l'operation au serveur 
		 */
		if (send(sock,buffer, strlen(buffer)+1, 0) == -1)
		{
			perror("send");
			exit(errno);
			printf("erreur pendant l'envoi au serveur"); 
		}
		
		/**
		 *	Reception de l'operation
		 */
		if (recv(sock, bufferReception, sizeof(bufferReception),0) == -1)
		{ 
			perror("recv");
			exit(errno);
			printf("erreur pendant la récéption du résultat"); 
		}		
		printf("Résultat de votre calcul : %s\n", bufferReception);
		
		/**
		 *	Proposition de continuation au client pour passer a une autre operation
		 */
		printf("Voulez-vous soumettre une autre opération? (O/n) : ");
		scanf("%c%*c", &suite);
		while (suite != 'n' && suite != 'o')
		{
			printf("Voulez-vous soumettre une autre opération? (O/n) : ");
			scanf("%c%*c", &suite);
		}
		if(suite == 'n')
			break;	
	}
	
	/**
	 *	Termiinaison du programme et fermeture de la socket
	 */
	printf("\t Programme terminé avec succès. Merci de votre visite. \n");
	close(sock);
	return 0;	
}

