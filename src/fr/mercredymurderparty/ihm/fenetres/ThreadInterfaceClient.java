package fr.mercredymurderparty.ihm.fenetres;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.Socket;

import fr.mercredymurderparty.client.CoeurClient;
import fr.mercredymurderparty.outil.Message;

/**
 * Classe ThreadClient
 *
 */
public class ThreadInterfaceClient extends Thread 
{

// ----- ATTRIBUTS ----- //
	
	// D�finir le socket
	private Socket socket;
	
	// D�finir le client
	private CoeurClient client;
	
	// Flux de donn�es en entr�e
	private ObjectInputStream entree;
	
	
// ----- CONSTRUCTEUR ----- //

	/**
	 * Constructeur de la classe ThreadClient
	 * @param _client le client
	 * @param _socket la socket de connexion
	 */
	public ThreadInterfaceClient(CoeurClient _client, Socket _socket) 
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
			entree = new ObjectInputStream(socket.getInputStream());
		} 
		catch (IOException ioe) 
		{
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
				Message message = (Message)entree.readObject();
				client.afficherMessage((String)message.getObject());
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
}
