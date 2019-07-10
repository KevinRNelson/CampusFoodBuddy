import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.FileWriter; 

public class webScrape {
    public static void main(String[] args) {
        final String url = 
                "https://nutrition.sa.ucsc.edu/menuSamp.asp?locationNum=25&locationName=merrill+crown+Dining+Hall&sName=&naFlag=";
        
        try {
            final Document document = Jsoup.connect(url).get();
			FileWriter fw = new FileWriter("data.txt");
			for (Element food : document.select("div.menusamprecipes,div.menusampmeals")) {
				if(food.text().equals("Breakfast")||food.text().equals("Lunch")||food.text().equals("Dinner")){
					fw.write("\n");
				}
				fw.write(food.text());
				fw.write("\n");
				if(food.text().equals("Breakfast")||food.text().equals("Lunch")||food.text().equals("Dinner")){
					fw.write("\n");
				}				
			}
			fw.close();
			
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
}