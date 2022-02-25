package HotelMgmt;

import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

@SuppressWarnings("unused")
public class HotelManagement {
	public static void main(String[] args) {

		//Open scanner mainScan to collect user input
		Scanner mainScan = new Scanner(System.in);
				
		System.out.println("Please sign in to the DB with your uozone credentials.");
		System.out.print("User: ");
		String user = mainScan.nextLine();
		
		Console cons = System.console();;
		char[] pass = cons.readPassword("Enter password");
		
		System.out.println();
		//Connect to DB through object openDB
		DBConnect openDB = new DBConnect(user,new String(pass));
		
			//Main Customer Interface
			System.out.println("*******************************************************");
			System.out.println("*******************Hotel Search Tool*******************");
			System.out.println("*******************************************************");
			
			//Initialize some variables
			boolean exceptionFlag;
			String customer_sin = null;
			String customer_name = null;
			
			System.out.println("Are you an existing customer? (0 for no, 1 for yes)");
			int userChoice = -1;
			do {
				System.out.print("Input: ");
				userChoice = mainScan.nextInt();
				mainScan.nextLine();
				if (userChoice < 0 || userChoice > 1) {
					System.out.println("Invalid Choice! Please try again.");
				}
				
			} while (userChoice < 0 || userChoice > 1);
			//If existing user
			if (userChoice == 1) {
				//Prompt user for sin to login, and query database for the user's full name. 
				do {
					exceptionFlag = false;
					System.out.print("Enter your SIN number to login : ");
					customer_sin = mainScan.nextLine();
					
					if (customer_sin.length() == 9) {
						try {
							customer_name = openDB.returnName(customer_sin);
						} catch(Throwable e) {
							System.out.println("The SIN entered is not valid. Please try again.");
							exceptionFlag = true;
						}
					} else {
						System.out.println("SIN number must be exactly 9 characters long. Please try again.");
						exceptionFlag = true;
					}
				} while (exceptionFlag == true);
				
			}
			//If New User
			else {
				System.out.println("----------------");
				System.out.println("Account Creation");
				
				do {
					System.out.print("What is your SIN number?: ");
					customer_sin = mainScan.nextLine();
					
					if (!(customer_sin.matches("[0-9]+") && customer_sin.length() == 9)) {
						System.out.println("Invalid SIN Format. Please try again.");
					}
					
				} while (!(customer_sin.matches("[0-9]+") && customer_sin.length() == 9));
				
				
				String first_name = "";
				do {
					System.out.print("What is your first name?: ");
					first_name = mainScan.nextLine();
					
					if (!(first_name.matches("[a-zA-Z]+"))) {
						System.out.println("Invalid Name Format. Please try again.");
					}
					
				} while (!(first_name.matches("[a-zA-Z]+")));
				
				
				
				String mid_init = "";
				do {
					System.out.print("What is your middle initial?: ");
					mid_init = mainScan.nextLine();
					
					if (!(mid_init.matches("[A-Z]"))) {
						System.out.println("Invalid Name Format. Please try again.");
					}
					
				} while (!(mid_init.matches("[A-Z]")));
				
				
				String last_name = "";
				do {
					System.out.print("What is your last name?: ");
					last_name = mainScan.nextLine();
					
					if (!(last_name.matches("[a-zA-Z]+"))) {
						System.out.println("Invalid Name Format. Please try again.");
					}
					
				} while (!(last_name.matches("[a-zA-Z]+")));
				
				
				
				String street_number = "";
				do {
					System.out.print("What is your street number?: ");
					street_number = mainScan.nextLine();
					
					if (!(street_number.matches("[0-9]+"))) {
						System.out.println("Not a Number. Please try again.");
					}
					
				} while (!(street_number.matches("[0-9]+")));
				
				
				
				String street_name = "";
				do {
					System.out.print("What is your street name?: ");
					street_name = mainScan.nextLine();
					
					if (!(street_name.matches("[a-zA-Z]+"))) {
						System.out.println("Invalid Street Name Format. Please try again.");
					}
					
				} while (!(street_name.matches("[a-zA-Z]+")));
				
				
				
				String unit_number = "";
				do {
					System.out.print("What is your unit number? (leave empty if none): ");
					unit_number = mainScan.nextLine();
					
					if (!(unit_number.matches("[0-9]*"))) {
						System.out.println("Not a Number. Please try again.");
					}
					
				} while (!(unit_number.matches("[0-9]*")));
				

				String city = "";
				do {
					System.out.print("What is your city?: ");
					city = mainScan.nextLine();
					
					if (!(city.matches("[a-zA-Z]+"))) {
						System.out.println("Invalid City Name Format. Please try again.");
					}
					
				} while (!(city.matches("[a-zA-Z]+")));
				
				
				String province = "";
				do {
					System.out.print("What is your province? ([A-Z][A-Z]): ");
					province = mainScan.nextLine();
					
					if (!(province.matches("[A-Z][A-Z]"))) {
						System.out.println("Invalid Province Name Format. Must be two uppercase letters only. Please try again.");
					}
					
				} while (!(province.matches("[A-Z][A-Z]")));
				

				String country = "";
				do {
					System.out.print("What is your country? ([A-Z][A-Z]): ");
					country = mainScan.nextLine();
					
					if (!(country.matches("[A-Z][A-Z]"))) {
						System.out.println("Invalid Country Name Format. Must be two uppercase letters only. Please try again.");
					}
					
				} while (!(country.matches("[A-Z][A-Z]")));
				
				
				String postal_code = "";
				do {
					System.out.print("What is your postal code?: ");
					postal_code = mainScan.nextLine();
					
					if (!(postal_code.matches("[A-Z][0-9][A-Z][0-9][A-Z][0-9]"))) {
						System.out.println("Invalid Postal Code Name Format. Must be length 6 and alternate between upper case letter and number. Please try again.");
					}
					
				} while (!(postal_code.matches("[A-Z][0-9][A-Z][0-9][A-Z][0-9]")));
				
				
				//Call new account function
				try {
					String resultOfNewCust = openDB.newCustomer(customer_sin, first_name, mid_init, last_name, street_number, street_name, unit_number, city, province, country, postal_code);
					System.out.println("Account Created Succesfully!");
				} catch (Throwable e) {
					System.out.println("Account Create Failed.");
					System.out.println("...Terminating Session...");
					//close DB
					openDB.closeDB();
					//Close Scanner
					mainScan.close();
					System.exit(0);
				}
				
				//Test If New Account
				try {
					customer_name = openDB.returnName(customer_sin);
				} catch(Throwable e) {
					System.out.println("The account was not created succesfully.");
					System.out.println("...Terminating Session...");
					//close DB
					openDB.closeDB();
					//Close Scanner
					mainScan.close();
					
					
					System.exit(0);
				}
			}
			
			//Until the user enters 0 to kill the session
			main_while_loop:
			while (true) {
					System.out.println("*******************************************************");
					//Clear Scanner Buffer
					mainScan = new Scanner(System.in);
					//Initialize 2d array to store results of DB queries.	
					String[][] roomResult = null;
					System.out.println();

					//Decision Branch A
					System.out.println(customer_name + ", please select one of the following options. ");
					System.out.println("Enter 0 to end the session.");
					System.out.println("Enter 1 to search for available rooms.");
					System.out.println("Enter 2 to view or cancel your bookings.");
					System.out.println("Enter 3 to get information from a hotel.");
					
					//Initialize decision variables to capture user input
					int inputContainer01 = 0, inputContainer02 = 0, inputContainer03 = 0, inputContainer04 = 0, inputContainer05 = 0, inputContainer06 = 0,
							inputContainer07 = 0;
					
					//Decision Section for Branch A		
					do {
						try {
							System.out.print("Input: ");
							inputContainer01 = mainScan.nextInt();
							mainScan.nextLine();
							if (inputContainer01 < 0 || inputContainer01 > 3) {
								System.out.println("Invalid Choice! Please try again.");
							}
						} catch (InputMismatchException e) {
							System.out.println("Expected: Integer. Invalid Type Provided. Returning to menu.");
							continue main_while_loop;
						}
						
					} while (inputContainer01 < 0 || inputContainer01 > 3);
					
					//If Decision to Branch A is 0
					if (inputContainer01 == 0) {
						break;
					} 
					//If Decision to Branch A is 1
					else if (inputContainer01 == 1) {
						System.out.println("****************************************");
						System.out.println("***Hotel Room Search (case sensitive)***");
						System.out.println("****************************************");
						
						////Decision Branch B
						decision_branch_b:
						do {
							exceptionFlag = false;
							//Query User for room requirements
							System.out.print("What city would you like to search for results in? : ");
							String city = mainScan.nextLine();
							
							int minPrice = 0,maxPrice = 0;
							try {
								System.out.print("What is the min price of your search? : ");
								minPrice = mainScan.nextInt();
								mainScan.nextLine();
								System.out.print("What is the max price of your search? : ");
								maxPrice = mainScan.nextInt();
							} catch (InputMismatchException e) {
								System.out.println("Expected: Integer. Invalid Type Provided. Returning to menu.");
								continue main_while_loop;
							}
							
							mainScan.nextLine();
							System.out.print("What is your desired room capacity (single/double/queen/king/% for any)? : ");
							String capacity = mainScan.nextLine();
							System.out.print("What is your desired room view (mountain/sea/none/% for any)? : ");
							String view = mainScan.nextLine();
							System.out.print("Do you require your room to be extendable (true/false)? : ");
							String extend = mainScan.nextLine();
							System.out.print("Amenity Preference (air conditioning/tv/fridge/% for any)? : ");
							String amnpref = mainScan.nextLine();
							System.out.print("Check In Date (yyyy-mm-dd)? : ");
							String checkindate = mainScan.nextLine();
							System.out.print("Check Out Date (yyyy-mm-dd)? : ");
							String checkoutdate = mainScan.nextLine();
							
							//Try to query the DB given the customers input.
							try {
								roomResult = openDB.QueryRooms(minPrice, maxPrice, capacity, extend, view, checkindate, checkoutdate,city,amnpref);
							} catch (Throwable e) {
								exceptionFlag = true;
								System.out.println("There are no rooms that match your criteria. This can either be due to invalid criteria or typing errors.");
								System.out.println("Would you like to try again (0 for no, 1 for yes)?");
								do {
									System.out.print("Input: ");
									inputContainer02 = mainScan.nextInt();
									mainScan.nextLine();
									if (inputContainer02 != 0 && inputContainer02 != 1) {
										System.out.println("Invalid Choice! Please try again.");
									} else if (inputContainer02 == 0) {
										continue main_while_loop;
									} else {
										System.out.println("Returning to room search.");
										System.out.println();
									}
								} while (inputContainer02 != 0 && inputContainer02 != 1);
							}
						} while (exceptionFlag == true);
						System.out.println();
						
						//Prompt for if a rental is to be made
						int ifBooking = 0;
						try {
							System.out.print("Would you like to reserve one of the following rooms (0 for no, 1 for yes)? : ");
							ifBooking = mainScan.nextInt();
							mainScan.nextLine();
						} catch (InputMismatchException e) {
							System.out.println("Expected: Integer. Invalid Type Provided. Returning to menu.");
							continue main_while_loop;
						}
						//If Yes
						if (ifBooking == 1) {
							
							int numberOfOptions = roomResult.length;

							do {
								try {
									System.out.print("What is the number corresponding to the room you would like to reserve? : ");
									inputContainer03 = mainScan.nextInt();
									mainScan.nextLine();
									
									if (inputContainer03 >= numberOfOptions || inputContainer03 < 0) {
										System.out.println("The entered number does not correspond to a room. Please try again.");
									}
								} catch (InputMismatchException e) {
									System.out.println("Expected: Integer. Invalid Type Provided.");
									inputContainer03 = -1;
									mainScan.nextLine();
								}
							} while (inputContainer03 >= numberOfOptions || inputContainer03 < 0);
							
							System.out.println();
							System.out.println("To confirm, you are about to reserve the following room details.");
							System.out.println("Hotel = "+roomResult[inputContainer03][5]+" , FranchiseID = "+roomResult[inputContainer03][1]+" , RoomNumber = "+roomResult[inputContainer03][2]+", Price = "+roomResult[inputContainer03][6]);
							System.out.println("Check in Date = "+roomResult[inputContainer03][3] + ", Check out Date = " + roomResult[inputContainer03][4]);
							System.out.println("Capacity = "+roomResult[inputContainer03][7] + ", View = " + roomResult[inputContainer03][8]);
							System.out.println("Amenities = " + openDB.returnRoomAmenityStrung(Integer.parseInt(roomResult[inputContainer03][0]), Integer.parseInt(roomResult[inputContainer03][1]),Integer.parseInt(roomResult[inputContainer03][02])));
							System.out.println();
							System.out.print("Would you like to proceed (0 for no, 1 for yes)? : ");

							try {
								inputContainer04 = mainScan.nextInt();
								mainScan.nextLine();
							} catch (InputMismatchException e) {
								System.out.println("Expected: Integer. Invalid Type Provided. Returning to menu.");
								mainScan.nextLine();
								continue main_while_loop;
							}
							
							if (inputContainer04 == 1) {
								System.out.println();
								openDB.reserveRoom(String.valueOf(customer_sin), roomResult[inputContainer03][0], roomResult[inputContainer03][1], roomResult[inputContainer03][2], roomResult[inputContainer03][3], roomResult[inputContainer03][4]);
							} else {
								System.out.println("Returning to menu.");
							}
							
						} 
						//If no is selected when prompted if the customer wants to make a booking
						else {
							System.out.println("Returning to menu.");
						}
					} 
					//If Decision to Branch A is 2
					else if (inputContainer01 == 2) {
						
						System.out.println("****************************************");
						System.out.println("****Active Bookings (case sensitive)****");
						System.out.println("****************************************");
					
						try {
							roomResult = openDB.viewMyRooms(String.valueOf(customer_sin));
						} catch (Throwable e) {
							System.out.println("There are no active bookings under your name.");
							continue main_while_loop;
						}
						
						System.out.print("Would you like to cancel one of these reservations (0 for no, 1 for yes)? : ");
						
						try {
							inputContainer05 = mainScan.nextInt();
							mainScan.nextLine();
						} catch (InputMismatchException e) {
							System.out.println("Expected: Integer. Invalid Type Provided. Returning to menu.");
							mainScan.nextLine();
							continue main_while_loop;
						}
						//If canceling a booking
						if (inputContainer05 == 1) {
							
							int numberOfOptions = roomResult.length;

							do {
								try {
									System.out.print("What is the number corresponding to the reservation you would like to cancel? : ");
									inputContainer06 = mainScan.nextInt();
									mainScan.nextLine();
									
									if (inputContainer06 >= numberOfOptions || inputContainer06 < 0) {
										System.out.println("The entered number does not correspond to a room. Please try again.");
									}
								} catch (InputMismatchException e) {
									System.out.println("Expected: Integer. Invalid Type Provided.");
									inputContainer06 = -1;
									mainScan.nextLine();
								}
							} while (inputContainer06 >= numberOfOptions || inputContainer06 < 0);
							
							System.out.println();
							System.out.println("To confirm, you are about to cancel the following reservation.");
							System.out.println("Booking Number = " + roomResult[inputContainer06][0] + ", Hotel = "+roomResult[inputContainer06][1]+", FranchiseID = "+roomResult[inputContainer06][3]+", RoomNumber = " +roomResult[inputContainer06][4]+", Check In Date = " + roomResult[inputContainer06][5] + ", Check Out Date = " +roomResult[inputContainer06][6]);
							System.out.println("Street Number = " + roomResult[inputContainer06][7] + ", Street Name = " +roomResult[inputContainer06][8]+", City = " + roomResult[inputContainer06][9] + ", Province = " +roomResult[inputContainer06][10]+", Country = "+roomResult[inputContainer06][11]+", Postal Code = "+roomResult[inputContainer06][12]);
							System.out.println();
							
							try {
								System.out.print("Would you like to proceed (0 for no, 1 for yes)? : ");
								inputContainer07 = mainScan.nextInt();
								mainScan.nextLine();
							} catch (InputMismatchException e) {
								System.out.println("Expected: Integer. Invalid Type Provided. Returning to menu.");
								mainScan.nextLine();
								continue main_while_loop;
							}
							//If customer wants to cancel their booking
							if(inputContainer07 == 1) {
								openDB.cancelRoom(roomResult[inputContainer06][0]);
							} else {
								System.out.println("Returning to menu.");
							}
						}
					} 
					//If Decision to Branch A is 3
					else if (inputContainer01 == 3) {
						System.out.println("****************************************");
						System.out.println("*******Hotel Franchise Directory********");
						System.out.println("****************************************");
						System.out.print("What is the name of the Hotel Brand you would like to query? : ");
						
						String nameOfHotelBrand = mainScan.nextLine();
						
						int numFranchises;
						try {
							numFranchises = openDB.returnNumHotels(nameOfHotelBrand);
						} catch (Throwable e) {
							System.out.println("Hotel Franchise name not found. Returning to main menu");
							continue main_while_loop;
						}
						
						System.out.print(nameOfHotelBrand + " has " + numFranchises + " locations. Which Franchise ID would you like information on? : ");
						int numChoice6 = mainScan.nextInt();
						mainScan.nextLine();
						
						System.out.println();
						
						openDB.queryFranchise(nameOfHotelBrand, numChoice6);
						
					} 
					//If Decision to Branch A is neither 0,1,2,3
					else {
						System.out.println("Invalid Option. Terminating Session.");
						break;
					}

			}
			
			//Close DB
			System.out.println("...Terminating Session...");
			openDB.closeDB();
			//Close Scanner
			mainScan.close();
	}
}


