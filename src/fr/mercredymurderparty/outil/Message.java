package fr.mercredymurderparty.outil;

import java.io.Serializable;

public class Message implements Serializable
{
	// ----- CONSTANTES ----- //
	public static int MESSAGE_TCHAT = 1;
	public static int MAJ_HEURE = 2;
	public static int REQUETE_SQL = 3;
	public static int INSTRUCTION_SQL = 4;
	public static int REPONSE_SQL = 5;
	public static int ERREUR_SQL = 6;
	
	// ----- ATTRIBUTS ----- //
	
	// --- générique --- //
	private int idClient; // identifiant du client qui a émi le message
	private int type; // le type du message ( prend la valeur d'une des constantes ci-dessus
	private Object object; // l'objet qu'envoi le message
	
	// --- dans le cas d'une requète --- //
	private String champ;
	
	// ----- CONSTRUCTEUR ----- //
	
	/**
	 * 
	 * @param _idClient identifiant du client qui a émi le message
	 * @param _type le type du message ( prend la valeur d'une des constantes ci-dessus
	 * @param _objet l'objet qu'envoi le message
	 */
	public Message (int _idClient, int _type, Object _object)
	{
		idClient = _idClient;
		type = _type;
		object = _object;
	}
	
	/**
	 * 
	 * @param _idClient identifiant du client qui a émi le message
	 * @param _type le type du message ( prend la valeur d'une des constantes ci-dessus
	 * @param _objet l'objet qu'envoi le message
	 * @param _champ la valeur du champ à retourner
	 */
	public Message (int _idClient, int _type, Object _object, String _champ)
	{
		idClient = _idClient;
		type = _type;
		object = _object;
		champ = _champ;
	}
	
	// ----- Getters & Setters ----- //

	
	/**
	 * Getter de l'identifiant du client
	 * @return l'identifiant du client
	 */
	public int getIdClient()
	{
		return idClient;
	}

	/**
	 * Getter du type du message
	 * @return le type du message
	 */
	public int getType()
	{
		return type;
	}

	/**
	 * Getter de l'object du message 
	 * @return l'object du message 
	 */
	public Object getObject()
	{
		return object;
	}

	public String getChamp()
	{
		return champ;
	}

	public void setChamp(String champ)
	{
		this.champ = champ;
	}
	
	
}
