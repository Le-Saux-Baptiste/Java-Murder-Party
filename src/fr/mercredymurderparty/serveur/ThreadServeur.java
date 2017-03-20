package fr.mercredymurderparty.serveur;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

import fr.mercredymurderparty.outil.Message;

public class ThreadServeur extends Thread 
{
	
// ----- ATTRIBUTS ----- //
	
	// Définir le serveur
	private CoeurServeur server;
	
	// Définir le socket
	private Socket socket;
	
	// Identifiant du thread serveur
	private int threadId = -1;
	
	// Flux de données en entrée
	private ObjectInputStream lecture;
	
	// Flux de données en sortie
	private ObjectOutputStream ecriture;

	
// ----- CONSTRUCTEUR ----- //
	
	/**
	 * Constructeur de la classe ThreadServeur
	 * @param _server TODO à décrire
	 * @param _socket TODO à décrire
	 */
	public ThreadServeur(CoeurServeur _server, Socket _socket) 
	{
		super();
		server = _server;
		socket = _socket;
		threadId = socket.getPort();
	}
	
	
// ----- METHODES ----- //	

	/**
	 * Procédure qui publie un message
	 * @param _msg Contenu du message
	 */
	public synchronized final void envoyerMessage(Message _msg)
	{
		try 
		{
			ecriture.writeObject(_msg);
			ecriture.flush();
		} 
		catch (IOException ioe) 
		{
			System.out.println(threadId + " Erreur d'envoi: " + ioe.getMessage());
			server.supprimer(threadId);
			
			// Interrompre le thread serveur
			this.interrupt();
			try 
			{
				// Attendre jusqu'à le thread s'arrete
				this.join();
			} 
			catch (InterruptedException ex) 
			{
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	/**
	 * TODO à décrire
	 * @return TODO à décrire
	 */
	public final int recupererId() 
	{
		return threadId;
	}

	/**
	 * Objet runnable de la classe ThreadServeur
	 */
	public final void run() 
	{
		System.out.println("Server Thread " + threadId + " en marche.");
		while (!Thread.currentThread().isInterrupted()) 
		{
			try 
			{
				server.traiterMessage(threadId, (Message)lecture.readObject());
			} 
			catch (IOException ioe) 
			{
				System.out.println("Erreur de lecture: " + ioe.getMessage());
				server.supprimer(threadId);
				
				// Interrompre le thread serveur
				//this.interrupt();
			} catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Procédure qui ouvre un flux de données en lecture et ecriture
	 * @throws IOException TODO à décrire
	 */
	public final void ouvrir() throws IOException 
	{
		ecriture = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		ecriture.flush(); // ESSENTIEL, SINON çA PLANTE
		System.out.println("OUTPUT SERVEUR CREE");
		lecture = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		System.out.println("INPUT SERVEUR CREE");
	}

	/**
	 * Procédure qui ferme le flux de données en lecture et ecriture en cours
	 * @throws IOException TODO à décrire
	 */
	public final void fermer() throws IOException 
	{
		if (socket != null)
		{
			socket.close();
		}
		
		if (lecture != null)
		{
			lecture.close();
		}
		
		if (ecriture != null)
		{
			ecriture.close();
		}
	}
	
	// ----- Getters & Setter ----- //
	
	/**
	 * Retourne l'id du Client auquel est lié le thread
	 * @return 
	 * @return l'id du Client auquel est lié le thread
	 */
	public int getIdClient()
	{
		return socket.getPort();
	}
}
