package fr.mercredymurderparty.client;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;

import fr.mercredymurderparty.outil.Message;

/**
 * Classe ThreadClient
 *
 */
public class ThreadClient extends Thread 
{

// ----- ATTRIBUTS ----- //
	
	// D�finir le socket
	private Socket socket;
	
	// D�finir le client
	private CoeurClient client;

	// Flux de donn�es en entr�e
	private ObjectOutputStream sortie;
	// Flux de donn�es en entr�e
	private ObjectInputStream entree;
	
	
// ----- CONSTRUCTEUR ----- //

	/**
	 * Constructeur de la classe ThreadClient
	 * @param _client le client
	 * @param _socket la socket de connexion
	 */
	public ThreadClient(CoeurClient _client, Socket _socket) 
	{
		client = _client;
		socket = _socket;
		ouvrir();
		start();
	}
	
	
// ----- METHODES ----- //

	/**
	 * Proc�dure qui ouvre un nouveau flux de donn�es en entr�e
	 */
	public final void ouvrir()
	{
		try
		{
			sortie = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("OUTPUT CLIENT CREE");
			entree = new ObjectInputStream(socket.getInputStream());
			System.out.println("INPUT CLIENT CREE");

		} 
		catch (Exception ioe) 
		{

			System.out.println(ioe.getMessage());
			System.out.println("Erreur de r�cup�ration du flux d'entr�e: " + ioe);
			client.arreter();
		}
	}

	/**
	 * Proc�dure qui ferme le flux de donn�es en entr�e en cours
	 */
	public final void fermer() 
	{
		try 
		{
			if (entree != null)
			{
				entree.close();
			}
		} 
		catch (IOException ioe) 
		{
			System.out.println("Erreur de fermeture du flux d'entr�e: " + ioe);
		}
	}

	/**
	 * Executer le thread client
	 */
	public final void run() 
	{
		while (!Thread.currentThread().isInterrupted()) 
		{
			try 
			{
				/*
				 *	Diff�rents messages peuvent �tre re�u. Ceux qui commencent par MES sont � afficher dans le tchat
				 *	Ceux qui commencent par HEU sont � afficher dans le champ heure.
				 *	D'autre type de message pourront �tre cr�� � l'avenir, le but est de pouvoir faire transiter toutes les infos
				 *	textuelles par un seul port ...  
				 */
				Message message = (Message)entree.readObject();
				if (message.getType() == Message.MESSAGE_TCHAT)
				{
					client.afficherMessage((String)message.getObject());
				}
				else if (message.getType() == Message.MAJ_HEURE)
				{
					//System.out.println(message.substring(3));
					client.majHeure((String)message.getObject());	
				}
				else if (message.getType() == Message.REPONSE_SQL)
				{
					//System.out.println("R�ception du r�sultat : "+ (String)message.getObject());
					client.setResultatSQL(message.getObject());
				}
				else if (message.getType() == Message.ERREUR_SQL)
				{
					client.getLabelMessageErreur().setText((String)message.getObject());
					client.setResultatNonRecu(false);
				}
			} 
			catch (IOException ioe) 
			{
				client.arreter();
			} catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}


	public ObjectOutputStream getSortie()
	{
		return sortie;
	}


	public void setSortie(ObjectOutputStream sortie)
	{
		this.sortie = sortie;
	}


	public ObjectInputStream getEntree()
	{
		return entree;
	}


	public void setEntree(ObjectInputStream entree)
	{
		this.entree = entree;
	}
	
	
}
