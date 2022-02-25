package HotelMgmt;

import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

@SuppressWarnings("unused")
public class HotelManagement {
	
	private static int userSin;
	private static int bID;
	private static int fID;
	
	
	public static void main(String[] args) {
		
			Scanner mainScan = new Scanner(System.in);
			
			System.out.println("Employee Admin Console");
			System.out.println("Please sign in to the DB with your uozone credentials.");
			System.out.print("User: ");
			String user = mainScan.nextLine();
			
			Console cons = System.console();
			char[] pass = cons.readPassword("Enter password");
			
			System.out.println();
			//Connect to DB through object openDB
			DBConnect openDB = new DBConnect(user,new String(pass));

			
			System.out.println("Please enter your SIN to authenticate: ");
			userSin = mainScan.nextInt();
			try {
				bID = openDB.getBrandID(userSin);
				fID = openDB.getFranchiseID(userSin);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			
			while (true) {
				System.out.print("Enter 0 to end session.\nEnter 1 for the hotel room services.\nEnter 2 to create a renting.\nEnter 3 for the customer management.\nEnter 4 for the hotel franchise directory\nENTER: ");
				
				int userInput = mainScan.nextInt();
				
				if (userInput == 0) {
					break;
				} else if (userInput == 1) {
					System.out.println("****************************************");
					System.out.println("**********Hotel Room Services***********");
					System.out.println("****************************************");
					System.out.print("Enter 0 to end session.\nEnter a to search for hotel rooms with a date.\nEnter b to search for a hotel room wihout a date.\nEnter c to find all bookings/rentings by room.\nEnter d to modify room specs.\nEnter e to add/remove ameneties.\nENTER: ");
					String userInput2 = mainScan.next();
					if(userInput2.equals("0")) {
						break;
					}
					else if(userInput2.equals("a")) {
						System.out.println("Hotel Room Search **CASE SENSITIVE**");
						System.out.print("Min Price? : ");
						int minP = mainScan.nextInt();
						mainScan.nextLine();
						
						System.out.print("Max Price? : ");
						int maxP = mainScan.nextInt();
						mainScan.nextLine();
						
						System.out.print("Capacity? (single/double/queen/king    % for any) : ");
						String capacity = mainScan.nextLine();

						System.out.print("View? (mountain/sea/none    % for any) : ");
						String view = mainScan.nextLine();
						
						System.out.print("Check In Date? (yyyy-mm-dd) : ");
						String checkindate = mainScan.nextLine();
								
						System.out.print("Check Out Date? (yyyy-mm-dd) : ");
						String checkoutdate = mainScan.nextLine();
					
						try {
							openDB.QueryRooms(minP,maxP,capacity,view,checkindate,checkoutdate);
						}catch(Exception e) {
							e.printStackTrace();
					        System.err.println(e.getClass().getName()+": "+e.getMessage());
					        openDB.closeDB();
					        System.out.println("Database connection closed");
						}
					}
					
					else if(userInput2.equals("b")) {
						System.out.println("Hotel Room Search (Without Date) **CASE SENSITIVE**");
						System.out.print("Min Price? : ");
						int minPw = mainScan.nextInt();
						mainScan.nextLine();
						
						System.out.print("Max Price? : ");
						int maxPw = mainScan.nextInt();
						mainScan.nextLine();
						
						System.out.print("Capacity? (single/double/queen/king    % for any) : ");
						String capacityw = mainScan.nextLine();

						System.out.print("View? (mountain/sea/none    % for any) : ");
						String vieww = mainScan.nextLine();
					
						try {
							openDB.QueryRoomsWithoutDate(minPw,maxPw,capacityw,vieww);
						}catch(Exception e) {
							e.printStackTrace();
					        System.err.println(e.getClass().getName()+": "+e.getMessage());
					        openDB.closeDB();
					        System.out.println("Database connection closed");
						}
					}
					else if(userInput2.equals("c")) {
						System.out.println("Find All Bookings/Rentings By Room");
						
						System.out.print("Room Number? : ");
						int roomnumberf = mainScan.nextInt();
						mainScan.nextLine();
						try {
							openDB.FindBookings(bID, fID, roomnumberf);
						}catch(SQLException e1) {
							e1.printStackTrace();
						}
					}
					else if(userInput2.equals("d")) {
						System.out.println("Modify Room Specs");
						
						System.out.print("Room Number? : ");
						int roomnumberm = mainScan.nextInt();
						mainScan.nextLine();
						
						System.out.print("New Price? (Leave blank for no change) : ");
						String newPrice = mainScan.nextLine();
						mainScan.nextLine();
						System.out.print("New Capacity? (Leave blank for no change) : ");
						String newCapacity = mainScan.nextLine();
						mainScan.nextLine();
						System.out.print("New Extendable? (true or false, leave blank for no change) : ");
						String newExtendable = mainScan.nextLine();
						mainScan.nextLine();
						System.out.print("New View? (Leave blank for no change) : ");
						String newView = mainScan.nextLine();
						mainScan.nextLine();
						try {
							openDB.ModifySpecs(bID, fID, roomnumberm, newPrice, newCapacity, newExtendable, newView);
						}catch(Exception e) {
							e.printStackTrace();
					        System.err.println(e.getClass().getName()+": "+e.getMessage());
					        openDB.closeDB();
					        System.out.println("Database connection closed");
						}
					}
						
					else if(userInput2.equals("e")) {
						System.out.println("Modify Room Amenites");
						
						System.out.print("Room Number? : ");
						int roomnumbera = mainScan.nextInt();
						
						System.out.print("Enter 1 to add an amenity, 2 to delete an amenity");
						int choice = mainScan.nextInt();
							if(choice ==1) {
								System.out.print("Enter the new amenity : ");
								String newAmenity = mainScan.next();
								
								try {
									openDB.AddAmenity(bID, fID, roomnumbera, newAmenity);
								}catch(Exception e) {
									e.printStackTrace();
							        System.err.println(e.getClass().getName()+": "+e.getMessage());
							        openDB.closeDB();
							        System.out.println("Database connection closed");
								}
								
							}else if(choice == 2) {
								System.out.print("Enter the amenity to delete : ");
								String deleteAmenity = mainScan.next();
								
								try {
									openDB.DeleteAmenity(bID, fID, roomnumbera, deleteAmenity);
								}catch(Exception e) {
									e.printStackTrace();
							        System.err.println(e.getClass().getName()+": "+e.getMessage());
							        openDB.closeDB();
							        System.out.println("Database connection closed");
								}
						}
						
						
					}
					
					
					
				} else if (userInput == 2) {
					System.out.println("****************************************");
					System.out.println("**********Hotel Room Renting************");
					System.out.println("****************************************");
					
					System.out.print("Customer Sin?  : ");
					int cust_sin = mainScan.nextInt();
					mainScan.nextLine();
					
					System.out.print("Room Number? : ");
					int roomnumber = mainScan.nextInt();
					mainScan.nextLine();
					
					System.out.print("Booking Number? (leave blank if no prior booking) : ");
					String booknumber = mainScan.nextLine();
					
					System.out.print("Check In Date? (yyyy-mm-dd) : ");
					String checkindate = mainScan.nextLine();
							
					System.out.print("Check Out Date? (yyyy-mm-dd) : ");
					String checkoutdate = mainScan.nextLine();
					
					try {
						openDB.EmployeeRentingRoom(cust_sin,userSin,bID,fID,roomnumber,booknumber,checkindate,checkoutdate);
					}catch(Exception e) {
						e.printStackTrace();
				        System.err.println(e.getClass().getName()+": "+e.getMessage());
				        openDB.closeDB();
				        System.out.println("Database connection closed");
					}
					
				}else if(userInput == 3) {
					System.out.println("****************************************");
					System.out.println("**********Customer Management***********");
					System.out.println("****************************************");
					System.out.println("Enter 0 to end session.\nEnter a to search for customer data.\nEnter b to update customer address.\nEnter c to view all reservations by customer SIN");
					String userInput2 = mainScan.next();
					if(userInput2.equals("0")) {
						break;
					}else if(userInput2.equals("a")) {
						System.out.println("Search Customer");
						System.out.println("Enter customer SIN:");
						int c_sin = mainScan.nextInt();
						try {
							openDB.GetCustomerData(c_sin);
						}catch(SQLException e1) {
							e1.printStackTrace();
					        System.err.println(e1.getClass().getName()+": "+e1.getMessage());
					        openDB.closeDB();
					        System.out.println("Database connection closed");
						
						}
					}else if(userInput2.equals("b")) {
						System.out.println("Update Customer Address");
						System.out.println("Enter customer SIN:");
						int c_sin = mainScan.nextInt();
						
						System.out.println("New Street Number: ");
						int street_number = mainScan.nextInt();
						
						System.out.println("New Street Name: ");
						String street_name = mainScan.next();
						
						System.out.println("New Unit Number: ");
						int unit_number = mainScan.nextInt();
						
						System.out.println("New City: ");
						String city = mainScan.next();
						
						System.out.println("New Province: ");
						String province = mainScan.next();
						
						System.out.println("New Country: ");
						String country = mainScan.next();
						
						System.out.println("New Postal Code: ");
						String postal_code = mainScan.next();
						
						try {
							openDB.EditCustomerAddress(c_sin, street_number, street_name, unit_number, city, province, country, postal_code);
						}catch(SQLException e1) {
							e1.printStackTrace();
					        System.err.println(e1.getClass().getName()+": "+e1.getMessage());
					        openDB.closeDB();
					        System.out.println("Database connection closed");
						}
						
						
					}
					else if(userInput2.equals("c")) {
						System.out.println("View Reservations by Customer");
						System.out.println("Enter customer SIN:");
						int sin = mainScan.nextInt();
						try {
							openDB.GetReservationsByCustomer(sin);
						}catch(SQLException e1) {
							e1.printStackTrace();
					        System.err.println(e1.getClass().getName()+": "+e1.getMessage());
					        openDB.closeDB();
					        System.out.println("Database connection closed");
						}
						
					}
				}else if(userInput == 4) {
					System.out.println("****************************************");
					System.out.println("*******Hotel Franchise Directory********");
					System.out.println("****************************************");
					System.out.print("What is the name of the Hotel Brand you would like to query? : ");
					
					String nameOfHotelBrand = mainScan.next();
					
					int numFranchises = -1;
					try {
						numFranchises = openDB.returnNumHotels(nameOfHotelBrand);
					} catch (Throwable e) {
						System.out.println("Hotel Franchise name not found. Returning to main menu");
					}
					
					if(numFranchises >= 0) {
						System.out.print(nameOfHotelBrand + " has " + numFranchises + " locations. Which Franchise ID would you like information on? : ");
						int numChoice6 = mainScan.nextInt();
						mainScan.nextLine();
						
						System.out.println();
						
						openDB.queryFranchise(nameOfHotelBrand, numChoice6);
					}
			
		}
		
			
			
			
			
			
	   

}
			openDB.closeDB();
}
}


