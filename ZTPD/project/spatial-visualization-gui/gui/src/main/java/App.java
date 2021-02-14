import com.spatial.controllers.MainWindowController;
import org.xml.sax.SAXException;
import com.spatial.views.MainWindow;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.sql.SQLException;


public class App {
    public static void main(String[] args) throws SQLException, ParserConfigurationException, SAXException, IOException {


        MainWindowController app = new MainWindowController();
        app.show();

    }


}