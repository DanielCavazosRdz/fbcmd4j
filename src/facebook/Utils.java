package facebook;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.restfb.types.User;

public class Utils {
	
	private static final Logger logger = LogManager.getLogger(Utils.class);
	
	
	public static <T, U> Properties loadPropertiesFromFile(String foldername, String filename) throws IOException{
		Properties props = new Properties();
		Path configFile = Paths.get(foldername, filename);
		if (Files.exists(configFile)){
			props.load(Files.newInputStream(configFile));
			BiConsumer<String, String> bc = (T, U) -> {
				if (T.isEmpty() || U.isEmpty())
					logger.error("los strings estan vacios");
			};
		} else {
			logger.info("Creando nuevo archivo de condifugración.");
			Files.copy(Paths.get("facebook","facebook.properties"), configFile);
		}
		return props;
	}
	
	public static void writefile(User me, Set<String>lines){
		try{
			Path file = Paths.get("posts de " + me.getName() + ".txt");
			Files.write(file, lines, Charset.forName("UTF-8"));
			System.out.println("file created succesfully");
		} catch (IOException e) {
			System.err.println("'ocurrio un error: " + e.getMessage());
		}
	}
	
}
