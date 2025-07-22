package com.yatra.assignment;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class YatraAssignment {

	public static void main(String[] args) throws InterruptedException {

		ChromeOptions chromeoptions = new ChromeOptions();
		chromeoptions.addArguments("--disable-notifications");
		chromeoptions.addArguments("--start-maximized");

		// Step 1 Launch the chrome browser
		WebDriver wd = new ChromeDriver(chromeoptions);
		WebDriverWait wait = new WebDriverWait(wd, Duration.ofSeconds(20));

		// Step 2 Load the page
		wd.get("https://www.yatra.com/");
		closePopup(wait);
		By Departurelocator = By.xpath("//div[@aria-label=\"Departure Date inputbox\" and @role=\"button\"]");
		WebElement Departureelement = wait.until(ExpectedConditions.elementToBeClickable(Departurelocator));
		Departureelement.click();
		WebElement currentmonthwebelement = calendermonth(wait, 0);
		WebElement nextmonthwebelement = calendermonth(wait, 1);

		// System.out.println(currentmonthelement.getText());
		Thread.sleep(2000);
		String lowestpriceforcurrentmonth =getMeLowestPrice(currentmonthwebelement);
		String lowestpricefornextmonth =getMeLowestPrice(nextmonthwebelement);
		System.out.println(lowestpriceforcurrentmonth);
		System.out.println(lowestpricefornextmonth);
		comparetwomonthsprice(lowestpriceforcurrentmonth,lowestpricefornextmonth);
	}

	public static void closePopup(WebDriverWait wait) {
		By popuplocator = By.xpath("//div[contains(@class,\"style_popup\")][1]");
		try {
		WebElement popwebelement = wait.until(ExpectedConditions.visibilityOfElementLocated(popuplocator));
		WebElement popupcloseelement = popwebelement.findElement(By.xpath(".//img[@alt=\"cross\"]"));
		popupcloseelement.click();
		}
		catch (Exception e){
			System.out.println("Pop up not shown on the screen");
		}
	}

	public static String getMeLowestPrice(WebElement monthwebelement) {
		By pricelist = By.xpath(".//span[contains(@class,\"custom-day-content \")]");
		List<WebElement> pricelistElement = monthwebelement.findElements(pricelist);
		int lowestprice = Integer.MAX_VALUE;
		WebElement priceElement = null;
		for (WebElement eachpricelist : pricelistElement) {
			// System.out.println(eachpricelist.getText());
			String priceString = eachpricelist.getText();
			if (priceString.length() > 0) {
				priceString = priceString.replace("₹", "").replace(",", "");
				// System.out.println(priceString);
				int priceInt = Integer.parseInt(priceString);
				if (priceInt < lowestprice) {

					lowestprice = priceInt;
					priceElement = eachpricelist;
				}
			}
		}
		// System.out.println(lowestprice);
		WebElement dateElement = priceElement.findElement(By.xpath(".//../.."));
		// System.out.println(dateElement.getAttribute("aria-label"));
		String result = dateElement.getAttribute("aria-label") + "---- Price is Rs" +lowestprice;
		return result;
	}

	public static WebElement calendermonth(WebDriverWait wait, int index) {

		By monthlocator = By.xpath("//div[contains(@class,\"react-datepicker__month-container\")]");

		List<WebElement> monthelement = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(monthlocator));
		System.out.println(monthelement.size());
		// focus on the current month
		WebElement currentmonthelement = monthelement.get(index);
		return currentmonthelement;
	}
	
	public static void comparetwomonthsprice(String currentmonthprice, String nextmonthprice) {
		
		int currentmonthrsindex = currentmonthprice.indexOf("Rs");	
		int nextmonthrsindex = nextmonthprice.indexOf("Rs");	
		System.out.println(currentmonthrsindex);
		System.out.println(nextmonthrsindex);
		String currentprice = currentmonthprice.substring(currentmonthrsindex+2);
		String nexttprice = nextmonthprice.substring(nextmonthrsindex+2);
	//	System.out.println(currentprice);
		int currentmontprice = Integer.parseInt(currentprice);
		int nextmontprice = Integer.parseInt(nexttprice);
		
		if(currentmontprice<nextmontprice) {
			System.out.println("The lowsest price for two months "+currentmontprice);			
		}
		else if(currentmontprice==nextmontprice){
			System.out.println("Price is same for both months");
		}
		else {
			System.out.println("The lowsest price for two months "+nextmontprice);		
		}
	}
}
