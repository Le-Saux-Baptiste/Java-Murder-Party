package fr.mercredymurderparty.outil;

import java.io.File;

import javax.swing.UIManager;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.nilo.plaf.nimrod.NimRODTheme;

/**
 * Gerer les thèmes de l'application, basé sur la lib look and feel NimRODTheme
 */
public class Theme 
{
	
// ----- ATTRIBUTS ----- //
	
	private String nomDuTheme;
	
	
// ----- CONSTRUCTEUR ----- //
	
	/**
	 * Constructeur de la classe thème
	 */
	public Theme()
	{
		// Charger le fichier de configuration
		FichierXML xml = new FichierXML("config.xml");
		
		// Définir le nom du thème
		this.setNomDuTheme(xml.valeurNoeud("theme", "nom") + ".theme");
	}
	
	
// ----- METHODES ----- //		
	
	/**
	 * Procédure qui définit le thème principal de l'application
	 */
	final public void charger() 
	{
		try 
		{
			// Si on a deja un nom de thème on l'utilise sinon, on passe par le fichier theme NimRODTheme
			if (this.getNomDuTheme() == null)
			{
				UIManager.setLookAndFeel("com.nilo.plaf.nimrod.NimRODLookAndFeel");
			}
			else
			{
				NimRODTheme nt = new NimRODTheme(System.getProperty("user.dir") + File.separator + "themes" + File.separator + this.getNomDuTheme());
				NimRODLookAndFeel nf = new NimRODLookAndFeel();
				NimRODLookAndFeel.setCurrentTheme(nt);
				UIManager.setLookAndFeel(nf);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Définir un fond pour une fenetre selon le thème en cours
	 * @return Nom du fichier jpg
	 */
	final public String definirFond()
	{
		String theme = this.getNomDuTheme();
		
		if ("Night.theme".equals(theme)) 
		{
			return "fond_accueil_bois_sombre.jpg";
		} 
		else if ("LightTabaco.theme".equals(theme)) 
		{
			return "fond_accueil_bois_clair.jpg";
		} 
		else 
		{
			return "fond_accueil_bois_clair.jpg";
		}
	}
	
	
// ----- GETTERS & SETTERS ----- //		

	/**
	 * Getter qui recupère le nom du package du theme
	 * @return Retourne le nom du thème
	 */
	final public String getNomDuTheme() 
	{
		return nomDuTheme;
	}

	/**
	 * Setter qui définit le nom du thmème
	 * @param _nomDuTheme Nom du thème
	 */
	final public void setNomDuTheme(String _nomDuTheme) 
	{
		this.nomDuTheme = _nomDuTheme;
	}
}
