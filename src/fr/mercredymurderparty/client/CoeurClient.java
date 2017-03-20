package fr.mercredymurderparty.client;

import java.io.BufferedReader;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JTextArea;

public class CoeurClient implements Runnable
{
	
// ----- ATTRIBUTS ----- //
	
	// D�finir un socket
	private Socket socket;
	
	// D�finir un thread
	private Thread thread;
	
	// Flux de donn�es en entr�e
	private BufferedReader entree;
	
	// Flux de donn�es en sortie
	private ObjectOutputStream sortie;
	
	// D�finir un thread client
	private ThreadClient client;
	
	// D�finir un JTextArea pour le chat en interface
	private JTextArea chatResults;
	
	// JLabel du compteur
	private JLabel compteur;
	
	// JLabel des message d'erreur
	private JLabel labelMessageErreur;
	
	private String login;
	
	// Stocke les r�sultats des requ�tes SQL
	private Object resultatSQL;
	
	private boolean resultatNonRecu;
	
	
// ----- CONSTRUCTEUR ----- //
	
	/**
	 * Constructeur de la classe CoeurClient
	 * @param _ip Adresse IP de la machine
	 * @param _port Num�ro de port de la machine
	 * @param _object l'object qui subira des modifications � la r�ception d'info de la part du serveur (JTextField, JTextArea, etc )
	 * @param _classeDeLObject la classe de cette object ( ce n'est pas beau, mais c'est uniquement pour gagner du temps )
	 */
	public CoeurClient(String _ip, int _port, Object _object, String _classeDeLObject) 
	{
		if (_classeDeLObject.equals("JLabel"))
		{
			compteur = (JLabel) _object;
		}
		else if (_classeDeLObject.equals("JTextArea"))
		{
			this.setChatResults((JTextArea)_object);
		}
		// Afficher un message d'attente
		afficherMessage("Etablissement de la connexion, veuillez patienter...");
		
		try 
		{
			// D�finir la socket selon l'ip et le port fournis
			setSocket(new Socket(_ip, _port));
			afficherMessage("Client num : "+socket.getLocalPort());
			// Afficher un message pour dire qu'on s'est bien connect�
			afficherMessage("Connect�: " + getSocket());
			// D�marrer le thread client
			demarrer();
		} 
		catch (UnknownHostException uhe) 
		{
			afficherMessage("H�te inconnu: " + uhe.getMessage());
		} 
		catch (IOException ioe) 
		{
			afficherMessage("Impossible de se connecter au serveur.");
		}
		
	}
	
	/**
	 * Surcharge du Constructeur de la classe CoeurClient
	 * @param _ip Adresse IP de la machine
	 * @param _port Num�ro de port de la machine
	 */
	public CoeurClient(String _ip, int _port) 
	{
		// Afficher un message d'attente
		afficherMessage("Etablissement de la connexion, veuillez patienter...");
		
		try 
		{
			// D�finir la socket selon l'ip et le port fournis
			setSocket(new Socket(_ip, _port));
			afficherMessage("Client num : "+socket.getLocalPort());
			// Afficher un message pour dire qu'on s'est bien connect�
			afficherMessage("Connect�: " + getSocket());
			
			// D�marrer le thread client
			demarrer();
		} 
		catch (UnknownHostException uhe) 
		{
			afficherMessage("H�te inconnu: " + uhe.getMessage());
		} 
		catch (IOException ioe) 
		{
			afficherMessage("Impossible de se connecter au serveur.");
		}
	}
	
	
// ----- METHODES ----- //		

	/**
	 * Executer la classe
	 */
	public final void run()
	{

	}

	/**
	 * Afficher le message de l'utilisateur
	 * @param _message Contenu du message � afficher
	 */
	public final void afficherMessage(String _message)
	{
		// Traitement si commande differente d'un simple message � afficher
		if (_message.equals(".bye")) 
		{
			System.out.println("Appuyez sur la touche ENTRER pour quitter...");
			arreter();
		} 
		else 
		{
			// Afficher le message en console ou interface utilisateur
			if (getChatResults() == null)
			{
				System.out.println(_message);
			}
			else
			{
				//System.out.println("KIKOOLOL");
				getChatResults().append(_message + "\n");
				getChatResults().setCaretPosition(getChatResults().getText().length() - 1);
			}
		}
	}
	
	/**
	 * Mise � jour de l'heure
	 * @param _heure la nouvelle heure
	 */
	public final void majHeure(String _heure)
	{
		if (compteur != null)
		{
			compteur.setText(_heure);
		}
	}

