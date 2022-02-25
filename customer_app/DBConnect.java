package HotelMgmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DBConnect {
	
	Connection c = null;
	
	public DBConnect(String user, String pass) {
		//ENTER YOUR UOZONE USER AND PASS
		String user_id = user;
		String password = pass;
		
		 try {
			 Class.forName("org.postgresql.Driver");
		     this.c = DriverManager.getConnection("jdbc:postgresql://web0.eecs.uottawa.ca:15432/group_a02_g49",user_id, password);
		 } catch (Exception e) {
		     System.err.println(e.getClass().getName()+": "+e.getMessage());
		     System.out.println("Connection to the DB Failed.");
		     System.out.println("Please try again later.");
		     System.exit(0);
		 }
		 System.out.println("Opened database successfully");
		 
		 try {
			Statement stmt = null;
			 stmt = c.createStatement();
		     String sql = "SET search_path = 'public'";
		     stmt.executeUpdate(sql);
		 } catch (Exception e) {
		        e.printStackTrace();
		        System.err.println(e.getClass().getName()+": "+e.getMessage());
		        System.exit(0);
		 }
		 
	}
	
	public void closeDB() {
		try {
			this.c.close();
			System.out.println("Closed database succesfully");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
		}
	}
	
	public String[][] QueryRooms(int priceMin, int priceMax, String capacity, String extendable, String view, String arrival_date, String departure_date, String city, String amenity) throws Throwable {
		
		String[][] FunctionResults = null;

			Statement stmtb = null;
			stmtb = this.c.createStatement();
		    ResultSet countQuery = stmtb.executeQuery( "    SELECT COUNT(DISTINCT (room.brand_id,room.franchise_id,room.room_number))\r\n"
		    		+ "FROM (room NATURAL JOIN hotel_franchise), parent_hotel_brand, inst_amenity, checkDate(room.brand_id,room.franchise_id,room.room_number,'"+arrival_date+"'::date,'"+departure_date+"'::date)\r\n"
		    		+ "WHERE hotel_franchise.city = '"+city+"' AND hotel_franchise.brand_id = parent_hotel_brand.brand_id \r\n"
		    		+ "AND room.brand_id = inst_amenity.brand_id AND room.franchise_id = inst_amenity.franchise_id AND room.room_number = inst_amenity.room_number\r\n"
		    		+ "AND room.price > "+priceMin+" AND room.price < "+priceMax+"\r\n"
		    		+ "AND capacity LIKE '"+capacity+"' AND  extendable = "+extendable+" AND view LIKE '"+view+"'\r\n"
		    		+ "AND inst_amenity.amenity LIKE '"+amenity+"' AND checkDate = true " );
			
		    countQuery.next();
		    int count = countQuery.getInt("count");
		    FunctionResults = new String[count][9];
		    
			Statement stmt = null;
			stmt = this.c.createStatement();
		    ResultSet rs = stmt.executeQuery( "    SELECT DISTINCT parent_hotel_brand.brand_name, room.brand_id, room.franchise_id, room.room_number, room.price, hotel_franchise.star_category,room.capacity, room.view, hotel_franchise.street_number, hotel_franchise.street_name\r\n"
	    		+ "FROM (room NATURAL JOIN hotel_franchise), parent_hotel_brand, inst_amenity, checkDate(room.brand_id,room.franchise_id,room.room_number,'"+arrival_date+"'::date,'"+departure_date+"'::date)\r\n"
	    		+ "WHERE hotel_franchise.city = '"+city+"' AND hotel_franchise.brand_id = parent_hotel_brand.brand_id \r\n"
	    		+ "AND room.brand_id = inst_amenity.brand_id AND room.franchise_id = inst_amenity.franchise_id AND room.room_number = inst_amenity.room_number\r\n"
	    		+ "AND room.price > "+priceMin+" AND room.price < "+priceMax+"\r\n"
	    		+ "AND capacity LIKE '"+capacity+"' AND  extendable = "+extendable+" AND view LIKE '"+view+"'\r\n"
	    		+ "AND inst_amenity.amenity LIKE '"+amenity+"' AND checkDate = true " );
		    
		    if (count != 0) {
		    		System.out.println();
				  System.out.println("Displaying rooms that are between "+priceMin+"$ and "+priceMax+"$ in " + city + " with capacity "+capacity+ " and view "+view );
				  System.out.println();
				  
				  int countRes = 0;
					 System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			         System.out.printf("%-5s %-15s %-15s %-15s %-15s %-10s %-15s %-12s %-15s %-17s %-30s", "NO.", "BRAND NAME", "FRANCHISE ID", "ROOM NUMBER", "UNIT PRICE", "STAR", "CAPACITY", "VIEW", "NUMBER", "STREET", "AMENITIES");
			         System.out.println();
			         System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			         
				    while ( rs.next() ) {
				    	
				    	 String brand_name = rs.getString("brand_name");
				    	 int brand_id = rs.getInt("brand_id");
				         int child_id = rs.getInt("franchise_id");
				         int room_number  = rs.getInt("room_number");
				         int price = rs.getInt("price");
				         int star_cat = rs.getInt("star_category");
				         String cap = rs.getString("capacity");
				         String roomview = rs.getString("view");
				         int streetNum = rs.getInt("street_number");
				         String streetName = rs.getString("street_name");
				         
				         String amenityString = returnRoomAmenityStrung(brand_id,child_id,room_number);
				    			         	
				        System.out.printf("%-5d %-15s %-15s %-15s %-15s %-10s %-15s %-12s %-15s %-17s %-30s", countRes, brand_name, String.valueOf(child_id), String.valueOf(room_number), String.valueOf(price), String.valueOf(star_cat), cap, roomview, String.valueOf(streetNum), streetName, amenityString );
						System.out.println();
							
							FunctionResults[countRes][0] = String.valueOf(brand_id);
							FunctionResults[countRes][1] = String.valueOf(child_id);
							FunctionResults[countRes][2] = String.valueOf(room_number);
							FunctionResults[countRes][3] = arrival_date;
							FunctionResults[countRes][4] = departure_date;
							FunctionResults[countRes][5] = brand_name;
							FunctionResults[countRes][6] = String.valueOf(price);
							FunctionResults[countRes][7] = cap;
							FunctionResults[countRes][8] = roomview;	
							countRes++;
							
						} 

				      rs.close();
				      stmt.close();
				      System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		    } else {
		    	throw new Throwable("No Rooms Returned");
		    }

		return FunctionResults;  
	}
	
	public String returnName(String customer_sin) throws Throwable  {
		String firstname = "";
		String lastname = "";

		Statement stmt = null;
		stmt = this.c.createStatement();
		ResultSet rs2 = stmt.executeQuery( "SELECT first_name,last_name FROM customer WHERE sin_number = '"+customer_sin+"'" );
		boolean flag = rs2.next();
		if (flag) {
			firstname = rs2.getString("first_name");
			lastname = rs2.getString("last_name");
		} else {
			throw new Throwable("Result Set Empty");
		}	

		return firstname + " " + lastname;
	}
	
	public String returnRoomAmenityStrung(int brand_id, int franchise_id, int room_number) {
		
		String amenity = "none";
		try {
			Statement stmt = null;
			stmt = this.c.createStatement();
			ResultSet rs2 = stmt.executeQuery( "  SELECT amenity from inst_amenity\r\n"
					+ "WHERE brand_id = "+brand_id+" AND franchise_id = "+franchise_id+" AND room_number = "+room_number+";  " );
			
			boolean flag = rs2.next();
			if (flag) {
				amenity = rs2.getString("amenity");
				
				while (rs2.next()) {
					amenity += ", ";
					amenity += rs2.getString("amenity");
				}
			} else {
				return "none";
			}
		}  catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeDB();
		}

		return amenity;
	}
	
	public void reserveRoom(String customer_sin, String brand_id, String franchise_id, String room_number, String arrival_date, String departure_date) {
		 
		Statement stmt = null;
		try {
			stmt = this.c.createStatement();
			 ResultSet rs = stmt.executeQuery("INSERT INTO public.booking(\r\n"
					+ "	booking_number, customer_sin, brand_id, franchise_id, room_number, renting_number, arrival_date, departure_date, archived)\r\n"
					+ "	VALUES (DEFAULT, "+customer_sin+", "+brand_id+", "+franchise_id+", "+room_number+", null, '"+arrival_date+"'::date, '"+departure_date+"'::date, false) RETURNING booking_number;");
			
			 rs.next();
			 int booking_no = rs.getInt("booking_number");
			 
			System.out.println("Your reservation has been submitted succesfully!");
			System.out.println("Your booking number is " + booking_no);
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeDB();
		}
	    
		
	}
	
	public String newCustomer(String customer_sin, String first_name, String middle_initial, String last_name, String street_number, String street_name, String unit_number, String city, String province, String country, String postal_code) throws Throwable {
		 
		Statement stmt = null;
		
		if (unit_number == "") {
			unit_number = "null";
		}
		
		stmt = this.c.createStatement();
		ResultSet rs = stmt.executeQuery(" INSERT INTO public.customer(\r\n"
				+ "	sin_number, first_name, middle_initial, last_name, street_number, street_name, unit_number, city, province, country, postal_code, registration_date)\r\n"
				+ "	VALUES ("+customer_sin+", '"+first_name+"', '"+middle_initial+"', '"+last_name+"', "+street_number+", '"+street_name+"', "+unit_number+", '"+city+"', '"+province+"', '"+country+"', '"+postal_code+"', CURRENT_DATE) RETURNING sin_number; ");
			 
		boolean flag = rs.next();

		if (!flag) {
			throw new Throwable("Create Fail");
		} 
			 
		return String.valueOf(rs.getInt("sin_number"));

	}
	
	
	public String[][] viewMyRooms(String customer_sin) throws Throwable {
		
		String[][] FunctionResults = null;
		String date = Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE);

			Statement stmtb = null;
			stmtb = this.c.createStatement();
		    ResultSet countQuery = stmtb.executeQuery( " \r\n"
		    		+ "SELECT count(*)\r\n"
		    		+ "FROM (booking a NATURAL JOIN parent_hotel_brand p ), hotel_franchise f\r\n"
		    		+ "WHERE customer_sin = '"+customer_sin+"' AND arrival_date >= '"+date+"' AND archived = false \r\n"
		    		+ "AND f.brand_id = a.brand_id AND f.franchise_id = a.franchise_id " );
			
		    countQuery.next();
		    int count = countQuery.getInt("count");
		    FunctionResults = new String[count][13];
		    
			Statement stmt = null;
			stmt = this.c.createStatement();
		    ResultSet rs = stmt.executeQuery( "SELECT a.booking_number, p.brand_name, p.brand_id, a.franchise_id, a.room_number, a.arrival_date, a.departure_date,f.street_number,f.street_name,f.city,f.province,f.country,f.postal_code \r\n"
		    		+ "FROM (booking a NATURAL JOIN parent_hotel_brand p ), hotel_franchise f\r\n"
		    		+ "WHERE customer_sin = '"+customer_sin+"' AND arrival_date >= '"+date+"' AND archived = false \r\n"
		    		+ "AND f.brand_id = a.brand_id AND f.franchise_id = a.franchise_id  " );

		    if (count != 0) {

				    System.out.println("Displaying active bookings under your name.");
				    System.out.println();
				    
				     System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			         System.out.printf("%-5s %-15s %-15s %-15s %-13s %-15s %-15s %-15s %-15s %-15s %-15s %-10s", "NO.", "BOOKING NO", "BRAND NAME", "FRANCHISE ID", "ROOM NUMBER", "CHECK IN", "CHECK OUT", "NUMBER", "STREET", "CITY", "PROVINCE", "COUNTRY" );
			         System.out.println();
			         System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			         
				    int countRes = 0;
				    while ( rs.next() ) {
				    	
				    	 int booking_number = rs.getInt("booking_number");
				    	 String brand_name = rs.getString("brand_name");
				    	 int brand_id = rs.getInt("brand_id");
				         int child_id = rs.getInt("franchise_id");
				         int room_number  = rs.getInt("room_number");
				         String arrive_date = rs.getString("arrival_date");
				         String departure_date = rs.getString("departure_date");
				         int street_number = rs.getInt("street_number");
				         String street_name = rs.getString("street_name");
				         String city = rs.getString("city");
				         String province = rs.getString("province");
				         String country = rs.getString("country");
				         String postal_code = rs.getString("postal_code");
				         
				         System.out.printf("%-5d %-15s %-15s %-15s %-13s %-15s %-15s %-15s %-15s %-15s %-15s %-10s", countRes, String.valueOf(booking_number), brand_name, String.valueOf(brand_id), String.valueOf(child_id), arrive_date, departure_date,
				        		 String.valueOf(street_number), street_name, city, province, country );
				         System.out.println();

							FunctionResults[countRes][0] = String.valueOf(booking_number);
							FunctionResults[countRes][1] = brand_name;
							FunctionResults[countRes][2] = String.valueOf(brand_id);
							FunctionResults[countRes][3] = String.valueOf(child_id);
							FunctionResults[countRes][4] = String.valueOf(room_number);
							FunctionResults[countRes][5] = arrive_date;
							FunctionResults[countRes][6] = departure_date;
							FunctionResults[countRes][7] = String.valueOf(street_number);
							FunctionResults[countRes][8] = street_name;
							FunctionResults[countRes][9] = city;
							FunctionResults[countRes][10] = province;
							FunctionResults[countRes][11] = country;
							FunctionResults[countRes][12] = postal_code;
							countRes++;
						} 
				    
				      System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
				      rs.close();
				      stmt.close();
				      
		    } else {
		    	throw new Throwable("No Rooms Returned");
		    }

		return FunctionResults;	
		
	}
	
	public void cancelRoom(String booking_number) {
		 
		Statement stmt = null;
		try {
			stmt = this.c.createStatement();
			stmt.executeUpdate("UPDATE public.booking SET archived=true WHERE booking_number = "+booking_number+";");
				 
			System.out.println("Your reservation number " + booking_number + " has been cancelled!");
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeDB();
		}
	}
	
	public int returnNumHotels(String brand_name) throws Throwable {
		int num = 0;
		
			Statement stmt = null;
			stmt = this.c.createStatement();
			ResultSet rs2 = stmt.executeQuery( "SELECT number_of_hotels FROM parent_hotel_brand WHERE brand_name = '"+brand_name+"';" );
			boolean flag = rs2.next();
			if (flag) {
				num = rs2.getInt("number_of_hotels");
			} else {
				throw new Throwable("Result Set Empty");
			}
			
		return num;
	}
	
	public void queryFranchise(String brand_name, int franchise_id) {
		
		try {		    
			Statement stmt = null;
			stmt = this.c.createStatement();
		    ResultSet rs = stmt.executeQuery( " SELECT a.street_number, a.street_name, a.city, a.province, a.country, a.number_of_rooms FROM hotel_franchise a,parent_hotel_brand b\r\n"
		    		+ "WHERE a.brand_id = b.brand_id AND b.brand_name = '"+brand_name+"' AND a.franchise_id = "+franchise_id+"; " );
		    
		    rs.next();
		    	
		    int street_num = rs.getInt("street_number");
		    String street_name = rs.getString("street_name");
		    String city = rs.getString("city");
		    String province = rs.getString("province");
		    String country = rs.getString("country");
		    int numRooms = rs.getInt("number_of_rooms");
		    
			System.out.println(brand_name + "'s Franchise " + franchise_id + " is located at " + street_num + " " + street_name + " in " +city+", "+province+", " +country+".");
			System.out.println("There are " + numRooms + " rooms at this location.");
			System.out.println("This location's email addresses are: ");
		    
		} catch (Exception e) {
	        e.printStackTrace();
	        System.err.println(e.getClass().getName()+": "+e.getMessage());
	        closeDB();
	        System.exit(0);
	    }
	
		try {		    
			Statement stmt = null;
			stmt = this.c.createStatement();
		    ResultSet rs = stmt.executeQuery( " SELECT email_address FROM inst_franchise_email, parent_hotel_brand\r\n"
		    		+ "WHERE inst_franchise_email.brand_id = parent_hotel_brand.brand_id AND parent_hotel_brand.brand_name = '"+brand_name+"' AND\r\n"
		    		+ "inst_franchise_email.franchise_id = "+franchise_id+"; " );
		    
		    while ( rs.next() ) {
		    	
		    	 String email = rs.getString("email_address");
		    	 System.out.println(email);
		    }    
		} catch (Exception e) {
	        e.printStackTrace();
	        System.err.println(e.getClass().getName()+": "+e.getMessage());
	        closeDB();
	        System.exit(0);
	    }
		
		System.out.println("This location's phone numbers are: ");
		try {		    
			Statement stmt = null;
			stmt = this.c.createStatement();
		    ResultSet rs = stmt.executeQuery( " SELECT phone_number FROM inst_franchise_phone,parent_hotel_brand\r\n"
		    		+ "WHERE inst_franchise_phone.brand_id = parent_hotel_brand.brand_id AND parent_hotel_brand.brand_name = '"+brand_name+"' AND\r\n"
		    		+ "inst_franchise_phone.franchise_id = "+franchise_id+"; " );
		    
		    while ( rs.next() ) {
		    	 String phonenum = rs.getString("phone_number");
		    	 System.out.println(phonenum);
		    }    
		} catch (Exception e) {
	        e.printStackTrace();
	        System.err.println(e.getClass().getName()+": "+e.getMessage());
	        closeDB();
	        System.exit(0);
	    }
		
	}

}
