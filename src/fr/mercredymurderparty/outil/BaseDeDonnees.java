package fr.mercredymurderparty.outil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fr.mercredymurderparty.client.CoeurClient;
import fr.mercredymurderparty.serveur.CoeurServeur;

/**
 * Connexion � une base de donn�es via driver JDBC
 *
 */
public class BaseDeDonnees 
{
	
// ----- ATTRIBUTS ----- //
	
	private String driver = ""; 			// D�claration du driver JDBC (ex: org.sqlite.JDBC)
	private String url; 					// Lien JDBC de la BDD (ex: jdbc:sqlite:ma_bdd.db)
	private int timeout = 30; 				// Temps avant deconnexion (timeout) de la BDD
	private Connection connexion;			// Savoir si connect� � la BDD
	private Statement etat;					// Etat de la BDD
	private CoeurServeur coeurServeur;					// Le mod�le de l'application serveur

	
// ----- CONSTRUCTEUR ----- //
	
	/**
	 * Constructeur pour une instanciation rapide
	 */
	public BaseDeDonnees() 
	{
		try 
		{
			// Charger fichier de config
			FichierXML xml = new FichierXML("config.xml");
			
			// Initialiser le pilote JDBC avec la base de donn�es SQLite
			init("org.sqlite.JDBC", "jdbc:sqlite:" + 
					System.getProperty("user.dir") + File.separator + "res" + File.separator + "murder_bdd.sqlite");
			
			// Cr�er une nouvelle bdd ou se connecter
			/*String mode = xml.valeurNoeud("application", "mode");
			if (mode.equals("serveur"))
			{
				init("org.h2.Driver", "jdbc:h2:" + System.getProperty("user.dir") + 
		        		File.separator + "res" + File.separator + "murder_bdd;MVCC=TRUE;LOCK_TIMEOUT=15000;IFEXISTS=FALSE;" +
		        				"INIT=RUNSCRIPT FROM 'res/murder_bdd.sql'");
			}
			else
			{
				init("org.h2.Driver", "jdbc:h2:tcp://localhost:12500/res/murder_bdd");
			}*/

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public BaseDeDonnees(CoeurServeur _coeurServeur) 
	{
		coeurServeur = _coeurServeur;
		try 
		{
			// Charger fichier de config
			FichierXML xml = new FichierXML("config.xml");
			
			// Initialiser le pilote JDBC avec la base de donn�es SQLite
			init("org.sqlite.JDBC", "jdbc:sqlite:" + 
					System.getProperty("user.dir") + File.separator + "res" + File.separator + coeurServeur.getNomBDD());
			
			// Cr�er une nouvelle bdd ou se connecter
			/*String mode = xml.valeurNoeud("application", "mode");
			if (mode.equals("serveur"))
			{
				init("org.h2.Driver", "jdbc:h2:" + System.getProperty("user.dir") + 
		        		File.separator + "res" + File.separator + "murder_bdd;MVCC=TRUE;LOCK_TIMEOUT=15000;IFEXISTS=FALSE;" +
		        				"INIT=RUNSCRIPT FROM 'res/murder_bdd.sql'");
			}
			else
			{
				init("org.h2.Driver", "jdbc:h2:tcp://localhost:12500/res/murder_bdd");
			}*/

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Constructeur ou on passe en param�tre le driver JDBC et l'adresse du fichier sqlite
	 * @param _chargerDriver Charger le pilote JDBC
	 * @param _chargerURL Charger l'adresse ou est d�pos� la bdd
	 * @throws Exception Probl�mes de communication avec la bdd
	 */
	public BaseDeDonnees(String _chargerDriver, String _chargerURL) throws Exception 
	{
		init(_chargerDriver, _chargerURL);
	}
	
	
// ----- METHODES ----- //		

	/**
	 * Initialiser la class
	 * @param _variableDriver Variable du driver JDBC
	 * @param _variableURL Variable de l'adresse de la bdd
	 * @throws Exception Probl�mes de communication avec la bdd
	 */
	final public void init(String _variableDriver, String _variableURL) throws Exception 
	{
		setDriver(_variableDriver);
		setUrl(_variableURL);
		setConnection();
		setStatement();
	}

	/**
	 * Executer une seule requete SQL de type INSERT / UPDATE / DELETE
	 * @param _sql D�finir une instruction SQL
	 * @throws SQLException Erreur li� � la bdd variable selon la situation (champ inexistant, bdd occup�, ...)
	 */
	final public void executerInstruction(String _sql) throws SQLException 
	{
		etat.execute(_sql);
	}
	
	/**
	 * Executer des instructions SQL par lots
	 * @param _sql : D�finir une liste de requ�tes SQL
	 * @throws SQLException Erreur li� � la bdd variable selon la situation (champ inexistant, bdd occup�, ...)
	 */
	final public void executerInstruction(String[] _sql) throws SQLException 
	{
		for (int i = 0; i < _sql.length; i++) 
		{
			executerInstruction(_sql[i]);
		}
	}

	/**
	 * Executer une requete SQL de type SELECT
	 * @param _sql D�finir la requ�te SQL � executer
	 * @throws SQLException Erreur li� � la bdd variable selon la situation (champ inexistant, bdd occup�, ...)
	 * @return Retourne le ResultSet de la requete � manipuler selon vos besoins
	 */
	final public ResultSet executerRequete(String _sql) throws SQLException 
	{
		return etat.executeQuery(_sql);
	}

	/**
	 * Fermer la connexion � la base de donn�es
	 */
	final public void fermerConnexion() 
	{
		try 
		{
			connexion.close();
		} 
		catch (Exception e) 
		{
			System.out.println("Un probl�me est survenue lors de la fermeture de la bdd!");
		}
	}
	
	/**
	 * SYSTEME D, � changer
	 * @param _sql
	 * @param coeurClient
	 */
	final public static void envoyerRequete(String _sql, CoeurClient coeurClient, String champ)
	{
		//Object resultat = null;
		try
		{
			coeurClient.setResultatNonRecu(true);
			coeurClient.getClient().getSortie().writeObject(new Message(coeurClient.getIdClient(), Message.REQUETE_SQL, _sql, champ));
			System.out.println("Envoi d'une requ�te par le client num : "+coeurClient.getIdClient());
			while (coeurClient.isResultatNonRecu())
			{
				System.out.println("Attente du r�sulat de la requ�te");
			}
			coeurClient.setResultatNonRecu(true);
		//	resultat = coeurClient.getResultatSQL();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	return resultat;
	}
	
	/**
	 * SYSTEME D, � changer
	 * @param _sql
	 * @param coeurClient
	 */
	final public static void envoyerInstruction(String _sql, CoeurClient coeurClient)
	{
		//Object resultat = null;
		try
		{
			coeurClient.setResultatNonRecu(true);
			coeurClient.getClient().getSortie().writeObject(new Message(coeurClient.getIdClient(), Message.INSTRUCTION_SQL, _sql));
			System.out.println("Envoi d'une instruction par le client num : "+coeurClient.getIdClient());
		//	resultat = coeurClient.getResultatSQL();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	return resultat;
	}
	
	
// ----- GETTERS & SETTERS ----- //	
	
	/**
	 * Setter qui d�finit le driver
	 * @param _variableDriver Appliquer la variable du pilote (driver)
	 */
	private void setDriver(String _variableDriver) 
	{
		driver = _variableDriver;
	}

	/**
	 * Setter qui D�finit l'url de la base de donn�es
	 * @param _variableURL Appliquer la variable url ou est d�pos� la bdd
	 */
	private void setUrl(String _variableURL) 
	{
		url = _variableURL;
	}

	/**
	 * Setter qui D�finit la connexion au driver de la base de donn�es
	 * @throws Exception Erreur en cas de probl�me de communication avec la bdd
	 */
	final public void setConnection() throws Exception 
	{
		Class.forName(driver);
		connexion = DriverManager.getConnection(url);
	}

	/**
	 * Getter qui r�cuperer la connexion de la base de donn�es
	 * @return Retourne l'etat de la connexion
	 */
	final public Connection getConnection() 
	{
		return connexion;
	}

	/**
	 * Setter qui d�finit l'etat de la connexion vers la base de donn�es
	 * @throws Exception Erreur en cas de probl�me de communication avec la bdd
	 */
	final public void setStatement() throws Exception 
	{
		if (connexion == null) 
		{
			setConnection();
		}
		
		etat = connexion.createStatement();
		etat.setQueryTimeout(timeout);
	}

	/**
	 * Getter qui r�cup�re l'etat de la connexion vers la base de donn�es
	 * @return Retourne l'etat actuel de la bdd
	 */
	final public Statement getStatement() 
	{
		return etat;
	}
	
}