	/**
	 * Proc�dure qui d�marre un nouveau thread client
	 * @throws IOException probl�me d'entr�e sortie
	 */
	public final void demarrer() throws IOException
	{

		// Instancier un nouveau thread si inexistant
		if (thread == null) 
		{
			setClient(new ThreadClient(this, getSocket()));
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * Proc�dure qui arr�te le thread client en cours
	 */
	public final void arreter() 
	{
		if (thread != null) 
		{
			// Interrompre le thread
			thread.interrupt();
			try 
			{
				// Attendre jusqu'� ce que le thread s'arrete
				thread.join();
			} 
			catch (InterruptedException ex) 
			{
				ex.printStackTrace();
				System.exit(1);
			}
			finally 
			{
				// Detruire l'objet thread
				thread = null;
			}
		}
		
		// Fermer les flux de donn�es en entr�e et sortie
		try 
		{
			if (getEntree() != null)
			{
				getEntree().close();
			}
			
			if (getSortie() != null)
			{
				getSortie().close();
			}
			
			if (getSocket() != null)
			{
				getSocket().close();
			}
		} 
		catch (IOException ioe) 
		{
			afficherMessage("Erreur lors de la fermeture du client ...");
		}
		
		// Si le thread client est en vie, on ferme ses objets
		if (getClient() != null) 
		{
			// Fermer le thread client
			getClient().fermer();
			
			// Interrompre le thread client
			getClient().interrupt();
			
			// Disposer le client
			setClient(null);
		}
	}

	
// ----- GETTERS & SETTERS ----- //	
	
	/**
	 * Getter du socket permettant la communication inter processus via tcp / ip
	 * @return Le socket
	 */
	public final Socket getSocket() 
	{
		return socket;
	}

	/**
	 * Setter du socket permettant la communication inter processus via tcp / ip
	 * @param _socket Le socket
	 */
	public final void setSocket(Socket _socket) 
	{
		this.socket = _socket;
	}

	/**
	 * Getter du flux de donn�es en entr�e
	 * @return Un flux de donn�es
	 */
	public final BufferedReader getEntree() 
	{
		return entree;
	}

	/**
	 * Setter du flux de donn�es en sortie
	 * @return le flux de donn�es en sortie
	 */
	public final ObjectOutputStream getSortie()
	{
		return sortie;
	}
	
	/**
	 * Setter du flux de donn�es en entr�e
	 * @param _entree flux de donn�es en entr�e
	 */
	public final void setEntree(BufferedReader _entree) 
	{
		this.entree = _entree;
	}

	/**
	 * Setter du flux de donn�es en sortie
	 * @param _sortie flux de donn�es en sortie
	 */
	public final void setSortie(ObjectOutputStream _sortie) 
	{
		this.sortie = _sortie;
	}

	/**
	 * Getter de la classe ThreadClient
	 * @return la classe ThreadClient
	 */
	public final ThreadClient getClient() 
	{
		return client;
	}

	/**
	 * Setter de la classe ThreadClient
	 * @param _client la classe ThreadClient
	 */
	public final void setClient(ThreadClient _client) 
	{
		this.client = _client;
	}

	/**
	 * Getter des messages du chat
	 * @return Messages du chat
	 */
	final public JTextArea getChatResults() 
	{
		return chatResults;
	}

	/**
	 * Setter des messages du chat
	 * @param _chatResults messages du chat
	 */
	final public void setChatResults(JTextArea _chatResults) 
	{
		this.chatResults = _chatResults;
	}

	/**
	 * Getter du login du joueur connect�
	 * @return Login
	 */
	final public String getLogin() 
	{
		return login;
	}

	/**
	 * Setter du login du joueur connect�
	 * @param _login login du joueur connect�
	 */
	final public void setLogin(String _login) 
	{
		this.login = _login;
	}

	/**
	 * Getter de l'id du client
	 * @return l'id du client
	 */
	public int getIdClient()
	{
		return socket.getLocalPort();
	}

	/**
	 * Getter du compteur ( �l�ment d'interface )
	 * @return le compteur ( �l�ment d'interface )
	 */
	public JLabel getCompteur()
	{
		return compteur;
	}

	/**
	 * Setter du compteur ( �l�ment d'interface )
	 * @param le compteur ( �l�ment d'interface ) 
	 */
	public void setCompteur(JLabel compteur)
	{
		this.compteur = compteur;
	}

	/**
	 * R�cuperer le resultat de la derni�re requ�te SQL
	 * @return le resultat de la derni�re requ�te SQL
	 */
	public Object getResultatSQL()
	{
		return resultatSQL;
	}

	/** 
	 * Mettre a jour resultatSQL
	 * @param string le resultat de la derni�re requ�te SQL
	 */
	public void setResultatSQL(Object resultat)
	{
		this.resultatSQL = resultat;
		this.resultatNonRecu = false;
	}

	public boolean isResultatNonRecu() {
		return resultatNonRecu;
	}

	public void setResultatNonRecu(boolean resultatNonRecu) {
		this.resultatNonRecu = resultatNonRecu;
	}

	public JLabel getLabelMessageErreur()
	{
		return labelMessageErreur;
	}

	public void setLabelMessageErreur(JLabel labelMessageErreur)
	{
		this.labelMessageErreur = labelMessageErreur;
		resultatSQL = 0;
		this.resultatNonRecu = false;
	}
	
	
	
	
}
