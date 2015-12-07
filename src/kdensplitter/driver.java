package kdensplitter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class driver {

	public static ArrayList<String> transforms = new ArrayList<String>();

	static {
		transforms.add("-480/-270:2880x1620:100;[STOP]=0/0:1920x1080:100");
		transforms.add("-480/-270:2880x1620:100;[STOP]=-96/-54:2112x1188:100");
		transforms.add("0/-270:2880x1620:100;[STOP]=-960/-270:2880x1620:100");
		transforms.add("-192/-108:2304x1296:100;[STOP]=0/-270:2880x1620:100");
		transforms.add("-576/-324:3072x1728:100;[STOP]=0/-108:2304x1296:100");
	}

	public static String getRandomTransformString() {
		int i = transforms.size();

		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((i));

		return transforms.get(randomNum);

	}

	public static void splitAndSave(int numSections, String producerId, Document doc, String fileToSaveTo) {
		try {
			NodeList playLists = doc.getElementsByTagName("playlist");
			for (int i = 0; i < playLists.getLength(); i++) {

				Node playList = playLists.item(i);
				// System.out.println(node.toString());

				NodeList childNodes = playList.getChildNodes();
				List<Node> entries = getChildNodesMyName("entry", childNodes);
				for (Node entry : entries) {

					// if(entry.getTextContent())

					NamedNodeMap attr = entry.getAttributes();

					// if
					// (producerId.equals(attr.getNamedItem("producer").getNodeValue()))
					// {
					/**
					 * We have found it...
					 */

					int orig_in = Integer.parseInt(attr.getNamedItem("in").getNodeValue());
					int orig_out = Integer.parseInt(attr.getNamedItem("out").getNodeValue());
					int origLength = orig_out - orig_in;
					int newLength = origLength / numSections;

					for (int sectionsToCreate = 0; sectionsToCreate < numSections; sectionsToCreate++) {
						Node newEntry = entry.cloneNode(true);

						String start = Integer.toString(orig_in + (newLength * sectionsToCreate));
						String stop = Integer.toString(orig_in + (newLength * (sectionsToCreate + 1)));

						newEntry.getAttributes().getNamedItem("in")
								.setNodeValue(Integer.toString(orig_in + (newLength * sectionsToCreate)));
						newEntry.getAttributes().getNamedItem("out")
								.setNodeValue(Integer.toString(orig_in + (newLength * (sectionsToCreate + 1))));

						playList.appendChild(newEntry);

					}

					playList.removeChild(entry);

					/*
					 * Node newEntry = entry.cloneNode(true);
					 * newEntry.getAttributes().getNamedItem("in").
					 * setNodeValue(Integer.toString(orig_out / 2));
					 * entry.getAttributes().getNamedItem("out").
					 * setNodeValue(Integer.toString(orig_out / 2));
					 * 
					 * playList.appendChild(newEntry);
					 */
					// }
				}
			}

			saveDoc(doc, fileToSaveTo);

			System.out.println("Done");

		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Node getRandomPanFilter(Document doc) {
		Node filter = doc.createElement("filter");

		/*
		
		*/

		Node track = doc.createElement("property");
		track.getAttributes().setNamedItem(doc.createAttribute("name"));
		track.getAttributes().getNamedItem("name").setNodeValue("track");

		// track.getAttributes().setNamedItem(doc.createAttribute("name").setValue("track"));
		Node background = doc.createElement("property");
		background.getAttributes().setNamedItem(doc.createAttribute("name"));
		background.getAttributes().getNamedItem("name").setNodeValue("background");

		Node mlt_type = doc.createElement("property");
		mlt_type.getAttributes().setNamedItem(doc.createAttribute("name"));
		mlt_type.getAttributes().getNamedItem("name").setNodeValue("mlt_type");

		Node mlt_service = doc.createElement("property");
		mlt_service.getAttributes().setNamedItem(doc.createAttribute("name"));
		mlt_service.getAttributes().getNamedItem("name").setNodeValue("mlt_service");

		Node kdenlive_id = doc.createElement("property");
		kdenlive_id.getAttributes().setNamedItem(doc.createAttribute("name"));
		kdenlive_id.getAttributes().getNamedItem("name").setNodeValue("kdenlive_id");

		Node tag = doc.createElement("property");
		tag.getAttributes().setNamedItem(doc.createAttribute("name"));
		tag.getAttributes().getNamedItem("name").setNodeValue("tag");

		Node kdenlive_ix = doc.createElement("property");
		kdenlive_ix.getAttributes().setNamedItem(doc.createAttribute("name"));
		kdenlive_ix.getAttributes().getNamedItem("name").setNodeValue("kdenlive_ix");

		Node kdenlive_info = doc.createElement("property");
		kdenlive_info.getAttributes().setNamedItem(doc.createAttribute("name"));
		kdenlive_info.getAttributes().getNamedItem("name").setNodeValue("kdenlive_info");

		Node transition_geometry = doc.createElement("property");
		transition_geometry.getAttributes().setNamedItem(doc.createAttribute("name"));
		transition_geometry.getAttributes().getNamedItem("name").setNodeValue("transition.geometry");

		Node transition_distort = doc.createElement("property");
		transition_distort.getAttributes().setNamedItem(doc.createAttribute("name"));
		transition_distort.getAttributes().getNamedItem("name").setNodeValue("transition.distort");

		track.setTextContent("0");
		background.setTextContent("colour:0x00000000");
		mlt_type.setTextContent("filter");
		mlt_service.setTextContent("affine");
		kdenlive_id.setTextContent("pan_zoom");
		tag.setTextContent("affine");
		kdenlive_ix.setTextContent("1");

		transition_distort.setTextContent("0");

		filter.appendChild(track);
		filter.appendChild(background);
		filter.appendChild(mlt_type);
		filter.appendChild(mlt_service);
		filter.appendChild(kdenlive_id);
		filter.appendChild(tag);
		filter.appendChild(kdenlive_ix);
		filter.appendChild(kdenlive_info);

		filter.appendChild(transition_distort);

		return filter;
	}

	public static void randomizePanAndZoom(String producerId, Document doc, String fileToSaveTo) {

		try {
			NodeList playLists = doc.getElementsByTagName("playlist");
			for (int i = 0; i < playLists.getLength(); i++) {

				Node playList = playLists.item(i);
				// System.out.println(node.toString());

				NodeList childNodes = playList.getChildNodes();
				List<Node> entries = getChildNodesMyName("entry", childNodes);
				for (Node entry : entries) {
					NamedNodeMap attr = entry.getAttributes();

					// if
					// (producerId.equals(attr.getNamedItem("producer").getNodeValue()))
					// {

					int orig_in = Integer.parseInt(attr.getNamedItem("in").getNodeValue());
					int orig_out = Integer.parseInt(attr.getNamedItem("out").getNodeValue());

					Node filter = getRandomPanFilter(doc);
					Attr in = doc.createAttribute("in");
					Attr out = doc.createAttribute("out");

					String start = Integer.toString(orig_in);
					String stop = Integer.toString(orig_out);

					in.setValue(start);
					out.setValue(stop);

					filter.getAttributes().setNamedItem(in);
					filter.getAttributes().setNamedItem(out);

					// calculate transition geometry
					String transgeometry = getRandomTransformString().replace("[STOP]",
							Integer.toString(orig_out - orig_in));

					Node transition_geometry = doc.createElement("property");
					transition_geometry.getAttributes().setNamedItem(doc.createAttribute("name"));
					transition_geometry.getAttributes().getNamedItem("name").setNodeValue("transition.geometry");

					transition_geometry.setTextContent(transgeometry);

					filter.appendChild(transition_geometry);

					entry.appendChild(filter);
					// }
				}
			}

			saveDoc(doc, fileToSaveTo);

			System.out.println("Done");

		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Document getDoc(String filepath) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(filepath);
		return doc;
	}

	public static void saveDoc(Document doc, String fileToSaveTo) throws TransformerException {
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(fileToSaveTo));
		transformer.transform(source, result);
		System.out.println("SAVED");
	}

	public static void main(String argv[]) throws IOException {

		// String filepath = argv[0];
		//String action = argv[1];

		String[] OPTIONS = { "Split These Bitches", "Random Pan Existing Clips", "Get me outta dis shiz" };

		JFrame frame = new JFrame("DialogDemo");
		int option = JOptionPane.showOptionDialog(null, "What would you like to do?", "suckit", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, OPTIONS, "Metric");

		
		while (option!=2) {
		
		String filepath = "~/videos/";

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(filepath));
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			filepath = fileChooser.getSelectedFile().getPath();
			JOptionPane.showMessageDialog(null, "You selected " + filepath);
		} else if (result == JFileChooser.CANCEL_OPTION) {
			JOptionPane.showMessageDialog(null, "You selected nothing.");
			System.exit(0);
		} else if (result == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(null, "An error occurred.");
			System.exit(0);
		}

		Runtime.getRuntime().exec("kdenlive " + filepath);

		// System.out.println(r);
		Document doc;

		try {
			if (filepath == null) {
				System.out.println("first argument must be path to an actual kdenlive file");
				System.exit(0);
			} else {
				
				doc = getDoc(filepath);

				switch (option) {
				case 0:
					JFrame frame2 = new JFrame("DialogDemo");
					String splits = JOptionPane.showInputDialog("Enter the number of splits per clip");
					
					int num = Integer.parseInt(splits);
					splitAndSave(num, "", doc, filepath);

					Runtime.getRuntime().exec("kdenlive " + filepath);
					
					String[] OPTIONS2 = { "No Thanks", "Random Pan Existing Clips" };
					int option2 = JOptionPane.showOptionDialog(null,
							"Great, you've split the clips up, now... re-open the kdenlive file, adjust as you need to, and randomize the pan and zoom, Would you like to?",
							"suckit", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, OPTIONS2,
							"Metric");

					if (option2 == 1) {
						option=1;
						continue;
						//randomizePanAndZoom("", doc, filepath);
					}
					break;
				case 1:
					System.out.println("randompan");
					randomizePanAndZoom("", doc, filepath);
					Runtime.getRuntime().exec("kdenlive " + filepath);
					
					break;
				default:
					//System.out.println(action);
					System.out.println("second argument must be randompan or split");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		}
	}

	public static List<Node> getChildNodesMyName(String name, NodeList nl) {
		ArrayList<Node> nodelist = new ArrayList<Node>();

		// System.out.println("Finding");

		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node.getNodeName().equals(name)) {
				// filter in only those with IDs that have an underscore, and
				// filter out those with the text 'audio'

				NamedNodeMap attr = node.getAttributes();
				String producerID = attr.getNamedItem("producer").getNodeValue();
				if (!producerID.contains("_audio") && producerID.contains("_"))
					nodelist.add(node);
			}

		}

		// System.out.println("Found: " + nodelist.size());

		return nodelist;

	}
}