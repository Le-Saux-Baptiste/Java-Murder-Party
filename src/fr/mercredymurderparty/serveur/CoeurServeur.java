package fr.mercredymurderparty.serveur;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.mercredymurderparty.outil.BaseDeDonnees;
import fr.mercredymurderparty.outil.Message;


/**
 * Classe CoeurServeur
 *
 */
public class CoeurServeur implements Runnable 
{
	
// ----- ATTRIBUTS ----- //
	
	// Liste des clients
	private ThreadServeur[] clients = new ThreadServeur[50];
	
	// Définir le serveur
	private ServerSocket server;
	
	// Définir le thread
	private Thread thread;
	
	// Compter le nombre de clients
	private int cptClients;
	
	// Le thread qui calcule le temps restant
	private CompteurTempsGeneral tempsGlobalRestant;
	
	// Nom de la base de donnée/partie en cours
	private String nomBDD;
	
	private boolean partieLancee;

	
// ----- CONSTRUCTEUR ----- //
	
	/**
	 * Constructeur de la classe CoeurServeur
	 * @param _port Port de la machine
	 */
	public CoeurServeur(int _port) 
	{
		try 
		{
			System.out.println("Liaison au port " + _port + ", veuillez patientez...");
			server = new ServerSocket(_port);
			System.out.println("Le serveur a démarré: " + server);
			demarrer();
	//		tempsGlobalRestant = new CompteurTempsGeneral(4968, this); // FIXME : passera en dynamique très prochainement, pour l'instant initialisé à 1h22.
			partieLancee = false;
		} 
		catch (IOException ioe) 
		{
			System.out.println("Impossible de se lier au port " + _port + ": " + ioe.getMessage());
		}
	}
	
	
// ----- METHODES ----- //	

	/**
	 * @see java.lang.Runnable#run()
	 */
	public final void run()
	{
		// Executer jusqu'à ce que le thread soit interrompu
		while (!Thread.currentThread().isInterrupted()) 
		{
			try 
			{
				System.out.println("Attente d'un client...");
				ajouterThread(server.accept());
			} 
			catch (IOException ioe) 
			{
				System.out.println("Erreur de serveur accept: " + ioe);
				arreter();
			}
		}
	}

