package fr.mercredymurderparty.client;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JLabel;

import fr.mercredymurderparty.outil.BaseDeDonnees;
import fr.mercredymurderparty.outil.Fonctions;

public class Personnage 
{
	
// ----- ATTRIBUTS ----- //
	
	// ----- VARIABLES ----- //
	private Integer id = -1;
	private String login = "Invité";
	
	// ----- CLASSES ----- //
	private Fonctions fct;
	// ----- MODELE ----- //
	private CoeurClient coeurClient;
	
	
// ----- CONSTRUCTEUR ----- //
	
	public Personnage(CoeurClient _coeurClient)
	{
		coeurClient = _coeurClient;
		setFct(new Fonctions());
	}
	
	
// ----- METHODES ----- //
	
	/**
	 * Fonction qui vérifie le login et mot de passe
	 * @param _login Identifiant du joueur
	 * @param _mdp Mot de passe du joueur
	 * @param _labelMessageErreur 
	 * @return Vrai ou faux selon si l'utilisateur existe dans la bdd
	 */
	public final boolean connexion(String _login, char[] _mdp, JLabel _labelMessageErreur) 
	{
		// Convertir le champs password d'array en String
		String mdp = getFct().convertirCharArrayVersString(_mdp);
		
		// Si les champs login et mdp sont pas vides, on continue...
		if (_login.length() > 0 && mdp.length() > 0) 
		{
			int cpt;
			try 
			{
				// Appeler la classe SQLite
			//	BaseDeDonnees sql = new BaseDeDonnees();
				
				// Vérifier que le personnage existe
			//	ResultSet rs = sql.executerRequete("SELECT count(id) cpt FROM personnage WHERE login = '" + _login + "' AND mdp = '" + mdp + "'");
				BaseDeDonnees.envoyerRequete("SELECT count(id) cpt FROM personnage WHERE login = '" + _login + "' AND mdp = '" + mdp + "'", coeurClient, "cpt");
				cpt = (Integer)coeurClient.getResultatSQL();
			//	rs.next();
				if  (cpt == 1) 
				{
		//			sql.fermerConnexion();
					return true; // Le personnage existe
				} 
				else 
				{
					_labelMessageErreur.setText("<html>Le nom d'utilisateur saisie et / ou<br/>le mot de passe n'est pas valide !</html>");
					_labelMessageErreur.setVisible(true);
				//	sql.fermerConnexion();
					return false; // Le personnage n'existe pas
				}
			} 
			catch (Exception e) 
			{
				_labelMessageErreur.setText("<html>Un problème est survenue au niveau<br/>de la base de données !</html>");
				_labelMessageErreur.setVisible(true);
				System.out.println(e.toString());
				return false;
			}
		} 
		else 
		{
			_labelMessageErreur.setText("<html>Il faut saisir un nom d'utilisateur et<br/>un mot de passe !</html>");
			_labelMessageErreur.setVisible(true);
			return false;
		}
	}
	
	/**
	 * Fonction qui retourner l'identifiant du personnage joueur
	 * @param _login Login pour retrouver le personnage dans la bdd
	 * @return Un entier representant l'id du joueur
	 */
	public final int recupererIdentifiant(String _login)
	{
		
		// Identifiant du personnage
		int idPerso = -1;
		
		try 
		{
			// Verifier si on a un login
			if (_login != null) 
			{
				// Recuperer l'id du personnage joueur
				BaseDeDonnees.envoyerRequete("SELECT id FROM personnage WHERE login = '" + _login + "'", coeurClient, "id");
				idPerso = (Integer)coeurClient.getResultatSQL();
				
				// Retourner l'id
				return idPerso;
			} 
			else 
			{
				return -1;
			}
		} 
		catch (Exception e) 
		{
			e.getStackTrace();
			return -1;
		}
	}
	
	/**
	 * Fonction qui retourner le temps de connexion qu'il reste au joueur
	 * @param _login Login pour retrouver le personnage dans la bdd
	 * @return Un entier representant le temps restant
	 */
	public final int recupererTemps(String _login)
	{
		// On verifie que le personnage existe bien
		int identifiant = recupererIdentifiant(_login);
		int temps = -1; // -1 indique que le temps n'a pas pu être trouvé
		
		if (identifiant != -1)
		{
				// Recuperer le temps du joueur
				BaseDeDonnees.envoyerRequete("SELECT temps FROM personnage WHERE id = '" + identifiant + "'", coeurClient, "temps");
				temps = (Integer)coeurClient.getResultatSQL();
		
		}
		
		return temps;
	}

	
// ----- GETTERS & SETTERS ----- //	

	/**
	 * Getter de la classe Fonctions
	 * @return Appel les méthodes de la classe
	 */
	public Fonctions getFct() 
	{
		return fct;
	}

	/**
	 * Setter de la classe fonctions
	 * @param Instancier la classe
	 */
	public void setFct(Fonctions fct) 
	{
		this.fct = fct;
	}


	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}
}
