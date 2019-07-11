import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.FileWriter; 

public class webScrape {
    public static void main(String[] args) {
		String DH[] = new String[5];
		String Name[] = new String[5];
        DH[0] = 
                "https://nutrition.sa.ucsc.edu/menuSamp.asp?locationNum=20&locationName=Crown+Merrill+Dining+Hall&sName=&naFlag=";
		DH[1] = 
                "https://nutrition.sa.ucsc.edu/menuSamp.asp?locationNum=25&locationName=Porter+Kresge+Dining+Hall&sName=&naFlag=";
		DH[2] = 
                "https://nutrition.sa.ucsc.edu/menuSamp.asp?locationNum=30&locationName=Rachel+Carson+Oakes+Dining+Hall&sName=&naFlag=";
		DH[3] = 
                "https://nutrition.sa.ucsc.edu/menuSamp.asp?locationNum=40&locationName=Colleges+Nine+%26+Ten+Dining+Hall&sName=&naFlag=";
		DH[4] = 
                "https://nutrition.sa.ucsc.edu/menuSamp.asp?locationNum=05&locationName=Cowell+Stevenson+Dining+Hall&sName=&naFlag=";
		Name[0] = "Crown&Merrill";
		Name[1] = "Porter&Kresge";
		Name[2] = "RachelCarson&Oakes";
		Name[3] = "Nine&Ten";
		Name[4] = "Cowell&Stevenson";
        
        try {
			FileWriter fw = new FileWriter("data.txt");
			for(int i = 0;i<5;i++){
				
				final Document document = Jsoup.connect(DH[i]).get();
				
				
				fw.write("==========================");
				fw.write(Name[i]);
				fw.write("==========================\n");
				
				for (Element food : document.select("div.menusamprecipes,div.menusampmeals,img[src$=.gif]")) {
					if(food.text().equals("Breakfast")||food.text().equals("Lunch")||food.text().equals("Dinner")){
						fw.write("\n");
					}
					fw.write(food.text());
					if (food.text().equals("")){  //finds the ingredients of the food
						String tag = food.attr("src");
						fw.write("------");
						fw.write( tag.substring(tag.indexOf('/')+1,tag.indexOf('.')));
					}
					fw.write("\n");
					if(food.text().equals("Breakfast")||food.text().equals("Lunch")||food.text().equals("Dinner")){
						fw.write("\n");
					}				
				}
				
			}
			fw.close();
			
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
}