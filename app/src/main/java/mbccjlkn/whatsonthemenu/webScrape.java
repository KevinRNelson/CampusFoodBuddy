import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.FileWriter; 
import java.util.Scanner;
import java.io.*; 


public class webScrape {
    public static void main(String[] args) {
		String DH[] = new String[5];
		String Name[] = new String[5];
		File files[] = new File[5];
		//Scanner sc = new Scanner(System.in);
		
		
        DH[0] = 
				"crown.html";                
		DH[1] = 
                "porter.html";
		DH[2] = 
                "oakes.html";
		DH[3] = 
                "c9c10.html";
		DH[4] = 
                "cowell.html";
		Name[0] = "Crown&Merrill";
		Name[1] = "Porter&Kresge";
		Name[2] = "RachelCarson&Oakes";
		Name[3] = "Nine&Ten";
		Name[4] = "Cowell&Stevenson";
		for(int i = 0;i<5;i++){
			files[i] = new File(DH[i]);
		}
        try {
			FileWriter fw = new FileWriter("data.txt");
			for(int i = 0;i<5;i++){
				
			//	final Document document = Jsoup.connect(DH[i]).timeout(12000).get();
				Document doc = Jsoup.parse(files[i],null);
				System.out.println(doc);
				
				fw.write("==========================");
				fw.write(Name[i]);
				fw.write("==========================\n");
				
				for (Element food : doc.select("div.menusamprecipes,div.menusampmeals,div.shortmenurecipes,div.shortmenumeals,img[src$=.gif]")) {
					if(food.text().equals("Breakfast")||food.text().equals("Lunch")||food.text().equals("Dinner")){
						fw.write("\n");
					}
					fw.write(food.text());
					if (food.text().equals("")){  //finds the ingredients of the food
						String tag = food.attr("src");
						fw.write("------");
						fw.write( tag.substring(tag.indexOf('_')+7,tag.length()-4));
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