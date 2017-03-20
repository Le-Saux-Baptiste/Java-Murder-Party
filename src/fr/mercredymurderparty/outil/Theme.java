package fr.mercredymurderparty.outil;

import java.io.File;

import javax.swing.UIManager;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.nilo.plaf.nimrod.NimRODTheme;

/**
 * Gerer les th�mes de l'application, bas� sur la lib look and feel NimRODTheme
 */
public class Theme 
{
	
// ----- ATTRIBUTS ----- //
	
	private String nomDuTheme;
	
	
// ----- CONSTRUCTEUR ----- //
	
	/**
	 * Constructeur de la classe th�me
	 */
	public Theme()
	{
		// Charger le fichier de configuration
		FichierXML xml = new FichierXML("config.xml");
		
		// D�finir le nom du th�me
		this.setNomDuTheme(xml.valeurNoeud("theme", "nom") + ".theme");
	}
	
	
// ----- METHODES ----- //		
	
	/**
	 * Proc�dure qui d�finit le th�me principal de l'application
	 */
	final public void charger() 
	{
		try 
		{
			// Si on a deja un nom de th�me on l'utilise sinon, on passe par le fichier theme NimRODTheme
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
	 * D�finir un fond pour une fenetre selon le th�me en cours
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
	 * Getter qui recup�re le nom du package du theme
	 * @return Retourne le nom du th�me
	 */
	final public String getNomDuTheme() 
	{
		return nomDuTheme;
	}

	/**
	 * Setter qui d�finit le nom du thm�me
	 * @param _nomDuTheme Nom du th�me
	 */
	final public void setNomDuTheme(String _nomDuTheme) 
	{
		this.nomDuTheme = _nomDuTheme;
	}
}
