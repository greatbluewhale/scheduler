import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


public class Weather {
    
    private static final String ICON_URL = "http://img.weather.weatherbug.com/forecast/icons/localized/50x42/en/trans/";
    
    private static ImageIcon[] weatherIcons = null;
    
    public static ImageIcon[] getWeather(String zipCode){
        if (weatherIcons == null){
            try{
                URL url = new URL("http://A3439005366.api.wxbug.net/getForecastRSS.aspx?ACode=A3439005366&zipCode=" + 
                        zipCode + "&OutputType=1");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.setReadTimeout(10000);
                connection.connect();
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(connection.getInputStream());
                NodeList nodes = doc.getElementsByTagName("aws:forecast");
                if (nodes.getLength() == MonthlyViewPage.DAYS_IN_WEEK){
                    weatherIcons = new ImageIcon[MonthlyViewPage.DAYS_IN_WEEK];
                    for (int i=0; i<MonthlyViewPage.DAYS_IN_WEEK; i++){
                        String iconName = nodes.item(i).getChildNodes().item(2).getAttributes().getNamedItem("icon").getTextContent();
                        weatherIcons[i] = new ImageIcon(new URL(ICON_URL + iconName.substring(0, 8) + "png"));
                    }
                }
            } catch (Exception e){
                weatherIcons = null;
                System.out.println("Connection to Weather API failed.");
            }
        }
        return weatherIcons;
    }
}
