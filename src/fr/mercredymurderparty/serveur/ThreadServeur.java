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
	
	// D�finir le serveur
	private CoeurServeur server;
	
	// D�finir le socket
	private Socket socket;
	
	// Identifiant du thread serveur
	private int threadId = -1;
	
	// Flux de donn�es en entr�e
	private ObjectInputStream lecture;
	
	// Flux de donn�es en sortie
	private ObjectOutputStream ecriture;

	
// ----- CONSTRUCTEUR ----- //
	
	/**
	 * Constructeur de la classe ThreadServeur
	 * @param _server TODO � d�crire
	 * @param _socket TODO � d�crire
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
	 * Proc�dure qui publie un message
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
				// Attendre jusqu'� le thread s'arrete
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
	 * TODO � d�crire
	 * @return TODO � d�crire
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
	 * Proc�dure qui ouvre un flux de donn�es en lecture et ecriture
	 * @throws IOException TODO � d�crire
	 */
	public final void ouvrir() throws IOException 
	{
		ecriture = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		ecriture.flush(); // ESSENTIEL, SINON �A PLANTE
		System.out.println("OUTPUT SERVEUR CREE");
		lecture = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		System.out.println("INPUT SERVEUR CREE");
	}

	/**
	 * Proc�dure qui ferme le flux de donn�es en lecture et ecriture en cours
	 * @throws IOException TODO � d�crire
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
	 * Retourne l'id du Client auquel est li� le thread
	 * @return 
	 * @return l'id du Client auquel est li� le thread
	 */
	public int getIdClient()
	{
		return socket.getPort();
	}
}
