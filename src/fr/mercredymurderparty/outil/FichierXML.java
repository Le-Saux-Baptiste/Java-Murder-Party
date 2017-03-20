package fr.mercredymurderparty.outil;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Classe qui définit des opération sur des fichiers xml
 *
 */
public class FichierXML 
{
	
// ----- ATTRIBUTS ----- //
	
	private String fichierXML;		// Adresse du fichier XML
	
	
// ----- CONSTRUCTEUR ----- //	
	
	/**
	 * Constructeur de la classe FichierXML
	 * @param _fichierXML Fichier XML
	 */
	public FichierXML(String _fichierXML) 
	{
		this.setFichierXML(System.getProperty("user.dir") + File.separator + "res" + File.separator + _fichierXML);
	}

	
// ----- METHODES ----- //	
	
	/**
	 * Fonction qui va lire un noeud depuis un fichier XML
	 * @param _noeudPere Noeud du pere
	 * @param _noeudFils Noeud du fils
	 * @return Contenu du noeud au format String
	 */
	public final String valeurNoeud(String _noeudPere, String _noeudFils) 
	{	
		// TODO Checkstyle identifie du code dupliqué ( code identique à partir de la ligne 91 ) qui pourrai être simplifié. A vous de voir si c'est faisaible et nécéssaire.
		try 
		{
			// Recuperer le fichier XML dans le dossier de l'application
			File fichierXML = new File(this.getFichierXML());
			
			// Instancier un nouveau DocumentBuilder
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			// Parser le fichier XML
			Document documentXML = dBuilder.parse(fichierXML);
			documentXML.getDocumentElement().normalize();
			
			// Liste des noeuds
			NodeList listeNoeuds = documentXML.getElementsByTagName(_noeudPere);

			// Parcourir la liste des noeuds...
			for (int cpt = 0; cpt < listeNoeuds.getLength(); cpt++) 
			{
				Node noeud = listeNoeuds.item(cpt);
				
				// ...jusqu'à obtenir le noeud passer en paramètre
				if (noeud.getNodeType() == Node.ELEMENT_NODE) 
				{
					Element element = (Element) noeud;
					return obtenirValeurNoeudFils(_noeudFils, element);
				}
			}
		} 
		catch (Exception e) 
		{
			System.out.println("Impossible de lire le fichier XML: " + e.getMessage());
			return null;
		}
		return null;
	}
	
	/**
	 * Procédure qui modifie l'élément d'un noeud
	 * @param _noeudPere Noeud père
	 * @param _noeudFils Noeud fils
	 * @param _contenu Nouveau contenu du noeud
	 */
	final public void modifierNoeud(String _noeudPere, String _noeudFils, String _contenu) 
	{
		try 
		{
			// Recuperer le fichier XML dans le dossier de l'application
			File fichierXML = new File(this.getFichierXML());

			// Instancier un nouveau DocumentBuilder
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			// Parser le fichier XML
			Document documentXML = dBuilder.parse(fichierXML);
			documentXML.getDocumentElement().normalize();

			// Liste des noeuds
			NodeList listeNoeuds = documentXML.getElementsByTagName(_noeudPere);

			// Parcourir la liste des noeuds...
			for (int cpt = 0; cpt < listeNoeuds.getLength(); cpt++) 
			{
				Node noeud = listeNoeuds.item(cpt);

				// ...jusqu'à obtenir le noeud passer en paramètre
				if (noeud.getNodeType() == Node.ELEMENT_NODE) 
				{
					Element element = (Element) noeud;

					// Recuperer le noeud fils
					NodeList nlFils = element.getElementsByTagName(_noeudFils).item(0).getChildNodes();
					Node valeurNoeud = (Node) nlFils.item(0);
					valeurNoeud.setTextContent(_contenu);

					// Modifier le fichier XML
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(documentXML);
					StreamResult result = new StreamResult(this.getFichierXML());
					transformer.transform(source, result);
				}
			}
		} 
		catch (Exception e) 
		{
			System.out.println("Impossible de lire le fichier XML: " + e.getMessage());
		}
	}

	/**
	 * Fonction qui recupère le contenue du noeud fils
	 * @param _noeudFils Le noeud fils du fichier XML
	 * @param _elementPere L'element du noeud pere
	 * @return Retourne le contenue du noeud fils au format String
	 */
	private final String obtenirValeurNoeudFils(String _noeudFils, Element _elementPere) 
	{
		NodeList listeNoeuds = _elementPere.getElementsByTagName(_noeudFils).item(0).getChildNodes();
		Node valeurNoeud = (Node) listeNoeuds.item(0);
		return valeurNoeud.getNodeValue();
	}
	
	
// ----- GETTERS & SETTERS ----- //
	
	/**
	 * Getter qui recupère le fichier XML
	 * @return Adresse du fichier XML sur le disque dur
	 */
	final public String getFichierXML() 
	{
		return fichierXML;
	}

	/**
	 * Setter qui définit l'adresse du fichier XML
	 * @param _fichierXML Adresse du fichier XML sur le disque dur
	 */
	final public void setFichierXML(String _fichierXML) 
	{
		this.fichierXML = _fichierXML;
	}
}