	/**
	 * Procédure qui démarre un nouveau thread serveur
	 */
	public final void demarrer()
	{
		if (thread == null)
		{
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * Procédure qui arête le thread serveur en cours
	 */
	public final void arreter()
	{
		if (thread != null)
		{
			// Interrompre le thread
			thread.interrupt();
			try 
			{
				// Attendre jusqu'à le thread s'arrete
				thread.join();
			} 
			catch (InterruptedException ex) 
			{
				System.out.println("Erreur lors de l'interruption du thread... ");
				System.exit(1);
			}
			finally 
			{
				// Detruire l'objet thread
				thread = null;
			}
		}
	}

	/**
	 * Rechercher un client connecté sur le serveur actuel
	 * @param _id Identifiant du client
	 * @return TODO à décrire
	 */
	private int rechercherClient(int _id) 
	{
		for (int i = 0; i < cptClients; i++)
		{
			if (clients[i].recupererId() == _id)
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * Procédure qui syncronise le message posté à tous les clients connectés sur le serveur
	 * @param _id Identifiant du client
	 * @param _message Message du client
	 */
	public final synchronized void traiterMessage(int _id, Message _message) 
	{
		if (_message.equals(".bye")) 
		{
			/*
			clients[rechercherClient(_id)].envoyerMessage(".bye");
			supprimer(_id);*/
		} 
		else 
		{
			if (_message.getType() == Message.REQUETE_SQL) // UNICAST
			{
				//ResultSet rs = effectuerRequete((String)_message.getObject());
				System.out.println("Réception d'une requète SQL");
				Object resultat = null;
				Message messageRetour;
				if (partieLancee)
				{
					System.out.println("Partie est lancée donc Faisable");
					BaseDeDonnees sql = new BaseDeDonnees(this);
					try
					{
						// Recuperer l'id du personnage joueur
						ResultSet rs = sql.executerRequete((String)_message.getObject());
						rs.next();
						resultat = rs.getObject(_message.getChamp());
						
						// Fermer la connexion SQLite
						sql.fermerConnexion();
					} 
					catch (SQLException e)
					{
						System.err.println("Erreur lors de l'excution de la requête");
					}
					finally
					{
						// Fermer la connexion SQLite
						sql.fermerConnexion();
					}
				//	System.out.println("RESULTAT : "+resultat);
					messageRetour = new Message(server.getLocalPort(), Message.REPONSE_SQL, resultat);
					System.out.println("Réponse du serveur au client "+ _message.getIdClient());
				}
				else
				{
					System.out.println("Partie est pas lancée donc pas Faisable");
					messageRetour = new Message(server.getLocalPort(), Message.ERREUR_SQL, "Pas de partie en cours");
					System.out.println("Réponse du serveur au client "+ _message.getIdClient());
				}
				for (int i = 0; i < cptClients; i++)
				{
					if (_message.getIdClient() == clients[i].getIdClient())
					{
						clients[i].envoyerMessage(messageRetour);
					}
				}
			}
			else if (_message.getType() == Message.INSTRUCTION_SQL) // Cas particulier pour les instruction sql autre que les SELECT
			{
				BaseDeDonnees sql = new BaseDeDonnees(this);
				
				try
				{
					// Recuperer l'id du personnage joueur
					sql.executerInstruction((String)_message.getObject());
					// Fermer la connexion SQLite
					sql.fermerConnexion();
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else // BROADCAST
			{
				for (int i = 0; i < cptClients; i++)
				{
					clients[i].envoyerMessage(_message);
				}
			}
		}
	}

	/**
	 * Procédure qui supprime un thread client en cours
	 * @param _id Identifiant du client
	 */
	public final synchronized void supprimer(int _id) 
	{
		int pos = rechercherClient(_id);
		
		if (pos >= 0) 
		{
			ThreadServeur threadClientATerminer = clients[pos];
			
			System.out.println("Suppression du thread client " + _id + " à " + pos);
			
			if (pos < cptClients - 1) 
			{
				for (int i = pos + 1; i < cptClients; i++)
				{
					clients[i - 1] = clients[i];
				}
			}
			
			cptClients = cptClients - 1;
			
			if (threadClientATerminer.isAlive()) 
			{
				try 
				{
					threadClientATerminer.fermer();
				} 
				catch (IOException ioe) 
				{
					System.out.println("Erreur lors de la fermeture du thread: " + ioe);
				}
				
				threadClientATerminer.interrupt();
				threadClientATerminer = null;
			}
		}
	}

	/**
	 * Procédure qui ajoute un nouveau thread client à la liste
	 * @param _socket Socket
	 */
	private void ajouterThread(Socket _socket) 
	{
		if (cptClients < clients.length) 
		{
			System.out.println("Client accepté: " + _socket);
			clients[cptClients] = new ThreadServeur(this, _socket);
			
			try 
			{
				clients[cptClients].ouvrir();
				clients[cptClients].start();
				cptClients = cptClients + 1;
			} 
			catch (IOException ioe) 
			{
				System.out.println("Erreur lors de l'ouverture du thread: " + ioe);
			}
		} 
		else 
		{
			System.out.println("Client refusé: maximum " + clients.length + " atteint.");
		}
	}
	
	/**
	 * Effectue une requète en BDD pour le compte d'un client
	 * @return 
	 */
	public ResultSet effectuerRequete(String _sql)
	{
		// Appeler la classe SQLite
		BaseDeDonnees sql = new BaseDeDonnees(this);			
		try 
		{
				// Recuperer l'id du personnage joueur
				ResultSet rs = sql.executerRequete(_sql);
				rs.next();
				
				// Fermer la connexion SQLite
				sql.fermerConnexion();
				
				// Retourner l'id
				return rs;
		} 
		catch (Exception e) 
		{
			e.getStackTrace();
			return null;
		}
	}

	// ----- Getters & Setters ----- //
	

	public void setServer(ServerSocket server)
	{
		this.server = server;
	}

	
	public void setTempsGlobalRestant(CompteurTempsGeneral tempsGlobalRestant)
	{
		this.tempsGlobalRestant = tempsGlobalRestant;
	}


	public CompteurTempsGeneral getTempsGlobalRestant()
	{
		return tempsGlobalRestant;
	}

	public String getNomBDD()
	{
		return nomBDD;
	}


	public void setNomBDD(String nomBDD)
	{
		this.nomBDD = nomBDD;
	}


	public boolean isPartieLancee()
	{
		return partieLancee;
	}


	public void setPartieLancee(boolean partieLancee)
	{
		this.partieLancee = partieLancee;
	}
	
	
}
