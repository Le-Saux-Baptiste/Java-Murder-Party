package fr.mercredymurderparty.outil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe qui d�finit des op�rations sur des fichiers text
 */
public class FichierTXT 
{
	/**
	 * Constructeur de la classe FichierTXT
	 */
	public FichierTXT()
	{
		
	}
	
	/**
	 * Proc�dure qui �crit des donn�es sur un fichier
	 * @param _fichier Adresse et nom du fichier (avec extension!)
	 * @param _contenu Contenu � ajouter au fichier
	 */
	public void ecrireFichier(String _fichier, String _contenu)
	{
		try 
		{
			// Instancier un nouveau flux en sortie sur un fichier
		    BufferedWriter out = new BufferedWriter(new FileWriter(_fichier));
		    
		    // Ecrire le flux sur le fichier
		    out.write(_contenu);
		    
		    // Fermer flux du fichier
		    out.close();
		} 
		catch (IOException e) 
		{
			System.out.println("Impossible d'�crire sur le fichier ! Trace : " + e.getMessage());
		}
	}
	
	/**
	 * Proc�dure qui supprime un fichier
	 * @param _fichier Adresse et nom du fichier (avec extension!)
	 */
	public void supprimerFichier(String _fichier)
	{
		try 
		{
			// Charger le fichier
			File fichier = new File(_fichier);

			// Supprimer le fichier
			if (!fichier.delete()) 
			{
				System.out.println("Le fichier n'a pas ete supprimer !");
			}
		} 
		catch (Exception e) 
		{
			System.out.println("Erreur lors de la suppression du fichier ! Trace : " + e.getMessage());
		}
	}
	
	/**
	 * Fonction qui v�rifie si un fichier existe
	 * @param _fichier Adresse et nom du fichier (avec extension!)
	 * @return Boolean qui indique si fichier existe ou pas
	 */
	public boolean fichierExiste(String _fichier)
	{
		// Charger le fichier
		File fichier = new File(_fichier);
		
		// Verifier si le fichier existe ou pas
        if (fichier.exists())
        {
        	return true;
        }
        else
        {
        	return false;
        }
	}
	
	/**
	 * Fonction qui va lire chaque lignes du fichier et les ajouter dans un tableau
	 * @param _fichier Adresse et nom du fichier (avec extension!)
	 * @return Un tableau (array) contenant n lignes du fichier
	 */
	public String[] lireLigneFichier(String _fichier)
	{
		List<String> listeLignesFichier = new ArrayList<String>();

		try 
		{
			File fichier = new File(_fichier);
		
			if (!fichier.exists() && fichier.length() < 0) 
			{
				System.out.println("Le fichier n'existe pas");
			} 
			else 
			{
				
				FileReader fr = new FileReader(fichier);
				BufferedReader reader = new BufferedReader(fr);
				String st = "";
				
				// Parcourir chaque lignes du fichier et l'ajouter dans l'array
				while ((st = reader.readLine()) != null) 
				{
					listeLignesFichier.add(st);
				}
			}
			
			// Convertir un array list en array.
			// Passage par cette technique afin de contourner le fait qu'un arry ne peux etre d�finis en dynmatique
			// et aussi par soucie de retourner un simple tableau manipulable dans notre code.
			String [] tableauLignesFichier = listeLignesFichier.toArray( new String[listeLignesFichier.size()] );
			
			// Retourner le tableu
			return tableauLignesFichier;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Fonction qui va lire ligne par ligne un fichier
	 * @param _nomFichier Dossier et nom du fichier � lire
	 * @return Le contenu du fichier
	 * @throws IOException Erreur si fichier inexistant ou bloqu� par exemple
	 */
	public String lireFichier(String _nomFichier) throws IOException 
	{
		// Variables locales
		String res = null;
		String dossierDuFichier = _nomFichier;

		// Instance un nouveau scanner
		Scanner scanner = new Scanner(new File(dossierDuFichier));

		// On parcour chaque ligne du fichier
		while (scanner.hasNextLine()) 
		{
			// Scan une ligne du fichier
			String line = scanner.nextLine();

			// Concat�ne la ligne au resultat final
			res = line + res;
		}

		// Fermer le scanner
		scanner.close();
		
		// Retourne les lignes du fichier
		return res;
	}
	
	/**
	 * Fonction qui va enregistrer dans un tableau tous les fichiers d'un repertoire filtr� par une extension
	 * @param _repertoire Repertoire � scanner
	 * @param _filtre Filtre des fichiers (ex: .txt, .xml, ...)
	 * @return Un tableau contenant n lignes de fichiers
	 */
	public String [] listerRepertoire(String _repertoire, String _filtre)
	{
		// Variables locales
		File repertoire = new File(_repertoire);  	// D�finir le repertoire dans la classe File
		String[] listeFichiersNonFiltrer; 			// Array pour contenir les fichiers avant filtre
		String[] listeFichiersFiltrer;				// Array pour contenir les fichiers apr�s filtre

		try 
		{
			// Initialise l'array ou met les fichiers filtr�
			listeFichiersFiltrer = new String[repertoire.list().length]; 
			
			// Met en array les fichiers du repertoire
			listeFichiersNonFiltrer = repertoire.list();
			
			// Parcourir les fichiers
			for(int i = 0; i < listeFichiersNonFiltrer.length; i++)
			{
				// Filtrer selon l'extension
				if(listeFichiersNonFiltrer[i].endsWith(_filtre))
				{
					listeFichiersFiltrer[i] = listeFichiersNonFiltrer[i].replaceFirst(_filtre, "");
				}
			}
			
			// Retourne la liste filtr�
			return listeFichiersFiltrer;
		} 
		catch (Exception e) 
		{
			System.err.println("Impossible de lister le repertoire ! \n" +
					"Trace de l'erreur: " + e.getMessage());
		}
		
		// Retourne rien en cas d'erreur
		return null;
	}
	
	/**
	 * Proc�dure qui renomme un fichier
	 * @param _fichierAncienNom Adresse et nom du fichier (avec extension!) de l'ancien fichier
	 * @param _fichierNouveauNom Adresse et nom du fichier (avec extension!) du nouveau fichier
	 */
	public void renommerFichier(String _fichierAncienNom, String _fichierNouveauNom)
	{
		try 
		{
			File fichier = new File(_fichierAncienNom);
			fichier.renameTo(new File(_fichierNouveauNom));
		} 
		catch (Exception e) 
		{
			System.out.println("Un probl�me technique emp�che la modification du nom du fichier.\n" + e.getMessage());
		}
	}
	
	/**
	 * Proc�dure qui copie un fichier d'un repertoire � un autre
	 * @param _fichierSource Repertoire source du fichier
	 * @param _fichierDestination Repertoire de d�stination du fichier
	 * @throws IOException Si emplacement non trouv�, retourne une erreur
	 */
	public final void copierFichier(File _fichierSource, File _fichierDestination) throws IOException 
	{
	    if(!_fichierDestination.exists()) 
	    {
	        _fichierDestination.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;
	    
	    try 
	    {
	        source = new FileInputStream(_fichierSource).getChannel();
	        destination = new FileOutputStream(_fichierDestination).getChannel();
	        long cpt = 0;
	        long taille = source.size();              
	        while((cpt += destination.transferFrom(source, 0, taille-cpt))<taille);
	    }
	    finally 
	    {
	        if(source != null) 
	        {
	            source.close();
	        }
	        
	        if(destination != null) 
	        {
	            destination.close();
	        }
	    }
	}
}
