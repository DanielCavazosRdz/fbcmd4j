package facebook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.io.PrintWriter;
import java.io.Reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import com.restfb.types.Post;
import com.restfb.types.User;

public class Main {
	
	private static final Logger logger = LogManager.getLogger(Main.class);
	
	private static final String CONFIG_DIR = "config";
	private static final String CONFIG_FILE = "facebook.properties";
	private static final String APP_VERSION = "v1.0";
	
	public static Scanner scanner = new Scanner(System.in);
	public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	@SuppressWarnings("null")
	public static void main(String[] args) throws IOException {
		
		Properties props = null;
		int loop = 0;
		try {
			props = Utils.loadPropertiesFromFile(CONFIG_DIR, CONFIG_FILE);
		} catch (IOException ex) {	
			System.out.println(ex);
			logger.error(ex);
		}
		
		int seleccion;
		String message;
		
		
		String AccessToken = props.getProperty("oauth.accessToken");
		@SuppressWarnings("deprecation")
		FacebookClient fbClient = new DefaultFacebookClient(AccessToken);
		
		Connection<Post> feed = fbClient.fetchConnection("me/feed", Post.class);
		
		User me = fbClient.fetchObject("me", User.class);
		
		while (loop == 0){
		System.out.format("Simple Twitter client %s\n\n", APP_VERSION);
		System.out.println("Opciones: ");
		System.out.println("(0) Cargar TimeLine");
		System.out.println("(1) publicar status en tu foro");
		System.out.println("(2) publicar link en tu foro");
		System.out.println("(3) salir");
		System.out.println("\nPor favor ingrese una opción: ");
		// Fin de Menu
			seleccion = scanner.nextInt();
			
			switch(seleccion){
			case 0:
				System.out.println(me.getName());
				System.out.println(me.getLanguages().get(0).getName());
				System.out.println();
				
				int counter = 0;
				
				Set<String> lines = new HashSet<String>();
				counter = 0;
				
				for(List<Post> page : feed){
					for(Post apost : page){
						System.out.println(apost.getMessage());
						System.out.println("fb.com/" + apost.getId());
						lines.add(apost.getMessage());
						lines.add("fb.com/" + apost.getId());
						counter++;
					}
				}
				System.out.println("# of results: " + counter);
				
				Utils.writefile(me, lines);
				break;
				case 1: 
					System.out.println("que quieres publicar?");
					System.out.println("status: ");
					message = br.readLine();
					try{
					FacebookType response = fbClient.publish("me/feed", FacebookType.class, Parameter.with("message", message));
					System.out.println("fb.com/" + response.getId());
					} catch(Exception e){
						logger.error(e);
					}
					break;
				case 2:
					System.out.println("que quieres publicar?");
					System.out.println("text with link: ");
					message = br.readLine();
					System.out.println("link: ");
					String link = br.readLine();
					try{
					FacebookType response1 = fbClient.publish("me/feed", FacebookType.class, Parameter.with("message", message), 
							Parameter.with("link", link));
					System.out.println("fb.com/" + response1.getId());
					} catch(Exception e){
						logger.error(e);
					}
					break;
					
				case 3:
					loop = 1;
					break;
				default:
					logger.error("Opcion invalida");
					break;
				}
		}

	}
}
