package fr.mercredymurderparty.outil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Classe ou l'on va d�finir toutes les fonction utiles pour l'application
 *
 */
public class Fonctions 
{
	/**
	 * Constructeur de la classe Fonction
	 */
	public Fonctions()
	{
		
	}
	
	/**
	 * Fonction qui convertit un tableau de caract�re en String
	 * @param _caracteres Un tableau de caract�res
	 * @return La chaine au format String
	 */
	public final String convertirCharArrayVersString(char[] _caracteres) 
	{
		char[] tableauDeCaracteres = _caracteres;
		String str = new String(tableauDeCaracteres);
		return(str);
	}
    
    /**
     * Fonction qui retourne le r�pertoire utilisateur
     * @return Retourne le repertoire utilisateur suivi du fichier � charger (ex: c/murder/config.xml)
     */
	public final String repertoireUtilisateur(String _dossierRacine) 
	{
		return System.getProperty("user.dir") + File.separator + _dossierRacine;
	}
	
    /**
     * Fonction qui retourne le r�pertoire utilisateur avec le fichier
     * @return Retourne le repertoire utilisateur (ex: c/murder)
     */
	public final String repertoireUtilisateur(String _dossierRacine, String _nomFichier) 
	{
		return System.getProperty("user.dir") + File.separator + _dossierRacine + File.separator + _nomFichier;
	}
	

	
	/**
	 * [[[ Fonction � supprimer avant la fin du projet ]]]
	 * T�l�charge un fichier depuis un ftp (pour notre cas, la bdd sqlite)
	 * @param _serveur Adresse serveur distant
	 * @param _login Login Le login du ftp
	 * @param _mdp Le mot de passe du ftp
	 * @param _fichierDistant Fichier sur le ftp � t�l�charger
	 * @param _fichierLocal Repertoire ou stocker le fichier ftp
	 * @throws Exception En cas d'erreur, on renvoie une grosse exception
	 */
	public void telechargerFichierViaFtp(String _serveur, String _login, String _mdp, String _fichierDistant, String _fichierLocal) throws Exception 
	{
		String server = _serveur;
		String userName = _login;
		String password = _mdp;
		String localFileName = _fichierLocal;
		String remoteFileName = _fichierDistant;
		
		System.out.println("Connexion au serveur ftp...");

		// Connection String
		URL url = new URL("ftp://" + userName + ":" + password + "@" + server  + remoteFileName + ";type=i");
		URLConnection con = url.openConnection();

		BufferedInputStream in = new BufferedInputStream(con.getInputStream());

		System.out.println("T�l�chargement du fichier.");

		// T�l�charger le fichier en local
		FileOutputStream out = new FileOutputStream(localFileName);

		int i = 0;
		byte[] bytesIn = new byte[1024];
		while ((i = in.read(bytesIn)) >= 0) 
		{
			out.write(bytesIn, 0, i);
		}
		out.close();
		in.close();

		System.out.println("Fichier t�l�charg�!.");
	}
	
	/**
	 * [[[ Fonction � supprimer avant la fin du projet ]]]
	 * Envoyer un fichier via ftp (dans notre cas la bdd)
	 * @param _serveur Adresse serveur distant
	 * @param _login Login Le login du ftp
	 * @param _mdp Le mot de passe du ftp
	 * @param _fichierDistant Fichier sur le ftp � t�l�charger
	 * @param _fichierLocal Repertoire ou stocker le fichier ftp
	 * @return
	 */
	public void envoyerFichierViaFtp(String _serveur, String _login, String _mdp, String _fichierDistant, String _fichierLocal) 
	{
		String server = _serveur;
		String userName = _login;
		String password = _mdp;
		String localFileName = _fichierLocal;
		String remoteFileName = _fichierDistant;

		try 
		{
			// Connection String
			URL url = new URL("ftp://" + userName + ":" + password + "@" + server + remoteFileName + ";type=i");
			URLConnection con = url.openConnection();

			InputStream is = new FileInputStream(localFileName);
			BufferedInputStream bis = new BufferedInputStream(is);
			OutputStream os = con.getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(os);
			byte[] buffer = new byte[1024];
			int readCount;

			while ((readCount = bis.read(buffer)) > 0) 
			{
				bos.write(buffer, 0, readCount);
			}
			bos.close();

			System.out.println("Fichier envoy�!");
		} 
		catch (Exception ex) 
		{
			StringWriter sw0 = new StringWriter();
			PrintWriter p0 = new PrintWriter(sw0, true);
			ex.printStackTrace(p0);
			String erMesg = sw0.getBuffer().toString();
			System.out.println(erMesg);
		}
	}
	

	

	

	

	
	

	

}
