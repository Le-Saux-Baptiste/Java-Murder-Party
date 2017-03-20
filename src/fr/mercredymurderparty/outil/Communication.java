package fr.mercredymurderparty.outil;

import java.io.ObjectOutputStream;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import fr.mercredymurderparty.client.CoeurClient;

/*
 * Classe outil ayant pour objectif de rendre g�n�rique l'envoi d'un message depuis le client vers le serveur
 */

public class Communication
{
	
	// ----- ATTRIBUTS ----- //

	private CoeurClient coeurClient; // n�c�ssaire pour effectuer une connexion
//	private Message messageEnvoye; // Le message � envoy�
	private ObjectOutputStream sortie; // La sortie
	private Object object; // C'est syst�me D mais je n'ai pas eu le choix : il fallait rendre tout �a g�n�rique.
	private String classeDeLObject; // Idem
	
	
	// ----- CONSTRUCTEUR ----- //
	
	public Communication ()
	{

	}
	
	
	public Communication (Object _object, String _classeDeLObject, CoeurClient _coeurClient)
	{
		object = _object;
		classeDeLObject = _classeDeLObject;
		coeurClient = _coeurClient;
		sortie = coeurClient.getClient().getSortie();
		// Ajout d'un �ventuel pointeur vers un �l�ment de l'interface qui serait modifi� apr�s envoi d'info
		if (classeDeLObject.equals("JTextArea"))
		{
			coeurClient.setChatResults((JTextArea)object);
		}
		else if (classeDeLObject.equals("JLabel"))
		{
			coeurClient.setCompteur((JLabel)object);
		}
	}
	
	/**
	 * D�marre une session
	 * @param _estJoueur
	 */
	final public void demarrerSession()
	{

		// �crit ce qui a �t� envoy� sur le serveur dans la JTexteArea (historique)
		coeurClient.setSortie(sortie);
	}
	
	/**
	 * Proc�dure qui va fermer la session
	 */
	final public void arreterSession()
	{
		// Arreter le coeur client, socket & thread associ�
		try
		{
			coeurClient.arreter();
		}
		catch (Exception e)
		{
			System.err.println("Erreur lors de la fermeture: " + e.getMessage());
		}
	}

	// ----- Getters & Setters ----- //
	public CoeurClient getCoeurClient()
	{
		return coeurClient;
	}

	public void setCoeurClient(CoeurClient coeurClient)
	{
		this.coeurClient = coeurClient;
	}

	/*
	public Message getMessageEnvoye()
	{
		return messageEnvoye;
	}

	public void setMessageEnvoye(Message messageEnvoye)
	{
		this.messageEnvoye = messageEnvoye;
	}
	*/
	public ObjectOutputStream getSortie()
	{
		return sortie;
	}

	public void setSortie(ObjectOutputStream sortie)
	{
		this.sortie = sortie;
	}

	public Object getObject()
	{
		return object;
	}

	public void setObject(Object object)
	{
		this.object = object;
	}
	
	
}
